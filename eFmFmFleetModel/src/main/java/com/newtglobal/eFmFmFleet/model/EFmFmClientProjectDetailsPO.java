package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the eFmFmClientProjectDetailsPO database table.
 * 
 */
@Entity
@Table(name="eFmFmClientProjectDetails")
public class EFmFmClientProjectDetailsPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ProjectId", length=10)
	private int projectId;
	
	
	@Column(name="ClientProjectId", length=50)
	private String clientProjectId;
	
	@Column(name="EmployeeProjectName", length=50)
	private String employeeProjectName;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ProjectAllocationStarDate", length=30)
	private Date projectAllocationStarDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ProjectAllocationEndDate", length=30)
	private Date projectAllocationEndDate;

	@Column(name="IsActive", length=10)
	private String isActive;
	
	@Column(name="Remarks", length=250)
	private String remarks;
		
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreatedDate", length=30)
	private Date createdDate;
	
	
	//bidirectional manytoone association to eFmFmClientProjectDetails
	@OneToMany(mappedBy="eFmFmClientProjectDetails")
	private List<EFmFmUserMasterPO> eFmFmUserMaster;
	
	
	//bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;	
	
	//bidirectional one-to-many association to EFmFmEmployeeProjectDetailsPO
	@OneToMany(mappedBy="eFmFmClientProjectDetails")
	private List<EFmFmEmployeeProjectDetailsPO> eFmFmEmployeeProjectDetailsPO;
	
	
	@Transient
	private String startDate;

	@Transient
	private String endDate;
	
	@Transient
	private String combinedFacility;
	
	@Transient
	private int userId;
	

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getClientProjectId() {
		return clientProjectId;
	}

	public void setClientProjectId(String clientProjectId) {
		this.clientProjectId = clientProjectId;
	}

	public String getEmployeeProjectName() {
		return employeeProjectName;
	}

	public void setEmployeeProjectName(String employeeProjectName) {
		this.employeeProjectName = employeeProjectName;
	}

	public Date getProjectAllocationStarDate() {
		return projectAllocationStarDate;
	}

	public void setProjectAllocationStarDate(Date projectAllocationStarDate) {
		this.projectAllocationStarDate = projectAllocationStarDate;
	}

	public Date getProjectAllocationEndDate() {
		return projectAllocationEndDate;
	}

	public void setProjectAllocationEndDate(Date projectAllocationEndDate) {
		this.projectAllocationEndDate = projectAllocationEndDate;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public List<EFmFmUserMasterPO> geteFmFmUserMaster() {
		return eFmFmUserMaster;
	}

	public void seteFmFmUserMaster(List<EFmFmUserMasterPO> eFmFmUserMaster) {
		this.eFmFmUserMaster = eFmFmUserMaster;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<EFmFmEmployeeProjectDetailsPO> geteFmFmEmployeeProjectDetailsPO() {
		return eFmFmEmployeeProjectDetailsPO;
	}

	public void seteFmFmEmployeeProjectDetailsPO(List<EFmFmEmployeeProjectDetailsPO> eFmFmEmployeeProjectDetailsPO) {
		this.eFmFmEmployeeProjectDetailsPO = eFmFmEmployeeProjectDetailsPO;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}



	
	
}
