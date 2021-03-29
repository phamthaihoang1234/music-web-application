package com.codegym.music.service;

import com.codegym.music.model.Role;

public interface RoleService {
    Role findByName(String name);

    Role save(Role role);
}
