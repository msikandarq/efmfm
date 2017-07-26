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
            <div class = "icon-map-marker mapMarkerIconCreateRequest col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add Geo Location Name</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div>  
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "addNewShift" name = "addNewShift">
        <div class = "input-group">
         <span class = "input-group-addon labelOriginLocation">Origin</span>
         <select ng-model="origin"
                           class="form-control" 
                           ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaName"
                           ng-change="setOriginSelected(origin)"
                           >
                     <option value="">Select Origin Name</option>
                  </select>
         <!-- <span class = "input-group-addon">.00</span> -->
        </div>
        
        <span ng-repeat="destinationPoint in destinationPoints track by $index">
        <div class = "input-group">
         <span class = "input-group-addon labelDestinationLocation">Destination {{$index + 1}}</span>
         <select ng-model="destinationPoint.destination"
                           class="form-control" 
                           ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaName"
                           ng-change = "setAreaSelected(destinationPoint.destination, $index)"
                           ng-click="checkOrginselected()"
                           ng-disabled="desinationDisabled"
                           ng-class="{'destinationWarning': destinationPoint.destination.areaId == areaId}"
                           >
                     <option value="">Select Location Name</option>
                  </select>
                  
         <span class = "input-group-addon"><div class = "icon-map-marker mapMarkerIcon pointer" ng-click="addMapDestination($index, origin)" ng-class="{enableAddButton: destinationAddButtonFlg == 'true' , disableAddButton: destinationAddButtonFlg == 'false'}"></div></span>
         <span class = "input-group-addon" ><div class = "icon-remove-sign mapMarkerDelete pointer" ng-click="deleteDestinationLocation(destinationPoint.destination, $index)" ng-class="{enableAddButton: destinationDeleteButtonFlg == 'true' , disableAddButton: destinationDeleteButtonFlg == 'false'}"></div></span>
        </div>
        </span> 
        <br/>
        <!-- ng-class="{enableAddButton: ddt.colorFlg == 'R' , disableAddButton: ddt.colorFlg == 'Y'}" -->
        <button ng-click="addDestination(destinationPoints)" class="btn btn-success btn-xs" ng-disabled="desinationDisabledButton">Add Destination</button>
        <span ng-show="errorMessageDestination" class="error" > <i> * Duplicate Value not allowed to add. Kindly check {{areaNameDetails}} Destination added two times continuously</i></span>
        <span ng-show="errorMessageOrigin" class="error" > <i> * Origin and first destination should not be same. Kindly change other destination.</i></span>
        <span ng-show="errorMessageEmptyDestination" class="error" > <i> * Destination empty</i></span>
       </form>
       
    </div>      
<div class="modal-footer modalFooter"> 
<div class="btn-group">
  <button type="button" 
          class="btn btn-primary btn-sm" 
          ng-click = "previewLocation(destinationPoints,origin)" ng-disabled="desinationDisabled || desinationDisabledButton">Preview Journey</button>
  <button type="button" 
          class="btn btn-warning btn-sm" 
          ng-click = "selectedLocationDetails(destinationPoints,origin)" ng-disabled="locationDetailButtonDisabled || desinationDisabledButton">Selected Location Details</button>
  <button type="button" 
          class="btn btn-success btn-sm"
          ng-click = "submitLocationDetails(destinationPoints,origin)" ng-disabled="desinationDisabledButton">Submit</button>
  <button type="button" 
          class="btn btn-info btn-sm"
          ng-click = "resetLocationDetails(destinationPoints)">Reset</button>        
  <button type="button" 
          class="btn btn-danger btn-sm"
          ng-click = "cancel()">Cancel</button>
</div>

</div>
     
</div>