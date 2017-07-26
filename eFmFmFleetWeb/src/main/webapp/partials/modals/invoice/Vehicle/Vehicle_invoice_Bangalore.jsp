<div class = "col-md-12 mainInHeader" div ng-if = 'gotVehicleData'>
    <div id = "exportableByVendor" class = "row">
        <table class = "table table-responsive container-fluid">
            <thead class ="invoceThead">
                <tr class = "mainFirstHeaderIn">
                    <th class = "col-md-4"><div class = "mainFirstInHeaderDiv"><span>I N V O I C E</span></div></th>
                    <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "GrandTotalDiv widthByVendorHeading">
                        <span>Grand Total Rs.</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData.totalAmount}} Rs.</span></div></th>
                    <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total1Div widthByVendorHeading">
                        <span>Total Km</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData.totalKm}} Km.</span></div></th>
                    <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total2Div widthByVendorHeading">
                        <span>Total Penalty Rs.</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData.totalpenalityAmnt}} Rs.</span></div></th>
                    <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total3Div widthByVendorHeading">
                        <span>Total Extra Rs.</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData.extraKmcharges}} Rs.</span></div></th>
                </tr>
            </thead>
            
            <tbody class = "secondTableMain">
                <tr>
                    <td colspan = "12">
                        <div class = "secondTableDiv row"><div class = "col-md-12 paddingLeft0">
                            <table class = "invoiceTable invoiceByVendorTable VehicleInvoiceWidth table table-bordered table-responsive container-fluid " >
                                <thead class ="tableHeading">
                                    <tr>
                                        <th class = "vendorNameHeading" colspan = "15">
                                            <span class = "floatLeft">Vendor Name : {{vehicleFixedDistanceBasedData.vendorName}}</span>

                                            <span class = "floatRight">Invoice Number : {{vehicleFixedDistanceBasedData.invoiceNumber}}</span>
                                        </th>
                                    </tr>
                                     <tr>
                                            <th class = ""><div><span>Vehicle Number</span></div></th>
                                            <th class = ""><div><span>Total Per Day Contracted Amount</span></div></th>
                                            <th class = ""><div><span>Per Km Amount</span></div></th>
                                            <th class = ""><div><span>Total (km)</span></div></th>
                                            <th class = ""><div><span>Total (km) Amount</span></div></th>
                                            <!-- <th class = ""><div><span>Extra KMs</span></div></th> -->
                                              <th class = ""><div><span>Fuel Amount</span></div></th>
                                            <!-- <th class = ""><div><span>Extra KM Amount</span></div></th> -->
                                           

                                            <th class = ""><div><span>No of Days Absent</span></div></th>
                                            <th class = ""><div><span>Penalty</span></div></th>
                                            <th class = ""><div><span>Deduction</span></div></th>
                                            <th class = ""><div><span>Penalty Net Amount<br /> (After deduction)</span></div></th>   

                                            <th class = "totalKmTd"><div><span>Total Amount<br /> (Amount + Ex KM Amt)</span></div></th>
                                            <th>Remarks</th>
                                            <th>Edit</th> 
                                        </tr>
                                </thead>
                                <tbody class = "invoiceTbody invoiceBodyByVendor">   
                                    <tr >                                        
                                      <td class='col-md-1'>{{vehicleFixedDistanceBasedData.vehicleNumber}}</td> 
                                      <td class='col-md-1'>{{vehicleFixedDistanceBasedData.contractPerDayAmount}}</td>
                                      <td class='col-md-1'>{{vehicleFixedDistanceBasedData.perKmCost}}</td>
                                      <td class='col-md-1'>
                                          <span ng-show = "!vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked">{{vehicleFixedDistanceBasedData.totalKm}}</span>
                                          <input ng-show = "vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked" 
                                                 type = 'text' 
                                                 class = "form-control" 
                                                 ng-model = "vehicleFixedDistanceBasedData.totalKm"
						                                     ng-change = "checkTotalKmValue(vehicleFixedDistanceBasedData, $index)" only-num>
                                      </td>    
                                      <td class='col-md-1'>
                                              <span>{{vehicleFixedDistanceBasedData.totalKmAmount}}</span>
                                          </td>                                        
                                     <!--  <td class='col-md-1'>
                                          <span>{{vehicleFixedDistanceBasedData.extraKm}}</span>
                                      </td> -->
                                      <!-- <td class='col-md-1'>
                                          <span>{{vehicleFixedDistanceBasedData.fuelExtraAmount}}</span>
                                      </td> -->

                                      <td class='col-md-1'>
                                          <span ng-show = "!vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked">{{vehicleFixedDistanceBasedData.fuelExtraAmount}}</span>
                                          <input ng-show = "vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked" 
                                                 type = 'text' 
                                                 ng-change = "checkTotalKmValue(vehicleFixedDistanceBasedData, $index)"
                                                 class = "form-control" 
                                                 ng-model = "vehicleFixedDistanceBasedData.fuelExtraAmount">
                                      </td>
                                      
                                      <!-- <td class='col-md-1'>
                                          <span>{{vehicleFixedDistanceBasedData.extraKmcharges}}</span>
                                      </td> -->
                                      <td class='col-md-1'>
                                          <span ng-show = "!vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked">{{vehicleFixedDistanceBasedData.absentDays}}</span>
                                          <input ng-show = "vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked" 
                                                 type = 'text' 
	                                               ng-change = "checkTotalKmValue(vehicleFixedDistanceBasedData, $index)"
                                                 class = "form-control" 
                                                 ng-model = "vehicleFixedDistanceBasedData.absentDays">
                                      </td>

                                      <td class='col-md-1'>
                                          <span>{{vehicleFixedDistanceBasedData.totalpenalityAmnt}}</span>
                                      </td>                                                                 
                                      <td class='col-md-1'>
                                          <span>{{vehicleFixedDistanceBasedData.totalPerDayDeduction}}</span>
                                      </td>
                                      <td class='col-md-2'>
                                          <span>{{vehicleFixedDistanceBasedData.totalDeduction}}</span>
                                      </td> 

                                      <td class='col-md-1 totalKmTd'>
                                          <span>{{vehicleFixedDistanceBasedData.totalAmount}}</span>
                                      </td> 

                                      <td class='col-md-2'>
                                          <span  ng-show = "!vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked">{{vehicleFixedDistanceBasedData.invoiceRemarks}}</span>

                                          <textarea ng-model="vehicleFixedDistanceBasedData.invoiceRemarks" 
                                          ng-show = "vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked"
                                          class = "textareaWidthStyle" maxlength="200" ></textarea>
                                      </td>  

                                      <td class='col-md-1'>
                                          <input type = 'button' 
                                                 class = 'btn btn-warning btn-sm' 
                                                 ng-show = "!vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked"
                                                 value = 'Edit'
                                                 ng-click = "editInvoice('byVehicle', vehicleFixedDistanceBasedData, $index)">
                                          <input type = 'button' 
                                                 class = 'btn btn-success btn-sm' 
                                                 ng-show = "vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked"
                                                 value = 'Save'
                                                 ng-click = "saveChangesInvoice('byVehicle', vehicleFixedDistanceBasedData, $index)">
                                          <input type = 'button'  
                                                     class = 'btn btn-danger btn-sm' 
                                                     ng-show = "vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked"
                                                     value = 'Cancel' 
                                                     ng-click = "cancelChangesVehicle('byVehicle', vehicleFixedDistanceBasedData, $index)">
                                      </td>
                                    </tr> 
                                </tbody>
                            </table>
                           </div>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

    </div>
</div>