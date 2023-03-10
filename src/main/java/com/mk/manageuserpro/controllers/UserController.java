package com.mk.manageuserpro.controllers;

import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.service.GroupService;
import com.mk.manageuserpro.service.JapanLevelService;
import com.mk.manageuserpro.service.RoleService;
import com.mk.manageuserpro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final GroupService groupService;

    private final RoleService roleService;
    private final JapanLevelService japanLevelService;

    @Autowired
    public UserController(
            UserService userService,
            GroupService groupService,
            RoleService roleService,
            JapanLevelService japanLevelService) {
        this.userService = userService;
        this.groupService = groupService;
        this.roleService = roleService;
        this.japanLevelService = japanLevelService;
    }

    @GetMapping({"/", "/users"})
    public String showListUser(
            Model model,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        Page<User> listUserPaging = userService.getTotalUsers(name, groupId, page - 1);

        model.addAttribute("seachName", name.trim());
        model.addAttribute("seachGroupId", groupId);
        model.addAttribute("listGroup", groupService.findAll());
        model.addAttribute("listUser", listUserPaging.getContent());
        model.addAttribute("totalPage", listUserPaging.getTotalPages());
        model.addAttribute("currentPage", listUserPaging.getPageable().getPageNumber() + 1);
        return "home";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("listGroup", groupService.findAll());
        model.addAttribute("listRole", roleService.findAll());
        model.addAttribute("listJapanLevel", japanLevelService.findAll());
        return "add_edit_user";
    }

    @PostMapping("/addUser")
    public String addUser(
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        User userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            for (FieldError errorFieldUserDetailJapan : bindingResult.getFieldErrors("userDetailJapan")) {
                if("RequiredOnInputJapanLevel".equals(errorFieldUserDetailJapan.getCode())) {
                    bindingResult
                            .rejectValue("userDetailJapan.", "error.user",
                                    "There is already a user registered with the user name provided");
                }
            }

            model.addAttribute("listGroup", groupService.findAll());
            model.addAttribute("listRole", roleService.findAll());
            model.addAttribute("listJapanLevel", japanLevelService.findAll());
            return "add_edit_user";
        } else {
            userService.saveUser(user);
            model.addAttribute("successMessage", "User has been registered successfully");
            return "info";
        }
    }
}
