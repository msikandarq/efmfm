/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       edsModalCtrl
@Modal Controllers      dropStatusCtrl
@template               partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var edsModalCtrl = function($scope, $modal, $log, $http) {
        $scope.openEDSModel = function(index, size) {
            switch (index) {
                case 0:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/edstotalPlannedDrop.jsp',
                        controller: 'dropStatusCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/edsemployeeScheduled.jsp',
                        controller: 'dropStatusCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/edsDropped.jsp',
                        controller: 'dropStatusCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/edsNoShow.jsp',
                        controller: 'dropStatusCtrl',
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

    var dropStatusCtrl = function($scope, $modalInstance, $state, $http, index) {
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");
        $scope.gotDropStatus = false;
        $scope.getTotalPlannedDrop = function() {
            $scope.gotDropStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "droprequest",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDropStatus = true;

                    $scope.totalPlannedDropData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getEmployeeScheduled = function() {
            $scope.gotDropStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "dropschedule",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDropStatus = true;
                    $scope.employeeScheduledData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getEmployeeDropped = function() {
            $scope.gotDropStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "dropboarded",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDropStatus = true;
                    $scope.employeeDroppedData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getNoShow = function() {
            $scope.gotDropStatus = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "EMPLOYEESTATUS",
                requestStatus: "noshowdrop",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDropStatus = true;
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

        $scope.saveInExcel = function(modal) {
            $scope.currentModalOpen = modal;

            if ($scope.currentModalOpen == 'totalPlannedDrop') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "EMPLOYEESTATUS",
                    requestStatus: "droprequest",
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

                    saveAs(blob, 'Total Planned Drop' + '.xlsx');
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
                    requestStatus: "dropschedule",
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

            if ($scope.currentModalOpen == 'employeeDropped') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "EMPLOYEESTATUS",
                    requestStatus: "dropboarded",
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

                    saveAs(blob, 'Employee Dropped' + '.xlsx');
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
                    requestStatus: "noshowdrop",
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
        };

        $scope.ok = function() {
            $modalInstance.close();
            $state.go('home.dashboard');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var edsModalInstanceCtrl = function($scope, $modalInstance, $state) {
        $scope.ok = function() {
            $modalInstance.close();
            $state.go('home.dashboard');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    angular.module('efmfmApp').controller('dropStatusCtrl', dropStatusCtrl);
    angular.module('efmfmApp').controller('edsModalCtrl', edsModalCtrl);

}());