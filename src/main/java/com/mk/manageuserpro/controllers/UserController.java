package com.mk.manageuserpro.controllers;

import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.model.UserDetailJapan;
import com.mk.manageuserpro.service.GroupService;
import com.mk.manageuserpro.service.JapanLevelService;
import com.mk.manageuserpro.service.RoleService;
import com.mk.manageuserpro.service.UserService;
import com.mk.manageuserpro.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final GroupService groupService;

    private final RoleService roleService;
    private final JapanLevelService japanLevelService;

    private final MessageSource messageSource;

    @Autowired
    public UserController(
            UserService userService,
            GroupService groupService,
            RoleService roleService,
            JapanLevelService japanLevelService, MessageSource messageSource) {
        this.userService = userService;
        this.groupService = groupService;
        this.roleService = roleService;
        this.japanLevelService = japanLevelService;
        this.messageSource = messageSource;
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
                            messageSource.getMessage("err.username.exist", null, LocaleContextHolder.getLocale()));
        }
        UserDetailJapan userDetailJapan = user.getUserDetailJapan();
        if (!Common.isEmpty(user.getUserDetailJapan().getJapanLevel().getCodeLevel())) {
            Date starDate = userDetailJapan.getStartDate();
            Date endDate = userDetailJapan.getEndDate();
            if (starDate == null || endDate == null) {
                if (starDate == null) {
                    bindingResult
                            .rejectValue("userDetailJapan.startDate", "error.user",
                                    messageSource.getMessage("err.start_date.required", null, LocaleContextHolder.getLocale()));
                }
                if (endDate == null) {
                    bindingResult
                            .rejectValue("userDetailJapan.endDate", "error.user",
                                    messageSource.getMessage("err.end_date.required", null, LocaleContextHolder.getLocale()));
                }
            } else if (endDate.before(starDate)) {      // Require starDate greater than endDate
                bindingResult
                        .rejectValue("userDetailJapan.endDate", "error.user",
                                messageSource.getMessage("err.end_date.compare", null, LocaleContextHolder.getLocale()));

            }
            if (userDetailJapan.getScore() == null) {
                bindingResult
                        .rejectValue("userDetailJapan.score", "error.user",
                                messageSource.getMessage("err.score.required", null, LocaleContextHolder.getLocale()));
            }

        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("listGroup", groupService.findAll());
            model.addAttribute("listRole", roleService.findAll());
            model.addAttribute("listJapanLevel", japanLevelService.findAll());
            return "add_edit_user";
        } else {
            userService.createUser(user);
            model.addAttribute("successMessage", messageSource.getMessage("msg.add.success", null, LocaleContextHolder.getLocale()));
            return "info";
        }
    }
}
