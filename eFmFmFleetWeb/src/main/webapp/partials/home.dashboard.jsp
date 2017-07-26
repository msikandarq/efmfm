<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    Main Landing Page after User Logins. The Dashboard is always visible to the ADMIN and hidden from all the other users until the access is granted by the    ADMIN. 
@State          home.dashboard
@URL            /dashboard   

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->
<div class = "dashboardTemplate container-fluid" ng-if = "isDashboardActive">
      <div class="row" ng-show="multiFacility == 'Y'" style="background-color: rgba(255,255,255,.15);
                        margin-top: -20px;
                        margin-bottom: 2px;
                        border-radius: 7px 4px 21px 18px;">
                        <div class = "col-md-9" style="color: white;
                        font-size: 16px;
                        position: relative;
                        top: 6px;
                        right: -2px;">
          Welcome <span class="font-weight: bold !important;">{{userName}} User</span>
          </div>

          <div class = "col-md-3">
            <am-multiselect class="input-lg dynamicDaysInput facilityDropdown"
                                   multiple="true"
                                   ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                   ng-model="facilityData"
                                   ms-header="All Facility"
                                   options="facility.branchId as facility.name for facility in facilityDetails"
                                   change="setFacilityDetails(facilityData)"
                                   
                                   ">
                            </am-multiselect>
                            <span class="btn btn-success" ng-click="submitFacilityDetails(facilityData)"><i class="icon-ok-sign"></i> Submit</span>
                            <!-- <input type="button" class="btn btn-primary" value="Submit" name=""> -->
          </div>
    </div>
    <div class = "row firstRow_dashboard">
        <div class = "col-md-3">
            <div class = "mainSOSBottom firstRowCountersBottom">
                <div class = "row">
                    <div class = "col-xs-9">
                    </div>
                    <div class = "col-xs-3">  
                    </div>
                </div>
            </div>


            <div class = "mainSOSTop firstRowCountersTop">
                <div class = "row">
                    <div class = "col-xs-3">
                         <i class="icon icon-bell-alt"></i>
                    </div>
                    <div class = "col-xs-9 marginNeg10">                        
                        <div class = "counter text-right"><span ng-bind = "dashboardData.sosAlerts"></span></div>
                        <div class="counterLabel text-right">SOS Alerts</div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class = "col-md-3">
            <div class = "mainEDSBottom firstRowCountersBottom">
                <div class = "row">
                    <div class = "col-xs-9">
                    </div>
                    <div class = "col-xs-3">                  
                    </div>
                </div>
            </div>
            <div class = "mainEDSTop firstRowCountersTop">
                <div class = "row">
                    <div class = "col-xs-3">
                         <i class="icon icon-user"></i>
                    </div>
                    <div class = "col-xs-9 marginNeg10">                        
                        <div class = "counter text-right"><span ng-bind = "dashboardData.dropInProgress"></span></div>
                        <div class="counterLabel text-right">Total Drop In Progress</div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class = "col-md-3">
            <div class = "mainEPSBottom firstRowCountersBottom">
                <div class = "row">
                    <div class = "col-xs-9">
                    </div>
                    <div class = "col-xs-3">   
                    </div>
                </div>
            </div>
            <div class = "mainEPSTop firstRowCountersTop">
                <div class = "row">
                    <div class = "col-xs-3">
                         <i class="icon icon-user"></i>
                    </div>
                    <div class = "col-xs-9 marginNeg10">                        
                        <div class = "counter text-right"><span ng-bind = "dashboardData.pickUpInProgress"></span></div>
                        <div class="counterLabel text-right">Total Pickup In Progress</div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class = "col-md-3">
            <div class = "mainVTBottom firstRowCountersBottom">
                <div class = "row">
                    <div class = "col-xs-9">
                    </div>
                    <div class = "col-xs-3">          
                    </div>
                </div>
            </div>
            <div class = "mainVTTop firstRowCountersTop">
                <div class = "row">
                    <div class = "col-xs-3">
                         <i class="icon icon-truck"></i>
                    </div>
                    <div class = "col-xs-9 marginNeg10">                        
                        <div class = "counter text-right"><span ng-bind = "dashboardData.vehiclesInTrip"></span></div>
                        <div class="counterLabel text-right">Total Vehicles On Road</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

   
<!--    FIRST ROW ENDED -->
    <div class="clearfix"></div>
    
<!--    SECOND ROW STARTS-->
    <div class = "row secondRow_dashboard">
        
        <div class = "col-md-4" id = "sosMainDiv">
            <div class = "header">
                <div class = "row">
                    <div class = "col-sm-8  hidden-xs">
                        <i class="icon-warning-sign"></i><span>SOS Alert(s)-Status</span>
                    </div>
                    <div class = "col-xs-6 .visible-xs-* hidden-lg hidden-md hidden-sm pointer">
                        <i class="icon-warning-sign"></i><span data-toggle="tooltip" data-placement="top" title="SOS Alert(s)-Status">SOS Alert(s)</span>
                    </div>
                    <div class = "col-sm-4 col-xs-6">
                         <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "sosMainDiv"
                                      target-div = "sosAlertsDiv">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "sosMainDiv"
                                      target-div = "sosAlertsDiv"
                                      ng-click = 'refreshDashboard()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "sosMainDiv"
                                      target-div = "sosAlertsDiv">
                        </efmfm-button>
                    </div>
                </div>
            </div>
            <div class = "content sosAlertsDiv" ng-controller = "sosModalCtrl">
              <div class = "content2">
                <ul>
                    <li class = "sosItem" ng-repeat = "sosItem in sosList">
                      <div class = "row rowBackground" ng-click="openSOSModel($index, 'lg')">
                      <div class = "col-xs-1 itemIcon">
                                <div class = "row">
                                    <div class="col-xs-12 empDropIcon label-warning" ng-class = "sosItem.icon">
                                    </div>
                                </div>
                        </div>
                        <div class = "col-xs-4 itemLabel">{{sosItem.label}}</div>
                        <div class = "col-xs-5 progressBar"><div class = "progressHolder"><progressbar class = "progress" value="sosItem.progressBar" type="warning"></progressbar></div></div>
                        <div class = "col-xs-2 itemCounter">
                            <div class = "row">
                                <div class = "col-xs-12 sosCounter label-warning floatLeft">{{sosItem.counter}}</div>
                            </div>
                            </div>
                      </div>
                    </li>
                </ul>
              </div>
            </div>
        </div>
        
        <div class = "col-md-4" id = "edsMainDiv">
            <div class = "header">
                <div class = "row">
                    <div class = "col-sm-8  hidden-xs">
                        <i class="icon icon-user"></i><span>Drop-Status</span>
                    </div>
                    <div class = "col-xs-6 .visible-xs-* hidden-lg hidden-md hidden-sm pointer">
                        <i class="icon icon-user"></i><span data-toggle="tooltip" data-placement="top" title="Employee(s) Drop-Status">Employee(s) Drop</span>
                    </div>
                    <div class = "col-sm-4 col-xs-6">
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "edsMainDiv"
                                      target-div = "empDropContent">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "edsMainDiv"
                                      target-div = "empDropContent"
                                      ng-click = 'refreshDashboard()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "edsMainDiv"
                                      target-div = "empDropContent">
                        </efmfm-button>
                    </div>
                </div>
            </div>
            <div class = "content empDropContent"  ng-controller = "edsModalCtrl">
<!--                <div class = "row content2">-->
                <div class = "content2">
                <ul>
                    <li class = "sosItem" ng-repeat = "emplyeeDrop in employeesDropList">
                        <div class ="row rowBackground" ng-click="openEDSModel($index, 'lg')">
                            <div class = "col-xs-1 itemIcon">
                                <div class = "row">
                                    <div class="col-xs-12 empDropIcon label-success" ng-class = "emplyeeDrop.icon">
                                    </div>
                                </div>
                            </div>
                            <div class = "col-xs-7 itemLabel">{{emplyeeDrop.label}}</div>
                            <div class = "col-xs-2"></div>
                            <div class = "col-xs-2 itemCounter">
                                <div class = "row">
                                    <div class = "col-xs-12 sosCounter label-success floatLeft">{{emplyeeDrop.counter}}</div>
                                </div>
                            </div>
                        </div>                        
                    </li>
                </ul>
                </div>
            </div>
        </div>
        
<!--  This Div is remove on Client Request      -->
        <div class = "col-md-4" id = "epsMainDiv">
            <div class = "header">
                <div class = "row">
                    <div class = "col-sm-8  hidden-xs">
                        <i class="icon icon-user"></i><span>Pickup Status</span>
                    </div>
                    <div class = "col-xs-6 .visible-xs-* hidden-lg hidden-md hidden-sm pointer">
                        <i class="icon icon-user"></i><span data-toggle="tooltip" data-placement="top" title="Employee(s) Pick-Up-Status">Employee(s) Pick-Up</span>
                    </div>
                    <div class = "col-sm-4 col-xs-6">
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "epsMainDiv"
                                      target-div = "empPickupDiv">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "epsMainDiv"
                                      target-div = "empPickupDiv"
                                      ng-click = 'refreshDashboard()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "epsMainDiv"
                                      target-div = "empPickupDiv">
                        </efmfm-button>
                    </div>
                </div>
            </div>
            <div class = "content empPickupDiv" ng-controller = "epsModalCtrl">
              <div class = "content2">
                <ul>
                    <li class = "sosItem" ng-repeat = "emplyeePickup in employeesPickupList">
                        <div class ="row rowBackground" ng-click="openEPSModel($index, 'lg')">
                            <div class = "col-xs-1 itemIcon">
                                <div class = "row">
                                    <div class="col-xs-12 empDropIcon label-success-eps" ng-class = "emplyeePickup.icon">
                                    </div>
                                </div>
                            </div>
                            <div class = "col-xs-7 itemLabel">{{emplyeePickup.label}}</div>
                            <div class = "col-xs-2"></div>
                            <div class = "col-xs-2 itemCounter">
                                <div class = "row">
                                    <div class = "col-xs-12 sosCounter label-success-eps floatLeft">{{emplyeePickup.counter}}</div>
                                </div>
                            </div>
                        </div>                        
                    </li>
                </ul>
              </div>
            </div>
        </div>
    </div>
<!--    SECOND ROW ENDED -->
    <div class="clearfix"></div>
<!--    THIRD ROW STARTS--> 
    
    
    
    <div class = "row thirdRow_dashboard">
        
        <div class = "col-md-4" id = "vehStatMainDiv">
            <div class = "header">
                <div class = "row">
                    <div class = "col-sm-8  hidden-xs">
                        <i class="icon icon-user"></i><span>Guest Request-Status</span>
                    </div>
                    <div class = "col-xs-6 .visible-xs-* hidden-lg hidden-md hidden-sm pointer">
                        <i class="icon-truck"></i><span data-toggle="tooltip" data-placement="top" title="SOS Alert(s)-Status">Vehicle(s)-Status</span>
                    </div>
                    <div class = "col-sm-4 col-xs-6">
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "vehStatMainDiv"
                                      target-div = "vehicleStatusDiv">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "vehStatMainDiv"
                                      target-div = "vehicleStatusDiv"
                                      ng-click = 'refreshDashboard()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "vehStatMainDiv"
                                      target-div = "vehicleStatusDiv">
                        </efmfm-button>
                    </div>
                </div>
            </div>
            <div class = "content vehicleStatusDiv" ng-controller = "gsModalCtrl">
             <div class = "content2">
               <ul>
                    <li class = "sosItem" ng-repeat = "guestReportStatus in guestReportStatusList">
                        <div class ="row rowBackground" ng-click="openGSModal($index, 'lg')">
                            <div class = "col-xs-1 itemIcon">
                                <div class = "row">
                                    <div class="col-xs-12 empDropIcon label-success-vs" ng-class = "guestReportStatus.icon">
                                    </div>
                                </div>
                            </div>
                            <div class = "col-xs-7 itemLabel">{{guestReportStatus.label}}</div>
                            <div class = "col-xs-2"></div>
                            <div class = "col-xs-2 itemCounter">
                                <div class = "row">
                                    <div class = "col-xs-12 sosCounter label-success-vs floatLeft">{{guestReportStatus.counter}}</div>
                                </div>
                            </div>
                        </div>                        
                    </li>
                </ul>
              </div>
            </div>
        </div>

        
        <div class = "col-md-4" id = "govDriverMainDiv">
            <div class = "header">
                <div class = "row">
                    <div class = "col-sm-8  hidden-xs">
                        <i class="icon-truck"></i><span>Drivers Governance Notification</span> <!-- <span class = "excelExportDashboard pointer" ng-click = "exportExcel('gd')"><i class = "icon-download-alt"></i></span> -->
                    </div>
                    <div class = "col-xs-6 .visible-xs-* hidden-lg hidden-md hidden-sm pointer">
                        <i class="icon-truck"></i><span data-toggle="tooltip" data-placement="top" title="Employee(s) Drop-Status">Governance Driver(s)</span>
                    </div>
                    <div class = "col-sm-4 col-xs-6">
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "govDriverMainDiv"
                                      target-div = "govDriverDiv">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "govDriverMainDiv"
                                      target-div = "govDriverDiv"
                                      ng-click = 'refreshDashboard()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "govDriverMainDiv"
                                      target-div = "govDriverDiv">
                        </efmfm-button>
                    </div>
                </div>
            </div>
            <div class = "content govDriverDiv" ng-controller = "gdModalCtrl">
              <div class = "content2">
                 <ul>
                    <li class = "sosItem" ng-repeat = "govStatus in govStatusList">
                        <div class ="row rowBackground" ng-click="openGDModel($index, 'lg')">
                            <div class = "col-xs-1 itemIcon">
                                <div class = "row">
                                    <div class="col-xs-12 empDropIcon label-success-gd" ng-class = "govStatus.icon">
                                    </div>
                                </div>
                            </div>
                            <div class = "col-xs-7 itemLabel">{{govStatus.label}}</div>
                            <div class = "col-xs-2"></div>
                            <div class = "col-xs-2 itemCounter">
                                <div class = "row">
                                    <div class = "col-xs-12 sosCounter label-success-gd floatLeft">{{govStatus.counter}}</div>
                                </div>
                            </div>
                        </div>                        
                    </li>
                </ul>
              </div>
            </div>
        </div>
        <div class = "col-md-4" id = "govVehMainDiv">
            <div class = "header">
                <div class = "row">
                    <div class = "col-sm-8  hidden-xs">
                        <i class="icon-truck"></i><span>Vehicles Governance Notification</span>
                        <!-- <span class = "excelExportDashboard pointer" ng-click = "exportExcel('gv')"><i class = "icon-download-alt"></i></span> -->
                    </div>
                    <div class = "col-xs-6 .visible-xs-* hidden-lg hidden-md hidden-sm pointer">
                        <i class="icon-truck"></i><span data-toggle="tooltip" data-placement="top" title="Employee(s) Pick-Up-Status">Governance Vehicle(s)</span>
                    </div>
                    <div class = "col-sm-4 col-xs-6">
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "govVehMainDiv"
                                      target-div = "govVehicleDiv">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_dashboarButtons"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "govVehMainDiv"
                                      target-div = "govVehicleDiv"
                                      ng-click = 'refreshDashboard()'>
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "govVehMainDiv"
                                      target-div = "govVehicleDiv">
                        </efmfm-button>
                    </div>
                </div>
            </div>
            <div class = "content govVehicleDiv" ng-controller = "gvModalCtrl">
              <div class = "content2">
                <ul>
                    <li class = "sosItem" ng-repeat = "govVehicle in govVehicleList">
                        <div class ="row rowBackground" ng-click="openGVModel($index, 'lg')">
                            <div class = "col-xs-1 itemIcon">
                                <div class = "row">
                                    <div class="col-xs-12 empDropIcon label-success-gv" ng-class = "govVehicle.icon">
                                    </div>
                                </div>
                            </div>
                            <div class = "col-xs-7 itemLabel">{{govVehicle.label}}</div>
                            <div class = "col-xs-2"></div>
                            <div class = "col-xs-2 itemCounter">
                                <div class = "row">
                                    <div class = "col-xs-12 sosCounter label-success-gv floatLeft">{{govVehicle.counter}}</div>
                                </div>
                            </div>
                        </div>                        
                    </li>
                </ul>
              </div>
            </div>
        </div>
    </div>
</div>