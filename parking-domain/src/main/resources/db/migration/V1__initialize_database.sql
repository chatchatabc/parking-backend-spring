-- Create Role Table
CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

ALTER SEQUENCE role_id_seq RESTART WITH 10;

-- Create Users Table
CREATE TABLE IF NOT EXISTS users
(
    id                SERIAL PRIMARY KEY,
    user_uuid         VARCHAR(36)       NOT NULL UNIQUE,
    notification_uuid VARCHAR(36)       NOT NULL UNIQUE,
    email             VARCHAR(255) UNIQUE,
    username          VARCHAR(15) UNIQUE,
    password          VARCHAR(255),
    phone             VARCHAR(15)       NOT NULL,
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    avatar            INT,
    flag              INT  DEFAULT 0    NOT NULL,
    status            INT  DEFAULT 0    NOT NULL,
    enabled           BOOL DEFAULT TRUE NOT NULL,
    email_verified_at TIMESTAMP,
    phone_verified_at TIMESTAMP,
    created_at        TIMESTAMP         NOT NULL,
    updated_at        TIMESTAMP         NOT NULL
);

create index idx_users_on_user_uuid on users (user_uuid);
create index idx_users_on_email on users (email);
create index idx_users_on_username on users (username);
create index idx_users_on_phone on users (phone);

ALTER SEQUENCE users_id_seq RESTART WITH 1000;

-- Create Authorities table
CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(15) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    constraint fk_authorities_users foreign key (username) references users (username) on update cascade
);

create unique index idx_auth_username on authorities (username, authority);

-- Function to automatically update user on authorities table
CREATE OR REPLACE FUNCTION update_username_in_authorities() RETURNS TRIGGER AS
$$
BEGIN
    UPDATE authorities SET username = NEW.username WHERE username = OLD.username;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_authorities_username
    AFTER UPDATE OF username
    ON users
    FOR EACH ROW
EXECUTE FUNCTION update_username_in_authorities();

-- Create user_login_log table
CREATE TABLE IF NOT EXISTS user_login_log
(
    id         SERIAL PRIMARY KEY,
    user_id    BIGINT             NOT NULL,
    type       INT  DEFAULT 0     NOT NULL,
    ip_address VARCHAR(39)        NOT NULL,
    success    BOOL DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP          NOT NULL
);

ALTER SEQUENCE user_login_log_id_seq RESTART WITH 1000;

-- Create user_logout_log table
CREATE TABLE IF NOT EXISTS user_logout_log
(
    id         SERIAL PRIMARY KEY,
    user_id    INT           NOT NULL,
    type       INT DEFAULT 0 NOT NULL,
    ip_address VARCHAR(39)   NOT NULL,
    created_at TIMESTAMP     NOT NULL
);

ALTER SEQUENCE user_logout_log_id_seq RESTART WITH 1000;

-- Create user_activity_log table
CREATE TABLE IF NOT EXISTS user_activity_log
(
    id           SERIAL PRIMARY KEY,
    performed_by BIGINT    NOT NULL,
    name         VARCHAR(255),
    target_id    VARCHAR(36),
    event_type   VARCHAR(255),
    column_name  VARCHAR(255),
    data_before  TEXT,
    data_after   TEXT,
    created_at   TIMESTAMP NOT NULL
);

ALTER SEQUENCE user_activity_log_id_seq RESTART WITH 1000;

-- Create user_ban_history_log table
CREATE TABLE IF NOT EXISTS user_ban_history_log
(
    id           SERIAL PRIMARY KEY,
    user_id      BIGINT        NOT NULL,
    banned_by    BIGINT        NOT NULL,
    until        TIMESTAMP     NOT NULL,
    reason       TEXT,
    unban_reason TEXT,
    unbanned_by  BIGINT,
    status       INT DEFAULT 0 NOT NULL,
    created_at   TIMESTAMP     NOT NULL
);

ALTER SEQUENCE user_ban_history_log_id_seq RESTART WITH 1000;
create index idx_user_ban_history_log_on_banned_by on user_ban_history_log (banned_by);

-- Create Vehicle table
CREATE TABLE IF NOT EXISTS vehicle
(
    id           SERIAL PRIMARY KEY,
    vehicle_uuid VARCHAR(36)  NOT NULL UNIQUE,
    name         VARCHAR(255) NOT NULL,
    plate_number VARCHAR(20)  NOT NULL UNIQUE,
    type         INT          NOT NULL DEFAULT 0,
    owner_id     BIGINT       NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL
);

ALTER SEQUENCE vehicle_id_seq RESTART WITH 1000;
create index idx_vehicle_on_type on vehicle (type);
create index idx_vehicle_on_plate_number on vehicle (plate_number);
create index idx_vehicle_on_owner_id on vehicle (owner_id);


-- Create user_vehicle table
CREATE TABLE IF NOT EXISTS user_vehicle
(
    user_id    INT NOT NULL,
    vehicle_id INT NOT NULL,
    PRIMARY KEY (user_id, vehicle_id)
);

-- Create rate table
CREATE TABLE IF NOT EXISTS rate
(
    id                                VARCHAR(36) PRIMARY KEY,
    type                              INT            NOT NULL DEFAULT 0,
    interval                          INT            NOT NULL DEFAULT 0,
    free_hours                        INT            NOT NULL DEFAULT 0,
    pay_for_free_hours_when_exceeding BOOL           NOT NULL DEFAULT FALSE,
    starting_rate                     DECIMAL(10, 2) NOT NULL DEFAULT 0,
    rate                              DECIMAL(10, 2) NOT NULL DEFAULT 0,
    created_at                        TIMESTAMP      NOT NULL,
    updated_at                        TIMESTAMP      NOT NULL
);

-- Create parking_lot table
CREATE TABLE IF NOT EXISTS parking_lot
(
    id                   SERIAL PRIMARY KEY,
    parking_lot_uuid     VARCHAR(36)   NOT NULL UNIQUE,
    owner_id             BIGINT        NOT NULL UNIQUE,
    rate_id              VARCHAR(36),
    name                 VARCHAR(255)  NOT NULL,
    address              VARCHAR(255)  NOT NULL,
    latitude             FLOAT         NOT NULL,
    longitude            FLOAT         NOT NULL,
    description          VARCHAR(255),
    capacity             INT DEFAULT 0 NOT NULL,
    available_slots      INT DEFAULT 0 NOT NULL,
    business_hours_start TIMESTAMP     NOT NULL,
    business_hours_end   TIMESTAMP     NOT NULL,
    open_days_flag       INT DEFAULT 0 NOT NULL,
    flag                 INT DEFAULT 0 NOT NULL,
    verified_at          TIMESTAMP,
    verified_by          BIGINT,
    rejection_reason     TEXT,
    status               INT DEFAULT 0 NOT NULL,
    created_at           TIMESTAMP     NOT NULL,
    updated_at           TIMESTAMP     NOT NULL
);

ALTER SEQUENCE parking_lot_id_seq RESTART WITH 1000;
create index idx_parking_lot_on_owner_id on parking_lot (owner_id);
create index idx_parking_lot_on_name on parking_lot (name);
create index idx_parking_lot_on_verified_at on parking_lot (verified_at);

-- Create cloud_file table
CREATE TABLE IF NOT EXISTS cloud_file
(
    id          SERIAL PRIMARY KEY,
    bucket      VARCHAR(127),
    key         VARCHAR(127) NOT NULL UNIQUE, -- key to access file with ext name
    name        VARCHAR(255) NOT NULL,        -- original file name
    size        INT          NOT NULL,
    mime_type   VARCHAR(127) NOT NULL,
    tags        VARCHAR(255),
    status      INT          NOT NULL DEFAULT 0,
    uploaded_by BIGINT       NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP
);

create index idx_cloud_file_on_uploaded_by on cloud_file (uploaded_by);
create index idx_cloud_file_on_key on cloud_file (key);

-- Create parking_lot_image table
CREATE TABLE IF NOT EXISTS parking_lot_image
(
    id             VARCHAR(36) PRIMARY KEY,
    cloud_file_id  BIGINT NOT NULL,
    parking_lot_id BIGINT NOT NULL,
    file_order     INT    NOT NULL DEFAULT 0
);

create index idx_parking_lot_image_on_parking_lot_id on parking_lot_image (parking_lot_id);

-- Create invoice table
CREATE TABLE IF NOT EXISTS invoice
(
    id                                  VARCHAR(36) PRIMARY KEY,
    parking_lot_id                      BIGINT    NOT NULL,
    vehicle_id                          BIGINT    NOT NULL,
    estimated_parking_duration_in_hours INT DEFAULT 0,
    total                               DECIMAL(10, 2),
    paid_at                             TIMESTAMP,
    start_at                            TIMESTAMP NOT NULL,
    end_at                              TIMESTAMP,
    created_at                          TIMESTAMP NOT NULL,
    updated_at                          TIMESTAMP NOT NULL
);

create index idx_invoice_on_parking_lot_id on invoice (parking_lot_id);
create index idx_invoice_on_vehicle_id on invoice (vehicle_id);

-- Create report table
CREATE TABLE IF NOT EXISTS report
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    plate_number VARCHAR(20)  NOT NULL,
    latitude     FLOAT        NOT NULL,
    longitude    FLOAT        NOT NULL,
    reported_by  BIGINT       NOT NULL,
    cancelled_at TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL
);

ALTER SEQUENCE report_id_seq RESTART WITH 1000;
create index idx_report_on_reported_by on report (reported_by);

-- Create report_status table
CREATE TABLE IF NOT EXISTS report_status
(
    id           SERIAL PRIMARY KEY,
    report_id    BIGINT    NOT NULL,
    performed_by BIGINT    NOT NULL,
    status       INT       NOT NULL DEFAULT 0,
    remarks      VARCHAR(255),
    created_at   TIMESTAMP NOT NULL
);

ALTER SEQUENCE report_status_id_seq RESTART WITH 1000;
create index idx_report_status_on_report_id on report_status (report_id);
create index idx_report_status_on_performed_by on report_status (performed_by);