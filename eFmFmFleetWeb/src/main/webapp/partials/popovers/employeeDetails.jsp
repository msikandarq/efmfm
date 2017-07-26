<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    POPUP Templates - Trip Time Reports

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div class = "empDetailPopoverTemp container-fluid">    
    <div class = "row marginRow">
    {{trip.tripType}}
    <div class = "col-md-4 floatLeft padding0">ID : </div>
        <div class = "col-md-8 floatLeft">{{employee.empId}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0">Name : </div>
        <div class = "col-md-8 floatLeft">{{employee.empName}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0">Gender : </div>
        <div class = "col-md-8 floatLeft">{{employee.empGender}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0">Destination : </div>
        <div class = "col-md-8 floatLeft">{{employee.empAddress}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0">Shift Time : </div>
        <div class = "col-md-8 floatLeft">{{employee.shiftTime}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0" ng-show="trip.tripType == 'PICKUP'">Scheduled Pickup Time : </div>
        <div class = "col-md-4 floatLeft padding0" ng-show="trip.tripType == 'DROP'">Scheduled Drop Time : </div>
        <div class = "col-md-8 floatLeft">{{employee.scheduleTime}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0" ng-show="trip.tripType == 'PICKUP'">Actual Pickup Time : </div>
        <div class = "col-md-4 floatLeft padding0" ng-show="trip.tripType == 'DROP'">Actual Drop Time : </div>
        <div class = "col-md-8 floatLeft">{{employee.travelTime}}</div> </div>
    
    <div class = "row marginRow">
        <div class = "col-md-4 floatLeft padding0">Employee Status : </div>
        <div class = "col-md-8 floatLeft">{{employee.travelStatus}}</div> </div>
</div>
