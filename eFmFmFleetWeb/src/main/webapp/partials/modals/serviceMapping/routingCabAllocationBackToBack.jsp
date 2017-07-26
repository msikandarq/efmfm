<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default accordianPanel_serviceMapping" ng-repeat = "zone in zoneData">
      <a data-toggle="collapse"
         data-parent="#accordion"
         href="#collapse{{zone.routeId}}"
         aria-expanded="false"
         aria-controls="collapse{{zone.routeId}}"
         ng-click = "getRoutes(zone,search)"
         class = "noTextDecoration collapse{{zone.routeId}}">
    <div class="panel-heading panelHeading_serviceMapping collapseZoneDiv{{zone.routeId}}"
         role="tab"
         id="headingOne">
      <h4 class="panel-title heading_serviceMapping">
          <span class = "floatLeft"><strong>{{zone.routeName}}</strong> </span>
          <span class = "floatLeft marginLeft10 marginRight10"> - </span>
<!--          <div class = "pregnantLady floatLeft"></div>-->
          <span class = "floatLeft" ng-hide="zone.pragnentLady == 0">Pregnant Employee: <mark class = "pregnantLady">{{zone.pragnentLady}}</mark> </span>
          <span class = "floatLeft marginLeft5 marginRight5" ng-hide="zone.pragnentLady == 0"> | </span>
<!--          <div class = "handicap floatLeft"></div>-->
          <span class = "floatLeft" ng-hide="zone.physicallyChallenged == 0">Differently Abled Employee : <mark class = "handicap">{{zone.physicallyChallenged}}</mark> </span>
          <span class = "floatLeft marginLeft5 marginRight5" ng-hide="zone.physicallyChallenged == 0"> | </span>
<!--          <div class = "isinjures floatLeft"></div>-->
          <span class = "floatLeft" ng-hide="zone.isInjured == 0">Injured Employee : <mark class = "isinjures">{{zone.isInjured}} </mark></span>
          <span class = "floatLeft" ng-hide="zone.isVIP == 0">VIP : <mark class = "isVIPflag">{{zone.isVIP}} </mark></span>
          <span class="floatRight badge customBadge badge_serviceZone">{{zone.NumberOfRoutes}}</span>
      </h4>

    </div>
     </a>
    <div id="collapse{{zone.routeId}}"
         class="panel-collapse collapse accordionContent_serviceMappingDisplay collapse{{zone.routeId}}"
         role="tabpanel"
         aria-labelledby="headingOne">

        <div class = "col-md-3 col-md-offset-9 col-xs-6 searchDiv">
                  <input type = 'text'
                         class = "search_serviceMapping form-control floatRight"
                         ng-model = 'searchEmployee'
                         placeholder = "Search Employee">
              </div> <br>
      <div class="panel-body accorMainContent">
        <div class = "routesBucket route{{zone.routeId}}{{route.routeId}}"
             ng-repeat = "route in routesData | filter: {routeName : zone.routeName} | filter : searchEmployee"
             ng-hide = 'route.isVehicleEmpty'>




 <!--        <div class="col-md-8"></div>  -->

          <!-- <div class = "row"> -->
           <!-- Trip Type - Drop , If Suggestive Drop Bucket - False -->
            <div class = "col-md-3 col-xs-6 customTabs" ng-show="route.tripType == 'DROP' && !suggestiveFlgDropBucket">
                  <span class = "floatLeft map_serviceMappingIcon pointer"
                      ng-click = "openMap(route, zone, size)">
                    <i class ="icon-map-marker mapMarkerIcon"
                       tooltip="View on Map"
                       tooltip-placement="right"
                       tooltip-trigger="mouseenter"></i></span>
                <span><strong>Route {{route.routeId}}</strong></span> <span>| {{route.tripActualAssignDate}}</span><span> | {{route.tripType}}</span><span> | {{route.shiftTime}}</span><span> | {{route.routeType}}</span>  <span ng-if="route.suggestiveVehicleNumber !='No'"> | Suggestive Vehicle Number <span class="buttonStyleSuggestiveDrop" ng-if="route.suggestiveVehicleNumber !='No'">{{route.suggestiveVehicleNumber}}</span></span>
                <span class = "floatRight pointer"
                      ng-click = 'deleteEmptyRoute(route, zone)'>
                    <i class = "delete_route icon-remove-sign"></i>
                </span>
              </div>

              <!-- Trip Type - Drop , If Suggestive Drop Bucket - True -->

              <div class = "col-md-8 col-xs-8 customTabsBacktoBack" ng-show="route.tripType == 'DROP' && suggestiveFlgDropBucket">
               <span class = "floatLeft map_serviceMappingIcon pointer"
                      ng-click = "openMap(route, zone, size)">
                    <i class ="icon-map-marker mapMarkerIcon"
                       tooltip="View on Map"
                       tooltip-placement="right"
                       tooltip-trigger="mouseenter"></i></span>
                <span><strong>Route {{route.routeId}}</strong></span> <span>| {{route.tripActualAssignDate}}</span><span> | {{route.tripType}}</span><span> | {{route.shiftTime}}</span><span> | {{route.routeType}}</span>  <span ng-if="route.suggestiveVehicleNumber !='No'"> | Suggestive Vehicle Number <span class="buttonStyleSuggestiveDrop" ng-if="route.suggestiveVehicleNumber !='No'">{{route.suggestiveVehicleNumber}}</span></span>
                <span class = "floatRight pointer"
                      ng-click = 'deleteEmptyRoute(route, zone)'>
                    <i class = "delete_route icon-remove-sign"></i>
                </span>
              </div>

            <div class = "col-md-3 col-xs-6 customTabs" ng-show="route.suggestiveVehicleNumber == 'No' && route.tripType == 'PICKUP'">
                <span class = "floatLeft map_serviceMappingIcon pointer"
                      ng-click = "openMap(route, zone, size)">
                    <i class ="icon-map-marker mapMarkerIcon"
                       tooltip="View on Map"
                       tooltip-placement="right"
                       tooltip-trigger="mouseenter"></i></span>
                <span><strong>Route {{route.routeId}}</strong></span> <span>| {{route.tripActualAssignDate}}</span><span> | {{route.tripType}}</span><span> | {{route.shiftTime}}</span><span> | {{route.routeType}}</span>
                <span class = "floatRight pointer"
                      ng-click = 'deleteEmptyRoute(route, zone)'>
                    <i class = "delete_route icon-remove-sign"></i>
                </span>


              </div>

              <!-- Suggestive Vehicle Number Coming From Backend Below div will be execute-->

              <div class = "col-md-8 col-xs-8 customTabsBacktoBack" ng-show="route.suggestiveVehicleNumber != 'No' && route.tripType == 'PICKUP'">
                <span class = "floatLeft map_serviceMappingIcon pointer"
                      ng-click = "openMap(route, zone, size)">
                    <i class ="icon-map-marker mapMarkerIcon"
                       tooltip="View on Map"
                       tooltip-placement="right"
                       tooltip-trigger="mouseenter"></i></span>
                <span><strong>Route {{route.routeId}}</strong></span> <span>| {{route.tripActualAssignDate}}</span><span> | {{route.tripType}}</span><span> | {{route.shiftTime}}</span><span> | {{route.routeType}} | Suggestive Vehicle Number - </span> <span ng-click="getSuggestiveVehicleNumber(route)" class="btn btn-xs buttonStyleSuggestive">{{route.suggestiveVehicleNumber}}</span>
                <span class = "floatRight pointer"
                      ng-click = 'deleteEmptyRoute(route, zone)'>
                    <i class = "delete_route icon-remove-sign"></i>
                </span>

              </div>

              <div class="row" ng-show="route.suggestiveVehicleNumber != 'No' && route.tripType == 'PICKUP' && branchCode == 'GNPTJP'">
                        <div class="col-md-2">

                          <div class="input-group">
                            <span class="input-group-btn">
                              <button class="btn btn-secondary b2bRouteLabel" type="button">Back To Back Vehicle</button>
                            </span>
                            <span class="input-group-addon">
                            <input type="checkbox" name="" class="b2bRouteCheckbox"  ng-model="b2bSelected" ng-change="backToBackChange(b2bSelected,route)" disabled>
                            </span>
                            <div ng-show ="dropdownShowB2B">
                            <am-multiselect
                                            class="input-lg multiselectBackToTop"
                                            ms-header="Select Vehicle No"
                                            ng-model="edit.vehicleNumber"
                                            options="checkInEntity as checkInEntity.vehicleNumber for checkInEntity in checkInEntitiesData"
                                            disabled="!backToBackEnabled"
                                            change = "changeVehiclemanualNumber(route,edit.vehicleNumber)">
                                          </am-multiselect>
                            </div>
                          </div>
                        </div>
                        <div ng-show="route.isBackTwoBack == 'Y'">
                            <div class="col-md-1" ng-show="vehicleDetailsHide">
                              <i
                           tooltip="After click, You will be get old Vehicle Details"
                           tooltip-placement="top"
                           tooltip-trigger="mouseenter"><input type="button" class="btn btn-danger vehicleDeleteButton" name="" value="Reset" ng-click="deleteSuggestedVehicleDetails(route)"></i>
                        </div>
                        </div>

                </div>

                <!-- Suggestive Vehicle Number Not Coming From Backend Below div will be execute-->

                <div class="row" ng-show="route.suggestiveVehicleNumber == 'No' && route.tripType == 'PICKUP'">
                            <div class="col-md-2 col-sm-4 col-xs-12" ng-if="branchCode == 'GNPTJP' && vendorDropdown">
                            <span class="input-group" >
                                  <select ng-model="route.vendorId"
                                            ng-options="vendorData.name for vendorData in vendorsData track by vendorData.vendorId"
                                            name = "selectVendor"
                                            class="form-control-width width100Percent"
                                            ng-change="changeVendor(selectedVendor, route.routeId)"
                                            required>
                                            <option value="">Allocate Route For Vendor</option>
                                    </select>
                          </span>
                        </div>
                        <div class="col-md-4 col-sm-6 col-xs-12">
                          <div class="input-group">
                            <span class="input-group-btn">
                              <button class="btn btn-secondary b2bRouteLabel" type="button">Back To Back Vehicle</button>
                            </span>
                            <span class="input-group-addon" ng-show="b2bSelectedShow">
                            <input type="checkbox" name="" class="b2bRouteCheckbox"  ng-model="b2bSelected" ng-change="backToBackChange(b2bSelected,route)">
                            </span>
                            <span class="input-group-addon" ng-show="!b2bSelectedShow">
                            <input type="checkbox" name="" class="b2bRouteCheckbox"  ng-model="b2bSelected" ng-change="backToBackChange(b2bSelected,route)">
                            </span>
                            <span>
                              <am-multiselect
                                            class="input-lg multiselectBackToTop"
                                            ms-header="Select Vehicle No"
                                            ng-model="edit.vehicleNumber"
                                            options="checkInEntity as checkInEntity.vehicleNumber for checkInEntity in checkInEntitiesData"
                                            disabled="!backToBackEnabled"
                                            change="changeVehicleNumber(route,edit.vehicleNumber)"
                                            >
                                          </am-multiselect>
                            </span>
                            </div>
                            </div>

                        <div ng-show="route.suggestiveVehicleNumber != 'No'">
                            <div class="col-md-4" ng-show="vehicleDetailsHide">
                                <input type="button" ng-show="vehicleSaveShow" class="btn btn-success vehicleDeleteButton" name="" value="Save" ng-click="saveSuggestedVehicleManualDetails(route,edit.vehicleNumber)">
                                <i
                                tooltip="After click, You will be get old Vehicle Details"
                                tooltip-placement="top"
                                tooltip-trigger="mouseenter"><input type="button" ng-show="vehicleDeleteShow" class="btn btn-danger vehicleDeleteButton" name="" value="Reset" ng-click="deleteSuggestedVehicleManualDetails(route, edit.vehicleNumber)"></i>
                          </div>
                        </div>

                        <div ng-show="route.suggestiveVehicleNumber == 'No'">
                            <div class="col-md-4" ng-show="vehicleDetailsHide">
                                <input type="button" ng-show="vehicleSaveShow" class="btn btn-success vehicleDeleteButton" name="" value="Save" ng-click="saveSuggestedVehicleManualDetails(route,edit.vehicleNumber)">
                                <i
                                tooltip="After click, You will be get old Vehicle Details"
                                tooltip-placement="top"
                                tooltip-trigger="mouseenter"><input type="button" ng-show="vehicleDeleteShow" class="btn btn-danger vehicleDeleteButton" name="" value="Reset" ng-click="deleteSuggestedVehicleManualDetails(route, edit.vehicleNumber)"></i>
                          </div>
                        </div>


                </div>


                <div class="col-md-2" ng-show="learnRoute == true">
                                    <select ng-model="learnRouteType"
                                    class="learnRouteDropdown"
                                    ng-options="learnRoute.value as learnRoute.text for learnRoute in learnRouteDatas"
                                    ng-change="learnRouteChange(learnRouteType,route)">
                                    <option value="">--- Select Learn Route ---</option>
                                    </select>
                                </div>
                       <div class = "col-md-12 col-xs-12 eachRouteInfo">
                          <div class = "row margin0">
                             <div class = "col-md-12 col-xs-12 routesButtons">
                              <span><select class = "floatRight btn btn-success btn-xs manuallyStartTripButton_serviceMapping"
                                               ng-model="tollAvailability"
                                               ng-options="toll.text for toll in isToll track by toll.value"
                                               ng-change = "isTollChange(tollAvailability, route)">
                                            <option value="">Is Toll</option>
                                    </select>
                              </span>
                             <input type = "button"
                                    id = "started{{zone.routeId}}{{route.routeId}}"
                                    class = "floatRight btn btn-danger1 btn-xs manuallyStartTripButton_serviceMapping"
                                    value= "Manually Trip Started"
                                    ng-show = "route.tripStatus !='Started'"
                                    ng-click = "manualTripStart(route, zone)">

                             <input type = "button"
                                    id = "ended{{zone.routeId}}{{route.routeId}}"
                                    class = "floatRight btn btn-danger1 btn-xs manuallyStartTripButton_serviceMapping"
                                    value= "Manually Trip Ended"
                                    ng-show = "route.tripStatus =='Started'"
                                    ng-click = "manualTripEnd(route, zone)">

                               <input type = "button"
                                      class = "floatRight btn btn-danger1 btn-xs closeBucketButton_serviceMapping"
                                      value = "Close Route"
                                      ng-click = "closeBucket(route, zone)"
                                      ng-class = "{disabled: route.bucketStatus != 'N'}">
                               <input type = "button"
                                       class = "floatRight btn btn-warning2 btn-xs"
                                       value = "Edit Route"
                                       ng-disabled = "editRouteDisabled"
                                       ng-class = "{disabled: route.tripStatus == 'F'}"
                                       ng-click = "editBucket(route, zone, 'lg')" >
                                <div class = "btn-info col-md-2" ng-if="advanceserachResult"><span>{{route.tripConfirmation}}</span></div>
                               </div>
                               <div class = "col-md-1 mainFirstRowRoute"><span>Device Number</span><br><span>{{route.deviceNumber}}</span></div>
                               <div class = "col-md-1 mainFirstRowRoute"><span>Driver Name</span><br><span>{{route.driverName}}</span></div>
                               <div class = "col-md-2 mainFirstRowRoute"><span>Driver Number</span><br><span>{{route.driverNumber}}</span></div>
                               <div class = "col-md-2 mainFirstRowRoute"><span>Vehicle Number</span><br><span>{{route.vehicleNumber}}</span></div>
                               <div class = "col-md-2 mainFirstRowRoute">
                                   <span>Escort Name {{route.escortRequired}}
                                       <span ng-show = "route.escortRequired == 'N' && !route.addFlag" ng-click = "addEscort(route)">
                                           <i class = "icon-plus-sign addEscortServiceMap pointer"></i></span>
                                       <span ng-show = "route.escortRequired == 'N' && route.addFlag" ng-click = "cancelAddingEscort(route)">
                                           <i class = "icon-remove-sign pointer cancelAddingEscort"></i></span>

                                       <span ng-show = "route.escortRequired == 'Y'" ng-click = "removeEscort(route)">
                                           <i class = "icon-minus-sign addEscortServiceMap pointer"></i></span>
                                   </span><br />
                                   <span ng-show = "!route.addFlag">{{route.escortName}}</span>
                                   <span ng-show = "route.addFlag">
                                       <select class = ''
                                               ng-model="edit.escortName"
                                               ng-options="escortData.escortName for escortData in escortsData track by escortData.escortName"
                                               ng-change = "saveAddedEscort(edit.escortName, route)">
                                            <option value="">-- Select Escort --</option>
                                       </select>

                                   </span>
                                </div>
                               <div class = "col-md-1 mainFirstRowRoute">
                                   <div class = "seatsAvailable_service"><span>Available Seats</span>
                                       <span class = 'vehicleAvailableRound'><mark>{{route.vehicleAvailableCapacity}}</mark></span>
                                    </div></div>
                                   <div class = "col-md-1 mainFirstRowRoute">
                                   <div class = "seatsAvailable_service">
                                    <button class="btn btn-primary btn-sm floatRight" ng-click = "printModal(route); close()">Print</button>
                                    </div></div>
                                  <div class = "col-md-1 mainFirstRowRoute">
                                   <div class = "seatsAvailable_service">
                                    <button class="btn btn-success btn-sm floatRight" ng-click = "addNewRoute(route)"><i class="icon-plus"></i> New Route</button>
                                    </div>
                                    </div>
                                   <div class = "col-md-1 mainFirstRowRoute">
                                   <div class = "seatsAvailable_service">
                                    <button class="btn btn-success btn-sm floatRight" ng-click = "addNewEmployee(route)"><i class="icon-plus"></i> Add Emp</button>
                                    </div></div>
                              </div>
                              <table class = "serviceMappingTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Employee Id</th>
                                      <th>Employee Name</th>
                                      <th>Number</th>
                                      <th>Gender</th>
                                      <th>Pickup/Drop Location</th>
                                      <th>Status</th>
                                      <th>Area</th>
                                      <th ng-show = "route.tripType == 'PICKUP'">Pick-up Time</th>
                                       <th ng-show = "route.tripType == 'DROP'">Drop Sequence</th>
                                      <th class="nowrapText">
                                      <small>
                                        <input type="checkbox" name=""
                                        class="smCheckbox"  ng-disabled="DropedChecked"
                                        ng-model="noShowChecked"
                                        ng-change="noShowChange(noShowChecked, pickedUpChecked, route.empDetails, route)"><span class="smCheckBoxText"> No Show</span>
                                      </small>
                                      <small ng-show = "route.tripType == 'PICKUP'">
                                        <input type="checkbox"  ng-disabled="noShowChecked"
                                        name="" class="smCheckbox"  
                                        ng-model="pickedUpChecked" 
                                        ng-change="pickedUpChange(pickedUpChecked, noShowChecked, route.empDetails, route)"><span class="smCheckBoxText"> PickedUp</span></small>
                                        <small ng-show = "route.tripType == 'DROP'">
                                        <input type="checkbox"  ng-disabled="noShowChecked"
                                        name="" class="smCheckbox"  
                                        ng-model="pickedUpChecked" 
                                        ng-change="droppedChange(pickedUpChecked, noShowChecked, route.empDetails, route)"><span class="smCheckBoxText"> Dropped</span></small><br>
                                      Status</th>
                                      <th>Move To Zone</th>
                                      <th>Move To Route</th>
                                      <th></th>
                                    </tr>
                                </thead>
                                <tbody ng-show = "route.empDetails.length==0">
                                    <tr><td colspan = "11"><div class = "noData_serviceMapping">There is No Employee in this Route</div></td></tr>
                                  </tbody>
                                <tbody>
                               <tr ng-repeat = "employee in route.empDetails  | filter : searchEmployee"
                                       ng-show = "route.empDetails.length>0"
                                       id = "row{{zone.routeId}}{{route.routeId}}{{employee.employeeId}}{{employee.requestId}}" ng-class="{yellowStyle: employee.requestColor == 'yellow' , whiteStyle: employee.requestColor == 'white'}">
                                  <td class = "col-md-1">
                                         <span>{{employee.employeeId}} <i class="icon-user"></i></span>   <br/>
                                         <div
                                              tooltip="Pregnant Employee"
                                              tooltip-placement="right"
                                              tooltip-trigger="mouseenter"
                                              ng-if = "employee.pragnentLady == 'YES'">
                                                <span>
                                              <i class="icon-star"></i>
                                              <i class="icon-star"></i>
                                                </span>
                                                <span class = "pregnantLady floatLeft pointer">

                                                </span>
                                              </div>
                                         <div
                                              tooltip="Handicap Employee"
                                              tooltip-placement="right"
                                              tooltip-trigger="mouseenter"
                                              ng-if = "employee.physicallyChallenged == 'YES'">
                                              <span>
                                              <i class="icon-star"></i>
                                              <i class="icon-star"></i>
                                                </span>
                                                <span class = "handicap floatLeft pointer">

                                                </span>
                                              </div>
                                         <div
                                              tooltip="Injured Employee"
                                              tooltip-placement="right"
                                              tooltip-trigger="mouseenter"
                                              ng-if = "employee.isInjured == 'YES'">
                                                                                              <span>
                                              <i class="icon-star"></i>
                                              <i class="icon-star"></i>
                                                </span>
                                                <span class = "isinjures floatLeft pointer">

                                                </span>
                                              </div>
                                        <div
                                              tooltip="VIP"
                                              tooltip-placement="right"
                                              tooltip-trigger="mouseenter"
                                              ng-if = "employee.isVIP == 'YES'">
                                                                                              <span>
                                              <i class="icon-star"></i>
                                              <i class="icon-star"></i>
                                                </span>
                                                <span class = "isVIPflag floatLeft pointer">

                                                </span>
                                              </div>
                                     </td>
                                     <td class = "col-md-1"><span>{{employee.name}}</span>
                                     </td>
                                     <td class = "col-md-1"><span>{{employee.employeeNumber}}</span>
                                     </td>
                                     <td class = "col-md-1"><span>{{employee.gender}}</span>
                                     </td>
                                     <td class = "col-md-2"><span>{{employee.address}}</span>
                                     </td>
                                       <td class = "col-md-1 error" ><span>{{employee.empComingStatus}}</span></td>

                                     <td class = "col-md-1"><span>{{employee.empArea}}</span>
                                     </td>
                                     <td  class = 'col-md-1'>
                                         <form name = "updatePickDrop">
                                             <span ng-show = "!employee.isUpdateClicked" class = marginRight20> {{employee.pickUpTime}}</span>
                                             <input type = "text"
                                                    class = "dropPickColumn_serviceMapping"
                                                    ng-model = "employee.dropSequence"
                                                    ng-pattern = 'IntegerNumber'
                                                    required
                                                    ng-show = "route.tripType == 'DROP' && employee.isUpdateClicked">
                                             <input type = "button"
                                                    class = "btn btn-xs btn-warning buttonRadius0"
                                                    value = "Update"
                                                    ng-click = "updateDropSeq(employee, route, $index, $parent.$index)"
                                                    ng-show = "route.tripType == 'DROP'"
                                                    ng-disabled="updatePickDrop.$invalid">

                                             <input type="time"
                                                    ng-model="employee.createNewAdHocTime"
                                                    ng-show = "route.tripType == 'PICKUP' && employee.isUpdateClicked"
                                                    >
                                            <!-- <timepicker ng-model="employee.createNewAdHocTime"
                                                      hour-step="hstep"
                                                      minute-step="mstep"
                                                      show-meridian="ismeridian"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft"
                                                      ng-show = "route.tripType == 'PICKUP' && employee.isUpdateClicked">
                                            </timepicker> -->
                                             <input type = "button"
                                                    class = "btn btn-xs btn-warning buttonRadius0"
                                                    value = "Update"
                                                    ng-click = "updatePickupTime(employee, route, $index, $parent.$index)"
                                                    ng-show = "route.tripType == 'PICKUP'">
                                        </form>
                                     </td>
                                    <td class = "col-md-1">
                                        <select ng-model="employee.employeeStatus"
                                                class = "zoneDropDown_serviceMapping floatLeft"
                                                ng-if = "route.tripType == 'PICKUP'"
                                                ng-options="pickupStatus.text for pickupStatus in employeePickupStatus track by pickupStatus.value"
                                                ng-change = "changeEmployeeStatus(employee, route, zone)">
                                        </select>
                                        <select ng-model="employee.employeeStatus"
                                                class = "zoneDropDown_serviceMapping floatLeft"
                                                ng-if = "route.tripType == 'DROP'"
                                                ng-options="dropStatus.text for dropStatus in employeeDropStatus track by dropStatus.value"
                                                ng-change = "changeEmployeeStatus(employee, route, zone)">
                                        </select>
                                    </td>
<!--                                      <td class = "col-md-1">
                                                                         <span>
                                     <input type="checkbox" name="zoneCheckBox" class="width20Percent"
                                    ng-model = "employee.zoneCheckBox"></span>
                                    <span>
                                        <select ng-model="moveToZone"
                                                class = "zoneDropDown_serviceMapping floatLeft"
                                                ng-options="zone.routeName for zone in zoneData track by zone.routeName"
                                                ng-change = "changeRoutesDropDown(employee, moveToZone)">
                                            <option value="">ZONE</option>
                                        </select>
                                      </td> -->
                                                                         <td class = "col-md-1">
                                    <span>
                                     <input type="checkbox" name="zoneCheckBox" class="width20Percent"
                                    ng-model = "employee.zoneCheckBox"></span>
                                    <span>
                                       <select ng-model="moveToZone"
                                               class = "zoneDropDown_serviceMapping floatLeft width80Percent"
                                               ng-options="zone.routeName for zone in zoneData track by zone.routeName"
                                               ng-change = "changeRoutesDropDown(employee, moveToZone, route.empDetails)">
                                           <option value="">ZONE</option>
                                       </select>
                                    </span>
                                     </td>
                                     <td class = "col-md-1">
                                        <select ng-model="moveTo"
                                                class = "routeDropDown_serviceMapping floatLeft"
                                                ng-options="routeDropDown.routeId for routeDropDown in routesDropDown track by routeDropDown.routeId"
                                                ng-change = "moveToRoute(moveTo, moveToZone, route, zone, employee, $index)"
                                                ng-disabled = "!employee.isZoneclicked">
                                            <option value="">*Routes*</option>
                                        </select>
                                     </td>

                                     <td class = "col-md-1">
                                         <button class = "pointer btn btn-danger delete_serviceMapping"
                                              tooltip="Delete Employee"
                                              tooltip-placement="top"
                                              tooltip-trigger="mouseenter"
                                              ng-click = "deleteEmployee(route, zone, employee, $index)">
                                             <i class = "icon-remove-sign"></i>
                                         </button>
                                     </td>
                               </tr>
                             </tbody>
                            </table>
                          </div>
                        </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
