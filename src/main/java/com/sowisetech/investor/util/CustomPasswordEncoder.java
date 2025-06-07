package com.sowisetech.investor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sowisetech.investor.service.InvestorService;

public class CustomPasswordEncoder implements PasswordEncoder {

	@Autowired
	InvestorService investorService;

	@Override
	public String encode(CharSequence rawPassword) {

		String encryptedPassword = investorService.encrypt((String) rawPassword);
		// System.out.println("in encode method " + encryptedPassword);
		return encryptedPassword;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if ((rawPassword.toString()).equals(investorService.decrypt(encodedPassword))) {
			return true;
		} else {
			return false;
		}
	}

}