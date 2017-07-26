<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    This Page tracks realtime location of the employee and cab on the routes. It gives further details of ETA and more. It has nested view of Map.
@State          home.viewmap >> home.viewmap.showAll
@URL            /viewmap/showRoutes   

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->
<div class = "viewMapTemplate container-fluid" ng-if = "isLiveTrackingActive" ng-init="getMapData()">
    <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <div class = "col-md-12 col-xs-12 filterDiv_viewMap" ng-if="!gotMapData">Live Tracking</div> 
            <div class = "col-md-10 col-xs-10 filterDiv_viewMap" ng-if="gotMapData">Live Tracking</div> 
            <div class = "col-md-2 text-right col-xs-2 filterDiv_viewMap" ng-if="gotMapData">
            <span class="btn-success btn-sm pointer"
                    ng-click="priorityTracking()">Priority Tracking</span>
                    </div> 
                      
            <div class = "col-md-12 col-xs-12 mainTabDiv_viewMap">
                <div class = "row marginBottom10">
                  
                  <div class="col-md-2 col-xs-3 col-sm-3">
                          <select ng-model="tripType"
                            class="form-control"
                            ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                            ng-change="setTripTypeliveTracking(tripType); shiftTime = null"
                            required
                            >
                            <option value="">Trip Type</option>
                          </select>
                  </div>
                  
                <div class="col-md-3 col-xs-6 col-sm-6" ng-show="multiFacility == 'Y'" style="padding-left: 0;"> 
                    <select ng-model="shiftTime" style="width: initial;" 
                      class="form-control floatLeft selectShiftTime_serviceMapping"
                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTime | orderBy:'shiftTime' track by shiftTime.shiftTime" 
                      ng-change="setResultButton(shiftTime, tripType)"
                      required
                      ng-disabled = "!liveTrackingShiftTime"
                      >
                      <option value="">Shift Time</option>
                    </select>
                  <am-multiselect class="input-lg dynamicDaysInput" style="position: absolute;"
                                   multiple="true"
                                   ms-selected ="{{facilityData.listOfFacility.length}} Facility(s) Selected"
                                   ng-model="facilityData.listOfFacility"
                                   ms-header="All Facility"
                                   options="facility.branchId as facility.name for facility in facilityDetails"
                                   change="setFacilityDetails(facilityData.listOfFacility)"
                                   
                                   ">
                                </am-multiselect>
                </div>

                <div  ng-show="multiFacility == 'N'" class="col-md-3 col-xs-6 col-sm-6"> 
                   <select ng-model="shiftTime" style="width: initial;"
                      class="form-control floatLeft selectShiftTime_serviceMapping"
                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTime | orderBy:'shiftTime' track by shiftTime.shiftTime" 
                      ng-change="setResultButton(shiftTime, tripType)"
                      required
                      ng-disabled = "!liveTrackingShiftTime"
                      >
                      <option value="">Shift Time</option>
                    </select>
                </div>

                <div class="col-md-1 col-xs-3 col-sm-3">
                    <input class="btn btn-success" 
                    style="width: 100%; padding-top: 5px; padding-bottom: 5px; padding-left: 0; padding-right: 0;" 
                    ng-disabled="!getResultButton && !shiftTime"
                    value="Result"
                    ng-click="getMapData(tripType, shiftTime, facilityData.listOfFacility); searchBy = null; searchText = null">
                </div>
                <div class="col-md-2 col-xs-4">

                      <select ng-model="searchBy" 
                                   class="form-control" 
                                   ng-options="searchType.value for searchType in searchTypes track by searchType.value"
                                   ng-change = "searchTypeChange(searchType.value, searchBy); searchText = null"
                                   >
                             <option value="">-- Search By --</option>
                      </select>
                  </div>
                <div class = "col-md-2 col-xs-4">
                        <!-- <div class = "searchViewMap"> -->
                        <div class = "input-group"> 
                          <input ng-model="searchText"
                                 type = "text" 
                                 class="form-control" 
                                 ng-keydown="$event.which === 13 && searchRoute(searchText,facilityData.listOfFacility)"       placeholder = "Search route by employeeId"
                                 maxlength =  '20'
                                 ng-maxlength =  '20'
                                 expect_special_char
                                 ng-disabled = "!searchTypeChangeStatus"
                                 ng-change = searchTextChangeButton()
                                 >
                           <span class="input-group-btn">
                               <button class="btn btn-success" 
                                       ng-click="searchRoute(searchBy,searchText,facilityData.listOfFacility); shiftTime = null; tripType = null" 
                                       ng-disabled="!searchTypeButton && !searchText">
                               <i class = "icon-search searchServiceMappingIcon"></i></button></span> 
                        </div>
                    <!-- </div> -->
                    
                </div>
                <div class="col-md-2 col-xs-4">
                  <input type = 'text'
                       ng-model = 'filterViewMap'
                       expect_special_char
                       class = 'buttonRadius0 floatRight form-control filterViewMap marginRight10'
                       placeholder = 'Filter by shift,triptype,routename...'>
                    </div>
                </div>
          <!--   /*START OF WRAPPER1 = DRIVER*/ -->
               <div class = "wrapper1" id="activeEscortContent" ng-show="routeDivLiveTracking">
                <div class = "heading2 row">
                    <span class = "col-md-7 floatLeft" id = "driver">Routes</span>
                    <div class= "col-md-5 floatRight">
                      <efmfm-button img-class = "efmfm_approvalButtons_collapse"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "activeEscortContent"
                                      target-div = "activeEscortTable">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_approvalButtons_reload"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "activeEscortContent"
                                      target-div = "activeEscortTable"
                                      ng-click = 'refreshViewMap()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "activeEscortContent"
                                      target-div = "activeEscortTable">
                        </efmfm-button>
                    </div>
                </div>
                    <div class = "activeEscortTable" ng-show="routeDivLiveTracking">
                        <table class = "viewMapTable table table-hover table-responsive container-fluid" >
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Route Id</th>
                                      <th>Shift Time</th>
                                      <th>Driver Number</th>
                                      <th>Driver Name</th>
                                      <th>Vehicle Number</th>
                                      <th>TripType</th>
                                      <th>Route Name</th>
                                      <th>Cab Location</th>
                                      <th>Status</th>
                                      <th>Expected time</th>
                                      <th>Total Employees</th>
                                      <th>Facility Name</th>
                                      <th>Speed(kph)</th>
                                      <th></th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "mapData.length==0">
                                        <tr>
                                            <td colspan = '13'>
                                                <div class = "noData">There is No Active Route</div>
                                            </td>
                                        </tr>
                                </tbody>
                                <tbody ng-show = "mapData.length>0">
                                   <tr ng-repeat = "route in mapData | filter:filterViewMap" class = "pointer">
                                     <td>{{route.routeId}}</td>
                                      <td class = "col-md-1">{{route.shiftTime}}</td>
                                  <td class = "col-md-1">{{route.driverNumber}}</td>
                                  <td class = "col-md-1">{{route.driverName}}</td>
                                      <td class = "col-md-1">{{route.vehicleNumber}}</td>
                                      <td class = "col-md-1">{{route.tripType}}</td>
                                  <td class = "col-md-1">{{route.zoneName}}</td>
                                  <td class = "col-md-1">{{route.currentCabLocation}}</td>                                 
                                  <td class = "col-md-1">{{route.status}}</td>
                                      <td class = "col-md-1">{{route.ExpectedTime}}</td>
                                       <td class = "col-md-1">{{route.numberOfEmployees}}</td>
                                       <td class = "col-md-1">{{route.facilityName}}</td>
                                      <td class = "col-md-1">{{route.speed}}</td>
                                      <td class = "col-md-1">
                                          <input type="button" 
                                                 class = "btn btn-success btn-xs showDetailButton_viewMap" 
                                                 value = "Show Details"
                                                 ng-click = "thisRouteDetails($index, route)">
                                      </td>
                                </tr>                                    
                             </tbody>
                            </table>
                    </div>
                
                
                </div>
                <div class="clearfix"></div>
                <br>
              <!--    /*END OF WRAPPER1 = DRIVER*/
                 /*START OF WRAPPER2 = VEHICLE*/ -->
                <div class = "wrapper1" id = "mapContent">
                <div class = "heading2 row">
                    <span class = "col-md-7 floatLeft" id = "map_viewMap">All Routes</span>
                    <div class= "col-md-5 floatRight">
                      <efmfm-button img-class = "efmfm_approvalButtons_collapse"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "mapContent"
                                      target-div = "map_viewMap">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_approvalButtons_reload"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "mapContent"
                                      target-div = "map_viewMap"
                                      ng-click="refreshViewMap()">
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "mapContent"
                                      target-div = "map_viewMap">
                        </efmfm-button>
                    </div>
                </div>

                <div ui-view class = "map_viewMap" ng-if = "dataLoaded">
                    
                </div>
                                
               </div>    
                
            </div>
                 
        </div>
        
    </div> 
   
    
</div>

