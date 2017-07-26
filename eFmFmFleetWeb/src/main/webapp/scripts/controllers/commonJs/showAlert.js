(function() {

    var showAlertCtrl = function($scope, $modalInstance, message, hint) {
        $scope.alertMessage = message;
        $scope.alertHint = hint;


        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };
    angular.module("myeFmFmMain").controller('showAlertCtrl', showAlertCtrl);
}());