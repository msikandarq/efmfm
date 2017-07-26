<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "addUserFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft addMobHead"></div>
            <div class = "addMobHeading col-md-10 floatLeft">Add Employee Id</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "addMobCancel" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body">
       <form class = "addEmpIdForm" name = "addEmpIdForm">
         <div class = "userInfo_adminForm row">
              <div class = "col-md-12 col-xs-12 form-group"> 
              <label>Enter Employee Id </label>
                   <input type="text" 
                      ng-model="employeeId"
                      class="form-control" 
                      placeholder = "Employee Id"
                      name = 'employeeId' 
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      expect-special-char
                      required
                      ng-class = "{error: addEmpIdForm.employeeId.$invalid && !addEmpIdForm.employeeId.$pristine}">                  
                <span class = "hintModal" ng-show = "addEmpIdForm.employeeId.$error.maxlength"></span>  
              </div> 
            </div>
        </form>
    </div>
<div class="addMobModal-footer">    
    <button type="button" class="btn btn-success" ng-click = "add(employeeId)" ng-disabled="addEmpIdForm.$invalid">Add</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
</div>