<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'">
</div>
<div class="loading">
</div>
<div class = "addNewMultipleLocationTemplate">
  <div class = "row">
    <div class="modal-header modal-header1 col-md-12">
      <div class = "row"> 
        <div class = "icon-map-marker mapMarkerIconCreateRequest col-md-1 floatLeft">
        </div>
        <div class = "modalHeading col-md-10 floatLeft">Auto Routing Configuration
        </div>
        <div class = "col-md-1 floatRight pointer">
          <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
        </div>  
      </div>  
    </div>        
  </div>
  <div class="modal-body addNewShiftTimeModal modalMainContent minHeight220">
     <div class = "row">

        <div class = "col-md-4">
        <label>Trip Type</label>
        <select name="mySelect" class="form-control"
                ng-options="option.requestTypes for option in requestTypesDataTS.availableOptions track by option.value"
                ng-model="requestTypesDataTS.selectedOption"
                ng-change="setTripTypeliveTracking(requestTypesDataTS.selectedOption)">
        </select>
        </div>
        <div class = "col-md-4 col-xs-12">
                  <div><label>Shift Times</label></div>                        
                <am-multiselect class="input-lg dynamicDaysInput"
                                    multiple="true"
                                    ms-selected ="{{shiftTimes.length}} Shift Times selected"
                                    ng-model="shiftTimes"
                                    ms-header="Select Shift Times"
                                    options="shiftTime as shiftTime.shiftTime for shiftTime in shiftsTimeData"
                                    change="addRoute(shiftTimes)">
                    </am-multiselect>
          </div>


     </div>
     <br>
    <div class = "row" ng-repeat="cutOffNotification in cutOffNotifications">
        <div class = "col-md-1"></div>
        <div class = "col-md-2">
        <label>Shift Time</label>
        <div>{{cutOffNotification.shiftTime}}</div></div>
     <!--     <div class = "col-md-2">
        <label>Routing</label>
            <select ng-model="cutOffNotification.routing" 
                    class="form-control"
                    ng-options="routeCutoff.name for routeCutoff in routeCutoffs"
                    ng-required="shiftTimeRequired"
                    required
                    ng-change = "setRoutingChange(cutOffNotification.routing)">
                    <option value="">No</option>
            </select>
        <br/>
        </div> -->
        <div class = "col-md-2">
        <label>Routing Type</label>
        <select name="mySelect" class="form-control"
                ng-options="routeType.text for routeType in routeTypes track by routeType.value"
                ng-model="cutOffNotification.routeType"
                ng-change="setRoutingTypeChange(cutOffNotification.routeType)">
                <option value="">None</option>
        </select>
        </div>
        <div class = "col-md-2 timePickerAlign">
           <timepicker ng-model="cutOffNotification.CutOffNoteTime" 
                          ng-show="cutOffNotification.routeType"
                          hour-step="hstep"
                          ng-change = "routingTimePicker(cutOffNotification.CutOffNoteTime)"
                          minute-step="mstep"
                          show-meridian="ismeridian"
                          readonly-input = 'true'
                          class = "timepicker2_empReq floatLeft">
           </timepicker>
             <br/>
         </div>
          <div class = "col-md-2">
        <label>Routing Close</label>
            <select ng-model="cutOffNotification.routingClose" 
                    class="form-control"
                    ng-options="routeTime for routeTime in routeAdhocTime"
                    ng-required="shiftTimeRequired"
                    required
                    ng-change = "setRoutingChange(cutOffNotification.routing)">
                    <option value="">No</option>
            </select>
        <br/>
        </div>
         <div class = "col-md-2 timePickerAlign" ng-if="cutOffNotification.routingClose">
           <timepicker ng-model="cutOffNotification.CutOffNoteRouting"
                          hour-step="hstep"
                          ng-change = setdropCancelCutOffTime(cutOffNotification.CutOffNoteRouting)
                          minute-step="mstep"
                          show-meridian="ismeridian"
                          readonly-input = 'true'
                          class = "timepicker2_empReq floatLeft">
           </timepicker>
             <br/>
         </div>
     </div>
      </div>      
    <div class="modal-footer modalFooter"> 
      <div class="">
        <button type="button" 
                class="btn btn-success btn-sm"
                ng-click = "saveCutoffNotifications(cutOffNotifications)" ng-disabled="desinationDisabledButton">Submit
        </button>
    
        <button type="button" 
                class="btn btn-danger btn-sm"
                ng-click = "cancel()">Cancel
        </button>
      </div>
    </div>
  </div>
