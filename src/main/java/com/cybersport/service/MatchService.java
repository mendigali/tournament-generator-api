package com.cybersport.service;

import com.cybersport.model.Match;
import com.cybersport.repository.MatchRepository;
import com.cybersport.repository.TeamRepository;
import com.cybersport.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public MatchService(
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository
    ) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public Match getMatchById(Long id) {
        Optional<Match> matchOptional = matchRepository.findById(id);
        return matchOptional
                .orElseThrow(() -> new RuntimeException("Match was not found! ID: " + id));
    }

    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    public List<Match> createAllMatches(List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    public Match updateMatch(Long id, Match updatedMatch) {
        return matchRepository.findById(id)
                .map(match -> {
                    match.setTeam1(Optional.ofNullable(updatedMatch.getTeam1())
                            .orElse(match.getTeam1())
                    );

                    match.setTeam1_score(
                            Optional.ofNullable(updatedMatch.getTeam1_score())
                                    .orElse(match.getTeam1_score())
                    );

                    match.setTeam2(Optional.ofNullable(updatedMatch.getTeam2())
                            .orElse(match.getTeam2())
                    );

                    match.setTeam2_score(
                            Optional.ofNullable(updatedMatch.getTeam2_score())
                                    .orElse(match.getTeam2_score())
                    );

                    match.setRoundNumber(
                            Optional.ofNullable(updatedMatch.getRoundNumber())
                                    .orElse(match.getRoundNumber())
                    );

                    return matchRepository.save(match);
                }).orElseThrow(() -> new RuntimeException("Match was not found! ID: " + id));
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
