package com.springboot.start_app_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.models.JobTitles;
import com.springboot.start_app_backend.repositories.JobTitlesRepository;

@RestController
@RequestMapping("/api/v1/job_titles")
public class JobTitlesController {
	@Autowired
	private JobTitlesRepository jobTitlesRepository;

	@GetMapping("/GetByTitle")
	public String getJobTitlesByTitle(@RequestBody String title) {
		
		PageRequest pageRequest = PageRequest.of(0, 20);
		Page<JobTitles> resultPage = jobTitlesRepository.findByTitleContainingIgnoreCase(title, pageRequest);
		return "Sized "  + title +  resultPage.getSize();

	}

}
