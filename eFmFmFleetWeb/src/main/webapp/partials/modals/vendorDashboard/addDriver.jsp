
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "addVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading floatLeft">CheckIn Driver & Vehicle With And Without Device</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body">
    <div class = "formWrapper">
       <form name = "addAdhocdriver">
           <div class = "col-md-4 col-xs-12"> 
              <div>
               <label>Select Vendor</label>
              </div>
            <select ng-model="adhocDrivers.selectVendor"
                    ng-options="vendorData.name for vendorData in vendorsData"
                    name = "selectVendor"
                    class = 'vendorSelect_invoiceByVendorForm'
                    required ng-change="vendorDatacheck(adhocDrivers.selectVendor)">                 
                <option value="">SELECT VENDOR</option>
            </select>

           </div>
           <div class = "col-md-4 col-xs-12">
              <div>
               <label>Vehicle Number</label>
              </div>
              <input type="text" 
                      ng-model="adhocDrivers.registerNumber"
                      class="form-control" 
                      placeholder = "Vehicle Registration Number"
                      name = 'registerNumber' 
                      ng-minlength="4"
                      ng-maxlength="20"
                      maxlength="20"
                      ng-change="vehicleNumCheck(adhocDrivers.registerNumber, adhocDrivers.selectVendor)"
                      expect_special_char
                      required
                      ng-disabled="adhocvehicleandDriver"
                      ng-class = "{error: addAdhocdriver.registerNumber.$invalid && !addAdhocdriver.registerNumber.$pristine}">
                      <span class="alertSpan" ng-show="failureMsg">* {{failureMsgdata}}</span>
           </div>

           <div class = "col-md-4 col-xs-12 inputSpin">
              <div>
               <label>Driver Mobile Number</label>
              </div>
              <input type="Number" 
                      ng-model="adhocDriverObj.driverMobileNo"
                      class="form-control" 
                      placeholder = "Enter Driver Mobile No"
                      name = 'driverMobileNo' 
                      ng-minlength="6"
                      ng-maxlength="18"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="18"
                      ng-change="driverNumCheck(adhocDriverObj.driverMobileNo, adhocDrivers.selectVendor)"
                      required
                      ng-disabled="adhocDriverpartMobile"
                      ng-class = "{error: addAdhocdriver.driverMobileNo.$invalid && !addAdhocdriver.driverMobileNo.$pristine}">
                      <span class="alertSpan" ng-show="failureMsgForDriver">* {{failureMsgdataForDriver}}</span>
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
                      ng-maxlength="30"
                      required
                      ng-disabled="true"
                      ng-class = "{error: addAdhocdriver.driverName.$invalid && !addAdhocdriver.driverName.$pristine}">
           </div> 
     <div class = "col-md-4 col-xs-12"> 
              <div>
               <label>Select Device</label>
              </div>
            <select ng-model="adhocDrivers.deviceId"
                    ng-options="deviceDetail.deviceId for deviceDetail in deviceDetailData"
                    name = "deviceId"
                    class = 'vendorSelect_invoiceByVendorForm'
                      ng-disabled="adhocDriverselectVendor">                 
                <option value="">NO DEVICE</option>
            </select>

           </div>
           <div class = "col-md-4 col-xs-12"></div> 
           
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success" ng-click = "adhocvehicleDriver(adhocDrivers, adhocDriverObj)" ng-disabled="addAdhocdriver.$invalid || failureMsg">CheckIn</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>