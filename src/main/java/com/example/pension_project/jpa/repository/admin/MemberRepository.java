package com.example.pension_project.jpa.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pension_project.jpa.entity.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{

	Member findByUsername(String username);

}
