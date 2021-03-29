package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;

@Repository

public class UserAuthDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Fetch the UserAuthEntity based on access token
	 * 
	 * @param accessToken
	 * @return
	 */
	public UserAuthEntity getUserAuthByToken(final String accessToken) {
		try {
			TypedQuery<UserAuthEntity> typedQuery = entityManager
					.createQuery("select u from UserAuthEntity u where u.accessToken=:accessToken",
							UserAuthEntity.class)
					.setParameter("accessToken", accessToken);

			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}

	}

	/**
	 * Save UserAuthentity to the Database
	 * 
	 * @param userAuthEntity
	 * @return
	 */
	public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
		entityManager.persist(userAuthEntity);
		return userAuthEntity;
	}

	/**
	 * Update the existing UserAuthEntity
	 * 
	 * @param updatedUserAuthEntity
	 */
	public void updateUserAuth(final UserAuthEntity updatedUserAuthEntity) {
		entityManager.merge(updatedUserAuthEntity);
	}
}
