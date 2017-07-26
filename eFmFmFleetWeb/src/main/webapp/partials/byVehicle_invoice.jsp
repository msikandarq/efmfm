<div class = "col-md-12 mainInHeader" div ng-if = 'gotVehicleData'>
    <div id = "exportableByVehicle" class = "row">
        <table class = "table table-responsive container-fluid">
            <thead class ="invoceThead">
                <tr class = "mainFirstHeaderIn">
                    <th class = "col-md-4"><div class = "mainFirstInHeaderDiv"><span>I N V O I C E</span></div></th>
                    <th class = "col-md-3 paddiingLeft0 paddiingRight2"><div class = "GrandTotalDiv vendorNameByVehicle">
                        <span>Vendor Name</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData[0].vendorName}}</span></div></th>
                    <th class = "col-md-3 paddiingLeft0 paddiingRight2"><div class = "total1Div vehicleNum">
                        <span>Vehicle Number</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData[0].vehicleNumber}}</span></div></th>

                    <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total2Div grandTotalByVehicle">
                        <span>Grand Total (Rs.)</span><br />
                        <span class= "rsSpan">{{vehicleFixedDistanceBasedData[0].totalAmount}} Rs.</span></div></th>
              <!--      <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total3Div">
                        <span>Total Extra Rs.</span><br />
                        <span class= "rsSpan">1,340 Rs.</span></div></th>
-->
                </tr>
            </thead>
            <tbody class = "secondTableMain">
                <tr>
                    <td colspan = "12">
                        <div class = "secondTableDiv row">
                            <div class = "col-md-4 extraInByVehicleDiv">
                            <table class = "invoiceTable invoiceByVendorTable byVehicleTable2 table table-responsive container-fluid">
<!--
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
-->
                                    <tbody>
                                        <tr>
                                            <td>Invoice Number</td>
                                            <td>{{vehicleFixedDistanceBasedData[0].invoiceNumber}}</td>
                                        </tr>
                                        <tr>
                                            <td>Invoice Month Date</td>
                                            <td>{{vehicleFixedDistanceBasedData[0].invoiceMonthDate}}</td>
                                        </tr>
                                        <tr>
                                            <td>Vendor Name</td>
                                            <td>{{vehicleFixedDistanceBasedData[0].vendorName}}</td>
                                        </tr>
                                        <tr>
                                            <td>Vehicle Numbe</td>
                                            <td>{{vehicleFixedDistanceBasedData[0].vehicleNumber}}</td>
                                        </tr>
                                        <tr>
                                            <td>Absent Days</td>
                                            <td>{{vehicleFixedDistanceBasedData[0].absentDays}}</td>
                                        </tr>
                                        <tr>
                                            <td>Total Working Days</td>
                                            <td>{{vehicleFixedDistanceBasedData[0].totalWorkingDays}}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class = "col-md-8">
                                <table class = "invoiceTable invoiceByVendorTable byVehicleTable table table-responsive container-fluid">
                                    <thead>
                                        <tr>
                                            <th class = "mainByVehicle_header byVehicleInvoiceHeader"></th>
                                            <th class = "mainByVehicle_header byVehicleInvoiceDistance" >Distance (Km)</th>                                    
                                            <th class = "mainByVehicle_header byVehicleInvoiceCharges">Charges (Rs.)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td class = "mainByVehicle_header2">Fixed/Contract</td>
                                            <td class = "contentByVehicle invoiceTdByVehicle">{{vehicleFixedDistanceBasedData[0].contractKM}}</td>
                                            <td class = "contentByVehicle invoiceTdByVehicle contractAmountBg" >{{vehicleFixedDistanceBasedData[0].contractAmount}}</td>
                                        </tr>
                                        <tr>
                                            <td class = "mainByVehicle_header2 paddingBottom0" >Extra</td>
                                            <td class = "contentByVehicle paddingBottom0 invoiceTdByVehicle" >
                                            </td>
                                            <td class = "contentByVehicle paddingBottom0 invoiceTdByVehicle chargeAmountBg " >
                                                <span>+ {{vehicleFixedDistanceBasedData[0].extraKmcharges}}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class = "mainByVehicle_header2 paddingTop0">Total (Fixed/Contract + Extra)</td>
                                            <td class = "contentByVehicle paddingTop0 invoiceTdByVehicle">
                                                <span>--------------</span><br />
                                                <span class = "resultByvehicle invoiceTdByVehicle">{{vehicleFixedDistanceBasedData[0].totalKm}}</span> <br />
                                                <span>--------------</span>
                                            </td>
                                            <td class = "contentByVehicle paddingTop0 invoiceTdByVehicle chargeAmountBg">
                                                <span>--------------</span><br />
                                                <span class = "resultByvehicle">{{vehicleFixedDistanceBasedData[0].extraKmcharges +vehicleFixedDistanceBasedData[0].contractAmount}}</span><br />
                                                <span>--------------</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class = "mainByVehicle_header2">Penalty</td>
                                            <td class = "contentByVehicle invoiceTdByVehicle"></td>
                                            <td class = "contentByVehicle invoiceTdByVehicle chargeAmountBg"> - {{vehicleFixedDistanceBasedData[0].penalty}}</td>
                                        </tr>
                                        <tr>
                                            <td class = "mainByVehicle_header3">Grand Total</td>
                                            <td class = "contentByVehicle invoiceGrandTotalByVehicle invoiceTdByVehicle2">
                                                <span>==========</span><br />
                                                <span class = "resultByvehicleGrand ">{{vehicleFixedDistanceBasedData[0].totalKm}}</span><br />
                                                <span>----------------</span>
                                            </td>
                                            <td class = "contentByVehicle invoiceGrandTotalByVehicle invoiceTdByVehicle2 byVehicleInvoiceGrandTotal">
                                                <span>==========</span><br />
                                                <span class = "resultByvehicleGrand ">{{vehicleFixedDistanceBasedData[0].totalAmount}}</span><br />
                                                <span>----------------</span>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            
<!--                            <div class = "col-md-2"></div>-->
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>