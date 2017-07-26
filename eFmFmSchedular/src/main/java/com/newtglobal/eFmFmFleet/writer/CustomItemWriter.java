package com.newtglobal.eFmFmFleet.writer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.services.SchedulingService;

public class CustomItemWriter implements Serializable,
		ItemWriter<EFmFmEmployeeRequestMasterPO> {
	
	private static Log log = LogFactory.getLog(CustomItemWriter.class);
	private static final int DELAY_THROTTLE = 120;

	private static final long serialVersionUID = 8132973191221859821L;
	SchedulingService scheduleServices;

	public void setentityManagerFactory(
			EntityManagerFactory _entityMangerFactory) {
		scheduleServices = new SchedulingService(_entityMangerFactory);
	}

	public void write(List<? extends EFmFmEmployeeRequestMasterPO> arg0)
			throws Exception {
		log.info("CustomItemWriter Batch Size: "+arg0.size());
		if(DELAY_THROTTLE > 0) Thread.sleep(DELAY_THROTTLE);
		DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		for (EFmFmEmployeeRequestMasterPO obj : arg0) {
				if (obj.getStatus().equalsIgnoreCase("Y")
						&& obj.getTripRequestEndDate().getTime() > new Date()
								.getTime()) {
					String requestDate = dateformate.format(obj
							.getTripRequestStartDate());
					String requestDateShiftTime = requestDate + " "
							+ obj.getShiftTime();
					Date shiftDateAndTime = dateTimeFormate
							.parse(requestDateShiftTime);
//					String[] weekOffDays = obj.getEfmFmUserMaster().getWeekOffDays().split(",");
					
				/*	if (shiftDateAndTime.getTime() < new Date().getTime() + 86400000
							&& !(obj.getEfmFmUserMaster().getWeekOffDays()
									.contains((CharSequence) new SimpleDateFormat(
											"EEEE").format(new Date())))
							&& obj.getRequestType().equalsIgnoreCase("normal")) {
						scheduleServices.updateTravelRequest(obj);
					} else if (shiftDateAndTime.getTime() < new Date().getTime() + 86400000
							&& (weekOffDays[weekOffDays.length - 1]
									.equalsIgnoreCase(new SimpleDateFormat("EEEE")
											.format(new Date())))
							&& obj.getRequestType().equalsIgnoreCase("normal")) {
						scheduleServices.updateTravelRequest(obj);
					}*/
//					log.info("CustomItemWriter Batch Size: "+arg0.size());
					
					if (shiftDateAndTime.getTime() < upcomingRequests(new Date(),10)
							&& (obj.getRequestType().equalsIgnoreCase("adhoc") || obj
									.getRequestType().equalsIgnoreCase("guest") || obj
                                    .getRequestType().equalsIgnoreCase("AdhocRequest"))) {
//						log.info("insdie writer");
						scheduleServices.updateTravelRequest(obj);
					}
				} else {
					log.info("insdie disableActiveRequests");
					scheduleServices.disableActiveRequests(obj);
				}
		}

	}
	public long upcomingRequests(Date date, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);
		return calendar.getTimeInMillis();
	}

}