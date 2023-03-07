package com.mk.manageuserpro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePage {
  
  @RequestMapping("/home")
  public String welcome() {
    return "index";
  }
}
