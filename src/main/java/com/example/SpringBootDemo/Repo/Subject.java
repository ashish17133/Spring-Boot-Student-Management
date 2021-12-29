package com.example.SpringBootDemo.Repo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Subject {
	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubTeacher() {
		return subTeacher;
	}

	public void setSubTeacher(String subTeacher) {
		this.subTeacher = subTeacher;
	}

	@Id
	int subjectId;

	String name;
	
	String subTeacher;
	
}
