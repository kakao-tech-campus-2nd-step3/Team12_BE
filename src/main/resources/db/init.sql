CREATE TABLE member (
    id SERIAL PRIMARY KEY,
    name VARCHAR(25),
    email VARCHAR(127),
    contact VARCHAR(25),
    nickname VARCHAR(255),
    description VARCHAR(255),
    profileImage VARCHAR(255)
);

CREATE TABLE study (
    id SERIAL PRIMARY KEY,
    name VARCHAR(127),
    description VARCHAR(255),
    createdAt DATE,
    isOpen BOOLEAN,
    topic VARCHAR(25),
    profileImage VARCHAR(255)
);

CREATE TABLE teammate (
    id SERIAL PRIMARY KEY,
    memberId INT NOT NULL,
    studyId INT NOT NULL,
    role VARCHAR(50),
    joinedAt DATE,
    CONSTRAINT FK_member_TO_teammate FOREIGN KEY (memberId) REFERENCES member (id),
    CONSTRAINT FK_study_TO_teammate FOREIGN KEY (studyId) REFERENCES study (id)
);