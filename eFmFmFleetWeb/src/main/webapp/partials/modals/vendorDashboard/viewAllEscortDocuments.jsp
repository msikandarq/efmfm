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
<div class = "editVehicleFormModalTemplate">
  
  <div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">View Escort Documents</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>

<div class="modal-body modalMainContent">   
  <table class="table">
    <thead>
      <tr>
        <th>S No</th>
        <th>Creation Date</th>
        <th>Document Name</th>
        <th>Documents</th>
      </tr>
    </thead>
    <tbody>
   
      <tr ng-repeat = "doc in pathNameData"> 
        <td>{{$index +1}}</td>
        <td>{{doc.creationDate}}</td>
        <td>{{doc.documentName}}</td>
        <td><div class = "floatLeft imageDiv_approval imageBorder" id = "div{{$index}}" 
                             >

                           <a href="{{doc.pathName}}" class = "noLinkLine" download>
                                <img class="img-responsive img-rounded fileImg" 
                                      ng-src = "{{doc.pathName}}">

                            </a>
                        </div></td>


      </tr>
    </tbody>
  </table>
</div>

<div class="modal-footer modalFooter">    
  <!-- <button type="button" class="btn btn-warning" ng-click = "updateVehicle(editVehicle)" ng-disabled="editVehicleForm.$invalid ">Save</button>  -->
  <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
</div>