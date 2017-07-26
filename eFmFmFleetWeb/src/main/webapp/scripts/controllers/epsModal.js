/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       epsModalCtrl
@Modal Controllers      epsModalInstanceCtrl
@template               partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var epsModalCtrl = function($scope, $modal, $log, $http) {
        $scope.openEPSModel = function(index, size) {
            switch (index) {
                case 0:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/epsTotalPlannedPickup.jsp',
                        controller: 'epsModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/epsEmployeeScheduled.jsp',
                        controller: 'epsModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/epsPicked.jsp',
                        controller: 'epsModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/epsNoShow.jsp',
                        controller: 'epsModalInstanceCtrl',
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
            }
        };
    };

    var epsModalInstanceCtrl = function($scope, $modalInstance, $state, index, $http) {
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");
        $scope.gotPickUpStatus = false;
        $scope.getTotalPlannedPickup = function() {
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "pickuprequest",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotPickUpStatus = true;
                    $scope.totalPlannedPickupData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getEmployeeScheduled = function() {
            $scope.gotPickUpStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "pickupschedule",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotPickUpStatus = true;
                    $scope.employeeScheduledData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getEmployeePicked = function() {
            $scope.gotPickUpStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "pickupboarded",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotPickUpStatus = true;
                    $scope.employeePickedData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getNoShow = function() {
            $scope.gotPickUpStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "noshowpickup",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotPickUpStatus = true;
                    $scope.noShowData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        switch (index) {
            case 0:
                $scope.getTotalPlannedPickup();
                break;
            case 1:
                $scope.getEmployeeScheduled();
                break;
            case 2:
                $scope.getEmployeePicked();
                break;
            case 3:
                $scope.getNoShow();
                break;
        }

        $scope.saveInExcel = function(modal) {
            $scope.currentModalOpen = modal;

            if ($scope.currentModalOpen == 'totalPlannedPickup') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "EMPLOYEESTATUS",
                    requestStatus: "pickuprequest",
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http({
                    url: 'services/dashboard/getDashBoardDetailListDwn/',
                    method: "POST",
                    data: dataObj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function(data, status, headers, config) {
                    var blob = new Blob([data], {});

                    saveAs(blob, 'Total Planned Pickup' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });

            }

            if ($scope.currentModalOpen == 'employeeAllocatedToCab') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "EMPLOYEESTATUS",
                    requestStatus: "pickupschedule",
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http({
                    url: 'services/dashboard/getDashBoardDetailListDwn/',
                    method: "POST",
                    data: dataObj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function(data, status, headers, config) {
                    var blob = new Blob([data], {});

                    saveAs(blob, 'Employee Allocated To Cab' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });

            }

            if ($scope.currentModalOpen == 'employeePicked') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "EMPLOYEESTATUS",
                    requestStatus: "pickupboarded",
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http({
                    url: 'services/dashboard/getDashBoardDetailListDwn/',
                    method: "POST",
                    data: dataObj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function(data, status, headers, config) {
                    var blob = new Blob([data], {});

                    saveAs(blob, 'Employee Picked' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });

            }

            if ($scope.currentModalOpen == 'employeeNoShow') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "EMPLOYEESTATUS",
                    requestStatus: "noshowpickup",
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http({
                    url: 'services/dashboard/getDashBoardDetailListDwn/',
                    method: "POST",
                    data: dataObj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function(data, status, headers, config) {
                    var blob = new Blob([data], {});

                    saveAs(blob, 'Employee No Show' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });


            }
        }

        $scope.ok = function() {
            $modalInstance.close();
            $state.go('home.dashboard');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    angular.module('efmfmApp').controller('epsModalInstanceCtrl', epsModalInstanceCtrl);
    angular.module('efmfmApp').controller('epsModalCtrl', epsModalCtrl);

}());