/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description     
@Main Controllers       vendorDashboardCtrl
@Modal Controllers      addVendorCtrl, newEscortCtrl, newDriverVendorCtrl, newVehicleVendorCtrl, editVendorCtrl, editDriverCtrl, 
                        editVehicleCtrl, editEscortCtrl, uploadDriverCtrl, uploadInspectionDocCtrl, uploadVehicleCtrl, 
                        vendorDashboardCtrl, entityCtrl, releaseDriverCtrl
@template               partials/home.vendorDashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes 
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation 
*/
(function() {
    var vendorDashboardCtrl = function($scope, $rootScope, $modal, $state, $http, $filter, $confirm, $timeout, deviceDetector, ngDialog) {

        if (!$scope.isVendorDashboardActive || $scope.isVendorDashboardActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $scope.vendorLength;
            $scope.currentIndex;
            $scope.checkBoxFlag = false;
            $scope.numberofRecords = 1000000;
            $scope.currentTab;
            $scope.showInspectionTable = false;
            $scope.formInsp = {};
            $scope.formInspOne = {};
            $scope.formInspTwo = {};
            $scope.formInspThree = {};
            $scope.formInspFour = {};
            $scope.allVendors = [];
            $scope.allVehicles = [];
            $scope.allInspectionVendors = [];
            $scope.allInspectionDetailVendors = [];
            $scope.allVehiclesInpectionForm = [];
            $scope.openCustomRange = false;
            $scope.detailIns = {};
            $scope.selectedInsDetailDate;
            $scope.inspectionIdSelected;
            $scope.allContractType = [];
            $scope.allDrivers = [];
            $scope.isAndroidDevice = true;
            $scope.isiOSDevice = true;
            $scope.checkAllShowInspection = true;
            $scope.checkAllButtonEnabled = false;
            localStorage.clear();
            $scope.facilityDetails = userFacilities;
            var array = JSON.parse("[" + combinedFacility + "]");
            $scope.facilityData = array;
            localStorage.setItem("combinedFacilityIdVendorDashboard", $scope.facilityData);
            var combinedFacilityId = localStorage.getItem("combinedFacilityIdVendorDashboard");
            $rootScope.combinedFacilityId = combinedFacilityId;

            $scope.getTabInfo = function(value){
                console.log("value",value);
                localStorage.setItem("vendorDashboardTabDetails", value);
            }


            $scope.getFacilityDetails = function(value) {
                console.log("valuevalue",value);
                $rootScope.combinedFacilityId = value;
                var tabInfo = localStorage.getItem("vendorDashboardTabDetails");
                console.log("tabInfo",tabInfo);

                switch (tabInfo) {
                    case 'vendorManagement':
                        $scope.getContractManag();
                        break;
                    case 'escortManagement':
                        $scope.getEscortDetails();
                        break;
                    case 'escortManagementActive':
                        $scope.getEscortDetails();
                        break;
                    case 'escortManagementInActive':
                        $scope.getEscortDetailsDisable();
                        break;
                    case 'checkInDrivers':
                        $scope.getVehicleCheckedIn();
                        break;
                    case 'availableDriver':
                        $scope.getAvailableVehicle();
                        break;
                    case 'driversOnRoad':
                        $scope.getDriversOnRoad();
                        break;
                    case 'escortCheckIn':
                        $scope.getEscortCheckIn();
                        break;
                    case 'YetToCheckInVehicle':
                        $scope.getYetToCheckInVehicle();
                        break;
                    case 'yetTocheckInDriver':
                        $scope.getYetToCheckInDriver();
                        break;
                    case 'escortAvailable':
                        $scope.getEscortAvailable();
                        break;
                    case 'deviceDetails':
                        $scope.getDeviceDetail();
                        break;
                    default:
                        $scope.getContractManag();
                        break;
                }

                
            }


            $scope.$on('$viewContentLoaded', function() {
                $scope.getContractManag();
                $scope.deviceDetector = deviceDetector;
                $scope.isAndroidDevice = "android";
            });
            $scope.selectDrivers;
            $scope.paginations = [{
                    'value': 10,
                    'text': '10 records'
                },
                {
                    'value': 15,
                    'text': '15 record'
                },
                {
                    'value': 20,
                    'text': '20 records'
                }
            ];

            $scope.selectSeats = [{
                    'value': 1,
                    'text': '2 Seater'
                },
                {
                    'value': 4,
                    'text': '4 Seater'
                },
                {
                    'value': 7,
                    'text': '7 Seater'
                },
                {
                    'value': 9,
                    'text': '9 Seater'
                },
                {
                    'value': 11,
                    'text': '11+ Seater'
                }
            ];

            $scope.dates = [{
                    'name': 'Today',
                    'isClicked': false
                },
                {
                    'name': 'Yesturday',
                    'isClicked': false
                },
                {
                    'name': 'Last 7 Days',
                    'isClicked': false
                },
                {
                    'name': 'Last 30 Days',
                    'isClicked': false
                },
                {
                    'name': 'This Month',
                    'isClicked': false
                },
                {
                    'name': 'Custom Range Date',
                    'isClicked': false
                }
            ];

            $scope.setLimit = function(showRecords) {
                if (!showRecords) {
                    $scope.numberofRecords = $scope.vendorContractManagData.length;
                } else $scope.numberofRecords = showRecords.value;
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

            // Converstion DD-MM-YYYY to YYYY-MM-DD

            var convertDate = function(dateString) {
                var p = dateString.split(/\D/g)
                return [p[2], p[1], p[0]].join("-")
            }


            //FUNCTION : Get aLL aCTIVE vENDORS to Populate the drop Down
            $scope.getAllVendors = function() {

                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.allVendors = [];
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/contract/allActiveVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        angular.forEach(data, function(item) {
                            $scope.allVendors.push({
                                'name': item.name,
                                'Id': item.vendorId
                            });
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
                return $scope.allVendors;
            };
            $scope.getAllVendors();

            $scope.getAllVehicle = function(vendor) {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }
                $scope.allVehicles = [];
                var data = {
                    vendorId: vendor.Id,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/contract/allActiveVehicle/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        angular.forEach(data, function(item) {
                            $scope.allVehicles.push({
                                vehicleNumber: item.vehicleNumber,
                                Id: item.vehicleId
                            });
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
                return $scope.allVehicles;
            };

            $scope.getYetToCheckInVehicle = function() {

                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/vehicle/listOfYetToCheckedInVehicles/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.checkInvehicleDataVehicle = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.getYetToCheckInDriver = function() {

                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/vehicle/listOfYetToCheckedInVehicles/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.checkInvehicleDataDriver = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

            // Get Supervisor Details

            $rootScope.getSupervisorDetails = function(post, value) {

                if (value == 'active') {
                    var statusFlg = 'A';

                } else {
                    var statusFlg = 'N';
                }

                var data = {
                    efmFmVendorMaster: {
                        "vendorId": post.vendorId,
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    isActive: statusFlg,
                    combinedFacility: combinedFacility
                };

                $http.post('services/fieldApp/getAllSupDetailsByVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $rootScope.getSupervisorData = data;
                        angular.forEach($rootScope.getSupervisorData, function(item) {
                            item.isClicked = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.viewSupervisorDetail = function(post, index) {

                if (!post.isClicked) {
                    angular.forEach($rootScope.getSupervisorData, function(item) {
                        item.isClicked = false;
                    });
                    post.isClicked = true;
                    $scope.currentIndex = index;

                    $scope.supervisorDetailIndex = post;

                } else post.isClicked = false;

            }

            /*Edit Supervisor*/


            $scope.editSupervisorDetail = function(post, index, size, tabValue) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/editSupervisorForm.jsp',
                    controller: 'editSupervisorCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    size: size,
                    resolve: {
                        supervisor: function() {
                            return post;
                        },
                        tabValue: function() {
                            return tabValue;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    var supervisorIndex;
                    var supervisorIndex = _.findIndex($scope.getSupervisorData, {
                        supervisorId: result.supervisorId
                    });
                    if (supervisorIndex >= 0) {
                        $scope.getSupervisorData[supervisorIndex] = {
                            'supervisorName': result.firstName,
                            'emailId': result.email,
                            'supervisorlastName': result.lastName,
                            'supervisorId': result.supervisorId,
                            'supervisorEmployeeId': result.supervisorEmployeeId,
                            'supervisorMobileNumber': result.mobileNumber,
                            'supervisorFatherName': result.fatherName,
                            'supervisorGender': result.gender,
                            'supervisordob': result.supervisordob,
                            'supervisorAddress': result.permanentAddress,
                            'presentAddress': result.temporaryAddresss,
                            'designation': result.designation,
                            'vendorName': result.supervisorVendorName,
                            'physicallyChallenged': result.physicallyChallenged
                        };
                    }

                });
            }


            //This Function is called on the Click of the ITEMS in the Date Popover
            $scope.updateDate = function(type) {
                switch (type) {
                    case 'today':
                        $scope.openCustomRange = false;
                        $scope.getReport(new Date(), new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[0].isClicked = true;
                        break;
                    case 'yesturday':
                        $scope.openCustomRange = false;
                        var yesturdayDate = new Date();
                        yesturdayDate.setDate(yesturdayDate.getDate() - 1);
                        $scope.getReport(yesturdayDate, yesturdayDate);
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[1].isClicked = true;
                        break;
                    case 'last7':
                        $scope.openCustomRange = false;
                        var last7 = new Date();
                        last7.setDate(last7.getDate() - 7);
                        $scope.getReport(last7, new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[2].isClicked = true;
                        break;
                    case 'last30':
                        $scope.openCustomRange = false;
                        var last30 = new Date();
                        last30.setDate(last30.getDate() - 30);
                        $scope.getReport(last30, new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[3].isClicked = true;
                        break;
                    case 'thisMonth':
                        $scope.openCustomRange = false;
                        var thisMonth = new Date();
                        thisMonth.setMonth(thisMonth.getMonth(), 1);
                        $scope.dates[4].value = thisMonth;
                        $scope.getReport(thisMonth, new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[4].isClicked = true;
                        break;
                    case 'customDate':
                        if ($scope.openCustomRange) {
                            $scope.openCustomRange = false;
                            $scope.from = '';
                            $scope.to = '';
                        } else {
                            $scope.openCustomRange = true;
                        }
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        break;
                }
            };

            //Function to check the Date Validity
            $scope.checkDateRangeValidity = function(fromDate, toDate) {
                if (fromDate > toDate) {
                    $scope.dateRangeError = true;
                    $timeout(function() {
                        $scope.dateRangeError = false;
                    }, 3000);
                    return false;
                } else return true;
            };

            //Date Picker for the 'From Date'
            $scope.fromDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'fromDate': true
                    };
                }, 50);
            };

            //Date Picker for the 'To Date'
            $scope.toDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'toDate': true
                    };
                }, 50);
            };

            //Get all allDrivers of selected vendor
            $scope.getAllDrivers = function(vendorId) {

                $scope.allDrivers = [];

                var data = {
                    branchId: branchId,
                    vendorId: vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };

                $http.post('services/contract/getCheckInEntity/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allDrivers = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };


            // ******VENDOR DASHBOARD TAB FUNCTIONS - called when the user click any tab*************** 

            $scope.getContractManag = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId,
                        userId: profileId
                    },
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/vendor/vendorByVehicleDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vendorContractManagData = data;
                        $scope.vendorLength = $scope.vendorContractManagData.length;
                        angular.forEach($scope.vendorContractManagData, function(item) {
                            item.driver_isClicked = false;
                            item.vehicle_isClicked = false;
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };


            $scope.getVehicleCheckedIn = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/vehicle/listOfCheckedInVehicles/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vehicleCheckInData = data;
                        $scope.vehicleCheckInDataLength = data.length;

                        angular.forEach($scope.vehicleCheckInData, function(item) {
                            item.checkBoxFlag = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getAvailableVehicle = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/vehicle/listOfAvailableVehicles/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.availableVehicleData = data;
                        $scope.availableVehicleDataLength = data.length;
                        $scope.availableData = [];
                        angular.forEach(data, function(value, key) {
                            $scope.availableData.push(value.deviceId);
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getEscortDetails = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/escort/listOfEscort/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.escortDetailData = data;
                        angular.forEach($scope.escortDetailData, function(item) {
                            item.isClicked = false;
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getEscortDetailsDisable = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/escort/disableEscortallDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.escortDetailDataDisable = data;
                        angular.forEach($scope.escortDetailDataDisable, function(item) {
                            item.isClicked = false;
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getEscortCheckIn = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/escort/escortCheckInDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.escortCheckInData = data;
                        $scope.escortCheckInDataLength = data.length;
                        angular.forEach($scope.availableVehicleData, function(item) {
                            item.checkBoxFlag = false;
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getEscortAvailable = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/escort/availableEscortDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.escortAvailableData = data;
                        $scope.escortAvailableDataLength = data.length;

                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getDeviceDetail = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId

                };
                $http.post('services/device/alldevices/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.deviceDetailData = data;
                        $scope.deviceDetailDataLength = data.length;
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getDriversOnRoad = function() {
                if ($rootScope.combinedFacilityId == undefined || $rootScope.combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                } else {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdVendorDashboard", combinedFacilityId);
                }

                $scope.progressbar.start();
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/vehicle/vehicleonroad/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.driversOnRoadData = data;
                        $scope.driversOnRoadDataLength = data.length;
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.setLimit = function(showRecords) {
                if (!showRecords) {
                    $scope.numberofRecords = $scope.vendorLength;
                } else $scope.numberofRecords = showRecords.value;
            };

            $scope.getIndex = function() {
                return $scope.currentIndex;
            };

            //************************************ADD VENDORS *******************************************
            $scope.addVendors = function(size, selectedFacilityId) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/newVendorForm.jsp',
                    controller: 'addVendorCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        selectedFacilityId: function() {
                            return selectedFacilityId;
                        }
                    }
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {

                    $scope.vendorContractManagData.push({
                        vendorName: result.vendorName,
                        vendorMobileNumber: result.vendorMobileNumber,
                        noOfDriver: 0,
                        noOfVehicle: 0,
                        emailId: result.email,
                        emailIdLvl1: result.emailIdLvl1,
                        emailIdLvl2: result.emailIdLvl2,
                        panNumber: result.panNumber,
                        vendorAddress: result.address,
                        vendorContactName: result.vendorContactName,
                        vendorContactNumber: result.vendorContactNumber,
                        vendorContactName2: result.vendorContactName2,
                        vendorContactNumber2: result.vendorContactNumber2,
                        vendorContactName3: result.vendorContactName3,
                        vendorContactNumber3: result.vendorContactNumber3,
                        vendorContactName4: result.vendorContactName4,
                        vendorContactNumber4: result.vendorContactNumber4,
                        serviceTaxNumber: result.serviceTaxNumber

                    });

                });
            };

            $scope.addAdhocVendor = function(size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/addAdhocDriverDetails.jsp',
                    controller: 'addAdhocVendorCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {}
                });

                modalInstance.result.then(function(result) {});

            }

            $scope.addAdhocDriverModal = function(size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/addDriver.jsp',
                    controller: 'addAdhocDriverCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {}
                });

                modalInstance.result.then(function(result) {});

            }

            //*********************************EDIT VENDORS*****************************************
            $scope.editVendor = function(post, index, size, selectedFacilityId) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/editVendorForm.jsp',
                    controller: 'editVendorCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        vendor: function() {
                            return post;
                        },
                        selectedFacilityId: function() {
                            return selectedFacilityId;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    var vendorIndex;
                    var vendorIndex = _.findIndex($scope.vendorContractManagData, {
                        vendorId: result.vendorId
                    });
                    if (vendorIndex >= 0) {

                        $scope.vendorContractManagData[vendorIndex] = {
                            'vendorName': result.vendorName,
                            'vendorMobileNumber': result.vendorMobileNumber,
                            'vendorContactNumber': result.vendorContactNumber,
                            'vendorContactName': result.vendorContactName,
                            'vendorAddress': result.address,
                            'tinNumber': result.tinNumber,
                            'emailId': result.email,
                            'vendorContactName1': result.vendorContactName1,
                            'vendorContactNumber1': result.vendorContactNumber1,
                            'vendorContactName2': result.vendorContactName2,
                            'vendorContactNumber2': result.vendorContactNumber2,
                            'vendorContactName3': result.vendorContactName3,
                            'vendorContactNumber3': result.vendorContactNumber3,
                            'vendorContactName4': result.vendorContactName4,
                            'vendorContactNumber4': result.vendorContactNumber4,
                            'emailIdLvl1': result.emailIdLvl1,
                            'emailIdLvl2': result.emailIdLvl2,
                            'panNumber': result.panNumber,
                            'serviceTaxNumber': result.serviceTaxNumber
                        };
                    }
                });
            }

            //*******************************************************VENDOR :: DRIVER ****************************************************************************************  

            //*****************************************VIEW DRIVERS************************************************
            $scope.viewDrivers = function(post, index) {
                $scope.progressbar.start();
                $scope.currentIndex = index;
                $('#vehicle' + a).hide();
                var a = post.vendorId;
                var data = {
                    efmFmVendorMaster: {
                        vendorId: post.vendorId
                    },
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/vendor/listOfDriverByVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vendorContractManagData[$scope.getIndex()].driverData = data;
                        angular.forEach($scope.vendorContractManagData[$scope.getIndex()].driverData, function(item) {
                            item.editDriver_Enable = false;
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

                angular.forEach($scope.vendorContractManagData[index].driverData, function(item) {
                    item.editDriver_Enable = false;

                });

                if (!post.driver_isClicked) {
                    angular.forEach($scope.vendorContractManagData, function(item) {
                        item.driver_isClicked = false;
                        item.vehicle_isClicked = false;
                        item.supervisor_isClicked = false;
                    });
                    post.driver_isClicked = true;
                    $('#vehicle' + a).hide();
                    $('#supervisor' + a).hide();
                    $('#driver' + a).show('slow');
                } else {
                    post.driver_isClicked = false;
                    $('#vehicle' + a).hide();
                    $('#supervisor' + a).hide();
                    $('#driver' + a).hide();
                }
            };

            //*********************************************ADD NEW DRIVER***************************************************
            $scope.addNewDriver = function(parentIndex, size) {
                $scope.currentIndex = parentIndex;

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/newDriverForm.jsp',
                    controller: 'newDriverVendorCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        masterVendor: function() {
                            return $scope.vendorContractManagData[$scope.getIndex()].vendorName;
                        }
                    }
                });

                modalInstance.result.then(function(result) { //when we close the model need to append the vale from ui to here directly.

                    $scope.vendorContractManagData[$scope.getIndex()].noOfDriver = $scope.vendorContractManagData[$scope.getIndex()].noOfDriver + 1;
                    $scope.vendorContractManagData[$scope.getIndex()].driverData.push({
                        'driverId': result.driverId,
                        'driverName': result.driverName,
                        'mobileNumber': result.mobileNumber,
                        'dateOfBirth': convertDateUTC(result.driverdob),
                        'licenceValid': convertDateUTC(result.licenceValid),
                        'ddtExpiryDate': convertDateUTC(result.driverDDT),
                        'batchDate': convertDateUTC(result.driverBatch),
                        'driverBatchNum': result.driverBatchNum,
                        'policeExpiryDate': convertDateUTC(result.driverPoliceVerification),
                        'medicalCertificateValid': convertDateUTC(result.driverMedicalExpiry),
                        'joinDate': convertDateUTC(result.driverJoining),
                        'batchValidity': convertDateUTC(result.badgeValidity),
                        'licenceNumber': result.licenceNumber,
                        'driverAddress': result.driverAddress,
                        'permanentAddress': result.permanentAddress
                    });
                });
            };
            //***********************************************EDIT DRIVERS************************************************
            $scope.editDriver = function(post, parentIndex, index, size) {

                $scope.currentIndex = parentIndex;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/editDriverForm.jsp',
                    controller: 'editDriverCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        driver: function() {
                            return post;
                        }
                    }
                });


                modalInstance.result.then(function(result) {
                    var driverIndex;
                    var driverIndex = _.findIndex($scope.vendorContractManagData[$scope.getIndex()].driverData, {
                        driverId: result.driverId
                    });
                    if (driverIndex >= 0) {
                        $scope.vendorContractManagData[$scope.getIndex()].driverData[driverIndex] = {
                            'driverId': result.driverId,
                            'driverName': result.driverName,
                            'driverAddress': result.driverAddress,
                            'licenceNumber': result.licenceNumber,
                            'medicalCertificateValid': result.driverMedicalExpiry,
                            'licenceValid': result.licenceValid,
                            'mobileNumber': result.mobileNumber,
                            'ddtExpiryDate': result.driverDDT,
                            'dateOfBirth': result.driverdob,
                            'policeExpiryDate': result.driverPoliceVerification,
                            'joinDate': result.driverJoining,
                            'batchDate': result.driverBatch,
                            'driverBatchNum': result.driverBatchNum,
                            'batchValidity': result.badgeValidity,
                            'permanentAddress': result.permanentAddress
                        };
                    }
                });
            };
            //***********************************************DELETE DRIVERS************************************************************
            $scope.deleteDriver = function(post, parentIndex, index) {
                var data = {
                    driverId: post.driverId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to delete this Driver?',
                        title: 'Delete Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/vendor/removeDriverDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data == 'Success') {
                                    $scope.vendorContractManagData[$scope.getIndex()].driverData.splice(index, 1);
                                    $scope.vendorContractManagData[$scope.getIndex()].noOfDriver = $scope.vendorContractManagData[$scope.getIndex()].noOfDriver - 1;
                                    ngDialog.open({
                                        template: 'Driver has been deleted successfully.',
                                        plain: true
                                    });

                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });


            };

            //**********************************************UPLOAD DRIVERS DOCUMENTS*****************************************************
            $scope.uploadDriver = function(post, parentIndex, index) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/uploadDriver.jsp',
                    controller: 'uploadDriverCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        driver: function() {
                            return post;
                        }
                    }
                });
                modalInstance.result.then(function(result) {});
            };

            //*****************************************************************VENDOR :: VEHICLE ********************************************************************* 

            //***********************************************VIEW VEHICLES***********************************************
            $scope.viewVehicles = function(post, index) {
                $scope.progressbar.start();
                $('#driver' + a).hide();
                var a = post.vendorId;
                $scope.currentIndex = index;
                var data = {
                    efmFmVendorMaster: {
                        vendorId: post.vendorId
                    },
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/vendor/listOfVehiclebyVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allContractType = data.contractDetails;
                        $scope.vendorContractManagData[$scope.getIndex()].vehicleData = data.vehicleDetails;
                        angular.forEach($scope.vendorContractManagData[$scope.getIndex()].vehicleData, function(item) {
                            item.editVehicle_Enable = false;
                        });
                        $scope.progressbar.complete();
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

                if (!post.vehicle_isClicked) {
                    angular.forEach($scope.vendorContractManagData, function(item) {
                        item.driver_isClicked = false;
                        item.vehicle_isClicked = false;
                        item.supervisor_isClicked = false;
                    });
                    post.vehicle_isClicked = true;
                    $('#driver' + a).hide();
                    $('#supervisor' + a).hide();
                    $('#vehicle' + a).show('slow');
                } else {
                    post.vehicle_isClicked = false;
                    $('#driver' + a).hide();
                    $('#supervisor' + a).hide();
                    $('#vehicle' + a).hide('slow');
                }
            };

            $scope.viewSupervisor = function(post, index) {

                //$scope.progressbar.start();
                $('#driver' + a).hide();
                $('#vehicle' + a).hide();
                var a = post.vendorId;

                var data = {
                    efmFmVendorMaster: {
                        "vendorId": post.vendorId,
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    isActive: 'A',
                    combinedFacility: combinedFacility
                };

                $http.post('services/fieldApp/getAllSupDetailsByVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $rootScope.getSupervisorData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });


                if (!post.supervisor_isClicked) {
                    angular.forEach($scope.vendorContractManagData, function(item) {
                        item.driver_isClicked = false;
                        item.vehicle_isClicked = false;
                        item.supervisor_isClicked = false;
                    });
                    post.supervisor_isClicked = true;
                    $('#driver' + a).hide();
                    $('#vehicle' + a).hide();
                    $('#supervisor' + a).show('slow');
                } else {
                    post.supervisor_isClicked = false;
                    $('#driver' + a).hide();
                    $('#vehicle' + a).hide();
                    $('#supervisor' + a).hide('slow');
                }
            }


            //***********************************************ADD NEW VEHICLE***********************************************
            $scope.addNewVehicle = function(index, size) {
                $scope.currentIndex = index;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/newVehicleForm.jsp',
                    controller: 'newVehicleVendorCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        masterVendor: function() {
                            return $scope.vendorContractManagData[$scope.getIndex()].vendorName;
                        },
                        contractTypeList: function() {
                            return $scope.allContractType;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    $scope.vendorContractManagData[$scope.getIndex()].noOfVehicle = $scope.vendorContractManagData[$scope.getIndex()].noOfVehicle + 1;

                    $scope.vendorContractManagData[$scope.getIndex()].vehicleData.push({
                        'vehicleId': result.vehicleId,
                        'vehicleMake': result.vehicleName,
                        'vehicleModel': result.vehicleModel,
                        'vehicleModelYear': result.modelYear,
                        'vehicleOwnerName': $scope.vendorContractManagData[$scope.getIndex()].vendorName,
                        'vehicleNumber': result.vehicleNumber,
                        'vehicleEngineNumber': result.contactNo,
                        'capacity': result.capacity,
                        'conType': result.conType.contractName,
                        'rcNumber': result.regCert,
                        'registrationDate': convertDateUTC(result.registrationDate),
                        'polutionValid': convertDateUTC(result.pollutionExpDate),
                        'InsuranceDate': convertDateUTC(result.InsuranceDate),
                        'vehicleFitnessDate': convertDateUTC(result.vehicleFitnessCert),
                        'PermitValid': convertDateUTC(result.PermitValid),
                        'taxCertificateValid': convertDateUTC(result.TaxValid),
                        'isReplacement': 'NO'
                    });

                });
            };

            //***********************************************EDIT VEHICLE***********************************************
            $scope.editVehicle = function(post, parentIndex, index, size) {
                $scope.currentIndex = parentIndex;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/editVehicleForm.jsp',
                    controller: 'editVehicleCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        vehicle: function() {
                            return post;
                        },
                        contractTypeList: function() {
                            return $scope.allContractType;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    var vehicleIndex;
                    var vehicleIndex = _.findIndex($scope.vendorContractManagData[$scope.getIndex()].vehicleData, {
                        vehicleId: result.vehicleId
                    });
                    if (vehicleIndex >= 0) {
                        $scope.vendorContractManagData[$scope.getIndex()].vehicleData[vehicleIndex] = {
                            'vehicleId': result.vehicleId,
                            'vehicleMake': result.vehicleName,
                            'vehicleModel': result.vehicleModel,
                            'vehicleModelYear': result.modelYear,
                            'vehicleOwnerName': $scope.vendorContractManagData[$scope.getIndex()].vendorName,
                            'vehicleNumber': result.vehicleNumber,
                            'vehicleEngineNumber': result.contactNo,
                            'capacity': result.capacity,
                            'conType': {
                                contractName: result.conType.contractName,
                                contractId: result.conType.contractId
                            },
                            'rcNumber': result.regCert,
                            "documents": result.documents,
                            'registrationDate': result.registrationDate,
                            'polutionValid': result.pollutionExpDate,
                            'InsuranceDate': result.InsuranceDate,
                            'vehicleFitnessDate': result.vehicleFitnessCert,
                            'PermitValid': result.PermitValid,
                            'taxCertificateValid': result.TaxValid,
                            'isReplacement': result.isReplacement,
                            'mileage': result.mileage,
                            'vehicleMaintenanceDate': convertDateUTC(result.vehicleMaintenanceDate)
                        };

                    }
                });
            };

            //***********************************************DELETE VEHICLES***********************************************
            $scope.deleteVehicle = function(post, parentIndex, index) {
                var data = {
                    vehicleId: post.vehicleId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to delete this Vehicle?',
                        title: 'Delete Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/vehicle/removeVehicleDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vendorContractManagData[$scope.getIndex()].vehicleData.splice(index, 1);
                                $scope.vendorContractManagData[$scope.getIndex()].noOfVehicle = $scope.vendorContractManagData[$scope.getIndex()].noOfVehicle - 1;
                                ngDialog.open({
                                    template: 'Vehicle deleted successfully.',
                                    plain: true
                                });

                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error    
                        });
                    });

            };

            //***********************************************UPLOAD VEHICLE DOCUMENTS****************************************
            $scope.uploadVehicle = function(post, parentIndex, index) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/uploadVehicle.jsp',
                    controller: 'uploadVehicleCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        vehicle: function() {
                            return post;
                        }
                    }
                });

                modalInstance.result.then(function(result) {});
            };

            //************************************************CHECK IN / CHECKOUT VEHICLE TAB********************************************************
            $scope.select_vehicleCheckIn = function(post) {
                if (!post.checkBoxFlag) {
                    post.checkBoxFlag = true;
                    $('.vehicle' + post.vehicleId).css('background-color', 'rgba(210, 230, 239, 0.5)');
                } else {

                    $('.vehicle' + post.vehicleId).css('background-color', 'white');
                    post.checkBoxFlag = false;
                }
            };

            $scope.submitVehicleCheckIn = function() {
                var checkedVehicle = [];
                angular.forEach($scope.vehicleCheckInData, function(item) {
                    if (item.checkBoxFlag) {
                        checkedVehicle.push(item.vehicleId);
                    }
                });
            };

            // From Vehicle Checkin Tab Change  Any Entity Vehicle,Driver,Device.....................
            $scope.editEntity = function(post, index) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/changeEntitesModal.jsp',
                    controller: 'entityCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        vehicle: function() {
                            return post;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    post.vehicleNumber = result.vehicleNumber.vehicleNumber;
                    post.vehicleId = result.vehicleNumber.vehicleId;
                    post.vendorId = result.vehicleNumber.vendorId;
                    post.DriverName = result.driverName.DriverName;
                    post.MobileNumber = result.driverName.MobileNumber;
                    post.driverId = result.driverName.driverId;
                    post.deviceId = result.deviceId.deviceId;
                    post.deviceNumber = result.deviceId.deviceNumber;
                });
            };

            $scope.cancelVehicleCheckIn = function() {
                angular.forEach($scope.vehicleCheckInData, function(item) {
                    if (item.checkBoxFlag) {
                        item.checkBoxFlag = false;
                    }
                });
            }

            $scope.checkoutVehicle = function(vehicle) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/checkOutRemarks.jsp',
                    controller: 'checkOutRemarksCtrl',
                    size: 'sm',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        driver: function() {
                            return vehicle;
                        }
                    }
                });

                modalInstance.result.then(function(result) {

                    var data = {
                        checkInId: vehicle.checkInId,
                        branchId: branchId,
                        checkOutRemarks: result.remarks,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    //                     alert(JSON.stringify(data));
                    $http.post('services/vehicle/driverCheckOut/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == 'success') {
                                $('.vehicle' + vehicle.checkInId).hide();
                                ngDialog.open({
                                    template: 'Driver checkout successfully.',
                                    plain: true
                                });
                            }
                            if (data.status == 'duplicat') {
                                $('.vehicle' + vehicle.checkInId).hide();
                                ngDialog.open({
                                    template: 'Sorry you can not checkout this vehicle.This vehicle is already allocated to some route.',
                                    plain: true
                                });
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                });
            };
            //*****************************************ESCORT MANAGEMENT TAB*******************************************************        
            $scope.addEscort = function(combinedFacilityId) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/newEscortForm.jsp',
                    controller: 'newEscortCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        vendorContractManagData: function() {
                            return $scope.vendorContractManagData;
                        },
                        combinedFacilityId: function() {
                            return combinedFacilityId;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    //  var dob = convertDateUTC(result.escortdob);

                    $scope.escortDetailData.push({
                        escortId: result.escortId,
                        escortName: result.escortName,
                        escortMobileNumber: result.escortMobileNumber,
                        escortdob: result.escortdob,
                        escortAddress: result.escortAddress,
                        permanentAddress: result.permanentAddress,
                        escortBatchNumber: result.escortBatchNum,
                        escortVendorName: result.escortVendorName
                    });
                });
            };

            $scope.viewEscortDetail = function(post, index) {
                if (!post.isClicked) {
                    angular.forEach($scope.escortDetailData, function(item) {
                        item.isClicked = false;
                    });
                    post.isClicked = true;
                    $scope.currentIndex = index;
                    var data = {
                        escortId: post.escortId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/escort/escortallDetails/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.escortDetails = data;
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else post.isClicked = false;
            };

            $scope.closeEscortDetail = function(post) {
                post.isClicked = false;
            };

            $scope.viewEscortDetailDoc = function(post) {
                $state.go('home.escortDetail', {
                    'escortId': post.vehicleId
                });
            };


            // Enable escort

            $scope.enableEscort = function(post, index) {
                var data = {
                    escortId: post.escortId,
                    firstName: post.escortName,
                    mobileNumber: post.escortMobileNumber,
                    dateOfBirth: convertDate(post.escortdob),
                    address: post.escortAddress,
                    escortEmployeeId: post.escortBatchNum,
                    isActive: "Y",
                    userId: profileId,
                    combinedFacility: combinedFacility,
                    permanentAddress: post.permanentAddress

                };


                $confirm({
                        text: 'Are you sure you want to Enable this escort again?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/escort/modifyEscortDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.InputInvalid) {
                                    ngDialog.open({
                                        template: data.InputInvalid,
                                        plain: true
                                    });
                                } else {
                                    $scope.escortDetailDataDisable.splice(index, 1);
                                    ngDialog.open({
                                        template: 'Escort has been enabled',
                                        plain: true
                                    });
                                }

                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            //Disable Escort

            $scope.disableEscort = function(post, index) {

                var data = {
                    escortId: post.escortId,
                    firstName: post.escortName,
                    mobileNumber: post.escortMobileNumber,
                    dateOfBirth: convertDate(post.escortdob),
                    address: post.escortAddress,
                    escortEmployeeId: post.escortBatchNum,
                    isActive: "N",
                    userId: profileId,
                    combinedFacility: combinedFacility,
                    permanentAddress: post.permanentAddress
                };


                $confirm({
                        text: 'Are you sure you want to Disable this escort?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/escort/modifyEscortDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.InputInvalid) {
                                    ngDialog.open({
                                        template: data.InputInvalid,
                                        plain: true
                                    });
                                } else {
                                    $scope.escortDetailData.splice(index, 1);

                                    ngDialog.open({
                                        template: 'Escort has been disabled',
                                        plain: true
                                    });
                                }
                            }


                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };




            $scope.editEscortDetail = function(post, index, size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/editEscortForm.jsp',
                    controller: 'editEscortCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        escort: function() {
                            return post;
                        },
                        vendorContractManagData: function() {
                            return $scope.vendorContractManagData;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    var escortIndex;
                    var escortIndex = _.findIndex($scope.escortDetailData, {
                        escortId: result.escortId
                    });
                    if (escortIndex >= 0) {
                        $scope.escortDetailData[escortIndex] = {
                            'escortId': result.escortId,
                            'escortName': result.escortName,
                            'escortMobileNumber': result.escortMobileNumber,
                            'escortdob': convertDateUTC(result.escortdob),
                            'escortPincode': result.pincode,
                            'escortFatherName': result.escortFatherName,
                            'escortAddress': result.escortAddress,
                            'permanentAddress': result.permanentAddress,
                            'escortBatchNum': result.escortBatchNumber,
                            'escortVendorName': result.escortVendorName
                        };
                    }

                });
            }
            //Using the same upload controller and view as in the Setting>ImportEscort

            $scope.uploadEscort = function(post, parentIndex, index) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/uploadEscort.jsp',
                    controller: 'uploadEscortCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        escort: function() {
                            return post;
                        }
                    }
                });
                modalInstance.result.then(function(result) {});
            };



            /*Supervisor Management Tab*/

            $scope.addSupervisorDetails = function(vendor, size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/newSupervisorDetails.jsp',
                    controller: 'newSupervisorCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        vendor: function() {
                            return vendor;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    if (userRole == 'superadmin' || userRole == 'admin') {
                        $scope.getSupervisorData.push({
                            'supervisorName': result.firstName,
                            'emailId': result.email,
                            'supervisorlastName': result.lastName,
                            'supervisorId': result.supervisorId,
                            'supervisorEmployeeId': result.supervisorEmployeeId,
                            'supervisorMobileNumber': result.mobileNumber,
                            'supervisorFatherName': result.fatherName,
                            'supervisorGender': result.gender,
                            'supervisordob': result.supervisordob,
                            'supervisorAddress': result.permanentAddress,
                            'presentAddress': result.temporaryAddresss,
                            'designation': result.designation,
                            'vendorName': result.supervisorVendorName,
                            'physicallyChallenged': result.physicallyChallenged,
                            'remarks': result.remarks
                        });
                    }
                });
            };


            // Enable Supervisor

            $scope.enableSupervisor = function(post, index) {

                if (userRole == 'superadmin' || userRole == 'admin') {
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        supervisorId: post.supervisorId,
                        isActive: 'A',
                        combinedFacility: combinedFacility
                    };
                } else {
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        supervisorId: post.supervisorId,
                        isActive: 'P',
                        combinedFacility: combinedFacility
                    };
                }

                $confirm({
                        text: 'Are you sure you want to Enable this Supervisor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/fieldApp/supervisorStatusModified', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.getSupervisorData.splice(index, 1);
                                ngDialog.open({
                                    template: 'Supervisor has been Enabled',
                                    plain: true
                                });
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            //Disable Supervisor

            $scope.disableSupervisor = function(post, index) {

                var data = {
                    branchId: branchId,
                    userId: profileId,
                    supervisorId: post.supervisorId,
                    isActive: 'N',
                    combinedFacility: combinedFacility
                };

                $confirm({
                        text: 'Are you sure you want to Disable this Supervisor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/fieldApp/supervisorStatusModified', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $rootScope.getSupervisorData.splice(index, 1);

                                ngDialog.open({
                                    template: 'Supervisor has been disabled',
                                    plain: true
                                });
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            //*******************************************CHECK IN / CHECKOUT ESCORT TAB****************************************************** 
            $scope.select_EscortCheckIn = function(post) {
                if (!post.checkBoxFlag) {
                    post.checkBoxFlag = true;
                    $('.escort' + post.escortId).css('background-color', 'rgba(210, 230, 239, 0.5)');
                } else {
                    $('.escort' + post.escortId).css('background-color', 'white');
                    post.checkBoxFlag = false;
                }
            };

            $scope.submitEscortCheckIn = function(index) {

                var checkedEscort = [];
                angular.forEach($scope.escortCheckInData, function(item) {
                    if (item.checkBoxFlag) {
                        checkedEscort.push(item.escortId);
                    }
                });
                var data = {
                    escortIds: checkedEscort.toString(),
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/escort/checkInEscort/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        ngDialog.open({
                            template: 'Escort Checkin Successfully.',
                            plain: true
                        });
                        angular.forEach($scope.escortCheckInData, function(item) {
                            if (item.checkBoxFlag) {
                                item.checkBoxFlag = false;
                                $('.escort' + item.escortId).hide('slow');
                            }
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            };

            $scope.cancelEscortCheckIn = function() {
                angular.forEach($scope.escortCheckInData, function(item) {
                    if (item.checkBoxFlag) {
                        item.checkBoxFlag = false;
                    }
                });
            };

            //Escort CheckOut Funtion
            $scope.checkoutEscort = function(escort) {
                var data = {
                    branchId: branchId,
                    escortCheckInId: escort.escortCheckId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/escort/checkOutEscort/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $('.escort' + escort.escortId).hide('slow');
                        ngDialog.open({
                            template: 'Escort Checkout Successfully.',
                            plain: true
                        });
                        //              $scope.showalertMessage("Escort Checkout Successfully", "");
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            //***********************************************************PLANNED RELEASE DRIVERS TAB **********************************************************

            $scope.releaseDriver = function(driver, index) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/releaseDrivers.jsp',
                    controller: 'releaseDriverCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        driver: function() {
                            return driver;
                        }
                    }
                });

                modalInstance.result.then(function(result) {});
            };

            //***********************************************************DEVICE DETAIL TAB **********************************************************

            $scope.enableDevice = function(device) {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    isActive: 'Y',
                    imeiNumber: device.imeiNumber,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/device/devicestatus/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        device.deviceStatus = 'Y';
                        ngDialog.open({
                            template: 'Device Enable Successfully.',
                            plain: true
                        });
                        //              $scope.showalertMessage("Device Enable Successfully", "");
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.disableDevice = function(device) {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    isActive: 'N',
                    imeiNumber: device.imeiNumber,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/device/devicestatus/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        device.deviceStatus = 'N';
                        ngDialog.open({
                            template: 'Device Disable Successfully.',
                            plain: true
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            //****************************************INSPECTION VEHICLE**********************************************************

            $scope.resetFormInsp = function() {
                $scope.inspection = {
                    documentRc: {
                        text: false,
                        remarks: ''
                    },
                    documentInsurance: {
                        text: false,
                        remarks: ''
                    },
                    documentDriverLicence: {
                        text: false,
                        remarks: ''
                    },
                    documentUpdateJmp: {
                        text: false,
                        remarks: ''
                    },
                    firstAidKit: {
                        text: false,
                        remarks: ''
                    },
                    fireExtingusher: {
                        text: false,
                        remarks: ''
                    },
                    allSeatBeltsWorking: {
                        text: false,
                        remarks: ''
                    },
                    placard: {
                        text: false,
                        remarks: ''
                    },
                    mosquitoBat: {
                        text: false,
                        remarks: ''
                    },
                    panicButton: {
                        text: false,
                        remarks: ''
                    },
                    walkyTalky: {
                        text: false,
                        remarks: ''
                    },
                    gps: {
                        text: false,
                        remarks: ''
                    },
                    driverUniform: {
                        text: false,
                        remarks: ''
                    },
                    stephney: {
                        text: false,
                        remarks: ''
                    },
                    umbrella: {
                        text: false,
                        remarks: ''
                    },
                    torch: {
                        text: false,
                        remarks: ''
                    },
                    toolkit: {
                        text: false,
                        remarks: ''
                    },
                    seatUpholtseryCleanNotTorn: {
                        text: false,
                        remarks: ''
                    },
                    vehcileRoofUpholtseryCleanNotTorn: {
                        text: false,
                        remarks: ''
                    },
                    vehcileFloorUpholtseryCleanNotTorn: {
                        text: false,
                        remarks: ''
                    },
                    vehcileDashboardClean: {
                        text: false,
                        remarks: ''
                    },
                    vehicleGlassCleanStainFree: {
                        text: false,
                        remarks: ''
                    },
                    vehicleInteriorLightBrightWorking: {
                        text: false,
                        remarks: ''
                    },
                    bolsterSeperatingLastTwoSeats: {
                        text: false,
                        remarks: ''
                    },
                    audioWorkingOrNot: {
                        text: false,
                        remarks: ''
                    },
                    exteriorScratches: {
                        text: false,
                        remarks: ''
                    },
                    noPatchWork: {
                        text: false,
                        remarks: ''
                    },
                    pedalBrakeWorking: {
                        text: false,
                        remarks: ''
                    },
                    handBrakeWorking: {
                        text: false,
                        remarks: ''
                    },
                    tyresNoDentsTrimWheel: {
                        text: false,
                        remarks: ''
                    },
                    tyresGoodCondition: {
                        text: false,
                        remarks: ''
                    },
                    allTyresAndStephneyInflate: {
                        text: false,
                        remarks: ''
                    },
                    jackAndTool: {
                        text: false,
                        remarks: ''
                    },
                    numberofPunctruresdone: {
                        text: 0,
                        remarks: ''
                    },
                    wiperWorking: {
                        text: false,
                        remarks: ''
                    },
                    acCoolingIn5mins: {
                        text: false,
                        remarks: ''
                    },
                    noSmellInAC: {
                        text: false,
                        remarks: ''
                    },
                    acVentsClean: {
                        text: false,
                        remarks: ''
                    },
                    enginOilChangeIndicatorOn: {
                        text: false,
                        remarks: ''
                    },
                    coolantIndicatorOn: {
                        text: false,
                        remarks: ''
                    },
                    brakeClutchIndicatorOn: {
                        text: false,
                        remarks: ''
                    },
                    highBeamWorking: {
                        text: false,
                        remarks: ''
                    },
                    lowBeamWorking: {
                        text: false,
                        remarks: ''
                    },
                    rightFromtIndicatorWorking: {
                        text: false,
                        remarks: ''
                    },
                    leftFrontIndicatorWorking: {
                        text: false,
                        remarks: ''
                    },
                    parkingLightsWorking: {
                        text: false,
                        remarks: ''
                    },
                    ledDayTimeRunningLightWorking: {
                        text: false,
                        remarks: ''
                    },
                    rightRearIndicatorWorking: {
                        text: false,
                        remarks: ''
                    },
                    leftRearIndicatorWorking: {
                        text: false,
                        remarks: ''
                    },
                    brakeLightsOn: {
                        text: false,
                        remarks: ''
                    },
                    reverseLightsOn: {
                        text: false,
                        remarks: ''
                    },
                    fogLights: {
                        text: false,
                        remarks: ''
                    },
                    driverCheckInDidOrNot: {
                        text: false,
                        remarks: ''
                    },
                    feedback: {
                        text: 0,
                        remarks: ''
                    },
                    diesel: {
                        text: 'Full',
                        remarks: ''
                    },
                    hornWorking: {
                        text: false,
                        remarks: ''
                    },
                    reflectionSignBoard: {
                        text: false,
                        remarks: ''
                    },
                    driverName: {
                        text: false,
                        remarks: ''
                    },
                    vendor: {
                        name: $scope.vendorSelectedForInspection,
                        Id: $scope.vendorSelectedIdForInspection
                    }
                };
            };

            $scope.checkAllInspection = function() {
                $scope.checkboxAll = "true";
                $scope.checkAllShowInspection = false;
                $scope.inspection.documentRc.text = "true";
                $scope.inspection.documentInsurance.text = "true";
                $scope.inspection.documentDriverLicence.text = "true";
                $scope.inspection.documentUpdateJmp.text = "true";
                $scope.inspection.firstAidKit.text = "true";
                $scope.inspection.fireExtingusher.text = "true";
                $scope.inspection.allSeatBeltsWorking.text = "true";
                $scope.inspection.placard.text = "true";
                $scope.inspection.mosquitoBat.text = "true";
                $scope.inspection.panicButton.text = "true";
                $scope.inspection.walkyTalky.text = "true";
                $scope.inspection.gps.text = "true";
                $scope.inspection.driverUniform.text = "true";
                $scope.inspection.stephney.text = "true";
                $scope.inspection.umbrella.text = "true";
                $scope.inspection.torch.text = "true";
                $scope.inspection.toolkit.text = "true";
                $scope.inspection.seatUpholtseryCleanNotTorn.text = "true";
                $scope.inspection.vehcileRoofUpholtseryCleanNotTorn.text = "true";
                $scope.inspection.vehcileFloorUpholtseryCleanNotTorn.text = "true";
                $scope.inspection.vehcileDashboardClean.text = "true";
                $scope.inspection.vehicleGlassCleanStainFree.text = "true";
                $scope.inspection.vehicleInteriorLightBrightWorking.text = "true";
                $scope.inspection.bolsterSeperatingLastTwoSeats.text = "true";
                $scope.inspection.exteriorScratches.text = "true";
                $scope.inspection.noPatchWork.text = "true";
                $scope.inspection.pedalBrakeWorking.text = "true";
                $scope.inspection.handBrakeWorking.text = "true";
                $scope.inspection.tyresNoDentsTrimWheel.text = "true";
                $scope.inspection.allTyresAndStephneyInflate.text = "true";
                $scope.inspection.jackAndTool.text = "true";
                $scope.inspection.numberofPunctruresdone.text = 0;
                $scope.inspection.wiperWorking.text = "true";
                $scope.inspection.acCoolingIn5mins.text = "true";
                $scope.inspection.noSmellInAC.text = "true";
                $scope.inspection.acVentsClean.text = "true";
                $scope.inspection.enginOilChangeIndicatorOn.text = "true";
                $scope.inspection.coolantIndicatorOn.text = "true";
                $scope.inspection.highBeamWorking.text = "true";
                $scope.inspection.lowBeamWorking.text = "true";
                $scope.inspection.rightFromtIndicatorWorking.text = "true";
                $scope.inspection.brakeClutchIndicatorOn.text = "true";
                $scope.inspection.parkingLightsWorking.text = "true";
                $scope.inspection.ledDayTimeRunningLightWorking.text = "true";
                $scope.inspection.rightRearIndicatorWorking.text = "true";
                $scope.inspection.leftRearIndicatorWorking.text = "true";
                $scope.inspection.brakeClutchIndicatorOn.text = "true";
                $scope.inspection.brakeLightsOn.text = "true";
                $scope.inspection.reverseLightsOn.text = "true";
                $scope.inspection.fogLights.text = "true";
                $scope.inspection.driverCheckInDidOrNot.text = "true";
                $scope.inspection.feedback.text = 0;
                $scope.inspection.diesel.text = 'Full';
                $scope.inspection.hornWorking.text = "true";
                $scope.inspection.reflectionSignBoard.text = "true";
                $scope.inspection.driverName.text = "true";
                $scope.inspection.audioWorkingOrNot.text = "true";
                $scope.inspection.tyresGoodCondition.text = "true";
                $scope.inspection.leftFrontIndicatorWorking.text = "true";
            };

            $scope.deselectAllInspection = function() {
                $scope.checkboxAll = false;
                $scope.checkAllShowInspection = true;
                $scope.inspection.documentRc.text = false;
                $scope.inspection.documentInsurance.text = false;
                $scope.inspection.documentDriverLicence.text = false;
                $scope.inspection.documentUpdateJmp.text = false;
                $scope.inspection.firstAidKit.text = false;
                $scope.inspection.fireExtingusher.text = false;
                $scope.inspection.allSeatBeltsWorking.text = false;
                $scope.inspection.placard.text = false;
                $scope.inspection.mosquitoBat.text = false;
                $scope.inspection.panicButton.text = false;
                $scope.inspection.walkyTalky.text = false;
                $scope.inspection.gps.text = false;
                $scope.inspection.driverUniform.text = false;
                $scope.inspection.stephney.text = false;
                $scope.inspection.umbrella.text = false;
                $scope.inspection.torch.text = false;
                $scope.inspection.toolkit.text = false;
                $scope.inspection.seatUpholtseryCleanNotTorn.text = false;
                $scope.inspection.vehcileRoofUpholtseryCleanNotTorn.text = false;
                $scope.inspection.vehcileFloorUpholtseryCleanNotTorn.text = false;
                $scope.inspection.vehcileDashboardClean.text = false;
                $scope.inspection.vehicleGlassCleanStainFree.text = false;
                $scope.inspection.vehicleInteriorLightBrightWorking.text = false;
                $scope.inspection.bolsterSeperatingLastTwoSeats.text = false;
                $scope.inspection.exteriorScratches.text = false;
                $scope.inspection.noPatchWork.text = false;
                $scope.inspection.pedalBrakeWorking.text = false;
                $scope.inspection.handBrakeWorking.text = false;
                $scope.inspection.tyresNoDentsTrimWheel.text = false;
                $scope.inspection.allTyresAndStephneyInflate.text = false;
                $scope.inspection.jackAndTool.text = false;
                $scope.inspection.numberofPunctruresdone.text = 0;
                $scope.inspection.wiperWorking.text = false;
                $scope.inspection.acCoolingIn5mins.text = false;
                $scope.inspection.noSmellInAC.text = false;
                $scope.inspection.acVentsClean.text = false;
                $scope.inspection.enginOilChangeIndicatorOn.text = false;
                $scope.inspection.coolantIndicatorOn.text = false;
                $scope.inspection.highBeamWorking.text = false;
                $scope.inspection.lowBeamWorking.text = false;
                $scope.inspection.rightFromtIndicatorWorking.text = false;
                $scope.inspection.brakeClutchIndicatorOn.text = false;
                $scope.inspection.parkingLightsWorking.text = false;
                $scope.inspection.ledDayTimeRunningLightWorking.text = false;
                $scope.inspection.rightRearIndicatorWorking.text = false;
                $scope.inspection.leftRearIndicatorWorking.text = false;
                $scope.inspection.brakeClutchIndicatorOn.text = false;
                $scope.inspection.brakeLightsOn.text = false;
                $scope.inspection.reverseLightsOn.text = false;
                $scope.inspection.fogLights.text = false;
                $scope.inspection.driverCheckInDidOrNot.text = false;
                $scope.inspection.feedback.text = 0;
                $scope.inspection.diesel.text = 'Full';
                $scope.inspection.hornWorking.text = false;
                $scope.inspection.reflectionSignBoard.text = false;
                $scope.inspection.driverName.text = false;
                $scope.inspection.audioWorkingOrNot.text = false;
                $scope.inspection.tyresGoodCondition.text = false;
                $scope.inspection.leftFrontIndicatorWorking.text = false;
            }

            $scope.diesels = ['Full', 'Half', 'Quater', 'Nearly Empty'];
            $scope.punctures = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30];

            $scope.getVehicleInspection = function(currentTab) {
                $scope.currentTab = currentTab;

                if ($scope.currentTab == "inspectionForm") {
                    if ($scope.isAndroidDevice || $scope.isiOSDevice || $scope.adminRole) {
                        $scope.allInspectionVendors = [];
                        $scope.allVehiclesInpectionForm = [];
                        $scope.resetFormInsp();

                        angular.forEach($scope.allVendors, function(item) {
                            $scope.allInspectionVendors.push({
                                name: item.name,
                                Id: item.Id
                            })
                        });
                        $scope.inspection.diesel.text = $scope.diesels[0];
                    }
                }
                if ($scope.currentTab == "inspectionDetail") {
                    $scope.allInspectionDetailVendors = [];
                    angular.forEach($scope.allVendors, function(item) {
                        $scope.allInspectionDetailVendors.push({
                            name: item.name,
                            Id: item.Id
                        })
                    });

                    var todaysDate = new Date();
                    var tomorrowDate = new Date();
                    tomorrowDate.setDate(tomorrowDate.getDate() + 1);
                    $scope.dates[0].value = todaysDate;
                    $scope.fromDate = todaysDate;
                    $scope.toDate = tomorrowDate;

                }
            };

            $scope.isReadytoGetVehicle = false;
            $scope.vendorSelectedForInspection = null;

            $scope.setVendor = function(vendorSelected) {
                if (angular.isObject(vendorSelected)) {
                    var data = {
                        vendorId: vendorSelected.Id,
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/contract/getVehicleDriverCheckInEntity/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if ($scope.currentTab == "inspectionForm") {
                                if ($scope.vendorSelectedForInspection != null) {
                                    $confirm({
                                            text: 'Changing the Vendor will reset the form and all your current changes will be lost!! Do you want to continue?',
                                            title: 'Confirmation',
                                            ok: 'Yes',
                                            cancel: 'No'
                                        })
                                        .then(function() {
                                            $scope.getAllDrivers(vendorSelected.Id);
                                            $scope.showInspectionTable = false;
                                            $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                                            $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                                            $scope.allVehiclesInpectionForm = [];
                                            $scope.driverSelected = '';
                                            $scope.vehicleTypeSelected = '';
                                            $scope.inspection.vehicleSelected = '';
                                            angular.forEach(data, function(item) {
                                                $scope.allVehiclesInpectionForm.push({
                                                    vehicleNumber: item.vehicleNumber,
                                                    Id: item.vehicleId,
                                                    vehicleType: item.vehicleType,
                                                    driverName: item.driverName
                                                });
                                            });
                                        }, function() {
                                            $scope.inspection.vendor = {
                                                name: $scope.vendorSelectedForInspection,
                                                Id: $scope.vendorSelectedIdForInspection
                                            };
                                        });
                                } else {
                                    $scope.getAllDrivers(vendorSelected.Id);
                                    $scope.showInspectionTable = false;
                                    $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                                    $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                                    $scope.allVehiclesInpectionForm = [];
                                    $scope.driverSelected = '';
                                    $scope.vehicleTypeSelected = '';
                                    angular.forEach(data, function(item) {
                                        $scope.allVehiclesInpectionForm.push({
                                            vehicleNumber: item.vehicleNumber,
                                            Id: item.vehicleId,
                                            vehicleType: item.vehicleType,
                                            driverName: item.driverName
                                        });
                                    });

                                }
                            }
                            if ($scope.currentTab == "inspectionDetail") {
                                $scope.allVehiclesInpectionDetail = [];
                                angular.forEach(data, function(item) {
                                    $scope.allVehiclesInpectionDetail.push({
                                        vehicleNumber: item.vehicleNumber,
                                        Id: item.vehicleId
                                    });
                                });
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                    // }
                } else {
                    $scope.allVehiclesInpectionForm = [];
                }

            };


            $scope.setVendorDriver = function(vendorSelected) {
                if (vendorSelected == null) {
                    $scope.detailIns.vehicleSelected = '';
                    $scope.vehicleDisabled = true;
                }

                if (angular.isObject(vendorSelected)) {
                    var data = {
                        vendorId: vendorSelected.Id,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/contract/allActiveVehicle/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.vehicleDisabled = false;
                            if ($scope.currentTab == "inspectionForm") {
                                if ($scope.vendorSelectedForInspection != null) {
                                    $confirm({
                                            text: 'Changing the Vendor will reset the form and all your current changes will be lost!! Do you want to continue?',
                                            title: 'Confirmation',
                                            ok: 'Yes',
                                            cancel: 'No'
                                        })
                                        .then(function() {
                                            $scope.getAllDrivers(vendorSelected.Id);
                                            $scope.showInspectionTable = false;
                                            $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                                            $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                                            $scope.allVehiclesInpectionForm = [];

                                            angular.forEach(data, function(item) {
                                                $scope.allVehiclesInpectionForm.push({
                                                    vehicleNumber: item.vehicleNumber,
                                                    Id: item.vehicleId,
                                                    vehicleType: item.vehicleType
                                                });
                                            });
                                        }, function() {
                                            $scope.inspection.vendor = {
                                                name: $scope.vendorSelectedForInspection,
                                                Id: $scope.vendorSelectedIdForInspection
                                            };
                                        });
                                } else {
                                    $scope.getAllDrivers(vendorSelected.Id);
                                    $scope.showInspectionTable = false;
                                    $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                                    $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                                    $scope.allVehiclesInpectionForm = [];
                                    angular.forEach(data, function(item) {
                                        $scope.allVehiclesInpectionForm.push({
                                            vehicleNumber: item.vehicleNumber,
                                            Id: item.vehicleId,
                                            vehicleType: item.vehicleType
                                        });
                                    });

                                }
                            }
                            if ($scope.currentTab == "inspectionDetail") {
                                $scope.allVehiclesInpectionDetail = [];
                                angular.forEach(data, function(item) {
                                    $scope.allVehiclesInpectionDetail.push({
                                        vehicleNumber: item.vehicleNumber,
                                        Id: item.vehicleId
                                    });
                                });
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                    // }
                } else {
                    $scope.allVehiclesInpectionForm = [];
                }

            };


            $scope.setInspectionForVehicle = function(vehicle) {
                $scope.checkAllButtonEnabled = true;
                $scope.diverName = vehicle.driverName;
                $scope.vehicleType = vehicle.vehicleType;
                $scope.driverSelected = [];
                $scope.driverSelected.push($scope.diverName);
                $scope.vehicleTypeSelected = [];
                $scope.vehicleTypeSelected.push($scope.vehicleType);

                if (angular.isObject(vehicle) && angular.isObject($scope.inspection.vendor)) {

                    if ($scope.showInspectionTable) {
                        $confirm({
                                text: 'Are you sure you want change the Vehicle for ' + $scope.inspection.vendor.name + '? All current changes will be lost!! Do you want to continue?',
                                title: 'Confirmation',
                                ok: 'Yes',
                                cancel: 'No'
                            })

                            .then(function() {
                                $scope.showInspectionTable = true;
                                $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                                $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                                $scope.vehicleSelectedForInspection = vehicle.vehicleNumber;
                                $scope.vehicleSelectedIdForInspection = vehicle.Id;
                                $scope.vehicleSelectedTypeForInspection = vehicle.vehicleType;
                                $scope.driverSelectedForInspection = $scope.inspection.driverName.driverName;
                                $scope.driverSelectedIdForInspection = $scope.inspection.driverName.driverId;

                                $scope.resetFormInsp();
                                $scope.inspection.vehicleSelected = {
                                    vehicleNumber: $scope.vehicleSelectedForInspection,
                                    Id: $scope.vehicleSelectedIdForInspection
                                };
                                $scope.inspection.driverName = {
                                    driverName: $scope.driverSelectedForInspection,
                                    driverId: $scope.driverSelectedIdForInspection
                                };


                            }, function() {
                                $scope.inspection.vehicleSelected = {
                                    vehicleNumber: $scope.vehicleSelectedForInspection,
                                    Id: $scope.vehicleSelectedIdForInspection
                                };
                            });
                    } else {
                        $scope.showInspectionTable = true;
                        $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                        $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                        $scope.vehicleSelectedForInspection = $scope.inspection.vehicleSelected.vehicleNumber;
                        $scope.vehicleSelectedIdForInspection = $scope.inspection.vehicleSelected.Id;
                        $scope.vehicleSelectedTypeForInspection = $scope.inspection.vehicleSelected.vehicleType;
                    }
                }
            };

            $scope.setInspectionForDriver = function(driver, vehicle) {
                if (angular.isObject(driver) && angular.isObject(vehicle) && angular.isObject($scope.inspection.vendor)) {
                    if ($scope.showInspectionTable) {
                        $confirm({
                                text: 'Are you sure you want change the Driver for ' + $scope.inspection.vendor.name + '?',
                                title: 'Confirmation',
                                ok: 'Yes',
                                cancel: 'No'
                            })
                            .then(function() {
                                $scope.showInspectionTable = true;
                                $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                                $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                                $scope.vehicleSelectedForInspection = $scope.inspection.vehicleSelected.vehicleNumber;
                                $scope.vehicleSelectedIdForInspection = $scope.inspection.vehicleSelected.Id;
                                $scope.vehicleSelectedTypeForInspection = $scope.inspection.vehicleSelected.vehicleType;
                            });
                    } else {
                        if (angular.isObject($scope.inspection.vehicleSelected)) {
                            $scope.showInspectionTable = true;
                            $scope.vendorSelectedForInspection = $scope.inspection.vendor.name;
                            $scope.vendorSelectedIdForInspection = $scope.inspection.vendor.Id;
                            $scope.vehicleSelectedForInspection = $scope.inspection.vehicleSelected.vehicleNumber;
                            $scope.vehicleSelectedIdForInspection = $scope.inspection.vehicleSelected.Id;
                            $scope.vehicleSelectedTypeForInspection = $scope.inspection.vehicleSelected.vehicleType;
                        } else {
                            ngDialog.open({
                                template: 'Please Select a valid vehicle',
                                plain: true
                            });
                            $scope.inspection.driverName = "";
                        }
                    }
                }
            };

            $scope.submitInspection = function(inspection, driverSelected) {
                $scope.inspection.vehicleSelected = {
                    vehicleNumber: $scope.vehicleSelectedForInspection,
                    Id: $scope.vehicleSelectedIdForInspection
                };
                $scope.inspection.vendor = {
                    name: $scope.vendorSelectedForInspection,
                    Id: $scope.vendorSelectedIdForInspection
                };

                if (angular.isObject(inspection.driverName)) {
                    $confirm({
                            text: 'Are you sure you want to submit the Inspection details?',
                            title: 'Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })

                        .then(function() {

                            angular.forEach($scope.inspection, function(item) {
                                if (item.text == 'false') {
                                    item.text = false;
                                } else {
                                    if (item.text == 'true' || item.text) {
                                        item.remarks = 'N'
                                    } else {
                                        item.remarks = item.remarks;
                                    }
                                }
                            });
                            //$("#inspectionSubmitButton").addClass("disabled"); 
                            var data = {
                                efmFmUserMaster: {
                                    userId: profileId
                                },
                                efmFmVehicleMaster: {
                                    vehicleId: inspection.vehicleSelected.Id
                                },
                                driverName: driverSelected.toString(),
                                documentRc: inspection.documentRc.text,
                                documentInsurance: inspection.documentInsurance.text,
                                documentDriverLicence: inspection.documentDriverLicence.text,
                                documentUpdateJmp: inspection.documentUpdateJmp.text,
                                firstAidKit: inspection.firstAidKit.text,
                                fireExtingusher: inspection.fireExtingusher.text,
                                allSeatBeltsWorking: inspection.allSeatBeltsWorking.text,
                                placard: inspection.placard.text,
                                mosquitoBat: inspection.mosquitoBat.text,
                                panicButton: inspection.panicButton.text,
                                walkyTalky: inspection.walkyTalky.text,

                                gps: inspection.gps.text,

                                driverUniform: inspection.driverUniform.text,
                                stephney: inspection.stephney.text,
                                umbrella: inspection.umbrella.text,
                                torch: inspection.torch.text,
                                toolkit: inspection.toolkit.text,
                                seatUpholtseryCleanNotTorn: inspection.seatUpholtseryCleanNotTorn.text,
                                vehcileRoofUpholtseryCleanNotTorn: inspection.vehcileRoofUpholtseryCleanNotTorn.text,
                                vehcileFloorUpholtseryCleanNotTorn: inspection.vehcileFloorUpholtseryCleanNotTorn.text,
                                vehcileDashboardClean: inspection.vehcileDashboardClean.text,
                                vehicleGlassCleanStainFree: inspection.vehicleGlassCleanStainFree.text,
                                vehicleInteriorLightBrightWorking: inspection.vehicleInteriorLightBrightWorking.text,
                                bolsterSeperatingLastTwoSeats: inspection.bolsterSeperatingLastTwoSeats.text,


                                audioWorkingOrNot: inspection.audioWorkingOrNot.text,
                                exteriorScratches: inspection.exteriorScratches.text,
                                noPatchWork: inspection.noPatchWork.text,
                                pedalBrakeWorking: inspection.pedalBrakeWorking.text,
                                handBrakeWorking: inspection.handBrakeWorking.text,
                                tyresNoDentsTrimWheel: inspection.tyresNoDentsTrimWheel.text,
                                tyresGoodCondition: inspection.tyresGoodCondition.text,
                                allTyresAndStephneyInflate: inspection.allTyresAndStephneyInflate.text,
                                jackAndTool: inspection.jackAndTool.text,
                                numberofPunctruresdone: inspection.numberofPunctruresdone.text,
                                wiperWorking: inspection.wiperWorking.text,
                                acCoolingIn5mins: inspection.acCoolingIn5mins.text,
                                noSmellInAC: inspection.noSmellInAC.text,
                                acVentsClean: inspection.acVentsClean.text,
                                enginOilChangeIndicatorOn: inspection.enginOilChangeIndicatorOn.text,
                                coolantIndicatorOn: inspection.coolantIndicatorOn.text,
                                brakeClutchIndicatorOn: inspection.brakeClutchIndicatorOn.text,
                                highBeamWorking: inspection.highBeamWorking.text,
                                lowBeamWorking: inspection.lowBeamWorking.text,
                                rightFromtIndicatorWorking: inspection.rightFromtIndicatorWorking.text,
                                leftFrontIndicatorWorking: inspection.leftFrontIndicatorWorking.text,
                                parkingLightsWorking: inspection.parkingLightsWorking.text,
                                ledDayTimeRunningLightWorking: inspection.ledDayTimeRunningLightWorking.text,
                                rightRearIndicatorWorking: inspection.rightRearIndicatorWorking.text,
                                leftRearIndicatorWorking: inspection.leftRearIndicatorWorking.text,
                                brakeLightsOn: inspection.brakeLightsOn.text,
                                reverseLightsOn: inspection.reverseLightsOn.text,

                                fogLights: inspection.fogLights.text,
                                driverCheckInDidOrNot: inspection.driverCheckInDidOrNot.text,
                                feedback: inspection.feedback.text,

                                diesel: inspection.diesel.text,
                                hornWorking: inspection.hornWorking.text,
                                reflectionSignBoard: inspection.reflectionSignBoard.text,

                                documentRcRemarks: inspection.documentRc.remarks,
                                documentInsuranceRemarks: inspection.documentInsurance.remarks,
                                documentDriverLicenceRemarks: inspection.documentDriverLicence.remarks,
                                documentUpdateJmpRemarks: inspection.documentUpdateJmp.remarks,
                                firstAidKitRemarks: inspection.firstAidKit.remarks,
                                fireExtingusherRemarks: inspection.fireExtingusher.remarks,
                                allSeatBeltsWorkingRemarks: inspection.allSeatBeltsWorking.remarks,
                                placardRemarks: inspection.placard.remarks,
                                mosquitoBatRemarks: inspection.mosquitoBat.remarks,
                                panicButtonRemarks: inspection.panicButton.remarks,
                                walkyTalkyRemarks: inspection.walkyTalky.remarks,
                                gpsRemarks: inspection.gps.remarks,
                                driverUniformRemarks: inspection.driverUniform.remarks,
                                stephneyRemarks: inspection.stephney.remarks,
                                umbrellaRemarks: inspection.umbrella.remarks,
                                torchRemarks: inspection.torch.remarks,
                                toolkitRemarks: inspection.toolkit.remarks,
                                seatUpholtseryCleanNotTornRemarks: inspection.seatUpholtseryCleanNotTorn.remarks,
                                vehcileRoofUpholtseryCleanNotTornRemarks: inspection.vehcileRoofUpholtseryCleanNotTorn.remarks,
                                vehcileFloorUpholtseryCleanNotTornRemarks: inspection.vehcileFloorUpholtseryCleanNotTorn.remarks,
                                vehcileDashboardCleanRemarks: inspection.vehcileDashboardClean.remarks,
                                vehicleGlassCleanStainFreeRemarks: inspection.vehicleGlassCleanStainFree.remarks,
                                vehicleInteriorLightBrightWorkingRemarks: inspection.vehicleInteriorLightBrightWorking.remarks,
                                bolsterSeperatingLastTwoSeatsRemarks: inspection.bolsterSeperatingLastTwoSeats.remarks,

                                audioWorkingOrNotRemarks: inspection.audioWorkingOrNot.remarks,
                                exteriorScratchesRemarks: inspection.exteriorScratches.remarks,
                                noPatchWorkRemarks: inspection.noPatchWork.remarks,
                                pedalBrakeWorkingRemarks: inspection.pedalBrakeWorking.remarks,
                                handBrakeWorkingRemarks: inspection.handBrakeWorking.remarks,
                                tyresNoDentsTrimWheelRemarks: inspection.tyresNoDentsTrimWheel.remarks,
                                tyresGoodConditionRemarks: inspection.tyresGoodCondition.remarks,
                                allTyresAndStephneyInflateRemarks: inspection.allTyresAndStephneyInflate.remarks,
                                jackAndToolRemarks: inspection.jackAndTool.remarks,
                                wiperWorkingRemarks: inspection.wiperWorking.remarks,
                                acCoolingIn5minsRemarks: inspection.acCoolingIn5mins.remarks,
                                noSmellInACRemarks: inspection.noSmellInAC.remarks,
                                acVentsCleanRemarks: inspection.acVentsClean.remarks,
                                enginOilChangeIndicatorOnRemarks: inspection.enginOilChangeIndicatorOn.remarks,
                                coolantIndicatorOnRemarks: inspection.coolantIndicatorOn.remarks,
                                brakeClutchIndicatorOnRemarks: inspection.brakeClutchIndicatorOn.remarks,
                                highBeamWorkingRemarks: inspection.highBeamWorking.remarks,
                                lowBeamWorkingRemarks: inspection.lowBeamWorking.remarks,
                                rightFromtIndicatorWorkingRemarks: inspection.rightFromtIndicatorWorking.remarks,
                                leftFrontIndicatorWorkingRemarks: inspection.leftFrontIndicatorWorking.remarks,
                                parkingLightsWorkingRemarks: inspection.parkingLightsWorking.remarks,
                                ledDayTimeRunningLightWorkingRemarks: inspection.ledDayTimeRunningLightWorking.remarks,
                                rightRearIndicatorWorkingRemarks: inspection.rightRearIndicatorWorking.remarks,
                                leftRearIndicatorWorkingRemarks: inspection.leftRearIndicatorWorking.remarks,
                                brakeLightsOnRemarks: inspection.brakeLightsOn.remarks,
                                reverseLightsOnRemarks: inspection.reverseLightsOn.remarks,

                                fogLightsRemarks: inspection.fogLights.remarks,
                                feedbackRemarks: inspection.feedback.remarks,
                                driverCheckInDidOrNotRemarks: inspection.driverCheckInDidOrNot.remarks,

                                hornWorkingRemarks: inspection.hornWorking.remarks,
                                reflectionSignBoardRemarks: inspection.reflectionSignBoard.remarks,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            };



                            $http.post('services/vehicle/addingVehicleInspection/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data == "Success") {
                                        ngDialog.open({
                                            template: 'Vehice Inspection Done Successfully !',
                                            plain: true
                                        });
                                        $("#inspectionSubmitButton").removeClass("disabled");
                                        $scope.formInspOne.pageOne.$setPristine();
                                        $scope.formInspTwo.pageTwo.$setPristine();
                                        $scope.formInspThree.pageThree.$setPristine();
                                        $scope.formInspFour.pageFour.$setPristine();
                                        $scope.resetFormInsp();
                                        $scope.nextToPage1();
                                        $scope.showInspectionTable = true;
                                    }
                                }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });

                        });
                } else {
                    ngDialog.open({
                        template: 'Please select a valid Driver from the dropdown.',
                        plain: true
                    });
                    //        $scope.showalertMessage("Please select a valid Driver from the dropdown.", "");
                }
            };

            $scope.nextToPage2 = function() {
                $('.page1').slideUp('slow');
                $('.page2').show();
            };

            $scope.nextToPage3 = function() {
                $('.page2').slideUp('slow');
                $('.page3').show();
            };

            $scope.nextToPage4 = function() {
                $('.page3').slideUp('slow');
                $('.page4').show();
            };

            $scope.nextToPage1 = function() {
                $('.page4').slideUp('slow');
                $('.page1').show();
            };

            $scope.backToPage3 = function() {
                $('.page3').slideDown('slow');
                $('.page4').hide();
            };

            $scope.backToPage2 = function() {
                $('.page2').slideDown('slow');
                $('.page3').hide();
            };

            $scope.backToPage1 = function() {
                $('.page1').slideDown('slow');
                $('.page2').hide();
            };



            //**************************************INSPECTION DETAIL VIEW ***************************************************
            $scope.compare = false;
            $scope.isAllCompare = false;
            $scope.compareInspectionArray = [];

            $scope.test = [{
                    inspectorName: "Saima Aziz",
                    gps: {
                        text: false,
                        remarks: "G1 123"
                    },
                    rcDocument: {
                        text: false,
                        remarks: "R1 123"
                    }
                },
                {
                    inspectorName: "Sarfraz Khan",
                    gps: {
                        text: true,
                        remarks: "G2 4456"
                    },
                    rcDocument: {
                        text: false,
                        remarks: "R2 456"
                    }
                },
                {
                    inspectorName: "Daniel Smith",
                    gps: {
                        text: false,
                        remarks: "G3 789"
                    },
                    rcDocument: {
                        text: false,
                        remarks: "R3 789"
                    }
                }
            ]

            //FUNCTION : Get aLL Vehicle Inspection from date vice
            $scope.getReport = function(fromDate, toDate) {
                $('.popover').hide();
                $scope.showVehicleNumber = true;
                $scope.byVendorVehicleButtonClicked = true;

                if ($scope.checkDateRangeValidity(new Date(fromDate).getTime(), new Date(toDate).getTime())) {

                    if ($scope.detailIns.vendor == undefined) {
                        $scope.efmFmVehicleMaster = {
                            vehicleId: 0
                        };
                        $scope.vendorId = 0;
                        $scope.showVehicleNumber = true;
                    } else if ($scope.detailIns.vendor != undefined && $scope.detailIns.vehicleSelected == undefined) {
                        $scope.vendorId = $scope.detailIns.vendor.Id;
                        $scope.efmFmVehicleMaster = {
                            vehicleId: 0
                        };
                        $scope.showVehicleNumber = true;
                    } else {
                        $scope.vendorId = $scope.detailIns.vendor.Id;
                        $scope.efmFmVehicleMaster = {
                            vehicleId: $scope.detailIns.vehicleSelected.Id
                        };
                        $scope.showVehicleNumber = false;
                    }

                    var data = {
                        branchId: branchId,
                        fromDate: convertDateUTC(fromDate), //"01-01-2016", //convertDateUTC(toDate),
                        toDate: convertDateUTC(toDate), //"25-02-2016" //convertDateUTC(fromDate),  
                        efmFmVehicleMaster: $scope.efmFmVehicleMaster,
                        vendorId: $scope.vendorId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/vehicle/getVehicleInspection/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.inspectionDetailData = data;
                            $scope.inspectionDetailDataLength = data.length;
                            $scope.byVendorVehicleButtonClicked = false;

                            $scope.isAllCompare = false;
                            if ($scope.inspectionDetailData.length > 0) {
                                angular.forEach($scope.inspectionDetailData, function(item) {
                                    item.checkCompare = false;
                                    item.documentRcRemarksIsFull = false;
                                    item.documentInsuranceRemarksIsFull = false;
                                    item.documentDriverLicenceRemarksIsFull = false;
                                    item.documentUpdateJmpRemarksIsFull = false;
                                    item.firstAidKitRemarksIsFull = false;
                                    item.fireExtingusherRemarksIsFull = false;
                                    item.allSeatBeltsWorkingRemarksIsFull = false;
                                    item.placardRemarksIsFull = false;
                                    item.mosquitoBatRemarksIsFull = false;
                                    item.panicButtonRemarksIsFull = false;
                                    item.walkyTalkyRemarksIsFull = false;
                                    item.gpsRemarksIsFull = false;
                                    item.driverUniformRemarksIsFull = false;
                                    item.stephneyRemarksIsFull = false;
                                    item.umbrellaRemarksIsFull = false;
                                    item.torchRemarksIsFull = false;
                                    item.seatUpholtseryCleanNotTornRemarksIsFull = false;
                                    item.vehcileRoofUpholtseryCleanNotTornRemarksIsFull = false;
                                    item.vehcileFloorUpholtseryCleanNotTornRemarksIsFull = false;
                                    item.vehcileDashboardCleanRemarksIsFull = false;
                                    item.vehicleGlassCleanStainFreeRemarksIsFull = false;
                                    item.vehicleInteriorLightBrightWorkingRemarksIsFull = false;
                                    item.bolsterSeperatingLastTwoSeatsRemarksIsFull = false;
                                    item.audioWorkingOrNotRemarksIsFull = false;
                                    item.exteriorScratchesRemarksIsFull = false;
                                    item.noPatchWorkRemarksIsFull = false;
                                    item.pedalBrakeWorkingRemarksIsFull = false;
                                    item.handBrakeWorkingRemarksIsFull = false;
                                    item.tyresNoDentsTrimWheelRemarksIsFull = false;
                                    item.tyresGoodConditionRemarksIsFull = false;
                                    item.allTyresAndStephneyInflateRemarksIsFull = false;
                                    item.jackAndToolRemarksIsFull = false;
                                    item.wiperWorkingRemarksIsFull = false;
                                    item.acCoolingIn5minsRemarksIsFull = false;
                                    item.noSmellInACRemarksIsFull = false;
                                    item.acVentsCleanRemarksIsFull = false;
                                    item.enginOilChangeIndicatorOnRemarksIsFull = false;
                                    item.coolantIndicatorOnIsFull = false; //CHECK
                                    item.brakeClutchIndicatorOnRemarksIsFull = false;
                                    item.highBeamWorkingRemarksIsFull = false;
                                    item.lowBeamWorkingRemarksIsFull = false;
                                    item.rightFromtIndicatorWorkingRemarksIsFull = false;
                                    item.leftFrontIndicatorWorkingRemarksIsFull = false;
                                    item.parkingLightsWorkingRemarksIsFull = false;
                                    item.ledDayTimeRunningLightWorkingRemarksIsFull = false;
                                    item.rightRearIndicatorWorkingRemarksIsFull = false;
                                    item.leftRearIndicatorWorkingRemarksIsFull = false;
                                    item.brakeLightsOnRemarksIsFull = false;
                                    item.reverseLightsOnRemarksIsFull = false;
                                    item.fogLightsRemarksIsFull = false;
                                    item.feedbackRemarksIsFull = false;
                                    item.driverCheckInDidOrNotRemarksIsFull = false;
                                    item.hornWorkingRemarksIsFull = false;
                                    item.reflectionSignBoardRemarksIsFull = false;
                                    item.numberofPunctruresdoneRemarksIsFull = false;
                                });
                                $scope.gotInspectionDetailResult = true;
                            } else {
                                ngDialog.open({
                                    template: 'No Data found for this Date and Vehicle.',
                                    plain: true
                                });

                                $scope.gotInspectionDetailResult = false;
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }



            };

            $scope.addToCompare = function(inpectionItem) {
                if (inpectionItem.checkCompare) {
                    inpectionItem.checkCompare = false;
                    var index = _.findIndex($scope.compareInspectionArray, {
                        inspectionId: inpectionItem.inspectionId
                    });

                    $scope.compareInspectionArray.splice(index, 1);
                    if ($scope.compareInspectionArray.length < $scope.inspectionDetailData.length) {
                        $scope.isAllCompare = false;
                    }
                } else {
                    inpectionItem.checkCompare = true;
                    $scope.compareInspectionArray.push(inpectionItem);
                }
            };

            $scope.checkAll = function() {
                if ($scope.isAllCompare) {
                    $scope.compareInspectionArray = [];
                    angular.forEach($scope.inspectionDetailData, function(item) {
                        if (item.checkCompare) {
                            item.checkCompare = false;
                        }
                    });
                    $scope.isAllCompare = false;
                } else {
                    angular.forEach($scope.inspectionDetailData, function(item) {
                        if (!item.checkCompare) {
                            item.checkCompare = true;
                        }
                    });
                    angular.copy($scope.inspectionDetailData, $scope.compareInspectionArray);
                    $scope.isAllCompare = true;
                }

            };

            $scope.compareSpinner = false;

            $scope.getCompareResults = function() {
                $scope.compareSpinner = true;
                // angular.copy($scope.inspectionDetailData, $scope.compareInspectionArray);
                if ($scope.compareInspectionArray.length > 1 && !$scope.compare) {
                    $scope.compare = true;
                    $scope.compareSpinner = false;
                    $scope.byVendorVehicleButtonClicked = false;
                } else if ($scope.compare) {
                    $scope.compare = false;
                    $scope.compareSpinner = false;
                    $scope.byVendorVehicleButtonClicked = false;
                } else {
                    $scope.compare = false;
                    $scope.compareSpinner = false;
                    ngDialog.open({
                        template: 'Please select Minimum two vehicles to compare',
                        plain: true
                    });
                }

            };

            $scope.expandCollapeRemarks = function(inspection) {
                if (inspection.seeFullRemark) {
                    inspection.seeFullRemark = false;
                } else {
                    inspection.seeFullRemark = true;
                }
            };

            $scope.excelExport = function() {
                var blob = new Blob([document.getElementById('inspectionDetailCompare').innerHTML], {
                    type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                });
                saveAs(blob, "Inspection Detail Comparison.xls");

            };


            $scope.uploadInspectionDocument = function(inspection) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/vendorDashboard/uploadInspectionDoc.jsp',
                    controller: 'uploadInspectionDocCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        inspection: function() {
                            return inspection;
                        }
                    }
                });
                modalInstance.result.then(function(result) {});

            };

            $scope.exportExcelByDate = function(date) {
                $scope.ExcelData = $filter('filter')($scope.inspectionDetailData, date.inspectionDate);
                var excelSingleArray = [{
                        Label: "RC Documents",
                        Result: $scope.ExcelData[0].documentRc,
                        Remarks: $scope.ExcelData[0].documentRcRemarks
                    },
                    {
                        Label: "Insurance Document",
                        Result: $scope.ExcelData[0].documentInsurance,
                        Remarks: $scope.ExcelData[0].documentInsuranceRemarks
                    },
                    {
                        Label: "Driver Licens",
                        Result: $scope.ExcelData[0].documentDriverLicence,
                        Remarks: $scope.ExcelData[0].documentDriverLicenceRemarks
                    },
                    {
                        Label: "Update JMP",
                        Result: $scope.ExcelData[0].documentUpdateJmp,
                        Remarks: $scope.ExcelData[0].documentUpdateJmpRemarks
                    },
                    {
                        Label: "First Aid Kit",
                        Result: $scope.ExcelData[0].firstAidKit,
                        Remarks: $scope.ExcelData[0].firstAidKitRemarks
                    },
                    {
                        Label: "Fire Extinguisher",
                        Result: $scope.ExcelData[0].fireExtingusher,
                        Remarks: $scope.ExcelData[0].fireExtingusherRemarks
                    },
                    {
                        Label: "All Seat Belt Working",
                        Result: $scope.ExcelData[0].allSeatBeltsWorking,
                        Remarks: $scope.ExcelData[0].allSeatBeltsWorkingRemarks
                    },
                    {
                        Label: "Placard",
                        Result: $scope.ExcelData[0].placard,
                        Remarks: $scope.ExcelData[0].placardRemarks
                    },
                    {
                        Label: "Mosquito Bat",
                        Result: $scope.ExcelData[0].mosquitoBat,
                        Remarks: $scope.ExcelData[0].mosquitoBatRemarks
                    },
                    {
                        Label: "Panic Button",
                        Result: $scope.ExcelData[0].panicButton,
                        Remarks: $scope.ExcelData[0].panicButtonRemarks
                    },
                    {
                        Label: "Walky Talky",
                        Result: $scope.ExcelData[0].walkyTalky,
                        Remarks: $scope.ExcelData[0].walkyTalkyRemarks
                    },
                    {
                        Label: "GPS",
                        Result: $scope.ExcelData[0].gps,
                        Remarks: $scope.ExcelData[0].gpsRemarks
                    },
                    {
                        Label: "Driver Uniform",
                        Result: $scope.ExcelData[0].driverUniform,
                        Remarks: $scope.ExcelData[0].driverUniformRemarks
                    },
                    {
                        Label: "Stepney",
                        Result: $scope.ExcelData[0].stephney,
                        Remarks: $scope.ExcelData[0].stephneyRemarks
                    },
                    {
                        Label: "Umbrella",
                        Result: $scope.ExcelData[0].umbrella,
                        Remarks: $scope.ExcelData[0].umbrellaRemarks
                    },
                    {
                        Label: "Torch",
                        Result: $scope.ExcelData[0].torch,
                        Remarks: $scope.ExcelData[0].torchRemarks
                    },
                    {
                        Label: "Toolkit",
                        Result: $scope.ExcelData[0].toolkit,
                        Remarks: $scope.ExcelData[0].toolkitRemarks
                    },
                    {
                        Label: "Seat Upholstery is Clean / Not Torn",
                        Result: $scope.ExcelData[0].seatUpholtseryCleanNotTorn,
                        Remarks: $scope.ExcelData[0].seatUpholtseryCleanNotTornRemarks
                    },
                    {
                        Label: "Vehicle Roof Upholstery is clean and not torn",
                        Result: $scope.ExcelData[0].vehcileRoofUpholtseryCleanNotTorn,
                        Remarks: $scope.ExcelData[0].vehcileRoofUpholtseryCleanNotTornRemarks
                    },
                    {
                        Label: "Vehicle Floor Upholstery is clean and not torn",
                        Result: $scope.ExcelData[0].vehcileFloorUpholtseryCleanNotTorn,
                        Remarks: $scope.ExcelData[0].vehcileFloorUpholtseryCleanNotTornRemarks
                    },
                    {
                        Label: "Vehicle Dashboard is Clean ",
                        Result: $scope.ExcelData[0].vehcileDashboardClean,
                        Remarks: $scope.ExcelData[0].vehcileDashboardCleanRemarks
                    },
                    {
                        Label: "Vehicle glasses is Clean / Stain Free / No Films etc",
                        Result: $scope.ExcelData[0].vehicleGlassCleanStainFree,
                        Remarks: $scope.ExcelData[0].vehicleGlassCleanStainFreeRemarks
                    },
                    {
                        Label: "Vehicle Interior Lighting is Bright and working",
                        Result: $scope.ExcelData[0].vehicleInteriorLightBrightWorking,
                        Remarks: $scope.ExcelData[0].vehicleInteriorLightBrightWorkingRemarks
                    },
                    {
                        Label: "Bolster Seperating the Last two seats (Only in Innova)",
                        Result: $scope.ExcelData[0].bolsterSeperatingLastTwoSeats,
                        Remarks: $scope.ExcelData[0].bolsterSeperatingLastTwoSeatsRemarks
                    },
                    {
                        Label: "Audio Working Or Not",
                        Result: $scope.ExcelData[0].audioWorkingOrNot,
                        Remarks: $scope.ExcelData[0].audioWorkingOrNotRemarks
                    },
                    {
                        Label: "Scratches on the body",
                        Result: $scope.ExcelData[0].exteriorScratches,
                        Remarks: $scope.ExcelData[0].exteriorScratchesRemarks
                    },
                    {
                        Label: "No Patch Work",
                        Result: $scope.ExcelData[0].noPatchWork,
                        Remarks: $scope.ExcelData[0].noPatchWorkRemarks
                    },
                    {
                        Label: "Pedal Brake Working",
                        Result: $scope.ExcelData[0].pedalBrakeWorking,
                        Remarks: $scope.ExcelData[0].pedalBrakeWorkingRemarks
                    },
                    {
                        Label: "Hand Brake Working",
                        Result: $scope.ExcelData[0].handBrakeWorking,
                        Remarks: $scope.ExcelData[0].handBrakeWorkingRemarks
                    },
                    {
                        Label: "No Dents on the Trim of the Wheel",
                        Result: $scope.ExcelData[0].tyresNoDentsTrimWheel,
                        Remarks: $scope.ExcelData[0].tyresNoDentsTrimWheelRemarks
                    },
                    {
                        Label: "Tyres in good condition",
                        Result: $scope.ExcelData[0].tyresGoodCondition,
                        Remarks: $scope.ExcelData[0].tyresGoodConditionRemarks
                    },
                    {
                        Label: "All Tyres including Stephney Inflat",
                        Result: $scope.ExcelData[0].allTyresAndStephneyInflate,
                        Remarks: $scope.ExcelData[0].allTyresAndStephneyInflateRemarks
                    },
                    {
                        Label: "Jack & Tools",
                        Result: $scope.ExcelData[0].jackAndTool,
                        Remarks: $scope.ExcelData[0].jackAndToolRemarks
                    },
                    {
                        Label: "Wiper Working",
                        Result: $scope.ExcelData[0].wiperWorking,
                        Remarks: $scope.ExcelData[0].wiperWorkingRemarks
                    },
                    {
                        Label: "Cooling achieved end to end in 5 mns",
                        Result: $scope.ExcelData[0].acCoolingIn5mins,
                        Remarks: $scope.ExcelData[0].acCoolingIn5minsRemarks
                    },
                    {
                        Label: "No Smell in AC",
                        Result: $scope.ExcelData[0].noSmellInAC,
                        Remarks: $scope.ExcelData[0].noSmellInACRemarks
                    },
                    {
                        Label: "AC vents are clean",
                        Result: $scope.ExcelData[0].acVentsClean,
                        Remarks: $scope.ExcelData[0].acVentsCleanRemarks
                    },

                    {
                        Label: "Engine oil Change Indicator ON",
                        Result: $scope.ExcelData[0].engionOilChangeIndicatorOn,
                        Remarks: $scope.ExcelData[0].enginOilChangeIndicatorOnRemarks
                    },

                    {
                        Label: "Coolant Indicator ON",
                        Result: $scope.ExcelData[0].coolantIndicatorOn,
                        Remarks: $scope.ExcelData[0].coolantIndicatorOnRemarks
                    },
                    {
                        Label: "Brake & Clutch oil Indicator On",
                        Result: $scope.ExcelData[0].brakeClutchIndicatorOn,
                        Remarks: $scope.ExcelData[0].brakeClutchIndicatorOnRemarks
                    },
                    {
                        Label: "High Beam Working",
                        Result: $scope.ExcelData[0].highBeamWorking,
                        Remarks: $scope.ExcelData[0].highBeamWorkingRemarks
                    },
                    {
                        Label: "Low Beam Working",
                        Result: $scope.ExcelData[0].lowBeamWorking,
                        Remarks: $scope.ExcelData[0].lowBeamWorkingRemarks
                    },

                    {
                        Label: "Right Front Indicator Working",
                        Result: $scope.ExcelData[0].rightFrontIndicatorWorking,
                        Remarks: $scope.ExcelData[0].rightFromtIndicatorWorkingRemarks
                    },
                    {
                        Label: "Left Front Indicator Working",
                        Result: $scope.ExcelData[0].leftFrontIndicatorWorking,
                        Remarks: $scope.ExcelData[0].leftFrontIndicatorWorkingRemarks
                    },
                    {
                        Label: "Parking Lights Working",
                        Result: $scope.ExcelData[0].parkingLightsWorking,
                        Remarks: $scope.ExcelData[0].parkingLightsWorkingRemarks
                    },
                    {
                        Label: "LED Day Time Running Lights Working",
                        Result: $scope.ExcelData[0].ledDayTimeRunningLightWorking,
                        Remarks: $scope.ExcelData[0].ledDayTimeRunningLightWorkingRemarks
                    },
                    {
                        Label: "Right Rear Indicator Working",
                        Result: $scope.ExcelData[0].rightRearIndicatorWorking,
                        Remarks: $scope.ExcelData[0].rightRearIndicatorWorkingRemarks
                    },
                    {
                        Label: "Left Rear Indicator Working",
                        Result: $scope.ExcelData[0].leftRearIndicatorWorking,
                        Remarks: $scope.ExcelData[0].leftRearIndicatorWorkingRemarks
                    },
                    {
                        Label: "Brake Lights On",
                        Result: $scope.ExcelData[0].brakeLightsOn,
                        Remarks: $scope.ExcelData[0].brakeLightsOnRemarks
                    },
                    {
                        Label: "Reverse Lights On",
                        Result: $scope.ExcelData[0].reverseLightsOn,
                        Remarks: $scope.ExcelData[0].reverseLightsOnRemarks
                    },
                    {
                        Label: "Fog Lights",
                        Result: $scope.ExcelData[0].fogLights,
                        Remarks: $scope.ExcelData[0].fogLightsRemarks
                    },
                    {
                        Label: "Remarks",
                        Result: $scope.ExcelData[0].feedback,
                        Remarks: $scope.ExcelData[0].feedbackRemarks
                    },
                    {
                        Label: "Driver CheckIn Did Or Not",
                        Result: $scope.ExcelData[0].driverCheckInDidOrNot,
                        Remarks: $scope.ExcelData[0].driverCheckInDidOrNotRemarks
                    },
                    {
                        Label: "Horn Working",
                        Result: $scope.ExcelData[0].hornWorking,
                        Remarks: $scope.ExcelData[0].hornWorkingRemarks
                    },
                    {
                        Label: "Reflective sign board",
                        Result: $scope.ExcelData[0].reflectionSignBoard,
                        Remarks: $scope.ExcelData[0].reflectionSignBoardRemarks
                    },
                    {
                        Label: "Number of Punctures done",
                        Result: $scope.ExcelData[0].numberofPunctruresdone,
                        Remarks: " "
                    },
                    {
                        Label: "Diesel",
                        Result: $scope.ExcelData[0].diesel,
                        Remarks: " "
                    },
                    {
                        Label: "Driver Name",
                        Result: $scope.ExcelData[0].driverName,
                        Remarks: " "
                    }
                ];
                var sheetLabel = "Inspection";
                var opts = [{
                    sheetid: sheetLabel,
                    header: true
                }];
                alasql('SELECT INTO XLSX("Inspection.xlsx",?) FROM ?', [opts, [excelSingleArray]]);
            };
        }

    };



    //**********************************************ADD NEW VENDOR CONTROLLER******************************************************************
    var addVendorCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $confirm, selectedFacilityId) {
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + selectedFacilityId + "]");
        $scope.facilityData = array;
        $scope.format = 'dd-MM-yyyy';
        $scope.alertMessage;
        $scope.alertHint;
        $scope.newVendor = {
            'vendorId': '',
            'vendorName': '',
            'vendorMobileNumber': '',
            'vendorContactNumber': '',
            'vendorContactName': '',
            'email': '',
            'noOfVehicle': ''
        };

        $scope.dateOptionsFrom = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.dateOptionsTo = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.contractStartCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'contractStartDate': true
                };
            }, 50);
        };

        $scope.contractEndCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'contractEndDate': true
                };
            }, 50);
        };

        $scope.addNewVendor = function(result, combinedFacilityId) {

            var facilityBranchName = [];

            angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                var branchName = _.where(userFacilities, {
                    branchId: item
                })[0].name;
                facilityBranchName.push(branchName);
            });

            $confirm({
                text: "Are you sure you want to add this vendor to" + ' ' + String(facilityBranchName) + ' ' + "facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {

                $scope.isInvalid = false;
                if ((new Date(result.contractStartdate) * 1000) > (new Date(result.contractEndDate) * 1000)) {
                    $scope.isInvalid = true;
                } else {
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        vendorId: result.vendorId,
                        vendorName: result.vendorName,
                        vendorMobileNo: result.vendorMobileNumber,
                        emailId: result.email,
                        vendorContactName: result.vendorContactName,
                        vendorOfficeNo: result.vendorContactNumber,
                        address: result.address,
                        tinNumber: result.tinNumber,
                        vendorContactName1: result.vendorContactName1,
                        vendorContactNumber1: result.vendorContactNumber1,
                        vendorContactName2: result.vendorContactName2,
                        vendorContactNumber2: result.vendorContactNumber2,
                        vendorContactName3: result.vendorContactName3,
                        vendorContactNumber3: result.vendorContactNumber3,
                        vendorContactName4: result.vendorContactName4,
                        vendorContactNumber4: result.vendorContactNumber4,
                        emailIdLvl1: result.emailIdLvl1,
                        emailIdLvl2: result.emailIdLvl2,
                        panNumber: result.panNumber,
                        serviceTaxNumber: result.serviceTaxNumber,
                        facilityName: result.facilityName,
                        userId: profileId,
                        combinedFacility: String(combinedFacilityId),

                    };

                    $http.post('services/xlUtilityVendorUpload/addnewvendor/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == "exist") {
                                ngDialog.open({
                                    template: 'This vendorName already exist',
                                    plain: true
                                });

                            } else if (data.inputInvalid) {
                                ngDialog.open({
                                    template: data.inputInvalid,
                                    plain: true
                                });

                            } else {
                                $('.loading').show();
                                ngDialog.open({
                                    template: 'Vendor added successfully',
                                    plain: true
                                });

                                $timeout(function() {
                                    $modalInstance.close(result);
                                    ngDialog.close();
                                }, 3000);
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    var addAdhocDriverCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog) {
        $scope.adhocDriverObj = {};
        $scope.adhocDrivers = {};
        $scope.getActiveVendor = function() {
            $scope.adhocDriverselectVendor = true;
            $scope.adhocVendorVehicle = true;
            $scope.adhocvehicleandDriver = true;
            $scope.adhocDriverpart = true;
            $scope.adhocDriverpartMobile = true;
            $scope.failureMsg = false;
            $scope.failureMsg1 = false;
            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/contract/allActiveVendor/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vendorsData = data;
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };
        $scope.getActiveVendor();

        $scope.getDeviceDetail = function() {

            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/device/alldevices/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.deviceDetailData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };
        $scope.getDeviceDetail();

        $scope.vehicleNumCheck = function(vehicleNumber, vendor) {
            $scope.adhocDriverObj.driverMobileNo = '';
            $scope.adhocDriverObj.driverName = '';
            if (vehicleNumber == undefined) {
                $scope.adhocDriverpartMobile = true;
            }

            if (vehicleNumber) {
                $scope.adhocvehicleNumber = vehicleNumber;
                $scope.adhocvendor = vendor;
                var data = {
                    vendorId: vendor.vendorId,
                    vehicleNumber: vehicleNumber,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/vendor/vehicleDetailByVendorId/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.adhocvehicleID = data.vehicleId;
                        if (data.status == "success") {
                            $scope.adhocvehicleandDriver = false;
                            $scope.failureMsg = false;
                            $scope.adhocDriverpartMobile = false;
                            // $scope.adhocDriverselectVendor= false;
                            $scope.adhocDriverData = data;
                            $scope.adhocDrivers = {
                                selectVendor: $scope.adhocvendor,
                                registerNumber: $scope.adhocvehicleNumber
                            }
                        } else if (data.status == "notApproved") {

                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsg = true;
                            $scope.adhocDriverpartMobile = true;
                            $scope.failureMsgdata = "Vehicle already exist in sytem, but not approved -Please approve it";
                        } else if (data.status == "vehicleRejected") {
                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsg = true;
                            $scope.adhocDriverpartMobile = true;
                            $scope.failureMsgdata = "Vehicle removed by team -Please add again";
                        } else if (data.status == "existDiffVen") {
                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsg = true;
                            $scope.adhocDriverpartMobile = true;
                            $scope.failureMsgdata = "This vehicle register with some other vendor name -Please check";
                        } else if (data.status == "fail") {

                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsg = true;
                            $scope.adhocDriverpartMobile = true;
                            $scope.failureMsgdata = "Vehicle not exit in to system";
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }


        };
        $scope.driverNumCheck = function(driverNumber, vendor) {

            $scope.adhocDrivers.driverName = '';

            if (driverNumber == undefined) {
                $scope.adhocDriverpart = true;
            }

            if (driverNumber) {
                var data = {
                    vendorId: vendor.vendorId,
                    mobileNumber: driverNumber,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };

                $http.post('services/vendor/driverDetailByVendorId/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.driverID = data.driverId;
                        if (data.status == "success") {
                            $scope.datadriverDetail = data;
                            $scope.failureMsgForDriver = false;
                            $scope.adhocDriverpartMobile = false;
                            $scope.adhocDriverselectVendor = false;
                            $scope.adhocDriverObj = {
                                // selectVendor:$scope.adhocvendor,
                                // registerNumber:$scope.adhocvehicleNumber,
                                driverName: $scope.datadriverDetail.driverName,
                                driverMobileNo: parseInt($scope.datadriverDetail.driverNumber)
                            }
                        } else if (data.status == "notApproved") {
                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsgForDriver = true;
                            $scope.failureMsgdataForDriver = "Driver already exist in sytem, but not approved -Please approve it";
                        } else if (data.status == "driverRejected") {
                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsgForDriver = true;
                            $scope.failureMsgdataForDriver = "Driver removed by team -Please add again";
                        } else if (data.status == "existDiffVen") {
                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsgForDriver = true;
                            $scope.failureMsgdataForDriver = "This Driver register with some other vendor name -Please check";
                        } else if (data.status == "fail") {
                            $scope.adhocDriverObj = {
                                driverMobileNo: driverNumber
                            }
                            $scope.adhocDriverselectVendor = true;
                            $scope.failureMsgForDriver = true;
                            $scope.failureMsgdataForDriver = "Driver not exit in to system";
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

        }
        $scope.vendorDatacheck = function(data) {
            $scope.adhocDriverObj = {};
            $scope.failureMsgForDriver = false;
            $scope.adhocDriverpartMobile = true;
            if (data) {
                $scope.adhocDrivers = {
                    selectVendor: data
                };
                $scope.adhocvehicleandDriver = false;
                $scope.failureMsg = false;
                $scope.adhocVendorVehicle = false;
                // $scope.adhocDriverselectVendor= false;
                $scope.adhocDriverpart = false;

            } else {
                $scope.adhocDrivers = {
                    selectVendor: data
                };
                $scope.adhocvehicleandDriver = true;
                $scope.failureMsg = false;
                $scope.adhocVendorVehicle = true;
                $scope.adhocDriverselectVendor = true;
                $scope.adhocDriverpart = true;
            }

        }


        $scope.adhocvehicleDriver = function(adhocData, adhocDriverObj) {
            if (adhocData.deviceId) {
                $scope.deviceIDforadhoc = adhocData.deviceId.deviceId;
            } else {
                $scope.deviceIDforadhoc = "No Device";
            }

            var data = {
                deviceId: $scope.deviceIDforadhoc,
                vehicleNumber: adhocData.registerNumber,
                mobileNumber: adhocDriverObj.driverMobileNo,
                vendorId: adhocData.selectVendor.vendorId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            // alert(JSON.stringify(data));              
            $http.post('services/vendor/vehicleDriverCheckIn/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "success") {
                        ngDialog.open({
                            template: 'Vehicle and driver CheckedIn successfully',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close();
                            ngDialog.close();
                        }, 3000);
                    } else if (data.status == "VcheckedIn") {
                        ngDialog.open({
                            template: 'This vehicle already checkedIn',
                            plain: true
                        });

                    } else if (data.status == "DcheckedIn") {
                        ngDialog.open({
                            template: 'This driver already checkedIn',
                            plain: true
                        });

                    } else if (data.status == "deviceCheckedIn") {
                        ngDialog.open({
                            template: 'This device already checkedIn',
                            plain: true
                        });

                    } else if (data.status == "mismatch") {
                        ngDialog.open({
                            template: 'Vehicle and driver not belongs to same vendor',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close();
                            ngDialog.close();
                        }, 3000);
                    } else if (data.status == "checkedIn") {
                        ngDialog.open({
                            template: 'Vehicle and driver Checked successfully with no device',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close();
                            ngDialog.close();
                        }, 3000);
                    }
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    }


    var addAdhocVendorCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog) {
        $scope.adhocDriverObj = {};
        $scope.adhocDriver = {};
        $scope.setMinDate = new Date();
        $scope.failureMsg = false;
        $scope.failureMsg1 = false;


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

        $scope.getActiveVendors = function() {
            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/contract/allActiveVendor/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vendorsData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };
        $scope.getActiveVendors();

        $scope.vendorDatacondition = function(data) {
            if (data) {

            } else {
                $scope.adhocDriverselectVendor = true;
                $scope.adhocDriverpartMobile = true;
                $scope.adhocDriverpart = true;
                $scope.checkBox = true;
                $scope.adhocVendorVehicle = true;
            }

        }


        $scope.deviceCheck = function(adhocDriver) {
            if ("withDevice" == adhocDriver) {
                $scope.withoutDevice = false;
            } else {
                $scope.withoutDevice = true;
            }

        }
        $scope.vehicleNumberCheck = function(vehicleNumber, vendor) {
            if (vehicleNumber == undefined) {
                $scope.adhocDriverselectVendor = true;
            }
            if (vehicleNumber) {
                if ($scope.adhocvehicleNumber && $scope.adhocvendor) {
                    if ($scope.adhocvehicleNumber == vehicleNumber && $scope.adhocvendor == vendor) {

                    } else {
                        $scope.adhocvehicleNumber = vehicleNumber;
                        $scope.adhocvendor = vendor;
                        var data = {
                            vendorId: vendor.vendorId,
                            vehicleNumber: vehicleNumber,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/vendor/vehicleDetailByVendorId/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $scope.adhocDriverselectVendor = true;
                                    $scope.adhocVendorVehicle = false;
                                    $scope.failureMsg = false;
                                    $scope.adhocDriverpartMobile = false;
                                    $scope.checkBox = false;
                                    $scope.adhocDriverData = data;
                                    $scope.adhocDriver = {
                                        PermitValid: $scope.adhocDriverData.permitValidity,
                                        TaxValid: $scope.adhocDriverData.taxValidity,
                                        insuranceValid: $scope.adhocDriverData.insuranceValidity,
                                        pollutionValid: $scope.adhocDriverData.polutionValidity,
                                        seatCapacity: String($scope.adhocDriverData.capacity),
                                        vehicleModel: $scope.adhocDriverData.vehicleModel,
                                        selectVendor: $scope.adhocvendor,
                                        registerNumber: $scope.adhocvehicleNumber
                                    }

                                } else if (data.status == "notApproved") {
                                    $scope.adhocDriver = {
                                        selectVendor: vendor,
                                        registerNumber: vehicleNumber
                                    };
                                    $scope.adhocDriverselectVendor = true;
                                    $scope.failureMsg = true;
                                    $scope.adhocDriverpart = true;
                                    $scope.adhocDriverpartMobile = true;
                                    $scope.checkBox = true;
                                    $scope.failureMsgdata = "Vehicle already exist in system, but not approved -Please approve it";
                                } else if (data.status == "vehicleRejected") {
                                    $scope.adhocDriver = {
                                        selectVendor: vendor,
                                        registerNumber: vehicleNumber
                                    };
                                    $scope.adhocDriverselectVendor = true;
                                    $scope.failureMsg = true;
                                    $scope.adhocDriverpart = true;
                                    $scope.adhocDriverpartMobile = true;
                                    $scope.checkBox = true;
                                    $scope.failureMsgdata = "Vehicle removed by team -Please add again";
                                } else if (data.status == "existDiffVen") {
                                    $scope.adhocDriver = {
                                        selectVendor: vendor,
                                        registerNumber: vehicleNumber
                                    };
                                    $scope.adhocDriverselectVendor = true;
                                    $scope.failureMsg = true;
                                    $scope.adhocDriverpart = true;
                                    $scope.adhocDriverpartMobile = true;
                                    $scope.checkBox = true;
                                    $scope.failureMsgdata = "This vehicle register with some other vendor name -Please check";
                                } else if (data.status == "fail") {
                                    $scope.adhocDriverselectVendor = false;
                                    $scope.adhocDriverpartMobile = false;
                                    $scope.failureMsg = false;
                                    $scope.checkBox = false;
                                    $scope.adhocDriver = {
                                        selectVendor: vendor,
                                        registerNumber: vehicleNumber
                                    };
                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    }
                } else {
                    $scope.adhocvehicleNumber = vehicleNumber;
                    $scope.adhocvendor = vendor;
                    var data = {
                        vendorId: vendor.vendorId,
                        vehicleNumber: vehicleNumber,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/vendor/vehicleDetailByVendorId/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == "success") {
                                $scope.adhocDriverselectVendor = true;
                                $scope.adhocVendorVehicle = false;
                                $scope.failureMsg = false;
                                $scope.adhocDriverpart = false;
                                $scope.adhocDriverpartMobile = false;
                                $scope.checkBox = false;
                                $scope.adhocDriverData = data;
                                $scope.adhocDriver = {
                                    PermitValid: $scope.adhocDriverData.permitValidity,
                                    TaxValid: $scope.adhocDriverData.taxValidity,
                                    insuranceValid: $scope.adhocDriverData.insuranceValidity,
                                    pollutionValid: $scope.adhocDriverData.polutionValidity,
                                    seatCapacity: String($scope.adhocDriverData.capacity),
                                    vehicleModel: $scope.adhocDriverData.vehicleModel,
                                    selectVendor: $scope.adhocvendor,
                                    registerNumber: $scope.adhocvehicleNumber
                                }

                            } else if (data.status == "notApproved") {
                                $scope.adhocDriver = {
                                    selectVendor: vendor,
                                    registerNumber: vehicleNumber
                                };
                                $scope.adhocDriverselectVendor = true;
                                $scope.failureMsg = true;
                                $scope.adhocDriverpart = true;
                                $scope.adhocDriverpartMobile = true;
                                $scope.checkBox = true;
                                $scope.failureMsgdata = "Vehicle already exist in sytem, but not approved -Please approve it";
                            } else if (data.status == "vehicleRejected") {
                                $scope.adhocDriver = {
                                    selectVendor: vendor,
                                    registerNumber: vehicleNumber
                                };
                                $scope.adhocDriverselectVendor = true;
                                $scope.failureMsg = true;
                                $scope.adhocDriverpart = true;
                                $scope.adhocDriverpartMobile = true;
                                $scope.checkBox = true;
                                $scope.failureMsgdata = "Vehicle removed by team -Please add again";
                            } else if (data.status == "existDiffVen") {
                                $scope.adhocDriver = {
                                    selectVendor: vendor,
                                    registerNumber: vehicleNumber
                                };
                                $scope.adhocDriverselectVendor = true;
                                $scope.failureMsg = true;
                                $scope.adhocDriverpart = true;
                                $scope.adhocDriverpartMobile = true;
                                $scope.checkBox = true;
                                $scope.failureMsgdata = "This vehicle register with some other vendor name -Please check";
                            } else if (data.status == "fail") {
                                $scope.adhocDriverselectVendor = false;
                                $scope.adhocDriverpartMobile = false;
                                $scope.failureMsg = false;
                                $scope.checkBox = false;
                                $scope.adhocDriver = {
                                    selectVendor: vendor,
                                    registerNumber: vehicleNumber
                                };
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }

            }


        };
        $scope.vendorDatacheck = function(data) {
            $scope.adhocDriverObj = {};
            $scope.failureMsg = false;
            $scope.failureMsgForDriver = false;
            if (data) {
                $scope.adhocDriver = {
                    selectVendor: data
                };
                $scope.adhocDriverselectVendor = true;
                $scope.adhocDriverpart = true;
                $scope.checkBox = false;
                $scope.withoutDevice = false;
                $scope.adhocVendorVehicle = false;
                $scope.adhocDriverpartMobile = false;
            } else {
                $scope.adhocDriver = {
                    selectVendor: data
                };
                $scope.adhocDriverselectVendor = true;
                $scope.adhocDriverpartMobile = true;
                $scope.adhocDriverpart = true;
                $scope.checkBox = true;
                $scope.withoutDevice = false;
                $scope.adhocVendorVehicle = true;
            }

        }
        $scope.driverNumberCheck = function(driverNumber, vendor) {

            if (driverNumber == undefined) {
                $scope.adhocDriverpart = true;
            }

            if (driverNumber) {
                if ($scope.defaultdriverNumber && $scope.defaultVendor) {
                    if ($scope.defaultdriverNumber == driverNumber && $scope.defaultVendor == vendor) {

                    } else {
                        $scope.defaultdriverNumber = driverNumber;
                        $scope.defaultVendor = vendor;
                        var data = {
                            vendorId: vendor.vendorId,
                            mobileNumber: driverNumber,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/vendor/driverDetailByVendorId/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == "success") {
                                    $scope.adhocDriverpart = true;
                                    $scope.adhocDriverpartMobile = false;
                                    $scope.failureMsgForDriver = false;
                                    $scope.adhocDriverObj = {
                                        driverName: data.driverName,
                                        driverMobileNo: parseInt(data.driverNumber),
                                        licenceNumber: data.licenceNum,
                                        licenceValidity: data.licenceValidity
                                    }

                                } else if (data.status == "notApproved") {
                                    $scope.adhocDriverObj = {
                                        driverMobileNo: driverNumber
                                    }
                                    $scope.adhocDriverpart = true;
                                    $scope.failureMsgForDriver = true;
                                    $scope.failureMsgdataForDriver = "Driver already exist in sytem, but not approved -Please approve it";
                                } else if (data.status == "driverRejected") {
                                    $scope.adhocDriverObj = {
                                        driverMobileNo: driverNumber
                                    }
                                    $scope.adhocDriverpart = true;
                                    $scope.failureMsgForDriver = true;
                                    $scope.failureMsgdataForDriver = "Driver removed by team -Please add again";
                                } else if (data.status == "existDiffVen") {
                                    $scope.adhocDriverObj = {
                                        driverMobileNo: driverNumber
                                    }
                                    $scope.adhocDriverpart = true;
                                    $scope.failureMsgForDriver = true;
                                    $scope.failureMsgdataForDriver = "This Driver register with some other vendor name -Please check";
                                } else if (data.status == "fail") {
                                    $scope.adhocDriverpart = false;
                                    $scope.failureMsgForDriver = false;
                                    $scope.adhocDriverObj = {
                                        driverMobileNo: driverNumber
                                    }
                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    }
                } else {
                    $scope.defaultdriverNumber = driverNumber;
                    var data = {
                        vendorId: vendor.vendorId,
                        mobileNumber: driverNumber,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/vendor/driverDetailByVendorId/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == "success") {
                                $scope.adhocDriverpart = true;
                                $scope.adhocDriverpartMobile = false;
                                $scope.failureMsgForDriver = false;
                                $scope.adhocDriverObj = {
                                    driverName: data.driverName,
                                    driverMobileNo: data.driverNumber,
                                    licenceNumber: data.licenceNum,
                                    licenceValidity: data.licenceValidity
                                }

                            } else if (data.status == "notApproved") {
                                $scope.adhocDriverObj = {
                                    driverMobileNo: driverNumber
                                }
                                $scope.adhocDriverpart = true;
                                $scope.failureMsgForDriver = true;
                                $scope.failureMsgdataForDriver = "Driver already exist in sytem, but not approved -Please approve it";
                            } else if (data.status == "driverRejected") {
                                $scope.adhocDriverObj = {
                                    driverMobileNo: driverNumber
                                }
                                $scope.adhocDriverpart = true;
                                $scope.failureMsgForDriver = true;
                                $scope.failureMsgdataForDriver = "Driver removed by team -Please add again";
                            } else if (data.status == "existDiffVen") {
                                $scope.adhocDriverObj = {
                                    driverMobileNo: driverNumber
                                }
                                $scope.adhocDriverpart = true;
                                $scope.failureMsgForDriver = true;
                                $scope.failureMsgdataForDriver = "This Driver register with some other vendor name -Please check";
                            } else if (data.status == "fail") {
                                $scope.adhocDriverpart = false;
                                $scope.failureMsgForDriver = false;
                                $scope.adhocDriverObj = {
                                    driverMobileNo: driverNumber
                                }
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }

            }


        }

        $scope.PermitValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'PermitValidDate': true
                };
            }, 50);
        };
        $scope.pollutionValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'PollutionValidDate': true
                };
            }, 50);
        };
        $scope.insurValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'insuranceValidDate': true
                };
            }, 50);
        };

        $scope.TaxValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'TaxValidDate': true
                };
            }, 50);
        };

        $scope.licenceValidity = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'licenceValidity': true
                };
            }, 50);
        };


        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.format = 'dd-MM-yyyy';

        $scope.addAdhocDriverDetail = function(adhocDriver, adhocDriverObj) {

            $scope.withoutDevice = false;
            $scope.withoutDevice = adhocDriver.withoutDevice;

            if ($scope.withoutDevice) {
                $scope.deviceDetail = "withoutDevice"
            } else {
                $scope.deviceDetail = "withDevice"
            }
            if ($scope.adhocDriverselectVendor && $scope.adhocDriverpart) {
                var data = {
                    branchId: branchId,
                    vendorId: adhocDriver.selectVendor.vendorId,
                    vehicleModel: adhocDriver.vehicleModel,
                    capacity: adhocDriver.seatCapacity,
                    vehicleNumber: adhocDriver.registerNumber,
                    permitValid: adhocDriver.PermitValid,
                    taxValidDate: adhocDriver.TaxValid,
                    insuranceValid: adhocDriver.insuranceValid,
                    polutionDate: adhocDriver.pollutionValid,
                    licenceValidDate: adhocDriverObj.licenceValidity,
                    firstName: adhocDriverObj.driverName,
                    mobileNumber: adhocDriverObj.driverMobileNo,
                    licenceNumber: adhocDriverObj.licenceNumber,
                    actionType: $scope.deviceDetail,
                    userId: profileId

                };
            } else if ($scope.adhocDriverselectVendor) {
                var data = {
                    branchId: branchId,
                    vendorId: adhocDriver.selectVendor.vendorId,
                    vehicleModel: adhocDriver.vehicleModel,
                    capacity: adhocDriver.seatCapacity,
                    vehicleNumber: adhocDriver.registerNumber,
                    permitValid: adhocDriver.PermitValid,
                    taxValidDate: adhocDriver.TaxValid,
                    insuranceValid: adhocDriver.insuranceValid,
                    polutionDate: adhocDriver.pollutionValid,
                    licenceValidDate: convertDateUTC(adhocDriverObj.licenceValidity),
                    firstName: adhocDriverObj.driverName,
                    mobileNumber: adhocDriverObj.driverMobileNo,
                    licenceNumber: adhocDriverObj.licenceNumber,
                    actionType: $scope.deviceDetail,
                    userId: profileId

                };

            } else if ($scope.adhocDriverpart) {
                var data = {
                    branchId: branchId,
                    vendorId: adhocDriver.selectVendor.vendorId,
                    vehicleModel: adhocDriver.vehicleModel,
                    capacity: adhocDriver.seatCapacity,
                    vehicleNumber: adhocDriver.registerNumber,
                    permitValid: convertDateUTC(adhocDriver.PermitValid),
                    taxValidDate: convertDateUTC(adhocDriver.TaxValid),
                    insuranceValid: convertDateUTC(adhocDriver.insuranceValid),
                    polutionDate: convertDateUTC(adhocDriver.pollutionValid),
                    licenceValidDate: adhocDriverObj.licenceValidity,
                    firstName: adhocDriverObj.driverName,
                    mobileNumber: adhocDriverObj.driverMobileNo,
                    licenceNumber: adhocDriverObj.licenceNumber,
                    actionType: $scope.deviceDetail,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
            } else {
                var data = {
                    branchId: branchId,
                    vendorId: adhocDriver.selectVendor.vendorId,
                    vehicleModel: adhocDriver.vehicleModel,
                    capacity: adhocDriver.seatCapacity,
                    vehicleNumber: adhocDriver.registerNumber,
                    permitValid: convertDateUTC(adhocDriver.PermitValid),
                    taxValidDate: convertDateUTC(adhocDriver.TaxValid),
                    insuranceValid: convertDateUTC(adhocDriver.insuranceValid),
                    polutionDate: convertDateUTC(adhocDriver.pollutionValid),
                    licenceValidDate: convertDateUTC(adhocDriverObj.licenceValidity),
                    firstName: adhocDriverObj.driverName,
                    mobileNumber: adhocDriverObj.driverMobileNo,
                    licenceNumber: adhocDriverObj.licenceNumber,
                    actionType: $scope.deviceDetail,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
            }



            $http.post('services/vendor/addingAdhocDetails/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "success") {
                        ngDialog.open({
                            template: 'Vehicle and driver added successfully',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close();
                            ngDialog.close();
                        }, 3000);
                    } else if (data.status == "bothExists") {
                        ngDialog.open({
                            template: 'Both driver and vehicle exist in the system,So No need to add it again.',
                            plain: true
                        });

                    } else if (data.status == "VcheckedIn") {
                        ngDialog.open({
                            template: 'This vehicle already checkedIn',
                            plain: true
                        });

                    } else if (data.status == "DcheckedIn") {
                        ngDialog.open({
                            template: 'This driver already checkedIn',
                            plain: true
                        });

                    } else if (data.status == "mismatch") {
                        ngDialog.open({
                            template: 'Vehicle and driver not belongs to same vendor',
                            plain: true
                        });

                    } else if (data.status == "checkedIn") {
                        ngDialog.open({
                            template: 'Vehicle and driver Checked successfully with no device',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close();
                            ngDialog.close();
                        }, 3000);
                    }
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    }

    //**********************************************ADD NEW DRIVER CONTROLLER******************************************************************
    var newDriverVendorCtrl = function($scope, $modalInstance, $state, $http, $timeout, masterVendor, ngDialog) {
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.maxdate = new Date();
        $scope.newDriver = {
            'driverId': '',
            'driverName': '',
            'driverAddress': '',
            'driverdob': '',
            'InsuranceValid': '',
            'licenceValid': '',
            'licenceNumber': '',
            'mobileNumber': ''
        };


        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.format = 'dd-MM-yyyy';

        $scope.insurranceExpDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'insurranceExpDate': true
                };
            }, 50);
        };

        $scope.openExpDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedExpDate': true
                };
            }, 50);
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

        $scope.openDDTCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedDDT': true
                };
            }, 50);
        };

        $scope.openBatchCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedBatch': true
                };
            }, 50);
        };

        $scope.openBatchNumCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedBatchNum': true
                };
            }, 50);
        };

        $scope.openPoliceVerificationCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedPoliceVerification': true
                };
            }, 50);
        };

        $scope.openMedicalExpiryCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedMedicalExpiry': true
                };
            }, 50);
        };

        $scope.openAntiExpiryCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedAntiExpiry': true
                };
            }, 50);
        };

        $scope.openJoiningCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedJoining': true
                };
            }, 50);
        };

        $scope.openbadgeValidity = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'badgeValidity': true
                };
            }, 50);
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


        $scope.addDriver = function(result) {
            $scope.isInvalid = false;
            if ((new Date(result.issDate) * 1000) > (new Date(result.expDate) * 1000)) {
                $scope.isInvalid = true;
            } else {
                var data = {
                    efmFmVendorMaster: {
                        vendorName: masterVendor,
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    firstName: result.driverName,
                    address: result.driverAddress,
                    dob: result.driverdob,
                    licenceValid: result.licenceValid,
                    licenceNumber: result.licenceNumber,
                    mobileNumber: result.mobileNumber,
                    batchNumber: result.driverBatchNum,
                    ddtValidDate: result.driverDDT,
                    batchDate: result.driverBatch,
                    policeVerificationValid: result.driverPoliceVerification,
                    medicalFitnessCertificateValid: result.driverMedicalExpiry,
                    driverAntiExpiry: result.driverAntiExpiry,
                    dateOfJoining: result.driverJoining,
                    badgeValidity: result.badgeValidity,
                    permanentAddress: result.permanentAddress,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $http.post('services/xlUtilityDriverUpload/addnewdriver/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == "vnexist") {
                            ngDialog.open({
                                template: 'Please check this vendor name not exist in system',
                                plain: true
                            });

                        } else if (data.status == "lexist") {
                            ngDialog.open({
                                template: 'Please check this licence number already exist in system.',
                                plain: true
                            });

                        } else if (data.status == "mexist") {
                            ngDialog.open({
                                template: 'Please check this mobile number already exist in system.',
                                plain: true
                            });

                        } else if (data.status == "fail") {
                            ngDialog.open({
                                template: 'Please check something went wrong.',
                                plain: true
                            });

                        } else {
                            $('.loading').show();
                            result.driverId = data.driverId;
                            ngDialog.open({
                                template: 'Driver added successfully',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.close(result);
                                ngDialog.close();
                            }, 3000);
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    //**********************************************ADD NEW VEHICLE CONTROLLER******************************************************************
    var newVehicleVendorCtrl = function($scope, $modalInstance, $state, $http, $timeout, masterVendor, ngDialog, contractTypeList) {
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.allContractType = contractTypeList;
        $scope.paginations = [{
                'value': 1,
                'text': '1 record'
            },
            {
                'value': 10,
                'text': '10 records'
            },
            {
                'value': 15,
                'text': '15 record'
            },
            {
                'value': 20,
                'text': '20 records'
            }
        ];

        $scope.selectVendors = [{
                'value': 'Fast Track'
            },
            {
                'value': 'BHARATHICALLTAXI'
            },
            {
                'value': 'OLACABS'
            },
            {
                'value': 'Meru Call Taxi'
            },
            {
                'value': 'Golden Call Taxi'
            },
            {
                'value': 'Yellow Call Taxi'
            }
        ];

        $scope.selectSeats = [{
                'value': 2,
                'text': '2 Seater'
            },
            {
                'value': 4,
                'text': '4 Seater'
            },
            {
                'value': 7,
                'text': '7 Seater'
            },
            {
                'value': 9,
                'text': '9 Seater'
            },
            {
                'value': 11,
                'text': '11+ Seater'
            }
        ];

        $scope.newVehicle = {
            'vehicleId': '',
            'vehicleOwnerName': '',
            'vehicleNumber': '',
            'PermitValid': '',
            'contactNo': '',
            'contactName': '',
            'capacity': '',
            'regCert': '',
            'pollutionCert': '',
            'pollutionExpDate': '',
            'modelYear': '',
            'InsuranceNumber': '',
            'InsuranceDate': '',
            'roadTax': '',
            'selectDrivers': '',
            'isReplacement': '',
            'mobileNumber': ''
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.format = 'dd-MM-yyyy';


        $scope.vehicleModelYearCheck = function(modelYear) {
            var todayDate = new Date();
            $scope.currentYear = todayDate.getFullYear();
            $scope.invalidYear = false;
            if ($scope.newVehicle.modelYear > $scope.currentYear) {
                $scope.invalidYear = true;
                $scope.modelFailureMsg = "Invalid Year";
            }
        };

        $scope.pollutionExpDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'pollutionExpDate': true
                };
            }, 50);
        };

        $scope.InsuranceExpiryCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'InsuranceExpiryDate': true
                };
            }, 50);
        };

        $scope.TaxValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'TaxValidDate': true
                };
            }, 50);
        };

        $scope.vehicleFitnessCertCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'vehicleFitnessCert': true
                };
            }, 50);
        };

        $scope.registrationDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'registrationDate': true
                };
            }, 50);
        };


        $scope.vehicleMaintenacedateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'vehicleMaintenaceDate': true
                };
            }, 50);
        }

        $scope.PermitValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'PermitValidDate': true
                };
            }, 50);
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

        $scope.addVehicle = function(result) {

            var data = {
                efmFmVendorMaster: {
                    vendorName: masterVendor,
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                vehicleMake: result.vehicleName,
                vehicleNumber: result.vehicleNumber,
                vehicleEngineNumber: result.contactNo,
                vehicleOwnerName: result.contactName,
                capacity: result.capacity,
                registartionCertificateNumber: result.regCert,
                vehicleModel: result.pollutionCert,
                polutionValid: result.pollutionExpDate,
                vehicleModelYear: result.modelYear,
                taxCertificateValid: result.TaxValid,
                insuranceValidDate: result.InsuranceDate,
                status: 'P',
                contractDetailId: result.conTariffId,
                permitValidDate: result.PermitValid,
                registrationDate: result.registrationDate,
                vehicleFitnessDate: result.vehicleFitnessCert,
                vehicleMaintenaneDate: result.vehicleMaintenaceDate,
                mileage: result.mileage,
                userId: profileId,
                combinedFacility: combinedFacility

            };
            $http.post('services/xlUtilityVehicleUpload/addnewvehicle/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.inputInvalid) {
                        ngDialog.open({
                            template: data.inputInvalid,
                            plain: true
                        });
                    } else if (data.status == "vnExist") {
                        ngDialog.open({
                            template: 'This vendor name not exist in system please check.',
                            plain: true
                        });
                    } else if (data.status == "vNumExist") {
                        ngDialog.open({
                            template: 'This vehicleNumber already exist in system',
                            plain: true
                        });
                    } else if (data.status == "vEngineNumExist") {
                        ngDialog.open({
                            template: 'This vehicle engine number already exist in system.',
                            plain: true
                        });
                    } else if (data.status == "vRcNumExist") {
                        ngDialog.open({
                            template: 'This vehicleRegistration (RC) number already exist in system.',
                            plain: true
                        });
                    } else {
                        $('.loading').show();
                        result.vehicleId = data.vehicleId;
                        ngDialog.open({
                            template: 'Vehicle added successfully.',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(result);
                            ngDialog.close();
                        }, 3000);
                    }
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    //**********************************************ADD NEW ESCORT CONTROLLER******************************************************************
    var newEscortCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, vendorContractManagData, $confirm, combinedFacilityId) {
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = combinedFacilityId;
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.vendorContractManagData = vendorContractManagData;

        $scope.newEscort = {
            'escortId': '',
            'escortName': '',
            'escortMobileNumber': '',
            'escortdob': '',
            'escortAddress': '',
            'escortBatchNumber': '',
            'escortVendorName': ''
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };

        $scope.format = 'dd-MM-yyyy';

        $scope.dobCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'dobDate': true
                };
            }, 50);
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

        //Convert the dates in DD-MM-YYYY format
        var convertDateUTCYear = function(date) {
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

        $scope.addEscort = function(result, combinedFacilityId) {

            var facilityBranchName = [];

            angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                var branchName = _.where(userFacilities, {
                    branchId: item
                })[0].name;
                facilityBranchName.push(branchName);
            });

            $confirm({
                text: "Are you sure you want to add this escort to" + ' ' + String(facilityBranchName) + ' ' + "facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {

                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    firstName: result.escortName,
                    fatherName: result.escortFatherName,
                    pincode: result.pincode,
                    dateOfBirth: convertDateUTCYear(result.escortdob),
                    mobileNumber: result.escortMobileNumber,
                    address: result.escortAddress,
                    permanentAddress: result.permanentAddress,
                    escortEmployeeId: result.escortBatchNumber,
                    vendorName: result.escortVendorName,
                    userId: profileId,
                    facilityName: result.facilityName,
                    combinedFacility: String(combinedFacilityId)
                };


                $http.post('services/escort/addEscortDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == 'vnotExist') {
                            ngDialog.open({
                                template: 'Please check VendorName not exist in system.',
                                plain: true
                            });
                        } else if (data.status == 'mExist') {
                            ngDialog.open({
                                template: 'Escort mobile number already exist in system.',
                                plain: true
                            });
                        } else if (data.inputInvalid) {
                            ngDialog.open({
                                template: data.inputInvalid,
                                plain: true
                            });

                        } else {
                            $('.loading').show();
                            ngDialog.open({
                                template: 'Escort added successfully',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.close(data);
                                ngDialog.close();
                            }, 3000);
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    /***********************************************EDIT VENDOR CONTROLLER******************************************************************/
    var editVendorCtrl = function($scope, $modalInstance, $state, $http, vendor, $timeout, ngDialog, $confirm, selectedFacilityId) {
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + selectedFacilityId + "]");
        $scope.facilityData = array;
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        var extension;
        $scope.editVendor = {
            'vendorId': vendor.vendorId,
            'vendorName': vendor.vendorName,
            'vendorMobileNumber': vendor.vendorMobileNumber,
            'vendorContactNumber': vendor.vendorContactNumber,
            'vendorContactName': vendor.vendorContactName,
            'address': vendor.vendorAddress,
            'numDays': '',
            'tinNumber': vendor.tinNumber,
            'email': vendor.emailId,
            'contractStartdate': vendor.contractStartDate,
            'contractEndDate': vendor.contractEndDate,
            'noOfVehicle': vendor.noOfVehicle,
            'noOfDriver': vendor.noOfDriver,
            'vendorContactName1': vendor.vendorContactName1,
            'vendorContactNumber1': vendor.vendorContactNumber1,
            'vendorContactName2': vendor.vendorContactName2,
            'vendorContactNumber2': vendor.vendorContactNumber2,
            'vendorContactName3': vendor.vendorContactName3,
            'vendorContactNumber3': vendor.vendorContactNumber3,
            'vendorContactName4': vendor.vendorContactName4,
            'vendorContactNumber4': vendor.vendorContactNumber4,
            'emailIdLvl1': vendor.emailIdLvl1,
            'emailIdLvl2': vendor.emailIdLvl2,
            'panNumber': vendor.panNumber,
            'serviceTaxNumber': vendor.serviceTaxNumber
        };

        $scope.vendorDocuments = [{
                name: 'test1',
                location: 'temp/test1.pdf',
                imgSrc: 'images/docImg.png'
            },
            {
                name: 'test2',
                location: 'temp/test2.xls',
                imgSrc: 'images/docImg.png'
            },
            {
                name: 'test3',
                location: 'temp/test3.docx',
                imgSrc: 'images/docImg.png'
            }
        ];
        angular.forEach($scope.vendorDocuments, function(item) {
            extension = item.location.split('.').pop();
            switch (extension) {
                case 'pdf':
                    item.imgSrc = 'images/adobeAcrobat.png';
                    break;
                case 'docx':
                    item.imgSrc = 'images/msWord.png';
                    break;
                case 'xls':
                    item.imgSrc = 'images/msExcel.png';
                    break;
                default:
                    item.imgSrc = 'images/docImg.png';
            }
        });


        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };

        $scope.format = 'dd-MM-yyyy';

        $scope.contractStartCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'contractStartDate': true
                };
            }, 50);
        };

        $scope.contractEndCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'contractEndDate': true
                };
            }, 50);
        };

        $scope.deleteDocument = function(index) {
            $confirm({
                    text: 'Are you sure you want to delete this docuument?',
                    title: 'Delete Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                })
                .then(function() {
                    $('#div' + index).hide('slow');
                });
        };

        $scope.saveVendor = function(result, combinedFacilityId) {

            var facilityBranchName = [];

            angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                var branchName = _.where(userFacilities, {
                    branchId: item
                })[0].name;
                facilityBranchName.push(branchName);
            });

            $confirm({
                text: "Are you sure you want to add this vendor to" + ' ' + String(facilityBranchName) + ' ' + "facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {

                var data = {
                    vendorId: result.vendorId,
                    vendorName: result.vendorName,
                    vendorMobileNo: result.vendorMobileNumber,
                    emailId: result.email,
                    vendorContactName: result.vendorContactName,
                    vendorOfficeNo: result.vendorContactNumber,
                    address: result.address,
                    vendorContactName1: result.vendorContactName1,
                    vendorContactNumber1: result.vendorContactNumber1,
                    vendorContactName2: result.vendorContactName2,
                    vendorContactNumber2: result.vendorContactNumber2,
                    vendorContactName3: result.vendorContactName3,
                    vendorContactNumber3: result.vendorContactNumber3,
                    vendorContactName4: result.vendorContactName4,
                    vendorContactNumber4: result.vendorContactNumber4,
                    emailIdLvl1: result.emailIdLvl1,
                    tinNumber: result.tinNumber,
                    emailIdLvl2: result.emailIdLvl2,
                    panNumber: result.panNumber,
                    serviceTaxNumber: result.serviceTaxNumber,
                    vendorContractId: result.vendorContractId,
                    userId: profileId,
                    combinedFacility: combinedFacility,
                    facilityName: result.facilityName
                };

                $http.post('services/vendor/modifyVendorDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == "exist") {
                            ngDialog.open({
                                template: 'This vendorName already exist',
                                plain: true
                            });

                        } else if (data == "Success") {
                            $('.loading').show();
                            ngDialog.open({
                                template: 'Vendor modified successfully',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.close(result);
                                ngDialog.close();
                            }, 3000);
                        } else {
                            ngDialog.open({
                                template: data.inputInvalid,
                                plain: true
                            });
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error  
                });
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    /********************************************EDIT DRIVER CONTROLLER*******************************************************************/
    var editDriverCtrl = function($scope, $modalInstance, $state, $http, driver, $timeout, ngDialog, $modal) {

        $scope.format = 'dd-MM-yyyy';
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.maxdate = new Date();

        var dateFormatConverter = function(x) {
            var date = x.split('-');
            return date[1] + '-' + date[0] + '-' + date[2];
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



        $scope.editDriver = {
            'driverId': driver.driverId,
            'driverName': driver.driverName,
            'mobileNumber': driver.mobileNumber,
            'driverAddress': driver.driverAddress,
            'driverdob': new Date(dateFormatConverter(driver.dateOfBirth)),
            'driverMedicalExpiry': new Date(dateFormatConverter(driver.medicalCertificateValid)),
            'licenceValid': new Date(dateFormatConverter(driver.licenceValid)),
            'licenceNumber': driver.licenceNumber,
            'driverBatchNum': driver.driverBatchNum,
            'driverDDT': new Date(dateFormatConverter(driver.ddtExpiryDate)),
            'driverBatch': new Date(dateFormatConverter(driver.batchDate)),
            'driverPoliceVerification': new Date(dateFormatConverter(driver.policeExpiryDate)),
            'driverJoining': new Date(dateFormatConverter(driver.joinDate)),
            'documents': driver.documents,
            'badgeValidity': new Date(dateFormatConverter(driver.batchValidity)),
            'permanentAddress': driver.permanentAddress
        };



        $scope.driverDocuments = [{
                name: 'test1',
                location: 'temp/test1.pdf',
                imgSrc: 'images/docImg.png'
            },
            {
                name: 'test2',
                location: 'temp/test2.xls',
                imgSrc: 'images/docImg.png'
            },
            {
                name: 'test3',
                location: 'temp/test3.docx',
                imgSrc: 'images/docImg.png'
            }
        ];

        angular.forEach($scope.driverDocuments, function(item) {
            extension = item.location.split('.').pop();
            switch (extension) {
                case 'pdf':
                    item.imgSrc = 'images/adobeAcrobat.png';
                    break;
                case 'docx':
                    item.imgSrc = 'images/msWord.png';
                    break;
                case 'xls':
                    item.imgSrc = 'images/msExcel.png';
                    break;
                default:
                    item.imgSrc = 'images/docImg.png';
            }
        });

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.insurranceExpDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'insurranceExpDate': true
                };
            }, 50);
        };

        $scope.openbadgeValidity = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'badgeValidity': true
                };
            }, 50);
        };

        $scope.openExpDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedExpDate': true
                };
            }, 50);
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

        $scope.openDDTCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedDDT': true
                };
            }, 50);
        };

        $scope.openBatchCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedBatch': true
                };
            }, 50);
        };

        $scope.openBatchNumCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedBatchNum': true
                };
            }, 50);
        };

        $scope.openPoliceVerificationCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedPoliceVerification': true
                };
            }, 50);
        };

        $scope.openMedicalExpiryCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedMedicalExpiry': true
                };
            }, 50);
        };

        $scope.openAntiExpiryCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedAntiExpiry': true
                };
            }, 50);
        };

        $scope.openJoiningCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedJoining': true
                };
            }, 50);
        };

        $scope.deleteDocument = function(index) {
            $confirm({
                    text: 'Are you sure you want to delete this row?',
                    title: 'Delete Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                })
                .then(function() {
                    $('#div' + index).hide('slow');
                });

        };

        // View All Driver Documents

        $scope.viewAllDocuments = function(editDriver, parentIndex, index, size) {

            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/vendorDashboard/viewAllDriverDocuments.jsp',
                controller: 'viewAllDocumentsCtrl',
                backdrop: 'static',
                keyboard: false,
                resolve: {
                    driver: function() {
                        return editDriver;
                    }
                }
            });


            modalInstance.result.then(function(result) {});

        }

        //Convert the dates in YYYY-MM-DD format
        var convertDateUTCYear = function(date) {
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


        $scope.saveDriver = function(result) {

            var data = {
                driverId: result.driverId,
                firstName: result.driverName,
                address: result.driverAddress,
                dobDate: convertDateUTC(result.driverdob),
                driverMedicalExpiryDate: convertDateUTC(result.driverMedicalExpiry),
                licenceValidDate: convertDateUTC(result.licenceValid),
                licenceNumber: result.licenceNumber,
                mobileNumber: result.mobileNumber,
                batchNumber: result.driverBatchNum,
                ddtExpiryDate: convertDateUTC(result.driverDDT),
                driverBatchDate: convertDateUTC(result.driverBatch),
                driverPoliceVerificationDate: convertDateUTC(result.driverPoliceVerification),
                driverMedicalExpiryDate: convertDateUTC(result.driverMedicalExpiry),
                driverJoiningDate: convertDateUTC(result.driverJoining),
                badgeValidity: convertDateUTCYear(result.badgeValidity),
                permanentAddress: result.permanentAddress,
                userId: profileId,
                combinedFacility: combinedFacility
            };

            $http.post('services/vendor/modifyDriverDetails/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "exist") {
                        ngDialog.open({
                            template: 'This Driver details already exist.',
                            plain: true
                        });

                    } else {
                        $('.loading').show();
                        result.driverdob = convertDateUTC(result.driverdob);
                        result.driverDDT = convertDateUTC(result.driverDDT);
                        result.driverMedicalExpiry = convertDateUTC(result.driverMedicalExpiry);
                        result.licenceValid = convertDateUTC(result.licenceValid);
                        result.driverBatch = convertDateUTC(result.driverBatch);
                        result.driverJoining = convertDateUTC(result.driverJoining);
                        result.badgeValidity = convertDateUTC(result.badgeValidity);
                        result.driverPoliceVerification = convertDateUTC(result.driverPoliceVerification);

                        ngDialog.open({
                            template: 'Driver modified successfully',
                            plain: true
                        });

                        $timeout(function() {
                            $modalInstance.close(result);
                            ngDialog.close();
                        }, 3000);
                    }
                }
            }).
            error(function(data, status, headers, config) {
                // log error    
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    /*************************************EDIT VEHICLE MODAL CONTROLLER*****************************************************************/
    var editVehicleCtrl = function($scope, $modal, $modalInstance, $state, $http, vehicle, $timeout, contractTypeList, $confirm, ngDialog) {
        $scope.format = 'dd-MM-yyyy';
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.contractList = contractTypeList;
        

        var dateFormatConverter = function(x) {
            var date = x.split('-');
            return date[1] + '-' + date[0] + '-' + date[2];
        };

        $scope.vehicleMaintenanceDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'vehicleMaintenanceDate': true
                };
            }, 50);
        }

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

        $scope.viewAllDocumentsVehicle = function(editVehicle, parentIndex, index, size) {

            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/vendorDashboard/viewAllVehicleDocuments.jsp',
                controller: 'viewAllVehicleDocumentsCtrl',
                backdrop: 'static',
                keyboard: false,
                resolve: {
                    vehicle: function() {
                        return editVehicle;
                    }
                }
            });
        }

        $scope.selectVendors = [{
                'value': 'Fast Track'
            },
            {
                'value': 'BHARATHICALLTAXI'
            },
            {
                'value': 'OLACABS'
            },
            {
                'value': 'Moorthy'
            },
            {
                'value': 'Golden Call Taxi'
            },
            {
                'value': 'Yellow Call Taxi'
            }
        ];

        $scope.selectSeats = [{
                'value': 2,
                'text': '2 Seater'
            },
            {
                'value': 4,
                'text': '4 Seater'
            },
            {
                'value': 5,
                'text': '5 Seater'
            },
            {
                'value': 6,
                'text': '6 Seater'
            },
            {
                'value': 7,
                'text': '7 Seater'
            },
            {
                'value': 8,
                'text': '8 Seater'
            },
            {
                'value': 9,
                'text': '9 Seater'
            },
            {
                'value': 10,
                'text': '10 Seater'
            },
            {
                'value': 11,
                'text': '11 Seater'
            },
            {
                'value': 12,
                'text': '12 Seater'
            },
            {
                'value': 13,
                'text': '13 Seater'
            },
            {
                'value': 24,
                'text': '24 Seater'
            }
        ];

        var vehicleOwnerIndex = _.findIndex($scope.selectVendors, {
            value: vehicle.vehicleOwnerName
        });
        var capacityIndex = _.findIndex($scope.selectSeats, {
            value: vehicle.capacity
        });
        var extension;

        $scope.vehicleDocuments = [{
                name: 'test1',
                location: 'temp/test1.pdf',
                imgSrc: 'images/docImg.png'
            },
            {
                name: 'test2',
                location: 'temp/test2.xls',
                imgSrc: 'images/docImg.png'
            },
            {
                name: 'test3',
                location: 'temp/test3.docx',
                imgSrc: 'images/docImg.png'
            }
        ];
        angular.forEach($scope.vehicleDocuments, function(item) {
            extension = item.location.split('.').pop();
            switch (extension) {
                case 'pdf':
                    item.imgSrc = 'images/adobeAcrobat.png';
                    break;
                case 'docx':
                    item.imgSrc = 'images/msWord.png';
                    break;
                case 'xls':
                    item.imgSrc = 'images/msExcel.png';
                    break;
                default:
                    item.imgSrc = 'images/docImg.png';
            }
        });

        $scope.editVehicle = {
            'vehicleId': vehicle.vehicleId,
            'vehicleOwnerName': vehicle.vehicleOwnerName,
            'vehicleNumber': vehicle.vehicleNumber,
            'isReplacement': vehicle.isReplacement,
            'PermitValid': new Date(dateFormatConverter(vehicle.PermitValid)),
            'contactNo': vehicle.vehicleEngineNumber,
            'contactName': vehicle.vehicleOwnerName,
            'capacity': vehicle.capacity,
            'regCert': vehicle.rcNumber,
            'pollutionCert': '',
            'TaxValid': new Date(dateFormatConverter(vehicle.taxCertificateValid)),
            'pollutionExpDate': new Date(dateFormatConverter(vehicle.polutionValid)),
            'vehicleModel': vehicle.vehicleModel,
            'modelYear': vehicle.vehicleModelYear,
            'InsuranceDate': new Date(dateFormatConverter(vehicle.InsuranceDate)),
            'vehicleName': vehicle.vehicleMake,
            'conTariffId': vehicle.contractTariffId,
            'selectDrivers': '',
            'mobileNumber': vehicle.mobileNumber,
            'registrationDate': new Date(dateFormatConverter(vehicle.registrationDate)),
            'vehicleFitnessCert': new Date(dateFormatConverter(vehicle.vehicleFitnessDate)),
            'documents': vehicle.documents,
            'mileage': vehicle.mileage,
            'vehicleMaintenanceDate': new Date(dateFormatConverter(vehicle.vehicleMaintenanceDate))
        };


        var editVehicleData = _.findWhere(contractTypeList, {
                    "contractName": vehicle.conType
                });
        $scope.editVehicle.conType = editVehicleData;


        /*if (vehicle.contractTariffId == undefined) {
            $scope.editVehicle.conType = {
                contractName: vehicle.conType.contractName,
                contractId: vehicle.conType.contractId
            };
        } else {
            $scope.editVehicle.conType = {
                contractName: vehicle.conType,
                contractId: vehicle.contractTariffId
            };
        }*/
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };
        $scope.pollutionExpDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'pollutionExpDate': true
                };
            }, 50);
        };

        $scope.InsuranceExpiryCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'InsuranceExpiryDate': true
                };
            }, 50);
        };

        $scope.vehicleFitnessCertCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'vehicleFitnessCert': true
                };
            }, 50);
        };

        $scope.registrationDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'registrationDate': true
                };
            }, 50);
        };


        $scope.PermitValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'PermitValidDate': true
                };
            }, 50);
        };

        $scope.TaxValidCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'TaxValidDate': true
                };
            }, 50);
        };

        $scope.deleteDocument = function(index) {
            $('#div' + index).hide('slow');
        };

        $scope.updateVehicle = function(result) {
            $confirm({
                    text: 'Are you sure you want to save the changes for this Vehicle?',
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                })
                .then(function() {
                    var data = {
                        vehicleId: result.vehicleId,
                        vehicleMake: result.vehicleName,
                        vehicleNumber: result.vehicleNumber,
                        vehicleEngineNumber: result.contactNo,
                        vehicleOwnerName: result.contactName,
                        capacity: result.capacity,
                        registartionCertificateNumber: result.regCert,
                        vehicleModel: result.vehicleModel,
                        polutionDate: convertDateUTC(result.pollutionExpDate),
                        vehicleModelYear: result.modelYear,
                        taxValidDate: convertDateUTC(result.TaxValid),
                        insuranceValid: convertDateUTC(result.InsuranceDate),
                        contractDetailId: result.conTariffId,
                        permitValid: convertDateUTC(result.PermitValid),
                        registrationValid: convertDateUTC(result.registrationDate),
                        maintenanceValid: convertDateUTC(result.vehicleFitnessCert),
                        contractDetailId: result.conType.contractId,
                        replaceMentVehicleNum: result.isReplacement,
                        mileage: result.mileage,
                        userId: profileId,
                        branchId: branchId,
                        vehicleMaintenaneDate: convertDateUTC(result.vehicleMaintenanceDate),
                        combinedFacility: combinedFacility

                    };

                    $http.post('services/vehicle/modifyVehicleDetails/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == "exist") {

                                ngDialog.open({
                                    template: 'This Vehicle details already exist.',
                                    plain: true
                                });

                            } else {
                                $('.loading').show();

                                ngDialog.open({
                                    template: 'Vehicle modified successfully.',
                                    plain: true
                                });

                                result.InsuranceDate = convertDateUTC(result.InsuranceDate);
                                result.documents = vehicle.documents;
                                result.pollutionExpDate = convertDateUTC(result.pollutionExpDate);
                                result.PermitValid = convertDateUTC(result.PermitValid);
                                result.TaxValid = convertDateUTC(result.TaxValid);
                                result.registrationDate = convertDateUTC(result.registrationDate);
                                result.vehicleFitnessCert = convertDateUTC(result.vehicleFitnessCert);
                                $timeout(function() {
                                    $modalInstance.close(result);
                                    ngDialog.close();
                                }, 3000);
                            }
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error    
                    });
                });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    /**************************************************EDIT ESCORT CONTROLLER**********************************************************/
    var editEscortCtrl = function($scope, $modal, $modalInstance, $state, $http, $timeout, escort, ngDialog, vendorContractManagData, $confirm) {
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = array;
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.maxdate = new Date();

        var dateFormatConverter = function(x) {
            var date = x.split('-');
            return date[1] + '-' + date[0] + '-' + date[2];
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
            return convert_date.getFullYear() + '-' + currentMonth + '-' + currentDay;
        };
        var parseUKDate = function(source, delimiter) {
            return new Date(source.split(delimiter).reverse().join(delimiter))
        };
        $scope.editEscort = {
            'escortId': escort.escortId,
            'escortName': escort.escortName,
            'escortMobileNumber': escort.escortMobileNumber,
            'escortdob': parseUKDate(escort.escortdob, "-"),
            'escortAddress': escort.escortAddress,
            'permanentAddress': escort.permanentAddress,
            'escortVendorName': {
                vendorName: escort.escortVendorName
            },
            'escortBatchNumber': escort.escortBatchNum,
            'escortFatherName': escort.escortFatherName,
            'pincode': escort.escortPincode,
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };

        $scope.format = 'dd-MM-yyyy';

        $scope.dobCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'dobDate': true
                };
            }, 50);
        };

        $scope.viewAllDocumentsEscort = function(editEscort, parentIndex, index, size) {
            $scope.pathNameData = [];
            var data = {
                escortId: editEscort.escortId,
                userId: profileId
            };

            $http.post('services/escort/listOfDocumentDetails', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    angular.forEach(data, function(value, key) {

                        $scope.pathNameData.push(value);

                    });
                    if ($scope.pathNameData.length > 0) {
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/vendorDashboard/viewAllEscortDocuments.jsp',
                            controller: 'viewAllEscortDocumentsCtrl',
                            backdrop: 'static',
                            keyboard: false,
                            resolve: {
                                escort: function() {
                                    return $scope.pathNameData;
                                }
                            }
                        });
                    } else {
                        ngDialog.open({
                            template: 'No Documents Found',
                            plain: true
                        });
                    }
                }

            }).
            error(function(data, status, headers, config) {
                // log error    
            });

        }


        $scope.saveEscort = function(result, combinedFacilityId) {

            var facilityBranchName = [];

            angular.forEach(JSON.parse("[" + combinedFacilityId + "]"), function(item, key) {
                var branchName = _.where(userFacilities, {
                    branchId: item
                })[0].name;
                facilityBranchName.push(branchName);
            });

            $confirm({
                text: "Are you sure you want to add this vendor to" + ' ' + String(facilityBranchName) + ' ' + "facility?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {

                result.escortVendorName = result.escortVendorName.vendorName;
                var data = {
                    escortId: result.escortId,
                    firstName: result.escortName,
                    fatherName: result.escortFatherName,
                    pincode: result.pincode,
                    mobileNumber: result.escortMobileNumber,
                    dateOfBirth: convertDateUTC(result.escortdob),
                    address: result.escortAddress,
                    escortEmployeeId: result.escortBatchNumber,
                    vendorName: result.escortVendorName,
                    isActive: 'Y',
                    permanentAddress: result.permanentAddress,
                    userId: profileId,
                    combinedFacility: combinedFacility,
                    facilityName: result.facilityName
                };

                $http.post('services/escort/modifyEscortDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $('.loading').show();

                        ngDialog.open({
                            template: 'Escort modified successfully.',
                            plain: true
                        });

                        $timeout(function() {
                            $modalInstance.close(result);
                            ngDialog.close();
                        }, 3000);
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            });
        };
        //        
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    /*******************************************************************************************************/
    var uploadVehicleCtrl = function($scope, $modalInstance, $state, $http, vehicle, $timeout, ngDialog) {
        $scope.alertMessage;
        $scope.alertHint;

        $scope.vehicleDocumentTypes = [{
                'value': 'Insurance',
                'text': 'Vehicle Insurance'
            },
            {
                'value': 'Registration',
                'text': 'Vehicle Registration'
            },
            {
                'value': 'pollution',
                'text': 'Polution Certificate'
            },
            {
                'value': 'Permit',
                'text': 'Vehicle Tax Permit'
            }
        ];

        var typeOfUploadDoc = "";
        $scope.setDocType = function(vehicleDocType) {
            typeOfUploadDoc = vehicleDocType.value;
        };
        $scope.importVehicleFile = function(result) {
            if (result.type == 'image/png' ||
                result.type == 'image/jpg' ||
                result.type == 'image/jpeg') {
                $scope.FileFormatValue = true;
            }

            if ($scope.FileFormatValue == undefined) {
                ngDialog.open({
                    template: 'Kindly upload valid image format(.png,.jpg,.jpeg)',
                    plain: true
                });
                return false;
            }

            if (result.size > imageUploadSize * 1048576) {
                ngDialog.open({
                    template: 'File size should not exceed' + ' ' + imageUploadSize + ' ' + ' MB!',
                    plain: true
                });
                return false;
            }

            var binaryData = [];
            binaryData.push(result);
            window.URL.createObjectURL(new Blob(binaryData, {
                type: "application/zip"
            }))

            function convertURIToImageData(URI) {
                return new Promise(function(resolve) {
                    var canvas = document.createElement('canvas'),
                        context = canvas.getContext('2d'),
                        image = new Image();
                    image.addEventListener('load', function() {
                        canvas.width = image.width;
                        canvas.height = image.height;
                        context.drawImage(image, 0, 0, canvas.width, canvas.height);
                        resolve(context.getImageData(0, 0, canvas.width, canvas.height));
                    }, false);

                    image.addEventListener('error', function() {
                        ngDialog.open({
                            template: 'Unsupported File format. Kindly upload valid format.',
                            plain: true
                        });
                        return false;
                    }, false);
                    image.src = URI;
                });

            }

            var URI = binaryData[0].data;
            var imageData;

            convertURIToImageData(URI).then(function(resolve) {

                var fd = new FormData();
                fd.append("filename", $("#filenameforactivity")[0].files[0]);
                var post_url = $("#addinstgroup").attr("action");
                var postdata = $("#addinstgroup").serialize();
                if (typeOfUploadDoc.length == 0) {

                    ngDialog.open({
                        template: 'Please select document type.',
                        plain: true
                    });

                    return false;
                }
                var vehicleId = vehicle.vehicleId;
                var url = post_url + "?" + postdata + "&profileId=" + profileId + "&branchId=" + branchId + "&vehicleId=" + vehicleId + "&typeOfUploadDoc=" + typeOfUploadDoc;
                $.ajax({
                    url: url,
                    type: 'POST',
                    cache: false,
                    data: fd,
                    processData: false,
                    contentType: false,
                    success: function(data, textStatus, jqXHR) {},
                    complete: function(data) {
                        $('.loading').show();
                        if (data.responseText == 'success') {
                            ngDialog.open({
                                template: typeOfUploadDoc + ' uploaded successfully.',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'exist') {
                            ngDialog.open({
                                template: 'Please check this filename already exist.',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);

                        } else if (data.responseText == 'bigSize') {
                            ngDialog.open({
                                template: 'Sorry you can not upload a file more than 2 MB',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data == 'notAnImage') {
                            ngDialog.open({
                                template: 'Kindly upload valid image format(.png,.jpg,.jpeg).',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else {
                            ngDialog.open({
                                template: 'Something went wrong please check document.',
                                plain: true
                            });

                        }
                    }

                });
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var uploadDriverCtrl = function($scope, $modalInstance, $state, $http, driver, $timeout, ngDialog) {
        $scope.alertMessage;
        $scope.alertHint;

        $scope.driverDocumentTypes = [{
                'value': 'License',
                'text': 'License'
            },
            {
                'value': 'Address Proof',
                'text': 'Address Proof'
            },
            {
                'value': 'Police Verification',
                'text': 'Police Verification'
            },
            {
                'value': 'Batch certificate',
                'text': 'Batch certificate'
            },
            {
                'value': 'medical',
                'text': 'Medical Certificate'
            },
            {
                'value': 'profilePic',
                'text': 'Profile Picture'
            }
        ];
        var typeOfUploadDoc = "";

        $scope.setDocType = function(driverDocType) {
            typeOfUploadDoc = driverDocType.value;
        };
        $scope.importDriverFile = function(result) {

            if (result.type == 'image/png' ||
                result.type == 'image/ ' ||
                result.type == 'image/jpeg') {
                $scope.FileFormatValue = true;
            }

            if ($scope.FileFormatValue == undefined) {
                ngDialog.open({
                    template: 'Kindly upload valid image format(.png,.jpg,.jpeg)',
                    plain: true
                });
                return false;
            }

            if (result.size > imageUploadSize * 1048576) {
                ngDialog.open({
                    template: 'File size should not exceed' + ' ' + imageUploadSize + ' ' + ' MB!',
                    plain: true
                });
                return false;
            }

            var binaryData = [];
            binaryData.push(result);
            window.URL.createObjectURL(new Blob(binaryData, {
                type: "application/zip"
            }))

            function convertURIToImageData(URI) {
                return new Promise(function(resolve) {
                    var canvas = document.createElement('canvas'),
                        context = canvas.getContext('2d'),
                        image = new Image();
                    image.addEventListener('load', function() {
                        canvas.width = image.width;
                        canvas.height = image.height;
                        context.drawImage(image, 0, 0, canvas.width, canvas.height);
                        resolve(context.getImageData(0, 0, canvas.width, canvas.height));
                    }, false);

                    image.addEventListener('error', function() {
                        ngDialog.open({
                            template: 'Unsupported File format. Kindly upload valid format.',
                            plain: true
                        });
                        return false;
                    }, false);
                    image.src = URI;
                });

            }

            var URI = binaryData[0].data;
            var imageData;

            convertURIToImageData(URI).then(function(resolve) {
                var fd = new FormData();
                fd.append("filename", $("#filenameforactivity")[0].files[0]);
                var post_url = $("#addinstgroup").attr("action");
                var postdata = $("#addinstgroup").serialize();
                if (typeOfUploadDoc.length == 0) {
                    ngDialog.open({
                        template: 'Please select document type.',
                        plain: true
                    });

                    return false;
                }
                var driverId = driver.driverId;
                var url = post_url + "?" + postdata + "&profileId=" + profileId + "&branchId=" + branchId + "&driverId=" + driverId + "&typeOfUploadDoc=" + typeOfUploadDoc;
                $.ajax({
                    url: url,
                    type: 'POST',
                    cache: false,
                    data: fd,
                    processData: false,
                    contentType: false,
                    success: function(data, textStatus, jqXHR) {},
                    complete: function(data) {
                        $('.loading').show();
                        if (data.responseText == 'success') {
                            ngDialog.open({
                                template: typeOfUploadDoc + ' uploaded successfully.',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'exist') {
                            ngDialog.open({
                                template: 'Please check this filename already exist.',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 7000);

                        } else if (data.responseText == 'bigSize') {
                            ngDialog.open({
                                template: 'Sorry you can not upload a file more than 2 MB',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'notAnImage') {
                            ngDialog.open({
                                template: 'Kindly upload valid image format(.png,.jpg,.jpeg).',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else {
                            ngDialog.open({
                                template: 'Please check uploaded file',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 7000);

                        }
                    }
                });
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    //*******************************************UPLOAD ESCORT DOCUMENT ***********************************//

    var uploadEscortCtrl = function($scope, $modalInstance, $state, $http, escort, $timeout, ngDialog) {
        $scope.alertMessage;
        $scope.alertHint;

        $scope.escortDocumentTypes = [{
                'value': 'Address Proof',
                'text': 'Address Proof'
            },
            {
                'value': 'Training Certificate',
                'text': 'Training Certificate'
            },
            {
                'value': 'profilePic',
                'text': 'Profile Picture'
            }
        ];
        var typeOfUploadDoc = "";

        $scope.setDocType = function(escortDocType) {
            typeOfUploadDoc = escortDocType.value;
        };

        $scope.importEscortFile = function(result) {

            if (result.type == 'image/png' ||
                result.type == 'image/jpg' ||
                result.type == 'image/jpeg') {
                $scope.FileFormatValue = true;
            }

            if ($scope.FileFormatValue == undefined) {
                ngDialog.open({
                    template: 'Kindly upload valid image format(.png,.jpg,.jpeg)',
                    plain: true
                });
                return false;
            }

            if (result.size > imageUploadSize * 1048576) {
                ngDialog.open({
                    template: 'File size should not exceed' + ' ' + imageUploadSize + ' ' + ' MB!',
                    plain: true
                });
                return false;
            }

            var binaryData = [];
            binaryData.push(result);
            window.URL.createObjectURL(new Blob(binaryData, {
                type: "application/zip"
            }))

            function convertURIToImageData(URI) {
                return new Promise(function(resolve) {
                    var canvas = document.createElement('canvas'),
                        context = canvas.getContext('2d'),
                        image = new Image();
                    image.addEventListener('load', function() {
                        canvas.width = image.width;
                        canvas.height = image.height;
                        context.drawImage(image, 0, 0, canvas.width, canvas.height);
                        resolve(context.getImageData(0, 0, canvas.width, canvas.height));
                    }, false);

                    image.addEventListener('error', function() {
                        ngDialog.open({
                            template: 'Unsupported File format. Kindly upload valid format.',
                            plain: true
                        });
                        return false;
                    }, false);
                    image.src = URI;
                });

            }

            var URI = binaryData[0].data;
            var imageData;

            convertURIToImageData(URI).then(function(resolve) {
                var fd = new FormData();
                fd.append("filename", $("#filenameforactivity")[0].files[0]);
                var post_url = $("#addinstgroup").attr("action");
                var postdata = $("#addinstgroup").serialize();

                if (typeOfUploadDoc.length == 0) {
                    ngDialog.open({
                        template: 'Please select document type.',
                        plain: true
                    });
                    return false;
                }
                var escortId = escort.escortId;
                var url = post_url + "?" + postdata + "&profileId=" + profileId + "&branchId=" + branchId + "&escortId=" + escortId + "&typeOfUploadDoc=" + typeOfUploadDoc;
                $.ajax({
                    url: url,
                    type: 'POST',
                    cache: false,
                    data: fd,
                    processData: false,
                    contentType: false,
                    success: function(data, textStatus, jqXHR) {},
                    complete: function(data) {

                        if (data.responseText == 'success') {
                            ngDialog.open({
                                template: typeOfUploadDoc + ' uploaded successfully.',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'exist') {
                            ngDialog.open({
                                template: 'Please check this filename already exist.',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 7000);

                        } else if (data.responseText == 'bigSize') {
                            ngDialog.open({
                                template: 'Sorry you can not upload a file more than 2 MB',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'notAnImage') {
                            ngDialog.open({
                                template: 'Kindly upload valid image format(.png,.jpg,.jpeg).',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else {
                            ngDialog.open({
                                template: 'Please check uploaded file',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 7000);

                        }
                    }
                });
            });

        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    //*******************************************UPLOAD INSPECTION DOCUMENT ***********************************//
    var uploadInspectionDocCtrl = function($scope, $modalInstance, $state, $http, inspection, $timeout, ngDialog) {
        $scope.alertMessage;
        $scope.alertHint;

        $scope.inspectionDocumentTypes = [{
                'value': 'vehiclePhotoFromFront',
                'text': 'VehiclePhotoFromFront'
            },
            {
                'value': 'vehiclePhotoFromBack',
                'text': 'VehiclePhotoFromBack'
            },
            {
                'value': 'vehiclePhotoFromLeftSide',
                'text': 'VehiclePhotoFromLeft'
            },
            {
                'value': 'vehiclePhotoFromRightSide',
                'text': 'VehiclePhotoFromRight'
            }
        ];
        var typeOfUploadDoc = "";

        $scope.setInspectionDocType = function(inspectionDocType) {
            typeOfUploadDoc = inspectionDocType.value;
        };
        $scope.importDriverFile = function(fileName, fileType) {
            if (fileName.type == 'image/png' ||
                fileName.type == 'image/jpg' ||
                fileName.type == 'image/jpeg') {
                $scope.FileFormatValue = true;
            }

            if ($scope.FileFormatValue == undefined) {
                ngDialog.open({
                    template: 'Kindly upload valid image format(.png,.jpg,.jpeg)',
                    plain: true
                });
                return false;
            }

            if (fileName.size > imageUploadSize * 1048576) {
                ngDialog.open({
                    template: 'File size should not exceed' + ' ' + imageUploadSize + ' ' + ' MB!',
                    plain: true
                });
                return false;
            }

            var binaryData = [];
            binaryData.push(fileName);
            window.URL.createObjectURL(new Blob(binaryData, {
                type: "application/zip"
            }))

            function convertURIToImageData(URI) {
                return new Promise(function(resolve) {
                    var canvas = document.createElement('canvas'),
                        context = canvas.getContext('2d'),
                        image = new Image();
                    image.addEventListener('load', function() {
                        canvas.width = image.width;
                        canvas.height = image.height;
                        context.drawImage(image, 0, 0, canvas.width, canvas.height);
                        resolve(context.getImageData(0, 0, canvas.width, canvas.height));
                    }, false);

                    image.addEventListener('error', function() {
                        ngDialog.open({
                            template: 'Unsupported File format. Kindly upload valid format.',
                            plain: true
                        });
                        return false;
                    }, false);
                    image.src = URI;
                });

            }

            var URI = binaryData[0].data;
            var imageData;

            convertURIToImageData(URI).then(function(resolve) {

                var fd = new FormData();
                fd.append("filename", $("#filenameforactivity")[0].files[0]);
                var post_url = $("#addinstgroup").attr("action");
                var postdata = $("#addinstgroup").serialize();
                if (typeOfUploadDoc.length == 0) {
                    ngDialog.open({
                        template: 'Please select document type.',
                        plain: true
                    });

                    return false;
                }
                var inspectionId = inspection.inspectionId;
                var url = post_url + "?" + postdata + "&profileId=" + profileId + "&branchId=" + branchId + "&inspectionId=" + inspectionId + "&typeOfUploadDoc=" + typeOfUploadDoc;
                $.ajax({
                    url: url,
                    type: 'POST',
                    cache: false,
                    data: fd,
                    processData: false,
                    contentType: false,
                    success: function(data, textStatus, jqXHR) {},
                    complete: function(data) {
                        $('.loading').show();
                        if (data.responseText == 'success') {
                            ngDialog.open({
                                template: typeOfUploadDoc + ' uploaded successfully.',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'exist') {
                            ngDialog.open({
                                template: 'Please check this filename already exist.',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 7000);

                        } else if (data.responseText == 'bigSize') {
                            ngDialog.open({
                                template: 'Sorry you can not upload a file more than 2 MB',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else if (data.responseText == 'notAnImage') {
                            ngDialog.open({
                                template: 'Kindly upload valid image format(.png,.jpg,.jpeg).',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 3000);
                        } else {
                            ngDialog.open({
                                template: 'Please check uploaded file',
                                plain: true
                            });
                            $timeout(function() {
                                $modalInstance.dismiss('cancel');
                                ngDialog.close();
                            }, 7000);

                        }
                    }
                });
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    //******************************************************EDIT ENTITY ON DRIVER CHECK-IN*********************************//
    //EDIT CHECK IN VEHICLE ENTITIES   
    var entityCtrl = function($scope, $modalInstance, $state, $http, $timeout, vehicle, ngDialog) {
        $scope.alertMessage;
        $scope.alertHint;
        $scope.isNotValid = true;
        $scope.vehicles = [];
        $scope.devices = [];
        $scope.drivers = [];
        $scope.edit = {};
        var deviceId;
        var driverId;
        var vehicleId;
        var checkInId;
        var isdriverChange = false;
        var isVehicleNumberChange = false;
        var isdeviceNumberChange = false;
        var currentDriverIndex;
        var currentVehicleIndex;
        var currentDeviceIndex;

        var data = {
            eFmFmClientBranchPO: {
                branchId: branchId
            },
            vendorId: vehicle.vendorId,
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/vehicle/listOfActiveEntity/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                $scope.Original_vehiclesData = data.vehicleDetails;
                $scope.Original_driversData = data.driverDetails;
                $scope.devicesData = data.deviceDetails;
                $scope.vehiclesData = data.vehicleDetails;
                $scope.driversData = data.driverDetails;
                currentDriverIndex = _.findIndex($scope.driversData, {
                    driverId: vehicle.driverId
                });
                currentVehicleIndex = _.findIndex($scope.vehiclesData, {
                    vehicleId: vehicle.vehicleId
                });
                currentDeviceIndex = _.findIndex($scope.devicesData, {
                    deviceId: vehicle.deviceId
                });
                if (currentDriverIndex >= 0) {
                    $scope.edit.driverName = $scope.driversData[currentDriverIndex];
                }

                if (currentVehicleIndex >= 0) {
                    $scope.edit.vehicleNumber = $scope.vehiclesData[currentVehicleIndex];
                }

                if (currentDeviceIndex >= 0) {
                    $scope.edit.deviceId = $scope.devicesData[currentDeviceIndex];
                }
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        $scope.updateDriverManual = function(driver) {
            if (angular.isObject(driver)) {
                var clickedDriver_vendorId = driver.vendorId;
                var curentVehicle_vendorId = $scope.edit.vehicleNumber.vendorId;
                var data = {
                    vendorId: driver.vendorId,
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/vehicle/listOfActiveVehicle/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vehiclesData = data.vehicleDetails;
                        if ($scope.vehiclesData.length > 0) {
                            if (clickedDriver_vendorId != curentVehicle_vendorId) {
                                $scope.edit.vehicleNumber = $scope.vehiclesData[0];
                            }
                        } else {
                            $scope.vehiclesData = $scope.Original_vehiclesData;
                            $scope.driversData = $scope.Original_driversData;

                            $scope.edit.driverNumber = $scope.driversData[currentDriverIndex];
                            $scope.edit.driverName = $scope.driversData[currentDriverIndex];
                            $scope.edit.vehicleNumber = $scope.vehiclesData[currentVehicleIndex];

                            ngDialog.open({
                                template: 'There is no Vehicle Available. Please change the Driver Selection.',
                                plain: true
                            });

                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
        };

        $scope.updateVehicleNumberManual = function(vehicleSelected) {
            if (angular.isObject(vehicleSelected)) {
                var clickedvehicler_vendorId = vehicleSelected.vendorId;
                var curentDriver_vendorId = $scope.edit.driverName.vendorId;
                vendorId = vehicleSelected.vendorId;
                var data = {
                    vendorId: vehicleSelected.vendorId,
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/vehicle/listOfActiveDriver/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.driversData = data.driverDetails;
                        if ($scope.driversData.length > 0) {
                            if (clickedvehicler_vendorId != curentDriver_vendorId) {
                                $scope.edit.driverNumber = $scope.driversData[0];
                                $scope.edit.driverName = $scope.driversData[0];
                            }
                        } else {
                            $scope.vehiclesData = $scope.Original_vehiclesData;
                            $scope.driversData = $scope.Original_driversData;
                            $scope.edit.driverNumber = $scope.driversData[currentDriverIndex];
                            $scope.edit.driverName = $scope.driversData[currentDriverIndex];
                            $scope.edit.vehicleNumber = $scope.vehiclesData[currentVehicleIndex];
                            ngDialog.open({
                                template: 'There is no Driver Available. Please change the Vehicle Selection.',
                                plain: true
                            });

                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
        };

        $scope.updateDeviceIdManual = function(device) {


        };

        $scope.updateVehicleEntity = function(entity) {

            if ($scope.edit.vehicleNumber.vehicleNumber != vehicle.vehicleNumber) {
                isVehicleNumberChange = true;
            } else {
                isVehicleNumberChange = false;
            }

            if ($scope.edit.driverName.driverName != vehicle.DriverName) {
                isdriverChange = true;
            } else {
                isdriverChange = false;
            }

            if ($scope.edit.deviceId.deviceId != vehicle.deviceId) {
                isdeviceNumberChange = true;
            } else {
                isdeviceNumberChange = false;
            }
            driverId = $scope.edit.driverName.driverId;
            vehicleId = $scope.edit.vehicleNumber.vehicleId;
            deviceId = $scope.edit.deviceId.deviceId;

            if (isdriverChange || isVehicleNumberChange || isdeviceNumberChange) {
                if ($scope.edit.vehicleNumber.vehicleNumber.indexOf('DUMMY') >= 0) {
                    ngDialog.open({
                        template: 'Sorry you can not update the dummy entities.',
                        plain: true
                    });
                    return false;
                }

                var data = {
                    efmFmVehicleMaster: {
                        vehicleId: vehicleId
                    },
                    eFmFmDeviceMaster: {
                        deviceId: deviceId
                    },
                    efmFmDriverMaster: {
                        driverId: driverId
                    },
                    branchId: branchId,
                    checkInId: vehicle.checkInId,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $http.post('services/vehicle/driverCheckIn/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.escortAvailableData = data;
                        if (data == "success") {
                            $('.loading').show();
                            ngDialog.open({
                                template: 'Entity changed Successfully.',
                                plain: true
                            });

                            $timeout(function() {
                                $modalInstance.close(entity);
                                ngDialog.close();
                            }, 3000);
                        } else {
                            $('.loading').show();
                            ngDialog.open({
                                template: 'Vehicle and Device Check-In already Allocated.',
                                plain: true
                            });

                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            } else {
                $('.loading').show();
                ngDialog.open({
                    template: 'None of the Entity has been Change',
                    plain: true
                });

                $timeout(function() {
                    $modalInstance.dismiss('cancel');
                    ngDialog.close();
                }, 3000);
            }
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    //******************************************************PLANNE RELEASE CONTROLLER*********************************//
    var releaseDriverCtrl = function($scope, $modalInstance, $state, $http, $timeout, driver, ngDialog) {
        $scope.todaysDate = new Date();

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
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };

        $scope.format = 'dd-MM-yyyy';

        $scope.fromCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'fromDate': true
                };
            }, 50);
        };

        $scope.toCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'toDate': true
                };
            }, 50);
        };

        $scope.driver = {
            driverName: driver.DriverName,
            vendorName: driver.vendorName,
            vehicleNumber: driver.vehicleNumber,
            vendorId: driver.vendorId,
            vehicleId: driver.vehicleId,
            deviceId: driver.deviceId,
            checkInId: driver.checkInId
        };

        $scope.release = function(result) {
            $modalInstance.close(result);
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var checkOutRemarksCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, driver) {
        $scope.addRemarks = function(result) {
            $modalInstance.close(result)
        };

        $scope.joinDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'joinDate': true
                };
            }, 50);
        };

        $scope.relieveDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'relieveDate': true
                };
            }, 50);
        };

        //Function to check the Date Validity

        $scope.checkDateRangeValidity = function(joinDate, relieveDate) {
            if (joinDate > relieveDate) {
                $scope.dateRangeError = true;
                $timeout(function() {
                    $scope.dateRangeError = false;
                }, 3000);
                return false;
            } else return true;
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    var viewAllDocumentsCtrl = function($scope, $modalInstance, $state, $http, driver, $timeout, ngDialog, $modal) {

        $scope.pathNameData = [];
        var data = {
            driverId: driver.driverId,
            userId: profileId,
            combinedFacility: combinedFacility
        };

        $http.post('services/xlUtilityDriverUpload/listOfDocumentDetails', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {

                angular.forEach(data, function(value, key) {

                    $scope.pathNameData.push(value);

                });
            }

        }).
        error(function(data, status, headers, config) {
            // log error    
        });

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };

    // Escort View All Documents

    var viewAllEscortDocumentsCtrl = function($scope, $modalInstance, $state, $http, escort, $timeout, ngDialog, $modal) {
        $scope.pathNameData = [];
        var data = {
            escortId: escort.escortId,
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/escort/listOfDocumentDetails', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(value, key) {
                    $scope.pathNameData.push(value);
                });
            }
        }).
        error(function(data, status, headers, config) {
            // log error    
        });

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };


    // Vehicle View All Documents

    var viewAllVehicleDocumentsCtrl = function($scope, $modalInstance, $state, $http, vehicle, $timeout, ngDialog, $modal) {
        $scope.pathNameData = [];
        var data = {
            vehicleId: vehicle.vehicleId,
            userId: profileId,
            combinedFacility: combinedFacility
        };

        $http.post('services/xlUtilityVehicleUpload/listOfDocumentDetails', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(value, key) {
                    $scope.pathNameData.push(value);
                });
            }
        }).
        error(function(data, status, headers, config) {
            // log error    
        });

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var newSupervisorCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, vendor) {
        $scope.IntegerNumber = /^\d+$/;
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.permanentAddressDisabled = false;
        $scope.newSupervisor = {};
        $scope.employeeGen = ['Male', 'Female'];
        $scope.physicallyChallenged = ['Yes', 'No'];
        $scope.allVendors = [];
        $scope.maxdate = new Date();

        $scope.addSupervisor = {
            'supervisorName': '',
            'supervisorMobileNumber': '',
            'supervisorVendorName': '',
            'supervisordob': '',
            'permanentAddress': '',
            'supervisorAddress': '',
            'supervisorBatchNumber': ''
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };

        $scope.format = 'dd-MM-yyyy';

        $scope.dobCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'supervisordob': true
                };
            }, 50);
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

        //Convert the dates in DD-MM-YYYY format
        var convertDateUTCYear = function(date) {
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

        // Get All Active Vendors

        var data = {
            branchId: branchId,
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/contract/allActiveVendor/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(item) {
                    $scope.allVendors.push({
                        'name': item.name,
                        'Id': item.vendorId
                    });
                });
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });


        // Apply Temporary Address as permanent address 
        $scope.applyPermanentAddress = function(value, address) {
            if (value == true) {
                $scope.permanentAddressDisabled = true;
                $scope.newSupervisor.permanentAddress = address;
            } else {
                $scope.permanentAddressDisabled = false;
            }
        }

        $scope.addSupervisor = function(result) {
            if (userRole == 'superadmin' || userRole == 'admin') {
                isActive = 'A';
            } else {
                isActive = 'P';
            }
            var data = {
                branchId: branchId,
                userId: profileId,
                firstName: result.firstName,
                lastName: result.lastName,
                fatherName: result.fatherName,
                designation: result.designation,
                mobileNumber: result.mobileNumber,
                efmFmVendorMaster: {
                    vendorId: vendor.vendorId
                },
                dobDate: convertDateUTC(result.supervisordob),
                emailId: result.email,
                gender: result.gender,
                physicallyChallenged: result.physicallyChallenged,
                presentAddress: result.temporaryAddresss,
                permanentAddress: result.permanentAddress,
                supervisorEmpId: result.supervisorEmployeeId,
                isActive: isActive,
                combinedFacility: combinedFacility
            };

            $http.post('services/fieldApp/addSupervisorDetails', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == 'DUPMOBNO') {
                        ngDialog.open({
                            template: 'This mobile number is already registered with another user.',
                            plain: true
                        });
                        return false;
                    }
                    if (userRole == 'superadmin' || userRole == 'admin') {
                        ngDialog.open({
                            template: 'Supervisor Added successfully',
                            plain: true
                        });
                    } else {
                        ngDialog.open({
                            template: 'Supervisor Added successfully, Waiting for Approval!',
                            plain: true
                        });
                    }
                    result.supervisorId = data.supervisorId;
                    result.remarks = 'NA';
                    $timeout(function() {
                        $modalInstance.close(result);
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


    var editSupervisorCtrl = function($scope, $rootScope, $modal, $modalInstance, $state, $http, $timeout, supervisor, tabValue, ngDialog) {
        $scope.IntegerNumber = /^\d+$/;
        $scope.IntegerNumber = /^\d+$/;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.permanentAddressDisabled = false;
        $scope.editSupervisor = {};
        $scope.employeeGen = ['Male', 'Female'];
        $scope.physicallyChallenged = ['Yes', 'No'];
        $scope.allVendors = [];

        // Apply Temporary Address as permanent address 
        $scope.applyPermanentAddress = function(value, address) {
            if (value == true) {
                $scope.permanentAddressDisabled = true;
                $scope.editSupervisor.permanentAddress = address;
            } else {
                $scope.permanentAddressDisabled = false;
            }
        }

        // Get All Active Vendors

        var data = {
            branchId: branchId,
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/contract/allActiveVendor/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(item) {
                    $scope.allVendors.push({
                        'vendorName': item.name,
                        'Id': item.vendorId
                    });
                });

                $rootScope.vendorName = _.findWhere($scope.allVendors, {
                    "Id": supervisor.vendorId
                });
                $scope.editSupervisor = {};
                $scope.editSupervisor = {
                    firstName: supervisor.supervisorName,
                    lastName: supervisor.supervisorlastName,
                    fatherName: supervisor.supervisorFatherName,
                    designation: supervisor.designation,
                    mobileNumber: supervisor.supervisorMobileNumber,
                    supervisorVendorName: $rootScope.vendorName,
                    supervisordob: supervisor.supervisordob,
                    email: supervisor.emailId,
                    gender: supervisor.supervisorGender,
                    temporaryAddresss: supervisor.presentAddress,
                    permanentAddress: supervisor.supervisorAddress,
                    supervisorId: supervisor.supervisorId,
                    supervisorEmployeeId: supervisor.supervisorEmployeeId,
                    physicallyChallenged: supervisor.physicallyChallenged
                }

                if ($rootScope.vendorName == undefined) {
                    $scope.editSupervisor = {
                        firstName: supervisor.supervisorName,
                        lastName: supervisor.supervisorlastName,
                        fatherName: supervisor.supervisorFatherName,
                        designation: supervisor.designation,
                        mobileNumber: supervisor.supervisorMobileNumber,
                        supervisorVendorName: supervisor.vendorName,
                        supervisordob: supervisor.supervisordob,
                        email: supervisor.emailId,
                        gender: supervisor.supervisorGender,
                        temporaryAddresss: supervisor.presentAddress,
                        permanentAddress: supervisor.supervisorAddress,
                        supervisorId: supervisor.supervisorId,
                        supervisorEmployeeId: supervisor.supervisorEmployeeId,
                        physicallyChallenged: supervisor.physicallyChallenged
                    }
                }
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });


        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false
        };

        $scope.format = 'dd-MM-yyyy';

        $scope.dobCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'supervisordob': true
                };
            }, 50);
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

        //Convert the dates in DD-MM-YYYY format
        var convertDateUTCYear = function(date) {
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




        // Apply Temporary Address as permanent address 
        $scope.applyPermanentAddress = function(value, address) {
            if (value == true) {
                $scope.permanentAddressDisabled = true;
                $scope.newSupervisor.permanentAddress = address;
            } else {
                $scope.permanentAddressDisabled = false;
                $scope.newSupervisor.physicallyChallenged = "1";
            }
        }
        if (tabValue == 'active') {
            var statusFlg = 'A';

        } else {
            var statusFlg = 'N';
        }

        $scope.modifySupervisor = function(result) {
            if (angular.isDate(result.supervisordob)) {
                result.supervisordob = convertDateUTC(result.supervisordob);
            } else {
                result.supervisordob = result.supervisordob;
            }
            var data = {
                branchId: branchId,
                userId: profileId,
                firstName: result.firstName,
                lastName: result.lastName,
                fatherName: result.fatherName,
                designation: result.designation,
                mobileNumber: result.mobileNumber,
                efmFmVendorMaster: {
                    vendorId: result.supervisorVendorName.Id
                },
                dobDate: result.supervisordob,
                emailId: result.email,
                gender: result.gender,
                presentAddress: result.temporaryAddresss,
                permanentAddress: result.permanentAddress,
                supervisorEmpId: result.supervisorEmployeeId,
                supervisorId: result.supervisorId,
                isActive: statusFlg,
                combinedFacility: combinedFacility
            };


            $http.post('services/fieldApp/modifySupervisorDetails', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == 'DUPMOBNO') {
                        ngDialog.open({
                            template: 'This mobile number is already registered with another user.',
                            plain: true
                        });
                        return false;
                    }
                    ngDialog.open({
                        template: 'Supervisor modified successfully',
                        plain: true
                    });
                    $timeout(function() {
                        $modalInstance.close(result);
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
    /************************************************************************************************************************/
    angular.module('efmfmApp').controller('addVendorCtrl', addVendorCtrl);
    angular.module('efmfmApp').controller('newEscortCtrl', newEscortCtrl);
    angular.module('efmfmApp').controller('newDriverVendorCtrl', newDriverVendorCtrl);
    angular.module('efmfmApp').controller('newVehicleVendorCtrl', newVehicleVendorCtrl);
    angular.module('efmfmApp').controller('editVendorCtrl', editVendorCtrl);
    angular.module('efmfmApp').controller('editDriverCtrl', editDriverCtrl);
    angular.module('efmfmApp').controller('editVehicleCtrl', editVehicleCtrl);
    angular.module('efmfmApp').controller('editEscortCtrl', editEscortCtrl);
    angular.module('efmfmApp').controller('uploadDriverCtrl', uploadDriverCtrl);
    angular.module('efmfmApp').controller('uploadInspectionDocCtrl', uploadInspectionDocCtrl);
    angular.module('efmfmApp').controller('uploadVehicleCtrl', uploadVehicleCtrl);
    angular.module('efmfmApp').controller('uploadEscortCtrl', uploadEscortCtrl);
    angular.module('efmfmApp').controller('vendorDashboardCtrl', vendorDashboardCtrl);
    angular.module('efmfmApp').controller('entityCtrl', entityCtrl);
    angular.module('efmfmApp').controller('releaseDriverCtrl', releaseDriverCtrl);
    angular.module('efmfmApp').controller('checkOutRemarksCtrl', checkOutRemarksCtrl);
    angular.module('efmfmApp').controller('viewAllDocumentsCtrl', viewAllDocumentsCtrl);
    angular.module('efmfmApp').controller('viewAllEscortDocumentsCtrl', viewAllEscortDocumentsCtrl);
    angular.module('efmfmApp').controller('viewAllVehicleDocumentsCtrl', viewAllVehicleDocumentsCtrl);
    angular.module('efmfmApp').controller('addAdhocVendorCtrl', addAdhocVendorCtrl);
    angular.module('efmfmApp').controller('addAdhocDriverCtrl', addAdhocDriverCtrl);
    angular.module('efmfmApp').controller('newSupervisorCtrl', newSupervisorCtrl);
    angular.module('efmfmApp').controller('editSupervisorCtrl', editSupervisorCtrl);


}());