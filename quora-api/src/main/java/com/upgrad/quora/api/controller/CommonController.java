package com.upgrad.quora.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@RestController
@RequestMapping("/")
public class CommonController {

	@Autowired
	CommonService commonUserService;

	/**
	 * Getuser
	 * @param accessToken
	 * @param userId
	 * @return
	 * @throws AuthorizationFailedException
	 * @throws UserNotFoundException
	 */
	
	@GetMapping("/userprofile/{userId}")
	public ResponseEntity<UserDetailsResponse> getProfile(@RequestHeader("authorization") final String accessToken,
			@PathVariable("userId") final String userId) throws AuthorizationFailedException, UserNotFoundException {
		commonUserService.checkIfTokenIsValid(accessToken);
		UserEntity userEntity = commonUserService.getUserById(userId);
		UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
		userDetailsResponse.setFirstName(userEntity.getFirstName());
		userDetailsResponse.setLastName(userEntity.getLastName());
		userDetailsResponse.setUserName(userEntity.getUserName());
		userDetailsResponse.setEmailAddress(userEntity.getEmail());
		userDetailsResponse.setDob(userEntity.getDob());
		userDetailsResponse.setAboutMe(userEntity.getAboutMe());
		userDetailsResponse.setContactNumber(userEntity.getContactNumber());
		userDetailsResponse.setCountry(userEntity.getCountry());
		return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
	}
}
