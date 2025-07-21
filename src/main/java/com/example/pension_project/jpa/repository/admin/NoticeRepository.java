package com.example.pension_project.jpa.repository.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pension_project.jpa.entity.admin.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>{

	@Query("SELECT n FROM Notice n ORDER BY n.b_created_at DESC")
    Page<Notice> findAllOrderByCreatedAt(Pageable pageable);
	
	@Query("SELECT n FROM Notice n ORDER BY n.b_created_at DESC")
	List<Notice> findTop5ByCustomQuery(Pageable pageable);


}
