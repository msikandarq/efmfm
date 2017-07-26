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
<div class = "newEscortFormModalTemplate">  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Nodal GEO Location</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div>  
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <div class = 'col-md-12 editEmployeeMap'>
               <fieldset class = 'fieldSetTravelDesk'>
                   <Legend class = 'editEmployeeDetailLegend'>Update Employee Geocode</Legend>
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
                                 ng-maxlength="30"
                                 maxlength="30"
                                 expect_special_char
                                 class="form-control" 
                                 placeholder = "Location"
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
                              readonly>                  
                        <span class = "hintModal"></span>  
                   </div>  
                 
                   <div class = "col-md-12 map_viewMap map_viewMapEditEmployee"  ng-if = "mapIsLoaded">
                    <efmfm-new-user-map-search-location id = "mapDiv_admin" center = 'loc' user = 'user'></efmfm-new-user-map-search-location>            
                   </div>
        </form>
    </fieldset>
    </div> 
    </div> 
</form>  
</div>      
</div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "updateNodalPointMapDetails(user,update,loc)" ng-disabled="editEmployeeDetail.$invalid">Update</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>