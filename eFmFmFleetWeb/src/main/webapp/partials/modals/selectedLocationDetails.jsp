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
            <div class = "modalHeading col-md-10 floatLeft">Selected Location Details</div>
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
        <!-- <th>Location Id</th> -->
        <th>Location Name</th>
        <!-- <th>Employee Name</th> -->
        <th>Location Address</th>
        <!-- <th>Longitude/latitude</th> -->
        <th>Remove</th>
      </tr>
    </thead>
    <tbody ng-repeat="destination in destinationArray track by $index">
      <tr>
        <td>{{$index + 1}}</td>
        <!-- <td>{{destination.areaId}}</td> -->
        <td>{{destination.areaName}}</td>
        <!-- <td>{{destination.employeeName}}</td> -->
        <td>{{destination.locationAddress}}</td>
        <!-- <td>{{destination.locationLatLng}}</td> -->
        <td><input type="button" class="btn btn-danger btn-xs" value="Remove" ng-disabled="destination.buttonStatus == 'disabled'" ng-click="removeLocationDetails(destination, $index)" name=""></td>
      </tr>
      
    </tbody>
  </table>
    </div>
      
<div class="modal-footer modalFooter">    
    <button type="button" class="btn btn-primary" ng-click = "ok()" >Ok</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>