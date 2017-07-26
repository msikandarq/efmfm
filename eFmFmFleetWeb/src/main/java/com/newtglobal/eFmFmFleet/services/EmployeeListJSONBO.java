package com.newtglobal.eFmFmFleet.services;

import java.util.List;

import javax.persistence.Transient;

import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCapacityMasterPO;

public class EmployeeListJSONBO {

	
	List<EmployeeJSON> employeeDetails;
	
	private int capacity;

	private String vehicleType;
	
	@Transient
	private int userId;		
	
	@Transient
	private int branchId;		
	
	@Transient
	private String combinedFacility;
	
	@Transient
	private int vehicleCapacityId;

	
	List<EFmFmVehicleCapacityMasterPO> vehicleTypeConf;

	public List<EmployeeJSON> getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(List<EmployeeJSON> employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public List<EFmFmVehicleCapacityMasterPO> getVehicleTypeConf() {
		return vehicleTypeConf;
	}

	public void setVehicleTypeConf(List<EFmFmVehicleCapacityMasterPO> vehicleTypeConf) {
		this.vehicleTypeConf = vehicleTypeConf;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getVehicleCapacityId() {
		return vehicleCapacityId;
	}

	public void setVehicleCapacityId(int vehicleCapacityId) {
		this.vehicleCapacityId = vehicleCapacityId;
	}
	
	
	
}
