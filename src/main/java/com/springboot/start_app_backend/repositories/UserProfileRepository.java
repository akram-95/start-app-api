package com.springboot.start_app_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.start_app_backend.models.UserProfile;



@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
