<div ng-include="'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div ng-include="'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="createNewRouteModal container-fluid">
  <div class="row">
    <div class="modal-header modal-header1 col-md-12">
      <div class="row">
        <!-- <div class="edsModal-icon col-md-1 floatLeft"></div> -->
        <div class="modalHeading col-md-8">Vendor Summary</div>
        <div class="col-md-2 floatRight pointer">
          <img src="images/portlet-remove-icon-white.png" class="floatRight"
            ng-click="cancel()">
        </div>
      </div>
    </div>
  </div>
  <div class="modal-body modalMainContent">

        <div class="table-responsive table-bordered  table-hover">          
          <table class="table differentTable">
            <thead class="bulkRouteHeader"> 
              <tr>
                <th>Route Id</th>
                <th>Route Name</th>
                <th>Trip Type</th>
                <th>No Of Employee</th>
                <th>Last Drop/First Pickup Loc</th>
                <th>Driver Name</th>
                <th>Vehicle Number</th>
                <th>Device Id</th>
                <th>Edit Route</th>
              </tr>
            </thead>
          
            <tbody ng-show="bulkRouteData.length>0">
              <tr ng-repeat="bulkRoute in bulkRouteData | filter:searchRouteFilter">
                <td>{{bulkRoute.routeId}}</td>
                <td>{{bulkRoute.routeName}}</td> 
                <td>{{bulkRoute.tripType}}</td>
                <td>{{bulkRoute.numberOfEmployees}}</td>
                <td>{{bulkRoute.dropPickAreaName}}</td>
                <td>{{bulkRoute.driverName}}</td>
                <td>{{bulkRoute.vehicleNumber}}</td>
                <td>{{bulkRoute.driverId}}</td>
                <td class="bulkRouteRowStyle">
                            <input type = "button" 
                                   class = "btn btn-warning6 btn-xs"
                                   value = "Edit Route"
                                   ng-class = "{disabled: bulkRoute.tripStatus == 'F'}"
                                   ng-click = "editBucketBulkRoute(bulkRoute, 'lg')" >
                </td>
              </tr>
               
            </tbody>
           
          </table>
                </div>
  </div>
<!--   <div class="modal-footer modalFooter createNewZone_modalFooter">
    <button type="button" class="btn btn-close floatRight"
      ng-click="cancel()">Cancel</button>
    <button type="button" class="btn btn-success floatRight"
      ng-click="createNew(newRoute)" ng-disabled="createNewZone.$invalid">Create
      New Route</button>

  </div> -->
</div>





