(function(){
    
    var efmfmSingleLiveTrip = function($interval, $http, $rootScope){
        return{
            restrict: 'E',
            template: '<div></div>',
            replace:true,           
            link:function(scope, element, attrs){
                scope.getMapData2 = function(){  
                   var data = {
                           eFmFmClientBranchPO:{branchId:branchId},
                           assignRouteId:scope.routeId,
                           combinedFacility:combinedFacility        
                   }; 
                   $http.post('services/view/individualtrip/',data).
                         success(function(data, status, headers, config) {
                          if (data.status != "invalidRequest") {
                           scope.singleRouteData = data;
                           scope.mapData = data;
                           scope.cabLocation = scope.singleRouteData[0].currentCabLatiLongi;
                           scope.singleDataLoaded = true;
                         }
                         }).
                         error(function(data, status, headers, config) {
                                  // log error
                         });          
                  };
                
                var singleMapInterval = $interval(scope.getMapData2, 4000);   
                
                var dereg = $rootScope.$on('$locationChangeSuccess', function() {
                    $interval.cancel(singleMapInterval);
                    $interval.cancel(scope.routeMapInterval);
                    dereg();
                });
                
                scope.$watch(
                    function( $scope ) {
                        return $scope.cabLocation;
                    },
                    function( newValue, oldvalue ) {
                        if(oldvalue!==newValue){
                            scope.cabLocation = newValue;                            
                            if(scope.cabLocation == undefined)
                            {
                                return false;
                            }
                            else
                            {
                              cab = scope.cabLocation.split(',');
                              moveCab(cab[0],cab[1]);
                            }
                        }                        
                    }
                );
                
                var moveCab = function(cab1, cab2){
                    marker.setPosition( new google.maps.LatLng( cab1, cab2 ) );
                };
                
                var cab = scope.cabLocation.split(',');
                
                var waypts = [];
			    var image = 'images/cabImage.png';
                var baseLocation = scope.mapData[0].baseLatLong.split(',');
                var originLocation = new google.maps.LatLng(baseLocation[0], baseLocation[1]);
                var destinLocation = new google.maps.LatLng(baseLocation[0], baseLocation[1]);
                
                var cords = scope.mapData[0].waypoints.split('|');
//                alert(cords);

                for(var i = 0; i < cords.length; i++){
                      if(cords[i] == null || cords[i] ==""){
                          cords.splice(i, 1);
                      }
                    else cords[i] = cords[i].replace(/^\s*/, "").replace(/\s*$/, "");
                } 
                var limit = cords.length;

                // Somewhere to store the wayoints                
                for (var x = 0; x < limit; x++) {

                    waypts.push({
                        location: cords[x],
                        stopover: true
                    });            
                }
               var directionsDisplay;
               var directionsService = new google.maps.DirectionsService();
               directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});
                var center = new google.maps.LatLng(baseLocation[0], baseLocation[1]);
                var mapOptions = {
                    zoom:13,
                    center: center
                  };
               var map = new google.maps.Map(document.getElementById(attrs.id), mapOptions);
                directionsDisplay.setMap(map);
//                var trafficLayer = new google.maps.TrafficLayer();
//                trafficLayer.setMap(map);
                
                var request = {
                    origin: originLocation,
                    destination: destinLocation,
                    waypoints: waypts,
                    optimizeWaypoints: false,
                    travelMode: google.maps.TravelMode.DRIVING
                };
                directionsService.route(request, function(response, status) {
                   if (status == google.maps.DirectionsStatus.OK) {
                       directionsDisplay.setDirections(response);
                       //************
                       route = response.routes[0];
                       var x = route.legs.length;
                       for(i=1; i<x; i++){ 
                           var leg = response.routes[ 0 ].legs[ i ];
                           var title = leg.start_address;
                           var empImage = 'images/EmployeeMapIcons/emp_'+i+'.png';
                           //Draw Marker for each Route
                           makeMarker( leg.start_location, empImage, title );
                       }  
                    }
                });
                
             //*********************
            //Draw Marker
             var makeMarker = function( position, icon, title){
                 new google.maps.Marker({
                  position: position,
                  map: map,
                  icon: icon,
                  title: title
                 });
                 
             };
            ///**********************  
            //Cab Location
            var marker = new google.maps.Marker({
            position: new google.maps.LatLng(cab[0],cab[1]),
            map: map,
			icon:image
            });
            //Traffic Layer
            var trafficLayer = new google.maps.TrafficLayer();
            trafficLayer.setMap(map);
            }
        };
        
    };

 angular.module('efmfmApp').directive('efmfmSingleLiveTrip', efmfmSingleLiveTrip);
}());