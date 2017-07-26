
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "addVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add Employee Wise</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div>  
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
          <form class = "addEmployeeIdForm" name = "addEmployeeIdForm">
          <div class="row">
              <div class="col-md-3">
                  <span><strong>Select Search Type</strong></span>
                  <select ng-model="search.searchType"
                          class="form-control" 
                          ng-options="searchType.text for searchType in searchTypes track by searchType.value"
                          ng-change = "setSearchType(search.searchType)"
                          >
                      <option value="">Select Search Type</option>
                  </select>
              </div>
          <div class="col-md-3 pointer">
          <span><strong>Select Search Type</strong></span>
          <div id="imaginary_container"> 
              <div class="input-group stylish-input-group" 
                   ng-keypress="($event.charCode==13)? searchEmployeeDetails(employeeDetails,search.searchType) : return">
              <input type="text" 
                     class="form-control" 
                     ng-model="employeeDetails" 
                     ng-disabled="searchTypeDisabled" 
                     placeholder="Search {{search.searchType.text}}" >
              <span class="input-group-addon" 
                    ng-disabled="searchTypeDisabled" 
                    ng-click="searchEmployeeDetails(employeeDetails,search.searchType)">
              <span class="glyphicon glyphicon-search"></span>
              </span>
              </div>
          </div>
          </div>
          <div class="col-md-2">
                <span><strong>Base Facility</strong></span>
                <input type="text" 
                       class="form-control" 
                       placeholder="Base Facility" 
                       ng-model="baseFacility" 
                       ng-disabled="true" 
                       name=""> 
          </div>
          <div class="col-md-4">
          <span><strong>Sub Facility</strong></span>
                <multi-select-dropdown-span-five 
                      input-model="compineFacilityDetails"
                      output-model="compineFacility" 
                      button-label="icon name"
                      item-label="icon name maker"
                      tick-property="ticked"
                >
                </multi-select-dropdown-span-five>
          </div>
          </div>
          <div>
          <table class="table table-bordered">
          <thead>
          <tr>
              <th>Sub Facility List <span ng-show="compineFacility.length > 0"> </span></th>
              <th>Remove</th>
          </tr>
          </thead>
          <tbody ng-show="compineFacility.length==0">
              <tr>
              <td colspan='12'>
              <div class="noData">There are no Facility Seleted Yet</div>
              </td>
              </tr>
          </tbody>
          <tbody ng-show="compineFacility.length > 0">
          <tr ng-repeat="item in compineFacility">
          <td><strong>{{$index + 1}} ) </strong> {{item.name}}</td>
          <td><div class="icon-remove-sign mapMarkerDelete pointer enableAddButton" ng-click="deleteCompineFacility(item,$index)"></div></td>
          </tr>
          </tbody>

          </table>
          </div>

          </form>
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
     <button type="button" class="btn btn-success buttonRadius0" ng-click = "addEmployeeId(baseFacility,compineFacility,compineFacilityDetails,employeeDetails,search.searchType)" ng-disabled= "addEmployeeIdForm.$invalid">Add Employee Wise Facility</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>