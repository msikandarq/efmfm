/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description      
@Main Controllers       reportsCtrl
@Modal Controllers      
@template               partials/home.reports.jsp 

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/ 

(function(){
   var reportsCtrl = function($scope, $rootScope, $location, $anchorScroll, $timeout, $http, $modal, $window, $confirm, ngDialog){     
       
    if(!$scope.isReportsActive  || $scope.isReportsActive == "false"){
        $state.go('home.accessDenied');  
    } 
       
    else{ 
      
       $('.reportMenu').addClass('active');  
        $scope.currentTab = 'tripSheet';       
        $scope.setMinDate = new Date();
        $scope.numberofRecords = 10000;
        $scope.fromDate;
        $scope.toDate;
        $scope.thisMonth;
        var selectedFrom;
        var selectedTo;
        $scope.dateRangeError = false;
        $scope.openCustomRange = false;
        $scope.dynamicDataDivEmployeeShow = false;
        $scope.dynamicDataDivDriverShow = false;
        $scope.hstep5 = 1;
        $scope.mstep5 = 1;
        $scope.ismeridian5 = false;
        $scope.branchCode = branchCode;
        $scope.viewTypeTripSheet = 'viewData';
        $scope.viewTypeOntime = 'viewData';
        $scope.facilityData = {};
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData.listOfFacility = array;


       $scope.dates = [{'name':'Today', 'isClicked':false},
                       {'name':'Yesturday', 'isClicked':false},
                       {'name':'Last 7 Days', 'isClicked':false},
                       {'name':'Last 30 Days', 'isClicked':false},
                       {'name':'This Month', 'isClicked':false},
                       {'name':'Custom Range Date', 'isClicked':false}];
       
       $scope.paginations = [{'value':10, 'text':'10 records'},
                   {'value':15, 'text':'15 record'},
                   {'value':20, 'text':'20 records'}];
       
       $scope.gotNoShowResult = false;
       
       $scope.reportTypes = [{'value':"vendor", 'text':'By VENDOR'},
                   {'value':"vehicle", 'text':'By VEHICLE'}];
       
       $scope.reportTypes_attendance = [ {'value':"vehicle", 'text':'By VEHICLE'},
                             {'value':"driver", 'text':'By DRIVER'},
                                         {'value':"vendor", 'text':'By VENDOR'}];       
       $scope.feedbackTypes = [ {'value':"all", 'text':'ALL'},
                                          {'value':"open", 'text':'Open'},
                                            {'value':"close", 'text':'Close'},
                                            {'value':"inprogress", 'text':'In-Progress'}];
      $scope.feedbackTypes.selectedType = {'value':"all", 'text':'ALL'};
       $scope.tripTypes = [{'value':'PICKUP', 'text':'PICKUP'},
                     {'value':'DROP', 'text':'DROP'}];
       
       $scope.reportKM = [];
       $scope.reportKM.reportType= {};
       $scope.reportKM.distanceShift = {};
       $scope.vendorVehicles_KMReport = [];
       $scope.reportTypeSelected;
       $scope.gotKMResult = false;
       $scope.gotTripResult = false;
       $scope.gotFeedbackResult = false;
       $scope.viewDetail = false;
       $scope.searchFromDateKM;
       $scope.searchToDateKM;  
       $scope.isShiftKM = false;
       $scope.searchKMType;
       $scope.distanceShiftType;
       
       $scope.reportsKMData = [];
       $scope.gotLoginResult = false;
       $scope.gotDropResult = false;
       $scope.gotSMSResult = false;
       $scope.gotVehComplianceResult = false;
       $scope.gotDriverComplianceResult = false;
       $scope.viewDetailIsClicked = false;
       $scope.allVendors = [];
       $scope.tripTimes = [];
       
       $scope.searchOT = [];
       $scope.searchOT.type = {};
       $scope.searchOT.OTShift = {};
       $scope.searchOT.OTVendors = {};
       $scope.OTShiftTimes = [];
       $scope.OTAllVendors = [];
       $scope.OTTripTypes = [];
       $scope.OTTripType;
       $scope.OTShift;
       $scope.OTVendor;
       $scope.OTresultTripType;
       $scope.OTresultDateOrShift;
       $scope.OTtotalShift;
       $scope.gotOTResult = false;
       $scope.vendorNameShow = false;   
       $scope.noShowResult = false; 
       $scope.OT;
      
       $scope.searchSU = [];
       $scope.searchSU.type = {};
       $scope.searchSU.SUShift = {};
       $scope.searchSU.SUVendors = {};
       $scope.SUShiftTimes = [];
       $scope.SUAllVendors = [];
       $scope.SUTripTypes = [];
       $scope.SUTripType;
       $scope.SUShift;
       $scope.SUVendor;
       $scope.SUresultTripType;
       $scope.gotSUResult = false;  
       $scope.reportSUOptions = [{text:'By Shift', value:'By Shift'}];
       
       $scope.searchNS = [];
       $scope.searchNS.type = {};
       $scope.NSTripTypes = [];  
       $scope.NSShiftTimes = [];
       $scope.NSTripType;  
       $scope.NSReportTitle;
       $scope.NSShift;
       $scope.searchFromDatesNS = '';
       $scope.searchToDatesNS = '';
       $scope.vendorWiseShiftTimes = [];
       $scope.DistanceShiftTime = [];
       $scope.vehicleNumberField;
       $scope.NSresultShift;
       $scope.searchNSByEmployeeId;
       
       $scope.reportTSExcel = [];
       $scope.reportOTExcel = [];
       $scope.reportSUExcel = [];
       $scope.reportNSExcel = [];
       $scope.reportKMExcel = [];
       $scope.reportSMSExcel = [];
       
       $scope.reportNoShowOptions = [{text:'By Count', value:'By Count'},
                                     {text:'By Employee Id', value:'By Employee Id'}];
       $scope.noShowCounts = [{text:'1', value:'1'},
                              {text:'2', value:'2'},                              
                              {text:'3+', value:'3+'}];
       $scope.gotNSResult = false; 
       
       $scope.gotEscortResult = false;
       
       $scope.gotRTResult = false;
       $scope.gotRWTResult = false;
       $scope.searchRWT = [];
       $scope.searchRWT.type = {};
       
       $scope.searchSpeed = [];
       $scope.searchSpeed.type = {};
       $scope.gotSpeedResult = false;
       $scope.vendorVehicles_SpeedReport = [];
       
       $scope.gotVDAttendanceResult = false;
        
       $scope.searchED = [];
       $scope.searchERD = [];
       $scope.searchTR= null;

       $scope.tripGetReportButtonClicked = false;
       $scope.OTGetReportButtonClicked = false;
       $scope.SUGetReportButtonClicked = false;
       $scope.NSGetReportButtonClicked = false;
       $scope.TRGetReportButtonClicked = false;
       $scope.distanceGetReportButtonClicked = false;
       $scope.SMSGetReportButtonClicked = false;
       $scope.escortGetReportButtonClicked = false;
       $scope.vdattendanceGetReportButtonClicked = false;
       $scope.workingGetReportButtonClicked = false;
       $scope.drivingHoursgGetReportButtonClicked = false;
       $scope.speedGetReportButtonClicked = false;
       $scope.routewiseGetReportButtonClicked = false;
       $scope.isTravelDistanceEdit = false;
       $scope.byReportDynamicClicked = false;
       $scope.gotDynamicResult = false;

       $scope.dynamicDataShow = function()
       {
        $scope.assignDateShow = false;
        $scope.routeCloseTimeShow = false;
        $scope.shiftTimeShow = false;
        $scope.tripTypeShow = false;
        $scope.tripStartTimeShow = false;
        $scope.tripEndTimeShow = false;
        $scope.routeNameShow = false;
        $scope.cabReachedTimeShow = false;
        $scope.boardingStatusShow = false;
        $scope.boardingTimeShow = false;
        $scope.driverNumberShow = false;
        $scope.driverNameShow = false;
        $scope.driverMobileNumberShow = false;
        $scope.driverAddressShow = false;
        $scope.checkInTimeShow = false;
        $scope.checkOutTimeShow = false;
        $scope.travelTimeShow = false;
        $scope.workingHoursShow = false;
        $scope.checkInVehicleNumberShow = false;
        $scope.alloccationMessageShow = false;
        $scope.cabDelayMsgShow = false;
        $scope.reachedMsgShow = false;
        $scope.fifteenMinuteMsgShow = false;
        $scope.noShowMsgShow = false;
        $scope.vehicleNumberShow = false;
        $scope.escortIdShow = false;
        $scope.escortNameShow = false;
        $scope.escortMobileNoShow = false;
        $scope.escortAddressShow = false;
        $scope.employeeIdShow = false;
        $scope.employeeMobileNoShow = false;
        $scope.employeeNameShow = false;
        $scope.cabDelayMessageShow = false;
        $scope.noShowMessageShow = false;
        $scope.pickupTimeShow = false;
        $scope.reachedMessageShow = false;
        $scope.empDetailsShow = false;
        $scope.plannedDistanceShow = false;
        $scope.gpsDistanceShow = false;
        $scope.vendorNameShow = false;
        $scope.driverIdShow = false;
        $scope.totalWorkingHoursShow = false;
        $scope.remarksShow = false;
        $scope.hostMobileNumberShow = false;
        $scope.noShowShow = false;
        $scope.driverDrivingHours = false;
        $scope.routeIdShow = false;
        $scope.totalDrivingHoursShow = false;
        $scope.driverDrivingHoursShow = false;
        $scope.driverDrivingHoursPerTripsShow = false;
       }
        
        
       $scope.dynamicDataInitialize = function()
       {
          $scope.assignDateFlg = 0;
          $scope.routeCloseTimeFlg = 0;
          $scope.shiftTimeFlg = 0;
          $scope.tripTypeFlg = 0;
          $scope.tripStartTimeFlg = 0;
          $scope.tripEndTimeFlg = 0;
          $scope.routeNameFlg = 0;
          $scope.cabReachedTimeFlg = 0;
          $scope.boardingStatusFlg = 0;
          $scope.boardingTimeFlg = 0;
          $scope.driverNumberFlg = 0;
          $scope.driverNameFlg = 0;
          $scope.driverMobileNumberFlg = 0;
          $scope.driverAddressFlg = 0;
          $scope.checkInTimeFlg = 0;
          $scope.checkOutTimeFlg = 0;
          $scope.travelTimeFlg = 0;
          $scope.workingHoursFlg = 0;
          $scope.checkInVehicleNumberFlg = 0;
          $scope.alloccationMessageFlg = 0;
          $scope.cabDelayMsgFlg = 0;
          $scope.reachedMsgFlg = 0;
          $scope.fifteenMinuteMsgFlg = 0;
          $scope.noShowMsgFlg = 0;
          $scope.vehicleNumberFlg = 0;
          $scope.escortIdFlg = 0;
          $scope.escortNameFlg = 0;
          $scope.escortMobileNoFlg = 0;
          $scope.escortAddressFlg = 0;
          $scope.employeeIdFlg = 0;
          $scope.employeeMobileNoFlg = 0;
          $scope.employeeNameFlg = 0;
          $scope.employeeDetailsFlg = 0;
          $scope.cabDelayMessageFlg = 0;
          $scope.noShowMessageFlg = 0;
          $scope.noShowMessage = 0;
          $scope.pickupTimeFlg = 0;
          $scope.reachedMessageFlg = 0;
          $scope.plannedDistanceFlg = 0;
          $scope.gpsFlg = 0;
          $scope.vendorNameFlg = 0;
          $scope.driverIdFlg = 0;
          $scope.totalWorkingHoursFlg = 0;
          $scope.driverDrivingHoursFlg = 0;
          $scope.remarksFlg = 0;
          $scope.hostMobileNumberFlg = 0;
          $scope.noShowFlg = 0;
          $scope.totalDrivingHoursFlg = 0;
          $scope.emailIdFlg = 0;
          $scope.employeeLocationFlg = 0;
          $scope.routeIdFlg = 0;
          $scope.driverDrivingHoursPerTripFlg = 0;

       }

      $scope.dynamicData = {
        availableOptions: [
          {value:"employee", requestTypes: 'Employee'},
          {value:"driverDetails", requestTypes: 'Driver'},
          {value:"guest", requestTypes: 'Guest'}
        ],
        selectedOption: {value:"employee", requestTypes: 'Employee'} 
      };
      

    $scope.setDynamicType = function(dynamicDataSelected)
    {
      $scope.gotDynamicResult = false;
      $scope.selectedDynamicType = dynamicDataSelected.value;
      if($scope.selectedDynamicType == 'employee' || $scope.selectedDynamicType == 'guest')
      { 
         $scope.dynamicDataDivEmployeeShow = true;
         $scope.dynamicDataDivDriverShow = false;
        
      }
      if($scope.selectedDynamicType == 'driverDetails')
      {
         $scope.dynamicDataDivEmployeeShow = false;
         $scope.dynamicDataDivDriverShow = true;
      }
      $scope.dynamicDataLoad();
      $scope.onloadDropdownChange();
    }
  

    if($scope.selectedDynamicType == undefined)
    {
      $scope.selectedDynamicType = "employee";
    }
 
    if($scope.selectedDynamicType == "employee" || $scope.selectedDynamicType == "guest")
    {
      $scope.webBrowsersGroupedData = [
        
        { 
            name: '<strong>Boarding Details - Click Here To Select All</strong>',
            msGroup: true,
            value: 2
        },
        {   
            name: 'Route Id',              
            ticked: false,
            dynamicDataFlg: 'routeId',
            value: 3,
            category: 'Boarding Details'
        },
        {   
            name: 'Assign Date',              
            ticked: false,
            dynamicDataFlg: 'assignDate',
            value: 3,
            category: 'Boarding Details'
        },
      
        { 
            name: 'Route Close Time',  
            ticked: false,
            dynamicDataFlg: 'routeCloseTime',
            value: 4,
            category: 'Boarding Details'
        },
        { 
           
            name: 'Shift Time',            
            ticked: false,
            dynamicDataFlg: 'shiftTime',
            value: 5,
            category: 'Boarding Details'
        },
        { 
            name: 'Trip Type',             
            ticked: false,
            dynamicDataFlg: 'tripType',
            value: 6,
            category: 'Boarding Details'
        },
        { 
            name: 'Trip Start Time',             
            ticked: false,
            dynamicDataFlg: 'tripStartTime',
            value: 7,
            category: 'Boarding Details' 
        },
         { 
            name: 'Trip End Time',             
            ticked: false,
            dynamicDataFlg: 'tripEndTime',
            value: 8,
            category: 'Boarding Details' 
        },
        { 
            name: 'Route Name',             
            ticked: false,
            dynamicDataFlg: 'routeName',
            value: 9,
            category: 'Boarding Details'
        },
        { 
            name: 'Cab Reached Time',             
            ticked: false,
            dynamicDataFlg: 'cabReachedTime',
            value: 10,
            category: 'Boarding Details'
        },
        { 
            name: 'Boarding Status',             
            ticked: false,
            dynamicDataFlg: 'boardingStatus',
            value: 11,
            category: 'Boarding Details'
        },
        { 
            name: 'Boarding Time',             
            ticked: false,
            dynamicDataFlg: 'boardingTime',
            value: 12,
            category: 'Boarding Details'
        },

        { 
            name: 'Host Mobile Number',             
            ticked: false,
            dynamicDataFlg: 'hostMobileNumber',
            value: 12,
            category: 'Boarding Details'
        },
        { 
            name: 'Pickup Time',             
            ticked: false,  
            dynamicDataFlg: 'pickupTime',
            value: 13,
            category: 'Boarding Details'
        },
        { 
            name: 'Planned Distance',             
            ticked: false,  
            dynamicDataFlg: 'plannedDistance',
            value: 13,
            category: 'Boarding Details'
        },
        { 
            name: 'GPS',             
            ticked: false,  
            dynamicDataFlg: 'gps',
            value: 13,
            category: 'Boarding Details'
        },
        {
            msGroup: false
        },
        {
            name: '<strong>Driver Details - Click Here To Select All</strong>',
            msGroup: true,
            value: 14
        },
        { 
            name: 'Driver Id', 
            ticked: false,
            dynamicDataFlg: 'driverId',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Vendor Name', 
            ticked: false,
            dynamicDataFlg: 'vendorName',
            value: 16,
            category: 'Driver Details' 
        }
        ,
        { 
            name: 'Total Working Hours', 
            ticked: false,
            dynamicDataFlg: 'totalWorkingHours',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Driver Driving Hours Per Trip', 
            ticked: false,
            dynamicDataFlg: 'driverDrivingHours',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Total Driving Hours', 
            ticked: false,
            dynamicDataFlg: 'totalDrivingHours',
            value: 16,
            category: 'Driver Details' 
        }
        ,
        { 
            name: 'CheckIn Remarks', 
            ticked: false,
            dynamicDataFlg: 'remarks',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Driver Name', 
            ticked: false,
            dynamicDataFlg: 'driverName',
            value: 16,
            category: 'Driver Details' 
        },
        {
            name: 'Driver Mobile Number', 
            ticked: false,
            dynamicDataFlg: 'driverMobileNumber',
            value: 17,
            category: 'Driver Details' 
        }
        ,
        { 
            name: 'Driver Address', 
            ticked: false,
            dynamicDataFlg: 'driverAddress',
            value: 18,
            category: 'Driver Details'
        },
        {
            name: 'CheckIn Time', 
            ticked: false,
            dynamicDataFlg: 'checkInTime',
            value: 19,
            category: 'Driver Details'
        },
        { 
            name: 'CheckOut Time', 
            ticked: false,
            dynamicDataFlg: 'checkOutTime',
            value: 20,
            category: 'Driver Details' 
        },
        {
            name: 'Travel Time', 
            ticked: false,
            dynamicDataFlg: 'travelTime',
            value: 21,
            category: 'Driver Details'
        },
        { 
            name: 'Working Hours', 
            ticked: false,
            dynamicDataFlg: 'workingHours',
            value: 22,
            category: 'Driver Details'  
        },
        {
            name: 'CheckIn Vehicle Number', 
            ticked: false,
            dynamicDataFlg: 'checkInVehicleNumber',
            value: 23,
            category: 'Driver Details'
        },
        {
            msGroup: false
        },
        {
            name: '<strong>Message Details - Click Here To Select All</strong>',
            msGroup: true,
            dynamicDataFlg: 'assignDate',
            value: 24,
            category: 'Message Details'
        },
        {
            name: 'Allocation Message', 
            ticked: false,
            dynamicDataFlg: 'alloccationMessage',
            value: 25,
            category: 'Message Details'
        },
        {
            name: 'Cab Delay Message', 
            ticked: false,
            dynamicDataFlg: 'cabDelayMessage',
            value: 26,
            category: 'Message Details'
        },
        {
            name: 'Reached Message', 
            ticked: false,
            dynamicDataFlg: 'reachedMessage',
            value: 27,
            category: 'Message Details'
        },
        {
            name: '15 Min Message', 
            ticked: false,
            dynamicDataFlg: 'fifteenMinuteMsg',
            value: 28,
            category: 'Message Details'
        },
        {
            name: 'No Show Message', 
            ticked: false,
            dynamicDataFlg: 'noShowMessage',
            value: 29,
            category: 'Message Details'
        },
        {
            msGroup: false,
        },
        {
            name: '<strong>Vehicle Details - Click Here To Select All</strong>',
            msGroup: true,
            dynamicDataFlg: 'vehileDetails',
            value: 30
        },
        {
            name: 'Vehicle Number', 
            ticked: false,
            dynamicDataFlg: 'vehicleNumber',
            value: 31,
            category: 'Vehicle Details'
        },
        {
            msGroup: false
        },
        {
            name: '<strong>Escort Details - Click Here To Select All</strong>',
            msGroup: true,
            dynamicDataFlg: 'escortDetails',
            value: 32
        },
        {
            name: 'Escort Id', 
            ticked: false,
            dynamicDataFlg: 'escortId',
            value: 33,
            category: 'Escort Details'
        },
        {
            name: 'Escort Name', 
            ticked: false,
            dynamicDataFlg: 'escortName',
            value: 34,
            category: 'Escort Details'
        },
        {
            name: 'Escort Mobile No', 
            ticked: false,
            dynamicDataFlg: 'escortMobileNo',
            value: 35,
            category: 'Escort Details'
        },
        {
            name: 'Escort Address', 
            ticked: false,
            dynamicDataFlg: 'escotrtAddress',
            value: 36,
            category: 'Escort Details'
        },
        {
            msGroup: false
        },
        {
            name: '<strong>Employee Details - Click Here To Select All</strong>',
            msGroup: true,
            dynamicDataFlg: 'employeeDetails',
            value: 37,
            category: 'Employee Details'
        },
        {
            name: 'Employee Id', 
            ticked: false,
            dynamicDataFlg: 'employeeId',
            value: 1,
            category: 'Employee Details'
        },
        {
            name: 'Employee Mobile No', 
            ticked: false,
            dynamicDataFlg: 'employeeMobileNo',
            value: 38,
            category: 'Employee Details'
        },
        {
            name: 'Employee Name', 
            ticked: false,
            dynamicDataFlg: 'employeeName',
            value: 39,
            category: 'Employee Details'
        },
        {
            name: 'Host Mobile Number', 
            ticked: false,
            dynamicDataFlg: 'hostMobileNumber',
            value: 39,
            category: 'Employee Details'
        },
        {
            name: 'Employee Email Id', 
            ticked: false,
            dynamicDataFlg: 'employeeEmailId',
            value: 39,
            category: 'Employee Details'
        },
        {
            name: 'Mobile Number', 
            ticked: false,
            dynamicDataFlg: 'empMobileNumber',
            value: 39,
            category: 'Employee Details'
        },
        {
            name: 'Employee Location', 
            ticked: false,
            dynamicDataFlg: 'employeeLocation',
            value: 39,
            category: 'Employee Details'
        },
        {
            msGroup: false
        }
    ];
    }

$scope.webBrowsersDriver = [];

  $scope.onloadDropdownChange = function()
  {
    if($scope.selectedDynamicType == "driverDetails")
    {
      $scope.webBrowsersDriver = [
        
        {
            name: '<strong>Driver Details - Click Here To Select All</strong>',
            msGroup: true,
            value: 14
        },
        {   
            name: 'Route Id',              
            ticked: false,
            dynamicDataFlg: 'routeId',
            value: 3,
            category: 'Boarding Details'
        },
        { 
            name: 'Driver Id', 
            ticked: false,
            dynamicDataFlg: 'driverId',
            value: 16,
            category: 'Driver Details' 
        },
        {   
            name: 'Assign Date',              
            ticked: false,
            dynamicDataFlg: 'assignDate',
            value: 3,
            category: 'Driver Details'
        },
        { 
            name: 'Trip Start Time',             
            ticked: false,
            dynamicDataFlg: 'tripStartTime',
            value: 7,
            category: 'Driver Details' 
        },
         { 
            name: 'Trip End Time',             
            ticked: false,
            dynamicDataFlg: 'tripEndTime',
            value: 8,
            category: 'Driver Details' 
        },
        { 
            name: 'Trip Type',             
            ticked: false,
            dynamicDataFlg: 'tripType',
            value: 6,
            category: 'Driver Details'
        },
        { 
            name: 'Route Name',             
            ticked: false,
            dynamicDataFlg: 'routeName',
            value: 9,
            category: 'Driver Details'
        },
        { 
            name: 'Vendor Name', 
            ticked: false,
            dynamicDataFlg: 'vendorName',
            value: 16,
            category: 'Driver Details' 
        }
        ,
        { 
            name: 'Total Working Hours', 
            ticked: false,
            dynamicDataFlg: 'totalWorkingHours',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Driver Driving Hours Per Trip', 
            ticked: false,
            dynamicDataFlg: 'driverDrivingHoursPerTrips',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Total Driving Hours', 
            ticked: false,
            dynamicDataFlg: 'totalDrivingHours',
            value: 16,
            category: 'Driver Details' 
        }
        ,
        { 
            name: 'CheckIn Remarks', 
            ticked: false,
            dynamicDataFlg: 'remarks',
            value: 16,
            category: 'Driver Details' 
        },
        { 
            name: 'Driver Name', 
            ticked: false,
            dynamicDataFlg: 'driverName',
            value: 16,
            category: 'Driver Details' 
        },
        {
            name: 'Driver Mobile Number', 
            ticked: false,
            dynamicDataFlg: 'driverMobileNumber',
            value: 17,
            category: 'Driver Details' 
        }
        ,
        { 
            name: 'Driver Address', 
            ticked: false,
            dynamicDataFlg: 'driverAddress',
            value: 18,
            category: 'Driver Details'
        },
        {
            name: 'CheckIn Time', 
            ticked: false,
            dynamicDataFlg: 'checkInTime',
            value: 19,
            category: 'Driver Details'
        },
        { 
            name: 'CheckOut Time', 
            ticked: false,
            dynamicDataFlg: 'checkOutTime',
            value: 20,
            category: 'Driver Details' 
        },
        {
            name: 'Travel Time', 
            ticked: false,
            dynamicDataFlg: 'travelTime',
            value: 21,
            category: 'Driver Details'
        },
        { 
            name: 'Working Hours', 
            ticked: false,
            dynamicDataFlg: 'workingHours',
            value: 22,
            category: 'Driver Details'  
        },
        {
            name: 'CheckIn Vehicle Number', 
            ticked: false,
            dynamicDataFlg: 'checkInVehicleNumber',
            value: 23,
            category: 'Driver Details'
        },
        {
            msGroup: false
        },
        {
            name: '<strong>Vehicle Details - Click Here To Select All</strong>',
            msGroup: true,
            dynamicDataFlg: 'vehileDetails',
            value: 30
        },
        {
            name: 'Vehicle Number', 
            ticked: false,
            dynamicDataFlg: 'vehicleNumber',
            value: 31,
            category: 'Vehicle Details'
        },
        {
            msGroup: false
        }
    ];
    }

  }
  
$scope.saveInExcelDynamicReport = function(shiftTimeDate, tripType, shiftTimes) {

        var data = $rootScope.dynamicReportData;
        data.combinedFacility = combinedFacility;
        data.userId = profileId;
        $http({
          url : 'services/report/dynamicReportDownload/',
          method : "POST",
          data : data,
          headers : {
            'Content-type' : 'application/json'
          },
          responseType : 'arraybuffer'
        }).success(function(data, status, headers, config) {
          var blob = new Blob([ data ], {});
          saveAs(blob, 'Dynamic Report_'+ convertDateUTC(new Date()) + '.xlsx');
        }).error(function(data, status, headers, config) {
          alert("Download Failed")
        });
      };

     $scope.dynamicDataLoad = function()
       {
         if($scope.selectedDynamicType == "driverDetails") 
          {
           var data = {           
            eFmFmClientBranchPO:{branchId:branchId},
            userId:profileId,
            combinedFacility:combinedFacility 
            };   
            $http.post('services/report/dynamicVendor/',data).
            success(function(data, status, headers, config) {
              if (data.status != "invalidRequest") {
                $scope.vendorListData = data; 
                var joinVendorData = $scope.vendorListData.concat($scope.webBrowsersDriver);
                $scope.webBrowsersGrouped = joinVendorData;
              }
            }).
            error(function(data, status, headers, config) {
            // log error
            }); 
          }
       }


    $scope.dynamicDataOnChange = function(dynamicDataValue,dynamicDataSelected)
    {
      $scope.dynamicDataSelected = dynamicDataSelected.value;
      $scope.vendorListCondition = dynamicDataValue.name;
      $scope.dynamicDataArray = [];
      $scope.dynamicDatalabelArray = [];
      $scope.vendorList = [];

      angular.forEach(dynamicDataValue, function(value, key) {
                  $scope.dynamicDataArray.push(value.dynamicDataFlg);
                  $scope.dynamicDatalabelArray.push(value);
      });

      if(dynamicDataSelected.value == 'driverDetails')
      {
        angular.forEach(dynamicDataValue, function(value, key) {
          
              if(value.vendorId == undefined || value.vendorId == null)
              {
              }
              else
              {
                $scope.vendorList.push(value.vendorId);
              }
        });
        $scope.vendorListDatas = $scope.vendorList.toString();
      }



      $scope.dataDynamicFlg = $scope.dynamicDataArray;
      $scope.dataDynamicFlgLength = $scope.dataDynamicFlg.length;
      

      $scope.dynamicDataShow();

      angular.forEach($scope.dataDynamicFlg, function(value, key) {

                  $scope.selectedCounts = value.length;

                  if(value == "assignDate")
                  {
                    $scope.assignDateFlg = 1;
                  }

                  if(value == "driverDrivingHours")
                  {
                    $scope.driverDrivingHoursFlg = 1;
                  }

                  if(value == "routeCloseTime")
                  {
                    $scope.routeCloseTimeFlg = 1;
                  }

                  if(value == "shiftTime")
                  {
                    $scope.shiftTimeFlg = 1;
                  }

                  if(value == "tripType")
                  {
                    $scope.tripTypeFlg = 1;
                  }

                  if(value == "tripStartTime")
                  {
                    $scope.tripStartTimeFlg = 1;
                  }

                  if(value == "tripEndTime")
                  {
                    $scope.tripEndTimeFlg = 1;
                  }

                  if(value == "routeName")
                  {
                    $scope.routeNameFlg = 1;
                  }

                  if(value == "cabReachedTime")
                  {
                    $scope.cabReachedTimeFlg = 1;
                  }
                  if(value == "boardingStatus")
                  {
                    $scope.boardingStatusFlg = 1;
                  }
                  if(value == "boardingTime")
                  {
                    $scope.boardingTimeFlg = 1;
                  }
                  if(value == "pickupTime")
                  {
                    $scope.pickupTimeFlg = 1;
                  }
                  if(value == "driverNumber")
                  {
                    $scope.driverNumberFlg = 1;
                  }
                  if(value == "driverName")
                  {
                    $scope.driverNameFlg = 1;
                  }
                  if(value == "driverMobileNumber")
                  {
                    $scope.driverMobileNumberFlg = 1;
                  }
                  if(value == "driverAddress")
                  {
                    $scope.driverAddressFlg = 1;
                  }
                  if(value == "checkInTime")
                  {
                    $scope.checkInTimeFlg = 1;
                  }
                  if(value == "checkOutTime")
                  {
                    $scope.checkOutTimeFlg = 1;
                  }
                   if(value == "workingHours")
                  {
                    $scope.workingHoursFlg = 1;
                  }
                   if(value == "travelTime")
                  {
                    $scope.travelTimeFlg = 1;
                  }
                   if(value == "checkInVehicleNumber")
                  {
                    $scope.checkInVehicleNumberFlg = 1;
                  }
                  if(value == "assignDate")
                  {
                    $scope.assignDateFlg = 1;
                  }
                  if(value == "alloccationMessage")
                  {
                    $scope.alloccationMessageFlg = 1;
                  }
                  if(value == "cabDelayMessage")
                  {
                    $scope.cabDelayMessageFlg = 1;
                  }
                  if(value == "reachedMessage")
                  {
                    $scope.reachedMessageFlg = 1;
                  }
                  if(value == "fifteenMinuteMsg")
                  {
                    $scope.fifteenMinuteMsgFlg = 1;
                  }
                  if(value == "noShowMessage")
                  {
                    $scope.noShowMessageFlg = 1;
                  }
                   if(value == "vehileDetails")
                  {
                    $scope.vehileDetailsFlg = 1;
                  }
                   if(value == "vehicleNumber")
                  {
                    $scope.vehicleNumberFlg = 1;
                  }
                  if(value == "escortDetails")
                  {
                    $scope.escortDetailsFlg = 1;
                  }
                  if(value == "escortId")
                  {
                    $scope.escortIdFlg = 1;
                  }
                  if(value == "escortName")
                  {
                    $scope.escortNameFlg = 1;
                  }
                  if(value == "escortMobileNo")
                  {
                    $scope.escortMobileNoFlg = 1;
                  }
                  if(value == "escotrtAddress")
                  {
                    $scope.escortAddressFlg = 1;
                  }
                  if(value == "employeeDetails")
                  {
                    $scope.employeeDetailsFlg = 1;
                  }
                  if(value == "employeeId")
                  {
                    $scope.employeeIdFlg = 1;
                  }
                  if(value == "escortMobileNo")
                  {
                    $scope.escortMobileNoFlg = 1;
                  }
                  if(value == "employeeName")
                  {
                    $scope.employeeNameFlg = 1;
                  }
                  if(value == "plannedDistance")
                  {
                    $scope.plannedDistanceFlg = 1;
                  }
                  if(value == "gps")
                  {
                    $scope.gpsFlg = 1;
                  }
                  if(value == "vendorName")
                  {
                    $scope.vendorNameFlg = 1;
                  }
                  if(value == "driverId")
                  {
                    $scope.driverIdFlg = 1;
                  }
                  if(value == "totalWorkingHours")
                  {
                    $scope.totalWorkingHoursFlg = 1;
                  }
                  if(value == "totalDrivingHours")
                  {
                    $scope.totalDrivingHoursFlg = 1;
                  }
                  if(value == "remarks")
                  {
                    $scope.remarksFlg = 1;
                  }
                  if(value == "hostMobileNumber")
                  {
                    $scope.hostMobileNumberFlg = 1;
                  }
                  if(value == "noShowMessage")
                  {
                    $scope.noShowFlg = 1;
                  }
                  if(value == "employeeMobileNo")
                  {
                    $scope.employeeMobileNoFlg = 1;
                  }
                  if(value == "employeeEmailId")
                  {
                    $scope.emailIdFlg = 1;
                  }
                  if(value == "employeeLocation")
                  {
                    $scope.employeeLocationFlg = 1;
                  }
                  if(value == "routeId")
                  {
                    $scope.routeIdFlg = 1;
                  }
                  if(value == "driverDrivingHoursPerTrips")
                  {
                    $scope.driverDrivingHoursPerTripFlg = 1;
                  }
               });  

    }
       $scope.requestTypesDataTS = {
        availableOptions: [
          {value:"All", requestTypes: 'All'},
          {value:"'guest'", requestTypes: 'Guest'},         
          {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'}
        ],
        selectedOption: {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'} 
      };

      $scope.viewTypeTripsheet = {
        availableOptions: [
          {value:"viewData", viewType: 'View Data'},         
          {value:"excelDownload", viewType: 'Excel Download'}
        ],
        selectedOption: {value:"viewData", viewType: 'View Data'}
      };
      $scope.viewTypesOntime = {
        availableOptions: [
          {value:"viewData", viewType: 'View Data'},         
          {value:"excelDownload", viewType: 'Excel Download'}
        ],
        selectedOption: {value:"viewData", viewType: 'View Data'}
      };
      $scope.requestTypesDataOT = {
        availableOptions: [
          {value:"'guest'", requestTypes: 'Guest'},
          {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'}
        ],
        selectedOption: {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'} 
      };

      $scope.requestTypesDataSU = {
        availableOptions: [
          {value:"'guest'", requestTypes: 'Guest'},
          {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'}
        ],
        selectedOption: {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'} 
      };

      $scope.requestTypesDataNS = {
        availableOptions: [
          {value:"'guest'", requestTypes: 'Guest'},
          {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'}
        ],
        selectedOption: {value:"'normal','adhoc','nodalAdhoc','nodal','AdhocRequest'", requestTypes: 'Employee'} 
      };


        //Convert to hh:mm
      var convertToTime = function(newdate) {
        d = new Date(newdate);
        hr = d.getHours();
        min = d.getMinutes();
        if (hr < 10) {
          hr = '0' + hr;
        }
        if (min < 10) {
          min = '0' + min;
        }
        return hr + ":" + min;
      };


     $scope.editDrivingHour = function(updateTime, dh, index) {
          dh.editDrivingHourIsClick = true;
          $scope.driverDrivingHoursTrip = updateTime;
          $scope.timeConverstionDrivingHour = [];
          $scope.timeConverstionDrivingHour = $scope.driverDrivingHoursTrip.split(':');

          var todaydate = new Date();
          todaydate.setHours($scope.timeConverstionDrivingHour[0], $scope.timeConverstionDrivingHour[1]);
          $scope.driverDrivingHoursTrip = todaydate;
          $scope.driverDrivingHoursPerTrip = [];
          $scope.driverDrivingHoursPerTrip.push($scope.driverDrivingHoursPerTrip);
      };
      $scope.updateDrivingHour = function(dh, driverDrivingHoursPerTrip, index){
        dh.editDrivingHourIsClick = false;
      
        var data = {
          eFmFmClientBranchPO : {branchId : branchId
          },
          time : convertToTime(driverDrivingHoursPerTrip),
          assignRouteId: dh.assignRouteId,
          userId:profileId,
          combinedFacility:combinedFacility 
        };        
        $http.post('services/report/updateDriverDrivingHours/', data).success(
            function(data, status, headers, config) {
              if (data.status != "invalidRequest") {
              ngDialog.open({
                template : 'Driver Driving Hours Updated Successfully',
                plain : true
              });
              
              dh.driverDrivingHoursPerTrip = convertToTime(driverDrivingHoursPerTrip);
            }
            }).error(function(data, status, headers, config) {
          // log error

        });

      }

      $scope.cancelDrivingHour = function(dh, index) {
        dh.editDrivingHourIsClick = false;
      };
      $scope.editDrivingHours = function(updateTime, report, index) {
          report.editDrivingHourIsClicked = true;
          $scope.driverDrivingHoursPerTrip = updateTime;
          $scope.timeConverstionDrivingHour = [];
          $scope.timeConverstionDrivingHour = $scope.driverDrivingHoursPerTrip.split(':');

          var todaydate = new Date();
          todaydate.setHours($scope.timeConverstionDrivingHour[0], $scope.timeConverstionDrivingHour[1]);
          $scope.driverDrivingHoursPerTrip = todaydate;
          $scope.driverDrivingHoursPerTrips = [];
          $scope.driverDrivingHoursPerTrips.push($scope.driverDrivingHoursPerTrip);
      };

      $scope.updateDrivingHours = function(report, index, driverDrivingHoursPerTrips){

        report.editDrivingHourIsClicked = false;
      
        var data = {
          eFmFmClientBranchPO : {branchId : branchId
          },
          time : convertToTime(driverDrivingHoursPerTrips),
          userId:profileId ,
          assignRouteId:report.tripsDetails[0].assignRouteId,
          combinedFacility:combinedFacility
        };        
        $http.post('services/report/updateDriverDrivingHours/', data).success(
            function(data, status, headers, config) {
              if (data.status != "invalidRequest") {
              ngDialog.open({
                template : 'Driver Driving Hours Updated Successfully',
                plain : true
              });
              
              report.tripsDetails[0].driverDrivingHoursPerTrip = convertToTime(driverDrivingHoursPerTrips);
            }
            }).error(function(data, status, headers, config) {
          // log error

        });

      }

      $scope.cancelDrivingHours = function(report, index) {
        report.editDrivingHourIsClicked = false;
      };


      $scope.viewOdometerTripSheet = function(trip)
      {
        var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/viewOdometerTripSheet.jsp',
           controller: 'viewOdometerTripSheetCtrl',
           resolve: {
               trip: function(){return trip;},
           }
           }); 

         modalInstance.result.then(function(result){
              trip.odoDistance = String(result.distanceOdomter);
              trip.startKm = result.odometerStartKm;
              trip.endKm = result.odometerEndKm;
         });
      }
      

       // Ascending , Decending Table Filter
 
        $scope.dateAscDsc = 'tripDates'; //On Time, Seat Utilization
        $scope.dateAscDsc = 'date'; // Distance, Driver Working Hours, Driver Driving Hours tab
        $scope.dateAscDsc = 'travelledDate'; // Daily SMS
        $scope.dateAscDsc = 'tripAssignDate'; // Escort
        $scope.dateAscDsc = 'dateTime';  // Speed
        $scope.dateAscDsc = 'assignDate';  // Employee Details
        $scope.dateAscDsc = 'tripStartDate';  // Driver Driving Hours
        $scope.dateAscDsc = 'tripAssignedDate';
        $scope.dateAscDsc = 'tripsDetails[0].tripAssignedDate';
        
        $scope.reverse = true;
 
        $scope.order = function(dateAscDsc) 
        {
          $scope.reverse = ($scope.dateAscDsc === dateAscDsc) ? !$scope.reverse : false;
          $scope.dateAscDsc = dateAscDsc;
        };
 

       $scope.setLimit = function(showRecords){
         if(!showRecords){
                 $scope.numberofRecords = tripDataLength;
            }
             else $scope.numberofRecords = showRecords.value;    
      };
       
       $scope.dateOptions = {formatYear: 'yy',
                              startingDay: 1,
                              showWeeks: false
                            }; 
       
        $scope.formats = ['yyyy','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate','dd-mm-yyyy', 'mm-yyyy'];
        $scope.monthformat = 'MMMM' + ' - ' + 'yyyy';

        $scope.datepickerOptions = {datepickerMode:"'month'",
                                    minMode:"month",
                                    minDate:"minDate",
                                    showWeeks:"false"};
       
        $scope.selectMonthCal = function($event){
            $event.preventDefault();
            $event.stopPropagation();
            $timeout( function(){
               $scope.datePicker = {'selectMonth' : true};  
             }, 50);
        };

        $scope.createOdometer = function()
        {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/createOdometerKm.jsp',
           controller: 'createOdometerReportCtrl',
           backdrop:'static'
           /*resolve: {
               data: function(){return data;},
           }*/
           }); 
        }
       
      $scope.employeeIdnull = function(data){
        if (!data) {
         ngDialog.open({
                    template: 'Enter Employee ID First',
                    plain: true
                });
             }
        };

      $scope.projectDetailnull = function(project){
        if (project) {
            $scope.searchTR = project[0];
             }else{
              $scope.searchTR = null;
             }
        };

      $scope.dynamicCategorynull = function(data)
      {
        if (!data) {
         ngDialog.open({
                    template: 'Kindly select category type',
                    plain: true
                });
             }
      }

       //Date Picker for the 'From Date'
       $scope.fromDateCal = function($event){
             $event.preventDefault();
            $event.stopPropagation();
            $timeout( function(){
               $scope.datePicker = {'fromDate' : true};  
             }, 50);
         };
       
       //Date Picker for the 'To Date'
       $scope.toDateCal = function($event){
             $event.preventDefault();
            $event.stopPropagation();
            $timeout( function(){
               $scope.datePicker = {'toDate' : true};  
             }, 50);
         };
       
       //Set the Label of the Date Selection Button
       $scope.setDates = function(tabClicked){
           $scope.isResult = false;
           $scope.currentTab  = tabClicked;   
           if($scope.currentTab == 'centerBase'){
               $scope.thisMonth = new Date();               
           }
           else{ 
               if($scope.currentTab == 'onTime'){
                   if($scope.OTShiftTimes.length == 0 && $scope.OTAllVendors.length == 0 && $scope.OTTripTypes.length == 0){
                       $scope.OTShiftTimes = $scope.getShiftTime('PICKUP');   
                       $scope.OTAllVendors = $scope.getAllVendors();
                       $scope.OTTripTypes = $scope.tripTypes;
                       $scope.searchOT.OTShift = {'text':'All Shifts', 'value':0};
                       $scope.searchOT.OTVendors = {'name':'All Vendors', 'Id':0};
                       $scope.searchOT.type = {'value':'PICKUP', 'text':'PICKUP'};
                   }         
               }
               if($scope.currentTab == 'seatUtil'){
                   if($scope.SUShiftTimes.length == 0 && $scope.SUAllVendors.length == 0 && $scope.SUTripTypes.length == 0){
                       $scope.SUShiftTimes = $scope.getShiftTime('PICKUP');   
                       $scope.SUAllVendors = $scope.getAllVendors();
                       $scope.SUTripTypes = $scope.tripTypes;
                       $scope.searchSU.SUShift = {'text':'All Shifts', 'value':0};
                       $scope.searchSU.SUVendors = {'name':'All Vendors', 'Id':0};
                       $scope.searchSU.type = {'value':'PICKUP', 'text':'PICKUP'};
                       $scope.searchSU.SUOption = {text:'By Shift', value:'By Shift'};
                   } 
               }
               if($scope.currentTab == 'noShow'){
                       $scope.NSShiftTimes = $scope.getShiftTime('PICKUP');
                       $scope.NSTripTypes = $scope.tripTypes;
                       $scope.searchNS.NSShift = {'text':'All Shifts', 'value':0};
                   
                       $scope.searchNS.type = {'value':'PICKUP', 'text':'PICKUP'};
                       $scope.searchNS.NSOption = {text:'By Count', value:'By Count'};
                       $scope.searchNS.NSCount = {text:'1', value:'1'};
               }
               if($scope.currentTab == 'kmReport'){
                   $scope.DistanceShiftTimes = [];
                   $scope.DistanceShiftTimes.push({'text':'All Shifts', 'value':0});
                   $scope.DistanceShiftTimes.push({'text':'By Shifts', 'value':1});
                   
                   $scope.reportKM.distanceShift = {'text':'By Shifts', 'value':1};
                   $scope.reportKM.reportType= {'value':"vendor", 'text':'By VENDOR'};
                   $scope.getVendorOrVehicle($scope.reportKM.reportType);
               }
               if($scope.currentTab == 'speed'){    
                   $scope.vendorVehicles_SpeedReport = [];
                   $scope.SpeedAllVendors = $scope.getAllVendors();
                   $scope.searchSpeed.SpeedVendors = {'name':'All Vendors', 'Id':0};
                   $scope.vendorVehicles_SpeedReport.push({'name':'All Vehicles', 'Id':0});
                   $scope.searchSpeed.VSelection = $scope.vendorVehicles_SpeedReport[0];
               }
               if($scope.currentTab == 'routeWiseTravel'){                   
                   $scope.RWTTripTypes = $scope.tripTypes;
                   $scope.searchRWT.type = {'value':'PICKUP', 'text':'PICKUP'};
               }
               if($scope.currentTab == 'employeeDetails'){   
                   
               }
               if($scope.currentTab == 'employeeRequestDetails'){

               }
               if($scope.currentTab == 'dynamicDetails'){

               }
               if($scope.currentTab == 'employeeFemaleDetails'){

               }
                 if($scope.currentTab == 'travelReport'){

               }
               //Set all the items from the Dates Drop Downs to false
               angular.forEach($scope.dates, function(item){
                            item.isClicked = false;});
               //Set today's, tomorrow's date
               var todaysDate = new Date();
               var tomorrowDate = new Date();
               tomorrowDate.setDate(tomorrowDate.getDate() + 1);
               $scope.dates[0].value = todaysDate;
               $scope.fromDate = todaysDate;
               $scope.toDate = tomorrowDate;
           }
       };
       $scope.setDates($scope.currentTab);
       function init() {
    $scope.activeTabs = [false, false, false];
    var lastTab = $window.sessionStorage.getItem('activeTab');
    
    if (lastTab) {
      $scope.activeTabs[lastTab] = true;
        switch (lastTab) {
            case '0':
                $scope.currentTab = 'tripSheet';
                $scope.setDates("tripSheet");                
                break;
            case '1':
                $scope.currentTab = 'onTime';
                $scope.setDates("onTime");                
                break;
            case '2':
                $scope.currentTab = 'seatUtil';
                $scope.setDates("seatUtil");                
                break;
            case '3':
                $scope.currentTab = 'noShow';
                $scope.setDates("noShow");                
                break;
            case '4':
                $scope.currentTab = 'kmReport';
                $scope.setDates("kmReport");                
                break;
            case '5':
                $scope.currentTab = 'SMSReport';
                $scope.setDates("SMSReport");                
                break;
            case '6':
                $scope.currentTab = 'escortReport';
                $scope.setDates("escortReport");                
                break;
            case '7':
                $scope.currentTab = 'vehicleDriverAttendence';
                $scope.setDates("vehicleDriverAttendence");                
                break;
            case '8':
                $scope.currentTab = 'driverWorkingHours';
                $scope.setDates("driverWorkingHours");                
                break;
            case '9':
                $scope.currentTab = 'driverDrivingHours';
                $scope.setDates("driverDrivingHours");                
                break;
            case '10':
                $scope.currentTab = 'speed';
                $scope.setDates("speed");                
                break;
            case '11':
                $scope.currentTab = 'routeWiseTravel';
                $scope.setDates("routeWiseTravel");                
                break;
            case '12':
                $scope.currentTab = 'employeeDetails';
                $scope.setDates("employeeDetails");                
                break;
            case '13':
                $scope.currentTab = 'employeeRequestDetails';
                $scope.setDates("employeeRequestDetails");                
                break;
            case '14':
                $scope.currentTab = 'dynamicDetails';
                $scope.setDates("dynamicDetails");                
                break;
            case '15':
              $scope.currentTab = 'employeeFemaleDetails';
                $scope.setDates("employeeFemaleDetails");
                break;
	          case '16':
              	$scope.currentTab = 'feedbacks';
                $scope.setDates("feedbacks");
                break;
            case '17':
              $scope.currentTab = 'travelReport';
                $scope.setDates("travelReport");
                break;
            case '18':
                $scope.currentTab = 'geocodedDistanceVariation';
                $scope.setDates("geocodedDistanceVariation");
                break;

            default:

        }
    } else {
      $scope.activeTabs[0] = true;
    }
  }

        $scope.onSelectTab = function(currentTab) {

          for (var i = 0; i < $scope.activeTabs.length; i++) {

            $scope.activeTabs[i] = false;
            if (i == currentTab) {
              $scope.activeTabs[i] = true;
            }
          }

        }


            $scope.fetchingGeocodedDistanceVariation= false;
            $scope.getGeocodedDistanceVariations = [];
            $scope.getGeocodedDistanceVariation = function(tabDetails) {
                if ($scope.fetchingGeocodedDistanceVariation) return;
                if ($scope.getGeocodedDistanceVariationsLength == undefined || $scope.getGeocodedDistanceVariationsLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.getGeocodedDistanceVariationsLength;
                    var endPgNoCount = webPageCount;
                }

                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.getYetToAppCountDataShow = false;
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId, 
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingGeocodedDistanceVariation) {
                    $scope.fetchingGeocodedDistanceVariation = true;
                    $http.post('services/report/getGeocodedDistanceVariation/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingGeocodedDistanceVariation = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.getGeocodedDistanceVariations.push(item);
                                });
                                console.log("$scope.getGeocodedDistanceVariations",$scope.getGeocodedDistanceVariations);
                                $rootScope.getGeocodedDistanceVariations = $scope.getYetToAppCountData;
                                $scope.getGeocodedDistanceVariationsLength = $scope.getGeocodedDistanceVariations.length;
                                $scope.getYetToAppCountDataShow = true;
                            } else {
                                setTimeout(function() {
                                    $scope.fetchingGeocodedDistanceVariation = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {

                        });
                } else {
                    return;
                }
            };

        $scope.searchTypes = [{
                text: "By Emp Id",
                value: "empId"
            }, {
                text: "By Email Id",
                value: "email"
            }, {
                text: "By Mobile Number ",
                value: "mobile"
            }];
             $scope.setSearchPlcaeholer = "Select Search Type";
             $scope.setSearchType = function(searchType) {
                if (searchType) {
                    if (searchType.value == "empId") {
                        $scope.setSearchPlcaeholer = "Enter Employee Id";
                    } else if (searchType.value == "mobile") {
                        $scope.setSearchPlcaeholer = "Enter Mobile Number"
                    } else {
                        $scope.setSearchPlcaeholer = "Enter Email Id";
                    }
                } else{
                        $scope.setSearchPlcaeholer = "Select Search Type";
                        $scope.search.searchType = null;
                        $scope.search.text=null;
                }
            };

            $scope.search = {};
            $scope.searchEmployees = function(search, combinedFacilityId) {
                $scope.getGeocodedDistanceVariations = [];
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    employeeId: search.text,
                    userId: profileId,
                    searchType: search.searchType.value,
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/employee/empSearchByIdMobOrEmail/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                            $scope.getGeocodedDistanceVariations = data;
                  
                    }
                }).
                error(function(data, status, headers, config) {});
            };

             $scope.geocodedDistanceVariationsExcel = function(data){
              console.log("data",data);
               $scope.getGeocodedDistanceVariationData = [];
               angular.forEach(data, function(item){
                   $scope.getGeocodedDistanceVariationData.push({
                                              'Employee Name':item.employeeName,
                                              'Mobile Number':item.mobileNumber, 
                                              'Employee Id':item.employeeId,
                                              'Email Id':item.emailId,
                                              'User Name':item.userName,
                                              'User Id':item.userId,
                                              'Employee Address':item.employeeAddress,
                                              'Distance Variation':item.distanceVariation,
                                              'Latitude/Longitude':item.employeeLatiLongi,
                                              'Geo Coded Address':item.geoCodedAddress,
                                              'Home GeoCode Points':item.homeGeoCodePoints,
                                            });
               }); 
               var sheetLabel = 'Geo Coded Distance Variations Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("Geocoded Distance Variations Excel.xlsx",?) FROM ?',[opts,[$scope.getGeocodedDistanceVariationData]]);   
             };


        $scope.select = function(tab) {
          $scope.onSelectTab(tab);
          $window.sessionStorage.setItem('activeTab', tab);
        };
             
       
       //Convert the dates in DD-MM-YYYY format
       var convertDateUTC = function(date){
          var convert_date = new Date(date);
          var currentMonth = convert_date.getMonth()+1;
          var currentDate=convert_date.getDate();
              if(currentDate<10){
                if (currentDate < 10) { 
                  currentDate = '0' + currentDate;
                  }  
              }
              if (currentMonth < 10) { 
                  currentMonth = '0' + currentMonth; 
              }
          return currentDate+'-'+currentMonth+'-'+convert_date.getFullYear();   
        };
       
       //This Function is called on the Click of the ITEMS in the Date Popover
       $scope.updateDate = function(type,combinedFacilityId){           
           switch(type) {
                case 'today':
                    $scope.openCustomRange = false;
                    $scope.getReport(new Date(), new Date(),combinedFacilityId);                   
                    angular.forEach($scope.dates, function(item){
                        item.isClicked = false;
                    });
                    $scope.dates[0].isClicked = true;
                    break;
                case 'yesturday':
                   $scope.openCustomRange = false;
                   var yesturdayDate = new Date();
                   yesturdayDate.setDate(yesturdayDate.getDate()-1);
                   $scope.getReport(yesturdayDate, yesturdayDate,combinedFacilityId);
                   angular.forEach($scope.dates, function(item){
                        item.isClicked = false;
                    });
                    $scope.dates[1].isClicked = true;
                    break;
                case 'last7':
                   $scope.openCustomRange = false;
                   var last7 = new Date();
                   last7.setDate(last7.getDate()-7);
                   $scope.getReport(last7, new Date(),combinedFacilityId);                   
                   angular.forEach($scope.dates, function(item){
                        item.isClicked = false;
                    });
                    $scope.dates[2].isClicked = true;
                    break;
                case 'last30':
                    $scope.openCustomRange = false;
                   var last30 = new Date();
                   last30.setDate(last30.getDate()-30);
                   $scope.getReport(last30, new Date(),combinedFacilityId);                   
                   angular.forEach($scope.dates, function(item){
                        item.isClicked = false;
                    });
                    $scope.dates[3].isClicked = true;
                    break;
                case 'thisMonth':
                    $scope.openCustomRange = false;
                    var thisMonth = new Date();
                    thisMonth.setMonth(thisMonth.getMonth(), 1);
                    $scope.dates[4].value = thisMonth;
                    $scope.getReport(thisMonth, new Date(),combinedFacilityId);                   
                    angular.forEach($scope.dates, function(item){
                        item.isClicked = false;
                    });
                    $scope.dates[4].isClicked = true;
                    break;
                case 'customDate':
                   if($scope.openCustomRange){
                       $scope.openCustomRange = false;
                       $scope.from = '';
                       $scope.to = '';
                   }
                   else{
                       $scope.openCustomRange = true;    
                      }
                   angular.forEach($scope.dates, function(item){
                        item.isClicked = false;
                    });
                   break;
            }
        };
        $scope.setProjectNameChangeTravel = function(value,combinedFacilityId){
            if(combinedFacilityId == undefined || combinedFacilityId.length == 0){
              combinedFacilityId = combinedFacility;
            }else{
              combinedFacilityId = String(combinedFacilityId);
            }

            if(value == 'projectName'){
                      var data = {
                           eFmFmClientBranchPO:{branchId:branchId},
                           userId:profileId,
                           combinedFacility:combinedFacilityId
                     };
                     $http.post('services/user/listOfProjectIdByClient',data).
                       success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                           angular.forEach(data, function(value, key) {
                             value.ticked = false;
                             value.name = value.projectName;
                          });
                          $scope.listOfProjectNameTR = data;
                        }
                       }).
                       error(function(data, status, headers, config) {
                         // log error
                    });
            }else if(value == 'projectId'){
                       var data = {
                           eFmFmClientBranchPO:{branchId:branchId},
                           userId:profileId,
                           combinedFacility:combinedFacilityId
                     };
                     $http.post('services/user/listOfProjectIdByClient',data).
                       success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                           angular.forEach(data, function(value, key) {
                             value.ticked = false;
                             value.name = value.ClientprojectId;
                          });
                          $scope.listOfProjectIdTR = data;
                        }
                       }).
                       error(function(data, status, headers, config) {
                         // log error
                    });
            }

        }
        $scope.setProjectNameChangeTravel("projectId");
        $scope.setProjectNameChangeTravel("projectName");

        $scope.sendSelectedUserDetailsTR = function(value,listOfProjectIdData){
        if(listOfProjectIdData == 'name'){
             value[0].name = value[0].ClientprojectId;
             $scope.listOfProjectIdTR = value;
        }else{
            value[0].name = value[0].projectName;
            $scope.listOfProjectNameTR = value;
        }

             var data = {
                 efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                      branchId: branchId
                    }},
                  userId:profileId,
                  eFmFmClientProjectDetails: {
                      projectId:value[0].projectId
                    },
                    combinedFacility:combinedFacility

                };
                $http.post('services/user/listReportingMngByProjectId/',data).
                success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                $scope.projectDetails = [];
                angular.forEach(data, function(value, key) {
                      value.name = value.repEmployeeId;
                      value.ticked = false;
                  })

                $scope.projectDetails = data;
                }
                }).
                error(function(data, status, headers, config) {
                // log error
                });
       }

       //Function to check the Date Validity
       $scope.checkDateRangeValidity = function(fromDate, toDate){
           if(fromDate > toDate){
               $scope.dateRangeError = true;
               $timeout(function() {$scope.dateRangeError = false; }, 3000);
               return false;
           }
           else return true;
       };
      
      //First Tab Function - Trip Sheet 
       $scope.getReport = function(fromDate, toDate,combinedFacilityId){ 
          if(combinedFacilityId == undefined || combinedFacilityId.length == 0){
            combinedFacilityId = combinedFacility;
          }else{
            combinedFacilityId = String(combinedFacilityId);
          }

          $scope.fromDate = fromDate;
          $scope.toDate = toDate;

           selectedFrom = fromDate;
           selectedTo = toDate;
           $scope.efmfilter = {};
           
           //Call the function to Check the Date range Validity before any other action
           if($scope.checkDateRangeValidity(new Date(fromDate).getTime(), new Date(toDate).getTime())) { 
             var data = {
         eFmFmClientBranchPO:{branchId:branchId},
         fromDate:convertDateUTC(fromDate),
         toDate:convertDateUTC(toDate)
       };

       // Excel Download

       $scope.tripSheetExcel = function()
       {
          requestType=$scope.requestTypesDataTS.selectedOption;
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          requestType:requestType.value,
                          userId:profileId,
                          combinedFacility: combinedFacilityId
                        };

       $http({
             url : 'services/report/tripSheetDownload/',          
             method: "POST",
             data: dataObj, 
             headers: {
                'Content-type': 'application/json'
             },
             responseType: 'arraybuffer'
         }).success(function (data, status, headers, config) {
             var blob = new Blob([data], {
             });
             saveAs(blob, 'TripSheet' + '.xlsx');
         }).error(function (data, status, headers, config) {
             alert("Download Failed")
         });
       }

       // On Time Excel Download

        $scope.onTimeExcel = function()
       {
          requestType=$scope.requestTypesDataOT.selectedOption;
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                         fromDate:convertDateUTC(fromDate),
                         toDate:convertDateUTC(toDate),
                         tripType:$scope.searchOT.type.value,
                         time:$scope.searchOT.OTShift.value,
                         vendorId:$scope.searchOT.OTVendors.Id,
                         requestType:requestType.value,
                         userId:profileId,
                         combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/ontimeArrivalDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               
               saveAs(blob, 'onTime' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }

       // Escort Excel Download

        $scope.escortExcel = function()
       {
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          userId:profileId,
                          combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/escortReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Escort' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }

      // Escort Excel Download  

        $scope.vehicleDriverAttendanceExcel = function()
       {
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          userId:profileId,
                          combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/attendenceReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Vehicle Driver Attendance' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }

       

       // Driver Working Hours Excel Download  

        $scope.driverWorkingHoursExcel = function()
       {
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          userId:profileId,
                          combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/driverWorkinHoursReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Driver Working Hours' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }
        
    // Driver Driving Hours Excel Download  

        $scope.driverDrivingHoursExcel = function()
       {
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          userId:profileId,
                          combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/driverDrivingHoursReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Driver Driving Hours' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }

      // Speed Excel Download  

        $scope.speedExcel = function()
       {
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          userId:profileId,
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          vendorId:$scope.searchSpeed.SpeedVendors.Id,
                          vehicleId:$scope.searchSpeed.VSelection.Id,
                          combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/speedReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Speed' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }


      // Route Wise Travel Time Excel Download  

        $scope.routeWiseTravelExcel = function()
       {
          var dataObj = {
                            eFmFmClientBranchPO:{branchId:branchId},
                            userId:profileId,
                            fromDate:convertDateUTC(fromDate),
                            toDate:convertDateUTC(toDate),
                            tripType:$scope.searchRWT.type.value,
                            combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/routeWiceReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Route Wise Travel Time' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }


      // On Time Modal View - OTA Trips (15 Minutes)

      $scope.onTimeOTATrips = function(OTA, $index, Triptype)
      {
           var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/onTimeTripsView.jsp',
           controller: 'onTimeReportsViewModalCtrl',
           size: 'lg',
           resolve: {
               OTA: function(){return OTA;},
               Triptype: function(){return Triptype.value;}
           }
           }); 
       
       modalInstance.result.then(function(result){});
      }

      // Delay Trips Modal View - Delay Trips

      $scope.onTimeDelayTrips = function(OTA, $index, Triptype)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/onTimeDelayTrips.jsp',
           controller: 'onTimeReportsViewModalCtrl',
           size: 'lg',
           resolve: {
               OTA: function(){return OTA;},
               Triptype: function(){return Triptype.value;}
           }
           }); 
      }

      // On Time Modal View - Beyond Login Time

      $scope.onTimeBeyondLoginTime = function(OTA, $index, Triptype)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/onTimeSummaryBeyondTime.jsp',
           controller: 'onTimeReportsViewModalCtrl',
           size: 'lg',
           resolve: {
               OTA: function(){return OTA;},
               Triptype: function(){return Triptype.value;}
           }
           }); 
      }

      // On Time Modal View - No Show

      $scope.onTimeNoShow = function(OTA, $index, Triptype)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/onTimeNoShow.jsp',
           controller: 'onTimeReportsViewModalCtrl',
           size: 'lg',
           resolve: {
               OTA: function(){return OTA;},
               Triptype: function(){return Triptype.value;}
           }
           }); 
      }

/****************************Seat Utilization Inner Table Row Modal******************************* */

      // Seat Utilization Modal View - No Show

      $scope.seatUtilNoOfVehicle = function(SU, $index)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/seatUtilNoOfVehicle.jsp',
           controller: 'seatUtilReportsViewModalCtrl',
           resolve: {
               SU: function(){return SU;}
           }
           }); 
      }


      // Seat Utilization Modal View - No Show

      $scope.seatUtilPickupPax = function(SU, $index)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/seatUtilPickupPax.jsp',
           controller: 'seatUtilReportsViewModalCtrl',
           resolve: {
               SU: function(){return SU;}
           }
           }); 
      }

/*********************************************************************************************** */

/*********************************** No Show Inner Table Row Modal ******************************* */

    // No Show Modal View - PICKUP Trips

      $scope.noShowPickupTrips = function(NS, $index)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/noShowPickupTrips.jsp',
           controller: 'noShowReportsViewModalCtrl',
           resolve: {
               NS: function(){return NS;}
           }
           }); 
      }

      // No Show Modal View - PICKUP Pax

      $scope.noShowPickupPax = function(NS, $index)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/noShowPickupPax.jsp',
           controller: 'noShowReportsViewModalCtrl',
           resolve: {
               NS: function(){return NS;}
           }
           }); 
      }

      // No Show Modal View 

      $scope.noShowView = function(NS, $index)
      {
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/noShowView.jsp',
           controller: 'noShowReportsViewModalCtrl',
           resolve: {
               NS: function(){return NS;}
           }
           }); 
      }


      // View Employee - Dynamic Report

      $scope.viewEmployeeDetails = function(routeId, $index, size)
      {
          var data = 
            {
              routeId : routeId,
              employeeId : $scope.employeeIdFlg,
              empName : $scope.employeeNameFlg,
              hostMobileNumber : $scope.hostMobileNumberFlg,
              empEmailId : $scope.emailIdFlg,
              mobileNumber : $scope.employeeMobileNoFlg,
              employeeLocation : $scope.employeeLocationFlg,
              fifteenMsg : $scope.fifteenMinuteMsgFlg,
              noShowMsg : $scope.noShowMessageFlg,
              reachedMsg : $scope.reachedMessageFlg,
              cabDelayMsg : $scope.cabDelayMessageFlg,
              cabReachedTime : $scope.cabReachedTimeFlg,
              boardingStatus : $scope.boardingStatusFlg,
              pickUpTime : $scope.pickupTimeFlg,
              boardingTime : $scope.boardingTimeFlg,
              alloccationMessage : $scope.alloccationMessageFlg
            };
         
          var modalInstance = $modal.open({
           templateUrl: 'partials/modals/reports/viewEmployeeDetails.jsp',
           controller: 'viewEmployeeDetailsCtrl',
           size:size,
           resolve: {
               data: function(){return data;},
           }
           }); 
      }




/*********************************************************************************************** */

      // Employee Wise Report Excel Download  

        $scope.employeeWiseReportExcel = function()
       {
          var dataObj = {
                               eFmFmClientBranchPO:{branchId:branchId},
                               userId:profileId,
                               fromDate:convertDateUTC(fromDate),
                               toDate:convertDateUTC(toDate) ,  
                               employeeId:$scope.searchED.employeeId,
                               combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/employeeWiseReportDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Employee Wise Report' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }

       $scope.travelReportExcel = function()
       {
             var data = {
                                        userId:profileId,
                                        eFmFmClientBranchPO:{branchId:branchId},
                                        fromDate: convertDateUTC(fromDate),
                                        toDate: convertDateUTC(toDate),
                                        combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/costReportDownload/',
               method: "POST",
               data: data,
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               saveAs(blob, 'Project Wise Travel Report' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
       }

            //If Trip Sheet Tab is Ckicked
            if($scope.currentTab == 'tripSheet'){
                if($scope.viewTypeTripSheet == 'viewData'){
                    requestType=$scope.requestTypesDataTS.selectedOption;
                    $scope.tripGetReportButtonClicked = true;
                    $scope.gotTripResult = false;
                    $('.popover').hide();
                    $scope.efmfilter= {filterTrips: ''};
                    
                     var data = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate) ,  
                          requestType:requestType.value,
                          userId:profileId,
                          combinedFacility: combinedFacilityId 
                        };
                     $http.post('services/report/tripSheet/',data).
                        success(function(data, status, headers, config) {
                          if (data.status != "invalidRequest") {
                           $scope.tripGetReportButtonClicked = false;
                           $scope.tripSheetData = data;
                           $scope.fromDate = fromDate;
                           $scope.toDate = toDate;
                           $scope.searchFromDateTS = fromDate;
                           $scope.searchToDateTS = toDate;
                          if($scope.tripSheetData.length>0){
                              $scope.gotTripResult = true;
                              angular.forEach($scope.tripSheetData, function(item){
                                  item.isTravelDistanceEdit = false;
                                  angular.forEach(item.tripDetail, function(item){
                                      item.orginalTravelledDistance = item.travelledDistance;
                                  });
                              });
                          }
                         else{
                             $scope.gotTripResult = false;
                             $scope.tripGetReportButtonClicked = false;
                             ngDialog.open({
                                        template: 'No Data Found. Please Change the Dates.',
                                        plain: true
                                    });
    //                         $scope.showalertMessage('No Data Found. Please Change the Dates', "");
                         }
                        }
                        }).
                        error(function(data, status, headers, config) {
                            $scope.tripGetReportButtonClicked = false;
                        });
                  }else{
                        requestType=$scope.requestTypesDataTS.selectedOption;
                        var dataObj = {
                                        eFmFmClientBranchPO:{branchId:branchId},
                                        fromDate:convertDateUTC(fromDate),
                                        toDate:convertDateUTC(toDate),
                                        requestType:requestType.value,
                                        userId:profileId,
                                        combinedFacility: combinedFacilityId
                                      };

                       $http({
                             url : 'services/report/tripSheetDownload/',          
                             method: "POST",
                             data: dataObj, 
                             headers: {
                                'Content-type': 'application/json'
                             },
                             responseType: 'arraybuffer'
                         }).success(function (data, status, headers, config) {
                             var blob = new Blob([data], {
                             });
                             saveAs(blob, 'TripSheet' + '.xlsx');
                         }).error(function (data, status, headers, config) {
                             alert("Download Failed")
                         });
                  }
                
             }
           
           //Check if KM Report Tab has been clicked                 
           if($scope.currentTab == 'kmReport'){
               $scope.isShiftKM = false;
               $('.popover').hide(); 
               $scope.distanceGetReportButtonClicked = true;
               $scope.efmfilter= {filterKm:''};
               
               if(angular.isObject($scope.reportKM.reportType) && angular.isObject($scope.reportKM.VSelection)){
                    $scope.reportsSum = [];                 
                    $scope.searchKMType = $scope.reportKM.reportType.value;
                    $scope.distanceShiftType = $scope.reportKM.distanceShift.text;
                    var vendor_id;
                    var vehicle_id;   
                   
                    if($scope.searchKMType == 'vendor'){
                       vendor_id = $scope.reportKM.VSelection.Id;
                       vehicle_id = 0;
                    }
                    else if ($scope.searchKMType == 'vehicle'){
                       vendor_id = 0;
                       vehicle_id = $scope.reportKM.VSelection.Id;               
                    }
                   
                   if($scope.reportKM.distanceShift.text == 'By Shifts'){
                    $scope.isShiftKM = false;
                   }
                   
                   else if($scope.reportKM.distanceShift.text == 'All Shifts' && $scope.reportKM.VSelection.Id != 0){
                       $scope.isShiftKM = false;
                      }
                   else if($scope.searchKMType == 'vehicle' && $scope.distanceShiftType=='By Shifts'){
                       $scope.isShiftKM = false;
                      }
                   else if($scope.distanceShiftType=='All Shifts' && $scope.searchKMType == 'vehicle'){
                     $scope.isShiftKM = true;
                   }
                   else{
                    $scope.isShiftKM = true;
                   }

                    var dataKm = {
                     eFmFmClientBranchPO:{branchId:branchId},
                     fromDate:convertDateUTC(fromDate),
                     toDate:convertDateUTC(toDate),
                     vendorId:vendor_id,
                     vehicleId:vehicle_id,
                     time:$scope.reportKM.distanceShift.value,
                     userId:profileId ,
                     searchType:$scope.searchKMType, //In case vendor please chenage the vehicle to vendor and add vendorId and searchType='vendor' as well
                     combinedFacility: combinedFacilityId
                    };
                    $http.post('services/report/kmreports/',dataKm).
                       success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                           $scope.distanceGetReportButtonClicked = false;
                           $scope.reportsSum = data;
                           $scope.reportsSumLength = data.length;
                           $scope.vehicleNumberField=vendor_id;
                           angular.forEach($scope.reportsSum, function(item){item.viewDetailIsClicked = false;})
                           $scope.viewDetail = false;                         
                           $scope.fromDate = fromDate;
                           $scope.toDate = toDate;                           
                           $scope.searchFromDateKM = fromDate;
                           $scope.searchToDateKM = toDate; 
                           $scope.headingReportTypeKM = $scope.reportKM.reportType.text;
                           $scope.reportSelectionKM = $scope.reportKM.VSelection.name;
                           if($scope.reportsSum.length>0){
                                $scope.gotKMResult = true;
                           }
                        else{
                            $scope.gotKMResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                        }
                        }).
                        error(function(data, status, headers, config) {
                             $scope.distanceGetReportButtonClicked = false;
                        });
               }else{  
                   $scope.distanceGetReportButtonClicked = false;
                   ngDialog.open({
                                    template: 'Please choose valid selection from the dropdowns.',
                                    plain: true
                                });
               } 
           }
        
            //Check if SMS Report Tab is clicked
           if($scope.currentTab == 'SMSReport'){
                $('.popover').hide();
                $scope.SMSGetReportButtonClicked = true;
                $scope.gotSMSResult = false;
                $scope.efmfilter= {filterSMSShow:''};
                data.combinedFacility = combinedFacilityId;
                data.userId=profileId;
                $http.post('services/report/smsreport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.SMSGetReportButtonClicked = false;
                        $scope.reportsSMSData = data;
                        $scope.reportsSMSDataLength = data.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDateSMS = fromDate;
                        $scope.searchToDateSMS = toDate;
                        if($scope.reportsSMSData.length>0){
                            $scope.gotSMSResult = true;
                        }else{
                            $scope.gotSMSResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                        if($scope.reportsSMSData == [])
                        {
                          $scope.gotSMSResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                          $scope.SMSGetReportButtonClicked = false;
                    });
           }  
               
          //Check if Escort Report Tab is clicked
          if($scope.currentTab == 'escortReport'){
                $('.popover').hide();
                $scope.escortGetReportButtonClicked = true;
                $scope.gotEscortResult = false;
                $scope.efmfilter= {filterEscort:''};
                data.combinedFacility = combinedFacilityId;
                data.userId=profileId;
                $http.post('services/report/escortReport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.escortGetReportButtonClicked = false;
                        $scope.reportEscortData = data.tripDetail;
                        $scope.reportEscortDataLength = data.tripDetail.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDateEscort = fromDate;
                        $scope.searchToDateEscort = toDate;
                        if($scope.reportEscortData.length>0){
                            $scope.gotEscortResult = true;
                        }else{
                            $scope.gotEscortResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                          $scope.escortGetReportButtonClicked = false;
                    });
               
           }
           
          //Check if Vehicle And Driver attendance report Tab is clicked
          if($scope.currentTab == 'vehicleDriverAttendence'){
                $('.popover').hide();
                $scope.vdattendanceGetReportButtonClicked = true;
                $scope.gotVDAttendanceResult = false;
                $scope.efmfilter= {filterVDAttendance:''};
                data.combinedFacility = combinedFacilityId;
                data.userId=profileId;
                $http.post('services/report/attendenceReport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.vdattendanceGetReportButtonClicked = false;
                    
                        $scope.reportsVDAttendanceData = data.tripDetail;
                        $scope.reportsVDAttendanceDataLength = data.tripDetail.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDatesVDA = fromDate;
                        $scope.searchToDatesVDA = toDate;
                        if($scope.reportsVDAttendanceData.length>0){
                            $scope.gotVDAttendanceResult = true;
                        }else{
                            $scope.gotVDAttendanceResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                          $scope.vdattendanceGetReportButtonClicked = false;
                    });              
               
           }
            
          //Check if Driver Working Hour report Tab is clicked
          if($scope.currentTab == 'driverWorkingHours'){
                $('.popover').hide();
                $scope.workingGetReportButtonClicked = true;
                $scope.gotDriverWHResult = false;
                $scope.efmfilter= {filterdriverWH:''};
                data.combinedFacility = combinedFacilityId;
                data.userId=profileId;
                $http.post('services/report/driverWorkinHoursReport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.workingGetReportButtonClicked = false;
                        $scope.reportsDriverWHData = data.tripDetail;
                        $scope.reportsDriverWHDataLength = data.tripDetail.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDatesDWH = fromDate;
                        $scope.searchToDatesDWH = toDate;
                        if($scope.reportsDriverWHData.length>0){
                            $scope.gotDriverWHResult = true;
                        }else{
                            $scope.gotDriverWHResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                         $scope.workingGetReportButtonClicked = false;
                    });
               
               
           }           
            
          //Check if Driver Driving Hour report Tab is clicked
          if($scope.currentTab == 'driverDrivingHours'){
                $('.popover').hide();
                $scope.drivingHoursgGetReportButtonClicked = true;
                $scope.gotDriverDHResult = false;
                $scope.efmfilter= {filterdriverDH:''};
                data.combinedFacility = combinedFacilityId;
                data.userId=profileId;
                $http.post('services/report/driverDrivingHoursReport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                       $scope.drivingHoursgGetReportButtonClicked = false;
                    
                        $scope.reportsDriverDHData = data;
                        $scope.reportsDriverDHDataLength = data.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDatesDDH = fromDate;
                        $scope.searchToDatesDDH = toDate;
                        if($scope.reportsDriverDHData.length>0){
                            $scope.gotDriverDHResult = true;
                        }else{
                            $scope.gotDriverDHResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                       $scope.drivingHoursgGetReportButtonClicked = false;
                    });
               
               
           }
          //Check if Speed report Tab is clicked
          if($scope.currentTab == 'speed'){    
                $('.popover').hide();
                $scope.speedGetReportButtonClicked = true;
                $scope.gotSpeedResult = false;
                $scope.efmfilter= {filterSpeed:''};

                var data = {
                        eFmFmClientBranchPO:{branchId:branchId},
                        fromDate:convertDateUTC(fromDate),
                        toDate:convertDateUTC(toDate),
                        vendorId:$scope.searchSpeed.SpeedVendors.Id,
                        vehicleId:$scope.searchSpeed.VSelection.Id,
                        userId:profileId,
                        combinedFacility: combinedFacilityId 
                      };
                $http.post('services/report/speedReport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.speedGetReportButtonClicked = false;
                        $scope.reportsSpeedData = data.tripDetail;
                        $scope.reportsSpeedDataLength = data.tripDetail.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDatesSpeed = fromDate;
                        $scope.searchFromDatesSpeed = toDate;
                    if($scope.reportsSpeedData.length){
                        $scope.gotSpeedResult = true;
                    }else{
                        $scope.gotSpeedResult = false;
                        ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates',
                                    plain: true
                                });
                    }
                    }
                    }).
                    error(function(data, status, headers, config) {
                         $scope.speedGetReportButtonClicked = false;
                    });
          }

          //Check if Route wise travel time Report is clicked
          if($scope.currentTab == 'routeWiseTravel'){               
                $('.popover').hide();
                $scope.routewiseGetReportButtonClicked = true;
                $scope.gotRWTResult = false;
                $scope.efmfilter= {filterRWT:''};
              
                var data = {
                        eFmFmClientBranchPO:{branchId:branchId},
                        fromDate:convertDateUTC(fromDate),
                        toDate:convertDateUTC(toDate),
                        tripType:$scope.searchRWT.type.value,
                        userId:profileId,
                        combinedFacility: combinedFacilityId 
                      };
                $http.post('services/report/routeWiceReport/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.routewiseGetReportButtonClicked = false;
                        $scope.reportsRouteWiseTravelData = data.tripDetail;
                        $scope.reportsRouteWiseTravelDataLength = data.tripDetail.length;
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDatesRWT = fromDate;
                        $scope.searchToDatesRWT = toDate;
                        $scope.RWTTripType = $scope.searchRWT.type.text;
                        if($scope.reportsRouteWiseTravelData.length>0){
                             $scope.gotRWTResult = true;
                        }else{
                             $scope.gotRWTResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                          $scope.routewiseGetReportButtonClicked = false;
                    });
          }
          if($scope.currentTab == 'feedbacks'){  
                $scope.gotFeedbackResult = false;
              $('.popover').hide();
                var data = {
                        branchId:branchId,
                        fromDate:convertDateUTC(fromDate),
                        toDate:convertDateUTC(toDate),
                        alertType:$scope.feedbackTypes.selectedType.value,
                        userId:profileId,
                        combinedFacility: combinedFacilityId 
                      };
                $http.post('services/alert/getFeedBacks/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                        $scope.routewiseGetReportButtonClicked = false;
                        $scope.feedbackData = data.data;
                        angular.forEach($scope.feedbackData, function(value, key) {
                          var dateSplited = value.updationDateTime.split(" ");
                          value.updationDateTime = dateSplited[0];
                         });
                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;
                        $scope.searchFromDatesRWT = fromDate;
                        $scope.searchToDatesRWT = toDate;
                        if($scope.feedbackData.length>0){
                             $scope.gotFeedbackResult = true;
                        }else{
                             $scope.gotFeedbackResult = false;
                            ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                        }
                      }
                    }).
                    error(function(data, status, headers, config) {
                          $scope.routewiseGetReportButtonClicked = false;
                    });
          }  
           //Check if On Time ArrivalReport is clicked
           if($scope.currentTab == 'onTime'){
           if($scope.viewTypeOntime == 'viewData'){               
               $('.popover').hide();
               $scope.OTGetReportButtonClicked = true;
               $scope.gotOTResult = false;
               $scope.efmfilter= {filterOTData: ''};
               requestType=$scope.requestTypesDataOT.selectedOption;
               if(angular.isObject($scope.searchOT.OTVendors) && angular.isObject($scope.searchOT.OTShift)){ 
                   $scope.headingVendorsOT = $scope.searchOT.OTVendors.name;
                   $scope.headingShiftOT = $scope.searchOT.OTShift.text;
                   $scope.searchFromDateOT = fromDate;
                   $scope.searchToDateOT = toDate;
                   
                   if($scope.searchOT.OTVendors.Id == 0 && $scope.searchOT.OTShift.value == 0){
                   }
                                  
                   
                   if($scope.searchOT.type.value == 'PICKUP'){
                       var data = {
                         eFmFmClientBranchPO:{branchId:branchId},
                         fromDate:convertDateUTC(fromDate),
                         toDate:convertDateUTC(toDate),
                         tripType:$scope.searchOT.type.value,
                         time:$scope.searchOT.OTShift.value,
                         vendorId:$scope.searchOT.OTVendors.Id,
                         requestType:requestType.value,
                         userId:profileId,
                         combinedFacility: combinedFacilityId 
                         
                       };

                      
                       $http.post('services/report/ontimearrival/',data).
                            success(function(data, status, headers, config) {
                             
                              if (data.status != "invalidRequest") {
                               $scope.onTimeData = data.tripDetail;
                               $scope.onTimeDataLength = data.tripDetail.length;                          
                               $scope.fromDate = fromDate;
                               $scope.toDate = toDate;
                               if($scope.onTimeData.length>0){
                                   $scope.gotOTResult = true;                                   
                                   $scope.OTresultTripType = 'Pickup';
                                   $scope.bufferTime='(15 Minutes)';
                                   if($scope.searchOT.OTVendors.Id != 0){
                                     $scope.vendorName = true; 
                                   }
                                   else{$scope.vendorName = false; 
                                       }
                                   
                                   if($scope.searchOT.OTShift.value == 0){
                                     $scope.OTresultDateOrShift='Date';
                                   }
                                   else{
                                     $scope.OTresultDateOrShift='Shift Time'; 
                                   }
                                   
                                   $scope.OT = 'A';
                                   }
                               else{
                                   $scope.gotOTResult = false;
                                   ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                                   }
                               $scope.OTGetReportButtonClicked = false;
                             }
                            }).
                            error(function(data, status, headers, config) {
                                $scope.OTGetReportButtonClicked = false;
                            });
                   }
                   else if($scope.searchOT.type.value == 'DROP'){
                       var data = {
                         eFmFmClientBranchPO:{branchId:branchId},
                         fromDate:convertDateUTC(fromDate),
                         toDate:convertDateUTC(toDate),
                         tripType:$scope.searchOT.type.value,
                         time:$scope.searchOT.OTShift.value,
                         vendorId:$scope.searchOT.OTVendors.Id,
                         requestType:requestType.value,
                         userId:profileId,
                         combinedFacility: combinedFacilityId 
                       };

                       $http.post('services/report/ontimearrival/',data).
                            success(function(data, status, headers, config) {
                              if (data.status != "invalidRequest") {
                               $scope.OTGetReportButtonClicked = false;
                               $scope.onTimeData = data.tripDetail;                           
                               $scope.fromDate = fromDate;
                               $scope.toDate = toDate;
                               if($scope.onTimeData.length>0){
                                   $scope.gotOTResult = true;                                   
                                   $scope.OTresultTripType = 'Drop';
                                   $scope.bufferTime='';
                                   if($scope.searchOT.OTVendors.Id != 0){
                                     $scope.vendorName = true; 
                                   }
                                   if($scope.searchOT.OTShift.value == 0){
                                     $scope.OTresultDateOrShift='Date';
                                   }
                                   else{
                                     $scope.OTresultDateOrShift='Shift Time'; 
                                   }
                                   $scope.OT = 'D';
                                   }
                               else{
                                   $scope.gotOTResult = false;
                                   ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                                   }
                                 }
                            }).
                            error(function(data, status, headers, config) {
                                $scope.OTGetReportButtonClicked = false;
                            });
                   }
               }
             } else{
              requestType = $scope.requestTypesDataOT.selectedOption;
          var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                         fromDate:convertDateUTC(fromDate),
                         toDate:convertDateUTC(toDate),
                         tripType:$scope.searchOT.type.value,
                         time:$scope.searchOT.OTShift.value,
                         vendorId:$scope.searchOT.OTVendors.Id,
                         requestType:requestType.value,
                         userId:profileId,
                         combinedFacility: combinedFacilityId
                        };

         $http({
               url : 'services/report/ontimeArrivalDownload/',          
               method: "POST",
               data: dataObj, 
               headers: {
                  'Content-type': 'application/json'
               },
               responseType: 'arraybuffer'
           }).success(function (data, status, headers, config) {
               var blob = new Blob([data], {
               });
               
               saveAs(blob, 'onTime' + '.xlsx');
           }).error(function (data, status, headers, config) {
               alert("Download Failed")
           });
             }
           }          
           //Check if Seat Utilization Report is clicked
           if($scope.currentTab == 'seatUtil'){               
               $('.popover').hide();
               $scope.SUGetReportButtonClicked = true;
               $scope.efmfilter= {filterSeatUtilizationData:''};
               $scope.gotSUResult = false;
               $scope.SUTripType = '';
               $scope.SUReportTitle = '';
               $scope.searchFromDatesSU = '';
               $scope.searchToDatesSU = '';
               requestType=$scope.requestTypesDataSU.selectedOption;
                 var data = {
                           eFmFmClientBranchPO:{branchId:branchId},
                           fromDate:convertDateUTC(fromDate),
                           toDate:convertDateUTC(toDate),
                           tripType:$scope.searchSU.type.value,
                           time:$scope.searchSU.SUShift.value,
                           requestType:requestType.value,
                           userId:profileId,
                           combinedFacility: combinedFacilityId 
                         };
                         $http.post('services/report/seatutilization/',data).
                         success(function(data, status, headers, config) {
                          if (data.status != "invalidRequest") {
                             $scope.SUGetReportButtonClicked = false;
                               $scope.seatUtilData = data.tripDetail;     
                               $scope.seatUtilDataLength = data.tripDetail.length;                     
                               $scope.fromDate = fromDate;
                               $scope.toDate = toDate;
                               if($scope.seatUtilData.length>0){
                                   $scope.gotSUResult = true;    
                                   $scope.SUresultTripType = $scope.searchSU.type.text;
                                   $scope.SUTripType = $scope.searchSU.type.text;
                                   $scope.SUReportTitle = 'Monthly Report for '+$scope.searchSU.SUShift.text;
                                   $scope.searchFromDatesSU = fromDate;
                                   $scope.searchToDatesSU = toDate;
                                   if($scope.searchSU.SUShift.value == 0){
                                     $scope.SUresultDateOrShift='Date';
                                   }
                                   else{
                                     $scope.SUresultDateOrShift='Shift Time'; 
                                   }
                                   }
                               else{
                                   $scope.gotSUResult = false;
                                   ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                                   }
                                 }
                            }).
                            error(function(data, status, headers, config) {
                                   $scope.SUGetReportButtonClicked = false;
                            });
           }
           


             //Check if Dynamic Report is clicked
           if($scope.currentTab == 'dynamicDetails'){               
               $('.popover').hide();

     
              var modalInstance = $modal.open({
               templateUrl: 'partials/modals/reports/popupDynamicReport.jsp',
               controller: 'dynamicDetailsReportCtrl',
               resolve: {
                   dynamicData: function(){return $scope.dynamicDatalabelArray;},
                   fromDate: function(){return convertDateUTC(fromDate);},
                   toDate: function(){return convertDateUTC(toDate);},
                   searchType: function(){return $scope.selectedDynamicType;},
               }
              }); 


              if($scope.employeeIdFlg == 1 || $scope.fifteenMinuteMsgFlg || $scope.hostMobileNumberFlg || $scope.boardingTimeFlg || $scope.reachedMessageFlg || $scope.boardingStatus || $scope.employeeLocationFlg || $scope.emailIdFlg || $scope.cabDelayMessageFlg || $scope.pickupTimeFlg == 1 || $scope.allocationMsgFlg == 1 || $scope.employeeMobileNoFlg == 1 || $scope.employeeNameFlg == 1)
              {
                $scope.employeeDetails = true;
              }


              $rootScope.$on('dynamicDataEvent', function(event, dataShow) { 
                $rootScope.dataShowValues = dataShow;
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                
                if($scope.valueFlg == "assignDate")
                {
                    if($scope.valueFlg == "assignDate")
                      {
                          $scope.assignDateShow = true;
                      }
                      else
                      {
                          $scope.assignDateShow = false;
                      }
                }
                else
                {
                  return true;
                }
                      
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "routeCloseTime")
                    {
                        if($scope.valueFlg == "routeCloseTime")
                        {
                            $scope.routeCloseTimeShow = true;
                        }
                        else
                        {
                            $scope.routeCloseTimeShow = false;
                        }
                    }
                    else
                    {
                        return true;
                    }
                      
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                      if($scope.valueFlg == "shiftTime")
                      {
                        if($scope.valueFlg == "shiftTime")
                        {
                            $scope.shiftTimeShow = true;
                        }
                        else
                        {
                            $scope.shiftTimeShow = false;
                        }
                      }
                      else
                      {
                        return true;
                      }
                      
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "tripType")
                    {
                        if($scope.valueFlg == "tripType")
                        {
                            $scope.tripTypeShow = true;
                        }
                        else
                        {
                            $scope.tripTypeShow = false;
                        }
                    }
                    else
                    {
                      return true;
                    }
                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "tripStartTime")
                    {
                      if($scope.valueFlg == "tripStartTime")
                      {
                          $scope.tripStartTimeShow = true;
                      }
                      else
                      {
                          $scope.tripStartTimeShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "tripEndTime")
                    {
                      if($scope.valueFlg == "tripEndTime")
                      {
                        $scope.tripEndTimeShow = true;
                      }
                      else
                      {
                       $scope.tripEndTimeShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "routeName")
                    {
                      if($scope.valueFlg == "routeName")
                      {
                          $scope.routeNameShow = true;
                      }
                      else
                      {
                          $scope.routeNameShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "cabReachedTime")
                    {
                      if($scope.valueFlg == "cabReachedTime")
                      {
                          $scope.cabReachedTimeShow = true;
                      }
                      else
                      {
                          $scope.cabReachedTimeShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "boardingStatus")
                    {
                      if($scope.valueFlg == "boardingStatus")
                      {
                         $scope.boardingStatusShow = true;
                      }
                      else
                      {
                          $scope.boardingStatusShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                }); 
               
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "boardingTime")
                    {
                      if($scope.valueFlg == "boardingTime")
                      {
                        $scope.boardingTimeShow = true;
                      }
                      else
                      {
                       $scope.boardingTimeShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "pickupTime")
                    {
                    if($scope.valueFlg == "pickupTime")
                    {
                      $scope.pickupTimeShow = true;
                    }
                    else
                    {
                     $scope.pickupTimeShow = false;
                    }
                    }
                    else
                    {
                      return true;
                    }
                });    

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverNumber")
                    {
                      if($scope.valueFlg == "driverNumber")
                      {
                        $scope.driverNumberShow = true;
                      }
                      else
                      {
                       $scope.driverNumberShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverName")
                    {
                      if($scope.valueFlg == "driverName")
                      {
                        $scope.driverNameShow = true;
                      }
                      else
                      {
                        $scope.driverNameShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverMobileNumber")
                    {
                      if($scope.valueFlg == "driverMobileNumber")
                      {
                        $scope.driverMobileNumberShow = true;
                      }
                      else
                      {
                         $scope.driverMobileNumberShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                });
                  
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverAddress")
                    {
                      if($scope.valueFlg == "driverAddress")
                      {
                        $scope.driverAddressShow = true;
                      }
                      else
                      {
                       $scope.driverAddressShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                });
                
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "checkInTime")
                    {
                      if($scope.valueFlg == "checkInTime")
                      {
                        $scope.checkInTimeShow = true;
                      }
                      else
                      {
                        $scope.checkInTimeShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "checkOutTime")
                    {
                      if($scope.valueFlg == "checkOutTime")
                      {
                        $scope.checkOutTimeShow = true;
                      }
                      else
                      {
                        $scope.checkOutTimeShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;

                    if($scope.valueFlg == "workingHours")
                    {
                      if($scope.valueFlg == "workingHours")
                      {
                          $scope.workingHoursShow = true;
                      }
                      else
                      {
                          $scope.workingHoursShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "travelTime")
                    {
                       if($scope.valueFlg == "travelTime")
                        {
                            $scope.travelTimeShow = true;
                        }
                        else
                        {
                            $scope.travelTimeShow = false;
                        }
                    }
                    else
                    {
                      return true;
                    }
                   
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "checkInVehicleNumber")
                    {
                      if($scope.valueFlg == "checkInVehicleNumber")
                      {
                        $scope.checkInVehicleNumberShow = true;
                      }
                      else
                      {
                       $scope.checkInVehicleNumberShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                    
                }); 
                  
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "assignDate")
                    {
                      if($scope.valueFlg == "assignDate")
                      {
                          $scope.assignDateShow = true;
                      }
                      else
                      {
                          $scope.assignDateShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "alloccationMessage")
                    { 
                      if($scope.valueFlg == "alloccationMessage")
                      {
                        $scope.alloccationMessageShow = true;
                      }
                      else
                      {
                       $scope.alloccationMessageShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 
                  
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "cabDelayMessage")
                    {
                      if($scope.valueFlg == "cabDelayMessage")
                      {
                        $scope.cabDelayMsgShow = true;
                      }
                      else
                      {
                       $scope.cabDelayMsgShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "reachedMessage")
                    {
                        if($scope.valueFlg == "reachedMessage")
                        {
                          $scope.reachedMsgShow = true;
                        }
                        else
                        {
                          $scope.reachedMsgShow = false;
                        }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 
                  
                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "fifteenMinuteMsg")
                    {
                      if($scope.valueFlg == "fifteenMinuteMsg")
                      {
                        $scope.fifteenMinuteMsgShow = true;
                      }
                      else
                      {
                        $scope.fifteenMinuteMsgShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "noShowMessage")
                    {
                      if($scope.valueFlg == "noShowMessage")
                      {
                        $scope.noShowMsgShow = true;
                      }
                      else
                      {
                       $scope.noShowMsgShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }


                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "vehileDetails")
                    {
                      if($scope.valueFlg == "vehileDetails")
                      {
                        $scope.vehicleDetailsShow = true;
                      }
                      else
                      {
                       $scope.vehicleDetailsShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "vehicleNumber")
                    {
                      $scope.vehicleNumberShow = true;
                    }
                    else
                    {
                     $scope.vehicleNumberShow = false;
                    }
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "escortDetails")
                    {
                      if($scope.valueFlg == "escortDetails")
                      {
                        $scope.escortDetailsShow = true;
                      }
                      else
                      {
                        $scope.escortDetailsShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "escortId")
                    {
                        if($scope.valueFlg == "escortId")
                        {
                          $scope.escortIdShow = true;
                        }
                        else
                        {
                         $scope.escortIdShow = false;
                        }
                    }
                    else
                    {
                      return true;
                    }

                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "routeId")
                    {
                        if($scope.valueFlg == "routeId")
                        {
                          $scope.routeIdShow = true;
                        }
                        else
                        {
                         $scope.routeIdShow = false;
                        }
                    }
                    else
                    {
                      return true;
                    }

                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "escortName")
                    {
                      if($scope.valueFlg == "escortName")
                      {
                        $scope.escortNameShow = true;
                      }
                      else
                      {
                        $scope.escortNameShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "escortMobileNo")
                    {
                      if($scope.valueFlg == "escortMobileNo")
                      {
                        $scope.escortMobileNoShow = true;
                      }
                      else
                      {
                       $scope.escortMobileNoShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }


                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "escotrtAddress")
                    {
                      if($scope.valueFlg == "escotrtAddress")
                      {
                        $scope.escortAddressShow = true;
                      }
                      else
                      {
                        $scope.escortAddressShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "employeeDetails")
                    {
                      if($scope.valueFlg == "employeeDetails")
                      {
                        $scope.empDetailsShow = true;
                      }
                      else
                      {
                        $scope.empDetailsShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }

                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "employeeId")
                    {
                      if($scope.valueFlg == "employeeId")
                      {
                        $scope.employeeIdShow = true;
                      }
                      else
                      {
                        $scope.employeeIdShow= false;
                      }
                    }
                    else
                    {
                      return true;
                    }


                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "escortMobileNo")
                    {
                      if($scope.valueFlg == "escortMobileNo")
                      {
                        $scope.escortMobileNoShow = true;
                      }
                      else
                      {
                        $scope.escortMobileNoShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }


                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "employeeName")
                    {
                      if($scope.valueFlg == "employeeName")
                      {
                        $scope.employeeNameShow = true;
                      }
                      else
                      {
                        $scope.employeeNameShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "plannedDistance")
                    {
                      if($scope.valueFlg == "plannedDistance")
                      {
                        $scope.plannedDistanceShow = true;
                      }
                      else
                      {
                        $scope.plannedDistanceShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                   
                    if($scope.valueFlg == "gps")
                    {
                      if($scope.valueFlg == "gps")
                      {
                        $scope.gpsDistanceShow = true;
                      }
                      else
                      {
                        $scope.gpsDistanceShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });       

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "vendorName")
                    {
                      if($scope.valueFlg == "vendorName")
                      {
                        $scope.vendorNameShow = true;
                      }
                      else
                      {
                        $scope.vendorNameShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });       

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverId")
                    {
                      if($scope.valueFlg == "driverId")
                      {
                        $scope.driverIdShow = true;
                      }
                      else
                      {
                        $scope.driverIdShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                });   

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "totalWorkingHours")
                    {
                      if($scope.valueFlg == "totalWorkingHours")
                      {
                        $scope.totalWorkingHoursShow = true;
                      }
                      else
                      {
                        $scope.totalWorkingHoursShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverDrivingHours")
                    {
                      if($scope.valueFlg == "driverDrivingHours")
                      {
                        $scope.driverDrivingHoursShow = true;
                      }
                      else
                      {
                        $scope.driverDrivingHoursShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "remarks")
                    {
                      if($scope.valueFlg == "remarks")
                      {
                        $scope.remarksShow = true;
                      }
                      else
                      {
                        $scope.remarksShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "hostMobileNumber")
                    {
                      if($scope.valueFlg == "hostMobileNumber")
                      {
                        $scope.hostMobileNumberShow = true;
                      }
                      else
                      {
                        $scope.hostMobileNumberShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "noShow")
                    {
                      if($scope.valueFlg == "noShow")
                      {
                        $scope.noShowShow = true;
                      }
                      else
                      {
                        $scope.noShowShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "totalDrivingHours")
                    {
                      if($scope.valueFlg == "totalDrivingHours")
                      {
                        $scope.totalDrivingHoursShow = true;
                      }
                      else
                      {
                        $scope.totalDrivingHoursShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 

                angular.forEach(dataShow.dynamicData, function(value, key) {
                    $scope.valueFlg = value.dynamicDataFlg;
                    if($scope.valueFlg == "driverDrivingHoursPerTrips")
                    {
                      if($scope.valueFlg == "driverDrivingHoursPerTrips")
                      {
                        $scope.driverDrivingHoursPerTripsShow = true;
                      }
                      else
                      {
                        $scope.driverDrivingHoursPerTripsShow = false;
                      }
                    }
                    else
                    {
                      return true;
                    }
                    
                }); 



                  if($scope.dynamicDataSelected == "driverDetails")
                  {
                    
                      var data = 
                         {
                          eFmFmClientBranchPO:{branchId:branchId},
                          userId:profileId,
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          searchType:$scope.dynamicDataSelected,
                          vendorIdSelected:$scope.vendorListDatas,
                          assignDateFlg : $scope.assignDateFlg,
                          routeCloseTimeFlg : $scope.routeCloseTimeFlg,
                          shiftTimeFlg : $scope.shiftTimeFlg,
                          tripTypeFlg : $scope.tripTypeFlg,
                          tripStartTimeFlg : $scope.tripStartTimeFlg,
                          tripEndTimeFlg : $scope.tripEndTimeFlg,
                          routeNameFlg : $scope.routeNameFlg,
                          cabReachedTimeFlg : $scope.cabReachedTimeFlg,
                          boardingStatusFlg : $scope.boardingStatusFlg,
                          boardingTimeFlg : $scope.boardingTimeFlg,
                          pickUpTimeFlg: $scope.pickupTimeFlg,                      
                          driverNameFlg: $scope.driverNameFlg,
                          driverMobileNumberFlg: $scope.driverMobileNumberFlg,
                          driverAddressFlg : $scope.driverAddressFlg,
                          checkInTimeFlg: $scope.checkInTimeFlg,
                          checkoutTimeFlg: $scope.checkOutTimeFlg,
                          travelTimeFlg: $scope.travelTimeFlg,
                          workingHoursFlg: $scope.workingHoursFlg,
                          allocationMsgFlg: $scope.alloccationMessageFlg,
                          cabDelayMsgFlg: $scope.cabDelayMessageFlg,
                          reachedMsgFlg: $scope.reachedMessageFlg,
                          fifteenMinuteMsgFlg: $scope.fifteenMinuteMsgFlg,
                          noshowMsgFlg: $scope.noShowMessageFlg,
                          vehicleNumberFlg: $scope.vehicleNumberFlg,
                          escortIdFlg: $scope.escortIdFlg,
                          escortNameFlg: $scope.escortNameFlg,
                          escortMobileNoFlg: $scope.escortMobileNoFlg,
                          escortAddressFlg: $scope.escortAddressFlg,
                          employeeIdFlg: $scope.employeeIdFlg,
                          employeeMobileNoFlg: $scope.employeeMobileNoFlg,
                          employeeNameFlg: $scope.employeeNameFlg,
                          selectedCounts : $scope.dataDynamicFlgLength,
                          plannedDistanceFlg : $scope.plannedDistanceFlg,
                          gpsFlg : $scope.gpsFlg,
                          vendorNameFlg : $scope.vendorNameFlg,
                          driverIdFlg : $scope.driverIdFlg,
                          totalWorkingHoursFlg : $scope.totalWorkingHoursFlg,
                          totalDrivingHoursFlg : $scope.totalDrivingHoursFlg,
                          driverDrivingHoursPerTripFlg : $scope.driverDrivingHoursPerTripFlg,
                          remarksFlg : $scope.remarksFlg,
                          hostMobileNumberFlg : $scope.hostMobileNumberFlg,
                          routeIdFlg : $scope.routeIdFlg,
                          combinedFacility: combinedFacilityId,
                          userId:profileId 
                     };
                  }

                  if($scope.dynamicDataSelected == "employee" || $scope.dynamicDataSelected == "guest")
                  {
                    
                      var data = 
                         {
                          eFmFmClientBranchPO:{branchId:branchId},
                          userId:profileId,
                          fromDate:convertDateUTC(fromDate),
                          toDate:convertDateUTC(toDate),
                          searchType:$scope.dynamicDataSelected,
                          assignDateFlg : $scope.assignDateFlg,
                          routeCloseTimeFlg : $scope.routeCloseTimeFlg,
                          shiftTimeFlg : $scope.shiftTimeFlg,
                          tripTypeFlg : $scope.tripTypeFlg,
                          tripStartTimeFlg : $scope.tripStartTimeFlg,
                          tripEndTimeFlg : $scope.tripEndTimeFlg,
                          routeNameFlg : $scope.routeNameFlg,
                          cabReachedTimeFlg : $scope.cabReachedTimeFlg,
                          boardingStatusFlg : $scope.boardingStatusFlg,
                          boardingTimeFlg : $scope.boardingTimeFlg,
                          pickUpTimeFlg: $scope.pickupTimeFlg,                      
                          driverNameFlg: $scope.driverNameFlg,
                          driverMobileNumberFlg: $scope.driverMobileNumberFlg,
                          driverAddressFlg : $scope.driverAddressFlg,
                          checkInTimeFlg: $scope.checkInTimeFlg,
                          checkoutTimeFlg: $scope.checkOutTimeFlg,
                          travelTimeFlg: $scope.travelTimeFlg,
                          workingHoursFlg: $scope.workingHoursFlg,
                          allocationMsgFlg: $scope.alloccationMessageFlg,
                          cabDelayMsgFlg: $scope.cabDelayMessageFlg,
                          reachedMsgFlg: $scope.reachedMessageFlg,
                          fifteenMinuteMsgFlg: $scope.fifteenMinuteMsgFlg,
                          noshowMsgFlg: $scope.noShowMessageFlg,
                          vehicleNumberFlg: $scope.vehicleNumberFlg,
                          escortIdFlg: $scope.escortIdFlg,
                          escortNameFlg: $scope.escortNameFlg,
                          escortMobileNoFlg: $scope.escortMobileNoFlg,
                          escortAddressFlg: $scope.escortAddressFlg,
                          employeeIdFlg: $scope.employeeIdFlg,
                          employeeMobileNoFlg: $scope.employeeMobileNoFlg,
                          employeeNameFlg: $scope.employeeNameFlg,
                          selectedCounts : $scope.dataDynamicFlgLength,
                          plannedDistanceFlg : $scope.plannedDistanceFlg,
                          gpsFlg : $scope.gpsFlg,
                          vendorNameFlg : $scope.vendorNameFlg,
                          driverIdFlg : $scope.driverIdFlg,
                          totalWorkingHoursFlg : $scope.totalWorkingHoursFlg,
                          totalDrivingHoursFlg : $scope.totalDrivingHoursFlg,
                          /*driverDrivingHoursFlg : $scope.driverDrivingHoursFlg,*/
                          driverDrivingHoursPerTripFlg : $scope.driverDrivingHoursPerTripFlg,
                          remarksFlg : $scope.remarksFlg,
                          hostMobileNumberFlg : $scope.hostMobileNumberFlg,
                          routeIdFlg : $scope.routeIdFlg,
                          combinedFacility: combinedFacilityId,
                          userId:profileId 
                     };
                  }
     
                      
                  $scope.byReportDynamicClicked = true;

                  if($scope.dynamicDataSelected == "employee" || $scope.dynamicDataSelected == "guest")
                  {
                     $scope.dynamicDataDivEmployeeShow = true;
                  }
                        $scope.gotDynamicResult = false;

                        $scope.fromDate = fromDate;
                        $scope.toDate = toDate;

                         $scope.pairsDataDynamic = [];
                         angular.forEach(_.pairs(data), function(value, key) {
                            if(value[1] == 0){
                              return true;
                            }else{
                              $scope.pairsDataDynamic.push(_.pairs(data)[key]);
                            }
                         });

                         var data = _.object($scope.pairsDataDynamic);
                         data.combinedFacility =  combinedFacilityId;

                         $rootScope.dynamicReportData = _.object($scope.pairsDataDynamic);
                         data.userId = profileId;
                         $http.post('services/report/dynamicReport/',data).
                         success(function(data, status, headers, config) {
                          if (data.status != "invalidRequest") {
                          $scope.dynamicDataRecords = data;
                          $scope.dynamicDataLengthLength = data.length;
                          $scope.byReportDynamicClicked = false;
                          $scope.gotDynamicResult = true;
                          $scope.searchFromDatesDR = fromDate;
                          $scope.searchToDatesDR = toDate;
                          }
                          }).
                          error(function(data, status, headers, config) {
                               
                          });

              });

     
           
           }

           //If the No Show Tab is clicked
           if($scope.currentTab == 'noShow'){              
               $('.popover').hide();
               $scope.NSGetReportButtonClicked = true;
               requestType=$scope.requestTypesDataNS.selectedOption;
               $scope.efmfilter= {filterNoShowData:''};
               $scope.gotNSResult = false;
               $scope.NSTripType = '';
               $scope.NSReportTitle = '';
               $scope.searchFromDatesNS = '';
               $scope.searchToDatesNS = '';  
               if(!$scope.searchNS.employeeId){
                 employeeId='0';
                   $scope.searchNSByEmployeeId = false;
               }
               else{
                 employeeId=$scope.searchNS.employeeId;
                   $scope.searchNSByEmployeeId = true;
                   $scope.searchNS.NSShift = {text:'All Shifts', value: 0};
               }
                   var data = {
                         eFmFmClientBranchPO:{branchId:branchId},
                         fromDate:convertDateUTC(fromDate),
                         toDate:convertDateUTC(toDate) ,  
                         tripType:$scope.searchNS.type.value,
                         time:$scope.searchNS.NSShift.value,
                         employeeId:employeeId,
                         requestType:requestType.value,
                         userId:profileId,
                         combinedFacility: combinedFacilityId 

                       };

                       $http.post('services/report/noshowreport/',data).
                            success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") { 
                              $scope.tripDetailData = [];
                              angular.forEach(data.tripDetail, function(value, key) {
                                $scope.tripDetailData.push(value.totalEmployeesNoShowCount);
                              }); 
                               $scope.NSGetReportButtonClicked = false;
                               $scope.fromDate = fromDate;
                               $scope.toDate = toDate;
                               $scope.noShowReportData = data.tripDetail;
                               $scope.noShowReportDataLength = data.tripDetail.length;
                               if($scope.noShowReportData.length>0){
                                  $scope.gotNSResult = true;
                                  
                                   $scope.NSResultTripType = $scope.searchNS.type.text;    
                                   $scope.NSTripType = $scope.searchNS.type.text;  
                                   $scope.NSresultShift = $scope.searchNS.NSShift.text;
                                   if($scope.searchNSByEmployeeId){
                                       $scope.NSReportTitle = 'No Show Report for By Employee Id';
                                   }
                                   else{
                                        $scope.NSReportTitle = "No Show Report for " + $scope.searchNS.NSShift.text;
                                   }
                                   $scope.searchFromDatesNS = $scope.fromDate;
                                   $scope.searchToDatesNS = $scope.toDate;
                                   if($scope.searchNS.NSShift.value == 0 && employeeId ==0){
                                     $scope.noShowResult = 1; //All Shifts with No Employee Id
                                     $scope.NStotalShift=$scope.searchNS.type.value+' Trips';
                                     $scope.NSTripType=$scope.searchNS.type.value +' Pax';
                                   } 
                                   else if($scope.searchNS.NSShift.value == 0 && employeeId !=0){ 
                                     $scope.noShowResult = 0; //All Shifts with Employee Id
                                     $scope.NStotalShift='Employee Id';
                                     $scope.NSTripType='Employee Name';

                                   }
                                   else{ 
                                     $scope.noShowResult = 0; //By Shift
                                     $scope.NStotalShift='Employee Id';
                                     $scope.NSTripType='Employee Name';
                                     
                                   }
                                }
                                else{
                                   $scope.gotNSResult = false;
                                    ngDialog.open({
                                    template: 'No Data Found. Please Change the Dates.',
                                    plain: true
                                });
                                }
                              }
                            }).
                            error(function(data, status, headers, config) {
                                   $scope.NSGetReportButtonClicked = false;
                            });                   
               }
               
               if($scope.currentTab == 'employeeDetails'){            
                   $('.popover').hide();
                   $scope.EDGetReportButtonClicked = true;
                   $scope.efmfilter= {filterEmployeeDetails:''};
                   $scope.gotEDResult = false;
                   $scope.searchFromDatesED = '';
                   $scope.searchToDatesED = '';  
                   if($scope.searchED.employeeId){
                     var data = {
                               eFmFmClientBranchPO:{branchId:branchId},
                               fromDate:convertDateUTC(fromDate),
                               toDate:convertDateUTC(toDate) ,  
                               employeeId:$scope.searchED.employeeId,
                               userId:profileId,
                               combinedFacility: combinedFacilityId 

                             };
                       $http.post('services/report/employeeWiseReport/',data).
                                success(function(data, status, headers, config) { 
                                  if (data.status != "invalidRequest") {
                                   $scope.EDGetReportButtonClicked = false;
                                   $scope.fromDate = fromDate;
                                   $scope.toDate = toDate;
                                   $scope.employeeDetailsData = data;
                                   $scope.employeeDetailsDataLength = data.length;
                                   if($scope.employeeDetailsData.length>0){  
                                       $scope.gotEDResult = true;
                                       $scope.searchFromDatesED = $scope.fromDate;
                                       $scope.searchToDatesED = $scope.toDate;
                                   }
                                   else{ 
                                       $scope.gotEDResult = false;
                                        ngDialog.open({
                                            template: 'No Data Found. Please Change the Dates.',
                                            plain: true
                                        });

                                   }
                                 }
                       }).error(function(data, status, headers, config) { });

                   }
                   else{
                        ngDialog.open({
                            template: 'Please enter a valid Employee Id',
                            plain: true
                        });
                   }

               }

                if($scope.currentTab == 'travelReport'){
                   $('.popover').hide();
                   $scope.TRGetReportButtonClicked = true;
                   $scope.efmfilter= {filterTravelDetails:''};
                   $scope.gotTravelResult = false;
                   $scope.searchFromDatesTR = '';
                   $scope.searchToDatesTR = '';
                          var data = {
                                        userId:profileId,
                                        eFmFmClientBranchPO:{branchId:branchId},
                                        fromDate: convertDateUTC(fromDate),
                                        toDate: convertDateUTC(toDate),
                                        combinedFacility: combinedFacilityId
                        };
                        if ($scope.searchTR) {

                                        data.projectId = $scope.searchTR.projectId;
                        } else{

                                        data.projectId = 0;
                        };
                     $http.post('services/report/costReport/',data).
                        success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                   $scope.TRGetReportButtonClicked = false;
                                   $scope.fromDate = fromDate;
                                   $scope.toDate = toDate;
                                   $scope.travelReportData = data;
                                   $scope.travelReportDataLength = data.length;
                                   if($scope.travelReportData.length>0){
                                       $scope.gotTravelResult = true;
                                       $scope.searchFromDatesTR = $scope.fromDate;
                                       $scope.searchToDatesTR = $scope.toDate;
                                   }
                                   else{
                                       $scope.gotTravelResult = false;
                                        ngDialog.open({
                                            template: 'No Data Found. Please Change the Dates.',
                                            plain: true
                                        });

                                   }
                                 }
                       }).error(function(data, status, headers, config) { });

               }


               if($scope.currentTab == 'employeeFemaleDetails'){ 

                   $('.popover').hide();
                   $scope.EDFemaleGetReportButtonClicked = true;
                   $scope.efmfilter= {filterEmployeeDetails:''};
                   $scope.gotFemaleEDResult = false;
                   $scope.searchFromDatesED = '';
                   $scope.searchToDatesED = '';  
                     var data = {
                               eFmFmClientBranchPO:{branchId:branchId},
                               fromDate:convertDateUTC(fromDate),
                               toDate:convertDateUTC(toDate) ,  
                               userId:profileId,
                               combinedFacility: combinedFacilityId 

                             };
                       $http.post('services/report/femaleEmployeeWiseReport/',data).
                                success(function(data, status, headers, config) { 
                                  if (data.status != "invalidRequest") {
                                   $scope.EDFemaleGetReportButtonClicked = false;
                                   $scope.fromDate = fromDate;
                                   $scope.toDate = toDate;
                                   $scope.employeeFemaleDetailsData = data;
                                   $scope.employeeFemaleDetailsDataLength = data.length;
                                   if($scope.employeeFemaleDetailsData.length>0){  
                                       $scope.gotFemaleEDResult = true;
                                       $scope.searchFromDatesED = $scope.fromDate;
                                       $scope.searchToDatesED = $scope.toDate;
                                   }
                                   else{ 
                                       $scope.gotFemaleEDResult = false;
                                        ngDialog.open({
                                            template: 'No Data Found. Please Change the Dates.',
                                            plain: true
                                        });

                                   }
                                 }
                       }).error(function(data, status, headers, config) { });

                   
               }   

               if($scope.currentTab == 'employeeRequestDetails'){            
                   $('.popover').hide();
                   $scope.EDGetReportButtonClicked = true;
                   $scope.efmfilter= {filterEmployeeDetails:''};
                   $scope.gotERDResult = false;
                   $scope.searchFromDatesERD = '';
                   $scope.searchToDatesERD = ''; 
                   
                   if($scope.searchERD.employeeId){
                     var data = {
                               eFmFmClientBranchPO:{branchId:branchId},
                               fromDate:convertDateUTC(fromDate),
                               toDate:convertDateUTC(toDate) ,  
                               employeeId:$scope.searchERD.employeeId,
                               userId:profileId,
                               combinedFacility: combinedFacilityId 
                             };
                       $http.post('services/report/employeeWiseReport/',data).
                                success(function(data, status, headers, config) { 
                                  if (data.status != "invalidRequest") {
                                   $scope.EDGetReportButtonClicked = false;
                                   $scope.fromDate = fromDate;
                                   $scope.toDate = toDate;
                                   $scope.employeeRequestDetailsData = data;
                                   $scope.employeeRequestDetailsDataLength = data.length;
                                   if($scope.employeeRequestDetailsData.length>0){  
                                       $scope.gotERDResult = true;
                                       $scope.searchFromDatesERD = $scope.fromDate;
                                       $scope.searchToDatesERD = $scope.toDate;
                                   }
                                   else{ 
                                       $scope.gotERDResult = false;
                                        ngDialog.open({
                                            template: 'No Data Found. Please Change the Dates.',
                                            plain: true
                                        });

                                   }
                                 }
                       }).error(function(data, status, headers, config) { });

                   }
                   else{
                        var data = {
                               eFmFmClientBranchPO:{branchId:branchId},
                               fromDate:convertDateUTC(fromDate),
                               toDate:convertDateUTC(toDate),
                               userId:profileId,
                               combinedFacility: combinedFacilityId 
                             };
                       $http.post('services/report/employeeWiseReport/',data).
                                success(function(data, status, headers, config) { 
                                  if (data.status != "invalidRequest") {
                                   $scope.EDGetReportButtonClicked = false;
                                   $scope.fromDate = fromDate;
                                   $scope.toDate = toDate;
                                   $scope.employeeRequestDetailsData = data;
                                   $scope.employeeRequestDetailsDataLength = data.length;
                                   if($scope.employeeRequestDetailsData.length>0){  
                                       $scope.gotERDResult = true;
                                       $scope.searchFromDatesERD = $scope.fromDate;
                                       $scope.searchToDatesERD = $scope.toDate;
                                   }
                                   else{ 
                                       $scope.gotERDResult = false;
                                        ngDialog.open({
                                            template: 'No Data Found. Please Change the Dates.',
                                            plain: true
                                        });

                                   }
                                 }
                       }).error(function(data, status, headers, config) { });
                   }
                   
               }   
                   
          }
       };    
       $scope.routeHistory = function(trip, size){
       var modalInstance = $modal.open({
           templateUrl: 'partials/modals/routeHistory.jsp',
           controller: 'routeHistoryCtrl',
           size:size,
           resolve: {
               trip:function(){return trip;}
           }
           }); 
       
           //Here the VendorId will be given to the Vendor from backend
       modalInstance.result.then(function(result){});
        
       };
       
       var isValidDate = function(from, to){
           var fromDate = new Date(from).getTime();
           var toDate = new Date(to).getTime();
           if(fromDate<=toDate){return true;}
           else return false;
       };
       
       //Kilometer Report Click Select on Report Type
       $scope.getVendorOrVehicle = function(reportType){
           $scope.vendorVehicles_KMReport = [];
           if(angular.isObject(reportType)){
               $scope.gotKMResult = false;
               $scope.reportTypeSelected = reportType.value;
               if(reportType.value === "vendor"){
                   var data = {
                      branchId:branchId,
                      userId:profileId,
                      combinedFacility: combinedFacility 
                   };
                  $http.post('services/contract/allActiveVendor/',data).
                        success(function(data, status, headers, config) {
                          if (data.status != "invalidRequest") {
                              angular.forEach(data, function(item){
                                  $scope.vendorVehicles_KMReport.push({'name':item.name, 'Id':item.vendorId});
                              });
                              $scope.vendorVehicles_KMReport.unshift({'name':'All Vendors', 'Id':0});
                              $scope.reportKM.VSelection = $scope.vendorVehicles_KMReport[0];
                            }
                        }).
                        error(function(data, status, headers, config) {
                              // log error
                        });
               }
               else {
                   $scope.vendorVehicles_KMReport.unshift({'name':'All Vehicles', 'Id':0});
                   $scope.reportKM.VSelection = $scope.vendorVehicles_KMReport[0];
               }
           }
       }; 
       
       $scope.viewKMDetails = function(reportSum){
           
           $scope.reportsKMData = [];
            if($scope.reportKM.reportType.value == 'vendor'){
                var data = {
                     eFmFmClientBranchPO:{branchId:branchId},
                     fromDate:convertDateUTC(selectedFrom),
                     toDate:convertDateUTC(selectedTo),
                     vendorId:reportSum.vendorId,
                     userId:profileId,
                     combinedFacility:combinedFacility 
                 };                
                $http.post('services/report/vendorvehiclekm/',data).
                   success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    if(reportSum.viewDetailIsClicked){
                        reportSum.viewDetailIsClicked = false;
                        $scope.viewDetail = false;
                    }
                    else if(!reportSum.viewDetailIsClicked){     
                       $scope.reportsKMData = data;
                       reportSum.viewDetailIsClicked = true;
                       $('.reportSum').addClass("kmSum");
                       $scope.viewDetail = true;
                }
              }
            }).
            error(function(data, status, headers, config) {
                               // log error
                         });   
            }       
       };
       
       //Set the Report Type in Vehicle Driver Attendance Report
       $scope.setReportType_attendance = function(reportType){
       };
       
       $scope.setTripTypeOT = function(type,combinedFacilityId){
           $scope.OTTripType = type.text;
           $scope.OTShiftTimes = $scope.getShiftTime(type.text,combinedFacilityId);
           $scope.onTimeData = [];
           $scope.gotOTResult = false;
       };
       
       $scope.setShiftOT = function(OTShift){
        if (OTShift.value == 0) {
          $scope.dateShites = false;
        } else {
          $scope.dateShites = true;
        }
           $scope.OTShift = OTShift;
           $scope.onTimeData = [];
           $scope.gotOTResult = false;
           

       };
       
       $scope.setVendorOT = function(OTVendors){
           $scope.OTVendor = OTVendors.name;
           $scope.onTimeData = [];
           $scope.gotOTResult = false;
       };

       $scope.setRequestType = function()
       {
         $scope.onTimeData = [];
         $scope.seatUtilData = [];
         $scope.gotOTResult = false;
         $scope.gotSUResult = false;
       }
       $scope.setfeedbackType = function()
       {
         $scope.gotFeedbackResult = false;
         $scope.tripSheetData = [];  
       }       
       $scope.setRequestTypeTS = function()
       {
         $scope.gotTripResult = false;
         $scope.tripSheetData = [];  
       }

       $scope.setviewTypeTripsheet = function(value){
          $scope.viewTypeTripSheet = value.value;
       }
       $scope.setviewTypeOntime = function(value){
        console.log(value);
          $scope.viewTypeOntime = value.value;
       }

       $scope.setRequestTypeNS = function()
       {
            $scope.noShowReportData = [];
            $scope.gotNSResult = false;  
       }
       $scope.setvendorVehicle = function()
       {
            $scope.gotSpeedResult = false;
            $scope.reportsSum = [];
       }
       $scope.setvendorVehicleD = function()
       {
          $scope.reportsSum = [];
       }
       $scope.setTripTypeSU = function(type,combinedFacilityId){
            $scope.SUShiftTimes = $scope.getShiftTime(type.text,combinedFacilityId);
            $scope.seatUtilData = [];

            $scope.gotSUResult = false;
       };

       $scope.setShiftSU = function(type)
       {
            if (type.value == 0) {
          $scope.dateShift = false;
        } else {
          $scope.dateShift = true;
        }
            $scope.seatUtilData = [];
            $scope.gotSUResult = false;
       }
       
       $scope.setTripTypeNS = function(type,combinedFacilityId){
            $scope.NSShiftTimes = $scope.getShiftTime(type.text,combinedFacilityId);
            $scope.noShowReportData = [];
            $scope.gotNSResult = false;
       };
       $scope.setShiftNS = function(type)
       {
            if (type.value == 0) {
          $scope.dateShiftns = false;
        } else {
          $scope.dateShiftns = true;
        }
            $scope.noShowReportData = [];
            $scope.gotNSResult = false;
       }
       $scope.setShiftDistance = function(type)
       {
           $scope.reportsSum = [];
           $scope.gotKMResult = false;
       }
        $scope.setTripTypeRWT = function(type)
       {
          $scope.gotRWTResult = false;
           $scope.reportsRouteWiseTravelData = [];
       }
       
       $scope.setVendorSpeed = function(vendor,combinedFacilityId){ 

          if(combinedFacilityId == undefined || combinedFacilityId.length == 0){
            combinedFacilityId = combinedFacility;
          }else{
            combinedFacilityId = String(combinedFacilityId);
          }

          $scope.gotSpeedResult = false;
          $scope.reportsSpeedData = [];
          $scope.vendorVehicles_SpeedReport = [];
          if (vendor.Id == 0) {
             $scope.vendorVehicles_SpeedReport.push({'name':'All Vehicles', 'Id':0})
            $scope.searchSpeed.VSelection = $scope.vendorVehicles_SpeedReport[0];
          } else {
              var data = {
                       branchId:branchId,
                       vendorId:vendor.Id,
                       userId:profileId,
                       combinedFacility:combinedFacilityId 
            };
          
            $http.post('services/vehicle/actualvehiclelist/',data).
                 success(function(data, status, headers, config) {
                  if (data.status != "invalidRequest") {
                if (data.length) {
                    angular.forEach(data, function(item){
                           $scope.vendorVehicles_SpeedReport.push({'name':item.vehicleNum, 'Id':item.vehicleId});
                        });
                       $scope.searchSpeed.VSelection = $scope.vendorVehicles_SpeedReport[0];
                  } else {
                   ngDialog.open({
                      template: 'No Vehicles Found',
                      plain: true
                      });
                   $scope.vendorVehicles_SpeedReport.push({'name':'All Vehicles', 'Id':0})
                    $scope.searchSpeed.VSelection = $scope.vendorVehicles_SpeedReport[0];
                  }
                }

                 }).
                 error(function(data, status, headers, config) {
                               // log error
            });
          }
          
          
         
       };
        $scope.editFeedback = function(feedback, index){
           var modalInstance = $modal.open({
                   templateUrl: 'partials/modals/editFeedbackModal.jsp',
                   controller: 'editFeedbackCtrl',
                   size:'lg',
                   resolve: {
                       feedback : function(){return feedback;},
                   }
            }); 
            modalInstance.result.then(function(result){
                          if (result) {
                    feedback.empDescription = result.empDescription;
                    feedback.remarks = result.remarks;
                          } else {

                          }
            });
        };
        $scope.editTravelDistance = function(trip,index){
           $scope.Distance = trip.editedDistance;
           var modalInstance = $modal.open({
                   templateUrl: 'partials/modals/editTravelDistanceModal.jsp',
                   controller: 'editTravelDistanceCtrl',
                   resolve: {
                       trip : function(){return trip;},
                   }
            }); 
            modalInstance.result.then(function(result){
              if(result.updatedDistance == undefined){
                  $scope.tripSheetData[0].tripDetail[index].travelledDistance = trip.travelledDistance;
              }else{
                  $scope.tripSheetData[0].tripDetail[index].travelledDistance = result.updatedDistance;
              }
              
            });
        };
        $scope.cancelTravelDistance = function(trip){
         
            trip.isTravelDistanceEdit = false;
        };
        $scope.saveTravelDistance = function(trip){
                var modalInstance = $modal.open({
                   templateUrl: 'partials/modals/remarksTripSheetTravelDist.jsp',
                   controller: 'remarksTripSheetTravelDistCtrl',
                   size:'sm',
                   resolve: {
                       oldTravelledDist : function(){return trip.orginalTravelledDistance ;},
                       newTravelledDistance: function(){return trip.editedDistance;}
                   }
                   }); 

                   modalInstance.result.then(function(result){
                       trip.isTravelDistanceEdit = false;
                       trip.orginalTravelledDistance = trip.editedDistance;
                       var data = {
                        eFmFmClientBranchPO:{branchId:branchId},
                      assignRouteId:trip.routeId,
                      editedTravelledDistance:trip.editedDistance,
                      remarksForEditingTravelledDistance:result,
                      userId:profileId,
                      combinedFacility:combinedFacility 
                    };
                     $http.post('services/approval/editDistanceWithRemarks/',data).
                          success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                              ngDialog.open({
                              template: 'Distance added succesfully.It will add in actual travelled distance after admin approval',
                              plain: true
                              });
                            }
                  
                          }).
                          error(function(data, status, headers, config) {
                            // log error
                          }); 
                       
                   });   
              
                
                }, function(){
                trip.travelledDistance = trip.orginalTravelledDistance;
                trip.isTravelDistanceEdit = false;
                  
        };
//****************************************EXCEL DOWNLOADS METHODS*******************************************//
       $scope.saveInExcel = function(){
           if($scope.currentTab == 'tripSheet'){
               var blob = new Blob([document.getElementById('exportableTripSheet').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "TripSheetReport.xls");               
           }

           if($scope.currentTab == 'travelReport'){
               var blob = new Blob([document.getElementById('exportableTravelReport').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob,"onTimeReport.xls");
           }

           if($scope.currentTab == 'onTime'){
               var blob = new Blob([document.getElementById('exportableOnTime').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob,"onTimeReport.xls");
           }
           
           if($scope.currentTab == 'escortReport'){
               var blob = new Blob([document.getElementById('exportableEscortReport').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "EscortReport.xls");
           }
           
           if($scope.currentTab == 'vehicleDriverAttendence'){    
               var blob = new Blob([document.getElementById('exportTableVDAttendance').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "Vehicle/DriverAttendance.xls");
          }
           
          if($scope.currentTab == 'driverWorkingHours'){    
               var blob = new Blob([document.getElementById('exportTableDriverWH').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "DriverWorkingHours.xls");
            }
           
           if($scope.currentTab == 'driverDrivingHours'){   
               var blob = new Blob([document.getElementById('exportTableDriverDH').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "DriverDrivingHours.xls");}
           
           if($scope.currentTab == 'speed'){    
               var blob = new Blob([document.getElementById('exportTableSpeed').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "SpeedReport.xls");
           }
           
           if($scope.currentTab == 'routeWiseTravel'){    
               var blob = new Blob([document.getElementById('exportTableRWT').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "RoutewiseTravelTime.xls");
           }           
           
         /*  if($scope.currentTab == 'noShow'){    
               var blob = new Blob([document.getElementById('exportableNoShow').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "No Show.xls");
           } */        
           
           if($scope.currentTab == 'employeeDetails'){    
               var blob = new Blob([document.getElementById('exportableEmployeeDetails').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "Employee Details Report.xls");
           }
           
           if($scope.currentTab == 'seatUtil'){
               $scope.reportSUExcel = [];
               if($scope.searchSU.SUShift.text == 'All Shifts'){
                   angular.forEach($scope.seatUtilData, function(item){
                        $scope.reportSUExcel.push({'Trip Type':$scope.SUresultTripType,
                                                   'Date':item.tripDates,
                                                   'Number of Vehicle Used':item.totalUsedVehicles,
                                                   'Total Opportunity': item.totalVehicleCapacity,
                                                   'PAX':item.totalEmployeesPickedDropCount,
                                                   'SU%':item.utilizedSeatPercentage});
                   });
               }
               else{
                   angular.forEach($scope.seatUtilData, function(item){
                        $scope.reportSUExcel.push({'Trip Type':$scope.SUresultTripType,
                                                   'Shift Time':item.tripDates,
                                                   'Number of Vehicle Used':item.totalUsedVehicles,
                                                   'Total Opportunity': item.totalVehicleCapacity,
                                                   'PAX':item.totalEmployeesPickedDropCount,
                                                   'SU%':item.utilizedSeatPercentage});
                   });
               }
               var sheetLabel = $scope.SUresultTripType+" " + $scope.SUReportTitle;
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("SeatUtilization.xlsx",?) FROM ?',[opts,[$scope.reportSUExcel]]);
               
           }
          if($scope.currentTab == 'noShow'){ 
               $scope.reportNSExcel = [];
               if($scope.searchNSByEmployeeId){
                  angular.forEach($scope.noShowReportData, function(item){
                 $scope.reportNSExcel.push({'Trip Type':$scope.NSResultTripType,
                                              'Date':item.tripDates, /*01/08/2016 Change with Dates*/
                                              'Shift Time': item.tripDates, 
                                              'Employee Id':item.totalUsedVehicles,
                                              'Employee Name':item.totalEmployeesPickedDropCount});
                  });
               }
              else{
                  if($scope.searchNS.NSShift.text == 'All Shifts'){
                       angular.forEach($scope.noShowReportData, function(item){
                           $scope.reportNSExcel.push({'Trip Type':$scope.NSResultTripType,
                                                       'Date':item.tripDates,
                                                       'Trips':item.totalUsedVehicles,
                                                       'PAX':item.totalEmployeesPickedDropCount,
                                                       'No Show':item.totalEmployeesNoShowCount});
                       });
                    }
                   else if($scope.searchNS.NSShift.text == 'By Shifts'){
                       angular.forEach($scope.noShowReportData, function(item){
                           $scope.reportNSExcel.push({'Trip Type':$scope.NSResultTripType,
                                                       'Shift Time':item.tripDates,
                                                       'Employee Id':item.totalUsedVehicles,
                                                       'Employee Name':item.totalEmployeesPickedDropCount});
                       });

                   }
                   
                  else if(!($scope.searchNS.NSShift.text == 'By Shifts') && !($scope.searchNS.NSShift.text == 'All Shifts')){
                      angular.forEach($scope.noShowReportData, function(item){
                           $scope.reportNSExcel.push({'Trip Type':$scope.NSResultTripType,                                                     
                                                       'Employee Id':item.totalUsedVehicles,
                                                       'Employee Name':item.totalEmployeesPickedDropCount});
                       });

                   }   
                  
                  
              }
              var sheetLabel = $scope.NSResultTripType+" " + $scope.NSReportTitle;
               var opts = [{sheetid:sheetLabel,header:true}];
              alasql('SELECT INTO XLSX("noShow.xlsx",?) FROM ?',[opts,[$scope.reportNSExcel]]);
          }
           
           if($scope.currentTab == 'kmReport'){
               $scope.reportKMExcel = [];
               if($scope.searchKMType == 'vendor'){
                   if($scope.distanceShiftType == 'All Shifts' && $scope.reportSelectionKM == 'All Vendors'){
                        angular.forEach($scope.reportsSum, function(item){
                            $scope.reportKMExcel.push({'Date':item.date,
                                                       'Shift Time':item.shiftTime,
                                                       'Vendor Name':item.vendorName,
                                                       'Vehicle Number':item.vehicleNumber,
                                                       'Type of Vehicle':item.vehicleType,
                                                       /*'Total Opportunity':item.totalApportunity,*/
                                                       'Utilized KMs':item.travelledDistance});
                        });
                   }
                   else{
                        angular.forEach($scope.reportsSum, function(item){
                            $scope.reportKMExcel.push({'Date':item.date,
                                                       'Vendor Name':item.vendorName,
                                                       'Vehicle Number':item.vehicleNumber,
                                                       'Type of Vehicle':item.vehicleType,
                                                       /*'Total Opportunity':item.totalApportunity,*/
                                                       'Utilized KMs':item.travelledDistance});
                        });
                   }
               }
               else{
                    angular.forEach($scope.reportsSum, function(item){
                        $scope.reportKMExcel.push({'Date':item.date,
                                                   'Vendor Name':item.vendorName,
                                                   'Vehicle Number':item.vehicleNumber,
                                                   'Type of Vehicle':item.vehicleType,
                                                   /*'Total Opportunity':item.totalApportunity,*/
                                                   'Utilized KMs':item.travelledDistance});   
                    });
               }
               
               var sheetLabel = 'Distance Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("distance_KM.xlsx",?) FROM ?',[opts,[$scope.reportKMExcel]]);   
           }
           
           if($scope.currentTab == 'SMSReport'){
               $scope.reportSMSExcel = [];
               angular.forEach($scope.reportsSMSData, function(item){
                   $scope.reportSMSExcel.push({
                                              'Travelled Date':item.travelledDate,
                                              'Employee Id':item.employeeId, 
                                              'Employee Mobile':item.employeeNumber,
                                              'Shift Time':item.shiftTime,
                                              'Route Name':item.routeName,
                                              'Trip Type':item.tripType,
                                              'Allocation Msg(Date)':item.allocationMsgDeliveryDate,
                                              'Est. 15mins Msg(Date)':item.eat15MinuteMsgDeliveryDate,
                                              'Cab Delay Msg(Date)':item.cabDelayMsgDeliveryDate,
                                              'Reached Msg(Date)':item.reachedMsgDeliveryDate,
                                              'No Show Msg(Date)':item.noShowMsgDeliveryDate});
               }); 
               var sheetLabel = 'SMS Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("smsReport.xlsx",?) FROM ?',[opts,[$scope.reportSMSExcel]]);               
           }   
        };
       
       //FUNCTION : Get All Shifts
       $scope.getAllShiftTime = function(){ 
         $scope.tripTimes = [];
           $scope.tripTimeData = [];          
         var data = {
             efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}},
             eFmFmEmployeeRequestMaster:{efmFmUserMaster:{userId:profileId}},
             userId:profileId,
             combinedFacility:combinedFacility 
              };  
           $http.post('services/trip/shiftime/',data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                          $scope.tripTimeData = data.shift;  
                          angular.forEach($scope.tripTimeData, function(item){
                          $scope.tripTimes.push({'text':item.shiftTime, 'value':item.shiftTime});
                              
                      });
                           $scope.tripTimes.unshift({'text':'All Shifts', 'value':0});
                           $scope.tripTimes.unshift({'text':'By Shifts', 'value':1});
                    }
                    }).
                error(function(data, status, headers, config) {
                  // log error
                });
            return $scope.tripTimes;
       };
    
       //FUNCTION : Get Shift Time by Trip Type
       $scope.getShiftTime = function(tripType,combinedFacilityId){

        if(combinedFacilityId == undefined || combinedFacilityId.length == 0){
            combinedFacilityId = combinedFacility;
        }else{
            combinedFacilityId = String(combinedFacilityId);
        }
         $scope.tripTimes = [];
           $scope.tripTimeData = [];
           
                var data = {
                       efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}},
                       eFmFmEmployeeRequestMaster:{efmFmUserMaster:{userId:profileId}},
                       tripType:tripType,
                       userId:profileId,
                       combinedFacility:combinedFacilityId
                          }; 
                $http.post('services/trip/tripshiftime/',data).
                    success(function(data, status, headers, config) {
                      if (data.status != "invalidRequest") {
                          $scope.tripTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
                          angular.forEach($scope.tripTimeData, function(item){
                          $scope.tripTimes.push({'text':item.shiftTime, 'value':item.shiftTime});
                              
                      });
                           $scope.tripTimes.unshift({'text':'All Shifts', 'value':0});
                           $scope.tripTimes.unshift({'text':'By Shifts', 'value':1});
                      }
                    }).
                error(function(data, status, headers, config) {
                  // log error
                });
            return $scope.tripTimes;
       };
      
       //FUNCTION : Get aLL aCTIVE vENDORS to Populate the drop Down
       $scope.getAllVendors = function(){
         $scope.allVendors = [];
          var data = {
                      branchId:branchId,
                      userId:profileId,
                      combinedFacility: combinedFacility 
                   };
          $http.post('services/contract/allActiveVendor/',data).
                success(function(data, status, headers, config) {
                  if (data.status != "invalidRequest") {
                      angular.forEach(data, function(item){
                          $scope.allVendors.push({'name':item.name, 'Id':item.vendorId});
                      });
                      $scope.allVendors.unshift({'name':'All Vendors', 'Id':0});
                  }
                }).
                error(function(data, status, headers, config) {
                      // log error
                });
           return $scope.allVendors;
       }; 
   }
   init(); 
};  
    
var routeHistoryCtrl = function($scope, $modal, $modalInstance, $state, $http, $timeout, trip, ngDialog){
    $scope.gotRouteHistory = false;
    $scope.routeId = trip.routeId;
    $scope.tripAssignDate = trip.tripAssignDate;
    $scope.tripStartDate = trip.tripStartDate;
    $scope.tripCompleteDate = trip.tripCompleteDate;
    
    $scope.getRouteHistory = function(){
        var data = {
            eFmFmClientBranchPO:{branchId:branchId},
            assignRouteId:trip.routeId,
            userId:profileId,
            combinedFacility:combinedFacility 
        }; 
      $http.post('services/report/routeHistory/',data).
        success(function(data, status, headers, config) {
          if (data.status != "invalidRequest") {
            $scope.routeHistoryData = data;
            if($scope.routeHistoryData.length > 0){
                $scope.gotRouteHistory = true;
            }
            else{$scope.gotRouteHistory = false;}
          }
        }).
        error(function(data, status, headers, config) {});    
    };
    
    $scope.getRouteHistory();   

    $scope.routeHistoryDetails = function()
    {
         var modalInstance = $modal.open({
           templateUrl: 'partials/modals/routeHistoryMapView.jsp',
           controller: 'routeHistoryMapViewCtrl',
           size:'lg',
           resolve: {
            routeId:function(){return trip.routeId;},
            tripAssignDate:function(){return trip.tripAssignDate;},
            tripStartDate:function(){return trip.tripStartDate;},
            tripCompleteDate:function(){return trip.tripCompleteDate;}
           }
           }); 
       
         modalInstance.result.then(function(result){});
          
   };
  
        
    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
    
    $scope.saveInExcel = function(){

      var dataObj = {
                          eFmFmClientBranchPO:{branchId:branchId},
                          assignRouteId:trip.routeId,
                          userId:profileId,
                          combinedFacility:combinedFacility
                        };

       $http({
             url : 'services/report/routeHistoryDownload/',          
             method: "POST",
             data: dataObj, 
             headers: {
                'Content-type': 'application/json'
             },
             responseType: 'arraybuffer'
         }).success(function (data, status, headers, config) {
             var blob = new Blob([data], {
             });
             saveAs(blob, 'RouteHistory' + '.xlsx');
         }).error(function (data, status, headers, config) {
             alert("Download Failed")
         });

    };
};
    
var remarksTripSheetTravelDistCtrl = function($scope, $modalInstance, $state, $http, $timeout, oldTravelledDist, newTravelledDistance, ngDialog){
    $scope.newValue = newTravelledDistance;
    $scope.oldValue = oldTravelledDist;
    
    $scope.save = function(remarks){
        $timeout(function() {$modalInstance.close(remarks), 3000});
    };
         
    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var onTimeReportsViewModalCtrl = function($scope, $modalInstance, $state, $http, $timeout, OTA, ngDialog,Triptype, $rootScope){
    
    $scope.onTimeviewData = [];
    $scope.delayTripViewData = [];
    $scope.delayBeyondTimeData = [];
    $scope.noShowviewData = [];

    $scope.onTimeview = OTA.onTimeview;
    $scope.delayTripView = OTA.delayTripView;
    $scope.delayBeyondTime = OTA.delayBeyondTime;
    $scope.noShowview = OTA.noShowView;
    $scope.tripType = Triptype;
    
    angular.forEach($scope.onTimeview, function(value, key) {
                  $scope.onTimeviewData.push(value);
    }); 

    $scope.onTimeTripType = [];

    $scope.onTimeTripType.push($scope.tripType);

    angular.forEach($scope.delayTripView, function(value, key) {
                  $scope.delayTripViewData.push(value);
    });

    angular.forEach($scope.delayBeyondTime, function(value, key) {
                  $scope.delayBeyondTimeData.push(value);
    });

    angular.forEach($scope.noShowview, function(value, key) {
                  $scope.noShowviewData.push(value);
    });
    //CLOSE BUTTON FUNCTION
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
};


var seatUtilReportsViewModalCtrl = function($scope, $modalInstance, $state, $http, $timeout, SU, ngDialog){
    
    $scope.SUnumberOfVehiclesUsedData = [];
    $scope.SUPickupPaxData = [];

    $scope.SUnumberOfVehiclesUsed = SU.VehiclesView;
    $scope.SUPickupPax = SU.PickupPaxView;

    angular.forEach($scope.SUnumberOfVehiclesUsed, function(value, key) {
                  $scope.SUnumberOfVehiclesUsedData.push(value);
    });  

    angular.forEach($scope.SUPickupPax, function(value, key) {
                  $scope.SUPickupPaxData.push(value);
    });
    
    //CLOSE BUTTON FUNCTION
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
};

var noShowReportsViewModalCtrl = function($scope, $modalInstance, $state, $http, $timeout, NS, ngDialog){

    $scope.pickupTripsData = [];
    $scope.pickupPaxData = [];
    $scope.noShowData = [];

    $scope.pickupTrips = NS.totalUsedVehiclesView;
    $scope.pickupPax = NS.PickedDropCountView;
    $scope.noShow = NS.noShowView;

    angular.forEach($scope.pickupTrips, function(value, key) {
                  $scope.pickupTripsData.push(value);
    });  

    angular.forEach($scope.pickupPax, function(value, key) {
                  $scope.pickupPaxData.push(value);
    });  

    angular.forEach($scope.noShow, function(value, key) {
                  $scope.noShowData.push(value);
    });  

    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var dynamicDetailsReportCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, dynamicData, fromDate,toDate, searchType, ngDialog){

  $scope.dynamicData = dynamicData;

    if(dynamicData == undefined)
    {
              ngDialog.open({
                        template: 'Please Select The Dynamic Paramenters',
                        plain: true
                        });
    } 

  $scope.dynamicDataLength = dynamicData.length;


  $scope.fromDate  = fromDate;
  $scope.toDate = toDate;
  $scope.searchType = searchType;

  var dataShow = {
              dynamicData: $scope.dynamicData
            }

  $scope.dynamicDataSubmit = function()
  {
    $rootScope.$emit('dynamicDataEvent', dataShow);
    $modalInstance.dismiss('cancel');
  }

    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var viewEmployeeDetailsCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, data){

          var data = 
            {
              assignRouteId : data.routeId,
              employeeIdFlg : data.employeeId,
              employeeNameFlg : data.empName,
              hostMobileNumberFlg : data.hostMobileNumber,
              emailIdFlg : data.empEmailId,
              employeeMobileNoFlg : data.mobileNumber,
              empLoacationFlg : data.employeeLocation,
              fifteenMinuteMsgFlg : data.fifteenMsg,
              noshowMsgFlg : data.noShowMsg,
              reachedMsgFlg : data.reachedMsg,
              cabDelayMsgFlg : data.cabDelayMsg,
              cabReachedTimeFlg : data.cabReachedTime,
              boardingStatusFlg : data.boardingStatus,
              pickUpTimeFlg : data.pickUpTime,
              boardingTimeFlg : data.boardingTime,
              allocationMsgFlg : data.alloccationMessage,
              userId:profileId,
              combinedFacility:combinedFacility 
            };
            $scope.dataDetailsEmployee = data;
              $http.post('services/report/dynamicReportEmployeeDetails/',data).
                success(function(data, status, headers, config) {  
                  if (data.status != "invalidRequest") {
                               $scope.employeeDetailsData = data;
                               $scope.employeeDetailsDataLengthDR = data.length;
                  }
                }).
                error(function(data, status, headers, config) {
                  // log error
                 
                });  

    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var viewOdometerTripSheetCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, trip){
  
    $scope.startOdometer = Number(trip.startKm);
    $scope.endOdometer = Number(trip.endKm);

    if($scope.startOdometer == 0)
    {
      $scope.startOdometer = '';
    }

    if($scope.endOdometer == 0)
    {
      $scope.endOdometer = '';
    }

    $scope.saveOdometer = function(startOdometer,endOdometer)
      {
        if(startOdometer > endOdometer)
        {
          ngDialog.open({
                template : 'Start Odometer should not be greater than End Odometer',
                plain : true
              });
          return false;
        }

        $scope.distanceOdomter = endOdometer - startOdometer;
        
        var distanceGlobal = {
          distanceOdomter : Number($scope.distanceOdomter),
          odometerStartKm : startOdometer,
          odometerEndKm : endOdometer
        }

        var data = {
          eFmFmClientBranchPO : {branchId : branchId},
          odometerStartKm : String(startOdometer),
          odometerEndKm: String(endOdometer),
          assignRouteId: trip.routeId,
          userId:profileId,
          combinedFacility:combinedFacility 
        };     

        $http.post('services/report/updateOdometerReading', data).success(
            function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
              ngDialog.open({
                template : 'Odometer Saved Successfully',
                plain : true
              });

            $timeout(function() {
                         $modalInstance.close(distanceGlobal)}, 3000);
            }
                     }).
                     error(function(data, status, headers, config) {
                       // log error
            });
      }

    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var createOdometerReportCtrl = function($scope, $modal, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog){
 
    $scope.hstep = 1;
    $scope.mstep = 1;
    $scope.odometer = {};
    $scope.shiftsTime = [];
    $scope.ismeridian = false;
    $scope.reportList = [];
    $scope.odometer.date = new Date();
    $scope.allInspectionVendors = [];
    $scope.shiftTime = 'preDefineShiftTime';

        // Get All vendors

    $scope.getAllVendors = function(){
         $scope.allVendors = [];
          var data = {
                      branchId:branchId,
                      userId:profileId,
                      combinedFacility:combinedFacility 
                   };
          $http.post('services/contract/allActiveVendor/',data).
                success(function(data, status, headers, config) {
                  if (data.status != "invalidRequest") {
                      angular.forEach(data, function(item){
                          $scope.allInspectionVendors.push({'name':item.name, 'Id':item.vendorId});
                      });
                  }
                }).
                error(function(data, status, headers, config) {
                      // log error
                });
           return $scope.allVendors;

       };
       $scope.getAllVendors();

    $scope.setVendor = function(vendorSelected)
    {
      var data = {vendorId:vendorSelected.Id,
               branchId:branchId,userId:profileId,combinedFacility:combinedFacility }; 
               $http.post('services/contract/getCheckInEntity/',data).
                 success(function(data, status, headers, config) {
                  if (data.status != "invalidRequest") {
                    $scope.allVehiclesInpectionForm = [];
                    angular.forEach(data, function(item){  
                                    $scope.allVehiclesInpectionForm.push({vehicleNumber:item.vehicleNumber, Id:item.vehicleId, vehicleType: item.vehicleType,driverName: item.driverName});
                               });
                  }
                  }).
                error(function(data, status, headers, config) {
                      // log error
                });

       };


 
    $scope.tripTypes = [ {
      'value' : 'PICKUP',
      'text' : 'PICKUP'
    }, {
      'value' : 'DROP',
      'text' : 'DROP'
    } ];

    $scope.odometer.tripType = {'value':'PICKUP', 'value':'PICKUP'};

    $scope.selectionTypeData = [ {
      'value' : 'Vehicle Wise',
      'text' : 'Vehicle Wise'
    }, {
      'value' : 'Shift Time Wise',
      'text' : 'Shift Time Wise'
    } ];

    $scope.odometer.selectionType = {'text':'Shift Time Wise', 'value':'Shift Time Wise'};


    //Convert the dates in DD-MM-YYYY format
    var convertDateUTC = function(date) {
      var convert_date = new Date(date);
      var currentMonth = date.getMonth() + 1;
      var currentDate = date.getDate();
      if (currentDate < 10) {
        currentDate = '0' + currentDate;
      }
      if (currentMonth < 10) {
        currentMonth = '0' + currentMonth;
      }
      return currentDate + '-' + currentMonth + '-'
          + convert_date.getFullYear();
    };

    //Initialize TimePicker to 00:00
    var timePickerInitialize = function() {
      var d = new Date();
      d.setHours(00);
      d.setMinutes(0);
      $scope.assignCab.createNewAdHocTime = d;
    };

    $scope.format = 'dd-MM-yyyy';
    $scope.dateOptions = {
      formatYear : 'yy',
      startingDay : 1,
      showWeeks : false,
    };

    $scope.openDownloadDateCal = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $timeout(function() {
        $scope.datePicker = {
          'openeddownloadDate' : true
        };
      })
    };

    //Initialize TimePicker to 00:00
    var initialize = function() {
      var d = new Date();
      d.setHours(00);
      d.setMinutes(0);
      $scope.odometer.createNewAdHocTime = d;
      $scope.odometer.tripType = {
        'value' : 'PICKUP',
        'text' : 'PICKUP'
      };

      $scope.setTripType($scope.odometer.tripType);
      $scope.odometer.shiftTime = $scope.shiftsTime[0];
      $scope.shiftTime = 'preDefineShiftTime';
      $scope.odometer.date = new Date();
    }
        

    $scope.selectShiftTimeRadio = function(shiftTime) {
      $scope.typeOfShiftTimeSelected = shiftTime;
      $('.btn-link').addClass('noPointer');
    };

    $scope.selectShiftTimeRadio2 = function(shiftTime) {
      $scope.typeOfShiftTimeSelected = shiftTime;
      $('.btn-link').removeClass('noPointer');
    };

    $scope.setTripTypeOnload = function()
    {
        var data = {
          efmFmUserMaster : {
            eFmFmClientBranchPO : {
              branchId : branchId
            }
          },
          eFmFmEmployeeRequestMaster : {
            efmFmUserMaster : {
              userId : profileId
            }
          },
          tripType : 'PICKUP',
          userId:profileId,
          combinedFacility:combinedFacility 
        };
       
        $http.post('services/trip/tripshiftime/', data).success(
            function(data, status, headers, config) {
              if (data.status != "invalidRequest") {
              $scope.shiftsTime = data.shift;
              $scope.odometer.shiftTime = $scope.shiftsTime[0];
            }

            }).error(function(data, status, headers, config) {
        });
    };

    $scope.setTripTypeOnload();

    $scope.setTripType = function(tripType) {
      if (angular.isObject(tripType)) {
        var data = {
          efmFmUserMaster : { 
            eFmFmClientBranchPO : {
              branchId : branchId
            }
          },
          eFmFmEmployeeRequestMaster : {
            efmFmUserMaster : {
              userId : profileId
            }
          },
          tripType : tripType.value,
          userId:profileId,
          combinedFacility:combinedFacility 
        };
        
        $http.post('services/trip/tripshiftime/', data).success(
            function(data, status, headers, config) {
              if (data.status != "invalidRequest") {
              $scope.shiftsTime = data.shift;
              $scope.odometer.shiftTime = $scope.shiftsTime[0];
            }

            }).error(function(data, status, headers, config) {
        });
      } else {
        $scope.shiftsTime = '';
      }
      $scope.odometer.shiftTime = $scope.shiftsTime[0];
    };  

    $scope.createOdometerData = function(odometer,shiftTime)
    {

      if (shiftTime == 'preDefineShiftTime') {
        $scope.timeSelected = odometer.shiftTime.shiftTime;
      } else {
        var fullDate = new Date(odometer.createNewAdHocTime);
        var time = fullDate.getHours() + ':' + fullDate.getMinutes()
            + ':00';
        $scope.timeSelected = time;
      }

      if(odometer.selectionType.value == 'Shift Time Wise')
      {
        var dataObj = {
        eFmFmClientBranchPO:{branchId:branchId},       
        tripType : odometer.tripType.value,
        time : $scope.timeSelected,        
        fromDate:convertDateUTC(odometer.date),
        toDate:convertDateUTC(odometer.date),
        vehicleId : 0,
        userId:profileId,
        combinedFacility:combinedFacility 
        };
      }
      if(odometer.selectionType.value == 'Vehicle Wise')
      {
        var dataObj = {
        eFmFmClientBranchPO:{branchId:branchId},       
        tripType : odometer.tripType.value,
        time :"00:00",        
        fromDate:convertDateUTC(odometer.date),
        toDate:convertDateUTC(odometer.date),
        vehicleId : odometer.vehicleSelected.Id,
        userId:profileId ,
        combinedFacility:combinedFacility
        };
      }

        $http.post('services/report/tripDetailsforUpdate', dataObj)
            .success(
                function(data, status, headers, config) {
                  
                 if (data.status != "invalidRequest") {
                 
                 if(data.length < 1)
                 {
                     ngDialog.open({
                      template : 'No Data Found. Please Change the Selection.',
                      plain : true
                    });
                  return false;
                 }

                  var modalInstance = $modal.open({
                  templateUrl: 'partials/modals/reports/createOdometerTableView.jsp',
                  controller: 'createOdometerTableViewCtrl',
                  size:'lg',
                  backdrop:'static',
                  resolve: {
                  data: function(){return data;},
                  dataObj: function(){return dataObj;},
                  }
                  }); 
                }
                  
                }).error(
                function(data, status, headers, config) {
                  // log error
      });

    }

    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var createOdometerTableViewCtrl = function($scope, $interval,  $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, data,dataObj){
 
  $scope.viewOdometerDetails = data;
  $scope.viewOdometerLength = data.length;
  $scope.viewKMDetails = dataObj;
  var results = [];
  var odometerDatas = [];
  $scope.isGrey = [];
  $scope.odometerFormDisbled = false;
  
  
  $scope.odometerIndex = function (argument, index) {
    $scope.odometerShow = false;
    var odometerFlg = Number(argument.startKm) >= Number(argument.endKm);
    

    if($scope.isGrey[index] == 'error')
    {
      $scope.isGrey[index] = $scope.isGrey[index]=='error'?'':'error';
      $scope.odometerFormDisbled = true;
    }
    if(odometerFlg)
    {
      $scope.odometerShow = true;
      $scope.odometerFormDisbled = true;
      $scope.isGrey[index] = $scope.isGrey[index]=='error'?'':'error';
      return false;
    }
    

    if(!odometerFlg)
    {
      if($scope.isGrey[index] == undefined || $scope.isGrey[index] == '')
      {
        $scope.odometerFormDisbled = false;
        $scope.isGrey[index] = $scope.isGrey[index]=='errorGreen'?'':'errorGreen';
      }
    
    //$scope.isGrey[index] = $scope.isGrey[index]=='errorGreen'?'':'errorGreen';
    results.push.apply(results, [{assignRouteId: String(argument.routeId) ,odometerStartKm: argument.startKm , odometerEndKm: argument.endKm}]);
    $rootScope.resultData = results;
    }
   
  }


      var datas = data;

      $scope.isGrey = [];

      $scope.updateAllOdometerData = function (odometer) {

        function hash(o){
          return o.assignRouteId;
        }    

        var hashesFound = {};

        $rootScope.resultData.forEach(function(o){
            hashesFound[hash(o)] = o;
        })

        var results = Object.keys(hashesFound).map(function(k){
          return hashesFound[k];
        })

        $rootScope.resultData = results;
        
        datas.forEach(function(v, i){
            v.startKm = datas[i].startKm;
            v.endKm = datas[i].endKm;
        });

         var data = {
            eFmFmClientBranchPO : {branchId : branchId},
            tripSheetValues : $rootScope.resultData ,
            userId:profileId ,
            combinedFacility:combinedFacility      
        };    
 
        $http.post('services/report/bulkUpdateOdometerReading', data).success(
            function(data, status, headers, config) {
              if (data.status != "invalidRequest") {
              odometer.odoDistance =  odometer.startKm - odometer.endKm;
             
                  ngDialog.open({
                    template : 'Odometer Saved Successfully',
                    plain : true
                  });

              $timeout(function() {
                         $modalInstance.close()}, 3000);
            }
                     }).
                     error(function(data, status, headers, config) {
                       // log error
            });
       
      };

  //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var routeHistoryMapViewCtrl = function($scope, routeId,tripAssignDate,tripStartDate,tripCompleteDate, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog){
       $scope.isloaded = false;
       $scope.singleServiceData;
       $scope.isloaded = true;
       $scope.routeId = routeId;
       $scope.tripAssignDate = tripAssignDate;
       $scope.tripStartDate = tripStartDate;
       $scope.tripCompleteDate = tripCompleteDate;
       $rootScope.routeIdData = routeId; // this Route id used for History route controller

    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var editTravelDistanceCtrl = function($scope,$rootScope, $modalInstance, $state, $http, $timeout, ngDialog, trip){
    $scope.addKmRequired = false;
    $scope.subtractionKmRequired = false;

    $scope.travelDistanceEditType = [ {
          'value' : 'addition',
          'text' : 'Addition'
      }, {
          'value' : 'subtraction',
          'text' : 'Subtraction'
    } ];

    $scope.travelDistance = $scope.travelDistanceEditType[0];
    $scope.settravelDistanceEditType = function(value){
        if(value.text == 'Addition'){
            $scope.addKmRequired = true;
            $scope.subtractionKmRequired = false;
        }else{
            $scope.addKmRequired = false;
            $scope.subtractionKmRequired = true;
        }
    }

    $scope.addOrSubtractDistanceKm = function(value, addOrSubtractDistanceValue,remarks){
      if(value == 'add'){
          var EditDistanceType = 'A';
          var DistanceType = "Added";
      }else{
          var EditDistanceType = 'S';
          var DistanceType = "Subtracted";
      }

        if(userRole == 'superadmin' || userRole == 'admin'){
            var serviceUrl = "services/approval/editDistanceByAdmin/";
            var approvalFlg = 'Y';
        }else{
            var serviceUrl = "services/approval/editDistanceWithRemarks/";
            var approvalFlg = 'N';
        } 

          var data = {
                      eFmFmClientBranchPO:{branchId:branchId},
                      assignRouteId:trip.routeId,
                      editedTravelledDistance:addOrSubtractDistanceValue,
                      remarksForEditingTravelledDistance:remarks,
                      editDistanceType:EditDistanceType,
                      userId:profileId, 
                      approvalFlg:approvalFlg,
                      combinedFacility:combinedFacility
                    };
                    
                    $http.post(serviceUrl,data).
                          success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                              if(userRole == 'superadmin' || userRole == 'admin'){
                                    ngDialog.open({
                                      template: 'Distance ' + DistanceType +' added succesfully.',
                                      plain: true
                                    });
                                     $timeout(function() {$modalInstance.close(data)}, 3000);

                              }else{
                                    ngDialog.open({
                                      template: 'Distance ' + DistanceType + ' succesfully.It will add in actual travelled distance after admin approval',
                                      plain: true
                                    });
                                    $timeout(function() {$modalInstance.close(data)}, 3000);
                              }
                            }
                          }).
                          error(function(data, status, headers, config) {
                            // log error
                          }); 
    }


    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};
var editFeedbackCtrl = function($scope,$rootScope, $modalInstance, $state, $http, $timeout, ngDialog, feedback){
       $scope.feedbackStatus = [ {'value':"open", 'text':'Open'},
                                          {'value':"inprogress", 'text':'In-Progress'},
                                            {'value':"resolved", 'text':'Resolved'}];
             $scope.assignFeedbackTypes = [ {'value':"routing", 'text':'Routing'},
                                          {'value':"tracking", 'text':'Tracking'},
                                            {'value':"none", 'text':'None'}];
$scope.feedback = {
  alertType:feedback.alertType,
  alertTitle:feedback.alertTitle,
  alertDate:feedback.creationTime,
  shiftTime:feedback.shiftTime,
  empDescription:feedback.empDescription,
  currentStatus:feedback.alertStatus,
  remarks:feedback.remarks,
  feedbackStatus:{value:feedback.alertStatus},
  assignFeedbackTo:{value:feedback.assignFeedbackTo}
}

    $scope.updatefeedback = function(value){
          var data = {
                      userId:profileId,
                      tripAlertsId:feedback.alertId,
                      alertClosingDescription:value.remarks,
                      alertOpenStatus:value.feedbackStatus.value,
                      assignFeedbackTo:value.assignFeedbackTo.value,
                      combinedFacility:combinedFacility
                      }
            

                    $http.post('services/alert/updateFeedBacks/',data).
                          success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                              if(data.status == "success"){
                                    ngDialog.open({
                                      template: 'Updated succesfully.',
                                      plain: true
                                    });
                                     $timeout(function() {$modalInstance.close(value)}, 2000);

                              }else{
                                    ngDialog.open({
                                      template: 'Updation Failed',
                                      plain: true
                                    });
                                    $timeout(function() {$modalInstance.close()}, 2000);
                              }
                            }
                          }).
                          error(function(data, status, headers, config) {
                            // log error
                          }); 
    }


    //CLOSE BUTTON FUNCTION
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};
angular.module('efmfmApp').controller('reportsCtrl', reportsCtrl);    
angular.module('efmfmApp').controller('routeHistoryCtrl', routeHistoryCtrl);  
angular.module('efmfmApp').controller('remarksTripSheetTravelDistCtrl', remarksTripSheetTravelDistCtrl);
angular.module('efmfmApp').controller('onTimeReportsViewModalCtrl', onTimeReportsViewModalCtrl);
angular.module('efmfmApp').controller('seatUtilReportsViewModalCtrl', seatUtilReportsViewModalCtrl);
angular.module('efmfmApp').controller('noShowReportsViewModalCtrl', noShowReportsViewModalCtrl);
angular.module('efmfmApp').controller('dynamicDetailsReportCtrl', dynamicDetailsReportCtrl);
angular.module('efmfmApp').controller('viewEmployeeDetailsCtrl', viewEmployeeDetailsCtrl);
angular.module('efmfmApp').controller('viewOdometerTripSheetCtrl', viewOdometerTripSheetCtrl);
angular.module('efmfmApp').controller('createOdometerReportCtrl', createOdometerReportCtrl);
angular.module('efmfmApp').controller('createOdometerTableViewCtrl', createOdometerTableViewCtrl);
angular.module('efmfmApp').controller('routeHistoryMapViewCtrl', routeHistoryMapViewCtrl);
angular.module('efmfmApp').controller('editTravelDistanceCtrl', editTravelDistanceCtrl);
angular.module('efmfmApp').controller('editFeedbackCtrl', editFeedbackCtrl);


}());