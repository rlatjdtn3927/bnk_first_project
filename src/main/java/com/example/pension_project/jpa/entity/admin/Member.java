package com.example.pension_project.jpa.entity.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="tbl_member")
@Data
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer userid;
	
	private String username;
	private String password;
	private String name;
	private String email;
	private String phone;
	private String roll;
}
