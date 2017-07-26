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
<div class = "editEscortFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Escort</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "editEscortForm" class = "editEscortForm">           
           <div class = "col-md-4 col-xs-12">

               <label for = "escortName">Escort Name</label>
               <input type="text" 
                      ng-model="editEscort.escortName" 
                      class="form-control" 
                      placeholder = "Escort's Name"
                      name = 'name'
                      required
                      ng-minlength="3"
                      ng-maxlength="20"
                      maxlength="20"
                      is-name-only-valid
                      ng-class = "{error: editEscortForm.name.$invalid && !editEscortForm.name.$pristine}">
               <span class = 'hintModal' ng-show = "editEscortForm.name.$error.minlength">* Name is too short</span>
           </div>
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Escort's Father Name</label> 
                   <input type="text" 
                      ng-model="editEscort.escortFatherName" 
                      class="form-control" 
                      placeholder = "Escort's Father Name"
                      name = 'escortFatherName'
                      required
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      is-name-only-valid
                      ng-class = "{error: editEscortForm.escortFatherName.$invalid && !editEscortForm.escortFatherName.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "editEscortForm.escortFatherName.$error.minlength">* Name is too short</span>
           </div>     
           <div class = "col-md-4 col-xs-12">
               <label for = "escortMobileNumber">Escort Mobile Number</label>
               <input type="text" 
                      ng-model="editEscort.escortMobileNumber" 
                      class="form-control" 
                      placeholder = "Escort Mobile Number"
                      name = 'mobileNumber'
                      is-number-only-valid
                      required
                      ng-minlength="6"
                      ng-maxlength="18"
                      maxlength="18"
                      ng-class = "{error: editEscortForm.mobileNumber.$invalid && !editEscortForm.mobileNumber.$pristine}"> 
               <span class = "hintModal" ng-show="editEscortForm.mobileNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>
           <div class = "col-md-4 col-xs-12"> 
              <div>
               <label>Select Vendor</label>
              </div>
            <select ng-model="editEscort.escortVendorName" ng-init="vendorDatacondition(adhocDriver.selectVendor)"
                    ng-options="vendorData as vendorData.vendorName for vendorData in vendorContractManagData track by vendorData.vendorName"
                    name = "selectVendor"
                    class = 'vendorSelect_invoiceByVendorForm form-control'
                    required ng-change="vendorDatacheck(adhocDriver.selectVendor)">                 
                <option value="">SELECT VENDOR</option>
            </select>

           </div> 
         <!--   <div class = "col-md-4 col-xs-12">
               <div>   
               <label>Escort Vendor Name</label>
                    <input type="text" 
                           ng-model="editEscort.escortVendorName" 
                           class="form-control" 
                           placeholder = "Escort Vendor name"
                           name = 'escortVendorName'
                           required
                           maxlength = "50"
                           minlength="3"
                           ng-minlength="3"
                           ng-maxlength="50"
                           expect_special_char
                           ng-class = "{error: editEscortForm.escortVendorName.$invalid && !editEscortForm.escortVendorName.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editEscortForm.escortVendorName.$error.maxlength">* In-valid Vendor Name</span>
           </div> -->
       
           <div class = "col-md-4 col-xs-12">
             <label for = "dob">Date of Birth</label>  
             <div class = "input-group calendarInput">
                 <span class="input-group-btn"><button class="btn btn-default" ng-click="dobCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="editEscort.escortdob" 
                      class="form-control" 
                      placeholder = "Date of Birth"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.dobDate" 
                      show-button-bar = false
                      max-date = "maxDateDOB" 
                      min-date = "minDateDOB"
                      datepicker-options = 'dateOptions'
                      required
                      name = 'dob'
                      readonly
                      ng-class = "{error: editEscortForm.dob.$invalid && !editEscortForm.dob.$pristine}">
                
             </div>
           </div>            
             
           <div class = "col-md-4 col-xs-12">
               <div>   
               <label>Escort Badge Number</label>
                    <input type="text" 
                           ng-model="editEscort.escortBatchNumber" 
                           class="form-control" 
                           placeholder = "Escort Batch Number"
                           name = 'batchNumber'
                           required
                           ng-minlength="2"
                           ng-maxlength="25"
                           maxlength="25"
                           expect_special_char
                           ng-class = "{error: editEscortForm.batchNumber.$invalid && !editEscortForm.batchNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editEscortForm.batchNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div> 
           
           <div class = "col-md-4 col-xs-12">
               <label for = "escortAddress">Escort Address</label>
               <textarea type="text" 
                          ng-model="editEscort.escortAddress" 
                          class="form-control textArea_editEscort" 
                          placeholder = "Escort's Address"
                          name = 'address'
                          required
                          ng-minlength="10"
                          ng-maxlength="200"
                          maxlength="200"
                          minlength="10"
                          is-address-only-valid
                          ng-class = "{error: editEscortForm.address.$invalid && !editEscortForm.address.$pristine}"></textarea>
               <span></span>
           </div>  

            <div class = "col-md-4 col-xs-12">
               <label for = "permanentAddress">Permanent Address</label>
               <textarea type="text" 
                          ng-model="editEscort.permanentAddress" 
                          class="form-control textArea_editEscort" 
                          placeholder = "Permanent Address"
                          name = 'permanentAddress'
                          required
                          ng-minlength="10"
                          ng-maxlength="200"
                          maxlength="200"
                          minlength="10"
                          is-address-only-valid
                          ng-class = "{error: editEscortForm.permanentAddress.$invalid && !editEscortForm.permanentAddress.$pristine}"></textarea>
               <span></span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>PIN Code</label>
                   <input type="text" 
                           ng-model="editEscort.pincode" 
                           class="form-control" 
                           placeholder = "PIN Code"
                           name = 'pincode'
                           required
                           ng-minlength="6"
                           ng-maxlength="6"
                           maxlength="6"
                           is-number-only-valid
                           expect_special_char
                           ng-class = "{error: editEscortForm.pincode.$invalid && !editEscortForm.pincode.$pristine}">
               </div>
               <span></span>
           </div>
           <div class = "col-md-12 col-xs-12 buttonAlignmentEditDriver">
          <input type="button" class="btn btn-primary" ng-click = "viewAllDocumentsEscort(editEscort, $parent.$index, $index, 'lg')" value="View Escort Documents" name=""> 
       </div>
           
       

       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-warning" ng-click = "saveEscort(editEscort,facilityData)" ng-disabled="editEscortForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>