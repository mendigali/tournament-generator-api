package com.cybersport.model;

import com.cybersport.util.TournamentState;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentId;

    private String name;
    private Integer bracketSize;

    @Enumerated(EnumType.STRING)
    private TournamentState state;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "tournament_team",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams;

    @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Match> matches;
}