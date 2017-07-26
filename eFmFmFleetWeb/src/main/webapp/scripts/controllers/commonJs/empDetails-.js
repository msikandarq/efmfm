/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       empDetailCtrl
@Modal Controllers      editEmployeeCtrl, employeeDetailsMapCtrl
@template               partials/home.empDetails.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var empDetailCtrl = function($scope, $http, $filter, $modal, $confirm, ngDialog) {

        if (!$scope.isEmployeeDetailActive || $scope.isEmployeeDetailActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $('.empDetail_admin').addClass('active');
            $('.admin_home').addClass('active');
            $scope.currentPage = 0;
            $scope.pageSize = 10;
            $scope.data = [];
            $scope.q = '';

            $scope.getData = function() {
                return $filter('filter')($scope.data, $scope.q)
            }

            $scope.numberOfPages = function() {
                return Math.ceil($scope.employeeDetailsLength / $scope.pageSize);
            }

            for (var i = 0; i < 300; i++) {
                $scope.data.push("Item " + i);
            }

            $scope.posts = [];
            $scope.gotEmployeeDetailsData = false;
            $scope.employeeTypes = [{
                text: "All",
                value: "All"
            }, {
                text: "Guest Only",
                value: "guest"
            }, {
                text: "Employee Only",
                value: "employee"
            }];
            $scope.employeeType = {
                text: "All",
                value: "All"
            };
            $scope.getEmployeesDetail = function() {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/employee/details/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.gotEmployeeDetailsData = true;
                        $scope.employeeDetailsLength = data.length;
                        $scope.posts = data;
                        angular.forEach($scope.posts, function(item) {
                            if (item.status == 'N') {
                                item.employeeStatusToBe = "Enable";
                            } else if (item.status == 'Y') {
                                item.employeeStatusToBe = "Disable";
                            }
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    $scope.gotEmployeeDetailsData = true;
                });

            };
            $scope.getEmployeesDetail();
            $scope.showSearchResultCount = false;
            $scope.numOfRows = 0;
            $scope.numberofRecords = 1000000;
            $scope.countofFilteredRecords;
            $scope.selectAllClicked = false;
            $scope.deleteAllClicked = false;

            $scope.paginations = [{
                    'value': 10,
                    'text': '10 records'
                },
                {
                    'value': 15,
                    'text': '15 record',
                },
                {
                    'value': 20,
                    'text': '20 records'
                }
            ];
            $scope.shiftsTime = ['6:00 - 8:00',
                '8:00 - 10:00',
                '10:00 - 12:00',
                '12:00 - 14:00',
                '14:00 - 16:00',
                '16:00 - 18:00',
                '18:00 - 20:00',
                '20:00 - 22:00',
                '22:00 - 24:00'
            ];

            $scope.checkBoxModel = {
                value: false
            };

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

            $scope.deleteAll = function() {
                if (!$scope.deleteAllClicked) {
                    $scope.deleteAllClicked = true;
                    angular.forEach($scope.posts, function(item) {
                        item.checkBoxFlag = true;
                    });
                } else {
                    $scope.deleteAllClicked = false;
                    angular.forEach($scope.posts, function(item) {
                        item.checkBoxFlag = false;
                    });
                }
            };

            $scope.employeeStatusChange = function(post) {
                $confirm({
                        text: "Are you sure you want to change the status of this employee?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var dataObj = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            userId: post.userId,
                            buttonType: post.employeeStatusToBe,
                            employeeId: profileId,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/user/deleteemployee/', dataObj).
                        success(function(data, status, headers, config) {

                            if (data.status == 'N') {
                                post.employeeStatusToBe = 'Enable';
                                post.status = 'Y';
                                ngDialog.open({
                                    template: 'Employee status has been change successfully',
                                    plain: true
                                });
                            } else if (data.status == 'Y') {
                                post.employeeStatusToBe = 'Disable';
                                post.status = 'N';
                                ngDialog.open({
                                    template: 'Employee status has been change successfully',
                                    plain: true
                                });
                            } else {
                                ngDialog.open({
                                    template: "Invalid Request",
                                    plain: true
                                });
                            }

                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });


            };

            $scope.employeeGeoCode = function(post) {
                var employeeGeoCodeStatusLabel = null;
                if (post.locationStatus == 'Y') {
                    employeeGeoCodeStatusLabel = 'Enable Geo-Code';
                } else employeeGeoCodeStatusLabel = 'Disable Geo-Code';
                $confirm({
                        text: "Are you sure you want to " + employeeGeoCodeStatusLabel + " Employee Geo-Code?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        var dataObj = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            userId: post.userId,
                            buttonType: post.locationStatus,
                            employeeId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/user/empGeoCodeStatus/', dataObj).
                        success(function(data, status, headers, config) {
                            if (data.status == 'disable') {
                                ngDialog.open({
                                    template: 'Sorry you can not enable Goe-Code, this employee already disable in system.',
                                    plain: true
                                });
                            } else {
                                if (post.locationStatus == 'Y') {
                                    post.locationStatus = 'N';
                                } else if (post.locationStatus == 'N') {
                                    post.locationStatus = 'Y';
                                }
                                ngDialog.open({
                                    template: 'Employee Goe-Code status has been change successfully',
                                    plain: true
                                });
                            }
                        }).

                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });


            };

            //THIS FUNCTION IS CALLED WHEN THE CHECKBOX IS CLICKED     
            $scope.select_thisEmployee = function(post) {
                if (!post.checkBoxFlag) {
                    post.checkBoxFlag = true;
                } else {
                    post.checkBoxFlag = false;
                    $scope.selectAllClicked = false;
                    $scope.deleteAllClicked = false;
                }
            };

            //Set the Limit of ng-repeat in <tr>
            $scope.setLimit = function(showRecords) {
                if (!showRecords) {
                    $scope.numberofRecords = $scope.posts.length;
                } else $scope.numberofRecords = showRecords.value;
            };

            $scope.openMap = function(post, size) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/editEmpDetailMapLocation.jsp',
                    controller: 'employeeDetailsMapCtrl',
                    size: size,
                    resolve: {
                        employee: function() {
                            return post;
                        },
                        branchId: function() {
                            return branchId;
                        },
                        officeLocation: function() {
                            return $scope.officeLocation;
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    post.employeeLatiLongi = result.cords;
                    post.employeeAddress = result.empaddress;

                    var dataObj = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        userId: post.userId,
                        latitudeLongitude: result.cords,
                        address: result.address,
                        employeeId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/user/updateuseraddress/', dataObj).
                    success(function(data, status, headers, config) {
                        if (data.status = 'success') {
                            ngDialog.open({
                                template: 'Geocodes updated successfully',
                                plain: true
                            });
                        } else {
                            ngDialog.open({
                                template: 'Please check Address',
                                plain: true
                            });
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });


                });
            };

            $scope.editEmployee = function(post, size) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/editEmployeeDetails.jsp',
                    controller: 'editEmployeeCtrl',
                    size: size,
                    resolve: {
                        employee: function() {
                            return post;
                        },
                        branchId: function() {
                            return branchId;
                        },
                        officeLocation: function() {
                            return $scope.officeLocation;
                        },

                    }
                });

                modalInstance.result.then(function(result) {

                    post.employeeName = result.name;
                    post.employeeAddress = result.empaddress;
                    post.mobileNumber = result.number;
                    post.emailId = result.email;
                    post.employeeGender = result.empGender;
                    post.isInjured = result.injuredPeople;
                    post.pragnentLady = result.pregnantEmp;
                    post.physicallyChallenged = result.physicallyChallenge;
                    post.employeeLatiLongi = result.cords;
                    post.hostMobileNumber = result.hostMobileNumber;
                    post.employeeDepartment = result.employeeDepartment;
                });

            };

            $scope.searchEmployees = function(search) {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    employeeId: search,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/employee/empiddetails/', dataObj).
                success(function(data, status, headers, config) {
                    $scope.posts = data;
                    angular.forEach($scope.posts, function(item) {
                        if (item.status == 'N') {
                            item.employeeStatusToBe = "Enable";
                        } else if (item.status == 'Y') {
                            item.employeeStatusToBe = "Disable";
                        }
                    });

                }).
                error(function(data, status, headers, config) {});
            };

            $scope.$watch("searchEmployeeReported", function(query) {
                if ($scope.searchEmployeeReported == '') {
                    $scope.showSearchResultCount = false;
                } else
                    $scope.showSearchResultCount = true;

                $scope.countofFilteredRecords = $filter("filter")($scope.posts, query);
            });

            $scope.refreshEmpDetail = function() {
                $scope.getEmployeesDetail();
            };

            $scope.setEmployeeType = function(employeeType) {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userType: employeeType.value,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/employee/userTypeDetails/', dataObj).
                success(function(data, status, headers, config) {
                    $scope.gotEmployeeDetailsData = true;
                    $scope.posts = data;
                    $scope.postsLength = data.length;
                    angular.forEach($scope.posts, function(item) {
                        if (item.status == 'N') {
                            item.employeeStatusToBe = "Enable";
                        } else if (item.status == 'Y') {
                            item.employeeStatusToBe = "Disable";
                        }
                    });
                }).
                error(function(data, status, headers, config) {
                    $scope.gotEmployeeDetailsData = true;
                });




            };

            $scope.saveInExcel = function() {
                $scope.reportSUExcel = [];
                angular.forEach($scope.posts, function(item) {
                    var gender;
                    if (item.employeeGender == 1) {
                        gender = "Male"
                    } else {
                        gender = "Female"
                    }
                    $scope.reportSUExcel.push({
                        'Employee Id': item.employeeId,
                        'Employee Name': item.employeeName,
                        'Employee Address': item.employeeAddress,
                        'Employee Gender': gender,
                        'Employee Number': item.mobileNumber,
                        'Employee EmailId': item.emailId
                    });
                });

                var sheetLabel = "Employee Roster";
                var opts = [{
                    sheetid: sheetLabel,
                    header: true
                }];
                alasql('SELECT INTO XLSX("employeeRoster.xlsx",?) FROM ?', [opts, [$scope.reportSUExcel]]);

            };

        }

    };

    var employeeDetailsMapCtrl = function($scope, $modalInstance, $state, employee, branchId, $timeout, ngDialog, officeLocation) {
        var latlong_center;
        if (employee.employeeLatiLongi == null) {
            latlong_center = officeLocation;
        } else {
            latlong_center = employee.employeeLatiLongi.split(',');
        }
        $scope.alertMessage;
        $scope.alertHint;
        $scope.mapIsLoaded = true;
        $scope.user = {
            address: '',
            cords: '',
            search: ''
        };
        $scope.loc = {
            lat: latlong_center[0],
            lon: latlong_center[1]
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

    var editEmployeeCtrl = function($scope, $modalInstance, $state, $http, $timeout, employee, branchId, ngDialog, officeLocation) {
        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
        var d = new Date();
        d.setHours(00);
        d.setMinutes(0);
        $scope.allZonesData = [];
        $scope.physicalChallenge = ['YES', 'NO'];
        $scope.injuredPeople = ['YES', 'NO'];
        $scope.isVip = ['Yes', 'No'];
        $scope.pregnantEmp = ['YES', 'NO'];
        $scope.employeeGen = ['Male', 'Female'];
        $scope.update = {};
        var isSeqChange;
        var todaysDate = new Date();
        $scope.update.daysModel = [];
        var shiftTimeIndex;
        $scope.alertMessage;
        $scope.alertHint;

        $scope.daysData = [{
                'id': 'Sunday',
                label: 'Sunday'
            },
            {
                'id': 'Monday',
                label: 'Monday'
            },
            {
                'id': 'Tuesday',
                label: 'Tuesday'
            },
            {
                'id': 'Wednesday',
                label: 'Wednesday'
            },
            {
                'id': 'Thursday',
                label: 'Thursday'
            },
            {
                'id': 'Friday',
                label: 'Friday'
            },
            {
                'id': 'Saturday',
                label: 'Saturday'
            }
        ];

        $scope.daysData = [{
                'id': 'Sunday',
                label: 'Sunday'
            },
            {
                'id': 'Monday',
                label: 'Monday'
            },
            {
                'id': 'Tuesday',
                label: 'Tuesday'
            },
            {
                'id': 'Wednesday',
                label: 'Wednesday'
            },
            {
                'id': 'Thursday',
                label: 'Thursday'
            },
            {
                'id': 'Friday',
                label: 'Friday'
            },
            {
                'id': 'Saturday',
                label: 'Saturday'
            }
        ];

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

        //****************************************************************************//
        var latlong_center;
        if (employee.employeeLatiLongi == null) {
            latlong_center = officeLocation;

        } else {
            latlong_center = employee.employeeLatiLongi.split(',');


        }
        $scope.alertMessage;
        $scope.alertHint;
        $scope.mapIsLoaded = true;
        $scope.user = {
            address: '',
            cords: '',
            search: ''
        };
        $scope.loc = {
            lat: latlong_center[0],
            lon: latlong_center[1]
        };



        //************************************************************************//

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

        $scope.getAreaNames = function() {
            $http.post('services/zones/allAreas/').
            success(function(data, status, headers, config) {

                $scope.allZonesData = data.areaDetails;

            }).
            error(function(data, status, headers, config) {});
        };
        $scope.getAreaNames();

        $scope.getRouteNames = function() {
            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                userId: profileId

            };
            $http.post('services/trip/editemployeetravelrequest/', data).
            success(function(data, status, headers, config) {

                $scope.allRouteData = data.routesData;

            }).
            error(function(data, status, headers, config) {});
        };
        $scope.getNodalPoints = function() {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                combinedFacility: combinedFacility

            };
            $http.post('services/trip/allNodalPoints/', data).
            success(function(data, status, headers, config) {
                $scope.nodalPointsdata = data.zones;
            }).
            error(function(data, status, headers, config) {});
        };


        $scope.setRouteSelected = function(routeName) {

            var data = {
                efmFmUserMaster: {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    }
                },
                routeId: routeName.routeId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            alert(JSON.stringify(data));
            $http.post('services/trip/demo/', data).
            success(function(data, status, headers, config) {
                $scope.allZonesData = data.routesData;
            }).
            error(function(data, status, headers, config) {});
        }
        $scope.getRouteNames();
        $scope.getNodalPoints();

        $scope.IntegerNumber = /^\d+$/;

        if (employee.employeeGender == 1) {
            employee.employeeGender = "Male"
        }
        if (employee.employeeGender == 2) {
            employee.employeeGender = "Female"
        }
        if (employee.employeeGender == 'Male') {
            employee.employeeGender = "Male"
        }
        if (employee.employeeGender == 'Female') {
            employee.employeeGender = "Female"
        }




        $scope.user = {
            'employeeId': employee.employeeId,
            'name': employee.employeeName,
            'employeeDesignation': employee.employeeDesignation,
            'number': employee.mobileNumber,
            'email': employee.emailId,
            'empaddress': employee.employeeAddress,
            'areaName': {
                areaName: employee.areaName,
                areaId: employee.areaId
            },
            'routeName': {
                routeName: employee.routeName,
                routeId: employee.routeId
            },
            'isVIP': employee.isVIP,
            'nodalPointName': {
                nodalPointName: employee.nodalPointName,
                nodalPointId: employee.nodalPointId
            },

            'physicallyChallenge': employee.physicallyChallenged,
            'injuredPeople': employee.isInjured,
            'pregnantEmp': employee.pragnentLady,
            'empGender': employee.employeeGender,
            'employeeDepartment': employee.employeeDepartment,
            'hostMobileNumber': employee.hostMobileNumber
        };
        //***************************************************************************************//
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
        //***************************************************************************************//

        $scope.updateEmployee = function(updatedEmployee, update, loc) {

            if (updatedEmployee.empGender == "Male") {
                updatedEmployee.empGender = 1;
            } else {
                updatedEmployee.empGender = 2;
            }


            $scope.lat = loc.lat;
            $scope.lon = loc.lon;
            $scope.cords = $scope.lat + ',' + $scope.lon;
            if (updatedEmployee.cords == undefined) {
                updatedEmployee.cords = $scope.cords;
            } else {
                updatedEmployee.cords = updatedEmployee.cords;
            }
            update.selectedDays = update.daysModel.map(function(el) {
                return el.id;
            }).join();

            //Watch for the Days Of String
            if (updatedEmployee.selectedDays != employee.weekOffs) {
                isDaysOffChange = true;
            } else {
                isDaysOffChange = false;
            }


            if (updatedEmployee.selectedDays == '') {
                ngDialog.open({
                    template: 'Kindly Select Days Off',
                    plain: true
                });
                return false;
            }

            if (updatedEmployee.empaddress != employee.employeeAddress && updatedEmployee.cords == undefined) {
                ngDialog.open({
                    template: 'You have change your address, Please enter the same address in the Geo Location Text box.',
                    plain: true
                });
            } else {
                var dataObj = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    employeeId: updatedEmployee.employeeId,
                    mobileNumber: updatedEmployee.number,
                    emailId: updatedEmployee.email,
                    firstName: updatedEmployee.name,
                    employeeDesignation: updatedEmployee.employeeDesignation,
                    address: updatedEmployee.empaddress,
                    latitudeLongitude: updatedEmployee.cords,
                    physicallyChallenged: updatedEmployee.physicallyChallenge,
                    pragnentLady: updatedEmployee.pregnantEmp,
                    isInjured: updatedEmployee.injuredPeople,
                    isVIP: updatedEmployee.isVIP,
                    nodalPointId: updatedEmployee.nodalPointName.nodalPointId,

                    routeId: updatedEmployee.routeName.routeId,

                    areaId: updatedEmployee.areaName.areaId,
                    weekOffDays: update.selectedDays,
                    userId: profileId,
                    employeeDepartment: updatedEmployee.employeeDepartment,
                    gender: updatedEmployee.empGender,
                    hostMobileNumber: updatedEmployee.hostMobileNumber,
                    combinedFacility: combinedFacility
                };

                $http.post('services/user/updateempdetails/', dataObj).
                success(function(data, status, headers, config) {
                    if (data.status == 'success') {
                        ngDialog.open({
                            template: 'Employee Details updated successfully.',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(updatedEmployee);
                            ngDialog.close();
                        }, 3000)

                    } else if (data.status == 'changeNodalPoint') {
                        ngDialog.open({
                            template: 'You selected home route please change nodal point name to default.',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(updatedEmployee);
                            ngDialog.close();
                        }, 8000)
                    } else if (data.status == 'changeToDefault') {
                        ngDialog.open({
                            template: 'Please select nodal point name.',
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(updatedEmployee);
                            ngDialog.close();
                        }, 8000)

                    } else if (data.status == 'mExist') {
                        ngDialog.open({
                            template: 'This mobile number is already attached with some other employee Id-' + data.employeeId,
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(updatedEmployee);
                            ngDialog.close();
                        }, 8000)
                    } else if (data.status == 'eExist') {
                        ngDialog.open({
                            template: 'This emailID is already attached with some other employee Id-' + data.employeeId,
                            plain: true
                        });
                        $timeout(function() {
                            $modalInstance.close(updatedEmployee);
                            ngDialog.close();
                        }, 8000)
                    } else {
                        ngDialog.open({
                            template: 'Please check Employee Details.',
                            plain: true
                        });
                    }

                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            }

        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    angular.module('efmfmApp').controller('employeeDetailsMapCtrl', employeeDetailsMapCtrl);
    angular.module('efmfmApp').controller('editEmployeeCtrl', editEmployeeCtrl);
    angular.module('efmfmApp').controller('empDetailCtrl', empDetailCtrl);
}());