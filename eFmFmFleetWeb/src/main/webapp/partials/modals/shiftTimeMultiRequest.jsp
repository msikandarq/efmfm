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
            <div class = "modalHeading col-md-10 floatLeft">Set Trip Type/Shift Times</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "createShiftTimesForm" name = "createShiftTimesForm">
         <div class = "userInfo_adminForm row">
                <div class ='col-md-6'>
                <!-- {{shiftTimeDetails.tripType}} -->
                    <label>Trip Type</label><br>
                        <select ng-model="shiftTimeDetails.tripType"
                                class="form-control" 
                                ng-options="tripType.value for tripType in tripTypes"
                                ng-change="setTripType(shiftTimeDetails.timeType,shiftTimeDetails.tripType)"
                                required 
                          >
                    <option value="">Trip Type
                    </option>
              </select>
                </div>

                <div class ='col-md-6'>
                <label>Time Type</label><br>
                <!-- {{shiftTimeDetails.timeType}} -->
                <select ng-model="shiftTimeDetails.timeType"
                        class="form-control" 
                        ng-options="timeType.value for timeType in timeTypes"
                        ng-change="setTimeType(shiftTimeDetails.timeType,shiftTimeDetails.tripType)"
                        required 
                      >
                <option value="">Time Type
                </option>
              </select>
              </div>

              <div class ='col-md-6' ng-show="pickupTripTimeShow">
                <label>Pickup Times</label><br>
                  <select ng-model="shiftTimeDetails.pickupTimeNomal"
                                      class="form-control" 
                                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeDataPickup" 
                                      required
                                      ng-change = "setPickupTimeAdhoc(shiftTimeDetails.pickupTimeNomal)"
                                      ng-required="pickupTimeNormalRequired"
                    >
                      <option value="">Pickup Times</option>
                  </select>
              </div>

              <div class ='col-md-6' ng-show="dropTripTimeShow">
                <label>Drop Times</label><br>
                  <select ng-model="shiftTimeDetails.dropTimeNormal"
                                      class="form-control" 
                                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeDataDrop" 
                                      required
                                      ng-change = "setShiftButton(shiftTimeDetails.dropTimeNormal)"
                                      ng-required="dropTimeNormalRequired"
                    >
                      <option value="">Drop Times</option>
                  </select>
              </div>

              <div class ='col-md-6' ng-show="pickupTripAdhocShow">
              <label>Pickup Times</label><br>
              <timepicker ng-model="shiftTimeDetails.pickupTimeAdhoc" 
                      hour-step="hstep" 
                      ng-change="setPickupTimeAdhoc(shiftTimeDetails.pickupTimeAdhoc)"
                      minute-step="mstep" 
                      show-meridian="ismeridian" 
                      readonly-input = 'true'
                      class = "timepicker2_empReq floatLeft"
                      ng-required="pickupTimeAdhocRequired"
                      >
              </timepicker>
              <!-- <input type="time" id="adhocTime" class="form-control"  name="input" ng-model="destinationPoint.adhocTimePickup"
                     placeholder="HH:mm:ss(Pickup)" required /> -->
              </div>
              <div class ='col-md-6' ng-show="dropTripAdhocShow">
              <label>Drop Times</label><br>
              <timepicker ng-model="shiftTimeDetails.dropTimeAdhoc" 
                      hour-step="hstep" 
                      ng-change="setDropTimeAdhoc(shiftTimeDetails.dropTimeAdhoc)"
                      minute-step="mstep" 
                      show-meridian="ismeridian" 
                      readonly-input = 'true'
                      class = "timepicker2_empReq floatLeft"
                      ng-required="dropTimeAdhocRequired"
                      >
              </timepicker>
              <!-- <input type="time" id="adhocTime" class="form-control"  name="input" ng-model="destinationPoint.adhocTimeDrop"
                     placeholder="HH:mm:ss(Drop)" required /> -->
              </div>
           

           </div>              

       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
            ng-click = "submitShiftTimeDetails(shiftTimeDetails)" 
            ng-disabled="createShiftTimesForm.$invalid">Submit</button> 
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>