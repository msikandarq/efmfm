<!-- 
@date           03/01/2016
@Author         Saima Aziz
@Description    This template is called by the home.invoice to show the Invoice by Vendor
@State          
@URL               

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->
         
                           
    <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "partialInvoiceLoad">
        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
    </div>    
    <div class = "col-md-12 mainInHeader" ng-show="partialInvoiceButtonClicked" >
        <div id = "exportableByVendor" class = "row">
            <div class="row">
              <div class="col-md-11"></div>
              <div class="col-md-1">
                <input type="button" name="" value="Excel" class="btn btn-success" ng-click="partialInvoiceExcel(partialInvoiceDetails)">
              </div>
            </div>
                <tbody class = "secondTableMain">
                    <tr>
                        <td colspan = "12">
                            <div class = "secondTableDiv row"><div class = "col-md-12 paddingLeft0">
                                <table class = "invoiceTable  table table-bordered table-responsive container-fluid">
                                
                                    <thead class ="tableHeading">
                                        
                                        <tr>
                                            <th class = ""><div><span>Vehicle Number</span></div></th>

                                            <th class = "" ng-show="partialInvoiceDetails[0].contractType == 'FDC'"><div><span>Contracted KMs</span></div></th>
                                            <th class = "" ng-show="partialInvoiceDetails[0].contractType == 'FDC'"><div><span>Contract Amount</span></div></th>
                                            <th class = "" ng-show="partialInvoiceDetails[0].contractType == 'PDDC'"><div><span>Per Day Contract Amount</span></div></th>
                                            <th class = "" ng-show="partialInvoiceDetails[0].contractType == 'PDDC'"><div><span>Per Km Amount</span></div></th>

                                            <th class = ""><div><span>Total (km) - {{distanceArrayData.selectedOption.value}}</span></div></th>

                                            <th class = ""><div><span>Extra KMs</span></div></th>
                                            <th class = ""><div><span>Total Km Amount</span></div></th>
                                            <th class = ""><div><span>Fuel Amount</span></div></th>
                                            <th class = ""><div><span>Extra KM Amount</span></div></th>
                                           

                                            <th class = ""><div><span>No of Days Absent</span></div></th>
                                            <th class = ""><div><span>Total Amount</span></div></th>
                                            
                                        </tr>  
                                    </thead> 
                                   
                                    <tbody class = "">    
                                        <tr ng-repeat = 'partialInvoice in partialInvoiceDetails' > 
                                          <td class='col-md-1'>{{partialInvoice.vehicleNumber}}</td> 
                                          <td class='col-md-1' ng-show="partialInvoice.contractType == 'FDC'">{{partialInvoice.contractedKm}}</td> 
                                          <td class='col-md-1' ng-show="partialInvoice.contractType == 'FDC'">{{partialInvoice.contractedAmount}}</td> 
                                          <td class='col-md-1' ng-show="partialInvoice.contractType == 'PDDC'">{{partialInvoice.contractPerDayAmount}}</td> 
                                          <td class='col-md-1' ng-show="partialInvoice.contractType == 'PDDC'">{{partialInvoice.perKmCost}}</td>
                                          
                                          <td class='col-md-1'>{{partialInvoice.totalKm}}
                                            <span
                                            class="floatRight"><input type="button"
                                            class="btn btn-xs btn-primary" value="View"
                                            ng-click="viewTotalKmInvoice('lg', partialInvoice, distanceArrayData.selectedOption.value)" name=""
                                            ></span>
                                          </td>
                                          <td class='col-md-1'>{{partialInvoice.extraKm}}</td>
                                          <td class='col-md-1'>{{partialInvoice.totalkmAmount}}</td>
                                          <td class='col-md-1'>{{partialInvoice.fuelExtraAmount}}</td>
                                          <td class='col-md-1'>{{partialInvoice.extraKmcharges}}</td>
                                          <td class='col-md-1'>{{partialInvoice.totalWorkingDays}} 
                                            <span
                                            class="floatRight"><input type="button"
                                            class="btn btn-xs btn-primary" value="View"
                                            ng-click="viewNoOfAbsent('lg',partialInvoice)" name=""
                                            ></span>
                                          </td>
                                          <td class='col-md-1'>{{partialInvoice.totalAmount}}</td>
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
