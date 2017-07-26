<!-- 
@date           07/05/2016
@Author         Saima Aziz
@Description    This page is accessible by the EMPLOYEES/USERS. Users can view, Edit, Cancel and Create Cab pick and drop requests.
@State          home.requestDetails 
@URL            /requestDetails 

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
07/05/2016  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div class = "empRequestDetailsTemplate container-fluid" ng-if = "iSRequestDetails">
    <div class = "row" ng-controller = "cabRequestCtrl">
        <div class = "col-md-12 col-xs-12 heading1">            
            <span class = "col-md-12 col-xs-12">Employee Requests</span> 
                      
            <div class = "col-md-12 col-xs-12 mainTabDiv_empRequestDetail">

            <!-- /*START OF WRAPPER1 = TODAY'S REQUEST */ -->
               <div class = "wrapper1">             
                
                <tabset class="tabset subTab_empRequestDetail">
                  <!-- FIRST TAB -->
                  <tab ng-click = "getTodayRequestDetails()">
                    <tab-heading>Booking Schedule</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                   </tr> 
                                </thead>
                                <tbody ng-show = "empRequestDetailsData.length==0">
                                    <tr>
                                        <td colspan = '6'>
                                            <div class = "noData">There are no Re-schedule Request</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsData.length>0">
		                           <tr ng-repeat = "request in empRequestDetailsData">
                                      <td class = "col-md-1">{{request.requestId}}</td>
		                              <td class = "col-md-2" ng-show = "!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}}</td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule">
                                          <input type = "button" 
                                                 class = "btn btn-info btn-xs floatLeft" 
                                                 value = "Change" 
                                                 ng-click = "adHoc()" 
                                                 ng-show = "!needAdHoc">
                                          
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs floatLeft cancelAdHocButton" 
                                                 value = "Cancel" 
                                                 ng-click = "cancelAdHoc()" 
                                                 ng-show = "needAdHoc">
                                      </td>
                                      <td class = "col-md-1">{{request.requestDate}}</td>
		                              <td class = "col-md-3">{{request.employeeAddress}}</td>
                                      <td class = "col-md-1">{{request.tripType}}</td>
		                            </tr>                                    
		                         </tbody>
                            </table>
                            

 						</div>
 				  </tab>
                  <!--SECOND - (A) TAB-->
                  <!-- This Tab Will be Seen By the Employee Only -->
                  <tab ng-click = "initialzeNewCustomTime()">
                    <tab-heading>Book a Cab</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.createNewRequestForm" class = "createNewRequestForm">
                                    <div class = "row createNewRequest">
                                        <div class = "col-md-6  form-group calenderRequest">
                                            <span>Start Request Date</span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.fromDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate()"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: createNewRequestForm.fromDate.$invalid && !createNewRequestForm.fromDate.$pristine}">                                                          </div>
                                        </div>
                                        <div class = "col-md-6  form-group calenderRequest">
                                            <span>End Request Date</span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequest.endDate"
                                                       placeholder = "End Request Date"
                                                       ng-change = "isFromDate()"
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
                                        <div class = "col-md-6 form-group">
                                            <span>Trip Type</span>
                                            <select ng-model="newRequest.tripType"
                                                    ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                                    ng-change = "setTripType(newRequest.createNewtripTime, newRequest.fromDate, newRequest.tripType)"
                                                    required>
                                                <option value="">-- Select Trip Type --</option>
                                            </select>
                                        </div>
                                        <div class = "col-md-6 form-group selectDiv2">
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
                                                <!-- <select ng-model="newRequest.createNewtripTime"
                                                        ng-options="tripTimes for tripTimes in tripTimes"
                                                        class = "floatLeft selectNewTripTime"                                                        
                                                        ng-disabled = "isShiftTimeDisable">
                                                    <option value="">-- Select Shift Time --</option>
                                                </select> -->
                                                 <select ng-model="newRequest.createNewtripTime"
                                                        ng-options="tripTime.shiftTime for tripTime in tripTimeData track by tripTime.shiftId"
                                                        class = "floatLeft selectNewTripTime"  
                                                        ng-disabled = "isShiftTimeDisable">
                                                        <option value="">-- Select Shift Time --</option>
                                                        </select>
                                               </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12" ng-if = "isadhocTimePickerForEmployee || isadhocTimePickerForEmployee == 'true'">
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
                                        </div>
                                        <div class = "col-md-4 col-md-offset-4">
                                            <input type = "button" 
                                                   class = "btn btn-success creatNewReqButton"
                                                   value = "Create New Request" 
                                                   ng-click = "createNewRequest(newRequest)"
                                                   ng-disabled = "createNewRequestForm.$invalid">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class = "col-md-4"> </div>
 						</div>
 				  </tab>

                  <!--THIRD TAB -->

                  <tab ng-click = "getRescheduleRequestDetails()">
                    <tab-heading>Reschedule Cab</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                      <th></th>
                                   </tr> 
                                </thead>
                                <tbody ng-show = "empRequestDetailsData.length==0">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Re-schedule Request</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsData.length>0">
		                           <tr ng-repeat = "request in empRequestDetailsData"
                                       ng-show = 'is24hrRequest'>
                                      <td class = "col-md-1">{{request.requestId}}</td>
		                              <td class = "col-md-2" 
                                          ng-show = "!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}}
                                       </td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule">
                                          <!-- <select ng-model="newTripTime" 
                                                  ng-options="tripTimes for tripTimes in tripTimes"
                                                  ng-change = "setNewTripTime(newTripTime, request)"
                                                  ng-show = "!needAdHoc"
                                                  class = "floatLeft"> -->
                                                  
                                                  <select ng-model="request.newTripTime"
                                                        ng-options="tripTime.shiftTime for tripTime in tripTimeData track by tripTime.shiftId"
                                                        class = "floatLeft selectNewTripTime"  
                                                        >
                                            <option value="">- Select Shift Time -</option>
                                          </select>
                                          <timepicker ng-model="adHoctime" 
                                                      ng-change="setNewTripTime(adHoctime, request)" 
                                                      hour-step="hstep" 
                                                      minute-step="mstep" 
                                                      show-meridian="ismeridian"
                                                      ng-if = "needAdHoc"
                                                      class = "floatLeft timepicker_empReq">
                                          </timepicker>
                                          <input type = "button" 
                                                 class = "btn btn-info btn-xs floatLeft" 
                                                 value = "AD HOC" 
                                                 ng-click = "adHoc()" 
                                                 ng-if = "needAdHoc"
                                                 ng-show="adhocTimePicker='adhoc'">
                                          
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs floatLeft cancelAdHocButton" 
                                                 value = "Cancel" 
                                                 ng-click = "cancelAdHoc()" 
                                                 ng-if = "needAdHoc">
                                      </td>
                                       
                                       <td class = "col-md-2" ng-show = "!request.needReshedule">{{request.requestDate}}</td>
                                       <td class = "col-md-2"  ng-show = "request.needReshedule">  {{request.requestDate}}                                         
                                           <!--  <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                       
                                                 <input ng-model = "fromDate"
                                                       class="form-control" 
                                                       placeholder = "Request Date"
                                                       ng-change = "isFromDate()"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly>                                                                                        
                                           </div> -->
                                       </td>
		                              <td class = "col-md-3">{{request.address}}</td>
                                      <td class = "col-md-1">{{request.tripType}}</td>
                                      <td class = "col-md-2 requestButtonsDiv">
                                          <input type = "button" 
                                                 class = "btn btn-warning btn-xs" 
                                                 value = "Re-Schedule"
                                                 ng-click = "reSheduleRequest(request)"
                                                 ng-show = "!request.needReshedule"
                                                 ng-disabled = "saveIsClicked">
                                          <input type = "button" 
                                                 class = "btn btn-success btn-xs" 
                                                 value = "Save"
                                                 ng-click = "saveRequest(request, fromDate, $index)"
                                                 ng-show = "request.needReshedule">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel Reschedule"
                                                 ng-click = "cancelReschedule(request, $index)"
                                                 ng-show = "request.needReshedule">
                                       </td>
		                            </tr>                                    
		                         </tbody>
                            </table>
                            

 						</div>
 					</tab>  
                    
                  <!--FOURTH TAB -->
                  <tab ng-click = "getCancelRequestDetails()">
                    <tab-heading>Cancel Cab</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                      <th></th>
                                   </tr> 
                                </thead>
                                <tbody ng-show = "empRequestDetailsData.length==0">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Requests for Cancel</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsData.length>0">
		                           <tr ng-repeat = "request in empRequestDetailsData" class = "request{{request.requestId}}">
                                      <td class = "col-md-1">{{request.requestId}}</td>
		                              <td class = "col-md-2">{{request.tripTime |date:'HH:mm:ss'}}</td>
                                      <td class = "col-md-1">{{request.requestDate}}</td>
		                              <td class = "col-md-3">{{request.address}}</td>
                                      <td class = "col-md-1">{{request.tripType}}</td>
                                      <td class = "col-md-3 requestButtonsDiv">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel"
                                                 ng-click = "deleteRequest(request)">
                                       </td>
		                            </tr>                                    
		                         </tbody>
                            </table>
                            

 						</div>
 					</tab>
 				</tabset>	
            </div>
        </div>
    </div>
</div>
</div>