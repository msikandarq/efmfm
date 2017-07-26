package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSmsAlertMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/dashboard")
@Consumes("application/json")
@Produces("application/json")
public class DashBoardDetailService {
	
	private static Log log = LogFactory.getLog(DashBoardDetailService.class);	

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	@POST
	@Path("/detail")
	public Response getAllDashBoardData(EFmFmAssignRoutePO assignRoutePO){		
//		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object>  requests = new HashMap<String, Object>();	
		
		log.info("assignRoutePO"+assignRoutePO.getCombinedFacility());
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//			requests.put("status", "invalidRequest");
//			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
//		}
//		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
//		if (!(userDetail.isEmpty())) {
//			String jwtToken = "";
//			try {
//				JwtTokenGenerator token = new JwtTokenGenerator();
//				jwtToken = token.generateToken();
//				userDetail.get(0).setAuthorizationToken(jwtToken);
//				userDetail.get(0).setTokenGenerationTime(new Date());
//				userMasterBO.update(userDetail.get(0));
//			} catch (Exception e) {
//				log.info("error" + e);
//			}
//		}
//	
//		}catch(Exception e){
//			log.info("authentication error"+e);
//			requests.put("status", "invalidRequest");
//			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
//		}
		
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");			
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		assignRoutePO.setTripAssignDate(new Date());
		
		int polutionExpired=0,insuranceExpired=0,licenseExpire=0,medicalFitnessCertificateValid=0,policeVarificationValid = 0,ddTrainingValid = 0,taxCertificateValid=0,permitValid=0,vehicleMaintenance=0;
		String polutionExpiredFlg="N",insuranceExpiredFlg="N",licenseExpireFlg="N",
				medicalFitnessCertificateValidFlg="N",policeVarificationValidFlg="N",ddTrainingValidFlg="N",
				taxCertificateValidFlg="N",permitValidFlg="N",vehicleMaintenanceFlg="N";
		
		Boolean polutionExpiredMsgFlg=false,insuranceExpiredMsgFlg=false,licenseExpireMsgFlg=false,
				medicalFitnessCertificateValidMsgFlg=false,policeVarificationValidMsgFlg=false,ddTrainingValidMsgFlg=false,
				taxCertificateValidMsgFlg=false,permitValidMsgFlg=false,vehicleMaintenanceMsgFlg=false;
		
		int speedAlert=0,breakDownalert=0,accidentAlerts=0 ,redAlert=0,yellowAlert=0;
		EFmFmTripAlertsPO eFmFmTripAlertsPO=new EFmFmTripAlertsPO();
		eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);
		eFmFmTripAlertsPO.setCreationTime(new Date());	
		
		List<EFmFmVendorMasterPO> listOfVendorByVehicle=iVendorDetailsBO.getAllEnableVendorsDetails(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
				assignRoutePO.getCombinedFacility()));	
		
		if(!(listOfVendorByVehicle.isEmpty())){													
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){					
				List<EFmFmVehicleMasterPO> listOfDriver=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
				for(EFmFmVehicleMasterPO vehicleDetails:listOfDriver){
					Date TodayDate=new Date();	
					if(vehicleDetails.getStatus().equalsIgnoreCase("A")|| vehicleDetails.getStatus().equalsIgnoreCase("allocated")){				
						if(vehicleDetails.getPolutionValid().before(TodayDate)){												
							polutionExpired++;
							polutionExpiredFlg="R";
							polutionExpiredMsgFlg=true;
						}else{
							int diffInDays = (int) ((vehicleDetails.getPolutionValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()){													
								polutionExpired++;
								polutionExpiredMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()/3;
								yellowAlert=redAlert+redAlert;			
								if(!polutionExpiredFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										polutionExpiredFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										polutionExpiredFlg="Y";
									}
								}
								if(!polutionExpiredFlg.equalsIgnoreCase("R") && !polutionExpiredFlg.equalsIgnoreCase("Y")){
									polutionExpiredFlg="G";
								}		
							}
						}	
						//pollution msg entries
						if(polutionExpiredMsgFlg){
							polutionExpiredMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									vehicleDetails.getVehicleId(),"vehicleId","pollution");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"pollution",
											vendorList.geteFmFmClientBranchPO().getPollutionDueSMSNumber(),vendorList.geteFmFmClientBranchPO().getPollutionDueEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getPollutionDueNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getPollutionDueRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"pollution",
												vendorList.geteFmFmClientBranchPO().getPollutionDueSMSNumber(),vendorList.geteFmFmClientBranchPO().getPollutionDueEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getPollutionDueNotificationType());										
									} catch (Exception e) {
										log.debug("exception"+e);
									}
								}
							}
							
						}					
						
						if(vehicleDetails.getInsuranceValidDate().before(TodayDate)){												
							insuranceExpired++;
							insuranceExpiredFlg="R";
							insuranceExpiredMsgFlg=true;
						}else{
							int diffInDays = (int) ((vehicleDetails.getInsuranceValidDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()){													
								insuranceExpired++;
								insuranceExpiredMsgFlg=true;	
								redAlert=vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()/3;
								yellowAlert=redAlert+redAlert;	
								if(!insuranceExpiredFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										insuranceExpiredFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										insuranceExpiredFlg="Y";
									}
								}
								if(!insuranceExpiredFlg.equalsIgnoreCase("R") && !insuranceExpiredFlg.equalsIgnoreCase("Y")){
									insuranceExpiredFlg="G";
								}
								
							}												
						}
						
						//Insurance msg entries
						if(insuranceExpiredMsgFlg){
							insuranceExpiredMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									vehicleDetails.getVehicleId(),"vehicleId","insurance");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"insurance",
											vendorList.geteFmFmClientBranchPO().getInsuranceDueSMSNumber(),vendorList.geteFmFmClientBranchPO().getInsuranceDueEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getInsuranceDueNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getInsuranceDueRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"insurance",
												vendorList.geteFmFmClientBranchPO().getInsuranceDueSMSNumber(),vendorList.geteFmFmClientBranchPO().getInsuranceDueEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getInsuranceDueNotificationType());									
									} catch (Exception e) {
										log.debug("exception"+e);
									}
								}
							}
							
						}				
						//Tax valid Date
						if(vehicleDetails.getTaxCertificateValid().before(TodayDate)){												
							taxCertificateValid++;
							taxCertificateValidFlg="R";
							taxCertificateValidMsgFlg=true;
						}else{
							int diffInDays = (int) ((vehicleDetails.getTaxCertificateValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getTaxCertificateExpiryDay()){													
								taxCertificateValid++;
								taxCertificateValidMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getTaxCertificateExpiryDay()/3;
								yellowAlert=redAlert+redAlert;	
								if(!taxCertificateValidFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										taxCertificateValidFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										taxCertificateValidFlg="Y";
									}
								}
								if(!taxCertificateValidFlg.equalsIgnoreCase("R") && !taxCertificateValidFlg.equalsIgnoreCase("Y")){
									taxCertificateValidFlg="G";
								}
							}												
						}
						
						//tax Valid msg entries
						if(taxCertificateValidMsgFlg){
							taxCertificateValidMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									vehicleDetails.getVehicleId(),"vehicleId","taxCertificate");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"taxCertificate",
											vendorList.geteFmFmClientBranchPO().getTaxCertificateSMSNumber(),vendorList.geteFmFmClientBranchPO().getTaxCertificateEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getTaxCertificateNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getTaxCertificateRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"taxCertificate",
												vendorList.geteFmFmClientBranchPO().getTaxCertificateSMSNumber(),vendorList.geteFmFmClientBranchPO().getTaxCertificateEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getTaxCertificateNotificationType());									
									} catch (Exception e) {
										log.debug("exception"+e);
									}
								}
							}
							
						}				
						
						//Permit valid Date
						if(vehicleDetails.getPermitValidDate().before(TodayDate)){												
							permitValid++;
							permitValidFlg="R";
							permitValidMsgFlg=true;
						}else{
							int diffInDays = (int) ((vehicleDetails.getPermitValidDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getPermitDueExpiryDay()){													
								permitValid++;
								permitValidMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getPermitDueExpiryDay()/3;
								yellowAlert=redAlert+redAlert;	
								if(!permitValidFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										permitValidFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										permitValidFlg="Y";
									}
								}
								if(!permitValidFlg.equalsIgnoreCase("R") && !permitValidFlg.equalsIgnoreCase("Y")){
									permitValidFlg="G";
								}
							}												
						}
						
						//Permit msg entries
						if(permitValidMsgFlg){
							permitValidMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									vehicleDetails.getVehicleId(),"vehicleId","permit");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"permit",
											vendorList.geteFmFmClientBranchPO().getPermitDueSMSNumber(),vendorList.geteFmFmClientBranchPO().getPermitDueEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getPermitDueNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getPermitDueRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"permit",
												vendorList.geteFmFmClientBranchPO().getPermitDueSMSNumber(),vendorList.geteFmFmClientBranchPO().getPermitDueEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getPermitDueNotificationType());									
									} catch (Exception e) {
										log.debug("exception"+e);
									}
								}
							}
							
						}				
						
						//VehicleMaitenance valid Date
						if(vehicleDetails.getVehicleFitnessDate().before(TodayDate)){												
							vehicleMaintenance++;
							vehicleMaintenanceFlg="R";
							vehicleMaintenanceMsgFlg=true;
						}else{
							int diffInDays = (int) ((vehicleDetails.getVehicleFitnessDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()){													
								vehicleMaintenance++;
								vehicleMaintenanceMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()/3;
								yellowAlert=redAlert+redAlert;
								if(!vehicleMaintenanceFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										vehicleMaintenanceFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										vehicleMaintenanceFlg="Y";
									}
								}
								if(!vehicleMaintenanceFlg.equalsIgnoreCase("R") && !vehicleMaintenanceFlg.equalsIgnoreCase("Y")){
									vehicleMaintenanceFlg="G";
								}
							}												
						}
						
						//VehicleMaitenance msg entries
						if(vehicleMaintenanceMsgFlg){
							vehicleMaintenanceMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									vehicleDetails.getVehicleId(),"vehicleId","vehicleMaintenance");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"vehicleMaintenance",
											vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceSMSNumber(),vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("vehicleId",vehicleDetails.getVehicleId(),"vehicleMaintenance",
												vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceSMSNumber(),vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceNotificationType());									
									} catch (Exception e) {
										log.debug("exception" + e);
									}
								}
							}

						}
					}
				}
			}
		}

		if(!(listOfVendorByVehicle.isEmpty())){			
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
				List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());	
				for(EFmFmDriverMasterPO driverDetails:listOfDriver){				
					Date TodayDate=new Date();
					if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
						
						if(driverDetails.getLicenceValid().before(TodayDate)){
							licenseExpire++;
							licenseExpireFlg="R";
							licenseExpireMsgFlg=true;
						}else{
							int diffInDays = (int) ((driverDetails.getLicenceValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getLicenseExpiryDay()){													
								licenseExpire++;
								licenseExpireMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getLicenseExpiryDay()/3;
								yellowAlert=redAlert+redAlert;
								if(!licenseExpireFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										licenseExpireFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										licenseExpireFlg="Y";
									}
								}
								if(!licenseExpireFlg.equalsIgnoreCase("R") && !licenseExpireFlg.equalsIgnoreCase("Y")){
									licenseExpireFlg="G";
								}
							}												
						}

						//Licence msg entries
						
							if(licenseExpireMsgFlg){
								licenseExpireMsgFlg=false;
								List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
										driverDetails.getDriverId(),"driverId","license");
								if(particularaAlertDetail.isEmpty()){
									try {
										addSMSEmailDetails("driverId",driverDetails.getDriverId(),"license",
												vendorList.geteFmFmClientBranchPO().getLicenseSMSNumber(),vendorList.geteFmFmClientBranchPO().getLicenseEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getLicenceNotificationType());									
									} catch (Exception e) {
										log.debug("exception"+e);
									}
								}else{								
									int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(repeatAlert==vendorList.geteFmFmClientBranchPO().getLicenseRepeatAlertsEvery()){
										try {
											addSMSEmailDetails("driverId",driverDetails.getDriverId(),"license",
													vendorList.geteFmFmClientBranchPO().getLicenseSMSNumber(),vendorList.geteFmFmClientBranchPO().getLicenseEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getLicenceNotificationType());									
										} catch (Exception e) {
											log.debug("exception"+e);
										}
									}
								}
								
							}
						
						if(driverDetails.getMedicalFitnessCertificateValid().before(TodayDate)){
							medicalFitnessCertificateValid++;
							medicalFitnessCertificateValidFlg="R";
							medicalFitnessCertificateValidMsgFlg=true;
						}else{
							int diffInDays = (int) ((driverDetails.getMedicalFitnessCertificateValid().getTime()- TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getMedicalFitnessExpiryDay()){													
								medicalFitnessCertificateValid++;
								medicalFitnessCertificateValidMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getMedicalFitnessExpiryDay()/3;
								yellowAlert=redAlert+redAlert;
								if(!medicalFitnessCertificateValidFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										medicalFitnessCertificateValidFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										medicalFitnessCertificateValidFlg="Y";
									}
								}
								if(!medicalFitnessCertificateValidFlg.equalsIgnoreCase("R") && !medicalFitnessCertificateValidFlg.equalsIgnoreCase("Y")){
									medicalFitnessCertificateValidFlg="G";
								}
							}												
						}
						
						//medicalFitness msg entries
						
						if(medicalFitnessCertificateValidMsgFlg){
							medicalFitnessCertificateValidMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									driverDetails.getDriverId(),"driverId","medicalFitnessCertificate");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("driverId",driverDetails.getDriverId(),"medicalFitnessCertificate",
											vendorList.geteFmFmClientBranchPO().getMedicalFitnessSMSNumber(),vendorList.geteFmFmClientBranchPO().getMedicalFitnessEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getMedicalFitnessNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getMedicalFitnessRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("driverId",driverDetails.getDriverId(),"medicalFitnessCertificate",
												vendorList.geteFmFmClientBranchPO().getMedicalFitnessSMSNumber(),vendorList.geteFmFmClientBranchPO().getMedicalFitnessEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getMedicalFitnessNotificationType());									
									} catch (Exception e) {
										log.debug("exception" + e);
									}
								}
							}

						}
						
						//Police Verification Valid
						if(driverDetails.getPoliceVerificationValid().before(TodayDate)){
							policeVarificationValid++;
							policeVarificationValidFlg="R";
							policeVarificationValidMsgFlg=true;
						}else{
							int diffInDays = (int) ((driverDetails.getPoliceVerificationValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()){													
								policeVarificationValid++;
								policeVarificationValidMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()/3;
								yellowAlert=redAlert+redAlert;
								if(!policeVarificationValidFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										policeVarificationValidFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										policeVarificationValidFlg="Y";
									}
								}
								if(!policeVarificationValidFlg.equalsIgnoreCase("R") && !policeVarificationValidFlg.equalsIgnoreCase("Y")){
									policeVarificationValidFlg="G";
								}
							}												
						}
						
						
						//PoliceVerification msg entries
						
						if(policeVarificationValidMsgFlg){
							policeVarificationValidMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									driverDetails.getDriverId(),"driverId","policeVarification");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("driverId",driverDetails.getDriverId(),"policeVarification",
											vendorList.geteFmFmClientBranchPO().getPoliceVerificationSMSNumber(),vendorList.geteFmFmClientBranchPO().getPoliceVerificationEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getPoliceVerificationNotificationType());									
								} catch (Exception e) {
									log.debug("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getPoliceVerificationRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("driverId",driverDetails.getDriverId(),"policeVarification",
												vendorList.geteFmFmClientBranchPO().getPoliceVerificationSMSNumber(),vendorList.geteFmFmClientBranchPO().getPoliceVerificationEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getPoliceVerificationNotificationType());									
									} catch (Exception e) {
										log.debug("exception"+e);
									}
								}
							}
							
						}
						
						
						//DD Training Valid
						if(driverDetails.getDdtValidDate().before(TodayDate)){
							ddTrainingValid++;
							ddTrainingValidFlg="R";
							ddTrainingValidMsgFlg=true;
						}else{
							int diffInDays = (int) ((driverDetails.getDdtValidDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getDdTrainingExpiryDay()){													
								ddTrainingValid++;
								ddTrainingValidMsgFlg=true;
								redAlert=vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()/3;
								yellowAlert=redAlert+redAlert;
								if(!ddTrainingValidFlg.equalsIgnoreCase("R")){
									if(diffInDays<=redAlert)	{
										ddTrainingValidFlg="R";
									}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
										ddTrainingValidFlg="Y";
									}
								}
								if(!ddTrainingValidFlg.equalsIgnoreCase("R") && !ddTrainingValidFlg.equalsIgnoreCase("Y")){
									ddTrainingValidFlg="G";
								}
							}												
						}
						
						
						//DDTraining msg entries
						
						if(ddTrainingValidMsgFlg){
							ddTrainingValidMsgFlg=false;
							List<EFmFmSmsAlertMasterPO> particularaAlertDetail=iAlertBO.getSMSRecordExist(vendorList.geteFmFmClientBranchPO().getBranchId(),
									driverDetails.getDriverId(),"driverId","ddTraining");
							if(particularaAlertDetail.isEmpty()){
								try {
									addSMSEmailDetails("driverId",driverDetails.getDriverId(),"ddTraining",
											vendorList.geteFmFmClientBranchPO().getDdTrainingSMSNumber(),vendorList.geteFmFmClientBranchPO().getDdTrainingEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getDdTrainingNotificationType());									
								} catch (Exception e) {
									log.info("exception"+e);
								}
							}else{								
								int repeatAlert = (int) ((particularaAlertDetail.get(0).getCreatedDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(repeatAlert==vendorList.geteFmFmClientBranchPO().getDdTrainingRepeatAlertsEvery()){
									try {
										addSMSEmailDetails("driverId",driverDetails.getDriverId(),"ddTraining",
												vendorList.geteFmFmClientBranchPO().getDdTrainingSMSNumber(),vendorList.geteFmFmClientBranchPO().getDdTrainingEmailId(),vendorList.geteFmFmClientBranchPO(),vendorList.geteFmFmClientBranchPO().getDdTrainingNotificationType());									
									} catch (Exception e) {
										log.info("exception"+e);
									}
								}
							}
							
						}
						

					}
				}	


			}
		}	
		/*
		 * Governance Alerts Details 
		 */		
		eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);								
		requests.put("noShowGuests", iCabRequestBO.getAllActiveNoShowGuestRequestCounter(assignRoutePO.getCombinedFacility()));
		requests.put("numberOfGuestRequest", iCabRequestBO.getAllActiveDropOrPickupRequestCounterForGuest(assignRoutePO.getCombinedFacility()));
		requests.put("boardedGuest", iCabRequestBO.getAllActiveBoardedGuestRequestCounter(assignRoutePO.getCombinedFacility())); 
		requests.put("tripScheduledForGuest",iCabRequestBO.getAllScheduleActiveRequestsForGuest(assignRoutePO.getCombinedFacility()));		

		requests.put("vehiclesInTrip",assignRouteBO.getAllVehiclesOnRoadCounter(assignRoutePO.getCombinedFacility()));
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String requestDate = requestDateFormat.format(new Date());

		requests.put("numberOfPickUpRequest", iCabRequestBO.getAllActivePickUpRequestCounterForTodays(assignRoutePO.getCombinedFacility(),requestDate));
		requests.put("boardedEmployee", iCabRequestBO.getAllActiveBoardedEmployeeRequestCounter(assignRoutePO.getCombinedFacility())); //employee picked in pickup tab
		requests.put("pickUpScheduled",iCabRequestBO.getAllPickupScheduleActiveRequests(assignRoutePO.getCombinedFacility()));		
		requests.put("noShowPickUp", iCabRequestBO.getAllActiveNoShowEmployeeRequestCounter(assignRoutePO.getCombinedFacility()));
		requests.put("pickUpInProgress", iCabRequestBO.getAllActivePickupInProgressEmployeeRequestCounter(assignRoutePO.getCombinedFacility()));

		requests.put("numberOfDropRequest", iCabRequestBO.getAllActiveDropRequestCounterForTodays(assignRoutePO.getCombinedFacility(),requestDate));
		requests.put("dropedEmployee", iCabRequestBO.getAllActiveDropedEmployeeRequestCounter(assignRoutePO.getCombinedFacility()));
		requests.put("noShowDrop", iCabRequestBO.getAllActiveDropNoShowEmployeeRequestCounter(assignRoutePO.getCombinedFacility()));
		requests.put("dropScheduled",iCabRequestBO.getAllDropScheduleActiveRequests(assignRoutePO.getCombinedFacility()));		
		requests.put("dropInProgress", iCabRequestBO.getAllActiveDropInProgressEmployeeRequestCounter(assignRoutePO.getCombinedFacility()));
		
		requests.put("roadAlerts",iAlertBO.getAllTodaysTripRoadAlertsCount(assignRoutePO.getCombinedFacility()));
		requests.put("sosAlerts", iAlertBO.getAllTodaysTripSOSAlertsCount(assignRoutePO.getCombinedFacility()));
		requests.put("openAlerts", iAlertBO.getAllTodaysTripOpenAlertsCount(assignRoutePO.getCombinedFacility()));
		requests.put("closeAlerts", iAlertBO.getAllTodaysTripCloseAlertsCount(assignRoutePO.getCombinedFacility()));

		requests.put("insuranceExpired", insuranceExpired);		
		requests.put("polutionExpired", polutionExpired);
		requests.put("breakDownalert", breakDownalert);		
		requests.put("licenseExpire", licenseExpire);		
		requests.put("medicalFitnessCertificateValid", medicalFitnessCertificateValid);			
		requests.put("policeVarificationValid", policeVarificationValid);		
		requests.put("ddTrainingValid", ddTrainingValid);		
		requests.put("taxCertificateValid", taxCertificateValid);		
		requests.put("permitValid", permitValid);	
		requests.put("vehicleMaintenance", vehicleMaintenance);		
		requests.put("speedAlert", speedAlert);		
		requests.put("accidentAlerts", accidentAlerts);		
		requests.put("polutionExpiredFlg",polutionExpiredFlg);
		requests.put("insuranceExpiredFlg",insuranceExpiredFlg);
		requests.put("licenseExpireFlg",licenseExpireFlg);
		requests.put("medicalFitnessCertificateValidFlg",medicalFitnessCertificateValidFlg);
		requests.put("policeVarificationValidFlg",policeVarificationValidFlg);
		requests.put("ddTrainingValidFlg",ddTrainingValidFlg);
		requests.put("taxCertificateValidFlg",taxCertificateValidFlg);
		requests.put("permitValidFlg",permitValidFlg);
		requests.put("vehicleMaintenanceFlg",vehicleMaintenanceFlg);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	

	
	/*
	 * Inserting the SMS /Email Record into EFmFmSmsAlertMaster
	 *  
	 */	
	
	private void addSMSEmailDetails(String userType, int vehicleId, String alertType, String pollutionDueSMSNumber,
			String pollutionDueEmailId,EFmFmClientBranchPO eFmFmClientBranchPO,String notoficationType) {		   
			IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
			EFmFmSmsAlertMasterPO eFmFmSmsAlertMasterPO=new EFmFmSmsAlertMasterPO(); 
			eFmFmSmsAlertMasterPO.setUserType(userType);
			eFmFmSmsAlertMasterPO.setUniqueId(vehicleId);
			eFmFmSmsAlertMasterPO.setAlertType(alertType);	
			eFmFmSmsAlertMasterPO.setAlertTitle(notoficationType);
			if(pollutionDueSMSNumber==null || pollutionDueSMSNumber.isEmpty() || pollutionDueSMSNumber==""){
				eFmFmSmsAlertMasterPO.setSmsAlertStatus("N");
			}else{
				eFmFmSmsAlertMasterPO.setSmsAlertStatus("Y");
				eFmFmSmsAlertMasterPO.setMobileNumber(pollutionDueSMSNumber);
			}
			
			if(pollutionDueEmailId==null || pollutionDueEmailId.isEmpty() || pollutionDueEmailId==""){
				eFmFmSmsAlertMasterPO.setEmailAlertStatus("N");
			}else{
				eFmFmSmsAlertMasterPO.setEmailAlertStatus("Y");
				eFmFmSmsAlertMasterPO.setEmailId(pollutionDueEmailId);
			}		
			eFmFmSmsAlertMasterPO.setCreatedDate(new Date());
			eFmFmSmsAlertMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			iAlertBO.addSMSAlertDetails(eFmFmSmsAlertMasterPO);					
		}
	/*
	 * Excel  Download
	 * 
	 */
	
	@POST
	@Path("/getDashBoardDetailListDwn")
	public Response getDashBoardDetailListDwn(EFmFmEmployeeTravelRequestPO travelRequestPO) throws UnsupportedEncodingException{	    	
		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
		if (!(userDetail.isEmpty())) {
			String jwtToken = "";
			try {
				JwtTokenGenerator token = new JwtTokenGenerator();
				jwtToken = token.generateToken();
				userDetail.get(0).setAuthorizationToken(jwtToken);
				userDetail.get(0).setTokenGenerationTime(new Date());
				userMasterBO.update(userDetail.get(0));
			} catch (Exception e) {
				log.info("error" + e);
			}
		}
	
		}catch(Exception e){
			log.info("authentication error"+e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
		String tripType=travelRequestPO.getTripType();
		String activityType=travelRequestPO.getRequestStatus();	    	
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");	
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		List<Map<String,Object>> dashBoardActivity = new ArrayList<>();
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		DateFormat  dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        XSSFCellStyle style = workbook.createCellStyle();       
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255,255,255)));//color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0,82,128)));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(font); 
        int rownum = 0, noOfRoute = 0;
        Row OutSiderow = sheet.createRow(rownum++); 
		if(tripType.toUpperCase().equalsIgnoreCase("ALERTS")){			
			for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
				sheet.autoSizeColumn(columnIndex);
			}
					Cell zerothCol = OutSiderow.createCell(0);
					zerothCol.setCellValue("Alert Date");
					zerothCol.setCellStyle(style);					
					Cell firstCol = OutSiderow.createCell(1);
					firstCol.setCellValue("Zone Name");
					firstCol.setCellStyle(style);					
					Cell secondCol = OutSiderow.createCell(2);
					secondCol.setCellValue("User Type");
					secondCol.setCellStyle(style);					
					Cell thirdCol = OutSiderow.createCell(3);
					thirdCol.setCellValue("Driver Name");
					thirdCol.setCellStyle(style);					
					Cell fourthCol = OutSiderow.createCell(4);
					fourthCol.setCellValue("Driver Mobile Number");
					fourthCol.setCellStyle(style);					
					Cell fifthCol = OutSiderow.createCell(5);
					fifthCol.setCellValue("Vehicle Number");
					fifthCol.setCellStyle(style);					
					Cell sixthCol = OutSiderow.createCell(6);
					sixthCol.setCellValue("Title");
					sixthCol.setCellStyle(style);		
					Cell seventhCol = OutSiderow.createCell(7);
					seventhCol.setCellValue("Facility Name");
					seventhCol.setCellStyle(style);
					Cell eighthCol = OutSiderow.createCell(8);
					eighthCol.setCellValue("Description");
					eighthCol.setCellStyle(style);	
		}else if (tripType.toUpperCase().equalsIgnoreCase("EMPLOYEESTATUS")){	
			for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
				sheet.autoSizeColumn(columnIndex);
			}
					Cell zerothCol = OutSiderow.createCell(0);
					zerothCol.setCellValue("Request ID");
					zerothCol.setCellStyle(style);					
					Cell firstCol = OutSiderow.createCell(1);
					firstCol.setCellValue("Request Time");
					firstCol.setCellStyle(style);					
					Cell secondCol = OutSiderow.createCell(2);
					secondCol.setCellValue("Employee Id");
					secondCol.setCellStyle(style);					
					Cell thirdCol = OutSiderow.createCell(3);
					thirdCol.setCellValue("Employee Name");
					thirdCol.setCellStyle(style);					
					Cell fourthCol = OutSiderow.createCell(4);
					fourthCol.setCellValue("Gender");
					fourthCol.setCellStyle(style);	
					Cell fifthCol = OutSiderow.createCell(5);
					fifthCol.setCellValue("Facility Name");
					fifthCol.setCellStyle(style);		
					Cell sixthCol = OutSiderow.createCell(6);
					sixthCol.setCellValue("Address");
					sixthCol.setCellStyle(style);					
					
		}else if (tripType.toUpperCase().equalsIgnoreCase("GOVERNANCEDRIVERS")){	
			for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
				sheet.autoSizeColumn(columnIndex);
			}
					Cell zerothCol = OutSiderow.createCell(0);
					zerothCol.setCellValue("Vendor Name");
					zerothCol.setCellStyle(style);					
					Cell firstCol = OutSiderow.createCell(1);
					firstCol.setCellValue("Vendor ContactName");
					firstCol.setCellStyle(style);					
					Cell secondCol = OutSiderow.createCell(2);
					secondCol.setCellValue("Vendor MobileNumber");
					secondCol.setCellStyle(style);					
					Cell thirdCol = OutSiderow.createCell(3);
					thirdCol.setCellValue("Driver Name");
					thirdCol.setCellStyle(style);					
					Cell fourthCol = OutSiderow.createCell(4);
					fourthCol.setCellValue("Mobile Number");
					fourthCol.setCellStyle(style);					
					Cell fifthCol = OutSiderow.createCell(5);
					fifthCol.setCellValue("Facility Name");
					fifthCol.setCellStyle(style);		
					Cell sixthCol = OutSiderow.createCell(6);
					sixthCol.setCellValue("Licence Number");
					sixthCol.setCellStyle(style);
					
		}else if (tripType.toUpperCase().equalsIgnoreCase("GOVERNCEVEHICLES")){	
			for (int columnIndex = 0; columnIndex < 6; columnIndex++) {
				sheet.autoSizeColumn(columnIndex);
			}
					Cell zerothCol = OutSiderow.createCell(0);
					zerothCol.setCellValue("Vendor Name");
					zerothCol.setCellStyle(style);					
					Cell firstCol = OutSiderow.createCell(1);
					firstCol.setCellValue("Vehicle Number");
					firstCol.setCellStyle(style);					
					Cell secondCol = OutSiderow.createCell(2);
					secondCol.setCellValue("Vendor ContactName");
					secondCol.setCellStyle(style);					
					Cell thirdCol = OutSiderow.createCell(3);
					thirdCol.setCellValue("Vendor MobileNumber");
					thirdCol.setCellStyle(style);					
					Cell fourthCol = OutSiderow.createCell(4);
					fourthCol.setCellValue("Facility Name");
					fourthCol.setCellStyle(style);	
		}
		
		switch (tripType.toUpperCase()) {			
		case "EMPLOYEESTATUS":	
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = requestDateFormat.format(new Date());
			switch (activityType.toLowerCase()) {	
			case "pickuprequest":	
				List<EFmFmEmployeeTravelRequestPO> employeesPickupRequestDetails=iCabRequestBO.getAllActivePickUpRequestDetailsForToday(travelRequestPO.getCombinedFacility(),currentDate);
				if(!(employeesPickupRequestDetails.isEmpty())){			
					for(EFmFmEmployeeTravelRequestPO allTravelRequest:employeesPickupRequestDetails){
						Map<String, Object>  requestList= new HashMap<>();				
						requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(allTravelRequest.getRequestDate()));
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.getBranchName());
						requestList.put("employeeName", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
	                    requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
	                    OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(allTravelRequest.getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(allTravelRequest.getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);	
						
						if(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
							gender.setCellValue(1);
						}
						else{
							requestList.put("gender", 2);
							gender.setCellValue(2);
						}
						Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( allTravelRequest.getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
					
						dashBoardActivity.add(requestList);
					}									
				}						
				break;
			case "droprequest":	
				List<EFmFmEmployeeTravelRequestPO> employeesDropRequestDetails=iCabRequestBO.getAllActiveDropRequestDetailsForToday(travelRequestPO.getCombinedFacility(),currentDate);
				if(!(employeesDropRequestDetails.isEmpty())){			
					for(EFmFmEmployeeTravelRequestPO allTravelRequest:employeesDropRequestDetails){
						Map<String, Object>  requestList= new HashMap<>();		
						requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(allTravelRequest.getRequestDate()));
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(allTravelRequest.getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(allTravelRequest.getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);	
                        if(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);
							gender.setCellValue(1);
						}
						else{
							requestList.put("gender", 2);	
							gender.setCellValue(2);
						}
                        
                    	Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( allTravelRequest.getBranchName());
						
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
						
						dashBoardActivity.add(requestList);
					}									
				}
				break;
			case "pickupschedule":
				List<EFmFmEmployeeTravelRequestPO> pickupScheduleEmployees=iCabRequestBO.getAllPickupScheduleActiveRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(pickupScheduleEmployees.isEmpty())){
					for(EFmFmEmployeeTravelRequestPO travelRequest:pickupScheduleEmployees){
						Map<String, Object>  requestList= new HashMap<>();											
						requestList.put("employeeId", travelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", travelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(travelRequest.getRequestDate()));
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(travelRequest.getShiftTime()));
						requestList.put("facilityName", travelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(travelRequest.getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(travelRequest.getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(travelRequest.getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);	
                        if(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);
							gender.setCellValue(1);
						}else{
							requestList.put("gender", 2);
							gender.setCellValue(2);
						}
                        
                    	Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( travelRequest.getBranchName());
						
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
						
						dashBoardActivity.add(requestList);
					}	
				}
				break;
			case "dropschedule":									
				List<EFmFmEmployeeTravelRequestPO> dropScheduleEmployees=iCabRequestBO.getAllDropScheduleActiveRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(dropScheduleEmployees.isEmpty())){
					for(EFmFmEmployeeTravelRequestPO travelRequest:dropScheduleEmployees){
						Map<String, Object>  requestList= new HashMap<>();											
						requestList.put("employeeId", travelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", travelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(travelRequest.getRequestDate()));
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(travelRequest.getShiftTime()));
						requestList.put("facilityName", travelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(travelRequest.getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(travelRequest.getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(travelRequest.getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);	
						
                        if(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
							gender.setCellValue(1);
						}else{
							requestList.put("gender", 2);
							gender.setCellValue(2);
						}
                        
                    	Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( travelRequest.getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
						
						dashBoardActivity.add(requestList);
					}	
				}
				break;
			case "guestrequests":	
				List<EFmFmEmployeeTravelRequestPO> guestRequestDetails=iCabRequestBO.getAllActiveGuestRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(guestRequestDetails.isEmpty())){			
					for(EFmFmEmployeeTravelRequestPO allTravelRequest:guestRequestDetails){
						Map<String, Object>  requestList= new HashMap<>();		
						requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(allTravelRequest.getRequestDate()));
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(allTravelRequest.getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(allTravelRequest.getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);
						
                        if(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);
							gender.setCellValue(1);
						}
						else{
							requestList.put("gender", 2);
							gender.setCellValue(2);
						}
                        
                        Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( allTravelRequest.getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
						
						dashBoardActivity.add(requestList);
					}									
				}
				break;		
			case "guestschedulerequests":									
				List<EFmFmEmployeeTravelRequestPO> guestRequests=iCabRequestBO.getAllScheduleActiveGuestRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(guestRequests.isEmpty())){
					for(EFmFmEmployeeTravelRequestPO travelRequest:guestRequests){
						Map<String, Object>  requestList= new HashMap<>();											
						requestList.put("employeeId", travelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("requestId", travelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(travelRequest.getRequestDate()));
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(travelRequest.getShiftTime()));
						requestList.put("facilityName", travelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(travelRequest.getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(travelRequest.getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(travelRequest.getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);
						
                        if(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);
							gender.setCellValue(1);
						}else{
							requestList.put("gender", 2);	
							gender.setCellValue(2);
						}
                        
                        Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( travelRequest.getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
						
                        dashBoardActivity.add(requestList);
					}	
				}
				break;
			case "guestboardedordropped":
				List<EFmFmEmployeeTripDetailPO> boardedOrDroppedRequestDetails=iCabRequestBO.getAllActiveBoardedOrDroppedEmployeeRequestsDetailsForGuest(travelRequestPO.getCombinedFacility());
				if(!(boardedOrDroppedRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:boardedOrDroppedRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripType", employeeDetails.geteFmFmEmployeeTravelRequest().getTripType());
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));				
						
						Cell gender = OutSiderow.createCell(4);
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
							gender.setCellValue(1);
						}
						else{
							activity.put("gender", 2); 
							gender.setCellValue(2);
						}
                        
                        Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
						
                        
						dashBoardActivity.add(activity);
					}
				}
				break;				
			case "guestnoshow":
				List<EFmFmEmployeeTripDetailPO> guestNoShowRequestDetails=iCabRequestBO.getAllActiveGuestNoShowRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(guestNoShowRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:guestNoShowRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripType", employeeDetails.geteFmFmEmployeeTravelRequest().getTripType());
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));				
						Cell gender = OutSiderow.createCell(4);
						if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
							gender.setCellValue(1);
						}
						else{
							activity.put("gender", 2); 
							gender.setCellValue(2);
						}
						
						 Cell facilityName = OutSiderow.createCell(5);
						 facilityName.setCellValue( employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
						 Cell employeeAddress = OutSiderow.createCell(6);
						 employeeAddress.setCellValue( new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
							
						 dashBoardActivity.add(activity);
					}
				}
				break;	
				
			case "pickupboarded":
				List<EFmFmEmployeeTripDetailPO> pickupBoardedRequestDetails=iCabRequestBO.getAllActivePickupBoardedEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(pickupBoardedRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:pickupBoardedRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));				
						
						Cell gender = OutSiderow.createCell(4);
						if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
							gender.setCellValue(1);
						}
						else{
							activity.put("gender", 2); 
							gender.setCellValue(2);
						}
						
						 Cell facilityName = OutSiderow.createCell(5);
						 facilityName.setCellValue( employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
						 Cell employeeAddress = OutSiderow.createCell(6);
						 employeeAddress.setCellValue( new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
						 
						 dashBoardActivity.add(activity);
					}
				}
				break;
			case "dropboarded":										
				List<EFmFmEmployeeTripDetailPO> dropBoardedRequestDetails=iCabRequestBO.getAllActiveDropedEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(dropBoardedRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:dropBoardedRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));				
						
						Cell gender = OutSiderow.createCell(4);
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
							gender.setCellValue(1);
						}
						else{
							activity.put("gender", 2); 
							gender.setCellValue(2);
						}
                        
                        Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
						 
						dashBoardActivity.add(activity);
					}
				}
				break;
			case "noshowpickup":
				List<EFmFmEmployeeTripDetailPO> noShowPickupRequestDetails=iCabRequestBO.getAllActivePickupNoShowEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(noShowPickupRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:noShowPickupRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));				
						
						Cell gender = OutSiderow.createCell(4);
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
							gender.setCellValue(1);
						}
						else{
							activity.put("gender", 2); 
							gender.setCellValue(2);
						}
                        
                        Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        
						dashBoardActivity.add(activity);
					}
				}
				break;							   
			case "noshowdrop":	
				List<EFmFmEmployeeTripDetailPO> noShowDropRequestDetails=iCabRequestBO.getAllActiveDropNoShowEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(noShowDropRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:noShowDropRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        OutSiderow = sheet.createRow(rownum++);	
	                    Cell requestId = OutSiderow.createCell(0);
						requestId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						Cell requestDate = OutSiderow.createCell(1);
						requestDate.setCellValue(timeFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime()));
						Cell employeeId = OutSiderow.createCell(2);	
						employeeId.setCellValue(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						Cell employeeName = OutSiderow.createCell(3);
						employeeName.setCellValue(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));				
						
						Cell gender = OutSiderow.createCell(4);
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
							gender.setCellValue(1);
						}
						else{
							activity.put("gender", 2); 
							gender.setCellValue(2);
						}
                        
                        Cell facilityName = OutSiderow.createCell(5);
						facilityName.setCellValue( employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
						Cell employeeAddress = OutSiderow.createCell(6);
						employeeAddress.setCellValue( new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
						
						dashBoardActivity.add(activity);
					}
				}
				break;		
			}
			break;
		case "VEHICLESTATUS":
			break;
		case "GOVERNANCEDRIVERS":						
			List<EFmFmVendorMasterPO> listOfVendorByDriver=iVendorDetailsBO.getAllEnableVendorsDetails(travelRequestPO.getCombinedFacility());	
			switch (activityType.toLowerCase()) {	
			case "policevarificationexp":																			
				if(!(listOfVendorByDriver.isEmpty())){
					Cell seventhCol = OutSiderow.createCell(7);
					seventhCol.setCellValue("Police Varification Exp Date");
					seventhCol.setCellStyle(style);					
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){										
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());		
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getPoliceVerificationValid().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("policeExpDate",dateFormat.format(driverDetails.getPoliceVerificationValid()));
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
									
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);	
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vendorContactName = OutSiderow.createCell(1);	
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(2);	
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell driverName = OutSiderow.createCell(3);	
									driverName.setCellValue(driverDetails.getFirstName());
									Cell mobileNumber = OutSiderow.createCell(4);	
									mobileNumber.setCellValue(driverDetails.getMobileNumber());
									Cell  facilityName= OutSiderow.createCell(5);	
									facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell licenceNumber = OutSiderow.createCell(6);	
									licenceNumber.setCellValue(driverDetails.getLicenceNumber());
									Cell  policeExpDate= OutSiderow.createCell(7);	
									policeExpDate.setCellValue(dateFormat.format(driverDetails.getPoliceVerificationValid()));
									
								}else{
									int diffInDays = (int) ((driverDetails.getPoliceVerificationValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()){													
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());													
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("policeExpDate",dateFormat.format(driverDetails.getPoliceVerificationValid()));
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);	
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vendorContactName = OutSiderow.createCell(1);	
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(2);	
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell driverName = OutSiderow.createCell(3);	
										driverName.setCellValue(driverDetails.getFirstName());
										Cell mobileNumber = OutSiderow.createCell(4);	
										mobileNumber.setCellValue(driverDetails.getMobileNumber());
										Cell  facilityName= OutSiderow.createCell(5);	
										facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell licenceNumber = OutSiderow.createCell(6);	
										licenceNumber.setCellValue(driverDetails.getLicenceNumber());
										Cell  policeExpDate= OutSiderow.createCell(7);	
										policeExpDate.setCellValue(dateFormat.format(driverDetails.getPoliceVerificationValid()));
										
									}												
								}											

							}
						}							
					}
				}	
				break;
			case "licenseexpire":																			
				if(!(listOfVendorByDriver.isEmpty())){	
					Cell sixthCol = OutSiderow.createCell(7);
					sixthCol.setCellValue("LicenseExpire");
					sixthCol.setCellStyle(style);	
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){						
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());		
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getLicenceValid().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("licenceExpDate",dateFormat.format(driverDetails.getLicenceValid()));
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);	
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vendorContactName = OutSiderow.createCell(1);	
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(2);	
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell driverName = OutSiderow.createCell(3);	
									driverName.setCellValue(driverDetails.getFirstName());
									Cell mobileNumber = OutSiderow.createCell(4);	
									mobileNumber.setCellValue(driverDetails.getMobileNumber());
									Cell  facilityName= OutSiderow.createCell(5);	
									facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell licenceNumber = OutSiderow.createCell(6);	
									licenceNumber.setCellValue(driverDetails.getLicenceNumber());
									Cell  licenceExpDate= OutSiderow.createCell(7);	
									licenceExpDate.setCellValue(dateFormat.format(driverDetails.getLicenceValid()));
									
								}else{
									int diffInDays = (int) ((driverDetails.getLicenceValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getLicenseExpiryDay()){													
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());													
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("licenceExpDate",dateFormat.format(driverDetails.getLicenceValid()));
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);	
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vendorContactName = OutSiderow.createCell(1);	
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(2);	
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell driverName = OutSiderow.createCell(3);	
										driverName.setCellValue(driverDetails.getFirstName());
										Cell mobileNumber = OutSiderow.createCell(4);	
										Cell  facilityName= OutSiderow.createCell(5);	
										facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										mobileNumber.setCellValue(driverDetails.getMobileNumber());
										Cell licenceNumber = OutSiderow.createCell(6);	
										licenceNumber.setCellValue(driverDetails.getLicenceNumber());
										Cell  licenceExpDate= OutSiderow.createCell(7);	
										licenceExpDate.setCellValue(dateFormat.format(driverDetails.getLicenceValid()));
									}												
								}											

							}
						}							
					}
				}	
				break;
			case "ddtrainingexp":																			
				if(!(listOfVendorByDriver.isEmpty())){	
					Cell sixthCol = OutSiderow.createCell(7);
					sixthCol.setCellValue("DD TrainingExp");
					sixthCol.setCellStyle(style);
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){										
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());	
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getDdtValidDate().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("ddExpDate",dateFormat.format(driverDetails.getDdtValidDate()));
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);	
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vendorContactName = OutSiderow.createCell(1);	
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(2);	
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell driverName = OutSiderow.createCell(3);	
									driverName.setCellValue(driverDetails.getFirstName());
									Cell mobileNumber = OutSiderow.createCell(4);	
									Cell  facilityName= OutSiderow.createCell(5);	
									facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									mobileNumber.setCellValue(driverDetails.getMobileNumber());
									Cell licenceNumber = OutSiderow.createCell(6);	
									licenceNumber.setCellValue(driverDetails.getLicenceNumber());
									Cell  ddExpDate= OutSiderow.createCell(7);	
									ddExpDate.setCellValue(dateFormat.format(driverDetails.getDdtValidDate()));
									
								}else{
									int diffInDays = (int) ((driverDetails.getDdtValidDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getDdTrainingExpiryDay()){													
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());													
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("ddExpDate",dateFormat.format(driverDetails.getDdtValidDate()));
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);	
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vendorContactName = OutSiderow.createCell(1);	
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(2);	
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell driverName = OutSiderow.createCell(3);	
										driverName.setCellValue(driverDetails.getFirstName());
										Cell mobileNumber = OutSiderow.createCell(4);	
										mobileNumber.setCellValue(driverDetails.getMobileNumber());
										Cell  facilityName= OutSiderow.createCell(5);	
										facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell licenceNumber = OutSiderow.createCell(6);	
										licenceNumber.setCellValue(driverDetails.getLicenceNumber());
										Cell  ddExpDate= OutSiderow.createCell(7);	
										ddExpDate.setCellValue(dateFormat.format(driverDetails.getDdtValidDate()));
									}												
								}											

							}
						}							
					}
				}	
				break;
			case "medicalexpire":																			
				if(!(listOfVendorByDriver.isEmpty())){	
					Cell sixthCol = OutSiderow.createCell(7);
					sixthCol.setCellValue("Medical Expire");
					sixthCol.setCellStyle(style);
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());	
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getMedicalFitnessCertificateValid().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("medicalExpDate",dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);	
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vendorContactName = OutSiderow.createCell(1);	
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(2);	
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell driverName = OutSiderow.createCell(3);	
									driverName.setCellValue(driverDetails.getFirstName());
									Cell mobileNumber = OutSiderow.createCell(4);	
									mobileNumber.setCellValue(driverDetails.getMobileNumber());
									Cell  facilityName= OutSiderow.createCell(5);	
									facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell licenceNumber = OutSiderow.createCell(6);	
									licenceNumber.setCellValue(driverDetails.getLicenceNumber());
									Cell  medicalExpDate= OutSiderow.createCell(7);	
									medicalExpDate.setCellValue(dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
									dashBoardActivity.add(driverMaster);
								}else{
									int diffInDays = (int) ((driverDetails.getMedicalFitnessCertificateValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getMedicalFitnessExpiryDay()){													
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("medicalExpDate",dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);	
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vendorContactName = OutSiderow.createCell(1);	
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(2);	
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell driverName = OutSiderow.createCell(3);	
										driverName.setCellValue(driverDetails.getFirstName());
										Cell mobileNumber = OutSiderow.createCell(4);	
										Cell  facilityName= OutSiderow.createCell(5);	
										facilityName.setCellValue(driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										mobileNumber.setCellValue(driverDetails.getMobileNumber());
										Cell licenceNumber = OutSiderow.createCell(6);	
										licenceNumber.setCellValue(driverDetails.getLicenceNumber());
										Cell  medicalExpDate= OutSiderow.createCell(7);	
										medicalExpDate.setCellValue(dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
										dashBoardActivity.add(driverMaster);
									}												
								}											

							}
						}
					}
				}	
				break;
			case "accidentalerts":
				Date TodayDate=new Date();
				List<EFmFmTripAlertsPO> allAlerts=iAlertBO.getAllTripAlerts(travelRequestPO.getCombinedFacility());
				if (!(allAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlerts){
						int diffInDays = (int) ((alerts.getCreationTime().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
						if(diffInDays<30){
							if(alerts.getEfmFmAlertTypeMaster().getAlertId()==8){											
								Map<String, Object>  alertsDetails= new HashMap<>();											
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								alertsDetails.put("userType", alerts.getUserType());
								alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());											
								alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
								alertsDetails.put("description", alerts.getEfmFmAlertTypeMaster().getAlertDescription());
								alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
								dashBoardActivity.add(alertsDetails);
							}
						}
					}
				}			
				break;
			case "speedalerts":
				Date todayDate=new Date();
				List<EFmFmTripAlertsPO> allSpeedAlerts=iAlertBO.getAllTripAlerts(travelRequestPO.getCombinedFacility());
				if(!(allSpeedAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allSpeedAlerts){
						int diffInDays = (int) ((alerts.getCreationTime().getTime() -todayDate.getTime()) / (1000 * 60 * 60 * 24));
						if(diffInDays<30){	
							if(alerts.getEfmFmAlertTypeMaster().getAlertId()==10){
								Map<String, Object>  alertsDetails= new HashMap<>();
								if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("")){
									alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is"+alerts.getCurrentSpeed());

								}else{
									alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								}
								alertsDetails.put("userType", alerts.getUserType());
								alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
								alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
								alertsDetails.put("currentSpeed", alerts.getCurrentSpeed());
								alertsDetails.put("description", alerts.getEfmFmAlertTypeMaster().getAlertDescription());
								alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
								dashBoardActivity.add(alertsDetails);
							}
						}
					}
				}			
				break;	
			}
			break;
		case "GOVERNCEVEHICLES":						
			List<EFmFmVendorMasterPO> listOfVendorByVehicle=iVendorDetailsBO.getAllEnableVendorsDetails(travelRequestPO.getCombinedFacility());	
			switch (activityType.toLowerCase()) {							
			case "polutionexpire":																			
				if(!(listOfVendorByVehicle.isEmpty())){		
					Cell fifthColType = OutSiderow.createCell(5);
					fifthColType.setCellValue("Polution Exp Date");
					fifthColType.setCellStyle(style);	
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());								
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getPolutionValid().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("polutionExpDate",dateFormat.format(vehicleDetails.getPolutionValid()));
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(vehicleMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vehicleNumber = OutSiderow.createCell(1);
									vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
									Cell vendorContactName = OutSiderow.createCell(2);
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(3);
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell facilityName = OutSiderow.createCell(4);
									facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell polutionExpDate = OutSiderow.createCell(5);
									polutionExpDate.setCellValue(dateFormat.format(vehicleDetails.getPolutionValid()));
								}else{
									int diffInDays = (int) ((vehicleDetails.getPolutionValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getPollutionDueExpiryDay()){													
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("polutionExpDate",dateFormat.format(vehicleDetails.getPolutionValid()));
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(vehicleMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vehicleNumber = OutSiderow.createCell(1);
										vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
										Cell vendorContactName = OutSiderow.createCell(2);
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(3);
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell facilityName = OutSiderow.createCell(4);
										facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell polutionExpDate = OutSiderow.createCell(5);
										polutionExpDate.setCellValue(dateFormat.format(vehicleDetails.getPolutionValid()));
									}												
								}									

							}
						}
					}
				}	
				break;
			case "insurancevalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){	
					Cell fifthColType = OutSiderow.createCell(5);
					fifthColType.setCellValue("Insurance Valid");
					fifthColType.setCellStyle(style);	
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());						
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getInsuranceValidDate().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("insuranceExpDate",dateFormat.format(vehicleDetails.getInsuranceValidDate()));
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vehicleNumber = OutSiderow.createCell(1);
									vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
									Cell vendorContactName = OutSiderow.createCell(2);
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(3);
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell facilityName = OutSiderow.createCell(4);
									facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell insuranceExpDate = OutSiderow.createCell(5);
									insuranceExpDate.setCellValue(dateFormat.format(vehicleDetails.getInsuranceValidDate()));
								}else{
									int diffInDays = (int) ((vehicleDetails.getInsuranceValidDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getInsuranceDueExpiryDay()){													
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("insuranceExpDate",dateFormat.format(vehicleDetails.getInsuranceValidDate()));
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

										dashBoardActivity.add(vehicleMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vehicleNumber = OutSiderow.createCell(1);
										vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
										Cell vendorContactName = OutSiderow.createCell(2);
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(3);
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell facilityName = OutSiderow.createCell(4);
										facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell insuranceExpDate = OutSiderow.createCell(5);
										insuranceExpDate.setCellValue(dateFormat.format(vehicleDetails.getInsuranceValidDate()));
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "taxvalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){	
					Cell fifthColType = OutSiderow.createCell(5);
					fifthColType.setCellValue("Tax Valid");
					fifthColType.setCellStyle(style);
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getTaxCertificateValid().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("taxExpDate",dateFormat.format(vehicleDetails.getTaxCertificateValid()));
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vehicleNumber = OutSiderow.createCell(1);
									vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
									Cell vendorContactName = OutSiderow.createCell(2);
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(3);
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell facilityName = OutSiderow.createCell(4);
									facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell taxExpDate = OutSiderow.createCell(5);
									taxExpDate.setCellValue(dateFormat.format(vehicleDetails.getTaxCertificateValid()));
									
								}else{
									int diffInDays = (int) ((vehicleDetails.getTaxCertificateValid().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getTaxCertificateExpiryDay()){													
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("taxExpDate",dateFormat.format(vehicleDetails.getTaxCertificateValid()));
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

										dashBoardActivity.add(vehicleMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vehicleNumber = OutSiderow.createCell(1);
										vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
										Cell vendorContactName = OutSiderow.createCell(2);
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(3);
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell facilityName = OutSiderow.createCell(4);
										facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell taxExpDate = OutSiderow.createCell(5);
										taxExpDate.setCellValue(dateFormat.format(vehicleDetails.getTaxCertificateValid()));
										
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "permitvalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){	
					Cell fifthColType = OutSiderow.createCell(5);
					fifthColType.setCellValue("Permit Valid");
					fifthColType.setCellStyle(style);
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){	
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getPermitValidDate().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("permitExpDate",dateFormat.format(vehicleDetails.getPermitValidDate()));
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vehicleNumber = OutSiderow.createCell(1);
									vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
									Cell vendorContactName = OutSiderow.createCell(2);
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(3);
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell facilityName = OutSiderow.createCell(4);
									facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell permitExpDate = OutSiderow.createCell(5);
									permitExpDate.setCellValue(dateFormat.format(vehicleDetails.getPermitValidDate()));
									
								}else{
									int diffInDays = (int) ((vehicleDetails.getPermitValidDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getPermitDueExpiryDay()){													
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("permitExpDate",dateFormat.format(vehicleDetails.getPermitValidDate()));
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

										dashBoardActivity.add(vehicleMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vehicleNumber = OutSiderow.createCell(1);
										vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
										Cell vendorContactName = OutSiderow.createCell(2);
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(3);
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell facilityName = OutSiderow.createCell(4);
										facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell permitExpDate = OutSiderow.createCell(5);
										permitExpDate.setCellValue(dateFormat.format(vehicleDetails.getPermitValidDate()));
										
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "vehiclemaintenancevalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){	
					Cell fifthColType = OutSiderow.createCell(5);
					fifthColType.setCellValue(" Vehicle Maintenance Valid");
					fifthColType.setCellStyle(style);
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){	
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getVehicleFitnessDate().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("vehicleManintenanceExpDate",dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
									OutSiderow = sheet.createRow(rownum++);	
									Cell vendorName = OutSiderow.createCell(0);
									vendorName.setCellValue(vendorList.getVendorName());
									Cell vehicleNumber = OutSiderow.createCell(1);
									vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
									Cell vendorContactName = OutSiderow.createCell(2);
									vendorContactName.setCellValue(vendorList.getVendorContactName());
									Cell vendorMobileNumber = OutSiderow.createCell(3);
									vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
									Cell facilityName = OutSiderow.createCell(4);
									facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									Cell vehicleManintenanceExpDate = OutSiderow.createCell(5);
									vehicleManintenanceExpDate.setCellValue(dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
									
								}else{
									int diffInDays = (int) ((vehicleDetails.getVehicleFitnessDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()){													
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("vehicleManintenanceExpDate",dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										
										dashBoardActivity.add(vehicleMaster);
										OutSiderow = sheet.createRow(rownum++);	
										Cell vendorName = OutSiderow.createCell(0);
										vendorName.setCellValue(vendorList.getVendorName());
										Cell vehicleNumber = OutSiderow.createCell(1);
										vehicleNumber.setCellValue(vehicleDetails.getVehicleNumber());
										Cell vendorContactName = OutSiderow.createCell(2);
										vendorContactName.setCellValue(vendorList.getVendorContactName());
										Cell vendorMobileNumber = OutSiderow.createCell(3);
										vendorMobileNumber.setCellValue(vendorList.getVendorMobileNo());
										Cell facilityName = OutSiderow.createCell(4);
										facilityName.setCellValue(vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										Cell vehicleManintenanceExpDate = OutSiderow.createCell(5);
										vehicleManintenanceExpDate.setCellValue(dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
										
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "breakdownsalerts":
				Date TodayDate=new Date();
				List<EFmFmTripAlertsPO> allAlerts=iAlertBO.getAllTripAlerts(travelRequestPO.getCombinedFacility());
				if(!(allAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlerts){
						int diffInDays = (int) ((alerts.getCreationTime().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
						if(diffInDays<30){	
							if(alerts.getEfmFmAlertTypeMaster().getAlertId()==9){											
								Map<String, Object>  alertsDetails= new HashMap<>();											
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								alertsDetails.put("userType", alerts.getUserType());
								alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
								alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
								alertsDetails.put("description", alerts.getEfmFmAlertTypeMaster().getAlertDescription());
								alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
								dashBoardActivity.add(alertsDetails);

							}
						}
					}
				}			
				break;
			}
			break;
		case "ALERTS":
			switch (activityType.toLowerCase()) {
			case "sos":	
				List<EFmFmTripAlertsPO> allAlerts=iAlertBO.getAllTodaysTripSOSAlerts(travelRequestPO.getCombinedFacility());
				if(!(allAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlerts){
						OutSiderow = sheet.createRow(rownum++);						
							Map<String, Object>  alertsDetails= new HashMap<>();
							alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
							Cell tittle = OutSiderow.createCell(6);							
							if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
								tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
							}else{
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle());
							}
							
							alertsDetails.put("userType", alerts.getUserType());
							Cell userType = OutSiderow.createCell(2);
							userType.setCellValue(alerts.getUserType());
							alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							Cell driverName = OutSiderow.createCell(3);	
							driverName.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
							Cell driverNumber = OutSiderow.createCell(4);
							driverNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
							alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							Cell vehicleNumber = OutSiderow.createCell(5);	
							vehicleNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
							alertsDetails.put("assignRouteId", alerts.getEfmFmAssignRoute().getAssignRouteId());
							alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
							Cell alertDate = OutSiderow.createCell(0);	
							alertDate.setCellValue(dateTimeFormat.format(alerts.getCreationTime()));
							
							alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
							Cell facilityName = OutSiderow.createCell(7);
							facilityName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
							
							alertsDetails.put("description", alerts.getAlertClosingDescription());
							Cell description = OutSiderow.createCell(8);
							description.setCellValue(alerts.getAlertClosingDescription());
							
							alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							Cell zoneName = OutSiderow.createCell(1);
							zoneName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							dashBoardActivity.add(alertsDetails);
					}
				}	
				break;
			case "roadalerts":	
				List<EFmFmTripAlertsPO> allAlert=iAlertBO.getAllTodaysTripRoadAlerts(travelRequestPO.getCombinedFacility());
				if(!(allAlert.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlert){
						OutSiderow = sheet.createRow(rownum++);	
						Map<String, Object>  alertsDetails= new HashMap<>();
						alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
						Cell tittle = OutSiderow.createCell(6);	
						if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
							tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());

						}else{
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
							tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle());
						}
						alertsDetails.put("userType", alerts.getUserType());
						Cell userType = OutSiderow.createCell(2);
						userType.setCellValue(alerts.getUserType());
						alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						Cell driverName = OutSiderow.createCell(3);	
						driverName.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						Cell driverNumber = OutSiderow.createCell(4);
						driverNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						Cell vehicleNumber = OutSiderow.createCell(5);	
						vehicleNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
						alertsDetails.put("assignRouteId", alerts.getEfmFmAssignRoute().getAssignRouteId());
						alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
						Cell alertDate = OutSiderow.createCell(0);	
						alertDate.setCellValue(dateTimeFormat.format(alerts.getCreationTime()));
						
						alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
						Cell facilityName = OutSiderow.createCell(7);
						facilityName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
						
						alertsDetails.put("description", alerts.getAlertClosingDescription());
						Cell description = OutSiderow.createCell(8);
						description.setCellValue(alerts.getAlertClosingDescription());
						
						alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						Cell zoneName = OutSiderow.createCell(1);
						zoneName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						dashBoardActivity.add(alertsDetails);
				}
				}		    
				break;
			case "openalerts":	
				List<EFmFmTripAlertsPO> allOpenAlert=iAlertBO.getAllTodaysTripOpenAlerts(travelRequestPO.getCombinedFacility());
				if(!(allOpenAlert.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allOpenAlert){
						OutSiderow = sheet.createRow(rownum++);	
						Cell tittle = OutSiderow.createCell(6);	
						Map<String, Object>  alertsDetails= new HashMap<>();
						alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
						if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
							tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
						}else{
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
							tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle());
						}
						alertsDetails.put("userType", alerts.getUserType());
						alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
						alertsDetails.put("assignRouteId", alerts.getEfmFmAssignRoute().getAssignRouteId());
						alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
						alertsDetails.put("description", alerts.getAlertClosingDescription());
						alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
						dashBoardActivity.add(alertsDetails);
						Cell userType = OutSiderow.createCell(2);
						userType.setCellValue(alerts.getUserType());						
						Cell driverName = OutSiderow.createCell(3);	
						driverName.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						
						Cell driverNumber = OutSiderow.createCell(4);
						driverNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						Cell vehicleNumber = OutSiderow.createCell(5);	
						vehicleNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						
						Cell alertDate = OutSiderow.createCell(0);	
						alertDate.setCellValue(dateTimeFormat.format(alerts.getCreationTime()));
						
						Cell facilityName = OutSiderow.createCell(7);
						facilityName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
						
						Cell description = OutSiderow.createCell(8);
						description.setCellValue(alerts.getAlertClosingDescription());
						
						Cell zoneName = OutSiderow.createCell(1);
						zoneName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				}
				}		    
				break;	
				case "closealerts":	
					List<EFmFmTripAlertsPO> allCloseAlerts=iAlertBO.getAllTodaysTripCloseAlerts(travelRequestPO.getCombinedFacility());
					log.info("all Alerts"+allCloseAlerts.size());
					if(!(allCloseAlerts.isEmpty())){
						for(EFmFmTripAlertsPO alerts:allCloseAlerts){
							OutSiderow = sheet.createRow(rownum++);	
							Map<String, Object>  alertsDetails= new HashMap<>();
							alertsDetails.put("tripAlertId", alerts.getTripAlertsId());							
							Cell tittle = OutSiderow.createCell(6);	
							if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
								tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());
							}else{
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								tittle.setCellValue(alerts.getEfmFmAlertTypeMaster().getAlertTitle());
							}		
							alertsDetails.put("userType", alerts.getUserType());
							alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
							alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
							alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
							alertsDetails.put("description", alerts.getAlertClosingDescription());
							alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
							dashBoardActivity.add(alertsDetails);
							Cell userType = OutSiderow.createCell(2);
							userType.setCellValue(alerts.getUserType());				
							Cell driverName = OutSiderow.createCell(3);	
							driverName.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							Cell driverNumber = OutSiderow.createCell(4);
							driverNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
							Cell vehicleNumber = OutSiderow.createCell(5);	
							vehicleNumber.setCellValue(alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							Cell alertDate = OutSiderow.createCell(0);	
							alertDate.setCellValue(dateTimeFormat.format(alerts.getCreationTime()));
							
							Cell facilityName = OutSiderow.createCell(7);
							facilityName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
							
							Cell description = OutSiderow.createCell(8);
							description.setCellValue(alerts.getAlertClosingDescription());
							
							Cell zoneName = OutSiderow.createCell(1);
							zoneName.setCellValue(alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					}
				}
				break;
			}
			break;

		}
		StreamingOutput streamOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream out) throws IOException, WebApplicationException {
				workbook.write(out);
			}
		};
		ResponseBuilder response = Response.ok(streamOutput,
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
		log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
		return response.build();
}
	

	@POST
	@Path("/getDashBoardDetailList")
	public Response getDashBoardDetailList(EFmFmEmployeeTravelRequestPO travelRequestPO) throws UnsupportedEncodingException{	    	
		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		
		}catch(Exception e){
			log.info("authentication error"+e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
		String tripType=travelRequestPO.getTripType();
		String activityType=travelRequestPO.getRequestStatus();	    	
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");	
		List<Map<String,Object>> dashBoardActivity = new ArrayList<>();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		DateFormat  dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		int redAlert=0,yellowAlert=0;
		if(tripType.toUpperCase().equalsIgnoreCase("EMPLOYEESTATUS")){
		}					
		switch (tripType.toUpperCase()) {			
		case "EMPLOYEESTATUS":	
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = requestDateFormat.format(new Date());
			switch (activityType.toLowerCase()) {						
			case "pickuprequest":	
				List<EFmFmEmployeeTravelRequestPO> employeesPickupRequestDetails=iCabRequestBO.getAllActivePickUpRequestDetailsForToday(travelRequestPO.getCombinedFacility(),currentDate);
				if(!(employeesPickupRequestDetails.isEmpty())){			
					for(EFmFmEmployeeTravelRequestPO allTravelRequest:employeesPickupRequestDetails){									
						Map<String, Object>  requestList= new HashMap<>();				
						requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(allTravelRequest.getRequestDate()));
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.getBranchName());
						requestList.put("employeeName", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
	                    requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
	                    if(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
						}
						else{
							requestList.put("gender", 2);	
						}
						dashBoardActivity.add(requestList);
					}									
				}						
				break;
			case "droprequest":	
				List<EFmFmEmployeeTravelRequestPO> employeesDropRequestDetails=iCabRequestBO.getAllActiveDropRequestDetailsForToday(travelRequestPO.getCombinedFacility(),currentDate);
				if(!(employeesDropRequestDetails.isEmpty())){			
					for(EFmFmEmployeeTravelRequestPO allTravelRequest:employeesDropRequestDetails){
						Map<String, Object>  requestList= new HashMap<>();		
						requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(allTravelRequest.getRequestDate()));
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
						}
						else{
							requestList.put("gender", 2);	
						}
						dashBoardActivity.add(requestList);
					}									
				}
				break;
			case "pickupschedule":
				List<EFmFmEmployeeTravelRequestPO> pickupScheduleEmployees=iCabRequestBO.getAllPickupScheduleActiveRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(pickupScheduleEmployees.isEmpty())){
					for(EFmFmEmployeeTravelRequestPO travelRequest:pickupScheduleEmployees){
						Map<String, Object>  requestList= new HashMap<>();											
						requestList.put("employeeId", travelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", travelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(travelRequest.getRequestDate()));
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(travelRequest.getShiftTime()));
						requestList.put("facilityName", travelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
						}else{
							requestList.put("gender", 2);	
						}
						dashBoardActivity.add(requestList);
					}	
				}
				break;
			case "dropschedule":									
				List<EFmFmEmployeeTravelRequestPO> dropScheduleEmployees=iCabRequestBO.getAllDropScheduleActiveRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(dropScheduleEmployees.isEmpty())){
					for(EFmFmEmployeeTravelRequestPO travelRequest:dropScheduleEmployees){
						Map<String, Object>  requestList= new HashMap<>();											
						requestList.put("employeeId", travelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("requestId", travelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(travelRequest.getRequestDate()));
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(travelRequest.getShiftTime()));
						requestList.put("facilityName", travelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
						}else{
							requestList.put("gender", 2);	
						}
						dashBoardActivity.add(requestList);
					}	
				}
				break;
			case "guestrequests":	
				List<EFmFmEmployeeTravelRequestPO> guestRequestDetails=iCabRequestBO.getAllActiveGuestRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(guestRequestDetails.isEmpty())){			
					for(EFmFmEmployeeTravelRequestPO allTravelRequest:guestRequestDetails){
						Map<String, Object>  requestList= new HashMap<>();		
						requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(allTravelRequest.getRequestDate()));
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
						}
						else{
							requestList.put("gender", 2);	
						}
						dashBoardActivity.add(requestList);
					}									
				}
				break;		
			case "guestschedulerequests":									
				List<EFmFmEmployeeTravelRequestPO> guestRequests=iCabRequestBO.getAllScheduleActiveGuestRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(guestRequests.isEmpty())){
					for(EFmFmEmployeeTravelRequestPO travelRequest:guestRequests){
						Map<String, Object>  requestList= new HashMap<>();											
						requestList.put("employeeId", travelRequest.getEfmFmUserMaster().getEmployeeId());
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("requestId", travelRequest.getRequestId());
						requestList.put("requestDate", dateFormat.format(travelRequest.getRequestDate()));
						requestList.put("tripType", travelRequest.getTripType());
						requestList.put("tripTime", timeFormat.format(travelRequest.getShiftTime()));
						requestList.put("facilityName", travelRequest.getBranchName());
                        requestList.put("employeeName", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
                        requestList.put("employeeAddress", new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(travelRequest.getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							requestList.put("gender", 1);	
						}else{
							requestList.put("gender", 2);	
						}
						dashBoardActivity.add(requestList);
					}	
				}
				break;
			case "guestboardedordropped":
				List<EFmFmEmployeeTripDetailPO> boardedOrDroppedRequestDetails=iCabRequestBO.getAllActiveBoardedOrDroppedEmployeeRequestsDetailsForGuest(travelRequestPO.getCombinedFacility());
				if(!(boardedOrDroppedRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:boardedOrDroppedRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripType", employeeDetails.geteFmFmEmployeeTravelRequest().getTripType());
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
						}
						else{
							activity.put("gender", 2); 
						}
						dashBoardActivity.add(activity);
					}
				}
				break;				
			case "guestnoshow":
				List<EFmFmEmployeeTripDetailPO> guestNoShowRequestDetails=iCabRequestBO.getAllActiveGuestNoShowRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(guestNoShowRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:guestNoShowRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripType", employeeDetails.geteFmFmEmployeeTravelRequest().getTripType());
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
						}
						else{
							activity.put("gender", 2); 
						}
						dashBoardActivity.add(activity);
					}
				}
				break;	
				
			case "pickupboarded":
				List<EFmFmEmployeeTripDetailPO> pickupBoardedRequestDetails=iCabRequestBO.getAllActivePickupBoardedEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(pickupBoardedRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:pickupBoardedRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
						}
						else{
							activity.put("gender", 2); 
						}
						dashBoardActivity.add(activity);
					}
				}
				break;
			case "dropboarded":										
				List<EFmFmEmployeeTripDetailPO> dropBoardedRequestDetails=iCabRequestBO.getAllActiveDropedEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(dropBoardedRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:dropBoardedRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
						}
						else{
							activity.put("gender", 2); 
						}
						dashBoardActivity.add(activity);
					}
				}
				break;
			case "noshowpickup":
				List<EFmFmEmployeeTripDetailPO> noShowPickupRequestDetails=iCabRequestBO.getAllActivePickupNoShowEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(noShowPickupRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:noShowPickupRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
						}
						else{
							activity.put("gender", 2); 
						}
						dashBoardActivity.add(activity);
					}
				}
				break;							   
			case "noshowdrop":	
				List<EFmFmEmployeeTripDetailPO> noShowDropRequestDetails=iCabRequestBO.getAllActiveDropNoShowEmployeeRequestsDetails(travelRequestPO.getCombinedFacility());
				if(!(noShowDropRequestDetails.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetails:noShowDropRequestDetails){
						Map<String,Object> activity=new HashMap<>();
						activity.put("requestId", employeeDetails.geteFmFmEmployeeTravelRequest().getRequestId());
						activity.put("requestDate", dateFormat.format(employeeDetails.geteFmFmEmployeeTravelRequest().getRequestDate()));
						activity.put("tripTime", employeeDetails.geteFmFmEmployeeTravelRequest().getShiftTime());
						activity.put("employeeId", employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						activity.put("facilityName", employeeDetails.geteFmFmEmployeeTravelRequest().getBranchName());
                        activity.put("employeeName", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
                        activity.put("employeeAddress", new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()), "utf-8"));
                        if(new String(Base64.getDecoder().decode(employeeDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8").equalsIgnoreCase("Male")){
							activity.put("gender", 1); 
						}
						else{
							activity.put("gender", 2); 
						}
						dashBoardActivity.add(activity);
					}
				}
				break;		
			}
			break;
		case "VEHICLESTATUS":
			break;
		case "GOVERNANCEDRIVERS":		
			List<EFmFmVendorMasterPO> listOfVendorByDriver=iVendorDetailsBO.getAllEnableVendorsDetails(travelRequestPO.getCombinedFacility());
			switch (activityType.toLowerCase()) {	
			case "policevarificationexp":																			
				if(!(listOfVendorByDriver.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getPoliceVerificationValid().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("colorFlg","R");
									driverMaster.put("policeExpDate",dateFormat.format(driverDetails.getPoliceVerificationValid()));
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
								}else{
									int diffInDays = (int) ((driverDetails.getPoliceVerificationValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()){
										redAlert=vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());													
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										if(diffInDays<=redAlert){
											driverMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											driverMaster.put("colorFlg","Y");
										}else{
											driverMaster.put("colorFlg","G");
										}
										driverMaster.put("policeExpDate",dateFormat.format(driverDetails.getPoliceVerificationValid()));
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
									}												
								}											

							}
						}							
					}
				}	
				break;
			case "licenseexpire":																			
				if(!(listOfVendorByDriver.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getLicenceValid().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("colorFlg","R");
									driverMaster.put("licenceExpDate",dateFormat.format(driverDetails.getLicenceValid()));
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
								}else{
									int diffInDays = (int) ((driverDetails.getLicenceValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getLicenseExpiryDay()){
										redAlert=vendorList.geteFmFmClientBranchPO().getLicenseExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());													
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("licenceExpDate",dateFormat.format(driverDetails.getLicenceValid()));
										if(diffInDays<=redAlert){
											driverMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											driverMaster.put("colorFlg","Y");
										}else{
											driverMaster.put("colorFlg","G");
										}
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
									}												
								}											

							}
						}							
					}
				}	
				break;
			case "ddtrainingexp":																			
				if(!(listOfVendorByDriver.isEmpty())){	
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){	
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());						
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getDdtValidDate().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("ddExpDate",dateFormat.format(driverDetails.getDdtValidDate()));
									driverMaster.put("colorFlg","R");
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
								}else{
									int diffInDays = (int) ((driverDetails.getDdtValidDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getDdTrainingExpiryDay()){	
										redAlert=vendorList.geteFmFmClientBranchPO().getDdTrainingExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());													
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("ddExpDate",dateFormat.format(driverDetails.getDdtValidDate()));
										if(diffInDays<=redAlert){
											driverMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											driverMaster.put("colorFlg","Y");
										}else{
											driverMaster.put("colorFlg","G");
										}
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
									}												
								}											

							}
						}							
					}
				}	
				break;
			case "medicalexpire":																			
				if(!(listOfVendorByDriver.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){	
						List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllActiveDriverDetails(vendorList.getVendorId());						
						for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
							Map<String,Object> driverMaster=new HashMap<>();
							Date TodayDate=new Date();
							if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
								if(driverDetails.getMedicalFitnessCertificateValid().before(TodayDate)){
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("medicalExpDate",dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
									driverMaster.put("colorFlg","R");
									driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(driverMaster);
								}else{
									int diffInDays = (int) ((driverDetails.getMedicalFitnessCertificateValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getMedicalFitnessExpiryDay()){
										redAlert=vendorList.geteFmFmClientBranchPO().getMedicalFitnessExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										driverMaster.put("driverId",driverDetails.getDriverId());
										driverMaster.put("driverName",driverDetails.getFirstName());
										driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
										driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
										driverMaster.put("vendorId",vendorList.getVendorId());
										driverMaster.put("vendorName",vendorList.getVendorName());
										driverMaster.put("vendorContactName",vendorList.getVendorContactName());
										driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										driverMaster.put("medicalExpDate",dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
										if(diffInDays<=redAlert){
											driverMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											driverMaster.put("colorFlg","Y");
										}else{
											driverMaster.put("colorFlg","G");
										}
										driverMaster.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(driverMaster);
									}												
								}											

							}
						}
					}
				}	
				break;
			case "accidentalerts":
				Date TodayDate=new Date();
				List<EFmFmTripAlertsPO> allAlerts=iAlertBO.getAllTripAlerts(travelRequestPO.getCombinedFacility());
				if (!(allAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlerts){
						int diffInDays = (int) ((alerts.getCreationTime().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
						if(diffInDays<30){
							if(alerts.getEfmFmAlertTypeMaster().getAlertId()==8){											
								Map<String, Object>  alertsDetails= new HashMap<>();											
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								alertsDetails.put("userType", alerts.getUserType());
								alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());											
								alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
								alertsDetails.put("description", alerts.getEfmFmAlertTypeMaster().getAlertDescription());
								alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
								dashBoardActivity.add(alertsDetails);
							}
						}
					}
				}			
				break;
			case "speedalerts":
				Date todayDate=new Date();
				List<EFmFmTripAlertsPO> allSpeedAlerts=iAlertBO.getAllTripAlerts(travelRequestPO.getCombinedFacility());
				if(!(allSpeedAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allSpeedAlerts){
						int diffInDays = (int) ((alerts.getCreationTime().getTime() -todayDate.getTime()) / (1000 * 60 * 60 * 24));
						if(diffInDays<30){	
							if(alerts.getEfmFmAlertTypeMaster().getAlertId()==10){
								Map<String, Object>  alertsDetails= new HashMap<>();
								if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("")){
									alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is"+alerts.getCurrentSpeed());

								}else{
									alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								}
								alertsDetails.put("userType", alerts.getUserType());
								alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
								alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
								alertsDetails.put("currentSpeed", alerts.getCurrentSpeed());
								alertsDetails.put("description", alerts.getEfmFmAlertTypeMaster().getAlertDescription());
								alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								alertsDetails.put("facilityName", alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
								dashBoardActivity.add(alertsDetails);
							}
						}
					}
				}			
				break;	
			}
			break;
		case "GOVERNCEVEHICLES":						
			List<EFmFmVendorMasterPO> listOfVendorByVehicle=iVendorDetailsBO.getAllEnableVendorsDetails(travelRequestPO.getCombinedFacility());
			switch (activityType.toLowerCase()) {							
			case "polutionexpire":																			
				if(!(listOfVendorByVehicle.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getPolutionValid().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("polutionExpDate",dateFormat.format(vehicleDetails.getPolutionValid()));
									vehicleMaster.put("colorFlg","R");
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
									dashBoardActivity.add(vehicleMaster);
								}else{
									int diffInDays = (int) ((vehicleDetails.getPolutionValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getPollutionDueExpiryDay()){	
										redAlert=vendorList.geteFmFmClientBranchPO().getPollutionDueExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("polutionExpDate",dateFormat.format(vehicleDetails.getPolutionValid()));
																				
										if(diffInDays<=redAlert){
											vehicleMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											vehicleMaster.put("colorFlg","Y");
										}else{
											vehicleMaster.put("colorFlg","G");
										}
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(vehicleMaster);
									}												
								}									

							}
						}
					}
				}	
				break;
			case "insurancevalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());

						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getInsuranceValidDate().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("insuranceExpDate",dateFormat.format(vehicleDetails.getInsuranceValidDate()));
									vehicleMaster.put("colorFlg","R");
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
								}else{
									int diffInDays = (int) ((vehicleDetails.getInsuranceValidDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getInsuranceDueExpiryDay()){	
										redAlert=vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("insuranceExpDate",dateFormat.format(vehicleDetails.getInsuranceValidDate()));
										if(diffInDays<=redAlert){
											vehicleMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											vehicleMaster.put("colorFlg","Y");
										}else{
											vehicleMaster.put("colorFlg","G");
										}
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(vehicleMaster);
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "taxvalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getTaxCertificateValid().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("taxExpDate",dateFormat.format(vehicleDetails.getTaxCertificateValid()));
									vehicleMaster.put("colorFlg","R");
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
								}else{
									int diffInDays = (int) ((vehicleDetails.getTaxCertificateValid().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getTaxCertificateExpiryDay()){
										redAlert=vendorList.geteFmFmClientBranchPO().getTaxCertificateExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("taxExpDate",dateFormat.format(vehicleDetails.getTaxCertificateValid()));
										if(diffInDays<=redAlert){
											vehicleMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											vehicleMaster.put("colorFlg","Y");
										}else{
											vehicleMaster.put("colorFlg","G");
										}
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

										dashBoardActivity.add(vehicleMaster);
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "permitvalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){	
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());

						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){											
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getPermitValidDate().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("permitExpDate",dateFormat.format(vehicleDetails.getPermitValidDate()));
									vehicleMaster.put("colorFlg","R");
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
								}else{
									int diffInDays = (int) ((vehicleDetails.getPermitValidDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getPermitDueExpiryDay()){	
										redAlert=vendorList.geteFmFmClientBranchPO().getPermitDueExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("permitExpDate",dateFormat.format(vehicleDetails.getPermitValidDate()));
										if(diffInDays<=redAlert){
											vehicleMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											vehicleMaster.put("colorFlg","Y");
										}else{
											vehicleMaster.put("colorFlg","G");
										}
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(vehicleMaster);
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "vehiclemaintenancevalid":																			
				if(!(listOfVendorByVehicle.isEmpty())){													
					for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){	
						List<EFmFmVehicleMasterPO> listOfVehicle=iVendorDetailsBO.getAllActiveVehicleDetails(vendorList.getVendorId());
						for(EFmFmVehicleMasterPO vehicleDetails:listOfVehicle){	
							Map<String,Object> vehicleMaster=new HashMap<>();
							Date TodayDate=new Date();	
							if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
								if(vehicleDetails.getVehicleFitnessDate().before(TodayDate)){												
									vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
									vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
									vehicleMaster.put("vendorId",vendorList.getVendorId());
									vehicleMaster.put("vendorName",vendorList.getVendorName());
									vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
									vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									vehicleMaster.put("vehicleManintenanceExpDate",dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
									vehicleMaster.put("colorFlg","R");
									vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

									dashBoardActivity.add(vehicleMaster);
								}else{
									int diffInDays = (int) ((vehicleDetails.getVehicleFitnessDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
									if(diffInDays<vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()){	
										redAlert=vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()/3;
										yellowAlert=redAlert+redAlert;
										vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
										vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
										vehicleMaster.put("vendorId",vendorList.getVendorId());
										vehicleMaster.put("vendorName",vendorList.getVendorName());
										vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
										vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
										vehicleMaster.put("vehicleManintenanceExpDate",dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
										if(diffInDays<=redAlert){
											vehicleMaster.put("colorFlg","R");
										}else if(diffInDays>redAlert && diffInDays <=yellowAlert){
											vehicleMaster.put("colorFlg","Y");
										}else{
											vehicleMaster.put("colorFlg","G");
										}
										vehicleMaster.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
										dashBoardActivity.add(vehicleMaster);
									}												
								}									

							}
						}	

					}
				}	
				break;
			case "breakdownsalerts":
				Date TodayDate=new Date();
				List<EFmFmTripAlertsPO> allAlerts=iAlertBO.getAllTripAlerts(travelRequestPO.getCombinedFacility());
				if(!(allAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlerts){
						int diffInDays = (int) ((alerts.getCreationTime().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
						if(diffInDays<30){	
							if(alerts.getEfmFmAlertTypeMaster().getAlertId()==9){											
								Map<String, Object>  alertsDetails= new HashMap<>();											
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
								alertsDetails.put("userType", alerts.getUserType());
								alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
								alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
								alertsDetails.put("description", alerts.getEfmFmAlertTypeMaster().getAlertDescription());
								alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								alertsDetails.put("facilityName",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
								dashBoardActivity.add(alertsDetails);

							}
						}
					}
				}			
				break;
			}
			break;
		case "ALERTS":
			switch (activityType.toLowerCase()) {
			case "sos":	
				List<EFmFmTripAlertsPO> allAlerts=iAlertBO.getAllTodaysTripSOSAlerts(travelRequestPO.getCombinedFacility());
				if(!(allAlerts.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlerts){
							Map<String, Object>  alertsDetails= new HashMap<>();
							alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
							if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());

							}else{
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
							}
							
							alertsDetails.put("userType", alerts.getUserType());
							alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
							alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
							alertsDetails.put("assignRouteId", alerts.getEfmFmAssignRoute().getAssignRouteId());
							alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
							alertsDetails.put("description", alerts.getAlertClosingDescription());
							alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							alertsDetails.put("facilityName",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
							dashBoardActivity.add(alertsDetails);
					}
				}	
				break;
			case "roadalerts":	
				List<EFmFmTripAlertsPO> allAlert=iAlertBO.getAllTodaysTripRoadAlerts(travelRequestPO.getCombinedFacility());
				if(!(allAlert.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allAlert){
						Map<String, Object>  alertsDetails= new HashMap<>();
						alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
						if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());

						}else{
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
						}
						alertsDetails.put("userType", alerts.getUserType());
						alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
						alertsDetails.put("assignRouteId", alerts.getEfmFmAssignRoute().getAssignRouteId());
						alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
						alertsDetails.put("description", alerts.getAlertClosingDescription());
						alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						alertsDetails.put("facilityName",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
						dashBoardActivity.add(alertsDetails);
				}
				}		    
				break;
			case "openalerts":	
				List<EFmFmTripAlertsPO> allOpenAlert=iAlertBO.getAllTodaysTripOpenAlerts(travelRequestPO.getCombinedFacility());
				if(!(allOpenAlert.isEmpty())){
					for(EFmFmTripAlertsPO alerts:allOpenAlert){
						Map<String, Object>  alertsDetails= new HashMap<>();
						alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
						if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());

						}else{
							alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
						}
						alertsDetails.put("userType", alerts.getUserType());
						alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
						alertsDetails.put("assignRouteId", alerts.getEfmFmAssignRoute().getAssignRouteId());
						alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
						alertsDetails.put("description", alerts.getAlertClosingDescription());
						alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						alertsDetails.put("facilityName",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
						alertsDetails.put("facilityId",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId());
						dashBoardActivity.add(alertsDetails);
				}
				}		    
				break;	
				case "closealerts":	
					List<EFmFmTripAlertsPO> allCloseAlerts=iAlertBO.getAllTodaysTripCloseAlerts(travelRequestPO.getCombinedFacility());
					log.info("all Alerts"+allCloseAlerts.size());
					if(!(allCloseAlerts.isEmpty())){
						for(EFmFmTripAlertsPO alerts:allCloseAlerts){
							Map<String, Object>  alertsDetails= new HashMap<>();
							alertsDetails.put("tripAlertId", alerts.getTripAlertsId());
							if(alerts.getEfmFmAlertTypeMaster().getAlertTitle().equalsIgnoreCase("over speed")){
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle()+"-Limit is-"+alerts.getCurrentSpeed());

							}else{
								alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
							}
							
							
							alertsDetails.put("userType", alerts.getUserType());
							alertsDetails.put("driverName", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							alertsDetails.put("driverNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
							alertsDetails.put("vehicleNumber", alerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							alertsDetails.put("alertType", alerts.getEfmFmAlertTypeMaster().getAlertType());
							alertsDetails.put("alertDate", dateTimeFormat.format(alerts.getCreationTime()));
							alertsDetails.put("description", alerts.getAlertClosingDescription());
							alertsDetails.put("zoneName", alerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							alertsDetails.put("facilityName",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchName());
							alertsDetails.put("facilityId",  alerts.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId());
							dashBoardActivity.add(alertsDetails);
					}
					}		    
					break;
			}
			break;

		}	
		 log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
		return Response.ok(dashBoardActivity, MediaType.APPLICATION_JSON).build();		
	}
	
	
	/**
	* get the driverComplainceExcel.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2016-21-March
	* 
	* @return EFmFmTripAlertsPO details.
	* 
	* @throws ParseException 
	*/	
	
	@POST
	@Path("/driverComplainceExcel")
	public Response getAllComplaiceExcel(EFmFmVendorMasterPO efmFmVendorMaster) throws IOException{
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> requests = new HashMap<>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),efmFmVendorMaster.getUserId()))){

			requests.put("status", "invalidRequest");
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(efmFmVendorMaster.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
			
		
		}catch(Exception e){
			log.info("authentication error"+e);
			requests.put("status", "invalidRequest");
		}
		
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		log.info("serviceStart -UserId :" + efmFmVendorMaster.getUserId());
		List<Map<String,Object>> policeExp = new ArrayList<>();
		List<Map<String,Object>> licenceExp = new ArrayList<>();
		List<Map<String,Object>> ddtExp = new ArrayList<>();
		List<Map<String,Object>> medicalExp = new ArrayList<>();

		List<EFmFmVendorMasterPO> listOfVendorByDriver=iVendorDetailsBO.getAllVendorsDetails(efmFmVendorMaster);		
			if(!(listOfVendorByDriver.isEmpty())){													
				for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){										
					EFmFmDriverMasterPO eFmFmDriverMasterPO=new EFmFmDriverMasterPO();
					efmFmVendorMaster.setVendorId(vendorList.getVendorId());
					eFmFmDriverMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
					List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllDriverDetails(eFmFmDriverMasterPO);		
					for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
						Map<String,Object> driverMaster=new HashMap<>();
						Date TodayDate=new Date();
						if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
							if(driverDetails.getPoliceVerificationValid().before(TodayDate)){
								driverMaster.put("driverId",driverDetails.getDriverId());
								driverMaster.put("driverName",driverDetails.getFirstName());
								driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
								driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
								driverMaster.put("vendorId",vendorList.getVendorId());
								driverMaster.put("vendorName",vendorList.getVendorName());
								driverMaster.put("vendorContactName",vendorList.getVendorContactName());
								driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								driverMaster.put("policeExpDate",dateFormat.format(driverDetails.getPoliceVerificationValid()));
								policeExp.add(driverMaster);
							}else{
								int diffInDays = (int) ((driverDetails.getPoliceVerificationValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(diffInDays<vendorList.geteFmFmClientBranchPO().getPoliceVerificationExpiryDay()){													
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());													
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("policeExpDate",dateFormat.format(driverDetails.getPoliceVerificationValid()));
									policeExp.add(driverMaster);
								}												
							}											

						}
					}							
				}
			}	
			if(!(listOfVendorByDriver.isEmpty())){													
				for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){										
					EFmFmDriverMasterPO eFmFmDriverMasterPO=new EFmFmDriverMasterPO();
					efmFmVendorMaster.setVendorId(vendorList.getVendorId());
					eFmFmDriverMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
					List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllDriverDetails(eFmFmDriverMasterPO);		
					for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
						Map<String,Object> driverMaster=new HashMap<>();
						Date TodayDate=new Date();
						if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
							if(driverDetails.getLicenceValid().before(TodayDate)){
								driverMaster.put("driverId",driverDetails.getDriverId());
								driverMaster.put("driverName",driverDetails.getFirstName());
								driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
								driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
								driverMaster.put("vendorId",vendorList.getVendorId());
								driverMaster.put("vendorName",vendorList.getVendorName());
								driverMaster.put("vendorContactName",vendorList.getVendorContactName());
								driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								driverMaster.put("licenceExpDate",dateFormat.format(driverDetails.getLicenceValid()));
								licenceExp.add(driverMaster);
							}else{
								int diffInDays = (int) ((driverDetails.getLicenceValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(diffInDays<vendorList.geteFmFmClientBranchPO().getLicenseExpiryDay()){													
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());													
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("licenceExpDate",dateFormat.format(driverDetails.getLicenceValid()));
									licenceExp.add(driverMaster);
								}												
							}											

						}
					}							
				}
			}	
			if(!(listOfVendorByDriver.isEmpty())){	
				for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){										
					EFmFmDriverMasterPO eFmFmDriverMasterPO=new EFmFmDriverMasterPO();
					efmFmVendorMaster.setVendorId(vendorList.getVendorId());
					eFmFmDriverMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
					List<EFmFmDriverMasterPO> listOfDriver=iVehicleCheckInBO.getAllDriverDetails(eFmFmDriverMasterPO);	
					for(EFmFmDriverMasterPO driverDetails:listOfDriver){											
						Map<String,Object> driverMaster=new HashMap<>();
						Date TodayDate=new Date();
						if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
							if(driverDetails.getDdtValidDate().before(TodayDate)){
								driverMaster.put("driverId",driverDetails.getDriverId());
								driverMaster.put("driverName",driverDetails.getFirstName());
								driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
								driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
								driverMaster.put("vendorId",vendorList.getVendorId());
								driverMaster.put("vendorName",vendorList.getVendorName());
								driverMaster.put("vendorContactName",vendorList.getVendorContactName());
								driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								driverMaster.put("ddExpDate",dateFormat.format(driverDetails.getDdtValidDate()));
								ddtExp.add(driverMaster);
							}else{
								int diffInDays = (int) ((driverDetails.getDdtValidDate().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(diffInDays<vendorList.geteFmFmClientBranchPO().getDdTrainingExpiryDay()){													
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());													
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("ddExpDate",dateFormat.format(driverDetails.getDdtValidDate()));
									ddtExp.add(driverMaster);
								}												
							}											

						}
					}							
				}
			}	
			if(!(listOfVendorByDriver.isEmpty())){													
				for(EFmFmVendorMasterPO vendorList:listOfVendorByDriver){	
					EFmFmDriverMasterPO eFmFmDriverMasterPO=new EFmFmDriverMasterPO();
					efmFmVendorMaster.setVendorId(vendorList.getVendorId());
					eFmFmDriverMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
					List<EFmFmDriverMasterPO> listOfVehicle=iVehicleCheckInBO.getAllDriverDetails(eFmFmDriverMasterPO);	
					for(EFmFmDriverMasterPO driverDetails:listOfVehicle){											
						Map<String,Object> driverMaster=new HashMap<>();
						Date TodayDate=new Date();
						if(driverDetails.getStatus().equalsIgnoreCase("A") || driverDetails.getStatus().equalsIgnoreCase("allocated")){
							if(driverDetails.getMedicalFitnessCertificateValid().before(TodayDate)){
								driverMaster.put("driverId",driverDetails.getDriverId());
								driverMaster.put("driverName",driverDetails.getFirstName());
								driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
								driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
								driverMaster.put("vendorId",vendorList.getVendorId());
								driverMaster.put("vendorName",vendorList.getVendorName());
								driverMaster.put("vendorContactName",vendorList.getVendorContactName());
								driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								driverMaster.put("medicalExpDate",dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));

								medicalExp.add(driverMaster);
							}else{
								int diffInDays = (int) ((driverDetails.getMedicalFitnessCertificateValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
								if(diffInDays<vendorList.geteFmFmClientBranchPO().getMedicalFitnessExpiryDay()){													
									driverMaster.put("driverId",driverDetails.getDriverId());
									driverMaster.put("driverName",driverDetails.getFirstName());
									driverMaster.put("mobileNumber",driverDetails.getMobileNumber());
									driverMaster.put("licenceNumber",driverDetails.getLicenceNumber());	
									driverMaster.put("vendorId",vendorList.getVendorId());
									driverMaster.put("vendorName",vendorList.getVendorName());
									driverMaster.put("vendorContactName",vendorList.getVendorContactName());
									driverMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
									driverMaster.put("medicalExpDate",dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
									medicalExp.add(driverMaster);
								}												
							}											

						}
					}
				}
			}

			requests.put("policeExp",policeExp);	
			requests.put("licenceExp",licenceExp);					
			requests.put("ddtExp",ddtExp);					
			requests.put("medicalExp",medicalExp);					
		    requests.put("status", "success");	
		    log.info("serviceEnd -UserId :" + efmFmVendorMaster.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	* get the vehicleComplainceExcel.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2016-21-March
	* 
	* @return EFmFmTripAlertsPO details.
	* 
	* @throws ParseException 
	*/	
	
	@POST
	@Path("/vehicleComplainceExcel")
	public Response getAllVehicleComplaiceExcel(EFmFmVendorMasterPO efmFmVendorMaster) throws IOException{
		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		
		List<EFmFmClientBranchPO> clientBranch = userMasterBO.getClientDetails(String.valueOf(efmFmVendorMaster.getBranchId()));
		Map<String, Object> requests = new HashMap<String, Object>();
	
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),efmFmVendorMaster.getUserId()))){

			requests.put("status", "invalidRequest");
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(efmFmVendorMaster.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		
		}catch(Exception e){
			log.info("authentication error"+e);
			requests.put("status", "invalidRequest");
		}

		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		log.info("serviceStart -UserId :" + efmFmVendorMaster.getUserId());
		List<EFmFmVendorMasterPO> listOfVendorByVehicle=iVendorDetailsBO.getAllVendorsDetails(efmFmVendorMaster);
		List<Map<String,Object>> poluttionList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> insuranceExp = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> taxExp = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> permitExp = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> vehicleManintenanceExp = new ArrayList<Map<String,Object>>();

		if(!(listOfVendorByVehicle.isEmpty())){													
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
				EFmFmVehicleMasterPO eFmFmVehicleMasterPO=new EFmFmVehicleMasterPO();
				efmFmVendorMaster.setVendorId(vendorList.getVendorId());
				eFmFmVehicleMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
				List<EFmFmVehicleMasterPO> listOfDriver=iVehicleCheckInBO.getAllVehicleDetails(eFmFmVehicleMasterPO);	
				for(EFmFmVehicleMasterPO vehicleDetails:listOfDriver){											
					Map<String,Object> vehicleMaster=new HashMap<>();
					Date TodayDate=new Date();	
					if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
						if(vehicleDetails.getPolutionValid().before(TodayDate)){												
							vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
							vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
							vehicleMaster.put("vendorId",vendorList.getVendorId());
							vehicleMaster.put("vendorName",vendorList.getVendorName());
							vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
							vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
							vehicleMaster.put("polutionExpDate",dateFormat.format(vehicleDetails.getPolutionValid()));
							poluttionList.add(vehicleMaster);
						}else{
							int diffInDays = (int) ((vehicleDetails.getPolutionValid().getTime() -TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getPollutionDueExpiryDay()){													
								vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
								vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
								vehicleMaster.put("vendorId",vendorList.getVendorId());
								vehicleMaster.put("vendorName",vendorList.getVendorName());
								vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
								vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								vehicleMaster.put("polutionExpDate",dateFormat.format(vehicleDetails.getPolutionValid()));
								poluttionList.add(vehicleMaster);
							}												
						}									

					}
				}
			}
		}	
		if(!(listOfVendorByVehicle.isEmpty())){													
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
				EFmFmVehicleMasterPO eFmFmVehicleMasterPO=new EFmFmVehicleMasterPO();
				efmFmVendorMaster.setVendorId(vendorList.getVendorId());
				eFmFmVehicleMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
				List<EFmFmVehicleMasterPO> listOfDriver=iVehicleCheckInBO.getAllVehicleDetails(eFmFmVehicleMasterPO);						
				for(EFmFmVehicleMasterPO vehicleDetails:listOfDriver){											
					Map<String,Object> vehicleMaster=new HashMap<>();
					Date TodayDate=new Date();	
					if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
						if(vehicleDetails.getInsuranceValidDate().before(TodayDate)){												
							vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
							vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
							vehicleMaster.put("vendorId",vendorList.getVendorId());
							vehicleMaster.put("vendorName",vendorList.getVendorName());
							vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
							vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
							vehicleMaster.put("insuranceExpDate",dateFormat.format(vehicleDetails.getInsuranceValidDate()));

							insuranceExp.add(vehicleMaster);
						}else{
							int diffInDays = (int) ((vehicleDetails.getInsuranceValidDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getInsuranceDueExpiryDay()){													
								vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
								vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
								vehicleMaster.put("vendorId",vendorList.getVendorId());
								vehicleMaster.put("vendorName",vendorList.getVendorName());
								vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
								vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								vehicleMaster.put("insuranceExpDate",dateFormat.format(vehicleDetails.getInsuranceValidDate()));

								insuranceExp.add(vehicleMaster);
							}												
						}									

					}
				}	

			}
		}	
		if(!(listOfVendorByVehicle.isEmpty())){													
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
				EFmFmVehicleMasterPO eFmFmVehicleMasterPO=new EFmFmVehicleMasterPO();
				efmFmVendorMaster.setVendorId(vendorList.getVendorId());
				eFmFmVehicleMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
				List<EFmFmVehicleMasterPO> listOfDriver=iVehicleCheckInBO.getAllVehicleDetails(eFmFmVehicleMasterPO);						
				for(EFmFmVehicleMasterPO vehicleDetails:listOfDriver){											
					Map<String,Object> vehicleMaster=new HashMap<>();
					Date TodayDate=new Date();	
					if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
						if(vehicleDetails.getTaxCertificateValid().before(TodayDate)){												
							vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
							vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
							vehicleMaster.put("vendorId",vendorList.getVendorId());
							vehicleMaster.put("vendorName",vendorList.getVendorName());
							vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
							vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
							vehicleMaster.put("taxExpDate",dateFormat.format(vehicleDetails.getTaxCertificateValid()));

							taxExp.add(vehicleMaster);
						}else{
							int diffInDays = (int) ((vehicleDetails.getTaxCertificateValid().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getTaxCertificateExpiryDay()){													
								vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
								vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
								vehicleMaster.put("vendorId",vendorList.getVendorId());
								vehicleMaster.put("vendorName",vendorList.getVendorName());
								vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
								vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								vehicleMaster.put("taxExpDate",dateFormat.format(vehicleDetails.getTaxCertificateValid()));

								taxExp.add(vehicleMaster);
							}												
						}									

					}
				}	

			}
		}	
		if(!(listOfVendorByVehicle.isEmpty())){													
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
				EFmFmVehicleMasterPO eFmFmVehicleMasterPO=new EFmFmVehicleMasterPO();
				efmFmVendorMaster.setVendorId(vendorList.getVendorId());
				eFmFmVehicleMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
				List<EFmFmVehicleMasterPO> listOfDriver=iVehicleCheckInBO.getAllVehicleDetails(eFmFmVehicleMasterPO);						
				for(EFmFmVehicleMasterPO vehicleDetails:listOfDriver){											
					Map<String,Object> vehicleMaster=new HashMap<>();
					Date TodayDate=new Date();	
					if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
						if(vehicleDetails.getPermitValidDate().before(TodayDate)){												
							vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
							vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
							vehicleMaster.put("vendorId",vendorList.getVendorId());
							vehicleMaster.put("vendorName",vendorList.getVendorName());
							vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
							vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
							vehicleMaster.put("permitExpDate",dateFormat.format(vehicleDetails.getPermitValidDate()));

							permitExp.add(vehicleMaster);
						}else{
							int diffInDays = (int) ((vehicleDetails.getPermitValidDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getPermitDueExpiryDay()){													
								vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
								vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
								vehicleMaster.put("vendorId",vendorList.getVendorId());
								vehicleMaster.put("vendorName",vendorList.getVendorName());
								vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
								vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								vehicleMaster.put("permitExpDate",dateFormat.format(vehicleDetails.getPermitValidDate()));

								permitExp.add(vehicleMaster);
							}												
						}									

					}
				}	

			}
		}	
		if(!(listOfVendorByVehicle.isEmpty())){													
			for(EFmFmVendorMasterPO vendorList:listOfVendorByVehicle){				
				EFmFmVehicleMasterPO eFmFmVehicleMasterPO=new EFmFmVehicleMasterPO();
				efmFmVendorMaster.setVendorId(vendorList.getVendorId());
				eFmFmVehicleMasterPO.setEfmFmVendorMaster(efmFmVendorMaster);
				List<EFmFmVehicleMasterPO> listOfDriver=iVehicleCheckInBO.getAllVehicleDetails(eFmFmVehicleMasterPO);						
				for(EFmFmVehicleMasterPO vehicleDetails:listOfDriver){											
					Map<String,Object> vehicleMaster=new HashMap<>();
					Date TodayDate=new Date();	
					if(vehicleDetails.getStatus().equalsIgnoreCase("A") || vehicleDetails.getStatus().equalsIgnoreCase("allocated")){												
						if(vehicleDetails.getVehicleFitnessDate().before(TodayDate)){												
							vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());			
							vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
							vehicleMaster.put("vendorId",vendorList.getVendorId());
							vehicleMaster.put("vendorName",vendorList.getVendorName());
							vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
							vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
							vehicleMaster.put("vehicleManintenanceExpDate",dateFormat.format(vehicleDetails.getVehicleFitnessDate()));

							vehicleManintenanceExp.add(vehicleMaster);
						}else{
							int diffInDays = (int) ((vehicleDetails.getVehicleFitnessDate().getTime()-TodayDate.getTime()) / (1000 * 60 * 60 * 24));
							if(diffInDays<vendorList.geteFmFmClientBranchPO().getVehicelMaintenanceExpiryDay()){													
								vehicleMaster.put("vehicleId",vehicleDetails.getVehicleId());
								vehicleMaster.put("vehicleNumber",vehicleDetails.getVehicleNumber());
								vehicleMaster.put("vendorId",vendorList.getVendorId());
								vehicleMaster.put("vendorName",vendorList.getVendorName());
								vehicleMaster.put("vendorContactName",vendorList.getVendorContactName());
								vehicleMaster.put("vendorMobileNumber",vendorList.getVendorMobileNo());
								vehicleMaster.put("vehicleManintenanceExpDate",dateFormat.format(vehicleDetails.getVehicleFitnessDate()));

								vehicleManintenanceExp.add(vehicleMaster);
							}												
						}									

					}
				}	

			}
		}	
		requests.put("poluttionList",poluttionList);	
		requests.put("insuranceExp",insuranceExp);					
		requests.put("taxExp",taxExp);					
		requests.put("permitExp",permitExp);					
		requests.put("vehicleManintenanceExp",vehicleManintenanceExp);					

		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + efmFmVendorMaster.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	

	
	
	/**
	* update the alerts description.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2016-01-11
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/updateAlertDesc")
	public Response updateAlertDesc(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> requests = new HashMap<>();
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
  	    
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmTripAlertsPO.getUserId()))) {

				requests.put("status", "invalidRequest");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmTripAlertsPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		
		}catch(Exception e){
			log.info("authentication error"+e);
			requests.put("status", "invalidRequest");
		}
		
		//IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());
		List<EFmFmTripAlertsPO> particularaAlertDetail=iAlertBO.getParticuarAlertDetailFromAlertId(eFmFmTripAlertsPO.getBranchId(), eFmFmTripAlertsPO.getTripAlertsId(), eFmFmTripAlertsPO.getAssignRouteId());
		particularaAlertDetail.get(0).setAlertClosingDescription(eFmFmTripAlertsPO.getAlertClosingDescription());
		iAlertBO.update(particularaAlertDetail.get(0));
		requests.put("status", "success");	
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* Close the trip alerts.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2016-01-11
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/closeOpenAlert")
	public Response closeOPenAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException{
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> requests = new HashMap<>();
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
  	    
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmTripAlertsPO.getUserId()))){

			requests.put("status", "invalidRequest");
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmTripAlertsPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
			
		}catch(Exception e){
			log.info("authentication error"+e);
			requests.put("status", "invalidRequest");

		}

		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());
		List<EFmFmTripAlertsPO> particularaAlertDetail=iAlertBO.getParticuarAlertDetailFromAlertId(eFmFmTripAlertsPO.getBranchId(), eFmFmTripAlertsPO.getTripAlertsId(), eFmFmTripAlertsPO.getAssignRouteId());
		particularaAlertDetail.get(0).setAlertClosingDescription(eFmFmTripAlertsPO.getAlertClosingDescription());
		particularaAlertDetail.get(0).setAlertOpenStatus("close");
		iAlertBO.update(particularaAlertDetail.get(0));
		requests.put("status", "success");	
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	
}