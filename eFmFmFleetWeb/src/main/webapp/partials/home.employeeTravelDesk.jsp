<!--
@date           04/01/2015
@Author         Saima Aziz
@Description    Page to track the Employees assigned to cabs with their trip type and shift time
@State          home.employeeTravelDesk
@URL            /employeeTravelDesk

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<div class="empTravelDeskTemplate container-fluid"
	ng-if="isEmployeeRosterActive">
	<div class="row">
		<div class="col-md-12 col-xs-12 heading1">
			<span class="col-md-12 col-xs-12">Employee-Roster <span
				class='spinner_travelDesk'> <img
					ng-src='images/spiffygif_24x24.gif' ng-show='isProcessing'>
			</span>
			</span>
			<div class="col-md-12 col-xs-12 ">
				<div class='col-md-12 col-xs-12'>
					<!-- <button type = 'button'
                        class = 'btn btn-success floatRight buttonRadius0 '
                        ng-click = 'assignCab()'><span>Assign Cab</span></button> -->
				</div>

			</div>
		</div>
	</div>

</div>


<div class="invoiceTemplate container-fluid">
	<div class="row">
		<div class="col-md-12 col-xs-12 invoiceHeading1">

			<div class="col-md-12 col-xs-12 mainTabDiv_invoice marginBottom50">

				<!-- /*START OF WRAPPER1 = VENDORWISE INVOICE */ -->
				<div class="wrapper1">

					<tabset class="tabset">
					<button type='button' style="padding: 8px;"
						class='btn btn-success floatRight buttonRadius0 buttonTravelDeskButton'
						ng-click='assignCab()'>
						<span>Routing</span>
					</button>
					<tab
						ng-click="getTravelDesk()" > <tab-heading>Employee
					Details</tab-heading>
					<div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
						<div class="heading2 heading2_travelDesk row">
							<span class="col-md-2 floatLeft">Employee Details</span>
							<span class="col-md-5 floatLeft"><h5 class="lengthEmployeeDetail">Employee Details Summary Report - <span class="badge">{{summaryLengthShift}} / {{totalRecordOfRoaster}}</span></h5></span>
							<div class="col-md-5 floatRight travelDeskHeadingButtons">
								<efmfm-button img-class="efmfm_approvalButtons_collapse"
									src-url="images/portlet-collapse-icon"
									selected-url="images/portlet-expand-icon"
									hover-url="images/portlet-collapse-icon"
									alt-text="Collapse Window" main-div="employeeList"
									target-div="employeeListContent"> </efmfm-button>
								<efmfm-button img-class="efmfm_approvalButtons_reload"
									src-url="images/portlet-reload-icon"
									selected-url="images/portlet-reload-icon"
									hover-url="images/portlet-reload-icon" alt-text="Reload Window"
									main-div="employeeList" target-div="employeeListContent"
									ng-click='refreshEmployeeTravelDesk()'> </efmfm-button>
								<efmfm-button img-class="efmfm_dashboarButtons_remove"
									src-url="images/portlet-remove-icon"
									selected-url="images/portlet-remove-icon"
									hover-url="images/portlet-remove-icon" alt-text="Remove Window"
									main-div="employeeList" target-div="employeeListContent">
								</efmfm-button>
							</div>
						</div>
						<div class="employeeListContent">
							<!-- /*FIRST ROW*/-->
							<div class="row paddingLeftRight10">

								<div class="col-xs-2">
									<div class="input-group calendarInput" style="width: 149px;">
										<span class="input-group-btn">
											<button class="btn btn-default"
												ng-click="shiftTimeDateCal($event)">
												<i class="icon-calendar calInputIcon"></i>
											</button>
										</span> <input type="text" ng-model="shiftTimeDate"
											class="form-control" placeholder="Shift Time Date"
											datepicker-popup='{{format}}'
											is-open="datePicker.shiftTimeDate" show-button-bar=false
											datepicker-options='dateOptions' name='shiftTimeDate'
											ng-change="getshiftTimeDates()" required readonly>
									</div>
								</div>

								<div class="col-xs-2" ng-show="multiFacility == 'Y'">
										<am-multiselect class="input-lg"
														style="margin-left: -52px;"
					                                    multiple="true"
					                                    ms-selected ="{{facilityData.length}} Facility(s) Selected"
					                                    ng-model="facilityData"
					                                    ms-header="All Facility"
					                                    options="facility.branchId as facility.name for facility in facilityDetails"
					                                    change="setFacilityDetails(facilityData); search.shiftTimes = null; search.tripType = null">
                   						</am-multiselect>
								</div>

								<div class="col-xs-2">
									<select ng-model="search.tripType"
				                            class="form-control"
				                            style="width: 112px; margin-left: -50px;"
				                            ng-options="tripType.text for tripType in tripTypes track by tripType.value"
				                            ng-change="setTripTypeliveTracking(search.tripType, facilityData); search.shiftTimes = null;" required
				                            ng-disabled= "!tripTypeEmployee || facilityData.length == 0"
				                            ng-focus="search.text = null"
				                            >
				                            <option value="">Trip Type</option>
				                    </select>
								</div>


								<div class="col-xs-2">
									<select ng-model="search.shiftTimes"
											style="width: 120px;margin-left: -137px;"
					                      class="form-control"
					                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeData"
					                      required
					                      ng-change = "setShiftButton()"
					                      ng-disabled= "!shiftTimeEmployee || !search.tripType"
				                      >
				                      <option value="">Shift Time</option>
				                    </select>
								</div>


								<div
									class="col-md-3" style="margin-left: -188px;">
									<button class="btn btn-success"
										ng-click="setShifts(shiftTimeDate, search.tripType, search.shiftTimes, facilityData, true)"
										ng-disabled="!setShiftButtonEmployee || !search.tripType || !search.shiftTimes || facilityData.length == 0 || !shiftTimeDate">Result</button>

									<button class="btn btn btn-success"
										ng-click="saveInExcel(shiftTimeDate, search.tripType, search.shiftTimes, search.text, facilityData)"
										ng-disabled="!excelButtonVisible">
										<i class="icon-download-alt"></i> <span class="marginLeft5">Excel</span>
									</button>


									<button class="btn btn btn-danger"
										ng-click="deleteEmployeeDetails(shiftTimeDate, search.tripType, search.shiftTimes, search.text,facilityData)"
										ng-disabled="!excelButtonVisible">
										<i class="icon-remove-sign"></i><span class="marginLeft5">Delete</span>
									</button>

								</div>

								<div class="col-md-2 col-xs-2 searchEmployeeTravelDesk">
									<div class="input-group floatLeft calendarInput">
										<input ng-model="search.text" type="text" class="form-control"
											ng-keydown="$event.which === 13 && searchEmployee(search.text)"
											placeholder="Search Employee" maxlength='15'
											ng-focus="searchIsEmpty = false; search.tripType = null; search.shiftTimes = null; shiftTimeDate = null"
											ng-class="{error: searchIsEmpty}"> <span
											class="input-group-btn">
											<button class="btn btn-success"
												ng-click="searchEmployee(search.text)" ng-disabled="!search.text">
												<i class="icon-search searchServiceMappingIcon"></i>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>

					    <!--   /*SECOND ROW*/-->
					    <div class="" ng-show="employeeRosterTableInit">
                            <div class="row divTableHeader" style="margin: 0; padding: 0; font-size: 14px; font-weight: 900;" ng-show="postsInit.length > 0">
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Employee Id</div>
                                        <div class="col-md-2 col-sm-2">Employee Name</div>
                                        <div class="col-md-2 col-sm-2">Trip Type</div>
                                        <div class="col-md-2 col-sm-2">Request Date</div>
                                        <div class="col-md-2 col-sm-2">Request Type</div>
                                        <div class="col-md-2 col-sm-2">Shift Time</div>
                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Route Name</div>
                                        <div class="col-md-2 col-sm-2">Pick/Drop Time</div>
                                        <div class="col-md-3 col-sm-3">Pickup/Drop Location</div>
                                        <div class="col-md-2 col-sm-2">Area Name</div>
                                        <div class="col-md-2 col-sm-2">Facility Name</div>
                                        <div class="col-md-1 col-sm-1">Nodal Points</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-4 col-sm-4">Cab Status</div>
                                        <div class="col-md-4 col-sm-4">Edit</div>
                                        <div class="col-md-4 col-sm-4">Delete</div>
                                    </div>
                            </div>
                            <div class="constrainedEmpRosterInit row margin0">
                                <div class="overflowXauto" infinite-scroll="getTravelDesk()" infinite-scroll-container='".constrainedEmpRosterInit"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0 text-center' ng-repeat="post in postsInit |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}" ng-show="postsInit.length > 0">
                                    	<div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeId}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripType}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripDate}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.requestType}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)">
										</div>
                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeRouteName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.pickUpTime}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.employeeAddress}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeAreaName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.facilityName}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.nodalPointTitle}}</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-4 col-sm-4 divTableData">{{post.cabAvailable}}</div>
                                        <div class="col-md-4 col-sm-4 divTableData"><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg",facilityData)'></div>
                                        <div class="col-md-4 col-sm-4 divTableData"><div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post,facilityData)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div></div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="postsInit.length == 0">
                                <div class="noData">No data Found</div>
                            </div>
                        </div>





					    <div class="" ng-show="multiEmployeeRosterTableShift">
                            <div class="row divTableHeader" style="margin: 0; padding: 0; font-size: 14px; font-weight: 900;"  ng-show="postShift.length > 0">
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Employee Id</div>
                                        <div class="col-md-2 col-sm-2">Employee Name</div>
                                        <div class="col-md-2 col-sm-2">Trip Type</div>
                                        <div class="col-md-2 col-sm-2">Request Date</div>
                                        <div class="col-md-2 col-sm-2">Request Type</div>
                                        <div class="col-md-2 col-sm-2">Shift Time</div>
                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Route Name</div>
                                        <div class="col-md-2 col-sm-2">Pick/Drop Time</div>
                                        <div class="col-md-3 col-sm-3">Pickup/Drop Location</div>
                                        <div class="col-md-2 col-sm-2">Area Name</div>
                                        <div class="col-md-2 col-sm-2">Facility Name</div>
                                        <div class="col-md-1 col-sm-1">Nodal Points</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-4 col-sm-4">Cab Status</div>
                                        <div class="col-md-4 col-sm-4">Edit</div>
                                        <div class="col-md-4 col-sm-4">Delete</div>
                                    </div>
                            </div>
                            <div class="constrainedEmpRosterShift row margin0">
                                <div class="overflowXauto" infinite-scroll="setShifts(shiftTimeDate, search.tripType, search.shiftTimes, facilityData)" infinite-scroll-container='".constrainedEmpRosterShift"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0 text-center' ng-repeat="post in postShift |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}" ng-show="postShift.length > 0">
                                    	<div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeId}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripType}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripDate}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.requestType}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)">
										</div>
                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeRouteName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.pickUpTime}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.employeeAddress}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeAreaName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.facilityName}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.nodalPointTitle}}</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-4 col-sm-4 divTableData">{{post.cabAvailable}}</div>
                                        <div class="col-md-4 col-sm-4 divTableData"><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg",facilityData)'></div>
                                        <div class="col-md-4 col-sm-4 divTableData"><div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post,facilityData)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div></div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="postShift.length == 0">
                                <div class="noData">No data found</div>
                            </div>
                        </div>

						<div class="row">
							<div class="wrapper2_EmpTravelDesk" id="employeeTravelDeskExport" ng-show="employeeRosterTable">
								<table
									class="employeeListTable_TravelDesk table table-hover table-responsive container-fluid"
									ng-show="isLoaded">
									<thead class="tableHeading">
										<tr>
											<th>Employee Id</th>
											<th>Employee Name</th>
											<th>Trip Type</th>
											<th>Request Date</th>
											<th>Request Type</th>
											<th>Shift Time</th>
											<th>Route Name</th>
											<th>Pick/Drop Time</th>
											<th>Pickup/Drop Location</th>
											<th>Area Name</th>
											<th>Facility Name</th>
											<th>Nodal Points</th>
											<th>Cab Status</th>
											<th>Edit</th>
											<th>Delete</th>
										</tr>
									</thead>
									<tbody ng-show="posts.length>0">
										<tr
											ng-repeat="post in posts |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}">
											<td class="col-md-1">{{post.employeeId}}</td>
											<td class="col-md-1">{{post.employeeName}}</td>
											<td class="col-md-1">{{post.tripType}}</td>
											<td class="col-md-1">{{post.tripDate}}</td>
											<td class="col-md-1">{{post.requestType}}</td>

											<td class="col-md-1"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)"></td>
											<td class="col-md-1">{{post.employeeRouteName}}</td>
											<td class="col-md-1">{{post.pickUpTime}}</td>
											<td class="col-md-1">{{post.employeeAddress}}</td>
											<td class="col-md-1">{{post.employeeAreaName}}</td>
											<td class="col-md-1">{{post.facilityName}}</td>
											<td class="col-md-1">{{post.nodalPointTitle}}</td>
											<td class="col-md-1">{{post.cabAvailable}}</td>
											<td><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg",facilityData)'></td>
											<td class="col-md-1">
												<div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post,facilityData)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div>
											</td>
										</tr>
									</tbody>

								</table>
							</div>
						</div> 
					</div>
					</tab>

					<tab ng-click="getMultiTravelRequest()" ng-show="locationVisible != 'N'">
						<tab-heading>Employee
					Multi Travel Request</tab-heading>
					<div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
						<div class="heading2 heading2_travelDesk row">
							<span class="col-md-3 floatLeft">Employee Multi Travel Request</span>
							<span class="col-md-4 floatLeft"><h5 class="lengthEmployeeDetail">Employee
					Multi Travel Request Summary Report - <span class="badge">{{summaryLengthMultiTravelRequest}} / {{summaryOfTotalMultiTravelRequest}}</span></h5></span>
							<div class="col-md-5 floatRight travelDeskHeadingButtons">
								<efmfm-button img-class="efmfm_approvalButtons_collapse"
									src-url="images/portlet-collapse-icon"
									selected-url="images/portlet-expand-icon"
									hover-url="images/portlet-collapse-icon"
									alt-text="Collapse Window" main-div="employeeList"
									target-div="employeeListContent"> </efmfm-button>
								<efmfm-button img-class="efmfm_approvalButtons_reload"
									src-url="images/portlet-reload-icon"
									selected-url="images/portlet-reload-icon"
									hover-url="images/portlet-reload-icon" alt-text="Reload Window"
									main-div="employeeList" target-div="employeeListContent"
									ng-click='refreshEmployeeMultipleTravelDesk()'> </efmfm-button>
								<efmfm-button img-class="efmfm_dashboarButtons_remove"
									src-url="images/portlet-remove-icon"
									selected-url="images/portlet-remove-icon"
									hover-url="images/portlet-remove-icon" alt-text="Remove Window"
									main-div="employeeList" target-div="employeeListContent">
								</efmfm-button>
							</div>
						</div>
						<div class="employeeListContent">
							<!-- /*FIRST ROW*/-->
							<div class="row paddingLeftRight10">

								<div
									class="col-md-2 col-xs-2">
									<div class="input-group calendarInput">
										<span class="input-group-btn">
											<button class="btn btn-default"
												ng-click="shiftTimeDateCal($event)">
												<i class="icon-calendar calInputIcon"></i>
											</button>
										</span> <input type="text" ng-model="shiftTimeDateMultiTravel"
											class="form-control" placeholder="Shift Time Date"
											datepicker-popup='{{format}}'
											is-open="datePicker.shiftTimeDate" show-button-bar=false
											datepicker-options='dateOptions' name='shiftTimeDate'
											ng-change="getshiftTimeDates()" required readonly>
									</div>
								</div>

								 <div class="col-md-2 col-xs-2">
				                    <div>
				                          <select ng-model="tripTypeMultiTravel"
				                            class="form-control"
				                            ng-options="tripType.text for tripType in tripTypes track by tripType.value"
				                            ng-change="setTripTypeliveTracking(tripTypeMultiTravel, facilityData)"
				                            required
				                            ng-disabled= "!shiftTimeDateMultiTravel"
				                            ng-focus="search.text = null"
				                            >
				                            <option value="">-- Select Trip Type --</option>
				                          </select>
				                    </div>
				                  </div>

				                <div class="col-md-2 col-xs-2">
				                  <div>
				                   <select ng-model="shiftTimesMultiTravel"
					                      class="form-control floatLeft selectShiftTime_serviceMapping"
					                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeData"
					                      required
					                      ng-change = "setShiftButton()"
					                      ng-disabled= "!tripTypeMultiTravel"
				                      >
				                      <option value="">-- Shift Time --</option>
				                    </select>

				                  </div>
				                </div>
								<div
									class="col-md-3">
									<button class="btn btn-success"
										ng-click="setShiftsMultiTravelRequest(shiftTimeDateMultiTravel, tripTypeMultiTravel, shiftTimesMultiTravel, true)"
										ng-disabled="!shiftTimesMultiTravel || !tripTypeMultiTravel">Result</button>

									<button class="btn btn btn-success"
										ng-click="saveAsaExcel(shiftTimeDateMultiTravel, tripTypeMultiTravel, shiftTimesMultiTravel, search.text)"
										ng-disabled="!excelButtonVisible">
										<i class="icon-download-alt"></i> <span class="marginLeft5">Excel</span>
									</button>


									<!-- <button class="btn btn btn-danger"
										ng-click="deleteEmployeeDetails(shiftTimeDateMultiTravel, tripTypeMultiTravel, shiftTimesMultiTravel)"
										ng-disabled="!excelButtonVisible">
										<i class="icon-remove-sign"></i><span class="marginLeft5">Delete</span>
									</button> -->

								</div>


								<!-- <div class="col-md-3 col-xs-2 searchEmployeeTravelDesk">
									<input type="text" class="form-control"
										ng-model="searchEmployeeReported"
										placeholder="Filter by Name, Id, Shift....">
								</div> -->

								<div class="col-md-3 col-xs-3 searchEmployeeTravelDesk">
									<div class="input-group floatLeft calendarInput">
										<input ng-model="search.text" type="text" class="form-control"
											ng-keydown="$event.which === 13 && searchMultipleLocation(search.text)"
											placeholder="Search Employee" maxlength='15'
											ng-focus="searchIsEmpty = false; search.tripType = null; search.shiftTimes = null; shiftTimeDate = null"
											ng-class="{error: searchIsEmpty}"> <span
											class="input-group-btn">
											<button class="btn btn-success"
												ng-click="searchMultipleLocation(search.text)">
												<i class="icon-search searchServiceMappingIcon"></i>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>

					    <!--   /*SECOND ROW*/-->
						<div class="row">
							<div class="wrapper2_EmpTravelDesk" id="employeeTravelDeskExport" ng-show="true">
								<!-- <div class="col-md-4 col-xs-12 col-md-offset-4"
									ng-show="getMultiTravelDeskButtonClicked">
									<img class="spinner02" src="images/spinner02.gif"
										alt="Getting Result..">
								</div> -->
								<div class="" ng-show="multiEmployeeRosterTableAll">
                            <div class="row divTableHeader" style="margin: 0; padding: 0; font-size: 14px; font-weight: 900;" ng-show="postsAllMInit.length == 0">
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-1 col-sm-1">Map</div>
                                        <div class="col-md-2 col-sm-2">View Location Details</div>
                                        <div class="col-md-1 col-sm-1">Employee Id</div>
                                        <div class="col-md-2 col-sm-2">Employee Name</div>
                                        <div class="col-md-2 col-sm-2">Trip Type</div>
                                        <div class="col-md-2 col-sm-2">Request Date</div>
                                        <div class="col-md-2 col-sm-2">Request Type</div>

                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                    	<div class="col-md-2 col-sm-2">Shift Time</div>
                                        <div class="col-md-2 col-sm-2">Route Name</div>
                                        <div class="col-md-2 col-sm-2">Pick/Drop Time</div>
                                        <div class="col-md-2 col-sm-2">Pickup/Drop Location</div>
                                        <div class="col-md-2 col-sm-2">Area Name</div>
                                        <div class="col-md-2 col-sm-2">Facility Name</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-4 col-sm-3">Nodal Points</div>
                                        <div class="col-md-4 col-sm-3">Cab Status</div>
                                        <div class="col-md-4 col-sm-3">Edit</div>
                                        <div class="col-md-4 col-sm-3">Delete</div>
                                    </div>
                            </div>
                            <div class="constrainedEmpRosterAll row margin0">
                                <div class="overflowXauto" infinite-scroll="getMultiTravelRequest()" infinite-scroll-container='".constrainedEmpRosterAll"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0 text-center' ng-repeat="post in postsAllMInit |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}" ng-show="postsAllMInit.length > 0">
                                    	<div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-1 col-sm-1 divTableData">
                                        	<span class="pointer" ng-click = "openMap(post)">
							                        <i class="icon-map-marker mapMarkerIcon ng-scope"
							                           tooltip="View on Map"
							                           tooltip-placement="right"
							                           tooltip-trigger="mouseenter">
							                        </i>
						                    </span>
						                </div>
                                        <div class="col-md-2 col-sm-2 divTableData">
                                        	<input type="button" class="btn btn-success btn-xs" value="View" ng-click="getLocationDetails(post)" name="">
                                        </div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.employeeId}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripType}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripDate}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.requestType}}</div>

                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                    	<div class="col-md-2 col-sm-2 divTableData"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)">
										</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeRouteName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.pickUpTime}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.employeeAddress}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeAreaName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.facilityName}}</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.nodalPointTitle}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.cabAvailable}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData"><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg",facilityData)'></div>
                                        <div class="col-md-3 col-sm-3 divTableData"><div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post,facilityData)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div></div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="postsAllMInit.length == 0">
                                <div class="noData">No data found</div>
                            </div>
                        </div>





                        <div class="" ng-show="multiEmployeeRosterTableAllm">
                            <div class="row divTableHeader" style="margin: 0; padding: 0; font-size: 14px; font-weight: 900;" ng-show="postMultiple.length > 0">
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-1 col-sm-1">Map</div>
                                        <div class="col-md-2 col-sm-2">View Location Details</div>
                                        <div class="col-md-1 col-sm-1">Employee Id</div>
                                        <div class="col-md-2 col-sm-2">Employee Name</div>
                                        <div class="col-md-2 col-sm-2">Trip Type</div>
                                        <div class="col-md-2 col-sm-2">Request Date</div>
                                        <div class="col-md-2 col-sm-2">Request Type</div>

                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                    	<div class="col-md-2 col-sm-2">Shift Time</div>
                                        <div class="col-md-2 col-sm-2">Route Name</div>
                                        <div class="col-md-2 col-sm-2">Pick/Drop Time</div>
                                        <div class="col-md-2 col-sm-2">Pickup/Drop Location</div>
                                        <div class="col-md-2 col-sm-2">Area Name</div>
                                        <div class="col-md-2 col-sm-2">Facility Name</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-4 col-sm-3">Nodal Points</div>
                                        <div class="col-md-4 col-sm-3">Cab Status</div>
                                        <div class="col-md-4 col-sm-3">Edit</div>
                                        <div class="col-md-4 col-sm-3">Delete</div>
                                    </div>
                            </div>
                            <div class="constrainedEmpRosterAll row margin0">
                                <div class="overflowXauto" infinite-scroll="getMultiTravelRequest()" infinite-scroll-container='".constrainedEmpRosterAll"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0 text-center' ng-repeat="post in postMultiple |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}" ng-show="postMultiple.length > 0">
                                    	<div class="col-md-5 col-sm-5 paddingTBLR">
                                        <div class="col-md-1 col-sm-1 divTableData">
                                        	<span class="pointer" ng-click = "openMap(post)">
							                        <i class="icon-map-marker mapMarkerIcon ng-scope"
							                           tooltip="View on Map"
							                           tooltip-placement="right"
							                           tooltip-trigger="mouseenter">
							                        </i>
						                    </span>
						                </div>
                                        <div class="col-md-2 col-sm-2 divTableData">
                                        	<input type="button" class="btn btn-success btn-xs" value="View" ng-click="getLocationDetails(post)" name="">
                                        </div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.employeeId}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripType}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.tripDate}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.requestType}}</div>

                                    </div>
                                    <div class="col-md-5 col-sm-5 paddingTBLR">
                                    	<div class="col-md-2 col-sm-2 divTableData"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)">
										</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeRouteName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.pickUpTime}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.employeeAddress}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeAreaName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.facilityName}}</div>
                                    </div>
                                    <div class="col-md-2 col-sm-2 paddingTBLR">
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.nodalPointTitle}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData">{{post.cabAvailable}}</div>
                                        <div class="col-md-3 col-sm-3 divTableData"><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg",facilityData)'></div>
                                        <div class="col-md-3 col-sm-3 divTableData"><div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post,facilityData)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div></div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="postMultiple.length == 0">
                                <div class="noData">No data found</div>
                            </div>
                        </div>
                        <table class="employeeListTable_TravelDesk table table-hover table-responsive container-fluid"
									ng-show="multipleSearch">
									<thead class="tableHeading">
										<tr>
											<th>Map</th>
											<th>View Location Details</th>
											<th>Employee Id</th>
											<th>Employee Name</th>
											<th>Trip Type</th>
											<th>Request Date</th>
											<th>Request Type</th>
											<th>Shift Time</th>
											<th>Route Name</th>
											<th>Pick/Drop Time</th>
											<th>Pickup/Drop Location</th>
											<th>Area Name</th>
											<th>Nodal Points</th>
											<th>Cab Status</th>
											<th></th>
											<th></th>
											<th></th>
										</tr>
									</thead>

									<tbody ng-show = "posts.length==0">
										<tr>
											<td colspan = '13'>
												<div class = "noData">No Employee has requested for Pick-up or Drop Services</div>
											</td>
										</tr>
									</tbody>

									<tbody ng-show="posts.length>0">
										<tr
											ng-repeat="post in posts |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}">
											<td class="col-md-1">
												<span class="pointer" ng-click = "openMap(post)">
							                        <i class="icon-map-marker mapMarkerIcon ng-scope"
							                           tooltip="View on Map"
							                           tooltip-placement="right"
							                           tooltip-trigger="mouseenter">
							                        </i>
						                        </span>
						                    </td>
						                    <td class="col-md-1">

						                    </td>
											<td class="col-md-1">{{post.employeeId}}</td>
											<td class="col-md-1">{{post.employeeName}}</td>
											<td class="col-md-1">{{post.tripType}}</td>
											<td class="col-md-1">{{post.tripDate}}</td>
											<td class="col-md-1">{{post.requestType}}</td>


											<td class="col-md-1"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)">
											</td>
											<td class="col-md-1">{{post.employeeRouteName}}</td>
											<td class="col-md-1">{{post.pickUpTime}}</td>
											<td class="col-md-1">{{post.employeeAddress}}</td>
											<td class="col-md-1">{{post.employeeAreaName}}</td>
											<td class="col-md-1">{{post.nodalPointTitle}}</td>
											<td class="col-md-1">{{post.cabAvailable}}</td>
											<td><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg")'></td>
											<td class="col-md-1">
												<div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div>
											</td>
										</tr>
									</tbody>

								</table>
							</div>
						</div>
					</div>
					</tab>

					<tab ng-click="getTravelDeskAdhoc()" ng-show="branchCode == 'SBOManila'">
						 <tab-heading>Adhoc
					Requests Details</tab-heading>
					<div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
						<div class="heading2 heading2_travelDesk row">
							<span class="col-md-3 floatLeft">Adhoc Requests Details</span>
							<span class="col-md-4 floatLeft"><h5 class="lengthEmployeeDetail">Adhoc
					Requests Details Summary Report - <span class="badge">{{summaryOfAdhocRequest}} / {{summaryOfTotalAdhocRequest}}</span></h5></span>
							<div class="col-md-5 floatRight travelDeskHeadingButtons">
								<efmfm-button img-class="efmfm_approvalButtons_collapse"
									src-url="images/portlet-collapse-icon"
									selected-url="images/portlet-expand-icon"
									hover-url="images/portlet-collapse-icon"
									alt-text="Collapse Window" main-div="employeeList"
									target-div="employeeListContent"> </efmfm-button>
								<efmfm-button img-class="efmfm_approvalButtons_reload"
									src-url="images/portlet-reload-icon"
									selected-url="images/portlet-reload-icon"
									hover-url="images/portlet-reload-icon" alt-text="Reload Window"
									main-div="employeeList" target-div="employeeListContent"
									ng-click='refreshEmployeeTravelDesk()'> </efmfm-button>
								<efmfm-button img-class="efmfm_dashboarButtons_remove"
									src-url="images/portlet-remove-icon"
									selected-url="images/portlet-remove-icon"
									hover-url="images/portlet-remove-icon" alt-text="Remove Window"
									main-div="employeeList" target-div="employeeListContent">
								</efmfm-button>
							</div>
						</div>
						<div class="employeeListContent">
							<!-- /*FIRST ROW*/-->
							<div class="row paddingLeftRight10">

								<div
									class="col-md-2 col-xs-2 searchEmployeeTravelDesk getShiftResult paddingTop10 paddingBottom10">
									<div class="input-group calendarInput">
										<span class="input-group-btn">
											<button class="btn btn-default"
												ng-click="shiftTimeDateCal($event)">
												<i class="icon-calendar calInputIcon"></i>
											</button>
										</span> <input type="text" ng-model="shiftTimeDateAdhoc"
											class="form-control" placeholder="Shift Time Date"
											datepicker-popup='{{format}}'
											is-open="datePicker.shiftTimeDate" show-button-bar=false
											datepicker-options='dateOptions' name='shiftTimeDate'
											ng-change="getshiftTimeDatesData()" required readonly>
									</div>
								</div>
									 <div class="col-md-2 col-xs-2 shiftTimeSelect getShiftResult paddingTop10 paddingBottom10">
				                    <div>
				                          <select ng-model="tripTypeMultiTravel"
				                            class="form-control"
				                            ng-options="tripType.text for tripType in tripTypes track by tripType.value"
				                            ng-change="setTripTypeliveTracking(tripTypeMultiTravel,facilityData)"
				                            required
				                            ng-disabled= "!shiftTimeDateAdhoc"
				                            ng-focus="search.text = null"
				                            >
				                          </select>
				                    </div>
				                  </div>
								<div
									class="col-md-2 col-xs-2 shiftTimeSelect getShiftResult paddingTop10 paddingBottom10">
									<!-- <select ng-model="selectShiftsAdhoc"
										class="form-control travelDeskSelect"
										ng-options="shiftTime for shiftTime in shiftsTimeData | orderBy:shiftTime">
										<option value="">Show ALL Shift Time</option>
									</select> -->
									<select ng-model="selectShiftsAdhoc"
					                      class="form-control travelDeskSelect"
					                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeData"
					                      required
					                      ng-change = "setShiftButton()"

				                      >
				                      <option value="">Shift Time</option>
				                    </select>
								</div>

								<div
									class="col-md-2 co-xs-2  getShiftResult paddingTop10 paddingBottom10 paddingLeft0">
									<button class="btn btn-success marginLeft10 buttonRadius0"
										ng-disabled="!selectShiftsAdhoc"
										ng-click="setShiftsAdhoc(selectShiftsAdhoc, shiftTimeDateAdhoc , tripTypeMultiTravel, true)">Result</button>
									<button class="btn btn btn-success marginLeft10 buttonRadius0"
										ng-click="saveInExcel()"
										ng-disabled="!selectShifts || !isLoaded">
										<i class="icon-download-alt"></i> <span class="marginLeft5">Excel</span>
									</button>
								</div>

						<!-- 		<div class="col-md-1 col-xs-1 searchEmployeeTravelDesk"></div> -->
								<div class="col-md-2 col-xs-2 searchEmployeeTravelDesk getShiftResult paddingTop10 paddingBottom10" >
									<input type="text" class="form-control"
										ng-model="searchEmployeeReported"
										placeholder="Filter by Name, Id, Shift....">
								</div>
								<div class="col-md-2 col-xs-2 searchEmployeeTravelDesk getShiftResult paddingTop10 paddingBottom10">
									<div class="input-group floatLeft calendarInput">
										<input ng-model="search.text" type="text" class="form-control"
											ng-keydown="$event.which === 13 && searchEmployee(search.text)"
											placeholder="Search Employee" maxlength='15'
											ng-focus="searchIsEmpty = false"
											ng-class="{error: searchIsEmpty}"> <span
											class="input-group-btn">
											<button class="btn btn-success"
												ng-click="searchEmployee(search.text)">
												<i class="icon-search searchServiceMappingIcon"></i>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
						<!--   /*SECOND ROW*/-->

						<div class="" ng-show="adhocInitTable" style="overflow: auto;">
                            <div class="row divTableHeader" style="margin: 0; width: 150%; padding: 0; font-size: 14px; font-weight: 900;" ng-show="postsAdhocInit.length > 0">

                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Passenger Id</div>
                                        <div class="col-md-2 col-sm-2">Passenger Name</div>
                                        <div class="col-md-2 col-sm-2">Booked By</div>
                                        <div class="col-md-2 col-sm-2">Charged To</div>
                                        <div class="col-md-1 col-sm-1">Shift Time</div>
                                        <div class="col-md-1 col-sm-1">Route Name</div>
                                        <div class="col-md-2 col-sm-2">Account Name</div>
                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Contact No</div>
                                        <div class="col-md-2 col-sm-2">Pickup Date</div>
                                        <div class="col-md-2 col-sm-2">Date Reserved</div>
                                        <div class="col-md-1 col-sm-1">Pickup Time</div>
                                        <div class="col-md-2 col-sm-2">Pickup Location</div>
                                        <div class="col-md-2 col-sm-2">Destinations</div>
                                        <div class="col-md-1 col-sm-1">Trip Type</div>
                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Duration</div>
                                        <div class="col-md-2 col-sm-2">Cash/Card Edit</div>
                                        <div class="col-md-2 col-sm-2">Remarks</div>
                                        <div class="col-md-1 col-sm-1">Edit</div>
                                        <div class="col-md-2 col-sm-2">Delete</div>
                                        <div class="col-md-1 col-sm-1">Accept</div>
                                        <div class="col-md-2 col-sm-2">Reject</div>
                                    </div>
                            </div>
                            <div class="constrainedAdhocInit row margin0" style="background: white; width: 150%;">
                                <div class="overflowXauto" infinite-scroll="getTravelDeskAdhoc()" infinite-scroll-container='".constrainedAdhocInit"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0 text-center' ng-repeat="post in postsAdhocInit |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}">
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeId}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.bookedBy}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.chargedTo}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)"></div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.employeeRouteName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.accountName}}</div>

                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.mobileNumber}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.startDate}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.endDate}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.pickUpTime}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.originAddress}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.destinationAddress}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.tripType}}</div>

                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.durationInHours}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">Credit Card</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.remarks}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">
                                        	<input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg")'></div>
                                        <div class="col-md-2 col-sm-2 divTableData">
                                        	<div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div></div>
                                        <div class="col-md-1 col-sm-1 divTableData"  ng-show="post.requestButtonStatus == 'N'"><input type="button" class="btn btn-success btn-xs" value="Accept" name="" ng-click="achocRequestAccept(post, $index)" ng-disabled="post.disabled"></div>
                                        <div class="col-md-1 col-sm-1 divTableData"  ng-show="post.requestButtonStatus == 'Y'"><input type="button" class="btn btn-success btn-xs" value="Accept" name="" ng-disabled = "true"></div>
                                        <div class="col-md-2 col-sm-2 divTableData"><input type="button" class="btn btn-danger btn-xs" value="Reject" name="" ng-click="achocRequestReject(post, $index)"></div>

                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="postsAdhocInit.length == 0">
                                <div class="noData">No data Found</div>
                            </div>
                        </div>


                        <div class="" ng-show="adhocTable" style="overflow: auto;">
                            <div class="row divTableHeader" style="margin: 0; width: 150%; padding: 0; font-size: 14px; font-weight: 900;" ng-show="postsAdhoc.length > 0">

                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Passenger Id</div>
                                        <div class="col-md-2 col-sm-2">Passenger Name</div>
                                        <div class="col-md-2 col-sm-2">Booked By</div>
                                        <div class="col-md-2 col-sm-2">Charged To</div>
                                        <div class="col-md-1 col-sm-1">Shift Time</div>
                                        <div class="col-md-1 col-sm-1">Route Name</div>
                                        <div class="col-md-2 col-sm-2">Account Name</div>
                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Contact No</div>
                                        <div class="col-md-2 col-sm-2">Pickup Date</div>
                                        <div class="col-md-2 col-sm-2">Date Reserved</div>
                                        <div class="col-md-1 col-sm-1">Pickup Time</div>
                                        <div class="col-md-2 col-sm-2">Pickup Location</div>
                                        <div class="col-md-2 col-sm-2">Destinations</div>
                                        <div class="col-md-1 col-sm-1">Trip Type</div>
                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2">Duration</div>
                                        <div class="col-md-2 col-sm-2">Cash/Card Edit</div>
                                        <div class="col-md-2 col-sm-2">Remarks</div>
                                        <div class="col-md-1 col-sm-1">Edit</div>
                                        <div class="col-md-2 col-sm-2">Delete</div>
                                        <div class="col-md-1 col-sm-1">Accept</div>
                                        <div class="col-md-2 col-sm-2">Reject</div>
                                    </div>
                            </div>
                            <div class="constrainedAdhoc row margin0"  style="background: white; width: 150%;">
                                <div class="overflowXauto" infinite-scroll="setShiftsAdhoc(selectShiftsAdhoc, shiftTimeDateAdhoc , tripTypeMultiTravel)" infinite-scroll-container='".constrainedAdhoc"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0 text-center' ng-repeat="post in postsAdhoc |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}">
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeId}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.employeeName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.bookedBy}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.chargedTo}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)"></div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.employeeRouteName}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.accountName}}</div>

                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.mobileNumber}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.startDate}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.endDate}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.pickUpTime}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.originAddress}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.destinationAddress}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">{{post.tripType}}</div>

                                    </div>
                                    <div class="col-md-4 col-sm-4 paddingTBLR">
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.durationInHours}}</div>
                                        <div class="col-md-2 col-sm-2 divTableData">Credit Card</div>
                                        <div class="col-md-2 col-sm-2 divTableData">{{post.remarks}}</div>
                                        <div class="col-md-1 col-sm-1 divTableData">
																					<button class='btn btn-warning btn-sm' ng-click='editTravelDesk(post, $index, "lg")'>
																					<i class="icon-edit"></i></button>
																				</div>
                                        <div class="col-md-2 col-sm-2 divTableData">
                                        	<div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div></div>
                                        <div class="col-md-1 col-sm-1 divTableData"  ng-show="post.requestButtonStatus == 'N'"><input type="button" class="btn btn-success btn-xs" value="Accept" name="" ng-click="achocRequestAccept(post, $index)" ng-disabled="post.disabled"></div>
                                        <div class="col-md-1 col-sm-1 divTableData"  ng-show="post.requestButtonStatus == 'Y'"><input type="button" class="btn btn-success btn-xs" value="Accept" name="" ng-disabled = "true"></div>
                                        <div class="col-md-2 col-sm-2 divTableData"><input type="button" class="btn btn-danger btn-xs" value="Reject" name="" ng-click="achocRequestReject(post, $index)"></div>

                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="postsAdhoc.length == 0">
                                <div class="noData">No data Found</div>
                            </div>
                        </div>

						<div class="row">
							<div class="wrapper2_EmpTravelDesk" id="employeeTravelDeskExport">
								<table
									class="employeeListTable_TravelDesk table table-hover table-responsive container-fluid"
									ng-show="adhocSearch">
									<thead class="tableHeading">
										<tr>
											<th>Passenger Id</th>
											<th>Passenger Name</th>
											<th>Booked By</th>
											<th>Charged To</th>
											<th>Shift Time</th>
											<th>Route Name</th>
											<th>Account Name</th>
											<th>Contact No</th>
											<th>Pickup Date</th>
											<th>Date Reserved</th>
											<th>Pickup Time</th>

											<th>Trip Type</th>
											<th>Pickup Location</th>
											<th>Destinations</th>
											<th>Duration</th>
											<th>Cash/Card Edit</th>
											<th>Remarks</th>
											<th>Edit</th>
											<th>Delete</th>
											<th>Accept</th>
											<th>Reject</th>
										</tr>
									</thead>
									                                <tbody ng-show = "posts.length==0">
                                    <tr>
                                        <td colspan = '13'>
                                            <div class = "noData">No Employee has requested for Pick-up or Drop Services</div>
                                        </td>
                                    </tr>
                                </tbody>

									<tbody ng-show="posts.length>0">
										<tr
											ng-repeat="post in posts |limitTo: numberofRecords | filter : searchEmployeeReported"
											class="employee{{post.employeeId}}{{post.requestId}}">
											<td class="col-md-1">{{post.employeeId}}</td>
											<td class="col-md-1">{{post.employeeName}}</td>
											<td class="col-md-1">{{post.bookedBy}}</td>
											<td class="col-md-1">{{post.chargedTo}}</td>
											<td class="col-md-1"><span
												ng-show="!post.isUpdateClicked">{{post.tripTime}}</span> <timepicker
													ng-model="post.createNewAdHocTime" hour-step="hstep"
													minute-step="mstep" show-meridian="ismeridian"
													readonly-input='true' class="timepicker2_empReq floatLeft"
													ng-show="post.isUpdateClicked"> </timepicker> <input
												type="button" class="btn btn-xs btn-warning" value="Update"
												ng-click="updatePickupTime(post)"></td>
											<td class="col-md-1">{{post.employeeRouteName}}</td>
											<td class="col-md-1">{{post.accountName}}</td>

											<td class="col-md-1">{{post.mobileNumber}}</td>
											<td class="col-md-1">{{post.startDate}}</td>
											<td class="col-md-1">{{post.endDate}}</td>
											<td class="col-md-1">{{post.pickUpTime}}</td>


											<td class="col-md-1">{{post.tripType}}</td>
											<td class="col-md-1">{{post.originAddress}}</td>
											<td class="col-md-1">{{post.destinationAddress}}</td>
											<td class="col-md-1">{{post.durationInHours}}</td>
											<td class="col-md-1">Credit Card</td>
											<td class="col-md-1">{{post.remarks}}</td>



											<td><input type='button'
												class='btn btn-warning btn-sm form-control' value='Edit'
												ng-click='editTravelDesk(post, $index, "lg")'></td>
											<td class="col-md-1">
												<div class="deletebutton_empTravelDesk pointer"
													ng-click="deleteEmployee(post)">
													<i class="delete-employee_TravelDesk icon-remove-sign"></i>
												</div>
											</td>
											<td class="col-md-1" ng-show="post.requestButtonStatus == 'N'">
												<input type="button" class="btn btn-success btn-xs" value="Accept" name="" ng-click="achocRequestAccept(post, $index)" ng-disabled="post.disabled">
											</td>
											<td class="col-md-1" ng-show="post.requestButtonStatus == 'Y'">
												<input type="button" class="btn btn-success btn-xs" value="Accept" name="" ng-disabled = "true">
											</td>
											<td class="col-md-1">
												<input type="button" class="btn btn-danger btn-xs" value="Reject" name="" ng-click="achocRequestReject(post, $index)">
											</td>
										</tr>
									</tbody>

								</table>
							</div>
						</div>
					</div>

					</tab> </tabset>
				</div>
			</div>
		</div>
	</div>
</div>
