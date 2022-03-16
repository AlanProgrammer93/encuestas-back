package com.capitan.encuestas.repositories;

import com.capitan.encuestas.entities.PollEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends CrudRepository<PollEntity, Long> {
    public PollEntity findByPollId(String pollId);

    public PollEntity findById(long id);

    public Page<PollEntity> findAllByUserId(long userId, Pageable pageable);

    PollEntity findByPollIdAndUserId(String pollId, long userId);
}
