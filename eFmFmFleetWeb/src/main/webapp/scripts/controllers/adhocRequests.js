(function() {
    var adhocRequestCtrl = function($scope, $timeout, $http, $modal, $state) {
        if ((!$scope.isAdhocRequestActive || $scope.isAdhocRequestActive == "false")) {
            $state.go('home.accessDenied');
        } else {
            $('.employeeRequestAdhoc').addClass('active');
        }
    };

    angular.module('efmfmApp').controller('adhocRequestCtrl', adhocRequestCtrl);
}());