/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       serviceRouteMapCtrl
@Modal Controllers      
@template               partials/home.serviceSingleRouteMap.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/

(function(){
   var serviceRouteMapCtrl = function($scope, $stateParams, $state, $http){
       $('.serviceMappingMenu').addClass('active');
       $scope.isloaded = false;
       $scope.singleServiceData;
       $scope.routeId = $stateParams.routeId; 
       $scope.waypoints = $stateParams.waypoints;
       $scope.baseLatLong = $stateParams.baseLatLong;      
       $scope.singleServiceData = [{'waypoints':$scope.waypoints, 'baseLatLong':$scope.baseLatLong}];
       $scope.isloaded = true;
       
       $scope.getSingleServiceMap = function(){ 
           return $scope.singleServiceData;
       };   
       
       $scope.backToServiceMapping = function(){
           $state.go('home.servicemapping');
       };
       
       
    };    
    
    angular.module('efmfmApp').controller('serviceRouteMapCtrl', serviceRouteMapCtrl);
}());