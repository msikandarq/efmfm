//(function(){
//   var alertsCtrl = function($scope, $location, $anchorScroll, $http, $timeout, $modal, $q){
//        $('.alertsMenu').addClass('active');
//        $scope.alertData;
//        $scope.isInvalid = false;
//        $scope.setMinDate = new Date();
//        $scope.toDate;
//        $scope.postDetail = {};
//        $scope.$on('$viewContentLoaded', function() { 
//        $scope.getAlertsList(convertDateUTC(new Date()), convertDateUTC(new Date()));		   
//		});
//        $scope.selectCustomers = [{'value':'employee', 'text':'Employee'},
//	    	 				      {'value':'driver', 'text':'Driver'}];
//        $scope.selectAlerts = [];
//        $scope.selectZones = [];
//        $scope.searchFromDate = new Date();
//        $scope.searchtoDate = new Date();       
//       
//       //Convert the dates in DD-MM-YYYY format
//       var convertDateUTC = function(date){
//          var convert_date = new Date(date);
//          var currentDay = convert_date.getDate();
//              if (currentDay < 10) { 
//                  currentDay = '0' + currentDay; 
//                  console.log(currentDay);
//              }
//          var currentMonth = convert_date.getMonth()+1;
//              if (currentMonth < 10) { 
//                  currentMonth = '0' + currentMonth; 
//                  console.log(currentMonth);
//              }
//          console.log(currentDay+'-'+currentMonth+'-'+convert_date.getFullYear());
//          return currentDay+'-'+currentMonth+'-'+convert_date.getFullYear();   
//        };
//       
//       //Get the Alert to Populate the Drop Downs and the Bottom Table
//       $scope.getAlertsList = function(from, to){
////           alert(new Date(convertDateUTC(new Date()))*1000);
////           console.log("in get alert function:: " + from + " : " + to);
//           var data = {
//        		efmFmAlertTypeMaster:{eFmFmClientBranchPO:{branchId:branchId}},       		
//     			fromDate:from,					   
//      			toDate:to,
//      		};	
//      		$http.post('services/alert/postedalerts/',data).
//      			success(function(data, status, headers, config) {
//                    $scope.alertData = data;
//                    angular.forEach($scope.alertData.postalerts, function(item){
//                        item.isEdit = false;
//                    });
//                    angular.forEach($scope.alertData.alerts, function(item){
//                        $scope.selectAlerts.push({'value':item.alertId, 'text': item.alertTitle});
//                    });
//                    angular.forEach($scope.alertData.zones, function(item){
//                        $scope.selectZones.push({'value':item.routeId, 'text': item.routeName});
//                    });
//                }).
//      		    error(function(data, status, headers, config) {
//      				// log error
//      			});
//       };
//       
//       $scope.openFromDateCal = function($event){
//            $event.preventDefault();
//            $event.stopPropagation();
//            $timeout( function(){
//               $scope.datePicker = {'fromDate' : true};  
//             }, 50);
//        };
//        
//        $scope.openToDateCal = function($event){              
//           $event.preventDefault();
//           $event.stopPropagation();
//            $timeout( function(){
//               $scope.datePicker = {'toDate' : true};  
//             }, 50);            
//        };
//       
//       $scope.openSearchFromDateCal = function($event){
//            $event.preventDefault();
//            $event.stopPropagation();
//            $timeout( function(){
//               $scope.datePicker = {'searchFromDate' : true};  
//             }, 50);
//        };
//        
//        $scope.openSearchToDateCal = function($event){              
//           $event.preventDefault();
//           $event.stopPropagation();
//            $timeout( function(){
//               $scope.datePicker = {'searchToDate' : true};  
//             }, 50);            
//        };
//       
//       //ng-change action
//       $scope.setCustType = function(cust){
//           if($scope.newalert.custType){
//           console.log("inside" +cust.value);
//           
//           $scope.postDetail.custType = cust.value;}
//       };
//       //ng-change action
//       $scope.setAlertType = function(alert){
//           if($scope.newalert.alertType){
//           console.log("inside" +alert.value);
//           $scope.postDetail.alertType = alert.value;}
//       };
//       //ng-change action
//       $scope.setZone = function(zone){
//           if($scope.newalert.selectZone){
//           console.log("inside" +zone.value);
//           $scope.postDetail.selectZone = zone.value;   }        
//       };
//       
//       //Button Action for the 'Create New Alert'
//       $scope.createNewAlerts = function(){
//           var modalInstance = $modal.open({
//           templateUrl: 'partials/modals/createNewPost.jsp',
//           controller: 'createNewPostCtrl',
//           resolve: {}
//           }); 
//		   
//           //Here the VendorId will be given to the Vendor from backend
//		   modalInstance.result.then(function(result){
//         }); 
//       };
//       
//       //Post Alert from the Alert Page
//       $scope.postAlerts = function(newalert){  
// //   	   alert("ho");
// //          alert($scope.newalert.description);
//           /*var data = {efmFmUserMaster:{userId:profileId},
//      				   efmFmAlertTypeMaster:{alertId:$scope.postDetail.alertType},
//      				   eFmFmRouteMaster:{routeId: $scope.postDetail.selectZone},				
//      				   description:$scope.newalert.description,
//      				   tittle:$scope.newalert.tittle,
//      				   fromDate: convertDateUTC($scope.newalert.fromDate),					   
//      				   toDate:convertDateUTC($scope.newalert.toDate),
//      				   userType:$scope.postDetail.custType};	             
//   */   		   $http.post('services/alert/postalert/',data).
//      				    success(function(data, status, headers, config) {
//     				    	if(data.status=="success"){
////                                alert("Comaprng : Todays Date from Cal" + new Date(convertDateUTC($scope.newalert.fromDate))*1000+ " : " + new Date(convertDateUTC(new Date()))*1000);   
//                                if(new Date(convertDateUTC($scope.newalert.fromDate))*1000 == new Date(convertDateUTC(new Date()))*1000){
//                                    $scope.alertData.postalerts.push({'startDate':convertDateUTC($scope.newalert.fromDate), 
//                                                                      'endDate':convertDateUTC($scope.newalert.toDate), 
//                                                                      'tittle':$scope.newalert.alertType.text,
//                                                                      'description':$scope.newalert.description,
//                                                                      'zoneName': $scope.newalert.selectZone.text,
//                                                                      'alertsType':$scope.newalert.custType.text});  
//                                    
//                                }  
//                                $scope.newalert.custType = "";
//                                $scope.newalert.alertType = "";
//                                $scope.newalert.selectZone = "";
//                                $scope.newalert.fromDate = "";
//                                $scope.newalert.toDate = "";
//                                $scope.newalert.description = "";
//                                $scope.alertPostForm.$setPristine();
//                                
//                            }
//                          }).
//      				    error(function(data, status, headers, config) {
//      				      // log error
//      				    });
//       };   
//       
//       $scope.searchPost = function(searchFromDate,searchtoDate){
//           console.log('From: ' + searchFromDate + " : To " + searchtoDate);
//           var from = new Date(searchFromDate)*1000;
//           var to = new Date(searchtoDate)*1000;
////           if(!$scope.searchFromDate){
////               searchFromDate = searchtoDate;
////           }
////           if(!$scope.searchtoDate){
////               searchtoDate = searchFromDate
////           }
////           if(!$scope.searchFromDate && !$scope.searchtoDate){
////               searchFromDate = new Date();
////               searchtoDate = new Date();
////           }
//           
////        
////           console.log('From: ' + searchFromDate + " : To " + searchtoDate);
//           
////           console.log("searchFromDate:: "+fromDate+" searchtoDate:: " +toDate);
////            console.log("in the ng-change function");
//           
//           if((!isNaN(from))&&(!isNaN(to))&&(from!=0) && (from!=0)){
//               if(from>to){
//                    $('.inValidMessageDiv').fadeIn('slow', function(){
//                                       $timeout(function() {$('.inValidMessageDiv').fadeOut('slow') }, 3000);
//                                                   });
//               }
//               else{
//                   console.log(convertDateUTC(searchFromDate));
//                   var data = {
//                        efmFmAlertTypeMaster:{efmFmClientMaster:{clientId:clientId}},
//                        fromDate:convertDateUTC(searchFromDate),					   
//                        toDate:convertDateUTC(searchtoDate),
//                    };	
//                    $http.post('services/alert/postedalerts/',data).
//                        success(function(data, status, headers, config) {
//                            $scope.alertData = data;
//                        }).
//                        error(function(data, status, headers, config) {
//                            // log error
//                      });
//               }
//           }
//       }
//       
//       $scope.editAlert = function(editAlert, index){           
//           if(editAlert.isEdit){
//               editAlert.isEdit = false;
//           }
//           else editAlert.isEdit = true;
//       };
//       
//       $scope.saveAlert = function(savealert, index){
////           alert(savealert.postAlertId + " :: " + savealert.description);
//           var data = {
//                   id:savealert.postAlertId,
//                   description:savealert.description
//    		};	
//    	 $http.post('services/trip/editalert/',data).
//      		    success(function(data, status, headers, config) {
//      		    	$scope.showalertMessage("Updated successfully", "");
//                    $scope.alertData.postalerts[index].description=savealert.description;
//                    savealert.isEdit = false;
//      		    }).
//      		    error(function(data, status, headers, config) {
//      		      // log error
//      		    });
//       };
//       
//       $scope.cancelAlert = function(deleteAlert){
////           alert(deleteAlert.postAlertId);
//           var data = {
//                   id:deleteAlert.postAlertId
//    		};	
//    	 $http.post('services/trip/cancelalert/',data).
//      		    success(function(data, status, headers, config) {
//      		    	$scope.showalertMessage("Canceled successfully", "");             
//                    $('.alertNum'+deleteAlert.postAlertId).hide();
//      		    }).
//      		    error(function(data, status, headers, config) {
//      		      // log error
//      		    });
//       }
//       
//       //Validate the TO Date : Check if the TO Date is more than FROM Date 
//       $scope.checkValidityPost = function(from, to){           
//           console.log("from :: " + from+ " To :: " + to);
//           var fromDate = new Date(from)*1000;
//           var toDate = new Date(to)*1000;
//           console.log("from :: " + fromDate+ " To :: " + toDate);
//           if((!isNaN(fromDate))&&(!isNaN(toDate))&&(fromDate!=0) && (toDate!=0)){
//               if(fromDate>toDate){
//                   $scope.alertPostForm.endDate.$setValidity('invalidRange', true);
//                   $('input.endDate').addClass('error');
//                    $('.inValidMessagePostDiv').fadeIn('slow', function(){
//                         $timeout(function() {$('.inValidMessagePostDiv').fadeOut('slow') }, 3000);
//                    });
//                }
//           }
//       }
//       
//    }; 
//        
//    //*******************Create New Post Controller***************************//
//    var createNewPostCtrl = function($scope, $modalInstance, $state, $http){   	
//            $scope.alertTypes = [{'value':'sos', 'text':'SOS Alert'},
//	    	 				    {'value':'employee', 'text':'Employee Alert'},
//                                {'value':'Driver', 'text':'Driver Alert'}];
//            var alertType;
//            
//            $scope.setAlertType = function(alert){
//                alertType = alert.value;
//            }
//            
//            //Submit New Post on the Server
//        	$scope.create = function(result){   
//                console.log("HELLOO :: " +result.title);
//        		var data = {
//             		   eFmFmClientBranchPO:{branchId:branchId},
//     				efmFmUserMaster:{userId:profileId},
//     				alertDescription:result.description,
//     				alertType:alertType,					   
//     				alertTitle:result.title,
//     			};	
//     		   $http.post('services/alert/create/',data).
//     				 success(function(data, status, headers, config) {
//    				   if(data.status=="success"){
//    				    $modalInstance.dismiss('cancel');
//                        $scope.showalertMessage("Alert created successfully", "");
//    				   	}
//                     }).
//     				 error(function(data, status, headers, config) {
//     				      // log error
//     				 });       		
//        	};    	
//            
//            //CLOSE BUTTON FUNCTION
//            $scope.cancel = function () {
//                $modalInstance.dismiss('cancel');
//            };
//        }; //END of Controller            
//    
//    angular.module('efmfmApp').controller('alertsCtrl', alertsCtrl);
//    angular.module('efmfmApp').controller('createNewPostCtrl', createNewPostCtrl);
//}());