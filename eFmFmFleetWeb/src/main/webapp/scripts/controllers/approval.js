/*
@date           04/01/2015
@Author         Saima Aziz
@Description    
@controllers    approvalCtrl
@template       partials/home.approval.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes 
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {
    var approvalCtrl = function($scope, $rootScope, $state, $http, $location, $anchorScroll, $modal, $confirm, ngDialog) {

        if (!$scope.IsApprovalActive || $scope.IsApprovalActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $scope.isClicked = false;
            $('.approvalMenu').addClass('active');
            $scope.modifedField = false;
            $scope.driverDocuments = [];
            $scope.vehicleDocuments = [];
            $scope.currentDriverTab = 'Pending';
            $scope.currentVehicleTab = 'Pending';
            $scope.currentVendorTab = 'Pending';
            $scope.currentSupervisorTab = 'Pending';
            $scope.currentTripSheetTab = 'Trip Sheet';
            $scope.currentInvoiceSheetTab = 'Invoice';
            localStorage.clear();
            $scope.facilityData = {};
            $scope.facilityDetails = userFacilities;

            var array = JSON.parse("[" + combinedFacility + "]");
            $scope.facilityData.listOfFacility = array;
            $scope.$on('$viewContentLoaded', function() {
                $scope.getPendingDriver();
                $scope.getPendingVehicle();
                $scope.getPendingVendor();
                $scope.getPendingInvoice();
                $scope.getTripSheetPendingReport();
                $scope.getPendingSupervisor();

            });

            $scope.getTabInfo = function(value) {
            localStorage.setItem("approvalTab", value);
            }

            $scope.setFacilityDetails = function(value) {
                 combinedFacilityId = value; 
                 var tabInfo = localStorage.getItem("approvalTab");
                 switch (tabInfo) {
                    case 'pendingDriver':
                        $scope.getPendingDriver(value);
                        break;
                    case 'registeredDriver':
                        $scope.getRegisteredDriver(value);
                        break;
                    case 'unregisteredDriver':
                        $scope.getUnRegisteredDriver(value);
                        break;
                    case 'pendingVehicle':
                        $scope.getPendingVehicle(value);
                        break;
                    case 'registeredVehicle':
                        $scope.getRegisteredVehicle(value);
                        break;
                    case 'unregisteredVehicle':
                        $scope.getUnRegisteredVehicle(value);
                        break;
                    case 'pendingVendor':
                        $scope.getPendingVendor(value);
                        break;
                    case 'registeredVendor':
                        $scope.getRegisteredVendor(value);
                        break;
                    case 'unregisteredVendor':
                        $scope.getUnRegisteredVendor(value);
                        break;
                    case 'pendingSupervisor':
                        $scope.getPendingSupervisor(value);
                        break;
                    case 'registeredSupervisor':
                        $scope.getRegisteredSupervisor(value);
                        break;
                    case 'unregisteredSupervisor':
                        $scope.getUnRegisteredSupervisor(value);
                        break;
                    case 'tripSheetPending':
                        $scope.getTripSheetPendingReport(value);
                        break;
                    case 'invoicePending':
                        $scope.getPendingInvoice(value);
                        break;
                    default:
                        $scope.getPendingDriver(value);
                        $scope.getPendingVehicle(value);
                        $scope.getPendingVendor(value);
                        break;
                }

            }

            $scope.scrollTo = function(id) {
                $location.hash(id);
                $anchorScroll();
            };

            var findImgSrc = function(extension) {
                var image;
                switch (extension) {
                    case 'pdf':
                        image = 'images/adobeAcrobat.png';
                        break;
                    case 'docx':
                        image = 'images/msWord.png';
                        break;
                    case 'xls':
                        image = 'images/msExcel.png';
                        break;
                    case 'jpg':
                        image = 'images/jpgIcon.png';
                        break;
                    default:
                        image = 'images/docImg.png';
                }
                return image;
            };

            //*************DRIVER BUTTON FUNCTIONS********************
            $scope.viewDriver = function(post, index) {
                angular.forEach($scope.driversPendingData, function(item) {
                    item.isClicked = false;
                });
                if (!post.isClicked) {
                    post.isClicked = true;
                    var dataObj = {
                        efmFmVendorMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        driverId: post.driverId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/approval/driverDetail/', dataObj).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.driverDetail = data;
                            angular.forEach($scope.driverDetail[0].documents, function(item) {
                                if (item.location != null) {
                                    extension = item.location.split('.').pop();
                                    item.imgSrc = findImgSrc(extension);
                                } else item.imgSrc = 'images/docImg.png';
                            });
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }
            };

            $scope.approveDriver = function(post, index) {
                post.isClicked = false;
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    driverId: post.driverId,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $http.post('services/approval/approvedriver/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data == "success") {
                            ngDialog.open({
                                template: 'Driver has been approved',
                                plain: true
                            });
                            post.isClicked = false;
                            $scope.driversPendingData.splice(index, 1);
                        } else {
                            ngDialog.open({
                                template: 'Please first approve the vendor',
                                plain: true
                            });

                        }
                    }

                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.rejectDriver = function(post, index) {
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    driverId: post.driverId,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $confirm({
                        text: 'Are you sure you want to reject this driver?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/removedriver/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                post.isClicked = false;
                                $scope.driversPendingData.splice(index, 1);
                                ngDialog.open({
                                    template: 'Driver has been rejected',
                                    plain: true
                                });
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.closeDriver = function(post) {
                post.isClicked = false;
            };



            $scope.removeDriver = function(post, index) {
                $confirm({
                        text: 'Are you sure you want to remove this registered driver?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/approvalRemarks.jsp',
                            controller: 'checkOutRemarksApprovalCtrl',
                            size: 'sm',
                            resolve: {
                                driver: function() {
                                    return vehicle;
                                }
                            }
                        });

                        modalInstance.result.then(function(result) {

                            //Convert the dates in DD-MM-YYYY format
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
                                return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
                            };

                            //Function to check the Date Validity

                            $scope.checkDateRangeValidity = function(joinDate, relieveDate) {
                                if (joinDate > relieveDate) {
                                    $scope.dateRangeError = true;
                                    $timeout(function() {
                                        $scope.dateRangeError = false;
                                    }, 3000);
                                    return false;
                                } else return true;
                            };

                            var data = {
                                efmFmVendorMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                driverId: post.driverId,
                                remarks: result.remarks,
                                relievingDate: convertDateUTC(result.relieveDate),
                                userId: profileId,
                                combinedFacility: combinedFacility

                            };
                            $http.post('services/approval/rejectdriver/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $scope.driversRegisterData.splice(index, 1);
                                    ngDialog.open({
                                        template: 'Driver has been removed',
                                        plain: true
                                    });

                                }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });
                        });
                    });
            };


            $scope.noEntryDriver = function(driver, index) {
                $confirm({
                        text: 'Are you sure you want to no entry this registered driver?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/noEntryDriverRemarks.jsp',
                            controller: 'noEntryRemarksApprovalCtrl',
                            resolve: {
                                dataNoEntry: function() {
                                    return driver;
                                }
                            }
                        });

                        modalInstance.result.then(function(result) {

                            //Convert the dates in DD-MM-YYYY format
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
                                return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
                            };

                            //Function to check the Date Validity


                            var data = {
                                efmFmVendorMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                driverId: driver.driverId,
                                remarks: result.remarks,
                                startDate: convertDateUTC(result.startDate),
                                endDate: convertDateUTC(result.endDate),
                                userId: profileId,
                                combinedFacility: combinedFacility

                            };
                            $http.post('services/approval/rejectdriver/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $scope.driversRegisterData.splice(index, 1);
                                    ngDialog.open({
                                        template: 'No entry successfully done for this registered driver',
                                        plain: true
                                    });

                                }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });
                        });
                    });

            };

            $scope.noEntryVehicle = function(vehicle, index) {
                $confirm({
                        text: 'Are you sure you want to no entry this registered vehicle?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/noEntryDriverRemarks.jsp',
                            controller: 'noEntryRemarksApprovalCtrl',
                            resolve: {
                                dataNoEntry: function() {
                                    return vehicle;
                                }
                            }
                        });

                        modalInstance.result.then(function(result) {

                            //Convert the dates in DD-MM-YYYY format
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
                                return currentDate + '-' + currentMonth + '-' + convert_date.getFullYear();
                            };

                            //Function to check the Date Validity

                            $scope.checkDateRangeValidity = function(joinDate, relieveDate) {
                                if (joinDate > relieveDate) {
                                    $scope.dateRangeError = true;
                                    $timeout(function() {
                                        $scope.dateRangeError = false;
                                    }, 3000);
                                    return false;
                                } else return true;
                            };

                            var data = {
                                efmFmVendorMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                driverId: vehicle.driverId,
                                remarks: result.remarks,
                                startDate: convertDateUTC(result.startDate),
                                endDate: convertDateUTC(result.endDate),
                                userId: profileId,
                                combinedFacility: combinedFacility

                            };
                            $http.post('services/approval/rejectdriver/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $scope.vehiclesRegisterData.splice(index, 1);
                                    ngDialog.open({
                                        template: 'Driver has been removed',
                                        plain: true
                                    });

                                }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });
                        });
                    });



            };



            $scope.addagainDriver = function(post, index) {
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    driverId: post.driverId,
                    userId: profileId,
                    combinedFacility: combinedFacility

                };
                $confirm({
                        text: 'Are you sure you want to add this driver again?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/addagaindriver/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.driversUnregisterData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };



            $scope.unregisteredDriverExcelDownload = function(combinedFacilityId) {

                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                var dataObj = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http({
                    url: 'services/approval/inactivedriverDownload/',
                    method: "POST",
                    data: dataObj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function(data, status, headers, config) {
                    var blob = new Blob([data], {});
                    saveAs(blob, 'Unregistered Driver' + '.xlsx');
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });
            }


            //*************VEHICLE BUTTON FUNCTIONS********************
            $scope.viewVehicle = function(post) {
                angular.forEach($scope.vehiclesPendingData, function(item) {
                    item.isClicked = false;
                });
                if (!post.isClicked) {
                    post.isClicked = true;
                    $('.vehicle' + post.vehicleId).show('slow');
                    var data = {
                        efmFmVendorMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        vehicleId: post.vehicleId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/approval/vehicleDetail/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.vehicleDetail = data;
                            angular.forEach($scope.vehicleDetail[0].documents, function(item) {
                                if (item.location != null) {
                                    extension = item.location.split('.').pop();
                                    item.imgSrc = findImgSrc(extension);
                                } else item.imgSrc = 'images/docImg.png';
                            });
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else post.isClicked = false;

            };

            $scope.approveVehicle = function(post, index) {
                post.isClicked = false;
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    vehicleId: post.vehicleId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/approval/approvevehicle/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data == "success") {
                            ngDialog.open({
                                template: 'Vehicle has been approved',
                                plain: true
                            });
                            $scope.vehiclesPendingData.splice(index, 1);
                        } else {
                            ngDialog.open({
                                template: 'Please first approve the vendor',
                                plain: true
                            });

                        }
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.rejectVehicle = function(post, index) {
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    vehicleId: post.vehicleId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to reject this vehicle?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/removevehicle/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vehiclesPendingData.splice(index, 1);
                                ngDialog.open({
                                    template: 'Vehicle has been rejected',
                                    plain: true
                                });

                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error});
                        });
                    });
            }

            $scope.closeVehicle = function(post) {
                post.isClicked = false;
            };

            $scope.removeVehicle = function(post, index) {
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    vehicleId: post.vehicleId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to remove this vehicle?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/rejectvehicle/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                                $scope.vehiclesRegisterData.splice(index, 1);
                                ngDialog.open({
                                    template: 'Vehicle has been removed',
                                    plain: true
                                });

                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.addagainVehicle = function(post, index) {
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    vehicleId: post.vehicleId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to add this vehicle again?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/addagainvehicle/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vehiclesUnregisterData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            //*************VENDOR BUTTON FUNCTIONS********************
            $scope.viewVendor = function(post) {
                angular.forEach($scope.vendorPendingData, function(item) {
                    item.isClicked = false;
                });
                if (!post.isClicked) {
                    post.isClicked = true;
                    $('.vendor' + post.vendorId).show('slow');
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        vendorId: post.vendorId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/approval/vendorDetail/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.vendorDetail = data;
                        }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else post.isClicked = false;
            };

            $scope.approveVendor = function(post, index) {
                $('.vendor' + post.vendorId).slideUp('slow');
                var data = {
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/approval/approvevendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {

                        ngDialog.open({
                            template: 'Vendor has been approved',
                            plain: true
                        });

                        post.isClicked = false;
                        $scope.vendorPendingData.splice(index, 1);
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            };

            $scope.rejectVendor = function(post, index) {
                var data = {

                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to reject this vendor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        $http.post('services/approval/removevendor/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                                ngDialog.open({
                                    template: 'Vendor has been rejected',
                                    plain: true
                                });


                                post.isClicked = false;
                                $scope.vendorPendingData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.closeVendor = function(post) {
                post.isClicked = false;
            };

            $scope.removeVendor = function(post, index) {
                var data = {
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to remove this vendor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/rejectvendor/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vendorRegisterData.splice(index, 1);
                                ngDialog.open({
                                    template: 'Vendor has been removed',
                                    plain: true
                                });

                            }

                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.addagainVendor = function(post, index) {
                var data = {
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };

                $confirm({
                        text: 'Are you sure you want to add again this vendor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/addagainvendor/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vendorUnregisterData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };


            // ******DRIVER APPROVAL FUNCTION - called when the user click any tab*************** 
            $scope.getPendingDriver = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentDriverTab = 'Pending';
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/approval/unapproveddriver/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.driversPendingData = data;
                        angular.forEach($scope.driversPendingData, function(item) {
                            item.isClicked = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getRegisteredDriver = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentDriverTab = 'Registered';
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/approveddriver/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.driversRegisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getUnRegisteredDriver = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentDriverTab = 'UnRegistered';
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/inactivedriver/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.driversUnregisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            // ******VEHICLE APPROVAL FUNCTION - called when the user click any tab*************** 

            $scope.getPendingVehicle = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentVehicleTab = 'Pending';
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/unapprovedvehicle/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vehiclesPendingData = data;
                        angular.forEach($scope.vehiclesPendingData, function(item) {
                            item.isClicked = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getRegisteredVehicle = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentVehicleTab = 'Registered';
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/approvedvehicle/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vehiclesRegisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getUnRegisteredVehicle = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentVehicleTab = 'UnRegistered';
                var data = {
                    efmFmVendorMaster: {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/inactivevehicle/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vehiclesUnregisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            // ******VENDOR APPROVAL FUNCTION - called when the user click any tab***************      
            $scope.getPendingVendor = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentVendorTab = 'Pending';
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/unapprovedvendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vendorPendingData = data;
                        angular.forEach($scope.vendorPendingData, function(item) {
                            item.isClicked = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getRegisteredVendor = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentVendorTab = 'Registered';
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/approvedvendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vendorRegisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getUnRegisteredVendor = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentVendorTab = 'UnRegistered';
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/inactivevendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.vendorUnregisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };


            // ******SUPERVISOR APPROVAL FUNCTION - called when the user click any tab***************      
            $scope.getPendingSupervisor = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentSupervisorTab = 'Pending';
                var data = {
                    branchId: branchId,
                    isActive: 'P',
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/fieldApp/getAllSupervisorByStatus', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.supervisorsPendingData = data;
                        angular.forEach($scope.supervisorsPendingData, function(item) {
                            item.isClicked = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getRegisteredSupervisor = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentSupervisorTab = 'Registered';
                var data = {
                    branchId: branchId,
                    isActive: 'A',
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/fieldApp/getAllSupervisorByStatus', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.supervisorsRegisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.getUnRegisteredSupervisor = function(combinedFacilityId) {
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                $scope.currentSupervisorTab = 'UnRegistered';
                var data = {
                    branchId: branchId,
                    isActive: 'N',
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/fieldApp/getAllSupervisorByStatus', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.supervisorsUnregisterData = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };


            //*************SUPERVISORS BUTTON FUNCTIONS********************
            $scope.viewSupervisor = function(post) {
                angular.forEach($scope.supervisorsPendingData, function(item) {
                    item.isClicked = false;
                });
                if (!post.isClicked) {
                    post.isClicked = true;
                    $('.vendor' + post.supervisorId).show('slow');
                } else post.isClicked = false;
            };

            $scope.approveSupervisor = function(post, index) {
                $('.supervisor' + post.supervisorId).slideUp('slow');

                var data = {
                    supervisorId: post.supervisorId,
                    isActive: 'A',
                    userId: profileId,
                    branchId: branchId,
                    combinedFacility: combinedFacility
                };

                $http.post('services/fieldApp/supervisorStatusModified', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        ngDialog.open({
                            template: 'Supervisor has been approved',
                            plain: true
                        });

                        post.isClicked = false;
                        $scope.supervisorsPendingData.splice(index, 1);
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            };

            $scope.rejectSupervisor = function(post, index) {
                var data = {
                    supervisorId: post.supervisorId,
                    isActive: 'R',
                    userId: profileId,
                    branchId: branchId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to reject this Supervisor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        $http.post('services/fieldApp/supervisorStatusModified', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                ngDialog.open({
                                    template: 'Supervisor has been rejected',
                                    plain: true
                                });

                                post.isClicked = false;
                                $scope.supervisorsPendingData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.closeSupervisor = function(post) {
                post.isClicked = false;
            };

            $scope.removeSupervisor = function(post, index) {

                $confirm({
                        text: 'Are you sure you want to remove this Supervisor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/supervisorRemarks.jsp',
                            controller: 'supervisorRemarksCtrl',
                            size: 'sm',
                            resolve: {
                                post: function() {
                                    return post;
                                }
                            }
                        });

                        modalInstance.result.then(function(result) {
                            var data = {
                                branchId: branchId,
                                userId: profileId,
                                supervisorId: post.supervisorId,
                                isActive: 'N',
                                remarks: result.remarks,
                                combinedFacility: combinedFacility
                            };

                            $http.post('services/fieldApp/supervisorStatusModified', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $scope.supervisorsRegisterData.splice(index, 1);
                                    ngDialog.open({
                                        template: 'Supervisor has been removed',
                                        plain: true
                                    });
                                }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });

                        });

                    });

            };

            $scope.addagainSupervisor = function(post, index) {
                var data = {
                    supervisorId: post.supervisorId,
                    isActive: 'A',
                    userId: profileId,
                    branchId: branchId,
                    combinedFacility: combinedFacility
                };

                $confirm({
                        text: 'Are you sure you want to add again this Supervisor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/fieldApp/supervisorStatusModified', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                ngDialog.open({
                                    template: 'Supervisor has been added',
                                    plain: true
                                });
                                $scope.supervisorsUnregisterData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };


            /**********************End Supervisors*****************/

            //*************VENDOR BUTTON FUNCTIONS********************
            $scope.viewVendor = function(post) {
                angular.forEach($scope.vendorPendingData, function(item) {
                    item.isClicked = false;
                });
                if (!post.isClicked) {
                    post.isClicked = true;
                    $('.vendor' + post.vendorId).show('slow');
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        vendorId: post.vendorId,
                        userId: profileId,
                        combinedFacility: combinedFacility
                    };
                    $http.post('services/approval/vendorDetail/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                            $scope.vendorDetail = data;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                } else post.isClicked = false;
            };

            $scope.approveVendor = function(post, index) {
                $('.vendor' + post.vendorId).slideUp('slow');
                var data = {
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/approval/approvevendor/', data).
                success(function(data, status, headers, config) {

                    if (data.status != "invalidRequest") {

                        ngDialog.open({
                            template: 'Vendor has been approved',
                            plain: true
                        });


                        post.isClicked = false;
                        $scope.vendorPendingData.splice(index, 1);
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            };

            $scope.rejectVendor = function(post, index) {
                var data = {

                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to reject this vendor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        $http.post('services/approval/removevendor/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                                ngDialog.open({
                                    template: 'Vendor has been rejected',
                                    plain: true
                                });


                                post.isClicked = false;
                                $scope.vendorPendingData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.closeVendor = function(post) {
                post.isClicked = false;
            };

            $scope.removeVendor = function(post, index) {
                var data = {
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };
                $confirm({
                        text: 'Are you sure you want to remove this vendor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/rejectvendor/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vendorRegisterData.splice(index, 1);
                                ngDialog.open({
                                    template: 'Vendor has been removed',
                                    plain: true
                                });
                            }

                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };

            $scope.addagainVendor = function(post, index) {
                var data = {
                    vendorId: post.vendorId,
                    userId: profileId,
                    combinedFacility: combinedFacility
                };

                $confirm({
                        text: 'Are you sure you want to add again this vendor?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        $http.post('services/approval/addagainvendor/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.vendorUnregisterData.splice(index, 1);
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    });
            };


            $scope.refreshDriverApproval = function() {
                if ($scope.currentDriverTab == 'Pending') {
                    $scope.getPendingDriver();
                }
                if ($scope.currentDriverTab == 'Registered') {
                    $scope.getRegisteredDriver();
                }
                if ($scope.currentDriverTab == 'UnRegistered') {
                    $scope.getUnRegisteredDriver();
                }

            };

            $scope.refreshVehicleApproval = function() {
                if ($scope.currentVehicleTab == 'Pending') {
                    $scope.getPendingVehicle();
                }
                if ($scope.currentVehicleTab == 'Registered') {
                    $scope.getRegisteredVehicle();
                }
                if ($scope.currentVehicleTab == 'UnRegistered') {
                    $scope.getUnRegisteredVehicle();
                }
            };

            $scope.refreshVendorApproval = function() {
                if ($scope.currentVendorTab == 'Pending') {
                    $scope.getPendingVendor();
                }
                if ($scope.currentVendorTab == 'Registered') {
                    $scope.getRegisteredVendor();
                }
                if ($scope.currentVendorTab == 'UnRegistered') {
                    $scope.getUnRegisteredVendor();
                }
            };

            $scope.refreshReportApproval = function() {
                if ($scope.currentTripSheetTab == 'Trip Sheet') {
                    $scope.getTripSheetPendingReport();
                }
            };

            // ******REPORT APPROVAL FUNCTION - called when the user click any tab***************           
            $scope.getTripSheetPendingReport = function(combinedFacilityId) {
                $scope.currentTripSheetTab = 'Trip Sheet';
                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };
                $http.post('services/approval/tripSheetForApproval/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        $scope.reportPendingData = data;
                        angular.forEach($scope.reportPendingData, function(item) {
                            item.isEdit = false;
                            item.approved = false;
                            item.originalTravellDist = item.mobileNumber;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });

            };

            $scope.editTravellDistance = function(post) {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/editTravelDistanceModal.jsp',
                    controller: 'editTravelApprovalDistanceCtrl',
                    resolve: {
                        trip: function() {
                            return post;
                        },
                    }
                });
            };



            $scope.saveTravellDistance = function(post) {
                $confirm({
                        text: 'Are you sure you want to make changes?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            assignRouteId: post.assignRouteId,
                            editedTravelledDistance: post.editDistance,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/approval/editDistanceByAdmin/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                post.isEdit = false;
                                ngDialog.open({
                                    template: 'Trip Sheet Pendings updated successfully',
                                    plain: true
                                });
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                            alert("Failed");
                        });

                    });
            };

            $scope.approveTripSheetPendings = function(post, index) {
                $confirm({
                        text: 'Are you sure you want to approve the travelled Distance for this trip?',
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            assignRouteId: post.assignRouteId,
                            userId: profileId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/approval/distanceApproval/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                $scope.reportPendingData.splice(index, 1);
                                if (data.status = 'success') {
                                    ngDialog.open({
                                        template: 'Distance approved successfully and added in to the travelled distance',
                                        plain: true
                                    });
                                }
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });

                    });
            };

            $scope.getPendingInvoice = function(combinedFacilityId) {
                $scope.currentInvoiceSheetTab = 'Invoice';

                if (combinedFacilityId == undefined || combinedFacilityId.length == 0) {
                    combinedFacilityId = combinedFacility;
                } else {
                    combinedFacilityId = String(combinedFacilityId);
                }

                if (combinedFacilityId == "undefined") {
                    combinedFacilityId = combinedFacility;
                }

                var data = {
                    branchId: branchId,
                    status: 'A',
                    statusFlg: 'M',
                    userId: profileId,
                    combinedFacility: combinedFacilityId
                };

                $http.post('services/contract/nonApprovalList/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        if (data.fixedDistanceBased == undefined) {
                            $scope.invoicePendingData = 0;
                        } else {
                           $scope.invoicePendingData = data.fixedDistanceBased;
                        }
                        $scope.invoicePendingDataCopy = angular.copy(data.fixedDistanceBased);

                        var log = [];
                        angular.forEach($scope.invoicePendingData, function(value, key) {

                            if ($scope.invoicePendingData[key].oldtotalKm == $scope.invoicePendingData[key].totalKm) {
                                $scope.modifedField = true;
                            } else {
                                $scope.modifedField = false;

                            }
                        });
                        angular.forEach($scope.invoicePendingData, function(item) {
                            item.isClicked = false;
                        });
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.editInvoiceApproval = function(invoiceType, invoicePending, contractDetail) {
                if (invoicePending.absentDays == 'NO') {
                    invoicePending.absentDays = 0;
                }

                if (invoiceType == 'byVendor') {
                    invoicePending.editByVendorInvoiceClicked = true;
                }

            };


            $scope.checkTotalKmValue = function(invoicePending, index) {
                var totalKmConversion = invoicePending.newtotalKm.toString();
                var extraKmConversion = invoicePending.newExtraKm.toString();
                var newFuelAmountConversion = invoicePending.newFuelAmount.toString();
                var absentDaysConversion = invoicePending.newPresentDays.toString();
                var invoiceRemarksConversion = invoicePending.newInvoiceRemarks.toString();

                if (invoicePending.newtotalKm.toString().length > 8) {
                    totalKmConversion = totalKmConversion.substring(0, totalKmConversion.length - 1);
                    invoicePending.newtotalKm = Number(totalKmConversion);
                    ngDialog.open({
                        template: 'You cant add more than 8 digit',
                        plain: true
                    });
                } else if (invoicePending.newExtraKm.toString().length > 8) {
                    extraKmConversion = extraKmConversion.substring(0, extraKmConversion.length - 1);
                    invoicePending.newExtraKm = Number(extraKmConversion);
                    ngDialog.open({
                        template: 'You cant add more than 8 digit',
                        plain: true
                    });
                } else if (invoicePending.newFuelAmount.toString().length > 5) {
                    newFuelAmountConversion = newFuelAmountConversion.substring(0, newFuelAmountConversion.length - 1);
                    invoicePending.newFuelAmount = Number(newFuelAmountConversion);
                    ngDialog.open({
                        template: 'You cant add more than 5 digit',
                        plain: true
                    });
                } else if (invoicePending.newPresentDays.toString().length > 8) {
                    absentDaysConversion = absentDaysConversion.substring(0, absentDaysConversion.length - 1);
                    invoicePending.newPresentDays = Number(absentDaysConversion);
                    ngDialog.open({
                        template: 'You cant add more than 8 digit',
                        plain: true
                    });
                } else {
                    return true;
                }
            };


            $scope.cancelChangesInvoiceApproval = function(invoiceType, invoicePending, index, vendorFixedDistanceBasedData) {
                $scope.cloneData = {};
                var oldvalue = $scope.invoicePendingDataCopy[index];
                invoicePending.editByVendorInvoiceClicked = false;
                invoicePending.newtotalKm = oldvalue.newtotalKm;
                invoicePending.newExtraKm = oldvalue.newExtraKm;
                invoicePending.newFuelAmount = oldvalue.newFuelAmount;
                invoicePending.newPresentDays = oldvalue.newPresentDays;
                invoicePending.newInvoiceRemarks = oldvalue.newInvoiceRemarks;

                if (_.isMatch(invoicePending, oldvalue)) {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });
                }
            }

            $scope.filter = function(obj1, obj2) {
                var result = {};
                for (key in obj1) {
                    if (obj2[key] != obj1[key]) result[key] = obj2[key];
                    if (typeof obj2[key] == 'array' && typeof obj1[key] == 'array')
                        result[key] = arguments.callee(obj1[key], obj2[key]);
                    if (typeof obj2[key] == 'object' && typeof obj1[key] == 'object')
                        result[key] = arguments.callee(obj1[key], obj2[key]);
                }
                return result;
            }

            $scope.saveChangesInvoiceApproval = function(invoiceType, invoicePending, index) {
                invoicePending.editByVendorInvoiceClicked = false;
                if (invoiceType == 'byVendor') {

                    var oldvalueCopy = $scope.invoicePendingDataCopy[index];

                    if (invoicePending.absentDays == 0) {
                        invoicePending.absentDays = 'NO';
                    }

                    if (_.isMatch(invoicePending, oldvalueCopy)) {
                        ngDialog.open({
                            template: 'No changes done',
                            plain: true
                        });
                        return false;
                    } else {
                        var compareResult = $scope.filter(oldvalueCopy, invoicePending);
                        $rootScope.compareResultObject = JSON.parse(JSON.stringify(compareResult));
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/invoiceConfirmationApproval.jsp',
                            controller: 'invoiceConfirmationApprovalCtrl',
                            size: 'lg',
                            resolve: {
                                compareResult: function() {
                                    return compareResult;
                                },
                                oldvalue: function() {
                                    return oldvalueCopy;
                                },
                            }
                        });
                    }
                }

                $rootScope.saveInvoiceVendorVehicleDetails = function() {
                    if (invoiceType == 'byVendor') {
                        if (invoicePending.absentDays == 'NO') {
                            invoicePending.absentDays = 0;
                        }

                        var data = {
                            sumTravelledDistance: invoicePending.totalKm,
                            extraDistace: invoicePending.extraKm,
                            noOfDays: invoicePending.absentDays,
                            invoiceId: invoicePending.invoiceId,
                            invoiceNumber: invoicePending.invoiceNumber,
                            invoiceRemarks: invoicePending.invoiceRemarks,
                            branchId: branchId,
                            userId: profileId,
                            statusFlg: 'P',
                            userRole: userRole,
                            combinedFacility: combinedFacility
                        };

                        $http.post('services/contract/modifiedInvoiceDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                                $('.loading').show();
                                ngDialog.open({
                                    template: 'Vendor Details updated successfully',
                                    plain: true
                                });
                            }

                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });


                    }

                    if (invoiceType == 'byVehicle') {
                        invoicePending.editByVehicleInvoiceClicked = false;
                    }
                }




            };

            /*Approval Invoice*/

            $scope.InvoiceApproval = function(invoiceType, invoicePending, index) {
                invoicePending.editByVendorInvoiceClicked = false;

                $confirm({
                        text: "Are you sure you want to Approve the invoice?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        if (invoiceType == 'byVendor') {
                            var data = {
                                sumTravelledDistance: invoicePending.newtotalKm,
                                extraDistace: invoicePending.newExtraKm,
                                noOfDays: invoicePending.newPresentDays,
                                invoiceId: invoicePending.invoiceId,
                                invoiceNumber: invoicePending.invoiceNumber,
                                invoiceRemarks: invoicePending.newInvoiceRemarks,
                                branchId: branchId,
                                fuelExtraAmount: invoice.newFuelAmount,
                                statusFlg: 'P',
                                userId: profileId,
                                userRole: userRole,
                                combinedFacility: combinedFacility
                            };

                            $http.post('services/contract/modifiedInvoiceDetails/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                    $('.loading').show();
                                    ngDialog.open({
                                        template: 'Invoice has been approved',
                                        plain: true
                                    });
                                }

                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });

                            $scope.invoicePendingData.splice(index, 1);
                        }

                        if (invoiceType == 'byVehicle') {
                            invoicePending.editByVehicleInvoiceClicked = false;
                        }

                    });

            };



            // Edit Contract Types

            $scope.editContractTypesApproval = function(invoiceType, contractTypesPending) {
                if (invoiceType == 'byVendor') {
                    contractTypesPending.editByContractTypeClicked = true;
                }
            };

            $scope.cancelContractTypeApproval = function(invoiceType, contractTypesPending, index) {
                var oldvalue = $scope.contractTypesPendingCopy[index];

                if (_.isMatch(contractTypesPending, oldvalue)) {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });
                    contractTypesPending.editByContractTypeClicked = false;
                    return false;
                } else {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });

                    contractTypesPending.editByContractTypeClicked = false;
                    contractTypesPending.contractType = oldvalue.contractType;
                    contractTypesPending.contractDescription = oldvalue.contractDescription;
                    contractTypesPending.serviceTax = oldvalue.serviceTax;
                }
            }

            $scope.saveContractTypesApproval = function(invoiceType, contractTypesPending, index) {
                if (invoiceType == 'byVendor') {

                    var oldvalueCopy = $scope.contractTypesPendingCopy[index];

                    if (_.isMatch(contractTypesPending, oldvalueCopy)) {
                        ngDialog.open({
                            template: 'No changes done',
                            plain: true
                        });
                        return false;
                    } else {
                        var compareResult = $scope.filter(oldvalueCopy, contractTypesPending);
                        $rootScope.compareResultObject = JSON.parse(JSON.stringify(compareResult));
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/invoiceConfirmationApproval.jsp',
                            controller: 'invoiceConfirmationApprovalCtrl',
                            size: 'lg',
                            resolve: {
                                compareResult: function() {
                                    return compareResult;
                                },
                                oldvalue: function() {
                                    return oldvalueCopy;
                                },
                            }
                        });
                    }

                    $rootScope.saveContractTypes = function() {
                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            userId: profileId,
                            contractStatus: "N",
                            serviceTax: contractTypesPending.serviceTax,
                            contractType: contractTypesPending.contractType,
                            contractDescription: contractTypesPending.contractDescription,
                            contractTypeId: contractTypesPending.contractTypeId,
                            combinedFacility: combinedFacility
                        };
                        $http.post('services/contract/updateContractType/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                                ngDialog.open({
                                    template: 'Contract Type Details Updated successfully',
                                    plain: true
                                });
                                contractTypesPending.editByContractTypeClicked = false;
                            }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });
                    }

                }
            }

            $scope.contractTypeApproval = function(invoiceType, contractTypesPending, index) {
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId,
                    contractStatus: "P",
                    serviceTax: contractTypesPending.serviceTax,
                    contractType: contractTypesPending.contractType,
                    contractDescription: contractTypesPending.contractDescription,
                    contractTypeId: contractTypesPending.contractTypeId,
                    combinedFacility: combinedFacility
                };
                $http.post('services/contract/updateContractType/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                        ngDialog.open({
                            template: 'Contract Type Approved successfully',
                            plain: true
                        });
                        $scope.contractTypesPending.splice(index, 1);
                        contractTypesPending.editByContractTypeClicked = false;
                    }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

        }
    };




    var checkOutRemarksApprovalCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, driver) {

        $scope.addRemarks = function(result) {
            $modalInstance.close(result)
        };

        $scope.joinDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'joinDate': true
                };
            }, 50);
        };

        $scope.relieveDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'relieveDate': true
                };
            }, 50);
        };

        //Function to check the Date Validity

        $scope.checkDateRangeValidity = function(joinDate, relieveDate) {
            if (joinDate > relieveDate) {
                $scope.dateRangeError = true;
                $timeout(function() {
                    $scope.dateRangeError = false;
                }, 3000);
                return false;
            } else return true;
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var noEntryRemarksApprovalCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, dataNoEntry) {

        $scope.addRemarks = function(result) {

            $modalInstance.close(result)
        };

        $scope.startDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'startDate': true
                };
            }, 50);
        };

        $scope.endDate = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'endDate': true
                };
            }, 50);
        };

        //Function to check the Date Validity

        $scope.checkDateRangeValidity = function(startDate, endDate) {
            if (startDate > endDate) {
                $scope.dateRangeError = true;
                $timeout(function() {
                    $scope.dateRangeError = false;
                }, 3000);
                return false;
            } else return true;
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    var invoiceConfirmationApprovalCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, compareResult, oldvalue) {


        $scope.totalKmOld = oldvalue.totalKm;
        $scope.extraKmOld = oldvalue.extraKm;
        $scope.fuelExtraAmountOld = oldvalue.fuelExtraAmount;
        $scope.absentDaysOld = oldvalue.absentDays;
        $scope.invoiceRemarksOld = oldvalue.invoiceRemarks;

        $scope.totalKmUpdated = compareResult.newtotalKm;
        $scope.extraKmUpdated = compareResult.newExtraKm;
        $scope.fuelExtraAmountUpdated = compareResult.newFuelAmount;
        $scope.absentDaysUpdated = compareResult.newPresentDays;
        $scope.invoiceRemarksUpdated = compareResult.newInvoiceRemarks;

        $scope.confirmModifiedInvoice = function() {
            $rootScope.saveInvoiceVendorVehicleDetails();
            $modalInstance.close(compareResult);
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var supervisorRemarksCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, post) {




        $scope.addRemarks = function(result) {
            $modalInstance.close(result)
        };


        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    var editTravelApprovalDistanceCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, ngDialog, trip) {
        $scope.addKmRequired = false;
        $scope.subtractionKmRequired = false;


        $scope.travelDistanceEditType = [{
            'value': 'addition',
            'text': 'Addition'
        }, {
            'value': 'subtraction',
            'text': 'Subtraction'
        }];

        $scope.travelDistance = $scope.travelDistanceEditType[0];
        $scope.addKm = String(trip.editDistance);
        $scope.remarks = trip.tripEditRemarks;

        $scope.settravelDistanceEditType = function(value) {
            if (value.text == 'Addition') {
                $scope.addKmRequired = true;
                $scope.subtractionKmRequired = false;
            } else {
                $scope.addKmRequired = false;
                $scope.subtractionKmRequired = true;
            }
        }

        $scope.addOrSubtractDistanceKm = function(value, addOrSubtractDistanceValue, remarks) {

            if (value == 'add') {
                var EditDistanceType = 'A';
                var DistanceType = "Added";
            } else {
                var EditDistanceType = 'S';
                var DistanceType = "Subtracted";
            }

            if (userRole == 'superadmin' || userRole == 'admin') {
                var serviceUrl = "services/approval/editDistanceByAdmin/";
                var approvalFlg = 'Y';
            } else {
                var serviceUrl = "services/approval/editDistanceWithRemarks/";
                var approvalFlg = 'N';
            }

            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                assignRouteId: trip.routeId,
                editedTravelledDistance: addOrSubtractDistanceValue,
                remarksForEditingTravelledDistance: remarks,
                editDistanceType: EditDistanceType,
                userId: profileId,
                approvalFlg: approvalFlg
            };

            $http.post(serviceUrl, data).
            success(function(data, status, headers, config) {
                if (userRole == 'superadmin' || userRole == 'admin') {
                    ngDialog.open({
                        template: 'Distance ' + DistanceType + ' added succesfully.',
                        plain: true
                    });
                    $timeout(function() {
                        $modalInstance.close()
                    }, 3000);

                } else {
                    ngDialog.open({
                        template: 'Distance ' + DistanceType + ' succesfully.It will add in actual travelled distance after admin approval',
                        plain: true
                    });
                    $timeout(function() {
                        $modalInstance.close()
                    }, 3000);
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }


        //CLOSE BUTTON FUNCTION
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    angular.module('efmfmApp').controller('approvalCtrl', approvalCtrl);
    angular.module('efmfmApp').controller('checkOutRemarksApprovalCtrl', checkOutRemarksApprovalCtrl);
    angular.module('efmfmApp').controller('invoiceConfirmationApprovalCtrl', invoiceConfirmationApprovalCtrl);
    angular.module('efmfmApp').controller('supervisorRemarksCtrl', supervisorRemarksCtrl);
    angular.module('efmfmApp').controller('editTravelApprovalDistanceCtrl', editTravelApprovalDistanceCtrl);
    angular.module('efmfmApp').controller('noEntryRemarksApprovalCtrl', noEntryRemarksApprovalCtrl);


}());