package com.newtglobal.eFmFmFleet.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.QRCodeGenarator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/vendor")
@Consumes("application/json")
@Produces("application/json")
public class VendorService {
    private static Log log = LogFactory.getLog(VendorService.class);
    private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsLinux", null, "docsLinux", null);

    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();



    /**
     * The vendorByVehicleDetails method implemented. for getting the list of
     * vendor details based on the vehicles.
     *
     * @author Rajan R
     * 
     * @since 2015-05-18
     * 
     * @return vendor details.
     */
    @POST
    @Path("/vendorByVehicleDetails")
    public Response vendorByVehicleDetails(EFmFmVendorMasterPO efmFmVendorMaster) {
        IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
   	    log.info(efmFmVendorMaster.getCombinedFacility()+"serviceStart -UserId :" + efmFmVendorMaster.geteFmFmClientBranchPO().getUserId());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),efmFmVendorMaster.geteFmFmClientBranchPO().getUserId()))){
//        		log.info("Inside authentication");
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//	        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(efmFmVendorMaster.geteFmFmClientBranchPO().getUserId());
//	     	   if (!(userDetail.isEmpty())) {
//	     		String jwtToken = "";
//	     		try {
//	     		 JwtTokenGenerator token = new JwtTokenGenerator();
//	     		 jwtToken = token.generateToken();
//	     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//	     		 userDetail.get(0).setTokenGenerationTime(new Date());
//	     		 userMasterBO.update(userDetail.get(0));
//	     		} catch (Exception e) {
//	     		 log.info("error" + e);
//	     		}
//	     	   }
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        List<Map<String, Object>> vendorByVehicle = new ArrayList<Map<String, Object>>();
        efmFmVendorMaster.setCombinedFacility(efmFmVendorMaster.getCombinedFacility());
       
        List<EFmFmVendorMasterPO> listOfvendor = iVendorDetailsBO.getAllEnableVendorsDetails(efmFmVendorMaster.getCombinedFacility());
        log.info("total number of vendors"+listOfvendor.size());
        if (!(listOfvendor.isEmpty())) {
            for (EFmFmVendorMasterPO vendorList : listOfvendor) {
                Map<String, Object> vendorDetails = new HashMap<String, Object>();
                vendorDetails.put("vendorId", vendorList.getVendorId());
                vendorDetails.put("vendorName", vendorList.getVendorName());
                vendorDetails.put("vendorContactNumber", vendorList.getVendorOfficeNo());
                vendorDetails.put("vendorContactName", vendorList.getVendorContactName());
                vendorDetails.put("vendorMobileNumber", vendorList.getVendorMobileNo());
                vendorDetails.put("vendorAddress", vendorList.getAddress());
                vendorDetails.put("emailId", vendorList.getEmailId());
                vendorDetails.put("emailIdLvl1", vendorList.getEmailIdLvl1());
                vendorDetails.put("emailIdLvl2", vendorList.getEmailIdLvl2());
                vendorDetails.put("panNumber", vendorList.getPanNumber());
                vendorDetails.put("serviceTaxNumber", vendorList.getServiceTaxNumber());
                vendorDetails.put("vendorContractId", vendorList.getVendorContractId());
                vendorDetails.put("vendorContactName1", vendorList.getVendorContactName1());
                vendorDetails.put("vendorContactName2", vendorList.getVendorContactName2());
                vendorDetails.put("vendorContactName3", vendorList.getVendorContactName3());
                vendorDetails.put("vendorContactName4", vendorList.getVendorContactName4());
                vendorDetails.put("vendorContactNumber1", vendorList.getVendorContactNumber1());
                vendorDetails.put("vendorContactNumber2", vendorList.getVendorContactNumber2());
                vendorDetails.put("vendorContactNumber3", vendorList.getVendorContactNumber3());
                vendorDetails.put("vendorContactNumber4", vendorList.getVendorContactNumber4());
                vendorDetails.put("tinNumber", vendorList.getTinNumber());
                vendorDetails.put("facilityName", vendorList.geteFmFmClientBranchPO().getBranchName());

                vendorDetails.put("noOfVehicle", iVehicleCheckInBO.getAllNonRemovedVehicleCountByBranchIdAndVendorId(vendorList.getVendorId(), new MultifacilityService().combinedBranchIdDetails(efmFmVendorMaster.getUserId(),efmFmVendorMaster.getCombinedFacility())));              
                vendorDetails.put("noOfDriver", iVehicleCheckInBO.getAllNonRemovedDriverCountByBranchIdAndVendorId(vendorList.getVendorId(), new MultifacilityService().combinedBranchIdDetails(efmFmVendorMaster.getUserId(),efmFmVendorMaster.getCombinedFacility())));
                vendorDetails.put("noOfSupervisor",iVehicleCheckInBO.getAllNonRemovedSupervisorCountByBranchIdAndVendorId(vendorList.getVendorId(),new MultifacilityService().combinedBranchIdDetails(efmFmVendorMaster.getUserId(),efmFmVendorMaster.getCombinedFacility())));    
               
                vendorByVehicle.add(vendorDetails);
            }
        }
        log.info("serviceEnd -UserId :" + efmFmVendorMaster.geteFmFmClientBranchPO().getUserId());
        return Response.ok(vendorByVehicle, MediaType.APPLICATION_JSON).build();
    }

    /**
     * The listOfVehiclebyVendor method implemented. for getting the list of
     * vehicle details based on the vendor.
     *
     * @author Rajan R
     * 
     * @since 2015-05-19
     * 
     * @return vehicle details.
     */
    @POST
    @Path("/listOfVehiclebyVendor")
    public Response listOfVehiclebyVendor(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
	
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleMasterPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
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
        List<Map<String, Object>> vendorByVehicle = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> contractDetailTypes = new ArrayList<Map<String, Object>>();
        List<EFmFmVehicleMasterPO> listOfVehicle = iVehicleCheckInBO.getAllUnRemovedVehicleDetailsByBranchIdAndVendorId(new MultifacilityService().combinedBranchIdDetails(eFmFmVehicleMasterPO.getUserId(),eFmFmVehicleMasterPO.getCombinedFacility()),eFmFmVehicleMasterPO.getEfmFmVendorMaster()
				.getVendorId());
        List<EFmFmFixedDistanceContractDetailPO> contractTypes = iVehicleCheckInBO
                .getFixedDistanceActiveContractDetails(eFmFmVehicleMasterPO.getBranchId());
        Map<String, Object> vehicleTripDetais = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (!(listOfVehicle.isEmpty())) {
            for (EFmFmVehicleMasterPO vehicleDetails : listOfVehicle) {
                List<Map<String, Object>> docs = new ArrayList<Map<String, Object>>();
                Map<String, Object> insuranceDoc = new HashMap<String, Object>();
                Map<String, Object> polutionDoc = new HashMap<String, Object>();
                Map<String, Object> registrationDocs = new HashMap<String, Object>();
                Map<String, Object> taxDocs = new HashMap<String, Object>();
                Map<String, Object> vehicleList = new HashMap<String, Object>();
                vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
                vehicleList.put("vehicleNumber", vehicleDetails.getVehicleNumber());
                vehicleList.put("vehicleOwnerName", vehicleDetails.getEfmFmVendorMaster().getVendorName());
                vehicleList.put("vehicleEngineNumber", vehicleDetails.getVehicleEngineNumber());
                vehicleList.put("capacity", vehicleDetails.getCapacity());
                vehicleList.put("rcNumber", vehicleDetails.getRegistartionCertificateNumber());
                vehicleList.put("vehicleMake", vehicleDetails.getVehicleMake());
                vehicleList.put("isReplacement", vehicleDetails.getReplaceMentVehicleNum());
                vehicleList.put("vehicleModel", vehicleDetails.getVehicleModel());
                vehicleList.put("vehicleModelYear", vehicleDetails.getVehicleModelYear());
                vehicleList.put("mileage", vehicleDetails.getMileage());
                vehicleList.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
                try {
                	if(vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType() !=null){
                    	vehicleList.put("contractType", vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType());
                    	vehicleList.put("conTariffId", vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractTypeId());
                	}else{
                		vehicleList.put("contractType","NA");
                		vehicleList.put("conTariffId","NA");
                	}					
				} catch (Exception e) {
					vehicleList.put("contractType","NA");
            		vehicleList.put("conTariffId","NA");
				}      
                if (!(contractTypes.isEmpty())) {
                    vehicleList.put("conType", contractTypes.get(0).getContractTitle());
                } else {
                    vehicleList.put("conType", 0);
                }
                try {
                    if (vehicleDetails.getInsurancePath() != null) {
                        insuranceDoc.put("type", "Insurance");
                        insuranceDoc.put("location", vehicleDetails.getInsurancePath()
                                .substring(vehicleDetails.getInsurancePath().indexOf("upload") - 1));
                        docs.add(insuranceDoc);
                    }
                } catch (Exception e) {
                }
                try {
                    if (vehicleDetails.getPoluctionCertificatePath() != null) {
                        polutionDoc.put("type", "PolutionCertificate");
                        polutionDoc.put("location", vehicleDetails.getPoluctionCertificatePath()
                                .substring(vehicleDetails.getPoluctionCertificatePath().indexOf("upload") - 1));
                        docs.add(polutionDoc);
                    }
                } catch (Exception e) {
                }
                try {
                    if (vehicleDetails.getRegistartionCertificatePath() != null) {
                        registrationDocs.put("type", "RegistrationCertificate");
                        registrationDocs.put("location", vehicleDetails.getRegistartionCertificatePath()
                                .substring(vehicleDetails.getRegistartionCertificatePath().indexOf("upload") - 1));
                        docs.add(registrationDocs);
                    }
                } catch (Exception e) {
                }
                try {
                    if (vehicleDetails.getTaxCertificatePath() != null) {
                        taxDocs.put("type", "TaxCertificate");
                        taxDocs.put("location", vehicleDetails.getTaxCertificatePath()
                                .substring(vehicleDetails.getTaxCertificatePath().indexOf("upload") - 1));
                        docs.add(taxDocs);
                    }
                } catch (Exception e) {
                }
                vehicleList.put("documents", docs);
                try {
                	  vehicleList.put("contractTariffId", vehicleDetails.geteFmFmContractDetails().getDistanceContractId());
				} catch (Exception e) {
					  vehicleList.put("contractTariffId","NA");
				}              
                vehicleList.put("taxCertificateValid", dateFormat.format(vehicleDetails.getTaxCertificateValid()));
                vehicleList.put("polutionValid", dateFormat.format(vehicleDetails.getPolutionValid()));
                vehicleList.put("InsuranceDate", dateFormat.format(vehicleDetails.getInsuranceValidDate()));
                vehicleList.put("registrationDate", dateFormat.format(vehicleDetails.getRegistrationDate()));
                vehicleList.put("vehicleFitnessDate", dateFormat.format(vehicleDetails.getVehicleFitnessDate()));
                vehicleList.put("PermitValid", dateFormat.format(vehicleDetails.getPermitValidDate()));
                try{
                vehicleList.put("vehicleMaintenanceDate", dateFormat.format(vehicleDetails.getVehicleMaintenanceDate()));
                }catch(Exception e){
                    vehicleList.put("vehicleMaintenanceDate", "");
                	log.info("Error"+e);
                }
                vendorByVehicle.add(vehicleList);
            }
            if (!(listOfVehicle.isEmpty())) {
                if (!(contractTypes.isEmpty())) {
                    for (EFmFmFixedDistanceContractDetailPO contractDetails : contractTypes) {
                        Map<String, Object> contractList = new HashMap<String, Object>();
                        contractList.put("contractName", contractDetails.getContractTitle());
                        contractList.put("contractId", contractDetails.getDistanceContractId());
                        contractDetailTypes.add(contractList);
                    }
                }
            }
            vehicleTripDetais.put("vehicleDetails", vendorByVehicle);
            vehicleTripDetais.put("contractDetails", contractDetailTypes);
        }
        log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
        return Response.ok(vehicleTripDetais, MediaType.APPLICATION_JSON).build();
    }

    /**
     * The listOfdriverbyVendor method implemented. for getting the list of
     * Driver details based on the vendor.
     *
     * @author Rajan R
     * 
     * @since 2015-05-19
     * 
     * @return driver details.
     */
    @POST
    @Path("/listOfDriverByVendor")
    public Response listOfdriverbyVendor(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
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
        List<Map<String, Object>> vendorByDrivers = new ArrayList<Map<String, Object>>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");   	
        List<EFmFmDriverMasterPO> listOfDrivers = iVehicleCheckInBO.getAllRemovedDriverDetailsByBranchIdAndVendorId(new MultifacilityService().combinedBranchIdDetails(eFmFmDriverMasterPO.getUserId(),eFmFmDriverMasterPO.getCombinedFacility()),eFmFmDriverMasterPO.getEfmFmVendorMaster().getVendorId());

        if (!(listOfDrivers.isEmpty())) {
            for (EFmFmDriverMasterPO driverDetails : listOfDrivers) {
                List<Map<String, Object>> docs = new ArrayList<Map<String, Object>>();
                Map<String, Object> medicalDoc = new HashMap<String, Object>();
                Map<String, Object> licenceDoc = new HashMap<String, Object>();
                Map<String, Object> profilePicDocs = new HashMap<String, Object>();
                Map<String, Object> driverList = new HashMap<String, Object>();
                driverList.put("driverId", driverDetails.getDriverId());
                driverList.put("driverName", driverDetails.getFirstName());
                driverList.put("driverAddress", driverDetails.getAddress());
                driverList.put("mobileNumber", driverDetails.getMobileNumber());
                driverList.put("dateOfBirth", dateFormat.format(driverDetails.getDob()));
                driverList.put("licenceNumber", driverDetails.getLicenceNumber());
                driverList.put("licenceValid", dateFormat.format(driverDetails.getLicenceValid()));
                driverList.put("medicalCertificateValid",
                        dateFormat.format(driverDetails.getMedicalFitnessCertificateValid()));
                driverList.put("batchDate", dateFormat.format(driverDetails.getBatchDate()));
                if(driverDetails.getBadgeValidity()!=null){
                	driverList.put("batchValidity", dateFormat.format(driverDetails.getBadgeValidity()));
                }else{
                	driverList.put("batchValidity","");
                }
                if(driverDetails.getRelievingDate()!=null){
                	
                	driverList.put("relievingDate", dateFormat.format(driverDetails.getRelievingDate()));
                }else{
                	driverList.put("relievingDate","");
                }
                driverList.put("joinDate", dateFormat.format(driverDetails.getDateOfJoining()));
                
                driverList.put("permanentAddress", driverDetails.getPermanentAddress());
             
                try {
                    if (driverDetails.getLicenceDocPath() != null) {
                        licenceDoc.put("type", "Licence");
                        licenceDoc.put("location", driverDetails.getLicenceDocPath()
                                .substring(driverDetails.getLicenceDocPath().indexOf("upload") - 1));
                        docs.add(licenceDoc);
                    }
                } catch (Exception e) {
                    log.info("Error licenceDoc"+e);
                }
                try {
                    if (driverDetails.getMedicalDocPath() != null) {
                        medicalDoc.put("type", "MedicalCertficate");
                        medicalDoc.put("location", driverDetails.getMedicalDocPath()
                                .substring(driverDetails.getMedicalDocPath().indexOf("upload") - 1));
                        docs.add(medicalDoc);
                    }
                } catch (Exception e) {
                    log.info("Error MedicalCertficate"+e);

                }
                try {
                    if (driverDetails.getProfilePicPath() != null) {
                        profilePicDocs.put("type", "ProfilePic");
                        profilePicDocs.put("location", driverDetails.getProfilePicPath()
                                .substring(driverDetails.getProfilePicPath().indexOf("upload") - 1));
                        docs.add(profilePicDocs);
                    }
                } catch (Exception e) {
                    log.info("Error ProfilePic"+e);
                }

                driverList.put("driverBatchNum", driverDetails.getBatchNumber());
                driverList.put("ddtExpiryDate", dateFormat.format(driverDetails.getDdtValidDate()));
                driverList.put("policeExpiryDate", dateFormat.format(driverDetails.getPoliceVerificationValid()));
                driverList.put("driverJoining", dateFormat.format(driverDetails.getDateOfJoining()));
                driverList.put("documents", docs);
                driverList.put("facilityName", driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
                

                vendorByDrivers.add(driverList);
            }
        }
        log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
        return Response.ok(vendorByDrivers, MediaType.APPLICATION_JSON).build();
    }

    /**
     * The addingVendorDetails method implemented. for Adding vendor details.
     *
     * @author Rajan R
     * 
     * @since 2015-05-19
     * 
     * @return added status.
     */
    @POST
    @Path("/addingVendorDetails")
    public Response addingVendorDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
    	 log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
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
  
        // EFmFmVendorMasterPO eFmFmVendorMasterPO=new EFmFmVendorMasterPO();
        /*
         * EFmFmClientMasterPO eFmFmClientMasterPO=new EFmFmClientMasterPO();
         * eFmFmClientMasterPO.setClientId(1);
         * eFmFmVendorMasterPO.setEfmFmClientMaster(eFmFmClientMasterPO);
         * eFmFmVendorMasterPO.setAddress("Navallur");
         * eFmFmVendorMasterPO.setContractStartDate(new Date());
         * eFmFmVendorMasterPO.setContractEndDate(new Date());
         * eFmFmVendorMasterPO.setVendorContactName("Suresh");
         * eFmFmVendorMasterPO.setVendorContactNo("98657565776657");
         * eFmFmVendorMasterPO.setVendorMobileNo("9349348594385");
         * eFmFmVendorMasterPO.setVendorName("NTL");
         * eFmFmVendorMasterPO.setNoOfDays(9);
         * eFmFmVendorMasterPO.setEmailId("gmail.com");
         * eFmFmVendorMasterPO.setStatus("Y");
         * eFmFmVendorMasterPO.setTinNumber("23423412323");
         */
        iVendorDetailsBO.save(eFmFmVendorMasterPO);
        log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
        return Response.ok("Success", MediaType.APPLICATION_JSON).build();
    }

    /**
     * The modifyVendorDetails method implemented. for Modifying vendor details.
     *
     * @author Rajan R
     * 
     * @since 2015-05-19
     * 
     * @return Modified Status.
     */
    @POST
    @Path("/modifyVendorDetails")
    public Response modifyVendorDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        Map<String, Object> requests = new HashMap<String, Object>();
		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
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
        
        
        String status="Input";
    	StringBuffer temp=new StringBuffer("PleaseCheck");
    	
    	if(true){
    		
    		if(eFmFmVendorMasterPO.getVendorName()==null||eFmFmVendorMasterPO.getVendorName().equals(""))
    		{temp.append("::Vendor name cannot be empty");
    		status="Fail";
    		}
    		else{
    		CharSequence vendorName =eFmFmVendorMasterPO.getVendorName();
			Matcher matcher= Validator.alphaNumericSpaceDot(vendorName);
			if(!matcher.matches()||eFmFmVendorMasterPO.getVendorName().length()<3||eFmFmVendorMasterPO.getVendorName().length()>200)
			{temp.append("::Vendor name can be alphanumeric, space, dot and range min 3 max 200 letters");
			    status="Fail";
			}}
    		
    		if(eFmFmVendorMasterPO.getVendorMobileNo()==null||eFmFmVendorMasterPO.getVendorMobileNo().equals(""))
    		{temp.append("::Vendor mobile number cannot be empty");
    		status="Fail";    			
    		}
    		else{
        		CharSequence vendorMobile =eFmFmVendorMasterPO.getVendorMobileNo();
    			Matcher matcher= Validator.mobNumber(vendorMobile);
    			if(!matcher.matches()||eFmFmVendorMasterPO.getVendorMobileNo().length()<6||eFmFmVendorMasterPO.getVendorMobileNo().length()>18)    			
    			{temp.append("::Vendor mobile number can be only numbers and range min 6 max 18 digits");
        		status="Fail";    				
    			}
    		}
			
			
    		if(eFmFmVendorMasterPO.getEmailId()==null||eFmFmVendorMasterPO.getEmailId().equals(""))
    		{temp.append("::Vendor email ID1 cannot be empty");
    		status="Fail";    			
    		}
    		else{CharSequence vendorEmail =eFmFmVendorMasterPO.getEmailId();
			Matcher matcher= Validator.email(vendorEmail);
			if(!matcher.matches())
			{temp.append("::Vendor email ID1 should be in correct format");
    		status="Fail";  				
			}
    		}
    			
    		if(eFmFmVendorMasterPO.getEmailIdLvl1()==null||eFmFmVendorMasterPO.getEmailIdLvl1().isEmpty()){}
    		else{
			CharSequence vendorEmail1 =eFmFmVendorMasterPO.getEmailIdLvl1();
			Matcher email1= Validator.email(vendorEmail1);
			if(!email1.matches())
			{temp.append("::Vendor emailID 2  ID should be in correct format");
    		status="Fail";  				
			}}
    		
			if(eFmFmVendorMasterPO.getEmailIdLvl2()==null||eFmFmVendorMasterPO.getEmailIdLvl2().isEmpty()){}
			else{
			CharSequence vendorEmail2 =eFmFmVendorMasterPO.getEmailIdLvl2();
			Matcher email2= Validator.email(vendorEmail2);
			if(!email2.matches())
			{temp.append("::Vendor email ID 3 should be in correct format");
    		status="Fail";  				
			}}
    		
			if(eFmFmVendorMasterPO.getPanNumber()==null||eFmFmVendorMasterPO.getPanNumber().equals(""))
			{temp.append("::Vendor pan number cannot be empty");
    		status="Fail"; 				
			}
			else{
				CharSequence vendorPanNumber =eFmFmVendorMasterPO.getPanNumber();
				Matcher matcher= Validator.alphaNumeric(vendorPanNumber);
				if(!matcher.matches()||eFmFmVendorMasterPO.getPanNumber().length()<5||eFmFmVendorMasterPO.getPanNumber().length()>15)
				{temp.append("::Vendor pan number can be alphabets and numbers, range min 5 max 15 ");
	    		status="Fail"; 					
				}			
			}
			if(eFmFmVendorMasterPO.getAddress()==null||eFmFmVendorMasterPO.getAddress().length()<5||eFmFmVendorMasterPO.getAddress().length()>200)
			{temp.append("::Vendor Address range min 5 max 200 characters");
    		status="Fail";				
			}
			
			if(eFmFmVendorMasterPO.getVendorContactName()==null||eFmFmVendorMasterPO.getVendorContactName().equals(""))
			{temp.append("::Vendor Contact Name 1 cannot be empty");
    		status="Fail";				
			}
			else{
				CharSequence vendorContactName =eFmFmVendorMasterPO.getVendorContactName();
				Matcher matcher= Validator.alphaSpaceDot(vendorContactName);
				if(!matcher.matches()||eFmFmVendorMasterPO.getVendorContactName().length()<3||eFmFmVendorMasterPO.getVendorContactName().length()>20)
				{temp.append("::Vendor ContactName1 can contain only alphabets and range min 3 max 20");
	    		status="Fail";					
				}
			}
			
			if(eFmFmVendorMasterPO.getVendorContactName2()==null||eFmFmVendorMasterPO.getVendorContactName2().isEmpty()){}							
			else{
			CharSequence vendorContactName2 =eFmFmVendorMasterPO.getVendorContactName2();
			Matcher matcher= Validator.alphaSpaceDot(vendorContactName2);
			if(!matcher.matches()||eFmFmVendorMasterPO.getVendorContactName2().length()<3||eFmFmVendorMasterPO.getVendorContactName2().length()>20)
			{temp.append("::Vendor ContactName2 can contain only alphabets and range min 3 max 20");
    		status="Fail";					
			}}
			
			if(eFmFmVendorMasterPO.getVendorContactName3()==null||eFmFmVendorMasterPO.getVendorContactName3().isEmpty()){}
			else{
			CharSequence vendorContactName3 =eFmFmVendorMasterPO.getVendorContactName3();
			Matcher contact3= Validator.alphaSpaceDot(vendorContactName3);
			if(!contact3.matches()||eFmFmVendorMasterPO.getVendorContactName3().length()<3||eFmFmVendorMasterPO.getVendorContactName3().length()>20)
			{temp.append("::Vendor ContactName3 can contain only alphabets and range min 3 max 20");
    		status="Fail";					
			}}
			
			if(eFmFmVendorMasterPO.getVendorContactName4()==null||eFmFmVendorMasterPO.getVendorContactName4().isEmpty()){}
			else{CharSequence vendorContactName4 =eFmFmVendorMasterPO.getVendorContactName4();
			Matcher contact4= Validator.alphaSpaceDot(vendorContactName4);
			if(!contact4.matches()||eFmFmVendorMasterPO.getVendorContactName4().length()<3||eFmFmVendorMasterPO.getVendorContactName4().length()>20)
			{temp.append("::Vendor ContactName4 can contain only alphabets and range min 3 max 20");
    		status="Fail";					
			}}
			
    		if(eFmFmVendorMasterPO.getVendorOfficeNo()==null||eFmFmVendorMasterPO.getVendorOfficeNo().equals("")||eFmFmVendorMasterPO.getVendorOfficeNo().isEmpty())	
    		{temp.append("::Vendor contact number1 cannot be empty");
    		status="Fail";
      		}
    		else{
    			CharSequence vendorContactNumb1 =eFmFmVendorMasterPO.getVendorOfficeNo();
    			Matcher contactNumb1= Validator.mobNumber(vendorContactNumb1);
    			if(!contactNumb1.matches()||eFmFmVendorMasterPO.getVendorOfficeNo().length()<6||eFmFmVendorMasterPO.getVendorOfficeNo().length()>18)
    			{temp.append("::Vendor contactNumber1 can contain only numbers and range min 6 max 18");
        		status="Fail";    				
    			}
    		}
    		
    		if(eFmFmVendorMasterPO.getVendorContactNumber2()==null||eFmFmVendorMasterPO.getVendorContactNumber2().isEmpty()){}
    		else{CharSequence vendorContactNumb2 =eFmFmVendorMasterPO.getVendorContactNumber2();
			Matcher contactNumb2= Validator.mobNumber(vendorContactNumb2);
			if(!contactNumb2.matches()||eFmFmVendorMasterPO.getVendorContactNumber2().length()<6||eFmFmVendorMasterPO.getVendorContactNumber2().length()>18)
			{temp.append("::Vendor contactNumber2 can contain only numbers and range min 6 max 18");
    		status="Fail";
			}
			}
    		
    		if(eFmFmVendorMasterPO.getVendorContactNumber3()==null||eFmFmVendorMasterPO.getVendorContactNumber3().isEmpty()){}
    		else{CharSequence vendorContactNumb3 =eFmFmVendorMasterPO.getVendorContactNumber3();
			Matcher contactNumb3= Validator.mobNumber(vendorContactNumb3);
			if(!contactNumb3.matches()||eFmFmVendorMasterPO.getVendorContactNumber3().length()<6||eFmFmVendorMasterPO.getVendorContactNumber3().length()>18)
			{temp.append("::Vendor contactNumber3 can contain only numbers and range min 6 max 18");
    		status="Fail";
			}}
			
    		if(eFmFmVendorMasterPO.getVendorContactNumber4()==null||eFmFmVendorMasterPO.getVendorContactNumber4().isEmpty()){}
    		else{CharSequence vendorContactNumb4 =eFmFmVendorMasterPO.getVendorContactNumber4();
			Matcher contactNumb4= Validator.mobNumber(vendorContactNumb4);
			if(!contactNumb4.matches()||eFmFmVendorMasterPO.getVendorContactNumber4().length()<6||eFmFmVendorMasterPO.getVendorContactNumber4().length()>18)
			{temp.append("::Vendor contactNumber4 can contain only numbers and range min 6 max 18");
    		status="Fail";				
			}}
    		
    		if(eFmFmVendorMasterPO.getServiceTaxNumber()==null||eFmFmVendorMasterPO.getServiceTaxNumber().equals("")||eFmFmVendorMasterPO.getServiceTaxNumber().isEmpty())
    		{temp.append("::Vendor service tax number cannot be empty");
    		status="Fail";    			
    		} 
//    		else{CharSequence vendorServiceTaxNo =eFmFmVendorMasterPO.getServiceTaxNumber();
//			Matcher serviceTax= Validator.validateNumber(vendorServiceTaxNo);
//    			if(!serviceTax.matches() || eFmFmVendorMasterPO.getServiceTaxNumber().length()<5||eFmFmVendorMasterPO.getServiceTaxNumber().length()>20)
//    			{temp.append("::Vendor service tax number can have numbers alphabets and range min 5 max 20");
//        		status="Fail"; 
//    			}
//    			}
    		if(status.equals("Fail"))
			{log.info("Invalid input:");
			requests.put("inputInvalid", temp);
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
    			
    		}
    	log.info("Input valid");
        
        
        
   	
        EFmFmVendorMasterPO vendorDetails = iVendorDetailsBO
                .getParticularVendorDetail(eFmFmVendorMasterPO.getVendorId());
        vendorDetails.setAddress(eFmFmVendorMasterPO.getAddress());
        vendorDetails.setVendorContactName(eFmFmVendorMasterPO.getVendorContactName());
        vendorDetails.setVendorOfficeNo(eFmFmVendorMasterPO.getVendorOfficeNo());
        vendorDetails.setVendorMobileNo(eFmFmVendorMasterPO.getVendorMobileNo());
        vendorDetails.setVendorName(eFmFmVendorMasterPO.getVendorName());
        vendorDetails.setVendorContactName1(eFmFmVendorMasterPO.getVendorContactName1());
        vendorDetails.setVendorContactNumber1(eFmFmVendorMasterPO.getVendorContactNumber1());
        vendorDetails.setVendorContactName2(eFmFmVendorMasterPO.getVendorContactName2());
        vendorDetails.setVendorContactNumber2(eFmFmVendorMasterPO.getVendorContactNumber2());
        vendorDetails.setVendorContactName3(eFmFmVendorMasterPO.getVendorContactName3());
        vendorDetails.setVendorContactNumber3(eFmFmVendorMasterPO.getVendorContactNumber3());
        vendorDetails.setVendorContactName4(eFmFmVendorMasterPO.getVendorContactName4());
        vendorDetails.setVendorContactNumber4(eFmFmVendorMasterPO.getVendorContactNumber4());
        // vendorDetails.setNoOfDays(9);
        vendorDetails.setEmailId(eFmFmVendorMasterPO.getEmailId());        
        vendorDetails.setEmailIdLvl1(eFmFmVendorMasterPO.getEmailIdLvl1());
        vendorDetails.setEmailIdLvl2(eFmFmVendorMasterPO.getEmailIdLvl2());        
        vendorDetails.setVendorContractId(eFmFmVendorMasterPO.getVendorContractId());
        vendorDetails.setPanNumber(eFmFmVendorMasterPO.getPanNumber());
        vendorDetails.setServiceTaxNumber(eFmFmVendorMasterPO.getServiceTaxNumber());
        // vendorDetails.setStatus("Y");
        // vendorDetails.setTinNumber("23423412323");
        iVendorDetailsBO.update(vendorDetails);
        log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
        return Response.ok("Success", MediaType.APPLICATION_JSON).build();
    }

    /**
     * The removeVendorDetails method implemented. for Modifying vendor details.
     *
     * @author Rajan R
     * 
     * @since 2015-05-19
     * 
     * @return removed Status.
     */
    @POST
    @Path("/removeVendorDetails")
    public Response removeVendorDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
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
   	 
        EFmFmVendorMasterPO vendorDetails = iVendorDetailsBO
                .getParticularVendorDetail(eFmFmVendorMasterPO.getVendorId());
        vendorDetails.setStatus("D");
        iVendorDetailsBO.update(vendorDetails);
        log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
        return Response.ok("Success", MediaType.APPLICATION_JSON).build();
    }

    /**
     * The removeDriverDetails method implemented. for Modifying driver details.
     *
     * @author Rajan R
     * 
     * @since 2015-05-29
     * 
     * @return removed Status.
     */
    @POST
    @Path("/removeDriverDetails")
    public Response removeDriverDetails(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
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
   	 
        EFmFmDriverMasterPO driverMasterPO = iApprovalBO.getParticularDriverDetail(eFmFmDriverMasterPO.getDriverId());
        driverMasterPO.setStatus("D");
        iApprovalBO.update(driverMasterPO);
        log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
        return Response.ok("Success", MediaType.APPLICATION_JSON).build();
    }

    /**
     * The modifyDriverDetails method implemented. for Modifying driver details.
     *
     * @author Rajan R
     * 
     * @since 2015-05-29
     * 
     * @return modified Status.
     * @throws ParseException
     */
    @POST
    @Path("/modifyDriverDetails")
    public Response modifyDriverDetails(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws ParseException {
        IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
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
        
   	 
        EFmFmDriverMasterPO driverMasterPO = iApprovalBO.getParticularDriverDetail(eFmFmDriverMasterPO.getDriverId());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date licenceValidDate = formatter.parse(eFmFmDriverMasterPO.getLicenceValidDate());
        Date ddtValidDate = formatter.parse(eFmFmDriverMasterPO.getDdtExpiryDate());
        Date dobValidDate = formatter.parse(eFmFmDriverMasterPO.getDobDate());
        Date batchValidDate = formatter.parse(eFmFmDriverMasterPO.getDriverBatchDate());
        Date joiningValidDate = formatter.parse(eFmFmDriverMasterPO.getDriverJoiningDate());
        Date medicalValidDate = formatter.parse(eFmFmDriverMasterPO.getDriverMedicalExpiryDate());
        Date policeValidDate = formatter.parse(eFmFmDriverMasterPO.getDriverPoliceVerificationDate());
        driverMasterPO.setDob(dobValidDate);

        driverMasterPO.setBatchNumber(eFmFmDriverMasterPO.getBatchNumber());
        driverMasterPO.setMedicalFitnessCertificateValid(medicalValidDate);
        driverMasterPO.setLicenceValid(licenceValidDate);
        driverMasterPO.setDdtValidDate(ddtValidDate);
        driverMasterPO.setBatchDate(batchValidDate);
        driverMasterPO.setPoliceVerificationValid(policeValidDate);
        driverMasterPO.setDateOfJoining(joiningValidDate);
        driverMasterPO.setFirstName(eFmFmDriverMasterPO.getFirstName());
        driverMasterPO.setMobileNumber(eFmFmDriverMasterPO.getMobileNumber());
        driverMasterPO.setLicenceNumber(eFmFmDriverMasterPO.getLicenceNumber());
        driverMasterPO.setAddress(eFmFmDriverMasterPO.getAddress());
        driverMasterPO.setPermanentAddress(eFmFmDriverMasterPO.getPermanentAddress());        
        driverMasterPO.setBadgeValidity(eFmFmDriverMasterPO.getBadgeValidity());       
        driverMasterPO.setRelievingDate(eFmFmDriverMasterPO.getRelievingDate());       
        
        iApprovalBO.update(driverMasterPO);
        log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
        return Response.ok("Success", MediaType.APPLICATION_JSON).build();
    }
    /*
     * driver details by vendor id and driver number
     * 
     */
    
    
    @POST
    @Path("/driverDetailByVendorId")
    public Response getDriverDetailsByVendorId(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());
		try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
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
		 
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
       
		List<EFmFmDriverMasterPO> driverExistDetail=iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(eFmFmDriverMasterPO.getMobileNumber(), eFmFmDriverMasterPO.getVendorId());
		if(!(driverExistDetail.isEmpty())){
			if(driverExistDetail.get(0).getStatus().equalsIgnoreCase("P")){
				responce.put("driverId", driverExistDetail.get(0).getDriverId());
				responce.put("driverName",driverExistDetail.get(0).getFirstName());
				responce.put("driverNumber", driverExistDetail.get(0).getMobileNumber());
				responce.put("licenceNum",driverExistDetail.get(0).getLicenceNumber());
				responce.put("licenceValidity", dateFormat.format(driverExistDetail.get(0).getLicenceValid()));
				responce.put("status", "notApproved");
				log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
				 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			else if(driverExistDetail.get(0).getStatus().equalsIgnoreCase("R")){
				responce.put("driverId", driverExistDetail.get(0).getDriverId());
				responce.put("driverName",driverExistDetail.get(0).getFirstName());
				responce.put("driverNumber", driverExistDetail.get(0).getMobileNumber());
				responce.put("licenceNum",driverExistDetail.get(0).getLicenceNumber());
				responce.put("licenceValidity", dateFormat.format(driverExistDetail.get(0).getLicenceValid()));
				responce.put("status", "driverRejected");
				log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
				 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			responce.put("status", "success");
			responce.put("driverId", driverExistDetail.get(0).getDriverId());
			responce.put("driverName",driverExistDetail.get(0).getFirstName());
			responce.put("driverNumber", driverExistDetail.get(0).getMobileNumber());
			responce.put("licenceNum",driverExistDetail.get(0).getLicenceNumber());
			responce.put("licenceValidity", dateFormat.format(driverExistDetail.get(0).getLicenceValid()));
			log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
			 return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		else{
			driverExistDetail=iVehicleCheckInBO.getDriverDetailsFromDriverMobilrNumber(eFmFmDriverMasterPO.getMobileNumber());
	if(!(driverExistDetail.isEmpty())){
		responce.put("status", "existDiffVen");
		log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
		 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
			responce.put("status", "fail");
		}
		log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
		 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }	
    
    /*
     * Vehicle details by vendor id and vehicle number
     * 
     */
    
    
    @POST
    @Path("/vehicleDetailByVendorId")
    public Response getVehiclesDetailsByVendorId(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleMasterPO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
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
	
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		log.info("getVehiclesDetailsByVendorId");
        
		List<EFmFmVehicleMasterPO> vehicleExistDetail=iVehicleCheckInBO.getVehicleDetailsFromVehicleNumberAndVendorId(eFmFmVehicleMasterPO.getVehicleNumber(), eFmFmVehicleMasterPO.getVendorId());
		if(!(vehicleExistDetail.isEmpty())){
			if(vehicleExistDetail.get(0).getStatus().equalsIgnoreCase("P")){
				responce.put("vehicleId", vehicleExistDetail.get(0).getVehicleId());
				responce.put("capacity", vehicleExistDetail.get(0).getCapacity());
				responce.put("vehicleModel",vehicleExistDetail.get(0).getVehicleModel());
				responce.put("taxValidity", dateFormat.format(vehicleExistDetail.get(0).getTaxCertificateValid()));
				responce.put("polutionValidity", dateFormat.format(vehicleExistDetail.get(0).getPolutionValid()));
				responce.put("permitValidity", dateFormat.format(vehicleExistDetail.get(0).getPermitValidDate()));
				responce.put("insuranceValidity", dateFormat.format(vehicleExistDetail.get(0).getInsuranceValidDate()));
				responce.put("status", "notApproved");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			else if(vehicleExistDetail.get(0).getStatus().equalsIgnoreCase("R")){
				responce.put("vehicleId", vehicleExistDetail.get(0).getVehicleId());
				responce.put("capacity", vehicleExistDetail.get(0).getCapacity());
				responce.put("vehicleModel",vehicleExistDetail.get(0).getVehicleModel());
				responce.put("taxValidity", dateFormat.format(vehicleExistDetail.get(0).getTaxCertificateValid()));
				responce.put("polutionValidity", dateFormat.format(vehicleExistDetail.get(0).getPolutionValid()));
				responce.put("permitValidity", dateFormat.format(vehicleExistDetail.get(0).getPermitValidDate()));
				responce.put("insuranceValidity", dateFormat.format(vehicleExistDetail.get(0).getInsuranceValidDate()));
				responce.put("status", "vehicleRejected");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			responce.put("status", "success");
			responce.put("vehicleId", vehicleExistDetail.get(0).getVehicleId());
			responce.put("capacity", vehicleExistDetail.get(0).getCapacity());
			responce.put("vehicleModel",vehicleExistDetail.get(0).getVehicleModel());
			responce.put("taxValidity", dateFormat.format(vehicleExistDetail.get(0).getTaxCertificateValid()));
			responce.put("polutionValidity", dateFormat.format(vehicleExistDetail.get(0).getPolutionValid()));
			responce.put("permitValidity", dateFormat.format(vehicleExistDetail.get(0).getPermitValidDate()));
			responce.put("insuranceValidity", dateFormat.format(vehicleExistDetail.get(0).getInsuranceValidDate()));
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		else{
			vehicleExistDetail=iVehicleCheckInBO.getVehicleDetailsFromVehicleNumber(eFmFmVehicleMasterPO.getVehicleNumber());
			if(!(vehicleExistDetail.isEmpty())){
				responce.put("status", "existDiffVen");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

			responce.put("status", "fail");
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }	
    
    
    /**
     * The adding adhoc vehicle and driver details adding in to the syatem. by vendor wise.
     *
     * @author Sarfraz Khan
     * 
     * @since 2016-02-02
     * 
     * @return added status.
     * @throws ParseException 
     */
    @POST
    @Path("/addingAdhocDetails")
    public Response addingAdhocEntitiesDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
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
    	
		String status="Input";
    	StringBuffer temp=new StringBuffer("PleaseCheck");
         if(true){
        	if(eFmFmVehicleMasterPO.getVehicleNumber()==null||eFmFmVehicleMasterPO.getVehicleNumber().equals(""))
        	{temp.append("::Vehicle number cannot be empty");
      	     status="Fail";
        	}
        	else{
    	CharSequence vehicleNoInput = eFmFmVehicleMasterPO.getVehicleNumber();
		Matcher vehicleNo= Validator.alphaNumeric(vehicleNoInput);
		if(!vehicleNo.matches()||eFmFmVehicleMasterPO.getVehicleNumber().length()<4||eFmFmVehicleMasterPO.getVehicleNumber().length()>15)		 
	     {temp.append("::Vehicle number should contain alphabets and numbers only, range min 4 max 15");
	      status="Fail";
	     }}
        	
        	if(eFmFmVehicleMasterPO.getVehicleModel()==null||eFmFmVehicleMasterPO.getVehicleModel().equals(""))
    		{status="Fail";
    		temp.append("::Vehicle model cannot be empty");			
    		}
    		else{
    	     CharSequence vehicleModel = eFmFmVehicleMasterPO.getVehicleModel();
    		Matcher model= Validator.alphaNumeric(vehicleModel);
    		if(!model.matches()||eFmFmVehicleMasterPO.getVehicleModel().length()>20)
    	     { status="Fail";
    			temp.append("::Vehicle model number should contain only characters and range max 20");
    	     }}	
        	
        if(!(eFmFmVehicleMasterPO.getVendorId()>0))
        { status="Fail";
		  temp.append("::Vendor Id required and should be positive integer");
       }
        		
        if(eFmFmVehicleMasterPO.getCapacity()<=0||eFmFmVehicleMasterPO.getCapacity()>99)
        { status="Fail";
		  temp.append("::Vehicle Capacity is required and should be positive, max range 99");        	
        }
        
		
		if(eFmFmVehicleMasterPO.getFirstName()==null||eFmFmVehicleMasterPO.getFirstName().equals(""))
		{status="Fail";
		  temp.append("::Driver name cannot be empty");
		}
		else{
		CharSequence driverName = eFmFmVehicleMasterPO.getFirstName();
		Matcher driver= Validator.alphaSpaceDot(driverName);
		if(!driver.matches()||eFmFmVehicleMasterPO.getFirstName().length()<3||eFmFmVehicleMasterPO.getFirstName().length()>20)
		{ status="Fail";
		  temp.append("::Driver name must contain only alphabets, space and dot, range min 3 max 20 characters");
		}}
		
		if(eFmFmVehicleMasterPO.getLicenceNumber()==null||eFmFmVehicleMasterPO.getLicenceNumber().equals(""))
		{status="Fail";
		  temp.append("::License number cannot be empty");
		}
		else{
		CharSequence licenseNo = eFmFmVehicleMasterPO.getLicenceNumber();
		Matcher license= Validator.alphaNumeric(licenseNo);
		if(!license.matches()||eFmFmVehicleMasterPO.getLicenceNumber().length()<6||eFmFmVehicleMasterPO.getLicenceNumber().length()>20)
		{ status="Fail";
		  temp.append("::License number should contain only characters and numbers, range min 6 max 20 characters");
		}}
		
		if(eFmFmVehicleMasterPO.getPolutionDate()==null)
		{   status="Fail";
			temp.append("::Pollutionvalid date should not be empty");
		}
		if(eFmFmVehicleMasterPO.getInsuranceValid()==null)
		{  status="Fail";
		   temp.append("::InsuranceValid date should not be empty and correct format");
		}
		
		if(eFmFmVehicleMasterPO.getPermitValid()==null)
		{   status="Fail";
		    temp.append("::PermitValid date should not be empty and correct format");
		}
		
		if(eFmFmVehicleMasterPO.getTaxValidDate()==null)
		{   status="Fail";
			temp.append("::TaxValid date should not be empty and correct format");
		}
		
		if(eFmFmVehicleMasterPO.getLicenceValidDate()==null)
		{   status="Fail";  
		    temp.append("::LicenceValid date should not be empty and correct format");
		}
		
		if(eFmFmVehicleMasterPO.getBranchId()<=0)
		{temp.append("::Vehicle contract TarrifID can be only positive number ");
	      status="Fail";			
		}
		else{
			int branchID=eFmFmVehicleMasterPO.getBranchId();
			String branch=Integer.toString(branchID);
			if(branch.length()>3)
			{temp.append("::Vehicle contract branchID can be max 3 digits");
		      status="Fail";}
		}
			
		
		
		
		if(status.equals("Fail"))
		{
		log.info("Invalid input:");
		responce.put("InputInvalid", temp);
	    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	   	}
         }
         log.info("valid input:");
        
		
		
		
		
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		log.info("addingAdhocDetails"+eFmFmVehicleMasterPO.getActionType());
		boolean vehicleExist=false;
		boolean driverExist=false;
      
		List<EFmFmVehicleMasterPO> vehicleExistDetail=iVehicleCheckInBO.getVehicleDetailsFromVehicleNumberAndVendorId(eFmFmVehicleMasterPO.getVehicleNumber(), eFmFmVehicleMasterPO.getVendorId());
		if(!(vehicleExistDetail.isEmpty())){
			vehicleExist=true;
		}
		else{
		EFmFmVendorMasterPO eFmFmVendorMasterPO=new EFmFmVendorMasterPO();
		eFmFmVendorMasterPO.setVendorId(eFmFmVehicleMasterPO.getVendorId());	
		
		EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();		
		vehicleMasterPO.setVehicleNumber(eFmFmVehicleMasterPO.getVehicleNumber());
		vehicleMasterPO.setRegistartionCertificateNumber(eFmFmVehicleMasterPO.getVehicleNumber());		
		vehicleMasterPO.setCapacity(eFmFmVehicleMasterPO.getCapacity());	
		vehicleMasterPO.setVehicleEngineNumber(eFmFmVehicleMasterPO.getVehicleNumber());
		vehicleMasterPO.setVehicleMake("adhoc");
		vehicleMasterPO.setVehicleModel(eFmFmVehicleMasterPO.getVehicleModel());
		vehicleMasterPO.setReplaceMentVehicleNum("NO");	
		
		vehicleMasterPO.setTaxCertificateValid(dateFormat.parse(eFmFmVehicleMasterPO.getTaxValidDate()));
		vehicleMasterPO.setPermitValidDate(dateFormat.parse(eFmFmVehicleMasterPO.getPermitValid()));
		vehicleMasterPO.setPolutionValid(dateFormat.parse(eFmFmVehicleMasterPO.getPolutionDate()));
		vehicleMasterPO.setInsuranceValidDate(dateFormat.parse(eFmFmVehicleMasterPO.getInsuranceValid()));	
		
		vehicleMasterPO.setVehicleModelYear("2016");
		vehicleMasterPO.setRemarks("adhoc");
		vehicleMasterPO.setVehicleACFitted("NO");
		vehicleMasterPO.setVehicleGPSFitted("NO");
		vehicleMasterPO.setrFIDFitted("NO");
		EFmFmVendorContractTypeMasterPO contractId=new EFmFmVendorContractTypeMasterPO();
		contractId.setContractTypeId(1);
		List<EFmFmFixedDistanceContractDetailPO> contractDetails = iVehicleCheckInBO.getFixedDistanceDummyDetails(eFmFmVehicleMasterPO.getBranchId());		
		vehicleMasterPO.seteFmFmContractDetails(contractDetails.get(0));	
		vehicleMasterPO.setUpdatedTime(new Date());
		vehicleMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);
		vehicleMasterPO.setRegistrationDate(new Date());
		vehicleMasterPO.setVehicleFitnessDate(new Date());
		vehicleMasterPO.setStatus("P");
		iVehicleCheckInBO.save(vehicleMasterPO);		
		List<EFmFmVehicleMasterPO> vehicleNumExist = iVehicleCheckInBO
				.getParticularVehicleDetailsByVehicleNumber(vehicleMasterPO.getVehicleNumber(), eFmFmVehicleMasterPO.getVendorId(),eFmFmVehicleMasterPO.getCombinedFacility());
		if (!(vehicleNumExist.isEmpty())) {
		try {
			QRCodeGenarator qRCode = new QRCodeGenarator();
			qRCode.createQRCode(vehicleMasterPO.getVehicleNumber(), SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
			EFmFmVehicleDocsPO eFmFmVehicleDocsPO = new EFmFmVehicleDocsPO();
			eFmFmVehicleDocsPO.setCreationTime(new Date());
			eFmFmVehicleDocsPO.setDocumentName(vehicleMasterPO.getVehicleNumber() + "QRCode.png");
			eFmFmVehicleDocsPO.setStatus("Y");
			eFmFmVehicleDocsPO.setUploadpath(
					SERVER_UPLOAD_LINUX_LOCATION_FOLDER + vehicleMasterPO.getVehicleNumber() + "QRCode.png");
			eFmFmVehicleDocsPO.seteFmFmVehicleMaster(vehicleNumExist.get(0));
			iVehicleCheckInBO.addVehicleUploadDetails(eFmFmVehicleDocsPO);
		} catch (Exception e) {
			log.info("QR Code Error:" + e);
		}
		
	}		
		}
		List<EFmFmDriverMasterPO> driverExistDetail=iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(eFmFmVehicleMasterPO.getMobileNumber(), eFmFmVehicleMasterPO.getVendorId());
		if(!(driverExistDetail.isEmpty())){
			driverExist=true;
		}else{
			EFmFmVendorMasterPO eFmFmVendorMasterPO=new EFmFmVendorMasterPO();
			eFmFmVendorMasterPO.setVendorId(eFmFmVehicleMasterPO.getVendorId());	
	 	EFmFmDriverMasterPO driverMasterPO = new EFmFmDriverMasterPO();
	 	driverMasterPO.setFirstName(eFmFmVehicleMasterPO.getFirstName());		 	
	 	driverMasterPO.setLastName("");
	 	driverMasterPO.setMedicalFitnessCertificateValid(new Date());
	 	driverMasterPO.setGender("MALE");			 	
	 	driverMasterPO.setMobileNumber(eFmFmVehicleMasterPO.getMobileNumber());			 	
	 	driverMasterPO.setLicenceNumber(eFmFmVehicleMasterPO.getLicenceNumber());	
	 	driverMasterPO.setAddress("");			 	
		driverMasterPO.setPermanentAddress("");
		driverMasterPO.setBatchNumber("adhoc");
		driverMasterPO.setBatchDate(new Date());
		driverMasterPO.setBadgeValidity(new Date());								
	 	driverMasterPO.setDateOfJoining(new Date());
	 	driverMasterPO.setDdtValidDate(new Date());
	 	driverMasterPO.setDob(new Date());		 		 	
	 	driverMasterPO.setPoliceVerification("notdone");
	 	driverMasterPO.setStatus("P");	
	 	driverMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);
	 	try{
	 	driverMasterPO.setLicenceValid(dateFormat.parse(eFmFmVehicleMasterPO.getLicenceValidDate()));
	 	}
	 	catch(Exception e){
		 	driverMasterPO.setLicenceValid(new Date());
		 	log.info("Error"+e);
	 	}
	 	driverMasterPO.setPoliceVerificationValid(new Date());	 	
		iRouteDetailBO.saveDriverRecord(driverMasterPO);
		}
		if(driverExist && vehicleExist && !(eFmFmVehicleMasterPO.getActionType().equalsIgnoreCase("withoutDevice"))){
			responce.put("status", "bothExists");
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		else{	
			if(eFmFmVehicleMasterPO.getActionType().equalsIgnoreCase("withoutDevice")){
				vehicleExistDetail=iVehicleCheckInBO.getVehicleDetailsFromVehicleNumberAndVendorId(eFmFmVehicleMasterPO.getVehicleNumber(), eFmFmVehicleMasterPO.getVendorId());
				driverExistDetail=iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(eFmFmVehicleMasterPO.getMobileNumber(), eFmFmVehicleMasterPO.getVendorId());	        
		        List<EFmFmVehicleCheckInPO> checkInVehicle = iVehicleCheckInBO.getParticularCheckedInVehicles(
		        		eFmFmVehicleMasterPO.getCombinedFacility(), vehicleExistDetail.get(0).getVehicleId());
		        System.out.println("checkInVehicle"+checkInVehicle.size());
		        if (!(checkInVehicle.isEmpty())) {
		            responce.put("status", "VcheckedIn");
		            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		        }
		        List<EFmFmVehicleCheckInPO> checkInDriver = iVehicleCheckInBO
		                .getParticularDriverCheckedInDetails(driverExistDetail.get(0).getDriverId());
		        if (!(checkInDriver.isEmpty())) {
		            responce.put("status", "DcheckedIn");
		            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		        }

		        if (driverExistDetail.get(0).getEfmFmVendorMaster().getVendorId() != vehicleExistDetail.get(0).getEfmFmVendorMaster()
		                .getVendorId()) {
		            responce.put("status", "mismatch");
		            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		        }

		        EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
		        EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
		        vehicleMaster.setVehicleId(vehicleExistDetail.get(0).getVehicleId());
		        
		        EFmFmDriverMasterPO driverMaster = new EFmFmDriverMasterPO();
		        driverMaster.setDriverId(driverExistDetail.get(0).getDriverId());
		       
		        eFmFmVehicleCheckInPO.setCheckInTime(new Date());
		        eFmFmVehicleCheckInPO.setReadFlg("Y");
		        eFmFmVehicleCheckInPO.setAdminMailTriggerStatus(false);
		        eFmFmVehicleCheckInPO.setSupervisorMailTriggerStatus(false);		        
		        eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(vehicleMaster);
		        eFmFmVehicleCheckInPO.setEfmFmDriverMaster(driverMaster);		        
 
		        EFmFmDeviceMasterPO deviceMaster = new EFmFmDeviceMasterPO();
		        deviceMaster.setDeviceId(1);
		        eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceMaster);
		        eFmFmVehicleCheckInPO.setStatus("Y");
		        iVehicleCheckInBO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);
				responce.put("status", "checkedIn");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}			
		}
		responce.put("status", "success");	
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
	    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
	/*
	 * Vehicle and driver checkedIn by System with dummy device and Actual
	 * device.
	 * 
	 */

	@POST
	@Path("/vehicleDriverCheckIn")
	public Response vehicleAndDriverCheckedIn(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("Adhoc vehicleDriverCheckIn");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
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
		
		
		List<EFmFmVehicleMasterPO> vehicleExistDetail = iVehicleCheckInBO
				.getVehicleDetailsFromVehicleNumberAndVendorId(eFmFmVehicleMasterPO.getVehicleNumber(),
						eFmFmVehicleMasterPO.getVendorId());
		if (!(vehicleExistDetail.isEmpty())) {
			if (vehicleExistDetail.get(0).getStatus().equalsIgnoreCase("P")) {
				responce.put("status", "notApproved");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else if (vehicleExistDetail.get(0).getStatus().equalsIgnoreCase("R")) {
				responce.put("status", "vehicleRejected");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		else{
			responce.put("status", "VNotRegister");
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmDriverMasterPO> driverExistDetail = iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(
				eFmFmVehicleMasterPO.getMobileNumber(), eFmFmVehicleMasterPO.getVendorId());
		if (!(driverExistDetail.isEmpty())) {
			if (driverExistDetail.get(0).getStatus().equalsIgnoreCase("P")) {
				responce.put("status", "notApproved");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else if (driverExistDetail.get(0).getStatus().equalsIgnoreCase("R")) {
				responce.put("status", "driverRejected");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}else{
			responce.put("status", "DNotRegister");
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
        if (driverExistDetail.get(0).getEfmFmVendorMaster().getVendorId() != vehicleExistDetail.get(0).getEfmFmVendorMaster()
                .getVendorId()) {
            responce.put("status", "mismatch");
            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        
        List<EFmFmVehicleCheckInPO> checkInVehicle = iVehicleCheckInBO.getParticularCheckedInVehicles(eFmFmVehicleMasterPO.getCombinedFacility(), vehicleExistDetail.get(0).getVehicleId());
        System.out.println("checkInVehicle"+checkInVehicle.size());
        if (!(checkInVehicle.isEmpty())) {
            responce.put("status", "VcheckedIn");
            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        List<EFmFmVehicleCheckInPO> checkInDriver = iVehicleCheckInBO
                .getParticularDriverCheckedInDetails(driverExistDetail.get(0).getDriverId());
        if (!(checkInDriver.isEmpty())) {
            responce.put("status", "DcheckedIn");
            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
        EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
        vehicleMaster.setVehicleId(vehicleExistDetail.get(0).getVehicleId());     
        EFmFmDriverMasterPO driverMaster = new EFmFmDriverMasterPO();
        driverMaster.setDriverId(driverExistDetail.get(0).getDriverId());      
        eFmFmVehicleCheckInPO.setCheckInTime(new Date());
        eFmFmVehicleCheckInPO.setReadFlg("Y");
        eFmFmVehicleCheckInPO.setAdminMailTriggerStatus(false);
        eFmFmVehicleCheckInPO.setSupervisorMailTriggerStatus(false);		        
        eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(vehicleMaster);
        eFmFmVehicleCheckInPO.setEfmFmDriverMaster(driverMaster);		        
       log.info("eFmFmVehicleMasterPO.getDeviceId()"+eFmFmVehicleMasterPO.getDeviceId());
        EFmFmDeviceMasterPO deviceMaster = new EFmFmDeviceMasterPO();
        if(eFmFmVehicleMasterPO.getDeviceId().equalsIgnoreCase("NO Device")){
        List<EFmFmDeviceMasterPO> dummyCheckInDetails=iVehicleCheckInBO.getDummyDeviceDetailsFromDeviceId(eFmFmVehicleMasterPO.getCombinedFacility());
        	if(!(dummyCheckInDetails.isEmpty())){
                deviceMaster.setDeviceId(dummyCheckInDetails.get(0).getDeviceId());
        	}
        	
        eFmFmVehicleCheckInPO.setCheckInType("NoDevice");
        }
       else if(eFmFmVehicleMasterPO.getDeviceId().equalsIgnoreCase("GPS Device")){
           List<EFmFmDeviceMasterPO> dummyCheckInDetails=iVehicleCheckInBO.getDummyDeviceDetailsFromDeviceId(eFmFmVehicleMasterPO.getCombinedFacility());
       	if(!(dummyCheckInDetails.isEmpty())){
               deviceMaster.setDeviceId(dummyCheckInDetails.get(0).getDeviceId());
       	}
            eFmFmVehicleCheckInPO.setCheckInType("GPSDevice");
            }
        else{
        	//Code for checkIng about device
        	try{
        	 List<EFmFmVehicleCheckInPO> checkInDevice = iVehicleCheckInBO
                     .getParticularDeviceCheckedInDetails(Integer.parseInt(eFmFmVehicleMasterPO.getDeviceId()));
             if (!(checkInDevice.isEmpty())) {
                 responce.put("status", "deviceCheckedIn");
                 log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
                 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
             }
        	}catch(Exception e){
        		log.info("deviceCheckedIn error"+e);
        	}
            deviceMaster.setDeviceId(Integer.parseInt(eFmFmVehicleMasterPO.getDeviceId()));
            eFmFmVehicleCheckInPO.setCheckInType("mobile");
        }
        eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceMaster);
        eFmFmVehicleCheckInPO.setStatus("Y");
        
        iVehicleCheckInBO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);
        responce.put("status", "success");
        log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}    
    
    
}