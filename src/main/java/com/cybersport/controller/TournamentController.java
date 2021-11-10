package com.cybersport.controller;

import com.cybersport.model.Match;
import com.cybersport.model.Team;
import com.cybersport.model.Tournament;
import com.cybersport.service.MatchService;
import com.cybersport.service.TeamService;
import com.cybersport.service.TournamentService;
import com.cybersport.util.TournamentState;
import com.cybersport.util.Utils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TeamService teamService;
    private final MatchService matchService;

    public TournamentController(TournamentService tournamentService,
                                TeamService teamService,
                                MatchService matchService) {
        this.tournamentService = tournamentService;
        this.teamService = teamService;
        this.matchService = matchService;
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("{id}")
    public Tournament getTournament(@PathVariable Long id) {
        return tournamentService.getTournamentById(id);
    }

    @GetMapping("{tournamentId}/team")
    public List<Team> getTeamsByTournament(@PathVariable Long tournamentId) {
        return tournamentService.getTournamentById(tournamentId).getTeams();
    }

    @PostMapping
    public Tournament createTournament(@RequestBody Tournament tournament) {
        return tournamentService.createTournament(tournament);
    }

    @PostMapping("{tournamentId}/team/{teamId}")
    public Tournament addTeamToTournament(@PathVariable Long tournamentId,
                                          @PathVariable Long teamId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        if (tournament.getState() == TournamentState.UPCOMING) {
            List<Team> teams = tournament.getTeams();
            if (teams.size() >= tournament.getBracketSize()) {
                throw new RuntimeException("Tournament teams are full!");
            }
            for (Team t : teams) {
                if (Objects.equals(t.getTeamId(), teamId)) {
                    throw new RuntimeException("Team already in the tournament!");
                }
            }
            teams.add(teamService.getTeamById(teamId));
            tournament.setTeams(teams);

            return tournamentService.updateTournament(tournamentId, tournament);
        }
        return tournament;
    }

    @PutMapping("{id}")
    public Tournament updateTournament(@PathVariable Long id, @RequestBody Tournament updatedTournament) {
        return tournamentService.updateTournament(id, updatedTournament);
    }

    @PutMapping("{id}/start")
    public Tournament startTournament(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        if (tournament.getState() == TournamentState.UPCOMING) {
            tournament.setState(TournamentState.ONGOING);
            List<Team> teams = tournament.getTeams();
            Collections.shuffle(teams);
            List<Match> matches = new ArrayList<>();
            Integer round = 1,
                    matchAmount = 1,
                    roundsNumber = Utils.log2(tournament.getBracketSize());
            while (round <= roundsNumber) {
                for (int i = 0, j = 0; i < matchAmount; i++, j += 2) {
                    Match match = new Match();
                    match.setRoundNumber(round);
                    match.setTeam1_score(0);
                    match.setTeam2_score(0);
                    match.setTeam1(null);
                    match.setTeam2(null);
                    match.setTournament(tournament);
                    if (round.equals(roundsNumber)) {
                        match.setTeam1(tournament.getTeams().get(j));
                        match.setTeam2(tournament.getTeams().get(j + 1));
                    }
                    matches.add(match);
                }
                matchAmount *= 2;
                round++;
            }
            tournament.setMatches(matchService.createAllMatches(matches));
            tournamentService.updateTournament(id, tournament);
        }
        return tournament;
    }

    @PutMapping("{tournamentId}/match/{matchId}")
    public Tournament matchResult(@PathVariable Long tournamentId,
                                  @PathVariable Long matchId,
                                  @RequestBody Match matchResult) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        if (tournament.getState() == TournamentState.ONGOING) {
            if (matchResult.getTeam1_score() + matchResult.getTeam2_score() > 30 ||
                    !(matchResult.getTeam1_score() == 16 || matchResult.getTeam2_score() == 16)) {
                throw new RuntimeException(
                        "Incorrect number of rounds! One of teams must have 16 rounds and total must be <=30"
                );
            }
            Collections.sort(tournament.getMatches(), (m1, m2) -> (int) (m2.getMatchId() - m1.getMatchId()));
            for (Match m : tournament.getMatches()) {
                if (m.getRoundNumber() > matchService.getMatchById(matchId).getRoundNumber()
                        && m.getTeam1_score() + m.getTeam2_score() < 16) {
                    throw new RuntimeException("Previous round is not over yet!");
                }
            }
            matchResult = matchService.updateMatch(matchId, matchResult);
            if (matchResult.getRoundNumber() != 1) {
                Collections.sort(tournament.getMatches(), (m1, m2) -> (int) (m1.getMatchId() - m2.getMatchId()));

                Team winner = (matchResult.getTeam1_score() > matchResult.getTeam2_score())
                        ? matchResult.getTeam1()
                        : matchResult.getTeam2();

                List<Match> currentRoundMatches = new ArrayList<>();
                List<Match> nextRoundMatches = new ArrayList<>();
                for (Match m : tournament.getMatches()) {
                    if (m.getRoundNumber().equals(matchResult.getRoundNumber())) {
                        currentRoundMatches.add(m);
                    }
                    if (m.getRoundNumber().equals(matchResult.getRoundNumber() - 1)) {
                        nextRoundMatches.add(m);
                    }
                }

                int currentMatchId = 1, nextMatchId = 1;
                for (Match m : currentRoundMatches) {
                    currentMatchId++;
                    if (m.getMatchId().equals(matchResult.getMatchId())) {
                        break;
                    }
                }
                for (Match m : nextRoundMatches) {
                    if (nextMatchId == (currentMatchId / 2)) {
                        if (currentMatchId % 2 == 0) {
                            m.setTeam1(winner);
                        } else {
                            m.setTeam2(winner);
                        }
                        matchService.updateMatch(m.getMatchId(), m);
                        break;
                    }
                    nextMatchId++;
                }
            }
            tournament = tournamentService.getTournamentById(tournamentId);
        }
        return tournament;
    }

    @DeleteMapping("{tournamentId}/team/{teamId}")
    public Tournament deleteTournament(@PathVariable Long tournamentId,
                                       @PathVariable Long teamId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        if (tournament.getState() == TournamentState.UPCOMING) {
            List<Team> teams = tournament.getTeams();
            int index = 0;
            for (Team t : teams) {
                if (Objects.equals(t.getTeamId(), teamId)) {
                    break;
                }
                index++;
            }
            teams.remove(index);
            tournament.setTeams(teams);
            return tournamentService.updateTournament(tournamentId, tournament);
        }
        return tournament;
    }
}