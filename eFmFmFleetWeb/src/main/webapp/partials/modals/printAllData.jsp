<div ng-include="'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div class="addNewShiftModalTemplate">

    <div class="row">
        <div class="modal-header modal-header1 col-md-12">
            <div class="row">
                <div class="icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
                <div class="modalHeading col-md-10 floatLeft">Print Preview</div>
                <div class="col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class="floatRight" ng-click="cancel()">
                </div>
            </div>
        </div>
    </div class="containar">
    <div id="printable">
        <div class="modal-body" ng-repeat="routePrintModal in routePrintModals">
            <div>{{todayDate | date:'medium'}}</div><br>
            <table style="border:1px solid gray; border-collapse: collapse;" class="table table-bordered printTab">
                <tbody style="border:2px solid black;">
                    <tr style="border:1px solid gray; padding:5px; font-size: 10px;">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="4">Journey Management Plan -<strong>{{routePrintModal.shiftTime}} {{routePrintModal.tripType}}</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Employee Transportation</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Date</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>{{routePrintModal.tripActualAssignDate}}</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" rowspan="5">Alternate Driver</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" rowspan="5">Rest Break</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" rowspan="5">Contact Notification Details</td>
                    </tr>
                    <tr style="border:1px solid gray; padding:5px; font-size: 10px;">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="4">Is the trip necessary? Why?</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Yes, for transportation of employees.</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Route No</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>{{routePrintModal.routeId}}</strong></td>
                    </tr>
                    <tr style="border:1px solid gray; padding:5px; font-size: 10px;">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="4">Can it be combined with another trip? If not, why?</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Yes, group trip</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Vehicle Type</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>{{routePrintModal.vehicleType}}</strong></td>
                    </tr>
                    <tr style="border:1px solid gray; padding:5px; font-size: 10px;">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="4">Is there a need for night driving? If so, why?</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Yes, for transportation of shift employees</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Route Name</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>{{routePrintModal.routeName}}</strong></td>
                    </tr>
                    <tr style="border:1px solid gray; padding:5px; font-size: 10px;">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="4">Name of person acting as Journey Manager</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Transport Team</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">Phone Number</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">+{{routePrintModal.transportNumber}}</td>
                    </tr>
                    <tr class="warning">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Departure Time</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Arrival Time
        </strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2">
                            <strong>Vehicle Number</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Name of Main Driver
        </strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Is the vehicle fit for the trip?</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2">
                            <strong>Is/are the driving licence/s valid for the vehicle and country</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2"><strong>
        Is Defensive Driving Training for the Driver (s) Valid? (Yes/No/NA)</strong></td>
                    </tr>
                    <tr>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2">
                            {{routePrintModal.vehicleNumber}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{routePrintModal.driverName}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">YES</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2">YES</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2">YES</td>
                    </tr>
                    <tr class="warning">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>S No</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Passenger Names</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Male/ Female</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Emp ID</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Address</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Area (Starting Km)</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Land Mark</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;"><strong>Boarding Time (Closing Km)</strong></td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2"><strong>Remarks</strong></td>
                    </tr>
                    <tr ng-repeat="emp in routePrintModal.empDetails">
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{$index +1}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{emp.name}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{emp.gender}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{emp.employeeId}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{emp.address}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{emp.empArea}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;">{{emp.pickUpTime}}</td>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="2">{{}}</td>
                    </tr>
                    <tr>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="5">Before Driver's Signature:
                            <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="5">After Driver's Signature:</td>
                    </tr>
                    <tr>
                        <td style="border:1px solid gray; padding:5px; font-size: 10px;" colspan="10">
                            <div class="col-sm-2">Life Saving rule</div>
                            <div class="col-sm-10"><img src="images/img_001.png" alt="alt"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div style="page-break-after:always;">
            </div>
        </div>

    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-success buttonRadius0 noMoreClick" data-ng-click="printDiv('printable')">Print</button>
        <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click="close()">Close</button>
    </div>

</div>
<!--ng-disabled="addUserForm.$invalid"-->