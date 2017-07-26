<div class = "row padding20">
    <form name = "travelDistRemarksForm">
<!--        <div class = "col-md-12"><span>Your have changed the Travelled Distance to <strong>{{newValue}}</strong> </span></div><br/>-->
    <div class = "col-md-12"><span><strong>Please enter remarks</strong></span></div>
    <div class = "col-md-12">
        <textarea class = 'form-control' 
                  ng-model = "remarks" 
                  placeholder = "Remarks" 
                  required>
        </textarea>
    </div>
    <div class = "col-md-12">
        <input type = "button" class = "btn btn-success marginTop10" 
               value = "Save" ng-click = "save(remarks)" 
               ng-disabled="travelDistRemarksForm.$invalid"
               />
        <input type = "button" class = "btn btn-default marginTop10" 
               value = "Close" ng-click = "cancel()" 
               ng-disabled="travelDistRemarksForm.$invalid"
               placeholder = "Remarks"
               />
    </div>
    </form>
</div>