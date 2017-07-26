(function(){
    
    var efmfmRouteHistory = function($http, $rootScope){
        return{
            restrict: 'E',
            scope : {
            mapDatas: '=',
            page: '@'
            },
            template: '<div></div>',
            replace:true,           
            link:function(scope, element, attrs){ 
            
            // Service - This ajax request will be take all the route history using routeId

            var dataObj = {
                routeId :$rootScope.routeIdData,
                userId:profileId,
                combinedFacility:combinedFacility   // 
            }; 
            $http.post('services/history/historyMap/',dataObj).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                var waypts = [];
                var lattilongi = [];
                var path_start = new Array();  // Empty array for path start 
                var path_end = new Array();    // Empty array for End start 
                var mapCenter = new Array(); 
                var baseLocation = new Array();   
                var location = [];

                angular.forEach(data, function(value, key) {
                    if(key === 0){  
                      var LattitudeStart = value.Lattitude; // Lattituute Start value stored here
                      var LongitudeStart = value.Longitude; // Longitude Start value stored here
                      path_start.push(new google.maps.LatLng(LattitudeStart,LongitudeStart));
                      mapCenter.push(new google.maps.LatLng(LattitudeStart,LongitudeStart));   // Map Center value stored here
                      baseLocation.push(value.Lattitude,value.Longitude);
                    }else{
                      var LattitudeEnd = value.Lattitude;   // Lattituute End value stored here
                      var LongitudeEnd = value.Longitude;   // Longitude End value stored here
                      var concatLattiLongi = LattitudeEnd + ',' + LongitudeEnd;
                      var stopover = true;
                      path_end.push(new google.maps.LatLng(LattitudeEnd,LongitudeEnd));
                      path_start.push(new google.maps.LatLng(value.Lattitude,value.Longitude));
                      location.push({concatLattiLongi , stopover});
                    }
                }); 
                
               // Map zoom value handled Below - If way points less then 40 : Zoom value set here - 17

               if(data.length <= 40){   
                  var mapOptions = {
                    zoom: 17,
                    center: mapCenter,
                    mapTypeId: google.maps.MapTypeId.TERRAIN
                  };
               }else{
                  var mapOptions = {
                    zoom: 12,
                    center: mapCenter,
                    mapTypeId: google.maps.MapTypeId.TERRAIN
                  };
               }
               
              var map = new google.maps.Map(document.getElementById(attrs.id),mapOptions);

              // Set Moving Icon Color, Stroke Color & Stroke Opacity.

              var carPolyline = new google.maps.Polyline({
                  map: map, 
                  geodesic : true,
                  strokeColor : '#FF0000',
                  strokeOpacity : 1.0,
                  strokeWeight : 2
              });

              var carPath = new google.maps.MVCArray();
                for ( var i = 0; i < path_start.length; i++) {
                    if(i === 0) {
                        carPath.push(path_start[i]);
                        carPolyline.setPath(carPath);
                    } else {
                        setTimeout((function(latLng) {
                            return function() {
                                  new google.maps.Marker({
                                        map: map,
                                        icon:{
                                            path:google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                                            scale:3,
                                            strokeColor: 'white',
                                            strokeWeight: 1,
                                            fillOpacity: 0.8,
                                            fillColor: 'orange',
                                            offset: '100%'
                                        },
                                        position: latLng,
                                      
                                    });
                                carPath.push(latLng);
                                map.setCenter(latLng);
                            };
                        })(path_start[i]), 300 * i);
                    }
                }
              }
              }).error(function(data, status, headers, config) {
            });
          }
        };
      };

 angular.module('efmfmApp').directive('efmfmRouteHistory', efmfmRouteHistory);
}());