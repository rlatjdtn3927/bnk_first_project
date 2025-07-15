package com.example.pension_project.jpa.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pension_project.jpa.entity.admin.Pending;

public interface PendingRepository extends JpaRepository<Pending, Integer>{
	
}
