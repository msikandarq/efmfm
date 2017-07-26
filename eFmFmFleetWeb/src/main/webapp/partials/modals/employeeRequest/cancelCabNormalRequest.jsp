<div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.cancelcab" 
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
                                      <th>Cancel</th>
                                   </tr> 
                                </thead>
                                <tbody ng-show = "empRequestDetailsDataCancel.length==0">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Requests for Cancel</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsDataCancel.length>0">
                               <tr ng-repeat = "request in empRequestDetailsDataCancel | filter:search.cancelcab" class = "request{{request.requestId}}">
                                      <td class = "col-md-1">{{request.requestId}}</td>
                                  <td class = "col-md-2">{{request.tripTime |date:'HH:mm:ss'}}</td>
                                      <td class = "col-md-1">{{request.requestDate}}</td>
                                  <td class = "col-md-3">{{request.address}}</td>
                                      <td class = "col-md-1">{{request.tripType}}</td>
                                      <td class = "col-md-3 requestButtonsDiv">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel"
                                                 ng-click = "deleteRequest(request, $index)">
                                       </td>
                                </tr>                                    
                             </tbody>
                            </table>
                            

            </div>