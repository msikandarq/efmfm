<!-- 
@date           06/27/2016
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
06/27/2016  Saima Aziz      Initial Creation
06/27/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "remarkFormModalTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-8 floatLeft">Add Remarks</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body ">
    <div class = "formWrapper_editEntity">
       <form name = "remarksForm" class = "editVehicleEntityForm">    
           <div class = "col-md-12 col-xs-12 form-group"> 
              <label for = "driverName">Remarks</label>

               <input type="text" class="form-control" 
                   ng-model = "remark.remarks"
                   name = 'remarks'
                   expect_special_char
                   placeholder= "Enter Remarks"
                   ng-maxlength = '40'
                   required>
            </div>
           
           <!-- <div class = "col-md-12 col-xs-12 form-group"> 
            <label>Driver Join Date</label>
            <div class = "input-group calendarInput">
            <span class="input-group-btn">
            <button class="btn btn-default" 
            ng-click="joinDate($event)">
            <i class = "icon-calendar calInputIcon"></i></button></span>
            <input type="text" 
                   ng-model="remark.joinDate" 
                   class="form-control" 
                   placeholder = "Join Date" 
                   datepicker-popup="dd-MM-yyyy"
                   is-open="datePicker.joinDate" 
                   show-button-bar = false
                   show-weeks=false
                   required
                   readonly
                   name = 'joinDate'
                   >   
              
               
           </div>
            </div>  -->
       
      <!--      <div class = "col-md-12 col-xs-12 form-group"> 
              <label>Driver Relieve Date</label>
                    <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                      <button class="btn btn-default" 
                  ng-click="relieveDate($event)">
                <i class = "icon-calendar calInputIcon"></i></button></span>
            <input type="text" 
               ng-model="remark.relieveDate" 
               class="form-control" 
               placeholder = "Relieve Date" 
               datepicker-popup="dd-MM-yyyy"
               is-open="datePicker.relieveDate" 
               show-button-bar = false
               show-weeks=false
               required
               readonly
               name = 'relieveDate'
               >   
            
           </div>
           </div>  -->
     
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-warning buttonRadius0" ng-click = "addRemarks(remark)" ng-disabled="remarksForm.$invalid"
            >Add Remarks</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>