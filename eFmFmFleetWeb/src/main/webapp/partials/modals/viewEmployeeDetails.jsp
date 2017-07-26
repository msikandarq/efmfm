<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "addVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
             <div class = ""></div>     
            <div class = "empDetailModalHeading col-md-10 floatLeft">Employee Details</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    
    <div class="modal-body modalMainContent">
    <div class="col-md-8">
        <div class="row">
        <div class="col-md-8">
            <h5 class="empDetailModalSum empDetaillengthReport">
                Employee/Message Details Summary Report - <span
                class="badge">{{employeeDetailsDataLength}}</span>
            </h5>
        </div>
        </div>
        </div>
        <div class="col-md-4">
            <input type='text' placeholder="Filter"
            class='form-control floatRight' ng-model='efmfilter.filterDRModal'>
        </div>  
        <table class="table table table-bordered table-striped table-responsive">    
            <thead>
                <tr class="success">
                    <th>S No</th>
                    <th ng-show="dataDetailsEmployee.employeeNameFlg == 1">Employee Name</th>
                    <th ng-show="dataDetailsEmployee.allocationMsgFlg == 1">Allocation Message</th>
                    <th ng-show="dataDetailsEmployee.cabReachedTimeFlg == 1">Cab Reached Time</th>
                    <th ng-show="dataDetailsEmployee.cabDelayMsgFlg == 1">Cab Delay Message</th>
                    <th ng-show="dataDetailsEmployee.pickUpTimeFlg == 1">PickUp Time</th>
                    <th ng-show="dataDetailsEmployee.employeeIdFlg == 1">Employee Id</th>
                    <th ng-show="dataDetailsEmployee.empLoacationFlg == 1">Employee Location</th>
                    <th ng-show="dataDetailsEmployee.emailIdFlg == 1">Employee Email Id</th>
                    <th ng-show="dataDetailsEmployee.employeeMobileNoFlg == 1">Mobile Number</th>
                    <th ng-show="dataDetailsEmployee.reachedMsgFlg == 1">Reached Message</th>
                    <th ng-show="dataDetailsEmployee.boardingStatusFlg == 1">Boarding Status</th>
                    <th ng-show="dataDetailsEmployee.hostMobileNumberFlg == 1">Host Mobile Number</th>
                    <th ng-show="dataDetailsEmployee.boardingTimeFlg == 1">Boarding Time</th>
                    <th ng-show="dataDetailsEmployee.fifteenMinuteMsgFlg == 1">ETA 15 min Message</th>
                    <th ng-show="dataDetailsEmployee.noshowMsgFlg == 1">No Show Message</th>
                </tr>
            </thead>
        <tbody>
        
        <tr ng-repeat="employee in employeeDetailsData|filter:efmfilter.filterDRModal| orderBy:dateAscDsc:reverse">
            <td>
            {{$index+1}}
            </td>
            <td ng-show="dataDetailsEmployee.employeeNameFlg == 1">
            {{employee.empName}}
            </td>
            <td ng-show="dataDetailsEmployee.allocationMsgFlg == 1">
            {{employee.allocationMsg}}
            </td>
            <td ng-show="dataDetailsEmployee.cabDelayMsgFlg == 1">
            {{employee.cabDelayMsg}}
            </td>
            <td ng-show="dataDetailsEmployee.pickUpTimeFlg == 1">
            {{employee.pickUpTime}}
            </td>
            <td ng-show="dataDetailsEmployee.employeeIdFlg == 1">
            {{employee.employeeId}}
            </td>
            <td ng-show="dataDetailsEmployee.empLoacationFlg == 1">
            {{employee.employeeLocation}}
            </td>
            <td ng-show="dataDetailsEmployee.emailIdFlg == 1">
            {{employee.empEmailId}}
            </td>
            <td ng-show="dataDetailsEmployee.employeeMobileNoFlg == 1">
            {{employee.mobileNumber}}
            </td>
            <td ng-show="dataDetailsEmployee.reachedMsgFlg == 1">
            {{employee.reachedMsg}}
            </td>
            <td ng-show="dataDetailsEmployee.boardingStatusFlg == 1">
            {{employee.boardingStatus}}
            </td>
            <td ng-show="dataDetailsEmployee.hostMobileNumberFlg == 1">
            {{employee.hostMobileNumber}}
            </td>
            <td ng-show="dataDetailsEmployee.boardingTimeFlg == 1">
            {{employee.boardingTime}}
            </td>
            <td ng-show="dataDetailsEmployee.fifteenMinuteMsgFlg == 1">
            {{employee.fifteenMsg}}
            </td>
             <td ng-show="dataDetailsEmployee.cabReachedTimeFlg == 1">
            {{employee.cabReachedTime}}
            </td>
             <td ng-show="dataDetailsEmployee.noshowMsgFlg == 1">
            {{employee.noShowMsg}}
            </td>
        </tr>
        </table>
    </div>   
    <div class="modal-footer modalFooter">    
 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>   
</div>