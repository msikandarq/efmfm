package com.newtglobal.eFmFmFleet.services;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;

public class EmployeeJSON {
	
	private int userId;
	
	private String employeeId;
	
    private List <EFmFmUserMasterPO>  empData;
    
	private String firstName;

	private String middleName;

	private String lastName;

	private String gender;

	private String employeeDepartment;
	
	private String mobileNumber;

	private String emailId;

	
	private String address;

	
	private String projectIds;
	
	private String projectName;

	private String userName;

	private String spocEmployeeId;

	private String facilityName;

    

	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public List<EFmFmUserMasterPO> getEmpData() {
		return empData;
	}

	public void setEmpData(List<EFmFmUserMasterPO> empData) {
		this.empData = empData;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSpocEmployeeId() {
		return spocEmployeeId;
	}

	public void setSpocEmployeeId(String spocEmployeeId) {
		this.spocEmployeeId = spocEmployeeId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	
	
}
