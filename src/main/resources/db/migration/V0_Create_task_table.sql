CREATE TABLE task_t
(
    id  SERIAL PRIMARY KEY,
    title   VARCHAR(255) NOT NULL,
    description TEXT,
    status   VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)