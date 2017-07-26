<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/18/2015  Saima Aziz      Initial Creation
05/18/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "addClientLocationMapTemplate">
	
<!--
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Add New Client</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
-->
    <div class="modal-body modalMainContent">
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
                         expect_special_char>
             </div>
           </div> 
           <div class = "col-md-6 col-xs-12 form-group"> 
              <label>Address</label>
                   <textarea type="text" 
                      ng-model="user.address"
                      class="form-control" 
                      id = "newAddress"
                      placeholder = "Address"
                      name = 'address'
                      readonly
                      required> </textarea>            
                <span class = "hintModal"></span>  
           </div> 
           <div class = "col-md-2 col-xs-12 form-group">
               <label></label>
               <button type="button" class="btn btn-success buttonRadius0 saveLocationbutton_empDetail" 
                       ng-click = "setPickDropLocation(user)"
                       ng-disabled="chooseLocationForm.$invalid">Save Location</button> 
               
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
        <div class = "col-md-12 map_viewMap"  ng-if = "mapIsLoaded">
            <efmfm-new-user-map-search-location id = "mapDiv_admin" center = 'loc' user = 'user'></efmfm-new-user-map-search-location>            
        </div>
       </form>
      </div> 
     
</div>