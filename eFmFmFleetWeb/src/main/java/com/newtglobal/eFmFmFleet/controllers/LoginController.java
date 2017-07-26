package com.newtglobal.eFmFmFleet.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Base;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchConfigurationMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchSubConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;
import com.newtglobal.eFmFmFleet.web.CustomAutoLogin;

@Controller
public class LoginController {

    private static Log log = LogFactory.getLog(LoginController.class);
    public static final String cookieName = "SPRING_SECURITY_REMEMBER_ME_COOKIE";
    private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsLinux", null, "docsLinux", null);
    private static final String SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsWindows", null, "docsWindows", null);

    IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String manageProfile(Principal principle, HttpSession sessionkk, HttpServletRequest request,HttpServletResponse response)
            throws SocketException, NamingException {
    	log.info("Old SessionId: "+request.getSession().getId());
    	sessionkk.invalidate();
    	HttpSession session = request.getSession(true); // create the session
    	log.info("New SessionId: "+request.getSession().getId());
        try {  
        	JwtTokenGenerator token=new JwtTokenGenerator();
      	    log.info("Logged In User IP Adress"+token.getClientIpAddr(request));
            session.setAttribute("failedFromLdap", "success");
            
            //log.info("User Logged In" + principle.getName());            
            /*Servion LADP
             * 
             */
            
            String userName=request.getParameter("j_username");
			String password=request.getParameter("j_password");			
			System.out.println("userName"+userName +"password"+password +"--principle.getName()--"+principle.getName());
			System.out.println("userName"+principle.getName() +"password"+principle.getName());
			/*if (request.getParameter("j_username") == null || request.getParameter("j_username").equals(null) || request.getParameter("j_password").isEmpty() || request.getParameter("j_password") == null || request.getParameter("j_password").equals(null)) {
				session.setAttribute("failedFromLdap", "emptyPass");
				return "index";
			}*/
			//Servion LDAP login code start
    	    //LdapContext ctx = ActiveDirectory.getConnection(userName,password);
    	   // ctx.close();
			session.setAttribute("failedFromLdap", "success");  
			//Servion LDAP login code end
	        
            //servion LADP Close
    	/*	if (request.getParameter("j_username") == null || request.getParameter("j_username").equals(null)) {
				return "index";
			} else {
			
			}*/
            
            if (principle.getName() == null || principle.getName().equals(null)) {
                return "index";
            } else {
                IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
                List<EFmFmUserMasterPO> userMasterPO = iUserMasterBO
                        .getSpecificUserDetailsByUserName(principle.getName());
                
                List<EFmFmClientUserRolePO> roleMasterPO = iUserMasterBO.getUserRolesFromUserIdAndBranchId(
                        userMasterPO.get(0).getUserId());
                log.info("goint to set All the information of " + principle.getName()
                        + "to session.Total Module Access Size" + roleMasterPO.size());
                
                Cookie[] cookies = (Cookie[]) request.getCookies();
                String[] cookieTokens = null;
                for (int i = 0; i < cookies.length; i++) {
                    if (cookieName.equals(cookies[i].getName())) {
                        cookieTokens = decodeCookie(cookies[i].getValue());
                        log.info("cookieTokens"+cookies[i].getValue());
                        log.info("tokens"+cookieTokens);
                    }
                }

//                if (cookieTokens != null) {
//                    PersistentLoginPO persistentLoginPO = iUserMasterBO.PersistentLoginPODettail(cookieTokens[0]);
//                    if (persistentLoginPO != null) {  
//                        if(iUserMasterBO.isAleradyLoggedin(principle.getName())>1){
//                        	log.info("alreadylogin");
//                      //  	iUserMasterBO.delteRecord(persistentLoginPO.getIpAddress());
//                        	return "alreadylogin";
//                        }
//                        persistentLoginPO.setIpAddress(request.getRemoteAddr());
//                        iUserMasterBO.updatePersistentPO(persistentLoginPO);
//                    } else {
//                        return "index";
//                    }
//
//                }
                
                setValuetoSession(session, roleMasterPO, principle.getName(),request,userMasterPO.get(0));
			
            }
        } catch (Exception e) {
            session.setAttribute("failedFromLdap", "failed");
            return "index";
        }
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap modelMap, HttpSession session, HttpServletRequest request) throws SocketException, UnsupportedEncodingException {
        Cookie[] cookies = (Cookie[]) request.getCookies();
        if (cookies != null) {
            CustomAutoLogin customAutoLogin = new CustomAutoLogin();
            return customAutoLogin.autoLogin(cookies, session, request);
        } else {
            return "login";
        }

    }

    @RequestMapping(value = "/error500", method = RequestMethod.GET)
    public String errorPage500(ModelMap modelMap) {
        return "error_500";
    }
    

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadDirectoryAccess(ModelMap modelMap) {
        return "index";
    }

    @RequestMapping(value = "/comingsoon", method = RequestMethod.GET)
    public String comingSoon(ModelMap modelMap) {

        return "coming_soon";
    }

    @RequestMapping(value = "/error404", method = RequestMethod.GET)
    public String errorPage404(ModelMap modelMap) {
        return "error_404";
    }

    @RequestMapping(value = "/loginFailed", method = RequestMethod.GET)
    public String loginError(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
        session.setAttribute("access", "wrongCredentials");
        return "index";
    }

    @RequestMapping(value = "/lastattempt", method = RequestMethod.GET)
    public String loginFaildLastAttempt(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
        session.setAttribute("access", "lastattempt");
        return "index";
    }

    @RequestMapping(value = "/wrongattempt", method = RequestMethod.GET)
    public String wrongAttempt(ModelMap modelMap, HttpSession session) {
        log.info("inside wrongAttempt");
        session.setAttribute("access", "wrongAttempt");
        return "index";
    }

    @RequestMapping(value = "/inactive", method = RequestMethod.GET)
    public String inactive(ModelMap modelMap, HttpSession session) {
        log.info("inside inactive");
        session.setAttribute("access", "inactive");
        return "index";
    }

    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    public String accountDisable(ModelMap modelMap, HttpSession session) {
        log.info("inside disable");
        session.setAttribute("access", "disable");
        return "index";
    }

    @RequestMapping(value = "/logindenied", method = RequestMethod.GET)
    public String loginDenied(ModelMap modelMap, HttpSession session) {
        log.info("inside logindenied");
        session.setAttribute("access", "passReset");
        return "index";
    }
    
    @RequestMapping(value = "/tempLogin", method = RequestMethod.GET)
    public String tempPassLogin(ModelMap modelMap, HttpSession session) {
        log.info("inside tempLogin");
        session.setAttribute("access", "tempLogin");
        return "index";
    }

    @RequestMapping(value = "/alreadylogin", method = RequestMethod.GET)
    public String alreadyloggedin(ModelMap modelMap, HttpSession session, HttpServletRequest request,
            Principal principle) {
        session.setAttribute("access", "alreadylogin");
    	return "index";
    }

    // All Module

    @RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
    public String accessdenied(ModelMap modelMap, HttpSession session) {
        return "accessdenied";
    }

    @RequestMapping(value = "/pagenotfound", method = RequestMethod.GET)
    public String pagenotfound(ModelMap modelMap) {
        modelMap.addAttribute("_productName", "efmfm COURIER");
        return "pagenotfound";
    }

    @RequestMapping(value = "/sessionExpier", method = RequestMethod.GET)
    public String sessionExpier(ModelMap modelMap, HttpSession session) {
        session.invalidate();
        return "sessionExpier";
    }

    

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpSession session) {
        return "index";
    }

    @RequestMapping(value = "/routehstory", method = RequestMethod.GET)
    public String routehstory(ModelMap modelMap) {
        return "routehstory";
    }

    @RequestMapping(value = "/userProfile", method = RequestMethod.GET)
    public String userProfileInfo(ModelMap modelMap, HttpSession session) {
        return "userProfile";
    }

    public void setValuetoSession(HttpSession session, List<EFmFmClientUserRolePO> roleMaster, String userName,HttpServletRequest request,EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException, UnknownHostException {
    	String ip="";
  		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");		 
    	try{
    	ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");   	
    	EFmFmSessionManagementPO eFmFmSessionManagementPO=new EFmFmSessionManagementPO();
    	eFmFmSessionManagementPO.setSessionId(request.getSession().getId());
    	eFmFmSessionManagementPO.setUserAgent(userAgentSelection(request.getHeader("User-Agent")));
    	eFmFmSessionManagementPO.setSessionStartTime(new Date());  	
    	ip = request.getRemoteAddr();
    	if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
    	    InetAddress inetAddress = InetAddress.getLocalHost();
    	    String ipAddress = inetAddress.getHostAddress();
    	    ip = ipAddress;
    	}
    	eFmFmSessionManagementPO.setUserIPAddress(ip);
    	eFmFmSessionManagementPO.setSessionActiveStatus("Y");
    	EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
    	
    	eFmFmClientBranchPO.setBranchId(roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
    	eFmFmSessionManagementPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
    	eFmFmSessionManagementPO.setEfmFmUserMaster(eFmFmUserMasterPO);
    	List<String> sessionDetails=iSessionManagementBO.getSessionDetails(eFmFmSessionManagementPO);
    	log.info("sessionDetails =="+sessionDetails.get(1));
    	if(sessionDetails.get(1).trim().equalsIgnoreCase("AllreadyLogin")){
    		iSessionManagementBO.logOutAllSession(eFmFmUserMasterPO.getUserId(),eFmFmClientBranchPO.getBranchId());
    	}
    	iSessionManagementBO.saveSessionData(eFmFmSessionManagementPO);
    	}catch(Exception e){
        	log.info("error =="+e);
    	}
    	
        session.setAttribute("userAgent", userAgentSelection(request.getHeader("User-Agent")));
        session.setAttribute("userIPAddress", ip);
        String jwtToken="";
    	try{
        	JwtTokenGenerator token=new JwtTokenGenerator();
        	jwtToken=token.generateToken();
            eFmFmUserMasterPO.setAuthorizationToken(jwtToken);
            eFmFmUserMasterPO.setTokenGenerationTime(new Date());
			iUserMasterBO.update(eFmFmUserMasterPO);
			}catch(Exception e){
				log.info("error"+e);
			}

    	session.setAttribute("role", roleMaster.get(0).getEfmFmRoleMaster().getRole());
    	 String rolevalue=roleMaster.get(0).getEfmFmRoleMaster().getRole();
        session.setAttribute("firstName", roleMaster.get(0).getEfmFmUserMaster().getFirstName());
        session.setAttribute("lastName", roleMaster.get(0).getEfmFmUserMaster().getLastName());
        session.setAttribute("branchId", roleMaster.get(0).geteFmFmClientBranchPO().getBranchId());
        session.setAttribute("branchCode", roleMaster.get(0).geteFmFmClientBranchPO().getBranchCode());
        session.setAttribute("profileId", roleMaster.get(0).getEfmFmUserMaster().getUserId());
        session.setAttribute("adhocTimePicker", roleMaster.get(0).geteFmFmClientBranchPO().getAdhocTimePickerForEmployee());
        session.setAttribute("imageUploadSize", roleMaster.get(0).geteFmFmClientBranchPO().getImageUploadSize());
        session.setAttribute("authenticationToken", jwtToken);
        session.setAttribute("userName", userName);
        session.setAttribute("sessionTimeOutInMin", roleMaster.get(0).geteFmFmClientBranchPO().getSessionTimeoutInMinutes());
        session.setAttribute("sessionTimeOutNotificationInSec", roleMaster.get(0).geteFmFmClientBranchPO().getSessionNotificationTime());             
        session.setAttribute("tempPassChange", roleMaster.get(0).getEfmFmUserMaster().isTempPassWordChange());
        session.setAttribute("officeLocation", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getLatitudeLongitude());
        session.setAttribute("lastRequest", new Date());
        session.setAttribute("locationVisible", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getLocationVisible());
        session.setAttribute("genderPreference", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getShiftTimeGenderPreference());
        session.setAttribute("minimumDestCount", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMinimumDestCount());
        session.setAttribute("mobilePageCount", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMobilePageCount());
        session.setAttribute("webPageCount", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getWebPageCount());
        session.setAttribute("multiFacility", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().isMultiFacility());
        
        try{
        	DateFormat shiftDateFormater = new SimpleDateFormat("yyyy-MM-dd");
        	session.setAttribute("requestCutOffNoOfDays", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getRequestCutOffNoOfDays());
        	if(roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getRequestCutOffDate()!=null){
        		session.setAttribute("requestToDateCutOff",shiftDateFormater.format(roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getRequestCutOffDate()));
    		}else{
    			session.setAttribute("requestToDateCutOff","NA");
    		}
        	if(roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getDaysRequest().equalsIgnoreCase("All") 
    				|| roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getDaysRequest().trim().equalsIgnoreCase("") 
    				|| roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getDaysRequest()==null){
        		session.setAttribute("daysRequest",0);			
    		}else{			
    			session.setAttribute("daysRequest",Integer.parseInt(roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getDaysRequest()));
    		}
        	
			try {
				JSONArray jArraysize = new JSONArray();
				List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMapping = facilityBO
						.getAllFacilitiesAttachedToUser(roleMaster.get(0).getEfmFmUserMaster().getUserId());
				String facilityIds = "";
				if (!(eFmFmUserFacilityMapping.isEmpty())) {
					for (EFmFmUserFacilityMappingPO userFacility : eFmFmUserFacilityMapping) {
						JSONObject facilityDetails = new JSONObject();
						facilityIds = facilityIds + userFacility.geteFmFmClientBranchPO().getBranchId() + ",";
						facilityDetails.put("branchId", userFacility.geteFmFmClientBranchPO().getBranchId());
						facilityDetails.put("name", userFacility.geteFmFmClientBranchPO().getBranchName());
						jArraysize.put(facilityDetails);
					}
				}
				session.setAttribute("combinedFacility", facilityIds.substring(0, facilityIds.length() - 1));
				session.setAttribute("userFacilities", jArraysize.toString());

			} catch (Exception e) {
				log.info("error" + e);
			}
        	
		session.setAttribute("managerReqCreateProcess", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getManagerReqCreateProcess());

        session.setAttribute("earlyRequestDate", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getEarlyRequestDate());		
        session.setAttribute("occurrenceFlg", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getOccurrenceFlg());
        session.setAttribute("destinationPointLimit",roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getDestinationPointLimit());
        session.setAttribute("managerReqCreateProcess", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getManagerReqCreateProcess());
        session.setAttribute("requestWithProject", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getRequestWithProject());
        }catch(Exception e){
        	log.info("error"+e);
        }
        session.setAttribute("requestType", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getRequestType());
        session.setAttribute("monthOrDays", roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMonthOrDays());
        session.setAttribute("approvalProcess",roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess()); 
        session.setAttribute("minimumDestCount",roleMaster.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMinimumDestCount());	
        
        if(rolevalue.equalsIgnoreCase("webuser")){                	
          	 if(roleMaster.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("Yes")){
          		 session.setAttribute("requestWithProject","Yes");
          		 session.setAttribute("managerReqCreateProcess","No");                    	
              }else{
            	  session.setAttribute("requestWithProject","No");
            	  session.setAttribute("managerReqCreateProcess","No"); 
              }
          }else if(rolevalue.equalsIgnoreCase("manager") || rolevalue.equalsIgnoreCase("supervisor") ){  
          	
          	if(roleMaster.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
             			&& roleMaster.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
          		 		session.setAttribute("requestWithProject","Yes");
          		 		session.setAttribute("managerReqCreateProcess","No");                    	
            	}else if(roleMaster.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("NO") 
             			&& roleMaster.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
            		 session.setAttribute("requestWithProject","No");
            		 session.setAttribute("managerReqCreateProcess","No");                    	
            	}else{
            		 session.setAttribute("requestWithProject","Yes");
            		 session.setAttribute("managerReqCreateProcess","Yes"); 
            	}          	
          	
          }else if(rolevalue.equalsIgnoreCase("admin") || rolevalue.equalsIgnoreCase("superadmin")){  
        	  if(roleMaster.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
           			|| roleMaster.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("YES")){
        		 		session.setAttribute("requestWithProject","Yes");
        		 		session.setAttribute("managerReqCreateProcess","Yes");                    	
          	}
          }
        
        
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        List<EFmFmUserMasterPO> userDetail = iUserMasterBO.getSpecificUserDetailsByUserName(userName);
        userDetail.get(0).setLastLoginTime(new Date());
        iUserMasterBO.update(userDetail.get(0));
        //code getPasswordResetPeriodForAdminInDays estimation(alert) three days before
        if ((!(userDetail.isEmpty()) && (passwordChangeDate(userDetail.get(0).getPasswordChangeDate(),
                userDetail.get(0).geteFmFmClientBranchPO().getPasswordResetPeriodForAdminInDays()-3) < new Date()
                        .getTime())) && !(userDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))) {
            log.info("inside logindenied");
            session.setAttribute("passExpire", "Y");
        } else {
            session.setAttribute("passExpire", "N");

        }

        List<EFmFmClientUserRolePO> userDetails = userMasterBO.getUserRolesFromUserIdAndBranchId(
                roleMaster.get(0).getEfmFmUserMaster().getUserId());
        log.info("Total Number of  accessable modules in controller...." + userDetails.size());
        List<EFmFmClientBranchConfigurationMappingPO> userMainModules = userMasterBO
                .getAllBranchMappingDetailsByBranchId(String.valueOf(roleMaster.get(0).geteFmFmClientBranchPO().getBranchId()));
        if (!(userDetails.isEmpty())) {
            for (EFmFmClientBranchConfigurationMappingPO mainModule : userMainModules) {
                List<EFmFmClientUserRolePO> moduleExistCheck = userMasterBO.getUserModulesByUserIdBranchIdAndModuleId(
                        roleMaster.get(0).getEfmFmUserMaster().getUserId(),
                        mainModule.geteFmFmClientBranchConfiguration().getClientBranchConfigurationId());
                if (moduleExistCheck.isEmpty()) {
                    session.setAttribute("iS"
                            + mainModule.geteFmFmClientBranchConfiguration().getModuleName().replaceAll("\\s+", ""),
                            false);
                } else {
                    session.setAttribute("iS"
                            + mainModule.geteFmFmClientBranchConfiguration().getModuleName().replaceAll("\\s+", ""),
                            true);
                }
                List<EFmFmClientBranchSubConfigurationPO> subModulesOfModule = userMasterBO
                        .getSubModulesOfMainModuleByModuleId(
                                mainModule.geteFmFmClientBranchConfiguration().getClientBranchConfigurationId());
                for (EFmFmClientBranchSubConfigurationPO subModules : subModulesOfModule) {
                    if (!(mainModule.geteFmFmClientBranchConfiguration().getModuleName().trim()
                            .equalsIgnoreCase(subModules.getSubModuleName().trim()))) {
                        List<EFmFmClientUserRolePO> subModuleExistCheck = userMasterBO
                                .getUserSubModulesByUserIdBranchIdAndSubModuleId(
                                        roleMaster.get(0).getEfmFmUserMaster().getUserId(),
                                        subModules.getClientBranchSubConfigurationId());
                        if (subModuleExistCheck.isEmpty()) {
                            session.setAttribute("iS" + subModules.getSubModuleName().replaceAll("\\s+", ""), false);
                        } else {
                            session.setAttribute("iS" + subModules.getSubModuleName().replaceAll("\\s+", ""), true);
                        }
                    }

                }
            }

        }
    }

    @RequestMapping(value = "/downloadRoutes.do", method = RequestMethod.GET)
    public @ResponseBody void downloadFiles(@RequestParam("fileName") String downloadFileName,
            HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = request.getServletContext();

        String name = "os.name", filePath = "";
        boolean OsName = System.getProperty(name).startsWith("Windows");

        if (OsName) {
            filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER + downloadFileName;
        } else {
            filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER + downloadFileName;
        }
        File downloadFile = new File(filePath);
        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            inputStream = new FileInputStream(downloadFile);
            response.setContentType(context.getMimeType(filePath));
            response.setContentLength((int) downloadFile.length());

            // response header
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());

            response.setHeader(headerKey, headerValue);

            // Write response
            outStream = response.getOutputStream();
            IOUtils.copy(inputStream, outStream);

        } catch (Exception e) {
        	log.info("error ="+e);
        } finally {
            try {
                if (null != inputStream)
                    inputStream.close();
                if (null != inputStream)
                    outStream.close();
            } catch (IOException e) {
            	log.info("error ="+e);
            }

        }

    }

    protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
        for (int j = 0; j < cookieValue.length() % 4; j++) {
            cookieValue = cookieValue + "=";
        }

        if (!Base.isBase64(cookieValue.getBytes())) {
            throw new InvalidCookieException("Cookie token was not Base64 encoded; value was '" + cookieValue + "'");
        }

        String cookieAsPlainText = new String(Base.decode(cookieValue.getBytes()));

        String[] tokens = StringUtils.delimitedListToStringArray(cookieAsPlainText, ":");

        if ((tokens[0].equalsIgnoreCase("http") || tokens[0].equalsIgnoreCase("https")) && tokens[1].startsWith("//")) {
            // Assume we've accidentally split a URL (OpenID identifier)
            String[] newTokens = new String[tokens.length - 1];
            newTokens[0] = tokens[0] + ":" + tokens[1];
            System.arraycopy(tokens, 2, newTokens, 1, newTokens.length - 1);
            tokens = newTokens;
        }

        return tokens;
    }

    public long passwordChangeDate(Date pastchangePassDate, int numDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pastchangePassDate);
        calendar.add(Calendar.DAY_OF_YEAR, numDays);
        return calendar.getTimeInMillis();
    }
    
  
  
    private String userAgentSelection(String userAgent1){   	
        String  browserDetails  =   userAgent1;
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";
        try{
        log.info("User Agent for the request is===>"+browserDetails);
        //=================OS=======================
         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
         {
             os = "Windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
         {
             os = "Mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
         {
             os = "Unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
         {
             os = "Android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
         {
             os = "IPhone";
         }else{
             os = "UnKnown, More-Info: "+userAgent;
         }
         //===============Browser===========================
        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        log.info("Operating System======>"+os);
        log.info("Browser Name==========>"+browser);
	}catch(Exception e){
		
	}
	return browser;
}
    
    
    
	 public LdapContext getLdapContext(){
	        LdapContext ctx = null;
	        try{
	            Hashtable<String, String> env = new Hashtable<String, String>();
	            env.put(Context.INITIAL_CONTEXT_FACTORY,
	                    "com.sun.jndi.ldap.LdapCtxFactory");
	            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
	            env.put(Context.SECURITY_PRINCIPAL, "traveluser");
	            env.put(Context.SECURITY_CREDENTIALS, "$ervion!23");
	            env.put(Context.PROVIDER_URL, "ldap://172.16.2.53:389");
	            ctx = new InitialLdapContext(env, null);
	            System.out.println("Connection Successful.");
	        }catch(NamingException nex){
	            System.out.println("LDAP Connection: FAILED");
	            nex.printStackTrace();
	        }
	        return ctx;
	    }
	 
	    private User getUserBasicAttributes(String username, LdapContext ctx) {
	    	System.out.println("in");
	        User user=null;
	        try {
	 
	            SearchControls constraints = new SearchControls();
	            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
	            String[] attrIDs = { "distinguishedName",
	                    "sn",
	                    "givenname",
	                    "mail",
	                    "telephonenumber"};
	            constraints.setReturningAttributes(attrIDs);
	            //First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
	            //Second Attribute can be uid=username
	            NamingEnumeration answer = ctx.search("DC=newtglobsl,DC=local", "sAMAccountName="
	                    + username, constraints);
	            if (answer.hasMore()) {
	                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
	               log.info("distinguishedName "+ attrs.get("distinguishedName"));
	               log.info("givenname "+ attrs.get("givenname"));
	               log.info("sn "+ attrs.get("sn"));
	               log.info("mail "+ attrs.get("mail"));
	               log.info("telephonenumber "+ attrs.get("telephonenumber"));
	            }else{
	                log.info("Invalid User");
	            }
	 
	        } catch (Exception ex) {
	        	log.info("Error"+ex);
	        }
	        return user;
	    }
}
