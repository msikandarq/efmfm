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

@Entity
@Table(name = "eFmFmVehicleInspection")
public class EFmFmVehicleInspectionPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "InspectionId", length = 10)
	private int inspectionId;

	@Column(name = "DocumentRc")
	private boolean documentRc;

	@Column(name = "DocumentInsurance")
	private boolean documentInsurance;

	@Column(name = "DocumentDriverLicence")
	private boolean documentDriverLicence;

	@Column(name = "DocumentUpdateJmp")
	private boolean documentUpdateJmp;

	@Column(name = "FirstAidKit")
	private boolean firstAidKit;

	@Column(name = "FireExtingusher")
	private boolean fireExtingusher;

	@Column(name = "AllSeatBeltsWorking")
	private boolean allSeatBeltsWorking;

	@Column(name = "Placard")
	private boolean placard;

	@Column(name = "MosquitoBat")
	private boolean mosquitoBat;

	@Column(name = "PanicButton")
	private boolean panicButton;

	@Column(name = "WalkyTalky")
	private boolean walkyTalky;

	@Column(name = "GPS")
	private boolean gps;
	
	@Column(name = "Status")
	private String  status;

	@Column(name = "DriverUniform")
	private boolean driverUniform;

	@Column(name = "Stephney")
	private boolean stephney;

	@Column(name = "Umbrella")
	private boolean umbrella;

	@Column(name = "Torch")
	private boolean torch;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="InspectionDate", length=30)
	private Date inspectionDate;
	
	@Transient
	private String inspectionCreateDate;

	@Column(name = "Toolkit")
	private boolean toolkit;

	@Column(name = "SeatUpholtseryCleanNotTorn")
	private boolean seatUpholtseryCleanNotTorn;

	@Column(name = "VehcileRoofUpholtseryCleanNotTorn")
	private boolean vehcileRoofUpholtseryCleanNotTorn;

	@Column(name = "VehcileFloorUpholtseryCleanNotTorn")
	private boolean vehcileFloorUpholtseryCleanNotTorn;

	@Column(name = "VehcileDashboardClean")
	private boolean vehcileDashboardClean;

	@Column(name = "VehicleGlassCleanStainFree")
	private boolean vehicleGlassCleanStainFree;

	@Column(name = "VehicleInteriorLightBrightWorking")
	private boolean vehicleInteriorLightBrightWorking;

	@Column(name = "BolsterSeperatingLastTwoSeats")
	private boolean bolsterSeperatingLastTwoSeats;

	@Column(name = "ExteriorScratches")
	private boolean exteriorScratches;

	@Column(name = "NoPatchWork")
	private boolean noPatchWork;

	@Column(name = "PedalBrakeWorking")
	private boolean pedalBrakeWorking;	
	
	@Column(name = "HandBrakeWorking")
	private boolean handBrakeWorking;

	@Column(name = "TyresNoDentsTrimWheel")
	private boolean tyresNoDentsTrimWheel;

	@Column(name = "TyresGoodCondition")
	private boolean tyresGoodCondition;

	@Column(name = "AllTyresAndStephneyInflate")
	private boolean allTyresAndStephneyInflate;

	@Column(name = "JackAndTool")
	private boolean jackAndTool;

	@Column(name = "NumberofPunctruresdone")
	private int numberofPunctruresdone;

	@Column(name = "WiperWorking")
	private boolean wiperWorking;

	@Column(name = "AcCoolingIn5mins")
	private boolean acCoolingIn5mins;

	@Column(name = "NoSmellInAC")
	private boolean noSmellInAC;

	@Column(name = "AcVentsClean")
	private boolean acVentsClean;

	@Column(name = "EnginOilChangeIndicatorOn")
	private boolean enginOilChangeIndicatorOn;

	@Column(name = "CoolantIndicatorOn")
	private boolean coolantIndicatorOn;

	@Column(name = "BrakeClutchIndicatorOn")
	private boolean brakeClutchIndicatorOn;

	@Column(name = "HighBeamWorking")
	private boolean highBeamWorking;

	@Column(name = "LowBeamWorking")
	private boolean lowBeamWorking;

	@Column(name = "RightFromtIndicatorWorking")
	private boolean rightFromtIndicatorWorking;

	@Column(name = "LeftFrontIndicatorWorking")
	private boolean leftFrontIndicatorWorking;

	@Column(name = "ParkingLightsWorking")
	private boolean parkingLightsWorking;

	@Column(name = "LedDayTimeRunningLightWorking")
	private boolean ledDayTimeRunningLightWorking;

	@Column(name = "RightRearIndicatorWorking")
	private boolean rightRearIndicatorWorking;

	@Column(name = "LeftRearIndicatorWorking")
	private boolean leftRearIndicatorWorking;

	@Column(name = "BrakeLightsOn")
	private boolean brakeLightsOn;

	@Column(name = "ReverseLightsOn")
	private boolean reverseLightsOn;

	@Column(name = "Diesel", length = 50)
	private String diesel;

	@Column(name = "HornWorking")
	private boolean hornWorking;

	@Column(name = "ReflectionSignBoard")
	private boolean reflectionSignBoard;

	@Column(name = "DriverName")
	private String driverName;
	
	@Column(name = "AudioWorkingOrNot")
	private boolean audioWorkingOrNot;
	
	
	@Column(name = "FogLights")
	private boolean fogLights;
	
	@Column(name = "DriverCheckInDidOrNot")
	private boolean driverCheckInDidOrNot;
	
	@Column(name = "Feedback")
	private boolean feedback;
		
	
	//**************************Remarks***************	
	@Column(name = "DocumentRcRemarks")
	private String documentRcRemarks;

	@Column(name = "DocumentInsuranceRemarks")
	private String documentInsuranceRemarks;

	@Column(name = "DocumentDriverLicenceRemarks")
	private String documentDriverLicenceRemarks;

	@Column(name = "DocumentUpdateJmpRemarks")
	private String documentUpdateJmpRemarks;

	@Column(name = "FirstAidKitRemarks")
	private String firstAidKitRemarks;

	@Column(name = "FireExtingusherRemarks")
	private String fireExtingusherRemarks;

	@Column(name = "AllSeatBeltsWorkingRemarks")
	private String allSeatBeltsWorkingRemarks;

	@Column(name = "PlacardRemarks")
	private String placardRemarks;

	@Column(name = "MosquitoBatRemarks")
	private String mosquitoBatRemarks;

	@Column(name = "PanicButtonRemarks")
	private String panicButtonRemarks;

	@Column(name = "WalkyTalkyRemarks")
	private String walkyTalkyRemarks;

	@Column(name = "GPSRemarks")
	private String gpsRemarks;
	
	@Column(name = "DriverUniformRemarks")
	private String driverUniformRemarks;

	@Column(name = "StephneyRemarks")
	private String stephneyRemarks;

	@Column(name = "UmbrellaRemarks")
	private String umbrellaRemarks;

	@Column(name = "TorchRemarks")
	private String torchRemarks;
	

	@Column(name = "ToolkitRemarks")
	private String toolkitRemarks;

	@Column(name = "SeatUpholtseryCleanNotTornRemarks")
	private String seatUpholtseryCleanNotTornRemarks;

	@Column(name = "VehcileRoofUpholtseryCleanNotTornRemarks")
	private String vehcileRoofUpholtseryCleanNotTornRemarks;

	@Column(name = "VehcileFloorUpholtseryCleanNotTornRemarks")
	private String vehcileFloorUpholtseryCleanNotTornRemarks;

	@Column(name = "VehcileDashboardCleanRemarks")
	private String vehcileDashboardCleanRemarks;

	@Column(name = "VehicleGlassCleanStainFreeRemarks")
	private String vehicleGlassCleanStainFreeRemarks;

	@Column(name = "VehicleInteriorLightBrightWorkingRemarks")
	private String vehicleInteriorLightBrightWorkingRemarks;

	@Column(name = "BolsterSeperatingLastTwoSeatsRemarks")
	private String bolsterSeperatingLastTwoSeatsRemarks;

	@Column(name = "ExteriorScratchesRemarks")
	private String exteriorScratchesRemarks;

	@Column(name = "NoPatchWorkRemarks")
	private String noPatchWorkRemarks;

	@Column(name = "PedalBrakeWorkingRemarks")
	private String pedalBrakeWorkingRemarks;

	
	@Column(name = "HandBrakeWorkingRemarks")
	private String handBrakeWorkingRemarks;

	@Column(name = "TyresNoDentsTrimWheelRemarks")
	private String tyresNoDentsTrimWheelRemarks;

	@Column(name = "TyresGoodConditionRemarks")
	private String tyresGoodConditionRemarks;

	@Column(name = "AllTyresAndStephneyInflateRemarks")
	private String allTyresAndStephneyInflateRemarks;

	@Column(name = "JackAndToolRemarks")
	private String jackAndToolRemarks;
	
	@Column(name = "WiperWorkingRemarks")
	private String wiperWorkingRemarks;

	@Column(name = "AcCoolingIn5minsRemarks")
	private String acCoolingIn5minsRemarks;

	@Column(name = "NoSmellInACRemarks")
	private String noSmellInACRemarks;

	@Column(name = "AcVentsCleanRemarks")
	private String acVentsCleanRemarks;

	@Column(name = "EnginOilChangeIndicatorOnRemarks")
	private String enginOilChangeIndicatorOnRemarks;

	@Column(name = "CoolantIndicatorOnRemarks")
	private String coolantIndicatorOnRemarks;

	@Column(name = "BrakeClutchIndicatorOnRemarks")
	private String brakeClutchIndicatorOnRemarks;

	@Column(name = "HighBeamWorkingRemarks")
	private String highBeamWorkingRemarks;

	@Column(name = "LowBeamWorkingRemarks")
	private String lowBeamWorkingRemarks;

	@Column(name = "RightFromtIndicatorWorkingRemarks")
	private String rightFromtIndicatorWorkingRemarks;

	@Column(name = "LeftFrontIndicatorWorkingRemarks")
	private String leftFrontIndicatorWorkingRemarks;

	@Column(name = "ParkingLightsWorkingRemarks")
	private String parkingLightsWorkingRemarks;

	@Column(name = "LedDayTimeRunningLightWorkingRemarks")
	private String ledDayTimeRunningLightWorkingRemarks;

	@Column(name = "RightRearIndicatorWorkingRemarks")
	private String rightRearIndicatorWorkingRemarks;

	@Column(name = "LeftRearIndicatorWorkingRemarks")
	private String leftRearIndicatorWorkingRemarks;

	@Column(name = "BrakeLightsOnRemarks")
	private String brakeLightsOnRemarks;

	@Column(name = "ReverseLightsOnRemarks")
	private String reverseLightsOnRemarks;

	@Column(name = "HornWorkingRemarks")
	private String hornWorkingRemarks;

	@Column(name = "ReflectionSignBoardRemarks")
	private String reflectionSignBoardRemarks;
	
	@Column(name = "VehiclePhotoPathFromFront")
	private String vehiclePhotoPathFromFront;
	
	@Column(name = "VehiclePhotoPathFromBack")
	private String vehiclePhotoPathFromBack;
	
	@Column(name = "VehiclePhotoPathFromLeft")
	private String vehiclePhotoPathFromLeft;
	
	@Column(name = "VehiclePhotoPathFromRight")
	private String vehiclePhotoPathFromRight;
	
	
	@Column(name = "AudioWorkingOrNotRemarks")
	private String audioWorkingOrNotRemarks;
	
	
	@Column(name = "FogLightsRemarks")
	private String fogLightsRemarks;
	
	
	@Column(name = "FeedbackRemarks")
	private String feedbackRemarks;
	
	@Column(name = "DriverCheckInDidOrNotRemarks")
	private String driverCheckInDidOrNotRemarks;
	
	
	@Transient
	private int branchId;
	
	@Transient
	private String toDate;
	
	@Transient
	private String fromDate;
	
	@Transient
	private int vendorId;
	
	@Transient
	private int userId;	
	
	@Transient
	private String combinedFacility;	
	
	
	//bi-directional many-to-one association to EFmFmVehicleMaster
	@ManyToOne
	@JoinColumn(name="VehicleId")
	private EFmFmVehicleMasterPO efmFmVehicleMaster;
	
	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;
	


	public boolean isFogLights() {
		return fogLights;
	}

	public void setFogLights(boolean fogLights) {
		this.fogLights = fogLights;
	}

	public boolean isDriverCheckInDidOrNot() {
		return driverCheckInDidOrNot;
	}

	public void setDriverCheckInDidOrNot(boolean driverCheckInDidOrNot) {
		this.driverCheckInDidOrNot = driverCheckInDidOrNot;
	}

	public boolean isFeedback() {
		return feedback;
	}

	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}

	public String getFogLightsRemarks() {
		return fogLightsRemarks;
	}

	public void setFogLightsRemarks(String fogLightsRemarks) {
		this.fogLightsRemarks = fogLightsRemarks;
	}

	public String getFeedbackRemarks() {
		return feedbackRemarks;
	}

	public void setFeedbackRemarks(String feedbackRemarks) {
		this.feedbackRemarks = feedbackRemarks;
	}

	public String getDriverCheckInDidOrNotRemarks() {
		return driverCheckInDidOrNotRemarks;
	}

	public void setDriverCheckInDidOrNotRemarks(String driverCheckInDidOrNotRemarks) {
		this.driverCheckInDidOrNotRemarks = driverCheckInDidOrNotRemarks;
	}

	public EFmFmVehicleMasterPO getEfmFmVehicleMaster() {
		return efmFmVehicleMaster;
	}

	public void setEfmFmVehicleMaster(EFmFmVehicleMasterPO efmFmVehicleMaster) {
		this.efmFmVehicleMaster = efmFmVehicleMaster;
	}

		public int getInspectionId() {
		return inspectionId;
	}

	public void setInspectionId(int inspectionId) {
		this.inspectionId = inspectionId;
	}

	public boolean isDocumentRc() {
		return documentRc;
	}

	public void setDocumentRc(boolean documentRc) {
		this.documentRc = documentRc;
	}

	public boolean isDocumentInsurance() {
		return documentInsurance;
	}

	public void setDocumentInsurance(boolean documentInsurance) {
		this.documentInsurance = documentInsurance;
	}

	public boolean isDocumentDriverLicence() {
		return documentDriverLicence;
	}

	public void setDocumentDriverLicence(boolean documentDriverLicence) {
		this.documentDriverLicence = documentDriverLicence;
	}

	public boolean isDocumentUpdateJmp() {
		return documentUpdateJmp;
	}

	public void setDocumentUpdateJmp(boolean documentUpdateJmp) {
		this.documentUpdateJmp = documentUpdateJmp;
	}

	public boolean isFirstAidKit() {
		return firstAidKit;
	}

	public void setFirstAidKit(boolean firstAidKit) {
		this.firstAidKit = firstAidKit;
	}

	public boolean isFireExtingusher() {
		return fireExtingusher;
	}

	public void setFireExtingusher(boolean fireExtingusher) {
		this.fireExtingusher = fireExtingusher;
	}

	public boolean isAllSeatBeltsWorking() {
		return allSeatBeltsWorking;
	}

	public void setAllSeatBeltsWorking(boolean allSeatBeltsWorking) {
		this.allSeatBeltsWorking = allSeatBeltsWorking;
	}

	public boolean isPlacard() {
		return placard;
	}

	public void setPlacard(boolean placard) {
		this.placard = placard;
	}

	public boolean isMosquitoBat() {
		return mosquitoBat;
	}

	public void setMosquitoBat(boolean mosquitoBat) {
		this.mosquitoBat = mosquitoBat;
	}

	public boolean isPanicButton() {
		return panicButton;
	}

	public void setPanicButton(boolean panicButton) {
		this.panicButton = panicButton;
	}

	public boolean isWalkyTalky() {
		return walkyTalky;
	}

	public void setWalkyTalky(boolean walkyTalky) {
		this.walkyTalky = walkyTalky;
	}

	

	public boolean isGps() {
		return gps;
	}

	public void setGps(boolean gps) {
		this.gps = gps;
	}

	public boolean isDriverUniform() {
		return driverUniform;
	}

	public void setDriverUniform(boolean driverUniform) {
		this.driverUniform = driverUniform;
	}

	public boolean isStephney() {
		return stephney;
	}

	public void setStephney(boolean stephney) {
		this.stephney = stephney;
	}

	public boolean isUmbrella() {
		return umbrella;
	}

	public void setUmbrella(boolean umbrella) {
		this.umbrella = umbrella;
	}

	public boolean isTorch() {
		return torch;
	}

	public void setTorch(boolean torch) {
		this.torch = torch;
	}

	public boolean isToolkit() {
		return toolkit;
	}

	public void setToolkit(boolean toolkit) {
		this.toolkit = toolkit;
	}

	public boolean isSeatUpholtseryCleanNotTorn() {
		return seatUpholtseryCleanNotTorn;
	}

	public void setSeatUpholtseryCleanNotTorn(boolean seatUpholtseryCleanNotTorn) {
		this.seatUpholtseryCleanNotTorn = seatUpholtseryCleanNotTorn;
	}

	public boolean isVehcileRoofUpholtseryCleanNotTorn() {
		return vehcileRoofUpholtseryCleanNotTorn;
	}
	
	

	public String getInspectionCreateDate() {
		return inspectionCreateDate;
	}

	public void setInspectionCreateDate(String inspectionCreateDate) {
		this.inspectionCreateDate = inspectionCreateDate;
	}

	public void setVehcileRoofUpholtseryCleanNotTorn(
			boolean vehcileRoofUpholtseryCleanNotTorn) {
		this.vehcileRoofUpholtseryCleanNotTorn = vehcileRoofUpholtseryCleanNotTorn;
	}

	public boolean isVehcileFloorUpholtseryCleanNotTorn() {
		return vehcileFloorUpholtseryCleanNotTorn;
	}

	public void setVehcileFloorUpholtseryCleanNotTorn(
			boolean vehcileFloorUpholtseryCleanNotTorn) {
		this.vehcileFloorUpholtseryCleanNotTorn = vehcileFloorUpholtseryCleanNotTorn;
	}

	public boolean isVehcileDashboardClean() {
		return vehcileDashboardClean;
	}

	public void setVehcileDashboardClean(boolean vehcileDashboardClean) {
		this.vehcileDashboardClean = vehcileDashboardClean;
	}

	public boolean isVehicleGlassCleanStainFree() {
		return vehicleGlassCleanStainFree;
	}

	public void setVehicleGlassCleanStainFree(boolean vehicleGlassCleanStainFree) {
		this.vehicleGlassCleanStainFree = vehicleGlassCleanStainFree;
	}

	public boolean isVehicleInteriorLightBrightWorking() {
		return vehicleInteriorLightBrightWorking;
	}

	public void setVehicleInteriorLightBrightWorking(
			boolean vehicleInteriorLightBrightWorking) {
		this.vehicleInteriorLightBrightWorking = vehicleInteriorLightBrightWorking;
	}

	public boolean isBolsterSeperatingLastTwoSeats() {
		return bolsterSeperatingLastTwoSeats;
	}

	public void setBolsterSeperatingLastTwoSeats(
			boolean bolsterSeperatingLastTwoSeats) {
		this.bolsterSeperatingLastTwoSeats = bolsterSeperatingLastTwoSeats;
	}

	public boolean isExteriorScratches() {
		return exteriorScratches;
	}

	public void setExteriorScratches(boolean exteriorScratches) {
		this.exteriorScratches = exteriorScratches;
	}

	public boolean isNoPatchWork() {
		return noPatchWork;
	}

	public void setNoPatchWork(boolean noPatchWork) {
		this.noPatchWork = noPatchWork;
	}
	

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public boolean isHandBrakeWorking() {
		return handBrakeWorking;
	}

	public void setHandBrakeWorking(boolean handBrakeWorking) {
		this.handBrakeWorking = handBrakeWorking;
	}

	public boolean isTyresNoDentsTrimWheel() {
		return tyresNoDentsTrimWheel;
	}

	public void setTyresNoDentsTrimWheel(boolean tyresNoDentsTrimWheel) {
		this.tyresNoDentsTrimWheel = tyresNoDentsTrimWheel;
	}

	public boolean isTyresGoodCondition() {
		return tyresGoodCondition;
	}

	public void setTyresGoodCondition(boolean tyresGoodCondition) {
		this.tyresGoodCondition = tyresGoodCondition;
	}

	public boolean isAllTyresAndStephneyInflate() {
		return allTyresAndStephneyInflate;
	}

	public void setAllTyresAndStephneyInflate(boolean allTyresAndStephneyInflate) {
		this.allTyresAndStephneyInflate = allTyresAndStephneyInflate;
	}

	
	public int getNumberofPunctruresdone() {
		return numberofPunctruresdone;
	}

	public void setNumberofPunctruresdone(int numberofPunctruresdone) {
		this.numberofPunctruresdone = numberofPunctruresdone;
	}

	public boolean isWiperWorking() {
		return wiperWorking;
	}

	public void setWiperWorking(boolean wiperWorking) {
		this.wiperWorking = wiperWorking;
	}

	public boolean isAcCoolingIn5mins() {
		return acCoolingIn5mins;
	}

	public void setAcCoolingIn5mins(boolean acCoolingIn5mins) {
		this.acCoolingIn5mins = acCoolingIn5mins;
	}

	

	public boolean isAcVentsClean() {
		return acVentsClean;
	}

	public void setAcVentsClean(boolean acVentsClean) {
		this.acVentsClean = acVentsClean;
	}

	public boolean isEnginOilChangeIndicatorOn() {
		return enginOilChangeIndicatorOn;
	}

	public void setEnginOilChangeIndicatorOn(boolean enginOilChangeIndicatorOn) {
		this.enginOilChangeIndicatorOn = enginOilChangeIndicatorOn;
	}

	public boolean isCoolantIndicatorOn() {
		return coolantIndicatorOn;
	}

	public void setCoolantIndicatorOn(boolean coolantIndicatorOn) {
		this.coolantIndicatorOn = coolantIndicatorOn;
	}

	public boolean isBrakeClutchIndicatorOn() {
		return brakeClutchIndicatorOn;
	}

	public void setBrakeClutchIndicatorOn(boolean brakeClutchIndicatorOn) {
		this.brakeClutchIndicatorOn = brakeClutchIndicatorOn;
	}

	public boolean isHighBeamWorking() {
		return highBeamWorking;
	}

	public void setHighBeamWorking(boolean highBeamWorking) {
		this.highBeamWorking = highBeamWorking;
	}

	public boolean isLowBeamWorking() {
		return lowBeamWorking;
	}

	public void setLowBeamWorking(boolean lowBeamWorking) {
		this.lowBeamWorking = lowBeamWorking;
	}

	public boolean isRightFromtIndicatorWorking() {
		return rightFromtIndicatorWorking;
	}

	public void setRightFromtIndicatorWorking(boolean rightFromtIndicatorWorking) {
		this.rightFromtIndicatorWorking = rightFromtIndicatorWorking;
	}

	public boolean isLeftFrontIndicatorWorking() {
		return leftFrontIndicatorWorking;
	}

	public void setLeftFrontIndicatorWorking(boolean leftFrontIndicatorWorking) {
		this.leftFrontIndicatorWorking = leftFrontIndicatorWorking;
	}

	public boolean isParkingLightsWorking() {
		return parkingLightsWorking;
	}

	public void setParkingLightsWorking(boolean parkingLightsWorking) {
		this.parkingLightsWorking = parkingLightsWorking;
	}

	public boolean isLedDayTimeRunningLightWorking() {
		return ledDayTimeRunningLightWorking;
	}

	public void setLedDayTimeRunningLightWorking(
			boolean ledDayTimeRunningLightWorking) {
		this.ledDayTimeRunningLightWorking = ledDayTimeRunningLightWorking;
	}

	public boolean isRightRearIndicatorWorking() {
		return rightRearIndicatorWorking;
	}

	public void setRightRearIndicatorWorking(boolean rightRearIndicatorWorking) {
		this.rightRearIndicatorWorking = rightRearIndicatorWorking;
	}

	public boolean isLeftRearIndicatorWorking() {
		return leftRearIndicatorWorking;
	}

	public void setLeftRearIndicatorWorking(boolean leftRearIndicatorWorking) {
		this.leftRearIndicatorWorking = leftRearIndicatorWorking;
	}

	public boolean isBrakeLightsOn() {
		return brakeLightsOn;
	}

	public void setBrakeLightsOn(boolean brakeLightsOn) {
		this.brakeLightsOn = brakeLightsOn;
	}

	public void setDiesel(String diesel) {
		this.diesel = diesel;
	}

	public boolean isHornWorking() {
		return hornWorking;
	}

	public void setHornWorking(boolean hornWorking) {
		this.hornWorking = hornWorking;
	}

	public boolean isReflectionSignBoard() {
		return reflectionSignBoard;
	}

	public void setReflectionSignBoard(boolean reflectionSignBoard) {
		this.reflectionSignBoard = reflectionSignBoard;
	}

	public String getDriverName() {
		return driverName;
	}
	
	public boolean isPedalBrakeWorking() {
		return pedalBrakeWorking;
	}

	public void setPedalBrakeWorking(boolean pedalBrakeWorking) {
		this.pedalBrakeWorking = pedalBrakeWorking;
	}

	public boolean isJackAndTool() {
		return jackAndTool;
	}

	public void setJackAndTool(boolean jackAndTool) {
		this.jackAndTool = jackAndTool;
	}

	public boolean isNoSmellInAC() {
		return noSmellInAC;
	}

	public void setNoSmellInAC(boolean noSmellInAC) {
		this.noSmellInAC = noSmellInAC;
	}

	public boolean isReverseLightsOn() {
		return reverseLightsOn;
	}

	public void setReverseLightsOn(boolean reverseLightsOn) {
		this.reverseLightsOn = reverseLightsOn;
	}

	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public String getDiesel() {
		return diesel;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getDocumentRcRemarks() {
		return documentRcRemarks;
	}

	public void setDocumentRcRemarks(String documentRcRemarks) {
		this.documentRcRemarks = documentRcRemarks;
	}

	public String getDocumentInsuranceRemarks() {
		return documentInsuranceRemarks;
	}

	public void setDocumentInsuranceRemarks(String documentInsuranceRemarks) {
		this.documentInsuranceRemarks = documentInsuranceRemarks;
	}

	public String getDocumentDriverLicenceRemarks() {
		return documentDriverLicenceRemarks;
	}

	public void setDocumentDriverLicenceRemarks(String documentDriverLicenceRemarks) {
		this.documentDriverLicenceRemarks = documentDriverLicenceRemarks;
	}

	public String getDocumentUpdateJmpRemarks() {
		return documentUpdateJmpRemarks;
	}

	public void setDocumentUpdateJmpRemarks(String documentUpdateJmpRemarks) {
		this.documentUpdateJmpRemarks = documentUpdateJmpRemarks;
	}

	public String getFirstAidKitRemarks() {
		return firstAidKitRemarks;
	}

	public void setFirstAidKitRemarks(String firstAidKitRemarks) {
		this.firstAidKitRemarks = firstAidKitRemarks;
	}

	public String getFireExtingusherRemarks() {
		return fireExtingusherRemarks;
	}

	public void setFireExtingusherRemarks(String fireExtingusherRemarks) {
		this.fireExtingusherRemarks = fireExtingusherRemarks;
	}

	public String getAllSeatBeltsWorkingRemarks() {
		return allSeatBeltsWorkingRemarks;
	}

	public void setAllSeatBeltsWorkingRemarks(String allSeatBeltsWorkingRemarks) {
		this.allSeatBeltsWorkingRemarks = allSeatBeltsWorkingRemarks;
	}

	public String getPlacardRemarks() {
		return placardRemarks;
	}

	public void setPlacardRemarks(String placardRemarks) {
		this.placardRemarks = placardRemarks;
	}

	public String getMosquitoBatRemarks() {
		return mosquitoBatRemarks;
	}

	public void setMosquitoBatRemarks(String mosquitoBatRemarks) {
		this.mosquitoBatRemarks = mosquitoBatRemarks;
	}

	public String getPanicButtonRemarks() {
		return panicButtonRemarks;
	}

	public void setPanicButtonRemarks(String panicButtonRemarks) {
		this.panicButtonRemarks = panicButtonRemarks;
	}

	public String getWalkyTalkyRemarks() {
		return walkyTalkyRemarks;
	}

	public void setWalkyTalkyRemarks(String walkyTalkyRemarks) {
		this.walkyTalkyRemarks = walkyTalkyRemarks;
	}

	public String getGpsRemarks() {
		return gpsRemarks;
	}

	public void setGpsRemarks(String gpsRemarks) {
		this.gpsRemarks = gpsRemarks;
	}

	public String getDriverUniformRemarks() {
		return driverUniformRemarks;
	}

	public void setDriverUniformRemarks(String driverUniformRemarks) {
		this.driverUniformRemarks = driverUniformRemarks;
	}

	public String getStephneyRemarks() {
		return stephneyRemarks;
	}

	public void setStephneyRemarks(String stephneyRemarks) {
		this.stephneyRemarks = stephneyRemarks;
	}

	public String getUmbrellaRemarks() {
		return umbrellaRemarks;
	}

	public void setUmbrellaRemarks(String umbrellaRemarks) {
		this.umbrellaRemarks = umbrellaRemarks;
	}

	public String getTorchRemarks() {
		return torchRemarks;
	}

	public void setTorchRemarks(String torchRemarks) {
		this.torchRemarks = torchRemarks;
	}

	public String getToolkitRemarks() {
		return toolkitRemarks;
	}

	public void setToolkitRemarks(String toolkitRemarks) {
		this.toolkitRemarks = toolkitRemarks;
	}

	public String getSeatUpholtseryCleanNotTornRemarks() {
		return seatUpholtseryCleanNotTornRemarks;
	}

	public void setSeatUpholtseryCleanNotTornRemarks(String seatUpholtseryCleanNotTornRemarks) {
		this.seatUpholtseryCleanNotTornRemarks = seatUpholtseryCleanNotTornRemarks;
	}

	public String getVehcileRoofUpholtseryCleanNotTornRemarks() {
		return vehcileRoofUpholtseryCleanNotTornRemarks;
	}

	public void setVehcileRoofUpholtseryCleanNotTornRemarks(String vehcileRoofUpholtseryCleanNotTornRemarks) {
		this.vehcileRoofUpholtseryCleanNotTornRemarks = vehcileRoofUpholtseryCleanNotTornRemarks;
	}

	public String getVehcileFloorUpholtseryCleanNotTornRemarks() {
		return vehcileFloorUpholtseryCleanNotTornRemarks;
	}

	public void setVehcileFloorUpholtseryCleanNotTornRemarks(String vehcileFloorUpholtseryCleanNotTornRemarks) {
		this.vehcileFloorUpholtseryCleanNotTornRemarks = vehcileFloorUpholtseryCleanNotTornRemarks;
	}

	public String getVehcileDashboardCleanRemarks() {
		return vehcileDashboardCleanRemarks;
	}

	public void setVehcileDashboardCleanRemarks(String vehcileDashboardCleanRemarks) {
		this.vehcileDashboardCleanRemarks = vehcileDashboardCleanRemarks;
	}

	public String getVehicleGlassCleanStainFreeRemarks() {
		return vehicleGlassCleanStainFreeRemarks;
	}

	public void setVehicleGlassCleanStainFreeRemarks(String vehicleGlassCleanStainFreeRemarks) {
		this.vehicleGlassCleanStainFreeRemarks = vehicleGlassCleanStainFreeRemarks;
	}

	public String getVehicleInteriorLightBrightWorkingRemarks() {
		return vehicleInteriorLightBrightWorkingRemarks;
	}

	public void setVehicleInteriorLightBrightWorkingRemarks(String vehicleInteriorLightBrightWorkingRemarks) {
		this.vehicleInteriorLightBrightWorkingRemarks = vehicleInteriorLightBrightWorkingRemarks;
	}

	public String getBolsterSeperatingLastTwoSeatsRemarks() {
		return bolsterSeperatingLastTwoSeatsRemarks;
	}

	public void setBolsterSeperatingLastTwoSeatsRemarks(String bolsterSeperatingLastTwoSeatsRemarks) {
		this.bolsterSeperatingLastTwoSeatsRemarks = bolsterSeperatingLastTwoSeatsRemarks;
	}

	public String getExteriorScratchesRemarks() {
		return exteriorScratchesRemarks;
	}

	public void setExteriorScratchesRemarks(String exteriorScratchesRemarks) {
		this.exteriorScratchesRemarks = exteriorScratchesRemarks;
	}

	public String getNoPatchWorkRemarks() {
		return noPatchWorkRemarks;
	}

	public void setNoPatchWorkRemarks(String noPatchWorkRemarks) {
		this.noPatchWorkRemarks = noPatchWorkRemarks;
	}

	public String getPedalBrakeWorkingRemarks() {
		return pedalBrakeWorkingRemarks;
	}

	public void setPedalBrakeWorkingRemarks(String pedalBrakeWorkingRemarks) {
		this.pedalBrakeWorkingRemarks = pedalBrakeWorkingRemarks;
	}

	public String getHandBrakeWorkingRemarks() {
		return handBrakeWorkingRemarks;
	}

	public void setHandBrakeWorkingRemarks(String handBrakeWorkingRemarks) {
		this.handBrakeWorkingRemarks = handBrakeWorkingRemarks;
	}

	public String getTyresNoDentsTrimWheelRemarks() {
		return tyresNoDentsTrimWheelRemarks;
	}

	public void setTyresNoDentsTrimWheelRemarks(String tyresNoDentsTrimWheelRemarks) {
		this.tyresNoDentsTrimWheelRemarks = tyresNoDentsTrimWheelRemarks;
	}

	public String getTyresGoodConditionRemarks() {
		return tyresGoodConditionRemarks;
	}

	public void setTyresGoodConditionRemarks(String tyresGoodConditionRemarks) {
		this.tyresGoodConditionRemarks = tyresGoodConditionRemarks;
	}

	public String getAllTyresAndStephneyInflateRemarks() {
		return allTyresAndStephneyInflateRemarks;
	}

	public void setAllTyresAndStephneyInflateRemarks(String allTyresAndStephneyInflateRemarks) {
		this.allTyresAndStephneyInflateRemarks = allTyresAndStephneyInflateRemarks;
	}

	public String getJackAndToolRemarks() {
		return jackAndToolRemarks;
	}

	public void setJackAndToolRemarks(String jackAndToolRemarks) {
		this.jackAndToolRemarks = jackAndToolRemarks;
	}

	public String getWiperWorkingRemarks() {
		return wiperWorkingRemarks;
	}

	public void setWiperWorkingRemarks(String wiperWorkingRemarks) {
		this.wiperWorkingRemarks = wiperWorkingRemarks;
	}

	public String getAcCoolingIn5minsRemarks() {
		return acCoolingIn5minsRemarks;
	}

	public void setAcCoolingIn5minsRemarks(String acCoolingIn5minsRemarks) {
		this.acCoolingIn5minsRemarks = acCoolingIn5minsRemarks;
	}

	public String getNoSmellInACRemarks() {
		return noSmellInACRemarks;
	}

	public void setNoSmellInACRemarks(String noSmellInACRemarks) {
		this.noSmellInACRemarks = noSmellInACRemarks;
	}

	public String getAcVentsCleanRemarks() {
		return acVentsCleanRemarks;
	}

	public void setAcVentsCleanRemarks(String acVentsCleanRemarks) {
		this.acVentsCleanRemarks = acVentsCleanRemarks;
	}

	public String getEnginOilChangeIndicatorOnRemarks() {
		return enginOilChangeIndicatorOnRemarks;
	}

	public void setEnginOilChangeIndicatorOnRemarks(String enginOilChangeIndicatorOnRemarks) {
		this.enginOilChangeIndicatorOnRemarks = enginOilChangeIndicatorOnRemarks;
	}

	public String getCoolantIndicatorOnRemarks() {
		return coolantIndicatorOnRemarks;
	}

	public void setCoolantIndicatorOnRemarks(String coolantIndicatorOnRemarks) {
		this.coolantIndicatorOnRemarks = coolantIndicatorOnRemarks;
	}

	public String getBrakeClutchIndicatorOnRemarks() {
		return brakeClutchIndicatorOnRemarks;
	}

	public void setBrakeClutchIndicatorOnRemarks(String brakeClutchIndicatorOnRemarks) {
		this.brakeClutchIndicatorOnRemarks = brakeClutchIndicatorOnRemarks;
	}

	public String getHighBeamWorkingRemarks() {
		return highBeamWorkingRemarks;
	}

	public void setHighBeamWorkingRemarks(String highBeamWorkingRemarks) {
		this.highBeamWorkingRemarks = highBeamWorkingRemarks;
	}

	public String getLowBeamWorkingRemarks() {
		return lowBeamWorkingRemarks;
	}

	public void setLowBeamWorkingRemarks(String lowBeamWorkingRemarks) {
		this.lowBeamWorkingRemarks = lowBeamWorkingRemarks;
	}

	public String getRightFromtIndicatorWorkingRemarks() {
		return rightFromtIndicatorWorkingRemarks;
	}

	public void setRightFromtIndicatorWorkingRemarks(String rightFromtIndicatorWorkingRemarks) {
		this.rightFromtIndicatorWorkingRemarks = rightFromtIndicatorWorkingRemarks;
	}

	public String getLeftFrontIndicatorWorkingRemarks() {
		return leftFrontIndicatorWorkingRemarks;
	}

	public void setLeftFrontIndicatorWorkingRemarks(String leftFrontIndicatorWorkingRemarks) {
		this.leftFrontIndicatorWorkingRemarks = leftFrontIndicatorWorkingRemarks;
	}

	public String getParkingLightsWorkingRemarks() {
		return parkingLightsWorkingRemarks;
	}

	public void setParkingLightsWorkingRemarks(String parkingLightsWorkingRemarks) {
		this.parkingLightsWorkingRemarks = parkingLightsWorkingRemarks;
	}

	public String getLedDayTimeRunningLightWorkingRemarks() {
		return ledDayTimeRunningLightWorkingRemarks;
	}

	public void setLedDayTimeRunningLightWorkingRemarks(String ledDayTimeRunningLightWorkingRemarks) {
		this.ledDayTimeRunningLightWorkingRemarks = ledDayTimeRunningLightWorkingRemarks;
	}

	public String getRightRearIndicatorWorkingRemarks() {
		return rightRearIndicatorWorkingRemarks;
	}

	public void setRightRearIndicatorWorkingRemarks(String rightRearIndicatorWorkingRemarks) {
		this.rightRearIndicatorWorkingRemarks = rightRearIndicatorWorkingRemarks;
	}

	public String getLeftRearIndicatorWorkingRemarks() {
		return leftRearIndicatorWorkingRemarks;
	}

	public void setLeftRearIndicatorWorkingRemarks(String leftRearIndicatorWorkingRemarks) {
		this.leftRearIndicatorWorkingRemarks = leftRearIndicatorWorkingRemarks;
	}

	public String getBrakeLightsOnRemarks() {
		return brakeLightsOnRemarks;
	}

	public void setBrakeLightsOnRemarks(String brakeLightsOnRemarks) {
		this.brakeLightsOnRemarks = brakeLightsOnRemarks;
	}

	public String getReverseLightsOnRemarks() {
		return reverseLightsOnRemarks;
	}

	public void setReverseLightsOnRemarks(String reverseLightsOnRemarks) {
		this.reverseLightsOnRemarks = reverseLightsOnRemarks;
	}

	public String getHornWorkingRemarks() {
		return hornWorkingRemarks;
	}

	public void setHornWorkingRemarks(String hornWorkingRemarks) {
		this.hornWorkingRemarks = hornWorkingRemarks;
	}

	public String getReflectionSignBoardRemarks() {
		return reflectionSignBoardRemarks;
	}

	public void setReflectionSignBoardRemarks(String reflectionSignBoardRemarks) {
		this.reflectionSignBoardRemarks = reflectionSignBoardRemarks;
	}

	public String getVehiclePhotoPathFromFront() {
		return vehiclePhotoPathFromFront;
	}

	public void setVehiclePhotoPathFromFront(String vehiclePhotoPathFromFront) {
		this.vehiclePhotoPathFromFront = vehiclePhotoPathFromFront;
	}

	public String getVehiclePhotoPathFromBack() {
		return vehiclePhotoPathFromBack;
	}

	public void setVehiclePhotoPathFromBack(String vehiclePhotoPathFromBack) {
		this.vehiclePhotoPathFromBack = vehiclePhotoPathFromBack;
	}

	public String getVehiclePhotoPathFromLeft() {
		return vehiclePhotoPathFromLeft;
	}

	public void setVehiclePhotoPathFromLeft(String vehiclePhotoPathFromLeft) {
		this.vehiclePhotoPathFromLeft = vehiclePhotoPathFromLeft;
	}

	public String getVehiclePhotoPathFromRight() {
		return vehiclePhotoPathFromRight;
	}

	public void setVehiclePhotoPathFromRight(String vehiclePhotoPathFromRight) {
		this.vehiclePhotoPathFromRight = vehiclePhotoPathFromRight;
	}

	

	public boolean isAudioWorkingOrNot() {
		return audioWorkingOrNot;
	}

	public void setAudioWorkingOrNot(boolean audioWorkingOrNot) {
		this.audioWorkingOrNot = audioWorkingOrNot;
	}

	public String getAudioWorkingOrNotRemarks() {
		return audioWorkingOrNotRemarks;
	}

	public void setAudioWorkingOrNotRemarks(String audioWorkingOrNotRemarks) {
		this.audioWorkingOrNotRemarks = audioWorkingOrNotRemarks;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}
	
	
	
}
