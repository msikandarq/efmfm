<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    This page gives a complete detail of Employees with the option to edit their location and to Enable and Disable the Employee from the list
@State          home.empDetails 
@URL            /empDetails   

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div class="empDetailsTemplate container-fluid" ng-if="isEmployeeDetailActive">
    <div class="row">
        <div class="col-md-12 col-xs-12 heading1">
            <span class="col-md-8 col-xs-8 vendorDashboardLabel">
            Employee Details
          </span>
            <span class="col-md-2 col-xs-2 vendorDashboardMultiSelect" ng-show="multiFacility == 'Y'">
              <am-multiselect class="input-lg pull-right" 
                                       multiple="true"
                                       ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                       ng-model="facilityData"
                                       ms-header="All Facility"
                                       options="facility.branchId as facility.name for facility in facilityDetails"
                                       >
                     </am-multiselect>
          </span>
            <span class="col-md-2 col-xs-2 vendorDashboardGetFacilityButton" ng-show="multiFacility == 'Y'">
            <input type="button" class="btn btn-success" value="Submit" name="" ng-click="getFacilityDetails(facilityData)">
          </span>
            <span class="col-md-2 col-xs-2 singleFacilityStyle" ng-show="multiFacility == 'N'">
          </span>

            <div class="col-md-12 col-xs-12 mainTabDiv_EmployeeDetails">
                <div class="wrapper1" id="employeeList">
                    <div class="heading2 row">
                        <span class="col-md-7 floatLeft">All Registered Employees</span>
                        <div class="col-md-5 floatRight">
                            <efmfm-button img-class="efmfm_approvalButtons_collapse" src-url="images/portlet-collapse-icon" selected-url="images/portlet-expand-icon" hover-url="images/portlet-collapse-icon" alt-text="Collapse Window" main-div="employeeList" target-div="employeeListContent">
                            </efmfm-button>
                            <efmfm-button img-class="efmfm_approvalButtons_reload" src-url="images/portlet-reload-icon" selected-url="images/portlet-reload-icon" hover-url="images/portlet-reload-icon" alt-text="Reload Window" main-div="employeeList" target-div="employeeListContent" ng-click='refreshEmpDetail()'>
                            </efmfm-button>
                            <efmfm-button img-class="efmfm_dashboarButtons_remove" src-url="images/portlet-remove-icon" selected-url="images/portlet-remove-icon" hover-url="images/portlet-remove-icon" alt-text="Remove Window" main-div="employeeList" target-div="employeeListContent">
                            </efmfm-button>
                        </div>
                    </div>
                    <div class="employeeListContent">
                        <!-- /*FIRST ROW*/-->
                        <div class="row">
                            <div class="col-md-2 showRecordsSelect">
                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                            </div>
                            <div class='col-md-3'>
                                <div class="input-group floatLeft calendarInput">
                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData)" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search)">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <input type="text" ng-model="searchEmployeeDetails" class='form-control floatRight' placeholder='Filter Employee Details' expect_special_char>
                            </div>
                            <div class="col-md-2 showRecordsSelect">
                                <select ng-model="employeeType" class="form-control" ng-options="employee.text for employee in employeeTypes track by employee.value" ng-change="setEmployeeType(employeeType)">
                      </select>

                            </div>
                            <div class="col-md-1">
                                <!--  <select ng-model="pageSize" id="pageSize" class="form-control"
                                            ng-change = "setpageSize(pageSize); currentPage = 1">
                                                  <option value="5">5</option>
                                                  <option value="10">10</option>
                                                  <option value="15">15</option>
                                                  <option value="20">20</option>
                                                  <option value="40">40</option>
                                                  <option value="60">60</option>
                                            </select> -->
                            </div>
                            <div class="col-md-2 showRecordsSelect">
                                <button class="btn btn-sm btn-success form-control exelBTN floatRight" ng-click="saveInExcel()">
                                  <i class = "icon-download-alt"></i>
                                  <span class = "marginLeft5">Excel</span>
                              </button>

                            </div>

                            <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotEmployeeDetailsData">
                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                    </div>

 --></div>
                        <div class="" ng-show="gotEmployeeDetailsData">
                            <div class="row divTableHeader" style="margin: 0; padding: 0; font-weight: 900;">
                                <div class="col-md-1 col-xs-1 paddingTop30px">Facility Name</div>
                                <div class="col-md-9 col-xs-9 padding0 borderRL">
                                    <div class="col-md-12 col-xs-12 empDetailsEmp">
                                        Employee
                                    </div>
                                    <div class="col-md-12 col-xs-12 paddingTB">
                                        <div class="col-md-1 col-xs-1">Id</div>
                                        <div class="col-md-2 col-xs-2">Name</div>
                                        <div class="col-md-2 col-xs-2">Address</div>
                                        <div class="col-md-1 col-xs-1">Gender</div>
                                        <div class="col-md-1 col-xs-1">Number</div>
                                        <div class="col-md-2 col-xs-2">EmailId</div>
                                        <div class="col-md-1 col-xs-1">Edit</div>
                                        <div class="col-md-2 col-xs-2">Enable / Disable</div>
                                    </div>
                                </div>
                                <div class="col-md-2 col-xs-2 paddingTop30px">Enable / Disable Geo-Code</div>
                            </div>
                            <div class="constrained row margin0">
                                <div class="overflowXauto" infinite-scroll="getEmployeesDetail()" infinite-scroll-container='".constrained"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                    <div class='row margin0' ng-repeat="post in posts" class="employee{{post.employeeId}}" ng-show="posts.length > 0">
                                        <div class="col-md-1 col-xs-1 divTableData textBreak">{{post.facilityName}}</div>

                                        <div class="col-md-9 col-xs-9">

                                            <div class="col-md-1 col-xs-1 divTableData textBreak">{{post.employeeId}}<br/>
                                                <div class="pregnantLady floatLeft pointer" tooltip="Pregnant Employee" tooltip-placement="right" tooltip-trigger="mouseenter" ng-show="post.pragnentLady == 'YES'"></div>
                                                <div class="handicap floatLeft pointer" tooltip="Handicap Employee" tooltip-placement="right" tooltip-trigger="mouseenter" ng-show="post.physicallyChallenged == 'YES'"></div>
                                                <div class="isinjures floatLeft pointer" tooltip="Injured Employee" tooltip-placement="right" tooltip-trigger="mouseenter" ng-show="post.isInjured == 'YES'"></div>
                                                <div class="isVIPflag floatLeft pointer" tooltip="VIP" tooltip-placement="right" tooltip-trigger="mouseenter" ng-if="employee.isVIP == 'Yes'"></div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 divTableData textBreak">{{post.employeeName}}</div>
                                            <div class="col-md-2 col-xs-2 divTableData textBreak">{{post.employeeAddress}}</div>
                                            <div class="col-md-1 col-xs-1 divTableData textBreak">
                                                <span ng-if="post.employeeGender == 1">Male</span>
                                                <span ng-if="post.employeeGender == 2">Female</span>
                                                <span ng-if="post.employeeGender == 'Male'">Male</span>
                                                <span ng-if="post.employeeGender == 'Female'">Female</span>
                                            </div>
                                            <div class="col-md-1 col-xs-1 divTableData textBreak">{{post.mobileNumber}}</div>
                                            <div class="col-md-2 col-xs-2 divTableData textBreak">{{post.emailId}}</div>
                                            <div class="col-md-1 col-xs-1 divTableData textBreak"><input type="button" class="btn btn-sm btn-primary editButton_travelDesk" value="Edit" ng-click="editEmployee(post, 'lg')"></div>
                                            <div class="col-md-2 col-xs-2 divTableData textBreak"><input class="btn btn-sm btn-warning buttonRadius0 enableDisableEmpDetailButton" ng-click="employeeStatusChange(post)" ng-value="post.employeeStatusToBe" ng-show="post.employeeStatusToBe == 'Disable'" />
                                                <input class="btn btn-sm btn-success buttonRadius0 enableDisableEmpDetailButton" ng-click="employeeStatusChange(post)" ng-value="post.employeeStatusToBe" ng-show="post.employeeStatusToBe == 'Enable'" /></div>

                                        </div>
                                        <div class="col-md-2 col-xs-2 divTableData textBreak"><input class="btn btn-sm btn-danger buttonRadius0" ng-click="employeeGeoCode(post)" value="Enable Geo-Code" ng-show="post.locationStatus == 'Y'" />
                                            <input class="btn btn-sm btn-info buttonRadius0" ng-click="employeeGeoCode(post)" value="Disable Geo-Code" ng-show="post.locationStatus == 'N'" /></div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" ng-show="getYetToGeoCodedEmployee.length == 0">
                                <div class="noData">There is No Data for App Download Details</div>
                            </div>
                        </div>

                        <!--   /*SECOND ROW*/-->
                        <!-- <div class="row pointer" ng-show="gotEmployeeDetailsData">
                            <div class="wrapper2_EmpDetail constrained">
                                <table class="employeeListTable_empDetail table table-hover table-responsive container-fluid" infinite-scroll="getEmployeesDetail()" infinite-scroll-parent="true" infinite-scroll-distance='5'>
                                    <thead class="tableHeading">
                                        <tr>
                                            <th>Employee Id</th>
                                            <th>Employee Name</th>
                                            <th>Employee Address</th>
                                            <th>Employee Gender</th>
                                            <th>Employee Number</th>
                                            <th>Employee EmailId</th>
                                            <th>Facility Name</th>
                                            <th>Edit Employee Details</th>
                                            <th>Enable/Disable Employee</th>
                                            <th>Enable/Disable Geo-Code</th>
                                        </tr>
                                    </thead>

                                    <tbody ng-show="posts.length == 0">
                                        <tr>
                                            <td colspan="12">
                                                <div class="noData">There is No Record found</div>
                                            </td>
                                        </tr>
                                    </tbody>

                                    <tbody>

                                        <tr ng-repeat="post in posts" class="employee{{post.employeeId}}" ng-show="posts.length > 0">
                                            <td class="col-md-1">{{post.employeeId}}<br/>
                                                <div class="pregnantLady floatLeft pointer" tooltip="Pregnant Employee" tooltip-placement="right" tooltip-trigger="mouseenter" ng-show="post.pragnentLady == 'YES'"></div>
                                                <div class="handicap floatLeft pointer" tooltip="Handicap Employee" tooltip-placement="right" tooltip-trigger="mouseenter" ng-show="post.physicallyChallenged == 'YES'"></div>
                                                <div class="isinjures floatLeft pointer" tooltip="Injured Employee" tooltip-placement="right" tooltip-trigger="mouseenter" ng-show="post.isInjured == 'YES'"></div>
                                                <div class="isVIPflag floatLeft pointer" tooltip="VIP" tooltip-placement="right" tooltip-trigger="mouseenter" ng-if="employee.isVIP == 'Yes'"></div>
                                            </td>
                                            <td class="col-md-2">{{post.employeeName}}</td>
                                            <td class="col-md-3">{{post.employeeAddress}}</td>
                                            <td class="col-md-1">
                                                <span ng-if="post.employeeGender == 1">Male</span>
                                                <span ng-if="post.employeeGender == 2">Female</span>
                                                <span ng-if="post.employeeGender == 'Male'">Male</span>
                                                <span ng-if="post.employeeGender == 'Female'">Female</span>
                                            </td>
                                            <td class="col-md-1">{{post.mobileNumber}}</td>
                                            <td class="col-md-2">{{post.emailId}}</td>
                                            <td class="col-md-2">{{post.facilityName}}</td>
                                            <td class="col-md-1">
                                                <input type="button" class="btn btn-sm btn-primary editButton_travelDesk" value="Edit" ng-click="editEmployee(post, 'lg')">
                                            </td>
                                            <td class="col-md-1">
                                                <input class="btn btn-sm btn-warning buttonRadius0 enableDisableEmpDetailButton" ng-click="employeeStatusChange(post)" ng-value="post.employeeStatusToBe" ng-show="post.employeeStatusToBe == 'Disable'" />

                        <input class="btn btn-sm btn-success buttonRadius0 enableDisableEmpDetailButton" ng-click="employeeStatusChange(post)" ng-value="post.employeeStatusToBe" ng-show="post.employeeStatusToBe == 'Enable'" />
                        </td>
                        <td class="col-md-1">
                            <input class="btn btn-sm btn-danger buttonRadius0" ng-click="employeeGeoCode(post)" value="Enable Geo-Code" ng-show="post.locationStatus == 'Y'" />
                  
                            <input class="btn btn-sm btn-info buttonRadius0" ng-click="employeeGeoCode(post)" value="Disable Geo-Code" ng-show="post.locationStatus == 'N'" />
                        </td>
                        </tr>
                        </tbody>

                        </table>
                    </div>
                    <div class="wrapper3_EmpDetail">
                        <div class="row">
                            <div class="col-md-6 numberofRows" ng-show=( showSearchResultCount)>
                                Total {{posts.length}} records
                            </div>
                            <div class="col-md-6 numberofRows">
                            </div>
                        </div>
                    </div> 

                     <div class="row">
                                        <div class="col-sm-5"></div>
                                        <div class="col-sm-4">
                                          
                                          <button class="btn btn-success btn-center" ng-disabled="currentPage == 0" ng-click="currentPage = currentPage-1">
                                        Previous
                                        </button>
                                        {{currentPage+1}}/{{numberOfPages()}}
                                        <button class="btn btn-primary btn-center" ng-disabled="currentPage >= employeeDetailsLength/pageSize - 1" ng-click="currentPage=currentPage+1">
                                        Next
                                        </button>
                                        </div>
                                        <div class="col-sm-3"></div>
                                        

                                </div> -->

                    <!-- </div> -->
                </div>
            </div>
        </div>
    </div>

</div>
</div>