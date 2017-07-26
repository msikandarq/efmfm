<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    This page show the DRIVER, VENDOR, VEHICLE approved, rejected, waiting for approval and some detail
@State          home.approval 
@URL            /approval 
  
CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->  

<div class="approvalTemplate container-fluid" ng-if="IsApprovalActive">
	<div class="row"> 
		<div class="col-md-12 col-xs-12 heading1"> 
			<span class="col-md-4 col-xs-12">Approval</span>
			<div class="tags_Approval col-md-8 col-xs-12">
				<!-- <div class="floatRight" ng-click="scrollTo('invoice')">
					<span>Contract Types</span>
				</div> --> 
				<div class="floatRight" ng-click="scrollTo('invoice')">
					<span>Invoice</span>
				</div>
				<div class=" floatRight" ng-click="scrollTo('report')">
					<span>Report</span>
				</div>
				<div class=" floatRight" ng-click="scrollTo('vendor')">
					<span>Vendor</span>	
				</div>
				<div class=" floatRight" ng-click="scrollTo('supervisor')">
					<span>Supervisors</span>
				</div>
				<div class=" floatRight" ng-click="scrollTo('vehicle')">
					<span>Vehicle</span>
				</div>
				<div class="floatRight" ng-click="scrollTo('driver')">
					<span>Driver</span>
				</div>
				<div class="floatRight" ng-show="multiFacility == 'Y'" style="margin-top: -6px; margin-right: 40px;">
					<am-multiselect class="input-lg dynamicDaysInput facilityDropdown"
                                   multiple="true"
                                   ms-selected ="{{facilityData.listOfFacility.length}} Facility(s) Selected"
                                   ng-model="facilityData.listOfFacility"
                                   ms-header="All Facility"
                                   options="facility.branchId as facility.name for facility in facilityDetails"
                                   >
                            </am-multiselect>
                    <input type="button" class="btn btn-success" value="Submit" name="" ng-click="setFacilityDetails(facilityData.listOfFacility)">
				</div>
				
				
			</div>
			<div class="col-md-12 col-xs-12 mainTabDiv_Approval">
				<!--   /*START OF WRAPPER1 = DRIVER*/ -->
				<div class="wrapper1" id="driverContent">
					<div class="heading2 row">
						<span class="col-md-7 floatLeft" id="driver">Driver</span>
						<div class="col-md-5 floatRight">
							<efmfm-button img-class="efmfm_approvalButtons_collapse"
								src-url="images/portlet-collapse-icon"
								selected-url="images/portlet-expand-icon"
								hover-url="images/portlet-collapse-icon"
								alt-text="Collapse Window" main-div="driverContent"
								target-div="driverTab_Approval"> </efmfm-button>
							<efmfm-button img-class="efmfm_approvalButtons_reload"
								src-url="images/portlet-reload-icon"
								selected-url="images/portlet-reload-icon"
								hover-url="images/portlet-reload-icon" alt-text="Reload Window"
								main-div="driverContent" target-div="driverTab_Approval"
								ng-click='refreshDriverApproval()'> </efmfm-button>
							<efmfm-button img-class="efmfm_dashboarButtons_remove"
								src-url="images/portlet-remove-icon"
								selected-url="images/portlet-remove-icon"
								hover-url="images/portlet-remove-icon" alt-text="Remove Window"
								main-div="driverContent" target-div="driverTab_Approval">
							</efmfm-button>
						</div>
					</div>
					<tabset class="tabset driverTab_Approval"> <tab
						ng-click="getPendingDriver(facilityData.listOfFacility); getTabInfo('pendingDriver')"> <tab-heading>Pending
					Driver</tab-heading>
					<div class="pendingTabContent row">
						<table class="pendingTable table table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Driver Id</th>
									<th>Driver Name</th>
									<th>Driver Number</th>
									<th>Vendor Name</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="driversPendingData.length==0">
								<tr>
									<td colspan='6'>
										<div class="noData">There are no Drivers Pending</div>
									</td>
								</tr>
							</tbody>

							<tbody ng-show="driversPendingData.length>0">
								<tr class="rowSeen"
									ng-repeat-start="post in driversPendingData | filter:searchtextDriver"
									ng-class="{driverRowExpanded:post.isClicked}">
									<td>{{post.driverId}}</td>
									<td>{{post.name}}</td>

									<td>{{post.mobileNumber}}</td>
									<td>{{post.vendorName}}</td>
									<td>{{post.facilityName}}</td>
									<td style="width: 165px;"><input ng-show="!post.isClicked"
										class="viewButton btn btn-primary btn-xs buttonRadius0"
										type="button" ng-class="{{post.driverId}}"
										ng-click="viewDriver(post, $index)" value="View Detail">
										<input ng-show="post.isClicked"
										class="approvalButton btn btn-success btn-xs buttonRadius0"
										type="button" ng-class="{{post.driverId}}"
										ng-click="approveDriver(post, $index)" value="Approve"
										ng-disabled="supervisorRole"> <input
										ng-show="post.isClicked"
										class="rejectButton btn btn-danger btn-xs buttonRadius0"
										type="button" ng-click="rejectDriver(post, $index)"
										value="Reject" ng-disabled="supervisorRole"></td>
								</tr>

								<tr ng-show="post.isClicked" ng-repeat-end=""
									ng-class="{{post.driverId}}" class="driverPendingDetail">
									<td colspan="5">
										<div class="row marginRow">
											<div class="col-md-9 marginRow padding0">
												<div class="row marginRow driverInfo_approval">
													<div class="col-md-3 Drivelabel">
														<span>Driver Name</span><br>
														<span>{{driverDetail[0].name}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Date of Birth</span><br>
														<span>{{driverDetail[0].dob}}</span>
													</div>

													<div class="col-md-3 Drivelabel" ng-hide="driverDetail[0].fatherName == null">
														<span>Father Name</span><br>
														<span>{{driverDetail[0].fatherName}}</span>
													</div>

													<div class="col-md-3 Drivelabel" ng-hide="driverDetail[0].address == ''">
														<span>Driver Address</span><br>
														<span>{{driverDetail[0].address}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Driver Mobile Number</span><br>
														<span>{{driverDetail[0].mobileNumber}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vendor Name</span><br>
														<span>{{driverDetail[0].vendorName}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Driver License Number</span><br>
														<span>{{driverDetail[0].licenceNum}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>License Valid Date</span><br>
														<span>{{driverDetail[0].licenceValid}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Medical Valid Date</span><br>
														<span>{{driverDetail[0].medicalValid}}</span>
													</div>

													<div class="col-md-3 Drivelabel" ng-hide="driverDetail[0].policeVerification == notdone">
														<span>Police Verification</span><br>
														<span>{{driverDetail[0].policeVerification}}</span>
													</div>

												</div>
											</div>
											<div class="col-md-3">
												<img src="images/portlet-remove-icon.png"
													class="floatRight pointer" ng-click="closeDriver(post)">
												<div class="col-md-12">
													<div ng-repeat="doc in driverDetail[0].documents"
														class="floatLeft imageDiv_approval" id="div{{$index}}">
														<i class="icon-remove-sign pointer"
															ng-click="deleteDocument($index)"></i> <a
															href="{{doc.location}}" class="noLinkLine"> <img
															class="img-responsive img-rounded fileImg"
															alt="Responsive image" ng-src="{{doc.location}}">
															<div class="docName">
																<span>{{doc.type}}</span>
															</div>
														</a>
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
							</tbody>

						</table>
					</div>
					</tab> <tab ng-click="getRegisteredDriver(facilityData.listOfFacility); getTabInfo('registeredDriver')"> <tab-heading>Registered
					Driver</tab-heading>
					<div class="registeredTabContent row">
						<table
							class="registeredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Driver Id</th>
									<th>Driver Name</th>
									<th>Driver Number</th>
									<th>Join Date</th>
									<th>Vendor Name</th>
									<th>Facility Name</th>	
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="driversRegisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Drivers for
											Registration</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="driversRegisterData.length>0">
								<tr
									ng-repeat="post in driversRegisterData | filter:searchtextDriver">
									<td>{{post.driverId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.mobileNumber}}</td>
									<td>{{post.joinDate}}</td>
									<td>{{post.vendorName}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="removeButton btn btn-warning btn-xs buttonRadius0"
										type="button" ng-click="removeDriver(post, $index)"
										value="Remove"><br/>
										<input
										class="removeButton btn btn-danger btn-xs buttonRadius0"
										type="button" ng-click="noEntryDriver(post, $index)"
										value="No Entry">
										</td>
								</tr>

							</tbody>
						</table>
					</div>
					</tab> 
					<tab ng-click="getUnRegisteredDriver(facilityData.listOfFacility); getTabInfo('unregisteredDriver')"> <tab-heading>Unregistered
					Driver</tab-heading>
					<div class="unregisteredTabContent row">
						<table
							class="unregisteredTable table table-hover table-responsive container-fluid">
							<div>
                                <button class="btn btn-success floatRight" ng-click="unregisteredDriverExcelDownload(facilityData.listOfFacility)">Excel</button>
                            </div>
							<thead class="tableHeading">
								<tr>
									<th>Driver Id</th>
									<th>Driver Name</th>
									<th>Driver Number</th>
									<th>Vendor Name</th>
									<th>Facility Name</th>
									<th>Join Date</th>
									<th>Relieving Date</th>
									<th>Removal Remarks</th>
									<th>Action</th>
									
								</tr>
							</thead>
							<tbody ng-show="driversUnregisterData.length==0">
								<tr>
									<td colspan='9'>
										<div class="noData">There are no Drivers for
											Registration</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="driversUnregisterData.length>0">
								<tr
									ng-repeat="post in driversUnregisterData | filter:searchtextDriver">
									<td>{{post.driverId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.mobileNumber}}</td>
									<td>{{post.vendorName}}</td>
									<td>{{post.facilityName}}</td>
									<td>{{post.joinDate}}</td>
									 <td>{{post.relievingDate}}</td>
								     <td>{{post.remarks}}</td>
									 
									<td><input
										class="addagainButton btn btn-info btn-xs buttonRadius0"
										type="button" ng-click="addagainDriver(post, $index)"
										value="Add Again"></td>
									
								</tr>
							</tbody>
						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Drivers" ng-model="searchtextDriver"
						expect_special_char>
					</tabset>
				</div>
				<div class="clearfix"></div>
				<br>
				<!--    /*END OF WRAPPER1 = DRIVER*/
                 /*START OF WRAPPER2 = VEHICLE*/ -->
				<div class="wrapper1" id="vehicleContent">
					<div class="heading2 row">
						<span class="col-md-7 floatLeft" id="vehicle">Vehicle</span>
						<div class="col-md-5 floatRight">
							<efmfm-button img-class="efmfm_approvalButtons_collapse"
								src-url="images/portlet-collapse-icon"
								selected-url="images/portlet-expand-icon"
								hover-url="images/portlet-collapse-icon"
								alt-text="Collapse Window" main-div="vehicleContent"
								target-div="vehicleTab_Approval"> </efmfm-button>
							<efmfm-button img-class="efmfm_approvalButtons_reload"
								src-url="images/portlet-reload-icon"
								selected-url="images/portlet-reload-icon"
								hover-url="images/portlet-reload-icon" alt-text="Reload Window"
								main-div="vehicleContent" target-div="vehicleTab_Approval"
								ng-click='refreshVehicleApproval()'> </efmfm-button>
							<efmfm-button img-class="efmfm_dashboarButtons_remove"
								src-url="images/portlet-remove-icon"
								selected-url="images/portlet-remove-icon"
								hover-url="images/portlet-remove-icon" alt-text="Remove Window"
								main-div="vehicleContent" target-div="vehicleTab_Approval">
							</efmfm-button>
						</div>
					</div>
					<tabset class="tabset vehicleTab_Approval"> <tab
						ng-click="getPendingVehicle(facilityData.listOfFacility); getTabInfo('pendingVehicle')"> <tab-heading>Pending
					Vehicle</tab-heading>
					<div class="pendingTabContent row">
						<table class="pendingTable table table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Vehicle Id</th>
									<th>Vehicle Vendor Name</th>
									<th>Vehicle Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="vehiclesPendingData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Vehicles Pending</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="vehiclesPendingData.length>0">
								<tr class="rowSeen" 
									ng-repeat-start="post in vehiclesPendingData | filter:searchtextVehicle"
									ng-class="{vehicleRowExpanded:post.isClicked}">
									<td>{{post.vehicleId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.vehicleNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input ng-show="!post.isClicked"
										class="viewButton btn btn-primary btn-xs buttonRadius0"
										type="button" ng-click="viewVehicle(post, $index)"
										value="View Detail"> <input ng-show="post.isClicked"
										class="approvalButton btn btn-success btn-xs buttonRadius0"
										type="button" ng-class="{{post.vehicleId}}"
										ng-click="approveVehicle(post, $index)" value="Approve"
										ng-disabled="supervisorRole"> <input
										ng-show="post.isClicked"
										class="rejectButton btn btn-danger btn-xs buttonRadius0"
										type="button" ng-click="rejectVehicle(post, $index)"
										value="Reject" ng-disabled="supervisorRole"></td>
								</tr>
								<tr ng-show="post.isClicked" ng-repeat-end=""
									ng-class="vehicle{{post.vehicleId}}"
									class="driverPendingDetail unvisibleRowVehiclePending">
									<td colspan="5">
										<div class="row marginRow">
											<div class="col-md-9 marginRow padding0">
												<div class="row marginRow driverInfo_approval">
													<div class="col-md-3 Drivelabel">
														<span>Vehicle Id</span><br>
														<span>{{vehicleDetail[0].vehicleId}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vehicle Number</span><br>
														<span>{{vehicleDetail[0].vehicleNumber}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vendor Name</span><br>
														<span>{{vehicleDetail[0].vendorName}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vehicle Make</span><br>
														<span>{{vehicleDetail[0].vehicleMake}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vehicle Model</span><br>
														<span>{{vehicleDetail[0].vehicleModel}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vehicle Capacity</span><br>
														<span>{{vehicleDetail[0].vehicleCapacity}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vehicle Engine Number</span><br>
														<span>{{vehicleDetail[0].vehicleEngineNum}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vehicle Registration Number</span><br>
														<span>{{vehicleDetail[0].vehicleRegistrationNum}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Insurrance Valid Date</span><br>
														<span>{{vehicleDetail[0].insuranceValid|date :
															'shortDate'}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Permit Valid Date</span><br>
														<span>{{vehicleDetail[0].permitValid|date :
															'shortDate'}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Tax Certification</span><br>
														<span>{{vehicleDetail[0].taxCertificate|date :
															'shortDate'}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Pollution Valid</span><br>
														<span>{{vehicleDetail[0].polutionValid|date :
															'shortDate'}}</span>
													</div>

												</div>
											</div>
											<div class="col-md-3">
												<img src="images/portlet-remove-icon.png"
													class="floatRight pointer" ng-click="closeDriver(post)">
												<div class="col-md-12">
													<div ng-repeat="doc in vehicleDetail[0].documents"
														class="floatLeft imageDiv_approval" id="div{{$index}}">
														<i class="icon-remove-sign pointer"
															ng-click="deleteDocument($index)"></i> <a
															href="{{doc.location}}" class="noLinkLine"> <img
															class="img-responsive img-rounded fileImg"
															alt="Responsive image" ng-src="{{doc.location}}">
															<div class="docName">
																<span>{{doc.type}}</span>
															</div>
														</a>
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
							</tbody>

						</table>
					</div>
					</tab> <tab ng-click="getRegisteredVehicle(facilityData.listOfFacility); getTabInfo('registeredVehicle')"> <tab-heading>Registered
					Vehicle</tab-heading>
					<div class="registeredTabContent row">
						<table
							class="registeredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Vehicle Id</th>
									<th>Vehicle Vendor Name</th>
									<th>Vehicle Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="vehiclesRegisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Vehicles for
											Registration</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="vehiclesRegisterData.length>0">
								<tr
									ng-repeat="post in vehiclesRegisterData | filter:searchtextVehicle">
									<td>{{post.vehicleId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.vehicleNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="removeButton btn btn-warning btn-xs buttonRadius0"
										type="button" ng-click="removeVehicle(post, $index)"
										value="Remove"><br/>
										<input
										class="removeButton btn btn-danger btn-xs buttonRadius0"
										type="button" ng-click="noEntryVehicle(post, $index)"
										value="No Entry">
										</td>

								</tr>
							</tbody>
						</table>
					</div>
					</tab> <tab ng-click="getUnRegisteredVehicle(facilityData.listOfFacility); getTabInfo('unregisteredVehicle')"> <tab-heading>Unregistered
					Vehicle</tab-heading>
					<div class="unregisteredTabContent row">
						<table
							class="unregisteredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Vehicle Id</th>
									<th>Vehicle Vendor Name</th>
									<th>Vehicle Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="vehiclesUnregisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Unregistered Vehicles</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="vehiclesUnregisterData.length>0">
								<tr
									ng-repeat="post in vehiclesUnregisterData | filter:searchtextVehicle">
									<td>{{post.vehicleId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.vehicleNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="addagainButton btn btn-info btn-xs buttonRadius0"
										type="button" ng-click="addagainVehicle(post, $index)"
										value="Add Again"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Vehicles" ng-model="searchtextVehicle"
						expect_special_char>
					</tabset>
				</div>
				<div class="clearfix"></div>
				<br>
				<!--   /*END OF WRAPER2 = VEHICLE*/ 
              /*START OF WRAPPER3 = VENDOER*/ -->
				<div class="wrapper1" id="vendorContent">
					<div class="heading2 row">
						<span class="col-md-7 floatLeft" id="vendor">Vendor</span>
						<div class="col-md-5 floatRight">
							<efmfm-button img-class="efmfm_approvalButtons_collapse"
								src-url="images/portlet-collapse-icon"
								selected-url="images/portlet-expand-icon"
								hover-url="images/portlet-collapse-icon"
								alt-text="Collapse Window" main-div="vendorContent"
								target-div="vendorTab_Approval"> </efmfm-button>
							<efmfm-button img-class="efmfm_approvalButtons_reload"
								src-url="images/portlet-reload-icon"
								selected-url="images/portlet-reload-icon"
								hover-url="images/portlet-reload-icon" alt-text="Reload Window"
								main-div="vendorContent" target-div="vendorTab_Approval"
								ng-click='refreshVendorApproval()'> </efmfm-button>
							<efmfm-button img-class="efmfm_dashboarButtons_remove"
								src-url="images/portlet-remove-icon"
								selected-url="images/portlet-remove-icon"
								hover-url="images/portlet-remove-icon" alt-text="Remove Window"
								main-div="vendorContent" target-div="vendorTab_Approval">
							</efmfm-button>
						</div>
					</div>
					<tabset class="tabset vendorTab_Approval"> 
					<tab ng-click="getPendingVendor(facilityData.listOfFacility); getTabInfo('pendingVendor')"> <tab-heading>Pending
					Vendor</tab-heading>
					<div class="pendingTabContent row">
						<table
							class="pendingTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Vendor Id</th>
									<th>Vendor Name</th>
									<th>Vendor Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="vendorPendingData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Vendors Pending</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="vendorPendingData.length>0">
								<tr
									ng-repeat-start="post in vendorPendingData | filter:searchtextVendor"
									class="rowSeen" ng-class="{vendorRowExpanded:post.isClicked}">
									<td>{{post.vendorId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.mobileNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input ng-show="!post.isClicked"
										class="viewButton btn btn-primary btn-xs buttonRadius0"
										type="button" ng-click="viewVendor(post, $index)"
										value="View Detail"> <input ng-show="post.isClicked"
										class="approvalButton btn btn-success btn-xs buttonRadius0"
										type="button" ng-class="{{post.vendorId}}"
										ng-click="approveVendor(post, $index)" value="Approve"
										ng-disabled="supervisorRole"> <input
										ng-show="post.isClicked"
										class="rejectButton btn btn-danger btn-xs buttonRadius0"
										type="button" ng-click="rejectVendor(post, $index)"
										value="Reject" ng-disabled="supervisorRole"></td>
								</tr>
								
								<tr ng-show="post.isClicked" ng-repeat-end=""
									ng-class="vendor{{post.vendorId}}" class="driverPendingDetail">
									<td colspan="5">
										<div class="row marginRow">
											<div class="col-md-9 marginRow padding0">
												<div class="row marginRow driverInfo_approval">
													<div class="col-md-3 Drivelabel">
														<span>Vendor Id</span><br>
														<span>{{vendorDetail[0].vendorId}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vendor Name</span><br>
														<span>{{vendorDetail[0].name}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Contact Number</span><br>
														<span>{{vendorDetail[0].mobileNumber}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Address</span><br>
														<span>{{vendorDetail[0].address}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Email Address</span><br>
														<span>{{vendorDetail[0].emailId}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Tin Number</span><br>
														<span>{{vendorDetail[0].tinNum}}</span>
													</div>
												</div>
											</div>
											<div class="col-md-3">
												<img src="images/portlet-remove-icon.png"
													class="floatRight pointer" ng-click="closeDriver(post)">
												<div class="col-md-12">
													<!--
                                                    <div ng-repeat = "doc in vehicleDocuments" class = "floatLeft" id = "div{{$index}}">
                                                       <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>
                                                            <a href="{{doc.location}}" class = "noLinkLine">
                                                                <img class="img-responsive img-rounded fileImg" 
                                                                  alt="Responsive image" 
                                                                  ng-src = "{{doc.imgSrc}}">
                                                                <div class = "docName"><span>{{doc.name}}</span></div>
                                                            </a>
                                                    </div>
-->
												</div>
											</div>
										</div>
									</td>
								</tr>
							</tbody>

						</table>
					</div>
					</tab> <tab ng-click="getRegisteredVendor(facilityData.listOfFacility); getTabInfo('registeredVendor')"> <tab-heading>Registered
					Vendor</tab-heading>
					<div class="registeredTabContent row">
						<table
							class="registeredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Vendor Id</th>
									<th>Vendor Name</th>
									<th>Vendor Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="vendorRegisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Vendors for
											Registration</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="vendorRegisterData.length>0">
							
								<tr
									ng-repeat="post in vendorRegisterData | filter:searchtextVendor">
									<td>{{post.vendorId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.mobileNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="removeButton btn btn-warning btn-xs buttonRadius0"
										type="button" ng-click="removeVendor(post,$index)" value="Remove"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</tab> <tab ng-click="getUnRegisteredVendor(facilityData.listOfFacility); getTabInfo('unregisteredVendor')"> <tab-heading>Unregistered
					Vendor</tab-heading>
					<div class="unregisteredTabContent row">
						<table
							class="unregisteredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Vendor Id</th>
									<th>Vendor Name</th>
									<th>Vendor Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="vendorUnregisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Unregistered Vendors</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="vendorUnregisterData.length>0">
								<tr
									ng-repeat="post in vendorUnregisterData | filter:searchtextVendor">
									<td>{{post.vendorId}}</td>
									<td>{{post.name}}</td>
									<td>{{post.mobileNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="addagainButton btn btn-info btn-xs buttonRadius0"
										type="button" ng-click="addagainVendor(post, $index)"
										value="Add Again"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Vendors" ng-model="searchtextVendor"
						expect_special_char>
					</tabset>
				</div>

				<div class="clearfix"></div>
				<br>
				<!--   /*END OF WRAPER2 = VEHICLE*/ 


				/*START OF WRAPPER3 = SUPERVISORS*/ -->
				<div class="wrapper1" id="supervisorContent">
					<div class="heading2 row">
						<span class="col-md-7 floatLeft" id="supervisor">Supervisor</span>
						<div class="col-md-5 floatRight">
							<efmfm-button img-class="efmfm_approvalButtons_collapse"
								src-url="images/portlet-collapse-icon"
								selected-url="images/portlet-expand-icon"
								hover-url="images/portlet-collapse-icon"
								alt-text="Collapse Window" main-div="supervisorContent"
								target-div="vendorTab_Approval"> </efmfm-button>
							<efmfm-button img-class="efmfm_approvalButtons_reload"
								src-url="images/portlet-reload-icon"
								selected-url="images/portlet-reload-icon"
								hover-url="images/portlet-reload-icon" alt-text="Reload Window"
								main-div="supervisorContent" target-div="vendorTab_Approval"
								ng-click='refreshVendorApproval()'> </efmfm-button>
							<efmfm-button img-class="efmfm_dashboarButtons_remove"
								src-url="images/portlet-remove-icon"
								selected-url="images/portlet-remove-icon"
								hover-url="images/portlet-remove-icon" alt-text="Remove Window"
								main-div="supervisorContent" target-div="vendorTab_Approval">
							</efmfm-button>
						</div>
					</div>
					<tabset class="tabset vendorTab_Approval"> <tab
						ng-click="getPendingSupervisor(facilityData.listOfFacility); getTabInfo('pendingSupervisor')"> <tab-heading>Pending
					Supervisor</tab-heading>
					<div class="pendingTabContent row">
						<table
							class="pendingTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Supervisor Id</th>
									<th>Supervisor Name</th>
									<th>Supervisor Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="supervisorsPendingData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Supervisor Pending</div>
									</td>
								</tr>
							</tbody>
							
							<tbody ng-show="supervisorsPendingData.length>0">
								<tr
									ng-repeat-start="post in supervisorsPendingData | filter:searchtextSupervisor"
									class="rowSeen" ng-class="{vendorRowExpanded:post.isClicked}">
									<td>{{post.supervisorId}}</td>
									<td>{{post.supervisorName}}</td>
									<td>{{post.supervisorMobileNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input ng-show="!post.isClicked"
										class="viewButton btn btn-primary btn-xs buttonRadius0"
										type="button" ng-click="viewSupervisor(post, $index)"
										value="View Detail"> <input ng-show="post.isClicked"
										class="approvalButton btn btn-success btn-xs buttonRadius0"
										type="button" ng-class="{{post.supervisorId}}"
										ng-click="approveSupervisor(post, $index)" value="Approve"
										ng-disabled="supervisorRole"> <input
										ng-show="post.isClicked"
										class="rejectButton btn btn-danger btn-xs buttonRadius0"
										type="button" ng-click="rejectSupervisor(post, $index)"
										value="Reject" ng-disabled="supervisorRole"></td>
								</tr>
								
								<tr ng-show="post.isClicked" ng-repeat-end=""
									ng-class="supervisor{{post.supervisorId}}" class="driverPendingDetail">
									<td colspan="5">
										<div class="row marginRow">
											<div class="col-md-9 marginRow padding0">
												<div class="row marginRow driverInfo_approval">
													<div class="col-md-3 Drivelabel">
														<span>Employee Id</span><br>
														<span>{{post.supervisorEmployeeId}}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>First Name</span><br>
														<span>{{post.supervisorName}}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>Last Name</span><br>
														<span>{{post.supervisorlastName}}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>Father Name</span><br>
														<span>{{post.supervisorFatherName}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Designation</span><br>
														<span>{{post.designation}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Mobile Number</span><br>
														<span>{{post.supervisorMobileNumber}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Vendor Name</span><br>
														<span>{{post.vendorName}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Date of Birth</span><br>
														<span>{{post.supervisordob}}</span>
													</div>

													<div class="col-md-3 Drivelabel">
														<span>Email Id</span><br>
														<span>{{post.emailId}}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>Gender</span><br>
														<span>{{post.supervisorGender}}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>Temporary Addresss</span><br>
														<span>{{post.presentAddress}}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>Permanent Addresss</span><br>
														<span>{{post.supervisorAddress }}</span>
													</div>
													<div class="col-md-3 Drivelabel">
														<span>Profile Picture</span><br>
														<img ng-src="{{post.profilePic}}" alt="Description"/>
													</div>
													
												</div>
											</div>
											<div class="col-md-3">
												<img src="images/portlet-remove-icon.png"
													class="floatRight pointer" ng-click="closeDriver(post)">
												<div class="col-md-12">
								
												</div>
											</div>
										</div>
									</td>
								</tr>
							</tbody>

						</table>
					</div>
					</tab> <tab ng-click="getRegisteredSupervisor(facilityData.listOfFacility); getTabInfo('registeredSupervisor')"> <tab-heading>Registered
					Supervisor</tab-heading>
					<div class="registeredTabContent row">
						<table
							class="registeredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Supervisor Id</th>
									<th>Supervisor Name</th>
									<th>Supervisor Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="supervisorsRegisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Supervisor for
											Registration</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="supervisorsRegisterData.length>0">
								<tr
									ng-repeat="post in supervisorsRegisterData | filter:searchtextSupervisor">
									<td>{{post.supervisorId}}</td>
									<td>{{post.supervisorName}}</td>
									<td>{{post.supervisorMobileNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="removeButton btn btn-warning btn-xs buttonRadius0"
										type="button" ng-click="removeSupervisor(post,$index)" value="Remove"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</tab> <tab ng-click="getUnRegisteredSupervisor(facilityData.listOfFacility); getTabInfo('unregisteredSupervisor')"> <tab-heading>Unregistered
					Supervisor</tab-heading>
					<div class="unregisteredTabContent row">
						<table
							class="unregisteredTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Supervisor Id</th>
									<th>Supervisor Name</th>
									<th>Supervisor Number</th>
									<th>Facility Name</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody ng-show="supervisorsUnregisterData.length==0">
								<tr>
									<td colspan='5'>
										<div class="noData">There are no Unregistered Supervisor</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="supervisorsUnregisterData.length>0">
								<tr
									ng-repeat="post in supervisorsUnregisterData | filter:searchtextSupervisor">
									<td>{{post.supervisorId}}</td>
									<td>{{post.supervisorName}}</td>
									<td>{{post.supervisorMobileNumber}}</td>
									<td>{{post.facilityName}}</td>
									<td><input
										class="addagainButton btn btn-info btn-xs buttonRadius0"
										type="button" ng-click="addagainSupervisor(post, $index)"
										value="Add Again"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Supervisor" ng-model="searchtextSupervisor"
						expect_special_char>
					</tabset>
				</div>

				<div class="clearfix"></div>
				<br>
				<!--   /*END OF WRAPER2 = SUPERVISORS*/ 

              /*START OF WRAPPER3 = Reports*/ -->
				<div class="wrapper1" id="vendorContent">
					<div class="heading2 row">
						<span class="col-md-7 floatLeft" id="report">Reports</span>
						<div class="col-md-5 floatRight">
							<efmfm-button img-class="efmfm_approvalButtons_collapse"
								src-url="images/portlet-collapse-icon"
								selected-url="images/portlet-expand-icon"
								hover-url="images/portlet-collapse-icon"
								alt-text="Collapse Window" main-div="vendorContent"
								target-div="reportTab_Approval"> </efmfm-button>
							<efmfm-button img-class="efmfm_approvalButtons_reload"
								src-url="images/portlet-reload-icon"
								selected-url="images/portlet-reload-icon"
								hover-url="images/portlet-reload-icon" alt-text="Reload Window"
								main-div="vendorContent" target-div="reportTab_Approval"
								ng-click='refreshVendorApproval()'> </efmfm-button>
							<efmfm-button img-class="efmfm_dashboarButtons_remove"
								src-url="images/portlet-remove-icon"
								selected-url="images/portlet-remove-icon"
								hover-url="images/portlet-remove-icon" alt-text="Remove Window"
								main-div="vendorContent" target-div="reportTab_Approval">
							</efmfm-button>
						</div>
					</div>
					<tabset class="tabset reportTab_Approval"> <tab
						ng-click="getTripSheetPendingReport(facilityData.listOfFacility); getTabInfo('tripSheetPending')"> <tab-heading>Trip
					Sheet Pendings</tab-heading>
					<div class="pendingTabContent row">
						<table
							class="pendingTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Route Id</th>
									<th>Edited Distance</th>
									<th>PlannedDistance</th>
									<th>TravelledDistance</th>
									<th>Facility Name</th>
									<th>Remarks</th>
									<th>RouteName</th>
									<th>VehicleNumber</th>

									<th>TripType</th>
									<th>TripAssignDate</th>
									<th>TripStartDate</th>
									<th>TripCompleteDate</th>
									<th>Edit</th>
									<th>Delete</th>
								</tr>
							</thead>
							<tbody ng-show="reportPendingData.length==0">
								<tr>
									<td colspan='14'>
										<div class="noData">There are no pending trip sheet</div>
									</td>
								</tr>
							</tbody>
							<tbody ng-show="reportPendingData.length>0">
								<tr
									ng-repeat="post in reportPendingData | filter:searchtextReport"
									class="pendingTripSheet{{$index}}" ng-show="!post.approved">
									<td>{{post.assignRouteId}}</td>
									<td ng-show="!post.isEdit">{{post.editDistance}}</td>	
									<td ng-show="post.isEdit"><input type="text"
										ng-model="post.editDistance" /></td>
									<td>{{post.plannedDistance}}</td>
									<td>{{post.travelledDistance}}</td>
									<td>{{post.facilityName}}</td>

									<td>{{post.tripEditRemarks}}</td>
									<td>{{post.routeName}}</td>
									<td>{{post.vehicleNumber}}</td>
									<td>{{post.tripType}}</td>
									<td>{{post.tripAssignDate}}</td>
									<td>{{post.tripStartDate}}</td>
									<td>{{post.tripCompleteDate}}</td>

									<td><input ng-show="!post.isEdit"
										class="viewButton btn btn-warning btn-xs buttonRadius0"
										type="button" ng-click="editTravellDistance(post)"
										value="Edit"> <input ng-show="post.isEdit"
										class="viewButton btn btn-success btn-xs buttonRadius0"
										type="button" ng-click="saveTravellDistance(post)"
										value="Save"></td>
									<td><input
										class="approvalButton btn btn-success btn-xs buttonRadius0"
										type="button" ng-click="approveTripSheetPendings(post, $index)"
										value="Approve" ng-disabled="supervisorRole"></td>
								</tr>
							</tbody>

						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Reports" ng-model="searchtextReport"
						expect_special_char>
					</tabset>
				</div>
				<br>

				<!-- Invoice Tab -->

				<div class="wrapper1" id="vendorContent">
					<div class="heading2 row">
						<span class="col-md-7 floatLeft" id="invoice">Invoice</span>
						<div class="col-md-5 floatRight">
							<efmfm-button img-class="efmfm_approvalButtons_collapse"
								src-url="images/portlet-collapse-icon"
								selected-url="images/portlet-expand-icon"
								hover-url="images/portlet-collapse-icon"
								alt-text="Collapse Window" main-div="vendorContent"
								target-div="invoiceTab_Approval"> </efmfm-button>
							<efmfm-button img-class="efmfm_approvalButtons_reload"
								src-url="images/portlet-reload-icon"
								selected-url="images/portlet-reload-icon"
								hover-url="images/portlet-reload-icon" alt-text="Reload Window"
								main-div="vendorContent" target-div="invoiceTab_Approval"
								ng-click='refreshVendorApproval()'> </efmfm-button>
							<efmfm-button img-class="efmfm_dashboarButtons_remove"
								src-url="images/portlet-remove-icon"
								selected-url="images/portlet-remove-icon"
								hover-url="images/portlet-remove-icon" alt-text="Remove Window"
								main-div="vendorContent" target-div="invoiceTab_Approval">
							</efmfm-button>
						</div>
					</div>
					<tabset class="tabset invoiceTab_Approval"> <tab
						ng-click="getPendingInvoice(facilityData.listOfFacility); getTabInfo('invoicePending')"> <tab-heading>Invoice Pending</tab-heading>
					<div class="pendingTabContent row pointer">
						<table
							class="pendingTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Invoice Id</th>
									<th>Invoice No</th>
									<th>Vendor Name</th>
									<th>Facility Name</th>
									<th>Vehicle No</th>
									<th>Contract Type</th>
									<th>Contracted KM/Perkm Amount</th>
									<th>Contract Amount/Per Day Cost</th>
									<th>Total (km)</th>
									<th>Extra KM</th>
									<th>Fuel Amount</th>
									<!-- <th>Extra KM Amount</th> -->
									<th>No of Days Absent</th>
									<th>Remarks</th>
									<th>Edit</th>
									<th>Approval</th>
								</tr>
							</thead>
							<tbody ng-show="invoicePendingData == 0">
						        <tr>
						         <td colspan='14'>
						          <div class="noData">There are no pending Invoice</div>
						         </td>
						        </tr>
						       </tbody>

							 <tbody ng-show="invoicePendingData.length > 0">
								<tr ng-repeat="invoicePending in invoicePendingData | filter:searchtextReport">

									<td class="active">{{invoicePending.invoiceId}}</td>

									<td>{{invoicePending.invoiceNumber}}</td>

									<td>{{invoicePending.vendorName}}</td>
									<td>{{invoicePending.facilityName}}</td>

									<td>{{invoicePending.vehicleNumber}}</td> 

									<td>{{invoicePending.contractType}}</td> 

									<td>{{invoicePending.contractedKm}}</td>

									<td>{{invoicePending.fixedcharges}}</td>

									<td ng-class="{updatedColorInvoice: invoicePending.oldtotalKmFlg == 'Y' , defaultColorInvoice: invoicePending.oldtotalKmFlg == 'N'}">
										<span ng-show = "!invoicePending.editByVendorInvoiceClicked">{{invoicePending.newtotalKm}} <i ng-show="invoicePending.oldtotalKmFlg == 'Y'" class="icon-eye-open" popover="Old Total Km - {{invoicePending.oldtotalKm}}" popover-trigger="mouseenter"></i></span>

										<input ng-show = "invoicePending.editByVendorInvoiceClicked" 
											   type = 'number'
											   class = "form-control" 
											   ng-model = "invoicePending.newtotalKm"
											   ng-change = "checkTotalKmValue(invoicePending, $index)" 
											   only-num >
									</td>	

									<td ng-class="{updatedColorInvoice: invoicePending.oldExtraKmFlg == 'Y' , defaultColorInvoice: invoicePending.oldExtraKmFlg == 'N'}"> 
										<span>{{invoicePending.newExtraKm}}</span>
										<!-- <span ng-show = "!invoicePending.editByVendorInvoiceClicked">{{invoicePending.newExtraKm}} <i ng-show="invoicePending.oldExtraKmFlg == 'Y'" class="icon-eye-open" popover="Old Extra Km - {{invoicePending.oldExtraKm}}" popover-trigger="mouseenter"></i></span> -->

										<!-- <input ng-show = "invoicePending.editByVendorInvoiceClicked" 
											   type = 'number' 
											   class = "form-control" 
											   ng-change = "checkTotalKmValue(invoicePending, $index)" 
											   ng-model = "invoicePending.newExtraKm" only-num> -->
									</td>
									
									<td ng-class="{updatedColorInvoice: invoicePending.oldFuelAmountFlg == 'Y' , defaultColorInvoice: invoicePending.oldFuelAmountFlg == 'N'}"> 

									<span ng-show = "!invoicePending.editByVendorInvoiceClicked">{{invoicePending.newFuelAmount}} <i ng-show = "invoicePending.oldFuelAmountFlg == 'Y'" class="icon-eye-open" popover="Old Fuel Days - {{invoicePending.oldFuelAmount}}" popover-trigger="mouseenter"></i></span>
									<input ng-show = "invoicePending.editByVendorInvoiceClicked" 
										   type = 'number' 
										   class = "form-control"
										   ng-change = "checkTotalKmValue(invoicePending, $index)"  
										   ng-model = "invoicePending.newFuelAmount" 
										   only-num>
									</td>
								
									<td ng-class="{updatedColorInvoice: invoicePending.oldPresentDaysFlg == 'Y' , defaultColorInvoice: invoicePending.oldPresentDaysFlg == 'N'}">

										<span ng-show = "!invoicePending.editByVendorInvoiceClicked">{{invoicePending.newPresentDays}} <i ng-show = "invoicePending.oldPresentDaysFlg == 'Y'" class="icon-eye-open" popover="Old Absent Days - {{invoicePending.oldPresentDays}}" popover-trigger="mouseenter"></i></span>
										<input ng-show = "invoicePending.editByVendorInvoiceClicked" 
											   type = 'number' 
											   class = "form-control"
											   ng-change = "checkTotalKmValue(invoicePending, $index)"  
											   ng-model = "invoicePending.newPresentDays" 
											   only-num>
									</td>

								
									<td ng-class="{updatedColorInvoice: invoicePending.oldInvoiceRemarksFlg == 'Y' , defaultColorInvoice: invoicePending.oldInvoiceRemarksFlg == 'N'}">
										<span ng-show = "!invoicePending.editByVendorInvoiceClicked">{{invoicePending.newInvoiceRemarks}} <i ng-show = "invoicePending.oldInvoiceRemarksFlg == 'Y'" class="icon-eye-open" popover="Old Remarks - {{invoicePending.oldInvoiceRemarks}}" popover-trigger="mouseenter"></i></span>

										<textarea ng-model="invoicePending.newInvoiceRemarks" 
												  ng-show = "invoicePending.editByVendorInvoiceClicked"
												  class = "textareaWidthStyle" >
									    </textarea>
									</td>

									<td>

										<input type = 'button' 
											   class = 'btn btn-warning btn-xs' 
											   ng-show = "!invoicePending.editByVendorInvoiceClicked"
											   value = 'Edit'
											   ng-click = "editInvoiceApproval('byVendor', invoicePending)">
										<input type = 'button' 
											   class = 'btn btn-success btn-xs' 
											   ng-show = "invoicePending.editByVendorInvoiceClicked"
											   value = 'Save'
											   ng-click = "saveChangesInvoiceApproval('byVendor', invoicePending, $index)">
										<input type = 'button' 
											   class = 'btn btn-default btn-xs' 
											   ng-show = "invoicePending.editByVendorInvoiceClicked"
											   value = 'Cancel'
											   ng-click = "cancelChangesInvoiceApproval('byVendor', invoicePending, $index)">

									</td>
									
									<td>
										<input class="approvalButton btn btn-success btn-xs buttonRadius0"
											   type="button" 
											   ng-click="InvoiceApproval('byVendor', invoicePending, $index)"
											   value="Approve" 
											   ng-disabled="supervisorRole">
									</td>
								</tr>

							</tbody>

						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Invoice" ng-model="searchtextReport"
						expect_special_char>
					</tabset>

<!-- 					<tabset class="tabset invoiceTab_Approval"> <tab
						ng-click="getPendingContractTypes()"> <tab-heading>Contact Types Pending</tab-heading>
					<div class="pendingTabContent row">
						<table
							class="pendingTable table table-hover table-responsive container-fluid">
							<thead class="tableHeading">
								<tr>
									<th>Contract Type Id</th>
									<th>Contract Type</th>
									<th>Contract Description</th>
									<th>Service Tax</th>
									<th>Edit/Save</th>
									<th>Approval</th>
								</tr>
							</thead>
							<tbody ng-show="contractTypesPendingData.length==0">
								<tr>
									<td colspan='14'>
										<div class="noData">There are no pending Contact Types</div>
									</td>
								</tr>
							</tbody>

							
								<tr ng-repeat="contractTypesPending in contractTypesPendingData | filter:searchtextReport">

									<td class="active">{{contractTypesPending.contractTypeId}}</td>
									<td>
										<span ng-show = "!contractTypesPending.editByContractTypeClicked">
										{{contractTypesPending.contractType}}
										</span>
										<input ng-show = "contractTypesPending.editByContractTypeClicked" 
											   type = 'text'
											   class = "form-control" 
											   ng-model = "contractTypesPending.contractType"
											   >
									</td>

									<td>
										<span ng-show = "!contractTypesPending.editByContractTypeClicked">
										{{contractTypesPending.contractDescription}}
										</span>
										<input ng-show = "contractTypesPending.editByContractTypeClicked" 
											   type = 'text'
											   class = "form-control" 
											   ng-model = "contractTypesPending.contractDescription"
											   >
									</td>	

									<td>
										<span ng-show = "!contractTypesPending.editByContractTypeClicked">
										{{contractTypesPending.serviceTax}}
										</span>
										<input ng-show = "contractTypesPending.editByContractTypeClicked" 
											   type = 'text'
											   class = "form-control" 
											   ng-model = "contractTypesPending.serviceTax"
											   >
									</td>	
									<td>
										<input type = 'button' 
											   class = 'btn btn-warning btn-xs' 
											   ng-show = "!contractTypesPending.editByContractTypeClicked"
											   value = 'Edit'
											   ng-click = "editContractTypesApproval('byVendor', contractTypesPending)">
										<input type = 'button' 
											   class = 'btn btn-success btn-xs' 
											   ng-show = "contractTypesPending.editByContractTypeClicked"
											   value = 'Save'
											   ng-click = "saveContractTypesApproval('byVendor', contractTypesPending, $index)">
										<input type = 'button' 
											   class = 'btn btn-danger btn-xs' 
											   ng-show = "contractTypesPending.editByContractTypeClicked"
											   value = 'Cancel'
											   ng-click = "cancelContractTypeApproval('byVendor', contractTypesPending, $index)">
									</td>
									
									<td>
										<input class="approvalButton btn btn-success btn-xs buttonRadius0"
											   type="button" 
											   ng-click="contractTypeApproval('byVendor', contractTypesPending, $index)"
											   value="Approve" 
											   >
									</td>
								</tr>

							</tbody>

						</table>
					</div>
					</tab> <input type='text' class="approvalSearchBox"
						placeholder="Filter Contract Types" ng-model="searchtextReport">
					</tabset> -->

				</div>

			</div>
		</div>
	</div>
</div>