<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    POPUP Templates - for the Calendars

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div class = "calenderReportPopoverTemp container-fluid">
    <div class = "row marginRow mainDiv">
      <div class = "dateRangeDiv">
        <ul class = "dateRange">
          <li ng-click = "updateDate('today',facilityData.listOfFacility)" class = "pointer todayReport reportDateList" ng-class = "{inActive: dates[0].isClicked}">Today</li>
          <li ng-click = "updateDate('yesturday',facilityData.listOfFacility)" class = "pointer yesturdayReport reportDateList" ng-class = "{inActive: dates[1].isClicked}">Yesterday</li>
          <li ng-click = "updateDate('last7',facilityData.listOfFacility)" class = "pointer last7Report reportDateList" ng-class = "{inActive: dates[2].isClicked}">Last 7 Days</li>
          <li ng-click = "updateDate('last30',facilityData.listOfFacility)" class = "pointer last30Report reportDateList" ng-class = "{inActive: dates[3].isClicked}">Last 30 Days</li>
          <li ng-click = "updateDate('thisMonth',facilityData.listOfFacility)" class = "pointer thisMonth reportDateList" ng-class = "{inActive: dates[4].isClicked}">This Month</li>
          <li ng-click = "updateDate('customDate',facilityData.listOfFacility)" class = "pointer customRange reportDateList" ng-class = "{customDivIsOpen: openCustomRange}">Custom Range</li>
          <li class = 'customDateDiv' ng-show = "openCustomRange">
            <form class = 'row' name = "calender">
                <div class = 'col-md-12 col-xs-12 customInput1'><span>From</span><br>
                    <div class = "input-group calendarInput calendarInput2">
                        <input type="text" 
                             ng-model="from" 
                             id = "from"
                             class="form-control" 
                             name = "fromDate"
                             placeholder=""
                             ng-change = "checkDateRangeValidity(from, to)"
                             datepicker-popup = '{{format}}'
                             is-open="datePicker.fromDate"
                             show-button-bar = false
                             datepicker-options = 'dateOptions'
                             required
                             ng-class = "{error: calender.fromDate.$invalid && !calender.fromDate.$pristine}">
                        <span class="input-group-btn"><button class="btn btn-default" ng-click="fromDateCal($event)">
                            <i class = "icon-calendar calInputIcon"></i></button></span>
                       </div>
                </div>
                <div class = 'col-md-12 col-xs-12 customInput2'><span>To</span><br>
                    <div class = "input-group calendarInput calendarInput2">
                        <input type="text" 
                             ng-model="to" 
                             class="form-control"
                             id = "toDate"
                             name='toDate'
                             placeholder=""
                             datepicker-popup = '{{format}}'
                             min-date = "from"
                             is-open="datePicker.toDate" 
                             show-button-bar = false
                             datepicker-options = 'dateOptions'
                             required
                             ng-class = "{error: calender.toDate.$invalid && !calender.toDate.$pristine}">
                        <span class="input-group-btn"><button class="btn btn-default" ng-click="toDateCal($event)">
                            <i class = "icon-calendar calInputIcon"></i></button></span>
                       </div>
                </div>
                <input class = 'col-md-12 col-xs-12 btn btn-success btn-xs showCustomReportButton' 
                       type='button' 
                       value = 'Show Report'
                       ng-click = "getReport(from, to,facilityData.listOfFacility)"
                       ng-show = '!dateRangeError'
                       ng-disabled="calender.$invalid">
                <input class = 'col-md-12 col-xs-12 btn btn-warning btn-xs showCustomReportButton' 
                       type='button' 
                       value = 'In-valid Date Range'
                       ng-click = "getReport(from, to,facilityData.listOfFacility)"
                       ng-show = 'dateRangeError'>
             </div>
          </li>
         </ul>      
      </div>    
</div>

                                        
                                        