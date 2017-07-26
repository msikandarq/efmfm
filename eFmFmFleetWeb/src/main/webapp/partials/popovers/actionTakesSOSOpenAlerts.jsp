<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    POPUP Templates - Called from DashBoard's sosOpenAlerts Modal

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<div ng-show = 'showActionDiv'  class = "sosOpenAlertActionModal container-fluid">    
    <div class="col-md-12 alert alert-warning customAlert" role="alert">
        <div class = 'row'> 
            <div class = 'col-md-12 sosOpenAlertModalHeader'>
                <span class = 'floatLeft'>{{selectedAlert_AlertId}}<sapn> | </sapn>{{selectedAlert_AlertType}}<sapn> | </sapn>{{selectedAlert_Title}}</span>
                <span class = "floatRight pointer" ng-click = "cancelOpenAlertModal()">x</span>
            </div>
            <div class ='row sosOpenAlertBody'>                
                <div class = 'col-md-4 marginTop15'><strong>Driver Name</strong></br>{{selectedAlert_DriverName}}</div>              
                <div class = 'col-md-4 marginTop15'><strong>Driver Number</strong></br>{{selectedAlert_DriverNumber}}</div>              
                <div class = 'col-md-4 marginTop15'><strong>Vehicle Number</strong></br>{{selectedAlert_vehicleNumber}}</div>
                <div class = 'col-md-12 marginTop15'>
                    <input type = 'text' class = 'hidden' ng-model = 'action.alertId'>
                    <input type = 'text' class = 'hidden' ng-model = 'action.facilityName'>
                    <input type = 'text' class = 'hidden' ng-model = 'action.facilityId'>
                    <input type = 'text' class = 'hidden' ng-model = 'action.assignRouteId'>
                      <textarea ng-model="action.text"
                             type = "text" 
                             class="form-control" 
                             placeholder = "Enter Action Taken"
                             rows="5"
                             maxLength = '300'
                             required>
                    </textarea>
                </div>
            </div>
            <div class = 'col-md-12 alertFooter'>
                <input type = 'button' 
                       class = 'btn btn-danger floatRight buttonRadius0 marginTop10 marginLeft10' 
                       value = 'Close Alert' 
                       ng-click = 'closeAlert(action)'
                        ng-disabled="!action.text">
                <button type = 'button' class = 'btn btn-primary floatRight buttonRadius0 marginTop10 marginLeft10' 
                        ng-click = 'updateAlert(action)'
                        ng-disabled="!action.text">
                    <span>
                        <i class = "icon-ok-circle iconSOSCircle searchServiceMappingIcon"></i>
                    </span> 
                    <span>Update Alert</span>
                </button>
            </div>
        </div>
    
    
</div>
    </div>