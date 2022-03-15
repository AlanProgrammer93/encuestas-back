package com.capitan.encuestas.repositories;

import java.util.List;

import com.capitan.encuestas.entities.PollReplyEntity;

import org.springframework.data.repository.CrudRepository;

public interface PollReplyRepository extends CrudRepository<PollReplyEntity, Long> {
    public List<PollReplyEntity> findAll();
}
