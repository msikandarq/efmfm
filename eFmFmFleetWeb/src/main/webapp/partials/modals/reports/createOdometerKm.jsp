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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "exportModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-pencil edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Add / Modify Odometer Reading - Shiftwise</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>   


<div class="modal-body modalMainContent modalMainContentDownload">     
  <form name = "assignCabForm" class = 'createNewZone'>  


      <div class = "col-md-6 col-xs-12"> 
           <div>
           <label>Select Type</label>
              <select ng-model="odometer.selectionType"
                       class="form-control" 
                       ng-options="tripType.text for tripType in selectionTypeData track by tripType.value"
                       ng-change = "setTripType(odometer.tripType)"
                       required>
              </select>
           </div>
       </div>

       <div class = 'col-md-6' ng-show="odometer.selectionType.value == 'Vehicle Wise'">
            <label>Date</label>                    
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="openDownloadDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
              <input type="text" 
                     ng-model="odometer.date" 
                     class="form-control" 
                     placeholder = "Select Date" 
                     datepicker-popup = '{{format}}'
                     is-open="datePicker.openeddownloadDate" 
                     show-button-bar = false
                     show-weeks=false
                     datepicker-options = 'dateOptions'
                     readonly
                     name = 'date'>
               </div>
        </div> 

       <div class = "col-md-6 col-xs-12" ng-show="odometer.selectionType.value == 'Vehicle Wise'"> 
           <div>
           <label>Select Vendor</label>
               <select ng-model="odometer.vendor"
                                           class="form-control marginBottom10" 
                                           ng-options="allInspectionVendor.name for allInspectionVendor in allInspectionVendors track by allInspectionVendor.Id"
                                           ng-change = "setVendor(odometer.vendor)"
                                           >
                                     <option value="">SELECT VENDOR</option>
                                  </select>
           </div>
       </div> 

       <div class = "col-md-6 col-xs-12" ng-show="odometer.selectionType.value == 'Vehicle Wise'"> 
           <div>
           <label>Select Vehicle</label>
               <select ng-model="odometer.vehicleSelected"
                                           class="form-control" 
                                           ng-options="allVehicle.vehicleNumber for allVehicle in allVehiclesInpectionForm track by allVehicle.Id"
                                           ng-change = "setInspectionForVehicle(odometer.vehicleSelected)">
                                     <option value="">SELECT VEHICLE</option>
                                  </select>
           </div>
       </div> 


	   <div class = "col-md-6 col-xs-12" ng-show="odometer.selectionType.value == 'Shift Time Wise'"> 
           <div>
           <label>Trip Type</label>
               <select ng-model="odometer.tripType"
                       class="form-control" 
                       ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                       ng-change = "setTripType(odometer.tripType)"
                       required>
              </select>
           </div>
       </div> 
        <div class = 'col-md-6' ng-show="odometer.selectionType.value == 'Shift Time Wise'">
            <label>Date</label>                    
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="openDownloadDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
              <input type="text" 
                     ng-model="odometer.date" 
                     class="form-control" 
                     placeholder = "Select Date" 
                     datepicker-popup = '{{format}}'
                     is-open="datePicker.openeddownloadDate" 
                     show-button-bar = false
                     show-weeks=false
                     datepicker-options = 'dateOptions'
                     readonly
                     name = 'date'>
               </div>
        </div> 
	   <div class = "col-md-6 col-xs-12" ng-show="odometer.selectionType.value == 'Shift Time Wise'"> 
           <div>
           <label>Select Shift Time</label><br>
               <input type="radio" 
                       class = "floatLeft select_radio_assignCb radio_assignCab"
                       ng-model="shiftTime"
                       ng-disabled = "isRadioDisable()"
                       value="preDefineShiftTime" 
                       ng-change = "selectShiftTimeRadio(shiftTime)"
                       >
               <select ng-model="odometer.shiftTime" 
                       class="form-control select_assignCab floatLeft" 
                       ng-options="shiftTime.shiftTime for shiftTime in shiftsTime | orderBy:'shiftTime' "
                       ng-disabled = "shiftTime != 'preDefineShiftTime'"
                       ng-required = "shiftTime == 'preDefineShiftTime'">
              </select>
           </div>
       </div>  
    <div class = "col-md-6 col-xs-12" ng-show="odometer.selectionType.value == 'Shift Time Wise'"> 
        <div class = "timerDiv">
            <input type="radio" 
                   ng-model="shiftTime" 
                   class = "timepickerRadioButton radio_assignCab floatLeft"
                   value="ADHOCTime" 
                   ng-disabled = "isRadioDisable()"
                   ng-change = "selectShiftTimeRadio2(shiftTime)"
                   >     
                <timepicker ng-model="odometer.createNewAdHocTime" 
                          ng-disabled = "typeOfShiftTimeSelected != 'ADHOCTime'"
                          hour-step="hstep" 
                          minute-step="mstep" 
                          show-meridian="ismeridian" 
                          readonly-input = 'true'
                          class = "timepicker2_empReq floatLeft">
               </timepicker>
        </div>
    </div>



  </form>   
</div>
     <div class="modal-footer modalFooter createNewZone_modalFooter">  
       <!--     <button type="button" class="btn btn-close floatRight noMoreClick" ng-click = "cancel()">Cancel</button>
           <button type="button" 
                   class="btn btn-success floatRight noMoreClick" 
                   ng-click = "createOdometer(odometer,shiftTime)"
                   ng-disabled="assignCabForm.$invalid">Next</button> -->

             <button type="button" class="btn btn-warning" ng-click = "cancel()">Cancel</button>    
        <button type="button" class="btn btn-success"  ng-click = "createOdometerData(odometer,shiftTime)" ng-disabled="assignCabForm.$invalid">Next</button>  

     </div>
    <!--  <div class="modal-footer modalFooter">    
        <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
        <button type="button" class="btn btn-success"  ng-click = "createOdometer(odometer,shiftTime)">Next</button>  
</div>  --> 
</div>


