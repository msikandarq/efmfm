/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description
@Main Controllers       routeCtrl
@Modal Controllers
@template               partials/home.routeMap.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var routeCtrl = function($scope, $stateParams, $state, $http, $interval, $rootScope, NgMap) {
        $('.veiwMapMenu').addClass('active');
        $scope.singleDataLoaded = false;
        $scope.routeId = $stateParams.routeId;
        $scope.singleMapData;
        $scope.cabLocation;
        $scope.mapData = [];
        var data = {
            eFmFmClientBranchPO: {
                branchId: branchId
            },
            assignRouteId: $scope.routeId,
            combinedFacility: combinedFacility
        };
        $http.post('services/view/individualtrip/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                $scope.singleRouteData = data[0];
                $scope.mapData = data;
                $scope.originLocation = $scope.singleRouteData.baseLatLong;
                $scope.destinLocation = $scope.singleRouteData.baseLatLong;
                $scope.mapCenter = $scope.singleRouteData.currentCabLatiLongi;
                $scope.singleDataLoaded = true;
                $scope.coorduinates = [];
                $rootScope.wayPoints = [];
                $scope.endPoint = [];
                $scope.TechielatitudeLongitude = null;
                $interval(function() {
                    $scope.getcurrentCabData = function() {
                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            assignRouteId: $scope.routeId,
                            userId: profileId
                        };

                        $http.post('services/view/individualtrip/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.mapCenter = data[0].currentCabLatiLongi;
                                var cablatlng = $scope.mapCenter.split(",");
                                var cabobj = {
                                    userLocations: [parseFloat(cablatlng[0]), parseFloat(cablatlng[1])],
                                    address: "Address:" + $scope.singleRouteData.currentLocation,
                                    icon: 'images/cabImage.png'
                                }
                                $scope.coorduinates[0] = cabobj;
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    };

                    $scope.getcurrentCabData();
                }, 4000);
                var cablatlng = $scope.mapCenter.split(",");
                var cabobj = {
                    userLocations: [parseFloat(cablatlng[0]), parseFloat(cablatlng[1])],
                    address: "Address:" + $scope.singleRouteData.currentLocation,
                    icon: 'images/cabImage.png'
                }
                $scope.coorduinates.push(cabobj);
                angular.forEach($scope.singleRouteData.empDetails, function(value, key) {
                    if (value != null) {
                        var latlng = value.emplatlng.split(",");
                        var obj = {
                            userLocations: [parseFloat(latlng[0]), parseFloat(latlng[1])],
                            id: value.appointmentId,
                            address: value.address,
                            name: value.name,
                            customer: true
                        }
                        if (value.gender.toUpperCase() == "MALE") {
                            obj.icon = 'images/EmployeeMapIcons/emp_' + (key + 1) + '.png';
                        } else {
                            obj.icon = 'images/EmployeeMapIcons/female_emp_' + (key + 1) + '.png';
                        }
                        $scope.coorduinates.push(obj);
                    }
                });
                var waypointsArray = $scope.singleRouteData.waypoints.split("|");
                angular.forEach(waypointsArray, function(value, key) {
                    if (value) {
                        var latlng = value.split(",");
                        $rootScope.wayPoints.push({
                            location: {
                                lat: parseFloat(latlng[0]),
                                lng: parseFloat(latlng[1])
                            },
                            stopover: true
                        });
                    }
                });
                $scope.mapRefresh = true;
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });


        $scope.$on('mapInitialized', function(event, map) {
            $scope.objMapa = map;
        });

        $scope.showInfoWindow = function(event, p) {
            var infowindow = new google.maps.InfoWindow();
            var center = new google.maps.LatLng(p.userLocations[0], p.userLocations[1]);

            infowindow.setContent(
                '<span> Name: ' + p.name + '</span><br><span> Address: ' + p.address + '</span>');

            infowindow.setPosition(center);
            infowindow.open($scope.objMapa);
        };


        $scope.getMapData = function() {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                assignRouteId: $scope.routeId,
                userId: profileId,
                combinedFacility: combinedFacility
            };

            $http.post('services/view/individualtrip/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.mapData = data;
                    $scope.expectedTime = $scope.mapData[0].expectedTime;
                    $scope.speed = $scope.mapData[0].speed;
                    $scope.plannedDistance = $scope.mapData[0].plannedDistance;
                    $scope.travelledDistance = $scope.mapData[0].travelledDistance;

                    $scope.cabLocation = $scope.mapData[0].currentCabLatiLongi;
                    $scope.singleDataLoaded = true;
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        };

        $scope.getMapData();


        $scope.getSingleMap = function() {
            return $scope.mapData;
        };


        $scope.showAll = function() {
            $scope.cabLocation = [];
            $state.go('home.viewmap');
            $interval.cancel($scope.routeMapInterval);
        };

        $scope.routeMapInterval = $interval($scope.getMapData, 6000);

        $scope.$watch(
            function($scope) {
                return $scope.cabLocation;
            },
            function(newValue, oldvalue) {
                if (oldvalue !== newValue) {}
            }
        );

        $scope.$watch(
            function($scope) {
                return $scope.expectedTime;
            },
            function(newValue, oldvalue) {
                if (oldvalue !== newValue) {
                    $scope.mapData[0].expectedTime = newValue;
                }
            }
        );

        $scope.$watch(
            function($scope) {
                return $scope.speed;
            },
            function(newValue, oldvalue) {
                if (oldvalue !== newValue) {
                    $scope.mapData[0].speed = newValue;
                }
            }
        );

        $scope.$watch(
            function($scope) {
                return $scope.plannedDistance;
            },
            function(newValue, oldvalue) {
                if (oldvalue !== newValue) {
                    $scope.mapData[0].plannedDistance = newValue;
                }
            }
        );

        $scope.$watch(
            function($scope) {
                return $scope.travelledDistance;
            },
            function(newValue, oldvalue) {
                if (oldvalue !== newValue) {
                    $scope.mapData[0].travelledDistance = newValue;
                }
            }
        );

    };

    angular.module('efmfmApp').controller('routeCtrl', routeCtrl);
}());