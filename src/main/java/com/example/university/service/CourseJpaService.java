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
import com.example.university.repository.*;
import com.example.university.model.*;

@Service
public class CourseJpaService implements CourseRepository {
    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Override
    public List<Course> getCourses() {
        try {
            List<Course> courseList = courseJpaRepository.findAll();
            ArrayList<Course> courses = new ArrayList<>(courseList);
            return courses;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        try {
            return courseJpaRepository.findById(courseId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course addCourse(Course course) {
        List<Integer> studentIds = new ArrayList<>();
        for (Student student : course.getStudents()) {
            studentIds.add(student.getStudentId());
        }
        try {
            int professorId = course.getProfessor().getProfessorId();
            Professor professor = professorJpaRepository.findById(professorId).get();
            course.setProfessor(professor);

            List<Student> complete_students = studentJpaRepository.findAllById(studentIds);
            if (studentIds.size() != complete_students.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid studentids");
            }
            course.setStudents(complete_students);

            // Add the author to all the books
            for (Student student : complete_students) {
                student.getCourses().add(course);
            }

            Course savedCourse = courseJpaRepository.save(course);

            studentJpaRepository.saveAll(complete_students);

            return savedCourse;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course updateCourse(int courseId, Course course) {
        try {
            Course newCourse = courseJpaRepository.findById(courseId).get();
            if (course.getCourseName() != null) {
                newCourse.setCourseName(course.getCourseName());
            }
            if (course.getCredits() != 0) {
                newCourse.setCredits(course.getCredits());
            }
            if (course.getProfessor() != null) {
                int professorId = course.getProfessor().getProfessorId();
                Professor professor = professorJpaRepository.findById(professorId).get();
                newCourse.setProfessor(professor);
            }

            if (course.getStudents() != null) {
                List<Student> students = newCourse.getStudents();
                for (Student student : students) {
                    student.getCourses().remove(newCourse);
                }
                studentJpaRepository.saveAll(students);

                List<Integer> studentIds = new ArrayList<>();
                for (Student student : course.getStudents()) {
                    studentIds.add(student.getStudentId());
                }
                List<Student> complete_students = studentJpaRepository.findAllById(studentIds);
                if (studentIds.size() != complete_students.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid studentids");
                }
                for (Student student : complete_students) {
                    student.getCourses().add(newCourse);
                }
                studentJpaRepository.saveAll(complete_students);
                newCourse.setStudents(complete_students);
            }
            return courseJpaRepository.save(newCourse);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        try {
            // Fetch the author entity
            Course course = courseJpaRepository.findById(courseId).get();

            // Remove the associations
            List<Student> students = course.getStudents();
            for (Student student : students) {
                student.getCourses().remove(course);
            }

            // Update the book entity after removing the association
            studentJpaRepository.saveAll(students);

            // Delete the author
            courseJpaRepository.deleteById(courseId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public Professor getCourseProfessor(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            // return countryJpaRepository.findByAthlete(athlete);
            Professor professor = course.getProfessor();
            return professor;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public List<Student> getCourseStudents(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getStudents();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}