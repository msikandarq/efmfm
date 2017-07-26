<div class = "vsModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-check edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-10 floatLeft">Drop Vehicles on the Road</div>
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
		      <th>Vendor Mobile Number</th>
		      <th>Vehicle Number</th>              		      
		      <th>Driver Name</th>
              <th>Facility Name</th>
              <th>Driver MobileNumber</th>	
            </tr> 
        </thead>
        <tbody ng-show = "dropVehicleOnRoadData.length==0">
                                    <tr>
                                        <td colspan = '5'>
                                            <div class = "noData">None of the Vehicle Rolled Out</div>
                                        </td>
                                    </tr>
                                </tbody>
        <tbody ng-show = "dropVehicleOnRoadData.length>0">
            <tr ng-repeat = "vehicle in dropVehicleOnRoadData | filter: search">
       			<td class = "col-md-1">{{vehicle.vendorName}}</td>
                <td class = "col-md-1">{{vehicle.mobilenumber}}</td>
                <td class = "col-md-1">{{vehicle.vehicleNumber}}</td>                
                <td class = "col-md-3">{{vehicle.DriverName}}</td>
                <td class = "col-md-1">{{vehicle.facilityName}}</td>
                <td class = "col-md-3">{{vehicle.DriverMobileno}}</td> 
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-success-vs" ng-click = "cancel()">Close</button>
</div>
</div>