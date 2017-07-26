<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "addUserFormModalTemplate">
	
	<div class = "row">
        <div class="modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft addMobHead"></div>
            <div class = "addMobHeading col-md-10 floatLeft">Add Mobile Number</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "addMobCancel" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body">
       <form class = "addMobNumForm" name = "addMobNumForm">
         <div class = "userInfo_adminForm row">
              <div class = "col-md-12 col-xs-12 form-group"> 
              <label>Enter Mobile Number with Country Code</label>
                   <input type="text" 
                      ng-model="mobileNumber"
                      class="form-control" 
                      placeholder = "Mobile Number"
                      name = 'mobileNumber' 
                      is-number-only-valid
                      ng-minlength="6"
                      ng-maxlength="18"
                      maxlength="18"
                      required
                      ng-class = "{error: addMobNumForm.mobileNumber.$invalid && !addMobNumForm.mobileNumber.$pristine}">                  
                <span class = "hintModal" ng-show = "addMobNumForm.mobileNumber.$error.maxlength"></span>  
              </div> 
            </div>
        </form>
    </div>
<div class="addMobModal-footer">    
    <button type="button" class="btn btn-success" ng-click = "add(mobileNumber)" ng-disabled="addMobNumForm.$invalid">Add</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
</div>