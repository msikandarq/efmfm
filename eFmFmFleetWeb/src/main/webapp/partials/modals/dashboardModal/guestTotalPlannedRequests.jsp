<div class = "vsModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
          <div class = "row"> 
            <div class = "icon-archive edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Total Planned Guest Requests</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
          </div>
        </div>        
    </div>  
    <div class = "row">
        <div class="modal-header2 col-md-12">
            <form><input class = "searchModal" 
                         type="text" 
                         class="form-control" 
                         placeholder="Search..." 
                         id="formGroupInputSmall"
                         ng-model = "search"></form>
        </div>
<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotGuestRequestStatus">
 <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
</div>
    </div>
    
<div class="modal-body modalMainContent" ng-show = "gotGuestRequestStatus">
    <table class="table table-bordered table-hover table-responsive container-fluid dashboardTable">
        <thead class ="tableHeading" ng-show = "totalPlannedDropData.length==0 || totalPlannedDropData.length>0">
            <tr>
		      <th>Request ID</th>
		      <th>Request Time</th>
              <th>Employee Id</th>
		      <th>Employee Name</th>
		      <th>Gender</th>
          <th>Facility Name</th>
		      <th>Address</th>
<!--              <th>Trip Type</th>-->
            </tr> 
        </thead>
        <tbody ng-show = "totalPlannedDropData.length==0">
              <tr>
                 <td colspan = '7'>
                   <div class = "noData">No Employee Planned for Pickup</div>
                 </td>
              </tr>
        </tbody>
        <tbody ng-show = "totalPlannedDropData.length>0">
            <tr ng-repeat = "dropRequest in totalPlannedDropData | filter:search">
                <td class = "col-md-1">{{dropRequest.requestId}}</td>
                <td class = "col-md-1">{{dropRequest.tripTime}}</td>
                <td class = "col-md-1">{{dropRequest.employeeId}}</td>
                <td class = "col-md-3">{{dropRequest.employeeName}}</td>
                <td class = "col-md-1">{{dropRequest.gender}}</td>
                <td class = "col-md-1">{{dropRequest.facilityName}}</td>
                <td class = "col-md-4">{{dropRequest.employeeAddress}}</td>
<!--                <td class = "col-md-1">{{dropRequest.tripType}}</td>-->
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-info" ng-click = "cancel()">Close</button>
</div>
</div>