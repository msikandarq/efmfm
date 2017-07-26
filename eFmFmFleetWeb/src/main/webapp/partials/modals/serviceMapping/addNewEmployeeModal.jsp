<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "manualStartModal container-fluid" cg-busy="{promise:promise,templateUrl:templateUrl,message:message,backdrop:backdrop,delay:delay,minDuration:minDuration}">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
          <div class = "row"> 
            <div class = "modalHeading col-md-10 floatLeft"><i class="icon-plus-sign-alt"></i> Add New Employee</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
          </div>
        </div>        
    </div> 
    
<div class="modal-body">
    <div class = "formWrapper">
       <form name="newEmployeeForm" class = "row driverEditForm">
           

         
           <div class = "col-md-6 col-xs-12 form-group"> 
                <label class = 'floatLeft' >Employee ID:</label>
                    <input type="text" 
                           class="form-control" 
                           name="EmployeeID" 
                           ng-model="employeeID" 
                           min="1" 
                           ng-min="1" 
                           max="20" 
                           ng-max="20" 
                           placeholder="Enter Employee ID"
                           ng-class = "{error: newEmployeeForm.EmployeeID.$invalid && !newEmployeeForm.EmployeeID.$pristine}"
                           required>
               
            </div>           
    </form>
    </div>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" 
            class="btn btn-info editBucketbutton buttonRadius0" 
            ng-click = "saveNewEmployee(employeeID)" 
            ng-disabled="newEmployeeForm.$invalid">Save</button>
    <button type="button" class="btn btn-default editBucketbutton buttonRadius0" ng-click = "cancel()">Cancel</button> 
</div>
</div>