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
 * The persistent class for the eFmFmEmployeeTripDetails database table.
 * 
 */
@Entity
@Table(name="eFmFmEmployeeTripDetails")
public class EFmFmEmployeeTripDetailPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="EmpTripId", length=10)
	private int empTripId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ActualTime", length=50)
	private Date actualTime;
	
	@Column(name="CabReachedDestinationTime", length=50)
	private long cabRecheddestinationTime;
	
	@Column(name="CabStartFromDestination", length=50)
	private long cabstartFromDestination;
	
	@Column(name="BoardedFlg", length=10)
	private String boardedFlg;
	
	@Column(name="ReachedFlg", length=10)
	private String reachedFlg;
	
	@Column(name="ComingStatus", length=50)
	private String comingStatus;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ReachedMessageDeliveryDate", length=50)
	private Date reachedMessageDeliveryDate;

	
	@Column(name="PickedUpDateAndTime")
    private long pickedUpDateAndTime;
	
	
	@Column(name="EmployeeOnboardStatusTime")
    private long employeeOnboardStatusTime;

	
	@Column(name="EmployeeOnboardStatus", length=10)
	private String employeeOnboardStatus;
	
	
//	@Column(name="EmployeeOffboardStatusTime")
//    private long employeeOffboardStatusTime;
//
//	@Column(name="EmployeeOffboardStatus", length=10)
//	private String employeeOffboardStatus;
	
	
	@Transient
	private String employeeId;
	
	@Transient
	private String time;

	
	@Transient
	private String geoCodes;
	
	@Transient
	private int userId;
	
	@Transient
	private int requestId;
	
	@Transient
	private int assignRouteId;
	
	@Transient
	private int branchId;
	
	@Transient
	private String combinedFacility;
	
	

	
	@Column(name="TenMinuteMessageStatus", length=10)
	private String tenMinuteMessageStatus;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TenMinuteMessageDeliveryDate", length=50)
	private Date tenMinuteMessageDeliveryDate;
	
	
	
	@Column(name="CabDelayMsgStatus", length=10)
	private String cabDelayMsgStatus;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CabDelayMsgDeliveryDate", length=50)
	private Date CabDelayMsgDeliveryDate;
	
	
	@Column(name="TwoMinuteMessageStatus", length=10)
	private String twoMinuteMessageStatus;
	
	@Column(name="EmployeeStatus", length=10)
	private String employeeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TwoMinuteMessageDeliveryDate", length=50)
	private Date twoMinuteMessageDeliveryDate;
	
	
	@Column(name="CurrentETA", length=50)
	private String currenETA;
	
	
	@Column(name="GoogleEta", length=50)
	private int googleEta;
	
	@Column(name="OrderId", length=50)
	private int orderId;
	
		
	//bi-directional many-to-one association to EFmFmAssignRoute
	@ManyToOne
	@JoinColumn(name="requestId")
	private EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest;
	
	//bi-directional many-to-one association to EFmFm_Alert_Type_Master
	@ManyToOne
	@JoinColumn(name="AssignRouteId")
	private EFmFmAssignRoutePO efmFmAssignRoute;


	
	public EFmFmEmployeeTripDetailPO() {
	}

	public int getEmpTripId() {
		return this.empTripId;
	}

	public void setEmpTripId(int empTripId) {
		this.empTripId = empTripId;
	}

	public Date getActualTime() {
		return this.actualTime;
	}

	public void setActualTime(Date actualTime) {
		this.actualTime = actualTime;
	}

	
	public String getEmployeeStatus() {
		return this.employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	
	public long getCabRecheddestinationTime() {
		return cabRecheddestinationTime;
	}

	public void setCabRecheddestinationTime(long cabRecheddestinationTime) {
		this.cabRecheddestinationTime = cabRecheddestinationTime;
	}

	public long getCabstartFromDestination() {
		return cabstartFromDestination;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCabstartFromDestination(long cabstartFromDestination) {
		this.cabstartFromDestination = cabstartFromDestination;
	}

	public String getBoardedFlg() {
		return boardedFlg;
	}

	public void setBoardedFlg(String boardedFlg) {
		this.boardedFlg = boardedFlg;
	}

	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	

	

	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getTenMinuteMessageStatus() {
		return tenMinuteMessageStatus;
	}

	public void setTenMinuteMessageStatus(String tenMinuteMessageStatus) {
		this.tenMinuteMessageStatus = tenMinuteMessageStatus;
	}

	public String getTwoMinuteMessageStatus() {
		return twoMinuteMessageStatus;
	}

	public void setTwoMinuteMessageStatus(String twoMinuteMessageStatus) {
		this.twoMinuteMessageStatus = twoMinuteMessageStatus;
	}

	public String getCurrenETA() {
		return currenETA;
	}

	public void setCurrenETA(String currenETA) {
		this.currenETA = currenETA;
	}

	public EFmFmEmployeeTravelRequestPO geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}

	public void seteFmFmEmployeeTravelRequest(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}

	public EFmFmAssignRoutePO getEfmFmAssignRoute() {
		return efmFmAssignRoute;
	}

	
	
	public Date getTenMinuteMessageDeliveryDate() {
		return tenMinuteMessageDeliveryDate;
	}

	public void setTenMinuteMessageDeliveryDate(Date tenMinuteMessageDeliveryDate) {
		this.tenMinuteMessageDeliveryDate = tenMinuteMessageDeliveryDate;
	}

	public Date getTwoMinuteMessageDeliveryDate() {
		return twoMinuteMessageDeliveryDate;
	}

	public void setTwoMinuteMessageDeliveryDate(Date twoMinuteMessageDeliveryDate) {
		this.twoMinuteMessageDeliveryDate = twoMinuteMessageDeliveryDate;
	}

	public String getReachedFlg() {
		return reachedFlg;
	}

	public void setReachedFlg(String reachedFlg) {
		this.reachedFlg = reachedFlg;
	}

	
	
	public int getGoogleEta() {
		return googleEta;
	}

	public void setGoogleEta(int googleEta) {
		this.googleEta = googleEta;
	}

	public String getCabDelayMsgStatus() {
		return cabDelayMsgStatus;
	}

	public void setCabDelayMsgStatus(String cabDelayMsgStatus) {
		this.cabDelayMsgStatus = cabDelayMsgStatus;
	}

	public Date getCabDelayMsgDeliveryDate() {
		return CabDelayMsgDeliveryDate;
	}

	public void setCabDelayMsgDeliveryDate(Date cabDelayMsgDeliveryDate) {
		CabDelayMsgDeliveryDate = cabDelayMsgDeliveryDate;
	}

	public String getGeoCodes() {
		return geoCodes;
	}

	public void setGeoCodes(String geoCodes) {
		this.geoCodes = geoCodes;
	}

	public void setEfmFmAssignRoute(EFmFmAssignRoutePO efmFmAssignRoute) {
		this.efmFmAssignRoute = efmFmAssignRoute;
	}

	public Date getReachedMessageDeliveryDate() {
		return reachedMessageDeliveryDate;
	}

	public void setReachedMessageDeliveryDate(Date reachedMessageDeliveryDate) {
		this.reachedMessageDeliveryDate = reachedMessageDeliveryDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getComingStatus() {
		return comingStatus;
	}

	public void setComingStatus(String comingStatus) {
		this.comingStatus = comingStatus;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getAssignRouteId() {
		return assignRouteId;
	}

	public void setAssignRouteId(int assignRouteId) {
		this.assignRouteId = assignRouteId;
	}

    public long getPickedUpDateAndTime() {
        return pickedUpDateAndTime;
    }

    public void setPickedUpDateAndTime(long pickedUpDateAndTime) {
        this.pickedUpDateAndTime = pickedUpDateAndTime;
    }

	public long getEmployeeOnboardStatusTime() {
		return employeeOnboardStatusTime;
	}

	public void setEmployeeOnboardStatusTime(long employeeOnboardStatusTime) {
		this.employeeOnboardStatusTime = employeeOnboardStatusTime;
	}

	public String getEmployeeOnboardStatus() {
		return employeeOnboardStatus;
	}

	public void setEmployeeOnboardStatus(String employeeOnboardStatus) {
		this.employeeOnboardStatus = employeeOnboardStatus;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}


	
}