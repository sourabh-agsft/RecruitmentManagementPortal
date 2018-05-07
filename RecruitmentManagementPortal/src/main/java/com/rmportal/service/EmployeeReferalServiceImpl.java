package com.rmportal.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rmportal.model.EmployeeReferal;
import com.rmportal.model.ReferralStatus;
import com.rmportal.model.User;
import com.rmportal.repository.EmployeeReferalRepository;
import com.rmportal.repository.ReferralStatusRepository;
import com.rmportal.repository.UserRepository;
import com.rmportal.requestModel.ReferralStatusRequestModel;
import com.rmportal.requestModel.UploadResumeRequestModel;
import com.rmportal.responseModel.CandidateJoinResponseModel;
import com.rmportal.responseModel.ChangeReferralStatusResponse;
import com.rmportal.responseModel.EmployeeReferalResponseModel;
import com.rmportal.responseModel.UploadResumeResponseModel;
import com.rmportal.utility.ConversionUtility;
import com.rmportal.utility.CustomException;

/**
 * @author saurabh
 *
 */
@Service
public class EmployeeReferalServiceImpl implements EmployeeReferalService {

	@Autowired
	ConversionUtility conversionUtility;

	@Autowired
	EmployeeReferalRepository employeeReferalRepository;

	@Autowired
	ReferralStatusRepository referralStatusRepository;
	
	@Autowired
	UserRepository userRepo;

	@Autowired
	EmployeeReferalRepository employeeReferralRepo;

	// Upload Resume
	@Override
	public UploadResumeResponseModel addResume(UploadResumeRequestModel uploadResumeRequestModel, MultipartFile file)
			throws CustomException {

		EmployeeReferal employeeReferal = null;
		UploadResumeResponseModel uploadResumeResponseModel = null;

		try {
			employeeReferal = conversionUtility.addEmployeeResume(uploadResumeRequestModel, file);

			if (Objects.isNull(employeeReferal)) {
				throw new CustomException(204, "No content filled");
			}

			employeeReferalRepository.save(employeeReferal);
			uploadResumeResponseModel = new UploadResumeResponseModel();
			uploadResumeResponseModel.setReference_id(employeeReferal.getReferal_id());
			uploadResumeResponseModel.setApplicant_name(employeeReferal.getApplicant_name());
			uploadResumeResponseModel.setDate(employeeReferal.getDate());
			uploadResumeResponseModel.setFileName(file.getOriginalFilename());
			uploadResumeResponseModel.setFileExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uploadResumeResponseModel;
	}

	// Get details of Referred Candidate
	@Override
	public List<EmployeeReferalResponseModel> getEmployeeDetails(String referance_email) throws CustomException {
		
		List<EmployeeReferal> employeeReferal = employeeReferalRepository.findByEmployeeEmail(referance_email);

		if (Objects.isNull(employeeReferal) || employeeReferal.isEmpty()) {
			throw new CustomException(500, " Invalid email id");
		}
		return conversionUtility.convertTOGetEmployees(employeeReferal);
	}

	// Retrieve the Resume from database
	@Override
	public EmployeeReferal fetchResume(int job_vacancy_id) throws CustomException {

		EmployeeReferal employeeReferal = employeeReferalRepository.findOne(job_vacancy_id);
		if(Objects.isNull(employeeReferal)){
			throw new CustomException(204, "Invalid job vacancy id");
		}
		
		return employeeReferal;
	}

	// Get List of All Applied candidate
	@Override
	public List<EmployeeReferalResponseModel> getEmployeeReferalList() throws CustomException {
		List<EmployeeReferal> empReferal = (List<EmployeeReferal>) employeeReferalRepository.findAll();
		if(Objects.isNull(empReferal))
		{
			throw new CustomException(HttpStatus.NOT_FOUND.value(),"No Candidate are Referred");
		}
		return conversionUtility.getAllEmployeeReferal(empReferal);
		// return null;
	}

	

	// Change the Referral Status
	@Override
	public ChangeReferralStatusResponse setReferralStatus(ReferralStatusRequestModel referralStatusRequestModel)
			throws CustomException {

		if(referralStatusRequestModel.getReferal_id()==0){
			throw new CustomException(413, "Invalid referral id");
			}
			/*if(referralStatusRequestModel.getApplicant_email().isEmpty()){
			throw new CustomException(413, "Invalid email id");
			}*/
			if(referralStatusRequestModel.getReferral_status().isEmpty()){
			throw new CustomException(413, "Status is invalid");
			}
		
		
		EmployeeReferal employeeReferal = employeeReferalRepository.findOne(referralStatusRequestModel.getReferal_id());
		if (Objects.isNull(employeeReferal)) {
			throw new CustomException(204, " Invalid Referral id");
		}

		/*if (referralStatusRequestModel.getReferral_status().compareTo("Joined") == 0) {
			if (referralStatusRequestModel.getApplicant_email().isEmpty()) {
				throw new CustomException(204, " Email is Mandatory");
			}
			User user = userRepo.findByEmail(referralStatusRequestModel.getApplicant_email());
			if (Objects.isNull(user)) {
				throw new CustomException(204, " User Not yet Registered");
			}
			if (!user.isActive()) {
				throw new CustomException(403, " User is not Active");
			}
			employeeReferal.setApplicant_email(user.getEmail());
		}*/

		employeeReferal.setApplication_status(referralStatusRequestModel.getReferral_status());
		Date date = new Date();
		employeeReferal.setDate(date);
		employeeReferalRepository.save(employeeReferal);
		ChangeReferralStatusResponse changeReferralResponse = new ChangeReferralStatusResponse();
		changeReferralResponse.setDate(employeeReferal.getDate());
		changeReferralResponse.setApplicant_name(employeeReferal.getApplicant_name());
		return changeReferralResponse;
	}

	// Get Referral Status List
	@Override
	public List<ReferralStatus> getReferralStatusList() throws CustomException {

		List<ReferralStatus> referralStatusList = (List<ReferralStatus>) referralStatusRepository.findAll();
		if(Objects.isNull(referralStatusList)){
			throw new CustomException(HttpStatus.NOT_FOUND.value(),"not candidate are refered");
		}
		
		return referralStatusList;
	}

	// join candidate list
	@Override
	public List<CandidateJoinResponseModel> getJoinCandidateList() throws CustomException {
		List<EmployeeReferal> joinCandicate = employeeReferalRepository.findByApplicationStatus();
		if(Objects.isNull(joinCandicate)){
			throw new CustomException(HttpStatus.NOT_FOUND.value(),"No candidate are joined");
		}

		return conversionUtility.getJoinCandidateList(joinCandicate);
	}
}
