-- liquibase formatted sql

-- changeset lplaudsmith:117
ALTER TABLE user_role
MODIFY COLUMN role ENUM('USER', 'GUEST', 'STUDENT', 'RESEARCHER', 'MEMBER', 'ADMIN', 'ALUMNI');

-- rollback alter table user_role modify column 'role' enum('USER', 'GUEST', 'STUDENT', 'RESEARCHER', 'MEMBER', 'ADMIN')

