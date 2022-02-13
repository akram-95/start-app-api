package com.springboot.start_app_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.start_app_backend.models.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
	@EntityGraph(attributePaths = { "followers", "following" })
	Optional<User> findByUsername(String username);
	@EntityGraph(attributePaths = { "followers", "following" })
	Optional<User> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	void deleteById(Long id);

	@EntityGraph(attributePaths = { "followers", "following" })
	Optional<User> findById(Long aLong);

}
