/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description
@Main Controllers       myProfileCtrl
@Modal Controllers      addUserCtrl, addNewShiftTime, mapMyProfileCtrl, addNewNodelPoint, assignModuleAccessCtrl
@template               partials/home.myProfile.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
05/04/2016  Saima Aziz      Functions Supporting New Fields in App Settings
 */
(function() {
    var myProfileCtrl = function($scope, $modal, $http, $timeout, $confirm,
        $state, ngDialog, $rootScope) {

        if (!$scope.isMyProfileActive || $scope.isMyProfileActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $scope.isDisable = true;
            $scope.getAdminResult = false;
            $scope.genderPreference = genderPreference;
            $('.admin_home').addClass('active');
            $('.myProfile_admin').addClass('active');
            $scope.appSettings = {};
            $scope.regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d][A-Za-z\d!@#$%^&*()_+]/;
            $scope.etaSMS;
            $scope.geoFenceArea;
            $scope.after13HrSMSContact;
            $scope.after14HrSMSContact;
            $scope.maxSpeedArray;
            $scope.driverCheckedout;
            $scope.delayMessage;
            $scope.employeeWaitingTime;
            $scope.emrgContactNumber;
            $scope.maxTravelTime;
            $scope.maxRadialDistance;
            $scope.maxRouteLength;
            $scope.escortConstraints = [];
            $scope.maxRoutedeviation;
            $scope.transportContactNumber;
            $scope.employeeAppReportBug;
            $scope.reportBugEmailIds;
            $scope.toEmployeeFeedBackEmail;
            $scope.employeeFeedbackEmailId;
            $scope.employeeFeedbackEmail;
            $scope.autoClustering;
            $scope.clusterSize;
            $scope.shiftsButtonLabel;
            $scope.daysSettings;
            $scope.transportFeedbackEmailId;
            $scope.driverImage;
            $scope.driverMobileNuumber;
            $scope.employeeCheckOutGeofenceRegion;
            $scope.driverName;
            $scope.profilePic;
            $scope.autoCallSmsDisable;
            $scope.pickDropNeeded;
            $scope.adminRequired;
            $scope.adminInactiveAccount;
            $scope.resetPaswwordForAdmin;
            $scope.resetPaswwordForUser;
            localStorage.clear();
            $scope.facilityDetails = userFacilities;
            var array = JSON.parse("[" + combinedFacility + "]");
            $scope.facilityData = array;
            $scope.currentCombinedFacilityId = $scope.facilityData;
            localStorage.setItem("combinedFacilityIdMyProfile", $scope.facilityData);
            var combinedFacilityId = localStorage.getItem("combinedFacilityIdMyProfile");
            $rootScope.combinedFacilityId = combinedFacilityId;
            $scope.getAppDownloadCountData = [];
            $scope.getYetToAppCountData = [];
            $scope.geoCodedEmployeeCountData = [];
            $scope.getYetToGeoCodedEmployee = [];
            $scope.appdownloadedButNoGeoCoded = [];
            $scope.appDownloadedAndGeocodedEmployees = [];
            $scope.noAppDownloadedAndGeocodedEmployees = [];
            $scope.appDownloadExcel = [];
            $scope.userRoleDetails = userRole;
            if (multiFacility == "true") {
                $scope.multiFacilityFlg = 'Y';
            } else {
                $scope.multiFacilityFlg = 'N';
            }

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
            $scope.searchEmployees = function(search, combinedFacilityId, tabDetails) {
              console.log("tabDetails",tabDetails);

             


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
                      console.log("tabDetails",tabDetails);
                        switch (tabDetails) {
                        case 'appDownload':
                            $scope.getAppDownloadCountData = data;
                            break;
                        case 'nonAppDownload':
                            $scope.getYetToAppCountData = data;
                            break;
                        case 'geoCodedEmployee':
                            $scope.geoCodedEmployeeCountData = data;
                            break;
                        case 'yetToGEOCodedEmployee':
                            $scope.getYetToGeoCodedEmployee = data;
                            break;
                        case 'appdownloadedButNoGeoCoded':
                            $scope.appdownloadedButNoGeoCoded = data;
                            break;
                        case 'appDownloadedAndGeocodedEmployees':
                            $scope.appDownloadedAndGeocodedEmployees = data;
                            break;
                        case 'noAppDownloadedAndGeocodedEmployees':
                            $scope.noAppDownloadedAndGeocodedEmployees = data;
                            break;
                        default:
                            break;
                    }
                    }
                }).
                error(function(data, status, headers, config) {});
            };

             $scope.saveInExcel = function(tabDetails, data){
               $scope.appDownloadExcel = [];
               angular.forEach(data, function(item){
                   $scope.appDownloadExcel.push({
                                              'Employee Id':item.employeeId,
                                              'Employee Name':item.employeeName, 
                                              'Address':item.address,
                                              'Device Type':item.deviceType,
                                              'Mobile Number':item.mobileNumber,
                                              'Email Id':item.emailId,
                                              'Geo Code':item.lattitudeLongitude});
               }); 
               var sheetLabel = 'App Download Count Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("appDownloadReport.xlsx",?) FROM ?',[opts,[$scope.appDownloadExcel]]);   
             };

             $scope.nonAppdownloadExcel = function(tabDetails, nonAppData){
               $scope.nonAppdownloadExcelArray= [];
               angular.forEach(nonAppData, function(nonApp){
                   $scope.nonAppdownloadExcelArray.push({
                                              'Employee Id':nonApp.employeeId,
                                              'Employee Name':nonApp.employeeName, 
                                              'Address':nonApp.address,
                                              'Device Type':nonApp.deviceType,
                                              'Mobile Number':nonApp.mobileNumber,
                                              'Email Id':nonApp.emailId,
                                              'Geo Code':nonApp.lattitudeLongitude});
               }); 
               var sheetLabel = 'Non Download Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("nonAppdownloadExcelReport.xlsx",?) FROM ?',[opts,[$scope.nonAppdownloadExcelArray]]);   
             };
                $scope.geoCodedEmployeeExcel = function(tabDetails, geoCodedData){
               $scope.geoCodedEmployeeExcelArray = [];
               angular.forEach(geoCodedData, function(geoCode){
                   $scope.geoCodedEmployeeExcelArray.push({
                                              'Employee Id':geoCode.employeeId,
                                              'Employee Name':geoCode.employeeName, 
                                              'Address':geoCode.address,
                                              'Device Type':geoCode.deviceType,
                                              'Mobile Number':geoCode.mobileNumber,
                                              'Email Id':geoCode.emailId,
                                              'Geo Code':geoCode.lattitudeLongitude});
               }); 
               var sheetLabel = 'Geo Coded Employee Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("geoCodedEmployeeExcelReport.xlsx",?) FROM ?',[opts,[$scope.geoCodedEmployeeExcelArray]]);   
             };

              $scope.yetToGEOCodedEmployeeExcel = function(tabDetails, nonGeoCodedData){
               $scope.nonGeoCodedEmployeeExcelArray = [];
               angular.forEach(nonGeoCodedData, function(nonGeoCode){
                   $scope.nonGeoCodedEmployeeExcelArray.push({
                                              'Employee Id':nonGeoCode.employeeId,
                                              'Employee Name':nonGeoCode.employeeName, 
                                              'Address':nonGeoCode.address,
                                              'Device Type':nonGeoCode.deviceType,
                                              'Mobile Number':nonGeoCode.mobileNumber,
                                              'Email Id':nonGeoCode.emailId,
                                              'Geo Code':nonGeoCode.lattitudeLongitude});
               }); 
               var sheetLabel = 'Yet To GEO Coded Employee Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("yetToGEOCodedEmployeeExcelReport.xlsx",?) FROM ?',[opts,[$scope.nonGeoCodedEmployeeExcelArray]]);   
             };

              $scope.appDownloadedButNoGeoCodedExcel = function(tabDetails, appDownloadedNoGeoCodedData){
               $scope.appDownloadedButNoGeoCodedExcelArray = [];
               angular.forEach(appDownloadedNoGeoCodedData, function(appnoGeoCode){
                   $scope.appDownloadedButNoGeoCodedExcelArray.push({
                                              'Employee Id':appnoGeoCode.employeeId,
                                              'Employee Name':appnoGeoCode.employeeName, 
                                              'Address':appnoGeoCode.address,
                                              'Device Type':appnoGeoCode.deviceType,
                                              'Mobile Number':appnoGeoCode.mobileNumber,
                                              'Email Id':appnoGeoCode.emailId,
                                              'Geo Code':appnoGeoCode.lattitudeLongitude});
               }); 
               var sheetLabel = 'App Downloaded But No GeoCoded Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("AppDownloadedButNoGeoCodedExcelReport.xlsx",?) FROM ?',[opts,[$scope.appDownloadedButNoGeoCodedExcelArray]]);   
             };

               $scope.appDownloadedAndGeocodedEmployeesExcel = function(tabDetails, appDownloadedGeoCodedData){
               $scope.appDownloadedAndGeocodedEmployeesExcelArray = [];
               angular.forEach(appDownloadedGeoCodedData, function(appGeoCode){
                   $scope.appDownloadedAndGeocodedEmployeesExcelArray.push({
                                              'Employee Id':appGeoCode.employeeId,
                                              'Employee Name':appGeoCode.employeeName, 
                                              'Address':appGeoCode.address,
                                              'Device Type':appGeoCode.deviceType,
                                              'Mobile Number':appGeoCode.mobileNumber,
                                              'Email Id':appGeoCode.emailId,
                                              'Geo Code':appGeoCode.lattitudeLongitude});
               }); 
               var sheetLabel = 'AppDownloaded And Geocoded Employees Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("appDownloadedAndGeocodedEmployeesExcelReport.xlsx",?) FROM ?',[opts,[$scope.appDownloadedAndGeocodedEmployeesExcelArray]]);   
             };

               $scope.noAppDownloadedAndGeocodedEmployeesExcel = function(tabDetails, noAppDownloadedGeoCodedData){
               $scope.noAppDownloadedAndGeocodedEmployeesExcelArray = [];
               angular.forEach(noAppDownloadedGeoCodedData, function(noAppAndGeoCode){
                   $scope.noAppDownloadedAndGeocodedEmployeesExcelArray.push({
                                              'Employee Id':noAppAndGeoCode.employeeId,
                                              'Employee Name':noAppAndGeoCode.employeeName, 
                                              'Address':noAppAndGeoCode.address,
                                              'Device Type':noAppAndGeoCode.deviceType,
                                              'Mobile Number':noAppAndGeoCode.mobileNumber,
                                              'Email Id':noAppAndGeoCode.lattitudeLongitude});
               }); 
               var sheetLabel = 'No AppDownloaded And Geocoded Employees Excel Report';
               var opts = [{sheetid:sheetLabel,header:true}];
               alasql('SELECT INTO XLSX("noAppDownloadedAndGeocodedEmployeesExcelReport.xlsx",?) FROM ?',[opts,[$scope.noAppDownloadedAndGeocodedEmployeesExcelArray]]);   
             };

            $scope.getTabInfo = function(value){
                localStorage.setItem("myProfileTabDetails", value);
            }
 

            $scope.getFacilityDetails = function(combinedFacilityId) {
                var tabInfo = localStorage.getItem("myProfileTabDetails");
                console.log("tabInfo",tabInfo);
                console.log("combinedFacilityId",combinedFacilityId);
                $scope.currentCombinedFacilityId = combinedFacilityId;
                switch (tabInfo) {
                    case 'administratorSettings':
                        $scope.getAdminSettings(combinedFacilityId);
                        break;
                    case 'applicationSettings':
                        $scope.getApplicationSettings(combinedFacilityId);
                        break;
                    case 'messageSentHistory':
                        $scope.getAllSentSmsHistory();
                        break;
                    case 'requestsDetail':
                        $scope.getRequests();
                        break;
                    case 'shiftTimes':
                        $scope.getAllShifts();
                        break;
                    case 'routeNames':
                        $scope.getAllRouteNames();
                        break;
                    case 'areaNames':
                        $scope.getAllAreaNames();
                        break;
                    case 'geoLocation':
                        $scope.getAllGeoLocations();
                        break;
                    case 'projectManagement':
                        $scope.getProjectManagement();
                        break;
                    case 'projectDetails':
                        $scope.getProjectManagement();
                        break;
                    case 'ProjectAllocationDelegation':
                        $scope.projectAllocation();
                        $scope.getProjectManagement();
                        break;
                    case 'allocatedProjectsDetails':
                        $scope.getProjectManagement();
                        break;
                    case 'delegatedDetails':
                        $scope.getDelegateUsers();
                        break;
                    case 'appDownload':
                        $scope.getAppDownloadCount('appDownloadCount');
                        break;
                    case 'nonAppDownload':
                        $scope.getYetToAppDownloadCount('yetToAppDownload')
                        break;
                    case 'geoCodedEmployee':
                        $scope.getGeoCodedEmployee('geoCodedEmployee');
                        break;
                    case 'yetToGEOCodedEmployee':
                        $scope.getDataYetToGeoCodedEmployee('yetToGEOCodedEmployee');
                        break;
                    case 'appdownloadedButNoGeoCoded':
                        $scope.getAppdownloadedButNoGeoCoded('appdownloadedButNoGeoCoded');
                        break;
                    case 'appDownloadedAndGeocodedEmployees':
                        $scope.getAppDownloadedAndGeocodedEmployees('appDownloadedAndGeocodedEmployees');
                        break;
                    case 'noAppDownloadedAndGeocodedEmployees':
                        $scope.getNoAppDownloadedAndGeocodedEmployees(noAppDownloadedAndGeocodedEmployees);
                        break;
                    case 'combinedFacility':
                        $scope.getCombinedFacility();
                        break;
                    case 'nodalPoints':
                        $scope.getAllNodalNames();
                        break;
                    default:
                        $scope.getAdminSettings(combinedFacilityId);
                        break;
                }

                $rootScope.combinedFacilityId = combinedFacilityId;
             
            }



            $scope.twoWayFactor;
            $scope.numberOfAttemptsWrongPass;
            $scope.sessiontimeOut;
            $scope.invoiceNumberRange;
            $scope.gotResultAppShift = false;
            $scope.appSettingShiftData = [];
            $scope.selectedEscortConstraints = [];
            $scope.shiftTimeData = [];
            $scope.isCreateNodal = false;
            $scope.isCreateNodalPoint = false;
            $scope.hstep5 = 1;
            $scope.mstep5 = 1;
            $scope.ismeridian5 = false;
            $scope.shiftTimeDisable = false;
            $scope.geoFenceAreaShow = false;
            $scope.dataChangeShift = true;
            $scope.shiftTimeRequired = false;
            $scope.location = {};
            $scope.escortTimeDivShow = false;
            $scope.storedMessageDisabled = true;
            $scope.manualMessageDisabled = true;
            $scope.messageDetailsHeader = "All Employees";
            $rootScope.messageValue = "allEmployee";
            $scope.notificationTypeSMSShow = false;
            $scope.notificationTypeEmailShow = false;
            $scope.getAppDownloadCountDataShow = false;
            $scope.getYetToAppCountDataShow = false;
            $scope.geoCodedEmployeeCountDataShow = false;
            $scope.getYetToGeoCodedEmployeeShow = false;
            $scope.appdownloadedButNoGeoCodedShow = false;
            $scope.appDownloadedAndGeocodedEmployeesShow = false;

            $rootScope.destinationPush = false;
            $scope.locationDetailButtonDisabled = true;
            $scope.desinationDisabled = true;
            $scope.desinationDisabledButton = true;
            var saveAreaIdLocal = [];
            $scope.destinationArray = [];
            $scope.destinationPointsArray = [];
            $scope.destinationValuesArray = [];
            $scope.statusTrue = [];
            $scope.destinationLanLng = [];

            $scope.gotShiftTimeResultsShow = false;
            $scope.requestsDataShow = false;

            if ($rootScope.result == undefined) {
                $scope.destinationPoints = [];
                $rootScope.destinationValue = [];
                $scope.destinationPoints.push({
                    destination: ''
                });
            } else {
                $scope.destinationPoints = $rootScope.result;
                $rootScope.destinationPoints = $rootScope.result;
                $scope.origin = $rootScope.originAreaId;
            }


            $scope.allZonesData = [];

            $rootScope.locationLoad = function() {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'A',
                    combinedFacility: combinedFacility
                };
                $http.post('services/employee/allActiveLocation/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allZonesData = data.reverse();
                        $rootScope.homeLocationAreaId = $scope.allZonesData[0].areaId;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
            // $rootScope.locationLoad();



            $scope.geoLocationStatus = [{
                'value': 'Enable',
                'text': 'Enable'
            }, {
                'value': 'Disable',
                'text': 'Disable'
            }];

            $scope.shiftTimeDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'shiftTimeDate': true
                    };
                }, 50);
            };

            $scope.tripTypes = [{
                'value': 'PICKUP',
                'text': 'PICKUP'
            }, {
                'value': 'DROP',
                'text': 'DROP'
            }];

            $scope.genderPreferenceData = [{
                'value': 'M',
                'text': 'Male'
            }, {
                'value': 'F',
                'text': 'Female'
            }, {
                'value': 'B',
                'text': 'Both'
            }];


            $scope.getAllAreaNames = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                var data = {
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/zones/allAreas/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.allAreaDatas = data.areaDetails;
                            $scope.user.areaName = "";
                        }
                    }).error(function(data, status, headers, config) {});
            };
            // $scope.getAllAreaNames();

            $scope.setTripTypeliveTracking = function(tripType) {

                $scope.shiftTimeEmployee = true;
                $scope.employeeRosterTable = false;
                $scope.summaryLengthShift = 0;

                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            userId: profileId
                        }
                    },
                    tripType: tripType.value,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/trip/tripshiftime/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.shiftsTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
                        }

                    }).error(function(data, status, headers, config) {});

            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1,
                showWeeks: false
            };

            $scope.formats = ['yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-mm-yyyy', 'mm-yyyy'];
            $scope.monthformat = 'MMMM' + ' - ' + 'yyyy';

            $scope.datepickerOptions = {
                datepickerMode: "'month'",
                minMode: "month",
                minDate: "minDate",
                showWeeks: "false"
            };

            $scope.openFromDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'fromDate': true
                    };
                }, 50);
            };

            $scope.openFromDateCalFrom = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'fromDate': true
                    };
                }, 50);
            };

            $scope.openFromDateCalTo = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'toDate': true
                    };
                }, 50);
            };

            $scope.example14model = [];
            $scope.example14settings = {

                scrollable: true,
                uncheckAll: false

            };

            _.mixin({
                capitalize: function(string) {
                    return string.charAt(0).toUpperCase() + string.substring(1).toLowerCase();
                }
            });

            $scope.messageList = [{
                    value: "SendMessage",
                    msgType: 'Send Message'
                },
                {
                    value: "SendNotification",
                    msgType: 'Send Notification'
                },
                {
                    value: "BOTH",
                    msgType: 'BOTH'
                }
            ];
            $scope.example14data = [{
                    "label": "Sunday",
                    "id": 1
                },
                {
                    "label": "Monday",
                    "id": 2
                },
                {
                    "label": "Tuesday",
                    "id": 3
                },
                {
                    "label": "Wednesday",
                    "id": 4
                },
                {
                    "label": "Thursday",
                    "id": 5
                },
                {
                    "label": "Friday",
                    "id": 6
                },
                {
                    "label": "Saturday",
                    "id": 7
                }
            ];


            $scope.example2settings = {
                displayProp: 'id'
            };

            $scope.onItemSelect = function(dynamicDays) {

                $scope.dynamicDays = dynamicDays;
            }


            $scope.daysRequestDetails = [{
                    id: 1,
                    name: 'Sunday'
                },
                {
                    id: 2,
                    name: 'Monday'
                },
                {
                    id: 3,
                    name: 'Tuesday'
                },
                {
                    id: 4,
                    name: 'Wednesday'
                },
                {
                    id: 5,
                    name: 'Thursday'
                },
                {
                    id: 6,
                    name: 'Friday'
                },
                {
                    id: 7,
                    name: 'Saturday'
                }
            ];

            $scope.dynamicDay = [];


            $scope.list = [{
                    number: 1,
                    label: "No Of Days",
                    value: 'Days',
                    group: 'Days'
                },
                {
                    number: 3,
                    label: "Every Month Last Date",
                    value: 'everymonthlastdate',
                    group: 'Date'
                },
                {
                    number: 2,
                    label: "Customized Date",
                    value: 'Date',
                    group: 'Date'
                },

            ];


            $scope.applicationSettingsArrays = {
                clustrings: ["Enable", "Disable"],
                driverImage: ['Yes', 'No'],
                driverMobileNuumber: ['Yes', 'No'],
                driverName: ['Yes', 'No'],
                profilePic: ['Yes', 'No'],
                invoiceGenDateArray: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
                autoCallSmsDisable: ['Yes', 'No'],
                tripType: ['PICKUP', 'DROP', 'BOTH'],
                twoWayFactorArray: ['Yes', 'No'],
                pickDropArray: ['PICKUP', 'DROP', 'Both'],
                adhocTimePickers: ['Yes', 'No'],
                panicAlertNeededArray: ['AnyTime', 'OnlyInTrip'],
                lastPasswordNotCurrentArray: ['Yes', 'No'],
                autoDropRosterArray: ['Yes', 'No'],
                distanceArray: ['Odometer', 'GPS'],
                autoDriverCheckout: ['Yes', 'No'],
                plaCardPrints: ['Yes', 'No'],
                plaCardTemplateType: ['Customized Own Template', 'Default Template With Logo'],
                autoVehicleAllocation: ['Yes', 'No'],
                singleDay: [{
                        'name': 'Select Days',
                        'value': "0"
                    },
                    {
                        'name': 'Sunday',
                        'value': "1"
                    },
                    {
                        'name': 'Monday',
                        'value': "2"
                    },
                    {
                        'name': 'Tuesday',
                        'value': "3"
                    },
                    {
                        'name': 'Wednesday',
                        'value': "4"
                    },
                    {
                        'name': 'Thursday',
                        'value': "5"
                    },
                    {
                        'name': 'Friday',
                        'value': "6"
                    },
                    {
                        'name': 'Saturday',
                        'value': "7"
                    },
                ],
                earlyRequestDate: [{
                        'name': 'Select Days',
                        'value': "0"
                    },
                    {
                        'name': '1',
                        'value': "1"
                    },
                    {
                        'name': '2',
                        'value': "2"
                    },
                    {
                        'name': '3',
                        'value': "3"
                    },
                    {
                        'name': '4',
                        'value': "4"
                    },
                    {
                        'name': '5',
                        'value': "5"
                    },
                    {
                        'name': '6',
                        'value': "6"
                    },
                    {
                        'name': '7',
                        'value': "7"
                    },
                ],
                requestType: ['Days', 'Date'],
                requestLocationArray: ['Single', 'Multiple'],
                locationVisibleArray: [{
                        value: 'N',
                        name: 'None'
                    },
                    {
                        value: 'A',
                        name: 'All'
                    },
                    {
                        value: 'AD',
                        name: 'Admin'
                    },
                    {
                        value: 'AVU',
                        name: 'Admin and User'
                    },
                ],
                invoiceGenTypeArray: ['Month', 'Date'],
                backToBackType: ['Distance', 'Time'],
                destinationPointLimitArray: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
                notificationType: ['SMS', 'Email', 'Both'],
                personalDeviceViaSms: ['Yes', 'No'],
                assignRoutesToVendor: ['Yes', 'No'],
                driverCallToEmployee: [{
                        value: 'none',
                        name: 'None'
                    },
                    {
                        value: 'normal',
                        name: 'Normal'
                    },
                    {
                        value: 'ivr',
                        name: 'IVR'
                    }
                ],
                driverCallToTransportDesk: [{
                        value: 'none',
                        name: 'None'
                    },
                    {
                        value: 'normal',
                        name: 'Normal'
                    },
                    {
                        value: 'ivr',
                        name: 'IVR'
                    }
                ],
                employeeCallToDriver: [{
                        value: 'none',
                        name: 'None'
                    },
                    {
                        value: 'normal',
                        name: 'Normal'
                    },
                    {
                        value: 'ivr',
                        name: 'IVR'
                    }
                ],
                employeeCallToTransport: [{
                        value: 'none',
                        name: 'None'
                    },
                    {
                        value: 'normal',
                        name: 'Normal'
                    },
                    {
                        value: 'ivr',
                        name: 'IVR'
                    }
                ],
                employeeFeedbackEmail: ['Yes', 'No'],
                employeeAppReportBug: ['Yes', 'No'],
                approvalProcess: ['Yes', 'No'],
                requestWithProject: ['Yes', 'No'],
                vehiceCheckList: ['Yes', 'No'],
                driverHistory: ['Yes', 'No'],
                shiftTimeGenderPreference: ['Yes', 'No'],
                postApproval: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30],
                managerReqCreateProcess: ['Yes', 'No'],
                autoCancelDrop: ['Yes', 'No'],
                gpsKmModification: ['None', 'Add', 'Subtraction', 'Both'],
                employeeCheckInVia: [{
                        value: 'QRCode',
                        name: 'QR Code'
                    },
                    {
                        value: 'EmployeeCode',
                        name: 'Employee Code'
                    }
                ],
                selectVehicleCapacitywise: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30],
                mobileLoginVia: [{
                        value: 'SSO',
                        name: 'SSO'
                    },
                    {
                        value: 'normal',
                        name: 'Normal'
                    }
                ],
                isMultiFacility: [{
                        value: 'Y',
                        name: 'Yes'
                    },
                    {
                        value: 'N',
                        name: 'No'
                    }
                ],
                webPageCount: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30],
                mobilePageCount: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30]

            };


            $scope.shiftsButtonLabel = {
                'buttonDefaultText': 'Select Shifts'
            };

            $scope.requestTypesDataTS = {
                availableOptions: [{
                    value: "DROP",
                    requestTypes: 'DROP'
                }, {
                    value: "PICKUP",
                    requestTypes: 'PICKUP'
                }],
                selectedOption: {
                    value: "DROP",
                    requestTypes: 'DROP'
                }
            };

            //Convert the dates in DD-MM-YYYY format
            var convertDateUTC = function(date) {
                var convert_date = new Date(date);

                var currentDay = convert_date.getDate();
                if (currentDay < 10) {
                    currentDay = '0' + currentDay;
                }
                var currentMonth = convert_date.getMonth() + 1;
                if (currentMonth < 10) {
                    currentMonth = '0' + currentMonth;
                }
                return currentDay + '-' + currentMonth + '-' + convert_date.getFullYear();
            };

            $scope.daysSettings = {
                smartButtonMaxItems: 2,
                selectionLimit: 2,
                smartButtonTextConverter: function(itemText, originalItem) {
                    return itemText;
                }
            };
            var shifDateConvertDateUTC = function(date) {
                var convert_date = new Date(date);

                var currentDay = convert_date.getDate();
                if (currentDay < 10) {
                    currentDay = '0' + currentDay;
                }
                var currentMonth = convert_date.getMonth() + 1;
                if (currentMonth < 10) {
                    currentMonth = '0' + currentMonth;
                }
                return convert_date.getFullYear() + '-' + currentMonth + '-' + currentDay;
            };
            $scope.$on('$viewContentLoaded', function() {
                $scope.getProfileDetail();
            });

            $scope.getApplicationSettings = function(combinedFacilityId) {
            $scope.applicationSettingsTab = false;
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: String(combinedFacilityId)
                };
                $http
                    .post('services/user/branchdetail/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $rootScope.vehicleTypeDetails = data;
                                $scope.escortSelection = data.escortRequired;
                                $scope.escortTimeStatus = data.escortTimeWindowEnable;
                                $scope.escortStartTime = data.escortStartTimePickup;
                                $scope.escortStartTimeDrop = data.escortStartTimeDrop;
                                $scope.escortEndTime = data.escortEndTimePickup;
                                $scope.escortEndTimeDrop = data.escortEndTimeDrop;
                                $scope.sessionTimeOutNotificationInSec = String(data.sessionTimeOutNotificationInSec);
                                $scope.OTPEnableTimeLimit = data.dissableTimeOTP;
                                $scope.OTPGenerationMaximumLimit = String(data.maxTimeOTP);
                                $scope.employeeCheckInVia = {
                                    value: data.employeeCheckInVia
                                };
                                $scope.selectVehicleCapacitywise = data.selectVehicleCapacitywise;
                                $scope.vehicleType = data.vehicleType;

                                $scope.approvalProcess = data.approvalProcess;
                                $scope.requestWithProject = data.requestWithProject;
                                $scope.vehiceCheckList = data.vehiceCheckList;
                                $scope.postApproval = String(data.postApproval);
                                $scope.shiftTimeGenderPreference = data.shiftTimeGenderPreference;
                                $scope.minimumDestCount = String(data.minimumDestCount);
                                $scope.driverHistory = data.driverHistory;
                                $scope.managerReqCreateProcess = data.managerReqCreateProcess;
                                $scope.tripHistoryCount = String(data.tripHistoryCount);
                                $scope.plaCardPrint = data.plaCardPrint;
                                $scope.autoCancelDrop = data.onPickUpNoShowCancelDrop;
                                $scope.consecutiveNoShowCount = String(data.consecutiveNoShowCount);
                                $scope.gpsKmModification = data.gpsKmModification;

                                $scope.mobileLoginUrl = data.mobileLoginUrl;
                                $scope.ssoLoginUrl = data.ssoLoginUrl;
                                $scope.webPageCount = data.webPageCount;
                                $scope.mobilePageCount = data.mobilePageCount;
                                $scope.mobileLoginVia = {
                                    value: data.mobileLoginVia
                                };

                                if (data.isMultiFacility == true) {
                                    data.isMultiFacility = 'Y';
                                } else {
                                    data.isMultiFacility = 'N';
                                }

                                $scope.isMultiFacility = {
                                    value: data.isMultiFacility
                                };

                                if (data.plaCardPrint == 'Yes') {
                                    $scope.plaCardTemplateTypeShow = true;
                                } else {
                                    $scope.plaCardTemplateTypeShow = false;
                                }
                                // Delay Trips
                                $scope.delayTrips = data.tripConsiderDelayAfter;
                                $scope.timeConverstiondelayTrips = [];
                                $scope.timeConverstiondelayTrips = $scope.delayTrips
                                    .split(':');

                                var todaydatedelayTrips = new Date();
                                todaydatedelayTrips
                                    .setHours(
                                        $scope.timeConverstiondelayTrips[0],
                                        $scope.timeConverstiondelayTrips[1],
                                        $scope.timeConverstiondelayTrips[2]);
                                $scope.delayTrips = todaydatedelayTrips;


                                if (data.approvalProcess == 'Yes') {
                                    $scope.postApprovalRequired = true;
                                    $scope.postApprovalShow = true;
                                } else {
                                    $scope.postApprovalRequired = false;
                                    $scope.postApprovalShow = false;
                                }


                                if ($scope.requestWithProject == 'Yes') {
                                    $scope.requestWithProject = 'Yes';
                                    $scope.managerReqCreateProcessShow = true;
                                } else {
                                    $scope.requestWithProject = 'No';
                                    $scope.managerReqCreateProcessShow = false;
                                }

                                // Disable Time OTP

                                $scope.timeConverstiondissableTimeOTP = [];
                                $scope.timeConverstiondissableTimeOTP = $scope.OTPEnableTimeLimit
                                    .split(':');

                                var todaydatedissableTimeOTP = new Date();
                                todaydatedissableTimeOTP
                                    .setHours(
                                        $scope.timeConverstiondissableTimeOTP[0],
                                        $scope.timeConverstiondissableTimeOTP[1],
                                        $scope.timeConverstiondissableTimeOTP[2]);
                                $scope.OTPEnableTimeLimit = todaydatedissableTimeOTP;


                                // Escort Start Time

                                $scope.timeConverstionescortStartTime = [];
                                $scope.timeConverstionescortStartTime = $scope.escortStartTime
                                    .split(':');

                                var todaydateescortStartTime = new Date();
                                todaydateescortStartTime
                                    .setHours(
                                        $scope.timeConverstionescortStartTime[0],
                                        $scope.timeConverstionescortStartTime[1],
                                        $scope.timeConverstionescortStartTime[2]);
                                $scope.escortStartTime = todaydateescortStartTime;
                                // Escort Start Time Drop
                                $scope.timeConverstionescortStartTimeDrop = [];
                                $scope.timeConverstionescortStartTimeDrop = $scope.escortStartTimeDrop
                                    .split(':');

                                var todaydateescortStartTimeDrop = new Date();
                                todaydateescortStartTimeDrop
                                    .setHours(
                                        $scope.timeConverstionescortStartTimeDrop[0],
                                        $scope.timeConverstionescortStartTimeDrop[1],
                                        $scope.timeConverstionescortStartTimeDrop[2]);
                                $scope.escortStartTimeDrop = todaydateescortStartTimeDrop;

                                // Escort End Time

                                $scope.timeConverstionescortEndTimeDrop = [];
                                $scope.timeConverstionescortEndTimeDrop = $scope.escortEndTimeDrop
                                    .split(':');

                                var todaydateescortEndTimeDrop = new Date();
                                todaydateescortEndTimeDrop
                                    .setHours(
                                        $scope.timeConverstionescortEndTimeDrop[0],
                                        $scope.timeConverstionescortEndTimeDrop[1],
                                        $scope.timeConverstionescortEndTimeDrop[2]);
                                $scope.escortEndTimeDrop = todaydateescortEndTimeDrop;


                                $scope.timeConverstionescortEndTime = [];
                                $scope.timeConverstionescortEndTime = $scope.escortEndTime
                                    .split(':');

                                var todaydateescortEndTime = new Date();
                                todaydateescortEndTime
                                    .setHours(
                                        $scope.timeConverstionescortEndTime[0],
                                        $scope.timeConverstionescortEndTime[1],
                                        $scope.timeConverstionescortEndTime[2]);
                                $scope.escortEndTime = todaydateescortEndTime;

                                var value = data.escortRequired;

                                if (value == 'None') {
                                    $scope.escortTimeDivShow = false;

                                } else if (value == 'Always') {
                                    if ($scope.escortTimeStatus === 'Y') {
                                        $scope.escortEditTimeShow = true;
                                        $scope.escortConfirmationYES = true;
                                        $scope.escortConfirmationNO = false;
                                    } else {
                                        $scope.escortEditTimeShow = false;
                                        $scope.escortConfirmationYES = false;
                                        $scope.escortConfirmationNO = true;
                                    }
                                    $scope.escortTimeDivShow = true;
                                    $scope.escortTimingPanelTitle = 'Always';
                                } else if (value == 'femalepresent') {
                                    if ($scope.escortTimeStatus === 'Y') {
                                        $scope.escortEditTimeShow = true;
                                        $scope.escortConfirmationYES = true;
                                        $scope.escortConfirmationNO = false;
                                    } else {
                                        $scope.escortEditTimeShow = false;
                                        $scope.escortConfirmationYES = false;
                                        $scope.escortConfirmationNO = true;
                                    }
                                    $scope.escortTimeDivShow = true;
                                    $scope.escortTimingPanelTitle = 'When Female Passenger Is Present';
                                } else if (value == 'femaleAlone') {
                                    if ($scope.escortTimeStatus === 'Y') {
                                        $scope.escortEditTimeShow = true;
                                        $scope.escortConfirmationYES = true;
                                        $scope.escortConfirmationNO = false;
                                    } else {
                                        $scope.escortEditTimeShow = false;
                                        $scope.escortConfirmationYES = false;
                                        $scope.escortConfirmationNO = true;
                                    }
                                    $scope.escortTimeDivShow = true;
                                    $scope.escortTimingPanelTitle = 'When only One female passenger alone in route';
                                } else if (value == 'allFemale') {
                                    if ($scope.escortTimeStatus === 'Y') {
                                        $scope.escortEditTimeShow = true;
                                        $scope.escortConfirmationYES = true;
                                        $scope.escortConfirmationNO = false;
                                    } else {
                                        $scope.escortEditTimeShow = false;
                                        $scope.escortConfirmationYES = false;
                                        $scope.escortConfirmationNO = true;
                                    }
                                    $scope.escortTimeDivShow = true;
                                    $scope.escortTimingPanelTitle = 'When all female passenger in route';
                                } else if (value == 'firstlastfemale') {
                                    if ($scope.escortTimeStatus === 'Y') {
                                        $scope.escortEditTimeShow = true;
                                        $scope.escortConfirmationYES = true;
                                        $scope.escortConfirmationNO = false;
                                    } else {
                                        $scope.escortEditTimeShow = false;
                                        $scope.escortConfirmationYES = false;
                                        $scope.escortConfirmationNO = true;
                                    }
                                    $scope.escortTimeDivShow = true;
                                    $scope.escortTimingPanelTitle = 'Last drop and first pickup of female passenger';
                                }
                                if (data.notificationType == 'SMS') {
                                    $scope.notificationTypeSMSShow = true;
                                    $scope.notificationTypeEmailShow = false;
                                    $scope.notificationTypeCommonEmail = false;
                                } else if (data.notificationType == 'Email') {
                                    $scope.notificationTypeSMSShow = false;
                                    $scope.notificationTypeEmailShow = true;
                                    $scope.notificationTypeCommonEmail = false;
                                } else if (data.notificationType == 'Both') {
                                    $scope.notificationTypeSMSShow = true;
                                    $scope.notificationTypeEmailShow = false;
                                    $scope.notificationTypeCommonEmail = true;
                                }

                                /*Driver Governance*/

                                $scope.licenseExpiryDay = String(data.compliance.licenseExpiryDay);
                                $scope.licenseRepeatAlertsEvery = String(data.compliance.licenseRepeatAlertsEvery);
                                $scope.licenceNotificationType = data.compliance.licenceNotificationType;
                                $scope.licenseSMSDays = data.compliance.licenseSMSNumber;
                                $scope.licenseEmailDays = data.compliance.licenseEmailId;
                                $scope.medicalFitnessExpiryDay = String(data.compliance.medicalFitnessExpiryDay);
                                $scope.medicalFitnessRepeatAlertsEvery = String(data.compliance.medicalFitnessRepeatAlertsEvery);
                                $scope.medicalFitnessNotificationType = data.compliance.medicalFitnessNotificationType;
                                $scope.medicalFitnessSMSDays = data.compliance.medicalFitnessSMSNumber;
                                $scope.medicalFitnessEmailDays = data.compliance.medicalFitnessEmailId;
                                $scope.policeVerificationExpiryDay = String(data.compliance.policeVerificationExpiryDay);
                                $scope.policeVerificationRepeatAlertsEvery = String(data.compliance.policeVerificationRepeatAlertsEvery);
                                $scope.policeVerificationNotificationType = data.compliance.policeVerificationNotificationType;
                                $scope.policeVerificationSMSDays = data.compliance.policeVerificationSMSNumber;
                                $scope.policeVerificationEmailDays = data.compliance.policeVerificationEmailId;
                                $scope.DDTrainingExpiryDay = String(data.compliance.DDTrainingExpiryDay);
                                $scope.DDTrainingRepeatAlertsEvery = String(data.compliance.ddTrainingRepeatAlertsEvery);
                                $scope.DDTrainingNotificationType = data.compliance.DDTrainingNotificationType;
                                $scope.DDTrainingSMSDays = data.compliance.DDTrainingSMSNumber;
                                $scope.DDTrainingEmailDays = data.compliance.DDTrainingEmailId;

                                /*Vehicle Governance*/

                                $scope.pollutionDueExpiryDay = String(data.compliance.pollutionDueExpiryDay);
                                $scope.pollutionDueRepeatAlertsEvery = String(data.compliance.pollutionDueRepeatAlertsEvery);
                                $scope.pollutionDueNotificationType = data.compliance.pollutionDueNotificationType;
                                $scope.pollutionDueSMSDays = data.compliance.pollutionDueSMSNumber;
                                $scope.pollutionDueEmailDays = data.compliance.pollutionDueEmailId;
                                $scope.insuranceDueExpiryDay = String(data.compliance.insuranceDueExpiryDay);
                                $scope.insuranceDueRepeatAlertsEvery = String(data.compliance.insuranceDueRepeatAlertsEvery);
                                $scope.insuranceDueNotificationType = data.compliance.insuranceDueNotificationType;
                                $scope.insuranceDueSMSDays = data.compliance.insuranceDueSMSNumber;
                                $scope.insuranceDueEmailDays = data.compliance.insuranceDueEmailId;
                                $scope.taxCertificateExpiryDay = String(data.compliance.taxCertificateExpiryDay);
                                $scope.taxCertificateRepeatAlertsEvery = String(data.compliance.taxCertificateRepeatAlertsEvery);
                                $scope.taxCertificateNotificationType = data.compliance.taxCertificateNotificationType;
                                $scope.taxCertificateSMSDays = data.compliance.taxCertificateSMSNumber;
                                $scope.taxCertificateEmailDays = data.compliance.taxCertificateEmailId;
                                $scope.permitDueExpiryDay = String(data.compliance.permitDueExpiryDay);
                                $scope.permitDueRepeatAlertsEvery = String(-data.compliance.permitDueRepeatAlertsEvery);
                                $scope.permitDueNotificationType = data.compliance.permitDueNotificationType;
                                $scope.permitDueSMSDays = data.compliance.permitDueSMSNumber;
                                $scope.permitDueEmailDays = data.compliance.permitDueEmailId;
                                $scope.vehicelMaintenanceExpiryDay = String(data.compliance.vehicelMaintenanceExpiryDay);
                                $scope.vehicelMaintenanceRepeatAlertsEvery = String(data.compliance.vehicelMaintenanceRepeatAlertsEvery);
                                $scope.vehicelMaintenanceNotificationType = data.compliance.vehicelMaintenanceNotificationType;
                                $scope.vehicelMaintenanceSMSDays = data.compliance.vehicelMaintenanceSMSNumber;
                                $scope.vehicelMaintenanceEmailDays = data.compliance.vehicelMaintenanceEmailId;

                                if ($scope.licenceNotificationType == 'SMS') {
                                    $scope.notificationLicenseTypeSMSShow = true;
                                    $scope.notificationLicenseTypeEmailShow = false;
                                    $scope.licenceNotificationSMSRequired = true;
                                    $scope.licenceNotificationMessageRequired = false;
                                }
                                if ($scope.licenceNotificationType == 'Email') {
                                    $scope.notificationLicenseTypeSMSShow = false;
                                    $scope.notificationLicenseTypeEmailShow = true;
                                    $scope.licenceNotificationSMSRequired = false;
                                    $scope.licenceNotificationMessageRequired = true;
                                }
                                if ($scope.licenceNotificationType == 'Both') {
                                    $scope.notificationLicenseTypeSMSShow = true;
                                    $scope.notificationLicenseTypeEmailShow = true;
                                    $scope.licenceNotificationSMSRequired = true;
                                    $scope.licenceNotificationMessageRequired = true;
                                }
                                if ($scope.medicalFitnessNotificationType == 'SMS') {
                                    $scope.notificationmedicalFitnessTypeSMSShow = true;
                                    $scope.notificationmedicalFitnessTypeEmailShow = false;
                                    $scope.medicalFitnessSMSRequired = true;
                                    $scope.medicalFitnessMessageRequired = false;
                                }
                                if ($scope.medicalFitnessNotificationType == 'Email') {
                                    $scope.notificationmedicalFitnessTypeSMSShow = false;
                                    $scope.notificationmedicalFitnessTypeEmailShow = true;
                                    $scope.medicalFitnessSMSRequired = false;
                                    $scope.medicalFitnessMessageRequired = true;
                                }
                                if ($scope.medicalFitnessNotificationType == 'Both') {
                                    $scope.notificationmedicalFitnessTypeSMSShow = true;
                                    $scope.notificationmedicalFitnessTypeEmailShow = true;
                                    $scope.medicalFitnessSMSRequired = true;
                                    $scope.medicalFitnessMessageRequired = true;
                                }
                                if ($scope.policeVerificationNotificationType == 'SMS') {
                                    $scope.notificationPoliceVerificationTypeSMSShow = true;
                                    $scope.notificationPoliceVerificationTypeEmailShow = false;
                                    $scope.policeVerificationSMSRequired = true;
                                    $scope.policeVerificationMessageRequired = false;
                                }
                                if ($scope.policeVerificationNotificationType == 'Email') {
                                    $scope.notificationPoliceVerificationTypeSMSShow = false;
                                    $scope.notificationPoliceVerificationTypeEmailShow = true;
                                    $scope.policeVerificationSMSRequired = false;
                                    $scope.policeVerificationMessageRequired = true;
                                }
                                if ($scope.policeVerificationNotificationType == 'Both') {
                                    $scope.notificationPoliceVerificationTypeSMSShow = true;
                                    $scope.notificationPoliceVerificationTypeEmailShow = true;
                                    $scope.policeVerificationSMSRequired = true;
                                    $scope.policeVerificationMessageRequired = true;
                                }
                                if ($scope.DDTrainingNotificationType == 'SMS') {
                                    $scope.DDTrainingTypeSMSShow = true;
                                    $scope.DDTrainingTypeEmailShow = false;
                                    $scope.DDTrainingSMSRequired = true;
                                    $scope.DDTrainingMessageRequired = false;
                                }
                                if ($scope.DDTrainingNotificationType == 'Email') {
                                    $scope.DDTrainingTypeSMSShow = false;
                                    $scope.DDTrainingTypeEmailShow = true;
                                    $scope.DDTrainingSMSRequired = false;
                                    $scope.DDTrainingMessageRequired = true;
                                }
                                if ($scope.DDTrainingNotificationType == 'Both') {
                                    $scope.DDTrainingTypeSMSShow = true;
                                    $scope.DDTrainingTypeEmailShow = true;
                                    $scope.DDTrainingSMSRequired = true;
                                    $scope.DDTrainingMessageRequired = true;
                                }
                                if ($scope.pollutionDueNotificationType == 'SMS') {
                                    $scope.notificationPollutionDueSMSShow = true;
                                    $scope.notificationPollutionDueEmailShow = false;
                                    $scope.pollutionDueSMSRequired = true;
                                    $scope.pollutionDueMessageRequired = false;
                                }
                                if ($scope.pollutionDueNotificationType == 'Email') {
                                    $scope.notificationPollutionDueSMSShow = false;
                                    $scope.notificationPollutionDueEmailShow = true;
                                    $scope.pollutionDueSMSRequired = false;
                                    $scope.pollutionDueMessageRequired = true;
                                }
                                if ($scope.pollutionDueNotificationType == 'Both') {
                                    $scope.notificationPollutionDueSMSShow = true;
                                    $scope.notificationPollutionDueEmailShow = true;
                                    $scope.pollutionDueSMSRequired = true;
                                    $scope.pollutionDueMessageRequired = true;
                                }
                                if ($scope.insuranceDueNotificationType == 'SMS') {
                                    $scope.notificationInsuranceDueSMSShow = true;
                                    $scope.notificationInsuranceDueEmailShow = false;
                                    $scope.insuranceDueSMSRequired = true;
                                    $scope.insuranceDueMessageRequired = false;
                                }
                                if ($scope.insuranceDueNotificationType == 'Email') {
                                    $scope.notificationInsuranceDueSMSShow = false;
                                    $scope.notificationInsuranceDueEmailShow = true;
                                    $scope.insuranceDueSMSRequired = false;
                                    $scope.insuranceDueMessageRequired = true;
                                }
                                if ($scope.insuranceDueNotificationType == 'Both') {
                                    $scope.notificationInsuranceDueSMSShow = true;
                                    $scope.notificationInsuranceDueEmailShow = true;
                                    $scope.insuranceDueSMSRequired = true;
                                    $scope.insuranceDueMessageRequired = true;
                                }
                                if ($scope.taxCertificateNotificationType == 'SMS') {
                                    $scope.notificationTaxCertificateSMSShow = true;
                                    $scope.notificationTaxCertificateEmailShow = false;
                                    $scope.taxCertificateSMSRequired = true;
                                    $scope.taxCertificateMessageRequired = false;
                                }
                                if ($scope.taxCertificateNotificationType == 'Email') {
                                    $scope.notificationTaxCertificateSMSShow = false;
                                    $scope.notificationTaxCertificateEmailShow = true;
                                    $scope.taxCertificateSMSRequired = false;
                                    $scope.taxCertificateMessageRequired = true;
                                }
                                if ($scope.taxCertificateNotificationType == 'Both') {
                                    $scope.notificationTaxCertificateSMSShow = true;
                                    $scope.notificationTaxCertificateEmailShow = true;
                                    $scope.taxCertificateSMSRequired = true;
                                    $scope.taxCertificateMessageRequired = true;
                                }
                                if ($scope.permitDueNotificationType == 'SMS') {
                                    $scope.notificationPermitDueSMSShow = true;
                                    $scope.notificationPermitDueEmailShow = false;
                                    $scope.permitDueSMSRequired = true;
                                    $scope.permitDueMessageRequired = false;
                                }
                                if ($scope.permitDueNotificationType == 'Email') {
                                    $scope.notificationPermitDueSMSShow = false;
                                    $scope.notificationPermitDueEmailShow = true;
                                    $scope.permitDueRequired = false;
                                    $scope.permitDueMessageRequired = true;
                                }
                                if ($scope.permitDueNotificationType == 'Both') {
                                    $scope.notificationPermitDueSMSShow = true;
                                    $scope.notificationPermitDueEmailShow = true;
                                    $scope.permitDueSMSRequired = true;
                                    $scope.permitDueMessageRequired = true;
                                }
                                if ($scope.vehicelMaintenanceNotificationType == 'SMS') {
                                    $scope.notificationVehicelMaintenanceSMSShow = true;
                                    $scope.notificationVehicelMaintenanceEmailShow = false;
                                    $scope.vehicelMaintenanceSMSRequired = true;
                                    $scope.vehicelMaintenanceMessageRequired = false;
                                }
                                if ($scope.vehicelMaintenanceNotificationType == 'Email') {
                                    $scope.notificationVehicelMaintenanceSMSShow = false;
                                    $scope.notificationVehicelMaintenanceEmailShow = true;
                                    $scope.vehicelMaintenanceSMSRequired = false;
                                    $scope.vehicelMaintenanceMessageRequired = true;
                                }
                                if ($scope.vehicelMaintenanceNotificationType == 'Both') {
                                    $scope.notificationVehicelMaintenanceSMSShow = true;
                                    $scope.notificationVehicelMaintenanceEmailShow = true;
                                    $scope.vehicelMaintenanceSMSRequired = true;
                                    $scope.vehicelMaintenanceMessageRequired = true;
                                }

                                $scope.personalDeviceViaSms = data.personalDeviceViaSms;
                                $scope.assignRoutesToVendor = data.assignRoutesToVendor;
                                $scope.driverCallToEmployee = {
                                    value: data.driverCallToEmployee
                                };
                                $scope.driverCallToTransportDesk = {
                                    value: data.driverCallToTransportDesk
                                };
                                $scope.employeeCallToDriver = {
                                    value: data.employeeCallToDriver
                                };
                                $scope.employeeCallToTransport = {
                                    value: data.employeeCallToTransport
                                };
                                $scope.licenseExpiryDate = data.licenseExpiryDate;
                                $scope.repeatAlerts = String(data.repeatAlerts);
                                $scope.notificationType = data.notificationType;
                                $scope.notificationTypeSMS = data.notificationTypeSMS;
                                $scope.notificationTypeEmail = data.notificationTypeEmail;
                                $scope.destinationPointLimit = data.destinationPointLimit;
                                $scope.requestLocation = data.requestLocation;
                                $scope.locationVisible = data.locationVisible;
                                $scope.invoiceGenType = data.invoiceGenType;
                                $scope.licenseExpiryDays = data.licenseExpiryDays;
                                $scope.backToBackType = _(data.backToBackType).capitalize();

                                $scope.approvalNeeded = data.managerApproval;
                                $scope.etaSMS = String(data.etaSMS);
                                $scope.geoFenceArea = String(data.employeeAddressGeoFenceArea);
                                $scope.after13HrSMSContact = data.shiftTimePlusOneHourrAfterSMSContact;
                                $scope.after14HrSMSContact = data.shiftTimePlusTwoHourrAfterSMSContact;
                                $scope.maxSpeedArray = String(data.maxSpeed);
                                $scope.driverCheckedout = data.driverAutoCheckedoutTime;
                                $scope.delayMessage = String(data.delayMessage);
                                $scope.employeeWaitingTime = String(data.employeeWaitingTime);
                                $scope.emrgContactNumber = data.emrgContactNumber;
                                $scope.maxTravelTime = data.maxTravelTimeEmployeeWiseInMin;
                                $scope.maxRadialDistance = data.maxRadialDistanceEmployeeWiseInKm;
                                $scope.maxRouteLength = data.maxRouteLengthInKm;
                                $scope.cutOffTimeFlg = data.cutOffTimeFlg;

                                $scope.maxRoutedeviation = data.maxRouteDeviationInKm;
                                $scope.transportContactNumber = data.transportContactNumberForMsg;
                                $scope.employeeAppReportBug = data.employeeAppReportBug;
                                $scope.reportBugEmailIds = data.reportBugEmailIds;
                                $scope.toEmployeeFeedBackEmail = data.toEmployeeFeedBackEmail;
                                $scope.employeeFeedbackEmailId = data.employeeFeedbackEmailId;
                                $scope.employeeFeedbackEmail = data.employeeFeedbackEmail;
                                $scope.autoClustring = data.autoClustring; // future will be change

                                $scope.autoTripStartGF = String(data.startTripGeoFenceArea);
                                $scope.autoTripEndGF = String(data.endTripGeoFenceArea);
                                $scope.transportFeedbackEmailId = data.feedBackEmailId;
                                $scope.clusterSize = data.clusterSize;

                                $scope.driverImage = data.driverImageForEmployee;
                                $scope.driverMobileNuumber = data.driverMobileNumberForEmployee;
                                $scope.employeeCheckOutGeofenceRegion = data.employeeCheckOutGeofenceRegion
                                $scope.driverName = data.driverNameForEmployee;
                                $scope.profilePic = data.driverProfilePicOnDriverDevice;
                                $scope.autoCallSmsDisable = data.autoCallAndSMSOnDriverDevice;

                                $scope.pickDropNeeded = data.tripType;
                                $scope.gotResultAppShift = true;
                                $scope.adminRequired = String(data.numberOfAdmin);
                                $scope.adminInactiveAccount = String(data.inactiveAdminAccountAfterNumOfDays);

                                $scope.resetPaswwordForAdmin = String(data.passwordResetPeriodForAdmin);
                                $scope.resetPaswwordForUser = String(data.passwordResetPeriodForUser);
                                $scope.twoWayFactor = data.twoFactorAuthenticationRequired;
                                $scope.numberOfAttemptsWrongPass = String(data.numberOfWrongPassAttempt);
                                $scope.sessiontimeOut = String(data.sessionTimeOut);
                                $scope.invoiceNumberRange = String(data.invoiceNumberDigits);
                                $scope.adhocTimePicker = data.adhocTimePicker;
                                $scope.dropPriorTime = data.dropPriorTime;
                                $scope.pickupPriorTime = data.pickupPriorTime;
                                $scope.empSecondNormalPickup = data.employeeSecondPickUpRequest;
                                $scope.empSecondNormalDrop = data.employeeSecondDropRequest;
                                $scope.panicAlertNeeded = data.panicAlertNeeded;
                                $scope.lastPasswordNotCurrent = String(data.lastPassCanNotCurrentPass);
                                $scope.autoDropRoster = data.autoDropRoster;
                                $scope.shiftTimeDifference = data.shiftTimeDiffPickToDrop;
                                $scope.addDistanceRecoverGeoFence = String(data.addingGeoFenceDistanceIntrip);
                                $scope.invoiceNoOfDay = String(data.invoiceNoOfDays);
                                $scope.cutOff.cutOffTime = $scope.cutOffTimeFlg;
                                $scope.shiftWise = 'N';
                                $scope.rescheduleDrop = data.rescheduleDropCutOffTime;
                                $scope.reschedulePickup = data.reschedulePickupCutOffTime;

                                $scope.invoiceGenDate = data.invoiceGenDate;
                                $scope.dropCancelCutOffTime = data.dropCancelCutOffTime;
                                $scope.pickupCancelCutOffTime = data.pickupCancelCutOffTime;
                                $scope.routeGeofence = 1; // future will be change
                                $scope.distanceFlg = data.distanceFlg;

                                $scope.requestCutOffNoOfDays = data.requestCutOffNoOfDays;

                                $scope.distanceFlg = data.distanceFlg;

                                $scope.monthOrDays = data.monthOrDays;



                                $scope.autoVehicleAllocationStatus = data.autoVehicleAllocationStatus;
                                $scope.driverAutoCheckoutStatus = data.driverAutoCheckoutStatus;

                                // Back To Back Parameters

                                $scope.driverWaitingTime = data.driverWaitingTime;

                                $scope.b2bByTravelDistanceInKM = data.b2bByTravelDistanceInKM;
                                $scope.b2bByTravelTimeInMinutes = data.b2bByTravelTimeInMinutes;

                                // Back To Back Travel Time In Minutes Converstion

                                $scope.timeConverstionb2bMinutes = [];
                                $scope.timeConverstionb2bMinutes = $scope.b2bByTravelTimeInMinutes
                                    .split(':');

                                var todaydateb2bMinutes = new Date();
                                todaydateb2bMinutes.setHours(
                                    $scope.timeConverstionb2bMinutes[0],
                                    $scope.timeConverstionb2bMinutes[1],
                                    $scope.timeConverstionb2bMinutes[2]);

                                $scope.b2bByTravelTimeInMinutes = todaydateb2bMinutes;

                                // Driver Waiting Time Converstion

                                $scope.timeConverstiondriverWaitingTime = [];
                                $scope.timeConverstiondriverWaitingTime = $scope.driverWaitingTime
                                    .split(':');

                                var todaydatedriverWaitingTime = new Date();
                                todaydatedriverWaitingTime.setHours(
                                    $scope.timeConverstiondriverWaitingTime[0],
                                    $scope.timeConverstiondriverWaitingTime[1],
                                    $scope.timeConverstiondriverWaitingTime[2]);

                                $scope.driverWaitingTime = todaydatedriverWaitingTime;

                                // Notification Cutoff Time For Pickup

                                $scope.notificationCutoffTimePickup = data.notificationCutoffTimePickup;
                                $scope.timeConverstionNotificationCutoffTimePickup = [];
                                $scope.timeConverstionNotificationCutoffTimePickup = $scope.notificationCutoffTimePickup
                                    .split(':');

                                var todaydatenotificationCutoffTimePickup = new Date();
                                todaydatenotificationCutoffTimePickup.setHours(
                                    $scope.timeConverstionNotificationCutoffTimePickup[0],
                                    $scope.timeConverstionNotificationCutoffTimePickup[1],
                                    $scope.timeConverstionNotificationCutoffTimePickup[2]);

                                $scope.notificationCutoffTimePickup = todaydatenotificationCutoffTimePickup;

                                // Notification Cutoff Time For DROP

                                $scope.notificationCutoffTimeDrop = data.notificationCutoffTimeDrop;
                                $scope.timeConverstionNotificationCutoffTimeDrop = [];
                                $scope.timeConverstionNotificationCutoffTimeDrop = $scope.notificationCutoffTimeDrop
                                    .split(':');

                                var todaydatenotificationCutoffTimeDrop = new Date();
                                todaydatenotificationCutoffTimeDrop.setHours(
                                    $scope.timeConverstionNotificationCutoffTimeDrop[0],
                                    $scope.timeConverstionNotificationCutoffTimeDrop[1],
                                    $scope.timeConverstionNotificationCutoffTimeDrop[2]);

                                $scope.notificationCutoffTimeDrop = todaydatenotificationCutoffTimeDrop;


                                $scope.earlyRequestDateEveryMonth = Number(data.earlyRequestDate);
                                $scope.earlyRequestDateCustomizeDate = Number(data.earlyRequestDate);
                                $scope.earlyRequestDatePerDay = data.earlyRequestDate.toString();
                                $scope.autoDriverCheckout = data.autoDriverCheckout;
                                $scope.autoVehicleAllocation = data.autoVehicleAllocation;
                                $scope.requestFromDateCutOff = new Date(data.requestDateCutOffFromDate);
                                $scope.requestToDateCutOff = new Date(data.requestToDateCutOff);
                                $scope.occurrenceFlg = data.occurrenceFlg;
                                if ($scope.employeeFeedbackEmail == 'Yes') {
                                    $scope.employeeFeedbackEmailIdSection = true;
                                } else {
                                    $scope.employeeFeedbackEmailIdSection = false;
                                    $scope.employeeFeedbackEmailId = "No";
                                    $scope.toEmployeeFeedBackEmail = "No";
                                }
                                if ($scope.employeeAppReportBug == 'Yes') {
                                    $scope.reportBugEmailIdSection = true;
                                } else {
                                    $scope.reportBugEmailIdSection = false;
                                    $scope.reportBugEmailIds = "No";
                                }
                                var daySplit = data.daysRequest.toString();
                                $scope.dynamicDaysData = [];
                                angular.forEach(daySplit, function(value, key) {

                                    var value = Number(value);

                                    if (value == 1) {
                                        var Sunday = "Sunday";
                                        $scope.dynamicDaysData.push(Sunday);
                                    }
                                    if (value == 2) {
                                        var Monday = "Monday";
                                        $scope.dynamicDaysData.push(Monday);
                                    }
                                    if (value == 3) {
                                        var Tuesday = "Tuesday";
                                        $scope.dynamicDaysData.push(Tuesday);
                                    }
                                    if (value == 4) {
                                        var Wednesday = "Wednesday";
                                        $scope.dynamicDaysData.push(Wednesday);
                                    }
                                    if (value == 5) {
                                        var Thursday = "Thursday";
                                        $scope.dynamicDaysData.push(Thursday);
                                    }
                                    if (value == 6) {
                                        var Friday = "Friday";
                                        $scope.dynamicDaysData.push(Friday);
                                    }
                                    if (value == 7) {
                                        var Saturday = "Saturday";
                                        $scope.dynamicDaysData.push(Saturday);
                                    }
                                });

                                $scope.dynamicDays = $scope.dynamicDaysData;



                                if (data.daysRequest == '0' || data.daysRequest == null) {

                                    $scope.dynamicDays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
                                }


                                if (data.occurrenceFlg == 'N') {
                                    $scope.occurenceCutOff = false;
                                } else {
                                    $scope.occurenceCutOff = true;
                                }


                                if (data.requestType == "Days") {
                                    $scope.earlyRequestDate = data.earlyRequestDate;
                                    $scope.requestType = 1;
                                    $scope.occurrenceFlg = 'Y';
                                }

                                if (data.requestType == "Date") {
                                    $scope.earlyRequestDate = data.earlyRequestDate;
                                    $scope.requestType = 2;
                                }

                                if (data.requestType == "Date" && data.monthOrDays == "everymonthlastdate") {
                                    $scope.earlyRequestDate = data.earlyRequestDate;
                                    $scope.requestType = 3;
                                }


                                if (data.requestType == "custDays") {
                                    $scope.earlyRequestDate = data.earlyRequestDate;
                                    $scope.earlyRequestDatePerDay = data.earlyRequestDate.toString();
                                    if (data.earlyRequestDate >= 7) {

                                        $scope.earlyRequestDatePerDay = "0";
                                    }

                                    $scope.requestType = 4;
                                }

                                if (data.requestType == "multipleDays") {
                                    $scope.earlyRequestDate = data.earlyRequestDate;
                                    $scope.requestType = 5;
                                }


                                $scope.singleDay = String(data.daysRequest);


                                if (data.daysRequest >= 7 || data.daysRequest == 0) {
                                    $scope.singleDay = "0";
                                }

                                $scope.multipleDaysStart = String(data.daysRequest);
                                $scope.multipleDaysEnd = String(data.daysRequest);




                                $scope.dateValid = false;


                                if ($scope.setMinDate <= $scope.requestFromDateCutOff) {

                                    $scope.setMinDate = new Date();
                                } else {
                                    $scope.dateValid = true;
                                    $scope.setMinDate = $scope.requestFromDateCutOff;
                                }



                                // Drop Converstion

                                $scope.timeConverstion = [];
                                $scope.timeConverstion = $scope.dropPriorTime
                                    .split(':');

                                var todaydate = new Date();
                                todaydate.setHours(
                                    $scope.timeConverstion[0],
                                    $scope.timeConverstion[1],
                                    $scope.timeConverstion[2]);

                                $scope.dropPriorTime = todaydate;


                                // Pickup Converstion

                                $scope.timeConverstionPickup = [];
                                $scope.timeConverstionPickup = $scope.pickupPriorTime
                                    .split(':');

                                var todaydatePickup = new Date();
                                todaydatePickup.setHours(
                                    $scope.timeConverstionPickup[0],
                                    $scope.timeConverstionPickup[1],
                                    $scope.timeConverstionPickup[2]);
                                $scope.pickupPriorTime = todaydatePickup;



                                // ShiftTime Difference Converstion

                                $scope.timeConverstionDifference = [];
                                $scope.timeConverstionDifference = $scope.shiftTimeDifference
                                    .split(':');

                                var todaydateDifference = new Date();
                                todaydateDifference
                                    .setHours(
                                        $scope.timeConverstionDifference[0],
                                        $scope.timeConverstionDifference[1],
                                        $scope.timeConverstionDifference[2]);
                                $scope.shiftTimeDifference = todaydateDifference;

                                // Cancel Drop Converstion

                                $scope.timeConverstionCancelDrop = [];
                                $scope.timeConverstionCancelDrop = $scope.dropCancelCutOffTime
                                    .split(':');

                                var todaydateCancelDrop = new Date();
                                todaydateCancelDrop
                                    .setHours(
                                        $scope.timeConverstionCancelDrop[0],
                                        $scope.timeConverstionCancelDrop[1],
                                        $scope.timeConverstionCancelDrop[2]);
                                $scope.dropCancelCutOffTime = todaydateCancelDrop;

                                // Cancel Pickup Converstion

                                $scope.timeConverstionCancelPickup = [];
                                $scope.timeConverstionCancelPickup = $scope.pickupCancelCutOffTime
                                    .split(':');

                                var todaydateCancelPickup = new Date();
                                todaydateCancelPickup
                                    .setHours(
                                        $scope.timeConverstionCancelPickup[0],
                                        $scope.timeConverstionCancelPickup[1],
                                        $scope.timeConverstionCancelPickup[2]);
                                $scope.pickupCancelCutOffTime = todaydateCancelPickup;

                                // driverCheckedout Converstion

                                $scope.timeConverstionCheckedout = [];
                                $scope.timeConverstionCheckedout = $scope.driverCheckedout
                                    .split(':');

                                var todaydateCheckedout = new Date();
                                todaydateCheckedout
                                    .setHours(
                                        $scope.timeConverstionCheckedout[0],
                                        $scope.timeConverstionCheckedout[1],
                                        $scope.timeConverstionCheckedout[2]);
                                $scope.driverCheckedout = todaydateCheckedout;

                                // rescheduleDrop Converstion

                                $scope.timeConverstionrescheduleDrop = [];
                                $scope.timeConverstionrescheduleDrop = $scope.rescheduleDrop
                                    .split(':');

                                var todaydaterescheduleDrop = new Date();
                                todaydaterescheduleDrop
                                    .setHours(
                                        $scope.timeConverstionrescheduleDrop[0],
                                        $scope.timeConverstionrescheduleDrop[1],
                                        $scope.timeConverstionrescheduleDrop[2]);
                                $scope.rescheduleDrop = todaydaterescheduleDrop;

                                // reschedulePickup Converstion

                                $scope.timeConverstionreschedulePickup = [];
                                $scope.timeConverstionreschedulePickup = $scope.reschedulePickup
                                    .split(':');

                                var todaydatereschedulePickup = new Date();
                                todaydatereschedulePickup
                                    .setHours(
                                        $scope.timeConverstionreschedulePickup[0],
                                        $scope.timeConverstionreschedulePickup[1],
                                        $scope.timeConverstionreschedulePickup[2]);
                                $scope.reschedulePickup = todaydatereschedulePickup;


                            }
                            $scope.applicationSettingsTab = true;
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });

            };

            $scope.user = {};
            $scope.user_Type;
            $scope.isCollapsed = false;
            $scope.isEdit = false;
            $scope.isProfileEdit = false;
            $scope.changePasswordClicked = false;
            $scope.isChangeLocation = false;
            $scope.mapClick = false;
            $scope.currLocation = "Irving, Texas";
            $scope.users = [{
                'value': 'admin',
                'text': 'admin'
            }, {
                'value': 'supervisor',
                'text': 'supervisor'
            }, {
                'value': 'manager',
                'text': 'manager'
            }, {
                'value': 'webuser',
                'text': 'webuser'
            }];
            $scope.tripTypes = [{
                'value': 'PICKUP',
                'text': 'PICKUP'
            }, {
                'value': 'DROP',
                'text': 'DROP'
            }];

            $scope.trip = {};
            $scope.trip.tripType = $scope.tripTypes[0];
            $scope.cutOffTimes = [{
                'value': 'S',
                'text': 'Shift Time'
            }, {
                'value': 'T',
                'text': 'Trip Type'
            }, ];
            $scope.cutOffNotifications = [{
                'value': 'S',
                'text': 'Shift Time'
            }, {
                'value': 'T',
                'text': 'Trip Type'
            }];
            $scope.shiftWiseData = [{
                'value': 'S',
                'text': 'Shift Wise'
            }, {
                'value': 'N',
                'text': 'Normal'
            }, ];

            $scope.requestTypesInvoice = {
                availableOptions: [{
                        value: "days",
                        requestTypes: 'No Of Days'
                    },
                    {
                        value: "date",
                        requestTypes: 'Date Wise'
                    }
                ],
                selectedOption: {
                    value: "days",
                    requestTypes: 'No Of Days'
                }
            };

            $scope.cutOff = {};
            $scope.cutOffNote = {};

            $scope.mobileVisibleOption = [{
                'value': 'W',
                'mobileVisibleFlg': 'Web Console'
            }, {
                'value': 'E',
                'mobileVisibleFlg': 'Employee App'
            }, {
                'value': 'NP',
                'mobileVisibleFlg': 'Nodal Point'
            }, {
                'value': 'B',
                'mobileVisibleFlg': 'Both'
            }];

            $scope.mobileVisible = {};

            $scope.shiftBufferTimeData = [{
                'value': '5',
                'bufferTime': '5'
            }, {
                'value': '10',
                'bufferTime': '10'
            }, {
                'value': '15',
                'bufferTime': '15'
            }, {
                'value': '20',
                'bufferTime': '20'
            }, {
                'value': '25',
                'bufferTime': '25'
            }, {
                'value': '30',
                'bufferTime': '30'
            }];

            $scope.baseLocation = 'Office';
            $scope.newAddress_baseLocation;
            $scope.regExName = /^[A-Za-z]+$/;
            $scope.rolelist;
            var originalShitTime;
            $scope.addNewShiftIsClicked = false;
            $scope.hstep = 1;
            $scope.mstep = 1;
            $scope.ismeridian = false;

            $scope.requestsData = [];

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
                return currentDate + '-' + currentMonth + '-' +
                    convert_date.getFullYear();
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

            //Convert to hh

            var convertToTimehour = function(newdate) {
                d = new Date(newdate);
                hr = d.getHours();
                min = d.getMinutes();
                if (hr < 10) {
                    hr = '0' + hr;
                }
                if (min < 10) {
                    min = '0' + min;
                }
                return hr;
            };

            var convertToTimeMin = function(newdate) {
                d = new Date(newdate);
                hr = d.getHours();
                min = d.getMinutes();
                if (hr < 10) {
                    hr = '0' + hr;
                }
                if (min < 10) {
                    min = '0' + min;
                }
                return min;
            };

            $scope.initializeTime = function() {
                var d = new Date();
                d.setHours(00);
                d.setMinutes(0);
                return d;
            };

            $scope.initializeTime2 = function(hr, min) {
                var d = new Date();
                d.setHours(hr);
                d.setMinutes(min);
                return d;
            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1,
                showWeeks: false
            };
            $scope.openDobCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'dobDate': true
                    };
                }, 50);
            };
            $scope.setCutOfNotificationType = function(cutOffNote) {

                if (cutOffNote == "S") {
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/cutOfNotificationRouteModal.jsp',
                        controller: 'cutOffNoteRouteCtrl',
                        size: 'lg',
                        backdrop: 'static',
                    });

                    modalInstance.result.then(function(result) {});
                } else {}
            }
            $scope.autoNotificationRouteClose = function(cutOffNote) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/cutOfNotificationModal.jsp',
                    controller: 'cutOffNoteCtrl',
                    size: 'lg',
                    backdrop: 'static',

                });
                modalInstance.result.then(function(result) {});
            }
            $scope.getProfileDetail = function() {
                $scope.progressbar.start();
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http
                    .post('services/user/loginuser/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.myProfileData = data;
                                $scope.user = {
                                    userName: $scope.myProfileData[0].userName,
                                    firstName: $scope.myProfileData[0].firstName,
                                    lastName: $scope.myProfileData[0].lastName,
                                    country: $scope.myProfileData[0].country,
                                    dob: new Date(
                                        $scope.myProfileData[0].birthDate),
                                    occupation: $scope.myProfileData[0].designation,
                                    email: $scope.myProfileData[0].emailId,
                                    interest: $scope.myProfileData[0].interest,
                                    website: $scope.myProfileData[0].emailId,
                                    mobileNumber: $scope.myProfileData[0].number,
                                    about: $scope.myProfileData[0].address
                                };
                                $scope.progressbar.complete();
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };

            $scope.shiftDataChange = function(shiftWise) {

                if (shiftWise == 'N') {
                    $scope.dataChangeShift = false;
                    $scope.shiftSelectType = 'Normal';
                    $scope.shiftTimeRequired = false;
                } else {
                    $scope.shiftSelectType = 'ShiftWise';
                    $scope.shiftTimeRequired = true;

                }
                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            userId: profileId
                        }
                    },
                    tripType: 'DROP',
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/trip/tripshiftime/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.shiftsTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
                        }

                    }).error(function(data, status, headers, config) {});

            }

            $scope.setTripTypeliveTracking = function(tripType) {

                $scope.shiftTimeDisable = true;
                $scope.shiftTimeEmployee = true;
                $scope.employeeRosterTable = false;
                $scope.summaryLengthShift = 0;
                $scope.tripType = tripType.value;

                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            userId: profileId
                        }
                    },
                    tripType: tripType.value,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/trip/tripshiftime/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.shiftsTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
                        }

                    }).error(function(data, status, headers, config) {});

            };

            $scope.shiftWiseChange = function(shiftTimes, tripType) {

                $scope.shiftTime = shiftTimes.shiftTime;
                $scope.geoFenceAreaShow = true;

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    time: $scope.shiftTime,
                    tripType: tripType.value,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };

                $http.post('services/user/getShiftDetail/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.autoClustring = data.areaGeofence;
                            $scope.clusterSize = data.clusterGeofence;
                            $scope.routeGeofence = data.routeGeofence;
                        }

                    }).error(function(data, status, headers, config) {});

            }

            //All Regular master request details
            $scope.getRequests = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.progressbar.start();
                $scope.requestsDataShow = false;

                tripTypeSelected = $scope.trip.tripType.value;
                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    tripType: $scope.trip.tripType.value,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/user/allrequestdetailtype/', data)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.requestsData = data;
                            $scope.requestsDataShow = true;

                            $scope.progressbar.complete();
                        }
                    }).error(function(data, status, headers, config) {
                        // log error
                    });
                // Area Name List
                /*var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId,
                        userId: profileId
                    },
                    combinedFacility: combinedFacility
                };
                $http.post('services/zones/allAreas/').success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.areaDetails = data.areaDetails;
                        }

                    }).error(function(data, status, headers, config) {});*/
            };



            $scope.initializeTime3 = function(hr, min) {
                var d = new Date();
                d.setHours(hr);
                d.setMinutes(min);
                return d;
            };

            // Edit Request Details

            $scope.editRequestDetail = function(requests, index) {
                var pickupTime = $scope.requestsData[index].pickupTime
                    .split(':');
                requests.pickupTime = $scope.initializeTime2(pickupTime[0],
                    pickupTime[1]);
                requests.editRequestDetailIsClicked = true;
            };

            // Update Request Details

            $scope.updateRequestDetail = function(requests, index) {

                $scope.requestsData[index].pickupTime = convertToTime(requests.pickupTime);
                requests.editRequestDetailIsClicked = false;

                var data = {
                    branchId: branchId,
                    routeName: requests.routeName,
                    areaName: requests.areaName,
                    time: requests.pickupTime,
                    tripId: requests.tripId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };

                $http
                    .post('services/user/updateMasterRequestDetails/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                ngDialog
                                    .open({
                                        template: 'Requests Detail Updated Successfully',
                                        plain: true
                                    });
                            }
                        }).error(
                        function(data, status, headers, config) {

                        });

            };

            $scope.enableRequest = function(device) {
                $confirm({
                        text: "Are you sure you want to Enable this request?",
                        title: 'Enable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                efmFmUserMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                userId: profileId,
                                tripId: device.tripId,
                                status: 'Y',
                                readFlg: 'Y',
                                combinedFacility: combinedFacility
                            };
                            $http.post('services/user/updaterequestdetail/', data)
                                .success(function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        device.status = 'Y',
                                            ngDialog
                                            .open({
                                                template: 'Request Enable Successfully',
                                                plain: true
                                            });
                                    }
                                }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        })
            };

            $scope.disableRequest = function(device) {
                $confirm({
                        text: "Are you sure you want to Disable this request?",
                        title: 'Disable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                efmFmUserMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                userId: profileId,
                                tripId: device.tripId,
                                status: 'N',
                                readFlg: 'N',
                                combinedFacility: combinedFacility
                            };
                            $http.post('services/user/updaterequestdetail/', data)
                                .success(function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        device.status = 'N'
                                        ngDialog
                                            .open({
                                                template: 'Request Disable Successfully',
                                                plain: true
                                            });
                                    }
                                }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        });
            };


            $scope.enableRequestShift = function(shift) {

                $confirm({
                        text: "Are you sure you want to Enable Ceiling?",
                        title: 'Enable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {

                            var modalInstance = $modal.open({
                                templateUrl: 'partials/modals/ceilingShiftTime.jsp',
                                controller: 'ceilingShiftTimeCtrl',
                                size: 'sm',
                                resolve: {
                                    shift: function() {
                                        return shift;
                                    }
                                }
                            });
                        })


            };

            $scope.disableRequestShift = function(shift) {

                $confirm({
                        text: "Are you sure you want to Disable Ceiling? , It will disable all your future created request.",
                        title: 'Disable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {

                            switch (shift.mobileVisibleFlg) {
                                case 'Web Console':
                                    shift.mobileVisibleFlg = 'W';
                                    break;
                                case 'Employee App':
                                    shift.mobileVisibleFlg = 'E';
                                    break;
                                case 'Nodal Point':
                                    shift.mobileVisibleFlg = 'NP';
                                    break;
                                case 'Both':
                                    shift.mobileVisibleFlg = 'B';
                                    break;
                                default:
                            }

                            var data = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                userId: profileId,
                                shiftId: shift.shiftId,
                                time: shift.shiftTime,
                                cut_Off_Time: shift.cutOffTime,
                                tripType: shift.tripType,
                                mobileVisibleFlg: shift.mobileVisibleFlg,
                                cancel_Cut_Off_Time: shift.CancelCutOffTime,
                                shiftBufferTime: shift.bufferTime,
                                reschedule_Cut_Off_Time: shift.RescheduleCutOffTime,
                                ceilingNo: shift.ceilingNo,
                                ceilingFlg: "N",
                                combinedFacility: combinedFacility
                            };

                            $http.post('services/user/updateCutOffTime/', data)
                                .success(function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        shift.ceilingFlg = 'N'
                                        ngDialog
                                            .open({
                                                template: 'Ceiling Disable Successfully',
                                                plain: true
                                            });
                                    }
                                }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        })
            };



            $scope.getAllShifts = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.gotShiftTimeResultsShow = false;
                $scope.addNewShiftIsClicked = false;
                $scope.ceilingNoFlg = false;

                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/trip/shiftime/', data)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.shiftTimeData = data.shift;
                            $scope.gotShiftTimeResultsShow = true;
                            angular
                                .forEach(
                                    $scope.shiftTimeData,
                                    function(item) {

                                        if (item.genderPreference == 'M') {
                                            item.genderPreference = 'Male'
                                        }
                                        if (item.genderPreference == 'F') {
                                            item.genderPreference = 'Female'
                                        }
                                        if (item.genderPreference == 'B') {
                                            item.genderPreference = 'Both'
                                        }


                                        if (item.mobileVisibleFlg == 'W') {
                                            item.mobileVisibleFlg = 'Web Console';
                                        }

                                        if (item.mobileVisibleFlg == 'NP') {
                                            item.mobileVisibleFlg = 'Nodal Point';
                                        }

                                        if (item.mobileVisibleFlg == 'E') {
                                            item.mobileVisibleFlg = 'Employee App';
                                        }

                                        if (item.mobileVisibleFlg == 'B') {
                                            item.mobileVisibleFlg = 'Both';
                                        }

                                        item.editShiftTimeIsClicked = false;
                                        item.timePicker = $scope
                                            .initializeTime();

                                    });
                        }
                    }).error(
                        function(data, status, headers, config) {
                            // log error
                        });

            };

            $rootScope.$on('ceilingValuesData', function(event, data) {

                $scope.getAllShifts();

            });

            $scope.getAllRouteNames = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId,
                        userId: profileId
                    },
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/trip/allzone/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.routeNameData = data.zones;
                            angular.forEach($scope.routeNameData,
                                function(item) {
                                    item.editRouteNameIsClicked = false;
                                });
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });

            };


            $scope.getAllGeoLocations = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                $rootScope.geoLocationsData = [];
                $rootScope.enableLocation = [];
                $rootScope.disableLocation = [];
                var objectData = {};

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'D',
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/employee/allActiveLocation/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            angular.forEach(data, function(item) {
                                item.locationFlg = 'Disable';
                                $rootScope.enableLocation.push(item);
                                $rootScope.geoLocationsData.push(item);
                            });
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'A',
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/employee/allActiveLocation/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            angular.forEach(data, function(item) {
                                item.locationFlg = 'Enable';
                                $rootScope.disableLocation.push(item);

                                $rootScope.geoLocationsData.push(item);
                            });
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });

                $scope.selection = {
                    ids: {}
                };
            }

            // $scope.getAllGeoLocations();
            $scope.getAllCustomMessages = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/message/getAllCustomMessages', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.customMessagesOnLoad = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
            // $scope.getAllCustomMessages();

            $scope.getAllSentSmsHistory = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/message/getAllSentSMSHistory', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.sentSmsHistoryOnLoad = data.status;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
            // $scope.getAllSentSmsHistory();


            $scope.enableAllLocation = function() {
                var geoObject = {};
                var allLocationId = _.pluck($rootScope.enableLocation, 'areaId');
                var dataObj = [];
                angular.forEach(allLocationId, function(item) {
                    var locationId = item;
                    var valueData = true;
                    geoObject[locationId] = valueData;
                    dataObj.push(geoObject);
                });
                $scope.selection = {
                    ids: {}
                };
                $scope.selection.ids = dataObj[0];

                $confirm({
                        text: "Are you sure want to enable all location",
                        title: 'Enable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                userId: profileId,
                                multipleLocation: String(allLocationId),
                                isActive: "D",
                                combinedFacility: combinedFacility
                            }
                            $http.post('services/employee/disableAndEnable/', data).success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        ngDialog.open({
                                            template: 'Location Enabled Successfully',
                                            plain: true
                                        });

                                        $rootScope.geoDataObject = [];
                                        angular.forEach($rootScope.geoLocationsData, function(item) {
                                            item.locationFlg = 'Disable';
                                            $rootScope.geoDataObject.push(item);
                                        })
                                    }
                                }).error(function(data, status, headers, config) {
                                // log error
                            });
                        });

            }

            $scope.disableAllLocation = function() {
                var geoObject = {};
                var allLocationId = _.pluck($rootScope.disableLocation, 'areaId');
                var dataObj = [];
                angular.forEach(allLocationId, function(item) {
                    var locationId = item;
                    var valueData = true;
                    geoObject[locationId] = valueData;
                    dataObj.push(geoObject);
                });
                $scope.selection = {
                    ids: {}
                };
                $scope.selection.ids = dataObj[0];

                $confirm({
                        text: "Are you sure want to disable all location",
                        title: 'Disable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                userId: profileId,
                                multipleLocation: String(allLocationId),
                                isActive: "A",
                                combinedFacility: combinedFacility
                            }
                            $http.post('services/employee/disableAndEnable/', data).success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        ngDialog.open({
                                            template: 'Location Disabled Successfully',
                                            plain: true
                                        });

                                        $rootScope.geoDataObject = [];
                                        angular.forEach($rootScope.geoLocationsData, function(item) {
                                            item.locationFlg = 'Enable';
                                            $rootScope.geoDataObject.push(item);
                                        })
                                    }
                                }).error(function(data, status, headers, config) {
                                // log error
                            });
                        });

            }


            $scope.enableLocation = function(location, index, locationId) {
                var myObj = locationId;
                var myArray = [];
                var locationData;

                function RemoveFalseAndTransformToArray() {
                    for (var key in myObj) {
                        if (myObj[key] === false) {
                            delete myObj[key];
                        } else {
                            myArray.push(key);
                        }
                    }
                }

                RemoveFalseAndTransformToArray();

                var locationDetails = myArray.length;

                if (locationDetails == 0) {
                    locationData = String(location.areaId);
                } else {
                    locationData = String(myArray);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    multipleLocation: locationData,
                    isActive: "A",
                    combinedFacility: combinedFacility
                }
                $http.post('services/employee/disableAndEnable/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == 'notExist') {
                                ngDialog.open({
                                    template: 'Location Not exists',
                                    plain: true
                                });

                            }
                            ngDialog.open({
                                template: 'Location Disabled Successfully',
                                plain: true
                            });
                            location.locationFlg = 'Enable';
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.disableLocation = function(location, index, locationId) {
                var myObj = locationId;
                var myArray = [];
                var locationData;

                function RemoveFalseAndTransformToArray() {
                    for (var key in myObj) {
                        if (myObj[key] === false) {
                            delete myObj[key];
                        } else {
                            myArray.push(key);
                        }
                    }
                }

                RemoveFalseAndTransformToArray();

                var locationDetails = myArray.length;

                if (locationDetails == 0) {
                    locationData = String(location.areaId);
                } else {
                    locationData = String(myArray);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    multipleLocation: locationData,
                    isActive: "D",
                    combinedFacility: combinedFacility
                }
                $http.post('services/employee/disableAndEnable/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template: 'Location Disabled Successfully',
                                plain: true
                            });
                            location.locationFlg = 'Disable';
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });
            }


            $scope.getNodalPoints = function(nodal) {
                var dataObj = {
                    branchId: branchId,
                    routeId: nodal.routeId,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $http.post('services/zones/routeNodelPoints/', dataObj)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            angular.forEach(data, function(item) {
                                angular.forEach(item, function(item) {
                                    if (item.nodalPointFlg == 'D') {
                                        item.nodalPointFlg = 'Disable';
                                    } else {
                                        item.nodalPointFlg = 'Enable';
                                    }
                                });
                            });
                            $rootScope.allNodelPointData = data.routeDetails;
                        }
                    }).error(function(data, status, headers, config) {});
            };

            $scope.enableNodalPoint = function(point, index) {
                $confirm({
                        text: "Are you sure want to enable this nodal point",
                        title: 'Enable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                nodalPointId: point.nodalPointId,
                                branchId: branchId,
                                nodalPointDescription: point.nodalPointDescription,
                                nodalPoints: point.nodalPoints,
                                nodalPointName: point.nodalPointTitle,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            }
                            $http.post('services/zones/updateNodalRoutePointName/', data).success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        ngDialog.open({
                                            template: 'Nodal Point Enabled Successfully',
                                            plain: true
                                        });
                                        angular.forEach($rootScope.allNodelPointData, function(item) {
                                            if (item.nodalPointId == point.nodalPointId) {
                                                item.nodalPointFlg = 'Enable';
                                            }
                                        });
                                    }
                                }).error(function(data, status, headers, config) {
                                // log error
                            });
                        });

            }

            $scope.disableNodalPoint = function(point, index) {

                $confirm({
                        text: "Are you sure want to enable this nodal point",
                        title: 'Enable Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                nodalPointId: point.nodalPointId,
                                branchId: branchId,
                                nodalPointDescription: point.nodalPointDescription,
                                nodalPointFlg: "D",
                                nodalPoints: point.nodalPoints,
                                nodalPointName: point.nodalPointTitle,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            }
                            $http.post('services/zones/updateNodalRoutePointName/', data).success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        ngDialog.open({
                                            template: 'Nodal Point Disabled Successfully',
                                            plain: true
                                        });
                                        angular.forEach($rootScope.allNodelPointData, function(item) {
                                            if (item.nodalPointId == point.nodalPointId) {
                                                item.nodalPointFlg = 'Disable';
                                            }
                                        });
                                    }
                                }).error(function(data, status, headers, config) {
                                // log error
                            });
                        });
            }

            $scope.editNodalPointLocation = function(value) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/nodalPointMapEdit.jsp',
                    controller: 'nodalPointMapEditCtrl',
                    size: 'lg',
                    resolve: {
                        officeLocation: function() {
                            return value.nodalPoints;
                        },
                        point: function() {
                            return value;
                        }

                    }
                });
                modalInstance.result.then(function(result) {
                    var findIndex = _.findIndex($rootScope.allNodelPointData, {
                        nodalPointId: result.nodalPointId
                    });

                    angular.forEach($rootScope.allNodelPointData, function(value, key) {
                        if (findIndex == key) {
                            value.nodalPoints = result.nodalPoints;
                        }
                    });

                });
            }

            $scope.enableLocation = function(location, index, locationId) {
                var myObj = locationId;
                var myArray = [];
                var locationData;

                function RemoveFalseAndTransformToArray() {
                    for (var key in myObj) {
                        if (myObj[key] === false) {
                            delete myObj[key];
                        } else {
                            myArray.push(key);
                        }
                    }
                }

                RemoveFalseAndTransformToArray();

                var locationDetails = myArray.length;

                if (locationDetails == 0) {
                    locationData = String(location.areaId);
                } else {
                    locationData = String(myArray);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    multipleLocation: locationData,
                    isActive: "A",
                    combinedFacility: combinedFacility
                }
                $http.post('services/employee/disableAndEnable/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == 'notExist') {
                                ngDialog.open({
                                    template: 'Location Not exists',
                                    plain: true
                                });

                            }
                            ngDialog.open({
                                template: 'Location Disabled Successfully',
                                plain: true
                            });
                            location.locationFlg = 'Enable';
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.disableLocation = function(location, index, locationId) {
                var myObj = locationId;
                var myArray = [];
                var locationData;

                function RemoveFalseAndTransformToArray() {
                    for (var key in myObj) {
                        if (myObj[key] === false) {
                            delete myObj[key];
                        } else {
                            myArray.push(key);
                        }
                    }
                }

                RemoveFalseAndTransformToArray();

                var locationDetails = myArray.length;

                if (locationDetails == 0) {
                    locationData = String(location.areaId);
                } else {
                    locationData = String(myArray);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    multipleLocation: locationData,
                    isActive: "D",
                    combinedFacility: combinedFacility
                }
                $http.post('services/employee/disableAndEnable/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template: 'Location Disabled Successfully',
                                plain: true
                            });
                            location.locationFlg = 'Disable';
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });
            }


            $scope.unCheckLocation = function() {
                $scope.selection.ids = [];
            }

            $scope.editLocationDetails = function(location, index) {

                $scope.selectedIndex = index;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addMapDestinationModal.jsp',
                    controller: 'editMapDestinationCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        index: function() {
                            return index;
                        },
                        location: function() {
                            return location;
                        }

                    }
                });

                modalInstance.result.then(function(result) {


                    $scope.geoLocationsData[index].areaName = result.locationName;
                    $scope.geoLocationsData[index].locationAddress = result.locationAddress;


                });

            }

            //Message Settings Tab

            $scope.setMessageDetail = function(value) {
                $rootScope.messageValue = value;
                if (value == 'byEmployeeId') {
                    $scope.employeeDetailsIdShow = true;
                } else {
                    $scope.employeeDetailsIdShow = false;
                }

                switch (value) {
                    case 'allEmployee':
                        $scope.messageDetailsHeader = "All Employees";
                        $scope.dataShiftWiseShow = false;
                        $scope.mobileNumberShow = false;
                        $scope.employeeIdShow = false;
                        $scope.msgSendTypeHide = false;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        break;
                    case 'shiftDateWise':
                        $scope.messageDetailsHeader = "Employees Shift Wise & Date Wise";
                        $scope.dataShiftWiseShow = true;
                        $scope.mobileNumberShow = false;
                        $scope.employeeIdShow = false;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        $scope.msgSendTypeHide = false;
                        break;
                    case 'allGuest':
                        $scope.messageDetailsHeader = "All Guest";
                        $scope.dataShiftWiseShow = false;
                        $scope.mobileNumberShow = false;
                        $scope.employeeIdShow = false;
                        $scope.msgSendTypeHide = false;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        break;
                    case 'allEmployeeAndGuest':
                        $scope.messageDetailsHeader = "All Employees And guest";
                        $scope.dataShiftWiseShow = true;
                        $scope.mobileNumberShow = false;
                        $scope.employeeIdShow = false;
                        $scope.msgSendTypeHide = false;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        break;
                    case 'byMobileNumber':
                        $scope.messageDetailsHeader = "Mobile Number";
                        $scope.dataShiftWiseShow = false;
                        $scope.employeeIdShow = false;
                        $scope.mobileNumberShow = true;
                        $scope.msgSendTypeHide = false;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        $scope.adhocMobileNumber = true;
                        break;
                    case 'byEmployeeId':
                        $scope.messageDetailsHeader = "By Employee Id";
                        $scope.dataShiftWiseShow = false;
                        $scope.mobileNumberShow = false;
                        $scope.employeeIdShow = true;
                        $scope.msgSendTypeHide = false;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        break;
                    case 'byAdhocMobileNumber':
                        $scope.messageDetailsHeader = "By Ad-hoc Mobile Number";
                        $scope.dataShiftWiseShow = false;
                        $scope.employeeIdShow = false;
                        $scope.mobileNumberShow = true;
                        $scope.msgSendTypeHide = true;
                        $scope.message = {};
                        $scope.addMobNumArray = [];
                        $scope.addEmpIdArray = [];
                        $scope.storedMessageDisabled = true;
                        $scope.manualMessageDisabled = true;
                        $scope.adhocMobileNumber = false;
                        break;
                    default:

                }

            }

            $scope.getAllMessageSetting = function() {

            }
            $scope.endDateSet = function(startDate) {
                $scope.minDate = startDate
            }
            $scope.allocationStatus = true;
            $scope.getProjectManagement = function() {
                $scope.allocateCheckBox = false;
                $scope.addCheckBox = false;

                $scope.isAllocatedProject = false;
                $scope.editProjectForm = null;
                $scope.addProject = {};
                $scope.minDate = new Date();
                $scope.allocateProject = {};
                $scope.search = {};
                $scope.projectForm = {};
                var dataForProject = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/user/listOfProjectIdByClient', dataForProject).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.listOfProjectNameAdhoc = data;
                    }

                }).error(function(data, status, headers, config) {
                    // log error
                });
                var dataForProjectManager = {
                    branchId: branchId,
                    userId: profileId,
                    userRoles: "'manager'",
                    combinedFacility: combinedFacility
                }

                $http.post('services/employee/listofProjectMngAndSpoc/', dataForProjectManager).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.projectManagerList = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
                var dataForProjectDetails = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: "A",
                    combinedFacility: combinedFacility
                };
                $http.post('services/employee/listofProjectId/', dataForProjectDetails).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.listofProjectIdDetails = data;
                    }

                }).error(function(data, status, headers, config) {
                    // log error
                });
            }
            $scope.sendMessage = function(value, combinedFacilityId) {
                var combinedFacilityId = String(combinedFacilityId);
                var facilityBranchName = [];

                angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                    var branchName = _.where(userFacilities, {
                        branchId: item
                    })[0].name;
                    facilityBranchName.push(branchName);
                });


                if ($scope.messageDetailsHeader == "All Employees") {
                    $confirm({
                        text: "Are you sure you want to send message to All Employees of" + ' ' + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    }).then(function() {

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            combinedFacility: combinedFacilityId
                        };
                        if (value.message == "storedMessage") {
                            data.messageDescription = value.storedMessageDetails.Message;
                            data.messageType = "C";
                        } else {
                            data.messageDescription = value.messageDetail;
                            data.messageType = "M";
                        };

                        $http.post('services/message/messageToAllEmployeesCount', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.empCount = data.status;

                                $confirm({
                                        text: $scope.empCount + ". Do you want to continue?",
                                        title: 'Send Confirmation',
                                        ok: 'Yes',
                                        cancel: 'No'
                                    })
                                    .then(function() {
                                        var data = {
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            },
                                            notificationType: value.selectedMessage.value,
                                            combinedFacility: combinedFacilityId
                                        };
                                        if (value.message == "storedMessage") {
                                            data.messageDescription = value.storedMessageDetails.Message;
                                            data.messageType = "C";
                                        } else {
                                            data.messageDescription = value.messageDetail;
                                            data.messageType = "M";
                                        };


                                        $http.post('services/message/messageToAllEmployees', data).
                                        success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                if (data.status == "Message Sent") {

                                                    ngDialog.open({
                                                        template: 'Message Sent successfully',
                                                        plain: true
                                                    });
                                                } else {
                                                    ngDialog.open({
                                                        template: 'somthing went wrong',
                                                        plain: true
                                                    });
                                                }
                                            }
                                        }).
                                        error(function(data, status, headers, config) {});
                                    });
                            }
                        }).error(function(data, status, headers, config) {});

                    });
                } else if ($scope.messageDetailsHeader == "Employees Shift Wise & Date Wise") {
                    $confirm({
                        text: "Are you sure you want to send message to this " + shifDateConvertDateUTC(value.shiftTimeDate) + ' date and this ' + value.shiftTimes.shiftTime + ' shift time employees of ' + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    }).then(function() {

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            shiftDate: shifDateConvertDateUTC(value.shiftTimeDate),
                            shiftTime: value.shiftTimes.shiftTime + ':00',
                            tripType: value.tripType.value,
                            combinedFacility: combinedFacilityId
                        };
                        if (value.message == "storedMessage") {
                            data.messageDescription = value.storedMessageDetails.Message;
                            data.messageType = "C";
                        } else {
                            data.messageDescription = value.messageDetail;
                            data.messageType = "M";
                        };

                        $http.post('services/message/messagetoallemployeesbasedonshiftCount', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                                $scope.empShiftCount = data.status;
                                if (data.status == "Sending message to-0- Employees") {
                                    ngDialog.open({
                                        template: 'There is No Employee on this Selected Shift Time and Date',
                                        plain: true
                                    });
                                } else {

                                    $confirm({
                                            text: $scope.empShiftCount + ". Do you want to continue?",
                                            title: 'Send Confirmation',
                                            ok: 'Yes',
                                            cancel: 'No'
                                        })
                                        .then(function() {
                                            var data = {
                                                shiftDate: shifDateConvertDateUTC(value.shiftTimeDate),
                                                shiftTime: value.shiftTimes.shiftTime + ':00',
                                                tripType: value.tripType.value,
                                                eFmFmClientBranchPO: {
                                                    branchId: branchId
                                                },
                                                notificationType: value.selectedMessage.value,
                                                combinedFacility: combinedFacilityId
                                            };
                                            if (value.message == "storedMessage") {
                                                data.messageDescription = value.storedMessageDetails.Message;
                                                data.messageType = "C";
                                            } else {
                                                data.messageDescription = value.messageDetail;
                                                data.messageType = "M";
                                            };

                                            $http.post('services/message/messagetoallemployeesbasedonshift', data).
                                            success(function(data, status, headers, config) {
                                                if (data.status != "invalidRequest") {
                                                    if (data.status == "Message Sent") {

                                                        ngDialog.open({
                                                            template: 'Message Sent successfully',
                                                            plain: true
                                                        });
                                                    } else {
                                                        ngDialog.open({
                                                            template: 'somthing went wrong',
                                                            plain: true
                                                        });
                                                    }
                                                }

                                            }).error(function(data, status, headers, config) {
                                                // log error
                                            });
                                        });
                                };
                            }
                        }).error(function(data, status, headers, config) {
                            // log error
                        });
                    });
                } else if ($scope.messageDetailsHeader == "All Guest") {
                    $confirm({
                        text: "Are you sure you want to send message to All Guest of " + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    }).then(function() {

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            combinedFacility: combinedFacilityId
                        };
                        if (value.message == "storedMessage") {
                            data.messageDescription = value.storedMessageDetails.Message;
                            data.messageType = "C";
                        } else {
                            data.messageDescription = value.messageDetail;
                            data.messageType = "M";
                        };

                        $http.post('services/message/messageToAllGuestsCount', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.allGuestCount = data.status;

                                $confirm({
                                        text: $scope.allGuestCount + ". Do you want to continue?",
                                        title: 'Send Confirmation',
                                        ok: 'Yes',
                                        cancel: 'No'
                                    })
                                    .then(function() {
                                        var data = {
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            },
                                            notificationType: value.selectedMessage.value,
                                            combinedFacility: combinedFacilityId
                                        };
                                        if (value.message == "storedMessage") {
                                            data.messageDescription = value.storedMessageDetails.Message;
                                            data.messageType = "C";
                                        } else {
                                            data.messageDescription = value.messageDetail;
                                            data.messageType = "M";
                                        };


                                        $http.post('services/message/messageToAllGuests', data).
                                        success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                if (data.status == "Message Sent") {

                                                    ngDialog.open({
                                                        template: 'Message Sent successfully',
                                                        plain: true
                                                    });
                                                } else {
                                                    ngDialog.open({
                                                        template: 'somthing went wrong',
                                                        plain: true
                                                    });
                                                }
                                            }

                                        }).error(function(data, status, headers, config) {
                                            // log error
                                        });
                                    });
                            }
                        }).error(function(data, status, headers, config) {
                            // log error
                        });
                    });

                } else if ($scope.messageDetailsHeader == "All Employees And guest") {
                    $confirm({
                        text: "Are you sure you want to send message to All Employees And guest of " + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    }).then(function() {
                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            shiftDate: shifDateConvertDateUTC(value.shiftTimeDate),
                            shiftTime: value.shiftTimes.shiftTime + ':00',
                            tripType: value.tripType.value,
                            combinedFacility: combinedFacilityId
                        };
                        if (value.message == "storedMessage") {
                            data.messageDescription = value.storedMessageDetails.Message;
                            data.messageType = "C";
                        } else {
                            data.messageDescription = value.messageDetail;
                            data.messageType = "M";
                        };

                        $http.post('services/message/messagetoallemployeesandguestbasedonshiftCount', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.empGuestShiftCount = data.status;

                                if (data.status == "Sending message to-0- Users") {
                                    ngDialog.open({
                                        template: 'There is No Employee and Guests on this Selected Shift Time and Date',
                                        plain: true
                                    });
                                } else {
                                    $confirm({
                                            text: $scope.empGuestShiftCount + ". Do you want to continue?",
                                            title: 'Send Confirmation',
                                            ok: 'Yes',
                                            cancel: 'No'
                                        })
                                        .then(function() {
                                            var data = {
                                                shiftDate: shifDateConvertDateUTC(value.shiftTimeDate),
                                                shiftTime: value.shiftTimes.shiftTime + ':00',
                                                tripType: value.tripType.value,
                                                eFmFmClientBranchPO: {
                                                    branchId: branchId
                                                },
                                                notificationType: value.selectedMessage.value,
                                                combinedFacility: combinedFacilityId
                                            };
                                            if (value.message == "storedMessage") {
                                                data.messageDescription = value.storedMessageDetails.Message;
                                                data.messageType = "C";
                                            } else {
                                                data.messageDescription = value.messageDetail;
                                                data.messageType = "M";
                                            };

                                            $http.post('services/message/messagetoallemployeesandguestbasedonshift', data).
                                            success(function(data, status, headers, config) {
                                                if (data.status != "invalidRequest") {
                                                    if (data.status == "Message Sent") {

                                                        ngDialog.open({
                                                            template: 'Message Sent successfully',
                                                            plain: true
                                                        });
                                                    } else {
                                                        ngDialog.open({
                                                            template: 'somthing went wrong',
                                                            plain: true
                                                        });
                                                    }
                                                }

                                            }).error(function(data, status, headers, config) {
                                                // log error
                                            });
                                        });
                                };
                            }
                        }).error(function(data, status, headers, config) {
                            // log error
                        });
                    });

                } else if ($scope.messageDetailsHeader == "Mobile Number") {
                    $confirm({
                        text: "Are you sure you want to send message to this Mobile Numbers of " + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    }).then(function() {

                        var mobileNumberArray = [];
                        mobileNumberArray.push(value.mobileNumber);
                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            mobileNumbers: mobileNumberArray,
                            combinedFacility: combinedFacilityId
                        };
                        if (value.message == "storedMessage") {
                            data.messageDescription = value.storedMessageDetails.Message;
                            data.messageType = "C";
                        } else {
                            data.messageDescription = value.messageDetail;
                            data.messageType = "M";
                        };

                        $http.post('services/message/sendSMSByMobileNumbersCount', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.mobNumCount = data.status;

                                $confirm({
                                        text: $scope.mobNumCount + ". Do you want to continue?",
                                        title: 'Send Confirmation',
                                        ok: 'Yes',
                                        cancel: 'No'
                                    })
                                    .then(function() {
                                        var mobileNumberArray = [];
                                        mobileNumberArray.push(value.mobileNumber);
                                        var data = {
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            },
                                            notificationType: value.selectedMessage.value,
                                            mobileNumbers: mobileNumberArray,
                                            combinedFacility: combinedFacilityId
                                        };
                                        if (value.message == "storedMessage") {
                                            data.messageDescription = value.storedMessageDetails.Message;
                                            data.messageType = "C";
                                        } else {
                                            data.messageDescription = value.messageDetail;
                                            data.messageType = "M";
                                        };

                                        $http.post('services/message/sendSMSByMobileNumbers', data).
                                        success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                if (data.status == "Message Sent") {

                                                    ngDialog.open({
                                                        template: 'Message Sent successfully',
                                                        plain: true
                                                    });
                                                } else {
                                                    ngDialog.open({
                                                        template: 'somthing went wrong',
                                                        plain: true
                                                    });
                                                }
                                            }

                                        }).error(function(data, status, headers, config) {
                                            // log error
                                        });
                                    });
                            }
                        }).error(function(data, status, headers, config) {
                            // log error
                        });
                    });
                } else if ($scope.messageDetailsHeader == "By Employee Id") {
                    $confirm({
                        text: "Are you sure you want to send message to this Employee Ids of " + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    }).then(function() {

                        var employeeIdArray = [];
                        employeeIdArray.push(value.employeeId);
                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            employeeIds: employeeIdArray,
                            combinedFacility: combinedFacilityId
                        };
                        if (value.message == "storedMessage") {
                            data.messageDescription = value.storedMessageDetails.Message;
                            data.messageType = "C";
                        } else {
                            data.messageDescription = value.messageDetail;
                            data.messageType = "M";
                        };

                        $http.post('services/message/sendSMSByEmployeeIDCount', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.empIdCount = data.status;

                                $confirm({
                                        text: $scope.empIdCount + ". Do you want to continue?",
                                        title: 'Send Confirmation',
                                        ok: 'Yes',
                                        cancel: 'No'
                                    })
                                    .then(function() {
                                        var employeeIdArray = [];
                                        employeeIdArray.push(value.employeeId);
                                        var data = {
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            },
                                            notificationType: value.selectedMessage.value,
                                            employeeIds: employeeIdArray,
                                            combinedFacility: combinedFacilityId
                                        };
                                        if (value.message == "storedMessage") {
                                            data.messageDescription = value.storedMessageDetails.Message;
                                            data.messageType = "C";
                                        } else {
                                            data.messageDescription = value.messageDetail;
                                            data.messageType = "M";
                                        };


                                        $http.post('services/message/sendSMSByEmployeeID', data).
                                        success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {

                                                if (data.status == "Message Sent") {

                                                    ngDialog.open({
                                                        template: 'Message Sent successfully',
                                                        plain: true
                                                    });
                                                } else {
                                                    ngDialog.open({
                                                        template: 'somthing went wrong',
                                                        plain: true
                                                    });
                                                }
                                            }
                                        }).
                                        error(function(data, status, headers, config) {
                                            // log error
                                        });
                                    });
                            }
                        }).error(function(data, status, headers, config) {
                            // log error
                        });
                    });

                } else if ($scope.messageDetailsHeader == "By Ad-hoc Mobile Number") {

                    var adhocMobileNumberArray = [];
                    adhocMobileNumberArray.push(value.mobileNumber);
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        mobileNumbers: adhocMobileNumberArray,
                        combinedFacility: combinedFacilityId
                    };
                    if (value.message == "storedMessage") {
                        data.messageDescription = value.storedMessageDetails.Message;
                        data.messageType = "C";
                    } else {
                        data.messageDescription = value.messageDetail;
                        data.messageType = "M";
                    };

                    $http.post('services/message/sendSMSToAdhocNumbersCount', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {

                            $scope.adHocCount = data.status;

                            $confirm({
                                    text: $scope.adHocCount + ". Do you want to continue?",
                                    title: 'Send Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    var adhocMobileNumberArray = [];
                                    adhocMobileNumberArray.push(value.mobileNumber);
                                    var data = {
                                        eFmFmClientBranchPO: {
                                            branchId: branchId
                                        },
                                        notificationType: "SendMessage",
                                        mobileNumbers: adhocMobileNumberArray,
                                        combinedFacility: combinedFacilityId
                                    };
                                    if (value.message == "storedMessage") {
                                        data.messageDescription = value.storedMessageDetails.Message;
                                        data.messageType = "C";
                                    } else {
                                        data.messageDescription = value.messageDetail;
                                        data.messageType = "M";
                                    };


                                    $http.post('services/message/sendSMSToAdhocNumbers', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == "Message Sent") {

                                                ngDialog.open({
                                                    template: 'Message Sent successfully',
                                                    plain: true
                                                });
                                            } else {
                                                ngDialog.open({
                                                    template: 'somthing went wrong',
                                                    plain: true
                                                });
                                            }
                                        }


                                    }).
                                    error(function(data, status, headers, config) {
                                        // log error
                                    });
                                });
                        }
                    }).error(function(data, status, headers, config) {
                        // log error
                    });
                }

            }
            $scope.setProjectName = function(value) {
                $scope.allocateProject.projectName = {
                    projectId: value
                }
            }
            $scope.setProjectNameForAllocate = function(project) {

                $scope.editAllocateProjectDetail.projectName = {
                    projectName: project.projectName
                }
            }
            $scope.setProjectNameForSearch = function(value) {
                $scope.search.projectName = {
                    projectId: value
                }
            }
            $scope.projectStartDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'projectStartDatePopup': true
                    };
                }, 50);
            };
            $scope.delegateUsersTable = false;
            $scope.NodelegateUsers = false;
            $scope.getDelegateUsers = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/employee/AllEmployeeDetails/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        $scope.employeeDetails = data;
                    }
                }).
                error(function(data, status, headers, config) {

                });

                var dataObj = {
                    eFmFmClientProjectDetails: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    delegatedCall: "0",
                    reportingManagerUserId: "0",
                    combinedFacility: combinedFacilityId
                }
                $http.post('services/employee/listofDeligatedUserDetailsByUserAndAdmin/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.length > 0) {
                            $scope.delegateUsers = data;
                            $scope.delegateUsersTable = true;
                            $scope.NodelegateUsers = false;
                        } else {
                            $scope.delegateUsersTable = false;
                            $scope.NodelegateUsers = true;
                            ngDialog.open({
                                template: 'No Result Found',
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {

                });

            }
            $scope.projectAllocation = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/employee/AllEmployeeDetails/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.employeeDetails = data;
                    }
                }).
                error(function(data, status, headers, config) {

                });

            }
            $scope.projectEndDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'projectEndDatePopup': true
                    };
                }, 50);
            };
            $scope.setAllocation = function(value) {
                if (value == 'allocation') {
                    $scope.allocationStatus = true;
                } else {
                    $scope.allocationStatus = false;
                }
            };
            $scope.addNewProject = function(project, addProjectForm) {

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    startDate: convertDateUTC(project.projectStartDate),
                    endDate: convertDateUTC(project.projectEndDate),
                    clientProjectId: project.projectId,
                    employeeProjectName: project.projectName,
                    combinedFacility: combinedFacility
                }

                $http.post('services/employee/addProjectDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == "success") {
                            ngDialog.open({
                                template: 'Saved successfully',
                                plain: true
                            });
                            $scope.addProject = {};
                            addProjectForm.$setPristine();
                        } else {
                            ngDialog.open({
                                template: data.status,
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
            $scope.allocateNewProject = function(project, allocateProjectForm) {

                var data = {
                    eFmFmClientProjectDetails: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                    },
                    userId: profileId,
                    reportingManagerUserId: project.reportingManagerId,
                    combinedFacility: combinedFacility
                }
                var url;
                if ($scope.allocationStatus) {
                    url = 'services/employee/projectAllocation/';

                    $scope.empList = [];
                    angular.forEach(project.listOfEmployees, function(value, key) {
                        $scope.empList.push({
                            efmFmUserMaster: {
                                userId: value
                            }
                        })
                    });
                    data.startDate = convertDateUTC(project.projectStartDate);
                    data.endDate = convertDateUTC(project.projectEndDate);
                    data.isActive = "Y";
                    data.remarks = "NA";
                    data.projectAllocatedEmployeeId = $scope.empList;
                    data.eFmFmClientProjectDetails.projectId = project.projectId;

                } else {
                    $scope.projectList = [];
                    angular.forEach(project.listOfEmployees, function(value, key) {
                        $scope.projectList.push({
                            projectId: value
                        })
                    });
                    url = 'services/employee/deligatedUserByReportingManager/';
                    data.delegatedUserId = project.delegatedUserId.userId;
                    data.ListOfProject = [{
                        projectId: project.projectId
                    }];
                }

                $http.post(url, data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status) {
                            $scope.allocateProject = {};
                            allocateProjectForm.$setPristine();
                            ngDialog.open({
                                template: data.status,
                                plain: true
                            });
                        } else {
                            ngDialog.open({
                                template: 'Allocated Successfully',
                                plain: true
                            });


                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            }
            $scope.editAllocateProject = function(project, index) {
                var parseUKDate = function(source, delimiter) {
                    return new Date(source.split(delimiter).reverse().join(delimiter))
                };

                $scope.editProjectAllocateForm = index;

                $scope.editAllocateProjectDetail = {
                    projectName: {
                        projectName: project.projectName
                    },
                    clientProjectId: {
                        ClientprojectId: project.clientProjectId
                    },
                    projectStartDate: parseUKDate(project.projectStartDate, "-"),
                    projectEndDate: parseUKDate(project.projectEndDate, "-")
                };
            }
            $scope.cancelEditAllocateProject = function(project) {
                $scope.editProjectAllocateForm = null;
            }
            $scope.saveEditedAllocateProject = function(editedAllocateProject, allocateProjectDetail) {

                $scope.data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    projectId: allocateProjectDetail.projectId,
                    isActive: 'Y',
                    startDate: convertDateUTC(editedAllocateProject.projectStartDate),
                    endDate: convertDateUTC(editedAllocateProject.projectEndDate),
                    clientProjectId: editedAllocateProject.clientProjectId.projectId,
                    employeeProjectName: editedAllocateProject.projectName.projectName,
                    combinedFacility: combinedFacility
                }

                $http.post('services/employee/updatedAndDisableProejctDetails/', $scope.data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        if (data.status == "success") {
                            allocateProjectDetail.projectStartDate = $scope.data.startDate;
                            allocateProjectDetail.projectEndDate = $scope.data.endDate;
                            allocateProjectDetail.projectName = $scope.data.employeeProjectName;
                            ngDialog.open({
                                template: 'Saved successfully',
                                plain: true
                            });
                            $scope.editProjectAllocateForm = null;
                        } else {
                            ngDialog.open({
                                template: data.status,
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            }
            $scope.editProject = function(project, index) {
                var parseUKDate = function(source, delimiter) {
                    return new Date(source.split(delimiter).reverse().join(delimiter))
                };

                $scope.editProjectForm = index;

                $scope.editProjectDetail = {
                    projectName: project.projectName,
                    clientProjectId: project.clientProjectId,
                    projectStartDate: parseUKDate(project.projectStartDate, "-"),
                    projectEndDate: parseUKDate(project.projectEndDate, "-")
                };


            }
            $scope.cancelEditProject = function(project) {
                $scope.editProjectForm = null;
            }
            $scope.saveEditedProject = function(edited, projectDetail) {

                $scope.data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    projectId: projectDetail.projectId,
                    isActive: 'A',
                    startDate: convertDateUTC(edited.projectStartDate),
                    endDate: convertDateUTC(edited.projectEndDate),
                    clientProjectId: edited.clientProjectId,
                    employeeProjectName: edited.projectName,
                    combinedFacility: combinedFacility
                }

                $http.post('services/employee/updatedAndDisableProejctDetails/', $scope.data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        if (data.status == "success") {
                            projectDetail.projectStartDate = $scope.data.startDate;
                            projectDetail.projectEndDate = $scope.data.endDate;
                            projectDetail.projectName = $scope.data.employeeProjectName;
                            projectDetail.clientProjectId = $scope.data.clientProjectId;
                            ngDialog.open({
                                template: 'Saved successfully',
                                plain: true
                            });
                            $scope.editProjectForm = null;
                        } else {
                            ngDialog.open({
                                template: data.status,
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            }
            $scope.editDelegateUser = function(project, index) {


                $scope.editProjectForm = index;


            }
            $scope.cancelEditDelegateUser = function(project) {
                $scope.editProjectForm = null;
            }
            $scope.saveEditDelegateUser = function(edited, projectDetail) {

                $scope.data = {
                    eFmFmClientProjectDetails: {
                        projectId: projectDetail.projectId,
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    reportingManagerUserId: projectDetail.reportingManagerUserId,
                    delegatedUserId: edited.employee.userId,
                    delegatedBy: profileId,
                    combinedFacility: combinedFacility
                }

                $http.post('services/employee/modifyDeligatedUser/', $scope.data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        if (data.status == "success") {
                            ngDialog.open({
                                template: 'Saved successfully',
                                plain: true
                            });
                            $scope.editProjectForm = null;
                        } else {
                            ngDialog.open({
                                template: data.status,
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            }
            $scope.disableAllocateProject = function(projectDetail, index) {

                $confirm({
                    text: "Are you sure you want to Disable this Allocate Project?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/projectDisableRemarkModal.jsp',
                        controller: 'projectDisableRemarkCtrl',
                        size: 'sm',
                        resolve: {
                            post: function() {
                                return projectDetail;
                            }
                        }
                    });
                    modalInstance.result.then(function(result) {

                        $scope.data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            userId: profileId,
                            projectId: projectDetail.projectId,
                            isActive: 'D',
                            remarks: result.remarks,
                            startDate: projectDetail.projectStartDate,
                            endDate: projectDetail.projectEndDate,
                            clientProjectId: projectDetail.clientProjectId,
                            employeeProjectName: projectDetail.projectName,
                            combinedFacility: combinedFacility
                        }

                        $http.post('services/employee/updatedAndDisableProejctDetails/', $scope.data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $scope.allocatedProjects.splice(index, 1);
                                    ngDialog.open({
                                        template: 'Disabled successfully',
                                        plain: true
                                    });
                                    $scope.editProjectForm = null;
                                } else {
                                    ngDialog.open({
                                        template: data.status,
                                        plain: true
                                    });
                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });


                });
            }
            $scope.disableDelegateProject = function(projectDetail, index) {

                $confirm({
                    text: "Are you sure you want to Disable this Project?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {


                    $scope.data = {
                        userId: profileId,
                        empProjectId: projectDetail.empProjectId,
                        combinedFacility: combinedFacility

                    }

                    $http.post('services/employee/disableDeligatedUser/', $scope.data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {

                            if (data) {
                                $scope.delegateUsers.splice(index, 1);
                                ngDialog.open({
                                    template: 'Disabled successfully',
                                    plain: true
                                });

                            } else {
                                ngDialog.open({
                                    template: data.status,
                                    plain: true
                                });
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });



                });


            }
            $scope.disableProject = function(projectDetail, index) {

                $confirm({
                    text: "Are you sure you want to Disable this Project?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/projectDisableRemarkModal.jsp',
                        controller: 'projectDisableRemarkCtrl',
                        size: 'sm',
                        resolve: {
                            post: function() {
                                return projectDetail;
                            }
                        }
                    });
                    modalInstance.result.then(function(result) {

                        $scope.data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            userId: profileId,
                            projectId: projectDetail.projectId,
                            isActive: 'D',
                            remarks: result.remarks,
                            startDate: projectDetail.projectStartDate,
                            endDate: projectDetail.projectEndDate,
                            clientProjectId: projectDetail.clientProjectId,
                            employeeProjectName: projectDetail.projectName,
                            combinedFacility: combinedFacility
                        }

                        $http.post('services/employee/updatedAndDisableProejctDetails/', $scope.data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $scope.listofProjectIdDetails.splice(index, 1);
                                    ngDialog.open({
                                        template: 'Disabled successfully',
                                        plain: true
                                    });

                                } else {
                                    ngDialog.open({
                                        template: data.status,
                                        plain: true
                                    });
                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });


                });


            }

            /* Add Facility Wise */

            $scope.addFacilityWise = function() {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addFacilityWise.jsp',
                    controller: 'addFacilityWiseCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    size: 'lg',
                    resolve: {}
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {});
            }

            /* Add Employee Wise */

            $scope.addEmployeeWise = function(facilityId) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addEmployeeWise.jsp',
                    controller: 'addEmployeeWiseCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    size: 'lg',
                    resolve: {
                        facilityId: function() {
                            return facilityId;
                        },
                    }
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {});
            }
            $scope.getCombinedFacility = function() {
                var data = {
                    branchId: branchId,
                    userId: profileId,
                };
                $http.post('services/facility/facilityDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.facilityDetailsData = data;
                        var result = _.chain(data)
                            .groupBy("branchId")
                            .pairs()
                            .map(function(currentItem) {
                                return _.object(_.zip(["branchId", "subFacility"], currentItem));
                            })
                            .value();

                        var subFacility = [];

                        function RemoveFalseAndTransformToArray() {
                            angular.forEach(result, function(value, key) {
                                for (var key in value.subFacility) {
                                    if (value.subFacility[key].ticked === false) {
                                        delete value.subFacility[key];
                                    }
                                }
                            });
                        }
                        RemoveFalseAndTransformToArray();

                        angular.forEach(result, function(value, key) {
                            for (var key in value.subFacility) {
                                var removeFalse = _.pluck(value.subFacility, 'branchName');

                                angular.forEach(removeFalse, function(data, key) {

                                    if (value.baseFacilityName == data || value.baseFacilityName == undefined) {
                                        removeFalse.splice(key, 1);
                                    }
                                });

                                if (removeFalse == '') {
                                    value.subFacilityDetails = 'N/A'
                                } else {
                                    value.subFacilityDetails = String(removeFalse);
                                }

                                value.subFacility[key].name = value.subFacility[key].branchName;
                                if (value.subFacility[key].branchType == 'base') {
                                    value.baseFacilityName = value.subFacility[key].branchName;
                                    value.baseFacilityId = value.subFacility[key].facilityToFacilityId;
                                }
                            }

                        });

                        $scope.compineFacilityDetails = result;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.getCombinedFacility();

            $scope.editCombinedFacilityDetails = function(facility, index) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/editCombinedFacilityDetails.jsp',
                    controller: 'editCombinedFacilityDetailsCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    size: 'lg',
                    resolve: {
                        facility: function() {
                            return facility;
                        },
                        index: function() {
                            return index;
                        },
                        facilityRecords: function() {
                            return $scope.compineFacilityDetails;
                        },
                        facilityDetailsData: function() {
                            return $scope.facilityDetailsData;
                        },


                    }

                });


                modalInstance.result.then(function(result) {

                    $scope.getCombinedFacility();

                });
            }

            $scope.deleteFacilityDetails = function(facility, index) {
                $confirm({
                    text: "Are you sure you want to delete this facility?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        facilityToFacilityId: facility.baseFacilityId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/facility/disableFacility/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.compineFacilityDetails.splice(index, 1);
                            ngDialog.open({
                                template: 'Facility has been deleted successfully',
                                plain: true
                            });
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                });

            }

            $scope.addVehicleType = function(facilityData) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addVehicleType.jsp',
                    controller: 'addVehicleTypecrtl',
                    backdrop: 'static',
                    resolve: {
                        vehicleTypeDetails: function() {
                            return $rootScope.vehicleTypeDetails;
                        },
                        facilityData: function() {
                            return facilityData;
                        }
                    }

                });
                modalInstance.result.then(function(result) {

                });
            }

            $scope.addNewProjectReset = function(addProjectForm) {
                $scope.addProject = {};
                addProjectForm.$setPristine();
            }
            $scope.allocateNewProjectReset = function(allocateProjectForm) {
                $scope.allocateProject = {};
                allocateProjectForm.$setPristine();
            }
            $scope.projectSelectAll = function(value) {

            }

            $scope.disableAllAllocate = function(allocatedProjects) {
                if (allocatedProjects.length > 0) {
                    var filtered = [];
                    angular.forEach(allocatedProjects, function(value, key) {
                        if (value.selected == true) {
                            filtered.push({
                                efmFmUserMaster: {
                                    userId: value.projectId
                                }
                            })
                        } else {

                        }
                    });
                    if (filtered.length > 0) {
                        $confirm({
                            text: "Are you sure you want to Disable All the Allocated Project?",
                            title: 'Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        }).then(function() {
                            var modalInstance = $modal.open({
                                templateUrl: 'partials/modals/projectDisableRemarkModal.jsp',
                                controller: 'projectDisableRemarkCtrl',
                                size: 'sm'
                            });
                            modalInstance.result.then(function(result) {
                                $scope.sendProjectData.isActive = 'D';
                                $scope.sendProjectData.remarks = result.remarks;
                                $scope.sendProjectData.projectAllocatedEmployeeId = filtered;
                                $scope.sendProjectData.combinedFacility = combinedFacility;


                                $http.post('services/employee/disableAllProjectAllocation/', $scope.sendProjectData).
                                success(function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {

                                        ngDialog.open({
                                            template: "Selected projects are Disabled",
                                            plain: true
                                        });
                                    }
                                }).
                                error(function(data, status, headers, config) {
                                    // log error
                                });
                            });


                        });



                    } else {
                        ngDialog.open({
                            template: "Kindly Select At Least One Project",
                            plain: true
                        });
                    }
                }
            }
            $scope.allocateProjectSelectAll = function(projects, value) {

                if (projects.length > 0) {

                    if (value) {
                        angular.forEach(projects, function(value, key) {

                            value.selected = true;

                        });
                    } else {
                        angular.forEach(projects, function(value, key) {

                            value.selected = false;

                        });
                    }
                }


            }
            $scope.allocatedProjectSelectCheck = function(value, index) {

            }
            $scope.projectSelectIndividual = function(value) {

            }
            $scope.searchProjectDetails = function(search) {

                $scope.sendProjectData = {
                    eFmFmClientProjectDetails: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        projectId: search.projectId
                    },
                    userId: profileId,
                    reportingManagerUserId: search.reportingManagerId,
                    combinedFacility: combinedFacility
                }

                $http.post('services/employee/listofProjectAllocationDetails/', $scope.sendProjectData).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.length > 0) {
                            $scope.allocatedProjects = data;
                            $scope.isAllocatedProject = true;
                        } else {
                            $scope.isAllocatedProject = false;
                            ngDialog.open({
                                template: "No Results Found",
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            }
            $scope.message = {};
            $scope.addMobNumArray = [];
            $scope.addEmpIdArray = [];
            $scope.addEmpIdShow = true;
            $scope.resetMessage = function(message) {
                $scope.message = message;
                $scope.message.message = '';
                $scope.message.selectedMessage = '';
                $scope.message.storedMessageDetails = '';
                $scope.message.shiftTimeDate = '';
                $scope.message.tripType = '';
                $scope.message.shiftTimes = '';
                $scope.message.toMessage = '';
                $scope.message.manualMessage = '';
                $scope.message.messageDetail = '';
                $scope.message.mobileNumber = '';
                $scope.message.employeeId = '';
                $scope.manualMessageDisabled = true;
            }

            $scope.resetTxtAreaMsg = function(msg) {
                $scope.message = msg;
                $scope.message.messageDetail = '';
            }


            $scope.setManualMessage = function(value) {

                if (value == 'manualMessage') {
                    $scope.storedMessageDisabled = true;
                    $scope.manualMessageDisabled = false;
                    $scope.message.storedMessageDetails = null;
                } else {
                    $scope.storedMessageDisabled = false;
                    $scope.manualMessageDisabled = true;
                    $scope.message.messageDetail = null;
                }

            }

            $scope.editManualMessage = function() {
                $scope.manualMessageDisabled = false;
            }

            $scope.addMobNum = function(size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/sendMessageAddMobileNum.jsp',
                    controller: 'addMobNumCtrl',
                    size: size,
                    backdrop: 'static'
                });
                modalInstance.result.then(function(result) {

                    if ($scope.addMobNumArray.indexOf(result) == -1) {
                        $scope.addMobNumArray.push(result);
                    } else {
                        ngDialog.open({
                            template: 'This Mobile Number Already Added',
                            plain: true
                        });
                    }

                    $scope.message.mobileNumber = $scope.addMobNumArray.toString();

                });

            };

            $scope.addEmpId = function(size) {
                $scope.addEmpIdShow = false;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/sendMessageAddEmpId.jsp',
                    controller: 'addEmpIdCtrl',
                    size: size,
                    backdrop: 'static'
                });
                modalInstance.result.then(function(result) {

                    if ($scope.addEmpIdArray.indexOf(result) == -1) {
                        $scope.addEmpIdArray.push(result);
                    } else {
                        ngDialog.open({
                            template: 'This EmployeeID Already Added',
                            plain: true
                        });
                    }

                    $scope.message.employeeId = $scope.addEmpIdArray.toString();

                });

            };

            $scope.fetchingAppCount = false;
            $scope.getAppDownloadCount = function() {
                if ($scope.fetchingAppCount) return;
                $scope.getYetToAppCountData = [];
                $scope.geoCodedEmployeeCountData = [];
                $scope.getYetToGeoCodedEmployee = [];
                $scope.appdownloadedButNoGeoCoded = [];
                $scope.appDownloadedAndGeocodedEmployees = [];
                $scope.noAppDownloadedAndGeocodedEmployees = [];
                $scope.getYetToAppCountDataLength = 0;
                $scope.geoCodedEmployeeCountDataLength = 0;
                $scope.getYetToGeoCodedEmployeeLength = 0;
                $scope.appdownloadedButNoGeoCodedLenght = 0;
                $scope.appDownloadedAndGeocodedEmployeesLength = 0;
                $scope.noAppDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.getAppDownloadCountDataLength == undefined || $scope.getAppDownloadCountDataLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.getAppDownloadCountDataLength;
                    var endPgNoCount = webPageCount;
                }

                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId,
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingAppCount) {
                    $scope.fetchingAppCount = true;
                    $http.post('services/app/appDownload/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.fetchingAppCount = false;
                            if (data.length == 0) return;

                            angular.forEach(data, function(item) {
                                $scope.getAppDownloadCountData.push(item);
                            });
                            $rootScope.getAppDownloadCountData = $scope.getAppDownloadCountData;
                            $scope.getAppDownloadCountDataLength = $scope.getAppDownloadCountData.length;
                        } else {
                            setTimeout(function() {
                                $scope.fetchingAppCount = false;
                            }, 300);
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else {
                    return;
                }
            }

            $scope.fetchingNonApp = false;
            $scope.getYetToAppDownloadCount = function() {
                if ($scope.fetchingNonApp) return;
                $scope.getAppDownloadCountData = [];
                $scope.geoCodedEmployeeCountData = [];
                $scope.getYetToGeoCodedEmployee = [];
                $scope.appdownloadedButNoGeoCoded = [];
                $scope.appDownloadedAndGeocodedEmployees = [];
                $scope.noAppDownloadedAndGeocodedEmployees = [];
                $scope.getAppDownloadCountDataLength = 0;
                $scope.geoCodedEmployeeCountDataLength = 0;
                $scope.getYetToGeoCodedEmployeeLength = 0;
                $scope.appdownloadedButNoGeoCodedLenght = 0;
                $scope.appDownloadedAndGeocodedEmployeesLength = 0;
                $scope.noAppDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.getYetToAppCountDataLength == undefined || $scope.getYetToAppCountDataLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.getYetToAppCountDataLength;
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
                if (!$scope.fetchingNonApp) {
                    $scope.fetchingNonApp = true;
                    $http.post('services/app/appNonDownload/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingNonApp = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.getYetToAppCountData.push(item);
                                });

                                $rootScope.getYetToAppCountData = $scope.getYetToAppCountData;
                                $scope.getYetToAppCountDataLength = $scope.getYetToAppCountData.length;
                                $scope.getYetToAppCountDataShow = true;
                            } else {
                                setTimeout(function() {
                                    $scope.fetchingNonApp = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {

                        });
                } else {
                    return;
                }
            }
            $scope.fetchingGeoCoded = false;
            $scope.getGeoCodedEmployee = function() {
                if ($scope.fetchingGeoCoded) return;
                $scope.getAppDownloadCountData = [];
                $scope.getYetToAppCountData = [];
                $scope.getYetToGeoCodedEmployee = [];
                $scope.appdownloadedButNoGeoCoded = [];
                $scope.appDownloadedAndGeocodedEmployees = [];
                $scope.noAppDownloadedAndGeocodedEmployees = [];
                $scope.getAppDownloadCountDataLength = 0;
                $scope.getYetToAppCountDataLength = 0;
                $scope.getYetToGeoCodedEmployeeLength = 0;
                $scope.appdownloadedButNoGeoCodedLenght = 0;
                $scope.appDownloadedAndGeocodedEmployeesLength = 0;
                $scope.noAppDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.geoCodedEmployeeCountDataLength == undefined || $scope.geoCodedEmployeeCountDataLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.geoCodedEmployeeCountDataLength;
                    var endPgNoCount = webPageCount;
                }

                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.geoCodedEmployeeCountDataShow = false;
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId,
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingGeoCoded) {
                    $scope.fetchingGeoCoded = true;
                    $http.post('services/app/getAllGeocodedEmployee/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingGeoCoded = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.geoCodedEmployeeCountData.push(item);
                                });

                                $rootScope.geoCodedEmployeeCountData = $scope.geoCodedEmployeeCountData;
                                $scope.geoCodedEmployeeCountDataLength = $scope.geoCodedEmployeeCountData.length;

                                $scope.geoCodedEmployeeCountDataShow = true;
                            } else {
                                setTimeout(function() {
                                    $scope.fetchingGeoCoded = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {

                        });
                } else {
                    return;
                }
            }
            $scope.fetchingNonGeoCoded = false;
            $scope.getYetToGeoCodedEmployee = [];
            $scope.getDataYetToGeoCodedEmployee = function() {
                if ($scope.fetchingNonGeoCoded) return;
                $scope.getAppDownloadCountData = [];
                $scope.getYetToAppCountData = [];
                $scope.geoCodedEmployeeCountData = [];
                $scope.appdownloadedButNoGeoCoded = [];
                $scope.appDownloadedAndGeocodedEmployees = [];
                $scope.noAppDownloadedAndGeocodedEmployees = [];
                $scope.getAppDownloadCountDataLength = 0;
                $scope.getYetToAppCountDataLength = 0;
                $scope.geoCodedEmployeeCountDataLength = 0;
                $scope.appdownloadedButNoGeoCodedLenght = 0;
                $scope.appDownloadedAndGeocodedEmployeesLength = 0;
                $scope.noAppDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.getYetToGeoCodedEmployeeLength == undefined || $scope.getYetToGeoCodedEmployeeLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.getYetToGeoCodedEmployeeLength;
                    var endPgNoCount = webPageCount;
                }
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.getYetToGeoCodedEmployeeShow = false;
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId,
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingNonGeoCoded) {
                    $scope.fetchingNonGeoCoded = true;
                    $http.post('services/app/getAllNonGeocodedEmployee/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingNonGeoCoded = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.getYetToGeoCodedEmployee.push(item);
                                });

                                $rootScope.getYetToGeoCodedEmployee = $scope.getYetToGeoCodedEmployee;
                                $scope.getYetToGeoCodedEmployeeLength = $scope.getYetToGeoCodedEmployee.length;
                                $scope.getYetToGeoCodedEmployeeShow = true;
                            } else {
                                setTimeout(function() {
                                    $scope.fetchingNonGeoCoded = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {

                        });
                } else {
                    return;
                }
            };
            $scope.fetchingNonGeoCodedDown = false;
            $scope.getAppdownloadedButNoGeoCoded = function() {
                if ($scope.fetchingNonGeoCodedDown) return;
                $scope.getAppDownloadCountData = [];
                $scope.getYetToAppCountData = [];
                $scope.geoCodedEmployeeCountData = [];
                $scope.getYetToGeoCodedEmployee = [];
                $scope.appDownloadedAndGeocodedEmployees = [];
                $scope.noAppDownloadedAndGeocodedEmployees = [];
                $scope.getAppDownloadCountDataLength = 0;
                $scope.getYetToAppCountDataLength = 0;
                $scope.geoCodedEmployeeCountDataLength = 0;
                $scope.getYetToGeoCodedEmployeeLength = 0;
                $scope.appDownloadedAndGeocodedEmployeesLength = 0;
                $scope.noAppDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.appdownloadedButNoGeoCodedLenght == undefined || $scope.appdownloadedButNoGeoCodedLenght == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.appdownloadedButNoGeoCodedLenght;
                    var endPgNoCount = webPageCount;
                }
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.appdownloadedButNoGeoCodedShow = false;
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId,
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingNonGeoCodedDown) {
                    $scope.fetchingNonGeoCodedDown = true;
                    $http.post('services/app/appDownloadNoGeo/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingNonGeoCodedDown = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.appdownloadedButNoGeoCoded.push(item);
                                });

                                $rootScope.appdownloadedButNoGeoCoded = $scope.appdownloadedButNoGeoCoded;
                                $scope.appdownloadedButNoGeoCodedLenght = $scope.appdownloadedButNoGeoCoded.length;

                                $scope.appdownloadedButNoGeoCodedShow = true;
                            } else {
                                setTimeout(function() {
                                    $scope.fetchingNonGeoCodedDown = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {});
                } else {
                    return;
                }
            }
            $scope.fetchingGeoCodedDown = false;
            $scope.getAppDownloadedAndGeocodedEmployees = function() {
                if ($scope.fetchingGeoCodedDown) return;
                $scope.getAppDownloadCountData = [];
                $scope.getYetToAppCountData = [];
                $scope.geoCodedEmployeeCountData = [];
                $scope.getYetToGeoCodedEmployee = [];
                $scope.appdownloadedButNoGeoCoded = [];
                $scope.noAppDownloadedAndGeocodedEmployees = [];
                $scope.getAppDownloadCountDataLength = 0;
                $scope.getYetToAppCountDataLength = 0;
                $scope.geoCodedEmployeeCountDataLength = 0;
                $scope.getYetToGeoCodedEmployeeLength = 0;
                $scope.appdownloadedButNoGeoCodedLenght = 0;
                $scope.noAppDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.appDownloadedAndGeocodedEmployeesLength == undefined || $scope.appDownloadedAndGeocodedEmployeesLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.appDownloadedAndGeocodedEmployeesLength;
                    var endPgNoCount = webPageCount;
                }
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                $scope.appDownloadedAndGeocodedEmployeesShow = false;
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId,
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingGeoCodedDown) {
                    $scope.fetchingGeoCodedDown = true;
                    $http.post('services/app/appDownloadGeoCode/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingGeoCodedDown = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.appDownloadedAndGeocodedEmployees.push(item);
                                });

                                $rootScope.appDownloadedAndGeocodedEmployees = $scope.appDownloadedAndGeocodedEmployees;
                                $scope.appDownloadedAndGeocodedEmployeesLength = $scope.appDownloadedAndGeocodedEmployees.length;

                                $scope.appDownloadedAndGeocodedEmployeesShow = true;
                            } else {
                                setTimeout(function() {
                                    $scope.fetchingGeoCodedDown = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {});
                } else {
                    return;
                }
            }
            $scope.fetchingGeoCodedNoAppDown = false;
            $scope.getNoAppDownloadedAndGeocodedEmployees = function() {
                if ($scope.fetchingGeoCodedNoAppDown) return;
                $scope.getAppDownloadCountData = [];
                $scope.getYetToAppCountData = [];
                $scope.geoCodedEmployeeCountData = [];
                $scope.getYetToGeoCodedEmployee = [];
                $scope.appdownloadedButNoGeoCoded = [];
                $scope.appDownloadedAndGeocodedEmployees = [];
                $scope.getAppDownloadCountDataLength = 0;
                $scope.getYetToAppCountDataLength = 0;
                $scope.geoCodedEmployeeCountDataLength = 0;
                $scope.getYetToGeoCodedEmployeeLength = 0;
                $scope.appdownloadedButNoGeoCodedLenght = 0;
                $scope.appDownloadedAndGeocodedEmployeesLength = 0;
                if ($scope.noAppDownloadedAndGeocodedEmployeesLength == undefined || $scope.noAppDownloadedAndGeocodedEmployeesLength == 0) {
                    var startPgNoCount = 0;
                    var endPgNoCount = webPageCount;
                } else {
                    var startPgNoCount = $scope.noAppDownloadedAndGeocodedEmployeesLength;
                    var endPgNoCount = webPageCount;
                }
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId,
                    startPgNo: startPgNoCount,
                    endPgNo: endPgNoCount
                };
                if (!$scope.fetchingGeoCodedNoAppDown) {
                    $scope.fetchingGeoCodedNoAppDown = true;
                    $http.post('services/app/appNonDownloadAndGeoCoded/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.fetchingGeoCodedNoAppDown = false;
                                if (data.length == 0) return;
                                angular.forEach(data, function(item) {
                                    $scope.noAppDownloadedAndGeocodedEmployees.push(item);
                                });

                                $rootScope.noAppDownloadedAndGeocodedEmployees = $scope.noAppDownloadedAndGeocodedEmployees;
                                $scope.noAppDownloadedAndGeocodedEmployeesLength = $scope.noAppDownloadedAndGeocodedEmployees.length;

                            } else {
                                setTimeout(function() {
                                    $scope.fetchingGeoCodedNoAppDown = false;
                                }, 300);
                            }
                        }).error(function(data, status, headers, config) {});
                } else {
                    return;
                }
            }
            $scope.createCustomMessages = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/createCustomMessages.jsp',
                    controller: 'createCustomMessagesCtrl',
                });

                modalInstance.result.then(function(result) {
                    $scope.customMessagesOnLoad.push({
                        Message: result.custMsgDescription
                    });

                });
            }

            $scope.getAllNodalNames = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId,
                        userId: profileId
                    },
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/trip/allNodalzone/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.nodelNameData = data.zones;
                        }

                    }).error(function(data, status, headers, config) {
                    // log error
                });
            };



            $scope.openPasswordDiv = function() {
                $('.passwordDiv').show('slow');
                $scope.changePasswordClicked = true;
            };

            $scope.savePassword = function(userPassword) {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    newPassword: userPassword.newPass1,
                    password: userPassword.oldPass,
                    combinedFacility: combinedFacility
                };
                $http.post('services/user/changeuserpassword/', data)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == "wrong") {
                                ngDialog
                                    .open({
                                        template: 'Your old password does not match',
                                        plain: true
                                    });
                            } else if (data.status == "wrongPattern") {
                                ngDialog
                                    .open({
                                        template: 'Password must contain at least one lowercase letter,upper case letter,a special character and at least one digit',
                                        plain: true
                                    });
                            } else if (data.status == "oldpass") {
                                ngDialog
                                    .open({
                                        template: 'Sorry you can not reuse your last ' +
                                            data.numberOfPasswords +
                                            ' passwords as new password.Your last password change date and time is ' +
                                            data.lastChangeDateTime,
                                        plain: true
                                    });
                            } else {
                                ngDialog
                                    .open({
                                        template: 'Password changed successfully',
                                        plain: true
                                    });
                                document.getElementById('logOut').click();

                                $('.passwordDiv').hide('slow');
                                $scope.changePasswordClicked = false;

                            }
                        }
                    }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };

            $scope.cancelPassword = function() {
                $('.passwordDiv').hide('slow');
                $scope.changePasswordClicked = false;
            };

            $scope.editMyProfile = function() {
                $scope.isProfileEdit = true;
            };

            $scope.setUserType = function(user) {
                if (!user) {
                    $scope.user_Type = "none";
                } else
                    $scope.user_Type = user.text;
            };

            $scope.saveProfile = function(user) {
                $timeout(function() {
                    $('.hintModal').hide('slow');
                }, 50);

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    userName: user.userName,
                    firstName: user.firstName,
                    lastName: user.lastName,
                    mobileNumber: user.mobileNumber,
                    emailId: user.email,
                    birthDate: user.dob,
                    employeeDomain: user.interest,
                    employeeDesignation: user.occupation,
                    employeeDepartment: user.interest,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };

                $http.post('services/user/updateprofile/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.invalidInput) {
                                $scope.isProfileEdit = true;

                                ngDialog.open({
                                    template: data.invalidInput,
                                    plain: true
                                });
                            } else {
                                $scope.isProfileEdit = false;
                                ngDialog.open({
                                    template: 'Updated Successfully',
                                    plain: true
                                });
                            }
                        }

                    }).error(function(data, status, headers, config) {
                    // log error
                });
            };

            /* Start fuction
             * Get all the users of the particular client
             */
            $scope.getAdminSettings = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                $scope.getAdminResult = false;
                $scope.progressbar.start();
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId,
                        userId: profileId,
                    },
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/user/allusers/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.administratorData = data.users;
                            if ($scope.administratorData.length > 0) {
                                $scope.getAdminResult = true;
                                if (userRole == 'superadmin') {
                                    angular.forEach($scope.administratorData, function(value, key) {
                                        if (value.userRole == 'superadmin') {
                                            value.accessEnabled = false;
                                        } else {
                                            value.accessEnabled = true;
                                        }
                                    });
                                } else if (userRole == 'admin') {
                                    angular.forEach($scope.administratorData, function(value, key) {
                                        if (value.userRole == 'superadmin') {
                                            value.accessEnabled = false;
                                        } else if (value.userRole == 'admin') {
                                            value.accessEnabled = false;
                                        } else {
                                            value.accessEnabled = true;
                                        }
                                    });
                                } else {
                                    angular.forEach($scope.administratorData, function(value, key) {
                                        value.accessEnabled = false;
                                    });
                                }

                            } else {
                                $scope.getAdminResult = false;
                            }
                            $scope.progressbar.complete();
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });



            };
            /* end fuction
             *
             */

            $scope.cancelProfile = function(user) {
                $scope.isProfileEdit = false;
                $scope.user = {
                    userName: $scope.myProfileData[0].userName,
                    firstName: $scope.myProfileData[0].firstName,
                    lastName: $scope.myProfileData[0].lastName,
                    country: $scope.myProfileData[0].country,
                    dob: $scope.myProfileData[0].birthDate,
                    occupation: $scope.myProfileData[0].designation,
                    email: $scope.myProfileData[0].emailId,
                    interest: $scope.myProfileData[0].interest,
                    website: $scope.myProfileData[0].emailId,
                    mobileNumber: $scope.myProfileData[0].mobileNumber,
                    about: $scope.myProfileData[0].address
                };
            };

            $scope.getApplicationSettingShift = function() {
                $scope.addNewShiftIsClicked = false;
                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId,
                            userId: profileId
                        }
                    },
                    combinedFacility: combinedFacility
                };
                $http.post('services/trip/shiftime/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            var count = 0;
                            $scope.appSettingShift = data.shift;
                            if ($scope.appSettingShift.length > 0) {
                                angular.forEach($scope.appSettingShift,
                                    function(item) {
                                        $scope.appSettingShiftData.push({
                                            'id': count,
                                            label: item.shiftTime
                                        });
                                        count = count + 1;
                                    });
                                $scope.gotResultAppShift = true;
                            } else {
                                $scope.gotResultAppShift = false;
                            }
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });
            };


            $scope.setapprovalNeeded = function(approvalNeeded) {
                $scope.approvalNeeded = approvalNeeded;
            };

            $scope.setLocation = function(baseLocation) {
                $scope.baseLocation = baseLocation;
                if ($scope.baseLocation == 'Office') {
                    $scope.isChangeLocation = false;
                } else
                    $scope.isChangeLocation = true;
            };

            $scope.setETA = function(etaSMS) {
                $scope.etaSMS = etaSMS;
            };

            $scope.setGeoFenceArea = function(geoFenceArea) {
                $scope.geoFenceArea = geoFenceArea;
            };

            $scope.setAfter13HrSMSContact = function(after13HrSMSContact) {
                $scope.after13HrSMSContact = after13HrSMSContact;
            };

            $scope.setAfter14HrSMSContact = function(after14HrSMSContact) {
                $scope.after14HrSMSContact = after14HrSMSContact;
            };

            $scope.setMaxSpeedArray = function(maxSpeedArray) {
                $scope.maxSpeedArray = maxSpeedArray;
            };

            $scope.setDriverCheckedout = function(driverCheckedout) {
                $scope.driverCheckedout = driverCheckedout;
            };

            $scope.setDelayMessage = function(delayMessage) {
                $scope.delayMessage = delayMessage;
            };

            $scope.setWaitingTime = function(employeeWaitingTime) {
                $scope.employeeWaitingTime = employeeWaitingTime;
            };

            $scope.setEmrgContactNumber = function(emrgContactNumber) {
                $scope.emrgContactNumber = emrgContactNumber;
            };

            $scope.setMaxTravelTime = function(maxTravelTime) {
                $scope.maxTravelTime = maxTravelTime;
            };

            $scope.setMaxRadialDistance = function(maxRadialDistance) {
                $scope.maxRadialDistance = maxRadialDistance;
            };

            $scope.setMaxRouteLength = function(maxRouteLength) {
                $scope.maxRouteLength = maxRouteLength;
            };

            $scope.setEscortConstraintsh = function(escortConstraints) {
                $scope.escortConstraints = escortConstraints;
            };

            $scope.setMaxRouteDevietion = function(maxRoutedeviation) {
                $scope.maxRoutedeviation = maxRoutedeviation;
            };

            $scope.setTransportContactNumber = function(transportContactNumber) {
                $scope.transportContactNumber = transportContactNumber;
            };

            $scope.setClustring = function(autoClustring) {
                $scope.autoClustring = autoClustring;
            };

            $scope.setClusterSize = function(clusterSize) {
                $scope.clusterSize = clusterSize;
            };

            $scope.setAutoTripStartGF = function(autoTripStartGF) {
                $scope.autoTripStartGF = autoTripStartGF;
            };

            $scope.setEmployeeFeedbackEmail = function(employeeFeedbackEmail) {
                $scope.employeeFeedbackEmail = employeeFeedbackEmail;
                if (employeeFeedbackEmail == 'Yes') {
                    $scope.employeeFeedbackEmailIdSection = true;
                } else {
                    $scope.employeeFeedbackEmailIdSection = false;
                    $scope.employeeFeedbackEmailId = "No";
                    $scope.toEmployeeFeedBackEmail = "No";
                }

            };
            $scope.setReportBugEmailIds = function(reportBugEmailIds) {
                $scope.reportBugEmailIds = reportBugEmailIds;
            };
            $scope.setToEmployeeFeedbackEmailIds = function(toEmployeeFeedBackEmail) {
                $scope.toEmployeeFeedBackEmail = toEmployeeFeedBackEmail;
            };

            $scope.setEmployeeAppReportBug = function(employeeAppReportBug) {
                $scope.employeeAppReportBug = employeeAppReportBug;
                if (employeeAppReportBug == 'Yes') {
                    $scope.reportBugEmailIdSection = true;
                } else {
                    $scope.reportBugEmailIdSection = false;
                    $scope.reportBugEmailIds = "No";
                }

            };
            $scope.setemployeeCheckOutGeofenceRegion = function(employeeCheckOutGeofenceRegion) {
                $scope.employeeCheckOutGeofenceRegion = employeeCheckOutGeofenceRegion;
            };
            $scope.setemployeeFeedbackEmailIds = function(employeeFeedbackEmailId) {

                $scope.employeeFeedbackEmailId = employeeFeedbackEmailId;
            };
            $scope.setAutoTripEndGF = function(autoTripEndGF) {
                $scope.autoTripEndGF = autoTripEndGF;
            };

            $scope.setTransportFeedbackEmailId = function(
                transportFeedbackEmailId) {
                $scope.transportFeedbackEmailId = transportFeedbackEmailId;
            };

            $scope.setDriverImage = function(driverImage) {
                $scope.driverImage = driverImage;
            };

            $scope.setDriverMobileNuumber = function(driverMobileNuumber) {
                $scope.driverMobileNuumber = driverMobileNuumber;
            };

            $scope.setDriverName = function(driverName) {
                $scope.driverName = driverName;
            };

            $scope.setProfilePic = function(profilePic) {
                $scope.profilePic = profilePic;
            };

            $scope.setAutoCallSmsDisable = function(autoCallSmsDisable) {
                $scope.autoCallSmsDisable = autoCallSmsDisable;
            };

            $scope.setAdminRequired = function(adminRequired) {
                $scope.adminRequired = adminRequired;
            };

            $scope.setAdminInactiveAccount = function(adminInactiveAccount) {
                $scope.adminInactiveAccount = adminInactiveAccount;
            };

            $scope.setResetPaswwordForAdmin = function(resetPaswwordForAdmin) {
                $scope.resetPaswwordForAdmin = resetPaswwordForAdmin;
            };

            $scope.setResetPaswwordForUser = function(resetPaswwordForUser) {
                $scope.resetPaswwordForUser = resetPaswwordForUser;
            };

            $scope.settwoWayFactor = function(twoWayFactor) {
                $scope.twoWayFactor = twoWayFactor;
            };

            $scope.setInvoiceType = function(requestType) {

                if (angular.isDate(requestType)) {
                    $scope.requestTypeInvoice = convertDateUTC(requestType);
                } else {
                    $scope.requestTypeInvoice = requestType;
                }

            }

            $scope.setNumberOfAttemptsWrongPass = function(
                numberOfAttemptsWrongPass) {
                $scope.numberOfAttemptsWrongPass = numberOfAttemptsWrongPass;
            };

            $scope.setSessiontimeOut = function(sessiontimeOut) {
                $scope.sessiontimeOut = sessiontimeOut;
            };

            $scope.setEmployeeCheckInVia = function(employeeCheckInVia) {
                $scope.employeeCheckInVia = employeeCheckInVia;
            }

            $scope.setSelectVehicleCapacitywise = function(selectVehicleCapacitywise) {
                $scope.selectVehicleCapacitywise = selectVehicleCapacitywise;
            }

            $scope.setVehicleType = function(vehicleType) {
                $scope.vehicleType = vehicleType;
            }

            $scope.setSessionNotificationTime = function(sessionTimeOutNotificationInSec) {
                $scope.sessionTimeOutNotificationInSec = sessionTimeOutNotificationInSec;
            };


            $scope.setPickDropNeeded = function(pickDropNeeded) {
                $scope.pickDropNeeded = pickDropNeeded;
            };

            $scope.setInvoiceNumberRange = function(invoiceNumberRange) {
                $scope.invoiceNumberRange = invoiceNumberRange;
            };

            $scope.setAdhocTimePicker = function(adhocTimePicker) {
                $scope.adhocTimePicker = adhocTimePicker;
            };

            $scope.setDropPriorTime = function(dropPriorTime) {
                $scope.dropPriorTime = dropPriorTime;
            };

            $scope.setPickupPriorTime = function(pickupPriorTime) {
                $scope.pickupPriorTime = pickupPriorTime;

            };

            $scope.setdropCancelCutOffTime = function(dropCancelCutOffTime) {
                $scope.dropCancelCutOffTime = dropCancelCutOffTime;

            };

            $scope.setpickupCancelCutOffTime = function(pickupCancelCutOffTime) {
                $scope.pickupCancelCutOffTime = pickupCancelCutOffTime;

            };

            $scope.setShiftTimeDifference = function(shiftTimeDifference) {
                $scope.shiftTimeDifference = shiftTimeDifference;

            };

            $scope.setPanicAlertNeeded = function(panicAlertNeeded) {
                $scope.panicAlertNeeded = panicAlertNeeded;
            };
            $scope.setinvoiceGenDate = function(invoiceGenDate) {
                $scope.invoiceGenDate = invoiceGenDate;
            };
            $scope.setdistance = function(setdistance) {
                $scope.distanceFlg = setdistance;
            };
            $scope.setSmallDropDown = function(smallDropDown) {
                $scope.smallDropDown = smallDropDown;
            };
            $scope.setAddDistanceRecoverGeoFence = function(
                addDistanceRecoverGeoFence) {
                $scope.addDistanceRecoverGeoFence = addDistanceRecoverGeoFence;
            };
            $scope.setAutoDropRoster = function(autoDropRoster) {
                $scope.autoDropRoster = autoDropRoster;
            };
            $scope.setLastPasswordNotCurrent = function(lastPasswordNotCurrent) {
                $scope.lastPasswordNotCurrent = lastPasswordNotCurrent;
            };
            $scope.setEmpSecondNormalPickup = function(empSecondNormalPickup) {
                $scope.empSecondNormalPickup = empSecondNormalPickup;
            };
            $scope.setEmpSecondNormalDrop = function(empSecondNormalDrop) {
                $scope.empSecondNormalDrop = empSecondNormalDrop;
            };
            $scope.setRouteGeofence = function(routeGeofence) {
                $scope.routeGeofence = routeGeofence;
            }
            $scope.setrescheduleDropCutOffTime = function(rescheduleDrop) {
                $scope.rescheduleDrop = rescheduleDrop;
            }
            $scope.setreschedulePickupCutOffTime = function(reschedulePickup) {
                $scope.reschedulePickup = reschedulePickup;
            }
            $scope.setautoClustring = function(autoClustring) {
                $scope.autoClustring = autoClustring;
            }
            $scope.setClusterSize = function(clusterSize) {
                $scope.clusterSize = clusterSize;
            }
            $scope.setRouteGeofence = function(routeGeofence) {
                $scope.routeGeofence = routeGeofence;
            }

            $scope.setSingleDay = function(singleDay) {
                $scope.singleDay = singleDay;
            }

            $scope.setMultipleDay = function(startDate, endDate) {
                var multipleDays = String(startDate).concat(String(endDate));
                var result = multipleDays.split("");

                $scope.multipleDays = $scope.multipleDays.result;
            }

            $scope.setOccurence = function(occurenceCutOff) {
                if (occurenceCutOff) {
                    $scope.occurenceCutOff = "Y";
                } else {
                    $scope.occurenceCutOff = "N";
                }
            }

            $scope.setRequestType = function(requestType) {

                if (requestType == 1) {
                    $scope.requestType = 'Days';
                    $scope.monthOrDays = 'days';
                    $scope.occurrenceFlg = "Y";
                }

                if (requestType == 2) {
                    $scope.requestType = 'Date';
                    $scope.monthOrDays = 'custdate';
                    $scope.occurrenceFlg = "N";
                    $scope.earlyRequestDate = $scope.earlyRequestDate;
                }
                if (requestType == 3) {
                    $scope.requestType = 'Date';
                    $scope.monthOrDays = 'everymonthlastdate';
                    $scope.occurrenceFlg = "Y";
                    if ($scope.earlyRequestDate <= 7) {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    } else {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    }
                }
                if (requestType == 4) {

                    if ($scope.earlyRequestDate >= 7 || $scope.earlyRequestDate == 0) {
                        $scope.earlyRequestDatePerDay = "0";
                    }

                    $scope.requestType = 'custDays';
                    $scope.monthOrDays = 'singleDay';
                    $scope.occurrenceFlg = "Y";
                    if ($scope.earlyRequestDate <= 7) {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    } else {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    }
                }

            }

            $scope.setEarlyRequestDateCustomizeDate = function(earlyRequest) {
                $scope.earlyRequestDate = earlyRequest;
            }

            $scope.setEarlyRequestDateEveryMonth = function(earlyRequest) {
                $scope.earlyRequestDate = earlyRequest;
            }

            $scope.setEarlyRequestDatePerDay = function(earlyRequest) {
                $scope.earlyRequestDate = earlyRequest;
            }

            $scope.setRequestDays = function(requestCutOffNoOfDays) {
                $scope.requestCutOffNoOfDays = requestCutOffNoOfDays;
            }

            $scope.setAutoDriverCheckout = function(driverAutoCheckoutStatus) {
                $scope.driverAutoCheckoutStatus = driverAutoCheckoutStatus;
            }

            $scope.setAutoVehicleAllocation = function(autoVehicleAllocationStatus) {
                $scope.autoVehicleAllocationStatus = autoVehicleAllocationStatus;
            }

            $scope.setRequestDateFrom = function(requestFromDateCutOff) {
                $scope.requestToDateCutOff = '';
                $scope.requestFromDateCutOff = convertDateUTC(requestFromDateCutOff);
            }

            $scope.setRequestDateTo = function(requestToDateCutOff) {
                $scope.requestToDateCutOff = convertDateUTC(requestToDateCutOff);

            }

            $scope.setEarlyRequestDate = function(earlyRequestDate) {
                $scope.earlyRequestDate = earlyRequestDate;

            }

            $scope.setEveryMonthLastDay = function(everyMonthLastday) {
                $scope.everyMonthLastday = everyMonthLastday;

            }

            $scope.setDaysRequest = function(daysRequest) {
                $scope.daysRequest = daysRequest;

            }

            $scope.setDynamicDays = function(dynamicDays) {
                $scope.dynamicDays = dynamicDays;
                $scope.dynamicvalue = dynamicDays;

            }

            $scope.setRequestLocation = function(requestLocation) {
                $scope.requestLocation = requestLocation;
            }

            $scope.setLocationVisible = function(locationVisible) {
                $scope.locationVisible = locationVisible;
            }

            $scope.setInvoiceGenType = function(invoiceGenType) {
                $scope.invoiceGenType = invoiceGenType;
            }

            $scope.setdriverWaitingTime = function(driverWaitingTime) {
                $scope.driverWaitingTime = driverWaitingTime;
            }


            $scope.setBackToBackType = function(backToBackType) {
                $scope.backToBackType = backToBackType;
            }

            $scope.setb2bInDistanceKm = function(b2bByTravelDistanceInKM) {
                $scope.b2bByTravelDistanceInKM = b2bByTravelDistanceInKM;
            }

            $scope.setb2bInMinutes = function(b2bByTravelTimeInMinutes) {
                $scope.b2bByTravelTimeInMinutes = b2bByTravelTimeInMinutes;
            }

            $scope.setDestinationPointLimit = function(destinationPointLimit) {

                $scope.destinationPointLimit = destinationPointLimit;
            }

            $scope.setNotificationCutoffTimePickup = function(notificationCutoffTimePickup) {
                $scope.notificationCutoffTimePickup = notificationCutoffTimePickup
            }

            $scope.setNotificationCutoffTimeDrop = function(notificationCutoffTimeDrop) {
                $scope.notificationCutoffTimeDrop = notificationCutoffTimeDrop
            }

            $scope.setLicenceNotificationType = function(notificationType) {
                $scope.licenceNotificationType = notificationType;

                if (notificationType == 'SMS') {
                    $scope.notificationLicenseTypeSMSShow = true;
                    $scope.notificationLicenseTypeEmailShow = false;
                    $scope.licenceNotificationSMSRequired = true;
                    $scope.licenceNotificationMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationLicenseTypeSMSShow = false;
                    $scope.notificationLicenseTypeEmailShow = true;
                    $scope.licenceNotificationSMSRequired = false;
                    $scope.licenceNotificationMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationLicenseTypeSMSShow = true;
                    $scope.notificationLicenseTypeEmailShow = true;
                    $scope.licenceNotificationSMSRequired = true;
                    $scope.licenceNotificationMessageRequired = true;
                }
            }

            $scope.setMedicalFitnessNotificationType = function(notificationType) {
                $scope.medicalFitnessNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationmedicalFitnessTypeSMSShow = true;
                    $scope.notificationmedicalFitnessTypeEmailShow = false;
                    $scope.medicalFitnessSMSRequired = true;
                    $scope.medicalFitnessMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationmedicalFitnessTypeSMSShow = false;
                    $scope.notificationmedicalFitnessTypeEmailShow = true;
                    $scope.medicalFitnessSMSRequired = false;
                    $scope.medicalFitnessMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationmedicalFitnessTypeSMSShow = true;
                    $scope.notificationmedicalFitnessTypeEmailShow = true;
                    $scope.medicalFitnessSMSRequired = true;
                    $scope.medicalFitnessMessageRequired = true;
                }
            }

            $scope.setPoliceVerificationNotificationType = function(notificationType) {
                $scope.policeVerificationNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationPoliceVerificationTypeSMSShow = true;
                    $scope.notificationPoliceVerificationTypeEmailShow = false;
                    $scope.policeVerificationSMSRequired = true;
                    $scope.policeVerificationMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationPoliceVerificationTypeSMSShow = false;
                    $scope.notificationPoliceVerificationTypeEmailShow = true;
                    $scope.policeVerificationSMSRequired = false;
                    $scope.policeVerificationMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationPoliceVerificationTypeSMSShow = true;
                    $scope.notificationPoliceVerificationTypeEmailShow = true;
                    $scope.policeVerificationSMSRequired = true;
                    $scope.policeVerificationMessageRequired = true;
                }
            }

            $scope.setDDTrainingNotificationType = function(notificationType) {
                $scope.DDTrainingNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.DDTrainingTypeSMSShow = true;
                    $scope.DDTrainingTypeEmailShow = false;
                    $scope.DDTrainingSMSRequired = true;
                    $scope.DDTrainingMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.DDTrainingTypeSMSShow = false;
                    $scope.DDTrainingTypeEmailShow = true;
                    $scope.DDTrainingSMSRequired = false;
                    $scope.DDTrainingMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.DDTrainingTypeSMSShow = true;
                    $scope.DDTrainingTypeEmailShow = true;
                    $scope.DDTrainingSMSRequired = true;
                    $scope.DDTrainingMessageRequired = true;
                }
            }

            $scope.setPollutionDue = function(notificationType) {
                $scope.pollutionDueNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationPollutionDueSMSShow = true;
                    $scope.notificationPollutionDueEmailShow = false;
                    $scope.pollutionDueSMSRequired = true;
                    $scope.pollutionDueMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationPollutionDueSMSShow = false;
                    $scope.notificationPollutionDueEmailShow = true;
                    $scope.pollutionDueSMSRequired = false;
                    $scope.pollutionDueMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationPollutionDueSMSShow = true;
                    $scope.notificationPollutionDueEmailShow = true;
                    $scope.pollutionDueSMSRequired = true;
                    $scope.pollutionDueMessageRequired = true;
                }
            }

            $scope.setInsuranceDue = function(notificationType) {
                $scope.insuranceDueNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationInsuranceDueSMSShow = true;
                    $scope.notificationInsuranceDueEmailShow = false;
                    $scope.insuranceDueSMSRequired = true;
                    $scope.insuranceDueMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationInsuranceDueSMSShow = false;
                    $scope.notificationInsuranceDueEmailShow = true;
                    $scope.insuranceDueSMSRequired = false;
                    $scope.insuranceDueMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationInsuranceDueSMSShow = true;
                    $scope.notificationInsuranceDueEmailShow = true;
                    $scope.insuranceDueSMSRequired = true;
                    $scope.insuranceDueMessageRequired = true;
                }
            }

            $scope.setTaxCertificate = function(notificationType) {
                $scope.taxCertificateNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationTaxCertificateSMSShow = true;
                    $scope.notificationTaxCertificateEmailShow = false;
                    $scope.taxCertificateSMSRequired = true;
                    $scope.taxCertificateMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationTaxCertificateSMSShow = false;
                    $scope.notificationTaxCertificateEmailShow = true;
                    $scope.taxCertificateSMSRequired = false;
                    $scope.taxCertificateMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationTaxCertificateSMSShow = true;
                    $scope.notificationTaxCertificateEmailShow = true;
                    $scope.taxCertificateSMSRequired = true;
                    $scope.taxCertificateMessageRequired = true;
                }
            }

            $scope.setPermitDue = function(notificationType) {
                $scope.permitDueNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationPermitDueSMSShow = true;
                    $scope.notificationPermitDueEmailShow = false;
                    $scope.permitDueSMSRequired = true;
                    $scope.permitDueMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationPermitDueSMSShow = false;
                    $scope.notificationPermitDueEmailShow = true;
                    $scope.permitDueRequired = false;
                    $scope.permitDueMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationPermitDueSMSShow = true;
                    $scope.notificationPermitDueEmailShow = true;
                    $scope.permitDueSMSRequired = true;
                    $scope.permitDueMessageRequired = true;
                }
            }

            $scope.setVehicelMaintenance = function(notificationType) {
                $scope.vehicelMaintenanceNotificationType = notificationType;
                if (notificationType == 'SMS') {
                    $scope.notificationVehicelMaintenanceSMSShow = true;
                    $scope.notificationVehicelMaintenanceEmailShow = false;
                    $scope.vehicelMaintenanceSMSRequired = true;
                    $scope.vehicelMaintenanceMessageRequired = false;
                } else if (notificationType == 'Email') {
                    $scope.notificationVehicelMaintenanceSMSShow = false;
                    $scope.notificationVehicelMaintenanceEmailShow = true;
                    $scope.vehicelMaintenanceSMSRequired = false;
                    $scope.vehicelMaintenanceMessageRequired = true;
                } else if (notificationType == 'Both') {
                    $scope.notificationVehicelMaintenanceSMSShow = true;
                    $scope.notificationVehicelMaintenanceEmailShow = true;
                    $scope.vehicelMaintenanceSMSRequired = true;
                    $scope.vehicelMaintenanceMessageRequired = true;
                }
            }


            $scope.setNotificationTypeSMS = function(notificationTypeSMS) {
                $scope.notificationTypeSMS = notificationTypeSMS
            }

            $scope.setNotificationTypeEmail = function(notificationTypeEmail) {
                $scope.notificationTypeEmail = notificationTypeEmail
            }

            $scope.setEscortConfirmation = function(toggleValue) {
                $scope.escortTimeStatus = toggleValue;
                if (toggleValue === 'Y') {
                    $scope.escortEditTimeShow = true;
                    $scope.escortConfirmationYES = true;
                    $scope.escortConfirmationNO = false;
                } else {
                    $scope.escortEditTimeShow = false;
                    $scope.escortConfirmationYES = false;
                    $scope.escortConfirmationNO = true;
                }
            }

            $scope.setEscortType = function(value) {
                $scope.escortSelection = value;
                if (value == 'None') {
                    $scope.escortTimeDivShow = false;
                } else if (value == 'Always') {
                    $scope.escortTimeDivShow = true;
                    $scope.escortTimingPanelTitle = 'Always';
                } else if (value == 'femalepresent') {
                    $scope.escortTimeDivShow = true;
                    $scope.escortTimingPanelTitle = 'When Female Passenger Is Present';
                } else if (value == 'firstlastfemale') {
                    $scope.escortTimeDivShow = true;
                    $scope.escortTimingPanelTitle = 'Last drop and first pickup of female passenger';
                } else if (value == 'femaleAlone') {
                    $scope.escortTimeDivShow = true;
                    $scope.escortTimingPanelTitle = 'When only One female passenger alone in route';
                } else if (value == 'allFemale') {
                    $scope.escortTimeDivShow = true;
                    $scope.escortTimingPanelTitle = 'When all female passenger in route';
                }
            }

            $scope.setPersonalDeviceViaSms = function(value) {
                $scope.personalDeviceViaSms = value;
            }
            $scope.setAssignRoutesToVendor = function(value) {
                $scope.assignRoutesToVendor = value;
            }
            $scope.setDriverCallToEmployee = function(value) {
                $scope.driverCallToEmployee = value;
            }
            $scope.setDriverCallToTransportDesk = function(value) {
                $scope.driverCallToTransportDesk = value;
            }
            $scope.setEmployeeCallToDriver = function(value) {
                $scope.employeeCallToDriver = value;
            }
            $scope.setEmployeeCallToTransport = function(value) {
                $scope.employeeCallToTransport = value;
            }
            $scope.setLicenseExpiryDate = function(value) {
                $scope.licenseExpiryDate = value;
            }
            $scope.setRepeatAlerts = function(value) {
                $scope.repeatAlerts = value;
            }

            $scope.setInvoiceWorkingDays = function(value) {
                $scope.invoiceNoOfDay = value;
            }



            $scope.setDelayTrips = function(value) {
                $scope.delayTrips = value;
            }


            $scope.setAutoCancelDrop = function(autoCancelDrop) {
                $scope.autoCancelDrop = autoCancelDrop;
            }
            $scope.plaCardPrintChange = function(plaCardPrint) {

                $scope.plaCardPrint = plaCardPrint;
                if (plaCardPrint == 'Yes') {
                    $scope.plaCardTemplateTypeShow = true;
                } else {
                    $scope.plaCardTemplateTypeShow = false;
                }
            }

            $scope.setPlaCardTemplateType = function(value) {

                if (value == 'Customized Own Template') {
                    $scope.customizedTemplateShow = true;
                    $scope.defultLogoTemplateShow = false;
                } else {
                    $scope.customizedTemplateShow = false;
                    $scope.defultLogoTemplateShow = true;
                }
            }

            $scope.setConsecutiveNoShow = function(value) {
                $scope.consecutiveNoShowCount = value;
            }

            $scope.setGpsKmModification = function(gpsKmModification) {
                $scope.gpsKmModification = gpsKmModification;
            }

            $scope.setMobileLoginUrl = function(mobileLoginUrl) {
                $scope.mobileLoginUrl = mobileLoginUrl;
            }

            $scope.setSSOLoginUrl = function(ssoLoginUrl) {
                $scope.ssoLoginUrl = ssoLoginUrl;
            }

            $scope.setWebPageCount = function(webPageCount) {
                $scope.webPageCount = webPageCount;
            }

            $scope.setMobilePageCount = function(mobilePageCount) {
                $scope.mobilePageCount = mobilePageCount;
            }

            $scope.setMobileLoginVia = function(mobileLoginVia) {
                $scope.mobileLoginVia = mobileLoginVia;
            }

            $scope.setIsMultiFacility = function(isMultiFacility) {
                $scope.isMultiFacility = isMultiFacility;
            }

            /*<!-- Driver Governance -->*/

            /*<!-- License -->*/

            $scope.getlicenseExpiryDay = function(value) {
                $scope.licenseExpiryDay = value
            }
            $scope.getlicenseRepeatAlertsEvery = function(value) {
                $scope.licenseRepeatAlertsEvery = value
            }
            $scope.getlicenseSMSDays = function(value) {
                $scope.licenseSMSDays = value
            }
            $scope.getlicenseEmailDays = function(value) {
                $scope.licenseEmailDays = value
            }

            /*<!-- MedicalFitness -->*/

            $scope.getmedicalFitnessExpiryDay = function(value) {
                $scope.medicalFitnessExpiryDay = value
            }
            $scope.getmedicalFitnessRepeatAlertsEvery = function(value) {
                $scope.medicalFitnessRepeatAlertsEvery = value
            }
            $scope.getmedicalFitnessSMSDays = function(value) {
                $scope.medicalFitnessSMSDays = value
            }
            $scope.getmedicalFitnessEmailDays = function(value) {
                $scope.medicalFitnessEmailDays = value
            }

            /*<!-- PoliceVerification -->*/

            $scope.getpoliceVerificationExpiryDay = function(value) {
                $scope.policeVerificationExpiryDay = value
            }
            $scope.getpoliceVerificationRepeatAlertsEvery = function(value) {
                $scope.policeVerificationRepeatAlertsEvery = value
            }
            $scope.getpoliceVerificationSMSDays = function(value) {
                $scope.policeVerificationSMSDays = value
            }
            $scope.getpoliceVerificationEmailDays = function(value) {
                $scope.policeVerificationEmailDays = value
            }

            /*<!-- DDTraining -->*/

            $scope.getDDTrainingExpiryDay = function(value) {
                $scope.DDTrainingExpiryDay = value
            }
            $scope.getDDTrainingRepeatAlertsEvery = function(value) {
                $scope.DDTrainingRepeatAlertsEvery = value
            }
            $scope.getDDTrainingSMSDays = function(value) {
                $scope.DDTrainingSMSDays = value
            }
            $scope.getDDTrainingEmailDays = function(value) {
                $scope.DDTrainingEmailDays = value
            }


            /*<!-- Vehicel Governance -->*/

            /*<!-- pollutionDue  -->*/

            $scope.getpollutionDueExpiryDay = function(value) {
                $scope.pollutionDueExpiryDay = value
            }
            $scope.getpollutionDueRepeatAlertsEvery = function(value) {
                $scope.pollutionDueRepeatAlertsEvery = value
            }
            $scope.getpollutionDueSMSDays = function(value) {
                $scope.pollutionDueSMSDays = value
            }
            $scope.getpollutionDueEmailDays = function(value) {
                $scope.pollutionDueEmailDays = value
            }

            /*<!-- insuranceDue -->*/

            $scope.getinsuranceDueExpiryDay = function(value) {
                $scope.insuranceDueExpiryDay = value
            }
            $scope.getinsuranceDueRepeatAlertsEvery = function(value) {
                $scope.insuranceDueRepeatAlertsEvery = value
            }
            $scope.getinsuranceDueSMSDays = function(value) {
                $scope.insuranceDueSMSDays = value
            }
            $scope.getinsuranceDueEmailDays = function(value) {
                $scope.insuranceDueEmailDays = value
            }

            /*<!--  taxCertificate -->*/

            $scope.gettaxCertificateExpiryDay = function(value) {
                $scope.taxCertificateExpiryDay = value
            }
            $scope.gettaxCertificateRepeatAlertsEvery = function(value) {
                $scope.taxCertificateRepeatAlertsEvery = value
            }
            $scope.gettaxCertificateSMSDays = function(value) {
                $scope.taxCertificateSMSDays = value
            }
            $scope.gettaxCertificateEmailDays = function(value) {
                $scope.taxCertificateEmailDays = value
            }

            /*<!--  permitDue -->*/

            $scope.getpermitDueExpiryDay = function(value) {
                $scope.permitDueExpiryDay = value
            }
            $scope.getpermitDueRepeatAlertsEvery = function(value) {
                $scope.permitDueRepeatAlertsEvery = value
            }
            $scope.getpermitDueSMSDays = function(value) {
                $scope.permitDueSMSDays = value
            }
            $scope.getpermitDueEmailDays = function(value) {
                $scope.permitDueEmailDays = value
            }

            /*<!--  vehicelMaintenance -->*/

            $scope.getvehicelMaintenanceExpiryDay = function(value) {
                $scope.vehicelMaintenanceExpiryDay = value
            }
            $scope.getvehicelMaintenanceRepeatAlertsEvery = function(value) {
                $scope.vehicelMaintenanceRepeatAlertsEvery = value
            }
            $scope.getvehicelMaintenanceSMSDays = function(value) {
                $scope.vehicelMaintenanceSMSDays = value
            }
            $scope.getvehicelMaintenanceEmailDays = function(value) {
                $scope.vehicelMaintenanceEmailDays = value
            }

            $scope.setEscortStartTime = function(value) {
                $scope.escortStartTime = value;
            }

            $scope.setEscortEndTime = function(value) {
                $scope.escortEndTime = value;
            }
            $scope.setEscortStartTimeDrop = function(value) {
                $scope.escortStartTimeDrop = value;
            }

            $scope.setEscortEndTimeDrop = function(value) {
                $scope.escortEndTimeDrop = value;
            }
            $scope.setOTPEnableTimeLimit = function(value) {
                $scope.OTPEnableTimeLimit = value;
            }

            $scope.setOTPGenerationMaximumLimit = function(value) {
                $scope.OTPGenerationMaximumLimit = value;
            }

            $scope.setApprovalProcess = function(value) {
                $scope.approvalProcess = value;
                if (value == 'Yes') {
                    $scope.postApprovalRequired = true;
                    $scope.postApprovalShow = true;
                } else {
                    $scope.postApprovalRequired = false;
                    $scope.postApprovalShow = false;
                }
            }

            $scope.setRequestWithProject = function(value) {
                $scope.requestWithProject = value;
                if (value == 'Yes') {
                    $scope.managerReqCreateProcessShow = true;
                } else {
                    $scope.managerReqCreateProcessShow = false;
                }
            }

            $scope.setVehiceCheckList = function(value) {
                $scope.vehiceCheckList = value;
            }

            $scope.setPostApproval = function(value) {
                $scope.postApproval = value;
            }

            $scope.setShiftTimeGenderPreference = function(value) {
                $scope.shiftTimeGenderPreference = value;
            }

            $scope.setMinimumDestCount = function(value) {
                $scope.minimumDestCount = value;
            }

            $scope.setDriverHistory = function(value) {
                $scope.driverHistory = value;
            }

            $scope.setManagerReqCreateProcess = function(value) {
                $scope.managerReqCreateProcess = value;
            }

            $scope.setTripHistoryCount = function(value) {
                $scope.tripHistoryCount = value;
            }

            $scope.customMessageText = function(size) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/customMessageTexts.jsp',
                    controller: 'customMessageTextsCtrl',
                    size: size,
                    resolve: {}
                });

                modalInstance.result.then(function(result) {

                });
            };

            $scope.saveAppSettings = function(emrgContactNumber, combinedFacilityId) {
                var facilityBranchName = [];

                angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                    var branchName = _.where(userFacilities, {
                        branchId: item
                    })[0].name;
                    facilityBranchName.push(branchName);
                });


                $scope.dynamicDaysData = $scope.dynamicDays;

                $scope.dynamicDays = $scope.dynamicDays.toString();

                if ($scope.dynamicDays == 3) {
                    $scope.requestType = 'Date';
                    $scope.monthOrDays = 'everymonthlastdate';
                    $scope.occurrenceFlg = "Y";
                    if ($scope.earlyRequestDate <= 7) {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    } else {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    }

                    $scope.earlyRequestDate = $scope.earlyRequestDate;

                }



                if ($scope.dynamicDays == "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday" || $scope.dynamicDays == []) {
                    $scope.dynamicDays = "All";
                } else {
                    $scope.dynamicDays = $scope.dynamicDaysData;
                    $scope.dynamicDaysDataNew = [];
                    angular.forEach($scope.dynamicDays, function(value, key) {

                        var demo = "Sunday";

                        if (value == "Sunday") {
                            var Sunday = "1";
                            $scope.dynamicDaysDataNew.push(Sunday);
                        }
                        if (value == "Monday") {
                            var Monday = "2";
                            $scope.dynamicDaysDataNew.push(Monday);
                        }
                        if (value == "Tuesday") {
                            var Tuesday = "3";
                            $scope.dynamicDaysDataNew.push(Tuesday);
                        }
                        if (value == "Wednesday") {
                            var Wednesday = "4";
                            $scope.dynamicDaysDataNew.push(Wednesday);
                        }
                        if (value == "Thursday") {
                            var Thursday = "5";
                            $scope.dynamicDaysDataNew.push(Thursday);
                        }
                        if (value == "Friday") {
                            var Friday = "6";
                            $scope.dynamicDaysDataNew.push(Friday);
                        }
                        if (value == "Saturday") {
                            var Saturday = "7";
                            $scope.dynamicDaysDataNew.push(Saturday);
                        }
                    });


                    $scope.dynamicDays = $scope.dynamicDaysDataNew.toString().replace(/\,/g, "");
                }




                if ($scope.requestType == 1) {
                    $scope.requestType = 'Days';
                    $scope.occurrenceFlg = "Y";

                }

                if ($scope.requestType == 2) {
                    $scope.earlyRequestDate = $scope.earlyRequestDate;
                    $scope.requestType = 'Date';
                    $scope.monthOrDays = 'custdate';
                    $scope.occurrenceFlg = $scope.occurenceCutOff;
                }

                if ($scope.requestType == 'Date' && $scope.monthOrDays == 'custdate') {
                    $scope.occurrenceFlg = $scope.occurenceCutOff;
                    if ($scope.dynamicDaysData == 'All' || $scope.dynamicDaysData.length == 7) {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    } else if ($scope.dynamicDaysData.length < 7) {
                        if ($scope.earlyRequestDate != 0 && $scope.earlyRequestDate < 7) {


                            ngDialog
                                .open({
                                    template: 'Early request date should be more than 7 or all week days',
                                    plain: true
                                });
                            $timeout(function() {
                                $scope.getApplicationSettings($scope.currentCombinedFacilityId);
                            }, 1000);
                            return false;
                        }

                    }
                }



                if ($scope.requestType == 3) {
                    $scope.requestType = 'Date';
                }


                if ($scope.requestType == 4) {

                    $scope.requestType = 'custDays';
                    $scope.monthOrDays = 'singleDay';
                    $scope.occurrenceFlg = "Y";
                    if ($scope.earlyRequestDate <= 7) {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    } else {
                        $scope.earlyRequestDate = $scope.earlyRequestDate;
                    }
                }

                if (angular.isDate($scope.requestFromDateCutOff)) {
                    $scope.requestFromDateCutOff = convertDateUTC($scope.requestFromDateCutOff);

                } else {
                    $scope.requestFromDateCutOff = $scope.requestFromDateCutOff;

                }

                if (angular.isDate($scope.requestToDateCutOff)) {
                    $scope.requestToDateCutOff = convertDateUTC($scope.requestToDateCutOff);

                } else {
                    $scope.requestToDateCutOff = $scope.requestToDateCutOff;

                }

                var tempSelectShiftsIds = $scope.selectedEscortConstraints.map(
                    function(el) {
                        return el.id;
                    }).join(',');

                if ($scope.shiftSelectType == undefined) {
                    $scope.shiftSelectType = 'Normal';
                }

                if ($scope.tripType == undefined) {
                    $scope.tripType = 'DROP';
                }

                if ($scope.shiftSelectType == 'S') {
                    if ($scope.shiftTime == undefined) {
                        ngDialog.open({
                            template: 'Kindly Choose Shift Time',
                            plain: true
                        });
                        return false;
                    }
                }

                if ($scope.shiftTime == undefined) {
                    $scope.shiftTime = "00:00";
                }



                $scope.occurrenceFlg = $scope.occurrenceFlg;


                if ($scope.occurrenceFlg == true || $scope.occurrenceFlg == 'Y') {
                    $scope.occurrenceFlg = 'Y';
                } else {
                    $scope.occurrenceFlg = 'N';
                }




                if (angular.isObject($scope.isMultiFacility)) {

                    if ($scope.isMultiFacility.value == 'Y') {
                        $scope.isMultiFacility = 1;
                    } else {
                        $scope.isMultiFacility = 0;
                    }
                } else {
                    if ($scope.isMultiFacility == 'Y') {
                        $scope.isMultiFacility = 1;
                    } else {
                        $scope.isMultiFacility = 0;
                    }
                }



                var d1 = convertDateUTC(new Date());
                var d2 = $scope.requestFromDateCutOff;

                var data = {
                    branchId: branchId,
                    userId: profileId,
                    escortRequired: $scope.escortSelection,
                    mangerApprovalRequired: $scope.approvalNeeded,
                    etaSMS: $scope.etaSMS,
                    employeeAddressgeoFenceArea: $scope.geoFenceArea,
                    shiftTimePlusOneHourrAfterSMSContact: $scope.after13HrSMSContact,
                    shiftTimePlusTwoHourrAfterSMSContact: $scope.after14HrSMSContact,
                    maxSpeed: $scope.maxSpeedArray,
                    driverAutoCheckOut: convertToTime($scope.driverCheckedout),
                    delayMessageTime: $scope.delayMessage,
                    employeeWaitingTime: $scope.employeeWaitingTime,
                    emergencyContactNumber: $scope.emrgContactNumber,
                    maxTravelTimeEmployeeWiseInMin: $scope.maxTravelTime,
                    maxRadialDistanceEmployeeWiseInKm: $scope.maxRadialDistance,
                    maxRouteLengthInKm: $scope.maxRouteLength,
                    maxRouteDeviationInKm: $scope.maxRoutedeviation,
                    transportContactNumberForMsg: $scope.transportContactNumber,
                    employeeAppReportBug: $scope.employeeAppReportBug,
                    reportBugEmailIds: $scope.reportBugEmailIds,
                    toEmployeeFeedBackEmail: $scope.toEmployeeFeedBackEmail,
                    employeeFeedbackEmailId: $scope.employeeFeedbackEmailId,
                    autoClustering: 'Enable', // future will be change
                    startTripGeoFenceAreaInMeter: $scope.autoTripStartGF,
                    endTripGeoFenceAreaInMeter: $scope.autoTripEndGF,
                    feedBackEmailId: $scope.transportFeedbackEmailId,
                    clusterSize: $scope.clusterSize,
                    empDeviceDriverImage: $scope.driverImage,
                    empDeviceDriverMobileNumber: $scope.driverMobileNuumber,
                    employeeCheckOutGeofenceRegion: $scope.employeeCheckOutGeofenceRegion,
                    employeeFeedbackEmail: $scope.employeeFeedbackEmail,
                    empDeviceDriverName: $scope.driverName,
                    driverDeviceDriverProfilePicture: $scope.profilePic,
                    driverDeviceAutoCallAndsms: $scope.autoCallSmsDisable,
                    tripType: $scope.pickDropNeeded,

                    numberOfAdministarator: $scope.adminRequired,
                    inactiveAdminAccountAfterNumOfDays: $scope.adminInactiveAccount,
                    passwordResetPeriodForAdminInDays: $scope.resetPaswwordForAdmin,

                    passwordResetPeriodForUserInDays: $scope.resetPaswwordForUser,
                    twoFactorAuthenticationRequired: $scope.twoWayFactor,
                    numberOfAttemptsWrongPass: $scope.numberOfAttemptsWrongPass,
                    sessionTimeoutInMinutes: $scope.sessiontimeOut,
                    sessionNotificationTime: Number($scope.sessionTimeOutNotificationInSec),
                    invoiceNumberDigitRange: $scope.invoiceNumberRange,
                    adhocTimePickerForEmployee: $scope.adhocTimePicker,
                    dropCutOffTime: convertToTime($scope.dropPriorTime),
                    pickupCutOffTime: convertToTime($scope.pickupPriorTime),
                    cutOffTime: $scope.cutOff.cutOffTime.value,
                    employeeSecondPickUpRequest: $scope.empSecondNormalPickup,
                    employeeSecondDropRequest: $scope.empSecondNormalDrop,
                    panicAlertNeeded: $scope.panicAlertNeeded,
                    lastPassCanNotCurrentPass: $scope.lastPasswordNotCurrent,
                    autoDropRoster: $scope.autoDropRoster,
                    dropPickupReqCutOfTime: convertToTime($scope.shiftTimeDifference),
                    addingGeoFenceDistanceIntrip: $scope.addDistanceRecoverGeoFence,
                    cutOffTime: $scope.cutOff.cutOffTime,
                    invoiceNoOfDWorkingDays: $scope.invoiceNoOfDay,
                    invoiceGenDate: $scope.invoiceGenDate,
                    rescheduleDropTime: convertToTime($scope.rescheduleDrop),
                    reschedulePickupTime: convertToTime($scope.reschedulePickup),
                    pickupCancelCutOffTime: convertToTime($scope.pickupCancelCutOffTime),
                    dropCancelCutOffTime: convertToTime($scope.dropCancelCutOffTime),
                    shiftSelectType: $scope.shiftSelectType,
                    distanceFlg: $scope.distanceFlg,
                    requestDateCutOff: $scope.requestDateCutOff,
                    requestCutOffNoOfDays: $scope.requestCutOffNoOfDays,
                    requestType: $scope.requestType,
                    earlyRequestDate: $scope.earlyRequestDate,
                    monthOrDays: $scope.monthOrDays,
                    occurrenceFlg: $scope.occurrenceFlg,

                    daysRequest: $scope.dynamicDays,
                    driverAutoCheckoutStatus: $scope.driverAutoCheckoutStatus,
                    autoVehicleAllocationStatus: $scope.autoVehicleAllocationStatus,
                    requestFromDateCutOff: $scope.requestFromDateCutOff,
                    requestToDateCutOff: $scope.requestToDateCutOff,
                    driverLastLocWaitingTime: convertToTime($scope.driverWaitingTime),
                    selectedB2bType: $scope.backToBackType,
                    b2bByTravelDistanceInKM: $scope.b2bByTravelDistanceInKM,
                    travelTimeFromDropToFirstPickUp: convertToTime($scope.b2bByTravelTimeInMinutes),

                    locationVisible: $scope.locationVisible,
                    invoiceGenType: $scope.invoiceGenType,
                    destinationPointLimit: $scope.destinationPointLimit,

                    notificationCutoffTimePickup: convertToTime($scope.notificationCutoffTimePickup),
                    notificationCutoffTimeDrop: convertToTime($scope.notificationCutoffTimeDrop),
                    notificationType: $scope.notificationType,
                    personalDeviceViaSms: $scope.personalDeviceViaSms,
                    assignRoutesToVendor: $scope.assignRoutesToVendor,
                    driverCallToEmployee: $scope.driverCallToEmployee.value,
                    driverCallToTransportDesk: $scope.driverCallToTransportDesk.value,
                    employeeCallToDriver: $scope.employeeCallToDriver.value,
                    employeeCallToTransport: $scope.employeeCallToTransport.value,
                    licenseExpiryDay: $scope.licenseExpiryDay,
                    licenseRepeatAlertsEvery: $scope.licenseRepeatAlertsEvery,
                    licenseSMSNumber: $scope.licenseSMSDays,
                    licenseEmailId: $scope.licenseEmailDays,
                    medicalFitnessExpiryDay: $scope.medicalFitnessExpiryDay,
                    medicalFitnessRepeatAlertsEvery: $scope.medicalFitnessRepeatAlertsEvery,
                    medicalFitnessSMSNumber: $scope.medicalFitnessSMSDays,
                    medicalFitnessEmailId: $scope.medicalFitnessEmailDays,
                    policeVerificationExpiryDay: $scope.policeVerificationExpiryDay,
                    policeVerificationRepeatAlertsEvery: $scope.policeVerificationRepeatAlertsEvery,
                    policeVerificationSMSNumber: $scope.policeVerificationSMSDays,
                    policeVerificationEmailId: $scope.policeVerificationEmailDays,

                    ddTrainingExpiryDay: $scope.DDTrainingExpiryDay,
                    ddTrainingRepeatAlertsEvery: $scope.DDTrainingRepeatAlertsEvery,
                    ddTrainingSMSNumber: $scope.DDTrainingSMSDays,
                    ddTrainingEmailId: $scope.DDTrainingEmailDays,

                    pollutionDueExpiryDay: $scope.pollutionDueExpiryDay,
                    pollutionDueRepeatAlertsEvery: $scope.pollutionDueRepeatAlertsEvery,
                    pollutionDueSMSNumber: $scope.pollutionDueSMSDays,
                    pollutionDueEmailId: $scope.pollutionDueEmailDays,
                    insuranceDueExpiryDay: $scope.insuranceDueExpiryDay,
                    insuranceDueRepeatAlertsEvery: $scope.insuranceDueRepeatAlertsEvery,
                    insuranceDueSMSNumber: $scope.insuranceDueSMSDays,
                    insuranceDueEmailId: $scope.insuranceDueEmailDays,
                    taxCertificateExpiryDay: $scope.taxCertificateExpiryDay,
                    taxCertificateRepeatAlertsEvery: $scope.taxCertificateRepeatAlertsEvery,
                    taxCertificateSMSNumber: $scope.taxCertificateSMSDays,
                    taxCertificateEmailId: $scope.taxCertificateEmailDays,
                    permitDueExpiryDay: $scope.permitDueExpiryDay,
                    permitDueRepeatAlertsEvery: $scope.permitDueRepeatAlertsEvery,
                    permitDueSMSNumber: $scope.permitDueSMSDays,
                    permitDueEmailId: $scope.permitDueEmailDays,
                    vehicelMaintenanceExpiryDay: $scope.vehicelMaintenanceExpiryDay,
                    vehicelMaintenanceRepeatAlertsEvery: $scope.vehicelMaintenanceRepeatAlertsEvery,
                    vehicelMaintenanceSMSNumber: $scope.vehicelMaintenanceSMSDays,
                    vehicelMaintenanceEmailId: $scope.vehicelMaintenanceEmailDays,
                    licenceNotificationType: $scope.licenceNotificationType,
                    medicalFitnessNotificationType: $scope.medicalFitnessNotificationType,
                    policeVerificationNotificationType: $scope.policeVerificationNotificationType,
                    ddTrainingNotificationType: $scope.DDTrainingNotificationType,
                    pollutionDueNotificationType: $scope.pollutionDueNotificationType,
                    insuranceDueNotificationType: $scope.insuranceDueNotificationType,
                    taxCertificateNotificationType: $scope.taxCertificateNotificationType,
                    permitDueNotificationType: $scope.permitDueNotificationType,
                    vehicelMaintenanceNotificationType: $scope.vehicelMaintenanceNotificationType,
                    otpDisableTime: convertToTime($scope.OTPEnableTimeLimit),
                    maxTimeOTP: Number($scope.OTPGenerationMaximumLimit),
                    escortStartTimeForPickup: convertToTime($scope.escortStartTime),
                    escortStartTimeForDrop: convertToTime($scope.escortStartTimeDrop),
                    escortEndTimeForPickup: convertToTime($scope.escortEndTime),
                    escortEndTimeForDrop: convertToTime($scope.escortEndTimeDrop),
                    escortTimeWindowEnable: $scope.escortTimeStatus,
                    approvalProcess: $scope.approvalProcess,
                    requestWithProject: $scope.requestWithProject,
                    vehiceCheckList: $scope.vehiceCheckList,
                    postApproval: $scope.postApproval,
                    shiftTimeGenderPreference: $scope.shiftTimeGenderPreference,
                    minimumDestCount: $scope.minimumDestCount,
                    driverTripHistory: $scope.driverHistory,
                    managerReqCreateProcess: $scope.managerReqCreateProcess,
                    numberOfConsecutiveNoShow: Number($scope.consecutiveNoShowCount),
                    onPickUpNoShowCancelDrop: $scope.autoCancelDrop,
                    plaCardPrint: $scope.plaCardPrint,
                    tripDelayTime: convertToTime($scope.delayTrips),
                    gpsKmModification: $scope.gpsKmModification,
                    combinedFacility: combinedFacilityId.toString(),
                    employeeCheckInVia: $scope.employeeCheckInVia.value,
                    selectVehicleCapacitywise: $scope.selectVehicleCapacitywise,
                    vehicleType: $scope.vehicleType,
                    mobileLoginUrl: $scope.mobileLoginUrl,
                    ssoLoginUrl: $scope.ssoLoginUrl,
                    webPageCount: $scope.webPageCount,
                    mobilePageCount: $scope.mobilePageCount,
                    mobileLoginVia: $scope.mobileLoginVia.value,
                    multiFacility: $scope.isMultiFacility,


                };


                if ($scope.shiftSelectType == 'ShiftWise') {
                    var shiftWiseData = {
                        tripType: $scope.tripType,
                        time: $scope.shiftTime,
                        areaGeoFenceRegion: $scope.autoClustring,
                        clusterGeoFenceRegion: $scope.clusterSize,
                        routeGeoFenceRegion: $scope.routeGeofence,
                    }
                    Object.assign(data, shiftWiseData);
                }


                if (multiFacility == "true") {
                    $confirm({
                        text: "Are you sure you want to save application Settings for " + ' ' + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    }).then(function() {

                        $http.post('services/user/updatebranch/', data)
                            .success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.status == "success") {
                                        $timeout(function() {
                                            $scope.getApplicationSettings($scope.currentCombinedFacilityId);
                                        }, 2000);
                                        ngDialog.open({
                                            template: 'Saved successfully',
                                            plain: true
                                        });

                                    } else if (data.status == "fail") {
                                        ngDialog
                                            .open({
                                                template: 'Sorry you already have ' +
                                                    data.numberOfAdmin +
                                                    ' admins in system.So in configuration it can not be less than exists admins',
                                                plain: true
                                            });
                                    }
                                }
                            }).error(
                                function(data, status, headers, config) {
                                    // log error
                                });
                    });
                } else {

                    $http.post('services/user/updatebranch/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $timeout(function() {
                                        $scope.getApplicationSettings($scope.currentCombinedFacilityId);
                                    }, 2000);
                                    ngDialog.open({
                                        template: 'Saved successfully',
                                        plain: true
                                    });

                                } else if (data.status == "fail") {
                                    ngDialog
                                        .open({
                                            template: 'Sorry you already have ' +
                                                data.numberOfAdmin +
                                                ' admins in system.So in configuration it can not be less than exists admins',
                                            plain: true
                                        });
                                }
                            }
                        }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                }




            };

            $scope.saveLocation = function(newAddress) {
                $scope.isEdit = false;
                $scope.isChangeLocation = false;
            };

            $scope.editLocation = function() {
                $scope.isEdit = true;
            };

            $scope.openMap = function(size) {
                if (!$scope.mapClick) {
                    $scope.mapClick = true;
                    $scope.isEdit = true;
                } else
                    $scope.mapClick = false;

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/mapMyProfile.jsp',
                    controller: 'mapMyProfileCtrl',
                    size: size,
                    resolve: {
                        officeLocation: function() {
                            return $scope.officeLocation;
                        }
                    }
                });
                modalInstance.result.then(function(result) {
                    $scope.currLocation = result.address;
                    $scope.currCords = result.cords;
                });
            };

            $scope.assignRole = function(user) {
                if ($scope.user_Type == undefined) {
                    ngDialog.open({
                        template: 'Please change the role',
                        plain: true
                    });

                    return false;
                }
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    efmFmUserMaster: {
                        userId: user.userId
                    },
                    efmFmRoleMaster: {
                        role: $scope.user_Type
                    },
                    combinedFacility: combinedFacility
                };
                $http.post('services/user/changeuserrole/', data)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (Object.keys(data).length == 0) {
                                ngDialog
                                    .open({
                                        template: 'This User does not have any Manager.',
                                        plain: true
                                    });

                            } else if (data.status == 'exist') {
                                $confirm({
                                        text: data.name +
                                            " is an existing Manager. Do you really want to change the role of this user to Manger",
                                        title: 'Delete Confirmation',
                                        ok: 'Yes',
                                        cancel: 'No'
                                    })
                                    .then(
                                        function() {

                                            var data = {
                                                eFmFmClientBranchPO: {
                                                    branchId: branchId
                                                },
                                                efmFmUserMaster: {
                                                    userId: user.userId
                                                },
                                                efmFmRoleMaster: {
                                                    role: $scope.user_Type
                                                },
                                                combinedFacility: combinedFacility
                                            };
                                            $http.post('services/user/changenormaluserrole/', data)
                                                .success(function(data, status, headers, config) {
                                                    if (data.status != "invalidRequest") {
                                                        if (data.status == "success") {
                                                            ngDialog
                                                                .open({
                                                                    template: 'Manager Changed Successfully.',
                                                                    plain: true
                                                                });

                                                        }
                                                    }
                                                }).error(function(data, status, headers, config) {
                                                    // log error
                                                });
                                        });
                            } else if (data.status == "success") {
                                ngDialog
                                    .open({
                                        template: 'Role Changed Successfully',
                                        plain: true
                                    });

                                user.userRole = $scope.user_Type;
                            }
                        }
                    }).error(
                        function(data, status, headers, config) {
                            // log error
                        });

            };

            $scope.addUser = function(size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addUserModal.jsp',
                    controller: 'addUserCtrl',
                    size: size,
                    resolve: {
                        officeLocation: function() {
                            return $scope.officeLocation;
                        }
                    }
                });
                modalInstance.result.then(function(result) {
                    var fullName = result.userFName + " " + result.userMName +
                        " " + result.userLName;
                    $scope.administratorData.push({
                        'fullName': fullName,
                        'userName': result.userMName,
                        'mobileNumber': result.mobileNumber,
                        'emailId': result.email,
                        'userRole': result.selectedUserRole.value
                    });
                });
            };

            $scope.assignModulesAccess = function(employee, facilityId) {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    efmFmUserMaster: {
                        userId: employee.userId
                    },
                    userId: profileId,
                    combinedFacility: facilityId.toString()
                };
                $http.post('services/user/allmodules/', data)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.modulesArrayData = data;
                            var modalInstance = $modal
                                .open({
                                    templateUrl: 'partials/modals/assignModulesAccess.jsp',
                                    controller: 'assignModuleAccessCtrl',
                                    resolve: {
                                        modulesArrayData: function() {
                                            return $scope.modulesArrayData
                                        },
                                        employee: function() {
                                            return employee;
                                        }
                                    }
                                });
                            modalInstance.result.then(function(result) {});
                        }
                    }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };

            $scope.setTripType = function() {
                $scope.getRequests();
            };

            $scope.searchRequests = function(searchText) {
                if (searchText) {
                    var dataObj = {
                        branchId: branchId,
                        employeeId: searchText,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/trip/emplyeeMasteRequestSearch/', dataObj)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.requests.length == 0) {
                                    ngDialog
                                        .open({
                                            template: 'This Request does not exist. Please check your Request Id and try again.',
                                            plain: true
                                        });

                                } else {
                                    $scope.requestsData = data.requests;
                                    angular
                                        .forEach(
                                            $scope.posts,
                                            function(item) {
                                                item.checkBoxFlag = false;
                                            });
                                    $scope.progressbar.complete();
                                }
                            }
                        }).error(function(data, status, headers, config) {
                            // log error
                        });
                }
            };

            // Route Name List

            // var data = {
            //     eFmFmClientBranchPO: {
            //         branchId: branchId,
            //         userId: profileId,
            //         userId: profileId,
            //     },
            //     combinedFacility: combinedFacility
            // };
            // $http.post('services/trip/allzone/', data).success(
            //     function(data, status, headers, config) {
            //         if (data.status != "invalidRequest") {

            //         $scope.zonesData = data.zones;
            //         angular.forEach($scope.routeNameData, function(item) {
            //             item.editRouteNameIsClicked = false;
            //         });
            //     }
            //     }).error(function(data, status, headers, config) {
            //     // log error
            // });

            $scope.editShiftTime = function(shift, index) {

                $scope.ceilingNo = shift.ceilingNo;
                $scope.bufferCeilingNo = shift.bufferCeilingNo;


                $scope.bufferTimeArray = [];
                $scope.bufferTimeArray.push(shift.bufferTime);

                $scope.cutOffTimeShift = shift.cutOffTime;
                $scope.CancelCutOffTimeShift = shift.CancelCutOffTime;
                $scope.RescheduleCutOffTimeShift = shift.RescheduleCutOffTime;

                // CutOff Time

                $scope.timeConverstionCutOff = [];
                $scope.timeConverstionCutOff = $scope.cutOffTimeShift
                    .split(':');

                var todaydate = new Date();
                todaydate.setHours($scope.timeConverstionCutOff[0],
                    $scope.timeConverstionCutOff[1]);

                $scope.cutOffTimeShift = todaydate;

                // CancelCutOffTime

                $scope.timeConverstionCancel = [];
                $scope.timeConverstionCancel = $scope.CancelCutOffTimeShift
                    .split(':');
                var todaydate = new Date();
                todaydate.setHours($scope.timeConverstionCancel[0],
                    $scope.timeConverstionCancel[1]);

                $scope.CancelCutOffTimeShift = todaydate;

                // RescheduleCutOffTime

                $scope.timeConverstionReschedule = [];
                $scope.timeConverstionReschedule = $scope.RescheduleCutOffTimeShift
                    .split(':');
                var todaydate = new Date();
                todaydate.setHours($scope.timeConverstionReschedule[0],
                    $scope.timeConverstionReschedule[1]);

                $scope.RescheduleCutOffTimeShift = todaydate;

                $scope.cutOffTimesShifts = [];
                $scope.CancelCutOffTimesShifts = [];
                $scope.RescheduleCutOffTimeShifts = [];

                $scope.cutOffTimesShifts.push($scope.cutOffTimeShift);
                $scope.CancelCutOffTimesShifts
                    .push($scope.CancelCutOffTimeShift);
                $scope.RescheduleCutOffTimeShifts
                    .push($scope.RescheduleCutOffTimeShift);

                shift.editShiftTimeIsClicked = true;



            };

            $scope.updateShiftTime = function(shift, index, cutOffTimesShift,
                CancelCutOffTimesShift, RescheduleCutOffTimeShifts, ceilingNo, bufferCeilingNo) {
                if (branchCode == 'SBOManila' && ceilingNo <= bufferCeilingNo) {

                    ngDialog.open({
                        template: 'Awaited Passenger value should not be more than Ceiling Values',
                        plain: true
                    });
                    return false;

                }


                shift.editShiftTimeIsClicked = false;

                switch (shift.genderPreference) {
                    case 'Male':
                        shift.genderPreference = 'M';
                        break;
                    case 'Female':
                        shift.genderPreference = 'F';
                        break;
                    case 'Both':
                        shift.genderPreference = 'B';
                        break;
                    default:
                }


                switch (shift.mobileVisibleFlg) {
                    case 'Web Console':
                        shift.mobileVisibleFlg = 'W';
                        break;
                    case 'Employee App':
                        shift.mobileVisibleFlg = 'E';
                        break;
                    case 'Nodal Point':
                        shift.mobileVisibleFlg = 'NP';
                        break;
                    case 'Both':
                        shift.mobileVisibleFlg = 'B';
                        break;
                    default:
                }

                if (shift.ceilingFlg == 'N') {
                    $scope.ceilingNo = 'N';
                } else {
                    $scope.ceilingNo = 'Y';
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    shiftId: shift.shiftId,
                    time: shift.shiftTime,
                    cut_Off_Time: convertToTime(cutOffTimesShift),
                    tripType: shift.tripType,
                    mobileVisibleFlg: shift.mobileVisibleFlg,
                    cancel_Cut_Off_Time: convertToTime(CancelCutOffTimesShift),
                    shiftBufferTime: shift.bufferTime,
                    reschedule_Cut_Off_Time: convertToTime(RescheduleCutOffTimeShifts),
                    ceilingNo: ceilingNo,
                    bufferCeilingNo: bufferCeilingNo,
                    ceilingFlg: $scope.ceilingNo,
                    userId: profileId,
                    genderPreference: shift.genderPreference,
                    combinedFacility: combinedFacility
                };

                $http.post('services/user/updateCutOffTime/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template: 'Updated Successfully',
                                plain: true
                            });
                            $scope.getAllShifts();
                        }

                    }).error(function(data, status, headers, config) {
                    // log error

                });


            };


            $scope.canceShiftTime = function(shift, index) {
                shift.editShiftTimeIsClicked = false;
                shift.shiftTime = $scope.shiftTimeData[index].shiftTime;
            };

            $scope.addShiftTime = function(facilityData) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addNewShift.jsp',
                    controller: 'addNewShiftTime',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        allExistingShifts: function() {
                            return $scope.shiftTimeData;
                        },
                        combinedFacilityId: function() {
                            return facilityData;
                        }
                    }
                });
                modalInstance.result
                    .then(function(result) {

                        $scope.shiftTimeData
                            .unshift({
                                shiftTime: convertToTime(result.createNewAdHocTime),
                                tripType: result.tripType,
                                bufferTime: result.bufferTime,
                                CancelCutOffTime: convertToTime(result.CancelCutOffTime),
                                mobileVisibleFlg: 'Web Console',
                                cutOffTime: convertToTime(result.cutOffTime),
                                RescheduleCutOffTime: convertToTime(result.RescheduleCutOffTime),
                                ceilingNo: result.ceilingValues,
                                bufferCeilingNo: result.bufferCeilingNo,
                                ceilingFlg: 'N'
                            });
                    });

            };

            $scope.deleteShiftTimes = function(shift, index) {

                $confirm({
                        text: "Are you sure you want to delete this Shift Time?",
                        title: 'Delete Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {


                            var data = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                time: shift.shiftTime,
                                tripType: shift.tripType,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            };
                            $http.post('services/user/disableShiftTime/', data)
                                .success(function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        $scope.shiftTimeData
                                            .splice(index,
                                                1);
                                        ngDialog
                                            .open({
                                                template: 'Shift time disable successfully',
                                                plain: true
                                            });
                                    }

                                }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });

                        });
            };

            $scope.editRouteName = function(route, index) {
                route.editRouteNameIsClicked = true;
                route.routeNameEdit = route.routeName;
            };

            $scope.updateRouteName = function(route, index) {

                routeNameAlreadyExist = _.findIndex($scope.routeNameData, {
                    routeName: route.routeNameEdit
                });
                if (routeNameAlreadyExist < 0) {

                    route.editRouteNameIsClicked = false;
                    var data = {
                        branchId: branchId,
                        zoneName: route.routeName,
                        zoneId: route.routeId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/zones/updateZoneName/', data)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                                if (data.status == 'success') {
                                    ngDialog
                                        .open({
                                            template: 'Route - ' +
                                                $scope.routeNameData[index].routeName +
                                                ' has been updated successfuly.',
                                            plain: true
                                        });
                                } else if (data.status == 'exist') {
                                    ngDialog.open({
                                        template: 'Route - ' +
                                            route.routeName +
                                            ' already exist.',
                                        plain: true
                                    });
                                }
                            }
                        }).error(
                            function(data, status, headers, config) {});

                } else {
                    ngDialog.open({
                        template: 'Route - ' + route.routeName +
                            ' already exist.',
                        plain: true
                    });
                }
            };

            $scope.editNodalPoint = function(point, index) {

                point.editNodalIsClicked = true;

            };

            $scope.updateNodalPoint = function(point, index) {
                point.editNodalIsClicked = false;
                var data = {
                    branchId: branchId,
                    nodalPointName: point.nodalPointTitle,
                    nodalPointDescription: point.nodalPointDescription,
                    nodalPointId: point.nodalPointId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/zones/updateNodalRoutePointName/', data)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {

                            if (data.status == 'success') {
                                ngDialog
                                    .open({
                                        template: 'Nodal point - ' +
                                            point.nodalPointTitle +
                                            ' has been updated successfuly.',
                                        plain: true
                                    });
                            } else if (data.status == 'exist') {
                                ngDialog.open({
                                    template: 'Route - ' +
                                        point.nodalPointTitle +
                                        ' already exist.',
                                    plain: true
                                });
                            }
                        }
                    }).error(
                        function(data, status, headers, config) {});

            };

            $scope.editNodalToggle = function(nodal, index) {
                nodal.editNodalToggleIsClicked = true;

            };
            //Update nodal route Names
            $scope.updateNodalToggle = function(nodal, index) {
                nodal.editNodalToggleIsClicked = false;
                var data = {
                    branchId: branchId,
                    zoneName: nodal.routeName,
                    zoneId: nodal.routeId,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $http.post('services/zones/updateZoneName/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {

                            if (data.status == 'success') {
                                ngDialog.open({
                                    template: 'Route name has been updated successfuly.',
                                    plain: true
                                });
                            } else if (data.status == 'exist') {
                                ngDialog.open({
                                    template: 'Route name already exist.',
                                    plain: true
                                });
                            }
                        }
                    }).error(function(data, status, headers, config) {});

            };

            $scope.canceRouteName = function(route, index) {
                route.editRouteNameIsClicked = false;
                route.routeName = $scope.routeNameData[index].routeName;
            };

            $scope.addRouteName = function() {
                $scope.addNewRouteIsClicked = true;
                $('.addNewRouteButton').hide('fast');
                $('.addNewRouteDiv').show('slow');
                $('.routeFilter').addClass('marginTop15');

            };

            $scope.saveNewRoute = function(newRoute, combinedFacilityId) {

                var facilityBranchName = [];

                angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                    var branchName = _.where(userFacilities, {
                        branchId: item
                    })[0].name;
                    facilityBranchName.push(branchName);
                });
                if (multiFacility == "true") {
                    $confirm({
                        text: "Are you sure you want to add this Route to" + ' ' + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    }).then(function() {

                        $scope.addNewRouteIsClicked = false;
                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            routeName: newRoute.routeName,
                            userId: profileId,
                            combinedFacility: combinedFacilityId.toString()

                        };
                        $http.post('services/trip/addzone/', data).success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.status == 'success') {
                                        $('.addNewRouteDiv').hide('fast', function() {
                                            $('.addNewRouteButton').show('slow');
                                        });
                                        ngDialog.open({
                                            template: 'Route  has been created successfuly.',
                                            plain: true
                                        });

                                        $scope.routeNameData.unshift({
                                            routeId: data.routeId,
                                            routeName: newRoute.routeName
                                        });
                                    } else {
                                        ngDialog.open({
                                            template: 'Route name already exist.',
                                            plain: true
                                        });

                                    }
                                }
                            }).error(function(data, status, headers, config) {
                            // log error
                        });
                    })
                } else {


                    $scope.addNewRouteIsClicked = false;
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        routeName: newRoute.routeName,
                        userId: profileId,
                        combinedFacility: combinedFacilityId.toString()

                    };
                    $http.post('services/trip/addzone/', data).success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == 'success') {
                                    $('.addNewRouteDiv').hide('fast', function() {
                                        $('.addNewRouteButton').show('slow');
                                    });
                                    ngDialog.open({
                                        template: 'Route  has been created successfuly.',
                                        plain: true
                                    });

                                    $scope.routeNameData.unshift({
                                        routeId: data.routeId,
                                        routeName: newRoute.routeName
                                    });
                                } else {
                                    ngDialog.open({
                                        template: 'Route name already exist.',
                                        plain: true
                                    });

                                }
                            }
                        }).error(function(data, status, headers, config) {
                        // log error
                    });

                }
            };

            $scope.cancelNewRoute = function() {
                $scope.addNewRouteIsClicked = false;
                $('.addNewRouteDiv').hide('fast', function() {
                    $('.addNewRouteButton').show('slow');
                    $('.routeFilter').removeClass('marginTop15');
                });
            };

            $scope.canceAreaName = function(area, index) {
                Area.editAreaNameIsClicked = false;
                area.areaName = $scope.areaNameData[index].areaName;
            };

            $scope.addAreaName = function() {
                $scope.addNewAreaIsClicked = true;
                $('.addNewAreaButton').hide('fast');
                $('.addNewAreaDiv').show('slow');
                $('.areaFilter').addClass('marginTop15');

            };

            $scope.saveNewArea = function(newArea, combinedFacilityId) {

                var facilityBranchName = [];

                angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                    var branchName = _.where(userFacilities, {
                        branchId: item
                    })[0].name;
                    facilityBranchName.push(branchName);
                });

                $confirm({
                    text: "Are you sure you want to add this Area to" + ' ' + facilityBranchName.join(", ") + ' ' + "facility?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {

                    $scope.addNewAreaIsClicked = false;
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        areaName: newArea.areaName,
                        userId: profileId,
                        combinedFacility: combinedFacility

                    };
                    $http.post('services/trip/addzone/', data).success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == 'success') {
                                    $('.addNewAreaDiv').hide('fast', function() {
                                        $('.addNewAreaButton').show('slow');
                                    });
                                    ngDialog.open({
                                        template: 'Area  has been created successfuly.',
                                        plain: true
                                    });
                                    $scope.areaNameData.unshift({
                                        areaId: data.areaId,
                                        areaName: newArea.areaName
                                    });
                                } else {
                                    ngDialog.open({
                                        template: 'Area name already exist.',
                                        plain: true
                                    });
                                }
                            }
                        }).error(function(data, status, headers, config) {
                        // log error
                    });
                })
            };

            $scope.cancelNewArea = function() {
                $scope.addNewAreaIsClicked = false;
                $('.addNewAreaDiv').hide('fast', function() {
                    $('.addNewAreaButton').show('slow');
                    $('.areaFilter').removeClass('marginTop15');
                });
            };

            $scope.editAreaName = function(area, index) {
                area.editAreaNameIsClicked = true;
                area.areaNameEdit = area.areaName;
            };

            $scope.updateAreaName = function(area, index) {

                areaNameAlreadyExist = _.findIndex($scope.allAreaDatas, {
                    areaName: area.areaNameEdit
                });
                if (areaNameAlreadyExist < 0) {
                    $confirm({
                        text: "Are you sure you want to update" + ' ' + area.areaNameEdit + "Area to " + area.areaName + "Area ?",
                        title: 'Update Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    }).then(function() {

                        area.editAreaNameIsClicked = false;
                        var data = {
                            branchId: branchId,
                            zoneName: area.areaName,
                            zoneId: area.areaId,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/zones/updateZoneName/', data)
                            .success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {

                                    if (data.status == 'success') {
                                        ngDialog
                                            .open({
                                                template: 'Area - ' +
                                                    $scope.allAreaDatas[index].areaName +
                                                    ' has been updated successfuly.',
                                                plain: true
                                            });
                                    } else if (data.status == 'exist') {
                                        ngDialog.open({
                                            template: 'Area - ' +
                                                area.areaName +
                                                ' already exist.',
                                            plain: true
                                        });
                                    }
                                }
                            }).error(
                                function(data, status, headers, config) {});
                    });
                } else {
                    ngDialog.open({
                        template: 'Area - ' + area.areaName +
                            ' already exist.',
                        plain: true
                    });
                }
            };

            $scope.createNewNodelPoint = function(size, type, combinedFacilityId) {
                var facilityBranchName = [];

                angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                    var branchName = _.where(userFacilities, {
                        branchId: item
                    })[0].name;
                    facilityBranchName.push(branchName);
                });
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/createNewNodel.jsp',
                    controller: 'addNewNodelPoint',
                    backdrop: 'static',
                    size: size,
                    resolve: {
                        nodal: function() {
                            return null;
                        },
                        type: function() {
                            return type;
                        },
                        facilityBranchName: function() {
                            return facilityBranchName;
                        },
                        officeLocation: function() {
                            return $scope.officeLocation;
                        }
                    }
                });
                modalInstance.result.then(function(result) {
                    $scope.nodelNameData.unshift({
                        routeName: result.routeName,
                        routeId: "2343"
                    });
                });
            };

            $scope.addNewNodalPoint = function(size, nodal, type, combinedFacilityId) {
                var facilityBranchName = [];

                angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                    var branchName = _.where(userFacilities, {
                        branchId: item
                    })[0].name;
                    facilityBranchName.push(branchName);
                });
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/createNewNodel.jsp',
                    controller: 'addNewNodelPoint',
                    backdrop: 'static',
                    size: size,
                    resolve: {
                        nodal: function() {
                            return nodal;
                        },
                        type: function() {
                            return type;
                        },
                        facilityBranchName: function() {
                            return facilityBranchName;
                        },
                        officeLocation: function() {
                            return $scope.officeLocation;
                        }
                    }
                });
                modalInstance.result.then(function(result) {
                    $scope.allNodelPointData.unshift({
                        nodalPointId: "*",
                        nodalPointTitle: result.nodelName,
                        nodalPointDescription: "**Description",
                        nodalPoints: result.cords
                    });
                });
            };
            $scope.deleteNodalPoint = function(point, nodal, index) {
                $confirm({
                    text: "Are you sure you want to delete this Nodal Point?",
                    title: 'Delete Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {
                    $scope.allNodelPointData.splice(index, 1);

                });
            };

            $scope.searchEmployee = function(search, facilityId) {
                $scope.getAdminResult = false;
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    employeeId: search,
                    userId: profileId,
                    combinedFacility: facilityId.toString()
                };
                $http.post('services/employee/empiddetails/', dataObj).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.administratorData = data;
                            $scope.getAdminResult = true;
                            
                            if (userRole == 'superadmin') {
                                angular.forEach($scope.administratorData, function(value, key) {
                                    if (value.userRole == 'superadmin') {
                                        value.accessEnabled = false;
                                    } else {
                                        value.accessEnabled = true;
                                    } 
                                });
                            } else if (userRole == 'admin') {
                                angular.forEach($scope.administratorData, function(value, key) {
                                    if (value.userRole == 'superadmin') {
                                        value.accessEnabled = false;
                                    } else if (value.userRole == 'admin') {
                                        value.accessEnabled = false;
                                    } else {
                                        value.accessEnabled = true;
                                    }
                                });
                            } else {
                                angular.forEach($scope.administratorData, function(value, key) {
                                    value.accessEnabled = false;
                                });
                            }
                            
                        }
                    }).error(function(data, status, headers, config) {});
            };

        }


    };

    var mapMyProfileCtrl = function($scope, $modalInstance, $state, $http,
        ngDialog, officeLocation) {
        $scope.alertMessage;
        $scope.alertHint;
        $scope.officeLocation = officeLocation;

        $scope.user = {
            address: '',
            cords: ''
        };
        $scope.save = function(user) {
            $modalInstance.close(user);
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addUserCtrl = function($scope, $modalInstance, $state, $http, $timeout,
        ngDialog, officeLocation) {
        $scope.alertMessage;
        $scope.alertHint;
        $scope.IntegerNumber = /^\d+$/;
        $scope.regExName = /^[A-Za-z]+$/;
        $scope.NoSpecialCharacters = /^[a-zA-Z0-9]*$/;
        $scope.onMap = false;
        $scope.gender;
        $scope.phyChall;
        $scope.userRole;
        $scope.onUserInfo = true;
        $scope.officeLocation = officeLocation;
        $scope.genders = [{
            'value': 'Male',
            'text': 'Male'
        }, {
            'value': 'Female',
            'text': 'Female'
        }];
        $scope.physicalChalls = [{
            'value': 'Yes',
            'text': 'Yes'
        }, {
            'value': 'No',
            'text': 'No'
        }];
        $scope.userRoles = [{
            'value': 'ADMIN',
            'text': 'ADMIN'
        }, {
            'value': 'SUPERVISOR',
            'text': 'SUPERVISOR'
        }, {
            'value': 'MANAGER',
            'text': 'MANAGER'
        }, {
            'value': 'WEBUSER',
            'text': 'WEB USER'
        }];
        $scope.format = 'dd-MM-yyyy'
        $scope.user = {
            address: '',
            cords: ''
        };

        $scope.openDobCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openeddob': true
                };
            }, 50);
        };

        $scope.setGender = function(selectedgender) {
            $scope.gender = selectedgender.value;
        };

        $scope.setPhysicalChall = function(selectedChall) {
            $scope.phyChall = selectedChall.value;
        };

        $scope.setPickDropLocation = function() {
            $scope.onMap = true;
            $scope.onUserInfo = false;
        };

        $scope.setUserRole = function(selectedUserRole) {
            $scope.userRole = selectedUserRole.value;
        };

        $scope.backtoUserInfo = function() {
            $scope.onMap = false;
            $scope.onUserInfo = true;
        };

        $scope.save = function(user) {
            $modalInstance.close(user);
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };


    };
    var addNewShiftTime = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, allExistingShifts, $confirm, combinedFacilityId) {

        if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
            combinedFacilityId = combinedFacility;
        } else {
            combinedFacilityId = String(combinedFacilityId);
        }

        var facilityBranchName = [];

        angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
            var branchName = _.where(userFacilities, {
                branchId: item
            })[0].name;
            facilityBranchName.push(branchName);
        });

        $scope.alertHint;
        $scope.newShift = {};
        $scope.genderPreferenceFlg = genderPreference;

        $scope.tripTypes = [{
            'value': 'PICKUP',
            'text': 'PICKUP'
        }, {
            'value': 'DROP',
            'text': 'DROP'
        }];
        $scope.newShift.tripType = {
            'value': 'PICKUP',
            'text': 'PICKUP'
        }

        $scope.ceilingTypes = [{
            'value': 'YES',
            'text': 'YES'
        }, {
            'value': 'NO',
            'text': 'NO'
        }];
        $scope.newShift.ceilingType = {
            'value': 'NO',
            'text': 'NO'
        }


        $scope.genderPreference = [{
            'value': 'M',
            'text': 'Male'
        }, {
            'value': 'F',
            'text': 'Female'
        }, {
            'value': 'B',
            'text': 'Both'
        }];
        $scope.newShift.genderPreference = $scope.genderPreference[2];

        $scope.shiftTypesData = [{
            'value': 'nodal',
            'text': 'Nodal Shift'
        }, {
            'value': 'normal',
            'text': 'Normal Shift'
        }, {
            'value': 'exceptional',
            'text': 'Exceptional Shift'
        }];

        $scope.newShift.shiftType = $scope.shiftTypesData[1];


        $scope.bufferTimes = [5, 10, 15, 20, 20, 25, 30];
        $scope.newShift.bufferTime = 5;
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;

        var dataObj = {
            branchId: branchId,
            userId: profileId,
            combinedFacility: combinedFacilityId
        };
        $http.post('services/user/branchdetail/', dataObj).success(
            function(data, status, headers, config) {
                if (data.status != "invalidRequest") {

                    $scope.cutOffTimeFlg = data.cutOffTimeFlg;
                    if ($scope.cutOffTimeFlg == 'S') {
                        $scope.cutOffTimeShow = true;
                    } else {
                        $scope.cutOffTimeShow = false;
                    }
                }
            }).error(function(data, status, headers, config) {
            // log error
        });

        $scope.initializeTime = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            return d;
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

        $scope.newShift.createNewAdHocTime = $scope.initializeTime();
        $scope.newShift.cutOffTime = $scope.initializeTime();
        $scope.newShift.CancelCutOffTime = $scope.initializeTime();
        $scope.newShift.RescheduleCutOffTime = $scope.initializeTime();

        $scope.setceilingType = function(ceilingType) {
            if (ceilingType.value == 'YES') {
                $scope.ceilingValuesFlg = true;
            } else {
                $scope.ceilingValuesFlg = false;
            }
        }


        $scope.saveNewShift = function(newShift) {
            if (multiFacility == "true") {
                $confirm({
                    text: "Are you sure you want to add this shift time to" + ' ' + facilityBranchName.join(", ") + ' ' + "facility?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {

                    if (newShift.ceilingType.value == 'NO') {
                        newShift.ceilingValues = 0;

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            userId: profileId,
                            time: convertToTime(newShift.createNewAdHocTime) + ':00',
                            cut_Off_Time: convertToTime(newShift.cutOffTime) + ':00',
                            tripType: newShift.tripType.text,
                            mobileVisibleFlg: 'W',
                            shiftBufferTime: newShift.bufferTime,
                            cancel_Cut_Off_Time: convertToTime(newShift.CancelCutOffTime) + ':00',
                            reschedule_Cut_Off_Time: convertToTime(newShift.RescheduleCutOffTime) +
                                ':00',
                            ceilingNo: newShift.ceilingValues,
                            ceilingFlg: 'N',
                            genderPreference: newShift.genderPreference.value,
                            combinedFacility: combinedFacilityId

                        };


                        $http.post('services/trip/addshiftime/', data)
                            .success(function(data, status, headers, config) {
                                if (data.status == 'success') {
                                    $('.noMoreClick').addClass('disabled');

                                    newShift.tripType = newShift.tripType.text;



                                    ngDialog
                                        .open({
                                            template: 'Shift has been created successfuly.',
                                            plain: true
                                        });



                                    $timeout(function() {
                                        $modalInstance.close(newShift);
                                        ngDialog.close();
                                    }, 3000);

                                } else if (data.status == 'invalidRequest') {
                                    // ngDialog
                                    //     .open({
                                    //         template: 'Invalid Request.',
                                    //         plain: true
                                    //     });

                                } else {
                                    $(".noMoreClick").removeClass("disabled");
                                    ngDialog
                                        .open({
                                            template: 'This Shift Time for this Trip Type already exist.',
                                            plain: true
                                        });

                                }

                            }).error(function(data, status, headers, config) {
                                // log error
                            });

                    } else {
                        if (newShift.ceilingValues >= newShift.bufferCeilingNo) {
                            $confirm({
                                    text: 'For this shift the maximum number of request limit will be ' + '  ' + newShift.ceilingValues,
                                    title: 'Confirmation',
                                    ok: 'Confirm',
                                    cancel: 'Cancel'
                                })
                                .then(
                                    function() {

                                        var data = {
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            },
                                            time: convertToTime(newShift.createNewAdHocTime) + ':00',
                                            cut_Off_Time: convertToTime(newShift.cutOffTime) + ':00',
                                            tripType: newShift.tripType.text,
                                            mobileVisibleFlg: 'W',
                                            shiftBufferTime: newShift.bufferTime,
                                            cancel_Cut_Off_Time: convertToTime(newShift.CancelCutOffTime),
                                            reschedule_Cut_Off_Time: convertToTime(newShift.RescheduleCutOffTime) +
                                                ':00',
                                            ceilingNo: newShift.ceilingValues,
                                            ceilingFlg: 'Y',
                                            bufferCeilingNo: newShift.bufferCeilingNo,
                                            userId: profileId,
                                            combinedFacility: combinedFacility

                                        };


                                        $http
                                            .post('services/trip/addshiftime/', data)
                                            .success(
                                                function(data, status, headers, config) {
                                                    if (data.status == 'success') {
                                                        $('.noMoreClick').addClass('disabled');
                                                        newShift.tripType = newShift.tripType.text;


                                                        ngDialog
                                                            .open({
                                                                template: 'Shift has been created successfuly.',
                                                                plain: true
                                                            });


                                                        $timeout(function() {
                                                            $modalInstance.close(newShift);
                                                            ngDialog.close();
                                                        }, 3000);

                                                    } else if (data.status == 'invalidRequest') {
                                                        // ngDialog
                                                        //     .open({
                                                        //         template: 'Invalid Request.',
                                                        //         plain: true
                                                        //     });

                                                    } else {
                                                        ngDialog
                                                            .open({
                                                                template: 'This Shift Time  for this Trip Type already exist.',
                                                                plain: true
                                                            });

                                                    }

                                                }).error(function(data, status, headers, config) {
                                                // log error
                                            });

                                    }
                                );
                        } else {
                            ngDialog.open({
                                template: 'Awaited Passenger value should not be more than Ceiling Values',
                                plain: true
                            });
                        }
                    };
                });
            } else {


                if (newShift.ceilingType.value == 'NO') {
                    newShift.ceilingValues = 0;

                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        userId: profileId,
                        time: convertToTime(newShift.createNewAdHocTime) + ':00',
                        cut_Off_Time: convertToTime(newShift.cutOffTime) + ':00',
                        tripType: newShift.tripType.text,
                        mobileVisibleFlg: 'W',
                        shiftBufferTime: newShift.bufferTime,
                        cancel_Cut_Off_Time: convertToTime(newShift.CancelCutOffTime) + ':00',
                        reschedule_Cut_Off_Time: convertToTime(newShift.RescheduleCutOffTime) +
                            ':00',
                        ceilingNo: newShift.ceilingValues,
                        ceilingFlg: 'N',
                        genderPreference: newShift.genderPreference.value,
                        combinedFacility: combinedFacilityId

                    };


                    $http
                        .post('services/trip/addshiftime/', data)
                        .success(
                            function(data, status, headers, config) {
                                if (data.status == 'success') {
                                    $('.noMoreClick').addClass('disabled');

                                    newShift.tripType = newShift.tripType.text;



                                    ngDialog
                                        .open({
                                            template: 'Shift has been created successfuly.',
                                            plain: true
                                        });



                                    $timeout(function() {
                                        $modalInstance.close(newShift);
                                        ngDialog.close();
                                    }, 3000);

                                } else if (data.status == 'invalidRequest') {
                                    // ngDialog
                                    //     .open({
                                    //         template: 'Invalid Request.',
                                    //         plain: true
                                    //     });

                                } else {
                                    $(".noMoreClick").removeClass("disabled");
                                    ngDialog
                                        .open({
                                            template: 'This Shift Time for this Trip Type already exist.',
                                            plain: true
                                        });

                                }

                            }).error(function(data, status, headers, config) {
                            // log error
                        });

                } else {
                    if (newShift.ceilingValues >= newShift.bufferCeilingNo) {
                        $confirm({
                                text: 'For this shift the maximum number of request limit will be ' + '  ' + newShift.ceilingValues,
                                title: 'Confirmation',
                                ok: 'Confirm',
                                cancel: 'Cancel'
                            })
                            .then(
                                function() {

                                    var data = {
                                        eFmFmClientBranchPO: {
                                            branchId: branchId
                                        },
                                        time: convertToTime(newShift.createNewAdHocTime) + ':00',
                                        cut_Off_Time: convertToTime(newShift.cutOffTime) + ':00',
                                        tripType: newShift.tripType.text,
                                        mobileVisibleFlg: 'W',
                                        shiftBufferTime: newShift.bufferTime,
                                        cancel_Cut_Off_Time: convertToTime(newShift.CancelCutOffTime),
                                        reschedule_Cut_Off_Time: convertToTime(newShift.RescheduleCutOffTime) +
                                            ':00',
                                        ceilingNo: newShift.ceilingValues,
                                        ceilingFlg: 'Y',
                                        bufferCeilingNo: newShift.bufferCeilingNo,
                                        userId: profileId,
                                        combinedFacility: combinedFacility

                                    };


                                    $http
                                        .post('services/trip/addshiftime/', data)
                                        .success(
                                            function(data, status, headers, config) {
                                                if (data.status == 'success') {
                                                    $('.noMoreClick').addClass('disabled');
                                                    newShift.tripType = newShift.tripType.text;



                                                    ngDialog
                                                        .open({
                                                            template: 'Shift has been created successfuly.',
                                                            plain: true
                                                        });


                                                    $timeout(function() {
                                                        $modalInstance.close(newShift);
                                                        ngDialog.close();
                                                    }, 3000);

                                                } else if (data.status == 'invalidRequest') {
                                                    // ngDialog
                                                    //     .open({
                                                    //         template: 'Invalid Request.',
                                                    //         plain: true
                                                    //     });

                                                } else {
                                                    ngDialog
                                                        .open({
                                                            template: 'This Shift Time  for this Trip Type already exist.',
                                                            plain: true
                                                        });

                                                }

                                            }).error(function(data, status, headers, config) {
                                            // log error
                                        });

                                }
                            );
                    } else {
                        ngDialog.open({
                            template: 'Awaited Passenger value should not be more than Ceiling Values',
                            plain: true
                        });
                    }
                };

            }

        }



        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addNewNodelPoint = function($scope, $rootScope, $modalInstance, $state, $http, $confirm,
        $timeout, ngDialog, nodal, type, officeLocation, facilityBranchName) {
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
        $scope.user = {};
        $scope.allZonesData = [];
        $scope.areaSelected = false;
        $scope.isCreateNodal = false;
        $scope.isCreateNodalPoint = false;
        $scope.officeLocation = officeLocation;

        if (type == 'CreateNodal') {
            $scope.isCreateNodal = true;
        } else {
            $scope.isCreateNodalPoint = true;
        }

        $scope.user = {
            address: '',
            cords: '',
            search: ''
        };
        $scope.loc = {
            lat: $scope.officeLocation[0],
            lon: $scope.officeLocation[1]
        };
        $scope.mapIsLoaded = true;

        $scope.initializeTime = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);

            return d;
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

        $scope.user.createNewAdHocTime = $scope.initializeTime();

        $scope.getAllAreaNames = function() {

            if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                combinedFacilityId = combinedFacility;
                localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
            } else {
                combinedFacilityId = String($rootScope.combinedFacilityId);
                localStorage.setItem("combinedFacilityIdMyProfile", combinedFacilityId);
            }

            var data = {
                combinedFacility: combinedFacilityId
            };

            $http.post('services/zones/allAreas/', data).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allZonesData = data.areaDetails;
                        $scope.user.areaName = "";
                    }
                }).error(function(data, status, headers, config) {});
        };
        $scope.getAllAreaNames();

        $scope.setAreaSelected = function(areaName) {
            if (angular.isObject(areaName)) {
                $scope.areaSelected = true;
            } else {
                $scope.areaSelected = false;
                $scope.user.nodelName = ""
            }

        };

        if (angular.isObject(nodal)) {
            $scope.user.routeName = nodal.routeName;
        }
        //-----------------------------------
        $scope.geoCode = function(search) {
            if (!this.geocoder)
                this.geocoder = new google.maps.Geocoder();
            this.geocoder.geocode({
                'address': $scope.user.search
            }, function(results, status) {

                if (status == google.maps.GeocoderStatus.OK) {
                    var loc = results[0].geometry.location;
                    $scope.search = results[0].formatted_address;
                    $scope.gotoLocation(loc.lat(), loc.lng());
                    $scope.user.cords = loc;
                    $scope.$apply(function() {
                        $scope.user.cords = loc.lat() + ',' + loc.lng();
                        $scope.user.address = $scope.search;
                    });
                } else {
                    $scope.$apply(function() {
                        $scope.user.cords = '';
                        $scope.user.address = '';
                    });
                    ngDialog.open({
                        template: 'Sorry, this search produced no results.',
                        plain: true
                    });

                }
            });

        };

        $scope.gotoLocation = function(lat, lon) {
            if ($scope.lat != lat || $scope.lon != lon) {
                $scope.loc = {
                    lat: lat,
                    lon: lon
                };
                if (!$scope.$$phase)
                    $scope.$apply("loc");
            }
        };
        //----------------------------------------------

        $scope.addNodelPoint = function(node) {
            if (multiFacility == "true") {
                $confirm({
                        text: "Are you sure you want to add this Nodal Point to" + ' ' + facilityBranchName.join(", ") + ' ' + "facility?",
                        title: 'Confirmation',
                        ok: 'Confirm',
                        cancel: 'Cancel'
                    })
                    .then(
                        function() {
                            var data = {
                                branchId: branchId,
                                userId: profileId,
                                nodalPointsAddress: node.address,
                                nodalPoints: node.cords,
                                nodalPointName: node.nodelName,
                                routeName: node.routeName,
                                areaName: node.areaName.areaName,
                                combinedFacility: combinedFacility
                            };
                            $http
                                .post('services/zones/createNodalRoutes/', data)
                                .success(
                                    function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == 'routeExistNormal') {
                                                ngDialog
                                                    .open({
                                                        template: 'The route name  is a Normal route, please change it to Nodal route.',
                                                        plain: true
                                                    });

                                            } else if (data.status == 'nodalPointExist') {
                                                ngDialog
                                                    .open({
                                                        template: 'Nodal point already exist in the system please change nodal point name.',
                                                        plain: true
                                                    });


                                            } else {
                                                ngDialog
                                                    .open({
                                                        template: 'Nodel point has been created successfuly.',
                                                        plain: true
                                                    });

                                                $timeout(function() {
                                                    $modalInstance.close(node);
                                                    ngDialog.close();
                                                }, 3000);
                                            }
                                        }
                                    }).error(function(data, status, headers, config) {});
                        });
            } else {

                var data = {
                    branchId: branchId,
                    userId: profileId,
                    nodalPointsAddress: node.address,
                    nodalPoints: node.cords,
                    nodalPointName: node.nodelName,
                    routeName: node.routeName,
                    areaName: node.areaName.areaName,
                    combinedFacility: combinedFacility
                };
                $http
                    .post('services/zones/createNodalRoutes/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == 'routeExistNormal') {
                                    ngDialog
                                        .open({
                                            template: 'The route name  is a Normal route, please change it to Nodal route.',
                                            plain: true
                                        });

                                } else if (data.status == 'nodalPointExist') {
                                    ngDialog
                                        .open({
                                            template: 'Nodal point already exist in the system please change nodal point name.',
                                            plain: true
                                        });


                                } else {
                                    ngDialog
                                        .open({
                                            template: 'Nodel point has been created successfuly.',
                                            plain: true
                                        });

                                    $timeout(function() {
                                        $modalInstance.close(node);
                                        ngDialog.close();
                                    }, 3000);
                                }
                            }
                        }).error(function(data, status, headers, config) {});

            }
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var assignModuleAccessCtrl = function($scope, $modalInstance, $state,
        $http, $timeout, $confirm, ngDialog, modulesArrayData, employee) {
        $scope.modulesArrayData = [];
        angular.copy(modulesArrayData, $scope.modulesArrayData);

        if (userRole == 'superadmin') {
            angular.forEach($scope.modulesArrayData, function(item, key) {
                if (item.moduleName == 'Super Admin') {
                    item.moduleName = 'Admin';
                }
            });
        } else {
            angular.forEach($scope.modulesArrayData, function(item, key) {
                if (item.moduleName == 'Super Admin') {
                    item.moduleName = '';
                }
            });
        }

        if (employee.userRole == 'webuser') {
            angular.forEach($scope.modulesArrayData, function(item, key) {
                if (item.moduleName == 'Super Admin') {
                    item.isActive = false;
                }
            });
        }

        $scope.employeeName = employee.employeeName;
        $scope.userRole = employee.userRole;
        $scope.userRoleType = userRole;

        $scope.accessChange = function(module) {
            if (module.moduleName == 'Super Admin') {
                module.moduleName = 'Admin';
            }

            var index = _.findIndex(modulesArrayData, {
                moduleId: module.moduleId
            });


            if (module.isActive) {
                if (module.moduleName == 'Admin') {
                    angular.forEach($scope.modulesArrayData, function(item) {

                        $("#ischangeId" + item.moduleId).removeClass(
                            "applyChangesbutton");
                        $("#ischangeId" + item.moduleId).removeClass(
                            "disableAssignButton");
                        item.isActive = false;
                    });

                    angular.copy(modulesArrayData, $scope.modulesArrayData);
                    if (userRole == 'superadmin') {
                        angular.forEach($scope.modulesArrayData, function(item, key) {

                            if (item.moduleName == 'Super Admin') {
                                item.moduleName = 'Admin';
                            }
                            item.isActive = false;
                        });
                    } else {
                        angular.forEach($scope.modulesArrayData, function(item, key) {
                            if (item.moduleName == 'Super Admin') {
                                item.moduleName = '';
                            }
                            item.isActive = false;
                        });
                    }

                    if (employee.userRole == 'webuser') {
                        angular.forEach($scope.modulesArrayData, function(item, key) {
                            if (item.moduleName == 'Super Admin' || item.moduleName == 'Admin') {
                                item.isActive = false;
                            }
                        });
                    }
                    module.isActive = false;
                } else {
                    if (module.moduleName == "") {

                        angular.forEach($scope.modulesArrayData, function(item) {

                            $("#ischangeId" + item.moduleId).removeClass(
                                "disableAssignButton");
                            item.isActive = false;
                        });
                    } else {
                        $("#ischangeId" + module.moduleId).removeClass(
                            "enableAssignButton");
                        $("#ischangeId" + module.moduleId).addClass(
                            "disableAssignButton");
                        module.isActive = false;
                    }


                }

                module.isActive = false;
            } else if (!module.isActive) {
                module.isActive = true;
                if (module.moduleName == 'Admin' || module.moduleName == '') {
                    angular.forEach($scope.modulesArrayData, function(item) {
                        $("#ischangeId" + item.moduleId).removeClass(
                            "disableAssignButton");
                        $("#ischangeId" + $scope.modulesArrayData[0].moduleId).addClass(
                            "applyChangesbutton");

                        item.isActive = true;
                    });
                } else {
                    $("#ischangeId" + module.moduleId).removeClass(
                        "disableAssignButton");
                    $("#ischangeId" + module.moduleId).addClass(
                        "enableAssignButton");
                    module.isActive = true;
                }

            }


        };

        $scope.applychanges = function(result, demo, index) {

            if (userRole == 'superadmin') {

                if (result.moduleName == 'Admin') {
                    $confirm({
                            text: "Please confirm the type of access you want to give to this user.",
                            title: 'Enable Confirmation',
                            ok: 'Give access to All Modules Only',
                            cancel: 'Give access as an Administrator'
                        })
                        .then(
                            function() {
                                var isAdminAccess = false;
                                var dataObj = {
                                    branchId: branchId,
                                    userId: employee.userId,
                                    clientBranchConfigurationId: result.moduleId,
                                    isActive: result.isActive,
                                    isAdminAccess: isAdminAccess,
                                    profileId: profileId,
                                    combinedFacility: combinedFacility
                                };
                                $http.post('services/user/addOrRemoveModules/', dataObj)
                                    .success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == "noadmin") {
                                                ngDialog
                                                    .open({
                                                        template: 'Sorry can not assign a admin role to this user as per your application setting.',
                                                        plain: true
                                                    });
                                                return false;
                                            }

                                            $(
                                                    "#ischangeId" +
                                                    result.moduleId)
                                                .removeClass(
                                                    "ischangeClass");
                                            $(
                                                    "#ischangeId" +
                                                    result.moduleId)
                                                .addClass(
                                                    "applyChangesbutton");
                                            ngDialog
                                                .open({
                                                    template: 'Changes have been applied successfully.',
                                                    plain: true
                                                });

                                            result.isChange = false;
                                            modulesArrayData[index].isActive = result.isActive;
                                        }
                                    }).error(
                                        function(data, status,
                                            headers, config) {
                                            // log error
                                        });

                            },
                            function() {
                                var isAdminAccess = true;
                                var dataObj = {
                                    branchId: branchId,
                                    userId: employee.userId,
                                    clientBranchConfigurationId: result.moduleId,
                                    isActive: result.isActive,
                                    isAdminAccess: isAdminAccess,
                                    profileId: profileId,
                                    combinedFacility: combinedFacility
                                };
                                $http.post('services/user/addOrRemoveModules/', dataObj)
                                    .success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == "noadmin") {
                                                ngDialog
                                                    .open({
                                                        template: 'Sorry can not assign a admin role to this user as per your application setting.',
                                                        plain: true
                                                    });
                                                return false;
                                            } else {
                                                $(
                                                        "#ischangeId" +
                                                        result.moduleId)
                                                    .removeClass(
                                                        "ischangeClass");
                                                $(
                                                        "#ischangeId" +
                                                        result.moduleId)
                                                    .addClass(
                                                        "applyChangesbutton");
                                                ngDialog
                                                    .open({
                                                        template: 'Changes have been applied successfully.',
                                                        plain: true
                                                    });

                                                result.isChange = false;
                                                modulesArrayData[index].isActive = result.isActive;
                                            }
                                        }
                                    }).error(
                                        function(data, status,
                                            headers, config) {
                                            // log error
                                        });
                            });
                } else {
                    var index = _.findIndex(modulesArrayData, {
                        moduleId: result.moduleId
                    });
                    var dataObj = {
                        branchId: branchId,
                        userId: employee.userId,
                        clientBranchConfigurationId: result.moduleId,
                        isActive: result.isActive,
                        isAdminAccess: false,
                        profileId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/user/addOrRemoveModules/', dataObj)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $("#ischangeId" + result.moduleId)
                                    .removeClass("ischangeClass");
                                $("#ischangeId" + result.moduleId)
                                    .addClass("applyChangesbutton");
                                ngDialog
                                    .open({
                                        template: 'Changes have been applied successfully.',
                                        plain: true
                                    });

                                result.isChange = false;
                                modulesArrayData[index].isActive = result.isActive;
                            }
                        }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                }
            } else if (userRole == 'admin') {
                if (result.moduleName == '') {
                    $confirm({
                            text: "Please confirm the type of access you want to give to this user.",
                            title: 'Enable Confirmation',
                            ok: 'Give access to All Modules Only',
                            cancel: 'Cancel'
                        })
                        .then(
                            function() {
                                var isAdminAccess = false;
                                var dataObj = {
                                    branchId: branchId,
                                    userId: employee.userId,
                                    clientBranchConfigurationId: result.moduleId,
                                    isActive: result.isActive,
                                    isAdminAccess: isAdminAccess,
                                    profileId: profileId,
                                    combinedFacility: combinedFacility
                                };
                                $http.post('services/user/addOrRemoveModules/', dataObj)
                                    .success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            $(
                                                    "#ischangeId" +
                                                    result.moduleId)
                                                .removeClass(
                                                    "ischangeClass");
                                            $(
                                                    "#ischangeId" +
                                                    result.moduleId)
                                                .addClass(
                                                    "applyChangesbutton");
                                            ngDialog
                                                .open({
                                                    template: 'Changes have been applied successfully.',
                                                    plain: true
                                                });

                                            result.isChange = false;
                                            modulesArrayData[index].isActive = result.isActive;
                                        }
                                    }).error(
                                        function(data, status,
                                            headers, config) {
                                            // log error
                                        });

                            },
                            function() {
                                ngDialog
                                    .open({
                                        template: 'No Changes done.',
                                        plain: true
                                    });
                            });
                } else {
                    var index = _.findIndex(modulesArrayData, {
                        moduleId: result.moduleId
                    });
                    var dataObj = {
                        branchId: branchId,
                        userId: employee.userId,
                        clientBranchConfigurationId: result.moduleId,
                        isActive: result.isActive,
                        isAdminAccess: false,
                        profileId: profileId,
                        combinedFacility: combinedFacility

                    };

                    $http.post('services/user/addOrRemoveModules/', dataObj)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $("#ischangeId" + result.moduleId)
                                    .removeClass("ischangeClass");
                                $("#ischangeId" + result.moduleId)
                                    .addClass("applyChangesbutton");
                                ngDialog
                                    .open({
                                        template: 'Changes have been applied successfully.',
                                        plain: true
                                    });

                                result.isChange = false;
                                modulesArrayData[index].isActive = result.isActive;
                            }
                        }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                }
            } else {
                var index = _.findIndex(modulesArrayData, {
                    moduleId: result.moduleId
                });
                var dataObj = {
                    branchId: branchId,
                    userId: employee.userId,
                    clientBranchConfigurationId: result.moduleId,
                    isActive: result.isActive,
                    isAdminAccess: false,
                    profileId: profileId,
                    combinedFacility: combinedFacility

                };

                $http.post('services/user/addOrRemoveModules/', dataObj)
                    .success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $("#ischangeId" + result.moduleId)
                                .removeClass("ischangeClass");
                            $("#ischangeId" + result.moduleId)
                                .addClass("applyChangesbutton");
                            ngDialog
                                .open({
                                    template: 'Changes have been applied successfully.',
                                    plain: true
                                });

                            result.isChange = false;
                            modulesArrayData[index].isActive = result.isActive;
                        }
                    }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            }
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var customMessageTextsCtrl = function($scope, $modalInstance, $state,
        $http, $timeout, $confirm, ngDialog) {
        $scope.isVehicleNumber = false;
        $scope.isVehicleType = false;
        $scope.isTripType = false;
        $scope.isShiftTime = false;

        $scope.save = function(msgLabel, customMsg) {
            $confirm({
                text: "New Custom Message : " + customMsg,
                title: 'Confirmation',
                ok: 'Save Message',
                cancel: 'Cancel'
            }).then(function() {});

        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };

    var ceilingShiftTimeCtrl = function($scope, $modalInstance, $state,
        $http, $timeout, $confirm, ngDialog, $rootScope, shift) {

        $scope.addCeilingValues = function(ceilingValues) {
            switch (shift.mobileVisibleFlg) {
                case 'Web Console':
                    shift.mobileVisibleFlg = 'W';
                    break;
                case 'Employee App':
                    shift.mobileVisibleFlg = 'E';
                    break;
                case 'Nodal Point':
                    shift.mobileVisibleFlg = 'NP';
                    break;
                case 'Both':
                    shift.mobileVisibleFlg = 'B';
                    break;
                default:
            }


            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                shiftId: shift.shiftId,
                time: shift.shiftTime,
                cut_Off_Time: shift.cutOffTime,
                tripType: shift.tripType,
                mobileVisibleFlg: shift.mobileVisibleFlg,
                cancel_Cut_Off_Time: shift.CancelCutOffTime,
                shiftBufferTime: shift.bufferTime,
                reschedule_Cut_Off_Time: shift.RescheduleCutOffTime,
                ceilingNo: Number(ceilingValues),
                ceilingFlg: "Y",
                combinedFacility: combinedFacility
            };

            $http.post('services/user/updateCutOffTime/', data)
                .success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        ngDialog
                            .open({
                                template: 'Ceiling Values added successfully',
                                plain: true
                            });
                        $rootScope.$emit('ceilingValuesData', data);

                        $modalInstance.dismiss('cancel');
                    }
                }).error(
                    function(data, status, headers, config) {
                        // log error
                    });

        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };

    var editMapDestinationCtrl = function($scope, $rootScope, $modalInstance, $state, $timeout, ngDialog, $http, index, location) {
        var latlong_center;
        var latLongValues = location.locationLatLng.split(',');
        latlong_center = latLongValues;
        var latValue =
            $scope.mapIsLoaded = true;
        $scope.loc = {
            lat: latLongValues[0],
            lon: latLongValues[1]
        };


        $scope.user = {};
        $scope.user.address = location.locationAddress;
        $scope.user.areaName = location.areaName;
        $scope.areaId = location.areaId;
        $scope.geoCode = function(search) {
            if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
            this.geocoder.geocode({
                'address': $scope.user.search
            }, function(results, status) {
                $scope.user.areaName = results[0].address_components[0].short_name;
                if (status == google.maps.GeocoderStatus.OK) {
                    var loc = results[0].geometry.location;
                    $scope.search = results[0].formatted_address;
                    $scope.gotoLocation(loc.lat(), loc.lng());
                    $scope.user.cords = loc;
                    $scope.$apply(function() {
                        $scope.user.cords = loc.lat() + ',' + loc.lng();
                        $scope.user.address = $scope.search;
                    });
                } else {
                    $scope.$apply(function() {
                        $scope.user.cords = '';
                        $scope.user.address = '';
                    });
                    ngDialog.open({
                        template: 'Sorry, this search produced no results.',
                        plain: true
                    });

                }
            });
        };

        $scope.updateDestination = function(user, update, loc, index) {
            user.index = $scope.index;
            if (userRole == 'admin') {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'A',
                    multipleLocation: $scope.areaId,
                    locationName: user.areaName,
                    locationAddress: user.address,
                    locationLatLng: user.cords,
                    combinedFacility: combinedFacility
                };
            } else {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'P',
                    multipleLocation: $scope.areaId,
                    locationName: user.areaName,
                    locationAddress: user.address,
                    locationLatLng: user.cords,
                    combinedFacility: combinedFacility
                };
            }

            var dataObj = data;

            $http.post('services/employee/modifyLocation/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "alreadyExist") {
                        ngDialog.open({
                            template: 'Location Name Already Exists',
                            plain: true
                        });
                        return false;
                    } else {
                        ngDialog.open({
                            template: 'Location has been added Successfully',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(dataObj);
                            ngDialog.close();
                        }, 3000);

                    }
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });



        }

        $scope.gotoLocation = function(lat, lon) {
            if ($scope.lat != lat || $scope.lon != lon) {
                $scope.loc = {
                    lat: lat,
                    lon: lon
                };
                if (!$scope.$$phase) $scope.$apply("loc");
            }
        };

        $scope.setPickDropLocation = function(user) {
            $modalInstance.close(user);
        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var createCustomMessagesCtrl = function($scope, $rootScope, $modalInstance, $state, $timeout, ngDialog, $http) {

        $scope.setMessageDetails = function(value) {
            $scope.remainingCharShow = true;
            var messageLength = value.length;
            $scope.remainLength = 200 - messageLength;
        }

        $scope.addMessages = function(value) {
            $scope.data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                custMsgDescription: value,
                combinedFacility: combinedFacility
            };
            $http.post('services/message/createCustomMessage', $scope.data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {

                    $scope.customMessages = data;

                    ngDialog.open({
                        template: 'Message added successfully',
                        plain: true
                    });

                    $timeout(function() {
                        $modalInstance.close($scope.data);
                        ngDialog.close();
                    }, 3000);
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });

        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    var addMobNumCtrl = function($scope, $modalInstance, $timeout, $http, ngDialog) {
        $scope.add = function(result) {
            var postData = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                mobileNumber: result,
                combinedFacility: combinedFacility
            };

            $http.post('services/message/checkEmployeeDetailsByMobileNumber', postData).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {

                    if (data.status) {
                        $timeout(function() {
                            $modalInstance.close(result);
                        }, 1000);
                    } else {
                        ngDialog.open({
                            template: 'Mobile Number Not Exist',
                            plain: true
                        });
                    };
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addEmpIdCtrl = function($scope, $modalInstance, $timeout, $http, ngDialog) {
        $scope.add = function(result) {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                employeeId: result,
                combinedFacility: combinedFacility
            };

            $http.post('services/message/checkEmployeeDetailsByEmployeeID', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {

                    if (data.status) {
                        $timeout(function() {
                            $modalInstance.close(result);
                        }, 1000);
                    } else {
                        ngDialog.open({
                            template: 'Employee Id  Not Exist',
                            plain: true
                        });
                    };
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    var cutOffNoteCtrl = function($scope, $rootScope, $state, $modalInstance, $http, $location, $interval, $modal, $timeout, ngDialog) {
        $scope.cutOffNotifications = [];
        $scope.shiftTimes = [];
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
        $scope.routingTimePicker = function(dropCancelCutOffTime) {


        };
        $scope.routeAdhocTime = ['Yes'];
        $scope.routeCutoffs = [{
            value: "yes",
            name: 'Yes'
        }];
        $scope.requestTypesDataTS = {
            availableOptions: [{
                value: "DROP",
                requestTypes: 'DROP'
            }, {
                value: "PICKUP",
                requestTypes: 'PICKUP'
            }],
            selectedOption: {
                value: "DROP",
                requestTypes: 'DROP'
            }
        };
        $scope.routeTypes = [{
                value: "manual",
                text: 'Manual'
            }, {
                value: "routing",
                text: 'Routing'
            }, {
                value: "automatic",
                text: 'Automatic'
            }],
            $scope.setTripTypeliveTracking = function(tripType) {

                if (tripType) {

                    $scope.shiftTimeEmployee = true;
                    $scope.employeeRosterTable = false;
                    $scope.multiEmployeeRosterTable = false;
                    $scope.summaryLengthShift = 0;

                    var data = {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        eFmFmEmployeeRequestMaster: {
                            efmFmUserMaster: {
                                userId: profileId
                            }
                        },
                        tripType: tripType.value,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/trip/tripshiftime/', data).success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $scope.shiftsTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });


                                } else {

                                }
                            }

                        }).error(function(data, status, headers, config) {});
                }
            };
        $scope.setTripTypeliveTracking({
            value: "DROP",
            requestTypes: "DROP"
        })
        $scope.addRoute = function(shiftime) {

            $scope.cutOffNotificationArray = [];
            $scope.cutOffNotifications = [];
            angular.forEach(shiftime, function(item1, key) {
                $scope.cutOffNotificationArray.indexOf(item1) === -1 ? $scope.cutOffNotificationArray.push(item1) : console.log("This item already exists");
            });
            angular.forEach(shiftime, function(item1, key) {
                angular.forEach($scope.cutOffNotificationArray, function(item2, key) {
                    if (item1.shiftId == item2.shiftId) {
                        $scope.cutOffNotifications.push(item1)
                    } else {}

                });

            });
        }

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
            return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
        };
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


        $scope.saveCutoffNotifications = function(cutOffNotifications) {

            alert(JSON.stringify(cutOffNotifications));

        }
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    }
    var cutOffNoteRouteCtrl = function($scope, $rootScope, $state, $modalInstance, $http, $location, $interval, $modal, $timeout, ngDialog) {
        $scope.cutOffNotifications = [];
        $scope.shiftTimes = [];
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
        $scope.routingTimePicker = function(dropCancelCutOffTime) {


        };
        $scope.routeAdhocTime = ['Yes'];
        $scope.routeCutoffs = [{
            value: "sms",
            text: 'SMS'
        }, {
            value: "notification",
            text: 'Notification'
        }, {
            value: "both",
            text: 'BOTH'
        }];
        $scope.requestTypesDataTS = {
            availableOptions: [{
                value: "DROP",
                requestTypes: 'DROP'
            }, {
                value: "PICKUP",
                requestTypes: 'PICKUP'
            }],
            selectedOption: {
                value: "DROP",
                requestTypes: 'DROP'
            }
        };
        $scope.routeTypes = [{
                value: "autorouting",
                text: '.Auto Routing'
            }, {
                value: "manualrouting",
                text: 'Manual Routing'
            }, {
                value: "rememberrouting",
                text: 'Remember Routing'
            }, {
                value: "autonodalrouting",
                text: 'Auto Nodal Routing'
            }],
            $scope.setTripTypeliveTracking = function(tripType) {

                if (tripType) {

                    $scope.shiftTimeEmployee = true;
                    $scope.employeeRosterTable = false;
                    $scope.multiEmployeeRosterTable = false;
                    $scope.summaryLengthShift = 0;

                    var data = {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        eFmFmEmployeeRequestMaster: {
                            efmFmUserMaster: {
                                userId: profileId
                            }
                        },
                        tripType: tripType.value,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/trip/tripshiftime/', data).success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $scope.shiftsTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });

                                } else {

                                }
                            }

                        }).error(function(data, status, headers, config) {});
                }
            };
        $scope.setTripTypeliveTracking({
            value: "DROP",
            requestTypes: "DROP"
        })
        $scope.addRoute = function(shiftime) {

            $scope.cutOffNotificationArray = [];
            $scope.cutOffNotifications = [];
            angular.forEach(shiftime, function(item1, key) {
                $scope.cutOffNotificationArray.indexOf(item1) === -1 ? $scope.cutOffNotificationArray.push(item1) : console.log("This item already exists");
            });
            angular.forEach(shiftime, function(item1, key) {
                angular.forEach($scope.cutOffNotificationArray, function(item2, key) {
                    if (item1.shiftId == item2.shiftId) {
                        $scope.cutOffNotifications.push(item1)
                    } else {}

                });

            });
        }

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
            return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
        };
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


        $scope.saveCutoffNotifications = function(cutOffNotifications) {

            alert(JSON.stringify(cutOffNotifications));


        }
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    }
    var projectDisableRemarkCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, post) {
        $scope.addRemarks = function(result) {
            $modalInstance.close(result)
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    var nodalPointMapEditCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, officeLocation, point) {
        var latlong_center;
        var officeLocation = officeLocation;
        var employee = {
            "employeeLatiLongi": officeLocation
        };
        latlong_center = officeLocation;
        if (employee.employeeLatiLongi == null) {
            latlong_center = officeLocation;
        } else {
            latlong_center = employee.employeeLatiLongi.split(',');
        }
        $scope.alertMessage;
        $scope.alertHint;
        $scope.mapIsLoaded = true;
        $scope.user = {
            address: '',
            cords: '',
            search: ''
        };
        $scope.loc = {
            lat: latlong_center[0],
            lon: latlong_center[1]
        };

        $scope.geoCode = function(search) {
            if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
            this.geocoder.geocode({
                'address': $scope.user.search
            }, function(results, status) {

                if (status == google.maps.GeocoderStatus.OK) {
                    var loc = results[0].geometry.location;
                    $scope.search = results[0].formatted_address;
                    $scope.gotoLocation(loc.lat(), loc.lng());
                    $scope.user.cords = loc;

                    $scope.$apply(function() {
                        $scope.user.cords = loc.lat() + ',' + loc.lng();
                        $scope.user.address = $scope.search;
                    });
                } else {
                    $scope.$apply(function() {
                        $scope.user.cords = '';
                        $scope.user.address = '';
                    });

                    ngDialog.open({
                        template: 'Sorry, this search produced no results.',
                        plain: true
                    });

                }
            });
        };

        $scope.updateNodalPointMapDetails = function(user, update, loc) {

            var data = {
                branchId: branchId,
                nodalPointName: point.nodalPointTitle,
                nodalPointDescription: point.nodalPointDescription,
                nodalPointId: point.nodalPointId,
                nodalPointFlg: "E",
                nodalPoints: user.cords,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/zones/updateNodalRoutePointName/', data)
                .success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        ngDialog
                            .open({
                                template: 'Nodal Geo Location updated successfuly',
                                plain: true
                            });

                        $timeout(function() {
                            $modalInstance.close(data);
                            ngDialog.close();
                        }, 3000);
                    }


                }).error(
                    function(data, status, headers, config) {});


        }

        $scope.gotoLocation = function(lat, lon) {
            if ($scope.lat != lat || $scope.lon != lon) {
                $scope.loc = {
                    lat: lat,
                    lon: lon
                };
                if (!$scope.$$phase) $scope.$apply("loc");
            }
        };

        $scope.setPickDropLocation = function(user) {
            $modalInstance.close(user);
        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addFacilityWiseCtrl = function($scope, $confirm, $modalInstance, $state, $http, $timeout, ngDialog) {


        $scope.addFacility = function(baseFacility, compineFacility) {
            var baseFacilityId = _.pluck(baseFacility, 'id');
            var compineFacilityId = _.pluck(compineFacility, 'id');

            var data = {
                branchId: branchId,
                userId: profileId,
                baseFacilityId: baseFacilityId,
                compineFacilityId: compineFacilityId
            };
            $http.post('services/employee/addFacilityDetails/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    ngDialog.open({
                        template: 'Facility has been added successfully',
                        plain: true
                    });

                    $timeout(function() {
                        $modalInstance.close();
                        ngDialog.close();
                    }, 3000);
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.deleteCompineFacility = function(data, index) {
            $confirm({
                text: "Are you sure you want to delete this facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {


                var findIndex = _.findIndex($scope.compineFacilityDetails, {
                    name: data.name
                });

                angular.forEach($scope.compineFacilityDetails, function(value, key) {
                    if (key == findIndex) {
                        value.ticked = false;
                    }
                });
            });

        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addEmployeeWiseCtrl = function($scope, $modalInstance, $confirm, $state, $http, $timeout, ngDialog, facilityId) {
        $scope.searchTypes = [{
                text: "Employee Id",
                value: "empId"
            },
            {
                text: "Email Id",
                value: "email"
            },
            {
                text: "Mobile Number ",
                value: "mobile"
            }
        ];

        $scope.compineFacility = [];
        $scope.searchTypeDisabled = true;

        $scope.setSearchType = function(value) {
            $scope.employeeDetails = '';
            if (value == null) {
                $scope.searchTypeDisabled = true;
            } else {
                $scope.searchTypeDisabled = false;
            }
        }
 
        $scope.searchEmployeeDetails = function(employeeId, searchType) {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                employeeId: employeeId,
                searchType: searchType.value,
                combinedFacility: String(facilityId)

            };
            $http.post('services/facility/empSearchByIdMobOrEmail/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {

                    if (data.status == 'notExist') {
                        ngDialog.open({
                            template: searchType.text + ' ' + 'not exists',
                            plain: true
                        });
                        return false;
                    }

                    if (data.status == 'invalidSearch') {
                        ngDialog.open({
                            template: 'Invalid Search',
                            plain: true
                        });
                        return false;
                    }
                    $scope.baseFacility = data.baseFacility;
                    $scope.compineFacilityDetails = data.data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }


        $scope.setCompineFacilityDetails = function(value, index) {

        }

        $scope.addEmployeeId = function(baseFacility, combineFacility, compineFacilityDetails, employeeDetails, searchType) {

            var data = {
                branchId: branchId,
                userId: profileId,
                employeeId: employeeDetails,
                facilityIds: String(_.pluck(combineFacility, 'branchId')),
                searchType: searchType.value
            };

            $http.post('services/facility/updateUserFacilityDetails/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    ngDialog.open({
                        template: 'Facility has been added successfully',
                        plain: true
                    });

                    $timeout(function() {
                        $modalInstance.close();
                        ngDialog.close();
                    }, 3000);
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.deleteCompineFacility = function(data, index) {
            $confirm({
                text: "Are you sure you want to delete this facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {
                var findIndex = _.findIndex($scope.compineFacilityDetails, {
                    name: data.name
                });
                angular.forEach($scope.compineFacilityDetails, function(value, key) {
                    if (key == findIndex) {
                        value.ticked = false;
                    }
                });
            });
        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var editCombinedFacilityDetailsCtrl = function($scope, $modalInstance, $confirm, $state, $http, $timeout, ngDialog, facility, index, facilityRecords, facilityDetailsData) {
        $scope.baseFacility = facility.baseFacilityName;
        $scope.subFacilityDetails = facility.subFacility;
        var fixedSubFacility = facility.subFacilityDetails.split(',');

        angular.forEach($scope.subFacilityDetails, function(value, key) {
            if (value.branchName == $scope.baseFacility) {
                $scope.subFacilityDetails.splice(key, 1);
            }
        });

        // Getting All In Active Facilities

        $scope.getAllInActiveFacilities = function() {
            var data = {
                branchId: branchId,
                userId: profileId,
            };
            $http.post('services/facility/inActiveFacilities/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.allInActiveFacilities = data;

                    angular.forEach($scope.allInActiveFacilities, function(value, key) {
                        value.name = value.branchName;
                        value.ticked = false;
                        $scope.subFacilityDetails.unshift(value);
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.getAllInActiveFacilities();

        $scope.getBaseFacilityDetails = function(value) {
            var baseFacilityBranchName = [];
            angular.forEach(value, function(value, key) {
                baseFacilityBranchName.push(value.branchName);
            });
        }

        $scope.checkCompineFacility = function(value, subFacilityDetails) {
            var branchNameList = [];
            angular.forEach(value, function(data, key) {
                branchNameList.push(data.branchName);
            });

            var removedSubFacilityName = _.difference(fixedSubFacility, branchNameList);

            if (removedSubFacilityName == 'N/A') {
                removedSubFacilityName.length = 0;
            }

            Array.prototype.remByVal = function(val) {
                for (var i = 0; i < this.length; i++) {
                    if (this[i] === val) {
                        this.splice(i, 1);
                        i--;
                    }
                }
                return this;
            }


            angular.forEach($scope.subFacilityDetails, function(data, key) {
                if (data.branchName == removedSubFacilityName[0]) {
                    data.ticked = true;
                }
            });

            if (removedSubFacilityName.length >= 1) {

                $confirm({
                    text: "Are you sure you want to un check this facility?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {

                    angular.forEach($scope.subFacilityDetails, function(data, key) {
                        if (data.branchName == removedSubFacilityName[0]) {
                            $scope.facilityId = data.facilityToFacilityId;
                            $scope.branchId = data.branchId;
                            data.ticked = false;
                        }
                    });

                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        facilityToFacilityId: $scope.facilityId
                    };

                    $http.post('services/facility/disableFacility/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            fixedSubFacility.remByVal(removedSubFacilityName[0]);
                            ngDialog.open({
                                template: 'Facility has been deleted successfully',
                                plain: true
                            });
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });

                }, function() {
                    angular.forEach($scope.subFacilityDetails, function(data, key) {
                        if (data.branchName == removedSubFacilityName[0]) {
                            data.ticked = true;
                        }
                    });
                });

            }

        }


        $scope.modifyFacilityDetails = function(baseFacility, combineFacility) {
            var baseFacilityId = _.where(facilityDetailsData, {
                name: baseFacility
            });
            var data = {
                branchId: baseFacilityId[0].branchId,
                userId: profileId,
                branchName: String(_.pluck(combineFacility, 'name'))
            };
            $http.post('services/facility/updateFacility/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == 'success') {
                        ngDialog.open({
                            template: 'Facility has been added successfully',
                            plain: true
                        });
                        var dataObj = {
                            branchId: baseFacilityId[0].branchId,
                            branchName: String(_.pluck(combineFacility, 'name'))
                        };
                        $timeout(function() {
                            $modalInstance.close(dataObj);
                            ngDialog.close();
                        }, 3000);
                    }

                    if (data.status == 'fail') {
                        ngDialog.open({
                            template: 'Branch Id not mapped correctly please check with eFmFm Team',
                            plain: true
                        });
                    }

                    if (data.status == 'fNotExist') {
                        ngDialog.open({
                            template: 'This Branch is already mapped to the given branch',
                            plain: true
                        });
                    }
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.deleteCompineFacility = function(facilityData, index) {
            $confirm({
                text: "Are you sure you want to delete this facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {

                angular.forEach(fixedSubFacility, function(value, key) {
                    if (value == facilityData.branchName) {
                        var dataObj = {
                            branchId: branchId,
                            userId: profileId,
                            facilityToFacilityId: facilityData.facilityToFacilityId
                        };

                        $http.post('services/facility/disableFacility/', dataObj).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                var findIndex = _.findIndex($scope.subFacilityDetails, {
                                    name: facilityData.name
                                });

                                angular.forEach($scope.subFacilityDetails, function(value, key) {
                                    if (key == findIndex) {
                                        value.ticked = false;
                                    }
                                });

                                ngDialog.open({
                                    template: 'Facility has been deleted successfully',
                                    plain: true
                                });
                            }

                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    } else {
                        var findIndex = _.findIndex($scope.subFacilityDetails, {
                            name: facilityData.name
                        });

                        angular.forEach($scope.subFacilityDetails, function(value, key) {
                            if (key == findIndex) {
                                value.ticked = false;
                            }
                        });
                    }
                });

            });


        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.close("1");

        };


    };

    var addVehicleTypecrtl = function($scope, $modalInstance, $confirm, $state, $http, $timeout, ngDialog, vehicleTypeDetails, facilityData) {

        $scope.vehicles = [];

        $scope.addVehicleRow = function() {

            if ($scope.vehicles.length == 0) {
                $scope.vehicles.push({});
            } else {
                var i = $scope.vehicles.length - 1;
                if ($scope.vehicles[i].selectCapacity && $scope.vehicles[i].vehicleType) {
                    $scope.vehicles.push({});
                } else {
                    ngDialog.open({
                        template: 'Vehicle Capacity and Vehicle Type are mandatory fileds',
                        plain: true
                    });
                }
            }

        };
        $scope.removeVehicleRow = function(vechile, index) {
            if (vechile.vehicleCapacityId) {
                var dataObj = {
                    branchId: branchId,
                    userId: profileId,
                    vehicleCapacityId: vechile.vehicleCapacityId
                };
                $http.post('services/vehicle/removeVehicleType/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == "success") {
                            $scope.vehicles.splice(index, 1);
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            } else {
                $scope.vehicles.splice(index, 1);
            }

        };
        $scope.getVehicleType = function() {
            var dataObj = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: facilityData.toString()
            };
            $http.post('services/vehicle/getVehicleCapacityType/', dataObj).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.vehicleTypeData.length > 0) {
                        $scope.vehicles = data.vehicleTypeData;
                    } else {
                        $scope.vehicles.push({});
                    }
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        }
        $scope.getVehicleType();
        $scope.saveVehicleType = function(vehicle) {
            var dataObj = {
                branchId: branchId,
                userId: profileId,
                vehicleTypeConf: vehicle,
                combinedFacility: facilityData.toString()
            };
            $http.post('services/vehicle/addVehicleTypeConf/', dataObj).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    ngDialog.open({
                        template: 'Vehicel Type has been added successfully',
                        plain: true
                    });
                    $timeout(function() {
                        $modalInstance.close(dataObj);
                        ngDialog.close();
                    }, 2000);
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        }

        $scope.cancel = function() {
            $modalInstance.close();
        };

    };

    angular.module('efmfmApp').controller('myProfileCtrl', myProfileCtrl);
    angular.module('efmfmApp').controller('addUserCtrl', addUserCtrl);
    angular.module('efmfmApp').controller('addNewShiftTime', addNewShiftTime);
    angular.module('efmfmApp').controller('mapMyProfileCtrl', mapMyProfileCtrl);
    angular.module('efmfmApp').controller('addNewNodelPoint', addNewNodelPoint);
    angular.module('efmfmApp').controller('projectDisableRemarkCtrl', projectDisableRemarkCtrl);
    angular.module('efmfmApp').controller('assignModuleAccessCtrl',
        assignModuleAccessCtrl);
    angular.module('efmfmApp').controller('customMessageTextsCtrl',
        customMessageTextsCtrl);
    angular.module('efmfmApp').controller('ceilingShiftTimeCtrl',
        ceilingShiftTimeCtrl);
    angular.module('efmfmApp').controller('editMapDestinationCtrl',
        editMapDestinationCtrl);
    angular.module('efmfmApp').controller('createCustomMessagesCtrl',
        createCustomMessagesCtrl);
    angular.module('efmfmApp').controller('cutOffNoteCtrl',
        cutOffNoteCtrl);
    angular.module('efmfmApp').controller('cutOffNoteRouteCtrl',
        cutOffNoteRouteCtrl);
    angular.module('efmfmApp').controller('nodalPointMapEditCtrl',
        nodalPointMapEditCtrl);
    angular.module('efmfmApp').controller('addFacilityWiseCtrl',
        addFacilityWiseCtrl);
    angular.module('efmfmApp').controller('addEmployeeWiseCtrl',
        addEmployeeWiseCtrl);
    angular.module('efmfmApp').controller('editCombinedFacilityDetailsCtrl',
        editCombinedFacilityDetailsCtrl);
    angular.module('efmfmApp').controller('addMobNumCtrl', addMobNumCtrl);
    angular.module('efmfmApp').controller('addEmpIdCtrl', addEmpIdCtrl);
    angular.module('efmfmApp').controller('addVehicleTypecrtl', addVehicleTypecrtl);

}());