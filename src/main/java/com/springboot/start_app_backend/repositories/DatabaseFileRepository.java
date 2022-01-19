package com.springboot.start_app_backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.springboot.start_app_backend.models.DatabaseFile;

import java.util.Optional;



@Transactional
@Repository
public interface DatabaseFileRepository extends JpaRepository<DatabaseFile, Long> {
	Optional<DatabaseFile> findFileByDownloadFileUrl(String downloadUrl);
}
