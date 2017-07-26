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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The persistent class for the eFmFmClientProjectDetailsPO database table.
 * 
 */
@Entity
@Table(name="eFmFmEmployeeProjectDetailsPO")
public class EFmFmEmployeeProjectDetailsPO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Emp_Project_Id", length=10)
	private int empProjectId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ProjectAllocationStarDate", length=30)
	private Date projectAllocationStarDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ProjectAllocationEndDate", length=30)
	private Date projectAllocationEndDate;
	

	@Column(name = "ReportingManagerUserId", length = 100)
	private String reportingManagerUserId;
	
	@Column(name = "DelegatedUserId", length = 20 ,nullable = false, columnDefinition = "int default 0")
	private int delegatedUserId;
	
	@Column(name = "DelegatedBy", length = 20 ,nullable = false, columnDefinition = "int default 0")
	private int delegatedBy;
	
	@Column(name = "IsDelegatedUser", length = 20,nullable = false, columnDefinition = "int default 0")
	private int isDelegatedUser;
	
	@Column(name="IsActive", length=10)
	private String isActive;	
	
	@Column(name="CreatedBy", length=10)
	private String createdBy;
	
	@Column(name="LastModified", length=10)
	private String lastModified;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreatedDate", length=30)
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ModifiedDate", length=30)
	private Date modifiedDate;	
	
	@Column(name="Remarks", length=250)
	private String remarks;
	
	@Transient
	private String startDate;

	@Transient
	private String endDate;	
	
	@Transient
	private int userId;
	
	@Transient
	private int delegatedCall;
	
	@Transient
	private String combinedFacility;
	
	@Transient
	@JsonProperty("projectAllocatedEmployeeId")
	private List<EFmFmEmployeeProjectDetailsPO> allocatedEmployees;
	
	@Transient
	@JsonProperty("ListOfProject")
	private List<EFmFmClientProjectDetailsPO> projectList;
	
	//bidirectional manytoone association to eFmFmClientProjectDetails
		
	@ManyToOne
	@JoinColumn(name = "ProjectId")
	private EFmFmClientProjectDetailsPO eFmFmClientProjectDetails;

	
	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;


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

	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
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

	
	public EFmFmClientProjectDetailsPO geteFmFmClientProjectDetails() {
		return eFmFmClientProjectDetails;
	}

	public void seteFmFmClientProjectDetails(EFmFmClientProjectDetailsPO eFmFmClientProjectDetails) {
		this.eFmFmClientProjectDetails = eFmFmClientProjectDetails;
	}

	public int getEmpProjectId() {
		return empProjectId;
	}

	public void setEmpProjectId(int empProjectId) {
		this.empProjectId = empProjectId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getReportingManagerUserId() {
		return reportingManagerUserId;
	}

	public void setReportingManagerUserId(String reportingManagerUserId) {
		this.reportingManagerUserId = reportingManagerUserId;
	}

	public List<EFmFmEmployeeProjectDetailsPO> getAllocatedEmployees() {
		return allocatedEmployees;
	}

	
	public void setAllocatedEmployees(List<EFmFmEmployeeProjectDetailsPO> allocatedEmployees) {
		this.allocatedEmployees = allocatedEmployees;
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

	public int getDelegatedUserId() {
		return delegatedUserId;
	}

	public void setDelegatedUserId(int delegatedUserId) {
		this.delegatedUserId = delegatedUserId;
	}

	public int getIsDelegatedUser() {
		return isDelegatedUser;
	}

	public void setIsDelegatedUser(int isDelegatedUser) {
		this.isDelegatedUser = isDelegatedUser;
	}

	public List<EFmFmClientProjectDetailsPO> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<EFmFmClientProjectDetailsPO> projectList) {
		this.projectList = projectList;
	}

	public int getDelegatedBy() {
		return delegatedBy;
	}

	public void setDelegatedBy(int delegatedBy) {
		this.delegatedBy = delegatedBy;
	}

	public int getDelegatedCall() {
		return delegatedCall;
	}

	public void setDelegatedCall(int delegatedCall) {
		this.delegatedCall = delegatedCall;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	

	
}
