<fieldset class = 'fieldSetTravelDesk'>
                                    <Legend class = 'editProfileCompliance'>Driver Compliance</Legend>
                                            <div class = "col-md-12">
                                                <div class = "row tabContentAppSetting">
                                                    <form name="floating" class="form-vertical" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        License
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="License Expiry Days" 
                                                                  id="licenseExpiryDay" 
                                                                  name="licenseExpiryDay"
                                                                  ng-model="licenseExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  ng-change = "getlicenseExpiryDay(licenseExpiryDay)"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="licenseRepeatAlertsEvery" 
                                                                  name="licenseRepeatAlertsEvery"
                                                                  ng-model="licenseRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getlicenseRepeatAlertsEvery(licenseRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="licenceNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setLicenceNotificationType(licenceNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationLicenseTypeSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="licenseSMSDays" 
                                                                  name="licenseSMSDays"
                                                                  ng-model="licenseSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required = "licenceNotificationSMSRequired"
                                                                  ng-change = "getlicenseSMSDays(licenseSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationLicenseTypeEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="licenseEmailDays" 
                                                                  name="licenseEmailDays"
                                                                  ng-model="licenseEmailDays" 
                                                                  ng-minlength="1"
                                                                  ng-maxlength="300"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="300"
                                                                  ng-required = "licenceNotificationMessageRequired"
                                                                  ng-change = "getlicenseEmailDays(licenseEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                                    </form>
                                                  
                                                    <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        Medical Fitness
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Fitness Expiry Days" 
                                                                  id="medicalFitnessExpiryDay" 
                                                                  name="medicalFitnessExpiryDay"
                                                                  ng-model="medicalFitnessExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getmedicalFitnessExpiryDay(medicalFitnessExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="medicalFitnessRepeatAlertsEvery" 
                                                                  name="medicalFitnessRepeatAlertsEvery"
                                                                  ng-model="medicalFitnessRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getmedicalFitnessRepeatAlertsEvery(medicalFitnessRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="medicalFitnessNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setMedicalFitnessNotificationType(medicalFitnessNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationmedicalFitnessTypeSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="medicalFitnessSMSDays" 
                                                                  name="medicalFitnessSMSDays"
                                                                  ng-model="medicalFitnessSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required ="medicalFitnessSMSRequired"
                                                                  ng-change = "getmedicalFitnessSMSDays(medicalFitnessSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationmedicalFitnessTypeEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="medicalFitnessEmailDays" 
                                                                  name="medicalFitnessEmailDays"
                                                                  ng-model="medicalFitnessEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required ="medicalFitnessMessageRequired"
                                                                  ng-change = "getmedicalFitnessEmailDays(medicalFitnessEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                                    </form>

                                                    <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        Police Verification
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Police Verification Days" 
                                                                  id="policeVerificationExpiryDay" 
                                                                  name="policeVerificationExpiryDay"
                                                                  ng-model="policeVerificationExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getpoliceVerificationExpiryDay(policeVerificationExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="policeVerificationRepeatAlertsEvery" 
                                                                  name="policeVerificationRepeatAlertsEvery"
                                                                  ng-model="policeVerificationRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getpoliceVerificationRepeatAlertsEvery(policeVerificationRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="policeVerificationNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setPoliceVerificationNotificationType(policeVerificationNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationPoliceVerificationTypeSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="policeVerificationSMSDays" 
                                                                  name="policeVerificationSMSDays"
                                                                  ng-model="policeVerificationSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required ="policeVerificationSMSRequired"
                                                                  ng-change = "getpoliceVerificationSMSDays(policeVerificationSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationPoliceVerificationTypeEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="policeVerificationEmailDays" 
                                                                  name="policeVerificationEmailDays"
                                                                  ng-model="policeVerificationEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required ="policeVerificationMessageRequired"
                                                                  ng-change = "getpoliceVerificationEmailDays(policeVerificationEmailDays)"
                                                                  floating-validation/>
                                                          </label>


                                                    </form>

                                                    <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        DD Training
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="DDTraining Expiry Day(s)" 
                                                                  id="DDTrainingExpiryDay" 
                                                                  name="DDTrainingExpiryDay"
                                                                  ng-model="DDTrainingExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getDDTrainingExpiryDay(DDTrainingExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="DDTrainingRepeatAlertsEvery" 
                                                                  name="DDTrainingRepeatAlertsEvery"
                                                                  ng-model="DDTrainingRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getDDTrainingRepeatAlertsEvery(DDTrainingRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="DDTrainingNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setDDTrainingNotificationType(DDTrainingNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="DDTrainingTypeSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="DDTrainingSMSDays" 
                                                                  name="DDTrainingSMSDays"
                                                                  ng-model="DDTrainingSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required = "DDTrainingSMSRequired"
                                                                  ng-change = "getDDTrainingSMSDays(DDTrainingSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="DDTrainingTypeEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="DDTrainingEmailDays" 
                                                                  name="DDTrainingEmailDays"
                                                                  ng-model="DDTrainingEmailDays" 
                                                                  ng-minlength="1"
                                                                  ng-maxlength="300"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required = "DDTrainingMessageRequired"
                                                                  ng-change = "getDDTrainingEmailDays(DDTrainingEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                                    </form>
                                                </div>
                                          </div>        

                                           
                                    </fieldset>
                                    <br/>
                                    <fieldset class = 'fieldSetTravelDesk'>
                                    <Legend class = 'editProfileCompliance'>Vehicle Compliance</Legend>

                                                <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        Pollution Due
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Pollution Expiry Day(s)" 
                                                                  id="pollutionDueExpiryDay" 
                                                                  name="pollutionDueExpiryDay"
                                                                  ng-model="pollutionDueExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getpollutionDueExpiryDay(pollutionDueExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="pollutionDueRepeatAlertsEvery" 
                                                                  name="pollutionDueRepeatAlertsEvery"
                                                                  ng-model="pollutionDueRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getpollutionDueRepeatAlertsEvery(pollutionDueRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="pollutionDueNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setPollutionDue(pollutionDueNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationPollutionDueSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="pollutionDueSMSDays" 
                                                                  name="pollutionDueSMSDays"
                                                                  ng-model="pollutionDueSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="pollutionDueSMSRequired"
                                                                  ng-change = "getpollutionDueSMSDays(pollutionDueSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationPollutionDueEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="pollutionDueEmailDays" 
                                                                  name="pollutionDueEmailDays"
                                                                  ng-model="pollutionDueEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="pollutionDueMessageRequired"
                                                                  ng-change = "getpollutionDueEmailDays(pollutionDueEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                              </form>

                                              <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        Insurance Due
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="insurance Expiry Day(s)" 
                                                                  id="insuranceDueExpiryDay" 
                                                                  name="insuranceDueExpiryDay"
                                                                  ng-model="insuranceDueExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getinsuranceDueExpiryDay(insuranceDueExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="insuranceDueRepeatAlertsEvery" 
                                                                  name="insuranceDueRepeatAlertsEvery"
                                                                  ng-model="insuranceDueRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getinsuranceDueRepeatAlertsEvery(insuranceDueRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="insuranceDueNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setInsuranceDue(insuranceDueNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationInsuranceDueSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="insuranceDueSMSDays" 
                                                                  name="insuranceDueSMSDays"
                                                                  ng-model="insuranceDueSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="insuranceDueSMSRequired"
                                                                  ng-change = "getinsuranceDueSMSDays(insuranceDueSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationInsuranceDueEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="setInsuranceDue" 
                                                                  name="setInsuranceDue"
                                                                  ng-model="insuranceDueEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="insuranceDueMessageRequired"
                                                                  ng-change = "getinsuranceDueEmailDays(insuranceDueEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                              </form>

                                              <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        Tax Certificate Due
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Tax Expiry Day(s)" 
                                                                  id="taxCertificateExpiryDay" 
                                                                  name="taxCertificateExpiryDay"
                                                                  ng-model="taxCertificateExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "gettaxCertificateExpiryDay(taxCertificateExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="taxCertificateRepeatAlertsEvery" 
                                                                  name="taxCertificateRepeatAlertsEvery"
                                                                  ng-model="taxCertificateRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "gettaxCertificateRepeatAlertsEvery(taxCertificateRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="taxCertificateNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setTaxCertificate(taxCertificateNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationTaxCertificateSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="taxCertificateSMSDays" 
                                                                  name="insuranceDueSMSDays"
                                                                  ng-model="taxCertificateSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="taxCertificateSMSRequired"
                                                                  ng-change = "gettaxCertificateSMSDays(taxCertificateSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationTaxCertificateEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="taxCertificateEmailDays" 
                                                                  name="taxCertificateEmailDays"
                                                                  ng-model="taxCertificateEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="taxCertificateMessageRequired"
                                                                  ng-change = "gettaxCertificateEmailDays(taxCertificateEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                              </form>

                                              <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                        Permit Due
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Permit Expiry Day(s)" 
                                                                  id="permitDueExpiryDay" 
                                                                  name="permitDueExpiryDay"
                                                                  ng-model="permitDueExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getpermitDueExpiryDay(permitDueExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="permitDueRepeatAlertsEvery" 
                                                                  name="permitDueRepeatAlertsEvery"
                                                                  ng-model="permitDueRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getpermitDueRepeatAlertsEvery(permitDueRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="permitDueNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setPermitDue(permitDueNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationPermitDueSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="permitDueSMSDays" 
                                                                  name="permitDueSMSDays"
                                                                  ng-model="permitDueSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="permitDueSMSRequired"
                                                                  ng-change = "getpermitDueSMSDays(permitDueSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationPermitDueEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="permitDueEmailDays" 
                                                                  name="permitDueEmailDays"
                                                                  ng-model="permitDueEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required="permitDueMessageRequired"
                                                                  ng-change = "getpermitDueEmailDays(permitDueEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                              </form>

                                              <form name="floating" class="myProfileFormMargin" novalidate
                                                    ng-model-options="{updateOn:'default blur',debounce:{'default':500,'blur':0}}">
                                                          <div class = "col-md-2">
                                                          <br/>
                                                          Vehicel Maintenance
                                                    </div>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Maintenance Expiry Day(s)" 
                                                                  id="vehicelMaintenanceExpiryDay" 
                                                                  name="vehicelMaintenanceExpiryDay"
                                                                  ng-model="vehicelMaintenanceExpiryDay" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getvehicelMaintenanceExpiryDay(vehicelMaintenanceExpiryDay)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Repeat Alerts Every" 
                                                                  id="vehicelMaintenanceRepeatAlertsEvery" 
                                                                  name="vehicelMaintenanceRepeatAlertsEvery"
                                                                  ng-model="vehicelMaintenanceRepeatAlertsEvery" 
                                                                  is-number-only-valid
                                                                  ng-minlength="1"
                                                                  ng-maxlength="3"
                                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                  maxlength="3"
                                                                  required
                                                                  ng-change = "getvehicelMaintenanceRepeatAlertsEvery(vehicelMaintenanceRepeatAlertsEvery)"
                                                                  floating-validation/>
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel">
                                                          <p class="requestTypeLabel">Request Type</p>
                                                          <select ng-model="vehicelMaintenanceNotificationType"
                                                                            class = "smallDropDownRequestType"
                                                                            ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                                            ng-change = "setVehicelMaintenance(vehicelMaintenanceNotificationType)"
                                                                            required>
                                                                    </select> 
                                                          </label>
                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationVehicelMaintenanceSMSShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="SMS(Mobile Number)" 
                                                                  id="vehicelMaintenanceSMSDays" 
                                                                  name="vehicelMaintenanceSMSDays"
                                                                  ng-model="vehicelMaintenanceSMSDays"
                                                                  is-number-with-comma-valid
                                                                   ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required = "vehicelMaintenanceSMSRequired"
                                                                  ng-change = "getvehicelMaintenanceSMSDays(vehicelMaintenanceSMSDays)"
                                                                  floating-validation/>
                                                          </label>

                                                          <label class="radio-inline form-group applicationSettingLabel" ng-show="notificationVehicelMaintenanceEmailShow">
                                                          <input type="text" 
                                                                  class="form-control MyProfileFormCtrl" 
                                                                  placeholder="Email" 
                                                                  id="vehicelMaintenanceEmailDays" 
                                                                  name="vehicelMaintenanceEmailDays"
                                                                  ng-model="vehicelMaintenanceEmailDays" 
                                                                  ng-minlength="1"
                                                                   ng-maxlength="300"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   maxlength="300"
                                                                  ng-required = "vehicelMaintenanceMessageRequired"
                                                                  ng-change = "getvehicelMaintenanceEmailDays(vehicelMaintenanceEmailDays)"
                                                                  floating-validation/>
                                                          </label>

                                              </form>
                                    </fieldset>
                                      <br/>