(function(){
    
	var eFmFm = angular.module('efmfmApp', ['ui.router','tagged.directives.infiniteScroll', 'checklist-model', 'multi-select-dropdown-span-three-facility', 'multi-select-dropdown-without-selectall', 'multi-select-dropdown-span-three', 'ngRateIt', 'multi-select-dropdown-span-five', 'multi-select-dropdown-span-six', 'ngIdle' ,'ngFloatingLabels', 'oc.lazyLoad' ,'am.multiselect' , 'isteven-multi-select','isteven-multi-show' ,'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ngProgress', 'ngAnimate','cgBusy', 'angularUtils.directives.dirPagination', 'angular-confirm', 'infinite-scroll', 'ng.deviceDetector', 'anguFixedHeaderTable', 'ngDialog', 'UserValidation','xeditable', 'ngMap']);    
       eFmFm.run(['Idle', function(Idle) {
          Idle.watch();
        }]);

    eFmFm.config(function($stateProvider, $urlRouterProvider, $locationProvider, IdleProvider, KeepaliveProvider) {
            
            var sessionTimeOutInMins = localStorage.getItem("sessionTimeOutInMin") * 60;
            var sessionTimeOutNotificationInSecs = localStorage.getItem("sessionTimeOutNotificationInSec") * 1;
            IdleProvider.idle(sessionTimeOutInMins);
            IdleProvider.timeout(sessionTimeOutNotificationInSecs);
            
            $urlRouterProvider.otherwise('/dashboard');
            //now setup the states
            $stateProvider           
            .state('home', {
                abstract: true,
                url: '',
                templateUrl: 'partials/main.jsp',
                controller: 'homeCtrl'})
            .state('home.dashboard', {
                url: '/dashboard',
                templateUrl: 'partials/home.dashboard.jsp',
                controller: 'dashCtrl',
                onEnter: function(){
                        $('.dashboardMenu').addClass('active');},  
                onExit: function() {
                        $('.dashboardMenu').removeClass('active');
                    }
            })
            .state('home.viewmap', {
                url: '/viewmap',
                templateUrl: 'partials/home.viewmap.jsp',
                controller: 'viewMapCtrl',
                onEnter: function(){
                        $('.veiwMapMenu').addClass('active');},  
                onExit: function() {
                        $('.veiwMapMenu').removeClass('active');
                    }
            })
            .state('home.viewmap.showRoutes', {
                url: '/showRoutes',
                templateUrl: 'partials/home.viewmap.showAll.jsp',
                controller: 'allRoutesMapCtrl',
                onEnter: function(){
                        $('.veiwMapMenu').addClass('active');},  
                onExit: function() {
                        $('.veiwMapMenu').removeClass('active');
                    }
            })
            .state('home.excelUpload', {
                url: '/excelUpload',
                templateUrl: 'partials/home.excelUpload.jsp',
                controller: 'excelUploadCtrl',
               
            })
            .state('home.approval', {
                url: '/approval',
                templateUrl: 'partials/home.approval.jsp',
                controller: 'approvalCtrl',
                onEnter: function(){
                        $('.approvalMenu').addClass('active');},  
                onExit: function() {
                        $('.approvalMenu').removeClass('active');
                    }
            })
            .state('home.employeeTravelDesk', {
                url: '/employeeTravelDesk',
                templateUrl: 'partials/home.employeeTravelDesk.jsp',
                controller: 'empTravelDeskCtrl',
                onExit: function() {
                    $('.empTravelDeskMenu').removeClass('active');
                }})
            .state('home.servicemapping', {
                url: '/servicemapping',
                templateUrl: 'partials/home.servicemapping.jsp',
                controller: 'serviceMappingCtrl',
                onExit: function() {
                    $('.serviceMappingMenu').removeClass('active');}
            })
            .state('home.serviceRouteMap', {
                url: '/serviceRouteMap/:routeId/:waypoints/:baseLatLong',
                templateUrl: 'partials/home.serviceSingleRouteMap.jsp',
                controller: 'serviceRouteMapCtrl',
                onExit: function() {
                    $('.serviceMappingMenu').removeClass('active');}
            })
            .state('home.reports', {
                url: '/reports',
                templateUrl: 'partials/home.reports.jsp',
                controller: 'reportsCtrl',
                onExit: function() {
                    $('.reportMenu').removeClass('active');}})
            .state('home.alerts', {
                url: '/alerts',
                templateUrl: 'partials/home.alerts.jsp',
                controller: 'alertsCtrl',
                onExit: function() {
                    $('.alertsMenu').removeClass('active');}
            })
            .state('home.invoice', {
                url: '/invoice',
                templateUrl: 'partials/home.invoice.jsp',
                controller: 'invoiceCtrl',
                onExit: function() {
                    $('.invoiceMenu').removeClass('active');}
            })
            .state('home.requestDetails', {
                url: '/requestDetails',
                templateUrl: 'partials/home.employeeRequestDetails.jsp',
                controller: 'cabRequestCtrl',
                onExit: function() {
                    $('.employeeRequest').removeClass('active');
                }})            
            .state('home.adhocRequests', {
                url: '/adhocRequests',
                templateUrl: 'partials/home.adhocRequests.jsp',
                controller: 'adhocRequestCtrl',
                onExit: function() {
                    $('.employeeRequestAdhoc').removeClass('active');
                }})
             .state('home.vendorDashboard', {
                url: '/vendorDashboard',
                templateUrl: 'partials/home.vendorDashboard.jsp',
                controller: 'vendorDashboardCtrl',
                onExit: function() {
                    $('.vendorDashboard_admin').removeClass('active');
                }}) 
            .state('home.activeRouteMap', {
                url: '/activeRoute/:routeId',
                templateUrl: 'partials/home.routeMap.jsp',
                controller: 'routeCtrl',
                onExit: function() {
                    $('.veiwMapMenu').removeClass('active');
                }}) 
            .state('home.rescheduleRequest', {
                url: '/rescheduleRequest',
                templateUrl: 'partials/home.rescheduleRequest.jsp',
                controller: 'rescheduleRequestCtrl',
                onExit: function() {
                    $('.rescheduleRequest_admin').removeClass('active');
                    $('.admin_home').removeClass('active');
                }})
             .state('home.empDetails', {
                url: '/empDetails',
                templateUrl: 'partials/home.empDetails.jsp',
                controller: 'empDetailCtrl',
                onExit: function() {
                    $('.empDetail_admin').removeClass('active');
                    $('.admin_home').removeClass('active');
                }})            
                .state('home.myProfile', {
                url: '/myProfile',
                templateUrl: 'partials/home.myProfile.jsp',
                controller: 'myProfileCtrl',
                onExit: function() {
                    $('.myProfile_admin').removeClass('active');
                    $('.admin_home').removeClass('active');
                }})             
                .state('home.checkInVehicalLocSpeed', {
                url: '/checkInVehicalLocSpeed',
                templateUrl: 'partials/home.checkInVehicalLocSpeed.jsp',
                controller: 'checkInVehicalLocSpeedCtrl',
                onExit: function() {
                    $('.vehicleLoc_admin').removeClass('active');
                    $('.admin_home').removeClass('active');
                }})                            
                .state('releasedHistory', {
                url: '/releasedHistory',
                templateUrl: 'partials/releasedHistory.jsp',                
                controller: 'releasedHistoryCtrl',
                onExit: function() {
                }})                                       
                .state('home.clientSettings', {
                url: '/clientSettings',
                templateUrl: 'partials/home.clientSettings.jsp',  
                controller: 'clientSettingsCtrl',
                onExit: function() {                    
                    $('.clientSetting_admin').removeClass('active');
                    $('.clientSetting_admin').removeClass('active');
                    
                }})                                       
                .state('home.accessDenied', {
                url: '/accessDenied',
                templateUrl: 'partials/home.accessDenied.jsp',  
                onExit: function() {
                }});
        });    
}());