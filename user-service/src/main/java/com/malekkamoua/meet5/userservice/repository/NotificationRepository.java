package com.malekkamoua.meet5.userservice.repository;

import com.malekkamoua.meet5.userservice.models.Notification;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertBatchNotifications(List<Notification> notifications) {
        String sql = "INSERT INTO notifications (userID, message) VALUES (?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Notification notification = notifications.get(i);
                preparedStatement.setLong(1, notification.getUserID());
                preparedStatement.setString(2, notification.getMessage());
            }

            @Override
            public int getBatchSize() {
                return notifications.size();
            }
        });
    }


}
