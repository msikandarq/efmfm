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
<div class = "mapMyProfileModal container-fluid">
   <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-map-marker edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Find Your Location on the Map</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>  
  <div class="modal-body modalMainContent">  
      <div class = "row picknDropLocation_myProfileAdmin"> 
          <form name = "mapForm">
           <div class = "col-md-4 col-xs-12 form-group"> 
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
           <div class = "col-md-4 col-xs-12 form-group"> 
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
        <div class = "col-md-12 map_viewMap">
            <efmfm-new-user-map-location id = "mapDiv_admin"></efmfm-new-user-map-location>            
        </div></form>
      </div>    
    </div>
         
    <div class="modal-footer modalFooter">    
        <button type="button" class="btn btn-success" ng-show = "!onUserInfo" ng-click = "save(user)" ng-disabled="mapForm.$invalid">Save</button> 
        <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
    </div>
</div>

