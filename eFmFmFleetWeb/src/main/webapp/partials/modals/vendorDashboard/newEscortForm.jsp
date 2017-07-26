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
<div class = "newEscortFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New Escort</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "addEscortForm">           
           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Escort's Name</label> 
                   <input type="text" 
                      ng-model="newEscort.escortName" 
                      class="form-control" 
                      placeholder = "Escort Name"
                      name = 'name'
                      required
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      is-name-only-valid
                      ng-class = "{error: addEscortForm.name.$invalid && !addEscortForm.name.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "addEscortForm.name.$error.minlength">* Name is too short</span>
           </div>
          <div class = "col-md-6 col-xs-12">
               <div>
               <label>Escort's Father Name</label> 
                   <input type="text" 
                      ng-model="newEscort.escortFatherName" 
                      class="form-control" 
                      placeholder = "Escort's Father Name"
                      name = 'escortFatherName'
                      required
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      is-name-only-valid
                      ng-class = "{error: addEscortForm.escortFatherName.$invalid && !addEscortForm.escortFatherName.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "addEscortForm.escortFatherName.$error.minlength">* Name is too short</span>
           </div>   
           <div class = "col-md-6 col-xs-12">
               <div>   
               <label>Escort's Mobile Number</label>
                    <input type="text" 
                           ng-model="newEscort.escortMobileNumber" 
                           class="form-control" 
                           placeholder = "Escort Mobile Number"
                           name = 'mobileNumber'
                           is-number-only-valid       
                           required
                           ng-minlength="6"
                           ng-maxlength="18"
                           maxlength="18"
                           limit-to="18"
                           ng-class = "{error: addEscortForm.mobileNumber.$invalid && !addEscortForm.mobileNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addEscortForm.mobileNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>
         <div class = "col-md-6 col-xs-12"> 
              <div>
               <label>Select Vendor</label>
              </div>
            <select ng-model="newEscort.escortVendorName" ng-init="vendorDatacondition(adhocDriver.selectVendor)"
                    ng-options="vendorData.vendorName as vendorData.vendorName for vendorData in vendorContractManagData"
                    name = "selectVendor"
                    class = 'vendorSelect_invoiceByVendorForm form-control'
                    required ng-change="vendorDatacheck(adhocDriver.selectVendor)">                 
                <option value="">SELECT VENDOR</option>
            </select>

           </div>

      
           <div class = "col-md-6 col-xs-12">
           <label>Date of Birth</label>
             <div class = "input-group calendarInput">  
                 <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="dobCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newEscort.escortdob" 
                      class="form-control" 
                      placeholder = "Escort Date of Birth"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.dobDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      required
                      max-date = "maxDateDOB" 
                      min-date = "minDateDOB"
                      readonly
                      name = 'dob'
                      ng-class = "{error: addEscortForm.dob.$invalid && !addEscortForm.dob.$pristine}">
             </div>
           </div> 
            <div class = "col-md-6 col-xs-12">
               <div>   
               <label>Escort Badge Number</label>
                    <input type="text" 
                           ng-model="newEscort.escortBatchNumber" 
                           class="form-control" 
                           placeholder = "Escort Batch Number"
                           name = 'batchNumber'
                           required
                           ng-minlength="2"
                           ng-maxlength="25"
                           maxlength="25"
                           expect_special_char
                           ng-class = "{error: addEscortForm.batchNumber.$invalid && !addEscortForm.batchNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addEscortForm.batchNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>   

            <div class = "col-md-6 col-xs-12">
               <div>
               <label>PIN Code</label>
                   <input type="text" 
                           ng-model="newEscort.pincode" 
                           class="form-control" 
                           placeholder = "PIN Code"
                           name = 'pincode'
                           required
                           ng-minlength="6"
                           ng-maxlength="6"
                           maxlength="6"
                           is-number-only-valid
                           expect_special_char
                           ng-class = "{error: addEscortForm.pincode.$invalid && !addEscortForm.pincode.$pristine}">
               </div>
               <span></span>
           </div>
           
           <div class = "col-md-6 col-xs-12" ng-show="multiFacility == 'Y'">
               <div>
               <label>Facility Name</label>
                   <am-multiselect class="input-lg pull-right escortFacilityDropdown"
                                       multiple="true"
                                       ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                       ng-model="facilityData"
                                       ms-header="All Facility"
                                       options="facility.branchId as facility.name for facility in facilityDetails"
                                       change="setFacilityDetails(facilityData)"
                                       >
                     </am-multiselect>
               </div>
               <span></span>
           </div>
           
           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Permanent Addresss</label>
                   <textarea type="text" 
                          ng-model="newEscort.permanentAddress" 
                          class="form-control textArea_editEscort" 
                          placeholder = "Permanent Address"
                          name = 'permanentAddress'
                          required
                          ng-minlength="10"
                          minlength="10"
                          ng-maxlength="200"
                          maxlength="200"
                          is-address-only-valid
                          ng-class = "{error: addEscortForm.permanentAddress.$invalid && !addEscortForm.permanentAddress.$pristine}"></textarea>
               </div>
               <span></span>
           </div>   
           
            <div class = "col-md-6 col-xs-12">
               <div>
               <label>Escort's Address</label>
                   <textarea type="text" 
                          ng-model="newEscort.escortAddress" 
                          class="form-control textArea_editEscort" 
                          placeholder = "Escort's Address"
                          name = 'address'
                          required
                          minlength="10"
                          ng-minlength="10"
                          ng-maxlength="200"
                          maxlength="200"
                          is-address-only-valid
                          ng-class = "{error: addEscortForm.address.$invalid && !addEscortForm.address.$pristine}"></textarea>
               </div>
               <span></span>
           </div>

       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success" ng-click = "addEscort(newEscort,facilityData)" ng-disabled="addEscortForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>