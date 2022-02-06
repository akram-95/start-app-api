package com.springboot.start_app_backend.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.Community;
import com.springboot.start_app_backend.models.Post;
import com.springboot.start_app_backend.models.User;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
	Optional<Community> findByName(String Name);

	Page<Community> findByOwnerId(Long ownerId, Pageable pageable);

	Page<Community> findBysubscribersId(Long ownerId, Pageable pageable);

}
