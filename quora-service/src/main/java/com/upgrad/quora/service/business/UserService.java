package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserService {

  @Autowired private UserDao userDao;

  @Autowired private UserAuthDao userAuthDao;

  @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

 /**
  * register new user
  * @param userEntity
  * @return
  * @throws SignUpRestrictedException
  */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
    if (isUserNameExisting(userEntity.getUserName())) {
      throw new SignUpRestrictedException(
          "SGR-001", "Try any other Username, this Username has already been taken");
    }

    if (isEmailExists(userEntity.getEmail())) {
      throw new SignUpRestrictedException(
          "SGR-002", "This user has already been registered, try with any other emailId");
    }
    userEntity.setUuid(UUID.randomUUID().toString());
    String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(encryptedText[0]);
    userEntity.setPassword(encryptedText[1]);
    return userDao.createUser(userEntity);
  }

 /**
  * Signin the user
  * @param username
  * @param password
  * @return
  * @throws AuthenticationFailedException
  */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthEntity signin(final String username, final String password)
      throws AuthenticationFailedException {

    UserEntity userEntity = userDao.getUserByUserName(username);
    if (userEntity == null) {
      throw new AuthenticationFailedException("ATH-001", "This username does not exist");
    }
    final String encryptedPassword =
        passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
    if (!encryptedPassword.equals(userEntity.getPassword())) {
      throw new AuthenticationFailedException("ATH-002", "Password failed");
    }

    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
    UserAuthEntity userAuthEntity = new UserAuthEntity();
    userAuthEntity.setUuid(UUID.randomUUID().toString());
    userAuthEntity.setUserEntity(userEntity);
    final ZonedDateTime now = ZonedDateTime.now();
    final ZonedDateTime expiresAt = now.plusHours(8);
    userAuthEntity.setAccessToken(
        jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
    userAuthEntity.setLoginAt(now);
    userAuthEntity.setExpiresAt(expiresAt);

    userAuthDao.createAuthToken(userAuthEntity);
    userDao.updateUserEntity(userEntity);

    return userAuthEntity;
  }

 /**
  * sign out the user
  * @param accessToken
  * @return
  * @throws SignOutRestrictedException
  */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signout(final String accessToken) throws SignOutRestrictedException {
    UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(accessToken);
    if (userAuthEntity == null) {
      throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
    }
    userAuthEntity.setLogoutAt(ZonedDateTime.now());
    userAuthDao.updateUserAuth(userAuthEntity);
    return userAuthEntity.getUserEntity();
  }

  private boolean isUserNameExisting(final String userName) {
    return userDao.getUserByUserName(userName) != null;
  }

  private boolean isEmailExists(final String email) {
    return userDao.getUserByEmail(email) != null;
  }
}
