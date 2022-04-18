-- liquibase formatted sql

-- changeset lbradley1:123

CREATE TABLE appointment_calibration (
    id INT NOT NULL AUTO_INCREMENT,
    appointment_id INT NOT NULL,
    calibration_type BIT NOT NULL,
    zenith_start_time DATETIME NOT NULL,
    zenith_end_time DATETIME NOT NULL,
    tree_start_time DATETIME NOT NULL,
    tree_end_time DATETIME NOT NULL,

    PRIMARY KEY (id)
);
-- DROP TABLE appointment_calibration;