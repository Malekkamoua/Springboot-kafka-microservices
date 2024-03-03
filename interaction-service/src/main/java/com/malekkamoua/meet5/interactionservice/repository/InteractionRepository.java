package com.malekkamoua.meet5.interactionservice.repository;

import com.malekkamoua.meet5.interactionservice.models.Interaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InteractionRepository {

    private final JdbcTemplate jdbcTemplate;

    public InteractionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveInteraction(Interaction interaction) {
        String sql = "INSERT INTO interactions (interactor_id, interacted_with_id, interaction_type) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, interaction.getInteractor_id(), interaction.getInteracted_with_id(), interaction.getInteractionType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Interaction> getInteractionsByIdAndType(String id, String type) {
        String sql = "SELECT * FROM interactions WHERE interactor_id = ? AND interaction_type = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Interaction.class), id, type);
    }

}
