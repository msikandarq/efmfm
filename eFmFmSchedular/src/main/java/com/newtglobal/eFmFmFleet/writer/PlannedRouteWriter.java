package com.newtglobal.eFmFmFleet.writer;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.services.SchedulingService;

public class PlannedRouteWriter implements Serializable,ItemWriter<EFmFmAssignRoutePO>{
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(PlannedRouteWriter.class);
	private static final int DELAY_THROTTLE = 120;

	SchedulingService scheduleServices;

	public void setentityManagerFactory(
			EntityManagerFactory _entityMangerFactory) {
		scheduleServices = new SchedulingService(_entityMangerFactory);
	}
	public void write(List<? extends EFmFmAssignRoutePO> assignRoutes) throws Exception {
		log.info("total assignRoutes PlannedRouteWriter Size: "+assignRoutes.size());
		if(DELAY_THROTTLE > 0) Thread.sleep(DELAY_THROTTLE);
		for(EFmFmAssignRoutePO  assignRoute:assignRoutes){
			if(!(assignRoute.geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP")))
			scheduleServices.routePlanning(assignRoute);		
		}
	}
}
