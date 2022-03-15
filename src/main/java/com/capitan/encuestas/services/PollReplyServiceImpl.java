package com.capitan.encuestas.services;

import java.util.HashSet;
import java.util.Set;

import com.capitan.encuestas.entities.PollEntity;
import com.capitan.encuestas.entities.PollReplyDetailEntity;
import com.capitan.encuestas.entities.PollReplyEntity;
import com.capitan.encuestas.models.requests.PollReplyRequestModel;
import com.capitan.encuestas.repositories.PollReplyRepository;
import com.capitan.encuestas.repositories.PollRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PollReplyServiceImpl implements PollReplyService {

    PollReplyRepository pollReplyRepository;
    PollRepository pollRepository;

    public PollReplyServiceImpl(PollReplyRepository pollReplyRepository, PollRepository pollRepository) {
        this.pollReplyRepository = pollReplyRepository;
        this.pollRepository = pollRepository;
    }

    @Override
    public void createPollReply(PollReplyRequestModel model) {
        
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setAmbiguityIgnored(true);

        PollReplyEntity pollReply = mapper.map(model, PollReplyEntity.class);

        PollEntity poll = pollRepository.findById(model.getPoll());

        pollReply.setPoll(poll);

        //Map<Long, Long> uniqueReplies = new HashMap<>();
        Set<Long> uniqueReplies = new HashSet<>();
        
        for (PollReplyDetailEntity pollReplyDetailEntity: pollReply.getPollReplies()) {
            pollReplyDetailEntity.setPollReply(pollReply);
            uniqueReplies.add(pollReplyDetailEntity.getQuestionId());
        }

        if (uniqueReplies.size() != poll.getQuestions().size()) {
            throw new RuntimeException("You must answer all the questions");
        }

        pollReplyRepository.save(pollReply);
    }
    
}
