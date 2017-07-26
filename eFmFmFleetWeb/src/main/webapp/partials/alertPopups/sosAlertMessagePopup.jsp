<!-- 
@date           03/01/2016
@Author         Saima Aziz
@Description    Custom Alert Popups for SOS Alert. This popups check the backend after every 10 secs
@State          
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->
<div class = "row alert_SOSPopup">
    <div class="col-md-12 customSOSAlert" role="alert">
        <div class = 'row'> 
            <div class = 'col-md-12 col-xs-12 alertSOSHeader'>
<!--                <span class = "floatRight pointer" ng-click = "closeAlertMessage()">x</span>-->
            </div>
            <div class ='col-md-12 alertSOSBody'>
                <div class = " row">
                    <div class = "col-md-4 col-xs-4">
                        <blink data-speed="200" ng-click="stop()">
                            <i class = "icon icon-bell-alt sosAlertPopBell"></i>
                        </blink>
                    </div>
                    <div class = "col-md-8 col-xs-8" class = "sosAlertMsg">
                        <span><strong>{{alertTitle}}</strong></span><br />
                        <span>{{alertDescription}}</span>
                    </div>
                </div>
            </div>
            <div class = 'col-md-12 col-xs-12 alertFooter'>
<!--                <input type = 'button' class = 'btn btn-success alertButtonsOK' value = 'OK' ng-click = 'closeAlertMessage()'>-->
            </div>
        </div>
    </div>
</div>   