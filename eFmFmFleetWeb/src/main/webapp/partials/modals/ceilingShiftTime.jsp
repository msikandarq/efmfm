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
            <div class = "modalHeading col-md-8 floatLeft">Add Ceiling Values</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body ">
    <div class = "formWrapper_editEntity">
       <form name = "ceilingForm" class = "editVehicleEntityForm">    
           <div class = "col-md-12 col-xs-12 form-group"> 
              <label for = "driverName">Ceiling Values</label>

                              <input type="number" 
                                     class="form-control" 
                                     name="remarks" 
                                     ng-model="ceilingValues" 
                                     min="1" 
                                     placeholder="Enter Ceiling Values" 
                                     required
                                     ng-class = "{error: ceilingForm.remarks.$invalid && !ceilingForm.remarks.$pristine}"
                                     >
                      
                      <span class="hintItallic error" ng-show="ceilingForm.remarks.$invalid && !ceilingForm.remarks.$pristine">Kinly Enter early request greater than 0</span>  


            </div>
           
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-warning buttonRadius0" ng-click = "addCeilingValues(ceilingValues)" ng-disabled="ceilingForm.$invalid"
            >Add Ceiling Values</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>