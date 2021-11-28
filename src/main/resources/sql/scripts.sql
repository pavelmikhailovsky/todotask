DROP TABLE IF EXISTS todo;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    image VARCHAR(3000) DEFAULT '',
    create_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE todo(
    todo_id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    text VARCHAR(5000) UNIQUE,
    create_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_completed BOOLEAN DEFAULT false,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

INSERT INTO users(username, password) VALUES ('user1', 'user');
INSERT INTO users(username, password) VALUES ('user2', 'user');
INSERT INTO users(username, password) VALUES ('user3', 'user');
