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
            <div class = "modalHeading col-md-10 floatLeft">Add New Vendor</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper"> 
       <form name = "addVendorForm">
           <div class = "col-md-4 col-xs-12"> 
               <div>
               <label>Vendor Name</label>
                <input type="text" 
                      ng-model="newVendor.vendorName"
                      class="form-control" 
                      placeholder = "Vendor Name"
                      name = 'name' 
                      ng-minlength="1"
                      ng-maxlength="50"
                      maxlength="50"
                      expect_special_char
                      required
                      ng-class = "{error: addVendorForm.name.$invalid && !addVendorForm.name.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "addVendorForm.name.$error.pattern">* Enter Only Alphabets</span>           
               <span class = "hintModal" ng-show = "addVendorForm.name.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "addVendorForm.name.$error.maxlength">* Invalid..!! Name is too long</span>
           </div>
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Mobile Number</label>             
               <input type="text" 
                           ng-model="newVendor.vendorMobileNumber" 
                           class="form-control" 
                           placeholder = "Mobile No"
                           name = 'mobileNumber'
                           is-number-only-valid
                           ng-minlength="6"
                           ng-maxlength="18"
                           maxlength="18"
                           required
                           ng-class = "{error: addVendorForm.mobileNumber.$invalid && !addVendorForm.mobileNumber.$pristine}">
               </div>    
                <span class = "hintModal" ng-show = "addVendorForm.mobileNumber.$error.pattern">* Enter Only Numbers</span>
                <span class = "hintModal" ng-show = "addVendorForm.mobileNumber.$error.minlength">* Mobile Num Is Too Short..</span>
               <span class = "hintModal" ng-show="addVendorForm.mobileNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>
           <div class = "col-md-4 col-xs-12" ng-show="multiFacility == 'Y'"> 
               <div>
               <label>Facility Name</label>
                     <am-multiselect class="input-lg pull-right vendorFacilityDropdown"
                                       multiple="true"
                                       ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                       ng-model="facilityData"
                                       ms-header="All Facility"
                                       options="facility.branchId as facility.name for facility in facilityDetails"
                                       change="setFacilityDetails(facilityData)"
                                       >
                     </am-multiselect>
               </div>
               <span class = "hintModal" ng-show = "addVendorForm.facilityName.$error.pattern">* Enter Only Alphabets</span>           
               <span class = "hintModal" ng-show = "addVendorForm.facilityName.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "addVendorForm.facilityName.$error.maxlength">* Invalid..!! Name is too long</span>
           </div>
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Email Id 1</label>
                    <input type="email" 
                           ng-model="newVendor.email" 
                           class="form-control" 
                           placeholder = "Email Id 1"
                           name = 'email'
                           required
                           ng-class = "{error: addVendorForm.email.$invalid && !addVendorForm.email.$pristine}">
               </div>
               <span class = 'hintModal'  ng-show="addVendorForm.email.$error.email">* In-valid Email Address</span>
           </div>

          <div class = "col-md-4 col-xs-12">
               <div>
               <label>Email Id 2 <span> (optional)</span></label>
                    <input type="email" 
                           ng-model="newVendor.emailIdLvl1" 
                           class="form-control" 
                           placeholder = "Email Id 2"
                           name = 'emailIdLvl1'                           
                           ng-class = "{error: addVendorForm.emailIdLvl1.$invalid && !addVendorForm.emailIdLvl1.$pristine}">
               </div>
               <span class = 'hintModal'  ng-show="addVendorForm.emailIdLvl1.$error.email">* In-valid Email Address</span>
           </div>

          <div class = "col-md-4 col-xs-12">
               <div>
               <label>Email Id 3 <span> (optional)</span></label>
                    <input type="email" 
                           ng-model="newVendor.emailIdLvl2" 
                           class="form-control" 
                           placeholder = "Email Id 3"
                           name = 'emailIdLvl2'
                         
                           ng-class = "{error: addVendorForm.emailIdLvl2.$invalid && !addVendorForm.emailIdLvl2.$pristine}">
               </div>
               <span class = 'hintModal'  ng-show="addVendorForm.emailIdLvl2.$error.email">* In-valid Email Address</span>
           </div>

           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Pan Number</label>
                    <input type="text" 
                           ng-model="newVendor.panNumber" 
                           class="form-control" 
                           placeholder = "Pan Number"
                           name = 'panNumber'
                           ng-minlength="6"
                           ng-maxlength="15"
                           maxlength="15"
                           required
                           expect_special_char
                           ng-class = "{error: addVendorForm.panNumber.$invalid && !addVendorForm.panNumber.$pristine}">  
                    <span class = 'hintModal'  ng-show="addVendorForm.panNumber.$error.minlength">* In-valid.. PAN number is too short</span>  
                    <span class = 'hintModal'  ng-show="addVendorForm.panNumber.$error.maxlength">* In-valid PAN Number</span>                 
               </div>
           </div> 

           <div class = "col-md-8 col-xs-12">
               <div>
               <label>Address</label>
                    <input type="text" 
                           ng-model="newVendor.address" 
                           class="form-control" 
                           placeholder = "Address"
                           name = 'address'
                           ng-minlength="10"
                           ng-maxlength="200"
                           required
                              ng-class = "{error: addVendorForm.address.$invalid && !addVendorForm.address.$pristine}">
               </div>
               <span class = 'hintModal'  ng-show="addVendorForm.address.$error.minlength">*In-valid ! Address is too short</span>    
               <span class = 'hintModal'  ng-show="addVendorForm.address.$error.maxlength">*In-valid ! Address is too long</span>        
           </div>
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 1</label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactName" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName'
                           ng-minlength="3"
                           ng-maxlength="20"
                           maxlength="20"
                           is-name-only-valid
                           required
                           ng-class = "{error: addVendorForm.contactName.$invalid && !addVendorForm.contactName.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "addVendorForm.contactName.$error.pattern">* Enter Only Alphabets </span>
               <span class = "hintModal" ng-show = "addVendorForm.contactName.$error.minlength">* Name is too short</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 1</label>                 
                    <input type="text" 
                           ng-model="newVendor.vendorContactNumber" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo"
                           is-number-only-valid
                           ng-maxlength="18"
                           ng-minlength="6"
                           maxlength="15"
                           required
                           ng-class = "{error: addVendorForm.contactNo.$invalid && !addVendorForm.contactNo.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVendorForm.contactNo.$error.pattern">* Enter Only Numbers</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo.$error.minlength">* Too Short.. In-valid Contact Number</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo.$error.maxlength">* In-valid Contact Number</span>
           </div>           
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 2 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactName2" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName2'
                           ng-minlength="3"
                           ng-maxlength="20" 
                           maxlength="20"
                           is-name-only-valid                          
                           ng-class = "{error: addVendorForm.contactName2.$invalid && !addVendorForm.contactName2.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "addVendorForm.contactName2.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "addVendorForm.contactName2.$error.pattern">* Enter only alphabets</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 2 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactNumber2" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo2"
                           is-number-only-valid
                           ng-minlength = '6'
                           ng-maxlength="18"
                           maxlength="18"
                           limit-to="15"
                           ng-class = "{error: addVendorForm.contactNo2.$invalid && !addVendorForm.contactNo2.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVendorForm.contactNo2.$error.pattern">* Enter Only Numbers</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo2.$error.minlength">* Too Short.. In-valid Contact Number</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo2.$error.maxlength">* In-valid Contact Number</span>
           </div>           
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 3 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactName3" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName3'
                           ng-minlength="3"
                           ng-maxlength="20"
                           maxlength="20"
                           is-name-only-valid
                           ng-class = "{error: addVendorForm.contactName3.$invalid && !addVendorForm.contactName3.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "addVendorForm.contactName3.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "addVendorForm.contactName3.$error.pattern">* Enter Only Alphabets</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 3 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactNumber3" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo3"
                           is-number-only-valid
                           ng-minlength = '6'
                           ng-maxlength="18"
                           maxlength="18"
                           limit-to="15"
                           ng-class = "{error: addVendorForm.contactNo3.$invalid && !addVendorForm.contactNo3.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVendorForm.contactNo3.$error.pattern">* Enter Only Nummbers</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo3.$error.minlength">* Too Short.. In-valid Contact Number</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo3.$error.maxlength">* In-valid Contact Number</span>
           </div>          
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 4 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactName4" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName4'
                           ng-minlength="3"
                           ng-maxlength="20"
                           maxlength="20"
                           is-name-only-valid
                           ng-class = "{error: addVendorForm.contactName4.$invalid && !addVendorForm.contactName4.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "addVendorForm.contactName4.$error.minlength">* Name is too short</span>
                <span class = "hintModal" ng-show = "addVendorForm.contactName4.$error.pattern">* Enter Only Alphabets</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 4 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="newVendor.vendorContactNumber4" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo4"
                           is-number-only-valid
                           ng-minlength = '6'
                           ng-maxlength="18"
                           maxlength="18"
                           limit-to="15"
                           ng-class = "{error: addVendorForm.contactNo4.$invalid && !addVendorForm.contactNo4.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVendorForm.contactNo4.$error.pattern">* Enter Only Nummbers</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo4.$error.minlength">* Too Short.. In-valid Contact Number</span>
               <span class = "hintModal" ng-show="addVendorForm.contactNo4.$error.maxlength">* In-valid Contact Number</span>
           </div>
           <div class = "col-md-4 col-xs-12"> 
               <div>
               <label>Service Tax Number</label>
                <input type="text" 
                      ng-model="newVendor.serviceTaxNumber"
                      class="form-control" 
                      placeholder = "Service Tax Number"
                      name = 'serviceTaxNumber' 
                       ng-minlength="2"
                       ng-maxlength="20"
                       maxlength="20"
                      required
                      ng-class = "{error: addVendorForm.serviceTaxNumber.$invalid && !addVendorForm.serviceTaxNumber.$pristine}">                   
               </div>
              <span class = "hintModal" ng-show="addVendorForm.serviceTaxNumber.$error.minlength">*Too short.. In-valid Service Tax Number</span>
              <span class = "hintModal" ng-show="addVendorForm.serviceTaxNumber.$error.maxlength">* In-valid Service Tax Number</span>
           </div>
           <div class = "col-md-4 col-xs-12"> 
               <div>
               <label>TIN Number</label>
                <input type="text" 
                      ng-model="newVendor.tinNumber"
                      class="form-control" 
                      placeholder = "TIN Number"
                      name = 'tinNumber' 
                       ng-minlength="2"
                       ng-maxlength="20"
                       maxlength="20"
                      required
                      ng-class = "{error: addVendorForm.tinNumber.$invalid && !addVendorForm.tinNumber.$pristine}">                   
               </div>
              <span class = "hintModal" ng-show="addVendorForm.tinNumber.$error.minlength">*Too short.. In-valid TIN Number</span>
              <span class = "hintModal" ng-show="addVendorForm.tinNumber.$error.maxlength">* In-valid TIN Number</span>
           </div>
           <div class = "col-md-12 col-xs-12 error" ng-show = "isInvalid">* Date period is not valid</div>
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success" ng-click = "addNewVendor(newVendor,facilityData)" ng-disabled="addVendorForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>