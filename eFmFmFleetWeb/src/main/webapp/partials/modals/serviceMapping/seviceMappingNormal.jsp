            <div class = "col-md-12 col-xs-12 mainTabDiv_serviceMapping">
                <div class = 'row searchArea'>
                    <div class="btn-group col-md-2">
                      <button type="button" class="btn btn-success creatNewRoute_serviceMapping" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Take Action on Routes <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu routeActions">
                        <li class = 'pointer' ng-click = 'createNewRoute()'>Create New Route</li>
                        <li class = 'pointer' ng-click = 'uploadRoute()'>Upload Route</li>
                        <li class = 'pointer' ng-click = 'uploadRouteBranch()' ng-show="branchCode == 'SRSTPT'">Sears Employee Request</li>
                        <li class = 'pointer' ng-click = 'exportRoute()'>Prepare to download</li>
                        <li class = 'pointer' ng-click = 'completeRoute()'>Complete Routing</li>
                        <li class = 'pointer' ng-click = 'printAll()'>Print All</li>
                        <!-- <li class = 'pointer' ng-click = 'rememberShiftRoute()'>Remember Shift Route</li> -->
                        <!-- <li class = 'pointer' ng-click = 'downloadRoute()'>Download Route</li> -->
                      </ul>
                    </div> 
                    
                    <div class = 'col-md-8 searchFirstOption'>  
                        <select ng-model="search.searchBy" 
                               class="form-control select_serviceMap floatLeft" 
                               ng-options="searchType.value for searchType in searchTypes track by searchType.value"
                               ng-change = "setSearchBy(search.searchBy)"
                               ng-show = '!advanceIsClicked'
                               >
                         <option value="">-- Search By --</option>
                         </select>
                         <div class = "input-group searchBox_serviceMap floatLeft calendarInput" ng-show = '!advanceIsClicked'> 
                              <input ng-model="search.text"
                                     type = "text" 
                                     class="form-control" 
                                     ng-keydown="$event.which === 13 && searchBy(search.text, search.searchBy)"   expect_special_char              
                                     placeholder = "{{setSearchPlaceHoler}}"
                                     maxLength = '20'
                                     ng-maxlength="20"
                                     ng-disabled="!search.searchBy"
                                     ng-focus = "searchIsEmpty = false"
                                     ng-class = "{error: searchIsEmpty}">
                               <span class="input-group-btn">
                                   <button class="btn btn-success" 
                                           ng-click="searchBy(search.text, search.searchBy)" >
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span> 
                         </div>
                          <div class = 'row' ng-show = 'advanceIsClicked'>
                             <div class="col-sm-2" ng-show="multiFacility == 'Y'">
                                <am-multiselect class="input-lg dynamicDaysInput"
                                   multiple="true"
                                   ms-selected ="{{facilityData.listOfFacility.length}} Facility(s) Selected"
                                   ng-model="facilityData.listOfFacility"
                                   ms-header="All Facility"
                                   options="facility.branchId as facility.name for facility in facilityDetails"
                                   change="setFacilityDetails(facilityData.listOfFacility); search.tripType = null; search.shiftTime = null">
                                </am-multiselect>
                             </div>
                             <div class="col-sm-2">
                                <select ng-model="search.tripType"
                                   class="form-control"                                            
                                   ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                   ng-change = "setSearchTripType(search.tripType, facilityData.listOfFacility)"
                                   ng-disabled="facilityData.listOfFacility.length == 0" style="margin-left: 26px;">
                                   <option value="">Trip Type</option>
                                </select>
                             </div>
                             <div class="col-sm-2">
                                <select ng-model="search.shiftTime" 
                                   class="form-control shiftTimeMarginServiceMapping" 
                                   ng-disabled="facilityData.listOfFacility.length == 0"
                                   ng-options="shiftTime.shiftTime for shiftTime in shiftsTime track by shiftTime.shiftTime"
                                   >
                                   <option value="">Shift Time</option>
                                </select>
                             </div>
                             <div class="col-sm-2">
                                <div class = "input-group calendarInput calendarServiceMapping">
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
                             <div class="col-sm-2">
                                <select ng-model="search.routeStatus"
                                   class="form-control routeStatusRouteStyle" 
                                   ng-options="routeStatus.text for routeStatus in routesStatus track by routeStatus.value"
                                   ng-change = "setSearchRouteStatus(search.routeStatus)"
                                   >
                                </select>
                             </div>
                             <div class="col-sm-2">
                                <input type = 'button'  
                                   class = 'form-control btn btn-success serviceMappingSuccessButton' 
                                   value = 'Search'
                                   ng-click = 'searchByTripTypeShift(search,facilityData.listOfFacility)'
                                   ng-disabled="facilityData.listOfFacility.length == 0">
                             </div>
                             <div class="col-sm-2">
                                <div ng-init="search.advanceflag = 'false'">
                                   <input class="hidden" type="text" ng-model="search.advanceflag">
                                </div>
                             </div>
                          </div>
                        
                       </div> 
                    <div class = 'col-md-2'>  
                        <span class = 'floatLeft orSpanServiceMapping'><mark>OR</mark></span>
                             <input type = 'button' 
                                    class = 'btn btn-warning form-control floatLeft advanceSearch_serviceMapping' 
                                    value = '{{advanceLabel}}'
                                    ng-click = 'advanceSearch()' 
                                    >
                    </div>  
                    <div class = 'col-md-12' ng-show = 'serachResultFound'>
                        <span class = 'floatLeft spanSearchLabel'>Search Results</span>
                        <input type = 'button' class = 'floatRight btn btn-success btn-xs buttonRadius0 backServiceMapping' 
                               value = 'Back To Main List'
                               ng-click = 'backtoMainList()'></div>


                </div>
                <div class="row vendorNameServiceMapping" ng-show="vendorNameShow">
                    <div class="col-xs-12"><strong>Vendor Name </strong>{{vendorName}}</div>
                </div>
               
                <!-- <div class="row">
                    <div class="col-sm-6"></div>
                    <div class="col-sm-4"></div>
                    <div class="col-sm-2">    
                              <input type = 'button' 
                                      class = 'btn btn-warning form-control' 
                                      value = 'Custom Route View'
                                      ng-click = 'customRouteView()' 
                                      >
                    </div>
                </div> -->
             
             <div ng-include="'partials/modals/serviceMapping/routingCabAllocationBackToBack.jsp'" ng-if="branchCode == 'GNPTJP'"></div> 
             <div ng-include="'partials/modals/serviceMapping/routingCabAllocationNormal.jsp'" ng-if="branchCode != 'GNPTJP'""></div>
                          
 
           </div> 