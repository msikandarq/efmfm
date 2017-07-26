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
    var requestDetailsCtrl = function($scope, $timeout, $http, $modal, $state) {
        if ((!$scope.iSRequestDetails || $scope.iSRequestDetails == "false")) {
            $state.go('home.accessDenied');
        } else {
            $('.employeeRequest').addClass('active');
        }
    };

    angular.module('efmfmApp').controller('requestDetailsCtrl', requestDetailsCtrl);
}());