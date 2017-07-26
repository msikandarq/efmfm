<div class = 'invoiceTabContent'>
         <div class = "firstRowInvoce ">
          <div class = "row">
                <div class = "col-md-2">
                </div>

                <div class = "col-md-6">
                </div>
                <div class = "col-md-2 text-right">
                      <!-- <button type="button" class="btn btn-success" ng-click = "disableAllProjects()">Disable All</button>  -->
                </div>
                <div class = "col-md-2">
                      <input class = "form-control" ng-model = "projectSearchText" placeholder = "Filter Delegates Details"
                      expect_special_char>
                </div>
         </div>
      </div>
<div class="container-fluid">
                                <div class="alert alert-info text-center" ng-if="NodelegateUsers"!>
  <strong>No results Found</strong> 
</div>
  <div ng-if="delegateUsersTable">
    <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
       <thead class ="tableHeading">
         <tr>
              <!-- <th><input type="checkbox" ng-model="addCheckBox" 
              ng-change="projectSelectAll(addCheckBox)"></th>
-->           <th>Project Id</th>
              <th>Project Name</th>
              <th>Delegate Date</th>
              <th>Delegate User</th>
              <th>Delegated By</th>
              <th>Edit</th>
              <th>Status</th>
          </tr>
      </thead>
      <tbody>

     <tr class = 'tabletdCenter'
                                             ng-repeat="delegateUser in delegateUsers | filter:projectSearchText">
                                             <!-- <td><input type="checkbox" ng-model="projectDetail.selected" 
                                                    ng-change="projectSelectIndividual(projectDetail.selected)">
                                                </td> -->
                                                <td><span>{{delegateUser.clientProjectId}}</span></td>
                                             
                                                <td> <span>{{delegateUser.projectName}}</span> </td>
                                       
                                                <td> <span>{{delegateUser.projectStartDate}}</span> </td>
                                               
                                                
                                                <td ng-hide="editProjectForm == $index"> <span>{{delegateUser.reportingManagerUserId}}</span> </td>
                                                <td  ng-show="editProjectForm == $index">
                                                <input type="text" ng-model="editDelegated.employee" ng-model-options="modelOptions"  typeahead="employee as employee.employeeId for employee in employeeDetails | filter:$viewValue | limitTo:8" 
                                                class="form-control" required>
                                                </td>
                                                <td> <span>{{delegateUser.delegatedBy}}</span> </td>
                                                <td class="nowrapText">
                                                  <button type="submit" 
                                                          ng-disabled="false" 
                                                          ng-show="editProjectForm == $index"
                                                          ng-click="saveEditDelegateUser(editDelegated, delegateUser)"
                                                          class="btn btn-success btn-sm">Save
                                                  </button>
                                                  <button type="button" 
                                                          ng-disabled="rowform.$waiting"
                                                          ng-show="editProjectForm == $index" 
                                                          ng-click="cancelEditDelegateUser()" 
                                                          class="btn btn-danger btn-sm">Cancel
                                                  </button>
                                                  <div class="buttons" ng-hide="editProjectForm == $index">
                                                    <button class="btn btn-primary btn-sm" 
                                                    ng-click="editDelegateUser(delegateUser, $index)">Edit</button>
                                                  </div>  
                                                </td>
                                           <!--      <td>
                                                <button type="button" class="btn btn-info" 
                                                ng-click = "editProject()">Edit</button>
                                                </td> -->
                                                <td>
                                                <button type="button" class="btn btn-warning" 
                                                ng-click = "disableDelegateProject(delegateUser, $index)">Disable</button>
                                                </td>
                                        </tr>
</tbody>
</table>
</div>
</div>
</div>