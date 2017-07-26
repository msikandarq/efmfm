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

<div class = "row">
    <div class="col-md-12 alert alert-warning customAlert" role="alert">
        <div class = 'row'> 
            <div class = 'col-md-12 alertHeader'>
                <span class = "floatRight pointer" ng-click = "cancel()">x</span>
            </div>
            <div class ='col-md-12 alertBodyModal'>
                <span class = 'floatLeft'>{{alertMessage}}</span><br>
                <span class = "hint floatLeft">{{alertHint}}</span>
            </div>
            <div class = 'col-md-12 alertFooter'>
                <div class="row">
                    <div class = "col-md-5 col-xs-5">
                    </div>
                    <div class = "col-md-2 col-xs-2 padding0">                        
                        <input type = 'button' 
                               class = 'btn btn-success alertButtonsOKModal form-control' 
                               value = 'OK' ng-click = 'cancel()'>
                    </div>
                    <div class = "col-md-5 col-xs-5">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>   