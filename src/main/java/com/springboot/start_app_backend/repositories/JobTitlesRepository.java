package com.springboot.start_app_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.start_app_backend.models.JobTitles;

public interface JobTitlesRepository extends JpaRepository<JobTitles, Long> {
	Page<JobTitles> findByTitleContainingIgnoreCase(String title);

}
