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
<div class = "newEscortFormModalTemplate">  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Employee Detail</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "editEmployeeDetail" >          
           <div class = "col-md-6 col-xs-12 hidden">
               <div>
               <label>Employee Id</label>
                   <input type="text" 
                      ng-model="user.employeeId" 
                      name = 'Id'>
               </div>
           </div>
           
           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Employee Name</label>
                   <input type="text" 
                      ng-model="user.name" 
                      class="form-control" 
                      placeholder = "Employee Name"
                      name = 'name'
                      required
                      is-name-only-valid
                      ng-minlength="3"
                      ng-maxlength="50"
                      maxlength="50"
                      ng-class = "{error: editEmployeeDetail.name.$invalid && !editEmployeeDetail.name.$pristine}" >
               </div>
           </div>

           <div class = "col-md-6 col-xs-12">
               <div>
               <label>User Name</label>
                   <input type="text" 
                      ng-model="user.userName" 
                      class="form-control" 
                      placeholder = "User Name"
                      name = 'userName'
                      required
                      ng-minlength="3"
                      ng-maxlength="50"
                      maxlength="50"
                      ng-class = "{error: editEmployeeDetail.userName.$invalid && !editEmployeeDetail.userName.$pristine}" >
               </div>
           </div>
                     
           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Employee Designation</label> 
                   <input type="text" 
                      ng-model="user.employeeDesignation" 
                      class="form-control" 
                      placeholder = "Employee Designation"
                      name = 'employeeDesignation'
                      ng-minlength="2"
                      ng-maxlength="30"
                      maxlength="30"
                      expect_special_char
                      required
                      ng-class = "{error: editEmployeeDetail.employeeDesignation.$invalid && !editEmployeeDetail.employeeDesignation.$pristine}" >
               </div>
           </div>

           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Employee Department</label> 
                   <input type="text" 
                      ng-model="user.employeeDepartment" 
                      class="form-control" 
                      placeholder = "Employee Department"
                      name = 'employeeDepartment'
                      ng-minlength="0"
                      ng-maxlength="30"
                      maxlength="30"
                      expect_special_char
                      required
                      ng-class = "{error: editEmployeeDetail.employeeDepartment.$invalid && !editEmployeeDetail.employeeDepartment.$pristine}" >
               </div>
           </div>
             
           <div class = "col-md-6 col-xs-12">
               <div>   
               <label>Employee Mobile Number</label>
                    <input type="text" 
                           ng-model="user.number" 
                           class="form-control" 
                           placeholder = "Employee Mobile Number with contry code"
                           name = 'mobileNumber'
                           ng-pattern = "IntegerNumber"
                           required
                           is-number-only-valid
                          ng-minlength="6"
                          ng-maxlength="18"
                          maxlength="18"
                           ng-class = "{error: editEmployeeDetail.mobileNumber.$invalid && !editEmployeeDetail.mobileNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editEmployeeDetail.mobileNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>
           
           <div class = "col-md-6 col-xs-12">
               <div>   
               <label>Host Mobile Number</label>
                    <input type="text" 
                           ng-model="user.hostMobileNumber" 
                           class="form-control" 
                           placeholder = "Host Mobile Number"
                           name = 'hostMobileNumber'
                           ng-class = "{error: editEmployeeDetail.hostMobileNumber.$invalid && !editEmployeeDetail.hostMobileNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editEmployeeDetail.mobileNumber.$error.maxlength">* In-valid Host Mobile Number</span>
           </div>
          
            <div class = "col-md-6 col-xs-12">
               <label for = "escortMobileNumber">Days Off</label>
               <div class = 'multiSelect_travelDesk'
                    ng-dropdown-multiselect="" 
                    options="daysData" 
                    selected-model="update.daysModel" 
                    extra-settings="daysSettings"
                    translation-texts="daysButtonLabel"></div>
           </div>

           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Employee Email</label> 
                   <input type="email" 
                      ng-model="user.email" 
                      class="form-control" 
                      placeholder = "Employee email"
                      name = 'email'
                      required
                      ng-class = "{error: editEmployeeDetail.email.$invalid && !editEmployeeDetail.email.$pristine}">
               </div>
           </div>

            <div class = "col-md-6 col-xs-12">
               <div>
               <label>Employee Gender</label>
                   <select ng-model="user.empGender"
                           class="form-control" 
                           ng-options="gender for gender in employeeGen"
                           ng-change = "setEmployeegender(user.empGender)"
                           required>
                    </select>
               </div>
           </div>
           <div ng-show = "user.empGender == 'Female'"  class = "col-md-6 col-xs-12">
               <div>
               <label>Pregnant</label>
                   <select ng-model="user.pragnentLady"
                           class="form-control" 
                           ng-options="employeeFemale for employeeFemale in pregnantEmp track by employeeFemale"
                           ng-change = "setEmployeegender(user.pragnentLady)"
                           ng-required="user.empGender == 'Female'" >
                            <option value="">Select Type</option>
                    </select>
               </div>
           </div>
          <div class = "col-md-6 col-xs-12">
               <div>
               <label>Differently Abled</label>
                   <select ng-model="user.physicallyChallenge"
                           class="form-control" 
                           ng-options="pc for pc in physicalChallenge"
                           ng-change = "setPhysicallyChallenge(user.physicallyChallenge)"
                           required>
                    </select>
               </div>
           </div>

            
           
            <div class = "col-md-6 col-xs-12">
               <div>
               <label>Route Name</label>
                   <select ng-model="user.routeName"
                           class="form-control" 
                           ng-options="allRouteData.routeName for allRouteData in allRouteData track by allRouteData.routeId"
                           ng-change = "setAreaSelected(user.routeName)"
                           required>
                     <option value="">Select Route Name</option>
                    </select>
               </div>
           </div>
            <div class = "col-md-6 col-xs-12">
               <div>
               <label>Area Name</label>
                   <select ng-model="user.areaName"
                           class="form-control" 
                           ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaId"
                           ng-change = "setAreaSelected(user.areaName)"
                           required>
                     <option value="">Select Area Name</option>
                    </select>
               </div>
           </div>
            <div class = "col-md-6 col-xs-12">
               <div>
               <label>Nodal Points</label>
                   <select ng-model="user.nodalPointName"
                           class="form-control" 
                           ng-options="nodalPointdata.nodelPointName for nodalPointdata in nodalPointsdata track by nodalPointdata.nodalPointId"
                           required>
                     <option value="">Select Route Name</option>
                    </select>
               </div>
           </div>
           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Employee Address</label>
                   <input type="text" 
                      ng-model="user.empaddress" 
                      class="form-control" 
                      placeholder = "Employee Address"
                      name = 'empaddress'
                      ng-minlength="10"
                      ng-maxlength="200"
                      maxlength="200"
                      minlength="10"
                      required
                      ng-class = "{error: editEmployeeDetail.empaddress.$invalid && !editEmployeeDetail.empaddress.$pristine}" >
               </div>
           </div>

          <!-- <div class = "col-md-6 col-xs-12">
               <div>
               <label>Drop Route Name</label>
                   <select ng-model="user.routeNameDrop"
                           class="form-control" 
                           ng-options="allRouteData.routeName for allRouteData in allRouteData track by allRouteData.routeId"
                           ng-change = "setAreaSelected(user.routeName)"
                           required>
                     <option value="">Select Route Name</option>
                    </select>
               </div>
           </div>
            <div class = "col-md-6 col-xs-12">
               <div>
               <label>Drop Area Name</label>
                   <select ng-model="user.areaNameDrop"
                           class="form-control" 
                           ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaId"
                           ng-change = "setAreaSelected(user.areaName)"
                           required>
                     <option value="">Select Area Name</option>
                    </select>
               </div>
           </div> -->
         <div class = "col-md-6 col-xs-12">
               <div>
               <label>Is VIP</label>
                   <select ng-model="user.isVIP"
                           class="form-control" 
                           ng-options="vip for vip in isVip"
                           ng-change = "setInjuredPeople(user.injuredPeople)"
                           required>
                    </select>
               </div>
           </div>
          <div class = "col-md-6 col-xs-12">
               <div>
               <label>Injured</label>
                   <select ng-model="user.injuredPeople"
                           class="form-control" 
                           ng-options="ip for ip in injuredPeople"
                           ng-change = "setInjuredPeople(user.injuredPeople)"
                           required>
                    </select>
               </div>
           </div>
            <div class = "col-md-6 col-xs-12" ng-show="multiFacility == 'Y'">
                  <label>Select Facility</label>
                  <select ng-model="user.facilityData"
                         class="form-control"                                            
                         ng-options="facility.branchId as facility.name for facility in facilityDetails"
                         ng-change = "getFacilityDetails(user.selectedFacility)"
                         >
                    <option value="">Select Facility</option>
                    </select>
           </div>

           <div class = "col-md-6 col-xs-12">
               <label>Trip Type</label>
                   <select ng-model="user.tripType"
                           class="form-control" 
                           ng-options="tripType for tripType in tripTypeDetails"
                           ng-change = "getTripTypeDetails(user.tripType)"
                           required>
                    </select>
               </div>
           </div>

            <div class = "col-md-6 col-xs-12" ng-if = "user.empGender == 2">
               <div>
               <label>Pregnant Employee</label>
                   <select ng-model="user.pragnentLady"
                           class="form-control" 
                           ng-options="pe for pe in pregnantEmp"
                           ng-change = "setPregnantEmp(user.pragnentLady)"
                           required>
                    </select>
               </div>
           </div>
           <div class = 'col-md-12 editEmployeeMap'>
               <fieldset class = 'fieldSetTravelDesk'>
                   <Legend class = 'editEmployeeDetailLegend'>Update Employee Geocode</Legend>
               <form class = "chooseLocationForm" name = "chooseLocationForm">         
                   <div class = "col-md-4 form-group">
                     <label>Search</label>
                     <div class = "input-group calendarInput"> 
                           <span class="input-group-btn">
                               <button class="btn btn-default" 
                                       ng-click="geoCode(search)" >
                               <i class = "icon-search mapMarkerIcon"></i></button></span> 
                          <input ng-model="user.search"
                                 id = "location"
                                 type = "text" 
                                 ng-maxlength="30"
                                 maxlength="30"
                                 expect_special_char
                                 class="form-control" 
                                 placeholder = "Location"
                                 expect_special_char>
                     </div>
                   </div> 
                   <div class = "col-md-6 col-xs-12 form-group"  > 
                      <label>Geocode Address</label>
                           <textarea type="text" 
                              class="form-control" 
                              ng-model="user.address"
                              id = "newAddress"
                              placeholder = "Address"
                              
                              readonly
                              > </textarea>            
                        <span class = "hintModal"></span>  
                   </div> 
                   <div class = "col-md-6 col-xs-12 form-group hidden"> 
                      <label>Cordinates</label>
                           <input type="text" 
                              ng-model="user.cords"
                              class="form-control"  
                              id = "latlangInput"
                              placeholder = "Co-ordinates"
                              name = 'cords'
                              readonly>                  
                        <span class = "hintModal"></span>  
                   </div>  
                 
                   <div class = "col-md-12 map_viewMap map_viewMapEditEmployee"  ng-if = "mapIsLoaded">
                    <efmfm-new-user-map-search-location id = "mapDiv_admin" center = 'loc' user = 'user'></efmfm-new-user-map-search-location>            
                   </div>
        </form>
    </fieldset>
    </div> 
</form>  
</div>      
</div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "updateEmployee(user,update,loc)" ng-disabled="editEmployeeDetail.$invalid">Update</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>