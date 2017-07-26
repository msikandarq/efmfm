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
            <div class = "modalHeading col-md-8 floatLeft">Add No Entry Details</div>
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
              <label for = "driverName">Reason Of No Entry</label>
                <textarea class="form-control" 
                          rows="3" 
                          ng-model = "remark.remarks"
                          name = 'remarks'
                          placeholder= "Enter Remarks"
                          ng-maxlength = '50'
                          expect_special_char
                          required></textarea>
            </div>
       
           <div class = "col-md-6 col-xs-6 form-group"> 
              <label>Start Date</label>
                    <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                      <button class="btn btn-default" 
                  ng-click="startDate($event)">
                <i class = "icon-calendar calInputIcon"></i></button></span>
                    <input type="text" 
                       ng-model="remark.startDate" 
                       class="form-control" 
                       placeholder = "Start Date" 
                       datepicker-popup="dd-MM-yyyy"
                       is-open="datePicker.startDate" 
                       show-button-bar = false
                       show-weeks=false
                       required
                       readonly
                       name = 'startDate'
                       >   
            
           </div>
           </div> 

           <div class = "col-md-6 col-xs-5 form-group"> 
              <label>End Date</label>
                    <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                      <button class="btn btn-default" 
                  ng-click="endDate($event)">
                <i class = "icon-calendar calInputIcon"></i></button></span>
                    <input type="text" 
                       ng-model="remark.endDate" 
                       class="form-control" 
                       placeholder = "End Date" 
                       max-date = "remark.startDate"
                       datepicker-popup="dd-MM-yyyy"
                       is-open="datePicker.endDate" 
                       show-button-bar = false
                       show-weeks=false
                       required
                       readonly
                       name = 'endDate'
                       >   
           </div>
           </div> 
     
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-warning buttonRadius0" ng-click = "addRemarks(remark)" ng-disabled="remarksForm.$invalid"
            >Add</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>