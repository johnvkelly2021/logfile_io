CREATE TABLE IF NOT EXISTS log_event (
    id BIGINT NOT NULL,
    event_id VARCHAR(100) NOT NULL,
    duration BIGINT NOT NULL,
    type VARCHAR(100),
    host VARCHAR(100),
    alert BOOLEAN DEFAULT FALSE NOT NULL,
    PRIMARY KEY (id)
);