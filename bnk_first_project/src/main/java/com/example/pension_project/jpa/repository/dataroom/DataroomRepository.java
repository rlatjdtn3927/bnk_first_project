package com.example.pension_project.jpa.repository.dataroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pension_project.jpa.entity.dataroom.Manual;


public interface DataroomRepository extends JpaRepository<Manual, Long>, JpaSpecificationExecutor<Manual> {

}

