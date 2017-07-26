<!-- 
@date           01/05/2016 
@Author         Saima Aziz
@Description    This page gives the invoice details and an option to update the rates. 
@State          home.invoice 
@URL            /invoice    

CODE CHANGE HISTORY 
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
01/05/2016  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation 
-->

<div class = "invoiceTemplate container-fluid"  ng-if = "isInvoiceActive">
    <div class = "row">
        <div class = "col-md-12 col-xs-12 invoiceHeading1">            
            <span class = "col-md-12 col-xs-12">Financial Analytics</span>                      
            <div class = "col-md-12 col-xs-12 mainTabDiv_invoice">

            <!-- /*START OF WRAPPER1 = VENDORWISE INVOICE */ -->
               <div class = "wrapper1">             
                
                <tabset class="tabset subTab_invoice">
            
                      <tab ng-click = "vendorTab()">
                        <tab-heading>Generate Invoice</tab-heading>

                        <tabset class="tabset subTab_invoiceContract">
                              <tab ng-click = "">
                    <tab-heading ng-click = 'vendorTab()'>By Vendor</tab-heading>
                        <form class = "invoiceTabContent row" name = "invoiceByVendor">
                           <table class = "invoiceByVendorTable invoicebyVendorHeight table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>

                                      <th>Invoice Type</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'invoice'">Distance</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'invoice'">Select Vendor</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'invoice'">Monthly Summary</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'invoice'">Invoice Number</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'invoice'">Get Invoice</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'invoice'">Downlaod</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">Distance</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">Select Vendor</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">Select Vehicle</th>
                                      
                                      
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">Start Date</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">End Date</th>
                                      <th ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">Partial Invoice</th>
                                    </tr>  
                                </thead>
                                <tbody>
                               <tr>

                                  <td class = "">
                                      <select name="mySelect" class="form-control-width" 
                                              ng-options="option.type for option in invoiceTypeData.invoiceType track by option.value"
                                              ng-model="invoiceTypeData.selectedOption"
                                              ng-change="partialInvoiceChange()">
                                      </select>
                                  </td>
                               
                                  <!-- Normal Invoice -->

                                  <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'invoice'">
                                       <select name="mySelect" class="form-control-width"
                                              ng-options="option.type for option in distanceArrayData.distanceArray track by option.value"
                                              ng-model="distanceArrayData.selectedOption"
                                              ng-change = "setdistance(distanceArrayData.selectedOption)"
                                              >
                                      </select>
                                     </td>


                                  <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'invoice'">
                                    <select ng-model="vendors.selectVendor" 
                                            ng-options="vendorData.name for vendorData in vendorsData"
                                            name = "selectVendor"
                                            class = 'vendorSelect_invoiceByVendorForm'
                                            ng-disabled = 'isInvoice'
                                            ng-class = "{error: invoiceByVendor.selectVendor.$invalid && !invoiceByVendor.selectVendor.$pristine}"
                                            required>                 
                                            <option value="">SELECT VENDOR</option>
                                    </select> 
                                 </td>
                                  <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'invoice'">
                                          <div class = "input-group calendarInput">
                                          <span class="input-group-btn"><button class="btn btn-default" ng-click="selectMonthCal($event)"
                                                                                ng-disabled = 'isInvoice'>
                                              <i class = "icon-calendar calInputIcon"></i></button></span>
                                              <input type="text" 
                                                     class="form-control-width" 
                                                     ng-model="vendors.monthSelected" 
                                                     placeholder = "Select Month"
                                                     datepicker-popup = '{{monthformat}}'
                                                     datepicker-options="{{datepickerOptions}}"
                                                     is-open="datePicker.selectMonth"
                                                     max-date = "setMaxDate"
                                                     readonly
                                                     show-button-bar = false
                                                     required>
                                          </div>
                                       
                                  <td class = "invoiceNumber_invoice" ng-show="invoiceTypeData.selectedOption.value == 'invoice'">
                                         <div class = "floatLeft">OR</div>
                                          <input type = "text" 
                                                 ng-model = "invoiceByVendorData.invoiceNumber"
                                                 placeholder = "Enter Invoice Number" 
                                                 class = "form-control-width floatLeft"
                                                 ng-change="invoiceNumberChange()"
                                                 typeahead="invoiceNumberData.invoiceNumber for invoiceNumberData in invoiceNumbersData | filter:$viewValue | limitTo:8 "
                                                 required>  
                                       </td>
                                      <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'invoice'">
                                          <input type = 'button' 
                                                 class = 'btn btn-success searchButton_Invoice' 
                                                 value = "Get Invoice"
                                                 ng-click = "getInvoiceByVendor(vendors, invoiceByVendorData, distanceArrayData.selectedOption)"
                                                 ng-disabled = "!invoiceByVendor.$dirty"
                                                 ng-mouseenter="downloadVisible = true">
                                       </td>
                                      <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'invoice'">
                                        <input type = 'button' 
                                                 class = 'btn btn-warning searchButton_Invoice' 
                                                 value = "Download" 
                                                 ng-click = "downloadExcel('byVendor',vendors,invoiceByVendorData)"
                                                 ng-disabled="!downloadVisible">

                                      </td>
                                       <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                       <!-- <select ng-model="distanceFlg"
                                                   class="form-control" 
                                                   ng-options="distanceArray for distanceArray in applicationSettingsArrays.distanceArray"
                                                   
                                                   required>
                                                </select> -->
                                        <select name="mySelect" class="form-control-width"
                                              ng-options="option.type for option in distanceArrayData.distanceArray track by option.value"
                                              ng-model="distanceArrayData.selectedOption"
                                              ng-change = "setdistance(distanceArrayData.selectedOption)"
                                              >
                                      </select>

                                     </td>
                             
                                       <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                         <select ng-model="partialInvoice.vendor"
                                           class="form-control-width" 
                                           ng-options="allInspectionVendor.name for allInspectionVendor in allInspectionVendors"
                                           ng-change = "getVehicleList(partialInvoice.vendor)"
                                           >
                                           <option value="">SELECT VENDOR</option>
                                         </select>

                                      </td>

                                     <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                       <select ng-model="partialInvoice.vehicleSelected"
                                           class="form-control-width" 
                                           ng-options="allVehicle.vehicleNumber for allVehicle in allVehiclesInpectionForm track by allVehicle.Id"
                                          >
                                     <option value="">ALL VEHICLE</option>
                                      </select>
                                     </td>

                                    

                                      <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                               <div class = "input-group calendarInput">
                                                   
                                                    <span class="input-group-btn">
                                                    <button class="btn btn-default" 
                                                            ng-click="startDate($event,$index)" ng-disabled="">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>
                                                  
                                                    <input type="text" 
                                                       ng-model="partialInvoice.startDate" 
                                                       class="form-control-contract" 
                                                       placeholder = "Start Date" 
                                                       datepicker-popup="{{format}}"
                                                       is-open="datePicker[$index].startDate" 
                                                       ng-change = "checkDateRangeValidityStart(startDate, endDate,partialInvoice.startDate) "
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       required
                                                       readonly
                                                       name = 'startDate'
                                                       max-date = "setMaxDateEnd"
                                                      
                                                    >   
                                                 </div> 
                                      </td>

                                       <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                                <div class = "form-group"> 
                                                    <div class = "input-group calendarInput">
                                                    <span class="input-group-btn">
                                                    <button class="btn btn-default" 
                                                            ng-click="endDate($event,$index)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>
                                                  
                                                    <input type="text" 
                                                       ng-model="partialInvoice.endDate" 
                                                       class="form-control-contract" 
                                                       placeholder = "End Date" 
                                                       datepicker-popup="{{format}}"
                                                       is-open="datePicker[$index].endDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       required
                                                       readonly
                                                       name = 'endDate'
                                                       min-date = "partialInvoice.startDate"
                                                       max-date = "endDateDisabled"
                                                    >   
                                                 </div>
                                              </div> 
                                      </td>

                                      <td class = "" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                          <input type = 'button' 
                                                 class = 'btn btn-success' 
                                                 value = "Partial Invoice"
                                                 ng-click = "getPartialInvoice(partialInvoice,distanceArrayData.selectedOption)"
                                                 ng-mouseenter="downloadVisible = true">
                                      </td>
                                      <!-- <td class = "col-md-2" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">

                                      </td>
                                      <td class = "col-md-2" ng-show="invoiceTypeData.selectedOption.value == 'partialInvoice'">
                                          
                                      </td> -->
                                       </div>
                                   
                                </tr>                                    
                             </tbody>
                            </table>
                            <div ng-include = "'partials/byVendor_invoice.jsp'"></div> 
                            <div ng-include = "'partials/partial_invoice.jsp'"></div>
            </form>
          </tab>

              <tab ng-click = "">
                    <tab-heading ng-click = "vehicleTab()">By Vehicle</tab-heading>
                        <form class = "invoiceTabContent row" name = "invoiceByVehicle">
                           <table class = "invoiceByVendorTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Trip Sheet for the Vendor</th>
                                      <th>Select Vehicle</th>
                                      <th>Distance</th>
                                      <th>Monthly Summary</th>
                                      <th></th>
                                    </tr> 
                                </thead>
                                <tbody>
                               <tr>

                                      

                                      <td class = "col-md-2">
                                          <select ng-model="vendors.selectVendor" 
                                                  ng-change = "getVehicles(vendors.selectVendor)"
                                                  ng-options="vendorData.name for vendorData in vendorsData"
                                                  name = "selectVendor"
                                                  class = 'vendorSelect_invoiceByVendorForm'
                                                  ng-class = "{error: invoiceByVendor.selectVendor.$invalid && !invoiceByVendor.selectVendor.$pristine}" required>                 
                                                  <option value="">SELECT VENDOR</option>
                                          </select>  
                                       </td>
                                      <td class = "col-md-2">
                                          <select ng-model="vendors.selectVehicle"
                                                  ng-options="selectVehicle.vehicleNumber for selectVehicle in selectVehicles"
                                                  ng-change = "setVehicleNumber(vendors.selectVehicle)"
                                                  name = "selectVehicle"
                                                  class = 'vendorSelect_invoiceByVendorForm' required>                 
                                                  <option value="">SELECT VEHICLE</option>
                                          </select> 
                                       </td>
                                       <td class = "col-md-2">
                                        <select name="mySelect" class="form-control-width"
                                              ng-options="option.type for option in distanceArrayData.distanceArray track by option.value"
                                              ng-model="distanceArrayData.selectedOption"
                                              ng-change = "setdistance(distanceArrayData.selectedOption)"
                                              >
                                      </select>
                                      </td>
                                      
                                       
                                  <td class = "col-md-2">
                                          <div class = "input-group calendarInput">
                                          <span class="input-group-btn"><button class="btn btn-default" ng-click="selectMonthCal($event)">
                                              <i class = "icon-calendar calInputIcon"></i></button></span>
                                              <input type="text" 
                                                     class="form-control-width" 
                                                     ng-model="vendors.monthSelected" 
                                                     placeholder = "Select Month"
                                                     datepicker-popup = '{{monthformat}}'
                                                     datepicker-options="{{datepickerOptions}}"
                                                     is-open="datePicker.selectMonth"
                                                     max-date = "setMaxDate"
                                                     show-button-bar = false required>
                                          </div>
                                           
                                       </td>
                                      <td class = "col-md-4">
                                          <input type = 'button' 
                                                 class = 'btn btn-success searchButton_Invoice' 
                                                 value = "Get Invoice"
                                                 ng-disabled="invoiceByVehicle.$invalid"
                                                 ng-click = "getInvoiceByVehicle(vendors, distanceArrayData.selectedOption)">
                                       </td>
                                </tr>                                    
                             </tbody>
                            </table>
                            <div ng-include = "'partials/byVehicle_invoice_Table.jsp'"></div>
            </form>
                    </tab>  

                              
                        </tabset>
                            
                </tab> 

                    <!-- Generated Invoice Details -->

                      <tab ng-click = "generatedInvoiceDetails()">
                        <tab-heading>Previous Invoice Details</tab-heading>
                            <div class = 'invoiceTabContent'>
                                <div class = "firstRowInvoce ">
                                    <div class = "row">
                                          <div class = "col-md-4"> 
                                              <h5 class="invoiceSummaryPanel">
                                                Generated Invoice Summary Report - <span class="badge">{{invoiceDetailsData.length}}</span>
                                              </h5>
                                          </div>
                 
                                          <div class = "col-md-6">
                                          </div>
                                          
                                          <div class = "col-md-2">
                                                <input type="text" class = "form-control" ng-model = "contractDetailsFilter" placeholder = "Filter Invoice Detail"
                                                expect_special_char>
                                          </div>
                                   </div>
                                </div> 
                                 <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotInvoiceSummaryResults">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div> 

                                <div class = "col-md-12 col-xs-12 tableWrapper_report" ng-show = "  gotInvoiceSummaryResults">
                                    <header ng-repeat-start="items in invoiceDetailsData" class="invoicePanelHeader" ng-hide="items.InvoiceData.length == 0 || (items.InvoiceData | filter:contractDetailsFilter).length == 0">
                                          <div class="row" ng-hide="items.InvoiceData.length == 0 || (items.InvoiceData | filter:contractDetailsFilter).length == 0">
                                              <div class = "row">
                                                  <div class="col-sm-12">
                                                      <div class="text-center headerTitleSize">
                                                            <strong>Invoice Number : </strong> 
                                                            <span>{{items.InvoiceNumber}}</span>
                                                    </div>
                                                  </div>
                                              </div> 
                                          </div>
                                          <hr class="invoiceLineStyle">
                                          <div class="row text-center" ng-hide="items.InvoiceData.length == 0 || (items.InvoiceData | filter:contractDetailsFilter).length == 0">
                                              <div class = "row">
                                                  <div class="col-sm-4">
                                                      <span class="invoiceRowHeaderTitle">Vendor Name</span><br\>
                                                      <span class="badgeStyleInvoice">{{items.VendorName}}</span><br>
                                                  </div>
                                                  <div class="col-sm-2">
                                                      <span class="invoiceRowHeaderTitle">Start Date</span><br\>
                                                      <span class="badgeStyleInvoice">{{items.InvoiceStartDate}}</span><br>
                                                  </div>
                                                  <div class="col-sm-2">
                                                      <span class="invoiceRowHeaderTitle">End Date</span><br\>
                                                      <span class="badgeStyleInvoice">{{items.InvoiceEndDate}}</span><br>
                                                  </div>
                                                  <div class="col-sm-3">
                                                      <span class="invoiceRowHeaderTitle">Invoice Data Summary Count</span><br\>
                                                      <span class="badgeStyleInvoice">{{items.invoiceSize}}</span><br>
                                                  </div>
                                                  <div class="col-sm-1">
                                                      <input type="button" class="btn btn-danger btn-xs" value="Delete" name="" ng-click="deleteInvoiceDetails(items, $index)">
                                                  </div>
                                              </div> 
                                          </div>
                                    </header>

         <div ng-show="items.InvoiceData.length == 0">
                <tr>
                  <td colspan='12'>
                    <div class="noData">There are no invoice generated</div>
                  </td>
                </tr>
         </div>
                                         
        <div class="body" ng-show="items.InvoiceData.length > 0">
            <table class="table table-condensed table-bordered table-hover table-responsive" ng-hide="items.InvoiceData.length == 0 || (items.InvoiceData | filter:contractDetailsFilter).length == 0">
                <thead class="headerStyleTable">
                    <tr>
                        <th>S No</th>
                        <th>Vehicle No</th>
                        <th>Total Km</th>
                        <th>Total Amount Payable</th>
                    </tr>
                </thead>
               
                <tbody ng-repeat="item in items.InvoiceData | filter:contractDetailsFilter">

                    <tr>
                        <td>{{$index + 1}}</td>
                        <td>{{item.VehicleNumber}}</td>
                        <td>{{item.totalKm}}</td>
                        <td>{{item.totalAmountPayable}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <footer ng-repeat-end>
        </footer>

                      </div> 
                  </div>
                </tab> 
                    <!-- THIRD TAB -->
                  <tab ng-click = "getContractDetail() && vehicleTab()">
                        <tab-heading>Contract Detail</tab-heading>

                        <tabset class="tabset subTab_invoiceContract">
                              <tab ng-click = "getContractDetail() && vehicleTab()">
                                    <tab-heading ng-click = "vehicleTab()">Contract Detail/Create New Contract</tab-heading> 
                                    <div class = 'invoiceTabContent contractDetailHeight'>
                                <div class = "firstRowInvoce "  >
                                    <div class = "row">
                                          <div class = "col-md-2">
                                              <input type = "button" 
                                                     class = "btn btn-success buttonRadius0" 
                                                     value = "Add New Contract"
                                                     ng-click = "addContractDetail()" >
                                          </div>
                 
                                          <div class = "col-md-8">
                                          </div>
                                          
                                          <div class = "col-md-2">
                                                <input type="text" class = "form-control" ng-model = "contractDetailsFilter" placeholder = "Filter Contracts Detail"
                                                expect_special_char>
                                          </div>
                                   </div>
                                </div>
                                 <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotContractDetailsResults">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div>  
                                <div class = "col-md-12 col-xs-12 tableWrapper_report" ng-show = "gotContractDetailsResults">
                                      <table class = "invoiceByContractTable table table-responsive container-fluid table-bordered">
                                          <thead class ="tableHeading">                            
                                              <tr>
                                                  <th>Contract Title</th>
                                                  <th>Contract Id</th>
                                                  <th>Contract Type</th>
                                                  <th>Start Date</th>
                                                  <th>End Date</th>
                                                  <th>Fixed Distance Monthly</th>                                  
                                                  <th>Fixed Distance Charge Rate</th>
                                                  <th>Extra Distance Charge Rate</th>
                                                  <!-- <th>Fixed Distance per day</th> -->
                                                  <th>Minimum Days</th>
                                                  <th>Petrol Price</th>
                                                  <th>Fuel Contract Type</th>
                                                  <th>Penalty</th>
                                                  <th>Per Day Cost</th>
                                                  <th>Penalty %</th>
                                                  <th>Copy</th>
                                              </tr>
                                          </thead>
                                          <tbody>

                                         <tr class = 'tabletdCenter' 
                                             ng-repeat="contractDetail in contractDetailData | filter:contractDetailsFilter">

                                                <td class = 'col-md-1'>
                                                    <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.contractTitle}}</span>
                                                    <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                    type = 'text' 
                                                    class = "form-control-contract" 
                                                    ng-model = "contractDetail.contractTitle">
                                                </td>

                                                <td class = 'col-md-1'>
                                                    {{contractDetail.contractId}}
                                                </td>
                                                
                                                <td class = 'col-md-1'>
                                                    <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.contractType}}</span>
                                                    <select ng-show = "contractDetail.editByContractInvoiceClicked" ng-model="contractDetail.contractType" 
                                                    ng-options="contract.contractType for contract in contractData"
                                                    name = "selectVendor"
                                                    class = 'vendorSelect_invoiceByVendorForm form-control-contract'
                                                    >  
                                                  </select> 
                                                </td>
                                                
                                                <td class = 'col-md-2'>
                  
                                                <div class = "form-group"> 
                                             
                                                    <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.startDate | date:'dd-MM-yyyy'}}</span>
                                                    <div class = "input-group calendarInput"       ng-show = "contractDetail.editByContractInvoiceClicked">
                                                   
                                                    <span class="input-group-btn">
                                                    <button class="btn btn-default" 
                                                            ng-click="startDate($event,$index)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>
                                                  
                                                    <input type="text" 
                                                       ng-model="contractDetail.startDate" 
                                                       class="form-control-contract" 
                                                       placeholder = "End Date" 
                                                       datepicker-popup="{{format}}"
                                                       is-open="datePicker[$index].startDate" 
                                                       ng-change = "checkDateRangeValidity(startDate, endDate)"
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       required
                                                       readonly
                                                       name = 'startDate'
                                                       min-date = "contractDetail.startDate"
                                                    >   
                                                 </div>
                                              </div> 
                                            </td>
                                            <td class = 'col-md-2'>
                                                  <div class = "form-group"> 
                                                      <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.endDate | date:'dd-MM-yyyy'}}</span>
                                                          <div class = "input-group calendarInput" ng-show = "contractDetail.editByContractInvoiceClicked">

                                                          <span class="input-group-btn">
                                                          <button class="btn btn-default" 
                                                          ng-click="endDate($event,$index)">
                                                          <i class = "icon-calendar calInputIcon"></i></button></span>
            
                                                          <input type="text" 
                                                             ng-model="contractDetail.endDate" 
                                                             class="form-control-contract" 
                                                             placeholder = "End Date" 
                                                             datepicker-popup="{{format}}"
                                                             is-open="datePicker[$index].endDate" 
                                                             show-button-bar = false
                                                             show-weeks=false
                                                             min-date = "contractDetail.startDate"
                                                             datepicker-options = 'dateOptions'
                                                             required
                                                             readonly
                                                             name = 'endDate'
                                                            >   
                                                        </div>
                                                  </div> 
                                            </td>
                   
                                            <td class = 'col-md-1'>
                                                          <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.contractDistance}}</span>
                                                          <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                          type = 'text' 
                                                          class = "form-control-contract" 
                                                          ng-model = "contractDetail.contractDistance">
                                            </td>
                   
                                            <td class = 'col-md-1'>
                                                          <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.fixedDistanceChargeRate}}</span>
                                                          <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                          type = 'text' 
                                                          class = "form-control-contract" 
                                                          ng-model = "contractDetail.fixedDistanceChargeRate">
                                            </td>

                                            <td class = 'col-md-1'>
                                                          <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.extraDistanceChargeRate}}</span>
                                                          <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                          type = 'text' 
                                                          class = "form-control-contract" 
                                                          ng-model = "contractDetail.extraDistanceChargeRate"
                                                          only-num>
                                           </td>
                                 
                                    <!--        <td class = 'col-md-1'>
                                                         <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.fixedDistancePerDay}}</span>
                                                         <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                         type = 'text' 
                                                         class = "form-control-contract" 
                                                         ng-model = "contractDetail.fixedDistancePerDay" only-num>
                                          </td> -->
                             

                                          <td class = 'col-md-1'>
                                                        <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.minimumDays}}</span>
                                                        <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                        type = 'text' 
                                                        class = "form-control-contract" 
                                                        ng-model = "contractDetail.minimumDays"
                                                        only-num>
                                          </td>

                                          <td class = 'col-md-1'>
                                                     <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.petrolPrice}}</span>
                                                     <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                            type = 'text' 
                                                            class = "form-control-contract" 
                                                            ng-model = "contractDetail.petrolPrice"
                                                            only-num>
                                          </td>


                                          <td class = 'col-md-1'>
                                                    <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.contractDescription}}</span>
                                                    <select ng-show = "contractDetail.editByContractInvoiceClicked" ng-model="contractDetail.contractDescription" 
                                                    ng-options="contractDetail.contractDescription for contractDetail in fuelContractType"
                                                    ng-change="setFuelTypeData(contractDetail.contractDescription)"
                                                    name = "selectVendor"
                                                    class = 'vendorSelect_invoiceByVendorForm form-control-contract'
                                                    >  
                                                  </select> 
                                                </td>
                                         <td class = 'col-md-2'>
                                                      <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.penalty}}</span>
                                                      <select ng-model="contractDetail.penalty"
                                                              ng-show = "contractDetail.editByContractInvoiceClicked"
                                                              class="form-control-contract" 
                                                              ng-options="penalty.Id as penalty.name for penalty in allPenalty "
                                                              ng-change = "setPenalty(contract)"
                                                              only-num>
                                          </td>

                                          <td class = 'col-md-2'>
                                                      <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.perDayCost}}</span>
                                                      <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                             type = 'text' 
                                                             class="form-control-contract penalityDisable" 
                                                             ng-model = "contractDetail.perDayCost" 
                                                             ng-disabled = "contractDetail.penalty == 'N' || contractDetail.penalty == 'NO' "
                                                      only-num>
                                          </td>

                                         <td class = 'col-md-1'>
                                                    <span ng-show = "!contractDetail.editByContractInvoiceClicked">{{contractDetail.panalityPercentage}}</span>
                                                    <input ng-show = "contractDetail.editByContractInvoiceClicked" 
                                                           type = 'text' 
                                                           class = "form-control-contract penalityDisable" 
                                                           ng-model = "contractDetail.panalityPercentage"
                                                           ng-disabled = "contractDetail.penalty == 'N' || contractDetail.penalty == 'NO' "
                                                           only-num>
                                          </td>

                                          <td class = 'col-md-1'>
                     
                                              <button class = "btn btn-xs btn-warning form-control  excelExportButtonStyle floatRight" 
                                              ng-disabled = "contractDetail.disabled"
                                              ng-show = "!contractDetail.editByContractInvoiceClicked"
                                              ng-click="cloneRecord($index,contractDetail, invoice)">
                                              <i class="icon-copy"></i>
                                              <span class = "marginLeft5">Copy</span>
                                              </button>

                                              <button class = "btn btn-xs btn-success form-control excelExportButtonStyle floatRight" 
                                              ng-show = "contractDetail.editByContractInvoiceClicked"
                                              value = 'Save'
                                              ng-click="saveChangesContractDetail('contractDetail', invoice,contractDetail, $index)">
                                              <i class="icon-save"></i>
                                              <span class = "marginLeft5">Save</span>
                                              </button>

                                          </td>
                                    </tr> 
                             </tbody>                                       
                        </table> 

                      </div> 
                  </div>  
              </tab>

              <tab ng-click = "getFuelDetail()">
                        <tab-heading>Fuel Price Change</tab-heading>
                              <div class = 'invoiceTabContent'>
                    
                                   <div class = "firstRowInvoce ">
                                    <div class = "row">
                                          <div class = "col-md-2">
                                              <input type = "button" 
                                                     class = "btn btn-success buttonRadius0" 
                                                     value = "Add Fuel Price"
                                                     ng-click = "addFuelDetail()" >
                                          </div>
                 
                                          <div class = "col-md-8">
                                          </div>
                                          
                                          <div class = "col-md-2">
                                                <input class = "form-control" ng-model = "searchText" placeholder = "Filter Contracts Detail"
                                                expect_special_char>
                                          </div>
                                   </div>
                                </div>

                                 <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotFuelDetailsResults">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div>

                                <div class="container-fluid" ng-show="gotFuelDetailsResults">
                                      <div class="table-responsive">
                                           <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
                            <thead class ="tableHeading"> 
                                            
                                                        <tr>
                                                              <th>Contract Title</th> 
                                                              <th>Date</th>
                                                              <th>Contract Type</th>
                                                              <th>New Price</th>
                                                              <!-- <th>Old Price</th> -->
                                                              <th>Month Amount</th>
                                                              <th>Fuel Type</th> 
                                                        </tr>
                                                  </thead>

                                                  <tbody ng-show="contractFuelDetailData == 0">
                                                        <tr>
                                                         <td colspan='14'>
                                                          <div class="noData">No Record Found</div>
                                                         </td>
                                                        </tr>
                                                  </tbody>

                                                  <tbody>
                                                      <tr class = 'tabletdCenter' ng-repeat="contractFuelDetail in contractFuelDetailData | filter:searchText"">
                                                            <td>
                                                            {{contractFuelDetail.contactTitle}}
                                                            </td>
                                                            <td>
                                                            {{contractFuelDetail.selectDate}}
                                                            </td>
                                                            <td>
                                                            {{contractFuelDetail.contractType}}
                                                            </td>
                                                            <td>
                                                            {{contractFuelDetail.newPrice}}
                                                            </td>
                                                            <!-- <td>{{contractFuelDetail.oldPrice}}</td> -->
                                                            <td>
                                                            {{contractFuelDetail.monthAmount}}
                                                            </td>
                                                            <td>
                                                            {{contractFuelDetail.fuelType}}
                                                            </td>
                                                      </tr>
                                                  </tbody>
                                          </table>
                                      </div>
                                </div>
                            </div>
                    </tab> 

                              <tab ng-click = "getContractTypeDetail()">
                                    <tab-heading ng-click = "getContractTypeDetail()">Create Contract Type</tab-heading>
                                    <div class = 'invoiceTabContent'>
                    
                                   <div class = "firstRowInvoce ">
                                    <div class = "row">
                                          <div class = "col-md-2">
                                              <input type = "button" 
                                                     class = "btn btn-success buttonRadius0" 
                                                     value = "Add Contract Type"
                                                     ng-click = "addContractType()" >
                                          </div>
                 
                                          <div class = "col-md-8">
                                          </div>
                                          
                                          <div class = "col-md-2">
                                                <input class = "form-control" ng-model = "searchText" placeholder = "Filter Contracts Type"
                                                expect_special_char>
                                          </div>
                                   </div>
                                </div>

                                 <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotContractTypeResults">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div>

                                <div class="container-fluid" ng-show="gotContractTypeResults">
                                      <div class="table-responsive">

                                                 <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
                                          <thead class ="tableHeading">                            
                                              <tr>
                                                  <th>Contract Type Id</th>
                                                  <th>Contract Type</th> 
                                                  <th>Contract Description</th>
                                                  <th>Service Tax</th>
                                                  <th>Edit/Save</th>
                                              </tr>
                                          </thead>
                                          <tbody>

                                         <tr class = 'tabletdCenter' 
                                             ng-repeat="contractTypeData in contractTypeDatas | filter:searchText">                                                
                                                <td>
                                                    <span>{{contractTypeData.contractTypeId}}</span>
                                                </td>

                                                <td>
                                                    <span ng-show = "!contractTypeData.editByContractInvoiceClicked">{{contractTypeData.contractType}}</span>
                                                    <input ng-show = "contractTypeData.editByContractInvoiceClicked"
                                                    type = 'text' 
                                                    ng-minlength="2"
                                                    ng-maxlength="15"
                                                    maxlength="15"
                                                    expect_special_char 
                                                    class = "form-control-contract" 
                                                    ng-model = "contractTypeData.contractType">
                                                  </select> 
                                                </td>

                                                <td>
                                                    <span ng-show = "!contractTypeData.editByContractInvoiceClicked">{{contractTypeData.contractDescription}}</span>
                                                    <input ng-show = "contractTypeData.editByContractInvoiceClicked"
                                                    type = 'text' 
                                                    ng-minlength="2"
                                                    ng-maxlength="30"
                                                    maxlength="30"
                                                    expect_special_char
                                                    class = "form-control-contract" 
                                                    ng-model = "contractTypeData.contractDescription">
                                                </td>

                                                <td>
                                                    <span ng-show = "!contractTypeData.editByContractInvoiceClicked">{{contractTypeData.serviceTax}}</span>
                                                    <input ng-show = "contractTypeData.editByContractInvoiceClicked"
                                                    type = 'text'
                                                    ng-minlength="2"
                                                    ng-maxlength="8"
                                                    maxlength="8"
                                                 is-number-valid
                                                  expect_special_char
                                                    class = "form-control-contract" 
                                                    ng-model = "contractTypeData.serviceTax">
                                                </td>
                                          <td>
                                          
                                              <input type = 'button' 
                                                     class = 'btn btn-warning btn-sm' 
                                                     ng-show = "contractTypeData.editOptionRequired == 'N'"
                                                     value = 'Edit'
                                                     ng-disabled = 'true'
                                                     ng-click = "editContractType(contractTypeData, $index)">
                                              <input type = 'button' 
                                                     class = 'btn btn-warning btn-sm' 
                                                     ng-show = "!contractTypeData.editByContractInvoiceClicked && contractTypeData.editOptionRequired == 'Y'"
                                                     value = 'Edit'
                                                     ng-click = "editContractType(contractTypeData, $index)">

                                              <input type = 'button' 
                                                     class = 'btn btn-success btn-sm' 
                                                     ng-show = "contractTypeData.editByContractInvoiceClicked"
                                                     value = 'Save'
                                                     ng-click = "saveContractType(contractTypeData, $index)">
                                              <input type = 'button' 
                                                     class = 'btn btn-danger btn-sm' 
                                                     ng-show = "contractTypeData.editByContractInvoiceClicked"
                                                     value = 'Cancel'
                                                     ng-click = "cancelContractType(contractTypeData, $index)">
                                          </td>
                                    </tr> 
                             </tbody>                                       
                        </table> 
                                      </div>
                                </div>
                            </div>
                              </tab>
                        </tabset>
                            
                </tab> 
                    
                    
             <!--      <tab ng-click = "expenseReport()">
                    <tab-heading ng-click = "vehicleTab()">Operating Expense</tab-heading>
            
            
                      <div class = 'invoiceTabContent'>
                      <div class = "firstRowInvoce ">
                          <div class = "row">
                              <div class = "col-md-2">
                            <input class = "form-control" ng-model = "efmfilter.expensDetail" placeholder = "Filter Expense Detail">
                            </div>
                              <div class = "col-md-2">
                                  <select ng-model="expense.criteria" 
                                          ng-options="searchCriteria for searchCriteria in searchCriterias"
                                          name = "criteria"
                                          class = 'vendorSelect_invoiceByVendorForm'
                                          ng-change = "getVendorOrVehicle(expense.criteria)">   
                                  </select> 
                              </div>
                              <div class = "col-md-2">
                                  <select ng-model="expense.vSelection" 
                                          ng-options="vSelection.name for vSelection in vSelectionsData track by vSelection.id"
                                          name = "criteria"
                                          class = 'vendorSelect_invoiceByVendorForm'> 
                                      <option value="">SELECT</option>
                                  </select> 
                              </div>
                              <div class = "col-md-6">
                                <div class = "calenderMainDiv floatRight pointer" 
                                     popover-template="partials/popovers/calenderReport.jsp"
                                     popover-placement="bottom"
                                     popover-title = "Get Report"
                                     popover-trigger ="click">                           
                                        <span><i class = "icon-calendar"></i></span>
                                        <span>{{fromDate | date : 'longDate'}} - {{toDate | date : 'longDate'}}</span>
                                </div>                                  
                              </div>
                          </div>
                      </div>
                      

                     <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "expenseReporttButtonClicked">
                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                     </div>
                     <div class = "col-md-12 col-xs-12 tableWrapper_report" ng-show = "gotExpenseResult">
                        <table class = "invoiceByContractTable table table-responsive container-fluid table-bordered">
                            <thead class ="tableHeading">  
                                <tr>
                                    <th class = "vendorNameHeading tabletdCenter" colspan = "13">
                                        <span>Vendor Name : {{expenseVendorName}}</span> <br/>
                                        <span>Invoice Date : {{expenseVendorNameFromDate}} - {{expenseVendorNameToDate}}</span>
                                    </th>
                                </tr>                             
                                <tr>                                
                                  <th>Vehicle Number</th>  
                                   <th>Total Km</th>
                                   <th>Per Km Amt</th>
                                   <th>Travelled Km Amt</th>
                                  <th>Working Days</th>
                                  <th>Absent Days</th>   
                                    <th>Working days Amt</th>
                                   <th>Deduction</th>
                                   <th>Penalty(20%)</th>                                   
                                   <th>Total Deduction(Rs.) + Penalty</th>
                                    <th>Total(Rs.)</th>
                            
                                </tr>
                            </thead>
                            <tbody>
                               <tr class = 'tabletdCenter' ng-repeat="expense in expenseData | filter:efmfilter.expensDetail">
                                   <td class = 'col-md-1'>{{expense.vehicleNumber}}</td>
                                   <td class = 'col-md-1'>{{expense.totalKm}}</td>
                                   <td class = 'col-md-1'>{{expense.perKmCharges}}</td>
                                   <td class = 'col-md-1'>{{expense.travelledKmAmt}}</td>                                   
                                   <td class = 'col-md-1'>{{expense.totalWorkingDays}}</td>                                   
                                   <td class = 'col-md-1'>{{expense.absentDays}}</td>
                                   <td class = 'col-md-1'>{{expense.workingDayAmt | roundDecimal:2}}</td>
                                   <td class = 'col-md-1'>{{expense.deduction}}</td>
                                   <td class = 'col-md-1'>{{expense.penaltyTotalAmt}}</td>                                                                    
                                   <td class = 'col-md-1'>{{expense.totalDeduction | roundDecimal:2}}</td>                                                                      
                                   <td class = 'col-md-1'>{{expense.totalAmount | roundDecimal:2}}</td>
                            
                                   
                               </tr> 
                              
                             </tbody>                                       
                        </table> 
                          </div> 
                      </div>
                    </tab>  -->

                    
                    <tab ng-click = "getVehicleWiseContract()">
                        <tab-heading>Vehicle Wise Contracted KM</tab-heading>
                              <div class = 'invoiceTabContent'>
                    
                                   <div class = "firstRowInvoce ">
                                    <div class = "row">
                                          <div class = "col-md-4">
                                              <input ng-model="q" id="search" class="form-control" placeholder="Filter Vendor Wise"
                                              expect_special_char>
                                          </div>
                 
                                          <div class = "col-md-4">
                                             <input class = "form-control" ng-model = "searchText" placeholder = "Global Search"
                                             expect_special_char>
                                          </div>
                                          <div class = "col-md-4">
                                            <select ng-model="pageSize" id="pageSize" class="form-control">
                                                  <option value="5">5</option>
                                                  <option value="10">10</option>
                                                  <option value="15">15</option>
                                                  <option value="20">20</option>
                                                  <option value="40">40</option>
                                                  <option value="60">60</option>
                                            </select>
                                          </div>
                                         
                                   </div>
                                </div>
                                <div class="container-fluid">

<ul>
<table class="table table-bordered table-responsive table-condensed">

                      <tr class="tableHeadingVehicleInvoice">
                                              <td style="width:10%">S.No</td>
                                              <td style="width:30%">Vendor Name</td>
                                              <td style="width:10%">Vehicle Number</td>
                                              <td style="width:10%">Vehicle Type</td>
                                              <td style="width:10%">Vehicle Contracted Km</td>
                                              <td style="width:10%">Travelled Km</td>                                             
                                              <td style="width:10%">Pending Km</td>                                             
                                              <td style="width:10%">Extra Km</td>
                                              <td style="width:25%">Edit</td>
                                          </tr>
                                          
    <tr ng-repeat="vehicleNumber in vehicleNumberData | filter:q | startFrom:currentPage*pageSize | limitTo:pageSize | filter:searchText">
                                         <td>
                                              {{$index+1}}
                                          </td>
                                          <td>
                                              {{vehicleNumber.vendorName}}
                                          </td>
                                          <td>
                                              {{vehicleNumber.vehicleNumber}}
                                          </td>
                                          <td>
                                              {{vehicleNumber.vehicleType}}
                                          </td>
                                           <td>
                                              {{vehicleNumber.vehicleContractedKm}}
                                          </td>
                                          <td>
                                              {{vehicleNumber.travelledKm}}
                                          </td>
                                          <td>
                                              <span editable-number="vehicleNumber.pendingKm" 
                                                    e-name="travelledKm" 
                                                    e-form="rowform" 
                                                    e-maxlength="10"
                                                    >
                                              {{vehicleNumber.pendingKm}}
                                          </span>
                                          </td>

                                          <td>
                                              {{vehicleNumber.extarKm}}
                                          </td>


                                          <td style="white-space: nowrap">
                                              <form editable-form 
                                                    name="rowform" 
                                                    onbeforesave="updateVehicleWiseContract($data, vehicleNumber.id, vehicleNumber)" 
                                                    ng-show="rowform.$visible" 
                                                    class="form-buttons form-inline" 
                                                    shown="inserted == vehicleNumber">
                                                  <button type="submit" 
                                                          ng-disabled="rowform.$waiting" 
                                                          class="btn btn-success btn-sm">Save
                                                  </button>
                                                  <button type="button" 
                                                          ng-disabled="rowform.$waiting" 
                                                          ng-click="rowform.$cancel()" 
                                                          class="btn btn-default btn-sm">Cancel
                                                  </button>
                                              </form>
                                                <div class="buttons" ng-show="!rowform.$visible">
                                              <button class="btn btn-primary btn-sm" ng-click="rowform.$show()">Edit</button>
                                              <!-- <button class="btn btn-danger btn-sm" ng-click="removeUser($index)">Delete</button> -->
                                            </div>  
                                          </td>
                                        </tr>
                                          </table>

                                        </ul>
                                        <div class="row">
                                        <div class="col-sm-5"></div>
                                        <div class="col-sm-4">
                                          
                                          <button class="btn btn-success btn-center" ng-disabled="currentPage == 0" ng-click="currentPage=currentPage-1">
                                        Previous
                                        </button>
                                        {{currentPage+1}}/{{numberOfPages()}}
                                        <button class="btn btn-primary btn-center" ng-disabled="currentPage >= vehicleNumberDataLength/pageSize - 1" ng-click="currentPage=currentPage+1">
                                        Next
                                        </button>
                                        </div>
                                        <div class="col-sm-3"></div>
                                        

                                </div>
                            </div>
                    </tab> 
                </tabset>
            </div>
        </div>
    </div>
   </div>
</div>