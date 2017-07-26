<tabset class="tabset subTab_empRequestDetail">
    <tab ng-click="getCancelRequestDetails()">
        <tab-heading>Cancel Cab Normal Request</tab-heading>
          <div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.cancelcab" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-change="searchTripTypeCancelCab(search.cancelcab, empRequestDetailsDataCancel)"
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
                                <tbody ng-show = "empRequestDetailsDataCancel.length==0 || requestDetailsNormalRequest.length == 0 || bookingRequestDetailsShow">
                                    <tr>
                                        <td colspan = '7'>
                                            <div class = "noData">There are no Requests for Cancel</div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody ng-show = "empRequestDetailsDataCancel.length>0">
                               <tr ng-repeat = "request in empRequestDetailsDataCancel | filter:search.cancelcab" class = "request{{request.requestId}}">
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'M'">{{request.requestId}}</td>
                                  <td class = "col-md-2" ng-hide="request.locationFlg == 'M'">{{request.tripTime |date:'HH:mm:ss'}}</td>
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'M'">{{request.requestDate}}</td>
                                  <td class = "col-md-3" ng-hide="request.locationFlg == 'M'">{{request.address}}</td>
                                      <td class = "col-md-1" ng-hide="request.locationFlg == 'M'">{{request.tripType}}</td>
                                      <td class = "col-md-3 requestButtonsDiv" ng-hide="request.locationFlg == 'M'">
                                          <input type = "button" 
                                                 class = "btn btn-danger btn-xs" 
                                                 value = "Cancel"
                                                 ng-click = "deleteRequest(request, $index)">
                                       </td>
                                </tr>                                    
                             </tbody>
                            </table>
            </div>
    </tab>
    <tab ng-click="getCancelRequestDetails()">
        <tab-heading>Cancel Cab Multiple Location Request</tab-heading>
                  <div class = "empRequestDetailsTabContent row">
                              <div class="modal-header2 col-md-10"></div>
                                <div class="modal-header2 col-md-2">
                                    <select ng-model="search.cancelcab" 
                                    class="form-control dashboardFilterDropdown" 
                                    ng-change="searchTripTypeCancelCab(search.cancelcab, empRequestDetailsDataCancel)"
                                    ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
                                </div>
                            <br>
                            <br>
                           <table class = "empRequestDetailsTable table table-hover table-responsive container-fluid" infinite-scroll="getCancelRequestDetails()" infinite-scroll-parent="true">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Map</th>
                                      <th>View Location Details</th>
                                      <th>Request Id</th>
                                      <th>Shift Time</th>
                                      <th>Request Date</th>
                                      <th>Drop/Pickup Location</th>
                                      <th>Trip Type</th>
                                      <th>Cancel</th>
                                   </tr> 
                                </thead>
                                <tbody ng-show = "empRequestDetailsDataCancel.length==0 || requestDetailsMultipleRequest.length == 0 || bookingRequestDetailsShow">
                                    <tr>
                                        <td colspan = '8'>
                                            <div class = "noData">There are no multiple location Requests for Cancel</div>
                                        </td>
                                    </tr>
                                </tbody>

                                <tbody ng-show = "empRequestDetailsDataCancel.length>0">
                               <tr ng-repeat = "request in empRequestDetailsDataCancel | filter:search.cancelcab" class = "request{{request.requestId}}">
                                  <td class="col-md-1" ng-hide="request.locationFlg == 'N'">
                                  <span class="pointer" ng-click="multipleLocationMapView(request)">
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
                                  <td class = "col-md-2" ng-hide="request.locationFlg == 'N'">{{request.tripTime |date:'HH:mm:ss'}}</td>
                                  <td class = "col-md-1" ng-hide="request.locationFlg == 'N'">{{request.requestDate}}</td>
                                  <td class = "col-md-3" ng-hide="request.locationFlg == 'N'">{{request.address}}</td>
                                        <td class = "col-md-1" ng-hide="request.locationFlg == 'N'">{{request.tripType}}</td>
                                        <td class = "col-md-3 requestButtonsDiv" ng-hide="request.locationFlg == 'N'">
                                            <input type = "button" 
                                                   class = "btn btn-danger btn-xs" 
                                                   value = "Cancel"
                                                   ng-click = "deleteRequest(request, $index)">
                                  </td>
                                </tr>                                    
                             </tbody>
                            </table>
            </div>
    </tab>
</tabset>


