package com.example.springboot.app.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NotEmpty
public class User {

    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    private String firstName;

    private String lastName;

    private String email;

    private List<Role> roles;

    private Integer attempts;

}
