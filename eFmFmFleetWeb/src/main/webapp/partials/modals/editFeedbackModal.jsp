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

<div ng-include="'partials/showAlertMessageModalTemplate.jsp'"></div>
<div class="loading"></div>
<div class="newEscortFormModalTemplate">
	<div class="row">
		<div class="modal-header modal-header1 col-md-12">
			<div class="row">
				<div class="icon-pencil addNewModal-icon col-md-1 floatLeft"></div>
				<div class="modalHeading col-md-10 floatLeft">Edit Feedback</div>
				<div class="col-md-1 floatRight pointer">
					<img src="images/portlet-remove-icon-white.png" class="floatRight"
						ng-click="cancel()">
				</div>
			</div>
		</div>
	</div>
	<div class="modal-body modalMainContent">
		<div class="formWrapper">
			<form name="editFeedbackDetail">
				<div class="col-md-6 col-xs-12">
					<div>
						<label>Subject Type</label> <input type="text"
							ng-model="feedback.alertType" class="form-control"
							placeholder="Subject Type" name='subjectType'
							ng-class="{error: editFeedbackDetail.subjectType.$invalid && !editFeedbackDetail.subjectType.$pristine}"
							readonly>
					</div>
				</div>

				<div class="col-md-6 col-xs-12">
					<div>
						<label>Subject</label> <input type="text"
							ng-model="feedback.alertTitle" class="form-control"
							placeholder="Subject" name='Subject'
							ng-class="{error: editFeedbackDetail.Subject.$invalid && !editFeedbackDetail.Subject.$pristine}"
							readonly>
					</div>
				</div>

				<div class="col-md-6 col-xs-12">
					<label>Shift Date</label> <input type="text"
						ng-model="feedback.alertDate" class="form-control"
						placeholder="Subject" name='alertDate'
						ng-class="{error: editFeedbackDetail.alertDate.$invalid && !editFeedbackDetail.alertDate.$pristine}"
						readonly>
				</div>

				<div class="col-md-6 col-xs-12">
					<div>
						<label>Shift Time</label> <input type="text"
							ng-model="feedback.shiftTime" class="form-control"
							placeholder="Subject" name='ShiftTime'
							ng-class="{error: editFeedbackDetail.ShiftTime.$invalid && !editFeedbackDetail.ShiftTime.$pristine}"
							readonly>

					</div>
				</div>
				<div class="col-md-6 col-xs-12">
					<div>
						<label>Current Status</label> <input type="currentStatus"
							ng-model="feedback.currentStatus" class="form-control"
							placeholder="Current Status" name='currentStatus'
							ng-class="{error: editFeedbackDetail.currentStatus.$invalid && !editFeedbackDetail.currentStatus.$pristine}"
							readonly>
					</div>
				</div>


				<div class="col-md-12 col-xs-12">
					<div>
						<label>Emp Description</label>
						<textarea type="textarea" ng-model="feedback.empDescription"
							class="form-control" placeholder="Description" name='Description'
							ng-class="{error: editFeedbackDetail.Description.$invalid && !editFeedbackDetail.Description.$pristine}"
							readonly>
                           </textarea>
					</div>
					<span class="hintModal"
						ng-show="editFeedbackDetail.mobileNumber.$error.maxlength">Invalid
						Description </span>
				</div>
				<div class="col-md-6 col-xs-12">
					<div>
						<label>Status</label> <select name="Status" class="form-control"
							ng-options="feedback.text for feedback in feedbackStatus track by feedback.value"
							ng-model="feedback.feedbackStatus"
							ng-change="setfeedbackType(feedbackTypes.selectedType)" required>
						</select>
					</div>
					<span class="hintModal"
						ng-show="editFeedbackDetail.Status.$error.maxlength">Status</span>
				</div>


				<div class="col-md-6 col-xs-12">
					<div>
						<label>Remarks</label>
						<textarea type="textarea" ng-model="feedback.remarks"
							class="form-control" placeholder="Remarks" name='Remarks'
							ng-class="{error: editFeedbackDetail.Remarks.$invalid && !editFeedbackDetail.Remarks.$pristine}"
							required>
                           </textarea>
					</div>
				</div>
				<div class="col-md-6 col-xs-12">
					<div>
						<label>Assign Feedback To</label> <select name="Status"
							class="form-control"
							ng-options="assignFeedback.text for assignFeedback in assignFeedbackTypes track by assignFeedback.value"
							ng-model="feedback.assignFeedbackTo" required>
						</select>
					</div>
					<span class="hintModal"
						ng-show="editFeedbackDetail.Status.$error.maxlength">Status</span>
				</div>
			</form>
			</fieldset>
		</div>
		</form>
	</div>
</div>
<div class="modal-footer modalFooter">
	<button type="button" class="btn btn-success buttonRadius0"
		ng-click="updatefeedback(feedback)"
		ng-disabled="editFeedbackDetail.$invalid">Save</button>
	<button type="button" class="btn btn-default buttonRadius0"
		ng-click="cancel()">Cancel</button>
</div>

</div>