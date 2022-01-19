package com.springboot.start_app_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.start_app_backend.models.Token;



@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
	Optional<Token> findTokenByTokenValue(String tokenValue);
}
