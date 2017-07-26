/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       importDataCtrl
@Modal Controllers      importEmployeeCtrl, importEmployeeRequestCtrl, importVendorMasterDataCtrl, importDriverMasterDataCtrl, importVehicleMasterDataCtrl, 
                        importAreaMasterDataCtrl, importEscortMasterDataCtrl, uploadRouteCtrl, exportRouteCtrl, downloadRouteCtrl
fail@template               

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
05/04/2016  Saima Aziz      Added new methods to support functionalities to Import Data for Guest Request
 */

(function() {
	var importDataCtrl = function($scope, $modal, $http, $timeout, ngDialog) {

		if (!$scope.isImportDataActive || $scope.isImportDataActive == "false") {
			$state.go('home.accessDenied');
		}

		else {

			// OPEN THE MODAL WHEN IMPORT EMPLOYEE CLICKED
			$scope.openImportEmployee = function() {

				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importEmployeeForm.jsp',
					controller : 'importEmployeeCtrl',
					resolve : {}
				});

				modalInstance.result.then(function(result) {
				});
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT EMPLOYEE REQUEST CLICKED


			$scope.importEmployeeRequest = function() {
				if(branchCode == 'GNPTJP'){
						var modalInstance = $modal.open({
						templateUrl : 'partials/modals/importCabRequestGenpact.jsp',
						controller : 'importEmployeeRequestCtrl',
						resolve : {}
					});
				}else{
						var modalInstance = $modal.open({
						templateUrl : 'partials/modals/importCabRequest.jsp',
						controller : 'importEmployeeRequestCtrl',
						resolve : {}
					});
				}

				
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT EMPLOYEE VENDOR DATA CLICKED
			$scope.importVendorData = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importVendorData.jsp',
					controller : 'importVendorMasterDataCtrl',
					resolve : {}
				});
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT DRIVER DATA CLICKED
			$scope.importDriverData = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importDriverData.jsp',
					controller : 'importDriverMasterDataCtrl',
					resolve : {}
				});
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT VEHICLE DATA CLICKED
			$scope.importVehicleData = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importVehicleData.jsp',
					controller : 'importVehicleMasterDataCtrl',
					resolve : {}
				});
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT AREA MASTER DATA CLICKED
			$scope.importAreaData = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importAreaData.jsp',
					controller : 'importAreaMasterDataCtrl',
					resolve : {}
				});
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT Escort MASTER DATA CLICKED
			$scope.importEscortData = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importEscortData.jsp',
					controller : 'importEscortMasterDataCtrl',
					resolve : {}
				});
			};

			// OPEN THE MODAL WHEN IMPORT Escort MASTER DATA CLICKED
			$scope.importGuestRequestData = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/imporGuestRequestData.jsp',
					controller : 'importGuestRequestMasterDataCtrl',
					resolve : {}
				});
			};

			// OPEN THE MODAL WHEN IMPORT NODAL REQUEST CLICKED
			$scope.importNodalRequest = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importNodalRequest.jsp',
					controller : 'importNodalRequestCtrl',
					resolve : {}
				});
			}; // End Of FUNCTION

			// OPEN THE MODAL WHEN IMPORT Batch Delete Excel CLICKED
			$scope.importBatchDeleteExcel = function() {
				var modalInstance = $modal.open({
					templateUrl : 'partials/modals/importBatchDeleteExcel.jsp',
					controller : 'importBatchDeleteExcelCtrl',
					resolve : {}
				});
			};
		}

	};

	// Employee Master Data Model Controller
	var importEmployeeCtrl = function($scope, $modal, $modalInstance, $state,
			$http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importEmployeeErrorModal.jsp',
										controller : 'importEmployeeErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});
							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);
						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};
		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	// Upload Driver CTRL

	var uploadExcelDriverCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							if (response == '') {
							} else {
								var modalInstance = $modal
										.open({
											templateUrl : 'partials/modals/uploadDriverErrorModal.jsp',
											controller : 'uploadDriverErrorCtrl',
											resolve : {
												result : function() {
													return response;
												}
											}
										});
							}

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};
		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	/** *********************** Error Controllers *************************** */

	/* Import Employee Data Error Controller */

	var importEmployeeErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {
		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Employee Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Employee Data successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}
	};

	/* Upload Driver Error Controller */

	var uploadDriverErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {
		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {

				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql('SELECT INTO XLSX("Upload Driver Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Employee Data successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}
	};

	/* Import Employee Request Error Controller */

	var importEmployeeRequestErrorCtrl = function($scope, $modalInstance,
			result, ngDialog, $timeout) {
		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {

				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Employee Request Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Employee Request successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);
			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Vendor Data Error Controller */

	var importVendorDataErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {

				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Vendor Data Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Ventor Data successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Driver Data Error Controller */

	var importDriverDataErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Driver Data Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Driver Data successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Vehicle Data Error Controller */

	var importVehicleDataErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Vehicle Data Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Vehicle Data successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);
			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Escort Data Error Controller */

	var importEscortDataErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Escort Data Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Escort Data successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Area Data Error Controller */

	var importAreaDataErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {

				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Home Route Data Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Home Route successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Nodal Request Error Controller */

	var importNodalRequestErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql('SELECT INTO XLSX("Nodal Request Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Nodal Request successfully uploaded',
				plain : true
			});

			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);
			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Guest Request Error Controller */

	var importGuestRequestErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {

		$scope.result = result;
		$scope.resultShow = $scope.result.length;

		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});
				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql(
						'SELECT INTO XLSX("Import Guest Request Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Guest Request successfully uploaded',
				plain : true
			});

			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);
			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}

	};

	/* Import Guest Request Error Controller */

	var importBatchDeleteExcelErrorCtrl = function($scope, $modalInstance,
			result, ngDialog) {

		$scope.result = result;
		$scope.saveInExcel = function() {

			$scope.saveInExcel = [];

			angular.forEach($scope.result, function(item) {
				$scope.saveInExcel.push({
					'Row' : item.RNo.split(',')[0],
					'Column' : item.RNo.split(',')[1],
					'IssueStatus' : item.IssueStatus
				});
			});

			var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
			var opts = [ {
				sheetid : sheetLabel,
				header : true
			} ];
			alasql(
					'SELECT INTO XLSX("Import Batch Delete Excel Error.xlsx",?) FROM ?',
					[ opts, [ $scope.saveInExcel ] ]);
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};

	// Employee Cab Request Model Controller
	var importEmployeeRequestCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog, $timeout) {
		$scope.alertMessage;
		$scope.alertHint;
		$scope.branchCode = branchCode;
		//$scope.importCabRequestShow = false;

		
		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importEmployeeRequestErrorModal.jsp',
										controller : 'importEmployeeRequestErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});

							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	// Nodal Request Model Controller
	var importNodalRequestCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog, $timeout) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importNodalRequestErrorModal.jsp',
										controller : 'importNodalRequestErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});

							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	// Vendor Master Data Model Controller
	var importVendorMasterDataCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importVendorDataErrorModal.jsp',
										controller : 'importVendorDataErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});

							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	// Driver Master Data Model Controller
	var importDriverMasterDataCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importDriverDataErrorModal.jsp',
										controller : 'importDriverDataErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});
							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	// Vehicle Master Data Model Controller
	var importVehicleMasterDataCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importVehicleDataErrorModal.jsp',
										controller : 'importVehicleDataErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});

							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	};// End of MODAL CTRL

	// Area Master Data Model Controller
	var importAreaMasterDataCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importAreaDataErrorModal.jsp',
										controller : 'importAreaDataErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});
							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};// End of MODAL CTRL

	// Escort Master Data Model Controller
	var importEscortMasterDataCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;

							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importEscortDataErrorModal.jsp',
										controller : 'importEscortDataErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});
							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};// End of MODAL CTRL

	var uploadRouteCtrl = function($scope, $modal, $modalInstance, $state,
			$http, $timeout, ngDialog) {

		// SUBMIT BUTTON FUNCTION
		$scope.uploadFile = function(obj) {
			if(obj.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
				|| obj.type == 'application/vnd.ms-excel'){
				$scope.excelFormatValue = true;
			}
			
			if($scope.excelFormatValue == undefined){
				ngDialog.open({
					template : 'Kindly upload valid format(.xlsx)',
					plain : true
				});
				return false;
			}

			var fd = new FormData();

			// cfpLoadingBar.start();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							if(data.status == 500){
								ngDialog.open({
										template : 'Unsupported File Format. Kindly upload valid format.',
										plain : true
									});
									return false;
							}
							if (branchCode == 'SRSTPT') // Sears - Bangalore
							{
								var response = data.responseJSON;
								if(response.length == 0)
								{
									// cfpLoadingBar.complete();
									ngDialog.open({
										template : 'Already Routes are created for these employees.',
										plain : true
									});
									return false;
								}
								else if(response[0].Status == 'success')
								{
									// cfpLoadingBar.complete();
									ngDialog.open({
										template : 'Route uploaded successfully.',
										plain : true
									});
								}
								else
								{
									// cfpLoadingBar.complete();
									var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importRouteDataErrorModal.jsp',
										controller : 'importRouteDataErrorCtrl',
										resolve : {
											result : function() {
												return response;
													}
												}
											});
									$timeout(function() {
										$modalInstance.dismiss('cancel');
										ngDialog.close();
									}, 5000);
								}
								
							} else // All other branchs
							{
								// cfpLoadingBar.complete();
								ngDialog.open({
									template : 'Already Routes are created for these employees.',
									plain : true
								});
								// $scope.showalertMessageModal('Route uploaded
								// successfully.', '');
								$timeout(function() {
									$modalInstance.dismiss('cancel');
									ngDialog.close();
								}, 5000);
							}

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		}
	};

	var exportRouteCtrl = function($scope, $modal, $modalInstance, $state,
			$http, $timeout, ngDialog) {
		$scope.hstep = 1;
		$scope.mstep = 1;
		$scope.download = {};
		$scope.shiftsTime = [];
		$scope.ismeridian = false;
		$scope.reportList = [];
		$scope.download.date = new Date();
		$scope.facilityData = [];
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = array;

		// $scope.setMinDate = new Date();
		$scope.tripTypes = [ {
			'value' : 'PICKUP',
			'text' : 'PICKUP'
		}, {
			'value' : 'DROP',
			'text' : 'DROP'
		} ];

		// Convert the dates in DD-MM-YYYY format
		var convertDateUTC = function(date) {
			var convert_date = new Date(date);
			var currentMonth = date.getMonth() + 1;
			var currentDate = date.getDate();
			if (currentDate < 10) {
				currentDate = '0' + currentDate;
			}
			if (currentMonth < 10) {
				currentMonth = '0' + currentMonth;
			}
			return currentDate + '-' + currentMonth + '-'
					+ convert_date.getFullYear();
		};

		// Initialize TimePicker to 00:00
		var timePickerInitialize = function() {
			var d = new Date();
			d.setHours(00);
			d.setMinutes(0);
			$scope.assignCab.createNewAdHocTime = d;
		};

		$scope.format = 'dd-MM-yyyy';
		$scope.dateOptions = {
			formatYear : 'yy',
			startingDay : 1,
			showWeeks : false,
		};

		$scope.openDownloadDateCal = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$timeout(function() {
				$scope.datePicker = {
					'openeddownloadDate' : true
				};
			})
		};

		// Initialize TimePicker to 00:00
		var initialize = function() {
			var d = new Date();
			d.setHours(00);
			d.setMinutes(0);
			$scope.download.createNewAdHocTime = d;
			$scope.download.tripType = {
				'value' : 'PICKUP',
				'text' : 'PICKUP'
			};

			$scope.setTripType($scope.download.tripType);
			$scope.download.shiftTime = $scope.shiftsTime[0];
			$scope.shiftTime = 'preDefineShiftTime';
			$scope.download.date = new Date();
		}

		$scope.selectShiftTimeRadio = function(shiftTime) {
			$scope.typeOfShiftTimeSelected = shiftTime;
			$('.btn-link').addClass('noPointer');
		};

		$scope.selectShiftTimeRadio2 = function(shiftTime) {
			$scope.typeOfShiftTimeSelected = shiftTime;
			$('.btn-link').removeClass('noPointer');
		};

		$scope.setTripType = function(tripType) {
			if (angular.isObject(tripType)) {
				var data = {
					efmFmUserMaster : {
						eFmFmClientBranchPO : {
							branchId : branchId
						}
					},
					eFmFmEmployeeRequestMaster : {
						efmFmUserMaster : {
							userId : profileId
						}
					},
					tripType : tripType.value,
					userId : profileId,
					combinedFacility:combinedFacility
				};
				$http.post('services/trip/tripshiftime/', data).success(
						function(data, status, headers, config) {
							if (data.status != "invalidRequest") {
							$scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });
							$scope.download.shiftTime = $scope.shiftsTime[0];
							}

						}).error(function(data, status, headers, config) {
				});
			} else {
				$scope.shiftsTime = '';
			}
			$scope.download.shiftTime = $scope.shiftsTime[0];
		};

		initialize();

		$scope.ExportRoute = function(fileInfo,combinedFacilityId) {

			if(combinedFacilityId == undefined || combinedFacilityId.length == 0){
				combinedFacilityId = combinedFacility;
			}else{
				combinedFacilityId = String(combinedFacilityId);
			}

			if ($scope.shiftTime == 'preDefineShiftTime') {
				$scope.timeSelected = fileInfo.shiftTime.shiftTime
			} else {
				var fullDate = new Date(fileInfo.createNewAdHocTime);
				var time = fullDate.getHours() + ':' + fullDate.getMinutes()
						+ ':00';
				$scope.timeSelected = time;
			}

			var dataObj = {
				branchId : branchId,
				executionDate : convertDateUTC(fileInfo.date),
				tripType : fileInfo.tripType.value,
				time : $scope.timeSelected,
				combinedFacility:combinedFacilityId
			};

			$http({
				url : 'services/xlEmployeeExport/exportRoute/',
				method : "POST",
				data : dataObj,
				headers : {
					'Content-type' : 'application/json'
				},
				responseType : 'arraybuffer'
			}).success(function(data, status, headers, config) {
				var blob = new Blob([ data ], {});
				saveAs(blob, 'Employee Wise Report' + '.xlsx');
			}).error(function(data, status, headers, config) {
				alert("Upload Failed")
			});

		}

		/*
		 * $scope.ExportRoute = function(fileInfo) {
		 * 
		 * if ($scope.shiftTime == 'preDefineShiftTime') { $scope.timeSelected =
		 * fileInfo.shiftTime.shiftTime } else { var fullDate = new
		 * Date(fileInfo.createNewAdHocTime); var time = fullDate.getHours() +
		 * ':' + fullDate.getMinutes() + ':00'; $scope.timeSelected = time; }
		 * 
		 * var dataObj = { branchId : branchId, executionDate :
		 * convertDateUTC(fileInfo.date), tripType : fileInfo.tripType.value,
		 * time : $scope.timeSelected }; $http
		 * .post('services/xlEmployeeExport/exportRoute/', dataObj) .success(
		 * function(data, status, headers, config) { if (data == "SUCCEES") {
		 * ngDialog .open({ template : 'RFiles have been exported
		 * successfully.', plain : true });
		 * //$scope.showalertMessageModal("Files have been exported
		 * successfully.", ""); $timeout(function() { $modalInstance.close();
		 * ngDialog.close(); }, 5000); } else { ngDialog .open({ template : 'No
		 * file found to export. Please change your selction and try again.',
		 * plain : true });
		 * 
		 * //$scope.showalertMessageModal("No file found to export. Please
		 * change your selction and try again", ""); }
		 * 
		 * }).error(function(data, status, headers, config) { }); };
		 */
		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};

	var importRouteDataErrorCtrl = function($scope, $modalInstance, result,
			ngDialog, $timeout) {
		$scope.result = result;
		$scope.resultShow = $scope.result.length;
		if ($scope.resultShow > 0) {
			$scope.saveInExcel = function() {
				$scope.saveInExcel = [];

				angular.forEach($scope.result, function(item) {
					$scope.saveInExcel.push({
						'Row' : item.RNo.split(',')[0],
						'Column' : item.RNo.split(',')[1],
						'IssueStatus' : item.IssueStatus
					});
				});

				var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
				var opts = [ {
					sheetid : sheetLabel,
					header : true
				} ];
				alasql('SELECT INTO XLSX("Import Route Error.xlsx",?) FROM ?',
						[ opts, [ $scope.saveInExcel ] ]);

			};

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		} else {
			ngDialog.open({
				template : 'Routes successfully uploaded',
				plain : true
			});
			$timeout(function() {
				$modalInstance.dismiss('cancel');
				ngDialog.close();
			}, 5000);

			// CLOSE BUTTON FUNCTION
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
		}
	};

	var rememberShiftRouteCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {
		$scope.hstep = 1;
		$scope.mstep = 1;
		$scope.download = {};
		$scope.shiftsTime = [];
		$scope.ismeridian = false;
		$scope.reportList = [];
		$scope.download.date = new Date();
		$scope.facilityData = [];
        $scope.facilityDetails = userFacilities;
        var array = JSON.parse("[" + combinedFacility + "]");
        $scope.facilityData = array;

		// $scope.setMinDate = new Date();
		$scope.tripTypes = [ {
			'value' : 'PICKUP',
			'text' : 'PICKUP'
		}, {
			'value' : 'DROP',
			'text' : 'DROP'
		} ];

		// Convert the dates in DD-MM-YYYY format
		var convertDateUTC = function(date) {
			var convert_date = new Date(date);
			var currentMonth = date.getMonth() + 1;
			var currentDate = date.getDate();
			if (currentDate < 10) {
				currentDate = '0' + currentDate;
			}
			if (currentMonth < 10) {
				currentMonth = '0' + currentMonth;
			}
			return currentDate + '-' + currentMonth + '-'
					+ convert_date.getFullYear();
		};

		// Initialize TimePicker to 00:00
		var timePickerInitialize = function() {
			var d = new Date();
			d.setHours(00);
			d.setMinutes(0);
			$scope.assignCab.createNewAdHocTime = d;
		};

		$scope.format = 'dd-MM-yyyy';
		$scope.dateOptions = {
			formatYear : 'yy',
			startingDay : 1,
			showWeeks : false,
		};

		$scope.openDownloadDateCal = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$timeout(function() {
				$scope.datePicker = {
					'openeddownloadDate' : true
				};
			})
		};

		// Initialize TimePicker to 00:00
		var initialize = function() {
			var d = new Date();
			d.setHours(00);
			d.setMinutes(0);
			$scope.download.createNewAdHocTime = d;
			$scope.download.tripType = {
				'value' : 'PICKUP',
				'text' : 'PICKUP'
			};

			$scope.setTripType($scope.download.tripType);
			$scope.download.shiftTime = $scope.shiftsTime[0];
			$scope.shiftTime = 'preDefineShiftTime';
			$scope.download.date = new Date();
		}

		$scope.selectShiftTimeRadio = function(shiftTime) {
			$scope.typeOfShiftTimeSelected = shiftTime;
			$('.btn-link').addClass('noPointer');
		};

		$scope.selectShiftTimeRadio2 = function(shiftTime) {
			$scope.typeOfShiftTimeSelected = shiftTime;
			$('.btn-link').removeClass('noPointer');
		};

		$scope.setTripType = function(tripType) {
			if (angular.isObject(tripType)) {
				var data = {
					efmFmUserMaster : {
						eFmFmClientBranchPO : {
							branchId : branchId
						}
					},
					eFmFmEmployeeRequestMaster : {
						efmFmUserMaster : {
							userId : profileId
						}
					},
					tripType : tripType.value,
					userId : profileId,
					combinedFacility:combinedFacility
				};
				$http.post('services/trip/tripshiftime/', data).success(
						function(data, status, headers, config) {
							if (data.status != "invalidRequest") {
							$scope.shiftsTime = _.uniq(data.shift, function(p){ return p.shiftTime; });
							$scope.download.shiftTime = $scope.shiftsTime[0];
							}

						}).error(function(data, status, headers, config) {
				});
			} else {
				$scope.shiftsTime = '';
			}
			$scope.download.shiftTime = $scope.shiftsTime[0];
		};

		initialize();

		$scope.rememberShiftRoute = function(fileInfo,combinedFacilityId) {

			if(combinedFacilityId == undefined || combinedFacilityId.length == 0){
				combinedFacilityId = combinedFacility;
			}else{
				combinedFacilityId = String(combinedFacilityId);
			}
		
			if ($scope.shiftTime == 'preDefineShiftTime') {
				$scope.timeSelected = fileInfo.shiftTime.shiftTime
			} else {
				var fullDate = new Date(fileInfo.createNewAdHocTime);
				var time = fullDate.getHours() + ':' + fullDate.getMinutes()
						+ ':00';
				$scope.timeSelected = time;
			}

			var dataObj = {
				branchId : branchId,
				executionDate : convertDateUTC(fileInfo.date),
				tripType : fileInfo.tripType.value,
				time : $scope.timeSelected,
				combinedFacility:combinedFacilityId
			};


			$http({
				url : 'services/xlEmployeeExport/exportRoute/',
				method : "POST",
				data : dataObj,
				headers : {
					'Content-type' : 'application/json'
				},
				responseType : 'arraybuffer'
			}).success(function(data, status, headers, config) {
				var blob = new Blob([ data ], {});
				saveAs(blob, 'Employee Wise Report' + '.xlsx');
			}).error(function(data, status, headers, config) {
				alert("Upload Failed")
			});

		}

		/*
		 * $scope.ExportRoute = function(fileInfo) {
		 * 
		 * if ($scope.shiftTime == 'preDefineShiftTime') { $scope.timeSelected =
		 * fileInfo.shiftTime.shiftTime } else { var fullDate = new
		 * Date(fileInfo.createNewAdHocTime); var time = fullDate.getHours() +
		 * ':' + fullDate.getMinutes() + ':00'; $scope.timeSelected = time; }
		 * 
		 * var dataObj = { branchId : branchId, executionDate :
		 * convertDateUTC(fileInfo.date), tripType : fileInfo.tripType.value,
		 * time : $scope.timeSelected }; $http
		 * .post('services/xlEmployeeExport/exportRoute/', dataObj) .success(
		 * function(data, status, headers, config) { if (data == "SUCCEES") {
		 * ngDialog .open({ template : 'RFiles have been exported
		 * successfully.', plain : true });
		 * //$scope.showalertMessageModal("Files have been exported
		 * successfully.", ""); $timeout(function() { $modalInstance.close();
		 * ngDialog.close(); }, 5000); } else { ngDialog .open({ template : 'No
		 * file found to export. Please change your selction and try again.',
		 * plain : true });
		 * 
		 * //$scope.showalertMessageModal("No file found to export. Please
		 * change your selction and try again", ""); }
		 * 
		 * }).error(function(data, status, headers, config) { }); };
		 */
		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};

	var downloadRouteCtrl = function($scope, $modal, $modalInstance, $state,
			$http, $timeout, ngDialog) {
		$scope.getFileList = function() {
			var data = {
				branchId : branchId,
				userId : profileId,
				combinedFacility:combinedFacility
			// eFmFmEmployeeRequestMaster:{efmFmUserMaster:{userId:profileId}}

			};
			$http.post('services/xlEmployeeExport/listOfFileNames/', data)
					.success(function(data, status, headers, config) {
						if (data.status != "invalidRequest") {
						$scope.reportList = data;
						}
					}).error(function(data, status, headers, config) {
					});
		}

		// populating the dropdown
		$scope.getFileList();

		$scope.downloadSelectedFile = function(fileSelected) {
			var fileUrl = "downloadRoutes.do?fileName="
					+ fileSelected.reportName;
			var atag = document.getElementById('reportName');
			$timeout(function() {
				atag.click();
			}, 500);
			// $timeout(function() {$modalInstance.close()}, 5000);

		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};

	var importGuestRequestMasterDataCtrl = function($scope, $modal,
			$modalInstance, $state, $http, $timeout, ngDialog) {
		$scope.alertMessage;
		$scope.alertHint;

		// SUBMIT BUTTON FUNCTION
		$scope.importEmpFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function(data) {
							var response = data.responseJSON;
							var modalInstance = $modal
									.open({
										templateUrl : 'partials/modals/importGuestRequestErrorModal.jsp',
										controller : 'importGuestRequestErrorCtrl',
										resolve : {
											result : function() {
												return response;
											}
										}
									});
							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);

						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

		// CLOSE BUTTON FUNCTION
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

	};

	var importBatchDeleteExcelCtrl = function($scope, $modal, $modalInstance,
			$state, $http, $timeout, ngDialog) {// SUBMIT BUTTON FUNCTION
		$scope.deleteBatchFile = function() {
			var fd = new FormData();
			fd.append("filename", $("#filenameforactivity")[0].files[0]);
			var post_url = $("#addinstgroup").attr("action");
			var postdata = $("#addinstgroup").serialize();
			var url = post_url + "?" + postdata + "&profileId=" + profileId
					+ "&branchId=" + branchId+ "&combinedFacility=" + combinedFacility;
			$
					.ajax({
						url : url,
						type : 'POST',
						cache : false,
						data : fd,
						processData : false,
						contentType : false,
						success : function(data, textStatus, jqXHR) {
						},
						complete : function() {
							ngDialog
									.open({
										template : 'The Batch Delete Excel File has been imported successfully.',
										plain : true
									});
							// $scope.showalertMessageModal('Route uploaded
							// successfully.', '');
							$timeout(function() {
								$modalInstance.dismiss('cancel');
								ngDialog.close();
							}, 5000);
						}

					});
			$timeout(function() {
				$modalInstance.close(fd);
				ngDialog.close();
			}, 3000);
			// write the code of import file over here
		};

	};

	angular.module('efmfmApp').controller('importDataCtrl', importDataCtrl);
	angular.module('efmfmApp').controller('importEmployeeCtrl',
			importEmployeeCtrl);
	angular.module('efmfmApp').controller('importEmployeeRequestCtrl',
			importEmployeeRequestCtrl);
	angular.module('efmfmApp').controller('importVendorMasterDataCtrl',
			importVendorMasterDataCtrl);
	angular.module('efmfmApp').controller('importDriverMasterDataCtrl',
			importDriverMasterDataCtrl);
	angular.module('efmfmApp').controller('importVehicleMasterDataCtrl',
			importVehicleMasterDataCtrl);
	angular.module('efmfmApp').controller('importAreaMasterDataCtrl',
			importAreaMasterDataCtrl);
	angular.module('efmfmApp').controller('importEscortMasterDataCtrl',
			importEscortMasterDataCtrl);
	angular.module('efmfmApp').controller('importNodalRequestCtrl',
			importNodalRequestCtrl);
	angular.module('efmfmApp').controller('uploadRouteCtrl', uploadRouteCtrl);
	angular.module('efmfmApp').controller('exportRouteCtrl', exportRouteCtrl);
	angular.module('efmfmApp').controller('downloadRouteCtrl',
			downloadRouteCtrl);
	angular.module('efmfmApp').controller('importGuestRequestMasterDataCtrl',
			importGuestRequestMasterDataCtrl);
	angular.module('efmfmApp').controller('importBatchDeleteExcelCtrl',
			importBatchDeleteExcelCtrl);
	angular.module('efmfmApp').controller('importEmployeeErrorCtrl',
			importEmployeeErrorCtrl);
	angular.module('efmfmApp').controller('importEmployeeRequestErrorCtrl',
			importEmployeeRequestErrorCtrl);
	angular.module('efmfmApp').controller('importVendorDataErrorCtrl',
			importVendorDataErrorCtrl);
	angular.module('efmfmApp').controller('importDriverDataErrorCtrl',
			importDriverDataErrorCtrl);
	angular.module('efmfmApp').controller('importVehicleDataErrorCtrl',
			importVehicleDataErrorCtrl);
	angular.module('efmfmApp').controller('importEscortDataErrorCtrl',
			importEscortDataErrorCtrl);
	angular.module('efmfmApp').controller('importAreaDataErrorCtrl',
			importAreaDataErrorCtrl);
	angular.module('efmfmApp').controller('importGuestRequestErrorCtrl',
			importGuestRequestErrorCtrl);
	angular.module('efmfmApp').controller('importNodalRequestErrorCtrl',
			importNodalRequestErrorCtrl);
	angular.module('efmfmApp').controller('importBatchDeleteExcelErrorCtrl',
			importBatchDeleteExcelErrorCtrl);
	angular.module('efmfmApp').controller('uploadExcelDriverCtrl',
			uploadExcelDriverCtrl);
	angular.module('efmfmApp').controller('uploadDriverErrorCtrl',
			uploadDriverErrorCtrl);
	angular.module('efmfmApp').controller('rememberShiftRouteCtrl',
			rememberShiftRouteCtrl);
	angular.module('efmfmApp').controller('importRouteDataErrorCtrl',
			importRouteDataErrorCtrl);

}());