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
                <div class = "modalHeading col-md-7 floatLeft">No Of Days Present Details</div>
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
      Present Summary Report - <span class="badge">{{absentDetailsLength}}</span>
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
        <th>Vehicle No</th>
        <th>Driver Name</th>
        <th>Check In Time</th>
        <th>Check Out Time</th>  
      </tr>
    </thead>

    <tbody>
      <tr ng-repeat="totalKm in noOfdaysAbsentDetail | filter:totalKmFilter ">
        <td>
        {{$index + 1}}
        </td>
        <td>
        {{totalKm.vehicleNumber}}
        </td>
        <td>
        {{totalKm.DriverName}}
        </td>
        <td>
        {{totalKm.LogInTime}}
        </td>
        <td>
        {{totalKm.LogOutTime}}
        </td>
      </tr>
      
    </tbody>
  </table>
</div>

<script type="text/javascript"> 

//Pending tab function
$("#imp_stud").live('click',function(){
 // alert("hiiimp_stud");
//  initApproval();
});
</script>


