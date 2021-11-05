package com.cybersport.model;

import com.cybersport.util.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private UserRole name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(UserRole name) {
        this.name = name;
    }
}
