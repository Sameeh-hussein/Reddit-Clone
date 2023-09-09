package com.sameeh.springit.service;

import com.sameeh.springit.Domain.Role;
import com.sameeh.springit.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
