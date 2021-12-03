#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE DATABASE $APP_DB_NAME;
  \connect $APP_DB_NAME
  BEGIN;
    CREATE TABLE IF NOT EXISTS users (
      user_id BIGSERIAL PRIMARY KEY,
      username VARCHAR(150) UNIQUE NOT NULL,
      password VARCHAR(200) NOT NULL,
      image VARCHAR(3000) DEFAULT '',
      create_at TIMESTAMP NOT NULL DEFAULT NOW()
	  );
	  CREATE TABLE IF NOT EXISTS todo(
      todo_id BIGSERIAL PRIMARY KEY,
      user_id INTEGER NOT NULL,
      text VARCHAR(5000) UNIQUE,
      create_at TIMESTAMP NOT NULL DEFAULT NOW(),
      is_completed BOOLEAN DEFAULT false,
      FOREIGN KEY (user_id) REFERENCES users (user_id)
    );
  COMMIT;
EOSQL