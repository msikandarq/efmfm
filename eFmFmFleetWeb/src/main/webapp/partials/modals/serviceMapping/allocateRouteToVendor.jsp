<div class = "serviceMappingTemplate container-fluid" ng-init="getAllVendors()">
     <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <!-- <span class = "col-md-12 col-xs-12">Custom Routes View</span>    -->
             
                 <div class = "col-md-12 col-xs-12 mainTabDiv_serviceMapping">

                    <div class="row">  
                        <div class="col-xs-2">
                            <select ng-model="search.vendor"
                                           class="form-control marginBottom10" 
                                           ng-options="vendorDetail.name for vendorDetail in vendorDetailsData"
                                           ng-change = "setVendor(inspection.vendor)"
                                           >
                                     <option value="">SELECT VENDOR</option>
                                  </select>
                        </div>
                        <div class="col-xs-2"> 
                        
                            <div class = "input-group calendarInput allocateRouteToVendorCalender">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" ng-click="openedsearchDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                              <input type="text" 
                                     ng-model="search.searchDate" 
                                     class="form-control" 
                                     placeholder = "Date" 
                                     datepicker-popup = '{{format}}'
                                     is-open="datePicker.openedsearchDate" 
                                     show-button-bar = false
                                     show-weeks=false
                                     datepicker-options = 'dateOptions' 
                                     required
                                     readonly
                                     name = 'date'>

                               </div>
                        </div>
                        <div class="col-xs-2" style="margin-left: -38px;" ng-show="multiFacility == 'Y'">
                            <am-multiselect class="input-lg dynamicDaysInput"
                                   multiple="true"
                                   ms-selected ="{{facilityData.listOfFacility.length}} Facility(s) Selected"
                                   ng-model="facilityData.listOfFacility"
                                   ms-header="All Facility"
                                   options="facility.branchId as facility.name for facility in facilityDetails"
                                   change="setFacilityDetails(facilityData.listOfFacility); search.tripType = null; search.shiftTime = null"
                                   
                                   ">
                            </am-multiselect>
                        </div>
                        <div class="col-xs-2">
                            <select ng-model="search.tripType"
                                           class="form-control allocateRouteToVendorTripType"  ng-disabled="facilityData.listOfFacility.length == 0"
                                           ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                           ng-change = "setSearchTripType(search.tripType, facilityData.listOfFacility)"
                                           >
                                     <option value="">Trip Type</option>
                                  </select>
                        </div>
                        <div class="col-xs-2">
                            <select ng-model="search.shiftTime" 
                                           class="form-control allocateRouteToVendorShiftTime" ng-disabled="facilityData.listOfFacility.length == 0"
                                           ng-options="shiftTime.shiftTime for shiftTime in shiftsTime track by shiftTime.shiftTime"
                                           
                                           >
                                     <option value="">Shift Time</option>
                            </select>
                        </div>
                        <div class="col-xs-1" style="margin-left: 31px;">
                        <button class="btn btn-success allocateRouteToVendorSearchButton" ng-click="searchRouteDetails(search,facilityData.listOfFacility)">
                                   <i class="icon-search searchServiceMappingIcon"></i></button>
                        </div>
                      
                        <div class="col-xs-2">
                            <input type="text" class="form-control allocateRouteToVendorRouteFilter" value="" placeholder="Search Details..." name="" ng-model="searchRouteFilter"
                            expect_special_char
                            >
                        </div>
                </div>
                <div class="row">
                    <div class="col-md-9"></div>
                    <div class="col-md-3">
                      <div class="btn-group pull-right"> 
                        <button type="button" class="btn btn-success btn-xs" ng-click="checkAll()">Check All</button>
                        <button type="button" class="btn btn-danger btn-xs" ng-click="uncheckAll()">Uncheck All</button>
                        <button type="button" class="btn btn-primary btn-xs" ng-click="allocateRouteToVendor(bulkRouteData,search,facilityData.listOfFacility)"">Allocate</button>
                        </div> 
                    </div>
                </div>

                <br/>


        <div class="table-responsive table-bordered  table-hover">          
          <table class="table differentTable">
            <thead class="bulkRouteHeader"> 
              <tr>
                <th>Select Route</th> 
              	<th>Route Id</th>
                <th>Route Name</th>
                <th>Trip Type</th>
                <th>No Of Employee</th>
                <th>Last Drop/First Pickup Loc</th>
                <th>Vendor Name</th>
                <th>Driver Name</th>
                <th>Vehicle Number</th>
                <th>Device Id</th>
                <!-- <th>Edit Route</th> -->
              </tr>
            </thead>

             <tbody ng-show="bulkRouteData.length==0">
                        <tr>
                          <td colspan='12'>
                            <div class="noData">There is no route available</div>
                          </td>
                        </tr>
            </tbody>
           
            <tbody ng-show="bulkRouteData.length>0">
              <tr ng-repeat="bulkRoute in bulkRouteData | filter:searchRouteFilter">
                
                <td>
                      <input type="checkbox" class="locationCheckBoxMargin" ng-model="bulkRoute.status" ng-change="setLocationSelectedValues(bulkRoute,bulkRoute.status)"/>
                </td>
                <td>{{bulkRoute.routeId}}</td>
                <td>{{bulkRoute.routeName}}</td> 
                <td>{{bulkRoute.tripType}}</td>
                <td>{{bulkRoute.numberOfEmployees}}</td>
                <td>{{bulkRoute.dropPickAreaName}}</td>
                 <td>{{bulkRoute.vendorName}}</td>
                <td>{{bulkRoute.driverName}}</td>
                <td>{{bulkRoute.vehicleNumber}}</td>
                <td>{{bulkRoute.driverId}}</td>
                </td>
              </tr>
               
            </tbody>
           
          </table>
                </div>

                <div class="row" ng-show="bulkRouteData.length>0">
                      <br/>
                      <div class="col-md-11"></div>
                      <div class="col-md-1">
                          <!-- <input type="button" style="margin-left: 10px" class="btn btn-xs btn-primary" value="Allocate" ng-click="allocateRouteToVendor(bulkRouteData,search)" name=""> -->
                      </div>
                </div>
            </div>  
        </div>
        </div>




