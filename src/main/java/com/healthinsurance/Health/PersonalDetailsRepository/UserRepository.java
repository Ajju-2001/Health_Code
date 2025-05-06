package com.healthinsurance.Health.PersonalDetailsRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.Users;

public interface UserRepository extends JpaRepository<Users, Integer>{
	Optional<Users> findByUsername(String username);
}
