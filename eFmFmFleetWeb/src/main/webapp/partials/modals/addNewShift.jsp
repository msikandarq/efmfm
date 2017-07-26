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
<div class = "addNewShiftModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New Shift</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "addNewShift" name = "addNewShift">
         <div class = "userInfo_adminForm row">
                <div class ='col-md-6'>
                    <label>Trip Type</label><br>
                    <select ng-model="newShift.tripType"
                            class = 'form-control'
                            ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                            ng-change = "setTripType(newShift.tripType)"
                            required>
                        
                    </select>
                </div>
                <div class ='col-md-6'>
                    <label>Shift Buffer Time</label><br>
                    <select ng-model="newShift.bufferTime"
                            class = 'form-control'
                            ng-options="bufferTime for bufferTime in bufferTimes"
                            ng-change = "setbufferTime(newShift.bufferTime)"
                            required>
                       
                    </select>
                </div>
                <div class ='col-md-6'>
                    <label class = 'floatLeft'>Shift Time</label>
                    <timepicker ng-model="newShift.createNewAdHocTime" 
                              hour-step="hstep" 
                              minute-step="mstep" 
                              show-meridian="ismeridian" 
                              readonly-input = 'true'
                              class = "timePickerMarginShiftLeft floatLeft" required>
                    </timepicker>
                </div>
                <div class ='col-md-6' ng-show="cutOffTimeShow == true">
                    <label class = 'floatLeft'>Cancel Cut Off Time</label>
                    <timepicker ng-model="newShift.CancelCutOffTime" 
                              hour-step="hstep" 
                              minute-step="mstep" 
                              show-meridian="ismeridian" 
                              readonly-input = 'true'
                              class = "timePickerMarginShift floatLeft" required>
                    </timepicker>
                </div>

                <div class ='col-md-6' ng-show="cutOffTimeShow == true">
                    <label class = 'floatLeft'>Reschedule CutOffTime</label>
                    <timepicker ng-model="newShift.RescheduleCutOffTime" 
                              hour-step="hstep" 
                              minute-step="mstep" 
                              show-meridian="ismeridian" 
                              readonly-input = 'true' 
                              class = "timepicker2_empReq floatLeft" required>
                    </timepicker>
                </div>

                <div class ='col-md-6' ng-show="cutOffTimeShow == true">
                    <label class = 'floatLeft' >Cut Off Time</label>
                    <timepicker ng-model="newShift.cutOffTime" 
                              hour-step="hstep" 
                              minute-step="mstep" 
                              show-meridian="ismeridian" 
                              readonly-input = 'true'
                              class = "timePickerMarginCutOff floatLeft">
                    </timepicker>
                </div>
                 <div class ='col-md-6'>
                    <label class = 'floatLeft'>Ceiling Limit</label><br>
                    <select ng-model="newShift.ceilingType"
                            class = 'form-control'
                            ng-options="tripType.text for tripType in ceilingTypes track by tripType.value"
                            ng-change = "setceilingType(newShift.ceilingType)"
                            required>
                        
                    </select>
                </div>
                <div class ='col-md-3' ng-show="newShift.ceilingType.value == 'YES'">
                    <label class = 'floatLeft' >Ceiling Values</label>
                    <input type="number" 
                           class="form-control" 
                           name="remarks" 
                           ng-model="newShift.ceilingValues" 
                           min="1" 
                           placeholder="Enter Ceiling Values" 
                           ng-required="ceilingValuesFlg"
                           ng-class = "{error: addNewShift.remarks.$invalid && !addNewShift.remarks.$pristine}"
                           >
                    
                      <span class="hintItallic error" ng-show="addNewShift.remarks.$invalid && !addNewShift.remarks.$pristine">Kinly Enter early request greater than 0</span>    
                </div>
                 <div class ='col-md-3' ng-show="newShift.ceilingType.value == 'YES'">
                    <label class = 'floatLeft' >Awaited Passenger</label>
                    <input type="number" 
                           class="form-control" 
                           name="awaitedPassenger" 
                           ng-model="newShift.bufferCeilingNo" 
                           min="0" 
                           placeholder="Enter Awaited Passengers" 
                           ng-required="ceilingValuesFlg"
                           ng-class = "{error: addNewShift.awaitedPassenger.$invalid && !addNewShift.awaitedPassenger.$pristine}"
                           >
                    
                      <span class="hintItallic error" ng-show="addNewShift.awaitedPassenger.$invalid && !addNewShift.awaitedPassenger.$pristine">Kinly Enter early request greater than 0</span>    
                </div>

                
          <!-- <input type="button" value="demo" name="" ng-hide="true"> -->

         </div>  
         <div class="row">
              <div class ='col-md-6' ng-show="genderPreferenceFlg == 'Yes'">
                    <label class = 'floatLeft' >Gender Preference</label>
                    <select ng-model="newShift.genderPreference"
                            class = 'form-control'
                            ng-options="genderPreference.text for genderPreference in genderPreference"
                            ng-change = "setGenderPreference(newShift.genderPreference)"
                            required>
                    </select>
              </div>

               <div class ='col-md-6'>
                  <label class = 'floatLeft' >Shift Type</label>
                  <select ng-model="newShift.shiftType"
                          class = 'form-control'
                          ng-options="shiftType.text for shiftType in shiftTypesData"
                          ng-change = "setShiftTypes(newShift.shiftType)"
                          required>
                  </select>
              </div>

         </div>
         

       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
            ng-click = "saveNewShift(newShift)" 
            ng-disabled="addNewShift.$invalid">Save</button> 
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>