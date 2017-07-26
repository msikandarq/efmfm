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
<div class = "newVehicleFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New Vehicle</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "addVehicleForm">
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Vehicle Make</label>
                    <input type="text" 
                      ng-model="newVehicle.vehicleName" 
                      class="form-control" 
                      placeholder = "Vehicle Make"
                      name = 'vehicleName'
                      required
                      expect_special_char
                      ng-minlength="0"
                      ng-maxlength="15"
                      maxlength="15"
                      ng-class = "{error: addVehicleForm.vehicleName.$invalid && !addVehicleForm.vehicleName.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.vehicleName.$error.maxlength">Invalid </span>
           </div>
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Vehicle Model</label>
                    <input type="text" 
                           ng-model="newVehicle.pollutionCert" 
                           class="form-control" 
                           placeholder = "Vehicle Model"
                           name = 'pollutionCert'
                           required
                           ng-minlength="0"
                           ng-maxlength="30"
                           maxlength="30"
                           expect_special_char
                           ng-class = "{error: addVehicleForm.pollutionCert.$invalid && !addVehicleForm.pollutionCert.$pristine}"> 
               </div>      
               <span class = "hintModal" ng-show="addVehicleForm.pollutionCert.$error.maxlength">* In-valid Vehicle model Number</span>
           </div>
           
         <div class = "col-md-4 col-xs-12">
               <div>
               <label>Vehicle Model Year</label>
                    <input type="text" 
                           id = "modelYear"
                           ng-model="newVehicle.modelYear" 
                           class="form-control" 
                           placeholder = "Vehicle Model Year [YYYY]"
                           name = 'InsuranceComp'
                           required
                           ng-change="vehicleModelYearCheck(newVehicle.modelYear)"
                           is-number-only-valid
                           ng-minlength="0"
                           ng-maxlength="4"
                           maxlength="4"
                           ng-class = "{error: addVehicleForm.InsuranceComp.$invalid && !        addVehicleForm.InsuranceComp.$pristine}">
                            <span class="alertSpan" ng-show="invalidYear">* {{modelFailureMsg}}</span>
               </div>
               <span class="hintModal" ng-show="addVehicleForm.InsuranceComp.$error.maxlength">Invalid         Year</span>
               </div>
            
            <div class = "col-md-4 col-xs-12">
               <div>
               <label>Vehicle Number</label>
                   <input type="text" 
                          ng-model="newVehicle.vehicleNumber" 
                          class="form-control" 
                          placeholder = "Vehicle Number"
                          name = 'vehicleNumber'
                          required
                          ng-minlength="0"
                          ng-maxlength="20"
                          maxlength="20"
                          expect_special_char
                          ng-class = "{error: addVehicleForm.vehicleNumber.$invalid && !addVehicleForm.vehicleNumber.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.vehicleNumber.$error.maxlength"> Invalid Vehicle Number </span>
           </div> 
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Vehicle Engine Number</label>
                    <input type="text" 
                           ng-model="newVehicle.contactNo" 
                           class="form-control" 
                           placeholder = "Vehicle Engine Number"
                           name = 'contactNumber'
                           required
                           ng-minlength="0"
                           ng-maxlength="15"
                           maxlength="15"
                           expect_special_char
                           ng-class = "{error: addVehicleForm.contactNumber.$invalid && !addVehicleForm.contactNumber.$pristine}">               
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.contactNumber.$error.maxlength">* In-valid Vehicle Engine Number</span>
           </div>        
          
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Select Seats</label>
                    <input type="text" 
                          ng-model="newVehicle.capacity" 
                          class="form-control" 
                          placeholder = "Vehicle Capacity Number"
                          name = 'selectSeats'
                          required
                          ng-minlength="0"
                          ng-maxlength="3"
                          maxlength="3"
                          is-number-only-valid
                          ng-class = "{error: addVehicleForm.selectSeats.$invalid && !addVehicleForm.selectSeats.$pristine}"> 
                          
                    <!-- <select ng-model="newVehicle.capacity" 
                        ng-options="selectSeat.text for selectSeat in selectSeats track by selectSeat.value"
                        class = "selectSeat_newVehicleForm"
                        name = "selectSeats"
                        required
                        ng-class = "{error: addVehicleForm.selectSeats.$invalid && !addVehicleForm.selectSeats.$pristine}">                 
                        <option value="">-- Select Seats --</option>
                    </select> -->
               </div>
      </div>
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Registration Certificate Number</label>
                    <input type="text" 
                           ng-model="newVehicle.regCert" 
                           class="form-control" 
                           placeholder = "Registration Certificate"
                           name = 'regCert'
                           required
                           ng-minlength="0"
                           ng-maxlength="30"
                           maxlength="30"
                           expect_special_char
                           ng-class = "{error: addVehicleForm.regCert.$invalid && !addVehicleForm.regCert.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.regCert.$error.maxlength">* In-valid Registration Number</span>
           </div>  
           
           <div class = "col-md-4 col-xs-12">
           <label>Registration Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="registrationDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.registrationDate" 
                      class="form-control" 
                      placeholder = "Registration Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.registrationDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'registrationDate'
                      required
                      readonly
                      ng-class = "{error: addVehicleForm.registrationDate.$invalid && !addVehicleForm.registrationDate.$pristine}">            
             </div>
           </div>

           <div class = "col-md-4 col-xs-12">
           <label>Vehicle Maintenance Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="vehicleMaintenacedateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.vehicleMaintenaceDate" 
                      class="form-control" 
                      placeholder = "Vehicle Maintenance Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.vehicleMaintenaceDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'vehicleMaintenaceDate'
                      required
                      readonly
                      ng-class = "{error: addVehicleForm.vehicleMaintenaceDate.$invalid && !addVehicleForm.vehicleMaintenaceDate.$pristine}">            
             </div>
           </div>

           
           <div class = "col-md-4 col-xs-12">
           <label>Pollution Expiry Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="pollutionExpDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.pollutionExpDate" 
                      class="form-control" 
                      placeholder="Pollution Expiry Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.pollutionExpDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      required
                      readonly
                      name = 'pollutionExpDate'
                      ng-class = "{error: addVehicleForm.pollutionExpDate.$invalid && !addVehicleForm.pollutionExpDate.$pristine}">               
             </div>
           </div>   
           
           <div class = "col-md-4 col-xs-12">
           <label>Insurance Expiry Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="InsuranceExpiryCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.InsuranceDate" 
                      class="form-control" 
                      placeholder = "Insurance Expiry Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.InsuranceExpiryDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'InsuranceExpiry'
                      required
                      readonly
                      ng-class = "{error: addVehicleForm.InsuranceExpiry.$invalid && !addVehicleForm.InsuranceExpiry.$pristine}">
            
             </div>
           </div> 
           
           <div class = "col-md-4 col-xs-12">
           <label>Tax Valid</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="TaxValidCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.TaxValid" 
                      class="form-control" 
                      placeholder = "Tax Valid"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.TaxValidDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'TaxValid'
                      required
                      readonly
                      ng-class = "{error: addVehicleForm.TaxValid.$invalid && !addVehicleForm.TaxValid.$pristine}">
            
             </div>
           </div>   
           <div class = "col-md-4 col-xs-12">
           <label>Permit Valid</label>
             <div class = "input-group calendarInput">
                  <span class="input-group-btn">
                      <button class="btn btn-default" ng-click="PermitValidCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.PermitValid" 
                      class="form-control" 
                      placeholder = "Permit Valid"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.PermitValidDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'PermitValid'
                      required
                      readonly
                      ng-class = "{error: addVehicleForm.PermitValid.$invalid && !addVehicleForm.PermitValid.$pristine}">
               
             </div>
           </div>
           
           <div class = "col-md-4 col-xs-12">
           <label>Vehicle Fitness Certification Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="vehicleFitnessCertCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="newVehicle.vehicleFitnessCert" 
                      class="form-control" 
                      placeholder = "Vehicle Fitness Certification Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.vehicleFitnessCert" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'vehicleFitnessCert'
                      required
                      readonly
                      ng-class = "{error: addVehicleForm.vehicleFitnessCert.$invalid && !addVehicleForm.vehicleFitnessCert.$pristine}">            
             </div>
           </div>  
       
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Vehicle Contact Person</label>
                    <input type="text" 
                           ng-model="newVehicle.contactName" 
                           class="form-control" 
                           placeholder = "Contact Person"
                           name = 'contactName'
                           required
                           is-name-only-valid
                           ng-minlength="3"
                           ng-maxlength="20"
                           maxlength="20"
                           ng-class = "{error: addVehicleForm.contactName.$invalid && !addVehicleForm.contactName.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.contactName.$error.pattern">* Enter Only Alphabets</span>
               <span class = "hintModal" ng-show="addVehicleForm.contactName.$error.minlength">* Contact Name is Too Short..</span>
               <span class = "hintModal" ng-show="addVehicleForm.contactName.$error.maxlength">* In-valid Contact Name</span>
           </div>    
           
           <div class = "col-md-4 col-xs-12">


               <div>
               <label>Contract Type</label>

               <select ng-model="newVehicle.conType" 
                  ng-options="contract.contractName for contract in allContractType track by contract.contractId"
                    name = "contract"
                    class = 'selectSeat_editVehicleForm'
                    required
                    ng-class = "{error: addVehicleForm.contract.$invalid && !addVehicleForm.contract.$pristine}">                 
                <option value="">-- SELECT CONTRACT TYPE --</option>
              </select> 

<!--                     <input type="text" 
                           ng-model="newVehicle.conType" 
                           class="form-control" 
                           placeholder = "Contract Type"
                           name = 'conType'
                           required
                           ng-minlength="0"
                           ng-maxlength="30"
                           ng-class = "{error: addVehicleForm.conType.$invalid && !addVehicleForm.conType.$pristine}"> -->
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.conType.$error.maxlength">* In-valid contractType</span>
           </div>
           
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Contract Tariff Id</label>
                    <input type="text" 
                           ng-model="newVehicle.conTariffId" 
                           class="form-control" 
                           placeholder = "Contract Tariff Id"
                           name = 'conTariffId'
                           required
                           is-number-only-valid
                           ng-minlength="0"
                           ng-maxlength="2"
                           maxlength="2"
                           expect_special_char
                           ng-class = "{error: addVehicleForm.conTariffId.$invalid && !addVehicleForm.conTariffId.$pristine}">
               </div>
               <span class = "hintModal" ng-show="addVehicleForm.conTariffId.$error.pattern">* Enter Only Numbers</span>
               <span class = "hintModal" ng-show="addVehicleForm.conTariffId.$error.maxlength">* In-valid Contract Tariff Id</span>
           </div>
            <div class = "col-md-4 col-xs-12">
                <label for = "mileage">Mileage</label>
                   <input type="number" 
                          ng-model="newVehicle.mileage" 
                          class="form-control" 
                          placeholder = "Mileage"
                          name = 'mileage' 
                          ng-minlength="0"
                           ng-maxlength="2"
                           maxlength="2"
                          expect_special_char
                          required
                          ng-class = "{error: addVehicleForm.mileage.$invalid && !addVehicleForm.mileage.$pristine}">
           </div>
   <!--         <div class = "col-md-4 col-xs-12">
                <label for = "vehicleNum">This Vehicle is Replacement of </label>
                   <input type="text" 
                          ng-model="editVehicle.isReplacement" 
                          class="form-control" 
                          placeholder = "This Vehicle is replacement of.."
                          name = 'replacementVehicle'
                          ng-minlength="0"
                          ng-maxlength="15"
                          expect_special_char
                          required
                          ng-class = "{error: editVehicleForm.replacementVehicle.$invalid && !editVehicleForm.replacementVehicle.$pristine}">
           </div> -->
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-info" ng-click = "addVehicle(newVehicle)" ng-disabled="addVehicleForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>