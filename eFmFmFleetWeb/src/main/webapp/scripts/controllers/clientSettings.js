/*
@date           04/01/2015
@Author         Saima Aziz
@Description    
@controllers    approvalCtrl
@template       partials/home.approval.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/17/2016  Saima Aziz      Initial Creation
05/17/2016  Saima Aziz      Final Creation
*/
(function() {
    var clientSettingsCtrl = function($scope, $state, $http, $location, $anchorScroll, $modal, $confirm, ngDialog) {
        if ((!$scope.superadmin || $scope.superadmin == "false")) {
            $state.go('home.accessDenied');
        } else {
            $('.admin_home').addClass('active');
            $('.clientSetting_admin').addClass('active');
            $scope.clientsData = [];
            $scope.gotClientModuleSettingsData = false;
            $scope.currentClient;

            $scope.pickDropArray = ['PICKUP', 'DROP', 'BOTH'];

            $scope.getAllClientsList = function() {
                $http.post('services/clients/clientsDetail').
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.clientsData = data;

                        angular.forEach($scope.clientsData, function(item) {
                            item.isAbout = false;
                            item.isModuleSettings = false;
                            item.isDisable = false;
                            if (item.branchName == 'ShellCHN') {
                                item.imageSrc = "images/clients/1.gif";
                            }
                            if (item.branchName == 'ServionCHN') {
                                item.imageSrc = "images/clients/2.png";
                            }
                            if (item.branchName == 'SearsPune') {
                                item.imageSrc = "images/clients/3.jpg";
                            }
                        });
                    }
                }).error(function(data, status, headers, config) {});

            };

            $scope.getAllClientsList();

            var closeAllDivs = function() {
                angular.forEach($scope.clientsData, function(item) {
                    item.isAbout = false;
                    item.isModuleSettings = false;
                    item.isDisable = false;
                });
            };

            $scope.addNewClient = function(size, type) {
                $scope.isClient = false;
                $scope.isBranch = false;

                if (type == 'client') {
                    $scope.isClient = true;
                }

                if (type == 'branch') {
                    $scope.isBranch = true;
                }

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addNewClient.jsp',
                    controller: 'addNewClientCtrl',
                    size: size,
                    backdrop: 'static',
                    resolve: {
                        isClient: function() {
                            return $scope.isClient
                        },
                        isBranch: function() {
                            return $scope.isBranch
                        },
                        clientCode: function() {
                            return 0;
                        },
                        branchName: function() {
                            return null;
                        }
                    }
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {
                    $scope.clientsData.push({
                        branchCode: result.branchCode,
                        branchId: 0,
                        branchUri: result.branchUri,
                        branchName: result.branchName
                    });
                });

            };


            $scope.aboutClient = function(client) {
                if (client.isAbout) {
                    client.isAbout = false;
                } else {
                    closeAllDivs();
                    client.isAbout = true;
                }
            };

            $scope.moduleSettingsClient = function(client) {
                $scope.gotClientModuleSettingsData = false;

                if (client.isModuleSettings) {
                    client.isModuleSettings = false;
                } else {
                    closeAllDivs();
                    var data = {
                        "branchName": client.branchName,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    }
                    $http.post('services/clients/particularClientDetail', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            client.isModuleSettings = true;
                            $scope.currentClient = client.branchName;
                            $scope.clientWebModulesData = data.webModules;
                            $scope.clientEmployeeModulesData = data.empModules;
                            if ($scope.clientWebModulesData.length > 0 || $scope.clientEmployeeModulesData.length > 0) {
                                $scope.gotClientModuleSettingsData = true;
                            } else {
                                $scope.gotClientModuleSettingsData = false;
                            }
                        }
                    }).error(function(data, status, headers, config) {
                        $scope.gotClientModuleSettingsData = false;
                    });


                }

            };

            $scope.accessChangeWeb = function(module) {
                if (module.isActive || module.isActive == true) {
                    module.isActive = false;
                } else {
                    module.isActive = true;
                }
            };

            $scope.accessChangeEmployee = function(module) {
                if (module.isActive) {
                    module.isActive = false;
                } else {
                    module.isActive = true;
                }
            };

            $scope.applyWebModuleChanges = function() {
                var webModules = [];
                angular.forEach($scope.clientWebModulesData, function(item) {
                    webModules.push({
                        moduleName: item.moduleName,
                        isActive: item.isActive
                    });
                });
                var data = {
                    branchName: $scope.currentClient,
                    modules: JSON.stringify(webModules),
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/clients/clientWebModulesSave/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        ngDialog.open({
                            template: 'Changes have been applied successfully',
                            plain: true
                        });
                    }

                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.applyEmployeeModuleChanges = function(module) {
                var empModules = [];
                angular.forEach($scope.clientEmployeeModulesData, function(item) {
                    empModules.push({
                        moduleName: item.moduleName,
                        isActive: item.isActive
                    });
                });

                var data = {
                    branchName: $scope.currentClient,
                    modules: JSON.stringify(empModules),
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/clients/clientEmployeeModulesSave/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        ngDialog.open({
                            template: 'Changes have been applied successfully',
                            plain: true
                        });
                    }

                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.addBranch = function(client, size, type) {
                $scope.isClient = false;
                $scope.isBranch = false;

                if (type == 'client') {
                    $scope.isClient = true;
                }

                if (type == 'branch') {
                    $scope.isBranch = true;
                }

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addNewClient.jsp',
                    controller: 'addNewClientCtrl',
                    size: size,
                    backdrop: 'static',
                    resolve: {
                        isClient: function() {
                            return $scope.isClient
                        },
                        isBranch: function() {
                            return $scope.isBranch
                        },
                        clientCode: function() {
                            return client.branchId;
                        },
                        branchName: function() {
                            return client.branchName;
                        }
                    }
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {
                    $scope.clientsData.push({
                        branchCode: result.branchCode,
                        branchId: 0,
                        branchUri: result.branchUri,
                        branchName: result.branchName
                    });
                });

            };
        }

    };

    var addNewClientCtrl = function($scope, $modal, $modalInstance, $state, $http, $timeout, ngDialog, isClient, isBranch, clientCode, branchName) {
        $scope.isClient = isClient;
        $scope.isBranch = isBranch;
        $scope.regexMin1to3Numbers = /^\d{1,3}$/;
        $scope.regexMin11to15Numbers = /^\d{11,15}$/;
        $scope.applicationSettingsArrays = {
            clustrings: ["Enable", "Disable"]
        };
        $scope.pickDropArray = ['Pickup', 'Drop', 'Both'];
        $scope.newClient = {};
        $scope.newClient.branchName = branchName;
        $scope.newClient.autoClustring = "Enable";
        $scope.escortNeededArray = [{
                text: 'None',
                value: 'None'
            },
            {
                text: 'Always',
                value: 'Always'
            },
            {
                text: 'When Female Pessenger Present',
                value: 'When Female Pessenger Present'
            },
            {
                text: 'For Last Drop and First Pickup Female Passenger',
                value: 'For Last Drop and First Pickup Female Passenger'
            }
        ]
        $scope.newClient.escortNeeded = {
            text: 'None',
            value: 'None'
        };
        $scope.managerApprovalArray = [{
            text: 'No',
            value: 'No'
        }, {
            text: 'Yes',
            value: 'Yes'
        }];
        $scope.newClient.managerApproval = {
            text: 'No',
            value: 'No'
        };
        $scope.driverImages = ['Yes', 'No'];
        $scope.driverNames = ['Yes', 'No'];
        $scope.driverMobileNumbers = ['Yes', 'No'];
        $scope.profilePics = ['Yes', 'No'];
        $scope.autoCallSmsDisables = ['Yes', 'No'];
        $scope.adhocTimerArray = ['Yes', 'No'];
        $scope.lastPasswordNotCurrents = ['Yes', 'No'];
        $scope.empSecondNormalPickups = ['Yes', 'No'];
        $scope.empSecondNormalDrops = ['Yes', 'No'];
        $scope.autoDropRosters = ['Yes', 'No'];
        $scope.panicAlertNeededs = ['Yes', 'No'];
        $scope.newClient.pickDrop = 'Both';
        $scope.newClient.driverImage = 'No';
        $scope.newClient.driverName = 'No';
        $scope.newClient.driverMobileNumber = 'No';
        $scope.newClient.profilePic = 'No';
        $scope.newClient.autoCallSmsDisable = 'No';
        $scope.newClient.adhocTimer = 'No';
        $scope.newClient.lastPasswordNotCurrent = 'No';
        $scope.newClient.empSecondNormalPickup = 'No';
        $scope.newClient.empSecondNormalDrop = 'No';
        $scope.newClient.autoDropRosters = 'No';
        $scope.newClient.panicAlertNeeded = 'No';

        if (isBranch == true) {
            $scope.newClient.branchCode = clientCode
        }

        $scope.openMap = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/addNewClientMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            //Here the VendorId will be given to the Vendor from backend


            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newClient.location = x;
                $scope.newClient.cords = result.cords;
            });
        };

        $scope.saveNewClient = function(newClient) {
            var dataObj = {
                autoClustering: newClient.autoClustring,
                branchName: newClient.branchName,
                branchCode: newClient.branchCode,
                address: newClient.location,
                latitudeLongitude: newClient.cords,
                stateName: newClient.state,
                branchUri: newClient.branchUri,
                emailId: newClient.emailId,
                branchDescription: newClient.description,
                etaSMS: newClient.etaSMS,
                employeeAddressgeoFenceArea: newClient.geoFenceArea,
                delayMessageTime: newClient.delayMessage,
                employeeWaitingTime: newClient.employeeWaitingTime,
                maxSpeed: newClient.maxSpeedArray,
                driverAutoCheckedoutTime: newClient.driverCheckedout,
                startTripGeoFenceAreaInMeter: newClient.autoTripStartGF,
                endTripGeoFenceAreaInMeter: newClient.autoTripEndGF,
                maxTravelTimeEmployeeWiseInMin: newClient.maxTravelTime,
                maxRadialDistanceEmployeeWiseInKm: newClient.maxRadialDistance,
                maxRouteLengthInKm: newClient.maxRouteLength,
                maxRouteDeviationInKm: newClient.maxRoutedeviation,
                clusterSize: newClient.clusterSize,
                transportContactNumberForMsg: newClient.transportContactNumber,
                emergencyContactNumber: newClient.emrgContactNumber,
                shiftTimePlusOneHourrAfterSMSContact: newClient.after13HrSMSContact,
                shiftTimePlusTwoHourrAfterSMSContact: newClient.after14HrSMSContact,
                feedBackEmailId: newClient.transportFeedbackEmailId,
                geoCodesForGeoFence: newClient.GeoCodeForOfficeGeoFence,
                mangerApprovalRequired: newClient.managerApproval.text,
                escortRequired: newClient.escortNeeded.text,
                userId: profileId,
                combinedFacility: combinedFacility
            }
            $http.post('services/clients/saveClient/', dataObj).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    ngDialog.open({
                        template: 'Client data added successfully',
                        plain: true
                    });

                    $timeout(function() {
                        $modalInstance.close(newClient);
                        ngDialog.close();
                    }, 3000);
                }
            }).
            error(function(data, status, headers, config) {
                $scope.gotEmployeeDetailsData = true;
            });
        };

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    var openClientaddressMapCtrl = function($scope, $modalInstance, $state, $http, $timeout) {

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    angular.module('efmfmApp').controller('clientSettingsCtrl', clientSettingsCtrl);
    angular.module('efmfmApp').controller('addNewClientCtrl', addNewClientCtrl);
    angular.module('efmfmApp').controller('openClientaddressMapCtrl', openClientaddressMapCtrl);
}());