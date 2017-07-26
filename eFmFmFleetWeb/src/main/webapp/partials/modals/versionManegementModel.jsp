
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "newEscortFormModalTemplate"> 
<i class = "icon-remove-sign pointer versionControlCross" ng-click="cancel()"></i> 
  <div class="text-center">
    <img src="images/efmfm-logo.png" class="img-about" alt="Responsive image">
    </div>
    <div class="modal-body modalMainContent">
      <div class = "formWrapper">
       <div class="text-center versionControlFontSize">
        <p class="versionControlFontWeight">Efmfm {{versionInfo.versionNo}}</p>
        <p class="versionControlFontWeight">Release Notes</p>
        <p>{{versionInfo.releaseNotes}}</p>
        <p>Copyright @ NGFV <a href="http://www.efmfm.com/#/home">www.efmfm.com</a></p>
        <p>@2017 efmfm Chennai | Bangalore | Hyderabad | New Delhi | Pune.</p>
            </div>
      </div>
</div>      
     
</div>