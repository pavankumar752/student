/*
 *
 * You can use the following import statements
 * 
 * import javax.persistence.*;
 * 
 */

// Write your code here
package com.example.university.model;

import javax.persistence.*;

@Entity
@Table(name = "professor")
public class Professor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int ProfessorId;

	@Column(name = "name")
	private String ProfessorName;

	@Column(name = "department")
	private String department;

	public Professor() {

	}

	public int getProfessorId() {
		return ProfessorId;
	}

	public void setProfessorId(int professorId) {
		ProfessorId = professorId;
	}

	public String getProfessorName() {
		return ProfessorName;
	}

	public void setProfessorName(String professorName) {
		ProfessorName = professorName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}