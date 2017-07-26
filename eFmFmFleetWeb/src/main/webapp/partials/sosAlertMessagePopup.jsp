<div class = "row alert_SOSPopup" ng-controller = "mainCtrl">
    <div class="col-md-12 customSOSAlert" role="alert">
        <div class = 'row'> 
            <div class = 'col-md-12 alertSOSHeader'>
<!--                <span class = "floatRight pointer" ng-click = "closeAlertMessage()">x</span>-->
            </div>
            <div class ='col-md-12 alertSOSBody'>
                <div class = " row">
                    <div class = "col-md-4">
                        <blink data-speed="200" ng-click="stop()">
                            <i class = "icon icon-bell-alt sosAlertPopBell"></i>
                        </blink>
                    </div>
                    <div class = "col-md-8" class = "sosAlertMsg">
                        <span><strong>Alert Header</strong></span><br />
                        <span>Vehicle number TXN23232 in accident</span>
                    </div>
                </div>
            </div>
            <div class = 'col-md-12 alertFooter'>
<!--                <input type = 'button' class = 'btn btn-success alertButtonsOK' value = 'OK' ng-click = 'closeAlertMessage()'>-->
            </div>
        </div>
    </div>
</div>   