package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;

@Repository

public class UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Create the new User in database
	 * 
	 * @param userEntity
	 * @return
	 */
	public UserEntity createUser(UserEntity userEntity) {
		entityManager.persist(userEntity);
		return userEntity;

	}

	/**
	 * Update the user
	 * 
	 * @param updatedUserEntity
	 */
	public void updateUserEntity(final UserEntity updatedUserEntity) {
		entityManager.merge(updatedUserEntity);
	}

	/**
	 * Delete the user
	 * 
	 * @param userId
	 * @return
	 */
	public UserEntity deleteUserEntity(final String userId) {
		UserEntity deleteUser = getUserById(userId);
		if (deleteUser != null) {
			this.entityManager.remove(deleteUser);
		}
		return deleteUser;
	}

	/**
	 * Fetch user based on user id
	 * 
	 * @param userId
	 * @return
	 */
	public UserEntity getUserById(final String userId) {
		try {
			TypedQuery<UserEntity> typedQuery = entityManager
					.createQuery("select u from UserEntity u where u.uuid=:userId", UserEntity.class)
					.setParameter("userId", userId);

			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}

	}

	/**
	 * Fetch the user based on emailID
	 * 
	 * @param email
	 * @return
	 */
	public UserEntity getUserByEmail(final String email) {
		try {
			TypedQuery<UserEntity> typedQuery = entityManager
					.createQuery("select u from UserEntity u where u.email=:email", UserEntity.class)
					.setParameter("email", email);

			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}

	}

	/**
	 * Fetch user from db based on username
	 * 
	 * @param userName
	 * @return
	 */
	public UserEntity getUserByUserName(final String userName) {
		try {
			TypedQuery<UserEntity> typedQuery = entityManager
					.createQuery("select u from UserEntity u where u.userName=:userName", UserEntity.class)
					.setParameter("userName", userName);

			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}

	}

}
