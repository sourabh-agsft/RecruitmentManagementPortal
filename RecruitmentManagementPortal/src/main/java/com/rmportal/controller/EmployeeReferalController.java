package com.rmportal.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmportal.constants.HttpStatusConstants;
import com.rmportal.repository.EmployeeReferalRepository;
import com.rmportal.requestModel.UploadResumeRequestModel;
import com.rmportal.responseModel.EmployeeReferalResponseModel;
import com.rmportal.responseModel.HttpResponseModel;
import com.rmportal.responseModel.UploadResumeResponseModel;
import com.rmportal.service.EmployeeReferalService;
import com.rmportal.utility.ConversionUtility;
import com.rmportal.utility.CustomException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author tejas
 *
 */
@RestController
@Api(value = "EmployeeReferal Controller", description = "Refer the Candidate")
@CrossOrigin("*")
public class EmployeeReferalController {
	
	@Autowired
	ConversionUtility conversionUtility;
	
	@Autowired
	EmployeeReferalRepository employeeReferalRepo;
	
	@Autowired
	EmployeeReferalService employeeReferalService;
	
	
	// Get Employee Details
	@RequestMapping(value = "/getEmployeeReferalList", method = RequestMethod.POST)
	@ApiOperation(value="Get Candidate list for the employee Reference")
	public ResponseEntity<?> getEmployeeReferalList(@RequestParam("referance_email") String referance_email){
		
		System.out.println(referance_email);
		
		EmployeeReferalResponseModel employeeReferalResponseModel = null; 
		
		try {
			employeeReferalResponseModel = employeeReferalService.getEmployeeDetails(referance_email);
		} catch (CustomException e) {
			return ResponseEntity.ok(
					new HttpResponseModel(e.getMessage(), HttpStatusConstants.INTERNAL_SERVER_ERROR.id, employeeReferalResponseModel));
		}
		
		return ResponseEntity.ok(new HttpResponseModel(HttpStatusConstants.OK.getStatus() + " Data Fetched Successfully",
				HttpStatusConstants.OK.id, employeeReferalResponseModel));
		
	}

	@RequestMapping(value = "/uploadResume", method = RequestMethod.POST, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Upload Resume")
	public ResponseEntity<?> uploadResume(@RequestParam("details") String details,
			@RequestParam("file") MultipartFile file) throws CustomException {

		UploadResumeRequestModel uploadResumeRequestModel = null;

		if (file.isEmpty()) {
			return ResponseEntity.ok(new HttpResponseModel(HttpStatus.NO_CONTENT.name() + " Please attach the Resume",
					HttpStatusConstants.OK.id, null));
		}
		if (Objects.isNull(details)) {
			return ResponseEntity.ok(new HttpResponseModel(HttpStatus.NO_CONTENT.name() + " Please Fill the Details",
					HttpStatusConstants.OK.id, null));
		}

		ObjectMapper mapper = new ObjectMapper();

		try {
			uploadResumeRequestModel = mapper.readValue(details, UploadResumeRequestModel.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		UploadResumeResponseModel uploadResumeResponseModel = employeeReferalService.addResume(uploadResumeRequestModel,
				file);

		return ResponseEntity.ok(new HttpResponseModel(HttpStatus.OK.name() + " Data Saved Successfully",
				HttpStatusConstants.OK.id, uploadResumeResponseModel));
	}

}
