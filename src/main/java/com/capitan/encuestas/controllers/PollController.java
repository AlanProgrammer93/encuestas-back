package com.capitan.encuestas.controllers;

import javax.validation.Valid;

import com.capitan.encuestas.entities.PollEntity;
import com.capitan.encuestas.models.requests.PollCreationRequestModel;
import com.capitan.encuestas.models.responses.CreatedPollRest;
import com.capitan.encuestas.models.responses.PollRest;
import com.capitan.encuestas.services.PollService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polls")
public class PollController {

    @Autowired
    PollService pollService;
    
    @PostMapping
    public CreatedPollRest createPoll(@RequestBody @Valid PollCreationRequestModel model, Authentication authentication) {
        String pollId = pollService.createPoll(model, authentication.getPrincipal().toString());
        return new CreatedPollRest(pollId);
    }

    @GetMapping(path = "{id}/questions")
    public PollRest getPollWithQuestions(@PathVariable String id) {
        PollEntity poll = pollService.getPoll(id);

        ModelMapper mapper = new ModelMapper();

        return mapper.map(poll, PollRest.class);
    }
}
