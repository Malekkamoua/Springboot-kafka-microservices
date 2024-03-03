package com.malekkamoua.meet5.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.malekkamoua.meet5.userservice.Constants.AppConstant;
import com.malekkamoua.meet5.userservice.models.*;
import com.malekkamoua.meet5.userservice.models.kafka.InteractionResponse;
import com.malekkamoua.meet5.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.malekkamoua.meet5.userservice.validators.UserValidation.validateUser;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    KafkaTemplate<String, Interaction> kafkaTemplate;

    @GetMapping(path = "/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            validateUser(user, false);
            if (userService.isUsernameUnique(user.getUsername())) {
                userService.createUser(user);
                return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Username is not unique", HttpStatus.CONFLICT);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        try {
            validateUser(updatedUser, true);
            if (userService.userExists(id)) {
                userService.updateUser(id, updatedUser);
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            if (userService.userExists(id)) {
                userService.deleteUser(id);
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //This endpoint aggregates both like and visit actions (interactionType) that user A (interactorID) makes on user B (interactedWithID)
    @PostMapping("/interact")
    public ResponseEntity<String> interact(@RequestBody Interaction interaction) {
        try {
            kafkaTemplate.send(AppConstant.USER_INTERACTIONS, interaction);
            return new ResponseEntity<>(interaction.getInteractionType() + " action received and processed successfully.", HttpStatus.OK);
        } catch (KafkaException e) {
            return new ResponseEntity<>("Failed to process the like action. Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @KafkaListener(topics = AppConstant.FRAUD)
    public ResponseEntity<String> consume(Fraud fraud) {
        try {
            Long fraudulentUserId = fraud.getFraudulentUserId();
            userService.disableUser(fraudulentUserId);
            return new ResponseEntity<>("Fraudulent activity. Blocked user" + userService.getUserById(fraudulentUserId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private final ObjectMapper objectMapper = new ObjectMapper();
    private String interactionMessage;

    @GetMapping("/profile/{id}/visitors")
    public ResponseEntity<List<Visitor>> getAllVisitors(@PathVariable String id) throws JsonProcessingException {
        if (interactionMessage != null) {
            List<InteractionResponse> interactionResponses = objectMapper.readValue(interactionMessage, new TypeReference<List<InteractionResponse>>() {});
            List<Visitor> allVisitors = new ArrayList<>();
            for (InteractionResponse interaction: interactionResponses) {

                if(id.equals(interaction.getInteracted_with_id()) && AppConstant.INTERACTION_TYPE_VISIT.equals(interaction.getInteractionType())) {
                    User user =  userService.getUserById(Long.parseLong(interaction.getInteractor_id()));
                    if (user != null) {
                        //Has All visits by username, gender and visit time
                        allVisitors.add(new Visitor(user.getUsername(), user.getGender(), interaction.getTimeStamp()));
                    }
                }
            }
            return new ResponseEntity<>(allVisitors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @KafkaListener(topics = AppConstant.VISIT_INTERACTIONS)
    public ResponseEntity<String> consumeVisitorsInfo(String message) throws JsonProcessingException {
        interactionMessage = message;
        try{
            List<InteractionResponse> interactionResponses = objectMapper.readValue(interactionMessage, new TypeReference<List<InteractionResponse>>() {});
            List<Notification> notificationList = new ArrayList<>();

            for (InteractionResponse interaction: interactionResponses) {

                String originalString = AppConstant.INTERACTION_TYPE_VISIT.equals(interaction.getInteractionType()) ?  AppConstant.NOTIFICATIONS_TYPE_VISIT : AppConstant.NOTIFICATIONS_TYPE_LIKE;
                String timestampToDate = interaction.getTimeStamp().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                String notifMsg  = originalString
                        .replace("#0", userService.getUserById(Long.parseLong(interaction.getInteractor_id())).getUsername())
                        .replace("#1", timestampToDate);
                Notification notification = new Notification(Long.parseLong(interaction.getInteracted_with_id()), notifMsg);
                notificationList.add(notification);
            }
            userService.insertBatchNotifications(notificationList);
            return new ResponseEntity<>("Notifications added with success", HttpStatus.OK);

        }catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @KafkaListener(topics = AppConstant.LIKE_INTERACTIONS)
    public ResponseEntity<String> consumeLikesInfo(String message) {
        interactionMessage = message;
        try{
            saveNotifications(interactionMessage);
        }catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<String> saveNotifications(String interactionMessage) throws JsonProcessingException {
        List<InteractionResponse> interactionResponses = objectMapper.readValue(interactionMessage, new TypeReference<List<InteractionResponse>>() {});
        List<Notification> notificationList = new ArrayList<>();

        for (InteractionResponse interaction: interactionResponses) {

            String originalString = AppConstant.INTERACTION_TYPE_VISIT.equals(interaction.getInteractionType()) ?  AppConstant.NOTIFICATIONS_TYPE_VISIT : AppConstant.NOTIFICATIONS_TYPE_LIKE;
            String timestampToDate = interaction.getTimeStamp().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            String notifMsg  = originalString
                    .replace("#0", userService.getUserById(Long.parseLong(interaction.getInteractor_id())).getUsername())
                    .replace("#1", timestampToDate);
            Notification notification = new Notification(Long.parseLong(interaction.getInteracted_with_id()), notifMsg);
            notificationList.add(notification);
        }
        userService.insertBatchNotifications(notificationList);
        System.out.print("saved notif");
        return new ResponseEntity<>("Notifications added with success", HttpStatus.OK);
    }
}