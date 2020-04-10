package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {
}
