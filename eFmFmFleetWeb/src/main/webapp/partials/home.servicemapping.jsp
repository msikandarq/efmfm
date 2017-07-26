<!--
@date           04/01/2015
@Author         Saima Aziz
@Description    Page to make changes or move employees'routes, zones, shift time, close the route, Start the trip, Edit the Route, View specifc route on the map, delete employee, delete route, upload the routes and create new route etc
@State          home.servicemapping
@URL            /servicemapping

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<div class = "serviceMappingTemplate container-fluid" ng-if = "isCaballocationActive">
 <div class = "row">
   <!-- <div class = "col-md-12 col-xs-12 heading1"> -->
<div class = "col-md-12 col-xs-12 heading1 divSpanHeading_serviceMapping">Routing And Cab Allocation</div>
               <div class = "col-md-12 col-xs-12 divSpanHeading_serviceMapping text-right">               
               
               <span class = "nowrapText">Total Routes:<mark class = 'mark_headingServiceMapping'><strong>{{numberOfRoutes}} | </strong></mark></span>
                   <span><span class = "nowrapText">Total Employees: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{numberOfEmployee}} | </strong></mark></span>
                   <span><span class = "nowrapText">Total Zones: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{zoneData.length}} | </strong></mark></span>
                   <span><span class = "nowrapText">Escort Required: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{escortRequired}} | </strong></mark></span>                 
                    <span><span class = "nowrapText">Total Men: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{totalNumberMaleEmployees}} |</strong></mark></span>
                   <span><span class = "nowrapText">Total Women: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{totalNumberFemaleEmployees}} </strong></mark></span>
                   <span><span class = "nowrapText">Total OnBoard: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{onBoard}} </strong></mark></span> 
                    <span><span class = "nowrapText">Yet To Board: </span>
                   <mark class = 'mark_headingServiceMapping'><strong>{{yetToBoard}} </strong></mark></span>          
                            
           <span class = ""> 
                    <input type = "button"
                         class = "floatRight btn btn-info btn-xs"
                         value = "Priority Routing"
                         ng-click = "priorityTracking()"
                         ng-if = "vendorSummary">
                   <input type = "button"
                         class = "floatRight btn btn-primary btn-xs"
                         value = "Vendor Summary"
                         ng-click = "vendorSummaryModal()"
                         ng-if = "vendorSummary">
                  
                  </span>
           </div> 
             <!-- <div class = "col-md-12 col-xs-12 divSpanHeading_serviceMapping" ng-show="!serviceMappingZoneDiv"><span class = "floatRight"><mark class = 'mark_headingServiceMapping'><strong></strong></mark></span>
                   <span class = "floatRight">
                   <mark class = 'mark_headingServiceMapping'><strong></strong></mark> </span></div> -->

     <!-- </div>     -->

<div class="invoiceTemplate container-fluid">
  <div class="row">
    <div class="col-md-12 col-xs-12 invoiceHeading1">

      <div class="col-md-12 col-xs-12 mainTabDiv_invoice">
        <div class="wrapper1">

          <tabset class="tabset subTab_invoice">
              <tab ng-click="getServiceMapping('Normal')"> <tab-heading>Normal Route View</tab-heading>
                  <div ng-include="'partials/modals/serviceMapping/seviceMappingNormal.jsp'"></div>
              </tab>
              <tab ng-click="customRouteView()" ng-controller="customRouteViewCtrl"> <tab-heading>Custom Route View</tab-heading>
                  <div ng-include="'partials/modals/serviceMapping/customRouteView.jsp'"></div>
              </tab>
              <tab ng-click="bulkRouteCloseView()" ng-controller="bulkRouteCloseCtrl"> <tab-heading>Bulk Route Close</tab-heading>
                  <div ng-include="'partials/modals/serviceMapping/bulkRouteCloseView.jsp'"></div>
              </tab>
              <tab ng-click="getAllVendors()" ng-controller="allocateRouteToVendorCtrl"> <tab-heading>Allocate Route To Vendor</tab-heading>
                  <div ng-include="'partials/modals/serviceMapping/allocateRouteToVendor.jsp'"></div>
              </tab>
          </tabset>
        </div>
      </div>
    </div>
  </div>
</div>
    </div>
</div>
