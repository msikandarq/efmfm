(function() {
    var customRouteViewCtrl = function($scope, $http, $state, $modal, $log,
        $timeout, $confirm, ngDialog, $rootScope) {

        if (!$scope.isCaballocationActive ||
            $scope.isCaballocationActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $('.serviceMappingMenu').addClass('active');

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
            }];
            $scope.search.routeStatus = {
                'value': 'All',
                'text': 'All Routes'
            };

            $scope.learnRouteDatas = [{
                    value: 'Learn For The Day',
                    text: 'Learn For The Day'
                },
                {
                    value: 'Learn For Permanant',
                    text: 'Learn For Permanant'
                }
            ];

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

            $scope.editBucket = function(route, zone, size) {

                if (route.bucketStatus == 'Y') {
                    route.bucketStatus = 'N';
                } else
                    route.bucketStatus == 'Y';

                route.editClicked = true;
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/editBucketModal.jsp',
                    controller: 'editBucketCustomCtrl',
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
                        var dataObj = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            eFmFmRouteAreaMapping: {
                                eFmFmZoneMaster: {

                                    zoneId: route.routeId
                                }
                            },
                            userId: profileId,
                            advanceFlag: "false",
                            combinedFacility: combinedFacility
                        };


                        $http
                            .post('services/zones/allroutes/', dataObj)
                            .success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        $scope.routesData = data.routeDetails;

                                        var index = _.findIndex($rootScope.CustomData, {
                                            routeId: route.routeId
                                        });


                                        if (route.escortRequired != 'N' &&
                                            route.escortName !== 'Escort Required But Not Available') {
                                            $scope.routesData[index].escortName = result.escortName.escortName;

                                        }


                                        $scope.customRouteDetails[index].deviceNumber = result.deviceId.deviceId;
                                        $scope.customRouteDetails[index].mobileNumber = result.driverNumber;
                                        $scope.customRouteDetails[index].vehicleNumber = result.driverName.vehicleNumber;
                                        $scope.customRouteDetails[index].deviceNumber = result.driverName.deviceNumber;
                                        $scope.customRouteDetails[index].vehicleAvailableCapacity = result.availableCapacity;
                                        $scope.customRouteDetails[index].checkInId = result.checkInId;
                                        $scope.customRouteDetails[index].driverName = result.driverName.driverName;
                                        $scope.customRouteDetails[index].deviceId = result.driverName.deviceId;
                                    }
                                });
                    });
            };


            $scope.closeBucket = function(items, zone) {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    eFmFmRouteAreaMapping: {
                        eFmFmZoneMaster: {
                            zoneId: items.routeId
                        }
                    },
                    advanceFlag: "false",
                    combinedFacility: combinedFacility
                };


                $http
                    .post('services/zones/allroutes/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.routesDatas = data.routeDetails;
                                $scope.routesData = data.routeDetails;

                                var dummyVehicleNumberCheck = items.vehicleNumber;
                                if (dummyVehicleNumberCheck.search("DUMMY") != -1) {
                                    ngDialog.open({
                                        template: 'Sorry this dummy vehicle please first update the dummy entities.',
                                        plain: true
                                    });

                                    return false;
                                }

                                var x = _.findIndex($scope.routesData, {
                                    routeId: items.routeId
                                });

                                $scope.routeDetailsData = [];

                                angular.forEach($scope.routesData, function(item) {
                                    angular.forEach(item.empDetails, function(item) {
                                        $scope.routeDetailsData.push(item);
                                    });

                                });

                                $scope.routesData = $scope.routeDetailsData;
                                var reArrangeEmployeeArray = $scope.routesData;
                                items.closeBucketClicked = true;
                                items.editClicked = false;
                                var dataObj = {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    },
                                    userId: profileId,
                                    assignRouteId: items.routeId,
                                    combinedFacility: combinedFacility
                                };
                                $http
                                    .post('services/zones/bucketclose/', dataObj)
                                    .success(
                                        function(data, status, headers, config) {
                                            if (data.status != "invalidRequest") {
                                                if (data.status == 'notClose' && data.type == 'DROP') {
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
                                                    items.bucketStatus = 'Y';
                                                    items.escortName = data.escortName;
                                                    $scope.customRouteDetails[0].vehicleAvailableCapacity = data.availableCapacity;

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
                            }
                        });
            };


            $scope.searchRouteDetails = function(data, combinedFacilityId) {

                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (data.tripType == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Trip type',
                        plain: true
                    });
                    return false;
                }

                if (data.shiftTime == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Shift time',
                        plain: true
                    });
                    return false;
                }

                $scope.customRouteViewShow = true;
                $scope.tableDataShow = false;


                if (data.empDetailsFlg == 'A') {
                    var dataObj = {
                        branchId: branchId,
                        executionDate: data.searchDate,
                        tripType: data.tripType,
                        time: data.shiftTime,
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };
                } else {
                    var dataObj = {
                        branchId: branchId,
                        executionDate: convertDateUTC(data.searchDate),
                        tripType: data.tripType.value,
                        time: data.shiftTime.shiftTime,
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };
                }

                $http.post('services/xlEmployeeExport/printall/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $rootScope.CustomData = data;

                                $scope.routeNameList = [];
                                var valueStr = "filter";
                                angular.forEach(data, function(item) {

                                    var filterData = {
                                        text: item.routeName,
                                        value: valueStr.concat(item.routeName)
                                    }
                                    $scope.routeNameList.push(filterData);

                                });

                                angular.forEach(data, function(item) {
                                    $scope.routeName = item.routeName;

                                    angular.forEach(item.empDetails, function(item) {

                                        item.employeeStatus = {
                                            text: item.boardedStatus,
                                            value: item.boardedFlg
                                        };

                                        item.routeNameList =
                                            valueStr.concat($scope.routeName)

                                    });

                                });

                                $scope.dataLength = data.length;
                                if ($scope.dataLength <= 0) {
                                    ngDialog.open({
                                        template: 'No Data Found. Kindly change the selection.',
                                        plain: true
                                    });
                                    $scope.customRouteViewShow = false;
                                    return false;
                                }
                                $rootScope.customRouteDetails = data;
                                $scope.customRouteViewShow = false;
                                $scope.tableDataShow = true;
                            }


                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });

                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    tripType: data.tripType.value,
                    time: data.shiftTime.shiftTime + ':00',
                    searchType: 'All',
                    toDate: convertDateUTC(data.searchDate),
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };


                $http.post('services/zones/triptypesearchinroute/',
                        dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.zoneData = data.routeDetails;
                            }

                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            }


            $scope.learnRouteClick = function() {
                $scope.learnRoute = true;
            }

            $scope.learnRouteChange = function(learnRouteType, route) {

                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    learnRouteType: learnRouteType,
                    routeId: route.routeId,
                    tripActualAssignDate: route.tripActualAssignDate,
                    tripType: route.tripType,
                    routeType: route.routeType,
                    shiftTime: route.shiftTime,
                    combinedFacility: combinedFacility
                };

                $http
                    .post('services/dummy/dummy/', dataObj)
                    .success(
                        function(data, status, headers, config) {

                        }).error(
                        function(data, status, headers, config) {
                            // log error
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
                                $scope.searchData = [];
                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });
            };

            $scope.openMap = function(items, zone, size) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/singleRouteMap.jsp',
                    controller: 'singleRouteMapViewCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        routeId: function() {
                            return items.routeId;
                        },
                        waypoints: function() {
                            return items.waypoints;
                        },
                        baseLatLong: function() {
                            return items.baseLatLong;
                        },
                        route: function() {
                            return items;
                        }
                    }
                });

            };

            $scope.initializeTime = function(time) {
                var d = new Date();
                var tempTime = time.split(":");
                d.setHours(tempTime[0]);
                d.setMinutes(tempTime[1]);
                return d;
            }

            $scope.moveToRoute = function(moveTo, moveToZone, items, zone, item, index) {
                var rowId = '#row' + items.zoneRouteId + items.routeId +
                    item.employeeId + item.requestId;

                var CustomData = $rootScope.CustomData;
                if (!moveTo) {} else {
                    $confirm({
                            text: "Are you sure you want to move this employee from " +
                                items.routeName +
                                " to " +
                                moveTo.routeName + " ?",
                            title: 'Delete Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })
                        .then(
                            function() {
                                //Checking if Routes are moving with in the Same Zone
                                if (moveToZone.routeName === items.routeName) {
                                    // Check if the User has selected the Same route as the Current one
                                    if (moveTo.routeId != items.routeId) {

                                        var moveFromIndex = _
                                            .findIndex(
                                                $rootScope.CustomData, {
                                                    routeId: items.routeId
                                                });
                                        var moveToIndex = _
                                            .findIndex(
                                                $rootScope.CustomData, {
                                                    routeId: moveTo.routeId
                                                });

                                        if ($rootScope.CustomData[moveToIndex].bucketStatus === 'Y') {
                                            ngDialog.open({
                                                template: 'This bucket ' + moveTo.routeId + ' is already Closed. Please first click on edit bucket.',
                                                plain: true
                                            });

                                        } else {

                                            if ($rootScope.CustomData[moveToIndex].vehicleAvailableCapacity > 0 &&
                                                items.shiftTime == moveTo.shiftTime &&
                                                items.tripType == moveTo.tripType &&
                                                items.tripActualAssignDate == items.tripActualAssignDate) {

                                                var dataObj = {
                                                    eFmFmClientBranchPO: {
                                                        branchId: branchId
                                                    },
                                                    assignRouteId: items.assignRouteId,
                                                    selectedAssignRouteId: moveTo.assignRouteId,
                                                    requestId: item.requestId,
                                                    userId: profileId,
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

                                                                    $rootScope.CustomData[moveFromIndex].vehicleAvailableCapacity = data.currentRouteAvailableCapacity;

                                                                    if (items.capacity -
                                                                        items.vehicleAvailableCapacity <= 1) {
                                                                        items.isVehicleEmpty = true;
                                                                    }

                                                                    var moveToIndex = _
                                                                        .findIndex(
                                                                            $rootScope.CustomData, {
                                                                                routeId: moveTo.routeId
                                                                            });
                                                                    $rootScope.CustomData[moveToIndex].vehicleAvailableCapacity = $scope.customRouteDetails[moveToIndex].vehicleAvailableCapacity - 1;
                                                                    var totalNoOfEmployeeCount = $scope.customRouteDetails[moveFromIndex].empDetails.length - 1;
                                                                    var time = $scope.customRouteDetails[moveFromIndex].empDetails[index].pickUpTime;
                                                                    var pickUpTimeTo = $scope.customRouteDetails[moveFromIndex].empDetails[index].pickUpTime;

                                                                    var dataObj = {
                                                                        branchId: branchId,
                                                                        executionDate: items.tripActualAssignDate,
                                                                        tripType: items.tripType,
                                                                        time: items.shiftTime,
                                                                        userId: profileId,
                                                                        combinedFacility: combinedFacility
                                                                    };

                                                                    $http.post('services/xlEmployeeExport/printall/', dataObj)
                                                                        .success(
                                                                            function(data, status, headers, config) {
                                                                                if (data.status != "invalidRequest") {
                                                                                    $rootScope.CustomData = data;

                                                                                    $scope.routeNameList = [];
                                                                                    var valueStr = "filter";
                                                                                    angular.forEach(data, function(item) {

                                                                                        var filterData = {
                                                                                            text: item.routeName,
                                                                                            value: valueStr.concat(item.routeName)
                                                                                        }
                                                                                        $scope.routeNameList.push(filterData);

                                                                                    });

                                                                                    angular.forEach(data, function(item) {
                                                                                        $scope.routeName = item.routeName;

                                                                                        angular.forEach(item.empDetails, function(item) {

                                                                                            item.employeeStatus = {
                                                                                                text: item.boardedStatus,
                                                                                                value: item.boardedFlg
                                                                                            };

                                                                                            item.routeNameList =
                                                                                                valueStr.concat($scope.routeName)

                                                                                        });

                                                                                    });

                                                                                    $scope.dataLength = data.length;
                                                                                    if ($scope.dataLength <= 0) {
                                                                                        ngDialog.open({
                                                                                            template: 'No Data Found. Kindly change the selection.',
                                                                                            plain: true
                                                                                        });
                                                                                        $scope.customRouteViewShow = false;
                                                                                        return false;
                                                                                    }
                                                                                    $rootScope.customRouteDetails = data;
                                                                                    $scope.customRouteViewShow = false;
                                                                                    $scope.tableDataShow = true;
                                                                                }
                                                                            }
                                                                        ).error(
                                                                            function(data, status, headers, config) {
                                                                                // log error
                                                                            });

                                                                    if (totalNoOfEmployeeCount == 0) {
                                                                        $scope.customRouteDetails.splice(moveFromIndex, 1);
                                                                    }

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
                                    } else {
                                        ngDialog.open({
                                            template: 'Please change the route you can not move the employee in the same Route.',
                                            plain: true
                                        });
                                    }
                                } else {
                                    var moveFromIndex = _.findIndex(
                                        $rootScope.CustomData, {
                                            routeId: items.routeId
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
                                                items.shiftTime == moveTo.shiftTime &&
                                                items.tripType == moveTo.tripType &&
                                                items.tripActualAssignDate == moveTo.tripActualAssignDate) {

                                                var dataObj = {
                                                    eFmFmClientBranchPO: {
                                                        branchId: branchId
                                                    },
                                                    assignRouteId: items.assignRouteId,
                                                    selectedAssignRouteId: moveTo.assignRouteId,
                                                    requestId: item.requestId,
                                                    userId: profileId,
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

                                                                    $rootScope.CustomData[moveFromIndex].vehicleAvailableCapacity = data.currentRouteAvailableCapacity;

                                                                    if (items.capacity -
                                                                        items.vehicleAvailableCapacity <= 1) {
                                                                        items.isVehicleEmpty = true;
                                                                    }

                                                                    var moveToIndex = _
                                                                        .findIndex(
                                                                            $rootScope.CustomData, {
                                                                                routeId: moveTo.routeId
                                                                            });
                                                                    $rootScope.CustomData[moveToIndex].vehicleAvailableCapacity = $scope.customRouteDetails[moveToIndex].vehicleAvailableCapacity - 1;
                                                                    var totalNoOfEmployeeCount = $scope.customRouteDetails[moveFromIndex].empDetails.length - 1;
                                                                    var time = $scope.customRouteDetails[moveFromIndex].empDetails[index].pickUpTime;
                                                                    var pickUpTimeTo = $scope.customRouteDetails[moveFromIndex].empDetails[index].pickUpTime;

                                                                    var dataObj = {
                                                                        branchId: branchId,
                                                                        executionDate: items.tripActualAssignDate,
                                                                        tripType: items.tripType,
                                                                        time: items.shiftTime,
                                                                        userId: profileId,
                                                                        combinedFacility: combinedFacility
                                                                    };

                                                                    $http.post('services/xlEmployeeExport/printall/', dataObj)
                                                                        .success(
                                                                            function(data, status, headers, config) {
                                                                                if (data.status != "invalidRequest") {
                                                                                    $rootScope.CustomData = data;

                                                                                    $scope.routeNameList = [];
                                                                                    var valueStr = "filter";
                                                                                    angular.forEach(data, function(item) {

                                                                                        var filterData = {
                                                                                            text: item.routeName,
                                                                                            value: valueStr.concat(item.routeName)
                                                                                        }
                                                                                        $scope.routeNameList.push(filterData);

                                                                                    });

                                                                                    angular.forEach(data, function(item) {
                                                                                        $scope.routeName = item.routeName;

                                                                                        angular.forEach(item.empDetails, function(item) {

                                                                                            item.employeeStatus = {
                                                                                                text: item.boardedStatus,
                                                                                                value: item.boardedFlg
                                                                                            };

                                                                                            item.routeNameList =
                                                                                                valueStr.concat($scope.routeName)

                                                                                        });

                                                                                    });

                                                                                    $scope.dataLength = data.length;
                                                                                    if ($scope.dataLength <= 0) {
                                                                                        ngDialog.open({
                                                                                            template: 'No Data Found. Kindly change the selection.',
                                                                                            plain: true
                                                                                        });
                                                                                        $scope.customRouteViewShow = false;
                                                                                        return false;
                                                                                    }
                                                                                    $rootScope.customRouteDetails = data;
                                                                                    $scope.customRouteViewShow = false;
                                                                                    $scope.tableDataShow = true;
                                                                                }
                                                                            }
                                                                        ).error(
                                                                            function(data, status, headers, config) {
                                                                                // log error
                                                                            });

                                                                    if (totalNoOfEmployeeCount == 0) {
                                                                        $scope.customRouteDetails.splice(moveFromIndex, 1);
                                                                    }

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

            $scope.updateDropSeq = function(item, route, index, parentIndex) {
                if (item.isUpdateClicked) {
                    $confirm({
                            text: "Are you sure you want to change the drop sequence from " +
                                item.pickUpTime +
                                " to " +
                                item.dropSequence + " ?",
                            title: 'Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })
                        .then(
                            function() {
                                item.pickUpTime = item.dropSequence;
                                item.isUpdateClicked = false;
                                var dataObj = {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    },
                                    userId: profileId,
                                    assignRouteId: route.routeId,
                                    time: item.dropSequence,
                                    requestId: item.requestId,
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
                                            }

                                            $scope.getRoutes(route);
                                        })
                                    .error(
                                        function(data, status,
                                            headers, config) {
                                            // log error
                                        });
                            });
                } else {
                    item.isUpdateClicked = true;
                }

            };


            $scope.updatePickUpTime = function(item, index) {
                item.pickUpTimeIsClicked = true;
                $scope.pickUpTimeAdhoc = item.pickUpTime;
            }

            $scope.updateDropTime = function(item, index) {

                item.pickUpTimeIsClicked = true;
                $scope.dropTimeUpdate = item.pickUpTime;
            }

            $scope.saveDropSeq = function(item, items, index, dropTimeUpdate) {

                $confirm({
                        text: "Are you sure you want to change the drop sequence from " +
                            item.pickUpTime +
                            " to " +
                            dropTimeUpdate + " ?",
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
                                userId: profileId,
                                assignRouteId: items.routeId,
                                time: dropTimeUpdate,
                                requestId: item.requestId,
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

                                            item.pickUpTime = dropTimeUpdate;
                                            item.pickUpTimeIsClicked = false;
                                        }
                                    })
                                .error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        });

            }


            $scope.savePickUpTime = function(item, index,
                pickUpTimeAdhoc) {
                item.pickUpTimeIsClicked = false;
                item.createNewAdHocTime = $scope.convertToTime(pickUpTimeAdhoc);

                $confirm({
                        text: "Are you sure you want to change the pickup time from " +
                            item.pickUpTime +
                            " to " +
                            pickUpTimeAdhoc,
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
                                assignRouteId: item.routeId,
                                time: pickUpTimeAdhoc,
                                requestId: item.requestId,
                                userId: profileId,
                                combinedFacility: combinedFacility

                            };

                            $http.post('services/zones/updatePickUpTime/', dataObj)
                                .success(function(data, status,
                                    headers, config) {
                                    if (data.status != "invalidRequest") {
                                        if (data.status == 'B') {
                                            item.isUpdateClicked = false;
                                            ngDialog.open({
                                                template: 'Fail to update pickup time because the employee has already been boarded.',
                                                plain: true
                                            });

                                        } else if (data.status == 'D') {
                                            item.isUpdateClicked = false;
                                            ngDialog.open({
                                                template: 'Fail to update pickup time because the employee has already been dropped',
                                                plain: true
                                            });

                                        } else if (data.status == 'NO') {
                                            item.isUpdateClicked = false;
                                            ngDialog.open({
                                                template: 'Fail to update pickup time because the employee is No-show',
                                                plain: true
                                            });

                                        } else {

                                            item.pickUpTime = pickUpTimeAdhoc;

                                            item.isUpdateClicked = false;
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
                            item.isUpdateClicked = false;
                        });

            };

            $scope.cancelPickupTime = function(item, index) {
                item.pickUpTimeIsClicked = false;

            };


            $scope.changeEmployeeStatus = function(item, items, employeeDetails) {

                $confirm({
                    text: "Are you sure you want to change employee status from " +
                        item.boardedStatus +
                        " to " +
                        employeeDetails.text + " ?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(function() {
                    var dataObj = {
                        eFmFmEmployeeTravelRequest: {
                            requestId: item.requestId
                        },
                        efmFmAssignRoute: {
                            assignRouteId: items.assignRouteId
                        },
                        boardedFlg: employeeDetails.value,
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/zones/updateEmployeeStatus/', dataObj)
                        .success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                item.boardedStatus = employeeDetails.text;
                                item.boardedFlg = employeeDetails.value;
                                ngDialog.open({
                                    template: 'Request status updated successfully.',
                                    plain: true
                                });

                            }
                        }).
                    error(function(data, status, headers, config) {});

                }, function() {
                    item.employeeStatus = {
                        text: item.boardedStatus,
                        value: item.boardedFlg
                    };
                });
            };

            $scope.getRoutes = function(zone, search) {

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
                if (search.advanceflag == 'true') {
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
                        advanceFlag: search.advanceflag,
                        combinedFacility: combinedFacility
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
                        advanceFlag: search.advanceflag,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                }

                $http
                    .post('services/zones/allroutes/', dataObj)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.routesData = data.routeDetails;
                                angular
                                    .forEach(
                                        $scope.routesData,
                                        function(item) {

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

            $scope.addEscort = function(items) {
                items.addFlag = true;

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    assignRouteId: items.routeId,
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

            $scope.saveAddedEscort = function(selectedEscort, items) {
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
                                assignRouteId: items.routeId,
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
                                            items.escortName = selectedEscort.escortName;
                                            items.escortRequired = 'Y';
                                            items.addFlag = false;
                                        }
                                    }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        },
                        function() {});

            };

            $scope.cancelAddingEscort = function(items) {
                items.addFlag = false;
            };

            $scope.removeEscort = function(items) {
                $confirm({
                    text: "Are you sure you want to remove the Escort?",
                    title: 'Delete Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                }).then(
                    function() {
                        items.addFlag = false;
                        var escortCheckInId = 0;
                        if (items.escortId == 'N') {
                            escortCheckInId = 0;
                        } else {
                            escortCheckInId = items.escortId;
                        }
                        var dataObj = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            assignRouteId: items.routeId,
                            searchType: 'removeEscort',
                            escortCheckInId: escortCheckInId,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/zones/addOrRemoveEscorts/',
                            dataObj).success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    items.escortName = "Not Required";
                                    items.escortRequired = 'N';
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                    },
                    function() {});

            };

            $scope.manualTripEnd = function(items, zone) {
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
                            assignRouteId: items.routeId,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/zones/employeeStatus/', dataObj).success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {

                                    if (data.status == "success") {
                                        var dummyVehicleNumberCheck = items.vehicleNumber;
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
                                            assignRouteId: items.routeId,
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

                                                            items.tripStatus = "Completed";
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

                                        $(".items" + items.zoneRouteId + items.routeId)
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

            $scope.deleteEmptyRoute = function(route, zone) {

                var index = _.findIndex($rootScope.CustomData, {
                    routeId: route.routeId
                });
                var CustomData = $rootScope.CustomData;

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
                                userId: profileId,
                                assignRouteId: route.routeId,
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



                                                $scope.customRouteDetails.splice(index, 1);


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

            $scope.searchBy = function(searchText, searchType) {
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

            $scope.searchByTripTypeShift = function(search) {

                $scope.search.advanceflag = 'true';
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
                        combinedFacility: combinedFacility
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

                                    } else {
                                        $scope.serachResultFound = true;
                                        $scope.zoneData = data.routeDetails;
                                        $scope.numberOfRoutes = data.totalNumberOfRoutes;

                                    }
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error 
                            });
                }
            };

            $scope.deleteEmployee = function(item, items, index, combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = facilityDetails;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                var routeIndex = _.findIndex($scope.routesData, {
                    routeId: items.routeId
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
                                eFmFmEmployeeTravelRequest: {
                                    requestId: item.requestId
                                },
                                userId: profileId,
                                efmFmAssignRoute: {
                                    assignRouteId: items.routeId
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
                                                items.empDetails
                                                    .splice(index,
                                                        1);

                                                var data = {
                                                    "searchDate": items.tripActualAssignDate,
                                                    "tripType": items.tripType,
                                                    "shiftTime": items.shiftTime,
                                                    "empDetailsFlg": "A"
                                                };

                                                $scope.searchRouteDetails(data);

                                                ngDialog.open({
                                                    template: 'Request deleted successfully.',
                                                    plain: true
                                                });

                                            });
                                        }
                                    }).error(
                                    function(data, status,
                                        headers, config) {
                                        // log error
                                    });
                        });
            };

            $scope.setSearchTripType = function(tripType) {
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
                                $scope.search.shiftTime = $scope.shiftsTime[0];
                            }

                        }).error(function(data, status, headers, config) {});
                } else {
                    $scope.shiftsTime = [];
                }
            };

            $scope.changeRoutesDropDown = function(item, moveToZone) {

                item.isZoneclicked = true;
                angular.forEach($scope.routesData, function(item) {
                    angular.forEach(item.empDetails, function(item) {
                        item.isZoneclicked = false;
                    });
                });
                item.isZoneclicked = true;

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


                var routeIndex = _.findIndex($rootScope.CustomData, {
                    routeId: route.routeId
                });



                $scope.routeId = $rootScope.CustomData[routeIndex].assignRouteId;
                $scope.waypoints = $rootScope.CustomData[routeIndex].waypoints;
                $scope.baseLatLong = $rootScope.CustomData[routeIndex].baseLatLong;

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/singleRouteMap.jsp',
                    controller: 'singleRouteMapViewCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        routeId: function() {
                            return $scope.routeId;
                        },
                        waypoints: function() {
                            return $scope.waypoints;
                        },
                        baseLatLong: function() {
                            return $scope.baseLatLong;
                        },
                        route: function() {
                            return route;
                        }
                    }
                });

            };

        }

    };

    var editBucketCustomCtrl = function($scope, $modalInstance, $state, $http,
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
                                        template: 'Sorry please change the vehicle heaving  capacity more than ' + route.vehicleAvailableCapacity + '.',
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


    angular.module('efmfmApp').controller('editBucketCustomCtrl',
        editBucketCustomCtrl);
    angular.module('efmfmApp').controller('customRouteViewCtrl',
        customRouteViewCtrl);

}());