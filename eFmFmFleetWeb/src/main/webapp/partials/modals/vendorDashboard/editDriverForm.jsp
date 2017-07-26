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
<div class = "editDriverFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Driver</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name="driverEditForm" class = "driverEditForm">
           <div class = "col-md-4 col-xs-12"> 
              <label for = "driverName">Driver Name</label>
              <input type="text" 
                          ng-model="editDriver.driverName" 
                          class="form-control" 
                          placeholder = "Driver Name"
                          ng-minlength="3"
                          ng-maxlength="20"
                          maxlength="20"
                          name = "name"
                          is-name-only-valid
                          required
                          ng-class = "{error: driverEditForm.name.$invalid && !driverEditForm.name.$pristine}">
               <span class = 'hintModal' ng-show = "driverEditForm.name.$error.minlength">* Name is too short</span>
       </div>
           
       <div class = "col-md-4 col-xs-12">  
           <label for = "mobileNum">Driver Mobile Number</label>       
           <input type="text" 
                 ng-model="editDriver.mobileNumber" 
                 class="form-control" 
                 placeholder = "Mobile Number"
                 name = "contactNo"
                 is-number-only-valid
                 ng-minlength="6"
                 ng-maxlength="18"
                 maxlength="18" 
                 limit-to="18"
                 required
                 ng-class = "{error: driverEditForm.contactNo.$invalid && !driverEditForm.contactNo.$pristine}">
          <span class = "hintModal" ng-show="driverEditForm.contactNo.$error.maxlength">* In-valid Mobile Number</span>
       </div>
           
       <div class = "col-md-4 col-xs-12">
        <label for = "driverdob">Date of Birth</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn"><button class="btn btn-default" ng-click="openDobCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="editDriver.driverdob" 
                 class="form-control" 
                 placeholder = "Date of Birth"
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openeddob" 
                 show-button-bar = false
                 show-weeks=false
                 max-date="maxdate"
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'dob'
                 ng-class = "{error: driverEditForm.dob.$invalid && !driverEditForm.dob.$pristine}">          
           </div>
       </div>       
      
       <div class = "col-md-4 col-xs-12">
       <label>DDT Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openDDTCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="editDriver.driverDDT" 
                 class="form-control" 
                 placeholder = "DDT Expiry Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedDDT" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'ddt'
                 ng-class = "{error: driverEditForm.ddt.$invalid && !driverEditForm.ddt.$pristine}">          
           </div>
       </div>
           
       <div class = "col-md-4 col-xs-12">
       <label>Badge Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openBatchCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="editDriver.driverBatch" 
                 class="form-control" 
                 placeholder = "Batch Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedBatch" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'batch'
                 ng-class = "{error: driverEditForm.batch.$invalid && !driverEditForm.batch.$pristine}">          
           </div>
       </div>
           
       <div class = "col-md-4 col-xs-12">
       <label>Badge Number</label>
        <div>
          <input type="text" 
                 ng-model="editDriver.driverBatchNum" 
                 class="form-control" 
                 placeholder = "Batch Number"
                 ng-maxlength="50"
                 maxlength="50"
                 expect_special_char
                 required
                 name = 'batchNum'
                 ng-class = "{error: driverEditForm.batchNum.$invalid && !driverEditForm.batchNum.$pristine}">          
           </div>
       </div>
           
       <div class = "col-md-4 col-xs-12">
       <label>Police Verification Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openPoliceVerificationCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="editDriver.driverPoliceVerification" 
                 class="form-control" 
                 placeholder = "Police Verification Expiry Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedPoliceVerification" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'policeVerification'
                 ng-class = "{error: driverEditForm.policeVerification.$invalid && !driverEditForm.policeVerification.$pristine}">          
           </div>
       </div>
           
       <div class = "col-md-4 col-xs-12">
       <label>Medical Expiry Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openMedicalExpiryCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="editDriver.driverMedicalExpiry" 
                 class="form-control" 
                 placeholder = "Medical Expiry Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedMedicalExpiry" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'medicalExpiry'
                 ng-class = "{error: driverEditForm.medicalExpiry.$invalid && !driverEditForm.medicalExpiry.$pristine}">          
           </div>
       </div>           
       <div class = "col-md-4 col-xs-12">
       <label>Joining Date</label>
        <div class = "input-group calendarInput">
            <span class="input-group-btn">
                <button class="btn btn-default" ng-click="openJoiningCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
          <input type="text" 
                 ng-model="editDriver.driverJoining" 
                 class="form-control" 
                 placeholder = "Joining Date" 
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedJoining" 
                 show-button-bar = false
                 show-weeks=false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'joining'
                 ng-class = "{error: driverEditForm.joining.$invalid && !driverEditForm.joining.$pristine}">          
           </div>
       </div>
           
       <div class = "col-md-4 col-xs-12">
           <label for = "licenseNum">License Number</label>
           <input type="text" 
                 ng-model="editDriver.licenceNumber" 
                 class="form-control" 
                 placeholder = "License Number"
                 required
                 name = "lisNum"
                 ng-minlength="6"
                 ng-maxlength="20"
                 maxlength="20"
                 expect_special_char
                 ng-class = "{error: driverEditForm.lisNum.$invalid && !driverEditForm.lisNum.$pristine}">
           <span class = "hintModal" ng-show="driverEditForm.lisNum.$error.maxlength">* In-valid Liscense Number</span>
       </div>
      
       <div class = "col-md-4 col-xs-12">
           <label for = "Insurancevaliddate">License Expiry Date</label>
           <div class = "input-group calendarInput">
               <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="openExpDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span> 
               <input type="text" 
                 ng-model="editDriver.licenceValid" 
                 class="form-control" 
                 placeholder = "License Expiry Date"
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.openedExpDate"
                 show-button-bar = false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'expDate'
                 ng-class = "{error: driverEditForm.expDate.$invalid && !driverEditForm.expDate.$pristine}">                          
             </div>          
       </div>

       <div class = "col-md-4 col-xs-12">
           <label for = "Insurancevaliddate">Badge Validity Date</label>
           <div class = "input-group calendarInput">
               <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="openbadgeValidity($event)"><i class = "icon-calendar calInputIcon"></i></button></span> 
               <input type="text" 
                 ng-model="editDriver.badgeValidity" 
                 class="form-control" 
                 placeholder = "Badge Validity Date"
                 datepicker-popup = '{{format}}'
                 is-open="datePicker.badgeValidity"
                 show-button-bar = false
                 datepicker-options = 'dateOptions'
                 required
                 readonly
                 name = 'badgeValidity'
                 ng-class = "{error: driverEditForm.badgeValidity.$invalid && !driverEditForm.badgeValidity.$pristine}">                          
             </div>          
       </div>
           
       <div class = "col-md-4 col-xs-12">
           <label for = "driveraddress">Driver Address</label>
            <textarea type="text"
                       ng-model="editDriver.driverAddress" 
                       class="form-control" 
                       placeholder = "Address"
                       name = "address"
                       minlength="10"
                       ng-minlength="10"
                       ng-maxlength="200"
                      maxlength="200"
                       required
                      ng-class = "{error: driverEditForm.address.$invalid && !driverEditForm.address.$pristine}"></textarea>
           <span></span>
       </div> 
      
        <div class = "col-md-4 col-xs-12">
           <label for = "driveraddress">Permanent Address</label>
            <textarea type="text"
                       ng-model="editDriver.permanentAddress" 
                       class="form-control" 
                       placeholder = "Address"
                       name = "permanentAddress"
                       minlength="10"
                       maxlength="200"
                       ng-minlength="10"
                       ng-maxlength="200"
                       
                       required
                       ng-class = "{error: driverEditForm.permanentAddress.$invalid && !driverEditForm.permanentAddress.$pristine}"></textarea>
           <span></span>
       </div> 

       <div class = "col-md-4 col-xs-4">
           <label for = "driveraddress">Driver Documents</label>
           <div class = "">
                <div class = "floatLeft imageDiv_approval imageBorder" id = "div{{$index}}" 
                     ng-repeat = "doc in editDriver.documents"
                     ng-show = "doc.location">

                   <a href="{{doc.location}}" class = "noLinkLine" download>
                        <img class="img-responsive img-rounded fileImg" 
                              ng-src = "{{doc.location}}">

                    </a>
                </div>
           </div>
       </div>     

        <div class = "col-md-12 col-xs-12 buttonAlignmentEditDriver">
          <input type="button" class="btn btn-primary" ng-click = "viewAllDocuments(editDriver, $parent.$index, $index, 'lg')" value="View Driver Documents" name=""> 
       </div>         
       <input type = 'text' class = 'hidden' ng-model = "editDriver.driverId">
           <div class = "col-md-4 col-xs-12 hidden">
               <input type="text" ng-model="newVendor.numCabs" class="form-control">
           </div> 
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-warning buttonRadius0" ng-click = "saveDriver(editDriver)" ng-disabled="driverEditForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>