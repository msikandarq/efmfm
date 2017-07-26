<div>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "importEmployeeModal container-fluid">
      <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-ok edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-7 floatLeft">Dynamic Report Confirmation</div>
                   <div class = 'col-md-2'>
                                                    
                                                </div>

                <div class = "col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    

            </div>
        </div>        
    </div>       
<div class="modal-body modalMainContent pointer"  >    

<div>  <h4 class="primary confirmationDynamicLbl"> <i class="icon-archive"></i> Are you sure want to create this dynamic Data?</h4>
<table class="table">     <thead>
    
      <tr class="success">
        <th>Search Type</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Total No Cloumn</th>
      </tr>
    </thead>
    <tbody>
      <tr   class="warning">

        <td>
        {{searchType}}
        </td>
        <td>
        {{fromDate}}
        </td>
        <td>
        {{toDate}}
        </td>
        <td>
        {{dynamicDataLength}}
        </td>
       
      </tr>
          <thead>
     
     
    </thead>
   
   
  </table>

  
   <table class="table table-bordered table-striped">
    <thead>
     
      <tr class="success">
        <th>S No</th>
        <th>Data Creation Types</th>
        <th>Category Type</th>
      </tr>
    </thead>
    <tbody>
      <tr class="primary" ng-repeat="data in dynamicData">
      <td class="col30">{{$index+1}}</td>
      <td class="width35">{{data.name}}</td>
      <td class="width35">{{data.category}}</td>
      </tr>
   
   
  </table>

</div>

  
  
</div>
 <div class = "row">
        <div class="modal-header modal-headerDynamic col-md-12"> 
            <div class = "row">
                <div class = "col-md-7 ">
                  
                </div>
                <div class = "col-md-3">
                  <input type="button" class="btn btn-primary" ng-click="dynamicDataSubmit()" value="Generate Report" name="">
                </div>
                   <div class = 'col-md-2'>
                       <input type="button" class="btn btn-danger" ng-click="cancel()" value="Cancel" name="">                             
              </div>


            </div>
        </div>        
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
