package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PushNotificationService;
import com.newtglobal.eFmFmFleet.model.EFmFmAdminCustomMessagePO;
import com.newtglobal.eFmFmFleet.model.EFmFmAdminSentSMSPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/message")
@Consumes("application/json")
@Produces("application/json")
public class MessageService {

	private static Log log = LogFactory.getLog(MessageService.class);

	@POST
	@Path("/messageToAllEmployeesCount")
	public Response sendMessageToAllEmployeesCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> response = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> employeeDetails = userMasterBO
				.getAllEmployeeDetailsFromBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());
		response.put("status", "Sending message to-" + employeeDetails.size() + "-users");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/messageToAllEmployees")
	public Response sendMessageToAllEmployees(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> employeeDetails = userMasterBO
				.getAllEmployeeDetailsFromBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

		final MessagingService messagingService = new MessagingService();
		final PushNotificationService pushNotification = new PushNotificationService();

		if (!employeeDetails.isEmpty()) {
			for (EFmFmUserMasterPO efmfmUserMasterDetails : employeeDetails) {
				Thread thread1 = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendMessage")
									|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")
									|| efmfmUserMasterDetails.getDeviceToken() == null) {
								messagingService.adminMessage(
										new String(Base64.getDecoder().decode(efmfmUserMasterDetails.getFirstName()),
												"utf-8"),
										eFmFmAdminSentSMSPO.getMessageDescription(),
										new String(Base64.getDecoder().decode(efmfmUserMasterDetails.getMobileNumber()),
												"utf-8"));

							}
						} catch (Exception e) {
							log.info("Error");
						}

						if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendNotification")
								|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")) {
							try {
								if (efmfmUserMasterDetails.getDeviceType().contains("Android")) {
									pushNotification.notification(efmfmUserMasterDetails.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								} else {
									pushNotification.iosPushNotification(efmfmUserMasterDetails.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								}
							} catch (Exception e) {
								log.info("Status" + e);
							}
						}

					}
				});
				thread1.start();

				EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePo = new EFmFmAdminSentSMSPO();

				EFmFmUserMasterPO efmfmUser = new EFmFmUserMasterPO();
				efmfmUser.setUserId(efmfmUserMasterDetails.getUserId());

				EFmFmClientBranchPO branchPo = new EFmFmClientBranchPO();

				branchPo.setBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

				eFmFmAdminSentSMSSavePo.setMobileNumber(efmfmUserMasterDetails.getMobileNumber());
				// System.out.println(efmfmUserMasterDetails.getMobileNumber());
				eFmFmAdminSentSMSSavePo.setMsgSentDate(new Date());
				eFmFmAdminSentSMSSavePo.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
				eFmFmAdminSentSMSSavePo.setStatus("Y");
				eFmFmAdminSentSMSSavePo.setCreatedBy("Admin");
				eFmFmAdminSentSMSSavePo.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
				eFmFmAdminSentSMSSavePo.setNotificationType(eFmFmAdminSentSMSPO.getNotificationType());

				eFmFmAdminSentSMSSavePo.setEfmFmUserMaster(efmfmUser);
				eFmFmAdminSentSMSSavePo.seteFmFmClientBranchPO(branchPo);

				userMasterBO.save(eFmFmAdminSentSMSSavePo);
			}
			responce.put("status", "Message Sent");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		responce.put("status", "fail");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/createCustomMessage")
	public Response createCustomMessage(EFmFmAdminCustomMessagePO eFmFmAdminCustomMessagePO) {
		Map<String, Object> responce = new HashMap<String, Object>();
		// System.out.println("id::"+eFmFmAdminCustomMessagePO.geteFmFmClientBranchPO().getBranchId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		eFmFmAdminCustomMessagePO.setCreatedDate(new Date());
		eFmFmAdminCustomMessagePO.setIsActive("Y");
		eFmFmAdminCustomMessagePO.setVisibleUser("Admin");
		eFmFmAdminCustomMessagePO.setBranchId(eFmFmAdminCustomMessagePO.geteFmFmClientBranchPO().getBranchId());
		eFmFmAdminCustomMessagePO.setCustMsgDescription(eFmFmAdminCustomMessagePO.getCustMsgDescription());

		userMasterBO.save(eFmFmAdminCustomMessagePO);

		responce.put("status", "Message created Successfully");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/getAllCustomMessages")
	public Response getAllCustomMessages(EFmFmAdminCustomMessagePO eFmFmAdminCustomMessagePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
		List<EFmFmAdminCustomMessagePO> eFmFmAdminCustomMessagePo = userMasterBO
				.getAllCustomMessagesFromBranchId(eFmFmAdminCustomMessagePO.getCombinedFacility());
		List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
		if (!eFmFmAdminCustomMessagePo.isEmpty()) {
			for (EFmFmAdminCustomMessagePO eFmFmAdminCustomMessage : eFmFmAdminCustomMessagePo) {
				Map<String, Object> messageListpo = new HashMap<String, Object>();
				messageListpo.put("Message", eFmFmAdminCustomMessage.getCustMsgDescription());
				messageList.add(messageListpo);
			}
			messages.addAll(messageList);
		}
		return Response.ok(messages, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/sendSMSByEmployeeIDCount")
	public Response sendMessageByEmployeeIDCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> response = new HashMap<String, Object>();

		List<String> temp = eFmFmAdminSentSMSPO.getEmployeeIds();
		StringBuffer employeeId = new StringBuffer();
		int count = 1;
		for (String employee : eFmFmAdminSentSMSPO.getEmployeeIds()) {
			if (count == temp.size()) {
				employeeId.append(employee);
			} else {
				employeeId.append(employee);
				employeeId.append(",");
			}
			count++;
		}
		List<EFmFmUserMasterPO> employeeUserDetails = userMasterBO.getEmployeeDetailsFromEmployeeIdAndBranchId(
				employeeId, eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

		response.put("status", "Sending message to-" + employeeUserDetails.size() + "-employees");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/sendSMSByEmployeeID")
	public Response sendMessageByEmployeeID(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> responce = new HashMap<String, Object>();

		List<String> temp = eFmFmAdminSentSMSPO.getEmployeeIds();
		StringBuffer employeeId = new StringBuffer();
		int count = 1;
		for (String employee : eFmFmAdminSentSMSPO.getEmployeeIds()) {
			if (count == temp.size()) {
				employeeId.append(employee);
			} else {
				employeeId.append(employee);
				employeeId.append(",");
			}
			count++;
		}
		List<EFmFmUserMasterPO> employeeUserDetails = userMasterBO.getEmployeeDetailsFromEmployeeIdAndBranchId(
				employeeId, eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

		final MessagingService messagingService = new MessagingService();
		final PushNotificationService pushNotification = new PushNotificationService();

		if (!employeeUserDetails.isEmpty()) {
			for (EFmFmUserMasterPO efmfmUserMasterDetails : employeeUserDetails) {
				Thread thread1 = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendMessage")
									|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")
									|| efmfmUserMasterDetails.getDeviceToken() == null) {
								messagingService.adminMessage(
										new String(Base64.getDecoder().decode(efmfmUserMasterDetails.getFirstName()),
												"utf-8"),
										eFmFmAdminSentSMSPO.getMessageDescription(),
										new String(Base64.getDecoder().decode(efmfmUserMasterDetails.getMobileNumber()),
												"utf-8"));

							}
						} catch (Exception e) {
							log.info("error" + e);

						}

						if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendNotification")
								|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")) {
							try {
								if (efmfmUserMasterDetails.getDeviceType().contains("Android")) {
									pushNotification.notification(efmfmUserMasterDetails.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								} else {
									pushNotification.iosPushNotification(efmfmUserMasterDetails.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								}
							} catch (Exception e) {
								log.info("Status" + e);
							}

						}

					}
				});
				thread1.start();

				EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePo = new EFmFmAdminSentSMSPO();

				EFmFmUserMasterPO efmfmUser = new EFmFmUserMasterPO();
				efmfmUser.setUserId(efmfmUserMasterDetails.getUserId());

				EFmFmClientBranchPO branchPo = new EFmFmClientBranchPO();

				branchPo.setBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

				eFmFmAdminSentSMSSavePo.setMobileNumber(efmfmUserMasterDetails.getMobileNumber());
				eFmFmAdminSentSMSSavePo.setMsgSentDate(new Date());
				eFmFmAdminSentSMSSavePo.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
				eFmFmAdminSentSMSSavePo.setStatus("Y");
				eFmFmAdminSentSMSSavePo.setCreatedBy("Admin");
				eFmFmAdminSentSMSSavePo.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
				eFmFmAdminSentSMSSavePo.setNotificationType(eFmFmAdminSentSMSPO.getNotificationType());

				eFmFmAdminSentSMSSavePo.setEfmFmUserMaster(efmfmUser);
				eFmFmAdminSentSMSSavePo.seteFmFmClientBranchPO(branchPo);

				userMasterBO.save(eFmFmAdminSentSMSSavePo);
			}
			responce.put("status", "Message Sent");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		responce.put("status", "fail");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/sendSMSByMobileNumbersCount")
	public Response sendMessageByMobileNumbersCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) throws IOException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> response = new HashMap<String, Object>();

		List<String> temp = eFmFmAdminSentSMSPO.getMobileNumbers();
		StringBuffer mobileNumbers = new StringBuffer();
		int count = 1;
		for (String mobileNos : eFmFmAdminSentSMSPO.getMobileNumbers()) {
			if (count == temp.size()) {
				mobileNumbers.append(Base64.getEncoder().encodeToString(mobileNos.toString().trim().getBytes("utf-8")));
			} else {
				try {
					mobileNumbers
							.append(Base64.getEncoder().encodeToString(mobileNos.toString().trim().getBytes("utf-8")));
				} catch (UnsupportedEncodingException e) {
					log.info("error" + e);
				}
				mobileNumbers.append(",");
			}
			count++;
		}
		List<EFmFmUserMasterPO> employeeUserDetails = userMasterBO.getEmployeeDetailsFromMobileNumberAndBranchId(
				mobileNumbers, eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

		response.put("status", "Sending message to-" + employeeUserDetails.size() + "-employees");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/sendSMSByMobileNumbers")
	public Response sendMessageByMobileNumbers(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) throws IOException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> responce = new HashMap<String, Object>();

		List<String> temp = eFmFmAdminSentSMSPO.getMobileNumbers();
		StringBuffer mobileNumbers = new StringBuffer();
		int count = 1;
		for (String mobileNos : eFmFmAdminSentSMSPO.getMobileNumbers()) {
			if (count == temp.size()) {
				mobileNumbers.append(Base64.getEncoder().encodeToString(mobileNos.toString().trim().getBytes("utf-8")));
			} else {
				try {
					mobileNumbers
							.append(Base64.getEncoder().encodeToString(mobileNos.toString().trim().getBytes("utf-8")));
				} catch (UnsupportedEncodingException e) {
					log.info("error" + e);
				}
				mobileNumbers.append(",");
			}
			count++;
		}
		List<EFmFmUserMasterPO> employeeUserDetails = userMasterBO.getEmployeeDetailsFromMobileNumberAndBranchId(
				mobileNumbers, eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

		final MessagingService messagingService = new MessagingService();
		final PushNotificationService pushNotification = new PushNotificationService();

		if (!employeeUserDetails.isEmpty()) {
			for (EFmFmUserMasterPO efmfmUserMasterDetails : employeeUserDetails) {
				Thread thread1 = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendMessage")
									|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")
									|| efmfmUserMasterDetails.getDeviceToken() == null) {
								messagingService.adminMessage(
										new String(Base64.getDecoder().decode(efmfmUserMasterDetails.getFirstName()),
												"utf-8"),
										eFmFmAdminSentSMSPO.getMessageDescription(),
										new String(Base64.getDecoder().decode(efmfmUserMasterDetails.getMobileNumber()),
												"utf-8"));

							}
						} catch (Exception e) {
							log.info("error" + e);

						}

						if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendNotification")
								|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")) {
							try {
								if (efmfmUserMasterDetails.getDeviceType().contains("Android")) {
									pushNotification.notification(efmfmUserMasterDetails.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								} else {
									pushNotification.iosPushNotification(efmfmUserMasterDetails.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								}
							} catch (Exception e) {
								log.info("Status" + e);
							}

						}

					}
				});
				thread1.start();

				EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePo = new EFmFmAdminSentSMSPO();

				EFmFmUserMasterPO efmfmUser = new EFmFmUserMasterPO();
				efmfmUser.setUserId(efmfmUserMasterDetails.getUserId());

				EFmFmClientBranchPO branchPo = new EFmFmClientBranchPO();

				branchPo.setBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

				eFmFmAdminSentSMSSavePo.setMobileNumber(efmfmUserMasterDetails.getMobileNumber());
				eFmFmAdminSentSMSSavePo.setMsgSentDate(new Date());
				eFmFmAdminSentSMSSavePo.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
				eFmFmAdminSentSMSSavePo.setStatus("Y");
				eFmFmAdminSentSMSSavePo.setCreatedBy("Admin");
				eFmFmAdminSentSMSSavePo.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
				eFmFmAdminSentSMSSavePo.setNotificationType(eFmFmAdminSentSMSPO.getNotificationType());

				eFmFmAdminSentSMSSavePo.setEfmFmUserMaster(efmfmUser);
				eFmFmAdminSentSMSSavePo.seteFmFmClientBranchPO(branchPo);

				userMasterBO.save(eFmFmAdminSentSMSSavePo);
			}
			responce.put("status", "Message Sent");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		responce.put("status", "fail");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/sendSMSToAdhocNumbersCount")
	public Response sendMessageToAdhocNumbersCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<String> temp = eFmFmAdminSentSMSPO.getMobileNumbers();

		StringBuffer mobileNo = new StringBuffer();
		int count = 1;
		for (String mobileNumber : eFmFmAdminSentSMSPO.getMobileNumbers()) {
			if (count == temp.size()) {
				mobileNo.append(mobileNumber);
			} else {
				mobileNo.append(mobileNumber);
				mobileNo.append(",");
			}
			count++;
		}
		log.info("sendSMSToAdhocNumbersCount" + eFmFmAdminSentSMSPO.getMobileNumbers());
		String[] mobileNumbers = mobileNo.toString().split(",");
		response.put("status", "Sending message to-" + mobileNumbers.length + "-Adhoc Numbers");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/sendSMSToAdhocNumbers")
	public Response sendMessageToAdhocNumbers(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> responce = new HashMap<String, Object>();

		List<String> temp = eFmFmAdminSentSMSPO.getMobileNumbers();

		StringBuffer mobileNo = new StringBuffer();
		int count = 1;
		for (String mobileNumber : eFmFmAdminSentSMSPO.getMobileNumbers()) {
			if (count == temp.size()) {
				mobileNo.append(mobileNumber);
			} else {
				mobileNo.append(mobileNumber);
				mobileNo.append(",");
			}
			count++;
		}
		String[] mobileNumbers = mobileNo.toString().split(",");
		final MessagingService messagingService = new MessagingService();
		for (int i = 0; i < mobileNumbers.length; i++) {
			String num = mobileNumbers[i];
			Thread thread1 = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						messagingService.adminMessage("", eFmFmAdminSentSMSPO.getMessageDescription(), num);
					} catch (Exception e) {
						log.info("Errror" + e);
					}
				}
			});
			thread1.start();

			EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePO = new EFmFmAdminSentSMSPO();
			eFmFmAdminSentSMSSavePO.setMobileNumber(num);
			eFmFmAdminSentSMSSavePO.setMsgSentDate(new Date());
			eFmFmAdminSentSMSSavePO.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
			eFmFmAdminSentSMSSavePO.setStatus("Y");
			eFmFmAdminSentSMSSavePO.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
			eFmFmAdminSentSMSSavePO.setCreatedBy("Admin");
			eFmFmAdminSentSMSSavePO.setNotificationType("SendMessage");
			userMasterBO.saveMessageSentByMobileNumber(eFmFmAdminSentSMSSavePO);

		}
		responce.put("status", "Message Sent");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/messageToAllGuestsCount")
	public Response sendMessageToAllGuestsCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> response = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> guestDetails = userMasterBO
				.getAllGuestDetailsFromBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

		response.put("status", "Sending message to-" + guestDetails.size() + "- Guests");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/messageToAllGuests")
	public Response sendMessageToAllGuests(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> guestDetails = userMasterBO
				.getAllGuestDetailsFromBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());
		final PushNotificationService pushNotification = new PushNotificationService();
		final MessagingService messagingService = new MessagingService();

		if (!guestDetails.isEmpty()) {
			for (EFmFmUserMasterPO guestEmployee : guestDetails) {
				Thread thread1 = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendMessage")
									|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")
									|| guestEmployee.getDeviceToken() == null) {
								messagingService.adminMessage(
										new String(Base64.getDecoder().decode(guestEmployee.getFirstName()), "utf-8"),
										eFmFmAdminSentSMSPO.getMessageDescription(), new String(
												Base64.getDecoder().decode(guestEmployee.getMobileNumber()), "utf-8"));

							}
						} catch (Exception e) {
							log.info("error" + e);

						}

						if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendNotification")
								|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")) {
							try {
								if (guestEmployee.getDeviceType().contains("Android")) {
									pushNotification.notification(guestEmployee.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								} else {
									pushNotification.iosPushNotification(guestEmployee.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								}
							} catch (Exception e) {
								log.info("Status" + e);
							}
						}
					}
				});
				thread1.start();
				EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePo = new EFmFmAdminSentSMSPO();

				EFmFmUserMasterPO efmfmUser = new EFmFmUserMasterPO();
				efmfmUser.setUserId(guestEmployee.getUserId());

				EFmFmClientBranchPO branchPo = new EFmFmClientBranchPO();

				branchPo.setBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

				eFmFmAdminSentSMSSavePo.setMobileNumber(guestEmployee.getMobileNumber());
				eFmFmAdminSentSMSSavePo.setMsgSentDate(new Date());
				eFmFmAdminSentSMSSavePo.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
				eFmFmAdminSentSMSSavePo.setStatus("Y");
				eFmFmAdminSentSMSSavePo.setCreatedBy("Admin");
				eFmFmAdminSentSMSSavePo.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
				eFmFmAdminSentSMSSavePo.setNotificationType(eFmFmAdminSentSMSPO.getNotificationType());

				eFmFmAdminSentSMSSavePo.setEfmFmUserMaster(efmfmUser);
				eFmFmAdminSentSMSSavePo.seteFmFmClientBranchPO(branchPo);

				userMasterBO.save(eFmFmAdminSentSMSSavePo);
			}
			responce.put("status", "Message Sent");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		responce.put("status", "fail");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/messagetoallemployeesbasedonshiftCount")
	public Response sendMessageToAllEmployeesOnShiftDateCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> response = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> eFmFmUserMasterPO = userMasterBO
				.getAllEmployeeDetailsFromShiftDate(eFmFmAdminSentSMSPO);

		response.put("status", "Sending message to-" + eFmFmUserMasterPO.size() + "- Employees");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/messagetoallemployeesbasedonshift")
	public Response sendMessageToAllEmployeesOnShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> eFmFmUserMasterPO = userMasterBO
				.getAllEmployeeDetailsFromShiftDate(eFmFmAdminSentSMSPO);
		final MessagingService messagingService = new MessagingService();
		final PushNotificationService pushNotification = new PushNotificationService();
		if (!eFmFmUserMasterPO.isEmpty()) {
			for (EFmFmUserMasterPO eFmFmUserMasterPo : eFmFmUserMasterPO) {
				Thread thread1 = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendMessage")
									|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")
									|| eFmFmUserMasterPo.getDeviceToken() == null) {
								messagingService.adminMessage(
										new String(Base64.getDecoder().decode(eFmFmUserMasterPo.getFirstName()),
												"utf-8"),
										eFmFmAdminSentSMSPO.getMessageDescription(),
										new String(Base64.getDecoder().decode(eFmFmUserMasterPo.getMobileNumber()),
												"utf-8"));

							}
						} catch (Exception e) {

							log.info("error" + e);

						}

						if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendNotification")
								|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")) {
							try {
								if (eFmFmUserMasterPo.getDeviceType().contains("Android")) {
									pushNotification.notification(eFmFmUserMasterPo.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								} else {
									pushNotification.iosPushNotification(eFmFmUserMasterPo.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								}
							} catch (Exception e) {
								log.info("Status" + e);
							}

						}

					}
				});
				thread1.start();
				EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePo = new EFmFmAdminSentSMSPO();

				EFmFmUserMasterPO efmfmUser = new EFmFmUserMasterPO();
				efmfmUser.setUserId(eFmFmUserMasterPo.getUserId());

				EFmFmClientBranchPO branchPo = new EFmFmClientBranchPO();

				branchPo.setBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

				eFmFmAdminSentSMSSavePo.setMobileNumber(eFmFmUserMasterPo.getMobileNumber());
				eFmFmAdminSentSMSSavePo.setMsgSentDate(new Date());
				eFmFmAdminSentSMSSavePo.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
				eFmFmAdminSentSMSSavePo.setStatus("Y");
				eFmFmAdminSentSMSSavePo.setCreatedBy("Admin");
				eFmFmAdminSentSMSSavePo.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
				eFmFmAdminSentSMSSavePo.setTripType(eFmFmAdminSentSMSPO.getTripType());
				eFmFmAdminSentSMSSavePo.setShiftTime(eFmFmAdminSentSMSPO.getShiftTime());

				eFmFmAdminSentSMSSavePo.setNotificationType(eFmFmAdminSentSMSPO.getNotificationType());

				eFmFmAdminSentSMSSavePo.setEfmFmUserMaster(efmfmUser);
				eFmFmAdminSentSMSSavePo.seteFmFmClientBranchPO(branchPo);

				userMasterBO.save(eFmFmAdminSentSMSSavePo);
			}

			responce.put("status", "Message Sent");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		responce.put("status", "fail No Employee Requests on mentioned date");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/messagetoallemployeesandguestbasedonshiftCount")
	public Response sendMessageToAllEmployeesAndGuestsOnShiftCount(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> response = new HashMap<String, Object>();

		List<EFmFmUserMasterPO> eFmFmUserMasterPO = userMasterBO
				.getAllEmployeeDetailsFromShiftDate(eFmFmAdminSentSMSPO);

		response.put("status", "Sending message to-" + eFmFmUserMasterPO.size() + "- Users");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/messagetoallemployeesandguestbasedonshift")
	public Response sendMessageToAllEmployeesAndGuestsOnShift(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> eFmFmUserMasterPO = userMasterBO
				.getAllEmployeeDetailsFromShiftDate(eFmFmAdminSentSMSPO);
		final MessagingService messagingService = new MessagingService();
		final PushNotificationService pushNotification = new PushNotificationService();

		if (!eFmFmUserMasterPO.isEmpty()) {
			for (EFmFmUserMasterPO eFmFmUserMasterPo : eFmFmUserMasterPO) {
				Thread thread1 = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendMessage")
									|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")
									|| eFmFmUserMasterPo.getDeviceToken() == null) {
								messagingService.adminMessage(
										new String(Base64.getDecoder().decode(eFmFmUserMasterPo.getFirstName()),
												"utf-8"),
										eFmFmAdminSentSMSPO.getMessageDescription(),
										new String(Base64.getDecoder().decode(eFmFmUserMasterPo.getMobileNumber()),
												"utf-8"));

							}
						} catch (Exception e) {
							log.info("error" + e);
						}

						if (eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("SendNotification")
								|| eFmFmAdminSentSMSPO.getNotificationType().equalsIgnoreCase("Both")) {
							try {
								if (eFmFmUserMasterPo.getDeviceType().contains("Android")) {
									pushNotification.notification(eFmFmUserMasterPo.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								} else {
									pushNotification.iosPushNotification(eFmFmUserMasterPo.getDeviceToken(),
											eFmFmAdminSentSMSPO.getMessageDescription());
								}
							} catch (Exception e) {
								log.info("Status" + e);
							}

						}

					}
				});
				thread1.start();
				EFmFmAdminSentSMSPO eFmFmAdminSentSMSSavePo = new EFmFmAdminSentSMSPO();

				EFmFmUserMasterPO efmfmUser = new EFmFmUserMasterPO();
				efmfmUser.setUserId(eFmFmUserMasterPo.getUserId());

				EFmFmClientBranchPO branchPo = new EFmFmClientBranchPO();

				branchPo.setBranchId(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());

				eFmFmAdminSentSMSSavePo.setMobileNumber(eFmFmUserMasterPo.getMobileNumber());
				eFmFmAdminSentSMSSavePo.setMsgSentDate(new Date());
				eFmFmAdminSentSMSSavePo.setMessageDescription(eFmFmAdminSentSMSPO.getMessageDescription());
				eFmFmAdminSentSMSSavePo.setStatus("Y");
				eFmFmAdminSentSMSSavePo.setCreatedBy("Admin");
				eFmFmAdminSentSMSSavePo.setMessageType(eFmFmAdminSentSMSPO.getMessageType());
				eFmFmAdminSentSMSSavePo.setTripType(eFmFmAdminSentSMSPO.getTripType());
				eFmFmAdminSentSMSSavePo.setNotificationType(eFmFmAdminSentSMSPO.getNotificationType());
				eFmFmAdminSentSMSSavePo.setShiftTime(eFmFmAdminSentSMSPO.getShiftTime());
				eFmFmAdminSentSMSSavePo.setEfmFmUserMaster(efmfmUser);
				eFmFmAdminSentSMSSavePo.seteFmFmClientBranchPO(branchPo);

				userMasterBO.save(eFmFmAdminSentSMSSavePo);
			}

			responce.put("status", "Message Sent");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		responce.put("status", "fail No Employee Requests on mentioned date");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/getAllSentSMSHistory")
	public Response getAllAdminSentMessagesHistory(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO)
			throws UnsupportedEncodingException {
		Map<String, Object> response = new HashMap<String, Object>();

		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPo = userMasterBO
				.getAllSentSMSHistory(eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (!eFmFmAdminSentSMSPo.isEmpty()) {
			for (EFmFmAdminSentSMSPO eFmFmAdminSentSMS : eFmFmAdminSentSMSPo) {
				Map<String, Object> eFmFmAdminSentMessages = new HashMap<String, Object>();

				eFmFmAdminSentMessages.put("msgSentDate", eFmFmAdminSentSMS.getMsgSentDate().toString());
				eFmFmAdminSentMessages.put("messageDescription", eFmFmAdminSentSMS.getMessageDescription());
				eFmFmAdminSentMessages.put("mobileNumber",
						new String(Base64.getDecoder().decode(eFmFmAdminSentSMS.getMobileNumber()), "utf-8"));
				eFmFmAdminSentMessages.put("notificationType", eFmFmAdminSentSMS.getNotificationType());
				eFmFmAdminSentMessages.put("status", eFmFmAdminSentSMS.getStatus());
				if (eFmFmAdminSentSMS.getShiftTime() == null) {
					eFmFmAdminSentMessages.put("shiftTime", "NA");
				} else {
					eFmFmAdminSentMessages.put("shiftTime", eFmFmAdminSentSMS.getShiftTime());
				}
				result.add(eFmFmAdminSentMessages);
			}

			response.put("status", result);
			return Response.ok(response, MediaType.APPLICATION_JSON).build();
		}

		response.put("status", "Empty");
		return Response.ok(response, MediaType.APPLICATION_JSON).build();

	}
	
	@POST
	@Path("/checkEmployeeDetailsByMobileNumber")
	public Response checkEmployeeDetailsByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) throws IOException{
		boolean status=false;
		Map<String, Object> response = new HashMap<String, Object>();
		eFmFmAdminSentSMSPO.setMobileNumber(Base64.getEncoder().encodeToString(eFmFmAdminSentSMSPO.getMobileNumber().toString().trim().getBytes("utf-8")));
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		List<EFmFmAdminSentSMSPO> userDetail=userMasterBO.checkEmployeeDetailsByMobileNumber(eFmFmAdminSentSMSPO);
		System.out.println(userDetail.size());
		if(userDetail.size()!=0)
		{status= true;			
		}
		
		response.put("status",status);
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/checkEmployeeDetailsByEmployeeID")
	public Response checkEmployeeDetailsByEmployeeID(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO)
			 {
		boolean status=false;
		Map<String, Object> response = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		List<EFmFmAdminSentSMSPO> userDetail=userMasterBO.checkEmployeeDetailsByEmployeeId(eFmFmAdminSentSMSPO);
		
		if(userDetail.size()!=0)
		{status= true;			
		}
		
		response.put("status",status);
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}
	

}
