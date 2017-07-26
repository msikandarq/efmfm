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

<div class="empRequestDetailsTemplate container-fluid" ng-if="iSRequestDetails">
    <div class="row" ng-controller="cabRequestCtrl">
        <div class="col-md-12 col-xs-12 heading1">
          <span class="col-md-8 col-xs-8 employeeRequestLabel">
            Employee Requests
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


            <div class="col-md-12 col-xs-12 mainTabDiv_empRequestDetail"> 

                <!-- /*START OF WRAPPER1 = TODAY'S REQUEST */ -->
                <div class="wrapper1">

                    <tabset class="tabset subTab_empRequestDetail">

                        <!-- FIRST TAB -->
                        <tab ng-click="getTodayRequestDetails(facilityData); getTabInfo(facilityData, 'bookingSchedule');   ">
                            <tab-heading>Booking Schedule</tab-heading> 
                            <div ng-include="'partials/modals/employeeRequest/bookingScheduleNormalRequest.jsp'" ng-show="locationVisible == 'N'"></div>
                            <div ng-include="'partials/modals/employeeRequest/bookingScheduleMultipleRequest.jsp'" ng-show="locationVisible != 'N'"></div>
                            
                        </tab>
                        
                        <!--SECOND - (A) TAB-->
                        <!-- This Tab Will be Seen By the Employee Only -->
                        <tab ng-click="initialzeNewCustomTime(); getTabInfo(facilityData, 'bookACab');">
                            <tab-heading>Book a Cab</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/bookCab.jsp'"></div>
                        </tab>
                        
                        <!--THIRD TAB -->
                        <tab ng-click="getRescheduleRequestDetails(); getTabInfo(facilityData, 'rescheduleCab');">
                            <tab-heading>Reschedule Cab</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/rescheduleCabNormalRequest.jsp'" ng-show="locationVisible == 'N'"></div>
                            <div ng-include="'partials/modals/employeeRequest/rescheduleCabMultipleRequest.jsp'" ng-show="locationVisible != 'N'"></div>
                        </tab>

                        <!--FOURTH TAB -->
                        <tab ng-click="getCancelRequestDetails(); getTabInfo(facilityData, 'cancelCab');">
                            <tab-heading>Cancel Cab</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/cancelCabNormalRequest.jsp'" ng-show="locationVisible == 'N'"></div>
                            <div ng-include="'partials/modals/employeeRequest/cancelCabMultipleRequest.jsp'" ng-show="locationVisible != 'N'"></div>
                        </tab>

                        <!--FIFTH TAB -->
                        <tab ng-click="getAdhocTravelRequestDesk() ; getTabInfo(facilityData, 'adhocRequest');" ng-show="branchCode == 'SBOManila' && userRole == 'webuser'">
                            <tab-heading>Adhoc Request</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/adhocRequest.jsp'"></div>
                        </tab>

                        <!--SIXTH TAB -->
                        <tab ng-click="getBookingRequestDetails() ; getTabInfo(facilityData, 'bulkRequestDetails');">
                            <tab-heading>Bulk Request Details</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/bulkRequestDetails.jsp'"></div>
                        </tab>

                        <!--SEVENTH TAB -->
                        <tab ng-click="getPendingApprovalRequests(); getTabInfo(facilityData, 'pendingApprovalRequests');" ng-show="requestWithProject == 'Yes'">
                            <tab-heading>Pending Approval Requests</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/pendingApprovalRequests.jsp'"></div>
                        </tab>
                        
                         <!--Eighth TAB -->
                        <tab ng-click="getEmployeeDetails(); getTabInfo(facilityData, 'delegatedSPOC');" ng-show="managerReqCreateProcess == 'Yes'">
                            <tab-heading>Delegated SPOC</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/delegatedSPOC.jsp'"></div>
                        </tab>

                        <!--Ninth TAB -->
                        <tab ng-click="getDelegatedUserDetails(); getEmployeeDetails(); getTabInfo(facilityData, 'delegatedDetails');" ng-show="managerReqCreateProcess == 'Yes'">
                            <tab-heading>Delegated Details</tab-heading>
                            <div ng-include="'partials/modals/employeeRequest/delegatedUser.jsp'"></div>
                        </tab>
                    </tabset>
                </div>
            </div>
        </div>
    </div>
</div>
