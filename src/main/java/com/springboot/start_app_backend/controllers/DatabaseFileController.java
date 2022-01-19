package com.springboot.start_app_backend.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.start_app_backend.models.DatabaseFile;
import com.springboot.start_app_backend.services.DatabaseFileService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/storage")
public class DatabaseFileController {
    @Autowired
    private DatabaseFileService fileStorageService;
    @PostMapping(value = "/uploadFile")
    public DatabaseFile uploadFile(@RequestParam(value = "file",required = false) MultipartFile file) {
        return fileStorageService.storeFile(file);

       
    }
    @PostMapping("/uploadMultipleFiles")
    public List < DatabaseFile > uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
            .stream()
            .map(file -> uploadFile(file))
            .collect(Collectors.toList());
    }
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity < Resource > downloadFile(@PathVariable long id, HttpServletRequest request) {
        // Load file as Resource
        DatabaseFile databaseFile = fileStorageService.getFile(id);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
            .body(new ByteArrayResource(databaseFile.getData()));
    }
    
    @GetMapping("/files/{id}")
    public ResponseEntity < DatabaseFile > getFileById(@PathVariable("id") long id) {
    	DatabaseFile filOptional = fileStorageService.getFile(id);
    	return ResponseEntity.ok(filOptional);
     
    }
  
    @GetMapping("/files")
    public ResponseEntity <List<DatabaseFile>> getAllFiles() {
    	List<DatabaseFile> files = fileStorageService.getAllFiles();
    	return ResponseEntity.ok(files);
     
    }
    @GetMapping("/filesUrl/{downloadUrl}")
    public ResponseEntity <DatabaseFile> getFileByDownloadUrl(@PathVariable("downloadUrl") String downloadUrl) {
    	DatabaseFile filOptional = fileStorageService.getFileByDownloadUrl(downloadUrl);
    	return ResponseEntity.ok(filOptional);
    }
    @DeleteMapping("/filesUrl/{downloadUrl}")
    public ResponseEntity <DatabaseFile> deleteFileByDownloadUrl(@PathVariable("downloadUrl") String downloadUrl) {
    	DatabaseFile filOptional = fileStorageService.removeFileByDownloadUrl(downloadUrl);
    	
    	return ResponseEntity.ok(filOptional);
    }
    @DeleteMapping("/files/{id}")
    public ResponseEntity <DatabaseFile> deleteFileById(@PathVariable("id") long id) {
    	DatabaseFile filOptional = fileStorageService.removeFileById(id);
    	return ResponseEntity.ok(filOptional);
    }
    
    
}
