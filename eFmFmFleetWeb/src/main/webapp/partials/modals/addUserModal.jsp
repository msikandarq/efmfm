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
<div class = "addUserFormModalTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New User</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
       <form class = "addUserForm" name = "addUserForm">
         <div class = "userInfo_adminForm row"  ng-show = "!onMap">
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>First Name</label>
                   <input type="text" 
                      ng-model="user.userFName"
                      class="form-control" 
                      placeholder = "First Name"
                      name = 'userFName' 
                      ng-minlength="3"
  ng-maxlength="20"                      
  maxlength="20"                      
  is-name-only-valid
                      required
                      ng-class = "{error: addUserForm.userFName.$invalid && !addUserForm.userFName.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.userFName.$error.maxlength">* Name is too short</span>  
           </div>     
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Middle Name</label>
                   <input type="text" 
                      ng-model="user.userMName"
                      class="form-control" 
                      placeholder = "Middle Name"
                      name = 'userMName' 
                      ng-minlength="3"
  ng-maxlength="20"                      
  maxlength="20"                      
  is-name-only-valid
                      required
                      ng-class = "{error: addUserForm.userMName.$invalid && !addUserForm.userMName.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.userMName.$error.maxlength"></span>  
           </div>         
           
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Last Name</label>
                   <input type="text" 
                      ng-model="user.userLName"
                      class="form-control" 
                      placeholder = "Last Name"
                      name = 'userLName' 
                      is-name-only-valid
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      ng-class = "{error: addUserForm.userLName.$invalid && !addUserForm.userLName.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.userLName.$error.maxlength">* Name is too short</span>  
           </div>  
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Email Address</label>
                   <input type="email" 
                      ng-model="user.email"
                      class="form-control" 
                      placeholder = "Email Address"
                      name = 'email' 
                      required
                      ng-class = "{error: addUserForm.email.$invalid && !addUserForm.email.$pristine}">                  
                <span class = "hintModal"></span>  
           </div>   
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Mobile Number</label>
                   <input type="text" 
                      ng-model="user.mobileNumber"
                      class="form-control" 
                      placeholder = "Mobile Number"
                      name = 'mobileNumber' 
                      is-number-only-valid
                      ng-minlength="6"
                      ng-maxlength="18"
                      maxlength="18"
                      required
                      ng-class = "{error: addUserForm.mobileNumber.$invalid && !addUserForm.mobileNumber.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.mobileNumber.$error.maxlength"></span>  
           </div>     
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Gender</label><br>
                <select ng-model="user.selectedgender"
                        class = 'form-control'
                	    ng-options="gender.text for gender in genders track by gender.value"
                		ng-change = "setGender(user.selectedgender)"
                        required>
      					<option value="">--Select --</option>
    			</select>
           </div>         
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Physically Challenge</label><br>
                <select ng-model="user.selectedChall"
                        class = 'form-control'
                	    ng-options="physicalChall.text for physicalChall in physicalChalls track by physicalChall.value"
                		ng-change = "setPhysicalChall(user.selectedChall)"
                        required>
      					<option value="">--Select--</option>
    			</select>
           </div>        
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>User Role</label><br>
                <select ng-model="user.selectedUserRole"
                        class = 'form-control'
                	    ng-options="userRole.text for userRole in userRoles track by userRole.value"
                		ng-change = "setUserRole(user.selectedUserRole)"
                        required>
      					<option value="">--Select--</option>
    			</select>
           </div> 
            <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Employee ID</label>
                   <input type="text" 
                      ng-model="user.empId"
                      class="form-control" 
                      placeholder = "Employee ID"
                      name = 'empId' 
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      expect_special_char
                      required
                      ng-class = "{error: addUserForm.empId.$invalid && !addUserForm.empId.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.empId.$error.maxlength"></span>  
           </div>          
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Employee Department</label>
                   <input type="text" 
                      ng-model="user.empDepartment"
                      class="form-control" 
                      placeholder = "Employee Department"
                      name = 'empDepartment' 
                      ng-minlength="2"
                      ng-maxlength="30"
                      maxlength="30"
                      expect_special_char
                      required
                      ng-class = "{error: addUserForm.empDepartment.$invalid && !addUserForm.empDepartment.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.empDepartment.$error.maxlength"></span>  
           </div>
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Project Name</label>
                   <input type="text" 
                      ng-model="user.projectName"
                      class="form-control" 
                      placeholder = "Project Name"
                      name = 'projectName' 
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      expect_special_char
                      required
                      ng-class = "{error: addUserForm.projectName.$invalid && !addUserForm.projectName.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.projectName.$error.maxlength"></span>  
           </div>          
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Employee Designation</label>
                   <input type="text" 
                      ng-model="user.empDesignation"
                      class="form-control" 
                      placeholder = "Employee Designation"
                      name = 'empDesignation' 
                      ng-minlength="2"
                      ng-maxlength="30"
                      maxlength="30"
                      expect_special_char
                      required
                      ng-class = "{error: addUserForm.empDesignation.$invalid && !addUserForm.empDesignation.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.empDesignation.$error.maxlength"></span>  
           </div>   
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Employee Business Unit</label>
                   <input type="text" 
                      ng-model="user.empBusiness"
                      class="form-control" 
                      placeholder = "Employee Business Unit"
                      name = 'empBusiness' 
                      ng-minlength="2"
                      ng-maxlength="20"
                      maxlength="20"
                      expect_special_char
                      required
                      ng-class = "{error: addUserForm.empBusiness.$invalid && !addUserForm.empBusiness.$pristine}">                  
                <span class = "hintModal" ng-show = "addUserForm.empBusiness.$error.maxlength"></span>  
           </div> 
      </div>  
      <div class = "row picknDropLocation_myProfileAdmin" ng-if = "onMap">        
           <div class = "col-md-4 col-xs-12 form-group"> 
              <label>Area</label>
                   <input type="text" 
                      ng-model="user.area"
                      class="form-control" 
                      placeholder = "area"
                      name = 'area'
                      readonly>           
                <span class = "hintModal"></span>  
           </div>        
           <div class = "col-md-8 col-xs-12 form-group"> 
              <label>Address</label>
                   <textarea type="text" 
                      ng-model="user.address"
                      class="form-control" 
                      id = "newAddress"
                      placeholder = "Address"
                      name = 'address'
                      readonly
                      required> </textarea>            
                <span class = "hintModal"></span>  
           </div>  
           <div class = "col-md-4 col-xs-12 form-group hidden"> 
              <label>Cordinates</label>
                   <input type="text" 
                      ng-model="user.cords"
                      class="form-control"  
                      id = "latlangInput"
                      placeholder = "Co-ordinates"
                      name = 'cords'
                      required
                      readonly>                  
                <span class = "hintModal"></span>  
           </div>  
        <div class = "col-md-12 map_viewMap">
            <efmfm-new-user-map-location id = "mapDiv_admin"></efmfm-new-user-map-location>            
        </div>
      </div>  
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
	<button type="button" class="btn btn-success" ng-show = "onUserInfo" ng-click = "setPickDropLocation()" ng-disabled="addUserForm.$invalid">Set Pick and Drop Location</button> 
	<button type="button" class="btn btn-success" ng-show = "!onUserInfo" ng-click = "backtoUserInfo()">Back</button>     
	<button type="button" class="btn btn-success" ng-show = "!onUserInfo" ng-click = "save(user)" ng-disabled="addUserForm.$invalid">Save</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>
<!--ng-disabled="addUserForm.$invalid"-->