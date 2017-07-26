/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       vsModalCtrl
@Modal Controllers      vsModalInstanceCtrl
@template               partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var vsModalCtrl = function($scope, $modal, $log, $http) {
        $scope.openVSModel = function(index, size) {
            switch (index) {
                case 0:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/vsRegisteredVehicle.jsp',
                        controller: 'vsModalInstanceCtrl',
                        size: size,
                        resolve: {
                            index: function() {
                                return index;
                            }
                        }
                    });
                    modalInstance.result.then(function() {}, function() {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                    break;
                case 1:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/vsVehicleAvailable.jsp',
                        controller: 'vsModalInstanceCtrl',
                        size: size,
                        resolve: {
                            index: function() {
                                return index;
                            }
                        }
                    });

                    modalInstance.result.then(function() {}, function() {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                    break;

                case 2:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/vsDropOnRoad.jsp',
                        controller: 'vsModalInstanceCtrl',
                        size: size,
                        resolve: {
                            index: function() {
                                return index;
                            }
                        }
                    });

                    modalInstance.result.then(function() {}, function() {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                    break;

                case 3:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/vsPickOnRoad.jsp',
                        controller: 'vsModalInstanceCtrl',
                        size: size,
                        resolve: {
                            index: function() {
                                return index;
                            }
                        }
                    });

                    modalInstance.result.then(function() {}, function() {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                    break;

                case 4:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/vsServices.jsp',
                        controller: 'vsModalInstanceCtrl',
                        size: size,
                        resolve: {
                            index: function() {
                                return index;
                            }
                        }
                    });

                    modalInstance.result.then(function() {}, function() {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                    break;
            } //switch ends
        };
    };
    angular.module('efmfmApp').controller('vsModalCtrl', vsModalCtrl);

    var vsModalInstanceCtrl = function($scope, $modalInstance, $state, index, $http) {
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");

        $scope.getRegisterdVehicle = function() {
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "VEHICLESTATUS",
                requestStatus: "registeredvehicle",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vehicleRegisteredData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };



        $scope.getVehicleAvailable = function() {
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "VEHICLESTATUS",
                requestStatus: "available",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vehicleAvailableData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getDropVehicleOnRoad = function() {

            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "VEHICLESTATUS",
                requestStatus: "rolledout",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.dropVehicleOnRoadData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getPickVehicleOnRoad = function() {
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "VEHICLESTATUS",
                requestStatus: "rolledin",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.pickVehicleOnRoadData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getVehicleForServices = function() {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "rolledin",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.vehicleForServicesData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        switch (index) {
            case 0:
                $scope.getRegisterdVehicle();
                break;
            case 1:
                $scope.getVehicleAvailable();
                break;
            case 2:
                $scope.getDropVehicleOnRoad();
                break;
            case 3:
                $scope.getPickVehicleOnRoad();
                break;
            case 4:
                $scope.getVehicleForServices();
                break;

        }


        $scope.ok = function() {

            $modalInstance.close();
            $state.go('home.dashboard');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    angular.module('efmfmApp').controller('vsModalInstanceCtrl', vsModalInstanceCtrl);

}());