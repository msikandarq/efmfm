<div class = "empRequestDetailsTabContent row">
        <form name = "form.projectForm" class = "createNewRequestForm">
            <div class = "createNewRequest">
                <div class = "col-md-1 col-xs-12"> </div>
                <div class = "col-md-3 col-xs-12">
                     <div><label>Project Ids and Names</label></div>
                     <am-multiselect class="input-lg dynamicDaysInput projectBtnWidth"
                                    multiple="true"
                                    ms-selected ="{{delegatedProject.project.length}} Projects selected"
                                    ng-model="delegatedProject.project"
                                    ms-header="Select Projects"
                                    options="listOfProjectId.projectId as listOfProjectId.ClientprojectId + ' - ' + listOfProjectId.projectName for listOfProjectId in listOfProjectNameAdhoc"
                                    change="setDynamicDays(dynamicDays)" required>
                    </am-multiselect>
       
                 </div>
                 <div class = "col-md-3 col-xs-12">
                     <div><label>Employee</label></div> 
                <input type="text" ng-model="delegatedProject.employee" ng-model-options="modelOptions" placeholder = "Enter Employee Id"
                typeahead="employee as employee.employeeId for employee in employeeDetails | filter:$viewValue | limitTo:8" class="form-control"
                required>
  
                 </div>
                 <div class="col-md-2 col-xs-12 buttonStyleFuel">    
                  <button type="button" class="btn btn-success" ng-click = "addDelegatedProject(delegatedProject, form.projectForm)" ng-disabled="form.projectForm.$invalid">Submit</button> 
                    <button type="button" class="btn btn-warning" ng-click = "resetDelegatedProject(form.projectForm)">Reset</button>    
                </div>            
               </div>
               <br>
        </form>
</div>