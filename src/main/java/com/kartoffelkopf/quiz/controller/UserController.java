package com.kartoffelkopf.quiz.controller;

import com.kartoffelkopf.quiz.model.Role;
import com.kartoffelkopf.quiz.model.User;
import com.kartoffelkopf.quiz.service.RoleService;
import com.kartoffelkopf.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

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
        //TODO: Possibly remove this check once everything is finished
        if (StreamSupport.stream(roleService.findAll().spliterator(), false).count() == 0) {
            Role userRole = new Role("USER", true);
            roleService.save(userRole);
        }
        user.setRole(roleService.getDefaultRole());
        userService.save(user);
        return "redirect:/login";
    }

    @RequestMapping("/user/edit")
    private String editForm(Model model, Principal principal) {
        User user = (User)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        model.addAttribute("user", user);
        return "user/edit";
    }

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public String edit(@RequestParam String username,
                       @RequestParam String firstName,
                       @RequestParam String lastName,
                       @RequestParam String password,
                       @RequestParam String confirmPassword,
                       Principal principal) {
        User user = userService.findByUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (!password.isEmpty()) {
            String passwordHash = passwordEncoder.encode(password);
            user.setPassword(passwordHash);
        }
        user.setEnabled(true);
        user.setRole(roleService.getDefaultRole());
        userService.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
