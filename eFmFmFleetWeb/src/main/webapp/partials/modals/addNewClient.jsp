<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/18/2015  Saima Aziz      Initial Creation
05/18/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "addNewShiftModalTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft" ng-if = "isClient">Add New Client</div>
               <div class = "modalHeading col-md-10 floatLeft" ng-if = "isBranch">Add New Branch for <strong>{{newClient.branchName}}</strong></div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "addNewClient" name = "addNewClient">
           
            <fieldset class = 'fieldSetTravelDesk'>
                 <legend class = 'clientSettingLegend' ng-if = "isClient">Client Settings</legend>
                 <legend class = 'clientSettingLegend' ng-if = "isBranch">Branch Settings</legend>
                 <div class = "userInfo_adminForm row">
<!--
                        <div class ='col-md-4'>
                            <label>Client Code</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.branchName" 
                                   placeholder = "Client Code"
                                   required>
                        </div>
-->
                        <div class ='col-md-4'>
                            <label ng-if = "isClient">Client Code</label>
                            <label ng-if = "isBranch">Branch Code</label>
                            <br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.branchCode" 
                                   placeholder = "Enter Code"
                                   required
                                   ng-readonly = "isBranch">
                        </div>

                        <div class ='col-md-4'>
                            <label ng-if = "isClient">Client Name</label>
                            <label ng-if = "isBranch">Branch Name</label>
                            <br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.branchName"
                                   placeholder = "Enter Name"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Email Id</label><br />
                            <input type = "email" class = "form-control" ng-model = "newClient.emailId" placeholder = "Email Id"
                                   name = "emailId"
                                   ng-class = "{error: addNewClient.emailId.$invalid && !addNewClient.emailId.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Branch Location</label><br />
                            <div class = "input-group calendarInput">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" ng-click="openMap('lg')">
                                        <i class = "icon-map-marker mapMarkerIcon"></i>
                                    </button>
                                </span>
                               <input type = "text" 
                                      class = "form-control" 
                                      ng-model = "newClient.location" 
                                      placeholder = "Branch Location" 
                                      required
                                      readonly>  
                                <input type = "text" class = "form-control hidden" ng-model = "newClient.cords" placeholder = "Branch Location">  
                         </div>
                            
                        </div>
                        <div class ='col-md-4'>
                            <label >State</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.state" 
                                   placeholder = "State Name"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Client URL</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.branchUri" 
                                   placeholder = "Client URL"
                                   required>
                        </div>  
                        <div class ='col-md-12'>
                            <label >Client Description</label><br />
                            <textarea type = "text" 
                                      class = "form-control" 
                                      ng-model = "newClient.description" 
                                      placeholder = "Client Description"
                                      required></textarea>
                        </div>
                 </div>  
           </fieldset>
            <fieldset class = 'fieldSetTravelDesk marginTop20'>
                <legend class = 'applicationSettingLegend'>Application Settings</legend>
                 <div class = "userInfo_adminForm row">
                        <div class ='col-md-4'>
                            <label>Escort Needed</label><br />
                            <select ng-model="newClient.escortNeeded" 
                                class ="form-control"
                                ng-options="escort.text for escort in escortNeededArray track by escort.value"
                                ng-change = "setEscortNeeded(newClient.escortNeeded)">
    					</select>
                        </div>
                        <div class ='col-md-4'>
                            <label>Manager Approval</label><br />
                            <select ng-model="newClient.managerApproval" 
                                class ="form-control"
                                ng-options="managerApproval.text for managerApproval in managerApprovalArray track by managerApproval.value"
                                ng-change = "setManagerApproval(newClient.managerApproval)">
    					</select>
                        </div>
                        <div class ='col-md-4'>
                            <label>Pick/Drop</label><br />
                            <select ng-model="newClient.pickDrop" 
                                class ="form-control"
                                ng-options="pickDrop for pickDrop in pickDropArray"
                                ng-change = "setNewClient(newClient.pickDrop)">
    					</select>
                        </div>
                     <div class ='col-md-4'>
                            <label >Number of Administrator required</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.numberOfAdminReqquired" 
                                   placeholder = "Number of Administrator required"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Password reset period for Admin</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.passwordResetPeriodForAdmin" 
                                   placeholder = "Password reset period for Admin"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Two factor authentication required</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.twoWayFactorAuth" 
                                   placeholder = "Two factor authentication required"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Number of attempts to enter wrong password</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.wrongPasswordAttempts" 
                                   placeholder = "Number of attempts to enter wrong password"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >Session Timeout(mins)</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.sessionTimeout" 
                                   placeholder = "Session Timeout(mins)"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label >ADHOC Request Time Picker</label><br />
                            <select ng-model="newClient.adhocTimer"
                               class="form-control" 
                               ng-options="adhoc for adhoc in adhocTimerArray"
                               ng-change = "setAdhocTimer(adhoc)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label >Drop Prior Time</label><br />
                            <input class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.dropPriorTime" 
                                   placeholder="Drop Prior Time"
                                   name = "dropPriorTime"
                                   required>   
                        </div>
                        <div class ='col-md-4'>
                            <label >Pickup Prior Time</label><br />
                            <input class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.pickupPriorTime" 
                                   placeholder="Pickup Prior Time"
                                   name = "pickupPriorTime"
                                   required> 
                        </div>
                     
                     
                     
                     
                        <div class ='col-md-4'>
                            <label>ETA Messages Trigger Time(min)</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.etaSMS" 
                                   placeholder = "ETA Messages Trigger Time(min)"
                                   name = "etaSMS"
                                   ng-class = "{error: addNewClient.etaSMS.$invalid && !addNewClient.etaSMS.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>Reached Message for Geofence Area(m)</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.geoFenceArea" 
                                   placeholder = "Reached Message for Geofence Area(meter)"
                                   name = "geoFenceArea"
                                   ng-class = "{error: addNewClient.geoFenceArea.$invalid && !addNewClient.geoFenceArea.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>Delay Message Time(min)</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.delayMessage" 
                                   placeholder = "Delay Message Time(min)"
                                   name = "delayMessage"
                                   ng-class = "{error: addNewClient.delayMessage.$invalid && !addNewClient.delayMessage.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>Employee Waiting Time(min)</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.employeeWaitingTime" 
                                   placeholder = "Employee Waiting Time(min)"
                                   name = "employeeWaitingTime"
                                   ng-class = "{error: addNewClient.employeeWaitingTime.$invalid && !addNewClient.employeeWaitingTime.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label> Maximum Speed Limit(kmph)</label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.maxSpeedArray" 
                                   placeholder = "Maximum speed in limit(kmph)"
                                   name = "maxSpeedArray"
                                   ng-class = "{error: addNewClient.maxSpeedArray.$invalid && !addNewClient.maxSpeedArray.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label> Driver Image</label><br />                                                                    
                            <select ng-model="newClient.driverImage"
                               class="appSettingsDropDowns" 
                               ng-options="driverImage for driverImage in driverImages"
                               ng-change = "setDriverImage(driverImage)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label> Driver Mobile Number</label><br />                                                                    
                            <select ng-model="newClient.driverMobileNumber"
                               class="appSettingsDropDowns" 
                               ng-options="driverMobileNumber for driverMobileNumber in driverMobileNumbers"
                               ng-change = "setDriverMobileNumber(driverMobileNumber)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label> Driver Name</label><br />                                                                    
                            <select ng-model="newClient.driverName"
                               class="appSettingsDropDowns" 
                               ng-options="driverName for driverName in driverNames"
                               ng-change = "setDriverName(driverName)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label> Trigger Message after 13 hours to </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin11to15Numbers"
                                   ng-model = "newClient.after13HrSMSContact" 
                                   placeholder = "Enter Mobile Number"
                                   name = "after13HrSMSContact"
                                   ng-class = "{error: addNewClient.after13HrSMSContact.$invalid && !addNewClient.after13HrSMSContact.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label> Trigger Message after 14 hours to  </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin11to15Numbers"
                                   ng-model = "newClient.after14HrSMSContact" 
                                   placeholder = "Enter Mobile Number"
                                   name = "after14HrSMSContact"
                                   ng-class = "{error: addNewClient.after14HrSMSContact.$invalid && !addNewClient.after14HrSMSContact.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label> Driver Auto Checked-Out Time(hours)  </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.driverCheckedout" 
                                   placeholder = "Driver Auto Checked-Out Time(hours)"
                                   name = "driverCheckedout"
                                   ng-class = "{error: addNewClient.driverCheckedout.$invalid && !addNewClient.driverCheckedout.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label> Profile Pictures  </label><br />                                                                                       <select ng-model="newClient.profilePic"
                               class="appSettingsDropDowns" 
                               ng-options="profilePic for profilePic in profilePics"
                               ng-change = "setProfilePic(newClient.profilePicverName)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label> Auto call and sms disabling </label><br />                                                                             
                            <select ng-model="newClient.autoCallSmsDisable"
                               class="appSettingsDropDowns" 
                               ng-options="autoCallSmsDisable for autoCallSmsDisable in autoCallSmsDisables"
                               ng-change = "setAutoCallSmsDisable(newClient.autoCallSmsDisable)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label> Auto Trip Start Geofence Area(m)  </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.autoTripStartGF" 
                                   placeholder = "Auto Trip Start Gefence Area(meter)"
                                   name = "autoTripStartGF"
                                   ng-class = "{error: addNewClient.autoTripStartGF.$invalid && !addNewClient.autoTripStartGF.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label> Auto Trip End Geofence Area(m)  </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.autoTripEndGF" 
                                   placeholder = "Auto Trip End Gefence Area(meter)"
                                   name = "autoTripEndGF"
                                   ng-class = "{error: addNewClient.autoTripEndGF.$invalid && !addNewClient.autoTripEndGF.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Max Travel Time Per Employee(min)  </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.maxTravelTime" 
                                   placeholder = "Enter Travel Time"
                                   name = "maxTravelTime"
                                   ng-class = "{error: addNewClient.maxTravelTime.$invalid && !addNewClient.maxTravelTime.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Max Radial Distance Per Employee(km)  </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.maxRadialDistance" 
                                   placeholder = "Enter Radial Distance"
                                   name = "maxRadialDistance"
                                   ng-class = "{error: addNewClient.maxRadialDistance.$invalid && !addNewClient.maxRadialDistance.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Max Route Length(km) </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.maxRouteLength" 
                                   placeholder = "Enter Route Length"
                                   name = "maxRouteLength"
                                   ng-class = "{error: addNewClient.maxRouteLength.$invalid && !addNewClient.maxRouteLength.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Enable Auto Clustering </label><br />
                            <select ng-model="newClient.autoClustring"
                                    class="form-control" 
                                    ng-options="clustring for clustring in applicationSettingsArrays.clustrings"
                                    ng-change = "setClustring(autoClustring)"
                                    required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label>  Max Route Deviation(km) </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.maxRoutedeviation" 
                                   placeholder = "Enter Route Deviation"
                                   name = "maxRoutedeviation"
                                   ng-class = "{error: addNewClient.maxRoutedeviation.$invalid && !addNewClient.maxRoutedeviation.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Cluster Size </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin1to3Numbers"
                                   ng-model = "newClient.clusterSize" 
                                   placeholder = "Enter Cluster Size"
                                   name = "clusterSize"
                                   ng-class = "{error: addNewClient.clusterSize.$invalid && !addNewClient.clusterSize.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Transport Number For Message </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin11to15Numbers"
                                   ng-model = "newClient.transportContactNumber" 
                                   placeholder = "Transport Contact Number"
                                   name = "transportContactNumber"
                                   ng-class = "{error: addNewClient.transportContactNumber.$invalid && !addNewClient.transportContactNumber.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Emergency Contact Number </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-pattern = "regexMin11to15Numbers"
                                   ng-model = "newClient.emrgContactNumber" 
                                   placeholder = "Enter Mobile Number"
                                   name = "emrgContactNumber"
                                   ng-class = "{error: addNewClient.emrgContactNumber.$invalid && !addNewClient.emrgContactNumber.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>  Transport Feedback Email Id </label><br />
                            <input type = "email" 
                                   class = "form-control" 
                                   ng-model = "newClient.transportFeedbackEmailId" 
                                   placeholder = "Transport Feedback Email Id"
                                   name = "transportFeedbackEmailId"
                                   ng-class = "{error: addNewClient.transportFeedbackEmailId.$invalid && !addNewClient.transportFeedbackEmailId.$pristine}">
                        </div>
                        <div class ='col-md-4'>
                            <label>  GeoCodeForOfficeGeoFence </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.GeoCodeForOfficeGeoFence" 
                                   placeholder = "Enter GeoCode For Office GeoFence"
                                   name = "GeoCodeForOfficeGeoFence"
                                   ng-class = "{error: addNewClient.GeoCodeForOfficeGeoFence.$invalid && !addNewClient.GeoCodeForOfficeGeoFence.$pristine}"
                                   required>
                        </div>
                     
                     
                        <div class ='col-md-4'>
                            <label>  Shift time difference pickup to drop (hr) </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.shiftTimeDiffPicktoDrop" 
                                   placeholder = "Enter Shift time difference pickup to drop"
                                   name = "shiftTimeDiffPicktoDrop"
                                   ng-class = "{error: addNewClient.shiftTimeDiffPicktoDrop.$invalid && !addNewClient.shiftTimeDiffPicktoDrop.$pristine}"
                                   required>
                        </div>
                     
                        <div class ='col-md-4'>
                            <label>  Add distance in each trip to recover the geo fence region (km) </label><br />
                            <input type = "text" 
                                   class = "form-control" 
                                   ng-model = "newClient.addDistanceRecoverGeoFence" 
                                   placeholder = "Enter Add distance in each trip to recover the geo fence region (km)"
                                   name = "addDistanceRecoverGeoFence"
                                   ng-class = "{error: addNewClient.addDistanceRecoverGeoFence.$invalid && !addNewClient.addDistanceRecoverGeoFence.$pristine}"
                                   required>
                        </div>
                        <div class ='col-md-4'>
                            <label>Last Passwords can not be your current password </label><br />                                                                             
                            <select ng-model="newClient.lastPasswordNotCurrent"
                               class="appSettingsDropDowns" 
                               ng-options="lastPasswordNotCurrent for lastPasswordNotCurrent in lastPasswordNotCurrents"
                               ng-change = "setLastPasswordNotCurrent(newClient.lastPasswordNotCurrent)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label>Employee second normal Pickup request </label><br />                                                                             
                            <select ng-model="newClient.empSecondNormalPickup"
                               class="appSettingsDropDowns" 
                               ng-options="empSecondNormalPickup for empSecondNormalPickup in empSecondNormalPickups"
                               ng-change = "setEmpSecondNormalPickup(newClient.empSecondNormalPickup)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label>Employee second normal DROP request </label><br />                                                                             
                            <select ng-model="newClient.empSecondNormalDrop"
                               class="appSettingsDropDowns" 
                               ng-options="empSecondNormalDrop for empSecondNormalDrop in empSecondNormalDrops"
                               ng-change = "setEmpSecondNormalDrop(newClient.empSecondNormalDrop)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label>Auto Drop Roster</label><br />                                                                             
                            <select ng-model="newClient.autoDropRosters"
                               class="appSettingsDropDowns" 
                               ng-options="autoDropRoster for autoDropRoster in autoDropRosters"
                               ng-change = "setAutoDropRoster(newClient.autoDropRoster)"
                               required>
                            </select>  
                        </div>
                        <div class ='col-md-4'>
                            <label>Panic Alert Needed</label><br />                                                                             
                            <select ng-model="newClient.panicAlertNeeded"
                               class="appSettingsDropDowns" 
                               ng-options="panicAlertNeeded for panicAlertNeeded in panicAlertNeededs"
                               ng-change = "setPanicAlertNeeded(newClient.panicAlertNeeded)"
                               required>
                            </select>  
                        </div>
                     
                     
                </div>
                
           </fieldset>
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
	<button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
            ng-click = "saveNewClient(newClient)" 
            ng-disabled="addNewClient.$invalid">Save</button> 
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>