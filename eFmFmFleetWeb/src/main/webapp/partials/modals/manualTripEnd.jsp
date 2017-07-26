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
<div class = "importEmployeeModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-calendar edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Manual Trip End</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>      
<div class="modal-body modalMainContent">     
  <form name = "addEndKmForm">  
	   <div class = "col-md-6 col-xs-12"> 
           <div>
           <label>Enter Trip End Kilometer</label>
                <input type="text" 
                       ng-model="tripEnd.kilometer" 
                       class="form-control" 
                       placeholder = "End Kilometer"
                       required
                       ng-pattern = "regexDecimalNumbers"
                       name = "endKm"
                       ng-minlength="0"
                       ng-maxlength="50"
                 required
                 ng-class = "{error: addEndKmForm.endKm.$invalid && !addEndKmForm.endKm.$pristine}">
           </div>
       </div>
  </form>   
     <div class="modal-footer modalFooter">  
           <button type="button" 
                   class="btn btn-info floatLeft" 
                   id='imp_stud' 
                   ng-click = "enterStartKm(tripEnd)"
                   ng-disabled="addEndKmForm.$invalid">End Trip</button>
           <button type="button" class="btn btn-close floatLeft" ng-click = "cancel()">Cancel</button>
     </div>
</div>
</div>