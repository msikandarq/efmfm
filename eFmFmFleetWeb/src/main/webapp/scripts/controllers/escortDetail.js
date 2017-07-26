/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       escortDetailCtrl
@Modal Controllers      
@template               

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var escortDetailCtrl = function($scope, $stateParams) {
        $scope.escortID = $stateParams.escortId;
    };

    angular.module('efmfmApp').controller('escortDetailCtrl', escortDetailCtrl);
}());