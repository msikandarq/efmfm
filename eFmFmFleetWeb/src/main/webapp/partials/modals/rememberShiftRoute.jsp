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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "exportModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-pencil edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Remember Shift Route</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>      
<div class="modal-body modalMainContent modalMainContentDownload">     
  <form name = "assignCabForm" class = 'createNewZone'>  
	   <div class = "col-md-6 col-xs-12"> 
           <div>
           <label>Trip Type</label>
               <select ng-model="download.tripType"
                       class="form-control" 
                       ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                       ng-change = "setTripType(download.tripType)"
                       required>
              </select>
           </div>
       </div> 
        <div class = 'col-md-6'>
            <label>Date</label>                    
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="openDownloadDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
              <input type="text" 
                     ng-model="download.date" 
                     class="form-control" 
                     placeholder = "Select Date" 
                     datepicker-popup = '{{format}}'
                     is-open="datePicker.openeddownloadDate" 
                     show-button-bar = false
                     show-weeks=false
                     datepicker-options = 'dateOptions'
                     readonly
                     name = 'date'>
               </div>
        </div> 
	   <div class = "col-md-6 col-xs-12"> 
           <div>
           <label>Select Shift Time</label><br>
               <input type="radio" 
                       class = "floatLeft select_radio_assignCb radio_assignCab"
                       ng-model="shiftTime"
                       ng-disabled = "isRadioDisable()"
                       value="preDefineShiftTime" 
                       ng-change = "selectShiftTimeRadio(shiftTime)"
                       >
               <select ng-model="download.shiftTime" 
                       class="form-control select_assignCab floatLeft" 
                       ng-options="shiftTime.shiftTime for shiftTime in shiftsTime | orderBy:'shiftTime' "
                       ng-disabled = "shiftTime != 'preDefineShiftTime'"
                       ng-required = "shiftTime == 'preDefineShiftTime'">
              </select>
           </div>
       </div>  
    <div class = "col-md-6 col-xs-12"> 
        <div class = "timerDiv">
            <input type="radio" 
                   ng-model="shiftTime" 
                   class = "timepickerRadioButton radio_assignCab floatLeft"
                   value="ADHOCTime" 
                   ng-disabled = "isRadioDisable()"
                   ng-change = "selectShiftTimeRadio2(shiftTime)"
                   >     
                <timepicker ng-model="download.createNewAdHocTime" 
                          ng-disabled = "typeOfShiftTimeSelected != 'ADHOCTime'"
                          hour-step="hstep" 
                          minute-step="mstep" 
                          show-meridian="ismeridian" 
                          readonly-input = 'true'
                          class = "timepicker2_empReq floatLeft">
               </timepicker>
        </div>
    </div>
    <div class = 'col-md-6 marginTopNeg20'>
      <a class = "hrefFile" href = "temp/test2.xlsx">{{download.fileList.reportName}}</a>
    </div>
    <div class = "col-md-12 col-xs-12" style="margin-top: -12px; margin-bottom: 34px;">
            <label>Select Facility</label>
            <am-multiselect class="input-lg"
                            multiple="true"
                            ms-selected ="{{facilityData.length}} Facility(s) Selected"
                            ng-model="facilityData"
                            ms-header="All Facility"
                            options="facility.branchId as facility.name for facility in facilityDetails"
                            change="setFacilityDetails(facilityData)">
            </am-multiselect>
    </div>

  </form>   
</div>
     <div class="modal-footer modalFooter createNewZone_modalFooter">  
           <button type="button" class="btn btn-close floatRight noMoreClick" ng-click = "cancel()">Cancel</button>
           <button type="button" 
                   class="btn btn-success floatRight noMoreClick" 
                   ng-click = "rememberShiftRoute(download,facilityData)"
                   ng-disabled="assignCabForm.$invalid">Remember Route</button>

     </div>
</div>


