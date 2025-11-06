INSERT INTO TEACHERS (first_name, last_name, created_at, updated_at)
VALUES ('Margot', 'DELAHAYE', now(), now()),
       ('Helene', 'THIERCELIN', now(), now());

INSERT INTO USERS (first_name, last_name, admin, email, password, created_at, updated_at)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq', now(), now()),
       ('Test', 'Test', false, 'test@test.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq', now(), now());

INSERT INTO SESSIONS (name, description, `date`, teacher_id, created_at, updated_at)
VALUES ('Session 1', 'Une premiere session', '2025-11-12', 1, now(), now()),
       ('Session 2', 'Une seconde session', '2025-11-15', 2, now(), now());

INSERT INTO PARTICIPATE
VALUES (1, 2);