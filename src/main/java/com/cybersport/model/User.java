package com.cybersport.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String email;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public Role getRoles() {
        return role;
    }
}
