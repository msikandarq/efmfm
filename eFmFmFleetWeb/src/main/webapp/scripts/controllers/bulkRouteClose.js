(function() {
    var bulkRouteCloseCtrl = function($scope, $http, $state, $modal, $log,
        $timeout, $confirm, ngDialog, $rootScope) {
        $scope.bulkRoute = {};
        $scope.bulkRouteData = {};
        $scope.bulkRouteData.length = 0;
        $scope.bulkRouteViewShow = false;
        $scope.serviceMappingZoneDiv = false;
        $scope.facilityData = {};
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData.listOfFacility = array;

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



        $scope.searchRouteDetails = function(data, combinedFacilityId , buttonSearchClick) {
            if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                combinedFacilityId = combinedFacility;
            } else {
                combinedFacilityId = String(combinedFacilityId);
            }

            $scope.bulkRouteViewShow = true;
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

            var dataObj = {
                branchId: branchId,
                executionDate: convertDateUTC(data.searchDate),
                tripType: data.tripType.value,
                time: data.shiftTime.shiftTime,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $scope.dataObj = dataObj;

            $http.post('services/xlEmployeeExport/openRoutes/', dataObj)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.bulkRouteData = data;
                            $rootScope.bulkRouteDataCopy = angular.copy(data);
                            if($scope.bulkRouteData.length>0){
                                $scope.escortRequiredShow = data[0].escortRequired;
                            }
                            $scope.bulkRouteViewShow = true;
                        }
                    }).error(
                    function(data, status, headers, config) {
                        // log error
                    });
        }

      

        $scope.editVehicleNumber = function(bulkRoute, index) {
            $scope.selectedVehicleNumber = $rootScope.bulkRouteDataCopy[index].vehicleNumber;
            bulkRoute.editVehicleIsClicked = true;
            var data = {
            eFmFmClientBranchPO: {
                branchId: branchId
            },
            assignRouteId: bulkRoute.routeId,
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http
            .post('services/zones/checkedinentity/', data)
            .success(
                function(data, status, headers, config) {
                    $scope.checkInEntitiesData = data.checkInList;
                    $rootScope.checkInEntitiesDatas = data.checkInList;
                    $scope.escortsData = data.escortDetails;
                }).error(function(data, status, headers, config) {
                // log error
            });


        }


       $scope.updateVehicleNumber = function(bulkRoute, index ,checkInEntitiesData, escortsData){
            $scope.checkInEntitiesData = checkInEntitiesData;
            $scope.escortsData = escortsData;
            var selectedCheckInIdIndex = _.findIndex(checkInEntitiesData, {
                  vehicleNumber: bulkRoute.vehicleNumber
                });

            if(bulkRoute.escortRequired != 'N'){
                if($rootScope.bulkRouteDataCopy[index].vehicleNumber == $scope.checkInEntitiesData[selectedCheckInIdIndex].vehicleNumber && 
                   $rootScope.bulkRouteDataCopy[index].escortName == bulkRoute.escortName){
                    ngDialog.open({
                                        template: 'No Changes Done.',
                                        plain: true
                                    });
                    bulkRoute.editVehicleIsClicked = false;
                    return false;
                }
            }else{
                if($rootScope.bulkRouteDataCopy[index].vehicleNumber == $scope.checkInEntitiesData[selectedCheckInIdIndex].vehicleNumber){
                    ngDialog.open({
                                        template: 'No Changes Done.',
                                        plain: true
                                    });
                    bulkRoute.editVehicleIsClicked = false;
                    return false;
                }
            }

            if(bulkRoute.escortRequired == 'N'){
                escortCheckInId = 0;
            }else{
                escortCheckInId = _.where(escortsData, {
                        escortName: bulkRoute.escortName
                    })[0].escortCheckInId;
            }

            $scope.bulkRouteData[index].driverName = $scope.checkInEntitiesData[selectedCheckInIdIndex].driverName;
            $scope.bulkRouteData[index].driverId = $scope.checkInEntitiesData[selectedCheckInIdIndex].driverId;
            bulkRoute.editVehicleIsClicked = false;

            var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    assignRouteId: bulkRoute.routeId,
                    vehicleId: $scope.checkInEntitiesData[selectedCheckInIdIndex].vehicleId,
                    driverId: $scope.checkInEntitiesData[selectedCheckInIdIndex].driverId,
                    deviceId: $scope.checkInEntitiesData[selectedCheckInIdIndex].deviceId,
                    escortCheckInId: escortCheckInId,
                    newCheckInId: $scope.checkInEntitiesData[selectedCheckInIdIndex].checkInId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $scope.promise = $http
                    .post('services/zones/saveeditbucket/', data)
                    .success(
                        function(data, status, headers, config) {
                            if (data.status == 'lessCapacity') {
                                ngDialog.open({
                                    template: 'Sorry please change the vehicle heaving  capacity more than ' + route.vehicleAvailableCapacity + '.',
                                    plain: true
                                });

                            } else {
                              
                                ngDialog.open({
                                    template: 'Route Details Change Successfully.',
                                    plain: true
                                });

                            }
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });

                    /*}, function() {
                        bulkRoute.editVehicleIsClicked = false;
                    });*/

        
       }

       $scope.cancelVehicleNumber = function(bulkRoute, index){
            bulkRoute.editVehicleIsClicked = false;
       }

        $scope.editBucketBulkRoute = function(bulkRoute, size) {

            if (bulkRoute.bucketStatus == 'Y') {
                bulkRoute.bucketStatus = 'N';
            } else
                bulkRoute.bucketStatus == 'Y';


            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/editBucketModal.jsp',
                controller: 'editBucketbulkCtrl',
                backdrop: 'static',
                size: size,
                resolve: {
                    route: function() {
                        return bulkRoute;
                    }
                }
            });

            modalInstance.result
                .then(function(result) {

                    var index = _.findIndex($scope.bulkRouteData, {
                        routeId: result.routeId
                    });

                    $scope.bulkRouteData[index].vehicleNumber = result.driverName.vehicleNumber;
                    $scope.bulkRouteData[index].driverName = result.driverName.driverName;
                    $scope.bulkRouteData[index].deviceId = result.driverName.deviceId;
                });
        }

        // Bulk Route Service Close

        $scope.bulkRouteCloseClick = function(bulkRoute) {
            var bulkRouteDetails = bulkRoute;
            var routeId = _.pluck(bulkRouteDetails, 'routeId');
            var checkInId = _.pluck(bulkRouteDetails, 'checkInId');
            var data = {
                branchId: branchId,
                userId: profileId,
                bulkRouteIds: String(routeId),
                bulkCheckInIds: String(checkInId),
                combinedFacility: combinedFacility
            };

            $http.post('services/xlEmployeeExport/bulkRouteClose/', data).
            success(function(data, status, headers, config) {
                ngDialog.open({
                    template: data.status,
                    plain: true
                });
                $scope.bulkRouteData.length = 0;

                $http.post('services/xlEmployeeExport/openRoutes/', $scope.dataObj)
                    .success(
                        function(data, status, headers, config) {

                            $scope.bulkRouteData = data;
                            $scope.bulkRouteViewShow = true;
                        }).error(
                        function(data, status, headers, config) {
                            // log error
                        });

            }).
            error(function(data, status, headers, config) {
                // log error
            });

        }


    };

    var editBucketbulkCtrl = function($scope, $modalInstance, $state, $http,
        $timeout, ngDialog, route, $rootScope) {
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
                            if (data.status == 'lessCapacity') {
                                ngDialog.open({
                                    template: 'Sorry please change the vehicle heaving  capacity more than ' + route.vehicleAvailableCapacity + '.',
                                    plain: true
                                });

                            } else {
                                updatedValue.availableCapacity = data.availableCapacity;
                                updatedValue.status = data.status;
                                updatedValue.checkInId = newCheckInId;
                                updatedValue.routeId = route.routeId;
                                ngDialog.open({
                                    template: 'Route Details Change Successfully.',
                                    plain: true
                                });

                                $timeout(function() {
                                    $modalInstance.close(updatedValue);
                                    ngDialog.close();
                                }, 3000);
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


    angular.module('efmfmApp').controller('editBucketbulkCtrl', editBucketbulkCtrl);
    angular.module('efmfmApp').controller('bulkRouteCloseCtrl',
        bulkRouteCloseCtrl);

}());