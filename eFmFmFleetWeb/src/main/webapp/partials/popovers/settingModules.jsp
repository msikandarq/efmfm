<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    POPUP Templates - Setting Modules for Clients - Super Admin Access only

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/17/2015  Saima Aziz      Initial Creation
05/17/2016  Saima Aziz      Final Creation
-->

<div class = "clientSettingModulesTemplate container-fluid">    
    <div class = "col-md-12 col-xs-12 mainTabDiv_invoice mainTabDiv_clients">

        <!-- /*START OF WRAPPER1 = VENDORWISE INVOICE */ -->
           <div class = "wrapper1 clientsettingWrappers">             

            <tabset class="tabset subTab_invoice">
              <tab ng-click = "">
                <tab-heading>Web Modules</tab-heading>
                    <div class = "clientSettingTabContent row">
                        
       <div class = "row mainSettingModulePopoverDiv">                
       <form class = "col-md-12 col-xs-12 addUserForm" name = "addUserForm">
           <input type = "text" ng-model = "currentClient" class = "hidden">
<!--
           <div class = "">
               <span class="floatLeft inspectorSpan colorWhite marginRight5">
                   <i class = "icon-check inpectionDetailCheckIcon pointer" ng-show = "module.isActive" ng-click = "accessChange(module)" 
                      ng-class = "{disableClick:module.moduleName == 'RequestDetails'}"></i>               
                   <i class = "icon-unchecked inpectionDetailUnCheckIcon pointer" ng-show = "!module.isActive" ng-click = "accessChange(module)" 
                      ng-class = "{disableClick:module.moduleName == 'RequestDetails'}"></i>
              </span> 
           </div>
-->
           <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <div class="panel panel-default accordianPanel_assignModules row" ng-repeat = "module in clientWebModulesData">
                  <a data-toggle="collapse" 
                     data-parent="#accordion" 
                     href="#collapse{{$index}}" 
                     aria-expanded="false" 
                     aria-controls="collapse{{$index}}"
                     ng-click = "setInspectionSelectedDate(inspection)"
                     class = "noTextDecoration assingatag collapse{{$index}} col-md-12">
                <div class="panel-heading_assignModules panelHeading_assignModules collapseZoneDiv{{$index}}" 
                     role="tab" 
                     id="headingOne_assignModules"
                     >
                  <h4 class="panel-title heading_inspection aModulesHeading">  
                      <span class="floatLeft inspectorSpan colorWhite marginRight5">
                           <i class = "icon-check inpectionDetailCheckIcon pointer" ng-show = "module.isActive || module.isActive == 'true'" ng-click = "accessChangeWeb(module)" ></i>               
                           <i class = "icon-unchecked inpectionDetailUnCheckIcon pointer" ng-show = "!module.isActive || module.isActive == 'false'" ng-click = "accessChangeWeb(module)"></i>
                      </span> 
<!--
                      <a data-toggle="collapse" 
                     data-parent="#accordion" 
                     href="#collapse{{$index}}" 
                     aria-expanded="false" 
                     aria-controls="collapse{{$index}}"
                     ng-click = "setInspectionSelectedDate(inspection)"
                     class = "noTextDecoration collapse{{$index}}">
-->
                      <span class="floatLeft inspectionDateSpan colorWhite">{{module.moduleName}}</span> <span class="floatLeft marginLeft10 smallHint" ng-show = " module.moduleName == 'Super Admin'">(Assign access to all Modules)</span>
<!--                      </a>       -->
                    </h4></div>
                    </a>
<!--                      <div id = "ischangeId{{module.moduleId}}" class = "col-md-3 applyChangesbutton tabletdCenter disabled pointer" ng-click = "applychanges(module, employee)" ng-class = "{disableAssignButton: module.moduleName == 'Super Admin' && module.isActive == false, enableAssignButton:module.moduleName == 'Super Admin' && module.isActive == true}"><span class = "floatRight spanChangeAccess">Apply changes</span></div>-->
                
               </div>

<!--
                <div id="collapse{{$index}}temp" 
                     class="panel-collapse collapse accordionContent_assingModules collapse{{$index}}" 
                     role="tabpanel" 
                     aria-labelledby="headingOne_assignModules">
                        <div class = "row assingModuleElements">
                            <div class = "col-md-6 col-xs-12" ng-repeat = "subElement in module.subModules">
                                <input type="checkbox"
                                       ng-model="subElement.isActive"
                                       id = "{{subElement.subModuleId}}"
                                       >
                                <span>{{subElement.subModuleName}}</span>
                                <div class = "subElements_assingModule" ng-repeat = "subElement in element.elements">
                                    <input type="checkbox"
                                       ng-model="subElement.isActive">{{subElement.name}}</div>
                            </div>
                    
                        </div> 
                    
                  </div>
-->
           
           </div>
           <div class = "">
               <input type = "button" class = "btn btn-success" value = "Apply Changes" ng-click = "applyWebModuleChanges(module)">
           </div>
        </form> 
        </div>

    </div>
    </tab>
    <tab ng-click = "">
    <tab-heading>Employee Modules</tab-heading>
                <div class = "clientSettingTabContent row">
       <div class = "row mainSettingModulePopoverDiv">                
       <form class = "col-md-12 col-xs-12 addUserForm" name = "addUserForm">
    <!--
           <div class = "">
               <span class="floatLeft inspectorSpan colorWhite marginRight5">
                   <i class = "icon-check inpectionDetailCheckIcon pointer" ng-show = "module.isActive" ng-click = "accessChange(module)" 
                      ng-class = "{disableClick:module.moduleName == 'RequestDetails'}"></i>               
                   <i class = "icon-unchecked inpectionDetailUnCheckIcon pointer" ng-show = "!module.isActive" ng-click = "accessChange(module)" 
                      ng-class = "{disableClick:module.moduleName == 'RequestDetails'}"></i>
              </span> 
           </div>
    -->
           <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <div class="panel panel-default accordianPanel_assignModules row" ng-repeat = "module in clientEmployeeModulesData">
                  <a data-toggle="collapse" 
                     data-parent="#accordion" 
                     href="#collapse{{$index}}" 
                     aria-expanded="false" 
                     aria-controls="collapse{{$index}}"
                     ng-click = "setInspectionSelectedDate(inspection)"
                     class = "noTextDecoration assingatag collapse{{$index}} col-md-12">
                <div class="panel-heading_assignModules panelHeading_assignModules collapseZoneDiv{{$index}}" 
                     role="tab" 
                     id="headingOne_assignModules"
                     >
                  <h4 class="panel-title heading_inspection aModulesHeading">  
                      <span class="floatLeft inspectorSpan colorWhite marginRight5">
                           <i class = "icon-check inpectionDetailCheckIcon pointer" ng-show = "module.isActive || module.isActive == 'true'" ng-click = "accessChangeEmployee(module)"></i>               
                           <i class = "icon-unchecked inpectionDetailUnCheckIcon pointer" ng-show = "!module.isActive || module.isActive == 'false'" ng-click = "accessChangeEmployee(module)" ></i>
                      </span> 
    <!--
                      <a data-toggle="collapse" 
                     data-parent="#accordion" 
                     href="#collapse{{$index}}" 
                     aria-expanded="false" 
                     aria-controls="collapse{{$index}}"
                     ng-click = "setInspectionSelectedDate(inspection)"
                     class = "noTextDecoration collapse{{$index}}">
    -->
                      <span class="floatLeft inspectionDateSpan colorWhite">{{module.moduleName}}</span> <span class="floatLeft marginLeft10 smallHint" ng-show = " module.moduleName == 'Super Admin'">(Assign access to all Modules)</span>
    <!--                      </a>       -->
                    </h4></div>
                    </a>
<!--                      <div id = "ischangeId{{module.moduleId}}" class = "col-md-3 applyChangesbutton tabletdCenter disabled pointer" ng-click = "applychanges(module, employee)" ng-class = "{disableAssignButton: module.moduleName == 'Super Admin' && module.isActive == false, enableAssignButton:module.moduleName == 'Super Admin' && module.isActive == true}"><span class = "floatRight spanChangeAccess">Apply changes</span></div>-->
                </div>

                <div id="collapse{{$index}}temp" 
                     class="panel-collapse collapse accordionContent_assingModules collapse{{$index}}" 
                     role="tabpanel" 
                     aria-labelledby="headingOne_assignModules">
                        <div class = "row assingModuleElements">
                            <div class = "col-md-6 col-xs-12" ng-repeat = "subElement in module.subModules">
                                <input type="checkbox"
                                       ng-model="subElement.isActive"
                                       id = "{{subElement.subModuleId}}"
                                       >
                                <span>{{subElement.subModuleName}}</span>
    <!--
                                <div class = "subElements_assingModule" ng-repeat = "subElement in element.elements">
                                    <input type="checkbox"
                                       ng-model="subElement.isActive">{{subElement.name}}</div>
    -->
                            </div>

                        </div> 

                      </div>
               </div>
           <div class = "">
               <input type = "button" class = "btn btn-success" value = "Apply Changes" ng-click = "applyEmployeeModuleChanges(module)">
           </div>
            </form> 
            </div>
        </div>
      </tab> 					
    </tabset>	
 </div>
</div>
</div>
