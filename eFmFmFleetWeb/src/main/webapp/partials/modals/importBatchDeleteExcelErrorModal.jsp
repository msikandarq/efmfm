<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "importEmployeeModal container-fluid">
      <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-calendar edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-7 floatLeft">Import Batch Delete Excel Error</div>
                   <div class = 'col-md-2'>
                                                    <button class = "btn btn-sm btn-success form-control excelExportButtonStyle floatRight" 
                                                            ng-click = "saveInExcel()">
                                                        <i class = "icon-download-alt"></i>
                                                        <span class = "marginLeft5">Excel</span>
                                                    </button>
                                                </div>

                <div class = "col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    

            </div>
        </div>        
    </div>       
<div class="modal-body modalMainContent">    

<div id="importBatchDeleteExcelError">
 <table class="table">
    <thead>
      <tr>
        <th>Error List</th>
      </tr>
    </thead>
    <tbody>
      <tr  ng-repeat="item in result">
        <td> {{item.id}} - {{item.error}}
            
        </td>
      </tr>
      
    </tbody>
  </table>
</div>

     <div class="modal-footer modalFooter">  
           <button type="button" class="btn btn-close floatLeft" ng-click = "cancel()">Close</button>
     </div>
  
</div>
</div>
<script type="text/javascript"> 

//Pending tab function
$("#imp_stud").live('click',function(){
 // alert("hiiimp_stud");
//  initApproval();
});
</script>
