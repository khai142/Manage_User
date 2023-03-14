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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

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
        bindingDataDropDown(model);
        return "add_edit_user";
    }

    @PostMapping("/addUser")
    public String addUser(
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        User userExists = userService.findByUsername(user.getUsername());
        boolean isAddUser = user.getId() == null;
//      Username cannot be the same as another user
        if (isAddUser && userExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            messageSource.getMessage("err.username.exist", null, LocaleContextHolder.getLocale()));
        }
        validatePassword(user, bindingResult);
        validateUserDetailJapan(user, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingDataDropDown(model);
            return "add_edit_user";
        } else {
            userService.createUser(user);
            model.addAttribute("message", messageSource.getMessage("msg.add.success", null,
                    LocaleContextHolder.getLocale()));
            return "info";
        }
    }

    private void validatePassword(User user, BindingResult bindingResult) {
        String password = user.getPassword();
        if (Common.isEmpty(password)) {
            bindingResult
                    .rejectValue("password", "error.user",
                            messageSource.getMessage("err.password.required", null, LocaleContextHolder.getLocale()));
        } else if (password.length() < 5) {
            bindingResult
                    .rejectValue("password", "error.user",
                            messageSource.getMessage("err.password.min_length", null, LocaleContextHolder.getLocale()));
        } else if (!password.equals(user.getPasswordConfirm())) {
            bindingResult
                    .rejectValue("passwordConfirm", "error.user",
                            messageSource.getMessage("err.password_confirm.wrong", null, LocaleContextHolder.getLocale()));
        }
    }

    private void validateUserDetailJapan(User user, BindingResult bindingResult) {
        UserDetailJapan userDetailJapan = user.getUserDetailJapan();
        if (!Common.isEmpty(user.getUserDetailJapan().getJapanLevel().getCodeLevel())) {
            Date starDate = userDetailJapan.getStartDate();
            Date endDate = userDetailJapan.getEndDate();
            if (starDate == null || endDate == null) {
                if (starDate == null) {
                    bindingResult
                            .rejectValue("userDetailJapan.startDate", "error.user",
                                    messageSource.getMessage("err.start_date.required", null,
                                            LocaleContextHolder.getLocale()));
                }
                if (endDate == null) {
                    bindingResult
                            .rejectValue("userDetailJapan.endDate", "error.user",
                                    messageSource.getMessage("err.end_date.required", null,
                                            LocaleContextHolder.getLocale()));
                }
            } else if (endDate.before(starDate)) {      // Require starDate greater than endDate
                bindingResult
                        .rejectValue("userDetailJapan.endDate", "error.user",
                                messageSource.getMessage("err.end_date.compare", null,
                                        LocaleContextHolder.getLocale()));

            }
            if (userDetailJapan.getScore() == null) {
                bindingResult
                        .rejectValue("userDetailJapan.score", "error.user",
                                messageSource.getMessage("err.score.required", null, LocaleContextHolder.getLocale()));
            }
        }
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable(name = "id", required = true) Long userId, Model model) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            model.addAttribute("message", messageSource.getMessage("msg.user_not_exist", null,
                    LocaleContextHolder.getLocale()));
            return "info";
        } else {
            model.addAttribute("user", user.get());
            return "user_info";
        }
    }

    @GetMapping("/user/{id}/edit")
    public String showEditUserPage(@PathVariable(value = "id", required = true) Long userId, Model model) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            model.addAttribute("message", messageSource.getMessage("msg.user_not_exist", null,
                    LocaleContextHolder.getLocale()));
            return "info";
        } else {
            model.addAttribute("user", user.get());
            bindingDataDropDown(model);
            return "add_edit_user";
        }
    }

    @PostMapping("/user/{id}/edit")
    public String editUser(
            @PathVariable(name = "id") Long userId,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
//      User must be exist to edit
        if (userService.findById(userId).isEmpty()) {
            model.addAttribute("message", messageSource.getMessage("msg.user_not_exist", null,
                    LocaleContextHolder.getLocale()));
            return "info";
        }
        if (user.isUpdatePasswordFlag()) {
            validatePassword(user, bindingResult);
        }
        validateUserDetailJapan(user, bindingResult);
        bindingResult.resolveMessageCodes("error.user", "password");
        if (bindingResult.hasErrors()) {
            bindingDataDropDown(model);
            return "add_edit_user";
        } else {
            userService.editUser(user, userId);
            model.addAttribute("message", messageSource.getMessage("msg.edit.success", null,
                    LocaleContextHolder.getLocale()));
            return "info";
        }
    }

    private void bindingDataDropDown(Model model) {
        model.addAttribute("listGroup", groupService.findAll());
        model.addAttribute("listRole", roleService.findAll());
        model.addAttribute("listJapanLevel", japanLevelService.findAll());
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable(value = "id", required = true) Long userId, Model model) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            model.addAttribute("message", messageSource.getMessage("msg.user_not_exist", null,
                    LocaleContextHolder.getLocale()));
        } else {
            userService.deleteUserById(userId);
            model.addAttribute("message", messageSource.getMessage("msg.delete.success", null,
                    LocaleContextHolder.getLocale()));
        }
        return "info";
    }
}
