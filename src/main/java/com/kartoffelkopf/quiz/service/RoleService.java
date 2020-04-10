package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Role;

import java.util.Optional;

public interface RoleService {
    Iterable<Role> findAll();
    Optional<Role> findById(long id);
    Role save(Role role);
    void delete(Role role);
    Role getDefaultRole();
}
