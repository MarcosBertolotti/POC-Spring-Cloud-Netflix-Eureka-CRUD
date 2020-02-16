package com.example.springboot.app.users.commons.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, length = 20)
    //@NotEmpty
    //@Size(min = 2, max = 20)
    private String username;

    @Column(length = 60)
    //@NotEmpty
    //@Size(min = 2, max = 60)
    private String password;

    private Boolean enabled;

    @Column(name = "first_name")
    //@NotEmpty
    private String firstName;

    @Column(name = "last_name")
    //@NotEmpty
    private String lastName;

    @Column(unique = true)
    //@NotEmpty
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)      // consulta fetch de tipo lazy, solo trae al user sin los roles
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name="user_id"),     // cambiamos el nombre de la clase intermedia y nombres de las foreign key
            inverseJoinColumns = @JoinColumn(name="role_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})})      // generar una constraint unica en la tabla
    private List<Role> roles;

    private Integer attempts;

    private static final long serialVersionUID = 1L;
}
