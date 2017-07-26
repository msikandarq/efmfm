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

<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'"></div><div class="loading"></div>
<div class = "importEmployeeModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-calendar edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-8 floatLeft">Import Vendor Master Data</div>
                <div class = "col-md-2 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>    
            </div>
        </div>        
    </div>  
<div class="modal-body modalMainContent">    
 <form class='importEmpForm' id="addinstgroup" action="services/xlUtilityVendorUpload/vendorRecord"
					method="post" enctype="multipart/form-data"
					class="form-horizontal form-bordered"
					name="myForm">
 <div class="fileinput fileinput-new input-group " data-provides="fileinput">
        <div class="form-control  col-md-6 col-xs-6" data-trigger="fileinput">
            <span class="fileinput-filename"></span>
        </div>
        <span class="input-group-addon btn btn-default btn-file">
            <span class="fileinput-new">Select file</span>
            <span class="fileinput-exists"></span>
            <input type="file" id = "filenameforactivity" name="filename" class="default"
            ng-model="filename" valid-file required
     accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
        </span>
     </div>
     <div class="modal-footer modalFooter">  
           <button type="button" class="btn btn-info floatLeft" id='imp_stud' ng-click = "importEmpFile()" ng-disabled="!myForm.$valid">Submit</button>
           <button type="button" class="btn btn-close floatLeft" ng-click = "cancel()">Close</button>
     </div>
    </form>
</div>
</div>