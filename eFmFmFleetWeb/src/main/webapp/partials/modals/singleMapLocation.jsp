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
<div class = "singleMapLocationModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-map-marker edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-8 floatLeft">View Location on Map for <strong>{{employee.employeeName}}</strong> </div>
            <div class = "col-md-2 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
<!--<form>  -->
<div class="modal-body modalMainContent">
    <div class = "row">
        <div class = "col-md-8">Address<br>            
            <textarea type = "text" class = "floatLeft form-control1" id = "newAddress" ng-model = "employee.employeeAddress" readonly></textarea></div>
        
        <div class = "col-md-6 hidden">Co-ordiantes<br>
            <input type = "text" class = "floatLeft form-control1" id = "latlangInput" ng-model = "employee.cords" readonly></div>
<!--        <span ng-bind = "cords.points"></span>-->
        <div class = "col-md-12 map_viewMap" ng-if = "isLoaded">
            <efmfm-map-location id = "mapDiv"></efmfm-map-location>
            
        </div>
    </div>    

</div>
    
<div class="modal-footer modalFooter"> 
    <button type="button" class="btn btn-info" ng-click = "saveNewCords(employee)">Save</button>
    <button type="button" class="btn btn-default" ng-click = "cancel()">Close</button>
</div>
<!--    </form> -->
</div>