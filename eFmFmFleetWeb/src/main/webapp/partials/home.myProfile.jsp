<!--
@date           06/05/2015
@Author         Saima Aziz
@Description    This page is viewable by the users with limited TABS view. Admin has the right to view all the Tabs.
@State          home.myProfile
@URL            /myProfile

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
06/05/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
05/04/2016  Saima Aziz      Added new Fields from EtaSMS onward in the Application Settings
-->

<div class = "myProfileTemplate container-fluid" ng-if = "isMyProfileActive">
    <div class = "row">
        <div class = "col-md-12 col-xs-12 heading1 marginBottom15">
            <span class="col-md-8 col-xs-8 vendorDashboardLabel">
            My Profile 
            </span>   
            <span class="col-md-2 col-xs-2 vendorDashboardMultiSelect" ng-show="multiFacility == 'Y'">
                <am-multiselect class="input-lg pull-right"
                                         multiple="true"
                                         ms-selected ="{{facilityData.length}} Facility(s) Selected"
                                         ng-model="facilityData"
                                         ms-header="All Facility"
                                         options="facility.branchId as facility.name for facility in facilityDetails"
                                         change="setFacilityDetails(facilityData)"
                                         >
                       </am-multiselect>
            </span>
            <span class="col-md-2 col-xs-2 vendorDashboardGetFacilityButton" ng-show="multiFacility == 'Y'">
              <input type="button" class="btn btn-success" value="Submit" name="" ng-click="getFacilityDetails(facilityData)">
            </span>
             <span class="col-md-2 col-xs-2 singleFacilityStyle" ng-show="multiFacility == 'N'" >
            </span>

            <div class = "col-md-12 col-xs-12 mainTabDiv_viewMap">
                <tabset class="tabset myProfileTab_myProfile">
                  <tab ng-click = "getTabInfo('profileInfo')"> 
                    <tab-heading>Profile Info</tab-heading>
                        <div class = "profileTabContent row">
                            <!--   ONLY SEEN ON THE BIG (LG and MD Sizes) SCREEN   -->
                          <div class = "col-md-12 col-xs-12 hidden-xs hidden-sm myProfileWrapper">
<!--                                <form name = "myProfileForm" class = "row">-->
                                    <div class = "col-md-2 col-xs-12 profileFrame">
                                        <img src = "images/default_profile.png" alt = "Employee Name">
                                    </div>
                                    <div class = "col-md-7 col-xs-12 btnFrame">
                                        <input type = "button"
                                               class = "btn btn-warning editProfileButton_myProfile"
                                               value = "Edit My Profile"
                                               ng-click = "editMyProfile()"
                                               ng-show = "!isProfileEdit"
                                               ng-class = "{disabled: changePasswordClicked}">
                                        <input type = "button"
                                               class = "btn btn-primary"
                                               value = "Change Password"
                                               ng-click = "openPasswordDiv()"
                                               ng-show = "!changePasswordClicked"
                                               ng-class = "{disabled: isProfileEdit}">
                                        <input type = "button"
                                               class = "btn btn-success"
                                               value = "Save Changes"
                                               ng-show = "isProfileEdit"
                                               ng-click = "saveProfile(user)"
                                               ng-disabled="myProfileForm.$invalid">
                                        <input type = "button"
                                               class = "btn btn-default"
                                               value = "Cancel Changes"
                                               ng-show = "isProfileEdit"
                                               ng-click = "cancelProfile(user)">
                                    </div>
                                    <div class = "col-md-10 col-xs-12 profileDataFrame">
                                        <div class = "row userInfo">
                                            <div class = "col-md-8">
                                                <div class = "passwordDiv"><form name = "changePasswordForm">
                                                    <div class = "row">
                                                        <div class = "col-md-8">
                                                            <div class = "row">
                                                            <a ng-href="j_spring_security_logout" class="hidden" target="{{condition ? ' _blank' : '_self'}}" id="logOut" href="j_spring_security_logout"><i class="icon-key"></i> Log Out</a>
                                                                <div class = "col-md-12">
                                                                    <span>Enter Old Password</span><br>
                                                                    <input type= "password" class = "col100" ng-model = "userPassword.oldPass" required>
                                                                </div>
                                                                <div class = "col-md-12 marginTop10">
                                                                    <span>Enter New Password</span><br>
                                                                    <input type= "password"
                                                                           ng-model = "userPassword.newPass1"
                                                                           ng-minlength='5'
                                                                           ng-maxlength="15"
                                                                           class = "col100"
                                                                           name = "password"
                                                                           ng-pattern = "regexPassword"
                                                                           required>
                                                                    <div class = "hintItallic">
                                                                        <span ng-show="!changePasswordForm.password.$error.required && (changePasswordForm.password.$error.minlength || changePasswordForm.password.$error.maxlength) && changePasswordForm.password.$dirty">Passwords must be between 5 and 15 characters.</span>
                                                                        <span ng-show="!changePasswordForm.password.$error.required && !changePasswordForm.password.$error.minlength && !changePasswordForm.password.$error.maxlength && changePasswordForm.password.$error.pattern && changePasswordForm.password.$dirty">(Must contain one lower &amp; uppercase letter,atleast one  number and one symbol.)</span>
                                                                    </div>
                                                                </div>
                                                                <div class = "col-md-12 marginTop10">
                                                                    <span>Re-Type New Password</span><br>
                                                                    <input type= "password" class = "col100" valid-password-c
                                                                           ng-model = "userPassword.newPass2"
                                                                           name = "password_c" required>
                                                                    <div class = "incorrectPassword2 help-block" ng-show="!changePasswordForm.password_c.$error.required && changePasswordForm.password_c.$error.noMatch && changePasswordForm.password.$dirty">Passwords do not match.</div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class = "col-md-4 savePassButtons">
                                                            <div class = "row">
                                                                <div class = "col-md-12">
                                                                    <input type= "button"
                                                                           class = "btn btn-success btn-sm col100"
                                                                           value = "Save"
                                                                           ng-disabled="changePasswordForm.$invalid"
                                                                           ng-click = "savePassword(userPassword)">
                                                                </div>
                                                                <div class = "col-md-12 marginTop20">
                                                                    <input type= "button"
                                                                           class = "btn btn-default btn-sm col100"
                                                                           value = "Cancel"
                                                                           ng-click = "cancelPassword()">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    </form>
                                                </div>


                                            </div>
                                            <form name = "myProfileForm" class = "row">
                                          <div class = "col-md-6 form-group">
                                                <span>User Name</span><br />
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.userName"
                                                       is-user-name-only-valid
                                                       ng-maxlength = '50'
                                                       maxlength = '50'
                                                       ng-disabled = 'true'
                                                       required
                                                       name = "username"
                                                       ng-class = "{error: myProfileForm.username.$invalid && !myProfileForm.username.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm.username.$invalid && !myProfileForm.username.$pristine">
                                                    * Please Enter a Valid User Name
                                                </span>
                                            </div>

                                            <div class = "col-md-6 form-group">
                                                <span>First Name</span>
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.firstName"
                                                       name = "fname"
                                                        ng-minlength="3"
                                                        ng-maxlength="20"
                                                              maxlength="20"
                                                          is-name-only-valid
                                                       ng-disabled = '!isProfileEdit'
                                                       required

                                                       ng-class = "{error2: myProfileForm.fname.$invalid && !myProfileForm.fname.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm.fname.$invalid && !myProfileForm.fname.$pristine">
                                                    * Please Enter a Valid First Name
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Last Name</span><br>
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.lastName"
                                                              ng-minlength="3"
                                                                  ng-maxlength="20"
                                                                  maxlength="20"
                                                              is-name-only-valid
                                                       ng-disabled = '!isProfileEdit'
                                                       name = "lname"
                                                       required

                                                       ng-class = "{error: myProfileForm.lname.$invalid && !myProfileForm.lname.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm.lname.$invalid && !myProfileForm.lname.$pristine">
                                                    * Please Enter a Valid Last Name
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Country</span><br>
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.country"
                                                       ng-maxlength = '15'
                                                       ng-disabled = '!isProfileEdit'
                                                       name = "country"
                                                       required
                                                       ng-pattern="regExName"
                                                       ng-class = "{error: myProfileForm.country.$invalid && !myProfileForm.country.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm.country.$invalid && !myProfileForm.country.$pristine">
                                                    * Please Enter a Valid Country Name
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Employee Designation</span><br>
                                                <input type= "text"
                                                       ng-model = "user.occupation"
                                                       ng-minlength="2"
                                                          ng-maxlength="30"
                                                        maxlength="30"
                                                              expect_special_char
                                                       ng-disabled = '!isProfileEdit'
                                                       class = "form-control">
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Email</span><br>
                                                <input type= "email"
                                                       class = "form-control"
                                                       ng-model = "user.email"
                                                       ng-disabled = '!isProfileEdit'
                                                       required
                                                       name = 'email'
                                                       ng-class = "{error: myProfileForm.email.$invalid && !myProfileForm.email.$pristine}">
                                                <span class = 'hintModal'  ng-show = "myProfileForm.email.$invalid && !myProfileForm.email.$pristine">
                                                    * Please Enter Valid Email Address
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Mobile Number</span><br>
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.mobileNumber"
                                                       is-number-only-valid                   ng-minlength="6"
                                                        ng-maxlength="18"
                                                        maxlength="18"
                                                       ng-disabled = '!isProfileEdit'
                                                       name = 'mobileNumber'
                                                       ng-class = "{error: myProfileForm.mobileNumber.$invalid && !myProfileForm.mobileNumber.$pristine}">
                                                <span class = 'hintModal'  ng-show = "myProfileForm.mobileNumber.$invalid && !myProfileForm.mobileNumber.$pristine">
                                                    * Please Enter Valid Mobile Number
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Department</span><br>

                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.interest"
                                                       ng-disabled = '!isProfileEdit'
                                               ng-minlength="0"
                                                ng-maxlength="30"
                                                maxlength="30"
                                                expect_special_char>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Website URL</span><br>

                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.website"
                                                       ng-disabled = '!isProfileEdit'>
                                            </div>
                                            <div class = "col-md-12 form-group">
                                                <span>Address</span><br>

                                                <textarea type= "text"
                                                          class = "form-control"
                                                          ng-model = "user.about"
                                                           ng-minlength="10"
                                                            ng-maxlength="250"
                                                             manlength="250"
                                                          expect_special_char
                                                       ng-disabled = 'true'>
                                                </textarea>
                                            </div>
                                        </form>
                                        </div>
                                    </div>
                          </div>

                          <!--   ONLY SEEN ON THE SMALL (SM and XS Sizes) SCREEN   -->
                          <div class = "col-md-12 col-xs-12 hidden-lg hidden-md myProfileWrapper">
<!--                                <form name = "myProfileForm2" class = "row">-->
                                    <div class = "col-md-2 col-xs-12 marginRow profileFrame2">
                                        <img src = "images/default_profile.png" alt = "Employee Name">
                                    </div>
                                    <div class = "col-md-7 col-xs-12 btnFrame2">
                                        <input type = "button"
                                               class = "btn btn-warning btn-xs editProfileButton_myProfile"
                                               value = "Edit My Profile"
                                               ng-click = "editMyProfile()"
                                               ng-show = "!isProfileEdit"
                                               ng-class = "{disabled: changePasswordClicked}">
                                        <input type = "button"
                                               class = "btn btn-primary btn-xs"
                                               value = "Change Password"
                                               ng-click = "openPasswordDiv()"
                                               ng-show = "!changePasswordClicked"
                                               ng-class = "{disabled: isProfileEdit}">
                                        <input type = "button"
                                               class = "btn btn-success btn-xs"
                                               value = "Save Changes"
                                               ng-show = "isProfileEdit"
                                               ng-click = "saveProfile(user)"
                                               ng-disabled="myProfileForm2.$invalid">
                                        <input type = "button"
                                               class = "btn btn-default btn-xs"
                                               value = "Cancel Changes"
                                               ng-show = "isProfileEdit"
                                               ng-click = "cancelProfile(user)">
                                    </div>
                                    <div class = "col-md-10 col-xs-12 profileDataFrame2">
                                        <div class = "row userInfo2">
                                            <div class = "col-md-8"><form name = "smScreenChangePasswordForm">
                                                <div class = "passwordDiv">
                                                    <div class = "row">
                                                        <div class = "col-md-12">
                                                            <div class = "row">
                                                                <div class = "col-md-12">
                                                                    <span>Enter Old Password</span><br>
                                                                    <input type= "password" class = "col100" ng-model = "userPasswordSmScreen.oldPass" required>
                                                                </div>
                                                                <div class = "col-md-12 marginTop10">
                                                                    <span>Enter New Password</span><br>
                                                                    <input type= "text"
                                                                           ng-model = "userPasswordSmScreen.newPass1" class = "col100"
                                                                           ng-minlength='4'
                                                                           ng-maxlength="10"
                                                                           maxlength="10"
                                                                           name = "password"
                                                                           ng-pattern = "regexPassword">
                                                                    <div class = "hintItallic">
                                                                        <span ng-show="!smScreenChangePasswordForm.password.$error.required && (smScreenChangePasswordForm.password.$error.minlength || smScreenChangePasswordForm.password.$error.maxlength) && smScreenChangePasswordForm.password.$dirty">Passwords must be between 4 and 8 characters.</span>
                                                                        <span ng-show="!smScreenChangePasswordForm.password.$error.required && !smScreenChangePasswordForm.password.$error.minlength && !smScreenChangePasswordForm.password.$error.maxlength && smScreenChangePasswordForm.password.$error.pattern && smScreenChangePasswordForm.password.$dirty">Must contain one lower &amp; uppercase letter, and one non-alpha character (a number or a symbol.)</span>
                                                                    </div>
                                                                </div>
                                                                <div class = "col-md-12 marginTop10">
                                                                    <span>Re-Type New Password</span><br>
                                                                    <input type= "text"
                                                                           ng-model = "userPasswordSmScreen.newPass2"
                                                                           class = "col100" sm-valid-password-c
                                                                           name = "password_c"
                                                                           required>
                                                                    <div class = "incorrectPassword2 help-block" ng-show="!smScreenChangePasswordForm.password_c.$error.required && smScreenChangePasswordForm.password_c.$error.noMatch && smScreenChangePasswordForm.password.$dirty">Passwords do not match.</div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class = "col-md-12 savePassButtons">
                                                            <input type= "button"
                                                                   class = "btn btn-success btn-xs floatLeft"
                                                                   value = "Save"
                                                                   ng-click = "savePassword(userPasswordSmScreen)"
                                                                   ng-disabled="smScreenChangePasswordForm.$invalid">
                                                            <input type= "button"
                                                                   class = "btn btn-default btn-xs floatLeft marginLeft20"
                                                                   value = "Cancel"
                                                                   ng-click = "cancelPassword()">
                                                        </div>
                                                    </div>

                                                </div></form>
                                            </div>

                                        <form name = "myProfileForm2" class = "row">
                                            <div class = "col-md-6 form-group">
                                                <span>User Name</span><br>
                                                <input type= "text" ng-model = "user.userName"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.userName"
                                                       ng-show = "isProfileEdit"
                                                       required
                                                       name = "username"
                                                       ng-class = "{error: myProfileForm2.username.$invalid && !myProfileForm2.username.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm2.username.$invalid && !myProfileForm2.username.$pristine">
                                                    * Please Enter a Valid User Name
                                                </span>
                                            </div>

                                            <div class = "col-md-6 form-group">
                                                <span>First Name</span>
                                                <input type= "text" ng-model = "user.firstName"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.firstName"
                                                       ng-show = "isProfileEdit"
                                                       name = "fname"
                                                       required
                                                       ng-minlength="3"
  ng-maxlength="20"
  maxlength="20"
  is-name-only-valid
                                                       ng-class = "{error2: myProfileForm2.fname.$invalid && !myProfileForm.fname.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm2.fname.$invalid && !myProfileForm2.fname.$pristine">
                                                    * Please Enter a Valid First Name
                                                </span>
                                            </div>
                                            <div class = "col-md-6  form-group">
                                                <span>Last Name</span>
                                                <input type= "text" ng-model = "user.lastName"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.lastName"
                                                       ng-show = "isProfileEdit"
                                                       name = "lname"
                                                       required
                                                       ng-minlength="3"
 ng-maxlength="20"
 maxlength="20"
 is-name-only-valid
                                                       ng-class = "{error: myProfileForm2.lname.$invalid && !myProfileForm2.lname.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm2.lname.$invalid && !myProfileForm2.lname.$pristine">
                                                    * Please Enter a Valid Last Name
                                                </span>
                                            </div>

                                            <div class = "col-md-6 form-group">
                                                <span>Country</span>
                                                <input type= "text" ng-model = "user.country"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.country"
                                                       ng-show = "isProfileEdit"
                                                       name = "country"
                                                       required
                                                       ng-pattern="regExName"
                                                       ng-class = "{error: myProfileForm2.country.$invalid && !myProfileForm2.country.$pristine}">
                                                <span class = 'hintModal' ng-show = "myProfileForm2.country.$invalid && !myProfileForm2.country.$pristine">
                                                    * Please Enter a Valid Country Name
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Designation</span>
                                                <input type= "text" ng-model = "user.occupation"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.occupation"
                                                       ng-show = "isProfileEdit"></div>
                                            <div class = "col-md-6 form-group">
                                                <span>Email</span>
                                                <input type= "email" ng-model = "user.email"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "email"
                                                       class = "form-control"
                                                       ng-model = "user.email"
                                                       ng-show = "isProfileEdit"
                                                       required
                                                       name = 'email'
                                                       ng-class = "{error: myProfileForm2.email.$invalid && !myProfileForm2.email.$pristine}">
                                                <span class = 'hintModal'  ng-show = "myProfileForm2.email.$invalid && !myProfileForm2.email.$pristine">
                                                    * Please Enter Valid Email Address
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Mobile Number</span><br>
                                                 <input type= "text" ng-model = "user.mobileNumber"
                                                        readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.mobileNumber"
                                                       ng-show = "isProfileEdit"
                                                       required
                                                       name = 'mobileNumber'
                                                       ng-class = "{error: myProfileForm2.mobileNumber.$invalid && !myProfileForm2.mobileNumber.$pristine}">
                                                <span class = 'hintModal'  ng-show = "myProfileForm2.mobileNumber.$invalid && !myProfileForm2.mobileNumber.$pristine">
                                                    * Please Enter Valid Mobile Number
                                                </span>
                                            </div>
                                            <div class = "col-md-6 form-group">
                                                <span>Department</span><br>
                                                <input type= "text" ng-model = "user.interest"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.interest"
                                                       ng-show = "isProfileEdit"></div>
                                            <div class = "col-md-6 form-group">
                                                <span>Website URL</span><br>
                                                <input type= "text" ng-model = "user.website"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.website"
                                                       ng-show = "isProfileEdit"></div>
                                            <div class = "col-md-6 form-group">
                                                <span>Address</span><br>
                                                <input type= "text" ng-model = "user.about"
                                                       readonly
                                                       class = "nonEditableInput_myProfile form-control"
                                                       ng-show = "!isProfileEdit">
                                                <input type= "text"
                                                       class = "form-control"
                                                       ng-model = "user.about"
                                                       ng-show = "isProfileEdit">
                                            </div>
                                        </form>
                                        </div>
                                    </div>
                          </div>

                        </div>
                    </tab>

                    <!--***********************SECOND TAB _ ADMIN SETTING*********************** -->
 <tab ng-if = "adminRole || superadmin" ng-hide ="userRoleDetails == 'webuser'"  ng-click = "getAdminSettings(facilityData); getTabInfo('administratorSettings')">
                    <tab-heading>Administrator Settings</tab-heading>
                        <div class = "profileTabContent row">
                          <div class = "col-md-12 col-xs-12 myProfileWrapper">
                                <div class = 'row'>
                                        <div class = "col-md-4 col-xs-12">
                                            <input type = "text" class = "form-control floatLeft adminFilter" placeholder = "Filter Users" ng-model = "employeeFilter"
                                            expect_special_char>
                                        </div>

                                        <div class = "col-md-4 searchEmployeeTravelDesk">
                                            <div class = "input-group floatLeft calendarInput">
                                              <input ng-model="search.text"
                                                     type = "text"
                                                     class="form-control"
                                                     ng-keydown="$event.which === 13 && searchEmployee(search.text, facilityData)"
                                                     placeholder = "Search By EmployeeId"
                                                      ng-minlegth="2"
                                                     ng-maxlegth="20"
                                                     maxlength =  '20'
                                                     expect_special_char
                                                     ng-focus = "searchIsEmpty = false"
                                                     ng-class = "{error: searchIsEmpty}">
                                               <span class="input-group-btn">
                                                   <button class="btn btn-success" ng-disabled = "!search.text"
                                                           ng-click="searchEmployee(search.text, facilityData)">
                                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                            </div>
                                        </div>

                                        <div class = 'col-md-4 col-xs-12'>
                                            <button class = "btn btn-success floatRight addUser_myProfiile"
                                                    ng-click = "addUser('lg')"><span class = "icon icon-user"></span><span>Add User</span>
                                            </button>
                                        </div>
                                        </div> 
                                        <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!getAdminResult">
                                            <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                        </div>
                                        <table class = "adminSettingsTable table table-responsive container-fluid" ng-if = "getAdminResult">
                                            <thead class ="tableHeading">
                                                <tr>
                                                  <th>Emlpoyee ID</th>
                                                  <th>Name</th>
                                                  <th>User Name</th>
                                                  <th>Mobile Number</th>
                                                  <th>EmailId</th>
                                                  <th>UserType</th>
                                                  <th>Facility Name</th>
                                                  <th></th>
                                                </tr>
                                            </thead>
                                            <tbody ng-show="filteredAdminData.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                          <div class = "noData">There is No Data for Administrator Settings</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>
                                            <tbody ng-show="filteredAdminData.length > 0">
                                               <tr ng-repeat = "user in filteredAdminData = (administratorData | filter:employeeFilter)">
                                                  <td class = "col-md-1">{{user.employeeId}}</td>
                                                  <td class = "col-md-3">{{user.employeeName}}</td>
                                                  <td class = "col-md-2">{{user.userName}}</td>
                                                  <td class = "col-md-2">{{user.mobileNumber}}</td>
                                                  <td class = "col-md-2">{{user.emailId}}</td>
                                                  <td class = "col-md-2">{{user.userRole}}</td>
                                                  <td class = "col-md-2">{{user.facilityName}}</td>

                                                   <td class = "col-md-2">
                                                         <button type = "button"
                                                                 class = "btn btn-primary btn-xs buttonRadius0 assignButton_myProfile"
                                                                 ng-click ="assignModulesAccess(user, facilityData)"
                                                                 ng-disabled="!user.accessEnabled"
                                                                 ng-disabled = "user.userRole == 'superadmin'">Assign Access
                                                       </button>

                                                   </td>
                                                </tr>
                                            </tbody>
                                    </table>
                            </div>
                    </div>
                </tab>
                    <!--******************THIRD TAB**************************** -->

                  <tab ng-if = "adminRole || superadmin" ng-hide ="userRoleDetails == 'webuser'" ng-click = "getApplicationSettings(facilityData);getTabInfo('applicationSettings')">
                    <tab-heading>Application Settings</tab-heading>
                        <div class = "profileTabContent row" ng-if = "gotResultAppShift">
                          <div class = "col-md-12 col-xs-12 applicationSetting">



                               <form name = "appSettings">

                        <div class="panel-group">
                          <div class="panel panel-success">
                            <div class="panel-heading">Settings</div>
                              <div class="panel-body new">
                                <div class = "col-md-12">


  <div class=""><br/>
  <div class="col-md-1"><br/>Escort Needed</div>
  <div class="col-md-5">
    <div class="funkyradio escortNeeded" >
        <div class="funkyradio-success">
            <input type="radio" name="radio" id="None" value="None" ng-model="escortSelection" ng-change="setEscortType(escortSelection)"/>
            <label for="None">None</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" name="radio" id="Always" value="Always" ng-model="escortSelection" ng-change="setEscortType(escortSelection)"/>
            <label for="Always">Always</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" name="radio" id="femalepresent" value="femalepresent" ng-model="escortSelection" ng-change="setEscortType(escortSelection)"/>
            <label for="femalepresent">When female passenger is present</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" name="radio" id="firstlastfemale" value="firstlastfemale" ng-model="escortSelection" ng-change="setEscortType(escortSelection)"/>
            <label for="firstlastfemale">For last drop and first pickup of female passenger</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" name="radio" id="femaleAlone" value="femaleAlone" ng-model="escortSelection" ng-change="setEscortType(escortSelection)"/>
            <label for="femaleAlone">When only One female passenger alone in route</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" name="radio" id="allFemale" value="allFemale" ng-model="escortSelection" ng-change="setEscortType(escortSelection)"/>
            <label for="allFemale">When all female passenger in route</label>
        </div>
        <br/>
        <br/>
    </div>
</div>
<div class="col-md-6" ng-show="escortTimeDivShow">
      <div class="panel panel-default">
      <div class="panel-heading">Set Escort Timing - {{escortTimingPanelTitle}}</div>
      <div class="panel-body">
          Are you sure want to set escort Timing?
          <input type="button" class="btn btn-success btn-sm" value="Yes" ng-disabled="escortConfirmationYES" ng-click="setEscortConfirmation('Y')" name="">
          <input type="button" class="btn btn-danger btn-sm" value="No" ng-disabled="escortConfirmationNO" ng-click="setEscortConfirmation('N')" name="">
<div class="btn-group btn-group-justified" ng-show="escortEditTimeShow">
          <a class="btn btn-default">Pickup</a>
          <a class="btn btn-default">Start Time</a>
          <a class="btn buttonBackgroundEscort">
          <timepicker ng-model="escortStartTime"
                      hour-step="hstep"
                      ng-change="setEscortStartTime(escortStartTime)"
                      minute-step="mstep"
                      show-meridian="ismeridian"
                      readonly-input = 'true'
                      class = "timepicker2_empReq floatLeft"
                      >
          </timepicker>  </a>

          <a class="btn btn-default">End Time</a>
          <a class="btn buttonBackgroundEscort">
          <timepicker ng-model="escortEndTime"
                      hour-step="hstep"
                      ng-change="setEscortEndTime(escortEndTime)"
                      minute-step="mstep"
                      show-meridian="ismeridian"
                      readonly-input = 'true'
                      class = "timepicker2_empReq floatLeft"
                      >
          </timepicker> </a>
      </div>
      <div class="btn-group btn-group-justified" ng-show="escortEditTimeShow">
          <a class="btn btn-default dropPad"> Drop </a>
          <a class="btn btn-default">Start Time</a>
          <a class="btn buttonBackgroundEscort">
          <timepicker ng-model="escortStartTimeDrop"
                      hour-step="hstep"
                      ng-change="setEscortStartTimeDrop(escortStartTimeDrop)"
                      minute-step="mstep"
                      show-meridian="ismeridian"
                      readonly-input = 'true'
                      class = "timepicker2_empReq floatLeft"
                      >
          </timepicker>  </a>

          <a class="btn btn-default">End Time</a>
          <a class="btn buttonBackgroundEscort">
          <timepicker ng-model="escortEndTimeDrop"
                      hour-step="hstep"
                      ng-change="setEscortEndTimeDrop(escortEndTimeDrop)"
                      minute-step="mstep"
                      show-meridian="ismeridian"
                      readonly-input = 'true'
                      class = "timepicker2_empReq floatLeft"
                      >
          </timepicker> </a>
      </div>

      </div>
      </div>
</div>


                                            </div>



                                    <div class = "row tabContentAppSetting">
                                     <!--    <div class = "col-md-2">
                                              Escort Needed
                                        </div>

                                        <div class = "col-md-4">
                                                <div class = "row escortNeeded">
                                                    <label class = "radioLabel">
                                                        <input type="radio"
                                                               ng-model="escortNeeded"
                                                               value="None"
                                                               ng-disabled = "isDisable"
                                                               ng-change = "setEscortNeeded(escortNeeded)">None</label><br>
                                                    <label class = "radioLabel">
                                                        <input type="radio"
                                                               ng-model="escortNeeded"
                                                               value="Always"
                                                               ng-disabled = "isDisable"
                                                               ng-change = "setEscortNeeded(escortNeeded)">Always</label><br>
                                                    <label class = "radioLabel">
                                                        <input type="radio"
                                                               ng-model="escortNeeded"
                                                               value="femalepresent"
                                                               ng-disabled = "isDisable"
                                                               ng-change = "setEscortNeeded(escortNeeded)">When female passenger is present</label><br>
                                                    <label class = "radioLabel">
                                                        <input type="radio"
                                                               ng-model="escortNeeded"
                                                               value="firstlastfemale"
                                                               ng-change = "setEscortNeeded(escortNeeded)">For last drop and first pickup of female passenger</label>

                                                </div>
                                            </div> -->
                                       </div></div>
                                       <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Manager Approval Required
                                            </div>
                                            <div class = "col-md-9">

                                                <div class = "row escortNeeded">
                                                    <label class = "radioLabel">
                                                        <input type="radio"
                                                               ng-model="approvalNeeded"
                                                               value="No"
                                                               ng-change = "setapprovalNeeded(approvalNeeded)">No</label><br>
                                                    <label class = "radioLabel">
                                                        <input type="radio"
                                                               ng-model="approvalNeeded"
                                                               value="Yes"
                                                               ng-disabled = "isDisable"
                                                               ng-change = "setapprovalNeeded(approvalNeeded)">Yes</label>
                                                </div>
                                            </div>
                                       </div>
                                   </div>

                                   <div class = "col-md-12" ng-show = "isChangeLocation">
                                       <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                        Update Location
                                    </div>
                                    <div class = "col-md-9">
                                        <div class = "row updateLocationDiv">
                                            <div class = "col-md-2 col-xs-12">
                                                <span>Company Name</span>
                                            </div>
                                            <div class = "col-md-9 col-xs-12">
                                                <span>Newt Global</span>
                                            </div>
                                            <div class = "col-md-2 col-xs-12">
                                                <span>Company Address</span>
                                            </div>
                                            <div class = "col-md-9 col-xs-12">
                                                <div ng-show = "!isEdit" class = "editedAddress floatLeft">{{currLocation}}</div>
                                                <input  ng-show = "isEdit"
                                                       type = "text"
                                                       class = "editedAddress floatLeft"
                                                       ng-model = "currLocation">
                                                <input  ng-show = "isEdit"
                                                       type = "text"
                                                       class = "editedAddress hidden"
                                                       ng-model = "currCords">
                                                <span class = "floatLeft pointer"><i class ="icon-map-marker mapMarkerIcon"
                                                         tooltip="Click this to Find Location on the Map"
                                                         tooltip-placement="top"
                                                         tooltip-trigger="mouseenter"
                                                         ng-click = "openMap('lg')"></i></span>
                                            </div>

                                        </div>
                                    </div>
                                       </div>
                                  </div>

                                   <div class = "col-md-12 marginTop20">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Number of Administrator required
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "adminRequired"
                                                       placeholder="Number of Administtrator required"
                                                       name = "adminRequired"
                                                       ng-change = "setAdminRequired(adminRequired)"
                                                       ng-class = "{error: appSettings.adminRequired.$invalid && !appSettings.adminRequired.$pristine}"
                                                       required>
                                               <br/>
                                            </div>
                                            <div class = "col-md-2">
                                                Password reset duration for Admin
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "resetPaswwordForAdmin"
                                                       placeholder="Password reset duration for Admin"
                                                       name = "resetPaswwordForAdmin"
                                                       ng-change = "setResetPaswwordForAdmin(resetPaswwordForAdmin)"
                                                       ng-class = "{error: appSettings.resetPaswwordForAdmin.$invalid && !appSettings.resetPaswwordForAdmin.$pristine}"
                                                       required>
                                               <br/>
                                            </div>
                                       </div>
                                   </div>
                                     <div class = "col-md-12 marginTop20">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Inactive Admin Password
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "adminInactiveAccount"
                                                       placeholder="Inactive Admin Password"
                                                       name = "inctiveAdminPass"
                                                       ng-change = "setAdminInactiveAccount(adminInactiveAccount)"
                                                       ng-class = "{error: appSettings.adminInactiveAccount.$invalid && !appSettings.adminInactiveAccount.$pristine}"
                                                       required>
                                               <br/> <!--  <span class ="hintItallic">( Inactive admin password in <strong>Days</strong>)</span>  -->
                                            </div>
                                            <div class = "col-md-2">
                                                Password reset period for user
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "resetPaswwordForUser"
                                                       placeholder="Password reset period for user"
                                                       name = "resetPaswwordForUser"
                                                       ng-change = "setResetPaswwordForUser(resetPaswwordForUser)"
                                                       ng-class = "{error: appSettings.resetPaswwordForUser.$invalid && !appSettings.resetPaswwordUser.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic">(Password reset period for user in <strong>Days</strong>)</span>  -->
                                            </div>
                                       </div>
                                   </div>

                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Two factor authentication
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="twoWayFactor"
                                                        class = "smallDropDown"
                                                        ng-options="twoWayFactor for twoWayFactor in applicationSettingsArrays.twoWayFactorArray"
                                                        ng-change = "settwoWayFactor(twoWayFactor)">
                                                </select>

                                            </div>
                                           <div class = "col-md-2">
                                                Number of wrong password attempts
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="2"
                                                       maxlength="2"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "numberOfAttemptsWrongPass"
                                                       placeholder="Number of wrong password attempts"
                                                       name = "numberOfAttemptsWrongPass"
                                                       ng-change = "setNumberOfAttemptsWrongPass(numberOfAttemptsWrongPass)"
                                                       ng-class = "{error: appSettings.numberOfAttemptsWrongPass.$invalid && !appSettings.numberOfAttemptsWrongPass.$pristine}"
                                                       required>
                                               <br/>
                                            </div>
                                       </div>
                                    </div>

                                  <!-- <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                              Trip Type
                                            </div>
                                           <div class = "col-md-4">
                                                <select ng-model="pickDropNeeded"
                                                        class = "smallDropDown"
                                                        ng-options="pickDrop for pickDrop in applicationSettingsArrays.pickDropArray"
                                                        ng-change = "setPickDropNeeded(pickDropNeeded)">
                                                </select>
                                           </div>

                                      </div>
                                  </div>  -->

                                  <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                              Session Time-out (Minutes)
                                            </div>
                                           <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-disabled = "{{sessionTimeOutDisabled}}"
                                                       ng-maxlength="2"
                                                       maxlength="2"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "sessiontimeOut"
                                                       placeholder="Session Timeout(mins)"
                                                       name = "sessiontimeOut"
                                                       ng-change = "setSessiontimeOut(sessiontimeOut)"
                                                       ng-class = "{error: appSettings.sessiontimeOut.$invalid && !appSettings.sessiontimeOut.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic">(Session timeout in <strong>Minutes</strong>)</span>  -->
                                           </div>

                                           <div class = "col-md-2">
                                              Session Notification Time (Seconds)
                                            </div>
                                           <div class = "col-md-4">
                                           <input type="text"
                                                  name=""
                                                  class = "appSettingsDropDowns"
                                                  ng-model="sessionTimeOutNotificationInSec"
                                                  is-number-only-valid
                                                  ng-minlength="1"
                                                  ng-maxlength="3"
                                                  oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                  maxlength="3"
                                                  required
                                                  ng-change="setSessionNotificationTime(sessionTimeOutNotificationInSec)">
                                                <!-- <select ng-model="sessionNotificationTime"
                                                   class="appSettingsDropDowns"
                                                   ng-options="adhoc for adhoc in applicationSettingsArrays.adhocTimePickers"
                                                   ng-change = "setAdhocTimePicker(adhocTimePicker)"
                                                   required>
                                                </select>   -->
                                               <br/>
                                           </div>
                                      </div>
                               </div>



                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class="col-md-2">Last Passwords can not be your
                                        current password</div>

                                      <div class="col-md-4">
                                        <input class="appSettingsDropDowns"
                                          is-number-valid
                                          ng-minlength="0"
                                          ng-maxlength="2"
                                          maxlength="2"
                                          oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                          ng-model="lastPasswordNotCurrent"
                                          placeholder="Employee second normal Pickup request"
                                          name="lastPasswordNotCurrent"
                                          ng-change="setLastPasswordNotCurrent(lastPasswordNotCurrent)"
                                          ng-class="{error: appSettings.lastPasswordNotCurrent.$invalid && !appSettings.lastPasswordNotCurrent.$pristine}"
                                          required> <br />
                                      </div>
                                      <div class = "col-md-2">
                                          Panic Alert
                                        </div>
                                       <div class = "col-md-4">
                                            <select ng-model="panicAlertNeeded"
                                                    class = "smallDropDown"
                                                    ng-options="panicAlertNeeded for panicAlertNeeded in applicationSettingsArrays.panicAlertNeededArray"
                                                    ng-change = "setPanicAlertNeeded(panicAlertNeeded)">
                                            </select>
                                       </div>
                                  </div>
                              </div>



                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class="col-md-2">OTP Enable Time Limit(Minutes/Hours)</div>

                                      <div class="col-md-4">
                                            <timepicker ng-model="OTPEnableTimeLimit"
                                                        hour-step="hstep"
                                                        ng-change ="setOTPEnableTimeLimit(OTPEnableTimeLimit)"
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft"
                                                        required>
                                            </timepicker>

                                            <!-- <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="OTPEnableTimeLimit"
                                                   name=""
                                                   is-number-only-valid
                                                   ng-minlength="1"
                                                   ng-maxlength="3"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                   maxlength="3"
                                                   required
                                                   ng-change="setOTPEnableTimeLimit(OTPEnableTimeLimit)"> -->
                                      </div>
                                      <div class = "col-md-2">
                                          OTP Generation Maximum Limit
                                        </div>
                                       <div class = "col-md-4">
                                            <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="OTPGenerationMaximumLimit"
                                                   name=""
                                                   is-number-only-valid
                                                   ng-minlength="1"
                                                   ng-maxlength="3"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                   maxlength="3"
                                                   required
                                                   ng-change="setOTPGenerationMaximumLimit(OTPGenerationMaximumLimit)">
                                       </div>
                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                Request Approval Required
                                      </div>
                                      <div class = "col-md-4">
                                          <select ng-model="approvalProcess"
                                             class="appSettingsDropDowns"
                                             ng-options="approvalProcess for approvalProcess in applicationSettingsArrays.approvalProcess"
                                             ng-change = "setApprovalProcess(approvalProcess)"
                                             required>
                                          </select>
                                      </div>

                                        <div class = "col-md-2" ng-show="postApprovalShow">
                                                Pre Approval Allowed Days
                                      </div>
                                      <div class = "col-md-4" ng-show="postApprovalShow">
                                          <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="postApproval"
                                                   name=""
                                                   is-number-only-valid
                                                   ng-minlength="1"
                                                   ng-maxlength="3"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                   maxlength="3"
                                                   ng-required="postApprovalRequired"
                                                   ng-change="setPostApproval(postApproval)">
                                      </div>
                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                Vehice Check List
                                      </div>
                                      <div class = "col-md-4">
                                          <select ng-model="vehiceCheckList"
                                             class="appSettingsDropDowns"
                                             ng-options="vehiceCheckList for vehiceCheckList in applicationSettingsArrays.vehiceCheckList"
                                             ng-change = "setVehiceCheckList(vehiceCheckList)"
                                             required>
                                          </select>
                                      </div>
                                      <div class = "col-md-2">
                                                Driver History
                                      </div>
                                      <div class = "col-md-4">
                                          <select ng-model="driverHistory"
                                             class="appSettingsDropDowns"
                                             ng-options="driverHistory for driverHistory in applicationSettingsArrays.driverHistory"
                                             ng-change = "setDriverHistory(driverHistory)"
                                             required>
                                          </select>
                                      </div>
                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                ShiftTime Gender Preference
                                      </div>
                                      <div class = "col-md-4">
                                          <select ng-model="shiftTimeGenderPreference"
                                             class="appSettingsDropDowns"
                                             ng-options="shiftTimeGenderPreference for shiftTimeGenderPreference in applicationSettingsArrays.shiftTimeGenderPreference"
                                             ng-change = "setShiftTimeGenderPreference(shiftTimeGenderPreference)"
                                             required>
                                          </select>
                                      </div>
                                      <div class = "col-md-2">
                                                Minimum Destination Count
                                        </div>
                                        <div class = "col-md-4">
                                            <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="minimumDestCount"
                                                   name=""
                                                   is-number-only-valid-with-out-zero
                                                   ng-minlength="1"
                                                   ng-maxlength="3"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                                                   maxlength="3"
                                                   required
                                                   ng-change="setMinimumDestCount(minimumDestCount)">
                                        </div>
                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                Request With Project
                                        </div>
                                        <div class = "col-md-4">
                                            <select ng-model="requestWithProject"
                                               class="appSettingsDropDowns"
                                               ng-options="requestWithProject for requestWithProject in applicationSettingsArrays.requestWithProject"
                                               ng-change = "setRequestWithProject(requestWithProject)"
                                               required>
                                            </select>
                                        </div>
                                      <div class = "col-md-2">
                                                Manager Request Create Process
                                        </div>
                                        <div class = "col-md-4">
                                            <select ng-model="managerReqCreateProcess"
                                                     class="appSettingsDropDowns"
                                                     ng-options="managerReqCreateProcess for managerReqCreateProcess in applicationSettingsArrays.managerReqCreateProcess"
                                                     ng-change = "setManagerReqCreateProcess(managerReqCreateProcess)"
                                                     required>
                                          </select>
                                        </div>
                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                GPS KM Modification
                                      </div>

                                      <div class = "col-md-4">
                                          <select ng-model="gpsKmModification"
                                             class="appSettingsDropDowns"
                                             ng-options="gpsKmModification for gpsKmModification in applicationSettingsArrays.gpsKmModification"
                                             ng-change = "setGpsKmModification(gpsKmModification)"
                                             required>
                                          </select>
                                      </div>


                                      <div class = "col-md-2">
                                                On Pickup noshow, auto cancel drop
                                      </div>

                                      <div class = "col-md-4">

                                          <select ng-model="autoCancelDrop"
                                                 class="appSettingsDropDowns"
                                                 ng-options="autoCancelDrop for autoCancelDrop in applicationSettingsArrays.autoCancelDrop"
                                                 ng-change = "setAutoCancelDrop(autoCancelDrop)"
                                                 required>
                                          </select>
                                      </div>

                                   </div>
                              </div>

                               <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                      <br/>
                                      <br/>
                                                Delay Trips
                                      </div>

                                      <div class = "col-md-4">
                                          <timepicker ng-model="delayTrips"
                                                        hour-step="hstep"
                                                        ng-change = setDelayTrips(delayTrips)
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                      </div>
                                      <div class = "col-md-2">
                                      <br/>
                                      <br/>
                                                Number Of Consecutive No Show
                                      </div>

                                      <div class = "col-md-4">
                                      <br/>
                                      <br/>
                                          <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="consecutiveNoShowCount"
                                                   name=""
                                                   is-number-only-valid-with-out-zero
                                                   ng-minlength="1"
                                                   ng-maxlength="3"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                                                   maxlength="3"
                                                   required
                                                   ng-change="setConsecutiveNoShow(consecutiveNoShowCount)">
                                      </div>

                                   </div>
                              </div>
                               <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                Mobile Login URL
                                      </div>

                                      <div class = "col-md-4">
                                      
                                          <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="mobileLoginUrl"
                                                   name=""
                                                   ng-minlength="1"
                                                   ng-maxlength="80"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                                                   maxlength="80"
                                                   required
                                                   popover="{{mobileLoginUrl}}"
                                                   popover-trigger="mouseenter"
                                                   ng-change="setMobileLoginUrl(mobileLoginUrl)">
                                      </div>
                                      <div class = "col-md-2">
                                                SSO Login Url
                                      </div>

                                      <div class = "col-md-4">
                                     
                                          <input type="text"
                                                   class = "smallDropDown"
                                                   ng-model="ssoLoginUrl"
                                                   name=""
                                                   ng-minlength="1"
                                                   ng-maxlength="80"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                                                   maxlength="80"
                                                   required
                                                   popover="{{ssoLoginUrl}}" 
                                                   popover-trigger="mouseenter"
                                                   ng-change="setSSOLoginUrl(ssoLoginUrl)">
                                      </div>

                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                Web Page Count
                                      </div>

                                      <div class = "col-md-4">
                                          <select ng-model="webPageCount"
                                                   class="appSettingsDropDowns"
                                                   ng-options="webPageCount for webPageCount in applicationSettingsArrays.webPageCount"
                                                   ng-change = "setWebPageCount(webPageCount)"
                                                   required>
                                                </select>
                                      </div>
                                      <div class = "col-md-2">
                                                Mobile Page Count
                                      </div>

                                      <div class = "col-md-4">
                                          <select ng-model="mobilePageCount"
                                                   class="appSettingsDropDowns"
                                                   ng-options="mobilePageCount for mobilePageCount in applicationSettingsArrays.mobilePageCount"
                                                   ng-change = "setMobilePageCount(mobilePageCount)"
                                                   required>
                                                </select>
                                      </div>

                                   </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                                Mobile Login Via
                                      </div>

                                      <div class = "col-md-4">
                                            <select ng-model="mobileLoginVia"
                                                   class="appSettingsDropDowns"
                                                   ng-options="mobileLoginVia.name for mobileLoginVia in applicationSettingsArrays.mobileLoginVia track by mobileLoginVia.value"
                                                   ng-change = "setMobileLoginVia(mobileLoginVia)"
                                                   required>
                                                </select>
                                      </div>
                                      <div class = "col-md-2">
                                                Is Multi Facility
                                      </div>

                                      <div class = "col-md-4">
                                            <select ng-model="isMultiFacility"
                                                   class="appSettingsDropDowns"
                                                   ng-options="isMultiFacility.name for isMultiFacility in applicationSettingsArrays.isMultiFacility track by isMultiFacility.value"
                                                   ng-change = "setIsMultiFacility(isMultiFacility)"
                                                   required>
                                                </select>
                                      </div>

                                   </div>
                              </div>


                   <!--            <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2">
                                               PlaCard Print
                                      </div>

                                      <div class = "col-md-4">

                                          <select ng-model="plaCardPrint"
                                                 class="appSettingsDropDowns"
                                                 ng-options="plaCardPrint for plaCardPrint in applicationSettingsArrays.plaCardPrints"
                                                 ng-change = "plaCardPrintChange(plaCardPrint)"
                                                 required>
                                          </select>
                                      </div>

                                      <div class = "col-md-2" ng-show="plaCardTemplateTypeShow">
                                               PlaCard Template Type
                                      </div>

                                      <div class = "col-md-4" ng-show="plaCardTemplateTypeShow">

                                          <select ng-model="plaCardTemplateType"
                                                 class="appSettingsDropDowns"
                                                 ng-options="plaCardTemplateType for plaCardTemplateType in applicationSettingsArrays.plaCardTemplateType"
                                                 ng-change = "setPlaCardTemplateType(plaCardTemplateType)"
                                                 ng-required="plaCardTemplateTypeShow">
                                          </select>
                                      </div>

                                   </div>
                              </div> -->

                       <!--        <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                      <div class = "col-md-2" ng-show="customizedTemplateShow">
                                               Upload Customized Template
                                      </div>
                                      <div class = "col-md-4" ng-show="customizedTemplateShow">
                                          <div class="fileinput fileinput-new input-group myProfileWidth82" data-provides="fileinput">
                                              <div class="form-control" data-trigger="fileinput">
                                              <span class="fileinput-filename"></span>
                                              </div>
                                              <span class="input-group-addon btn btn-default btn-file">
                                              <span class="fileinput-new">Select file</span>
                                              <span class="fileinput-exists"></span>
                                              <input type="file" id = "filenameforactivity" name="customizedTemplate" class="default"
                                              ng-model="customizedTemplate" ng-file-model="customizedTemplate"  accept=".png, .jpg, .jpeg" valid-file ng-required="customizedTemplateShow">
                                              </span>
                                          </div>
                                          <span class ="hintItallic">(Upload only <strong>A4 Size</strong> Template)</span>
                                      </div>

                                      <div class = "col-md-2" ng-show="defultLogoTemplateShow">
                                      Upload Company Logo
                                      </div>
                                      <div class = "col-md-4" ng-show="defultLogoTemplateShow">
                                            <div class="fileinput fileinput-new input-group myProfileWidth82" data-provides="fileinput">
                                            <div class="form-control" data-trigger="fileinput">
                                            <span class="fileinput-filename"></span>
                                            </div>
                                            <span class="input-group-addon btn btn-default btn-file">
                                            <span class="fileinput-new">Select file</span>
                                            <span class="fileinput-exists"></span>
                                            <input type="file" id = "filenameforactivity" name="DefaultLogo" class="default"
                                            ng-model="DefaultLogo" ng-file-model="DefaultLogo"  accept=".png, .jpg, .jpeg" valid-file ng-required="defultLogoTemplateShow">
                                            </span>
                                            </div>
                                            <span class ="hintItallic">(Upload Company Own logo)</span>
                                      </div>

                                   </div>
                              </div> -->

                                  </div>
                                </div>

                                <!-- <div class="panel panel-success">
                                        <div class="panel-heading">Automatic Message Trigger Level Setting</div>
                                            <div class="panel-body">

                                                <div class = "col-md-12">
                                                   <div class = "row tabContentAppSetting">
                                                    <div class="col-md-6">

<span ng-repeat="destinationPoint in destinationPoints track by $index">
    <div class = "input-group">
        <span class = "input-group-addon labelDestinationLocation">
              Level {{$index + 1}}
        </span>

        <multi-select-dropdown
                input-model="modernBrowsers"
                output-model="outputBrowsers"
                button-label="icon name"
                item-label="icon name maker"
                tick-property="ticked"
        >
        </multi-select-dropdown>

        <span class = "input-group-addon">
              <div class = "icon-map-marker mapMarkerIcon pointer"
                   ng-click="addMapDestination($index, origin)"
                   >
              </div>
        </span>
        <span class = "input-group-addon">
              <div class = "icon-remove-sign mapMarkerDelete pointer" ng-click="deleteDestinationLocation(destinationPoint.destination, $index)"></div>
        </span>
    </div>
</span>





                                                    </div>

                                                   </div>
                                                </div>

                                  </div>
                              </div>    -->

                                <div class="panel panel-success">
                                  <div class="panel-heading">Message Settings</div>
                                      <div class="panel-body">
                                          <div class = "col-md-12">
                                               <div class = "row tabContentAppSetting">
                                                    <div class = "col-md-2">
                                                        ETA Message Trigger (Minutes)
                                                    </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "etaSMS"
                                                       placeholder="ETA Messages Trigger Time(min)"
                                                       name = "etaSMS"
                                                       ng-change = "setETA(etaSMS)"
                                                       ng-class = "{error: appSettings.etaSMS.$invalid && !appSettings.etaSMS.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic">(ETA messages trigger time in <strong>minutes</strong>)</span>  -->
                                            </div>

                                            <div class = "col-md-2">
                                                Arrival Message Trigger (Meters)
                                            </div>
                                            <div class = "col-md-4">                                                                                           <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "geoFenceArea"
                                                       placeholder="Reached Message for Geofence Area(meter)"
                                                       name = "geoFenceArea"
                                                       ng-change = "setGeoFenceArea(geoFenceArea)"
                                                       ng-class = "{error: appSettings.geoFenceArea.$invalid && !appSettings.geoFenceArea.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic"> (Reached Message for Geofence Area in <strong>meters</strong>)</span>  -->

                                           </div>
                                       </div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">

                                            <div class = "col-md-2">
                                                Delay Message Trigger (Minutes)
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "delayMessage"
                                                       placeholder="Delay Message Time(min)"
                                                       name = "delayMessage"
                                                       ng-change = "setDelayMessage(delayMessage)"
                                                       ng-class = "{error: appSettings.delayMessage.$invalid && !appSettings.delayMessage.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic">(Delay message time in <strong>minutes</strong>)</span> -->
                                           </div>

                                            <div class = "col-md-2">
                                                Vehicle Waiting time
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "employeeWaitingTime"
                                                       placeholder="Employee Waiting Time(min)"
                                                       name = "employeeWaitingTime"
                                                       ng-change = "setWaitingTime(employeeWaitingTime)"
                                                       ng-class = "{error: appSettings.employeeWaitingTime.$invalid && !appSettings.employeeWaitingTime.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic">(Employee waiting time in <strong>minutes</strong>)</span> -->
                                           </div>

                                            <!-- <div class = "col-md-2">
                                                Customize the Text Messages
                                            </div>
                                            <div class = "col-md-4">
                                                  <input type= "button" class = "btn btn-xs btn-info "
                                                  ng-click = "customMessageText('lg')"
                                                  value = "Click">
                                           </div> -->


                                       </div>
                                   </div>

                                        <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Customize the Text Messages
                                            </div>
                                            <div class = "col-md-4">
                                                 <input type= "button" class = "btn btn-xs btn-info buttonStyleProfile"
                                                  ng-click = "customMessageText('lg')"
                                                  value = "Click Here">


                                            </div>
                                       </div>
                                   </div>
                                </div>
                              </div>
                            </div>

                           <div class="panel panel-success">
                                <div class="panel-heading">Employee Mobile App Settings</div>
                                      <div class="panel-body">
                                          <div class = "col-md-12">
                                             <div class = "row tabContentAppSetting">
                                                  <div class = "col-md-2">
                                                      Driver Picture
                                                  </div>
                                                  <div class = "col-md-4">
                                                      <select ng-model="driverImage"
                                                         class="appSettingsDropDowns"
                                                         ng-options="driverImage for driverImage in applicationSettingsArrays.driverImage"
                                                         ng-change = "setDriverImage(driverImage)"
                                                         required>
                                                      </select>
<!--                                                <span class ="hint" ng-show = "appSettings.autoTripStartGF.$invalid && !appSettings.autoTripStartGF.$pristine"></span>                                     -->
<!--                                              <br/>   <span class ="hintItallic">(Enter <strong>0</strong> to Disable this field)</span> -->
                                            </div>

                                            <div class = "col-md-2">
                                                Driver Mobile Number
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="driverMobileNuumber"
                                                   class="appSettingsDropDowns"
                                                   ng-options="driverMobileNuumber for driverMobileNuumber in  applicationSettingsArrays.driverMobileNuumber"
                                                   ng-change = "setDriverMobileNuumber(driverMobileNuumber)"
                                                   required>
                                                </select>
<!--                                                <span class ="hint" ng-show = "appSettings.autoTripEndGF.$invalid && !appSettings.autoTripEndGF.$pristine">* required maximum length of 3 digits.</span>                                       -->
<!--                                              <br/>   <span class ="hintItallic">(Enter <strong>0</strong> to Disable this field)</span> -->
                                            </div>
                                       </div>
                                   </div>

                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Driver Name
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="driverName"
                                                   class="appSettingsDropDowns"
                                                   ng-options="driverName for driverName in applicationSettingsArrays.driverName"
                                                   ng-change = "setDriverName(driverName)"
                                                   required>
                                                </select>
                                            </div>

                                            <div class = "col-md-2">
                                                Employee Call to Driver
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="employeeCallToDriver"
                                                   class="appSettingsDropDowns"
                                                   ng-options="employeeCallToDriver.name for employeeCallToDriver in applicationSettingsArrays.employeeCallToDriver track by employeeCallToDriver.value"
                                                   ng-change = "setEmployeeCallToDriver(employeeCallToDriver)"
                                                   required>
                                                </select>
                                            </div>
                                       </div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Employee Call to Transport Team
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="employeeCallToTransport"
                                                   class="appSettingsDropDowns"
                                                   ng-options="employeeCallToTransport.name for employeeCallToTransport in applicationSettingsArrays.employeeCallToTransport track by employeeCallToTransport.value"
                                                   ng-change = "setEmployeeCallToTransport(employeeCallToTransport)"
                                                   required>
                                                </select>
                                            </div>
                                             <div class = "col-md-2">
                                                Employee CheckOut Geofence Region(meter)
                                            </div>
                                           <div class = "col-md-4">
                                              <input class = "appSettingsDropDowns"
                                                     is-number-valid
                                                       maxlength="4"
                                                     oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                     ng-model = "employeeCheckOutGeofenceRegion"
                                                     placeholder=""
                                                     name = "employeeCheckOutGeofenceRegion"
                                                     ng-change = "setemployeeCheckOutGeofenceRegion(employeeCheckOutGeofenceRegion)"
                                                     ng-class = "{error: appSettings.employeeCheckOutGeofenceRegion.$invalid && !appSettings.employeeCheckOutGeofenceRegion.$pristine}"
                                                     required>
                                            </div>
                                       </div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                             <div class = "col-md-2">
                                                Employee Feedback Email
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="employeeFeedbackEmail"
                                                   class="appSettingsDropDowns"
                                                   ng-options="employeeFeedbackEmailStatus for employeeFeedbackEmailStatus in applicationSettingsArrays.employeeFeedbackEmail"
                                                   ng-change = "setEmployeeFeedbackEmail(employeeFeedbackEmail)"
                                                   required>
                                                </select>
                                            </div>
                                            <div class = "col-md-2" ng-if="employeeFeedbackEmailIdSection">
                                                Employee Feedback Email Ids
                                            </div>
                                             <div class = "col-md-4" ng-if="employeeFeedbackEmailIdSection">
                                                <input class = "appSettingsDropDowns"
                                                       type="text"
                                                       maxlength="200"
                                                       ng-maxlength=200
                                                       ng-max="200"
                                                       ng-model = "employeeFeedbackEmailId"
                                                       placeholder="Employee Feedback Email Id"
                                                       name = "employeeFeedbackEmailId"
                                                       ng-change = "setemployeeFeedbackEmailIds(employeeFeedbackEmailId)"
                                                       ng-class = "{error: appSettings.employeeFeedbackEmailId.$invalid && !appSettings.employeeFeedbackEmailId.$pristine}"
                                                       required> <br/>
                                           </div>
                                       </div>
                                   </div>
                                    <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                       <div class = "col-md-6"></div>
                                            <div class = "col-md-2" ng-if="employeeFeedbackEmailIdSection">
                                               To Employee Feed Back Email Ids
                                            </div>
                                             <div class = "col-md-4" ng-if="employeeFeedbackEmailIdSection">
                                                <input class = "appSettingsDropDowns"
                                                       type="text"
                                                       maxlength="200"
                                                       ng-maxlength=200
                                                       ng-max="200"
                                                       ng-model = "toEmployeeFeedBackEmail"
                                                       placeholder="To Employee Feedback Email Id"
                                                       name = "toEmployeeFeedBackEmail"
                                                       ng-change = "setToEmployeeFeedbackEmailIds(toEmployeeFeedBackEmail)"
                                                       ng-class = "{error: appSettings.toEmployeeFeedBackEmail.$invalid && !appSettings.toEmployeeFeedBackEmail.$pristine}"
                                                       required> <br/>
                                           </div>
                                       </div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                             <div class = "col-md-2">
                                                Employee App Report Bug
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="employeeAppReportBug"
                                                   class="appSettingsDropDowns"
                                                   ng-options="employeeAppReportBugStatus for employeeAppReportBugStatus in applicationSettingsArrays.employeeAppReportBug"
                                                   ng-change = "setEmployeeAppReportBug(employeeAppReportBug)"
                                                   required>
                                                </select>
                                            </div>
                                            <div class = "col-md-2" ng-if="reportBugEmailIdSection">
                                                Report bug email Ids
                                            </div>
                                             <div class = "col-md-4" ng-if="reportBugEmailIdSection">
                                                <input class = "appSettingsDropDowns"
                                                       type="text"
                                                       maxlength="200"
                                                       ng-maxlength=200
                                                       ng-max="200"
                                                       ng-model = "reportBugEmailIds"
                                                       placeholder="Report bug email Ids"
                                                       name = "reportBugEmailIds"
                                                       ng-change = "setReportBugEmailIds(reportBugEmailIds)"
                                                       ng-class = "{error: appSettings.reportBugEmailIds.$invalid && !appSettings.reportBugEmailIds.$pristine}"
                                                       required> <br/>
                                           </div>
                                       </div>
                                   </div>

                                 

                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                             <div class = "col-md-2">
                                                Employee CheckIn Via
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="employeeCheckInVia"
                                                   class="appSettingsDropDowns"
                                                   ng-options="employeeCheckInVia.name for employeeCheckInVia in applicationSettingsArrays.employeeCheckInVia track by employeeCheckInVia.value"
                                                   ng-change = "setEmployeeCheckInVia(employeeCheckInVia)"
                                                   required>
                                                </select>
                                            </div>
                                            
                                       </div>
                                   </div>

                                     <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Add Vehicle Type
                                            </div>
                                            <div class = "col-md-4">
                                                 <input type= "button" class = "btn btn-xs btn-info buttonStyleProfile"
                                                  ng-click = "addVehicleType(facilityData)"
                                                  value = "Click Here">


                                            </div>
                                       </div>
                                   </div>

                              </div>
                            </div>
<!--                                   <form name = "appSettings">    -->



<!--
                               <div class = "row mainTabApplicationSeetings">
                                   <div class = "col-md-12 tabAppMyProfile">
                                       <div class = "headingTab_appSettings msgSeeting">Dynamic Speed Settings</div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">

                                            <div class = "col-md-2">
                                                Maximum Speed Limit(kmph)
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       ng-pattern = "regexMin1to3Numbers"
                                                       ng-model = "maxSpeedArray"
                                                       placeholder="Maximum speed in limit(kmph)"
                                                       name = "maxSpeedArray"
                                                       ng-change = "setMaxSpeedArray(maxSpeedArray)"
                                                       ng-class = "{error: appSettings.maxSpeedArray.$invalid && !appSettings.maxSpeedArray.$pristine}"
                                                       required>
                                               <br/>  <span class ="hintItallic">(Enter <strong>0</strong> to Disable this field)</span>
                                           </div>
                                       </div>
                                   </div>
                               </div>
-->

                              <div class="panel panel-success">
                                  <div class="panel-heading">Driver Mobile App Settings</div>
                                        <div class="panel-body">
 <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">

                                            <div class = "col-md-2">
                                                Driver Auto Checked-Out Time (Hours)
                                            </div>
                                            <div class = "col-md-4">

                                            <timepicker ng-model="driverCheckedout"
                                                        hour-step="hstep"
                                                        ng-change = setDriverCheckedout(driverCheckedout)
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                            </timepicker>
                                                <!-- <input class = "appSettingsDropDowns"
                                                       ng-pattern = "regexMin1to3Numbers"
                                                       ng-model = "driverCheckedout"
                                                       placeholder="Driver Auto Checked-Out Time(hours)"
                                                       name = "driverCheckedout"
                                                       ng-change = "setDriverCheckedout(driverCheckedout)"
                                                       ng-class = "{error: appSettings.driverCheckedout.$invalid && !appSettings.driverCheckedout.$pristine}"
                                                       required> -->
                                                <br/> <!-- <span class ="hintItallic">(Enter driver Auto Checked-Out Time in <strong>hours</strong>)</span>    -->
                                           </div>

                                            <div class = "col-md-2">
                                                Profile Picture
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="profilePic"
                                                   class="appSettingsDropDowns"
                                                   ng-options="profilePic for profilePic in applicationSettingsArrays.profilePic"
                                                   ng-change = "setProfilePic(profilePic)"
                                                   required>
                                                </select>
                                                <br/> <!-- <span class ="hintItallic">( Driver profilr picture <strong>on device</strong>)</span> -->
                                           </div>

                                       </div>
                                   </div>

                                              <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Doing Driver SMS, After 13 Hours
                                            </div>
                                            <div class = "col-md-4">
                                                <input type = "text"
                                                       class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="17"
                                                       maxlength="17"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "after13HrSMSContact"
                                                       placeholder="Enter Mobile Number"
                                                       name = "mobileNumber13"
                                                       ng-change = "setAfter13HrSMSContact(after13HrSMSContact)"
                                                       ng-class = "{error: appSettings.mobileNumber13.$invalid && !appSettings.mobileNumber13.$pristine}"
                                                       required>
<!--                                                <span class ="hint" ng-show = "appSettings.mobileNumber13.$invalid && !appSettings.mobileNumber13.$pristine">*Invalid Mobile Number</span> -->
                                             <br/>    <!-- <span class ="hintItallic">(Ex : 919999999999 or 19999999999)</span> -->
                                           </div>

                                            <div class = "col-md-2">
                                                Doing Driver SMS, After 14 Hours
                                            </div>
                                            <div class = "col-md-4">
                                                <input type = "text"
                                                       class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="17"
                                                       maxlength="17"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "after14HrSMSContact"
                                                       placeholder="Enter Mobile Number"
                                                       name = "after14HrSMSContact"
                                                       ng-change = "setAfter14HrSMSContact(after14HrSMSContact)"
                                                       ng-class = "{error: appSettings.after14HrSMSContact.$invalid && !appSettings.after14HrSMSContact.$pristine}"
                                                       required>
<!--                                                <span class ="hint" ng-show = "appSettings.after14HrSMSContact.$invalid && !appSettings.after14HrSMSContact.$pristine">*Invalid Mobile Number</span>-->
                                               <br/> <!--  <span class ="hintItallic">(Ex : 919999999999 or 19999999999)</span>   -->
                                           </div>

                                       </div>
                                   </div>


                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">

                                            <div class = "col-md-2">
                                                Auto call and sms disabling
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="autoCallSmsDisable"
                                                   class="appSettingsDropDowns"
                                                   ng-options="autoCallSmsDisable for autoCallSmsDisable in applicationSettingsArrays.autoCallSmsDisable"
                                                   ng-change = "setAutoCallSmsDisable(autoCallSmsDisable)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>

                                           <div class = "col-md-2">
                                                Maximum Speed Limit (Kilometers/hours)
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="2"
                                                       maxlength="2"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "maxSpeedArray"
                                                       placeholder="Maximum speed in limit(kmph)"
                                                       name = "maxSpeedArray"
                                                       ng-change = "setMaxSpeedArray(maxSpeedArray)"
                                                       ng-class = "{error: appSettings.maxSpeedArray.$invalid && !appSettings.maxSpeedArray.$pristine}"
                                                       required>
                                               <br/>  <!-- <span class ="hintItallic">(Maximum Speed Limit in <strong>KMPH</strong>)</span> -->
                                           </div>
                                       </div>
                                   </div>

                                   <div class = "col-md-12">
                                        <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                                Auto Driver Checkout
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="driverAutoCheckoutStatus"
                                                   class="appSettingsDropDowns"
                                                   ng-options="autoDriverCheckout for autoDriverCheckout in applicationSettingsArrays.autoDriverCheckout"
                                                   ng-change = "setAutoDriverCheckout(driverAutoCheckoutStatus)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>

                                            <div class = "col-md-2">
                                                Auto Vehicle Allocation
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="autoVehicleAllocationStatus"
                                                   class="appSettingsDropDowns"
                                                   ng-options="autoVehicleAllocation for autoVehicleAllocation in applicationSettingsArrays.autoVehicleAllocation"
                                                   ng-change = "setAutoVehicleAllocation(autoVehicleAllocationStatus)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>
                                          </div>
                                       </div>

                                        <div class = "col-md-12">
                                        <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                                Trip to driver personal device Via SMS
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="personalDeviceViaSms"
                                                   class="appSettingsDropDowns"
                                                   ng-options="personalDeviceViaSms for personalDeviceViaSms in applicationSettingsArrays.personalDeviceViaSms"
                                                   ng-change = "setPersonalDeviceViaSms(personalDeviceViaSms)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>

                                            <div class = "col-md-2">
                                                Assign routes to vendor before vehicle allocation.
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="assignRoutesToVendor"
                                                   class="appSettingsDropDowns"
                                                   ng-options="assignRoutesToVendor for assignRoutesToVendor in applicationSettingsArrays.assignRoutesToVendor"
                                                   ng-change = "setAssignRoutesToVendor(assignRoutesToVendor)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>
                                          </div>
                                       </div>

                                       <div class = "col-md-12">
                                        <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                                Driver Call to employee(Number masking)
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="driverCallToEmployee"
                                                   class="appSettingsDropDowns"
                                                   ng-options="driverCallToEmployee.name for driverCallToEmployee in applicationSettingsArrays.driverCallToEmployee track by driverCallToEmployee.value"
                                                   ng-change = "setDriverCallToEmployee(driverCallToEmployee)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>

                                            <div class = "col-md-2">
                                                Driver Call to transport desk
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="driverCallToTransportDesk"
                                                   class="appSettingsDropDowns"
                                                   ng-options="driverCallToTransportDesk.name for driverCallToTransportDesk in applicationSettingsArrays.driverCallToTransportDesk track by driverCallToTransportDesk.value"
                                                   ng-change = "setDriverCallToTransportDesk(driverCallToTransportDesk)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>
                                          </div>
                                       </div>

                              </div>
                          </div>

                          <div class="panel panel-success">
                                <div class="panel-heading">Invoice Settings</div>
                                      <div class="panel-body">
                                          <div class = "col-md-12">
                                             <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                              Invoice digits required
                                            </div>
                                           <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "invoiceNumberRange"
                                                       placeholder="Invoice digits required"
                                                       name = "invoiceNumberRange"
                                                       ng-change = "setInvoiceNumberRange(invoiceNumberRange)"
                                                       ng-class = "{error: appSettings.invoiceNumberRange.$invalid && !appSettings.invoiceNumberRange.$pristine}"
                                                       required>
                                               <br/>
                                           </div>

                                              <div class="col-md-2">Invoice Working Days</div>
                                              <div class="col-md-4">
                                                      <input class="appSettingsDropDowns"
                                                      type="text"
                                                       ng-model="invoiceNoOfDay"
                                                       placeholder="Invoice Working Days"
                                                       name="empSecondNormalPickup"
                                                       is-number-valid
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       ng-change="setInvoiceWorkingDays(invoiceNoOfDay)"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       required> <br/>
                                              </div>
                                       </div>
                                   </div>

                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class="col-md-2">Invoice Generation Frequency</div>
                                                <div class="col-md-4">
                                                  <select ng-model="invoiceGenDate"
                                                          class = "smallDropDown"
                                                          ng-options="invoiceGenDate for invoiceGenDate in applicationSettingsArrays.invoiceGenDateArray"
                                                          ng-change = "setinvoiceGenDate(invoiceGenDate)">
                                                  </select>
                                                  </div>
                                            <div class = "col-md-2">
                                                Distance
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="distanceFlg"
                                                   class="appSettingsDropDowns"
                                                   ng-options="distanceArray for distanceArray in applicationSettingsArrays.distanceArray"
                                                   ng-change = "setdistance(distanceFlg)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>
                                       </div>
                                   </div>



                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class="col-md-2">Invoice Generation Type</div>
                                                <div class="col-md-4">
                                                  <select ng-model="invoiceGenType"
                                                          class = "smallDropDown"
                                                          ng-options="invoiceGenType for invoiceGenType in applicationSettingsArrays.invoiceGenTypeArray"
                                                          ng-change = "setInvoiceGenType(invoiceGenType)"
                                                          required>
                                                  </select>
                                                  </div>
                                       </div>
                                   </div>
                              </div>
                            </div>

                          <div class="panel panel-success">
                                <div class="panel-heading">Geo fencing Settings</div>
                                     <div class="panel-body">
                                          <div class = "col-md-12">
                                                <div class = "row tabContentAppSetting">
                                                    <div class = "col-md-2">
                                                        Auto Trip Start (Meters)
                                                    </div>
                                                    <div class = "col-md-4">
                                                        <input class = "appSettingsDropDowns"
                                                               is-number-valid
                                                               ng-minlength="0"
                                                               ng-maxlength="3"
                                                               maxlength="3"
                                                               oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                               ng-model = "autoTripStartGF"
                                                               placeholder="Auto Trip Start Gefence Area(meter)"
                                                               name = "autoTripStartGF"
                                                               ng-change = "setAutoTripStartGF(autoTripStartGF)"
                                                               ng-class = "{error: appSettings.autoTripStartGF.$invalid && !appSettings.autoTripStartGF.$pristine}"
                                                               required>
        <!--                                                <span class ="hint" ng-show = "appSettings.autoTripStartGF.$invalid && !appSettings.autoTripStartGF.$pristine"></span>                                     -->
                                                      <br/>   <!-- <span class ="hintItallic">(Auto Trip Start Geofence Area in <strong>meters</strong>)</span>  -->
                                                    </div>

                                                    <div class = "col-md-2">
                                                        Auto Trip Close (Meters)
                                                    </div>
                                                    <div class = "col-md-4">
                                                        <input class = "appSettingsDropDowns"
                                                               is-number-valid
                                                               ng-minlength="0"
                                                               ng-maxlength="3"
                                                               maxlength="3"
                                                               oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                               ng-model = "autoTripEndGF"
                                                               placeholder="Auto Trip End Gefence Area(meter)"
                                                               name = "autoTripEndGF"
                                                               ng-change = "setAutoTripEndGF(autoTripEndGF)"
                                                               ng-class = "{error: appSettings.autoTripEndGF.$invalid && !appSettings.autoTripEndGF.$pristine}"
                                                               required>

                                                      <br/>
                                                    </div>
                                                </div>
                                          </div>
                                      </div>
                                </div>

                                <div class="panel panel-success">
                                <div class="panel-heading">Driver/Vehicle Compliance</div>
                                     <div class="panel-body">
                                        <div ng-include = "'partials/modals/vehicle_DriverCompliance.jsp'" ></div>
                                      </div>
                                </div>



                               <div class="panel panel-success">
                                <div class="panel-heading">Back To Back Route Settings</div>
                                     <div class="panel-body">
                                          <div class = "col-md-12">
                                                <div class = "row tabContentAppSetting">
                                                    <div class = "col-md-2">
                                                     <br/>
                                                     <br/>
                                                        Driver Waiting time at last drop location
                                                    </div>
                                                    <div class = "col-md-4">
                                                        <timepicker ng-model="driverWaitingTime"
                                                                      hour-step="hstep"
                                                                      ng-change="setdriverWaitingTime(driverWaitingTime)"
                                                                      minute-step="mstep"
                                                                      show-meridian="ismeridian"
                                                                      readonly-input = 'true'
                                                                      class = "timepicker2_empReq floatLeft"
                                                                      required>
                                                         </timepicker>

                                                      <br/>
                                                    </div>

                                                    <div class = "col-md-2">
                                                        <br/>
                                                        <br/>
                                                        Back To Back Type By Distance Or TravelTime
                                                    </div>
                                                    <div class = "col-md-4">
                                                        <br/>
                                                        <br/>
                                                        <select ng-model="backToBackType"
                                                                class = "smallDropDown"
                                                                ng-options="pickDrop for pickDrop in applicationSettingsArrays.backToBackType"
                                                                ng-change = "setBackToBackType(backToBackType)"
                                                                required>
                                                        </select>
                                                      <br/>
                                                    </div>
                                                </div>
                                          </div>
                                          <div class = "col-md-12">
                                                <div class = "row tabContentAppSetting">
                                                  <div ng-show="backToBackType == 'Distance'">
                                                    <div class = "col-md-2">
                                                        Back To Back by travel distance in KM
                                                    </div>
                                                    <div class = "col-md-4">
                                                          <input type="number"
                                                                class="appSettingsDropDowns"
                                                                name=""
                                                                ng-minlength="1"
                                                                ng-maxlength="3"
                                                                oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                maxlength="3"
                                                                ng-change = setb2bInDistanceKm(b2bByTravelDistanceInKM)
                                                                ng-model="b2bByTravelDistanceInKM"
                                                                required
                                                          >

                                                      <br/>
                                                    </div>
                                                    </div>
                                                  <div ng-show="backToBackType == 'Time'">
                                                    <div class = "col-md-2">
                                                       <br/>
                                                       <br/>
                                                       Back To Back By travel Time In Minutes
                                                    </div>
                                                    <div class = "col-md-4">
                                                         <timepicker ng-model="b2bByTravelTimeInMinutes"
                                                                      hour-step="hstep"
                                                                      ng-change = setb2bInMinutes(b2bByTravelTimeInMinutes)
                                                                      minute-step="mstep"
                                                                      show-meridian="ismeridian"
                                                                      readonly-input = 'true'
                                                                      class = "timepicker2_empReq floatLeft"
                                                                      required>
                                                         </timepicker>
                                                      <br/>
                                                    </div>
                                                  </div>
                                                </div>
                                          </div>
                                      </div>
                                </div>

                                <div class="panel panel-success">
                                <div class="panel-heading">Request Settings</div>
                                     <div class="panel-body">
                                          <div class = "col-md-12">
                                                <div class = "row tabContentAppSetting">
                                                   <div class = "col-md-2">
                                                      Trip Type
                                                    </div>
                                                   <div class = "col-md-4">
                                                        <select ng-model="pickDropNeeded"
                                                                class = "smallDropDown"
                                                                ng-options="pickDrop for pickDrop in applicationSettingsArrays.pickDropArray"
                                                                ng-change = "setPickDropNeeded(pickDropNeeded)">
                                                        </select>
                                                   </div>
                                                   <div class = "col-md-2">
                                                      ADHOC Request Time Picker
                                                    </div>
                                                   <div class = "col-md-4">
                                                          <select ng-model="adhocTimePicker"
                                                           class="appSettingsDropDowns"
                                                           ng-options="adhoc for adhoc in applicationSettingsArrays.adhocTimePickers"
                                                           ng-change = "setAdhocTimePicker(adhocTimePicker)"
                                                           required>
                                                        </select>
                                                       <br/>
                                                   </div>
                                                </div>
                                          </div>

                                          <div class = "col-md-12" >
                                       <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                               Cut Off Time
                                            </div>

                                        <div class = "col-md-4">

                                        <select ng-model="cutOff.cutOffTime"
                                                 class="requestDetailFirstRow appSettingsDropDowns"
                                                 ng-options="cutOffTime.value as cutOffTime.text for cutOffTime in cutOffTimes"

                                                 required>
                                          </select>

                                          </div>
                                          <div class = "col-md-2">
                                            Auto roster copy
                                          </div>
                                         <div class = "col-md-4">
                                              <select ng-model="autoDropRoster"
                                                      class = "smallDropDown"
                                                      ng-options="autoDropRoster for autoDropRoster in applicationSettingsArrays.autoDropRosterArray"
                                                      ng-change = "setAutoDropRoster(autoDropRoster)">
                                              </select>
                                         </div>
                                          <br>
                                          <br>
                                          <span class ="hintItallicCutOffTime"  ng-show="cutOff.cutOffTime =='S'">(<strong>*</strong>Configure cutOff Time at <strong>Shift Time Tab</strong>)</span>



                                      </div>


                               </div>



                              <div class = "col-md-12"  ng-show="cutOff.cutOffTime =='T'">
                                   <div class = "row tabContentAppSetting">
                                       <div class = "col-md-2">
                                          <br/>
                                          <br/>
                                          Booking Cut-off Time Drop
                                        </div>
                                       <div class = "col-md-4">
                                         <timepicker ng-model="dropPriorTime"
                                                        hour-step="hstep"
                                                        ng-change = setDropPriorTime(dropPriorTime)
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>

                                       <div class = "col-md-2">
                                          <br/>
                                          <br/>
                                          Booking Cutoff Time Pickup
                                        </div>
                                       <div class = "col-md-4">
                                       <timepicker ng-model="pickupPriorTime"
                                                        hour-step="hstep"
                                                        minute-step="mstep"
                                                        ng-change = "setPickupPriorTime(pickupPriorTime)"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>
                                  </div>
                              </div>

                                 <div class = "col-md-12" ng-show="cutOff.cutOffTime =='T'">
                                   <div class = "row tabContentAppSetting">
                                       <div class = "col-md-2">
                                          <br/>
                                          <br/>
                                          Cancel CutOff Time Drop
                                        </div>
                                       <div class = "col-md-4">
                                         <timepicker ng-model="dropCancelCutOffTime"
                                                        hour-step="hstep"
                                                        ng-change = setdropCancelCutOffTime(dropCancelCutOffTime)
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>

                                       <div class = "col-md-2" ng-show="cutOff.cutOffTime =='T'">
                                          <br/>
                                          <br/>
                                          Cancel CutOff Time Pickup
                                        </div>
                                       <div class = "col-md-4">
                                       <timepicker ng-model="pickupCancelCutOffTime"
                                                        hour-step="hstep"
                                                        minute-step="mstep"
                                                        ng-change = "setpickupCancelCutOffTime(pickupCancelCutOffTime)"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>
                                  </div>
                              </div>

                                <div class = "col-md-12" ng-show="cutOff.cutOffTime =='T'">
                                   <div class = "row tabContentAppSetting">

                                      <div class = "col-md-2">
                                          <br/>
                                          <br/>
                                          Reschedule CutOff Drop Time
                                        </div>

                                       <div class = "col-md-4">
                                       <timepicker ng-model="rescheduleDrop"
                                                        hour-step="hstep"
                                                        minute-step="mstep"
                                                        ng-change = "setrescheduleDropCutOffTime(rescheduleDrop)"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>

                                       <div class = "col-md-2" ng-show="cutOff.cutOffTime =='T'">
                                          <br/>
                                          <br/>
                                            Reschedule CutOff Pickup Time
                                        </div>

                                       <div class = "col-md-4">
                                         <timepicker ng-model="reschedulePickup"
                                                        hour-step="hstep"
                                                        ng-change = setreschedulePickupCutOffTime(reschedulePickup)
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>


                                  </div>
                              </div>




                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">

                                       <div class = "col-md-2">
                                          Shift time difference pickup to drop (Hours)
                                        </div>
                                       <div class = "col-md-4">


                                       <timepicker ng-model="shiftTimeDifference"
                                                        hour-step="hstep"
                                                        ng-change ="setShiftTimeDifference(shiftTimeDifference)"
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>

                                       <!-- <input class = "appSettingsDropDowns"
                                              ng-pattern = "regexMin1to3Numbers"
                                              ng-model = "shiftTimeDifference"
                                              placeholder="Shift time difference pickup to drop"
                                              name = "shiftTimeDifference"
                                              ng-change = "setShiftTimeDifference(shiftTimeDifference)"
                                              ng-class = "{error: appSettings.shiftTimeDifference.$invalid && !appSettings.shiftTimeDifference.$pristine}"
                                              required>  -->
                                           <br/>
                                       </div>

                                       <div class = "col-md-2">
                                         Add distance in each trip to recover the geo fence region (kilometers)
                                        </div>
                                       <div class = "col-md-4">
                                           <input class = "appSettingsDropDowns"
                                                   is-number-valid
                                                   ng-minlength="0"
                                                   ng-maxlength="3"
                                                   maxlength="3"
                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                   ng-model = "addDistanceRecoverGeoFence"
                                                   placeholder="Pickup Prior Time"
                                                   name = "addDistanceRecoverGeoFence"
                                                   ng-change = "setAddDistanceRecoverGeoFence(addDistanceRecoverGeoFence)"
                                                   ng-class = "{error: appSettings.addDistanceRecoverGeoFence.$invalid && !appSettings.addDistanceRecoverGeoFence.$pristine}"
                                                   required>
                                           <br/>  <!-- <span class ="hintItallic">(Geo fence region distance in <strong>km</strong>)</span>  -->
                                       </div>

                                  </div>
                              </div>

                              <!-- <div class = "col-md-12" ng-show="cutOff.cutOffTime =='T'">
                                   <div class = "row tabContentAppSetting">

                                      <div class = "col-md-2">

                                            Inform to employee via
                                        </div>

                                       <div class = "col-md-4">
                                       <select ng-model="notificationType"
                                                          class = "smallDropDown"
                                                          ng-options="notificationType for notificationType in applicationSettingsArrays.notificationType"
                                                          ng-change = "setNotificationType(notificationType)"
                                                          required>
                                                  </select>
                                           <br/>
                                       </div>

                                  </div>
                              </div>     -->
             <div class = "col-md-12" >
                                       <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                               Cut Off Notification
                                            </div>

                                        <div class = "col-md-4">

                                        <select ng-model="cutOffNote.cutOffTime"
                                                 class="requestDetailFirstRow appSettingsDropDowns"
                                                 ng-options="cutOffNotification.value as cutOffNotification.text for cutOffNotification in cutOffNotifications"
                                                 ng-change = "setCutOfNotificationType(cutOffNote.cutOffTime)">
                                                 <option value="">None</option>
                                          </select>

                                          </div>
                                          <div class = "col-md-2">
                                            
                                          </div>
                                         <div class = "col-md-4">
                                            
                                         </div>
                                      </div>


                               </div>
                              <div class = "col-md-12" ng-show="cutOffNote.cutOffTime =='T'">
                                   <div class = "row tabContentAppSetting">

                                      <div class = "col-md-2">
                                          <br/>
                                          <br/>
                                            Roster/Request Cutoff Time For Pickup
                                        </div>

                                       <div class = "col-md-4">
                                       <timepicker ng-model="notificationCutoffTimePickup"
                                                        hour-step="hstep"
                                                        minute-step="mstep"
                                                        ng-change = "setNotificationCutoffTimePickup(notificationCutoffTimePickup)"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>

                                       <div class = "col-md-2" ng-show="cutOff.cutOffTime =='T'">
                                          <br/>
                                          <br/>
                                            Roster/Request Cutoff Time For Drop
                                        </div>

                                       <div class = "col-md-4">
                                         <timepicker ng-model="notificationCutoffTimeDrop"
                                                        hour-step="hstep"
                                                        ng-change = setNotificationCutoffTimeDrop(notificationCutoffTimeDrop)
                                                        minute-step="mstep"
                                                        show-meridian="ismeridian"
                                                        readonly-input = 'true'
                                                        class = "timepicker2_empReq floatLeft">
                                         </timepicker>
                                           <br/>
                                       </div>


                                  </div>
                              </div>

                              <div class = "col-md-12">
                                   <div class = "row tabContentAppSetting">
                                          <div class="col-md-2">Employee second normal Pickup
                                                                                request</div>
                                              <div class="col-md-4">
                                                <input class="appSettingsDropDowns"
                                                       type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model="empSecondNormalPickup"
                                                       placeholder="Employee second normal Pickup request"
                                                       name="empSecondNormalPickup"
                                                       ng-change="setEmpSecondNormalPickup(empSecondNormalPickup)"
                                                       ng-class="{error: appSettings.empSecondNormalPickup.$invalid && !appSettings.empSecondNormalPickup.$pristine}"
                                                       required> <br />
                                                        <!-- <span class="hintItallic">(Employee second normal
                                                  <strong>PICKUP</strong> request difference in hours)
                                                </span> -->
                                              </div>

                                              <div class="col-md-2">Employee second normal DROP
                                                                                    request</div>
                                                  <div class="col-md-4">
                                                    <input class="appSettingsDropDowns"
                                                           type="number"
                                                           ng-minlength="0"
                                                           ng-maxlength="3"
                                                           maxlength="3"
                                                           oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                           ng-model="empSecondNormalDrop"
                                                           placeholder="Employee second normal DROP request"
                                                           name="empSecondNormalDrop"
                                                           ng-change="setEmpSecondNormalDrop(empSecondNormalDrop)"
                                                           ng-class="{error: appSettings.empSecondNormalDrop.$invalid && !appSettings.empSecondNormalDrop.$pristine}"
                                                           required> <br />
                                                           <!-- <span class="hintItallic">(Employee second normal
                                                          <strong>DROP</strong> request difference in hours)
                                                    </span> -->
                                                  </div>
                                           </div>
                                  </div>
                                  <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">

                                            <div class="col-md-2">Request Type</div>
                                                <div class="col-md-4">
                                                   <select ng-model="requestType"
                                                            class="appSettingsDropDowns"
                                                            ng-change="setRequestType(requestType)"
                                                            ng-options="v.number as (v.label) group by v.group for v in list">
                                                  </select>

                                                  </div>
                                          <div ng-show="requestType== '1'">
                                            <div class = "col-md-2">
                                               No Of Days
                                            </div>
                                            <div class = "col-md-4">
                                              <input type="number"
                                                      ng-model="requestCutOffNoOfDays"
                                                      class="appSettingsDropDowns"
                                                      name=""
                                                      type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                      ng-change = "setRequestDays(requestCutOffNoOfDays)"
                                                      placeholder="Number Of Days">
                                                <br/> <span class="hintItallic">*Next 5 days from today</span>
                                            </div>

                                           </div>

                                            <div ng-show="requestType== '2'">
                                            <div class = "col-md-2">
                                               Dynamic Days
                                            </div>
                                            <div class = "col-md-4">                                                      <am-multiselect class="input-lg dynamicDaysInput"
                                                                multiple="true"
                                                                ms-selected ="Day(s) selected"
                                                                ng-model="dynamicDays"
                                                                ms-header="Select Days"
                                                                options="days.name for days in daysRequestDetails"
                                                                change="setDynamicDays(dynamicDays)">
                                                </am-multiselect>
                                                 <br/> <span class="hintItallic">* Request to be allowed days</span>
                                            </div>
                                        </div>



                                        <div ng-show="requestType== '2'">
                                           <div class = "col-md-2">
                                               From Date
                                            </div>
                                            <div class = "col-md-4">
                                              <div class = "input-group calendarInputInvoice">
                                                <span class="input-group-btn myProfileMarginLeft185">
                                                <button class="btn btn-default" ng-click="openFromDateCalFrom($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>
                                                 <input class="calenderInputStyleProfile requestTypeProfileDropdown
                                                 "
                                                       ng-model = "requestFromDateCutOff"
                                                       placeholder = "Start Request Date"
                                                       ng-change = "setRequestDateFrom(requestFromDateCutOff)"
                                                       datepicker-popup = '{{format}}'
                                                       is-open="datePicker.fromDate"
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "fromDate"
                                                       readonly

                                                      >
                                                      </div>
                                                <br/>
                                           </div>

                                           </div>

                                           <div ng-show="requestType== '2'">
                                           <div class = "col-md-2">
                                               To Date
                                            </div>
                                            <div class = "col-md-4">
                                              <div class = "input-group calendarInputInvoice">
                                                <span class="input-group-btn myProfileMarginLeft185">
                                                <button class="btn btn-default" ng-click="openFromDateCalTo($event)">
                                                    <i class = "icon-calendar calInputIcon"></i></button></span>
                                                 <input class="calenderInputStyleProfile requestTypeProfileDropdown
                                                 "
                                                       ng-model = "requestToDateCutOff"
                                                       placeholder = "End Request Date"
                                                       ng-change = "setRequestDateTo(requestToDateCutOff)"
                                                       datepicker-popup = '{{format}}'
                                                       is-open="datePicker.toDate"
                                                       show-button-bar = false
                                                       show-weeks=false
                                                       datepicker-options = 'dateOptions'
                                                       name = "toDate"
                                                       readonly
                                                      >
                                                      </div>

                                                      Occurence * <input type="checkbox" ng-change="setOccurence(occurenceCutOff)" ng-model="occurenceCutOff" name="">
                                                      <!-- <br/> <span class="hintItallic">* No of days </span> -->
                                                <br/>
                                           </div>

                                           </div>

                                           <div ng-show="requestType== '2'" >
                                                  <div class = "col-md-2">
                                                      Early Request Date
                                                  </div>
                                                  <div class = "col-md-4">
                                                      <input type="number"
                                                             class="appSettingsDropDowns"
                                                             name="earlyRequestCust"
                                                             ng-model="earlyRequestDateCustomizeDate"
                                                             min="1"
                                                             placeholder="Early Request Date"
                                                             max="99"
                                                             required
                                                             ng-change="setEarlyRequestDateCustomizeDate(earlyRequestDateCustomizeDate)"
                                                             ng-class = "{error: appSettings.earlyRequestCust.$invalid && !appSettings.earlyRequestCust.$pristine}"
                                                             >
                                                      <br>
                                                        <span class="hintItallic" ng-show="!appSettings.earlyRequestCust.$invalid">* Early Request to be open before from date</span>

                                                        <span class="hintItallic error" ng-show="appSettings.earlyRequestCust.$invalid && !appSettings.earlyRequestCust.$pristine">Kinly Enter early request greater than 0</span>
                                                  </div>

                                           </div>

                                           <div ng-show="requestType == '3'" >
                                           <div class = "col-md-2">
                                                      Early Request Date
                                                  </div>
                                                  <div class = "col-md-4">

                                                <input type="number"
                                                             class="appSettingsDropDowns"
                                                             name="earlyRequestEveryMonth"
                                                             ng-model="earlyRequestDateEveryMonth"
                                                             min="1"
                                                             placeholder="Early Request Date"
                                                             max="99"
                                                             required
                                                             ng-change="setEarlyRequestDateEveryMonth(earlyRequestDateEveryMonth)"
                                                             ng-class = "{error: appSettings.earlyRequestEveryMonth.$invalid && !appSettings.earlyRequestEveryMonth.$pristine}"
                                                             >
                                                      <br>
                                                        <span class="hintItallic" ng-show="!appSettings.earlyRequestEveryMonth.$invalid">* Early Request to be open before from date</span>

                                                        <span class="hintItallic error" ng-show="appSettings.earlyRequestEveryMonth.$invalid && !appSettings.earlyRequestEveryMonth.$pristine">Kinly Enter early request greater than 0</span>


                                                  </div>

                                           </div>

                                           <div ng-show="requestType == '4'">
                                           <div class = "col-md-2">
                                               Per Day Request(With Occurence)
                                            </div>

                                            <div class = "col-md-4">
                                              <select ng-model="singleDay"
                                                   class="appSettingsDropDowns"
                                                   ng-options="singleDay.value as singleDay.name for singleDay in applicationSettingsArrays.singleDay"
                                                   ng-change = "setSingleDay(singleDay)"
                                                   required>
                                                </select>

                                           </div>
                                           </div>
                                           <br/>

                                           <div ng-show="requestType == '4'">
                                           <br/>
                                          <div class = "col-md-2">
                                                      Early Request Day
                                                  </div>
                                                  <div class = "col-md-4">
                                                  <select ng-model="earlyRequestDatePerDay"
                                                           class="appSettingsDropDowns"
                                                           ng-options="earlyRequestDate.value as earlyRequestDate.name for earlyRequestDate in applicationSettingsArrays.earlyRequestDate"
                                                           ng-change = "setEarlyRequestDatePerDay(earlyRequestDatePerDay)"
                                                           required>
                                                  </select>

                                                      <!-- <input type="text"
                                                              class="appSettingsDropDowns"
                                                              placeholder="Early Request Date"
                                                              name=""
                                                              ng-change="setEarlyRequestDate(earlyRequestDate)"
                                                              ng-model="earlyRequestDate"> -->
                                                  </div>
                                           </div>

                                           <div ng-show="requestType == '5'">
                                           <br/>
                                           <div class = "col-md-2">
                                               Start Day
                                            </div>

                                            <div class = "col-md-4">
                                              <select ng-model="multipleDaysStart"
                                                   class="appSettingsDropDowns"
                                                   ng-options="singleDay.value as singleDay.name for singleDay in applicationSettingsArrays.singleDay"
                                                   ng-change = "setMultipleDay(multipleDaysStart,multipleDaysEnd)"
                                                   required>
                                                </select>

                                           </div>

                                           <div class = "col-md-2">
                                               End Day
                                            </div>

                                            <div class = "col-md-4">
                                              <select ng-model="multipleDaysEnd"
                                                   class="appSettingsDropDowns"
                                                   ng-options="singleDay.value as singleDay.name for singleDay in applicationSettingsArrays.singleDay"
                                                   ng-change = "setMultipleDay(multipleDaysStart,multipleDaysEnd)"
                                                   required>
                                                </select>

                                           </div>
                                           </div>
                                           <br/>
                                           <div ng-show="requestType== '1'">
                                            <div class = "col-md-2">
                                               Dynamic Days
                                            </div>
                                            <div class = "col-md-4">                                                      <am-multiselect class="input-lg dynamicDaysInput"
                                                                multiple="true"
                                                                ms-selected ="Day(s) selected"
                                                                ng-model="dynamicDays"
                                                                ms-header="Select Days"
                                                                options="days.name for days in daysRequestDetails"
                                                                change="setDynamicDays(dynamicDays)">
                                                </am-multiselect>
                                                 <br/> <span class="hintItallic">* Request to be allowed days</span>
                                            </div>
                                           </div>


                                       </div>

                                   </div>
                                  <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class="col-md-2">Destination Point Limit</div>
                                                <div class="col-md-4">
                                                  <select ng-model="destinationPointLimit"
                                                          class = "smallDropDown"
                                                          ng-options="destinationPointLimit for destinationPointLimit in applicationSettingsArrays.destinationPointLimitArray"
                                                          ng-change = "setDestinationPointLimit(destinationPointLimit)"
                                                          required>
                                                  </select>
                                                  </div>


                                            <div class = "col-md-2">
                                                Location Visible
                                            </div>
                                            <div class = "col-md-4">
                                                <select ng-model="locationVisible"
                                                   class="appSettingsDropDowns"
                                                   ng-options="locationVisible.value as locationVisible.name for locationVisible in applicationSettingsArrays.locationVisibleArray"
                                                   ng-change = "setLocationVisible(locationVisible)"
                                                   required>
                                                </select>
                                                <br/>
                                           </div>
                                       </div>
                                   </div>
                                      </div>
                                </div>

                                <div class="panel panel-success">
                                      <div class="panel-heading">Auto routing configuration</div>
                                            <div class="panel-body">
                                                  <div class = "col-md-12">
                                                        <div class = "row tabContentAppSetting">
                                                            <div class = "col-md-2">
                                                                Employee Travel Time Max (Minutes)
                                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="3"
                                                       maxlength="3"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "maxTravelTime"
                                                       placeholder="Enter Travel Time"
                                                       name = "maxTravelTime"
                                                       ng-change = "setMaxTravelTime(maxTravelTime)"
                                                       ng-class = "{error: appSettings.maxTravelTime.$invalid && !appSettings.maxTravelTime.$pristine}"
                                                       required>
<!--                                                <span class ="hint" ng-show = "appSettings.maxTravelTime.$invalid && !appSettings.maxTravelTime.$pristine"></span> -->
                                             <br/>    <!-- <span class ="hintItallic">(Max Travel Time Per Employee in <strong>minutes</strong>)</span>  -->

                                            </div>

                                           <div class = "col-md-2">
                                                Employee Maximum Distance (Meters)
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="5"
                                                       maxlength="5"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       ng-model = "maxRadialDistance"
                                                       placeholder="Enter Radial Distance"
                                                       name = "maxRadialDistance"
                                                       ng-change = "setMaxRadialDistance(maxRadialDistance)"
                                                       ng-class = "{error: appSettings.maxRadialDistance.$invalid && !appSettings.maxRadialDistance.$pristine}"
                                                       required>
<!--                                                <span class ="hint" ng-show = "appSettings.maxRadialDistance.$invalid && !appSettings.maxRadialDistance.$pristine"></span> -->
                                                <br/> <!-- <span class ="hintItallic">(Max Distance Per Employee in <strong>km</strong>)</span> -->

                                           </div>
                                       </div>
                                   </div>

                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                           <!-- <div class = "col-md-2">
                                                Max Route Length (Meters)
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       ng-pattern = "regexMin1to3Numbers"
                                                       ng-model = "maxRouteLength"
                                                       placeholder="Enter Route Length"
                                                       name = "maxRouteLength"
                                                       ng-change = "setMaxRouteLength(maxRouteLength)"
                                                       ng-class = "{error: appSettings.maxRouteLength.$invalid && !appSettings.maxRouteLength.$pristine}"
                                                       required>

                                                 <br/>
                                            </div> -->
                                          <div class = "col-md-2">
                                                Max Route Deviation (Meters)
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       ng-model = "maxRoutedeviation"
                                                       type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="5"
                                                       maxlength="5"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       placeholder="Enter Route Deviation"
                                                       name = "maxRoutedeviation"
                                                       ng-change = "setMaxRouteDevietion(maxRoutedeviation)"
                                                       ng-class = "{error: appSettings.maxRoutedeviation.$invalid && !appSettings.maxRoutedeviation.$pristine}"
                                                       required>
<!--                                                <span class ="hint" ng-show = "appSettings.maxRoutedeviation.$invalid && !appSettings.maxRoutedeviation.$pristine"></span>                                         -->
                                               <br/>  <!-- <span class ="hintItallic">(Max Route Deviation in <strong>km</strong>)</span> -->
                                           </div>

                                           <div class = "col-md-2">
                                                Shift Wise / Normal
                                            </div>
                                            <div class = "col-md-4" ng-init="shiftWise.text == 'Shift Wise'">

                                               <select ng-model="shiftWise"
                                                 class="requestDetailFirstRow appSettingsDropDowns"
                                                 ng-options="shiftWise.value as shiftWise.text for shiftWise in shiftWiseData"
                                                 ng-change = "shiftDataChange(shiftWise)"
                                                 required>
                                             </select>


                                           </div>

                                       </div>
                                   </div>


                                   <div class = "col-md-12" ng-show="shiftWise == 'S'">
                                       <div class = "row tabContentAppSetting">


                                           <div class = "col-md-2">
                                              Trip Type
                                          </div>
                                          <div class = "col-md-4">
                                          <select name="mySelect" class="requestDetailFirstRow appSettingsDropDowns"
                                                  ng-options="option.requestTypes for option in requestTypesDataTS.availableOptions track by option.value"
                                                  ng-model="requestTypesDataTS.selectedOption"
                                                  ng-change="setTripTypeliveTracking(requestTypesDataTS.selectedOption)">
                                          </select>
                                          </div>

                                          <div class = "col-md-2">
                                          Shift Times
                                          </div>

                                          <div class = "col-md-4">
                                              <select ng-model="shiftTimes"
                                                      class="requestDetailFirstRow appSettingsDropDowns "
                                                      ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeData | orderBy:'shiftTime' track by shiftTime.shiftTime"
                                                      ng-required="shiftTimeRequired"
                                                      required
                                                      ng-change = "shiftWiseChange(shiftTimes,requestTypesDataTS.selectedOption)"
                                              >
                                              <option value="">-- Shift Time --</option>
                                              </select>
                                          <br/>
                                          </div>


                                       </div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                            <div class = "col-md-2">
                                                Auto Routing and Route close
                                            </div>
                                            <div class = "col-md-4">
                                                 <input type= "button" class = "btn btn-xs btn-info buttonStyleProfile"
                                                  ng-click = "autoNotificationRouteClose()"
                                                  value = "Click Here">


                                            </div>
                                       </div>
                                   </div>
                                   <div class = "col-md-12" ng-show="geoFenceAreaShow && shiftWise == 'S'">
                                       <div class = "row tabContentAppSetting">


                                            <div class = "col-md-2">
                                                Area Geofence (In Meters)
                                            </div>
                                            <div class = "col-md-4">

                                           <input type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="5"
                                                       maxlength="5"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       name=""
                                                       ng-model="autoClustring"
                                                       class="appSettingsDropDowns"
                                                       ng-change = "setautoClustring(autoClustring)"
                                                       ng-required="shiftTimeRequired"
                                                       required>
                                                <!-- <select ng-model="autoClustring"
                                                   class="appSettingsDropDowns"
                                                   ng-options="clustring for clustring in applicationSettingsArrays.clustrings"
                                                   ng-change = "setClustring(autoClustring)"
                                                   required>
                                                </select>   -->
                                           </div>
                                           <div class = "col-md-2">
                                               Cluster Geofence (In Meters)
                                            </div>
                                            <div class = "col-md-4">
                                                <input type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="5"
                                                       maxlength="5"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       class = "appSettingsDropDowns"
                                                       ng-model = "clusterSize"
                                                       placeholder="Enter Cluster Geofence"
                                                       name = "clusterSize"
                                                       required
                                                       ng-change="setClusterSize(clusterSize)"
                                                       ng-required="shiftTimeRequired">
<!--                                                <span class ="hint" ng-show = "appSettings.clusterSize.$invalid && !appSettings.clusterSize.$pristine"></span>                                         -->
                                                <br/> <!-- <span class ="hintItallic">(Cluster size in <strong>meters</strong>)</span>  -->
                                            </div>

                                       </div>
                                   </div>


                                   <div class = "col-md-12" ng-show="geoFenceAreaShow && shiftWise == 'S'">
                                       <div class = "row tabContentAppSetting">


                                            <div class = "col-md-2">
                                                Route Geofence (In Meters)
                                            </div>
                                            <div class = "col-md-4">

                                           <input type="number"
                                                       ng-minlength="0"
                                                       ng-maxlength="5"
                                                       maxlength="5"
                                                       oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                       name=""
                                                       ng-model="routeGeofence"
                                                       class="appSettingsDropDowns"
                                                       ng-change = "setRouteGeofence(routeGeofence)"
                                                       ng-required="shiftTimeRequired">
                                                <!-- <select ng-model="autoClustring"
                                                   class="appSettingsDropDowns"
                                                   ng-options="clustring for clustring in applicationSettingsArrays.clustrings"
                                                   ng-change = "setClustring(autoClustring)"
                                                   required>
                                                </select>   -->
                                           </div>


                                       </div>
                                   </div>

                                </div>
                          </div>

                                    <div class="panel panel-success">
                                        <div class="panel-heading">Transport Details</div>
                                            <div class="panel-body">

                                                <div class = "col-md-12">
                                                   <div class = "row tabContentAppSetting">

                                                        <div class = "col-md-2">
                                                            Transport Contact Number (With Country Code)
                                                        </div>
                                                        <div class = "col-md-4">
                                                            <input class = "appSettingsDropDowns"
                                                                   type="text"
                                                                   ng-minlength="0"
                                                                   ng-maxlength="18"
                                                                   maxlength="18"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   ng-model = "transportContactNumber"
                                                                   placeholder="Transport Contact Number"
                                                                   name = "transportContactNumber"
                                                                   ng-change = "setTransportContactNumber(transportContactNumber)"
                                                                   ng-class = "{error: appSettings.transportContactNumber.$invalid && !appSettings.transportContactNumber.$pristine}"
                                                                   required>
            <!--                                                <span class ="hint" ng-show = "appSettings.transportContactNumber.$invalid && !appSettings.transportContactNumber.$pristine">*Invalid Mobile Number</span>                                       -->
                                                             <br/><!-- <span class ="hintItallic">(Ex : 919999999999 or 19999999999)</span> -->
                                                        </div>

                                                       <div class = "col-md-2">
                                                            Emergency Contact Number (With Country Code)
                                                        </div>
                                                        <div class = "col-md-4">
                                                            <input class = "appSettingsDropDowns"
                                                                   type="text"
                                                                   ng-minlength="0"
                                                                   ng-maxlength="18"
                                                                   maxlength="18"
                                                                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
                                                                   ng-model = "emrgContactNumber"
                                                                   placeholder="Enter Mobile Number"
                                                                   name = "emrgContactNumber"
                                                                   ng-change = "setEmrgContactNumber(emrgContactNumber)"
                                                                   ng-class = "{error: appSettings.emrgContactNumber.$invalid && !appSettings.emrgContactNumber.$pristine}"
                                                                   required>
            <!--                                                <span class ="hint" ng-show = "appSettings.emrgContactNumber.$invalid && !appSettings.emrgContactNumber.$pristine">*Invalid Mobile Number</span>                                         --> <br/>
                                                           <!--  <span class ="hintItallic">(Ex : 919999999999 or 19999999999)</span>  -->
                                                        </div>
                                                   </div>
                                   </div>
                                   <div class = "col-md-12">
                                       <div class = "row tabContentAppSetting">
                                           <div class = "col-md-2">
                                                Feedback Email Id
                                            </div>
                                            <div class = "col-md-4">
                                                <input class = "appSettingsDropDowns"
                                                       type = "email"
                                                       ng-model = "transportFeedbackEmailId"
                                                       placeholder="Transport Feedback Email Id"
                                                       name = "transportFeedbackEmailId"
                                                       ng-change = "setTransportFeedbackEmailId(transportFeedbackEmailId)"
                                                       ng-class = "{error: appSettings.transportFeedbackEmailId.$invalid && !appSettings.transportFeedbackEmailId.$pristine}"
                                                       required> <br/>
                                                <!-- <span class ="hintItallic">(Ex : emailId@xyz.com)</span>     -->
                                           </div>

                                       </div>
                                   </div>
                              </div>
                         </div>

                                <div class = "row lastSaveButtonRow">
                                    <div class = "col-md-2">

                                    </div>
                                    <div class = "col-md-9 floatRight">

                                <input type = "button"
                                                   class = "btn btn-success btn-sm saveButton_myProfile floatRight marginTop30"
                                                   value = "Save"
                                                   ng-click = "saveAppSettings(emrgContactNumber, facilityData)"
                                                   ng-disabled = "appSettings.$invalid"
                                                  >
                                    </div>
                                </div>
                                </form>
                           </div>
                        </div>
                    </tab>
                    <tab ng-hide ="userRoleDetails == 'webuser'">
                       <tab-heading ng-click = 'getAllMessageSetting()'>Message Settings</tab-heading>

                       <tabset class="tabset subTab_invoiceContract">
                                   <tab>
        <tab-heading ng-click = "vehicleTab()">Send Message</tab-heading>
        <div class = "row">
        <br/>
        <div class = "row">
        <div class="col-md-1"><br/></div>
        <div class="col-md-11">
        <div class="btn-group" data-toggle="buttons">
              <label class="btn btn-default active" ng-click="setMessageDetail('allEmployee')">
                      <input type="radio"
                              name="options"
                              id="option2"
                              ng-model="allEmployee"
                              value="allEmployee"
                              autocomplete="off"
                              chacked>All Employees
                      <span class="glyphiconCustom glyphicon-ok"></span>
              </label>

              <label class="btn btn-default" ng-click="setMessageDetail('shiftDateWise',message)">
                    <input type="radio"
                            name="options"
                            id="option2"
                            ng-model="shiftDateWise"
                            value="shiftDateWise"
                            autocomplete="off"> Employees Shift Wise & Date Wise
                    <span class="glyphiconCustom glyphicon-ok"></span>
              </label>

              <label class="btn btn-default" ng-click="setMessageDetail('allGuest')">
                    <input type="radio"
                          name="options"
                          id="option1"
                          ng-model="allGuest"
                          value="allGuest"
                          autocomplete="off">All Guest
                    <span class="glyphiconCustom glyphicon-ok"></span>
              </label>

              <label class="btn btn-default" ng-click="setMessageDetail('allEmployeeAndGuest')">
                    <input type="radio"
                            name="options"
                            id="option2"
                            ng-model="allEmployeeAndGuest"
                            value="allEmployeeAndGuest"
                            >All Employees And guest
                    <span class="glyphiconCustom glyphicon-ok"></span>
              </label>

              <label class="btn btn-default" ng-click="setMessageDetail('byMobileNumber')">
                  <input type="radio"
                          name="options"
                          id="option2"
                          ng-model="byMobileNumber"
                          value="byMobileNumber"
                          >By Mobile Number
                  <span class="glyphiconCustom glyphicon-ok"></span>
              </label>

        <label class="btn btn-default" ng-click="setMessageDetail('byEmployeeId')">
              <input type="radio"
                    name="options"
                    id="option2"
                    ng-model="byEmployeeId"
                    value="byEmployeeId"
                    >By Employee Id
              <span class="glyphiconCustom glyphicon-ok"></span>
        </label>
         <label class="btn btn-default" ng-click="setMessageDetail('byAdhocMobileNumber')">
                  <input type="radio"
                          name="options"
                          id="option2"
                          ng-model="byAdhocMobileNumber"
                          value="byAdhocMobileNumber"
                          >To Ad-hoc Mobile Number
                  <span class="glyphiconCustom glyphicon-ok"></span>
              </label>
        </div>
        </div>
        </div>
        <br/>
        <div class="col-xs-3">
        </div>

        <div class="col-xs-6">
            <div class="panel panel-primary">
                <div class="panel-heading">{{messageDetailsHeader}}</div>
                      <div class="panel-body">
                          <form name="messageDetailForm">
                            <div  ng-show="dataShiftWiseShow">
                                  <div class="input-group calendarInput">
                                        <span class="input-group-btn">
                                              <button class="btn btn-default"
                                                      ng-click="shiftTimeDateCal($event)">
                                                      <i class="icon-calendar calInputIcon"></i>
                                              </button>
                                        </span>
                                        <input type="text"
                                                ng-model="message.shiftTimeDate"
                                                class="form-control"
                                                placeholder="Shift Time Date"
                                                datepicker-popup='{{format}}'
                                                is-open="datePicker.shiftTimeDate"
                                                show-button-bar=false
                                                datepicker-options='dateOptions'
                                                name='shiftTimeDate'
                                                ng-change="getshiftTimeDates()"
                                                ng-required = "dataShiftWiseShow"
                                                readonly>
                                  </div>
                                  <br/>
                                  <select ng-model="message.tripType"
                                          class="form-control"
                                          ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                          ng-change="setTripTypeliveTracking(message.tripType)"
                                          ng-required = "dataShiftWiseShow"
                                          >
                                      <option value="">-- Select Trip Type --</option>
                                  </select>
                                  <br/>
                                  <select ng-model="message.shiftTimes"
                                          class="form-control floatLeft"
                                          ng-options="shiftTime.shiftTime for shiftTime in shiftsTimeData"
                                          ng-required = "dataShiftWiseShow"
                                          ng-change = "setShiftButton()"
                                          >
                                      <option value="">-- Shift Time --</option>
                                  </select>
                                  <br/>
                                  </div>
                               <!--    <input type="text"
                                        style="margin-top: 15px;"
                                        placeholder="Employee Id"
                                        ng-model="message.employeeId"
                                        class="form-control floatLeft"
                                        ng-minlength="2"
                                        ng-maxlength="20"
                                        maxlength="20"
                                        expect-special-char
                                        ng-show="employeeIdShow"
                                        ng-required = "employeeIdShow"
                                  > -->
                                  <label ng-show="employeeIdShow">Enter Multiple Employee Id (Separated by Comma)</label>
                                   <textarea class="form-control floatLeft marginTop15" 
                                            ng-show="employeeIdShow"
                                            placeholder="Enter Employee Id"
                                            rows="2"
                                            expect-special-char
                                            ng-model="message.employeeId"
                                            ng-required = "employeeIdShow"
                                            disabled>
                                  </textarea>
                                   <button class="addMobNum"
                                           ng-show="employeeIdShow"
                                           ng-click="addEmpId('sm')">
                                           <span>Add</span>
                                         </button>
                                  <!-- <input type="text"
                                          style="margin-top: 15px;"
                                          placeholder="Mobile Number"
                                          ng-minlength="6"
                                          ng-maxlength="37"
                                          maxlength="18"
                                          ng-model="message.mobileNumber"
                                          class="form-control floatLeft"
                                          ng-show="mobileNumberShow"
                                          is-number-with-comma-valid
                                          ng-required = "mobileNumberShow"
                                  > -->
                                  <label ng-show="mobileNumberShow">Enter Multiple Mobile Numbers With Country Code (Separated by Comma)</label>
                                  <textarea class="form-control floatLeft marginTop15" 
                                            ng-show="mobileNumberShow"
                                            placeholder="Type Mobile Number..."
                                            rows="2"
                                            is-number-with-comma-valid
                                            ng-model="message.mobileNumber"
                                            ng-required = "mobileNumberShow"
                                            ng-disabled="adhocMobileNumber" >
                                  </textarea>
                                   <button class="addMobNum"
                                           ng-show="mobileNumberShow && adhocMobileNumber"
                                           ng-click="addMobNum('sm')">
                                           <span>Add</span></button>
                                  <div>
                                  <label class="radioMsgType" >
                                  <input type="radio" required
                                          name="optradio"
                                          value="storedMessage"
                                          ng-model="message.message"
                                          ng-click="setManualMessage('storedMessage')"> Choose Stored Messages
                                  </label>
                                  <label class="radioMsgType ">
                                  <input type="radio" required
                                          name="optradio"
                                          value="manualMessage"
                                          ng-model="message.message"
                                          ng-click="setManualMessage('manualMessage')"> Type Manual Message
                                  </label>
                                  <select ng-model="message.selectedMessage"
                                          class="radioMsgType"
                                          ng-options="msg.msgType for msg in messageList track by msg.value"
                                          ng-hide="msgSendTypeHide"
                                          ng-required ="!msgSendTypeHide"
                                  >
                                  <option value="">--Select Type to Send--</option>
                                  </select>
                                </div>
                                  <select ng-model="message.storedMessageDetails"
                                          class="form-control marginTop15"
                                          ng-options="customMsg.Message for customMsg in customMessagesOnLoad"
                                          ng-required = "message.message == 'storedMessage'"
                                          ng-disabled="storedMessageDisabled"
                                          style="margin-top: 15px;"
                                  >
                                  <option value="">-- Choose Previous Message --</option>
                                  </select>
                                  <div>
                                  <textarea class="form-control floatLeft marginTop15" 
                                            ng-disabled="manualMessageDisabled" 
                                            placeholder="Type Message..." 
                                            rows="5" 
                                            ng-model="message.messageDetail"
                                            ng-required = "message.message == 'manualMessage'"
                                            id="comment">
                                  </textarea>
                                  <button class="messageResetButton"
                                           ng-click="resetTxtAreaMsg(message)">Reset</button>

                                  </div>
                              </form>
                              <div class="btn-group messageSettingButton">
                                      <button type="button" class="btn btn-primary" ng-disabled="messageDetailForm.$invalid" ng-click="sendMessage(message, facilityData)">Send</button>
                                      <button type="button" class="btn btn-danger" ng-click="resetMessage(message)">Reset</button>
                              </div>
                         </div>
                  </div>
             </div>
          <div class="col-xs-3">
        </div>
      </div>
    </tab>

            <tab>
                        <tab-heading ng-click = "getAllCustomMessages()">Create Custom Messages</tab-heading>
                              <div class = 'invoiceTabContent'>
                    
                                   <div class = "firstRowInvoce ">
                                    <div class = "row">
                                          <div class = "col-md-2">
                                              <input type = "button"
                                                     class = "btn btn-success buttonRadius0"
                                                     value = "Create Message"
                                                     ng-click = "createCustomMessages()" >
                                          </div>

                                          <div class = "col-md-8">
                                          </div>

                                          <div class = "col-md-2">
                                                <input class = "form-control" ng-model = "searchMessage" placeholder = "Filter Message">
                                          </div>
                                   </div>
                                </div>


                                <div class="container-fluid">
                                  <div class="col-sm-2"></div>
                                      <div class="table-responsive col-sm-8">
                                           <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
                                                <thead class ="tableHeading">
                                                        <tr>
                                                              <th>S NO</th>
                                                              <th>Message</th>
                                                        </tr>
                                                  </thead>
                                                  <tbody ng-show="filteredCustomMsg.length == 0">
                                                      <tr>
                                                      <td colspan = '8'>
                                                      <div class = "noData">There are No Custom Message Templates</div>
                                                      </td>
                                                      </tr>
                                                  </tbody>
                                                  <tbody ng-show="filteredCustomMsg.length > 0">
                                                      <tr  ng-repeat="customMsg in filteredCustomMsg=(customMessagesOnLoad | filter:searchMessage)">
                                                            <td>
                                                            {{$index+1}}
                                                            </td>
                                                            <td>
                                                            {{customMsg.Message}}
                                                      </tr>
                                                  </tbody>
                                          </table>
                                          <div class="col-sm-2"></div>
                                      </div>
                                </div>
                            </div>
                    </tab>

                              <tab  ng-click = "getAllSentSmsHistory(); getTabInfo('messageSentHistory')">
                                    <tab-heading>Message Sent History</tab-heading>
                                    <div class = 'invoiceTabContent'>

                                   <div class = "firstRowInvoce ">
                                    <div class = "row">
                                          <div class = "col-md-2">
                                              <!-- <input type = "button"
                                                     class = "btn btn-success buttonRadius0"
                                                     value = "Add Contact Type"
                                                     ng-click = "addContractType()" > -->
                                          </div>

                                          <div class = "col-md-8">
                                          </div>

                                          <div class = "col-md-2">
                                                <input class = "form-control" ng-model = "searchText" placeholder = "Filter Contracts Type"
                                                expect_special_char>
                                          </div>
                                   </div>
                                </div>

                                <!--  <div class = "col-md-4 col-xs-12 col-md-offset-4">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div> -->

                                <div class="container-fluid">
                                      <div class="table-responsive">
                                        <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
                                          <thead class ="tableHeading">
                                              <tr>
                                                  <th>Date</th>
                                                  <th>Message Description</th>
                                                  <th>Mobile Number</th>
                                                  <th>Notification Type</th>
                                                  <th>Shift Time</th>
                                                  <th>Delivery Status</th>
                                              </tr>
                                          </thead>
                                          <tbody ng-show="filteredSMSHistory == 'Empty'">
                                             <tr>
                                                      <td colspan = '10'>
                                                      <div class = "noData">There is No Sent SMS History</div>
                                                      </td>
                                                      </tr>
                                          </tbody>
                                          <tbody ng-show="filteredSMSHistory != 'Empty'">
                                         <tr class = 'tabletdCenter' 
                                             ng-repeat="sentSMSHistory in filteredSMSHistory = (sentSmsHistoryOnLoad | filter:searchText)">
                                                <td>{{sentSMSHistory.msgSentDate}}</td>
                                                <td>{{sentSMSHistory.messageDescription}}</td>
                                                <td>{{sentSMSHistory.mobileNumber}}</td>
                                                <td>{{sentSMSHistory.notificationType}}</td>
                                                <td>{{sentSMSHistory.shiftTime}}</td>
                                                <td>{{sentSMSHistory.status}}</td>
                                    </tr>
                             </tbody>
                        </table>
                                      </div>
                                </div>
                            </div>
                              </tab>
                        </tabset>


                    </tab>



                    <tab ng-click = "getRequests(); getTabInfo('requestsDetail')" ng-hide ="userRoleDetails == 'webuser'">
                        <tab-heading>Requests Detail</tab-heading>
                      <div class = "escortAvailableTabContent requestDetailMyProfile row">
                            <div class = "row firstRowActionDiv">

                                <div class = 'col-md-2'>
                                   <select ng-model="trip.tripType"
                                           class="form-control requestDetailFirstRow"
                                           ng-options="tripType.text for tripType in tripTypes track by tripType.value"
                                           ng-change = "setTripType(trip.tripType)"
                                           required>
                                  </select>
                                </div>
                                <div class = 'col-md-4 searchEmployeeTravelDesk'>
                                        <div class = "input-group floatLeft calendarInput">
                                          <input ng-model="search.text"
                                                 type = "text"
                                                 class="form-control"
                                                 placeholder = "Search by Employee Id"
                                                 expect_special_char
                                                 ng-minlength ="2"
                                                 ng-maxlength ="20"
                                                 maxlength ="20">
                                           <span class="input-group-btn">
                                               <button class="btn btn-success"
                                                       ng-click="searchRequests(search.text)">
                                               <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                        </div>
                                </div>
                                <div class = 'col-md-2'></div>
                                <div class = "col-md-3 floatRight">
                                    <input type = "text"
                                           class = 'form-control requestDetailFirstRow'
                                           ng-model = "filterRequests"
                                           expect_special_char
                                           placeholder = "Filter the Request Table">
                                </div>
                            </div>
                            <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!requestsDataShow">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                            </div>
                        <table class = "escortAvailableTable table table-responsive container-fluid" ng-show = "requestsDataShow">
                                <thead class ="tableHeading">
                                    <tr>
                                    <th>Employee Id</th>
                                    <th>Request Creation Time</th>
                                    <th>Request Start Time</th>
                                    <th>Request End Time</th>
                                      <th>Pick/drop Time</th>
                                      <th>Trip Type</th>
                                      <th>Shift Time</th>
                                      <th>Request Type</th>
                                      <th>Address</th>
                                      <th>Route Name</th>
                                      <th>Area Name</th>
                                      <th>Facility Name</th>
                                      <th>Edit</th>
                                      <th></th>
                                      <th></th>
                                    </tr>
                                </thead>
                                <tbody ng-show="filteredRequest.length == 0">
                                <tr><td colspan="12">
                                <div class = "noData">There is No Data for Request Details</div>
                                </td></tr>
                                </tbody>
                                <tbody ng-show="filteredRequest.length > 0">
                               <tr ng-repeat = "requests in filteredRequest = ( requestsData|filter:filterRequests)">
                                    <td class = 'col-md-1'>{{requests.employeeId}}</td>
                                     <td class = 'col-md-1'>{{requests.requestDate}}</td>
                                    <td class = 'col-md-1'>{{requests.requestStartDate}}</td>
                                    <td class = 'col-md-1'>{{requests.requestEndDate}}</td>
                                    <td class = 'col-md-1' ng-show = '!requests.editRequestDetailIsClicked'>{{requests.pickupTime}}</td>
                                    <td  class = 'col-md-1' ng-show = 'requests.editRequestDetailIsClicked'>
                                    <timepicker ng-model="requests.pickupTime"
                                                      hour-step="hstep"
                                                      minute-step="mstep"
                                                      show-meridian="ismeridian"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                    </td>
                                     <td class = 'col-md-1'>{{requests.shiftTime}}</td>
                                     <td class = 'col-md-1'>{{requests.requestType}}</td>
                                     <td class = 'col-md-3'>{{requests.address}}</td>

                                    <td class = 'col-md-1' ng-show = '!requests.editRequestDetailIsClicked'>{{requests.routeName}}</td>
                                    <td  class = 'col-md-1' ng-show = 'requests.editRequestDetailIsClicked'>
                                      <select class = 'form-control'
                                              ng-model="requests.routeName"
                                              ng-options="zoneData.routeName as zoneData.routeName for zoneData in zonesData"
                                              ng-change = "setZone(update.zone)"
                                              required>
                                          <option value="">SELECT ROUTES</option>
                                      </select>
                                    </td>

                                    <td class = 'col-md-1' ng-show = '!requests.editRequestDetailIsClicked'>{{requests.areaName}}</td>
                                    <td  class = 'col-md-1' ng-show = 'requests.editRequestDetailIsClicked'>
                                      <select class = 'form-control'
                                              ng-model="requests.areaName"
                                              ng-options="areaDetail.areaName as areaDetail.areaName for areaDetail in areaDetails"
                                              ng-change = "setZone(update.zone)"
                                              required>
                                          <option value="">SELECT AREA NAME</option>
                                      </select>
                                    </td>
                                    <td class = 'col-md-1'>{{requests.facilityName}}</td>

                                    <td class='col-md-1'>
                                              <input type = 'button'
                                                     class = 'btn btn-warning btn-xs'
                                                     ng-show = "!requests.editRequestDetailIsClicked"
                                                     value = 'Edit'
                                                     ng-click = "editRequestDetail(requests, $index)">
                                              <input type = 'button'
                                                     class = 'btn btn-success btn-xs'
                                                     ng-show = "requests.editRequestDetailIsClicked"
                                                     value = 'Save'
                                                     ng-click = "updateRequestDetail(requests, $index)">
                                    </td>

                                    <td class = 'col-md-1'>
                                             <input type = "button"
                                                    class = "btn btn-success btn-xs enable_vendorDeviceDetail"
                                                    value = "Enable"
                                                    ng-click = "enableRequest(requests)"
                                                    ng-class = "{disabled: requests.status=='Y'}">

                                    </td>
                                    <td class = 'col-md-1'>
                                             <input type = "button"
                                                    class = "btn btn-danger btn-xs disable_vendorDeviceDetail"
                                                    value = "Disable"
                                                    ng-click = "disableRequest(requests)"
                                                    ng-class = "{disabled: requests.status=='N'}">
                                    </td>

                                </tr>
                             </tbody>
                            </table>
                        </div>

                    </tab>
                    <tab ng-click = "getAllShifts();getTabInfo('shiftTimes')" ng-hide ="userRoleDetails == 'webuser'">
                       <tab-heading >Shift Times</tab-heading>
                      <div class = "shiftTimeTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = 'col-md-6' ng-show = '!addNewShiftIsClicked'>
                                    <input type = 'button'
                                           value = 'Create New Shift Time'
                                           class = 'btn btn-primary buttonRadius0 addShiftButton'
                                           ng-click = "addShiftTime(facilityData)">
                                </div>

                                <div class = 'col-md-6'>
                                    <input type = 'text'
                                          class = 'form-control shiftFilter'
                                           placeholder = 'Filter Shift'
                                           expect_special_char
                                           ng-model = 'searchShift'>
                                </div>
                            </div>
                             <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!gotShiftTimeResultsShow">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div>
                        <table class = "escortAvailableTable table table-responsive container-fluid"
                        ng-show = "gotShiftTimeResultsShow">
                                <thead class ="tableHeading">
                                    <tr>
                                    <th>Shift Time</th>
                                      <th>Trip Type</th>
                                      <th>Request CutOff Time</th>
                                      <th>Cancel CutOff Time</th>
                                      <th>Reschedule CutOff Time</th>
                                      <th>Buffer Time In Minutes</th>
                                      <th ng-show="genderPreference == 'Yes'">Gender Preference</th>
                                      <th>Mobile Visible</th>
                                      <th>Ceiling Values</th>
                                      <th ng-show = "!shift.editShiftTimeIsClicked && branchCode == 'SBOManila'">Awaited Passenger</th>
                                      <th>Facility Name</th>
                                      <th>Ceiling Enable/Disable</th>
                                      <th>Edit</th>
                                      <th>Delete</th>
                                    </tr>
                                </thead>
                                <tbody ng-show="filteredShift.length == 0">
                                <tr><td colspan="12">
                                <div class = "noData">There is No Data for Shift Time Details</div>
                                </td></tr>
                                </tbody>
                                <tbody ng-show="filteredShift.length > 0">
                                <!-- <pre>{{shiftTimeData | json}}</pre> -->
                               <tr ng-repeat = "shift in filteredShift = (shiftTimeData | filter:searchShift)" id = "shift{{$index}}">

                                    <td class = 'col-md-1'>{{shift.shiftTime}}</td>
                                    <td class = 'col-md-1'>{{shift.tripType}}</td>

                                    <td class = 'col-md-3' ng-show = '!shift.editShiftTimeIsClicked'>{{shift.cutOffTime}}</td>
                                    <td  class = 'col-md-3' ng-show = 'shift.editShiftTimeIsClicked'>

                                          <timepicker ng-model="cutOffTimesShifts"
                                                      hour-step="hstep5"
                                                      minute-step="mstep5"
                                                      show-meridian="ismeridian5"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                    </td>

                                    <td class = 'col-md-2' ng-show = '!shift.editShiftTimeIsClicked'>{{shift.CancelCutOffTime}}</td>
                                    <td  class = 'col-md-2' ng-show = 'shift.editShiftTimeIsClicked'>

                                          <timepicker ng-model="CancelCutOffTimesShifts"
                                                      hour-step="hstep"
                                                      minute-step="mstep"
                                                      show-meridian="ismeridian"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                    </td>

                                  <td class = 'col-md-3' ng-show = '!shift.editShiftTimeIsClicked'>{{shift.RescheduleCutOffTime}}</td>
                                    <td  class = 'col-md-3' ng-show = 'shift.editShiftTimeIsClicked'>

                                          <timepicker ng-model="RescheduleCutOffTimeShifts"
                                                      hour-step="hstep5"
                                                      minute-step="mstep5"
                                                      show-meridian="ismeridian5"
                                                      readonly-input = 'true'
                                                      class = "timepicker2_empReq floatLeft">
                                          </timepicker>
                                    </td>

                                  <td class = 'col-md-1' ng-show = '!shift.editShiftTimeIsClicked'>{{shift.bufferTime}}</td>

                                  <td class = 'col-md-1' ng-show = 'shift.editShiftTimeIsClicked'>
                                          <select ng-model="shift.bufferTime"
                                                 class="shiftTimeTextWidth"
                                                 ng-options="shiftTime.bufferTime as shiftTime.bufferTime for shiftTime in shiftBufferTimeData"
                                                 required>
                                                 <option value="" class="">{{shift.bufferTime}}</option>
                                          </select>
                                  </td>

                                  <td class = 'col-md-1' ng-show = '!shift.editShiftTimeIsClicked && genderPreference == "Yes"'>{{shift.genderPreference}}</td>

                                  <td class = 'col-md-1' ng-show = 'shift.editShiftTimeIsClicked && genderPreference == "Yes"'>
                                          <select ng-model="shift.genderPreference"
                                                 class="shiftTimeTextWidth"
                                                 ng-options="genderPreference.text as genderPreference.text for genderPreference in genderPreferenceData"
                                                 required>
                                                 <option value="" class="">Select Gender Preference</option>
                                          </select>
                                  </td>


                                  <td class = 'col-md-2' ng-show = "!shift.editShiftTimeIsClicked ">{{shift.mobileVisibleFlg}}</td>


                                  <td  class = 'col-md-2' ng-show = 'shift.editShiftTimeIsClicked'>
                                          <select ng-model="shift.mobileVisibleFlg"
                                                 class="shiftTimeTextWidth"
                                                 ng-options="mobileVisible.mobileVisibleFlg as mobileVisible.mobileVisibleFlg for mobileVisible in mobileVisibleOption"
                                                 required>
                                                 <option value="" class="">Select Mobile Visible Type</option>
                                          </select>
                                    </td>

                                  <td class = 'col-md-2' ng-show = '!shift.editShiftTimeIsClicked'>{{shift.ceilingNo}}
                                  </td>
                                  <td class = 'col-md-2' ng-show = 'shift.editShiftTimeIsClicked'><input type="text" ng-model="ceilingNo" name="" class="form-control shiftTimeTextWidth" only-num></td>
                                  <td class = 'col-md-2' ng-show = "!shift.editShiftTimeIsClicked && branchCode == 'SBOManila'">{{shift.bufferCeilingNo}}
                                  </td>
                                  <td class = 'col-md-2' ng-show = "shift.editShiftTimeIsClicked && branchCode == 'SBOManila'"><input type="text" ng-model="bufferCeilingNo" name="" class="form-control shiftTimeTextWidth" only-num></td>
                                   <td class = 'col-md-2'>{{shift.facilityName}}</td>

                                    <td class = 'col-md-1' ng-show="shift.ceilingFlg=='N'">
                                             <input type = "button"
                                                    class = "btn btn-success btn-xs enable_vendorDeviceDetail"
                                                    value = "Enable"
                                                    ng-click = "enableRequestShift(shift)"
                                                    >

                                    </td>
                                    <td class = 'col-md-1' ng-show="shift.ceilingFlg=='Y'">
                                             <input type = "button"
                                                    class = "btn btn-danger btn-xs disable_vendorDeviceDetail"
                                                    value = "Disable"
                                                    ng-click = "disableRequestShift(shift)"
                                                    >
                                    </td>
                                      <td class='col-md-4 col-sm-4'>
                                             <button type = 'button'
                                                     class = 'btn btn-warning btn-sm'
                                                     ng-show = "!shift.editShiftTimeIsClicked"
                                                     ng-click = "editShiftTime(shift, $index)"><i class="icon-edit-sign"></i></button>
                                            <div class="btn-group btn-group-sm shiftTimeEdit">
                                            <button type="button" class="btn btn-success "
                                                     ng-show = "shift.editShiftTimeIsClicked"
                                                     ng-click = "updateShiftTime(shift, $index, cutOffTimesShifts, CancelCutOffTimesShifts,RescheduleCutOffTimeShifts, ceilingNo, bufferCeilingNo)"><i class="icon-save"></i>
                                            </button>
                                            <button type="button" class="btn btn-warning "
                                                     ng-show = "shift.editShiftTimeIsClicked"
                                                     ng-click = "canceShiftTime(shift, $index)"><i class = "icon-remove-sign"></i>
                                            </button>
                                            </div>
                                          </td>
                                    <td class = 'col-md-3'>
                                            <button type = "button" class = "btn btn-danger btn-sm" ng-click = "deleteShiftTimes(shift, $index)"><i class="icon-remove"></i> </button>
                                        </td>
                                </tr>
                             </tbody>
                            </table>
                        </div>

                    </tab>

                    <tab ng-hide ="userRoleDetails == 'webuser'" ng-click = "getAllRouteNames(); getTabInfo('routeNames')">
                       <tab-heading >Route Names</tab-heading>
                      <div class = "shiftTimeTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = 'col-md-6 addNewRouteButton'>
                                    <input type = 'button'
                                           value = 'Create New Route'
                                           class = 'btn btn-primary buttonRadius0 addShiftButton'
                                           ng-click = 'addRouteName()'
                                           required>
                                </div>
                                <div class = 'col-md-6 addNewRouteDiv'>
                                    <label>Route Name</label><br>
                                    <input type = 'text'
                                           ng-model = 'newRoute.routeName'
                                           placeholder = 'Enter Route Name'
                                           expect_special_char
                                           ng-maxlength="50"
                                           maxlength="50"
                                           class = 'form-control floatLeft marginRight10 addShiftInput'>
                                    <input type = 'button'
                                           value = 'Save New Route'
                                           class = 'btn btn-success buttonRadius0 addShiftButton marginRight10 floatLeft'
                                           ng-click = 'saveNewRoute(newRoute, facilityData)'
                                           ng-disabled = '!newRoute.routeName'>
                                    <input type = 'button'
                                           value = 'Cancel'
                                           class = 'btn btn-default buttonRadius0 addShiftButton floatLeft'
                                           ng-click = 'cancelNewRoute()'>
                                </div>
                                <div class = 'col-md-6'>
                                    <input type = 'text'
                                           class = 'form-control routeFilter'
                                           placeholder = 'Filter Route'
                                           expect_special_char
                                           ng-model = 'searchRouteName'>
                                </div>
                            </div>
                        <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                      <th>Route Id</th>
                                      <th>Route Name</th>
                                      <th>Facility Name</th>
                                      <th>Edit</th>
                                    </tr>
                                </thead>
                                <tbody ng-show="filteredRoute.length == 0">
                                <tr><td colspan="12">
                                <div class = "noData">There is No Data for Route Name Details</div>
                                </td></tr>
                                </tbody>
                                <tbody ng-show="filteredRoute.length > 0">
                               <tr ng-repeat = "route in filteredRoute = (routeNameData | filter: searchRouteName)">
                                    <td class = 'col-md-4'>{{route.routeId}}</td>
                                    <td class = 'col-md-4' ng-show = '!route.editRouteNameIsClicked'>{{route.routeName}}</td>
                                    <td class = 'col-md-4' ng-show = "route.editRouteNameIsClicked" >
                                      <input
                                                          type = 'text'
                                                          expect_special_char
                                                          ng-maxlength="50"
                                                          maxlength="50"
                                                          ng-model = "route.routeName"
                                                          >
                                    </td>
                                    <td class = 'col-md-3'>{{route.facilityName}}</td>

                                          <td class='col-md-3'>
                                              <input type = 'button'
                                                     class = 'btn btn-warning btn-sm'
                                                     ng-show = "!route.editRouteNameIsClicked"
                                                     value = 'Edit'
                                                     ng-click = "editRouteName(route, $index)">
                                              <input type = 'button'
                                                     class = 'btn btn-success btn-sm'
                                                     ng-show = "route.editRouteNameIsClicked"
                                                     value = 'Save'
                                                     ng-click = "updateRouteName(route, $index)">
                                          </td>
                                </tr>
                             </tbody>
                            </table>
                        </div>
                    </tab>

                    <tab ng-click = "getAllAreaNames(); getTabInfo('areaNames')" ng-hide ="userRoleDetails == 'webuser'">
                       <tab-heading>Area Names</tab-heading>
                      <div class = "shiftTimeTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = 'col-md-6 addNewAreaButton'>
                                    <input type = 'button'
                                           value = 'Create New Area'
                                           class = 'btn btn-primary buttonRadius0 addShiftButton'
                                           ng-click = 'addAreaName()'
                                           required>
                                </div>
                                <div class = 'col-md-6 addNewAreaDiv'>
                                    <label>Area Name</label><br>
                                    <input type = 'text'
                                           ng-model = 'newArea.areaName'
                                           placeholder = 'Enter Area Name'
                                           expect_special_char
                                           ng-maxlength="50"
                                           maxlength="50"
                                           class = 'form-control floatLeft marginRight10 addShiftInput'>
                                    <input type = 'button'
                                           value = 'Save New Area'
                                           class = 'btn btn-success buttonRadius0 addShiftButton marginRight10 floatLeft'
                                           ng-click = 'saveNewArea(newArea,facilityData)'
                                           ng-disabled = '!newArea.areaName'>
                                    <input type = 'button'
                                           value = 'Cancel'
                                           class = 'btn btn-default buttonRadius0 addShiftButton floatLeft'
                                           ng-click = 'cancelNewArea()'>
                                </div>
                                <div class = 'col-md-6'>
                                </div>
                                <div class = 'col-md-6'>
                                    <input type = 'text'
                                           class = 'form-control routeFilter'
                                           placeholder = 'Filter Area'
                                           expect_special_char
                                           ng-model = 'searchAreaName'>
                                </div>
                            </div>
                        <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                    <tr>
                                    <th>Area Id</th>
                                    <!-- <th>Area Status</th> -->
                                    <th>Area Name</th>
                                    <th>Facility Name</th>
                                    <th>Edit</th>
                                    </tr>
                                </thead>
                                <tbody ng-show="filteredArea.length == 0">
                                <tr><td colspan="12">
                                <div class = "noData">There is No Data for Area Name Details</div>
                                </td></tr>
                                </tbody>
                                <tbody ng-show="filteredArea.length > 0">
                               <tr ng-repeat = "area in filteredArea = (allAreaDatas | filter: searchAreaName)">
                                    <td class = 'col-md-3'>{{area.areaId}}</td>
                                    <!-- <td class = 'col-md-3' ng-show = '!area.editAreaNameIsClicked'>{{area.areaStatus}}</td> -->
                                    <!-- <td class = 'col-md-3' ng-show = "area.editAreaNameIsClicked" >
                                     <select ng-model="area.areaStatus"
                                                 class="shiftTimeTextWidth"
                                                 ng-options="areaStatus.text as areaStatus.text for areaStatus in zoneTypeStatus"
                                                 required>
                                                 <option value="" class="">{{shift.bufferTime}}</option>
                                          </select>
                                    </td> -->
                                    <td class = 'col-md-3' ng-show = '!area.editAreaNameIsClicked'>{{area.areaName}}</td>
                                    <td class = 'col-md-3' ng-show = "area.editAreaNameIsClicked" >
                                      <input
                                                          type = 'text'
                                                          expect_special_char
                                                          ng-maxlength="50"
                                                          maxlength="50"
                                                          ng-model = "area.areaName"
                                                          >
                                    </td>
                                    <td class = 'col-md-1'>{{area.facilityName}}</td>

                                          <td class='col-md-3'>
                                              <input type = 'button'
                                                     class = 'btn btn-warning btn-sm'
                                                     ng-show = "!area.editAreaNameIsClicked"
                                                     value = 'Edit'
                                                     ng-click = "editAreaName(area, $index)">
                                              <input type = 'button'
                                                     class = 'btn btn-success btn-sm'
                                                     ng-show = "area.editAreaNameIsClicked"
                                                     value = 'Save'
                                                     ng-click = "updateAreaName(area, $index)">
                                          </td>
                                </tr>
                             </tbody>
                            </table>
                        </div>
                    </tab>

                    <tab ng-hide ="userRoleDetails == 'webuser'"  ng-click = "getAllGeoLocations(); getTabInfo('geoLocation')">
                       <tab-heading>GEO Locations</tab-heading>
                      <div class = "shiftTimeTabContent row">
                            <div class = "row">

                                <div class = 'col-md-6'>
                                    <!-- <div class="input-group">
                                        <input type="text" class="form-control" placeholder="Search for location Id...">
                                        <span class="input-group-btn">
                                          <button class="btn btn-primary" type="button">Search</button>
                                        </span>
                                      </div> -->
                                </div>
                                <div class = 'col-md-2'>

                                </div>

                                <div class = 'col-md-2'>
                                    <select ng-model="locationStatusSearch"
                                            class="form-control"
                                            ng-options="status.value as status.text for status in geoLocationStatus"
                                            required
                                            >
                                            <option value="">-- Select Filter Status --</option>
                                    </select>

                                </div>
                                <div class = 'col-md-2'>
                                    <input type = 'text'
                                           class = 'form-control'
                                           expect_special_char
                                           placeholder = 'Filter Location'
                                           ng-model = 'searchGeoLocation'>
                                </div>
                                <!-- <div class = 'col-md-3'> -->
                                  <!-- <div class="btn-group"> -->
                                      <!-- <button type="button" class="btn btn-success btn-sm" ng-click="enableAllLocation()">Enable All</button>
                                      <button type="button" class="btn btn-danger btn-sm" ng-click="disableAllLocation()">Disable All</button> -->
                                      <!-- <button type="button" class="btn btn-success btn-sm" ng-click="enableSelectedLocation()">Enable Selected Location</button>
                                      <button type="button" class="btn btn-danger btn-sm" ng-click="disableSelectedLocation()">Disable Selected Location</button> -->
                                      <!-- <button type="button" class="btn btn-warning btn-sm" ng-click="unCheckLocation()">Uncheck All</button> -->
                                  <!-- </div> -->

                                <!-- </div> -->
                            </div>

                        <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                      <!-- <th>S No</th> -->
                                      <!-- <th>Select</th> -->
                                      <!-- <th>Location Id</th> -->
                                      <th>Location Name</th>
                                      <th>Created By</th>
                                      <th>Location Address</th>
                                      <th>Facility Name</th>
                                      <th>Enable/Disable</th>
                                    </tr>
                                </thead>
                                <tbody ng-show="FilteredGeoLocation.length == 0">
                                <tr><td colspan="12">
                                <div class = "noData">There is No Data for Geo Location Details</div>
                                </td></tr>
                                </tbody>
                                <tbody ng-show="FilteredGeoLocation.length > 0">
                               <tr ng-repeat = "location in FilteredGeoLocation =(geoLocationsData | filter: searchGeoLocation)">
                                    <!-- <td>{{$index+1}}</td>   -->
                                    <!-- <td style="padding-top: 0px; padding-bottom: 0px">
                                      <label class="checkbox" for="{{location.areaId}}">
                                          <input type="checkbox" ng-model="selection.ids[location.areaId]" name="group" id="{{location.areaId}}" class="locationCheckBoxMargin" ng-change="setLocationSelectedValues(selection.ids)"/>
                                      </label>
                                    </td>  -->
                                    <!-- <td>{{location.areaId}}</td>  -->
                                    <td>{{location.areaName}}</td>
                                    <td>{{location.employeeName}}</td>
                                    <td>{{location.locationAddress}}</td>
                                    <td>{{location.facilityName}}</td>
                                    <td class="hidden">{{location.locationFlg}}</td>
                                    <td>
                                        <input type="button"
                                               class="btn btn-primary btn-xs"
                                               name=""
                                               ng-click="editLocationDetails(location, $index)"
                                               value="Edit">
                                        <input type="button"
                                               class="btn btn-success btn-xs"
                                               value="Enable"
                                               ng-show="location.locationFlg == 'Disable'"
                                               ng-click="enableLocation(location, $index, selection.ids)"
                                               name="">
                                        <input type="button"
                                               class="btn btn-danger btn-xs"
                                               value="Disable"
                                               ng-show="location.locationFlg == 'Enable'"
                                               ng-click="disableLocation(location, $index, selection.ids)"
                                               name="">
                                    </td>
                                </tr>
                             </tbody>
                            </table>
                        </div>
                    </tab>

<tab ng-click = "getProjectManagement(); getTabInfo('projectManagement')" ng-hide ="userRoleDetails == 'webuser'">
   <tab-heading>Project Management</tab-heading>
   <tabset class="tabset subTab_invoiceContract">
      <tab>
        <tab-heading ng-click = "getProjectManagement()">Add Project</tab-heading>
<div class = "">
    <div class="">
    <div class = "formWrapper">
       <form name = "projectForm.addProjectForm">
           <div class = "col-md-2 col-xs-12"> </div>
<!--                      <div class = "col-md-4 col-xs-12">
               <div><label>Project Id</label></div>

               <select ng-model="addProject.projectId"
                      class = "form-control"
                      ng-options="listOfProjectId.projectId for listOfProjectId in listOfProjectNameAdhoc"
                      required>
              <option value="">-- Select Id --</option>
              </select>
           </div>
           <div class = "col-md-4 col-xs-12">
               <div><label>Project Name</label></div>
                <select ng-model="addProject.projectName"
                        class = "form-control"
                        ng-options="listOfProjectName.projectName for listOfProjectName in listOfProjectNameAdhoc"
                        required>
                <option value="">-- Select Name --</option>
                </select>
           </div> -->
            <div class = "col-md-4 col-xs-12">
               <div>
               <label>Project Id</label>
               <input type="text"
                           ng-model="addProject.projectId"
                           class="form-control"
                           placeholder = "Project Id"
                           name = 'projectId'
                           ng-minlength="1"
                           ng-maxlength="20"
                           maxlength="20"
                           required
                           ng-class = "{error: projectForm.addProjectForm.projectId.$invalid && !projectForm.addProjectForm.projectId.$pristine}">
               </div>
                <span class = "hintModal" ng-show = "addProjectForm.projectId.$error.pattern">* Enter Only Numbers</span>
                <span class = "hintModal" ng-show = "addProjectForm.projectId.$error.minlength">* Mobile Num Is Too Short..</span>
               <span class = "hintModal" ng-show="addProjectForm.projectId.$error.maxlength">* In-valid Mobile Number</span>
           </div>
           <div class = "col-md-4 col-xs-12">
               <div>
               <label>Project Name</label>
                <input type="text"
                      ng-model="addProject.projectName"
                      class="form-control"
                      placeholder = "Project Name"
                      name = 'projectName'
                      ng-minlength="1"
                      ng-maxlength="50"
                      maxlength="50"
                      expect_special_char
                      required
                      ng-class = "{error: projectForm.addProjectForm.projectName.$invalid && !projectForm.addProjectForm.projectName.$pristine}">
               </div>
               <span class = "hintModal" ng-show = "projectForm.addProjectForm.projectName.$error.pattern">* Enter Only Alphabets</span>
               <span class = "hintModal" ng-show = "projectForm.addProjectForm.projectName.$error.minlength">* Name is too short</span>
               <span class = "hintModal" ng-show = "projectForm.addProjectForm.projectName.$error.maxlength">* Invalid..!! Name is too long</span>
           </div>
           <div class = "col-md-2 col-xs-12"> </div>
           <div class = "col-md-2 col-xs-12"> </div>

          <div class = "col-md-4 col-xs-12">
              <label>Project Start Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="projectStartDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text"
                      ng-model="addProject.projectStartDate"
                      class="form-control"
                      placeholder = "Start Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.projectStartDatePopup"
                      ng-change="endDateSet(addProject.projectStartDate)"
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'projectStartDate'
                      required
                      readonly
                      ng-class = "{error: projectForm.addProjectForm.projectStartDate.$invalid && !projectForm.addProjectForm.projectStartDate.$pristine}">
             </div>
           </div>

           <div class = "col-md-4 col-xs-12">
               <label>Project End Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="projectEndDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text"
                      ng-model="addProject.projectEndDate"
                      class="form-control"
                      placeholder = "End Date"
                      datepicker-popup = '{{format}}'
                      min-date="minDate"
                      is-open="datePicker.projectEndDatePopup"
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'projectEndDate'
                      required
                      readonly
                      ng-class = "{error: projectForm.addProjectForm.projectEndDate.$invalid && !projectForm.addProjectForm.projectEndDate.$pristine}">
             </div>
           </div>
           <div class = "col-md-2 col-xs-12"> </div>
       </form>
      </div>
    </div>
<div class = "col-md-5 col-xs-12"> </div>
<div class="col-md-3 col-xs-12">
  <button type="button" class="btn btn-success" ng-click = "addNewProject(addProject, projectForm.addProjectForm)" ng-disabled="projectForm.addProjectForm.$invalid">Add</button>
    <button type="button" class="btn btn-warning" ng-click = "addNewProjectReset(projectForm.addProjectForm)">Reset</button>
</div>
 <div class = "col-md-4 col-xs-12"> </div>
</div>
    </tab>


              <tab ng-click = "getProjectManagement();getTabInfo('projectDetails')">
     <tab-heading>Project Details</tab-heading>
<div class = 'invoiceTabContent'>

         <div class = "firstRowInvoce ">
          <div class = "row">
                <div class = "col-md-2">
                </div>

                <div class = "col-md-6">
                </div>
                <div class = "col-md-2 text-right">
                      <!-- <button type="button" class="btn btn-success" ng-click = "disableAllProjects()">Disable All</button>  -->
                </div>
                <div class = "col-md-2">
                      <input class = "form-control" ng-model = "projectSearchText" placeholder = "Filter Project Details"
                      expect_special_char>
                </div>
         </div>
      </div>
                                <div class="container-fluid">
                                      <div class="">

                                                 <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
                                          <thead class ="tableHeading">
                                              <tr>
                                                  <!-- <th><input type="checkbox" ng-model="addCheckBox"
                                                  ng-change="projectSelectAll(addCheckBox)"></th>
 -->                                              <th>Project Id</th>
                                                  <!-- <th>Client Project Id</th> -->
                                                  <th>Employee Project Name</th>
                                                  <th>Project Allocation Start Date</th>
                                                  <th>Project Allocation End Date</th>
                                                  <th>Facility Name</th>
                                                  <th>Edit</th>
                                                  <th>Status</th>
                                              </tr>
                                          </thead>
                                          <tbody ng-show="filteredProjecDetails.length == 0">
                                          <tr><td colspan="12">
                                          <div class = "noData">There is No Data for Project Details</div>
                                          </td></tr>
                                          </tbody>
                                          <tbody ng-show="filteredProjecDetails.length > 0">
                                         <tr class = 'tabletdCenter'
                                             ng-repeat="projectDetail in filteredProjecDetails = (listofProjectIdDetails | filter:projectSearchText)">
                                             <!-- <td><input type="checkbox" ng-model="projectDetail.selected"
                                                    ng-change="projectSelectIndividual(projectDetail.selected)">
                                                </td> -->
                                                <td ng-hide="editProjectForm == $index">
                                                    <span>{{projectDetail.clientProjectId}}</span>
                                                </td>
                                                <td ng-show="editProjectForm == $index"> 
                                                <input type="text" 
                                                        ng-model="editProjectDetail.clientProjectId"
                                                        class="form-control"
                                                        placeholder = "Project Name"
                                                        name = 'projectName'
                                                        ng-minlength="1"
                                                        ng-maxlength="50"
                                                        maxlength="50"
                                                        expect_special_char
                                                        required
                                                        ng-class = "{error: projectForm.addProjectForm.projectName.$invalid && !projectForm.addProjectForm.projectName.$pristine}">  </td>
                                                <!-- <td>
                                                    <span>{{projectDetail.clientProjectId}}</span>
                                                </td> -->
                                                <td ng-hide="editProjectForm == $index"> <span>{{projectDetail.projectName}}</span> </td>
                                                <td ng-show="editProjectForm == $index">
                                                <input type="text"
                                                        ng-model="editProjectDetail.projectName"
                                                        class="form-control"
                                                        placeholder = "Project Name"
                                                        name = 'projectName'
                                                        ng-minlength="1"
                                                        ng-maxlength="50"
                                                        maxlength="50"
                                                        expect_special_char
                                                        required
                                                        ng-class = "{error: projectForm.addProjectForm.projectName.$invalid && !projectForm.addProjectForm.projectName.$pristine}">  </td>
                                                <td ng-hide="editProjectForm == $index"> <span>{{projectDetail.projectStartDate}}</span> </td>
                                                <td ng-show="editProjectForm == $index"><div class = "input-group calendarInput">
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-default" ng-click="projectStartDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                                                   <input type="text"
                                                          ng-model="editProjectDetail.projectStartDate"
                                                          class="form-control"
                                                          placeholder = "Start Date"
                                                          datepicker-popup = '{{format}}'
                                                          is-open="datePicker.projectStartDatePopup"
                                                          show-button-bar = false
                                                          datepicker-options = 'dateOptions'
                                                          name = 'projectStartDate'
                                                          required
                                                          readonly
                                                          ng-class = "{error: projectForm.addProjectForm.projectStartDate.$invalid && !projectForm.addProjectForm.projectStartDate.$pristine}">
                                                 </div></td>

                                                <td ng-hide="editProjectForm == $index"> <span>{{projectDetail.projectEndDate}}</span> </td>
                                                <td ng-show="editProjectForm == $index">    <div class = "input-group calendarInput">
                                                          <span class="input-group-btn">
                                                              <button class="btn btn-default" ng-click="projectEndDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                                                         <input type="text"
                                                                ng-model="editProjectDetail.projectEndDate"
                                                                class="form-control"
                                                                placeholder = "End Date"
                                                                datepicker-popup = '{{format}}'
                                                                is-open="datePicker.projectEndDatePopup"
                                                                show-button-bar = false
                                                                datepicker-options = 'dateOptions'
                                                                name = 'projectEndDate'
                                                                required
                                                                readonly
                                                                ng-class = "{error: projectForm.addProjectForm.projectEndDate.$invalid && !projectForm.addProjectForm.projectEndDate.$pristine}">
                                                       </div> </td>
                                                       <td>{{projectDetail.facilityName}}</td>
                                                <td class="nowrapText">
                                                  <button type="submit"
                                                          ng-disabled="false"
                                                          ng-show="editProjectForm == $index"
                                                          ng-click="saveEditedProject(editProjectDetail, projectDetail)"
                                                          class="btn btn-success btn-sm">Save
                                                  </button>
                                                  <button type="button"
                                                          ng-disabled="rowform.$waiting"
                                                          ng-show="editProjectForm == $index"
                                                          ng-click="cancelEditProject()"
                                                          class="btn btn-danger btn-sm">Cancel
                                                  </button>
                                                  <div class="buttons" ng-hide="editProjectForm == $index">
                                                    <button class="btn btn-primary btn-sm" ng-click="editProject(projectDetail, $index)">Edit</button>
                                                  </div>  
                                                </td>
                                           <!--      <td>
                                                <button type="button" class="btn btn-info" 
                                                ng-click = "editProject()">Edit</button>
                                                </td> -->
                                                <td>
                                                <button type="button" class="btn btn-warning" 
                                                ng-click = "disableProject(projectDetail, $index)">Disable</button>
                                                </td>
                                        </tr>
                                </tbody>
                        </table>
                      </div>
                </div>
            </div>
    </tab>
  <tab ng-click = "projectAllocation();getProjectManagement();getTabInfo('ProjectAllocationDelegation')">
        <tab-heading>Project Allocation/Delegation</tab-heading>
<div class = "">
    <div class="">
    <div class = "formWrapper">
       <form name = "projectForm.allocateProjectForm">
        <div class="col-md-2"></div>
        <div class="col-md-8">
        <div class="btn-group width100per" data-toggle="buttons">
              <label class="btn btn-default active dropPickColumn_customRoute" ng-click="setAllocation('allocation')">
                      <input type="radio"
                              name="option1"
                              id="option1"
                              ng-model="allocationAndDelegation"
                              value="allocation"
                              autocomplete="off"
                              chacked>Allocation
                      <span class="glyphiconCustom glyphicon-ok"></span>
              </label>
              <label class="btn btn-default dropPickColumn_customRoute" ng-click="setAllocation('delegation')">
                    <input type="radio"
                          name="option1"
                          id="option1"
                          ng-model="allocationAndDelegation"
                          value="delegation"
                          >Delegate to other user
                    <span class="glyphiconCustom glyphicon-ok"></span>
              </label>
        </div>
        </div>
        <div class="col-md-2"></div>
           <div class = "col-md-2 col-xs-12"> </div>
          <div class = "col-md-4 col-xs-12">
               <div><label>Project Id</label></div>  
               
               <select ng-model="allocateProject.projectId"
                      class = "form-control"
                      ng-change="setProjectName(allocateProject.projectId)"
                      ng-options="listOfProjectId.projectId as listOfProjectId.ClientprojectId for listOfProjectId in listOfProjectNameAdhoc"
                      ng-required>
              <option value="">-- Select Id --</option>
              </select>            
           </div>
          <!--  <div class = "col-md-4 col-xs-12" ng-if="!allocationStatus">
               <div><label>Project Ids and Names</label></div>
                     <am-multiselect class="input-lg dynamicDaysInput projectBtnWidth"
                                    multiple="true"
                                    ms-selected ="{{allocateProject.projectList.length}} Projects selected"
                                    ng-model="allocateProject.projectList"
                                    ms-header="Select Projects"
                                    options="listOfProjectId.projectId as listOfProjectId.ClientprojectId + ' - ' + listOfProjectId.projectName for listOfProjectId in listOfProjectNameAdhoc"
                                    change="setDynamicDays(dynamicDays)" ng-required="!allocationStatus">
                    </am-multiselect>            
           </div> -->
           <div class = "col-md-4 col-xs-12" > 
               <div><label>Project Name</label></div>  
                <select ng-model="allocateProject.projectName"
                        class = "form-control"
                        ng-options="listOfProjectName.projectName for listOfProjectName in listOfProjectNameAdhoc track by listOfProjectName.projectId"
                        ng-required disabled>
                <option value="">-- Select Name --</option>
                </select> 
           </div>
           <div class = "col-md-2 col-xs-12"> </div>
           <div class = "col-md-2 col-xs-12" ng-if="allocationStatus"> </div>
           <div class = "col-md-4 col-xs-12" ng-if="allocationStatus">
              <label>Project Start Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="projectStartDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text"
                      ng-model="allocateProject.projectStartDate"
                      class="form-control"
                      placeholder = "Start Date"
                      datepicker-popup = '{{format}}'
                      is-open="datePicker.projectStartDatePopup"
                      show-button-bar = false
                      ng-change="endDateSet(allocateProject.projectStartDate)"
                      datepicker-options = 'dateOptions'
                      name = 'projectStartDate'
                      ng-required="allocationStatus"
                      readonly
                      ng-class = "{error: projectForm.allocateProjectForm.projectStartDate.$invalid && !projectForm.allocateProjectForm.projectStartDate.$pristine}">
             </div>
           </div>

           <div class = "col-md-4 col-xs-12" ng-if="allocationStatus">
               <label>Project End Date</label>
            <div class = "input-group calendarInput">
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="projectEndDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
               <input type="text" 
                      ng-model="allocateProject.projectEndDate" 
                      class="form-control" 
                      placeholder = "End Date"
                      datepicker-popup = '{{format}}'
                      min-date="minDate" 
                      is-open="datePicker.projectEndDatePopup" 
                      show-button-bar = false
                      datepicker-options = 'dateOptions'
                      name = 'projectEndDate'
                      ng-required="allocationStatus"
                      readonly
                      ng-class = "{error: projectForm.allocateProjectForm.projectEndDate.$invalid && !projectForm.allocateProjectForm.projectEndDate.$pristine}">            
             </div>
           </div>
           <div class = "col-md-2 col-xs-12" ng-if="allocationStatus"> </div>
           <div class = "col-md-2 col-xs-12"> </div>

          <div class = "col-md-4 col-xs-12">
              <div><label>Reporting Manager Id</label></div>  
                    <select ng-model="allocateProject.reportingManagerId"
                            class = "form-control"
                            ng-options="projectManager.UserId as projectManager.employeeId for projectManager in projectManagerList"
                            required>
                      <option value="">-- Select Id --</option>
                    </select>
           </div>
            <div class = "col-md-4 col-xs-12" ng-if="allocationStatus">
                  <div><label>List of Employee Names</label></div>
                <am-multiselect class="input-lg dynamicDaysInput"
                                    multiple="true"
                                    ms-selected ="{{allocateProject.listOfEmployees.length}} Employee(s) selected"
                                    ng-model="allocateProject.listOfEmployees"
                                    ms-header="Select Employees"
                                    options="employee.userId as employee.employeeId for employee in employeeDetails"
                                    change="setDynamicDays(dynamicDays)" ng-required="allocationStatus">
                    </am-multiselect>
          </div>
           <div class = "col-md-4 col-xs-12" ng-if="!allocationStatus">
                  <div><label>Employee</label></div>

    <input type="text" ng-model="allocateProject.delegatedUserId"
    ng-model-options="modelOptions" placeholder = "Enter Employee Id"
    typeahead="employee as employee.employeeId for employee in employeeDetails | filter:$viewValue | limitTo:8" class="form-control" ng-required="!allocationStatus">

          </div>
           <div class = "col-md-2 col-xs-12"> </div>
       </form>
      </div>
    </div>
<div class = "col-md-5 col-xs-12"> </div>
<div class="col-md-3 col-xs-12">
  <button type="button" class="btn btn-success" ng-click = "allocateNewProject(allocateProject, projectForm.allocateProjectForm)" ng-disabled="projectForm.allocateProjectForm.$invalid">Allocate</button>
    <button type="button" class="btn btn-warning" ng-click = "allocateNewProjectReset(projectForm.allocateProjectForm)">Reset</button>
</div>
 <div class = "col-md-4 col-xs-12"> </div>
</div>
    </tab>
                              <tab ng-click = "getProjectManagement();getTabInfo('allocatedProjectsDetails')">
                                    <tab-heading ng-click = "getContractTypeDetail()">Allocated Projects Details</tab-heading>
                                    <div class = 'invoiceTabContent'>

                                   <div class = "firstRowInvoce ">
                                    <div class = "row">
                                <form name = "projectForm.searchProjectForm">
                               <div class = 'col-md-2'>
                                   <select ng-model="search.projectId"
                                          class = "form-control"
                                          ng-change="setProjectNameForSearch(search.projectId)"
                                          ng-options="listOfProjectId.projectId as listOfProjectId.ClientprojectId for listOfProjectId in listOfProjectNameAdhoc"
                                          required>
                                  <option value="">-- Select Project Id --</option>
                                  </select>
                                </div>
                                <div class = 'col-md-2'>
                                   <select ng-model="search.projectName"
                                          class = "form-control"
                                          ng-options="listOfProjectName.projectName for listOfProjectName in listOfProjectNameAdhoc track by listOfProjectName.projectId"
                                          required disabled>
                                  <option value="">-- Select Project Name --</option>
                                  </select>
                                </div>
                                <div class = 'col-md-2'>
                                   <select ng-model="search.reportingManagerId"
                                          class = "form-control"
                                          ng-options="projectManager.UserId as projectManager.employeeId for projectManager in projectManagerList"
                                          required>
                                    <option value="">--Select Manager Id--</option>
                                  </select>
                                </div>
                                </form>
                                <div class = 'col-md-1 searchEmployeeTravelDesk'>
                                   <span class="input-group-btn">
                                     <button class="btn btn-success" ng-disabled="projectForm.searchProjectForm.$invalid"
                                             ng-click="searchProjectDetails(search)">
                                     <i class = "icon-search searchServiceMappingIcon"></i></button></span>

                                </div>
                                <div class = "col-md-1"></div>
                                <div class = "col-md-2 text-right" ng-if="isAllocatedProject">
                                     <button class="btn btn-warning" ng-disabled="disableAllBtn"
                                             ng-click="disableAllAllocate(allocatedProjects)">Disable All</button>
                                </div>
                                          <div class = "col-md-2" ng-if="isAllocatedProject">
                                                <input class = "form-control" ng-model = "allocatedSearchText" placeholder = "Allocated Projects Details"
                                                expect_special_char>
                                          </div>
                                   </div>
                                </div>

                                <!--  <div class = "col-md-4 col-xs-12 col-md-offset-4">
                                    <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                </div> -->

                                <div class="" ng-if="isAllocatedProject">
                                      <div class="">
                                  <table class = "invoiceByFuelTable table container-fluid table-bordered">
                                          <thead class ="tableHeading">
                                              <tr>
                                                  <th class="text-center"><input type="checkbox"
                                                  ng-model="allocateCheckBox"
                                                  ng-change="allocateProjectSelectAll(allocatedProjects, allocateCheckBox)"></th>
                                                  <th>Employee Project Id</th>
                                                  <th>Project Id</th>
                                                  <th>Project Name</th>
                                                  <!-- <th>Reporting Manager User Id</th> -->
                                                  <th>Project Allocation Star Date</th>
                                                  <th>Project Allocation End Date</th>
                                                  <!-- <th>Edit</th> -->
                                                  <th>Status</th>
                                              </tr>
                                          </thead>
                                          <tbody>
                                         <tr class = 'tabletdCenter'
                                             ng-repeat="allocatedProject in allocatedProjects | filter:projectSearchText">
                                             <td><input type="checkbox" ng-model="allocatedProject.selected"
                                                    ng-change="allocatedProjectSelectCheck(allocatedProject.selected, $index)">
                                                </td>
                                                <td> <span>{{allocatedProject.employeeId}}</span> </td>
                                                <td ng-hide="editProjectAllocateForm == $index"> <span>{{allocatedProject.clientProjectId}}</span> </td>
                                                <td ng-show="editProjectAllocateForm == $index">
                                                <select ng-model="editAllocateProjectDetail.clientProjectId"
                                                        class = "form-control"
                                                        ng-change="setProjectNameForAllocate(editAllocateProjectDetail.clientProjectId)"
                                                        ng-options="listOfProjectId.ClientprojectId for listOfProjectId in listOfProjectNameAdhoc track by listOfProjectId.ClientprojectId"
                                                        required>
                                                <option value="">-- Select Id --</option>
                                                </select>  </td>
                                                <td ng-hide="editProjectAllocateForm == $index"> <span>{{allocatedProject.projectName}}</span> </td>
                                                <td ng-show="editProjectAllocateForm == $index">
                                                <select ng-model="editAllocateProjectDetail.projectName"
                                                        class = "form-control"
                                                        ng-options="listOfProjectName.projectName for listOfProjectName in listOfProjectNameAdhoc track by listOfProjectName.projectName"
                                                        required disabled>
                                                <option value="">-- Select Project Name --</option>
                                                </select>  </td>
                                                <td ng-hide="editProjectAllocateForm == $index"> <span>{{allocatedProject.projectStartDate}}</span> </td>
                                                <td ng-show="editProjectAllocateForm == $index"><div class = "input-group calendarInput">
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-default" ng-click="projectStartDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                                                   <input type="text"
                                                          ng-model="editAllocateProjectDetail.projectStartDate"
                                                          class="form-control"
                                                          placeholder = "Start Date"
                                                          datepicker-popup = '{{format}}'
                                                          is-open="datePicker.projectStartDatePopup"
                                                          show-button-bar = false
                                                          datepicker-options = 'dateOptions'
                                                          name = 'projectStartDate'
                                                          required
                                                          readonly
                                                          ng-class = "{error: projectForm.addProjectForm.projectStartDate.$invalid && !projectForm.addProjectForm.projectStartDate.$pristine}">
                                                 </div></td>

                                                <td ng-hide="editProjectAllocateForm == $index"> <span>{{allocatedProject.projectEndDate}}</span> </td>
                                                <td ng-show="editProjectAllocateForm == $index">    <div class = "input-group calendarInput">
                                                          <span class="input-group-btn">
                                                              <button class="btn btn-default" ng-click="projectEndDateCal($event)"><i class = "icon-calendar calInputIcon"></i></button></span>
                                                         <input type="text"
                                                                ng-model="editAllocateProjectDetail.projectEndDate"
                                                                class="form-control"
                                                                placeholder = "End Date"
                                                                datepicker-popup = '{{format}}'
                                                                is-open="datePicker.projectEndDatePopup"
                                                                show-button-bar = false
                                                                datepicker-options = 'dateOptions'
                                                                name = 'projectEndDate'
                                                                required
                                                                readonly
                                                                ng-class = "{error: projectForm.addProjectForm.projectEndDate.$invalid && !projectForm.addProjectForm.projectEndDate.$pristine}">
                                                       </div> </td>
                                               <!--  <td class="nowrapText">
                                                  <button type="submit"
                                                          ng-disabled="false"
                                                          ng-show="editProjectAllocateForm == $index"
                                                          ng-click="saveEditedAllocateProject(editAllocateProjectDetail, allocatedProject)"
                                                          class="btn btn-success btn-sm">Save
                                                  </button>
                                                  <button type="button"
                                                          ng-disabled="rowform.$waiting"
                                                          ng-show="editProjectAllocateForm == $index"
                                                          ng-click="cancelEditAllocateProject()"
                                                          class="btn btn-danger btn-sm">Cancel
                                                  </button>
                                                  <div class="buttons" ng-hide="editProjectAllocateForm == $index">
                                                    <button class="btn btn-primary btn-sm"
                                                    ng-click="editAllocateProject(allocatedProject, $index)">Edit</button>
                                                  </div>
                                                </td> -->
                                           <!--      <td>
                                                <button type="button" class="btn btn-info"
                                                ng-click = "editProject()">Edit</button>
                                                </td> -->
                                                <td>
                                                <button type="button" class="btn btn-warning"
                                                ng-click = "disableAllocateProject(allocatedProject, $index)">Disable</button>
                                                </td>
                                        </tr>

                             </tbody>
                        </table>
                                      </div>
                                </div>
                            </div>
                              </tab>

              <tab ng-click = "getDelegateUsers();getTabInfo('delegatedDetails')">
     <tab-heading>Delegated Details</tab-heading>
<div class = 'invoiceTabContent'>

         <div class = "firstRowInvoce ">
          <div class = "row">
                <div class = "col-md-2">
                </div>

                <div class = "col-md-6">
                </div>
                <div class = "col-md-2 text-right">
                      <!-- <button type="button" class="btn btn-success" ng-click = "disableAllProjects()">Disable All</button>  -->
                </div>
                <div class = "col-md-2">
                      <input class = "form-control" ng-model = "projectSearchText" placeholder = "Filter Delegates Details"
                      expect_special_char>
                </div>
         </div>
      </div>
                                <div class="container-fluid">
                                <div class="alert alert-info text-center" ng-if="NodelegateUsers"!>
  <strong>No results Found</strong>
</div>
                                      <div ng-if="delegateUsersTable">
    <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered">
       <thead class ="tableHeading">
         <tr>
              <!-- <th><input type="checkbox" ng-model="addCheckBox"
              ng-change="projectSelectAll(addCheckBox)"></th>
-->           <th>Project Id</th>
              <th>Project Name</th>
              <th>Delegate Date</th>
              <th>Delegate User</th>
              <th>Delegated By</th>
              <th>Edit</th>
              <th>Status</th>
          </tr>
      </thead>
      <tbody>

     <tr class = 'tabletdCenter'
                                             ng-repeat="delegateUser in delegateUsers | filter:projectSearchText">
                                             <!-- <td><input type="checkbox" ng-model="projectDetail.selected"
                                                    ng-change="projectSelectIndividual(projectDetail.selected)">
                                                </td> -->
                                                <td><span>{{delegateUser.clientProjectId}}</span></td>

                                                <td> <span>{{delegateUser.projectName}}</span> </td>

                                                <td> <span>{{delegateUser.projectStartDate}}</span> </td>


                                                <td ng-hide="editProjectForm == $index"> <span>{{delegateUser.reportingManagerUserId}}</span> </td>
                                                <td  ng-show="editProjectForm == $index">
                                                <input type="text" ng-model="editDelegated.employee" ng-model-options="modelOptions"  typeahead="employee as employee.employeeId for employee in employeeDetails | filter:$viewValue | limitTo:8"
                                                class="form-control" required>
                                                </td>
                                                <td> <span>{{delegateUser.delegatedBy}}</span> </td>
                                                <td class="nowrapText">
                                                  <button type="submit"
                                                          ng-disabled="false"
                                                          ng-show="editProjectForm == $index"
                                                          ng-click="saveEditDelegateUser(editDelegated, delegateUser)"
                                                          class="btn btn-success btn-sm">Save
                                                  </button>
                                                  <button type="button"
                                                          ng-disabled="rowform.$waiting"
                                                          ng-show="editProjectForm == $index"
                                                          ng-click="cancelEditDelegateUser()"
                                                          class="btn btn-danger btn-sm">Cancel
                                                  </button>
                                                  <div class="buttons" ng-hide="editProjectForm == $index">
                                                    <button class="btn btn-primary btn-sm"
                                                    ng-click="editDelegateUser(delegateUser, $index)">Edit</button>
                                                  </div>
                                                </td>
                                           <!--      <td>
                                                <button type="button" class="btn btn-info"
                                                ng-click = "editProject()">Edit</button>
                                                </td> -->
                                                <td>
                                                <button type="button" class="btn btn-warning"
                                                ng-click = "disableDelegateProject(delegateUser, $index)">Disable</button>
                                                </td>
                                        </tr>
</tbody>
</table>

                      </div>
                </div>
            </div>
    </tab>
                        </tabset>


                    </tab>

                    <tab ng-hide="userRoleDetails == 'webuser'" ng-click="getTabInfo('appDownload')">
                        <tab-heading>Employee App Downloaded/Geo Coded Details</tab-heading>
                        <tabset class="tabset subTab_invoiceContract">
                            <tab ng-click="getAppDownloadCount('appDownloadCount');getTabInfo('appDownload');appDownloadCount=null; setSearchType();">
                                <tab-heading>App Download</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">App Download Summary - <span class="badge">{{getAppDownloadCountData.length}} / {{getAppDownloadCountData[0].totalRecordCount}}</span></h5>
                                            </div>
                                            <!-- 
                                              <div class = "col-md-2">
                                              </div> -->

                                            <div class="col-md-2 ">
                                                <input class="form-control" ng-model="appDownloadCount" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                                            <option value="">Select Search Type</option>
                                            </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData, 'appDownload')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'appDownload')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="saveInExcel('appDownload',getAppDownloadCountData)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                    <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!getAppDownloadCountDataShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row">
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Employee Id</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Name</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Address</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Device Type</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Number</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee EmailId</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>
                                        </div>
                                        <div class="constrainedAppCount row" ng-show="filtered.length > 0" >
                                            <div class="overflowXauto" infinite-scroll="getAppDownloadCount('appDownloadCount')" infinite-scroll-container='".constrainedAppCount"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in filtered =(getAppDownloadCountData | filter:appDownloadCount)">
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.address}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.deviceType}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                </div>
                                            </div>
                                        </div>
                                          <div class="row" ng-show="filtered.length == 0">
                                            <div class="noData">There is No Data for App Download Details</div>
                                        </div>
                                    </div>
                                    <!--   <div class="container-fluid">
                                            <div class="table-responsive constrainedAppCount pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getAppDownloadCount('appDownloadCount')" infinite-scroll-container='".constrainedAppCount"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>

                                                  <tbody ng-show="getAppDownloadCountData.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for App Download Details</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>

                                                <tbody ng-show="getAppDownloadCountData.length > 0">
                                                     <tr class = 'tabletdCenter'
                                                         ng-repeat="Value in getAppDownloadCountData | filter:appDownloadCount">
                                                            <td><span>{{Value.employeeId}}</span></td>
                                                            <td><span>{{Value.employeeName}}</span></td>
                                                            <td><span>{{Value.address}}</span></td>
                                                            <td><span>{{Value.deviceType}}</span></td>
                                                            <td><span>{{Value.mobileNumber}}</span></td>
                                                            <td><span>{{Value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                </div>
                            </tab>
                            <tab ng-click="getYetToAppDownloadCount('yetToAppDownload');getTabInfo('nonAppDownload');setSearchType();getYetToAppCount=null">
                                <tab-heading>Non App download</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">Non App download Summary - <span class="badge">{{getYetToAppCountData.length}} / {{getYetToAppCountData[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!--      <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="getYetToAppCount" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData,'nonAppDownload')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'nonAppDownload')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="nonAppdownloadExcel('nonAppDownload',getYetToAppCountData)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                    <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!getYetToAppCountDataShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row">
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Employee Id</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Name</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Address</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Device Type</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Number</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee EmailId</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>
                                        </div>
                                        <div ng-show="filteredYetToApp.length > 0" class="constrainedNonAppCount row">
                                            <div class="overflowXauto" infinite-scroll="getYetToAppDownloadCount('yetToAppDownload')" infinite-scroll-container='".constrainedNonAppCount"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in filteredYetToApp =(getYetToAppCountData | filter:getYetToAppCount)">
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.address}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.deviceType}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                </div>
                                            </div>
                                        </div>
                                         <div class="row" ng-show="filteredYetToApp.length == 0">
                                            <div class="noData">There is No Data for Yet To App Download Details</div>
                                        </div>
                                    </div>
                                    <!-- <div class="container-fluid">
                                            <div class="table-responsive constrainedNonAppCount pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getYetToAppDownloadCount('yetToAppDownload')" infinite-scroll-container='".constrainedNonAppCount"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>
                                                  <tbody ng-show="getYetToAppCountData.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for No App Download Details</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>

                                                <tbody ng-show="getYetToAppCountData.length > 0">
                                                     <tr class = 'tabletdCenter'
                                                         ng-repeat="value in getYetToAppCountData">
                                                            <td><span>{{value.employeeId}}</span></td>
                                                            <td><span>{{value.employeeName}}</span></td>
                                                            <td><span>{{value.address}}</span></td>
                                                            <td><span>{{value.deviceType}}</span></td>
                                                            <td><span>{{value.mobileNumber}}</span></td>
                                                            <td><span>{{value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                </div>
                            </tab>
                            <tab ng-click="getGeoCodedEmployee('geoCodedEmployee');getTabInfo('geoCodedEmployee');setSearchType();geoCodedEmployeeCount=null">
                                <tab-heading>Geo Coded User</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">Geo Coded User Summary - <span class="badge">{{geoCodedEmployeeCountData.length}} / {{geoCodedEmployeeCountData[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!--     <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="geoCodedEmployeeCount" placeholder="Filter" expect_special_char>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData,'geoCodedEmployee')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'geoCodedEmployee')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="geoCodedEmployeeExcel('geoCodedEmployee',geoCodedEmployeeCountData)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                    <!--  <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!geoCodedEmployeeCountDataShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row">
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Employee Id</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Name</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Address</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Device Type</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Number</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee EmailId</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>
                                        </div>
                                        <div class="constrainedGeoCoded row" ng-show="FilteredGeo.length > 0">
                                            <div class="overflowXauto" infinite-scroll="getGeoCodedEmployee('geoCodedEmployee')" infinite-scroll-container='".constrainedGeoCoded"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in FilteredGeo = (geoCodedEmployeeCountData | filter:geoCodedEmployeeCount)">
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.address}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.deviceType}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" ng-show="FilteredGeo.length == 0">
                                            <div class="noData">There is No Data for Geo Coded Employee Details</div>
                                        </div>
                                    </div>
                                    <!--  <div class="container-fluid">
                                            <div class="table-responsive constrainedGeoCoded pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getGeoCodedEmployee('geoCodedEmployee')" infinite-scroll-container='".constrainedGeoCoded"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>
                                                  <tbody ng-show="geoCodedEmployeeCountData.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for Geo Coded User Details</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>
                                                <tbody ng-show="geoCodedEmployeeCountData.length > 0">
                                                     <tr class = 'tabletdCenter'
                                                         ng-repeat="value in geoCodedEmployeeCountData | filter:geoCodedEmployeeCount">
                                                            <td><span>{{value.employeeId}}</span></td>
                                                            <td><span>{{value.employeeName}}</span></td>
                                                            <td><span>{{value.address}}</span></td>
                                                            <td><span>{{value.deviceType}}</span></td>
                                                            <td><span>{{value.mobileNumber}}</span></td>
                                                            <td><span>{{value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                </div>
                            </tab>
                            <tab ng-click="getDataYetToGeoCodedEmployee('yetToGEOCodedEmployee');getTabInfo('yetToGEOCodedEmployee');setSearchType();yetToGeoCodedEmployee=null">
                                <tab-heading>Non GeoCoded</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">Non GeoCoded Summary - <span class="badge">{{getYetToGeoCodedEmployee.length}} / {{getYetToGeoCodedEmployee[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!--   <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="yetToGeoCodedEmployee" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData,'yetToGEOCodedEmployee')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'yetToGEOCodedEmployee')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect" ">                        
                                              <button class = "btn btn-sm btn-success form-control exelBTN " ng-click = "yetToGEOCodedEmployeeExcel( 'yetToGEOCodedEmployee',getYetToGeoCodedEmployee) ">
                                              <i class = "icon-download-alt "></i>
                                              <span class = "marginLeft5 ">Excel</span>
                                              </button></div>
                                         </div>
                                       </div>

                                       <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4 " ng-show = "!getYetToGeoCodedEmployeeShow ">
                                        <img class = "spinner02 " src = "images/spinner02.gif " alt = "Getting Result.. ">
                                      </div> -->
                                      <div class=" ">
                                                <div class ="row ">
                                                        <div class="col-md-1 col-xs-1 divTableHeader nowrapText ">Employee Id</div>
                                                        <div class="col-md-2 col-xs-2 divTableHeader nowrapText ">Employee Name</div>
                                                        <div class="col-md-2 col-xs-2 divTableHeader nowrapText ">Employee Address</div>
                                                        <div class="col-md-1 col-xs-1 divTableHeader nowrapText ">Device Type</div>
                                                        <div class="col-md-2 col-xs-2 divTableHeader nowrapText ">Employee Number</div>
                                                        <div class="col-md-2 col-xs-2 divTableHeader nowrapText ">Employee EmailId</div>
                                                        <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                                        <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>

                                                </div>
                                                 <div ng-show="filteredYetToGeo.length> 0" class="constrainedNonGeoCoded row">
                                                <div class="overflowXauto" infinite-scroll="getDataYetToGeoCodedEmployee('yetToGEOCodedEmployee')" infinite-scroll-container='".constrainedNonGeoCoded"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                    <div class='row margin0' ng-repeat="Value in filteredYetToGeo = (getYetToGeoCodedEmployee | filter:yetToGeoCodedEmployee)">
                                                        <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                        <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                        <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.address}}</div>
                                                        <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.deviceType}}</div>
                                                        <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                        <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                        <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                        <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row" ng-show="filteredYetToGeo.length == 0">
                                                <div class="noData">There is No Data for Non Geo Coded Employee Details</div>
                                            </div>
                                        </div>
                                        <!-- <div class="container-fluid">
                                            <div class="table-responsive constrainedNonGeoCoded pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getDataYetToGeoCodedEmployee('yetToGEOCodedEmployee')" infinite-scroll-container='".constrainedNonGeoCoded"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>
                                                  <tbody ng-show="getYetToGeoCodedEmployee.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for Non GeoCoded Details</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>
                                                <tbody>
                                                     <tr class = 'tabletdCenter'
                                                     ng-show="getYetToGeoCodedEmployee.length > 0"
                                                         ng-repeat="value in getYetToGeoCodedEmployee | filter:yetToGeoCodedEmployee" >
                                                            <td><span>{{value.employeeId}}</span></td>
                                                            <td><span>{{value.employeeName}}</span></td>
                                                            <td><span>{{value.address}}</span></td>
                                                            <td><span>{{value.deviceType}}</span></td>
                                                            <td><span>{{value.mobileNumber}}</span></td>
                                                            <td><span>{{value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                    </div>
                            </tab>

                            <tab ng-click="getAppdownloadedButNoGeoCoded('appdownloadedButNoGeoCoded'); getTabInfo('appdownloadedButNoGeoCoded');setSearchType();appdownloadedButNoGeoCodedFilter=null">
                                <tab-heading>App download & Non GeoCoded</tab-heading>
                                <div class='invoiceTabContent pointer'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">App download & Non GeoCoded Summary - <span class="badge">{{appdownloadedButNoGeoCoded.length}} / {{appdownloadedButNoGeoCoded[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!--   <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="appdownloadedButNoGeoCodedFilter" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData,'appdownloadedButNoGeoCoded')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'appdownloadedButNoGeoCoded')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="appDownloadedButNoGeoCodedExcel('appdownloadedButNoGeoCoded',appdownloadedButNoGeoCoded)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                    <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!appdownloadedButNoGeoCodedShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row">
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Employee Id</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Name</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Address</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Device Type</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Number</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee EmailId</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>

                                        </div>
                                        <div ng-show="filteredAppDownloaded.length > 0" class="constrainedNonGeoCodedDown row">
                                            <div class="overflowXauto" infinite-scroll="getAppdownloadedButNoGeoCoded('appdownloadedButNoGeoCoded')" infinite-scroll-container='".constrainedNonGeoCodedDown"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in filteredAppDownloaded = (appdownloadedButNoGeoCoded | filter:appdownloadedButNoGeoCodedFilter)">
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                    <div class="col-md-3 col-xs-3 divTableData textBreak">{{Value.address}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.deviceType}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" ng-show="filteredAppDownloaded.length == 0">
                                            <div class="noData">There is No Data for App download & Non GeoCoded Details</div>
                                        </div>
                                    </div>
                                    <!-- <div class="container-fluid">
                                            <div class="table-responsive constrainedNonGeoCodedDown pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getAppdownloadedButNoGeoCoded('appdownloadedButNoGeoCoded')" infinite-scroll-container='".constrainedNonGeoCodedDown"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>
                                                   <tbody ng-show="appdownloadedButNoGeoCoded.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for App download & Non GeoCoded Details</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>
                                                <tbody ng-show="appdownloadedButNoGeoCoded.length > 0">
                                                     <tr class = 'tabletdCenter'
                                                         ng-repeat="value in appdownloadedButNoGeoCoded | filter:appdownloadedButNoGeoCodedFilter">
                                                            <td><span>{{value.employeeId}}</span></td>
                                                            <td><span>{{value.employeeName}}</span></td>
                                                            <td><span>{{value.address}}</span></td>
                                                            <td><span>{{value.deviceType}}</span></td>
                                                            <td><span>{{value.mobileNumber}}</span></td>
                                                            <td><span>{{value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                </div>
                            </tab>
                            <tab ng-click="getAppDownloadedAndGeocodedEmployees('appDownloadedAndGeocodedEmployees'); getTabInfo('appDownloadedAndGeocodedEmployees');setSearchType();appDownloadedAndGeocodedEmployeesFilter=null">
                                <tab-heading>App download & GeoCoded</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">App download & GeoCoded Summary - <span class="badge">{{appDownloadedAndGeocodedEmployees.length}} / {{appDownloadedAndGeocodedEmployees[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!-- <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="appDownloadedAndGeocodedEmployeesFilter" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData,'appDownloadedAndGeocodedEmployees')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'appDownloadedAndGeocodedEmployees')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="appDownloadedAndGeocodedEmployeesExcel('appDownloadedAndGeocodedEmployees',appDownloadedAndGeocodedEmployees)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                    <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!appDownloadedAndGeocodedEmployeesShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row">
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Employee Id</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Name</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Address</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Device Type</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Number</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee EmailId</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>

                                        </div>
                                        <div class="constrainedGeoCodedDown row" ng-show="filteredGeoCoded.length > 0">
                                            <div class="overflowXauto" infinite-scroll="getAppDownloadedAndGeocodedEmployees('appDownloadedAndGeocodedEmployees')" infinite-scroll-container='".constrainedGeoCodedDown"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in filteredGeoCoded = (appDownloadedAndGeocodedEmployees | filter:appDownloadedAndGeocodedEmployeesFilter)">
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.address}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.deviceType}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" ng-show="filteredGeoCoded.length == 0">
                                            <div class="noData">There is No Data for App download & GeoCoded Details</div>
                                        </div>
                                    </div>
                                    <!-- <div class="container-fluid">
                                            <div class="table-responsive constrainedGeoCodedDown pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getAppDownloadedAndGeocodedEmployees('appDownloadedAndGeocodedEmployees')" infinite-scroll-container='".constrainedGeoCodedDown"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>
                                                   <tbody ng-show="appDownloadedAndGeocodedEmployees.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for App download & GeoCoded Details</div>
                                                    </td>
                                                    </tr>
                                                  </tbody>
                                                <tbody ng-show="appDownloadedAndGeocodedEmployees.length > 0">
                                                     <tr class = 'tabletdCenter'
                                                         ng-repeat="value in appDownloadedAndGeocodedEmployees | filter:appDownloadedAndGeocodedEmployeesFilter">
                                                            <td><span>{{value.employeeId}}</span></td>
                                                            <td><span>{{value.employeeName}}</span></td>
                                                            <td><span>{{value.address}}</span></td>
                                                            <td><span>{{value.deviceType}}</span></td>
                                                            <td><span>{{value.mobileNumber}}</span></td>
                                                            <td><span>{{value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                </div>
                            </tab>
                            <tab ng-click="getNoAppDownloadedAndGeocodedEmployees('noAppDownloadedAndGeocodedEmployees');getTabInfo('noAppDownloadedAndGeocodedEmployees');setSearchType();noAppDownloadedAndGeocodedEmployeesFilter=null">
                                <tab-heading>GeoCoded & No App download</tab-heading>
                                <div class='invoiceTabContent'>
                                    <div class="firstRowInvoce ">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <h5 class="lengthVendorDashboard">GeoCoded & No App download Summary - <span class="badge">{{noAppDownloadedAndGeocodedEmployees.length}} / {{noAppDownloadedAndGeocodedEmployees[0].totalRecordCount}}</span></h5>
                                            </div>

                                            <!-- <div class = "col-md-6">
                                              </div> -->

                                            <div class="col-md-2">
                                                <input class="form-control" ng-model="noAppDownloadedAndGeocodedEmployeesFilter" placeholder="Filter" expect_special_char>
                                            </div>
                                            <div class="col-md-2 showRecordsSelect">
                                                <select ng-model="search.searchType" class="form-control width100per" ng-options="searchType.text for searchType in searchTypes track by searchType.value" ng-change="setSearchType(search.searchType)">
                               <option value="">Select Search Type</option>
                      </select>

                                            </div>
                                            <div class='col-md-2 padding0'>
                                                <div class="input-group floatLeft calendarInput">
                                                    <input ng-model="search.text" type="text" class="form-control" placeholder="{{setSearchPlcaeholer}}" ng-maxlength='50' maxlength='50' ng-keydown="$event.which === 13 &amp;&amp; searchEmployees(search,facilityData,'noAppDownloadedAndGeocodedEmployees')" ng-focus="searchIsEmpty = false" ng-class="{error: searchIsEmpty}" ng-disabled="!search.searchType">
                                                    <span class="input-group-btn">
                                   <button class="btn btn-success"  ng-disabled="!search.searchType || !search.text"
                                           ng-click="searchEmployees(search,facilityData,'noAppDownloadedAndGeocodedEmployees')">
                                   <i class = "icon-search searchServiceMappingIcon"></i></button></span>
                                                </div>
                                            </div>

                                            <div class="col-md-2 showRecordsSelect">
                                                <button class="btn btn-sm btn-success form-control exelBTN" ng-click="noAppDownloadedAndGeocodedEmployeesExcel('noAppDownloadedAndGeocodedEmployees',noAppDownloadedAndGeocodedEmployees)">
                                              <i class = "icon-download-alt"></i>
                                              <span class = "marginLeft5">Excel</span>
                                              </button></div>
                                        </div>
                                    </div>

                                      <!-- <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "!appDownloadedAndGeocodedEmployeesShow">
                                        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
                                      </div> -->
                                    <div class="">
                                        <div class="row">
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Employee Id</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Name</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Address</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Device Type</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee Number</div>
                                            <div class="col-md-2 col-xs-2 divTableHeader nowrapText">Employee EmailId</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Geo Code</div>
                                            <div class="col-md-1 col-xs-1 divTableHeader nowrapText">Facility Name</div>
                                        </div>
                                        <div class="constrainedGeoCodedNoDown row" ng-show="filteredNoApp.length > 0">
                                            <div class="overflowXauto" infinite-scroll="getNoAppDownloadedAndGeocodedEmployees('noAppDownloadedAndGeocodedEmployees')" infinite-scroll-container='".constrainedGeoCodedNoDown"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <div class='row margin0' ng-repeat="Value in filteredNoApp = (noAppDownloadedAndGeocodedEmployees | filter:noAppDownloadedAndGeocodedEmployeesFilter)">
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.employeeId}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.employeeName}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.address}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.deviceType}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.mobileNumber}}</div>
                                                    <div class="col-md-2 col-xs-2 divTableData textBreak">{{Value.emailId}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.lattitudeLongitude}}</div>
                                                    <div class="col-md-1 col-xs-1 divTableData textBreak">{{Value.facilityName}}</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" ng-show="filteredNoApp.length == 0">
                                            <div class="noData">There is No Data for No App Downloaded And Geo Coded Employees Details</div>
                                        </div>
                                    </div>
                                    <!-- <div class="container-fluid">
                                            <div class="table-responsive constrainedGeoCodedNoDown pointer">
                                              <table class = "invoiceByFuelTable table table-responsive container-fluid table-bordered"  infinite-scroll="getNoAppDownloadedAndGeocodedEmployees('noAppDownloadedAndGeocodedEmployees')" infinite-scroll-container='".constrainedGeoCodedNoDown"' infinite-scroll-distance="5" infinite-scroll-parent="true">
                                                <thead class ="tableHeading">
                                                    <tr>
                                                        <th>Employee Id</th>
                                                        <th>Employee Name</th>
                                                        <th>Employee Address</th>
                                                        <th>Device Type</th>
                                                        <th>Employee Number</th>
                                                        <th>Employee EmailId</th>
                                                    </tr>
                                                </thead>
                                                <tbody ng-show="noAppDownloadedAndGeocodedEmployees.length == 0">
                                                    <tr>
                                                    <td colspan="12">
                                                <div class = "noData">There is No Data for GeoCoded & No App download</div>
                                                    </td>
                                                    </tr>
                                                    </tbody>
                                                <tbody ng-show="noAppDownloadedAndGeocodedEmployees.length > 0">
                                                     <tr class = 'tabletdCenter'
                                                         ng-repeat="value in noAppDownloadedAndGeocodedEmployees | filter:noAppDownloadedAndGeocodedEmployeesFilter">
                                                            <td><span>{{value.employeeId}}</span></td>
                                                            <td><span>{{value.employeeName}}</span></td>
                                                            <td><span>{{value.address}}</span></td>
                                                            <td><span>{{value.deviceType}}</span></td>
                                                            <td><span>{{value.mobileNumber}}</span></td>
                                                            <td><span>{{value.emailId}}</span></td>
                                                      </tr>
                                                </tbody>
                                              </table>
                                            </div>
                                      </div> -->
                                </div>
                            </tab>

                        </tabset>
                    </tab>
                    
                    <tab ng-hide ="userRoleDetails == 'webuser' || multiFacilityFlg == 'N'">
                       <tab-heading ng-click = "getCombinedFacility();getTabInfo('combinedFacility')">Combined Facility </tab-heading>
                      <div class = "shiftTimeTabContent row">
                            <div class = "row">

                                <div class = 'col-md-6'>
                                    <div class="btn-group">
                                              <button type="button"
                                                      class="btn btn-warning btn-sm"
                                                      ng-click = "addEmployeeWise(facilityData)">Combined Employee Wise</button>

                                              </div>
                                </div>
                                <div class = 'col-md-2'>
                                </div>

                                <div class = 'col-md-2'>
                                </div>
                                <div class = 'col-md-2'>
                                    <input type = 'text'
                                           class = 'form-control'
                                           expect_special_char
                                           placeholder = 'Filter Facility Details'
                                           ng-model = 'filterFacilityDetails'>
                                </div>

                            </div>

                        <span><strong>Facility Wise</strong></span>
                        <table class = "escortAvailableTable table table-responsive container-fluid">
                                <thead class ="tableHeading">
                                      <th>Base Facility</th>
                                      <th>Sub Facility</th>
                                      <!-- <th>Edit</th> -->
                                      <!-- <th>Delete</th> -->
                                    </tr>
                                </thead>
                                <tbody ng-show="filteredFacility.length == 0">
                                <tr><td colspan="12">
                                <div class = "noData">There is No Data for Combined facility Details</div>
                                </td></tr>
                                </tbody>
                                <tbody ng-show="filteredFacility.length > 0">
                               <tr ng-repeat = "facility in filteredFacility =(compineFacilityDetails | filter: filterFacilityDetails)">
                               <td>{{facility.baseFacilityName}}</td>
                               <td>{{facility.subFacilityDetails}}</td>
                               <!-- <td><input type="button" class="btn btn-primary btn-xs" value="Edit" ng-click="editCombinedFacilityDetails(facility, $index)" name=""></td> -->
                               <!-- <td><input type="button" class="btn btn-danger btn-xs" value="Delete" ng-click="deleteFacilityDetails(facility, $index)" name="" ></td> -->
                               </tr>
                             </tbody>
                            </table>

                            <!-- <span><strong>Employee Id Wise</strong></span>

                            <table class = "escortAvailableTable table table-responsive container-fluid table-striped">
                                <thead class ="tableHeading">
                                      <th>Base Facility</th>
                                      <th>Mapped Employee Id</th>
                                      <th>Base Facility</th>
                                      <th>Edit</th>
                                      <th>Delete</th>
                                    </tr>
                                </thead>
                                <tbody>

                               <tr ng-repeat = "facility in compineFacilityDetails | filter: searchGeoLocation | filter: locationStatusSearch">

                               </tr>
                             </tbody>
                            </table> -->
                        </div>
                    </tab>

                    <tab ng-click = "getAllNodalNames(); getTabInfo('nodalPoints')" ng-hide ="userRoleDetails == 'webuser'">
                       <tab-heading>Nodal Points</tab-heading>
                      <div class = "shiftTimeTabContent row">
                            <div class = "row firstRowActionDiv">
                                <div class = 'col-md-6 addNewRouteButton'>
                                    <input type = 'button'
                                           value = 'Create New Nodal'
                                           class = 'btn btn-primary buttonRadius0 addShiftButton'
                                           ng-click = "createNewNodelPoint('lg', 'CreateNodal', facilityData)"
                                           required>
                                </div>
                                <div class = 'col-md-6'>
                                    <input type = 'text'
                                           class = 'form-control routeFilter'
                                           placeholder = 'Filter Nodal Routes'
                                           expect_special_char
                                           ng-model = 'searchNodelRoute'>
                                </div>
                            </div>
                            <div class = "col-md-12 accordianNodalPoint">
                               <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                  <div class="panel panel-default accordianPanel_serviceMapping" ng-repeat = "nodal in nodelNameData | filter : searchNodelRoute">
                                      <a data-toggle="collapse"
                                         data-parent="#accordion"
                                         href="#collapse{{nodal.routeId}}"
                                         aria-expanded="false"
                                         aria-controls="collapse{{nodal.routeId}}"
                                         ng-click = "getNodalPoints(nodal)"
                                         class = "noTextDecoration collapse{{nodal.routeId}}">
                                    <div class="panel-heading panelHeading_serviceMapping collapseZoneDiv{{nodal.routeId}}"
                                         role="tab"
                                         id="headingOne">
                                      <h4 class="panel-title heading_serviceMapping">
                                          <span ng-show="!nodal.editNodalToggleIsClicked">{{nodal.routeName}} </span>
                                          <span ng-show="nodal.editNodalToggleIsClicked">
                                          <input type="text" ng-model="nodal.routeName"
                                          ng-maxlength="50"
                                          maxlength="50"
                                          expect_special_char
                                          class="textBoxStyleNodal form-control"></span>
                                          <span class="floatRight badge customBadge badge_serviceZone"></span>

                                      </h4>

                                    </a>
                                   <input type = 'button'
                                                     class = 'btn btn-warning btn-sm floatRight nodalButtonToggleStyle'
                                                     ng-show = "!nodal.editNodalToggleIsClicked"
                                                     value = 'Edit'
                                                     ng-click = "editNodalToggle(nodal, $index)">
                                              <input type = 'button'
                                                     class = 'btn btn-success btn-sm floatRight nodalButtonToggleStyle'
                                                     ng-show = "nodal       .editNodalToggleIsClicked"
                                                     value = 'Save'
                                                     ng-click = "updateNodalToggle(nodal, $index)">
                                    </div>

                                    <div id="collapse{{nodal.routeId}}"
                                         class="panel-collapse collapse accordionContent_serviceMapping collapse{{nodal.routeId}}"
                                         role="tabpanel"
                                         aria-labelledby="headingOne">

                                        <div class = "col-md-3">
                                            <input type = "text"
                                                   class = "btn btn-success buttonRadius0 nodalPointAddButton"
                                                   value = "Add Nodal Point"
                                                   ng-click = "addNewNodalPoint('lg', nodal, 'CreateNodalPoint', facilityData)">
                                        </div>
                                        <div class = "col-md-3 col-md-offset-6 col-xs-6 searchDiv">
                                                  <input type = 'text'
                                                         class = "form-control floatRight"
                                                         ng-model = 'searchPoint'
                                                         placeholder = "Search Nodal Points">
                                              </div> <br>
                                      <div class="panel-body accorMainContent">

                                          <table class = "serviceMappingTable table table-responsive container-fluid" ng-show = "allNodelPointData.length>0">
                                            <thead class ="tableHeading">
                                                <tr>
                                                  <th>Nodal Point Id</th>
                                                  <th>Title</th>
                                                  <th>Description</th>
                                                  <th>GEO Code</th>
                                                  <th>Facility Name</th>
                                                  <th>Edit</th>
                                                  <th>Enable/Disable</th>
                                                </tr>
                                            </thead>
                                            <tbody ng-show="filteredNodalPoints.length == 0">
                                            <tr><td colspan="12">
                                            <div class = "noData">There is No Data for Nodal Point Details</div>
                                            </td></tr>
                                            </tbody>
                                            <tbody ng-show="filteredNodalPoints.length > 0">

                                               <tr ng-repeat = "point in filteredNodalPoints =(allNodelPointData  | filter : searchPoint)"
                                                   id = "point{{point.nodalPointId}}{{nodal.routeId}}" ng-class="{disableStyle: point.nodalPointFlg == 'Disable' , enableStyle: point.nodalPointFlg == 'Enable'}">
                                                 <td class = "col-md-2"><span>{{point.nodalPointId}}</span>
                                                 </td>
                                                 <td class = "col-md-4"><span ng-show="!point.editNodalIsClicked">{{point.nodalPointTitle}}</span>       <span ng-show="point.editNodalIsClicked"><input type="text" ng-model="point.nodalPointTitle" class="myProfileEditNodal"></span>
                                                 </td>
                                                 <td class = "col-md-4"><span ng-show="!point.editNodalIsClicked">{{point.nodalPointDescription}}</span>
                                                 <span ng-show="point.editNodalIsClicked"><input type="text" class="myProfileEditNodal" ng-model="point.nodalPointDescription"></span>
                                                 </td>
                                                 <td class = "col-md-4"><span>
                                                    <span class = "floatLeft map_serviceMappingIcon pointer"
                                                          ng-click = "editNodalPointLocation(point, $index)"
                                                          ng-show="point.nodalPointFlg == 'Enable'"
                                                          >
                                                        <i class ="icon-map-marker mapMarkerIcon"
                                                           tooltip="View on Map"
                                                           tooltip-placement="right"
                                                           tooltip-trigger="mouseenter"></i></span>{{point.nodalPoints}}</span>
                                                 
                                                 </td>
                                                 <td class = "col-md-2">{{point.facilityName}}</span>
                                                 </td>
                                                <td class='col-md-3'>
                                                    <input type = 'button'
                                                           class = 'btn btn-warning btn-sm'
                                                           ng-show = "!point.editNodalIsClicked"
                                                           value = 'Edit'
                                                           ng-disabled="point.nodalPointFlg == 'Disable'"
                                                           ng-click = "editNodalPoint(point, $index)">
                                                    <input type = 'button'
                                                           class = 'btn btn-success btn-sm'
                                                           ng-show = "point       .editNodalIsClicked"
                                                           value = 'Save'
                                                           ng-click = "updateNodalPoint(point, $index)">
                                                </td>
                                        <td class='col-md-3'>
                                                <input type="button"
                                               class="btn btn-success btn-xs"
                                               value="Enable"
                                               ng-show="point.nodalPointFlg == 'Disable'"
                                               ng-click="enableNodalPoint(point, $index)"
                                               name="">
                                        <input type="button"
                                               class="btn btn-danger btn-xs"
                                               value="Disable"
                                               ng-show="point.nodalPointFlg == 'Enable'"
                                               ng-click="disableNodalPoint(point, $index)"
                                               name="">
                                               </td>
                                               </tr>
                                             </tbody>
                                           </table>
                                        </div>
                                    </div>
                                  </div>
                            </div>
                        </div>
                    </tab>
                </tabset>
            </div>
           </div>
         </div>
   </div>
