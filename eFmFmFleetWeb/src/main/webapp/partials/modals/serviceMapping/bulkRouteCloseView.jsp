<div class = "serviceMappingTemplate container-fluid">
     <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <!-- <span class = "col-md-12 col-xs-12">Custom Routes View</span>    -->
             
                 <div class = "col-md-12 col-xs-12 mainTabDiv_serviceMapping">

                    <div class="row">  
                        <div class="col-xs-2"> 
                        
                            <div class = "input-group calendarInput">
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
                        <div class="col-xs-2" ng-show="multiFacility == 'Y'">
                               <am-multiselect class="input-lg dynamicDaysInput"
                                   multiple="true"
                                   ms-selected ="{{facilityData.listOfFacility.length}} Facility(s) Selected"
                                   ng-model="facilityData.listOfFacility"
                                   ms-header="All Facility"
                                   options="facility.branchId as facility.name for facility in facilityDetails"
                                   change="setFacilityDetails(facilityData.listOfFacility); search.tripType = null; search.shiftTime = null"
                                   >
                                </am-multiselect>
                        </div>
                        <div class="col-xs-2">
                            <select ng-model="search.tripType"
                                           class="form-control"                                            
                                           ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                           ng-change = "setSearchTripType(search.tripType, facilityData.listOfFacility)" 
                                           ng-disabled="facilityData.listOfFacility.length == 0"
                                           >
                                     <option value="">Trip Type</option>
                                  </select>
                        </div>
                        <div class="col-xs-2">
                            <select ng-model="search.shiftTime" 
                                           class="form-control" 
                                           ng-disabled="facilityData.listOfFacility.length == 0"
                                           ng-options="shiftTime.shiftTime for shiftTime in shiftsTime track by shiftTime.shiftTime"
                                           >
                                     <option value="">Shift Time</option>
                            </select>
                        </div>
                        <div class="col-xs-1">
                        <button class="btn btn-success" ng-click="searchRouteDetails(search,facilityData.listOfFacility, 'buttonSearchClick')">
                                   <i class="icon-search searchServiceMappingIcon"></i></button>
                        </div>
                        <div class="col-xs-1">
                               
                        </div>
                        <div class="col-xs-2">
                            <input type="text" class="form-control" value="" placeholder="Search Details..." name="" ng-model="searchRouteFilter"
                            expect_special_char>
                        </div>
                </div>

                <br/>


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
                <th>Device Id</th>
                <th>Vehicle Number</th>
                <th ng-hide="escortRequiredShow == 'N'">Escort Name</th>
                <th>Edit/Save</th>
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
              <tr ng-repeat="bulkRoute in bulkRouteData | filter:searchRouteFilter" id = "shift{{$index}}">
                <td>{{bulkRoute.routeId}}</td>
                <td>{{bulkRoute.routeName}}</td> 
                <td>{{bulkRoute.tripType}}</td>
                <td>{{bulkRoute.numberOfEmployees}}</td>
                <td>{{bulkRoute.dropPickAreaName}}</td>
                <td>{{bulkRoute.driverName}}</td>
                <td>{{bulkRoute.driverId}}</td>
                <td class="pointer"><span ng-show = "!bulkRoute.editVehicleIsClicked">{{bulkRoute.vehicleNumber}}</span>
                <span ng-show = "bulkRoute.editVehicleIsClicked">
                <am-multiselect class="input-lg dynamicDaysInput"
                                   ng-model="bulkRoute.vehicleNumber"
                                   ms-header="All Facility"
                                   options="checkInEntities.vehicleNumber for checkInEntities in checkInEntitiesData"
                                   >
                                </am-multiselect>
                </span>
                </td>
                <td class="pointer" ng-hide="escortRequiredShow == 'N'"><span ng-show = "!bulkRoute.editVehicleIsClicked">{{bulkRoute.escortName}}</span>
                <span ng-show = "bulkRoute.editVehicleIsClicked">
                <am-multiselect class="input-lg dynamicDaysInput"
                                   ng-model="bulkRoute.escortName"
                                   ms-header="All Escort"
                                   options="checkInEntities.escortName for checkInEntities in escortsData"
                                   >
                                </am-multiselect>
                </span>
                </td>
                <td>
                <button type = 'button'
                         class = 'btn btn-warning btn-sm'
                         ng-show = "!bulkRoute.editVehicleIsClicked"
                         ng-click = "editVehicleNumber(bulkRoute, $index)"><i class="icon-edit-sign"></i></button>
                <div class="btn-group btn-group-sm shiftTimeEdit">
                <button type="button" class="btn btn-success "
                                         ng-show = "bulkRoute.editVehicleIsClicked"
                                         ng-click = "updateVehicleNumber(bulkRoute, $index, checkInEntitiesData, escortsData)"><i class="icon-save"></i>
                                            </button>
                <button type="button" class="btn btn-danger "
                         ng-show = "bulkRoute.editVehicleIsClicked"
                         ng-click = "cancelVehicleNumber(bulkRoute, $index)"><i class = "icon-remove-sign"></i>
                </button>
                </div>
                </td>

                
              
              </tr>
               
            </tbody>
           
          </table>
                </div>

                <div class="row" ng-show="bulkRouteData.length>0">
                      <div class="col-md-10"></div>
                      <div class="col-md-2">
                          <input type="button" class="btn btn-xs btn-primary marginLeft10" value="Close All Routes" ng-click="bulkRouteCloseClick(bulkRouteData)" name="">
                      </div>
                </div>
            </div>  
        </div>
        </div>




