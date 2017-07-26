<!-- 
@date           04/15/2016
@Author         Saima Aziz
@Description    This page comes when user try to access a page which he does not have access to
@State          home.clientSettings 
@URL            /clientSettings 

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/17/2016  Saima Aziz      Initial Creation
05/17/2016  Saima Aziz      Final Creation
-->
 
<div class = "empTravelDeskTemplate container-fluid" ng-if = "isEmployeeRosterActive">
	<div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <span class = "col-md-12 col-xs-12">Client Settings and Configurations
                <span class = 'spinner_travelDesk'>
                <img ng-src='images/spiffygif_24x24.gif' 
                     ng-show = 'isProcessing'>
                </span>
            </span>
            <div class = "col-md-12 col-xs-12 mainTabDiv_EmployeeTravelDesk mainTabDiv_clientSetting">
            <div class = 'col-md-12 col-xs-12'>
                <button type = 'button' 
                        class = 'btn btn-success floatLeft buttonRadius0 '
                        ng-click = "addNewClient('lg', 'client')"><span>Add Client</span></button>
            </div>
            <div class = "col-md-12 marginTop20">
                <div class = "row mainClientDiv1"  ng-repeat = "client in clientsData">
                  <div class = "col-md-5 mainClientDiv">
                      <div class = "row">
                          <div class = "col-md-12">
                             <div class = "row clientHeading">
                                 <div class = "col-md-12 clientName">{{client.branchName}}
<!--                                     <span class = "cityClient">Chennai, India</span>-->
                                 </div>
                             </div>
                             <div class = "row">
                                 <div class = "col-md-4 col-xs-4 aboutClient" ng-click = "aboutClient(client)">About</div>
                                 <div class = "col-md-4 col-xs-4 settingModules" ng-click = "moduleSettingsClient(client)">Settings</div>
                                 <!-- <div class = "col-md-3 col-xs-3 addBranchModules" ng-click = "addBranch(client, 'lg', 'branch')">Add Branch</div> -->
                                 <div class = "col-md-4 col-xs-4 enableDisableClient" ng-click = "disableClient(client)">Disable</div>
                             </div>
                          </div>
                      </div>
                  </div>
                  <div class = "col-md-12">
                             <div class = "row">
                                 <div class = "col-md-12 aboutClientContent" ng-show = "client.isAbout">
                                     <div class = "aboutDivContent row">
                                         <div class = "col-md-12"><span>Branch Id    : </span><span>{{client.branchId}}</span></div>
                                         <div class = "col-md-12"><span>Branch Code  : </span><span>{{client.branchCode}}</span></div>
                                         <div class = "col-md-12"><span>Branch URL   : </span><span>{{client.branchUri}}</span></div>
                                         <div class = "col-md-12"><span>Client since : </span><span>March 2015</span></div>
                                     </div>
                                 
                                 </div>
                                 <div class = "col-md-12 settingModulesContent" ng-if = "client.isModuleSettings && gotClientModuleSettingsData">
                                     <div ng-include = "'partials/popovers/settingModules.jsp'"></div>
                                 </div>
                                 <div class = "col-md-12 enableDisableClientContent" ng-show = "client.isDisable">Disable</div>
                             </div>
                  </div>
                </div>
            </div>
           </div>
        </div>
        
    </div>
</div>