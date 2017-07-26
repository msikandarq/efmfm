package com.newtglobal.eFmFmFleet.model;

import java.util.Date;

public class TokenDetails {
	
	
	private String authorizationToken;
	
	private Date takenGenrationTime;
	
	private String branchCode;
	
	private int userId;

	private int branchId;
	
	private String userName;

	
	

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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	

}
