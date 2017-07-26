/* 
@date                   04/01/2015 
@Author                 Saima Aziz
@Description    
@Main Controllers       invoiceCtrl
@Modal Controllers      addContractDetailCtrl, byVehicleInvoiceCtrl
@template               partials/home.invoice.jsp
 
CODE CHANGE HISTORY 
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Saima Aziz      Initial Creation
04/15/2016  Saima Aziz      Final Creation
*/
(function() {

    var invoiceCtrl = function($scope, $state, $http, $timeout, $modal, $confirm, ngDialog, $filter, $rootScope, commonApiService) {

        if (!$scope.isInvoiceActive || $scope.isInvoiceActive == "false") {
            $state.go('home.accessDenied');
        } else {
            $scope.viewSummaryClicked = false;
            $scope.gotVendorData = false;
            $scope.gotVehicleData = false;
            $scope.isInvoice = false;
            $scope.vendors = {
                'invoiceNumber': ''
            };
            $scope.vehicleNumber;
            $scope.gotContractDetailsResults = false;
            $scope.vendorFixedDistanceBasedData = [];
            $scope.byVendorInvoicButtonClicked = false;
            $scope.partialInvoiceLoad = false;
            $scope.currentTab;
            $scope.gotExpenseResult = false;
            $scope.searchCriterias = ["By Vendor", "By Vehicle", "Fuel Price"];
            $scope.setMaxDate = new Date();
            $scope.monthYear;
            $scope.fromDate;
            $scope.routeTypeSelected;
            $scope.datePicker = [];
            $scope.toDate;
            $scope.vendors = {};
            $scope.partialInvoice = '';
            $scope.branchCode = branchCode;

            $scope.visibleEditVehicle = true;
            $scope.partialInvoiceButtonClicked = false;
            $scope.dates = [{
                    'name': 'Today',
                    'isClicked': false
                },
                {
                    'name': 'Yesturday',
                    'isClicked': false
                },
                {
                    'name': 'Last 7 Days',
                    'isClicked': false
                },
                {
                    'name': 'Last 30 Days',
                    'isClicked': false
                },
                {
                    'name': 'This Month',
                    'isClicked': false
                },
                {
                    'name': 'Custom Range Date',
                    'isClicked': false
                }
            ];
            $scope.expenseReporttButtonClicked = false;

            $scope.contractDetail = {};
            $scope.allPenalty = [{
                name: "No",
                Id: "No"
            }, {
                name: "Yes",
                Id: "Yes"
            }];
            $scope.fuel = [];

            $scope.invoiceTypeData = {
                invoiceType: [{
                        value: "invoice",
                        type: 'Invoice'
                    },
                    {
                        value: "partialInvoice",
                        type: 'Partial Invoice'
                    }
                ],
                selectedOption: {
                    value: "partialInvoice",
                    type: 'Partial Invoice'
                }
            };

            $scope.distanceArrayData = {
                distanceArray: [{
                        value: "GPS",
                        type: 'GPS'
                    },
                    {
                        value: "Odometer",
                        type: 'Odometer'
                    }
                ],
                selectedOption: {
                    value: "GPS",
                    type: 'GPS'
                }
            };

            $scope.setdistance = function(setdistance) {
                $scope.distanceFlg = setdistance;
            };

            $scope.currentPage = 0;
            $scope.pageSize = 10;
            $scope.data = [];
            $scope.q = '';

            $rootScope.extractText = function(str) {
                var ret = "";

                if (/"/.test(str)) {
                    ret = str.match(/"(.*?)"/)[1];
                } else {
                    ret = str;
                }

                return ret;
            }

            $scope.getData = function() {

                return $filter('filter')($scope.data, $scope.q)

            }

            $scope.numberOfPages = function() {
                return Math.ceil($scope.vehicleNumberDataLength / $scope.pageSize);
            }

            for (var i = 0; i < 300; i++) {
                $scope.data.push("Item " + i);
            }


            /*StartDate*/

            $scope.startDate = function($event, $index) {
                $event.preventDefault();
                $event.stopPropagation();

                $timeout(function() {

                    $scope.datePicker[$index] = {
                        'startDate': true
                    };
                }, 50);
            };

            /*EndDate*/

            $scope.endDate = function($event, $index) {
                $event.preventDefault();
                $event.stopPropagation();

                $timeout(function() {

                    $scope.datePicker[$index] = {
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


            $scope.viewNoOfAbsent = function(size, partialInvoice) {

                var data = $rootScope.data;

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/noOfDaysAbsentInvoice.jsp',
                    controller: 'viewNoOfAbsentCtrl',
                    size: size,
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        data: function() {
                            return data;
                        },
                        partialInvoice: function() {
                            return partialInvoice;
                        }
                    }
                });

            }

            $scope.viewTotalKmInvoice = function(size, partialInvoice, distanceArray) {
                var data = $rootScope.data;

                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/viewTotalKmInvoice.jsp',
                    controller: 'totalKmInvoiceCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    size: size,
                    resolve: {
                        data: function() {
                            return data;
                        },
                        partialInvoice: function() {
                            return partialInvoice;
                        },
                        distanceArray: function() {
                            return distanceArray;
                        }
                    }
                });

            }

            $scope.addContractType = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addContactTypeModal.jsp',
                    controller: 'addContactTypeCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {}
                });

                modalInstance.result.then(function(result) {
                    var contractTypeLength = $scope.contractTypeDatas.length + 3;
                    $scope.contractTypeDatas.push({
                        "contractType": result.contractType,
                        "contractDescription": result.contractDescription,
                        "serviceTax": result.serviceTax,
                        "contractTypeId": contractTypeLength
                    });
                });
            };

            /*Get Contract Type Details*/

            $scope.getContractTypeDetail = function() {
                $scope.gotContractTypeResults = false;
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    userId: profileId
                };
                $http.post('services/contract/getAllContractType/', data).
                success(function(data, status, headers, config) {
                    $scope.gotContractTypeResults = true;
                    if (data.status != "invalidRequest") {
                    $scope.contractTypeDatas = data;
                    $scope.contractTypeCopy = angular.copy(data);
                        }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }


            // List Of Contract Type 

            var data = {
                branchId: branchId,
                userId: profileId
            };
            $http.post('services/contract/listOfContractType/', data).
            success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
                $scope.contractData = data;

            }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

            /*List Of Fuel Type*/

            var data = {
                branchId: branchId,
                userId: profileId
            };
            $http.post('services/contract/listOfFuelContractType/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                $scope.fuelContractType = data;
                $scope.fuelContractType
                    .unshift({
                        contractDescription: "Not Required",
                        contractFuelType: "NR",
                        fuelContractId: 0
                    });
                }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

            /*Getting Generated Invoice Details*/

            $scope.generatedInvoiceDetails = function() {
                $scope.gotInvoiceSummaryResults = false;
                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId
                    },
                    invoiceStatus: "A",
                    userId: profileId
                };
                $http.post('services/contract/generatedInvoiceDetails', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    $scope.gotInvoiceSummaryResults = true;
                    $scope.invoiceDetailsData = data;
                }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }

            $scope.deleteInvoiceDetails = function(invoiceDetails, index) {
                $confirm({
                        text: "Are you sure want to delete this invoice ?",
                        title: 'Delete Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(
                        function() {
                            var data = {
                                eFmFmClientBranchPO: {
                                    branchId: branchId
                                },
                                invoiceStatus: "D",
                                invoiceNumber: invoiceDetails.InvoiceNumber,
                                userId: profileId
                            };
                            $http.post('services/contract/disableInvoiceDetails', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                ngDialog.open({
                                    template: 'Invoice deleted successfully',
                                    plain: true
                                });
                                $scope.invoiceDetailsData.splice(index, 1);
                                    }
                            }).
                            error(function(data, status, headers, config) {
                                // log error
                            });
                        });
            }

            //Show Custome Alert Messages

            $scope.showalertMessageModal = function(message, hint) {
                $scope.alertMessage = message;
                $scope.alertHint = hint;
                $('.loading').show();
                $('.alert_Modal').show('slow', function() {
                    $timeout(function() {
                        $('.alert_Modal').fadeOut('slow');
                        $('.loading').hide();
                    }, 3000);
                });
            };

            $scope.setFuelTypeData = function(fuelType) {

                $scope.contractDetail.contractDescription = fuelType.contractDescription;
            }

            //Get last Month
            var getLastMonth = function() {
                var previousMonth = new Date();
                previousMonth.setDate(1);
                previousMonth.setMonth(previousMonth.getMonth() - 1);
                return previousMonth;
            };

            $scope.setMaxDate = new Date(getLastMonth());

            //This Function is called on the Click of the ITEMS in the Date Popover
            $scope.updateDate = function(type) {
                switch (type) {
                    case 'today':
                        $scope.openCustomRange = false;
                        $scope.getReport(new Date(), new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[0].isClicked = true;
                        break;
                    case 'yesturday':
                        $scope.openCustomRange = false;
                        var yesturdayDate = new Date();
                        yesturdayDate.setDate(yesturdayDate.getDate() - 1);
                        $scope.getReport(yesturdayDate, yesturdayDate);
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[1].isClicked = true;
                        break;
                    case 'last7':
                        $scope.openCustomRange = false;
                        var last7 = new Date();
                        last7.setDate(last7.getDate() - 7);
                        $scope.getReport(last7, new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[2].isClicked = true;
                        break;
                    case 'last30':
                        $scope.openCustomRange = false;
                        var last30 = new Date();
                        last30.setDate(last30.getDate() - 30);
                        $scope.getReport(last30, new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[3].isClicked = true;
                        break;
                    case 'thisMonth':
                        $scope.openCustomRange = false;
                        var thisMonth = new Date();
                        thisMonth.setMonth(thisMonth.getMonth(), 1);
                        $scope.dates[4].value = thisMonth;
                        $scope.getReport(thisMonth, new Date());
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        $scope.dates[4].isClicked = true;
                        break;
                    case 'customDate':
                        if ($scope.openCustomRange) {
                            $scope.openCustomRange = false;
                            $scope.from = '';
                            $scope.to = '';
                        } else {
                            $scope.openCustomRange = true;
                        }
                        angular.forEach($scope.dates, function(item) {
                            item.isClicked = false;
                        });
                        break;
                }
            };

            $scope.$on('$viewContentLoaded', function() {
                $scope.getActiveVendors();
                $scope.getInvoiceNumbers();
            });

            $scope.getActiveVendors = function() {
                var data = {
                    branchId: branchId,
                    userId: profileId,
                    combinedFacility: branchId
                };
                $http.post('services/contract/allActiveVendor/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    $scope.vendorsData = data;
                        }

                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };


            $scope.getInvoiceNumbers = function() {
                var data = {
                    branchId: branchId,
                    userId: profileId
                };
                $http.post('services/contract/listOfInvoiceDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    $scope.invoiceNumbersData = data;
                        }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            var convertMonthYear = function(d) {
                var month = d.getMonth() + 1;
                var year = d.getFullYear()
                if (month < 10) {
                    month = '0' + month;
                }
                return month + '-' + year;
            };

            $scope.$watch(
                function($scope) {
                    if ($scope.vendors.invoiceNumber) {
                        $scope.isInvoice = true;
                    } else $scope.isInvoice = false;
                    return $scope.vendors.invoiceNumber;

                },
                function(newValue, oldvalue) {
                    if (oldvalue !== newValue) {}
                }
            );

            $scope.vendorTab = function() {

                $scope.vendors = {
                    'selectVendor': '',
                    'monthSelected': '',
                    'invoiceNumber': ''
                };
                $scope.currentTab = "By Vendor";


            };


            $scope.invoiceGenDateFunction = function() {
                $scope.setMaxDateEnd = new Date();
                var data = {
                    branchId: branchId,
                    userId: profileId
                };

                $http.post('services/user/branchdetail/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    $rootScope.invoiceGenDate = data.invoiceGenDate;
                    $scope.distanceFlg = data.distanceFlg;
                    var currentDate = new Date();
                    var firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
                    firstDay.setDate(firstDay.getDate() + $rootScope.invoiceGenDate - 1);
                    $scope.currentDateDisabled = firstDay;
                }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }
            $scope.invoiceGenDateFunction();



            $scope.getInvoiceByVendor = function(vendor, invoiceByVendorData, distanceFlg) {

                $scope.invoiceByVendorData = {};
                if ($scope.vendors.selectVendor == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Vendor Name',
                        plain: true
                    });
                    return false;
                }
                if ($scope.vendors.monthSelected == undefined && invoiceByVendorData.invoiceNumber == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Month or vehicle Number',
                        plain: true
                    });
                    return false;
                }
                var data;

                $scope.gotVendorData = false;

                if (angular.isObject($scope.vendors.selectVendor) && angular.isDate($scope.vendors.monthSelected)) {
                    $scope.byVendorInvoicButtonClicked = true;
                    var actionTypes = "VENDORBASED";
                    var vendorId = vendor.selectVendor.vendorId;

                    var d = new Date(vendor.monthSelected);
                    $scope.monthYear = convertMonthYear(d);
                    data = {
                        invoiceDate: $scope.monthYear,
                        actionType: actionTypes,
                        efmFmVendorMaster: {
                            vendorId: vendorId,
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        distanceFlg: distanceFlg.value,
                        userId: profileId,
                        invoiceAcptType: 'N'
                    };

                    $http.post('services/contract/invoiceTripDetails/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {

                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                            ngDialog.open({
                                template: 'kinldy create the Contract Details for Vehicles',
                                plain: true
                            });

                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                            ngDialog.open({
                                template: 'Create the contract details and the mapped to vehicles',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'INVALIDGENTYPE') {
                            ngDialog.open({
                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'NODATA') {
                            ngDialog.open({
                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'NOTFOUNDCONTRACT') {
                            ngDialog.open({
                                template: 'Contract Details not Configured for this vendor Name',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'REMAININGVEHICLES') {
                            $scope.byVendorInvoicButtonClicked = false;
                            $confirm({
                                    text: "Are you sure want to generate the invoice remaining vehicles?",
                                    title: 'Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    $scope.byVendorInvoicButtonClicked = true;
                                    data = {
                                        invoiceDate: $scope.monthYear,
                                        actionType: actionTypes,
                                        efmFmVendorMaster: {
                                            vendorId: vendorId,
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            }
                                        },
                                        distanceFlg: distanceFlg.value,
                                        userId: profileId,
                                        invoiceAcptType: 'Y'
                                    };
                                    $http.post('services/contract/invoiceTripDetails/', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                        $scope.byVendorInvoicButtonClicked = false;
                                        $scope.invoiceByVendorData = data;
                                        $scope.invoiceByVendor = data;
                                        $scope.vendorFixedDistanceBasedData = data.fixedDistanceBased;
                                        if (data.contractTypeCount == 1) {
                                            $scope.contractTypeValues = "contractSingle";
                                        } else {
                                            $scope.contractTypeValues = "contractMultiple";
                                        }
                                        $rootScope.vendorFixedDistance = angular.copy(data.fixedDistanceBased);
                                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                                            ngDialog.open({
                                                template: 'kinldy create the Contract Details for Vehicles',
                                                plain: true
                                            });

                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                                            ngDialog.open({
                                                template: 'Create the contract details and the mapped to vehicles',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }

                                        if (data.failed == 'INVALIDGENTYPE') {
                                            ngDialog.open({
                                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NODATA') {
                                            ngDialog.open({
                                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }

                                        if (data.failed == 'NOTFOUNDCONTRACT') {
                                            ngDialog.open({
                                                template: 'Contract Details not Configured for this vendor Name',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }
                                        if ($scope.vendorFixedDistanceBasedData.length > 0) {
                                            $scope.vendorName = $scope.invoiceByVendor.vendorName;
                                            $scope.invoiceNumber = $scope.invoiceByVendor.invoiceNumber;
                                            $scope.invoiceMonthDate = $scope.invoiceByVendor.invoiceMonthDate;
                                            $scope.invoiceCreationDate = $scope.invoiceByVendor.invoiceCreationDate;
                                            $scope.noOfvehicle = $scope.invoiceByVendor.noOfvehicle;
                                            $scope.totalAmount = $scope.invoiceByVendor.totalAmount;
                                            $scope.totalPayableAmount = $scope.invoiceByVendor.totalPayableAmount;
                                            $scope.total = $scope.invoiceByVendor.total;
                                            $scope.serviceTax = $scope.invoiceByVendor.serviceTax;
                                            $scope.serviceTaxAmount = $scope.invoiceByVendor.serviceTaxAmount;
                                            $scope.penalty = $scope.invoiceByVendor.penalty;
                                            $scope.invoiceRemarks = $scope.invoiceByVendor.invoiceRemarks;
                                            $scope.gotVendorData = true;
                                            $scope.viewSummaryClicked = false;


                                            angular.forEach($scope.vendorFixedDistanceBasedData, function(item) {
                                                item.editByVendorInvoiceClicked = false;
                                            });
                                        } else {
                                            $scope.showalertMessage('No Invoice found. Please try again!', '');
                                            $('.loading').show();
                                            ngDialog.open({
                                                template: 'No Invoice found. Please try again!',
                                                plain: true
                                            });
                                    }
                                        }
                                    });
                                });
                        }

                        if (data.failed == 'NOTGENINV') {
                            $scope.byVendorInvoicButtonClicked = false;
                            $confirm({
                                    text: "Are you sure want to generate invoice?",
                                    title: 'Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    $scope.byVendorInvoicButtonClicked = true;
                                    data = {
                                        invoiceDate: $scope.monthYear,
                                        actionType: actionTypes,
                                        efmFmVendorMaster: {
                                            vendorId: vendorId,
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            }
                                        },
                                        distanceFlg: distanceFlg.value,
                                        userId: profileId,
                                        invoiceAcptType: 'Y'
                                    };
                                    $http.post('services/contract/invoiceTripDetails/', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                        $scope.byVendorInvoicButtonClicked = false;
                                        $scope.invoiceByVendorData = data;
                                        $scope.invoiceByVendor = data;
                                        if (data.contractTypeCount == 1) {
                                            $scope.contractTypeValues = "contractSingle";
                                        } else {
                                            $scope.contractTypeValues = "contractMultiple";
                                        }
                                        $scope.vendorFixedDistanceBasedData = data.fixedDistanceBased;
                                        $rootScope.vendorFixedDistance = angular.copy(data.fixedDistanceBased);
                                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                                            ngDialog.open({
                                                template: 'kinldy create the Contract Details for Vehicles',
                                                plain: true
                                            });

                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                                            ngDialog.open({
                                                template: 'Create the contract details and the mapped to vehicles',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }

                                        if (data.failed == 'INVALIDGENTYPE') {
                                            ngDialog.open({
                                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NODATA') {
                                            ngDialog.open({
                                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }

                                        if (data.failed == 'NOTFOUNDCONTRACT') {
                                            ngDialog.open({
                                                template: 'Contract Details not Configured for this vendor Name',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }
                                        if ($scope.vendorFixedDistanceBasedData.length > 0) {
                                            $scope.vendorName = $scope.invoiceByVendor.vendorName;
                                            $scope.invoiceNumber = $scope.invoiceByVendor.invoiceNumber;
                                            $scope.invoiceMonthDate = $scope.invoiceByVendor.invoiceMonthDate;
                                            $scope.invoiceCreationDate = $scope.invoiceByVendor.invoiceCreationDate;
                                            $scope.noOfvehicle = $scope.invoiceByVendor.noOfvehicle;
                                            $scope.totalAmount = $scope.invoiceByVendor.totalAmount;
                                            $scope.totalPayableAmount = $scope.invoiceByVendor.totalPayableAmount;
                                            $scope.total = $scope.invoiceByVendor.total;
                                            $scope.serviceTax = $scope.invoiceByVendor.serviceTax;
                                            $scope.serviceTaxAmount = $scope.invoiceByVendor.serviceTaxAmount;
                                            $scope.penalty = $scope.invoiceByVendor.penalty;
                                            $scope.invoiceRemarks = $scope.invoiceByVendor.invoiceRemarks;
                                            $scope.gotVendorData = true;
                                            $scope.viewSummaryClicked = false;


                                            angular.forEach($scope.vendorFixedDistanceBasedData, function(item) {
                                                item.editByVendorInvoiceClicked = false;
                                            });
                                        } else {
                                            $scope.showalertMessage('No Invoice found. Please try again!', '');
                                            $('.loading').show();
                                            ngDialog.open({
                                                template: 'No Invoice found. Please try again!',
                                                plain: true
                                            });
                                        }
                                    }
                                    });
                                });
                        }

                        if (data.failed == 'ALREADYGENINV') {
                            $scope.byVendorInvoicButtonClicked = false;
                            $confirm({
                                    text: "Invoice already generated If you want view the details?",
                                    title: 'Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    data = {
                                        invoiceDate: $scope.monthYear,
                                        actionType: actionTypes,
                                        efmFmVendorMaster: {
                                            vendorId: vendorId,
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            }
                                        },
                                        distanceFlg: distanceFlg.value,
                                        userId: profileId,
                                        invoiceAcptType: 'Y'
                                    };
                                    $scope.byVendorInvoicButtonClicked = true;
                                    $http.post('services/contract/invoiceTripDetails/', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                        $scope.byVendorInvoicButtonClicked = false;
                                        $scope.invoiceByVendorData = data;
                                        $scope.invoiceByVendor = data;
                                        $scope.vendorFixedDistanceBasedData = data.fixedDistanceBased;
                                        $rootScope.vendorFixedDistance = angular.copy(data.fixedDistanceBased);
                                        if (data.contractTypeCount == 1) {
                                            $scope.contractTypeValues = "contractSingle";
                                        } else {
                                            $scope.contractTypeValues = "contractMultiple";
                                        }
                                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                                            ngDialog.open({
                                                template: 'kinldy create the Contract Details for Vehicles',
                                                plain: true
                                            });

                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                                            ngDialog.open({
                                                template: 'Create the contract details and the mapped to vehicles',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }

                                        if (data.failed == 'INVALIDGENTYPE') {
                                            ngDialog.open({
                                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NODATA') {
                                            ngDialog.open({
                                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }

                                        if (data.failed == 'NOTFOUNDCONTRACT') {
                                            ngDialog.open({
                                                template: 'Contract Details not Configured for this vendor Name',
                                                plain: true
                                            });
                                            $scope.byVendorInvoicButtonClicked = false;
                                            return false;
                                        }
                                        if ($scope.vendorFixedDistanceBasedData.length > 0) {
                                            $scope.vendorName = $scope.invoiceByVendor.vendorName;
                                            $scope.invoiceNumber = $scope.invoiceByVendor.invoiceNumber;
                                            $scope.invoiceMonthDate = $scope.invoiceByVendor.invoiceMonthDate;
                                            $scope.invoiceCreationDate = $scope.invoiceByVendor.invoiceCreationDate;
                                            $scope.noOfvehicle = $scope.invoiceByVendor.noOfvehicle;
                                            $scope.totalAmount = $scope.invoiceByVendor.totalAmount;
                                            $scope.totalPayableAmount = $scope.invoiceByVendor.totalPayableAmount;
                                            $scope.total = $scope.invoiceByVendor.total;
                                            $scope.serviceTax = $scope.invoiceByVendor.serviceTax;
                                            $scope.serviceTaxAmount = $scope.invoiceByVendor.serviceTaxAmount;
                                            $scope.penalty = $scope.invoiceByVendor.penalty;
                                            $scope.invoiceRemarks = $scope.invoiceByVendor.invoiceRemarks;
                                            $scope.gotVendorData = true;
                                            $scope.viewSummaryClicked = false;


                                            angular.forEach($scope.vendorFixedDistanceBasedData, function(item) {
                                                item.editByVendorInvoiceClicked = false;
                                            });
                                        } else {
                                            $scope.showalertMessage('No Invoice found. Please try again!', '');
                                            $('.loading').show();
                                            ngDialog.open({
                                                template: 'No Invoice found. Please try again!',
                                                plain: true
                                            });
                                    }
                                        }
                                    });

                                }, function() {
                                    return false;
                                });
                       }
                        }
                    }).
                    error(function(data, status, headers, config) {
                        $scope.byVendorInvoicButtonClicked = false;
                    });
                } else {
                    $scope.byVendorInvoicButtonClicked = true;
                    var actionTypes = "INVOICEDETAILS";
                    data = {
                        actionType: actionTypes,
                        invoiceNumber: invoiceByVendorData.invoiceNumber,
                        efmFmVendorMaster: {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        distanceFlg: distanceFlg.value,
                        userId: profileId
                    };

                    $http.post('services/contract/invoiceTripDetails/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                            ngDialog
                                .open({
                                    template: 'kinldy create the Contract Details for Vehicles',
                                    plain: true
                                });

                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'NODATA') {
                            ngDialog.open({
                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                            ngDialog.open({
                                template: 'Create the contract details and the mapped to vehicles',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        if (data.failed == 'NOTFOUNDCONTRACT') {
                            ngDialog.open({
                                template: 'Contract Details not Configured for this vendor Name',
                                plain: true
                            });
                            $scope.byVendorInvoicButtonClicked = false;
                            return false;
                        }

                        $scope.byVendorInvoicButtonClicked = false;
                        $scope.invoiceByVendorData = data;
                        $scope.invoiceByVendor = data;
                        $scope.vendorFixedDistanceBasedData = data.fixedDistanceBased;
                        if (data.contractTypeCount == 1) {
                            $scope.contractTypeValues = "contractSingle";
                        } else {
                            $scope.contractTypeValues = "contractMultiple";
                        }

                        if ($scope.vendorFixedDistanceBasedData.length > 0) {
                            $scope.vendorName = $scope.invoiceByVendor.vendorName;
                            $scope.invoiceNumber = $scope.invoiceByVendor.invoiceNumber;
                            $scope.invoiceMonthDate = $scope.invoiceByVendor.invoiceMonthDate;
                            $scope.invoiceCreationDate = $scope.invoiceByVendor.invoiceCreationDate;
                            $scope.noOfvehicle = $scope.invoiceByVendor.noOfvehicle;
                            $scope.totalAmount = $scope.invoiceByVendor.totalAmount;
                            $scope.totalPayableAmount = $scope.invoiceByVendor.totalPayableAmount;
                            $scope.total = $scope.invoiceByVendor.total;
                            $scope.serviceTax = $scope.invoiceByVendor.serviceTax;
                            $scope.serviceTaxAmount = $scope.invoiceByVendor.serviceTaxAmount;
                            $scope.penalty = $scope.invoiceByVendor.penalty;
                            $scope.gotVendorData = true;
                            $scope.viewSummaryClicked = false;
                            $scope.vendors = {
                                'selectVendor': '',
                                'monthSelected': '',
                                'invoiceNumber': ''
                            };


                            angular.forEach($scope.vendorFixedDistanceBasedData, function(item) {
                                item.editByVendorInvoiceClicked = false;
                            });
                        } else {
                            $('.loading').show();
                            ngDialog.open({
                                template: 'No Invoice found. Please try again!',
                                plain: true
                            });
                        }
                    }
                    }).error(function(data, status, headers, config) {
                        $scope.byVendorInvoicButtonClicked = false;
                    });
                }

            };


            $scope.partialInvoiceExcel = function(invoiceByVendorData) {
                var dataObj = $rootScope.data;
                var data = {
                    efmFmVendorMaster: dataObj.efmFmVendorMaster,
                    vehicleId: dataObj.vehicleId,
                    fromDate: dataObj.fromDate,
                    toDate: dataObj.toDate,
                    distanceFlg: dataObj.distanceFlg,
                    contractType: invoiceByVendorData[0].contractType,
                    userId: profileId
                };

                $http({
                    url: 'services/contract/partialInvoiceDownload',
                    method: "POST",
                    data: data,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    var blob = new Blob([data], {});
                    saveAs(blob, 'Partial Invoice' + '.xlsx');
                }
                }).error(function(data, status, headers, config) {
                    alert("Download Failed")
                });
            }

            var dateFormatConverter = function(x) {
                var date = x.split('-');
                return date[1] + '-' + date[0] + '-' + date[2];
            };
            //Convert the dates in DD-MM-YYYY format
            var convertDateUTC = function(date) {
                var convert_date = new Date(date);

                var currentDay = convert_date.getDate();
                if (currentDay < 10) {
                    currentDay = '0' + currentDay;
                }
                var currentMonth = convert_date.getMonth() + 1;
                if (currentMonth < 10) {
                    currentMonth = '0' + currentMonth;
                }
                return currentDay + '-' + currentMonth + '-' + convert_date.getFullYear();
            };

            $scope.editInvoice = function(invoiceType, invoice, contractDetail) {
                if (invoiceType == 'byVendor') {
                    if (invoice.absentDays == 'NO') {
                        invoice.absentDays = 0;
                    }
                    invoice.editByVendorInvoiceClicked = true;
                }

                if (invoiceType == 'byVehicle') {
                    if (invoice.absentDays == 'NO') {
                        invoice.absentDays = 0;
                    }
                    invoice.editByVehicleInvoiceClicked = true;
                }
                if (invoiceType == 'contractDetail') {
                    contractDetail.editByContractInvoiceClicked = true;
                }
            };

            $scope.editContractType = function(contractTypeData, index) {
                contractTypeData.editByContractInvoiceClicked = true;
            }

            $scope.cancelContractType = function(contractTypeData, index) {
                var oldvalue = $scope.contractTypeCopy[index];
                if (_.isMatch(contractTypeData, oldvalue)) {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });
                    contractTypeData.editByContractInvoiceClicked = false;
                    return false;
                } else {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });

                    contractTypeData.editByContractInvoiceClicked = false;
                    contractTypeData.contractType = oldvalue.contractType;
                    contractTypeData.contractDescription = oldvalue.contractDescription;
                    contractTypeData.serviceTax = oldvalue.serviceTax;
                }
            }

            $scope.saveContractType = function(contractTypeData, index) {
                var oldvalue = $scope.contractTypeCopy[index];
                if (_.isMatch(contractTypeData, oldvalue)) {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });
                    return false;
                } else {
                    var compareResult = $scope.filter(contractTypeData, oldvalue);
                    var modalInstance = $modal.open({
                        templateUrl: 'partials/modals/invoiceContractTypeConfirmation.jsp',
                        controller: 'invoiceContractTypeConfirmCtrl',
                        size: 'lg',
                        backdrop: 'static',
                        keyboard: false,
                        resolve: {
                            compareResult: function() {
                                return compareResult;
                            },
                            contractTypeData: function() {
                                return contractTypeData;
                            },
                        }
                    });
                }

                $rootScope.updateContractType = function() {
                    var data = {
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        },
                        userId: profileId,
                        contractStatus: "Y",
                        serviceTax: contractTypeData.serviceTax,
                        contractType: contractTypeData.contractType,
                        contractDescription: contractTypeData.contractDescription,
                        contractTypeId: contractTypeData.contractTypeId
                    };
                    $http.post('services/contract/updateContractType/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        ngDialog.open({
                            template: 'Contract Type Details Updated successfully',
                            plain: true
                        });
                        contractTypeData.editByContractInvoiceClicked = false;
                    }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }

            }


            //Convert the dates in DD-MM-YYYY format
            var convertDateUTC = function(date) {
                var convert_date = new Date(date);

                var currentDay = convert_date.getDate();
                if (currentDay < 10) {
                    currentDay = '0' + currentDay;
                }
                var currentMonth = convert_date.getMonth() + 1;
                if (currentMonth < 10) {
                    currentMonth = '0' + currentMonth;
                }
                return currentDay + '-' + currentMonth + '-' + convert_date.getFullYear();
            };

            $scope.partialInvoiceChange = function() {
                $scope.vendorFixedDistanceBasedData = [];
                $scope.partialInvoiceButtonClicked = false;
                $scope.gotVendorData = false;
            }

            // Get All Vendor Lists

            $scope.allInspectionVendors = [];


            var data = {
                branchId: branchId,
                userId: profileId,
                combinedFacility: branchId
            };
            $http.post('services/contract/allActiveVendor/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                angular.forEach(data, function(item) {
                    $scope.allInspectionVendors.push({
                        name: item.name,
                        Id: item.vendorId
                    })
                });
            }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

            $scope.allVehiclesInpectionForm = [];

            $scope.getVehicleList = function(getVehicleList) {
                if (getVehicleList) {
                    var data = {
                        vendorId: getVehicleList.Id,
                        userId: profileId,
                        branchId: branchId
                    };
                    $scope.allVehiclesInpectionForm = [];
                    $http.post('services/contract/getCheckInEntity/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        angular.forEach(data, function(item) {
                            $scope.allVehiclesInpectionForm.push({
                                vehicleNumber: item.vehicleNumber,
                                Id: item.vehicleId
                            });
                        });
                    }
                    });
                } else {
                    $scope.allVehiclesInpectionForm = [];
                }

            }



            $scope.getPartialInvoice = function(partialInvoice, distanceFlg) {
                $scope.invoiceGenDateFunction();
                $scope.partialInvoiceLoad = true;
                $scope.partialInvoiceButtonClicked = false;

                if (partialInvoice == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Vendor Name, StartDate and EndDate',
                        plain: true
                    });
                    $scope.partialInvoiceLoad = false;
                    return false;

                }
                if (partialInvoice.vendor == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Vendor Name',
                        plain: true
                    });
                    $scope.partialInvoiceLoad = false;
                    return false;

                }

                if (partialInvoice.vehicleSelected == undefined) {
                    $scope.vehicleSelected = 0;
                } else {
                    $scope.vehicleSelected = partialInvoice.vehicleSelected.Id;
                }


                if (partialInvoice.startDate == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose Start Date',
                        plain: true
                    });
                    $scope.partialInvoiceLoad = false;
                    return false;
                }
                if (partialInvoice.endDate == undefined) {
                    ngDialog.open({
                        template: 'Kindly Choose End Date',
                        plain: true
                    });
                    $scope.partialInvoiceLoad = false;
                    return false;
                }
                var data = {
                    efmFmVendorMaster: {
                        vendorId: partialInvoice.vendor.Id,
                        eFmFmClientBranchPO: {
                            branchId: branchId
                        }
                    },
                    vehicleId: $scope.vehicleSelected,
                    fromDate: convertDateUTC(partialInvoice.startDate),
                    toDate: convertDateUTC(partialInvoice.endDate),
                    distanceFlg: distanceFlg.value,
                    userId: profileId
                };

                $rootScope.data = data;

                $http.post('services/contract/partialInvoiceDetails', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    if (data.failed == 'NOTFOUNDCONTRACT') {
                        ngDialog.open({
                            template: 'Contract Details not Configured for this vendor Name',
                            plain: true
                        });
                    }

                    $scope.partialInvoiceLoad = false;
                    $scope.partialInvoiceButtonClicked = true;
                    $scope.partialInvoiceDetails = data.partialInvoiceDetails;
                    $scope.partialInvoiceLength = data.partialInvoiceDetails.length;

                    if ($scope.partialInvoiceLength == 0) {
                        ngDialog.open({
                            template: 'No Records Found',
                            plain: true
                        });
                        $scope.partialInvoiceButtonClicked = false;
                        return false;
                    }
                }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            }


            $scope.updateVehicleWiseContract = function(newValue, id, vehicleNumber) {

                if (vehicleNumber.travelledKm == newValue.travelledKm) {
                    ngDialog.open({
                        template: 'No Changes done',
                        plain: true
                    });
                    return false;
                }
                $confirm({
                        text: "Are you sure you want to save the changes?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {
                        var data = {
                            vehicleId: vehicleNumber.vehicleId,
                            monthlyPendingFixedDistance: newValue.travelledKm,
                            userId: profileId
                        };

                        $http.post('services/contract/updatePendingKm', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                            ngDialog.open({
                                template: 'Vehicle Wise Travelled KM updated successfully',
                                plain: true
                            });
                            vehicleNumber.travelledKm = newValue.travelledKm;
                            $scope.getVehicleWiseContract();
                        }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });

                    });
                return false;
            };



            // SaveChanges contract Detail


            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1,
                showWeeks: false
            };

            $scope.formats = ['yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-mm-yyyy', 'mm-yyyy'];
            $scope.monthformat = 'MMMM' + ' - ' + 'yyyy';

            $scope.datepickerOptions = {
                datepickerMode: "'month'",
                minMode: "month",
                minDate: "minDate",
                showWeeks: "false"
            };


            $scope.cloneRecord = function(index, contractDetail, invoice) {
                var contractTypeObject = _.findWhere($scope.contractData, {
                    contractType: contractDetail.contractType
                });
                var fuelContractTypeObject = _.findWhere($scope.fuelContractType, {
                    contractDescription: contractDetail.contractDescription
                });

                $scope.contractDetailData[index].disabled = true;

                $scope.cloneData = {};

                angular.forEach($scope.contractData, function(value, key) {
                    if (value.contractDescription === $scope.cloneData.contractDescription) {
                        $scope.cloneData.contractDescription = value.contractId;
                    }
                });

                angular.copy(contractDetail, $scope.cloneData);
                $scope.cloneData['contractType'] = contractTypeObject;
                $scope.cloneData['contractDescription'] = fuelContractTypeObject;

                $scope.cloneData.cloneId = contractDetail.contractId;
                $scope.cloneData.editByContractInvoiceClicked = true;
                $scope.contractDetailData.splice(index + 1, 0, $scope.cloneData);
            }


            $scope.saveChangesContractDetail = function(invoiceType, invoice, contractDetail, index) {



                //Show Custome Alert Messages
                $scope.showalertMessageModal = function(message, hint) {
                    $scope.alertMessage = message;
                    $scope.alertHint = hint;
                    $('.loading').show();
                    $('.alert_Modal').show('slow', function() {
                        $timeout(function() {
                            $('.alert_Modal').fadeOut('slow');
                            $('.loading').hide();
                        }, 3000);
                    });
                };

                //BUTTON ACTION :: CLOSE ALERT
                $scope.closeAlertMessageModal = function() {
                    $('.loading').hide();
                    $('.alert_Modal').hide('slow');
                };

                //StartDate Conversion

                var myStartDate = new Date(contractDetail.startDate);
                var month = myStartDate.getMonth() + 1;
                var startDate = myStartDate.getFullYear() + "-" + month + "-" + myStartDate.getDate();


                //EndDate Conversion

                var myEndDate = new Date(contractDetail.endDate);
                var month = myEndDate.getMonth() + 1;
                var endDate = myEndDate.getFullYear() + "-" + month + "-" + myEndDate.getDate();

                contractDetail.editByContractInvoiceClicked = false;

                if (contractDetail.contractDescription.contractFuelType == 'NR') {
                    contractFuelType = 'NR';
                } else {
                    contractFuelType = 'Y';
                }

                $confirm({
                        text: "Are you sure you want to save the changes?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var data = {
                            contractTitle: contractDetail.contractTitle,
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            eFmFmVendorContractTypeMaster: {
                                contractTypeId: parseInt(contractDetail.contractType.contractId)
                            },
                            eFmFmVendorFuelContractTypeMaster: {
                                fuelTypeId: contractDetail.contractDescription.fuelContractId
                            },
                            startDate: startDate,
                            endDate: endDate,
                            extraDistanceChargeRate: contractDetail.extraDistanceChargeRate,
                            fixedDistanceChargeRate: contractDetail.fixedDistanceChargeRate,
                            fixedDistanceMonthly: contractDetail.fixedDistanceChargeMonthly,
                            fixedDistancePrDay: contractDetail.fixedDistancePerDay,
                            minimumDays: contractDetail.minimumDays,
                            cloneId: $scope.cloneData.cloneId,
                            penalty: contractDetail.penalty,
                            penaltyInPercentagePerDay: contractDetail.panalityPercentage,
                            perDayCost: contractDetail.perDayCost,
                            petrolPrice: contractDetail.petrolPrice,
                            fixedDistanceMonthly: Number(contractDetail.contractDistance),
                            userId: profileId,
                            fuelPriceCalculation: contractFuelType,
                            perKmCost: contractDetail.perKmCost

                        };

                        contractDetail.contractDescription = contractDetail.contractDescription.contractDescription;
                        contractDetail.contractType = contractDetail.contractType.contractType;

                        $http.post('services/contract/addContractDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                            if (data.status == 'AlreadyExist') {
                                ngDialog.open({
                                    template: 'Contact Title already exists',
                                    plain: true
                                });
                                return false;
                            }

                            $('.loading').show();
                            ngDialog.open({
                                template: 'Contact Details added successfully',
                                plain: true
                            });
                            $scope.clone = false;
                        }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });

                        if (invoiceType == 'contractDetail') {
                            contractDetail.editByContractInvoiceClicked = false;
                        }

                    });

            };


            $scope.checkTotalKmValue = function(invoice, index) {
                $scope.invoiceTotalKm = invoice.totalKm;
                var totalKm = invoice.totalKm;

                var contractKm = invoice.contractedKm;
                if (invoice.totalKm > invoice.contractedKm) {

                    invoice.extraKm = totalKm - contractKm;
                } else {
                    invoice.extraKm = 0;
                }

                var totalKmConversion = invoice.totalKm.toString();
                var extraKmConversion = invoice.extraKm.toString();
                var fuelExtraAmountConversion = invoice.fuelExtraAmount.toString();
                var absentDaysConversion = invoice.absentDays.toString();

                if (invoice.totalKm.toString().length > 7) {
                    totalKmConversion = totalKmConversion.substring(0, totalKmConversion.length - 1);
                    invoice.totalKm = Number(totalKmConversion);
                    ngDialog.open({
                        template: 'You cant add more than 7 digit',
                        plain: true
                    });
                } else if (invoice.extraKm.toString().length > 7) {
                    extraKmConversion = extraKmConversion.substring(0, extraKmConversion.length - 1);
                    invoice.extraKm = Number(extraKmConversion);
                    ngDialog.open({
                        template: 'You cant add more than 7 digit',
                        plain: true
                    });
                } else if (invoice.fuelExtraAmount.toString().length > 7) {
                    fuelExtraAmountConversion = fuelExtraAmountConversion.substring(0, fuelExtraAmountConversion.length - 1);
                    invoice.fuelExtraAmount = Number(fuelExtraAmountConversion);
                    ngDialog.open({
                        template: 'You cant add more than 7 digit',
                        plain: true
                    });
                } else if (invoice.absentDays.toString().length > 3) {
                    absentDaysConversion = absentDaysConversion.substring(0, absentDaysConversion.length - 1);
                    invoice.absentDays = Number(absentDaysConversion);
                    ngDialog.open({
                        template: 'You cant add more than 3 digit',
                        plain: true
                    });
                } else {
                    return true;
                }



            };

            /*Vendor Cancel Invoice*/

            $scope.cancelChangesInvoice = function(invoiceType, invoice, index, vendorFixedDistanceBasedData) {
                $scope.cloneData = {};
                var oldvalue = $rootScope.vendorFixedDistance[index];
                invoice.editByVendorInvoiceClicked = false;
                invoice.totalKm = oldvalue.totalKm;
                invoice.extraKm = oldvalue.extraKm;
                invoice.fuelExtraAmount = oldvalue.fuelExtraAmount;
                invoice.absentDays = oldvalue.absentDays;
                invoice.invoiceRemarks = oldvalue.invoiceRemarks;

                if (_.isMatch(invoice, oldvalue)) {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });
                }
            }

            /*Vehicle Cancel Invoice*/

            $scope.cancelChangesVehicle = function(vehicleType, vehicleFixedDistanceBasedData, index) {

                var oldvalue = $rootScope.vehicleInvoiceDataCopy;

                vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked = false;
                vehicleFixedDistanceBasedData.totalKm = oldvalue.totalKm;
                vehicleFixedDistanceBasedData.extraKm = oldvalue.extraKm;
                vehicleFixedDistanceBasedData.fuelExtraAmount = oldvalue.fuelExtraAmount;
                vehicleFixedDistanceBasedData.absentDays = oldvalue.absentDays;
                vehicleFixedDistanceBasedData.invoiceRemarks = oldvalue.invoiceRemarks;

                if (_.isMatch(vehicleFixedDistanceBasedData, oldvalue)) {
                    ngDialog.open({
                        template: 'No changes done',
                        plain: true
                    });
                }
            }

            // Comparing New and Old objects
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

            $scope.saveChangesInvoice = function(invoiceType, invoice, index) {
                if (invoiceType == 'byVendor') {
                    var oldvalue = $rootScope.vendorFixedDistance[index];
                    var oldvalueCopy = angular.copy($rootScope.vendorFixedDistance[index]);

                    if (invoice.absentDays == 0) {
                        invoice.absentDays = 'NO';
                    }

                    if (_.isMatch(invoice, oldvalue)) {
                        ngDialog.open({
                            template: 'No changes done',
                            plain: true
                        });
                        return false;
                    } else {
                        var compareResult = $scope.filter(oldvalue, invoice);
                        $rootScope.compareResultObject = JSON.parse(JSON.stringify(compareResult));
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/invoiceVendorAndVehicleConfirmation.jsp',
                            controller: 'invoiceVendorConfirmationCtrl',
                            size: 'lg',
                            backdrop: 'static',
                            keyboard: false,
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

                if (invoiceType == 'byVehicle') {
                    var oldvalue = $rootScope.vehicleInvoiceDataCopy;
                    var oldvalueCopy = angular.copy($rootScope.vehicleInvoiceDataCopy);

                    if (invoice.absentDays == 0) {
                        invoice.absentDays = 'NO';
                    }

                    if (_.isMatch(invoice, oldvalue)) {
                        ngDialog.open({
                            template: 'No changes done',
                            plain: true
                        });
                        return false;
                    } else {
                        var compareResult = $scope.filter(oldvalue, invoice);
                        $rootScope.compareResultObject = JSON.parse(JSON.stringify(compareResult));
                        var modalInstance = $modal.open({
                            templateUrl: 'partials/modals/invoiceVendorAndVehicleConfirmation.jsp',
                            controller: 'invoiceVendorConfirmationCtrl',
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

                    invoice.editByVendorInvoiceClicked = false;
                    if (invoiceType == 'byVendor') {
                        if (invoice.absentDays == 'NO') {
                            invoice.absentDays = 0;
                        }
                        if (userRole == 'admin') {
                            var data = {
                                sumTravelledDistance: invoice.totalKm,
                                extraDistace: invoice.extraKm,
                                noOfDays: invoice.absentDays,
                                invoiceId: invoice.invoiceId,
                                invoiceNumber: invoice.invoiceNumber,
                                invoiceRemarks: invoice.invoiceRemarks,
                                branchId: branchId,
                                fuelExtraAmount: invoice.fuelExtraAmount,
                                statusFlg: 'P',
                                userId: profileId,
                                userRole: userRole
                            };
                        } else {
                            var dataObject = $rootScope.compareResultObject;
                            var data = {
                                sumTravelledDistance: dataObject.totalKm,
                                extraDistace: dataObject.extraKm,
                                noOfDays: dataObject.absentDays,
                                invoiceId: invoice.invoiceId,
                                invoiceNumber: invoice.invoiceNumber,
                                invoiceRemarks: dataObject.invoiceRemarks,
                                branchId: branchId,
                                fuelExtraAmount: dataObject.fuelExtraAmount,
                                statusFlg: 'M',
                                userId: profileId,
                                userRole: userRole
                            };

                            if (data.sumTravelledDistance == undefined) {
                                data.sumTravelledDistance = 0;
                            }
                            if (data.extraDistace == undefined) {
                                data.extraDistace = 0;
                            }
                            if (data.noOfDays == undefined) {
                                data.noOfDays = 0;
                            }
                            if (data.invoiceId == undefined) {
                                data.invoiceId = 0;
                            }
                            if (data.invoiceNumber == undefined) {
                                data.invoiceNumber = 0;
                            }
                            if (data.invoiceRemarks == undefined) {
                                data.invoiceRemarks = 0;
                            }
                            if (data.fuelExtraAmount == undefined) {
                                data.fuelExtraAmount = 0;
                            }
                        }

                        $http.post('services/contract/modifiedInvoiceDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                            $('.loading').show();
                            if (userRole == 'admin') {
                                ngDialog.open({
                                    template: 'Vendor Details updated successfully',
                                    plain: true
                                });

                                invoice.extraKm = data.fixedDistanceBased[0].extraKm;
                                invoice.contractKM = data.fixedDistanceBased[0].contractKM;
                                invoice.extraKmcharges = data.fixedDistanceBased[0].extraKmcharges;
                                invoice.contractedKm = data.fixedDistanceBased[0].contractedKm;
                                invoice.invoiceCreationDate = data.fixedDistanceBased[0].invoiceCreationDate;
                                invoice.penalty = data.fixedDistanceBased[0].penalty;
                                invoice.totalKm = data.fixedDistanceBased[0].totalKm;
                                invoice.totalpenalityAmnt = data.fixedDistanceBased[0].totalpenalityAmnt;
                                invoice.absentDays = data.fixedDistanceBased[0].absentDays;
                                invoice.vendorName = data.fixedDistanceBased[0].vendorName;
                                invoice.totalWorkingDays = data.fixedDistanceBased[0].totalWorkingDays;
                                invoice.fixedcharges = data.fixedDistanceBased[0].fixedcharges;
                                invoice.totalAmount = data.fixedDistanceBased[0].totalAmount;
                                invoice.totalPerDayDeduction = data.fixedDistanceBased[0].totalPerDayDeduction;
                                invoice.totalAmountExtraKmCharge = data.fixedDistanceBased[0].totalAmountExtraKmCharge;
                                invoice.invoiceNumber = data.fixedDistanceBased[0].invoiceNumber;
                                invoice.invoiceType = data.fixedDistanceBased[0].invoiceType;
                                invoice.vehicleNumber = data.fixedDistanceBased[0].vehicleNumber;
                                invoice.contractAmount = data.fixedDistanceBased[0].contractAmount;
                                invoice.invoiceMonthDate = data.fixedDistanceBased[0].invoiceMonthDate;
                                invoice.invoiceRemarks = data.fixedDistanceBased[0].invoiceRemarks;
                                invoice.totalDeduction = data.fixedDistanceBased[0].totalDeduction;
                            } else {
                                invoice.approvalStatus = 'M';
                                ngDialog.open({
                                    template: 'Vendor Details updated successfully. Waiting for approval!',
                                    plain: true
                                });
                            }
                        }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });

                    }

                    if (invoiceType == 'byVehicle') {
                        invoice.editByVehicleInvoiceClicked = false;
                        if (invoice.absentDays == 'NO') {
                            invoice.absentDays = 0;
                        }
                        if (userRole == 'admin') {
                            var data = {
                                sumTravelledDistance: invoice.totalKm,
                                extraDistace: invoice.extraKm,
                                noOfDays: invoice.absentDays,
                                invoiceId: invoice.invoiceId,
                                invoiceNumber: invoice.invoiceNumber,
                                invoiceRemarks: invoice.invoiceRemarks,
                                branchId: branchId,
                                fuelExtraAmount: invoice.fuelExtraAmount,
                                statusFlg: 'P',
                                userId: profileId,
                                userRole: userRole
                            };
                        } else {
                            var dataObject = $rootScope.compareResultObject;
                            var data = {
                                sumTravelledDistance: dataObject.totalKm,
                                extraDistace: dataObject.extraKm,
                                noOfDays: dataObject.absentDays,
                                invoiceId: invoice.invoiceId,
                                invoiceNumber: invoice.invoiceNumber,
                                invoiceRemarks: dataObject.invoiceRemarks,
                                branchId: branchId,
                                fuelExtraAmount: dataObject.fuelExtraAmount,
                                statusFlg: 'M',
                                userId: profileId,
                                userRole: userRole
                            };

                            if (data.sumTravelledDistance == undefined) {
                                data.sumTravelledDistance = 0;
                            }
                            if (data.extraDistace == undefined) {
                                data.extraDistace = 0;
                            }
                            if (data.noOfDays == undefined) {
                                data.noOfDays = 0;
                            }
                            if (data.invoiceId == undefined) {
                                data.invoiceId = 0;
                            }
                            if (data.invoiceNumber == undefined) {
                                data.invoiceNumber = 0;
                            }
                            if (data.invoiceRemarks == undefined) {
                                data.invoiceRemarks = 0;
                            }
                            if (data.fuelExtraAmount == undefined) {
                                data.fuelExtraAmount = 0;
                            }

                        }


                        $http.post('services/contract/modifiedInvoiceDetails/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {
                            if (userRole == 'admin') {
                                ngDialog.open({
                                    template: 'Vehicle Details updated successfully',
                                    plain: true
                                });
                                $('.loading').show();
                                invoice.approvalStatus = 'M';
                                invoice.totalKm = data.fixedDistanceBased[0].totalKm;
                                invoice.extraKm = data.fixedDistanceBased[0].extraKm;
                                invoice.absentDays = data.fixedDistanceBased[0].absentDays;
                            } else {
                                ngDialog.open({
                                    template: 'Vehicle Details updated successfully. Waiting for approval!',
                                    plain: true
                                });
                            }
                        }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });

                    }
                }
            }

            $scope.vehicleTab = function() {
                $scope.vendors = {
                    'selectVendor': '',
                    'monthSelected': '',
                    'invoiceNumber': ''
                };
                $scope.currentTab = "By Vehicle";
            };

            $scope.getVehicles = function(vendor) {
                if (angular.isObject(vendor)) {
                    var vendorId = vendor.vendorId;
                    var data = {
                        vendorId: vendorId,
                        userId: profileId
                    };
                    $http.post('services/contract/allActiveVehicle/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.selectVehicles = data;
                    }
                    }).
                    error(function(data, status, headers, config) {
                        // log error
                    });
                }
            };

            $scope.setVehicleNumber = function(vehicle) {
                $scope.vehicleNumber = vehicle.vehicleNumber;
            };

            $scope.getInvoiceByVehicle = function(vendor, distanceFlg) {

                if (!$scope.vendors.selectVendor || !$scope.vendors.selectVehicle || !$scope.vendors.monthSelected) {

                    $('.loading').show();
                    ngDialog.open({
                        template: 'Please select Vendor, Vehicle and Date!',
                        plain: true
                    });
                } else {
                    var d = new Date(vendor.monthSelected);
                    var actionTypes = "VEHICLEBASED";
                    var vehicleId = vendor.selectVehicle.vehicleId;
                    var vendorId = vendor.selectVendor.vendorId;
                    var monthYear = convertMonthYear(d);
                    var data = {
                        invoiceDate: monthYear,
                        actionType: actionTypes,
                        vehicleId: vehicleId,
                        efmFmVendorMaster: {
                            vendorId: vendorId,
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        distanceFlg: distanceFlg.value,
                        userId: profileId,
                        invoiceAcptType: 'N'
                    };
                    $http.post('services/contract/invoiceTripDetails/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {

                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                            ngDialog.open({
                                template: 'kinldy create the Contract Details for Vehicles',
                                plain: true
                            });

                            $scope.gotVehicleData = false;
                            return false;
                        }

                        if (data.failed == 'INVALIDGENTYPE') {
                            ngDialog.open({
                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                plain: true
                            });
                            $scope.gotVehicleData = false;
                            return false;
                        }

                        if (data.failed == 'NODATA') {
                            ngDialog.open({
                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                plain: true
                            });
                            $scope.gotVehicleData = false;
                            return false;
                        }

                        if (data.failed == 'NOTFOUNDCONTRACT') {
                            ngDialog.open({
                                template: 'Contract Details not Configured for this vendor Name',
                                plain: true
                            });
                            $scope.gotVehicleData = false;
                            return false;
                        }

                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                            ngDialog.open({
                                template: 'Create the contract details and the mapped to vehicles',
                                plain: true
                            });
                            $scope.gotVehicleData = false;
                            return false;
                        }

                        if (data.failed == 'NOTGENINV') {
                            $scope.gotVehicleData = false;
                            $confirm({
                                    text: "Are you sure want to generate invoice?",
                                    title: 'Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    $scope.gotVehicleData = true;
                                    data = {
                                        invoiceDate: monthYear,
                                        actionType: actionTypes,
                                        vehicleId: vehicleId,
                                        efmFmVendorMaster: {
                                            vendorId: vendorId,
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            }
                                        },
                                        distanceFlg: distanceFlg.value,
                                        userId: profileId,
                                        invoiceAcptType: 'Y'
                                    };
                                    $http.post('services/contract/invoiceTripDetails/', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                        $scope.vehicleFixedDistanceBasedData = data.fixedDistanceBased[0];
                                        $rootScope.vehicleInvoiceDataCopy = angular.copy(data.fixedDistanceBased[0]);
                                        $scope.vehicleTripBasedFixedDetailsData = data.tripBasedFixedDetails;
                                        if (data.contractTypeCount == 1) {
                                            $scope.contractTypeValues = "contractSingle";
                                        } else {
                                            $scope.contractTypeValues = "contractMultiple";
                                        }

                                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                                            ngDialog.open({
                                                template: 'kinldy create the Contract Details for Vehicles',
                                                plain: true
                                            });

                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'INVALIDGENTYPE') {
                                            ngDialog.open({
                                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NODATA') {
                                            ngDialog.open({
                                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NOTFOUNDCONTRACT') {
                                            ngDialog.open({
                                                template: 'Contract Details not Configured for this vendor Name',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }
                                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                                            ngDialog.open({
                                                template: 'Create the contract details and the mapped to vehicles',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (Object.keys(data).length == 0) {
                                            $scope.gotVehicleData = false;
                                            $('.loading').show();
                                            ngDialog.open({
                                                template: 'No Invoice found. Please try again!',
                                                plain: true
                                            });
                                        } else {
                                            $scope.gotVehicleData = true;
                                            $scope.vendors = {
                                                'selectVendor': '',
                                                'monthSelected': '',
                                                'invoiceNumber': ''
                                            };
                                            $scope.vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked = false;
                                        }
                                    }
                                    });
                                });
                        }

                        if (data.failed == 'REMAININGVEHICLES') {
                            $scope.gotVehicleData = false;
                            $confirm({
                                    text: "Are you sure want to generate the invoice remaining vehicles?",
                                    title: 'Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    $scope.gotVehicleData = true;
                                    data = {
                                        invoiceDate: monthYear,
                                        actionType: actionTypes,
                                        vehicleId: vehicleId,
                                        efmFmVendorMaster: {
                                            vendorId: vendorId,
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            }
                                        },
                                        distanceFlg: distanceFlg.value,
                                        userId: profileId,
                                        invoiceAcptType: 'Y'
                                    };
                                    $http.post('services/contract/invoiceTripDetails/', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                        $scope.vehicleFixedDistanceBasedData = data.fixedDistanceBased[0];
                                        $rootScope.vehicleInvoiceDataCopy = angular.copy(data.fixedDistanceBased[0]);
                                        $scope.vehicleTripBasedFixedDetailsData = data.tripBasedFixedDetails;
                                        if (data.contractTypeCount == 1) {
                                            $scope.contractTypeValues = "contractSingle";
                                        } else {
                                            $scope.contractTypeValues = "contractMultiple";
                                        }

                                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                                            ngDialog.open({
                                                template: 'kinldy create the Contract Details for Vehicles',
                                                plain: true
                                            });

                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                                            ngDialog.open({
                                                template: 'Create the contract details and the mapped to vehicles',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'INVALIDGENTYPE') {
                                            ngDialog.open({
                                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NODATA') {
                                            ngDialog.open({
                                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NOTFOUNDCONTRACT') {
                                            ngDialog.open({
                                                template: 'Contract Details not Configured for this vendor Name',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }
                                        if (Object.keys(data).length == 0) {
                                            $scope.gotVehicleData = false;
                                            $('.loading').show();
                                            ngDialog.open({
                                                template: 'No Invoice found. Please try again!',
                                                plain: true
                                            });
                                        } else {
                                            $scope.gotVehicleData = true;
                                            $scope.vendors = {
                                                'selectVendor': '',
                                                'monthSelected': '',
                                                'invoiceNumber': ''
                                            };
                                            $scope.vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked = false;
                                        }
                                    }
                                });
                                });
                        }

                        if (data.failed == 'ALREADYGENINV') {
                            $scope.gotVehicleData = false;
                            $confirm({
                                    text: "Invoice already generated If you want view the details?",
                                    title: 'Confirmation',
                                    ok: 'Yes',
                                    cancel: 'No'
                                })
                                .then(function() {
                                    data = {
                                        invoiceDate: monthYear,
                                        actionType: actionTypes,
                                        vehicleId: vehicleId,
                                        efmFmVendorMaster: {
                                            vendorId: vendorId,
                                            eFmFmClientBranchPO: {
                                                branchId: branchId
                                            }
                                        },
                                        distanceFlg: distanceFlg.value,
                                        userId: profileId,
                                        invoiceAcptType: 'Y'
                                    };
                                    $scope.gotVehicleData = true;
                                    $http.post('services/contract/invoiceTripDetails/', data).
                                    success(function(data, status, headers, config) {
                                        if (data.status != "invalidRequest") {
                                        $scope.vehicleFixedDistanceBasedData = data.fixedDistanceBased[0];
                                        $scope.vehicleTripBasedFixedDetailsData = data.tripBasedFixedDetails;
                                        $rootScope.vehicleInvoiceDataCopy = angular.copy(data.fixedDistanceBased[0]);

                                        if (data.contractTypeCount == 1) {
                                            $scope.contractTypeValues = "contractSingle";
                                        } else {
                                            $scope.contractTypeValues = "contractMultiple";
                                        }

                                        if (data.failed == 'kinldy create the Contract Details for Vehicles') {
                                            ngDialog.open({
                                                template: 'kinldy create the Contract Details for Vehicles',
                                                plain: true
                                            });

                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'Create the contract details and the mapped to vehicles') {
                                            ngDialog.open({
                                                template: 'Create the contract details and the mapped to vehicles',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'INVALIDGENTYPE') {
                                            ngDialog.open({
                                                template: 'Invoice generation failed. Kindly configure Inovoice Generation type at Appllication setting.',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NODATA') {
                                            ngDialog.open({
                                                template: 'Invoice Details not available for this vehicles, Kindly configure contract details',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (data.failed == 'NOTFOUNDCONTRACT') {
                                            ngDialog.open({
                                                template: 'Contract Details not Configured for this vendor Name',
                                                plain: true
                                            });
                                            $scope.gotVehicleData = false;
                                            return false;
                                        }

                                        if (Object.keys(data).length == 0) {
                                            $scope.gotVehicleData = false;
                                            $('.loading').show();
                                            ngDialog.open({
                                                template: 'No Invoice found. Please try again!',
                                                plain: true
                                            });
                                        } else {
                                            $scope.gotVehicleData = true;
                                            $scope.vendors = {
                                                'selectVendor': '',
                                                'monthSelected': '',
                                                'invoiceNumber': ''
                                            };
                                            $scope.vehicleFixedDistanceBasedData.editByVehicleInvoiceClicked = false;
                                        }
                                    }
                                     });

                                }, function() {
                                    return false;
                                });
                        }
                    }
                    }).
                    error(function(data, status, headers, config) {
                        $scope.gotVehicleData = false;
                    });

                }
            };



            $scope.getContractDetail = function() {
                $scope.gotContractDetailsResults = false;
                $scope.contractDetailData = [];
                var data = {
                    branchId: branchId,
                    userId: profileId
                };
                $http.post('services/contract/activeContractDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    angular.forEach(data, function(item) {
                        $scope.contractDetailData.push({
                            'contractTitle': item.contractTitle,
                            'contractType': item.contractType,
                            'contractId': item.contractId,
                            'startDate': new Date(dateFormatConverter(item.startDate)),
                            'endDate': new Date(dateFormatConverter(item.endDate)),
                            'extraDistanceChargeRate': item.extraDistanceChargeRate,
                            'fixedDistanceChargeRate': item.fixedDistanceChargeRate,
                            'contractDistance': item.contractDistance,
                            'fixedDistancePerDay': item.fixedDistancePerDay,
                            'minimumDays': item.minimumDays,
                            'perDayCost': item.perDayCost,
                            'petrolPrice': item.petrolPrice,
                            'panalityPercentage': item.panalityPercentage,
                            'penalty': item.penaltyFlg,
                            'contractDescription': item.fuelContractDesc,
                            'fuelId': item.fuelContractDesc,
                            'perKmCost': item.perKmCost

                        });
                    });

                    if ($scope.contractDetailData.length > 0) {
                        $scope.gotContractDetailsResults = true;
                    } else {
                        $scope.gotContractDetailsResults = false;
                    }
                }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.addContractDetail = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/invoiceContractDetailForm.jsp',
                    controller: 'addContractDetailCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {}
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {

                    $scope.contractDetailData.push({
                        "contractDate": $scope.convertDateUTC(new Date()),
                        "contractTitle": result.contractTitle,
                        "contractType": result.contractType.contractType,
                        "startDate": $scope.convertDateUTC(result.startDate),
                        "endDate": $scope.convertDateUTC(result.endDate),
                        "extraDistanceChargeRate": result.extraDistanceChargeRate,
                        "serviceTax": 12,
                        "minimumDays": result.minimumDays,
                        "fixedDistancePerDay": result.fixedDistanceperDay,
                        "panalityPercentage": result.penaltyPercent,
                        "contractDistance": result.fixedDistanceChargeMonthly,
                        "perDayCost": result.perDayCost,
                        "petrolPrice": result.petrolPrice,
                        "penalty": result.penalty.name,
                        "contractDescription": result.contractDescription.contractDescription,
                        "fixedDistanceChargeRate": result.fixedDistanceChargeRate,
                        "fuelId": result.fuelId
                    });
                });

            };

            $scope.addFuelDetail = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'partials/modals/addFuelPriceDetail.jsp',
                    controller: 'addFuelDetailCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {}
                });

                //Here the VendorId will be given to the Vendor from backend
                modalInstance.result.then(function(result) {

                    if (result.monthAmount == undefined) {
                        $scope.monthAmount = 0;
                    } else {
                        $scope.monthAmount = result.monthAmount;
                    }
                    if (result.contractType == 1) {
                        result.contractType = "K M Based"
                    } else {
                        result.contractType = "Month Based"
                    }

                    $scope.contractFuelDetailData.push({
                        "contactTitle": result.contactTitle,
                        "selectDate": convertDateUTC(result.selectDate),
                        "contractType": result.contractType,
                        "newPrice": result.newPrice,
                        "fuelType": result.fuelType,
                        "monthAmount": $scope.monthAmount
                    });
                });
            };

            $scope.formats = ['yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-mm-yyyy', 'mm-yyyy'];
            $scope.monthformat = 'MM-yyyy';

            $scope.datepickerOptions = {
                datepickerMode: "'month'",
                minMode: "month",
                minDate: "minDate",
                showWeeks: "false"
            };

            $scope.selectMonthCal = function($event) {
                $scope.invoiceByVendorData = {};
                $scope.invoiceByVendorData.invoiceNumber = '';
                $event.preventDefault();
                $event.stopPropagation();
                $timeout(function() {
                    $scope.datePicker = {
                        'selectMonth': true
                    };
                }, 50);
            };

            $scope.invoiceNumberChange = function() {

                $scope.vendors.monthSelected = '';
            }

            $scope.viewSummary = function() {
                $scope.viewSummaryClicked = true;
            };

            $scope.backToDetails = function() {
                $scope.viewSummaryClicked = false;
            };

            $scope.downloadExcel = function(selected, vendor, invoiceByVendorData) {

                if (selected == "byVendor") {
                    var actionTypes = "INVOICEDETAILS";
                    var vendorId = vendor.selectVendor.vendorId;
                    var d = new Date(vendor.monthSelected);
                    $scope.monthYear = convertMonthYear(d);
                    var dataObj = {
                        invoiceDate: $scope.monthYear,
                        actionType: actionTypes,
                        invoiceNumber: invoiceByVendorData.invoiceNumber,
                        efmFmVendorMaster: {
                            vendorId: vendorId,
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        },
                        userId: profileId,
                        contractType: invoiceByVendorData.contractType,
                        contractTypeCount: invoiceByVendorData.contractTypeCount
                    };

                    $http({
                        url: 'services/contract/invoiceDownload/',
                        method: "POST",
                        data: dataObj,
                        headers: {
                            'Content-type': 'application/json'
                        },
                        responseType: 'arraybuffer'
                    }).success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        var blob = new Blob([data], {});

                        saveAs(blob, 'Invoice By Vendor' + '.xlsx');
                    }
                    }).error(function(data, status, headers, config) {
                        alert("Download Failed")
                    });


                }

                if (selected == "byVehicle") {

                    var actionTypes = "VENDORBASED";
                    var vendorId = vendor.selectVendor.vendorId;
                    var d = new Date(vendor.monthSelected);
                    $scope.monthYear = convertMonthYear(d);
                    var dataObj = {
                        invoiceDate: $scope.monthYear,
                        actionType: actionTypes,
                        efmFmVendorMaster: {
                            vendorId: vendorId,
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            }
                        }
                    };


                    $http({
                        url: 'services/contract/invoiceTripDetails/',
                        method: "POST",
                        data: dataObj,
                        headers: {
                            'Content-type': 'application/json'
                        },
                        responseType: 'arraybuffer'
                    }).success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        var blob = new Blob([data], {});

                        saveAs(blob, 'Invoice By Vehicle' + '.xlsx');
                    }
                    }).error(function(data, status, headers, config) {
                        alert("Download Failed")
                    });


                }
            };

            $scope.expense = {};
            $scope.vSelectionsData = [];

            $scope.expenseReport = function() {
                $scope.currentTab = "Expense Report"
                $scope.expense.criteria = "By Vendor";
                //Set today's, tomorrow's date
                var todaysDate = new Date();
                var tomorrowDate = new Date();
                tomorrowDate.setDate(tomorrowDate.getDate() + 1);
                $scope.dates[0].value = todaysDate;
                $scope.fromDate = todaysDate;
                $scope.toDate = tomorrowDate;

                $scope.getVendorOrVehicle($scope.expense.criteria);

            };

            $scope.getVendorOrVehicle = function(criteriaType) {
                $scope.vSelections = [];
                $scope.vSelectionsData = [];
                if (criteriaType == "By Vendor") {
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: branchId
                    };
                    $http.post('services/contract/allActiveVendor/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vSelections = data;
                        angular.forEach($scope.vSelections, function(item) {
                            $scope.vSelectionsData.push({
                                name: item.name,
                                id: item.vendorId
                            });
                        });
                        $scope.vSelectionsData.unshift({
                            name: "ALL Vendors",
                            id: 0
                        });
                    }
                    }).
                    error(function(data, status, headers, config) {});
                }


                if (criteriaType == "By Vehicle") {
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: branchId
                    };
                    $http.post('services/contract/allActiveVendor/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vSelections = data;
                        angular.forEach($scope.vSelections, function(item) {
                            $scope.vSelectionsData.push({
                                name: item.name,
                                id: item.vendorId
                            });
                        });
                        $scope.vSelectionsData.unshift({
                            name: "ALL Vehicles",
                            id: 0
                        });
                    }
                     }).
                    error(function(data, status, headers, config) {});
                }

                if (criteriaType == "Fuel Price") {
                    var data = {
                        branchId: branchId,
                        userId: profileId,
                        combinedFacility: branchId
                    };
                    $http.post('services/contract/allActiveVendor/', data).
                    success(function(data, status, headers, config) {
                        if (data.status != "invalidRequest") {
                        $scope.vSelections = data;
                        angular.forEach($scope.vSelections, function(item) {
                            $scope.vSelectionsData.push({
                                name: item.name,
                                id: item.vendorId
                            });
                        });
                        $scope.vSelectionsData.unshift({
                            name: "ALL Vehicles",
                            id: 0
                        });
                    }
                    }).
                    error(function(data, status, headers, config) {});
                }
            };


            // Vehicle Wise Contracted KM
            $scope.getVehicleWiseContract = function() {



                commonApiService.post('/contract/allEnanbleVehicles', data).then(function(data, status, headers, config) {
                    $scope.vehicleNumberData = data;
                    $scope.vehicleNumberDataLength = data.length;
                }, function(data, status, headers, config) {

                    if ($rootScope.extractText(data) == '404') {
                        ngDialog.open({
                            template: '404 Url not found, Kindly check URL',
                            plain: true
                        });
                    } else if ($rootScope.extractText(data) == '500') {
                        ngDialog.open({
                            template: '500 internal server error',
                            plain: true
                        });
                    } else {
                        ngDialog.open({
                            template: 'Unrecognized field' + ' ' + $rootScope.extractText(data),
                            plain: true
                        });
                    }

                });

            }

            $scope.editVehicleContract = function(vehicleNumber, $index) {
                $scope.visibleEditVehicle = false;

                var vehicleNumberOld = vehicleNumber;
                return vehicleNumberOld;
            }

            $scope.saveVehicleContract = function(vehicleNumber, $index, vehicleNumberOld) {

                $confirm({
                        text: "Are you sure you want to save the changes?",
                        title: 'Confirmation',
                        ok: 'Yes',
                        cancel: 'No'
                    })
                    .then(function() {

                        var data = {
                            eFmFmClientBranchPO: {
                                branchId: branchId
                            },
                            vehicleNumber: vehicleNumber.invoiceNumber,
                            userId: profileId,
                            userRole: userRole
                        };

                        $http.post('services/contract/test/', data).
                        success(function(data, status, headers, config) {
                            if (data.status != "invalidRequest") {

                            $('.loading').show();
                            ngDialog.open({
                                template: 'Vehicle Wise Contracted KM updated successfully',
                                plain: true
                            });
                        }
                        }).
                        error(function(data, status, headers, config) {
                            // log error
                        });

                    });

                $scope.visibleEditVehicle = true;
            }

            //Function to check the Date Validity
            $scope.checkDateRangeValidityStart = function(fromDate, toDate, dateValidation) {
                var startDateValidation = angular.copy(dateValidation);
                startDateValidation.setDate(startDateValidation.getDate() + 1);
                var firstDay = new Date(startDateValidation.getFullYear(), startDateValidation.getMonth(), 1);
                firstDay.setDate(firstDay.getDate() + $rootScope.invoiceGenDate - 1);

                if (startDateValidation <= firstDay) {
                    var endDateDisabled = firstDay;
                    endDateDisabled.setDate(endDateDisabled.getDate() - 1);
                    $scope.endDateDisabled = endDateDisabled;
                } else {
                    var lastDay = new Date(startDateValidation.getFullYear(), startDateValidation.getMonth() + 1, 0);
                    lastDay.setDate(lastDay.getDate() + $rootScope.invoiceGenDate);
                    $scope.endDateDisabled = lastDay;
                    var currentDateval = new Date();
                    if ($scope.endDateDisabled <= currentDateval) {
                        var lastDay = new Date(startDateValidation.getFullYear(), startDateValidation.getMonth() + 1, 0);
                        lastDay.setDate(lastDay.getDate() + $rootScope.invoiceGenDate - 1);
                        $scope.endDateDisabled = lastDay;
                    } else {
                        $scope.endDateDisabled = new Date();
                    }
                }

                var currentDate = startDateValidation;
                var lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
                lastDay.setDate(lastDay.getDate() + $rootScope.invoiceGenDate - 1);
                $scope.maxdatePartial = lastDay;
                $scope.invoiceGenDateFunction();
                var invoiceGenDate = $rootScope.invoiceGenDate;
                var dateCount = dateValidation.getDate();
                var lastDay = new Date(dateValidation.getFullYear(), dateValidation.getMonth() + 1, 0);
                var LastdateCount = lastDay.getDate();
                var dateGen = LastdateCount + invoiceGenDate - 1;

                if (fromDate > toDate) {
                    $scope.dateRangeError = true;
                    $timeout(function() {
                        $scope.dateRangeError = false;
                    }, 3000);
                    return false;
                } else return true;
            };



            $scope.getReport = function(fromDate, toDate) {
                $scope.expenseReporttButtonClicked = true;
                selectedFrom = fromDate;
                selectedTo = toDate;
                $scope.efmfilter = {};
                $('.popover').hide();

                //Call the function to Check the Date range Validity before any other action
                if ($scope.checkDateRangeValidity(new Date(fromDate).getTime(), new Date(toDate).getTime())) {
                    if (angular.isObject($scope.expense.vSelection)) {
                        if ($scope.currentTab = "Expense Report") {
                            $scope.gotExpenseResult = false;
                            var actionTypes = "VENDORBASED";
                            var data = {
                                actionType: actionTypes,
                                efmFmVendorMaster: {
                                    eFmFmClientBranchPO: {
                                        branchId: branchId
                                    }
                                },
                                fromDate: $scope.convertDateUTC(fromDate),
                                toDate: $scope.convertDateUTC(toDate),
                                userId: profileId
                            };
                            $http.post('services/contract/operatingReports/', data).
                            success(function(data, status, headers, config) {
                                if (data.status != "invalidRequest") {
                                $scope.expenseVendorName = $scope.expense.vSelection.name;
                                $scope.expenseVendorNameFromDate = $scope.convertDateUTC(fromDate);
                                $scope.expenseVendorNameToDate = $scope.convertDateUTC(toDate);
                                $scope.expenseData = data;

                                if ($scope.expenseData.length > 0) {
                                    $scope.expenseReporttButtonClicked = false;
                                    $scope.gotExpenseResult = true;
                                } else {
                                    $scope.gotExpenseResult = false;
                                    $('.loading').show();
                                    ngDialog.open({
                                        template: 'No Data found. Please change your selection and try again.',
                                        plain: true
                                    });

                                }
                                $scope.expenseReporttButtonClicked = false;
                            }
                            }).
                            error(function(data, status, headers, config) {
                                $scope.expenseReporttButtonClicked = false;
                            });
                        }
                    } else {
                        ngDialog.open({
                            template: 'Please select a valid Vendor/Vehicle from the dropdown',
                            plain: true
                        });

                        $scope.expenseReporttButtonClicked = false;
                    }

                }
            }

            // Fuel Price Tab 


            // Get Fuel Detail

            $scope.getFuelDetail = function() {
                $scope.gotFuelDetailsResults = false;
                $scope.contractFuelDetailData = [];

                var data = {
                    eFmFmClientBranchPO: {
                        branchId: branchId 
                    },
                    userId: profileId
                };

                $http.post('services/contract/getFuelDetails/', data).
                success(function(data, status, headers, config) {
                    if (data.status != "invalidRequest") {
                    $scope.gotFuelDetailsResults = true;
                    angular.forEach(data, function(item) {

                        $scope.contractFuelDetailData.push({
                            'contactTitle': item.contractTitle,
                            'selectDate': item.startDate,
                            'contractType': item.fuelContractDesc,
                            'newPrice': item.newPrice,
                            'oldPrice': item.OldPrice,
                            'monthAmount': item.perMonthAmount,
                            'fuelType': item.fuelType


                        });
                    });

                }
                }).
                error(function(data, status, headers, config) {
                    // log error
                });
            };

            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            };


        }
    };


    var addContractDetailCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $timeout) {
        $scope.IntegerNumber = /^\d+$/;
        $scope.contract = [];
        $scope.allPenalty = [{
            name: "NO",
            Id: 0
        }, {
            name: "YES",
            Id: 1
        }];
        $scope.contract = {};
        var contractFuelType;

        $scope.contract.penalty = $scope.allPenalty[0];
        $scope.distanceChargeRate = 'Fixed Distance Charge Rate';
        $scope.extraChargeRate = 'Extra Distance Charge Rate';
        $scope.perKmChargeDisabled = true;
        $scope.perDayCostDisabled = true;
        $scope.fixedDistanceDisabled = true;
        $scope.dateOptions = {
            'year-format': "'yy'",
            'starting-day': 1
        };


        //Show Custome Alert Messages
        $scope.showalertMessageModal = function(message, hint) {
            $scope.alertMessage = message;
            $scope.alertHint = hint;
            $('.loading').show();
            $('.alert_Modal').show('slow', function() {
                $timeout(function() {
                    $('.alert_Modal').fadeOut('slow');
                    $('.loading').hide();
                }, 3000);
            });
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


        //BUTTON ACTION :: CLOSE ALERT
        $scope.closeAlertMessageModal = function() {
            $('.loading').hide();
            $('.alert_Modal').hide('slow');
        };

        $scope.setPenalty = function(contract) {
            if (contract.penalty.name == 'NO') {
                $scope.contract.penaltyPercent = "";
            }
        };

        /*Contract Type - FDC*/

        //Per KM Cost = Fixed Distance Charge Rate / Fixed Distance Charge Monthly

        $scope.setFixedDistanceMonthly = function(contract) {
            contract.perKmCost = contract.fixedDistanceChargeRate / contract.fixedDistanceChargeMonthly;
            contract.perDayCost = contract.fixedDistanceChargeRate / contract.minimumDays;
        }

        $scope.setFixedDistanceChangeRate = function(contract) {
            contract.perKmCost = contract.fixedDistanceChargeRate / contract.fixedDistanceChargeMonthly;
            contract.perDayCost = contract.fixedDistanceChargeRate / contract.minimumDays;
        }

        //Per Day Cost = Fixed Distance Charge Rate / Fixed Distance Charge Monthly

        $scope.setFixedMinimumDays = function(contract) {
            contract.perDayCost = contract.fixedDistanceChargeRate / contract.minimumDays;
        }

        /*Contract Type - PDDC*/

        $scope.setDayCostPDDC = function(contract) {
            contract.fixedDistanceChargeRate = contract.perDayCost * contract.minimumDays;
        }

        $scope.setMinimumDays = function(contract) {
            contract.fixedDistanceChargeRate = contract.perDayCost * contract.minimumDays;
        }

        // Add New Contract Details - Date Picker

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

        $scope.setContractType = function(value) {
            if (value.contractType == 'FDC') {
                $scope.contractDisabledFDC = true;
                $scope.contract.minimumDays = {};
                $scope.contract.perDayCost = {};
                $scope.contract.perKmCost = {};
                $scope.contract.fixedDistanceChargeRate = {};
                $scope.contract.petrolPrice = {};
                $scope.contract.fixedDistanceChargeMonthly = {};
                $scope.contract.extraDistanceChargeRate = {};

            } else {
                $scope.contractDisabledFDC = false;
                $scope.contract.minimumDays = {};
                $scope.contract.perDayCost = {};
                $scope.contract.perKmCost = {};
                $scope.contract.fixedDistanceChargeRate = {};
                $scope.contract.petrolPrice = {};
            }
        }


        $scope.addContract = function(contract, fixedDistanceChargeMonthly) {

            if (contract.penalty.name == 'NO') {
                $scope.contract.penaltyPercent = 0;
            }

            if (contract.contractDescription.contractFuelType == 'NR') {
                contractFuelType = 'NR';
            } else {
                contractFuelType = 'Y';
            }


            // Start Date Calculation
            var myStartDate = new Date($scope.contract.startDate);
            var month = myStartDate.getMonth() + 1;
            var startDate = myStartDate.getFullYear() + "-" + month + "-" + myStartDate.getDate();

            // End Date Calculation
            var myEndDate = new Date($scope.contract.endDate);
            var month = myEndDate.getMonth() + 1;
            var endDate = myEndDate.getFullYear() + "-" + month + "-" + myEndDate.getDate();

            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                userId: profileId,
                contractTitle: contract.contractTitle,
                startDate: startDate,
                endDate: endDate,
                eFmFmVendorContractTypeMaster: {
                    contractTypeId: parseInt(contract.contractType.contractId)
                },
                fixedDistanceMonthly: contract.fixedDistanceChargeMonthly,
                fixedDistanceChargeRate: contract.fixedDistanceChargeRate,
                minimumDays: contract.minimumDays,
                extraDistanceChargeRate: contract.extraDistanceChargeRate,
                petrolPrice: contract.petrolPrice,
                perDayCost: contract.perDayCost,
                perKmCost: contract.perKmCost,
                penalty: contract.penalty.name,
                penaltyInPercentagePerDay: contract.penaltyPercent,
                eFmFmVendorFuelContractTypeMaster: {
                    fuelTypeId: contract.contractDescription.fuelContractId
                },
                fixedDistancePrDay: 0,
                fuelPriceCalculation: contractFuelType,
            };

            $http.post('services/contract/addContractDetails/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                $scope.contractData = data;
                $('.loading').show();
                ngDialog.open({
                    template: 'The Contract has been added successfully',
                    plain: true
                });
                $timeout(function() {
                    $modalInstance.close(contract)
                }, 3000);
            }
            }).
            error(function(data, status, headers, config) {
                // log error
            });

        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        /*List Of Contract Type*/

        var data = {
            branchId: branchId,
            userId: profileId
        };
        $http.post('services/contract/listOfContractType/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
            $scope.contractData = data;
            $scope.contract.contractType = $scope.contractData[0];
        }
        }).
        error(function(data, status, headers, config) {
            // log error
        });


        /*List Of Fuel Type*/

        var data = {
            branchId: branchId,
            userId: profileId
        };
        $http.post('services/contract/listOfFuelContractType/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
            $scope.fuelContractType = data;
            $scope.fuelContractType
                .unshift({
                    contractDescription: "Not Required",
                    contractFuelType: "NR",
                    fuelContractId: 3
                });
            }
        }).
        error(function(data, status, headers, config) {
            // log error
        });
    };


    var addFuelDetailCtrl = function($scope, $modalInstance, $state, $http, $timeout, ngDialog, $timeout) {

        $scope.fuel = [];
        $scope.dateOptions = {
            'year-format': "'yy'",
            'starting-day': 1
        };

        //Show Custome Alert Messages
        $scope.showalertMessageModal = function(message, hint) {
            $scope.alertMessage = message;
            $scope.alertHint = hint;
            $('.loading').show();
            $('.alert_Modal').show('slow', function() {
                $timeout(function() {
                    $('.alert_Modal').fadeOut('slow');
                    $('.loading').hide();
                }, 3000);
            });
        };

        $scope.startDateFuel = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $timeout(function() {
                $scope.datePicker = {
                    'startDate': true
                };
            }, 50);
        };

        var data = {
            branchId: branchId,
            userId: profileId
        };
        $http.post('services/contract/listOfFuelContractType/', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
            $scope.fuelContractType = data;
        }
        }).
        error(function(data, status, headers, config) {
            // log error
        });



        $scope.addFuel = function(fuel) {

            if ($scope.fuel.fuelType == 'K') {
                $scope.fuel.monthAmount = 0;
            }

            /*Start Date Calculation*/

            var mySelectDate = new Date($scope.fuel.selectDate);
            var month = mySelectDate.getMonth() + 1;
            var selectDate = mySelectDate.getFullYear() + "-" + month + "-" + mySelectDate.getDate();

            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                contractTitle: fuel.contactTitle,
                startDate: selectDate,
                eFmFmVendorFuelContractTypeMaster: {
                    fuelTypeId: fuel.contractType
                },
                newPrice: fuel.newPrice,
                oldPrice: fuel.newPrice,
                perMonthAmount: fuel.monthAmount,
                fuelType: fuel.fuelType,
                userId: profileId
            };

            $http.post('services/contract/addFuelDetails/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                $scope.contractData = data;
                $('.loading').show();
                ngDialog.open({
                    template: 'The Fuel Price has been added successfully',
                    plain: true
                });

                $timeout(function() {
                    $modalInstance.close(fuel)
                }, 3000);
            }
            }).
            error(function(data, status, headers, config) {
                // log error
            });



        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };


    var byVehicleInvoiceCtrl = function($scope, $modalInstance, $state, $http, $timeout) {

    };

    var viewNoOfAbsentCtrl = function($scope, $modal, $modalInstance, $state, $http, $timeout, data, partialInvoice) {

        var data = {
            efmFmVendorMaster: data.efmFmVendorMaster,
            vehicleId: partialInvoice.vehicleId,
            fromDate: data.fromDate,
            toDate: data.toDate,
            userId: profileId
        };


        $http.post('services/contract/viewDaysDetails', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
            $scope.noOfdaysAbsentDetail = data;
            $scope.absentDetailsLength = data.length;
        }
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        $scope.viewTravelledRoute = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'partials/modals/viewTravelledRoute.jsp',
                controller: 'travelledRouteDistanceCtrl',
                backdrop: 'static',
                size: size,

            });
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    };

    var totalKmInvoiceCtrl = function($scope, $modalInstance, $state, $http, $timeout, data, partialInvoice, distanceArray) {

        $scope.distanceArray = distanceArray;
        var data = {
            efmFmVendorMaster: data.efmFmVendorMaster,
            vehicleId: partialInvoice.vehicleId,
            fromDate: data.fromDate,
            toDate: data.toDate,
            userId: profileId
        };


        $http.post('services/contract/viewVehicleDistance', data).
        success(function(data, status, headers, config) {
            if (data.status != "invalidRequest") {
            $scope.vehicleInvoiceData = data;
            $scope.distanceFlg = data[0].distanceFlg;

            $scope.totalKmDataLength = data.length;
        }           
        }).
        error(function(data, status, headers, config) {
            // log error
        });

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var invoiceVendorConfirmationCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, compareResult, oldvalue) {
        $scope.totalKmOld = oldvalue.totalKm;
        $scope.extraKmOld = oldvalue.extraKm;
        $scope.fuelExtraAmountOld = oldvalue.fuelExtraAmount;
        $scope.absentDaysOld = oldvalue.absentDays;
        $scope.invoiceRemarksOld = oldvalue.invoiceRemarks;

        $scope.totalKmUpdated = compareResult.totalKm;
        $scope.extraKmUpdated = compareResult.extraKm;
        $scope.fuelExtraAmountUpdated = compareResult.fuelExtraAmount;
        $scope.absentDaysUpdated = compareResult.absentDays;
        $scope.invoiceRemarksUpdated = compareResult.invoiceRemarks;

        $scope.confirmModifiedInvoice = function() {
            $rootScope.saveInvoiceVendorVehicleDetails();
            $modalInstance.close(compareResult);
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var addContactTypeCtrl = function($scope, ngDialog, $rootScope, $modalInstance, $state, $http, $timeout) {

        $scope.addContractType = function(contract) {
            var data = {
                eFmFmClientBranchPO: {
                    branchId: branchId
                },
                contractType: contract.contractType,
                contractDescription: contract.contractDescription,
                serviceTax: contract.serviceTax,
                userId: profileId
            };
            $http.post('services/contract/addContractType/', data).
            success(function(data, status, headers, config) {
                if (data.status != "invalidRequest") {
                if (data.status == "alreadyExist") {
                    ngDialog.open({
                        template: 'Contract Type already exists',
                        plain: true
                    });
                    return false;
                } else {
                    ngDialog.open({
                        template: 'Contract Type added successfully',
                        plain: true
                    });
                }

                $timeout(function() {
                    $modalInstance.close(contract)
                }, 3000);
            }
            }).
            error(function(data, status, headers, config) {
                // log error
            });
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    var invoiceContractTypeConfirmCtrl = function($scope, $rootScope, $modalInstance, $state, $http, $timeout, compareResult, contractTypeData) {

        // Exist Values Contract

        $scope.contractTypeOld = contractTypeData.contractType;
        $scope.contractDescriptionOld = contractTypeData.contractDescription;
        $scope.serviceTaxOld = contractTypeData.serviceTax;

        // New Values Contract

        $scope.contractTypeUpdated = compareResult.contractType;
        $scope.contractDescriptionUpdated = compareResult.contractDescription;
        $scope.serviceTaxUpdated = compareResult.serviceTax;

        $scope.confirmModifiedContractType = function() {
            $rootScope.updateContractType();
            $modalInstance.close(compareResult);
        }

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    };

    angular.module('efmfmApp').controller('invoiceCtrl', invoiceCtrl);
    angular.module('efmfmApp').controller('addContractDetailCtrl', addContractDetailCtrl);
    angular.module('efmfmApp').controller('byVehicleInvoiceCtrl', byVehicleInvoiceCtrl);
    angular.module('efmfmApp').controller('addFuelDetailCtrl', addFuelDetailCtrl);
    angular.module('efmfmApp').controller('viewNoOfAbsentCtrl', viewNoOfAbsentCtrl);
    angular.module('efmfmApp').controller('totalKmInvoiceCtrl', totalKmInvoiceCtrl);
    angular.module('efmfmApp').controller('invoiceVendorConfirmationCtrl', invoiceVendorConfirmationCtrl);
    angular.module('efmfmApp').controller('addContactTypeCtrl', addContactTypeCtrl);
    angular.module('efmfmApp').controller('invoiceContractTypeConfirmCtrl', invoiceContractTypeConfirmCtrl);


}());


/* ************************************** Invoice.js BluePrint :************************************** */

/* Controllers :
    - invoiceCtrl
    - addContractDetailCtrl
    - byVehicleInvoiceCtrl

                  ************** invoiceCtrl ************       

              Function:                         Services:

          - getVendorOrVehicle()        ->    contract/allActiveVendor 
          - getInvoiceNumbers()         ->    contract/listOfInvoiceDetails
          - getInvoiceByVendor()        ->    contract/invoiceTripDetails
          - saveChangesContractDetail() ->    contract/updateFDCDetails
          - saveChangesInvoice()        ->    contract/modifiedInvoiceDetails
          - getVehicles()               ->    contract/allActiveVehicle
          - getInvoiceByVehicle()       ->    contract/invoiceTripDetails

                ********* addContractDetailCtrl **********

              Function:                         Services:

          - getContractDetail()         ->    contract/activeContractDetails
          - getVendorOrVehicle()        ->    contract/allActiveVendor
          - getReport()                 ->    contract/expenseReport
          - addContract()               ->    contract/addContractDetails
          - addContract ()            ->    contract/listOfContractType
    
*/

/* **************************************************************************************************** */