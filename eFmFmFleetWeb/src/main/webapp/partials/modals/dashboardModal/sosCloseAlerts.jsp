<div class = "sosModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-bell-alt edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Close Alerts</div>
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
                    ng-click = "saveInExcel('alertsClosed')">
                <i class = "icon-download-alt"></i>
                <span class = "marginLeft5">Excel</span>
            </button>
        </div>
<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotSosData">
 <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
</div>
    </div>
    
<div id="alertsClosed" class="modal-body modalMainContent" ng-show = "gotSosData">
    <table class="table table-bordered table-hover table-responsive container-fluid dashboardTable">
        <thead class ="tableHeading" ng-show = "sosRoadAlertsData.length==0 || sosRoadAlertsData.length>0">
            <tr>
            <th>Alert Date</th>
		      <th>Zone Name</th>
		      <th>User Type</th>
              <th>Driver Name</th>
		      <th>Driver Mobile Number</th>
		      <th>Vehicle Number</th>
		      <th>Title</th>
          <th>Facility Name</th>
		      <th>Description</th>
            </tr> 
        </thead>
        <tbody ng-show = "sosRoadAlertsData.length==0">
              <tr>
                 <td colspan = '11'>
                   <div class = "noData">There No Close Alerts</div>
                 </td>
              </tr>
        </tbody>
        <tbody ng-show = "sosRoadAlertsData.length>0">
            <tr ng-repeat = "roadAlert in sosRoadAlertsData | filter: searchRoadAlerts">
                            <td class = "col-md-2">{{roadAlert.alertDate}}</td>
            
                <td class = "col-md-1">{{roadAlert.zoneName}}</td>
                <td class = "col-md-1">{{roadAlert.userType}}</td>
                <td class = "col-md-2">{{roadAlert.driverName}}</td>
                <td class = "col-md-1">{{roadAlert.driverNumber}}</td>
                <td class = "col-md-1">{{roadAlert.vehicleNumber}}</td>
                <td class = "col-md-2">{{roadAlert.tittle}}</td>
                <td class = "col-md-1">{{roadAlert.facilityName}}</td>

                <td class = "col-md-4">{{roadAlert.description}}</td>
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-warning" ng-click = "cancel()">Close</button>
</div>
</div>