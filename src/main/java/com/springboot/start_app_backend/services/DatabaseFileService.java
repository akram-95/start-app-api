package com.springboot.start_app_backend.services;

import java.io.IOException;
import java.util.*;
import java.util.UUID; 
import org.hibernate.dialect.DB2390Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springboot.start_app_backend.exceptions.FileNotFoundException;
import com.springboot.start_app_backend.exceptions.FileStorageException;
import com.springboot.start_app_backend.models.DatabaseFile;
import com.springboot.start_app_backend.repositories.DatabaseFileRepository;

import org.springframework.util.StringUtils;

@Service
public class DatabaseFileService {

    @Autowired
    private DatabaseFileRepository dbFileRepository;

    public DatabaseFile storeFile(MultipartFile file) {
    	
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        UUID uuid=UUID.randomUUID();

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/" + uuid +  "/")
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();
            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), file.getBytes() , fileDownloadUri);
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DatabaseFile getFile(long id) {
    	
        return dbFileRepository.findById(id)
            .orElseThrow(() -> new FileNotFoundException("File not found with id " + id));
    }
    public List<DatabaseFile> getAllFiles(){
    	return dbFileRepository.findAll();
    }
    public DatabaseFile getFileByDownloadUrl(String downloadUrl) {
    	Optional<DatabaseFile> fileOptional = dbFileRepository.findFileByDownloadFileUrl(downloadUrl);
    	if(fileOptional.isPresent()) {
    		return fileOptional.get();
    	}
    	return null;
    }
    public DatabaseFile removeFileByDownloadUrl(String downloadUrl) {
    	Optional<DatabaseFile> fileOptional = dbFileRepository.findFileByDownloadFileUrl(downloadUrl);
    	if(fileOptional.isPresent()) {
    		 dbFileRepository.delete(fileOptional.get());
    		 return fileOptional.get();
    	}
    	return null;
    }
    public DatabaseFile removeFileById(long id) {
    	Optional<DatabaseFile> fileOptional = dbFileRepository.findById(id);
    	if(fileOptional.isPresent()) {
    		 dbFileRepository.delete(fileOptional.get());
    		 return fileOptional.get();
    	}
    	return null;
    }
}
