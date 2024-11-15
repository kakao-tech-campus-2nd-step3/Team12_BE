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
       ('두디토', 'duditto@email.com', '010-0000-0000', 'DuDitto', '', 'https://test-domain.com/image.jpg', 'USER'),
       ('한디토', 'gilldong@kakao.com', '010-0000-0000', 'DuDitto', '', 'https://test-domain.com/image.jpg', 'GUEST');

INSERT INTO study_member (member_id, study_id, role, joined_at, message)
VALUES (1, 1, 'LEADER', CURRENT_TIMESTAMP(), ''),
       (2, 2, 'LEADER', CURRENT_TIMESTAMP(), ''),
       (3, 1, 'MEMBER', CURRENT_TIMESTAMP(), ''),
       (3, 2, 'MEMBER', CURRENT_TIMESTAMP(), '');

INSERT INTO notice (title, content, study_id, member_id, created_at)
VALUES ('테스트 공지', '테스트 공지입니다.', 1, 1, CURRENT_TIMESTAMP()),
       ('과제 공지', '과제 공지입니다.', 1, 2, CURRENT_TIMESTAMP()),
       ('회의 공지', '회의 공지입니다.', 2, 3, CURRENT_TIMESTAMP()),
       ('마감 공지', '마감 공지입니다.', 3, 3, CURRENT_TIMESTAMP());

INSERT INTO assignment (title, content, study_id, deadline, created_at)
VALUES ('과제1번', '과제1을 마감기한까지 제출하세요', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('과제2번', '과제2을 마감기한까지 제출하세요', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('과제3번', '과제3을 마감기한까지 제출하세요', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('과제4번', '과제4을 마감기한까지 제출하세요', 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO assignment_file (file_name, file_url, assignment_id, member_id, created_at)
VALUES ('assignment/file1', 'https://https://test-domain.com/file1.text', 1, 1, CURRENT_TIMESTAMP()),
       ('assignment/file2', 'https://https://test-domain.com/file2.text', 2, 1, CURRENT_TIMESTAMP()),
       ('assignment/5848ad4b-1e46-4800-a813-0f4e3333e502test.text', 'https://ditto2024.s3.ap-northeast-2.amazonaws.com/assignment/5848ad4b-1e46-4800-a813-0f4e3333e502test.text', 1, 4, CURRENT_TIMESTAMP());

INSERT INTO attendance_date (study_id, start_time, deadline, code)
VALUES (1, '2024-11-11 11:00:00', '2024-11-11 11:15:00', 'true_'),
       (1, CURRENT_TIMESTAMP(), TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP()), 'true_'),
       (2, CURRENT_TIMESTAMP(), TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP()), 'abcde'),
       (3, CURRENT_TIMESTAMP(), TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP()), 'te2st'),
       (4, CURRENT_TIMESTAMP(), TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP()), 'he110');

INSERT INTO attendance (member_id, date_id, created_at)
VALUES (1, 1, CURRENT_TIMESTAMP()),
       (2, 2, CURRENT_TIMESTAMP()),
       (3, 1, CURRENT_TIMESTAMP()),
       (3, 3, CURRENT_TIMESTAMP());

