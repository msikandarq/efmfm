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
            <div class = "modalHeading col-md-10 floatLeft">No Show Pickup Trips View Details</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>

<div class="modal-body modalMainContent">   
  <table class="table table-condensed table-striped table-bordered table-responsive">
    <thead>
      <tr>
        <th>S No</th>
        <th>Vehicle Number</th>
        <th>Vehicle Model</th>
        <th>Driver Name</th>
      </tr>
    </thead>
    <tbody>

    <tr ng-repeat = "pickupTrips in pickupTripsData">
        <td>{{$index +1}}</td>
        <td>{{pickupTrips.vehicleNumber}}</td>
        <td>{{pickupTrips.vehicleModel}}</td>
        <td>{{pickupTrips.driverName}}</td>
    </tr>

    </tbody>
  </table>
</div>

<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
</div>