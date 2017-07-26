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
<div class = "editVehicleFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Vehicle</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "editVehicleForm" class = "editVehicleForm">
           
           <div class = "col-md-4 col-xs-12">
               <label for = "vehicleName">Vehicle Make</label>
               <input type="text" 
                      ng-model="editVehicle.vehicleName" 
                      class="form-control" 
                      placeholder = "Vehicle Make"
                      name = 'vehicleName'
                      required
                      ng-minlength="0"
                      ng-maxlength="15"
                      expect_special_char
                      ng-class = "{error: editVehicleForm.vehicleName.$invalid && !editVehicleForm.vehicleName.$pristine}">
               
           </div>
               <div class = "col-md-4 col-xs-12">
               <label for = "insurName">Vehicle Model</label>
               <input type="text" 
                          ng-model="editVehicle.vehicleModel" 
                          class="form-control" 
                          placeholder = "Vehicle Model"
                          name = 'InsuranceComp'
                          required
                          expect_special_char
                          ng-minlength="0"
                          ng-maxlength="30"
                          ng-class = "{error: editVehicleForm.InsuranceComp.$invalid && !editVehicleForm.InsuranceComp.$pristine}">
               <span></span>
           </div> 
           <div class = "col-md-4 col-xs-12">
               <label for = "InsuranceNumber">Vehicle Model Year</label>
                   <input type="text" 
                          ng-model="editVehicle.modelYear" 
                          class="form-control" 
                          placeholder = "Vehicle Model Year"
                          name = 'InsuranceNumber'
                          required
                          expect_special_char
                          ng-minlength="0"
                          ng-maxlength="4"
                          ng-class = "{error: editVehicleForm.InsuranceNumber.$invalid && !editVehicleForm.InsuranceNumber.$pristine}">
               <span></span>
           </div> 
           <div class = "col-md-4 col-xs-12">
                <label for = "vehicleNum">Vehicle Number</label>
                   <input type="text" 
                          ng-model="editVehicle.vehicleNumber" 
                          class="form-control" 
                          placeholder = "Vehicle Number"
                          name = 'vehicleNumber'
                          required
                          expect_special_char
                          ng-minlength="0"
                          ng-maxlength="15"
                          ng-class = "{error: editVehicleForm.vehicleNumber.$invalid && !editVehicleForm.vehicleNumber.$pristine}">
           </div>   
           <div class = "col-md-4 col-xs-12">
               <label for = "vehicleContact">Vehicle Engine Number</label>
               <input type="text" 
                          ng-model="editVehicle.contactNo" 
                          class="form-control" 
                          placeholder = "Vehicle Engine Number"
                          name = 'contactNumber'
                          required
                          expect_special_char
                          ng-minlength="0"
                          ng-maxlength="50"
                          ng-class = "{error: editVehicleForm.contactNumber.$invalid && !editVehicleForm.contactNumber.$pristine}">
           </div>  
           <div class = "col-md-4 col-xs-12">
               <label for = "vehicleCapacity">Vehicle Capacity</label>
               <input type="number" 
                          ng-model="editVehicle.capacity" 
                          class="form-control" 
                          placeholder = "Vehicle Capacity Number"
                          name = 'selectSeats'
                          required
                          expect_special_char
                          ng-minlength="0"
                          ng-maxlength="50"
                          ng-class = "{error: editVehicleForm.selectSeats.$invalid && !editVehicleForm.selectSeats.$pristine}"> 
        <!-- <select ng-model="editVehicle.capacity" 
                  ng-options="selectSeat.text for selectSeat in selectSeats track by selectSeat.value"
                    name = "selectSeats"
                    class = 'selectSeat_editVehicleForm'
                    required
                    ng-class = "{error: editVehicleForm.selectSeats.$invalid && !editVehicleForm.selectSeats.$pristine}">                 
              <option value="">-- Select Seats --</option>
          </select> -->
           </div>
            
           <div class = "col-md-4 col-xs-12">
               <label for = "vehicleCapacity">Contract Type</label>
               <select ng-model="editVehicle.conType" 
                  ng-options="contract.contractName for contract in contractList"
                    name = "contract"
                    class = 'selectSeat_editVehicleForm'
                    required
                    ng-class = "{error: editVehicleForm.contract.$invalid && !editVehicleForm.contract.$pristine}">                 
              <option value="">-- SELECT CONTRACT TYPE --</option>
          </select>               
           </div>
           <div class = "col-md-4 col-xs-12">
               <label for = "vehicleCapacity">Vehicle Registration Number</label>
               <input type="text" 
                          ng-model="editVehicle.regCert" 
                          class="form-control" 
                          placeholder = "Registration Certificate"
                          name = 'regCert'
                          required
                          expect_special_char
                          ng-minlength="0"
                          ng-maxlength="50"
                          ng-class = "{error: editVehicleForm.regCert.$invalid && !editVehicleForm.regCert.$pristine}">
               <span class = "hintModal" ng-show="editVehicleForm.regCert.$error.maxlength">* In-valid Registration Number</span>
           </div> 
           
           <div class = "col-md-4 col-xs-12">
           <label>Registration Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="registrationDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="editVehicle.registrationDate" 
                      class="form-control" 
                      placeholder = "Registration Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.registrationDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'registrationDate'
                      required
                      readonly
                      ng-class = "{error: editVehicleForm.registrationDate.$invalid && !editVehicleForm.registrationDate.$pristine}">            
             </div>
           </div>

           <div class = "col-md-4 col-xs-12">
           <label>Vehicle Maintenance Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="vehicleMaintenanceDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="editVehicle.vehicleMaintenanceDate" 
                      class="form-control" 
                      placeholder = "Vehicle Maintenance Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.vehicleMaintenanceDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'vehicleMaintenanceDate'
                      required
                      readonly
                      ng-class = "{error: editVehicleForm.vehicleMaintenanceDate.$invalid && !editVehicleForm.vehicleMaintenanceDate.$pristine}">            
             </div>
           </div>
          
           <div class = "col-md-4 col-xs-12">
               <label for = "pollutionexDate">Pollution Expiry Date</label>
               <div class = "input-group calendarInput">
                   <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="pollutionExpDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                <input type="text" 
                       ng-model="editVehicle.pollutionExpDate" 
                       class="form-control" 
                       placeholder="Pollution Expiry Date"
                       datepicker-popup = '{{format}}'
                       is-open="datePicker.pollutionExpDate" 
                       show-button-bar = false
                       datepicker-options = 'dateOptions'
                       required
                       name = 'pollutionExpDate'
                       ng-class = "{error: editVehicleForm.pollutionExpDate.$invalid && !editVehicleForm.pollutionExpDate.$pristine}">
                
               </div>
           </div>         
       
           <div class = "col-md-4 col-xs-12">
             <label for = "InsuranceExpiryDate">Insurance Expiry Date</label> 
             <div class = "input-group calendarInput">
                 <span class="input-group-btn">
                     <button class="btn btn-default" ng-click="InsuranceExpiryCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                 <input type="text" 
                      ng-model="editVehicle.InsuranceDate" 
                      class="form-control" 
                      placeholder = "Insurance Expiry Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.InsuranceExpiryDate" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'InsuranceExpiry'
                      required
                      ng-class = "{error: editVehicleForm.InsuranceExpiry.$invalid && !editVehicleForm.InsuranceExpiry.$pristine}">
                
             </div>
           </div> 
           
           <div class = "col-md-4 col-xs-12">
           <label>Vehicle Fitness Certification Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="vehicleFitnessCertCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="editVehicle.vehicleFitnessCert" 
                      class="form-control" 
                      placeholder = "Vehicle Fitness Certification Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.vehicleFitnessCert" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'vehicleFitnessCert'
                      required
                      readonly
                      ng-class = "{error: editVehicleForm.vehicleFitnessCert.$invalid && !editVehicleForm.vehicleFitnessCert.$pristine}">            
             </div>
           </div>          
           
           <div class = "col-md-4 col-xs-12">
               <label for = "PermitValid">Permit Valid Date</label> 
               <div class = "input-group calendarInput">
                   <span class="input-group-btn">
                       <button class="btn btn-default" ng-click="PermitValidCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                 <input type="text" 
                        ng-model="editVehicle.PermitValid" 
                        class="form-control" 
                        placeholder = "Permit Valid"
                        datepicker-popup = '{{format}}'
                        is-open="datePicker.PermitValidDate" 
                        show-button-bar = false
                        datepicker-options = 'dateOptions'
                        name = 'PermitValid'
                        required
                        ng-class = "{error: editVehicleForm.PermitValid.$invalid && !editVehicleForm.PermitValid.$pristine}">
                  
               </div>
           </div> 
           
             <div class = "col-md-4 col-xs-12">
                <label for = "TaxValid">Tax Valid Date</label>
                <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                        <button class="btn btn-default" ng-click="TaxValidCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                   <input type="text" 
                          ng-model="editVehicle.TaxValid" 
                          class="form-control" 
                          placeholder = "Tax Valid"
                          datepicker-popup = '{{format}}'
                          is-open="datePicker.TaxValidDate" 
                          show-button-bar = false
                          datepicker-options = 'dateOptions'
                          name = 'TaxValid'
                          required
                          ng-class = "{error: editVehicleForm.TaxValid.$invalid && !editVehicleForm.TaxValid.$pristine}">
                
                 </div>
           </div>
           <div class = "col-md-4 col-xs-12 hidden">
               <input type="text" ng-model="editVehicle.numCabs" class="form-control">
           </div> 
           <div class = "col-md-4 col-xs-12 hidden">
               <input type="text" ng-model="editVehicle.vehicleId" class="form-control">
           </div>
           
           <div class = "col-md-4 col-xs-12">
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
           </div> 

            <div class = "col-md-4 col-xs-12">
                <label for = "mileage">Mileage</label>
                   <input type="number" 
                          ng-model="editVehicle.mileage" 
                          class="form-control" 
                          placeholder = "Mileage"
                          name = 'mileage' 
                          expect_special_char
                          required
                          ng-class = "{error: editVehicleForm.mileage.$invalid && !editVehicleForm.mileage.$pristine}">
           </div> 
          
     <div class = "col-md-4 col-xs-4">
           <label for = "driveraddress">Vehicle Documents</label>
           <div class = "">
                <div class = "floatLeft imageDiv_approval imageBorder" id = "div{{$index}}" 
                     ng-repeat = "doc in editVehicle.documents"
                     ng-show = "doc.location">

                   <a href="{{doc.location}}" class = "noLinkLine" download>
                        <img class="img-responsive img-rounded fileImg" 
                              ng-src = "{{doc.location}}">

                    </a>
                </div>
           </div>
       </div>     

        <div class = "col-md-4 col-xs-12 buttonAlignmentEditVehicle">
          <input type="button" class="btn btn-primary" ng-click = "viewAllDocumentsVehicle(editVehicle, $parent.$index, $index, 'lg')" value="View Vehicle Documents" name="" ng-disabled="editVehicle.documents.length == 0"> 
       </div>  

       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-warning" ng-click = "updateVehicle(editVehicle)" ng-disabled="editVehicleForm.$invalid ">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>