(function(){
   var sosAlertPopupCtrl = function($scope, $state, $http, $location, $interval, $timeout){
	   $(".alert_SOSPopup").hide();    
       $scope.sosAlertData = [];
       $scope.alertTitle;
       $scope.alertDescription;	   
       $scope.getSosAlerts = function(){          
           var data = {
                 branchId:branchId,
                 userId:profileId,
                 combinedFacility:combinedFacility 
           };
           $http.post('services/alert/unreadAlerts/',data).
                    success(function(data, status, headers, config) {
                       if (data.status != "invalidRequest") {
                       $scope.sosAlertData = data;  
                       if($scope.sosAlertData.length>0 && $scope.adminRole){                                                 
                           $scope.alertTitle = $scope.sosAlertData[0].alertTitle;
                           $scope.alertDescription = "Alert sent by vehicle number - " + $scope.sosAlertData[0].vehicleNumber + ".\n Driver Name -"+$scope.sosAlertData[0].driverName;
                           $(".alert_SOSPopup").show('slow', function(){
                               $timeout(function() {
                                   $(".alert_SOSPopup").hide('slow');
                               }, 5000);
                           });
                           $.playSound("temp/2knocks");
                       }
                     }
                    }).
                    error(function(data, status, headers, config) {});
        };   
       
       var sosAlertInterval = $interval( function(){ 
           $scope.getSosAlerts(); }, 10000); 
         
    };
    
    angular.module('efmfmApp').controller('sosAlertPopupCtrl', sosAlertPopupCtrl);
}());