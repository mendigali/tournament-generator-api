package com.cybersport.service;

import com.cybersport.exception.TournamentException;
import com.cybersport.model.Match;
import com.cybersport.model.Participant;
import com.cybersport.model.Tournament;
import com.cybersport.repositories.ParticipantRepository;
import com.cybersport.repositories.TournamentRepository;
import com.cybersport.util.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;

    public ParticipantService(TournamentRepository tournamentRepository, ParticipantRepository participantRepository) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
    }

    public List<Participant> findAllParticipants(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentException(Utils.tournamentNotFoundMessage(tournamentId));
        }

        return participantRepository.findByTournamentId(tournamentId);
    }

    public Participant addParticipant(Long tournamentId, Participant participant) {
        return tournamentRepository.findById(tournamentId)
                .map(tournament -> {
                    if (tournament.getParticipants().size() < tournament.getMaxParticipants()) {
                        participant.setActive(true);
                        participant.setMatch(new Match());
                        participant.setTournament(tournament);

                        return participantRepository.save(participant);
                    }

                    return null;
                }).orElseThrow(() -> new TournamentException(Utils.tournamentNotFoundMessage(tournamentId)));
    }

    public Participant updateParticipant(Long tournamentId,
                                         Long participantId,
                                         Participant participantUpdated) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentException(Utils.tournamentNotFoundMessage(tournamentId));
        }

        return participantRepository.findById(participantId)
                .map(participant -> {
                    participant.setId(participantUpdated.getId());
                    participant.setName(participantUpdated.getName());
                    participant.setActive(participantUpdated.isActive());

                    return participantRepository.save(participant);
                }).orElseThrow(() -> new TournamentException(Utils.participantNotFoundMessage()));
    }

    public String removeParticipant(Long tournamentId, Long participantId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentException(Utils.tournamentNotFoundMessage(tournamentId));
        }

        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if (tournamentOptional.isEmpty()) {
            throw new TournamentException(Utils.tournamentNotFoundMessage(tournamentId));
        }

        Tournament tournament = tournamentOptional.get();

        if (!tournament.isOnHold()) {
            return "Can't delete participant, because tournament is not on hold!";
        }

        return participantRepository.findById(participantId)
                .map(participant -> {
                    participantRepository.delete(participant);

                    return "Participant deleted successfully!";
                }).orElseThrow(() -> new TournamentException(Utils.participantNotFoundMessage()));
    }
}
