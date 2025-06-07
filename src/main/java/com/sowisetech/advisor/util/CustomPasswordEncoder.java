package com.sowisetech.advisor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sowisetech.advisor.service.AdvisorService;

public class CustomPasswordEncoder implements PasswordEncoder {

	@Autowired
	AdvisorService advisorService;

	@Override
	public String encode(CharSequence rawPassword) {

		String encryptedPassword = advisorService.encrypt((String) rawPassword);
		// System.out.println("in encode method " + encryptedPassword);
		return encryptedPassword;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if ((rawPassword.toString()).equals(advisorService.decrypt(encodedPassword))) {
			return true;
		} else {
			return false;
		}
	}

}