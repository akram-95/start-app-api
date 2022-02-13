package com.springboot.start_app_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.start_app_backend.models.Followers;

public interface FollowersRepository extends JpaRepository<Followers, Long> {

}
