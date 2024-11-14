SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE study RESTART IDENTITY;
TRUNCATE TABLE member RESTART IDENTITY;
TRUNCATE TABLE study_member RESTART IDENTITY;
TRUNCATE TABLE notice RESTART IDENTITY;
TRUNCATE TABLE assignment RESTART IDENTITY;
TRUNCATE TABLE assignment_file RESTART IDENTITY;
TRUNCATE TABLE attendance_date RESTART IDENTITY;
TRUNCATE TABLE attendance RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO study (name, description, topic, is_open, profile_image, created_at)
VALUES ('테스트 스터디', '테스트에 대한 스터디입니다.', '테스트', TRUE, 'https://test-domain.com/image.jpg', CURRENT_TIMESTAMP()),
       ('CS 스터디', 'CS 스터디입니다.', 'CS', TRUE, 'https://test-domain.com/image.jpg', CURRENT_TIMESTAMP()),
       ('코테 스터디', '코딩 테스트 문제 풀이 스터디입니다.', '코딩 테스트', TRUE, 'https://test-domain.com/image.jpg', CURRENT_TIMESTAMP()),
       ('수능 스터디', '수능 문제 풀이 스터디입니다.', '수능', TRUE, 'https://test-domain.com/image.jpg', CURRENT_TIMESTAMP()),
       ('면접 스터디', '기업 면접 스터디입니다.', '면접', TRUE, 'https://test-domain.com/image.jpg', CURRENT_TIMESTAMP());

INSERT INTO member (name, email, contact, nickname, description, profile_image, role)
VALUES ('김디토', 'test_member@email.com', '010-0000-0000', 'kimDitto', '', 'https://test-domain.com/image.jpg', 'USER'),
       ('나디토', 'naditto@email.com', '010-0000-0000', 'NaDitto', '', 'https://test-domain.com/image.jpg', 'USER'),
       ('두디토', 'duditto@email.com', '010-0000-0000', 'DuDitto', '', 'https://test-domain.com/image.jpg', 'USER');

INSERT INTO study_member (member_id, study_id, role, joined_at, message)
VALUES (1, 1, 'LEADER', CURRENT_TIMESTAMP(), ''),
       (2, 2, 'LEADER', CURRENT_TIMESTAMP(), ''),
       (3, 1, 'MEMBER', CURRENT_TIMESTAMP(), ''),
       (3, 2, 'MEMBER', CURRENT_TIMESTAMP(), '');