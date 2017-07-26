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
<div class = "addNewShiftModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">
            <span ng-if = "addDropLocation">Add</span> <span ng-if = "!addDropLocation">Edit</span>  Drop Location</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "addDroplocation" name = "addDroplocation">
         <div class = "userInfo_adminForm row">
                <div class ='col-md-6'>
                    <label>Start Area Name</label><br>
                    <select ng-model="location.startAreaDrop"
                      class="form-control" 
                      ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaId"
                      required>
                  <option value="">Start Area Name
                  </option>
                </select>
                </div>

                <div class ='col-md-6'>
                    <label>End Area Name</label><br>
                    <select ng-model="location.endAreaDrop"
                      class="form-control" 
                      ng-options="allZoneData.areaName for allZoneData in (allZonesData | filter: { areaName: '!' + location.startAreaDrop.areaName }) track by allZoneData.areaId"
                      required>
                  <option value="">End Area Name
                  </option>
                </select>
                </div>
                
         </div>
         

       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
            ng-click = "saveAddDroplocation(location)" 
            ng-disabled="addDroplocation.$invalid">Save</button> 
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>