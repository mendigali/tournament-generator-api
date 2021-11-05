package com.cybersport.util;

import com.cybersport.model.Match;
import com.cybersport.model.Participant;
import com.cybersport.model.Tournament;
import com.cybersport.repositories.MatchRepository;
import com.cybersport.repositories.ParticipantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static Integer totalMatches;
    private static Integer matchesPlayed = 0;

    public static String tournamentNotFoundMessage(Long id) {
        return "Can't find tournament with id: " + id + "!";
    }

    public static String participantNotFoundMessage() {
        return "Can't find participant!";
    }

    public static String matchNotFoundMessage(Long id) {
        return "Can't find match with id: " + id + "!";
    }

    public static void matchParticipants(Tournament tournament,
                                         MatchRepository matchRepository,
                                         List<Participant> participants,
                                         ParticipantRepository participantRepository) {
        int i = 0;
        int totalParticipants = participants.size();
        while (i < totalParticipants) {
            while (i < totalParticipants && !participants.get(i).isActive()) {
                ++i;
            }

            if (i == totalParticipants) {
                break;
            }

            int firstParticipantIndex = i++;

            while (i < totalParticipants && !participants.get(i).isActive()) {
                ++i;
            }

            if (i == totalParticipants) {
                break;
            }

            int secondParticipantIndex = i++;

            Participant firstParticipant = participants.get(firstParticipantIndex);
            Participant secondParticipant = participants.get(secondParticipantIndex);

            Match firstMatch = firstParticipant.getMatch();
            Match secondMatch = secondParticipant.getMatch();

            Match match = new Match();

            firstParticipant.setMatch(match);
            secondParticipant.setMatch(match);

            if (firstMatch.getStartTime() == null) {
                matchRepository.delete(firstMatch);
            }

            if (secondMatch.getStartTime() == null) {
                matchRepository.delete(secondMatch);
            }

            match.setStartTime(LocalDateTime.now());

            List<Participant> matchesParticipants = new ArrayList<>();
            matchesParticipants.add(firstParticipant);
            matchesParticipants.add(secondParticipant);
            match.setFirstParticipantId(firstParticipant.getId());
            match.setSecondParticipantId(secondParticipant.getId());
            match.setParticipants(matchesParticipants);
            match.setTournament(tournament);

            matchRepository.save(match);

            firstParticipant.setMatch(match);
            secondParticipant.setMatch(match);

            participantRepository.save(firstParticipant);
            participantRepository.save(secondParticipant);
        }

        int activeParticipants = 0;
        for (Participant participant : participants) {
            if (participant.isActive()) {
                activeParticipants += 1;
            }
        }

        Utils.setTotalMatches(activeParticipants / 2);
    }

    public static Integer getTotalMatches() {
        return totalMatches;
    }

    public static void setTotalMatches(Integer totalMatches) {
        Utils.totalMatches = totalMatches;
    }

    public static Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public static void setMatchesPlayed(Integer matchesPlayed) {
        Utils.matchesPlayed = matchesPlayed;
    }
}
