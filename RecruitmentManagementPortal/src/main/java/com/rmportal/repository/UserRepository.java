package com.rmportal.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmportal.model.User;

/**
 * @author tejas
 *
 */

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Integer> {

	
	@Query(value = "SELECT * FROM user WHERE email =:email AND password=:password", nativeQuery = true)
	User findByEmail(@Param("email")String username, @Param("password")String password);
}
