
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "addContractFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add Contract Type</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
       <form class = "addContractTypeForm" name = "addContractTypeForm">
            <div class = "col-md-6 col-xs-12 form-group"> 
                <label>Contract Type</label>
                      <input type="text" 
                             ng-model="contract.contractType"
                             class="form-control" 
                             placeholder = "Contract Type"
                             name = 'contractType' 
                             ng-minlength="0"
                             ng-maxlength="15"
                             maxlength="15"
                             expect_special_char
                             required
                      >
            </div> 
            <div class = "col-md-6 col-xs-12 form-group"> 
                <label>Contract Description</label>
                      <input type="text" 
                             ng-model="contract.contractDescription"
                             class="form-control" 
                             placeholder = "Contract Description"
                             name = 'contractDescription'
                             ng-minlength="0"
                             ng-maxlength="30"
                             maxlength="30" 
                             expect_special_char
                             required
                      >
            </div> 
            <div class = "col-md-6 col-xs-12 form-group"> 
                <label>Service Tax</label>
                      <input type="text" 
                             ng-model="contract.serviceTax"
                             class="form-control"
                             ng-minlength="2"
                             ng-maxlength="8"
                             maxlength="8"
                             placeholder = "Service Tax"
                             name = 'serviceTax'
                             is-number-valid
                             required
                      >
            </div> 
          
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "addContractType(contract)" ng-disabled= "addContractTypeForm.$invalid">Add Contract Type</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>