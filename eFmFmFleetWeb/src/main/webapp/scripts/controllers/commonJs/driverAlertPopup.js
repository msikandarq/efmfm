/*
@date           04/01/2015
@Author         Saima Aziz
@Description    
@controllers    driverAlertPopupCtrl
@template       main.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var driverAlertPopupCtrl = function($scope, $state, $http, $location, $interval, $timeout) {
        $(".alert_driveLatePopup").hide();
        $scope.alertDescription;

        $scope.getLateCheckOutDrivers = function() {
            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: combinedFacility
            };
            $http.post('services/trip/unreadCheckInDrivers/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                    $scope.driverCheckInAlertData = data;
                    if ($scope.driverCheckInAlertData.length > 0 && $scope.adminRole) {
                        $scope.alertTitle = $scope.driverCheckInAlertData[0].driverName;
                        $scope.alertDescription = "System is going to autocheckout for driver name-" + $scope.alertTitle + " and vehicle Number - " + $scope.driverCheckInAlertData[0].vehicleNumber + "."
                        $(".alert_driveLatePopup").show('slow', function() {
                            $timeout(function() {
                                $(".alert_driveLatePopup").hide('slow');
                            }, 5000);
                        });
                        $.playSound("temp/justLikeThat");
                    }
                }
            }).
            error(function(data, status, headers, config) {});
        };

        $scope.closeLateDriverPoopup = function() {
            $(".alert_driveLatePopup").hide('slow');
        }

        var driverLateInterval = $interval(function() {
            $scope.getLateCheckOutDrivers();
        }, 10000);

    };

    angular.module('efmfmApp').controller('driverAlertPopupCtrl', driverAlertPopupCtrl);
}());