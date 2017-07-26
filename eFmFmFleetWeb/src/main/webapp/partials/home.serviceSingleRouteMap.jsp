<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    This Page shows a single route on the Map.
                This page is accessible from the home.serviceMapping state only
@State          home.serviceRouteMap
@URL            /serviceRouteMap/:routeId/:waypoints/:baseLatLong   

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<div class = "serviceMappingTemplate container-fluid">
 <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <span class = "col-md-12 col-xs-12">Service Requested Routes</span>   
            
            <div class = "col-md-12 col-xs-12 mainTabDiv_serviceRouteMap" ng-if="isloaded">
                <div class = 'col-md-12'>
                <input class = 'btn btn-success backtoServiceMapping floatRight' type = 'button' 
                       value = 'Back to Service Mapping'
                       ng-click = 'backToServiceMapping()'>
            </div>
                
                <efmfm-google-map-single map-data = "getSingleServiceMap()" class = "mapContainer col-md-12" id = "map-canvas2"></efmfm-google-map-single>
            </div>

              
         </div>
</div>
</div> 
