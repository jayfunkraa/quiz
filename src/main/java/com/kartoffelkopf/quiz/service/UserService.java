package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
