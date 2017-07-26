<div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.valueSchedule" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
                                </div>
                            <br>
                            <br>
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid" 
                           >
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                      <th>Remarks</th>
                                      <th>Approval Status</th>
                                   </tr> 
                                </thead>
                                <tbody ng-show = "empRequestDetailsData.length==0">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Booking Schedule Request</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsData.length>0">
                               <tr ng-repeat = "request in empRequestDetailsData | filter:search.valueSchedule">
                                      <td class = "col-md-1">{{request.requestId}}</td>
                                  <td class = "col-md-2" ng-show = "!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}}</td>
                                      <td class = "col-md-2 resheduleTimeDiv_empReq" ng-show = "request.needReshedule">
                                          <input type = "button" 
                                                 class = "btn btn-info btn-xs floatLeft" 
                                                 value = "Change" 
                                                 ng-click = "adHoc()" 
                                                 ng-show = "!needAdHoc">
                                          
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs floatLeft cancelAdHocButton" 
                                                 value = "Cancel" 
                                                 ng-click = "cancelAdHoc()" 
                                                 ng-show = "needAdHoc">
                                      </td>
                                      <td class = "col-md-1">{{request.requestDate}}</td>
                                      <td class = "col-md-3">{{request.employeeAddress}}</td>
                                      <td class = "col-md-1">{{request.tripType}}</td>
                                      <td class = "col-md-1">{{request.requestRemarks}}</td>
                                      <td class = "col-md-1">Not Approved</td>
                                     
                    </tr>

                                </tr>                                    
                             </tbody>
                            </table>
                            
            </div>