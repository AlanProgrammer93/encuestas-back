package com.capitan.encuestas.services;

import com.capitan.encuestas.entities.PollEntity;
import com.capitan.encuestas.models.requests.PollCreationRequestModel;

public interface PollService {
    public String createPoll(PollCreationRequestModel model, String email);

    public PollEntity getPoll(String pollId);
}
