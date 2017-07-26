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
            <div class = "modalHeading col-md-10 floatLeft">Route History</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>          
    
            <br/>
          <table class="table table table-bordered table-striped table-responsive height100Perct">    
              <thead>
                  <tr class="routHistoryMapBg">
                      
                      <th class="col20 text-center"><strong class="routeHistoryMapFont">Route Id</strong>  <br/>  {{routeId}}</th>
                      <th class="col20 text-center"><strong class="routeHistoryMapFont">Trip Assign Date</strong>  <br/> {{tripAssignDate}}</th>
                      <th class="col20 text-center"><strong class="routeHistoryMapFont">Trip Start Date</strong>  <br/>{{tripStartDate}}</th>
                      <th class="col20 text-center"><strong class="routeHistoryMapFont">Trip Complete Date</strong>  <br/>{{tripCompleteDate}}</th>
                      <th class="col20 text-center"><strong class="routeHistoryMapFont">Distance</strong>  <br/>{{tripCompleteDate}}</th>
                  </tr>
              </thead>
          </table>
           
          <div></div>
            <div class = "col-md-12 col-xs-12 mainTabDiv_serviceRouteMap" ng-if="isloaded">
                <efmfm-route-history map-data = "getRouteHistoryMap()" class = "mapContainer col-md-12" id = "map-canvas"></efmfm-route-history>
            </div>

              
         </div>
         <br/>
         <div class="modal-footer modalFooter">    
  
    <button type="button" class="btn btn-warning" ng-click = "cancel()">Cancel</button>    
</div>
</div>
</div> 
