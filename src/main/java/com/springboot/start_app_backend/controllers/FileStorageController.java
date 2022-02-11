package com.springboot.start_app_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.start_app_backend.services.AmazonClient;

@RestController
@RequestMapping("/api/storage/aws")
public class FileStorageController {
	private AmazonClient amazonClient;

	@Autowired
	FileStorageController(AmazonClient amazonClient) {
		this.amazonClient = amazonClient;
	}

	@PostMapping("/uploadFile")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws Exception {
		return this.amazonClient.uploadFile(file);
	}

	@DeleteMapping("/deleteFile/{url}")
	public String deleteFile(@PathVariable String fileUrl) throws Exception {
		return "Aka";
		//return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
	}
}
