package com.capitan.encuestas.services;

import com.capitan.encuestas.models.requests.PollReplyRequestModel;

public interface PollReplyService {
    public void createPollReply(PollReplyRequestModel model);
}
