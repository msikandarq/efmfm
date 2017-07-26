<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "addNewShiftModalTemplate">
<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-plus col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-8 floatLeft">Add Employee To Pluck Card</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "close()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body">
    <form name = "pluckCardForm" class = "editVehicleEntityForm">    
           <div class = "col-md-12 col-xs-12">
  <strong>Choose Employee Name : </strong>
  <label ng-repeat="employeeName in employeeNamesList" class="multiCheckboxSelect">
    <input type="checkbox" checklist-model="user.employeeNamesList" checklist-value="employeeName"> {{employeeName}}<br/>
  </label>
  <br>

  <strong>Select Pluck Card Design Type: </strong>
                      <select ng-model="pluckCardDesignType" 
                               class="form-control" 
                               ng-options="pluckCardDesignType.value for pluckCardDesignType in pluckCardDesignTypes"
                               ng-change="selectDesignType(pluckCardDesignType)"
                               required 
                               >
                         <option value="">-- Select Type --</option>
                         </select>

  <br>


            </div>
     </form>       
    </div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-warning buttonRadius0" ng-disabled="pluckCardFormDisabled" ng-click = "previewPluckCard(user.employeeNamesList)" >Add</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>