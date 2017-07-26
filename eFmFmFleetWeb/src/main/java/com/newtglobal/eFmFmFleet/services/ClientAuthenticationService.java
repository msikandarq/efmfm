package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/clients")
@Consumes("application/json")
@Produces("application/json")
public class ClientAuthenticationService {
	private static Log log = LogFactory.getLog(ClientAuthenticationService.class);	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
    /*
     * this service will respond you on the basis of client code,
     * it will authenticate your client code from a JSON file
     */
    @POST
    @Path("/clientExistCheck")
    public Response getClientDetailFromBranchCode(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {    	
    	  Map<String, Object> requests = new HashMap<String, Object>();    
    	  log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
    	  IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO"); 		 		
 		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
            List<EFmFmClientBranchPO> branchDetails=userMasterBO.getBranchDetailsFromBranchCode(eFmFmClientBranchPO.getBranchCode().trim());
            if (!(branchDetails.isEmpty())) {
            	for(EFmFmClientBranchPO branchName:branchDetails){ 
            		if(eFmFmClientBranchPO.getBranchCode().equalsIgnoreCase("GNPTJP") || eFmFmClientBranchPO.getBranchCode().equalsIgnoreCase("SBOCapeTown") || eFmFmClientBranchPO.getBranchCode().equalsIgnoreCase("SBOCHN") || eFmFmClientBranchPO.getBranchCode().equalsIgnoreCase("SBOManila")|| eFmFmClientBranchPO.getBranchCode().equalsIgnoreCase("SBOBNG")){
            		requests.put("branchId", 1);  
            	}
            	else{
            		requests.put("branchId", branchName.getBranchId());  
            	}
            		requests.put("branchName", branchName.getBranchName());
            		requests.put("branchUri", branchName.getMobileLoginUrl());  
            		requests.put("loginVia", branchName.getMobileLoginVia());
            		requests.put("ssoLoginUrl", branchName.getSsoLoginUrl());
            		requests.put("status", "success");
            	}      
            }else{
            	 log.info("wrongCode");
 		    	 requests.put("status", "wrongCode");
            }	    
		    log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }
    }