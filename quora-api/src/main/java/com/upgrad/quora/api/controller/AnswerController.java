package com.upgrad.quora.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;

@RestController
@RequestMapping("/")
public class AnswerController {

	@Autowired
	private AnswerService answerService;

	/**
	 * Api to create answer to a question
	 * @param accessToken
	 * @param questionId
	 * @param answerRequest
	 * @return
	 * @throws AuthorizationFailedException
	 * @throws InvalidQuestionException
	 */
	@PostMapping("/question/{questionId}/answer/create")
	public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String accessToken,
			@PathVariable("questionId") final String questionId, AnswerRequest answerRequest)
			throws AuthorizationFailedException, InvalidQuestionException {
		AnswerEntity answerEntity = new AnswerEntity();
		answerEntity.setAnswer(answerRequest.getAnswer());
		answerEntity = answerService.createAnswer(answerEntity, accessToken, questionId);
		AnswerResponse answerResponse = new AnswerResponse();
		answerResponse.setId(answerEntity.getUuid());
		answerResponse.setStatus("ANSWER CREATED");
		return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
	}

	/**
	 * Api to update the answer
	 * @param accessToken
	 * @param answerId
	 * @param answerEditRequest
	 * @return
	 * @throws AuthorizationFailedException
	 * @throws AnswerNotFoundException
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AnswerEditResponse> editAnswer(@RequestHeader("authorization") final String accessToken,
			@PathVariable("answerId") final String answerId, AnswerEditRequest answerEditRequest)
			throws AuthorizationFailedException, AnswerNotFoundException {
		AnswerEditResponse answerEditResponse = new AnswerEditResponse();
		AnswerEntity answerEntity = answerService.editAnswerContent(accessToken, answerId, answerEditRequest.getContent());
		answerEditResponse.setId(answerEntity.getUuid());
		answerEditResponse.setStatus("ANSWER EDITED");
		return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
	}

	/**
	 * delete a answer using answerId
	 *
	 * @param answerId    id of the answer to be delete.
	 * @param accessToken token to authenticate user.
	 * @return Id and status of the answer deleted.
	 * @throws AuthorizationFailedException In case the access token is invalid.
	 * @throws AnswerNotFoundException      if answer with answerId doesn't exist.
	 */
	@RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String accessToken,
			@PathVariable("answerId") String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
		AnswerEntity answerEntity = answerService.deleteAnswer(answerId, accessToken);
		AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid())
				.status("ANSWER DELETED");
		return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
	}

	/**
	 * Api to get all answers for the question
	 * @param accessToken
	 * @param questionId
	 * @return
	 * @throws AuthorizationFailedException
	 * @throws InvalidQuestionException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
			@RequestHeader("authorization") final String accessToken, @PathVariable("questionId") String questionId)
			throws AuthorizationFailedException, InvalidQuestionException {
		List<AnswerEntity> answers = answerService.getAllAnswersToQuestion(questionId, accessToken);
		List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<>();
		for (AnswerEntity answerEntity : answers) {
			AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
			answerDetailsResponse.setId(answerEntity.getUuid());
			answerDetailsResponse.setQuestionContent(answerEntity.getQuestionEntity().getContent());
			answerDetailsResponse.setAnswerContent(answerEntity.getAnswer());
			answerDetailsResponses.add(answerDetailsResponse);
		}
		return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses, HttpStatus.OK);
	}
}
