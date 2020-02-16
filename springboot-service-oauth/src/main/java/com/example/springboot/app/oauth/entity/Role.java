package com.example.springboot.app.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NotEmpty
public class Role {

    private Long id;

    private String name;

}
