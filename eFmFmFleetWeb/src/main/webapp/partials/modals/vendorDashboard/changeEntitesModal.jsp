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
<div class = "editVehicleEntityFormModalTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-8 floatLeft">Edit Entities</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper_editEntity">
       <form name = "editVehicleEntityForm" class = "editVehicleEntityForm">    
           <div class = "col-md-12 col-xs-12 form-group"> 
              <label for = "driverName">Driver Name</label>
               <select class = 'form-control'
                        ng-model="edit.driverName" 
                        ng-options="driverData.driverName for driverData in driversData track by driverData.driverName"
                        ng-change = "updateDriverManual(edit.driverName)"
                        required>
                    <option value="">-- Select Driver --</option>
               </select>
               
                   <input type="text" class="noPointer hidden form-control" 
                       aria-label="..." 
                       ng-model = "edit.driverNumber"
                       name = 'driverName'>
            </div>
           
           <div class = "col-md-12 col-xs-12 form-group"> 
              <label for = "driverName">Vehicle Number</label>
               <select class = 'form-control'
                        ng-model="edit.vehicleNumber" 
                        ng-options="vehicleData.vehicleNumber for vehicleData in vehiclesData track by vehicleData.vehicleNumber"
                        ng-change = "updateVehicleNumberManual(edit.vehicleNumber)"
                        required>
                    <option value="">-- Select Vehicle --</option>
               </select>
               
            </div>
           
           <div class = "col-md-12 col-xs-12 form-group"> 
              <label for = "driverName">Device Number</label>               
               <select class = 'form-control'
                        ng-model="edit.deviceId" 
                        ng-options="deviceData.deviceId for deviceData in devicesData track by deviceData.deviceId"
                        ng-change = "updateDeviceIdManual(edit.deviceId)"
                        required>
                    <option value="">-- Select Device --</option>
               </select>               
            </div>
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-warning buttonRadius0" ng-click = "updateVehicleEntity(edit)" 
            ng-disabled="editVehicleEntityForm.$invalid">Update Check Details</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>