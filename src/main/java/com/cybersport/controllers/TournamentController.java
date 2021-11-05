package com.cybersport.controllers;

import com.cybersport.exception.TournamentException;
import com.cybersport.model.Tournament;
import com.cybersport.service.TournamentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TournamentController {
    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/tournaments")
    public List<Tournament> get() {
        return tournamentService.findAllTournaments();
    }


    @GetMapping("/tournaments/{id}")
    public Tournament get(@PathVariable Long id) throws TournamentException {
        return tournamentService.findTournamentById(id);
    }

    @PostMapping("/tournaments")
    public Tournament create(@RequestBody Tournament tournament) throws TournamentException {
        return tournamentService.createTournament(tournament);
    }

    @PutMapping("/tournaments/start/{id}")
    public String start(@PathVariable Long id) throws TournamentException {
        return tournamentService.startTournament(id);
    }

    @PutMapping("/tournaments/hold/{id}")
    public String hold(@PathVariable Long id) throws TournamentException {
        return tournamentService.holdTournament(id);
    }

    @PutMapping("/tournaments/{id}")
    public Tournament update(@PathVariable Long id,
                             @RequestBody Tournament tournamentUpdated) throws TournamentException {
        return tournamentService.updateTournament(id, tournamentUpdated);
    }

    @DeleteMapping("/tournaments/{id}")
    public String remove(@PathVariable Long id) throws TournamentException {
        return tournamentService.removeTournament(id);
    }
}
