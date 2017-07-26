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
<div class = "releaseDriverFormModalTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Planned Release Drivers</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "releaseDriverForm">  
           
           
           <div class = "col-md-12 col-xs-12">
               <div>   
               <label>Vendor Name</label>
                    <input type="text" 
                           ng-model="driver.vendorName" 
                           class="form-control" 
                           placeholder = "Vendor name"
                           name = 'vendorName'
                           required
                           readonly
                           maxlength = '20'
                           ng-class = "{error: releaseDriverForm.vendorName.$invalid && !releaseDriverForm.vendorName.$pristine}">
               </div>
           </div>
           
           <div class = "col-md-6 col-xs-12">
               <div>
               <label>Driver Name</label> 
                   <input type="text" 
                      ng-model="driver.driverName" 
                      class="form-control" 
                      placeholder = "Driver Name"
                      name = 'name'
                      required
                      ng-minlength="2"
                      ng-maxlength="15"
                      ng-class = "{error: releaseDriverForm.name.$invalid && !releaseDriverForm.name.$pristine}"
                      readonly>
               </div>
           </div>
             
           <div class = "col-md-6 col-xs-12">
               <div>   
               <label>Vehicle Number</label>
                    <input type="text" 
                           ng-model="driver.vehicleNumber" 
                           class="form-control" 
                           placeholder = "Vehicle Number"
                           name = 'vehicleNumber'
                           required
                           ng-class = "{error: releaseDriverForm.vehicleNumber.$invalid && !releaseDriverForm.vehicleNumber.$pristine}"
                           readonly>
               </div>
           </div>
       
           <div class = "col-md-6 col-xs-12">
           <label>Release From</label>
             <div class = "input-group calendarInput">  
                 <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="fromCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="driver.fromDate" 
                      class="form-control" 
                      placeholder = "Release From Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.fromDate" 
                      show-button-bar = false
                      min-date = "todaysDate"
                      datepicker-options = 'dateOptions'
                      required
                      readonly
                      name = 'from'
                      ng-class = "{error: releaseDriverForm.from.$invalid && !releaseDriverForm.from.$pristine}">
             </div>
           </div> 
       
           <div class = "col-md-6 col-xs-12">
           <label>Release To</label>
             <div class = "input-group calendarInput">  
                 <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="toCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="driver.toDate" 
                      class="form-control" 
                      placeholder = "Release To Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.toDate" 
                      min-date = "driver.fromDate"
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      required
                      readonly
                      name = 'from'
                      ng-class = "{error: releaseDriverForm.from.$invalid && !releaseDriverForm.from.$pristine}">
             </div>
           </div> 
             
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-success" ng-click = "release(driver)" ng-disabled="releaseDriverForm.$invalid">Release Driver</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>