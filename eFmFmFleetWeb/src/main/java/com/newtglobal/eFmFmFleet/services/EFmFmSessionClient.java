package com.newtglobal.eFmFmFleet.services;

import java.io.Serializable;

public class EFmFmSessionClient implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int userId;
	private int branchId;
	private String userIPAddress;
	private String userAgent;
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
	public String getUserIPAddress() {
		return userIPAddress;
	}
	public void setUserIPAddress(String userIPAddress) {
		this.userIPAddress = userIPAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	
}
