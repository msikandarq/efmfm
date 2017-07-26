<div class="escortAvailableTabContent requestDetailMyProfile row">
    <div class="row firstRowActionDiv">
        <div class='col-md-4 searchEmployeeTravelDesk'>
        </div>

        <div class='col-md-2'></div>
        <div class="col-md-3 floatRight">
            <input type="text" class='form-control requestDetailFirstRow' ng-model="filterRequests" placeholder="Filter Pending Requests" expect_special_char>
        </div>
    </div>
    <div class="row">
                    <div class="col-md-9"></div> 
                    <div class="col-md-3">
                      <div class="btn-group pull-right"> 
                        <button type="button" class="btn btn-success btn-xs" ng-click="checkAll()" ng-show="getPendingApprovalRequestsData.length !=0">Check All</button>
                        <button type="button" class="btn btn-danger btn-xs" ng-click="uncheckAll()" ng-show="getPendingApprovalRequestsData.length !=0">Uncheck All</button>
                        <button type="button" class="btn btn-primary btn-xs" ng-click="approveAllPendingRequests(getPendingApprovalRequestsData,search)" ng-show="getPendingApprovalRequestsData.length !=0">Approve All</button>
                        </div> 
                    </div>
                </div>
    <table class="escortAvailableTable table table-responsive container-fluid">
        <thead class="tableHeading">
            <tr>
            	<th>Select Request</th>
                <th>Employee Id</th>
                <th>Employee Name</th>
                <th>Trip Type</th>
                <th>Request Id</th>
                <th>Shift Time</th>
                <th>Request Date</th>
                <th>Approve</th>
                <th>Reject</th>

            </tr>
        </thead>

        <tbody ng-show="getPendingApprovalRequestsData.length==0">
            <tr>
                <td colspan='1'>
                </td>
                <td colspan='6'>
                    <div class="noData">There are no pending approval requests</div>
                </td>
            </tr>
        </tbody>
        <tbody ng-show="getPendingApprovalRequestsData.length>0">
            <tr ng-repeat="requests in getPendingApprovalRequestsData|filter:filterRequests">
            	<td class='col-md-1'>
                      <input type="checkbox" class="locationCheckBoxMargin" ng-model="requests.status" ng-change="setLocationSelectedValues(bulkRoute,bulkRoute.status)"/>
                </td>
                <td class='col-md-1'>{{requests.employeeId}}</td>
                <td class='col-md-1'>{{requests.employeeName}}</td>
                <td class='col-md-1'>{{requests.tripType}}</td>
                <td class='col-md-1'>{{requests.requestId}}</td>
                <td class='col-md-1'>{{requests.shiftTime}}</td>
                <td class='col-md-1'>{{requests.requestDate}}</td>
                <td class='col-md-1'><input type="button" class="btn btn-success btn-xs" value="Approve" ng-click="approvePendingRequest(requests, $index)" name=""></td>
                <td class='col-md-1'><input type="button" class="btn btn-danger btn-xs" value="Reject" ng-click="rejectPendingRequest(requests,$index)" name=""></td>

            </tr>
        </tbody>
    </table>
</div>