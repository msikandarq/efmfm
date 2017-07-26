package com.newtglobal.eFmFmFleet.eFmFmFleet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryption {

	/*
	 * Password encryption logic done using “BCryptPasswordEncoder
	 * (hashing algorithm with randomly generated salt).
	 * @param userNewPassword
	 * @return encrypted password		  
	 */
	public String PasswordEncoderGenerator(String inputPassword) {		
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String hashedPassword = passwordEncoder.encode(inputPassword);					
				return hashedPassword;			  
	}	

}
