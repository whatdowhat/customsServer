package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public User findByLoginId(String loginId);
}
