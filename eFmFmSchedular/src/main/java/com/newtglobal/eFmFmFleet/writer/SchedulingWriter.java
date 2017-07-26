package com.newtglobal.eFmFmFleet.writer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.services.SchedulingService;

public class SchedulingWriter implements Serializable,ItemWriter<EFmFmAssignRoutePO>{
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(SchedulingWriter.class);
	private static final int DELAY_THROTTLE = 120;

	SchedulingService scheduleServices;

	public void setentityManagerFactory(
			EntityManagerFactory _entityMangerFactory) {
		scheduleServices = new SchedulingService(_entityMangerFactory);
	}
	public void write(List<? extends EFmFmAssignRoutePO> assignRoutes) throws Exception {
		log.info("total SchedulingWriter Batch Size: "+assignRoutes.size());
		if(DELAY_THROTTLE > 0) Thread.sleep(DELAY_THROTTLE);
		if(!assignRoutes.isEmpty()){
		//calling scheduling logic after 3 minute of allocation.till the time route creation part will complete		
		if(assignRoutes.get(0).geteFmFmClientBranchPO().getAutoVehicleAllocationStatus().equalsIgnoreCase("YES")){
		if(((assignRoutes.get(0).getCreatedDate().getTime())+120000) < new Date().getTime()){
		for(EFmFmAssignRoutePO  assignRoute:assignRoutes){
				log.info("SchedulingWriter Date"+new Date()+"Distance"+assignRoute.getPlannedDistance());
			scheduleServices.cabScheduling(assignRoute);
		}
		}
		}
	}
	}
}
