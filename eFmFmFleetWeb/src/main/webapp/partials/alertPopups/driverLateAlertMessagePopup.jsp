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

<div class = "row alert_driveLatePopup">
    <div class="col-md-12 customSOSAlert paddingTop10" role="alert">
        <div class = 'row'> 
            <div class = 'col-md-12 alertSOSHeader'>
<!--                <span class = "floatRight pointer" ng-click = "closeAlertMessage()">x</span>-->
            </div>
            <div class ='col-md-12 alertDriverLateBody height100Perct'>
                <div class = " row height95Perct">
                    <div class = "col-md-2  col-xs-2 height80Perct">
                        <blink data-speed="200" ng-click="stop()">
                            <i class = "icon icon icon-user sosDriverPopBell"></i>
                        </blink>
                    </div>
                    <div class = "col-md-10 col-xs-8 height80Perct" class = "sosAlertMsg overflowAuto">
                        <span><strong>Driver Checkout</strong></span><br />
                        <span>System is going to checkout this driver</span>
                        <ul>
                            <li><span>Driver Name : </span><span>{{driverCheckInAlertData[0].driverName}}</span></li>
                            <li><span>Vehicle Number : </span><span>{{driverCheckInAlertData[0].vehicleNumber}}</span></li>
                        </ul>
                    </div>
                    
                    <div class = "col-md-12 col-xs-12 height20Perct" class = "sosAlertMsg">
                        <input type = "button" class = "btn btn-xs floatRight closeLateDriver" value = "OK" ng-click = "closeLateDriverPoopup()" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>   