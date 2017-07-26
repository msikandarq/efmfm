<!--
@date           12/01/2015
@Author         Saima Aziz
@Description    This page shows the reports in tabular format
@State          home.reports
@URL            /reports

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
12/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
--> 
<div class="reportsTemplate container-fluid" ng-if="isReportsActive">
	<div class="row">
		<div class="col-md-12 col-xs-12 heading1">
		  <span class="col-md-8 col-xs-8 vendorDashboardLabel">
            Reports
          </span>
          <span class="col-md-2 col-xs-2 reportHeaderNormalView" ng-show="multiFacility == 'N'">
          </span>
          <span class="col-md-2 col-xs-2 vendorDashboardMultiSelect" ng-show="multiFacility == 'Y'">
              <am-multiselect class="input-lg pull-right"
                                       multiple="true"
                                       ms-selected ="{{facilityData.listOfFacility.length}} Facility(s) Selected"
                                       ng-model="facilityData.listOfFacility"
                                       ms-header="All Facility"
                                       options="facility.branchId as facility.name for facility in facilityDetails"
                                       change="setFacilityDetails(facilityData.listOfFacility)"
                                       >
                     </am-multiselect>
          </span>
          <span class="col-md-2 col-xs-2 vendorDashboardGetFacilityButton" ng-show="multiFacility == 'Y'">
            <input type="button" class="btn btn-success" value="Submit" name="" ng-click="getFacilityDetails(facilityData)">
          </span>
			<div class="col-md-12 col-xs-12 mainTabDiv_reports">
				<!-- /*START OF WRAPPER1 = DRIVER*/ -->
				<div class="wrapper1" id="driverContent">

					<tabset class="tabset tripSheet_reports"> <!-- --------------------------------------------------------------- -->
					<tab ng-click="setDates('tripSheet'); select(0)" active="activeTabs[0]"> <tab-heading>Trip
					Sheet</tab-heading>
					<div class="tripSheetTabContent">
						<div class="searchDIVREPORT row">
							<div class="col-md-2">
								<input class="form-control" ng-model="efmfilter.filterTrips"
								expect_special_char
									placeholder="Filter Trip">
							</div>

							<div class="col-md-2">
								<select name="mySelect" class="form-control"
									ng-options="option.requestTypes for option in requestTypesDataTS.availableOptions track by option.value"
									ng-model="requestTypesDataTS.selectedOption"
									ng-change="setRequestTypeTS(requestTypesDataTS.selectedOption)">
									<!-- <option value="">Select Request Types1</option> -->
								</select>
							</div>

							<div class="col-md-2">
								<select name="mySelect" class="form-control"
									ng-options="option.viewType for option in viewTypeTripsheet.availableOptions track by option.value"
									ng-model="viewTypeTripsheet.selectedOption"
									ng-change="setviewTypeTripsheet(viewTypeTripsheet.selectedOption)">
								</select>
							</div>

							<div class="col-md-2">
								<input type="button" class="btn btn-success" ng-click="createOdometer()" value="Add/Modify Odometer Reading" name="">
							</div>
							<div class="col-md-4">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4 animated  slideInUp"
							ng-show="tripGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<div id='exportableTripSheet'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotTripResult">
							<table
								class="reportTable reportTable_km table table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan="20" class="tableHeadding_km repotTabMain">
											<div class='row'>
												<div class='col-md-4'>
													<!-- <h5 class="lengthReport">
														Trip Sheet Summary Report - <span class="badge">{{onTimeDataLength}}</span>
													</h5> -->
												</div>
												<div class='col-md-4'>
													<h5 class="lengthReport">Trip Sheet Report</h5>
													<div>Date : <span>{{searchFromDateTS
														| date : 'longDate'}} - {{searchToDateTS | date :
														'longDate'}}</span></div>
												</div>
												<div class='col-md-4'>
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="tripSheetExcel()">
														<i class="icon-download-alt"></i> <span
															class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>Trip Id</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												Trip Assign Time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												Trip Start Time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												Trip Completed Time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Shift Time</th>
										<th>Route Name</th>
										<th>Facility Name</th>
										<th>Vehicle No</th>
										<th>Driver Name</th>
										<th>Device Id</th>
										<th>Escort</th>
										<th>Type</th>
										<th>Emp Details</th>
										<th>Planned Distance (KM)</th>
										<th ng-show="branchCode == 'GNPTJP'">Suggestive Distance</th>
										<th>Odometer Distance</th>
										<th>GPS Traveled Distance (KM)</th>
										<th>Editable Distance (KM)</th>
										<th>Trip Status</th>
										<th>Route History</th>
									</tr>
								</thead>

								<tbody>
									<tr
										ng-repeat-start="date in tripSheetData | orderBy:dateAscDsc:reverse"
										class="visibleRow_reportTripSheet">
										<td colspan="18">{{date.tripAssignDate}}</td>
									</tr>
									<tr ng-repeat-end=""
										ng-repeat-start="trip in date.tripDetail | filter: efmfilter.filterTrips"
										class="secondaryloop_tripReport">
										<td class="col5">{{trip.routeId}}</td>
										<td class="col10">{{trip.tripAssignDate}}</td>
										<td class="col10">{{trip.tripStartDate}}</td>
										<td class="col10">{{trip.tripCompleteDate}}</td>
										<td class="col10">{{trip.shitTime}}</td>
										<td class="col10">{{trip.routeName}}</td>
										<td class="col10">{{trip.facilityName}}</td>
										<td class="col10">{{trip.vehicleNumber}}</td>
										<td class="col10">{{trip.driverName}}</td>
										<td class="col10">{{trip.deviceId}}</td>
										<td class="col5">{{trip.escortRequired}}</td>
										<td class="col5">{{trip.tripType}}</td>
										<td class="col10"><div
												ng-repeat="employee in trip.empDetails"
												class="pointer employeeDetailDiv"
												popover-template="partials/popovers/employeeDetails.jsp"
												popover-placement="right" popover-title="Employee Detail"
												popover-trigger="mouseenter" popover-append-to-body=true>
												<span><i class="icon-user employeeIcon_report"
													ng-class="{noShowTrip: employee.travelStatus == 'noShow'}"></i></span>
												<span>Id - {{employee.empId}}</span> <span>|</span> <span
													class="marginLeft10">Name - {{employee.empName}}</span>
											</div></td>
										<td class="col5">{{trip.plannedDistance}}</td>
										<td class="col5" ng-show="branchCode == 'GNPTJP'">{{trip.suggestiveDistance}}</td>
										<td>{{trip.odoDistance}}

										<button class='btn btn-success btn-xs buttonRadius0 tripSheetTravelDisSave'
												ng-click="viewOdometerTripSheet(trip)">
												<i class='icon-plus editPensilReportsTripSheet'></i>
											</button>

										</td>
										<td class="col5">{{trip.travelledDistance}}

											</td>
										<td class="col5" ng-show="!trip.isTravelDistanceEdit">{{trip.odoDistance}}
											<button class='btn btn-warning btn-xs buttonRadius0'
												ng-click="editTravelDistance(trip, $index)">
												<i class='icon-pencil editPensilReportsTripSheet'></i>
											</button>
										</td>
										<td class="col5" ng-show="trip.isTravelDistanceEdit"><input
											type='text' class="tripSheetTravelDisInput"
											ng-model="trip.editedDistance ">
											<button
												class='btn btn-success btn-xs buttonRadius0 tripSheetTravelDisSave'
												ng-click="saveTravelDistance(trip)">
												<i class='icon-save editPensilReportsTripSheet'></i>
											</button>
											<button class = "btn-xs btn btn-danger editCloseBtn buttonRadius0"
												ng-click="trip.editedDistance = Distance; cancelTravelDistance(trip)">
												<i class='icon-remove-sign'></i>
											</button></td>
										<td class="hidden"><input
											ng-model="trip.orginalTravelledDistance"></td>
										<td>
											Ontime
										</td>
										<td><input type="button" class="btn btn-xs btn-info"
											value="Route History" ng-click="routeHistory(trip, 'lg')">
										</td>
									</tr>
									<tr ng-repeat-end="" class="displayNone"></tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab><!--First Tab Ends--> <!-- On Time Report Tab -->
					<tab
						ng-click="setDates('onTime'); select(1)" active="activeTabs[1]"> <tab-heading>On
					Time</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">

							<div class="col-md-8 padding0">
							<div class="col-md-3">
								<select ng-model="searchOT.type" class="form-control"
									ng-options="OTTripType.text for OTTripType in OTTripTypes track by OTTripType.value"
									ng-change="setTripTypeOT(searchOT.type,facilityData.listOfFacility)">
								</select>
							</div>
							<div class="col-md-3">
								<select ng-model="searchOT.OTShift" class="form-control"
									ng-options="OTShiftTime.text for OTShiftTime in OTShiftTimes track by OTShiftTime.value"
									ng-change="setShiftOT(searchOT.OTShift)">
								</select>
							</div>
							<div class="col-md-3">
								<select ng-model="searchOT.OTVendors" class="form-control"
									ng-options="OTAllVendor.name for OTAllVendor in OTAllVendors track by OTAllVendor.Id"
									ng-change="setVendorOT(searchOT.OTVendors)">
									<!--                                     <option value="">-- Select Vendors --</option>-->
								</select>
							</div>

							<div class="col-md-3">
								<select name="mySelect" class="form-control"
									ng-options="option.requestTypes for option in requestTypesDataOT.availableOptions track by option.value"
									ng-model="requestTypesDataOT.selectedOption"
									ng-change="setRequestType()">
									<!-- <option value="">Select Request Types</option> -->
								</select>
							</div>
							</div>
							<div class="col-md-4 padding0">
							<div class="col-md-5">
								<select name="mySelect" class="form-control"
									ng-options="option.viewType for option in viewTypesOntime.availableOptions track by option.value"
									ng-model="viewTypesOntime.selectedOption"
									ng-change="setviewTypeOntime(viewTypesOntime.selectedOption)">
								</select>
							</div>
							<div class="col-md-7 padding0">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span>
								</div>
							</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-2 reportOntimeFilter" >
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterOTData'
									expect_special_char>
								<div class="col-md-2 reportTablePad" ></div>
							</div>
						</div>

						<div
							class="col-md-4 col-xs-12 col-md-offset-4 animate-show-loading"
							ng-show="OTGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<!-- Table-->
						<div id="exportableOnTime"
							class="col-md-12 col-xs-12 tableWrapper_report">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid"
								ng-show="gotOTResult">
								<thead class="tableHeading">
									<tr>
										<th colspan='11' class="repotTabMain">
											<div class="row">
												<div class='col-md-4'>
													<h5 class="lengthReport repotTabMainSum">
														On Time Summary Report - <span class="badge">{{onTimeDataLength}}</span>
													</h5>
												</div>
												<div class='col-md-4'>
													<h5 class="lengthReport">OT{{OT}} Report for {{headingVendorsOT}} |
													{{headingShiftOT}}</h5>
													<div>{{searchFromDateOT |
														date : 'longDate'}} - {{searchToDateOT | date :
														'longDate'}}</div>
												</div>
												<div class='col-md-4'>
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="onTimeExcel()">
														<i class="icon-download-alt"></i> <span
															class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th rowspan='2'>
											<div ng-show="dateShites" href="" class="filterSort pointer"
												ng-click="order('tripDates')">
												Shift Time<span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripDates'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
											<div ng-show="!dateShites" href="!searchOT.OTShift=0" class="filterSort pointer"
												ng-click="order('tripDates')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripDates'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th rowspan='2' ng-if='vendorName'>Vendor Name</th>
										<th rowspan='2'>Actual Users</th>
										<th rowspan='2'>Total Fleets of the Day</th>
                    <th rowspan='2'>Facility Name</th>
										<th colspan='7' class='monthlyDailyCol monthlyDailyOTReport'>Monthly and Daily
											OT{{OT}}</th>
									</tr>
									<tr>
										<th>{{OTresultTripType}} Pax</th>
										<th>{{OTresultTripType}} Trips</th>
										<th>OT{{OT}} Trips {{bufferTime}}</th>
										<th>OT{{OT}} in %</th>
										<th ng-if="OTresultTripType=='Pickup'">Delay Trips</th>
										<th class="beyondLoginTime">Beyond
											Login Time</th>
										<th>No Show</th>
									</tr>
								</thead>
								<tbody>
									<tr class="reportResultTable tabletdCenter animate-repeat"
										ng-repeat="OTA in onTimeData |  orderBy:dateAscDsc:reverse | filter: efmfilter.filterOTData">
										<td>{{OTA.tripDates}}</td>
										<td ng-if='vendorName'>{{OTA.vendorName}}</td>
										<td>{{OTA.totalAllocatedEmployeesCount}}</td>
										<td>{{OTA.totalUsedVehicles}}</td>
										<td>{{OTA.facilityName}}</td>

										<td>{{OTA.totalEmployeesPickedDropCount}}</td>
										<td>{{OTA.totalTrips}}</td>

										<td><span>{{OTA.onTimeTrips}}</span> <span
											class="floatRight"><input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="onTimeOTATrips(OTA, $index, searchOT.type)" name=""
												ng-disabled="OTA.onTimeTrips == 0"></span></td>
										<td>{{OTA.delayTripsPercentage}}</td>

										<td ng-if="OTresultTripType=='Pickup'">{{OTA.totalDelayTrips}}
											<span class="floatRight"><input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="onTimeDelayTrips(OTA, $index, searchOT.type)" name=""
												ng-disabled="OTA.totalDelayTrips == 0"></span>
										</td>
										<td class='highlightedColumn_Report'>{{OTA.totalDelayTripsBeyondLogin}}
											<span class="floatRight"><input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="onTimeBeyondLoginTime(OTA, $index, searchOT.type)" name=""
												ng-disabled="OTA.totalDelayTripsBeyondLogin == 0"></span>
										</td>
										<td>{{OTA.totalEmployeesNoShowCount}} <span
											class="floatRight"><input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="onTimeNoShow(OTA, $index, searchOT.type)" name=""
												ng-disabled="OTA.totalEmployeesNoShowCount == 0"></span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of On Time Report Tab --> <!-- Seat Utilization Report Tab -->
					<tab ng-click="setDates('seatUtil'); select(2)" active="activeTabs[2]"> <tab-heading>Seat
					Utilization</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterSeatUtilizationData'
									expect_special_char>
							</div>
							<div class="col-md-2">
								<select ng-model="searchSU.type" class="form-control"
									ng-options="SUTripType.text for SUTripType in SUTripTypes track by SUTripType.value"
									ng-change="setTripTypeSU(searchSU.type,facilityData.listOfFacility)">
								</select>
							</div>

							<div class="col-md-2">
								<select ng-model="searchSU.SUShift" class="form-control"
									ng-options="SUShiftTime.text for SUShiftTime in SUShiftTimes track by SUShiftTime.value"
									ng-change="setShiftSU(searchSU.SUShift)"
									ng-disabled='searchSU.SUOption.text != "By Shift"'>
								</select>
							</div>
							<div class="col-md-2">
								<select name="mySelect" class="form-control"
									ng-options="option.requestTypes for option in requestTypesDataSU.availableOptions track by option.value"
									ng-model="requestTypesDataSU.selectedOption"
									ng-change="setRequestType(requestTypesDataSU.selectedOption)">
									<!-- <option value="">Select Request Types</option> -->
								</select>
							</div>
							<div class="col-md-4">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>
						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="SUGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<!-- Table-->
						<div class="col-md-12 col-xs-12 tableWrapper_report">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid"
								ng-show="gotSUResult">
								<thead class="tableHeading">
									<tr>
										<th colspan='10' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Seat Utilization Summary Report - <span class="badge">{{seatUtilDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">{{SUresultTripType}} - {{SUReportTitle}}</h5>
													<div>{{fromDate
														| date : 'longDate'}} - {{toDate | date : 'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="saveInExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th rowspan='2'>
											<div  ng-show="dateShift" href="" class="filterSort pointer"
												ng-click="order('tripDates')">
												Shift Time<span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripDates'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
											<div  ng-show="!dateShift" href=""
											class="filterSort pointer"
												ng-click="order('tripDates')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripDates'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>

										</th>
										<th rowspan='2'>Number Of Trips</th>
										<th rowspan='2'>Facility Name</th>
										<th rowspan='2'>Total Opportunity</th>
										<th colspan='3' class='monthlyDailyCol'
											style="background-color: #eb9316;">PICKUP Details</th>
									</tr>
									<tr>
										<th>{{SUresultTripType}} Pax</th>
										<th class="beyondLoginTime">{{SUresultTripType}}
											SU %</th>

									</tr>
								</thead>
								<tbody>
									<tr class="reportResultTable tabletdCenter"
										ng-repeat="SU in seatUtilData | filter:efmfilter.filterSeatUtilizationData | orderBy:dateAscDsc:reverse ">
										<td>{{SU.tripDates}}</td>
										<td>{{SU.totalUsedVehicles}} <span class="floatRight"><input
												type="button" class="btn btn-xs btn-primary" value="View"
												ng-click="seatUtilNoOfVehicle(SU, $index)" name=""
												ng-disabled="SU.totalUsedVehicles == 0"></span>
										</td>
										<td>{{SU.facilityName}}</td>
										<td>{{SU.totalVehicleCapacity}}</td>
										<td>{{SU.totalEmployeesPickedDropCount}} <span
											class="floatRight"><input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="seatUtilPickupPax(SU, $index)" name=""
												ng-disabled="SU.totalEmployeesPickedDropCount == 0"></span>
										</td>
										<td class='highlightedColumn_Report'>{{SU.utilizedSeatPercentage}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Seat Utilization Report Tab --> <!-- No Show Report Tab -->
					<tab ng-click="setDates('noShow'); select(3)" active="activeTabs[3]"> <tab-heading>No
					Show</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<select ng-model="searchNS.type" class="form-control"
									ng-options="NSTripType.text for NSTripType in NSTripTypes track by NSTripType.value"
									ng-change="setTripTypeNS(searchNS.type,facilityData.listOfFacility)">
								</select>
							</div>

							<div class="col-md-2">
								<select ng-model="searchNS.NSShift" class="form-control"
									ng-options="NSShiftTime.text for NSShiftTime in NSShiftTimes track by NSShiftTime.value"
									ng-change="setShiftNS(searchSU.SUShift)"
									ng-disabled='searchNS.employeeId'>
								</select>
							</div>

							<div class="col-md-2">
								<input ng-model="searchNS.employeeId" class="form-control"
									placeholder='Employee Id'
									ng-minlength="0"
									ng-maxlength="20"
									maxlength="20"
									 expect_special_char>
							</div>
							<div class="col-md-2">
								<select name="mySelect" class="form-control"
									ng-options="option.requestTypes for option in requestTypesDataNS.availableOptions track by option.value"
									ng-model="requestTypesDataNS.selectedOption"
									ng-change="setRequestTypeNS(requestTypesDataNS.selectedOption)">
									<!-- <option value="">Select Request Types</option> -->
								</select>
							</div>
							<div class="col-md-4">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-2 reportOntimeFilter" >
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterNoShowData'
									expect_special_char>
							</div>
							<div class="col-md-10 reportTablePad" ></div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="NSGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<!-- Table-->
						<div id="exportableNoShow"
							class="col-md-12 col-xs-12 tableWrapper_report">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid"
								ng-show="gotNSResult">
								<thead class="tableHeading">
									<tr>
										<th colspan='5' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														No Show Summary Report - <span class="badge">{{noShowReportDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">{{NSResultTripType}} - {{NSReportTitle}}</h5> <div>{{searchFromDatesNS
														| date : 'longDate'}} - {{searchToDatesNS | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="saveInExcel()">
														<i class="icon-download-alt"></i> <span
															class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>

										</th>
									</tr>
									<tr>

										<th
											ng-if='NSresultShift == "By Shifts" || NSresultShift == "All Shifts"'>
											<div href="" class="filterSort pointer"
												ng-click="order('tripDates')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripDates'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th
											ng-hide='NSresultShift == "By Shifts" || NSresultShift == "All Shifts"'>
											<div href="" class="filterSort pointer"
												ng-click="order('tripDates')">
												Shift Time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripDates'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th
											ng-if='NSresultShift == "By Shifts" && !searchNSByEmployeeId'>Shift
											Time</th>
										<!-- <th ng-if='NSresultShift == "By Shifts" || NSresultShift == "All Shifts" || searchNSByEmployeeId' ><span href=""
											class="filterSort pointer" ng-click="order('tripDates')">Date</span>
											<span class="sortorder pointer"
											ng-show="dateAscDsc === 'tripDates'"
											ng-class="{reverse:reverse}"></span></th> -->

										<th ng-if='searchNSByEmployeeId'>Shift Time</th>
										<th>{{NStotalShift}}</th>
										<th>{{NSTripType}}</th>
										<th>Facility Name</th>
										<th ng-if=noShowResult
											!= '0' class="beyondLoginTime">No-Show</th>
									</tr>
								</thead>
								<tbody>
									<tr class="reportResultTable tabletdCenter"
										ng-repeat="NS in noShowReportData | filter:efmfilter.filterNoShowData | orderBy:dateAscDsc:reverse">
										<td ng-if='NSresultShift == "By Shifts"'>{{NS.actualTravelledDate}}</td>
										<td
											ng-if='NSresultShift == "All Shifts" && !searchNSByEmployeeId'>{{NS.tripDates}}
										</td>
										<td
											ng-if='NSresultShift == "By Shifts" && !searchNSByEmployeeId'>{{NS.tripDates}}</td>
										<td ng-if='searchNSByEmployeeId'>{{NS.tripDates}}</td>
										<td ng-hide='NSresultShift == "By Shifts" || NSresultShift == "All Shifts"'>{{NS.tripDates}}</td>
										<td ng-if='searchNSByEmployeeId'>{{NS.shiftTime}}</td>

										<td>{{NS.totalUsedVehicles}} <!-- <span class="floatRight"> -->
										<span ng-if='NSresultShift == "All Shifts"' class="floatRight">
										<input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="noShowPickupTrips(NS, $index)" name=""
												ng-hide="searchNS.NSShift == 'All Shift' || searchNSByEmployeeId"
												ng-disabled="NS.totalUsedVehicles == 0"></span>
										</td>
										<td>{{NS.totalEmployeesPickedDropCount}} <span ng-if='NSresultShift == "By Shifts" || NSresultShift == "All Shifts"' class="floatRight"> <input type="button"
												class="btn btn-xs btn-primary" value="View"
												ng-click="noShowPickupPax(NS, $index)" name=""
												ng-hide="searchNS.NSShift.value == '1' || searchNSByEmployeeId"
												ng-disabled="NS.totalEmployeesPickedDropCount == 0"></span>
										</td>
										<td>{{NS.facilityName}}</td>
										<td ng-if=noShowResult != '0' class='highlightedColumn_Report'>{{NS.totalEmployeesNoShowCount}}
											<!--  <input type="button" class="btn btn-xs btn-primary" value="View" ng-click="noShowView(NS, $index)" name=""> -->
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Seat Utilization Report Tab --> <!--  Fifth Tab --> <tab
						ng-click="setDates('kmReport'); select(4)" active="activeTabs[4]"> <tab-heading>Distance</tab-heading>
					<div class="kmTabContent" id="pdfKM">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' class='form-control' placeholder='Filter'
									ng-model='efmfilter.filterKm'
									expect_special_char>
							</div>

							<div class="col-md-2">
								<select class="marginBottom10 form-control"
									ng-model="reportKM.reportType"
									ng-options="reportType.text for reportType in reportTypes track by reportType.value"
									ng-change="getVendorOrVehicle(reportKM.reportType)" required>
								</select>
							</div>

							<div class="col-md-2">
								<select ng-model="reportKM.distanceShift" class="form-control"
									ng-options="DistanceShiftTime.text for DistanceShiftTime in DistanceShiftTimes track by DistanceShiftTime.value"
									ng-change="setShiftDistance(searchDistance.distanceShift)">
								</select>
							</div>

							<div class="col-md-2">
								<select
									class="select_reports_VSelection marginBottom10 form-control"
									ng-model="reportKM.VSelection"
									ng-options="vendorVehicle.name for vendorVehicle in vendorVehicles_KMReport track by vendorVehicle.Id"
									ng-change="setvendorVehicleD(reportKM.VSelection)" required>
									<!--                        <option value="">-- Select --</option>-->
								</select>
							</div>
							<div class="col-md-4">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="distanceGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<!--ByVendor Div -->
						<div class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotKMResult && reportTypeSelected == 'vendor'">
							<table
								class="reportTable reportTable_km table table-responsive container-fluid"
								ng-show="reportsSum.length>0">
								<thead class="tableHeading">
									<tr>
										<th colspan="8" class="tableHeadding_km repotTabMain">
											<div class="row">
												<div class="col-md-4 ">
													<h5 class="lengthReport repotTabMainSum">
														Distance Summary Report - <span class="badge">{{reportsSumLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
												<h5 class="lengthReport">
													{{headingReportTypeKM}} Report for {{reportSelectionKM}}</h5>
													<div>Date
													: <span>{{searchFromDateKM | date : 'longDate'}} -
														{{searchToDateKM | date : 'longDate'}}</span></div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="saveInExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('date')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'date'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th ng-if="isShiftKM">Shift Time</th>
										<th>Vendor Name</th>
										<th ng-if="vehicleNumberField!= '0'" >Vehicle Number</th>
										<th>Type of Vehicle</th>
										<!-- <th>Total Opportunity</th> -->
										<th class="beyondLoginTime">Utilized
											KMs</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr class="reportSum tabletdCenter"
										ng-repeat="reportSum_vendor in reportsSum | filter:efmfilter.filterKm |  orderBy:dateAscDsc:reverse">
										<td>{{reportSum_vendor.date}}</td>
										<td ng-if="isShiftKM">{{reportSum_vendor.shiftTime}}</td>
										<td>{{reportSum_vendor.vendorName}}</td>
										<td ng-if="vehicleNumberField!= '0'">{{reportSum_vendor.vehicleNumber}}</td>
										<td>{{reportSum_vendor.vehicleType}}</td>
										<!-- <td>{{reportSum_vendor.totalApportunity}}</td> -->
										<td class='highlightedColumn_Report'>{{reportSum_vendor.travelledDistance}}</td>
										<td>{{reportSum_vendor.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>
						<!--ByVehicle Div -->
						<div class="col-md-12 col-xs-12"
							ng-show="gotKMResult && reportTypeSelected == 'vehicle'">
							<table
								class="reportTable reportTable_km table table-responsive container-fluid"
								ng-show="reportsSum.length>0">
								<thead class="tableHeading">
									<tr>
										<th colspan="8" class="tableHeadding_km repotTabMain">
											<div class="row">
												<div class="col-md-4"></div>
												<div class="col-md-4">
													{{headingReportTypeKM}} Report for {{reportSelectionKM}} <br>Date
													: <span>{{searchFromDateKM | date : 'longDate'}} -
														{{searchToDateKM | date : 'longDate'}}</span>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="saveInExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>Date</th>
										<th ng-if="isShiftKM">Shift Time</th>
										<th>Vendor</th>
										<th>Vehicle Number</th>
										<th>Vehicle Type</th>
										<!-- <th>Total Opportunity</th> -->
										<th class="beyondLoginTime">Utilized
											KM's</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr class='tabletdCenter'
										ng-repeat="reportSum_vehicle in reportsSum | filter:efmfilter.filterKm">
										<td>{{reportSum_vehicle.date}}</td>
										<td ng-if="isShiftKM">{{reportSum_vehicle.shiftTime}}</td>
										<td>{{reportSum_vehicle.vendorName}}</td>
										<td>{{reportSum_vehicle.vehicleNumber}}</td>
										<td>{{reportSum_vehicle.vehicleType}}</td>
										<!-- <td>{{reportSum_vehicle.totalApportunity}}</td> -->
										<td class='highlightedColumn_Report'>{{reportSum_vehicle.travelledDistance}}</td>
										<td>{{reportSum_vehicle.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>

						<!-- Table-->
						<div class="col-md-12 col-xs-12 tableKmDetail tableWrapper_report">
							<div class="floatRight reportButtons"></div>
							<table
								class="reportTable reportTable_kmDetail table table-responsive container-fluid"
								ng-show="viewDetail">
								<thead class="tableHeading">
									<tr class="tableHeadding_km">
									</tr>
									<tr>
										<th>Vehicle Id</th>
										<th>Vehicle Number</th>
										<th>Travelled Date</th>
										<th>Travelled Distance</th>
										<th>Planned Distance</th>
										<th>Shift Time</th>
										<th>Vendor Name</th>
										<th>Trip Type</th>
										<th>Facility Type</th>
									</tr>
								</thead>
								<tbody>
									<tr class='tabletdCenter'
										ng-repeat="report in reportsKMData | filter:searchKm">
										<td>{{report.vehicleId}}</td>
										<td>{{report.vehicleNum}}</td>
										<td>{{report.travelledDate}}</td>
										<td>{{report.travelledDistance}}</td>
										<td>{{report.plannedDistance}}</td>
										<td>{{report.shiftTime}}</td>
										<td>{{report.vendorName}}</td>
										<td>{{report.tripType}}</td>
										<td>{{report.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Fifth Tab --> <!-- Ninth Tab --> <tab
						ng-click="setDates('SMSReport'); select(5)" active="activeTabs[5]"> <tab-heading>Daily
					SMS</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">

							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterSMSShow'
									expect_special_char>
							</div>
							<div class="col-md-4"></div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="SMSGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<!-- Table-->
						<div class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotSMSResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan="12" class="tableHeadding_km repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Daily SMS Summary Report - <span class="badge">{{reportsSMSDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">SMS Report</h5><div>Date : <span>{{searchFromDateSMS |
														date : 'longDate'}} - {{searchToDateSMS | date :
														'longDate'}}</span></div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="saveInExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('travelledDate')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'travelledDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Employee Id</th>
										<th>Employee Number</th>
										<th>Shift Time</th>
										<th>Route Name</th>
										<th>Trip Type</th>
										<th>Allocation Msg</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('travelledDate')">
												ETA Msg <span class="sortorder pointer"
													ng-show="dateAscDsc === 'travelledDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('travelledDate')">
												Cab Delay Msg <span class="sortorder pointer"
													ng-show="dateAscDsc === 'travelledDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('travelledDate')">
												Reached Msg <span class="sortorder pointer"
													ng-show="dateAscDsc === 'travelledDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('travelledDate')">
												No Show Msg <span class="sortorder pointer"
													ng-show="dateAscDsc === 'travelledDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr class='tabletdCenter'
										ng-repeat="report in reportsSMSData | orderBy:dateAscDsc:reverse| filter:efmfilter.filterSMSShow">
										<td class="col-md-1">{{report.travelledDate}}</td>
										<td class="col-md-1">{{report.employeeId}}</td>
										<td class="col-md-1">{{report.employeeNumber}}</td>
										<td class="col-md-1">{{report.shiftTime}}</td>
										<td class="col-md-1">{{report.routeName}}</td>
										<td class="col-md-1">{{report.tripType}}</td>
										<td class="col-md-1">{{report.allocationMsgDeliveryDate}}</td>
										<td class="col-md-1">{{report.eat15MinuteMsgDeliveryDate}}</td>
										<td class="col-md-1">{{report.cabDelayMsgDeliveryDate}}</td>
										<td class="col-md-1">{{report.reachedMsgDeliveryDate}}</td>
										<td class="col-md-1">{{report.noShowMsgDeliveryDate}}</td>
										<td class="col-md-1">{{report.facilityName}}</td>

									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Nineth Tab --> <!-- Tenth Tab --> <tab
						ng-click="setDates('escortReport'); select(6)" active="activeTabs[6]"> <tab-heading>Escort</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterEscort'
									expect_special_char>
							</div>
							<div class="col-md-4"></div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="escortGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id="exportableEscortReport"
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotEscortResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='10' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Escort Summary Report - <span class="badge">{{reportEscortDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Escort Report</h5><div>{{searchFromDateEscort
														| date : 'longDate'}} - {{searchToDateEscort | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="escortExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Vehicle Number</th>
										<th>Driver Name</th>
										<th>Employee Name/Id</th>
										<th>ShiftTime</th>
										<th>Escort id</th>
										<th>Escort Name</th>
										<th>Time of Drop/Pickup</th>
										<th>Route</th>
										<th>Trip Type</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr
										ng-repeat="report in reportEscortData | filter:efmfilter.filterEscort | orderBy:dateAscDsc:reverse">
										<td class="col-md-1">{{report.tripAssignDate}}</td>
										<td class="col-md-1">{{report.vehicleNumber}}</td>
										<td class="col-md-1">{{report.driverName}}</td>
										<td class="col-md-1">{{report.employeeName}}/{{report.employeeId}}</td>
										<td class="col-md-1">{{report.shiftTime}}</td>
										<td class="col-md-1">{{report.escortId}}</td>
										<td class="col-md-2">{{report.escortName}}</td>
										<td class="col-md-1">{{report.pickOrDropTime}}</td>
										<td class="col-md-1">{{report.routeName}}</td>
										<td class="col-md-1">{{report.tripType}}</td>
										<td class="col-md-1">{{report.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Tenth Tab --> <!-- Eleventh Tab --> 
					<tab ng-click="getGeocodedDistanceVariation('geocodedDistanceVariation'); setDates('geocodedDistanceVariation'); select(18)" active="activeTabs[18]">
                                <tab-heading>Geocoded Distance Variation</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">Geocoded Distance Variation Summary - <span class="badge">{{getGeocodedDistanceVariations.length}} / {{getGeocodedDistanceVariations[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!--      <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="getGeocodedDistanceVariationFilter" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
						                               <option value="">Select Search Type</option>
						                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData.listOfFacility)" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
				                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
				                                           ng-click="searchEmployees(search,facilityData.listOfFacility)">
				                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="geocodedDistanceVariationsExcel(getGeocodedDistanceVariations)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                    <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!getYetToAppCountDataShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row divTableHeader" style="margin: -15px; padding: 0; font-size: 14px; font-weight: 900;" >
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-3 col-sm-3">Employee Name</div>
                                        <div class="col-md-3 col-sm-3">Mobile Number</div>
                                        <div class="col-md-3 col-sm-3">Employee Id</div>
                                        <div class="col-md-3 col-sm-3">Email Id</div>
                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-3 col-sm-3">User Name</div>
                                        <div class="col-md-3 col-sm-3">User Id</div>
                                        <div class="col-md-3 col-sm-3">Employee Address</div>
                                        <div class="col-md-3 col-sm-3">Distance Variation</div>
                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                    	<div class="col-md-4 col-sm-4">Latitude/Longitude</div>
                                       	<div class="col-md-4 col-sm-4">Geo Coded Address</div>
                                       	<div class="col-md-4 col-sm-4">Home GeoCode Points</div>
                                    </div>
                            	</div>
                                        <div ng-show="filterGeoCodeDistance.length > 0"  class="constrainedNonAppCount row pointer">
                                            <div class="overflowXauto" infinite-scroll="getGeocodedDistanceVariation('geocodedDistanceVariation')" infinite-scroll-container='".constrainedNonAppCount"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in filterGeoCodeDistance =(getGeocodedDistanceVariations | filter:getGeocodedDistanceVariationFilter)">

                                                <div class="col-md-4 col-sm-4 paddingTBLR">
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.employeeName}}</div>
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.mobileNumber}}</div>
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.employeeId}}</div>
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.emailId}}</div>
			                                    </div>
			                                    <div class="col-md-4 col-sm-4 paddingTBLR">
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.userName}}</div>
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.userId}}</div>
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.employeeAddress}}</div>
			                                        <div class="col-md-3 col-sm-3 divTableData textBreak">{{Value.distanceVariation}}</div>
			                                    </div>
			                                    <div class="col-md-4 col-sm-4 paddingTBLR">
			                                    	<div class="col-md-4 col-sm-4 divTableData textBreak">{{Value.employeeLatiLongi}}</div>
			                                       	<div class="col-md-4 col-sm-4 divTableData textBreak">{{Value.geoCodedAddress}}</div>
			                                       	<div class="col-md-4 col-sm-4 divTableData textBreak">{{Value.homeGeoCodePoints}}</div>
			                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <br/>
                                         <div class="row" ng-show="filterGeoCodeDistance.length == 0">
                                            <div class="noData">There is No Data for Geocoded distance variation details</div>
                                        </div>
                                    </div>
                                   
                                </div>
                            </tab>
					<tab
						ng-click="setDates('vehicleDriverAttendence'); select(7)" active="activeTabs[7]"> <tab-heading>Vehicle
					& Driver Attendance</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">

							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterVDAttendance'
									expect_special_char>
							</div>
							<div class="col-md-4"></div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="vdattendanceGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportTableVDAttendance'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotVDAttendanceResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='9' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Vehicle & Driver Attendance Summary Report - <span
															class="badge">{{reportsVDAttendanceDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Vehicle And Driver Attendance</h5> <div>{{searchFromDatesVDA
														| date : 'longDate'}} - {{searchToDatesVDA | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="vehicleDriverAttendanceExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('checkInDate')">
												CheckInDate <span class="sortorder pointer"
													ng-show="dateAscDsc === 'checkInDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('checkInDate')">
												CheckOutDate <span class="sortorder pointer"
													ng-show="dateAscDsc === 'checkInDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>AdminMailTriggerStatus</th>
										<th>SupervisorMailTriggerStatus</th>
										<th>Vendor Name</th>
										<th>Vehicle Number</th>
										<th>Driver Id</th>
										<th>Driver Name</th>
										<th>Status</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr
										ng-repeat="report in reportsVDAttendanceData | filter:efmfilter.filterVDAttendance | orderBy:dateAscDsc:reverse">
										<td class="col-md-2">{{report.checkInDate}}</td>
										<td class="col-md-2">{{report.checkOutDate}}</td>
										<td class="col-md-2">{{report.adminMailTriggeredTime}}</td>
										<td class="col-md-2">{{report.supervisorMailTriggerStatus}}</td>
										<td class="col-md-3">{{report.vendorName}}</td>
										<td class="col-md-2">{{report.vehicleNumber}}</td>
										<td class="col-md-1">{{report.driverId}}</td>
										<td class="col-md-3">{{report.driverName}}</td>
										<td class="col-md-1">{{report.status}}</td>
										<td class="col-md-1">{{report.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Tab --> <!-- Driver working hours Tab --> <tab
						ng-click="setDates('driverWorkingHours'); select(8)" active="activeTabs[8]"> <tab-heading>Driver
					Working Hours</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">

							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterdriverWH'
									expect_special_char>
							</div>
							<div class="col-md-4"></div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="workingGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportTableDriverWH'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotDriverWHResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='10' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Driver Working Hours Summary Report - <span class="badge">{{reportsDriverWHDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Driver Working Hours</h5> <div>{{searchFromDatesDWH
														| date : 'longDate'}} - {{searchToDatesDWH | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="driverWorkingHoursExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>

												</div>
											</div>
										</th>

									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('date')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'date'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Vendor Name</th>
										<th>Vehicle Number</th>
										<th>Driver Id</th>
										<th>Driver Name</th>
										<th>Login time</th>
										<th>Logout time</th>
										<th>Remarks</th>
										<th>Total Hours</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr
										ng-repeat="report in reportsDriverWHData | filter:efmfilter.filterdriverWH | orderBy:dateAscDsc:reverse">
										<td class="col-md-1">{{report.date}}</td>
										<td class="col-md-2">{{report.vendorName}}</td>
										<td class="col-md-1">{{report.vehicleNumber}}</td>
										<td>{{report.driverId}}</td>
										<td class="col-md-2">{{report.driverName}}</td>
										<td class="col-md-2">{{report.loginTime}}</td>
										<td class="col-md-2">{{report.logOutTime}}</td>
										<td class="col-md-2">{{report.remarks}}</td>
										<td class="col-md-1">{{report.totalWorkingHours}}</td>
										<td class="col-md-1">{{report.facilityName}}</td>

									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Tab --> <!-- Driver Driving hours Tab -->
					<tab
						ng-click="setDates('driverDrivingHours'); select(9)" active="activeTabs[9]"> <tab-heading>Driver
					Driving Hours</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">

							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterdriverDH'
									expect_special_char>
							</div>
							<div class="col-md-4"></div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="drivingHoursgGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportTableDriverDH'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotDriverDHResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid table-bordered">
								<thead class="tableHeading">
									<tr>
										<th colspan='13' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Driver Driving Hours Summary Report - <span class="badge">{{reportsDriverDHDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Driver Driving Hours</h5> <div>{{searchFromDatesDDH
														| date : 'longDate'}} - {{searchToDatesDDH | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="driverDrivingHoursExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>

									</tr>
									<tr>
										<th>

											<div href="" class="filterSort pointer"
												ng-click="order('tripsDetails[0].tripAssignedDate')">
												Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripsDetails[0].tripAssignedDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Vendor Name</th>
										<th>Facility Name</th>
										<th>Vehicle Number</th>
										<th>Driver Id</th>
										<th>Driver Name</th>
										<th>Trip Type</th>
										<th>Route Name</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripsDetails[0].tripAssignedDate')">
												Trip start time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripsDetails[0].tripAssignedDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripsDetails[0].tripAssignedDate')">
												Trip End time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripsDetails[0].tripAssignedDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Driving hrs/trip</th>
										<th>Edit/Save</th>
										<th>Total driving hrs</th>
									</tr>
								</thead>

								<tbody
									ng-repeat="report in reportsDriverDHData | filter:efmfilter.filterdriverDH | orderBy:dateAscDsc:reverse" >
									<tr>
									<!-- <tr ng-class="{nonModifiedFlg: report.tripsDetails[0].driverDrivingHoursPerTripFlg == 'M' , modifiedFlg: report.tripsDetails[0].driverDrivingHoursPerTripFlg == 'N'}"> -->
										<td class="col-md-1">{{report.tripsDetails[0].tripAssignedDate}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].vendorName}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].facilityName}}</td>
										<td class="col-md-2">{{report.tripsDetails[0].vehicleNumber}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].driverId}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].driverName}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].tripType}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].routeName}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].tripStartDate}}</td>
										<td class="col-md-1">{{report.tripsDetails[0].tripCompleteDate}}</td>
										<!-- <td class="col-md-1">{{report.tripsDetails[0].driverDrivingHoursPerTrip}}</td> -->
										<td class = 'col-md-3' ng-show = '!report.editDrivingHourIsClicked'>{{report.tripsDetails[0].driverDrivingHoursPerTrip}}</td>
                                   		<td  class = 'col-md-3' ng-show = 'report.editDrivingHourIsClicked'>
                                          <timepicker ng-model="driverDrivingHoursPerTrips"
                                                      hour-step="hstep5"
                                                      minute-step="mstep5"
                                                      show-meridian="ismeridian5"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                   		 </td>
                                   		 <td class='col-md-3 col-sm-3'>
                                             <button type = 'button'
                                                     class = 'btn btn-warning btn-sm'
                                                     ng-show = "!report.editDrivingHourIsClicked"
                                                     ng-click = "editDrivingHours(report.tripsDetails[0].driverDrivingHoursPerTrip, report, $index)"><i class="icon-edit-sign"></i></button>
                                              <div class="col-sm-12">
                                              <div class="col-md-6"><button type = 'button'
                                                     class = 'btn btn-success btn-sm'
                                                     ng-show = "report.editDrivingHourIsClicked"
                                                     ng-click = "updateDrivingHours(report, $index, driverDrivingHoursPerTrips)"><i class="icon-save"></i></button></div>

                                              <div class="col-md-6"><button type = 'button'
                                                     class = 'btn btn-warning btn-sm cancelBtn'
                                                     ng-show = "report.editDrivingHourIsClicked"
                                                     ng-click = "cancelDrivingHours(report, $index)"><i class = "icon-remove-sign"></i></button></div></div>
                                          </td>
										<td rowspan="{{report.tripsDetails.length}}" class="col-md-1">{{report.totalDrivingHours}}</td>

									</tr>
									<tr
										ng-repeat="dh in report.tripsDetails.slice(1,report.tripsDetails.length) | filter:efmfilter.filterdriverDH | orderBy:dateAscDsc:reverse">
										<td class="col-md-1">{{dh.tripAssignedDate}}</td>
										<td class="col-md-1">{{dh.vendorName}}</td>
										<td class="col-md-1">{{dh.facilityName}}</td>
										<td class="col-md-2">{{dh.vehicleNumber}}</td>
										<td class="col-md-1">{{dh.driverId}}</td>
										<td class="col-md-1">{{dh.driverName}}</td>
										<td class="col-md-1">{{dh.tripType}}</td>
										<td class="col-md-1">{{dh.routeName}}</td>
										<td class="col-md-1">{{dh.tripStartDate}}</td>
										<td class="col-md-1">{{dh.tripCompleteDate}}</td>
										<td class="col-md-1" ng-show = "!dh.editDrivingHourIsClick">{{dh.driverDrivingHoursPerTrip}}</td>
										<td  class = 'col-md-3' ng-show = 'dh.editDrivingHourIsClick'>
                                          <timepicker ng-model="driverDrivingHoursTrip"
                                                      hour-step="hstep5"
                                                      minute-step="mstep5"
                                                      show-meridian="ismeridian5"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                   		 </td>
										 <td class='col-md-3 col-sm-3'>
                                             <button type = 'button'
                                                     class = 'btn btn-warning btn-sm'
                                                     ng-show = "!dh.editDrivingHourIsClick"
                                                     ng-click = "editDrivingHour(dh.driverDrivingHoursPerTrip, dh, $index)"><i class="icon-edit-sign"></i></button>
                                              <div class="col-sm-12">
                                              <div class="col-md-6"><button type = 'button'
                                                     class = 'btn btn-success btn-sm'
                                                     ng-show = "dh.editDrivingHourIsClick"
                                                     ng-click = "updateDrivingHour(dh, driverDrivingHoursTrip, $index)"><i class="icon-save"></i></button></div>

                                              <div class="col-md-6"><button type = 'button'
                                                     class = 'btn btn-warning btn-sm cancelBtn'
                                                     ng-show = "dh.editDrivingHourIsClick"
                                                     ng-click = "cancelDrivingHour(dh, $index)"><i class = "icon-remove-sign"></i></button></div>
                                                     </div>
                                          </td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Eleventh Tab --> <!-- Twelveth Tab --> <tab
						ng-click="setDates('speed'); select(10)" active="activeTabs[10]"> <tab-heading>Speed</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">

							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									ng-model='efmfilter.filterSpeed'
									expect_special_char>
							</div>
							<div class="col-md-2">
								<select ng-model="searchSpeed.SpeedVendors" class="form-control"
									ng-options="SpeedAllVendor.name for SpeedAllVendor in SpeedAllVendors track by SpeedAllVendor.Id"
									ng-change="setVendorSpeed(searchSpeed.SpeedVendors,facilityData.listOfFacility)">
								</select>
							</div>
							<div class="col-md-2">
								<select
									class="select_reports_VSelection marginBottom10 form-control"
									ng-model="searchSpeed.VSelection"
									ng-options="vendorVehicle.name for vendorVehicle in vendorVehicles_SpeedReport track by vendorVehicle.Id"
									ng-change="setvendorVehicle(searchSpeed,facilityData.listOfFacility)" required>
								</select>
							</div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="speedGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportTableSpeed'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotSpeedResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='14' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Speed Summary Report - <span class="badge">{{reportsSpeedDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Speed Report</h5> <div>{{searchFromDatesSpeed |
														date : 'longDate'}} - {{searchFromDatesSpeed | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="speedExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('dateTime')">
												Date / Time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'dateTime'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Vendor Name</th>
										<th>Driver Id</th>
										<th>Driver Name</th>
										<th>Vehicle Number</th>
										<th>Route Name</th>
										<th>Shift Time</th>
										<th>Speed</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr class="reportSum"
										ng-repeat="speed in reportsSpeedData| filter:efmfilter.filterSpeed | orderBy:dateAscDsc:reverse">
										<td>{{speed.dateTime}}</td>
										<td>{{speed.vendorName}}</td>
										<td>{{speed.driverId}}</td>
										<td>{{speed.driverName}}</td>
										<td>{{speed.vehicleNumber}}</td>
										<td>{{speed.area}}</td>
										<td>{{speed.shiftTime}}</td>
										<td>{{speed.speed}}</td>
										<td>{{speed.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Twelveth Tab --> <!-- Thirteenth Tab --> <tab
						ng-click="setDates('routeWiseTravel'); select(11)" active="activeTabs[11]"> <tab-heading>Route
					Wise Travel Time</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									expect_special_char
									class='form-control floatRight' ng-model='efmfilter.filterRWT'>
							</div>
							<div class="col-md-2">
								<select ng-model="searchRWT.type" class="form-control"
									ng-options="RWTTripType.text for RWTTripType in RWTTripTypes track by RWTTripType.value"
									ng-change="setTripTypeRWT(searchRWT.type,facilityData.listOfFacility)">
								</select>
							</div>
							<div class="col-md-2"></div>
							<div class="col-md-6">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="routewiseGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportTableRWT'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotRWTResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='12' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Route Wise Travel Time Summary Report - <span
															class="badge">{{reportsRouteWiseTravelDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Route Wise Travel Time - {{RWTTripType}}</h5> <div>{{searchFromDatesRWT
														| date : 'longDate'}} - {{searchToDatesRWT | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="routeWiseTravelExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>

									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												Travelled Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Vendor Name</th>
										<th>Vehicle Number</th>
										<th>Driver Id</th>
										<th>Driver name</th>
										<th>Shift Time</th>
										<th><span>Employee Detail</span><br />
										<span>( Id | Name | TravelTime )</span></th>
										<th>Route Name</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												Start time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('tripAssignDate')">
												End time <span class="sortorder pointer"
													ng-show="dateAscDsc === 'tripAssignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Total travel time</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tbody>
									<tr
										ng-repeat="report in reportsRouteWiseTravelData | filter:efmfilter.filterRWT | orderBy:dateAscDsc:reverse">
										<td class="col-md-1 tabletdCenter">{{report.tripAssignDate}}</td>
										<td class="col-md-2">{{report.vendorName}}</td>
										<td class="col-md-1 tabletdCenter">{{report.vehicleNumber}}</td>
										<td class="tabletdCenter">{{report.driverId}}</td>
										<td class="col-md-1">{{report.driverName}}</td>
										<td class="col-md-1 tabletdCenter">{{report.shiftTime}}</td>

										<td class="col-md-2">
											<div>
												<table
													class="table empDetail_routeWise table-striped table-condensed">
													<tbody>
														<tr class="empDetail_routeWiseTr pointer"
															ng-repeat="emp in report.empTravelDetails">
															<td><span><i
																	class="icon-user employeeIcon_report_routeWise"></i>
																	{{emp.employeeId}}</span></td>
															<td><span>{{emp.empName}}</span></td>
															<td><span>{{emp.travelTime}}</span></td>
														</tr>
													</tbody>
												</table>
											</div>
										</td>

										<td class="col-md-1">{{report.routeName}}</td>
										<td class="col-md-1 tabletdCenter">{{report.tripStartDate}}</td>
										<td class="col-md-1 tabletdCenter">{{report.tripCompleteDate}}</td>
										<td class="col-md-1 tabletdCenter">{{report.totalRouteTravelledTime}}</td>
										<td class="col-md-1">{{report.facilityName}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					</tab> <!-- End of Thirteenth Tab --> <!-- End of fourtennth Tab -->
					<tab
						ng-click="setDates('employeeDetails'); select(12)" active="activeTabs[12]"> <tab-heading>Employee
					Wise Reports</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									expect_special_char
									ng-model='efmfilter.employeeWiseReport'>
							</div>
							<div class="col-md-2">
								<input type="text" placeholder="Enter Employee Id"
									ng-minlength="0"
									ng-maxlength="20"
									maxlength="20"
									ng-model="searchED.employeeId" class="form-control"
									expect_special_char>
							</div>
							<div class="col-md-2"></div>
							<div class="col-md-6">
							<div ng-mouseover="employeeIdnull(searchED.employeeId);">
								<fieldset class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click" ng-disabled="!searchED.employeeId">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</fieldset>
							</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="routewiseGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportableEmployeeDetails'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotEDResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='18' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Employee Details Summary Report - <span class="badge">{{employeeDetailsDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Employee Details</h5> <div>{{searchFromDatesED
														| date : 'longDate'}} - {{searchToDatesED | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="employeeWiseReportExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>

									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('assignDate')">
												Assign Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'assignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Route Close Time</th>
										<th>Trip Start Time</th>
										<th>Cab Reached Time</th>
										<th>Boarding Time</th>

										<th>Trip End Time</th>
										<th>PickUp Time</th>
										<th>Shift Time</th>
										<th>Trip Type</th>
										<th>Driver Name</th>
										<th>Driver Number</th>
										<th>Vehicle Number</th>
										<th>Route Name</th>
										<th>Boarding Status</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tr
									ng-repeat="report in employeeDetailsData | filter:efmfilter.employeeWiseReport | orderBy:dateAscDsc:reverse">
									<td class="col-md-1 tabletdCenter">{{report.assignDate}}</td>
									<td class="col-md-1">{{report.routeCloseTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.tripStartTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.cabReachedTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.boardingTime}}</td>


									<td class="col-md-1 tabletdCenter">{{report.tripEndTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.plannedPickUpTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.shiftTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.tripType}}</td>
									<td class="col-md-1">{{report.driverName}}</td>
									<td class="tabletdCenter">{{report.driverNumber}}</td>
									<td class="col-md-1 tabletdCenter">{{report.vehicleNumber}}</td>
									<td class="col-md-1 tabletdCenter">{{report.routeName}}</td>
									<td class="col-md-1 tabletdCenter">{{report.boardingStatus}}</td>
									<td class="col-md-1 tabletdCenter">{{report.facilityName}}</td>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
					</tab>


					<tab
						ng-click="setDates('employeeFemaleDetails'); select(15)" active="activeTabs[15]"> <tab-heading>
					Female Employee Report</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									expect_special_char
									ng-model='efmfilter.employeeFemaleWiseReport'>
							</div>
							<div class="col-md-2">

							</div>
							<div class="col-md-2"></div>
							<div class="col-md-6">
							<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="routewiseGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportableEmployeeDetails'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotFemaleEDResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='18' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Female Employee Summary Report - <span class="badge">{{femaleemployeeDetailsDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Employee Details</h5> <div>{{searchFromDatesED
														| date : 'longDate'}} - {{searchToDatesED | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="employeeWiseReportExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>

									</tr>
									<tr>
									    <th>Emp Id</th>
										<th>Emp Name</th>
										<th>Emp Number</th>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('assignDate')">
												Assign Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'assignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>



										<th>Route Close Time</th>
										<th>Trip Start Time</th>
										<th>Cab Reached Time</th>
										<th>Boarding Time</th>

										<th>Trip End Time</th>
										<th>PickUp Time</th>
										<th>Shift Time</th>
										<th>Trip Type</th>
										<th>Driver Name</th>
										<th>Driver Number</th>
										<th>Vehicle Number</th>
										<th>Route Name</th>
										<th>Boarding Status</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tr
									ng-repeat="report in employeeFemaleDetailsData | filter:efmfilter.employeeFemaleWiseReport | orderBy:dateAscDsc:reverse">
									<td class="col-md-1 tabletdCenter">{{report.employeeId}}</td>
									<td class="col-md-1 tabletdCenter">{{report.empName}}</td>
									<td class="col-md-1 tabletdCenter">{{report.empNumber}}</td>

									<td class="col-md-1 tabletdCenter">{{report.assignDate}}</td>
									<td class="col-md-1">{{report.routeCloseTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.tripStartTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.cabReachedTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.boardingTime}}</td>


									<td class="col-md-1 tabletdCenter">{{report.tripEndTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.plannedPickUpTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.shiftTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.tripType}}</td>
									<td class="col-md-1">{{report.driverName}}</td>
									<td class="tabletdCenter">{{report.driverNumber}}</td>
									<td class="col-md-1 tabletdCenter">{{report.vehicleNumber}}</td>
									<td class="col-md-1 tabletdCenter">{{report.routeName}}</td>
									<td class="col-md-1 tabletdCenter">{{report.boardingStatus}}</td>
									<td class="col-md-1 tabletdCenter">{{report.facilityName}}</td>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
					</tab>

					<tab
						ng-click="setDates('employeeRequestDetails'); select(13)" active="activeTabs[13]"> <tab-heading>Employee Requests Reports</tab-heading>
					<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<div class="col-md-2">
								<input type='text' placeholder="Filter"
									class='form-control floatRight'
									expect_special_char
									ng-model='efmfilter.employeeRequestReport'>
							</div>
							<div class="col-md-2">
								<input type="text" placeholder="Enter Employee Id"
									ng-minlength="0"
									ng-maxlength="20"
									maxlength="20"
									ng-model="searchERD.employeeId" class="form-control"
									 expect_special_char>
							</div>
							<div class="col-md-2">
								<!-- <select name="mySelect" class="form-control"
										ng-options="option.requestTypes for option in requestTypesData.availableOptions track by option.value"
										ng-model="requestTypesData.selectedOptionERD">
								<option value="">Select Request Types</option>
								</select> -->
							</div>
							<div class="col-md-6">
								<div ng-mouseover="employeeIdnull(searchERD.employeeId);">
								<fieldset class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click" ng-disabled="!searchERD.employeeId" >
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</fieldset></div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="routewiseGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportableEmployeeDetails'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotERDResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='18' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Employee Request Details Summary Report - <span class="badge">{{employeeRequestDetailsDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Employee Request Details</h5> <div>{{searchFromDatesERD
														| date : 'longDate'}} - {{searchToDatesERD | date :
														'longDate'}}</div>
												</div>

												<!-- <div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="employeeRequestWiseReportExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div> -->
											</div>
										</th>

									</tr>
									<tr>
										<th>
											<div href="" class="filterSort pointer"
												ng-click="order('assignDate')">
												Assign Date <span class="sortorder pointer"
													ng-show="dateAscDsc === 'assignDate'"
													ng-class="{reverse:reverse}"></span><i class="icon-filter"></i>
											</div>
										</th>
										<th>Route Close Time</th>
										<th>Trip Start Time</th>
										<th>Cab Reached Time</th>
										<th>Boarding Time</th>

										<th>Trip End Time</th>
										<th>PickUp Time</th>
										<th>Shift Time</th>
										<th>Trip Type</th>
										<th>Driver Name</th>
										<th>Driver Number</th>
										<th>Vehicle Number</th>
										<th>Route Name</th>
										<th>Boarding Status</th>
										<th>Facility Name</th>
									</tr>
								</thead>
								<tr
									ng-repeat="report in employeeRequestDetailsData | filter:efmfilter.employeeRequestReport | orderBy:dateAscDsc:reverse">
									<td class="col-md-1 tabletdCenter">{{report.assignDate}}</td>
									<td class="col-md-1">{{report.routeCloseTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.tripStartTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.cabReachedTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.boardingTime}}</td>


									<td class="col-md-1 tabletdCenter">{{report.tripEndTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.plannedPickUpTime}}</td>

									<td class="col-md-1 tabletdCenter">{{report.shiftTime}}</td>
									<td class="col-md-1 tabletdCenter">{{report.tripType}}</td>
									<td class="col-md-1">{{report.driverName}}</td>
									<td class="tabletdCenter">{{report.driverNumber}}</td>
									<td class="col-md-1 tabletdCenter">{{report.vehicleNumber}}</td>
									<td class="col-md-1 tabletdCenter">{{report.routeName}}</td>
									<td class="col-md-1 tabletdCenter">{{report.boardingStatus}}</td>
									<td class="col-md-1 tabletdCenter">{{report.facilityName}}</td>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
					</tab>

					<!-- <tabset class="tabset tripSheet_reports"> -->
					<tab ng-click="setDates('feedbacks'); select(16)" active="activeTabs[16]">
					<tab-heading>Feedbacks</tab-heading>
					<div class="tripSheetTabContent" style="height: 450px;">
						<div class="searchDIVREPORT row">
							<div class="col-md-2">
								<input class="form-control" ng-model="efmfilter.filterTrips"
								expect_special_char
									placeholder="Filter Feedbacks">
							</div>

							<div class="col-md-2">
								<select name="mySelect" class="form-control"
									ng-options="feedback.text for feedback in feedbackTypes track by feedback.value"
									ng-model="feedbackTypes.selectedType"
									ng-change="setfeedbackType(feedbackTypes.selectedType)">
									<!-- <option value="">Select Request Types1</option> -->
								</select>
							</div>
							<div class="col-md-2"></div>
							<div class="col-md-2"></div>
							<div class="col-md-4">
								<div class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span>
								</div>
							</div>
						</div>

						<div class="col-md-4 col-xs-12 col-md-offset-4 animated  slideInUp"
							ng-show="tripGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>
						<div id='exportableTripSheet'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotFeedbackResult">
							<table
								class="reportTable reportTable_km table table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan="20" class="tableHeadding_km repotTabMain">
											<div class='row'>
												<div class='col-md-4'>
													<!-- <h5 class="lengthReport">
														Trip Sheet Summary Report - <span class="badge">{{onTimeDataLength}}</span>
													</h5> -->
												</div>
												<div class='col-md-4'>
													<h5 class="lengthReport">Feedbacks</h5>
													<div>Date : <span>{{fromDate
														| date : 'longDate'}} - {{toDate | date :
														'longDate'}}</span></div>
												</div>
												<div class='col-md-4'>
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="feedbacksExcel()">
														<i class="icon-download-alt"></i> <span
															class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>Alert Id</th>
										<th>Emp Id</th>
										<th>Emp Name</th>
										<th>Subject Type</th>
										<th>Subject</th>
										<th>Trip Type</th>
										<th>Shift Time</th>
										<th>Description</th>
										<th>Remarks</th>
										<th>Facility Name</th>
										<th>Date</th>
										<th>Edit</th>
									</tr>
								</thead>

								<tbody>
									<tr ng-repeat-end=""
										ng-repeat-start="feedback in feedbackData | filter: efmfilter.filterTrips"
										class="secondaryloop_tripReport">
										<td class="col5">{{feedback.alertId}}</td>
										<td class="col5">{{feedback.empId}}</td>
										<td class="col10">{{feedback.empName}}</td>
										<td class="col10">{{feedback.alertType}}</td>
										<td class="col10">{{feedback.alertTitle}}</td>
										<td class="col10">{{feedback.tripType}}</td>
										<td class="col10">{{feedback.shiftTime}}</td>
										<td class="col10">{{feedback.empDescription}}</td>
										<td class="col10">{{feedback.remarks}}</td>
										<td class="col10">{{feedback.facilityName}}</td>
										<td class="col10">{{feedback.updationDateTime}}</td>
										<td class="col5">
											<button class='btn btn-warning btn-xs buttonRadius0'
												ng-click="editFeedback(feedback, $index)">
												<i class='icon-pencil editPensilReportsTripSheet'></i>
											</button>
										</td>
									</tr>
									<tr ng-repeat-end="" class="displayNone" ></tr>
								</tbody>
							</table>
						</div>
					</div>
				</tab>

	<tab ng-click="setDates('travelReport'); select(17)" active="activeTabs[17]"> <tab-heading>Travel Report</tab-heading>
							<div class="kmTabContent">
						<div class="searchDIVREPORT row marginRow">
							<!-- drop down-->
							  <div class = "col-md-3" ng-click = "setProjectNameChangeTravel('projectName',facilityData.listOfFacility)">

                                        <span>Project Name</span>

                                                               <multi-select-dropdown-span-three
                                                                      input-model="listOfProjectNameTR"
                                                                      output-model="userDataProjectNameTR"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "sendSelectedUserDetailsTR(userDataProjectNameTR,'name')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-three>
                                    </div>


                                    <div class = "col-md-3" ng-click ="setProjectNameChangeTravel('projectId',facilityData.listOfFacility)">
                                         <span>Project Id</span>
                                                               <multi-select-dropdown-span-three
                                                                      input-model="listOfProjectIdTR"
                                                                      output-model="userDataProjectIdTR"
                                                                      button-label="icon name"
                                                                      item-label="icon name maker"
                                                                      tick-property="ticked"
                                                                      on-item-click = "sendSelectedUserDetailsTR(userDataProjectIdTR,'id')"
                                                                      selection-mode="single"
                                                                      >
                                                               </multi-select-dropdown-span-three>

                                    </div>
							<div class="col-md-6">
							<div ng-mouseover="projectDetailnull(userDataProjectNameTR)">
								<fieldset class="calenderMainDiv floatRight pointer"
									popover-template="partials/popovers/calenderReport.jsp"
									popover-placement="bottom" popover-title="Get Report"
									popover-trigger="click">
									<span><i class="icon-calendar"></i></span> <span>{{fromDate
										| date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
										ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
								</fieldset>
							</div>
							</div>
						</div>
					<!-- spinner -->
						<div class="col-md-4 col-xs-12 col-md-offset-4"
							ng-show="TRGetReportButtonClicked">
							<img class="spinner02" src="images/spinner02.gif"
								alt="Getting Result..">
						</div>

						<div id='exportableTravelReport'
							class="col-md-12 col-xs-12 tableWrapper_report"
							ng-show="gotTravelResult">
							<table
								class="reportTable table reportTable_km table-responsive container-fluid">
								<thead class="tableHeading">
									<tr>
										<th colspan='24' class="repotTabMain">
											<div class="row">
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Travel Details Summary Report - <span class="badge">{{travelReportDataLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Travel Details</h5> <div>{{searchFromDatesTR
														| date : 'longDate'}} - {{searchToDatesTR | date :
														'longDate'}}</div>
												</div>
												<div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="travelReportExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div>
											</div>
										</th>

									</tr>
									<tr>
										<th>Trip Id</th>
										<th>Trip Type</th>
										<th>User Id</th>
										<th>User Name</th>
										<th>Team / Department</th>
										<th>Project Id</th>
										<th>Project Name</th>
										<th>Guest Mobile Number</th>
										<th>Usage Location</th>
										<th>Tariff Type</th>
										<th>Car Type</th>
										<th>Vehicle Reg Num</th>
										<th>Cab Req Time</th>
										<th>Cab Departure Time</th>
										<th>Trip Distance KM</th>
										<th>Distance Per User</th>
										<th>Utilization</th>
										<th>Travel Charges</th>
										<th>Travel Charges Tax</th>
										<th>Escort</th>
										<th>Facility Name</th>
									</tr>
								</thead>

								<tr
									ng-repeat-start="dateAssigned in travelReportData | orderBy:dateAscDsc:reverse"
										class="visibleRow_reportTripSheet">
										<td colspan="24">{{dateAssigned.tripAssignDate}}</td>
									</tr>

								<tr ng-repeat-end=""
										ng-repeat-start="travelReport in dateAssigned.tripDetail | filter: efmfilter.filterTrips"
										class="secondaryloop_tripReport">{{}}
										<!-- <td class="col5">{{trip.routeId}}</td> -->
									<td class="col5 tabletdCenter">{{travelReport.tripId}}</td>
									<td class="col10 tabletdCenter">{{travelReport.tripType}}</td>
									<td class="col10 tabletdCenter">{{travelReport.employeeId}}</td>
									<td class="col10 tabletdCenter">{{travelReport.employeeName}}</td>
									<td class="col10 tabletdCenter">{{travelReport.employeeDepartment}}</td>
									<td class="col5 tabletdCenter">{{travelReport.projectId}}</td>
									<td class="col10 tabletdCenter">{{travelReport.projectName}}</td>
									<td class="col10 tabletdCenter">{{travelReport.mobileNumber}}</td>
									<td class="col10 tabletdCenter">{{travelReport.routeName}}</td>
									<td class="col10 tabletdCenter">{{travelReport.contractDetails}}</td>
									<td class="col10 tabletdCenter">{{travelReport.vehicleType}}</td>
									<td class="col10 tabletdCenter">{{travelReport.vehicleNumber}}</td>
									<td class="col10 tabletdCenter">{{travelReport.shiftTime}}</td>
									<td class="col10 tabletdCenter">{{travelReport.startTime}}</td>
									<td class="col10 tabletdCenter">{{travelReport.distance}}</td>
									<td class="col10 tabletdCenter">{{travelReport.userKm}}</td>
									<td class="col10 tabletdCenter">{{travelReport.utilization}}</td>
									<td class="col10 tabletdCenter">{{travelReport.employeeCost}}</td>
									<td class="col10 tabletdCenter">{{travelReport.empCostWithTax}}</td>
									<td class="col10 tabletdCenter">{{travelReport.escort}}</td>
									<td class="col10 tabletdCenter">{{travelReport.facilityName}}</td>
								</tr>
								<tr ng-repeat-end="" class="displayNone"></tr>
								<tbody>
								</tbody>
							</table>
						</div>
						</div>
					</tab>




                              <tab ng-click="setDates('dynamicDetails'); select(14)" active="activeTabs[14]"> <tab-heading>Dynamic Reports</tab-heading>
                               <div class="kmTabContent pointer dynamicReportHeight">
                                    <div class="searchDIVREPORT row marginRow">

                                          <div class="col-md-3">
                                                <select name="mySelect" class="form-control pointer"
                                                      ng-options="option.requestTypes for option in dynamicData.availableOptions track by option.value"
                                                      ng-model="dynamicData.selectedOption"
                                                      ng-change="setDynamicType(dynamicData.selectedOption)">
                                                      <!-- <option value="">Select Category Types</option> -->
                                                </select>
                                          </div>
                                          <div class="col-md-3">
											<input type='text' placeholder="Filter"
											expect_special_char
											class='form-control floatRight'
											ng-model='efmfilter.filterDR'>
								   		  </div>




									<!-- 	<div class="col-md-1">
										</div> -->

                                          <div class="col-md-6">
                                             <div ng-mouseover="dynamicCategorynull(dynamicData.selectedOption);">
                                                <fieldset class="calenderMainDiv floatRight pointer"
                                                      popover-template="partials/popovers/calenderReport.jsp"
                                                      popover-placement="bottom" popover-title="Get Report"
                                                      popover-trigger="click" >
                                                      <span><i class="icon-calendar"></i></span> <span>{{fromDate
                                                            | date : 'longDate'}} - {{toDate | date : 'longDate'}}</span> <span><img
                                                            ng-src='images/spiffygif_22x22.gif' ng-show='isProcessing' /></span>
                                                </fieldset>
                                              </div>
                                          </div>
                                    </div>

								  <div ng-show="!dynamicDataDivEmployeeShow">
									   <form class="form-inline" ng-show="dynamicData.selectedOption.value=='employee' || dynamicData.selectedOption.value=='guest' ">
										    <div class="form-group">
										      <label for="email">Please Select The Dynamic Paramenters</label>
										    </div>
										    <div class="form-group">
												<div isteven-multi-select
													input-model="webBrowsersGroupedData"
													output-model="dynamicDataValues"
													button-label="icon name"
													item-label="icon name maker"
													tick-property="ticked"
													group-property="msGroup"
													orientation="horizontal"
													class="force-overflow pointer"
													on-item-click = "dynamicDataOnChange(dynamicDataValues,dynamicData.selectedOption)"
												>
												</div>
										    </div>
										    <br/>
									  	</form>
								  	</div>

								  	<div ng-show="!dynamicDataDivDriverShow">
									  	<form class="form-inline" ng-show="dynamicData.selectedOption.value=='driverDetails'">
										    <div class="form-group">
										      <label for="email">Please Select The Dynamic Paramenters</label>
										    </div>
										    <div class="form-group">
												<div isteven-multi-select
		                                          input-model="webBrowsersGrouped"
		                                          output-model="dynamicDataValues"
		                                          button-label="icon name"
		                                          item-label="icon name maker"
		                                          tick-property="ticked"
		                                          group-property="msGroup"
		                                          orientation="horizontal"
		                                          class="force-overflow pointer"
		                                          on-item-click = "dynamicDataOnChange(dynamicDataValues,dynamicData.selectedOption)"
		                                          >
		                                    </div>
										    </div>
										    <br/>
									  	</form>
								  	</div>

                                 	<div ng-show="dynamicDataDivEmployeeShow">
									  	<form class="form-inline">
										    <div class="form-group">
										      <label for="email">Please Select The Dynamic Paramenters</label>
										    </div>
										    <div class="form-group">
												<div isteven-multi-show
		                                          input-model="webBrowsersGroupedData"
		                                          output-model="dynamicDataValues"
		                                          button-label="icon name"
		                                          item-label="icon name maker"
		                                          tick-property="ticked"
		                                          group-property="msGroup"
		                                          orientation="horizontal"
		                                          class="force-overflow pointer"
		                                          on-item-click = "dynamicDataOnChange(dynamicDataValues,dynamicData.selectedOption)"
		                                          >
		                                    </div>
										    </div>
										    <br/>
									  	</form>
								  	</div>

								  	<div ng-show="dynamicDataDivDriverShow">
									  	<form class="form-inline">
										    <div class="form-group">
										      <label for="email">Please Select The Dynamic Paramenters</label>
										    </div>
										    <div class="form-group">
												<div isteven-multi-show
		                                          input-model="webBrowsersGrouped"
		                                          output-model="dynamicDataValues"
		                                          button-label="icon name"
		                                          item-label="icon name maker"
		                                          tick-property="ticked"
		                                          group-property="msGroup"
		                                          orientation="horizontal"
		                                          class="force-overflow pointer"
		                                          on-item-click = "dynamicDataOnChange(dynamicDataValues,dynamicData.selectedOption)"
		                                          >
		                                    </div>
										    </div>
										    <br/>
									  	</form>
								  	</div>

                                    <br/>


                                    <div class="col-md-12 col-xs-12 tableWrapper_report"
                                         ng-init="dynamicDataInitialize() && dynamicDataShow()">
									<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "byReportDynamicClicked">
									<img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
									</div>

									<div>
                                    <div class="col-md-12 col-xs-12 tableWrapper_report " ng-show="dynamicData.selectedOption.value=='employee' || dynamicData.selectedOption.value=='guest' ">
                                    <!-- <div class="dynamicDataRow">Dynamic Report Summ</div> -->
                                          <table
                                                class="reportTable table reportTable_km table-responsive container-fluid table-bordered table-hover table-striped">

                                          <thead class="tableHeading">
                                          <tr ng-show="gotDynamicResult && dynamicDataLengthLength==0">
										  <th colspan="25" class="repotTabMain">
											<div class="row" >
												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Dynamic Summary Report - <span
															class="badge">{{dynamicDataLengthLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Dynamic Report - {{dynamicData.selectedOption.requestTypes}}</h5> <div>{{searchFromDatesDR
														| date : 'longDate'}} - {{searchToDatesDR | date :
														'longDate'}}</div>
												</div>

												<div class="col-md-4">
												</div>
												</div>
										</th>
									</tr>


                                         <tr ng-show="gotDynamicResult && dynamicDataLengthLength>0">

										  <th colspan="25" class="repotTabMain">

											<div class="row" >

												<div class="col-md-4">
													<h5 class="lengthReport repotTabMainSum">
														Dynamic Summary Report - <span
															class="badge">{{dynamicDataLengthLength}}</span>
													</h5>
												</div>
												<div class="col-md-4">
													<h5 class="lengthReport">Dynamic Report - {{dynamicData.selectedOption.requestTypes}}</h5> <div>{{searchFromDatesDR
														| date : 'longDate' }} - {{searchToDatesDR | date : 'longDate'}}</div>
												</div>

												<div class="col-md-4">
													<h5 class="reportTabMainExcel"><button class="btn btn btn-success"
													ng-click="saveInExcelDynamicReport(shiftTimeDate, tripType, shiftTimes)"
													>
													<i class="icon-download-alt"></i> <span class="marginLeft5">Excel</span>
													</button>
												</div>
												<!-- <div class="col-md-4">
													<button
														class="btn btn-sm btn-success form-control excelExportButton floatRight"
														ng-click="routeWiseTravelExcel()">
														<i class="icon-download-alt"></i><span class="marginLeft5">Excel</span>
													</button>
												</div> -->
												</div>
										</th>
									</tr>
                                                      <tr  ng-show="gotDynamicResult">
                                                      		<th class="dynamicDatatd" ng-show="routeIdShow">Route Id</th>
                                                            <th class="dynamicDatatd" ng-show="assignDateShow">Assign Date</th>
                                                            <th class="dynamicDatatd" ng-show="routeCloseTimeShow">Route Close Time</th>
                                                            <th class="dynamicDatatd" ng-show="shiftTimeShow">Shift Time</th>
                                                            <th class="dynamicDatatd" ng-show="tripTypeShow">Trip Type</th>
                                                            <th class="dynamicDatatd" ng-show="tripStartTimeShow">Trip Start Time</th>
                                                            <th class="dynamicDatatd" ng-show="tripEndTimeShow">Trip End time</th>
                                                            <th class="dynamicDatatd" ng-show="routeNameShow">Route Name</th>
                                                            <th class="dynamicDatatd" ng-show="driverNameShow">Driver Name</th>
                                                            <th class="dynamicDatatd" ng-show="driverMobileNumberShow">Driver Number</th>
                                                            <th class="dynamicDatatd" ng-show="checkInTimeShow">CheckIn Time</th>
                                                            <th class="dynamicDatatd" ng-show="checkOutTimeShow">CheckOut Time</th>
                                                            <th class="dynamicDatatd" ng-show="travelTimeShow">Route Travel time</th>
                                                            <th class="dynamicDatatd" ng-show="checkInVehicleNumberShow">Vehicle Number</th>
                                                            <th class="dynamicDatatd" ng-show="vehicleNumberShow">Vehicle Number</th>
                                                            <th class="dynamicDatatd" ng-show="escortIdShow">Escort Id</th>
                                                            <th class="dynamicDatatd" ng-show="escortNameShow">Escort Name</th>
                                                            <th class="dynamicDatatd" ng-show="escortMobileNoShow">Escort Mobile No</th>
                                                            <th class="dynamicDatatd" ng-show="escortAddressShow">Escort Address</th>
                                                            <th class="dynamicDatatd" ng-show="employeeDetails">Employee Details/Message Details</th>
                                                            <th class="dynamicDatatd" ng-show="plannedDistanceShow">Planned Distance</th>
                                                            <th class="dynamicDatatd" ng-show="gpsDistanceShow">GPS Distance</th>
                                                            <th class="dynamicDatatd" ng-show="vendorNameShow">Vendor Name</th>
                                                            <th class="dynamicDatatd" ng-show="driverIdShow">Driver Id</th>
                                                            <th class="dynamicDatatd" ng-show="driverAddressShow">Driver Address</th>
                                                            <th class="dynamicDatatd" ng-show="driverDrivingHoursPerTripsShow">Driver Driving Hours Per Trip</th>
                                                            <th class="dynamicDatatd" ng-show="totalWorkingHoursShow">Total Working Hours</th>
                                                            <th class="dynamicDatatd" ng-show="remarksShow">Remarks</th>
                                                          <!--   <th class="dynamicDatatd" ng-show="noShowMsgShow">No Show</th> -->
                                                            <th class="dynamicDatatd" ng-show="driverDrivingHoursShow">Driver Driving Hours</th>
                                                            <th class="dynamicDatatd" ng-show="totalDrivingHoursShow">Total Driving Hours</th>
                                                      </tr>
                                                </thead>
                                                <tr  ng-show="gotDynamicResult &&dynamicDataLengthLength==0">
                                        	   <td colspan = '12'>
                                            	<div class = "noData">There are No Dynamic Reports for the Selected date period</div>
                                        	   </td>
                                               </tr>

                                                 <tr ng-repeat="report in dynamicDataRecords| filter:efmfilter.filterDR| orderBy:dateAscDsc:reverse" ng-show="gotDynamicResult && dynamicDataLengthLength>0">
                                                		<th class="dynamicDatatd" ng-show="routeIdShow">{{report.routeId}}</th>
                                                      	<td ng-show="assignDateShow">{{report.assignDate}}</td>
                                                      	<td ng-show="routeCloseTimeShow">{{report.routeCloseTime}}</td>
                                                      	<td ng-show="shiftTimeShow">{{report.shiftTime}}</td>
                                                      	<td ng-show="tripTypeShow">{{report.tripType}}</td>
                                                      	<td ng-show="tripStartTimeShow">{{report.tripStartTime}}</td>
                                                      	<td ng-show="tripEndTimeShow">{{report.tripEndTime}}</td>
                                                      	<td ng-show="routeNameShow">{{report.routeName}}</td>
                                                      	<td ng-show="driverNameShow">{{report.driverName}}</td>
                                                      	<td ng-show="driverMobileNumberShow">{{report.driverNumber}}</td>
                                                      	<td ng-show="checkInTimeShow">{{report.checkInTime}}</td>
                                                      	<td ng-show="checkOutTimeShow">{{report.checkoutTime}}</td>
                                                      	<td ng-show="travelTimeShow">{{report.driverDrivingHoursPerTrip}}</td>
                                                      	<td ng-show="checkInVehicleNumberShow">{{report.vehicleNumber}}</td>
                                                      	<td ng-show="vehicleNumberShow">{{report.vehicleNumber}}</td>
                                                      	<td ng-show="escortIdShow">{{report.escortId}}</td>
                                                      	<td ng-show="escortNameShow">{{report.escortName}}</td>
                                                      	<td ng-show="escortMobileNoShow">{{report.escortMobileNo}}</td>
                                                      	<td ng-show="escortAddressShow">{{report.escortAddress}}</td>
                                                     <!--  	<td ng-show="driverDrivingHoursShow">{{report.driverDrivingHours}}</td>   -->
                                                      	<td ng-show="employeeDetails">
                                                      		<input type="button"
																class="btn btn-xs btn-primary" value="View"
																ng-click="viewEmployeeDetails(report.routeId, $index, 'lg')">
                                                      	</td>
                                                      	<td ng-show="plannedDistanceShow">{{report.plannedDistance}}</td>
                                                      	<td ng-show="gpsDistanceShow">{{report.gpsDistance}}</td>
                                                      	<td ng-show="vendorNameShow">{{report.vendorName}}</td>
                                                      	<td ng-show="driverIdShow">{{report.driverId}}</td>
                                                      	<td ng-show="driverAddressShow">{{report.driverAddress}}</td>
                                                      	<td ng-show="driverDrivingHoursPerTripsShow">{{report.driverDrivingHoursPerTrip}}</td>
                                                      	<td ng-show="totalWorkingHoursShow">{{report.totalWorkingHours}}</td>
                                                      	<td ng-show="remarksShow">{{report.remarks}}</td>
                                                      	<!-- <td ng-show="noShowMsgShow">{{report.noShowMsg}}</td> -->
                                                      	<td ng-show="driverDrivingHoursShow">{{report.totalDrivingHours}}</td>
                                                      	<td ng-show="totalDrivingHoursShow">{{report.totalDrivingHours}}</td>

                                                </tr>

                                                 <tbody>
                                                </tbody>
                                          </table>
                                    </div>

                             		<div class="col-md-12 col-xs-12 tableWrapper_report dynamicReportHeight " ng-show="dynamicData.selectedOption.value=='driverDetails'">
                                          <table
                                                class="reportTable table reportTable_km table-responsive container-fluid table-bordered table-hover" ng-show="gotDynamicResult">
                                                <thead class="tableHeading">
                                                      <tr>
                                                     		<th class="dynamicDatatd" ng-show="assignDateShow">Assign Date</th>
                                                      		<th class="dynamicDatatd" ng-show="routeIdShow">Route Id</th>
                                                      		<th class="dynamicDatatd" ng-show="routeNameShow">Route Name</th>
                                                      		<th class="dynamicDatatd" ng-show="vendorNameShow">Vendor Name</th>
                                                            <th class="dynamicDatatd" ng-show="vehicleNumberShow">Vehicle Number</th>
                                                            <th class="dynamicDatatd" ng-show="driverNameShow">Driver Name</th>
                                                            <th class="dynamicDatatd" ng-show="driverAddressShow">Driver Address</th>
                                                            <th class="dynamicDatatd" ng-show="driverIdShow">Driver Id</th>
                                                            <th class="dynamicDatatd" ng-show="tripTypeShow">Trip Type</th>
                                                            <th class="dynamicDatatd" ng-show="tripStartTimeShow">Trip Start Time</th>
                                                            <th class="dynamicDatatd" ng-show="tripEndTimeShow">Trip End Time</th>
                                                            <th class="dynamicDatatd" ng-show="driverDrivingHoursPerTripsShow">Driving Hours Per Trip</th>
                                                            <th class="dynamicDatatd" ng-show="driverDrivingHoursShow">Driver hrs/Trip</th>
                                                            <th class="dynamicDatatd" ng-show="totalDrivingHoursShow">Total Driving Hours</th>
                                                            <th class="dynamicDatatd" ng-show="checkInTimeShow">CheckIn Time</th>
	                                                        <th class="dynamicDatatd" ng-show="checkOutTimeShow">Check Out Time</th>
                                                            <th class="dynamicDatatd" ng-show="totalWorkingHoursShow">Total Working Hours</th>
                                                            <th class="dynamicDatatd" ng-show="remarksShow">Remarks</th>


                                                      </tr>
                                                </thead>
                                                  <tbody ng-repeat="report in dynamicDataRecords">
                                                <tr>
                                                		<td ng-show="assignDateShow">{{report.tripsDetails[0].assignDate}}</td>
                                                		<th class="dynamicDatatd" ng-show="routeIdShow">{{report.tripsDetails[0].routeId}}</th>

                                                      	<td ng-show="routeNameShow">{{report.tripsDetails[0].routeName}}</td>
                                                      	<td ng-show="vendorNameShow">{{report.tripsDetails[0].vendorName}}</td>
                                                      	<td ng-show="vehicleNumberShow">{{report.tripsDetails[0].vehicleNumber}}</td>
                                                      	<td ng-show="driverNameShow">{{report.tripsDetails[0].driverName}}</td>
                                                      	<td ng-show="driverIdShow">{{report.tripsDetails[0].driverId}}</td>
                                                      	<td ng-show="tripTypeShow">{{report.tripsDetails[0].tripType}}</td>
                                                      	<td ng-show="tripStartTimeShow">{{report.tripsDetails[0].tripStartTime}}</td>
                                                      	<td ng-show="tripEndTimeShow">{{report.tripsDetails[0].tripEndTime}}</td>
                                                      	<td ng-show="driverDrivingHoursPerTripsShow">{{report.tripsDetails[0].driverDrivingHoursPerTrip}}</td>
                                                      	<td rowspan="{{report.tripsDetails.length}}" ng-show="totalDrivingHoursShow" >{{report.totalDrivingHours}}</td>
                                                      <!-- 	<td ng-show="checkInTimeShow">{{report.tripsDetails[0].checkInTime}}</td> -->
                                                      	<td rowspan="{{report.tripsDetails.length}}" ng-show="checkInTimeShow">{{report.checkInTime}}</td>

                                                     <!--  	<td ng-show="checkOutTimeShow">{{report.tripsDetails[0].checkoutTime}}</td>  -->
                                                      	<td rowspan="{{report.tripsDetails.length}}" ng-show="checkOutTimeShow">{{report.checkoutTime}}</td>
                                                      	<!-- <td ng-show="totalWorkingHoursShow">{{report.tripsDetails[0].totalWorkingHours}}</td> -->
                                                      	<td rowspan="{{report.tripsDetails.length}}" ng-show="totalWorkingHoursShow">{{report.totalWorkingHours}}</td>
                                                      	<!-- <td ng-show="remarksShow">{{report.tripsDetails[0].remarks}}</td> -->
                                                      	<td rowspan="{{report.tripsDetails.length}}" ng-show="remarksShow">{{report.remarks}}</td>


                                                </tr>
                                                <tr
										ng-repeat="dh in report.tripsDetails.slice(1,report.tripsDetails.length)">

														<td ng-show="assignDateShow">{{dh.assignDate}}</td>
                                                		<th class="dynamicDatatd" ng-show="routeIdShow">{{dh.routeId}}</th>
                                                      	<td ng-show="routeNameShow">{{dh.routeName}}</td>
                                                      	<td ng-show="vendorNameShow">{{dh.vendorName}}</td>
                                                      	<td ng-show="vehicleNumberShow">{{dh.vehicleNumber}}</td>
                                                      	<td ng-show="driverNameShow">{{dh.driverName}}</td>
                                                      	<td ng-show="driverIdShow">{{dh.driverId}}</td>
                                                      	<td ng-show="tripTypeShow">{{dh.tripType}}</td>
                                                      	<td ng-show="tripStartTimeShow">{{dh.tripStartTime}}</td>
                                                      	<td ng-show="tripEndTimeShow">{{dh.tripEndTime}}</td>
                                                      	<td ng-show="driverDrivingHoursPerTripsShow">{{dh.driverDrivingHoursPerTrip}}</td>
                                                      	<!-- <td ng-show="checkInTimeShow">{{dh.checkInTime}}</td>
                                                      	<td ng-show="checkOutTimeShow">{{dh.checkoutTime}}</td>  -->
                                                      	<!-- <td ng-show="totalWorkingHoursShow">{{dh.totalWorkingHours}}</td> -->
                                                      	<!-- <td ng-show="remarksShow">{{dh.remarks}}</td>   -->

												</tr>
                                               </tbody>
                                          </table>
                                    </div>


                              </div>
                              </tab>



					 </tabset>
				</div>
				<div class="clearfix"></div>
				<br>
			</div>

		</div>

	</div>
</div>
