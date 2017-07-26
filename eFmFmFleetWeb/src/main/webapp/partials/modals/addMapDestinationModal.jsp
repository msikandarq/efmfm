<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "newEscortFormModalTemplate">  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add Geo Location Name</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form name = "addGeoLocationForm" >          
           <div class = 'col-md-12 editEmployeeMap'>
               <form class = "chooseLocationForm" name = "chooseLocationForm">         
                   <div class = "col-md-6 form-group">
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
                                 ng-minlength="0"
                              ng-maxlength="50"
                              maxlength="50"
                              expect_special_char
                                 >
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
                              required
                              > </textarea>            
                        <span class = "hintModal"></span>  
                   </div> 
                   <div class = "col-md-6 col-xs-12 form-group"> 
                      <label>Location Name</label>
                           <input type="text" 
                              ng-model="user.areaName"
                              class="form-control"  
                              id = "areaName"
                              placeholder = "Enter Area Name"
                              ng-minlength="0"
                              ng-maxlength="50"
                              maxlength="50"
                              expect_special_char
                              required
                              >                  
                        <span class = "hintModal"></span>  
                   </div>  
                   <!-- <div class = "col-md-6 col-xs-12 form-group"> 
                      <label>Cordinates</label>
                           <input type="text" 
                              ng-model="user.cords"
                              class="form-control"  
                              id = "latlangInput"
                              placeholder = "Co-ordinates"
                              name = 'cords'
                              readonly >                  
                        <span class = "hintModal"></span>  
                   </div>   -->
                 
                   <div class = "col-md-12 map_viewMap map_viewMapEditEmployee"  ng-if = "mapIsLoaded">
                    <efmfm-new-user-map-search-location id = "mapDiv_admin" center = 'loc' user = 'user'></efmfm-new-user-map-search-location>            
                   </div>
        </form>
    <!-- </fieldset> -->
    </div> 
</form>


</div> 


</div>      
<div class="modal-footer modalFooter">    
  <button type="button" class="btn btn-success buttonRadius0" ng-click = "updateDestination(user,update,loc)" ng-disabled="addGeoLocationForm.$invalid">Save Location</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>