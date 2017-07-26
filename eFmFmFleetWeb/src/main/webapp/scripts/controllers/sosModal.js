/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       sosModalCtrl
@Modal Controllers      sosModalInstanceCtrl
@template               partials/dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var sosModalCtrl = function($scope, $modal, $log, $http, ngDialog) {
        $scope.openSOSModel = function(index, size) {
            switch (index) {
                case 0:
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/dashboardModal/sosAlerts.jsp',
                        controller: 'sosModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/sosRoadAlerts.jsp',
                        controller: 'sosModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/sosOpenAlerts.jsp',
                        controller: 'sosModalInstanceCtrl',
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
                        templateUrl: 'partials/modals/dashboardModal/sosCloseAlerts.jsp',
                        controller: 'sosModalInstanceCtrl',
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
    angular.module('efmfmApp').controller('sosModalCtrl', sosModalCtrl);

    var sosModalInstanceCtrl = function($scope, $modalInstance, $state, $http, $timeout, index) {
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");
        $scope.sosRoadAlertsData = [];
        $scope.showActionDiv = false;
        $scope.action = {};
        $scope.gotSosData = false;
        $scope.getSOSAlertData = function() {
            $scope.gotSosData = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "ALERTS",
                requestStatus: "sos",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.sosAlertsData = data;
                    $scope.gotSosData = true;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getRoadAlerts = function() {
            $scope.gotSosData = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "ALERTS",
                requestStatus: "roadalerts",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.sosRoadAlertsData = data;
                    $scope.gotSosData = true;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };


        $scope.getOpenAlerts = function() {
            $scope.gotSosData = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "ALERTS",
                requestStatus: "openalerts",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.sosOpenAlertsData = data;
                    $scope.gotSosData = true;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getCloseAlerts = function() {
            $scope.gotSosData = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                tripType: "ALERTS",
                requestStatus: "closealerts",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/dashboard/getDashBoardDetailList/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.gotSosData = true;
                    $scope.sosRoadAlertsData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };




        switch (index) {
            case 0:
                $scope.getSOSAlertData();
                break;
            case 1:
                $scope.getRoadAlerts();
                break;
            case 2:
                $scope.getOpenAlerts();
                break;
            case 3:
                $scope.getCloseAlerts();
                break;
        }

        $scope.saveInExcel = function(modal) {
            $scope.currentModalOpen = modal;

            if ($scope.currentModalOpen == 'sosAlert') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    userId: profileId,
                    tripType: "ALERTS",
                    requestStatus: "sos",
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
                    saveAs(blob, 'SOS Alerts' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });
            }

            if ($scope.currentModalOpen == 'sosRoadAlert') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "ALERTS",
                    requestStatus: "roadalerts",
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
                    saveAs(blob, 'Road Alerts' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });
            }

            if ($scope.currentModalOpen == 'alertsOpen') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "ALERTS",
                    requestStatus: "openalerts",
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
                    saveAs(blob, 'Alerts Open' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });
            }

            if ($scope.currentModalOpen == 'alertsClosed') {

                var dataObj = {
                    eFmFmEmployeeRequestMaster: {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    },
                    tripType: "ALERTS",
                    requestStatus: "closealerts",
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
                    saveAs(blob, 'Alerts Close' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });
            }

        }


        $scope.openAction = function(openAlert) {
            $('.loading').show();
            if ($scope.showActionDiv) {
                $scope.showActionDiv = false;
            } else {
                $scope.showActionDiv = true;
                $('.loading').show();
                $scope.action.text = openAlert.description;
                $scope.action.facilityName = openAlert.facilityName;
                $scope.action.facilityId = openAlert.facilityId;
                $scope.action.alertId = openAlert.tripAlertId;
                $scope.action.assignRouteId = openAlert.assignRouteId;
                $scope.selectedAlert_DriverName = openAlert.driverName;
                $scope.selectedAlert_DriverNumber = openAlert.driverNumber;
                $scope.selectedAlert_AlertId = openAlert.tripAlertId;
                $scope.selectedAlert_AlertType = openAlert.alertType;
                $scope.selectedAlert_Title = openAlert.tittle;
                $scope.selectedAlert_vehicleNumber = openAlert.vehicleNumber;
            }
        };

        $scope.ok = function() {

            //put the values from text box in a variable
            //check the Login userId and Password
            //if the Login is success then close the Modal and proceed to Home Page
            $modalInstance.close();
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.updateAlert = function(action) {
            var data = {
                branchId: action.facilityId,
                assignRouteId: action.assignRouteId,
                tripAlertsId: action.alertId,
                alertClosingDescription: action.text,
                userId: profileId,
                facilityName: action.facilityName
            };
            $http.post('services/dashboard/updateAlertDesc/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    var openAlertIndex = _.findIndex($scope.sosOpenAlertsData, {
                        tripAlertId: action.alertId
                    });
                    $scope.sosOpenAlertsData[openAlertIndex].description = action.text;
                    $scope.showActionDiv = false;
                    $('.loading').hide();
                    ngDialog.open({
                        template: 'The Alert has been updated.',
                        plain: true
                    });
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });

        };

        $scope.closeAlert = function(action) {
            var data = {
                branchId: action.facilityId,
                assignRouteId: action.assignRouteId,
                tripAlertsId: action.alertId,
                alertClosingDescription: action.text,
                userId: profileId,
                facilityName: action.facilityName
            };
            $http.post('services/dashboard/closeOpenAlert/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.showActionDiv = false;
                    $('.loading').hide();
                    $('.row' + action.alertId).hide('slow');
                    ngDialog.open({
                        template: 'The Alert has been close.',
                        plain: true
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        };

        $scope.cancelOpenAlertModal = function() {
            $scope.showActionDiv = false;
            $('.loading').hide();
        };
    };
    angular.module('efmfmApp').controller('sosModalInstanceCtrl', sosModalInstanceCtrl);

}());