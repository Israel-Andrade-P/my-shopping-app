CREATE TABLE IF NOT EXISTS users (
id SERIAL PRIMARY KEY,
user_id CHARACTER VARYING(255) NOT NULL,
first_name CHARACTER VARYING(255),
last_name CHARACTER VARYING(255),
email CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
last_login TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
login_attempts INTEGER DEFAULT 0,
enabled BOOLEAN NOT NULL DEFAULT FALSE,
account_non_expired BOOLEAN NOT NULL DEFAULT FALSE,
account_non_locked BOOLEAN NOT NULL DEFAULT FALSE,
created_by BIGINT NOT NULL,
updated_by BIGINT NOT NULL,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT uq_users_email UNIQUE (email),
CONSTRAINT uq_users_user_id UNIQUE (user_id),
CONSTRAINT fk_users_created_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_users_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS confirmations (
id SERIAL PRIMARY KEY,
key CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
user_id BIGINT NOT NULL,
created_by BIGINT NOT NULL,
updated_by BIGINT NOT NULL,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT uq_confirmations_user_id UNIQUE (user_id),
CONSTRAINT uq_confirmations_key UNIQUE (key),
CONSTRAINT fk_confirmations_user_id FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_confirmations_created_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_confirmations_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS credentials (
id SERIAL PRIMARY KEY,
password CHARACTER VARYING(255) NOT NULL,
reference_id CHARACTER VARYING(255),
user_id BIGINT NOT NULL,
created_by BIGINT NOT NULL,
updated_by BIGINT NOT NULL,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT uq_credentials_user_id UNIQUE (user_id),
CONSTRAINT fk_credentials_user_id FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_credentials_created_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_credentials_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles (
id SERIAL PRIMARY KEY,
name CHARACTER VARYING(255),
authority CHARACTER VARYING(255),
reference_id CHARACTER VARYING(255),
created_by BIGINT NOT NULL,
updated_by BIGINT NOT NULL,
created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT uq_roles_name UNIQUE (name),
CONSTRAINT fk_roles_created_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_roles_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX index_users_email ON users (email);

CREATE INDEX index_users_user_id ON users (user_id);

CREATE INDEX index_confirmations_user_id ON confirmations (user_id);

CREATE INDEX index_credentials_user_id ON credentials (user_id);