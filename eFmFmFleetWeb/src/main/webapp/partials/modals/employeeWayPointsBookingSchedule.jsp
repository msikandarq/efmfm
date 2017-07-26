<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->
 
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "newDriverFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-map-marker mapMarkerIconCreateRequest col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Location Details</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <table class="table table-responsive table-bordered table-hover">
    <thead class="locationHeaderView">
      <tr>
        <th>S No</th>
        <th>View Map</th>
        <th>Location Name</th>
        <th>Address</th>
        <!-- <th>Remove</th> -->
      </tr>
    </thead>

    <tbody ng-repeat="destination in employeeWayPointsDetails">
      <tr>
        <td>{{$index + 1}}</td>
        <td>
          <span class="pointer" ng-click = "openMap($index)">
                                      <i class="icon-map-marker mapMarkerIcon ng-scope" 
                                         tooltip="View on Map" 
                                         tooltip-placement="right" 
                                         tooltip-trigger="mouseenter">
                                      </i>
          </span>
        </td>
        <!-- <td>{{destination.LocationId}}</td> -->
        <td>{{destination.LocationName}}</td>
        <td>{{destination.LocationAddress}}</td>
        <!-- <td><input type="button" class="btn btn-danger btn-xs" value="Delete" ng-click="removeLocationDetails(destination, $index)" name=""></td> -->
      </tr>
    </tbody>
  </table>
    </div>
      
<div class="modal-footer modalFooter">    
    <button type="button" class="btn btn-primary" ng-click = "cancel()" >Ok</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>