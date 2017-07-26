package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

public class ReportTestService {

	@Test
	public void escortReportTestFun() {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader
				.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader
				.getContext().getBean("IAssignRouteBO");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = new Date();
		Date toDate = new Date();
		Map<String, Object> allTrips = new HashMap<String, Object>();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		List<Map<String, Object>> escortReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO
				.getAllEscortRequiredTripsByDate(fromDate, toDate, "1");
		if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
			for (EFmFmAssignRoutePO trips : allTripDetails) {
				Map<String, Object> escortReport = new HashMap<String, Object>();
				escortReport.put("tripStartDate",
						formatter.format(trips.getTripStartTime()));
				escortReport.put("tripAssignDate",
						formatter.format(trips.getTripAssignDate()));
				escortReport.put("tripCompleteDate",
						formatter.format(trips.getTripCompleteTime()));
				escortReport.put("shiftTime",
						timeFormat.format(trips.getShiftTime()));
				escortReport.put("tripType", trips.getTripType());
				escortReport.put("vehicleNumber", trips
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getVehicleNumber());
				escortReport.put("vendorName", trips.getEfmFmVehicleCheckIn()
						.getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorName());
				escortReport.put("driverName", trips.getEfmFmVehicleCheckIn()
						.getEfmFmDriverMaster().getFirstName());
				escortReport.put("driverId", trips.getEfmFmVehicleCheckIn()
						.getEfmFmDriverMaster().getDriverId());

				try {
					int a = trips.geteFmFmEscortCheckIn().getEscortCheckInId();
					escortReport.put("escortName", trips
							.geteFmFmEscortCheckIn().geteFmFmEscortMaster()
							.getFirstName());
					escortReport.put("escortId", trips.geteFmFmEscortCheckIn()
							.geteFmFmEscortMaster().getEscortId());
				} catch (Exception e) {
					escortReport.put("escortName",
							"Escort Required But Not Available");
					escortReport.put("escortId", "NA");
				}
				escortReport.put("routeName", trips.geteFmFmRouteAreaMapping()
						.geteFmFmZoneMaster().getZoneName());
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (trips.getTripType().equalsIgnoreCase("DROP")) {
					employeeTripDetailPO = iCabRequestBO
							.getDropTripAllSortedEmployees(trips
									.getAssignRouteId());
					escortReport.put("pickOrDropTime", timeFormat
							.format(employeeTripDetailPO.get(
									employeeTripDetailPO.size() - 1)
									.getCabstartFromDestination()));
				} else {
					employeeTripDetailPO = iCabRequestBO
							.getParticularTripAllEmployees(trips
									.getAssignRouteId());
					escortReport.put("pickOrDropTime", timeFormat
							.format(employeeTripDetailPO.get(0)
									.getCabstartFromDestination()));
				}
				escortReport.put("employeeName", employeeTripDetailPO.get(0)
						.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
						.getFirstName());
				escortReport.put("employeeId", employeeTripDetailPO.get(0)
						.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
						.getEmployeeId());
				escortReportList.add(escortReport);
			}
		}
		allTrips.put("tripDetail", escortReportList);
		System.out.println("Escort Report details" + escortReportList);
	}

}
