<div class = "gvModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-circle edsModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Permit Due For Renewal</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>  
    <div class = "row">
        <div class="modal-header2 col-md-3">
            <form><input class = "searchModal width100per" 
                         ng-model = "searchRoadAlerts"
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
                    ng-click = "saveInExcel('gvPermit')">
                <i class = "icon-download-alt"></i>
                <span class = "marginLeft5">Excel</span>
            </button>
        </div>
<div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!vehicleGovernanceNotification">
 <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
</div>
    </div>
    
<div id = "exportExcelPermit" class="modal-body modalMainContent" ng-show = "vehicleGovernanceNotification">
    <table class="table table-bordered table-responsive container-fluid dashboardTable">
        <thead class ="tableHeadingDashboard" ng-show = "permitDueData.length==0 || permitDueData.length>0">
            <tr>
		      <th>Vendor Name</th>
		      <th>Vehicle Number</th>
              <th>Vendor ContactNam</th>
		      <th>Vendor MobileNumber</th>	
              <th>Facility Name</th>
		      <th>Permit Exp Date</th>		      
            </tr> 
        </thead>
        <tbody ng-if = "permitDueData.length==0">
              <tr>
                 <td colspan = '7'>
                   <div class = "noData">No Employee Dropped</div>
                 </td>
              </tr>
        </tbody>
        <div class="dashboardAlertContainer" ng-if = "permitDueData.length>0">
            <i class="icon-thumbs-down-alt redDashboardStatus"></i> - <span class="redDashboardStatus">Red (Permit Will be expire In 10 Days)</span>  | <i class="icon-warning-sign yellowDashboardStatus"></i> - <span class="yellowDashboardStatus">Yellow (Permit Will be expire In 20 Days) </span>| <i class="icon-thumbs-up-alt greenDashboardStatus"></i> - <span class="greenDashboardStatus">Green (30 Days)</span>
        </div>
        <tbody ng-if = "permitDueData.length>0">
            <tr ng-repeat = "permit in permitDueData | filter:filterSearch: filterSearchFlag | filter: searchRoadAlerts" ng-class="{dangerStyle: permit.colorFlg == 'R' , warningStyle: permit.colorFlg == 'Y' , successStyle: permit.colorFlg == 'G'}">
            	<td class = "col-md-2">{{permit.vendorName}}</td>
                <td class = "col-md-1">{{permit.vehicleNumber}}</td>                                
                <td class = "col-md-1">{{permit.vendorContactName}}</td>
                <td class = "col-md-1">{{permit.vendorMobileNumber | tel}}</td>
                <td class = "col-md-1">{{permit.facilityName}}</td>
                <td class = "col-md-1">{{permit.permitExpDate}}</td>
                
                
            </tr>
        </tbody>
        
    </table>
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-success-gv" ng-click = "cancel()">Close</button>
</div>
</div>