package com.cybersport.repositories;

import com.cybersport.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByTournamentId(Long id);
}
