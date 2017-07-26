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
<div class = "addVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add / Modify Odometer Reading</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    
    <div class="modal-body modalMainContent">
      <form name="odometerForm">
        <!-- <table class="table table table-bordered table-striped table-responsive">    
            <thead>
                <tr style="background-color: #4b4c4c; color: white;">
                    <th style="width: 35%; text-align: center;"><strong>Date</strong>  :  {{viewKMDetails.fromDate}}</th>
                    <th style="width: 35%; text-align: center; "><strong>Trip Type</strong>  :  {{viewKMDetails.tripType}}</th>
                    <th style="width: 30%; text-align: center;"><strong>Shift Time</strong>  :  {{viewKMDetails.time}}</th>
                </tr>
            </thead>
         
        </table> -->
        <table class="table table table-bordered table-striped table-responsive">    

            <thead>
                <tr class="success">
                    <th>Route Id</th>
                    <th>Route Name</th>
                    <th>Driver Name</th>
                    <th>Trip Type</th>
                    <th>Shift Time</th>
                    <th>Vehicle Number</th>
                    <th>Planned Distance</th>
                    <th>Travelled Distance</th>
                    <th>Odometer Distance</th>
                    <th>Start Odometer</th>
                    <th>End Odometer</th>
                    <!-- <th>Update</th> -->
                </tr>
            </thead>
        <tbody>
      
        <div class="panel panel-danger odometerDetails" ng-if="odometerShow">
          <div class="panel-heading">* Start Odometer should not be greater than End Odometer</div>
        </div>
        <div class="panel panel-success odometerDetails" ng-if="!odometerShow">
          <div class="panel-heading"><strong><p class="text-center">Odometer Reading Details</p></strong></div>
        </div>
       
        <tr ng-repeat="odometer in viewOdometerDetails" > 
               <td>{{odometer.routeId}}</td>
               <td>{{odometer.routeName}}</td>
               <td>{{odometer.driverName}}</td>
               <td>{{odometer.tripType}}</td>
               <td>{{odometer.shiftTime}}</td>
               <td>{{odometer.vehicleNumber}}</td>
               <td>{{odometer.plannedDistance}}</td>
               <td>{{odometer.travelledDistance}}</td>
               <td>{{odometer.odoDistance}}</td>
               <td ng-class="isGrey[$index]"><input type="text" class="odometerTextWidth" name="" ng-model="odometer.startKm" ng-change = "odometerIndex(odometer,$index)" required only-num></td>
               <td ng-class="isGrey[$index]"><input type="text" class="odometerTextWidth" name="" ng-model="odometer.endKm" ng-change = "odometerIndex(odometer,$index)" required only-num></td>

        </tr>
        </table>
    </form>
    </div>   
    <div class="modal-footer modalFooter">    
        <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
        <button type="button" class="btn btn-success"  ng-click = "updateAllOdometerData(viewOdometerDetails)" ng-disabled= "odometerFormDisbled" data-ng-disabled="odometer.startKm >= odometer.endKm">Update</button>  
</div>   
</div>