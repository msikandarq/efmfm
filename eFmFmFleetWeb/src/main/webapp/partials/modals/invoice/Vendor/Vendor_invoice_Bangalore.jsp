 <div class = "col-md-12 mainInHeader" div ng-if = 'gotVendorData'>
        <div id = "exportableByVendor" class = "row pointer VendorInvoiceHeight" >
            <table class = "table table-responsive container-fluid">
                <thead class ="invoceThead"> 
                    <tr class = "mainFirstHeaderIn">
                        <th class = "col-md-4"><div class = "mainFirstInHeaderDiv"><span>I N V O I C E</span></div></th>
                        <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "GrandTotalDiv widthByVendorHeading">
                            <span>Grand Total Rs.</span><br />
                            <span class= "rsSpan">{{invoiceByVendorData.total}}</span></div></th>
                        <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total1Div widthByVendorHeading">
                            <span>Service Tax </span><br />
                            <span class= "rsSpan">{{invoiceByVendorData.serviceTaxAmount}}</span></div></th>
                        <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total2Div widthByVendorHeading">
                            <span>Total Penalty Rs.</span><br />
                            <span class= "rsSpan">{{invoiceByVendorData.penalty}}</span></div></th>
                        <th class = "col-md-2 paddiingLeft0 paddiingRight2"><div class = "total3Div widthByVendorHeading">
                            <span>Total PayableAmount</span><br />
                            <span class= "rsSpan">{{invoiceByVendorData.totalPayableAmount}}</span></div></th>
                    </tr> 
                </thead>
                <tbody class = "secondTableMain">
                    <tr>
                        <td colspan = "12">
                            <div class = "secondTableDiv row"><div class = "col-md-12 paddingLeft0">
                                <table class = "invoiceTable invoiceByVendorTable table table-bordered table-responsive container-fluid VehicleInvoiceWidth" >
                                    <thead class ="tableHeading">
                                        <tr>
                                            <th class = "vendorNameHeading" colspan = "15">
                                                <span class = "floatRight">Invoice Date : {{monthYear}}</span><br />

                                                <span class = "floatRight">Invoice Number : {{invoiceByVendorData.invoiceNumber}}</span>
                                                <span class = "floatLeft">Vendor Name : {{invoiceByVendorData.vendorName}}</span>
                                            </th> 
                                        </tr>
                                        <tr>
                                            <th class = ""><div><span>Vehicle Number</span></div></th>
                                            <th class = ""><div><span>Contracted Per Day Amount</span></div></th>
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
                                        <tr ng-repeat = 'invoice in vendorFixedDistanceBasedData' ng-class="{modifiedColorInvoice: invoice.approvalStatus == 'M'}">  
                                          <td class='col-md-1'>{{invoice.vehicleNumber}}</td> 
                                          <td class='col-md-1'>{{invoice.contractPerDayAmount}}</td> 
                                          <td class='col-md-1'>{{invoice.perKmCost}}</td>
                                          <td class='col-md-3'>
                                              <span ng-show = "!invoice.editByVendorInvoiceClicked">{{invoice.totalKm}}</span>
                                              <input ng-show = "invoice.editByVendorInvoiceClicked" 
                                                     type = 'number'
                                                     class = "form-control" 
                                                     ng-model = "invoice.totalKm"
                                                     ng-change = "checkTotalKmValue(invoice, $index)" only-num >
                                          </td>    
                                          <td class='col-md-3' ng-show="invoiceByVendorData.contractType == 'PDDC'">
                                              <span>{{invoice.totalKmAmount}}</span>
                                          </td>                                           
                                          <!-- <td class='col-md-3'> 
                                              <span>{{invoice.extraKm}}</span>
                                          </td> -->
                                           <td class='col-md-3'> 
                                              <span ng-show = "!invoice.editByVendorInvoiceClicked">{{invoice.fuelExtraAmount}}</span>
                                              <input ng-show = "invoice.editByVendorInvoiceClicked" 
                                                     type = 'number' 
                                                     ng-change = "checkTotalKmValue(invoice, $index)"
                                                     class = "form-control invoiceInputWidth" 
                                                     ng-model = "invoice.fuelExtraAmount" only-num>
                                          </td>
                                          <!-- <td class='col-md-3'>
                                              <span>{{invoice.extraKmcharges}}</span>
                                            </td> -->
                                          <td>
                                              <span  ng-show = "!invoice.editByVendorInvoiceClicked">{{invoice.absentDays}}</span>
                                              <input ng-show = "invoice.editByVendorInvoiceClicked" 
                                                     type = 'number' 
                                                     ng-change = "checkTotalKmValue(invoice, $index)"
                                                     class = "form-control invoiceInputWidth" 
                                                     ng-model = "invoice.absentDays" only-num>
                                          </td>

                                          <td class='col-md-1'>
                                              <span>{{invoice.totalpenalityAmnt}}</span>
                                          </td>                                                                 
                                          <td>
                                              <span>{{invoice.totalPerDayDeduction}}</span>
                                          </td>
                                          <td class='col-md-2'>
                                              <span>{{invoice.totalDeduction}}</span>
                                          </td>

                                          <td class='col-md-1 totalKmTd'>
                                              <span>{{invoice.totalAmount}}</span>
                                          </td>
                                         <td class='col-md-2'>
                                              <span  ng-show = "!invoice.editByVendorInvoiceClicked">{{invoice.invoiceRemarks}}</span>

                                             <textarea ng-model="invoice.invoiceRemarks" 
                                                       ng-show = "invoice.editByVendorInvoiceClicked"
                                                       class = "textareaWidthStyle" maxlength="200"></textarea>
                                           </td>
                      
                      
                                          <td class='col-md-1'>
                                              <input type = 'button' 
                                                     class = 'btn btn-warning btn-sm' 
                                                     ng-show = "!invoice.editByVendorInvoiceClicked"
                                                     value = 'Edit'
                                                     ng-click = "editInvoice('byVendor', invoice)">
                                              <input type = 'button' 
                                                     class = 'btn btn-success btn-sm' 
                                                     ng-show = "invoice.editByVendorInvoiceClicked"
                                                     value = 'Save'
                                                     ng-click = "saveChangesInvoice('byVendor', invoice, $index)">
                                               <input type = 'button' 
                                                     class = 'btn btn-danger btn-sm' 
                                                     ng-show = "invoice.editByVendorInvoiceClicked"
                                                     value = 'Cancel'
                                                     ng-click = "cancelChangesInvoice('byVendor', invoice, $index)">


                                          </td>
                                        </tr>   
                                   </tbody>

                                </table>

                                </div></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>


