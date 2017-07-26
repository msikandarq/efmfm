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
<div class = "createNewPostFormModalTemplate">
	
	<div class = "row">
        <div class="modal-header modal-header1 col-md-12">
           <div class = "row"> 
            <div class = "icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
            <div class = "modalHeading col-md-10 floatLeft">Create New Post</div>
            <div class = "col-md-1 floatRight pointer">
                <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
            </div> 
           </div> 
        </div>        
    </div>
    <div class="modal-body modalMainContent">
    <div class = "formWrapper">
       <form class = "createNewAlertForm" name = "createNewAlertForm">           
           <div class = "col-md-6 col-xs-12 form-group">
               <label for = "alertType">Alert Type</label>
               <select ng-model="alert.alertType" 
                       class= "selectAlertType form-control"
                       ng-options="alertType.text for alertType in alertTypes track by alertType.value"
                       ng-change = "setAlertType(alert.alertType)"
                       name = "alertType"
                       required
                       ng-class = "{error: alertPostForm.alertType.$invalid && !alertPostForm.alertType.$pristine}">
                   <option value="">--Select Alert Type --</option>    							         
               </select>
           </div>
             
           <div class = "col-md-6 col-xs-12 form-group">
               <label for = "title">Alert Title</label>
               <input type="text" 
                      ng-model="alert.title" 
                      class="form-control" 
                      placeholder = "Alert Title"
                      name = 'alertTitle'
                      required
                      ng-class = "{error: createNewAlertForm.alertTitle.$invalid && !createNewAlertForm.alertTitle.$pristine}">
           </div>
       
           <div class = "col-md-12 col-xs-12 form-group">
                <label for = "desc">Alert Description</label>
                <textarea class="form-control" 
                          placeholder = "Description" 
                          ng-model = "alert.description"
                          required
                          name = "description"
                          ng-class = "{error: createNewAlertForm.description.$invalid && !createNewAlertForm.description.$pristine}">
               </textarea>
           </div>             
       </form>   
      </div>        
    </div>      
<div class="modal-footer modalFooter">    
	<button type="button" class="btn btn-primary" ng-click = "create(alert)" ng-disabled="createNewAlertForm.$invalid">Post Alert</button> 
    <button type="button" class="btn btn-default" ng-click = "cancel()">Cancel</button>    
</div>
     
</div>