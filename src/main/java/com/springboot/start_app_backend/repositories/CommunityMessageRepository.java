package com.springboot.start_app_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.start_app_backend.models.CommunityMessage;

public interface CommunityMessageRepository extends JpaRepository<CommunityMessage, Long> {
	Page<CommunityMessage> findByCommunityId(long id, Pageable pageable);

}
