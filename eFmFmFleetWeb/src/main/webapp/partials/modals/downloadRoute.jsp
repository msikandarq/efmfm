<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    MODAL TEMPLATES

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
-->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class = "downloadFileModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-pencil edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Download Route</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>      
<div class="modal-body modalMainContent modalMainContentDownload">     
  <form name = "downloadFileForm" class = 'createNewZone'> 

      <div class = 'col-md-8'>
          <label>Please select a file from the dropdown to download</label><br>
            <select ng-model="download.fileList" 
                   class="form-control select_assignCab floatLeft" 
                   ng-options="reportList.reportName for reportList in reportList track by reportList.reportName"
                   ng-change=downloadSelectedFile(download.fileList) >
                 <option value="">-- Select File Name --</option>
          </select>
      </div>
      <div class = 'col-md-4 hidden'>
          <a id = 'reportName' ng-href="downloadRoutes.do?fileName={{download.fileList.reportName}}">{{download.fileList.reportName}}</a></div>

  </form>   
</div>
     <div class="modal-footer modalFooter createNewZone_modalFooter">  
         <button type="button" class="btn btn-close floatRight noMoreClick" ng-click = "cancel()">Cancel</button>  
         
     </div>
</div>




