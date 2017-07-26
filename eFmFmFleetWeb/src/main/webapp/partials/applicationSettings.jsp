<div class = "row">               
<form name = "appSettings">     
   <div class = "row">
        <div class = "col-md-2">
            Escort Needed
        </div>
        <div class = "col-md-9">

            <div class = "row escortNeeded">
                <label class = "radioLabel">
                    <input type="radio" 
                           ng-model="escortNeeded" 
                           value="None" 
                           ng-disabled = "isDisable"
                           ng-change = "setEscortNeeded(escortNeeded)">None</label><br>
                <label class = "radioLabel">
                    <input type="radio" 
                           ng-model="escortNeeded" 
                           value="Always" 
                           ng-disabled = "isDisable"
                           ng-change = "setEscortNeeded(escortNeeded)">Always</label><br>
                <label class = "radioLabel">
                    <input type="radio" 
                           ng-model="escortNeeded" 
                           value="femalepresent"
                           ng-disabled = "isDisable" 
                           ng-change = "setEscortNeeded(escortNeeded)">When Female Passenger Present</label><br>
                <label class = "radioLabel">
                    <input type="radio" 
                           ng-model="escortNeeded" 
                           value="firstlastfemale" 
                           ng-change = "setEscortNeeded(escortNeeded)">For Last Drop and First Pickup Female Passenger</label>

            </div>
        </div>
    </div>

   <div class = "row">
        <div class = "col-md-2">
            Manager Approval Needed
        </div>
        <div class = "col-md-9">

            <div class = "row escortNeeded">
                <label class = "radioLabel">
                    <input type="radio" 
                           ng-model="approvalNeeded" 
                           value="No" 
                           ng-change = "setapprovalNeeded(approvalNeeded)">No</label><br>
                <label class = "radioLabel">
                    <input type="radio" 
                           ng-model="approvalNeeded" 
                           value="Yes" 
                           ng-disabled = "isDisable"
                           ng-change = "setapprovalNeeded(approvalNeeded)">Yes</label>                                            
            </div>
        </div>
    </div>                       

    <div class = "row" ng-show = "isChangeLocation">
        <div class = "col-md-2">
            Update Location
        </div>
        <div class = "col-md-9">
            <div class = "row updateLocationDiv">
                <div class = "col-md-2 col-xs-12">
                    <span>Company Name</span>
                </div>
                <div class = "col-md-9 col-xs-12">
                    <span>Newt Global</span>
                </div>
                <div class = "col-md-2 col-xs-12">
                    <span>Company Address</span>
                </div>
                <div class = "col-md-9 col-xs-12">
                    <div ng-show = "!isEdit" class = "editedAddress floatLeft">{{currLocation}}</div>
                    <input  ng-show = "isEdit" 
                           type = "text" 
                           class = "editedAddress floatLeft" 
                           ng-model = "currLocation"> 
                    <input  ng-show = "isEdit" 
                           type = "text" 
                           class = "editedAddress hidden" 
                           ng-model = "currCords"> 
                    <span class = "floatLeft pointer"><i class ="icon-map-marker mapMarkerIcon"
                             tooltip="Click this to Find Location on the Map"
                             tooltip-placement="top"
                             tooltip-trigger="mouseenter"
                             ng-click = "openMap('lg')"></i></span>
                </div>

            </div>                                        
        </div>
    </div>  
</form>
</div>