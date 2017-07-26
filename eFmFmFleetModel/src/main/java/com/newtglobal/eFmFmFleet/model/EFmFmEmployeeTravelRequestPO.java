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
@Table(name="eFmFmEmployeeTravelRequest")
public class EFmFmEmployeeTravelRequestPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="RequestId", length=10)
	private int requestId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RequestDate", length=30)
	private Date requestDate;
	
	
	
	@Column(name="RequestStatus", length=10)
	private String requestStatus;
	
	@Column(name="RequestType", length=50)
	private String requestType;

	
	@Column(name="TripType", length=10)
	private String tripType;

	@Column(name="DropSequence", length=50)
	private int dropSequence;
	
	@Column(name="ShiftTime", length=30)
	private Time shiftTime;

	
	@Column(name="Approve_Status", length=10)
	private String approveStatus;
	
	@Column(name="RequestColor", length=50)
	private String requestColor;
	
	@Column(name = "RoutingAreaCreation", length = 10)
	private String routingAreaCreation;
		
	@Transient
	private int branchId;
	
	@Transient
	private int userId;
	
	
	@Transient
	private int nodalPointId;
	
		@Column(name="BranchName", length=150)
    private String branchName;

	
	@Transient
	private String userRole;

	@Column(name="ReadFlg", length=10)
	private String readFlg;
	
	@Column(name="IsActive", length=10)
	private String isActive;
	
	@Column(name="PickUpTime")
	private Time pickUpTime;
	
	@Column(name="CompletionStatus", length=10)
	private String completionStatus;
	
	
	@Column(name = "RequestRemarks", length = 10)
	private String requestRemarks;
	
	/*
	 * Single-S(origin,one destination  from 
	 * drop Down,M-MultipleDestination,N-Normal Request
	 */
	@Column(name="LocationFlg", length=250)
    private String locationFlg;
	
	@Column(name="LocationWaypointsIds", length=250)
    private String locationWaypointsIds;  
	
	//Service Imp Points
	@Column(name = "ReportingManagerUserId", length = 100)
	private String reportingManagerUserId;
	
	@Column(name="ReqApprovalStatus", length=500)
    private String reqApprovalStatus;
	
	@Column(name="ProjectId", length=500)
    private int projectId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="StatusUpdatedTime", length=30)
	private Date statusUpdatedTime;	
	
	@Column(name="ModifiedPickUpTime")
	private Time modifiedPickUpTime;	
	
	@Column(name="TripSheetStatus", length=500)
    private String tripSheetStatus;
	
	@Column(name="TripStatusUpdatedFrom", length=500)
    private String tripStatusUpdatedFrom;
	
	@Transient
	private String pickTime;
	
	
	@Transient
	private Integer employeeCount;
	
	
	@Transient
	private Integer innovaCount;
	
	@Transient
	private Integer tempoCount;
	
	@Transient
	private String typeExecution;
	
	@Transient
	private String filePath;
	
	@Transient
    private int shiftId;

	@Transient
	private String executionDate;
	
	@Transient
	private String cutOffTimeFlg;

	
	@Transient
	private String time;
	
	@Transient
	private int areaGeoFenceRegion;
	
	
	@Transient
	private int clusterRouteGeoFenceRegion;
	
	
	@Transient
	private int routeGeoFenceRegion;
	
	@Transient
	private String multipleRequestId;
	
	@Transient
	private String projectName;
	
	@Transient
	private String combinedFacility;
	
	
	
	@Transient
	private String employeeId;
	
	@Transient
	private String weekOffs;
	
	@Transient
	private String updateRegularRequest;
	
	@Transient
	private int zoneId;
	
	@Transient
	private int areaId;
		
	@Transient
	private String resheduleDate;
	
	
	@Transient
    private int baseFacility;
	
	@Transient
	private int startPgNo;
	
	@Transient
	private int endPgNo;

	//bi-directional one-to-one association to EFmFmEmployeeMaster
	@ManyToOne
	@JoinColumn(name="TripId")
	private EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMaster;
	
	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;

	//bi-directional many-to-one association to EFmFmAreaMaster
	@ManyToOne
	@JoinColumn(name="RouteAreaId")
	private EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping;
	
	// bi-directional many-to-one association to EFmFmClientBranchPO
		@ManyToOne
		@JoinColumn(name = "BranchId")
		private EFmFmClientBranchPO eFmFmClientBranchPO;

	
	// bidirectional manytoone association to eFmFmRoutingArea
	@OneToMany(mappedBy = "eFmFmEmployeeTravelRequest")
	private List<EFmFmRoutingAreaPO> eFmFmRoutingArea;

	// bidirectional manytoone association to areaEmpClusterMapping
	@OneToMany(mappedBy = "eFmFmEmployeeTravelRequest")
	private List<AreaEmpClusterMappingPO> areaEmpClusterMapping;
	
	// bidirectional manytoone association to eFmFmAlgoRoutes
    @OneToMany(mappedBy = "eFmFmEmployeeTravelRequest")
    private List<EFmFmAlgoRoutesPO> eFmFmAlgoRoutes;




	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Time getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(Time pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public EFmFmEmployeeTravelRequestPO() {
	}

	public int getRequestId() {
		return requestId;
	}
	
	

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Time getShiftTime() {
		return shiftTime;
	}

	public void setShiftTime(Time shiftTime) {
		this.shiftTime = shiftTime;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	


	public String getPickTime() {
		return pickTime;
	}

	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	
	public String getWeekOffs() {
		return weekOffs;
	}

	public void setWeekOffs(String weekOffs) {
		this.weekOffs = weekOffs;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getReadFlg() {
		return readFlg;
	}

	public void setReadFlg(String readFlg) {
		this.readFlg = readFlg;
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
	
	

	public String getResheduleDate() {
		return resheduleDate;
	}

	public void setResheduleDate(String resheduleDate) {
		this.resheduleDate = resheduleDate;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}
	
	

	public String getUpdateRegularRequest() {
		return updateRegularRequest;
	}

	public void setUpdateRegularRequest(String updateRegularRequest) {
		this.updateRegularRequest = updateRegularRequest;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getUserRole() {
		return userRole;
	}

	
	
	public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public EFmFmEmployeeRequestMasterPO geteFmFmEmployeeRequestMaster() {
		return eFmFmEmployeeRequestMaster;
	}

	public void seteFmFmEmployeeRequestMaster(
			EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMaster) {
		this.eFmFmEmployeeRequestMaster = eFmFmEmployeeRequestMaster;
	}

	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public EFmFmRouteAreaMappingPO geteFmFmRouteAreaMapping() {
		return eFmFmRouteAreaMapping;
	}

	public void seteFmFmRouteAreaMapping(
			EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping) {
		this.eFmFmRouteAreaMapping = eFmFmRouteAreaMapping;
	}

	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	public int getDropSequence() {
		return dropSequence;
	}

	public void setDropSequence(int dropSequence) {
		this.dropSequence = dropSequence;
	}

	public Integer getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Integer employeeCount) {
		this.employeeCount = employeeCount;
	}

	public Integer getInnovaCount() {
		return innovaCount;
	}

	public void setInnovaCount(Integer innovaCount) {
		this.innovaCount = innovaCount;
	}

	public Integer getTempoCount() {
		return tempoCount;
	}

	public void setTempoCount(Integer tempoCount) {
		this.tempoCount = tempoCount;
	}

	public String getTypeExecution() {
		return typeExecution;
	}

	public void setTypeExecution(String typeExecution) {
		this.typeExecution = typeExecution;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}

	public int getNodalPointId() {
		return nodalPointId;
	}

	public void setNodalPointId(int nodalPointId) {
		this.nodalPointId = nodalPointId;
	}

	public String getCutOffTimeFlg() {
		return cutOffTimeFlg;
	}

	public void setCutOffTimeFlg(String cutOffTimeFlg) {
		this.cutOffTimeFlg = cutOffTimeFlg;
	}

	public int getAreaGeoFenceRegion() {
		return areaGeoFenceRegion;
	}

	public void setAreaGeoFenceRegion(int areaGeoFenceRegion) {
		this.areaGeoFenceRegion = areaGeoFenceRegion;
	}

	public int getClusterRouteGeoFenceRegion() {
		return clusterRouteGeoFenceRegion;
	}

	public void setClusterRouteGeoFenceRegion(int clusterRouteGeoFenceRegion) {
		this.clusterRouteGeoFenceRegion = clusterRouteGeoFenceRegion;
	}

	public int getRouteGeoFenceRegion() {
		return routeGeoFenceRegion;
	}

	public void setRouteGeoFenceRegion(int routeGeoFenceRegion) {
		this.routeGeoFenceRegion = routeGeoFenceRegion;
	}

	public String getRoutingAreaCreation() {
		return routingAreaCreation;
	}

	public void setRoutingAreaCreation(String routingAreaCreation) {
		this.routingAreaCreation = routingAreaCreation;
	}

	public List<EFmFmRoutingAreaPO> geteFmFmRoutingArea() {
		return eFmFmRoutingArea;
	}

	public void seteFmFmRoutingArea(List<EFmFmRoutingAreaPO> eFmFmRoutingArea) {
		this.eFmFmRoutingArea = eFmFmRoutingArea;
	}

	public List<AreaEmpClusterMappingPO> getAreaEmpClusterMapping() {
		return areaEmpClusterMapping;
	}

	public void setAreaEmpClusterMapping(List<AreaEmpClusterMappingPO> areaEmpClusterMapping) {
		this.areaEmpClusterMapping = areaEmpClusterMapping;
	}

	public List<EFmFmAlgoRoutesPO> geteFmFmAlgoRoutes() {
		return eFmFmAlgoRoutes;
	}

	public void seteFmFmAlgoRoutes(List<EFmFmAlgoRoutesPO> eFmFmAlgoRoutes) {
		this.eFmFmAlgoRoutes = eFmFmAlgoRoutes;
	}

	public String getRequestRemarks() {
		return requestRemarks;
	}

	public void setRequestRemarks(String requestRemarks) {
		this.requestRemarks = requestRemarks;
	}

	public String getRequestColor() {
		return requestColor;
	}

	public void setRequestColor(String requestColor) {
		this.requestColor = requestColor;
	}

	public String getLocationFlg() {
		return locationFlg;
	}

	public void setLocationFlg(String locationFlg) {
		this.locationFlg = locationFlg;
	}

	public String getLocationWaypointsIds() {
		return locationWaypointsIds;
	}

	public void setLocationWaypointsIds(String locationWaypointsIds) {
		this.locationWaypointsIds = locationWaypointsIds;
	}

	public String getReportingManagerUserId() {
		return reportingManagerUserId;
	}

	public void setReportingManagerUserId(String reportingManagerUserId) {
		this.reportingManagerUserId = reportingManagerUserId;
	}

	public String getReqApprovalStatus() {
		return reqApprovalStatus;
	}

	public void setReqApprovalStatus(String reqApprovalStatus) {
		this.reqApprovalStatus = reqApprovalStatus;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public Date getStatusUpdatedTime() {
		return statusUpdatedTime;
	}

	public void setStatusUpdatedTime(Date statusUpdatedTime) {
		this.statusUpdatedTime = statusUpdatedTime;
	}

	public String getMultipleRequestId() {
		return multipleRequestId;
	}

	public void setMultipleRequestId(String multipleRequestId) {
		this.multipleRequestId = multipleRequestId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	
	
		public int getBaseFacility() {
		return baseFacility;
	}

	public void setBaseFacility(int baseFacility) {
		this.baseFacility = baseFacility;
	}

		public Time getModifiedPickUpTime() {
		return modifiedPickUpTime;
	}

	public void setModifiedPickUpTime(Time modifiedPickUpTime) {
		this.modifiedPickUpTime = modifiedPickUpTime;
	}

	public String getTripSheetStatus() {
		return tripSheetStatus;
	}

	public String getTripStatusUpdatedFrom() {
		return tripStatusUpdatedFrom;
	}

	public void setTripStatusUpdatedFrom(String tripStatusUpdatedFrom) {
		this.tripStatusUpdatedFrom = tripStatusUpdatedFrom;
	}

	public void setTripSheetStatus(String tripSheetStatus) {
		this.tripSheetStatus = tripSheetStatus;
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