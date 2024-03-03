package com.malekkamoua.meet5.fraudservice.service;

import com.malekkamoua.meet5.fraudservice.constant.AppConstant;
import com.malekkamoua.meet5.fraudservice.models.Fraud;
import com.malekkamoua.meet5.fraudservice.models.Interaction;
import com.malekkamoua.meet5.fraudservice.repository.FraudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FraudService {

    @Autowired
    private FraudRepository fraudRepository;
    private static final Map<String, Long> userHistory = new HashMap<>();
    private static final Map<String, Integer> interactionsCount = new HashMap<>();

    @Transactional
    public void saveFraud(Fraud fraud) {
        fraudRepository.saveFraud(fraud);
    }

    public boolean isFraudulentInteraction(Interaction interaction) {
        String interactorId = interaction.getInteractorID();
        
        restartCalculation(interaction.getInteractorID());
        int actionsCount = interactionsCount.getOrDefault(interaction.getInteractorID(), 0);
        interactionsCount.put(interaction.getInteractorID(), actionsCount + 1);

        return actionsCount > AppConstant.MAX_INTERACTIONS;
    }


    private void restartCalculation(String userId) {
        long now = System.currentTimeMillis();
        long userHistoryTime = userHistory.getOrDefault(userId, now);

        if (now - userHistoryTime >= AppConstant.TIME_IN_MINUTES * 60 * 1000) {
            userHistory.put(userId, now);
            interactionsCount.put(userId, 0);
            System.out.println("Counts reset for user: " + userId);
        }
    }
}
