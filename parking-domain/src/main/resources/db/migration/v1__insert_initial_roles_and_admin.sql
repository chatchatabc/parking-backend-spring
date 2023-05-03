-- Insert initial roles data
INSERT INTO roles (id, name)
SELECT '7d6312f7-149f-4320-9be9-7bb1171bdaa1', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (id, name)
SELECT '27cec7e4-58cd-4a78-b8b4-574a66a747a4', 'ROLE_PARKING_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_PARKING_MANAGER');

INSERT INTO roles (id, name)
SELECT 'a755bfee-c78b-4166-9b17-017409328060', 'ROLE_ENFORCER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ENFORCER');

INSERT INTO roles (id, name)
SELECT '9b9fd9be-bea0-44f5-98e0-18ea30d06a62', 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

-- Insert initial admin user
INSERT INTO users (user_id, notification_id, email, username, phone, password, flag)
SELECT 'ec4af6e9-ec57-434d-990d-ae83d9459a31',
       'bdc2ae2c-fabf-4889-8110-f0ec4d406ff2',
       'admin@gmail.com',
       'admin',
       '1234567890',
       '$2a$10$HfewouomThstiUJu.zfYPOsLJahJHCVnqS7GbEz0KFBQjiZUcsINK',
       1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Assign the 'ROLE_ADMIN' role to the user
INSERT INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users,
     roles
WHERE users.username = 'admin'
  AND roles.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;
