package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;



@Repository

public class QuestionDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Save QuestionEntity to the database
	 * 
	 * @param questionEntity questionEntity
	 * @return
	 */

	public QuestionEntity createQuestion(QuestionEntity questionEntity) {
		entityManager.persist(questionEntity);
		return questionEntity;
	}

	/**
	 * Fetch all Questions from question table
	 * 
	 * @return
	 */

	public List<QuestionEntity> getAllQuestions() {
		TypedQuery<QuestionEntity> query = entityManager.createQuery("select q from QuestionEntity q", QuestionEntity.class);
		return query.getResultList();
	}
	
	/**
	 * Fetch the question based on questionId
	 * @param questionId
	 * @return
	 */

	public QuestionEntity getQuestionById(final String questionId) {
		try {
			TypedQuery<QuestionEntity> typedQuery = entityManager
					.createQuery("select q from QuestionEntity q where q.uuid=:uuid", QuestionEntity.class)
					.setParameter("uuid", questionId);

			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Delete the question
	 * @param question
	 */
	
	public void deleteQuestion(QuestionEntity question) {
		entityManager.remove(question);
		
	}
	/**
	 * Update the question
	 * @param question
	 */
	public void updateQuestion(QuestionEntity question) {
		entityManager.merge(question);
		
	}
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<QuestionEntity> getAllQuestionsByUser(final UserEntity userId) {
	    return entityManager
	        .createNamedQuery("getQuestionByUser", QuestionEntity.class)
	        .setParameter("user", userId)
	        .getResultList();
	  }
	
}
