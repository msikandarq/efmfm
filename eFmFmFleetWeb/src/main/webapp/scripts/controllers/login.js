(function() {
	$(".hidebutton").trigger('click');

	$(".empApp").hover(function() {
		$(".labelModuleIndexEmp").css("background", "#08c")
	}, function() {
		$(".labelModuleIndexEmp").css("background", "#fcfcfc")
	});
	$(".empApp").hover(function() {
		$(".labelModuleIndexEmp>div").css("color", "#fff")
	}, function() {
		$(".labelModuleIndexEmp>div").css("color", "#000")
	});

	$(".escortApp").hover(function() {
		$(".labelModuleIndexEscort").css("background", "#08c")
	}, function() {
		$(".labelModuleIndexEscort").css("background", "#fcfcfc")
	});
	$(".escortApp").hover(function() {
		$(".labelModuleIndexEscort>div").css("color", "#fff")
	}, function() {
		$(".labelModuleIndexEscort>div").css("color", "#000")
	});

	$(".adminApp").hover(function() {
		$(".labelModuleIndexAdmin").css("background", "#08c")
	}, function() {
		$(".labelModuleIndexAdmin").css("background", "#fcfcfc")
	});
	$(".adminApp").hover(function() {
		$(".labelModuleIndexAdmin>div").css("color", "#fff")
	}, function() {
		$(".labelModuleIndexAdmin>div").css("color", "#000")
	});

	var myeFmFmMain = angular.module("myeFmFmMain", [ 'ui.bootstrap',
			'UserValidation' ]);

	angular
			.module('myeFmFmMain')
			.controller(
					'indexCtrl',
					function($scope, $http, $timeout, $modal) {					
						$scope.loginDetails = function(user){
						       localStorage.setItem("userName", user.name);
						      }
						localStorage.removeItem('clickcount');
						$scope.isRegisteredNumberExist = false;
						$scope.currentTransportCode;
						$scope.currentRegisteredNumber;
						$scope.regexMin11to15Numbers = /^\d{11,15}$/;
						$scope.regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d][A-Za-z\d!@#$%^&*()_+]/;
						$scope.textSecurity = "textSecurityPassword";
						$scope.objType = "password";
						$scope.mouseoverPass = function() {
							var obj = document.getElementById('inputPassword3');
							obj.type = "text";
							$scope.objType = "text";
						}
						$scope.mouseoutPass = function() {
							var obj = document.getElementById('inputPassword3');
							obj.type = "password";
							$scope.objType = "password";
						}

						$scope.mouseoverForgotPass = function() {
							var objPassword = document.getElementById('password');
							objPassword.type = "text";
						}
						$scope.mouseoutForgotPass = function() {
							var objPassword = document.getElementById('password');
							objPassword.type = "password";
						}

						$scope.mouseoverConfForgotPass = function() {
							var objConfirmPassword = document.getElementById('password_c');
							objConfirmPassword.type = "text";
						}
						$scope.mouseoutConfForgotPass = function() {
							var objConfirmPassword = document.getElementById('password_c');
							objConfirmPassword.type = "password";
						}

						// Show Custome Alert Messages
						$scope.showalertMessage = function(message, hint) {
							$scope.alertMessage = message;
							$scope.alertHint = hint;

							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/showAlertTemplate.jsp',
										controller : 'showAlertCtrl',
										size : 'sm',
										resolve : {
											message : function() {
												return message;
											},
											hint : function() {
												return hint;
											}
										}
									});

							// Here the VendorId will be given to the Vendor
							// from backend
							modalInstance.result.then(function(result) {
							});
						};

						$scope.getTempCode = function(code) {
							$scope.currentTransportCode = "";
							$scope.currentRegisteredNumber = "";
							var data = {
								branchCode : code.transportCode,
								mobileNumber : code.registeredNumber
							};
							$http
									.post('services/user/webforgotpassword/',
											data)
									.success(
											function(data, status, headers,
													config) {
												$scope.tempcodeData = data;
												$scope.currentTransportCode = code.transportCode;
												$scope.currentRegisteredNumber = code.registeredNumber;
												if (data.status == "success") {
													$scope
															.showalertMessage("Temporary code has been sent to your mobile number. Please check your sms and use this code to change your password.");
													$('#forgetPassword').modal(
															"hide");
													$('#loginModal').modal(
															"hide");
													$('#changePassword').modal(
															"show");

												}
												else if (data.status == "disabletwenty") {
													$scope
															.showalertMessage("Your account is permanent disable for next 24 hours");
													$('#forgetPassword').modal(
															"hide");
													$('#loginModal').modal(
															"show");
													$('#changePassword').modal(
															"hide");
												}

												else if (data.status == "disable") {
													$scope
															.showalertMessage("The system is unable to send you the temporary code because the registered number and transport code you entered is currently Disable. Please contact the administrator for help.");
													$('#forgetPassword').modal(
															"hide");
													$('#loginModal').modal(
															"show");
													$('#changePassword').modal(
															"hide");

												}
												else if (data.status == "disableOTP") {								            
										             $scope
										               .showalertMessage("Your account is disable for "+data.otoEnableTime );
										             $('#forgetPassword').modal(
										               "hide");
										             $('#loginModal').modal(
										               "show");
										             $('#changePassword').modal(
										               "hide");

										            }

												else if (data.status == "invalid") {
													$scope
															.showalertMessage("The information you entered is invalid. Please try again.");
													$('#forgetPassword').modal(
															"show");
													$('#loginModal').modal(
															"show");
													$('#changePassword').modal(
															"hide");

												}
												else if (data.status == "invalidNum") {
													$scope
															.showalertMessage("Please check mobile number.This number is not registerted with us.");
													$('#forgetPassword').modal(
															"show");
													$('#loginModal').modal(
															"show");
													$('#changePassword').modal(
															"hide");

												}
												else if (data.status == "invalidCode") {
													$scope
															.showalertMessage("Transport code is invalid,Please check with transport team");
													$('#forgetPassword').modal(
															"show");
													$('#loginModal').modal(
															"show");
													$('#changePassword').modal(
															"hide");

												}
												else{
													$scope
													.showalertMessage("Given details are invalid,please check with transport team");
											$('#forgetPassword').modal(
													"show");
											$('#loginModal').modal(
													"show");
											$('#changePassword').modal(
													"hide");
												}
																							}).error(
											function(data, status, headers,
													config) {
												// log error
											});
						};

						$scope.changePassword = function(newPass) {
								var data = {
									branchCode : $scope.currentTransportCode,
									mobileNumber : $scope.currentRegisteredNumber,
									tempCode : newPass.tempCode,
									newPassword : newPass.newPassword
								};
								$http
										.post(
												'services/user/changeWebForgotPassword/',
												data)
										.success(
												function(data, status, headers,
														config) {
													$scope.changePasswordData = data;
													if ($scope.changePasswordData.status == 'success') {
														$scope
																.showalertMessage("Your password has been changed successfully.");
														$('#forgetPassword')
																.modal("hide");
														$('#loginModal').modal(
																"show");
														$('#changePassword')
																.modal("hide");
													}
													else if ($scope.changePasswordData.status == 'wrongPattern') {
														$scope
																.showalertMessage("Password must contain at least one lowercase letter,upper case letter,a special character and at least one digit");
														$('#forgetPassword')
																.modal("hide");
														$('#loginModal').modal(
																"show");
														$('#changePassword')
																.modal("hide");
													}
													
													else if ($scope.changePasswordData.status == 'fail') {
														$scope
																.showalertMessage("Failed to change the password.Please try again.");

														$('#forgetPassword')
																.modal("hide");
														$('#loginModal').modal(
																"hide");
														$('#changePassword')
																.modal("show");
													}
													else if ($scope.changePasswordData.status == 'disable') {
														$scope
																.showalertMessage("The system is unable to send you the temporary code because the registered number and transport code you entered is currently Disable. Please contact the Administrator for help.");
														$('#forgetPassword')
																.modal("hide");
														$('#loginModal').modal(
																"show");
														$('#changePassword')
																.modal("hide");
													}
													else if ($scope.changePasswordData.status == 'invalid') {
														$scope
																.showalertMessage("The information you entered is invalid. Please try again.");

														$('#forgetPassword')
																.modal("hide");
														$('#loginModal').modal(
																"hide");
														$('#changePassword')
																.modal("show");
													}
													else if ($scope.changePasswordData.status == 'oldpass') {
														$scope
																.showalertMessage("Sorry you can not reuse your last "+$scope.changePasswordData.numberOfPasswords+' passwords as new password.Your last password change date and time is '+$scope.changePasswordData.lastChangeDateTime);

														$('#forgetPassword')
																.modal("hide");
														$('#loginModal').modal(
																"hide");
														$('#changePassword')
																.modal("show");
													}

												}).error(
												function(data, status, headers,
														config) {
												})
							
						};
					});
}());