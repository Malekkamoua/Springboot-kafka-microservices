CREATE TABLE IF NOT EXISTS frauds (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fraudulent_user_id BIGINT,
    cause VARCHAR(255),
    timestamp TIMESTAMP
);
