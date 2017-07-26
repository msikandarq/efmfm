package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
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
import javax.persistence.Transient;


/**
* The persistent class for the eFmFmDriverMaster database table.
* 
*/
@Entity
@Table(name="eFmFmDeviceMaster")
public class EFmFmDeviceMasterPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DeviceId")
	private int deviceId;

	@Column(name="ImeiNumber", length=200)
	private String imeiNumber;

	@Column(name="DeviceToken", length=255)
	private String deviceToken;

	@Column(name="DeviceType", length=100)
	private String deviceType;
	
	@Column(name="MobileNumber", length=50)
	private String mobileNumber;
	
	
	@Column(name="DeviceOs", length=50)
	private String deviceOs;

	@Column(name="SimOperator", length=50)
	private String simOperator;
	
	@Column(name="DeviceModel", length=50)
	private String deviceModel;

	@Column(name="Status", length=10)
	private String status;
	
	@Column(name="IsActive", length=10)
	private String isActive;
	
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@OneToMany(mappedBy="eFmFmDeviceMaster")
	private List<EFmFmVehicleCheckInPO> efmFmVehicleCheckIns;

	@Transient
	private String vehicleNum;
	
	@Transient
	private int driverId;
	
	@Transient
	private String speed;
	
	@Transient
	private int userId;	
	
	@Transient
	private int branchId;	
	
	@Transient
	public String branchCode;
	
	@Transient
	private String latitudeLongitude;
		
	@Transient
	private String combinedFacility;
	
	
	@Transient
	private int startPgNo;
	
	@Transient
	private int endPgNo;
	
	
	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDeviceOs() {
		return deviceOs;
	}
	
	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	public String getSimOperator() {
		return simOperator;
	}

	public void setSimOperator(String simOperator) {
		this.simOperator = simOperator;
	}

	
	
	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getIsActive() {
		return isActive;
	}

	
	
	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}

	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public List<EFmFmVehicleCheckInPO> getEfmFmVehicleCheckIns() {
		return efmFmVehicleCheckIns;
	}

	public void setEfmFmVehicleCheckIns(
			List<EFmFmVehicleCheckInPO> efmFmVehicleCheckIns) {
		this.efmFmVehicleCheckIns = efmFmVehicleCheckIns;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
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

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public int getStartPgNo() {
		return startPgNo;
	}

	public void setStartPgNo(int startPgNo) {
		this.startPgNo = startPgNo;
	}

	public int getEndPgNo() {
		return endPgNo;
	}

	public void setEndPgNo(int endPgNo) {
		this.endPgNo = endPgNo;
	}

	
	
}