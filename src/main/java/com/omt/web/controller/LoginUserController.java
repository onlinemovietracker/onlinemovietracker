package com.omt.web.controller;

import java.security.MessageDigest;
import java.util.*;

import com.omt.JsonResults.HashedEmail;
import com.omt.JsonResults.Password;
import com.omt.JsonResults.UpdateUser;
import com.omt.domain.AuthenticatedUser;
import com.omt.domain.LoginUser;
import com.omt.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.omt.service.UserNotificationService;
import com.omt.service.UserService;
import sun.rmi.runtime.Log;

import javax.mail.MessagingException;


@RestController
@RequestMapping("/users")
public class LoginUserController {

	private Logger logger = LoggerFactory.getLogger(LoginUserController.class);

	UserService userService;
	private UserNotificationService userNotificationService;

	@Autowired
	public LoginUserController(UserService userService, UserNotificationService userNotificationService) {
		this.userService = userService;
		this.userNotificationService = userNotificationService;
	}

	// Find single user by ID
	@RequestMapping(path = "{id}", method = RequestMethod.GET)
	public LoginUser findOne(@PathVariable("id") Long id) {
		return userService.findOne(id);
	}

	// Function for finding user by activation code
	@RequestMapping(path = "code/{code}", method = RequestMethod.GET)
	public LoginUser findByCodeForActivation(@PathVariable("code") String code) {
		return userService.findByCodeForActivation(code);
	}

	// Function for finding user by username
	@RequestMapping(path = "username/{username}", method = RequestMethod.GET)
	public LoginUser findByUsername(@PathVariable("username") String username) {
		return userService.findByUsername(username);
	}

	// Function for getting all users
	@RequestMapping(method = RequestMethod.GET)
	public List<LoginUser> findAll() {
		List<LoginUser> users = userService.findAll();

		for(LoginUser user : users) {
			user.setPassword(null);
			user.setPasswordTemp(null);
			user.setPasswordActivationLink(null);
			user.setCodeForActivation(null);
			user.setHashed_email(null);
		}

		return users;
	}

	// Function for updating user
	@RequestMapping(method = RequestMethod.PUT)
	public LoginUser update(@RequestBody LoginUser user) {
		return userService.save(user);
	}

	// Function for deleting user
	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		userService.delete(id);
	}

	// Function for changing password
	// TODO exception for short password
	@RequestMapping(value = "/change-password/{username}", method = RequestMethod.POST)
	public void changePassword(@PathVariable("username") String username, @RequestBody Password password) {
		LoginUser user = userService.findByUsername(username);

		if(user != null) {
			if(user.getPassword().equals(password.getOldPassword())) {
				user.setPassword(password.getNewPassword());
				userService.save(user);
			}
		}
	}

	// Function for getting hashed email needed for gravatar service
	@RequestMapping(value = "/hashed-email/{username}", method = RequestMethod.GET)
	public HashedEmail getHashedEmail(@PathVariable("username") String username) {
		LoginUser user = userService.findByUsername(username);
		HashedEmail hashedEmail = new HashedEmail();

		if(user != null) {
			String hash = user.getHashed_email();
			hashedEmail.setHashedEmail(hash);
			return hashedEmail;
		}

		hashedEmail.setHashedEmail("?d=identicon");
		return hashedEmail;
	}

	// Function for logging in user
	// TODO Exception for bad password
	// TODO Exceptions for not active and blocked until
	// TODO check for status, active, and blocked until
	@RequestMapping("/user")
	public AuthenticatedUser getUser(Authentication authentication) {

		LoginUser loginUser = userService.findByUsername(authentication.getName());

		// Logic for checking is user active and not blocked
		if(loginUser != null) {
			if(loginUser.isActive() == false) { // User is not active
				// TODO exception da nalog nije aktiviran
			}

			if(loginUser.isStatus() == true) { // User is blocked
				// TODO exception da je user blokiran
			}

			Date current_date = new Date();

			if(loginUser.getBlockedUntil().after(current_date)) { // User is blocked until
				// TODO exception da je user blokiran do tad i tad
			}
		}

		List<String> roles = new ArrayList<>();
		for(GrantedAuthority authority : authentication.getAuthorities()) {
			roles.add(authority.getAuthority());
		}
		AuthenticatedUser user = new AuthenticatedUser(authentication.getName(), roles);
		return user;
	}

	// Function for requesting new password if user forgotten password
	@RequestMapping(value = "/request-new-password/{email}/", method = RequestMethod.POST)
	public void requestNewPassword(@PathVariable("email") String email) throws MessagingException {
		// Check if user exists and grab an user
		LoginUser user = userService.findByEmail(email);

		if(user != null) {
			// Generate new password
			char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
			StringBuilder sb = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			Random random = new Random();

			for (int i = 0; i < 8; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}

			for(int i = 0; i < 60; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb2.append(c);
			}

			String password = sb.toString();
			String password_activation_link = sb2.toString();

			// Save as temp password
			user.setPasswordTemp(password);
			user.setPasswordActivationLink(password_activation_link);
			userService.save(user);

			// Send email with new password
			userNotificationService.sendNewPassword(email, password, password_activation_link);
		}
	}

	// Function for activating new requested password
	@RequestMapping(value = "/activate-new-password/{password_activation_code}", method = RequestMethod.GET)
	public String activateNewPassword(@PathVariable("password_activation_code") String password_activation_code) {

		LoginUser user = userService.findByPasswordActivationLink(password_activation_code);

		if(user != null) {
			user.setPassword(user.getPasswordTemp());
			user.setPasswordActivationLink(null);
			user.setPasswordTemp(null);
			userService.save(user);

			return "<script>window.location = 'http://localhost:8080/#/messages/success-password-activation';</script>";
		}

		return "<script>window.location = 'http://localhost:8080/#/messages/failed-password-activation';</script>";
	}

	// Function for user registration
	// TODO Exceptions for username, email, short password
	@RequestMapping(method = RequestMethod.POST)
	public LoginUser save(@RequestBody LoginUser user) throws Exception {

		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 60; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}

		String account_activation_link = sb.toString();

		user.setCodeForActivation(account_activation_link);
		user.setActive(false);
		user.setStatus(true);
		user.setSubscription(true);

		Set<Role> role = new HashSet<Role>();

		Role newRole = new Role();
		newRole.setId((long)1);
		newRole.setType(Role.RoleType.ROLE_USER);
		role.add(newRole);

		user.setRoles(role);

		String original = user.getEmail();
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
		StringBuffer hash = new StringBuffer();
		for (byte b : digest) {
			hash.append(String.format("%02x", b & 0xff));
		}

		user.setHashed_email(hash.toString());
		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());

		userNotificationService.sendActivationLink(user.getEmail(), account_activation_link);

		return userService.save(user);
	}

	// Function for activating user
	@RequestMapping(path = "activate/{activation-code}", method = RequestMethod.GET)
	public String activateUser(@PathVariable("activation-code") String code) {
	LoginUser user = findByCodeForActivation(code);
		if(user != null) {
			user.setActive(true);
			user.setCodeForActivation(null);
			userService.save(user);

			return "<script>window.location = 'http://localhost:8080/#/messages/success-account-activation';</script>";
		}
		return "<script>window.location = 'http://localhost:8080/#/messages/failed-account-activation';</script>";
	}

	// Admin function for updating user information about status, active, subscription and Date until user is blocked
	// TODO Exceptions
	@RequestMapping(path = "update-user/{id}", method = RequestMethod.PUT)
	public void updateUser(@PathVariable("id") Long id, @RequestBody UpdateUser userData) {

		//Grab an user
		LoginUser user = userService.findOne(id);

		// Update fields
		if(user != null) {
			user.setSubscription(userData.isSubscription());
			user.setActive(userData.isActive());
			user.setStatus(userData.isStatus());
			user.setBlockedUntil(userData.getBlockedUntil());

			Date current_date = new Date();

			if(current_date.before(userData.getBlockedUntil())) {
				user.setStatus(false);
			}

			user.setUpdatedDate(current_date);
			userService.save(user);
		}
	}
}
