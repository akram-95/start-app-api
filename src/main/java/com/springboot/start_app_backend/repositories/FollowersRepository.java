package com.springboot.start_app_backend.repositories;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.start_app_backend.models.Followers;
import com.springboot.start_app_backend.models.User;

public interface FollowersRepository extends JpaRepository<Followers, Long> {
	Page<Followers> findByFromIdAndToId(long fromId, long toId, Pageable pageable);
}
