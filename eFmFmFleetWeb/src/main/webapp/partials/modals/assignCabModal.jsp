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
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "createNewRouteModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-pencil edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Routing</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>      
<div class="modal-body modalMainContent">     
  <form name = "form.assignCabForm" class = 'createNewZone'> 
     <div class = "col-md-6 col-xs-12"> 
           <div>
               <label>{{assignCab.tripType.text}} Date</label>                    
               <div class = "input-group calendarInput">
                    <!--  tooltip="Date of Birth"
                     tooltip-placement="top"
                     tooltip-trigger="mouseenter"> -->
                    <span class="input-group-btn">
                        <button class="btn btn-default" ng-click="openAssignDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                  <input type="text" 
                         ng-model="assignCab.assignDate" 
                         class="form-control" 
                         placeholder = "Select Date" 
                         datepicker-popup = '{{format}}'
                         min-date = "setMinDate"
                         is-open="datePicker.openedassignDate" 
                         show-button-bar = false
                         show-weeks=false
                         datepicker-options = 'dateOptions'
                         readonly
                         required
                         name = 'assignDate'>

           </div>
           </div>
       </div>  
    <div class = "col-md-6 col-xs-6" ng-show="multiFacility == 'Y'"> 
        <label>Select Facility For Routing</label><br>
        <am-multiselect class="input-lg"
                        multiple="true"
                        ms-selected ="{{facilityData.length}} Facility(s) Selected"
                        ng-model="facilityData"
                        ms-header="All Facility"
                        class="facilitySpanSixMargin"
                        style="margin-left: -17px;"
                        options="facility.branchId as facility.name for facility in facilityDetails"
                        change="setFacilityDetails(facilityData)">
        </am-multiselect> 
    </div> 
    
    <div class = "col-md-6 col-xs-12">  
           <div>
           <label>Trip Type</label>
               <select ng-model="assignCab.tripType"
                       class="form-control" 
                       ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                       ng-change = "setTripType(assignCab.tripType, facilityData)" 
                       ng-disabled="facilityData.length == 0" 
                       required>
              </select>
           </div>
       </div>  


    <div class = "col-md-6 col-xs-6" ng-show="multiFacility == 'Y'"> 
        <div  ng-if="assignCab.tripType.value == 'PICKUP'"> <label>Trip complete Facilty For PickUp</label> <br/></div>
        <div  ng-if="assignCab.tripType.value == 'DROP'"> <label>Trip Start Facility For Drop </label> <br/></div>
        <select ng-model="assignCab.baseFacilityData"
               class="form-control"                                            
               ng-options="facility.branchId as facility.name for facility in baseFacilityDetails"
               ng-change = "setBaseFacilityDetails(assignCab.baseFacilityData)"
               ng-required="multiFacilityRequired" 
               >
         <option value="">{{baseFacilityLabel}}</option>
        </select>
     </div>
     

     

     <div class = "col-md-6 col-xs-12"> 
           <div>
               <input type="radio" 
                       class = "floatLeft radio_assignCab"
                       ng-model="shiftTime"
                       ng-disabled = "isRadioDisable();"
                       value="preDefineShiftTime" 
                       ng-change = "selectShiftTimeRadio(shiftTime)"
                       >
           <label>Select Shift Time</label><br />
               <select ng-model="assignCab.shiftTime"
                       style="width: 240px;" 
                       class="form-control select_assignCab floatLeft" 
                       ng-options="shiftTime.shiftTime for shiftTime in shiftsTime | orderBy:'shiftTime' "
                       ng-disabled = "shiftTime != 'preDefineShiftTime'"
                       ng-required = "shiftTime == 'preDefineShiftTime'"
                       ng-change = 'setAlgo()'>
<!--                 <option value="">-- Select Shift Time --</option>-->
              </select>
           </div>
       </div>  
    <div class = "col-md-6 col-xs-12"> 
        <div class = "timerDiv row" style="margin-left: 2px;">
            <input type="radio" 
                   ng-model="shiftTime" 
                   class = "radio_assignCab2 floatLeft"
                   value="ADHOCTime" 
                   ng-disabled = "isRadioDisable()"
                   ng-change = "selectShiftTimeRadio2(shiftTime)"
                   >     
            <label class = "floatLeft labelTimePickerShiftTime">Select Adhoc Shift Time</label>
            
                <timepicker ng-model="assignCab.createNewAdHocTime" 
                          ng-disabled = "typeOfShiftTimeSelected != 'ADHOCTime'"
                          hour-step="hstep" 
                          minute-step="mstep" 
                          show-meridian="ismeridian" 
                          readonly-input = 'true'
                          class = "timepicker2_empReq floatLeft"
                            ng-change = 'setAlgo()'>
               </timepicker>
        </div>
    </div>

     
 

<div class="" >
<div class = 'col-md-12 assignCabDivStyle' >
  <div class = 'row marginNeg10'>
    <div class = 'col-md-6 marginBottom15'>
      <div ng-init="assignCab.manualRouting=1" class="" />
          <h5>
            <label class="radio-inline">
                  <input type="radio" 
                          ng-model="assignCab.manualRouting" 
                          value="1" 
                          ng-change="manualRouting(assignCab.tripType, shiftTime, assignCab.manualRouting)"> 
                          Manual Routing
            </label>
          </h5>
      </div>
    </div>
    
    <div class = 'col-md-6 marginBottom15'>
        <div ng-init="assignCab.manualRouting=1" class="" />
            <h5>
              <label class="radio-inline">
                    <input type="radio" 
                            ng-model="assignCab.autoRoute" 
                            value="2" 
                            ng-change = 'applyAlgorithm(assignCab.tripType, shiftTime,assignCab.autoRoute)'> 
                            Auto Routing
              </label>
            </h5>
        </div>
    </div> 
</div>




<div class = 'row marginNeg10'>
    <div class = 'col-md-6 marginBottom15'>
      <div ng-init="assignCab.manualRouting=1" class="" />
          <h5>
            <label class="radio-inline">
                  <input type="radio" 
                  ng-model="assignCab.autoNodalRouting" 
                  value="3" 
                  ng-change = 'autoNodalRouting(assignCab.tripType, shiftTime,assignCab.autoNodalRouting)'> 
                  Auto Nodal Routing
            </label>
          </h5>
      </div>
    </div>
    
    <div class = 'col-md-6 marginBottom15'>
        <div ng-init="assignCab.manualRouting=1" class="" />
            <h5>
              <label class="radio-inline">
                    <input type="radio" 
                            ng-model="assignCab.rememberShiftRoute" 
                            value="4" 
                            ng-change = 'rememberShiftRoute(assignCab.tripType, shiftTime,assignCab.rememberShiftRoute)'> 
                            Remember Route Routing
              </label>
            </h5>
        </div>
    </div> 
</div>




</div>

 
            <div class = 'row marginNeg10'>
            </div>
        </fieldset>
    </div>
    <div class = "col-md-6 col-xs-6">           
       <input type="checkbox" 
              ng-model="assignCab.needRoutewise">
       <span>Assign a Cab by Route</span>
    </div>
    <div class = "col-md-6 col-xs-6" ng-show="rememberShiftRouteView">           
       <div style="border: 1px solid #5cb85c;">
               <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                        <button class="btn btn-default" ng-click="rememberShiftDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                  <input type="text" 
                         ng-model="assignCab.rememberDate" 
                         class="form-control" 
                         placeholder = "Remember Shift Route" 
                         datepicker-popup = '{{format}}'
                         is-open="datePicker.rememberDate" 
                         show-button-bar = false
                         show-weeks=false
                         datepicker-options = 'dateOptions'
                         readonly
                         name = 'rememberDate'>

           </div>
           </div>
    </div>
      
    <div class = "col-md-12 col-xs-12" ng-show = "assignCab.needRoutewise"> 
           <div>
           <label>Route Name</label>
               <select ng-model="assignCab.zone"
                       class="form-control" 
                       ng-options="allZoneData.routeName for allZoneData in allZonesData track by allZoneData.routeName"
                       ng-required = 'assignCab.needRoutewise'>
                 <option value="">-- Select Zone --</option>
              </select>
           </div>
       </div>
  </form>   
</div>
     <div class="modal-footer modalFooter createNewZone_modalFooter">  
     <button type="button" class="btn btn-warning btn-close floatRight noMoreClick" ng-click = "cancel()">Cancel</button>
           <button type="button" 
                   class="btn btn-success floatRight noMoreClick" 
                   ng-click = "assign(assignCab,shiftTime,facilityData)"
                   name = "assingbutton"
                   ng-disabled="form.assignCabForm.$invalid || facilityData.length == 0">Assign Cab</button>
           
     </div>
</div>