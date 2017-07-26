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

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "newDriverFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New Driver</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <form name = "addDriverForm">  
     <div class = "col-md-4 col-xs-12"> 
           <div>
           <label>Driver's Name</label>
            <!-- tooltip="Driver's Name"
            tooltip-placement="bottom"
            tooltip-trigger="mouseenter"> -->
                <input type="text" 
                 ng-model="newDriver.driverName" 
                 class="form-control" 
                 placeholder = "Driver Name"
                 name = "name"
                 ng-minlength="3"
                 ng-maxlength="20"
                 maxlength="20"
                 is-name-only-valid
                 required
                 ng-class = "{error: addDriverForm.name.$invalid && !addDriverForm.name.$pristine}">
           </div>
           <span class = 'hintModal' ng-show = "addDriverForm.name.$error.pattern">* Enter Only Alphabets</span>
           <span class = 'hintModal' ng-show = "addDriverForm.name.$error.minlength">* Name is too short</span>
       </div>       
       <div class = "col-md-4 col-xs-12">
           <div>
           <label>Driver's Mobile Number</label>
                <input type="text"
                       ng-model="newDriver.mobileNumber" 
                       class="form-control" 
                       placeholder = "Contact Number"
                       name = "contactNo"
                       required 
                       is-number-only-valid
                       ng-minlength="6"
                       ng-maxlength="18"
                       maxlength="18" 
                       limit-to="18"
                       ng-class = "{error: addDriverForm.contactNo.$invalid && !addDriverForm.contactNo.$pristine}">  
           </div> 
           <span class = "hintModal" ng-show="addDriverForm.contactNo.$error.pattern">* Enter Only Numbers</span>
           <span class = "hintModal" ng-show="addDriverForm.contactNo.$error.minlength">* Too Short.. In-valid Mobile Number</span>       
           <span class = "hintModal" ng-show="addDriverForm.contactNo.$error.maxlength">* In-valid Mobile Number</span>
       </div>
      
       <div class = "col-md-4 col-xs-12">
       <label>Date of Birth</label>
        <div class = "input-group calendarInput">
            <!--  tooltip="Date of Birth"
             tooltip-placement="top"
             tooltip-trigger="mouseenter"> -->
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openDobCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                <input type="text" 
                 ng-model="newDriver.driverdob" 
                 class="form-control" 
                 placeholder = "Date of Birth" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openeddob" 
                 show-button-bar = false
                 show-weeks=false 
                 max-date = "maxDateDOB" 
                 min-date = "minDateDOB"
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'dob'
                 ng-class = "{error: addDriverForm.dob.$invalid && !addDriverForm.dob.$pristine}">
          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>License Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openExpDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span> 
          <input type="text" 
                 ng-model="newDriver.licenceValid" 
                 class="form-control" 
                 placeholder = "License Expire Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedExpDate"
                 show-button-bar = false 
                 min-date="mindate"
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'expDate'
                 ng-class = "{error: addDriverForm.expDate.$invalid && !addDriverForm.expDate.$pristine}">
                          
             </div>          
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>DDT Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openDDTCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.driverDDT" 
                 class="form-control" 
                 placeholder = "DDT Expiry Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedDDT" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 min-date="mindate"
                 required
                 readonly
                 name = 'ddt'
                 ng-class = "{error: addDriverForm.ddt.$invalid && !addDriverForm.ddt.$pristine}">          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>Batch Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openBatchCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.driverBatch" 
                 class="form-control" 
                 placeholder = "Batch Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedBatch" 
                 min-date="mindate"
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'batch'
                 ng-class = "{error: addDriverForm.batch.$invalid && !addDriverForm.batch.$pristine}">          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>Batch Number</label>
        <div>
          <input type="text" 
                 ng-model="newDriver.driverBatchNum" 
                 class="form-control" 
                 placeholder = "Batch Number"
                 ng-minlength="3"
                 ng-maxlength="25"
                 maxlength="25"
                 required
                 expect_special_char
                 name = 'batchNum'
                 ng-class = "{error: addDriverForm.batchNum.$invalid && !addDriverForm.batchNum.$pristine}">          
           </div>
           <span class = "hintModal" ng-show="addDriverForm.batchNum.$error.minlength">*Batch Num is Too Short..</span> 
           <span class = "hintModal" ng-show="addDriverForm.batchNum.$error.maxlength">* In-valid Batch Number</span> 
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>Police Verification Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openPoliceVerificationCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.driverPoliceVerification" 
                 class="form-control" 
                 placeholder = "Police Verification Expiry Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedPoliceVerification" 
                 min-date="mindate"
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'policeVerification'
                 ng-class = "{error: addDriverForm.policeVerification.$invalid && !addDriverForm.policeVerification.$pristine}">          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>Medical Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openMedicalExpiryCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.driverMedicalExpiry" 
                 class="form-control" 
                 placeholder = "Medical Expiry Date" 
                 datepicker-popup = '{{format}}'
                 min-date="mindate"
                 is-open="datePicker.openedMedicalExpiry" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'medicalExpiry'
                 ng-class = "{error: addDriverForm.medicalExpiry.$invalid && !addDriverForm.medicalExpiry.$pristine}">          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>Anti Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openAntiExpiryCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.driverAntiExpiry" 
                 class="form-control" 
                 placeholder = "Anti Expiry Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedAntiExpiry" 
                 min-date="mindate"
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'antiExpiry'
                 ng-class = "{error: addDriverForm.antiExpiry.$invalid && !addDriverForm.antiExpiry.$pristine}">          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
       <label>Joining Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openJoiningCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.driverJoining" 
                 class="form-control" 
                 placeholder = "Joining Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedJoining" 
                 min-date="mindate"
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'joining'
                 ng-class = "{error: addDriverForm.joining.$invalid && !addDriverForm.joining.$pristine}">          
           </div>
       </div>

      <div class = "col-md-4 col-xs-12">
       <label>Badge Validity</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openbadgeValidity($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="newDriver.badgeValidity" 
                 class="form-control" 
                 placeholder = "Badge Validity" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.badgeValidity" 
                 min-date="mindate"
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'badgeValidity'
                 ng-class = "{error: addDriverForm.badgeValidity.$invalid && !addDriverForm.badgeValidity.$pristine}">          
           </div>
       </div>
       <div class = "col-md-4 col-xs-12">
           <div>
           <label>License Number</label>
                <input type="text" 
                       ng-model="newDriver.licenceNumber" 
                       class="form-control" 
                       placeholder = "License Number"
                       required
                       name = "lisNum"
                       ng-maxlength="20"
                       ng-minlength="5"
                       maxlength="20"
                       minlength="5"
                       expect_special_char
                       ng-class = "{error: addDriverForm.lisNum.$invalid && !addDriverForm.lisNum.$pristine}">
           </div>
           <span class = "hintModal" ng-show="addDriverForm.lisNum.$error.minlength">* Too Short.. In-valid License Number</span>
           <span class = "hintModal" ng-show="addDriverForm.lisNum.$error.maxlength">* In-valid Liscense Number</span>
       </div>
       <div class = "col-md-4 col-xs-12">
           <div>
           <label>Address</label>
               <textarea type="text" 
                      ng-model="newDriver.driverAddress" 
                      class="form-control" 
                      placeholder = "Address"
                      name = "address" 
                      minlength="10"
                      ng-minlength="10"
                      ng-maxlength="200"
                     maxlength="200"
                     is-address-only-valid
                      required
                         ng-class = "{error: addDriverForm.address.$invalid && !addDriverForm.address.$pristine}"></textarea>
           </div>
           <span class = "hintModal" ng-show="addDriverForm.address.$error.minlength">*Your Address is too short..</span>
          <span class = "hintModal" ng-show="addDriverForm.address.$error.maxlength">*In-valid Address</span>
       </div>

       <div class = "col-md-4 col-xs-12">
           <div>
           <label>Permanent Address</label>
               <textarea type="text" 
                      ng-model="newDriver.permanentAddress" 
                      class="form-control" 
                      placeholder = "Address"
                      name = "permanentAddress" 
                      minlength="10"
                      maxlength="200"
                      ng-minlength="10"
                      ng-maxlength="200"
                      is-address-only-valid
                      required
                         ng-class = "{error: addDriverForm.permanentAddress.$invalid && !addDriverForm.permanentAddress.$pristine}"></textarea>
           </div>
           <span class = "hintModal" ng-show="addDriverForm.permanentAddress.$error.minlength">*Address is Too Short..</span>
       </div>
          <div class = "col-md-12 col-xs-12 error" ng-show = "isInvalid">* Date period is not valid</div>
      </form> 
    </div>
      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-primary" ng-click = "addDriver(newDriver)" ng-disabled="addDriverForm.$invalid">Add</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>