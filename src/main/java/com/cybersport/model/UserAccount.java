package com.cybersport.model;

import com.cybersport.util.UserRole;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAccountId;

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
