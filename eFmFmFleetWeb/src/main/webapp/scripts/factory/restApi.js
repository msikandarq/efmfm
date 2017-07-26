angular.module('efmfmApp').factory("commonApiService", ["$rootScope","$http", "$q", function($rootScope, $http, $q){
	var base_url = "services";
    var commonParameters;
	return{
			//POST Method
			post:function(url,user_data){
				//$http.defaults.headers.post['authenticationToken']= $rootScope.authenticationToken;
				  //commonParameters = {branchId:branchId,userId:profileId};
				  //user_data = Object.assign(commonParameters, user_data);
				var deffered = $q.defer();
				$http({
					/*headers: {
			            'authenticationToken': $rootScope.authenticationToken,
			        },*/
					method: 'POST',
					url : base_url + url,
					data : user_data
				}).success(function(data, status, headers, config){
					deffered.resolve(data);
				}).error(function(data, status, headers, config){
					deffered.reject(data);
				});
				return deffered.promise;		
			},
	}
}
]);
