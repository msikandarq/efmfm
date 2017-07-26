
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
                <div class = "modalHeading col-md-7 floatLeft">Total Km View Details</div>
                   <div class = 'col-md-2'>
                                                  
                   </div>

                <div class = "col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    

            </div>
        </div>        
    </div>       

<div class="modal-body modalMainContent"  >    
<div id="importEmployeeError">
<div class="row">
  <div class="col-sm-6">
    <h5 class="lengthInvoice">
      Total Km Summary Report - <span class="badge">{{totalKmDataLength}}</span>
    </h5>
  </div>
  <div class="col-sm-2"></div>
  <div class="col-sm-4">
    <input type="text" ng-model="totalKmFilter" class="form-control input-sm">
  </div>
</div>
<br>

 <table class="table table-striped">
    <thead>
      <tr class="success">
        <th>S No</th>
        <th>Trip Id</th>
        <th>Driver Name</th>
        <th>Vehicle Number</th>
        <th>Trip Start Date</th>
        <th>Trip End Date</th> 
        <th ng-show="distanceArray == 'Odometer'">Start Km</th>
        <th ng-show="distanceArray == 'Odometer'">End Km</th>
        <th ng-show="distanceArray == 'Odometer'">Total KM</th>
        <th ng-show="distanceArray == 'GPS'">Travelled Distance</th>
        
      </tr>
    </thead>

    <tbody>
      <tr  ng-repeat="totalKm in vehicleInvoiceData | filter:totalKmFilter ">
        <td>
        {{$index + 1}}
        </td>
        <td>
        {{totalKm.tripId}}
        </td>
        <td>
        {{totalKm.DriverName}}
        </td>
        <td>
        {{totalKm.vehicleNumber}}
        </td>
        <td>
        {{totalKm.tripStartTime}}
        </td>
        <td>
        {{totalKm.completedTime}}
        </td>
        <td ng-show="distanceArray == 'Odometer'">
        {{totalKm.startKm}}
        </td>
        <td ng-show="distanceArray == 'Odometer'">
        {{totalKm.endKm}}
        </td>
        <td ng-show="distanceArray == 'Odometer'">
        {{totalKm.totalKm}}
        </td>
        <td ng-show="distanceArray == 'GPS'">
        {{totalKm.travelledDistance}}
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
