<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "sosModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-bell-alt edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">SOS Alerts</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>  
    <div class = "row">
        <div class="modal-header2 col-md-6">
            <form><input class = "searchModal" 
                         ng-model = "searchSOS"
                         type="text" 
                         class="form-control" 
                         placeholder="Search..." 
                         id="formGroupInputSmall"></form>
        </div>

        <div class="modal-header2 col-md-6">
            <button class = "btn btn-sm btn-success form-control excelExportButton floatRight" 
                    ng-click = "saveInExcel('sosAlert')">
                <i class = "icon-download-alt"></i>
                <span class = "marginLeft5">Excel</span>
            </button>
        </div>
<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotSosData">
 <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
</div>
    </div>
    
<div id="sosAlert" class="modal-body modalMainContent" ng-show = "gotSosData">
    <table class="table table-bordered table-hover table-responsive container-fluid dashboardTable">
        <thead class ="tableHeading" ng-show = "sosAlertsData.length==0 || sosAlertsData.length>0">
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
        <tbody ng-show = "sosAlertsData.length==0">
              <tr>
                 <td colspan = '11'>
                   <div class = "noData">No SOS Alerts</div>
                 </td>
              </tr>
        </tbody>

        <tbody ng-show = "sosAlertsData.length>0">
            <tr ng-repeat = "sosAlert in sosAlertsData | filter: searchSOS">
            
            <td class = "col-md-2">{{sosAlert.alertDate}}</td>
                <td class = "col-md-1">{{sosAlert.zoneName}}</td>
                <td class = "col-md-1">{{sosAlert.userType}}</td>
                <td class = "col-md-2">{{sosAlert.driverName}}</td>
                <td class = "col-md-1">{{sosAlert.driverNumber}}</td>
                <td class = "col-md-1">{{sosAlert.vehicleNumber}}</td>
                <td class = "col-md-2">{{sosAlert.tittle}}</td>
                <td class = "col-md-1">{{sosAlert.facilityName}}</td>

                <td class = "col-md-4">{{sosAlert.description}}</td>
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-warning" ng-click = "cancel()">Close</button>
</div>
</div>