package com.rmportal.requestModel;

import lombok.Data;

@Data
public class UpdateRequestModel {

	// int user_id;
	String employee_id;
	String user_name;
	String first_name;
	String last_name;
	String email;
//	Department department;
	String address;
	String dateOfBirth;
	String city;
	String country;
	long mobile;
	String blood_group;

}
