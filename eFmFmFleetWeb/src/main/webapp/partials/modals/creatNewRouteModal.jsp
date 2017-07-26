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

<div ng-include="'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div ng-include="'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="createNewRouteModal container-fluid">
	<div class="row">
		<div class="modal-header modal-header1 col-md-12">
			<div class="row">
				<div class="icon-pencil edsModal-icon col-md-1 floatLeft"></div>
				<div class="modalHeading col-md-8 floatLeft">Create New Route</div>
				<div class="col-md-2 floatRight pointer">
					<img src="images/portlet-remove-icon-white.png" class="floatRight"
						ng-click="cancel()">
				</div>
			</div>
		</div>
	</div>
	<div class="modal-body modalMainContent">
		<form name="createNewZone" class='createNewZone'>
			<div class="col-md-6 col-xs-6">
				<div>
					<label>Trip Type</label> <select ng-model="newRoute.tripType"
						class="form-control"
						ng-options="tripType.text for tripType in tripTypes track by tripType.value"
						ng-change="setTripType(newRoute.tripType)" required>
						<option value="">-- Select Trip Type --</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 col-xs-6">
				<div>
					<label>Trip Date</label>
					<div class="input-group calendarInput">
						<span class="input-group-btn">
							<button class="btn btn-default"
								ng-click="tripTimeDateCal($event)">
								<i class="icon-calendar calInputIcon"></i>
							</button>
						</span> <input type="text" ng-model="newRoute.tripTimeDate"
							class="form-control" placeholder="Trip Date"
							datepicker-popup='{{format}}' is-open="datePicker.tripTimeDate"
							show-button-bar=false datepicker-options='dateOptions'
							name='tripTimeDate' ng-change="getshiftTimeDatesData()" required
							readonly>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-xs-12">
				<div>
					<label>Shift Time</label><br> <input type="radio"
						class="floatLeft select_radio_assignCb radio_assignCab"
						ng-model="shiftTime" ng-disabled="isRadioDisable()"
						value="preDefineShiftTime"
						ng-change="selectShiftTimeRadio(shiftTime)" required> <select
						ng-model="newRoute.shiftTime"
						class="form-control floatLeft selectShiftTime_serviceMapping"
						ng-options="shiftTime.shiftTime for shiftTime in shiftsTime | orderBy:'shiftTime' track by shiftTime.shiftTime"
						ng-disabled="typeOfShiftTimeSelected != 'preDefineShiftTime'"
						required>
					</select>
				</div>
			</div>

			<div class="col-md-6 col-xs-12 createNewRouteTimepicker">
				<div class="timerDiv">
					<input type="radio" ng-model="shiftTime"
						class="timepickerRadioButton radio_assignCab floatLeft"
						value="ADHOCTime" ng-disabled="isRadioDisable()"
						ng-change="selectShiftTimeRadio2(shiftTime)" required>
					<timepicker ng-model="newRoute.createNewAdHocTime"
						ng-disabled="typeOfShiftTimeSelected != 'ADHOCTime'"
						hour-step="hstep" minute-step="mstep" show-meridian="ismeridian"
						readonly-input='true' class="timepicker2_empReq floatLeft">
					</timepicker>
				</div>
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
			<div class="col-md-12 col-xs-12">
				<label>Route Type</label> <br /> 
				<input type="radio"
					class="floatLeft routeTypeRadio" ng-model="routeType"
					value="normalRoute" ng-change="setRouteTypeNormal(routeType)"
					required> <span class="floatLeft routetypeSpan">Normal
					Route</span> 
					
					<input type="radio" class="floatLeft routeTypeRadio"
					ng-model="routeType" value="nodal"
					ng-change="setRouteTypeNodal(routeType)" required> <span
					class="floatLeft routetypeSpan">Nodal Point Route</span> 
					
					<input
					type="radio" class="floatLeft routeTypeRadio" ng-model="routeType"
					value="nodalAdhoc" ng-change="setRouteTypeAdhoc(routeType)"
					required> <span class="floatLeft">Adhoc Nodal Route</span>

			</div>
			<div class="col-md-12 col-xs-12">
				<div>
					<label>Route Name</label> <select ng-model="newRoute.zone"
						class="form-control"
						ng-options="allZoneData.routeName for allZoneData in allZonesData track by allZoneData.routeId"
						ng-change="setRouteName(newRoute.zone)" required>
						<option value="">SELECT ROUTE</option>
					</select>
				</div>
			</div>


			<div class="col-md-12 col-xs-12">
				<div>
					<label>Nodal Point Name</label> <select
						ng-model="newRoute.nodelPoint" class="form-control"
						ng-options="nodelPoint.nodalPointTitle for nodelPoint in allNodelPointData track by nodelPoint.nodalPointId"
						multiple ng-disabled="isNormalRoute || !routeSelected"
						ng-required="!isNormalRoute" required>
						<option value="">SELECT NODAL POINT</option>
					</select>
				</div>
			</div>

		</form>
	</div>
	<div class="modal-footer modalFooter createNewZone_modalFooter">
		<button type="button" class="btn btn-close floatRight"
			ng-click="cancel()">Cancel</button>
		<button type="button" class="btn btn-success floatRight"
			ng-click="createNew(newRoute,facilityData)" ng-disabled="createNewZone.$invalid">Create
			New Route</button>

	</div>
</div>