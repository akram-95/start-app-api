package com.springboot.start_app_backend.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.start_app_backend.models.Followers;
import com.springboot.start_app_backend.models.User;

public interface FollowersRepository extends JpaRepository<Followers, Long> {
	List<User> findFollowersById(long id);

	List<User> findFollowingsById(long id);

	Optional<Followers> findFollowersByFromIdAndToId(long fromId, long toId);

}
