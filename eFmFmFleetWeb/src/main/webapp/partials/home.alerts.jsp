
<div class = "alertsTemplate container-fluid">
    <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1">            
            <span class = "col-md-12 col-xs-12">Broadcast</span> 
                      
            <div class = "col-md-12 col-xs-12 mainTabDiv_alerts">
          <!--   /*START OF WRAPPER1 = DRIVER*/ -->
               <div class = "wrapper1" id="alertFormContent">
                <div class = "heading2 row">
                    <span class = "col-md-7 floatLeft" id = "driver">Post Broadcast</span>
                    <div class= "col-md-5 floatRight">
                    	<efmfm-button img-class = "efmfm_approvalButtons_collapse"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "alertFormContent"
                                      target-div = "alertForm">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_approvalButtons_reload"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "alertFormContent"
                                      target-div = "alertForm">
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "alertFormContent"
                                      target-div = "alertForm">
                        </efmfm-button>
                    </div>
                </div>
                    <form class = "alertForm" name = "alertPostForm">
                        <div class = "row">
                            <div class = "col-md-8">
                                <div class = "row postAlertDiv">
                                    <div class = "col-md-4">
<!--                                        <input type = "text" class="form-control" placeholder = "Customer Type" ng-model = "custName">-->
                                        <select ng-model="newalert.custType" 
                                                class= "selectCustType"
                						        ng-options="selectCustomer.text for selectCustomer in selectCustomers track by selectCustomer.value"
                						        ng-change = "setCustType(newalert.custType)"
                                                name = "custType"
                                                required
                                                ng-class = "{error: alertPostForm.custType.$invalid && !alertPostForm.custType.$pristine}">
      						  		        <option value="">-- Select Recipients Type --</option>
    							         </select>
                                    </div>
                                    <div class = "col-md-4">
                                        <input type="text" 
                      ng-model="alert.title" 
                      class="form-control" 
                      placeholder = "Broadcast Msg Title"
                      name = 'title'
                      required
                      ng-class = "{error: alertPostForm.title.$invalid && !alertPostForm.title.$pristine}">
                      
                                        
                                         <!-- <select ng-model="newalert.alertType" 
                                                class= "selectAlertType"
                						        ng-options="selectAlert.text for selectAlert in selectAlerts track by selectAlert.value"
                						        ng-change = "setAlertType(newalert.alertType)"
                                                name = "alertType"
                                                required
                                               ng-class = "{error: alertPostForm.alertType.$invalid && !alertPostForm.alertType.$pristine}">
      						  		        <option value="">--Select Alert Title --</option>
    							         </select> -->
                                    </div>
                                    <div class = "col-md-4">
<!--                                        <input type = "text" class="form-control" placeholder = "Select Zone" ng-model = "selectZone">-->
                                        <select ng-model="newalert.selectZone" 
                                                class= "selectAlertType"
                						        ng-options="selectZone.text for selectZone in selectZones track by selectZone.value"
                						        ng-change = "setZone(newalert.selectZone)"
                                                name = "zoneType"
                                                required
                                               ng-class = "{error: alertPostForm.zoneType.$invalid && !alertPostForm.zoneType.$pristine}">
      						  		        <option value="">-- Select Zone --</option>
    							         </select>
                                    </div>
<!--
                                    <div class = "col-md-6">
                                        <input type = "text" class="form-control" placeholder = "Alert Title" ng-model = "alertTitle">
                                    </div>
-->
                                    <div class = "col-md-12 inValidMessagePostDiv">
                                        <div class="alert alert-danger" role="alert">
                                        <div><span><strong>Error</strong> : Incorrect Date entered.</span>
                                             <span class = "hint">[Hint: From Date is greater than To Date]</span>
                                        </div></div>
                                    </div>
                                    <div class = "clearfix"></div>
                                    <div class = "col-md-6">
                                        <div class = "input-group calendarInput">
                                          <span class="input-group-btn">
                                           <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                              <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                         <input class="form-control" 
                                               placeholder = "From Date" 
                                               ng-model = "newalert.fromDate"
                                               readonly
                                               class="form-control" 
                                               placeholder = "From Date"
                                               datepicker-popup = '{{format}}'
                                               min-date = "setMinDate"
                                               is-open="datePicker.fromDate" 
                                               show-button-bar = false
                                               show-weeks=false
                                               datepicker-options = 'dateOptions'
                                               name = "fromDate"
                                               required
                                               ng-class = "{error: alertPostForm.fromDate.$invalid && !alertPostForm.fromDate.$pristine}">
                                        </div>
                                    </div>
                                    <div class = "col-md-6">
                                        <div class = "input-group calendarInput"> 
                                          <span class="input-group-btn">
                                           <button class="btn btn-default" ng-click="openToDateCal($event)">
                                              <i class = "icon-calendar calInputIcon"></i></button></span>  
                                        <input class="form-control" 
                                               placeholder = "To Date" 
                                               ng-model = "newalert.toDate"
                                               class="form-control endDate" 
                                               ng-change = "checkValidityPost(newalert.fromDate,newalert.toDate)"
                                               placeholder = "To Date"
                                               datepicker-popup = '{{format}}'
                                               min-date = "setMinDate"
                                               is-open="datePicker.toDate" 
                                               show-button-bar = false
                                               show-weeks=false
                                               datepicker-options = 'dateOptions'
                                               name = "endDate"
                                               required
                                               ng-class = "{error: alertPostForm.endDate.$invalid && !alertPostForm.endDate.$pristine}">
                                        </div>
                                    </div>
                                    
                                    <div class = "col-md-12">
                                        <textarea class="form-control"
                                        
                                                  placeholder = "Description" 
                                                  ng-model = "newalert.description"
                                                  required
                                                  name = "description"
                                                  ng-class = "{error: alertPostForm.description.$invalid && !alertPostForm.description.$pristine}"></textarea>

                                    </div>
                                </div>
                            </div>
                            <div class = "col-md-3">
                                <!-- <button type="button" class = "btn btn-primary createNewAlert" ng-click="createNewAlerts()">
                                    <i class = "icon-plus"></i>Create New Alert
                                </button> -->
                                <input type="button" class = "btn btn-success" value = "Post Broadcast" ng-click="postAlerts(newalert)" ng-disabled="alertPostForm.$invalid">
<!--                                <input type="button" class = "btn btn-default" value = "Cancel Post" ng-click = "cancelPost()">-->
                            </div>
                        </div>
                    </form>
                
                
                </div>
                <div class="clearfix"></div>
                <br>
              <!--    /*END OF WRAPPER1 = DRIVER*/
                 /*START OF WRAPPER2 = VEHICLE*/ -->
                <div class = "wrapper1" id = "alertContent">
                <div class = "heading2 row">
                    <span class = "col-md-7 floatLeft" id = "map_viewMap">Posted Broadcast List</span>
                    <div class= "col-md-5 floatRight">
                    	<efmfm-button img-class = "efmfm_approvalButtons_collapse"
                                      src-url = "images/portlet-collapse-icon"
                                      selected-url = "images/portlet-expand-icon"
                                      hover-url = "images/portlet-collapse-icon"
                                      alt-text = "Collapse Window"
                                      main-div = "alertContent"
                                      target-div = "alertTable">
                        </efmfm-button> 
                        <efmfm-button img-class = "efmfm_approvalButtons_reload"
                                      src-url = "images/portlet-reload-icon"
                                      selected-url = "images/portlet-reload-icon"
                                      hover-url = "images/portlet-reload-icon"
                                      alt-text = "Reload Window"
                                      main-div = "alertContent"
                                      target-div = "alertTable">
                        </efmfm-button>
                        <efmfm-button img-class = "efmfm_dashboarButtons_remove"
                                      src-url = "images/portlet-remove-icon"
                                      selected-url = "images/portlet-remove-icon"
                                      hover-url = "images/portlet-remove-icon"
                                      alt-text = "Remove Window"
                                      main-div = "alertContent"
                                      target-div = "alertTable">
                        </efmfm-button>
                    </div>
                </div>
                <div class = "alertTable">
                    <div class = "row">
                        <div class = "col-md-12 inValidMessageDiv">
                            <div class="alert alert-danger" role="alert">
                                <div><span><strong>Error</strong> : Incorrect Date entered.</span>
                                     <span class = "hint">[Hint: From Date is greater than To Date]</span>
                                </div></div></div>
                        <div class = "clearfix"></div>
                        <div class = "col-md-1 col-xs-12"><span>Search:</span></div>
                        <div class = "col-md-3 col-xs-12">
                          <div class = "input-group calendarInput">  
                          <input class="form-control floatLeft" 
                                 placeholder = "From" 
                                 ng-model = "searchFromDate"
                                 ng-change = "searchPost(searchFromDate,searchtoDate)"
                                 class="form-control"
                                 datepicker-popup = '{{format}}'
                                 is-open="datePicker.searchFromDate" 
                                 show-button-bar = false
                                 show-weeks=false
                                 datepicker-options = 'dateOptions'>
                               <span class="input-group-btn">
                                <button class="btn btn-default" ng-click="openSearchFromDateCal($event)">
                                <i class = "icon-calendar calInputIcon"></i></button></span>
                            </div>                            
                        </div>
<!--
                        <div class = "col-md-1 col-xs-12">
                          to 
                        </div>
-->
                        <div class = "col-md-3 col-xs-12">
                         <div class = "input-group calendarInput">  
                          <input class="form-control floatLeft" 
                                 placeholder = "To" 
                                 ng-model = "searchtoDate"
                                 ng-change = "searchPost(searchFromDate,searchtoDate)"
                                 class="form-control" 
                                 datepicker-popup = '{{format}}'
                                 is-open="datePicker.searchToDate" 
                                 show-button-bar = false
                                 show-weeks=false
                                 datepicker-options = 'dateOptions'>
                              <span class="input-group-btn">
                               <button class="btn btn-default" ng-click="openSearchToDateCal($event)">
                                <i class = "icon-calendar calInputIcon"></i></button></span>
                          </div> 
                            
                        </div>
                    </div>
                    <div class = "alertTableDiv">
                        <table class = "postTable table table-hover table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>From Date</th>
                                      <th>To Date</th>
                                      <th>Title</th>
                                      <th>Description</th>
                                      <th>Zone</th>
                                      <th>UserType</th>
                                      <th>
                                      </th>
                                    </tr> 
                                </thead>
                                <tbody>
                                    <tr ng-repeat = "alert in alertData.postalerts" class = "alertNum{{alert.postAlertId}}">
<!--                                    <tr ng-repeat = "alert in alertData.postalerts | alertSearch:searchFromDate:searchtoDate">-->
                                      <td class = "col-md-1">{{alert.startDate|date:'shortDate'}}</td>		                                        
		                              <td class = "col-md-1">{{alert.endDate|date:'shortDate'}}</td>
		                              <td class = "col-md-1">{{alert.tittle}}</td>
		                              <td class = "col-md-5" ng-show = "!alert.isEdit">{{alert.description}}</td>
                                      <td class = "col-md-5" ng-show = "alert.isEdit">
                                            <textarea class="form-control" 
                                                  ng-model = "alert.description"></textarea>
                                      </td>
		                              <td class = "col-md-1">{{alert.zoneName}}</td>
		                              <td class = "col-md-1">{{alert.alertsType}}</td>
		                              
                                      <td class = "col-md-2">
                                          <input type = "button" 
                                                 class = "col-md-5 col-xs-12 btn btn-warning btn-xs" 
                                                 value = "Edit"
                                                 ng-click = "editAlert(alert, $index)"
                                                 ng-show = "!alert.isEdit">
                                          <input type = "button" 
                                                 class = "col-md-5 col-xs-12 btn btn-success btn-xs" 
                                                 value = "Save"
                                                 ng-click = "saveAlert(alert, $index)"
                                                 ng-show = "alert.isEdit">
                                          <input type = "button" 
                                                 class = "col-md-5 col-xs-12 btn btn-danger btn-xs" 
                                                 value = "Cancel"
                                                 ng-click = "cancelAlert(alert)">
                                      </td>		                                                                  
		                            </tr>                                    
		                         </tbody>
                            </table>
                    </div>
                    </div>
               
                </div>             
            </div>
            
            
        </div>
        
    </div> 
   
    
</div>