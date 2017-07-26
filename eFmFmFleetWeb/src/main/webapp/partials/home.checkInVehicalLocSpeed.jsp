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
<div class = "viewMapTemplate container-fluid" ng-if = "isLiveTrackingActive">
    <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <div class = "col-md-12 col-xs-12 filterDiv_viewMap">Check-in Vehicle Location and Speed</div>
                      
            <div class = "col-md-12 col-xs-12 mainTabDiv_viewMap">
                <div class = "row marginBottom10">
                    <div class = "col-md-6">
                        <input type = "button" class = "btn btn-success" value = "View All" ng-click = "viewAll()" ng-show = "showViewAllButton">
                    </div>
                    <div class = "col-md-6">
                        <div class = "searchViewMap">
                        <div class = "input-group calendarInput"> 
                          <input ng-model="searchText"
                                 type = "text" 
                                 class="form-control" 
                                 ng-keydown="$event.which === 13 && searchRoute(searchText)"                   expect_special_char      
                                 placeholder = "Search route by employeeId"
                                 ng-maxlength="20"
                                 maxlength =  "20">
                           <span class="input-group-btn">
                               <button class="btn btn-success" 
                                       ng-click="searchRoute(searchText)">
                               <i class = "icon-search searchServiceMappingIcon"></i></button></span> 
                        </div>
                    </div>
                <input type = 'text'
                       ng-model = 'filterViewMap'
                       class = 'buttonRadius0 floatRight form-control filterViewMap marginRight10'
                       placeholder = 'Filter by Driver Name, Id, Vehiclle Number...'
                       expect_special_char>
                    </div>
                </div>
          <!--   /*START OF WRAPPER1 = DRIVER*/ -->
               <div class = "wrapper1" id="activeEscortContent">
                <div class = "heading2 row">
                    <span class = "col-md-7 floatLeft" id = "driver">Vehicles</span>
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
                    <div class = "activeEscortTable">
                        <table class = "viewMapTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Vendor Name</th>                            
                                      <th>Driver Name</th>
                                      <th>Driver Number</th>
                                      <th>Vehicle Number</th>
                                      <th>Vehicle Speed</th>                                      
                                      <th>Cab Location</th>
                                      <th>Current Time</th>
                                    </tr> 
                                </thead>
                                <tbody ng-show = "mapData.length==0">
                                        <tr>
                                            <td colspan = '14'>
                                                <div class = "noData">There is No Active Route</div>
                                            </td>
                                        </tr>
                                </tbody>
                                <tbody ng-show = "mapData.length>0">
                                   <tr ng-repeat = "driver in mapData | filter:filterViewMap | filter: resultRouteName" class = "pointer">
                                      <td class = "col-md-1">{{driver.vendorName}}</td>                                  
                                      <td class = "col-md-1">{{driver.DriverName}}</td>
		                              <td class = "col-md-1">{{driver.MobileNumber}}</td>
		                               <td class = "col-md-1">{{driver.vehicleNumber}}</td>
		                              <td class = "col-md-1">{{driver.vehicleSpeed}}</td>    
		                              <td class = "col-md-1">{{driver.cabLocation}}</td>
		                              <td class = "col-md-1">{{driver.currentTime}}</td>
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
                    <span class = "col-md-7 floatLeft" id = "map_viewMap">All Vehicles Location and Speed</span>
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
                    <div id = "map_viewMap" class = "col-md-12 map_viewMap checkinVehicleLocSpeed">
                            <efmfm-show-all-cab-locations id = 'map-canvas'  ng-if = "dataLoaded"></efmfm-show-all-cab-locations> 
                    </div>
                                
               </div>    
                
            </div>
                 
        </div>
        
    </div> 
   
    
</div>

