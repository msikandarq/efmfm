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
<div class = "addVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add Adhoc Driver & Vehicle Details Without Device</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "addAdhocdriverForm">
           <div class = "col-md-4 col-xs-12"> 
              <div>
               <label>Select Vendor</label>
              </div>
            <select ng-model="adhocDriver.selectVendor" ng-init="vendorDatacondition(adhocDriver.selectVendor)"
                    ng-options="vendorData.name for vendorData in vendorsData"
                    name = "selectVendor"
                    class = 'vendorSelect_invoiceByVendorForm'
                    required ng-change="vendorDatacheck(adhocDriver.selectVendor)">                 
                <option value="">SELECT VENDOR</option>
            </select>

           </div>
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Vehicle Number <small>Eg-TN31AA1234</small> </label>
              </div>
              <input type="text" 
                      ng-model="adhocDriver.registerNumber"
                      class="form-control" 
                      placeholder = "Full Vehicle Number"
                      name = 'registerNumber' 
                      ng-minlength="4"
                      ng-maxlength="15"
                      maxlength="15"
                      required
                      expect_special_char
                      ng-disabled="adhocVendorVehicle"
                      ng-change="vehicleNumberCheck(adhocDriver.registerNumber, adhocDriver.selectVendor, $event)"
                      ng-class = "{error: addAdhocdriverForm.registerNumber.$invalid && !addAdhocdriverForm.registerNumber.$pristine}">
                      <span class="alertSpan" ng-show="failureMsg">* {{failureMsgdata}}</span>
           </div>

           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Vehicle Model</label>             
              </div>    
               
              <input type="text" 
                      ng-model="adhocDriver.vehicleModel"
                      class="form-control" 
                      placeholder = "Vehicle Model - Like Innova, Indica"
                      name = 'vehicleModel' 
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      expect_special_char
                      required
                      ng-disabled="adhocDriverselectVendor"
                      ng-class = "{error: addAdhocdriverForm.vehicleModel.$invalid && !addAdhocdriverForm.vehicleModel.$pristine}"
                      ng-focus="">

           </div>


           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Seat Capacity</label>             
              </div>    
               
              <input type="text" 
                      ng-model="adhocDriver.seatCapacity"
                      class="form-control" 
                      placeholder = "Enter Seat Capacity"
                      name = 'seatCapacity' 
                      is-number-only-valid
                      ng-minlength="1"
                      ng-maxlength="50"
                       maxlength="50"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                     
                      required
                      ng-disabled="adhocDriverselectVendor"
                      ng-class = "{error: addAdhocdriverForm.seatCapacity.$invalid && !addAdhocdriverForm.seatCapacity.$pristine}">
             <span class="alertSpan" ng-show="addAdhocdriverForm.seatCapacity.$error.pattern">* Please , enter number only</span>
           </div>

           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Pollution Validity</label>
              </div>
               <div class = "input-group calendarInput">
                   <span class="input-group-btn">
                       <button class="btn btn-default" ng-click="pollutionValidCal($event)" ng-disabled="adhocDriverselectVendor"><i class = "icon-calendar calInputIcon" ></i></button></span>
                 <input type="text" 
                        ng-model="adhocDriver.pollutionValid" 
                        class="form-control" 
                        placeholder = "Pollution Validity"
                        datepicker-popup = '{{format}}'
                        is-open="datePicker.PollutionValidDate" 
                        min-date = "setMinDate"
                        show-button-bar = false
                        datepicker-options = 'dateOptions'
                        name = 'pollutionValid'
                        ng-disabled="true"
                        required
                        ng-class = "{error: addAdhocdriverForm.pollutionValid.$invalid && !addAdhocdriverForm.pollutionValid.$pristine}">
                  
               </div>
           </div>
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Insurance Validity</label>
              </div>
               <div class = "input-group calendarInput">
                   <span class="input-group-btn">
                       <button class="btn btn-default" ng-click="insurValidCal($event)" ng-disabled="adhocDriverselectVendor"><i class = "icon-calendar calInputIcon"></i></button></span>
                 <input type="text" 
                        ng-model="adhocDriver.insuranceValid" 
                        class="form-control" 
                        placeholder = "DD-MM-YYYY"
                        datepicker-popup = '{{format}}'
                        is-open="datePicker.insuranceValidDate" 
                        min-date = "setMinDate"
                        show-button-bar = false
                        datepicker-options = 'dateOptions'
                        name = 'insuranceValid'
                        ng-disabled="true"
                        required
                        ng-class = "{error: addAdhocdriverForm.insuranceValid.$invalid && !addAdhocdriverForm.insuranceValid.$pristine}">
                  
               </div>
           </div>
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Permit Validity</label>
              </div>
               <div class = "input-group calendarInput">
                   <span class="input-group-btn">
                       <button class="btn btn-default" ng-click="PermitValidCal($event)" ng-disabled="adhocDriverselectVendor"><i class = "icon-calendar calInputIcon"></i></button></span>
                 <input type="text" 
                        ng-model="adhocDriver.PermitValid" 
                        class="form-control" 
                        placeholder = "DD-MM-YYYY"
                        datepicker-popup = '{{format}}'
                        is-open="datePicker.PermitValidDate" 
                        min-date = "setMinDate"
                        show-button-bar = false
                        datepicker-options = 'dateOptions'
                        name = 'PermitValid'
                        ng-disabled="true"
                        required
                        ng-class = "{error: addAdhocdriverForm.PermitValid.$invalid && !addAdhocdriverForm.PermitValid.$pristine}">
                  
               </div>
           </div>
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Tax Validity</label>
              </div>
              <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                        <button class="btn btn-default" ng-click="TaxValidCal($event)"  ng-disabled="adhocDriverselectVendor"><i class = "icon-calendar calInputIcon"></i></button></span>
                   <input type="text" 
                          ng-model="adhocDriver.TaxValid" 
                          class="form-control" 
                          placeholder = "DD-MM-YYYY"
                          datepicker-popup = '{{format}}'
                          is-open="datePicker.TaxValidDate" 
                          min-date = "setMinDate"
                          show-button-bar = false
                          datepicker-options = 'dateOptions'
                          name = 'TaxValid'
                          ng-disabled="true"
                          required
                          ng-class = "{error: addAdhocdriverForm.TaxValid.$invalid && !addAdhocdriverForm.TaxValid.$pristine}">
                
                 </div>
           </div>
           <!-- <div class = "col-md-4 col-xs-12 inputSpin">
              <div>
               <label>Driver Mobile Number</label>
              </div>
              <input type="number" 
                      ng-model="adhocDriverObj.driverMobileNo"
                      class="form-control" 
                      placeholder = "Enter Driver Mobile No"
                      name = 'driverMobileNo' 
                      ng-minlength="6"
                      ng-maxlength="18"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="18"
                      ng-blur="driverNumberCheck(adhocDriverObj.driverMobileNo, adhocDriver.selectVendor, $event)"
                      required
                      ng-disabled="adhocDriverpartMobile"
                      ng-class = "{error: addAdhocdriverForm.driverMobileNo.$invalid && !addAdhocdriverForm.driverMobileNo.$pristine}" />
                      <span class="alertSpan" ng-show="failureMsgForDriver">* {{failureMsgdataForDriver}}</span>
           </div> -->
            
            <div class = "col-md-4 col-xs-12 inputSpin">
              <div>
               <label>Driver Mobile Number</label>
              </div>
              <input type="text" 
                      ng-model="adhocDriverObj.driverMobileNo"
                      class="form-control" 
                      placeholder = "Enter Driver Mobile No"
                      name = 'driverMobileNo' 
                      is-number-valid
                      ng-minlength="6"
                      ng-maxlength="18"
                      maxlength="18"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      
                      ng-change="driverNumberCheck(adhocDriverObj.driverMobileNo, adhocDriver.selectVendor, $event)"
                      required
                      ng-disabled="adhocDriverpartMobile"
                      ng-class = "{error: addAdhocdriverForm.driverMobileNo.$invalid && !addAdhocdriverForm.driverMobileNo.$pristine}" />
                      <span class="alertSpan" ng-show="failureMsgForDriver">* {{failureMsgdataForDriver}}</span>
                      <span class="alertSpan" ng-show="addAdhocdriverForm.driverMobileNo.$error.pattern">* Please , enter number only</span>
           </div>

           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Driver Name</label>
              </div>
              <input type="text" 
                      ng-model="adhocDriverObj.driverName"
                      class="form-control" 
                      placeholder = "Enter Driver Name"
                      name = 'driverName' 
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                       is-name-only-valid
                      required
                      ng-disabled="adhocDriverpart"
                      ng-class = "{error: addAdhocdriverForm.driverName.$invalid && !addAdhocdriverForm.driverName.$pristine}">
                 <span class="alertSpan" ng-show="  addAdhocdriverForm.driverName.$invalid && !addAdhocdriverForm.driverName.$pristine">Please enter text only</span>    
           </div>
           
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Licence Number</label>
              </div>
              <input type="text" 
                      ng-model="adhocDriverObj.licenceNumber"
                      class="form-control" 
                      placeholder = "Enter Licence Number"
                      name = 'licenceNumber' 
                      ng-minlength="6"
                      ng-maxlength="20"
                      maxlength="20"
                      required
                      expect_special_char
                      ng-disabled="adhocDriverpart"
                      ng-class = "{error: addAdhocdriverForm.licenceNumber.$invalid && !addAdhocdriverForm.licenceNumber.$pristine}">
           </div>
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Licence Validity</label>
              </div>
              <div class = "input-group calendarInput">
                   <span class="input-group-btn">
                       <button class="btn btn-default" ng-click="licenceValidity($event)" ng-disabled="adhocDriverpart"><i class = "icon-calendar calInputIcon"></i></button></span>
                 <input type="text" 
                        ng-model="adhocDriverObj.licenceValidity" 
                        class="form-control" 
                        placeholder = "DD-MM-YYYY"
                        datepicker-popup = '{{format}}'
                        is-open="datePicker.licenceValidity" 
                        show-button-bar = false
                        min-date = "setMinDate"
                        datepicker-options = 'dateOptions'
                        name = 'licenceValidity'
                        ng-disabled="true"
                        required
                        ng-class = "{error: addAdhocdriverForm.licenceValidity.$invalid && !addAdhocdriverForm.licenceValidity.$pristine}">
                  
               </div>
           </div>
           
        
          
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success" ng-click = "addAdhocDriverDetail(adhocDriver, adhocDriverObj)" ng-disabled="addAdhocdriverForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>