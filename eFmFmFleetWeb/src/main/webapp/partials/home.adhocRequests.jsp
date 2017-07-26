<!--  
@date           07/05/2016
@Author         Saima Aziz
@Description    This page is only accessible by the ADMIN. Other users need to request the access right from ADMIN
@State          home.adhocRequests 
@URL            /adhocRequests  

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
07/05/2016  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div class = "empRequestDetailsTemplate container-fluid" ng-if = "isAdhocRequestActivec || isAdhocRequestActive == 'true'" ng-init="getAdhocFacilityDetails('adhocTab')">
    <div class = "row" ng-controller = "cabRequestCtrl">
        <div class = "col-md-12 col-xs-12 heading1">            
          <span class="col-md-8 col-xs-8 employeeRequestLabel">
            AD-HOC Request
          </span> 
          
              <span class="col-md-2 col-xs-2 selectFacilityDropdown" ng-show="facilityMultiDropdown && multiFacility == 'Y'">
                 <span class="singleFacilityStyle">
                </span>

                <span >
                               <am-multiselect class="input-lg"
                                                 multiple="true"
                                                 ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                                 ng-model="facilityData"
                                                 ms-header="All Facility"
                                                 options="facility.branchId as facility.name for facility in facilityDetails"
                                                 change="setFacilityDetails(facilityData)"
                                                 >
                               </am-multiselect>
                </span>
          </span>

          <span class="col-md-2 col-xs-2 singleFacilityStyle" ng-show="facilitySingleDropdown && multiFacility == 'Y'" >
          </span>
          <span class="col-md-2 col-xs-2 singleFacilityStyle" ng-show="multiFacility == 'N'" >
          </span>

          

          <span class="col-md-2 col-xs-2 getFacilityButton" ng-show="facilityMultiDropdown && multiFacility == 'Y'"> 
                 <input type="button" class="btn btn-success" value="Submit" name="" ng-click="getFacilityDetails(facilityData)">
          </span>
                      
            <div class = "col-md-12 col-xs-12 mainTabDiv_empRequestDetail">

            <!-- /*START OF WRAPPER1 = TODAY'S REQUEST */ -->
               <div class = "wrapper1">              
                
                <tabset class="tabset subTab_empRequestDetail">

                  <tab ng-click = "getTravelDesk(); getTabInfo(facilityData, 'selfRescheduleRequest');">
                    <tab-heading>Self Re-Schedule Request</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                                <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.value" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
                                </div>
                            <br>
                             <br>
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop Location</th>
                                      <th>Trip Type</th>
                                      <th>Action</th> 
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
                               <tr ng-repeat = "request in empRequestDetailsData | filter:search.value "
                                       ng-show = 'is24hrRequest'>
                                      <td class = "col-md-1">{{request.requestId}}</td>
                                  <td class = "col-md-2" 
                                          ng-show = "!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}}  
                                       </td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule">
                                          <select ng-model="newTripTime" 
                                                  ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataReschedule track by tripTimes.shiftId"
                                                  ng-change = "setNewTripTime(newTripTime, request)"
                                                  ng-show = "!needAdHoc"
                                                  class = "floatLeft">
                                                    <option value="">- Select Shift Time -</option>
                                          </select>
                                           <div ng-show = "adhocTimePicker == 'Yes'">
                                          <timepicker ng-model="adHoctime" 
                                                      ng-change="setNewTripTime(adHoctime, request)" 
                                                      hour-step="hstep" 
                                                      minute-step="mstep" 
                                                      show-meridian="ismeridian"
                                                      ng-if = "needAdHoc"
                                                      class = "floatLeft timepicker_empReq"
                                                      >
                                          </timepicker>
                                          </div>
                                          <input type = "button" 
                                                 class = "btn btn-info btn-xs floatLeft" 
                                                 value = "AD HOC" 
                                                 ng-click = "adHoc()"
                                                 ng-show = "adhocTimePicker == 'Yes'" 
                                                 ng-if = "!needAdHoc">
                                          
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs floatLeft cancelAdHocButton" 
                                                 value = "Cancel" 
                                                 ng-click = "cancelAdHoc()" 
                                                 ng-if = "needAdHoc">
                                      </td>
                                       
                                       <td class = "col-md-2" ng-show = "!request.needReshedule">{{request.requestDate}}</td>
                                       <td class = "col-md-2"  ng-show = "request.needReshedule">     {{request.requestDate}}                                  
                                            <!-- <div class = "input-group calendarInput">
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
                                                 ng-click = "saveRequest(request, fromDate, $index, newTripTime, adHoctime,'adhoc')"
                                                 ng-show = "request.needReshedule">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel Reschedule"
                                                 ng-click = "cancelReschedule(request, $index,'adhoc')"
                                                 ng-show = "request.needReshedule">
                                       </td>
                                </tr>                                    
                             </tbody>
                            </table>
                            

            </div>
          </tab>  

                  <!--SECOND - (B) TAB-->
                  <!-- This Tab Will be Seen By the ADMIN Only -->
                  <tab ng-click = "initialzeNewCustomTimeByAdmin(); getTabInfo(facilityData, 'createRequest');">
                    <tab-heading>Create Request</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                            <div class = "col-md-1"> </div>
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.createNewRequestByAdminForm" class = "createNewRequestByAdminForm">
                                    <div class = "row createNewRequest">
                                    
                                       <!--  <div class = "col-md-6" ng-show="managerReqCreateProcess == 'Yes'">
                                          <span>Project Employee Allocated Details</span>
                                          <div class = 'multiSelect_travelDesk'
                                               ng-dropdown-multiselect="" 
                                               options="projectDetails" 
                                               selected-model="project.allocatedDetails" 
                                               extra-settings="daysSettings"
                                               translation-texts="daysButtonLabel"></div>
                                        </div> -->

                                        <!-- <div class = "col-md-2 form-group" ng-show="requestWithProject == 'Yes'">
                                            <span>Project Name</span>
                                            <am-multiselect 
                                                      class="input-lg"
                                                      ng-model="projectName"
                                                      options="checkInEntity as checkInEntity.projectName for checkInEntity in listOfProjectIdData"
                                                      change = "setProjectName(projectName,listOfProjectIdData)">
                                            </am-multiselect>
                                        </div>

                                        <div class = "col-md-2 form-group" ng-show="requestWithProject == 'Yes'">
                                            <span>Project Id</span>
                                            <am-multiselect 
                                                      class="input-lg"
                                                      ng-model="projectName"
                                                      options="checkInEntity as checkInEntity.projectId for checkInEntity in listOfProjectIdData"
                                                      change = "setProjectName(projectName,listOfProjectIdData)">
                                            </am-multiselect>
                                        </div> -->
                                        
                                        <!-- <div class = "col-md-8 form-group" ng-show="requestWithProject == 'Yes'">
                                        </div>
                                        <span class="projectDetailsSpan"></span>  -->
                                        <div class="row zeroMargin">
                                        <div class = "col-md-3" ng-show="requestWithProject == 'Yes'" ng-click = "setProjectNameChangeAdhoc('projectName')">
                                    
                                        <span>Project Name</span>

                                                               <multi-select-dropdown-span-three   
                                                                      input-model="listOfProjectNameAdhoc"    
                                                                      output-model="userDataProjectNameAdhoc"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "sendSelectedUserDetailsAdhoc(userDataProjectNameAdhoc,'name')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-three>
                                    </div>


                                    <div class = "col-md-3" ng-show="requestWithProject == 'Yes'" ng-click ="setProjectNameChangeAdhoc('projectId')">
                                         <span>Project Id</span>
                                                               <multi-select-dropdown-span-three   
                                                                      input-model="listOfProjectIdAdhoc"    
                                                                      output-model="userDataProjectIdAdhoc"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "sendSelectedUserDetailsAdhoc(userDataProjectIdAdhoc,'id')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-three>

                                    </div>

                                    <div class = "col-md-6" ng-show="managerReqCreateProcess == 'Yes'">
                                         <span>Reporting Manager Employee Id</span>
                                                               <multi-select-dropdown-span-six  
                                                                      input-model="projectDetails"    
                                                                      output-model="employeeAllocatedDetailsAdhoc"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "setEmployeeAllocatedDetails(employeeAllocatedDetailsAdhoc,'id')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-six>

                                    </div>
                                      </div><br>                                                                                
                                    <div class="row zeroMargin">
                                        <div class = "col-md-6  form-group">
                                            <span>Request Type</span>
                                            <select ng-model="newRequestByAdmin.requestType" 
                                                    class="form-control"
                                                    ng-options="requestType.text for requestType in requestTypes track by requestType.value"
                                                    ng-change = "setRequestType(newRequestByAdmin.requestType)"
                                                    required>
                                                <option value="" class= "optionTextSize" >--SELECT REQUEST TYPE --</option>
                                            </select>                                            
                                        </div>
                                        
                                        <div class = "col-md-6 form-group" ng-show="multiFacility == 'Y'">
                                            <span>Select Facility</span>
                                            <select ng-model="facilityData"
                                                   class="form-control"                                            
                                                   ng-options="facility.branchId as facility.name for facility in facilityDetails"
                                                   ng-change = "getFacilityDetails(facilityData)"
                                                   >
                                              <option value="">Select Facility</option>
                                              </select>
                                        </div>

                                        <div class = "col-md-6 form-group" ng-show="multiFacility == 'N'">
                                            <span>Employee Id</span>
                                            <input ng-model="newRequestByAdmin.id"  
                                                   type = "text"
                                                   class="form-control" 
                                                   placeholder = "Employee Id"
                                                   name = "empid"
                                                   ng-maxlength="20"
                                                   maxlength="20"
                                                   ng-blur = "checkIDExist()"
                                                   ng-change = "idIsEntered()"
                                                   expect_special_char
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "id"
                                                   ng-disabled = "createRequestRole_ADMIN && !createRequestRole_EMPLOYEE && !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.id.$invalid && !form.createNewRequestByAdminForm.id.$pristine}">
                                        </div>

                                        </div>
                                        <div class="row zeroMargin">

                                        <div class = "col-md-6 form-group" ng-show="multiFacility == 'Y'">
                                            <span>Employee Id</span>
                                            <input ng-model="newRequestByAdmin.id"  
                                                   type = "text"
                                                   class="form-control" 
                                                   placeholder = "Employee Id"
                                                   name = "empid"
                                                   ng-maxlength="20"
                                                   maxlength="20"
                                                   ng-blur = "checkIDExist()"
                                                   ng-change = "idIsEntered()"
                                                   expect_special_char
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "id"
                                                   ng-disabled = "createRequestRole_ADMIN && !createRequestRole_EMPLOYEE && !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.id.$invalid && !form.createNewRequestByAdminForm.id.$pristine}">
                                        </div>

                                        <div class = "col-md-3 form-group" ng-show="multiFacility == 'Y'">
                                            <span>First Name</span>
                                            <input ng-model="newRequestByAdmin.name" 
                                                   type = "text" 
                                                   class="form-control" 
                                                   placeholder = "Employee First Name"
                                                   is-name-only-valid
                                                   ng-minlength="3"
                                                   ng-maxlength="20"
                                                   maxlength="20"
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "fName"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.fName.$invalid && !form.createNewRequestByAdminForm.fName.$pristine}">
                                        </div>

                                        <div class = "col-md-6 form-group" ng-show="multiFacility == 'N'">
                                            <span>First Name</span>
                                            <input ng-model="newRequestByAdmin.name" 
                                                   type = "text" 
                                                   class="form-control" 
                                                   placeholder = "Employee First Name"
                                                   is-name-only-valid
                                                   ng-minlength="3"
                                                   ng-maxlength="20"
                                                   maxlength="20"
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "fName"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.fName.$invalid && !form.createNewRequestByAdminForm.fName.$pristine}">
                                        </div>

                                        <div class = "col-md-3 form-group" ng-show="multiFacility == 'Y'">
                                            <span>Last Name</span>
                                            <input ng-model="newRequestByAdmin.lname" 
                                                   type = "text" 
                                                   class="form-control" 
                                                   placeholder = "Employee Last Name"
                                                   is-name-only-valid
                                                   ng-minlength="3"
                                                   ng-maxlength="20"
                                                   maxlength="20"
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "lName"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.lName.$invalid && !form.createNewRequestByAdminForm.lName.$pristine}">
                                        </div>

                                        <div class = "col-md-6 form-group" ng-show="multiFacility == 'N'">
                                            <span>Last Name</span>
                                            <input ng-model="newRequestByAdmin.lname" 
                                                   type = "text" 
                                                   class="form-control" 
                                                   placeholder = "Employee Last Name"
                                                   is-name-only-valid
                                                   ng-minlength="3"
                                                   ng-maxlength="20"
                                                   maxlength="20"
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "lName"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.lName.$invalid && !form.createNewRequestByAdminForm.lName.$pristine}">
                                        </div>
                                        </div>
                                        <div class="row zeroMargin">
                                        <div class = "col-md-6 form-group">
                                            <span>Email</span>
                                            <input ng-model="newRequestByAdmin.email" 
                                                   type = "email" 
                                                   class="form-control" 
                                                   placeholder = "Employee Email"
                                                   ng-required="requestFor == 'GUEST'"
                                                   name = "email"
                                                   ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                   ng-class = "{error: form.createNewRequestByAdminForm.email.$invalid && !form.createNewRequestByAdminForm.email.$pristine}">
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>Guest Contact Number With Country Code</span>
                                            <div class = "">          
                                                <input ng-model="newRequestByAdmin.contact" 
                                                       type = "text" 
                                                       is-number-with-comma-valid
                                                       ng-minlength = "6"
                                                       ng-maxlength = "18"
                                                       maxlength="18"
                                                       class="floatLeft form-control" 
                                                       placeholder = "eg.919999999999"
                                                       ng-required="requestFor == 'GUEST'"
                                                       name = "empNum"
                                                       ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                       ng-class = "{error: form.createNewRequestByAdminForm.empNum.$invalid && !form.createNewRequestByAdminForm.empNum.$pristine}">
                                                
                                            </div>
                                        </div> 
                                        </div>
                                        <div class="row zeroMargin">  
                                        <div class = "col-md-6 form-group">
                                            <span>Host Contact Numbers Comma Separated</span>
                                            <div class = "">
                                                <input ng-model="newRequestByAdmin.contact2" 
                                                       type = "text" 
                                                       ng-minlength = "6"
                                                       ng-maxlength = "37"
                                                       maxlength="37"
                                                       is-number-with-comma-valid
                                                       class="floatLeft form-control" 
                                                       placeholder = "Eg.919811172193,919999999999"
                                                       ng-required="requestFor == 'GUEST'"
                                                       name = "hostPhone"
                                                       ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST"
                                                       ng-class = "{error: form.createNewRequestByAdminForm.hostPhone.$invalid && !form.createNewRequestByAdminForm.hostPhone.$pristine}">                                                
                                            </div>
                                        </div>  

                                        

<!--                                         <div class = "col-md-6 form-group">
                                            <span>Trip Type</span>
                                            <select ng-model="newRequestByAdmin.tripType" 
                                                    ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                                    ng-change = "setTripType(newRequestByAdmin.createNewtripTime, newRequestByAdmin.fromDate, newRequestByAdmin.tripType)"
                                                    required>
                                                <option value="">-- Select Trip Type --</option>
                                            </select>
                                        </div>  -->      
                                                                         
                                        <div class = "col-md-6 form-group">
                                            <span>Gender</span>
                                            <select ng-model="newRequestByAdmin.gender" 
                                                    class="form-control"
                                                    ng-options="gender.text for gender in genders track by gender.value"
                                                    ng-change = "setGender(newRequestByAdmin.gender)"
                                                    ng-required="requestFor == 'GUEST'"
                                                    ng-disabled = "createRequestRole_ADMIN && createRequestRole_EMPLOYEE || !createRequestRole_GUEST">
                                                <option value="">-- Select Gender --</option>
                                            </select>
                                        </div>
                                                                                </div>
                                        <div class="row zeroMargin" ng-if = "requestFor == 'GUEST'">
                                        <div class = "col-md-5 form-group">
                                            <span>Route Names</span>
                                            <div class = "">
                                                    <select ng-model="newRequestByAdmin.routeName"
                                                            class="form-control" 
                                                            ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaId"
                                                            >
                                                    <option value="">Select Route Name</option>
                                                    </select>                                                
                                            </div>
                                        </div>
                                        <div class = "col-md-1 form-group mapLocationbtn">
                                            <span class = "input-group-addon" ng-click="addMapDestination($index, origin)"><div class = "icon-map-marker mapMarkerIcon pointer"  ng-class="{enableAddButton: destinationAddButtonFlg == 'true' , disableAddButton: destinationAddButtonFlg == 'false'}"></div></span>
                                        </div>
                                        <div class = "col-md-6 form-group mapIcanFix" ng-if = "requestFor == 'GUEST'">
                                            <span>Destination Request Method</span>
                                            <select ng-model="newRequestByAdmin.request" 
                                                    class="form-control"
                                                    ng-options="request.text for request in requestMethod track by request.value"
                                                    ng-change = "requestMethodChecker(newRequestByAdmin.request)"
                                                    required>
                                                <option value="">-- Select Request --</option>
                                            </select>
                                        </div>
                                        </div>
                                        <div ng-if = "singleRequestMethod || requestFor != 'GUEST'">
                                        <div class="row zeroMargin">
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
                                        </div>
                                        <div class = "col-md-6 form-group">
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
                                        </div>
                                        <div class="row zeroMargin">
                                   
                                        <div class = "col-md-6 form-group">
                                            <span>Start Request Date</span>
                                            <div class = "input-group calendarInput"> 
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input ng-model = "newRequestByAdmin.fromDate"
                                                       class="form-control" 
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDateAdmin()"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: form.createNewRequestForm.fromDate.$invalid && !form.createNewRequestForm.fromDate.$pristine}">                                                          
                                            </div>
                                        </div> 
                                        <div class = "col-md-6  form-group">
                                            <span>End Request Date</span>
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openEndDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "newRequestByAdmin.endDate"
                                                       placeholder = "End Request Date"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "newRequestByAdmin.fromDate"
                                                       is-open="datePicker.endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "endDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: form.createNewRequestForm.endDate.$invalid && !form.createNewRequestForm.endDate.$pristine}">
                                                             </div>
                                        </div>
                                        </div>
                                        <div class="row zeroMargin">
                                         <div class = "col-md-3 form-group">
                                            <span>Trip Type</span>
                                            <select ng-model="newRequestByAdmin.tripType" class="form-control"
                                                    ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                                    ng-change = "setTripType(newRequestByAdmin.createNewtripTime, newRequestByAdmin.fromDate, newRequestByAdmin.tripType)"
                                                    required>
                                                <option value="">-- Select Trip Type --</option>
                                            </select>
                                        </div> 
                                        <div class = "col-md-3 form-group">
                                            <span>Shift Type</span>
                                            <select ng-model="newRequestByAdmin.shiftType" class="form-control" 
                                                    ng-options="shiftType.value as shiftType.text for shiftType in shiftTypes"
                                                    required>
                                                <option value="">-- Select Shift Type --</option>
                                            </select>
                                        </div>

                                        <!-- </div>
                                        <div class="row zeroMargin"> -->
                                        
                                        <div class = "col-md-6 form-group" ng-if="tripTypeBoth && newRequestByAdmin.tripType">
                                            <!-- <span> </span><br> -->
                                            <div class = "row">
                                            <label class = "radioLabel floatLeft col-md-6 col-xs-12" ng-show="newRequestByAdmin.shiftType == 'NORMAL'">       Pickup Shift Time                                         
                                                                                                   
                                                <select ng-model="newRequestByAdmin.createNewPickuptripTime"
                                                        ng-options="tripTime.shiftTime for tripTime in pickupTripTimeData track by tripTime.shiftId"
                                                        class = "floatLeft selectNewTripTime form-control"  
                                                        >

                                                    <option value="">Select Shift Time</option>
                                                </select>
                                               </label>
                                             <label class = "radioLabel floatLeft col-md-6 col-xs-12" ng-show="newRequestByAdmin.shiftType == 'NORMAL'">                Drop Shift Time     
                                                <select ng-model="newRequestByAdmin.createNewDroptripTime"
                                                        ng-options="tripTime.shiftTime for tripTime in DropTripTimeData track by tripTime.shiftId"
                                                        class = "floatLeft selectNewTripTime form-control"  
                                                        >

                                                    <option value="">Select Shift Time</option>
                                                </select>
                                               </label>
                                            </div>                                           
                                        </div>
                                        <div class = "col-md-6 form-group" ng-if="!tripTypeBoth && newRequestByAdmin.tripType">
                                            <!-- <span> </span><br> -->
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12" ng-show="newRequestByAdmin.shiftType == 'NORMAL'">Shift Time                                                
                                                                                             
                                                <select ng-model="newRequestByAdmin.createNewtripTime"
                                                        ng-options="tripTime.shiftTime for tripTime in tripTimeData track by tripTime.shiftId"
                                                        class = "floatLeft selectNewTripTime form-control"  
                                                        >

                                                    <option value="">Select Shift Time</option>
                                                </select>
                                               </label>
                                            <label class = "col-md-6  col-xs-12 myProfileFormMargin text-right" ng-show="newRequestByAdmin.shiftType == 'ADHOC'">
                                                Shift Time
                                            </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12 timePickerMarginTop" ng-show="newRequestByAdmin.shiftType == 'ADHOC'">
                                        
                                                <timepicker ng-model="newRequestByAdmin.createNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                            </label> </div>                                           
                                        </div> 
                                        </div>
                                         <div class="row zeroMargin" ng-if="tripTypeBoth">
                                               <div class = "col-md-6 form-group">
                                            <!-- <span></span><br> -->
                                            <div class = "row">
                                              
                                            <label class = "col-md-6  col-xs-12 myProfileFormMargin text-right" ng-show="newRequestByAdmin.shiftType == 'ADHOC'">
                                               Pickup Shift Time 
                                            </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12 timePickerMarginTop" ng-show="newRequestByAdmin.shiftType == 'ADHOC'">
                                            
                                                <timepicker ng-model="newRequestByAdmin.createNewPickupAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                            </label> </div>                                           
                                        </div> 
                                        <div class = "col-md-6 form-group">
                                            <!-- <span></span><br> -->
                                            <div class = "row">
                                             
                                          <label class = "col-md-6  col-xs-12 myProfileFormMargin text-right" ng-show="newRequestByAdmin.shiftType == 'ADHOC'">
                                                Drop Shift Time
                                            </label>
                                            <label class = "radioLabel customNewTimePicker col-md-6  col-xs-12 timePickerMarginTop" ng-show="newRequestByAdmin.shiftType == 'ADHOC'">
                                                
                                                <timepicker ng-model="newRequestByAdmin.createDropNewAdHocTime" 
                                                          hour-step="hstep" 
                                                          minute-step="mstep" 
                                                          show-meridian="ismeridian" 
                                                          readonly-input = 'true'
                                                          class = "timepicker2_empReq floatLeft">
                                                </timepicker>
                                            </label> </div>                                           
                                        </div> 
                                        </div>
                                        <!-- <div class="row zeroMargin"> -->
                                    <!--     <div class = "col-md-6 form-group">
                                            <span>Route Names</span>
                                            <div class = "">
                                                    <select ng-model="newRequestByAdmin.routeName"
                                                            class="form-control" 
                                                            ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaId"
                                                            >
                                                    <option value="">Select Route Name</option>
                                                    </select>                                                
                                            </div>
                                        </div> -->  
                                        </div>  
                                        
                                            <span ng-if = "multipleRequestBtn && multipleRequestModal" 
                                            class="multipleText">
                                            <hr> * Press add multiple request for add request</span>
                                        <div ng-show="locationVisible != 'N'">
                                            <div class = "col-md-4">
                                            </div>
                                        
                                            <div class="btn-group">
<!--                                               <button type="button" 
                                                      class="btn btn-primary btn-sm" 
                                                      ng-click="addMultipleLocation()" ng-if = "multipleRequestBtn">Add Multiple Location</button> -->
                                              <button type="button" 
                                                      class="btn btn-primary btn-sm" 
                                                      ng-click="addMultipleLocationAdhoc()" ng-if = "multipleRequestBtn && multipleRequestModal">Add Multiple Request</button>
                                              <!-- <button type="button"
                                                      class="btn btn-warning btn-sm" 
                                                      ng-click = "previewLocation()">Preview Journey</button> -->
                                              <button type="button" 
                                                      class="btn btn-success btn-sm"
                                                      id = "createRequestSubmitButton"
                                                      ng-click = "createNewRequestByAdmin(newRequestByAdmin,userDataProjectNameAdhoc,employeeAllocatedDetailsAdhoc)"
                                                      ng-disabled = "form.createNewRequestByAdminForm.$invalid || submitFlag">Create New Request</button>
                                              <button type="button" 
                                                      class="btn btn-danger btn-sm"
                                                      ng-click = "resetRequestDetails(newRequest)">Reset</button>        
                                              </div>
                                        </div>

                                        <div ng-show="locationVisible == 'N'">
                                            <div class = "col-md-4">
                                            </div>
                                            <div class="btn-group">
                                            <button type="button" 
                                                      class="btn btn-primary btn-sm" 
                                                      ng-click="addMultipleLocationAdhoc()" ng-if = "multipleRequestBtn && multipleRequestModal">Add Multiple Request</button>
                                              <button type="button" 
                                                      class="btn btn-success btn-sm"
                                                      id = "createRequestSubmitButton"
                                                      ng-click = "createNewRequestByAdmin(newRequestByAdmin,userDataProjectNameAdhoc,employeeAllocatedDetailsAdhoc)"
                                                      ng-disabled = "form.createNewRequestByAdminForm.$invalid">Create New Request</button>
                                              <button type="button" 
                                                      class="btn btn-danger btn-sm"
                                                      ng-click = "resetRequestDetails(newRequest)">Reset</button>        
                                              </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class = "col-md-4"> </div>
            </div>
          </tab> 


          <!--SECOND - (B) TAB-->
                  <!-- This Tab Will be Seen By the ADMIN Only -->
                  <tab ng-click = "getAdhocTravelRequestDesk(); getTabInfo(facilityData, 'createRequestManila');" ng-show="branchCode == 'SBOManila'" >
                    <tab-heading>Create Request - Manila</tab-heading>
                        <div class = "empRequestDetailsTabContent row">
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.createNewRequestByAdminFormManila" class = "createNewRequestByAdminFormManila" novalidate>
                                    <div class = "row createNewRequest">
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
          </tab> 



        </tabset> 
            </div>
        </div>
    </div>
</div>
</div>