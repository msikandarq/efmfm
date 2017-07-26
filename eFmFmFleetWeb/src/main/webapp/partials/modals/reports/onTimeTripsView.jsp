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

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "editVehicleFormModalTemplate">
  

    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "col-md-2"></div>
            <div class = "modalHeading col-md-10 floatLeft" ng-show="onTimeTripType == 'PICKUP'">OTA Trips (15 Minutes) View Details</div>
            <div class = "modalHeading col-md-10 floatLeft" ng-show="onTimeTripType == 'DROP'">OTD Trips View Details</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    

<div class="modal-body modalMainContent">   
  <table class="table reportMOdal table-condensed table-striped table-bordered table-responsive">
    <thead>
      <tr>
          <th>S No</th>
          <th>Route Name</th>
          <th>Vehicle Number</th>
          <th>Driver Name</th>
          <th>Route Assign Time</th>
          <th>Route Start Time</th>
          <th>Route Close Time</th>
      </tr>
    </thead>
    <tbody>

    <tr ng-repeat = "onTimeview in onTimeviewData">
        <td>{{$index +1}}</td>
         <td>{{onTimeview.routeName}}</td>
        <td>{{onTimeview.vehicleNumber}}</td>
        <td>{{onTimeview.driverName}}</td>     
        <td>{{onTimeview.tripCloseTime}}</td>
        <td>{{onTimeview.tripStartTime}}</td>
         <td>{{onTimeview.tripEndTime}}</td>
       
    </tr>

    </tbody>
  </table>
</div>

<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
</div>