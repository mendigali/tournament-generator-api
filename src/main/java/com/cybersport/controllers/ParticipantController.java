package com.cybersport.controllers;

import com.cybersport.exception.TournamentException;
import com.cybersport.model.Participant;
import com.cybersport.service.ParticipantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/tournaments/{tournamentId}/participants")
    public List<Participant> get(@PathVariable Long tournamentId) throws TournamentException {
        return participantService.findAllParticipants(tournamentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tournaments/{tournamentId}/participants")
    public Participant add(@PathVariable Long tournamentId,
                           @RequestBody Participant participant) throws TournamentException {
        return participantService.addParticipant(tournamentId, participant);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tournaments/{tournamentId}/participants/{participantId}")
    public Participant update(@PathVariable Long tournamentId,
                              @PathVariable Long participantId,
                              @RequestBody Participant participantUpdated) throws TournamentException {
    	return participantService.updateParticipant(tournamentId, participantId, participantUpdated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tournaments/{tournamentId}/participants/{participantId}")
    public String remove(@PathVariable Long tournamentId,
                         @PathVariable Long participantId) throws TournamentException {
    	return participantService.removeParticipant(tournamentId, participantId);
    }
}
