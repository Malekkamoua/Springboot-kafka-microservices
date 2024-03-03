CREATE TABLE IF NOT EXISTS interactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interactor_id VARCHAR(255),
    interacted_with_id VARCHAR(255),
    interaction_type VARCHAR(255),
    timestamp TIMESTAMP
);
