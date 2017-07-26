package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Map;

import org.junit.Test;
import org.junit.Test;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

public class ApprovalTestService {

	@Test
	public void getAllUnapprovedDriversTest() {
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext()
				.getBean("IApprovalBO");
		List<EFmFmDriverMasterPO> unapprovedDrivers = approvalBO
				.getAllUnapprovedDrivers("1");
				//For testing
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (unapprovedDrivers != null) {
			for (EFmFmDriverMasterPO pending : unapprovedDrivers) {
				Map<String, Object> allPendingRequests = new HashMap<String, Object>();
				allPendingRequests.put("mobileNumber",
						pending.getMobileNumber());
				allPendingRequests.put("vendorName", pending
						.getEfmFmVendorMaster().getVendorName());
				allPendingRequests.put("name", pending.getFirstName());
				allPendingRequests.put("driverId", pending.getDriverId());
				requests.add(allPendingRequests);
			}
			System.out.println("requests" + requests.toString());
		}

	}

	@Test
	public void getAllRejectedDriversTest() {
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext()
				.getBean("IApprovalBO");
		List<EFmFmDriverMasterPO> inactiveDrivers = approvalBO
				.getAllInActiveDrivers("1");
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (inactiveDrivers != null) {
			for (EFmFmDriverMasterPO pending : inactiveDrivers) {
				Map<String, Object> allPendingRequests = new HashMap<String, Object>();
				allPendingRequests.put("mobileNumber",
						pending.getMobileNumber());
				allPendingRequests.put("vendorName", pending
						.getEfmFmVendorMaster().getVendorName());
				allPendingRequests.put("name", pending.getFirstName());
				allPendingRequests.put("driverId", pending.getDriverId());
				requests.add(allPendingRequests);
			}
		}
		System.out.println("requests" + requests.toString());

	}

	@Test
	public void getAllapprovedDriversTest() {

		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext()
				.getBean("IApprovalBO");
		List<EFmFmDriverMasterPO> approvedDrivers = approvalBO
				.getAllApprovedDrivers(1);
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (approvedDrivers != null) {
			for (EFmFmDriverMasterPO pending : approvedDrivers) {
				Map<String, Object> allPendingRequests = new HashMap<String, Object>();
				allPendingRequests.put("mobileNumber",
						pending.getMobileNumber());
				allPendingRequests.put("vendorName", pending
						.getEfmFmVendorMaster().getVendorName());
				allPendingRequests.put("name", pending.getFirstName());
				allPendingRequests.put("driverId", pending.getDriverId());
				requests.add(allPendingRequests);
			}
		}
		System.out.println("getAllapprovedDriversTest" + requests.toString());
	}

}