package com.kartoffelkopf.quiz.controller;

import com.kartoffelkopf.quiz.model.User;
import com.kartoffelkopf.quiz.service.RoleService;
import com.kartoffelkopf.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/user/add")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add(User user, @RequestParam String confirmPassword) {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        user.setEnabled(true);
        user.setRole(roleService.getDefaultRole());
        userService.save(user);
        return "redirect:/login";
    }
}
