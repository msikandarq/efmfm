(function(){
    
    var INVOICE = function($http){
        var factory = {};
        factory.test = "WORKING!!..";
        
        factory.getInvoiceByMonth = function(){
            var dataObj = {
					   efmFmVendorMaster:{eFmFmClientBranchPO:{branchId:branchId}},
					   driverId:1					   
				};
            return $http.post('services/approval/driverDetail/',dataObj);        
    };
        
       factory.getSomeData = function(){
           var dataObj = {
					   efmFmVendorMaster:{eFmFmClientBranchPO:{branchId:branchId}},
					   driverId:1					   
				};
           
           var promise = $http.post('services/approval/driverDetail/',dataObj);  
           promise.then(function(response){
               this.someData = response.data;  
               return  this.someData;
           });
           return promise;
       }; 
        return factory;
  };
    
 angular.module('efmfmApp').factory('INVOICE', INVOICE);
}());

//(function(){
//    
//    var INVOICE = function($http){
//        var factory = {};
//        factory.test = "WORKING!!..";
//        
//        factory.getInvoiceByMonth = function(){
//            var dataObj = {
//					   efmFmVendorMaster:{efmFmClientMaster:{clientId:clientId}},
//					   driverId:1					   
//				};
//            return $http.post('services/approval/driverDetail/',dataObj);
//            
////            promise.then(
////                function(response){
////                   return response.data;
////                });
////            return promise;
////			return $http.post('services/approval/driverDetail/',dataObj).then(function(response){   //returns a call back
////                                                            this.driverDetail = response.data; 
////                console.log(response.data);
////                                                            return this.driverDetail;
////            });
//        
//    };
//        return factory;
//  };
//    
// angular.module('efmfmApp').factory('INVOICE', INVOICE);
//}());