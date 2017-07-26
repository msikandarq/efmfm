<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    Main Page
@State          
@URL               

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>eFmFm - Enterprise Find Me Follow Me</title>
        <link rel="icon" type="image/png" href="images/favicon.png" />
<link href="bower_components/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="bower_components/bootstrap/css/bootstrap-theme.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="styles/fonts/font.css" type="text/css">
<link rel="stylesheet"
	href="bower_components/font-awesome/css/font-awesome.css"
	type="text/css">
<link rel="stylesheet" href="styles/eFmFmStyle.css">
<link rel="stylesheet" href="styles/index2.css">
<script src="bower_components/jquery/dist/jquery.min.js"></script>
<script src="bower_components/angular/angular.js"></script>
<script src="scripts/controllers/login.js"></script>
<script src="bower_components/angular-bootstrap/ui-bootstrap-tpls2.js"></script>
<script src="bower_components/bootstrap/js/bootstrap.min.js"></script>
<link href="bower_components/sweetalert/dist/sweetalert.css"
    rel="stylesheet">
 <script src="bower_components/sweetalert/dist/sweetalert.min.js"></script>
<style type="text/css">
.error-template {padding: 40px 15px;text-align: center;}
.error-actions {margin-top:15px;margin-bottom:15px;}
.error-actions .btn { margin-right:10px; }
.errorTemplateStyle { background-color: whitesmoke;}
</style>
</head>
<body ng-app="myeFmFmMain">

	<div class="indexPageTemplate container-fluid"
		ng-controller="indexCtrl">
		<div class="row topBanner">
			<div class="floatRight loginLink" data-toggle="modal"
				data-target="#loginModal">Login</div>
		</div>
		<div class="row">
			<div class="col-md-12 ">
				<img src="images/loginBgImage.jpg" class="img-responsive">
			</div>
		</div>
		<!--    MODAL-->
		<button type="button" class="btn btn-primary btn-lg hidebutton"
			data-toggle="modal" data-target="#loginModal"></button>
		<div class="modal fade" id="loginModal" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modalHeaderStyle">
						<button type="button" class="close headerCloseModal" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel"><img src="images/efmfm-logo.png"></h4>
					</div>
					<div class="modal-body bgLogin">
						<!--     <form class="form-horizontal formLogin" method="post">-->
						<form class="form-horizontal formLogin" method="post"
							action="j_spring_security_check" name="login_form" autocomplete="off">
							<div class="form-group">
								<div class="col-sm-1"></div>
								<label for="inputEmail3" class="col-sm-2 control-label">UserName</label>
								<div class="col-sm-8">
									<div class="input-group">
										<input type="text" 
											   class="form-control" 
											   ng-model="user.name" 
											   id="inputEmail3" 
											   placeholder="Username" 
											   name="j_username" 
											   autocomplete="off"
											   aria-describedby="basic-addon1" 
											   data-rule-required="true">
										<span class="input-group-addon" 
											  id="basic-addon1">
											  <i class="icon-user pointer"></i></span>
									</div>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-1"></div>
								<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
								<div class="col-sm-8">
									<div class="input-group">
									<input type="password" class="stealthy" tabindex="-1">
										<input type="{{objType}}" 
											   class="form-control" 
											   ng-model="user.password" 
											   id="inputPassword3" 
											   placeholder="Password" 
											   name="j_password" 
											   autocomplete="off"
											   aria-describedby="basic-addon1" 
											   data-rule-required="true">
									<input type="password" class="stealthy" tabindex="-1">
										<span class="input-group-addon" 
											  id="basic-addon1">
											  <i class="icon-eye-open pointer" ng-mouseenter="mouseoverPass()" ng-mouseleave="mouseoutPass()" ></i></span>
											  
									</div>
								</div>
							
							</div>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-9">
									<label class="checkbox displayNone"> <input
										type="checkbox" name="_spring_security_remember_me"
										value="true" checked> Keep Me Login
									</label>
									<button type="submit" class="btn blueBtn"
          ng-model="user.submit" ng-click="loginDetails(user)">Sign in</button>
									
								</div>
							</div>
								<div class="form-group mainDivForgetPassword">
								<div class="col-sm-offset-3 col-sm-9">
									<label class="checkbox displayNone" > <input
										type="checkbox" name="_spring_security_remember_me"
										value="true" checked>
									</label>
									<div>
										<span data-target="#forgetPassword" class="forgetPasswordLink pointer"
										 id="forgetPasswordButton"
										data-toggle="modal"
										data-dismiss="modal">Forget Password?</span></div>
								</div>
							</div>
						</form>
					</div>
					

					<div class="modal-footer login-footer-style">
					<div class = "row login-footer">
	                    <div class = "col-sm-6 text-left"><p class="login-footer-style">Copyright @ NGFV<p></div>
						<div class = "col-sm-6 text-right"><p><a class="login-footer-style" href="http://efmfm.com/" target="_blank">www.efmfm.com</a></p></div>
	                </div>
	                </div>
				</div>
			</div>
		</div>

<!--        ------------------------------------------------------------------------------------ --->
        
        <div class="modal fade" id="invalidPassword1" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header invalidPasswordHeader">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Alert</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal formLogin"
							name="invalidPasswordForm">
                            <div class = "col-md-12 invalidText">You have entered an invalid Password or User Name. Please try again</div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-4 invalidButton">
									<button type="submit" id="getTempPasswordButton"
										class="btn blueBtn floatLeft" data-dismiss="modal"
										ng-disabled="invalidPasswordForm.$invalid">Ok</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
        
<!--        ------------------------------------------------------------------------------------- -->

<!--        ------------------------------------------------------------------------------------ --->
        
        <div class="modal fade" id="invalidPassword2" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header invalidPasswordHeader">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Alert</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal formLogin"
							name="invalidPasswordForm">
                            <div class = "col-md-12 invalidText">Your password has been reset as per your application setting.Please do a forget password</div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-4 invalidButton">
									<button type="submit" id="getTempPasswordButton"
										class="btn blueBtn floatLeft" data-dismiss="modal"
										ng-disabled="invalidPasswordForm.$invalid">Ok</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
        
<!--        ------------------------------------------------------------------------------------- -->

<!--        ------------------------------------------------------------------------------------ --->
        
        <div class="modal fade" id="invalidPassword3" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header invalidPasswordHeader">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Alert</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal formLogin"
							name="invalidPasswordForm">
                            <div class = "col-md-12 invalidText">Your password will reset after one more wrong attempt.</div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-4 invalidButton">
									<button type="submit" id="getTempPasswordButton"
										class="btn blueBtn floatLeft" data-dismiss="modal"
										ng-disabled="invalidPasswordForm.$invalid">Ok</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
        
<!--        ------------------------------------------------------------------------------------- -->

<!--        ------------------------------------------------------------------------------------ --->
        
        <div class="modal fade" id="invalidPassword3" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header invalidPasswordHeader">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Alert</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal formLogin"
							name="invalidPasswordForm">
                            <div class = "col-md-12 invalidText">Your password will reset after one more wrong attempt.</div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-4 invalidButton">
									<button type="submit" id="getTempPasswordButton"
										class="btn blueBtn floatLeft" data-dismiss="modal"
										ng-disabled="invalidPasswordForm.$invalid">Ok</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
        
<!--        ------------------------------------------------------------------------------------- -->

<!--        ------------------------------------------------------------------------------------ --->
        
        <div class="modal fade" id="invalidPassword4" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header invalidPasswordHeader">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Alert</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal formLogin"
							name="invalidPasswordForm">
                            <div class = "col-md-12 invalidText">Your password has been reset due to wrong number of attempt.Please do a forget password</div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-4 invalidButton">
									<button type="submit" id="getTempPasswordButton"
										class="btn blueBtn floatLeft" data-dismiss="modal"
										ng-disabled="invalidPasswordForm.$invalid">Ok</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
        
<!--        ------------------------------------------------------------------------------------- -->


		<div class="modal fade" id="forgetPassword" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Forgot Password</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal formLogin padding20"
							name="getTempCodeForm">
							<div class="form-group">
								<div class="col-sm-1"></div>
								<label for="inputEmail3" class="col-sm-12 control-label">
									<span class="floatLeft">Registered Mobile Number with
										Country Code</span>
								</label>
								<div class="col-sm-12">
									<input type="text" ng-model="code.registeredNumber"
										class="m-wrap form-control userNameInput"
										placeholder="Registered Mobile Number"
										autocomplete="off"
										data-rule-required="true" data-rule-email="true"
										ng-minlength='11' ng-maxlength="15" maxlength="15" required>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-1"></div>
								<label for="inputEmail3" class="col-sm-12 control-label">
									<span class="floatLeft">Enter Transport Code</span>
								</label>
								<div class="col-sm-12">
									<input type="text" class="m-wrap form-control userNameInput"
										ng-model="code.transportCode" placeholder="Transport Code" autocomplete="off"
										name="j_registeredMobileNumber" data-rule-required="true"
										data-rule-email="true" maxlength="50" required>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-9">
									<button type="submit" id="getTempPasswordButton"
										class="btn blueBtn floatLeft" ng-model="user.submit"
										ng-click="getTempCode(code)" data-dismiss="modal"
										ng-disabled="getTempCodeForm.$invalid">Get Temporary
										Password</button>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<!--            <div class = "row">
	                    <div class = "col-sm-4"></div>
	                    <div class = "col-sm-4"><img src = "images/avatar1_small.jpg" class = "companyLogo" alt = "Company Logo"></div>
	                    <div class = "col-sm-4"></div>
	                </div> -->
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="changePassword" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span>&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Change Password</h4>
					</div>
					<div class="modal-body">
						<!--     <form class="form-horizontal formLogin" method="post">-->
						<form class="form-horizontal formLogin padding20"
							name="changePasswordForm">
							<div class="form-group">
								<div class="col-sm-1"></div>
								<label for="inputEmail3" class="col-sm-12 control-label">
									<span class="floatLeft">Enter Temporary Code Sent On
										your Registered Number</span>
								</label>
								<div class="col-sm-12">
									<input type="text" ng-model="newPass.tempCode"
										class="m-wrap form-control userNameInput"
										placeholder="Enter Temporary Code Sent On your Registered Number"
										data-rule-required="true" data-rule-email="true" autocomplete="off"
										ng-minlength='4' ng-maxlength="10" maxlength="10" required>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-1"></div>
								<label for="inputEmail3" class="col-sm-12 control-label">
									<span class="floatLeft">New Password</span>
								</label>


								<div class="col-sm-12">

								<div class="input-group">
										<!-- <input type="text" 
											   class="form-control" 
											   ng-model="user.name" 
											   id="inputEmail3" 
											   placeholder="Username" 
											   name="j_username" 
											   aria-describedby="basic-addon1" 
											   data-rule-required="true"> -->

										<input type="password" 
                                        ng-model="newPass.newPassword"
										id="password" 
                                        name="password"
										class="m-wrap form-control userNameInput"
										placeholder="New Password" 
                                        ng-minlength='5' 
                                        ng-maxlength="15"
                                        autocomplete="off"
                                        required>
										<span class="input-group-addon" 
											  id="basic-addon1">
											   <i class="icon-eye-open pointer" ng-mouseenter="mouseoverForgotPass()" ng-mouseleave="mouseoutForgotPass()" ></i></span>
									</div>
									<!-- <input type="password" 
                                        ng-model="newPass.newPassword"
										id="password" 
                                        name="password"
										class="m-wrap form-control userNameInput"
										placeholder="New Password" 
                                        ng-minlength='4' 
                                        ng-maxlength="10"
										maxlength="10" 
                                        ng-pattern = "regexPassword"
                                        required> -->
								<div class = "hintItallic">
                                    <span ng-show="!changePasswordForm.password.$error.required && (changePasswordForm.password.$error.minlength || changePasswordForm.password.$error.maxlength) && changePasswordForm.password.$dirty">Passwords must be between 4 and 8 characters.</span>
                                    <span ng-show="!changePasswordForm.password.$error.required && !changePasswordForm.password.$error.minlength && !changePasswordForm.password.$error.maxlength && changePasswordForm.password.$error.pattern && changePasswordForm.password.$dirty">(Must contain one lower &amp; uppercase letter,atleast one  number and one symbol.)</span>
                                </div>
 
								</div>
							</div>

							<div class="form-group"> 
								<div class="col-sm-1"></div>
								<label for="inputEmail3" class="col-sm-12 control-label">
									<span class="floatLeft">Re-enter New Password</span>
								</label>
								<div class="col-sm-12">

								<div class="input-group">
										<input type="password" ng-model="newPass.confrimPassword"
										id="password_c" name="password_c" valid-password-c
										class="m-wrap form-control userNameInput"
										placeholder="Re-enter New Password" ng-minlength='5'
										ng-maxlength="15" required autocomplete="off">
										<span class="input-group-addon" 
											  id="basic-addon1">
											   <i class="icon-eye-open pointer" ng-mouseenter="mouseoverConfForgotPass()" ng-mouseleave="mouseoutConfForgotPass()" ></i></span>
								</div>
								

									<!-- <input type="password" ng-model="newPass.confrimPassword"
										id="password_c" name="password_c" valid-password-c
										class="m-wrap form-control userNameInput"
										placeholder="Re-enter New Password" ng-minlength='4'
										ng-maxlength="10" maxlength="10" required> -->
									                                 <br / >
                                <div class = "incorrectPassword" ng-show="!changePasswordForm.password_c.$error.required && changePasswordForm.password_c.$error.noMatch && changePasswordForm.password.$dirty">Passwords do not match.</div>
 
								</div>
							</div>



							<div class="form-group">
								<div class="col-sm-9">
									<button type="button" class="btn blueBtn floatLeft"
										ng-click="changePassword(newPass)"
										ng-disabled="changePasswordForm.$invalid">Change
										Password</button>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<!--            <div class = "row">
	                    <div class = "col-sm-4"></div>
	                    <div class = "col-sm-4"><img src = "images/avatar1_small.jpg" class = "companyLogo" alt = "Company Logo"></div>
	                    <div class = "col-sm-4"></div>
	                </div> -->
					</div>
				</div>
			</div>
		</div>
		<!--End of the Modal   -->
		<!-- <div class="mainIndexContent">
			<div class="row description_home">
				<div class="col-md-4 col-xs-12">
					<div class="col-md-12">
						<i class="icon-location-arrow iconIndex blue floatLeft"></i>
						<div class="heading_home col-md-10 test">Route Optimization</div>
					</div>
					<p>Real time tracking of technician information will be shared
						within view map. ASP's status, speed, distance, and ETA will be
						viewed within view map.</p>
				</div>
				<div class="col-md-4 col-xs-12">
					<div class="col-md-12">
						<i class="icon-ok iconIndex red floatLeft"></i>
						<div class="heading_home col-md-10">Geofencing</div>
					</div>
					<p>The system allows you to set up a series of geographic zones
						together with the time-based rules.</p>
				</div>
				<div class="col-md-4 col-xs-12">
					<div class="col-md-12">
						<i class="icon-resize-small iconIndex green floatLeft"></i>
						<div class="heading_home col-md-10">Location/route maps</div>
					</div>
					<p>The efmfm system comes with integrated mapping with google
						maps. Google Mapping provides real-time updates to vehicle
						positions overlaid on maps, and Google's satellite, hybrid and
						terrain views.</p>
				</div>
			</div>
			<div class="row moreDescription">
				<div class="col-md-3 col-xs-12 moduleIndex">
					<h2 class="loginH2">Modules</h2>
					<p class="moreDescriptionP">Mobile device with in the vehicle
						displays list of available employee.A vital feature "Scheduler"
						helps in automatic optimization and assignment of employee within
						vehicles.</p>
				</div>
				<div class="col-md-3 col-xs-12 empApp">

					<img src="images/dash.png">
					<div class="labelModuleIndexEmp">
						<div class="row labelModuleIndex1">
							<div class="col-md-12 col-xs-12">Employee Application</div>
						</div>
						<div class="row labelModuleIndex2">
							<div class="col-md-12 col-xs-12">Module</div>
						</div>
					</div>
				</div>
				<div class="col-md-3 col-xs-12 escortApp">
					<img src="images/scheduler.png">
					<div class="labelModuleIndexEscort">
						<div class="row labelModuleIndex1">
							<div class="col-md-12 col-xs-12">Escort Application</div>
						</div>
						<div class="row labelModuleIndex2">
							<div class="col-md-12 col-xs-12">Module</div>
						</div>
					</div>
				</div>
				<div class="col-md-3 col-xs-12 adminApp">
					<img src="images/seller.png">
					<div class="labelModuleIndexAdmin">
						<div class="row labelModuleIndex1">
							<div class="col-md-12 col-xs-12">Admin Application</div>
						</div>
						<div class="row labelModuleIndex2">
							<div class="col-md-12 col-xs-12">Module</div>
						</div>
					</div>
				</div>
			</div>
		</div> -->
		<!-- <div class="footerIndex row">
			<span>Copyright @ Newt|Global Consulting LLC.</span>
		</div> -->
	</div>
	<script src="bower_components/angular-bootstrap/ui-bootstrap-tpls2.js"></script>
	<script src="bower_components/jquery/dist/jquery.min.js"></script>
	<script src="bower_components/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="bower_components/bootstrap/js/jasny-bootstrap.min.js"> </script>
	<script src="scripts/controllers/login.js"></script>

	<script src="scripts/module/passwordValidation.js"></script>
	<script src="scripts/controllers/commonJs/showAlert.js"></script>
	<script type="text/javascript">
        
        $('#getTempPasswordButton').click(function(){            
              $('#loginModal').modal("show");
        })
         var alreadyloggedIn ="<%=request.getSession().getAttribute("access")%>";
    	 var flag = " ${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}";
    	 var failedToLogin ="<%=request.getSession().getAttribute("failedFromLdap")%>";
	 if(alreadyloggedIn=="alreadylogin"){
	 	var previousWindowKeyDown = window.onkeydown;
		 swal({
             title: "Already Login?",
             text: "Requested URL already opened in another window.If you want to continue here press Yes, want to continue here or press cancel?",
             type: "warning",
             showCancelButton: true,
             confirmButtonColor: "#DD6B55",
             confirmButtonText: "Yes, want to continue here!",
             cancelButtonText: "No, cancel it!",
             closeOnConfirm: false,
             closeOnCancel: false,
             allowEscapeKey: false
           },
           function(isConfirm){
           	window.onkeydown = previousWindowKeyDown;
             if (isConfirm) {
             	var previousWindowKeyDown = window.onkeydown;
						swal({
								title : "Approved! Please do a login again",
								text : "Press ok to continue",
							});
						window.onkeydown = previousWindowKeyDown;
							swal("Approved! Please do a login again",
									"We have close all your other sessions.",
									"success");
							var userName = localStorage.getItem("userName");
							var URL = "/eFmFmFleetWeb/services/authorization/clearSession/"+userName;
							$.ajax({
								
								type: 'POST',
								contentType: 'text/plain',
								url : URL,
								dataType: 'text',
								success : function(data) {
								}
							}).done(function(data) {

							}).error(function(data) {

							});

						} else {
							swal({
								title : "Cancelled Successfully!",
								text : "I will close in 2 seconds.",
								timer : 2000,
								showConfirmButton : false
							});
						}
					});
		} else if (alreadyloggedIn == "wrongCredentials") {
			var previousWindowKeyDown = window.onkeydown;
			swal({
				title : "Wrong Credentials",
				text : "Invalid Username and Password!",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "Try Again!",
				closeOnConfirm : false,
				allowEscapeKey : false
			}, function() {
				window.onkeydown = previousWindowKeyDown;
				swal({
					title : "Auto close alert!",
					timer : 0,
					showConfirmButton : false
				});
			});
		} else if (alreadyloggedIn == "tempLogin") {
			var previousWindowKeyDown = window.onkeydown;
			swal(
					{
						title : "Change Password",
						text : "For your personal account security reason please change your temporary password to strong password for login.please get a new password",
						type : "warning",
						showCancelButton : false,
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "Try Again!",
						closeOnConfirm : false,
						allowEscapeKey : false
					}, function() {
						window.onkeydown = previousWindowKeyDown;
						swal({
							title : "Auto close alert!",
							timer : 0,
							showConfirmButton : false
						});
					});
		} else if (alreadyloggedIn == "passReset") {
			var previousWindowKeyDown = window.onkeydown;
			swal(
					{
						title : "Reset Password",
						text : "Your password has been reset as per your application setting.Please get a new password!",
						type : "warning",
						showCancelButton : false,
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "Reset Password",
						closeOnConfirm : false,
						allowEscapeKey : false
					}, function() {
						window.onkeydown = previousWindowKeyDown;
						swal({
							title : "Auto close alert!",
							timer : 0,
							showConfirmButton : false
						});
					});
		} else if (alreadyloggedIn == "lastattempt") {
			var previousWindowKeyDown = window.onkeydown;
			swal(
					{
						title : "Last Attempt Warning",
						text : "Your password will reset after one more wrong attempt!",
						type : "warning",
						showCancelButton : false,
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "Yes",
						closeOnConfirm : false,
						allowEscapeKey : false
					}, function() {
						window.onkeydown = previousWindowKeyDown;
						swal({
							title : "Auto close alert!",
							timer : 0,
							showConfirmButton : false
						});
					});

		} else if (alreadyloggedIn == "wrongAttempt") {
			var previousWindowKeyDown = window.onkeydown;
			swal(
					{
						title : "Number Of time wrong attempt",
						text : "Your password has been reset due to wrong number of attempt.Please get a new password!",
						type : "warning",
						showCancelButton : false,
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "Yes",
						closeOnConfirm : false,
						allowEscapeKey : false
					}, function() {
						window.onkeydown = previousWindowKeyDown;
						swal({
							title : "Auto close alert!",
							timer : 0,
							showConfirmButton : false
						});
					});

		} else if (alreadyloggedIn == "disable") {
			var previousWindowKeyDown = window.onkeydown;
			swal({
				title : "Account Disabled",
				text : "Your account is permanent disable for next 24 hours",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "Ok",
				closeOnConfirm : false,
				allowEscapeKey : false
			}, function() {
				window.onkeydown = previousWindowKeyDown;
				swal({
					title : "Auto close alert!",
					timer : 0,
					showConfirmButton : false
				});
			});

		}

		else if (alreadyloggedIn == "inactive") {
			var previousWindowKeyDown = window.onkeydown;
			swal({
				title : "Your account is inactive",
				text : "Please get a new password",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "Ok",
				closeOnConfirm : false,
				allowEscapeKey : false
			}, function() {
				window.onkeydown = previousWindowKeyDown;
				swal({
					title : "Auto close alert!",
					timer : 0,
					showConfirmButton : false
				});
			});
		} else if (flag.length == 1) {
		}

		
	</script>

<script>
//Right Click Disable Option for all browsers - so 3 rd party people can't get our UI and server side source

document.addEventListener('contextmenu', event => event.preventDefault());
$(document).keydown(function(event){
    if(event.keyCode==123){
        return false;
    }
    else if (event.ctrlKey && event.shiftKey && event.keyCode==73){        
             return false;
    }
});

$(document).on("contextmenu",function(e){        
   e.preventDefault();
});
</script>
</body>
</html>