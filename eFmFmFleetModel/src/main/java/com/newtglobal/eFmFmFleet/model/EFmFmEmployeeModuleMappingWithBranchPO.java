package com.newtglobal.eFmFmFleet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the EFmFmEmployeeModuleMasterPO database table.
 * 
 */
@Entity
@Table(name = "eFmFmEmployeeModuleMappingWithBranchPO")
public class EFmFmEmployeeModuleMappingWithBranchPO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EmplModuleBranchMappingId")
	private int emplModuleBranchMappingId;

	@Column(name = "EmpModuleStatus", length = 50)
	private boolean empModuleStatus;

	// bi-directional many-to-one association to EFmFmEmployeeModuleMasterPO
	@ManyToOne
	@JoinColumn(name = "EmployeeModuleId")
	private EFmFmEmployeeModuleMasterPO eFmFmEmployeeModuleMasterPO;

	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	public int getEmplModuleBranchMappingId() {
		return emplModuleBranchMappingId;
	}

	public void setEmplModuleBranchMappingId(int emplModuleBranchMappingId) {
		this.emplModuleBranchMappingId = emplModuleBranchMappingId;
	}

	

	public boolean isEmpModuleStatus() {
		return empModuleStatus;
	}

	public void setEmpModuleStatus(boolean empModuleStatus) {
		this.empModuleStatus = empModuleStatus;
	}

	public EFmFmEmployeeModuleMasterPO geteFmFmEmployeeModuleMasterPO() {
		return eFmFmEmployeeModuleMasterPO;
	}

	public void seteFmFmEmployeeModuleMasterPO(EFmFmEmployeeModuleMasterPO eFmFmEmployeeModuleMasterPO) {
		this.eFmFmEmployeeModuleMasterPO = eFmFmEmployeeModuleMasterPO;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}
	
	

}
