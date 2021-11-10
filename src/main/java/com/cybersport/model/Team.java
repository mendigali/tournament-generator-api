package com.cybersport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "teams")
    private Set<Tournament> tournaments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team1", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Match> matches1 = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team2", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Match> matches2 = new HashSet<>();
}
