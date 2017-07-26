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

<div class = "viewDriver_VendorDashBoard">
    <img src = "images/portlet-remove-icon.png" class="img-circle pointer" ng-click = "viewDrivers(post, $index)">
	<div class = "row">
	 <div class = "col-md-4">
		<input id = "addDriverNew" 
			   class = "addDriver_vendor btn btn-primary" 
			   type = "button" 
			   ng-click = "addNewDriver($index, 'lg');" 
			   value = "Add New Driver">							
	 </div>
	 <div class = "col-md-8"><input type = "text" ng-model = "searchDrivers" class = "searchboxDriverVehicle searchbox form-control floatRight" placeholder = "Search Drivers" expect_special_char></div>
	</div>
<table class = "viewDriverTable table">
	<thead class = "driverThead">
		<tr>
		  <th>Driver Id</th>
		  <th>Name</th>
		  <th>Mobile Number</th>
		  <th>Driver Address</th>
		  <th>License No.</th>
		  <th>License Expiry</th>
		  <th>Medical Certificate Valid</th>	
		  <th>Facility Name</th>	
		  <th>Action<th>
		</tr>
	</thead>
    <tbody ng-show = "vendorContractManagData[$parent.$index].driverData.length==0">
        <tr>
            <td colspan = '10'>
                <div class = "noData">No Driver found for this Vendor</div>
            </td>
        </tr>
    </tbody>
	<tbody>	
	   <tr ng-repeat = "post in vendorContractManagData[$parent.$index].driverData | filter:searchDrivers"
           ng-show = 'vendorContractManagData[$parent.$index].driverData.length>0'>
	    <td class = 'col-md-1'><div>{{post.driverId}}</div>	    	
	    </td>
	    <td class = 'col-md-2'><div>{{post.driverName}}</div>	    	
	    </td>
	    <td class = 'col-md-1'><div>{{post.mobileNumber}}</div>	    	
	    </td>
		<td class = 'col-md-3'><div>{{post.driverAddress }}</div>			
		</td>
		<td class = 'col-md-1'><div>{{post.licenceNumber}}</div>			
		</td>
		<td class = 'col-md-1'><div>{{post.licenceValid}}</div>			
		</td>
		<td class = 'col-md-2'><div>{{post.medicalCertificateValid}}</div>			
		</td>
		<td class = 'col-md-1'><div>{{post.facilityName}}</div>			
		</td>
		<!-- <td><div>{{post.mobileNumber}}</div>			
		</td> -->
		<td class = 'col-md-1'>
		   <input id = "editDriver" 
			      class = "editDriver_vendor btn btn-warning btn-xs" 
			      type = "button" 
			      ng-show = "!post.editDriver_Enable"
			   	  ng-click = "editDriver(post, $parent.$index, $index, 'lg')" 			   	  
			      value = "Edit">
<!--
		   <input id = "deleteDriver" 
			      class = "deleteDriver_vendor btn btn-danger btn-xs" 
			      type = "button" 
			      ng-show = "!post.editDriver_Enable"
			   	  ng-click = "deleteDriver(post, $parent.$index, $index)" 			   	  
			      value = "Delete">
-->
            <input id = "uploadDriver" 
			      class = "uploadDriver_vendor btn btn-success btn-xs" 
			      type = "button" 
			      ng-show = "!post.editDriver_Enable"
			   	  ng-click = "uploadDriver(post, $parent.$index, $index)" 			   	  
			      value = "Upload">
		</td>
	   </tr>
	</tbody>
</table>
</div>