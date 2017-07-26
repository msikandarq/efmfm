<div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.rescheduleCab" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
                                </div>
                            <br>
                            <br>
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid">
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

                                
                                <tbody ng-show = "empRequestDetailsDataReshedule.length==0">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Reschedule Requests</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsDataReshedule.length>0">
                               <tr ng-repeat = "request in empRequestDetailsDataReshedule | filter:search.rescheduleCab"
                                       ng-show = 'is24hrRequest'>
                                      <td class = "col-md-1">{{request.requestId}}</td>
                                        <td class = "col-md-2" 
                                          ng-show = "!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}} 
                                       </td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule">
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
                                       
                                       <td class = "col-md-2" ng-show = "!request.needReshedule">{{request.requestDate}}</td>

                                       <td class = "col-md-2" ng-if="request.needReshedule && request.tripType == 'PICKUP'">{{request.requestDate}}</td>

                                       <td class = "col-md-2" 
                                       ng-if="request.tripType == 'DROP' && request.needReshedule">                                 
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
                                  <td class = "col-md-3">{{request.address}}</td>
                                      <td class = "col-md-1">{{request.tripType}}</td>
                                      <td class = "col-md-2 requestButtonsDiv">
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