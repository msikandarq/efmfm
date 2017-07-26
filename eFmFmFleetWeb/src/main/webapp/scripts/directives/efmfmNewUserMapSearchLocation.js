(function(){    
    var efmfmNewUserMapSearchLocation = function(){        
            return{
            restrict: 'E',
            scope :{center: '=', user: '='},
            template: '<div></div>',
            replace:true,           
            link:function(scope, element, attrs){
                 var x = scope.center.lat;
                 var y = scope.center.lon; 
                 var newAddress = '';
                 var areaName = '';
                 var center; 
                 if(!scope.center){
                    center = new google.maps.LatLng( scope.center.lat, scope.center.lon);
                 }
                 else{
                    center = new google.maps.LatLng(x,y);
                 }              
                
                scope.$watch("center", function () {
                    if (map && scope.center)
                        map.setCenter(getLocation(scope.center));
                        marker.setPosition( new google.maps.LatLng( scope.center.lat, scope.center.lon ) );
                });
                
                // convert current location to Google maps location
                function getLocation(loc) {
                    if (loc == null) return new google.maps.LatLng( scope.center.lat, scope.center.lon);
                    if (angular.isString(loc)) loc = scope.$eval(loc);
                    return new google.maps.LatLng(loc.lat, loc.lon);
                }
                
                var mapOptions = {
                center: center,
                zoom: 11,
                mapTypeControl: false,
                streetViewControl: false,
                mapTypeId: google.maps.MapTypeId.ROADMAP
                };
            
                map = new google.maps.Map(document.getElementById(attrs.id), mapOptions); 
                
                //Creating Marker
                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(x,y),
                    map: map,
                    animation: google.maps.Animation.DROP,
                    draggable:true
                  }); 
                
                //Getting Address for New Location
                //This function is called after the Pin is dragged to a new location
                function geocodePosition(pos) 
                {
                   geocoder = new google.maps.Geocoder();
                   geocoder.geocode
                    ({
                        latLng: pos
                    }, 
                        function(results, status) 
                        {
                            if (status == google.maps.GeocoderStatus.OK) 
                            {
                                newAddress = results[0].formatted_address;
                                //VERY IMPORTANT: $apply() will update the parent scope    
                              scope.$apply(function(){
                                    scope.user.address = newAddress;
                                    //scope.user.areaName = areaName;
                                });
                                document.getElementById("newAddress").value = newAddress; 
                            } 
                            else 
                            {
                                newAddress = 'Address not Found';
                                scope.$apply(function(){
                                    scope.user.address = newAddress;
                                });
                         }
                 });} 
     
                //Drag Event for the Pin
                google.maps.event.addListener(marker, 'dragend', function (event) {
                    var newlatlang = event.latLng.lat() + ',' + event.latLng.lng();
                    geocodePosition(marker.getPosition());                    
                    scope.$apply(function(){
                        scope.user.cords = newlatlang;
                    });   
                    document.getElementById("latlangInput").value = newlatlang;  
                    
                });
            }
        };
        
    };

 angular.module('efmfmApp').directive('efmfmNewUserMapSearchLocation', efmfmNewUserMapSearchLocation);
}());