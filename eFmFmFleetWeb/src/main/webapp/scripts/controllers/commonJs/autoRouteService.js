/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       sosAlertPopupCtrl
@Modal Controllers      
@template               partials/alertPopups/sosAlertMessagePopup.jsp

CODE CHANGE HISTORY 
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var autoRouteCtrl = function($scope, $rootScope, $state, $http, $location, $interval, $timeout, ngDialog) {

        $rootScope.$on('autoRoutingValue', function(event, data) {

            var dataAutoRoute = data;
            var data = {
                branchId: branchId,
                time: dataAutoRoute.time,
                zoneId: dataAutoRoute.zoneId,
                tripType: dataAutoRoute.tripType,
                resheduleDate: dataAutoRoute.resheduleDate,
                userId: profileId,
                combinedFacility: combinedFacility
            };


            $http.post('services/algo/createArea/', data).success(
                function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.status == "complete") {
                            ngDialog.open({
                                template: 'Request not available for this shift',
                                plain: true
                            });
                        } else if (dataAutoRoute.tripType == "DROP") {
                            var data = {
                                branchId: branchId,
                                time: dataAutoRoute.time,
                                tripType: dataAutoRoute.tripType,
                                userId: profileId,
                                combinedFacility: combinedFacility
                            };
                            $http.post('services/algo/dropSequence/', data).success(
                                function(data, status, headers, config) {
                                    ngDialog.open({
                                        template: 'Routes Created Successfully',
                                        plain: true
                                    });
                                }).error(function(data, status, headers, config) {});
                        } else {
                            ngDialog.open({
                                template: 'Routes Created Successfully',
                                plain: true
                            });
                        }
                    } else {
                        return false;
                    }


                }).error(function(data, status, headers, config) {});
        });


    };

    angular.module('efmfmApp').controller('autoRouteCtrl', autoRouteCtrl);
}());