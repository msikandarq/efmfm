<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    This page is a parent for all the other pages. 
@State          home
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
05/05/2016  Saima Aziz      Added Import Data for Guest Requests under Import Data Link
-->

<div class="loadingMain"></div>
<div ng-include = "'partials/showAlertMessageTemplate.jsp'"></div>

<div ng-include = "'partials/alertPopups/sosAlertMessagePopup.jsp'" ng-controller = "autoRouteCtrl"></div>
<div ng-if = "adminRole" ng-include = "'partials/alertPopups/sosAlertMessagePopup.jsp'" ng-controller = "sosAlertPopupCtrl"></div>
<div ng-if = "adminRole" ng-include = "'partials/alertPopups/driverLateAlertMessagePopup.jsp'" ng-controller = "driverAlertPopupCtrl"></div>

<div class="homePage container-fluid">

<!--    NAVIGATION BAR    -->
   <nav class = "navbar navBar_home margin0">
        <div class = "container-fluid innerNavigation">
            <!--Branding Log - Company Name-->
            <div class="navbar-header pull-left">
                <a ui-sref="home.dashboard" class = "navbar-brand custome_navbarBrand_home"><img src="images/efmfm-logo.png" width="120px"> </a> 

           </div>      
                <!--Navigation button will appear for small screen devices-->
              <div class = "navbar-header pull-right pointer">
          <a ui-sref="home.dashboard" class = "navbar-brand custome_navbarBrand_home"><img src="images/genpact-logo.png" width="120px" ng-show="branchCode == 'GNPTJP'"> </a>    
            <button type="button" 
                        class="navbar-toggle" 
                        data-toggle="collapse" 
                        data-target=".targetForButton_home">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>                
                </button>
                <div class="floatRight rightHeader_home">
                    <div class = "admin_home floatLeft dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" role="button">
                            <span class="icon-cog adminIconHeader" aria-hidden="true"></span></a>
                        <ul class="dropdown-menu" role="menu">
<!--
                            <li class = "vendorDashboard_admin" ui-sref="home.vendorDashboard" ng-if = "isVendorDashboardActive || isVendorDashboardActive == 'true'">
                                <i class = "icon-envelope"></i><span>Vendor Management</span>
                            </li>
-->
                            <li class = "empDetail_admin" ui-sref="home.empDetails" ng-if = "isEmployeeDetailActive || isEmployeeDetailActive == 'true'">
                                <i class = "icon-group"></i><span>Employee Details</span>
                            </li>
                            <li class = "myProfile_admin" ui-sref="home.myProfile" ng-if = "isMyProfileActive || isMyProfileActive == 'true'">
                                <i class = "icon-user"></i><span>My Profile</span></li>
                            
                            <li class = "vehicleLoc_admin" ui-sref="home.checkInVehicalLocSpeed" ng-hide="userRole == 'webuser'">
                                <i class = "icon-truck"></i><span>Check-in Vehicle Tracking</span></li>
                            <li class = "vehicleLoc_admin"  ng-click="reloadPage()" ui-sref="home.excelUpload"  ng-if = "isImportDataActive || isImportDataActive == 'true'">
                                <i class = "icon-calendar"></i><span>Import Upload</span></li>

                                <li class = "vehicleLoc_admin"  ng-click="versionControl()" ng-if = "isMyProfileActive || isMyProfileActive == 'true'">
                                <i class="icon-info-sign"></i><span>About</span></li>

                                <!-- <i class = "icon-calendar"></i><span>About Version</span></li> -->
                             <!-- <a ui-sref="home.excelTest" ng-class = "{activeMenuTab: getActiveClassStatus('/excelTest')}">Excel</a></li> -->    
                            <!-- <li ng-click= "openMenu()" ng-if = "isImportDataActive || isImportDataActive == 'true'"><i class = "icon-calendar" ></i>
                                <span>Import Data</span>
                                <i class = "icon-chevron-down menuSettingArrow" ng-show = "!isOpen"></i>
                                <i class = "icon-chevron-up menuSettingArrow" ng-show = "isOpen"></i>
                                <div class = "importSubmenu"  ng-show = "isOpen" ng-controller = "importDataCtrl">
                                    <ul>
                                        <li class = "importSubItems"  ng-click= "openImportEmployee()">
                                            <span>Import Employee Data</span>
                                        </li>
                                        <li class = "importSubItems"  ng-click= "importEmployeeRequest()">
                                            <span>Import Employee Request</span>
                                        </li>       
                                                                
                                        <li class = "importSubItems"  ng-click= "importVendorData()">
                                            <span>Import Vendor Data</span>
                                        </li>
                                        <li class = "importSubItems"  ng-click= "importDriverData()">
                                            <span>Import Driver Data</span>
                                        </li>
                                        <li class = "importSubItems"  ng-click= "importVehicleData()">
                                            <span>Import Vehicle Data</span>
                                        </li>  
                                         <li class = "importSubItems"  ng-click= "importEscortData()">
                                            <span>Import Escort Data</span>
                                        </li>   
                                        <li class = "importSubItems"  ng-click= "importNodalRequest()">
                                            <span>Import Nodal Route</span>
                                        </li>                         
                                         <li class = "importSubItems"  ng-click= "importAreaData()">
                                             <span>Import Home Route</span>
                                        </li>                            
                                         <li class = "importSubItems"  ng-click= "importGuestRequestData()">
                                             <span>Import Guest Request</span>
                                        </li>                             
                                         <li class = "importSubItems"  ng-click= "importBatchDeleteExcel()">
                                             <span>Import Batch Delete Excel</span>
                                        </li>  
                                    </ul>
                                </div>
                                
                            </li>  -->                          
                            <li class= "lineDivider" ng-if = "superadmin"><hr></li>
                            
                            <li class = "clientSetting_admin" ui-sref="home.clientSettings"  ng-if = "superadmin">
                                <i class = "icon-qrcode"></i><span>Client Settings</span></li>
                            
                            <li class= "lineDivider"><hr></li>
                            <li class = "importSubItems">
                                <a ng-href="j_spring_security_logout" id="logOut"><i class="icon-key"></i> Log Out</a></li>
                       </ul>
                    </div>
                </div>
            </div>
            
            <!--Navigetion Links on the Right-->
            <div class="clearfix visible-sm visible-md visible-xs"></div>
            <div class = "collapse navbar-collapse targetForButton_home">                
                    <ul class="nav navbar-nav navMenu_home navbar-left">
                        <li class="dashboardMenu" ng-if = "isDashboardActive || isDashboardActive == 'true'">
                            <a ui-sref="home.dashboard" ng-class = "{activeMenuTab: getActiveClassStatus('/dashboard')}">Dashboard</a></li>                         
                        <li class="" ng-if = "isEmployeeRosterActive  || isEmployeeRosterActive == 'true'">
                            <a ui-sref="home.employeeTravelDesk" ng-class = "{activeMenuTab: getActiveClassStatus('/employeeTravelDesk')}">Employee Roster</a></li>  
                        <li class="serviceMappingMenu" ng-if = "isCaballocationActive || isCaballocationActive == 'true'">
                            <a ui-sref="home.servicemapping" ng-class = "{activeMenuTab: getActiveClassStatus('/servicemapping')}">Routing/Cab allocation</a></li>     
                        <li class="veiwMapMenu" ng-if = "isLiveTrackingActive || isLiveTrackingActive == 'true'">
                            <a ui-sref="home.viewmap" ng-class = "{activeMenuTab: getActiveClassStatus('/viewmap')}">Live Tracking</a></li>                       
                        <li class="reportMenu" ng-if = "isReportsActive || isReportsActive == 'true'">
                            <a ui-sref="home.reports" ng-class = "{activeMenuTab: getActiveClassStatus('/reports')}">Reports</a></li>
                        <li class="invoiceMenu" ng-if = "isInvoiceActive || isInvoiceActive == 'true'">
                            <a ui-sref="home.invoice" ng-class = "{activeMenuTab: getActiveClassStatus('/invoice')}">Financial Analytics</a></li>  
                        <li class="approvalMenu" ng-if = "IsApprovalActive || IsApprovalActive == 'true'">
                            <a ui-sref="home.approval" ng-class = "{activeMenuTab: getActiveClassStatus('/approval')}">Approval</a></li>

                         <!-- <li class="approvalMenu">
                            <a ui-sref="home.excelTest" ng-class = "{activeMenuTab: getActiveClassStatus('/excelTest')}">Excel</a></li> -->

                        <li class = "employeeRequest" ng-if = "iSRequestDetails && iSRequestDetails == 'true'">
                            <a ui-sref="home.requestDetails" ng-class = "{activeMenuTab: getActiveClassStatus('/requestDetails')}">Request Details</a></li>
                        <li class = "employeeRequestAdhoc" ng-if = "isAdhocRequestActive && isAdhocRequestActive == 'true'">
                            <a ui-sref="home.adhocRequests" ng-class = "{activeMenuTab: getActiveClassStatus('/adhocRequests')}">AD-HOC Request</a></li>
<!--
                        <li class = "superadminMenu" ng-if = "superadmin">
                            <a ui-sref="home.clientSettings" ng-class = "{activeMenuTab: getActiveClassStatus('/clientSettings')}">Client Settings</a></li>
-->
                        <li class = "superadminMenu" ng-if = "isVendorDashboardActive || isVendorDashboardActive == 'true'">
                            <a ui-sref="home.vendorDashboard" ng-class = "{activeMenuTab: getActiveClassStatus('/vendorDashboard')}">Vendor Management</a></li>
                    </ul>            
            </div> 
        </div>
    </nav> 
<!-- END OF NAVIGATION-->
  <div class ="homeContent">
    <div ui-view></div>
  </div>  
</div>

