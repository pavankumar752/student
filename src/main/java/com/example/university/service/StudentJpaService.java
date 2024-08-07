/*
 *
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */

// Write your code here
package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.university.model.*;
import com.example.university.repository.*;

@Service
public class StudentJpaService implements StudentRepository {
    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Override
    public List<Student> getStudents() {
        try {
            List<Student> studentList = studentJpaRepository.findAll();
            ArrayList<Student> students = new ArrayList<>(studentList);
            return students;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Student getStudentById(int studentId) {
        try {
            return studentJpaRepository.findById(studentId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Student addStudent(Student student) {
        List<Integer> courseIds = new ArrayList<>();
        for (Course course : student.getCourses()) {
            courseIds.add(course.getCourseId());
        }
        try {
            List<Course> complete_courses = courseJpaRepository.findAllById(courseIds);
            if (courseIds.size() != complete_courses.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid course ids");
            }
            student.setCourses(complete_courses);

            studentJpaRepository.save(student);
            return student;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        try {
            Student newStudent = studentJpaRepository.findById(studentId).get();
            if (student.getStudentName() != null) {
                newStudent.setStudentName(student.getStudentName());
            }
            if (student.getEmail() != null) {
                newStudent.setEmail(student.getEmail());
            }
            if (student.getCourses() != null) {
                List<Integer> courseIds = new ArrayList<>();
                for (Course course : student.getCourses()) {
                    courseIds.add(course.getCourseId());
                }
                List<Course> complete_courses = courseJpaRepository.findAllById(courseIds);
                if (courseIds.size() != complete_courses.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid course ids");
                }
                newStudent.setCourses(complete_courses);
            }
            return studentJpaRepository.save(newStudent);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteStudent(int studentId) {
        try {
            // Fetch the author entity
            Student student = studentJpaRepository.findById(studentId).get();

            // Remove the associations
            List<Course> courses = student.getCourses();
            for (Course course : courses) {
                course.getStudents().remove(student);
            }

            // Update the book entity after removing the association
            courseJpaRepository.saveAll(courses);

            // Delete the author
            studentJpaRepository.deleteById(studentId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Course> getStudentCourses(int studentId) {
        try {
            Student student = studentJpaRepository.findById(studentId).get();
            return student.getCourses();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}