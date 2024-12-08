CREATE SEQUENCE users_id_seq START 101;

CREATE TABLE IF NOT EXISTS users (
id SERIAL PRIMARY KEY,
user_id CHARACTER VARYING(255),
first_name CHARACTER VARYING(255),
last_name CHARACTER VARYING(255),
email CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
last_login TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
login_attempts INTEGER DEFAULT 0,
dob DATE,
telephone CHARACTER VARYING(50),
enabled BOOLEAN DEFAULT FALSE,
account_non_expired BOOLEAN DEFAULT FALSE,
account_non_locked BOOLEAN DEFAULT FALSE,
created_by BIGINT,
updated_by BIGINT,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
role CHARACTER VARYING(50)
);

CREATE TABLE IF NOT EXISTS confirmations (
id SERIAL PRIMARY KEY,
key_code CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
user_id BIGINT,
created_by BIGINT,
updated_by BIGINT,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_confirmations_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS credentials (
id SERIAL PRIMARY KEY,
password CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
user_id BIGINT,
created_by BIGINT,
updated_by BIGINT,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
id SERIAL PRIMARY KEY,
name CHARACTER VARYING(255),
authority CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
created_by BIGINT,
updated_by BIGINT,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS locations(
id SERIAL PRIMARY KEY,
reference_id CHARACTER VARYING(255),
country CHARACTER VARYING(50),
city CHARACTER VARYING(50),
street CHARACTER VARYING(100),
zip_code CHARACTER VARYING(50),
user_id BIGINT,
created_by BIGINT,
updated_by BIGINT,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


