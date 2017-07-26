/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       addMultipleRequestCreateCtrl
@Modal Controllers      
@template               controllers/commonJs/addMultipleRequestCreateCtrl.js

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
27/04/2017  Kathiravan    Initial Creation
27/04/2017  Kathiravan      Final Creation
*/
(function() {
    var addMultipleRequestCreateCtrl = function($scope, $rootScope, $state, $modalInstance, $http, $location, $interval, $modal, $timeout, ngDialog, rawData) {

        if (rawData.status) {
            $scope.destinationPoints = rawData.data;
        } else {
            $scope.destinationPoints = [{}];
        }

        $scope.timeTypeNormalShow = false;
        $scope.timeTypeAdhocShow = false;
        $scope.timeTypeDisabled = true;
        $scope.timeTypeReadOnly = true;
        $scope.sdatePicker = [];
        $scope.edatePicker = [];
        $scope.minDate = [];
        $scope.endMinDate = [];
        $scope.minDate[0] = new Date();
        $scope.endMinDate[0] = new Date();
        var objectPrePosition = 0;
        $scope.addDestination = function(index) {
            $scope.minDate.push(new Date());
            objectPrePosition = objectPrePosition + index;
            objectPosition = objectPrePosition - 1;
            if ($scope.destinationPoints[objectPosition].startDate && $scope.destinationPoints[objectPosition].endDate &&
                $scope.destinationPoints[objectPosition].tripType && $scope.destinationPoints[objectPosition].timeType) {
                $scope.destinationPoints.push({
                    destination: ''
                });
            } else {
                objectPrePosition = objectPrePosition - 1;
                ngDialog.open({
                    template: 'Fill up the required information',
                    plain: true
                });
            }

        }
        $scope.reqstartDate = function($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.sdatePicker[index] = true;
            }, 50);
        };
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
        $scope.reqendDate = function($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.edatePicker[index] = true;
            }, 50);
        };
        $scope.format = 'dd-MM-yyyy';

        $rootScope.$on('deleteLocationList', function(event, data) {
            $scope.destinationPoints.splice(data - 1, 1);
            var data = $scope.destinationPoints;
            $scope.sendRecordToLocationDetails();

        });

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
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $rootScope.locationLoad();




        $scope.setShiftTimes = function(index, requestDetails) {


            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/shiftTimeMultiRequest.jsp',
                controller: 'shiftTimeMultiRequestCtrl',
                backdrop: 'static',
                resolve: {
                    requestDetails: function() {
                        return requestDetails;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                $scope.destinationPoints[index]['tripType'] = result.tripType;
                $scope.destinationPoints[index]['timeType'] = result.timeType;

                if (result.tripType.value == 'BOTH' && result.timeType.value == "NORMAL") {
                    $scope.destinationPoints[index]['pickupTimeNomal'] = result.pickupTimeNomal;
                    $scope.destinationPoints[index]['dropTimeNormal'] = result.dropTimeNormal;
                } else if (result.tripType.value == 'PICKUP' && result.timeType.value == "NORMAL") {
                    $scope.destinationPoints[index]['pickupTimeNomal'] = result.pickupTimeNomal;
                    $scope.destinationPoints[index]['dropTimeNormal'] = '';
                } else if (result.tripType.value == 'DROP' && result.timeType.value == "NORMAL") {
                    $scope.destinationPoints[index]['dropTimeNormal'] = result.dropTimeNormal;
                    $scope.destinationPoints[index]['pickupTimeNomal'] = '';
                }

                if (result.tripType.value == 'BOTH' && result.timeType.value == "ADHOC") {
                    $scope.destinationPoints[index]['pickupTimeAdhoc'] = result.pickupTimeAdhoc;
                    $scope.destinationPoints[index]['dropTimeAdhoc'] = result.dropTimeAdhoc;
                } else if (result.tripType.value == 'PICKUP' && result.timeType.value == "ADHOC") {
                    $scope.destinationPoints[index]['pickupTimeAdhoc'] = result.pickupTimeAdhoc;
                    $scope.destinationPoints[index]['dropTimeAdhoc'] = '';
                } else if (result.tripType.value == 'DROP' && result.timeType.value == "ADHOC") {
                    $scope.destinationPoints[index]['dropTimeAdhoc'] = result.dropTimeAdhoc;
                    $scope.destinationPoints[index]['pickupTimeAdhoc'] = '';
                }

            });
        }

        $scope.setPickupLocation = function(index, requestDetails, type) {

            var data;
            if (type == 'edit') {
                data = {
                    edit: true,
                    destination: requestDetails
                }
            } else {
                data = {
                    edit: false,
                    destination: requestDetails
                }
            }
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/setPickupLocationModal.jsp',
                controller: 'setPickupLocationCtrl',
                backdrop: 'static',
                resolve: {
                    requestData: function() {
                        return data;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                $scope.destinationPoints[index]['startAreaPickup'] = result.startAreaPickup;
                $scope.destinationPoints[index]['endAreaPickup'] = result.endAreaPickup;
            });
        }

        $scope.setDropLocation = function(index, requestDetails, type) {

            var data;
            if (type == 'edit') {
                data = {
                    edit: true,
                    destination: requestDetails
                }
            } else {
                data = {
                    edit: false,
                    destination: requestDetails
                }
            }
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/setDropLocationModal.jsp',
                controller: 'setDropLocationCtrl',
                backdrop: 'static',
                resolve: {
                    requestData: function() {
                        return data;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                $scope.destinationPoints[index]['startAreaDrop'] = result.startAreaDrop;
                $scope.destinationPoints[index]['endAreaDrop'] = result.endAreaDrop;
            });
        }


        $scope.addMapDestination = function(index, requestDetails, type) {


            $scope.selectedIndex = index;
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/addMapDestinationModal.jsp',
                controller: 'createMapLocationCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    index: function() {
                        return index;
                    },
                    requestDetails: function() {
                        return requestDetails;
                    }

                }
            });

            modalInstance.result.then(function(result) {

                $rootScope.locationLoad();
                var indexLast = _.findLastIndex($scope.allZonesData, {
                    areaName: result.areaName
                });

                var multipleLocation = $scope.allZonesData[indexLast]


                if (type == 'startArea') {
                    $scope.destinationPoints[index]['startArea'] = multipleLocation;

                } else {
                    $scope.destinationPoints[index]['endArea'] = multipleLocation;
                }

            });

        }
        $rootScope.saveMultipleRequest = function(destinationPoints) {
            $scope.multipleRequest = [];
            angular.forEach(destinationPoints, function(item, key) {
                var obj = {};

                if (item.tripType.value == "PICKUP") {
                    if (item.timeType.value == 'ADHOC') {
                        obj.timeFlg = 'A';
                        obj.pickupTime = convertToTime(item.pickupTimeAdhoc);
                    } else {
                        obj.timeFlg = 'S';
                        obj.pickupTime = item.pickupTimeNomal.shiftTime;
                    }
                    obj.tripType = item.tripType.value;
                    obj.startDate = convertDateUTC(item.startDate);
                    obj.endDate = convertDateUTC(item.endDate);
                    obj.startPickupLocation = item.startAreaPickup.areaId;
                    obj.endPickupLocation = item.endAreaPickup.areaId;
                } else if (item.tripType.value == "DROP") {
                    if (item.timeType.value == 'ADHOC') {
                        obj.timeFlg = 'A';
                        obj.dropTime = convertToTime(item.dropTimeAdhoc);
                    } else {
                        obj.timeFlg = 'S';
                        obj.dropTime = item.dropTimeNormal.shiftTime;
                    }
                    obj.tripType = item.tripType.value;
                    obj.startDate = convertDateUTC(item.startDate);
                    obj.endDate = convertDateUTC(item.endDate);
                    obj.startDropLocation = item.startAreaDrop.areaId;
                    obj.endDropLocation = item.endAreaDrop.areaId;
                } else if (item.tripType.value == "BOTH") {
                    if (item.timeType.value == 'ADHOC') {
                        obj.timeFlg = 'A';
                        obj.pickupTime = convertToTime(item.pickupTimeAdhoc);
                        obj.dropTime = convertToTime(item.dropTimeAdhoc);
                    } else {
                        obj.timeFlg = 'S';
                        obj.pickupTime = item.pickupTimeNomal.shiftTime;
                        obj.dropTime = item.dropTimeNormal.shiftTime;
                    }
                    obj.tripType = item.tripType.value;
                    obj.startDate = convertDateUTC(item.startDate);
                    obj.endDate = convertDateUTC(item.endDate);
                    obj.startPickupLocation = item.startAreaPickup.areaId;
                    obj.startDropLocation = item.startAreaDrop.areaId;
                    obj.endPickupLocation = item.endAreaPickup.areaId;
                    obj.endDropLocation = item.endAreaDrop.areaId;
                }

                $scope.multipleRequest.push(obj);
            });

            var data = {
                modified: $scope.multipleRequest,
                rawData: $scope.destinationPoints
            }
            ngDialog.open({
                template: 'Requests are added',
                plain: true
            });
            $modalInstance.close(data);

        }

        $rootScope.deleteDestinationLocation = function(destinationPoint, index) {
            $scope.destinationPoints.splice(index, 1);

        }
        $scope.startDateSet = function(startDate, index) {

            $scope.destinationPoints[index]['startDate'] = startDate;
            $scope.endMinDate[index] = startDate;
        };
        $scope.endDateSet = function(endDate, index) {

            $scope.destinationPoints[index]['endDate'] = endDate;
        };
        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    }

    var startAndEndDatePickerCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog) {


        alert();
        $scope.startDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'startDate': true
                };
            }, 50);
        };

        $scope.endDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'endDate': true
                };
            }, 50);
        };



        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var createMapLocationCtrl = function($scope, $rootScope, $modalInstance, $state, $timeout, ngDialog, $http, index) {

        var latlong_center;
        var requestDetails = officeLocation;
        var latLongValues = requestDetails.split(',');
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
            user.index = $scope.index;
            if (userRole == 'admin') {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    isActive: 'A',
                    locationName: user.areaName,
                    locationAddress: user.address,
                    locationLatLng: user.cords,
                    combinedFacility: combinedFacility
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
                    combinedFacility: combinedFacility
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

    var shiftTimeMultiRequestCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, requestDetails) {

        $scope.shiftTimeDetails = {};
        $scope.shiftTimeDetails.tripType = requestDetails[0].tripType;

        $scope.hstep = 1;
        $scope.mstep = 1;
        $scope.ismeridian = false;
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

        $scope.timeTypes = [{
                'value': 'NORMAL',
                'text': 'NORMAL'
            },
            {
                'value': 'ADHOC',
                'text': 'ADHOC'
            }
        ];

        // Drop Shift Time

        $scope.getDropShiftTime = function() {
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
                tripType: "DROP",
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/trip/tripshiftime/', data).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.shiftsTimeDataDrop = _.uniq(data.shift, function(p){ return p.shiftTime; });
                    }
                }).error(function(data, status, headers, config) {});
        }
        $scope.getDropShiftTime();

        // Pickup Shift Time

        $scope.getPickupShiftTime = function() {
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
                tripType: "PICKUP",
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/trip/tripshiftime/', data).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.shiftsTimeDataPickup = _.uniq(data.shift, function(p){ return p.shiftTime; });
                    }
                }).error(function(data, status, headers, config) {});
        }
        $scope.getPickupShiftTime();

        $scope.setTimeType = function(timeType, tripType) {
            if (tripType == undefined) {
                return false;
            }
            if (tripType.value == 'BOTH' && timeType.value == "NORMAL") {
                $scope.pickupTripTimeShow = true;
                $scope.dropTripTimeShow = true;
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTimeNormalRequired = true;
                $scope.dropTimeNormalRequired = true;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            } else if (tripType.value == 'PICKUP' && timeType.value == "NORMAL") {
                $scope.pickupTripTimeShow = true;
                $scope.dropTripTimeShow = false;
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTimeNormalRequired = true;
                $scope.dropTimeNormalRequired = false;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            } else if (tripType.value == 'DROP' && timeType.value == "NORMAL") {
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = true;
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTimeNormalRequired = false;
                $scope.dropTimeNormalRequired = true;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            }

            if (tripType.value == 'BOTH' && timeType.value == "ADHOC") {
                $scope.pickupTripAdhocShow = true;
                $scope.dropTripAdhocShow = true;
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = false;
                $scope.pickupTimeNormalRequired = false;
                $scope.dropTimeNormalRequired = false;
                $scope.pickupTimeAdhocRequired = true;
                $scope.dropTimeAdhocRequired = true;
            } else if (tripType.value == 'PICKUP' && timeType.value == "ADHOC") {
                $scope.pickupTripAdhocShow = true;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = false;
                $scope.pickupTimeNormalRequired = false;
                $scope.dropTimeNormalRequired = false;
                $scope.pickupTimeAdhocRequired = true;
                $scope.dropTimeAdhocRequired = false;
            } else if (tripType.value == 'DROP' && timeType.value == "ADHOC") {
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = true;
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = false;
                $scope.pickupTimeNormalRequired = false;
                $scope.dropTimeNormalRequired = false;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = true;
            }
        }

        $scope.setTripType = function(timeType, tripType) {



            if (timeType == undefined) {
                return false;
            }

            if (tripType.value == 'BOTH' && timeType.value == "NORMAL") {
                $scope.pickupTripTimeShow = true;
                $scope.dropTripTimeShow = true;
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTimeNormalRequired = true;
                $scope.dropTimeNormalRequired = true;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            } else if (tripType.value == 'PICKUP' && timeType.value == "NORMAL") {
                $scope.pickupTripTimeShow = true;
                $scope.dropTripTimeShow = false;
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTimeNormalRequired = true;
                $scope.dropTimeNormalRequired = false;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            } else if (tripType.value == 'DROP' && timeType.value == "NORMAL") {
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = true;
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTimeNormalRequired = false;
                $scope.dropTimeNormalRequired = true;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            }

            if (tripType.value == 'BOTH' && timeType.value == "ADHOC") {
                $scope.pickupTripAdhocShow = true;
                $scope.dropTripAdhocShow = true;
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = false;
                $scope.pickupTimeNormalRequired = true;
                $scope.dropTimeNormalRequired = true;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;

            } else if (tripType.value == 'PICKUP' && timeType.value == "ADHOC") {
                $scope.pickupTripAdhocShow = true;
                $scope.dropTripAdhocShow = false;
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = false;
                $scope.pickupTimeNormalRequired = true;
                $scope.dropTimeNormalRequired = false;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            } else if (tripType.value == 'DROP' && timeType.value == "ADHOC") {
                $scope.pickupTripAdhocShow = false;
                $scope.dropTripAdhocShow = true;
                $scope.pickupTripTimeShow = false;
                $scope.dropTripTimeShow = false;
                $scope.pickupTimeNormalRequired = false;
                $scope.dropTimeNormalRequired = true;
                $scope.pickupTimeAdhocRequired = false;
                $scope.dropTimeAdhocRequired = false;
            }

        }


        $scope.submitShiftTimeDetails = function(data) {

            if (data.tripType.value == 'BOTH' && data.timeType.value == "NORMAL") {
                $scope.shiftTimeDeatails = [];
                $scope.shiftTimeDeatails['tripType'] = data.tripType;
                $scope.shiftTimeDeatails['timeType'] = data.timeType;
                $scope.shiftTimeDeatails['pickupTimeNomal'] = data.pickupTimeNomal;
                $scope.shiftTimeDeatails['dropTimeNormal'] = data.dropTimeNormal;


            } else if (data.tripType.value == 'PICKUP' && data.timeType.value == "NORMAL") {
                $scope.shiftTimeDeatails = [];
                $scope.shiftTimeDeatails['tripType'] = data.tripType;
                $scope.shiftTimeDeatails['timeType'] = data.timeType;
                $scope.shiftTimeDeatails['pickupTimeNomal'] = data.pickupTimeNomal;
            } else if (data.tripType.value == 'DROP' && data.timeType.value == "NORMAL") {
                $scope.shiftTimeDeatails = [];
                $scope.shiftTimeDeatails['tripType'] = data.tripType;
                $scope.shiftTimeDeatails['timeType'] = data.timeType;
                $scope.shiftTimeDeatails['dropTimeNormal'] = data.dropTimeNormal;
            }

            if (data.tripType.value == 'BOTH' && data.timeType.value == "ADHOC") {
                $scope.shiftTimeDeatails = [];
                $scope.shiftTimeDeatails['tripType'] = data.tripType;
                $scope.shiftTimeDeatails['timeType'] = data.timeType;
                $scope.shiftTimeDeatails['pickupTimeAdhoc'] = data.pickupTimeAdhoc;
                $scope.shiftTimeDeatails['dropTimeAdhoc'] = data.dropTimeAdhoc;

            } else if (data.tripType.value == 'PICKUP' && data.timeType.value == "ADHOC") {
                $scope.shiftTimeDeatails = [];
                $scope.shiftTimeDeatails['tripType'] = data.tripType;
                $scope.shiftTimeDeatails['timeType'] = data.timeType;
                $scope.shiftTimeDeatails['pickupTimeAdhoc'] = data.pickupTimeAdhoc;

            } else if (data.tripType.value == 'DROP' && data.timeType.value == "ADHOC") {
                $scope.shiftTimeDeatails = [];
                $scope.shiftTimeDeatails['tripType'] = data.tripType;
                $scope.shiftTimeDeatails['timeType'] = data.timeType;
                $scope.shiftTimeDeatails['dropTimeAdhoc'] = data.dropTimeAdhoc;

            }


            $modalInstance.close($scope.shiftTimeDeatails);
        }

        //Function to check the Date Validity

        $scope.checkDateRangeValidity = function(startDate, endDate) {
            if (startDate > endDate) {
                $scope.dateRangeError = true;
                $timeout(function() {
                    $scope.dateRangeError = false;
                }, 3000);
                return false;
            } else return true;
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var setPickupLocationCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, requestData) {

        if (requestData.edit) {
            $scope.addPickupLocation = false;
            $scope.location = {
                startAreaPickup: requestData.destination.startAreaPickup,
                endAreaPickup: requestData.destination.endAreaPickup
            };
        } else {
            $scope.addPickupLocation = true;
        }

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
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $rootScope.locationLoad();

        $scope.saveAddPickUplocation = function(data) {

            $modalInstance.close(data);
        }


        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var setDropLocationCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, requestData) {

        if (requestData.edit) {
            $scope.addDropLocation = false;
            $scope.location = {
                startAreaDrop: requestData.destination.startAreaDrop,
                endAreaDrop: requestData.destination.endAreaDrop
            };
        } else {
            $scope.addDropLocation = true;
        }
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
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $rootScope.locationLoad();

        $scope.saveAddDroplocation = function(data) {

            $modalInstance.close(data);
        }


        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    angular.module('efmfmApp').controller('addMultipleRequestCreateCtrl', addMultipleRequestCreateCtrl);
    angular.module('efmfmApp').controller('startAndEndDatePickerCtrl', startAndEndDatePickerCtrl);
    angular.module('efmfmApp').controller('createMapLocationCtrl', createMapLocationCtrl);
    angular.module('efmfmApp').controller('shiftTimeMultiRequestCtrl', shiftTimeMultiRequestCtrl);
    angular.module('efmfmApp').controller('setPickupLocationCtrl', setPickupLocationCtrl);
    angular.module('efmfmApp').controller('setDropLocationCtrl', setDropLocationCtrl);



}());