package com.newtglobal.eFmFmFleet.writer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.services.SchedulingService;

public class AutoRequestWriter implements Serializable,
		ItemWriter<EFmFmEmployeeTravelRequestPO> {
	
	private static Log log = LogFactory.getLog(AutoRequestWriter.class);
	private static final int DELAY_THROTTLE = 120;

	private static final long serialVersionUID = 8132973191221859821L;
	SchedulingService scheduleServices;

	public void setentityManagerFactory(
			EntityManagerFactory _entityMangerFactory) {
		scheduleServices = new SchedulingService(_entityMangerFactory);
	}

	public void write(List<? extends EFmFmEmployeeTravelRequestPO> arg0)
			throws Exception {
		log.info("AutoRequestWriter Batch Size: "+arg0.size());
		if(DELAY_THROTTLE > 0) Thread.sleep(DELAY_THROTTLE);
		DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		for (EFmFmEmployeeTravelRequestPO obj : arg0) {
			String requestDate = dateformate.format(obj
					.getRequestDate());
			String requestDateShiftTime = requestDate + " "
					+ obj.getShiftTime();
			Date shiftDateAndTime = dateTimeFormate
					.parse(requestDateShiftTime);
			//after 30 mintes when shift time passes
			if((shiftDateAndTime.getTime()+1800000) < new Date().getTime()){
				scheduleServices.disableRequestsFromTravelRequest(obj);
			}				
		}

	}

}