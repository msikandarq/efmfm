package com.newtglobal.eFmFmFleet.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the EFmFmEmployeeModuleMasterPO database table.
 * 
 */
@Entity
@Table(name="eFmFmEmployeeModuleMasterPO")
public class EFmFmEmployeeModuleMasterPO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="employeeModuleId")
	private int employeeModuleId;

	@Column(name="EmpModuleName", length=200)
	private String empModuleName;
	
	@Column(name="EmpModuleDescription", length=200)
	private String empModuleDescription;
	
	@Column(name="EmpModuleStatus", length=100)
	private String empModuleStatus;

	public int getEmployeeModuleId() {
		return employeeModuleId;
	}

	public void setEmployeeModuleId(int employeeModuleId) {
		this.employeeModuleId = employeeModuleId;
	}

	public String getEmpModuleName() {
		return empModuleName;
	}

	public void setEmpModuleName(String empModuleName) {
		this.empModuleName = empModuleName;
	}

	public String getEmpModuleDescription() {
		return empModuleDescription;
	}

	public void setEmpModuleDescription(String empModuleDescription) {
		this.empModuleDescription = empModuleDescription;
	}

	public String getEmpModuleStatus() {
		return empModuleStatus;
	}

	public void setEmpModuleStatus(String empModuleStatus) {
		this.empModuleStatus = empModuleStatus;
	}
	
	
	
	}
