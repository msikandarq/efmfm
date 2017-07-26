/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       gvModalCtrl
@Modal Controllers      gvModalInstanceCtrl
@template               partials/home.dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/

(function(){
    var gvModalCtrl = function($scope, $modal, $log){
       $scope.openGVModel = function(index, size){
       switch(index){
         case 0:
            var modalInstance = $modal.open({
            templateUrl: 'partials/modals/dashboardModal/gvPollutionDue.jsp',
            controller: 'gvModalInstanceCtrl',
            size: size,
            resolve: {
                index : function(){
                    return index;
                }}
            });

            modalInstance.result.then(function (){}, function(){
                  $log.info('Modal dismissed at: ' + new Date());
            });
            break;
       
         case 1:  
            var modalInstance = $modal.open({
            templateUrl: 'partials/modals/dashboardModal/gvInsuranceDue.jsp',
            controller: 'gvModalInstanceCtrl',
            size: size,
            resolve: {
                index : function(){
                    return index;
                }}
            });

            modalInstance.result.then(function (){}, function(){
                  $log.info('Modal dismissed at: ' + new Date());
            });
            break;   
       
         case 2:  
            var modalInstance = $modal.open({
            templateUrl: 'partials/modals/dashboardModal/gvTaxCert.jsp',
            controller: 'gvModalInstanceCtrl',
            size: size,
            resolve: {
                index : function(){
                    return index;
                }}
            });

            modalInstance.result.then(function (){}, function(){
                  $log.info('Modal dismissed at: ' + new Date());
            });
            break;
       
         case 3:  
            var modalInstance = $modal.open({
            templateUrl: 'partials/modals/dashboardModal/gvPermitRenewal.jsp',
            controller: 'gvModalInstanceCtrl',
            size: size,
            resolve: {
                index : function(){
                    return index;
                }}
            });

            modalInstance.result.then(function (){}, function(){
                  $log.info('Modal dismissed at: ' + new Date());
            });
            break;
       
         case 4:  
            var modalInstance = $modal.open({
            templateUrl: 'partials/modals/dashboardModal/gvVehicleMaintenance.jsp',
            controller: 'gvModalInstanceCtrl',
            size: size,
            resolve: {
                index : function(){
                    return index;
                }}
            });

            modalInstance.result.then(function (){}, function(){
                  $log.info('Modal dismissed at: ' + new Date());
            });
            break;  
          }   //switch ends
   }; 
 };
    angular.module('efmfmApp').controller('gvModalCtrl', gvModalCtrl);
    
     var gvModalInstanceCtrl = function($scope, $modalInstance, $state, index, $http){ 
        var combinedFacilityId = localStorage.getItem("combinedFacilityIdDashboard");
        $scope.vehicleGovernanceNotification=false;
        $scope.colorFlgData = [
                {
                    flagId: 'R',
                    flagName: 'Red - 10 Days'
                },
                {
                    flagId: 'Y',
                    flagName: 'Yellow - 20 Days'
                }, 
                {
                    flagId: 'G',
                    flagName: 'Green - 30 Days'
                }
        ];

         $scope.getPollutionDueData = function(){
            $scope.vehicleGovernanceNotification=false;
             var data = {
                     eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                       tripType:"GOVERNCEVEHICLES",
                           requestStatus:"polutionexpire",
                           userId:profileId,
                           combinedFacility:combinedFacilityId    
                };
               $http.post('services/dashboard/getDashBoardDetailList/',data).
                     success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vehicleGovernanceNotification=true;
                        $scope.sosRoadAlertsData = data;
                        }
                     }).
                     error(function(data, status, headers, config) {
                          // log error
                     });             
         };
         
         $scope.getInssuranceDueData = function(){
              $scope.vehicleGovernanceNotification=false;
             var data = {
                     eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                     tripType:"GOVERNCEVEHICLES",
                         requestStatus:"insurancevalid",
                         userId:profileId,
                         combinedFacility:combinedFacilityId    
                };
               $http.post('services/dashboard/getDashBoardDetailList/',data).
                     success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vehicleGovernanceNotification=true;
                        $scope.sosRoadAlertsData = data;
                        }
                     }).
                     error(function(data, status, headers, config) {
                          // log error
                     });    
         };
         
         $scope.getBreakdownsData = function(){
             $scope.vehicleGovernanceNotification=false;
             var data = {
                     eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                     tripType:"GOVERNCEVEHICLES",
                         requestStatus:"breakdownsalerts",
                         userId:profileId,
                         combinedFacility:combinedFacilityId    
                };
               $http.post('services/dashboard/getDashBoardDetailList/',data).
                     success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vehicleGovernanceNotification=true;
                        $scope.sosRoadAlertsData = data;
                        }
                     }).
                     error(function(data, status, headers, config) {
                          // log error
                     });    
         };
         
         $scope.getContractData = function(){
             $scope.vehicleGovernanceNotification=false;
             var data = {
                     eFmFmClientBranchPO:{branchId:branchId},
                       tripType:"ALERTS",
                           requestStatus:"roadalerts",
                           userId:profileId, 
                           combinedFacility:combinedFacilityId   
                };
               $http.post('services/dashboard/getDashBoardDetailList/',data).
                     success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vehicleGovernanceNotification=true;
                        $scope.sosRoadAlertsData = data;
                        }
                     }).
                     error(function(data, status, headers, config) {
                          // log error
                     });    
         };
         
         $scope.getTaxCertData = function(){  
             $scope.vehicleGovernanceNotification=false;
             var data = {
                eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                tripType:"GOVERNCEVEHICLES",
                requestStatus:"taxvalid",
                userId:profileId,
                combinedFacility:combinedFacilityId    
                };
             $http.post('services/dashboard/getDashBoardDetailList/',data).
                 success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                     $scope.vehicleGovernanceNotification=true;
                        $scope.taxCertData = data;
                    }
                     }).
                 error(function(data, status, headers, config) {
                          // log error
                     });
         };
         
         $scope.getPermitDueData = function(){
             $scope.vehicleGovernanceNotification=false;
             var data = {
                eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                tripType:"GOVERNCEVEHICLES",
                requestStatus:"permitvalid",
                userId:profileId,
                combinedFacility:combinedFacilityId    
                };
             $http.post('services/dashboard/getDashBoardDetailList/',data).
                 success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                     $scope.vehicleGovernanceNotification=true;
                        $scope.permitDueData = data;
                    }
                     }).
                 error(function(data, status, headers, config) {
                          // log error
                     });
         };
         
         $scope.getVehicleMaintenanceData = function(){
             $scope.vehicleGovernanceNotification=false;
             var data = {
                eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                tripType:"GOVERNCEVEHICLES",
                requestStatus:"vehiclemaintenancevalid",
                userId:profileId,
                combinedFacility:combinedFacilityId    
                };
             $http.post('services/dashboard/getDashBoardDetailList/',data).
                 success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                     $scope.vehicleGovernanceNotification=true;
                        $scope.vehicleMaintenanceData = data;
                    }
                     }).
                 error(function(data, status, headers, config) {
                          // log error
                     });
         };
         $scope.setSearchFlag = function(search){
            if (search) {
                    $scope.filterSearch =    {'colorFlg': search}
                    $scope.filterSearchFlag =    true;
            } else {
                    $scope.filterSearch = search;
                    $scope.filterSearchFlag =    false;
            }

        };
          switch(index) {
            case 0:
                $scope.getPollutionDueData();
                break;
            case 1:
                $scope.getInssuranceDueData();
                break;
            case 2:
                $scope.getTaxCertData();
                break;
            case 3:
                $scope.getPermitDueData();
                break;
            case 4:
                $scope.getVehicleMaintenanceData();
                break;
        }        
         
        $scope.saveInExcel = function(modal){
            $scope.currentModalOpen = modal;
            
            if($scope.currentModalOpen == 'gvPollution'){
                
                 var dataObj = {
                            eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                            tripType:"GOVERNCEVEHICLES",
                            requestStatus:"polutionexpire",
                            userId:profileId,
                            combinedFacility:combinedFacilityId   
                        };

                       $http({
                             url : 'services/dashboard/getDashBoardDetailListDwn/',          
                             method: "POST",
                             data: dataObj, 
                             headers: {
                                'Content-type': 'application/json'
                             },
                             responseType: 'arraybuffer'
                         }).success(function (data, status, headers, config) {
                             var blob = new Blob([data], {
                             });
                            
                             saveAs(blob, 'Pollution Due For Renewal' + '.xlsx');
                         }).error(function (data, status, headers, config) {
                             alert("Upload Failed")
                         });
               
            }
            if($scope.currentModalOpen == 'gvInsurance'){
                
                   var dataObj = {
                            eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                            tripType:"GOVERNCEVEHICLES",
                            requestStatus:"insurancevalid",
                            userId:profileId,
                            combinedFacility:combinedFacilityId   
                        };

                       $http({
                             url : 'services/dashboard/getDashBoardDetailListDwn/',          
                             method: "POST",
                             data: dataObj, 
                             headers: {
                                'Content-type': 'application/json'
                             },
                             responseType: 'arraybuffer'
                         }).success(function (data, status, headers, config) {
                             var blob = new Blob([data], {
                             });
                   
                             saveAs(blob, 'Insurance Due For Renewal' + '.xlsx');
                         }).error(function (data, status, headers, config) {
                             alert("Upload Failed")
                         });
               
            }
            if($scope.currentModalOpen == 'gvTaxCert'){

                 var dataObj = {
                            eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                            tripType:"GOVERNCEVEHICLES",
                            requestStatus:"taxvalid",
                            userId:profileId,
                            combinedFacility:combinedFacilityId   
                        };

                       $http({
                             url : 'services/dashboard/getDashBoardDetailListDwn/',          
                             method: "POST",
                             data: dataObj, 
                             headers: {
                                'Content-type': 'application/json'
                             },
                             responseType: 'arraybuffer'
                         }).success(function (data, status, headers, config) {
                             var blob = new Blob([data], {
                             });
                       
                             saveAs(blob, 'Tax Certificate Due For Renewal' + '.xlsx');
                         }).error(function (data, status, headers, config) {
                             alert("Upload Failed")
                         });
                
            }
            if($scope.currentModalOpen == 'gvPermit'){

                var dataObj = {
                            eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                            tripType:"GOVERNCEVEHICLES",
                            requestStatus:"permitvalid",
                            userId:profileId,
                            combinedFacility:combinedFacilityId   
                        };

                       $http({
                             url : 'services/dashboard/getDashBoardDetailListDwn/',          
                             method: "POST",
                             data: dataObj, 
                             headers: {
                                'Content-type': 'application/json'
                             },
                             responseType: 'arraybuffer'
                         }).success(function (data, status, headers, config) {
                             var blob = new Blob([data], {
                             });
                     
                             saveAs(blob, 'Permit Due For Renewal' + '.xlsx');
                         }).error(function (data, status, headers, config) {
                             alert("Upload Failed")
                         });
                
            }
            if($scope.currentModalOpen == 'gvVehicleMaint'){
                
                var dataObj = {
                            eFmFmEmployeeRequestMaster:{efmFmUserMaster:{eFmFmClientBranchPO:{branchId:branchId}}},
                            tripType:"GOVERNCEVEHICLES",
                            requestStatus:"vehiclemaintenancevalid",
                            userId:profileId,
                            combinedFacility:combinedFacilityId   
                        };

                       $http({
                             url : 'services/dashboard/getDashBoardDetailListDwn/',          
                             method: "POST",
                             data: dataObj, 
                             headers: {
                                'Content-type': 'application/json'
                             },
                             responseType: 'arraybuffer'
                         }).success(function (data, status, headers, config) {
                             var blob = new Blob([data], {
                             });
                  
                             saveAs(blob, 'Vehicles Needed Maintenance' + '.xlsx');
                         }).error(function (data, status, headers, config) {
                             alert("Upload Failed")
                         });
              
            }
        };
         
        $scope.ok = function () {  
            $modalInstance.close();
            $state.go('home.dashboard');
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }; 
    angular.module('efmfmApp').controller('gvModalInstanceCtrl', gvModalInstanceCtrl);

}());