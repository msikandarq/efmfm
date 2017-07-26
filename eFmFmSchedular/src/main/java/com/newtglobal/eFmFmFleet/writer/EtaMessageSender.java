package com.newtglobal.eFmFmFleet.writer;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.services.SchedulingService;

public class EtaMessageSender implements Serializable,
		ItemWriter<EFmFmEmployeeTripDetailPO> {
	private static Log log = LogFactory.getLog(EtaMessageSender.class);
	private static final int DELAY_THROTTLE = 100;

	private static final long serialVersionUID = 6086291233792717912L;

	SchedulingService scheduleServices;

	public void setentityManagerFactory(
			EntityManagerFactory _entityMangerFactory) {
		scheduleServices = new SchedulingService(_entityMangerFactory);
	}

	public void write(List<? extends EFmFmEmployeeTripDetailPO> arg0)
			throws Exception {
		log.info("EtaMessageSender Batch Size: "+arg0.size());
		if(DELAY_THROTTLE > 0) Thread.sleep(DELAY_THROTTLE);
		for (EFmFmEmployeeTripDetailPO obj : arg0) {
			if (obj.getEfmFmAssignRoute().getTripType()
					.equalsIgnoreCase("PICKUP")
					&& obj.getEfmFmAssignRoute().getTripStatus()
							.equalsIgnoreCase("Started")) {
				scheduleServices.sendingAutoMsgOnDeviceStop(obj);
			}
		}
	}

}