package com.newtglobal.eFmFmFleet.services;

import java.util.Date;

public class TokenDetails {
	
	
	private String authorizationToken;
	
	private Date takenGenrationTime;
	
	private int userId;
	
	private String secretKey;


	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	public Date getTakenGenrationTime() {
		return takenGenrationTime;
	}

	public void setTakenGenrationTime(Date takenGenrationTime) {
		this.takenGenrationTime = takenGenrationTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	

}
