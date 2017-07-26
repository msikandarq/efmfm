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
<div class = "addVendorFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Edit Facility Wise</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form class = "addFacilityForm" name = "addFacilityForm">
            <div class="row">
              <div class="col-md-6">
                    <span><strong>Select Base Facility</strong></span>
              </div>
              <div class="col-md-6">
                    <span><strong>Sub Facility</strong></span>
              </div>
                <div class="col-md-6">
                    <input type="text" class="form-control" ng-model="baseFacility" readonly="true" name="">
                </div>
                <div class="col-md-6" ng-click = "getBaseFacilityDetails(compineFacility)">
                    <multi-select-dropdown-span-six 
                    input-model="subFacilityDetails"
                    output-model="compineFacility" 
                    button-label="icon name"
                    item-label="icon name maker"
                    tick-property="ticked"
                    on-item-click = "checkCompineFacility(compineFacility,subFacilityDetails)"
                    required
            >
            </multi-select-dropdown-span-six>
                </div>
            </div>
            <div>
                  <table class="table table-bordered">
                      <thead>
                          <tr>
                              <th>Sub Facility <span ng-show="compineFacility.length > 0"></span></th>
                              <th>Remove</th>
                          </tr>
                      </thead>
                  <tbody ng-show="compineFacility.length==0">
                      <tr>
                        <td colspan='12'>
                            <div class="noData">There are no Facility Seleted Yet</div>
                        </td>
                      </tr>
                      </tbody>
                      <tbody ng-show="compineFacility.length > 0">
                          <tr ng-repeat="item in compineFacility">
                              <td><strong>{{$index + 1}} ) </strong> {{item.name}}</td>
                              <td><div class="icon-remove-sign mapMarkerDelete pointer enableAddButton" ng-click="deleteCompineFacility(item,$index)"></div></td>
                          </tr>
                      </tbody>
                  </table>
            </div>
            
       </form>
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
     <button type="button" class="btn btn-success buttonRadius0" ng-click = "modifyFacilityDetails(baseFacility,compineFacility)" ng-disabled= "addFacilityForm.$invalid">Modify Facility</button> 
    <button type="button" class="btn btn-default buttonRadius0" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>