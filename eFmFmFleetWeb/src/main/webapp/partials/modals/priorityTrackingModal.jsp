<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "newEscortFormModalTemplate">  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-map-marker addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Priority 
            <span ng-if = "!routingPriority">Tracking</span>
            <span ng-if = "routingPriority">Routing</span></div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
<div class="modal-body modalMainContent">
   <div id='exportableTripSheet' class="col-md-12 col-xs-12 tableWrapper_report">
              <table
                class="reportTable reportTable_km table table-responsive container-fluid">
                <thead class="tableHeading">
                  <tr>
                    <th>Alert Id</th>
                    <th>Emp Id</th>
                    <th>Emp Name</th>
                    <th>Subject Type</th>
                    <th>Subject</th>
                    <th>Shift Date</th>
                    <th>Shift Time</th>
                    <th>Description</th>
                    <th>Remarks</th>
                    <th>Date</th>
                  </tr>
                </thead>
                
                <tbody>
                  <tr ng-repeat-end=""
                    ng-repeat-start="feedback in feedbacks | filter: efmfilter.filterTrips"
                    class="secondaryloop_tripReport">
                    <td class="col5">{{feedback.alertId}}</td>
                    <td class="col5">{{feedback.empId}}</td>
                    <td class="col10">{{feedback.empName}}</td>
                    <td class="col10">{{feedback.alertType}}</td>
                    <td class="col10">{{feedback.alertTitle}}</td>
                    <td class="col10">{{feedback.creationTime}}</td>
                    <td class="col10">{{feedback.shiftTime}}</td>
                    <td class="col10">{{feedback.empDescription}}</td>
                    <td class="col10">{{feedback.remarks}}</td>
                    <td class="col10">{{feedback.updationDateTime}}</td>
                  </tr>
                  <tr ng-repeat-end="" class="displayNone"></tr>
                </tbody>
              </table>
            </div> 
</div>      
<div class="modal-footer modalFooter">    
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Close</button>    
</div>
     
</div>