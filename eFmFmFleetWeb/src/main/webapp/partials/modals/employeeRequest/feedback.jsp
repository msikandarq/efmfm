<tabset class="tabset subTab_empRequestDetail">
    <tab ng-click="getTodayRequestDetails()">
        <tab-heading>Feedback</tab-heading>
<div class = "empRequestDetailsTabContent row">
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.feedbackForm" class = "createNewRequestForm">
                                    <div class = "row createNewRequest">
                                     <div class = "col-md-6 form-group">
                                            <span> Feedback Type</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">
                                                <select ng-model="feedback.feedbackType" 
                                                        ng-options="feedbackT.text for feedbackT in feedbackTypes track by feedbackT.value"
                                                        class = "floatLeft selectNewTripTime form-control width100per"
                                                        ng-change="onloadDisableButton()" required>
                                                    <option value="">- Select Feedback -</option>
                                          </select>
                                               </label>
                                        </div>                                           
                                        </div>
                                        <div class = "col-md-6  form-group">
                                            <span> Shift Date </span>
                                            <div class = "input-group calendarInput" >
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "feedback.shiftDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "feedbackMaxDate"
                                                       max-date = "feedbackMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "shiftDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: feedbackForm.shiftDate.$invalid && !feedbackForm.shiftDate.$pristine}" required>                                                          </div>
                                        </div>
                                        


                                        </div>
                                        <div class = "row createNewRequest">
                                          <div class = "col-md-6 form-group">
                                            <span> Shift Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">
                                                <select ng-model="feedback.shiftTime" 
                                                        ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataReschedule track by tripTimes.shiftId"
                                                        class = "floatLeft selectNewTripTime form-control width100per"
                                                        ng-change="onloadDisableButton()" required>
                                                    <option value="">- Select Shift Time -</option>
                                          </select>
                                               </label>
                                        </div>                                           
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>  Trip Type</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">
                                                <select ng-model="feedback.tripType" 
                                                        ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                                        class = "floatLeft selectNewTripTime form-control width100per"
                                                        ng-change="onloadDisableButton()" required>
                                                    <option value="">- Select Trip Type -</option>
                                          </select>
                                               </label>
                                        </div>                                           
                                        </div>  
                                        </div> 
                                        <div class = "col-md-12 col-xs-12 form-group">
                                          <div ng-rate-it ng-model="feedback.rateit" step = "1" star-width="48" star-height="48" class="bigstar"></div>
                                        </div>
                                        <div class = "col-md-12 col-xs-12 form-group">
                                          <span>Comments</span><br>
                                                <textarea type="textarea" 
                                                       ng-model="feedback.comment" 
                                                       class="form-control" 
                                                       placeholder = "Type your comment here"
                                                       name = 'Description'
                                                       ng-class = "{error: editFeedbackDetail.Description.$invalid && !editFeedbackDetail.Description.$pristine}" required>
                                                       </textarea>
                                           <span class = "hintModal" ng-show="editFeedbackDetail.mobileNumber.$error.maxlength">Invalid Description </span>
                                       </div>
                                       
                                            <div class = "col-md-12 col-xs-12 form-group">
                                              <button type="button" 
                                                      class="btn btn-success btn-sm"
                                                      id = "createRequestSubmitButton"
                                                      ng-click = "createfeedback(feedback, 'feedback')"
                                                      ng-disabled = "form.feedbackForm.$invalid">Submit</button>
        
                                              </div>
                                        </div>
                                        
                                    </div>
                                </form>
                            </div>
                            <div class = "col-md-4"> </div>
            </div>
    </tab>
    <tab ng-click="getTodayRequestDetails()">
        <tab-heading>Complains</tab-heading>
<div class = "empRequestDetailsTabContent row">
                            <div class = "col-md-8 col-xs-12 formWrapper">
                                <form name = "form.complainForm" class = "createNewRequestForm">
                                    <div class = "row createNewRequest">
                                     <div class = "col-md-6 form-group">
                                            <span> Complaint Type</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">
                                                <select ng-model="complain.complainType" 
                                                        ng-options="complainT.text for complainT in complaintTypes track by complainT.value"
                                                        class = "floatLeft selectNewTripTime form-control width100per"
                                                        ng-change="onloadDisableButton()" required>
                                                    <option value="">- Select Feedback -</option>
                                          </select>
                                               </label>
                                        </div>                                           
                                        </div>
                                        <div class = "col-md-6  form-group">
                                            <span> Shift Date </span>
                                            <div class = "input-group calendarInput" >
                                                <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="openFromDateCal($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>                                         
                                                 <input class="form-control" 
                                                       ng-model = "complain.shiftDate"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "isFromDate(newRequest.fromDate)"
                                                       datepicker-popup = '{{format}}'
                                                       min-date = "feedbackMaxDate"
                                                       max-date = "feedbackMinDate"
                                                       is-open="datePicker.fromDate" 
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "shiftDate"
                                                       required
                                                       readonly
                                                       ng-class = "{error: complainForm.shiftDate.$invalid && !complainForm.shiftDate.$pristine}" required>                                                          </div>
                                        </div>
                                        


                                        </div>
                                        <div class = "row createNewRequest">
                                          <div class = "col-md-6 form-group">
                                            <span> Shift Time</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">
                                                <select ng-model="complain.shiftTime" 
                                                        ng-options="tripTimes.shiftTime for tripTimes in tripTimeDataReschedule track by tripTimes.shiftId"
                                                        class = "floatLeft selectNewTripTime form-control width100per"
                                                        ng-change="onloadDisableButton()" required>
                                                    <option value="">- Select Shift Time -</option>
                                          </select>
                                               </label>
                                        </div>                                           
                                        </div>
                                        <div class = "col-md-6 form-group">
                                            <span>  Trip Type</span><br>
                                            <div class = "row">
                                              <label class = "radioLabel floatLeft col-md-6 col-xs-12">
                                                <select ng-model="complain.tripType" 
                                                        ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                                        class = "floatLeft selectNewTripTime form-control width100per"
                                                        ng-change="onloadDisableButton()" required>
                                                    <option value="">- Select Trip Type -</option>
                                          </select>
                                               </label>
                                        </div>                                           
                                        </div>  
                                        </div> 
                                        <div class = "col-md-12 col-xs-12 form-group">
                                          <span>Comments</span><br>
                                                <textarea type="textarea" 
                                                       ng-model="complain.comment" 
                                                       class="form-control" 
                                                       placeholder = "Type your comment here"
                                                       name = 'Description'
                                                       ng-class = "{error: editFeedbackDetail.Description.$invalid && !editFeedbackDetail.Description.$pristine}" required>
                                                       </textarea>
                                       </div>
                                       
                                            <div class = "col-md-12 col-xs-12 form-group">
                                              <button type="button" 
                                                      class="btn btn-success btn-sm"
                                                      id = "createRequestSubmitButton"
                                                      ng-click = "createfeedback(complain, 'complain')"
                                                      ng-disabled = "form.complainForm.$invalid">Submit</button>
        
                                              </div>
                                        </div>
                                        
                                    </div>
                                </form>
                            </div>
                  <div class = "col-md-4"> </div>
            </div>

    </tab>
</tabset>