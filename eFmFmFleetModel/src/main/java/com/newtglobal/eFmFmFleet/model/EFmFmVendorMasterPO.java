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
 * The persistent class for the eFmFmVendorMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmVendorMaster")
public class EFmFmVendorMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VendorId", length=10)
	private int vendorId;

	@Column(name="StateName", length=50)
	private String stateName;

	@Column(name="CityName", length=50)
	private String cityName;
	
	@Column(name="Pincode", length=10)
	private int pinCode;
	
	@Transient
	private int assignRouteId;
	
	@Column(name="Address", length=200)
	private String address;

	@Column(name="EmailId", length=50)
	private String emailId;
	
	@Column(name="Status", length=10)
	private String status;

	@Column(name="TinNumber", length=50)
	private String tinNumber;

	@Column(name="VendorContactName", length=50)
	private String vendorContactName;
	
	
	@Column(name="VendorContactName1", length=50)
	private String vendorContactName1;
	
	@Column(name="VendorContactName2", length=50)
	private String vendorContactName2;
	
	@Column(name="VendorContactName3", length=50)
	private String vendorContactName3;
	
	@Column(name="VendorContactName4", length=50)
	private String vendorContactName4;
	
	
	@Column(name="VendorContactNumber1", length=50)
	private String vendorContactNumber1;

	@Column(name="VendorContactNumber2", length=50)
	private String vendorContactNumber2;

	@Column(name="VendorContactNumber3", length=50)
	private String vendorContactNumber3;
	
	@Column(name="VendorContactNumber4", length=50)
	private String vendorContactNumber4;	

	@Column(name="VendorOfficeNo", length=20)
	private String vendorOfficeNo;

	@Column(name="VendorMobileNo", length=20)
	private String vendorMobileNo;

	@Column(name="VendorName", length=50)
	private String vendorName;
	
	@Transient
	private int vehicleId;	
	
	@Transient
	private int branchId;	
	

	@Transient
	private int userId;	
	
	@Column(name="EmailIdLvl1", length=100)
	private String emailIdLvl1;
	
	@Column(name="EmailIdLvl2", length=100)
	private String emailIdLvl2;	
	
	@Column(name="PanNumber", length=100)
	private String panNumber;
	
	@Column(name="ServiceTaxNumber", length=100)
	private String serviceTaxNumber;
	
	@Column(name="VendorContractId", length=100)
	private String vendorContractId;
	
	@Transient
	private String combinedFacility;
	
	
	//bi-directional many-to-one association to EFmFmDriverMaster
	@OneToMany(mappedBy="efmFmVendorMaster")
	private List<EFmFmDriverMasterPO> efmFmDriverMasters;

	
	//bi-directional many-to-one association to EFmFmVehicleMaster
	@OneToMany(mappedBy="efmFmVendorMaster")
	private List<EFmFmVehicleMasterPO> efmFmVehicleMasters;
	
	//bi-directional many-to-one association to eFmFmEscortMaster
	@OneToMany(mappedBy="efmFmVendorMaster")
	private List<EFmFmEscortMasterPO> eFmFmEscortMaster;
	
	@OneToMany(mappedBy="efmFmVendorMaster")
	private List<EFmFmSupervisorMasterPO> eFmFmSupervisorMaster;
		
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	public EFmFmVendorMasterPO() {
	}

	public int getVendorId() {
		return this.vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
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

	public String getEmailId() {
		return this.emailId;
	}

	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<EFmFmEscortMasterPO> geteFmFmEscortMaster() {
		return eFmFmEscortMaster;
	}

	public void seteFmFmEscortMaster(List<EFmFmEscortMasterPO> eFmFmEscortMaster) {
		this.eFmFmEscortMaster = eFmFmEscortMaster;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	

	public String getVendorOfficeNo() {
		return vendorOfficeNo;
	}

	public void setVendorOfficeNo(String vendorOfficeNo) {
		this.vendorOfficeNo = vendorOfficeNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTinNumber() {
		return this.tinNumber;
	}

	
	public int getAssignRouteId() {
		return assignRouteId;
	}

	public void setAssignRouteId(int assignRouteId) {
		this.assignRouteId = assignRouteId;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public String getVendorContactName() {
		return this.vendorContactName;
	}

	public void setVendorContactName(String vendorContactName) {
		this.vendorContactName = vendorContactName;
	}

	public String getVendorMobileNo() {
		return this.vendorMobileNo;
	}	
	
	public String getVendorContactName1() {
		return vendorContactName1;
	}

	public void setVendorContactName1(String vendorContactName1) {
		this.vendorContactName1 = vendorContactName1;
	}

	public String getVendorContactName2() {
		return vendorContactName2;
	}

	public void setVendorContactName2(String vendorContactName2) {
		this.vendorContactName2 = vendorContactName2;
	}

	public String getVendorContactName3() {
		return vendorContactName3;
	}

	public void setVendorContactName3(String vendorContactName3) {
		this.vendorContactName3 = vendorContactName3;
	}

	public String getVendorContactNumber1() {
		return vendorContactNumber1;
	}

	public void setVendorContactNumber1(String vendorContactNumber1) {
		this.vendorContactNumber1 = vendorContactNumber1;
	}

	public String getVendorContactNumber2() {
		return vendorContactNumber2;
	}

	public void setVendorContactNumber2(String vendorContactNumber2) {
		this.vendorContactNumber2 = vendorContactNumber2;
	}

	public String getVendorContactNumber3() {
		return vendorContactNumber3;
	}

	public void setVendorContactNumber3(String vendorContactNumber3) {
		this.vendorContactNumber3 = vendorContactNumber3;
	}

	
	
	public String getVendorContactName4() {
		return vendorContactName4;
	}

	public void setVendorContactName4(String vendorContactName4) {
		this.vendorContactName4 = vendorContactName4;
	}

	public String getVendorContactNumber4() {
		return vendorContactNumber4;
	}

	public void setVendorContactNumber4(String vendorContactNumber4) {
		this.vendorContactNumber4 = vendorContactNumber4;
	}

	public void setVendorMobileNo(String vendorMobileNo) {
		this.vendorMobileNo = vendorMobileNo;
	}

	public String getVendorName() {
		return this.vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<EFmFmDriverMasterPO> getEfmFmDriverMasters() {
		return this.efmFmDriverMasters;
	}

	public void setEfmFmDriverMasters(List<EFmFmDriverMasterPO> efmFmDriverMasters) {
		this.efmFmDriverMasters = efmFmDriverMasters;
	}

	
	

	public EFmFmDriverMasterPO addEfmFmDriverMaster(EFmFmDriverMasterPO efmFmDriverMaster) {
		getEfmFmDriverMasters().add(efmFmDriverMaster);
		efmFmDriverMaster.setEfmFmVendorMaster(this);

		return efmFmDriverMaster;
	}

	public EFmFmDriverMasterPO removeEfmFmDriverMaster(EFmFmDriverMasterPO efmFmDriverMaster) {
		getEfmFmDriverMasters().remove(efmFmDriverMaster);
		efmFmDriverMaster.setEfmFmVendorMaster(null);

		return efmFmDriverMaster;
	}


	public List<EFmFmVehicleMasterPO> getEfmFmVehicleMasters() {
		return this.efmFmVehicleMasters;
	}

	public void setEfmFmVehicleMasters(List<EFmFmVehicleMasterPO> efmFmVehicleMasters) {
		this.efmFmVehicleMasters = efmFmVehicleMasters;
	}

	public EFmFmVehicleMasterPO addEfmFmVehicleMaster(EFmFmVehicleMasterPO efmFmVehicleMaster) {
		getEfmFmVehicleMasters().add(efmFmVehicleMaster);
		efmFmVehicleMaster.setEfmFmVendorMaster(this);

		return efmFmVehicleMaster;
	}

	public EFmFmVehicleMasterPO removeEfmFmVehicleMaster(EFmFmVehicleMasterPO efmFmVehicleMaster) {
		getEfmFmVehicleMasters().remove(efmFmVehicleMaster);
		efmFmVehicleMaster.setEfmFmVendorMaster(null);

		return efmFmVehicleMaster;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getEmailIdLvl1() {
		return emailIdLvl1;
	}

	public void setEmailIdLvl1(String emailIdLvl1) {
		this.emailIdLvl1 = emailIdLvl1;
	}

	public String getEmailIdLvl2() {
		return emailIdLvl2;
	}

	public void setEmailIdLvl2(String emailIdLvl2) {
		this.emailIdLvl2 = emailIdLvl2;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getServiceTaxNumber() {
		return serviceTaxNumber;
	}

	public void setServiceTaxNumber(String serviceTaxNumber) {
		this.serviceTaxNumber = serviceTaxNumber;
	}

	public String getVendorContractId() {
		return vendorContractId;
	}

	public void setVendorContractId(String vendorContractId) {
		this.vendorContractId = vendorContractId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<EFmFmSupervisorMasterPO> geteFmFmSupervisorMaster() {
		return eFmFmSupervisorMaster;
	}

	public void seteFmFmSupervisorMaster(List<EFmFmSupervisorMasterPO> eFmFmSupervisorMaster) {
		this.eFmFmSupervisorMaster = eFmFmSupervisorMaster;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	
	
}