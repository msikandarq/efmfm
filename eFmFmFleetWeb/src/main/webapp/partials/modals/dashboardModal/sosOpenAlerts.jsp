<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div ng-include = "'partials/popovers/actionTakesSOSOpenAlerts.jsp'"></div><div class="loading"></div>
<div class = "sosModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-bell-alt edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Open Alerts</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>  
    <div class = "row">
        <div class="modal-header2 col-md-6">
            <form><input class = "searchModal" 
                         ng-model = "searchRoadAlerts"
                         type="text" 
                         class="form-control" 
                         placeholder="Search..." 
                         id="formGroupInputSmall"></form>
        </div>
        <div class="modal-header2 col-md-6">
            <button class = "btn btn-sm btn-success form-control excelExportButton floatRight" 
                    ng-click = "saveInExcel('alertsOpen')">
                <i class = "icon-download-alt"></i>
                <span class = "marginLeft5">Excel</span>
            </button>
        </div>
<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotSosData">
 <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
</div>
    </div>

<div id="alertsOpen" class="modal-body modalMainContent" ng-show = "gotSosData">
    <table class="table table-bordered table-hover table-responsive container-fluid dashboardTable">
        <thead class ="tableHeading" ng-show = "sosOpenAlertsData.length==0 || sosOpenAlertsData.length>0">
            <tr>
            <th>Alert Date</th>
              <th>Alert Id</th>
              <th>Route Id</th>
              <th>Title</th>
		          <th>Zone Name</th>
		          <th>User Type</th>
              <th>Driver Name</th>
		          <th>Driver Mobile Number</th>
		          <th>Vehicle Number</th>
		          <th>Alert Type</th>
		          <th>Description</th>
              <th>Facility Name</th>
              <th>Actions</th>
            </tr> 
        </thead>
        <tbody ng-show = "sosOpenAlertsData.length==0">
              <tr>
                 <td colspan = '11'>
                   <div class = "noData">There are No Open Alerts</div>
                 </td>
              </tr>
        </tbody>
        <tbody ng-show = "sosOpenAlertsData.length>0">
            <tr class = 'row{{openAlert.tripAlertId}}' ng-repeat = "openAlert in sosOpenAlertsData  | filter: searchRoadAlerts">
                <td class = "col-md-2">{{openAlert.alertDate}}</td>
                <td class = "col-md-1">{{openAlert.tripAlertId}}</td>
                <td class = "col-md-1">{{openAlert.assignRouteId}}</td>
                <td class = "col-md-1">{{openAlert.tittle}}</td>
                <td class = "col-md-1">{{openAlert.zoneName}}</td>
                <td class = "col-md-1">{{openAlert.userType}}</td>
                <td class = "col-md-2">{{openAlert.driverName}}</td>
                <td class = "col-md-1">{{openAlert.driverNumber}}</td>
                <td class = "col-md-1">{{openAlert.vehicleNumber}}</td>
                <td class = "col-md-1">{{openAlert.alertType}}</td>
                <td class = "col-md-2">{{openAlert.description}}</td>
                <td class = "col-md-1">{{openAlert.facilityName}}</td>

                <td class = "col-md-1">          
<!--
                    <div class="dropdown">
                      <button class="btn btn-success dropdown-toggle buttonRadius0" 
                              type="button" 
                              id="dropdownMenu1" 
                              data-toggle="dropdown" 
                              aria-haspopup="true" 
                              aria-expanded="false">
                        Action
                        <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu sosDropDown" aria-labelledby="dropdownMenu1">
                        <li><div class = "input-group calendarInput"> 
                              <input ng-model="action.text"
                                     type = "text" 
                                     class="form-control" 
                                     placeholder = "Enter Action Taken"
                                     maxLength = '150'>
                               <span class="input-group-btn">
                                   <button class="btn btn-default buttonRadius0" 
                                           ng-click="action(action.text, roadAlert, $index)" >
                                   <i class = "icon-ok-circle iconSOSCircle searchServiceMappingIcon" style = "color: forestgreen;"></i></button></span> 
                         </div>
                          </li>
  </ul>
</div>
-->
<!--
                    <button type = 'button' 
                                               value = 'Action' 
                                               class = 'actionSOSButton btn btn-success form-control'
                                               ng-click = 'openAction()'
                                              popover-template="partials/popovers/actionTakesSOSOpenAlerts.jsp"
                                              popover-title = "SOS OPEN ALERT ACTION"
                                              popover-placement="left"
                                              popover-trigger ="click">Take Action
                      </button>
-->
                    
                    <button type = 'button' 
                           value = 'Action' 
                           class = 'btn btn-warning btn-xs actionSOSButton buttonRadius0 form-control'
                           ng-click = 'openAction(openAlert)'>Take Action
                      </button>
<!--
                    <div class="btn-group">
                          <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Action <span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu sosDropDown">
                            <li>
                                <div class = "input-group calendarInput"> 
                              <input ng-model="action.text"
                                     type = "text" 
                                     class="form-control" 
                                     placeholder = "Enter Action Taken"
                                     maxLength = '150'>
                               <span class="input-group-btn">
                                   <button class="btn btn-default" 
                                           ng-click="action(action.text, roadAlert, $index)" >
                                   <i class = "icon-ok-circle iconSOSCircle searchServiceMappingIcon"></i></button></span> 
                         </div>

                            </li>
                          </ul>
-->
                    </div>
<!--                    <input type = 'button' value = 'Action' class = 'btn btn-sm btn-success buttonRadius0'>-->
                </td>
<!--
    <td>          
                    <div class="dropdown">
                      <button class="btn btn-success dropdown-toggle buttonRadius0" 
                              type="button" 
                              id="dropdownMenu1" 
                              data-toggle="dropdown" 
                              aria-haspopup="true" 
                              aria-expanded="false">
                        Action
                        <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu sosDropDown" aria-labelledby="dropdownMenu1">
                        <li><div class = "input-group calendarInput"> 
                              <input ng-model="action.text"
                                     type = "text" 
                                     class="form-control" 
                                     placeholder = "Enter Action Taken"
                                     maxLength = '150'>
                               <span class="input-group-btn">
                                   <button class="btn btn-default buttonRadius0" 
                                           ng-click="action(action.text, roadAlert, $index)" >
                                   <i class = "icon-ok-circle iconSOSCircle searchServiceMappingIcon" style = "color: forestgreen;"></i></button></span> 
                         </div>
                          </li>
  </ul>
        </div></td>
-->
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Close</button>
</div>
</div>