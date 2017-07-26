<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div class = "addNewShiftModalTemplate pointer">
  
  <div class = "row ">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Print Preview </div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    <div class="containar">
    <div id="printableWithImage" ng-show="employeeNameLength == 1"> 
    <div class="modal-body">
        <div class="row">
            <img ng-src="{{templateSrc}}" style=" height: 1280px; ">
            <h1 id="textEmployeeName" 
                class="ng-binding" 
                ng-repeat = "employeeNames in routePrintModals"
                style="position: absolute;
                      bottom: 760px;
                      top: 360px;
                      font-size: 100px;
                      max-width: 100%;
                      max-height: 100%;
                      margin-left: 130px;
                      margin-right: 130px;
                      text-align: center;">
                    {{employeeNames}}</h1>
        </div>
    <div style="page-break-after:always;">
    </div>
    </div>
  </div>

   <div id="printableWithOutImage" ng-show="employeeNameLength >= 2"> 
    <div class="modal-body">
        <div class="row">
          <div class="col-md-12">
                <img ng-src="{{logoSrc}}" align="right" style="width: 50%; height: 50%"><br/>
          </div>
            
            <div class="col-md-12">
           </br>
           </br>
           </br>
           </br>
           </br>

           </br>
           </br>
           </br>
           </br>
           </br>
              <h1 id="textEmployeeName" 
                class="ng-binding " 
                style="text-align: center; font-size: 90px;" 
                ng-repeat = "employeeNames in routePrintModals"
                >
                    {{employeeNames}}</h1>
            </div>
            
        </div>
    <div style="page-break-after:always;">
    </div>
    </div>
  </div>

<div class="modal-footer"> 
  <button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
            data-ng-click="printDiv('printableWithImage')" ng-show="employeeNameLength == 1"
            >Print</button> 
  <button type="button" class="btn btn-success buttonRadius0 noMoreClick" 
  data-ng-click="printDiv('printableWithOutImage')" ng-show="employeeNameLength >= 2"
  >Print</button> 
    <button type="button" class="btn btn-default buttonRadius0 noMoreClick" ng-click = "close()">Close</button>    
</div>
     
</div>
<!--ng-disabled="addUserForm.$invalid"-->