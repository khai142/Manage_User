package com.mk.manageuserpro.controllers;

import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.service.GroupService;
import com.mk.manageuserpro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomePage {
  private final UserService userService;
  private final GroupService groupService;

  @Autowired
  public HomePage(UserService userService, GroupService groupService) {
    this.userService = userService;
    this.groupService = groupService;
  }

  @RequestMapping({"/", "users"})
  public String showHomePage(
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
}
