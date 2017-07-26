package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the eFmFm_triptime database table.
 * 
 */
@Entity
@Table(name="eFmFmShiftTiming")
public class EFmFmTripTimingMasterPO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ShiftId", length=15)
	private int shiftId;

	@Column(name="Shift_Time", length=30)
	private Time shiftTime;
	
	@Column(name="Shift_Buffer_Time", length=30)
	private int shiftBufferTime;
	
	@Column(name="IsActive", length=10)
	private String isActive;
	
	@Column(name="CutOff_Time", length=30)
	 private Time cutOffTime;
	
	@Column(name="CancelCutoffTime", length=30)
	 private Time cancelCutoffTime;
	 
	@Column(name="MobileVisibleFlg", length=50)
	private String mobileVisibleFlg;
	
	
	@Column(name="ShiftType", length=200)
	private String shiftType;
	
	
	@Column(name="AreaGeoFenceRegion", length=30)
	private int areaGeoFenceRegion;
	
	/*
	 * Gender Preference for all,Male,Female
	 */
	@Column(name="GenderPreference", length=30)
	private String genderPreference;
	
	
	@Column(name="ClusterGeoFenceRegion", length=30)
	private int clusterGeoFenceRegion;
	
	
	@Column(name="RouteGeoFenceRegion", length=30)
	private int routeGeoFenceRegion;
	
	
	
	 @Transient
	 private String cut_Off_Time;
	 
	 @Transient
		private int userId;	

	 @Transient
	 private String cancel_Cut_Off_Time;
	 
	 @Transient
	 private String reschedule_Cut_Off_Time;
	
	@Transient
	private String time;
	
	@Transient
	private String combinedFacility;
	
	@Column(name="TripType", length=50)
	private String tripType;
	
	@Column(name="RescheduleCutOffTime", length=30)
	private Time RescheduleCutOffTime;
	
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	@Column(name="CeilingFlg", length=50)
	private String ceilingFlg;
	
	@Column(name="CeilingNo", length=50)
	private int ceilingNo;
	
	@Column(name="BufferCeilingNo", length = 20 ,nullable = false, columnDefinition = "int default 0")
	private int bufferCeilingNo;
	

	
	public int getShiftId() {
		return shiftId;
	}

	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public Time getShiftTime() {
		return shiftTime;
	}

	public int getShiftBufferTime() {
		return shiftBufferTime;
	}

	public void setShiftBufferTime(int shiftBufferTime) {
		this.shiftBufferTime = shiftBufferTime;
	}

	public void setShiftTime(Time shiftTime) {
		this.shiftTime = shiftTime;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

    public Time getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(Time cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public String getCut_Off_Time() {
        return cut_Off_Time;
    }

    public void setCut_Off_Time(String cut_Off_Time) {
        this.cut_Off_Time = cut_Off_Time;
    }

	public String getMobileVisibleFlg() {
		return mobileVisibleFlg;
	}

	public void setMobileVisibleFlg(String mobileVisibleFlg) {
		this.mobileVisibleFlg = mobileVisibleFlg;
	}

	public Time getCancelCutoffTime() {
		return cancelCutoffTime;
	}

	public void setCancelCutoffTime(Time cancelCutoffTime) {
		this.cancelCutoffTime = cancelCutoffTime;
	}

	public String getCancel_Cut_Off_Time() {
		return cancel_Cut_Off_Time;
	}

	public void setCancel_Cut_Off_Time(String cancel_Cut_Off_Time) {
		this.cancel_Cut_Off_Time = cancel_Cut_Off_Time;
	}

	public String getReschedule_Cut_Off_Time() {
		return reschedule_Cut_Off_Time;
	}

	public void setReschedule_Cut_Off_Time(String reschedule_Cut_Off_Time) {
		this.reschedule_Cut_Off_Time = reschedule_Cut_Off_Time;
	}

	public Time getRescheduleCutOffTime() {
		return RescheduleCutOffTime;
	}

	public void setRescheduleCutOffTime(Time rescheduleCutOffTime) {
		RescheduleCutOffTime = rescheduleCutOffTime;
	}

	public int getAreaGeoFenceRegion() {
		return areaGeoFenceRegion;
	}

	public void setAreaGeoFenceRegion(int areaGeoFenceRegion) {
		this.areaGeoFenceRegion = areaGeoFenceRegion;
	}

	public int getClusterGeoFenceRegion() {
		return clusterGeoFenceRegion;
	}

	public void setClusterGeoFenceRegion(int clusterGeoFenceRegion) {
		this.clusterGeoFenceRegion = clusterGeoFenceRegion;
	}

	public int getRouteGeoFenceRegion() {
		return routeGeoFenceRegion;
	}

	public void setRouteGeoFenceRegion(int routeGeoFenceRegion) {
		this.routeGeoFenceRegion = routeGeoFenceRegion;
	}

	public String getCeilingFlg() {
		return ceilingFlg;
	}

	public void setCeilingFlg(String ceilingFlg) {
		this.ceilingFlg = ceilingFlg;
	}

	public int getCeilingNo() {
		return ceilingNo;
	}

	public void setCeilingNo(int ceilingNo) {
		this.ceilingNo = ceilingNo;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getGenderPreference() {
		return genderPreference;
	}

	public void setGenderPreference(String genderPreference) {
		this.genderPreference = genderPreference;
	}

	public String getShiftType() {
		return shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}
	
		public int getBufferCeilingNo() {
		return bufferCeilingNo;
	}

	public void setBufferCeilingNo(int bufferCeilingNo) {
		this.bufferCeilingNo = bufferCeilingNo;
	}
    
    

}
