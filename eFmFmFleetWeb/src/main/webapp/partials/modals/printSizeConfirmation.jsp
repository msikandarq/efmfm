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
            <div class = "modalHeading col-md-8 floatLeft">Choose Print Size</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body ">
    <div class = "formWrapper_editEntity">
       <form name = "remarksForm" class = "editVehicleEntityForm">    
           <div class = "col-md-4 col-xs-4 form-group"> 
           <span><strong>Choose Paper Size</strong></span>
           </div>

           <div class = "col-md-8 col-xs-8 form-group"> 
                    <div class="radio radio-success radio-inline">
                        <input type="radio" id="inlineRadio1" value="sizeA3" ng-model="printSize" name="radioInline" checked="">
                        <label for="inlineRadio1"> <strong>A3 Size</strong> </label>
                        <span><i>(3508 pixels x 4961 pixels (print resolution))</i></span>
                    </div><br/>
                    <br/>
                    <div class="radio radio-inline">
                        <input type="radio" id="inlineRadio2" value="sizeA4" ng-model="printSize" name="radioInline">
                        <label for="inlineRadio2"> <strong>A4 Size</strong> </label>
                        <span><i>(2480 pixels x 3508 pixels (print resolution))</i></span>
                    </div>
           </div> 
     
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-warning buttonRadius0" ng-click = "submitPrintSize(printSize)" ng-disabled="remarksForm.$invalid"
            >Ok</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>