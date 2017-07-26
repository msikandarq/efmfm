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
<div class = "editVehicleFormModalTemplate">
  

    <div class = "row">
         
         <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-plus col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft"> Odometer Distance</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>      
    </div>
    

<div class="modal-body ">   
  <form name="OdometerForm">
      <div class="col-md-6">
        <strong>Start Odometer Km</strong> <input type="number" class="form-control" name="" ng-model="startOdometer" required>
      </div>
      <div class="col-md-6">
        <strong>End Odometer Km</strong> <input type="number"  class="form-control" name="" ng-model="endOdometer" required>
      </div>
  </form>
</div>
<br/>
<br/>
<br/>
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success"  ng-disabled="OdometerForm .$invalid" ng-click = "saveOdometer(startOdometer,endOdometer)">Save</button>    
  <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
</div>