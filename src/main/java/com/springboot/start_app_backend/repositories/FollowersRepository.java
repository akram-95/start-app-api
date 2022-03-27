package com.springboot.start_app_backend.repositories;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.start_app_backend.models.Followers;
import com.springboot.start_app_backend.models.User;
import org.springframework.data.repository.query.Param;

public interface FollowersRepository extends JpaRepository<Followers, Long> {
    Page<Followers> findByFromIdAndToId(long fromId, long toId, Pageable pageable);

    @Query(value = "select * from followers fl where fl.from_user_fk = ?1", nativeQuery = true)
    Page<Followers> findByFromId(long fromId, Pageable pageable);

    @Query(value = "select * from followers fl where fl.to_user_fk = ?1", nativeQuery = true)
    Page<Followers> findByToId(long toId, Pageable pageable);

    @Query(value = "select * from followers fl where fl.to_user_fk in :ids AND fl.from_user_fk = id", nativeQuery = true)
    List<Followers> findByFromIdAndToIds(@Param("ids") List<Long> userIdList, @Param("id") long userId, Pageable pageable);

    @Query(value = "select * from followers fl where fl.to_user_fk = id and  fl.from_user_fk in :ids ", nativeQuery = true)
    List<Followers> findByFromIdsAndToId(@Param("ids") List<Long> userIdList, @Param("id") long userId, Pageable pageable);
}
