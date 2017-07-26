<!--
@date           04/01/2015
@Author         Saima Aziz
@Description    Main Page
@State
@URL

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->
<!doctype html> 
<html ng-app="efmfmApp">
<head>
<meta charset="utf-8">
<!-- <meta http-equiv="Access-Control-Allow-Origin" content="https://wheelsreloaded.efmfm.com"> -->
<title>eFmFm</title>
<script src="scripts/directives/bluebird.min.js" type="text/javascript"></script>
<link rel="icon" type="image/png" href="images/favicon.png" />
<link href="bower_components/bootstrap/css/bootstrap.min.css"
    rel="stylesheet">
<link href="bower_components/bootstrap/css/bootstrap-theme.min.css"
    rel="stylesheet">
 <link rel="stylesheet"
    href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="styles/fonts/font.css" type="text/css">
<link rel="stylesheet"
    href="bower_components/font-awesome/css/font-awesome.css"
    type="text/css">
<link rel="stylesheet" href="styles/eFmFmStyle.css">
<link rel="stylesheet" href="styles/loading.css">
<link rel="stylesheet" href="styles/index2.css">
<link rel="stylesheet" href="styles/eFmFmAnimate.css">
<link rel= "stylesheet" href = "styles/isteven-multi-select.css">
<link rel="stylesheet"
    href="bower_components/dropdownCheckbox/multiselect.css"
    type="text/css">
<link rel='stylesheet' href='bower_components/angular-loading-bar/src/loading-bar.css' type='text/css' media='all' />
<script async defer
    src="https://maps.googleapis.com/maps/api/js?client=gme-newtglobalindiaprivate&v=3.25" type="text/javascript"></script>
<script src="bower_components/angular/angular.js" type="text/javascript"></script>
<script src="bower_components/ngProgress/ngProgress.js" type="text/javascript"></script>
<link rel="stylesheet" href="bower_components/ngProgress/ngProgress.css">
<script src="bower_components/pagination/dirPagination.js" type="text/javascript"></script>
<link href="bower_components/ngSpinner/angular-busy.min.css"
    rel="stylesheet">
<script src="bower_components/jsPDF/jspdf.min.js" type="text/javascript"></script>
<script src="bower_components/jsPDF/jspdf.debug.js" type="text/javascript"></script>
<script src="bower_components/excelExport/alasql.min.js" type="text/javascript"></script>
<script src="bower_components/excelExport/xlsx.core.min.js" type="text/javascript"></script>
<script src="bower_components/excelExport/FileSaver.js" type="text/javascript"></script>
<link rel="stylesheet" href="styles/ngDialog.css">
<link rel="stylesheet" href="styles/ngDialog-theme-default.css">
<link href="bower_components/angular-xeditable/css/xeditable.css"
    rel="stylesheet">
<link rel="stylesheet" href="bower_components/angular-rateit/dist/ng-rateit.css">
<link rel="stylesheet" href="bower_components/ngFloatingLabels/ng-floating-labels.css">
<link rel="stylesheet" href="bower_components/angular-ui-tab-scroll/angular-ui-tab-scroll.css">
    <!-- <link href="bower_components/angular-bootstrap-toggle/angular-bootstrap-toggle.css"
    rel="stylesheet"> -->
<!--        <script src = "bower_components/animationCss/animate.css"></script>-->
<script src="bower_components/multiDropdown/highlight.pack.js" type="text/javascript"></script>
<script src="bower_components/multiDropdown/isteven-multi-select.js" type="text/javascript"></script>
<link href="bower_components/sweetalert/dist/sweetalert.css"
    rel="stylesheet">
<!-- <script src="bower_components/ng-file-model/ng-file-model.js" type="text/javascript"></script> -->

</head>


<body ng-controller="homeCtrl">

<div ng-include = "'partials/timeOutModal.jsp'"></div>
    <div ui-view>
        <div class="hidden">
       <select name="format">
            <option value="csv" selected> demo123</option>
        </select>
        <div id="drop"></div>
        <p><input type="file" name="xlfile" id="xlf" /></p>
            <input type="checkbox" name="useworker" checked><br />
            <input type="checkbox" name="xferable" checked><br />
            <input type="checkbox" name="userabs" checked><br />
        <pre id="out"></pre>
        </div>
    </div>


    <script src="bower_components/angular-ui-router/release/angular-ui-router.js" type="text/javascript"></script>
    <script src="bower_components/angular-bootstrap/ui-bootstrap-tpls2.js" type="text/javascript"></script>
    <script src="bower_components/jquery/dist/jquery.min.js" type="text/javascript"></script>
    <script src="bower_components/jquery/dist/jquery.cookie.js" type="text/javascript"></script>
    <script src="bower_components/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="bower_components/bootstrap/js/jasny-bootstrap.min.js" type="text/javascript"> </script>
    <script src="//code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js" type="text/javascript"></script>
    <script src="bower_components/underscore/underscore-min.js" type="text/javascript"></script>
    <script src="bower_components/angular-animate/angular-animate.min.js" type="text/javascript"></script>
    <script src="bower_components/ngSpinner/angular-busy.min.js" type="text/javascript"></script>
    <script src='bower_components/angular-confirm/angular-confirm.js' type="text/javascript" ></script>
    <script src='bower_components/ng-auto-scroll/ng-infinite-scroll.min.js' type="text/javascript" ></script>
    <script src="bower_components/deviceDetector/re-tree.js" type="text/javascript"></script>
    <script src="bower_components/deviceDetector/ng-device-detector.js" type="text/javascript"></script>
    <script src="bower_components/ngDialog/ngDialog.js" type="text/javascript"></script>
    <script src="bower_components/angular-xeditable/js/xeditable.js" type="text/javascript"></script>
    <script src="bower_components/dropdownCheckbox/multiselect-tpls.js" type="text/javascript"></script>
    <script src="bower_components/ngFloatingLabels/ngFloatingLabels.js" type="text/javascript"></script>
    <script src="bower_components/oclazyload/dist/ocLazyLoad.js" type="text/javascript"></script>
    <script src="bower_components/angular-idle/angular-idle.js" type="text/javascript"></script>
    <script src="bower_components/sweetalert/dist/sweetalert.min.js" type="text/javascript"></script>
    <script src="bower_components/dropdownMultiSelectDirectory/multiSelectDropdown.js" type="text/javascript"></script>
    <script src="bower_components/angular-rateit/dist/ng-rateit.js" type="text/javascript"></script>
    <script src="bower_components/checklist-model/checklist-model.js" type="text/javascript"></script>
    <script src="bower_components/infinite-scroll-master/taggedInfiniteScroll.js" type="text/javascript"></script>
    <link rel="stylesheet" href="styles/multiSelectDropdown.css">
    
    <!-- <script src="bower_components/angular-bootstrap-toggle/angular-bootstrap-toggle.js" type="text/javascript"></script> -->

    <!-- app.js - have all the routing information  -->
    <script src="scripts/app.js" type="text/javascript"></script>

    <!--Controllers-->
    <script src="scripts/controllers/commonJs/autoRouteService.js" type="text/javascript"></script>
    <script src="scripts/controllers/commonJs/createMultipleRequest.js" type="text/javascript"></script>
    <script src="scripts/controllers/login.js" type="text/javascript"></script>
    <script src="scripts/controllers/home.js" type="text/javascript"></script>
    <script src="scripts/controllers/commonJs/sosAlertPopup.js" type="text/javascript"></script>
    <script src="scripts/controllers/commonJs/driverAlertPopup.js" type="text/javascript"></script>
    <script src="scripts/controllers/dashboard.js" type="text/javascript"></script>
    <script src="scripts/controllers/viewMap.js" type="text/javascript"></script>
    <script src="scripts/controllers/approval.js" type="text/javascript"></script>
    <script src="scripts/controllers/serviceMapping.js" type="text/javascript"></script>
    <script src="scripts/controllers/serviceRouteMap.js" type="text/javascript"></script>
    <script src="scripts/controllers/invoice.js" type="text/javascript"></script>
    <script src="scripts/controllers/employeeRequestDetails.js" type="text/javascript"></script>
    <script src="scripts/controllers/adhocRequests.js" type="text/javascript"></script>
    <script src="scripts/controllers/commonJs/cabRequests.js" type="text/javascript"></script>
    <script src="scripts/controllers/alerts.js" type="text/javascript"></script>
    <script src="scripts/controllers/vendorDashboard.js" type="text/javascript"></script>
    <script src="scripts/controllers/routeMap.js" type="text/javascript"></script>
    <script src="scripts/controllers/allRouteMap.js" type="text/javascript"></script>
    <script src="scripts/controllers/employeeTravelDesk.js" type="text/javascript"></script>
    <script src="scripts/controllers/rescheduleRequest.js" type="text/javascript"></script>
    <script src="scripts/controllers/empDetails.js" type="text/javascript"></script>
    <script src="scripts/controllers/myProfile.js" type="text/javascript"></script>
    <script src="scripts/controllers/reports.js" type="text/javascript"></script>
    <script src="scripts/controllers/edsModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/sosModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/epsModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/vsModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/gdModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/gvModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/importData.js" type="text/javascript"></script>
    <script src="scripts/controllers/gsModal.js" type="text/javascript"></script>
    <script src="scripts/controllers/clientSettings.js" type="text/javascript"></script>
    <script src="scripts/controllers/checkInVehicleLocSpeed.js" type="text/javascript"></script>
    <script src="scripts/controllers/bulkRouteClose.js" type="text/javascript"></script>
    <script src="scripts/controllers/customRouteViewCtrl.js" type="text/javascript"></script>
    <script src="scripts/controllers/excelUploadCtrl.js" type="text/javascript"></script>
    <script src="scripts/controllers/versionManegementCtrl.js" type="text/javascript"></script>
    <script src="scripts/controllers/allocateRouteToVendorCtrl.js" type="text/javascript"></script>

    <!--Factory-->
    <script src="scripts/factory/restApi.js" type="text/javascript"></script>

    <!--Services-->
    <script src="scripts/services/empDetails.js" type="text/javascript"></script>
    <script src="scripts/services/alerts.js" type="text/javascript"></script>
    <script src="scripts/services/invoiceService.js" type="text/javascript"></script>
    <script type='text/javascript' src='bower_components/angular-loading-bar/src/loading-bar.js' type="text/javascript"></script>

    <!--Directives-->
    <script src="scripts/directives/efmfmbutton.js" type="text/javascript"></script>
    <!-- <script src="bower_components/lodash/lodash.js" type="text/javascript"></script> -->

    <script src="scripts/directives/href.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmShowAllLiveTrips.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmSingleLiveTrip.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmGoogleMapSingle.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmMapLocation.js" type="text/javascript"></script>
    <script src="scripts/directives/cleanInputField.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmNewUserMapLocation.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmNewUserMapSearchLocation.js" type="text/javascript"></script>
    <script src="scripts/directives/checkboxGroup.js" type="text/javascript"></script>
    <script src="scripts/directives/angularjs-dropdown-multiselect.js" type="text/javascript"></script>
    <script src="scripts/directives/ngBlur.js" type="text/javascript"></script>
    <script src="scripts/directives/ngBlink.js" type="text/javascript"></script>
    <script src="bower_components/play-alertSound/alertSound.js" type="text/javascript"></script>
    <script src="scripts/directives/fixedTableColumns.js" type="text/javascript"></script>
    <script src="scripts/directives/ngValidateFileType.js" type="text/javascript"></script>
    <script src="scripts/directives/fixedTableHeader.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmShowAllCabLocation.js" type="text/javascript"></script>
    <script src="scripts/directives/textBoxValidation.js" type="text/javascript"></script>
    <script src="scripts/directives/efmfmRouteHistory.js" type="text/javascript"></script>
    <script src="scripts/directives/previewMultipleLocation.js" type="text/javascript"></script>
    <script src="scripts/directives/numberValidation.js" type="text/javascript"></script>

    <!--Filters-->
    <script src="scripts/filters/telephoneFormat.js" type="text/javascript"></script>
    <script src="scripts/filters/alertSearch.js" type="text/javascript"></script>
    <script src="scripts/filters/roundDecimalNumbers.js" type="text/javascript"></script>
    <script src="scripts/filters/filterPagination.js" type="text/javascript"></script>

    <script src="bower_components/js-xlsx-master/shim.js" type="text/javascript"></script>
    <script src="bower_components/js-xlsx-master/jszip.js" type="text/javascript"></script>
    <script src="bower_components/js-xlsx-master/xlsx.js" type="text/javascript"></script>
    <script src="bower_components/js-xlsx-master/ods.js" type="text/javascript"></script>
    <script src="bower_components/js-xlsx-master/fileValidation.js" type="text/javascript"></script>
    <script src="bower_components/ngmap/build/scripts/ng-map.min.js" type="text/javascript"></script>
    <script src="bower_components/angular-ui-tab-scroll/angular-ui-tab-scroll.js" type="text/javascript"></script>
    <!--Modules-->
    <script src="scripts/module/passwordValidation.js" type="text/javascript"></script>

    <script type="text/javascript">

    <%
    int timeout = session.getMaxInactiveInterval();
    response.setHeader("Refresh", timeout + "; URL = index");
    %>
    var firstName = "<%=request.getSession().getAttribute("firstName")%>";
    var lastName = "<%=request.getSession().getAttribute("lastName")%>";
    var branchId = "<%=request.getSession().getAttribute("branchId")%>";
    var profileId = "<%=request.getSession().getAttribute("profileId")%>";
    var userRole ="<%=request.getSession().getAttribute("role")%>";
    var access = "<%=request.getSession().getAttribute("accessModules")%>";
    var passExpire = "<%=request.getSession().getAttribute("passExpire")%>";
    var tempPassChange = "<%=request.getSession().getAttribute("tempPassChange")%>";
    var combinedFacility = "<%=request.getSession().getAttribute("combinedFacility")%>";
    var userFacilities = <%=request.getSession().getAttribute("userFacilities")%>;

    var officeLocation ="<%=request.getSession().getAttribute("officeLocation")%>";

    var isDashboardActive ="<%=request.getSession().getAttribute("iSDashboard")%>";
    var isEmployeeRosterActive ="<%=request.getSession().getAttribute("iSEmployeeRoster")%>";
    var isCaballocationActive ="<%=request.getSession().getAttribute("iSRoutingCabAllocation")%>";
    var isLiveTrackingActive ="<%=request.getSession().getAttribute("iSLiveTracking")%>";
    var isReportsActive ="<%=request.getSession().getAttribute("iSReports")%>";
    var isInvoiceActive ="<%=request.getSession().getAttribute("iSFinancialAnalytics")%>";
    var IsApprovalActive ="<%=request.getSession().getAttribute("iSApproval")%>";
    var isAdhocRequestActive ="<%=request.getSession().getAttribute("iSAdhocRequest")%>";

    var isVendorDashboardActive ="<%=request.getSession().getAttribute("iSVendorDashBoard")%>";
    var isEmployeeDetailActive ="<%=request.getSession().getAttribute("iSEmployeeDetails")%>";
    var isMyProfileActive ="<%=request.getSession().getAttribute("iSMyProfile")%>";
    var isImportDataActive ="<%=request.getSession().getAttribute("iSImportData")%>";

    var iSRequestDetails ="<%=request.getSession().getAttribute("iSRequestDetails")%>";
    var iSAssignCab ="<%=request.getSession().getAttribute("iSAssignCab")%>";
    var iSUpdateShiftTime ="<%=request.getSession().getAttribute("iSUpdateShiftTime")%>";
    var iSEditEmployeeDetailsforRoster ="<%=request.getSession().getAttribute("iSEditEmployeeDetailsforRoster")%>";
    var iSCreateNewRoutes ="<%=request.getSession().getAttribute("iSCreateNewRoutes")%>";
    var iSEditRoutes ="<%=request.getSession().getAttribute("iSEditRoutes")%>";
    var iSCloseRoutes ="<%=request.getSession().getAttribute("iSCloseRoutes")%>";
    var iSDeleteRoute ="<%=request.getSession().getAttribute("iSDeleteRoute")%>";
    var iSManuallyTripStartedEnded ="<%=request.getSession().getAttribute("iSManuallyTripStartedEnded")%>";
    var iSUploadRoutes ="<%=request.getSession().getAttribute("iSUploadRoutes")%>";
    var iSDeleteEmployeefromRoute ="<%=request.getSession().getAttribute("iSDeleteEmployeefromRoute")%>";
    var iSChangeEmployeeStatus ="<%=request.getSession().getAttribute("iSChangeEmployeeStatus")%>";
    var iSMoveEmployeetodifferentZones ="<%=request.getSession().getAttribute("iSMoveEmployeetodifferentZones")%>";
    var iSMovesEmployeetodifferentRoutes ="<%=request.getSession().getAttribute("iSMovesEmployeetodifferentRoutes")%>";
    var iSPrepareToDownload ="<%=request.getSession().getAttribute("iSPrepareToDownload")%>";
    var iSDownloadRoutes ="<%=request.getSession().getAttribute("iSDownloadRoutes")%>";
    var iSUpdatePickupTime ="<%=request.getSession().getAttribute("iSUpdatePickupTime")%>";
    var iSViewRouteonMap ="<%=request.getSession().getAttribute("iSViewRouteonMap")%>";
    var iSTripSheet ="<%=request.getSession().getAttribute("iSTripSheet")%>";
    var iSOnTime ="<%=request.getSession().getAttribute("iSOnTime")%>";
    var iSSeatUtilization ="<%=request.getSession().getAttribute("iSSeatUtilization")%>";
    var iSNoShow ="<%=request.getSession().getAttribute("iSNoShow")%>";
    var iSDistance ="<%=request.getSession().getAttribute("iSDistance")%>";
    var iSDailySMS ="<%=request.getSession().getAttribute("iSDailySMS")%>";
    var iSVehicleDriverAttendance ="<%=request.getSession().getAttribute("iSVehicleDriverAttendance")%>";
    var iSDriverWorkingHours ="<%=request.getSession().getAttribute("iSDriverWorkingHours")%>";
    var iSSpeed ="<%=request.getSession().getAttribute("iSSpeed")%>";
    var iSRouteWiseTravelTime ="<%=request.getSession().getAttribute("iSRouteWiseTravelTime")%>";
    var iSRejectEmployee ="<%=request.getSession().getAttribute("iSRejectEmployee")%>";
    var iSApproveEmployee ="<%=request.getSession().getAttribute("iSApproveEmployee")%>";
    var iSRemoveEmployee ="<%=request.getSession().getAttribute("iSRemoveEmployee")%>";
    var iSAddAgainEmployee ="<%=request.getSession().getAttribute("iSAddAgainEmployee")%>";
    var iSReScheduleRequest ="<%=request.getSession().getAttribute("iSRe-ScheduleRequest")%>";
    var iSCreateRequest ="<%=request.getSession().getAttribute("iSCreateRequest")%>";
    var iSVendorManagement ="<%=request.getSession().getAttribute("iSVendorManagement")%>";
    var iSEscortManagement ="<%=request.getSession().getAttribute("iSEscortManagement")%>";
    var iSCheckInDrivers ="<%=request.getSession().getAttribute("iSCheckInDrivers")%>";
    var iSAvailableDrivers ="<%=request.getSession().getAttribute("iSAvailableDrivers")%>";
    var iSDriversonRoad ="<%=request.getSession().getAttribute("iSDriversonRoad")%>";
    var iSPlannedReleaseDrivers ="<%=request.getSession().getAttribute("iSPlannedReleaseDrivers")%>";
    var iSCheckInEscort ="<%=request.getSession().getAttribute("iSCheckInEscort")%>";
    var iSAvailableEscorts ="<%=request.getSession().getAttribute("iSAvailableEscorts")%>";
    var iSDeviceDetail ="<%=request.getSession().getAttribute("iSDeviceDetail")%>";
    var iSVehicleInspectionForm ="<%=request.getSession().getAttribute("iSVehicleInspectionForm")%>";
    var iSVehicleInspectionDetail ="<%=request.getSession().getAttribute("iSVehicleInspectionDetail")%>";
    var iSViewDrivers ="<%=request.getSession().getAttribute("iSViewDrivers")%>";
    var iSViewVehicles ="<%=request.getSession().getAttribute("iSViewVehicles")%>";
    var iSEditVendors ="<%=request.getSession().getAttribute("iSEditVendors")%>";
    var iSAddNewVendors ="<%=request.getSession().getAttribute("iSAddNewVendors")%>";
    var iSEditDrivers ="<%=request.getSession().getAttribute("iSEditDrivers")%>";
    var iSAddNewDrivers ="<%=request.getSession().getAttribute("iSAddNewDrivers")%>";
    var iSDeleteDrivers ="<%=request.getSession().getAttribute("iSDeleteDrivers")%>";
    var iSUploadDriverDocuments ="<%=request.getSession().getAttribute("iSUploadDriverDocuments")%>";
    var iSEditVehicle ="<%=request.getSession().getAttribute("iSEditVehicle")%>";
    var iSAddNewVehicle ="<%=request.getSession().getAttribute("iSAddNewVehicle")%>";
    var iSDeleteVehicle ="<%=request.getSession().getAttribute("iSDeleteVehicle")%>";
    var iSUploadVehicle ="<%=request.getSession().getAttribute("iSUploadVehicle")%>";
    var iSAddNewEscort ="<%=request.getSession().getAttribute("iSAddNewEscort")%>";
    var iSViewEscortDetail ="<%=request.getSession().getAttribute("iSViewEscortDetail")%>";
    var iSEditEscort ="<%=request.getSession().getAttribute("iSEditEscort")%>";
    var iSUploadVehicleDocument ="<%=request.getSession().getAttribute("iSUploadVehicleDocument")%>";
    var iSEditEmployee ="<%=request.getSession().getAttribute("iSEditEmployee")%>";
    var iSEnableDisableEmployee ="<%=request.getSession().getAttribute("iSEnableDisableEmployee")%>";
    var iSProfileInfo ="<%=request.getSession().getAttribute("iSProfileInfo")%>";
    var iSEditProfile ="<%=request.getSession().getAttribute("iSEditProfile")%>";
    var iSChangePassword ="<%=request.getSession().getAttribute("iSChangePassword")%>";
    var iSShiftTimes ="<%=request.getSession().getAttribute("iSShiftTimes")%>";
    var iSNodalPoints ="<%=request.getSession().getAttribute("iSNodalPoints")%>";
    var iSAdministratorSettings ="<%=request.getSession().getAttribute("iSAdministratorSettings")%>";
    var iSApplicationSettings ="<%=request.getSession().getAttribute("iSApplicationSettings")%>";
    var iSRouteNames ="<%=request.getSession().getAttribute("iSRouteNames")%>";
    var iSImportEmployeeData ="<%=request.getSession().getAttribute("iSImportEmployeeData")%>";
    var iSImportEmployeeRequest ="<%=request.getSession().getAttribute("iSImportEmployeeRequest")%>";
    var iSImportVendorData ="<%=request.getSession().getAttribute("iSImportVendorData")%>";
    var iSImportDriverData ="<%=request.getSession().getAttribute("iSImportDriverData")%>";
    var iSImportVehicleData ="<%=request.getSession().getAttribute("iSImportVehicleData")%>";
    var iSImportEscortData ="<%=request.getSession().getAttribute("iSImportEscortData")%>";
    var iSImportAreaData ="<%=request.getSession().getAttribute("iSImportAreaData")%>";
    var adhocTimePicker ="<%=request.getSession().getAttribute("adhocTimePicker")%>";
    var branchCode ="<%=request.getSession().getAttribute("branchCode")%>";
    var sessionTimeOutInMin ="<%=request.getSession().getAttribute("sessionTimeOutInMin")%>";
    var sessionTimeOutNotificationInSec ="<%=request.getSession().getAttribute("sessionTimeOutNotificationInSec")%>";
    var imageUploadSize ="<%=request.getSession().getAttribute("imageUploadSize")%>";
    var authenticationToken ="<%=request.getSession().getAttribute("authenticationToken")%>";
    var userAgent ="<%=request.getSession().getAttribute("userAgent")%>";
    var userIPAddress ="<%=request.getSession().getAttribute("userIPAddress")%>";
    var minimumDestCount ="<%=request.getSession().getAttribute("minimumDestCount")%>";
    var genderPreference ="<%=request.getSession().getAttribute("genderPreference")%>";
    var locationVisible ="<%=request.getSession().getAttribute("locationVisible")%>";
    var managerReqCreateProcess ="<%=request.getSession().getAttribute("managerReqCreateProcess")%>";
    var requestWithProject ="<%=request.getSession().getAttribute("requestWithProject")%>";
    var requestType ="<%=request.getSession().getAttribute("requestType")%>";
    var monthOrDays ="<%=request.getSession().getAttribute("monthOrDays")%>";
    var multiFacility ="<%=request.getSession().getAttribute("multiFacility")%>";
    var webPageCount ="<%=request.getSession().getAttribute("webPageCount")%>";
    localStorage.setItem("sessionTimeOutInMin", sessionTimeOutInMin);
    localStorage.setItem("sessionTimeOutNotificationInSec", sessionTimeOutNotificationInSec);


 //    console.log("sessionTimeOutNotificationInSec",sessionTimeOutInMin);
    // Right Click Disable Option for all browsers - so 3 rd party people can't get our UI and server side source

    /* document.addEventListener('contextmenu', event >= event.preventDefault());

    $(document).keydown(function(event){
    if(event.keyCode==123){
        return false;
    }
    else if (event.ctrlKey && event.shiftKey && event.keyCode==73){
             return false;
    }
    });

    $(document).on("contextmenu",function(e){
       e.preventDefault();
    }); */
</script>


</body>
</html>
