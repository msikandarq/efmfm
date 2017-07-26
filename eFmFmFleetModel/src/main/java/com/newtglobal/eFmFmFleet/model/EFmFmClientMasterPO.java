package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the eFmFmClientMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmClientMaster")
public class EFmFmClientMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ClientId", length=50)
	private int clientId;

	@Column(name="StateName", length=50)
	private String stateName;

	@Column(name="CityName", length=50)
	private String cityName;
	
	@Column(name="Pincode", length=50)
	private int pinCode;

	@Column(name="Address", length=255)
	private String address;
	
	@Column(name="LatitudeLongitude", length=200)
	private String latitudeAndLongitude;

	
	@Column(name="ClientName", length=50)
	private String clientName;
	
	@Column(name="ClientContactNumber", length=100)
	private String contactNumber;


	@Column(name="CountryCode", length=50)
	private String countryCode;

	@Column(name="CreatedBy", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=30)
	private Date creationTime;

	@Column(name="EmailId", length=50)
	private String emailId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BranchEndDate", length=50)
	private Date branchEndDate;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BranchStartDate", length=50)
	private Date branchStartDate;

	
	@Column(name="Status", length=50)
	private String status;

	
	@Column(name="UpdatedBy", length=50)
	private String updatedBy;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;

	
	//bidirectional manytoone association to eFmFmClientBranchPO
	@OneToMany(mappedBy="eFmFmClientMaster")
	private List<EFmFmClientBranchPO> eFmFmClientBranchPO;
	

	
	public EFmFmClientMasterPO() {
	}


	public int getClientId() {
		return clientId;
	}


	public void setClientId(int clientId) {
		this.clientId = clientId;
	}


	public String getStateName() {
		return stateName;
	}


	public void setStateName(String stateName) {
		this.stateName = stateName;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public int getPinCode() {
		return pinCode;
	}


	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getClientName() {
		return clientName;
	}


	public void setClientName(String clientName) {
		this.clientName = clientName;
	}



	public String getLatitudeAndLongitude() {
		return latitudeAndLongitude;
	}


	public void setLatitudeAndLongitude(String latitudeAndLongitude) {
		this.latitudeAndLongitude = latitudeAndLongitude;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public Date getBranchEndDate() {
		return branchEndDate;
	}


	public void setBranchEndDate(Date branchEndDate) {
		this.branchEndDate = branchEndDate;
	}


	public Date getBranchStartDate() {
		return branchStartDate;
	}


	public void setBranchStartDate(Date branchStartDate) {
		this.branchStartDate = branchStartDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Date getUpdatedTime() {
		return updatedTime;
	}


	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}


	public List<EFmFmClientBranchPO> geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}


	public void seteFmFmClientBranchPO(List<EFmFmClientBranchPO> eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}
	
	

}