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
        <div class = "row">
        <div class="modal-headerMap modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-map-marker edsModal-icon addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Route Map View</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>          
    <!-- <pre>{{empDetails | json}}</pre> -->
            <br/>
          <!-- <table class="table table table-bordered table-striped table-responsive" style="height: 100px;">     -->
              <!-- <thead>
                  <tr style="background-color: #4b4c4c; color: white;">
                      
                      <th style="width: 15%; text-align: center;"><strong>Route Id</strong>  :  {{route.routeId}}</th>
                      <th style="width: 15%; text-align: center;"><strong>Date</strong>  :  {{route.tripActualAssignDate}}</th>
                      <th style="width: 20%; text-align: center;"><strong>Trip Type</strong>  :  {{route.tripType}}</th>
                      <th style="width: 20%; text-align: center;"><strong>Shift Time</strong>  :  {{route.shiftTime}}</th>
                      <th style="width: 30%; text-align: center; "><strong>Route Name</strong>  :  {{route.routeName}}</th>
                  </tr>
              </thead> -->
             <!--  <input type="button" class="btn btn-success" value="click" name=""> -->
              <!-- <tr ng-repeat="empDetail in empDetails">
                    <td>{{empDetail.name}}</td>
              </tr> -->
          <!-- </table> -->
           
          <div></div>
            <div class = "col-md-12 col-xs-12 mainTabDiv_serviceRouteMap" ng-if="isloaded">
                <efmfm-google-map-single map-data = "getSingleServiceMap()" class = "mapContainer col-md-12" id = "map-canvas2"></efmfm-google-map-single>
            </div>

              
         </div>
         <br/>
         <div class="modal-footer modalFooter">    
  
    <button type="button" class="btn btn-warning" ng-click = "cancel()">Cancel</button>    
</div>
</div>
</div> 
