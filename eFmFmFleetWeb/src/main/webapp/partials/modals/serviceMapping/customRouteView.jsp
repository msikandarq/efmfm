<div class = "serviceMappingTemplate container-fluid" >
     <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <!-- <span class = "col-md-12 col-xs-12">Custom Routes View</span>    -->
            
                 <div class = "col-md-12 col-xs-12 mainTabDiv_serviceMapping">

                    <div class="row">  
                        <div class="col-xs-2"> 
                        
                            <div class = "input-group calendarInput customRouteCalender">
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
                        <div class="col-xs-2" style="margin-left: -47px; margin-right: 10px;" ng-show="multiFacility == 'Y'">
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
                                           class="form-control customRouteTripType" ng-disabled="facilityData.listOfFacility.length == 0"
                                           ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                           ng-change = "setSearchTripType(search.tripType)"
                                           >
                                     <option value="">Trip Type</option>
                                  </select>
                        </div>
                        <div class="col-xs-2">
                            <select ng-model="search.shiftTime" 
                                           class="form-control customRouteShiftTime" 
                                           ng-disabled="facilityData.listOfFacility.length == 0"
                                           ng-options="shiftTime.shiftTime for shiftTime in shiftsTime track by shiftTime.shiftTime"
                                           >
                                     <option value="">Shift Time</option>
                            </select>
                        </div>
                        
                        <div class="col-xs-1" style="margin-left: -178px;">
                        <button class="btn btn-success " ng-click="searchRouteDetails(search,facilityData.listOfFacility)">
                                   <i class="icon-search searchServiceMappingIcon"></i></button>
                        </div>
                      
                        <div class="col-xs-2">
                                <select ng-model="routeNames" 
                                        class="form-control " 
                                        ng-options="routeName.value as routeName.text for routeName in routeNameList"
                                        >
                                <option value="">All Zones</option>
                                </select>

                                <input type="text" class="form-control customeRouteFilter" value="" placeholder="Gobal Search Filter..." name="" ng-model="searchRouteFilter" expect_special_char>
                        </div>
                       
                </div>

                <br/>

        <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "customRouteViewShow">
          <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
        </div>
        <div ng-show = "tableDataShow">

        <header ng-repeat-start="items in customRouteDetails" class="headerContainerServiceMap pointer items{{items.zoneRouteId}}{{items.routeId}}" ng-hide="items.empDetails.length == 0 || (items.empDetails | filter:searchRouteFilter).length == 0 || (items.empDetails | filter:routeNames).length == 0" id = "row{{items.zoneRouteId}}{{items.routeId}}{{item.employeeId}}{{item.requestId}}">
            <div class="row">
                <div class="text-center headerTitleSize">
                        <strong>Zone Name : {{message}}</strong> 
                        <span>{{items.routeName}}</span>
                </div>

                <div class = "row">
                    <div class="col-sm-2">
                        <strong>  &nbsp; &nbsp; Route Id : {{items.routeId}} </strong> 
                        <span class="" ng-click = "openMap(items, zone, size)">
                        <i class="icon-map-marker mapMarkerIcon ng-scope" 
                           tooltip="View on Map" 
                           tooltip-placement="right" 
                           tooltip-trigger="mouseenter">
                        </i>
                        </span>
                    </div>

                <div class="col-sm-2">
                    <strong>Total Employee Count : </strong> 
                        <span class="badgeServiceMap">{{(items.empDetails | filter:searchRouteFilter).length}}</span>
                </div>

                <div class="col-sm-2">
                        <strong>Available Seats : </strong> 
                        <span class="badgeServiceMap">{{items.vehicleAvailableCapacity}}</span>
                </div>
                

                <div class="col-sm-2">
                    <span>Escort Name {{items.escortRequired}}
                        <span ng-show = "items.escortRequired == 'N' && !items.addFlag" ng-click = "addEscort(items)">
                            <i class = "icon-plus-sign addEscortServiceMap pointer"></i></span>
                            <span ng-show = "items.escortRequired == 'N' && items.addFlag" 
                                  ng-click = "cancelAddingEscort(items)">
                            <i class = "icon-remove-sign pointer cancelAddingEscort escortCancelCRV"></i></span>

                            <span ng-show = "items.escortRequired == 'Y'" 
                                  ng-click = "removeEscort(items)">
                            <i class = "icon-minus-sign addEscortServiceMap pointer"></i></span>                                   
                            </span><br />
                            <span ng-show = "!items.addFlag">{{items.escortName}}</span>
                            <span ng-show = "items.addFlag" class="fontBlack" >
                            <select class = ''
                                    ng-model="edit.escortName" 
                                    ng-options="escortData.escortName for escortData in escortsData track by escortData.escortName"
                                    ng-change = "saveAddedEscort(edit.escortName, items)">
                            <option value="">-- Select Escort --</option>
                            </select>
                        </span>
                </div> 

                
                <div class="col-sm-4 deleteRouteCRV">
                    <input type = "button" 
                           class = "floatRight btn btn-warning7 btn-xs" 
                           value= "Delete Route"  
                           ng-click = 'deleteEmptyRoute(items, zone,facilityData.listOfFacility)'>

                    <input type = "button" 
                           id = "ended{{items.routeId}}{{items.routeId}}"
                           class = "floatRight btn btn-warning5 btn-xs "
                           ng-show = "items.tripStatus =='Started'" 
                           value= "Manually Trip Ended"  
                           ng-click = "manualTripEnd(items, zone)">

                    <input type = "button" 
                           id = "started{{items.routeId}}{{items.routeId}}"
                           class = "floatRight btn btn-warning6 btn-xs" 
                           ng-show = "items.tripStatus !='Started'"
                           value= "Manually Trip Started"  
                           ng-click = "manualTripStart(items, zone)">

                    <input type = "button" 
                           class = "floatRight btn btn-warning4 btn-xs closeBucketButton_CustomRoute" 
                           value = "Close Route"
                           ng-click = "closeBucket(items, zone)"
                           ng-class = "{disabled: items.bucketStatus != 'N'}">

                    <input type = "button" 
                           class = "floatRight btn btn-warning3 btn-xs"
                           value = "Edit Route"
                           ng-class = "{disabled: items.tripStatus == 'F'}"
                           ng-click = "editBucket(items, zone, 'lg')" >
                </div>

                <!-- <div class="col-sm-1">
                </div> -->
            </div> 
            </div>
            <hr class="lineMarginStyle">

            <div class="row text-center divLineCRView" >
                
               
            </div>
            <div class="row text-center divLineCRView" >
               
              <div class="col-xs-12 customRouteB2BPanel">
                      <!-- Trip Type - Drop , If Suggestive Drop Bucket - False -->
                        <div class = "" ng-show="items.tripType == 'DROP' && !suggestiveFlgDropBucket">
                           <button type="button" 
                                   class="btn btn-success" 
                                   ng-if="items.suggestiveVehicleNumber !='No'">
                                   Suggestive Vehicle Number <span class="badge" >{{items.suggestiveVehicleNumber}}</span>
                            </button> 
                        </div>

                        <!-- Trip Type - Drop , If Suggestive Drop Bucket - True -->

                          <div class = "" ng-show="items.tripType == 'DROP' && suggestiveFlgDropBucket">
                            <button type="button" 
                                   class="btn btn-success" 
                                   ng-if="items.suggestiveVehicleNumber !='No'">
                                   Suggestive Vehicle Number <span class="badge" >{{items.suggestiveVehicleNumber}}</span>
                            </button> 
                          </div>


                        <!-- Suggestive Vehicle Number Coming From Backend Below div will be execute-->

                      <div class = "row" ng-show="items.suggestiveVehicleNumber != 'No' && items.tripType == 'PICKUP'">
                        <div class="col-md-3">
                            <button type="button" 
                                    class="btn btn-success" ng-click="getSuggestiveVehicleNumber(items)">Suggestive Vehicle Number 
                                        <span class="badge" >{{items.suggestiveVehicleNumber}}</span></button>     
                        </div>
                        <div class="col-md-1"></div>
                        <div class="col-md-2">
                           <div class="input-group">
                            <span class="input-group-btn">
                              <button class="btn btn-secondary b2bRouteLabel" type="button">Back To Back Vehicle</button>
                            </span>
                            <span class="input-group-addon">
                            <input type="checkbox" name="" class="b2bRouteCheckbox"  ng-model="b2bSelected" ng-change="backToBackChange(b2bSelected,items)" disabled>
                            </span>
                            <div ng-show ="dropdownShowB2B">
                            <am-multiselect 
                                            class="input-lg multiselectBackToTop"
                                            ms-header="Select Vehicle No"
                                            ng-model="edit.vehicleNumber"
                                            options="checkInEntity as checkInEntity.vehicleNumber for checkInEntity in checkInEntitiesData"
                                            disabled="!backToBackEnabled"
                                            change = "changeVehiclemanualNumber(items,edit.vehicleNumber)">
                                          </am-multiselect>
                            </div>
                          </div>
                        </div>

                        <div class="col-md-2" ng-show="items.isBackTwoBack == 'Y'">
                            <div class="col-md-1" ng-show="vehicleDetailsHide">
                              <i 
                           tooltip="After click, You will be get old Vehicle Details"
                           tooltip-placement="top"
                           tooltip-trigger="mouseenter"><input type="button" class="btn btn-danger vehicleDeleteButton" name="" value="Reset" ng-click="deleteSuggestedVehicleDetails(items)"></i>
                        </div>
                        </div>
                    </div>

                <!-- Suggestive Vehicle Number Not Coming From Backend Below div will be execute-->
                 
                <div class="row" ng-show="items.suggestiveVehicleNumber == 'No' && items.tripType == 'PICKUP'">


                        <div class="col-md-3">
                          <div class="input-group">
                            <span class="input-group-btn">
                              <button class="btn btn-secondary b2bRouteLabel" type="button">Back To Back Vehicle</button>
                            </span>
                            <span class="input-group-addon" ng-show="b2bSelectedShow">
                            <input type="checkbox" name="" class="b2bRouteCheckbox"  ng-model="b2bSelected" ng-change="backToBackChange(b2bSelected,items)">
                            </span>
                            <span class="input-group-addon" ng-show="!b2bSelectedShow">
                            <input type="checkbox" name="" class="b2bRouteCheckbox"  ng-model="b2bSelected" ng-change="backToBackChange(b2bSelected,items,$index)">
                            </span>
                            <am-multiselect 
                                            class="input-lg multiselectBackToTop"
                                            ms-header="Select Vehicle No"
                                            ng-model="edit.vehicleNumber"
                                            options="checkInEntity as checkInEntity.vehicleNumber for checkInEntity in checkInEntitiesData"
                                            disabled="!items.vehicleNumberShow"
                                            change="changeVehicleNumber(items,edit.vehicleNumber,$index)"
                                            >
                                          </am-multiselect>

                          </div>
                        </div>
                        <div ng-show="items.suggestiveVehicleNumber != 'No'">
                            <div class="col-md-4" ng-show="vehicleDetailsHide">
                                <input type="button" ng-show="items.saveButtonShow" class="btn btn-success vehicleDeleteButton" name="" value="Save" ng-click="saveSuggestedVehicleManualDetails(items,edit.vehicleNumber)">
                                <i 
                                tooltip="After click, You will be get old Vehicle Details"
                                tooltip-placement="top"
                                tooltip-trigger="mouseenter"><input type="button" ng-show="vehicleDeleteShow" class="btn btn-danger vehicleDeleteButton" name="" value="Reset" ng-click="deleteSuggestedVehicleManualDetails(items, edit.vehicleNumber)"></i>
                          </div>
                        </div>

                        <div ng-show="items.suggestiveVehicleNumber == 'No'">
                            <div class="col-md-4" ng-show="vehicleDetailsHide">
                                <input type="button" ng-show="items.saveButtonShow" class="btn btn-success vehicleDeleteButton" name="" value="Save" ng-click="saveSuggestedVehicleManualDetails(items,edit.vehicleNumber,$index)">
                                <i 
                                tooltip="After click, You will be get old Vehicle Details"
                                tooltip-placement="top"
                                tooltip-trigger="mouseenter"><input type="button" ng-show="items.resetButtonShow" class="btn btn-danger vehicleDeleteButton" name="" value="Reset" ng-click="deleteSuggestedVehicleManualDetails(items, edit.vehicleNumber,$index)"></i>
                            </div>
                      
                        </div>
                </div>

              </div>
            

                 
            </div>

            <hr class="lineMarginStyle">
            <div class="row text-center divLineCRView" >
            </div>

             <div class="row text-center divLineCRView" >
               <div class="col-xs-1"></div>
               <div class="col-xs-2">
                    <strong>Trip Confirmation : </strong> 
                        <span class="badgeServiceMap">{{items.tripConfirmation}}</span>
                </div>
                <div class="col-xs-2">
                    <strong>Device Id : </strong> 
                        <span class="badgeServiceMap">{{items.deviceNumber}}</span>
                </div>

                <div class="col-xs-2">
                    <strong>Driver Name : </strong> 
                        <span class="badgeServiceMap">{{items.driverName}}</span>
                </div>
               
                <div class="col-xs-2">
                    <strong>Vehicle Number : </strong> 
                        <span class="badgeServiceMap">{{items.vehicleNumber}}</span>
                </div>
                <div class="col-xs-2">
                    <strong>Driver Number : </strong> 
                        <span class="badgeServiceMap">{{items.driverNumber}}</span>
                </div>
                <div class="col-xs-1"></div>
                 
            </div>

            

            
        </header>


        <div class = "items{{items.zoneRouteId}}{{items.routeId}}" 
             class="body" 
             ng-hide="items.empDetails.length == 0 || (items.empDetails | filter:searchRouteFilter).length == 0 || (items.empDetails | filter:routeNames).length == 0" >
            <table class="table table-condensed table-bordered table-hover table-responsive" ng-hide="items.empDetails.length == 0">
                <thead class="headerStyleTable">
                    <tr>
                        <!-- <th>S No</th> -->
                        <th>Employee Id</th>
                        <th>Employee Name</th>
                        <th>Employee Number</th>
                        <th>Gender</th>
                        <th>Facility Name</th>
                        <th>Pickup/Drop Location</th>
                        <th>Status</th>
                        <th>Area</th>
                        <th ng-show="search.tripType.value =='PICKUP'">Pick-up Time</th>
                        <th ng-show="search.tripType.value =='DROP'">Drop Time</th>
                        <th>Employee Status</th>
                        <th>Move To Zone</th>
                        <th>Move To Route</th>
                        <th>Delete</th>
                    </tr>
                </thead>
               
                <tbody ng-repeat="item in items.empDetails | filter:searchRouteFilter | filter:routeNames" id = "row{{items.zoneRouteId}}{{items.routeId}}{{item.employeeId}}{{item.requestId}}" ng-class="{yellowStyle: item.requestColor == 'yellow' , whiteStyle: item.requestColor == 'white'}">

                    <tr>
                        
                        <td>{{item.employeeId}}</td>
                        <td>{{item.name}}</td>
                        <td>{{item.employeeNumber}}</td>
                        <td>{{item.gender}}</td>
                        <td>{{item.facilityName}}</td>
                        <td>{{item.address}}</td>
                        <td>{{item.empComingStatus}}</td>
                        <td>{{item.empArea}}</td>
                        <td class="hidden">{{item.routeNameList}}</td>
                        <td ng-show="search.tripType.value =='PICKUP'">
                            <form name = "updatePickDrop">
                                             <span ng-show = "!item.pickUpTimeIsClicked" class = marginRight20> {{item.pickUpTime}}</span> 
                                            <input type="time"
                                                    ng-model="pickUpTimeAdhoc"
                                                    ng-show="item.pickUpTimeIsClicked"
                                                    >
                                          <button type = 'button' 
                                                     class = 'btn btn-warning btn-sm' 
                                                     ng-show = "!item.pickUpTimeIsClicked"
                                                     ng-click = "updatePickUpTime(item, $index)"><i class="icon-edit-sign"></i></button>
                                            <button type = 'button' 
                                                     class = 'btn btn-success btn-sm' 
                                                     ng-show = "item.pickUpTimeIsClicked"
                                                     ng-click = "savePickUpTime(item, $index, pickUpTimeAdhoc)"><i class="icon-save"></i></button>
                                            <button type = 'button' 
                                                     class = 'btn btn-warning btn-sm cancelBtn' 
                                                     ng-show = "item.pickUpTimeIsClicked"
                                                     ng-click = "cancelPickupTime(item, $index)"><i class = "icon-remove-sign"></i></button>
                            </form>           
                        </td> 

                        <td ng-show="search.tripType.value =='DROP'">
                           
                                             <span ng-show = "!item.pickUpTimeIsClicked" class = marginRight20> {{item.pickUpTime}}</span> 
                                            <input type="text"
                                            class="dropPickColumn_customRoute" 
                                                    ng-model="dropTimeUpdate"
                                                    ng-show="item.pickUpTimeIsClicked"
                                                    >
                                            <button type = 'button' 
                                                     class = 'btn btn-warning btn-sm' 
                                                     ng-show = "!item.pickUpTimeIsClicked"
                                                     ng-click = "updateDropTime(item, $index)"><i class="icon-edit-sign"></i></button>
                                            <br/>
                                            <button type = 'button' 
                                                     class = 'btn btn-success btn-sm' 
                                                     ng-show = "item.pickUpTimeIsClicked"
                                                     ng-click = "saveDropSeq(item, items, $index, dropTimeUpdate)"><i class="icon-save"></i></button>
                                            <button type = 'button' 
                                                     class = 'btn btn-warning btn-sm cancelBtn' 
                                                     ng-show = "item.pickUpTimeIsClicked"
                                                     ng-click = "cancelPickupTime(item, $index)"><i class = "icon-remove-sign"></i></button>
                        </td> 

                        <td>

                       

                            <select ng-model="item.employeeStatus" 
                                                class = "zoneDropDown_serviceMapping floatLeft"
                                                ng-if = "item.tripType == 'PICKUP'"
                                                ng-options="pickupStatus.text for pickupStatus in employeePickupStatus track by pickupStatus.value"
                                                ng-change = "changeEmployeeStatus(item, items, item.employeeStatus)">
                                        </select>   
                                        <select ng-model="item.employeeStatus" 
                                                class = "zoneDropDown_serviceMapping floatLeft"
                                                ng-if = "item.tripType == 'DROP'"
                                                ng-options="dropStatus.text for dropStatus in employeeDropStatus track by dropStatus.value"
                                                ng-change = "changeEmployeeStatus(item, items, item.employeeStatus)">
                                        </select>   
                        </td>
                        <td>
                            <select ng-model="moveToZone" 
                                                class = "zoneDropDown_serviceMapping floatLeft"
                                                ng-options="zone.routeName for zone in zoneData track by zone.routeName"
                                                ng-change = "changeRoutesDropDown(item, moveToZone)">
                                            <option value="">ZONE</option>
                            </select>  
                        </td>
                        <td>
                            <select ng-model="moveTo" 
                                                class = "routeDropDown_serviceMapping floatLeft"
                                                ng-options="routeDropDown.routeId for routeDropDown in routesDropDown track by routeDropDown.routeId"
                                                ng-change = "moveToRoute(moveTo, moveToZone, items, zone, item, $index)"
                                                >
                                            <option value="">*Routes*</option>
                            </select>
                        </td>
                        <td>
                            <button class = "pointer btn btn-danger delete_serviceMapping" 
                                              tooltip="Delete Employee"
                                              tooltip-placement="top"
                                              tooltip-trigger="mouseenter"
                                              ng-click = "deleteEmployee(item, items, $index,facilityData.listOfFacility)">
                                             <i class = "icon-remove-sign"></i>
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <footer ng-repeat-end>
        </footer> 

        </div>
        </div>
    </div>  
</div>
</div>




