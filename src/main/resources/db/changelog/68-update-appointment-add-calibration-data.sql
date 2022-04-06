-- liquibase formatted sql

-- changeset lbradley1:123

CREATE TABLE appointment_calibration (
    appointment_id INT NOT NULL,
    calibration_type BIT NOT NULL,
    zenith_start_time DATETIME NOT NULL,
    zenith_end_time DATETIME NOT NULL,
    tree_start_time DATETIME NOT NULL,
    tree_end_time DATETIME NOT NULL,

    FOREIGN KEY (appointment_id) REFERENCES appointment(id)
);

-- DROP TABLE appointment_calibration;