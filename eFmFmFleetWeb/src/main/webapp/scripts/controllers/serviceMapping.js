/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description
@Main Controllers       serviceMappingCtrl
@Modal Controllers      creatNewRouteCtrl, editBucketCtrl, manualtripstartCtrl
@template               partials/home.servicemapping.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
 */
(function() {
    var serviceMappingCtrl = function($scope, $http, $state, $modal, $log,
        $timeout, $confirm, ngDialog, $rootScope, filterFilter) {
        if (!$scope.isCaballocationActive ||
            $scope.isCaballocationActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $scope.advanceIsClicked = false;
            $scope.serachResultFound = false;
            $scope.advanceLabel = 'Advance Search';
            $scope.shiftsTime = [];
            $scope.searchData = [];
            $scope.searchIsEmpty = false;
            $scope.search = {};
            $scope.hstep = 1;
            $scope.mstep = 1;
            $scope.ismeridian = false;
            $scope.addFlag = false;
            $scope.learnRoute = false;
            $scope.branchCode = branchCode;
            $scope.backToBackEnabled = false;
            $scope.backToBackDeleteEnabled = false;
            $scope.dropdownShowB2B = false;
            $scope.editRouteDisabled = false;
            $scope.vehicleDetailsHide = false;
            $scope.suggestiveFlgDropBucket = true;
            $scope.vehicleSaveShow = false;
            $scope.vehicleDeleteShow = false;
            $scope.deleteSuggestButton = false;
            $scope.vendorDropdown = false;
            $scope.pickedUpChecked = false;
            $scope.noShowChecked = false;
            $rootScope.allInspectionVendors = [];
            $scope.vendorNameShow = false;
            $scope.vendorSummary = false;
            $scope.facilityData = {};
            $scope.facilityDetails = userFacilities;
            var array = JSON.parse("[" + combinedFacility + "]");
            $scope.facilityData.listOfFacility = array;

            $scope.searchTypes = [{
                'value': 'Search by VEHICLE',
                'text': 'Vehicle'
            }, {
                'value': 'Search by EMPLOYEE ID',
                'text': 'EmployeeId'
            }];

            $scope.tripTypes = [{
                'value': 'PICKUP',
                'text': 'PICKUP'
            }, {
                'value': 'DROP',
                'text': 'DROP'
            }];

            $scope.isToll = [{
                'value': 'Yes',
                'text': 'Yes'
            }, {
                'value': 'No',
                'text': 'No'
            }];
            $scope.routesStatus = [{
                'value': 'All',
                'text': 'All Routes'
            }, {
                'value': 'Close',
                'text': 'Close Routes'
            }, {
                'value': 'closeNotStarted',
                'text': 'Closed Routes But Not Started'
            }, {
                'value': 'Started',
                'text': 'Started Routes'
            }, {
                'value': 'open',
                'text': 'Open Routes'
            }, {
                'value': 'backToBack',
                'text': 'Back To Back Routes'
            }, {
                'value': 'googleSequence',
                'text': 'Google Sequence'
            }, {
                'value': 'vendorWise',
                'text': 'Vendor Wise'
            }, {
                'value': 'Delivered',
                'text': 'Delivered on driver device'
            }, {
                'value': 'Not Delivered',
                'text': 'Yet to delivered on driver device'
            }];

            $scope.employeePickupStatus = [{
                text: 'No Show',
                value: 'NO'
            }, {
                text: 'PickedUp',
                value: 'B'
            }, {
                text: 'Yet to picked up',
                value: 'N'
            }];
            $scope.employeeDropStatus = [{
                    text: 'No Show',
                    value: 'NO'
                }, {
                    text: 'Dropped',
                    value: 'D'
                }, {
                    text: 'Yet to dropped',
                    value: 'N'
                },
                {
                    text: 'Taxi',
                    value: 'T'
                }
            ];
            $scope.search.routeStatus = {
                'value': 'All',
                'text': 'All Routes'
            };

            $scope.learnRouteDatas = [{
                    value: 'moveRoute',
                    text: 'Move Route'
                },
                {
                    value: 'back2back',
                    text: 'Back 2 Back Route'
                }
            ];

            $('.serviceMappingMenu').addClass('active');
            $scope.editBucket = false;
            $scope.singleRouteData = [];
            $scope.isloaded = false;
            $scope.currentMapindex;
            $scope.moveToRoutes = [];
            $scope.routesData = [];

            $scope.$on('$viewContentLoaded', function() {
                $scope.getZoneData();
            });

            $scope.$watch("search.text", function(query) {
                if ($scope.search.text == "") {
                    $scope.searchIsEmpty = false;
                }
            });

            $scope.initializeTime = function(time) {
                var d = new Date();
                var tempTime = time.split(":");
                d.setHours(tempTime[0]);
                d.setMinutes(tempTime[1]);
                return d;
            }
            $scope.search.searchDate = new Date();
            $scope.openedsearchDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'openedsearchDate': true
                    };
                }, 50);
            };

            $scope.learnRouteClick = function() {
                $scope.learnRoute = true;
            }


            //FUNCTION : Get aLL aCTIVE vENDORS to Populate the drop Down
            $scope.getAllVendors = function() {
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/contract/allActiveVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vendorDetailsData = data;
                        angular.forEach(data, function(item) {
                            $rootScope.allInspectionVendors.push({
                                'name': item.name,
                                'Id': item.vendorId
                            });
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.pluckCardEmployees = function(route) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/pluckCardEmpName.jsp',
                    controller: 'pluckCardEmpNameCtrl',
                    size: 'sm',
                    backdrop: 'static',
                    resolve: {
                        empNameDetails: function() {
                            return route;
                        }
                    }
                });
                modalInstance.result
                    .then(function(result) {});
            };

            $scope.backToBackChange = function(b2bSelected, route) {
                if (b2bSelected) {
                    $scope.backToBackEnabled = true;
                } else {
                    $scope.backToBackEnabled = false;
                    $scope.vehicleSaveShow = false;
                }

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    assignRouteId: route.routeId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http
                    .post('services/zones/checkedinentity/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.checkInEntitiesData = data.checkInList;
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            }
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

            $scope.changeVehicleNumber = function(route, vehicleNumber) {
                $scope.backToBackDeleteEnabled = true;
                $scope.vehicleDetailsHide = true;
                $scope.vehicleSaveShow = true;
                $scope.vehicleDeleteShow = false;
            }

            $scope.saveSuggestedVehicleManualDetails = function(route, vehicleNumber) {

                var dataObj = {
                    assignRouteId: route.routeId,
                    vehicleId: vehicleNumber.vehicleId,
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    combinedFacility: combinedFacility
                }

                $http
                    .post('services/zones/updateManualVehicleNum/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == 'notTravelledInDrop') {
                                    ngDialog.open({
                                        template: 'This vehicle Not travelled in back to back , please check!',
                                        plain: true
                                    });
                                    return false;
                                } else {
                                    ngDialog.open({
                                        template: 'Vehicle Number updated successfully',
                                        plain: true
                                    });
                                }
                                $scope.vehicleSaveShow = false;
                                $scope.vehicleDeleteShow = true;
                                var routesData = $rootScope.routesDataDetails;

                                var index = _.findIndex(routesData, {
                                    routeId: route.routeId
                                });
                            }


                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            }

            $scope.getSuggestiveVehicleNumber = function(route) {
                if (route.vehicleNumber == route.suggestiveVehicleNumber) {
                    ngDialog.open({
                        template: 'Vehicle Number and suggestive Vehicle Number are same, So cant proceed further. Please check!',
                        plain: true
                    });
                    return false;
                }

                var dataObj = {
                    assignRouteId: route.routeId,
                    backTwoBackRouteId: route.backTwoBackRouteId,
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    combinedFacility: combinedFacility
                }

                $http
                    .post('services/zones/updateSuggestedVehicleNum/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                var data = data;
                                if (data.status == 'B2bVehicleNotCheckIn') {
                                    ngDialog.open({
                                        template: 'Please do a checkIn for this back2back vehicle.',
                                        plain: true
                                    });
                                    return false;
                                }
                                $confirm({
                                        text: "Are you sure you want to change this vehicle From" +
                                            ' ' +
                                            route.vehicleNumber +
                                            ' ' +
                                            route.suggestiveVehicleNumber,
                                        title: 'Confirmation',
                                        ok: 'Yes',
                                        cancel: 'No'
                                    })
                                    .then(
                                        function() {
                                            var routesData = $rootScope.routesDataDetails;

                                            var index = _.findIndex(routesData, {
                                                routeId: route.routeId
                                            });
                                            route.isBackTwoBack = 'Y';
                                            $scope.vehicleDetailsHide = true;
                                            $scope.editRouteDisabled = true;
                                            $scope.b2bSelected = true;
                                            $scope.routesData[index].deviceNumber = data.deviceNumber;
                                            $scope.routesData[index].driverName = data.driverName;
                                            $scope.routesData[index].driverNumber = data.driverNumber;
                                            $scope.routesData[index].vehicleNumber = data.vehicleNumber;


                                        });
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });



            }

            $scope.changeVendor = function(vendor, routeId) {

            }

            $scope.noShowChange = function(noShow, pickedUp, empDetails, route) {
                if (noShow) {
                    var firstString = 'Yet to pickedUp';
                    var secondString = 'No Show';
                } else {
                    var secondString = 'Yet to pickedUp';
                    var firstString = 'No Show';
                }
                $confirm({
                        text: 'Are you sure want to change all ' + firstString + ' into ' + secondString + '?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        if (noShow) {
                            $scope.pickedUpChecked = false;
                            for (var i = empDetails.length - 1; i >= 0; i--) {
                                var emp = empDetails[i];
                                if (empDetails[i].employeeStatus.text == "Yet to picked up") {
                                    var dataObj = {
                                        eFmFmEmployeeTravelRequest: {
                                            requestId: emp.requestId
                                        },
                                        efmFmAssignRoute: {
                                            assignRouteId: route.assignRouteId
                                        },
                                        boardedFlg: "NO",
                                        branchId: branchId,
                                        userId: profileId
                                    };
                                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                                        .success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                emp.boardedStatus = "No Show";
                                                emp.boardedFlg = "NO";
                                                emp.employeeStatus.text = "No Show";
                                                emp.employeeStatus.value = "NO";
                                            }
                                        }).
                                    error(function(data, status, headers, config) {});

                                } else {

                                }
                            }
                        } else {
                            for (var i = empDetails.length - 1; i >= 0; i--) {
                                var emp = empDetails[i];
                                if (empDetails[i].employeeStatus.text == "No Show") {
                                    var dataObj = {
                                        eFmFmEmployeeTravelRequest: {
                                            requestId: emp.requestId
                                        },
                                        efmFmAssignRoute: {
                                            assignRouteId: route.assignRouteId
                                        },
                                        boardedFlg: "N",
                                        branchId: branchId,
                                        userId: profileId
                                    };
                                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                                        .success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                emp.boardedStatus = "Yet to picked up";
                                                emp.boardedFlg = "N";
                                                emp.employeeStatus.text = "Yet to picked up";
                                                emp.employeeStatus.value = "N";
                                            }
                                        }).
                                    error(function(data, status, headers, config) {});

                                } else {

                                }
                            }

                        }
                        if (pickedUp) {
                            $scope.noShowChecked = false;
                        } else {}

                    }, function() {

                    });
            }
            $scope.pickedUpChange = function(pickedUp, noShow, empDetails, route) {
                if (pickedUp) {
                    var firstString = 'Yet to pickedUp';
                    var secondString = 'PickedUp';
                } else {
                    var secondString = 'Yet to pickedUp';
                    var firstString = 'PickedUp';
                }
                /*$confirm({
                        text: 'Are you sure want to change all ' + firstString + ' into ' + secondString + '?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {*/

                        if (pickedUp) {

                            for (var i = empDetails.length - 1; i >= 0; i--) {

                                var emp = empDetails[i];
                                if (empDetails[i].employeeStatus.text == "Yet to picked up") {
                                    var dataObj = {
                                        eFmFmEmployeeTravelRequest: {
                                            requestId: emp.requestId
                                        },
                                        efmFmAssignRoute: {
                                            assignRouteId: route.assignRouteId
                                        },
                                        boardedFlg: "B",
                                        branchId: branchId,
                                        userId: profileId
                                    };
                                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                                        .success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                emp.boardedStatus = "PickedUp";
                                                emp.boardedFlg = "B";
                                                emp.employeeStatus.text = "PickedUp";
                                                emp.employeeStatus.value = "B";
                                            }
                                        }).
                                    error(function(data, status, headers, config) {});

                                } else {

                                }
                            }
                        } else {
                            for (var i = empDetails.length - 1; i >= 0; i--) {

                                var emp = empDetails[i];
                                if (empDetails[i].employeeStatus.text == "PickedUp") {
                                    var dataObj = {
                                        eFmFmEmployeeTravelRequest: {
                                            requestId: emp.requestId
                                        },
                                        efmFmAssignRoute: {
                                            assignRouteId: route.assignRouteId
                                        },
                                        boardedFlg: "B",
                                        branchId: branchId,
                                        userId: profileId
                                    };
                                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                                        .success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                emp.boardedStatus = "Yet to picked up";
                                                emp.boardedFlg = "N";
                                                emp.employeeStatus.text = "Yet to picked up";
                                                emp.employeeStatus.value = "N";
                                            }
                                        }).
                                    error(function(data, status, headers, config) {});

                                } else {

                                }
                            }
                        }
                        if (noShow) {
                            $scope.noShowChecked = false;
                        } else {}

                   /* }, function() {*/

                    /*});*/
            }
            $scope.droppedChange = function(pickedUp, noShow, empDetails, route) {
                if (pickedUp) {
                    var firstString = 'Yet to pickedUp';
                    var secondString = 'Dropped';
                } else {
                    var secondString = 'Yet to pickedUp';
                    var firstString = 'Dropped';
                }
/*                $confirm({
                        text: 'Are you sure want to change all ' + firstString + ' into ' + secondString + '?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
*/
                        if (pickedUp) {

                            for (var i = empDetails.length - 1; i >= 0; i--) {


                                var emp = empDetails[i];
                                if (empDetails[i].employeeStatus.text == "Yet to picked up") {
                                    var dataObj = {
                                        eFmFmEmployeeTravelRequest: {
                                            requestId: emp.requestId
                                        },
                                        efmFmAssignRoute: {
                                            assignRouteId: route.assignRouteId
                                        },
                                        boardedFlg: "D",
                                        branchId: branchId,
                                        userId: profileId
                                    };
                                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                                        .success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                emp.boardedStatus = "Dropped";
                                                emp.boardedFlg = "D";
                                                emp.employeeStatus.text = "Dropped";
                                                emp.employeeStatus.value = "D";
                                            }
                                        }).
                                    error(function(data, status, headers, config) {});

                                } else {

                                }

                            }
                        } else {
                            for (var i = empDetails.length - 1; i >= 0; i--) {

                                var emp = empDetails[i];
                                if (empDetails[i].employeeStatus.text == "Dropped") {
                                    var dataObj = {
                                        eFmFmEmployeeTravelRequest: {
                                            requestId: emp.requestId
                                        },
                                        efmFmAssignRoute: {
                                            assignRouteId: route.assignRouteId
                                        },
                                        boardedFlg: "N",
                                        branchId: branchId,
                                        userId: profileId
                                    };
                                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                                        .success(function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                emp.boardedStatus = "Yet to picked up";
                                                emp.boardedFlg = "N";
                                                emp.employeeStatus.text = "Yet to picked up";
                                                emp.employeeStatus.value = "N";
                                            }
                                        }).
                                    error(function(data, status, headers, config) {});

                                } else {

                                }
                            }
                        }
                        if (noShow) {
                            $scope.noShowChecked = false;
                        } else {}
                    /*}, function() {

                    });*/
            }

            $scope.deleteSuggestedVehicleDetails = function(route) {
                $confirm({
                        text: "Are you sure you want to reset this vehicle details",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var dataObj = {
                                assignRouteId: route.routeId,
                                backTwoBackRouteId: route.backTwoBackRouteId,
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                combinedFacility: combinedFacility
                            }
                            $http
                                .post('services/zones/deleteSuggestedVehicleNum/', dataObj)
                                .success(
                                    function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            var data = data;
                                            if (data.status == 'notCheckInvehicle') {
                                                ngDialog.open({
                                                    template: 'Check In vehicle not available',
                                                    plain: true
                                                });
                                                return false;
                                            }

                                            var routesData = $rootScope.routesDataDetails;
                                            var index = _.findIndex(routesData, {
                                                routeId: route.routeId
                                            });
                                            $scope.editRouteDisabled = false;
                                            $scope.b2bSelected = false;
                                            $scope.vehicleDetailsHide = false;
                                            $scope.routesData[index].deviceNumber = data.deviceNumber;
                                            $scope.routesData[index].driverName = data.driverName;
                                            $scope.routesData[index].driverNumber = data.driverNumber;
                                            $scope.routesData[index].vehicleNumber = data.vehicleNumber;
                                        }

                                    }).error(
                                    function(data, status, headers, config) {
                                        // log error
                                    });
                        });
            }

            $scope.deleteSuggestedVehicleManualDetails = function(route, vehicleNumber) {
                $confirm({
                        text: "Are you sure you want to reset this vehicle details",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            if (route.isBackTwoBack == 'N') {
                                $scope.vehicleSaveShow = true;
                                $scope.vehicleDeleteShow = false;
                            } else {
                                $scope.vehicleSaveShow = false;
                                $scope.vehicleDeleteShow = true;
                            }

                            var dataObj = {
                                assignRouteId: route.routeId,
                                vehicleId: vehicleNumber.vehicleId,
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                combinedFacility: combinedFacility
                            }

                            $http
                                .post('services/zones/resetManualVehicleNum/', dataObj)
                                .success(
                                    function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            var data = data;
                                            var routesData = $rootScope.routesDataDetails;
                                            var index = _.findIndex(routesData, {
                                                routeId: route.routeId
                                            });
                                            $scope.editRouteDisabled = false;
                                            $scope.vehicleDetailsHide = true;
                                            $scope.routesData[index].deviceNumber = data.deviceNumber;
                                            $scope.routesData[index].driverName = data.driverName;
                                            $scope.routesData[index].driverNumber = data.driverNumber;
                                            $scope.routesData[index].vehicleNumber = data.vehicleNumber;
                                        }
                                    }).error(
                                    function(data, status, headers, config) {
                                        // log error
                                    });
                        });
            }


            $scope.getZoneData = function() {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http
                    .post('services/zones/allzones/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.Original_zoneData = data.routeDetails;
                                $scope.Original_routeCount = data.totalNumberOfRoutes;

                                $scope.zoneData = data.routeDetails;
                                $scope.numberOfRoutes = data.totalNumberOfRoutes;
                                $scope.numberOfEmployee = data.totalNumberOfEmployee;
                                $scope.escortRequired = data.escortRequired;
                                $scope.yetToBoard = data.yetToBoard;

                                $scope.onBoard = data.onBoard;
                                $scope.totalNumberMaleEmployees = data.totalNumberMaleEmployees;
                                $scope.totalNumberFemaleEmployees = data.totalNumberFemaleEmployees;
                                $scope.searchData = [];
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };

            $scope.getRoutes = function(zone, search, combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                var date = search.searchDate;
                var convert_date = new Date(date);
                var currentMonth = date.getMonth() + 1;
                var currentDate = date.getDate();
                if (currentDate < 10) {
                    currentDate = '0' + currentDate;
                }
                if (currentMonth < 10) {
                    currentMonth = '0' + currentMonth;
                }
                var todate = currentDate + '-' + currentMonth + '-' +
                    convert_date.getFullYear();
                if (search.advanceflag == 'true' && search.tripType) {
                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        eFmFmRouteAreaMapping: {
                            eFmFmZoneMaster: {
                                zoneId: zone.routeId
                            }
                        },
                        searchType: search.routeStatus.value,
                        tripType: search.tripType.value,
                        time: search.shiftTime.shiftTime + ':00',
                        toDate: todate,
                        advanceFlag: true,
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };

                } else {
                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        eFmFmRouteAreaMapping: {
                            eFmFmZoneMaster: {
                                zoneId: zone.routeId
                            }
                        },
                        advanceFlag: false,
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };

                }

                $http
                    .post('services/zones/allroutes/', dataObj)
                    .success(
                        function(data, status, headers, config, statusText) {
                            if (data.status != "invalidRequest") {
                                $scope.routesData = data.routeDetails;
                                $rootScope.routesDataDetails = data.routeDetails;
                                angular
                                    .forEach(
                                        $scope.routesData,
                                        function(item) {
                                            item.vendorId = {
                                                vendorId: item.vendorId
                                            }
                                            if (item.suggestiveVehicleNumber == "No") {
                                                $scope.suggestiveFlgDropBucket = false;
                                                $scope.b2bSelected = false;
                                            } else {
                                                $scope.suggestiveFlgDropBucket = true;
                                                $scope.b2bSelected = true;
                                            }

                                            if (item.isBackTwoBack == 'Y') {
                                                $scope.vehicleDetailsHide = true;
                                                $scope.b2bSelectedShow = true;
                                                $scope.b2bSelected = true;
                                                if (item.tripType == 'PICKUP' && item.bucketStatus == 'Y') {
                                                    $scope.editRouteDisabled = true;
                                                } else {
                                                    $scope.editRouteDisabled = false;
                                                }
                                            } else {
                                                $scope.vehicleDetailsHide = false;
                                                $scope.b2bSelectedShow = false;
                                                $scope.b2bSelected = false;
                                            }

                                            item.isVehicleEmpty = false;
                                            if (item.tripType == "DROP") {
                                                angular.forEach(item.empDetails, function(item) {
                                                    item.upArrows = false;
                                                    item.downArrows = false;
                                                    item.isZoneclicked = false;
                                                    item.isUpdateClicked = false;
                                                    item.dropSequence = item.pickUpTime;
                                                    item.employeeStatus = {
                                                        text: item.boardedStatus,
                                                        value: item.boardedFlg
                                                    };
                                                });
                                            } else {
                                                angular.forEach(item.empDetails, function(item) {
                                                    item.upArrows = false;
                                                    item.downArrows = false;
                                                    item.isZoneclicked = false;
                                                    item.isUpdateClicked = false;
                                                    item.createNewAdHocTime = $scope
                                                        .initializeTime(item.pickUpTime);
                                                    item.employeeStatus = {
                                                        text: item.boardedStatus,
                                                        value: item.boardedFlg
                                                    };
                                                });
                                            }
                                            item.editClicked = false;
                                            item.closeBucketClicked = true;
                                            item.addFlag = false;
                                        });

                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };

            $scope.createNewRoute = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/creatNewRouteModal.jsp',
                    controller: 'creatNewRouteCtrl',
                    backdrop: 'static',
                    resolve: {
                        uiZone: function() {
                            return $scope.zoneData;
                        }
                    }
                });

                modalInstance.result
                    .then(function(result) {
                        //Check if the Zone already exist
                        var zoneExistIndex = _.findIndex($scope.zoneData, {
                            routeId: result.zoneRouteId
                        });
                        if (zoneExistIndex >= 0) {
                            $scope.routesData
                                .unshift({
                                    'routeId': result.routeId,
                                    'tripType': result.tripType,
                                    'shiftTime': result.shiftTime,
                                    'tripStatus': result.tripStatus,
                                    'routeName': result.zoneName,
                                    'escortRequired': result.escortRequired,
                                    'escortName': result.escortName,
                                    'editClicked': true,
                                    'closeBucketClicked': true,
                                    'bucketStatus': result.bucketStatus,
                                    'capacity': result.capacity,
                                    'deviceNumber': result.deviceNumber,
                                    'driverId': result.driverId,
                                    'driverName': result.driverName,
                                    'driverNumber': result.driverNumber,
                                    'vehicleAvailableCapacity': result.vehicleAvailableCapacity,
                                    'vehicleId': result.vehicleId,
                                    'vehicleNumber': result.vehicleNumber,
                                    'vehicleStatus': result.vehicleStatus,
                                    'vendorId': result.vendorId,
                                    'empDetails': [],
                                    'pragnentLady': 0,
                                    'physicallyChallenged': 0,
                                    'isInjured': 0
                                });
                            $scope.zoneData[zoneExistIndex].NumberOfRoutes = $scope.zoneData[zoneExistIndex].NumberOfRoutes + 1;
                        } else {
                            $scope.zoneData.unshift({
                                'NumberOfRoutes': 1,
                                'routeId': result.zoneRouteId,
                                'routeName': result.zoneName,
                                'pragnentLady': 0,
                                'physicallyChallenged': 0,
                                'isInjured': 0
                            });
                        }
                    });
            };

            $scope.advanceSearch = function() {
                if ($scope.advanceIsClicked) {
                    $scope.advanceIsClicked = false;
                    $scope.search.advanceflag = 'false';
                    $scope.advanceLabel = 'Advance Search';
                } else {
                    $scope.advanceIsClicked = true;
                    $scope.search.advanceflag = 'true';
                    $scope.advanceLabel = 'Search Employee';
                }
            };

            $scope.backtoMainList = function() {
                $scope.search.advanceflag = 'false';
                $scope.serachResultFound = false;
                $scope.advanceIsClicked = false;
                $scope.search.tripType = '';
                $scope.search.shiftTime = '';
                $scope.search.employeeId = '';
                $scope.zoneData = $scope.Original_zoneData;
                $scope.numberOfRoutes = $scope.Original_routeCount;

            };

            $scope.setSearchRouteStatus = function(value) {
                $scope.vendorNameShow = false;
                if (value.text == 'Vendor Wise') {
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/vendorListModal.jsp',
                        controller: 'vendorListCtrl',
                        backdrop: 'static',
                        resolve: {

                        }
                    });

                    modalInstance.result.then(function(result) {
                        $scope.selectedVendor = result;
                        $scope.vendorName = result.name;
                        $scope.vendorNameShow = true;

                    });
                }
            }


            $scope.setSearchPlaceHoler = "select searchBy Option";
            $scope.setSearchBy = function(searchBy) {
                $scope.search.text = '';
                if (searchBy) {
                    if (searchBy.value == "Search by VEHICLE") {
                        $scope.setSearchPlaceHoler = "Enter Vehicle Number";
                    } else if (searchBy.value == "Search by EMPLOYEE ID") {
                        $scope.setSearchPlaceHoler = "Enter Employee ID";
                    }
                } else {
                    $scope.setSearchPlaceHoler = "select searchBy Option";
                }
            };


            $scope.searchBy = function(searchText, searchType) {
                $scope.vendorDropdown = false;
                if (searchText && angular.isObject(searchType)) {
                    $scope.searchIsEmpty = false;
                    var employeeId = searchText;
                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        employeeId: employeeId,
                        searchType: searchType.text,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http
                        .post('services/zones/employeesearchinroute/',
                            dataObj)
                        .success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.routeDetails.length == 0) {
                                        ngDialog.open({
                                            template: 'This search type does not exist in routes please check entered correct value.',
                                            plain: true
                                        });
                                    } else {
                                        $scope.serachResultFound = true;
                                        $scope.zoneData = data.routeDetails;
                                        $scope.numberOfRoutes = data.totalNumberOfRoutes;
                                        $scope.vendorSummary = false;

                                    }
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                } else {
                    $scope.searchIsEmpty = true;
                }
            };

            $scope.searchByTripTypeShift = function(search, combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (search.routeStatus.value == "All") {

                } else {
                    if (angular.isObject($scope.selectedVendor)) {
                        search.routeStatus.value = $scope.selectedVendor.vendorId;
                    }
                }

                $scope.search.advanceflag = 'true';
                $scope.vendorDropdown = true;
                if (search.shiftTime && search.tripType) {
                    var date = search.searchDate;
                    var convert_date = new Date(date);
                    var currentMonth = date.getMonth() + 1;
                    var currentDate = date.getDate();

                    if (currentDate < 10) {
                        currentDate = '0' + currentDate;
                    }
                    if (currentMonth < 10) {
                        currentMonth = '0' + currentMonth;
                    }
                    var todate = currentDate + '-' + currentMonth + '-' +
                        convert_date.getFullYear();
                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        tripType: search.tripType.value,
                        time: search.shiftTime.shiftTime + ':00',
                        searchType: search.routeStatus.value,
                        toDate: todate,
                        userId: profileId,
                        combinedFacility: combinedFacilityId

                    };

                    $http
                        .post('services/zones/triptypesearchinroute/',
                            dataObj)
                        .success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.routeDetails.length == 0) {
                                        ngDialog.open({
                                            template: 'Route not Exist for this selection.',
                                            plain: true
                                        });
                                        $scope.zoneData = '';
                                        $scope.advanceserachResult = false;
                                    } else {
                                        $scope.serachResultFound = true;
                                        $scope.advanceserachResult = true;
                                        $scope.zoneData = data.routeDetails;
                                        $scope.numberOfRoutes = data.totalNumberOfRoutes;
                                        $scope.numberOfEmployee = data.totalNumberOfEmployee;
                                        $scope.vendorSummary = true;
                                        $scope.vendorSummaryData = data;
                                        $scope.yetToBoard = data.yetToBoard;
                                        $scope.escortRequired = data.escortRequired;
                                        $scope.onBoard = data.onBoard;
                                        $scope.totalNumberMaleEmployees = data.totalNumberMaleEmployees;
                                        $scope.totalNumberFemaleEmployees = data.totalNumberFemaleEmployees;
                                    }
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                }
            };

            $scope.rememberShiftRoute = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/rememberShiftRoute.jsp',
                    controller: 'rememberShiftRouteCtrl',
                    backdrop: 'static',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(result) {});

            }

            /*Complete Route*/

            $scope.completeRoute = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/completeRoute.jsp',
                    controller: 'completeRouteCtrl',
                    backdrop: 'static',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(result) {});

            }


            $scope.setSearchTripType = function(tripType, facilityData) {
                for (var i = $scope.routesStatus.length - 1; i >= 0; i--) {
                    if ($scope.routesStatus[i].value == "checkedIn" || $scope.routesStatus[i].value == "checkedOut") {
                        $scope.routesStatus.splice(i, 1)
                    } else {}
                }
                if (tripType) {
                    if (tripType.value == "PICKUP") {

                    } else if (tripType.value == "DROP") {}
                } else {}
                if (angular.isObject(tripType)) {
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
                        combinedFacility: facilityData.toString()
                    };
                    $http.post('services/trip/tripshiftime/', data).success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });
                                $scope.search.shiftTime = $scope.shiftsTime[0];
                            }

                        }).error(function(data, status, headers, config) {});
                } else {
                    $scope.shiftsTime = [];
                }
            };

            $scope.changeRoutesDropDown = function(employee, moveToZone, employeeDetails) {
                var selectedEmployee = filterFilter(employeeDetails, function(employee) {
                    return employee.zoneCheckBox;
                });
                $scope.selectedEmployeeList = selectedEmployee.map(function(obj) {
                    return obj.employeeId;
                }).join(',');
                employee.isZoneclicked = true;
                angular.forEach($scope.routesData, function(item) {
                    angular.forEach(item.empDetails, function(item) {
                        item.isZoneclicked = false;
                    });
                });
                employee.isZoneclicked = true;

                $scope.routesDropDown = [];
                if (moveToZone) {

                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        eFmFmRouteAreaMapping: {
                            eFmFmZoneMaster: {
                                zoneId: moveToZone.routeId
                            }
                        },
                        advanceFlag: "false",
                        userId: profileId,
                        combinedFacility: combinedFacility

                    };
                    $http.post('services/zones/allroutes/', dataObj).success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.routesDropDown = data.routeDetails;
                            }
                        }).error(function(data, status, headers, config) {
                        // log error
                    });
                } else {
                    $scope.routesDropDown = [];
                    ngDialog.open({
                        template: 'Please Select a Zone',
                        plain: true
                    });
                }
            }



            $scope.openMap = function(route, zone, size) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/singleRouteMap.jsp',
                    controller: 'singleRouteMapViewCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        routeId: function() {
                            return route.routeId;
                        },
                        waypoints: function() {
                            return route.waypoints;
                        },
                        baseLatLong: function() {
                            return route.baseLatLong;
                        },
                        route: function() {
                            return route;
                        }
                    }
                });


                /*$state.go('home.serviceRouteMap', {
                	'routeId' : route.routeId,
                	'waypoints' : route.waypoints,
                	'baseLatLong' : route.baseLatLong
                });*/
            };
            $scope.printModal = function(route) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/printModal.jsp',
                    controller: 'printRouteCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        printZone: function() {
                            return route;
                        }
                    }
                });

                modalInstance.result
                    .then(function(result) {


                    });

            };


            $scope.moveToRoute = function(moveTo, moveToZone, route, zone,
                employee, index) {
                var rowId = '#row' + zone.routeId + route.routeId +
                    employee.employeeId + employee.requestId;
                if (!moveTo) {} else {
                    $confirm({
                            text: "Are you sure you want to move this employee from " +
                                zone.routeName +
                                " to " +
                                moveTo.routeName + " ?",
                            title: 'Delete Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })
                        .then(
                            function() {
                                //Checking if Routes are moving with in the Same Zone
                                if (moveToZone.routeName === route.routeName) {
                                    // Check if the User has selected the Same route as the Current one
                                    if (moveTo.routeId != route.routeId) {
                                        var moveFromIndex = _
                                            .findIndex(
                                                $scope.routesData, {
                                                    routeId: route.routeId
                                                });
                                        var moveToIndex = _
                                            .findIndex(
                                                $scope.routesData, {
                                                    routeId: moveTo.routeId
                                                });

                                        if ($scope.routesData[moveToIndex].bucketStatus === 'Y') {
                                            ngDialog.open({
                                                template: 'This bucket ' + moveTo.routeId + ' is already Closed. Please first click on edit bucket.',
                                                plain: true
                                            });
                                        } else {
                                            if ($scope.routesData[moveToIndex].vehicleAvailableCapacity > 0 &&
                                                route.shiftTime == moveTo.shiftTime &&
                                                route.tripType == moveTo.tripType &&
                                                route.tripActualAssignDate == moveTo.tripActualAssignDate) {

                                                var dataObj = {
                                                    eFmFmClientBranchPO: {
                                                        branchId: branchId
                                                    },
                                                    userId: profileId,
                                                    assignRouteId: route.assignRouteId,
                                                    selectedAssignRouteId: moveTo.assignRouteId,
                                                    requestId: employee.requestId,
                                                    combinedFacility: combinedFacility
                                                };
                                                $http
                                                    .post(
                                                        'services/zones/swapemployee/',
                                                        dataObj)
                                                    .success(
                                                        function(
                                                            data,
                                                            status,
                                                            headers,
                                                            config) {
                                                            if (data.status != "invalidRequest") {
                                                                if (data.status == 'failed') {
                                                                    ngDialog.open({
                                                                        template: 'Sorry you can not swap this emplyee in selected zone.',
                                                                        plain: true
                                                                    });
                                                                } else {
                                                                    $scope.routesData[moveToIndex].empDetails
                                                                        .push({
                                                                            "tripType": $scope.routesData[moveFromIndex].empDetails[index].tripType,
                                                                            "address": $scope.routesData[moveFromIndex].empDetails[index].address,
                                                                            "gender": $scope.routesData[moveFromIndex].empDetails[index].gender,
                                                                            "projectId": $scope.routesData[moveFromIndex].empDetails[index].projectId,
                                                                            "requestWithProject": $scope.routesData[moveFromIndex].empDetails[index].requestWithProject,
                                                                            "tripConfirmation": $scope.routesData[moveFromIndex].empDetails[index].tripConfirmation,
                                                                            "isInjured": $scope.routesData[moveFromIndex].empDetails[index].isInjured,
                                                                            "requestColor": $scope.routesData[moveFromIndex].empDetails[index].requestColor,
                                                                            "name": $scope.routesData[moveFromIndex].empDetails[index].name,
                                                                            "physicallyChallenged": $scope.routesData[moveFromIndex].empDetails[index].physicallyChallenged,
                                                                            "pragnentLady": $scope.routesData[moveFromIndex].empDetails[index].pragnentLady,
                                                                            "isVIP": $scope.routesData[moveFromIndex].empDetails[index].isVIP,
                                                                            "boardedFlg": $scope.routesData[moveFromIndex].empDetails[index].boardedFlg,
                                                                            "tripDetailId": $scope.routesData[moveFromIndex].empDetails[index].tripDetailId,
                                                                            "locationFlg": $scope.routesData[moveFromIndex].empDetails[index].locationFlg,
                                                                            "locationWayPointsIds": $scope.routesData[moveFromIndex].empDetails[index].locationWayPointsIds,
                                                                            "empArea": $scope.routesData[moveFromIndex].empDetails[index].empArea,
                                                                            "pickUpTime": $scope.routesData[moveFromIndex].empDetails[index].pickUpTime,
                                                                            "boardedStatus": $scope.routesData[moveFromIndex].empDetails[index].boardedStatus,
                                                                            "requestType": $scope.routesData[moveFromIndex].empDetails[index].requestType,
                                                                            "tripTime": $scope.routesData[moveFromIndex].empDetails[index].tripTime,
                                                                            "empComingStatus": $scope.routesData[moveFromIndex].empDetails[index].empComingStatus,
                                                                            "employeeId": $scope.routesData[moveFromIndex].empDetails[index].employeeId,
                                                                            "employeeStatus": {
                                                                                "text": $scope.routesData[moveFromIndex].empDetails[index].boardedStatus,
                                                                                "value": $scope.routesData[moveFromIndex].empDetails[index].boardedFlg
                                                                            },
                                                                            "employeeNumber": $scope.routesData[moveFromIndex].empDetails[index].employeeNumber
                                                                        });
                                                                    $(
                                                                            rowId)
                                                                        .hide(
                                                                            'slow');

                                                                    route.vehicleAvailableCapacity = data.currentRouteAvailableCapacity;
                                                                    $scope.routesData[moveToIndex].vehicleAvailableCapacity = data.selectedRouteAvailableCapacity;
                                                                    $scope.moveToZone = '';

                                                                    if (route.capacity -
                                                                        route.vehicleAvailableCapacity <= 1) {
                                                                        route.isVehicleEmpty = true;
                                                                    }
                                                                    route.bucketStatus = 'N';
                                                                    ngDialog.open({
                                                                        template: 'Employee swapped successfully, please close the bucket.',
                                                                        plain: true
                                                                    });
                                                                    $scope.getRoutes(zone);
                                                                }
                                                            }
                                                        })
                                                    .error(
                                                        function(
                                                            data,
                                                            status,
                                                            headers,
                                                            config) {
                                                            // log error
                                                        });
                                            } else {
                                                ngDialog.open({
                                                    template: 'Sorry you can not move this employee in to the selected route.',
                                                    plain: true
                                                });

                                            }
                                        }
                                    } else {
                                        ngDialog.open({
                                            template: 'Please change the route you can not move the employee in the same Route.',
                                            plain: true
                                        });
                                    }
                                } else {
                                    var moveFromIndex = _.findIndex(
                                        $scope.routesData, {
                                            routeId: route.routeId
                                        });
                                    var routeIndex = _
                                        .findIndex(
                                            $scope.routesDropDown, {
                                                routeId: moveTo.routeId
                                            });
                                    if (routeIndex >= 0) {
                                        if ($scope.routesDropDown[routeIndex].bucketStatus == 'Y') {
                                            ngDialog.open({
                                                template: 'This bucket ' + moveTo.routeName + ' is already Closed. Please first click on edit bucket.',
                                                plain: true
                                            });

                                        } else {
                                            if ($scope.routesDropDown[routeIndex].vehicleAvailableCapacity > 0 &&
                                                route.shiftTime == moveTo.shiftTime &&
                                                route.tripType == moveTo.tripType &&
                                                route.tripActualAssignDate == moveTo.tripActualAssignDate) {

                                                var dataObj = {
                                                    eFmFmClientBranchPO: {
                                                        branchId: branchId
                                                    },
                                                    userId: profileId,
                                                    assignRouteId: route.assignRouteId,
                                                    selectedAssignRouteId: moveTo.assignRouteId,
                                                    requestId: employee.requestId,
                                                    combinedFacility: combinedFacility

                                                };
                                                $http
                                                    .post(
                                                        'services/zones/swapemployee/',
                                                        dataObj)
                                                    .success(
                                                        function(
                                                            data,
                                                            status,
                                                            headers,
                                                            config) {
                                                            if (data.status != "invalidRequest") {
                                                                if (data.status == 'failed') {
                                                                    ngDialog.open({
                                                                        template: 'Sorry you can not swap this emplyee in selected zone.',
                                                                        plain: true
                                                                    });
                                                                } else {
                                                                    $(
                                                                            rowId)
                                                                        .hide(
                                                                            'slow');
                                                                    $scope.routesData[moveFromIndex].vehicleAvailableCapacity = data.currentRouteAvailableCapacity;

                                                                    if (route.capacity -
                                                                        route.vehicleAvailableCapacity <= 1) {
                                                                        route.isVehicleEmpty = true;
                                                                    }
                                                                    // Get the Routes and Employee Details to render the correct Oder
                                                                    $scope.getRoutes(zone);
                                                                    ngDialog.open({
                                                                        template: 'Employee swapped successfully.',
                                                                        plain: true
                                                                    });

                                                                }
                                                                $scope.moveToZone = '';
                                                            }
                                                        })
                                                    .error(
                                                        function(
                                                            data,
                                                            status,
                                                            headers,
                                                            config) {
                                                            // log error
                                                        });
                                            } else {
                                                ngDialog.open({
                                                    template: 'Sorry you can not move this employee in to the selected route.',
                                                    plain: true
                                                });
                                            }

                                        }
                                    }
                                }
                            });
                }
            };

            $scope.closeBucket = function(route, zone) {

                var dummyVehicleNumberCheck = route.vehicleNumber;
                if (dummyVehicleNumberCheck.search("DUMMY") != -1) {
                    ngDialog.open({
                        template: 'Sorry this dummy vehicle please first update the dummy entities.',
                        plain: true
                    });
                    return false;
                }
                var x = _.findIndex($scope.routesData, {
                    routeId: route.routeId
                });

                var reArrangeEmployeeArray = $scope.routesData[x].empDetails;

                route.closeBucketClicked = true;
                route.editClicked = false;
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    assignRouteId: route.routeId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http
                    .post('services/zones/bucketclose/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.suggestiveVehicleNumber == 'No') {
                                    $scope.suggestiveFlgDropBucket = false;
                                } else {
                                    $scope.suggestiveFlgDropBucket = true;
                                }
                                $scope.suggestiveVehicleNumber = data.suggestiveVehicleNumber;
                                if (data.status == 'routingNotComplete') {
                                    ngDialog.open({
                                        template: 'Sorry you can not close this route ,Please press the complete routing first.',
                                        plain: true
                                    });
                                } else if (data.status == 'notClose' && data.type == 'DROP') {
                                    ngDialog.open({
                                        template: data.errorMessage,
                                        plain: true
                                    });
                                } else if (data.status == 'notClose' && data.type == 'PICKUP') {
                                    ngDialog.open({
                                        template: data.errorMessage,
                                        plain: true
                                    });
                                } else if (data.status == 'duplicat') {
                                    ngDialog.open({
                                        template: 'Sorry you can not allocate a second trip of same shift to same driver.',
                                        plain: true
                                    });
                                } else {
                                    route.bucketStatus = 'Y';
                                    route.escortName = data.escortName;
                                    $scope.routesData[x].vehicleAvailableCapacity = data.availableCapacity;

                                    ngDialog.open({
                                        template: 'Route close successfully.',
                                        plain: true
                                    });
                                }
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };
            $scope.addNewEmployee = function(route) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/serviceMapping/addNewEmployeeModal.jsp',
                    controller: 'addNewEmployeeCtrl',
                    backdrop: 'static',
                    resolve: {
                        routeDetails: function() {
                            return route;
                        }
                    }
                });
                modalInstance.result
                    .then(function(result) {});


            };
            $scope.addNewRoute = function(route) {
                $confirm({
                        text: 'Are you sure want to create new route?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var dataObj = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            time: route.shiftTime,
                            selectedAssignRouteId: route.zoneId,
                            tripType: route.tripType,
                            toDate: route.tripActualAssignDate,
                            userId: profileId,
                            nodalPoints: 0,
                            combinedFacility: combinedFacility
                        };
                        if (route.routeType == "normal") {
                            dataObj.routeType = "normal";
                            dataObj.routeGenerationType = "normalRoute";
                        } else {
                            dataObj.routeType = "nodal";
                            dataObj.routeGenerationType = "nodal";

                        }
                        $http.post('services/zones/savecreatebucket', dataObj).success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.status == 'fail') {
                                        ngDialog.open({
                                            template: 'No driver available please checked in driver first.',
                                            plain: true
                                        });
                                        $timeout(function() {
                                            $modalInstance.dismiss('cancel');
                                            ngDialog.close();
                                        }, 3000);
                                    } else {
                                        $scope.routesData.push(data);
                                        ngDialog.open({
                                            template: 'Empty route created successfully.',
                                            plain: true
                                        });
                                        $timeout(function() {
                                            ngDialog.close();
                                        }, 3000);
                                    }
                                }
                            }).error(function(data, status, headers, config) {
                            // log error
                        });



                    }, function() {

                    });


            };
            $scope.manualTripStart = function(route, zone) {

                $confirm({
                        text: 'Are you sure want to start this trip?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var dummyVehicleNumberCheck = route.vehicleNumber;
                        if (dummyVehicleNumberCheck.search("DUMMY") != -1) {
                            ngDialog.open({
                                template: 'Sorry this dummy vehicle please first update the vehicle and driver to start the trip.',
                                plain: true
                            });
                            return false;
                        }

                        $scope.regexDecimalNumbers = /^[0-9]+(\.[0-9]{2})?$/;
                        $scope.tripStatus = route.tripStatus;
                        $scope.alertMessage;
                        $scope.alertHint;

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

                        var currentDate = convertDateUTC(new Date()) + ' ' + convertToTime(new Date());

                        if (route.tripStatus == "allocated") {
                            var dataObj = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                assignRouteId: route.routeId,
                                odometerStartKm: 0,
                                time: currentDate,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            };
                            $http.post('services/trip/tripstartkm/', dataObj).success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        ngDialog.open({
                                            template: 'Trip Started',
                                            plain: true
                                        });
                                        route.tripStatus = "Started";
                                        $timeout(function() {
                                            ngDialog.close();
                                        }, 3000);
                                    }
                                }).error(function(data, status, headers, config) {
                                // log error
                            });
                        } else {
                            var dummyVehicleNumberCheck = route.vehicleNumber;
                            if (dummyVehicleNumberCheck.search("DUMMY") != -1) {
                                ngDialog.open({
                                    template: 'Sorry this dummy vehicle please first update the vehicle and driver to start the trip.',
                                    plain: true
                                });
                                return false;
                            }

                            var dataObj = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                assignRouteId: route.routeId,
                                odometerEndKm: 0,
                                time: currentDate,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            };
                            $http
                                .post('services/trip/tripcomplete/', dataObj)
                                .success(
                                    function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == 'fail') {
                                                ngDialog.open({
                                                    template: 'Please enter correct Odometer reading.',
                                                    plain: true
                                                });
                                            } else {

                                                route.tripStatus = "Completed";
                                                ngDialog.open({
                                                    template: 'Trip Completed',
                                                    plain: true
                                                });
                                                $timeout(function() {
                                                    $modalInstance.close(route.tripStatus);
                                                    ngDialog.close();
                                                }, 3000);
                                            }
                                        }
                                    }).error(
                                    function(data, status, headers, config) {
                                        // log error
                                    });
                        }

                    }, function() {

                    });


            };

            $scope.manualTripEnd = function(route, zone) {
                $confirm({
                        text: 'Are you sure want to end this trip?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

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

                        var currentDate = convertDateUTC(new Date()) + ' ' + convertToTime(new Date());

                        var dataObj = {
                            branchId: branchId,
                            assignRouteId: route.routeId,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/zones/employeeStatus/', dataObj).success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.status == "success") {
                                        var dummyVehicleNumberCheck = route.vehicleNumber;
                                        if (dummyVehicleNumberCheck.search("DUMMY") != -1) {
                                            ngDialog.open({
                                                template: 'Sorry this dummy vehicle please first update the vehicle and driver to start the trip.',
                                                plain: true
                                            });
                                            return false;
                                        }

                                        var dataObj = {
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            },
                                            assignRouteId: route.routeId,
                                            odometerEndKm: 0,
                                            time: currentDate,
                                            userId: profileId,
                                            combinedFacility: combinedFacility
                                        };
                                        $http
                                            .post('services/trip/tripcomplete/', dataObj)
                                            .success(
                                                function(data, status, headers, config) {
                                                    if (data.status != "invalidRequest") {
                                                        if (data.status == 'fail') {
                                                            ngDialog.open({
                                                                template: 'Please enter correct Odometer reading.',
                                                                plain: true
                                                            });
                                                        } else {

                                                            route.tripStatus = "Completed";
                                                            ngDialog.open({
                                                                template: 'Trip Completed',
                                                                plain: true
                                                            });
                                                            $timeout(function() {
                                                                ngDialog.close();
                                                            }, 3000);
                                                        }
                                                    }
                                                }).error(
                                                function(data, status, headers, config) {
                                                    // log error
                                                });

                                        $(".route" + zone.routeId + route.routeId)
                                            .hide('slow');

                                    } else {
                                        ngDialog.open({
                                            template: 'Please update selected route employees status before completing the route',
                                            plain: true
                                        });
                                    }
                                }
                            }).error(function(data, status, headers, config) {

                        });
                    });
            };

            $scope.editBucket = function(route, zone, size) {
                if (route.bucketStatus == 'Y') {
                    route.bucketStatus = 'N';
                } else
                    route.bucketStatus == 'Y';

                route.editClicked = true;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/editBucketModal.jsp',
                    controller: 'editBucketCtrl',
                    backdrop: 'static',
                    size: size,
                    resolve: {
                        zone: function() {
                            return zone;
                        },
                        route: function() {
                            return route;
                        }
                    }
                });

                modalInstance.result
                    .then(function(result) {
                        var driverNumberFlg = result.vehicleNumber.vehicleNumber

                        var index = _.findIndex($scope.routesData, {
                            routeId: route.routeId
                        });
                        if (route.escortRequired != 'N' &&
                            route.escortName !== 'Escort Required But Not Available') {
                            $scope.routesData[index].escortName = result.escortName.escortName;

                        }

                        $scope.routesData[index].deviceNumber = result.deviceId.deviceId;
                        $scope.routesData[index].mobileNumber = result.driverNumber;
                        $scope.routesData[index].vehicleNumber = result.vehicleNumber.vehicleNumber;
                        $scope.routesData[index].driverNumber = result.driverNumber;
                        $scope.routesData[index].driverName = result.driverName.driverName;
                        $scope.routesData[index].vehicleAvailableCapacity = result.availableCapacity;
                        $scope.routesData[index].checkInId = result.checkInId;
                        $scope.routesData[index].driverNumberFlg = driverNumberFlg.substr(0, 5);
                    });
            };


            $scope.isTollChange = function(toll, route) {
                var data = {
                    assignRouteId: route.routeId,
                    userId: profileId,
                    isToll: toll.value,
                    combinedFacility: combinedFacility
                };

                $http.post('services/zones/toll/', data).
                success(function(data, status, headers, config) {}).error(function(data, status, headers, config) {});

            };




            $scope.deleteEmployee = function(route, zone, employee,
                employeeIndex, combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                var routeIndex = _.findIndex($scope.routesData, {
                    routeId: route.routeId
                });
                $confirm({
                        text: 'Are you sure you want to delete this row?',
                        title: 'Delete Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var dataObj = {
                                branchId: branchId,
                                userId: profileId,
                                eFmFmEmployeeTravelRequest: {
                                    requestId: employee.requestId
                                },
                                efmFmAssignRoute: {
                                    assignRouteId: route.routeId
                                },
                                combinedFacility: combinedFacilityId
                            };
                            $http
                                .post(
                                    'services/zones/cancelbucketrequest/',
                                    dataObj)
                                .success(
                                    function(data, status,
                                        headers, config) {
                                        if (data.status != "invalidRequest") {
                                            $timeout(function() {
                                                $('#row' + zone.routeId + route.routeId + employee.employeeId + employee.requestId).hide('slow');
                                                route.escortName = data.escortName;
                                                ngDialog.open({
                                                    template: 'Request deleted successfully.',
                                                    plain: true
                                                });
                                                $scope.routesData[routeIndex].vehicleAvailableCapacity = data.availableCapacity;

                                                if (route.capacity - route.vehicleAvailableCapacity <= 1) {
                                                    route.isVehicleEmpty = true;
                                                }
                                            });
                                        }
                                    }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        });
            };

            $scope.moveDown = function(route, zone, employee, index) {
                var x = _.findIndex($scope.routesData, {
                    routeId: route.routeId
                });
                route.closeBucketClicked = false;
                var a = zone.routeId + route.routeId + employee.employeeId;
                var row = $('#down' + a).parents("tr:first");
                row.insertAfter(row.next());
                if (index != $scope.routesData[x].empDetails.length - 1) {
                    var temp = $scope.routesData[x].empDetails[index];
                    $scope.routesData[x].empDetails[index] = $scope.routesData[x].empDetails[index + 1];
                    $scope.routesData[x].empDetails[index + 1] = temp;
                    for (i = 0; i < $scope.routesData[x].empDetails.length; i++) {}
                }

            };

            $scope.moveUp = function(route, zone, employee, index) {
                var x = _.findIndex($scope.routesData, {
                    routeId: route.routeId
                });
                route.closeBucketClicked = false;
                var a = zone.routeId + route.routeId + employee.employeeId;
                var row = $('#up' + a).parents("tr:first");
                row.insertBefore(row.prev());

                if (index != 0) {
                    var temp = $scope.routesData[x].empDetails[index];
                    $scope.routesData[x].empDetails[index] = $scope.routesData[x].empDetails[index - 1];
                    $scope.routesData[x].empDetails[index - 1] = temp;
                    for (i = 0; i < $scope.routesData[x].empDetails.length; i++) {}
                }
            };

            $scope.deleteEmptyRoute = function(route, zone, combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                $confirm({
                        text: 'Are you sure you want to delete this Route?',
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
                                assignRouteId: route.routeId,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            };
                            $http
                                .post(
                                    'services/zones/deleteroute/',
                                    data)
                                .success(
                                    function(data, status,
                                        headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == 'failed') {
                                                ngDialog.open({
                                                    template: 'Sorry you can not delete started routes.',
                                                    plain: true
                                                });
                                            } else {
                                                ngDialog.open({
                                                    template: 'Route deleted successfully.',
                                                    plain: true
                                                });
                                                $('.route' + zone.routeId + route.routeId).hide('slow');
                                            }
                                        }
                                    }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        });
            };
            $scope.searchEmployeesInZone = function(search) {};

            //Controller: importData.js
            $scope.uploadRoute = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/uploadRoute.jsp',
                    controller: 'uploadRouteCtrl',
                    backdrop: 'static',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(result) {});

            };

            //Controller: importData.js , Branch wise - Pune

            $scope.uploadRouteBranch = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/uploadRouteSears.jsp',
                    controller: 'uploadRouteCtrl',
                    backdrop: 'static',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(result) {});

            };

            //Controller: importData.js
            $scope.exportRoute = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/exportRoute.jsp',
                    controller: 'exportRouteCtrl',
                    backdrop: 'static',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(result) {});

            };
            $scope.printAll = function() {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/printAll.jsp',
                    controller: 'printAllSearchCtrl',
                    backdrop: 'static',
                    resolve: {
                        dataZone: function() {
                            return $scope.zoneData;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    if (result.length != 0) {
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/printAllData.jsp',
                            controller: 'printAllDataCtrl',
                            backdrop: 'static',
                            size: "lg",
                            resolve: {
                                printAllData: function() {
                                    return result;
                                }
                            }
                        });

                        modalInstance.result.then(function(result) {

                        });

                    } else {
                        ngDialog.open({
                            template: 'Routes not Exist for this selection.',
                            plain: true
                        });
                    };

                });

            };

            //Controller: importData.js
            $scope.downloadRoute = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/downloadRoute.jsp',
                    controller: 'downloadRouteCtrl',
                    backdrop: 'static',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(result) {});

            };

            $scope.updateDropSeq = function(employee, route, index, parentIndex) {
                if (employee.isUpdateClicked) {
                    $confirm({
                            text: "Are you sure you want to change the drop sequence from " +
                                employee.pickUpTime +
                                " to " +
                                employee.dropSequence + " ?",
                            title: 'Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })
                        .then(
                            function() {
                                employee.pickUpTime = employee.dropSequence;
                                employee.isUpdateClicked = false;
                                var dataObj = {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    },
                                    assignRouteId: route.routeId,
                                    time: employee.dropSequence,
                                    requestId: employee.requestId,
                                    userId: profileId,
                                    combinedFacility: combinedFacility
                                };
                                $http
                                    .post(
                                        'services/zones/updateDropSequnce/',
                                        dataObj)
                                    .success(
                                        function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                ngDialog.open({
                                                    template: 'Drop Sequence Updated Successfully.',
                                                    plain: true
                                                });
                                                $scope.getRoutes(route);
                                            }
                                        })
                                    .error(
                                        function(data, status,
                                            headers, config) {
                                            // log error
                                        });
                            });
                } else {
                    employee.isUpdateClicked = true;
                }

            };

            $scope.openMapIndividualRoute = function(route) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/routeMapView.jsp',
                    controller: 'multiLocationMapViewCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        waypoints: function() {
                            return route.empDetails[0].multipleWaypoints;
                        },
                        baseLatLong: function() {
                            return route.baseLatLong;
                        }
                    }
                });
            }


            $scope.individualRouteDetails = function(post) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/employeeWayPointsDetails.jsp',
                    controller: 'employeeWayPointsServiceCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        employeeWayPointsDetails: function() {
                            return post.waypointsList;
                        },
                        post: function() {
                            return post;
                        }
                    }
                });
            }

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
                return currentDate + '-' + currentMonth + '-' +
                    convert_date.getFullYear();
            };


            $scope.updatePickupTime = function(employee, route, index,
                parentIndex) {

                if (employee.isUpdateClicked) {

                    $confirm({
                            text: "Are you sure you want to change the pickup time from " +
                                employee.pickUpTime +
                                " to " +
                                employee.createNewAdHocTime +
                                ":00 ?",
                            title: 'Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })
                        .then(
                            function() {
                                var dataObj = {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    },
                                    assignRouteId: route.routeId,
                                    time: employee.createNewAdHocTime,
                                    requestId: employee.requestId,
                                    userId: profileId,
                                    combinedFacility: combinedFacility

                                };

                                $http.post('services/zones/updatePickUpTime/', dataObj)
                                    .success(function(data, status,
                                        headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.status == 'B') {
                                                employee.isUpdateClicked = false;
                                                ngDialog.open({
                                                    template: 'Fail to update pickup time because the employee has already been boarded.',
                                                    plain: true
                                                });
                                            } else if (data.status == 'D') {
                                                employee.isUpdateClicked = false;
                                                ngDialog.open({
                                                    template: 'Fail to update pickup time because the employee has already been dropped',
                                                    plain: true
                                                });
                                            } else if (data.status == 'NO') {
                                                employee.isUpdateClicked = false;
                                                ngDialog.open({
                                                    template: 'Fail to update pickup time because the employee is No-show',
                                                    plain: true
                                                });
                                            } else {
                                                employee.pickUpTime = employee.createNewAdHocTime +
                                                    ":00";
                                                employee.isUpdateClicked = false;
                                                ngDialog.open({
                                                    template: 'PickUpTime Updated Successfully',
                                                    plain: true
                                                });
                                            }
                                        }
                                    })
                                    .error(
                                        function(data, status,
                                            headers, config) {
                                            // log error
                                        });

                            },
                            function() {
                                employee.isUpdateClicked = false;
                            });
                } else {

                    if (angular.isDate(employee.createNewAdHocTime)) {
                        employee.createNewAdHocTime = $scope.convertToTime(employee.createNewAdHocTime);
                    } else {
                        $scope.timeConverstionPickup = [];
                        $scope.timeConverstionPickup = employee.createNewAdHocTime
                            .split(':');
                        var todaydatePickup = new Date();
                        todaydatePickup.setHours(
                            $scope.timeConverstionPickup[0],
                            $scope.timeConverstionPickup[1]);
                        employee.createNewAdHocTime = todaydatePickup;

                        if (angular.isDate(employee.createNewAdHocTime)) {
                            employee.createNewAdHocTime = $scope.convertToTime(employee.createNewAdHocTime);
                        }


                    }
                    employee.isUpdateClicked = true;
                }
            };

            $scope.changeEmployeeStatus = function(employee, routes, zone, employeeStatus) {
                $confirm({
                    text: "Are you sure you want to change employee status from " +
                        employee.boardedStatus +
                        " to " +
                        employee.employeeStatus.text + " ?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {
                    var dataObj = {
                        eFmFmEmployeeTravelRequest: {
                            requestId: employee.requestId
                        },
                        efmFmAssignRoute: {
                            assignRouteId: routes.assignRouteId
                        },
                        boardedFlg: employee.employeeStatus.value,
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                employee.boardedStatus = employee.employeeStatus.text;
                                employee.boardedFlg = employee.employeeStatus.value;
                                ngDialog.open({
                                    template: 'Request status updated successfully.',
                                    plain: true
                                });
                            }
                        }).
                    error(function(data, status, headers, config) {});

                }, function() {

                });
            };

            $scope.addEscort = function(route) {
                route.addFlag = true;

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    assignRouteId: route.routeId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/zones/checkedinentity/', data).success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.escortsData = data.escortDetails;
                        }
                    }).error(function(data, status, headers, config) {});
            };

            $scope.saveAddedEscort = function(selectedEscort, route) {
                $confirm({
                        text: "Are you sure you want to add the Escort?",
                        title: 'Delete Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var escortCheckInId = 0;
                            if (selectedEscort.escortCheckInId == 'N') {
                                escortCheckInId = 0;
                            } else {
                                escortCheckInId = selectedEscort.escortCheckInId;
                            }
                            var dataObj = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                userId: profileId,
                                assignRouteId: route.routeId,
                                searchType: 'addEscort',
                                escortCheckInId: escortCheckInId,
                                combinedFacility: combinedFacility
                            };
                            $http
                                .post(
                                    'services/zones/addOrRemoveEscorts/',
                                    dataObj)
                                .success(
                                    function(data, status,
                                        headers, config) {
                                        if (data.status != "invalidRequest") {
                                            if (data.length == 0) {
                                                ngDialog.open({
                                                    template: 'No Escort CheckIn.Please checkIn the escort first.',
                                                    plain: true
                                                });
                                            }
                                            route.escortName = selectedEscort.escortName;
                                            route.escortRequired = 'Y';
                                            route.addFlag = false;
                                        }
                                    }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        },
                        function() {});

            };
            $scope.vendorSummaryModal = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/serviceMapping/vendorSummaryModal.jsp',
                    controller: 'vendorSummaryCtrl',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        vendorSummaryData: function() {
                            return $scope.vendorSummaryData;
                        }
                    }
                });

                modalInstance.result.then(function(result) {});

            }
            $scope.priorityTracking = function() {
                if ($scope.vendorSummaryData.feedBacks.length > 0) {
                    var data = {
                        priority: true,
                        data: $scope.vendorSummaryData.feedBacks
                    }
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/priorityTrackingModal.jsp',
                        controller: 'priorityTrackingCtrl',
                        size: 'lg',
                        resolve: {
                            feedbackData: function() {
                                return data;
                            }
                        }
                    });

                    modalInstance.result.then(function(result) {});
                } else {
                    $scope.gotFeedbackResult = false;
                    ngDialog.open({
                        template: 'No Data Found. Please Change the Dates.',
                        plain: true
                    });
                }
            }
            $scope.cancelAddingEscort = function(route) {
                route.addFlag = false;
            };

            $scope.removeEscort = function(route) {
                $confirm({
                    text: "Are you sure you want to remove the Escort?",
                    title: 'Delete Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(
                    function() {
                        route.addFlag = false;
                        var escortCheckInId = 0;
                        if (route.escortId == 'N') {
                            escortCheckInId = 0;
                        } else {
                            escortCheckInId = route.escortId;
                        }
                        var dataObj = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            assignRouteId: route.routeId,
                            searchType: 'removeEscort',
                            escortCheckInId: escortCheckInId,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/zones/addOrRemoveEscorts/',
                            dataObj).success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    route.escortName = "Not Required";
                                    route.escortRequired = 'N';
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                    },
                    function() {});

            };
        }

    };

    var creatNewRouteCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, uiZone, ngDialog) {
        $scope.shiftsTime = [];
        $scope.newRoute = {};
        var tripTimeSelected;
        $scope.typeOfShiftTimeSelected;
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
        $scope.isNormalRoute = true;
        $scope.routeSelected = false;
        $scope.routeTypeSelected;
        $scope.allNodelPointData = [];
        $scope.facilityData = [];
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = array;

        $scope.tripTypes = [{
            'value': 'PICKUP',
            'text': 'PICKUP'
        }, {
            'value': 'DROP',
            'text': 'DROP'
        }];

        $scope.tripTimeDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'tripTimeDate': true
                };
            })
        };

        //Initialize TimePicker to 00:00
        var timePickerInitialize = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            $scope.newRoute.createNewAdHocTime = d;
        };

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
            return currentDate + '-' + currentMonth + '-' +
                convert_date.getFullYear();
        };

        //Format of the Calenders Used in all the Children Views
        $scope.format = 'dd-MM-yyyy';
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false,
        };

        var data = {
            eFmFmClientBranchPO: {
                branchId: branchId,
                userId: profileId
            },
            combinedFacility: combinedFacility
        };
        $http.post('services/zones/createbucket/', data).success(
            function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.shiftsTime = [{
                        'shiftTime': 'Select Shift Time'
                    }];
                    $scope.newRoute.shiftTime = $scope.shiftsTime[0];
                    $('.btn-link').addClass('noPointer');
                    timePickerInitialize();
                }
            }).error(function(data, status, headers, config) {
            // log error
        });

        $scope.selectShiftTimeRadio = function(shiftTime) {
            $scope.typeOfShiftTimeSelected = shiftTime;
            $('.btn-link').addClass('noPointer');
        };

        $scope.selectShiftTimeRadio2 = function(shiftTime) {
            $scope.typeOfShiftTimeSelected = shiftTime;
            $('.btn-link').removeClass('noPointer');
        };

        /*Normal Route Type*/

        $scope.setRouteTypeNormal = function(routeType) {
            $scope.allZonesData = [];
            $scope.allNodelPointData = [];
            $scope.routeTypeSelected = routeType;

            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/zones/nonNodelroutes/', data).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allZonesData = data.routeDetails;
                        $scope.isNormalRoute = true;
                        $scope.newRoute.zone = "";
                        $scope.newRoute.nodelPoint = "";
                    }
                }).error(function(data, status, headers, config) {
                // log error
            });

        };

        /*Nodal Route Type*/

        $scope.setRouteTypeNodal = function(routeType) {
            $scope.allZonesData = [];
            $scope.allNodelPointData = [];
            $scope.routeTypeSelected = routeType;

            var dataObj = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/zones/nodelroutes/', dataObj).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allZonesData = data.routeDetails;
                        $scope.newRoute.zone = "";

                        $scope.isNormalRoute = true;
                        $scope.routeSelected = false;
                    }
                }).error(function(data, status, headers, config) {
                // log error
            });

        };

        /*Adhoc nodal Route Type*/

        $scope.setRouteTypeAdhoc = function(routeType) {
            $scope.allZonesData = [];
            $scope.allNodelPointData = [];
            $scope.routeTypeSelected = routeType;

            var dataObj = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/zones/nodelroutes/', dataObj).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.allZonesData = data.routeDetails;
                        $scope.newRoute.zone = "";

                        $scope.isNormalRoute = false;
                        $scope.routeSelected = false;
                    }
                }).error(function(data, status, headers, config) {
                // log error
            });

        };


        $scope.setRouteName = function(route) {
            if (angular.isObject(route)) {
                $scope.routeSelected = true;

                if (!$scope.isNormalRoute) {
                    var dataObj = {
                        branchId: branchId,
                        routeId: route.routeId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http
                        .post('services/zones/routeNodelPoints/', dataObj)
                        .success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $scope.allNodelPointData = data.routeDetails;
                                    if ($scope.allNodelPointData.length == 0) {
                                        ngDialog.open({
                                            template: 'No Nodal Point found for this route.',
                                            plain: true
                                        });
                                    }
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error
                            });

                }
            } else {
                $scope.routeSelected = false;
            }
        };

        $scope.setTripType = function(tripType) {
            if (angular.isObject(tripType)) {
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
                            $scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });
                            $scope.newRoute.shiftTime = $scope.shiftsTime[0];
                        }
                    }).error(function(data, status, headers, config) {});
            } else {
                $scope.shiftsTime = '';
            }
            $scope.newRoute.shiftTime = $scope.shiftsTime[0];
        };

        $scope.getSelectedNodelIds = function(nodelPointsSelected) {
            var nodelPoints;
            var nodelPointsArray = nodelPointsSelected;
            for (var index = 0; index < nodelPointsArray.length; index++) {
                if (index == 0) {
                    nodelPoints = nodelPointsArray[0].nodalPointId;
                } else {
                    nodelPoints = nodelPoints + "," +
                        nodelPointsArray[index].nodalPointId;
                }
            }
            return nodelPoints;
        };

        $scope.createNew = function(newRoute, combinedFacilityId) {

            if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                combinedFacilityId = combinedFacility;
            } else {
                combinedFacilityId = String(combinedFacilityId);
            }

            var selectedNodelIds = 0;
            if ($scope.routeTypeSelected == 'nodalAdhoc') {
                selectedNodelIds = $scope
                    .getSelectedNodelIds(newRoute.nodelPoint);
            }
            var dataObj = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/zones/nodelroutes/', dataObj).success(
                function(data, status, headers, config) {}).error(function(data, status, headers, config) {
                // log error
            });
            if ($scope.typeOfShiftTimeSelected == 'preDefineShiftTime') {
                tripTimeSelected = newRoute.shiftTime.shiftTime;
            } else {
                var fullDate = new Date(newRoute.createNewAdHocTime);
                var time = fullDate.getHours() + ':' + fullDate.getMinutes() +
                    ':00';
                tripTimeSelected = time;
            }
            var dataObj = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                time: tripTimeSelected,
                selectedAssignRouteId: newRoute.zone.routeId,
                tripType: newRoute.tripType.value,
                routeGenerationType: $scope.routeTypeSelected,
                nodalPoints: selectedNodelIds,
                toDate: convertDateUTC(newRoute.tripTimeDate),
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http
                .post('services/zones/savecreatebucket', dataObj)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == 'fail') {
                                ngDialog.open({
                                    template: 'No driver available please checked in driver first.',
                                    plain: true
                                });
                                $timeout(function() {
                                    $modalInstance.dismiss('cancel');
                                    ngDialog.close();
                                }, 3000);
                            } else {
                                $scope.resultData = data;
                                $scope.resultData.zoneRouteId = newRoute.zone.routeId;
                                $scope.resultData.zoneName = newRoute.zone.routeName;
                                ngDialog.open({
                                    template: 'Empty route created successfully.',
                                    plain: true
                                });
                                $timeout(function() {
                                    $modalInstance.close(data);
                                    ngDialog.close();
                                }, 3000);
                            }
                        }
                    }).error(function(data, status, headers, config) {
                    // log error
                });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };


    var editBucketCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, zone, route, $rootScope) {
        $scope.delay = 0;
        $scope.manualStartForm;
        $scope.minDuration = 0;
        $scope.templateUrl = 'bower_components/ngSpinner/custom-template.html';
        $scope.message = 'Please Wait...';
        $scope.backdrop = true;
        $scope.promise = null;
        $scope.alertMessage;
        $scope.alertHint;
        var okIsClicked = false;
        var routeID = route.routeId;
        var isEscortNameChange = false;
        var escortCheckInId;
        var escortId;
        var deviceId;
        var driverId;
        var vehicleId;
        var vendorId;
        $scope.IntegerNumber = /^\d+$/;
        $scope.regExName = /^[A-Za-z]+$/;
        $scope.NoSpecialCharacters = /^[a-zA-Z0-9]*$/;
        $scope.routes = route;
        $scope.checkInEntitiesData = [];
        $scope.backToBackData = [];
        var selectedCheckInIdIndex;
        var currentCheckInId = route.checkInId;
        var newCheckInId;
        var currentEntityIndex;
        $scope.escortRequired = route.escortRequired;
        $scope.escortNameFlag = route.escortName;
        var data = {
            eFmFmClientBranchPO: {
                branchId: branchId
            },
            assignRouteId: route.routeId,
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http
            .post('services/zones/checkedinentity/', data)
            .success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.checkInEntitiesData = data.checkInList;
                        $scope.escortsData = data.escortDetails;
                        currentEntityIndex = _.findIndex(
                            $scope.checkInEntitiesData, {
                                checkInId: currentCheckInId
                            });
                        if ($scope.escortRequired &&
                            route.escortName != 'Escort Required But Not Available') {
                            currentEscortIndex = _.findIndex(
                                $scope.escortsData, {
                                    escortName: route.escortName
                                });
                            $scope.edit = {
                                driverName: $scope.checkInEntitiesData[currentEntityIndex],
                                driverNumber: $scope.checkInEntitiesData[currentEntityIndex].mobileNumber,
                                vehicleNumber: $scope.checkInEntitiesData[currentEntityIndex],
                                deviceId: $scope.checkInEntitiesData[currentEntityIndex],
                                escortName: $scope.escortsData[currentEscortIndex]
                            };

                        } else {
                            $scope.edit = {
                                driverName: $scope.checkInEntitiesData[currentEntityIndex],
                                driverNumber: $scope.checkInEntitiesData[currentEntityIndex].mobileNumber,
                                vehicleNumber: $scope.checkInEntitiesData[currentEntityIndex],
                                deviceId: $scope.checkInEntitiesData[currentEntityIndex]
                            };
                        }
                    }
                }).error(function(data, status, headers, config) {
                // log error
            });

        $scope.updateDriverManual = function(driver) {
            if (angular.isObject(driver)) {
                newCheckInId = driver.checkInId;
                selectedCheckInIdIndex = _.findIndex(
                    $scope.checkInEntitiesData, {
                        checkInId: driver.checkInId
                    });
                $scope.edit.driverName = $scope.checkInEntitiesData[selectedCheckInIdIndex];
                $scope.edit.driverNumber = $scope.checkInEntitiesData[selectedCheckInIdIndex].mobileNumber;
                $scope.edit.vehicleNumber = $scope.checkInEntitiesData[selectedCheckInIdIndex];
                $scope.edit.deviceId = $scope.checkInEntitiesData[selectedCheckInIdIndex];
            }
        };

        $scope.updateVehicleNumberManual = function(vehicle) {

            if (angular.isObject(vehicle)) {
                newCheckInId = vehicle.checkInId;
                selectedCheckInIdIndex = _.findIndex(
                    $scope.checkInEntitiesData, {
                        checkInId: vehicle.checkInId
                    });
                $scope.edit.driverName = $scope.checkInEntitiesData[selectedCheckInIdIndex];
                $scope.edit.driverNumber = $scope.checkInEntitiesData[selectedCheckInIdIndex].mobileNumber;
                $scope.edit.vehicleNumber = $scope.checkInEntitiesData[selectedCheckInIdIndex];
                $scope.edit.deviceId = $scope.checkInEntitiesData[selectedCheckInIdIndex];
            }
        };

        $scope.updateDeviceIdManual = function(device) {
            if (angular.isObject(device)) {
                selectedCheckInIdIndex = _.findIndex(
                    $scope.checkInEntitiesData, {
                        checkInId: device.checkInId
                    });
                $scope.edit.driverName = $scope.checkInEntitiesData[selectedCheckInIdIndex];
                $scope.edit.driverNumber = $scope.checkInEntitiesData[selectedCheckInIdIndex].mobileNumber;
                $scope.edit.vehicleNumber = $scope.checkInEntitiesData[selectedCheckInIdIndex];
                $scope.edit.deviceId = $scope.checkInEntitiesData[selectedCheckInIdIndex];
            }
        };

        $scope.updateEscortManual = function(escort) {
            if (angular.isObject(escort)) {
                escortCheckInId = escort.escortCheckInId;
            }
        };

        $scope.save = function(updatedValue) {
            newCheckInId = $scope.edit.vehicleNumber.checkInId;
            if ($scope.escortRequired == 'N') {
                escortCheckInId = 0;
                isEscortNameChange = false;
            } else {
                if ($scope.edit.escortName.escortName == route.escortName) {
                    isEscortNameChange = false;
                } else {
                    isEscortNameChange = true;
                }
            }

            if ($scope.edit.driverName.checkInId !== currentCheckInId ||
                isEscortNameChange) {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    assignRouteId: route.routeId,
                    vehicleId: $scope.edit.vehicleNumber.vehicleId,
                    driverId: $scope.edit.driverName.driverId,
                    deviceId: $scope.edit.deviceId.deviceId,
                    escortCheckInId: escortCheckInId,
                    newCheckInId: newCheckInId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $scope.promise = $http
                    .post('services/zones/saveeditbucket/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == 'lessCapacity') {
                                    ngDialog.open({
                                        template: 'Sorry please change the vehicle, Which has  ' + route.vehicleAvailableCapacity + ' more seat capacity.',
                                        plain: true
                                    });
                                } else {
                                    updatedValue.availableCapacity = data.availableCapacity;
                                    updatedValue.status = data.status;
                                    updatedValue.checkInId = newCheckInId;
                                    ngDialog.open({
                                        template: 'Route Details Change Successfully.',
                                        plain: true
                                    });
                                    $timeout(function() {
                                        $modalInstance.close(updatedValue);
                                        ngDialog.close();
                                    }, 3000);
                                }
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            } else {
                ngDialog.open({
                    template: 'None of the Entity has been Change.',
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
    var printRouteCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, printZone, $rootScope, $modal) {
        $scope.todayDate = new Date();
        $scope.routePrintModal = printZone;
        $scope.date = new Date();
        $scope.print = function() {
            window.print();
        }
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };

    
            $scope.printDiv = function(divName) {

                var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/printSizeConfirmation.jsp',
                        controller: 'printSizeConfirmationCtrl',
                        backdrop: 'static',
                        resolve: {

                        }
                    });

                    modalInstance.result.then(function(result) {
                        console.log("result",result);
                        if(result == 'sizeA3'){
                            $modalInstance.close();
                            var printContents = document.getElementById(divName).innerHTML;
                            var popupWin = window.open('', '_blank', 'width=30000,height=2000,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no,top=50');
                            popupWin.window.focus();
                            popupWin.document.open();
                            popupWin.document.write('<!DOCTYPE html><html><head><title>eFmFm</title>' +
                                '<link rel="stylesheet" type="text/css" href="styles/printLandscape.css" />' +
                                '</head><body onload="window.print(); window.close();"><div>' +
                                printContents + '</div></html>');
                            popupWin.document.close();
                        }else{
                            $modalInstance.close();
                            var printContents = document.getElementById(divName).innerHTML;
                            var popupWin = window.open('', '_blank', 'width=30000,height=2000,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no,top=50');
                            popupWin.window.focus();
                            popupWin.document.open();
                            popupWin.document.write('<!DOCTYPE html><html><head><title>eFmFm</title>' +
                                '<link rel="stylesheet" type="text/css" href="styles/print.css" />' +
                                '</head><body onload="window.print(); window.close();"><div>' +
                                printContents + '</div></html>');
                            popupWin.document.close();
                        }
                        

                    });

            
            }
      
        

    };

    var printAllSearchCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, dataZone, $rootScope) {
        $scope.dataZoneall = dataZone;

        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.download = {};
        $scope.shiftsTime = [];
        $scope.ismeridian = false;
        $scope.reportList = [];
        $scope.download.date = new Date();
        $scope.vendorListShow = false;
        $scope.vehiceListShow = false;
        $scope.facilityData = [];
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = array;


        //       $scope.setMinDate = new Date();
        $scope.tripTypes = [{
            'value': 'PICKUP',
            'text': 'PICKUP'
        }, {
            'value': 'DROP',
            'text': 'DROP'
        }];

        $scope.printTypeData = [{
            'value': 'none',
            'text': 'None'
        }, {
            'value': 'vendorWise',
            'text': 'Vendor Wise'
        }, {
            'value': 'vehicleWise',
            'text': 'Vehicle Wise'
        }];

        $scope.download.printType = $scope.printTypeData[0];

        $scope.setPrintType = function(data) {
            if (data.value == 'vendorWise') {
                $scope.vendorListShow = true;
                $scope.vehiceListShow = false;
            } else if (data.value == 'vehicleWise') {
                $scope.vendorListShow = false;
                $scope.vehiceListShow = true;
            } else {
                $scope.vendorListShow = false;
                $scope.vehiceListShow = false;
            }
        }

        $scope.getAllVendors = function() {
            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/contract/allActiveVendor/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vendorDetailsData = data;
                    angular.forEach(data, function(item) {
                        $rootScope.allInspectionVendors.push({
                            'name': item.name,
                            'Id': item.vendorId
                        });
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getAllVendors();

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
            return currentDate + '-' + currentMonth + '-' +
                convert_date.getFullYear();
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
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false,
        };

        $scope.openDownloadDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openeddownloadDate': true
                };
            })
        };

        //Initialize TimePicker to 00:00
        var initialize = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            $scope.download.createNewAdHocTime = d;
            $scope.download.tripType = {
                'value': 'PICKUP',
                'text': 'PICKUP'
            };

            $scope.setTripType($scope.download.tripType);
            $scope.download.shiftTime = $scope.shiftsTime[0];
            $scope.shiftTime = 'preDefineShiftTime';
            $scope.download.date = new Date();
        }


        $scope.selectShiftTimeRadio = function(shiftTime) {
            $scope.typeOfShiftTimeSelected = shiftTime;
            $('.btn-link').addClass('noPointer');
        };

        $scope.selectShiftTimeRadio2 = function(shiftTime) {
            $scope.typeOfShiftTimeSelected = shiftTime;
            $('.btn-link').removeClass('noPointer');
        };

        $scope.setTripType = function(tripType) {
            if (angular.isObject(tripType)) {
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
                            $scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });
                            $scope.download.shiftTime = $scope.shiftsTime[0];
                        }

                    }).error(function(data, status, headers, config) {});
            } else {
                $scope.shiftsTime = '';
            }
            $scope.download.shiftTime = $scope.shiftsTime[0];
        };

        initialize();

        $scope.printRoute = function(fileInfo, combinedFacilityId) {

            if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                combinedFacilityId = combinedFacility;
            } else {
                combinedFacilityId = String(combinedFacilityId);
            }


            if ($scope.shiftTime == 'preDefineShiftTime') {
                $scope.timeSelected = fileInfo.shiftTime.shiftTime
            } else {
                var fullDate = new Date(fileInfo.createNewAdHocTime);
                var time = fullDate.getHours() + ':' + fullDate.getMinutes() +
                    ':00';
                $scope.timeSelected = time;
            }

            var dataObj = {
                branchId: branchId,
                executionDate: convertDateUTC(fileInfo.date),
                tripType: fileInfo.tripType.value,
                time: $scope.timeSelected,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };

            $http.post('services/xlEmployeeExport/printall/', dataObj)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $modalInstance.close(data);
                        }

                    }).error(
                    function(data, status, headers, config) {
                        // log error
                    });
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };


    var printAllDataCtrl = function($scope, $modal, $modalInstance, $state, $http,
        $timeout, ngDialog, printAllData, $rootScope) {
        $scope.todayDate = new Date();
        $scope.routePrintModals = printAllData;
        $scope.printDiv = function(divName) {
            var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/printSizeConfirmation.jsp',
                        controller: 'printSizeConfirmationCtrl',
                        backdrop: 'static',
                        resolve: {

                        }
                    });

                    modalInstance.result.then(function(result) {
                        console.log("result",result);
                        if(result == 'sizeA3'){
                            $modalInstance.close();
                            var printContents = document.getElementById(divName).innerHTML;
                            var popupWin = window.open('', '_blank', 'width=30000,height=2000,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no,top=50');
                            popupWin.window.focus();
                            popupWin.document.open();
                            popupWin.document.write('<!DOCTYPE html><html><head><title>eFmFm</title>' +
                                '<link rel="stylesheet" type="text/css" href="styles/printLandscape.css" />' +
                                '</head><body onload="window.print(); window.close();"><div>' +
                                printContents + '</div></html>');
                            popupWin.document.close();
                        }else{
                            $modalInstance.close();
                            var printContents = document.getElementById(divName).innerHTML;
                            var popupWin = window.open('', '_blank', 'width=30000,height=2000,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no,top=50');
                            popupWin.window.focus();
                            popupWin.document.open();
                            popupWin.document.write('<!DOCTYPE html><html><head><title>eFmFm</title>' +
                                '<link rel="stylesheet" type="text/css" href="styles/print.css" />' +
                                '</head><body onload="window.print(); window.close();"><div>' +
                                printContents + '</div></html>');
                            popupWin.document.close();
                        }
                        

                    });
            /*$modalInstance.close();
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=3000,height=2000,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no,top=50');
            popupWin.window.focus();
            popupWin.document.open();
            popupWin.document.write('<!DOCTYPE html><html><head><title>eFmFm</title>' +
                '<link rel="stylesheet" type="text/css" href="styles/print.css" />' +
                '</head><body onload="window.print(); window.close();"><div>' +
                printContents + '</div></html>');
            popupWin.document.close();*/
        }
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


    };

    var singleRouteMapViewCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $rootScope, routeId, waypoints, baseLatLong, route) {

        $('.serviceMappingMenu').addClass('active');
        $scope.isloaded = false;
        $scope.singleServiceData;
        $scope.route = route;
        $scope.empDetails = route.empDetails;
        $scope.empDetailsLength = route.empDetails.length;
        $scope.routeId = routeId;
        $scope.waypoints = waypoints;
        $scope.baseLatLong = baseLatLong;
        $scope.singleServiceData = [{
            'waypoints': $scope.waypoints,
            'baseLatLong': $scope.baseLatLong
        }];
        $scope.isloaded = true;

        $scope.getSingleServiceMap = function() {
            return $scope.singleServiceData;
        };

        $scope.backToServiceMapping = function() {
            $state.go('home.servicemapping');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


    };


    var completeRouteCtrl = function($scope, $modal, $modalInstance,
        $state, $http, $timeout, ngDialog) {
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.complete = {};
        $scope.shiftsTime = [];
        $scope.ismeridian = false;
        $scope.reportList = [];
        $scope.complete.date = new Date();
        $scope.routingCompleteRoutingShow = true;
        $scope.tripTypes = [{
            'value': 'PICKUP',
            'text': 'PICKUP'
        }, {
            'value': 'DROP',
            'text': 'DROP'
        }];

        // Convert the dates in DD-MM-YYYY format
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

        // Initialize TimePicker to 00:00
        var timePickerInitialize = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            $scope.assignCab.createNewAdHocTime = d;
        };

        $scope.format = 'dd-MM-yyyy';
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false,
        };

        $scope.opencompleteDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'openedcompleteDate': true
                };
            })
        };

        // Initialize TimePicker to 00:00
        var initialize = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            $scope.complete.createNewAdHocTime = d;
            $scope.complete.tripType = {
                'value': 'PICKUP',
                'text': 'PICKUP'
            };

            $scope.setTripType($scope.complete.tripType);
            $scope.complete.shiftTime = $scope.shiftsTime[0];
            $scope.shiftTime = 'preDefineShiftTime';
            $scope.complete.date = new Date();
        }

        $scope.selectShiftTimeRadio = function(shiftTime) {
            $scope.typeOfShiftTimeSelected = shiftTime;
            $('.btn-link').addClass('noPointer');
        };

        $scope.selectShiftTimeRadio2 = function(shiftTime) {
            $scope.typeOfShiftTimeSelected = shiftTime;
            $('.btn-link').removeClass('noPointer');
        };

        $scope.setTripType = function(tripType) {
            if (angular.isObject(tripType)) {
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
                            $scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });
                            $scope.complete.shiftTime = $scope.shiftsTime[0];
                        }

                    }).error(function(data, status, headers, config) {});
            } else {
                $scope.shiftsTime = '';
            }
            $scope.complete.shiftTime = $scope.shiftsTime[0];
        };

        initialize();

        $scope.completeRoute = function(completeShift) {
            $scope.routingCompleteRoutingShow = false;
            if ($scope.shiftTime == 'preDefineShiftTime') {
                $scope.timeSelected = completeShift.shiftTime.shiftTime
            } else {
                var fullDate = new Date(completeShift.createNewAdHocTime);
                var time = fullDate.getHours() + ':' + fullDate.getMinutes() +
                    ':00';
                $scope.timeSelected = time;
            }

            var dataObj = {
                branchId: branchId,
                executionDate: convertDateUTC(completeShift.date),
                tripType: completeShift.tripType.value,
                time: $scope.timeSelected,
                userId: profileId,
                combinedFacility: combinedFacility
            };

            $http.post('services/backTwoBack/routingComplete/', dataObj).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.routingCompleteRoutingShow = true;
                        ngDialog.open({
                            template: 'Routing Completed Successfully',
                            plain: true
                        });
                        $modalInstance.close(data);
                    }
                }).error(function(data, status, headers, config) {});

        }

        // CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };

    var employeeWayPointsServiceCtrl = function($scope, post, employeeWayPointsDetails, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, $modal) {
        $scope.employeeWayPointsDetails = employeeWayPointsDetails;

        $scope.openMap = function(index) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/singleRouteMap.jsp',
                controller: 'singleRouteMapViewCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    routeId: function() {
                        return post.requestId;
                    },
                    waypoints: function() {
                        return post.waypointsList[index].LatiLng;
                    },
                    baseLatLong: function() {
                        return post.waypointsList[index].LatiLng;
                    },
                    route: function() {
                        return post;
                    }
                }
            });
        }

        $scope.removeLocationDetails = function(post, index) {
            var data = {
                userId: profileId,
                eFmFmLocationMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                actionFlg: "C",
                multipleReqId: post.employeeAreaId,
                combinedFacility: combinedFacility
            };

            $http.post('services/trip/updateTravelledLocation/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    ngDialog.open({
                        template: post.LocationName + ' ' + 'Removed Successfully',
                        plain: true
                    });
                    $scope.employeeWayPointsDetails.splice(index, 1);
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };

    var vendorListCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, $modal) {

        $scope.getAllVendors = function() {
            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/contract/allActiveVendor/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vendorDetailsData = data;
                    angular.forEach(data, function(item) {
                        $rootScope.allInspectionVendors.push({
                            'name': item.name,
                            'Id': item.vendorId
                        });
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getAllVendors();

        $scope.submitVendor = function(value) {
            $modalInstance.close(value);
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };
    var vendorSummaryCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, $rootScope, vendorSummaryData) {
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };

    };
    var addNewEmployeeCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, $rootScope, ngDialog, routeDetails) {
        $scope.saveNewEmployee = function(employeeId) {
            var data = {
                branchId: branchId,
                assignRouteId: routeDetails.assignRouteId,
                employeeId: employeeId,
                combinedFacility: combinedFacility
            }
            $http.post('services/zones/addEmpInRoute/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "empIdNotExist") {
                        ngDialog.open({
                            template: 'Please check employee Id not exists in system',
                            plain: true
                        });
                    } else if (data.status == "noCapacity") {
                        ngDialog.open({
                            template: 'Please check seat not available in this route',
                            plain: true
                        });
                    } else if (data.status == "requestExist") {
                        ngDialog.open({
                            template: 'Please check you already have a request for this date and shift time',
                            plain: true
                        });
                    } else {
                        ngDialog.open({
                            template: 'Employee Added successfully in route please check',
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
        $scope.close = function() {
            $modalInstance.close();
        };

    };

    var pluckCardEmpNameCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, empNameDetails, $rootScope, $filter, $modal) {
        $scope.todayDate = new Date();
        $scope.empNameDetailsCheck = empNameDetails;
        $scope.date = new Date();
        var customizedTemplateFlg = 'Yes';
        var logoTemplateFlg = 'No';
        $scope.pluckCardFormDisabled = true;

        $scope.pluckCardDesignTypes = [{
            'value': 'Customized Own Template',
            'text': 'customizedTemplate'
        }, {
            'value': 'Company Logo Template',
            'text': 'logoTemplate'
        }];

        $scope.selectDesignType = function(value) {
            if (value.text == 'customizedTemplate') {
                if (customizedTemplateFlg != 'Yes') {
                    ngDialog.open({
                        template: 'There is no Customized templates are not uploaded yet. Please upload template in configuration setting first, and try again.',
                        plain: true
                    });
                    $scope.pluckCardFormDisabled = true;
                } else {
                    $scope.pluckCardFormDisabled = false;
                }
            } else {
                if (logoTemplateFlg != 'Yes') {
                    ngDialog.open({
                        template: 'Logo are not uploaded yet. Please upload logo in configuration setting first, and try again.',
                        plain: true
                    });
                    $scope.pluckCardFormDisabled = true;
                } else {
                    $scope.pluckCardFormDisabled = false;
                }
            }
        }

        $scope.employeeNamesList = [];

        angular.forEach($scope.empNameDetailsCheck.empDetails, function(item, key) {
            $scope.employeeNamesList.push(item.name);
        });

        $scope.user = {
            employeeNamesList: []
        };

        $scope.employeeNameCheck = function(selected, index) {
            angular.forEach($scope.empNameDetailsCheck.empDetails, function(item, key) {
                if (key == index) {
                    item.selected = true;
                } else {
                    item.selected = false;
                };
            });
        };
        $scope.previewPluckCard = function(employeeNames) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/previewPluckCardModal.jsp',
                controller: 'previewPluckCardCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    employeeNameData: function() {
                        return employeeNames;
                    }
                }
            });
            modalInstance.result
                .then(function(result) {});

        };
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };
    var previewPluckCardCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, employeeNameData, $rootScope) {
        var customizedTemplateFlg = 'Yes';
        var logoTemplateFlg = 'No';
        $scope.templateSrc = 'http://localhost:8080/eFmFmFleetWeb/images/pluckCard.jpg';
        $scope.logoSrc = 'http://localhost:8080/eFmFmFleetWeb/images/efmfm-logo.png';

        $scope.employeeNameLength = employeeNameData.length;
        $scope.todayDate = new Date();
        $scope.routePrintModals = employeeNameData;
        $scope.date = new Date();
        $scope.print = function() {
            printElement(document.getElementById('printable'));
            window.print();
        }
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


        $scope.printDiv = function(divName) {
            $modalInstance.close();
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=3000,height=2000,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no,top=50');
            popupWin.window.focus();
            popupWin.document.open();
            popupWin.document.write('<!DOCTYPE html><html><head><title>eFmFm</title>' +
                '<link rel="stylesheet" type="text/css" href="styles/print.css" />' +
                '</head><body onload="window.print(); window.close();"><div>' +
                printContents + '</div></html>');
            popupWin.document.close();
        }

    };


     var printSizeConfirmationCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, $rootScope) {
        
        $scope.submitPrintSize = function(value){
            console.log(value);
            if(value == undefined){
                ngDialog.open({
                        template: 'Kindly choose print paper size',
                        plain: true
                    });
                return false;
            }
            $modalInstance.close(value);
        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


    };

    angular.module('efmfmApp').controller('printAllDataCtrl',
        printAllDataCtrl);
    angular.module('efmfmApp').controller('printAllSearchCtrl',
        printAllSearchCtrl);
    angular.module('efmfmApp').controller('printRouteCtrl',
        printRouteCtrl);
    angular.module('efmfmApp').controller('creatNewRouteCtrl',
        creatNewRouteCtrl);
    angular.module('efmfmApp').controller('editBucketCtrl', editBucketCtrl);
    /*angular.module('efmfmApp').controller('manualtripstartCtrl',
    		manualtripstartCtrl);*/

    angular.module('efmfmApp').controller('serviceMappingCtrl',
        serviceMappingCtrl);
    angular.module('efmfmApp').controller('singleRouteMapViewCtrl',
        singleRouteMapViewCtrl);
    angular.module('efmfmApp').controller('completeRouteCtrl',
        completeRouteCtrl);
    angular.module('efmfmApp').controller('employeeWayPointsServiceCtrl',
        employeeWayPointsServiceCtrl);
    angular.module('efmfmApp').controller('vendorListCtrl',
        vendorListCtrl);
    angular.module('efmfmApp').controller('vendorSummaryCtrl',
        vendorSummaryCtrl);
    angular.module('efmfmApp').controller('pluckCardEmpNameCtrl',
        pluckCardEmpNameCtrl);
    angular.module('efmfmApp').controller('previewPluckCardCtrl',
        previewPluckCardCtrl);
    angular.module('efmfmApp').controller('addNewEmployeeCtrl',
        addNewEmployeeCtrl);
    angular.module('efmfmApp').controller('printSizeConfirmationCtrl',
        printSizeConfirmationCtrl);
}());