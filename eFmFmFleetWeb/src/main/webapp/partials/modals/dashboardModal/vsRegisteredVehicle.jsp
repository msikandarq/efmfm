<div class = "vsModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-check edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-10 floatLeft">Registered Vehicle</div>
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
    </div>
    
<div class="modal-body modalMainContent">
    <table class="table table-bordered table-hover table-responsive container-fluid dashboardTable">
        <thead class ="tableHeading">
            <tr>
              <th>Vendor Name</th>		      
		      <th>Vehicle Number</th>
              <th>Vehicle Type</th>		      		      
		      <th>Vendor ContactName</th>
              <th>Facility Name</th>
		      <th>Vendor MobileNumber</th>
<!--              <th>Trip Type</th>-->
            </tr> 
        </thead>
        <tbody ng-show = "vehicleRegisteredData.length==0">
                                    <tr>
                                        <td colspan = '5'>
                                            <div class = "noData">None of the Vehicle Rolled Out</div>
                                        </td>
                                    </tr>
                                </tbody>
        <tbody ng-show = "vehicleRegisteredData.length>0">
            <tr ng-repeat = "vehicle in vehicleRegisteredData | filter: search">
            	<td class = "col-md-3">{{vehicle.vendorName}}</td>                
                <td class = "col-md-1">{{vehicle.vehicleNumber}}</td>
                <td class = "col-md-1">{{vehicle.vehicleType}}</td>                
                <td class = "col-md-1">{{vehicle.vendorContactName}}</td>
                <td class = "col-md-1">{{vehicle.facilityName}}</td>
                <td class = "col-md-4">{{vehicle.vendorMobileNumber}}</td>
<!--                <td class = "col-md-1">{{dropRequest.tripType}}</td>-->
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-success-vs" ng-click = "cancel()">Close</button>
</div>
</div>