package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.data.RoleDao;
import com.kartoffelkopf.quiz.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Override
    public Iterable<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Optional<Role> findById(long id) {
        return roleDao.findById(id);
    }

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }

    @Override
    public void delete(Role role) {
        roleDao.delete(role);
    }

    @Override
    public Role getDefaultRole() {
        List<Role> roles = new ArrayList<>();
        for (Role r : roleDao.findAll()) {
            if (r.isDefaultRole()) {
                roles.add(r);
            }
        }
        return roles.get(0);
    }
}
