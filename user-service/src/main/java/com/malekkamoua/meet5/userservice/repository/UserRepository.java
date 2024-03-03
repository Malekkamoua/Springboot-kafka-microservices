package com.malekkamoua.meet5.userservice.repository;

import com.malekkamoua.meet5.userservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
    }

    public User getUserByCriteria(String criteria, Long value) {
        try {
            String sql = "SELECT * FROM users WHERE " + criteria + " = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{value}, BeanPropertyRowMapper.newInstance(User.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by criteria: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void createUser(User user) {
        String sql = "INSERT INTO users (username, email, password, age, gender, status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, user.getUsername(), user.getEmail(),
                    user.getPassword(), user.getAge(), user.getGender(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUsernameUnique(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username) == 0;
    }

    public boolean userExists(String id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    @Transactional
    public void updateUser(String id, User updatedUser) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, age = ?, gender = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, updatedUser.getUsername(), updatedUser.getEmail(),
                    updatedUser.getPassword(), updatedUser.getAge(), updatedUser.getGender(), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Transactional
    public void disableUser(Long id) {
        String sql = "UPDATE users SET status = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<User> getAllVisitors(Long userId) {
        String sql = "SELECT u.id, u.username, u.email, u.age, u.gender " +
                "FROM users u " +
                "JOIN ProfileInfos pi ON u.id = pi.interactor_id " +
                "WHERE pi.owner_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(User.class));
    }
}
