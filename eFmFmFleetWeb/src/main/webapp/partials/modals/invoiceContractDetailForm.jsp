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
<div class = "addContractFormModalTemplateLarge">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New Contract Details</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
       <form class = "addUserForm" name = "addContractForm">
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Contract Title</label>
                   <input type="text" 
                      ng-model="contract.contractTitle"
                      class="form-control" 
                      placeholder = "Contract Title"
                      name = 'contractTitle'  
                      ng-minlength="2"
                      ng-maxlength="30"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="30"
                      expect_special_char
                      required
                     > 
           </div> 
             <div class = "col-md-4 col-xs-12 form-group">
              <label>Start Date</label>
                    <div class = "input-group calendarInput calendarInput2">
                        <input type="text" 
                             ng-model="contract.startDate" 
                             id = "startDate"
                             class="form-control" 
                             name = "startDate"
                             placeholder="Select Start Date"
                             ng-change = "checkDateRangeValidity(startDate, endDate)"
                             datepicker-popup = 'dd-MM-yyyy'
                             is-open="datePicker.startDate"
                             show-button-bar = false
                             datepicker-options = 'dateOptions'
                             required
                             ng-click="startDate($event)"
                           >
                        <span class="input-group-btn"><button class="btn btn-default" ng-click="startDate($event)">
                            <i class = "icon-calendar calInputIcon"></i></button></span>
                       </div>
                </div>
                <div class = "col-md-4 col-xs-12 form-group">
                <label>End Date</label>
                    <div class = "input-group calendarInput calendarInput2">
                        <input type="text" 
                             ng-model="contract.endDate" 
                             class="form-control"
                             id = "endDate"
                             name='endDate'
                             placeholder="Select End Date"
                             datepicker-popup = 'dd-MM-yyyy'
                             min-date = "contract.startDate"
                             is-open="datePicker.endDate" 
                             show-button-bar = false
                             datepicker-options = 'dateOptions'
                             required
                             ng-click="endDate($event)"
                             >
                        <span class="input-group-btn"><button class="btn btn-default" ng-click="endDate($event)">
                            <i class = "icon-calendar calInputIcon"></i></button></span>
                       </div>
                </div>

        <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Contract Type</label>
            <select ng-model="contract.contractType" 
                ng-options="contract.contractType for contract in contractData"
                name = "selectVendor"
                ng-change="setContractType(contract.contractType)"
                class = 'vendorSelect_invoiceByVendorForm form-control'
                required 
               >                  
                <option value="">Contract Type</option>
            </select> 
           </div> 
        <div>

        <!-- Contract Type - Fixed Distance Contract --> 

        <div ng-show="contract.contractType.contractType != 'PDDC'">
        <div class = "col-md-4 col-xs-12 form-group" >               
              <label>Fixed Distance Monthly</label>
                   <input type="text" 
                      ng-model="contract.fixedDistanceChargeMonthly"
                      class="form-control" 
                      placeholder = "Fixed Distance Charge Monthly"
                      name = 'fixedcharMonthly' 
                      ng-minlength="1"
                      is-number-valid
                      ng-maxlength="8"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="8"
                      ng-change="setFixedDistanceMonthly(contract)"
                      ng-required ="contractDisabledFDC"
                      >  
        </div>   

        <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Fixed Distance Charge Rate</label>
                   <input type="text" 
                      ng-model="contract.fixedDistanceChargeRate"
                      class="form-control" 
                      placeholder = "Fixed Distance Charge Rate"
                      name = 'fixedcharRate' 
                      ng-change="setFixedDistanceChangeRate(contract)"
                      ng-minlength="1"
                      ng-maxlength="8"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="8"
                      is-number-valid
                      ng-class = "{error: addContractForm.fixedcharRate.$invalid && !addContractForm.fixedcharRate.$pristine}"
                      required> 
       </div> 

       <div class = "col-md-4 col-xs-12 form-group">            
              <label>Minimum days</label>
                   <input type="text" 
                      ng-model="contract.minimumDays"
                      class="form-control" 
                      placeholder = "Minimum days"
                      name = 'minimumDays'
                      ng-minlength="1"
                      ng-maxlength="2"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="2"
                      is-number-valid
                      ng-change="setFixedMinimumDays(contract)"
                      required
                      ng-class = "{error: addContractForm.minimumDays.$invalid && !addContractForm.minimumDays.$pristine}"> 
      </div>

      <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Extra Distance Charge Rate</label>
              <label></label>
                   <input type="text" 
                      ng-model="contract.extraDistanceChargeRate"
                      class="form-control" 
                      placeholder = "Extra Distance Charge Rate"
                      name = 'extracharRate' 
                      ng-minlength="1"
                      ng-maxlength="8"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="8"
                      is-number-valid
                      ng-required ="contractDisabledFDC"
                      required
                      ng-class = "{error: addContractForm.extracharRate.$invalid && !addContractForm.extracharRate.$pristine}"> 
      </div>      

          <div class = "col-md-4 col-xs-12 form-group">             
                  <label>Petrol Price</label>
                       <input type="text" 
                          ng-model="contract.petrolPrice"
                          class="form-control" 
                          placeholder = "Petrol Price"
                          name = 'petrolPrice' 
                          required
                          ng-minlength="1"
                          ng-maxlength="8"
                          oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                          maxlength="8"
                          is-number-valid
                          ng-class = "{error: addContractForm.petrolPrice.$invalid && !addContractForm.petrolPrice.$pristine}">  
          </div>       
           
           <div class = "col-md-4 col-xs-12 form-group">            
                <label>Per Day Cost</label>
                <input type = "text"  
                       class = "form-control"
                       ng-model = "contract.perDayCost"
                       ng-minlength="1"
                        ng-maxlength="8"
                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                        maxlength="8"
                        is-number-valid
                        required 
                       placeholder = "Per Day Cost (Rs.)"
                       name = "perDayCost"
                       ng-disabled="perDayCostDisabled"
                       ng-class = "{error: addContractForm.perDayCost.$invalid && !addContractForm.perDayCost.$pristine}">
                   
           </div>

            <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Per Km Charge Rate</label>
              <label></label>
                   <input type="text" 
                      ng-model="contract.perKmCost"
                      class="form-control" 
                      placeholder = "Per Km Charge Rate"
                      name = 'extracharRate' 
                      ng-minlength="1"
                      ng-maxlength="8"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="8"
                      is-number-valid
                      required
                      ng-disabled="perKmChargeDisabled"
                      ng-class = "{error: addContractForm.extracharRate.$invalid && !addContractForm.extracharRate.$pristine}"> 
           </div>   

           <div class = "col-md-4 col-xs-12 form-group">            
              <label>Penalty</label>
                   <select ng-model="contract.penalty"
                                class="form-control" 
                                ng-options="penalty.name for penalty in allPenalty "
                                ng-change = "setPenalty(contract)"
                                ng-class = "{error: addContractForm.penalty.$invalid && !addContractForm.penalty.$pristine}">
                                <option value="">--Select Penalty--</option>
               </select>
           </div>                       
           
           <div class = "col-md-4 col-xs-12 form-group">            
                <label>Penalty in %</label>
                <input type = "text"  
                       class = "form-control"
                       ng-model = "contract.penaltyPercent"
                       ng-minlength="1"
                       ng-maxlength="4"
                       maxlength="4"
                       is-number-valid
                       placeholder = "Penalty in %"
                       ng-required = "contract.penalty.name == 'YES'"
                       ng-disabled = "contract.penalty.name == 'NO'"
                       name = "penaltyPercentage"
                       ng-class = "{error: addContractForm.penaltyPercentage.$invalid && !addContractForm.penaltyPercentage.$pristine}">                   
           </div>


          <div class = "col-md-4 col-xs-12 form-group"> 
                <label>Fuel Contract Type</label>
                    <select ng-model="contract.contractDescription" 

                       ng-options="contract.contractDescription for contract in fuelContractType"
                      name = "selectVendor"
                      class = 'vendorSelect_invoiceByVendorForm form-control'
                      required 
                      >                
                      <option value="">Select Fuel Contract Type</option>
                </select> 
          </div> 
        </div>

        <!-- Contract Type - Per Day Distance Contract --> 

        <div ng-show="contract.contractType.contractType == 'PDDC'">

        <div class = "col-md-4 col-xs-12 form-group">            
                <label>Per Day Cost</label>
                <input type = "text"  
                       class = "form-control"
                       ng-model = "contract.perDayCost"
                       ng-minlength="1"
                       ng-maxlength="8"
                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                       maxlength="8"
                       is-number-valid
                       placeholder = "Per Day Cost (Rs.)"
                       name = "perDayCost"
                       ng-change="setDayCostPDDC(contract)"
                       ng-class = "{error: addContractForm.perDayCost.$invalid && !addContractForm.perDayCost.$pristine}">
                   
        </div>

        <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Per Km Charge Rate</label>
              <label></label>
                   <input type="text" 
                      ng-model="contract.perKmCost"
                      class="form-control" 
                      placeholder = "Per Km Charge Rate"
                      name = 'extracharRate' 
                      ng-minlength="1"
                      ng-maxlength="8"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="8"
                      is-number-valid
                      required
                      ng-class = "{error: addContractForm.extracharRate.$invalid && !addContractForm.extracharRate.$pristine}"> 
        </div>

       <div class = "col-md-4 col-xs-12 form-group">            
              <label>Minimum days</label>
                   <input type="text" 
                      ng-model="contract.minimumDays"
                      class="form-control" 
                      placeholder = "Minimum days"
                      name = 'minimumDays'
                      ng-minlength="1"
                      ng-maxlength="3"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="3"
                      is-number-valid
                      ng-change="setMinimumDays(contract)"
                      required
                      ng-class = "{error: addContractForm.minimumDays.$invalid && !addContractForm.minimumDays.$pristine}"> 
       </div>

        <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Fixed Distance Charge Rate</label>
                   <input type="text" 
                      ng-model="contract.fixedDistanceChargeRate"
                      class="form-control" 
                      placeholder = "{{distanceChargeRate}}"
                      name = 'fixedcharRate' 
                      ng-minlength="1"
                      ng-maxlength="8"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="8"
                      required
                      is-number-valid
                      ng-disabled="fixedDistanceDisabled"
                      ng-class = "{error: addContractForm.fixedcharRate.$invalid && !addContractForm.fixedcharRate.$pristine}"> 
       </div> 

         <div class = "col-md-4 col-xs-12 form-group">             
                  <label>Petrol Price</label>
                       <input type="text" 
                          ng-model="contract.petrolPrice"
                          class="form-control" 
                          placeholder = "Petrol Price"
                          name = 'petrolPrice' 
                          required
                          ng-minlength="1"
                          ng-maxlength="8"
                          oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                          maxlength="8"
                          is-number-valid
                          ng-class = "{error: addContractForm.petrolPrice.$invalid && !addContractForm.petrolPrice.$pristine}">  
          </div>       

           <div class = "col-md-4 col-xs-12 form-group">            
              <label>Penalty</label>
                   <select ng-model="contract.penalty"
                                class="form-control" 
                                ng-options="penalty.name for penalty in allPenalty "
                                ng-change = "setPenalty(contract)"
                                ng-class = "{error: addContractForm.penalty.$invalid && !addContractForm.penalty.$pristine}">
                                <option value="">--Select Penalty--</option>
               </select>
           </div>                       
           
           <div class = "col-md-4 col-xs-12 form-group">            
                <label>Penalty in %</label>
                <input type = "text"  
                       class = "form-control"
                       ng-model = "contract.penaltyPercent"
                       ng-minlength="1"
                       ng-maxlength="4"
                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                       maxlength="4"
                       is-number-valid
                       placeholder = "Penalty in %"
                       ng-required = "contract.penalty.name == 'YES'"
                       ng-disabled = "contract.penalty.name == 'NO'"
                       name = "penaltyPercentage"
                       ng-class = "{error: addContractForm.penaltyPercentage.$invalid && !addContractForm.penaltyPercentage.$pristine}">                   
           </div>


          <div class = "col-md-4 col-xs-12 form-group"> 
                <label>Fuel Contract Type</label>
                    <select ng-model="contract.contractDescription" 

                       ng-options="contract.contractDescription for contract in fuelContractType"
                      name = "selectVendor"
                      class = 'vendorSelect_invoiceByVendorForm form-control'
                      required 
                      >                
                      <option value="">Select Fuel Contract Type</option>
                </select> 
          </div> 
        </div> 
      </div>
       
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "addContract(contract)" ng-disabled= "addContractForm.$invalid">Add New Contract</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>