package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
