<!-- 
@date           11/01/2015
@Author         Saima Aziz
@Description    Custom Alert Popups for all Main pages
@State          
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
11/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<div class = "row alert_TravelDesk" id = 'showAlerteFmFm'>
    <div class="col-md-12 alert alert-warning customAlert" role="alert">
        <div class = 'row'> 
            <div class = 'col-md-12 alertHeader'>
                <span class = "floatRight pointer" ng-click = "closeAlertMessage()">x</span>
            </div>
            <div class ='col-md-12 alertBody'>
                <span class = 'floatLeft'>{{alertMessage}}</span><br>
                <span class = "hint floatLeft">{{alertHint}}</span>
            </div>
            <div class = 'col-md-12 alertFooter'>
                <input type = 'button' class = 'btn btn-success alertButtonsOK' value = 'OK' ng-click = 'closeAlertMessage()'>
            </div>
        </div>
    </div>
</div>   