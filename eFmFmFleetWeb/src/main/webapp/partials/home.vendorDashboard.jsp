<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    This Page is a Dashboard for Admin to view Details of VENDOR, VEHICLE, DRIVERS, ESCORTS, and INSPECTIONS.
                This page is accessible from the home.serviceMapping state only
@State          home.vendorDashboard
@URL            /vendorDashboard 

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->  
 
<div class = "vendorDashboardTemplate container-fluid" ng-if = "isVendorDashboardActive">
    <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
           
          <span class="col-md-8 col-xs-8 vendorDashboardLabel">
            Vendor Management 
          </span>
          <span class="col-md-2 col-xs-2 vendorDashboardMultiSelect" ng-show="multiFacility == 'Y'">
              <am-multiselect class="input-lg pull-right"
                                       multiple="true"
                                       ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                       ng-model="facilityData"
                                       ms-header="All Facility"
                                       options="facility.branchId as facility.name for facility in facilityDetails"
                                       change="setFacilityDetails(facilityData)"
                                       >
                     </am-multiselect>
          </span>
          <span class="col-md-2 col-xs-2 vendorDashboardGetFacilityButton" ng-show="multiFacility == 'Y'">
            <input type="button" class="btn btn-success" value="Submit" name="" ng-click="getFacilityDetails(facilityData)">
          </span>


          <!-- <span class="col-md-2 col-xs-2 singleFacilityStyle" ng-show="facilitySingleDropdown && multiFacility == 'Y'" >
          </span> -->
          <span class="col-md-2 col-xs-2 singleFacilityStyle" ng-show="multiFacility == 'N'" >
          </span>
          

            <div class = "col-md-12 col-xs-12 mainTabDiv_vendorDashBoard">
            <!-- /*START OF WRAPPER1 = DRIVER*/ -->
               <div class = "wrapper1" id="driverContent">             
                
                <tabset class="tabset vendorTab_vendorDashboard"> 
                  <tab ng-click = "getContractManag(); getTabInfo('vendorManagement');">
                    <tab-heading>Vendor Management</tab-heading>
                        <div class = "contractMangTabContent row">
                            <div class = "col-md-7">
                              <button class = "addVendor button btn btn-success btn-sm" 
                                        ng-model = "addVendors" 
                                        ng-click = "addVendors('lg',facilityData)">
                              <i class = ""><span>Add Vendors</span></i>
                            </button>
                            <button class = "addVendor button btn btn-warning btn-sm" 
                                        ng-model = "addAdhocVendor" 
                                        ng-click = "addAdhocVendor('lg')">
                              <i class = ""><span>Add Adhoc Driver & Vehicle</span></i>
                            </button>
                            <button class = "addVendor button btn btn-info btn-sm" 
                                        ng-model = "addAdhocDriverModal" 
                                        ng-click = "addAdhocDriverModal('lg')">
                              <i class = ""><span>Driver CheckIn</span></i>
                            </button>

                          
                            </div>
                            <!-- <div class = "col-md-3">
                            
                            </div> -->
                            <div class = "col-md-2">
                            </div>
                          <div class = "col-md-3">
                            <input class = "form-control" ng-model = "searchVendors" placeholder = "Search Vendors"
                            expect_special_char>
                          </div>
                          
                            <div class = "col-md-12 col-xs-12 tableWrapper">
                            <table class = "contractMangTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Vendor Name</th>
                                      <th>Vendor Number</th>
                                      <th>Total Drivers</th> 
                                      <th>Total Cabs</th>
                                      <th>Facility Name</th>
                                      <th></th>
                                    </tr> 
                                </thead>
                                <tbody>
                               <tr class = "visibleRow" 
                                       ng-class = "{expandRow:post.vehicle_isClicked || post.driver_isClicked || post.supervisor_isClicked}"
                                       ng-repeat-start = "post in vendorContractManagData |limitTo: numberofRecords | filter:searchVendors">
                                     <td>{{post.vendorName}}</td>
                                     <td>{{post.vendorMobileNumber}}</td>
                                     <td>{{post.noOfDriver}}</td>
                                     <td>{{post.noOfVehicle}}</td> 
                                     <td>{{post.facilityName}}</td> 
                                     <td>
                                       <input id= "viewDriverButton_vendor{{post.vehicleId}}" 
                                          class = "viewDriverButton_vendor btn btn-primary btn-xs" 
                                          type = "button" 
                                          ng-disabled = "post.vehicle_isClicked" 
                                          ng-click = "viewDrivers(post, $index)" 
                                          value = "View Drivers">                                         
                                       <input id = "viewVehicleButton_vendor{{post.vehicleId}}" 
                                          class = "viewVehicleButton_vendor btn btn-info btn-xs" 
                                          type = "button" 
                                          ng-disabled = "post.driver_isClicked"
                                          ng-click = "viewVehicles(post, $index)" 
                                          value = "View Vehicles">
                                          <input id = "viewSupervisorButton_vendor{{post.vehicleId}}" 
                                          class = "viewSupervisorButton_vendor btn btn-success btn-xs" 
                                          type = "button" 
                                          ng-click = "viewSupervisor(post, $index)" 
                                          value = "View Supervisor">
                                             <input id = "editVendorButton_vendor{{post.vehicleId}}" 
                                          class = "editVendorButton_vendor btn btn-warning btn-xs" 
                                          type = "button"
                                          ng-click = "editVendor(post, $index, 'lg', facilityData)" 
                                          value = "View/Edit">
                                     </td>                                    
                                </tr>   
                                <tr ng-repeat-end="" id = "secondRow{{post.vehicleId}}" 
                                  class = "secondRow_VendorContractManag"
                                  ng-show = "post.driver_isClicked || post.vehicle_isClicked || post.supervisor_isClicked">
                                  <td colspan="6">
                                  <div id = "driver{{post.vehicleId}}" 
                                     ng-show = "post.driver_isClicked" 
                                     class = "viewDriver" 
                                     ng-include = "'partials/viewDriver_Vendor.jsp'"></div>
                                  <div id = "vehicle{{post.vehicleId}}" 
                                       ng-show = "post.vehicle_isClicked" 
                                       class = "viewVehicle" 
                                       ng-include = "'partials/viewVehicle_Vendor.jsp'"></div>
                                  <div>      
                                  <div id = "supervisor{{post.vehicleId}}" 
                                       ng-show = "post.supervisor_isClicked" 
                                       class = "viewSupervisor" 
                                       ng-include = "'partials/viewSupervisor_Vendor.jsp'"></div>
                                  <div>                                
                  </div>
                  </td>                                 
                                </tr>                                
                             </tbody>                                
                            </table> 
                          </div> 
                        </div>
                    </tab>
                     
                    <tab ng-click = "getEscortDetails(); getTabInfo('escortManagement')">
                    <tab-heading>Escort Management</tab-heading> 
                      <div class = "escortDetailsTabContent row">
                            <div class = "col-md-3">
                              <button class = "addEscort button btn btn-success" ng-model = "addEscorts" ng-click = "addEscort(facilityData)">
                              <i class = ""><span>Add New Escort</span></i>
                            </button>
                            </div>
                            <div class = "col-md-3">
                </div>
                            <div class = "col-md-3"></div>
                          <div class = "col-md-3">
                            <input class = "searchEscortDetail form-control"           
                                       expect_special_char
                                       type = "text" 
                                       ng-model = "searchEscortDetails" 
                                       placeholder = "Search Escort">
                          </div>  

                          <div class="invoiceTemplate container-fluid">
  <div class="row">
    <div class="col-md-12 col-xs-12 invoiceHeading1">
      <div class="col-md-12 col-xs-12 mainTabDiv_invoice">
        <div class="wrapper1">
          <tabset class="tabset subTab_invoice"> <tab
            ng-click="getEscortDetails(); getTabInfo('escortManagementActive')"> <tab-heading>Escort Management - Active</tab-heading>
          <div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
            <div class="employeeListContent">
              <div class="row paddingLeftRight10">
                  <table class = "escortDetailTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Escort Id</th>
                                      <th>Escort Name</th>
                                      <th>Escort Number</th>
                                      <th>Facility Name</th>
                                      <th>Address</th>
                                      <th>Action</th>
                                    </tr> 
                                </thead>

                                <tbody ng-show="escortDetailData.length == 0">
                                  <tr>
                                   <td colspan='14'>
                                    <div class="noData">There are no Active escort details</div>
                                   </td>
                                  </tr>
                                 </tbody>

                                <tbody ng-show="escortDetailData.length > 0">
                               <tr class = "visibleRow" 
                                       ng-repeat-start = "post in escortDetailData |limitTo: numberofRecords | filter:searchEscortDetails">
                                     <td class = "col-md-1">{{post.escortId}}</td>
                                     <td class = "col-md-2">{{post.escortName}}</td>   
                                     <td class = "col-md-2">{{post.escortMobileNumber}}</td>
                                     <td class = "col-md-2">{{post.facilityName}}</td>
                                     <td class = "col-md-3">{{post.escortAddress}}</td>
                                         <td class = "col-md-4">
                                             <input id= "viewEscortDetailButton_vendor" 
                                          class = "viewEscortDetail_vendor btn btn-primary btn-xs escortActionButton" 
                                          type = "button"  
                                          ng-click = "viewEscortDetail(post, $index)" 
                                          value = "View Detail"><br/>  
                                             <input id= "editEscortButton_vendor{{post.regId}}" 
                                          class = "viewEscortDetail_vendor btn btn-warning btn-xs escortActionButton"
                                          type = "button"  
                                          ng-click = "editEscortDetail(post, $index,'lg')" 
                                          value = "Edit"><br/> 
                                             <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-success btn-xs escortActionButton" 
                                                    type = "button"
                                                    ng-click = "uploadEscort(post, $parent.$index, $index)"             
                                                    value = "Upload"><br/> 
                                            <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-danger btn-xs escortActionButton" 
                                                    type = "button"
                                                    ng-click = "disableEscort(post,$index)"             
                                                    value = "Disable">
                                   </td>  
                                </tr> 
                                    <tr ng-repeat-end=""
                                        ng-show = "post.isClicked"
                                        ng-class = "escort{{post.vehicleId}}" 
                                        class = "unVisisbleRow">
                                        <td colspan="5" class = "unVisisbleRowtd">
                                            <div class = "row">
                                                <div class = "col-md-12 escortLabel">
                                                    <img src="images/portlet-remove-icon.png" class="floatRight pointer" ng-click="closeEscortDetail(post)"></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Escort Id</span><br><span>{{escortDetails[0].escortId}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Full Name</span><br><span>{{escortDetails[0].escortName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Father's Name</span><br><span>{{escortDetails[0].escortFatherName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Gender</span><br><span>{{escortDetails[0].escortGender}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Date of Birth</span><br><span>{{escortDetails[0].escortdob}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Mobile Number</span><br><span>{{escortDetails[0].escortMobileNumber}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Pincode</span><br><span>{{escortDetails[0].escortPincode}}</span></div>   
                                                <div class = "col-md-4 escortLabel">
                                                        <span>Address</span><br><span>{{escortDetails[0].escortAddress}}</span></div>                                                                                      <div class = "col-md-4 escortLabel">
                                                        <span>Remarks</span><br><span>{{escortDetails[0].remarks}}</span></div> 
                                            </div>
                                        </td>
                                    </tr>
                             </tbody>
                            </table>
                
              </div>
            </div>
            
            <div class="row">
              <div class="wrapper2_EmpTravelDesk" id="employeeTravelDeskExport">
              
                
              </div>
            </div>
          </div>
          </tab> 

          <tab ng-click="getEscortDetailsDisable(); getTabInfo('escortManagementInActive')"> 
              <tab-heading>Escort Management - InActive</tab-heading>
                  <div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
                    
                    <div class="employeeListContent">
                      <!-- /*FIRST ROW*/-->
                      <div class="row paddingLeftRight10">

                         <table class = "escortDetailTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Escort Id</th>
                                      <th>Escort Name</th>
                                      <th>Escort Number</th>
                                      <!-- <th>Facility Name</th> -->
                                      <th>Address</th>
                                      <th>Action</th>
                                    </tr> 
                                </thead>

                                <tbody ng-show="escortDetailDataDisable.length == 0">
                                  <tr>
                                   <td colspan='14'>
                                    <div class="noData">There are no In Active escort details</div>
                                   </td>
                                  </tr>
                                 </tbody>

                                <tbody ng-show="escortDetailDataDisable.length > 0">
                               <tr class = "visibleRow" 
                                       ng-repeat-start = "post in escortDetailDataDisable |limitTo: numberofRecords | filter:searchEscortDetails">
                                     <td class = "col-md-1">{{post.escortId}}</td>
                                     <td class = "col-md-2">{{post.escortName}}</td>   
                                     <td class = "col-md-2">{{post.escortMobileNumber}}</td>
                                     <!-- <td class = "col-md-2">{{post.facilityName}}</td> -->
                                     <td class = "col-md-3">{{post.escortAddress}}</td>
                                         <td class = "col-md-4">
                                            <!--  <input id= "viewEscortDetailButton_vendor" 
                                          class = "viewEscortDetail_vendor btn btn-primary btn-xs" 
                                          type = "button"  
                                          ng-click = "viewEscortDetail(post, $index)" 
                                          value = "View Detail">   -->
                                             <!-- <input id= "editEscortButton_vendor{{post.regId}}" 
                                          class = "viewEscortDetail_vendor btn btn-warning btn-xs" 
                                          type = "button"  
                                          ng-click = "editEscortDetail(post, $index,'lg')" 
                                          value = "Edit">
                                             <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-success btn-xs" 
                                                    type = "button"
                                                    ng-click = "uploadEscort(post, $parent.$index, $index)"             
                                                    value = "Upload"> -->

                                            <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-success btn-xs" 
                                                    type = "button"
                                                    ng-click = "enableEscort(post,$index)" value = "Enable">

                                   </td>  
                                </tr> 
                                    <tr ng-repeat-end=""
                                        ng-show = "post.isClicked"
                                        ng-class = "escort{{post.vehicleId}}" 
                                        class = "unVisisbleRow">
                                        <td colspan="5" class = "unVisisbleRowtd">
                                            <div class = "row">
                                                <div class = "col-md-12 escortLabel">
                                                    <img src="images/portlet-remove-icon.png" class="floatRight pointer" ng-click="closeEscortDetail(post)"></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Escort Id</span><br><span>{{escortDetails[0].escortId}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Full Name</span><br><span>{{escortDetails[0].escortName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Father's Name</span><br><span>{{escortDetails[0].escortFatherName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Gender</span><br><span>{{escortDetails[0].escortGender}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Date of Birth</span><br><span>{{escortDetails[0].escortdob}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Mobile Number</span><br><span>{{escortDetails[0].escortMobileNumber}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Pincode</span><br><span>{{escortDetails[0].escortPincode}}</span></div>   
                                                <div class = "col-md-4 escortLabel">
                                                        <span>Address</span><br><span>{{escortDetails[0].escortAddress}}</span></div>                                                                                      <div class = "col-md-4 escortLabel">
                                                        <span>Remarks</span><br><span>{{escortDetails[0].remarks}}</span></div> 
                                            </div>
                                        </td>
                                    </tr>
                             </tbody>
                            </table>

                      </div>
                    </div>
                  </div>
              </tab> 
          </tabset>
        </div>
      </div>
    </div>
  </div>
</div>                    
</div>
</tab>
                    

                    
                   <tab ng-click = "getVehicleCheckedIn();getTabInfo('checkInDrivers')"> 
                       <tab-heading>Check In Drivers</tab-heading>
                        <div class = "vehickeCheckInTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft" >
                                <h5 class="lengthVendorDashboard">Check In Drivers Summary Report - <span class="badge">{{vehicleCheckInDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "searchCheckInVehicle" class = "form-control" placeholder = "Search Check In Vehicle"
                                    expect_special_char>
                                </div>
                            </div>
                            <table class = "vehickeCheckInTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                        <th>Vendor Name</th> 
                                        <th>Vehicle Number</th> 
                                        <th>Vehicle Capacity</th> 
                                        <th>Vehicle Make</th>                                    
                                        <th>Device Id</th>
                                        <th>Driver Name</th>
                                        <th>Driver Id</th>
                                        <th>Driver Mobile Number</th>
                                        <th>Facility Name</th>
                                        <th></th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "vehicleCheckInData.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">Found No Vehicle for Check-In</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "vehicleCheckInData.length>0">
                               <tr class = "vehicle{{post.vehicleId}}" 
                                       ng-repeat = "post in vehicleCheckInData |limitTo: numberofRecords | filter : searchCheckInVehicle">
                                     <td>{{post.vendorName}}</td>
                                     <td>{{post.vehicleNumber}}</td>
                                     <td>{{post.capacity}}</td>
                                     <td>{{post.vehicleMake}}</td>                                     
                                      <td>{{post.deviceId}}</td>               
                                     <td>{{post.DriverName}}</td> 
                                     <td>{{post.driverId}}</td> 
                                     <td>{{post.MobileNumber}}</td> 
                                     <td>{{post.facilityName}}</td> 
                                   <td>
                                             <input id= "changeEntity_button{{post.vehicleId}}" 
                                          class = "editEntity_vendor btn btn-warning btn-xs" 
                                          type = "button"  
                                          ng-click = "editEntity(post, $index)" 
                                          value = "Edit Entity">  
                                       </td> 
                                </tr>                                   
                             </tbody>
                            </table>
                        </div>
                    </tab>
                
                 
                    <tab ng-click = "getAvailableVehicle(); getTabInfo('availableDriver')">
                    <tab-heading>Available Driver</tab-heading>
                      <div class = "availableVehicleTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Available Drivers Summary Report - <span class="badge">{{availableVehicleDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "searchAvailableVehicle" class ="form-control" placeholder = "Search Available Vehicle"
                                    expect_special_char>
                                </div>
                            </div>
                            <table class = "availableVehicleTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                       <th>CheckInId</th>
                                      <th>Driver Number</th>
                                      <th>Device Id</th>
                                      <th>Driver Name</th>
                                      <th>Driver Id</th>
                                      <th>Vehicle Number</th>
                                      <th>Capacity</th> 
                                       <th>Vehicle Make</th>   
                                       <th>Facility Name</th>        
                                      <th>Status</th>
                                      <th>CheckOut</th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "availableVehicleData.length==0">
                                    <tr>
                                        <td colspan = '11'>
                                            <div class = "noData">Found No Available Vehicle</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "availableVehicleData.length>0">
                               <tr ng-repeat = "post in availableVehicleData  |limitTo: numberofRecords | filter:searchAvailableVehicle"
                                       class = 'vehicle{{post.checkInId}}'>
                                        <td>{{post.checkInId}}</td>
                                        <td>{{post.driverNumber}}</td>
                                        <td>{{post.deviceId}}</td>                                    
                                        <td>{{post.driverName}}</td>
                                        <td>{{post.driverId}}</td>
                                        <td>{{post.vehicleNumber}}</td>
                                        <td>{{post.capacity}}</td>
                                        <td>{{post.vehicleMake}}</td>  
                                        <td>{{post.facilityName}}</td>  
                                        <td>{{post.status}}</td>
                                        <td>
                                             <input type = 'button' 
                                                    class = 'btn btn-success checkout_vendorDashboard' 
                                                    value = 'Checkout'
                                                    ng-click = 'checkoutVehicle(post)'>
                                        </td>
                                </tr>                                   
                             </tbody>
                            </table>
                        </div>
                    </tab>                 
                
                    
                    <!-- DRIVERS ON ROAD TAB -->
                    <tab ng-click = "getDriversOnRoad(); getTabInfo('driversOnRoad')">
                    <tab-heading>Drivers On Road</tab-heading>
                      <div class = "escortAvailableTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Drivers On Road Summary Report - <span class="badge">{{driversOnRoadDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "driversOnRoad" class = "form-control" placeholder = "Search driver on road"
                                    expect_special_char>
                                </div>
                            </div>
                            <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                     <th>Vendor Name</th> 
                                      <th>Vehicle Number</th> 
                                      <th>Vehicle Capacity</th> 
                                      <th>Vehicle Make</th>                                    
                                      <th>Device Id</th>
                                      <th>Driver Name</th>
                                      <th>Driver Id</th>
                                       <th>Driver Mobile Number</th>
                                       <th>Facility Name</th>
                                      <th></th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "driversOnRoadData.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">No Record Found</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "driversOnRoadData.length>0">
                               <tr ng-repeat = "driver in driversOnRoadData |limitTo: numberofRecords | filter:driversOnRoad">
                                    <td>{{driver.vendorName}}</td>
                                     <td>{{driver.vehicleNumber}}</td>
                                     <td>{{driver.capacity}}</td>
                                     <td>{{driver.vehicleMake}}</td>                                     
                                      <td>{{driver.deviceId}}</td>               
                                     <td>{{driver.DriverName}}</td> 
                                     <td>{{driver.driverId}}</td> 
                                     <td>{{driver.MobileNumber}}</td> 
                                     <td>
                                     <td>{{driver.facilityName}}</td> 
                                     <td>
                                                  
                                         </td>
                                </tr>                                   
                             </tbody>
                            </table>
                        </div>
                    </tab>
                    
                    <!-- ------------------------------------------------------------------------------------------- -->
                 <!--    <tab ng-click = "getVehicleCheckedIn()"> 
                       <tab-heading>Planned Release Drivers</tab-heading>
                        <div class = "vehickeCheckInTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                 <h5 class="lengthVendorDashboard">Planned Release Drivers Summary Report - <span class="badge">{{vehicleCheckInDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "searchCheckInVehicle" class = "form-control" placeholder = "Search planned leave driver">
                                </div>
                            </div>
                            <table class = "vehickeCheckInTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                        <th>Vendor Name</th> 
                                        <th>Vehicle Number</th> 
                                        <th>Vehicle Capacity</th> 
                                        <th>Vehicle Make</th>                                    
                                        <th>Device Id</th>
                                        <th>Driver Name</th>
                                        <th>Driver Mobile Number</th>
                                        <th></th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "vehicleCheckInData.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">Found No Vehicle for Check-In</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "vehicleCheckInData.length>0">
                               <tr class = "vehicle{{post.vehicleId}}" 
                                       ng-repeat = "post in vehicleCheckInData |limitTo: numberofRecords | filter : searchCheckInVehicle">
                                     <td>{{post.vendorName}}</td>
                                     <td>{{post.vehicleNumber}}</td>
                                     <td>{{post.capacity}}</td>
                                     <td>{{post.vehicleMake}}</td>                                     
                                      <td>{{post.deviceId}}</td>               
                                     <td>{{post.DriverName}}</td> 
                                     <td>{{post.MobileNumber}}</td> 
                                   <td>
                                             <input id= "changeEntity_button{{post.vehicleId}}" 
                                          class = "editEntity_vendor btn btn-success btn-xs" 
                                          type = "button"  
                                          ng-click = "releaseDriver(post, $index)" 
                                          value = "Release Driver">   
                                       </td> 
                                </tr>                                   
                             </tbody>
                            </table>
                        </div>
                    </tab>
 -->                    <!-- ------------------------------------------------------------------------------------------- -->
                   <tab ng-click = "getEscortCheckIn(); getTabInfo('escortCheckIn')">
                      <tab-heading>Escort Check-In</tab-heading>
                      <div class = "escortCheckInTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Escort Check-In Summary Report - <span class="badge">{{escortCheckInDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "escortDetailSearch" class = "form-control" placeholder = "Search Escort"
                                    expect_special_char>
                                </div>
                            </div>
                                     
                            <table class = "escortCheckInTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                     <th>Escort Id</th>                                    
                                      <th>Escort Name</th>
                                      <th>Escort Number</th>
                                      <th>Address</th>
                                      <th>Facility Name</th>
                                      <th>Check In</th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "escortCheckInData.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">Found No Escort for Check-In</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "escortCheckInData.length>0">
                               <tr class = "escort{{post.escortId}}" ng-repeat = "post in escortCheckInData | filter:escortDetailSearch">
                                     <td>{{post.escortId}}</td>
                                     <td>{{post.escortName}}</td> 
                                     <td>{{post.escortMobileNumber}}</td>                                        
                                                                            
                                     <td>{{post.escortAddress}}</td>
                                     <td>{{post.facilityName}}</td>
                                         <td ng-click = "select_EscortCheckIn(post)">
                                             <i ng-hide = "post.checkBoxFlag" class = "icon-check-empty"></i>
                                             <i ng-show = "post.checkBoxFlag" class = "icon-check icon-check-custom"></i>
                                   </td>
                                </tr>                                   
                             </tbody>
                            </table>
                            
                            <div class = "row lastRowActionDiv">
                                <div class = "col-md-12 col-xs-12">
                                    <span></span>
                                </div>
                                    
                                <div class = "col-md-12 col-xs-12" ng-if="0!=escortCheckInData">
                                    <input type = "button" 
                                           class = "submitEscortCheckIn btn btn-default" 
                                           value="Cancel"
                                           ng-click = "cancelEscortCheckIn()"> 
                                    <input type = "button" 
                                           class = "submitEscortCheckIn btn btn-success" 
                                           value="Check In" 
                                           ng-click = "submitEscortCheckIn($index)">
                                </div>
                            </div>
                        </div>
                    </tab>
                    
                    <tab ng-click = "getYetToCheckInVehicle(); getTabInfo('YetToCheckInVehicle')">
                      <tab-heading>Yet To checkIn - Vehicle</tab-heading>
                      <div class = "escortCheckInTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Yet To CheckIn Vehicle Summary Report - <span class="badge">{{checkInvehicleDataVehicle.length}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "YetToCheckInSearch" class = "form-control" placeholder = "Search Yet To CheckIn" expect_special_char>
                                </div>
                            </div>
                                     
                            <table class = "escortCheckInTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                     <th>Vendor Name</th>                                    
                                      <th>Vehicle Number</th>
                                      <th>Vehicle Id</th>
                                      <th>Vehicle Modal</th>
                                      <th>Facility Name</th>
                                      <th>Capcity</th>
                                      <!-- <th>CheckOut Time</th> -->
                                    </tr> 
                                </thead>
                                <tbody ng-show = "checkInvehicleDataVehicle.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">Found No Vehicle for Check-In</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "checkInvehicleDataVehicle.length>0">
                               <tr ng-repeat = "post in checkInvehicleDataVehicle">
                                     <td>{{post.vendorName}}</td>
                                     <td>{{post.vehicleNumber}}</td> 
                                     <td>{{post.vehicleId}}</td>                                        
                                     <td>{{post.vehicleMake}}</td>
                                     <td>{{post.facilityName}}</td>
                                     <td>{{post.capcity}}</td>
                                </tr>                                   
                             </tbody>
                            </table>
                        
                        </div>
                    </tab>

                    <tab ng-click = "getYetToCheckInDriver(); getTabInfo('yetTocheckInDriver')">
                      <tab-heading>Yet To checkIn - Driver</tab-heading>
                      <div class = "escortCheckInTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Yet To CheckIn Driver Summary Report - <span class="badge">{{checkInvehicleDataDriver.length}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "YetToCheckInSearch" class = "form-control" placeholder = "Search Yet To CheckIn" expect_special_char>
                                </div>
                            </div>
                                      
                            <table class = "escortCheckInTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                     <th>Vendor Name</th>                                    
                                      <th>Vehicle Number</th>
                                      <th>Vehicle Id</th>
                                      <th>Vehicle Modal</th>
                                      <th>Facility Name</th>
                                      <th>Capcity</th>
                                      <!-- <th>CheckOut Time</th> -->
                                    </tr> 
                                </thead>
                                <tbody ng-show = "checkInvehicleDataDriver.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">Found No Driver for Check-In</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "checkInvehicleDataDriver.length>0">
                               <tr ng-repeat = "post in checkInvehicleDataDriver">
                                     <td>{{post.vendorName}}</td>
                                     <td>{{post.vehicleNumber}}</td> 
                                     <td>{{post.vehicleId}}</td>                                        
                                     <td>{{post.vehicleMake}}</td>
                                     <td>{{post.facilityName}}</td>
                                     <td>{{post.capcity}}</td>
                                </tr>                                   
                             </tbody>
                            </table>
                        
                        </div>
                    </tab>

                    <tab ng-click = "getEscortAvailable(); getTabInfo('escortAvailable')">
                    <tab-heading>Escort Available</tab-heading>
                      <div class = "escortAvailableTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Escort Available Summary Report - <span class="badge">{{escortAvailableDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "escortAvailable" class = "form-control" placeholder = "Search Escort Available"
                                    expect_special_char>
                                </div>
                            </div>
                            <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Escort id</th>
                                    
                                      <th>Escort Name</th>
                                      <th>Escort Number</th>
                                      <th>Facility Name</th>
                                      <th>Address</th>
                                      <th>Action</th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "escortAvailableData.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">Found No Available Escort</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "escortAvailableData.length>0">
                               <tr  class = "escort{{post.escortId}}"  ng-repeat = "post in escortAvailableData |limitTo: numberofRecords | filter:escortAvailable">
                                    <td>{{post.escortId}}</td>
                                     <td>{{post.escortName}}</td> 
                                     <td>{{post.escortMobileNumber}}</td>                                        
                                      <td>{{post.facilityName}}</td>   
                                     <td>{{post.escortAddress}}</td>
                                         <td>
                                             <input type = 'button' 
                                                    class = 'btn btn-success checkout_vendorDashboard' 
                                                    value = 'Checkout'
                                                    ng-click = 'checkoutEscort(post)'>
                                         </td>
                                </tr>                                   
                             </tbody>
                            </table>
                        </div>
                    </tab>
                    
                    <!-- DEVICE DETAIL TAB -->
                    <tab ng-click = "getDeviceDetail(); getTabInfo('deviceDetails')">
                    <tab-heading>Device Detail</tab-heading>
                      <div class = "escortAvailableTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft">
                                <h5 class="lengthVendorDashboard">Device Detail Summary Report - <span class="badge">{{deviceDetailDataLength}}</span></h5>
                                </div>
                                <div class = "floatRight">
                                    <input type = "text" ng-model = "deviceSearch" class = "form-control" placeholder = "Search device"
                                    expect_special_char>
                                </div>
                            </div>
                            <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                    <th>Device Id</th>
                                      <th>Device Type</th>
                                      <th>Device Model</th>
                                      <th>Device Version</th>
                                      <th>Device Mobile Number</th>
                                      <th>Device Status</th>
                                      <th>Facility Names</th>
                                      <th>Enable</th>
                                      <th>Disable</th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "deviceDetailData.length==0">
                                    <tr>
                                        <td colspan = '9'>
                                            <div class = "noData">No Record Found</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "deviceDetailData.length>0">
                               <tr ng-repeat = "device in deviceDetailData |limitTo: numberofRecords | filter:deviceSearch">
                                    <td>{{device.deviceId}}</td>
                                    <td>{{device.driverType}}</td>
                                     <td>{{device.deviceModel}}</td>                                        
                                     <td>{{device.deviceOs}}</td>
                                     <td>{{device.mobileNumber}}</td>                                        
                                     <td>{{device.deviceStatus}}</td>
                                     <td>{{device.facilityName}}</td>
                                     <td>
                                             <input type = "button"
                                                    class = "btn btn-success btn-xs enable_vendorDeviceDetail"
                                                    value = "Enable"
                                                    ng-click = "enableDevice(device)"
                                                    ng-class = "{disabled: device.deviceStatus=='Y'}"
                                                    ng-disabled="device.deviceStatus == 'Y'">
                                                  
                                         </td>                                        
                                     <td>
                                             <input type = "button"
                                                    class = "btn btn-danger btn-xs disable_vendorDeviceDetail"
                                                    value = "Disable"
                                                    ng-click = "disableDevice(device)"
                                                    ng-class = "{disabled: device.deviceStatus=='N'}"
                                                    ng-disabled="device.deviceStatus == 'N'"></td>
                                </tr>                                   
                             </tbody>
                            </table>
                        </div>
                    </tab>
                    
                    <!-- -------------------------------------------------------------------------------------->
                    
                    <!-- Vehicle Inspection TAB -->
                    <tab ng-click = "getVehicleInspection('inspectionForm');getTabInfo('inspectionForm')">
                    <tab-heading>Vehicle Inspection Form</tab-heading>
                      <div class = "escortAvailableTabContent row" ng-if = "isAndroidDevice || isiOSDevice || adminRole ">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft col-md-2">
                                    <select ng-model="inspection.vendor"
                                           class="form-control marginBottom10" 
                                           ng-options="allInspectionVendor.name for allInspectionVendor in allInspectionVendors track by allInspectionVendor.Id"
                                           ng-change = "setVendor(inspection.vendor)"
                                           >
                                     <option value="">SELECT VENDOR</option>
                                  </select>
                                </div>

                                <div class = "floatLeft col-md-2">
                                    <select ng-model="inspection.vehicleSelected"
                                           class="form-control" 
                                           ng-options="allVehicle.vehicleNumber for allVehicle in allVehiclesInpectionForm track by allVehicle.Id"
                                           ng-change = "setInspectionForVehicle(inspection.vehicleSelected)">
                                     <option value="">SELECT VEHICLE</option>
                                  </select>
                                </div>
                               
                                <div class = "floatLeft col-md-2">
                                 <input type="text" 
                                        name="" 
                                        placeholder="Driver Name" 
                                        ng-model="driverSelected"
                                        class="form-control" ng-disabled='true'>
                                </div>

                                <div class = "floatLeft col-md-2">
                                 <input type="text" 
                                        name="" 
                                        placeholder="Vehicle Type" 
                                        ng-model="vehicleTypeSelected"
                                        class="form-control" ng-disabled='true'>
                                </div>

                                <div class = "col-md-2" ng-show="checkAllButtonEnabled">
                                <input type="button" 
                                       name="" 
                                       class="btn btn-success" 
                                       value="Check All"
                                       ng-click="checkAllInspection()"
                                       ng-show="checkAllShowInspection">
                                <input type="button" 
                                       name="" 
                                       class="btn btn-danger" 
                                       value="Deselect All"
                                       ng-click="deselectAllInspection()"
                                       ng-show="!checkAllShowInspection">
                                </div>
                                <div class = "col-md-5"> 
                                </div>
                                <div class = "col-md-3"> 
                                </div>
                            </div>
                            
                           
                           <div ng-show = "showInspectionTable && !inspection.vehicleSelected.vehicleNumber == ''" >
                                <div class = 'page1 col-md-12'> 
                                    <form name = "formInspOne.pageOne" >
                                        <table class = "escortAvailableTable table table-responsive container-fluid">
                                            <thead class ="tableHeading">
                                                <tr>
                                                 <th>Vendor Name</th> 
                                                  <!-- <th>Vehicle Number</th> 
                                                  <th>Vehicle Type</th> --> 

                                                  <th>Vehicle Documents</th>                                    
                                                  <th>List of items any one missing/expired</th>
                                                  <th>Interior</th>

                                                </tr> 
                                            </thead>
                                            <tbody class = 'bottomBorder1px'>
                                               <tr>
                                                    <td class = 'col-md-2'>{{vendorSelectedForInspection}}</td>
                                                    <!-- <td class = 'col-md-1'>{{vehicleSelectedForInspection}}</td>
                                                    <td class = 'col-md-1'>{{vehicleSelectedTypeForInspection}}</td> -->
                                                    <td class = 'col-md-2'>

                                                        
                                            <!-- RC Document-->

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.documentRc.text" 
                                            ng-true-value=true 
                                            ng-false-value=false 
                                            ng-checked ="checkboxAll">

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.documentRc.text" >RC Document</span>

                                            <span  ng-false-value=false class="textShow"  ng-show="inspection.documentRc.text" >RC Document</span><br>
                                            <input type="text" 
                                                   class ="remarks" 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.documentRc.remarks" 
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   ng-hide="inspection.documentRc.text"
                                                   ng-required = "inspection.documentRc.text=='false' || !inspection.documentRc.text"><br>  

                                            <!-- Insurance Document-->                           

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.documentInsurance.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll"> 


                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.documentInsurance.text" >Insurance Document</span>

                                            <span  ng-false-value=false class="textShow" ng-show="inspection.documentInsurance.text" >Insurance Document</span><br>

                                            <input type="text" 
                                                   placeholder="Remarks" 
                                                   class ="remarks" 
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   ng-model="inspection.documentInsurance.remarks" 
                                                   ng-hide="inspection.documentInsurance.text"
                                                   ng-required = "inspection.documentInsurance.text=='false' || !inspection.documentInsurance.text"><br>  


                                            <!-- Driver License -->      

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.documentDriverLicence.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                               
                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.documentDriverLicence.text" >Driver License</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.documentDriverLicence.text" >Driver License</span><br>

                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char 
                                                   ng-model="inspection.documentDriverLicence.remarks" 
                                                   ng-hide="inspection.documentDriverLicence.text"
                                                   ng-required = "inspection.documentDriverLicence.text=='false' || !inspection.documentDriverLicence.text"> <br> 

                                            <!-- Update JMP -->  

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.documentUpdateJmp.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">     
                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.documentUpdateJmp.text" >Update JMP</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.documentUpdateJmp.text" >Update JMP</span><br>

                                            <input type="text" 
                                                   class="remarks"
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.documentUpdateJmp.remarks" 
                                                   ng-hide="inspection.documentUpdateJmp.text"
                                                   ng-required = "inspection.documentUpdateJmp.text=='false' || !inspection.documentUpdateJmp.text"> <br>  

                                            </td> 

                                            <!-- First Aid Kit -->  

                                            <td class = 'col-md-2'>
                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.firstAidKit.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll"> 


                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.firstAidKit.text" >First Aid Kit</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.firstAidKit.text" >First Aid Kit</span><br>

                                            <input type="text" 
                                                   class="remarks" 
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.firstAidKit.remarks" 
                                                   ng-hide="inspection.firstAidKit.text"
                                                   ng-required = "inspection.firstAidKit.text=='false' || !inspection.firstAidKit.text"> <br> 

                                            <!-- Fire Extinguisher --> 

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.fireExtingusher.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.fireExtingusher.text" >Fire Extinguisher</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.fireExtingusher.text" >Fire Extinguisher</span><br>
                                                
                                            <input type="text" 
                                                   class="remarks" 
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.fireExtingusher.remarks" 
                                                   ng-hide="inspection.fireExtingusher.text"                                                   
                                                   ng-required = "inspection.fireExtingusher.text=='false' || !inspection.fireExtingusher.text"> <br> 

                                            <!-- All Seat Belt Working --> 

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.allSeatBeltsWorking.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.allSeatBeltsWorking.text" >All Seat Belt Working</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.allSeatBeltsWorking.text" >All Seat Belt Working</span><br>
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   ng-model="inspection.allSeatBeltsWorking.remarks"
                                                   ng-hide="inspection.allSeatBeltsWorking.text"                                              
                                                   ng-required = "inspection.allSeatBeltsWorking.text=='false' || !inspection.allSeatBeltsWorking.text"> <br> 

                                            <!-- Placard --> 

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.placard.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          


                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.placard.text" >Placard</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.placard.text" >Placard</span><br>
                                            <input type="text" 
                                                   class="remarks" placeholder="Remarks"
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char 
                                                   ng-model="inspection.placard.remarks"
                                                   ng-hide="inspection.placard.text"                                        
                                                   ng-required = "inspection.placard.text=='false' || !inspection.placard.text"> <br> 

                                            <!-- Mosquito Bat -->                                 

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.mosquitoBat.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">        

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.mosquitoBat.text" >Mosquito Bat</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.mosquitoBat.text" >Mosquito Bat</span><br>
                                            <input type="text" 
                                                   class="remarks"
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.mosquitoBat.remarks" 
                                                   ng-hide="inspection.mosquitoBat.text"                                      
                                                   ng-required = "inspection.mosquitoBat.text=='false' || !inspection.mosquitoBat.text">  <br> 

                                            <!-- Panic Button -->    

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.panicButton.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.panicButton.text" >Panic Button</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.panicButton.text" >Panic Button</span><br>

                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   ng-model="inspection.panicButton.remarks" 
                                                   ng-hide="inspection.panicButton.text"                                      
                                                   ng-required = "inspection.panicButton.text=='false' || !inspection.panicButton.text"> <br> 

                                            <!-- Walky Talky -->    

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.walkyTalky.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.walkyTalky.text" >Walky Talky</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.walkyTalky.text" >Walky Talky</span><br>
                                            <input type="text" 
                                                   class="remarks"
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.walkyTalky.remarks" 
                                                   ng-hide="inspection.walkyTalky.text"                                   
                                                   ng-required = "inspection.walkyTalky.text=='false' || !inspection.walkyTalky.text">  <br> 

                                            <!-- GPS -->  

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.gps.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          
                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.gps.text" >GPS</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.gps.text" >GPS</span><br>
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                     ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char
                                                   ng-model="inspection.gps.remarks" 
                                                   ng-hide="inspection.gps.text"     
                                                   ng-required = "inspection.gps.text=='false' || !inspection.gps.text">  <br> 

                                            <!-- Driver Uniform --> 

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.driverUniform.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.driverUniform.text" >Driver Uniform</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.driverUniform.text" >Driver Uniform</span><br>
                                            <input type="text" 
                                                   class="remarks" 
                                                    ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.driverUniform.remarks" 
                                                   ng-hide="inspection.driverUniform.text"                                
                                                   ng-required = "inspection.driverUniform.text=='false' || !inspection.driverUniform.text"> <br> 

                                            <!-- Stepney -->                

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.stephney.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.stephney.text" >Stepney</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.stephney.text" >Stepney</span><br>          
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char
                                                   ng-model="inspection.stephney.remarks"  
                                                   ng-hide="inspection.stephney.text"                            
                                                   ng-required = "inspection.stephney.text=='false' || !inspection.stephney.text">  <br>    


                                            <!-- Umbrella -->   

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.umbrella.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.umbrella.text" >Umbrella</span>

                                            <span  ng-false-value=true class="textShow"  ng-show="inspection.umbrella.text" >Umbrella</span><br>         
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.umbrella.remarks" 
                                                   ng-hide="inspection.umbrella.text"
                                                    ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                     expect_special_char                       
                                                   ng-required = "inspection.umbrella.text=='false' || !inspection.umbrella.text"> <br> 

                                            <!-- Torch -->               

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.torch.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.torch.text" >Torch</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.torch.text" >Torch</span><br>  
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"  
                                                   ng-model="inspection.torch.remarks" 
                                                   ng-hide="inspection.torch.text"
                                                    ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                     expect_special_char                    
                                                   ng-required = "inspection.torch.text=='false' || !inspection.torch.text"> <br>      

                                            <!-- Toolkit -->           

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.toolkit.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide"  ng-hide="inspection.toolkit.text" >Toolkit</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.toolkit.text" >Toolkit</span><br>  
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.toolkit.remarks" 
                                                   ng-hide="inspection.toolkit.text"  
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char                
                                                   ng-required = "inspection.toolkit.text=='false' || !inspection.toolkit.text"> <br>   

                                            </td> 

                                            <!-- Seat Upholstery is Clean / Not Torn -->

                                            <td class = 'col-md-4'>
                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.seatUpholtseryCleanNotTorn.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll"> 

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.seatUpholtseryCleanNotTorn.text" >Seat Upholstery is Clean / Not Torn</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.seatUpholtseryCleanNotTorn.text" >Seat Upholstery is Clean / Not Torn</span><br>
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.seatUpholtseryCleanNotTorn.remarks" 
                                                   ng-hide="inspection.seatUpholtseryCleanNotTorn.text"  
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char            
                                                   ng-required = "inspection.seatUpholtseryCleanNotTorn.text=='false' || !inspection.seatUpholtseryCleanNotTorn.text"> <br>   

                                            <!-- Vehicle Roof Upholstery is clean and not torn -->

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.vehcileRoofUpholtseryCleanNotTorn.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">
                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.vehcileRoofUpholtseryCleanNotTorn.text" >Vehicle Roof Upholstery is clean and not torn</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.vehcileRoofUpholtseryCleanNotTorn.text" >Vehicle Roof Upholstery is clean and not torn</span><br>  
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char
                                                   ng-model="inspection.vehcileRoofUpholtseryCleanNotTorn.remarks"  
                                                   ng-hide="inspection.vehcileRoofUpholtseryCleanNotTorn.text"           
                                                   ng-required = "inspection.vehcileRoofUpholtseryCleanNotTorn.text=='false' || !inspection.vehcileRoofUpholtseryCleanNotTorn.text"> <br>   

                                            <!-- Vehicle Floor Upholstery is clean and not torn -->

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.vehcileFloorUpholtseryCleanNotTorn.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          
                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.vehcileFloorUpholtseryCleanNotTorn.text" >Vehicle Floor Upholstery is clean and not torn</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.vehcileFloorUpholtseryCleanNotTorn.text" >Vehicle Floor Upholstery is clean and not torn</span><br>  
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char 
                                                   ng-model="inspection.vehcileFloorUpholtseryCleanNotTorn.remarks" 
                                                   ng-hide="inspection.vehcileFloorUpholtseryCleanNotTorn.text"         
                                                   ng-required = "inspection.vehcileFloorUpholtseryCleanNotTorn.text=='false' || !inspection.vehcileFloorUpholtseryCleanNotTorn.text"> <br>   

                                           <!-- Vehicle Dashboard is Clean -->

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.vehcileDashboardClean.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          
                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.vehcileDashboardClean.text" >Vehicle Dashboard is Clean</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.vehcileDashboardClean.text" >Vehicle Dashboard is Clean</span><br>
                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.vehcileDashboardClean.remarks" 
                                                   ng-hide="inspection.vehcileDashboardClean.text"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char       
                                                   ng-required = "inspection.vehcileDashboardClean.text=='false' || !inspection.vehcileDashboardClean.text"> <br>   


                                             <!--Vehicle glasses is Clean / Stain Free / No Films etc-->

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.vehicleGlassCleanStainFree.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.vehicleGlassCleanStainFree.text" >Vehicle glasses is Clean / Stain Free / No Films etc</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.vehicleGlassCleanStainFree.text" >Vehicle glasses is Clean / Stain Free / No Films etc</span><br> 

                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.vehicleGlassCleanStainFree.remarks" 
                                                   ng-hide="inspection.vehicleGlassCleanStainFree.text"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char     
                                                   ng-required = "inspection.vehicleGlassCleanStainFree.text=='false' || !inspection.vehicleGlassCleanStainFree.text"> 
                                                <br />   

                                        <!--Vehicle Interior Lighting is Bright and working-->

                                            <input type="checkbox" 
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.vehicleInteriorLightBrightWorking.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                       

                                            <span  ng-true-value=true class="textHide" ng-hide="inspection.vehicleInteriorLightBrightWorking.text" >Vehicle Interior Lighting is Bright and working</span>

                                            <span  ng-false-value=true class="textShow" ng-show="inspection.vehicleInteriorLightBrightWorking.text" >Vehicle Interior Lighting is Bright and working</span><br> 


                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char
                                                   ng-model="inspection.vehicleInteriorLightBrightWorking.remarks" 
                                                   ng-hide="inspection.vehicleInteriorLightBrightWorking.text"     
                                                   ng-required = "inspection.vehicleInteriorLightBrightWorking.text=='false' || !inspection.vehicleInteriorLightBrightWorking.text">
                                             <br />   


                                           <!--Bolster Seperating the Last two seats (Only in Innova)-->

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.bolsterSeperatingLastTwoSeats.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true 
                                                  class="textHide"  
                                                  ng-hide="inspection.bolsterSeperatingLastTwoSeats.text" >
                                                Bolster Seperating the Last two seats (Only in Innova)
                                            </span>

                                            <span  ng-false-value=true 
                                                  class="textShow" 
                                                  ng-show="inspection.bolsterSeperatingLastTwoSeats.text" >
                                                Bolster Seperating the Last two seats (Only in Innova)
                                            </span>
                                                <br /> 

                                            <input type="text" 
                                                   class="remarks" 
                                                   placeholder="Remarks"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200"
                                                   expect_special_char 
                                                   ng-model="inspection.bolsterSeperatingLastTwoSeats.remarks" 
                                                   ng-hide="inspection.bolsterSeperatingLastTwoSeats.text"  
                                                   ng-required = "inspection.bolsterSeperatingLastTwoSeats.text=='false' || !inspection.bolsterSeperatingLastTwoSeats.text"> 
                                            <br />  

                                            <!--Audio Working Or Not-->

                                            <input type="checkbox"
                                            class = 'radiobuttonRight pointer' 
                                            ng-model="inspection.audioWorkingOrNot.text" 
                                            ng-true-value=true 
                                            ng-false-value=false
                                            ng-checked ="checkboxAll">                                                          

                                            <span  ng-true-value=true 
                                                  class="textHide"  
                                                  ng-hide="inspection.audioWorkingOrNot.text" >
                                                Audio Working Or Not
                                            </span>

                                            <span  ng-false-value=true 
                                                  class="textShow" 
                                                  ng-show="inspection.audioWorkingOrNot.text" >
                                                Audio Working Or Not
                                            </span>
                                                <br /> 

                                            <input type="text" 
                                                   class="remarks"
                                                   ng-minlength="0"
                                                   ng-maxlength="200"
                                                   maxlength="200" 
                                                   expect_special_char
                                                   placeholder="Remarks" 
                                                   ng-model="inspection.audioWorkingOrNot.remarks" 
                                                   ng-hide="inspection.audioWorkingOrNot.text"  
                                                   ng-required = "inspection.audioWorkingOrNot.text=='false' || !inspection.audioWorkingOrNot.text">
                                            <br/>
                                            </td>  
                                          </tr>                                   
                                        </tbody>
                                    </table>
                                          
                                    <div class = 'row'>
                                    <div class = 'col-md-12'> 
                                    
                                    <button class = 'btn btn-info buttonRadius0 floatRight' 
                                            ng-click = "nextToPage2()"
                                            ng-disabled = "formInspOne.pageOne.$invalid">
                                        <span><i class ='icon-chevron-right'></i></span>
                                    </button>
                                        <span  class="InspIndication floatRight">*Please enter Remarks for all unChecked boxes</span>
                                    </div>
                                    </div>
                                </form> 
                            </div>
                               
                            <!-- ************PAGE 2************* --> 
                            <div class = 'page2 col-md-12'>
                                <form name = "formInspTwo.pageTwo">
                                <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                <tr>
                                <th>Vendor Name</th> 
                               <!--  <th>Vehicle Number</th> 
                                <th>Vehicle Type</th>  -->

                                <th>Exterior</th>                                    
                                <th>Brake Working</th>
                                <th>Tyre & Wheel Condition</th>
                                <th>Wiper</th>
                                </tr> 
                                </thead>
                                <tbody class = 'bottomBorder1px'>
                                <tr>
                                <td class = 'col-md-1'>{{vendorSelectedForInspection}}</td>
                                <!-- <td class = 'col-md-2'>{{vehicleSelectedForInspection}}</td>
                                <td class = 'col-md-1'>{{vehicleSelectedTypeForInspection}}</td> -->


                                <!--Scratches on the body-->

                                <td class = 'col-md-2'>
                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.exteriorScratches.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll"> 

                                <span  ng-true-value=true class="textHide"  ng-hide="inspection.exteriorScratches.text" >Scratches on the body</span>

                                <span  ng-false-value=true class="textShow"  ng-show="inspection.exteriorScratches.text" >Scratches on the body</span><br> 
                                <input type="text" 
                                       class="remarks" 
                                       placeholder="Remarks"
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200" 
                                       expect_special_char
                                       ng-model="inspection.exteriorScratches.remarks" 
                                       ng-hide="inspection.exteriorScratches.text"
                                       ng-required = "inspection.exteriorScratches.text=='false' || !inspection.exteriorScratches.text">

                                <br>

                                <!--No Patch Work-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.noPatchWork.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll">

                                <span  ng-true-value=true class="textHide" ng-hide="inspection.noPatchWork.text" >No Patch Work</span>

                                <span  ng-false-value=true class="textShow"  ng-show="inspection.noPatchWork.text" >No Patch Work</span><br> 
                                <input type="text" 
                                       class="remarks" 
                                       placeholder="Remarks" 
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char
                                       ng-model="inspection.noPatchWork.remarks" 
                                       ng-hide="inspection.noPatchWork.text"
                                       ng-required = "inspection.noPatchWork.text=='false' || !inspection.noPatchWork.text">
                                </td>

                                <td class = 'col-md-2'>

                                <!--Pedal Brake Working-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.pedalBrakeWorking.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll"> 

                                 <span  ng-true-value=true class="textHide" ng-hide="inspection.pedalBrakeWorking.text" >Pedal Brake Working</span>

                                <span  ng-false-value=true class="textShow" ng-show="inspection.pedalBrakeWorking.text" >Pedal Brake Working</span><br> 
                                <input type="text" 
                                       class="remarks"
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200" 
                                       expect_special_char
                                       placeholder="Remarks" 
                                       ng-model="inspection.pedalBrakeWorking.remarks"  
                                       ng-hide="inspection.pedalBrakeWorking.text"
                                       ng-required = "inspection.pedalBrakeWorking.text=='false' || !inspection.pedalBrakeWorking.text">
                                <br>

                                <!--Hand Brake Working-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.handBrakeWorking.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll">

                                <span  ng-true-value=true class="textHide" ng-hide="inspection.handBrakeWorking.text" >Hand Brake Working</span>

                                <span  ng-false-value=true class="textShow" ng-show="inspection.handBrakeWorking.text" >Hand Brake Working</span><br> 
                                <input type="text" 
                                       class="remarks" 
                                       placeholder="Remarks"
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char 
                                       ng-model="inspection.handBrakeWorking.remarks" 
                                       ng-hide="inspection.handBrakeWorking.text"
                                       ng-required = "inspection.handBrakeWorking.text=='false' || !inspection.handBrakeWorking.text">
                                </td>

                                <td class = 'col-md-3'>

                                <!--No Dents on the Trim of the Wheel-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.tyresNoDentsTrimWheel.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll"> 

                                <span  ng-true-value=true class="textHide" ng-hide="inspection.tyresNoDentsTrimWheel.text" >No Dents on the Trim of the Wheel</span>

                                <span  ng-false-value=true class="textShow" ng-show="inspection.tyresNoDentsTrimWheel.text" >No Dents on the Trim of the Wheel</span><br> 
                                <input type="text" 
                                       class="remarks" 
                                       placeholder="Remarks" 
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char
                                       ng-model="inspection.tyresNoDentsTrimWheel.remarks"  
                                       ng-hide="inspection.tyresNoDentsTrimWheel.text"
                                       ng-required = "inspection.tyresNoDentsTrimWheel.text=='false' || !inspection.tyresNoDentsTrimWheel.text">
                                <br>

                                <!--Tyres in good condition-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.tyresGoodCondition.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll">

                                <span  ng-true-value=true class="textHide" ng-hide="inspection.tyresGoodCondition.text" >Tyres in good condition</span>

                                <span  ng-false-value=true class="textShow" ng-show="inspection.tyresGoodCondition.text" >Tyres in good condition</span><br> 
                                <input type="text" 
                                       class="remarks" 
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char
                                       placeholder="Remarks" 
                                       ng-model="inspection.tyresGoodCondition.remarks" 
                                       ng-hide="inspection.tyresGoodCondition.text"
                                       ng-required = "inspection.tyresGoodCondition.text=='false' || !inspection.tyresGoodCondition.text">
                                <br>

                                <!--All Tyres Including Stephney Inflate-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.allTyresAndStephneyInflate.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll"> 

                                <span  ng-true-value=true 
                                      class="textHide" 
                                      ng-hide="inspection.allTyresAndStephneyInflate.text" >All Tyres Including Stephney Inflate</span>

                                <span  ng-false-value=true 
                                      class="textShow" 
                                      ng-show="inspection.allTyresAndStephneyInflate.text" >All Tyres Including Stephney Inflate</span><br> 
                                <input type="text" 
                                       class="remarks" 
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char
                                       placeholder="Remarks" 
                                       ng-model="inspection.allTyresAndStephneyInflate.remarks" 
                                       ng-hide="inspection.allTyresAndStephneyInflate.text"
                                       ng-required = "inspection.allTyresAndStephneyInflate.text=='false' || !inspection.allTyresAndStephneyInflate.text">
                                <br>

                                <!--Jack & Tools-->

                                <input type="checkbox" 
                                class = 'radiobuttonRight pointer' 
                                ng-model="inspection.jackAndTool.text" 
                                ng-true-value=true 
                                ng-false-value=false
                                ng-checked ="checkboxAll">

                                <span  ng-true-value=true class="textHide" ng-hide="inspection.jackAndTool.text" >Jack & Tools</span>

                                <span  ng-false-value=true class="textShow" ng-show="inspection.jackAndTool.text" >Jack & Tools</span> <br>

                                <input type="text" 
                                       class="remarks" 
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char
                                       placeholder="Remarks" 
                                       ng-model="inspection.jackAndTool.remarks" 
                                       ng-hide="inspection.jackAndTool.text"
                                       ng-required = "inspection.jackAndTool.text=='false' || !inspection.jackAndTool.text">
                                <br>

                                <!--Number Of Punctures Done-->

                                <span class="dropdownInspectionLabel"> Number Of Punctures Done</span></br>
                                <select class="remarks" ng-model="inspection.numberofPunctruresdone.text" 
                                ng-options="puncture for puncture in punctures track by puncture">  
                                    </select>
                                </td>

                                <td class = 'col-md-2'>
                                <input type="checkbox" 
                                       class = 'radiobuttonRight pointer' 
                                       ng-model="inspection.wiperWorking.text" 
                                       ng-true-value=true 
                                       ng-false-value=false
                                       ng-checked ="checkboxAll"> 

                                <span  ng-true-value=true class="textHide" ng-hide="inspection.wiperWorking.text" >Wiper Working</span>

                                <span  ng-false-value=true class="textShow"  ng-show="inspection.wiperWorking.text" >Wiper Working</span><br>
                                <input type="text" class="remarks" placeholder="Remarks" 
                                       ng-minlength="0"
                                       ng-maxlength="200"
                                       maxlength="200"
                                       expect_special_char
                                       ng-model="inspection.wiperWorking.remarks" 
                                       ng-hide="inspection.wiperWorking.text"
                                       ng-required = "inspection.wiperWorking.text=='false' || !inspection.wiperWorking.text">  
                                </td>
                                </tr>
                                </tbody>
                                </table>
                               
                            <div class = 'row'>
                            <div class = 'col-md-12'> 
                                <button class = 'btn btn-info buttonRadius0 floatRight' 
                                        ng-click = "nextToPage3()"
                                        ng-disabled = "formInspTwo.pageTwo.$invalid"> 
                                    <span><i class ='icon-chevron-right'></i></span>
                                </button>

                                <button class = 'btn btn-primary marginRight10 buttonRadius0 floatRight' 
                                        ng-click = "backToPage1()">
                                    <span><i class ='icon-chevron-left'></i></span>
                                </button>
                                <span  class="InspIndication floatRight">*Please enter Remarks for all unChecked boxes</span>

                            </div>

                            </div>
                    </form>
                    </div>
                    <!-- PAGE 3 -->
                    <div class = 'page3 col-md-12'>
                    <form name = "formInspThree.pageThree">
                    <table class = "escortAvailableTable table table-responsive container-fluid" ng-model="newreset">
                    <thead class ="tableHeading">
                    <tr>
                    <th>Vendor Name</th> 
<!--                     <th>Vehicle Number</th> 
                    <th>Vehicle Type</th>  -->

                    <th>AC</th>                                    
                    <th>Indicators</th>
                    <th>Lights / Inidcator</th>
                    </tr> 
                    </thead>
                    <tbody class = 'bottomBorder1px'>
                    <tr>
                    <td class = 'col-md-1'>{{vendorSelectedForInspection}}</td>
<!--                     <td class = 'col-md-1'>{{vehicleSelectedForInspection}}</td>
                    <td class = 'col-md-1'>{{vehicleSelectedTypeForInspection}}</td> -->                                           

                    <td class = 'col-md-3'>
                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.acCoolingIn5mins.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.acCoolingIn5mins.text" >Cooling achieved end to end in 5 mins</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.acCoolingIn5mins.text" >Cooling achieved end to end in 5 mins</span><br>

                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.acCoolingIn5mins.remarks" 
                           ng-hide="inspection.acCoolingIn5mins.text"
                           ng-required = "inspection.acCoolingIn5mins.text=='false' || !inspection.acCoolingIn5mins.text">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.noSmellInAC.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.noSmellInAC.text" >No Smell In AC</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.noSmellInAC.text" >No Smell In AC</span><br>
                    <input type="text" 
                           class="remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char 
                           placeholder="Remarks" 
                           ng-model="inspection.noSmellInAC.remarks" 
                           ng-hide="inspection.noSmellInAC.text"
                           ng-required = "inspection.noSmellInAC.text=='false' || !inspection.noSmellInAC.text">  <br>                              

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.acVentsClean.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.acVentsClean.text" >AC vents are clean </span>

                    <span  ng-false-value=true class="textShow"  ng-show="inspection.acVentsClean.text" >AC vents are clean </span><br>
                    <input type="text" 
                           class="remarks" 
                           placeholder="Remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           ng-model="inspection.acVentsClean.remarks" 
                           ng-hide="inspection.acVentsClean.text"
                           ng-required = "inspection.acVentsClean.text=='false' || !inspection.acVentsClean.text">  <br>    
                    </td>    
                        
                    <!--<input type="text" placeholder="Remarks" ng-hide="inspection.acVentsClean">  <br>-->
                    <td class = 'col-md-3'>
                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.enginOilChangeIndicatorOn.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.enginOilChangeIndicatorOn.text" >Engine Oil Change Indicator ON</span>

                    <span  ng-false-value=true class="textShow"  ng-show="inspection.enginOilChangeIndicatorOn.text" >Engine Oil Change Indicator ON</span><br>
                    <input type="text" 
                           class="remarks" 
                           placeholder="Remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char 
                           ng-model="inspection.enginOilChangeIndicatorOn.remarks" 
                           ng-hide="inspection.enginOilChangeIndicatorOn.text"
                           ng-required = "inspection.enginOilChangeIndicatorOn.text=='false' || !inspection.enginOilChangeIndicatorOn.text">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.coolantIndicatorOn.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.coolantIndicatorOn.text" >Coolant Indicator ON</span>

                    <span  ng-false-value=true class="textShow"  ng-show="inspection.coolantIndicatorOn.text" >Coolant Indicator ON</span><br>
                    <input type="text" 
                           class="remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200" 
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.coolantIndicatorOn.remarks" 
                           ng-hide="inspection.coolantIndicatorOn.text"
                           ng-required = "inspection.coolantIndicatorOn.text=='false' || !inspection.coolantIndicatorOn.text">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.brakeClutchIndicatorOn.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                     <span  ng-true-value=true class="textHide" ng-hide="inspection.brakeClutchIndicatorOn.text" >Breake Cluntch Indicator ON</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.brakeClutchIndicatorOn.text" >Breake Cluntch Indicator ON</span><br> 
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.brakeClutchIndicatorOn.remarks" 
                           ng-hide="inspection.brakeClutchIndicatorOn.text"
                           ng-required = "inspection.brakeClutchIndicatorOn.text=='false' || !inspection.brakeClutchIndicatorOn.text">  <br>
                    </td>                                       

                    <td class = 'col-md-3'>
                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.highBeamWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.highBeamWorking.text" >High Beam Working</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.highBeamWorking.text" >High Beam Working</span><br>
                    <input type="text" 
                           class="remarks" 
                           placeholder="Remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           ng-model="inspection.highBeamWorking.remarks" 
                           ng-hide="inspection.highBeamWorking.text"
                           ng-required = "inspection.highBeamWorking.text=='false' || !inspection.highBeamWorking.text">  <br>
                        
                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.lowBeamWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.lowBeamWorking.text" >Low Beam Working</span>

                    <span  ng-false-value=true class="textShow"  ng-show="inspection.lowBeamWorking.text" >Low Beam Working</span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           placeholder="Remarks" 
                           expect_special_char
                           ng-model="inspection.lowBeamWorking.remarks" 
                           ng-hide="inspection.lowBeamWorking.text"
                           ng-required = "inspection.lowBeamWorking.text=='false' || !inspection.lowBeamWorking.text">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.rightFromtIndicatorWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.rightFromtIndicatorWorking.text" >Right Front Indicator Working </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.rightFromtIndicatorWorking.text" >Right Front Indicator Working </span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.rightFromtIndicatorWorking.remarks" 
                           ng-hide="inspection.rightFromtIndicatorWorking.text"
                           ng-required = "inspection.rightFromtIndicatorWorking.text=='false' || !inspection.rightFromtIndicatorWorking.text">  <br>


                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.leftFrontIndicatorWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.leftFrontIndicatorWorking.text" >Left Front Indicator Working </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.leftFrontIndicatorWorking.text" >Left Front Indicator Working</span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.leftFrontIndicatorWorking.remarks" 
                           ng-hide="inspection.leftFrontIndicatorWorking.text"
                           ng-required = "inspection.leftFrontIndicatorWorking.text=='false' || !inspection.leftFrontIndicatorWorking.text">  <br>


                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.parkingLightsWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.parkingLightsWorking.text" >Parking Lights Working</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.parkingLightsWorking.text" >Parking Lights Working</span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.parkingLightsWorking.remarks" 
                           ng-hide="inspection.parkingLightsWorking.text"
                           ng-required = "inspection.parkingLightsWorking.text=='false' || !inspection.parkingLightsWorking.text"
                           ng-checked ="checkboxAll">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.ledDayTimeRunningLightWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.ledDayTimeRunningLightWorking.text" >LED Day Time Running Lights Working</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.ledDayTimeRunningLightWorking.text" >LED Day Time Running Lights Working</span><br>
                    <input type="text" 
                           class="remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200" 
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.ledDayTimeRunningLightWorking.remarks" 
                           ng-hide="inspection.ledDayTimeRunningLightWorking.text"
                           ng-required = "inspection.ledDayTimeRunningLightWorking.text=='false' || !inspection.ledDayTimeRunningLightWorking.text">  <br>
                        
                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.rightRearIndicatorWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.rightRearIndicatorWorking.text" >Right Rear Indicator Working </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.rightRearIndicatorWorking.text" >Right Rear Indicator Working </span><br>
                    <input type="text" 
                           class="remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char 
                           placeholder="Remarks" 
                           ng-model="inspection.rightRearIndicatorWorking.remarks" 
                           ng-hide="inspection.rightRearIndicatorWorking.text"
                           ng-required = "inspection.rightRearIndicatorWorking.text=='false' || !inspection.rightRearIndicatorWorking.text">  <br>


                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.leftRearIndicatorWorking.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.leftRearIndicatorWorking.text" >Left Rear Indicator Working  </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.leftRearIndicatorWorking.text" >Left Rear Indicator Working  </span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.leftRearIndicatorWorking.remarks" 
                           ng-hide="inspection.leftRearIndicatorWorking.text"
                           ng-required = "inspection.leftRearIndicatorWorking.text=='false' || !inspection.leftRearIndicatorWorking.text">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.brakeLightsOn.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.brakeLightsOn.text" >Brake Lights On  </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.brakeLightsOn.text" >Brake Lights On  </span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.brakeLightsOn.remarks" 
                           ng-hide="inspection.brakeLightsOn.text"
                           ng-required = "inspection.brakeLightsOn.text=='false' || !inspection.brakeLightsOn.text">  <br>


                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.reverseLightsOn.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.reverseLightsOn.text" >Reverse Lights On  </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.reverseLightsOn.text" >Reverse Lights On  </span><br>
                    <input type="text" 
                           class="remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200" 
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.reverseLightsOn.remarks" 
                           ng-hide="inspection.reverseLightsOn.text"
                           ng-required = "inspection.reverseLightsOn.text=='false' || !inspection.reverseLightsOn.text">  <br>
                    <!-- Fog Lights -->
                   
                    <input type="checkbox" 
                    class = 'radiobuttonRight pointer' 
                    ng-model="inspection.fogLights.text" 
                    ng-true-value=true 
                    ng-false-value=false
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide"  ng-hide="inspection.fogLights.text" >Fog Lights</span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.fogLights.text" >Fog Lights</span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.fogLights.remarks" 
                           ng-hide="inspection.fogLights.text"
                           ng-required = "inspection.fogLights.text=='false' || !inspection.fogLights.text">  <br>

                    </td>
                    </tr>                                   
                    </tbody>
                    </table>
                    <div class = 'row'>
                    <div class = 'col-md-12'> 
                        
                        <button class = 'btn btn-info buttonRadius0 floatRight' 
                                ng-click = "nextToPage4()" 
                                ng-disabled = "formInspThree.pageThree.$invalid"> 
                            <span><i class ='icon-chevron-right'></i></span>
                        </button>

                        <button class = 'btn btn-primary marginRight10 buttonRadius0 floatRight' 
                        ng-click = "backToPage2()">
                                <span><i class ='icon-chevron-left'></i></span>
                        </button>
                        <span  class="InspIndication floatRight">*Please enter Remarks for all unChecked boxes</span>
                    </div>
                    </div>
                    </form>
                    </div>
                   
                   
                    <!--  PAGE 4 -->
                    <div class = 'page4 col-md-12'>
                    <form name = "formInspFour.pageFour">
                    <table class = "escortAvailableTable table table-responsive container-fluid">
                    <thead class ="tableHeading">
                    <tr>
                    <th>Vendor Name</th> 
<!--                     <th>Vehicle Number</th> 
                    <th>Vehicle Type</th> --> 

                    <th>Diesel</th>
                    <th>Horn Working</th>
                    <th>Reflective sign board</th>


                    </tr> 
                    </thead>
                    <tbody class = 'bottomBorder1px'>
                    <tr>
                    <td class = 'col-md-1'>{{vendorSelectedForInspection}}</td>
<!--                     <td class = 'col-md-1'>{{vehicleSelectedForInspection}}</td>
                    <td class = 'col-md-1'>{{vehicleSelectedTypeForInspection}}</td>  --> 

          <td class = 'col-md-2'>
          <span>Diesel</span></br>
          <select ng-model="inspection.diesel.text" 
              ng-options="diesel for diesel in diesels">
                    </select>
                    </td>

                    <td class = 'col-md-2'>
                    <input type="checkbox" 
                            class = 'radiobuttonRight' 
                            ng-model="inspection.hornWorking.text" 
                            ng-true-value=true 
                            ng-false-value=false
                            ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.hornWorking.text" >Horn Working  </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.hornWorking.text" >Horn Working </span><br>
                    <input type="text" 
                           class="remarks" 
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           placeholder="Remarks" 
                           ng-model="inspection.hornWorking.remarks" 
                           ng-hide="inspection.hornWorking.text"
                           ng-required = "inspection.hornWorking.text=='false' || !inspection.hornWorking.text">  <br>
                    </td>

                    <td class = 'col-md-2'>
                    <input type="checkbox" 
                    class = 'radiobuttonRight' 
                    ng-model="inspection.reflectionSignBoard.text" 
                    ng-true-value=true 
                    ng-false-value=false 
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.reflectionSignBoard.text" >Reflective sign board </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.reflectionSignBoard.text" >Reflective sign board </span><br>
                    <input type="text" 
                           class="remarks"
                           expect_special_char 
                           placeholder="Remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           ng-model="inspection.reflectionSignBoard.remarks" 
                           ng-hide="inspection.reflectionSignBoard.text"
                           ng-required = "inspection.reflectionSignBoard.text=='false' || !inspection.reflectionSignBoard.text">  <br>

                    <input type="checkbox" 
                    class = 'radiobuttonRight' 
                    ng-model="inspection.driverCheckInDidOrNot.text" 
                    ng-true-value=true 
                    ng-false-value=false 
                    ng-checked ="checkboxAll"> 

                    <span  ng-true-value=true class="textHide" ng-hide="inspection.driverCheckInDidOrNot.text" >Did Driver completed checklist? </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.driverCheckInDidOrNot.text" >Did Driver completed checklist?</span><br>
                    <input type="text" 
                           class="remarks" 
                           placeholder="Remarks"
                           ng-minlength="0"
                           ng-maxlength="200"
                           maxlength="200"
                           expect_special_char
                           ng-model="inspection.driverCheckInDidOrNot.remarks" 
                           ng-hide="inspection.driverCheckInDidOrNot.text"
                           ng-required = "inspection.driverCheckInDidOrNot.text=='false' || !inspection.driverCheckInDidOrNot.text">  <br>

                    <!-- <input type="checkbox" 
                    class = 'radiobuttonRight' 
                    ng-model="inspection.feedback.text" 
                    ng-true-value=true 
                    ng-false-value=false 
                    ng-checked ="checkboxAll">  -->

                    <!-- <span  ng-true-value=true class="textHide" ng-hide="inspection.feedback.text" >Remarks </span>

                    <span  ng-false-value=true class="textShow" ng-show="inspection.feedback.text" >Remarks </span> --><br>

                    <textarea class="form-control feedbackInspection" 
                              placeholder="Remarks"
                              ng-minlength="0"
                              ng-maxlength="200"
                              maxlength="200"
                              expect_special_char
                              ng-model="inspection.feedback.remarks" 
                              rows="4"></textarea>
                    <!-- <input type="text" 
                           class="remarks" 
                           placeholder="Remarks"
                           ng-model="inspection.feedback.remarks" 
                           ng-hide="inspection.feedback.text"
                           ng-required = "inspection.feedback.text=='false' || !inspection.feedback.text">  --> 
                           <br>

                    </td> 


                    <!--
                    <input type="text" 
                    class = 'form-control' 
                    ng-model="inspection.driverName"> 
                    -->
                    </td> 



                    </tr>                                   
                    </tbody>
                    </table>
                <div class = 'row'>
                    <div class = 'col-md-12'>  
                        <button id = "inspectionSubmitButton" 
                                class = 'btn btn-success buttonRadius0 floatRight' 
                                ng-click = "submitInspection(inspection,driverSelected)"                                
                                ng-disabled = "formInspFour.pageFour.$invalid">Submit</button>
                        
                        <button class = 'btn btn-primary buttonRadius0 marginRight10 floatRight marginRight20' 
                                ng-click = "backToPage3()"><span><i class ='icon-chevron-left'></i></span></button>
                        <span  class="InspIndication floatRight">*Please enter Remarks for all unChecked boxes</span>
                    </div>

                </div>
                <div class = 'col-md-3'>  

                </div>
                </form>
                </div> <!--END of Page4 -->
            </div>
            </div>

               <div class = "row" ng-if = "!isAndroidDevice && !isiOSDevice && !adminRole">
                    <div class = "col-md-4"></div>
                    <div class = "col-md-4 osInfo">
                        <span class = "spanTextOsInfo">You are currently using </span>
                        <ul>
                            <li><span class = "headingOs">OS : </span><span><mark>{{deviceDetector.os}}</mark></span>
                                <span class = "headingOsversion">(ver: {{deviceDetector.os_version}} )</span></li>
                            <li><span class = "headingOs">Browser : </span>
                                <span><mark>{{deviceDetector.browser}}</mark></span>
                                <span class = "headingOsversion">(ver: {{deviceDetector.browser_version}} )</span></li>
                            <li><span class = "headingOs">Device : </span>
                                <span><mark>{{deviceDetector.device}}</mark></span></li>
                            <li class = "osInAndroid tabletdCenter">
                                <span class = "spanAndroidInfo"><blink data-speed="200"><i class = "icon-info-sign"></i></blink></span>
                                <span>To view this form please use an <strong>Android or iOS</strong> Device only</span></li>
                        </ul>
                    </div>
                    <div class = "col-md-4"></div>

                </div>
        </tab>
<!-- ----------------------------------------------------------------------------------- -->
    
                    <!-- Vehicle Inspection TAB -->
    
                    <tab ng-click = "getVehicleInspection('inspectionDetail'); getTabInfo('inspectionDetail')">    
                        <tab-heading>Vehicle Inspection Detail</tab-heading>
                        <div class = "escortAvailableTabContent InspectionTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = "floatLeft col-md-2">
                                    <select ng-model="detailIns.vendor"
                                           class="form-control marginBottom10" 
                                           ng-options="allInspectionDetailVendor.name for allInspectionDetailVendor in allInspectionDetailVendors track by allInspectionDetailVendor.Id"
                                           ng-change = "setVendorDriver(detailIns.vendor)"
                                           >
                                     <option value="">All Vendors</option>
                                  </select>
                                </div>
                                <div class = "floatLeft col-md-2">
                                    <select ng-model="detailIns.vehicleSelected"
                                           class="form-control" 
                                           ng-options="allVehicle.vehicleNumber for allVehicle in allVehiclesInpectionDetail track by allVehicle.Id" ng-disabled="vehicleDisabled">
                                     <option value="">All Vehicles</option>
                                  </select>
                                </div>
                                <div class = "floatLeft col-md-1">                                    
                                </div>
                                <div class = "floatLeft col-md-7"> 
                                    <div class = "calenderMainDiv floatRight pointer" 
                                     popover-template="partials/popovers/calenderReport.jsp"
                                     popover-placement="bottom"
                                     popover-title = "Get Report"
                                     popover-trigger ="click">                           
                                        <span><i class = "icon-calendar"></i></span>
                                        <span>{{fromDate | date : 'longDate'}} - {{toDate | date : 'longDate'}}</span>
                                        <span><img ng-src = 'images/spiffygif_22x22.gif' ng-show = 'isProcessing'/></span>
                                    </div>  
                                </div>
                            </div>
                            <form name="formInsp.inspectionForm2">
                            
                            <div ng-include = "'partials/inpectionDetail_vendorDashboard.jsp'">
                                               </div>
                            
                            </form></div>
                    </tab>
                     


                   </tabset>
                </div>

                <div class="clearfix"></div>
                <br>
            </div>
            
            
        </div>
        
    </div>
</div>