package com.newtglobal.eFmFmFleet.services;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;


@Component
@Path("/multifacility")
@Consumes("application/json")
@Produces("application/json")
public class MultifacilityService {	
	
	private static Log log = LogFactory.getLog(MultifacilityService.class);	
	
	 @Context
	 private HttpServletRequest httpRequest;
	 JwtTokenGenerator token=new JwtTokenGenerator();

	
	public String combinedBranchIdDetails(int userId,String combinedFacility){		
		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");		 
		  List<EFmFmUserFacilityMappingPO>  eFmFmUserFacilityMappingPO=facilityBO.getAllFacilitiesAttachedToUser(userId);
		  String combinedBranchId="";
		  StringBuffer branchId= new StringBuffer();
		  try {
			    if(null !=combinedFacility.trim() && combinedFacility.trim()!="" && combinedFacility.trim()!="0"){			  	 
			    	combinedBranchId=combinedFacility;
			    }else{
			    	if(!(eFmFmUserFacilityMappingPO.isEmpty())){
						   for(EFmFmUserFacilityMappingPO facility:eFmFmUserFacilityMappingPO){			   
							   branchId.append(facility.geteFmFmClientBranchPO().getBranchId());
							   branchId.append(","); 
						   }	
						   combinedBranchId = branchId.toString().substring(0, branchId.toString().length()-1);									   
					  }
			    }
			    
			    if(combinedBranchId.trim().replace(",","").isEmpty()){
					  return "0";				
				}else{
					 return combinedBranchId;
				}

		} catch (Exception e) {
			log.debug("combinedBranchId log"+e);
			return "failed";
		}
		  
	}
	
}
