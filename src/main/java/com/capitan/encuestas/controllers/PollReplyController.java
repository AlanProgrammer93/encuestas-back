package com.capitan.encuestas.controllers;

import javax.validation.Valid;

import com.capitan.encuestas.models.requests.PollReplyRequestModel;
import com.capitan.encuestas.services.PollReplyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polls/reply")
public class PollReplyController {

    @Autowired
    PollReplyService pollReplyService;
    
    @PostMapping
    public void replyPoll(@RequestBody @Valid PollReplyRequestModel model) {
        pollReplyService.createPollReply(model);
    }

}
