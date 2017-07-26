(function(){
    
    var efmfmGoogleMapSingle = function(){
        return{
            restrict: 'E',
            scope : {
            mapData: '=',
            page: '@'
            },
            template: '<div></div>',
            replace:true,           
            link:function(scope, element, attrs){             
                var waypts = [];
                var baseLocation = scope.mapData[0].baseLatLong.split(',');
                var originLocation = new google.maps.LatLng(baseLocation[0], baseLocation[1]);
                var destinLocation = new google.maps.LatLng(baseLocation[0], baseLocation[1]);
                
                var cords = scope.mapData[0].waypoints.split('|');
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
                    zoom:12,
                    center: center
                  };
               var map = new google.maps.Map(document.getElementById(attrs.id), mapOptions);
                directionsDisplay.setMap(map);
                
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
                       //Drwaing Custom Icons on the Map on each Route
                       for(i=1; i<x; i++){ 
                           var leg = response.routes[ 0 ].legs[ i ];
                           var title = leg.start_address;
                           var empImage = 'images/EmployeeMapIcons/emp_'+i+'.png';
                           makeMarker( leg.start_location, empImage, title );
                       } 
                    }
                });
                
             //*********************
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
            position: originLocation,
            map: map,
			icon:'images/cabImage.png'
            });
            // Traffic Layer
            var trafficLayer = new google.maps.TrafficLayer();
            trafficLayer.setMap(map);
                
            }
        };
        
    };

 angular.module('efmfmApp').directive('efmfmGoogleMapSingle', efmfmGoogleMapSingle);
}());