<tabset class="tabset subTab_empRequestDetail">
    <tab ng-click="getTodayRequestDetails()">
        <tab-heading>Normal Booking Requests</tab-heading>
        <div class="empRequestDetailsTabContent row">
            <div class="modal-header2 col-md-10"></div>
            <div class="modal-header2 col-md-2">
                <select ng-model="search.valueSchedule" class="form-control dashboardFilterDropdown" ng-change="searchTripTypeBookingSchedule(search.valueSchedule, empRequestDetailsData)" ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                <option value="">---Search  Trip Type---</option>
                </select>
            </div>
            <br>
            <br>
            <table class="empRequestDetailsTable table table-hover table-responsive container-fluid" >
                <thead class="tableHeading">
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

                <tbody ng-show="filtered.length==0 || normalLocation.length == 0 || bookingRequestDetailsShow">
                    <tr>
                        <td colspan='9'>
                            <div class="noData">There are no Normal Booking Requests</div>
                        </td>
                    </tr>
                </tbody>

                <tbody ng-show="filtered.length>0">
                    <!--  <tr ng-repeat="request in empRequestDetailsData | filter:search.valueSchedule"> -->
                     <tr ng-repeat="request in filtered = (normalLocation | filter:search.valueSchedule)">
                     <!-- <div ng-repeat="person in filtered = (data | filter: query)"> -->

                        <td class="col-md-1" ng-hide="request.locationFlg == 'M'">{{request.requestId}}</td>
                        <td class="col-md-2" ng-hide="request.locationFlg == 'M'" ng-show="!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}}</td>
                        <td class="col-md-2 resheduleTimeDiv_empReq" ng-show="request.needReshedule && request.locationFlg != 'M'">
                            <input type="button" class="btn btn-info btn-xs floatLeft" value="Change" ng-click="adHoc()" ng-show="!needAdHoc">

                            <input type="button" class="btn btn-danger btn-xs floatLeft cancelAdHocButton" value="Cancel" ng-click="cancelAdHoc()" ng-show="needAdHoc">
                        </td>
                        <td ng-hide="request.locationFlg == 'M'" class="col-md-1">{{request.requestDate}}</td>
                        <td ng-hide="request.locationFlg == 'M'" class="col-md-3">{{request.employeeAddress}}</td>
                        <td ng-hide="request.locationFlg == 'M'" class="col-md-1">{{request.tripType}}</td>
                        <td ng-hide="request.locationFlg == 'M'" class = "col-md-1">{{request.requestRemarks}}</td>
                        <td ng-if = "request.locationFlg != 'M' && request.reqApprovalStatus == 'Y'"
                            class = "col-md-1">Approved</td>
                        <td ng-if="request.locationFlg != 'M' && request.reqApprovalStatus == 'N'"
                            class = "col-md-1">Not Approved</td>
                        <td ng-if="request.locationFlg != 'M' && request.reqApprovalStatus == 'R'"
                            class = "col-md-1">Rejected</td>

                    </tr>
                </tbody>
            </table>
        </div>
    </tab>
    <tab ng-click="getTodayRequestDetails()">
        <tab-heading>Multiple Location Booking Requests</tab-heading>
        <div class="empRequestDetailsTabContent row">
            <div class="modal-header2 col-md-10"></div>
            <div class="modal-header2 col-md-2">
                <select ng-model="search.valueSchedule" class="form-control dashboardFilterDropdown" ng-change="searchTripTypeBookingSchedule(search.valueSchedule, empRequestDetailsData)" ng-options="tripTypeFilter.value as tripTypeFilter.text for tripTypeFilter in tripTypeFilters">
                                    <option value="">---Search  Trip Type---</option>
                                    </select>
            </div>
            <br>
            <br>
            <table class="empRequestDetailsTable table table-hover table-responsive container-fluid" infinite-scroll="getTodayRequestDetails()" infinite-scroll-parent="true">
                <thead class="tableHeading">
                    <tr>
                        <th>Map</th>
                        <th>View Location Details</th>
                        <th>Request Id</th>
                        <th>Shift Time</th>
                        <th>Request Date</th>
                        <th>Drop/Pickup Location</th>
                        <th>Trip Type</th>
                        <th>Remarks</th>
                        <th>Approval Status</th>
                    </tr>
                </thead>

                <tbody ng-show="flitered.length==0 || multipleLocation.length == 0 || bookingRequestDetailsShow">
                    <tr>
                        <td colspan='9'>
                            <div class="noData">There are no multiple location Booking Requests</div>
                        </td>
                    </tr>
                </tbody>

                <tbody ng-show="flitered.length>0">
                    <tr ng-repeat="request in flitered = (multipleLocation | filter:search.valueSchedule)">
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
                        <td class="col-md-1" ng-hide="request.locationFlg == 'N'">{{request.requestId}}</td>
                        <td class="col-md-2" ng-hide="request.locationFlg == 'N'" ng-show="!request.needReshedule">{{request.tripTime |date:'HH:mm:ss'}}</td>
                        <td class="col-md-2 resheduleTimeDiv_empReq" ng-show="request.needReshedule">
                            <input type="button" class="btn btn-info btn-xs floatLeft" value="Change" ng-click="adHoc()" ng-show="!needAdHoc">

                            <input type="button" class="btn btn-danger btn-xs floatLeft cancelAdHocButton" value="Cancel" ng-click="cancelAdHoc()" ng-show="needAdHoc">
                        </td>
                        <td class="col-md-1" ng-hide="request.locationFlg == 'N'">{{request.requestDate}}</td>
                        <td class="col-md-3" ng-hide="request.locationFlg == 'N'">{{request.employeeAddress}}</td>
                        <td class="col-md-1" ng-hide="request.locationFlg == 'N'">{{request.tripType}}</td>
                        <td class = "col-md-1" ng-hide="request.locationFlg == 'N'">{{request.requestRemarks}}</td>
                        <td class = "col-md-1" ng-hide="request.locationFlg == 'N'">Not Approved</td>

                    </tr>
                </tbody>
            </table>
        </div>

    </tab>
</tabset>
