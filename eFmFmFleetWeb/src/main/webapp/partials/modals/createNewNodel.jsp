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
<div class = "addNewShiftModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft" ng-if = "isCreateNodal">Add New Nodal</div>
            <div class = "modalHeading col-md-10 floatLeft" ng-if = "isCreateNodalPoint">Add New Nodal Point</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "addNewShift" name = "addNewNode">
         <div class = "userInfo_adminForm row">
                <div class ='col-md-6'>
                    <label>Route Name</label><br>
                    <input type = "text" 
                           class = "form-control" 
                           placeholder="Route Name" 
                           maxlength = "50"
                           ng-maxlength = "50"  
                           ng-model = "user.routeName"
                           expect_special_char
                           required
                           ng-readonly = "isCreateNodalPoint"
                           >
                </div>
             
                <div class ='col-md-6'>
                    <label class = 'floatLeft'>Area Name</label>
                   <select ng-model="user.areaName"
                           class="form-control" 
                           ng-options="allZoneData.areaName for allZoneData in allZonesData track by allZoneData.areaId"
                           ng-change = "setAreaSelected(user.areaName)"
                           required>
                     <option value="">Select Area Name</option>
                    </select>
                </div>
                <div class ='col-md-6'>
                    <label class = 'floatLeft'>Nodal Point Name</label>
                    <input type = "text" 
                           placeholder = "Enter Nodal Point Name" 
                           class = "form-control"
                           maxlength = "50"
                           ng-maxlength = "50"
                           ng-model = "user.nodelName"
                           ng-disabled = "!areaSelected"
                           expect_special_char
                           required>
                </div>
<!--
                <div class ='col-md-6'>
                    <label class = 'floatLeft'>Shift Time</label>
                    <timepicker ng-model="user.createNewAdHocTime" 
                              hour-step="hstep" 
                              minute-step="mstep" 
                              show-meridian="ismeridian" 
                              readonly-input = 'true'
                              class = "timepicker2_empReq floatLeft">
                    </timepicker>
                </div>
-->
             
             <div id = "nodelMap" class = "col-md-12">
               <fieldset class = 'fieldSetTravelDesk'>
                   <Legend class = 'legendNewNodal'>Select New Nodal Point</Legend>
                   <form class = "chooseLocationForm" name = "chooseLocationForm">         
                       <div class = "col-md-4 form-group">
                         <label>Search</label>
                         <div class = "input-group calendarInput"> 
                               <span class="input-group-btn">
                                   <button class="btn btn-default" 
                                           ng-click="geoCode(search)" >
                                   <i class = "icon-search mapMarkerIcon"></i></button></span> 
                              <input ng-model="user.search"
                                     id = "location"
                                     type = "text" 
                                     class="form-control" 
                                     placeholder = "Location"
                                     maxlength = "50"
                                     ng-maxlength = "50"
                                     expect_special_char>
                         </div>
                       </div> 
                       <div class = "col-md-6 col-xs-12 form-group"  > 
                          <label>Geocode Address</label>
                               <textarea type="text" 
                                  class="form-control" 
                                  ng-model="user.address"
                                  id = "newAddress"
                                  placeholder = "Address"
                                  required
                                  readonly
                                  > </textarea>            
                            <span class = "hintModal"></span>  
                       </div> 
                       
                       <div class = "col-md-6 col-xs-12 form-group hidden"> 
                          <label>Cordinates</label>
                               <input type="text" 
                                  ng-model="user.cords"
                                  class="form-control"  
                                  id = "latlangInput"
                                  placeholder = "Co-ordinates"
                                  name = 'cords'
                                  required
                                  readonly>                  
                            <span class = "hintModal"></span>  
                       </div>  
                    <div class = "col-md-12 map_viewMap map_viewMapEditEmployee"  ng-if = "mapIsLoaded">
                        <efmfm-new-user-map-search-location id = "mapDiv_admin" center = 'loc' user = 'user'></efmfm-new-user-map-search-location>            
                    </div>
               </div>
           </form> 
    </fieldset>
                
             </div>
<!--
                <div class ='col-md-4'>
                    <input type = 'button' class = 'btn btn-success' value = 'Save' ng-click = 'addNewShift(newShift)'>
                </div>
-->
         </div>  
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
  <button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
            ng-click = "addNodelPoint(user)" 
            ng-disabled="addNewNode.$invalid">Add Nodal Point</button> 
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>
<!--ng-disabled="addUserForm.$invalid"-->