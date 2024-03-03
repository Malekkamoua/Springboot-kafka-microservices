package com.malekkamoua.meet5.interactionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malekkamoua.meet5.interactionservice.constants.AppConstant;
import com.malekkamoua.meet5.interactionservice.models.Interaction;
import com.malekkamoua.meet5.interactionservice.models.kafka.InteractionResponse;
import com.malekkamoua.meet5.interactionservice.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;
    @Autowired
    private KafkaTemplate<String, List<Interaction>> kafkaProdTemplate;

    //Listens to user-interactions-topic and saves the interactions (Likes, visits) accordingly
    @KafkaListener(topics = AppConstant.USER_INTERACTIONS, groupId = AppConstant.USER_INTERACTIONS_GROUP)
    public String consume(String interactionObject) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            InteractionResponse interactionResponse = objectMapper.readValue(interactionObject, InteractionResponse.class);

            Interaction interaction = new Interaction(interactionResponse.getInteractorID(), interactionResponse.getInteractedWithID(), interactionResponse.getInteractionType());
            interactionService.saveInteraction(interaction);

            //Refresh message
            List<Interaction>  likeInteractions =  interactionService.getInteractionsByIdAndType(interactionResponse.getInteractorID(), AppConstant.INTERACTION_TYPE_LIKE);
            kafkaProdTemplate.send(AppConstant.LIKE_INTERACTIONS, likeInteractions);

            List<Interaction>  visitInteractions =  interactionService.getInteractionsByIdAndType(interactionResponse.getInteractorID(), AppConstant.INTERACTION_TYPE_VISIT);
            kafkaProdTemplate.send(AppConstant.VISIT_INTERACTIONS, visitInteractions);

            return "Interaction" + interaction.toString() + "saved.";
        } catch (Exception e) {
            return "An unexpected error occurred. Error: " + e.getMessage();
        }
    }

    /*
    @GetMapping("/user/{userId}/visit")
    public ResponseEntity<String> getAllVisitors(@PathVariable String userId) {
        try {
            List<Interaction>  visits =  interactionService.getInteractionsByIdAndType(userId, AppConstant.INTERACTION_TYPE_VISIT);
            kafkaProdTemplate.send(AppConstant.INTERACTIONS, visits);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (KafkaException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}/like")
    public ResponseEntity<String> getAllLikes(@PathVariable String userId) {
        try {
            List<Interaction>  likes =  interactionService.getInteractionsByIdAndType(userId, AppConstant.INTERACTION_TYPE_LIKE);
            kafkaProdTemplate.send(AppConstant.INTERACTIONS, likes);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (KafkaException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}
