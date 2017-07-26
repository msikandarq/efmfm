<!-- 
@date           05/31/2016
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/31/2016  Saima Aziz      Initial Creation
05/31/2016  Saima Aziz      Final Creation
-->

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "customMessageTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Customize the Text Messages</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body addNewShiftTimeModal modalMainContent">
       <form class = "addNewShift" name = "customMessage">
         <div class = "userInfo_adminForm row">
             <div class ='col-md-8 col-md-offset-2 overflowAuto infoMessage'>
                 <div class = "row marginRow mainRowCustomMsg">
                     <div class = "col-md-4 col-xs-4 messageHeader">Dynamic Values</div>
                     <div class = "col-md-4 col-xs-4 messageHeader">Markers</div>
                     <div class = "col-md-4 col-xs-4 messageHeader">Example</div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Vehicle Number</div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">[vehicleNumber]
                        <i class = "icon-lightbulb" 
                           ng-class = "{msgLightBulb:!isVehicleNumber, openLightBulb:isVehicleNumber}"
                           ng-click = "isVehicleNumber = !isVehicleNumber"></i>
                     </div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Ex: TXN234009</div>
                     <div class = "col-md-12 messageInfoRowsExample" 
                          ng-show = "isVehicleNumber"><span class ="msgHint">Example : Vehicle Number [vehicleNumber] needs assistant</span>
                     </div>
                     
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Vehicle Type</div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">[vehicleType]
                        <i class = "icon-lightbulb"
                           ng-class = "{msgLightBulb:!isVehicleType, openLightBulb:isVehicleType}"
                          ng-click = "isVehicleType = !isVehicleType"></i>
                     </div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Ex: Inova</div>
                     <div class = "col-md-12 messageInfoRowsExample" 
                          ng-show = "isVehicleType"><span class ="msgHint">Example : Vehicle Type [vehicleType] has been...</span>
                     </div>
                     
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Trip Type</div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">[tripType]
                        <i class = "icon-lightbulb"
                           ng-class = "{msgLightBulb:!isTripType, openLightBulb:isTripType}"
                           ng-click = "isTripType = !isTripType"></i>
                     </div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Ex: Pickup or Drop</div>
                     <div class = "col-md-12 messageInfoRowsExample" 
                          ng-show = "isTripType"><span class ="msgHint">Example : Trip Type : [tripType] has been added for ...</span>
                     </div>
                     
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Shift Time
                     </div>
                     <div class = "col-md-4 col-xs-4 messageInfoRows">[shiftTime]
                        <i class = "icon-lightbulb"
                           ng-class = "{msgLightBulb:!isShiftTime, openLightBulb:isShiftTime}"
                           ng-click = "isShiftTime = !isShiftTime"></i>
                     </div> 
                     <div class = "col-md-4 col-xs-4 messageInfoRows">Ex: 12:30 (hh:mm)</div>
                     <div class = "col-md-12 messageInfoRowsExample" 
                          ng-show = "isShiftTime"><span class ="msgHint">Example : Your new [tripType] is [shiftTime]</span>
                     </div>                   
                         
                </div>
<!--
                     <div class = "col-md-12"><span>For dynamic <mark>Vehicle Number value of </mark> use <strong>[vehicleNumber]</strong></span><br />
                         <span class ="msgHint">Example : Vehicle Number [vehicleNumber] needs assistant</span>
                         <span>For dynamic value of <mark>Vehicle Type</mark> use <strong>[vehicleType]</strong></span><br />
                         <span class ="msgHint">Example : Vehicle Type [vehicleType] has been...</span>
                     </div>
                     <div class = "col-md-4">
                         <span>For dynamic value of <mark>Trip Type</mark> use <strong>[tripType]</strong></span><br />
                         <span class ="msgHint">Example : Trip Type : [tripType] has been added for ...</span>
                     </div>
                     <div class = "col-md-4">
                         <span>For dynamic value of <mark>Shift Time Type</mark> use <strong>[shiftTime]</strong></span><br />
                         <span class ="msgHint">Example : You new [tripType] is [shiftTime].</span>
                     </div>
-->
             </div>
                <div class ='col-md-6 col-xs-6 messageInfoRows'>
                    <label>Allocation Message Text</label>
                    <input type = "button" class = "btn btn-xs btn-success floatRight" value = "Save" ng-click = "save('Allocation Message Text', message.allocationMsg)" ng-disabled = "!message.allocationMsg">
                    <br>
                    <textarea type="text" 
                      ng-model="message.allocationMsg"
                      class="form-control tesxtAreaMsg" 
                      name = "allocationMsg"
                      placeholder = "Allocation Message Text"></textarea>
                </div>
                <div class ='col-md-6 col-xs-6 messageInfoRows'>
                    <label>ETA Message</label>
                    <input type = "button" class = "btn btn-xs btn-success floatRight" value = "Save" ng-click = "save('ETA Message', message.etaMsg)"  ng-disabled = "!message.etaMsg">
                    <br>
                    <textarea type="text" 
                      ng-model="message.etaMsg"
                      class="form-control tesxtAreaMsg" 
                      placeholder = "ETA Message"></textarea>
                </div>
                <div class ='col-md-6 col-xs-6 messageInfoRows'>
                    <label>Arrived Message</label>
                    <input type = "button" class = "btn btn-xs btn-success floatRight" value = "Save" ng-click = "save('Arrived Message', message.arrivedMsg)" ng-disabled = "!message.arrivedMsg">
                    <br>
                    <textarea type="text" 
                      ng-model="message.arrivedMsg"
                      class="form-control tesxtAreaMsg" 
                      placeholder = "Arrived Message"></textarea>
                </div>
                <div class ='col-md-6 col-xs-6 messageInfoRows'>
                    <label>Delay Message</label>
                    <input type = "button" class = "btn btn-xs btn-success floatRight" value = "Save" ng-click = "save('Delay Message', message.delayMsg)" ng-disabled = "!message.delayMsg">
                    <br>
                    <textarea type="text" 
                      ng-model="message.delayMsg"
                      class="form-control tesxtAreaMsg" 
                      placeholder = "Delay Message"></textarea>
                </div>
                <div class ='col-md-6 col-xs-6 messageInfoRows'>
                    <label>No-show Message</label>
                    <input type = "button" class = "btn btn-xs btn-success floatRight" value = "Save" ng-click = "save('No-show Message', message.noShowMsg)" ng-disabled = "!message.noShowMsg">
                    <br>
                    <textarea type="text" 
                      ng-model="message.noShowMsg"
                      class="form-control tesxtAreaMsg" 
                      placeholder = "No-show Messagee"></textarea>
                </div>
                <div class ='col-md-6 col-xs-6 messageInfoRows'>
                    <label>Cab Change Message</label>
                    <input type = "button" class = "btn btn-xs btn-success floatRight" value = "Save" ng-click = "save('Cab Change Message', message.cabChangeMsg)" ng-disabled = "!message.cabChangeMsg">
                    <br>
                    <textarea type="text" 
                      ng-model="message.cabChangeMsg"
                      class="form-control tesxtAreaMsg" 
                      placeholder = "Cab Change Message"></textarea>
                </div>
         </div>  
       </form>
    </div>      
<div class="modal-footer modalFooter"> 
<!--
	<button type="button" class="btn btn-info buttonRadius0 noMoreClick" 
            ng-click = "saveCustomMsg(message)" 
            ng-disabled="customMessage.$invalid">Save Custom Messages</button> 
-->
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>