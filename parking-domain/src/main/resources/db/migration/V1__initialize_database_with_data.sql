-- Create Role Table
CREATE TABLE IF NOT EXISTS role
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Insert initial role data
INSERT INTO role (id, name)
SELECT '7d6312f7-149f-4320-9be9-7bb1171bdaa1', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_ADMIN');

INSERT INTO role (id, name)
SELECT '27cec7e4-58cd-4a78-b8b4-574a66a747a4', 'ROLE_PARKING_OWNER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_PARKING_OWNER');

INSERT INTO role (id, name)
SELECT 'a755bfee-c78b-4166-9b17-017409328060', 'ROLE_ENFORCER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_ENFORCER');

INSERT INTO role (id, name)
SELECT '9b9fd9be-bea0-44f5-98e0-18ea30d06a62', 'ROLE_MEMBER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_MEMBER');

-- Create Member Table
CREATE TABLE IF NOT EXISTS member
(
    id                SERIAL PRIMARY KEY,
    member_id         UUID          NOT NULL UNIQUE,
    notification_id   UUID          NOT NULL UNIQUE,
    email             VARCHAR(255) UNIQUE,
    username          VARCHAR(15) UNIQUE,
    password          VARCHAR(255),
    phone             VARCHAR(15)   NOT NULL,
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    avatar            VARCHAR(255),
    flag              INT DEFAULT 0 NOT NULL,
    status            INT DEFAULT 0 NOT NULL,
    email_verified_at TIMESTAMP,
    phone_verified_at TIMESTAMP,
    created_at        TIMESTAMP     NOT NULL,
    updated_at        TIMESTAMP     NOT NULL
);

-- Create member_role table
CREATE TABLE IF NOT EXISTS member_role
(
    member_id UUID NOT NULL,
    role_id   UUID NOT NULL,
    PRIMARY KEY (member_id, role_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);

-- Create member_login_log table
CREATE TABLE IF NOT EXISTS member_login_log
(
    id         UUID PRIMARY KEY,
    member_id  UUID               NOT NULL,
    email      VARCHAR(255)       NOT NULL,
    phone      VARCHAR(15)        NOT NULL,
    type       INT  DEFAULT 0     NOT NULL,
    ip_address VARCHAR(255)       NOT NULL,
    success    BOOL DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP          NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);

-- Create member_logout_log table
CREATE TABLE IF NOT EXISTS member_logout_log
(
    id         UUID PRIMARY KEY,
    member_id  UUID          NOT NULL,
    email      VARCHAR(255)  NOT NULL,
    phone      VARCHAR(15)   NOT NULL,
    type       INT DEFAULT 0 NOT NULL,
    ip_address VARCHAR(255)  NOT NULL,
    created_at TIMESTAMP     NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);

-- Insert initial admin member
INSERT INTO member (member_id, notification_id, email, username, phone, password, created_at, updated_at)
SELECT 'ec4af6e9-ec57-434d-990d-ae83d9459a31',
       'bdc2ae2c-fabf-4889-8110-f0ec4d406ff2',
       'admin@gmail.com',
       'admin',
       '1234567890',
       '$2a$10$HfewouomThstiUJu.zfYPOsLJahJHCVnqS7GbEz0KFBQjiZUcsINK',
       now(),
       now()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE username = 'admin');

-- Assign admin role to admin member
INSERT INTO member_role (member_id, role_id)
SELECT users.id, roles.id
FROM users,
     roles
WHERE users.username = 'admin'
  AND roles.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

-- Create Vehicle table
CREATE TABLE IF NOT EXISTS vehicle
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    plate_number VARCHAR(255) NOT NULL UNIQUE,
    type         INT          NOT NULL DEFAULT 0,
    owner_id     UUID         NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES member (member_id)
);

-- Create member_vehicle table
CREATE TABLE IF NOT EXISTS member_vehicle
(
    member_id  UUID NOT NULL,
    vehicle_id INT  NOT NULL,
    PRIMARY KEY (member_id, vehicle_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

-- Create rate table
CREATE TABLE IF NOT EXISTS rate
(
    id                                UUID PRIMARY KEY,
    type                              INT            NOT NULL DEFAULT 0,
    interval                          INT            NOT NULL DEFAULT 0,
    free_hours                        INT            NOT NULL DEFAULT 0,
    pay_for_free_hours_when_exceeding BOOL           NOT NULL DEFAULT FALSE,
    starting_rate                     DECIMAL(10, 2) NOT NULL DEFAULT 0,
    rate                              DECIMAL(10, 2) NOT NULL DEFAULT 0,
    created_at                        TIMESTAMP      NOT NULL,
    updated_at                        TIMESTAMP      NOT NULL
-- TODO: Add version for history
);

-- Create parking_lot table
CREATE TABLE IF NOT EXISTS parking_lot
(
    id                   UUID PRIMARY KEY,
    owner_id             UUID          NOT NULL,
    rate_id              UUID,
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
    verified_at          TIMESTAMP,
    verified_by          UUID,
    status               INT DEFAULT 0 NOT NULL,
    created_at           TIMESTAMP     NOT NULL,
    updated_at           TIMESTAMP     NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES member (member_id),
    FOREIGN KEY (rate_id) REFERENCES rate (id),
    FOREIGN KEY (verified_by) REFERENCES member (member_id)
);

-- Create parking_lot_image table
CREATE TABLE IF NOT EXISTS parking_lot_image
(
    id             UUID PRIMARY KEY,
    parking_lot_id UUID         NOT NULL,
    file_order     INT          NOT NULL DEFAULT 0,
    filename       VARCHAR(255) NOT NULL,
    filesize       INT          NOT NULL,
    mimetype       VARCHAR(255) NOT NULL,
    url            VARCHAR(255) NOT NULL,
    status         INT          NOT NULL DEFAULT 0,
    uploaded_by    UUID         NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL,
    FOREIGN KEY (parking_lot_id) REFERENCES parking_lot (id),
    FOREIGN KEY (uploaded_by) REFERENCES member (member_id)
);

-- Create invoice table
CREATE TABLE IF NOT EXISTS invoice
(
    id                                  UUID PRIMARY KEY,
    parking_lot_id                      UUID           NOT NULL,
    vehicle_id                          INT            NOT NULL,
    plate_number                        VARCHAR(255)   NOT NULL,
    estimated_parking_duration_in_hours INT            NOT NULL DEFAULT 0,
    total                               DECIMAL(10, 2) NOT NULL,
    paid_at                             TIMESTAMP,
    start_at                            TIMESTAMP      NOT NULL,
    end_at                              TIMESTAMP      NOT NULL,
    created_at                          TIMESTAMP      NOT NULL,
    updated_at                          TIMESTAMP      NOT NULL,
    FOREIGN KEY (parking_lot_id) REFERENCES parking_lot (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

-- Create report table
CREATE TABLE IF NOT EXISTS report
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    plate_number VARCHAR(255) NOT NULL,
    latitude     FLOAT        NOT NULL,
    longitude    FLOAT        NOT NULL,
    reported_by  UUID         NOT NULL,
    cancelled_at TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    FOREIGN KEY (reported_by) REFERENCES member (member_id)
);

-- Create report_status table
CREATE TABLE IF NOT EXISTS report_status
(
    id           UUID PRIMARY KEY,
    report_id    UUID      NOT NULL,
    performed_by UUID      NOT NULL,
    status       INT       NOT NULL DEFAULT 0,
    remarks      VARCHAR(255),
    created_at   TIMESTAMP NOT NULL,
    FOREIGN KEY (report_id) REFERENCES report (id),
    FOREIGN KEY (performed_by) REFERENCES member (member_id)
);