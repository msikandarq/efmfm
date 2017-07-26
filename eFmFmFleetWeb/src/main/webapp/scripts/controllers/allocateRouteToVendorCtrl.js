(function() {
    var allocateRouteToVendorCtrl = function($scope, $http, $state, $modal, $log,
        $timeout, $confirm, ngDialog, $rootScope) {
        $scope.bulkRouteData = {};
        $scope.bulkRouteData.length = 0;
        $scope.bulkRouteViewShow = false;
        $scope.serviceMappingZoneDiv = false;
        $scope.search = {};
        $scope.allInspectionVendors = [];
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

        $scope.searchRouteDetails = function(data, combinedFacilityId) {
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

            $http.post('services/route/allocateToVendor/', dataObj)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            angular.forEach(data, function(value, key) {
                                value["status"] = false;
                            });

                            $scope.bulkRouteData = data;
                            $scope.bulkRouteViewShow = true;
                        }
                    }).error(
                    function(data, status, headers, config) {
                        // log error
                    });
        }

        $scope.allocateRouteToVendor = function(locationId, data, combinedFacilityId) {

            if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                combinedFacilityId = 0;
            } else {
                combinedFacilityId = String(combinedFacilityId);
            }
            var routeSelectedId = _.where(locationId, {
                status: true
            });
            var routeId = String(_.pluck(routeSelectedId, 'assignRouteId'));
            if (routeId == "") {
                ngDialog.open({
                    template: 'Kindly allocate route to vendor',
                    plain: true
                });
                return false;
            }

            if (data.vendor == undefined) {
                ngDialog.open({
                    template: 'Kindly Choose vendor name',
                    plain: true
                });
                return false;
            }

            var dataObj = {
                branchId: branchId,
                userId: profileId,
                bulkRouteIds: routeId,
                assignedVendorName: data.vendor.name,
                combinedFacility: combinedFacilityId
            };

            $http.post('services/route/assignVendor/', dataObj)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == 'success') {
                                alert("Routes allocated to vendor")
                            }
                        }
                    }).error(
                    function(data, status, headers, config) {
                        // log error
                    });

        }

        $scope.checkAll = function() {
            angular.forEach($scope.bulkRouteData, function(value, key) {
                value["status"] = true;
            });
        }

        $scope.uncheckAll = function() {
            angular.forEach($scope.bulkRouteData, function(value, key) {
                value["status"] = false;
            });
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
                if (data.status != "invalidRequest") {
                    ngDialog.open({
                        template: data.status,
                        plain: true
                    });
                    $scope.bulkRouteData.length = 0;

                    $http.post('services/xlEmployeeExport/openRoutes/', $scope.dataObj)
                        .success(
                            function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $scope.bulkRouteData = data;
                                    $scope.bulkRouteViewShow = true;
                                }
                            }).error(
                            function(data, status, headers, config) {
                                // log error
                            });
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });

        }


    };


    angular.module('efmfmApp').controller('allocateRouteToVendorCtrl',
        allocateRouteToVendorCtrl);

}());