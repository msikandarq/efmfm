package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.sql.Time;
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
 * The persistent class for the eFmFmEmployeeTravelRequest database table.
 * 
 */
@Entity
@Table(name="eFmFmMultipleLocationTvlReqPO")
public class EFmFmMultipleLocationTvlReqPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MultipleReqId", length=10)
	private int multipleReqId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RequestUpdateDate", length=30)
	private Date requestUpdateDate;
	
	@Column(name="LocationStatus", length=10)
	private String locationStatus;	
	
	@Column(name="TravelledStatus", length=10)
	private String travelledStatus;
	

	//bi-directional one-to-one association to EFmFmEmployeeMaster
	@ManyToOne
	@JoinColumn(name="RequestId")
	private EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest;
	
	@ManyToOne
	@JoinColumn(name="LocationId")
	private EFmFmLocationMasterPO eFmFmLocationMaster;
	
	@Transient
	private  int userId;
	
	@Transient
	private  String actionFlg;

	public int getMultipleReqId() {
		return multipleReqId;
	}

	public void setMultipleReqId(int multipleReqId) {
		this.multipleReqId = multipleReqId;
	}

	public Date getRequestUpdateDate() {
		return requestUpdateDate;
	}

	public void setRequestUpdateDate(Date requestUpdateDate) {
		this.requestUpdateDate = requestUpdateDate;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	public String getTravelledStatus() {
		return travelledStatus;
	}

	public void setTravelledStatus(String travelledStatus) {
		this.travelledStatus = travelledStatus;
	}
	
	public EFmFmLocationMasterPO geteFmFmLocationMaster() {
		return eFmFmLocationMaster;
	}

	public void seteFmFmLocationMaster(EFmFmLocationMasterPO eFmFmLocationMaster) {
		this.eFmFmLocationMaster = eFmFmLocationMaster;
	}

	public EFmFmEmployeeTravelRequestPO geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}

	public void seteFmFmEmployeeTravelRequest(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getActionFlg() {
		return actionFlg;
	}

	public void setActionFlg(String actionFlg) {
		this.actionFlg = actionFlg;
	}


	
	
}