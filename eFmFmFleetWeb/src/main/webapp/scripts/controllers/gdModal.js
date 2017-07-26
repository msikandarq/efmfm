/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       gdModalCtrl
@Modal Controllers      gdModalInstanceCtrl
@template               partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var gdModalCtrl = function($scope, $modal, $log) {
        $scope.openGDModel = function(index, size) {
            switch (index) {
                case 0:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/gdLicense.jsp',
                        controller: 'gdModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/gdMedicalDue.jsp',
                        controller: 'gdModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/gdPolicVerification.jsp',
                        controller: 'gdModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/gdDDTraining.jsp',
                        controller: 'gdModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/gdOverSpeed.jsp',
                        controller: 'gdModalInstanceCtrl',
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

                case 5:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/gdAccidentAss.jsp',
                        controller: 'gdModalInstanceCtrl',
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
    angular.module('efmfmApp').controller('gdModalCtrl', gdModalCtrl);

    var gdModalInstanceCtrl = function($scope, $modalInstance, $state, index, $http) {
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");
        $scope.colorFlgData = [{
                flagId: 'R',
                flagName: 'Red - 10 Days'
            },
            {
                flagId: 'Y',
                flagName: 'Yellow - 20 Days'
            },
            {
                flagId: 'G',
                flagName: 'Green - 30 Days'
            }
        ];
        $scope.setSearchFlag = function(search) {
            if (search) {
                $scope.filterSearch = {
                    'colorFlg': search
                }
                $scope.filterSearchFlag = true;
            } else {
                $scope.filterSearch = search;
                $scope.filterSearchFlag = false;
            }

        };
        $scope.gotDriverGovernanceNotification = false;
        $scope.getLicenseDueData = function() {
            $scope.gotDriverGovernanceNotification = false;

            $scope.sosRoadAlertsData1 = [];
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "GOVERNANCEDRIVERS",
                requestStatus: "licenseexpire",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDriverGovernanceNotification = true;
                    $scope.sosRoadAlertsData = data;
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getMedicalDueData = function() {
            $scope.gotDriverGovernanceNotification = false;

            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "GOVERNANCEDRIVERS",
                requestStatus: "medicalexpire",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDriverGovernanceNotification = true;

                    $scope.sosRoadAlertsData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getPoliceVerification = function() {
            $scope.gotDriverGovernanceNotification = false;

            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "GOVERNANCEDRIVERS",
                requestStatus: "policeVarificationExp",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDriverGovernanceNotification = true;

                    $scope.gdPoliceVerificationData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getDDTraining = function() {
            $scope.gotDriverGovernanceNotification = false;

            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "GOVERNANCEDRIVERS",
                requestStatus: "ddtrainingexp",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotDriverGovernanceNotification = true;
                    $scope.gdDDTrainingData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };


        $scope.getTotalSpeedAlertsData = function() {
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "GOVERNANCEDRIVERS",
                requestStatus: "speedalerts",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.sosRoadAlertsData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getTotalAccidentData = function() {
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "GOVERNANCEDRIVERS",
                requestStatus: "accidentalerts",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.sosRoadAlertsData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };



        switch (index) {
            case 0:
                $scope.getLicenseDueData();
                break;
            case 1:
                $scope.getMedicalDueData();
                break;
            case 2:
                $scope.getPoliceVerification();
                break;
            case 3:
                $scope.getDDTraining();
                break;
            case 4:
                $scope.getTotalSpeedAlertsData();
                break;
            case 5:
                $scope.getTotalAccidentData();
                break;
        }

        $scope.saveInExcel = function(modal) {
            $scope.currentModalOpen = modal;

            if ($scope.currentModalOpen == 'gdLicense') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "GOVERNANCEDRIVERS",
                    requestStatus: "licenseexpire",
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

                    saveAs(blob, 'License Due For Renewal' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });

            }
            if ($scope.currentModalOpen == 'gdMedical') {
                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "GOVERNANCEDRIVERS",
                    requestStatus: "medicalexpire",
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

                    saveAs(blob, 'Medical Due For Renewal' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });

            }
            if ($scope.currentModalOpen == 'gdPoliceVerification') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "GOVERNANCEDRIVERS",
                    requestStatus: "policeVarificationExp",
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

                    saveAs(blob, 'Police Varification Due For Renewal' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });

            }
            if ($scope.currentModalOpen == 'gdDDTraining') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "GOVERNANCEDRIVERS",
                    requestStatus: "ddtrainingexp",
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

                    saveAs(blob, 'DD Training Due For Renewal' + '.xlsx');
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
    angular.module('efmfmApp').controller('gdModalInstanceCtrl', gdModalInstanceCtrl);

}());