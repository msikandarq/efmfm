<div class = "rescheduleRequestTemplate container-fluid" ng-if = "isAdhocRequestActive">
	<div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <span class = "col-md-12 col-xs-12">Reschedule Request</span>            
            <div class = "col-md-12 col-xs-12 mainTabDiv_rescheduleRequestDetails">
            <div class = "wrapper1" id="employeeList">
                <div class = "heading2 row">
                    <span class = "col-md-7 floatLeft">Request List</span>
                    <div class= "col-md-5 floatRight">
                    	<efmfm-button img-class = "efmfm_approvalButtons_collapse"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "employeeList"
                                      target-div = "employeeListContent">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_approvalButtons_reload"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "employeeList"
                                      target-div = "employeeListContent">
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "employeeList"
                                      target-div = "employeeListContent">
                        </efmfm-button>
                    </div>
                </div>
                <div class = "rescheduleRequestListContent">
                <!-- /*FIRST ROW*/-->
                <div class = "row">
                	<div class = "col-md-3 showRecordsSelect">
                		<span>Show</span>
                		<select ng-model="showRecords" 
                				ng-options="pagination.text for pagination in paginations track by pagination.value"
                				ng-change = "setLimit(showRecords)">
      						  <option value="">-- All Records --</option>
    					</select>
                	</div>
                	<div class = "col-md-4 shiftTimeSelect">
                		<!-- <span>Shift Time</span>
                		<select ng-model="selectShifts" ng-options="shiftTime for shiftTime in shiftsTime">
      						<option value="">-- Select Shift Time --</option>
    					</select>-->
                	</div> 
                	<div class = "col-md-5 searchrescheduleDetail">
                		<span>Search</span>
                		<input type = "text" ng-model = "searchEmployeeReported">
                	</div>
                </div>
                <!--   /*SECOND ROW*/-->
                <div class = "row">
                	<!-- <div class = "wrapper1_rescheduleDetail">
                		<div ng-click = "selectAll()" class = "selectAllCheckBox" ng-class = {disable:deleteAllClicked}>
                		<i ng-hide = "selectAllClicked" class = "icon-check-empty"></i>
		                <i ng-show = "selectAllClicked" class = "icon-check"></i>
                		Select All</div>
                		<div ng-click = "deleteAll()" class = "deleteCheckBox" ng-class = {disable:selectAllClicked}>
						<i ng-hide = "deleteAllClicked" class = "icon-check-empty"></i>
		                <i ng-show = "deleteAllClicked" class = "icon-check"></i>
		                Delete All</div>
                	</div>--> 
                	<div class = "wrapper2_rescheduleDetail">
                		<table class = "requestListTable_rescheduleRequestDetail table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                     <!--  <th ng-click = "selectAll()">
                                      <i ng-hide = "selectAllClicked" class = "headerIcon icon-check-empty"></i>
		                           	  <i ng-show = "selectAllClicked" class = "headerIcon icon-check"></i>
									 </th>--> 
                                      <th>Employee Id</th>
                                       <th>Employee Name</th>
                                       <th>Gender</th>
                                        <th>Employee Number</th>                                    
                                      <th>Trip Type</th>
                                      <th>Request Status</th>
                                      <th>Trip Time</th>
                                      <th>Approval</th>
                                      <th></th>
                                    </tr> 
                                </thead>
                                <tbody>
		                           <tr ng-repeat = "post in posts |limitTo: numberofRecords | filter : searchEmployeeReported" 
		                           	   class = "{{post.regId}}">
		                           		<!-- <td ng-click = "select_thisEmployee(post)">
		                           		 <i ng-hide = "post.checkBoxFlag" class = "icon-check-empty"></i>
		                           		 <i ng-show = "post.checkBoxFlag" class = "icon-check"></i>
		                           		 </td>--> 
		                                 <td>{{post.employeeId}}</td>
		                                 <td>{{post.employeeName}}</td>
		                                 <td>Male</td>	
		                                 <td>{{post.employeeNumber}}</td>
		                                 <td>{{post.tripType}}</td>  
		                                 <td>{{post.requestStatus}}</td>
		                                  <td>{{post.tripTime}}</td>  
		                                  <td><input type = "button" 
                                                     class = "btn btn-success btn-xs" 
                                                     value = "Approve" 
                                                     ng-click = "approve(post)">
                                              <input type = "button" 
                                                     class = "btn btn-danger btn-xs" 
                                                     value = "Reject" 
                                                     ng-click = "reject(post)">
                                          </td>  
		                                   
		                            </tr>                                  
		                         </tbody>
                                
                            </table>
                         </div>
                         
                         <div class = "wrapper3_rescheduleDetail">
	                         <div class = "row">
	                         	<div class = "col-md-6 numberofRows" ng-show = (showSearchResultCount)>
	                         		Searched {{countofFilteredRecords.length}}/{{posts.length}} records
	                         	</div>
	                         	<div class = "col-md-6 numberofRows">
	                         <!--	Total {{result.length}}/{{posts.length}} records  -->
	                         		<!-- Total {{getNumOfRecordsOnPage()}}/{{posts.length}} records -->
	                         	</div>
	                         </div>
	                      <!--  <div class = "row">
	                         	<div class = "col-md-4 col-xs-12 numberofRows">	                         		
	                         	</div>
	                         	<div class = "col-md-4 col-xs-12 hidden-xs numberofRows">	
	                         	<input type = "button" class = "btn btn-primary" value = "Employee Reported at Service Desk">   
	                         	<input type = "button" class = "btn btn-default" value = "Cancel">                   		
	                         	</div>
	                         	<div class = "col-md-4 col-xs-12 visible-xs-* hidden-lg hidden-md hidden-sm numberofRows">	
	                         	<input type = "button" class = "btn btn-primary" value = "Reported at Serv. Desk">   
	                         	<input type = "button" class = "btn btn-default" value = "Cancel">                   		
	                         	</div>
	                         	<div class = "col-md-4 col-xs-12 numberofRows">	                         		
	                         	</div>
	                         </div>      -->                   	
                         </div>                        
                </div>
                </div>
                </div>        
           </div>
        </div>
        
    </div>
</div>