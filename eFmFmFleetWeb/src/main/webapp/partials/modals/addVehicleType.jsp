<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'">
</div>
<div class="loading">
</div>
<div class = "addNewMultipleLocationTemplate">
  <div class = "row">
    <div class="modal-header modal-header1 col-md-12">
      <div class = "row"> 
        
        <div class = "modalHeading col-md-10 floatLeft">Add Vehicle Type
        </div>
        <div class = "col-md-1 floatRight pointer">
          <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
        </div>   
      </div>  
    </div>        
  </div>
  <div class="modal-body addNewShiftTimeModal">
  <div class = "row">
        <div class = "col-md-6">
          <label>Vehicle Capacity</label>
         <!--  <input type="text" class="form-control" placeholder="Enter Vehicle Capacity" is-number-only-valid ng-model="vehicle.selectCapacity" name=""> -->
        </div>
        <div class = "col-md-6">
          <label>Vehicle Type</label>
          <!-- <input type="text" class="form-control" placeholder="Enter Vehicle Type" expect-special-char ng-model="vehicle.vehicleType" name=""> -->
        </div>
     </div>
     <br>
     <div class = "row marginBottom10" ng-repeat="vehicle in vehicles">
        <div class = "col-md-5">
          <input type="text" class="form-control" placeholder="Enter Vehicle Capacity" maxlength="2" is-number-only-valid ng-model="vehicle.selectCapacity" name="">
        </div>
        <div class = "col-md-5">
          <input type="text" class="form-control" placeholder="Enter Vehicle Type" expect-special-char ng-model="vehicle.vehicleType" name="">
        </div>
        <div class = "col-md-2">
         <button type="button"
                class="btn btn-danger btn-sm"
                ng-click = "removeVehicleRow(vehicle, $index)" 
                ng-disabled="!vehicle.selectCapacity || !vehicle.vehicleType">Remove
        </button>
        </div>
     </div>
     <br>
     <div class = "row">
        <div class = "col-md-12">
          <button type="button" 
                class="btn btn-success btn-sm"
                ng-click = "addVehicleRow()">Add
        </button>
        </div>
     </div>
  </div>      
    <div class="modal-footer modalFooter pointer"> 
      <div class="">
        <button type="button" 
                class="btn btn-success btn-sm"
                ng-click = "saveVehicleType(vehicles)">Submit
        </button>
    
        <button type="button" 
                class="btn btn-danger btn-sm"
                ng-click = "cancel()">Cancel
        </button>
      </div>
    </div>
  </div>
