package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;

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


/**
 * The persistent class for the eFmFmDriverFeedback database table.
 * 
 */
@Entity
@Table(name="eFmFmDriverFeedback")
public class EFmFmDriverFeedbackPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id", length=15)
	private int id;

	@Column(name="Comments", length=100)
	private String comments;

	@Column(name="DriverRaiting", length=10)
	private double driverRaiting;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripDate", length=30)
	private Date tripDate;

	@Transient
	private String time;
	
	@Transient
	private int branchId;

	@Transient
	private int userId;	
	
	@Column(name="TripType", length=10)
	private String tripType;
	
	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;


	public EFmFmDriverFeedbackPO() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public double getDriverRaiting() {
		return this.driverRaiting;
	}

	public void setDriverRaiting(double driverRaiting) {
		this.driverRaiting = driverRaiting;
	}
	

	public Date getTripDate() {
		return this.tripDate;
	}

	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}

	public String getTripType() {
		return this.tripType;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}


	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

	
}