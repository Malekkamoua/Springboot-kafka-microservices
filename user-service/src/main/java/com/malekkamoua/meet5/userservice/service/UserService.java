package com.malekkamoua.meet5.userservice.service;

import com.malekkamoua.meet5.userservice.models.Notification;
import com.malekkamoua.meet5.userservice.models.User;
import com.malekkamoua.meet5.userservice.repository.NotificationRepository;
import com.malekkamoua.meet5.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private NotificationRepository notificationRepository;

    public List<User> getAllUsers() {
        return  userRepository.getAllUsers();
    }

    public User getUserById(Long userId) {
        return  userRepository.getUserByCriteria("id", userId);
    }

    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.createUser(user);
    }

    public boolean isUsernameUnique(String username) {
        return userRepository.isUsernameUnique(username);
    }

    public boolean userExists(String id) {
        return userRepository.userExists(id);
    }

    public void updateUser(String id, User updatedUser) {
        userRepository.updateUser(id, updatedUser);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    public void disableUser(Long fraudulentUserId) {
        userRepository.disableUser(fraudulentUserId);
    }

    public void insertBatchNotifications(List<Notification> notifications) {
         notificationRepository.insertBatchNotifications(notifications);
    }

}
