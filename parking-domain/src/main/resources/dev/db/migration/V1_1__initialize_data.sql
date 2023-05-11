-- Insert initial role data
INSERT INTO role (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_ADMIN');

INSERT INTO role (name)
SELECT 'ROLE_PARKING_OWNER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_PARKING_OWNER');

INSERT INTO role (name)
SELECT 'ROLE_ENFORCER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_ENFORCER');

INSERT INTO role (name)
SELECT 'ROLE_MEMBER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_MEMBER');

-- Insert initial admin member
INSERT INTO member (member_uuid, notification_uuid, email, username, phone, password, created_at, updated_at)
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
SELECT member.id, role.id
FROM member,
     role
WHERE member.username = 'admin'
  AND role.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;