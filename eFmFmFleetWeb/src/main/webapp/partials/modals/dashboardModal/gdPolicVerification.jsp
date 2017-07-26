<div class = "gdModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-circle edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Police Varification Due For Renewal</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>  
    <div class = "row">
        <div class="modal-header2 col-md-3">
            <form><input class = "searchModal width100per" 
                         ng-model = "searchgdPoliceVerification"
                         type="text" 
                         class="form-control" 
                         placeholder="Search..." 
                         id="formGroupInputSmall"></form>
        </div>

        <div class="modal-header2 col-md-6">
          <span class="col-md-4">
          <label class="modalLabel">Search Priority</label> </span>
          <span class="col-md-8"><select ng-model="search.flagId" 
                     class="form-control dashboardFilterDropdown" 
                     ng-options="colorFlg.flagId as colorFlg.flagName for colorFlg in colorFlgData"
                     ng-change="setSearchFlag(search.flagId)">
              <option value="">ALL</option>
              </select></span>
             
        </div>
        
        <div class="modal-header2 col-md-3">
            <button class = "btn btn-sm btn-success form-control modalExcelbtn floatRight" 
                    ng-click = "saveInExcel('gdPoliceVerification')">
                <i class = "icon-download-alt"></i>
                <span class = "marginLeft5">Excel</span>
            </button>
        </div>
<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotDriverGovernanceNotification">
 <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
</div>

    </div>
    
<div id = "exportExcelPoliceVer" class="modal-body modalMainContent" ng-show = "gotDriverGovernanceNotification">
    <table class="table table-bordered table-responsive container-fluid dashboardTable">
        <thead class ="tableHeadingDashboard" ng-show = "gdPoliceVerificationData.length==0 || gdPoliceVerificationData.length>0">
            <tr>
		      <th>Vendor Name</th>
		      <th>Vendor ContactName</th>
              <th>Vendor MobileNumber</th>
		      <th>Driver Name</th>
		      <th>Mobile Number</th>
		      <th>Licence Number</th>
          <th>Facility Name</th>
		      <th>Police Varification Exp Date</th>
		      <!-- <th>Description</th> -->
            </tr> 
        </thead>
        <tbody ng-if = "gdPoliceVerificationData.length==0">
              <tr>
                 <td colspan = '8'>
                   <div class = "noData">Police Varification Due For Renewal</div>
                 </td>
              </tr>
        </tbody>
        <tbody ng-if = "gdPoliceVerificationData.length>0">
            <div class="dashboardAlertContainer">
              <i class="icon-thumbs-down-alt redDashboardStatus"></i> - <span class="redDashboardStatus">Red (Police Varification Certificate Will be expire In 10 Days)</span>  | <i class="icon-warning-sign yellowDashboardStatus"></i> - <span class="yellowDashboardStatus">Yellow (Police Varification Certificate Will be expire In 20 Days) </span>| <i class="icon-thumbs-up-alt greenDashboardStatus"></i> - <span class="greenDashboardStatus">Green (30 Days)</span>
            </div>
            <tr ng-repeat = "policeCert in gdPoliceVerificationData | filter:filterSearch: filterSearchFlag | filter: searchgdPoliceVerification" ng-class="{dangerStyle: policeCert.colorFlg == 'R' , warningStyle: policeCert.colorFlg == 'Y' , successStyle: policeCert.colorFlg == 'G'}">
            	  <td class = "col-md-2">{{policeCert.vendorName}}</td>
            	  <td class = "col-md-1">{{policeCert.vendorContactName}}</td>
                <td class = "col-md-2">{{policeCert.vendorMobileNumber | tel}}</td>
                <td class = "col-md-2">{{policeCert.driverName}}</td>                
                <td class = "col-md-2">{{policeCert.mobileNumber | tel}}</td>                
                <td class = "col-md-1">{{policeCert.licenceNumber}}</td>  
                <td class = "col-md-1">{{policeCert.facilityName}}</td>
                <td class = "col-md-2">{{policeCert.policeExpDate}}</td>      
                    
                
                <!-- <td class = "col-md-4">{{roadAlert.description}}</td> -->
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-success-gd" ng-click = "cancel()">Close</button>
</div>
</div>