package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFuelChargesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractInvoicePO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorFuelContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/contract")
@Consumes("application/json")
@Produces("application/json")
public class InvoiceService {

	private static Log log = LogFactory.getLog(InvoiceService.class);

	@Context
	private HttpServletRequest httpRequest;
	JwtTokenGenerator token = new JwtTokenGenerator();

	@POST
	@Path("/invoiceTripDetails")
	public Response invoiceTripDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		Map<String, Object> InvoiceDetails = new HashMap<String, Object>();
		List<EFmFmClientBranchPO> clientBranch = userMasterBO
				.getClientDetails(String.valueOf(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId()));
		if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("SBOCHN")) {
			InvoiceDetails = shellchennaiInvoice(eFmFmVehicleMasterPO);
		} else if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("SBOManila")) {
			InvoiceDetails = shellchennaiInvoice(eFmFmVehicleMasterPO);
		} else if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("SBOBNG")) {
			InvoiceDetails = shellchennaiInvoice(eFmFmVehicleMasterPO);
		} else if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("GNPTJP")) {
			InvoiceDetails = shellchennaiInvoice(eFmFmVehicleMasterPO);
		} else if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("Newt")) {
			InvoiceDetails = shellchennaiInvoice(eFmFmVehicleMasterPO);
		} else if (clientBranch.get(0).getBranchCode() == null || clientBranch.get(0).getBranchCode() == "") {
			InvoiceDetails.put("failed", "WRONGTRANSPORTCODE");
		} else {
			InvoiceDetails = shellchennaiInvoice(eFmFmVehicleMasterPO);
		}
		return Response.ok(InvoiceDetails, MediaType.APPLICATION_JSON).build();
	}

	public Map<String, Object> shellchennaiInvoice(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat monthDate = new SimpleDateFormat("MM-yyyy");
		List<Map<String, Object>> invoiceVendorDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		Map<String, Object> fixedContractDistanceDetails = new HashMap<String, Object>();
		String activityType = eFmFmVehicleMasterPO.getActionType();
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		List<EFmFmClientBranchPO> clientBranch = userMasterBO
				.getClientDetails(String.valueOf(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId()));
		long randomNumber = 0;
		String diffNumber = "";
		int invoiceNumRange = 0, fixedDisatanceId = 0;
		boolean validContract = false, notCloneId = true;
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		List<EFmFmAssignRoutePO> assignRouteDetail = null;
		try {
			invoiceNumRange = userMasterBO.getBranchInvoiceNumberDigitRangeFromBranchId(
					eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		} catch (Exception e) {
			log.info("invoice Num" + invoiceNumRange);
		}
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < invoiceNumRange; i++) {
			diffNumber += numbers.get(i).toString();
		}
		randomNumber = Long.parseLong(diffNumber);
		log.info("invoice Num" + invoiceNumRange + "randomNumber" + randomNumber);
		Date fromDate = null;
		Date toDate = null;
		Date systemDate = new Date();
		String distanceFlg = eFmFmVehicleMasterPO.getDistanceFlg();
		if (!(activityType.toUpperCase().equalsIgnoreCase("INVOICEDETAILS"))) {
			if (eFmFmVehicleMasterPO.getInvoiceDate() != null) {
				if (clientBranch.get(0).getInvoiceGenType().equalsIgnoreCase("Month")) {
					fromDate = monthDate.parse(eFmFmVehicleMasterPO.getInvoiceDate());
					toDate = monthDate.parse(eFmFmVehicleMasterPO.getInvoiceDate());
					Format year = new SimpleDateFormat("yyyy");
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.DATE, clientBranch.get(0).getInvoiceGenDate());
					cal.set(Calendar.MONTH, fromDate.getMonth());
					cal.set(Calendar.YEAR, Integer.valueOf(year.format(fromDate)));
					cal.set(Calendar.DATE, clientBranch.get(0).getInvoiceGenDate());
					log.info(" ---Min Date::---" + cal.getTime());
					fromDate = cal.getTime();
					cal.set(Calendar.DATE,
							cal.getActualMaximum(Calendar.DATE) + (clientBranch.get(0).getInvoiceGenDate() - 1));
					log.info(" ---Date::---" + cal.getTime() + "yesr" + monthDate.format(cal.getTime()));
					toDate = cal.getTime();
					if (toDate.getTime() >= systemDate.getTime()) {
						fixedDistanceVehicleDetails.put("failed", "INVALIDGENTYPE");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return fixedDistanceVehicleDetails;
					}
				} else if (clientBranch.get(0).getInvoiceGenType().equalsIgnoreCase("Date")) {
					fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
					toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
					Format year = new SimpleDateFormat("yyyy");
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.DATE, clientBranch.get(0).getInvoiceGenDate());
					cal.set(Calendar.MONTH, fromDate.getMonth());
					cal.set(Calendar.YEAR, Integer.valueOf(year.format(fromDate)));
					cal.set(Calendar.DATE, clientBranch.get(0).getInvoiceGenDate());
					log.info(" ---Min Date::---" + cal.getTime());
					fromDate = cal.getTime();
					cal.set(Calendar.DATE,
							cal.getActualMaximum(Calendar.DATE) + (clientBranch.get(0).getInvoiceGenDate() - 1));
					log.info(" ---Date::---" + cal.getTime() + "yesr" + monthDate.format(cal.getTime()));
					toDate = cal.getTime();
					if (toDate.getTime() >= systemDate.getTime()) {
						fixedDistanceVehicleDetails.put("failed", "INVALIDGENTYPE");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return fixedDistanceVehicleDetails;
					}
				} else {
					fixedDistanceVehicleDetails.put("failed", "INVALIDGENTYPE");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
					return fixedDistanceVehicleDetails;
				}

			} else {
				fixedDistanceVehicleDetails.put("failed", "INVALIDDATE");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return fixedDistanceVehicleDetails;
			}

			if (eFmFmVehicleMasterPO.getInvoiceAcptType().equalsIgnoreCase("N")
					&& activityType.equalsIgnoreCase("VENDORBASED")) {

				List<EFmFmVendorContractInvoicePO> alreadyInvoiceCreatedVendor = iVehicleCheckInBO
						.getInvoiceforVendorByGroupDistance(fromDate, toDate, clientBranchPO.getBranchId(),
								eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
								eFmFmVehicleMasterPO.getDistanceFlg());
				if (alreadyInvoiceCreatedVendor.isEmpty()) {
					fixedDistanceVehicleDetails.put("failed", "NOTGENINV");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
					return fixedDistanceVehicleDetails;
				} else {
					List<EFmFmFixedDistanceContractDetailPO> vehicleContractDetails = iVehicleCheckInBO
							.getAllVehicleContractDetails(fromDate, toDate, clientBranchPO.getBranchId(),
									eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId());
					if (vehicleContractDetails.size() == alreadyInvoiceCreatedVendor.size()) {
						fixedDistanceVehicleDetails.put("failed", "ALREADYGENINV");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return fixedDistanceVehicleDetails;
					} else {
						fixedDistanceVehicleDetails.put("failed", "REMAININGVEHICLES");
						// fixedDistanceVehicleDetails.put("failed","ALREADYGENINV");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return fixedDistanceVehicleDetails;
					}
				}
			} else if (eFmFmVehicleMasterPO.getInvoiceAcptType().equalsIgnoreCase("N")
					&& activityType.equalsIgnoreCase("VEHICLEBASED")) {

				List<EFmFmVendorContractInvoicePO> alreadyInvoiceCreatedVehicle = iVehicleCheckInBO
						.getInvoiceByVehicleFixedDistance(fromDate, toDate, clientBranchPO.getBranchId(),
								eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
								eFmFmVehicleMasterPO.getVehicleId(), eFmFmVehicleMasterPO.getDistanceFlg());

				if (alreadyInvoiceCreatedVehicle.isEmpty()) {
					fixedDistanceVehicleDetails.put("failed", "NOTGENINV");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
					return fixedDistanceVehicleDetails;
				} else {
					fixedDistanceVehicleDetails.put("failed", "ALREADYGENINV");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
					return fixedDistanceVehicleDetails;
				}
			}
		}
		switch (activityType.toUpperCase().trim()) {
		case "VENDORBASED":
			log.info("fromDate" + fromDate);
			log.info("toDate" + toDate);
			List<EFmFmVendorContractInvoicePO> listInvoiceDetails = iVehicleCheckInBO
					.getInvoiceforVendorByGroupDistance(fromDate, toDate, clientBranchPO.getBranchId(),
							eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
							eFmFmVehicleMasterPO.getDistanceFlg());
			log.info("listInvoiceDetails" + listInvoiceDetails.size());
			// if (listInvoiceDetails.isEmpty()) {
			List<EFmFmFixedDistanceContractDetailPO> fixedDistanceVendorValidationDetails = iVehicleCheckInBO
					.getFixedContractDetailsValidation(fromDate, toDate, clientBranchPO.getBranchId(),
							eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId());
			if (fixedDistanceVendorValidationDetails.isEmpty()) {
				fixedDistanceVehicleDetails.put("failed", "NOTFOUNDCONTRACT");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return fixedDistanceVehicleDetails;
			}
			List<EFmFmVehicleMasterPO> allVehiclesDetail = iVehicleCheckInBO.getAllApprovedVehiclesByVendorId(
					eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(), clientBranchPO.getBranchId());
			if (!(allVehiclesDetail.isEmpty())) {
				// start
				for (EFmFmVehicleMasterPO vehicleDetails : allVehiclesDetail) {
					System.out.println("vehicleDetails" + vehicleDetails.getVehicleNumber());
					if (vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType()
							.equalsIgnoreCase("FDC")
							|| vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
									.getContractType().equalsIgnoreCase("PDDC")) {
						fixedContractDistanceDetails = processInvoice(
								vehicleDetails.getEfmFmVendorMaster().getVendorId(),
								vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
										.getContractType(),
								clientBranchPO.getBranchId(), fromDate, toDate, allVehiclesDetail.get(0)
										.getEfmFmVendorMaster().geteFmFmClientBranchPO().getInvoiceGenDate());
						if (fixedContractDistanceDetails.isEmpty()) {
							validContract = true;
						} else {
							for (Map.Entry<String, Object> entry : fixedContractDistanceDetails.entrySet()) {
								if (entry.getKey().equalsIgnoreCase("cloneId")) {
									notCloneId = false;
									fixedDisatanceId = (int) entry.getValue();
									validContract = true;
									System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
								} else {
									log.info("not valid contract");
									fixedDistanceVehicleDetails.put("failed", entry.getValue());
									log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
									return fixedDistanceVehicleDetails;
								}

							}
						}
						if (validContract) {

							List<EFmFmVendorContractInvoicePO> alreadyCreatedForVehicle = iVehicleCheckInBO
									.getInvoiceByVehicleFixedDistance(fromDate, toDate, clientBranchPO.getBranchId(),
											eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
											vehicleDetails.getVehicleId(), eFmFmVehicleMasterPO.getDistanceFlg());
							if (alreadyCreatedForVehicle.isEmpty()) {

								assignRouteDetail = iAssignRouteBO
										.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate, toDate,
												clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());

								if (!(assignRouteDetail.isEmpty())) {
									for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
										log.info("vehicle NUmber" + vehicleDetails.getVehicleNumber());
										double totalAmt = 0.0, totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0,
												penaltyAmt = 0.0, penaltyTotalAmt = 0.0, tripBasedAmount = 0.0,
												extraKmCharges = 0.0, tripAmount = 0.0, extraKm = 0.0,
												panalityAmount = 0.0, totalKmAmount = 0.0;
										int absentDays = 0;
										// Vehicle belongs to a particular
										// contract type
										if (notCloneId) {
											fixedDisatanceId = allVehicleDetails.getEfmFmVehicleCheckIn()
													.getEfmFmVehicleMaster().geteFmFmContractDetails()
													.getDistanceContractId();
										}
										List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
												.getFixedDistanceDetails(fixedDisatanceId,
														clientBranchPO.getBranchId());

										List<EFmFmVehicleCheckInPO> totalWorkingDays = iVehicleCheckInBO
												.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate,
														clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());
										log.info("total working days" + totalWorkingDays.size());
										List<EFmFmVehicleMasterPO> SumOftotalKm = null;
										if (distanceFlg.equalsIgnoreCase("GPS")) {
											SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicle(fromDate, toDate,
													clientBranchPO.getBranchId(),
													allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
															.getVehicleId(),
													fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
															.getContractType().trim(),
													fixedDistanceDetails.get(0).getDistanceContractId());
										} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
											SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicleOdometer(fromDate,
													toDate, clientBranchPO.getBranchId(),
													allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
															.getVehicleId(),
													fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
															.getContractType().trim(),
													fixedDistanceDetails.get(0).getDistanceContractId());
										}
										log.info("SumOftotalKm" + SumOftotalKm.size());
										double totatSumOfKmAmount = 0.0;
										if (!(fixedDistanceDetails.get(0).getFuelPriceCalculation()
												.equalsIgnoreCase("NR"))) {
											totatSumOfKmAmount = fuelCalculation(fromDate, toDate, allVehicleDetails,
													clientBranchPO, fixedDistanceDetails,
													SumOftotalKm.get(0).getSumTravelledDistance(), distanceFlg);
										}
										if (vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
												.getContractType().equalsIgnoreCase("FDC")) {
											if (clientBranch.get(0).getInvoiceGenType().equalsIgnoreCase("Month")) {
												if (SumOftotalKm.get(0)
														.getSumTravelledDistance() >= fixedDistanceDetails.get(0)
																.getFixedDistanceMonthly()) {
													extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
															- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
													extraKmCharges = extraKm
															* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
													totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
															+ extraKmCharges + totatSumOfKmAmount;
													absentDays = fixedDistanceDetails.get(0).getMinimumDays()
															- totalWorkingDays.size();

													if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")
															&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																	.getMinimumDays()) {
														log.info("absentDays" + absentDays);
														penaltyAmt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														penaltyTotalAmt = (penaltyAmt
																+ (penaltyAmt * fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay()) / 100);
														log.info("penaltyTotalAmt" + penaltyTotalAmt);
														penalityPerday = (((fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay())
																/ 100) * absentDays;
														log.info("penalityPerday" + penalityPerday);
														totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
														// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
														totalAmt = totalAmt - penaltyTotalAmt;
														log.info("totalAmt" + Math.round(totalAmt));
													} else if (fixedDistanceDetails.get(0).getPenalty()
															.equalsIgnoreCase("N")
															&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																	.getMinimumDays()) {
														totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														totalAmt = totalAmt - totalPerDayDeductionAmnt;
														penaltyTotalAmt = totalPerDayDeductionAmnt;
													}

												} else if (SumOftotalKm.get(0)
														.getSumTravelledDistance() < fixedDistanceDetails.get(0)
																.getFixedDistanceMonthly()
														&& totalWorkingDays.size() >= fixedDistanceDetails.get(0)
																.getMinimumDays()) {

													totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
															+ totatSumOfKmAmount;

												} else if (SumOftotalKm.get(0)
														.getSumTravelledDistance() < fixedDistanceDetails.get(0)
																.getFixedDistanceMonthly()
														&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																.getMinimumDays()) {

													totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
															+ totatSumOfKmAmount;
													absentDays = fixedDistanceDetails.get(0).getMinimumDays()
															- totalWorkingDays.size();

													if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")
															&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																	.getMinimumDays()) {

														log.info("absentDays" + absentDays);
														penaltyAmt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														log.info("panalityAmount" + panalityAmount);
														penaltyTotalAmt = (penaltyAmt
																+ (penaltyAmt * fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay()) / 100);
														log.info("penaltyTotalAmt" + penaltyTotalAmt);
														penalityPerday = (((fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay())
																/ 100) * absentDays;
														log.info("penalityPerday" + penalityPerday);
														totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
														// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
														totalAmt = totalAmt - penaltyTotalAmt;
														log.info("totalAmt" + totalAmt);
													} else if (fixedDistanceDetails.get(0).getPenalty()
															.equalsIgnoreCase("N")
															&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																	.getMinimumDays()) {
														totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														totalAmt = totalAmt - totalPerDayDeductionAmnt;
														penaltyTotalAmt = totalPerDayDeductionAmnt;
													}
												}

											} else if (clientBranch.get(0).getInvoiceGenType()
													.equalsIgnoreCase("Date")) {

												if (SumOftotalKm.get(0)
														.getSumTravelledDistance() >= fixedDistanceDetails.get(0)
																.getFixedDistanceMonthly()) {
													extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
															- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
													extraKmCharges = extraKm
															* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
													totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
															+ extraKmCharges + totatSumOfKmAmount;
													absentDays = fixedDistanceDetails.get(0).getMinimumDays()
															- totalWorkingDays.size();
													if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")
															&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																	.getMinimumDays()) {
														log.info("absentDays" + absentDays);
														penaltyAmt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														penaltyTotalAmt = (penaltyAmt
																+ (penaltyAmt * fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay()) / 100);
														log.info("penaltyTotalAmt" + penaltyTotalAmt);
														penalityPerday = (((fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay())
																/ 100) * absentDays;
														log.info("penalityPerday" + penalityPerday);
														totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
														// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
														totalAmt = totalAmt - penaltyTotalAmt;
														log.info("totalAmt" + Math.round(totalAmt));
													}

												} else if (SumOftotalKm.get(0)
														.getSumTravelledDistance() < fixedDistanceDetails.get(0)
																.getFixedDistanceMonthly()
														&& totalWorkingDays.size() >= fixedDistanceDetails.get(0)
																.getMinimumDays()) {

													totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
															+ totatSumOfKmAmount;

												} else if (SumOftotalKm.get(0)
														.getSumTravelledDistance() < fixedDistanceDetails.get(0)
																.getFixedDistanceMonthly()
														&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
																.getMinimumDays()) {

													totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
															+ totatSumOfKmAmount;
													absentDays = fixedDistanceDetails.get(0).getMinimumDays()
															- totalWorkingDays.size();

													if (fixedDistanceDetails.get(0).getPenalty()
															.equalsIgnoreCase("Y")) {

														log.info("absentDays" + absentDays);
														penaltyAmt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														log.info("panalityAmount" + panalityAmount);
														penaltyTotalAmt = (penaltyAmt
																+ (penaltyAmt * fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay()) / 100);
														log.info("penaltyTotalAmt" + penaltyTotalAmt);
														penalityPerday = (((fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* fixedDistanceDetails.get(0)
																		.getPenaltyInPercentagePerDay())
																/ 100) * absentDays;
														log.info("penalityPerday" + penalityPerday);
														totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
																.getFixedDistanceChargeRate()
																/ fixedDistanceDetails.get(0).getMinimumDays())
																* absentDays;
														log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
														// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
														totalAmt = totalAmt - penaltyTotalAmt;
														log.info("totalAmt" + totalAmt);
													}
												}

											}

										} else if (vehicleDetails.geteFmFmContractDetails()
												.geteFmFmVendorContractTypeMaster().getContractType()
												.equalsIgnoreCase("PDDC")) {

											totalAmt = fixedDistanceDetails.get(0).getPerDayCost()
													* totalWorkingDays.size();
											tripAmount = totalAmt;
											totalKmAmount = (SumOftotalKm.get(0).getSumTravelledDistance()
													* fixedDistanceDetails.get(0).getPerKmCost());
											totalAmt = totalAmt + totatSumOfKmAmount + totalKmAmount;
											absentDays = fixedDistanceDetails.get(0).getMinimumDays()
													- totalWorkingDays.size();
											if ((totalWorkingDays.size() < fixedDistanceDetails.get(0).getMinimumDays())
													&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
												absentDays = fixedDistanceDetails.get(0).getMinimumDays()
														- totalWorkingDays.size();
												log.info("absentDays" + absentDays);
												penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												log.info("panalityAmount" + panalityAmount);
												penaltyTotalAmt = penaltyAmt + ((penaltyAmt
														* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
														/ 100);
												log.info("penaltyTotalAmt" + penaltyTotalAmt);
												totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												penalityPerday = (((fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays())
														* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
														/ 100) * absentDays;
												log.info("penalityPerday" + penalityPerday);
												totalAmt = totalAmt - penaltyTotalAmt;
												log.info("totalAmt" + totalAmt);

											} else {
												if (totalWorkingDays.size() < fixedDistanceDetails.get(0)
														.getMinimumDays()) {
													totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
															.getFixedDistanceChargeRate()
															/ fixedDistanceDetails.get(0).getMinimumDays())
															* absentDays;
													totalAmt = totalAmt - totalPerDayDeductionAmnt;
													penaltyTotalAmt = totalPerDayDeductionAmnt;
												}
											}

										}

										System.out.println("totalSumOfAmount+" + totatSumOfKmAmount);
										addInvoiceRecord(totalPerDayDeductionAmnt, penalityPerday, tripBasedAmount,
												allVehicleDetails, clientBranchPO, fromDate, toDate, totalAmt,
												penaltyTotalAmt, totalWorkingDays.size(),
												SumOftotalKm.get(0).getSumTravelledDistance(),
												fixedDistanceDetails.get(0).getFixedDistanceMonthly(),
												fixedDistanceDetails.get(0).getMinimumDays(),
												fixedDistanceDetails.get(0).getFixedDistanceChargeRate(), randomNumber,
												allVehicleDetails.getTravelledDistance(), extraKm, extraKmCharges,
												vehicleDetails, fixedDistanceDetails.get(0).getDistanceContractId(),
												totatSumOfKmAmount, eFmFmVehicleMasterPO.getDistanceFlg(),
												fixedDistanceDetails.get(0).getPerDayCost(), totalKmAmount,
												eFmFmVehicleMasterPO.getUserId(), tripAmount);
									}

								}

							}
						} else {
							fixedDistanceVehicleDetails.put("failed", "INVALIDGENTYPE");
							log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
							return fixedDistanceVehicleDetails;
						}
					} else {
						fixedDistanceVehicleDetails.put("failed", "INVALIDCONTRACTTYPE");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return fixedDistanceVehicleDetails;
					}
				}
			} // end code
			// }
			List<Map<String, Object>> tripBasedVehicleDetails = new ArrayList<Map<String, Object>>();
			List<EFmFmVendorContractInvoicePO> vendorInvoiceDetails = iVehicleCheckInBO.getInvoiceforVendor(fromDate,
					toDate, clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
					eFmFmVehicleMasterPO.getDistanceFlg());
			if (!(vendorInvoiceDetails.isEmpty())) {
				double totalAmount = 0.0, penalty = 0.0, tripAmount = 0.0, totalBaseAmount = 0.0, serviceTax = 0.0,
						totalServiceTaxAmount = 0.0;
				int noOfvehicle = 0, contractCount = 0;
				String contractTypeValue = null;
				Set contrctSetCount = new HashSet();
				for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : vendorInvoiceDetails) {
					contrctSetCount.add(vendorInvoiceDetailsList.getInvoiceType());
					fixedDistanceVehicleDetails.put("contractTypeCount", contrctSetCount.size());
					fixedDistanceVehicleDetails.put("vendorName",
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					fixedDistanceVehicleDetails.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
					fixedDistanceVehicleDetails.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
					fixedDistanceVehicleDetails.put("invoiceMonthDate",
							monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
					fixedDistanceVehicleDetails.put("invoiceCreationDate",
							formatter.format(vendorInvoiceDetailsList.getCreationTime()));
					fixedDistanceVehicleDetails.put("contractType", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
							.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType());
					penalty = penalty + vendorInvoiceDetailsList.getTotalDeductibles();
					if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")
							|| vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
									.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
						Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
						totalAmount = totalAmount + vendorInvoiceDetailsList.getTotalAmountPayable();
						totalBaseAmount = totalBaseAmount + vendorInvoiceDetailsList.getBaseTotal();
						fixedDistanceTrips.put("invoiceType", vendorInvoiceDetailsList.getInvoiceType());
						fixedDistanceTrips.put("vehicleNumber",
								vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
						fixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
						fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
						fixedDistanceTrips.put("distanceFlg", vendorInvoiceDetailsList.getDistanceFlg());
						fixedDistanceTrips.put("totalKm", vendorInvoiceDetailsList.getTotalDistance());
						fixedDistanceTrips.put("extraKm", vendorInvoiceDetailsList.getTotalExtraDistance());
						fixedDistanceTrips.put("tripConsolidatedAmount", vendorInvoiceDetailsList.getTripTotalAmount());
						fixedDistanceTrips.put("contractedKm", vendorInvoiceDetailsList.getBaseDistance());
						fixedDistanceTrips.put("totalPerDayDeduction",
								vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());
						fixedDistanceTrips.put("totalpenalityAmnt", vendorInvoiceDetailsList.getTotalPenalityAmount());
						fixedDistanceTrips.put("totalDeduction", vendorInvoiceDetailsList.getTotalDeductibles());
						fixedDistanceTrips.put("totalWorkingDays", vendorInvoiceDetailsList.getPresentDays());

						// PDDC
						fixedDistanceTrips.put("totalKmAmount",
								Math.round(vendorInvoiceDetailsList.getTotalKmAmount() * 100) / 100.00);
						fixedDistanceTrips.put("contractPerDayAmount",
								Math.round(vendorInvoiceDetailsList.getPerDayCost() * 100) / 100.00);
						fixedDistanceTrips.put("contractAmount", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("fixedcharges", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("penalty", vendorInvoiceDetailsList.getTotalDeductibles());
						if (vendorInvoiceDetailsList.getInvoiceRemarks() == null) {
							fixedDistanceTrips.put("invoiceRemarks", "NA");
						} else {
							fixedDistanceTrips.put("invoiceRemarks", vendorInvoiceDetailsList.getInvoiceRemarks());
						}
						fixedDistanceTrips.put("extraKmcharges", vendorInvoiceDetailsList.getExtraDistanceCharge());
						fixedDistanceTrips.put("totalAmount", vendorInvoiceDetailsList.getTotalAmountPayable());
						fixedDistanceTrips.put("fuelExtraAmount", vendorInvoiceDetailsList.getFuelExtraAmount());
						fixedDistanceTrips.put("approvalStatus", vendorInvoiceDetailsList.getApprovalStatus());
						fixedDistanceTrips.put("perKmCost", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.geteFmFmContractDetails().getPerKmCost());
						fixedDistanceTrips.put("contractType", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType());
						if (vendorInvoiceDetailsList.getPresentDays() < vendorInvoiceDetails.get(0).getWorkingDays()) {
							fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
									- vendorInvoiceDetailsList.getPresentDays()));
						} else {
							fixedDistanceTrips.put("absentDays", "NO");
						}
						fixedDistanceTrips.put("totalAmountExtraKmCharge", "0");
						invoiceVendorDetails.add(fixedDistanceTrips);
						noOfvehicle++;
					} else if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("TDC")) {
						noOfvehicle++;
						List<EFmFmVendorContractInvoicePO> vendorFixedInvoiceDetails = iVehicleCheckInBO
								.getInvoiceTripBasedVehicle(fromDate, toDate, clientBranchPO.getBranchId(),
										vendorInvoiceDetailsList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getVehicleId());
						if (!(vendorFixedInvoiceDetails.isEmpty())) {
							for (EFmFmVendorContractInvoicePO fixedTripList : vendorFixedInvoiceDetails) {
								Map<String, Object> tripFixedDistanceTrips = new HashMap<String, Object>();
								totalBaseAmount = totalBaseAmount + fixedTripList.getBaseTotal();
								tripAmount = tripAmount + fixedTripList.getTripTotalAmount();
								tripFixedDistanceTrips.put("invoiceType", fixedTripList.getInvoiceType());
								tripFixedDistanceTrips.put("vehicleNumber",
										fixedTripList.getEfmFmVehicleMaster().getVehicleNumber());
								tripFixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
								tripFixedDistanceTrips.put("invoiceNumber",
										vendorInvoiceDetailsList.getInvoiceNumber());

								tripFixedDistanceTrips.put("tripId",
										fixedTripList.getEfmFmAssignRoute().getAssignRouteId());
								tripFixedDistanceTrips.put("totalKm", fixedTripList.getTotalDistance());
								tripFixedDistanceTrips.put("extraKm", fixedTripList.getTotalExtraDistance());
								tripFixedDistanceTrips.put("fixedcharges", fixedTripList.getBaseTotal());
								tripFixedDistanceTrips.put("extraKmcharges", fixedTripList.getExtraDistanceCharge());
								tripFixedDistanceTrips.put("totalAmount", fixedTripList.getTripTotalAmount());
								tripBasedVehicleDetails.add(tripFixedDistanceTrips);
							}

						}

					}
				}

				serviceTax = (totalAmount + tripAmount) * vendorInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax() / 100;
				totalServiceTaxAmount = (totalAmount + tripAmount) + serviceTax;
				fixedDistanceVehicleDetails.put("totalAmount", (double) Math.round(totalBaseAmount * 100) / 100);
				fixedDistanceVehicleDetails.put("penalty", (double) Math.round(penalty * 100) / 100);
				fixedDistanceVehicleDetails.put("totalPayableAmount",
						(double) Math.round((totalAmount + tripAmount) * 100) / 100);
				fixedDistanceVehicleDetails.put("serviceTax", vendorInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax());
				fixedDistanceVehicleDetails.put("serviceTaxAmount", (double) Math.round(serviceTax * 100) / 100);
				fixedDistanceVehicleDetails.put("total", (double) Math.round(totalServiceTaxAmount * 100) / 100);
				fixedDistanceVehicleDetails.put("noOfvehicle", noOfvehicle);
				fixedDistanceVehicleDetails.put("fixedDistanceBased", invoiceVendorDetails);
				fixedDistanceVehicleDetails.put("tripBasedFixedDetails", tripBasedVehicleDetails);

			} else {
				fixedDistanceVehicleDetails.put("failed", "NODATA");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return fixedDistanceVehicleDetails;
			}
			break;
		case "VEHICLEBASED":
			/*
			 * List<EFmFmVendorContractInvoicePO> listInvoiceDetailsForVehicle =
			 * iVehicleCheckInBO .getInvoiceforVendorByGroupDistance(fromDate,
			 * toDate, clientBranchPO.getBranchId(),
			 * eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
			 * eFmFmVehicleMasterPO.getDistanceFlg());
			 */
			List<EFmFmVendorContractInvoicePO> listInvoiceDetailsForVehicle = iVehicleCheckInBO
					.getInvoiceByVehicleFixedDistance(fromDate, toDate, clientBranchPO.getBranchId(),
							eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
							eFmFmVehicleMasterPO.getVehicleId(), eFmFmVehicleMasterPO.getDistanceFlg());
			log.info("listInvoiceDetails" + listInvoiceDetailsForVehicle.size());
			if (listInvoiceDetailsForVehicle.isEmpty()) {
				// Get all vehicles by Vendor Id and branchId
				List<EFmFmFixedDistanceContractDetailPO> fixedDistanceVehicleValidationDetails = iVehicleCheckInBO
						.getFixedContractDetailsVehicleValidation(fromDate, toDate, clientBranchPO.getBranchId(),
								eFmFmVehicleMasterPO.getVehicleId());
				if (fixedDistanceVehicleValidationDetails.isEmpty()) {
					fixedDistanceVehicleDetails.put("failed", "NOTFOUNDCONTRACT");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
					return fixedDistanceVehicleDetails;
				}
				assignRouteDetail = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate,
						toDate, clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getVehicleId());

				if (!(assignRouteDetail.isEmpty())) {
					if (assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType()
							.equalsIgnoreCase("FDC")
							|| assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
									.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType()
									.equalsIgnoreCase("PDDC")) {
						fixedContractDistanceDetails = processInvoice(
								assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
										.getEfmFmVendorMaster().getVendorId(),
								assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
										.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType(),
								clientBranchPO.getBranchId(), fromDate, toDate,
								assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
										.getEfmFmVendorMaster().geteFmFmClientBranchPO().getInvoiceGenDate());
						if (fixedContractDistanceDetails.isEmpty()) {
							validContract = true;
						} else {
							for (Map.Entry<String, Object> entry : fixedContractDistanceDetails.entrySet()) {
								if (entry.getKey().equalsIgnoreCase("cloneId")) {
									notCloneId = false;
									fixedDisatanceId = (int) entry.getValue();
									validContract = true;
									System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
								} else {
									log.info("not valid contract");
									fixedDistanceVehicleDetails.put("failed", entry.getValue());
									log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
									return fixedDistanceVehicleDetails;
								}

							}
						}
						if (validContract) {
							assignRouteDetail = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(
									fromDate, toDate, clientBranchPO.getBranchId(),
									eFmFmVehicleMasterPO.getVehicleId());
							if (!(assignRouteDetail.isEmpty())) {
								for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
									log.info("vehicle NUmber" + eFmFmVehicleMasterPO.getVehicleNumber());
									double totalAmt = 0.0, totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0,
											penaltyAmt = 0.0, penaltyTotalAmt = 0.0, tripBasedAmount = 0.0,
											extraKmCharges = 0.0, tripAmount = 0.0, extraKm = 0.0, panalityAmount = 0.0,
											totalKmAmount = 0.0;
									int absentDays = 0;
									// Vehicle belongs to a particular
									// contract type
									if (notCloneId) {
										fixedDisatanceId = allVehicleDetails.getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().geteFmFmContractDetails()
												.getDistanceContractId();
									}
									List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
											.getFixedDistanceDetails(fixedDisatanceId, clientBranchPO.getBranchId());

									List<EFmFmVehicleCheckInPO> totalWorkingDays = iVehicleCheckInBO
											.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate,
													clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getVehicleId());
									log.info("total working days" + totalWorkingDays.size());
									List<EFmFmVehicleMasterPO> SumOftotalKm = null;
									if (distanceFlg.equalsIgnoreCase("GPS")) {
										SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicle(fromDate, toDate,
												clientBranchPO.getBranchId(),
												allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.getVehicleId(),
												fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
														.getContractType().trim(),
												fixedDistanceDetails.get(0).getDistanceContractId());
									} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
										SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicleOdometer(fromDate,
												toDate, clientBranchPO.getBranchId(),
												allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.getVehicleId(),
												fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
														.getContractType().trim(),
												fixedDistanceDetails.get(0).getDistanceContractId());
									}
									log.info("SumOftotalKm" + SumOftotalKm.size());
									double totatSumOfKmAmount = 0.0;

									if (!(fixedDistanceDetails.get(0).getFuelPriceCalculation()
											.equalsIgnoreCase("NR"))) {
										totatSumOfKmAmount = fuelCalculation(fromDate, toDate, allVehicleDetails,
												clientBranchPO, fixedDistanceDetails,
												SumOftotalKm.get(0).getSumTravelledDistance(), distanceFlg);
									}

									if (assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
											.getContractType().equalsIgnoreCase("FDC")) {
										if (SumOftotalKm.get(0).getSumTravelledDistance() >= fixedDistanceDetails.get(0)
												.getFixedDistanceMonthly()) {
											extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
													- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
											extraKmCharges = extraKm
													* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
											totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													+ extraKmCharges + totatSumOfKmAmount;
											absentDays = fixedDistanceDetails.get(0).getMinimumDays()
													- totalWorkingDays.size();

											if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")
													&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
															.getMinimumDays()) {

												log.info("absentDays" + absentDays);
												penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												penaltyTotalAmt = (penaltyAmt + (penaltyAmt
														* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
														/ 100);
												log.info("penaltyTotalAmt" + penaltyTotalAmt);
												penalityPerday = (((fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays())
														* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
														/ 100) * absentDays;
												log.info("penalityPerday" + penalityPerday);
												totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
												// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
												totalAmt = totalAmt - penaltyTotalAmt;
												log.info("totalAmt" + Math.round(totalAmt));
											} else if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("N")
													&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
															.getMinimumDays()) {
												totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												totalAmt = totalAmt - totalPerDayDeductionAmnt;
												penaltyTotalAmt = totalPerDayDeductionAmnt;
											}
										} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails
												.get(0).getFixedDistanceMonthly()
												&& totalWorkingDays.size() >= fixedDistanceDetails.get(0)
														.getMinimumDays()) {
											totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													+ totatSumOfKmAmount;
										} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails
												.get(0).getFixedDistanceMonthly()
												&& totalWorkingDays.size() < fixedDistanceDetails.get(0)
														.getMinimumDays()) {
											totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													+ totatSumOfKmAmount;

											absentDays = fixedDistanceDetails.get(0).getMinimumDays()
													- totalWorkingDays.size();
											if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {

												log.info("absentDays" + absentDays);
												penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												log.info("panalityAmount" + panalityAmount);
												penaltyTotalAmt = (penaltyAmt + (penaltyAmt
														* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
														/ 100);
												log.info("penaltyTotalAmt" + penaltyTotalAmt);
												penalityPerday = (((fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays())
														* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
														/ 100) * absentDays;
												log.info("penalityPerday" + penalityPerday);
												totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
												// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
												totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
														- penaltyTotalAmt;
												log.info("totalAmt" + totalAmt);
											} else {
												totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												totalAmt = totalAmt - totalPerDayDeductionAmnt;
												penaltyTotalAmt = totalPerDayDeductionAmnt;
											}
										}

									} else if (assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
											.getContractType().equalsIgnoreCase("PDDC")) {

										totalAmt = fixedDistanceDetails.get(0).getPerDayCost()
												* totalWorkingDays.size();
										tripAmount = totalAmt;
										totalKmAmount = (SumOftotalKm.get(0).getSumTravelledDistance()
												* fixedDistanceDetails.get(0).getPerKmCost());
										totalAmt = totalAmt + totatSumOfKmAmount + totalKmAmount;
										absentDays = fixedDistanceDetails.get(0).getMinimumDays()
												- totalWorkingDays.size();
										if ((totalWorkingDays.size() < fixedDistanceDetails.get(0).getMinimumDays())
												&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
											absentDays = fixedDistanceDetails.get(0).getMinimumDays()
													- totalWorkingDays.size();
											log.info("absentDays" + absentDays);
											penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											log.info("panalityAmount" + panalityAmount);
											penaltyTotalAmt = penaltyAmt + ((penaltyAmt
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
											log.info("penaltyTotalAmt" + penaltyTotalAmt);
											totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
													.getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays())
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
													* absentDays;
											log.info("penalityPerday" + penalityPerday);
											totalAmt = totalAmt - penaltyTotalAmt;
											log.info("totalAmt" + totalAmt);

										} else {
											if (totalWorkingDays.size() < fixedDistanceDetails.get(0)
													.getMinimumDays()) {
												totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
														.getFixedDistanceChargeRate()
														/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
												totalAmt = totalAmt - totalPerDayDeductionAmnt;
												penaltyTotalAmt = totalPerDayDeductionAmnt;
											}
										}

									}
									System.out.println("totalSumOfAmount+" + totatSumOfKmAmount);
									addInvoiceRecord(totalPerDayDeductionAmnt, penalityPerday, tripBasedAmount,
											allVehicleDetails, clientBranchPO, fromDate, toDate, totalAmt,
											penaltyTotalAmt, totalWorkingDays.size(),
											SumOftotalKm.get(0).getSumTravelledDistance(),
											fixedDistanceDetails.get(0).getFixedDistanceMonthly(),
											fixedDistanceDetails.get(0).getMinimumDays(),
											fixedDistanceDetails.get(0).getFixedDistanceChargeRate(), randomNumber,
											allVehicleDetails.getTravelledDistance(), extraKm, extraKmCharges,
											assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster(),
											fixedDistanceDetails.get(0).getDistanceContractId(), totatSumOfKmAmount,
											eFmFmVehicleMasterPO.getDistanceFlg(),
											fixedDistanceDetails.get(0).getPerDayCost(), totalKmAmount,
											eFmFmVehicleMasterPO.getUserId(), tripAmount);
								}

							}
						} else {
							fixedDistanceVehicleDetails.put("failed", "INVALIDGENTYPE");
							log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
							return fixedDistanceVehicleDetails;
						}
					} else {
						fixedDistanceVehicleDetails.put("failed", "NOTVALIDCONTRACTTYPE");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return fixedDistanceVehicleDetails;
					}
				}

			}
			List<Map<String, Object>> tripByVehicle = new ArrayList<Map<String, Object>>();
			List<EFmFmVendorContractInvoicePO> invoiceByVehicleFixedDistance = iVehicleCheckInBO
					.getInvoiceByVehicleFixedDistance(fromDate, toDate, clientBranchPO.getBranchId(),
							eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(),
							eFmFmVehicleMasterPO.getVehicleId(), eFmFmVehicleMasterPO.getDistanceFlg());
			if (!(invoiceByVehicleFixedDistance.isEmpty())) {
				Set contrctSetCount = new HashSet();
				for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : invoiceByVehicleFixedDistance) {
					if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")
							|| vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
									.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
						Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
						contrctSetCount.add(vendorInvoiceDetailsList.getInvoiceType());
						fixedDistanceVehicleDetails.put("contractTypeCount", contrctSetCount.size());
						fixedDistanceTrips.put("invoiceType", vendorInvoiceDetailsList.getInvoiceType());
						fixedDistanceTrips.put("vehicleNumber",
								vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
						fixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
						fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
						fixedDistanceTrips.put("distanceFlg", vendorInvoiceDetailsList.getDistanceFlg());
						fixedDistanceTrips.put("totalKm", vendorInvoiceDetailsList.getTotalDistance());
						fixedDistanceTrips.put("extraKm", vendorInvoiceDetailsList.getTotalExtraDistance());
						fixedDistanceTrips.put("totalWorkingDays", vendorInvoiceDetailsList.getPresentDays());
						fixedDistanceTrips.put("fixedcharges", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("penalty", vendorInvoiceDetailsList.getTotalDeductibles());
						if (vendorInvoiceDetailsList.getInvoiceRemarks() == null) {
							fixedDistanceTrips.put("invoiceRemarks", "NA");
						} else {
							fixedDistanceTrips.put("invoiceRemarks", vendorInvoiceDetailsList.getInvoiceRemarks());
						}
						fixedDistanceTrips.put("extraKmcharges", vendorInvoiceDetailsList.getExtraDistanceCharge());
						fixedDistanceTrips.put("contractedKm", vendorInvoiceDetailsList.getBaseDistance());
						fixedDistanceTrips.put("totalDeduction", vendorInvoiceDetailsList.getTotalDeductibles());
						fixedDistanceTrips.put("totalPerDayDeduction",
								vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());
						fixedDistanceTrips.put("totalpenalityAmnt", vendorInvoiceDetailsList.getTotalPenalityAmount());
						// net amount after deduction
						// PDDC contractType
						fixedDistanceTrips.put("totalKmAmount",
								Math.round(vendorInvoiceDetailsList.getTotalKmAmount() * 100) / 100.00);
						fixedDistanceTrips.put("contractPerDayAmount",
								Math.round(vendorInvoiceDetailsList.getPerDayCost() * 100) / 100.00);
						fixedDistanceTrips.put("tripConsolidatedAmount",
								Math.round(vendorInvoiceDetailsList.getTripTotalAmount() * 100) / 100.00);

						fixedDistanceTrips.put("totalAmount", vendorInvoiceDetailsList.getTotalAmountPayable());
						fixedDistanceTrips.put("contractAmount", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("contractKM", vendorInvoiceDetailsList.getBaseDistance());
						fixedDistanceTrips.put("fuelExtraAmount", vendorInvoiceDetailsList.getFuelExtraAmount());
						fixedDistanceTrips.put("approvalStatus", vendorInvoiceDetailsList.getApprovalStatus());
						fixedDistanceTrips.put("contractType", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType());
						fixedDistanceTrips.put("perKmCost", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.geteFmFmContractDetails().getPerKmCost());
						if (vendorInvoiceDetailsList.getPresentDays() < invoiceByVehicleFixedDistance.get(0)
								.getWorkingDays()) {
							fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
									- vendorInvoiceDetailsList.getPresentDays()));
						} else {
							fixedDistanceTrips.put("absentDays", "NO");
						}
						fixedDistanceTrips.put("totalAmountExtraKmCharge", "0");
						fixedDistanceTrips.put("vendorName", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.getEfmFmVendorMaster().getVendorName());
						fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
						fixedDistanceTrips.put("invoiceMonthDate",
								monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
						fixedDistanceTrips.put("invoiceCreationDate",
								formatter.format(vendorInvoiceDetailsList.getCreationTime()));
						invoiceVendorDetails.add(fixedDistanceTrips);
					} else if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("TDC")) {
						List<EFmFmVendorContractInvoicePO> vendorFixedInvoiceDetails = iVehicleCheckInBO
								.getInvoiceTripBasedVehicle(fromDate, toDate, clientBranchPO.getBranchId(),
										vendorInvoiceDetailsList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getVehicleId());
						if (!(vendorFixedInvoiceDetails.isEmpty())) {
							for (EFmFmVendorContractInvoicePO fixedTripList : vendorFixedInvoiceDetails) {
								Map<String, Object> tripFixedDistanceTrips = new HashMap<String, Object>();
								tripFixedDistanceTrips.put("invoiceType", fixedTripList.getInvoiceType());
								tripFixedDistanceTrips.put("vehicleNumber",
										fixedTripList.getEfmFmVehicleMaster().getVehicleNumber());
								tripFixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
								tripFixedDistanceTrips.put("invoiceNumber",
										vendorInvoiceDetailsList.getInvoiceNumber());
								tripFixedDistanceTrips.put("tripId",
										fixedTripList.getEfmFmAssignRoute().getAssignRouteId());
								tripFixedDistanceTrips.put("totalKm", fixedTripList.getTotalDistance());
								tripFixedDistanceTrips.put("extraKm", fixedTripList.getTotalExtraDistance());
								tripFixedDistanceTrips.put("fixedcharges", fixedTripList.getBaseTotal());
								tripFixedDistanceTrips.put("extraKmcharges", fixedTripList.getExtraDistanceCharge());
								tripFixedDistanceTrips.put("totalAmount", fixedTripList.getTripTotalAmount());
								tripByVehicle.add(tripFixedDistanceTrips);
							}
						}
					}
				}
				fixedDistanceVehicleDetails.put("fixedDistanceBased", invoiceVendorDetails);
				fixedDistanceVehicleDetails.put("tripBasedFixedDetails", tripByVehicle);

			} else {
				fixedDistanceVehicleDetails.put("failed", "NODATA");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return fixedDistanceVehicleDetails;
			}

			break;
		case "INVOICEDETAILS":
			List<Map<String, Object>> tripInvoiceBasedVehicleDetails = new ArrayList<Map<String, Object>>();
			List<EFmFmVendorContractInvoicePO> vendorSummaryInvoiceDetails = iVehicleCheckInBO
					.getInvoiceDetails(clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getInvoiceNumber());
			if (!(vendorSummaryInvoiceDetails.isEmpty())) {
				Set contrctSetCount = new HashSet();
				double totalAmount = 0.0, penalty = 0.0, tripAmount = 0.0, totalBaseAmount = 0.0, serviceTax = 0.0,
						totalServiceTaxAmount = 0.0;
				int noOfvehicle = 0;
				for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : vendorSummaryInvoiceDetails) {
					contrctSetCount.add(vendorInvoiceDetailsList.getInvoiceType());
					fixedDistanceVehicleDetails.put("contractTypeCount", contrctSetCount.size());
					fixedDistanceVehicleDetails.put("vendorName",
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					fixedDistanceVehicleDetails.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
					fixedDistanceVehicleDetails.put("invoiceMonthDate",
							monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
					fixedDistanceVehicleDetails.put("invoiceCreationDate",
							formatter.format(vendorInvoiceDetailsList.getCreationTime()));
					fixedDistanceVehicleDetails.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
					fixedDistanceVehicleDetails.put("contractType", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
							.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType());

					penalty = penalty + vendorInvoiceDetailsList.getTotalDeductibles();
					if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")
							|| vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
									.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
						Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
						totalAmount = totalAmount + vendorInvoiceDetailsList.getTotalAmountPayable();
						totalBaseAmount = totalBaseAmount + vendorInvoiceDetailsList.getBaseTotal();
						fixedDistanceTrips.put("invoiceType", vendorInvoiceDetailsList.getInvoiceType());
						fixedDistanceTrips.put("vehicleNumber",
								vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
						fixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
						fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
						fixedDistanceTrips.put("distanceFlg", vendorInvoiceDetailsList.getDistanceFlg());
						fixedDistanceTrips.put("totalKm", vendorInvoiceDetailsList.getTotalDistance());
						fixedDistanceTrips.put("extraKm", vendorInvoiceDetailsList.getTotalExtraDistance());
						fixedDistanceTrips.put("totalWorkingDays", vendorInvoiceDetailsList.getPresentDays());
						fixedDistanceTrips.put("totalDeduction", vendorInvoiceDetailsList.getTotalDeductibles());
						fixedDistanceTrips.put("fixedcharges", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("penalty", vendorInvoiceDetailsList.getTotalDeductibles());
						if (vendorInvoiceDetailsList.getInvoiceRemarks() == null) {
							fixedDistanceTrips.put("invoiceRemarks", "NA");
						} else {
							fixedDistanceTrips.put("invoiceRemarks", vendorInvoiceDetailsList.getInvoiceRemarks());
						}
						fixedDistanceTrips.put("extraKmcharges", vendorInvoiceDetailsList.getExtraDistanceCharge());
						fixedDistanceTrips.put("totalAmount", vendorInvoiceDetailsList.getTotalAmountPayable());
						fixedDistanceTrips.put("totalPerDayDeduction",
								vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());
						fixedDistanceTrips.put("totalpenalityAmnt", vendorInvoiceDetailsList.getTotalPenalityAmount());
						// PDDC contrctType
						fixedDistanceTrips.put("totalKmAmount", vendorInvoiceDetailsList.getTotalKmAmount());
						fixedDistanceTrips.put("contractPerDayAmount",
								Math.round(vendorInvoiceDetailsList.getPerDayCost() * 100) / 100.00);
						fixedDistanceTrips.put("contractedKm", vendorInvoiceDetailsList.getBaseDistance());
						fixedDistanceTrips.put("contractAmount", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("contractKM", vendorInvoiceDetailsList.getBaseDistance());
						if (vendorInvoiceDetailsList.getPresentDays() < vendorSummaryInvoiceDetails.get(0)
								.getWorkingDays()) {
							fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
									- vendorInvoiceDetailsList.getPresentDays()));
						} else {
							fixedDistanceTrips.put("absentDays", "NO");
						}
						fixedDistanceTrips.put("totalAmountExtraKmCharge", "0");
						fixedDistanceTrips.put("vendorName", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.getEfmFmVendorMaster().getVendorName());
						fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
						fixedDistanceTrips.put("invoiceMonthDate",
								monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
						fixedDistanceTrips.put("invoiceCreationDate",
								formatter.format(vendorInvoiceDetailsList.getCreationTime()));
						fixedDistanceTrips.put("fuelExtraAmount", vendorInvoiceDetailsList.getFuelExtraAmount());
						fixedDistanceTrips.put("approvalStatus", vendorInvoiceDetailsList.getApprovalStatus());
						fixedDistanceTrips.put("perKmCost", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.geteFmFmContractDetails().getPerKmCost());
						invoiceVendorDetails.add(fixedDistanceTrips);
						noOfvehicle++;
					} else if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("TDC")) {
						noOfvehicle++;
						List<EFmFmVendorContractInvoicePO> vendorFixedInvoiceDetails = iVehicleCheckInBO
								.getInvoiceTripBasedVehicle(vendorSummaryInvoiceDetails.get(0).getInvoiceStartDate(),
										vendorSummaryInvoiceDetails.get(0).getInvoiceStartDate(),
										clientBranchPO.getBranchId(), vendorInvoiceDetailsList.getEfmFmAssignRoute()
												.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
						if (!(vendorFixedInvoiceDetails.isEmpty())) {
							for (EFmFmVendorContractInvoicePO fixedTripList : vendorFixedInvoiceDetails) {
								Map<String, Object> tripFixedDistanceTrips = new HashMap<String, Object>();
								totalBaseAmount = totalBaseAmount + fixedTripList.getBaseTotal();
								tripAmount = tripAmount + fixedTripList.getTripTotalAmount();
								tripFixedDistanceTrips.put("invoiceType", fixedTripList.getInvoiceType());
								tripFixedDistanceTrips.put("vehicleNumber",
										fixedTripList.getEfmFmVehicleMaster().getVehicleNumber());
								tripFixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
								tripFixedDistanceTrips.put("invoiceNumber",
										vendorInvoiceDetailsList.getInvoiceNumber());
								tripFixedDistanceTrips.put("tripId",
										fixedTripList.getEfmFmAssignRoute().getAssignRouteId());
								tripFixedDistanceTrips.put("totalKm", fixedTripList.getTotalDistance());
								tripFixedDistanceTrips.put("extraKm", fixedTripList.getTotalExtraDistance());
								tripFixedDistanceTrips.put("fixedcharges", fixedTripList.getBaseTotal());
								tripFixedDistanceTrips.put("extraKmcharges", fixedTripList.getExtraDistanceCharge());
								tripFixedDistanceTrips.put("totalAmount", fixedTripList.getTripTotalAmount());
								tripInvoiceBasedVehicleDetails.add(tripFixedDistanceTrips);
							}
						}
					}
				}
				serviceTax = (totalAmount + tripAmount) * vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax() / 100;
				totalServiceTaxAmount = (totalAmount + tripAmount) + serviceTax;
				fixedDistanceVehicleDetails.put("totalAmount", (double) Math.round(totalBaseAmount * 100) / 100);
				fixedDistanceVehicleDetails.put("penalty", (double) Math.round(penalty * 100) / 100);
				fixedDistanceVehicleDetails.put("totalPayableAmount",
						(double) Math.round((totalAmount + tripAmount) * 100) / 100);
				fixedDistanceVehicleDetails.put("serviceTax", vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax());
				fixedDistanceVehicleDetails.put("serviceTaxAmount", (double) Math.round(serviceTax * 100) / 100);
				fixedDistanceVehicleDetails.put("total", (double) Math.round(totalServiceTaxAmount * 100) / 100);
				fixedDistanceVehicleDetails.put("noOfvehicle", noOfvehicle);
				fixedDistanceVehicleDetails.put("fixedDistanceBased", invoiceVendorDetails);
				fixedDistanceVehicleDetails.put("tripBasedFixedDetails", tripInvoiceBasedVehicleDetails);

			} else {
				fixedDistanceVehicleDetails.put("failed", "NODATA");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return fixedDistanceVehicleDetails;
			}
			break;
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return fixedDistanceVehicleDetails;
	}

	public Map<String, Object> processInvoice(int vendorId, String contractType, int branchId, Date fromDate,
			Date todate, int genPreDate) throws ParseException {
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		/*
		 * Format year = new SimpleDateFormat("yyyy"); DateFormat monthDate =
		 * new SimpleDateFormat("MM-yyyy");
		 * 
		 * 
		 * Calendar cal = Calendar.getInstance(); cal.set(Calendar.MONTH,
		 * fromDate.getMonth()); cal.set(Calendar.YEAR,
		 * Integer.valueOf(year.format(fromDate))); cal.set(Calendar.DATE,
		 * genPreDate); cal.set(Calendar.DATE,
		 * cal.getActualMinimum(Calendar.DATE)); System.out.println(
		 * " ---Min Date::---" + cal.getTime()); Date startDate = cal.getTime();
		 * cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE) +
		 * (genPreDate - 1));
		 */

		// System.out.println(" ---Date::---" + cal.getTime() + "yesr" +
		// monthDate.format(cal.getTime()));
		// Date endDate = cal.getTime();
		List<EFmFmVehicleMasterPO> contractDetails = iVehicleCheckInBO.getAllContractDetailsByVendorId(vendorId,
				contractType, branchId);
		if (!contractDetails.isEmpty() && contractDetails.size() > 0) {
			for (EFmFmVehicleMasterPO vehContractDetails : contractDetails) {
				List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
						.getFixedDistanceDetails(vehContractDetails.geteFmFmContractDetails().getDistanceContractId(),
								branchId);
				if (!fixedDistanceDetails.isEmpty()) {
					if (dateFormat.parse(dateFormat.format(fixedDistanceDetails.get(0).getFromDate()))
							.after(dateFormat.parse(dateFormat.format(fromDate)))
							|| dateFormat.parse(dateFormat.format(fixedDistanceDetails.get(0).getToDate()))
									.before(dateFormat.parse(dateFormat.format(todate)))) {
						/*
						 * if(dateFormat.parse(dateFormat.format(
						 * fixedDistanceDetails.get(0).getToDate())).before(
						 * dateFormat.parse(dateFormat.format(endDate)))){ int
						 * diffDays= (int)
						 * ((dateFormat.parse(dateFormat.format(endDate)).
						 * getTime()-dateFormat.parse(dateFormat.format(
						 * fixedDistanceDetails.get(0).getToDate())).getTime())/
						 * (1000 * 60 * 60 * 24));
						 * System.out.println("diffDays"+diffDays); }
						 */
						List<EFmFmFixedDistanceContractDetailPO> fixedConeDetails = iVehicleCheckInBO
								.getFixedDistanceDetailsByCloneId(
										vehContractDetails.geteFmFmContractDetails().getDistanceContractId(), branchId);
						if (!fixedConeDetails.isEmpty()) {
							/*
							 * int cloneStartDiff= (int)
							 * ((dateFormat.parse(dateFormat.format(
							 * fixedDistanceDetails.get(0).getStartDate())).
							 * getTime()-dateFormat.parse(dateFormat.format(
							 * endDate)).getTime())/(1000 * 60 * 60 * 24));
							 * System.out.println("cloneStartDiff"+
							 * cloneStartDiff);
							 */
							System.out.println("1---" + dateFormat.format(fixedConeDetails.get(0).getFromDate()) + "---"
									+ dateFormat.format(fromDate));
							System.out.println("2--" + dateFormat.format(fixedConeDetails.get(0).getToDate()) + "---"
									+ dateFormat.format(todate));
							if (dateFormat.parse(dateFormat.format(fixedConeDetails.get(0).getFromDate()))
									.after(dateFormat.parse(dateFormat.format(fromDate)))
									&& dateFormat.parse(dateFormat.format(fixedConeDetails.get(0).getToDate()))
											.before(dateFormat.parse(dateFormat.format(todate)))) {
								fixedDistanceVehicleDetails.put("failed", "kinldy check the clone Id contract date");
							} else {
								// update all the vehicles contractdetailsId
								List<EFmFmVehicleMasterPO> vehicleDetailsList = iVehicleCheckInBO.bulkUpdateContractId(
										vendorId, fixedConeDetails.get(0).getDistanceContractId(),
										fixedConeDetails.get(0).getCloneId(), branchId);
								if (!vehicleDetailsList.isEmpty()) {
									for (EFmFmVehicleMasterPO vehicleList : vehicleDetailsList) {
										EFmFmVehicleMasterPO updateVehicleDetails = iVehicleCheckInBO
												.getParticularVehicleDetail(vehicleList.getVehicleId());
										updateVehicleDetails.seteFmFmContractDetails(fixedConeDetails.get(0));
										iVehicleCheckInBO.update(updateVehicleDetails);
										fixedDistanceVehicleDetails.put("cloneId",
												fixedConeDetails.get(0).getDistanceContractId());
									}
								} else {
									fixedDistanceVehicleDetails.put("failed",
											"vehicle Contract Details are notUpdated");
								}
							}
						}
					}
				} else {
					fixedDistanceVehicleDetails.put("failed", "kinldy create the Contract Details for Vehicles");
				}

			}
		} /*
			 * else { fixedDistanceVehicleDetails.put("failed",
			 * "Create the contract details and the mapped to vehicles"); }
			 */

		return fixedDistanceVehicleDetails;
	}

	/*
	 * Invoice non approval details.
	 */

	@POST
	@Path("/nonApprovalList")
	public Response nonApprovalDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		log.info("invice modify service" + eFmFmVehicleMasterPO.getInvoiceId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat monthDate = new SimpleDateFormat("MM-yyyy");
		List<Map<String, Object>> invoiceVendorDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		List<Map<String, Object>> tripInvoiceBasedVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmVendorContractInvoicePO> vendorSummaryInvoiceDetails = iVehicleCheckInBO.getNonApprovalList(
				eFmFmVehicleMasterPO.getCombinedFacility(), eFmFmVehicleMasterPO.getStatusFlg(),
				eFmFmVehicleMasterPO.getStatus());
		if (!(vendorSummaryInvoiceDetails.isEmpty())) {
			double totalAmount = 0.0, penalty = 0.0, tripAmount = 0.0, totalBaseAmount = 0.0, serviceTax = 0.0,
					totalServiceTaxAmount = 0.0;
			int noOfvehicle = 0;
			for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : vendorSummaryInvoiceDetails) {
				if (vendorInvoiceDetailsList.getApprovalStatus().equalsIgnoreCase("M")) {
					fixedDistanceVehicleDetails.put("vendorName",
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					fixedDistanceVehicleDetails.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
					fixedDistanceVehicleDetails.put("invoiceMonthDate",
							monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
					fixedDistanceVehicleDetails.put("invoiceCreationDate",
							formatter.format(vendorInvoiceDetailsList.getCreationTime()));
					penalty = penalty + vendorInvoiceDetailsList.getTotalDeductibles();
					if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")
							|| vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
									.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
						Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
						totalAmount = totalAmount + vendorInvoiceDetailsList.getTotalAmountPayable();
						totalBaseAmount = totalBaseAmount + vendorInvoiceDetailsList.getBaseTotal();
						fixedDistanceTrips.put("invoiceType", vendorInvoiceDetailsList.getInvoiceType());
						fixedDistanceTrips.put("vehicleNumber",
								vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
						fixedDistanceTrips.put("totalKm", vendorInvoiceDetailsList.getTotalDistance());
						fixedDistanceTrips.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
						fixedDistanceTrips.put("newtotalKm", vendorInvoiceDetailsList.getModTotalDistance());
						fixedDistanceTrips.put("oldtotalKm", vendorInvoiceDetailsList.getTotalDistance());
						fixedDistanceTrips.put("extraKm", vendorInvoiceDetailsList.getTotalExtraDistance());
						fixedDistanceTrips.put("newExtraKm", vendorInvoiceDetailsList.getModTotalExtraDistance());
						fixedDistanceTrips.put("oldExtraKm", vendorInvoiceDetailsList.getTotalExtraDistance());
						fixedDistanceTrips.put("newPresentDays", vendorInvoiceDetailsList.getModPresentDays());
						fixedDistanceTrips.put("oldPresentDays", vendorInvoiceDetailsList.getPresentDays());
						fixedDistanceTrips.put("totalWorkingDays", vendorInvoiceDetailsList.getPresentDays());
						fixedDistanceTrips.put("fixedcharges", vendorInvoiceDetailsList.getBaseTotal());
						fixedDistanceTrips.put("penalty", vendorInvoiceDetailsList.getTotalDeductibles());
						fixedDistanceTrips.put("contractType", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType());
						if (vendorInvoiceDetailsList.getInvoiceRemarks() == null) {
							fixedDistanceTrips.put("invoiceRemarks", "NA");
						} else {
							fixedDistanceTrips.put("invoiceRemarks", vendorInvoiceDetailsList.getInvoiceRemarks());
						}
						fixedDistanceTrips.put("extraKmcharges", vendorInvoiceDetailsList.getExtraDistanceCharge());
						fixedDistanceTrips.put("totalAmount", vendorInvoiceDetailsList.getTotalAmountPayable());
						fixedDistanceTrips.put("totalPerDayDeduction",
								vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());
						fixedDistanceTrips.put("totalpenalityAmnt", vendorInvoiceDetailsList.getTotalPenalityAmount());
						if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
								.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")) {
							fixedDistanceTrips.put("contractedKm", vendorInvoiceDetailsList.getBaseDistance());
							fixedDistanceTrips.put("contractAmount", vendorInvoiceDetailsList.getBaseTotal());
						} else if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
								.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
							fixedDistanceTrips.put("contractedKm",
									vendorInvoiceDetailsList.geteFmFmContractDetails().getPerKmCost());
							fixedDistanceTrips.put("fixedcharges",
									vendorInvoiceDetailsList.geteFmFmContractDetails().getPerDayCost());
						} else {
							fixedDistanceTrips.put("contractedKm", vendorInvoiceDetailsList.getBaseDistance());
							fixedDistanceTrips.put("contractAmount", vendorInvoiceDetailsList.getBaseTotal());
						}

						fixedDistanceTrips.put("oldFuelAmount", vendorInvoiceDetailsList.getFuelExtraAmount());
						fixedDistanceTrips.put("newFuelAmount", vendorInvoiceDetailsList.getModifiedFuelExtraAmount());
						if (vendorInvoiceDetailsList.getInvoiceRemarks() == null) {
							fixedDistanceTrips.put("oldInvoiceRemarks", "NA");
						} else {
							fixedDistanceTrips.put("oldInvoiceRemarks", vendorInvoiceDetailsList.getInvoiceRemarks());
						}
						if (vendorInvoiceDetailsList.getModifiedInvoiceRemarks() == null) {
							fixedDistanceTrips.put("newInvoiceRemarks", "NA");
						} else {
							fixedDistanceTrips.put("newInvoiceRemarks",
									vendorInvoiceDetailsList.getModifiedInvoiceRemarks());
						}
						fixedDistanceTrips.put("contractKM", vendorInvoiceDetailsList.getBaseDistance());
						fixedDistanceTrips.put("perKmCost",
								vendorInvoiceDetailsList.geteFmFmContractDetails().getPerKmCost());
						if (vendorInvoiceDetailsList.getModPresentDays() > 0) {
							fixedDistanceTrips.put("oldPresentDaysFlg", "Y");
						} else {
							fixedDistanceTrips.put("oldPresentDaysFlg", "N");
						}

						if (vendorInvoiceDetailsList.getModTotalDistance() > 0) {
							fixedDistanceTrips.put("oldtotalKmFlg", "Y");
						} else {
							fixedDistanceTrips.put("oldtotalKmFlg", "N");
						}

						if (vendorInvoiceDetailsList.getModTotalExtraDistance() > 0) {
							fixedDistanceTrips.put("oldExtraKmFlg", "Y");
						} else {
							fixedDistanceTrips.put("oldExtraKmFlg", "N");
						}

						if (vendorInvoiceDetailsList.getModifiedFuelExtraAmount() > 0) {
							fixedDistanceTrips.put("oldFuelAmountFlg", "Y");
						} else {
							fixedDistanceTrips.put("oldFuelAmountFlg", "N");
						}

						if (vendorInvoiceDetailsList.getModifiedInvoiceRemarks() != null) {
							fixedDistanceTrips.put("oldInvoiceRemarksFlg", "Y");
						} else {
							fixedDistanceTrips.put("oldInvoiceRemarksFlg", "N");
						}

						if (vendorInvoiceDetailsList.getPresentDays() < vendorSummaryInvoiceDetails.get(0)
								.getWorkingDays()) {
							fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
									- vendorInvoiceDetailsList.getPresentDays()));
						} else {
							fixedDistanceTrips.put("absentDays", "NO");
						}
						fixedDistanceTrips.put("totalAmountExtraKmCharge", "0");
						fixedDistanceTrips.put("vendorName", vendorInvoiceDetailsList.getEfmFmVehicleMaster()
								.getEfmFmVendorMaster().getVendorName());
						fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
						fixedDistanceTrips.put("invoiceMonthDate",
								monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
						fixedDistanceTrips.put("invoiceCreationDate",
								formatter.format(vendorInvoiceDetailsList.getCreationTime()));
						fixedDistanceTrips.put("approvalStatus", vendorInvoiceDetailsList.getApprovalStatus());
						fixedDistanceTrips.put("facilityName", vendorInvoiceDetailsList.geteFmFmClientBranchPO().getBranchName());
						invoiceVendorDetails.add(fixedDistanceTrips);
						noOfvehicle++;
					}
				}
				serviceTax = (totalAmount + tripAmount) * vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax() / 100;
				totalServiceTaxAmount = (totalAmount + tripAmount) + serviceTax;
				fixedDistanceVehicleDetails.put("totalAmount", (double) Math.round(totalBaseAmount * 100) / 100);
				fixedDistanceVehicleDetails.put("penalty", (double) Math.round(penalty * 100) / 100);
				fixedDistanceVehicleDetails.put("totalPayableAmount",
						(double) Math.round((totalAmount + tripAmount) * 100) / 100);
				fixedDistanceVehicleDetails.put("serviceTax", vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax());
				fixedDistanceVehicleDetails.put("serviceTaxAmount", (double) Math.round(serviceTax * 100) / 100);
				fixedDistanceVehicleDetails.put("total", (double) Math.round(totalServiceTaxAmount * 100) / 100);
				fixedDistanceVehicleDetails.put("noOfvehicle", noOfvehicle);
				fixedDistanceVehicleDetails.put("fixedDistanceBased", invoiceVendorDetails);
				fixedDistanceVehicleDetails.put("tripBasedFixedDetails", tripInvoiceBasedVehicleDetails);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(fixedDistanceVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/modifiedInvoiceDetails")
	public Response modifiedInvoiceDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		log.info("invice modify service" + eFmFmVehicleMasterPO.getInvoiceId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat monthDate = new SimpleDateFormat("MM-yyyy");
		List<Map<String, Object>> invoiceVendorDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();

		List<EFmFmVendorContractInvoicePO> vehicleInvoiceDetails = iVehicleCheckInBO
				.getInvoiceByContractInvoiceId(eFmFmVehicleMasterPO.getInvoiceId(), eFmFmVehicleMasterPO.getBranchId());
		if (!(vehicleInvoiceDetails.isEmpty())) {
			if (eFmFmVehicleMasterPO.getUserRole().equalsIgnoreCase("ADMIN")
					&& eFmFmVehicleMasterPO.getStatusFlg().equalsIgnoreCase("P")) {

				for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : vehicleInvoiceDetails) {

					if (vendorInvoiceDetailsList.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
							.getContractType().equalsIgnoreCase("FDC")
							|| vendorInvoiceDetailsList.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
									.getContractType().equalsIgnoreCase("PDDC")) {
						int absentDays = 0, totalWorkingDays = 0;
						double totalAmt = 0.0, totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0, penaltyAmt = 0.0,
								penaltyTotalAmt = 0.0, tripBasedAmount = 0.0, extraKmCharges = 0.0, extraKm = 0.0,
								panalityAmount = 0.0, totalKmAmount = 0.0, tripAmount = 0.0;
						List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
								.getFixedDistanceDetails(vendorInvoiceDetailsList.getEfmFmVehicleMaster()
										.geteFmFmContractDetails().getDistanceContractId(),
										eFmFmVehicleMasterPO.getBranchId());
						if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
								.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")) {
							totalWorkingDays = vendorInvoiceDetailsList.getWorkingDays()
									- eFmFmVehicleMasterPO.getNoOfDays();
							if (eFmFmVehicleMasterPO.getSumTravelledDistance() >= fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()) {
								extraKm = eFmFmVehicleMasterPO.getSumTravelledDistance()
										- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
								extraKmCharges = extraKm * fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate() + extraKmCharges
										+ eFmFmVehicleMasterPO.getFuelExtraAmount();
								absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
								if (totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {

									if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
										penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays())
												* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
												* absentDays;
										penaltyTotalAmt = (penaltyAmt + (penaltyAmt
												* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100);
										totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
												.getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										totalAmt = totalAmt - penaltyTotalAmt;
									} else {
										totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
												.getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										totalAmt = totalAmt - totalPerDayDeductionAmnt;
										penaltyTotalAmt = totalPerDayDeductionAmnt;
									}
								}
							} else if (eFmFmVehicleMasterPO.getSumTravelledDistance() < fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()
									&& totalWorkingDays >= fixedDistanceDetails.get(0).getMinimumDays()) {
								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										+ eFmFmVehicleMasterPO.getFuelExtraAmount();
							} else if (eFmFmVehicleMasterPO.getSumTravelledDistance() < fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()
									&& totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {
								absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;

								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate() + extraKmCharges
										+ eFmFmVehicleMasterPO.getFuelExtraAmount();

								if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {

									penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays())
											* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
											* absentDays;
									penaltyTotalAmt = (penaltyAmt
											+ (penaltyAmt * fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
									totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									totalAmt = totalAmt - penaltyTotalAmt;
								} else {
									totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									totalAmt = totalAmt - totalPerDayDeductionAmnt;
									penaltyTotalAmt = totalPerDayDeductionAmnt;
								}

							}

						} else if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
								.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
							totalWorkingDays = vendorInvoiceDetailsList.getWorkingDays()
									- eFmFmVehicleMasterPO.getNoOfDays();
							absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
							totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
							tripAmount = totalAmt;
							totalKmAmount = (eFmFmVehicleMasterPO.getSumTravelledDistance()
									* fixedDistanceDetails.get(0).getPerKmCost());
							totalAmt = totalAmt + vendorInvoiceDetailsList.getFuelExtraAmount() + totalKmAmount;
							absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
							if ((totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays())
									&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
								log.info("absentDays" + absentDays);
								penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
								log.info("panalityAmount" + panalityAmount);
								penaltyTotalAmt = penaltyAmt
										+ ((penaltyAmt * fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
												/ 100);
								log.info("penaltyTotalAmt" + penaltyTotalAmt);
								totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
								penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays())
										* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
										* absentDays;
								log.info("penalityPerday" + penalityPerday);
								totalAmt = totalAmt - penaltyTotalAmt;
								log.info("totalAmt" + totalAmt);

							} else {
								if (totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {
									totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									totalAmt = totalAmt - totalPerDayDeductionAmnt;
									penaltyTotalAmt = totalPerDayDeductionAmnt;
								}
							}

						}

						List<EFmFmVendorContractInvoicePO> updateInvoiceDetails = iVehicleCheckInBO
								.getInvoiceByContractInvoiceId(eFmFmVehicleMasterPO.getInvoiceId(),
										eFmFmVehicleMasterPO.getBranchId());
						if (!(updateInvoiceDetails.isEmpty())) {
							updateInvoiceDetails.get(0).setModInvoiceGenerationDate(new Date());
							updateInvoiceDetails.get(0)
									.setModExtraDistanceCharge(vendorInvoiceDetailsList.getTotalExtraDistance());
							updateInvoiceDetails.get(0).setModPresentDays(vendorInvoiceDetailsList.getWorkingDays());
							updateInvoiceDetails.get(0)
									.setModTotalAmountPayable(vendorInvoiceDetailsList.getTotalAmountPayable());
							updateInvoiceDetails.get(0)
									.setModTotalDeductibles(vendorInvoiceDetailsList.getTotalDeductibles());
							updateInvoiceDetails.get(0)
									.setModTotalDistance(vendorInvoiceDetailsList.getTotalDistance());
							updateInvoiceDetails.get(0)
									.setModTotalExtraDistance(vendorInvoiceDetailsList.getTotalExtraDistance());
							updateInvoiceDetails.get(0)
									.setModTotPenalityAmount(vendorInvoiceDetailsList.getTotalPenalityAmount());
							updateInvoiceDetails.get(0).setModTotPerDayDeductionAmnt(
									vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());
							updateInvoiceDetails.get(0)
									.setModTravelledDistance(vendorInvoiceDetailsList.getTravelledDistance());
							updateInvoiceDetails.get(0)
									.setModTripTotalAmount(vendorInvoiceDetailsList.getTripTotalAmount());
							updateInvoiceDetails.get(0)
									.setModifiedFuelExtraAmount(vendorInvoiceDetailsList.getFuelExtraAmount());
							updateInvoiceDetails.get(0)
									.setModifiedInvoiceRemarks(vendorInvoiceDetailsList.getInvoiceRemarks());
							updateInvoiceDetails.get(0)
									.setModTripTotalAmount(vendorInvoiceDetailsList.getTripTotalAmount());
							updateInvoiceDetails.get(0).setFuelExtraAmount(eFmFmVehicleMasterPO.getFuelExtraAmount());
							updateInvoiceDetails.get(0).setTripTotalAmount(Math.round(tripAmount * 100.0) / 100.0);
							updateInvoiceDetails.get(0)
									.setExtraDistanceCharge(Math.round(extraKmCharges * 100.0) / 100.0);
							updateInvoiceDetails.get(0).setPresentDays(totalWorkingDays);
							updateInvoiceDetails.get(0).setTotalAmountPayable(Math.round(totalAmt * 100.0) / 100.0);
							updateInvoiceDetails.get(0)
									.setTotalDeductibles(Math.round(penaltyTotalAmt * 100.0) / 100.0);
							updateInvoiceDetails.get(0).setTotalDistance(
									Math.round(eFmFmVehicleMasterPO.getSumTravelledDistance() * 100.0) / 100.0);
							updateInvoiceDetails.get(0).setTotalExtraDistance(Math.round(extraKm * 100.0) / 100.0);
							updateInvoiceDetails.get(0)
									.setTotalPenalityAmount(Math.round(penalityPerday * 100.0) / 100.0);
							updateInvoiceDetails.get(0)
									.setTotalPerDayDeductionAmnt(Math.round(totalPerDayDeductionAmnt * 100.0) / 100.0);
							updateInvoiceDetails.get(0).setTripTotalAmount(Math.round(tripBasedAmount * 100.0) / 100.0);
							updateInvoiceDetails.get(0).setApprovalStatus(eFmFmVehicleMasterPO.getStatusFlg());
							updateInvoiceDetails.get(0).setInvoiceRemarks(eFmFmVehicleMasterPO.getInvoiceRemarks());
							updateInvoiceDetails.get(0).setTotalKmAmount(totalKmAmount);
							iVehicleCheckInBO.invoiceUpdate(updateInvoiceDetails.get(0));
						}
					}
				}

			} else {
				vehicleInvoiceDetails.get(0).setModInvoiceGenerationDate(new Date());
				// vehicleInvoiceDetails.get(0).setModExtraDistanceCharge(eFmFmVehicleMasterPO.getExtraDistace());
				vehicleInvoiceDetails.get(0).setModPresentDays(eFmFmVehicleMasterPO.getNoOfDays());
				vehicleInvoiceDetails.get(0).setModTotalDistance(eFmFmVehicleMasterPO.getSumTravelledDistance());
				vehicleInvoiceDetails.get(0).setModifiedFuelExtraAmount(eFmFmVehicleMasterPO.getFuelExtraAmount());
				vehicleInvoiceDetails.get(0).setModifiedInvoiceRemarks(eFmFmVehicleMasterPO.getInvoiceRemarks());
				vehicleInvoiceDetails.get(0).setApprovalStatus(eFmFmVehicleMasterPO.getStatusFlg());
				iVehicleCheckInBO.invoiceUpdate(vehicleInvoiceDetails.get(0));
				// fixedDistanceVehicleDetails.put("failed","NOTVALIDUSER");

			}
		}

		List<Map<String, Object>> tripInvoiceBasedVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmVendorContractInvoicePO> vendorSummaryInvoiceDetails = iVehicleCheckInBO
				.getInvoiceDetailsById(eFmFmVehicleMasterPO.getBranchId(), eFmFmVehicleMasterPO.getInvoiceId());
		if (!(vendorSummaryInvoiceDetails.isEmpty())) {
			double totalAmount = 0.0, penalty = 0.0, tripAmount = 0.0, totalBaseAmount = 0.0, serviceTax = 0.0,
					totalServiceTaxAmount = 0.0;
			int noOfvehicle = 0;
			for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : vendorSummaryInvoiceDetails) {
				fixedDistanceVehicleDetails.put("vendorName",
						vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				fixedDistanceVehicleDetails.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
				fixedDistanceVehicleDetails.put("invoiceMonthDate",
						monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
				fixedDistanceVehicleDetails.put("invoiceCreationDate",
						formatter.format(vendorInvoiceDetailsList.getCreationTime()));
				penalty = penalty + vendorInvoiceDetailsList.getTotalDeductibles();
				if (vendorInvoiceDetailsList.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
						.getContractType().equalsIgnoreCase("FDC")
						|| vendorInvoiceDetailsList.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
								.getContractType().equalsIgnoreCase("PDDC")) {
					Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
					totalAmount = totalAmount + vendorInvoiceDetailsList.getTotalAmountPayable();
					totalBaseAmount = totalBaseAmount + vendorInvoiceDetailsList.getBaseTotal();
					fixedDistanceTrips.put("invoiceType", vendorInvoiceDetailsList.getInvoiceType());
					fixedDistanceTrips.put("vehicleNumber",
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
					fixedDistanceTrips.put("totalKm", vendorInvoiceDetailsList.getTotalDistance());
					fixedDistanceTrips.put("extraKm", vendorInvoiceDetailsList.getTotalExtraDistance());
					fixedDistanceTrips.put("totalWorkingDays", vendorInvoiceDetailsList.getPresentDays());
					fixedDistanceTrips.put("fixedcharges", vendorInvoiceDetailsList.getBaseTotal());
					fixedDistanceTrips.put("penalty", vendorInvoiceDetailsList.getTotalDeductibles());
					fixedDistanceTrips.put("totalDeduction", vendorInvoiceDetailsList.getTotalDeductibles());
					fixedDistanceTrips.put("invoiceRemarks", vendorInvoiceDetailsList.getInvoiceRemarks());
					fixedDistanceTrips.put("extraKmcharges", vendorInvoiceDetailsList.getExtraDistanceCharge());
					fixedDistanceTrips.put("totalAmount", vendorInvoiceDetailsList.getTotalAmountPayable());

					fixedDistanceTrips.put("totalPerDayDeduction",
							vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());
					fixedDistanceTrips.put("totalpenalityAmnt", vendorInvoiceDetailsList.getTotalPenalityAmount());
					fixedDistanceTrips.put("tripConsolidatedAmount", vendorInvoiceDetailsList.getTripTotalAmount());
					fixedDistanceTrips.put("contractedKm", vendorInvoiceDetailsList.getBaseDistance());
					fixedDistanceTrips.put("contractAmount", vendorInvoiceDetailsList.getBaseTotal());
					fixedDistanceTrips.put("contractKM", vendorInvoiceDetailsList.getBaseDistance());
					fixedDistanceTrips.put("perKmCost",
							vendorInvoiceDetailsList.geteFmFmContractDetails().getPerKmCost());
					fixedDistanceTrips.put("contractType", vendorInvoiceDetailsList.geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType());
					if (vendorInvoiceDetailsList.getPresentDays() < vehicleInvoiceDetails.get(0).getWorkingDays()) {
						fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
								- vendorInvoiceDetailsList.getPresentDays()));
					} else {
						fixedDistanceTrips.put("absentDays", "NO");
					}
					fixedDistanceTrips.put("totalAmountExtraKmCharge", "0");
					fixedDistanceTrips.put("vendorName",
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					fixedDistanceTrips.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
					fixedDistanceTrips.put("invoiceMonthDate",
							monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
					fixedDistanceTrips.put("invoiceCreationDate",
							formatter.format(vendorInvoiceDetailsList.getCreationTime()));
					fixedDistanceTrips.put("approvalStatus", vendorInvoiceDetailsList.getApprovalStatus());
					fixedDistanceTrips.put("fuelExtraAmount", vendorInvoiceDetailsList.getFuelExtraAmount());
					invoiceVendorDetails.add(fixedDistanceTrips);
					noOfvehicle++;
				}
			}

			List<EFmFmVendorContractInvoicePO> vendorAmountSummary = iVehicleCheckInBO.getModifiedAmountDetails(
					eFmFmVehicleMasterPO.getBranchId(), eFmFmVehicleMasterPO.getInvoiceNumber());
			totalAmount = vendorAmountSummary.get(0).getTotalAmountPayable();
			penalty = vendorAmountSummary.get(0).getTotalPenalityAmount();
			serviceTax = (totalAmount + tripAmount) * vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
					.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax() / 100;
			totalServiceTaxAmount = (totalAmount + tripAmount) + serviceTax;
			fixedDistanceVehicleDetails.put("totalAmount", (double) Math.round(totalBaseAmount * 100) / 100);
			fixedDistanceVehicleDetails.put("penalty", (double) Math.round(penalty * 100) / 100);
			fixedDistanceVehicleDetails.put("totalPayableAmount",
					(double) Math.round((totalAmount + tripAmount) * 100) / 100);
			fixedDistanceVehicleDetails.put("serviceTax", vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
					.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax());
			fixedDistanceVehicleDetails.put("serviceTaxAmount", (double) Math.round(serviceTax * 100) / 100);
			fixedDistanceVehicleDetails.put("total", (double) Math.round(totalServiceTaxAmount * 100) / 100);
			fixedDistanceVehicleDetails.put("noOfvehicle", noOfvehicle);
			fixedDistanceVehicleDetails.put("fixedDistanceBased", invoiceVendorDetails);
			fixedDistanceVehicleDetails.put("tripBasedFixedDetails", tripInvoiceBasedVehicleDetails);
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(fixedDistanceVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/operatingReports")
	public Response expenseReport(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<Map<String, Object>> expenseReportData = new ArrayList<Map<String, Object>>();
		String activityType = eFmFmVehicleMasterPO.getActionType();
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
		Date toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
		System.out.println("activityType" + activityType);
		int requestingDays = 7;
		switch (activityType.toUpperCase().trim()) {
		case "VENDORBASED":
			/*
			 * fromDate =(Date)
			 * monthDate.parse(eFmFmVehicleMasterPO.getFromDate()); toDate =
			 * (Date) monthDate.parse(eFmFmVehicleMasterPO.getToDate());
			 */
			// Get all vehicles by Vendor Id and branchId
			List<EFmFmVehicleMasterPO> allVehiclesDetail = iVehicleCheckInBO.getAllApprovedVehiclesByVendorId(27,
					clientBranchPO.getBranchId());
			if (!(allVehiclesDetail.isEmpty())) {
				for (EFmFmVehicleMasterPO vehicleDetails : allVehiclesDetail) {
					Map<String, Object> expenseReportDataDetails = new HashMap<String, Object>();
					List<EFmFmAssignRoutePO> assignRouteDetail = iAssignRouteBO.getAllVehicleDistance(fromDate, toDate,
							clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());
					if (vehicleDetails.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractType()
							.equalsIgnoreCase("FDC")) {
						if (!(assignRouteDetail.isEmpty())) {
							for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
								log.info("vehicle NUmber" + vehicleDetails.getVehicleNumber());
								double totalAmt = 0.0, travelledKmAmt = 0.0, totalDeductionAmnt = 0.0,
										workingDayAmt = 0.0, totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0,
										penaltyAmt = 0.0, penaltyTotalAmt = 0.0, tripBasedAmount = 0.0,
										extraKmCharges = 0.0, extraKm = 0.0, panalityAmount = 0.0, perKmCharges = 0.0;
								int absentDays = 0;
								List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
										.getFixedDistanceDetails(
												allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.geteFmFmContractDetails().getDistanceContractId(),
												clientBranchPO.getBranchId());
								if (!fixedDistanceDetails.isEmpty()) {
									List<EFmFmVehicleCheckInPO> totalWorkingDays = iVehicleCheckInBO
											.getVehicleWorkingDays(fromDate, toDate, clientBranchPO.getBranchId(),
													vehicleDetails.getVehicleId());
									log.info("total working days" + totalWorkingDays.size());
									List<EFmFmVehicleMasterPO> SumOftotalKm = iVehicleCheckInBO.getSumOfTravelledKm(
											fromDate, toDate, clientBranchPO.getBranchId(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleId(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster()
													.getContractType().trim(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.geteFmFmContractDetails().getDistanceContractId());
									log.info("SumOftotalKm" + SumOftotalKm.size());
									if (SumOftotalKm.get(0).getSumTravelledDistance() > 0) {
										perKmCharges = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getFixedDistanceMonthly();
										travelledKmAmt = SumOftotalKm.get(0).getSumTravelledDistance() * perKmCharges;
										if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
											absentDays = requestingDays - totalWorkingDays.size();
											penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											log.info("panalityAmount" + panalityAmount);
											penaltyTotalAmt = penaltyAmt
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay() / 100;
											log.info("penaltyTotalAmt" + penaltyTotalAmt);
											penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays())
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
											log.info("penalityPerday" + penalityPerday);
											workingDayAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays())
													* totalWorkingDays.size();
											totalDeductionAmnt = (fixedDistanceDetails.get(0)
													.getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
											// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
											// totalAmt=(SumOftotalKm.get(0).getSumTravelledDistance()
											// * perKmCharges) -
											// totalDeductionAmnt;
											totalAmt = workingDayAmt - totalDeductionAmnt;
										}

									}
									// fixedDistanceVehicleDetails.put("vendorName",
									// vehicleDetails.getEfmFmVendorMaster().getVendorName());
									expenseReportDataDetails.put("vehicleNumber", vehicleDetails.getVehicleNumber());
									expenseReportDataDetails
											.put("totalKm",
													(double) Math
															.round(SumOftotalKm.get(0).getSumTravelledDistance() * 100)
															/ 100);
									expenseReportDataDetails.put("perKmCharges", perKmCharges);
									expenseReportDataDetails.put("travelledKmAmt",
											(double) Math.round(travelledKmAmt * 100) / 100);
									expenseReportDataDetails.put("totalWorkingDays", totalWorkingDays.size());
									expenseReportDataDetails.put("absentDays", absentDays);
									expenseReportDataDetails.put("deduction",
											(double) Math.round(totalDeductionAmnt * 100) / 100);
									expenseReportDataDetails.put("penaltyTotalAmt",
											(double) Math.round(penaltyTotalAmt * 100) / 100);
									expenseReportDataDetails.put("workingDayAmt",
											(double) Math.round(workingDayAmt * 100) / 100);
									expenseReportDataDetails.put("totalDeduction",
											(double) Math.round((totalDeductionAmnt + penaltyTotalAmt) * 100) / 100);
									expenseReportDataDetails.put("totalAmount",
											(double) Math.round(totalAmt * 100) / 100);
									expenseReportData.add(expenseReportDataDetails);
								}

							}
						}

					}
				}
			}
			break;
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(expenseReportData, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Fuel calculation Done.
	 *
	 * @author Rajan R
	 * 
	 * @since 2015-05-29
	 * 
	 * @return fuel calculation details.
	 * @throws ParseException
	 */
	public double fuelCalculation(Date fromDate, Date toDate, EFmFmAssignRoutePO allVehicleDetails,
			EFmFmClientBranchPO eFmFmClientBranchPO, List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails,
			double SumOftotalKm, String distanceFlg) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		double totalSumOfAmount = 0.0;
		Date oneDayValidaton = null;
		double distance = 0.0;
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		List<EFmFmAssignRoutePO> vehicleKmDetails = iVehicleCheckInBO.getTotalKmByVehicle(fromDate, toDate,
				eFmFmClientBranchPO.getBranchId(),
				allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId(),
				fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType().trim(),
				fixedDistanceDetails.get(0).getDistanceContractId());
		if (!vehicleKmDetails.isEmpty()) {
			for (EFmFmAssignRoutePO routeDetails : vehicleKmDetails) {
				eFmFmAssignRoutePO.setAssignRouteId(routeDetails.getAssignRouteId());
				if (distanceFlg.equalsIgnoreCase("GPS")) {
					distance = routeDetails.getTravelledDistance();
				} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
					if (routeDetails.getOdometerEndKm() != null && !routeDetails.getOdometerEndKm().isEmpty()
							&& Double.parseDouble(routeDetails.getOdometerEndKm()) > Double
									.parseDouble(routeDetails.getOdometerStartKm())) {
						distance = (Double.parseDouble(routeDetails.getOdometerEndKm())
								- Double.parseDouble(routeDetails.getOdometerStartKm()));
					}
				}

				List<EFmFmFuelChargesPO> fuelChargesDetails = iVehicleCheckInBO.getFuelDetailsByDate(
						routeDetails.getTripAssignDate(),
						fixedDistanceDetails.get(0).geteFmFmVendorFuelContractTypeMaster().getFuelTypeId(),
						eFmFmClientBranchPO.getBranchId());
				if (!fuelChargesDetails.isEmpty()) {
					List<EFmFmAssignRoutePO> updateKm = iAssignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
					for (EFmFmFuelChargesPO fuelCharges : fuelChargesDetails) {
						boolean execution = false;

						if (fuelCharges.getToDate() == null
								&& (routeDetails.getTripAssignDate().after(fuelCharges.getFromDate())
										|| routeDetails.getTripAssignDate().equals(fuelCharges.getToDate()))) {
							execution = true;
						} else {
							if (fuelCharges.getToDate() == null) {
								execution = true;
							} else if (routeDetails.getTripAssignDate().before(fuelCharges.getToDate())
									|| routeDetails.getTripAssignDate().equals(fuelCharges.getToDate())) {
								execution = true;
							}
						}
						if (execution) {
							if (fuelCharges.geteFmFmVendorFuelContractTypeMaster().getContractType()
									.equalsIgnoreCase("M")) {
								// totalSumOfAmount=(fuelCharges.getNewPrice()-fixedDistanceDetails.get(0).getPetrolPrice())*fuelCharges.getPerMonthAmount();
								if (fuelCharges.getNewPrice() > 0 && fixedDistanceDetails.get(0).getPetrolPrice() > 0) {
									if (fuelCharges.getNewPrice() >= fixedDistanceDetails.get(0).getPetrolPrice()) {
										if (oneDayValidaton == null || (!dateFormate.format(oneDayValidaton)
												.equals(dateFormate.format(routeDetails.getTripAssignDate())))) {
											double totalAmount = (fuelCharges.getNewPrice()
													- fixedDistanceDetails.get(0).getPetrolPrice())
													* (fuelCharges.getPerMonthAmount()
															/ fixedDistanceDetails.get(0).getMinimumDays());
											updateKm.get(0)
													.setFuelKmAmount((double) Math.round(totalAmount * 100) / 100);
											iAssignRouteBO.update(updateKm.get(0));
											totalSumOfAmount += (double) Math.round(totalAmount * 100) / 100;
											oneDayValidaton = routeDetails.getTripAssignDate();
										}
									} else {
										System.out.println("old price Should not be greaterthan New Price");
									}
								} else {
									System.out.println("milage or Travel Distance Should not be Zero");
								}

							} else if (fuelCharges.geteFmFmVendorFuelContractTypeMaster().getContractType()
									.equalsIgnoreCase("K")) {
								if (routeDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getMileage() > 0
										&& distance > 0) {
									if (fuelCharges.getNewPrice() >= fixedDistanceDetails.get(0).getPetrolPrice()) {
										double totalAmount = (distance / routeDetails.getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getMileage())
												* (fuelCharges.getNewPrice()
														- fixedDistanceDetails.get(0).getPetrolPrice());
										updateKm.get(0).setFuelKmAmount((double) Math.round(totalAmount * 100) / 100);
										iAssignRouteBO.update(updateKm.get(0));
										totalSumOfAmount += (double) Math.round(totalAmount * 100) / 100;
									} else {
										System.out.println("old price Should not be greaterthan New Price");
									}
								} else {
									System.out.println("milage or Travel Distance Should not be Zero");
								}
							}
						}
					}

				} else {
					System.out.println("Need to add the fuel details");
				}
			}
		}
		return totalSumOfAmount;
	}

	/*
	 * Add new fuel details
	 */

	@POST
	@Path("/addFuelDetails")
	public Response addFuelDetaild(EFmFmFuelChargesPO eFmFmFuelChargesPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmFuelChargesPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmFuelChargesPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmFuelChargesPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = dateFormate.parse(eFmFmFuelChargesPO.getStartDate());
		eFmFmFuelChargesPO.setFromDate(startDate);
		List<EFmFmFuelChargesPO> fuelChargesDetails = iVehicleCheckInBO.getFuelDetails(
				eFmFmFuelChargesPO.geteFmFmVendorFuelContractTypeMaster().getFuelTypeId(),
				eFmFmFuelChargesPO.geteFmFmClientBranchPO().getBranchId());
		if (!fuelChargesDetails.isEmpty()) {
			if (fuelChargesDetails.get(0).getFromDate().after(startDate)) {
				responce.put("status", "Should not allowed the back date");
				log.info("serviceEnd -UserId :" + eFmFmFuelChargesPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else {
				if (eFmFmFuelChargesPO.getNewPrice() >= fuelChargesDetails.get(0).getNewPrice() || eFmFmFuelChargesPO
						.getNewPrice() >= fuelChargesDetails.get(fuelChargesDetails.size() - 1).getNewPrice()) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.add(Calendar.DATE, -1);
					fuelChargesDetails.get(0).setToDate(cal.getTime());
					iVehicleCheckInBO.update(fuelChargesDetails.get(0));
				} else {
					responce.put("status",
							"New Price Should not less than Contract petrol price or last entered petrol price");
					log.info("serviceEnd -UserId :" + eFmFmFuelChargesPO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
		}
		eFmFmFuelChargesPO.setCreatedBy("admin");
		eFmFmFuelChargesPO.setContractTitle(eFmFmFuelChargesPO.getContractTitle());
		eFmFmFuelChargesPO.setCreationTime(new Date());
		eFmFmFuelChargesPO.setStatus("Y");
		// eFmFmFuelChargesPO.setOldPrice(eFmFmFuelChargesPO.getOldPrice());
		eFmFmFuelChargesPO.setNewPrice(eFmFmFuelChargesPO.getNewPrice());
		eFmFmFuelChargesPO.setFuelType(eFmFmFuelChargesPO.getFuelType());
		eFmFmFuelChargesPO.setPerMonthAmount(eFmFmFuelChargesPO.getPerMonthAmount());
		iVehicleCheckInBO.save(eFmFmFuelChargesPO);
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmFuelChargesPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/addContractType")
	public Response addContractType(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO)
			throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorContractTypeMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorContractTypeMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmVendorContractTypeMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmVendorContractTypeMasterPO> conractDetails = iVehicleCheckInBO.validateVendorContractTypeDetails(
				eFmFmVendorContractTypeMasterPO.getContractType(),
				eFmFmVendorContractTypeMasterPO.geteFmFmClientBranchPO().getBranchId());
		if (conractDetails.isEmpty()) {
			EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
			eFmFmClientBranchPO.setBranchId(eFmFmVendorContractTypeMasterPO.geteFmFmClientBranchPO().getBranchId());
			eFmFmVendorContractTypeMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			eFmFmVendorContractTypeMasterPO.setContractStatus("Y");
			eFmFmVendorContractTypeMasterPO.setCreatedBy(String.valueOf(eFmFmVendorContractTypeMasterPO.getUserId()));
			eFmFmVendorContractTypeMasterPO.setCreationTime(new Date());
			iVehicleCheckInBO.save(eFmFmVendorContractTypeMasterPO);
		} else {
			responce.put("status", "alreadyExist");
			log.info("serviceEnd -UserId :" + eFmFmVendorContractTypeMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmVendorContractTypeMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/getAllContractType")
	public Response getAllContractType(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO)
			throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorContractTypeMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorContractTypeMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmVendorContractTypeMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<Map<String, Object>> conractData = new ArrayList<Map<String, Object>>();
		List<EFmFmVendorContractTypeMasterPO> conractDetails = iVehicleCheckInBO
				.getVendorContractTypeDetails(eFmFmVendorContractTypeMasterPO.geteFmFmClientBranchPO().getBranchId());
		if (!conractDetails.isEmpty()) {
			for (EFmFmVendorContractTypeMasterPO conractDetailsList : conractDetails) {
				Map<String, Object> listofContract = new HashMap<String, Object>();
				listofContract.put("contractTypeId", conractDetailsList.getContractTypeId());
				listofContract.put("contractType", conractDetailsList.getContractType());
				listofContract.put("contractDescription", conractDetailsList.getContractDescription());
				listofContract.put("serviceTax", conractDetailsList.getServiceTax());
				listofContract.put("contractStatus", conractDetailsList.getContractStatus());
				listofContract.put("editOptionRequired", conractDetailsList.getEditOptionRequired());
				conractData.add(listofContract);
			}
		}
		return Response.ok(conractData, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updateContractType")
	public Response updateContractType(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO)
			throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorContractTypeMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorContractTypeMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmVendorContractTypeMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		Map<String, Object> listofContract = new HashMap<String, Object>();
		listofContract.put("status", "failure");
		List<EFmFmVendorContractTypeMasterPO> conractDetails = iVehicleCheckInBO.getContractTypeIdDetails(
				eFmFmVendorContractTypeMasterPO.getContractTypeId(),
				eFmFmVendorContractTypeMasterPO.geteFmFmClientBranchPO().getBranchId());
		if (!conractDetails.isEmpty()) {
			conractDetails.get(0).setContractType(eFmFmVendorContractTypeMasterPO.getContractType());
			conractDetails.get(0).setContractDescription(eFmFmVendorContractTypeMasterPO.getContractDescription());
			conractDetails.get(0).setServiceTax(eFmFmVendorContractTypeMasterPO.getServiceTax());
			conractDetails.get(0).setContractStatus(eFmFmVendorContractTypeMasterPO.getContractStatus());
			iVehicleCheckInBO.update(conractDetails.get(0));
			listofContract.put("status", "success");
		}
		return Response.ok(listofContract, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/getFuelDetails")
	public Response getFuelDetails(EFmFmFuelChargesPO eFmFmFuelChargesPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmFuelChargesPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmFuelChargesPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmFuelChargesPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		List<EFmFmFuelChargesPO> fuelChargesDetails = iVehicleCheckInBO
				.getAllFuelDetails(eFmFmFuelChargesPO.geteFmFmClientBranchPO().getBranchId());
		List<Map<String, Object>> fuelDetailsList = new ArrayList<Map<String, Object>>();
		if (!(fuelChargesDetails.isEmpty())) {
			for (EFmFmFuelChargesPO fuelDetails : fuelChargesDetails) {
				Map<String, Object> FuelList = new HashMap<String, Object>();
				FuelList.put("startDate", formatter.format(fuelDetails.getFromDate()));
				FuelList.put("contractTitle", fuelDetails.getContractTitle());
				FuelList.put("newPrice", fuelDetails.getNewPrice());
				FuelList.put("OldPrice", fuelDetails.getOldPrice());
				FuelList.put("fuelType", fuelDetails.getFuelType());
				FuelList.put("tariffFlg", fuelDetails.geteFmFmVendorFuelContractTypeMaster().getContractType());
				FuelList.put("perMonthAmount", fuelDetails.getPerMonthAmount());
				FuelList.put("fuelContractDesc",
						fuelDetails.geteFmFmVendorFuelContractTypeMaster().getContractDescription());
				if (fuelDetails.getToDate() == null) {
					FuelList.put("endDate", "");
				} else {
					FuelList.put("endDate", formatter.format(fuelDetails.getToDate()));
				}
				fuelDetailsList.add(FuelList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmFuelChargesPO.getUserId());
		return Response.ok(fuelDetailsList, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Add new contract details
	 */

	@POST
	@Path("/addContractDetails")
	public Response addNewContractDetails(EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO)
			throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmFixedDistanceContractDetailPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmFixedDistanceContractDetailPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmFixedDistanceContractDetailPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		/*
		 * EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMaster=new
		 * EFmFmVendorContractTypeMasterPO();
		 */

		log.info("Fuel Contract Type" + eFmFmFixedDistanceContractDetailPO.getFuelPriceCalculation());
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date startDate = dateFormate.parse(eFmFmFixedDistanceContractDetailPO.getStartDate());
		Date endDate = dateFormate.parse(eFmFmFixedDistanceContractDetailPO.getEndDate());

		log.info("adding contract Details");
		List<EFmFmFixedDistanceContractDetailPO> contractExit = iVehicleCheckInBO
				.getFixedDistanceContrctTitleExistDetails(eFmFmFixedDistanceContractDetailPO.getContractTitle().trim(),
						eFmFmFixedDistanceContractDetailPO.geteFmFmClientBranchPO().getBranchId());
		if (contractExit.isEmpty()) {
			if (eFmFmFixedDistanceContractDetailPO.getPenalty().equalsIgnoreCase("NO")) {
				eFmFmFixedDistanceContractDetailPO.setPenalty("N");
			} else {
				eFmFmFixedDistanceContractDetailPO.setPenalty("Y");
			}
			eFmFmFixedDistanceContractDetailPO.setCreatedBy("admin");
			eFmFmFixedDistanceContractDetailPO.setCreationTime(new Date());
			eFmFmFixedDistanceContractDetailPO.setUpdatedBy("admin");
			eFmFmFixedDistanceContractDetailPO.setUpdatedTime(new Date());
			eFmFmFixedDistanceContractDetailPO.setFromDate(startDate);
			eFmFmFixedDistanceContractDetailPO.setToDate(endDate);
			if (eFmFmFixedDistanceContractDetailPO.getCloneId() != 0) {
				eFmFmFixedDistanceContractDetailPO.setCloneId(eFmFmFixedDistanceContractDetailPO.getCloneId());
				List<EFmFmFixedDistanceContractDetailPO> contractDetails = iVehicleCheckInBO.getFixedDistanceDetails(
						eFmFmFixedDistanceContractDetailPO.getCloneId(),
						eFmFmFixedDistanceContractDetailPO.geteFmFmClientBranchPO().getBranchId());
				if (eFmFmFixedDistanceContractDetailPO.getFromDate().equals(contractDetails.get(0).getFromDate())) {
					contractDetails.get(0).setUpdatedTime(new Date());
					contractDetails.get(0).setStatus("N");
				} else {
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.add(Calendar.DATE, -1);
					contractDetails.get(0).setToDate(cal.getTime());
					contractDetails.get(0).setUpdatedTime(new Date());
				}
				iVehicleCheckInBO.update(contractDetails.get(0));
			} else {
				eFmFmFixedDistanceContractDetailPO.setCloneId(0);
			}
			eFmFmFixedDistanceContractDetailPO.setStatus("Y");
			eFmFmFixedDistanceContractDetailPO.setPerDayCost(eFmFmFixedDistanceContractDetailPO.getPerDayCost());
			eFmFmFixedDistanceContractDetailPO.setPetrolPrice(eFmFmFixedDistanceContractDetailPO.getPetrolPrice());
			eFmFmFixedDistanceContractDetailPO.setPerKmCost(eFmFmFixedDistanceContractDetailPO.getPerKmCost());
			if (eFmFmFixedDistanceContractDetailPO.getFuelPriceCalculation().equalsIgnoreCase("NR")) {
				List<EFmFmVendorFuelContractTypeMasterPO> fuelDetails = iVehicleCheckInBO.getDummyFuelType(
						eFmFmFixedDistanceContractDetailPO.getFuelPriceCalculation(),
						eFmFmFixedDistanceContractDetailPO.geteFmFmClientBranchPO().getBranchId());
				if (!(fuelDetails.isEmpty())) {
					eFmFmFixedDistanceContractDetailPO.seteFmFmVendorFuelContractTypeMaster(fuelDetails.get(0));
				} else {
					responce.put("status", "FUELTYPENOTEXIST");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			} else {
				eFmFmFixedDistanceContractDetailPO.seteFmFmVendorFuelContractTypeMaster(
						eFmFmFixedDistanceContractDetailPO.geteFmFmVendorFuelContractTypeMaster());
			}

			eFmFmFixedDistanceContractDetailPO
					.seteFmFmClientBranchPO(eFmFmFixedDistanceContractDetailPO.geteFmFmClientBranchPO());
			eFmFmFixedDistanceContractDetailPO.seteFmFmVendorContractTypeMaster(
					eFmFmFixedDistanceContractDetailPO.geteFmFmVendorContractTypeMaster());
			iVehicleCheckInBO.save(eFmFmFixedDistanceContractDetailPO);
			responce.put("status", "success");
		} else {
			responce.put("status", "AlreadyExist");
		}
		log.info("serviceEnd -UserId :" + eFmFmFixedDistanceContractDetailPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/activeContractDetails")
	public Response getActiveContractDetails(EFmFmClientBranchPO eFmFmClientBranchPO) throws ParseException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		List<EFmFmFixedDistanceContractDetailPO> contractTypes = iVehicleCheckInBO
				.getFixedDistanceActiveContractDetails(eFmFmClientBranchPO.getBranchId());
		List<Map<String, Object>> invoiceList = new ArrayList<Map<String, Object>>();
		if (!(contractTypes.isEmpty())) {
			for (EFmFmFixedDistanceContractDetailPO invoiceDetails : contractTypes) {
				Map<String, Object> contractList = new HashMap<String, Object>();
				contractList.put("contractTitle", invoiceDetails.getContractTitle());
				contractList.put("contractType", invoiceDetails.geteFmFmVendorContractTypeMaster().getContractType());
				contractList.put("contractId", invoiceDetails.getDistanceContractId());
				contractList.put("extraDistanceChargeRate", invoiceDetails.getExtraDistanceChargeRate());
				contractList.put("fixedDistanceChargeRate", invoiceDetails.getFixedDistanceChargeRate());
				contractList.put("contractDistance", invoiceDetails.getFixedDistanceMonthly());
				contractList.put("perKmCost", invoiceDetails.getPerKmCost());
				contractList.put("fixedDistancePerDay", invoiceDetails.getFixedDistancePrDay());
				contractList.put("contractDate", formatter.format(invoiceDetails.getCreationTime()));
				if (invoiceDetails.getToDate() == null) {
					contractList.put("startDate", "");
					contractList.put("endDate", "");
				} else {
					contractList.put("startDate", formatter.format(invoiceDetails.getFromDate()));
					contractList.put("endDate", formatter.format(invoiceDetails.getToDate()));
				}
				contractList.put("panalityPercentage", invoiceDetails.getPenaltyInPercentagePerDay());
				contractList.put("contractType", invoiceDetails.geteFmFmVendorContractTypeMaster().getContractType());
				contractList.put("serviceTax", invoiceDetails.geteFmFmVendorContractTypeMaster().getServiceTax());
				contractList.put("minimumDays", invoiceDetails.getMinimumDays());
				contractList.put("perDayCost", invoiceDetails.getPerDayCost());
				if (invoiceDetails.getPenalty().equalsIgnoreCase("Y")) {
					contractList.put("penaltyFlg", "Yes");
				} else {
					contractList.put("penaltyFlg", "No");
				}
				contractList.put("petrolPrice", invoiceDetails.getPetrolPrice());
				contractList.put("fuelContractId",
						invoiceDetails.geteFmFmVendorFuelContractTypeMaster().getFuelTypeId());
				contractList.put("fuelContractDesc",
						invoiceDetails.geteFmFmVendorFuelContractTypeMaster().getContractDescription());
				invoiceList.add(contractList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(invoiceList, MediaType.APPLICATION_JSON).build();
	}

	/* vehicle Details for Invoice */

	@POST
	@Path("/listOfInvoiceVehicleDetails")
	public Response listOfInvoiceVehicleDetails(EFmFmClientBranchPO eFmFmClientBranchPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmVendorContractInvoicePO> listInvoiceDetails = iVehicleCheckInBO
				.getListOfInvoiceNumbers(eFmFmClientBranchPO.getBranchId());
		List<Map<String, Object>> invoiceList = new ArrayList<Map<String, Object>>();
		if (!(listInvoiceDetails.isEmpty())) {

			for (EFmFmVendorContractInvoicePO invoiceDetails : listInvoiceDetails) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("invoiceNumber", invoiceDetails.getInvoiceNumber());
				invoiceList.add(vehicleList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(invoiceList, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/listOfInvoiceDetails")
	public Response listOfInvoiceDetails(EFmFmClientBranchPO eFmFmClientBranchPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmVendorContractInvoicePO> listInvoiceDetails = iVehicleCheckInBO
				.getListOfInvoiceNumbers(eFmFmClientBranchPO.getBranchId());
		List<Map<String, Object>> invoiceList = new ArrayList<Map<String, Object>>();
		if (!(listInvoiceDetails.isEmpty())) {
			for (EFmFmVendorContractInvoicePO invoiceDetails : listInvoiceDetails) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("invoiceNumber", invoiceDetails.getInvoiceNumber());
				invoiceList.add(vehicleList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(invoiceList, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * 
	 * Contract Type Details
	 * 
	 * @return List of Contract Types
	 */

	@POST
	@Path("/listOfContractType")
	public Response listOfContractType(EFmFmClientBranchPO eFmFmClientBranchPO) throws ParseException {

		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmVendorContractTypeMasterPO> contractTypeDetails = iVehicleCheckInBO
				.getAllContractType(eFmFmClientBranchPO.getBranchId());
		List<Map<String, Object>> ContractTypeList = new ArrayList<Map<String, Object>>();
		if (!(contractTypeDetails.isEmpty())) {
			for (EFmFmVendorContractTypeMasterPO contractDetails : contractTypeDetails) {
				Map<String, Object> typeList = new HashMap<String, Object>();
				typeList.put("contractId", contractDetails.getContractTypeId());
				typeList.put("contractType", contractDetails.getContractType());
				ContractTypeList.add(typeList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(ContractTypeList, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/listOfFuelContractType")
	public Response listOfFuelContractType(EFmFmClientBranchPO eFmFmClientBranchPO) throws ParseException {

		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmVendorFuelContractTypeMasterPO> contractTypeDetails = iVehicleCheckInBO
				.getAllContractFuelType(eFmFmClientBranchPO.getBranchId());
		List<Map<String, Object>> ContractTypeList = new ArrayList<Map<String, Object>>();
		if (!(contractTypeDetails.isEmpty())) {
			for (EFmFmVendorFuelContractTypeMasterPO contractDetails : contractTypeDetails) {
				Map<String, Object> typeList = new HashMap<String, Object>();
				typeList.put("fuelContractId", contractDetails.getFuelTypeId());
				typeList.put("contractDescription", contractDetails.getContractDescription());
				typeList.put("contractFuelType", contractDetails.getContractType());
				ContractTypeList.add(typeList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(ContractTypeList, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * 
	 * updating FDC contract Details
	 * 
	 * @return Failure or Success
	 */

	@POST
	@Path("/updateFDCDetails")
	public Response updateFDCDetails(EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO)
			throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmFixedDistanceContractDetailPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmFixedDistanceContractDetailPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmFixedDistanceContractDetailPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		Map<String, Object> requests = new HashMap<String, Object>();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date startDate = formatter.parse(eFmFmFixedDistanceContractDetailPO.getStartDate());
		Date endDate = formatter.parse(eFmFmFixedDistanceContractDetailPO.getEndDate());
		requests.put("status", "failure");
		List<EFmFmFixedDistanceContractDetailPO> contractDetails = iVehicleCheckInBO
				.getFixedModifyDistanceDetails(eFmFmFixedDistanceContractDetailPO);
		if (!(contractDetails.isEmpty())) {
			contractDetails.get(0).setUpdatedTime(new Date());
			contractDetails.get(0).setContractTitle(eFmFmFixedDistanceContractDetailPO.getContractTitle());
			contractDetails.get(0)
					.setExtraDistanceChargeRate(eFmFmFixedDistanceContractDetailPO.getExtraDistanceChargeRate());
			contractDetails.get(0)
					.setFixedDistanceChargeRate(eFmFmFixedDistanceContractDetailPO.getFixedDistanceChargeRate());
			contractDetails.get(0)
					.setFixedDistanceMonthly(eFmFmFixedDistanceContractDetailPO.getFixedDistanceMonthly());
			contractDetails.get(0).setFixedDistancePrDay(eFmFmFixedDistanceContractDetailPO.getFixedDistancePrDay());
			contractDetails.get(0).setFromDate(startDate);
			contractDetails.get(0).setToDate(endDate);
			contractDetails.get(0).setMinimumDays(eFmFmFixedDistanceContractDetailPO.getMinimumDays());
			contractDetails.get(0).setPenalty(eFmFmFixedDistanceContractDetailPO.getPenalty());
			contractDetails.get(0)
					.setPenaltyInPercentagePerDay(eFmFmFixedDistanceContractDetailPO.getPenaltyInPercentagePerDay());
			requests.put("status", "success");
			iVehicleCheckInBO.update(contractDetails.get(0));
		}
		log.info("serviceEnd -UserId :" + eFmFmFixedDistanceContractDetailPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * get Particular vendors Checked In driver and vehicle details
	 */
	@POST
	@Path("/getCheckInEntity")
	public Response allCheckedActiveVehicleAndDrivers(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		EFmFmVehicleMasterPO eFmFmVehicleMasterPO = new EFmFmVehicleMasterPO();
		eFmFmVehicleMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);
		List<EFmFmVehicleMasterPO> allActiveVehicle = iVehicleCheckInBO
				.getAllApprovedVehiclesByVendorId(eFmFmVendorMasterPO.getVendorId(), eFmFmVendorMasterPO.getBranchId());
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveVehicle.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : allActiveVehicle) {
				// if(vehicleDetails.getStatus().equalsIgnoreCase("A")){
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleNumber", vehicleDetails.getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vehicleType", vehicleDetails.getVehicleModel());
				vehicleList.put("driverName", vehicleDetails.getFirstName());
				vehicleList.put("driverId", "");
				requests.add(vehicleList);
				// }
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * get Particular vendors Checked In driver and vehicle details
	 */
	@POST
	@Path("/getVehicleDriverCheckInEntity")
	public Response allVehicleDriverCheckInEntity(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		EFmFmVehicleMasterPO eFmFmVehicleMasterPO = new EFmFmVehicleMasterPO();
		eFmFmVehicleMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);
		List<EFmFmVehicleCheckInPO> allActiveVehicle = iVehicleCheckInBO
				.getParticularCheckedInDriverDetailsByBranchIdAndVendorId(eFmFmVendorMasterPO.getCombinedFacility(),
						eFmFmVendorMasterPO.getVendorId());
		
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleDetails : allActiveVehicle) {
				// if(vehicleDetails.getStatus().equalsIgnoreCase("A")){
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleNumber", vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails.getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vehicleType", vehicleDetails.getEfmFmVehicleMaster().getVehicleModel());
				vehicleList.put("driverName", vehicleDetails.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("driverId", vehicleDetails.getEfmFmDriverMaster().getDriverId());
				requests.add(vehicleList);
				// }
			}
		}
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * All active drivers
	 */
	@POST
	@Path("/allActiveVehicle")
	public Response allActiveVehicle(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		EFmFmVehicleMasterPO eFmFmVehicleMasterPO = new EFmFmVehicleMasterPO();
		eFmFmVehicleMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);
		List<EFmFmVehicleMasterPO> allActiveVehicle = iVehicleCheckInBO.getAllVehicleDetails(eFmFmVehicleMasterPO);
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveVehicle.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : allActiveVehicle) {
				// if(vehicleDetails.getStatus().equalsIgnoreCase("A")){
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleNumber", vehicleDetails.getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vehicleType", vehicleDetails.getVehicleModel());

				requests.add(vehicleList);
				// }
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * All checkin driver by vendor and by vehicle
	 */

	@POST
	@Path("/getVehicleCheckInDriver")
	public Response getVehicleCheckInDrivers(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmVehicleCheckInPO> allActiveDrivers = iVehicleCheckInBO.getAllCheckedInDriversByVendorIdAndVehicleNum(
				eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId(),
				eFmFmVendorMasterPO.getVehicleId());
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveDrivers.isEmpty())) {
			for (EFmFmVehicleCheckInPO driverDetails : allActiveDrivers) {
				Map<String, Object> driverList = new HashMap<String, Object>();
				driverList.put("driverName", driverDetails.getEfmFmDriverMaster().getFirstName());
				driverList.put("driverId", driverDetails.getEfmFmDriverMaster().getDriverId());
				requests.add(driverList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * All active driver by vendor and by branch
	 */

	@POST
	@Path("/allActiveDrivers")
	public Response allActiveDrivers(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmDriverMasterPO> allActiveDrivers = iVehicleCheckInBO.getAllDriverDetailsByBranchIdAndVendorId(
				eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId());
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveDrivers.isEmpty())) {
			for (EFmFmDriverMasterPO driverDetails : allActiveDrivers) {
				Map<String, Object> driverList = new HashMap<String, Object>();
				driverList.put("driverName", driverDetails.getFirstName());
				driverList.put("driverId", driverDetails.getDriverId());
				requests.add(driverList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * get allEnanbleVehicles by branch
	 * 
	 */

	@POST
	@Path("/allEnanbleVehicles")
	public Response allActiveVehicles(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		List<EFmFmVehicleMasterPO> allActiveVehicles = iVehicleCheckInBO
				.getAllVehicleDetailsByBranchId(eFmFmVendorMasterPO.getBranchId());
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveVehicles.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : allActiveVehicles) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleNumber", vehicleDetails.getVehicleNumber());
				vehicleList.put("vendorName", vehicleDetails.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vehicleType", vehicleDetails.getVehicleModel());
				vehicleList.put("vehicleContractedKm", 4000);

				if (vehicleDetails.getMonthlyPendingFixedDistance() > 4000) {
					vehicleList.put("extarKm", Math.round((vehicleDetails.getMonthlyPendingFixedDistance() - 4000)));
					vehicleList.put("pendingKm", 0);
					vehicleList.put("travelledKm", Math.round(vehicleDetails.getMonthlyPendingFixedDistance()));
				} else {
					if (vehicleDetails.getMonthlyPendingFixedDistance() < 0) {
						vehicleList.put("pendingKm", 0);
						vehicleList.put("travelledKm",
								(4000 - (Math.round(vehicleDetails.getMonthlyPendingFixedDistance()))));
						vehicleList.put("extarKm", (0 - (Math.round(vehicleDetails.getMonthlyPendingFixedDistance()))));
					} else {
						vehicleList.put("pendingKm", (Math.round(vehicleDetails.getMonthlyPendingFixedDistance())));
						vehicleList.put("travelledKm",
								(4000 - (Math.round(vehicleDetails.getMonthlyPendingFixedDistance()))));
						vehicleList.put("extarKm", 0);
					}
				}
				requests.add(vehicleList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * get allEnanbleVehicles by branch
	 * 
	 */

	@POST
	@Path("/updatePendingKm")
	public Response updateVehiclePendingKm(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		EFmFmVehicleMasterPO allActiveVehicles = iVehicleCheckInBO
				.getParticularVehicleDetail(eFmFmVehicleMasterPO.getVehicleId());
		if (eFmFmVehicleMasterPO.getMonthlyPendingFixedDistance() > 4000) {
			allActiveVehicles
					.setMonthlyPendingFixedDistance(4000 - (eFmFmVehicleMasterPO.getMonthlyPendingFixedDistance()));
		} else {
			allActiveVehicles.setMonthlyPendingFixedDistance(eFmFmVehicleMasterPO.getMonthlyPendingFixedDistance());
		}
		iVehicleCheckInBO.update(allActiveVehicles);
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/allActiveVendor")
	public Response allActiveVendor(EFmFmClientBranchPO eFmFmClientBranchPO) {
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmVendorMasterPO> allActiveVendor = approvalBO.getAllApprovedVendors(new MultifacilityService()
				.combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(), eFmFmClientBranchPO.getCombinedFacility()));
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(allActiveVendor.isEmpty())) {
			for (EFmFmVendorMasterPO vendorDetails : allActiveVendor) {
				Map<String, Object> vendorList = new HashMap<String, Object>();
				vendorList.put("name", vendorDetails.getVendorName());
				vendorList.put("vendorId", vendorDetails.getVendorId());
				requests.add(vendorList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Adding Record into Invoice Table
	 */
	private void addInvoiceRecord(double totalPerDayDeductionAmnt, double penalityPerday, double tripBasedAmount,
			EFmFmAssignRoutePO allVehicleDetails, EFmFmClientBranchPO clientBranchPO, Date fromDate, Date endDate,
			double totalAmt, double penaltyTotalAmt, int noOfDays, double travelledDistance, double fixedDistance,
			int workingDays, double fixedDistanceChargeRate, long invoiceNumber, double tripTravelledDistance,
			double extraKm, double extraKmCharges, EFmFmVehicleMasterPO eFmFmVehicleMasterPO, int contractDetailsId,
			double totalSumOfAmount, String distanceFlg, double perdayCost, double totalKmAmount, int userId,
			double tripAmount) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO = new EFmFmVendorContractInvoicePO();
		eFmFmVendorContractInvoicePO.seteFmFmClientBranchPO(clientBranchPO);
		eFmFmVendorContractInvoicePO.setEfmFmVehicleMaster(eFmFmVehicleMasterPO);
		eFmFmVendorContractInvoicePO.setInvoiceType(eFmFmVehicleMasterPO.geteFmFmContractDetails()
				.geteFmFmVendorContractTypeMaster().getContractDescription());
		eFmFmVendorContractInvoicePO.setEfmFmAssignRoute(allVehicleDetails);

		eFmFmVendorContractInvoicePO.setTotalAmountPayable(Math.round(totalAmt * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setCreationTime(new Date());
		eFmFmVendorContractInvoicePO.setInvoiceGenerationDate(new Date());
		eFmFmVendorContractInvoicePO.setInvoiceStartDate(fromDate);
		eFmFmVendorContractInvoicePO.setInvoiceEndDate(endDate);
		eFmFmVendorContractInvoicePO.setTripTotalAmount(Math.round(tripBasedAmount * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setTotalPenalityAmount(Math.round(penalityPerday * 100.0) / 100.0);

		eFmFmVendorContractInvoicePO.setTotalPerDayDeductionAmnt(Math.round(totalPerDayDeductionAmnt * 100.0) / 100.0);
		// eFmFmVendorContractInvoicePO.setEfmFmVehicleMaster(efmFmVehicleMaster);
		eFmFmVendorContractInvoicePO.setInvoiceStatus("A");
		eFmFmVendorContractInvoicePO.setTotalDeductibles(Math.round(penaltyTotalAmt * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setPresentDays(noOfDays);
		eFmFmVendorContractInvoicePO.setTotalDistance(Math.round(travelledDistance * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setBaseDistance(Math.round(fixedDistance * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setWorkingDays(workingDays);
		eFmFmVendorContractInvoicePO.setBaseTotal(Math.round(fixedDistanceChargeRate * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setUpdatedTime(new Date());
		eFmFmVendorContractInvoicePO.setInvoiceNumber(invoiceNumber);
		eFmFmVendorContractInvoicePO.setTravelledDistance(Math.round(tripTravelledDistance * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setExtraDistanceCharge(Math.round(extraKmCharges * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO.setTotalExtraDistance(Math.round(extraKm * 100.0) / 100.0);
		EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO = new EFmFmFixedDistanceContractDetailPO();
		eFmFmFixedDistanceContractDetailPO.setDistanceContractId(contractDetailsId);
		eFmFmVendorContractInvoicePO.seteFmFmContractDetails(eFmFmFixedDistanceContractDetailPO);
		eFmFmVendorContractInvoicePO.setDistanceFlg(distanceFlg);
		eFmFmVendorContractInvoicePO.setPerDayCost(perdayCost);
		eFmFmVendorContractInvoicePO.setTotalKmAmount(Math.round(totalKmAmount * 100.0) / 100.0);
		eFmFmVendorContractInvoicePO
				.setMileage(allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getMileage());
		eFmFmVendorContractInvoicePO.setFuelExtraAmount(totalSumOfAmount);
		eFmFmVendorContractInvoicePO.setCreatedBy(String.valueOf(userId));
		eFmFmVendorContractInvoicePO.setTripTotalAmount(tripAmount);
		eFmFmVendorContractInvoicePO.setApprovalStatus("P");
		iVehicleCheckInBO.save(eFmFmVendorContractInvoicePO);

	}

	@POST
	@Path("/invoiceDownload")
	public Response invoiceTripDetailsDwn(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat monthDate = new SimpleDateFormat("MM-yyyy");
		List<Map<String, Object>> invoiceVendorDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		String activityType = eFmFmVehicleMasterPO.getActionType();
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		int columSize = 13;
		if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")
				&& eFmFmVehicleMasterPO.getContractTypeCount() == 1) {
			columSize = 14;
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Invoice Details");
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255, 255, 255)));// color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 82, 128)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(font);
		int rownum = 0, noOfRoute = 0;
		Row OutSiderow = sheet.createRow(rownum++);
		for (int columnIndex = 0; columnIndex < columSize; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = OutSiderow.createCell(columnIndex);
			columnCol.setCellStyle(style);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
		String third_ColTitle = "";
		String third_extra_ColTitle = "";
		String forth_ColTitle = "";
		String fifth_ColTitle = "";
		String sixth_ColTitle = "";
		String seven_ColTitle = "";
		String eight_ColTitle = "";
		String nine_ColTitle = "";
		String ten_ColTitle = "";
		String eleven_ColTitle = "";
		String twelth_ColTitle = "";
		Cell invocieDetails = OutSiderow.createCell(4);
		invocieDetails.setCellValue("INVOICE DETAILS ");
		invocieDetails.setCellStyle(style);
		OutSiderow = sheet.createRow(rownum++);
		for (int columnIndex = 0; columnIndex < columSize; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = OutSiderow.createCell(columnIndex);
			columnCol.setCellStyle(style);
		}
		OutSiderow = sheet.createRow(rownum++);

		switch (activityType.toUpperCase().trim()) {
		case "VENDORBASED":
			break;
		case "VEHICLEBASED":
			break;
		case "INVOICEDETAILS":
			List<EFmFmVendorContractInvoicePO> vendorSummaryInvoiceDetails = iVehicleCheckInBO
					.getInvoiceDetails(clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getInvoiceNumber());
			if (!(vendorSummaryInvoiceDetails.isEmpty())) {
				third_ColTitle = "Total (km)";
				third_extra_ColTitle = "Total (km) Amount";
				forth_ColTitle = "Extra KMs";
				fifth_ColTitle = "Fuel Amount";
				sixth_ColTitle = "Extra KM Amount";
				seven_ColTitle = "No of Days Absent";
				eight_ColTitle = "Penalty";
				nine_ColTitle = "Deduction";
				ten_ColTitle = "Penalty Net Amount (After deduction)";
				eleven_ColTitle = "Total Amount (Amount + Ex KM Amt)";
				twelth_ColTitle = "Remarks";
				OutSiderow = sheet.createRow(rownum++);
				double totalAmount = 0.0, penalty = 0.0, tripAmount = 0.0, totalBaseAmount = 0.0, serviceTax = 0.0,
						totalServiceTaxAmount = 0.0;
				int noOfvehicle = 0;
				for (int columnIndex = 0; columnIndex < columSize; columnIndex++) {
					sheet.autoSizeColumn(columnIndex);
				}
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("Vehicle Number");
				zerothCol.setCellStyle(style);
				Cell firstCol = OutSiderow.createCell(1);
				Cell secondCol = OutSiderow.createCell(2);
				if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")
						&& eFmFmVehicleMasterPO.getContractTypeCount() == 1) {
					firstCol.setCellValue(" Per Day Contracted Amount");
					firstCol.setCellStyle(style);
					secondCol.setCellValue(" Per Km Amount");
					secondCol.setCellStyle(style);
					Cell twelthCol = OutSiderow.createCell(13);
					twelthCol.setCellValue(twelth_ColTitle);
					twelthCol.setCellStyle(style);

				} else if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("FDC")
						&& eFmFmVehicleMasterPO.getContractTypeCount() == 1) {

					firstCol.setCellValue("Contracted KMs ");
					firstCol.setCellStyle(style);

					secondCol.setCellValue("Contracted Amount");
					secondCol.setCellStyle(style);
					third_extra_ColTitle = "Extra KMs";
					forth_ColTitle = "Fuel Amount";
					fifth_ColTitle = "Extra KM Amount";
					sixth_ColTitle = "No of Days Absent";
					seven_ColTitle = "Penalty";
					eight_ColTitle = "Deduction";
					nine_ColTitle = "Penalty Net Amount (After deduction)";
					ten_ColTitle = "Total Amount (Amount + Ex KM Amt)";
					eleven_ColTitle = "Remarks";
					twelth_ColTitle = "";

				} else {
					firstCol.setCellValue("Contracted Kms/Per Km Amount");
					firstCol.setCellStyle(style);

					secondCol.setCellValue("Contracted Per Month / Day Amount");
					secondCol.setCellStyle(style);

					Cell twelthCol = OutSiderow.createCell(13);
					twelthCol.setCellValue(twelth_ColTitle);
					twelthCol.setCellStyle(style);

				}

				Cell thirdCol = OutSiderow.createCell(3);
				thirdCol.setCellValue(third_ColTitle);
				thirdCol.setCellStyle(style);

				Cell thirdCol_extra = OutSiderow.createCell(4);
				thirdCol_extra.setCellValue(third_extra_ColTitle);
				thirdCol_extra.setCellStyle(style);

				Cell fourthCol = OutSiderow.createCell(5);
				fourthCol.setCellValue(forth_ColTitle);
				fourthCol.setCellStyle(style);

				Cell fifthCol = OutSiderow.createCell(6);
				fifthCol.setCellValue(fifth_ColTitle);
				fifthCol.setCellStyle(style);

				Cell sixthCol = OutSiderow.createCell(7);
				sixthCol.setCellValue(sixth_ColTitle);
				sixthCol.setCellStyle(style);

				Cell seventhCol = OutSiderow.createCell(8);
				seventhCol.setCellValue(seven_ColTitle);
				seventhCol.setCellStyle(style);

				Cell eighthCol = OutSiderow.createCell(9);
				eighthCol.setCellValue(eight_ColTitle);
				eighthCol.setCellStyle(style);

				Cell ninethCol = OutSiderow.createCell(10);
				ninethCol.setCellValue(nine_ColTitle);
				ninethCol.setCellStyle(style);

				Cell tenthCol = OutSiderow.createCell(11);
				tenthCol.setCellValue(ten_ColTitle);
				tenthCol.setCellStyle(style);

				Cell eleventhCol = OutSiderow.createCell(12);
				eleventhCol.setCellValue(eleven_ColTitle);
				eleventhCol.setCellStyle(style);

				for (EFmFmVendorContractInvoicePO vendorInvoiceDetailsList : vendorSummaryInvoiceDetails) {

					fixedDistanceVehicleDetails.put("vendorName",
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					fixedDistanceVehicleDetails.put("invoiceNumber", vendorInvoiceDetailsList.getInvoiceNumber());
					Row secondRow = sheet.createRow(1);
					for (int columnIndex = 0; columnIndex < columSize; columnIndex++) {
						sheet.autoSizeColumn(columnIndex);
						Cell columnCol = secondRow.createCell(columnIndex);
						columnCol.setCellStyle(style);
					}
					Cell invoiceDateCol = secondRow.createCell(10);
					invoiceDateCol.setCellValue("Inovice Date");
					invoiceDateCol.setCellStyle(style);
					Row singleRow = sheet.createRow(2);
					for (int columnIndex = 0; columnIndex < columSize; columnIndex++) {
						sheet.autoSizeColumn(columnIndex);
						Cell columnCol = singleRow.createCell(columnIndex);
						columnCol.setCellStyle(style);
					}
					Cell vendorCol = singleRow.createCell(0);
					vendorCol.setCellValue("Vendor Name");
					vendorCol.setCellStyle(style);

					Cell invoiceNoCol = singleRow.createCell(10);
					invoiceNoCol.setCellValue("invoice Number");
					invoiceNoCol.setCellStyle(style);
					Cell vendorNameCol = singleRow.createCell(1);
					vendorNameCol.setCellValue(
							vendorInvoiceDetailsList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vendorNameCol.setCellStyle(style);
					Cell invoiceNoHeaderCol = singleRow.createCell(11);
					invoiceNoHeaderCol.setCellValue(vendorInvoiceDetailsList.getInvoiceNumber());
					invoiceNoHeaderCol.setCellStyle(style);
					Cell invoiceDateHeaderCol = secondRow.createCell(11);
					invoiceDateHeaderCol.setCellValue(monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
					invoiceDateHeaderCol.setCellStyle(style);
					fixedDistanceVehicleDetails.put("invoiceMonthDate",
							monthDate.format(vendorInvoiceDetailsList.getInvoiceStartDate()));
					fixedDistanceVehicleDetails.put("invoiceCreationDate",
							formatter.format(vendorInvoiceDetailsList.getCreationTime()));
					fixedDistanceVehicleDetails.put("invoiceId", vendorInvoiceDetailsList.getInvoiceId());
					penalty = penalty + vendorInvoiceDetailsList.getTotalDeductibles();
					OutSiderow = sheet.createRow(rownum++);
					if (vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
							.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")
							|| vendorInvoiceDetailsList.getEfmFmVehicleMaster().geteFmFmContractDetails()
									.geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("PDDC")) {
						Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
						totalAmount = totalAmount + vendorInvoiceDetailsList.getTotalAmountPayable();
						totalBaseAmount = totalBaseAmount + vendorInvoiceDetailsList.getBaseTotal();

						if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("FDC")
								&& eFmFmVehicleMasterPO.getContractTypeCount() == 1) {
							Cell vehicelNumber = OutSiderow.createCell(0);
							vehicelNumber
									.setCellValue(vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
							Cell contKMs = OutSiderow.createCell(1);
							contKMs.setCellValue(vendorInvoiceDetailsList.getBaseDistance());
							Cell contractAmount = OutSiderow.createCell(2);
							contractAmount.setCellValue(vendorInvoiceDetailsList.getBaseTotal());
							Cell totalkm = OutSiderow.createCell(3);
							totalkm.setCellValue(vendorInvoiceDetailsList.getTotalDistance());

							Cell extrakm = OutSiderow.createCell(4);
							extrakm.setCellValue(vendorInvoiceDetailsList.getTotalExtraDistance());
							Cell fuelAmt = OutSiderow.createCell(5);
							fuelAmt.setCellValue(vendorInvoiceDetailsList.getFuelExtraAmount());
							Cell extraKmAmt = OutSiderow.createCell(6);
							extraKmAmt.setCellValue(vendorInvoiceDetailsList.getExtraDistanceCharge());

							Cell absent = OutSiderow.createCell(7);

							if (vendorInvoiceDetailsList.getPresentDays() < vendorSummaryInvoiceDetails.get(0)
									.getWorkingDays()) {
								fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
										- vendorInvoiceDetailsList.getPresentDays()));
								absent.setCellValue((vendorInvoiceDetailsList.getWorkingDays()
										- vendorInvoiceDetailsList.getPresentDays()));
							} else {
								fixedDistanceTrips.put("absentDays", "NO");
								absent.setCellValue("NO");
							}

							Cell penaltynetAmt = OutSiderow.createCell(8);
							penaltynetAmt.setCellValue(vendorInvoiceDetailsList.getTotalPenalityAmount());

							Cell deduction = OutSiderow.createCell(9);
							deduction.setCellValue(vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());

							Cell penaltyNetAmt = OutSiderow.createCell(10);
							penaltyNetAmt.setCellValue(vendorInvoiceDetailsList.getTotalDeductibles());
							Cell totalAmt = OutSiderow.createCell(11);
							totalAmt.setCellValue(vendorInvoiceDetailsList.getTotalAmountPayable());
							Cell remarks = OutSiderow.createCell(12);
							remarks.setCellValue(vendorInvoiceDetailsList.getInvoiceRemarks());

						} else if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")
								&& eFmFmVehicleMasterPO.getContractTypeCount() == 1) {
							Cell vehicelNumber = OutSiderow.createCell(0);
							vehicelNumber
									.setCellValue(vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
							Cell contractAmount = OutSiderow.createCell(1);
							Cell contKMs = OutSiderow.createCell(2);

							Cell totalkm = OutSiderow.createCell(3);
							totalkm.setCellValue(vendorInvoiceDetailsList.getTotalDistance());
							//
							Cell totalkmAmount = OutSiderow.createCell(4);
							if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")) {
								contKMs.setCellValue(vendorInvoiceDetailsList.geteFmFmContractDetails().getPerKmCost());
								contractAmount.setCellValue(
										vendorInvoiceDetailsList.geteFmFmContractDetails().getPerDayCost());
								totalkmAmount.setCellValue(
										Math.round(vendorInvoiceDetailsList.getTotalKmAmount() * 100.0) / 100.0);

							} else if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("FDC")) {
								contKMs.setCellValue(vendorInvoiceDetailsList.getBaseDistance());
								contractAmount.setCellValue(vendorInvoiceDetailsList.getBaseTotal());
								totalkmAmount.setCellValue(0);
							}
							Cell extrakm = OutSiderow.createCell(5);
							extrakm.setCellValue(vendorInvoiceDetailsList.getTotalExtraDistance());
							Cell fuelAmt = OutSiderow.createCell(6);
							fuelAmt.setCellValue(vendorInvoiceDetailsList.getFuelExtraAmount());
							Cell extraKmAmt = OutSiderow.createCell(7);
							extraKmAmt.setCellValue(vendorInvoiceDetailsList.getExtraDistanceCharge());

							Cell absent = OutSiderow.createCell(8);

							if (vendorInvoiceDetailsList.getPresentDays() < vendorSummaryInvoiceDetails.get(0)
									.getWorkingDays()) {
								fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
										- vendorInvoiceDetailsList.getPresentDays()));
								absent.setCellValue((vendorInvoiceDetailsList.getWorkingDays()
										- vendorInvoiceDetailsList.getPresentDays()));
							} else {
								fixedDistanceTrips.put("absentDays", "NO");
								absent.setCellValue("NO");
							}

							Cell penaltynetAmt = OutSiderow.createCell(9);
							penaltynetAmt.setCellValue(vendorInvoiceDetailsList.getTotalPenalityAmount());

							Cell deduction = OutSiderow.createCell(10);
							deduction.setCellValue(vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());

							Cell penaltyNetAmt = OutSiderow.createCell(11);
							penaltyNetAmt.setCellValue(vendorInvoiceDetailsList.getTotalDeductibles());
							Cell totalAmt = OutSiderow.createCell(12);
							totalAmt.setCellValue(vendorInvoiceDetailsList.getTotalAmountPayable());
							Cell remarks = OutSiderow.createCell(13);
							remarks.setCellValue(vendorInvoiceDetailsList.getInvoiceRemarks());

						} else {

							Cell vehicelNumber = OutSiderow.createCell(0);
							vehicelNumber
									.setCellValue(vendorInvoiceDetailsList.getEfmFmVehicleMaster().getVehicleNumber());
							Cell contKMs = OutSiderow.createCell(1);
							Cell contractAmount = OutSiderow.createCell(2);
							Cell totalkm = OutSiderow.createCell(3);
							totalkm.setCellValue(vendorInvoiceDetailsList.getTotalDistance());

							Cell totalkmAmount = OutSiderow.createCell(4);
							if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")) {
								contKMs.setCellValue(vendorInvoiceDetailsList.geteFmFmContractDetails().getPerKmCost());
								contractAmount.setCellValue(
										vendorInvoiceDetailsList.geteFmFmContractDetails().getPerDayCost());
								totalkmAmount.setCellValue(vendorInvoiceDetailsList.getTotalKmAmount());

							} else if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("FDC")) {
								contKMs.setCellValue(vendorInvoiceDetailsList.getBaseDistance());
								contractAmount.setCellValue(vendorInvoiceDetailsList.getBaseTotal());
								totalkmAmount.setCellValue(0);
							}
							Cell extrakm = OutSiderow.createCell(5);
							extrakm.setCellValue(vendorInvoiceDetailsList.getTotalExtraDistance());
							Cell fuelAmt = OutSiderow.createCell(6);
							fuelAmt.setCellValue(vendorInvoiceDetailsList.getFuelExtraAmount());
							Cell extraKmAmt = OutSiderow.createCell(7);
							extraKmAmt.setCellValue(vendorInvoiceDetailsList.getExtraDistanceCharge());

							Cell absent = OutSiderow.createCell(8);

							if (vendorInvoiceDetailsList.getPresentDays() < vendorSummaryInvoiceDetails.get(0)
									.getWorkingDays()) {
								fixedDistanceTrips.put("absentDays", (vendorInvoiceDetailsList.getWorkingDays()
										- vendorInvoiceDetailsList.getPresentDays()));
								absent.setCellValue((vendorInvoiceDetailsList.getWorkingDays()
										- vendorInvoiceDetailsList.getPresentDays()));
							} else {
								fixedDistanceTrips.put("absentDays", "NO");
								absent.setCellValue("NO");
							}

							Cell penaltynetAmt = OutSiderow.createCell(9);
							penaltynetAmt.setCellValue(vendorInvoiceDetailsList.getTotalPenalityAmount());

							Cell deduction = OutSiderow.createCell(10);
							deduction.setCellValue(vendorInvoiceDetailsList.getTotalPerDayDeductionAmnt());

							Cell penaltyNetAmt = OutSiderow.createCell(11);
							penaltyNetAmt.setCellValue(vendorInvoiceDetailsList.getTotalDeductibles());
							Cell totalAmt = OutSiderow.createCell(12);
							totalAmt.setCellValue(vendorInvoiceDetailsList.getTotalAmountPayable());
							Cell remarks = OutSiderow.createCell(13);
							remarks.setCellValue(vendorInvoiceDetailsList.getInvoiceRemarks());

						}
						invoiceVendorDetails.add(fixedDistanceTrips);
						noOfvehicle++;
					}
				}
				serviceTax = (totalAmount + tripAmount) * vendorSummaryInvoiceDetails.get(0).getEfmFmVehicleMaster()
						.geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getServiceTax() / 100;

				totalServiceTaxAmount = (totalAmount + tripAmount) + serviceTax;

				OutSiderow = sheet.createRow(rownum++);
				OutSiderow = sheet.createRow(rownum++);
				Cell grandTotalCol = OutSiderow.createCell(1);
				grandTotalCol.setCellValue("Grand Total Rs.");
				grandTotalCol.setCellStyle(style);
				Cell grandTotalCell = OutSiderow.createCell(2);
				grandTotalCell.setCellValue((double) Math.round(totalServiceTaxAmount * 100) / 100);

				Cell serviceTaxCol = OutSiderow.createCell(3);
				serviceTaxCol.setCellValue("Service Tax Rs.");
				serviceTaxCol.setCellStyle(style);

				Cell serviceTaxCell = OutSiderow.createCell(4);
				serviceTaxCell.setCellValue((double) Math.round(serviceTax * 100) / 100);

				Cell totalPenaltyHeaderCol = OutSiderow.createCell(5);
				totalPenaltyHeaderCol.setCellValue("Total Penalty Rs.");
				totalPenaltyHeaderCol.setCellStyle(style);

				Cell totalPenaltyHeaderCell = OutSiderow.createCell(6);
				totalPenaltyHeaderCell.setCellValue((double) Math.round(penalty * 100) / 100);

				Cell totalPayableHeaderCol = OutSiderow.createCell(7);
				totalPayableHeaderCol.setCellValue("Total PayableAmount Rs.");
				totalPayableHeaderCol.setCellStyle(style);
				Cell totalPayableHeaderCell = OutSiderow.createCell(8);
				totalPayableHeaderCell.setCellValue((double) Math.round((totalAmount + tripAmount) * 100) / 100);

			}
			break;
		}
		StreamingOutput streamOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream out) throws IOException, WebApplicationException {
				workbook.write(out);
			}
		};
		ResponseBuilder response = Response.ok(streamOutput,
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
		return response.build();

	}

	@POST
	@Path("/partialInvoiceDownload")
	public Response partialInvoiceDownload(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		List<Map<String, Object>> invoiceVendorDetails = new ArrayList<Map<String, Object>>();
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());

		List<EFmFmClientBranchPO> clientBranch = userMasterBO.getClientDetails(String.valueOf(clientBranchPO.getBranchId()));
		int fixedDisatanceId = 0;
		String distanceFlg = "";
		DecimalFormat doubleConversion = new DecimalFormat("#.##");
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		Date fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
		Date toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
		long diffDays = Math.abs(toDate.getTime() - fromDate.getTime());
		long calculatedDays = diffDays / (24 * 60 * 60 * 1000);
		log.info("fromDate" + fromDate);
		log.info("toDate" + toDate);
		// Get all vehicles by Vendor Id and branchId
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Invoice Details");
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255, 255, 255)));// color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 82, 128)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(font);
		int rownum = 0, noOfRoute = 0, columnSize = 11;

		if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")) {
			columnSize = 10;
		}

		Row OutSiderow = sheet.createRow(rownum++);
		for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = OutSiderow.createCell(columnIndex);
			columnCol.setCellStyle(style);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));

		Cell invocieDetails = OutSiderow.createCell(4);
		invocieDetails.setCellValue("PARTIAL INVOICE DETAILS ");
		invocieDetails.setCellStyle(style);
		OutSiderow = sheet.createRow(rownum++);
		for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = OutSiderow.createCell(columnIndex);
			columnCol.setCellStyle(style);
		}
		OutSiderow = sheet.createRow(rownum++);
		OutSiderow = sheet.createRow(rownum++);

		for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
		Cell zerothCol = OutSiderow.createCell(0);
		zerothCol.setCellValue("Vehicle Number");
		zerothCol.setCellStyle(style);

		Cell firstCol = OutSiderow.createCell(1);
		Cell secondCol = OutSiderow.createCell(2);

		Cell thirdCol = OutSiderow.createCell(3);
		thirdCol.setCellValue("Total (km)");
		thirdCol.setCellStyle(style);

		Cell fourthCol = OutSiderow.createCell(4);

		Cell fifthCol = OutSiderow.createCell(5);
		fifthCol.setCellValue("Total Km Amount");
		fifthCol.setCellStyle(style);

		Cell sixthCol = OutSiderow.createCell(6);
		sixthCol.setCellValue("Fuel Amount");
		sixthCol.setCellStyle(style);

		if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")) {
			firstCol.setCellValue(" Per Day Contract Amount");
			firstCol.setCellStyle(style);

			secondCol.setCellValue(" Per Km Amount");
			secondCol.setCellStyle(style);

			fourthCol.setCellValue("Total Per Day Contracted Amount");
			fourthCol.setCellStyle(style);

			Cell eighthCol = OutSiderow.createCell(7);
			eighthCol.setCellValue("No of Days Absent");
			eighthCol.setCellStyle(style);

			Cell prsentDays = OutSiderow.createCell(8);
			prsentDays.setCellValue("No of Prsent Days");
			prsentDays.setCellStyle(style);

			Cell ninethCol = OutSiderow.createCell(9);
			ninethCol.setCellValue("Total Amount");
			ninethCol.setCellStyle(style);

		} else if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("FDC")) {
			firstCol.setCellValue("Contracted Amount");
			firstCol.setCellStyle(style);

			secondCol.setCellValue("Contracted KMs ");
			secondCol.setCellStyle(style);

			fourthCol.setCellValue("Extra KMs");
			fourthCol.setCellStyle(style);

			Cell seventhCol = OutSiderow.createCell(7);
			seventhCol.setCellValue("Extra KM Amount");
			seventhCol.setCellStyle(style);

			Cell eighthCol = OutSiderow.createCell(8);
			eighthCol.setCellValue("No of Days Absent");
			eighthCol.setCellStyle(style);

			Cell prsentDays = OutSiderow.createCell(9);
			prsentDays.setCellValue("No of Prsent Days");
			prsentDays.setCellStyle(style);

			Cell ninethCol = OutSiderow.createCell(10);
			ninethCol.setCellValue("Total Amount");
			ninethCol.setCellStyle(style);

		} else {
			firstCol.setCellValue("Contracted Per Month / Day Amount");
			firstCol.setCellStyle(style);

			secondCol.setCellValue("Contracted KMs/Per Km Amount");
			secondCol.setCellStyle(style);

			fourthCol.setCellValue("Extra KMs /Total Per Day Contracted Amount");
			fourthCol.setCellStyle(style);

			Cell seventhCol = OutSiderow.createCell(7);
			seventhCol.setCellValue("Extra KM Amount");
			seventhCol.setCellStyle(style);

			Cell eighthCol = OutSiderow.createCell(8);
			eighthCol.setCellValue("No of Days Absent");
			eighthCol.setCellStyle(style);

			Cell prsentDays = OutSiderow.createCell(9);
			prsentDays.setCellValue("No of Prsent Days");
			prsentDays.setCellStyle(style);

			Cell ninethCol = OutSiderow.createCell(10);
			ninethCol.setCellValue("Total Amount");
			ninethCol.setCellStyle(style);
		}
		if (eFmFmVehicleMasterPO.getVehicleId() == 0) {
			List<EFmFmVehicleMasterPO> allVehiclesDetail = iVehicleCheckInBO.getAllApprovedVehiclesByVendorId(
					eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(), clientBranchPO.getBranchId());
			if (!(allVehiclesDetail.isEmpty())) {

				Row secondRow = sheet.createRow(1);
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					sheet.autoSizeColumn(columnIndex);
					Cell columnCol = secondRow.createCell(columnIndex);
					columnCol.setCellStyle(style);
				}
				Cell invoiceDateCol = secondRow.createCell(8);
				invoiceDateCol.setCellValue("From Date");
				invoiceDateCol.setCellStyle(style);
				Row singleRow = sheet.createRow(2);
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					sheet.autoSizeColumn(columnIndex);
					Cell columnCol = singleRow.createCell(columnIndex);
					columnCol.setCellStyle(style);
				}
				Cell vendorCol = singleRow.createCell(0);
				vendorCol.setCellValue("Vendor Name");
				vendorCol.setCellStyle(style);

				Cell invoiceNoCol = singleRow.createCell(8);
				invoiceNoCol.setCellValue("To Date Date");
				invoiceNoCol.setCellStyle(style);
				// OutSiderow = sheet.createRow(rownum++);
				Cell vendorNameCol = singleRow.createCell(1);
				vendorNameCol.setCellValue(allVehiclesDetail.get(0).getEfmFmVendorMaster().getVendorName());
				vendorNameCol.setCellStyle(style);
				Cell invoiceNoHeaderCol = singleRow.createCell(9);
				invoiceNoHeaderCol.setCellValue(formatter.format(toDate));
				invoiceNoHeaderCol.setCellStyle(style);
				Cell invoiceDateHeaderCol = secondRow.createCell(9);
				invoiceDateHeaderCol.setCellValue(formatter.format(fromDate));
				invoiceDateHeaderCol.setCellStyle(style);

				for (EFmFmVehicleMasterPO vehicleDetails : allVehiclesDetail) {
					System.out.println("vehicleDetails" + vehicleDetails.getVehicleNumber());
					List<EFmFmAssignRoutePO> assignRouteDetail = iAssignRouteBO
							.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate, toDate,
									clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());
					if (!(assignRouteDetail.isEmpty())) {
						for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
							OutSiderow = sheet.createRow(rownum++);
							distanceFlg = eFmFmVehicleMasterPO.getDistanceFlg();
							log.info("vehicle NUmber" + vehicleDetails.getVehicleNumber());
							double totalAmt = 0.0, totalTripsAmt = 0.0, totalkmTripsAmt = 0.0, perKmCharge = 0.0,
									totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0, penaltyAmt = 0.0,
									penaltyTotalAmt = 0.0, tripBasedAmount = 0.0, tripTripAmount = 0.0,
									presentDays = 0.0, totalPerDayCost = 0.0, totalMontlyKm = 0.0,
									totatSumOfKmAmount = 0.0, totalTravelledKm = 0.0, extraKmCharges = 0.0,
									extraKm = 0.0, panalityAmount = 0.0;
							int absentDays = 0, totalWorkingDays = 0;
							fixedDisatanceId = allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
									.geteFmFmContractDetails().getDistanceContractId();
							List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
									.getFixedDistanceDetails(fixedDisatanceId, clientBranchPO.getBranchId());
							if (!fixedDistanceDetails.isEmpty()) {

								if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("GNPTJP")) {
									List<EFmFmAssignRoutePO> GNPTJPtotalWorkingDays = iVehicleCheckInBO
											.getPresentDaysBasedOnTrips(fromDate, toDate,
													clientBranch.get(0).getBranchId(), vehicleDetails.getVehicleId());
									totalWorkingDays = GNPTJPtotalWorkingDays.size();
								} else {
									List<EFmFmVehicleCheckInPO> OtherTotalWorkingDays = iVehicleCheckInBO
											.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate,
													clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());
									log.info("total working days" + totalWorkingDays);
									totalWorkingDays = OtherTotalWorkingDays.size();
								}

								List<EFmFmVehicleMasterPO> SumOftotalKm = null;
								if (distanceFlg.equalsIgnoreCase("GPS")) {
									SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicle(fromDate, toDate,
											clientBranchPO.getBranchId(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleId(),
											fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
													.getContractType().trim(),
											fixedDistanceDetails.get(0).getDistanceContractId());
								} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
									SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicleOdometer(fromDate, toDate,
											clientBranchPO.getBranchId(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleId(),
											fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
													.getContractType().trim(),
											fixedDistanceDetails.get(0).getDistanceContractId());
								}
								log.info("SumOftotalKm" + SumOftotalKm.size());
								if (!(fixedDistanceDetails.get(0).getFuelPriceCalculation().equalsIgnoreCase("NR"))) {
									totatSumOfKmAmount = partialfuelCalculation(fromDate, toDate, allVehicleDetails,
											clientBranchPO, fixedDistanceDetails,
											SumOftotalKm.get(0).getSumTravelledDistance(), distanceFlg);
								}
								if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
										.equalsIgnoreCase("FDC")) {
									perKmCharge = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getFixedDistanceMonthly();
									totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
									totalPerDayCost = fixedDistanceDetails.get(0).getFixedDistanceChargeRate();
									totalMontlyKm = fixedDistanceDetails.get(0).getFixedDistanceMonthly();
									if (SumOftotalKm.get(0).getSumTravelledDistance() >= fixedDistanceDetails.get(0)
											.getFixedDistanceMonthly()) {
										extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
												- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
										extraKmCharges = (double) Math.round(extraKm)
												* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
										totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												+ extraKmCharges;
									} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails
											.get(0).getFixedDistanceMonthly()
											&& totalWorkingDays >= fixedDistanceDetails.get(0).getMinimumDays()) {
										totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												+ totatSumOfKmAmount;
									} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails
											.get(0).getFixedDistanceMonthly()
											&& totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {
										if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
											// absentDays=fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size();
											// absentDays=(int)calculatedDays-totalWorkingDays.size();
											penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											penaltyTotalAmt = (penaltyAmt + (penaltyAmt
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
											penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays())
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
													* absentDays;
											totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
													.getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
											totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													- penaltyTotalAmt + totatSumOfKmAmount;
										}
									}
									totalTripsAmt = Double.valueOf(
											((SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge)
													+ totatSumOfKmAmount + extraKmCharges);
									totalkmTripsAmt = Double.valueOf(
											(SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge);

								} else if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
										.getContractType().equalsIgnoreCase("PDDC")) {
									totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
									totalPerDayCost = fixedDistanceDetails.get(0).getPerDayCost();
									if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("N")) {
										totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
										tripTripAmount = totalAmt;
										tripBasedAmount = fixedDistanceDetails.get(0).getPerDayCost()
												* totalWorkingDays;
										totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
												* fixedDistanceDetails.get(0).getPerKmCost());
										totalAmt = totalAmt + totatSumOfKmAmount + extraKmCharges;
										totalTripsAmt = totalAmt + totalkmTripsAmt;

									} else if ((totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays())
											&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
										totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
										tripTripAmount = totalAmt;
										totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
												* fixedDistanceDetails.get(0).getPerKmCost());
										totalAmt = totalAmt + extraKmCharges;
										absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
										log.info("absentDays" + absentDays);
										penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										log.info("panalityAmount" + panalityAmount);
										penaltyTotalAmt = (penaltyAmt + (penaltyAmt
												* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100);
										log.info("penaltyTotalAmt" + penaltyTotalAmt);
										penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays())
												* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
												* absentDays;
										log.info("penalityPerday" + penalityPerday);
										totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
												.getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
										totalAmt = totalAmt - penaltyTotalAmt + totatSumOfKmAmount;
										log.info("totalAmt" + totalAmt);
										totalTripsAmt = totalAmt + totalkmTripsAmt;
									}

								}
								presentDays = totalWorkingDays;
								absentDays = (1 + (int) calculatedDays) - totalWorkingDays;
								System.out.println("totalSumOfAmount+" + totatSumOfKmAmount);

							}
							Cell vehicelNumber = OutSiderow.createCell(0);
							vehicelNumber.setCellValue(vehicleDetails.getVehicleNumber());

							Cell contractAmount = OutSiderow.createCell(1);
							contractAmount.setCellValue(Math.round(totalPerDayCost * 100) / 100.00);

							Cell contKMs = OutSiderow.createCell(2);
							if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")) {
								contKMs.setCellValue(totalPerDayCost);
								Cell totalkm = OutSiderow.createCell(3);
								totalkm.setCellValue(Math.round(totalTravelledKm * 100) / 100.00);
								Cell extrakm = OutSiderow.createCell(4);
								extrakm.setCellValue(Math.round(tripTripAmount * 100) / 100.00);
								Cell totalKmAmount = OutSiderow.createCell(5);
								totalKmAmount.setCellValue(Math.round(totalkmTripsAmt * 100) / 100.00);
								Cell fuelAmt = OutSiderow.createCell(6);
								fuelAmt.setCellValue(Math.round(totatSumOfKmAmount * 100) / 100.00);
								Cell absentDaysCol = OutSiderow.createCell(7);
								absentDaysCol.setCellValue(absentDays);
								Cell presentDaysCol = OutSiderow.createCell(8);
								presentDaysCol.setCellValue(presentDays);
								Cell totalAmount = OutSiderow.createCell(9);
								totalAmount.setCellValue(Math.round(totalTripsAmt * 100) / 100.00);

							} else {
								contKMs.setCellValue(Math.round(totalMontlyKm * 100) / 100.00);
								Cell totalkm = OutSiderow.createCell(3);
								totalkm.setCellValue(Math.round(totalTravelledKm * 100) / 100.00);
								Cell extrakm = OutSiderow.createCell(4);
								extrakm.setCellValue(Math.round(extraKm * 100) / 100.00);
								Cell totalKmAmount = OutSiderow.createCell(5);
								totalKmAmount.setCellValue(Math.round(totalkmTripsAmt * 100) / 100.00);
								Cell fuelAmt = OutSiderow.createCell(6);
								fuelAmt.setCellValue(Math.round(totatSumOfKmAmount * 100) / 100.00);
								Cell extraKmAmt = OutSiderow.createCell(7);
								extraKmAmt.setCellValue(Math.round(extraKmCharges * 100) / 100.00);
								Cell absentDaysCol = OutSiderow.createCell(8);
								absentDaysCol.setCellValue(absentDays);
								Cell presentDaysCol = OutSiderow.createCell(9);
								presentDaysCol.setCellValue(presentDays);
								Cell totalAmount = OutSiderow.createCell(10);
								totalAmount.setCellValue(Math.round(totalTripsAmt * 100) / 100.00);
							}

						}
					}

				}
			}
		} else {
			List<EFmFmAssignRoutePO> assignRouteDetail = iAssignRouteBO
					.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate, toDate,
							clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getVehicleId());
			if (!(assignRouteDetail.isEmpty())) {

				Row secondRow = sheet.createRow(1);
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					sheet.autoSizeColumn(columnIndex);
					Cell columnCol = secondRow.createCell(columnIndex);
					columnCol.setCellStyle(style);
				}
				Cell invoiceDateCol = secondRow.createCell(8);
				invoiceDateCol.setCellValue("From Date");
				invoiceDateCol.setCellStyle(style);
				Row singleRow = sheet.createRow(2);
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					sheet.autoSizeColumn(columnIndex);
					Cell columnCol = singleRow.createCell(columnIndex);
					columnCol.setCellStyle(style);
				}
				Cell vendorCol = singleRow.createCell(0);
				vendorCol.setCellValue("Vendor Name");
				vendorCol.setCellStyle(style);

				Cell invoiceNoCol = singleRow.createCell(8);
				invoiceNoCol.setCellValue("To Date Date");
				invoiceNoCol.setCellStyle(style);
				Cell vendorNameCol = singleRow.createCell(1);
				vendorNameCol.setCellValue(assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorName());
				vendorNameCol.setCellStyle(style);
				Cell invoiceNoHeaderCol = singleRow.createCell(9);
				invoiceNoHeaderCol.setCellValue(formatter.format(toDate));
				invoiceNoHeaderCol.setCellStyle(style);
				Cell invoiceDateHeaderCol = secondRow.createCell(9);
				invoiceDateHeaderCol.setCellValue(formatter.format(fromDate));
				invoiceDateHeaderCol.setCellStyle(style);

				for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
					distanceFlg = eFmFmVehicleMasterPO.getDistanceFlg();
					double totalAmt = 0.0, totalTripsAmt = 0.0, totalkmTripsAmt = 0.0, perKmCharge = 0.0,
							totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0, penaltyAmt = 0.0,
							penaltyTotalAmt = 0.0, tripTotalAmount = 0.0, tripBasedAmount = 0.0, presentDays = 0.0,
							totalMonthlyKm = 0.0, totalPerDayCost = 0.0, totatSumOfKmAmount = 0.0,
							totalTravelledKm = 0.0, extraKmCharges = 0.0, extraKm = 0.0, panalityAmount = 0.0;
					int absentDays = 0, totalWorkingDays = 0;
					fixedDisatanceId = allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.geteFmFmContractDetails().getDistanceContractId();
					List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
							.getFixedDistanceDetails(fixedDisatanceId, clientBranchPO.getBranchId());
					if (!fixedDistanceDetails.isEmpty()) {

						if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("GNPTJP")) {
							List<EFmFmAssignRoutePO> GNPTJPtotalWorkingDays = iVehicleCheckInBO
									.getPresentDaysBasedOnTrips(fromDate, toDate, clientBranch.get(0).getBranchId(),
											eFmFmVehicleMasterPO.getVehicleId());
							totalWorkingDays = GNPTJPtotalWorkingDays.size();
						} else {
							List<EFmFmVehicleCheckInPO> OtherTotalWorkingDays = iVehicleCheckInBO
									.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate,
											clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getVehicleId());
							log.info("total working days" + totalWorkingDays);
							totalWorkingDays = OtherTotalWorkingDays.size();
						}

						log.info("total working days" + totalWorkingDays);
						List<EFmFmVehicleMasterPO> SumOftotalKm = null;
						if (distanceFlg.equalsIgnoreCase("GPS")) {
							SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicle(fromDate, toDate,
									clientBranchPO.getBranchId(),
									allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId(),
									fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
											.trim(),
									fixedDistanceDetails.get(0).getDistanceContractId());
						} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
							SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicleOdometer(fromDate, toDate,
									clientBranchPO.getBranchId(),
									allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId(),
									fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
											.trim(),
									fixedDistanceDetails.get(0).getDistanceContractId());
						}
						log.info("SumOftotalKm" + SumOftotalKm.size());

						if (!(fixedDistanceDetails.get(0).getFuelPriceCalculation().equalsIgnoreCase("NR"))) {
							totatSumOfKmAmount = partialfuelCalculation(fromDate, toDate, allVehicleDetails,
									clientBranchPO, fixedDistanceDetails, SumOftotalKm.get(0).getSumTravelledDistance(),
									distanceFlg);
						}
						if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
								.equalsIgnoreCase("FDC")) {
							perKmCharge = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
									/ fixedDistanceDetails.get(0).getFixedDistanceMonthly();
							totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
							totalPerDayCost = fixedDistanceDetails.get(0).getFixedDistanceChargeRate();
							totalMonthlyKm = fixedDistanceDetails.get(0).getFixedDistanceMonthly();
							if (SumOftotalKm.get(0).getSumTravelledDistance() >= fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()) {
								extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
										- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
								extraKmCharges = (double) Math.round(extraKm)
										* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate() + extraKmCharges;
							} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()
									&& totalWorkingDays >= fixedDistanceDetails.get(0).getMinimumDays()) {
								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										+ totatSumOfKmAmount;
							} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()
									&& totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {
								if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
									// absentDays=fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size();
									// absentDays=(int)calculatedDays-totalWorkingDays.size();
									penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									penaltyTotalAmt = (penaltyAmt
											+ (penaltyAmt * fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
									penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays())
											* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
											* absentDays;
									totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
									totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											- penaltyTotalAmt + totatSumOfKmAmount;
								}
							}
							totalTripsAmt = Double
									.valueOf(((SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge)
											+ totatSumOfKmAmount + extraKmCharges);
							totalkmTripsAmt = Double
									.valueOf((SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge);

						} else if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
								.equalsIgnoreCase("PDDC")) {
							totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
							totalPerDayCost = fixedDistanceDetails.get(0).getPerDayCost();
							if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("N")) {
								totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
								tripTotalAmount = totalAmt;
								tripBasedAmount = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
								totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
										* fixedDistanceDetails.get(0).getPerKmCost());
								totalAmt = totalAmt + totatSumOfKmAmount + extraKmCharges;
								totalTripsAmt = totalAmt + totalkmTripsAmt;

							} else if ((totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays())
									&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
								totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
								tripTotalAmount = totalAmt;
								totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
										* fixedDistanceDetails.get(0).getPerKmCost());
								totalAmt = totalAmt + extraKmCharges;
								absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
								log.info("absentDays" + absentDays);
								penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
								log.info("panalityAmount" + panalityAmount);
								penaltyTotalAmt = (penaltyAmt
										+ (penaltyAmt * fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
												/ 100);
								log.info("penaltyTotalAmt" + penaltyTotalAmt);
								penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays())
										* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
										* absentDays;
								log.info("penalityPerday" + penalityPerday);
								totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
								log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
								totalAmt = totalAmt - penaltyTotalAmt + totatSumOfKmAmount;
								log.info("totalAmt" + totalAmt);
								totalTripsAmt = totalAmt + totalkmTripsAmt;

							}

						}
						presentDays = totalWorkingDays;
						absentDays = (1 + (int) calculatedDays) - totalWorkingDays;
					}
					OutSiderow = sheet.createRow(rownum++);
					Cell vehicelNumber = OutSiderow.createCell(0);
					vehicelNumber.setCellValue(assignRouteDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getVehicleNumber());

					Cell contractAmount = OutSiderow.createCell(1);
					contractAmount.setCellValue(Math.round(totalPerDayCost * 100) / 100.00);

					Cell contKMs = OutSiderow.createCell(2);
					if (eFmFmVehicleMasterPO.getContractType().equalsIgnoreCase("PDDC")) {
						contKMs.setCellValue(totalPerDayCost);
						Cell totalkm = OutSiderow.createCell(3);
						totalkm.setCellValue(Math.round(totalTravelledKm * 100) / 100.00);
						Cell extrakm = OutSiderow.createCell(4);
						extrakm.setCellValue(Math.round(tripTotalAmount * 100) / 100.00);
						Cell totalKmAmount = OutSiderow.createCell(5);
						totalKmAmount.setCellValue(Math.round(totalkmTripsAmt * 100) / 100.00);
						Cell fuelAmt = OutSiderow.createCell(6);
						fuelAmt.setCellValue(Math.round(totatSumOfKmAmount * 100) / 100.00);
						Cell absentDaysCol = OutSiderow.createCell(7);
						absentDaysCol.setCellValue(absentDays);
						Cell presentDaysCol = OutSiderow.createCell(8);
						presentDaysCol.setCellValue(presentDays);
						Cell totalAmount = OutSiderow.createCell(9);
						totalAmount.setCellValue(Math.round(totalTripsAmt * 100) / 100.00);

					} else {
						contKMs.setCellValue(Math.round(totalMonthlyKm * 100) / 100.00);
						Cell totalkm = OutSiderow.createCell(3);
						totalkm.setCellValue(Math.round(totalTravelledKm * 100) / 100.00);
						Cell extrakm = OutSiderow.createCell(4);
						extrakm.setCellValue(Math.round(extraKm * 100) / 100.00);
						Cell totalKmAmount = OutSiderow.createCell(5);
						totalKmAmount.setCellValue(Math.round(totalkmTripsAmt * 100) / 100.00);
						Cell fuelAmt = OutSiderow.createCell(6);
						fuelAmt.setCellValue(Math.round(totatSumOfKmAmount * 100) / 100.00);
						Cell extraKmAmt = OutSiderow.createCell(7);
						extraKmAmt.setCellValue(Math.round(extraKmCharges * 100) / 100.00);
						Cell absentDaysCol = OutSiderow.createCell(8);
						absentDaysCol.setCellValue(absentDays);
						Cell presentDaysCol = OutSiderow.createCell(9);
						presentDaysCol.setCellValue(presentDays);
						Cell totalAmount = OutSiderow.createCell(10);
						totalAmount.setCellValue(Math.round(totalTripsAmt * 100) / 100.00);
					}
				}
			}
		}
		StreamingOutput streamOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream out) throws IOException, WebApplicationException {
				workbook.write(out);
			}
		};
		ResponseBuilder response = Response.ok(streamOutput,
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
		return response.build();

	}

	@POST
	@Path("/partialInvoiceDetails")
	public Response partialInvoiceDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		List<Map<String, Object>> invoiceVendorDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		int fixedDisatanceId = 0;
		String distanceFlg = "";
		DecimalFormat doubleConversion = new DecimalFormat("#.##");
		clientBranchPO.setBranchId(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
		List<EFmFmClientBranchPO> clientBranch = userMasterBO.getClientDetails(String.valueOf(clientBranchPO.getBranchId()));
		Date fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
		Date toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
		long diffDays = Math.abs(toDate.getTime() - fromDate.getTime());
		long calculatedDays = diffDays / (24 * 60 * 60 * 1000);
		log.info("fromDate" + fromDate);
		log.info("toDate" + toDate);
		// Get all vehicles by Vendor Id and branchId
		if (eFmFmVehicleMasterPO.getVehicleId() == 0) {
			List<EFmFmFixedDistanceContractDetailPO> fixedDistanceVendorValidationDetails = iVehicleCheckInBO
					.getFixedContractDetailsValidation(fromDate, toDate, clientBranchPO.getBranchId(),
							eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId());
			if (fixedDistanceVendorValidationDetails.isEmpty()) {
				fixedDistanceVehicleDetails.put("failed", "NOTFOUNDCONTRACT");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				/* return fixedDistanceVehicleDetails; */
				return Response.ok(fixedDistanceVehicleDetails, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmVehicleMasterPO> allVehiclesDetail = iVehicleCheckInBO.getAllApprovedVehiclesByVendorId(
					eFmFmVehicleMasterPO.getEfmFmVendorMaster().getVendorId(), clientBranchPO.getBranchId());
			if (!(allVehiclesDetail.isEmpty())) {
				for (EFmFmVehicleMasterPO vehicleDetails : allVehiclesDetail) {
					System.out.println("vehicleDetails" + vehicleDetails.getVehicleNumber());
					List<EFmFmAssignRoutePO> assignRouteDetail = iAssignRouteBO
							.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate, toDate,
									clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());
					if (!(assignRouteDetail.isEmpty())) {
						for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
							distanceFlg = eFmFmVehicleMasterPO.getDistanceFlg();
							log.info("vehicle NUmber" + vehicleDetails.getVehicleNumber());
							double totalAmt = 0.0, perKmCharge = 0.0, totalPerDayDeductionAmnt = 0.0,
									penalityPerday = 0.0, penaltyAmt = 0.0, penaltyTotalAmt = 0.0,
									tripBasedAmount = 0.0, totalTravelledKm = 0.0, totalkmTripsAmt = 0.0,
									tripTotalAmount = 0.0, totalTripsAmt = 0.0, totalPerDayCost = 0.0,
									extraKmCharges = 0.0, extraKm = 0.0, panalityAmount = 0.0;
							int absentDays = 0, totalWorkingDays = 0;
							fixedDisatanceId = allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
									.geteFmFmContractDetails().getDistanceContractId();
							List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
									.getFixedDistanceDetails(fixedDisatanceId, clientBranchPO.getBranchId());
							if (!fixedDistanceDetails.isEmpty()) {
								if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("GNPTJP")) {
									List<EFmFmAssignRoutePO> GNPTJPtotalWorkingDays = iVehicleCheckInBO
											.getPresentDaysBasedOnTrips(fromDate, toDate,
													clientBranch.get(0).getBranchId(), vehicleDetails.getVehicleId());
									totalWorkingDays = GNPTJPtotalWorkingDays.size();
								} else {
									List<EFmFmVehicleCheckInPO> OtherTotalWorkingDays = iVehicleCheckInBO
											.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate,
													clientBranchPO.getBranchId(), vehicleDetails.getVehicleId());

									totalWorkingDays = OtherTotalWorkingDays.size();
								}
								log.info("total working days" + totalWorkingDays);

								List<EFmFmVehicleMasterPO> SumOftotalKm = null;
								if (distanceFlg.equalsIgnoreCase("GPS")) {
									SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicle(fromDate, toDate,
											clientBranchPO.getBranchId(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleId(),
											fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
													.getContractType().trim(),
											fixedDistanceDetails.get(0).getDistanceContractId());
								} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
									SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicleOdometer(fromDate, toDate,
											clientBranchPO.getBranchId(),
											allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleId(),
											fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
													.getContractType().trim(),
											fixedDistanceDetails.get(0).getDistanceContractId());
								}
								log.info("SumOftotalKm" + SumOftotalKm.size());
								double totatSumOfKmAmount = 0.0;
								if (!(fixedDistanceDetails.get(0).getFuelPriceCalculation().equalsIgnoreCase("NR"))) {
									totatSumOfKmAmount = partialfuelCalculation(fromDate, toDate, allVehicleDetails,
											clientBranchPO, fixedDistanceDetails,
											SumOftotalKm.get(0).getSumTravelledDistance(), distanceFlg);
								}
								if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
										.equalsIgnoreCase("FDC")) {
									totalPerDayCost = fixedDistanceDetails.get(0).getFixedDistanceChargeRate();
									totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
									perKmCharge = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getFixedDistanceMonthly();
									if (SumOftotalKm.get(0).getSumTravelledDistance() >= fixedDistanceDetails.get(0)
											.getFixedDistanceMonthly()) {
										extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
												- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
										extraKmCharges = (double) Math.round(extraKm)
												* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
										totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												+ extraKmCharges;
									} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails
											.get(0).getFixedDistanceMonthly()
											&& totalWorkingDays >= fixedDistanceDetails.get(0).getMinimumDays()) {
										totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												+ totatSumOfKmAmount;
									} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails
											.get(0).getFixedDistanceMonthly()
											&& totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {
										if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
											// absentDays=fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size();
											// absentDays=(int)calculatedDays-totalWorkingDays.size();
											penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											penaltyTotalAmt = (penaltyAmt + (penaltyAmt
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
											penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays())
													* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
													* absentDays;
											totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0)
													.getFixedDistanceChargeRate()
													/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
											// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
											totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
													- penaltyTotalAmt + totatSumOfKmAmount;
										}
									}
									totalTripsAmt = Double.valueOf(
											((SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge)
													+ totatSumOfKmAmount + extraKmCharges);
									totalkmTripsAmt = Double.valueOf(
											(SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge);

								} else if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster()
										.getContractType().equalsIgnoreCase("PDDC")) {
									totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
									totalPerDayCost = fixedDistanceDetails.get(0).getPerDayCost();
									if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("N")) {
										totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
										tripTotalAmount = totalAmt;
										tripBasedAmount = fixedDistanceDetails.get(0).getPerDayCost()
												* totalWorkingDays;
										totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
												* fixedDistanceDetails.get(0).getPerKmCost());
										// totalAmt = totalAmt +
										// totatSumOfKmAmount+totalkmTripsAmt;
										totalTripsAmt = totalAmt + totatSumOfKmAmount + totalkmTripsAmt;

									} else if ((totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays())
											&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
										totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
										tripTotalAmount = totalAmt;
										totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
												* fixedDistanceDetails.get(0).getPerKmCost());
										totalAmt = totalAmt + extraKmCharges;
										absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
										log.info("absentDays" + absentDays);
										penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										log.info("panalityAmount" + panalityAmount);
										penaltyTotalAmt = (penaltyAmt + (penaltyAmt
												* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100);
										log.info("penaltyTotalAmt" + penaltyTotalAmt);
										penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
												/ fixedDistanceDetails.get(0).getMinimumDays())
												* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
												* absentDays;
										log.info("penalityPerday" + penalityPerday);
										totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getPerKmCost()
												/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
										log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
										totalAmt = totalAmt - penaltyTotalAmt + totatSumOfKmAmount;
										log.info("totalAmt" + totalAmt);
										totalTripsAmt = totalAmt + totalkmTripsAmt;

									}

								}

								absentDays = (1 + (int) calculatedDays) - totalWorkingDays;

								System.out.println("totalSumOfAmount+" + totatSumOfKmAmount);
								// added

								Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
								fixedDistanceTrips.put("vehicleNumber", vehicleDetails.getVehicleNumber());
								fixedDistanceTrips.put("vehicleId", vehicleDetails.getVehicleId());
								fixedDistanceTrips.put("vendorName",
										vehicleDetails.getEfmFmVendorMaster().getVendorName());
								fixedDistanceTrips.put("totalKm", Math.round(totalTravelledKm * 100) / 100.00);
								fixedDistanceTrips.put("extraKm", Math.round(extraKm * 100) / 100.00);
								fixedDistanceTrips.put("contractedKm",
										fixedDistanceDetails.get(0).getFixedDistanceMonthly());
								fixedDistanceTrips.put("distanceFlg", eFmFmVehicleMasterPO.getDistanceFlg());
								fixedDistanceTrips.put("contractPerDayAmount",
										vehicleDetails.geteFmFmContractDetails().getPerDayCost());
								fixedDistanceTrips.put("perKmCost",
										vehicleDetails.geteFmFmContractDetails().getPerKmCost());
								fixedDistanceTrips.put("contractedAmount", Math.round(totalPerDayCost * 100) / 100.00);
								fixedDistanceTrips.put("totalPerDayContractedAmounts",
										Math.round(tripTotalAmount * 100) / 100.00);
								fixedDistanceTrips.put("totalPerDayDeduction",
										Math.round(penaltyTotalAmt * 100) / 100.00);
								fixedDistanceTrips.put("totalpenalityAmnt", Math.round(penalityPerday * 100) / 100.00);
								fixedDistanceTrips.put("totalDeduction",
										Math.round(totalPerDayDeductionAmnt * 100) / 100.00);
								fixedDistanceTrips.put("totalWorkingDays", absentDays);
								fixedDistanceTrips.put("baseAmount", Math.round(tripBasedAmount * 100) / 100.00);
								fixedDistanceTrips.put("totalAmountPerDaysContract",
										Math.round(totalAmt * 100) / 100.00);
								fixedDistanceTrips.put("contractType", fixedDistanceDetails.get(0)
										.geteFmFmVendorContractTypeMaster().getContractType());
								fixedDistanceTrips.put("penalty", Math.round(penaltyAmt * 100) / 100.00);
								fixedDistanceTrips.put("totalkmAmount", Math.round(totalkmTripsAmt * 100) / 100.00);
								fixedDistanceTrips.put("fuelExtraAmount",
										Math.round(totatSumOfKmAmount * 100) / 100.00);
								fixedDistanceTrips.put("extraKmcharges", Math.round(extraKmCharges * 100) / 100.00);
								fixedDistanceTrips.put("totalAmount", Math.round(totalTripsAmt * 100) / 100.00);
								invoiceVendorDetails.add(fixedDistanceTrips);

							}
						}
					}

				}
				fixedDistanceVehicleDetails.put("partialInvoiceDetails", invoiceVendorDetails);
			}
		} else {

			List<EFmFmFixedDistanceContractDetailPO> fixedDistanceVehicleValidationDetails = iVehicleCheckInBO
					.getFixedContractDetailsVehicleValidation(fromDate, toDate, clientBranchPO.getBranchId(),
							eFmFmVehicleMasterPO.getVehicleId());
			if (fixedDistanceVehicleValidationDetails.isEmpty()) {
				fixedDistanceVehicleDetails.put("failed", "NOTFOUNDCONTRACT");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(fixedDistanceVehicleDetails, MediaType.APPLICATION_JSON).build();
			}

			List<EFmFmAssignRoutePO> assignRouteDetail = iAssignRouteBO
					.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate, toDate,
							clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getVehicleId());
			if (!(assignRouteDetail.isEmpty())) {
				for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
					distanceFlg = eFmFmVehicleMasterPO.getDistanceFlg();

					double totalAmt = 0.0, perKmCharge = 0.0, totalPerDayDeductionAmnt = 0.0, penalityPerday = 0.0,
							penaltyAmt = 0.0, penaltyTotalAmt = 0.0, tripBasedAmount = 0.0, totalTravelledKm = 0.0,
							totalkmTripsAmt = 0.0, tripTotalAmount = 0.0, totalTripsAmt = 0.0, totalPerDayCost = 0.0,
							extraKmCharges = 0.0, extraKm = 0.0, panalityAmount = 0.0;
					int absentDays = 0, totalWorkingDays = 0;
					fixedDisatanceId = allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.geteFmFmContractDetails().getDistanceContractId();
					List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
							.getFixedDistanceDetails(fixedDisatanceId, clientBranchPO.getBranchId());
					if (!fixedDistanceDetails.isEmpty()) {
						if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("GNPTJP")) {
							List<EFmFmAssignRoutePO> GNPTJPtotalWorkingDays = iVehicleCheckInBO
									.getPresentDaysBasedOnTrips(fromDate, toDate, clientBranchPO.getBranchId(),
											eFmFmVehicleMasterPO.getVehicleId());
							totalWorkingDays = GNPTJPtotalWorkingDays.size();
						} else {
							List<EFmFmVehicleCheckInPO> OtherTotalWorkingDays = iVehicleCheckInBO
									.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate,
											clientBranchPO.getBranchId(), eFmFmVehicleMasterPO.getVehicleId());

							totalWorkingDays = OtherTotalWorkingDays.size();
						}
						log.info("total working days" + totalWorkingDays);

						List<EFmFmVehicleMasterPO> SumOftotalKm = null;
						if (distanceFlg.equalsIgnoreCase("GPS")) {
							SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicle(fromDate, toDate,
									clientBranchPO.getBranchId(),
									allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId(),
									fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
											.trim(),
									fixedDistanceDetails.get(0).getDistanceContractId());
						} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
							SumOftotalKm = iVehicleCheckInBO.getSumOfTotalKmByVehicleOdometer(fromDate, toDate,
									clientBranchPO.getBranchId(),
									allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId(),
									fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
											.trim(),
									fixedDistanceDetails.get(0).getDistanceContractId());
						}
						log.info("SumOftotalKm" + SumOftotalKm.size());

						double totatSumOfKmAmount = 0.0;
						if (!(fixedDistanceDetails.get(0).getFuelPriceCalculation().equalsIgnoreCase("NR"))) {
							totatSumOfKmAmount = partialfuelCalculation(fromDate, toDate, allVehicleDetails,
									clientBranchPO, fixedDistanceDetails, SumOftotalKm.get(0).getSumTravelledDistance(),
									distanceFlg);
						}

						if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
								.equalsIgnoreCase("FDC")) {
							totalPerDayCost = fixedDistanceDetails.get(0).getFixedDistanceChargeRate();
							totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
							perKmCharge = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
									/ fixedDistanceDetails.get(0).getFixedDistanceMonthly();
							if (SumOftotalKm.get(0).getSumTravelledDistance() >= fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()) {
								extraKm = SumOftotalKm.get(0).getSumTravelledDistance()
										- fixedDistanceDetails.get(0).getFixedDistanceMonthly();
								extraKmCharges = (double) Math.round(extraKm)
										* fixedDistanceDetails.get(0).getExtraDistanceChargeRate();
								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate() + extraKmCharges;
							} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()
									&& totalWorkingDays >= fixedDistanceDetails.get(0).getMinimumDays()) {
								totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										+ totatSumOfKmAmount;
							} else if (SumOftotalKm.get(0).getSumTravelledDistance() < fixedDistanceDetails.get(0)
									.getFixedDistanceMonthly()
									&& totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays()) {
								if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
									// absentDays=fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size();
									// absentDays=(int)calculatedDays-totalWorkingDays.size();
									penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									penaltyTotalAmt = (penaltyAmt
											+ (penaltyAmt * fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
													/ 100);
									penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays())
											* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
											* absentDays;
									totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
									// penaltyTotalAmt=penaltyAmt*(fixedDistanceDetails.get(0).getMinimumDays()-totalWorkingDays.size());
									totalAmt = fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
											- penaltyTotalAmt + totatSumOfKmAmount;
								}
							}
							totalTripsAmt = Double
									.valueOf(((SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge)
											+ totatSumOfKmAmount + extraKmCharges);
							totalkmTripsAmt = Double
									.valueOf((SumOftotalKm.get(0).getSumTravelledDistance() - extraKm) * perKmCharge);

						} else if (fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType()
								.equalsIgnoreCase("PDDC")) {
							totalTravelledKm = SumOftotalKm.get(0).getSumTravelledDistance();
							totalPerDayCost = fixedDistanceDetails.get(0).getPerDayCost();
							if (fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("N")) {
								totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
								tripTotalAmount = totalAmt;
								tripBasedAmount = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
								totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
										* fixedDistanceDetails.get(0).getPerKmCost());
								// totalAmt = totalAmt +
								// totatSumOfKmAmount+totalkmTripsAmt;
								totalTripsAmt = totalAmt + totatSumOfKmAmount + totalkmTripsAmt;

							} else if ((totalWorkingDays < fixedDistanceDetails.get(0).getMinimumDays())
									&& fixedDistanceDetails.get(0).getPenalty().equalsIgnoreCase("Y")) {
								totalAmt = fixedDistanceDetails.get(0).getPerDayCost() * totalWorkingDays;
								tripTotalAmount = totalAmt;
								totalkmTripsAmt = (SumOftotalKm.get(0).getSumTravelledDistance()
										* fixedDistanceDetails.get(0).getPerKmCost());
								totalAmt = totalAmt + extraKmCharges;
								absentDays = fixedDistanceDetails.get(0).getMinimumDays() - totalWorkingDays;
								log.info("absentDays" + absentDays);
								penaltyAmt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
								log.info("panalityAmount" + panalityAmount);
								penaltyTotalAmt = (penaltyAmt
										+ (penaltyAmt * fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay())
												/ 100);
								log.info("penaltyTotalAmt" + penaltyTotalAmt);
								penalityPerday = (((fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays())
										* fixedDistanceDetails.get(0).getPenaltyInPercentagePerDay()) / 100)
										* absentDays;
								log.info("penalityPerday" + penalityPerday);
								totalPerDayDeductionAmnt = (fixedDistanceDetails.get(0).getFixedDistanceChargeRate()
										/ fixedDistanceDetails.get(0).getMinimumDays()) * absentDays;
								log.info("totalPerDayDeductionAmnt" + totalPerDayDeductionAmnt);
								totalAmt = totalAmt - penaltyTotalAmt + totatSumOfKmAmount;
								log.info("totalAmt" + totalAmt);
								totalTripsAmt = totalAmt + totalkmTripsAmt;

							}
						}
						absentDays = (1 + (int) calculatedDays) - totalWorkingDays;
						System.out.println("totalSumOfAmount+" + totatSumOfKmAmount);
						// added
						Map<String, Object> fixedDistanceTrips = new HashMap<String, Object>();
						fixedDistanceTrips.put("vehicleNumber",
								allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						fixedDistanceTrips.put("vehicleId",
								allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
						fixedDistanceTrips.put("vendorName", allVehicleDetails.getEfmFmVehicleCheckIn()
								.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
						fixedDistanceTrips.put("totalKm", Math.round(totalTravelledKm * 100) / 100.00);
						fixedDistanceTrips.put("extraKm", Math.round(extraKm * 100) / 100.00);
						fixedDistanceTrips.put("distanceFlg", eFmFmVehicleMasterPO.getDistanceFlg());
						fixedDistanceTrips.put("contractedKm",
								Math.round(fixedDistanceDetails.get(0).getFixedDistanceMonthly() * 100) / 100.00);
						fixedDistanceTrips.put("contractedAmount", Math.round(totalPerDayCost * 100) / 100.00);

						fixedDistanceTrips.put("contractPerDayAmount",
								Math.round(allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
										.geteFmFmContractDetails().getPerDayCost() * 100) / 100.00);
						fixedDistanceTrips.put("perKmCost", allVehicleDetails.getEfmFmVehicleCheckIn()
								.getEfmFmVehicleMaster().geteFmFmContractDetails().getPerKmCost());
						fixedDistanceTrips.put("totalPerDayContractedAmounts",
								Math.round(tripTotalAmount * 100) / 100.00);
						fixedDistanceTrips.put("totalPerDayDeduction", Math.round(penaltyTotalAmt * 100) / 100.00);
						fixedDistanceTrips.put("totalpenalityAmnt", Math.round(penalityPerday * 100) / 100.00);
						fixedDistanceTrips.put("totalDeduction", Math.round(totalPerDayDeductionAmnt * 100) / 100.00);
						fixedDistanceTrips.put("totalWorkingDays", absentDays);
						fixedDistanceTrips.put("contractType",
								fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType());
						fixedDistanceTrips.put("penalty", Math.round(penaltyAmt * 100) / 100.00);
						fixedDistanceTrips.put("totalkmAmount", Math.round(totalkmTripsAmt * 100) / 100.00);
						fixedDistanceTrips.put("fuelExtraAmount", Math.round(totatSumOfKmAmount * 100) / 100.00);
						fixedDistanceTrips.put("extraKmcharges", Math.round(extraKmCharges * 100) / 100.00);
						fixedDistanceTrips.put("totalAmount", Math.round(totalTripsAmt * 100) / 100.00);
						invoiceVendorDetails.add(fixedDistanceTrips);
					}
				}
				fixedDistanceVehicleDetails.put("partialInvoiceDetails", invoiceVendorDetails);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(fixedDistanceVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	public Map<String, Object> partialProcessInvoice(int vendorId, String contractType, int branchId, Date fromDate,
			Date todate, int genPreDate) throws ParseException {
		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Format year = new SimpleDateFormat("yyyy");
		DateFormat monthDate = new SimpleDateFormat("MM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, fromDate.getMonth());
		cal.set(Calendar.YEAR, Integer.valueOf(year.format(fromDate)));
		cal.set(Calendar.DATE, genPreDate);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		System.out.println(" ---Min Date::---" + cal.getTime());
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE) + (genPreDate - 1));
		System.out.println(" ---Date::---" + cal.getTime() + "yesr" + monthDate.format(cal.getTime()));
		Date endDate = cal.getTime();
		List<EFmFmVehicleMasterPO> contractDetails = iVehicleCheckInBO.getAllContractDetailsByVendorId(vendorId,
				contractType, branchId);
		if (!contractDetails.isEmpty() && contractDetails.size() > 0) {
			for (EFmFmVehicleMasterPO vehContractDetails : contractDetails) {
				List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails = iVehicleCheckInBO
						.getFixedDistanceDetails(vehContractDetails.geteFmFmContractDetails().getDistanceContractId(),
								branchId);
				if (!fixedDistanceDetails.isEmpty()) {
					if (dateFormat.parse(dateFormat.format(fixedDistanceDetails.get(0).getFromDate()))
							.after(dateFormat.parse(dateFormat.format(startDate)))
							|| dateFormat.parse(dateFormat.format(fixedDistanceDetails.get(0).getToDate()))
									.before(dateFormat.parse(dateFormat.format(endDate)))) {
						List<EFmFmFixedDistanceContractDetailPO> fixedConeDetails = iVehicleCheckInBO
								.getFixedDistanceDetailsByCloneId(
										vehContractDetails.geteFmFmContractDetails().getDistanceContractId(), branchId);
						if (!fixedConeDetails.isEmpty()) {
							System.out.println("1---" + dateFormat.format(fixedConeDetails.get(0).getFromDate()) + "---"
									+ dateFormat.format(startDate));
							System.out.println("2--" + dateFormat.format(fixedConeDetails.get(0).getToDate()) + "---"
									+ dateFormat.format(endDate));
							if (dateFormat.parse(dateFormat.format(fixedConeDetails.get(0).getFromDate()))
									.after(dateFormat.parse(dateFormat.format(startDate)))
									&& dateFormat.parse(dateFormat.format(fixedConeDetails.get(0).getToDate()))
											.before(dateFormat.parse(dateFormat.format(endDate)))) {
								fixedDistanceVehicleDetails.put("failed", "kinldy check the clone Id contract date");
							} else {
								// update all the vehicles contractdetailsId
								List<EFmFmVehicleMasterPO> vehicleDetailsList = iVehicleCheckInBO.bulkUpdateContractId(
										vendorId, fixedConeDetails.get(0).getDistanceContractId(),
										fixedConeDetails.get(0).getCloneId(), branchId);
								if (!vehicleDetailsList.isEmpty()) {
									for (EFmFmVehicleMasterPO vehicleList : vehicleDetailsList) {
										EFmFmVehicleMasterPO updateVehicleDetails = iVehicleCheckInBO
												.getParticularVehicleDetail(vehicleList.getVehicleId());
										updateVehicleDetails.seteFmFmContractDetails(fixedConeDetails.get(0));
										iVehicleCheckInBO.update(updateVehicleDetails);
										fixedDistanceVehicleDetails.put("cloneId",
												fixedConeDetails.get(0).getDistanceContractId());
									}
								} else {
									fixedDistanceVehicleDetails.put("failed",
											"vehicle Contract Details are notUpdated");
								}
							}
						}
					}
				} else {
					fixedDistanceVehicleDetails.put("failed", "kinldy create the Contract Details for Vehicles");
				}

			}
		} else {
			fixedDistanceVehicleDetails.put("failed", "Create the contract details and the mapped to vehicles");
		}

		return fixedDistanceVehicleDetails;
	}

	@POST
	@Path("/viewDaysDetails")
	public Response viewDaysDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		SimpleDateFormat simpleformatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
		Date toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> totalWorkingDays = iVehicleCheckInBO.getVehicleAndDriverAttendenceByVehicleId(
				fromDate, toDate, eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId(),
				eFmFmVehicleMasterPO.getVehicleId());

		List<EFmFmClientBranchPO> clientBranch = userMasterBO
				.getClientDetails(String.valueOf(eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId()));
		if (clientBranch.get(0).getBranchCode().equalsIgnoreCase("GNPTJP")) {
			List<EFmFmAssignRoutePO> assignRouteDetail = iVehicleCheckInBO.getPresentDaysBasedOnTrips(fromDate, toDate,
					eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId(),
					eFmFmVehicleMasterPO.getVehicleId());
			if (!(assignRouteDetail.isEmpty())) {
				for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
					Map<String, Object> listTrips = new HashMap<String, Object>();
					listTrips.put("vehicleNumber",
							allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					listTrips.put("DriverName",
							allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					listTrips.put("LogInTime", simpleformatter.format(allVehicleDetails.getTripStartTime()));
					if (allVehicleDetails.getTripCompleteTime() == null) {
						listTrips.put("LogOutTime", "Not yet Completed");
					} else {
						List<EFmFmAssignRoutePO> maxTime = iVehicleCheckInBO.getMaxTimePresentDaysBasedOnTrips(
								allVehicleDetails.getTripAssignDate(),
								eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId(),
								eFmFmVehicleMasterPO.getVehicleId());
						if (!(maxTime.isEmpty())) {
							listTrips.put("LogOutTime", simpleformatter.format(maxTime.get(0).getTripCompleteTime()));
						} else {
							listTrips.put("LogOutTime", "Not yet Completed");
						}
					}
					requests.add(listTrips);
				}
			}
		} else {
			if (!(totalWorkingDays.isEmpty())) {
				for (EFmFmVehicleCheckInPO allVehicleDetails : totalWorkingDays) {
					Map<String, Object> listTrips = new HashMap<String, Object>();
					listTrips.put("vehicleNumber", allVehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
					listTrips.put("DriverName", allVehicleDetails.getEfmFmDriverMaster().getFirstName());
					listTrips.put("LogInTime", simpleformatter.format(allVehicleDetails.getCheckInTime()));
					if (allVehicleDetails.getCheckOutTime() == null) {
						listTrips.put("LogOutTime", "Not yet checkedOut");
					} else {
						listTrips.put("LogOutTime", simpleformatter.format(allVehicleDetails.getCheckOutTime()));
					}
					requests.add(listTrips);
				}
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/viewVehicleDistance")
	public Response viewVehicleDistance(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVehicleMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat simpleformatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
		Date toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
		DecimalFormat doubleConversion = new DecimalFormat("#.##");
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> assignRouteDetail = iVehicleCheckInBO.getOngoingVehicleDetails(fromDate, toDate,
				eFmFmVehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId(),
				eFmFmVehicleMasterPO.getVehicleId());
		if (!(assignRouteDetail.isEmpty())) {
			for (EFmFmAssignRoutePO allVehicleDetails : assignRouteDetail) {
				Map<String, Object> listTrips = new HashMap<String, Object>();
				listTrips.put("tripId", allVehicleDetails.getAssignRouteId());
				listTrips.put("assignedDate", simpleformatter.format(allVehicleDetails.getTripAssignDate()));
				if (allVehicleDetails.getTripStartTime() != null) {
					listTrips.put("tripStartTime", simpleformatter.format(allVehicleDetails.getTripStartTime()));
				} else {
					listTrips.put("tripStartTime", "");
				}
				if (allVehicleDetails.getTripCompleteTime() != null) {
					listTrips.put("completedTime", simpleformatter.format(allVehicleDetails.getTripCompleteTime()));
				} else {
					listTrips.put("completedTime", "");
				}
				listTrips.put("vehicleNumber",
						allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				listTrips.put("DriverName",
						allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				listTrips.put("travelledDistance",
						Double.valueOf(doubleConversion.format(allVehicleDetails.getTravelledDistance())));
				listTrips.put("distanceFlg", allVehicleDetails.geteFmFmClientBranchPO().getDistanceFlg());
				if (allVehicleDetails.getOdometerStartKm() != null) {
					listTrips.put("startKm", allVehicleDetails.getOdometerStartKm());
				} else {
					listTrips.put("startKm", "0");
				}
				if (allVehicleDetails.getOdometerEndKm() != null) {
					listTrips.put("endKm", allVehicleDetails.getOdometerEndKm());
					listTrips.put("totalKm",
							Double.valueOf(doubleConversion.format(Double.valueOf(allVehicleDetails.getOdometerEndKm())
									- Double.valueOf(allVehicleDetails.getOdometerStartKm()))));
				} else {
					listTrips.put("endKm", "0");
				}
				requests.add(listTrips);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	public double partialfuelCalculation(Date fromDate, Date toDate, EFmFmAssignRoutePO allVehicleDetails,
			EFmFmClientBranchPO eFmFmClientBranchPO, List<EFmFmFixedDistanceContractDetailPO> fixedDistanceDetails,
			double SumOftotalKm, String distanceFlg) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		double totalSumOfAmount = 0.0;
		double distance = 0.0;
		Date oneDayValidaton = null;
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		List<EFmFmAssignRoutePO> vehicleKmDetails = iVehicleCheckInBO.getTotalKmByVehicle(fromDate, toDate,
				eFmFmClientBranchPO.getBranchId(),
				allVehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId(),
				fixedDistanceDetails.get(0).geteFmFmVendorContractTypeMaster().getContractType().trim(),
				fixedDistanceDetails.get(0).getDistanceContractId());
		if (!vehicleKmDetails.isEmpty()) {
			for (EFmFmAssignRoutePO routeDetails : vehicleKmDetails) {
				eFmFmAssignRoutePO.setAssignRouteId(routeDetails.getAssignRouteId());
				if (distanceFlg.equalsIgnoreCase("GPS")) {
					distance = routeDetails.getTravelledDistance();
				} else if (distanceFlg.equalsIgnoreCase("Odometer")) {
					if (routeDetails.getOdometerEndKm() != null && !routeDetails.getOdometerEndKm().isEmpty()
							&& Double.parseDouble(routeDetails.getOdometerEndKm()) > Double
									.parseDouble(routeDetails.getOdometerStartKm())) {
						distance = (Double.parseDouble(routeDetails.getOdometerEndKm())
								- Double.parseDouble(routeDetails.getOdometerStartKm()));
					}
				}

				List<EFmFmFuelChargesPO> fuelChargesDetails = iVehicleCheckInBO.getFuelDetailsByDate(
						routeDetails.getTripAssignDate(),
						fixedDistanceDetails.get(0).geteFmFmVendorFuelContractTypeMaster().getFuelTypeId(),
						eFmFmClientBranchPO.getBranchId());
				if (!fuelChargesDetails.isEmpty()) {
					List<EFmFmAssignRoutePO> updateKm = iAssignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
					for (EFmFmFuelChargesPO fuelCharges : fuelChargesDetails) {
						boolean execution = false;

						if (fuelCharges.getToDate() == null
								&& (routeDetails.getTripAssignDate().after(fuelCharges.getFromDate())
										|| routeDetails.getTripAssignDate().equals(fuelCharges.getToDate()))) {
							execution = true;
						} else {
							if (fuelCharges.getToDate() == null) {
								execution = true;
							} else if (routeDetails.getTripAssignDate().before(fuelCharges.getToDate())
									|| routeDetails.getTripAssignDate().equals(fuelCharges.getToDate())) {
								execution = true;
							}
						}
						if (execution) {
							if (fuelCharges.geteFmFmVendorFuelContractTypeMaster().getContractType()
									.equalsIgnoreCase("M")) {
								// totalSumOfAmount=(fuelCharges.getNewPrice()-fixedDistanceDetails.get(0).getPetrolPrice())*fuelCharges.getPerMonthAmount();
								if (fuelCharges.getNewPrice() > 0 && fixedDistanceDetails.get(0).getPetrolPrice() > 0) {
									if (fuelCharges.getNewPrice() >= fixedDistanceDetails.get(0).getPetrolPrice()) {
										if (oneDayValidaton == null || (!dateFormate.format(oneDayValidaton)
												.equals(dateFormate.format(routeDetails.getTripAssignDate())))) {
											double totalAmount = (fuelCharges.getNewPrice()
													- fixedDistanceDetails.get(0).getPetrolPrice())
													* (fuelCharges.getPerMonthAmount()
															/ fixedDistanceDetails.get(0).getMinimumDays());
											updateKm.get(0)
													.setFuelKmAmount((double) Math.round(totalAmount * 100) / 100);
											iAssignRouteBO.update(updateKm.get(0));
											totalSumOfAmount += (double) Math.round(totalAmount * 100) / 100;
											oneDayValidaton = routeDetails.getTripAssignDate();
										}
									} else {
										log.info("old price Should not be greaterthan New Price");
									}
								} else {
									log.info("milage or Travel Distance Should not be Zero");
								}

							} else if (fuelCharges.geteFmFmVendorFuelContractTypeMaster().getContractType()
									.equalsIgnoreCase("K")) {
								if (routeDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getMileage() > 0
										&& distance > 0) {
									if (fuelCharges.getNewPrice() >= fixedDistanceDetails.get(0).getPetrolPrice()) {
										double totalAmount = (distance / routeDetails.getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getMileage())
												* (fuelCharges.getNewPrice()
														- fixedDistanceDetails.get(0).getPetrolPrice());
										updateKm.get(0).setFuelKmAmount((double) Math.round(totalAmount * 100) / 100);
										iAssignRouteBO.update(updateKm.get(0));
										totalSumOfAmount += (double) Math.round(totalAmount * 100) / 100;
									} else {
										log.info("old price Should not be greaterthan New Price");
									}
								} else {
									log.info("milage or Travel Distance Should not be Zero");
								}
							}
						}
					}

				} else {
					log.info("Need to add the fuel details");
				}
			}
		}
		return totalSumOfAmount;
	}

	@POST
	@Path("/generatedInvoiceDetails")
	public Response generatedInvoiceDetails(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVendorContractInvoicePO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorContractInvoicePO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmVendorContractInvoicePO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<Map<String, Object>> allActiveInvoiceNumberData = new ArrayList<Map<String, Object>>();
		SimpleDateFormat simpleformatter = new SimpleDateFormat("dd-MM-yyyy");
		List<EFmFmVendorContractInvoicePO> allActiveInvoiceNumbers = iVehicleCheckInBO
				.getListOfInvoiceDetailsByGroupInvoiceNumber(
						eFmFmVendorContractInvoicePO.geteFmFmClientBranchPO().getBranchId(),
						eFmFmVendorContractInvoicePO.getInvoiceStatus());
		if (!(allActiveInvoiceNumbers.isEmpty())) {
			for (EFmFmVendorContractInvoicePO allInvoiceNumbersList : allActiveInvoiceNumbers) {
				Map<String, Object> allInvoiceNumberList = new HashMap<String, Object>();
				allInvoiceNumberList.put("InvoiceNumber", allInvoiceNumbersList.getInvoiceNumber());
				allInvoiceNumberList.put("VendorName",
						allInvoiceNumbersList.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				allInvoiceNumberList.put("InvoiceStartDate",
						simpleformatter.format(allInvoiceNumbersList.getInvoiceStartDate()));
				allInvoiceNumberList.put("distanceFlg", allInvoiceNumbersList.getDistanceFlg());
				allInvoiceNumberList.put("InvoiceEndDate",
						simpleformatter.format(allInvoiceNumbersList.getInvoiceEndDate()));
				List<EFmFmVendorContractInvoicePO> allActiveInvoiceDetails = iVehicleCheckInBO.getInvoiceDetails(
						eFmFmVendorContractInvoicePO.geteFmFmClientBranchPO().getBranchId(),
						(int) allInvoiceNumbersList.getInvoiceNumber());
				if (!(allActiveInvoiceDetails.isEmpty())) {
					allInvoiceNumberList.put("invoiceSize", allActiveInvoiceDetails.size());
					List<Map<String, Object>> allActiveInvoiceData = new ArrayList<Map<String, Object>>();
					for (EFmFmVendorContractInvoicePO allInvoiceListDetails : allActiveInvoiceDetails) {
						Map<String, Object> InvoiceList = new HashMap<String, Object>();
						InvoiceList.put("VendorName",
								allInvoiceListDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
						InvoiceList.put("VehicleNumber",
								allInvoiceListDetails.getEfmFmVehicleMaster().getVehicleNumber());
						InvoiceList.put("totalKm", allInvoiceListDetails.getTotalDistance());
						InvoiceList.put("totalAmountPayable", allInvoiceListDetails.getTotalAmountPayable());
						InvoiceList.put("InvoiceStartDate", allInvoiceListDetails.getInvoiceStartDate());
						InvoiceList.put("ApprovalStatus", allInvoiceListDetails.getApprovalStatus());
						allActiveInvoiceData.add(InvoiceList);
					}
					allInvoiceNumberList.put("InvoiceData", allActiveInvoiceData);
				}
				allActiveInvoiceNumberData.add(allInvoiceNumberList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorContractInvoicePO.getUserId());
		return Response.ok(allActiveInvoiceNumberData, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/disableInvoiceDetails")
	public Response diableInvoiceDetails(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO)
			throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorContractInvoicePO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorContractInvoicePO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmVendorContractInvoicePO.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setAuthorizationToken(jwtToken);
					userDetail.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
			}
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		Map<String, Object> fixedDistanceVehicleDetails = new HashMap<String, Object>();
		List<EFmFmVendorContractInvoicePO> allActiveInvoiceDetails = iVehicleCheckInBO.getInvoiceDetails(
				eFmFmVendorContractInvoicePO.geteFmFmClientBranchPO().getBranchId(),
				(int) eFmFmVendorContractInvoicePO.getInvoiceNumber());
		if (!(allActiveInvoiceDetails.isEmpty())) {
			iVehicleCheckInBO.updateVehicleContractInvoiceStatus((int) eFmFmVendorContractInvoicePO.getInvoiceNumber(),
					eFmFmVendorContractInvoicePO.getInvoiceStatus());
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorContractInvoicePO.getUserId());
		return Response.ok(fixedDistanceVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

}