/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       viewMapCtrl
@Modal Controllers      
@template               partials/home.viewmap.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var viewMapCtrl = function($scope, $timeout, $http, $state, $interval, $rootScope, ngDialog, $modal) {

        if (!$scope.isLiveTrackingActive || $scope.isLiveTrackingActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $('.veiwMapMenu').addClass('active');
            $scope.dataLoaded = false;
            $scope.selectedRoute;
            $scope.cabLocation = [];
            $scope.singleMapData = {};
            $scope.mapData = [];
            $scope.showViewAllButton = false;
            $scope.gotMapData = false;
            $scope.liveTrackingTripType = false;
            $scope.liveTrackingShiftTime = false;
            $scope.getResultButton = false;
            $scope.routeDivLiveTracking = false;
            $scope.searchTypeChangeStatus = false;
            $scope.searchTypeButton = false;
            $scope.facilityData = {};
            $scope.facilityDetails = userFacilities;
            var array = JSON.parse("[" + combinedFacility + "]");
            $scope.facilityData.listOfFacility = array;

            $scope.tripTypes = [{
                'value': 'PICKUP',
                'text': 'PICKUP'
            }, {
                'value': 'DROP',
                'text': 'DROP'
            }];

            $scope.searchTypes = [{
                    'value': 'Search by VEHICLE',
                    'text': 'Vehicle'
                }, {
                    'value': 'Search by EMPLOYEE ID',
                    'text': 'EmployeeId'
                }, {
                    'value': 'Female Employee Tracking',
                    'text': 'femaleEmployeeId'
                }, {
                    'value': 'Delay Trips',
                    'text': 'delayTrips'
                },
                {
                    'value': 'Priority Tracking',
                    'text': 'priorityTracking'
                }
            ];

            $scope.trip = {};
            $scope.trip.tripType = $scope.tripTypes[0];

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

            $scope.tripTimeDateCal = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'tripTimeDate': true
                    };
                })
            };

            $scope.shiftTimeliveTracking = function() {
                $scope.liveTrackingTripType = true;
            }

            $scope.setResultButton = function(data, tripType) {
                $scope.getResultButton = true;
                $scope.mapData = [];
                $rootScope.time = data.shiftTime;
                $rootScope.tripType = data.tripType;
            }

            $scope.searchTextChangeButton = function() {
                $scope.searchTypeButton = true;
            }
            $scope.setTripTypeliveTracking = function(tripType) {
                if (tripType) {
                    $scope.liveTrackingShiftTime = true;
                    $scope.getResultButton = false;
                    $scope.mapData = [];

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
                            }

                        }).error(function(data, status, headers, config) {});
                } else {

                    $scope.liveTrackingShiftTime = false;
                }


            };

            $scope.getMapData = function(tripType, shiftTime, combinedFacilityId) {

                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                $scope.dataLoaded = false;
                $scope.mapData = [];
                $scope.cabLocation = [];

                $rootScope.$on('showAllEvent', function(event, data) {
                    $rootScope.tripType = data.tripType;
                    $rootScope.time = data.time;
                });

                if (tripType == undefined || shiftTime == undefined) {
                    tripType = $rootScope.tripType;
                    shiftTime = $rootScope.shiftTime;
                }

                if ($rootScope.tripType == undefined || $rootScope.time == undefined) {
                    if (tripType == undefined || shiftTime == undefined) {

                        $scope.routeDivLiveTracking = false;
                        var data = {
                            branchId: branchId,
                            tripType: 'PICKUP',
                            time: "00:00",
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/view/livetrips/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                data.splice(0, 1);
                                $scope.mapData = data;
                                angular.forEach($scope.mapData, function(item) {
                                    $scope.cabLocation.push(item.currentCabLatiLongi);
                                });
                                $scope.defaultLocation = $scope.officeLocation;
                                $scope.dataLoaded = true;
                                return false;
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    } else {
                        $scope.routeDivLiveTracking = true;
                        var data = {
                            branchId: branchId,
                            tripType: tripType.value,
                            time: shiftTime.shiftTime,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $rootScope.$emit('showAllEvent', data);
                        $http.post('services/view/livetrips/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                data.splice(0, 1);
                                $scope.mapDataLength = data.length;
                                if ($scope.mapDataLength == 0) {
                                    ngDialog.open({
                                        template: 'No Data Found',
                                        plain: true
                                    });
                                    $scope.routeDivLiveTracking = true;
                                    return false;
                                }

                                angular.forEach($scope.mapData, function(item) {
                                    $scope.cabLocation.push(item.currentCabLatiLongi);
                                });
                                $scope.defaultLocation = $scope.officeLocation;
                                $scope.dataLoaded = true;
                                return false;
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    }
                } else {

                    $scope.routeDivLiveTracking = true;
                    var data = {
                        branchId: branchId,
                        tripType: $rootScope.tripType,
                        time: $rootScope.time,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/view/livetrips/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            data.splice(0, 1);
                            $scope.mapData = data;

                            if (data.length > 0) {
                                $scope.gotMapData = true;
                            } else {
                                $scope.gotMapData = false;
                            }

                            $scope.MapFeedbackData = data;

                            $scope.tripType = $rootScope.tripType;
                            $scope.shiftTime = $rootScope.time;

                            angular.forEach($scope.mapData, function(item) {
                                $scope.cabLocation.push(item.currentCabLatiLongi);
                            });
                            $scope.defaultLocation = $scope.officeLocation;
                            $scope.dataLoaded = true;
                            return false;
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }

            };


            //$scope.viewAllMapInterval = $interval($scope.getMapData, 4000);  

            /* var dereg = $rootScope.$on('$locationChangeSuccess', function() {
                  $interval.cancel($scope.viewAllMapInterval);
                  dereg();
              });  
             */
            //$scope.getMapData();    

            $state.go('home.viewmap.showRoutes');


            $scope.getMap = function() {
                return $scope.mapData;
            };

            $scope.thisRouteDetails = function(index, route) {
                $state.go('home.activeRouteMap', {
                    'routeId': route.routeId
                });
            };

            $scope.refreshViewMap = function() {
                $scope.getMapData();
            };
            $scope.priorityTracking = function() {
                if ($scope.MapFeedbackData.length > 0) {
                    var data = {
                        priority: false,
                        data: $scope.MapFeedbackData[0].feedbacks
                    }
                    $scope.gotFeedbackResult = true;
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

                    //Here the VendorId will be given to the Vendor from backend
                    modalInstance.result.then(function(result) {});
                } else {
                    $scope.gotFeedbackResult = false;
                    ngDialog.open({
                        template: 'No Data Found. Please Change the Dates.',
                        plain: true
                    });
                    //                             $scope.showalertMessage('No Data Found. Please Change the Dates', "");
                }
            };
            $scope.searchTypeChange = function(value, searchType) {


                if (searchType.text == 'femaleEmployeeId') { // Service call for all Female employees Tracking.
                    $scope.searchTypeChangeStatus = false;
                    $scope.routeDivLiveTracking = true;
                    $scope.dataLoaded = false;
                    $scope.mapData = [];
                    $scope.cabLocation = [];
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };

                    $http.post('services/view/femaleTracking/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.mapData = data;
                            $scope.tripType = $rootScope.tripType;
                            $scope.shiftTime = $rootScope.time;

                            angular.forEach($scope.mapData, function(item) {
                                $scope.cabLocation.push(item.currentCabLatiLongi);
                            });
                            $scope.defaultLocation = $scope.officeLocation;
                            $scope.dataLoaded = true;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else {
                    $scope.searchTypeChangeStatus = true;
                    $scope.searchTypeButton = false;
                    $scope.mapData = [];
                }
            }

            $scope.searchRoute = function(searchBy, empId, combinedFacilityId) {

                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (searchBy && empId) {

                    $scope.dataLoaded = false;
                    $scope.mapData = [];
                    $scope.cabLocation = [];
                    $scope.routeDivLiveTracking = true;

                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        employeeId: empId,
                        searchType: searchBy.text,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/view/enmployeeSerchInLiveTrip/', dataObj).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            //               alert(data[0].status);
                            if (data[0].status != "vehicleNumNotExist" && data[0].status != "empIdNotExist" &&
                                data[0].status != "vehicleCheckedIn" && data[0].status != "vehicleNotCheckedIn" &&
                                data[0].status != "vehicleNotStarted" && data[0].status != "notExistInTracking" &&
                                data[0].status != "notAllocatedForCab") {
                                $scope.mapData = data;
                            }

                            angular.forEach($scope.mapData, function(item) {
                                $scope.cabLocation.push(item.currentCabLatiLongi);
                            });
                            $scope.defaultLocation = $scope.officeLocation;
                            $scope.dataLoaded = true;


                            if (data[0].status == 'empIdNotExist') {
                                ngDialog.open({
                                    template: 'Employee Id does not exist. Please check the Employee Id.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("Employee Id does not exist. Please check the Employee Id.")
                            }
                            if (data[0].status == 'vehicleNumNotExist') {
                                ngDialog.open({
                                    template: 'Vehicle Number does not exist. Please check the Vehicle Number.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("Employee Id does not exist. Please check the Employee Id.")
                            }


                            if (data[0].status == 'vehicleCheckedIn') {
                                ngDialog.open({
                                    template: 'Vehicle is not started yet.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("Employee Id does not exist. Please check the Employee Id.")
                            }
                            if (data[0].status == 'vehicleNotCheckedIn') {
                                ngDialog.open({
                                    template: 'Vehicle is not checkedIn yet.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("Employee Id does not exist. Please check the Employee Id.")
                            }


                            if (data[0].status == 'notExistInTracking') {
                                ngDialog.open({
                                    template: 'This Employee does not exist in the tracking system.Please check the Employee Id.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("This Employee does not exist in the tracking system.Please check the Employee Id.")
                            }
                            if (data[0].status == "notAllocatedForCab") {
                                ngDialog.open({
                                    template: 'A cab has not allocated to this Employee yet. Please try again later.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("A cab has not allocated to this Employee yet. Please try again later.");
                            }
                            if (data[0].status == "vehicleNotStarted") {
                                ngDialog.open({
                                    template: 'Vehicle has not started yet for this Empployee. Please try again later.',
                                    plain: true
                                });
                                return false;
                                //                        $scope.showalertMessage("Vehicle has not started yet for this Empployee. Please try again later.");
                            } else {
                                $scope.showViewAllButton = true;
                                $scope.resultRouteName = data[0].zoneName;
                                return false;
                            }
                        }

                    }).
                    error(function(data, status, headers, config) {});
                } else {
                    ngDialog.open({
                        template: 'Please enter a valid number',
                        plain: true
                    });
                }
            };


            $scope.viewAll = function() {
                $scope.showViewAllButton = false;
                $scope.filterViewMap = "";
                $scope.resultRouteName = "";
                $scope.searchText = "";
            };

            $scope.$watch("searchText", function(query) {
                if ($scope.searchText == "") {
                    $scope.showViewAllButton = false;
                    $scope.resultRouteName = "";
                }
            });
        }
    };
    var priorityTrackingCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, feedbackData) {
        $scope.feedbacks = feedbackData.data;
        $scope.routingPriority = feedbackData.priority;
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    angular.module('efmfmApp').controller('viewMapCtrl', viewMapCtrl);
    angular.module('efmfmApp').controller('priorityTrackingCtrl', priorityTrackingCtrl);
}());