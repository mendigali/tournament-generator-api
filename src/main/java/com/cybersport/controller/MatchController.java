package com.cybersport.controller;

import com.cybersport.model.Match;
import com.cybersport.service.MatchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("{id}")
    public Match getMatch(@PathVariable Long id) {
        return matchService.getMatchById(id);
    }

    @PostMapping
    public Match createMatch(@RequestBody Match match) {
        return matchService.createMatch(match);
    }

    @PutMapping("{id}")
    public Match updateMatch(@PathVariable Long id, @RequestBody Match updatedMatch) {
        return matchService.updateMatch(id, updatedMatch);
    }

    @DeleteMapping("{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
    }
}
