package com.newtglobal.eFmFmFleet.writer;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.services.SchedulingService;

public class AutoCheckoutWriter implements Serializable,ItemWriter<EFmFmVehicleCheckInPO>{
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AutoCheckoutWriter.class);
	private static final int DELAY_THROTTLE = 120;

	SchedulingService scheduleServices;

	public void setentityManagerFactory(
			EntityManagerFactory _entityMangerFactory) {
		scheduleServices = new SchedulingService(_entityMangerFactory);
	}
	public void write(List<? extends EFmFmVehicleCheckInPO> checkInDriverList) throws Exception {
		log.info("DriverAutoCheckOutSender Batch Size: "+checkInDriverList.size());
		if(DELAY_THROTTLE > 0) Thread.sleep(DELAY_THROTTLE);		
		for(EFmFmVehicleCheckInPO  obj:checkInDriverList){	
			
			try{
			scheduleServices.updateVehicleDistanceInDiffCasesEarlyStartAndComplete(obj.getCheckInId());
			}catch(Exception e){
				log.info("Error On adding distance via schedular"+e);
			}
			
			if((obj.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP") && obj.getStatus().equalsIgnoreCase("N") && !(obj.getCheckInType().equalsIgnoreCase("mobile")))){
				scheduleServices.driverTripServiceCalled(obj);
			}
			
			if(obj.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverAutoCheckoutStatus().equalsIgnoreCase("YES")){
			//Greater than 14 Hours and less than 14 hours driver will Auto checkout 
			if(((driverAutoCheckOutTimeAfterTwoHours(obj.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverAutoCheckedoutTime(), 0, obj.getCheckInTime())) < new Date().getTime())  && !(obj.isAdminMailTriggerStatus()) && (!(obj.getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY"))) && obj.getStatus().equalsIgnoreCase("N")){
				   scheduleServices.sendingMailToAdmin(obj);
			}
			//Greater than 13 Hours  driver will Auto checkout 
		    else if(((driverAutoCheckOutTimeAfterOneHour(obj.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverAutoCheckedoutTime(), 0, obj.getCheckInTime())) < new Date().getTime()) && (!(obj.getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY"))) && !(obj.isSupervisorMailTriggerStatus()) && obj.getStatus().equalsIgnoreCase("N")){
				   scheduleServices.sendingMailToSuperVisor(obj);
			}
			//After 11 Hours 45 Minutes(43740000) driver will Auto checkout //for common 15 minutes is buffer time		
			else if((driverAutoCheckOutTimeAfterGivenTime(obj.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverAutoCheckedoutTime(),0, obj.getCheckInTime()) < new Date().getTime()) && (!(obj.getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY"))) && obj.getStatus().equalsIgnoreCase("Y")){
				   scheduleServices.drivetAutoCheckout(obj);
			}
			}
			if(obj.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getAutoVehicleAllocationStatus().equalsIgnoreCase("YES")){
			try{
				if(!(obj.getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY")) && (obj.getEfmFmVehicleMaster().getMonthlyPendingKmUpdateDate().getYear()<new Date().getYear() || (obj.getEfmFmVehicleMaster().getMonthlyPendingKmUpdateDate().getMonth()<(new Date().getMonth())))){
					scheduleServices.updateVehicleMonthlyContractedKmAndDate(obj.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
			}}catch(Exception e){
				log.info("contract distance error"+e);
			}	
			}
		}
		
	}
	public long driverAutoCheckOutTimeAfterOneHour(Time hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours.getHours()+1);
		calendar.add(Calendar.MINUTE, hours.getMinutes());
		return calendar.getTimeInMillis();
	}
	
	public long driverAutoCheckOutTimeAfterTwoHours(Time hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours.getHours()+2);
		calendar.add(Calendar.MINUTE, hours.getMinutes());
		return calendar.getTimeInMillis();
	}
	public long driverAutoCheckOutTimeAfterGivenTime(Time hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours.getHours());
		calendar.add(Calendar.MINUTE, hours.getMinutes());
		return calendar.getTimeInMillis();
	}
}
