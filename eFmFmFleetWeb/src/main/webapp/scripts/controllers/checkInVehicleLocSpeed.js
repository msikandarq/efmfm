/*
@date                   07/01/2016
@Author                 Saima Aziz
@Description    
@Main Controllers       checkInVehicalLocSpeedCtrl
@Modal Controllers      
@template               partials/home.checkInVehicalLocSpeed.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
07/01/2016  Saima Aziz      Initial Creation
07/03/2016  Saima Aziz      Final Creation
*/
(function() {
    var checkInVehicalLocSpeedCtrl = function($scope, $http, $state, $interval, $rootScope, ngDialog) {

        if (!$scope.isLiveTrackingActive || $scope.isLiveTrackingActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $('.admin_home').addClass('active');
            $('.vehicleLoc_admin').addClass('active');
            $scope.dataLoaded = false;
            $scope.selectedRoute;
            $scope.cabLocation = [];
            $scope.singleMapData = {};
            $scope.mapData = [];
            $scope.showViewAllButton = false;
            $scope.mapData = [];

            $scope.getMapData = function() {
                dataObj = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacility,
                };
                $http.post('services/view/checkInVehicleTrack/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.mapData = data;

                        $scope.defaultLocation = $scope.officeLocation;
                        $scope.dataLoaded = true;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.viewAllCabsInterval = $interval($scope.getMapData, 4000);

            var dereg = $rootScope.$on('$locationChangeSuccess', function() {
                $interval.cancel($scope.viewAllCabsInterval);
                dereg();
            });

            $scope.getMapData();


            $scope.searchRoute = function(empId) {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    employeeId: empId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/view/enmployeeSerchInLiveTrip/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data[0].status == 'empIdNotExist') {
                            ngDialog.open({
                                template: 'Employee Id does not exist. Please check the Employee Id.',
                                plain: true
                            });

                        }
                        if (data[0].status == 'notExistInTracking') {
                            ngDialog.open({
                                template: 'This Employee does not exist in the tracking system.Please check the Employee Id.',
                                plain: true
                            });

                        }
                        if (data[0].status == "notAllocatedForCab") {
                            ngDialog.open({
                                template: 'A cab has not allocated to this Employee yet. Please try again later.',
                                plain: true
                            });

                        }
                        if (data[0].status == "vehicleNotStarted") {
                            ngDialog.open({
                                template: 'Vehicle has not started yet for this Empployee. Please try again later.',
                                plain: true
                            });

                        } else {
                            $scope.showViewAllButton = true;
                            $scope.resultRouteName = data[0].zoneName;
                        }
                    }
                }).
                error(function(data, status, headers, config) {});
            };

            $scope.getMap = function() {
                return $scope.mapData;
            };
        }
    };

    angular.module('efmfmApp').controller('checkInVehicalLocSpeedCtrl', checkInVehicalLocSpeedCtrl);
}());