(function(){
    
    var efmfmShowAllCabLocations = function($http, $interval, $rootScope){
        return{
            restrict: 'E',
            template: '<div></div>',
            replace:true,           
            link:function(scope, element, attrs){
                var markers = [];
                var image = 'images/cabImage.png';
                var map = new google.maps.Map(document.getElementById('map-canvas'), {
                      zoom: 8,
                      center: {lat: parseInt(scope.defaultLocation[0]), lng: parseInt(scope.defaultLocation[1])}
                    });
                scope.setCabLocation = function(){                    
                    
                        if(scope.mapData.length!=0) {

                        angular.forEach(scope.mapData, function (item){
                            var lattiLongi = [];

                            var content = "<div style = 'font-size:12px;'><span><strong>Vehicle Number</strong> <span></div>" +  item.vehicleNumber + 
                                          "<br />" +
                                          "<div style = 'font-size:12px;'><span><strong>Vehicle Speed</strong> <span></div>" +  item.vehicleSpeed +
                                          "<br />" +
                                          "<div style = 'font-size:12px;'><span><strong>Driver Name</strong><span></div>" + item.driverName;
                            var infowindow = new google.maps.InfoWindow();
                            lattiLongi = item.lattiLongi.split(',');
                            var cabLocations = {lat: parseFloat(lattiLongi[0]), lng: parseFloat(lattiLongi[1])};

                            var marker = new google.maps.Marker({
                              position: cabLocations,
                              map: map,
                              title: parseFloat(lattiLongi[0])+" , " + parseFloat(lattiLongi[1]),
                              icon:image
                            });
                            markers.push(marker);



                            google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
                                return function() {
                                   infowindow.setContent(content);
                                   infowindow.open(map,marker);
                                };
                            })(marker,content,infowindow));
                        });

                    }//END of if(scope.mapData.length!=0) 
//                    else{
//                        var center = new google.maps.LatLng(scope.defaultLocation[0],scope.defaultLocation[1]);
//                        var mapOptions = {
//                        center: center,
//                        zoom: 8,
//                        mapTypeControl: false,
//                        streetViewControl: false,
//                        mapTypeId: google.maps.MapTypeId.ROADMAP,
//                        icon:image
//                        };            
//                        map = new google.maps.Map(document.getElementById(attrs.id), mapOptions);  
//                        //Traffic Layer
//                        var trafficLayer = new google.maps.TrafficLayer();
//                        trafficLayer.setMap(map);
//                    }
                    
                };
                scope.setCabLocation();
                
       
               scope.setNewCabLocationInterval = $interval(scope.setCabLocation, 4000);  

               var dereg = $rootScope.$on('$locationChangeSuccess', function() {
                    $interval.cancel(scope.setNewCabLocationInterval);
                    dereg();
                });  

                }//****END OF LINK
            
        };
        
    };

 angular.module('efmfmApp').directive('efmfmShowAllCabLocations', efmfmShowAllCabLocations);
}());