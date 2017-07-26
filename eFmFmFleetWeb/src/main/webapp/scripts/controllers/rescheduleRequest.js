/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       rescheduleRequestCtrl
@Modal Controllers      
@template               partials/home.rescheduleRequest.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/

(function(){
	   var rescheduleRequestCtrl = function($scope, $http, $filter, ngDialog){
       
    if(!$scope.iSRequestDetails  || $scope.iSRequestDetails == "false"){
        $state.go('home.accessDenied');  
    } 
       
    else{
		 $('.rescheduleRequest_admin').addClass('active');
		 $('.admin_home').addClass('active');
		 $scope.posts;
		 var data = {
        		 eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
        		 efmFmUserMaster:{userId:profileId},
        		 combinedFacility:combinedFacility
			};
		 $http.post('services/trip/reshedulerequest/',data).
	    		    success(function(data, status, headers, config) {
	    		    if (data.status != "invalidRequest") {
	    		      $scope.posts = data;   
	    		      $scope.countofFilteredRecords = $scope.posts.length;
	    		      $scope.countofFilteredRecords = 0;
	    		      $scope.showSearchResultCount = false;
	    		      angular.forEach($scope.posts, function(item) {
	    		    	  item.checkBoxFlag=false;
	    	         });
	    		  	}
	    		    }).
	    		    error(function(data, status, headers, config) {
	    		      // log error
	    		    });
		 
	     $scope.showSearchResultCount = false;
	     $scope.numOfRows = 0;
	     $scope.numberofRecords = 10;
	     $scope.countofFilteredRecords;
	     $scope.selectAllClicked = false;
	     $scope.deleteAllClicked = false;
	     
	     $scope.paginations = [{'value':1, 'text':'1 record'},
	    	 				   {'value':10, 'text':'10 records'},
	    	 				   {'value':15, 'text':'15 record',},
	    	 				   {'value':20, 'text':'20 records'}];
        
	     $scope.shiftsTime = ['6:00 - 8:00', 
	                         '8:00 - 10:00', 
	                         '10:00 - 12:00', 
	                         '12:00 - 14:00', 
	                         '14:00 - 16:00', 
	                         '16:00 - 18:00', 
	                         '18:00 - 20:00', 
	                         '20:00 - 22:00', 
	                         '22:00 - 24:00'];
	     
	     $scope.checkBoxModel  = {value : false};
	     $scope.approve = function(post){
	    	 var data = {
					 efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}},
					   requestId:post.requestId,
					   userId:profileId,
					   combinedFacility:combinedFacility 
					   };
			 $http.post('services/trip/approvereshedulerequest/',data).
		    		    success(function(data, status, headers, config) {
		    		    	if (data.status != "invalidRequest") {
		    		    	ngDialog.open({
                                template : 'Request approve successfully.',
                                plain : true
                            });
		    		    }
//                            alert("Request approve successfully");
		    		    }).
		    		    error(function(data, status, headers, config) {
		    		      // log error
		    		    });
			 };
         
         $scope.reject = function(post){
        	 var data = {
					 efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}},
					   requestId:post.requestId,
					   userId:profileId,
					   combinedFacility:combinedFacility 
					   };
			 $http.post('services/trip/rejectreshedulerequest/',data).
		    		    success(function(data, status, headers, config) {
		    		    	if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template : 'Request rejected successfully.',
                                plain : true
                            });
                        }
//	                         alert("Request rejected successfully");
		    		    }).
		    		    error(function(data, status, headers, config) {
		    		      // log error
		    		    });
           
            
         };
	     
	     $scope.getNumOfRecordsOnPage = function(numOfRecords){
	    	 
	     };
	     
	     //Set the Limit of ng-repeat in <tr>
	     $scope.setLimit = function(showRecords){
	    	 if(!showRecords){$scope.numberofRecords = $scope.posts.length;}
	    	 else $scope.numberofRecords = showRecords.value;  	 
	    };
	    
	    $scope.$watch("searchEmployeeReported", function(query){
	    	if($scope.searchEmployeeReported == ''){
	    	     $scope.showSearchResultCount = false;
	    	}
	    	else
	    	     $scope.showSearchResultCount = true;
	    	
	        $scope.countofFilteredRecords = $filter("filter")($scope.posts, query).length;
	      });
       }
	           
	  };    
	    
	    angular.module('efmfmApp').controller('rescheduleRequestCtrl', rescheduleRequestCtrl);
	}());