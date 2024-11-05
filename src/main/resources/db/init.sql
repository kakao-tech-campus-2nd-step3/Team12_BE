CREATE TABLE member (
    id SERIAL PRIMARY KEY,
    name VARCHAR(25),
    email VARCHAR(127),
    contact VARCHAR(25),
    nickname VARCHAR(255),
    description VARCHAR(255),
    profile_image VARCHAR(255),
    role VARCHAR(50)
);

CREATE TABLE study (
    id SERIAL PRIMARY KEY,
    name VARCHAR(127),
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_open BOOLEAN,
    topic VARCHAR(25),
    profile_image VARCHAR(255)
);

CREATE TABLE teammate (
    id SERIAL PRIMARY KEY,
    member_id INT NOT NULL,
    study_id INT NOT NULL,
    role VARCHAR(50),
    joined_at DATE,
    CONSTRAINT FK_member_TO_teammate FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT FK_study_TO_teammate FOREIGN KEY (study_id) REFERENCES study (id)
);

CREATE TABLE attendance_dates (
    id SERIAL PRIMARY KEY,
    study_id INT NOT NULL,
    attendance_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_study_TO_attendance_dates FOREIGN KEY (study_id) REFERENCES study (id)
);

CREATE TABLE attendance (
    id SERIAL PRIMARY KEY,
    member_id INT NOT NULL,
    date_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_member_TO_attendance FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT FK_dates_TO_attendance FOREIGN KEY (date_id) REFERENCES attendance_dates (id)
);

CREATE TABLE notice (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    teammate_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_teammate_TO_notice FOREIGN KEY (teammate_id) REFERENCES teammate(id)
);

CREATE TABLE assignment (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    study_id INT NOT NULL,
    content TEXT NOT NULL,
    deadline TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_study_TO_assignment FOREIGN KEY (study_id) REFERENCES study(id)
);
