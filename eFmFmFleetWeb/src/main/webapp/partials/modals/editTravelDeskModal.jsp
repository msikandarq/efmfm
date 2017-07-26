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
<div class="loading"></div>
<div class = "editModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Travel Desk</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "editTravelDesk" class = "editEscortForm">           
           <div class = "col-md-6 col-xs-12 form-group">
               <label for = "escortName">Shift Time</label>
                <select class = 'form-control'
                        ng-model="update.shiftTime" 
                        ng-options="shiftsTime.shiftTime for shiftsTime in shiftsTimes track by shiftsTime.shiftTime"
                        ng-change = "setshiftTime(update.shiftTime)"
                        required>
                    <option value="">SELECT SHIFT TIME</option>
               </select>
                <!-- <select class = 'form-control'
                        ng-model="update.shiftTime" 
                        ng-if = 'triptype == "DROP"'
                        ng-options="shiftsTime.shiftTime for shiftsTime in shiftsTimes track by shiftsTime.shiftTime"
                        ng-change = "setshiftTime(update.shiftTime)"
                        required>
                    <option value="">SELECT SHIFT TIME</option>
               </select> -->
           </div>
             
           <div class = "col-md-6 col-xs-12 form-group">
               <label for = "escortMobileNumber">Days Off</label>
               <div class = 'multiSelect_travelDesk'
                    ng-dropdown-multiselect="" 
                    options="daysData" 
                    selected-model="update.daysModel" 
                    extra-settings="daysSettings"
                    translation-texts="daysButtonLabel"></div>
           </div>
           
           <div class = "col-md-6 col-xs-12 form-group">
               <label for = "escortAddress">Routes</label>
                <select class = 'form-control'
                        ng-model="update.zone" 
                        ng-options="zoneData.routeName for zoneData in zonesData track by zoneData.routeId"
                        ng-change = "setZone(update.zone)"
                        required>
                    <option value="">SELECT ROUTES</option>
               </select>
           </div>
           
           <div class = "col-md-6 col-xs-12 form-group">
               <label for = "location">Pickup/Drop Location</label>               
                <select class = 'form-control'
                        ng-model="update.location" 
                        ng-options="normNod.nodalPointTitle for normNod in normalNodalData track by normNod.nodalPointId"
                        ng-change = "setNormNod(update.location)"
                        required>
                    <option value="">SELECT LOCATION</option>
               </select>
           </div>    
           <div class = "col-md-6 col-xs-12 form-group" ng-if = 'triptype == "DROP"'>
               <label for = "dob">Drop Sequence</label>
               <input type = 'text' class = 'form-control' ng-model = 'update.sequenceNumber'>
           </div>
       
           <div class = "col-md-2 col-xs-12 form-group"
                ng-hide = 'triptype == "DROP"'>
               <label class ='areaTimePicker' for = "dob">{{triptype}}<span class = 'tripTypeLabel'>Time</span></label></div>           
       
           <div class = "col-md-4 col-xs-12 form-group"
                ng-if = 'triptype != "DROP"'>
               <timepicker ng-model="update.selectedDate" 
                           hour-step="hstep" 
                           minute-step="mstep" 
                           show-meridian="ismeridian" 
                           readonly-input = 'true'
                           class = "timepicker2_empReq floatLeft">
               </timepicker>
           </div>       
       
           <div class = "col-md-6 col-xs-12 form-group checkBox_travelRequest">           
               <input type="checkbox" 
                      ng-model="update.changeMasterData"
                      ng-true-value="'Y'" 
                      ng-false-value="'N'">
               <span style="margin-left: 30px;">Change Regular Shift</span>
           </div>
           
           <input ng-model = 'update.timePickerSelectedTime' class = 'hidden'>
           <input ng-model = 'update.selectedDays' class = 'hidden'>
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "save(update)" ng-disabled="editTravelDesk.$invalid">Save
    </button> 
<button type="button" class="btn btn-warning buttonRadius0" ng-click = "cancel()">Cancel</button></div>
     
</div>