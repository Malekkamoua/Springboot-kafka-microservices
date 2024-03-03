package com.malekkamoua.meet5.userservice.validators;

import com.malekkamoua.meet5.userservice.models.User;

public class UserValidation {

    public static void validateUser(User user, boolean isUpdate) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if(!isUpdate) {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("email is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getAge() < 0 ) {
            throw new IllegalArgumentException("Age is required");
        }
        if (user.getGender() == null || user.getGender().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }
    }
}
