<div class = "escortAvailableTabContent requestDetailMyProfile row">
                            <div class = "row firstRowActionDiv">

                                <!-- <div class = 'col-md-2'>
                                   <select ng-model="search.searchBy" 
                                           class="form-control select_serviceMap floatLeft" 
                                           ng-options="searchType.value for searchType in searchTypes track by searchType.value"
                                           >
                         <option value="">-- Select Trip Type --</option>
                         </select>
                                                                 
                                </div> -->
                                <div class = 'col-md-4 searchEmployeeTravelDesk'>
                                </div>
                                <!-- <div class = 'col-md-4 searchEmployeeTravelDesk'>
                                        <div class = "input-group floatLeft calendarInput"> 
                                          <input ng-model="search.text"
                                                 type = "text" 
                                                 class="form-control" 
                                                 placeholder = "Search Pick/Drop Requests by Request Id"
                                                 maxlength =  '10'>
                                           <span class="input-group-btn">
                                               <button class="btn btn-success" 
                                                       ng-click="searchRequests(search.text)">
                                               <i class = "icon-search searchServiceMappingIcon"></i></button></span> 
                                        </div>
                                </div> -->
                                <div class = 'col-md-2'></div>
                                <div class = "col-md-3 floatRight">
                                    <input type = "text" 
                                           class = 'form-control requestDetailFirstRow' 
                                           ng-model = "filterRequests" 
                                           placeholder = "Filter Bulk Request Details"
                                           expect_special_char>
                                </div>
                            </div>
                        <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                    <th>Employee Id</th>
                                    <th>Request Creation Time</th>
                                    <th>Request Start Time</th>
                                    <th>Trip Type</th>
                                    <th>Request End Time</th>                                    
                                      <!-- <th>Pick/drop Time</th> -->
                                      <th>Shift Time</th>
                                      <th>Request Type</th>
                                      <th>Address</th>
                                      <th>Route Name</th>
                                      <th>Area Name</th>
                                      <th>Disable</th>
                                      
                                    </tr> 
                                </thead>

                                  <tbody ng-show = "requestsData.length==0">
                                    <tr>
                                         <!-- <td colspan = '2'>
                                         </td> -->
                                        <td colspan = '12'>
                                            <div class = "noData">There are no Bulk Request Details</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "requestsData.length>0">
                               <tr ng-repeat = "requests in requestsData|filter:filterRequests">
                                    <td class = 'col-md-1'>{{requests.employeeId}}</td>
                                     <td class = 'col-md-1'>{{requests.requestDate}}</td>
                                    <td class = 'col-md-1'>{{requests.requestStartDate}}</td>
                                    <td class = 'col-md-1'>{{requests.tripType}}</td>
                                    <td class = 'col-md-1'>{{requests.requestEndDate}}</td>
                                  
                                      <!-- <td class = 'col-md-1' ng-show = '!requests.editRequestDetailIsClicked'>{{requests.pickupTime}}</td>                                            -->
                                    <!-- <td  class = 'col-md-1' ng-show = 'requests.editRequestDetailIsClicked'>   
                                    <timepicker ng-model="requests.pickupTime" 
                                                      hour-step="hstep" 
                                                      minute-step="mstep" 
                                                      show-meridian="ismeridian" 
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                    </td> --> 
                                     <td class = 'col-md-1'>{{requests.shiftTime}}</td>                                        
                                     <td class = 'col-md-1'>{{requests.requestType}}</td>
                                     <td class = 'col-md-3'>{{requests.address}}</td>

                                    <td class = 'col-md-1' ng-show = '!requests.editRequestDetailIsClicked'>{{requests.routeName}}</td>                                           
                                    <td  class = 'col-md-1' ng-show = 'requests.editRequestDetailIsClicked'>   
                                      <select class = 'form-control'
                                              ng-model="requests.routeName" 
                                              ng-options="zoneData.routeName as zoneData.routeName for zoneData in zonesData"
                                              ng-change = "setZone(update.zone)"
                                              required>
                                          <option value="">SELECT ROUTES</option>
                                      </select>
                                    </td>      

                                    <td class = 'col-md-1' ng-show = '!requests.editRequestDetailIsClicked'>{{requests.areaName}}</td>                                           
                                    <td  class = 'col-md-1' ng-show = 'requests.editRequestDetailIsClicked'>   
                                      <select class = 'form-control'
                                              ng-model="requests.areaName" 
                                              ng-options="areaDetail.areaName as areaDetail.areaName for areaDetail in areaDetails"
                                              ng-change = "setZone(update.zone)"
                                              required>
                                          <option value="">SELECT AREA NAME</option>
                                      </select>
                                    </td>  

                                    <td class = 'col-md-1'>
                                             <input type = "button"
                                                    class = "btn btn-danger btn-xs disable_vendorDeviceDetail"
                                                    value = "Disable"
                                                    ng-click = "disableRequest(requests, $index)"
                                                    ng-class = "{disabled: requests.status=='N'}">
                                    </td>

                                </tr>                                   
                             </tbody>
                            </table>
                        </div>