package com.capitan.encuestas.services;

import java.util.List;

import com.capitan.encuestas.entities.PollEntity;
import com.capitan.encuestas.interfaces.PollResult;
import com.capitan.encuestas.models.requests.PollCreationRequestModel;

import org.springframework.data.domain.Page;

public interface PollService {
    public String createPoll(PollCreationRequestModel model, String email);

    public PollEntity getPoll(String pollId);

    public Page<PollEntity> getPolls(int page, int limit, String email);

    public void togglePollOpened(String pollId, String email);

    public void deletePoll(String pollId, String email);

    public List<PollResult> getResults(String pollId, String email);
}
