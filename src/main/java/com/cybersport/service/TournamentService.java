package com.cybersport.service;

import com.cybersport.model.Tournament;
import com.cybersport.repository.MatchRepository;
import com.cybersport.repository.TeamRepository;
import com.cybersport.repository.TournamentRepository;
import com.cybersport.util.TournamentState;
import com.cybersport.util.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public TournamentService(
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository
    ) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Long id) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(id);
        return tournamentOptional
                .orElseThrow(() -> new RuntimeException("Tournament was not found! ID: " + id));
    }

    public Tournament createTournament(Tournament tournament) {
        if (!Utils.isPowerOfTwo(tournament.getBracketSize())) {
            throw new RuntimeException("Bracket size must be power of 2!");
        }
        tournament.setState(TournamentState.UPCOMING);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournament(Long id, Tournament updatedTournament) {
        return tournamentRepository.findById(id)
                .map(tournament -> {
                    if (updatedTournament.getBracketSize() != null &&
                        !Utils.isPowerOfTwo(updatedTournament.getBracketSize())) {
                        throw new RuntimeException("Bracket size must be power of 2!");
                    }

                    tournament.setName(
                            Optional.ofNullable(updatedTournament.getName())
                                    .orElse(tournament.getName())
                    );

                    tournament.setBracketSize(
                            Optional.ofNullable(updatedTournament.getBracketSize())
                                    .orElse(tournament.getBracketSize())
                    );

                    tournament.setState(
                            Optional.ofNullable(updatedTournament.getState())
                                    .orElse(tournament.getState())
                    );

                    tournament.setTeams(
                            Optional.ofNullable(updatedTournament.getTeams())
                                    .orElse(tournament.getTeams())
                    );

                    tournament.setMatches(
                            Optional.ofNullable(updatedTournament.getMatches())
                                    .orElse(tournament.getMatches())
                    );

                    return tournamentRepository.save(tournament);
                }).orElseThrow(() -> new RuntimeException("Tournament was not found! ID: " + id));
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }
}
