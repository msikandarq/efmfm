<div class = "empRequestDetailsTabContent row">
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.createNewRequestForm" class = "createNewRequestForm">
                                    <div class = "row createNewRequest">

                                    <div class = "col-md-3" ng-show="requestWithProject == 'Yes'" ng-click = "setProjectNameChange('projectName')">
                                    
                                        <span>Project Name</span>

                                                               <multi-select-dropdown-span-three   
                                                                      input-model="listOfProjectName"    
                                                                      output-model="userDataProjectName"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "sendSelectedUserDetails(userDataProjectName,'name')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-three>
                                    </div>


                                    <div class = "col-md-3" ng-show="requestWithProject == 'Yes'" ng-click ="setProjectNameChange('projectId')">
                                         <span>Project Id</span>
                                                               <multi-select-dropdown-span-three   
                                                                      input-model="listOfProjectId"    
                                                                      output-model="userDataProjectId"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "sendSelectedUserDetails(userDataProjectId,'id')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-three>

                                    </div>

                                    <div class = "col-md-6" ng-show="managerReqCreateProcess == 'Yes'">
                                         <span>Project Employee Allocated Details</span>
                                                               <multi-select-dropdown-span-six  
                                                                      input-model="projectDetails"    
                                                                      output-model="employeeAllocatedDetails"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "setEmployeeAllocatedDetails(employeeAllocatedDetails,'id')"
                                                                      >
                                                               </multi-select-dropdown-span-six>

                                    </div>

                                    
                                       </div>

                                       <div class = "row createNewRequest">
                                  
                                    <!-- <div class = "col-md-6  form-group calenderRequest">
                                            <span>Project Id</span>
                                            <am-multiselect 
                                                      class="input-lg"
                                                      ng-model="projectName"
                                                      options="checkInEntity as checkInEntity.projectId for checkInEntity in listOfProjectIdData"
                                                      change = "setProjectName(projectName,listOfProjectIdData)">
                                            </am-multiselect>
                                        </div> --> 

                                    <!-- Date - custdate Normal-->
                                    
                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays  == 'custdate' && normalView">
                                            <span>Start Request Date </span>
                                            <div class = "input-group calendarInput" >
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDateNew"
                                                       max-date = "lastDayCust"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">                                                          </div>
                                        </div>
                                        
                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays  == 'custdate' && normalView">
                                            <span>End Request Date </span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       max-date = "lastDayCust"
                                                       min-date = "newRequest.fromDate"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.endDate.$invalid && !createNewRequestForm.endDate.$pristine}">                                                          </div>
                                        </div>



                    <!-- Date - custdate next month-->


                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays  == 'custdate' && NextMonthNormal">
                                            <span>Start Request Date </span>
                                            <div class = "input-group calendarInput" >
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDateNew"
                                                       max-date = "setmaxDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">                                                          </div>
                                        </div>
                                        
                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays  == 'custdate' && NextMonthNormal">
                                            <span>End Request Date </span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       max-date = "setmaxDate"
                                                       min-date = "newRequest.fromDate"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.endDate.$invalid && !createNewRequestForm.endDate.$pristine}">                                                          </div>
                                        </div>


                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays  == 'custdate' && NextMonthOccurrence">
                                            <span>Start Request Date </span>
                                            <div class = "input-group calendarInput" >
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDateNew"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">                                                          </div>
                                        </div>
                                        
                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays  == 'custdate' && NextMonthOccurrence">
                                            <span>End Request Date</span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "newRequest.fromDate"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.endDate.$invalid && !createNewRequestForm.endDate.$pristine}">                                                          </div>
                                        </div>

  
                    <!-- Date - everymonthlastdate -->

                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays == 'everymonthlastdate'">
                                            <span>Start Request Date </span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>         
                                                                                 
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       max-date = "lastDay"
                                                       min-date = "minDateValid"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">                                                          </div>
                                        </div>
                                        
                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Date' && monthOrDays == 'everymonthlastdate'">
                                            <span>End Request Date </span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       max-date = "lastDay"
                                                       min-date = "newRequest.fromDate"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.endDate.$invalid && !createNewRequestForm.endDate.$pristine}">                                                          </div>
                                        </div>

                <!-- No Of Days -->
                
                                        <div class="panel panel-danger marginBottom0" ng-show="datePickerFlg == 'Days'" >
                                             <div class="panel-heading" ng-show="dataEnable">* As per transport policy user can raise the request only  on {{dynamicRequest}}. </div>
                                             <div class="panel-heading" >* As per transport policy user can raise the request only for next {{requestCutOffNoOfDays}} days</div>
                                        </div>
                                        
                                        <!-- <div class="panel panel-danger" ng-show="datePickerFlg == 'Date'" style="margin-bottom: 0px;" >
                                             <div class="panel-heading" ng-show="dataEnable">* As per transport policy user can raise the request only  on {{dynamicRequest}}. </div>
                                             <div class="panel-heading" >* As per transport policy user can raise the request only for next {{requestCutOffNoOfDays}} days</div>
                                             <br/>
                                        </div> -->

                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Days' && !datePickerEnable" >
                                            <span>Start Request Date</span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-disabled="true" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>         
                                                                                 
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       date-disabled="disabled(date, mode)"
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">                
                                                       </div>
                                        </div>

                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Days' && datePickerEnable">
                                            <span>Start Request Date</span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>         
                                                                               
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       min-date = "currentDatenew"
                                                       max-date = "endDateVal"
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">             
                                                      </div>
                                        </div>
                                        
                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Days' && !datePickerEnable">
                                            <span>End Request Date</span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-disabled="true" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "newRequest.fromDate"
                                                       max-date = "endDateVal"
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.endDate.$invalid && !createNewRequestForm.endDate.$pristine}">                                                          </div>
                                        </div>

                                        <div class = "col-md-6  form-group calenderRequest" ng-show="datePickerFlg == 'Days' && datePickerEnable">
                                            <span>End Request Date</span>

                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "newRequest.fromDate"
                                                       max-date = "endDateVal"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.endDate.$invalid && !createNewRequestForm.endDate.$pristine}">                                                          </div>
                                        </div>

                                      <div class = "col-md-6  form-group shiftTimeAlignment" ng-show="multiFacility == 'Y'">
                                      <span>Select Facility</span>
                                        <select ng-model="facilityData"
                                                class="form-control"                                            
                                                ng-options="facility.branchId as facility.name for facility in facilityDetails"
                                                ng-change = "getFacilityDetails(facilityData)"
                                         >
                                        <option value="">Select Facility</option>
                                        </select>
                                      </div>


                                        <div class = "col-md-6 form-group shiftTimeAlignment">
                                            <span>Trip Type</span>
                                            <select ng-model="newRequest.tripType" class="form-control" 
                                                    ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                                    ng-change = "setShiftType(newRequest)"
                                                    required>
                                                <option value="">-- Select Trip Type --</option>
                                            </select>
                                        </div>

                                        <div class = "col-md-6 form-group" ng-show="adhocShiftTimeShow">
                                            <span>Shift Type</span>
                                            <select ng-model="newRequest.shiftType" class="form-control" 
                                                    ng-options="shiftType.text for shiftType in shiftTypes track by shiftType.value"
                                                    ng-change = "setShiftType(newRequest)"
                                                    ng-disabled="shiftTypeDisabled"
                                                    required>
                                                <option value="">-- Select Shift Type --</option>
                                            </select>
                                        </div>

                                        <div class = "col-md-6 form-group" ng-show="normalShiftTimeShow">
                                            <span>Shift Type</span>
                                            <select ng-model="newRequest.shiftType" class="form-control" 
                                                    ng-options="shiftType.text for shiftType in shiftTypesNormal track by shiftType.value"
                                                    ng-change = "setShiftType(newRequest)"
                                                    ng-disabled="shiftTypeDisabled"
                                                    required>
                                                <option value="">-- Select Shift Type --</option>
                                            </select>
                                        </div>


                                        <div class = "col-md-3 form-group" ng-show="pickupNormalTimeShow">
                                        <span> Pickup Time</span><br>
                                                <select ng-model="newRequest.createNewPickupTime" 
                                                        ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataForPickup track by tripTimes.shiftId"
                                                        class = "floatLeft selectNewTripTime  form-control"  
                                                        ng-required="pickupNormalTimeShow"
                                                        ng-change="setTimeValues(newRequest,createNewPickupAdHocTime,createNewDropAdHocTime)"
                                                        >
                                                    <option value="">-Shift Time-</option>
                                          </select>
                                          </div>

                                        <div class = "col-md-3 form-group" ng-show="dropNormalTimeShow">
                                          <span> Drop Time</span><br>
                                                  <select ng-model="newRequest.createNewDroptripTime" 
                                                          ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataForDrop track by tripTimes.shiftId"
                                                          class = "floatLeft selectNewTripTime  form-control"  
                                                          ng-required="dropNormalTimeShow"
                                                          ng-change="setTimeValues(newRequest,createNewPickupAdHocTime,createNewDropAdHocTime)">
                                                      <option value="">-Shift Time-</option>
                                            </select>
                                        </div>

                                        <div class = "col-md-3 form-group achocTimePicker" ng-show="pickupAdhocTimeShow">
                                        <span> Pickup ADHOC Time</span><br>
                                                <timepicker
                                                ng-model="createNewPickupAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          ng-required="pickupAdhocTimeShow"
                                                          ng-change="setTimeValues(newRequest,createNewPickupAdHocTime,createNewDropAdHocTime)"
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                          </div>

                                        <div class = "col-md-3 form-group achocTimePicker" ng-show="dropAdhocTimeShow">
                                          <span> Drop ADHOC Time</span><br>
                                                  <timepicker 
                                                              ng-model="createNewDropAdHocTime" 
                                                              hour-step="hstep" 
                                                              minute-step="mstep" 
                                                              show-meridian="ismeridian" 
                                                              readonly-input = 'true'
                                                              ng-required="dropAdhocTimeShow"
                                                              ng-change="setTimeValues(newRequest,createNewPickupAdHocTime,createNewDropAdHocTime)"
                                                              class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                        </div> 

                                        

                                        <!-- <div class = "col-md-6 form-group selectDiv2">
                                            <span> Shift Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">                                                
                                                <input type="radio" 
                                                       class = "floatLeft"
                                                       ng-model="shiftTime"
                                                       ng-disabled = "isRadioDisable()"
                                                       value="preDefineShiftTime" 
                                                       ng-change = "selectShiftTimeRadio(shiftTime, 'empRequestDetails')"
                                                       required>
                                               
                                                 <select ng-model="newRequest.createNewtripTime"
                                                        ng-options="tripTime.shiftTime for tripTime in tripTimeData track by tripTime.shiftId"
                                                        class = "floatLeft selectNewTripTime"  
                                                        ng-disabled = "isShiftTimeDisable">
                                                        <option value="">-- Select Shift Time --</option>
                                                        </select>
                                               </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12" ng-if = "adhocTimePicker='Yes'">
                                                <input type="radio" 
                                                       ng-model="shiftTime" 
                                                       class = "timepickerRadioButton floatLeft"
                                                       value="ShiftTimeCustom" 
                                                       ng-disabled = "isRadioDisable()"
                                                       ng-change = "selectShiftTimeRadio2(shiftTime, 'empRequestDetails')"
                                                       required>
                                                <timepicker type="radio" 
                                                ng-model="newRequest.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                            </label> 
                                            </div>                                           
                                        </div> -->

                                          <!-- <div class = "col-md-6 form-group shiftTimeAlignment" ng-if="!bothDropdown">
                                            <span> Shift Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12 width100per">                                                
                                                <input type="radio" ng-show = "adhocTimePicker == 'Yes'"
                                                       class = "floatLeft" 
                                                       ng-model="shiftTime"
                                                       ng-disabled = "isRadioDisable()"
                                                       value="preDefineShiftTime" 
                                                       ng-change = "selectShiftTimeRadio(shiftTime, 'empRequestDetails')"
                                                       ng-required="{{adhocChanged}}">

                                                <select ng-model="newRequest.createNewtripTime" 
                                                        ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataReschedule track by tripTimes.shiftId"
                                                        class = "floatLeft selectNewTripTime  form-control"  
                                                        ng-required="{{adhocChanged}}"
                                                        ng-change="onloadDisableButton()">
                                                    <option value="">- Select Shift Time -</option>
                                          </select>
                                               </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12" ng-show = "adhocTimePicker == 'Yes'">
                                                <input type="radio" 
                                                       ng-model="shiftTime" 
                                                       class = "timepickerRadioButton floatLeft"
                                                       value="ShiftTimeCustom" 
                                                       ng-disabled = "isRadioDisable()"
                                                       ng-change = "selectShiftTimeRadio2(shiftTime, 'empRequestDetails')"
                                                       required>
                                                <timepicker ng-model="newRequest.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft"
                                                          ng-required="true"
                                                          >
                                                </timepicker>
                                            </label> </div>                                           
                                        </div> 
                                        <div class = "col-md-4 form-group shiftTimeAlignment" ng-if="bothDropdown">
                                            <span> Pickup Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12 width100per">                                                
                                                <input type="radio" ng-show = "adhocTimePicker == 'Yes'"
                                                       class = "floatLeft" 
                                                       ng-model="shiftTime"
                                                       ng-disabled = "isRadioDisable()"
                                                       value="preDefineShiftTime" 
                                                       ng-change = "selectShiftTimeRadio(shiftTime, 'empRequestDetails')"
                                                       ng-required="{{adhocChanged}}">

                                                <select ng-model="newRequest.createNewPickupTime" 
                                                        ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataForPickup track by tripTimes.shiftId"
                                                        class = "floatLeft selectNewTripTime  form-control"  
                                                        ng-required="{{adhocChanged}}"
                                                        ng-change="onloadDisableButton()">
                                                    <option value="">-Shift Time-</option>
                                          </select>
                                               </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12" ng-show = "adhocTimePicker == 'Yes'">
                                                <input type="radio" 
                                                       ng-model="shiftTime" 
                                                       class = "timepickerRadioButton floatLeft"
                                                       value="ShiftTimeCustom" 
                                                       ng-disabled = "isRadioDisable()"
                                                       ng-change = "selectShiftTimeRadio2(shiftTime, 'empRequestDetails')"
                                                       required>
                                                <timepicker ng-model="newRequest.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft"
                                                          ng-required="true"
                                                          >
                                                </timepicker>
                                            </label> </div>                                           
                                        </div>
                                        <div class = "col-md-4 form-group shiftTimeAlignment" ng-if="bothDropdown">
                                            <span> Drop Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12 width100per">                                                
                                                <input type="radio" ng-show = "adhocTimePicker == 'Yes'"
                                                       class = "floatLeft" 
                                                       ng-model="shiftTime"
                                                       ng-disabled = "isRadioDisable()"
                                                       value="preDefineShiftTime" 
                                                       ng-change = "selectShiftTimeRadio(shiftTime, 'empRequestDetails')"
                                                       ng-required="{{adhocChanged}}">

                                                <select ng-model="newRequest.createNewDroptripTime" 
                                                        ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataForDrop track by tripTimes.shiftId"
                                                        class = "floatLeft selectNewTripTime  form-control"  
                                                        ng-required="{{adhocChanged}}"
                                                        ng-change="onloadDisableButton()">
                                                    <option value="">-Shift Time-</option>
                                          </select>
                                               </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12" ng-show = "adhocTimePicker == 'Yes'">
                                                <input type="radio" 
                                                       ng-model="shiftTime" 
                                                       class = "timepickerRadioButton floatLeft"
                                                       value="ShiftTimeCustom" 
                                                       ng-disabled = "isRadioDisable()"
                                                       ng-change = "selectShiftTimeRadio2(shiftTime, 'empRequestDetails')"
                                                       required>
                                                <timepicker ng-model="newRequest.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft"
                                                          ng-required="true"
                                                          >
                                                </timepicker>
                                            </label> </div>                                           
                                        </div> -->

                                        <div ng-show="locationVisible != 'N'">
                                        <br/>
                                            <div class = "col-md-2">
                                            </div>
                                            <div class="btn-group" style="position: absolute; bottom: -9px; left: 340px;">
                                              <button type="button" 
                                                      class="btn btn-primary btn-sm" 
                                                      ng-disabled="!multipleLocationButtonDisabled" 
                                                      ng-click="addMultipleLocation()">Add Multiple Location</button>
                                              <button type="button"
                                                      class="btn btn-warning btn-sm" 
                                                      ng-click = "previewLocation()">Preview Journey</button>
                                              <button type="button" 
                                                      class="btn btn-success btn-sm"
                                                      id = "createRequestSubmitButton"
                                                      ng-click = "createNewRequest(newRequest,createNewPickupAdHocTime,createNewDropAdHocTime, userDataProjectName,employeeAllocatedDetails)" 
                                                      ng-disabled = "createNewRequestForm">Create New Request</button>
                                              <button type="button" 
                                                      class="btn btn-danger btn-sm"
                                                      ng-click = "resetRequestDetails(newRequest)">Reset</button>        
                                              </div>
                                        </div>

                                        <div ng-show="locationVisible == 'N'">
                                        <br/>
                                           <div class="createRequestNormal">

                                             <div class = "col-md-4">
                                             </div>
                                            <div class="btn-group bookACanButton" style="">

                                              <button type="button" 
                                                      class="btn btn-success btn-sm"
                                                      id = "createRequestSubmitButton"
                                                      ng-click = "createNewRequest(newRequest,createNewPickupAdHocTime,createNewDropAdHocTime, userDataProjectName,employeeAllocatedDetails)" 
                                                      ng-disabled = "createNewRequestForm">Create New Request</button>
                                              <button type="button" 
                                                      class="btn btn-danger btn-sm"
                                                      ng-click = "resetRequestDetails(newRequest)">Reset</button>        
                                              </div>
                                           </div>
                                            
                                        </div>
                                        

                                        <!-- <div class = "col-md-3">
                                            <input type="button" 
                                                   value="Add Multiple Location" 
                                                   class="btn btn-warning"
                                                   ng-disabled="!multipleLocationButtonDisabled" 
                                                   name=""
                                                   ng-click="addMultipleLocation()">
                                        </div>
                                        <div class = "col-md-3">
                                            <input type="button" ng-click="previewLocation()" value="Preview Location" class="btn btn-primary" name="">
                                        </div>
                                         <div class = "col-md-4">
                                            <input type="button" 
                                                   value="Create New Request"
                                                   id = "createRequestSubmitButton"  
                                                   class="btn btn-success"  
                                                   ng-click = "createNewRequest(newRequest)"
                                                   ng-disabled = "createNewRequestForm" 
                                                   name="">
                                        </div> -->
                                        <!-- <div class = "col-md-4 col-md-offset-4">
                                            <input type = "button"
                                                   id = "createRequestSubmitButton"  
                                                   class = "btn btn-success creatNewReqButton"
                                                   value = "Create New Request" 
                                                   ng-click = "createNewRequest(newRequest)"
                                                   ng-disabled = "createNewRequestForm">
                                        </div> -->
                                    </div>
                                </form>
                            </div>
                            <div class = "col-md-4"> </div>
            </div>