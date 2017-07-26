/*
*CLIENT : SHELL
*/

/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       homeCtrl
@Modal Controllers      
@template               All JSP Pages

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/

(function(){
    var homeCtrl = function($scope, $filter, $rootScope, commonApiService, $modal, $log, $state, $http, $location, $timeout, ngDialog, ngProgressFactory,Idle, $location, $window){   
    	$http.defaults.headers.post['authenticationToken']= authenticationToken;
     
      /*Session Management*/
      setInterval(function(){ 
          var data ={userAgent: userAgent, 
        		  userIPAddress:userIPAddress,
        		  branchId:branchId,
        		  userId:profileId       		  
          };         
          commonApiService.post('/authorization/sessionValidityCheck',data
                ).then(function(data, status, headers, config){
                    var authenticationToken = data.token;
                    $http.defaults.headers.post['authenticationToken']= data.token;
                    $.ajaxSetup({
                        beforeSend: function (xhr)
                        {
                           xhr.setRequestHeader("authenticationToken",data.token);        
                        }
                    });
                      if(data.data == 'Invalid'){
                          document.getElementById('logOut').click();
                      }
                },function(data, status, headers, config){
                       
          });
       }, 1000);

      
    $scope.started = false;
      function closeModals() {
        if ($scope.warning) {
          $scope.warning.close();
          $scope.warning = null; 
        }

        if ($scope.timedout) {
          $scope.timedout.close();
          $scope.timedout = null;
        }
      }
      $scope.active = true;
      $scope.progress = {value:10, type:"danger"};
      $timeout(function(){$scope.progress.value=100;}, 3000);
      
      
       updateProgressBar(20);
       
       $timeout(function()
       {
         updateProgressBar(100);
         }, 4000);
  
      function updateProgressBar(value){
        $scope.bar_upload_style = {  "width": value+'%'  };
        $scope.bar_upload_text = value + "%";
        
        if(value ==100) $scope.active = false;
      }

      $scope.$on('IdleStart', function() {
        closeModals();

        $scope.warning = $modal.open({
          templateUrl: 'warning-dialog.html',
          windowClass: 'modal-danger'
        });
      });

      $scope.$on('IdleEnd', function() {
        closeModals();
      });

      $scope.$on('IdleTimeout', function() {
        closeModals();
        $scope.timedout = $modal.open({
          templateUrl: 'timedout-dialog.html',
          windowClass: 'modal-danger'
        });
          $timeout(function() {
            document.getElementById('logOut').click();
          }, 1000);
        
      });

        closeModals();
        Idle.watch();
        $scope.started = true;

       $scope.adminRole=false;
       $scope.supervisorRole = false;
       $scope.managerRole = false;
       $scope.webUserRole = false; 
       $scope.superadmin = false;      
       $scope.IntegerNumber = /^\d+$/;
       $scope.regExName = /^[A-Za-z]+$/;
       $scope.NoSpecialCharacters = /^[a-zA-Z0-9]*$/;
       $scope.regexDecimalNumbers = /^[0-9]+(\.[0-9]{2})?$/;
       $scope.regexMin11Numbers = /^\d{11,}$/;
       $scope.regexMin3Numbers = /^\d{3,}$/;
       $scope.regexMin1to3Numbers = /^\d{1,3}$/;
       $scope.regexMin11to15Numbers = /^\d{11,15}$/;
       $scope.alertMessage;
       $scope.alertHint;
       $scope.hstep = 1;
       $scope.mstep = 1;
       $scope.ismeridian = false;
       $scope.branchCode = branchCode;
       $rootScope.multiFacility;
       $scope.userRole = userRole;

       if(multiFacility == "true"){
          $rootScope.multiFacility = 'Y';
       }else{
          $rootScope.multiFacility = 'N';
       }
       //Loading Spinner    
       $scope.$on('LOAD', function(){$scope.isProcessing = true;});
       $scope.$on('UNLOAD', function(){$scope.isProcessing = true;});
       //Progress Bar    
       $scope.progressbar = ngProgressFactory.createInstance();
       $scope.progressbar.setHeight('5px');
       $scope.progressbar.setColor('#ff3300');
        
       //**To Remove Class ACTIVE when another Nav Button is clicked
       $(".nav a").on("click", function(){
           $(".nav").find(".active").removeClass("active");
           $(this).parent().addClass("active");
        });
        
        $scope.getActiveClassStatus = function(viewLocation){
            return viewLocation === $location.path();
        };
        
        $scope.$on('$viewContentLoaded', function() { 
        	
            if(userRole=='admin'){$scope.adminRole=true;}
            if(userRole=='supervisor'){$scope.supervisorRole=true;}
            if(userRole=='manager'){$scope.managerRole = true;}
            if(userRole=='webuser'){$scope.webUserRole = true;}
            if(userRole=='superadmin'){$scope.superadmin = true;}
            $scope.isDashboardActive = isDashboardActive;
            $scope.isEmployeeRosterActive = isEmployeeRosterActive;
            $scope.isCaballocationActive = isCaballocationActive;
            $scope.isLiveTrackingActive = isLiveTrackingActive;
            $scope.isReportsActive = isReportsActive;
            $scope.isInvoiceActive = isInvoiceActive;
            $scope.IsApprovalActive = IsApprovalActive;
            $scope.isAdhocRequestActive = isAdhocRequestActive;
            $scope.isVendorDashboardActive = isVendorDashboardActive;
            $scope.isEmployeeDetailActive = isEmployeeDetailActive;
            $scope.isMyProfileActive = isMyProfileActive;
            $scope.isImportDataActive = isImportDataActive;
            $scope.iSRequestDetails = iSRequestDetails;
            $scope.isadhocTimePickerForEmployee = true; //adhocTimePickerForEmployee;
            $scope.passExpire = passExpire;

            $scope.officeLocation = officeLocation.split(',');
        });
        
       //To make the Nav Bar FIX when User Tries to to Scroll the Page down     
       $(window).bind('scroll', function () {
          if ($(window).scrollTop() > 50) {
              $('.navBar_home').addClass('navbar-fixed-top');
           }
           else{
              $('.navBar_home').removeClass('navbar-fixed-top');
            }
        });       

        if($scope.passExpire === 'Y'){
            ngDialog.open({
                template: 'Your Password is going to expired in few days. Please change you password.',
                plain: true
            });
        }
        
       //Format of the Calenders Used in all the Children Views
       $scope.format = 'dd-MM-yyyy';
       $scope.dateOptions = {formatYear: 'yy',
                             startingDay: 1,
                              showWeeks: false,
                             };

        $scope.isOpen = false;
        $scope.openMenu = function(){
            if(!$scope.isOpen){$scope.isOpen = true;}
            else $scope.isOpen = false;           
        };
                    
       $scope.logout = function(){
       }; //End Of FUNCTION    
        
      //Convert the dates in DD-MM-YYYY format
      $scope.convertDateUTC = function(date){
          var convert_date = new Date(date);
          var currentMonth = date.getMonth()+1;
          var currentDate=date.getDate();
                  if (currentDate < 10) { 
                      currentDate = '0' + currentDate; 
              }
              if (currentMonth < 10) { 
                  currentMonth = '0' + currentMonth; 
              }
          return currentDate+'-'+currentMonth+'-'+convert_date.getFullYear();   
     }; 
       
     $scope.initializeTime = function(time){
            var d = new Date();
            var tempTime = time.split(":");
            d.setHours( tempTime[0] );
            d.setMinutes( tempTime[1] );
            return d;           
     }
     //Convert to hh:mm
     $scope.convertToTime = function(newdate){
           d = new Date(newdate);
           hr = d.getHours();
           min = d.getMinutes();
           if(hr<10){hr = '0'+hr;} 
           if(min<10){min = '0'+min;}    
           return hr+":"+min;
    };

    if(typeof(Storage) !== "undefined") {
          if (localStorage.clickcount) {
              localStorage.clickcount = Number(localStorage.clickcount)+1;
              
          } else {
              localStorage.clickcount = 1;
          }
    } 

    if(localStorage.clickcount != 2){
      $state.go('home.excelUpload'); 
    }else{
      localStorage.removeItem('clickcount');
    }

    $scope.versionControl = function(){
       var modalInstance = $modal.open({
           templateUrl: 'partials/modals/versionManegementModel.jsp',
           controller: 'versionManegementCtrl',
           size:'lg',
           resolve: {
               // versionInfo : $rootScope.versionInfo
           }
       }); 
    }

    $scope.reloadPage = function()
    {
      if(branchCode == 'GNPTJP'){
        window.location.reload();
        $state.go('home.excelUpload');  
      }else{
        $state.go('home.excelUpload'); 
      }
      
    }

        var todayDate = new Date();
        var yearsBack = new Date(todayDate);
        yearsBack.setYear(todayDate.getFullYear()-18);
        $rootScope.maxDateDOB = $filter('date')(yearsBack,'yyyy');


        var todayMinDate = new Date();
        var yearStart = new Date(todayMinDate);
        yearStart.setYear(todayMinDate.getFullYear()-50);

        $rootScope.minDateDOB = $filter('date')(yearStart,'yyyy');

        
};    
    angular.module('efmfmApp').controller('homeCtrl', homeCtrl);
  
}());