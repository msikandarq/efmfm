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
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "createNewRouteModalTravelDistance container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12 bgOrg"> 
            <div class = "row">
                <div class = "icon-pencil edsModal-icon col-md-1 floatLeft clrblk"></div>
                <div class = "modalHeading col-md-8 floatLeft clrblk">Edit Travel Distance</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>      
<div class="modal-body modalMainContentTravelDistance">     
  <form name = "travelDistanceForm" class = 'travelDistanceForm'>  
     <div class = "col-md-6 col-xs-12"> 
           <div>
           <label>Edit Type</label>
              <select ng-model="travelDistance"
                      class="form-control"
                      ng-options="travelDistance.text for travelDistance in travelDistanceEditType"
                      required
                      ng-change="settravelDistanceEditType(travelDistance)"
                      >
                            <option value="">-- Select Edit Type --</option>
                          </select>
           </div>
       </div>  
       
      <div class = "col-md-6 col-xs-12" ng-show="travelDistance.text == 'Addition'"> 
           <div>
               <label>Add Km</label>                    
               <input type="text" 
                      class="form-control" 
                      is-number-only-valid
                      ng-minlength="1"
                      ng-maxlength="3"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="3" 
                      placeholder="Enter Km" 
                      name="" 
                      ng-model="addKm"
                      ng-required="addKmRequired">
           </div>
       </div> 

       <div class = "col-md-6 col-xs-12" ng-show="travelDistance.text == 'Subtraction'"> 
           <div>
               <label>Subtraction Km</label>                    
               <input type="text" 
                      class="form-control" 
                      is-number-only-valid
                      ng-minlength="1"
                      ng-maxlength="3"
                      oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                      maxlength="3" 
                      placeholder="Enter Km" 
                      name="" 
                      ng-model="subtractionKm"
                      ng-required="subtractionKmRequired">
           </div>
       </div>

       <div class = "col-md-12 col-xs-12" ng-show="travelDistance.text == 'Addition' || travelDistance.text == 'Subtraction'"> 
                  <div class="form-group">
                  <label for="remarks">Enter Remarks</label>
                  <textarea class="form-control" 
                            rows="2" 
                            id="remarks"
                            ng-minlength="1"
                            ng-maxlength="100"
                            placeholder="Enter Remarks" 
                            oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                            maxlength="100" 
                            expect-special-char
                            ng-model="remarks"
                            required></textarea>
                  </div>
       </div> 

  </form>   
</div>
     <div class="modal-footer modalFooter createNewZone_modalFooter">  
            <div class="btn-group">
            <button type="button" 
                    class="btn btn-success btn-sm" 
                    ng-show="travelDistance.text != 'Addition' && travelDistance.text != 'Subtraction'"
                    ng-disabled ='true'
                    ng-click="addDistanceKm(addKm)">Add Km</button>
            <button type="button" 
                    class="btn btn-success btn-sm" 
                    ng-show="travelDistance.text == 'Addition'"
                    ng-click="addOrSubtractDistanceKm('add',addKm,remarks)"
                    ng-disabled="travelDistanceForm.$invalid">Add Km</button>
            <button type="button"
                    class="btn btn-success btn-sm" 
                    ng-show="travelDistance.text == 'Subtraction'" 
                    ng-click="addOrSubtractDistanceKm('subtraction',subtractionKm,remarks)"
                    ng-disabled="travelDistanceForm.$invalid">Subtraction Km</button>
            <button type="button" 
                    class="btn btn-warning btn-sm"
                    ng-click = "cancel()">Cancel</button>        
            </div>

     </div>
</div>