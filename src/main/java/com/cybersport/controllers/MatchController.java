package com.cybersport.controllers;

import com.cybersport.exception.TournamentException;
import com.cybersport.model.Match;
import com.cybersport.service.MatchService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    @GetMapping("/tournaments/{tournamentId}/matches")
    public List<Match> get(@PathVariable Long tournamentId) throws TournamentException {
        return matchService.findAllMatches(tournamentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tournaments/{tournamentId}/matches/{matchId}")
    public Match summarizeMatch(@PathVariable Long tournamentId,
                                @PathVariable Long matchId,
                                @RequestBody Map<String, Object> scores
                                )
            throws TournamentException {
        Long firstParticipantScore = ((Number) scores.get("firstParticipantScore")).longValue();
        Long secondParticipantScore = ((Number) scores.get("secondParticipantScore")).longValue();
        return matchService.summarizeMatch(tournamentId, matchId, firstParticipantScore, secondParticipantScore);
    }
}
