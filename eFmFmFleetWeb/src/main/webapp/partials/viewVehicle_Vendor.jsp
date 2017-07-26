<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    It is a template - ng-include in the vendorDashboard page under Vendor Managements
@State          
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<div class = "viewVehicle_VendorDashBoard">
   <img src = "images/portlet-remove-icon.png" class="img-circle pointer" ng-click = "viewVehicles(post, $index)">
	<div class = "row">
	 <div class = "col-md-4">
		<input id = "addVehicleNew" 
			   class = "addVehicle_vendor btn btn-info" 
			   type = "button" 
			   ng-click = "addNewVehicle($index, 'lg');" 
			   value = "Add New Vehicle">						
	 </div>
	 <div class = "col-md-8">
         <input type = "text" 
                ng-model = "searchVehicles" 
                class = "searchboxDriverVehicle searchbox form-control floatRight" 
                expect_special_char
                placeholder = "Search Vehicles"></div>
	</div>
<table class = "viewVehicleTable table table-responsive container-fluid">
	<thead class = "vehicleThead">
		<tr>
		  <th>Vehicle Id</th>
		  <th>Vehicle Number</th>
		  <th>Capacity</th> 
		  <th>Mileage</th> 
		  <th>vehicle Owner Name</th>
		  <th>Insurance Valid</th>
		  <th>Permit Valid</th>
		  <th>Is Replacement</th>
		  <th>Facility Name</th>
		  <th>Action</th>
		  <th></th>
		  <th><th>
		</tr>
	</thead>
    <tbody ng-show = "vendorContractManagData[$parent.$index].vehicleData.length==0">
        <tr>
            <td colspan = '9'>
                <div class = "noData">No Vehicle found for this Vendor</div>
            </td>
        </tr>
    </tbody>
	<tbody>
	
	   <tr ng-repeat = "post in vendorContractManagData[$parent.$index].vehicleData | filter : searchVehicles"
           ng-show = 'vendorContractManagData[$parent.$index].vehicleData.length>0'>
	    	    <td><div ng-show = "!post.editVehicle_Enable">{{post.vehicleId}}</div>
	    </td>
	    <td><div ng-show = "!post.editVehicle_Enable">{{post.vehicleNumber}}</div>
	    </td>
	    <td><div ng-show = "!post.editVehicle_Enable">{{post.capacity}}</div>	
	    </td>
	    <td><div ng-show = "!post.editVehicle_Enable">{{post.mileage}}</div>	
	    </td>
		<td><div ng-show = "!post.editVehicle_Enable">{{post.vehicleOwnerName}}</div>
		</td>
		<td><div ng-show = "!post.editVehicle_Enable">{{post.InsuranceDate}}</div>
		</td>
		<td><div ng-show = "!post.editVehicle_Enable">{{post.PermitValid}}</div>
		</td>
        <td>
            <div ng-show = "!post.editVehicle_Enable">{{post.isReplacement}}</div>
        </td>
        <td>
            <div ng-show = "!post.editVehicle_Enable">{{post.facilityName}}</div>
        </td>
		<td>
		   <input id = "editVehicle" 
			      class = "editVehicle_vendor btn btn-warning btn-xs" 
			      type = "button" 
			      ng-show = "!post.editVehicle_Enable"
			   	  ng-click = "editVehicle(post, $parent.$index, $index, 'lg')" 			   	  
			      value = "View/Edit">
<!--
		   <input id = "deleteVehicle" 
			      class = "deleteVehicle_vendor btn btn-danger btn-xs" 
			      type = "button" 
			      ng-show = "!post.editVehicle_Enable"
			   	  ng-click = "deleteVehicle(post, $parent.$index, $index)" 			   	  
			      value = "Delete">
-->
            <input id = "uploadDriver" 
			      class = "uploadVehicle_vendor btn btn-success btn-xs" 
			      type = "button" 
			      ng-show = "!post.editDriver_Enable"
			   	  ng-click = "uploadVehicle(post, $parent.$index, $index)" 			   	  
			      value = "Upload">
		</td>
	   </tr>
	</tbody>
</table>
</div>