package com.springboot.start_app_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.start_app_backend.models.Followers;

public interface FollowersRepository extends JpaRepository<Followers, Long> {
	Optional<Followers> findByFromIdAndToId(long fromId, long toId);
}
