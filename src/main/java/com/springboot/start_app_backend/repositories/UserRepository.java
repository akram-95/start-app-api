package com.springboot.start_app_backend.repositories;

import java.util.Optional;

import com.springboot.start_app_backend.models.CustomUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.start_app_backend.models.User;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    void deleteById(Long id);

    @Query(value = "SELECT id,username,email , creation_date ,is_enabled FROM Users u INNER JOIN (SELECT from_user_fk,to_user_fk from followers) as f on u.id = f.to_user_fk where f.from_user_fk = ?1", nativeQuery = true)
    Page<User> findByFromId(long fromId, Pageable pageable);

    @Query(value = "SELECT id,username,email , creation_date ,is_enabled FROM Users u INNER JOIN (SELECT from_user_fk,to_user_fk from followers) as f on u.id = f.from_user_fk where f.to_user_fk = ?1", nativeQuery = true)
    Page<User> findByToId(long fromId, Pageable pageable);

}
