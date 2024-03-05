--liquibase formatted sql

--changeset eliseevdry:1
ALTER TABLE users
    ADD COLUMN image VARCHAR(64);

--changeset eliseevdry:2
ALTER TABLE users_aud
    ADD COLUMN image VARCHAR(64);
