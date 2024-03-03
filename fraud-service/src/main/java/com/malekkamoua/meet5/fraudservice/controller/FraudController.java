package com.malekkamoua.meet5.fraudservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malekkamoua.meet5.fraudservice.constant.AppConstant;
import com.malekkamoua.meet5.fraudservice.models.Fraud;
import com.malekkamoua.meet5.fraudservice.models.Interaction;
import com.malekkamoua.meet5.fraudservice.service.FraudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping ("/fraud")
public class FraudController {

    @Autowired
    private FraudService fraudService;
    @Autowired
    private KafkaTemplate<String, Fraud> kafkaTemplate;

    @KafkaListener(topics = AppConstant.USER_INTERACTIONS, groupId = AppConstant.FRAUD_CONS_GROUP)
    public String consume(String interactionObject) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Interaction interaction = objectMapper.readValue(interactionObject, Interaction.class);

            if(fraudService.isFraudulentInteraction(interaction)) {

                String suspectId = interaction.getInteractorID();
                String cause = "Like".equals(interaction.getInteractionType()) ? AppConstant.LIKE_FRAUD:  AppConstant.VISIT_FRAUD;

                Fraud fraud = new Fraud(suspectId, cause);
                fraudService.saveFraud(fraud);

                kafkaTemplate.send(AppConstant.FRAUD, fraud);
            }
            return "Fraudulent activity. Blocked user";
        } catch (Exception e) {
            return "An unexpected error occurred. Error: " + e.getMessage();
        }
    }

}
