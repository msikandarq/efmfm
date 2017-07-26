<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    It is a template - ng-include in the vendorDashboard page under Vendor Managements
@State          
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<div class = "viewVehicle_VendorDashBoard">
<img src = "images/portlet-remove-icon.png" class="img-circle pointer" ng-click = "viewSupervisor(post, $index)">
	<div class = "row">
	 <div class = "col-md-4">
		<input id = "addVehicleNew" 
			   class = "addVehicle_vendor btn btn-primary" 
			   type = "button" 
			   ng-click = "addSupervisorDetails(post, 'lg');" 
			   value = "Add New Supervisor">	
	 </div>
	 <div class = "col-md-8">
         <input type = "text" 
                ng-model = "searchSupervisorDetails" 
                class = "searchboxDriverVehicle searchbox form-control floatRight" 
                placeholder = "Search Supervisors"
                expect_special_char></div>
	</div>

 <tabset class="tabset subTab_invoice"> <tab
            ng-click="getSupervisorDetails(post,'active')"> <tab-heading>Supervisor Management - Active</tab-heading>
          <div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
            <div class="employeeListContent">
              <div class="row paddingLeftRight10">
                  <table class = "escortDetailTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Supervisor Id</th>
                                      <th>Supervisor Name</th>
                                      <th>Supervisor Number</th>
                                      <th>Address</th>
                                      <th>Facility Name</th>
                                      <th>Action</th>
                                    </tr> 
                                </thead>

	                            <tbody ng-show="getSupervisorData.length==0">
									<tr>
										<td colspan='5'>
											<div class="noData">There are no active Supervisor</div>
										</td>
									</tr>
								</tbody>

                             <tbody ng-show="getSupervisorData.length>0">

                               <tr class = "visibleRow" 
                                       ng-repeat-start = "post in getSupervisorData |limitTo: numberofRecords | filter:searchSupervisorDetails">
                                     <td class = "col-md-1">{{post.supervisorId}}</td>
                                     <td class = "col-md-2">{{post.supervisorName}}</td>   
                                         <td class = "col-md-2">{{post.supervisorMobileNumber}}</td>
                                     <td class = "col-md-3">{{post.supervisorAddress}}</td>
                                     <td class = "col-md-2">{{post.facilityName}}</td>
                                         <td class = "col-md-4">
                                             <input id= "viewEscortDetailButton_vendor" 
                                          class = "viewEscortDetail_vendor btn btn-primary btn-xs supervisorActionButton" 
                                          type = "button"  
                                          ng-click = "viewSupervisorDetail(post, $index)" 
                                          value = "View Detail">  <br/>
                                             <input id= "editEscortButton_vendor{{post.regId}}" 
                                          class = "viewEscortDetail_vendor btn btn-warning btn-xs supervisorActionButton"
                                          type = "button"  
                                          ng-click = "editSupervisorDetail(post, $index,'lg','active')" 
                                          value = "Edit"><br/>
                                            <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-danger btn-xs supervisorActionButton" 
                                                    type = "button"
                                                    ng-click = "disableSupervisor(post,$index)"             
                                                    value = "Disable">
                                   </td>  
                                </tr> 
                                    <tr ng-repeat-end=""
                                        ng-show = "post.isClicked"
                                        ng-class = "escort{{post.vehicleId}}" 
                                        class = "unVisisbleRow">
                                        <td colspan="5" class = "unVisisbleRowtd">
                                            <div class = "row">
                                                <div class = "col-md-12 escortLabel">
                                                    <img src="images/portlet-remove-icon.png" class="floatRight pointer" ng-click="closeEscortDetail(post)"></div>
                                                <!-- <div class = "col-md-2 escortLabel">
                                                        <span>Supervisor Id</span><br><span>{{supervisorDetailIndex.supervisorId}}</span></div> -->
                                                       
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Full Name</span><br><span>{{supervisorDetailIndex.supervisorName}}</span></div>
                                                <div class = "col-md-2 escortLabel hidden">
                                                        <span>Last Name</span><br><span>{{supervisorDetailIndex.supervisorlastName}}</span></div>
                                                <div class = "col-md-2 escortLabel hidden">
                                                        <span>Supervisor Id</span><br><span>{{supervisorDetailIndex.supervisorId}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Supervisor Id</span><br><span>{{supervisorDetailIndex.supervisorEmployeeId}}</span></div>
                                                <div class = "col-md-2 escortLabel hidden">
                                                        <span>Physically Challenged</span><br><span>{{supervisorDetailIndex.physicallyChallenged}}</span></div>
                                                <div class = "col-md-2 escortLabel hidden">
                                                        <span>Vendor Name</span><br><span>{{supervisorDetailIndex.vendorName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Father's Name</span><br><span>{{supervisorDetailIndex.supervisorFatherName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Gender</span><br><span>{{supervisorDetailIndex.supervisorGender}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Date of Birth</span><br><span>{{supervisorDetailIndex.supervisordob}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Mobile Number</span><br><span>{{supervisorDetailIndex.supervisorMobileNumber}}</span></div>
                                                
                                                <div class = "col-md-3 escortLabel">
                                                        <span>Permanent Address</span><br><span>{{supervisorDetailIndex.supervisorAddress}}</span></div>                                                                
                                                <div class = "col-md-3 escortLabel">
                                                        <span>Present Address</span><br><span>{{supervisorDetailIndex.presentAddress}}</span></div> 
                                                <div class = "col-md-2 escortLabel ">
                                                        <span>Email</span><br><span>{{supervisorDetailIndex.emailId}}</span></div> 
                                                <div class = "col-md-3 escortLabel">
                                                        <span>Remarks</span><br><span>{{supervisorDetailIndex.remarks}}</span></div> 
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Designation</span><br><span>{{supervisorDetailIndex.designation}}</span></div>  
                                                      
                                            </div>
                                        </td>
                                    </tr>
                             </tbody>
                            </table>
                </div>
            </div>
            
            
          </div>
          </tab> 

          <tab ng-click="getSupervisorDetails(post, 'Inactive')"> 
              <tab-heading>Supervisor Management - InActive</tab-heading>
                 <div class="wrapper1 wrapper1_travelDesk1" id="employeeList">
            <div class="employeeListContent">
              <div class="row paddingLeftRight10">
                  <table class = "escortDetailTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Supervisor Id</th>
                                      <th>Supervisor Name</th>
                                      <th>Supervisor Number</th>
                                      <th>Address</th>
                                      <th>Facility Name</th>
                                      <th></th>
                                    </tr> 
                                </thead>

                                <tbody ng-show="getSupervisorData.length==0">
									<tr>
										<td colspan='5'>
											<div class="noData">There are no Inactive Supervisor</div>
										</td>
									</tr>
								</tbody>

                               <tbody ng-show="getSupervisorData.length>0">
                               <tr class = "visibleRow" 
                                       ng-repeat-start = "post in getSupervisorData |limitTo: numberofRecords | filter:searchSupervisorDetails">
                                     <td class = "col-md-1">{{post.supervisorId}}</td>
                                     <td class = "col-md-2">{{post.supervisorName}}</td>   
                                         <td class = "col-md-2">{{post.supervisorMobileNumber}}</td>
                                     <td class = "col-md-3">{{post.supervisorAddress}}</td>
                                     <td class = "col-md-2">{{post.facilityName}}</td>
                                         <td class = "col-md-4">
                                             <input id= "viewEscortDetailButton_vendor" 
                                          class = "viewEscortDetail_vendor btn btn-primary btn-xs" 
                                          type = "button"  
                                          ng-click = "viewSupervisorDetail(post, $index)" 
                                          value = "View Detail">  
                                             <!-- <input id= "editEscortButton_vendor{{post.regId}}" 
                                          class = "viewEscortDetail_vendor btn btn-warning btn-xs"
                                          type = "button"  
                                          ng-click = "editSupervisorDetail(post, $index,'lg','inActive')" 
                                          value = "Edit"> -->
                                             <!-- <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-success btn-xs" 
                                                    type = "button"
                                                    ng-click = "uploadEscort(post, $parent.$index, $index)"             
                                                    value = "Upload"> -->
                                            <input id = "uploadEscortButton_vendor" 
                                                    class = "uploadEscort_vendor btn btn-success btn-xs" 
                                                    type = "button"
                                                    ng-click = "enableSupervisor(post,$index)"       
                                                    value = "Enable">
                                   </td>  
                                </tr> 
                                    <tr ng-repeat-end=""
                                        ng-show = "post.isClicked"
                                        ng-class = "escort{{post.vehicleId}}" 
                                        class = "unVisisbleRow">
                                        <td colspan="5" class = "unVisisbleRowtd">
                                            <div class = "row">
                                                <div class = "col-md-12 escortLabel">
                                                    <img src="images/portlet-remove-icon.png" class="floatRight pointer" ng-click="closeEscortDetail(post)"></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Supervisor Id</span><br><span>{{supervisorDetailIndex.supervisorId}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Full Name</span><br><span>{{supervisorDetailIndex.supervisorName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Father's Name</span><br><span>{{supervisorDetailIndex.supervisorFatherName}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Gender</span><br><span>{{supervisorDetailIndex.supervisorGender}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Date of Birth</span><br><span>{{supervisorDetailIndex.supervisordob}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Mobile Number</span><br><span>{{supervisorDetailIndex.supervisorMobileNumber}}</span></div>
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Employee Id</span><br><span>{{supervisorDetailIndex.supervisorEmployeeId}}</span></div>
                                                        
                                                <div class = "col-md-3 escortLabel">
                                                        <span>Permanent Address</span><br><span>{{supervisorDetailIndex.supervisorAddress}}</span></div>                                                                
                                                <div class = "col-md-3 escortLabel">
                                                        <span>Present Address</span><br><span>{{supervisorDetailIndex.presentAddress}}</span></div> 

                                                <div class = "col-md-3 escortLabel">
                                                        <span>Remarks</span><br><span>{{supervisorDetailIndex.remarks}}</span></div> 
                                                <div class = "col-md-2 escortLabel">
                                                        <span>Designation</span><br><span>{{supervisorDetailIndex.designation}}</span></div>        
                                            </div>
                                        </td>
                                    </tr>
                             </tbody>
                            </table>
                </div>
            </div>
            
            <div class="row">
              <div class="wrapper2_EmpTravelDesk" id="employeeTravelDeskExport">
              
                
              </div>
            </div>
          </div>
              </tab> 
          </tabset>
        </div>
      </div>
    </div>
  </div>
</div>                    
</div>
</tab>

</div>