(function() {

    var versionManegementCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog) {

        var data = {
            branchId: branchId,
            combinedFacility: combinedFacility
        }
        $http.post('scripts/controllers/aboutVersion.json', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                $scope.versionInfo = data;
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });


        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    angular.module('efmfmApp').controller('versionManegementCtrl', versionManegementCtrl);

}());