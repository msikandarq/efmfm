/*
@date           04/01/2015
@Author         Saima Aziz
@Description    
@controllers    dashCtrl
@template       partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var dashCtrl = function($scope, $state, $http, $rootScope, $interval) {

        if (!$scope.isDashboardActive || $scope.isDashboardActive == "false") {
            if ($scope.webUserRole || userRole === 'webuser') {
                $state.go('home.requestDetails');
            } else {
                $state.go('home.accessDenied');
            }
        } else {

            $scope.sosList = [];
            $scope.employeesDropList = [];
            $scope.employeesPickupList = [];
            $scope.vehicleStatusList = [];
            $scope.govStatusList = [];
            $scope.govVehicleList = [];
            localStorage.clear();
            $scope.facilityData = [];
            $scope.facilityDetails = userFacilities;
            var array = JSON.parse("[" + combinedFacility + "]");
            $scope.facilityData = array;
            localStorage.setItem("combinedFacilityIdDashboard", $scope.facilityData);
            $scope.userName = localStorage.getItem("userName");

            $("#sosMainDiv").draggable();
            $("#edsMainDiv").draggable();
            $("#epsMainDiv").draggable();
            $("#vehStatMainDiv").draggable();
            $("#govDriverMainDiv").draggable();
            $("#govVehMainDiv").draggable();

            $scope.$on('$viewContentLoaded', function() {

                $('.dashboardMenu').addClass('active');
                $scope.getDashBoardData();
            });

            $scope.getDashBoardData = function() {
                var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");

                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/dashboard/detail/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.dashboardData = data;
                        $scope.sosList = [{
                                'icon': "icon-exclamation-sign",
                                label: 'SOS Alerts',
                                progressBar: $scope.dashboardData.sosAlerts,
                                counter: $scope.dashboardData.sosAlerts
                            },
                            {
                                icon: "icon-warning-sign",
                                label: 'Road Alerts',
                                progressBar: $scope.dashboardData.roadAlerts,
                                counter: $scope.dashboardData.roadAlerts
                            },
                            {
                                icon: "icon-exclamation",
                                label: 'Alerts Open',
                                progressBar: $scope.dashboardData.openAlerts,
                                counter: $scope.dashboardData.openAlerts
                            },
                            {
                                icon: "icon-check-sign",
                                label: 'Alerts closed',
                                progressBar: $scope.dashboardData.closeAlerts,
                                counter: $scope.dashboardData.closeAlerts
                            }
                        ];

                        $scope.employeesDropList = [{
                                'icon': "icon-archive",
                                'label': 'Total Planned Drops',
                                'counter': $scope.dashboardData.numberOfDropRequest
                            },
                            {
                                icon: "icon-check",
                                label: 'Employee Allocated To Cab',
                                counter: $scope.dashboardData.dropScheduled
                            },
                            {
                                icon: "icon-map-marker",
                                label: 'Employee Dropped',
                                counter: $scope.dashboardData.dropedEmployee
                            },
                            {
                                icon: "icon-spinner",
                                label: 'Employee No Show',
                                counter: $scope.dashboardData.noShowDrop
                            }
                        ];

                        $scope.employeesPickupList = [{
                                icon: "icon-plus-sign",
                                label: 'Total Planned Pickups',
                                counter: $scope.dashboardData.numberOfPickUpRequest
                            },
                            {
                                icon: "icon-spinner",
                                label: 'Employee Allocated To Cab',
                                counter: $scope.dashboardData.pickUpScheduled
                            },
                            {
                                icon: "icon-check",
                                label: 'Employee Picked',
                                counter: $scope.dashboardData.boardedEmployee
                            },
                            {
                                icon: "icon-spinner",
                                label: 'Employee No Show',
                                counter: $scope.dashboardData.noShowPickUp
                            },
                        ];



                        $scope.guestReportStatusList = [{
                                icon: "icon-compass",
                                label: 'Total Planned Guest Requests',
                                counter: $scope.dashboardData.numberOfGuestRequest
                            },
                            {
                                icon: "icon-check",
                                label: 'Guest Allocated To Cab',
                                counter: $scope.dashboardData.tripScheduledForGuest
                            },
                            {
                                icon: "icon-signin",
                                label: 'Guest Picked',
                                counter: $scope.dashboardData.boardedGuest
                            },
                            {
                                icon: "icon-spinner",
                                label: 'Guest No Show',
                                counter: $scope.dashboardData.noShowGuests
                            }

                        ];
                        $scope.govStatusList = [{
                                icon: "icon-circle",
                                label: 'License Due For Renewal',
                                counter: $scope.dashboardData.licenseExpire
                            },
                            {
                                icon: "icon-compass",
                                label: 'Medical Fitness Due For Renewal',
                                counter: $scope.dashboardData.medicalFitnessCertificateValid
                            },
                            {
                                icon: "icon-compass",
                                label: 'Police Verification Due For Renewal',
                                counter: $scope.dashboardData.policeVarificationValid
                            },
                            {
                                icon: "icon-compass",
                                label: 'DD Training Due For Renewal',
                                counter: $scope.dashboardData.ddTrainingValid
                            }

                        ];

                        $scope.govVehicleList = [{
                                icon: "icon-circle",
                                label: 'Pollution Due  For Renewal',
                                counter: $scope.dashboardData.polutionExpired
                            },
                            {
                                icon: "icon-compass",
                                label: 'Insurance Due For Renewal',
                                counter: $scope.dashboardData.insuranceExpired
                            },
                            {
                                icon: "icon-compass",
                                label: 'Tax Certificate Due For Renewal',
                                counter: $scope.dashboardData.taxCertificateValid
                            },
                            {
                                icon: "icon-compass",
                                label: 'Permit Due For Renewal',
                                counter: $scope.dashboardData.permitValid
                            },
                            {
                                icon: "icon-compass",
                                label: 'Vehicles Needed Maintenance',
                                counter: $scope.dashboardData.vehicleMaintenance
                            }
                        ];
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.submitFacilityDetails = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdDashboard", combinedFacilityId);
                }
                $scope.getDashBoardData();
            }


            $scope.refreshDashboard = function() {
                $scope.getDashBoardData();
            };

            $scope.exportExcel = function(section) {

                if (section == 'gd') {
                    var sheetArray = [];
                    var dataArray = [];
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/dashboard/driverComplainceExcel/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.licenseDueData = data.licenceExp;
                            $scope.medicalDueData = data.medicalExp;
                            $scope.policeVerificationData = data.policeExp;
                            $scope.DDTData = data.ddtExp;

                            if (data.licenceExp.length > 0) {
                                $scope.licenseDueData = data.licenceExp;
                                sheetArray.push({
                                    sheetid: 'License Due',
                                    header: true
                                });
                                dataArray.push($scope.licenseDueData);
                            }
                            if (data.medicalExp.length > 0) {
                                $scope.medicalDueData = data.medicalExp;
                                sheetArray.push({
                                    sheetid: 'Medical Fitness Due',
                                    header: true
                                });
                                dataArray.push($scope.medicalDueData);
                            }
                            if (data.policeExp.length > 0) {
                                $scope.policeVerificationData = data.policeExp;
                                sheetArray.push({
                                    sheetid: 'Police Verification',
                                    header: true
                                });
                                dataArray.push($scope.policeVerificationData);
                            }
                            if (data.ddtExp.length > 0) {
                                $scope.DDTData = data.ddtExp;
                                sheetArray.push({
                                    sheetid: 'DD Training Due',
                                    header: true
                                });
                                dataArray.push($scope.DDTData);
                            }
                            alasql('SELECT INTO XLSX("Drivers_Governance_30Days_Notice.xlsx",?) FROM ?', [sheetArray, dataArray]);
                        }
                    }).
                    error(function(data, status, headers, config) {});
                }

                if (section == 'gv') {
                    var sheetArray = [];
                    var dataArray = [];
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/dashboard/vehicleComplainceExcel/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.poluttionList.length > 0) {
                                $scope.pollutionDueData = data.poluttionList;
                                sheetArray.push({
                                    sheetid: 'Pollution Due ',
                                    header: true
                                });
                                dataArray.push($scope.pollutionDueData);
                            }
                            if (data.insuranceExp.length > 0) {
                                $scope.InsuranceDueData = data.insuranceExp;
                                sheetArray.push({
                                    sheetid: 'Insurrance Due',
                                    header: true
                                });
                                dataArray.push($scope.InsuranceDueData);
                            }
                            if (data.taxExp.length > 0) {
                                $scope.taxCertData = data.taxExp;
                                sheetArray.push({
                                    sheetid: 'Tax Certification',
                                    header: true
                                });
                                dataArray.push($scope.taxCertData);
                            }
                            if (data.permitExp.length > 0) {
                                $scope.permitDueData = data.permitExp;
                                sheetArray.push({
                                    sheetid: 'Permit Training Due',
                                    header: true
                                });
                                dataArray.push($scope.permitDueData);
                            }
                            if (data.vehicleManintenanceExp.length > 0) {
                                $scope.vehicleNeedMaintData = data.vehicleManintenanceExp;
                                sheetArray.push({
                                    sheetid: 'Vehicle need maintenance',
                                    header: true
                                });
                                dataArray.push($scope.vehicleNeedMaintData);
                            }

                            alasql('SELECT INTO XLSX("Vehicle_Vendor_Governance_30Days.xlsx",?) FROM ?', [sheetArray, dataArray]);
                        }
                    }).
                    error(function(data, status, headers, config) {});
                }
            };


        }
    };
    angular.module('efmfmApp').controller('dashCtrl', dashCtrl);
}());