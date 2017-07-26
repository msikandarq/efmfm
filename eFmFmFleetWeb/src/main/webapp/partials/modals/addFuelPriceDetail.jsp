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
<div class = "addContractFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add Fuel Price Details</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
       <form class = "addUserForm" name = "addFuelForm">
            <div class = "col-md-6 col-xs-12 form-group"> 
                <label>Contract Title</label>
                      <input type="text" 
                             ng-model="fuel.contactTitle"
                             class="form-control" 
                             placeholder = "Contract Title"
                             name = 'contactTitle'
                             ng-minlength="2"
                             ng-maxlength="30"
                             maxlength="30"
                             expect_special_char 
                             required
                      >
            </div> 
       
            <div class = "col-md-6 col-xs-12 form-group"> 
                <label>Select Date</label>
                      <div class = "input-group calendarInput">
                            <span class="input-group-btn"><button class="btn btn-default" ng-click="startDateFuel($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                             <input type="text" 
                                    ng-model="fuel.selectDate" 
                                    class="form-control" 
                                    placeholder = "Select Date"
                                    datepicker-popup = '{{format}}'
                                    is-open="datePicker.startDate" 
                                    show-button-bar = false
                                    datepicker-options = 'dateOptions'
                                    required
                                    ng-click="startDateFuel($event)"
                                    name = 'startDate'
                                    readonly
                                    ng-disabled="myForm.$invalid"
                                    
                                    >
                     </div>
           </div> 
           
          <div class = "col-md-6 col-xs-12 form-group"> 
                <label>Contract Type</label>
                      <!-- <select name="singleSelect" 
                              class="vendorSelect_invoiceByVendorForm form-control-contract" 
                              id="singleSelect" 
                              ng-model="fuel.fuelType">
                      <option value="">---Contract Type---</option> 
                      <option value="K">Km / Mileage</option> 
                      <option value="M">Month</option>
                      </select> -->

                      <select ng-model="fuel.contractType" 
                ng-options="fuel.fuelContractId as fuel.contractDescription for fuel in fuelContractType"
                name = "selectVendor"
                class = 'vendorSelect_invoiceByVendorForm form-control'
                required 
                 
               >                  
                <option value="">Contract Type</option>
            </select>
          </div> 

          <div class = "col-md-6 col-xs-12 form-group" ng-show="fuel.contractType == 1 || fuel.contractType == 2" > 
               <label>New Price</label>
                     <input type="text" 
                        ng-model="fuel.newPrice"
                        class="form-control" 
                        placeholder = "New Price"
                        name = 'newPrice' 
                        ng-minlength="1"
                        ng-maxlength="15"
                        maxlength="15"
                        required 
                        only-num
                       > 
                   
           </div> 
        <!--   <div class = "col-md-6 col-xs-12 form-group"> 
               <label>Old Price</label>
                     <input type="text" 
                        ng-model="fuel.oldPrice"
                        class="form-control" 
                        placeholder = "Old Price"
                        name = 'oldPrice' 
                        ng-minlength="1"
                        ng-maxlength="15"
                        readonly 
                       > 
                   
           </div>  -->
           <div class = "col-md-6 col-xs-12 form-group" ng-if="fuel.contractType == 2"> 
              <label>Month Amount</label>
                     <input type="text" 
                        ng-model="fuel.monthAmount"
                        class="form-control" 
                        placeholder = "Month Amount"
                        name = 'monthAmount' 
                        ng-minlength="1"
                        ng-maxlength="15"
                        maxlength="15"
                        required 
                        only-num
                       
                       >
           </div> 
            <div class = "col-md-6 col-xs-12 form-group"> 
              <label>Fuel Type</label>
                   <select name="singleSelect" 
                              class="vendorSelect_invoiceByVendorForm form-control-contract" 
                              id="singleSelect" 
                              ng-model="fuel.fuelType"
                                required>
                      <option value="">---Fuel Type---</option> 
                      <option value="petrol">Petrol </option> 
                      <option value="diesel">Diesel</option>
                      <option value="cng">CNG</option>
                      <option value="lpg">LPG</option>
                      </select>
           </div> 
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "addFuel(fuel)" ng-disabled= "addFuelForm.$invalid">Add Fuel Price</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>