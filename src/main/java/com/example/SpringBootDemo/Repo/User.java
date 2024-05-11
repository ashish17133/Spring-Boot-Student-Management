package com.example.SpringBootDemo.Repo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	String name;
	
	String password;
	String EmailId;
	public User() {
		
	}
	public User(String name) {
		this.name=name;
	}
	public String getEmailId() {
		return this.EmailId;
	}
	public void setEmailId(String EmailId) {
		this.EmailId=EmailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", EmailId=" + EmailId + "]";
	}
	
	
}
