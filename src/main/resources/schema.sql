

CREATE TABLE professor(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250),
    department VARCHAR(250)
);


CREATE TABLE course(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250),
    credits INT,
    professorId INT,
    FOREIGN KEY(professorId) REFERENCES professor(id)
);


CREATE TABLE student(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250),
    email VARCHAR(250) 
);



CREATE TABLE course_student(
    studentId INT,
    courseId INT,
    PRIMARY key(studentId, courseId),
    FOREIGN key(studentId) REFERENCES student(id),
    FOREIGN key(courseId) REFERENCES course(id)
);

