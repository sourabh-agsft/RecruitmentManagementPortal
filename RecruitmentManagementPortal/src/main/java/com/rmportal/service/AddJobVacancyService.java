package com.rmportal.service;

import org.springframework.stereotype.Component;

import com.rmportal.requestModel.JobVacancyRequestModel;
import com.rmportal.responseModel.AddJobVacancyResponse;
import com.rmportal.utility.CustomException;

@Component
public interface AddJobVacancyService {

	public AddJobVacancyResponse addVacancy(JobVacancyRequestModel jobVacancyRequestModel) throws CustomException;
	
}