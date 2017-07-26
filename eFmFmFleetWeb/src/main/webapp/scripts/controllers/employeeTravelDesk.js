	/*
	@date                   04/01/2015
	@Author                 Saima Aziz
	@Description
	@Main Controllers       empTravelDeskCtrl
	@Modal Controllers      singleMapCtrl. assignCabCtrl, editTravelDeskCtrl
	@template               partials/home.employeeTravelDesk.jsp

	CODE CHANGE HISTORY
	-----------------------------------------------------------------------------
	Date        Author          Changes 
	-----------------------------------------------------------------------------
	04/01/2015  Saima Aziz      Initial Creation
	04/15/2016  Saima Aziz      Final Creation
	 */
	(function() {
	    var empTravelDeskCtrl = function($scope, $rootScope, $http, $filter, $modal, $timeout,
	        $confirm, ngDialog, $state) {

	        if (!$scope.isEmployeeRosterActive ||
	            $scope.isEmployeeRosterActive == "false") {
	            $state.go('home.accessDenied');
	        } else {

	            $('.empTravelDeskMenu').addClass('active');
	            $('.empTravelDeskMenu').addClass('active');
	            $scope.isLoaded = false;
	            $scope.getTravelDeskButtonClicked = false;
	            $scope.getMultiTravelDeskButtonClicked = false;
	            $scope.currentPage = 1;
	            $scope.totalRecords = 0;
	            $scope.hint;
	            $scope.showSearchResultCount = false;
	            $scope.countofFilteredRecords;
	            $scope.showSelectShiftsCount = false;
	            $scope.showSelectShiftsRecords;
	            $scope.numOfRows = 0;
	            $scope.numberofRecords = 10000;
	            $scope.selectAllClicked = false;
	            $scope.deleteAllClicked = false;
	            $scope.search = {};
	            $scope.posts = [];
	            $scope.searchIsEmpty = false;
	            $scope.tripTypeEmployee = true;
	            $scope.shiftTimeEmployee = false;
	            $scope.setShiftButtonEmployee = false;
	            $scope.employeeRosterTable = false;
	            $scope.employeeRosterTableInit = false;
	            $scope.multiEmployeeRosterTableShift = false;
	            $scope.multiEmployeeRosterTableAllm = false;
	            $scope.multipleSearch = false;
	            $scope.adhocInitTable = false;
	            $scope.adhocTable = false;
	            $scope.adhocSearch = false;
	            $scope.multiEmployeeRosterTableAll = false;
	            $scope.excelButtonVisible = false;
	            $scope.manualRoutingFlg = 'manualRouting';
	            $scope.branchCode = branchCode;
	            $scope.locationVisible = locationVisible;
	            $scope.facilityData = [];
	            $scope.facilityDetails = userFacilities;
	            var array = JSON.parse("[" + combinedFacility + "]");
	            $scope.facilityData = array;
	            $scope.shiftsTimeData = {};
	            $scope.shiftsTimeData = [{
	                                    "shiftTime": "All",
	                                    "shiftId": "All"
	                                }];

	            var dataObj = {
	                eFmFmClientBranchPO: {
	                    branchId: branchId
	                },
	                userId: profileId,
	                combinedFacility: combinedFacility
	            };
	            $http.post('services/employee/AllEmployeeDetails/', dataObj).
	            success(function(data, status, headers, config) {
	                if (data.status != "invalidRequest") {
	                    $scope.employeeDetails = data;
	                }
	            }).
	            error(function(data, status, headers, config) {

	            });

	            $scope.paginations = [{
	                'value': 10,
	                'text': '10 records'
	            }, {
	                'value': 15,
	                'text': '15 record',
	            }, {
	                'value': 20,
	                'text': '20 records'
	            }];
	            $scope.shiftTimeDate = new Date();
	            $scope.shiftsTime = [];

	            $scope.tripTypes = [{
	                'value': 'BOTH',
	                'text': 'BOTH'
	            }, {
	                'value': 'PICKUP',
	                'text': 'PICKUP'
	            }, {
	                'value': 'DROP',
	                'text': 'DROP'
	            }];

	             $scope.tripTypeMultiTravel = $scope.tripTypes[0];

	            $scope.trip = {};
	            $scope.trip.tripType = $scope.tripTypes[0];

	            $scope.$on('$viewContentLoaded', function() {
	                $scope.getTravelDesk();
	            });

	            $scope.getshiftTimeDates = function() {
	                $scope.tripTypeEmployee = true;
	                $scope.employeeRosterTable = false;
	                $scope.employeeRosterTableInit = false;
	                $scope.excelButtonVisible = false;
	                $scope.multiEmployeeRosterTable = false;
	                $scope.multiEmployeeRosterTableAllm = false;
	                $scope.multipleSearch = false;
	                $scope.adhocInitTable = false;
	                $scope.adhocTable = false;
	                $scope.adhocSearch = false;
	                $scope.multiEmployeeRosterTableAll = false;
	                $scope.multiEmployeeRosterTableShift = false;
	                $scope.summaryLengthShift = 0;
	                $scope.totalRecordOfRoaster = 0;
	                $scope.summaryLengthMultiTravelRequest = 0;
	                $scope.summaryOfTotalMultiTravelRequest = 0;
	                $scope.summaryOfAdhocRequest = 0;
	                $scope.summaryOfTotalAdhocRequest = 0;
	            }

	            $scope.setShiftButton = function() {
	                $scope.setShiftButtonEmployee = true;
	                $scope.employeeRosterTable = false;
	                $scope.employeeRosterTableInit = false;
	                $scope.multiEmployeeRosterTable = false;
	                $scope.multiEmployeeRosterTableAllm = false;
	                $scope.multipleSearch = false;
	                $scope.adhocInitTable = false;
	                $scope.adhocTable = false;
	                $scope.adhocSearch = false;
	                $scope.multiEmployeeRosterTableAll = false;
	                $scope.multiEmployeeRosterTableShift = false;
	                $scope.summaryLengthShift = 0;
	                $scope.totalRecordOfRoaster = 0;
	                $scope.summaryLengthMultiTravelRequest = 0;
	                $scope.summaryOfTotalMultiTravelRequest = 0;
	                $scope.summaryOfAdhocRequest = 0;
	                $scope.summaryOfTotalAdhocRequest = 0;
	            }

	            $scope.setTripTypeliveTracking = function(tripType, facilityData) {

	                if (tripType) {

	                    $scope.shiftTimeEmployee = true;
	                    $scope.employeeRosterTable = false;
	                    $scope.multiEmployeeRosterTable = false;
	                    $scope.multiEmployeeRosterTableAll = false;
	                    $scope.multiEmployeeRosterTableAllm = false;
	                    $scope.multipleSearch = false;
	                    $scope.adhocInitTable = false;
	                    $scope.adhocTable = false;
	                    $scope.adhocSearch = false;
	                    $scope.multiEmployeeRosterTableShift = false;
	                    $scope.employeeRosterTableInit = false;
	                    $scope.summaryLengthShift = 0;
	                    $scope.totalRecordOfRoaster = 0;
	                    $scope.summaryLengthMultiTravelRequest = 0;
	                	$scope.summaryOfTotalMultiTravelRequest = 0;
	                	$scope.summaryOfAdhocRequest = 0;
	                	$scope.summaryOfTotalAdhocRequest = 0;

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
	                        tripType: tripType.value,
	                        userId: profileId
	                        // combinedFacility: facilityData.toString()
	                    };
	                    if (facilityData) {
	                    	data.combinedFacility = facilityData.toString();
	                    } 
	                    $http.post('services/trip/tripshiftime/', data).success(
	                        function(data, status, headers, config) {
	                            if (data.status != "invalidRequest") {
	                                $scope.shiftsTimeData = _.uniq(data.shift, function(p){ return p.shiftTime; });
	                                $scope.shiftsTimeData.unshift({
	                                    "shiftTime": "All",
	                                    "shiftId": "All"
	                                });

	                            }

	                        }).error(function(data, status, headers, config) {});
	                }
	            };

	            $scope.shiftTimeDateCal = function($event) {
	                $event.preventDefault();
	                $event.stopPropagation();
	                $timeout(function() {
	                    $scope.datePicker = {
	                        'shiftTimeDate': true
	                    };
	                }, 50);
	            };
	            $scope.postsInit = [];
	            $scope.fetchinggetTravelDesk = false;
	            $scope.getTravelDesk = function() {
	            	if ($scope.fetchinggetTravelDesk) return;
	            	if ($scope.postsInitLength == undefined || $scope.postsInitLength == 0) {
	                    var startPgNoCount = 0;
	                } else {
	                    var startPgNoCount = $scope.postsInitLength;
	                }
	                $scope.isLoaded = false;
	                var data = {
	                    eFmFmEmployeeRequestMaster: {
	                        efmFmUserMaster: {
	                            eFmFmClientBranchPO: {
	                                branchId: branchId
	                            }
	                        }
	                    },
	                    userRole: userRole,
	                    efmFmUserMaster: {
	                        userId: profileId
	                    },
	                    userId: profileId,
	                    combinedFacility: combinedFacility,
		                startPgNo: startPgNoCount,
		                endPgNo: webPageCount
	                };
	                if (!$scope.fetchinggetTravelDesk) {
                    $scope.fetchinggetTravelDesk = true;
	                $http.post('services/trip/employeeTravelRequest/', data)
	                    .success(function(data, status, headers, config) {
	                    	$scope.fetchinggetTravelDesk = false;
	                            if (data.status != "invalidRequest") {
	                                $scope.OriginalPostsData = data.requests;
	                                $scope.shiftData = data.shifts;
	                                angular.forEach($scope.shiftData, function(item) {
	                                    $scope.shiftsTime.push(item.shiftTime);
	                                });
	                                if (data.requests.length > 0) {
	                                    $scope.isLoaded = true;
	                                    $scope.employeeRosterTableInit = true;
	                                    $scope.employeeRosterTable = false;
	                                   	$scope.multiEmployeeRosterTableShift = false;
	                                    angular.forEach(data.requests, function(item) {
	                                    	$scope.postsInit.push(item);
	                                                item.checkBoxFlag = false;
	                                                item.isUpdateClicked = false;
	                                                item.createNewAdHocTime = $scope
	                                                    .initializeTime(item.tripTime);
	                                            });
	                                $scope.summaryLengthShift = $scope.postsInit.length;
	                                $scope.totalRecordOfRoaster = $scope.postsInit[0].totalRecordcount;
	                                $scope.postsInitLength = $scope.postsInit.length;
	                                } else {
	                                    $scope.isLoaded = false;
	                                }
	                            }
	                        }).error(
	                        function(data, status, headers, config) {
	                            $scope.isLoaded = false;
	                        });
	            } else {
                    return;
                }
	            };
	            $scope.postsAllMInit = [];
	            $scope.fetchingsetShiftsAllM = false;
	            $scope.getMultiTravelRequest = function() {
	            	$scope.shiftTimeDateMultiTravel = new Date();
	            	if ($scope.fetchingsetShiftsAllM) return;
	            	if ($scope.postsAllMInitLength == undefined || $scope.postsAllMInitLength == 0) {
	                    var startPgNoCount = 0;
	                } else {
	                    var startPgNoCount = $scope.postsAllMInitLength;
	                }
	                $scope.getMultiTravelDeskButtonClicked = true;
	                $scope.isLoaded = false;
	                var data = {
	                    eFmFmEmployeeRequestMaster: {
	                        efmFmUserMaster: {
	                            eFmFmClientBranchPO: {
	                                branchId: branchId
	                            }
	                        }
	                    },
	                    userRole: userRole,
	                    efmFmUserMaster: {
	                        userId: profileId
	                    },
	                    userId: profileId,
	                    locationFlg: 'M',
	                    typeExecution: 'all',
	                    combinedFacility: combinedFacility,
		                startPgNo: startPgNoCount,
		                endPgNo: webPageCount
	                };
	                if (!$scope.fetchingsetShiftsAllM) {
                    $scope.fetchingsetShiftsAllM = true;
	                $http
	                    .post('services/trip/employeeMultipleTravelRequest/', data)
	                    .success(
	                        function(data, status, headers, config) {
	                            if (data.status != "invalidRequest") {
	                        		$scope.fetchingsetShiftsAllM = false;
	                                $scope.multiEmployeeRosterTable = true;
	                                $scope.OriginalPostsData = data.requests;
	                                $scope.shiftData = data.shifts;
	                                $scope.getMultiTravelDeskButtonClicked = false;
	                                angular.forEach($scope.shiftData, function(
	                                    item) {
	                                    $scope.shiftsTime.push(item.shiftTime);
	                                });

	                                if (data.requests.length > 0) {
	                                	$scope.multiEmployeeRosterTableAll = true;
	                                	$scope.multiEmployeeRosterTableAllm = false;
										$scope.multipleSearch = false;
	                                    $scope.isLoaded = true;
	                                    angular.forEach(data.requests, function(item) {
	                                    	 $scope.postsAllMInit.push(item);
	                                                item.checkBoxFlag = false;
	                                                item.isUpdateClicked = false;
	                                                item.createNewAdHocTime = $scope
	                                                    .initializeTime(item.tripTime);
	                                            });
	                                $scope.postsAllMInitLength = $scope.postsAllMInit.length;
	                                $scope.summaryLengthMultiTravelRequest = $scope.postsAllMInit.length;
	                                $scope.summaryOfTotalMultiTravelRequest = $scope.postsAllMInit[0].totalRecordcount;
	                                } else {
	                                    $scope.isLoaded = false;
	                                }
	                                $scope.getMultiTravelDeskButtonClicked = false;
	                            } else{
	                            	setTimeout(function() {
	                        		$scope.fetchingsetShiftsAllM = false;
	                            	}, 100);
	                            }
	                        }).error(
	                        function(data, status, headers, config) {
	                            $scope.isLoaded = false;
	                            // log error
	                        });
	                        } else {
			                    return;
			                }
	            };
	            $scope.saveAsaExcel = function(shiftTimeDate, tripType, shiftTimes, employeeId) {

	                if ($scope.multiLocationSearchById) {
	                    var data = {
	                        branchId: branchId,
	                        employeeId: employeeId,
	                        userId: profileId,
	                        locationFlg: 'M'
	                    };
	                } else {
	                    var data = {
	                        eFmFmEmployeeRequestMaster: {
	                            efmFmUserMaster: {
	                                eFmFmClientBranchPO: {
	                                    branchId: branchId
	                                }
	                            }
	                        },
	                        userRole: userRole,
	                        efmFmUserMaster: {
	                            userId: profileId
	                        },
	                        time: shiftTimes.shiftTime,
	                        resheduleDate: $scope.convertDateUTC(shiftTimeDate),
	                        tripType: tripType.text,
	                        userId: profileId,
	                        locationFlg: 'M',
	                        typeExecution: 'search'
	                    };
	                }

	                $http({
	                    url: 'services/trip/shiftRosterForMultipleLocation/',
	                    method: "POST",
	                    data: data,
	                    headers: {
	                        'Content-type': 'application/json'
	                    },
	                    responseType: 'arraybuffer'
	                }).success(function(data, status, headers, config) {
	                    var blob = new Blob([data], {});

	                    saveAs(blob, 'Employee Multi Travel Request' + '.xlsx');
	                }).error(function(data, status, headers, config) {
	                    alert("Download Failed")
	                });

	            };
	            $scope.postMultiple = [];
	            $scope.fetchingMultiple = false;
	            $scope.setShiftsMultiTravelRequest = function(shiftTimeDate, tripType, shiftTimes, startPage) {

	            	if ($scope.fetchingMultiple) return;
	            	if ($scope.postMultipleLength == undefined || $scope.postMultipleLength == 0 || startPage) {
	                    var startPgNoCount = 0;
	                    $scope.postMultiple = [];
	                } else {
	                    var startPgNoCount = $scope.postMultipleLength;
	                }
	                $scope.multiEmployeeRosterTable = true;
	                $scope.getMultiTravelDeskButtonClicked = true;
	                $scope.isLoaded = false;

	                if (shiftTimes) {
	                    var data = {
	                        eFmFmEmployeeRequestMaster: {
	                            efmFmUserMaster: {
	                                eFmFmClientBranchPO: {
	                                    branchId: branchId
	                                }
	                            }
	                        },
	                        userRole: userRole,
	                        efmFmUserMaster: {
	                            userId: profileId
	                        },
	                        time: shiftTimes.shiftTime,
	                        resheduleDate: $scope.convertDateUTC(shiftTimeDate),
	                        tripType: tripType.text,
	                        userId: profileId,
	                        locationFlg: 'M',
	                        typeExecution: 'search',
	                        combinedFacility: combinedFacility,
		                    startPgNo: startPgNoCount,
		                    endPgNo: webPageCount
	                    };
	                    if (!$scope.fetchingMultiple) {
                    	$scope.fetchingMultiple = true;
	                    $http
	                        .post('services/trip/employeeMultipleTravelRequest/', data)
	                        .success(
	                            function(data, status, headers, config) {
	                                if (data.status != "invalidRequest") {
	                            	$scope.fetchingMultiple = false;
	                                	if (startPgNoCount == 0) {
	                                		if (data.requests.length == 0) {
	                                			ngDialog.open({
	                                                template: 'No Data Found. Please try again.',
	                                                plain: true
	                                            });
	                                		}
	                                	}
                            		if (data.requests.length == 0) return;
	                                    $scope.multiLocationSearchById = false;
	                                    $scope.getMultiTravelDeskButtonClicked = false;
	                                    $scope.multiEmployeeRosterTable = true;
	                                    $scope.excelButtonVisible = true;
	                                    if (data.requests > 0) {
	                                    $scope.multiEmployeeRosterTableAll = false;
	                                	$scope.multiEmployeeRosterTableAllm = true;
										$scope.multipleSearch = false;
	                                        $scope.excelButtonVisible = true;
	                                        $scope.isLoaded = true;
	                                        angular
	                                            .forEach(
	                                                $scope.posts,
	                                                function(item) {
	                                                $scope.postMultiple.push(item);	
	                                                    item.checkBoxFlag = false;
	                                                    item.isUpdateClicked = false;
	                                                    if (item.tripType == "PICKUP") {
	                                                        item.createNewAdHocTime = $scope
	                                                            .initializeTime(item.tripTime);
	                                                    }
	                                                });
	                                    $scope.summaryLengthMultiTravelRequest = $scope.postMultiple.length;
	                               		$scope.summaryOfTotalMultiTravelRequest = $scope.postMultiple[0].totalRecordcount;
	                                    $scope.postMultipleLength =  $scope.postMultiple.length;
	                                    } else {
	                                        $scope.isLoaded = false;
	                                        $scope.getMultiTravelDeskButtonClicked = false;
	                                        ngDialog
	                                            .open({
	                                                template: 'No Data Found. Please try again.',
	                                                plain: true
	                                            });

	                                    }
	                                } else {
	                            	setTimeout(function() {
	                        		$scope.fetchingMultiple = false;
	                            	}, 100);
	                            }
	                            }).error(
	                            function(data, status, headers, config) {
	                                $scope.isLoaded = false;
	                            });
	                             } else {
                    return;
                }
	                } else {
	                    $scope.posts = $scope.OriginalPostsData;
	                }

	            };


	            $scope.multiLocationSearchById = false;
	            $scope.searchMultipleLocation = function(searchText) {
	                if (searchText) {
	                    $scope.searchIsEmpty = false;
	                    var dataObj = {
	                        branchId: branchId,
	                        employeeId: searchText,
	                        userId: profileId,
	                        locationFlg: 'M',
	                        combinedFacility: combinedFacility
	                    };
	                    $http
	                        .post('services/trip/emplyeerequestsearch/', dataObj)
	                        .success(
	                            function(data, status, headers, config) {
	                                if (data.status != "invalidRequest") {
	                                    $scope.multiLocationSearchById = true;
	                                    if (data.requests.length == 0) {
	                                        $scope.posts = data.requests;
	                                        $scope.multipleSearch = false;
	                                        ngDialog
	                                            .open({
	                                                template: 'Request not exist for this employeeId,Please check employeeId.',
	                                                plain: true
	                                            });

	                                    } else {
	                                        $scope.summaryLengthMultiTravelRequest = data.requests.length;
	                               			$scope.summaryOfTotalMultiTravelRequest = data.requests.length;
	                                        $scope.posts = data.requests;
	                                        $scope.excelButtonVisible = true;
	                                        $scope.multipleSearch = true;
	                                        $scope.multiEmployeeRosterTableAll = false;
											$scope.multiEmployeeRosterTableAllm = false;
	                                        angular
	                                            .forEach(
	                                                $scope.posts,
	                                                function(item) {
	                                                    item.checkBoxFlag = false;
	                                                });
	                                        $scope.progressbar.complete();
	                                        $scope.isLoaded = true;
	                                    }
	                                }
	                            }).error(
	                            function(data, status, headers, config) {
	                                // log error
	                            });
	                } else {
	                    $scope.searchIsEmpty = true;
	                }
	            };
	            $scope.postsAdhocInit = [];
	            $scope.fetchingsetAdhocInit = false;
	            $scope.getTravelDeskAdhoc = function() {
	            	console.log($scope.fetchingsetAdhocInit)
	            	if ($scope.fetchingsetAdhocInit) return;
	            	if ($scope.postsAdhocInitLength == undefined || $scope.postsAdhocInitLength == 0) {
	                    var startPgNoCount = 0;
	                } else {
	                    var startPgNoCount = $scope.postsAdhocInitLength;
	                }
	                // $scope.getTravelDeskButtonClicked = false;
	                $scope.isLoaded = false;
	                $scope.shiftTimeDateAdhoc =  new Date();

	                var data = {
	                    eFmFmEmployeeRequestMaster: {
	                        efmFmUserMaster: {
	                            eFmFmClientBranchPO: {
	                                branchId: branchId
	                            }
	                        }
	                    },
	                    userRole: userRole,
	                    efmFmUserMaster: {
	                        userId: profileId
	                    },
	                    userId: profileId,
	                    combinedFacility: combinedFacility,
		                startPgNo: startPgNoCount,
		                endPgNo: webPageCount
	                };
	                if (!$scope.fetchingsetAdhocInit) {
                    $scope.fetchingsetAdhocInit = true;
	                $http
	                    .post('services/adhoc/employeeAdhocRoster/', data)
	                    .success(
	                        function(data, status, headers, config) {
	                        	$scope.fetchingsetAdhocInit = false;
	                            if (data.status != "invalidRequest") {
	                            	
	                                $scope.OriginalPostsData = data.requests;
	                                $scope.posts = data.requests;
	                                $rootScope.$on('rejectAchoc', function(event, dataIndex) {
	                                    $scope.posts.splice(dataIndex, 1);
	                                });

	                                $scope.shiftData = data.shifts;

	                                angular.forEach($scope.shiftData, function(
	                                    item) {
	                                    $scope.shiftsTime.push(item.shiftTime);
	                                });

	                                if (data.requests.length > 0) {
	                                	$scope.adhocInitTable = true;
	                                	$scope.adhocTable = false;
										$scope.adhocSearch = false;
	                                    $scope.isLoaded = true;
	                                    angular
	                                        .forEach(
	                                            data.requests,
	                                            function(item) {
	                                            	$scope.postsAdhocInit.push(item);
	                                                item.checkBoxFlag = false;
	                                                item.isUpdateClicked = false;
	                                                item.createNewAdHocTime = $scope
	                                                    .initializeTime(item.tripTime);
	                                            });
	                                $scope.postsAdhocInitLength = $scope.postsAdhocInit.length;
	                                $scope.summaryOfAdhocRequest = $scope.postsAdhocInit.length;
	                				$scope.summaryOfTotalAdhocRequest = $scope.postsAdhocInit[0].totalRecordcount;
	                                } else {
	                                    $scope.isLoaded = false;
	                                    $scope.adhocInitTable = false;
	                                }
	                            }

	                        }).error(
	                        function(data, status, headers, config) {
	                            $scope.isLoaded = false;
	                            // log error
	                        });
	                        } else {
			                    return;
			                }
	            };

	            $scope.achocRequestAccept = function(post, index) {

	                var data = {
	                    branchId: branchId,
	                    requestId: post.requestId,
	                    userId: profileId,
	                    combinedFacility: combinedFacility
	                };

	                $http.post('services/adhoc/employeeRequestAcceptance/', data).
	                success(function(data, status, headers, config) {
	                    if (data.status != "invalidRequest") {
	                        $scope.posts[index].disabled = true;
	                        ngDialog
	                            .open({
	                                template: 'Request Accepted Successfully',
	                                plain: true
	                            });
	                    }

	                }).
	                error(function(data, status, headers, config) {
	                    // log error
	                });
	            }

	            $scope.achocRequestReject = function(post, index) {

	                var modalInstance = $modal.open({
	                    templateUrl: 'partials/modals/achocRequestRejectRemarks.jsp',
	                    controller: 'achocRequestRejectCtrl',
	                    size: 'sm',
	                    resolve: {
	                        post: function() {
	                            return post;
	                        },
	                        index: function() {
	                            return index;
	                        }
	                    }
	                });

	            }

	            $scope.saveInExcel = function(shiftTimeDate, tripType, shiftTimes, employeeId, facilityDetails) {
	                if (facilityDetails == undefined || facilityDetails.length == 0) {
	                    facilityDetails = facilityDetails;
	                } else {
	                    facilityDetails = String(facilityDetails);
	                }

	                if ($scope.searchByidExel) {
	                    var dataObj = {
	                        branchId: branchId,
	                        employeeId: employeeId,
	                        userId: profileId,
	                        locationFlg: 'N',
	                        combinedFacility: String(facilityDetails)
	                    };
	                } else {
	                    var dataObj = {
	                        eFmFmEmployeeRequestMaster: {
	                            efmFmUserMaster: {
	                                eFmFmClientBranchPO: {
	                                    branchId: branchId
	                                }
	                            }
	                        },
	                        userRole: userRole,
	                        efmFmUserMaster: {
	                            userId: profileId
	                        },
	                        userId: profileId,
	                        time: shiftTimes.shiftTime,
	                        resheduleDate: $scope.convertDateUTC(shiftTimeDate),
	                        tripType: tripType.text,
	                        combinedFacility: String(facilityDetails)
	                    };
	                }

	                $http({
	                    url: 'services/trip/shiftRoster/',
	                    method: "POST",
	                    data: dataObj,
	                    headers: {
	                        'Content-type': 'application/json'
	                    },
	                    responseType: 'arraybuffer'
	                }).success(function(data, status, headers, config) {
	                    var blob = new Blob([data], {});

	                    saveAs(blob, 'Employee Shift Roster' + '.xlsx');
	                }).error(function(data, status, headers, config) {
	                    alert("Download Failed")
	                });
	            };

	            //ACTION :: Called on ng-change of the Show Record Drop Down :: Set the limits of Records to show
	            $scope.setLimit = function(showRecords) {
	                if (!showRecords) {
	                    $scope.numberofRecords = $scope.posts.length;
	                } else
	                    $scope.numberofRecords = showRecords.value;
	            };

	            $scope.deleteEmployeeDetails = function(shiftTimeDate, tripType, shiftTimes, employeeId, combinedFacilityId) {
	                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
	                    facilityDetails = facilityDetails;
	                } else {
	                    combinedFacilityId = String(combinedFacilityId);
	                }

	                $confirm({
	                        text: 'Are you sure you want to delete the all employees from selected shift?',
	                        title: 'Delete Confirmation',
	                        ok: 'Yes',
	                        cancel: 'No'
	                    })
	                    .then(
	                        function() {
	                            if ($scope.searchByidExel) {
	                                var dataObj = {
	                                    eFmFmEmployeeRequestMaster: {
	                                        efmFmUserMaster: {
	                                            eFmFmClientBranchPO: {
	                                                branchId: branchId
	                                            }
	                                        }
	                                    },
	                                    userRole: userRole,
	                                    efmFmUserMaster: {
	                                        userId: profileId
	                                    },
	                                    employeeId: employeeId,
	                                    locationFlg: 'N',
	                                    combinedFacility: combinedFacilityId
	                                };

	                            } else {
	                                var dataObj = {
	                                    eFmFmEmployeeRequestMaster: {
	                                        efmFmUserMaster: {
	                                            eFmFmClientBranchPO: {
	                                                branchId: branchId
	                                            }
	                                        }
	                                    },
	                                    userRole: userRole,
	                                    efmFmUserMaster: {
	                                        userId: profileId
	                                    },
	                                    time: shiftTimes.shiftTime,
	                                    resheduleDate: $scope
	                                        .convertDateUTC(shiftTimeDate),
	                                    tripType: tripType.text,
	                                    combinedFacility: combinedFacilityId
	                                };
	                            }

	                            $http.post('services/trip/deleteShiftRequest/', dataObj)
	                                .success(function(data, status, headers, config) {
	                                    if (data.status != "invalidRequest") {
	                                        if (data.status == "success") {
	                                            ngDialog
	                                                .open({
	                                                    template: ' Employee details has been deleted successfully.',
	                                                    plain: true
	                                                });

	                                            $scope
	                                                .getTravelDesk();
	                                        }
	                                    }
	                                }).error(
	                                    function(data, status,
	                                        headers, config) {
	                                        // log error
	                                    });
	                        });

	            }
	            $scope.postShift = [];
				$scope.fetchingsetShifts = false;
	            $scope.setShifts = function(shiftTimeDate, tripType, shiftTimes, facilityDetails, startPage) {	            	
	            	if ($scope.fetchingsetShifts) return;

	            	if ($scope.postsLength == undefined || $scope.postsLength == 0 || startPage) {
	                    var startPgNoCount = 0;
	                    $scope.postShift = [];
	                } else {
	                    var startPgNoCount = $scope.postsLength;
	                }
	                if (facilityDetails == undefined || facilityDetails.length == 0) {
	                    facilityDetails = combinedFacility;
	                } else {
	                    facilityDetails = String(facilityDetails);
	                }
	                // $scope.getTravelDeskButtonClicked = true;
	                $scope.isLoaded = false;

	                if (shiftTimes) {
	                    var data = {
	                        eFmFmEmployeeRequestMaster: {
	                            efmFmUserMaster: {
	                                eFmFmClientBranchPO: {
	                                    branchId: branchId
	                                }
	                            }
	                        },
	                        userRole: userRole,
	                        efmFmUserMaster: {
	                            userId: profileId
	                        },
	                        time: shiftTimes.shiftTime,
	                        resheduleDate: $scope.convertDateUTC(shiftTimeDate),
	                        tripType: tripType.text,
	                        userId: profileId,
	                        combinedFacility: String(facilityDetails),
		                    startPgNo: startPgNoCount,
		                    endPgNo: webPageCount
	                    };
	                    if (!$scope.fetchingsetShifts) {
                    $scope.fetchingsetShifts = true;
	                    $http.post('services/trip/employeeshiftwiserequest/', data)
	                        .success(function(data, status, headers, config) {
	                            if (data.status != "invalidRequest") {	                            		                        	
	                            	$scope.fetchingsetShifts = false;
	                            	if (startPgNoCount == 0) {
	                                		if (data.requests.length == 0) {
	                                			ngDialog.open({
	                                                template: 'No Data Found. Please try again.',
	                                                plain: true
	                                            });
	                                		}
	                                	}
                            		if (data.requests.length == 0) return;
                            
	                                $scope.searchByidExel = false;
	                                if (data.requests.length > 0) {
	                                    $scope.excelButtonVisible = true;
	                                    $scope.multiEmployeeRosterTableShift = true;
	                                    $scope.employeeRosterTable = false;
	                                    $scope.employeeRosterTableInit = false;
	                                    $scope.isLoaded = true;
	                                    angular
	                                        .forEach(
	                                            data.requests,
	                                            function(item) {
	                                            	$scope.postShift.push(item);
	                                                item.checkBoxFlag = false;
	                                                item.isUpdateClicked = false;
	                                                if (item.tripType == "PICKUP") {
	                                                    item.createNewAdHocTime = $scope
	                                                        .initializeTime(item.tripTime);
	                                                }
	                                            });
	                                $scope.totalRecordOfRoaster = $scope.postShift[0].totalRecordcount;
	                                $scope.postsLength = $scope.postShift.length; 
	                                $scope.summaryLengthShift = $scope.postShift.length;       
	                                } else {
	                                    $scope.excelButtonVisible = false;
	                                    $scope.multiEmployeeRosterTableShift = false;
	                                    $scope.isLoaded = false;
	                                    // $scope.postShift = [];
	                                    // $scope.getTravelDeskButtonClicked = false;
	                                    ngDialog
	                                        .open({
	                                            template: 'No Data Found. Please try again.',
	                                            plain: true
	                                        });

	                                }
	                            } else{
	                            	setTimeout(function() {
	                            		$scope.fetchingsetShifts = false;
	                            	}, 100);
	                            }
	                        }).error(
	                            function(data, status, headers, config) {
	                                $scope.isLoaded = false;
	                            });
	                                        } else {
                    return;
                }
	                } else {
	                    $scope.posts = $scope.OriginalPostsData;
	                }

	            };
	            $scope.postsAdhoc = [];
	            $scope.fetchingAdhoc = false;
	            $scope.setShiftsAdhoc = function(shiftTimes, date, tripType, startPage) {
	            	console.log($scope.fetchingAdhoc)
				if ($scope.fetchingAdhoc) return;
	            	if ($scope.postsAdhocLength == undefined || $scope.postsAdhocLength == 0 || startPage) {
	                    var startPgNoCount = 0;
	                    $scope.postsAdhoc = [];
	                } else {
	                    var startPgNoCount = $scope.postsAdhocLength;
	                }
	            	if(tripType.text = 'BOTH'){
	            		//tripType.text = 'All';
	            		shiftTimes = 'All';
	            	}

	                // $scope.getTravelDeskButtonClicked = true;
	                if (shiftTimes) {
	                    var data = {
	                        eFmFmEmployeeRequestMaster: {
	                            efmFmUserMaster: {
	                                eFmFmClientBranchPO: {
	                                    branchId: branchId
	                                }
	                            }
	                        },
	                        userRole: userRole,
	                        efmFmUserMaster: {
	                            userId: profileId
	                        },
	                        time: shiftTimes,
	                        tripType: tripType.text,
	                        resheduleDate: $scope.convertDateUTC(date),
	                        userId: profileId,
	                        combinedFacility: combinedFacility,
			                startPgNo: startPgNoCount,
			                endPgNo: webPageCount
		                };
		                if (!$scope.fetchingAdhoc) {
	                    $scope.fetchingAdhoc = true;
	                    $http
	                        .post('services/trip/employeeshiftwiserequest/',
	                            data)
	                        .success(
	                            function(data, status, headers, config) {
	                                if (data.status != "invalidRequest") {	                                	
	                            		$scope.fetchingAdhoc = false;
	                                	if (startPgNoCount == 0) {
	                                		if (data.requests.length == 0) {
	                                			ngDialog.open({
	                                                template: 'No Data Found. Please try again.',
	                                                plain: true
	                                            });
	                                		}
	                                	}
	                                	if (data.requests.length == 0) return;
	                                    if (data.requests.length > 0) {
	                                        // $scope.getTravelDeskButtonClicked = false;
	                                        $scope.isLoaded = true;
	                                        angular
	                                            .forEach(
	                                                data.requests,
	                                                function(item) {
	                                                	$scope.postsAdhoc.push(item);
	                                                    item.checkBoxFlag = false;
	                                                    item.isUpdateClicked = false;
	                                                    if (item.tripType == "PICKUP") {
	                                                        item.createNewAdHocTime = $scope
	                                                            .initializeTime(item.tripTime);
	                                                    }
	                                                });
	                                $scope.postsAdhocLength = $scope.postsAdhoc.length;
	                                $scope.summaryOfAdhocRequest = $scope.postsAdhoc.length;
	                				$scope.summaryOfTotalAdhocRequest = $scope.postsAdhoc[0].totalRecordcount;
	                                $scope.adhocInitTable = false;
									$scope.adhocTable = true;
									$scope.adhocSearch = false;
	                                    } else {
	                                        // $scope.getTravelDeskButtonClicked = false;
	                                        $scope.adhocTable = false;
	                                        $scope.isLoaded = false;
	                                        ngDialog
	                                            .open({
	                                                template: 'No Data Found. Please try again.',
	                                                plain: true
	                                            });

	                                    }
	                                } else{
	                            	setTimeout(function() {
	                            	$scope.fetchingAdhoc = false;
	                            	}, 100);
	                            }
	                            }).error(
	                            function(data, status, headers, config) {
	                                $scope.isLoaded = false;
	                                // $scope.getTravelDeskButtonClicked = false;
	                            });
	                            } else {
			                    return;
			                }
	                } else {
	                    $scope.posts = $scope.OriginalPostsData;
	                }
	                // $scope.getTravelDeskButtonClicked = false;
	            };

	            //ACTION :: SelectAll Check Box is Clicked
	            $scope.selectAll = function() {
	                if (!$scope.selectAllClicked) {
	                    $scope.selectAllClicked = true;
	                    angular.forEach($scope.posts, function(item) {
	                        item.checkBoxFlag = true;
	                    });
	                } else {
	                    $scope.selectAllClicked = false;
	                    angular.forEach($scope.posts, function(item) {
	                        item.checkBoxFlag = false;
	                    });
	                }
	            };

	            //ACTION :: Single Check Box Clicked
	            $scope.select_thisEmployee = function(post) {
	                if (!post.checkBoxFlag) {
	                    post.checkBoxFlag = true;
	                } else {
	                    post.checkBoxFlag = false;
	                    $scope.selectAllClicked = false;
	                    $scope.deleteAllClicked = false;
	                }
	            };

	            //BUTTON ACTION :: EMPLOYEE REPORTED AT SERVICE DESK
	            $scope.employeeReported = function() {
	                var checkedEmployee = [];
	                var x = 0;
	                angular.forEach($scope.posts, function(item) {
	                    if (item.checkBoxFlag) {
	                        checkedEmployee.push(item.requestId);
	                        x++;
	                    }
	                });

	                if (checkedEmployee.length == 0) {
	                    ngDialog.open({
	                        template: 'Please Check one or more Employees.',
	                        plain: true
	                    });

	                    return false;
	                }

	                var dataObj = {

	                    eFmFmEmployeeRequestMaster: {
	                        efmFmUserMaster: {
	                            eFmFmClientBranchPO: {
	                                branchId: branchId
	                            }
	                        }
	                    },
	                    employeeId: JSON.stringify(checkedEmployee),
	                    userId: profileId,
	                    combinedFacility: combinedFacility
	                };
	                $http
	                    .post('services/trip/travelrequest/', dataObj)
	                    .success(
	                        function(data, status, headers, config) {
	                            if (data.status != "invalidRequest") {
	                                if (data.status == "fail") {
	                                    ngDialog
	                                        .open({
	                                            template: 'No vehicle checked in please wait..',
	                                            plain: true
	                                        });

	                                } else {
	                                    angular
	                                        .forEach(
	                                            $scope.posts,
	                                            function(item) {
	                                                if (item.checkBoxFlag) {
	                                                    $(
	                                                            '.employee' +
	                                                            item.employeeId +
	                                                            item.requestId)
	                                                        .hide(
	                                                            'slow');
	                                                    item.checkBoxFlag = false;
	                                                }
	                                            });
	                                    $scope.selectAllClicked = false;
	                                }
	                            }
	                        }).error(
	                        function(data, status, headers, config) {
	                            // log error
	                        });
	            };




	            $scope.openMap = function(route) {

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
	                            return route.nodalPoints;
	                        }
	                    }
	                });
	            }

	            $scope.getLocationDetails = function(post) {

	                var modalInstance = $modal.open({
	                    templateUrl: 'partials/modals/employeeWayPointsDetails.jsp',
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


	            //BUTTON ACTION :: DELETE SINGLE EMPLOYEE
	            $scope.deleteEmployee = function(post, facilityData) {
	                $confirm({
	                        text: 'Are you sure you want to delete this row?',
	                        title: 'Delete Confirmation',
	                        ok: 'Yes',
	                        cancel: 'No'
	                    })
	                    .then(
	                        function() {
	                            var dataObj = {
	                                branchId: branchId,
	                                requestId: post.requestId,
	                                userId: profileId,
	                                combinedFacility: String(facilityData)
	                            };
	                            $http
	                                .post(
	                                    'services/trip/deleteRequestTravelDesk/',
	                                    dataObj)
	                                .success(
	                                    function(data, status,
	                                        headers, config) {
	                                        if (data.status != "invalidRequest") {
	                                            if (data.status == "success") {
	                                                $(
	                                                        '.employee' +
	                                                        post.employeeId +
	                                                        post.requestId)
	                                                    .hide(
	                                                        'slow');

	                                                ngDialog
	                                                    .open({
	                                                        template: post.employeeName +
	                                                            ' has been deleted successfully.',
	                                                        plain: true
	                                                    });


	                                            }
	                                        }
	                                    }).error(
	                                    function(data, status,
	                                        headers, config) {
	                                        // log error
	                                    });
	                        });
	            };

	            //BUTTON ACTION :: CANCEL ALL ACTIONS/SELECTIONS
	            $scope.cancel = function() {
	                if ($scope.selectAllClicked) {
	                    $scope.selectAllClicked = false;
	                }
	                angular.forEach($scope.posts, function(item) {
	                    item.checkBoxFlag = false;
	                });
	            };

	            //BUTTON ACTION :: CLOSE ALERT
	            $scope.closeAlert = function() {
	                $('.alert_TravelDesk').hide('slow');
	            };

	            $scope.$watch("searchEmployeeReported", function(query) {
	                if (!$scope.searchEmployeeReported) {
	                    $scope.showSearchResultCount = false;
	                } else {
	                    $scope.showSearchResultCount = true;
	                    $scope.countofFilteredRecords = $filter("filter")(
	                        $scope.posts, query);
	                }
	            });
	            $scope.$watch("selectShifts", function(query) {
	                if ($scope.selectShifts == "") {
	                    $scope.showSelectShiftsCount = false;
	                } else {
	                    $scope.showSelectShiftsCount = true;
	                    $scope.showSelectShiftsRecords = $filter("filter")(
	                        $scope.posts, query);
	                }
	            });

	            $scope.$watch("search.text", function(query) {
	                if ($scope.search.text == "") {
	                    $scope.searchIsEmpty = false;
	                }
	            });

	            //BUTTON ACTION : Edit Travel Desk Employee
	            $scope.editTravelDesk = function(post, index, size, facilityData) {
	                var tripTimeArray = post.tripTime.split(':');
	                var newTripTimeFormat = tripTimeArray[0] + ':' +
	                    tripTimeArray[1];
	                var modalInstance = $modal.open({
	                    templateUrl: 'partials/modals/editTravelDeskModal.jsp',
	                    controller: 'editTravelDeskCtrl',
	                    size: size,
	                    resolve: {
	                        employee: function() {
	                            return post;
	                        },
	                        tripTimeSelected: function() {
	                            return newTripTimeFormat;
	                        },
	                        facilityData: function() {
	                            return facilityData;
	                        },
	                        facilityDetails: function() {
	                            return $scope.facilityDetails;
	                        },

	                    }
	                });

	                modalInstance.result
	                    .then(function(result) {
	                        if (result.changeMasterData != 'N') {
	                            post.weekOffs = result.selectedDays;
	                        }

	                        if (post.tripType == 'DROP') {
	                            post.pickUpTime = result.sequenceNumber;
	                        } else {
	                            post.pickUpTime = result.timePickerSelectedTime;
	                        }

	                        post.tripTime = result.shiftTime.shiftTime + ':00';
	                        post.employeeRouteId = result.zone.routeId;
	                        post.employeeRouteName = result.zone.routeName;
	                        post.nodalPointId = result.location.nodalPointId;
	                        post.nodalPointTitle = result.location.nodalPointTitle;
	                        post.nodalPointDescription = result.location.nodalPointDescription;
	                        post.nodalPoints = result.location.nodalPoints;
	                        post.requestType = result.requestType;
	                    });
	            };

	            $scope.updatePickupTime = function(employee) {

	                if (employee.isUpdateClicked) {
	                    $confirm({
	                            text: "Are you sure you want to change pickup time from " +
	                                employee.tripTime +
	                                " to " +
	                                $scope
	                                .convertToTime(employee.createNewAdHocTime) +
	                                ":00" + " ?",
	                            title: 'Confirmation',
	                            ok: 'Yes',
	                            cancel: 'No'
	                        })
	                        .then(
	                            function() {
	                                employee.isUpdateClicked = false;
	                                employee.tripTime = $scope
	                                    .convertToTime(employee.createNewAdHocTime) +
	                                    ":00";
	                                var dataObj = {
	                                    eFmFmClientBranchPO: {
	                                        branchId: branchId
	                                    },
	                                    assignRouteId: '0',
	                                    time: employee.tripTime,
	                                    requestId: employee.requestId,
	                                    userId: profileId,
	                                    combinedFacility: combinedFacility
	                                };

	                                $http
	                                    .post(
	                                        'services/trip/updateShiftTimeForRequest/',
	                                        dataObj)
	                                    .success(
	                                        function(data, status,
	                                            headers, config) {
	                                            if (data.status != "invalidRequest") {
	                                                ngDialog
	                                                    .open({
	                                                        template: 'Shift time has been updated successfully.',
	                                                        plain: true
	                                                    });

	                                            }
	                                        })
	                                    .error(
	                                        function(data, status,
	                                            headers, config) {
	                                            // log error
	                                        });
	                            });
	                } else {
	                    employee.isUpdateClicked = true;
	                }
	            };

	            //BUTON ACTION :: Assign Cab
	            $scope.assignCab = function() {
	                var modalInstance = $modal.open({
	                    templateUrl: 'partials/modals/assignCabModal.jsp',
	                    controller: 'assignCabCtrl',
	                    backdrop: 'static',
	                    resolve: {}
	                });

	                modalInstance.result
	                    .then(function(result) {

	                    });
	            };

	            //BUTTON ACTION :: Map Icon is Clicked
	            $scope.openSingleModal = function(employee, size) {
	                var modalInstance = $modal.open({
	                    templateUrl: 'partials/modals/singleMapLocation.jsp',
	                    controller: 'singleMapCtrl',
	                    size: size,
	                    resolve: {
	                        employee: function() {
	                            return employee;
	                        },
	                        defaultLocation: function() {
	                            return $scope.officeLocation;
	                        }
	                    }
	                });

	                modalInstance.result
	                    .then(function(result) {
	                        var index = _.findIndex($scope.posts, {
	                            employeeId: result.employeeId
	                        });
	                        if (index >= 0) {
	                            $scope.posts[index].employeeWaypoints = result.cords;
	                            $scope.posts[index].employeeAddress = result.employeeAddress;
	                        }
	                    });
	            };

	            $scope.searchEmployee = function(searchText) {
	                if (searchText) {
	                    $scope.searchIsEmpty = false;
	                    var dataObj = {
	                        branchId: branchId,
	                        employeeId: searchText,
	                        userId: profileId,
	                        locationFlg: 'N',
	                        combinedFacility: combinedFacility
	                    };
	                    $http.post('services/trip/emplyeerequestsearch/', dataObj)
	                        .success(function(data, status, headers, config) {
	                            if (data.status != "invalidRequest") {
	                                if (data.requests.length == 0) {
	                                    $scope.searchByidExel = false;
	                                    $scope.excelButtonVisible = true;
		                				$scope.employeeRosterTable = false; 
		                				$scope.adhocSearch = false;                                   
	                                    ngDialog
	                                        .open({
	                                            template: 'Request not exist for this employeeId,Please check employeeId.',
	                                            plain: true
	                                        });

	                                } else {
	                                    $scope.summaryLengthShift = data.requests.length;
	                                    $scope.totalRecordOfRoaster = data.requests.length;
	                                    $scope.summaryOfAdhocRequest = data.requests.length;
	                					$scope.summaryOfTotalAdhocRequest = data.requests.length;
	                                    $scope.posts = data.requests;
	                                    angular.forEach($scope.posts, function(item) {
	                                        item.checkBoxFlag = false;
	                                    });
	                                    $scope.adhocInitTable = false;
										$scope.adhocTable = false;
										$scope.adhocSearch = true;
	                                   	$scope.employeeRosterTable = true;
	                                   	$scope.multiEmployeeRosterTableShift = false;
	                                   	$scope.employeeRosterTableInit = false;
	                                    $scope.progressbar.complete();
	                                    $scope.isLoaded = true;
	                                    $scope.searchByidExel = true;
	                                    $scope.excelButtonVisible = true;
	                                }
	                            }
	                        }).error(
	                            function(data, status, headers, config) {
	                                // log error
	                            });
	                } else {
	                    $scope.searchIsEmpty = true;
	                }
	            };

	            $scope.reload = function() {

	                $state.reload('contact.detail');
	            }


	            $scope.refreshEmployeeTravelDesk = function() {
	                // $scope.getTravelDesk();
	            };

	            $scope.refreshEmployeeMultipleTravelDesk = function() {
	                $scope.getMultiTravelRequest();
	            }
	        }
	    };

	    //CONTROLLER FOR THE 'Edit Travel Desk Row' MODAL
	    var editTravelDeskCtrl = function($scope, $modalInstance, $state, employee,
	        $http, $timeout, tripTimeSelected, ngDialog, facilityData, facilityDetails) {


	        $scope.update = {};
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
	            tripType: employee.tripType,
	            userId: profileId,
	            combinedFacility: combinedFacility
	        };
	        $http.post('services/trip/tripshiftime/', data).success(
	            function(data, status, headers, config) {
	                if (data.status != "invalidRequest") {
	                    $scope.shiftsTimes = _.uniq(data.shift, function(p){ return p.shiftTime; });

	                    employee.tripTime = employee.tripTime.slice(0, -3);

	                    var shiftsTimes = _.where($scope.shiftsTimes, {
	                        shiftTime: employee.tripTime
	                    });

	                    $scope.update.shiftTime = shiftsTimes[0];
	                }
	            }).error(function(data, status, headers, config) {});



	        $scope.delay = 0;
	        $scope.minDuration = 0;
	        $scope.templateUrl = 'bower_components/ngSpinner/custom-template.html';
	        $scope.message = 'Please Wait...';
	        $scope.backdrop = true;
	        $scope.promise = null;

	        $scope.$on('LOADMODAL', function() {
	            $scope.isProcessingModal = true;
	        });
	        $scope.$on('UNLOADMODAL', function() {
	            $scope.isProcessingModal = true;
	        });

	        $scope.update = {};
	        $scope.update.shiftTime = {};
	        $scope.hstep = 1;
	        $scope.mstep = 1;
	        $scope.ismeridian = false;
	        var isShiftTimeChange = false;
	        var isZoneChange = false;
	        var isAreaChange = false;
	        var isDaysOffChange = false;
	        var isChangeToRegularShiftChange = false;
	        var isPickUpTimeChange = false;
	        var isNodalPointChange = false;
	        $scope.update.changeMasterData = 'N';
	        $scope.triptype = employee.tripType;
	        $scope.update.sequenceNumber = employee.pickUpTime;
	        var isSeqChange;
	        var todaysDate = new Date();
	        $scope.update.daysModel = [];
	        var shiftTimeIndex;
	        $scope.alertMessage;
	        $scope.alertHint;

	        $scope.daysData = [{
	            'id': 'Sunday',
	            label: 'Sunday'
	        }, {
	            'id': 'Monday',
	            label: 'Monday'
	        }, {
	            'id': 'Tuesday',
	            label: 'Tuesday'
	        }, {
	            'id': 'Wednesday',
	            label: 'Wednesday'
	        }, {
	            'id': 'Thursday',
	            label: 'Thursday'
	        }, {
	            'id': 'Friday',
	            label: 'Friday'
	        }, {
	            'id': 'Saturday',
	            label: 'Saturday'
	        }];

	        if (employee.weekOffs !== '') {
	            var tempDaysOff = employee.weekOffs.split(',');
	            angular.forEach(tempDaysOff, function(item) {
	                $scope.update.daysModel.push({
	                    'id': item,
	                    'label': item
	                });
	            });
	        }

	        $scope.daysButtonLabel = {
	            'buttonDefaultText': 'Select Days Off'
	        };

	        $scope.daysSettings = {
	            smartButtonMaxItems: 5,
	            smartButtonTextConverter: function(itemText, originalItem) {
	                return itemText;
	            }
	        };

	        $scope.setShiftTime = function(triptype) {

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
	                tripType: employee.tripType,
	                userId: profileId,
	                combinedFacility: combinedFacility
	            };
	            $http.post('services/trip/tripshiftime/', data).success(
	                function(data, status, headers, config) {
	                    if (data.status != "invalidRequest") {
	                        $scope.shiftsTimes = _.uniq(data.shift, function(p){ return p.shiftTime; });
	                    }
	                }).error(function(data, status, headers, config) {});


	        };
	        $scope.setShiftTime($scope.triptype);

	        $scope.getNormNodRoutes = function(emprouteId) {
	            var dataObj = {
	                branchId: branchId,
	                routeId: emprouteId,
	                userId: profileId,
	                combinedFacility: combinedFacility
	            };
	            $http
	                .post('services/zones/routeNodelPoints/', dataObj)
	                .success(
	                    function(data, status, headers, config) {
	                        if (data.status != "invalidRequest") {
	                            $scope.normalNodalData = data.routeDetails;
	                            if ($scope.normalNodalData.length == 0) {
	                                ngDialog
	                                    .open({
	                                        template: 'No Nodal Point found for this route.',
	                                        plain: true
	                                    });

	                            }
	                        }
	                    }).error(function(data, status, headers, config) {
	                    // log error
	                });
	        };
	        $scope.getNormNodRoutes(employee.employeeRouteId);
	        $scope.update.location = {
	            nodalPointTitle: employee.nodalPointTitle,
	            nodalPointId: employee.nodalPointId,
	            nodalPointDescription: employee.nodalPointDescription,
	            nodalPoints: employee.nodalPoints
	        };

	        $scope.initializeTimePickerTime = function(time) {
	            var pickUpTime = time.split(':');
	            var d = new Date();
	            d.setHours(pickUpTime[0]);
	            d.setMinutes(pickUpTime[1]);
	            return d;
	        };

	        //Convert to dd-mm-yyyy
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

	        if (employee.tripType == 'PICKUP') {
	            $scope.update.selectedDate = $scope
	                .initializeTimePickerTime(employee.pickUpTime);
	        }

	        var data = {
	            efmFmUserMaster: {
	                eFmFmClientBranchPO: {
	                    branchId: branchId
	                }
	            },
	            userId: profileId,
	            combinedFacility: _.where(userFacilities, {
	                name: employee.facilityName
	            })[0].branchId

	        };
	        $http
	            .post('services/trip/editemployeetravelrequest/', data)
	            .success(
	                function(data, status, headers, config) {
	                    if (data.status != "invalidRequest") {
	                        $scope.zonesData = data.routesData;
	                        var zoneIndex = _.findIndex($scope.zonesData, {
	                            routeName: employee.employeeRouteName
	                        });
	                        if (employee.tripType == 'DROP') {
	                            shiftTimeIndex = _.findIndex(
	                                $scope.shiftsTimes, {
	                                    shiftTime: tripTimeSelected
	                                });
	                            if (shiftTimeIndex >= 0) {
	                                $scope.update.shiftTime = $scope.shiftsTimes[shiftTimeIndex];
	                            } else
	                                $scope.update.shiftTime = '';
	                        } else {
	                            shiftTimeIndex = _.findIndex(
	                                $scope.shiftsTimes, {
	                                    shiftTime: tripTimeSelected
	                                });
	                            if (shiftTimeIndex >= 0) {
	                                $scope.update.shiftTime = $scope.shiftsTimes[shiftTimeIndex];
	                            } else
	                                $scope.update.shiftTime = '';
	                        }

	                        if (zoneIndex >= 0) {
	                            $scope.update.zone = $scope.zonesData[zoneIndex];
	                            $scope.update.area = employee.employeeAreaName;
	                        } else {
	                            $scope.update.zone = '';
	                        }
	                    }
	                }).error(function(data, status, headers, config) {
	                // log error
	            });

	        $scope.setshiftTime = function(shift) {
	            if (angular.isObject(shift)) {
	                if (shift.shiftTime != employee.tripTime) {
	                    isShiftTimeChange = true;
	                } else {
	                    isShiftTimeChange = false;
	                }
	            }
	        };

	        $scope.setZone = function(zone) {
	            if (angular.isObject(zone)) {
	                if (zone.routeName !== employee.employeeRouteName) {
	                    isZoneChange = true;
	                    $scope.getNormNodRoutes(zone.routeId);
	                } else {
	                    isZoneChange = false;
	                }
	            }
	        };

	        $scope.setNormNod = function(nodalPoints) {
	            if (angular.isObject(nodalPoints)) {
	                if (nodalPoints.nodalPointTitle !== employee.nodalPointTitle) {
	                    isNodalPointChange = true;
	                } else {
	                    isNodalPointChange = false;
	                }
	            }
	        };

	        //SAVE BUTTON FUNCTION
	        $scope.save = function(result) {
	            result.selectedDays = result.daysModel.map(function(el) {
	                return el.id;
	            }).join();
	            //Watch for the Days Of String
	            if (result.selectedDays != employee.weekOffs) {
	                isDaysOffChange = true;
	            } else {
	                isDaysOffChange = false;
	            }

	            //assign RouteType value
	            if (result.location.nodalPointTitle == "default") {
	                result.requestType = "normal";
	            } else {
	                result.requestType = "nodal";
	            }

	            //Watch for the CheckBox
	            if ($scope.update.changeMasterData != 'N') {
	                isChangeToRegularShiftChange = true;
	            } else {
	                isChangeToRegularShiftChange = false;
	            }

	            if ($scope.triptype == 'DROP') {
	                result.timePickerSelectedTime = $scope.update.sequenceNumber;
	            } else {
	                result.timePickerSelectedTime = convertToTime(result.selectedDate);
	                //Watch for Time From Timer
	                if (result.timePickerSelectedTime != employee.pickUpTime) {
	                    isPickUpTimeChange = true;
	                } else {
	                    isPickUpTimeChange = false;
	                }
	            }
	            if (result.sequenceNumber == employee.pickUpTime) {
	                isSeqChange = false;
	            } else
	                isSeqChange = true;

	            if (isShiftTimeChange || isZoneChange || isAreaChange ||
	                isPickUpTimeChange || isDaysOffChange ||
	                isChangeToRegularShiftChange || isSeqChange ||
	                isNodalPointChange) {
	                var dataObj = {
	                    efmFmUserMaster: {
	                        eFmFmClientBranchPO: {
	                            branchId: branchId
	                        }
	                    },
	                    userId: profileId,
	                    requestId: employee.requestId,
	                    areaId: employee.employeeAreaId,
	                    zoneId: result.zone.routeId,
	                    weekOffs: result.selectedDays,
	                    updateRegularRequest: result.changeMasterData,
	                    time: result.shiftTime.shiftTime,
	                    pickTime: result.timePickerSelectedTime,
	                    nodalPointId: result.location.nodalPointId,
	                    combinedFacility: _.where(facilityDetails, {
	                        name: employee.facilityName
	                    })[0].branchId
	                };
	                $scope.promise = $http.post(
	                        'services/trip/updatetravelrequestdata/', dataObj)
	                    .success(function(data, status, headers, config) {
	                        if (data.status != "invalidRequest") {
	                            if (data.status == "success") {
	                                ngDialog.open({
	                                    template: 'Request updated successfully.',
	                                    plain: true
	                                });

	                                $('.loading').show();
	                                $timeout(function() {
	                                    $modalInstance.close(result)
	                                }, 3000);
	                            }
	                        }
	                    }).error(function(data, status, headers, config) {
	                        // log error
	                    });
	            } else {
	                ngDialog.open({
	                    template: 'None of the Entity has been change.',
	                    plain: true
	                });

	                $timeout(function() {
	                    $modalInstance.close(result);
	                    ngDialog.close();
	                }, 3000);
	            }
	        };

	        //CLOSE BUTTON FUNCTION
	        $scope.cancel = function() {
	            $modalInstance.dismiss('cancel');
	        };
	    };

	    //CONTROLLER FOR THE 'view on map' MODAL
	    var singleMapCtrl = function($scope, $modalInstance, $state, employee,
	        defaultLocation, $http, $timeout, ngDialog) {
	        $scope.isLoaded = false;
	        $scope.defaultLocation = defaultLocation;
	        $scope.employee = {
	            cords: employee.employeeWaypoints,
	            employeeAddress: employee.employeeAddress,
	            employeeName: employee.employeeName,
	            employeeId: employee.employeeId,
	            employeeName: employee.employeeName
	        };
	        $scope.isLoaded = true;

	        //SAVE BUTTON FUNCTION
	        $scope.saveNewCords = function(result) {
	            employee.employeeWaypoints = result.cords;
	            employee.employeeAddress = result.employeeAddress;
	            var dataObj = {
	                eFmFmEmployeeRequestMaster: {
	                    efmFmUserMaster: {
	                        eFmFmClientBranchPO: {
	                            branchId: branchId
	                        }
	                    }
	                },
	                requestId: employee.requestId,
	                userId: profileId,
	                combinedFacility: combinedFacility
	            };
	            $http.post('services/trip/addressUpdate/', dataObj).success(
	                function(data, status, headers, config) {
	                    if (data.status != "invalidRequest") {
	                        if (data.status == "success") {
	                            $modalInstance.close(result);
	                        }
	                    }
	                }).error(function(data, status, headers, config) {
	                // log error
	            });
	        };

	        //CLOSE BUTTON FUNCTION
	        $scope.cancel = function() {
	            $modalInstance.dismiss('cancel');
	        };
	    };

	    //CONTROLLER FOR THE 'view on map' MODAL
	    var assignCabCtrl = function($scope, $modalInstance, $state, $http, $rootScope,
	        $timeout, ngDialog, $modal) {
	        $scope.shiftsTime = [];
	        $scope.assignCab = {};
	        $scope.assignCab.assignDate = new Date();
	        $scope.typeOfShiftTimeSelected;
	        var tripTimeSelected;
	        $scope.hstep = 1;
	        $scope.mstep = 1;
	        $scope.ismeridian = false;
	        $scope.setMinDate = new Date();
	        $scope.form = {};
	        $scope.rememberShiftRouteView = false;
	        $scope.facilityData = [];
	        $scope.facilityDetails = userFacilities;
	        $scope.baseFacilityDetails = userFacilities;
	        var array = JSON.parse("[" + combinedFacility + "]");
	        $scope.facilityData = array;

	        if (multiFacility == 'Y') {
	            $scope.multiFacilityRequired = true;
	        } else {
	            $scope.multiFacilityRequired = false;
	        }

	        $scope.tripTypes = [{
	            'value': 'PICKUP',
	            'text': 'PICKUP'
	        }, {
	            'value': 'DROP',
	            'text': 'DROP'
	        }];
	        $scope.pickupShiftsTime = [{
	            'shiftTime': '05:30:00'
	        }, {
	            'shiftTime': '07:00:00'
	        }, {
	            'shiftTime': '12:00:00'
	        }, {
	            'shiftTime': '14:00:00'
	        }, {
	            'shiftTime': '16:00:00'
	        }, {
	            'shiftTime': '18:00:00'
	        }, {
	            'shiftTime': '19:00:00'
	        }];

	        $scope.dropShiftsTime = [{
	            'shiftTime': '14:30:00'
	        }, {
	            'shiftTime': '16:00:00'
	        }, {
	            'shiftTime': '21:00:00'
	        }, {
	            'shiftTime': '23:00:00'
	        }, {
	            'shiftTime': '01:00:00'
	        }, {
	            'shiftTime': '03:00:00'
	        }];

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

	        $scope.openAssignDateCal = function($event) {
	            $event.preventDefault();
	            $event.stopPropagation();
	            $timeout(function() {
	                $scope.datePicker = {
	                    'openedassignDate': true
	                };
	            })
	        };

	        //Initialize TimePicker to 00:00
	        var timePickerInitialize = function() {
	            var d = new Date();
	            d.setHours(00);
	            d.setMinutes(0);
	            $scope.assignCab.createNewAdHocTime = d;
	        }
	        $scope.assignCab.needRoutewise = false;
	        var data = {
	            eFmFmClientBranchPO: {
	                branchId: branchId,
	                userId: profileId
	            },
	            combinedFacility: combinedFacility
	        };
	        $http.post('services/zones/createbucket/', data).success(
	            function(data, status, headers, config) {
	                if (data.status != "invalidRequest") {
	                    $scope.allZonesData = data.routesData;
	                    $('.btn-link').addClass('noPointer');
	                    $scope.assignCab.tripType = $scope.tripTypes[0];
	                    $scope.setTripType($scope.assignCab.tripType, combinedFacility);
	                    $scope.shiftsTime = [{
	                        'shiftTime': 'Select Shift Time'
	                    }];
	                    $scope.assignCab.shiftTime = $scope.shiftsTime[0];
	                    $scope.shiftTime = 'preDefineShiftTime';
	                    timePickerInitialize();
	                }
	            }).error(function(data, status, headers, config) {
	            // log error
	        });

	        $scope.selectShiftTimeRadio = function(shiftTime) {
	            $scope.typeOfShiftTimeSelected = shiftTime;
	            $('.btn-link').addClass('noPointer');
	            $scope.assignCab.applyAlgorithm = false;

	            $scope.assignCab.noOfEmp = '';
	            $scope.assignCabsTempo = [];
	            $scope.assignCabsInova = [];
	        };

	        $scope.selectShiftTimeRadio2 = function(shiftTime) {
	            $scope.typeOfShiftTimeSelected = shiftTime;
	            $('.btn-link').removeClass('noPointer');
	            $scope.assignCab.applyAlgorithm = false;

	            $scope.assignCab.noOfEmp = '';
	            $scope.assignCabsTempo = [];
	            $scope.assignCabsInova = [];
	        };

	        $scope.setTripType = function(tripType, facilityData) {

	            if (tripType.value == 'PICKUP') {
	                $scope.baseFacilityLabel = 'Complete Facilty For PickUp';
	            } else {
	                $scope.baseFacilityLabel = 'Start Facility For Drop';
	            }

	            if (angular.isObject(tripType)) {
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
	                    tripType: tripType.value,
	                    userId: profileId,
	                    combinedFacility: facilityData.toString()
	                };
	                $http.post('services/trip/tripshiftime/', data).success(
	                    function(data, status, headers, config) {
	                        if (data.status != "invalidRequest") {
	                            $scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });;
	                            $scope.assignCab.shiftTime = $scope.shiftsTime[0];
	                            $scope.assignCab.applyAlgorithm = false;

	                            $scope.assignCab.noOfEmp = '';
	                            $scope.assignCabsTempo = [];
	                            $scope.assignCabsInova = [];
	                        }

	                    }).error(function(data, status, headers, config) {});
	            } else {
	                $scope.shiftsTime = '';
	            }
	            $scope.assignCab.shiftTime = $scope.shiftsTime[0];
	        };

	        $scope.setAlgo = function() {
	            $scope.assignCab.applyAlgorithm = false;

	            $scope.assignCab.noOfEmp = '';
	            $scope.assignCabsTempo = [];
	            $scope.assignCabsInova = [];
	        };

	        //on Date Change from Calendar
	        $scope.getAlgoData = function() {

	        };

	        $scope.manualRouting = function(assignCab, shiftTime, manualRouting) {
	            $scope.assignCab.autoRoute = '';
	            $scope.assignCab.autoNodalRouting = '';
	            $scope.assignCab.rememberShiftRoute = '';
	            $scope.rememberShiftRouteView = false;
	            if (manualRouting == 1) {
	                $scope.manualRoutingFlg = 'manualRouting';
	            }


	        }

	        $scope.applyAlgorithm = function(assignCab, shiftTime, applyAlgorithm) {
	            $scope.assignCab.manualRouting = '';
	            $scope.assignCab.autoNodalRouting = '';
	            $scope.assignCab.rememberShiftRoute = '';
	            $scope.rememberShiftRouteView = false;
	            if (applyAlgorithm == 2) {
	                $scope.autoRoutingFlg = 'autoRouting';
	            }

	            if ($scope.assignCab.applyAlgorithm) {
	                var tripTimeSelected;
	                var selectedZone;

	                if (shiftType == 'preDefineShiftTime') {
	                    tripTimeSelected = $scope.assignCab.shiftTime.shiftTime
	                } else {
	                    var fullDate = new Date($scope.assignCab.createNewAdHocTime);
	                    var time = fullDate.getHours() + ':' +
	                        fullDate.getMinutes() + ':00';
	                    tripTimeSelected = time;
	                }

	                if (!$scope.assignCab.needRoutewise) {
	                    selectedZone = 0;
	                } else {
	                    selectedZone = $scope.assignCab.zone.routeId;
	                }

	                if (angular.isObject($scope.assignCab.assignDate)) {
	                    var dataObj = {
	                        branchId: branchId,
	                        time: tripTimeSelected,
	                        zoneId: selectedZone,
	                        tripType: tripType.text,
	                        executionDate: convertDateUTC($scope.assignCab.assignDate),
	                        userId: profileId,
	                        combinedFacility: combinedFacility
	                    };

	                    $http
	                        .post('services/xlEmployeeExport/algoPopUpInputs/',
	                            dataObj)
	                        .success(
	                            function(data, status, headers, config) {
	                                if (data.status != "invalidRequest") {
	                                    $scope.assignCab.assignDate = new Date();
	                                    $scope.assignCab.noOfEmp = data[0].employeeCount;
	                                    $scope.assignCabsTempo = data[1].tempo;
	                                    $scope.assignCabsInova = data[0].inova;
	                                    $scope.assignCab.vehicleTempo = $scope.assignCabsTempo[0];
	                                    $scope.assignCab.vehicleInova = $scope.assignCabsInova[0];
	                                }

	                            }).error(
	                            function(data, status, headers, config) {
	                                // log error
	                            });
	                }
	            } else {

	                $scope.assignCab.noOfEmp = '';
	                $scope.assignCabsTempo = [];
	                $scope.assignCabsInova = [];
	            }
	        };

	        $scope.rememberShiftDateCal = function($event) {
	            $event.preventDefault();
	            $event.stopPropagation();
	            $timeout(function() {
	                $scope.datePicker = {
	                    'rememberDate': true
	                };
	            })
	        };


	        $scope.autoNodalRouting = function(tripType, shiftTime, autoRoute) {
	            $scope.assignCab.manualRouting = '';
	            $scope.assignCab.autoRoute = '';
	            $scope.assignCab.rememberShiftRoute = '';
	            $scope.rememberShiftRouteView = false;
	        }


	        $scope.rememberShiftRoute = function(tripType, shiftTime, autoRoute) {
	            $scope.rememberShiftRouteView = true;
	            $scope.assignCab.manualRouting = '';
	            $scope.assignCab.autoRoute = '';
	            $scope.assignCab.autoNodalRouting = '';

	        }


	        /*Routing Assign Function- Manual Route / Auto Route  , Flag Names -  1. Manual Routing , 2. Auto Routing*/

	        $scope.assign = function(cab, shiftTime, combinedFacilityId) {
	            if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
	                combinedFacilityId = combinedFacility;
	            } else {
	                combinedFacilityId = String(combinedFacilityId);
	            }

	            if (cab.manualRouting == !1) {
	                $scope.autoRouting = 2;
	            }


	            var selectedZone;
	            $('.noMoreClick').addClass('disabled');
	            if (shiftTime == 'preDefineShiftTime') {
	                tripTimeSelected = cab.shiftTime.shiftTime;
	            } else {
	                var fullDate = new Date(cab.createNewAdHocTime);
	                var time = fullDate.getHours() + ':' + fullDate.getMinutes() + ':00';
	                tripTimeSelected = time;
	            }

	            if (!cab.needRoutewise) {
	                selectedZone = 0;
	            } else {
	                selectedZone = cab.zone.routeId;
	            }
	            if (cab.manualRouting == "1") {

	                if (tripTimeSelected == '0:0:00') {
	                    time = cab.shiftTime.shiftTime;
	                } else {
	                    time = tripTimeSelected;
	                }

	                if (multiFacility == 'Y') {
	                    var baseFacilityId = String(cab.baseFacilityData);
	                } else {
	                    var baseFacilityId = branchId;
	                }

	                var dataObj = {
	                    branchId: branchId,
	                    time: time,
	                    zoneId: selectedZone,
	                    tripType: cab.tripType.value,
	                    executionDate: convertDateUTC(cab.assignDate),
	                    userId: profileId,
	                    combinedFacility: combinedFacilityId,
	                    baseFacility: baseFacilityId
	                };

	                $http.post('services/request/caballocation/', dataObj).success(
	                    function(data, status, headers, config) {
	                        if (data.status != "invalidRequest") {
	                            if (data == "") {
	                                ngDialog.open({
	                                    template: 'Cab allocation is in progress....',
	                                    plain: true
	                                });

	                                $timeout(function() {
	                                    $modalInstance.close(data)
	                                }, 3000);

	                                if ($scope.selectShifts) {
	                                    $scope.setShifts($scope.selectShifts);
	                                } else {
	                                    var data = {
	                                        eFmFmEmployeeRequestMaster: {
	                                            efmFmUserMaster: {
	                                                eFmFmClientBranchPO: {
	                                                    branchId: branchId
	                                                }
	                                            }
	                                        },
	                                        userRole: userRole,
	                                        efmFmUserMaster: {
	                                            userId: profileId
	                                        },
	                                        userId: profileId,
	                                        combinedFacility: combinedFacilityId
	                                    };
	                                    $http
	                                        .post(
	                                            'services/trip/employeeTravelRequest/',
	                                            data)
	                                        .success(
	                                            function(data, status, headers,
	                                                config) {
	                                                $scope.OriginalPostsData = data.requests;
	                                                $scope.posts = data.requests;
	                                            }).error(
	                                            function(data, status, headers,
	                                                config) {});
	                                }

	                                $timeout(
	                                    function() {
	                                        ngDialog
	                                            .open({
	                                                template: 'Cab has been assigned successfully.',
	                                                plain: true
	                                            });

	                                    }, 3000);

	                            }
	                        }
	                    }).error(
	                    function(data, status, headers, config) {
	                        // log error
	                    });
	            }
	            if (cab.autoRoute == "2") {


	                var data = {
	                    branchId: branchId,
	                    time: tripTimeSelected,
	                    zoneId: selectedZone,
	                    tripType: cab.tripType.value,
	                    resheduleDate: convertDateUTC(cab.assignDate),
	                    combinedFacility: combinedFacilityId,
	                    baseFacility: String(cab.baseFacilityData)
	                };


	                ngDialog.open({
	                    template: 'Cab allocation is in progress....',
	                    plain: true
	                });


	                $timeout(function() {
	                    $modalInstance.close()
	                }, 1000);

	                $rootScope.$emit('autoRoutingValue', data);

	            }
	            if (cab.autoNodalRouting == "3") {
	                if (tripTimeSelected == '0:0:00') {
	                    time = cab.shiftTime.shiftTime;
	                } else {
	                    time = tripTimeSelected;
	                }

	                var dataObj = {
	                    branchId: branchId,
	                    time: time,
	                    zoneId: selectedZone,
	                    tripType: cab.tripType.value,
	                    executionDate: convertDateUTC(cab.assignDate),
	                    userId: profileId,
	                    combinedFacility: combinedFacilityId,
	                    baseFacility: String(cab.baseFacilityData)
	                };

	                $http.post('services/request/caballocation/', dataObj).success(
	                    function(data, status, headers, config) {
	                        if (data.status != "invalidRequest") {
	                            if (data == "") {
	                                ngDialog.open({
	                                    template: 'Cab allocation is in progress....',
	                                    plain: true
	                                });

	                                $timeout(function() {
	                                    $modalInstance.close(data)
	                                }, 3000);

	                                if ($scope.selectShifts) {
	                                    $scope.setShifts($scope.selectShifts);
	                                } else {
	                                    var data = {
	                                        eFmFmEmployeeRequestMaster: {
	                                            efmFmUserMaster: {
	                                                eFmFmClientBranchPO: {
	                                                    branchId: branchId
	                                                }
	                                            }
	                                        },
	                                        userRole: userRole,
	                                        efmFmUserMaster: {
	                                            userId: profileId
	                                        },
	                                        userId: profileId,
	                                        combinedFacility: combinedFacility
	                                    };
	                                    $http
	                                        .post(
	                                            'services/trip/employeeTravelRequest/',
	                                            data)
	                                        .success(
	                                            function(data, status, headers,
	                                                config) {
	                                                $scope.OriginalPostsData = data.requests;
	                                                $scope.posts = data.requests;
	                                            }).error(
	                                            function(data, status, headers,
	                                                config) {});
	                                }

	                                $timeout(
	                                    function() {
	                                        ngDialog
	                                            .open({
	                                                template: 'Cab has been assigned successfully.',
	                                                plain: true
	                                            });

	                                    }, 3000);

	                            }
	                        }
	                    }).error(
	                    function(data, status, headers, config) {
	                        // log error
	                    });
	            }


	            if (cab.rememberShiftRoute == "4") {

	                if (cab.rememberDate == undefined) {
	                    ngDialog
	                        .open({
	                            template: 'Kindly add remember Date',
	                            plain: true
	                        });
	                    return false;
	                    $('.noMoreClick').removeClass('disabled');
	                }
	                var data = {
	                    branchId: branchId,
	                    time: tripTimeSelected,

	                    tripType: cab.tripType.value,
	                    toDate: convertDateUTC(cab.assignDate),
	                    fromDate: convertDateUTC(cab.rememberDate),
	                    userId: profileId,
	                    combinedFacility: combinedFacilityId,
	                    baseFacility: String(cab.baseFacilityData)
	                };
	                $timeout(function() {
	                    $modalInstance.close(data)
	                }, 3000);

	                $http.post('services/learnRoute/rememberRoute/', data).success(
	                    function(data, status, headers, config) {
	                        if (data.status != "invalidRequest") {
	                            alert("Remember Routes Created Successfully");
	                        }

	                    }).error(function(data, status, headers, config) {});
	            }

	        }

	        //CLOSE BUTTON FUNCTION
	        $scope.cancel = function() {
	            $modalInstance.dismiss('cancel');
	        };

	    };

	    var achocRequestRejectCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, $modal, post, index) {
	        $scope.addRemarks = function(remarks) {
	            var data = {
	                branchId: branchId,
	                requestId: post.requestId,
	                requestRemarks: remarks,
	                userId: profileId,
	                combinedFacility: combinedFacility
	            };

	            $http.post('services/adhoc/employeeRequestRejectance/', data).
	            success(function(data, status, headers, config) {
	                if (data.status != "invalidRequest") {
	                    ngDialog
	                        .open({
	                            template: 'Request rejected successfully with mail notification',
	                            plain: true
	                        });
	                    $timeout(function() {
	                        $modalInstance.close(post);
	                        ngDialog.close();
	                    }, 3000);

	                    var dataIndex = index;
	                    $rootScope.$emit('rejectAchoc', dataIndex);
	                }
	            }).
	            error(function(data, status, headers, config) {
	                // log error
	            });

	        }



	        $scope.cancel = function() {
	            $modalInstance.dismiss('cancel');
	        };

	    };

	    var progressbarSpinnerCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, $modal) {



	        $scope.cancel = function() {
	            $modalInstance.dismiss('cancel');
	        };

	    };

	    var employeeWayPointsCtrl = function($scope, post, employeeWayPointsDetails, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, $modal) {
	        $scope.employeeWayPointsDetails = employeeWayPointsDetails;
	        var baseLatLong =

	            $scope.requestId = post.requestId;
	        $scope.openMap = function(index) {
	            var modalInstance = $modal.open({
	                templateUrl: 'partials/modals/routeMapView.jsp',
	                controller: 'multiLocationMapViewCtrl',
	                size: 'lg',
	                backdrop: 'static',
	                resolve: {
	                    waypoints: function() {
	                        return post.employeeWaypoints.wayPoints[index].LatiLng;
	                    },
	                    baseLatLong: function() {
	                        return post.employeeWaypoints.wayPoints[index].LatiLng;
	                    }
	                }
	            });
	        }

	        $scope.removeLocationDetails = function(post, index) {


	            var LocationId = _.without($scope.employeeWayPointsDetails, _.findWhere($scope.employeeWayPointsDetails, {
	                LocationId: post.LocationId
	            }));

	            var data = {
	                userId: profileId,
	                locationWaypointsIds: String(_.pluck(LocationId, 'LocationId')),
	                requestId: $scope.requestId,
	                combinedFacility: combinedFacility
	            };

	            $http.post('services/trip/updateLocationWayPoints/', data).
	            success(function(data, status, headers, config) {
	                if (data.status != "invalidRequest") {
	                    ngDialog.open({
	                        template: post.LocationName + ' ' + 'Removed Successfully',
	                        plain: true
	                    });
	                    $scope.employeeWayPointsDetails.splice(index, 1);

	                }
	            }).
	            error(function(data, status, headers, config) {
	                // log error
	            });
	        }

	        $scope.cancel = function() {
	            $modalInstance.dismiss('cancel');
	        };

	    };


	    angular.module('efmfmApp').controller('editTravelDeskCtrl',
	        editTravelDeskCtrl);
	    angular.module('efmfmApp').controller('singleMapCtrl', singleMapCtrl);
	    angular.module('efmfmApp').controller('assignCabCtrl', assignCabCtrl);
	    angular.module('efmfmApp').controller('empTravelDeskCtrl',
	        empTravelDeskCtrl);
	    angular.module('efmfmApp').controller('achocRequestRejectCtrl',
	        achocRequestRejectCtrl);
	    angular.module('efmfmApp').controller('progressbarSpinnerCtrl',
	        progressbarSpinnerCtrl);
	    angular.module('efmfmApp').controller('employeeWayPointsCtrl',
	        employeeWayPointsCtrl);



	}());