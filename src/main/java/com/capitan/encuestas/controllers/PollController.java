package com.capitan.encuestas.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import com.capitan.encuestas.entities.PollEntity;
import com.capitan.encuestas.models.requests.PollCreationRequestModel;
import com.capitan.encuestas.models.responses.CreatedPollRest;
import com.capitan.encuestas.models.responses.PaginatedPollRest;
import com.capitan.encuestas.models.responses.PollRest;
import com.capitan.encuestas.services.PollService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public PaginatedPollRest getPolls(@RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit,
        Authentication authentication
    ) {
        Page<PollEntity> paginatedPolls = pollService.getPolls(page, limit, authentication.getPrincipal().toString());
        
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(PollEntity.class, PollRest.class).addMappings(m -> m.skip(PollRest::setQuestions));
        
        PaginatedPollRest paginatedPollRest = new PaginatedPollRest();

        paginatedPollRest.setPolls(
            paginatedPolls.getContent().stream().map(p -> mapper.map(p, PollRest.class)).collect(Collectors.toList())
        );

        paginatedPollRest.setTotalPages(paginatedPolls.getTotalPages());
        paginatedPollRest.setTotalRecords(paginatedPolls.getTotalElements());
        paginatedPollRest.setCurrentPageRecords(paginatedPolls.getNumberOfElements());
        paginatedPollRest.setCurrentPage(paginatedPolls.getPageable().getPageNumber() + 1);

        return paginatedPollRest;
    }

    @PatchMapping(path = "/{id}")
    public void togglePollOpened(@PathVariable String id, Authentication authentication) {
        pollService.togglePollOpened(id, authentication.getPrincipal().toString());
    }

    @DeleteMapping(path = "/{id}")
    public void togglePoll(@PathVariable String id, Authentication authentication) {
        pollService.deletePoll(id, authentication.getPrincipal().toString());
    }
}
