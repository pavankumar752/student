/*
 *
 * You can use the following import statements
 * 
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.university.repository;

import java.util.*;
import com.example.university.model.*;

public interface StudentRepository {
    List<Student> getStudents();

    Student getStudentById(int studentId);

    Student addStudent(Student student);

    Student updateStudent(int studentId, Student student);

    public void deleteStudent(int studentId);

    List<Course> getStudentCourses(int studentId);
}