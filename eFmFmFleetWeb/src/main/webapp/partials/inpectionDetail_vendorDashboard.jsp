<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    It is a template - ng-include in the vendorDashboard page under Inspection Detail tab
@State          
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->


<div class="row marginRow container-fluid">
	<div class="col-md-12">
		<div class="row marginBottom10" ng-show="gotInspectionDetailResult">
			<div class="col-md-2 pointer">
				<span ng-show="!compare"> <i
					class="icon-check inspectionAllCheck pointer" ng-show="isAllCompare"
					ng-click="checkAll()"></i> <i
					class="icon-unchecked inspectionAllCheck pointer" ng-show="!isAllCompare"
					ng-click="checkAll()"></i> <span class="marginRight10 pointer">Compare
						All</span>
				</span>
			</div>
			<div class="col-md-4">
				<h5 class="lengthVendorDashboard">
					Vehicle Inspection Summary Report - <span class="badge">{{inspectionDetailDataLength}}</span>
				</h5>
			</div>
			<div class="col-md-6">
				<input type="button" class="btn btn-purple floatRight"
					value="Compare Checked Boxes" ng-show="!compare"
					ng-click="getCompareResults()"> <input type="button"
					class="btn btn-primary floatRight" value="Go Back"
					ng-show="compare" ng-click="getCompareResults()">
				<button
					class="btn btn-sm btn-success form-control excelExportButton floatRight excelExportInspection marginRight10"
					ng-click="excelExport()" ng-show="compare">
					<i class="icon-download-alt"></i> <span class="marginLeft5">Excel</span>
				</button>
			</div>
		</div>
	</div>


	<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "compareSpinner">
           <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
 	</div> 

	<!-- *************************************** COMPARE INSPECTION OBJECTs' VIEW ****************************************-->
	<div id="inspectionDetailCompare" class="col-md-12 compareDiv"
		ng-show="compare && compareInspectionArray.length>1 && !compareSpinner">
		<table
			class="table table-responsive comparetable inpectionDetailCompareTable container-fluid table-bordered">
			<tbody>
				<tr>
					<td class="inspectorTd1 verticalInsHeader"><span>Inspected
							Date</span></td>
					<td class="inspectorTd"
						ng-repeat="inpection in compareInspectionArray"><span>{{inpection.inspectionDate}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Vehicle Number </span></td>
					<td class="inspectorTdName"
						ng-repeat="inpection in compareInspectionArray"><span>{{inpection.vehicleNumber}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Inspector Name </span></td>
					<td class="inspectorTdName"
						ng-repeat="inpection in compareInspectionArray"><span>{{inpection.vehicleInspector}}</span>
					</td>
				</tr>
				
				<tr>
					<td class="verticalInsHeader"><span>Driver Name</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						class="driverName"><span>{{inpection.driverName}}</span></td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Rc Document</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.documentRc, falseCell: !inpection.documentRc}">
						<span>{{inpection.documentRc}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Rc Document Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.documentRcRemarksIsFull,expandInspectionRemarks:inpection.documentRcRemarksIsFull}"
							ng-click="inpection.documentRcRemarksIsFull = !inpection.documentRcRemarksIsFull">
							<span>{{inpection.documentRcRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Insurance Document</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.documentInsurance, falseCell: !inpection.documentInsurance}">
						<span>{{inpection.documentInsurance}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Insurance Document
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.documentInsuranceRemarksIsFull,expandInspectionRemarks:inpection.documentInsuranceRemarksIsFull}"
							ng-click="inpection.documentInsuranceRemarksIsFull = !inpection.documentInsuranceRemarksIsFull">
							<span>{{inpection.documentInsuranceRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Driver License</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.documentDriverLicence, falseCell: !inpection.documentDriverLicence}">
						<span>{{inpection.documentDriverLicence}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Driver License
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.documentDriverLicenceRemarksIsFull,expandInspectionRemarks:inpection.documentDriverLicenceRemarksIsFull}"
							ng-click="inpection.documentDriverLicenceRemarksIsFull = !inpection.documentDriverLicenceRemarksIsFull">
							<span>{{inpection.documentDriverLicenceRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Update JMP</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.documentUpdateJmp, falseCell: !inpection.documentUpdateJmp}">
						<span>{{inpection.documentUpdateJmp}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Update JMP Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.documentUpdateJmpRemarksIsFull,expandInspectionRemarks:inpection.documentUpdateJmpRemarksIsFull}"
							ng-click="inpection.documentUpdateJmpRemarksIsFull = !inpection.documentUpdateJmpRemarksIsFull">
							<span>{{inpection.documentUpdateJmpRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>First Aid Kit</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.firstAidKit, falseCell: !inpection.firstAidKit}">
						<span>{{inpection.firstAidKit}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>First Aid Kit
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.firstAidKitRemarksIsFull,expandInspectionRemarks:inpection.firstAidKitRemarksIsFull}"
							ng-click="inpection.firstAidKitRemarksIsFull = !inpection.firstAidKitRemarksIsFull">
							<span>{{inpection.firstAidKitRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Fire Extinguisher</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.fireExtingusher, falseCell: !inpection.fireExtingusher}">
						<span>{{inpection.fireExtingusher}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Fire Extinguisher
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.fireExtingusherRemarksIsFull,expandInspectionRemarks:inpection.fireExtingusherRemarksIsFull}"
							ng-click="inpection.fireExtingusherRemarksIsFull = !inpection.fireExtingusherRemarksIsFull">
							<span>{{inpection.fireExtingusherRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> All Seat Belt
							Working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.allSeatBeltsWorking, falseCell: !inpection.allSeatBeltsWorking}">
						<span>{{inpection.allSeatBeltsWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> All Seat Belt
							Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.allSeatBeltsWorkingRemarksIsFull,expandInspectionRemarks:inpection.allSeatBeltsWorkingRemarksIsFull}"
							ng-click="inpection.allSeatBeltsWorkingRemarksIsFull = !inpection.allSeatBeltsWorkingRemarksIsFull">
							<span>{{inpection.allSeatBeltsWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Placard</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.placard, falseCell: !inpection.placard}">
						<span>{{inpection.placard}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Placard Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.placardRemarksIsFull,expandInspectionRemarks:inpection.placardRemarksIsFull}"
							ng-click="inpection.placardRemarksIsFull = !inpection.placardRemarksIsFull">
							<span>{{inpection.placardRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Mosquito Bat</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.mosquitoBat, falseCell: !inpection.mosquitoBat}">
						<span>{{inpection.mosquitoBat}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span> Mosquito Bat
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.mosquitoBatRemarksIsFull,expandInspectionRemarks:inpection.mosquitoBatRemarksIsFull}"
							ng-click="inpection.mosquitoBatRemarksIsFull = !inpection.mosquitoBatRemarksIsFull">
							<span>{{inpection.mosquitoBatRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Panic Button</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.panicButton, falseCell: !inpection.panicButton}">
						<span>{{inpection.panicButton}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Panic Button
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.panicButtonRemarksIsFull,expandInspectionRemarks:inpection.panicButtonRemarksIsFull}"
							ng-click="inpection.panicButtonRemarksIsFull = !inpection.panicButtonRemarksIsFull">
							<span>{{inpection.panicButtonRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Walky Talky</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.walkyTalky, falseCell: !inpection.walkyTalky}">
						<span>{{inpection.walkyTalky}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Walky Talky Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.walkyTalkyRemarksIsFull,expandInspectionRemarks:inpection.walkyTalkyRemarksIsFull}"
							ng-click="inpection.walkyTalkyRemarksIsFull = !inpection.walkyTalkyRemarksIsFull">
							<span>{{inpection.walkyTalkyRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>GPS</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.gps, falseCell: !inpection.gps}">
						<span>{{inpection.gps}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>GPS Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.gpsRemarksIsFull,expandInspectionRemarks:inpection.gpsRemarksIsFull}"
							ng-click="inpection.gpsRemarksIsFull = !inpection.gpsRemarksIsFull">
							<span>{{inpection.gpsRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Driver Uniform</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.driverUniform, falseCell: !inpection.driverUniform}">
						<span>{{inpection.driverUniform}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Driver Uniform
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.driverUniformRemarksIsFull,expandInspectionRemarks:inpection.driverUniformRemarksIsFull}"
							ng-click="inpection.driverUniformRemarksIsFull = !inpection.driverUniformRemarksIsFull">
							<span>{{inpection.driverUniformRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Stepney</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.stephney, falseCell: !inpection.stephney}">
						<span>{{inpection.stephney}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Stepney Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.stephneyRemarksIsFull,expandInspectionRemarks:inpection.stephneyRemarksIsFull}"
							ng-click="inpection.stephneyRemarksIsFull = !inpection.stephneyRemarksIsFull">
							<span>{{inpection.stephneyRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Umbrella</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.umbrella, falseCell: !inpection.umbrella}">
						<span>{{inpection.umbrella}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Umbrella Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.umbrellaRemarksIsFull,expandInspectionRemarks:inpection.umbrellaRemarksIsFull}"
							ng-click="inpection.umbrellaRemarksIsFull = !inpection.umbrellaRemarksIsFull">
							<span>{{inpection.umbrellaRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Torch</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.torch, falseCell: !inpection.torch}">
						<span>{{inpection.torch}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Torch Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.torchRemarksIsFull,expandInspectionRemarks:inpection.torchRemarksIsFull}"
							ng-click="inpection.torchRemarksIsFull = !inpection.torchRemarksIsFull">
							<span>{{inpection.torchRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Seat Upholstery is
							Clean / Not Torn</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.seatUpholtseryCleanNotTorn, falseCell: !inpection.seatUpholtseryCleanNotTorn}">
						<span>{{inpection.seatUpholtseryCleanNotTorn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Seat Upholstery is
							Clean / Not Torn Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.seatUpholtseryCleanNotTornRemarksIsFull,expandInspectionRemarks:inpection.seatUpholtseryCleanNotTornRemarksIsFull}"
							ng-click="inpection.seatUpholtseryCleanNotTornRemarksIsFull = !inpection.seatUpholtseryCleanNotTornRemarksIsFull">
							<span>{{inpection.seatUpholtseryCleanNotTornRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Roof
							Upholstery is clean and not torn</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.vehcileRoofUpholtseryCleanNotTorn, falseCell: !inpection.vehcileRoofUpholtseryCleanNotTorn}">
						<span>{{inpection.vehcileRoofUpholtseryCleanNotTorn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Roof
							Upholstery is clean and not torn Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.vehcileRoofUpholtseryCleanNotTornRemarksIsFull,expandInspectionRemarks:inpection.vehcileRoofUpholtseryCleanNotTornRemarksIsFull}"
							ng-click="inpection.vehcileRoofUpholtseryCleanNotTornRemarksIsFull = !inpection.vehcileRoofUpholtseryCleanNotTornRemarksIsFull">
							<span>{{inpection.vehcileRoofUpholtseryCleanNotTornRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Floor
							Upholstery is clean and not torn</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.vehcileFloorUpholtseryCleanNotTorn, falseCell: !inpection.vehcileFloorUpholtseryCleanNotTorn}">
						<span>{{inpection.vehcileFloorUpholtseryCleanNotTorn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Floor
							Upholstery is clean and not torn Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.vehcileFloorUpholtseryCleanNotTornRemarksIsFull,expandInspectionRemarks:inpection.vehcileFloorUpholtseryCleanNotTornRemarksIsFull}"
							ng-click="inpection.vehcileFloorUpholtseryCleanNotTornRemarksIsFull = !inpection.vehcileFloorUpholtseryCleanNotTornRemarksIsFull">
							<span>{{inpection.vehcileFloorUpholtseryCleanNotTornRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Dashboard
							is Clean </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.vehcileDashboardClean, falseCell: !inpection.vehcileDashboardClean}">
						<span>{{inpection.vehcileDashboardClean}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Dashboard
							is Clean Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.vehcileDashboardCleanRemarksIsFull,expandInspectionRemarks:inpection.vehcileDashboardCleanRemarksIsFull}"
							ng-click="inpection.vehcileDashboardCleanRemarksIsFull = !inpection.vehcileDashboardCleanRemarksIsFull">
							<span>{{inpection.vehcileDashboardCleanRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle glasses is
							Clean / Stain Free / No Films etc</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.vehicleGlassCleanStainFree, falseCell: !inpection.vehicleGlassCleanStainFree}">
						<span>{{inpection.vehicleGlassCleanStainFree}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle glasses is
							Clean / Stain Free / No Films etc Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.vehicleGlassCleanStainFreeRemarksIsFull,expandInspectionRemarks:inpection.vehicleGlassCleanStainFreeRemarksIsFull}"
							ng-click="inpection.vehicleGlassCleanStainFreeRemarksIsFull = !inpection.vehicleGlassCleanStainFreeRemarksIsFull">
							<span>{{inpection.vehicleGlassCleanStainFreeRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Interior
							Lighting is Bright and working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.vehicleInteriorLightBrightWorking, falseCell: !inpection.vehicleInteriorLightBrightWorking}">
						<span>{{inpection.vehicleInteriorLightBrightWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Vehicle Interior
							Lighting is Bright and working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.vehicleInteriorLightBrightWorkingRemarksIsFull,expandInspectionRemarks:inpection.vehicleInteriorLightBrightWorkingRemarksIsFull}"
							ng-click="inpection.vehicleInteriorLightBrightWorkingRemarksIsFull = !inpection.vehicleInteriorLightBrightWorkingRemarksIsFull">
							<span>{{inpection.vehicleInteriorLightBrightWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Bolster Seperating
							the Last two seats (Only in Innova)</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.bolsterSeperatingLastTwoSeats, falseCell: !inpection.bolsterSeperatingLastTwoSeats}">
						<span>{{inpection.bolsterSeperatingLastTwoSeats}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Bolster Seperating
							the Last two seats (Only in Innova) Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.bolsterSeperatingLastTwoSeatsRemarksIsFull,expandInspectionRemarks:inpection.bolsterSeperatingLastTwoSeatsRemarksIsFull}"
							ng-click="inpection.bolsterSeperatingLastTwoSeatsRemarksIsFull = !inpection.bolsterSeperatingLastTwoSeatsRemarksIsFull">
							<span>{{inpection.bolsterSeperatingLastTwoSeatsRemarks}}</span>
						</div>
					</td>
				</tr>

				<tr>
					<td class="verticalInsHeader"><span>Audio Working Or
							Not</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.audioWorkingOrNot, falseCell: !inpection.audioWorkingOrNot}">
						<span>{{inpection.audioWorkingOrNot}}</span>
					</td>
				</tr>

				<tr>
					<td class="verticalInsHeader"><span>Audio Working Or
							Not Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.audioWorkingOrNotRemarksIsFull,expandInspectionRemarks:inpection.audioWorkingOrNotRemarksIsFull}"
							ng-click="inpection.audioWorkingOrNotRemarksIsFull = !inpection.audioWorkingOrNotRemarksIsFull">
							<span>{{inpection.audioWorkingOrNotRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Scratches on the
							body </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.exteriorScratches , falseCell: !inpection.exteriorScratches }">
						<span>{{inpection.exteriorScratches }}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Scratches on the
							body Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.exteriorScratchesRemarksIsFull,expandInspectionRemarks:inpection.exteriorScratchesRemarksIsFull}"
							ng-click="inpection.exteriorScratchesRemarksIsFull = !inpection.exteriorScratchesRemarksIsFull">
							<span>{{inpection.exteriorScratchesRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>No Patch Work</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.noPatchWork, falseCell: !inpection.noPatchWork}">
						<span>{{inpection.noPatchWork}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>No Patch Work
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.noPatchWorkRemarksIsFull,expandInspectionRemarks:inpection.noPatchWorkRemarksIsFull}"
							ng-click="inpection.noPatchWorkRemarksIsFull = !inpection.noPatchWorkRemarksIsFull">
							<span>{{inpection.noPatchWorkRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Pedal Brake Working
					</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.pedalBrakeWorking, falseCell: !inpection.pedalBrakeWorking}">
						<span>{{inpection.pedalBrakeWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Pedal Brake Working
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.pedalBrakeWorkingRemarksIsFull,expandInspectionRemarks:inpection.pedalBrakeWorkingRemarksIsFull}"
							ng-click="inpection.pedalBrakeWorkingRemarksIsFull = !inpection.pedalBrakeWorkingRemarksIsFull">
							<span>{{inpection.pedalBrakeWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Hand Brake Working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.handBrakeWorking, falseCell: !inpection.handBrakeWorking}">
						<span>{{inpection.handBrakeWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Hand Brake Working
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.handBrakeWorkingRemarksIsFull,expandInspectionRemarks:inpection.handBrakeWorkingRemarksIsFull}"
							ng-click="inpection.handBrakeWorkingRemarksIsFull = !inpection.handBrakeWorkingRemarksIsFull">
							<span>{{inpection.handBrakeWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>No Dents on the
							Trim of the Wheel</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.tyresNoDentsTrimWheel , falseCell: !inpection.tyresNoDentsTrimWheel }">
						<span>{{inpection.tyresNoDentsTrimWheel }}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>No Dents on the
							Trim of the Wheel Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.tyresNoDentsTrimWheelRemarksIsFull,expandInspectionRemarks:inpection.tyresNoDentsTrimWheelRemarksIsFull}"
							ng-click="inpection.tyresNoDentsTrimWheelRemarksIsFull = !inpection.tyresNoDentsTrimWheelRemarksIsFull">
							<span>{{inpection.tyresNoDentsTrimWheelRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Tyres in good
							condition</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.tyresGoodCondition, falseCell: !inpection.tyresGoodCondition}">
						<span>{{inpection.tyresGoodCondition}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Tyres in good
							condition Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.tyresGoodConditionRemarksIsFull,expandInspectionRemarks:inpection.tyresGoodConditionRemarksIsFull}"
							ng-click="inpection.tyresGoodConditionRemarksIsFull = !inpection.tyresGoodConditionRemarksIsFull">
							<span>{{inpection.tyresGoodConditionRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>All Tyres including
							Stephney Inflate</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.allTyresAndStephneyInflate, falseCell: !inpection.allTyresAndStephneyInflate}">
						<span>{{inpection.allTyresAndStephneyInflate}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>All Tyres including
							Stephney Inflate Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.allTyresAndStephneyInflateRemarksIsFull,expandInspectionRemarks:inpection.allTyresAndStephneyInflateRemarksIsFull}"
							ng-click="inpection.allTyresAndStephneyInflateRemarksIsFull = !inpection.allTyresAndStephneyInflateRemarksIsFull">
							<span>{{inpection.allTyresAndStephneyInflateRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Jack & Tools</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.jackAndTool, falseCell: !inpection.jackAndTool}">
						<span>{{inpection.jackAndTool}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Jack & Tools
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.jackAndToolRemarksIsFull,expandInspectionRemarks:inpection.jackAndToolRemarksIsFull}"
							ng-click="inpection.jackAndToolRemarksIsFull = !inpection.jackAndToolRemarksIsFull">
							<span>{{inpection.jackAndToolRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Wiper Working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.wiperWorking, falseCell: !inpection.wiperWorking}">
						<span>{{inpection.wiperWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Wiper Working
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.wiperWorkingRemarksIsFull,expandInspectionRemarks:inpection.wiperWorkingRemarksIsFull}"
							ng-click="inpection.wiperWorkingRemarksIsFull = !inpection.wiperWorkingRemarksIsFull">
							<span>{{inpection.wiperWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Cooling achieved
							end to end in 5 mins </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.acCoolingIn5mins, falseCell: !inpection.acCoolingIn5mins}">
						<span>{{inpection.acCoolingIn5mins}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Cooling achieved
							end to end in 5 mins Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.acCoolingIn5minsRemarksIsFull,expandInspectionRemarks:inpection.acCoolingIn5minsRemarksIsFull}"
							ng-click="inpection.acCoolingIn5minsRemarksIsFull = !inpection.acCoolingIn5minsRemarksIsFull">
							<span>{{inpection.acCoolingIn5minsRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>No Smell in AC</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.noSmellInAC, falseCell: !inpection.noSmellInAC}">
						<span>{{inpection.noSmellInAC}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>No Smell in AC
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.noSmellInACRemarksIsFull,expandInspectionRemarks:inpection.noSmellInACRemarksIsFull}"
							ng-click="inpection.noSmellInACRemarksIsFull = !inpection.noSmellInACRemarksIsFull">
							<span>{{inpection.noSmellInACRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>AC vents are clean
					</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.acVentsClean , falseCell: !inpection.acVentsClean }">
						<span>{{inpection.acVentsClean}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>AC vents are clean
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.acVentsCleanRemarksIsFull,expandInspectionRemarks:inpection.acVentsCleanRemarksIsFull}"
							ng-click="inpection.acVentsCleanRemarksIsFull = !inpection.acVentsCleanRemarksIsFull">
							<span>{{inpection.acVentsCleanRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Engine oil Change
							Indicator ON </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.engionOilChangeIndicatorOn, falseCell: !inpection.engionOilChangeIndicatorOn}">
						<span>{{inpection.engionOilChangeIndicatorOn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Engine oil Change
							Indicator ON Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.enginOilChangeIndicatorOnRemarksIsFull,expandInspectionRemarks:inpection.enginOilChangeIndicatorOnRemarksIsFull}"
							ng-click="inpection.enginOilChangeIndicatorOnRemarksIsFull = !inpection.enginOilChangeIndicatorOnRemarksIsFull">
							<span>{{inpection.enginOilChangeIndicatorOnRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Coolant Indicator
							ON</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.coolantIndicatorOn, falseCell: !inpection.coolantIndicatorOn}">
						<span>{{inpection.coolantIndicatorOn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Coolant Indicator
							ON Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.coolantIndicatorOnIsFull,expandInspectionRemarks:inpection.coolantIndicatorOnIsFull}"
							ng-click="inpection.coolantIndicatorOnIsFull = !inpection.coolantIndicatorOnIsFull">
							<span>{{inpection.coolantIndicatorOnRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Brake & Clutch oil
							Indicator On</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.brakeClutchIndicatorOn, falseCell: !inpection.brakeClutchIndicatorOn}">
						<span>{{inpection.brakeClutchIndicatorOn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Brake & Clutch oil
							Indicator On Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.brakeClutchIndicatorOnRemarksIsFull,expandInspectionRemarks:inpection.brakeClutchIndicatorOnRemarksIsFull}"
							ng-click="inpection.brakeClutchIndicatorOnRemarksIsFull = !inpection.brakeClutchIndicatorOnRemarksIsFull">
							<span>{{inpection.brakeClutchIndicatorOnRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>High Beam Working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.highBeamWorking, falseCell: !inpection.highBeamWorking}">
						<span>{{inpection.highBeamWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>High Beam Working
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.highBeamWorkingRemarksIsFull,expandInspectionRemarks:inpection.highBeamWorkingRemarksIsFull}"
							ng-click="inpection.highBeamWorkingRemarksIsFull = !inpection.highBeamWorkingRemarksIsFull">
							<span>{{inpection.highBeamWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Low Beam Working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.lowBeamWorking, falseCell: !inpection.lowBeamWorking}">
						<span>{{inpection.lowBeamWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Low Beam Working
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.lowBeamWorkingRemarksIsFull,expandInspectionRemarks:inpection.lowBeamWorkingRemarksIsFull}"
							ng-click="inpection.lowBeamWorkingRemarksIsFull = !inpection.lowBeamWorkingRemarksIsFull">
							<span>{{inpection.lowBeamWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Right Front
							Indicator Working </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.rightFrontIndicatorWorking, falseCell: !inpection.rightFrontIndicatorWorking}">
						<span>{{inpection.rightFrontIndicatorWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Right Front
							Indicator Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.rightFromtIndicatorWorkingRemarksIsFull,expandInspectionRemarks:inpection.rightFromtIndicatorWorkingRemarksIsFull}"
							ng-click="inpection.rightFromtIndicatorWorkingRemarksIsFull = !inpection.rightFromtIndicatorWorkingRemarksIsFull">
							<span>{{inpection.rightFromtIndicatorWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Left Front
							Indicator Working </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.leftFrontIndicatorWorking, falseCell: !inpection.leftFrontIndicatorWorking}">
						<span>{{inpection.leftFrontIndicatorWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Left Front
							Indicator Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.leftFrontIndicatorWorkingRemarksIsFull,expandInspectionRemarks:inpection.leftFrontIndicatorWorkingRemarksIsFull}"
							ng-click="inpection.leftFrontIndicatorWorkingRemarksIsFull = !inpection.leftFrontIndicatorWorkingRemarksIsFull">
							<span>{{inpection.leftFrontIndicatorWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Parking Lights
							Working</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.parkingLightsWorking, falseCell: !inpection.parkingLightsWorking}">
						<span>{{inpection.parkingLightsWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Parking Lights
							Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.parkingLightsWorkingRemarksIsFull,expandInspectionRemarks:inpection.parkingLightsWorkingRemarksIsFull}"
							ng-click="inpection.parkingLightsWorkingRemarksIsFull = !inpection.parkingLightsWorkingRemarksIsFull">
							<span>{{inpection.parkingLightsWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>LED Day Time
							Running Lights Working </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.ledDayTimeRunningLightWorking, falseCell: !inpection.ledDayTimeRunningLightWorking}">
						<span>{{inpection.ledDayTimeRunningLightWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>LED Day Time
							Running Lights Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.ledDayTimeRunningLightWorkingRemarksIsFull,expandInspectionRemarks:inpection.ledDayTimeRunningLightWorkingRemarksIsFull}"
							ng-click="inpection.ledDayTimeRunningLightWorkingRemarksIsFull = !inpection.ledDayTimeRunningLightWorkingRemarksIsFull">
							<span>{{inpection.ledDayTimeRunningLightWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Right Rear
							Indicator Working </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.rightRearIndicatorWorking, falseCell: !inpection.rightRearIndicatorWorking}">
						<span>{{inpection.rightRearIndicatorWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Right Rear
							Indicator Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.rightRearIndicatorWorkingRemarksIsFull,expandInspectionRemarks:inpection.rightRearIndicatorWorkingRemarksIsFull}"
							ng-click="inpection.rightRearIndicatorWorkingRemarksIsFull = !inpection.rightRearIndicatorWorkingRemarksIsFull">
							<span>{{inpection.rightRearIndicatorWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Left Rear Indicator
							Working </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.leftRearIndicatorWorking, falseCell: !inpection.leftRearIndicatorWorking}">
						<span>{{inpection.leftRearIndicatorWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Left Rear Indicator
							Working Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.leftRearIndicatorWorkingRemarksIsFull,expandInspectionRemarks:inpection.leftRearIndicatorWorkingRemarksIsFull}"
							ng-click="inpection.leftRearIndicatorWorkingRemarksIsFull = !inpection.leftRearIndicatorWorkingRemarksIsFull">
							<span>{{inpection.leftRearIndicatorWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Brake Lights On</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.brakeLightsOn, falseCell: !inpection.brakeLightsOn}">
						<span>{{inpection.brakeLightsOn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Brake Lights On
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.brakeLightsOnRemarksIsFull,expandInspectionRemarks:inpection.brakeLightsOnRemarksIsFull}"
							ng-click="inpection.brakeLightsOnRemarksIsFull = !inpection.brakeLightsOnRemarksIsFull">
							<span>{{inpection.brakeLightsOnRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Reverse Lights On</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.reverseLightsOn, falseCell: !inpection.reverseLightsOn}">
						<span>{{inpection.reverseLightsOn}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Reverse Lights On
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.reverseLightsOnRemarksIsFull,expandInspectionRemarks:inpection.reverseLightsOnRemarksIsFull}"
							ng-click="inpection.reverseLightsOnRemarksIsFull = !inpection.reverseLightsOnRemarksIsFull">
							<span>{{inpection.reverseLightsOnRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Horn Working </span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.hornWorking, falseCell: !inpection.hornWorking}">
						<span>{{inpection.hornWorking}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Horn Working
							Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.hornWorkingRemarksIsFull,expandInspectionRemarks:inpection.hornWorkingRemarksIsFull}"
							ng-click="inpection.hornWorkingRemarksIsFull = !inpection.hornWorkingRemarksIsFull">
							<span>{{inpection.hornWorkingRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Reflective sign
							board</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						ng-class="{trueCell:inpection.reflectionSignBoard, falseCell: !inpection.reflectionSignBoard}">
						<span>{{inpection.reflectionSignBoard}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Reflective sign
							board Remarks</span></td>
					<td class="remarksCell"
						ng-repeat="inpection in compareInspectionArray">
						<div
							ng-class="{collapseInspectionRemarks:!inpection.reflectionSignBoardRemarksIsFull,expandInspectionRemarks:inpection.reflectionSignBoardRemarksIsFull}"
							ng-click="inpection.reflectionSignBoardRemarksIsFull = !inpection.reflectionSignBoardRemarksIsFull">
							<span>{{inpection.reflectionSignBoardRemarks}}</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Number of Punctures
							done</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						class="remarksCell"><span class="punctureNumberInsDet">{{inpection.numberofPunctruresdone}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Fog Lights</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						class="remarksCell"><span class="punctureNumberInsDet">{{inpection.fogLights}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Did Driver completed checklist?</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						class="remarksCell"><span class="punctureNumberInsDet">{{inpection.driverCheckInDidOrNot}}</span>
					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Diesel</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						class="remarksCell"><span>{{inpection.diesel}}</span></td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Documents</span></td>
					<td ng-repeat="inspection in compareInspectionArray"
						class="remarksCell">
						<div class="floatLeft imageDiv_approval imageBorder"
							id="div{{$index}}"
							ng-show="inspection.InspDocs.vehicleInsepectionLeftImage">
							<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
							<a href="{{inspection.InspDocs.vehicleInsepectionLeftImage}}"
								class="noLinkLine"> <img
								class="img-responsive img-rounded fileImg"
								ng-src="{{inspection.InspDocs.vehicleInsepectionLeftImage}}">
								<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
							</a>
						</div>
						<div class="floatLeft imageDiv_approval imageBorder"
							id="div{{$index}}"
							ng-show="inspection.InspDocs.vehicleInsepectionRightImage">
							<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
							<a href="{{inspection.InspDocs.vehicleInsepectionRightImage}}"
								class="noLinkLine"> <img
								class="img-responsive img-rounded fileImg"
								ng-src="{{inspection.InspDocs.vehicleInsepectionRightImage}}">
								<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
							</a>
						</div>
						<div class="floatLeft imageDiv_approval imageBorder"
							id="div{{$index}}"
							ng-show="inspection.InspDocs.vehicleInsepectionBackImage">
							<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
							<a href="{{inspection.InspDocs.vehicleInsepectionBackImage}}"
								class="noLinkLine"> <img
								class="img-responsive img-rounded fileImg"
								ng-src="{{inspection.InspDocs.vehicleInsepectionBackImage}}">
								<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
							</a>
						</div>
						<div class="floatLeft imageDiv_approval imageBorder"
							id="div{{$index}}"
							ng-show="inspection.InspDocs.vehicleInsepectionFrontImage">
							<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
							<a href="{{inspection.InspDocs.vehicleInsepectionFrontImage}}"
								class="noLinkLine"> <img
								class="img-responsive img-rounded fileImg"
								ng-src="{{inspection.InspDocs.vehicleInsepectionFrontImage}}">
								<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
							</a>
						</div>

					</td>
				</tr>
				<tr>
					<td class="verticalInsHeader"><span>Remarks</span></td>
					<td ng-repeat="inpection in compareInspectionArray"
						class="remarksCell"><span class="punctureNumberInsDet">{{inpection.feedback}}</span>
					</td>
				</tr>
			</tbody>
		</table>

	</div>
</div>


<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "byVendorVehicleButtonClicked">
           <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
 </div> 


<!-- *************************************** ACCORDIANS VIEW FOR RESULT ****************************************-->
<div class="panel-group" id="accordion" role="tablist"
	aria-multiselectable="true" ng-show="!compare && !byVendorVehicleButtonClicked">
	<div class="panel panel-default accordianPanel_inspection"
		ng-repeat="date in inspectionDetailData">
		<!--
      <a data-toggle="collapse" 
         data-parent="#accordion" 
         href="#collapse{{$index}}" 
         aria-expanded="false" 
         aria-controls="collapse{{$index}}"
         ng-click = "setInspectionSelectedDate(inspection)"
         class = "noTextDecoration collapse{{$index}}">
-->
		<div
			class="panel-heading_inpsection panelHeading_InspectionDetail collapseZoneDiv{{$index}}"
			role="tab" id="headingOne_inpsection">
			<h4 class="panel-title heading_inspection">
				<span class="floatLeft inspectorSpan marginRight5"> <i
					class="icon-check inpectionDetailCheckIcon"
					ng-show="date.checkCompare" ng-click="addToCompare(date)"></i> <i
					class="icon-unchecked inpectionDetailUnCheckIcon"
					ng-show="!date.checkCompare" ng-click="addToCompare(date)"></i>
				</span> <a data-toggle="collapse" data-parent="#accordion"
					href="#collapse{{$index}}" aria-expanded="false"
					aria-controls="collapse{{$index}}"
					ng-click="setInspectionSelectedDate(inspection)"
					class="noTextDecoration collapse{{$index}}"> <span
					class="floatLeft inspectionDateSpan">{{date.inspectionDate}}</span>
					<span class="floatLeft marginLeft10">-</span> 
					<span
					class="floatLeft inspectorSpan">Inspected by
						{{date.vehicleInspector}}</span>    <span
					class="floatLeft inspectorSpan" ng-show="showVehicleNumber">Vehicle Number : {{date.vehicleNumber}}</span>
				</a> <span class="floatRight inspectorSpan pointer"> <!--
              <button class ="btn btn-inspection btn-xs" ng-click = "exportExcel()">
              <span>Excel</span>
--> <i class="icon-download-alt inspectionDownload"
					ng-click="exportExcelByDate(date)"></i> <!--              </button>-->
				</span> <span class="floatRight inspectorSpan pointer"> <!--
              <button class ="btn btn-inspection btn-xs" ng-click = "exportExcel()">
              <span>Excel</span>
--> <i class="icon-upload-alt inspectionDownload"
					ng-click="uploadInspectionDocument(date)"></i> <!--              </button>-->
				</span>
			</h4>
			<!--    </a>-->
		</div>

		

		<div id="collapse{{$index}}"
			class="panel-collapse collapse accordionContent_serviceMapping collapse{{$index}}"
			role="tabpanel" aria-labelledby="headingOne_inpsection">
			<div id='excelExportInspection'
				class="panel-body accorMainContent accorMainContentInspection">
				<table
					class="inspectionDetailTable table table-responsive container-fluid">
					<tbody>

						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<!--inspection -->
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.documentRc"
								disabled> <span>RC Document</span> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.documentRcRemarks"
								tooltip="{{inspection.documentRcRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.documentRcRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.documentInsurance"
								disabled> <span>Insurance Document</span><br /> <input
								type="text"
								class="form-control inpectionReadonlyDetails input-sm"
								ng-model="inspection.documentInsuranceRemarks"
								tooltip="{{inspection.documentInsuranceRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.documentInsuranceRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.documentDriverLicence" disabled> <span>Driver
									License</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.documentDriverLicenceRemarks"
								tooltip="{{inspection.documentDriverLicenceRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.documentDriverLicenceRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.documentUpdateJmp"
								disabled> <span>Update JMP</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.documentUpdateJmpRemarks"
								tooltip="{{inspection.documentUpdateJmpRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.documentUpdateJmpRemarks == 'N'"></td>
						</tr>

						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.firstAidKit"
								disabled> <span>First Aid Kit</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.firstAidKitRemarks"
								tooltip="{{inspection.firstAidKitRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.firstAidKitRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.fireExtingusher"
								disabled> <span>Fire Extinguisher</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.fireExtingusherRemarks"
								tooltip="{{inspection.fireExtingusherRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.fireExtingusherRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.allSeatBeltsWorking" disabled> <span>All
									Seat Belt Working</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.allSeatBeltsWorkingRemarks"
								tooltip="{{inspection.allSeatBeltsWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.allSeatBeltsWorkingRemarks == 'N'">
							</td>

							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.placard" disabled>
								<span>Placard</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.placardRemarks"
								tooltip="{{inspection.placardRemarks}}" tooltip-placement="top"
								tooltip-trigger="click" readonly
								ng-hide="inspection.placardRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.mosquitoBat"
								disabled> <span>Mosquito Bat</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.mosquitoBatRemarks"
								tooltip="{{inspection.mosquitoBatRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.mosquitoBatRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.panicButton"
								disabled> <span>Panic Button</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.panicButtonRemarks"
								tooltip="{{inspection.panicButtonRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.panicButtonRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.walkyTalky"
								disabled> <span>Walky Talky</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.walkyTalkyRemarks"
								tooltip="{{inspection.walkyTalkyRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.walkyTalkyRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.GPS" disabled>
								<span>GPS</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.gpsRemarks"
								tooltip="{{inspection.gpsRemarks}}" tooltip-placement="top"
								tooltip-trigger="click" readonly
								ng-hide="inspection.gpsRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.driverUniform"
								disabled> <span>Driver Uniform</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.driverUniformRemarks"
								tooltip="{{inspection.driverUniformRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.driverUniformRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.stephney" disabled>
								<span>Stepney</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.stephneyRemarks"
								tooltip="{{inspection.stephneyRemarks}}" tooltip-placement="top"
								tooltip-trigger="click" readonly
								ng-hide="inspection.stephneyRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.umbrella" disabled>
								<span>Umbrella</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.umbrellaRemarks"
								tooltip="{{inspection.umbrellaRemarks}}" tooltip-placement="top"
								tooltip-trigger="click" readonly
								ng-hide="inspection.umbrellaRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.torch" disabled>
								<span>Torch</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.torchRemarks"
								tooltip="{{inspection.torchRemarks}}" tooltip-placement="top"
								tooltip-trigger="click" readonly
								ng-hide="inspection.torchRemarks == 'N'"></td>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.toolkit" disabled>
								<span>Toolkit</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.toolkitRemarks"
								tooltip="{{inspection.toolkitRemarks}}" tooltip-placement="top"
								tooltip-trigger="click" readonly
								ng-hide="inspection.toolkitRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.seatUpholtseryCleanNotTorn" disabled>
								<span>Seat Upholstery is Clean / Not Torn</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.seatUpholtseryCleanNotTornRemarks"
								tooltip="{{inspection.seatUpholtseryCleanNotTornRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.seatUpholtseryCleanNotTornRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.vehcileRoofUpholtseryCleanNotTorn" disabled>
								<span>Vehicle Roof Upholstery is clean and not torn</span><br />

								<input type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.vehcileRoofUpholtseryCleanNotTornRemarks"
								tooltip="{{inspection.vehcileRoofUpholtseryCleanNotTornRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.vehcileRoofUpholtseryCleanNotTornRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.vehcileFloorUpholtseryCleanNotTorn"
								disabled> <span>Vehicle Floor Upholstery is clean
									and not torn</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.vehcileFloorUpholtseryCleanNotTornRemarks"
								tooltip="{{inspection.vehcileFloorUpholtseryCleanNotTornRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.vehcileFloorUpholtseryCleanNotTornRemarks == 'N'">
							</td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.vehcileDashboardClean" disabled> <span>Vehicle
									Dashboard is Clean </span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.vehcileDashboardCleanRemarks"
								tooltip="{{inspection.vehcileDashboardCleanRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.vehcileDashboardCleanRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.vehicleGlassCleanStainFree" disabled>
								<span>Vehicle glasses is Clean / Stain Free / No Films
									etc</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.vehicleGlassCleanStainFreeRemarks"
								tooltip="{{inspection.vehicleGlassCleanStainFreeRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.vehicleGlassCleanStainFreeRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.vehicleInteriorLightBrightWorking" disabled>
								<span>Vehicle Interior Lighting is Bright and working</span><br />

								<input type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.vehicleInteriorLightBrightWorkingRemarks"
								tooltip="{{inspection.vehicleInteriorLightBrightWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.vehicleInteriorLightBrightWorkingRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.bolsterSeperatingLastTwoSeats" disabled>
								<span>Bolster Seperating the Last two seats (Only in
									Innova)</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.bolsterSeperatingLastTwoSeatsRemarks"
								tooltip="{{inspection.bolsterSeperatingLastTwoSeatsRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.bolsterSeperatingLastTwoSeatsRemarks == 'N'">
							</td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.exteriorScratches"
								disabled> <span>Scratches on the body </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.exteriorScratchesRemarks"
								tooltip="{{inspection.exteriorScratchesRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.exteriorScratchesRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.noPatchWork"
								disabled> <span>No Patch Work</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.noPatchWorkRemarks"
								tooltip="{{inspection.noPatchWorkRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.noPatchWorkRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.pedalBrakeWorking"
								disabled> <span>Pedal Brake Working</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.pedalBrakeWorkingRemarks"
								tooltip="{{inspection.pedalBrakeWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.pedalBrakeWorkingRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.handBrakeWorking"
								disabled> <span>Hand Brake Working</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.handBrakeWorkingRemarks"
								tooltip="{{inspection.handBrakeWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.handBrakeWorkingRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.tyresNoDentsTrimWheel" disabled> <span>No
									Dents on the Trim of the Wheel</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.tyresNoDentsTrimWheelRemarks"
								tooltip="{{inspection.tyresNoDentsTrimWheelRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.tyresNoDentsTrimWheelRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.tyresGoodCondition" disabled> <span>Tyres
									in good condition</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.tyresGoodConditionRemarks"
								tooltip="{{inspection.tyresGoodConditionRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.tyresGoodConditionRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.allTyresAndStephneyInflate" disabled>
								<span>All Tyres including Stephney Inflate</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.allTyresAndStephneyInflateRemarks"
								tooltip="{{inspection.allTyresAndStephneyInflateRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.allTyresAndStephneyInflateRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.jackAndTool"
								disabled> <span>Jack & Tools</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.jackAndToolRemarks"
								tooltip="{{inspection.jackAndToolRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.jackAndToolRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.wiperWorking"
								disabled> <span>Wiper Working</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.wiperWorkingRemarks"
								tooltip="{{inspection.wiperWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.wiperWorkingRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.acCoolingIn5mins"
								disabled> <span>Cooling achieved end to end in 5
									mins </span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.acCoolingIn5minsRemarks"
								tooltip="{{inspection.acCoolingIn5minsRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.acCoolingIn5minsRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.noSmellInAC"
								disabled> <span>No Smell in AC </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.noSmellInACRemarks"
								tooltip="{{inspection.noSmellInACRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.noSmellInACRemarks == 'N'"></td>

							<td class="inspectionAccordTd" class="inspectionAccordTd"><input
								type="checkbox" class='radiobuttonRight'
								ng-model="inspection.acVentsClean" disabled> <span>AC
									vents are clean </span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.acVentsCleanRemarks"
								tooltip="{{inspection.acVentsCleanRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.acVentsCleanRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.engionOilChangeIndicatorOn" disabled>
								<span>Engine oil Change Indicator ON </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.enginOilChangeIndicatorOnRemarks"
								tooltip="{{inspection.enginOilChangeIndicatorOnRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.enginOilChangeIndicatorOnRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.coolantIndicatorOn" disabled> <span>Coolant
									Indicator ON</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.coolantIndicatorOnRemarks"
								tooltip="{{inspection.coolantIndicatorOnRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.coolantIndicatorOnRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.brakeClutchIndicatorOn" disabled> <span>Brake
									& Clutch oil Indicator On </span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails input-sm"
								ng-model="inspection.brakeClutchIndicatorOnRemarks"
								tooltip="{{inspection.brakeClutchIndicatorOnRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.brakeClutchIndicatorOn == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.highBeamWorking"
								disabled> <span>High Beam Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.highBeamWorkingRemarks"
								tooltip="{{inspection.highBeamWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.highBeamWorkingRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.lowBeamWorking"
								disabled> <span>Low Beam Working</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.lowBeamWorkingRemarks"
								tooltip="{{inspection.lowBeamWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.lowBeamWorkingRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.rightFrontIndicatorWorking" disabled>
								<span>Right Front Indicator Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.rightFromtIndicatorWorkingRemarks"
								tooltip="{{inspection.rightFromtIndicatorWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.rightFromtIndicatorWorkingRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.leftFrontIndicatorWorking" disabled>
								<span>Left Front Indicator Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.leftFrontIndicatorWorkingRemarks"
								tooltip="{{inspection.leftFrontIndicatorWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.leftFrontIndicatorWorkingRemarks == 'N'">
							</td>

							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.parkingLightsWorking" disabled> <span>Parking
									Lights Working</span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.parkingLightsWorkingRemarks"
								tooltip="{{inspection.parkingLightsWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.parkingLightsWorkingRemarks == 'N'">
							</td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.ledDayTimeRunningLightWorking" disabled>
								<span>LED Day Time Running Lights Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.ledDayTimeRunningLightWorkingRemarks"
								tooltip="{{inspection.ledDayTimeRunningLightWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.ledDayTimeRunningLightWorkingRemarks == 'N'">
							</td>

							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.rightRearIndicatorWorking" disabled>
								<span>Right Rear Indicator Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.rightRearIndicatorWorkingRemarks"
								tooltip="{{inspection.rightRearIndicatorWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.rightRearIndicatorWorkingRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.leftRearIndicatorWorking" disabled>
								<span>Left Rear Indicator Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.leftRearIndicatorWorkingRemarks"
								tooltip="{{inspection.leftRearIndicatorWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.leftRearIndicatorWorkingRemarks == 'N'">
							</td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.brakeLightsOn"
								disabled> <span>Brake Lights On</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.brakeLightsOnRemarks"
								tooltip="{{inspection.brakeLightsOnRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.brakeLightsOnRemarks == 'N'"></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.reverseLightsOn"
								disabled> <span>Reverse Lights On </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.reverseLightsOnRemarks"
								tooltip="{{inspection.reverseLightsOnRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.reverseLightsOnRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.hornWorking"
								disabled> <span>Horn Working </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.hornWorkingRemarks"
								tooltip="{{inspection.hornWorkingRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.hornWorkingRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight'
								ng-model="inspection.reflectionSignBoard" disabled> <span>Reflective
									sign board </span><br /> <input type="text"
								class="form-control inpectionReadonlyDetails"
								ng-model="inspection.reflectionSignBoardRemarks"
								tooltip="{{inspection.reflectionSignBoardRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.reflectionSignBoardRemarks == 'N'">
							</td>


							<td class="inspectionAccordTd"><span
								class="inspectiondetailspan">Number of Punctures done</span> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.numberofPunctruresdone" readonly> <!--
								<select ng-model="inspection.numberofPunctruresdone" 
										ng-options="puncture for puncture in punctures track by puncture" 
										disabled><br /></select>
--></td>
						</tr>
						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.audioWorkingOrNot"
								disabled> <span>Audio Working Or Not </span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.audioWorkingOrNotRemarks"
								tooltip="{{inspection.audioWorkingOrNotRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.audioWorkingOrNotRemarks == 'N'"></td>
							<td class="inspectionAccordTd"><span
								class="inspectiondetailspan">Diesel</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.diesel" readonly> <!--
								<select ng-model="inspection.diesel" 
										ng-options="diesel for diesel in diesels"
									   disabled>
								</select>
--></td>
							<td class="inspectionAccordTd"><span
								class="inspectiondetailspan">Driver Name</span> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.driverName" readonly></td>
							<td class="inspectionAccordTd" colspan='2'><span
								class="inspectiondetailspan">Documents</span>
								<div class="row imagesTdInspectionDetail">
									<div class="floatLeft imageDiv_approval imageBorder"
										id="div{{$index}}"
										ng-show="inspection.InspDocs.vehicleInsepectionLeftImage">
										<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
										<a href="{{inspection.InspDocs.vehicleInsepectionLeftImage}}"
											class="noLinkLine"> <img
											class="img-responsive img-rounded fileImg"
											ng-src="{{inspection.InspDocs.vehicleInsepectionLeftImage}}">
											<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
										</a>
									</div>
									<div class="floatLeft imageDiv_approval imageBorder"
										id="div{{$index}}"
										ng-show="inspection.InspDocs.vehicleInsepectionRightImage">
										<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
										<a href="{{inspection.InspDocs.vehicleInsepectionRightImage}}"
											class="noLinkLine"> <img
											class="img-responsive img-rounded fileImg"
											ng-src="{{inspection.InspDocs.vehicleInsepectionRightImage}}">
											<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
										</a>
									</div>
									<div class="floatLeft imageDiv_approval imageBorder"
										id="div{{$index}}"
										ng-show="inspection.InspDocs.vehicleInsepectionBackImage">
										<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
										<a href="{{inspection.InspDocs.vehicleInsepectionBackImage}}"
											class="noLinkLine"> <img
											class="img-responsive img-rounded fileImg"
											ng-src="{{inspection.InspDocs.vehicleInsepectionBackImage}}">
											<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
										</a>
									</div>
									<div class="floatLeft imageDiv_approval imageBorder"
										id="div{{$index}}"
										ng-show="inspection.InspDocs.vehicleInsepectionFrontImage">
										<!--                                   <i class = "icon-remove-sign pointer" ng-click="deleteDocument($index)"></i>-->
										<a href="{{inspection.InspDocs.vehicleInsepectionFrontImage}}"
											class="noLinkLine"> <img
											class="img-responsive img-rounded fileImg"
											ng-src="{{inspection.InspDocs.vehicleInsepectionFrontImage}}">
											<!--                                            <div class = "docName"><span>{{doc.type}}</span></div>-->
										</a>
									</div>

								</div></td>

						</tr>

						<!-- // New -->

						<tr
							ng-repeat="inspection in inspectionDetailData | filter: {inspectionId:date.inspectionId} | filter: {inspectionDate:date.inspectionDate}">
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.fogLights"
								disabled> <span>Fog Light</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.fogLightsRemarks"
								tooltip="{{inspection.fogLightsRemarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.fogLightsRemarks == 'N'"></td>
							
							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.driverCheckInDidOrNot"
								disabled> <span>Driver Check In Did Or Not</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.driverCheckInDidOrNot"
								tooltip="{{inspection.driverCheckInDidOrNot}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.driverCheckInDidOrNotRemarks == 'N'"></td>

							<td class="inspectionAccordTd"><input type="checkbox"
								class='radiobuttonRight' ng-model="inspection.remarks"
								disabled> <span>Remarks</span><br /> <input
								type="text" class="form-control inpectionReadonlyDetails"
								ng-model="inspection.remarks"
								tooltip="{{inspection.remarks}}"
								tooltip-placement="top" tooltip-trigger="click" readonly
								ng-hide="inspection.remarks == 'N'"></td>

						</tr>

					</tbody>
				</table>
			</div>
		</div>
	</div>