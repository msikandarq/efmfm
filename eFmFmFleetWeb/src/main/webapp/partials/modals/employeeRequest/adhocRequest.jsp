<div class = "empRequestDetailsTabContent row" ng-if="adhocTravelRequestDesk">
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.createNewRequestByAdminFormManila" class = "createNewRequestByAdminFormManila" novalidate>
                                    <div class = "row createNewRequest">
                                        <div class = "col-md-6  form-group">
                                            <span>Request Type</span>
                                            <select ng-model="newRequestByAdminAdhocManila.requestType" 
                                                    class="form-control"
                                                    ng-options="requestType.text for requestType in requestTypes track by requestType.value"
                                                    required>
                                                <option value="" class= "optionTextSize" >--SELECT REQUEST TYPE --</option>
                                            </select>                                            
                                        </div>


                                        <div class = "col-md-6  form-group">
                                            <span>Cab Type</span>
                                            <select ng-model="newRequestByAdminAdhocManila.cabType" 
                                                    class="form-control"
                                                    ng-options="cabType.text for cabType in cabTypes track by cabType.value"
                                                    required>
                                                <option value="" class= "optionTextSize" >--SELECT CAB TYPE --</option>
                                            </select>                                            
                                        </div>

                                        <div class = "col-md-6  form-group">
                                            <span>Trip Type</span>
                                            <select ng-model="newRequestByAdminAdhocManila.cabTripType" 
                                                    class="form-control"
                                                    ng-options="cabTripType.text for cabTripType in cabTripTypes track by cabTripType.value"
                                                    required>
                                                <option value="" class= "optionTextSize" >--SELECT PICKUP/DROP--</option>
                                            </select>                                            
                                        </div>

                                         <div class = "col-md-6  form-group">
                                            <span>Pickup Time</span>

                                                <timepicker ng-model="newRequestByAdminAdhocManila.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                                                              
                                        </div>

                                         <div class = "col-md-6 form-group">
                                            <span>Origin</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="origin('lg')"          required                                 
                                                        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.origin"
                                                   id = "location"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"

                                                   >
                                            </div>
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>Final Destination</span>
                                            <div class = "input-group calendarInput" > 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="finalDestination('lg')" required        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.finalDestination"
                                                   id = "finalDestination"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   >
                                            </div>
                                      
                                            <input type="button" value="Add More Destination" ng-click="showMultipleDestination()" ng-disabled="newRequestByAdminAdhocManila.cabType.value == 'REGULAR'" class="btn btn-default" name="">
                                            
                                        </div> 

                                          <div class = "col-md-6 form-group well" ng-show="destinationLocation">
                                            <span>Sub Destination1</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="subDestination1('lg')"        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.subDestination1"
                                                   id = "subDestination1"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   >
                                            </div>
                                        </div> 

                                        <div class = "col-md-6 form-group well" ng-show="destinationLocation">
                                            <span>Sub Destination 2</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="subDestination2('lg')"        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.subDestination2"
                                                   id = "subDestination2"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   >
                                            </div>
                                        </div> 

                                         <div class = "col-md-6 form-group well" ng-show="destinationLocation">
                                            <span>Sub Destination 3</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="subDestination3('lg')"        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.subDestination3"
                                                   id = "subDestination3"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   >
                                            </div>
                                        </div> 

                                         <div class = "col-md-6 form-group well" ng-show="destinationLocation">
                                            <span>Sub Destination 4</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="subDestination4('lg')"        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.subDestination4"
                                                   id = "subDestination4"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   >
                                            </div>
                                        </div> 

                                        <div class = "col-md-6 form-group well" ng-show="destinationLocation">
                                            <span>Sub Destination 5</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="subDestination5('lg')"        >
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdminAdhocManila.subDestination5"
                                                   id = "subDestination5"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   >
                                            </div>
                                        </div> 

                                        <div class = "col-md-6 form-group">
                                            <span>Passenger Id</span>
                                            <input ng-model="newRequestByAdminAdhocManila.id"  
                                                   type = "text"
                                                   class="form-control" 
                                                   placeholder = "Passenger Id"
                                                   name = "empid"
                                                   name = "id"
                                                   ng-disabled="newRequestByAdminAdhocManila.requestType.value == 'SELF'" 
                                                   >
                                        </div>

                                        <div class = "col-md-6 form-group">
                                            <span>Passenger Name</span>
                                            <input ng-model="newRequestByAdminAdhocManila.name" 
                                                   name = 'name'
                                                   type = "text" 
                                                   class="form-control" 
                                                   placeholder = "Passenger Name"
                                                   
                                                   name = "fName"
                                                   required
                                                   >
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>Passenger Last Name</span>
                                            <input ng-model="newRequestByAdminAdhocManila.lname" 
                                                   type = "text" 
                                                   class="form-control" 
                                                   placeholder = "Passenger Last Name"
                                                   
                                                   name = "lName"
                                                   required
                                                  >
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>Passenger Email</span>
                                            <input ng-model="newRequestByAdminAdhocManila.email" 
                                                   type = "email" 
                                                   class="form-control" 
                                                   placeholder = "Passenger Email"
                                                   name = "email"
                                                   required
                                                   >
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>Passenger Contact Number</span>
                                            <div class = "">          
                                                <input ng-model="newRequestByAdminAdhocManila.contact" 
                                                       type = "text" 
                                                       class="floatLeft form-control " 
                                                       placeholder = "Passenger Contact Number"
                                                       required
                                                       >
                                                
                                            </div>
                                        </div>   

                                            <div class = "col-md-6 form-group">
                                            <span>Booked By</span>
                                            <div class = "">          
                                                <input ng-model="newRequestByAdminAdhocManila.bookedBy" 
                                                       type = "text" 
                                                       class="floatLeft form-control " 
                                                       placeholder = "Booked By"
                                                       required
                                                    >
                                                
                                            </div>
                                        </div>

                                            <div class = "col-md-6 form-group">
                                            <span>Charged To</span>
                                            <div class = "">          
                                                <input ng-model="newRequestByAdminAdhocManila.chargedTo" 
                                                       type = "text" 
                                                       class="floatLeft form-control " 
                                                       placeholder = "Charged To"
                                                       required
                                                       >
                                                
                                            </div>
                                        </div>

                                              <div class = "col-md-6 form-group">
                                            <span>Account Name</span>
                                            <div class = "">          
                                                <input ng-model="newRequestByAdminAdhocManila.accountName" 
                                                       type = "text" 
                                                       class="floatLeft form-control " 
                                                       placeholder = "Account Name"
                                                       required
                                                     >
                                                
                                            </div>
                                        </div>
                                          <div class = "col-md-6 form-group">
                                           
                                            <div class = "">          
                                                   <span>Payment Type</span>
                                            <select ng-model="newRequestByAdminAdhocManila.paymentType" 
                                                    class="form-control"
                                                    ng-options="paymentType.text for paymentType in paymentTypes track by paymentType.value"
                                                    required>
                                                <option value="" class= "optionTextSize" >--SELECT PAYMENT TYPE --</option>
                                            </select>
                                                
                                            </div>
                                        </div>

                                          <div class = "col-md-6 form-group">
                                            <span>Remarks</span>
                                            <div class = "">          
                                                <input ng-model="newRequestByAdminAdhocManila.remarks" 
                                                       type = "text" 
                                                       class="floatLeft form-control" 
                                                       placeholder = "Remarks"
                                                       required
                                                       >

                                            </div>
                                        </div>

                                        <div class = "col-md-6 form-group">
                                            <span>Host Contact Numbers Comma Separated</span>
                                            <div class = "">
                                                <input ng-model="newRequestByAdminAdhocManila.contact2" 
                                                       type = "text" 
                                                       class="floatLeft form-control contact_createRequest" 
                                                       placeholder = "Eg.919811172193,919999999999"
                                                       name = "hostPhone"
                                                       required
                                                       >                                                
                                            </div>
                                        </div>                                      
                                        <div class = "col-md-6 form-group">
                                            <span>Gender</span>
                                            <select ng-model="newRequestByAdminAdhocManila.gender" 
                                                    class="floatLeft form-control areaCode_createRequest"
                                                    ng-options="gender.text for gender in genders track by gender.value"
                                                    ng-change = "setGender(newRequestByAdminAdhocManila.gender)"
                                                    required
                                                    >
                                                <option value="">-- Select Gender --</option>
                                            </select>
                                        </div>
                                        <!-- <div class = "col-md-6 form-group">
                                            <span>Pick/Drop Location</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="openMap('lg')"                                                        
                                                        ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST">
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdmin.location"
                                                   id = "location"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   ng-required="requestFor == 'GUEST'"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST">
                                            </div>
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>SMS Triggered Location</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" 
                                                        ng-click="openMap2('lg')"                                                        
                                                        ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST">
                                                    <i class = "icon-map-marker mapMarkerIcon"></i></button></span> 
                                            <input ng-model="newRequestByAdmin.location2"
                                                   id = "location2"
                                                   type = "text" 
                                                   class="noPointer form-control" 
                                                   placeholder = "Location"   
                                                   ng-required="requestFor == 'GUEST'"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST">
                                            </div>
                                        </div>  -->
                                        <div class = "col-md-6 form-group">
                                            <span>Start Request Date</span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input ng-model = "newRequestByAdminAdhocManila.fromDate"
                                                       class="form-control" 
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
                                                       >                                                          
                                            </div>
                                        </div> 
                                        <div class = "col-md-6  form-group">
                                            <span>End Request Date</span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequestByAdminAdhocManila.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "newRequestByAdminAdhocManila.fromDate"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       >                                                          </div>
                                        </div> 

                                           <div class = "col-md-6  form-group">
                                            <span>Duration In Hours</span>

                                                 <input ng-model="newRequestByAdminAdhocManila.durationInHours" 
                                                       type = "number" 
                                                       class="floatLeft form-control contact_createRequest" 
                                                       placeholder = "Hours"
                                                       name = "durationInHours"
                                                       required
                                                       >   
                                                                              
                                        </div>
                                 <!--        <div class = "col-md-6 form-group">
                                            <span> Shift Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">                                                
                                                <input type="radio" 
                                                       class = "floatLeft"
                                                       ng-model="shiftTime"
                                                       ng-disabled = "isRadioDisable()"
                                                       value="preDefineShiftTime" 
                                                       ng-change = "selectShiftTimeRadio(shiftTime, 'ADHOC')"
                                                       required>
                                                <select ng-model="newRequestByAdmin.createNewtripTime"
                                                        ng-options="tripTime for tripTime in tripTimes"
                                                        class = "floatLeft selectNewTripTime"  
                                                        ng-disabled = "isShiftTimeDisable"
                                                        ng-required = '!isShiftTimeDisable'>
                                                    <option value="">Select Shift Time</option>
                                                </select>
                                               </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12">
                                                <input type="radio" 
                                                       ng-model="shiftTime" 
                                                       class = "timepickerRadioButton floatLeft"
                                                       value="ShiftTimeCustom" 
                                                       ng-disabled = "isRadioDisable()"
                                                       ng-change = "selectShiftTimeRadio2(shiftTime, 'ADHOC')"
                                                       required>
                                                <timepicker ng-model="newRequestByAdmin.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                            </label> </div>                                           
                                        </div>  -->
                                        <div class = "col-md-4 col-md-offset-4">
                                            <input type = "button" 
                                                   id="createNewRequestByAdminGuest"
                                                   class = "btn btn-success creatNewReqButton"
                                                   value = "Create New Request" 
                                                   ng-click = "createNewRequestByAdminGuest(newRequestByAdminAdhocManila)"
                                                   ng-disabled = "form.createNewRequestByAdminFormManila.$invalid"

                                                   >
                                        </div> 
                                    </div>
                                </form>
                            </div>
                            <div class = "col-md-4"> </div>
            </div>