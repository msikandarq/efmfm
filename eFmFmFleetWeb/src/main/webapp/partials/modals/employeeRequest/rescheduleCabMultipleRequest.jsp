<tabset class="tabset subTab_empRequestDetail">
                          <tab ng-click = "getTodayRequestDetails()">
                              <tab-heading>Normal Reschedule Cab</tab-heading>
                              <div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.rescheduleCab" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-change="searchTripTypeBookingSchedule(search.rescheduleCab, empRequestDetailsDataReshedule)"
                                    ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
                                </div>
                            <br>
                            <br>
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid" >
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                      <th>Reschedule</th>
                                   </tr> 
                                </thead>

                                
                                <tbody ng-show = "empRequestDetailsDataReshedule.length==0 || requestDetailsNormalRequest.length == 0 || bookingRequestDetailsShow">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Reschedule Requests</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsDataReshedule.length>0">
                               <tr ng-repeat = "request in empRequestDetailsDataReshedule | filter:search.rescheduleCab"
                                       ng-show = 'is24hrRequest'>
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'M'">{{request.requestId}}</td>
                                        <td class = "col-md-2" 
                                          ng-show = "!request.needReshedule" ng-hide="request.locationFlg == 'M'">{{request.tripTime |date:'HH:mm:ss'}} 
                                       </td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule && request.locationFlg != 'M'">
                                          <select ng-model="newTripTime" 
                                                  ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataReschedule track by tripTimes.shiftId""
                                                  ng-change = "setNewTripTime(newTripTime, request)"
                                                  ng-show = "!needAdHoc"
                                                  class = "floatLeft">
                                                    <option value="">- Select Shift Time -</option>
                                          </select>

                                          <timepicker ng-model="adHoctime" 
                                                      ng-change="setNewTripTime(adHoctime, request)" 
                                                      hour-step="hstep" 
                                                      minute-step="mstep" 
                                                      show-meridian="ismeridian"
                                                      ng-if = "needAdHoc"
                                                      class = "floatLeft timepicker_empReq">
                                          </timepicker>
                                          <div ng-show = "adhocTimePicker == 'Yes'">
                                          <input type = "button" 
                                                 class = "btn btn-info btn-xs floatLeft" 
                                                 value = "AD HOC" 
                                                 ng-click = "adHoc()" 
                                                 ng-show = "!needAdHoc">
                                        </div>
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs floatLeft cancelAdHocButton" 
                                                 value = "Cancel" 
                                                 ng-click = "cancelAdHoc()" 
                                                 ng-if = "needAdHoc">
                                      </td>
                                       
                                       <td class = "col-md-2" ng-show = "!request.needReshedule" ng-hide="request.locationFlg == 'M'">{{request.requestDate}}</td>

                                       <td class = "col-md-2" ng-if="request.needReshedule && request.tripType == 'PICKUP'" ng-hide="request.locationFlg == 'M'">{{request.requestDate}}</td>

                                       <td class = "col-md-2" ng-if="request.tripType == 'DROP' && request.needReshedule" ng-hide="request.locationFlg == 'M'">                                 
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                       
                                                 <input ng-model = "fromDate"
                                                       class="form-control" 
                                                       placeholder = "Request Date"
                                                       ng-change = "isFromDateChange(fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly>                                                                                        
                                           </div>
                                       </td>
                                  <td class = "col-md-3" ng-hide="request.locationFlg == 'M'">{{request.address}}</td>
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'M'">{{request.tripType}}</td>
                                      <td class = "col-md-2 requestButtonsDiv" ng-hide="request.locationFlg == 'M'">
                                          <input type = "button" 
                                                 class = "btn btn-warning btn-xs" 
                                                 value = "Re-Schedule"
                                                 ng-click = "reSheduleRequest(request)"
                                                 ng-show = "!request.needReshedule"
                                                 ng-disabled = "saveIsClicked">
                                          <input type = "button" 
                                                 class = "btn btn-success btn-xs" 
                                                 value = "Save"
                                                 ng-click = "saveRequest(request, fromDate, $index, newTripTime, adHoctime)"
                                                 ng-show = "request.needReshedule">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel Reschedule"
                                                 ng-click = "cancelReschedule(request, $index)"
                                                 ng-show = "request.needReshedule">
                                       </td>
                                </tr>                                    
                             </tbody>
                            </table>
                            </div>
                          </tab>
                          <tab ng-click = "getTodayRequestDetails()">
                              <tab-heading>MultiLocation Reschedule Cab</tab-heading>
                              <div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.rescheduleCab" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-change="searchTripTypeBookingSchedule(search.rescheduleCab, empRequestDetailsDataReshedule)"
                                    ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
                                </div>
                            <br>
                            <br>
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid" infinite-scroll="getRescheduleRequestDetails()" infinite-scroll-parent="true">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Map</th>
                                      <th>View Location Details</th>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                      <th>Reschedule</th>
                                   </tr> 
                                </thead>

                                
                                <tbody ng-show = "empRequestDetailsDataReshedule.length==0 || requestDetailsMultipleRequest.length == 0 || bookingRequestDetailsShow">
                                    <tr>
                                        <td colspan = '8'>
                                            <div class = "noData">There are no Reschedule Requests</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsDataReshedule.length>0">
                               <tr ng-repeat = "request in empRequestDetailsDataReshedule | filter:search.rescheduleCab"
                                       ng-show = 'is24hrRequest'>
                                      <td class="col-md-1" ng-hide="request.locationFlg == 'N'">
                                        <span class="pointer" ng-click = "multipleLocationMapView(request)">
                                            <i class="icon-map-marker mapMarkerIcon ng-scope" 
                                               tooltip="View on Map" 
                                               tooltip-placement="right" 
                                               tooltip-trigger="mouseenter">
                                            </i>
                                        </span>
                                      </td>
                                      <td class="col-md-1" ng-hide="request.locationFlg == 'N'">
                                            <input type="button" class="btn btn-success btn-xs" value="View" ng-click="getLocationDetails(request)" name="">
                                      </td>
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'N'">{{request.requestId}}</td>
                                        <td class = "col-md-2" 
                                          ng-show = "!request.needReshedule" ng-hide="request.locationFlg == 'N'">{{request.tripTime |date:'HH:mm:ss'}} 
                                       </td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule && request.locationFlg == 'M'">
                                      
                                          <select ng-model="newTripTime" 
                                                  ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataReschedule track by tripTimes.shiftId""
                                                  ng-change = "setNewTripTime(newTripTime, request)"
                                                  ng-show = "!needAdHoc"
                                                  class = "floatLeft">
                                                    <option value="">- Select Shift Time -</option>
                                          </select>

                                          <timepicker ng-model="adHoctime" 
                                                      ng-change="setNewTripTime(adHoctime, request)" 
                                                      hour-step="hstep" 
                                                      minute-step="mstep" 
                                                      show-meridian="ismeridian"
                                                      ng-if = "needAdHoc"
                                                      class = "floatLeft timepicker_empReq">
                                          </timepicker>
                                          <div ng-show = "adhocTimePicker == 'Yes'">
                                          <input type = "button" 
                                                 class = "btn btn-info btn-xs floatLeft" 
                                                 value = "AD HOC" 
                                                 ng-click = "adHoc()" 
                                                 ng-show = "!needAdHoc">
                                        </div>
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs floatLeft cancelAdHocButton" 
                                                 value = "Cancel" 
                                                 ng-click = "cancelAdHoc()" 
                                                 ng-if = "needAdHoc">
                                      </td>
                                       
                                       <td class = "col-md-2" ng-hide="request.locationFlg == 'N'" ng-show = "!request.needReshedule">{{request.requestDate}}</td>

                                       <td class = "col-md-2" ng-hide="request.locationFlg == 'N'" ng-if="request.needReshedule && request.tripType == 'PICKUP'">{{request.requestDate}}</td>

                                       <td class = "col-md-2"  ng-hide="request.locationFlg == 'N'" ng-show = "request.needReshedule" ng-if="request.tripType == 'DROP'">                                 
                                            <div class = "input-group calendarInput">
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                       
                                                 <input ng-model = "fromDate"
                                                       class="form-control" 
                                                       placeholder = "Request Date"
                                                       ng-change = "isFromDateChange(fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "setMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       required
                                                       readonly>                                                                                        
                                           </div>
                                       </td>
                                  <td class = "col-md-2" ng-hide="request.locationFlg == 'N'">{{request.address}}</td>
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'N'">{{request.tripType}}</td>
                                      <td class = "col-md-2 requestButtonsDiv" ng-hide="request.locationFlg == 'N'">
                                          <input type = "button" 
                                                 class = "btn btn-warning btn-xs" 
                                                 value = "Re-Schedule"
                                                 ng-click = "reSheduleRequest(request)"
                                                 ng-show = "!request.needReshedule"
                                                 ng-disabled = "saveIsClicked">
                                          <input type = "button" 
                                                 class = "btn btn-success btn-xs" 
                                                 value = "Save"
                                                 ng-click = "saveRequest(request, fromDate, $index, newTripTime, adHoctime)"
                                                 ng-show = "request.needReshedule">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel Reschedule"
                                                 ng-click = "cancelReschedule(request, $index)"
                                                 ng-show = "request.needReshedule">
                                       </td>
                                </tr>                                    
                             </tbody>
                            </table>
                            </div>


                          </tab>
                    </tabset>