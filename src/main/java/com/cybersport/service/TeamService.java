package com.cybersport.service;

import com.cybersport.model.Team;
import com.cybersport.repository.MatchRepository;
import com.cybersport.repository.TeamRepository;
import com.cybersport.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TeamService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public TeamService(
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository
    ) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        Optional<Team> teamOptional = teamRepository.findById(id);
        return teamOptional
                .orElseThrow(() -> new RuntimeException("User was not found! ID: " + id));
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Team updatedTeam) {
        return teamRepository.findById(id)
                .map(team -> {
                   team.setName(
                           Optional.ofNullable(updatedTeam.getName())
                                   .orElse(team.getName())
                   );

                    return teamRepository.save(team);
                }).orElseThrow(() -> new RuntimeException("Team was not found! ID: " + id));
    }
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}
