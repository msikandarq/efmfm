<div>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "importEmployeeModal container-fluid">
      <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-ok edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-7 floatLeft">Invoice Vendor Modification Confirmation?</div>
                   <div class = 'col-md-2'>
                                                    
                                                </div>

                <div class = "col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    

            </div>
        </div>        
    </div>       
<div class="modal-body modalMainContent pointer"  >    

<div>  <h4 class="primary confirmationDynamicLbl"> <i class="icon-archive"></i> Are you sure want to modify invoice?</h4>
<table class="table table-bordered table-striped">
  <thead class="success">
      <tr>
        <th>Modified Parameters</th>
        <th>Old Values</th>
        <th>New Values</th>
      </tr>
    </thead>
    <tbody class="primary">
      <tr ng-hide="{{totalKmUpdated === undefined}}">
        <td>Total KM</td>
        <td>{{totalKmOld}}</td>
        <td>{{totalKmUpdated}}</td>
      </tr>
      <tr ng-hide="{{extraKmUpdated === undefined}}">
        <td>Extra KM</td>
        <td>{{extraKmOld}}</td>
        <td>{{extraKmUpdated}}</td>
      </tr>
      <tr ng-hide="{{fuelExtraAmountUpdated === undefined}}">
        <td>Fuel Extra Amount</td>
        <td>{{fuelExtraAmountOld}}</td>
        <td>{{fuelExtraAmountUpdated}}</td>
      </tr>
      <tr ng-hide="{{absentDaysUpdated === undefined}}">
        <td>Absent Days</td>
        <td>{{absentDaysOld}}</td>
        <td>{{absentDaysUpdated}}</td>
      </tr>
      <tr ng-hide="{{invoiceRemarksUpdated === undefined}}">
        <td>Invoice Remarks</td>
        <td>{{invoiceRemarksOld}}</td>
        <td>{{invoiceRemarksUpdated}}</td>
      </tr>
    </tbody>
  </table>

</div>
  
  
</div>
 <div class = "row">
        <div class="modal-header modal-headerDynamic col-md-12"> 
            <div class = "row">
                <div class = "col-md-10">
                  
                </div>
                <div class = 'col-md-1'>
                    <input type="button" class="btn btn-danger" ng-click="cancel()" value="Cancel" name="">                             
                </div>
                <div class = "col-md-1">
                  <input type="button" class="btn btn-success" ng-click="confirmModifiedInvoice()" value="Yes" name="">
                </div>
            </div>
        </div>        
    </div> 
</div>
</div>

