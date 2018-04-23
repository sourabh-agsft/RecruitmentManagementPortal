package com.rmportal.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmportal.model.EmployeeReferal;
import com.rmportal.model.User;

@Repository
public interface EmployeeReferalRepository extends CrudRepository<EmployeeReferal, Integer>{
	
	@Query(value = "SELECT * FROM employee_referal WHERE referance_email =:referance_email", nativeQuery = true)

	EmployeeReferal findByReferalEmail(@Param("referance_email")String referance_email);
}
