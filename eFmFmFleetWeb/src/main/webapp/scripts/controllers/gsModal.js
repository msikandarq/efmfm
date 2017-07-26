/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       gsModalCtrl
@Modal Controllers      guestRequestStatusCtrl
@template               partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var gsModalCtrl = function($scope, $modal, $log, $http) {
        $scope.openGSModal = function(index, size) {
            switch (index) {
                case 0:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/guestTotalPlannedRequests.jsp',
                        controller: 'guestRequestStatusCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/guestemployeeScheduled.jsp',
                        controller: 'guestRequestStatusCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/guestPicked.jsp',
                        controller: 'guestRequestStatusCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/guestNoShow.jsp',
                        controller: 'guestRequestStatusCtrl',
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

    var guestRequestStatusCtrl = function($scope, $modalInstance, $state, $http, index) {
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");
        $scope.gotGuestRequestStatus = false;
        $scope.getTotalPlannedDrop = function() {
            $scope.gotGuestRequestStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "guestrequests",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotGuestRequestStatus = true;
                    $scope.totalPlannedDropData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getEmployeeScheduled = function() {
            $scope.gotGuestRequestStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "guestschedulerequests",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotGuestRequestStatus = true;
                    $scope.employeeScheduledData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getEmployeeDropped = function() {
            $scope.gotGuestRequestStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "guestboardedordropped",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotGuestRequestStatus = true;
                    $scope.employeeDroppedData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getNoShow = function() {
            $scope.gotGuestRequestStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "guestnoshow",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotGuestRequestStatus = true;
                    $scope.noShowData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        switch (index) {
            case 0:
                $scope.getTotalPlannedDrop();
                break;
            case 1:
                $scope.getEmployeeScheduled();
                break;
            case 2:
                $scope.getEmployeeDropped();
                break;
            case 3:
                $scope.getNoShow();
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

    angular.module('efmfmApp').controller('guestRequestStatusCtrl', guestRequestStatusCtrl);
    angular.module('efmfmApp').controller('gsModalCtrl', gsModalCtrl);

}());