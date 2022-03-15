package com.capitan.encuestas.repositories;

import com.capitan.encuestas.entities.PollEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends CrudRepository<PollEntity, Long> {
    public PollEntity findByPollId(String pollId);

    public PollEntity findById(long id);
}
