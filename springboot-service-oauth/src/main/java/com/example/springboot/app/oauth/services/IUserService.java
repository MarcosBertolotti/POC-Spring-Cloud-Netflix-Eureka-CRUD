package com.example.springboot.app.oauth.services;

import com.example.springboot.app.oauth.entity.User;

public interface IUserService {

    User findByUsername(String username);

    User update(User user, Long id);
}
