<div>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "importEmployeeModal container-fluid">
      <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-ok edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-7 floatLeft">Contract Type Details Edit Confirmation?</div>
                   <div class = 'col-md-2'>
                                                    
                                                </div>

                <div class = "col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    

            </div>
        </div>        
    </div>       
<div class="modal-body modalMainContent pointer"  >    

<div>  <h4 class="primary confirmationDynamicLbl"> <i class="icon-archive"></i> Are you sure want to modify this contract Type Details?</h4>
<table class="table table-bordered table-striped">
  <thead class="success">
      <tr>
        <th>Modified Parameters</th>
        <th>Old Values</th>
        <th>New Values</th>
      </tr>
    </thead>
    <tbody class="primary">
      <tr ng-hide="{{contractTypeUpdated === undefined}}">
        <td>Contract Type</td>
        <td>{{contractTypeOld}}</td>
        <td>{{contractTypeUpdated}}</td>
      </tr>
      <tr ng-hide="{{contractDescriptionUpdated === undefined}}">
        <td>Contract Description</td>
        <td>{{contractDescriptionOld}}</td>
        <td>{{contractDescriptionUpdated}}</td>
      </tr>
      <tr ng-hide="{{serviceTaxUpdated === undefined}}">
        <td>Service Tax</td>
        <td>{{serviceTaxOld}}</td>
        <td>{{serviceTaxUpdated}}</td>
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
                  <input type="button" class="btn btn-success" ng-click="confirmModifiedContractType()" value="Yes" name="">
                </div>
            </div>
        </div>        
    </div> 
</div>
</div>

