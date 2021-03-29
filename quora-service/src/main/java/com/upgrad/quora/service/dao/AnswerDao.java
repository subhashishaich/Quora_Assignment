package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;

@Repository

public class AnswerDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Saves AnswerEntity to the database
	 * 
	 * @param questionEntity
	 * @return
	 */

	public AnswerEntity createAnswer(AnswerEntity answerEntity) {
		entityManager.persist(answerEntity);
		return answerEntity;
	}

	/**
	 * Fetch all the answers from the database
	 * 
	 * @return
	 */
	public List<AnswerEntity> getAllAnswers() {
		TypedQuery<AnswerEntity> query = entityManager.createQuery("select a from AnswerEntity a", AnswerEntity.class);
		return query.getResultList();
	}

	/**
	 * Fetch the question based on questionId
	 * 
	 * @param questionId
	 * @return
	 */

	public AnswerEntity getAnswerById(final String answerId) {
		try {
			TypedQuery<AnswerEntity> typedQuery = entityManager
					.createQuery("select a from AnswerEntity a where a.uuid=:uuid", AnswerEntity.class)
					.setParameter("uuid", answerId);

			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Update the answer to the database
	 * 
	 * @param answerEntity
	 */
	public void updateAnswer(AnswerEntity answerEntity) {
		entityManager.merge(answerEntity);
	}

	/**
	 * Delete the answer based on answerid
	 * 
	 * @param answerId
	 * @return
	 */
	public AnswerEntity deleteAnswer(final String answerId) {
		AnswerEntity deleteAnswer = getAnswerById(answerId);
		if (deleteAnswer != null) {
			entityManager.remove(deleteAnswer);
		}
		return deleteAnswer;
	}

	/**
	 * Get all the answers for a question based on question Id
	 * @param questionId
	 * @return
	 */
	 public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
		    return entityManager
		        .createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class)
		        .setParameter("uuid", questionId)
		        .getResultList();
		  }
}
