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
            <div class = "modalHeading col-md-10 floatLeft">Add New Supervisor1</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "addSupervisorForm">           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>First Name</label> 
                   <input type="text" 
                      ng-model="newSupervisor.firstName" 
                      class="form-control" 
                      placeholder = "First Name" 
                      name = 'firstName'
                      required
                      minlength="3"
                      ng-minlength="3"
                      ng-maxlength="20"
                      is-name-only-valid
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="20"
                      ng-class = "{error: addSupervisorForm.firstName.$invalid && !addSupervisorForm.firstName.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "addSupervisorForm.name.$error.minlength">* Name is too short</span>
           </div>
             
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Last Name</label> 
                   <input type="text" 
                      ng-model="newSupervisor.lastName" 
                      class="form-control" 
                      placeholder = "Last Name"
                      name = 'lastName'
                      required
                      minlength="3"
                      ng-minlength="3"
                      ng-maxlength="20"
                      is-name-only-valid
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="20"
                      ng-class = "{error: addSupervisorForm.lastName.$invalid && !addSupervisorForm.lastName.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "addSupervisorForm.name.$error.minlength">* Name is too short</span>
           </div>

           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Father Name</label> 
                   <input type="text" 
                      ng-model="newSupervisor.fatherName" 
                      class="form-control" 
                      placeholder = "Father Name"
                      name = 'fatherName'
                      required
                      is-name-only-valid
                      ng-minlength="3"
                      ng-maxlength="20"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="20"
                      ng-class = "{error: addSupervisorForm.fatherName.$invalid && !addSupervisorForm.fatherName.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "addSupervisorForm.fatherName.$error.minlength">* Name is too short</span>
           </div>

           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Designation</label> 
                   <input type="text" 
                      ng-model="newSupervisor.designation" 
                      class="form-control" 
                      placeholder = "Designation"
                      name = 'designation'
                      required
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      expect_special_char
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      ng-class = "{error: addSupervisorForm.designation.$invalid && !addSupervisorForm.designation.$pristine}">
               </div>
               <span class = 'hintModal' ng-show = "addSupervisorForm.name.$error.minlength">* Designation is too short</span>
           </div>

           <div class = "col-md-4 col-xs-12">
               <div>   
               <label>Supervisor's Mobile Number</label>
                    <input type="text" 
                           ng-model="newSupervisor.mobileNumber" 
                           class="form-control" 
                           placeholder = "Supervisor Mobile Number"
                           name = 'mobileNumber'
                           is-number-only-valid
                           required
                           minlength="6"
                           ng-minlength="6"
                           ng-maxlength="18"
                           oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                           maxlength="18"
                           ng-class = "{error: addSupervisorForm.mobileNumber.$invalid && !addSupervisorForm.mobileNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addSupervisorForm.mobileNumber.$error.maxlength">* In-valid Mobile Number</span>
           </div>
           
           <!-- <div class = "col-md-4 col-xs-12">
               <div>   
               <label>Vendor Name</label>
                      <select ng-model="newSupervisor.supervisorVendorName"
                             class="form-control marginBottom10" 
                             ng-options="allInspectionVendor.name for allInspectionVendor in allVendors track by allInspectionVendor.Id"
                             ng-change = "setVendor(newSupervisor.supervisorVendorName)"
                             >
                        <option value="">SELECT VENDOR</option>
                      </select>
                    
               </div>
               <span class = "hintModal" ng-show="addSupervisorForm.supervisorVendorName.$error.maxlength">* In-valid Vendor Name</span>
           </div> -->
       
           <div class = "col-md-4 col-xs-12">
           <label>Date of Birth</label>
             <div class = "input-group calendarInput">  
                 <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="dobCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newSupervisor.supervisordob" 
                      class="form-control" 
                      placeholder = "Date of Birth"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.supervisordob" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      max-date="maxdate"
                      required
                      readonly
                      name = 'supervisordob'
                      ng-class = "{error: addSupervisorForm.supervisordob.$invalid && !addSupervisorForm.supervisordob.$pristine}">
             </div>
           </div> 
             
           

           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Email Id</label>
                    <input type="email" 
                           ng-model="newSupervisor.email" 
                           class="form-control" 
                           placeholder = "Email Id"
                           name = 'email'
                           required
                           ng-class = "{error: addSupervisorForm.email.$invalid && !addSupervisorForm.email.$pristine}">
               </div>
               <span class = 'hintModal'  ng-show="addSupervisorForm.email.$error.email">* In-valid Email Address</span>
           </div>
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Gender</label>
                   <select ng-model="newSupervisor.gender"
                           class="form-control" 
                           ng-options="gender for gender in employeeGen"
                           ng-change = "setSupervisorgender(newSupervisor.gender)"
                           required>
                           <option value="">Select Gender</option>
                    </select>
               </div>
           </div>
          
          

          <div class = "col-md-4 col-xs-12">
               <div>
               <label>Temporary Addresss</label>
                   <textarea type="text" 
                          ng-model="newSupervisor.temporaryAddresss" 
                          class="form-control textArea_editEscort" 
                          placeholder = "Temporary Addresss"
                          name = 'temporaryAddresss'
                          rows="4" 
                          required
                          ng-minlength="10"
                          ng-maxlength="200"
                           minlength="10"
                          maxlength="200"
                          ng-class = "{error: addSupervisorForm.temporaryAddresss.$invalid && !addSupervisorForm.temporaryAddresss.$pristine}"></textarea>
               </div>
               <strong>Same as Permanent Address <input type="checkbox" name="" ng-model="setPermanentAddress" ng-change="applyPermanentAddress(setPermanentAddress,newSupervisor.temporaryAddresss)"></strong>
               
           </div>   

           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Permanent Addresss</label>
                   <textarea type="text" 
                          ng-model="newSupervisor.permanentAddress" 
                          class="form-control textArea_editEscort" 
                          placeholder = "Permanent Address"
                          name = 'permanentAddress'
                          rows="4" 
                          ng-minlength="10"
                          ng-maxlength="200"
                          maxlength="200"
                          minlength="10"
                          ng-disabled="permanentAddressDisabled"
                          ng-class = "{error: addSupervisorForm.permanentAddress.$invalid && !addSupervisorForm.permanentAddress.$pristine}"></textarea>
               </div>
               <span></span>
           </div> 
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Supervisor Employee Id</label>
                   <input type="text" 
                           ng-model="newSupervisor.supervisorEmployeeId" 
                           class="form-control" 
                           placeholder = "Supervisor Employee Id"
                           name = 'supervisorEmployeeId'
                           ng-minlength="2"
                           ng-maxlength="20"
                           oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                           maxlength="15"
                           required
                           ng-class = "{error: addSupervisorForm.supervisorEmployeeId.$invalid && !addSupervisorForm.supervisorEmployeeId.$pristine}">
               </div>

               <div>
               <label>Physically Challenged</label>
                   <select ng-model="newSupervisor.physicallyChallenged"
                           class="form-control" 
                           ng-options="physically for physically in physicallyChallenged"
                           ng-change = "setPhysicallyChallenged(newSupervisor.physicallyChallenged)"
                           required>
                           <option value="">Select Physically Challenged</option>
                    </select>
               </div>
              
           </div>
       </form>    
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-success" ng-click = "addSupervisor(newSupervisor)" ng-disabled="addSupervisorForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>