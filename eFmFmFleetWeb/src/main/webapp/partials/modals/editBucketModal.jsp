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
<div class = "manualStartModal container-fluid" cg-busy="{promise:promise,templateUrl:templateUrl,message:message,backdrop:backdrop,delay:delay,minDuration:minDuration}">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
          <div class = "row"> 
            <div class = "icon-map-marker edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Bucket</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
          </div>
        </div>        
    </div> 
    
<div class="modal-body">
    <div class = "formWrapper">
       <form name="manualStartForm" class = "row driverEditForm">
           <div class = "col-md-6 col-xs-12 form-group"> 
              <label for = "driverName">Driver Name</label>
               <select class = 'form-control'
                        ng-model="edit.driverName" 
                        ng-options="checkInEntitieData.driverName for checkInEntitieData in checkInEntitiesData track by checkInEntitieData.driverName"
                        ng-change = "updateDriverManual(edit.driverName)"
                        required>
                    <option value="">-- Select Driver --</option>
               </select>
               <input type = 'text' ng-model = 'edit.driverNumber' class = 'hidden'>
            </div><!-- /.col-lg-6 -->
           <div class = "col-md-6 col-xs-12 form-group"> 
              <label for = "driverName">Vehicle Number</label>  <br/>             

                    <am-multiselect 
                    class="input-lg"
                    ng-model="edit.vehicleNumber"
                    options="checkInEntity as checkInEntity.vehicleNumber + ' -- ' + checkInEntity.capacity for checkInEntity in checkInEntitiesData"
                    change = "updateVehicleNumberManual(edit.vehicleNumber)">
                    </am-multiselect>
               
            </div>
           <div class = "col-md-6 col-xs-12 form-group"> 
              <label for = "driverName">Device ID</label>       
               <select class = 'form-control'
                        ng-model="edit.deviceId" 
                        ng-options="checkInEntity.deviceId for checkInEntity in checkInEntitiesData track by checkInEntity.deviceId"
                        ng-change = "updateDeviceIdManual(edit.deviceId)"
                        required>
                    <option value="">-- Select Device --</option>
               </select>
               
            </div>
           
           <div class = "col-md-6 col-xs-12 form-group"
                ng-if = "escortRequired !='N'"> 
              <label for = "driverName">Escort Name</label>
               <select class = 'form-control'
                        ng-model="edit.escortName" 
                        ng-options="escortData.escortName for escortData in escortsData track by escortData.escortName"
                        ng-change = "updateEscortManual(edit.escortName)"
                        ng-required = "escortNameFlag !='Escort Required But Not Available'">
                    <option value="">-- Select Escort --</option>
               </select>
               
            </div>           
    </form>
    </div>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" 
            class="btn btn-info editBucketbutton buttonRadius0" 
            ng-click = "save(edit)" 
            ng-disabled="manualStartForm.$invalid">Save</button>
    <button type="button" class="btn btn-default editBucketbutton buttonRadius0" ng-click = "cancel()">Cancel</button> 
</div>
</div>