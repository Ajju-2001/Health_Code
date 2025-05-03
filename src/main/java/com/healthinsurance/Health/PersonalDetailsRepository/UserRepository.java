package com.healthinsurance.Health.PersonalDetailsRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
}
