/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       requestDetailsCtrl
@Modal Controllers      creatRequestMapCtrl
@template               partials/home.employeeRequestDetails.jsp
 
CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() { 
    var cabRequestCtrl = function($scope, $rootScope, $timeout, $http, $modal, $state, ngDialog, $filter, $confirm) {
        $scope.form = {};
        $scope.adhocTimePickerNew = 'No';
        $scope.tripTimes = [];
        $scope.selectedNewTripTime;
        $scope.needAdHoc = false;
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
        $scope.bothDropdown = false;
        $scope.adHoctime;
        $scope.saveIsClicked = false;
        var feeddate = new Date();
        $scope.feedbackMaxDate = feeddate.setDate(feeddate.getDate() - 30);
        $scope.feedbackMinDate = new Date();
        $scope.createNewRequest = {};
        $scope.newRequestByAdminAdhocManila = {};
        $scope.feedback = {};
        $scope.delegatedProject = {};
        $scope.complain = {};
        $scope.adhocChanged = true;
        $scope.branchCode = branchCode;
        $scope.userRole = userRole;
        $scope.multipleLocationButtonDisabled = false;
        $scope.tripTypeBoth = false;
        $scope.submitFlag = false;
        $scope.shiftTypeDisabled = true;
        localStorage.removeItem('areaId');
        var currentDateTime = new Date();
        currentDateTime.setHours(00, 00, 00);
        $scope.createNewPickupAdHocTime = currentDateTime;
        $scope.createNewDropAdHocTime = currentDateTime;
        $scope.createNewRequestForm = true;
        $scope.requestWithProject = requestWithProject;
        $scope.managerReqCreateProcess = managerReqCreateProcess;
        localStorage.clear();
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = array;
        localStorage.setItem("combinedFacilityIdAdhocRequest", $scope.facilityData);
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdAdhocRequest");
        $rootScope.combinedFacilityId = combinedFacilityId;
        $scope.facilityMultiDropdown = true;

        $scope.getTabInfo = function(facilityId, value) {
            localStorage.setItem("employeeRequestDetailsTab", value);
            if(value == 'bookACab' || value == 'createRequestManila' || value == 'createRequest'){
                $scope.facilityMultiDropdown = false;
                $scope.facilitySingleDropdown = true;
            }else{
                $scope.facilityMultiDropdown = true;
                $scope.facilitySingleDropdown = false;
            }
        }

        $scope.getFacilityDetails = function(value) {
            $rootScope.combinedFacilityId = value; 
            var tabInfo = localStorage.getItem("employeeRequestDetailsTab");
                switch (tabInfo) {
                    case 'bookingSchedule':
                        $scope.getTodayRequestDetails();
                        break;
                    case 'bookACab':
                        $scope.initialzeNewCustomTime();
                        break;
                    case 'rescheduleCab':
                        $scope.getRescheduleRequestDetails();
                        break;
                    case 'cancelCab':
                        $scope.getCancelRequestDetails();
                        break;
                    case 'bulkRequestDetails':
                        $scope.getBookingRequestDetails();
                        break;
                    case 'pendingApprovalRequests':
                        $scope.getPendingApprovalRequests();
                        break;
                    case 'delegatedSPOC':
                        $scope.getEmployeeDetails();
                        break;
                    case 'delegatedDetails':
                        $scope.getDelegatedUserDetails();
                        $scope.getEmployeeDetails();
                        break;
                    default:
                        $scope.getTodayRequestDetails();
                        break;
                }
       
        }
        localStorage.removeItem('AreaIdDetails');
        $scope.tripTypes = [{
                'value': 'BOTH',
                'text': 'BOTH'
            },
            {
                'value': 'PICKUP',
                'text': 'PICKUP'
            },
            {
                'value': 'DROP',
                'text': 'DROP'
            }
        ];
        $scope.shiftTypes = [{
                'value': 'NORMAL',
                'text': 'NORMAL'
            },
            {
                'value': 'ADHOC',
                'text': 'ADHOC'
            }
        ];
        $scope.shiftTypesNormal = [{
            'value': 'NORMAL',
            'text': 'NORMAL'
        }, ];
        $scope.requestTypes = [{
                'value': 'SELF',
                'text': 'SELF'
            },
            {
                'value': 'EMPLOYEE',
                'text': 'EMPLOYEE'
            },
            {
                'value': 'GUEST',
                'text': 'GUEST'
            }
        ];
        $scope.complaintTypes = [{
                'value': 'Cab Issues',
                'text': 'Cab Issues'
            },
            {
                'value': 'IVR Issue',
                'text': 'IVR Issue'
            },
            {
                'value': 'Login Issue',
                'text': 'Login Issue'
            },
            {
                'value': 'Others',
                'text': 'Others'
            }
        ];
        $scope.feedbackTypes = [{
                'value': 'Trip Feedback',
                'text': 'Trip Feedback'
            },
            {
                'value': 'Routing Feedback',
                'text': 'Routing Feedback'
            },
            {
                'value': 'Speed of Cab',
                'text': 'Speed of Cab'
            },
            {
                'value': 'Tracking Feedback',
                'text': 'Tracking Feedback'
            },
            {
                'value': 'Driver Feedback',
                'text': 'Driver Feedback'
            }
        ];
        $scope.requestTypeName = [{
                'value': 'SELF',
                'text': 'SELF'
            },
            {
                'value': 'GUEST',
                'text': 'GUEST'
            }
        ];
        $scope.cabTypes = [{
                'value': 'REGULAR',
                'text': 'REGULAR'
            },
            {
                'value': 'CAB HIRE',
                'text': 'CAB HIRE'
            },
        ];
        $scope.cabTripTypes = [{
                'value': 'PICKUP',
                'text': 'PICKUP'
            },
            {
                'value': 'DROP',
                'text': 'DROP'
            }
        ];
        $scope.areaCodes = [{
                'value': '91',
                'text': 'IND - 91'
            },
            {
                'value': '1',
                'text': 'USA - 1'
            },
            {
                'value': '44',
                'text': 'UK - 44'
            }
        ];
        $scope.genders = [{
                'value': 'Male',
                'text': 'Male'
            },
            {
                'value': 'Female',
                'text': 'Female'
            }
        ];
        $scope.paymentTypes = [{
                'value': 'CASH',
                'text': 'CASH'
            },
            {
                'value': 'CREDIT',
                'text': 'CREDIT'
            }
        ];
        $scope.requestMethod = [{
                value: '1',
                text: 'Multiple'
            },
            {
                value: '0',
                text: 'Single'
            }
        ];
        $scope.searchTypes = [{
            'value': 'PICKUP',
            'text': 'PICKUP'
        }, {
            'value': 'DROP',
            'text': 'DROP'
        }];


        $scope.tripTypeFilters = [{
                value: 'PICKUP',
                text: 'PICKUP'
            },
            {
                value: 'DROP',
                text: 'DROP'
            }
        ];
        $scope.editDelegateUser = function(project, index) {
            var parseUKDate = function(source, delimiter) {
                return new Date(source.split(delimiter).reverse().join(delimiter))
            };
            $scope.editProjectForm = index;

        }
        $scope.cancelEditDelegateUser = function(project) {
            $scope.editProjectForm = null;
        }
        $scope.saveEditDelegateUser = function(edited, projectDetail) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.data = {
                eFmFmClientProjectDetails: {
                    projectId: projectDetail.projectId,
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                reportingManagerUserId: projectDetail.reportingManagerUserId,
                delegatedUserId: edited.employee.userId,
                delegatedBy: profileId,
                combinedFacility: combinedFacilityId
            }

            $http.post('services/employee/modifyDeligatedUser/', $scope.data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "success") {
                        ngDialog.open({
                            template: 'Saved successfully',
                            plain: true
                        });
                        $scope.editProjectForm = null;
                    } else {
                        ngDialog.open({
                            template: data.status,
                            plain: true
                        });
                    }
                }
            }).
            error(function(data, status, headers, config) {

            });

        }
        $scope.disableDelegateProject = function(projectDetail, index) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $confirm({
                text: "Are you sure you want to Disable this Project?",
                title: 'Confirmation',
                ok: 'Yes',
                cancel: 'No'
            }).then(function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/projectDisableRemarkModal.jsp',
                    controller: 'projectDisableRemarkCtrl',
                    size: 'sm',
                    resolve: {
                        post: function() {
                            return projectDetail;
                        }
                    }
                });
                modalInstance.result.then(function(result) {
                    $scope.data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        userId: profileId,
                        projectId: projectDetail.projectId,
                        isActive: 'D',
                        remarks: result.remarks,
                        startDate: projectDetail.projectStartDate,
                        endDate: projectDetail.projectEndDate,
                        clientProjectId: projectDetail.clientProjectId,
                        employeeProjectName: projectDetail.projectName,
                        combinedFacility: combinedFacilityId
                    }
                    $http.post('services/employee/updatedAndDisableProejctDetails/', $scope.data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == "success") {
                                $scope.listofProjectIdDetails.splice(index, 1);
                                ngDialog.open({
                                    template: 'Disabled successfully',
                                    plain: true
                                });

                            } else {
                                ngDialog.open({
                                    template: data.status,
                                    plain: true
                                });
                            }
                        }
                    }).
                    error(function(data, status, headers, config) {});
                });


            });


        }
        $scope.getEmployeeDetails = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            var dataObj = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/employee/AllEmployeeDetails/', dataObj).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.employeeDetails = data;
                }
            }).
            error(function(data, status, headers, config) {});
        }
        $scope.delegateUsersTable = false;
        $scope.NodelegateUsers = false;
        $scope.getDelegatedUserDetails = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            var dataObj = {
                eFmFmClientProjectDetails: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                delegatedCall: "1",
                reportingManagerUserId: profileId,
                combinedFacility: combinedFacilityId
            }
            $http.post('services/employee/listofDeligatedUserDetailsByUserAndAdmin/', dataObj).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.length > 0) {
                        $scope.delegateUsers = data;
                        $scope.delegateUsersTable = true;
                        $scope.NodelegateUsers = false;
                    } else {
                        $scope.delegateUsersTable = false;
                        $scope.NodelegateUsers = true;
                        ngDialog.open({
                            template: 'No Result Found',
                            plain: true
                        });
                    }
                }

            }).
            error(function(data, status, headers, config) {});
        }
        $scope.addDelegatedProject = function(project, allocateProjectForm) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.projectList = [];
            angular.forEach(project.project, function(value, key) {
                $scope.projectList.push({
                    projectId: value
                })
            });
            var data = {
                eFmFmClientProjectDetails: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                },
                userId: profileId,
                delegatedUserId: project.employee.userId,
                ListOfProject: $scope.projectList,
                reportingManagerUserId: profileId,
                combinedFacility: combinedFacilityId
            };

            $http.post('services/employee/deligatedUserByReportingManager/', data).
            success(function(data, status, headers, config) {
                if (data.status == "success") {
                    ngDialog.open({
                        template: 'Saved successfully',
                        plain: true
                    });
                    $scope.allocateProject = {};
                    allocateProjectForm.$setPristine();
                } else {
                    ngDialog.open({
                        template: data.status,
                        plain: true
                    });
                }
            }).
            error(function(data, status, headers, config) {});

        }
        $scope.resetDelegatedProject = function(form) {
            $scope.delegatedProject = {};
            form.$setPristine();
        }
        $scope.setProjectName = function(value) {
            $scope.delegatedProject.projectName = {
                projectId: value
            }
        }
        var data = {
            efmFmUserMaster: {
                eFmFmClientBranchPO: {
                    branchId: branchId
                }
            },
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/user/listOfProjectId/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                $scope.listOfProjectId = data;
            }

        }).
        error(function(data, status, headers, config) {});

        var data = {
            branchId: branchId,
            userId: profileId,
            combinedFacility: combinedFacility
        };

        $http
            .post('services/user/branchdetail/', data)
            .success(
                function(data, status, headers, config) {
                    $scope.data = data;
                    if (data.status == "invalidRequest") {

                    } else {


                        $rootScope.locationVisible = data.locationVisible;
                        $scope.requestDateCutOff = data.requestDateCutOff;
                        $scope.requestCutOffNoOfDays = data.requestCutOffNoOfDays;
                        $scope.dateCutOff = data.earlyRequestDate;
                        $scope.datePickerEnable = false;
                        $scope.dataEnable = true;
                        $scope.daysRequest = data.daysRequest;
                        var dateCurrent = new Date();
                        $scope.setMinDateNew = dateCurrent.setDate(dateCurrent.getDate());
                        $scope.setmaxDate = data.requestToDateCutOff;
                        $rootScope.destinationPointLimit = data.destinationPointLimit;
                        $scope.locationVisible = data.locationVisible;

                        var todayDateValidation = new Date();
                        var lastDayCust = new Date(todayDateValidation.getFullYear(), todayDateValidation.getMonth() + 1, 0);
                        lastDayCust.setDate(lastDayCust.getDate() - data.earlyRequestDate);

                        if (lastDayCust <= todayDateValidation) {
                            if (data.occurrenceFlg == 'N') {
                                $scope.NextMonthNormal = true;
                                $scope.NextMonthOccurrence = false;
                                $scope.normalView = false;
                                $scope.lastDayCust = new Date(todayDateValidation.getFullYear(), todayDateValidation.getMonth() + 1, 0);
                                var lastDayCust = new Date(todayDateValidation.getFullYear(), todayDateValidation.getMonth() + 1, 0);
                                lastDayCust.setDate(lastDayCust.getDate() - data.earlyRequestDate);
                            }
                            if (data.occurrenceFlg == 'Y') {
                                $scope.NextMonthOccurrence = true;
                                $scope.NextMonthNormal = false;
                                $scope.normalView = false;

                            }
                        }

                        if (lastDayCust >= todayDateValidation) {

                            $scope.normalView = true;
                            $scope.NextMonthOccurrence = false;
                            $scope.NextMonthNormal = false;
                            $scope.lastDayCust = new Date(todayDateValidation.getFullYear(), todayDateValidation.getMonth() + 1, 0);
                            var lastDayCust = new Date(todayDateValidation.getFullYear(), todayDateValidation.getMonth() + 1, 0);
                        }

                        $scope.createNewRequestForm = true;
                        $scope.AllDays = true;
                        $scope.currentDate = new Date();
                        $scope.dynamicRequestData = [];

                        angular.forEach(String($scope.daysRequest), function(value, key) {

                            if (value == 1) {
                                var Sunday = "Sunday";
                                $scope.dynamicRequestData.push(Sunday);
                            }
                            if (value == 2) {
                                var Monday = "Monday";
                                $scope.dynamicRequestData.push(Monday);
                            }
                            if (value == 3) {
                                var Tuesday = "Tuesday";
                                $scope.dynamicRequestData.push(Tuesday);
                            }
                            if (value == 4) {
                                var Wednesday = "Wednesday";
                                $scope.dynamicRequestData.push(Wednesday);
                            }
                            if (value == 5) {
                                var Thursday = "Thursday";
                                $scope.dynamicRequestData.push(Thursday);
                            }
                            if (value == 6) {
                                var Friday = "Friday";
                                $scope.dynamicRequestData.push(Friday);
                            }
                            if (value == 7) {
                                var Saturday = "Saturday";
                                $scope.dynamicRequestData.push(Saturday);
                            }
                        });

                        $scope.dynamicRequest = $scope.dynamicRequestData.toString();


                        $scope.currentDatenew = angular.copy($scope.currentDate.setDate($scope.currentDate.getDate()));

                        var numberOfDaysToAdd = data.requestCutOffNoOfDays;
                        var dayCurrent = $scope.currentDate.getDay() + 1;


                        if (dayCurrent == 1) {
                            dayCurrent = 1;
                        } else if (dayCurrent == 2) {
                            dayCurrent = 2;
                        } else if (dayCurrent == 3) {
                            dayCurrent = 3;
                        } else if (dayCurrent == 4) {
                            dayCurrent = 4;
                        } else if (dayCurrent == 5) {
                            dayCurrent = 5;
                        } else if (dayCurrent == 6) {
                            dayCurrent = 6;
                        } else if (dayCurrent == 7) {
                            dayCurrent = 7;
                        }

                        var number = $scope.daysRequest,
                            output = [],
                            sNumber = number.toString();

                        angular.forEach(sNumber, function(value, key) {

                            var d = Number(value);

                            if (d === 1 && dayCurrent === 1) {

                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);
                            }
                            if (d === 2 && dayCurrent === 2) {
                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);
                            }
                            if (d === 3 && dayCurrent === 3) {
                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);
                            }
                            if (d === 4 && dayCurrent === 4) {
                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);
                            }
                            if (d === 5 && dayCurrent === 5) {
                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);

                            }
                            if (d === 6 && dayCurrent === 6) {

                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);

                            }
                            if (d === 7 && dayCurrent === 7) {
                                $scope.dataEnable = false;
                                $scope.datePickerEnable = true;
                                $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                                $scope.endDateVal = new Date($scope.lastDateValidation);
                            }
                        });

                        if ($scope.daysRequest == 0) {
                            $scope.AllDays = false;
                            $scope.datePickerEnable = true;
                            $scope.dataEnable = false;
                            $scope.lastDateValidation = $scope.currentDate.setDate($scope.currentDate.getDate() + numberOfDaysToAdd - 1);
                            $scope.endDateVal = new Date($scope.lastDateValidation);
                        }

                        if (data.monthOrDays == 'everymonthlastdate') {
                            var someDate = new Date();
                            $scope.minDateValid = new Date();
                            $scope.firstDay = new Date(someDate.getFullYear(), someDate.getMonth(), 1);
                            $scope.lastDay = new Date(someDate.getFullYear(), someDate.getMonth() + 1, 0);
                            $scope.lastDayNextMonth = new Date(someDate.getFullYear(), someDate.getMonth() + 2, 0);
                            var lastDayNextMonth = $scope.lastDayNextMonth.getDate();
                            var lastDay = new Date(someDate.getFullYear(), someDate.getMonth() + 1, 0);
                            var x = data.earlyRequestDate;
                            lastDay.setDate(lastDay.getDate() - x + 1);
                            var today = new Date();
                            var dateFormat = lastDay <= someDate;
                            var todayMonth = today.getMonth() + 1;
                            var lastDayMonth = $scope.lastDay.getMonth() + 1;
                            var monthValidation = todayMonth == lastDayMonth;
                            if (dateFormat && monthValidation) {
                                $scope.lastDay = $scope.lastDay.setDate($scope.lastDay.getDate() + lastDayNextMonth);
                            } else {
                                $scope.lastDay = $scope.lastDay.setDate($scope.lastDay.getDate());
                            }
                        }

                    }
                }).error(
                function(data, status, headers, config) {});

        $scope.allZonesData = [];

        $rootScope.locationLoad = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                isActive: 'A',
                combinedFacility: combinedFacilityId
            };
            $http.post('services/employee/allActiveLocation/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.length > 0) {
                        $scope.allZonesData = data.reverse();
                        $rootScope.homeLocationAreaId = $scope.allZonesData[0].areaId;
                    }
                }

            }).
            error(function(data, status, headers, config) {});
        }
        $rootScope.locationLoad();
        $scope.addMapDestination = function(index, origin) {
            $scope.selectedIndex = 0;
            var location = officeLocation;
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/addMapDestinationModal.jsp',
                controller: 'mapDestinationCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    index: function() {
                        return index;
                    },
                    origin: function() {
                        return location;
                    }

                }
            });

            modalInstance.result.then(function(result) {

                var indexLast = _.findLastIndex($scope.allZonesData, {
                    areaName: result.areaName
                });

                var multipleLocation = $scope.allZonesData[indexLast];
                multipleLocation = {
                    'destination': multipleLocation
                };
                $scope.destinationPoints[$scope.selectedIndex] = multipleLocation;
            })
        };

        $scope.addMultipleLocation = function() {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/addMultipleLocation.jsp',
                controller: 'addMultipleLocationCtrl',
                backdrop: 'static'

            });

            modalInstance.result.then(function(result) {
                $rootScope.result = result;
            });
        }
        $scope.createfeedback = function(feedback, Type) {
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
                return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
            };

            var data = {
                employeeComments: feedback.comment,
                userId: profileId,
                toDate: convertDateUTC(feedback.shiftDate),
                tripType: feedback.tripType.value,
                time: feedback.shiftTime.shiftTime,
                combinedFacility: combinedFacility
            }
            if (Type == "feedback") {
                data.alertType = Type;
                data.driverRaiting = feedback.rateit;
                data.alertTitle = feedback.feedbackType.value;
            } else {
                data.alertType = Type;
                data.alertTitle = feedback.complainType.value;
            }


            $http.post('services/alert/employeeDeviceAlert/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == 'failed') {
                        if (Type == "feedback") {
                            ngDialog.open({
                                template: 'Your Feedback is Failed',
                                plain: true
                            });
                        } else {
                            ngDialog.open({
                                template: 'Your Complain is Failed',
                                plain: true
                            });
                        }

                    } else if (data.status == 'success') {
                        $scope.feedback = {};
                        $scope.complain = {};
                        $scope.form.feedbackForm.$setPristine();
                        $scope.form.complainForm.$setPristine();
                        if (Type == "feedback") {
                            ngDialog.open({
                                template: 'Your Feedback has been created successfully',
                                plain: true
                            });
                        } else {
                            ngDialog.open({
                                template: 'Your Complain has been created successfully',
                                plain: true
                            });
                        }
                        $(".creatNewReqButton").removeClass("disabled");
                        $scope.initialzeNewCustomTime();
                        $scope.initialzeNewCustomTimeByAdmin();
                    } else {
                        var statusMessage = data.status;

                        ngDialog.open({
                            template: statusMessage,
                            plain: true
                        });
                    }
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });


        }
        $scope.addMultipleLocationAdhoc = function() {
            if ($scope.multipleRequestRawData) {
                var data = {
                    status: true,
                    data: $scope.multipleRequestRawData
                }
            } else {
                var data = {
                    status: false
                }
            }
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/addMultipleLocationAdhoc.jsp',
                controller: 'addMultipleRequestCreateCtrl',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    rawData: function() {
                        return data;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                if (result) {
                    $scope.multipleRequestData = result.modified;
                    $scope.multipleRequestRawData = result.rawData;
                    $scope.submitFlag = false;
                } else {
                    $scope.submitFlag = true;
                }

            });
        }

        // Button Click for show multiple Destination

        $scope.destinationLocation = false;

        $scope.showMultipleDestination = function() {
            $scope.destinationLocation = true;
        };


        if (adhocTimePicker == 'Yes') {
            $scope.adhocShiftTimeShow = true;
            $scope.normalShiftTimeShow = false;
        } else {
            $scope.adhocShiftTimeShow = false;
            $scope.normalShiftTimeShow = true;
        }

        $scope.newShiftTypeSelected;
        var newShiftTimeSelected;
        $scope.newShiftIdSelected;
        var newShiftTimeType;
        var newShiftTimeSelectedTime;
        $scope.newShiftTimeSelectedTime;
        var newShiftTimeSelectedId;
        $scope.newShiftTimeSelectedId;
        $scope.setMinDate = new Date();
        $scope.isShiftTimeDisable = true;
        $scope.isFromDateEntered = true;
        $scope.isTripTypeSelected = true;
        $scope.isLocationEntered = false;
        $scope.isLocationEntered2 = false;
        $scope.validFormFlag = true;
        $scope.createRequestRole_ADMIN = true;
        $scope.createRequestRole_EMPLOYEE = false;
        $scope.createRequestRole_GUEST = false;
        $scope.createRequestIsClicked = false;
        $scope.regExName = /^[A-Za-z]+$/;
        $scope.IntegerNumber = /^[0-9,]*$/;
        $scope.NoSpecialCharacters = /^[a-zA-Z0-9]*$/;
        $scope.requestFor;
        $scope.is24hrRequest = true;
        $scope.newRequest = [];
        $scope.singleRequestMethod = false;
        $scope.multipleRequestBtn = false;
        $scope.$on('$viewContentLoaded', function() {
            $scope.adHoctime = $scope.initializeTime();
            $scope.initialzeNewCustomTimeByAdmin();
            $scope.getShiftTime('DROP');
        });



        $scope.previewLocation = function() {

            $scope.destinationArray = [];
            angular.forEach($rootScope.destinationMultipleLocation, function(item) {
                if (_.isEmpty(item.destination)) {
                    return true;
                } else {
                    $scope.destinationArray.push(item.destination);
                }
            });

            var duplicateArray = _.pluck($scope.destinationArray, 'areaId');
            var sorted_arr = duplicateArray.slice().sort();
            var results = [];
            for (var i = 0; i < duplicateArray.length - 1; i++) {
                if (sorted_arr[i + 1] == sorted_arr[i]) {
                    results.push(sorted_arr[i]);
                }
            }

            if ($rootScope.destinationValue == undefined || $rootScope.destinationValue.length == 0) {
                ngDialog.open({
                    template: 'Kindly choose origin and destination',
                    plain: true
                });
                return false;
            }

            var previewData = _.pluck($scope.destinationArray, 'locationLatLng');
            var splitArray = [];
            for (var i = 0; i < previewData.length; i++) {
                var lanLong = previewData[i].split(",");
                var lan = lanLong[0];
                var lon = lanLong[1];
                splitArray.push(lan + ',' + lon + '|');
            }
            var route = {};
            route.waypoints = splitArray.join("");
            route.baseLatLong = $rootScope.destinationValue[0].locationLatLng;
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/routeMapView.jsp',
                controller: 'multiLocationMapViewCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    waypoints: function() {
                        return route.waypoints;
                    },
                    baseLatLong: function() {
                        return route.baseLatLong;
                    }
                }
            });

        }

        $scope.getShiftTime = function(tripType) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.tripTimeData = [];
            $scope.tripTimes = [];
            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        userId: profileId
                    }
                },
                tripType: tripType,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/tripshiftime/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.tripTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
                    angular.forEach($scope.tripTimeData, function(item) {
                        $scope.tripTimes.push(item.shiftTime);
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        };
        $scope.getPickUpShiftTime = function(tripType) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.pickupTripTimeData = [];
            $scope.tripTimes = [];
            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        userId: profileId
                    }
                },
                tripType: tripType,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/tripshiftime/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.pickupTripTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });

                    angular.forEach($scope.pickupTripTimeData, function(item) {
                        $scope.tripTimes.push(item.shiftTime);
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        };
        $scope.getDropShiftTime = function(tripType) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.DropTripTimeData = [];
            $scope.tripTimes = [];
            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        userId: profileId
                    }
                },
                tripType: tripType,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/tripshiftime/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.DropTripTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
                    angular.forEach($scope.DropTripTimeData, function(item) {
                        $scope.tripTimes.push(item.shiftTime);
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        };
        $scope.empRequestDetailsData = [];
        $scope.fetching = false;
        $scope.disabled = false;
        //FUNCTION : TODAY'S REQUEST LIST
        $scope.getTodayRequestDetails = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.getShiftTime('DROP');
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                efmFmUserMaster: {
                    userId: profileId
                },
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/employeetodayWebRequest/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {

                    $scope.multipleLocation = [];
                    $scope.normalLocation = [];
                    $scope.empRequestDetailsData = data.requests;
                    angular.forEach($scope.empRequestDetailsData, function(item) {
                        if (item.locationFlg == 'M') {
                            $scope.multipleLocation.push(item);
                        }
                        if (item.locationFlg == 'N') {
                            $scope.normalLocation.push(item);
                        }
                        item.needReshedule = false;
                        item.cancel = false;
                    });
                    $scope.requestDetailsMultipleRequest = _.where($scope.multipleLocation, {
                        multipleLocationFlg: "M"
                    });
                    $scope.requestDetailsNormalRequest = _.where($scope.normalLocation, {
                        multipleLocationFlg: "N"
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };
        $scope.getTodayRequestDetails();

        $scope.searchTripTypeBookingSchedule = function(searchType, empRequestDetailsData) {
            $scope.bookingRequestDetailsShow = false;
            if (searchType == 'PICKUP') {
                $scope.pickupRequests = _.where(empRequestDetailsData, {
                    tripType: "PICKUP"
                });
                if ($scope.pickupRequests.length == 0) {
                    $scope.bookingRequestDetailsShow = true;
                } else {
                    $scope.bookingRequestDetailsShow = false;
                }
            } else {
                $scope.dropRequests = _.where(empRequestDetailsData, {
                    tripType: "DROP"
                });
                if ($scope.dropRequests.length == 0) {
                    $scope.bookingRequestDetailsShow = true;
                } else {
                    $scope.bookingRequestDetailsShow = false;
                }
            }
        }

        // Search Reschedule Cab - Trip Type

        $scope.searchTripTypeRescheduleCab = function(searchType, empRequestDetailsData) {
            $scope.bookingRequestDetailsShow = false;
            if (searchType == 'PICKUP') {
                $scope.pickupRequests = _.where(empRequestDetailsData, {
                    tripType: "PICKUP"
                });
                if ($scope.pickupRequests.length == 0) {
                    $scope.bookingRequestDetailsShow = true;
                } else {
                    $scope.bookingRequestDetailsShow = false;
                }
            } else {
                $scope.dropRequests = _.where(empRequestDetailsData, {
                    tripType: "DROP"
                });
                if ($scope.dropRequests.length == 0) {
                    $scope.bookingRequestDetailsShow = true;
                } else {
                    $scope.bookingRequestDetailsShow = false;
                }
            }
        }

        // Search Reschedule Cab - Trip Type

        $scope.searchTripTypeCancelCab = function(searchType, empRequestDetailsData) {
            $scope.bookingRequestDetailsShow = false;
            if (searchType == 'PICKUP') {
                $scope.pickupRequests = _.where(empRequestDetailsData, {
                    tripType: "PICKUP"
                });
                if ($scope.pickupRequests.length == 0) {
                    $scope.bookingRequestDetailsShow = true;
                } else {
                    $scope.bookingRequestDetailsShow = false;
                }
            } else {
                $scope.dropRequests = _.where(empRequestDetailsData, {
                    tripType: "DROP"
                });
                if ($scope.dropRequests.length == 0) {
                    $scope.bookingRequestDetailsShow = true;
                } else {
                    $scope.bookingRequestDetailsShow = false;
                }
            }
        }

        //Map View

        $scope.multipleLocationMapView = function(route) {
            var baseLatLong = route.employeeWaypoints.wayPointsList.split('|');

            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/routeMapView.jsp',
                controller: 'multiLocationMapViewCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    waypoints: function() {
                        return route.employeeWaypoints.wayPointsList;
                    },
                    baseLatLong: function() {
                        return baseLatLong[0];
                    }
                }
            });
        }


        $scope.getLocationDetails = function(post) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/employeeWayPointsBookingSchedule.jsp',
                controller: 'employeeWayPointsCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    employeeWayPointsDetails: function() {
                        return post.employeeWaypoints.wayPoints;
                    },
                    post: function() {
                        return post;
                    }
                }
            });
        }

        $scope.empRequestDetailsDataReshedule = [];
        $scope.disabled1 = false;
        //FUNCTION : RE-SCHEDULE REQUEST LIST
        $scope.getRescheduleRequestDetails = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.createRequestIsClicked = false;
            $scope.saveIsClicked = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                efmFmUserMaster: {
                    userId: profileId
                },
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/requestsforWebreshedule/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.multipleLocation = [];
                    $scope.normalLocation = [];
                    $scope.empRequestDetailsDataReshedule = data.requests;
                    $scope.tripTimeData = data.shifts;
                    angular.forEach($scope.empRequestDetailsDataReshedule, function(item) {
                        if (item.locationFlg == 'M') {
                            $scope.multipleLocation.push({
                                "multipleLocationFlg": item.locationFlg
                            });
                        }
                        if (item.locationFlg == 'N') {
                            $scope.normalLocation.push({
                                "multipleLocationFlg": item.locationFlg
                            });
                        }
                        item.needReshedule = false;
                        item.cancel = false;
                    });
                    $scope.requestDetailsMultipleRequest = _.where($scope.multipleLocation, {
                        multipleLocationFlg: "M"
                    });
                    $scope.requestDetailsNormalRequest = _.where($scope.normalLocation, {
                        multipleLocationFlg: "N"
                    });
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };
        $scope.empRequestDetailsDataCancel = [];
        //FUNCTION : CANCEL REQUEST LIST
        $scope.getCancelRequestDetails = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.createRequestIsClicked = false;
            var data = {
                eFmFmEmployeeRequestMaster: {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    }
                },
                efmFmUserMaster: {
                    userId: profileId
                },
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/requestsforWebcancel/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.multipleLocation = [];
                    $scope.normalLocation = [];
                    $scope.empRequestDetailsDataCancel = data.requests;
                    $scope.tripTimeData = data.shifts;
                    angular.forEach($scope.empRequestDetailsDataCancel, function(item) {
                        if (item.locationFlg == 'M') {
                            $scope.multipleLocation.push({
                                "multipleLocationFlg": item.locationFlg
                            });
                        }
                        if (item.locationFlg == 'N') {
                            $scope.normalLocation.push({
                                "multipleLocationFlg": item.locationFlg
                            });
                        }
                        item.needReshedule = false;
                        item.cancel = false;
                    });
                    $scope.requestDetailsMultipleRequest = _.where($scope.multipleLocation, {
                        multipleLocationFlg: "M"
                    });
                    $scope.requestDetailsNormalRequest = _.where($scope.normalLocation, {
                        multipleLocationFlg: "N"
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.requestsData = [];

        $scope.getBookingRequestDetails = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/user/employeeRequestDetails', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.requestsData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error    
            });
        };

        $scope.disableRequest = function(device, index) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $confirm({
                    text: "Are you sure you want to Disable this request?",
                    title: 'Disable Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                })
                .then(
                    function() {
                        var data = {
                            efmFmUserMaster: {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                }
                            },
                            tripId: device.tripId,
                            status: 'N',
                            readFlg: 'N',
                            userId: profileId,
                            combinedFacility: combinedFacilityId
                        };
                        $http
                            .post(
                                'services/user/updaterequestdetail/',
                                data)
                            .success(
                                function(data, status,
                                    headers, config) {
                                    if (data.status != "invalidRequest") {
                                        device.status = 'N'
                                        ngDialog
                                            .open({
                                                template: 'Request Disable Successfully',
                                                plain: true
                                            });
                                        $scope.requestsData.splice(index, 1);
                                    }
                                    //                        $scope.showalertMessage("Request Disable Successfully", "");
                                }).error(
                                function(data, status,
                                    headers, config) {
                                    // log error
                                });
                    })
        };


        $scope.getPendingApprovalRequests = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }
            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post(
                    'services/trip/pendingApprovalRequest/',
                    data)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.getPendingApprovalRequestsData = data;
                        }
                    }).error(
                    function(data, status,
                        headers, config) {
                        // log error
                    });
        }

        $scope.approvePendingRequest = function(request, index) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }
            var data = {
                branchId: branchId,
                userId: profileId,
                requestId: request.requestId,
                requestRemarks: "No",
                reqApprovalStatus: "Y",
                multipleRequestId: 0,
                combinedFacility: combinedFacilityId
            };
            $http.post(
                    'services/trip/approveAndRejectRequest/',
                    data)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template: 'Request Approved Successfully',
                                plain: true
                            });
                            $scope.getPendingApprovalRequestsData.splice(index, 1);
                        }

                    }).error(
                    function(data, status,
                        headers, config) {
                        // log error
                    });
        }

        $scope.checkAll = function() {
            angular.forEach($scope.getPendingApprovalRequestsData, function(value, key) {
                value["status"] = true;
            });
        }

        $scope.uncheckAll = function() {
            angular.forEach($scope.getPendingApprovalRequestsData, function(value, key) {
                value["status"] = false;
            });
        }


        $scope.approveAllPendingRequests = function(getPendingApprovalRequestsData, data) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            var requestSelectedId = _.where(getPendingApprovalRequestsData, {
                status: true
            });
            var requestId = String(_.pluck(requestSelectedId, 'requestId'));

            if (requestId == "") {
                ngDialog.open({
                    template: 'Kindly allocate route to request',
                    plain: true
                });
                return false;
            }

            var data = {
                branchId: branchId,
                userId: profileId,
                requestRemarks: "No",
                reqApprovalStatus: "Y",
                multipleRequestId: requestId,
                combinedFacility: combinedFacilityId
            };
            $http.post(
                    'services/trip/approveAndRejectRequest/',
                    data)
                .success(
                    function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template: 'Selected Request are approved Successfully',
                                plain: true
                            });

                            angular.forEach(_.pluck(routeSelectedId, 'requestId'), function(value, key) {
                                $scope.getPendingApprovalRequestsData = _.without($scope.getPendingApprovalRequestsData, _.findWhere($scope.getPendingApprovalRequestsData, {
                                    requestId: value
                                }));
                            });
                        }

                    }).error(
                    function(data, status, headers, config) {
                        // log error
                    });

        }

        $scope.rejectPendingRequest = function(request, index) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }


            $confirm({
                    text: "Are you sure want to remove this Request?",
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                })
                .then(function() {
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/supervisorRemarks.jsp',
                        controller: 'rejectPendingRequestCtrl',
                        size: 'sm',
                        backdrop: 'static',
                        resolve: {
                            request: function() {
                                return request;
                            }
                        }
                    });
                    modalInstance.result.then(function(remarks) {
                        var data = {
                            branchId: branchId,
                            userId: profileId,
                            requestId: request.requestId,
                            requestRemarks: remarks.remarks,
                            reqApprovalStatus: "R",
                            multipleRequestId: 0,
                            combinedFacility: combinedFacilityId
                        };
                        $http.post(
                                'services/trip/approveAndRejectRequest/',
                                data)
                            .success(
                                function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        ngDialog.open({
                                            template: 'Request Rejected Successfully',
                                            plain: true
                                        });
                                        $scope.getPendingApprovalRequestsData.splice(index, 1);
                                    }

                                }).error(
                                function(data, status,
                                    headers, config) {
                                    // log error
                                });
                    });
                });
        }



        $scope.getAdhocTravelRequestDesk = function() {
            if (userRole == 'webuser') {
                $scope.adhocTravelRequestDesk = false;
                $confirm({
                        text: "This option is only available for business trips and visitors/guests. If you are creating a request for the same, please press YES button.",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $scope.adhocTravelRequestDesk = true;
                    });
            }

        }

        // Normal Employee Request Create

        var data = {
            efmFmUserMaster: {
                eFmFmClientBranchPO: {
                    branchId: branchId
                }
            },
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/user/listOfProjectId', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(value, key) {
                    value.ticked = false;
                    value.name = value.projectName;
                });
                $scope.listOfProjectName = data;
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        var data = {
            efmFmUserMaster: {
                eFmFmClientBranchPO: {
                    branchId: branchId
                }
            },
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/user/listOfProjectId', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(value, key) {
                    value.ticked = false;
                    value.name = value.projectId;
                });
                $scope.listOfProjectId = data;
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        // Adhoc Request Create

        var data = {
            eFmFmClientBranchPO: {
                branchId: branchId
            },
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/user/listOfProjectIdByClient', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(value, key) {
                    value.ticked = false;
                    value.name = value.projectName;
                });
                $scope.listOfProjectNameAdhoc = data;
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        var data = {
            eFmFmClientBranchPO: {
                branchId: branchId
            },
            userId: profileId,
            combinedFacility: combinedFacility
        };
        $http.post('services/user/listOfProjectIdByClient', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                angular.forEach(data, function(value, key) {
                    value.ticked = false;
                    value.name = value.projectId;
                });
                $scope.listOfProjectIdAdhoc = data;
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        // Normal Employee Request

        $scope.setProjectNameChange = function(value) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            if (value == 'projectName') {
                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/user/listOfProjectId', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        angular.forEach(data, function(value, key) {
                            value.ticked = false;
                            value.name = value.projectName;
                        });
                        $scope.listOfProjectName = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            } else if (value == 'projectId') {
                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/user/listOfProjectId', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        angular.forEach(data, function(value, key) {
                            value.ticked = false;
                            value.name = value.ClientprojectId;
                        });
                        $scope.listOfProjectId = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

        }



        // Adhoc Employee Request Create

        $scope.setProjectNameChangeAdhoc = function(value) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            if (value == 'projectName') {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/user/listOfProjectIdByClient', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        angular.forEach(data, function(value, key) {
                            value.ticked = false;
                            value.name = value.projectName;
                        });
                        $scope.listOfProjectNameAdhoc = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            } else if (value == 'projectId') {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/user/listOfProjectIdByClient', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        angular.forEach(data, function(value, key) {
                            value.ticked = false;
                            value.name = value.ClientprojectId;
                        });
                        $scope.listOfProjectIdAdhoc = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

        }



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
            return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
        };

        //Convert to mm-dd-yyyy
        var dateFormatConverter = function(x) {
            var date = x.split('-');
            return date[1] + '-' + date[0] + '-' + date[2];
        };

        //Convert to hh:mm
        var convertToTime = function(newdate) {
            d = new Date(newdate);
            hr = d.getHours();
            min = d.getMinutes();
            if (hr < 10) {
                hr = '0' + hr;
            }
            if (min < 10) {
                min = '0' + min;
            }
            return hr + ":" + min;
        };

        $scope.initializeTime = function() {
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            return d;
        }

        $scope.openFromDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'fromDate': true
                };
            }, 50);
        };

        $scope.openEndDateCal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'endDate': true
                };
            }, 50);
        };

        $scope.setTimeValues = function(request, createNewPickupAdHocTime, createNewDropAdHocTime) {
            $scope.multipleLocationButtonDisabled = true;
            if (request.tripType.text == "BOTH" && request.shiftType.text == "NORMAL") {
                if (request.createNewPickupTime == undefined || request.createNewDroptripTime == undefined) {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "BOTH" && request.shiftType.text == "ADHOC") {
                if (createNewPickupAdHocTime == undefined || createNewDropAdHocTime == undefined || createNewPickupAdHocTime.getHours() == '0' || createNewDropAdHocTime.getHours() == '0') {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "NORMAL") {
                if (request.createNewPickupTime == undefined) {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "ADHOC") {
                if (createNewPickupAdHocTime == undefined || createNewPickupAdHocTime.getHours() == '0') {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "NORMAL") {
                if (request.createNewDroptripTime == undefined) {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "ADHOC") {
                if (request.createNewDropAdHocTime == undefined || createNewDropAdHocTime.getHours() == '0') {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            }
        }

        $scope.setShiftType = function(request) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            if (request.tripType == undefined) {
                $scope.shiftTypeDisabled = true;
            } else {
                $scope.shiftTypeDisabled = false;
            }
            // Drop Shift Time
            var data = {
                branchId: branchId,
                tripType: "DROP",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/shiftTimeByTripType/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.tripTimeDataForDrop = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

            // Pickup Shift Time

            var data = {
                branchId: branchId,
                tripType: "PICKUP",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };

            $http.post('services/trip/shiftTimeByTripType/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.tripTimeDataForPickup = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

            if (request.tripType.text == "BOTH" && request.shiftType.text == "NORMAL") {
                $scope.pickupNormalTimeShow = true;
                $scope.dropNormalTimeShow = true;
                $scope.pickupAdhocTimeShow = false;
                $scope.dropAdhocTimeShow = false;
            } else if (request.tripType.text == "BOTH" && request.shiftType.text == "ADHOC") {
                $scope.pickupNormalTimeShow = false;
                $scope.dropNormalTimeShow = false;
                $scope.pickupAdhocTimeShow = true;
                $scope.dropAdhocTimeShow = true;
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "NORMAL") {
                $scope.pickupNormalTimeShow = true;
                $scope.dropNormalTimeShow = false;
                $scope.pickupAdhocTimeShow = false;
                $scope.dropAdhocTimeShow = false;
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "ADHOC") {
                $scope.pickupNormalTimeShow = false;
                $scope.dropNormalTimeShow = false;
                $scope.pickupAdhocTimeShow = true;
                $scope.dropAdhocTimeShow = false;
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "NORMAL") {
                $scope.pickupNormalTimeShow = false;
                $scope.dropNormalTimeShow = true;
                $scope.pickupAdhocTimeShow = false;
                $scope.dropAdhocTimeShow = false;
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "ADHOC") {
                $scope.pickupNormalTimeShow = false;
                $scope.dropNormalTimeShow = false;
                $scope.pickupAdhocTimeShow = false;
                $scope.dropAdhocTimeShow = true;
            }


            if (request.tripType.text == "BOTH" && request.shiftType.text == "NORMAL") {
                if (request.createNewPickupTime == undefined || request.createNewDroptripTime == undefined) {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "BOTH" && request.shiftType.text == "ADHOC") {
                if (createNewPickupAdHocTime == undefined || createNewDropAdHocTime == undefined || createNewPickupAdHocTime.getHours() == '0' || createNewDropAdHocTime.getHours() == '0') {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "NORMAL") {
                if (request.createNewPickupTime == undefined) {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "ADHOC") {
                if (createNewPickupAdHocTime == undefined || createNewPickupAdHocTime.getHours() == '0') {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "NORMAL") {
                if (request.createNewDroptripTime == undefined) {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "ADHOC") {
                if (request.createNewDropAdHocTime == undefined || createNewDropAdHocTime.getHours() == '0') {
                    $scope.createNewRequestForm = true;
                } else {
                    $scope.createNewRequestForm = false;
                }
            }

        }

        $scope.shiftTimeByTripType = function(request) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            if (request) {
                $scope.multipleLocationButtonDisabled = true;
                if (request.tripType.value == "BOTH") {
                    $scope.bothDropdown = true;
                    var data = {
                        branchId: branchId,
                        tripType: "DROP",
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };

                    $http.post('services/trip/shiftTimeByTripType/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.tripTimeDataForDrop = data;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                    var data1 = {
                        branchId: branchId,
                        tripType: "PICKUP",
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };

                    $http.post('services/trip/shiftTimeByTripType/', data1).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.tripTimeDataForPickup = data;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else {
                    $scope.bothDropdown = false;
                    var data = {
                        branchId: branchId,
                        tripType: request.tripType.value,
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };

                    $http.post('services/trip/shiftTimeByTripType/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.tripTimeDataReschedule = data;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }

            } else {}
        };

        $scope.reSheduleRequest = function(request) {
            $scope.tripTimeData = request.tripTime;
            var data = {
                branchId: branchId,
                tripType: request.tripType,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };

            $http.post('services/trip/shiftTimeByTripType/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.tripTimeDataReschedule = data;
                    $scope.newTripTime = [];
                    $confirm({
                            text: 'Are you sure you want to Reschedule Cab?',
                            title: 'Confirmation',
                            ok: 'Yes',
                            cancel: 'No'
                        })
                        .then(function() {
                            $scope.fromDate = new Date(dateFormatConverter(request.requestDate));
                            if (request.needReshedule) {
                                request.needReshedule = false;
                            } else {
                                $scope.saveIsClicked = true;
                                var i = $scope.tripTimes.indexOf(request.tripTime);
                                request.needReshedule = true;
                                $scope.newTripTime = _.where($scope.tripTimeDataReschedule, {
                                    shiftTime: request.tripTime
                                })[0];
                            }
                        });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.requestMethodChecker = function(request) {
            if (request) {
                if (request.value == "1") {
                    $scope.singleRequestMethod = false;
                    $scope.multipleRequestBtn = true;
                    $scope.submitFlag = true;
                } else {
                    $scope.singleRequestMethod = true;
                    $scope.multipleRequestBtn = false;
                    $scope.submitFlag = false;
                }
            } else {
                $scope.singleRequestMethod = false;
                $scope.multipleRequestBtn = false;
            }
        }

        $scope.validateRe_requestTime = function(requestTime, fromDate, tripType) {
            var RequestedScheduleTime;
            var timeSelectedArray;
            var requestedDateInMS;
            var timeDiffInMins;
            var selectedTimeInMinutes;
            var validTimeHoursStarts;
            var seledtedFromDate = new Date(fromDate);

            var todaysDate = new Date();
            var todayInMS = todaysDate.getTime();

            var today = convertDateUTC(todaysDate);
            var requested = convertDateUTC(seledtedFromDate);

            if (tripType == 'DROP') {
                validTimeHoursStarts = 2 + todaysDate.getHours();
            }
            if (tripType == 'PICKUP') {
                validTimeHoursStarts = 6 + todaysDate.getHours();
            }

            if (!$scope.needAdHoc) {
                timeSelectedArray = requestTime.split(':');
                selectedTimeInMinutes = parseInt(timeSelectedArray[0] * 60) + parseInt(timeSelectedArray[1]);
                seledtedFromDate.setHours(timeSelectedArray[0]);
                seledtedFromDate.setMinutes(timeSelectedArray[1]);
                requestedDateInMS = seledtedFromDate.getTime();
            } else if ($scope.needAdHoc) {
                var timePicker = new Date(requestTime)
                var hh = timePicker.getHours();
                var min = timePicker.getMinutes();
                selectedTimeInMinutes = hh * 60 + min;
                seledtedFromDate.setHours(hh);
                seledtedFromDate.setMinutes(min);
                requestedDateInMS = seledtedFromDate.getTime();
            }

            timeDiffInMins = Math.floor((requestedDateInMS - todayInMS) / 1000 / 60);

            //Check the User Role
            if ($scope.adminRole) {
                //Check if its Request Date is equals to Todays Date              
                if (today == requested && (requestedDateInMS < todayInMS || validTimeHoursStarts > 24)) {
                    ngDialog.open({
                        template: 'This is not the Valid Time. Please Change the Time or the Date',
                        plain: true
                    });
                    return false;
                } else {
                    if (tripType == 'PICKUP' && timeDiffInMins < 5) {
                        ngDialog.open({
                            template: 'Please select PickUp time 6 hours from now.',
                            plain: true
                        });
                    }
                    if (tripType == 'DROP' && timeDiffInMins < 5) {
                        ngDialog.open({
                            template: 'Please select Drop time 2 hours from now',
                            plain: true
                        });
                        return false;
                    }
                }
                return true;
            } else if (!$scope.adminRole && $scope.needAdHoc) {
                if (selectedTimeInMinutes >= 180 && selectedTimeInMinutes <= 300) {
                    if (today == requested && (requestedDateInMS < todayInMS || validTimeHoursStarts > 24)) {

                        ngDialog.open({
                            template: 'This is not the Valid Time. Please Change the Time or the Date.',
                            plain: true
                        });
                        return false;
                    } else {
                        if (tripType == 'PICKUP' && timeDiffInMins < 360) {

                            ngDialog.open({
                                template: 'Please select PickUp time 6 hours from now.',
                                plain: true
                            });
                            return false;
                        }
                        if (tripType == 'DROP' && timeDiffInMins < 120) {

                            ngDialog.open({
                                template: 'Please select Drop time 2 hours from now.',
                                plain: true
                            });
                            return false;
                        }
                    }

                }
                return true;
            } else if (!$scope.adminRole && !$scope.needAdHoc) {
                if ((today == requested && requestedDateInMS < todayInMS) || validTimeHoursStarts > 24) {

                    ngDialog.open({
                        template: 'This is not the Valid Time. Please Change the Time or the Date.',
                        plain: true
                    });
                    return false;
                } else {
                    if (tripType == 'PICKUP' && timeDiffInMins < 360) {

                        ngDialog.open({
                            template: 'Please select PickUp time 6 hours from now.',
                            plain: true
                        });
                        return false;
                    }
                    if (tripType == 'DROP' && timeDiffInMins < 120) {

                        ngDialog.open({
                            template: 'Please select Drop time 2 hours from now.',
                            plain: true
                        });
                        return false;
                    }
                }
                return true;
            }
        };

        $scope.isFromDateChange = function(fromDate) {
            $scope.fromDate = convertDateUTC(fromDate);
        }

        $scope.saveRequest = function(request, fromDate, index, newTripTime, adHoctime, tabDetails) {

            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            $scope.tripTimeNew = request.tripTime;


            $scope.adHoctimeConvert = convertToTime(adHoctime);

            if (adhocTimePicker == 'Yes') {
                if (newTripTime == undefined && adHoctime == undefined) {
                    ngDialog.open({
                        template: 'Kindly choose shift Time or adHoctime',
                        plain: true
                    });
                    return false;
                }
            } else {
                if (newTripTime == undefined) {
                    ngDialog.open({
                        template: 'Kindly choose shift Time ',
                        plain: true
                    });
                    return false;
                }
            }


            if (request.needReshedule) {
                if (!$scope.needAdHoc) {
                    if ($scope.newTripTime) {
                        $scope.validRescheduleRequest = false;
                        newShiftTimeType = "N";
                        $scope.tripTime = newTripTime.shiftTime;
                        newShiftIdSelected = newTripTime.shiftId;
                        if (tabDetails == 'adhoc') {
                            $scope.empRequestDetailsData[index].tripTime = $scope.newTripTime.shiftTime;
                        } else {
                            $scope.empRequestDetailsDataReshedule[index].tripTime = $scope.newTripTime.shiftTime;
                        }

                    } else {
                        $scope.selectedNewTripTime = $scope.newTripTime;
                        newShiftTimeType = "N";
                        $scope.tripTime = newTripTime.shiftTime;
                        newShiftIdSelected = newTripTime.shiftId;
                        if (tabDetails == 'adhoc') {
                            $scope.empRequestDetailsData[index].tripTime = $scope.newTripTime;
                            $scope.empRequestDetailsData[index].requestDate = $scope.fromDate;
                        } else {
                            $scope.empRequestDetailsDataReshedule[index].tripTime = $scope.newTripTime;
                            $scope.empRequestDetailsDataReshedule[index].requestDate = $scope.fromDate;
                        }

                    }

                } else {
                    $scope.selectedNewTripTime = convertToTime($scope.adHoctime);
                    newShiftTimeType = "A";
                    newShiftIdSelected = 0;
                    $scope.tripTime = $scope.adHoctimeConvert;
                    if (tabDetails == 'adhoc') {
                        $scope.empRequestDetailsData[index].tripTime = convertToTime($scope.adHoctime);
                        $scope.empRequestDetailsData[index].requestDate = $scope.fromDate;
                    } else {
                        $scope.empRequestDetailsDataReshedule[index].tripTime = convertToTime($scope.adHoctime);
                        $scope.empRequestDetailsDataReshedule[index].requestDate = $scope.fromDate;
                    }


                }

                if (angular.isDate($scope.fromDate)) {
                    $scope.fromDate = convertDateUTC($scope.fromDate)
                } else {
                    $scope.fromDate = $scope.fromDate
                }
                var data = {
                    efmFmUserMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    requestId: request.requestId,
                    time: $scope.tripTime,
                    resheduleDate: $scope.fromDate,
                    shiftId: newShiftIdSelected,
                    requestType: newShiftTimeType,
                    requestStatus: 'W',
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/trip/reshedulerequestfromweb/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == 'dayRequestExist') {
                            ngDialog.open({
                                template: 'You already have a request for the given date',
                                plain: true
                            });
                        } else if (data.status == 'backDateRequest') {
                            ngDialog.open({
                                template: 'Sorry you can not create a back date request.',
                                plain: true
                            });
                        } else if (data.status == 'notValidShiftTime') {
                            ngDialog.open({
                                template: 'kindly raise the request before the cut-off Time.',
                                plain: true
                            });
                        } else if (data.status == 'ceilingCountExceeded') {
                            ngDialog.open({
                                template: 'Request Limit Exceeded  for this Shift Time',
                                plain: true
                            });
                        } else {
                            ngDialog.open({
                                template: 'Your request has been reshedule successfully.',
                                plain: true
                            });
                            $scope.getRescheduleRequestDetails();
                            $scope.saveIsClicked = false;
                            request.needReshedule = false;
                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            } else {
                request.needReshedule = true;
            }
            $scope.needAdHoc = false;
        };

        $scope.cancelReschedule = function(request, index, tabDetails) {

            if (tabDetails == 'adhoc') {
                angular.forEach($scope.empRequestDetailsData, function(item) {
                    item.needReshedule = false;
                });
            } else {
                angular.forEach($scope.empRequestDetailsDataReshedule, function(item) {
                    item.needReshedule = false;
                });
            }

            $scope.saveIsClicked = false;
            $scope.needAdHoc = false;
        };

        $scope.adHoc = function() {
            $scope.needAdHoc = true;
        };

        $scope.cancelAdHoc = function() {
            $scope.needAdHoc = false;
        }


        $scope.setNewTripTime = function(newTime, request) {
            if (!$scope.needAdHoc) {
                $scope.newTripTime = newTime;
            } else if ($scope.needAdHoc) {
                $scope.adHoctime = newTime;
            }
        };

        $scope.isFromDate = function(fromDateFormat) {
            $scope.isFromDateEntered = false;
            $scope.newRequest.endDate = '';
            var noOfDaysRequest = $scope.requestCutOffNoOfDays;
            var requestDateCutOff = $scope.requestDateCutOff;


            if ($scope.requestType == 'Days' || $scope.requestType == 'days') {
                $scope.isFromDateEntered = false;
                $scope.newRequest.endDate = '';
                var someDate = fromDateFormat;
                var dateFinal = new Date();
                var lastDate = dateFinal.setDate(dateFinal.getDate() + noOfDaysRequest);
                var minusCurrentDate = dateFinal.setDate(dateFinal.getDate() - noOfDaysRequest);
                var finalDate = new Date(lastDate);
                $scope.lastDayCount = finalDate;
            }

            if ($scope.requestType == 'Date' || $scope.requestType == 'date') {
                var requestDateType = requestDateCutOff;
                var finalDate = new Date(requestDateType);
                $scope.lastDayCount = finalDate;
            }

        };

        $scope.isFromDateAdmin = function() {
            $scope.isFromDateEntered = false;
            $scope.newRequestByAdmin.endDate = '';
        };

        //Delete Request from the 'Cancel Request Tab'
        $scope.deleteRequest = function(request, index) {

            $confirm({
                    text: 'Are you sure you want to Cancel Cab?',
                    title: 'Confirmation',
                    ok: 'Yes',
                    cancel: 'No'
                })
                .then(function() {
                    combinedFacilityId = String($rootScope.combinedFacilityId);
                    localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
                    if (combinedFacilityId == "undefined") {
                        combinedFacilityId = branchId;
                        localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
                    }

                    var requestTime = request.tripTime.split(':');
                    var requestDate = new Date(dateFormatConverter(request.requestDate));
                    requestDate.setHours(requestTime[0]);
                    requestDate.setMinutes(requestTime[1]);
                    var requestTimeInMS = requestDate.getTime();

                    var todaysDate = new Date();
                    var todayInMS = todaysDate.getTime();

                    var timeDifference = (requestTimeInMS - todayInMS) / 1000 / 60;

                    var data = {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        requestId: request.requestId,
                        userId: profileId,
                        combinedFacility: combinedFacilityId
                    };
                    $http.post('services/trip/employeerequestdelete/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            if (data.status == 'success') {
                                ngDialog.open({
                                    template: 'Your request has been cancel successfully.',
                                    plain: true
                                });
                                $scope.empRequestDetailsDataCancel.splice(index, 1);
                            } else {
                                ngDialog.open({
                                    template: 'Could not able to cancel this request',
                                    plain: true
                                });

                            }
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });

                });
        };

        $scope.selectShiftTimeRadio = function(shiftTime, tabTypeClicked) {
            $scope.tabTypeClicked = tabTypeClicked;
            //if the User Role is ADMIN
            if (tabTypeClicked == 'ADHOC') {
                $scope.newShiftTypeSelected = shiftTime;
                if ($scope.newRequestByAdmin.fromDate) {
                    $scope.isShiftTimeDisable = false;
                }
                $('.btn-link').addClass('noPointer');
            } else {
                $scope.newShiftTypeSelected = shiftTime;
                if ($scope.newRequest.fromDate) {
                    $scope.isShiftTimeDisable = false;
                }
                $('.btn-link').addClass('noPointer');
            }
        };

        $scope.selectShiftTimeRadio2 = function(shiftTime, tabTypeClicked) {
            if (tabTypeClicked == 'ADHOC') {
                $scope.newShiftTypeSelected = shiftTime;
                if ($scope.newRequestByAdmin.fromDate) {
                    $scope.isShiftTimeDisable = true;
                }
                $('.btn-link').removeClass('noPointer');
            } else {
                $scope.newShiftTypeSelected = shiftTime;
                if ($scope.newRequest.fromDate) {
                    $scope.isShiftTimeDisable = true;
                }
                $('.btn-link').removeClass('noPointer');
            }
        };

        $scope.timePickerChange = function() {
            $scope.adhocChanged = false;
            $scope.form.createNewRequestForm.$invalid = false;
        }

        $scope.setTripType = function(requestTime, fromDate, tripType) {
            $scope.tripTypeBoth = false;
            var validFromtime;
            if (angular.isObject(tripType)) {
                if (tripType.value == "BOTH") {
                    $scope.tripTypeBoth = true;
                    $scope.getDropShiftTime("DROP");
                    $scope.getPickUpShiftTime("PICKUP");
                } else {
                    $scope.getShiftTime(tripType.value);
                    $scope.tripTypeBoth = false;
                }
                $scope.createNewRequest.tripType = tripType.value;
                $scope.isTripTypeSelected = false;
            } else $scope.createNewRequest.tripType = '';
        };

        $scope.isRadioDisable = function() {
            if (!$scope.isFromDateEntered && !$scope.isTripTypeSelected) {
                return false;
            } else return true;
        }

        $scope.validateRequestTime = function(requestTime, fromDate, tripType) {
            var RequestedScheduleTime;
            var timeSelectedArray;
            var requestedDateInMS;
            var timeDiffInMins;
            var validTimeHoursStarts;
            var seledtedFromDate = new Date(fromDate);

            var todaysDate = new Date();
            var todayInMS = todaysDate.getTime();

            var today = convertDateUTC(todaysDate);
            var requested = convertDateUTC(seledtedFromDate);

            if (tripType.value == 'DROP') {}
            if (tripType.value == 'PICKUP') {}

            if ($scope.newShiftTypeSelected == 'preDefineShiftTime') {


                requestedDateInMS = seledtedFromDate.getTime();
            } else if ($scope.newShiftTypeSelected == 'ShiftTimeCustom') {
                var timePicker = new Date(requestTime)
                var hh = timePicker.getHours();
                var min = timePicker.getMinutes();
                seledtedFromDate.setHours(hh);
                seledtedFromDate.setMinutes(min);
                selectedTimeInMinutes = hh * 60 + min;
                requestedDateInMS = seledtedFromDate.getTime();
            }

            timeDiffInMins = Math.floor((requestedDateInMS - todayInMS) / 1000 / 60);

            //Check the User Role
            if ($scope.adminRole && ($scope.newShiftTypeSelected == 'preDefineShiftTime' || $scope.newShiftTypeSelected == 'ShiftTimeCustom')) {
                //Check if its Request Date is equals to Todays Date              
                if (today == requested && (requestedDateInMS < todayInMS || validTimeHoursStarts > 24)) {

                    ngDialog.open({
                        template: 'This is not the Valid Time. Please Change the Time or the Date.',
                        plain: true
                    });
                    return false;
                } else {
                    if (tripType.value == 'PICKUP' && timeDiffInMins < 5) {
                        ngDialog.open({
                            template: 'Please select PickUp time 6 hours from now.',
                            plain: true
                        });
                        return false;
                    }
                    if (tripType.value == 'DROP' && timeDiffInMins < 5) {
                        ngDialog.open({
                            template: 'Please select Drop time 2 hours from now.',
                            plain: true
                        });
                        return false;
                    }
                }
                return true;
            } else if (!$scope.adminRole && $scope.newShiftTypeSelected == 'ShiftTimeCustom') {
                if (selectedTimeInMinutes >= 180 && selectedTimeInMinutes <= 300) {
                    if (today == requested && (requestedDateInMS < todayInMS || validTimeHoursStarts > 24)) {
                        ngDialog.open({
                            template: 'This is not the Valid Time. Please Change the Time or the Date.',
                            plain: true
                        });
                        return false;
                    } else {
                        if (tripType.value == 'PICKUP' && timeDiffInMins < 360) {
                            ngDialog.open({
                                template: 'Please select PickUp time 6 hours from now.',
                                plain: true
                            });
                            return false;
                        }
                        if (tripType.value == 'DROP' && timeDiffInMins < 120) {
                            ngDialog.open({
                                template: 'Please select Drop time 2 hours from now.',
                                plain: true
                            });
                            return false;
                        }
                    }
                }
                return true;
            } else if (!$scope.adminRole && $scope.newShiftTypeSelected == 'preDefineShiftTime') {
                if (today == requested && (requestedDateInMS < todayInMS || validTimeHoursStarts > 24)) {
                    ngDialog.open({
                        template: 'This is not the Valid Time. Please Change the Time or the Date.',
                        plain: true
                    });
                    return false;
                } else {
                    if (tripType.value == 'PICKUP' && timeDiffInMins < 360) {
                        ngDialog.open({
                            template: 'Please select PickUp time 6 hours from now.',
                            plain: true
                        });
                        return false;
                    }
                    if (tripType.value == 'DROP' && timeDiffInMins < 120) {
                        ngDialog.open({
                            template: 'Please select Drop time 2 hours from now',
                            plain: true
                        });
                        return false;
                    }
                }
                return true;
            }
        };

        $scope.setRequestType = function(requestType) {
            if (angular.isObject(requestType)) {
                $scope.isFromDateEntered = true;
                $scope.newRequestByAdmin = {};
                $scope.isShiftTimeDisable = true;

                requestIndex = _.findIndex($scope.requestTypes, {
                    value: requestType.value
                });
                var d = new Date();
                d.setHours(00);
                d.setMinutes(0);
                $('.btn-link').addClass('noPointer');
                $scope.newRequestByAdmin = {
                    'requestType': $scope.requestTypes[requestIndex],
                    'id': '',
                    'name': '',
                    'email': '',
                    'createNewAdHocTime': d,
                    'createNewtripTime': $scope.tripTimes[0],

                    'areaCode2': $scope.areaCodes[0],
                    'contact': '',
                    "createNewAdHocTime": new Date(),
                    "createNewPickupAdHocTime": new Date(),
                    "createDropNewAdHocTime": new Date(),
                };
                $scope.isTripTypeSelected = false;
                $scope.createRequestIsClicked = true;
                $scope.createRequestRole_ADMIN = true;
                $scope.createRequestRole_EMPLOYEE = false;
                $scope.createRequestRole_GUEST = false;

                $scope.requestFor = requestType.value;
                switch (requestType.value) {
                    case "SELF":
                        $scope.createRequestRole_ADMIN = true;
                        $scope.createRequestRole_EMPLOYEE = false;
                        $scope.createRequestRole_GUEST = false;
                        $scope.multipleRequestModal = false;
                        break;
                    case "EMPLOYEE":
                        $scope.createRequestRole_ADMIN = false;
                        $scope.createRequestRole_EMPLOYEE = true;
                        $scope.createRequestRole_GUEST = false;
                        $scope.multipleRequestModal = false;
                        break;
                    case "GUEST":
                        $scope.createRequestRole_ADMIN = false;
                        $scope.createRequestRole_EMPLOYEE = false;
                        $scope.createRequestRole_GUEST = true;
                        $scope.multipleRequestModal = true;
                        break;
                }
            }
        }

        $scope.idIsEntered = function() {
            if ($scope.newRequestByAdmin.id) {
                $(".creatNewReqButton").removeClass("disabled");
            } else $(".creatNewReqButton").addClass("disabled");
        };


        $scope.project = {};
        $scope.project.allocatedDetails = [];

        $scope.daysButtonLabel = {
            'buttonDefaultText': 'Select Project Employee Allocated Details'
        };

        $scope.daysSettings = {
            smartButtonMaxItems: 5,
            smartButtonTextConverter: function(itemText, originalItem) {
                return itemText;
            }
        };

        $scope.datePickerFlg = requestType;
        $scope.requestType = requestType;
        $scope.monthOrDays = monthOrDays;
        $scope.initialzeNewCustomTime = function() {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }
            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/user/listOfProjectId', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.listOfProjectIdData = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });


            var data = {
                branchId: branchId,
                reportingManagerUserId: profileId,
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/employee/projectEmployeesByRepMng/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.projectDetails = [];
                    angular.forEach(data, function(value, key) {
                        var obj = {};
                        obj.id = value.userId;
                        obj.label = value.employeeId;
                        $scope.projectDetails.push(obj);
                    })
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

            var data = {
                branchId: branchId,
                tripType: "Drop",
                userId: profileId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/trip/shiftTimeByTripType/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.tripTimeDataReschedule = data;
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });

            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            $('.btn-link').addClass('noPointer');
            $scope.getShiftTime('DROP');
            $scope.isTripTypeSelected = false;
            $scope.newRequest = {
                'createNewAdHocTime': d,
                'createNewtripTime': $scope.tripTimes[0]
            };
        };

        // Set Project Name and Project Id - Request Details

        $scope.sendSelectedUserDetails = function(value, listOfProjectIdData) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }
            if (listOfProjectIdData == 'name') {
                value[0].name = value[0].ClientprojectId;
                $scope.listOfProjectId = value;
            } else {
                value[0].name = value[0].projectName;
                $scope.listOfProjectName = value;
            }
            var data = {
                branchId: branchId,
                reportingManagerUserId: profileId,
                userId: profileId,
                projectId: value[0].projectId,
                combinedFacility: combinedFacilityId
            };
            $http.post('services/employee/projectEmployeesByRepMng/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.projectDetails = [];
                    angular.forEach(data, function(value, key) {
                        value.name = value.employeeId;
                        value.ticked = false;
                        value.label = value.userId;
                    })

                    $scope.projectDetails = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }


        $scope.sendSelectedUserDetailsAdhoc = function(value, listOfProjectIdData) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }


            if (listOfProjectIdData == 'name') {
                value[0].name = value[0].ClientprojectId;
                $scope.listOfProjectIdAdhoc = value;
            } else {
                value[0].name = value[0].projectName;
                $scope.listOfProjectNameAdhoc = value;
            }

            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                eFmFmClientProjectDetails: {
                    projectId: value[0].projectId
                },
                combinedFacility: combinedFacilityId

            };
            $http.post('services/user/listReportingMngByProjectId/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.projectDetails = [];
                    angular.forEach(data, function(value, key) {
                        value.name = value.repEmployeeId;
                        value.ticked = false;
                    })

                    $scope.projectDetails = data;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.initialzeNewCustomTimeByAdmin = function() {
            $scope.initialzeNewCustomTime();
            $scope.newRequestByAdmin = {};
            var d = new Date();
            d.setHours(00);
            d.setMinutes(0);
            $('.btn-link').addClass('noPointer');
            $scope.getShiftTime('DROP');
            $scope.newRequestByAdmin = {
                'createNewAdHocTime': d,
                'createNewtripTime': $scope.tripTimes[0],
                'requestType': $scope.requestTypes[0],
                'areaCode2': $scope.areaCodes[0],
                "createNewAdHocTime": new Date(),
                "createNewPickupAdHocTime": new Date(),
                "createDropNewAdHocTime": new Date(),
                'location': ''
            };
            $scope.isTripTypeSelected = false;
            $scope.createRequestIsClicked = true;
            $scope.createRequestRole_ADMIN = true;
            $scope.createRequestRole_EMPLOYEE = false;
            $scope.createRequestRole_GUEST = false;
        };

        $scope.resetRequestDetails = function(request) {
            $scope.newRequest.fromDate = '';
            $scope.newRequest.endDate = '';
            $scope.newRequest.tripType = '';
            $scope.newRequest.createNewtripTime = '';
            $scope.newRequest.createNewAdHocTime = '';
            $scope.shiftTime = '';
            $scope.newRequestByAdmin.id = '';
            $scope.newRequestByAdmin.name = '';
            $scope.newRequestByAdmin.lname = '';
            $scope.newRequestByAdmin.email = '';
            $scope.newRequestByAdmin.contact = '';
            $scope.newRequestByAdmin.contact2 = '';
            $scope.newRequestByAdmin.location = '';
            $scope.newRequestByAdmin.location2 = '';
            $scope.newRequestByAdmin.fromDate = '';
            $scope.newRequestByAdmin.endDate = '';
            $scope.newRequestByAdmin.createNewtripTime = '';
        }

        $scope.createNewRequest = function(request, createNewPickupAdHocTime, createNewDropAdHocTime, project, projectName) {
            if (request.fromDate == undefined && request.endDate == undefined) {
                ngDialog.open({
                    template: 'Kindly Choose From Date and End Date',
                    plain: true
                });
                return false;
            } else if (request.fromDate == undefined) {
                ngDialog.open({
                    template: 'Kindly Choose From Date',
                    plain: true
                });
                return false;
            } else if (request.endDate == undefined) {
                ngDialog.open({
                    template: 'Kindly Choose End Date',
                    plain: true
                });
                return false;
            }

            if ($scope.tabTypeClicked == 'ADHOC') {
                var timeFlg = 'A';
            } else {
                var timeFlg = 'S';
            }

            if (projectName.length == 0) {
                var multipleProjectEmpIds = 0;
                var multipleEmpIds = 0;
            } else {
                var multipleProjectEmpIds = String(_.pluck(projectName, 'userId'));
                var multipleEmpIds = 1;
            }

            if (project == 0) {
                var projectId = 0;
                var repUserId = 0;
            } else {
                var projectId = project[0].projectId;
                var repUserId = project[0].repUserId;
            }

            if ($rootScope.result != undefined) {
                var resultLocation = $rootScope.result;
                var locationLength = resultLocation.length;
                var locationDetailsData = [];
                angular.forEach(resultLocation, function(item) {
                    locationDetailsData.push(item.destination);
                });

                var areaListDetails = locationDetailsData;
                var originDetails = $rootScope.originAreaId;
                areaListDetails.unshift(originDetails);
                var locationIdList = _.pluck(areaListDetails, 'areaId');
            }

            if (locationIdList == undefined) {
                locationIdList = 0;
                var locationFlg = "N";
            } else {
                locationIdList = String(locationIdList);
                var locationFlg = "M";
            }

            $(".creatNewReqButton").addClass("disabled");
            if (true) {
                newShiftTimeType = "N";
            } else if ($scope.newShiftTypeSelected == 'ShiftTimeCustom') {
                newShiftIdSelected = 0;
                newShiftTimeType = "A"
            }
            if (multipleEmpIds == 1) {
                repUserId = profileId;
            }


            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                requestFrom: "W",
                startDate: convertDateUTC(request.fromDate),
                endDate: convertDateUTC(request.endDate),
                tripType: request.tripType.value,
                employeeId: "NO",
                requestType: newShiftTimeType,
                locationWaypointsIds: locationIdList,
                locationFlg: locationFlg,
                multipleProjectEmpIds: multipleProjectEmpIds,
                projectId: projectId,
                multipleEmpIds: multipleEmpIds,
                reportingManagerUserId: repUserId,
                pickupTime: 0,
                pickupShiftId: 0,
                dropTime: 0,
                dropShiftId: 0,
                timeFlg: timeFlg,
                combinedFacility: combinedFacilityId
            };

            if (request.tripType.text == "BOTH" && request.shiftType.text == "NORMAL") {
                data.pickupTime = request.createNewPickupTime.shiftTime;
                data.pickupShiftId = request.createNewPickupTime.shiftId;
                data.dropTime = request.createNewDroptripTime.shiftTime;
                data.dropShiftId = request.createNewDroptripTime.shiftId;
                data.shiftId = 0;
                data.time = "00:00:00";
            } else if (request.tripType.text == "BOTH" && request.shiftType.text == "ADHOC") {
                data.pickupTime = convertToTime(createNewPickupAdHocTime);
                data.pickupShiftId = 0;
                data.dropTime = convertToTime(createNewDropAdHocTime);
                data.dropShiftId = 0;
                data.shiftId = 0;
                data.time = "00:00:00";
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "NORMAL") {
                data.shiftId = request.createNewPickupTime.shiftId;
                data.time = request.createNewPickupTime.shiftTime;;
            } else if (request.tripType.text == "PICKUP" && request.shiftType.text == "ADHOC") {
                data.shiftId = 0;
                data.time = convertToTime(createNewPickupAdHocTime);
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "NORMAL") {
                data.shiftId = request.createNewDroptripTime.shiftId;
                data.time = request.createNewDroptripTime.shiftTime;;
            } else if (request.tripType.text == "DROP" && request.shiftType.text == "ADHOC") {
                data.shiftId = 0;
                data.time = convertToTime(createNewDropAdHocTime);
            }


            $http.post('services/trip/devicerequest/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.form.createNewRequestForm.$setPristine();
                    if (data.status == 'dayRequestExist') {
                        ngDialog.open({
                            template: 'You have already raised a request for this date.',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'WithOutApprovalReqExceeded') {
                        ngDialog.open({
                            template: 'Approval request limit has exceeded please Contact your approver',
                            plain: true
                        });
                    } else if (data.status == 'backDateRequest') {
                        ngDialog.open({
                            template: 'Could not able to raise the back date request.',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'notValidShiftTime') {
                        ngDialog.open({
                            template: 'kindly raise the request before the cut-off Time',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'failed') {
                        ngDialog.open({
                            template: 'Request has been failed',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'empDisable') {
                        ngDialog.open({
                            template: 'Sorry this employee id is already disable',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'requestExist') {
                        ngDialog.open({
                            template: 'Already You have a request for this Date interval',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'adminPolicy') {
                        ngDialog.open({
                            template: 'As per transport policy You could not create the request for this Dates',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'setConfig') {
                        ngDialog.open({
                            template: 'Kinldy the configuration at application setting',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'requestNotPossible') {
                        ngDialog.open({
                            template: 'As per transport policy You could not create the request for this Dates',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'daysExceeded') {
                        ngDialog.open({
                            template: 'Could not able to raise the request more than' + ' ' + $scope.requestCutOffNoOfDays + ' ' + 'Days',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'ceilingCountExceeded') {
                        ngDialog.open({
                            template: 'Request Limit Exceeded  for this Shift Time',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'dateExceeded') {
                        ngDialog.open({
                            template: 'Could not able to raise the request more than' + ' ' + $scope.requestDateCutOff + ' ' + 'Date',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'exception') {
                        ngDialog.open({
                            template: 'kindly contact admin',
                            plain: true
                        });
                        $scope.initialzeNewCustomTime();
                        $(".creatNewReqButton").removeClass("disabled");
                    } else if (data.status == 'success') {
                        $scope.newRequest.fromDate = '';
                        $scope.newRequest.endDate = '';
                        $scope.newRequest.tripType = '';
                        $scope.newRequest.createNewtripTime = '';
                        $scope.newRequest.createNewAdHocTime = '';
                        $scope.shiftTime = '';

                        ngDialog.open({
                            template: 'Your request has been created successfully',
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");
                        $scope.initialzeNewCustomTime();
                        $scope.initialzeNewCustomTimeByAdmin();
                    } else {
                        var statusMessage = data.status;

                        ngDialog.open({
                            template: statusMessage,
                            plain: true
                        });
                        $(".creatNewReqButton").removeClass("disabled");

                    }
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.makeFormValid = function() {
            $("#hostNum").removeClass("error");
            $("#hostNum").removeClass("ng-invalid");
            $("#hostNum").removeClass("ng-invalid-pattern ");
        }

        $scope.manilaAdhocReset = function() {
            $scope.newRequestByAdminAdhocManila = {
                requestType: '',
                cabType: '',
                cabTripType: '',
                createNewAdHocTime: '',
                origin: '',
                finalDestination: '',
                subDestination1: '',
                subDestination2: '',
                subDestination3: '',
                subDestination4: '',
                subDestination5: '',
                subDestination5: '',
                id: '',
                name: '',
                lname: '',
                email: '',
                contact: '',
                bookedBy: '',
                chargedTo: '',
                accountName: '',
                paymentType: '',
                remarks: '',
                contact2: '',
                gender: '',
                fromDate: '',
                endDate: '',
                durationInHours: ''
            };

        }

        $scope.createNewRequestByAdminGuest = function(newRequestByAdminAdhocManila) {
            $("#createNewRequestByAdminGuest").addClass("disabled");
            //Origin 
            if ($scope.newRequestByAdminAdhocManila.origin == undefined) {
                $scope.newRequestByAdminAdhocManila.origin = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.origin = $scope.newRequestByAdminAdhocManila.origin;
            }
            // Final Destination
            if ($scope.newRequestByAdminAdhocManila.finalDestination == undefined) {
                $scope.newRequestByAdminAdhocManila.finalDestination = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.finalDestination = $scope.newRequestByAdminAdhocManila.finalDestination;
            }
            // subDestination1 
            if ($scope.newRequestByAdminAdhocManila.subDestination1 == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination1 = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination1 = $scope.newRequestByAdminAdhocManila.subDestination1;
            }
            // subDestination2 
            if ($scope.newRequestByAdminAdhocManila.subDestination2 == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination2 = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination2 = $scope.newRequestByAdminAdhocManila.subDestination2;
            }
            // subDestination3 
            if ($scope.newRequestByAdminAdhocManila.subDestination3 == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination3 = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination3 = $scope.newRequestByAdminAdhoc.subDestination3;
            }
            // subDestination4 
            if ($scope.newRequestByAdminAdhocManila.subDestination4 == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination4 = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination4 = $scope.newRequestByAdminAdhocManila.subDestination4;
            }
            // subDestination5 
            if ($scope.newRequestByAdminAdhocManila.subDestination5 == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination5 = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination5 = $scope.newRequestByAdminAdhocManila.subDestination5;
            }
            // OriginLatiLogi 
            if ($scope.newRequestByAdminAdhocManila.originLatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.originLatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.originLatiLogi = $scope.newRequestByAdminAdhocManila.originLatiLogi;
            }

            // finalDestinationLatiLogi

            if ($scope.newRequestByAdminAdhocManila.finalDestinationLatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.finalDestinationLatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.finalDestinationLatiLogi = $scope.newRequestByAdminAdhocManila.finalDestinationLatiLogi;
            }


            // subDestination1LatiLogi

            if ($scope.newRequestByAdminAdhocManila.subDestination1LatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination1LatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination1LatiLogi = $scope.newRequestByAdminAdhocManila.subDestination1LatiLogi;
            }


            // subDestination2LatiLogi

            if ($scope.newRequestByAdminAdhocManila.subDestination2LatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination2LatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination2LatiLogi = $scope.newRequestByAdminAdhocManila.subDestination2LatiLogi;
            }


            // subDestination3LatiLogi

            if ($scope.newRequestByAdminAdhocManila.subDestination3LatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination3LatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination3LatiLogi = $scope.newRequestByAdminAdhocManila.subDestination3LatiLogi;
            }


            // subDestination4LatiLogi

            if ($scope.newRequestByAdminAdhocManila.subDestination4LatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination4LatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination4LatiLogi = $scope.newRequestByAdminAdhocManila.subDestination4LatiLogi;
            }


            // subDestination5LatiLogi

            if ($scope.newRequestByAdminAdhocManila.subDestination5LatiLogi == undefined) {
                $scope.newRequestByAdminAdhocManila.subDestination5LatiLogi = 'N';
            } else {
                $scope.newRequestByAdminAdhocManila.subDestination5LatiLogi = $scope.newRequestByAdminAdhocManila.subDestination5LatiLogi;
            }

            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }


            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId,
                requestType: newRequestByAdminAdhocManila.requestType.value,
                reservationType: newRequestByAdminAdhocManila.cabType.value,
                tripType: newRequestByAdminAdhocManila.cabTripType.value,
                time: convertToTime(newRequestByAdminAdhocManila.createNewAdHocTime),
                requestFrom: "W",
                originAddress: newRequestByAdminAdhocManila.origin,
                endDestinationAddress: newRequestByAdminAdhocManila.finalDestination,
                destination1Address: newRequestByAdminAdhocManila.subDestination1,
                destination2Address: newRequestByAdminAdhocManila.subDestination2,
                destination3Address: newRequestByAdminAdhocManila.subDestination3,
                destination4Address: newRequestByAdminAdhocManila.subDestination4,
                destination5Address: newRequestByAdminAdhocManila.subDestination5,
                originLattitudeLongitude: newRequestByAdminAdhocManila.originLatiLogi,
                endDestinationAddressLattitudeLongitude: newRequestByAdminAdhocManila.finalDestinationLatiLogi,
                destination1AddressLattitudeLongitude: newRequestByAdminAdhocManila.subDestination1LatiLogi,
                destination2AddressLattitudeLongitude: newRequestByAdminAdhocManila.subDestination2LatiLogi,
                destination3AddressLattitudeLongitude: newRequestByAdminAdhocManila.subDestination3LatiLogi,
                destination4AddressLattitudeLongitude: newRequestByAdminAdhocManila.subDestination4LatiLogi,
                destination5AddressLattitudeLongitude: newRequestByAdminAdhocManila.subDestination5LatiLogi,
                employeeId: newRequestByAdminAdhocManila.id,
                firstName: newRequestByAdminAdhocManila.name,
                lastName: newRequestByAdminAdhocManila.lname,
                emailId: newRequestByAdminAdhocManila.email,
                mobileNumber: newRequestByAdminAdhocManila.contact,
                durationInHours: newRequestByAdminAdhocManila.durationInHours,
                bookedBy: newRequestByAdminAdhocManila.bookedBy,
                chargedTo: newRequestByAdminAdhocManila.chargedTo,
                accountName: newRequestByAdminAdhocManila.accountName,
                paymentType: newRequestByAdminAdhocManila.paymentType.value,
                remarks: newRequestByAdminAdhocManila.remarks,
                hostMobileNumber: newRequestByAdminAdhocManila.contact2,
                gender: newRequestByAdminAdhocManila.gender.value,
                startDate: convertDateUTC(newRequestByAdminAdhocManila.fromDate),
                endDate: convertDateUTC(newRequestByAdminAdhocManila.endDate),
                combinedFacility: combinedFacilityId
            };
            $http.post('services/adhoc/guestAdhocRequest/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == 'dayRequestExist') {
                        ngDialog.open({
                            template: 'You have already raised a request for this Shift Time.',
                            plain: true
                        });
                    } else if (data.status == 'empDisable') {
                        ngDialog.open({
                            template: 'Sorry this employee id is already disable',
                            plain: true
                        });
                    } else {
                        ngDialog.open({
                            template: 'Your request has been created successfully',
                            plain: true
                        });
                        $scope.manilaAdhocReset();
                        $scope.initialzeNewCustomTimeByAdmin();

                    }
                }

            }).
            error(function(data, status, headers, config) {
                // log error
            });

        }

        $scope.createNewRequestByAdmin = function(requestByAdmin, project, projectName) {

            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }


            if (projectName.length == 0) {
                var multipleProjectEmpIds = 0;
                var multipleEmpIds = 0;
                var repUserId = 0;
            } else {
                var multipleProjectEmpIds = 0;
                var multipleEmpIds = 1;
                var repUserId = projectName[0].repUserId;
            }

            if (project.length == 0) {
                var projectId = 0;

            } else {
                var projectId = project[0].projectId;
            }

            if (requestByAdmin.routeName == null) {
                var routeAreaId = 0;
            } else {
                var routeAreaId = requestByAdmin.routeName.routeAreaId;
            }

            if ($rootScope.result != undefined) {
                var resultLocation = $rootScope.result;
                var locationLength = resultLocation.length;
                var locationDetailsData = [];
                angular.forEach(resultLocation, function(item) {
                    locationDetailsData.push(item.destination);
                });

                var areaListDetails = locationDetailsData;
                var originDetails = $rootScope.originAreaId;
                areaListDetails.unshift(originDetails);

                var locationIdList = _.pluck(areaListDetails, 'areaId');
            }

            if (locationIdList == undefined) {
                locationIdList = 0;
                var locationFlg = "N";
            } else {
                locationIdList = String(locationIdList);
                var locationFlg = "M";
            }
            if (requestByAdmin.tripType) {
                if (requestByAdmin.tripType.value != "BOTH" && requestByAdmin.shiftType == "NORMAL") {
                    if (requestByAdmin.createNewtripTime == undefined) {
                        ngDialog.open({
                            template: 'Kindly choose shift time',
                            plain: true
                        });
                        return false;
                    }
                }
            }
            $scope.form.createNewRequestByAdminForm.$setPristine();
            if (requestByAdmin.tripType) {
                if (requestByAdmin.tripType.value != "BOTH") {
                    if (requestByAdmin.shiftType == 'NORMAL') {
                        newShiftTimeSelectedTime = requestByAdmin.createNewtripTime.shiftTime;
                        newShiftTimeSelectedId = requestByAdmin.createNewtripTime.shiftId;
                        newShiftTimeType = "N";
                        var timeFlg = 'S';
                    } else if (requestByAdmin.shiftType == 'ADHOC') {
                        newShiftTimeType = "A";
                        var timeFlg = 'A';
                        newShiftTimeSelectedTime = convertToTime(requestByAdmin.createNewAdHocTime);
                        newShiftTimeSelected = convertToTime(requestByAdmin.createNewAdHocTime);

                        newShiftTimeSelectedId = 0;
                    }
                }
            }
            switch (requestByAdmin.requestType.value) {
                case "SELF":
                    var data = {
                        efmFmUserMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        userId: profileId,
                        requestFrom: "W",
                        startDate: convertDateUTC(requestByAdmin.fromDate),
                        endDate: convertDateUTC(requestByAdmin.endDate),
                        tripType: requestByAdmin.tripType.value,
                        employeeId: "NO",
                        time: newShiftTimeSelectedTime,
                        shiftId: newShiftTimeSelectedId,
                        requestType: newShiftTimeType,
                        locationWaypointsIds: locationIdList,
                        locationFlg: locationFlg,
                        multipleProjectEmpIds: multipleProjectEmpIds,
                        projectId: projectId,
                        multipleEmpIds: multipleEmpIds,
                        reportingManagerUserId: repUserId,
                        routeAreaId: routeAreaId,
                        timeFlg: timeFlg,
                        combinedFacility: combinedFacilityId
                    };
                    if (requestByAdmin.tripType.value == "BOTH") {
                        if (requestByAdmin.shiftType == 'NORMAL') {
                            data.time = '00:00:00';
                            data.shiftId = 0;
                            data.timeFlg = "S";
                            data.requestType = "N";
                            data.pickupTime = requestByAdmin.createNewPickuptripTime.shiftTime;
                            data.dropTime = requestByAdmin.createNewDroptripTime.shiftTime;
                            data.pickupShiftId = requestByAdmin.createNewPickuptripTime.shiftId;
                            data.dropShiftId = requestByAdmin.createNewDroptripTime.shiftId;
                            data.combinedFacility = combinedFacilityId
                        } else if (requestByAdmin.shiftType == 'ADHOC') {
                            data.time = '00:00:00';
                            data.shiftId = 0;
                            data.timeFlg = "A";
                            data.requestType = "A";
                            data.pickupTime = convertToTime(requestByAdmin.createNewPickupAdHocTime);
                            data.dropTime = convertToTime(requestByAdmin.createDropNewAdHocTime);
                            data.pickupShiftId = 0;
                            data.dropShiftId = 0;
                            data.combinedFacility = combinedFacilityId;
                        }

                    }

                    if ($scope.validFormFlag) {
                        $http.post('services/trip/devicerequest/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                if (data.status == 'dayRequestExist') {
                                    ngDialog.open({
                                        template: 'You have already raised a request for given date.',
                                        plain: true
                                    });
                                } else if (data.status == 'empDisable') {
                                    ngDialog.open({
                                        template: 'Sorry this employee id is already disable',
                                        plain: true
                                    });
                                } else if (data.status == 'notValidShiftTime') {
                                    ngDialog.open({
                                        template: 'kindly raise the request before the Cut-off',
                                        plain: true
                                    });
                                } else if (data.status == 'backDateRequest') {
                                    ngDialog.open({
                                        template: 'Could not able to raise the back date request.',
                                        plain: true
                                    });
                                } else if (data.status == 'notValidShiftTime') {
                                    ngDialog.open({
                                        template: 'kindly raise the request before the cut-off Time',
                                        plain: true
                                    });
                                } else if (data.status == 'failed') {
                                    ngDialog.open({
                                        template: 'Request has been failed',
                                        plain: true
                                    });
                                } else if (data.status == 'empNotExist') {
                                        ngDialog.open({
                                            template: 'Sorry this employee id is not registered.',
                                            plain: true
                                        });
                                }else if (data.status == 'requestExist') {
                                    ngDialog.open({
                                        template: 'Already You have a request for this Date interval',
                                        plain: true
                                    });
                                    $(".creatNewReqButton").removeClass("disabled");
                                } else if (data.status == 'success') {
                                    ngDialog.open({
                                        template: 'Your request has been created successfully.',
                                        plain: true
                                    });
                                    $(".creatNewReqButton").addClass("disabled");
                                    $scope.initialzeNewCustomTimeByAdmin();
                                } else {
                                    var statusMessage = data.status;

                                    ngDialog.open({
                                        template: statusMessage,
                                        plain: true
                                    });
                                    $(".creatNewReqButton").removeClass("disabled");
                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    }
                    break;
                case "EMPLOYEE":
                    if (!$scope.newRequestByAdmin.id) {
                        ngDialog.open({
                            template: 'Please enter a Valid Employee ID.',
                            plain: true
                        });

                    } else {
                        var data = {
                            efmFmUserMaster: {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                }
                            },
                            userId: profileId,
                            requestFrom: "W",
                            startDate: convertDateUTC(requestByAdmin.fromDate),
                            endDate: convertDateUTC(requestByAdmin.endDate),
                            time: newShiftTimeSelected,
                            tripType: requestByAdmin.tripType.value,
                            employeeId: requestByAdmin.id,
                            time: newShiftTimeSelectedTime,
                            shiftId: newShiftTimeSelectedId,
                            requestType: newShiftTimeType,
                            locationWaypointsIds: locationIdList,
                            locationFlg: locationFlg,
                            multipleProjectEmpIds: multipleProjectEmpIds,
                            projectId: projectId,
                            multipleEmpIds: multipleEmpIds,
                            reportingManagerUserId: repUserId,
                            routeAreaId: routeAreaId,
                            timeFlg: timeFlg,
                            combinedFacility: combinedFacilityId
                        };
                        if (requestByAdmin.tripType.value == "BOTH") {
                            if (requestByAdmin.shiftType == 'NORMAL') {
                                data.time = '00:00:00';
                                data.shiftId = 0;
                                data.timeFlg = "S";
                                data.requestType = "N";
                                data.pickupTime = requestByAdmin.createNewPickuptripTime.shiftTime;
                                data.dropTime = requestByAdmin.createNewDroptripTime.shiftTime;
                                data.pickupShiftId = requestByAdmin.createNewPickuptripTime.shiftId;
                                data.dropShiftId = requestByAdmin.createNewDroptripTime.shiftId;
                                data.combinedFacility = combinedFacilityId
                            } else if (requestByAdmin.shiftType == 'ADHOC') {
                                data.time = '00:00:00';
                                data.shiftId = 0;
                                data.timeFlg = "A";
                                data.requestType = "A";
                                data.pickupTime = convertToTime(requestByAdmin.createNewPickupAdHocTime);
                                data.dropTime = convertToTime(requestByAdmin.createDropNewAdHocTime);
                                data.pickupShiftId = 0;
                                data.dropShiftId = 0;
                                data.combinedFacility = combinedFacilityId
                            }

                        }

                        if ($scope.validFormFlag) {
                            $http.post('services/trip/devicerequest/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.status == 'dayRequestExist') {
                                        ngDialog.open({
                                            template: 'You have already raised a request for given date.',
                                            plain: true
                                        });
                                    } else if (data.status == 'WithOutApprovalReqExceeded') {
                                        ngDialog.open({
                                            template: 'Approval request limit has exceeded please Contact your approver',
                                            plain: true
                                        });
                                    } else if (data.status == 'empDisable') {
                                        ngDialog.open({
                                            template: 'Sorry this employee id is already disable.',
                                            plain: true
                                        });
                                    } else if (data.status == 'notValidShiftTime') {
                                        ngDialog.open({
                                            template: 'kindly Raise the Request before the Cut-off',
                                            plain: true
                                        });
                                    } else if (data.status == 'backDateRequest') {
                                        ngDialog.open({
                                            template: 'Could not able to raise the back dated Request',
                                            plain: true
                                        });
                                    } else if (data.status == 'failed') {
                                        ngDialog.open({
                                            template: 'Request has been failed',
                                            plain: true
                                        });
                                    } else if (data.status == 'empNotExist') {
                                        ngDialog.open({
                                            template: 'Sorry this employee id is not registered.',
                                            plain: true
                                        });
                                    } else {
                                        ngDialog.open({
                                            template: 'Your request has been created successfully.',
                                            plain: true
                                        });
                                        $(".creatNewReqButton").addClass("disabled");
                                        $scope.initialzeNewCustomTimeByAdmin();
                                    }
                                }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });
                        }
                    }
                    break;
                case "GUEST":
                    if ($scope.multipleRequestBtn) {
                        var data = {
                            efmFmUserMaster: {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                }
                            },
                            userId: profileId,
                            requestFrom: "W",
                            multipleEmpIds: multipleEmpIds,
                            hostMobileNumber: requestByAdmin.contact2,
                            firstName: requestByAdmin.name,
                            lastName: requestByAdmin.lname,
                            emailId: requestByAdmin.email,
                            mobileNumber: requestByAdmin.contact,
                            gender: $scope.newRequestByAdmin.gender.value,
                            employeeId: requestByAdmin.id,
                            projectId: projectId,
                            locationWaypointsIds: locationIdList,
                            routeAreaId: routeAreaId,
                            locationFlg: locationFlg,
                            reportingManagerUserId: repUserId,
                            multipleProjectEmpIds: multipleProjectEmpIds,
                            multipleRequestFlg: 1,
                            tripRequestValues: $scope.multipleRequestData,
                            combinedFacility: combinedFacilityId
                        };

                        if ($scope.validFormFlag) {
                            $http.post('services/trip/requestforguest/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    if (data.status == 'alreadyRegisterAsEmp') {
                                        ngDialog.open({
                                            template: 'Sorry this employee id is already register as employee',
                                            plain: true
                                        });
                                    } else if (data.status == 'alreadyRaised') {
                                        ngDialog.open({
                                            template: 'You have already raised a request for given time',
                                            plain: true
                                        });
                                    } else if (data.status == 'backDateRequest') {
                                        ngDialog.open({
                                            template: 'Could not able to raise the back dated Request',
                                            plain: true
                                        });
                                    } else if (data.status == 'NOTVALIDLOCATIONID') {
                                        ngDialog.open({
                                            template: 'Location Id is Not valid ',
                                            plain: true
                                        });
                                    } else if (data.status == 'notValidApprovalManagerId') {
                                        ngDialog.open({
                                            template: 'Kinldy check Approval Manager Id ',
                                            plain: true
                                        });
                                    } else if (data.status == 'failed') {
                                        ngDialog.open({
                                            template: 'Request has been failed',
                                            plain: true
                                        });
                                    } else {
                                        ngDialog.open({
                                            template: 'Your request has been created successfully.',
                                            plain: true
                                        });
                                        $scope.multipleRequestRawData = false;
                                        $(".creatNewReqButton").addClass("disabled");
                                        $scope.initialzeNewCustomTimeByAdmin();
                                    }
                                }


                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });
                        }
                    } else {
                        if ($scope.isLocationEntered && $scope.isLocationEntered2) {
                            var data = {
                                efmFmUserMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                userId: profileId,
                                hostMobileNumber: requestByAdmin.contact2,
                                guestMiddleLoc: requestByAdmin.cords2,
                                firstName: requestByAdmin.name,
                                lastName: requestByAdmin.lname,
                                emailId: requestByAdmin.email,
                                mobileNumber: requestByAdmin.contact,
                                address: $scope.newRequestByAdmin.location,
                                latitudeLongitude: $scope.newRequestByAdmin.cords,
                                gender: $scope.newRequestByAdmin.gender.value,
                                requestFrom: "W",
                                startDate: convertDateUTC(requestByAdmin.fromDate),
                                endDate: convertDateUTC(requestByAdmin.endDate),
                                time: newShiftTimeSelectedTime,
                                tripType: requestByAdmin.tripType.value,
                                employeeId: requestByAdmin.id,
                                locationWaypointsIds: locationIdList,
                                locationFlg: locationFlg,
                                multipleProjectEmpIds: multipleProjectEmpIds,
                                projectId: projectId,
                                multipleEmpIds: multipleEmpIds,
                                reportingManagerUserId: repUserId,
                                routeAreaId: routeAreaId,
                                timeFlg: timeFlg,
                                combinedFacility: combinedFacilityId
                            };
                            if (requestByAdmin.tripType.value == "BOTH") {
                                if (requestByAdmin.shiftType == 'NORMAL') {
                                    data.time = '00:00:00';
                                    data.shiftId = 0;
                                    data.timeFlg = "S";
                                    data.requestType = "N";
                                    data.pickupTime = requestByAdmin.createNewPickuptripTime.shiftTime;
                                    data.dropTime = requestByAdmin.createNewDroptripTime.shiftTime;
                                    data.pickupShiftId = requestByAdmin.createNewPickuptripTime.shiftId;
                                    data.dropShiftId = requestByAdmin.createNewDroptripTime.shiftId;
                                    data.combinedFacility = combinedFacilityId
                                } else if (requestByAdmin.shiftType == 'ADHOC') {
                                    data.time = '00:00:00';
                                    data.shiftId = 0;
                                    data.timeFlg = "A";
                                    data.requestType = "A";
                                    data.pickupTime = convertToTime(requestByAdmin.createNewPickupAdHocTime);
                                    data.dropTime = convertToTime(requestByAdmin.createDropNewAdHocTime);
                                    data.pickupShiftId = 0;
                                    data.dropShiftId = 0;
                                    data.combinedFacility = combinedFacilityId;
                                }

                            }

                            if ($scope.validFormFlag) {
                                $http.post('services/trip/requestforguest/', data).
                                success(function(data, status, headers, config) {
                                    if (data.status != "invalidRequest") {
                                        if (data.status == 'alreadyRegisterAsEmp') {
                                            ngDialog.open({
                                                template: 'Sorry this employee id is already register as employee',
                                                plain: true
                                            });
                                        } else if (data.status == 'alreadyRaised') {
                                            ngDialog.open({
                                                template: 'You have already raised a request for given time',
                                                plain: true
                                            });
                                        } else if (data.status == 'backDateRequest') {
                                            ngDialog.open({
                                                template: 'Could not able to raise the back dated Request',
                                                plain: true
                                            });
                                        } else if (data.status == 'NOTVALIDLOCATIONID') {
                                            ngDialog.open({
                                                template: 'Location Id is Not valid ',
                                                plain: true
                                            });
                                        } else if (data.status == 'notValidApprovalManagerId') {
                                            ngDialog.open({
                                                template: 'Kinldy check Approval Manager Id ',
                                                plain: true
                                            });
                                        } else if (data.status == 'failed') {
                                            ngDialog.open({
                                                template: 'Request has been failed',
                                                plain: true
                                            });
                                        } else {
                                            ngDialog.open({
                                                template: 'Your request has been created successfully.',
                                                plain: true
                                            });
                                            $(".creatNewReqButton").addClass("disabled");
                                            $scope.initialzeNewCustomTimeByAdmin();
                                        }
                                    }


                                }).
                                error(function(data, status, headers, config) {
                                    // log error
                                });
                            }
                        } else {
                            if (!$scope.isLocationEntered && !$scope.isLocationEntered2) {
                                ngDialog.open({
                                    template: 'Please enter Pick/Drop Location.',
                                    plain: true
                                });
                            }
                        }
                    }
                    break;
            }
        };


        $scope.openMap = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdmin.location = x;
                $scope.newRequestByAdmin.cords = result.cords;
                $scope.isLocationEntered = true;
            });
        };

        $scope.openMap2 = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdmin.location2 = x;
                $scope.newRequestByAdmin.cords2 = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };


        $scope.origin = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.origin = x;
                $scope.newRequestByAdminAdhocManila.originLatiLogi = result.cords;
                $scope.isLocationEntered = true;
            });
        };

        $scope.finalDestination = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.finalDestination = x;
                $scope.newRequestByAdminAdhocManila.finalDestinationLatiLogi = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };

        $scope.subDestination1 = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.subDestination1 = x;
                $scope.newRequestByAdminAdhocManila.subDestination1LatiLogi = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };

        $scope.subDestination2 = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.subDestination2 = x;
                $scope.newRequestByAdminAdhocManila.subDestination2LatiLogi = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };

        $scope.subDestination3 = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.subDestination3 = x;
                $scope.newRequestByAdminAdhocManila.subDestination3LatiLogi = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };


        $scope.subDestination4 = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.subDestination4 = x;
                $scope.newRequestByAdminAdhocManila.subDestination4LatiLogi = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };

        $scope.subDestination5 = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/createRequestMapLocation.jsp',
                controller: 'creatRequestMapCtrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    officeLocation: function() {
                        return $scope.officeLocation;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                var x = document.getElementById("newAddress").value;
                $scope.newRequestByAdminAdhocManila.subDestination5 = x;
                $scope.newRequestByAdminAdhocManila.subDestination5LatiLogi = result.cords;
                $scope.isLocationEntered2 = true;
            });
        };

        $scope.checkIDExist = function() {};
    };


    var creatRequestMapCtrl = function($scope, $modalInstance, $state, $timeout, ngDialog, officeLocation) {
        $scope.mapIsLoaded = true;
        $scope.alertMessage;
        $scope.alertHint;
        $scope.user = {
            address: '',
            cords: '',
            search: ''
        };
        $scope.loc = {
            lat: officeLocation[0],
            lon: officeLocation[1]
        };
        $scope.geoCode = function(search) {
            if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
            this.geocoder.geocode({
                'address': $scope.user.search
            }, function(results, status) {

                if (status == google.maps.GeocoderStatus.OK) {
                    var loc = results[0].geometry.location;
                    $scope.search = results[0].formatted_address;
                    $scope.gotoLocation(loc.lat(), loc.lng());
                    $scope.user.cords = loc;
                    $scope.$apply(function() {
                        $scope.user.cords = loc.lat() + ',' + loc.lng();
                        $scope.user.address = $scope.search;
                    });
                } else {
                    ngDialog.open({
                        template: 'Sorry, this search produced no results.',
                        plain: true
                    });
                    $scope.$apply(function() {
                        $scope.user.cords = '';
                        $scope.user.address = '';
                    });
                }
            });
        };

        $scope.gotoLocation = function(lat, lon) {
            if ($scope.lat != lat || $scope.lon != lon) {
                $scope.loc = {
                    lat: lat,
                    lon: lon
                };
                if (!$scope.$$phase) $scope.$apply("loc");
            }
        };

        $scope.setPickDropLocation = function(user) {
            var address = document.getElementById("newAddress").value;

            ngDialog.open({
                template: 'Address has been updated successfully.',
                plain: true
            });
            $timeout(function() {
                $modalInstance.close(user);
                ngDialog.close();
            }, 3000);
        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addMultipleLocationCtrl = function($scope, $q, $modal, $rootScope, $modalInstance, $state, $timeout, ngDialog, $http) {
        $rootScope.destinationPush = false;
        $scope.locationDetailButtonDisabled = true;
        $scope.desinationDisabled = true;
        $scope.desinationDisabledButton = true;
        var saveAreaIdLocal = [];
        $scope.destinationArray = [];
        $scope.destinationPointsArray = [];
        $scope.destinationValuesArray = [];
        $scope.statusTrue = [];
        $scope.destinationLanLng = [];

        $rootScope.$on('deleteLocationList', function(event, data) {
            $scope.destinationPoints.splice(data - 1, 1);
            var data = $scope.destinationPoints;
            $scope.sendRecordToLocationDetails();

        });

        $scope.sendRecordToLocationDetails = function() {
            $rootScope.$emit('getCurrentDestnationPoints', $scope.destinationPoints);
        }

        if ($rootScope.result == undefined) {
            $scope.destinationPoints = [];
            $rootScope.destinationValue = [];
            $scope.destinationPoints.push({
                destination: ''
            });
        } else {
            $scope.destinationPoints = $rootScope.result;
            $rootScope.destinationPoints = $rootScope.result;
            $scope.origin = $rootScope.originAreaId;
        }

        $scope.$watch('origin', function(origin) {
            if (origin == undefined) {
                $scope.desinationDisabled = true;
                $scope.destinationAddButtonFlg = 'true';
                $scope.destinationDeleteButtonFlg = 'true';
            } else {
                $scope.desinationDisabled = false;
                if ($rootScope.locationVisible == 'AVU') {
                    $scope.destinationAddButtonFlg = 'true';
                    $scope.destinationDeleteButtonFlg = 'false';
                } else {
                    $scope.destinationAddButtonFlg = 'false';
                }
            }
        }, true);

        $scope.$watch('destinationPoints', function(destinationPoints) {

            $scope.statusTrue.splice(0, $scope.statusTrue.length);
            $scope.destinationPointsArray.splice(0, $scope.destinationPointsArray.length);
            $scope.destinationValuesArray.splice(0, $scope.destinationValuesArray.length);
            $scope.destinationLanLng.splice(0, $scope.destinationLanLng.length);
            $rootScope.destinationMultipleLocation = destinationPoints;
            angular.forEach($rootScope.destinationMultipleLocation, function(item) {
                if (_.isEmpty(item.destination)) {
                    return true;
                } else {
                    $scope.destinationValuesArray.push(item.destination);
                    $scope.destinationPointsArray.push(item.destination.areaId);
                    $scope.destinationLanLng.push(item.destination.locationLatLng);

                    if ($rootScope.originAreaId.locationLatLng == $scope.destinationLanLng[0]) {
                        $scope.desinationDisabledButton = true;
                        $scope.errorMessageOrigin = true;
                        $scope.errorMessageDestination = true;
                        ngDialog.open({
                            template: 'Origin and first destination should not be same. Kindly change other destination',
                            plain: true
                        });
                    } else {
                        $scope.errorMessageOrigin = false;
                        $scope.errorMessageDestination = false;
                    }

                    function pairwiseEach(arr, callback) {
                        arr.reduce(function(prev, current) {
                            callback(prev, current);
                            return current;
                        });
                    }

                    function pairwise(arr, callback) {
                        const result = []
                        arr.reduce(function(prev, current) {
                            result.push(callback(prev, current));
                            return current;
                        });
                        return result
                    }

                    const arr = $scope.destinationPointsArray;
                    pairwiseEach(arr, function(a, b) {
                        return console.log(a, b);
                    });
                    var result = pairwise(arr, function(a, b) {
                        return [a, b];
                    });

                    angular.forEach(result, function(value, key) {

                        $scope.statusTrue.push(value[0] == value[1]);
                        var checkStatusValues = _.some($scope.statusTrue);
                        $rootScope.checkStatusValues = checkStatusValues;
                        if (checkStatusValues == true) {
                            $scope.desinationDisabledButton = true;
                            $scope.errorMessageDestination = true;
                            $scope.errorMessageOrigin = false;
                        } else {
                            $scope.errorMessageDestination = false;
                            $scope.errorMessageOrigin = false;
                        }

                        if (value[0] == value[1]) {
                            $scope.areaId = value[1];
                            var areaNameSearch = _.where($scope.destinationValuesArray, {
                                areaId: value[1]
                            });
                            $scope.areaNameDetails = areaNameSearch[0].areaName;
                            ngDialog.open({
                                template: 'Duplicate Value not allowed to add. Kindly check' + ' " ' + areaNameSearch[0].areaName + ' " ' + 'Destination added two times continuously.',
                                plain: true
                            });
                            return false;
                        }
                    });
                    $scope.destinationArray.push(item.destination);
                }
            });

            $scope.destinationCountEmpty = [];
            $scope.getDestinationDetails = function(callback) {
                var deferred = $q.defer();
                var promise = deferred.promise;
                promise.then(function() {
                    angular.forEach(destinationPoints, function(item, key) {
                        $scope.destinationPointsLength = destinationPoints.length;
                        if (item.destination == null || _.isEmpty(item.destination) || $rootScope.checkStatusValues) {
                            $scope.desinationEmptyLength = key + 1;
                            $scope.destinationCountEmpty.push(item.destination);
                            $scope.desinationDisabledButton = true;
                            return false;
                        } else {
                            $scope.desinationDisabledButton = false;
                        }
                    });
                }).then(function() {
                    callback();
                });
                deferred.resolve();
            };
            $scope.getDestinationDetails(function() {
                if ($scope.destinationCountEmpty.length == 1) {
                    $scope.desinationDisabledButton = true;
                    $scope.destinationCountEmpty = 0;
                    return false;
                } else {}

            });

        }, true);

        $scope.allZonesData = [];

        $rootScope.locationLoad = function() {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                isActive: 'A',
                combinedFacility: combinedFacility
            };
            $http.post('services/employee/allActiveLocation/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.allZonesData = data.reverse();
                    $rootScope.homeLocationAreaId = $scope.allZonesData[0].areaId;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }
        $rootScope.locationLoad();

        $scope.setOriginSelected = function(origin) {
            if (origin == null) {
                $scope.desinationDisabled = true;
                $scope.desinationDisabledButton = true;
            } else {
                $scope.desinationDisabled = false;
                $scope.desinationDisabledButton = false;
            }
            $rootScope.origin = origin.locationLatLng;
            $rootScope.originAreaId = origin;
        }


        $scope.addDestination = function(value) {

            var destinationLength = value.length;
            if (destinationLength <= $rootScope.destinationPointLimit - 1) {
                angular.forEach(value, function(item) {
                    $rootScope.destinationValue.push(item.destination);
                });
                var areaId = _.pluck($rootScope.destinationValue, 'areaId');
                var lengthData = $rootScope.destinationValue.length - 1;
                for (var i = lengthData; i == lengthData; i++) {
                    if ($rootScope.destinationValue[i] === "") {
                        ngDialog.open({
                            template: 'Kindly select destination point.',
                            plain: true
                        });
                        return false;
                    } else {
                        $scope.destinationPoints.push({
                            destination: ''
                        });
                    }
                }
                return true;
            } else {
                ngDialog.open({
                    template: 'As per the transport policy, you cant create more than' + ' ' + $rootScope.destinationPointLimit + ' ' + 'destinationPoints',
                    plain: true
                });
                return false;
            }

        }

        $scope.onchangeDestination = function() {
            $scope.$watch('destinationPoints', function(newValue, oldValue) {
                    $scope.destinationLocationEmpty = [];
                    angular.forEach(newValue, function(item) {
                        $scope.destinationLocationEmpty.push(item.destination);
                    });
                    if (_.isEmpty($scope.destinationLocationEmpty)) {
                        $scope.locationDetailButtonDisabled = true;
                    } else {
                        $scope.locationDetailButtonDisabled = false;
                    }
                },
                true);
        }

        $scope.submitLocationDetails = function(destinationPoints) {
            $scope.destinationCountEmpty = [];
            $scope.getDestinationDetails = function(callback) {
                var deferred = $q.defer();
                var promise = deferred.promise;
                promise.then(function() {
                    angular.forEach(destinationPoints, function(item, key) {
                        if (item.destination == null || _.isEmpty(item.destination)) {
                            $scope.desinationEmptyLength = key + 1;
                            $scope.destinationCountEmpty.push(item.destination);
                            ngDialog.open({
                                template: 'Destination ' + $scope.desinationEmptyLength + ' is empty. Kindly select valid destination and try again...',
                                plain: true
                            });
                            return false;
                        }
                    });
                }).then(function() {
                    callback();
                });
                deferred.resolve();
            };
            $scope.getDestinationDetails(function() {
                if ($scope.destinationCountEmpty.length == 1) {
                    $scope.destinationCountEmpty = [];
                    return false;
                } else {
                    $modalInstance.close(destinationPoints);
                }

            });
        }

        $scope.resetLocationDetails = function(destinationPoints) {
            $scope.destinationPoints = [];
            $scope.destinationPoints.push({
                destination: ''
            });
        }

        $scope.setAreaSelected = function(destination, index) {
            $scope.onchangeDestination();
            var areaDuplicate = _.where($rootScope.destinationValue, destination);
            $rootScope.areaDuplicate = areaDuplicate.length;
            if (areaDuplicate.length == 0) {
                $rootScope.destinationPush = true;
                return true;
            } else {

            }
        }

        $rootScope.deleteDestinationLocation = function(destinationPoint, index) {
            if ($scope.destinationPoints.length == 1) {
                ngDialog.open({
                    template: 'As per the admin policy, you dont have access to delete this destination.',
                    plain: true
                });
                return false;
            } else {
                $scope.destinationPoints.splice(index, 1);
            }

        }

        $scope.checkOrginselected = function() {
            if ($rootScope.origin == undefined) {
                ngDialog.open({
                    template: 'Kindly choose origin',
                    plain: true
                });
                return false;
            } else {
                return true;
            }
        }

        $scope.addMapDestination = function(index, origin) {
            if ($rootScope.origin == undefined) {
                ngDialog.open({
                    template: 'Kindly choose origin',
                    plain: true
                });
                return false;
            }

            $scope.selectedIndex = index;
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/addMapDestinationModal.jsp',
                controller: 'mapDestinationCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    index: function() {
                        return index;
                    },
                    origin: function() {
                        return $rootScope.origin;
                    }

                }
            });

            modalInstance.result.then(function(result) {

                var newLocation = {
                    "areaName": result.areaName,
                    "locationLatLng": result.cords,
                    "locationAddress": result.address,
                }
                $scope.allZonesData.push(newLocation);
                $scope.destinationPoints[index].destination = {
                    "areaName": result.areaName,
                    "locationLatLng": result.cords,
                    "locationAddress": result.address,
                }

            })
        };

        $scope.previewLocation = function(destinationPoints, origin, callback) {
            $scope.destinationCountEmpty = [];
            $scope.getDestinationDetails = function(callback) {
                var deferred = $q.defer();
                var promise = deferred.promise;
                promise.then(function() {
                    angular.forEach(destinationPoints, function(item, key) {
                        if (item.destination == null || _.isEmpty(item.destination)) {
                            $scope.desinationEmptyLength = key + 1;
                            $scope.destinationCountEmpty.push(item.destination);
                            ngDialog.open({
                                template: 'Destination ' + $scope.desinationEmptyLength + ' is empty. Kindly select valid destination and try again...',
                                plain: true
                            });
                            return false;
                        }
                    });
                }).then(function() {
                    callback();
                });
                deferred.resolve();
            };
            $scope.getDestinationDetails(function() {
                if ($scope.destinationCountEmpty.length == 1) {
                    $scope.destinationCountEmpty = [];
                    return false;
                } else {
                    $scope.destinationArray = [];
                    angular.forEach(destinationPoints, function(item, key) {
                        if (_.isEmpty(item.destination)) {
                            return true;
                        } else {
                            $scope.destinationArray.push(item.destination);
                        }
                    });

                    var duplicateArray = _.pluck($scope.destinationArray, 'areaId');
                    var sorted_arr = duplicateArray.slice().sort();
                    var results = [];
                    for (var i = 0; i < duplicateArray.length - 1; i++) {
                        if (sorted_arr[i + 1] == sorted_arr[i]) {
                            results.push(sorted_arr[i]);
                        }
                    }

                    var previewData = _.pluck($scope.destinationArray, 'locationLatLng');
                    var splitArray = [];
                    for (var i = 0; i < previewData.length; i++) {
                        var lanLong = previewData[i].split(",");
                        var lan = lanLong[0];
                        var lon = lanLong[1];
                        splitArray.push(lan + ',' + lon + '|');
                    }
                    var route = {};
                    splitArray.unshift(origin.locationLatLng + '|');
                    route.waypoints = splitArray.join("");

                    if ($rootScope.destinationValue.length == 0) {
                        ngDialog.open({
                            template: 'Kindly choose Destination',
                            plain: true
                        });
                        return false;
                    }

                    route.baseLatLong = $rootScope.destinationValue[0].locationLatLng;
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/routeMapView.jsp',
                        controller: 'multiLocationMapViewCtrl',
                        size: 'lg',
                        backdrop: 'static',
                        resolve: {
                            waypoints: function() {
                                return route.waypoints;
                            },
                            baseLatLong: function() {
                                return route.baseLatLong;
                            }
                        }
                    });

                }

            });
        }

        $scope.selectedLocationDetails = function(destinationPoints, origin, callback) {
            $scope.destinationCountEmpty = [];
            $scope.getDestinationDetails = function(callback) {
                var deferred = $q.defer();
                var promise = deferred.promise;
                promise.then(function() {
                    angular.forEach(destinationPoints, function(item, key) {
                        if (item.destination == null || _.isEmpty(item.destination)) {
                            $scope.desinationEmptyLength = key + 1;
                            $scope.destinationCountEmpty.push(item.destination);
                            ngDialog.open({
                                template: 'Destination ' + $scope.desinationEmptyLength + ' is empty. Kindly select valid destination and try again...',
                                plain: true
                            });
                            return false;
                        }
                    });
                }).then(function() {
                    callback();
                });
                deferred.resolve();
            };
            $scope.getDestinationDetails(function() {
                if ($scope.destinationCountEmpty.length == 1) {
                    $scope.destinationCountEmpty = [];
                    return false;
                } else {
                    $scope.destinationArray = [];
                    angular.forEach(destinationPoints, function(item, key) {
                        if (_.isEmpty(item.destination)) {
                            return true;
                        } else {
                            $scope.destinationArray.push(item.destination);
                        }
                    });

                    var duplicateArray = _.pluck($scope.destinationArray, 'areaId');
                    var sorted_arr = duplicateArray.slice().sort();
                    var results = [];
                    for (var i = 0; i < duplicateArray.length - 1; i++) {
                        if (sorted_arr[i + 1] == sorted_arr[i]) {
                            results.push(sorted_arr[i]);
                        }
                    }

                    var previewData = _.pluck($scope.destinationArray, 'locationLatLng');
                    var splitArray = [];
                    for (var i = 0; i < previewData.length; i++) {
                        var lanLong = previewData[i].split(",");
                        var lan = lanLong[0];
                        var lon = lanLong[1];
                        splitArray.push(lan + ',' + lon + '|');
                    }
                    var route = {};
                    splitArray.unshift(origin.locationLatLng + '|');
                    route.waypoints = splitArray.join("");

                    if ($rootScope.destinationValue.length == 0) {
                        ngDialog.open({
                            template: 'Kindly choose Destination',
                            plain: true
                        });
                        return false;
                    }

                    route.baseLatLong = $rootScope.destinationValue[0].locationLatLng;
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/selectedLocationDetails.jsp',
                        controller: 'selectedLocationDetailsCtrl',
                        size: 'lg',
                        backdrop: 'static',
                        resolve: {
                            destinationPoints: function() {
                                return $scope.destinationArray;
                            },
                            origin: function() {
                                return origin;
                            }
                        }
                    });
                    modalInstance.result.then(function(index) {


                    });

                }

            });

        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var mapDestinationCtrl = function($scope, $rootScope, $modalInstance, $state, $timeout, ngDialog, $http, index, origin) {
        var latlong_center;
        var latLongValues = origin.split(',');
        latlong_center = latLongValues;
        var latValue =
            $scope.mapIsLoaded = true;
        $scope.loc = {
            lat: latLongValues[0],
            lon: latLongValues[1]
        }

        $scope.user = {};
        $scope.geoCode = function(search) {
            if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
            this.geocoder.geocode({
                'address': $scope.user.search
            }, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    var loc = results[0].geometry.location;
                    $scope.search = results[0].formatted_address;
                    $scope.gotoLocation(loc.lat(), loc.lng());
                    $scope.user.cords = loc;
                    $scope.$apply(function() {
                        $scope.user.cords = loc.lat() + ',' + loc.lng();
                        $scope.user.address = $scope.search;
                    });
                } else {
                    $scope.$apply(function() {
                        $scope.user.cords = '';
                        $scope.user.address = '';
                    });
                    ngDialog.open({
                        template: 'Sorry, this search produced no results.',
                        plain: true
                    });
                }
            });
        };

        $scope.updateDestination = function(user, update, loc, index) {
            combinedFacilityId = String($rootScope.combinedFacilityId);
            localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            if (combinedFacilityId == "undefined") {
                combinedFacilityId = branchId;
                localStorage.setItem("combinedFacilityIdAdhocRequest", combinedFacilityId);
            }

            user.index = $scope.index;
            if (userRole == 'admin' || userRole == 'superadmin') {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'A',
                    locationName: user.areaName,
                    locationAddress: user.address,
                    locationLatLng: user.cords,
                    combinedFacility: combinedFacilityId
                };
            } else { 
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'P',
                    locationName: user.areaName,
                    locationAddress: user.address,
                    locationLatLng: user.cords,
                    combinedFacility: combinedFacilityId
                };
            }

            $http.post('services/employee/addLocation/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    if (data.status == "alreadyExist") {
                        ngDialog.open({
                            template: 'Location Name Already Exists',
                            plain: true
                        });
                        return false;
                    } else {
                        ngDialog.open({
                            template: 'Location has been added Successfully',
                            plain: true
                        });
                        $rootScope.destinationPush = true;
                        $timeout(function() {
                            $modalInstance.close(user);
                            ngDialog.close();
                        }, 3000);
                        $rootScope.locationLoad();
                    }
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });



        }

        $scope.gotoLocation = function(lat, lon) {
            if ($scope.lat != lat || $scope.lon != lon) {
                $scope.loc = {
                    lat: lat,
                    lon: lon
                };
                if (!$scope.$$phase) $scope.$apply("loc");
            }
        };

        $scope.setPickDropLocation = function(user) {
            $modalInstance.close(user);
        }

        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var previewMapViewCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $rootScope, waypoints, baseLatLong) {
        $('.serviceMappingMenu').addClass('active');
        $scope.isloaded = false;
        $scope.singleServiceData;
        $scope.waypoints = waypoints;
        $scope.baseLatLong = baseLatLong;
        $scope.singleServiceData = [{
            'waypoints': $scope.waypoints,
            'baseLatLong': $scope.baseLatLong
        }];
        $scope.isloaded = true;

        $scope.getSingleServiceMap = function() {
            return $scope.singleServiceData;
        };

        $scope.backToServiceMapping = function() {
            $state.go('home.servicemapping');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


    };

    var selectedLocationDetailsCtrl = function($scope, $q, $modalInstance, $state, $http, $timeout, ngDialog, $rootScope, destinationPoints, origin) {
        $scope.statusTrue = [];
        var destinationLength = destinationPoints.length - 1;
        angular.forEach(destinationPoints, function(item, key) {
            if (destinationLength == 0) {
                item["buttonStatus"] = 'disabled';
            } else {
                item["buttonStatus"] = 'enabled';
            }
        });

        $scope.destinationPointsArrayData = [];
        angular.copy(destinationPoints, $scope.destinationPointsArrayData);

        $scope.destinationArray = $scope.destinationPointsArrayData;
        origin["buttonStatus"] = 'disabled';
        $scope.destinationArray.unshift(origin);

        var indexCount = [];
        $scope.removeLocationDetails = function(destination, index) {

            function pairwiseEach(arr, callback) {
                arr.reduce(function(prev, current) {
                    callback(prev, current);
                    return current;
                });
            }

            function pairwise(arr, callback) {
                const result = []
                arr.reduce(function(prev, current) {
                    result.push(callback(prev, current));
                    return current;
                });
                return result
            }

            const arr = $scope.destinationArray;
            pairwiseEach(arr, function(a, b) {
                return console.log(a, b);
            });
            var result = pairwise(arr, function(a, b) {
                return [a, b];
            });
            angular.forEach(result, function(value, key) {


                if (value[0] == value[1]) {
                    alert("true");
                    ngDialog.open({
                        template: 'Duplicate Value not allowed to add. Kindly check' + ' " ' + areaNameSearch[0].areaName + ' " ' + 'Destination added two times continuously.',
                        plain: true
                    });
                    return false;
                }
            });
            $scope.destinationArray.splice(index, 1);
            $scope.destinationCountEmpty = [];
            $scope.getDestinationDetails = function(callback) {
                var deferred = $q.defer();
                var promise = deferred.promise;
                promise.then(function() {
                    $scope.destinationArray.splice(index, 1);
                }).then(function() {
                    callback();
                });
                deferred.resolve();
            };
            $scope.getDestinationDetails(function() {
                $scope.destinationArray = [];
                $rootScope.$emit('deleteLocationList', index);
            });
        }

        $rootScope.$on('getCurrentDestnationPoints', function(event, data) {
            var destinationLength = data.length - 1;
            angular.forEach(data, function(item, key) {
                $scope.destinationArray.push(item.destination);
            });
            angular.forEach($scope.destinationArray, function(item, key) {
                if (destinationLength == 0) {
                    item["buttonStatus"] = 'disabled';
                } else {
                    item["buttonStatus"] = 'enabled';
                }
            });
            origin["buttonStatus"] = 'disabled';
            $scope.destinationArray.unshift(origin);
        });

        $scope.ok = function() {
            $modalInstance.close(indexCount);
        }

        $scope.cancel = function() {
            $modalInstance.close(indexCount);
        };

    };

    var multiLocationMapViewCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $rootScope, waypoints, baseLatLong) {

        $('.serviceMappingMenu').addClass('active');
        $scope.isloaded = false;
        $scope.singleServiceData;
        $scope.waypoints = waypoints;
        $scope.baseLatLong = baseLatLong;
        $scope.singleServiceData = [{
            'waypoints': $scope.waypoints,
            'baseLatLong': $scope.baseLatLong
        }];
        $scope.isloaded = true;

        $scope.getSingleServiceMap = function() {
            return $scope.singleServiceData;
        };

        $scope.backToServiceMapping = function() {
            $state.go('home.servicemapping');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


    };

    var rejectPendingRequestCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $rootScope, request) {

        $scope.addRemarks = function(value) {
            $modalInstance.close(value);
        }
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.close = function() {
            $modalInstance.close();
        };


    };



    angular.module('efmfmApp').controller('creatRequestMapCtrl', creatRequestMapCtrl);
    angular.module('efmfmApp').controller('cabRequestCtrl', cabRequestCtrl);
    angular.module('efmfmApp').controller('addMultipleLocationCtrl', addMultipleLocationCtrl);
    angular.module('efmfmApp').controller('mapDestinationCtrl', mapDestinationCtrl);
    angular.module('efmfmApp').controller('previewMapViewCtrl', previewMapViewCtrl);
    angular.module('efmfmApp').controller('selectedLocationDetailsCtrl', selectedLocationDetailsCtrl);
    angular.module('efmfmApp').controller('multiLocationMapViewCtrl', multiLocationMapViewCtrl);
    angular.module('efmfmApp').controller('rejectPendingRequestCtrl', rejectPendingRequestCtrl);


}());