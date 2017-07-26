(function(){
   var alertSearch = function($http){
        return function(input, from, to) {
//        var alertSearchResult = [];
        var fromDate = new Date(from)*1000;
        var toDate = new Date(to)*1000;
            if(((isNaN(fromDate))&&(isNaN(toDate))) || ((fromDate==0) && (toDate==0))){
                return input;
            }
            else{
                var data = {
        		efmFmAlertTypeMaster:{efmFmClientMaster:{clientId:clientId}},
            userId:profileId,
     			fromDate:from,					   
      			toDate:to,
            combinedFacility:combinedFacility
      		};	
      		$http.post('services/alert/postedalerts/',data).
      			success(function(data, status, headers, config) {
                    var alertSearchResult = data;
                    return alertSearchResult

                }).
      		    error(function(data, status, headers, config) {
      				// log error
      			});
            }
        }
     };    
    
    angular.module('efmfmApp').filter('alertSearch', alertSearch);
}());