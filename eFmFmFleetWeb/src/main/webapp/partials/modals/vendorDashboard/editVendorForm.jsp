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
<div class = "editVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Vendor</div> 
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "editVendorForm" class = "editVendorForm">
           <div class = "col-md-4 col-xs-12 form-group editVendorTextbox">
               <div>
               <label for = "name">Vendor Name</label>
               <input type="text" 
                      ng-model="editVendor.vendorName" 
                      class="form-control" 
                      placeholder = "Vendor Name"
                      is-name-only-valid
                      ng-minlength="1"
                      ng-maxlength="50"
                      maxlength="50"
                      name = 'name'
                      required
                      ng-class = "{error: editVendorForm.name.$invalid && !editVendorForm.name.$pristine}">
               </div>
               <span class = "hintModal" ng-show = "editVendorForm.name.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "editVendorForm.name.$error.maxlength">* Invalid Vendor Name</span>
               <span class = "hintModal" ng-show = "editVendorForm.name.$error.pattern && !editVendorForm.name.$pristine">* Enter Only Alphabets </span>


           </div>
           <div class = "col-md-4 col-xs-12 form-group editVendorTextbox">
               <label for = "mobileNumber">Mobile Number</label>
               <input type="text" 
                          ng-model="editVendor.vendorMobileNumber" 
                          class="form-control" 
                          placeholder = "Mobile Number"
                          name = 'mobileNumber'
                          is-number-only-valid
                          ng-minlength="6"
                          ng-maxlength="18"
                          maxlength="18"
                          required
                          ng-class = "{error: editVendorForm.mobileNumber.$invalid && !editVendorForm.mobileNumber.$pristine}">
                <span class = "hintModal" ng-show="editVendorForm.mobileNumber.$error.pattern">* Enter Only Numbers</span>    
                <span class = "hintModal" ng-show="editVendorForm.mobileNumber.$error.minlength">* Too short.. In-valid Mobile Number</span>          
               <span class = "hintModal" ng-show="editVendorForm.mobileNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>
           <div class = "col-md-4 col-xs-12 form-group editVendorTextbox">
               <div>
               <label for = "facilityName">Facility Name</label>
               <am-multiselect class="input-lg pull-right vendorFacilityDropdown"
                                       multiple="true"
                                       ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                       ng-model="facilityData"
                                       ms-header="All Facility"
                                       options="facility.branchId as facility.name for facility in facilityDetails"
                                       change="setFacilityDetails(facilityData)"
                                       >
                     </am-multiselect>
               <!-- <input type="text" 
                      ng-model="editVendor.facilityName" 
                      class="form-control" 
                      placeholder = "Facility Name"
                      ng-minlength="1"
                      ng-maxlength="50"
                      maxlength="50"
                      name = 'facilityName'
                      required
                      ng-class = "{error: editVendorForm.facilityName.$invalid && !editVendorForm.facilityName.$pristine}"> -->
               </div>
               <span class = "hintModal" ng-show = "editVendorForm.facilityName.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "editVendorForm.facilityName.$error.maxlength">* Invalid Vendor Name</span>
               <span class = "hintModal" ng-show = "editVendorForm.facilityName.$error.pattern && !editVendorForm.facilityName.$pristine">* Enter Only Alphabets </span>
           </div>
           <div class = "col-md-4 col-xs-12 form-group editVendorTextbox">
               <label for = "email">Email Address 1</label>
                       <input type="email" 
                          ng-model="editVendor.email" 
                          class="form-control" 
                          placeholder = "Email Address 1"
                          name = 'email'
                          required
                          ng-class = "{error: editVendorForm.email.$invalid && !editVendorForm.email.$pristine}">
               <span class = 'hintModal'  ng-show="editVendorForm.email.$error.email">* In-valid Email Address</span>
           </div> 
            <div class = "col-md-4 col-xs-12 form-group editVendorTextbox">
               <label for = "email">Email Address 2 <span> (optional)</span></label>
                       <input type="email" 
                          ng-model="editVendor.emailIdLvl1" 
                          class="form-control" 
                          placeholder = "Email Address 2"
                          name = 'emailIdLvl1'
                          
                          ng-class = "{error: editVendorForm.emailIdLvl1.$invalid && !editVendorForm.emailIdLvl1.$pristine}">
               <span class = 'hintModal'  ng-show="editVendorForm.emailIdLvl1.$error.email">* In-valid Email Address</span>
           </div> 
            <div class = "col-md-4 col-xs-12 form-group editVendorTextbox">
               <label for = "email">Email Address 3 <span> (optional)</span></label>
                       <input type="email" 
                          ng-model="editVendor.emailIdLvl2" 
                          class="form-control" 
                          placeholder = "Email Address 3"
                          name = 'emailIdLvl2'
                          
                          ng-class = "{error: editVendorForm.emailIdLvl2.$invalid && !editVendorForm.emailIdLvl2.$pristine}">
                <span class = 'hintModal'  ng-show="editVendorForm.emailIdLvl2.$error.email">* In-valid Email Address</span>
               
           </div> 
           <div class = "col-md-4 col-xs-12 form-group">
               <label for = "panNumber">Pan Number</label>
               <input type="text" 
                      ng-model="editVendor.panNumber" 
                      class="form-control" 
                      placeholder = "Pan Number"
                      name = 'panNumber'
                      ng-minlength="1"
                      ng-maxlength="15"
                      maxlength="15"
                      expect_special_char
                      required
                      ng-class = "{error: editVendorForm.panNumber.$invalid && !editVendorForm.panNumber.$pristine}">
                <span class = 'hintModal'  ng-show="editVendorForm.panNumber.$error.minlength">* PAN Number is Too short.. </span>
                <span class = 'hintModal'  ng-show="editVendorForm.panNumber.$error.maxlength">* In-valid PAN Number</span>
           </div>
           <div class = "col-md-8 col-xs-12 form-group">
               <label for = "address">Address</label>
               <input type="text" 
                          ng-model="editVendor.address" 
                          class="form-control" 
                          placeholder = "Address"
                          name = 'address'
                          ng-minlength="10"
                          ng-maxlength="200"
                          maxlength="200"
                          minlength="10"
                          required
                          ng-class = "{error: editVendorForm.address.$invalid && !editVendorForm.address.$pristine}">
              <span class = 'hintModal'  ng-show="editVendorForm.address.$error.minlength">* Address is Too Short..</span>
              <span class = 'hintModal'  ng-show="editVendorForm.address.$error.maxlength">* In-valid Address</span>
           </div>

           <div class = "col-md-4 col-xs-12 form-group">
               <label for = "contactName">Contact Name 1</label>
               <input type="text" 
                      ng-model="editVendor.vendorContactName" 
                      class="form-control" 
                      placeholder = "Contact Name"
                      name = 'contactName'
                      is-name-only-valid
                      ng-minlength="3"
                      ng-maxlength="20"
                      maxlength="20"
                      required
                      ng-class = "{error: editVendorForm.contactName.$invalid && !editVendorForm.contactName.$pristine}">
               <span class = "hintModal" ng-show = "editVendorForm.contactName.$error.pattern">* Enter Only Alphabets</span>
               <span class = "hintModal" ng-show = "editVendorForm.contactName.$error.minlength">* Name is too short</span>
           </div>
       
           <div class = "col-md-4 col-xs-12 form-group">
           
               <label for = "contactNo">Contact Number 1</label>
               <input type="text" 
                          ng-model="editVendor.vendorContactNumber" 
                          class="form-control" 
                          placeholder = "Contact No."
                          name ="contactNo"
                          is-number-only-valid
                          ng-minlength = "6"
                          ng-maxlength="18"
                          limit-to="15"
                          required
                          ng-class = "{error: editVendorForm.contactNo.$invalid && !editVendorForm.contactNo.$pristine}">

               
               <span class = "hintModal" ng-show="editVendorForm.contactNo.$error.minlength">-* Too Short.. Invalid Contact Number</span>           
               <span class = "hintModal" ng-show="editVendorForm.contactNo.$error.maxlength">* In-valid Contact Number</span>
               <span class = "hintModal" ng-show="editVendorForm.contactNo.$error.pattern && !editVendorForm.contactNo.$pristine">* Enter Only Numbers</span> 
           </div>
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 2 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="editVendor.vendorContactName2" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName2'
                           ng-minlength = "3"
                           ng-maxlength="20"
                           maxlength="20"
                           is-name-only-valid
                           ng-class = "{error: editVendorForm.contactName2.$invalid && !editVendorForm.contactName2.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "editVendorForm.contactName2.$error.pattern">* Enter Only Alphabets</span>
               <span class = "hintModal" ng-show = "editVendorForm.contactName2.$error.minlength">* Name is too short</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 2 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="editVendor.vendorContactNumber2" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo2"
                           is-number-only-valid
                           ng-minlength = "6"
                           ng-maxlength="18"
                           maxlength="18"
                           ng-class = "{error: editVendorForm.contactNo2.$invalid && !editVendorForm.contactNo2.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editVendorForm.contactNo2.$error.pattern">* Enter Only Numbers</span> 
               <span class = "hintModal" ng-show="editVendorForm.contactNo2.$error.minlength">* Invalid ! Contact Number is Too Short..</span>
               <span class = "hintModal" ng-show="editVendorForm.contactNo2.$error.maxlength">* In-valid Contact Number</span>
           </div>           
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 3 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="editVendor.vendorContactName3" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName3'
                           ng-minlength = "3"
                           ng-maxlength="20"
                           maxlength="20"
                            is-name-only-valid 
                           ng-class = "{error: editVendorForm.contactName3.$invalid && !editVendorForm.contactName3.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "editVendorForm.contactName3.$error.pattern">* Enter Only Alphabets</span>
               <span class = "hintModal" ng-show = "editVendorForm.contactName3.$error.minlength">* Name is too short</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 3 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="editVendor.vendorContactNumber3" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo3"
                           is-number-only-valid
                           ng-minlength = "6"
                           ng-maxlength="18"
                           maxlength="18"
                           ng-class = "{error: editVendorForm.contactNo3.$invalid && !editVendorForm.contactNo3.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editVendorForm.contactNo3.$error.pattern">* Enter Only Numbers</span> 
               <span class = "hintModal" ng-show="editVendorForm.contactNo3.$error.minlength">* In-valid! Contact Number Is Too Short..</span>
               <span class = "hintModal" ng-show="editVendorForm.contactNo3.$error.maxlength">* In-valid Contact Number</span>
           </div>          
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Name 4 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="editVendor.vendorContactName4" 
                           class="form-control" 
                           placeholder = "Contact Name"
                           name = 'contactName4'
                           ng-minlength = "3"
                           ng-maxlength="20"
                           maxlength="20"
                           is-name-only-valid
                           ng-class = "{error: editVendorForm.contactName4.$invalid && !editVendorForm.contactName4.$pristine}">                   
               </div>
               <span class = "hintModal" ng-show = "editVendorForm.contactName4.$error.pattern">* Enter Only Alphabets</span>
               <span class = "hintModal" ng-show = "editVendorForm.contactName4.$error.minlength">* Name is too short</span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contact Number 4 <span> (optional)</span></label>
                    <input type="text" 
                           ng-model="editVendor.vendorContactNumber4" 
                           class="form-control" 
                           placeholder = "Contact Number"
                           name = "contactNo4"
                           is-number-only-valid
                           ng-minlength = "6"
                           ng-maxlength="18"
                           maxlength="18"
                           ng-class = "{error: editVendorForm.contactNo4.$invalid && !editVendorForm.contactNo4.$pristine}">
               </div>
               <span class = "hintModal" ng-show="editVendorForm.contactNo4.$error.pattern">* Enter Only Numbers</span> 
               <span class = "hintModal" ng-show="editVendorForm.contactNo4.$error.minlength">* In-valid! Contact Number Is Too Short..</span>
               <span class = "hintModal" ng-show="editVendorForm.contactNo4.$error.maxlength">* In-valid Contact Number</span>
           </div>

           <!-- <div class = "col-md-4 col-xs-12">
               <div>
               <label>Pan Number</label>
                    <input type="text" 
                           ng-model="editVendor.panNumber" 
                           class="form-control" 
                           placeholder = "Pan Number"
                           name = 'panNumber'
                           ng-minlength="2"
                           ng-maxlength="30"
                           ng-class = "{error: editVendorForm.panNumber.$invalid && !editVendorForm.panNumber.$pristine}">                   
               </div>
           </div>  -->

            <div class = "col-md-4 col-xs-12">
               <div>
               <label>Service Tax Number</label>
                    <input type="text" 
                           ng-model="editVendor.serviceTaxNumber" 
                           class="form-control" 
                           placeholder = "Service Tax Number"
                           name = 'serviceTaxNumber'
                           ng-minlength="2"
                           ng-maxlength="20"
                           maxlength="20" required
                           ng-class = "{error: editVendorForm.serviceTaxNumber.$invalid && !editVendorForm.serviceTaxNumber.$pristine}">                   
               </div>
            <span class = "hintModal" ng-show="editVendorForm.serviceTaxNumber.$error.minlength">* In-valid! Service Tax Number Is Too Short..</span>
            <span class = "hintModal" ng-show="editVendorForm.serviceTaxNumber.$error.maxlength">* In-valid Service Tax Number</span>
           </div> 
             <div class = "col-md-4 col-xs-12"> 
               <div>
               <label>TIN Number</label>
                <input type="text" 
                      ng-model="editVendor.tinNumber"
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
           <div class = "col-md-4 col-xs-12"> 
               <div>
               <label>Facility Name</label>
                <input type="text" 
                      ng-model="editVendor.tinNumber"
                      class="form-control" 
                      placeholder = "TIN Number"
                      name = 'tinNumber' 
                       ng-minlength="2"
                       ng-maxlength="20"
                       maxlength="20"
                      required
                      ng-class = "{error: addVendorForm.tinNumber.$invalid && !addVendorForm.tinNumber.$pristine}">                   
               </div>
              
           </div>
         <!--   <div class = "col-md-8 col-xs-12 form-group">
           <div ng-repeat = "doc in vendorDocuments" class = "floatLeft" id = "div{{$index}}">
               <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>
                    <a href="{{doc.location}}" class = "noLinkLine">
                        <img class="img-responsive img-rounded fileImg" 
                          alt="Responsive image" 
                          ng-src = "{{doc.imgSrc}}">
                        <div class = "docName"><span>{{doc.name}}</span></div>
                    </a>                     
                   
           </div>
       </div>           -->  
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-warning" ng-click = "saveVendor(editVendor,facilityData)" ng-disabled="editVendorForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>