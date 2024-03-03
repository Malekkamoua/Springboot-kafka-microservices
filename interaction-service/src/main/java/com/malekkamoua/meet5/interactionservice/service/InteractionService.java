package com.malekkamoua.meet5.interactionservice.service;

import com.malekkamoua.meet5.interactionservice.models.Interaction;
import com.malekkamoua.meet5.interactionservice.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;
    private final JdbcTemplate jdbcTemplate;

    public InteractionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveInteraction(Interaction interaction) {
        interactionRepository.saveInteraction(interaction);
    }

    public List<Interaction> getInteractionsByIdAndType(String id, String type) {
        return interactionRepository.getInteractionsByIdAndType(id, type);
    }

}
