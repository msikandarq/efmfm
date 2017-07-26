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
            <div class = "modalHeading col-md-8 floatLeft">Create Message</div>
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
              <label for = "driverName">Type your message</label>
              <textarea class="form-control" 
                        placeholder="Type Message..." 
                        name="myTextarea" 
                        rows="4" 
                        ng-maxlength="300" 
                        maxlength="300" 
                        ng-change="setMessageDetails(typeMessage)"
                        ng-model="typeMessage" 
                        required
                        >
              </textarea>
              <strong ng-show="remainingCharShow">Remaining Character {{remainLength}}</strong> 
            </div>
       </form>  

      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-warning buttonRadius0" ng-click = "addMessages(typeMessage)" ng-disabled="remarksForm.$invalid"
            >Add Message</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>