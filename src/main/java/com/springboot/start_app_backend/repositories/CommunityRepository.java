package com.springboot.start_app_backend.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
	final String queryOwnderAndSubscribedCommunityByUserId = "SELECT cm.* FROM communities cm INNER JOIN users us ON cm.user_id = us.id  where cm.user_id = ?1 UNION SELECT cm.* FROM communities cm INNER JOIN communities_users com ON cm.id = com.community_id where com.user_id = ?1";

	Optional<Community> findByName(String Name);

	Page<Community> findByOwnerId(Long ownerId, Pageable pageable);

	Page<Community> findBysubscribersId(Long ownerId, Pageable pageable);

	@Query(value = queryOwnderAndSubscribedCommunityByUserId, nativeQuery = true)
	Page<Community> findAllOwnderAndSubscribedCommunityByUserId(Long userId, Pageable pageable);
	@Query(value = "select count(cm) from communities cm where cm.id !=?1",nativeQuery=true)
    long findAllCommunitiesByQuery(long id);

}
