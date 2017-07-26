<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "moduleAccessFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Assign Process</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
           <div class = "row employeeInfo">
               <div class = "col-md-12">
                   <span>Name - </span><span>{{employeeName}}</span>
               </div>
               <div class = "col-md-12">
                   <span>Current Role - </span><span>{{userRole}}</span>
               </div>
           </div>
       <form class = "addUserForm" name = "addUserForm">
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
              <div class="panel panel-default accordianPanel_assignModules row" ng-repeat = "module in modulesArrayData">
                  <a data-toggle="collapse" 
                     data-parent="#accordion" 
                     href="#collapse{{$index}}" 
                     aria-expanded="false" 
                     aria-controls="collapse{{$index}}"
                     ng-click = "setInspectionSelectedDate(inspection)"
                     class = "noTextDecoration assingatag collapse{{$index}} col-md-9">
                <div class="panel-heading_assignModules panelHeading_assignModules collapseZoneDiv{{$index}}" 
                     role="tab" 
                     id="headingOne_assignModules"
                     >
                  <h4 class="panel-title heading_inspection aModulesHeading">  
                      <span class="floatLeft inspectorSpan colorWhite marginRight5">
                           <i class = "icon-check inpectionDetailCheckIcon pointer" ng-show = "module.isActive" ng-click = "accessChange(module)" 
                              ng-class = "{disableClick:module.moduleName == 'RequestDetails'}"></i>               
                           <i class = "icon-unchecked inpectionDetailUnCheckIcon pointer" ng-show = "!module.isActive" ng-click = "accessChange(module)" 
                              ng-class = "{disableClick:module.moduleName == 'RequestDetails'}"></i>
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

                      <span class="floatLeft inspectionDateSpan colorWhite" >{{module.moduleName}}</span> 
                      <span class="floatLeft smallHint assignAccessModule" ng-show = " module.moduleName == ''">Assign access to all Modules</span> 
                      <span class="floatLeft marginLeft10 smallHint" ng-show = " module.moduleName == 'Admin'">(Assign access to all Modules)</span>
<!--                      </a>       -->
                    </h4></div>
                    </a>
                     
                      <div ng-show="module.moduleName != ''" id = "ischangeId{{module.moduleId}}" class = "col-md-3 applyChangesbutton tabletdCenter disabled pointer" ng-click = "applychanges(module, employee, $index)" ng-class = "{disableAssignButton: module.moduleName == 'Admin' && module.isActive == false, enableAssignButton:module.moduleName == 'Admin' && module.isActive == true}"><span class = "floatRight spanChangeAccess">Apply changes</span></div>

                      <div ng-show="module.moduleName == ''" id = "ischangeId{{module.moduleId}}" class = "col-md-3 applyChangesbutton tabletdCenter disabled pointer" ng-click = "applychanges(module, employee, $index)" ng-class = "{disableAssignButton: module.moduleName == '' && module.isActive == false, enableAssignButton:module.moduleName == '' && module.isActive == true}"><span class = "floatRight spanChangeAccess">Apply changes</span></div>

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
        </form>
    </div>
    
         
<div class="modal-footer modalFooter">   
<!--  <button type="button" class="btn btn-black buttonRadius0" ng-click = "changeAccessRights(modulesArrayData)">Change Access Rights</button> -->
    <button type="button" class="btn btn-black buttonRadius0" ng-click = "cancel()">Close</button> 
</div>
    
</div>
<!--ng-disabled="addUserForm.$invalid"-->