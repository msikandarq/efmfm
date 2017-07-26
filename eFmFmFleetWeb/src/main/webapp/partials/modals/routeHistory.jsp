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

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "routeHistoryFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "col-md-2"></div>
            <div class = "modalHeading col-md-10 floatLeft">
                <span class = "floatLeft">Route History</span>
               </div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
        <div class = "row">
            <div class = "col-md-12">
                <input type= "text" class = "floatRight" placeholder = "Filter" ng-model = "route">
            </div>
        </div>
        <div class = "routeInfoTripSheet row marginRow">
            <div class = "col-md-8">
                <div class = "row routemainInfo">
                    <div class = "col-md-6">
                        <span><strong>Trip Id</strong> </span><br/><span>{{routeId}}</span>                        
                    </div>
                    <div class = "col-md-6">
                        <span><strong>Assign Date</strong> </span><br/><span>{{tripAssignDate}}</span>                        
                    </div>
                    <div class = "col-md-6">
                        <span><strong>Start Date</strong> </span><br/><span>{{tripStartDate}}</span>                        
                    </div>
                    <div class = "col-md-6">
                        <span><strong>End Date</strong> </span><br/><span>{{tripCompleteDate}}</span>                        
                    </div>
                </div>
            </div>            
            <div class = "col-md-2">                
                <button class = "btn btn-sm btn-success form-control floatRight" 
                        ng-click = "saveInExcel()">
                <i class = "icon-download-alt"></i><span class = "marginLeft5">Excel</span></button>
            </div>

            <div class = "col-md-2">                
                <button class = "btn btn-sm btn-primary form-control floatRight" 
                        ng-click = "routeHistoryDetails(routeId,tripAssignDate,tripStartDate,tripCompleteDate)">
                <i class = ""></i><span class = "marginLeft5">History Map</span></button>
            </div>
            
        </div>
        
        <div id = "exportRouteHistory" class = "row marginRow">                                   
         <table class = "reportTable table reportTable_km table_RouteHistory table-responsive container-fluid" ng-if = "gotRouteHistory" fixed-header>
            <thead class ="tableHeading" >                
                <tr>
                  <th>Cab Location</th>
                  <th>Cab ETA</th>
                  <th>Travelled Time</th>
                </tr>
            </thead>
            <tbody>
               <tr ng-repeat="route in routeHistoryData | filter:route">
                   <td class = "col-md-6">{{route.cabLocation}}</td>
                   <td class = "col-md-3 tabletdCenter">{{route.cabEta}}</td>
                   <td class = "col-md-3 tabletdCenter">{{route.cabTravelledTime}}</td> 
               </tr> 
             </tbody>                                
        </table> 
        </div> 
    </div>        
    </div>      
<div class="modal-footer modalFooter">    
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>