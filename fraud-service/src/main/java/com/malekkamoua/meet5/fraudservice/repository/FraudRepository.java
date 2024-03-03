package com.malekkamoua.meet5.fraudservice.repository;

import com.malekkamoua.meet5.fraudservice.models.Fraud;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FraudRepository {
    private final JdbcTemplate jdbcTemplate;

    public FraudRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveFraud (Fraud fraud) {
        String sql = "INSERT INTO frauds (fraudulent_user_id, cause, timestamp) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, fraud.getFraudulentUserId(), fraud.getCause(), fraud.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
