/*
@date                   04/01/2015
@Author                 Saima Aziz
@Description    
@Main Controllers       sosModalCtrl
@Modal Controllers      sosModalInstanceCtrl
@template               partials/dashboard.jsp

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
04/01/2015  Kathiravan     Initial Creation
04/15/2016  Kathiravan      Final Creation
*/
(function() {
    var excelUploadCtrl = function($scope, $modal, $log, $http, ngDialog, $window, $timeout, $ocLazyLoad) {

        $scope.uploadTypeHeader = 'Import Employee Data';
        $scope.importDataType = 'employeeData';
        $scope.actionTypeUrl = 'services/xlUtilityEmployeeUpload/employeeRecord';
        localStorage.setItem("tabNameExcel", "excelDownload");
        $scope.uploadButtonDisabled = false;
        $scope.spinnerShow = false;
        $scope.UploadButtonText = "Upload";

        $scope.uploadDependencyLoad = function() {
            $ocLazyLoad.load([{
                files: ['bower_components/js-xlsx-master/shim.js'],
                cache: true
            }, {
                files: ['bower_components/js-xlsx-master/jszip.js'],
                cache: true
            }, {
                files: ['bower_components/js-xlsx-master/xlsx.js'],
                cache: true
            }, {
                files: ['bower_components/js-xlsx-master/ods.js'],
                cache: true
            }, {
                files: ['bower_components/js-xlsx-master/fileValidation.js'],
                cache: true
            }]);
        }

        $scope.uploadDependencyLoad();

        $scope.setimportDataType = function(uploadType) {
            $scope.uploadDependencyLoad();
            switch (uploadType) {
                case 'employeeData':
                    $scope.uploadTypeHeader = 'Import Employee Data';;
                    $scope.actionTypeUrl = 'services/xlUtilityEmployeeUpload/employeeRecord';
                    break;
                case 'employeeRequest':
                    $scope.uploadTypeHeader = 'Import Employee Request';
                    if (branchCode == 'GNPTJP') {
                        $scope.actionTypeUrl = 'services/xlUtilityEmployeeUpload/employeeTravelTrip';
                    } else {
                        $scope.actionTypeUrl = 'services/xlUtilityEmployeeUpload/employeeTravelTripRequest';
                    }
                    break;
                case 'vendorData':
                    $scope.uploadTypeHeader = 'Import Vendor Data';
                    $scope.actionTypeUrl = 'services/xlUtilityVendorUpload/vendorRecord';
                    break;
                case 'driverData':
                    $scope.uploadTypeHeader = 'Import Driver Data';
                    $scope.actionTypeUrl = 'services/xlUtilityDriverUpload/driverRecord';
                    break;
                case 'vehicleData':
                    $scope.uploadTypeHeader = 'Import Vehicle Data';
                    $scope.actionTypeUrl = 'services/xlUtilityVehicleUpload/vehicleRecord';
                    break;
                case 'escortData':
                    $scope.uploadTypeHeader = 'Import Escort Data';
                    $scope.actionTypeUrl = 'services/escort/escortDetails';
                    break;
                case 'nodalData':
                    $scope.uploadTypeHeader = 'Import Nodal Route';
                    $scope.actionTypeUrl = 'services/xlUtilityAreaUpload/nodalRecord';
                    break;
                case 'homeData':
                    $scope.uploadTypeHeader = 'Import Home Route';
                    $scope.actionTypeUrl = 'services/xlUtilityAreaUpload/areaRecord';
                    break;
                case 'guestData':
                    $scope.uploadTypeHeader = 'Import Guest Request';
                    $scope.actionTypeUrl = 'services/xlUtilityEmployeeUpload/guestExcelUpload';
                    break;
                case 'batchDeleteData':
                    $scope.uploadTypeHeader = 'Import Batch Delete Excel';
                    $scope.actionTypeUrl = 'services/user/empBatchdelete';
                    break;
                case 'employeeRequestManager':
                    $scope.uploadTypeHeader = 'Import Employee Request - Manager';
                    $scope.actionTypeUrl = 'services/xlUtilityEmployeeUpload/travelRequestByProjectDetails';
                    break;
                default:

            }
        }

        // SUBMIT BUTTON FUNCTION
        $scope.uploadFiles = function(value) {
            $scope.uploadDependencyLoad();
            $scope.spinnerShow = true;
            $scope.uploadButtonDisabled = true;
            $scope.UploadButtonText = "Uploading";


            var input = document.getElementById('xlf');

            var validationStatus = localStorage.getItem("validationStatus");
            var excelContentValidation = localStorage.getItem("excelContentValidation");
            if (excelContentValidation == 'empty') {
                ngDialog.open({
                    template: 'uploaded Failed. Empty excel not able to upload',
                    plain: true
                });
                $scope.spinnerShow = false;
                $scope.uploadButtonDisabled = false;
                $scope.UploadButtonText = "Upload";
                localStorage.removeItem('validationStatus');
                return false;
            } else if (validationStatus == 'Invalid') {
                ngDialog.open({
                    template: 'Unsupported File Format. Kindy upload valid excel file',
                    plain: true
                });
                $scope.spinnerShow = false;
                $scope.uploadButtonDisabled = false;
                $scope.UploadButtonText = "Upload";
                localStorage.removeItem('excelContentValidation');
                return false;
            }

            var fd = new FormData();
            fd.append("filename", $("#xlf")[0].files[0]);
            var post_url = $("#addinstgroup").attr("action");
            var postdata = $("#addinstgroup").serialize();
            var url = post_url + "?" + postdata + "&profileId=" + profileId +
                "&branchId=" + branchId + "&combinedFacility=" + combinedFacility;
            $
                .ajax({
                    url: url,
                    type: 'POST',
                    cache: false,
                    data: fd,
                    processData: false,
                    contentType: false,
                    success: function(data, textStatus, jqXHR) {},
                    complete: function(data) {
                        var response = data.responseJSON;
                        $scope.uploadButtonDisabled = false;
                        $scope.spinnerShow = false;
                        $scope.UploadButtonText = "Upload";
                        if (response.length == 0) {
                            ngDialog.open({
                                template: $scope.uploadTypeHeader + ' ' + 'successfully uploaded',
                                plain: true
                            });
                            $scope.spinnerShow = false;
                            $scope.uploadButtonDisabled = false;
                            $scope.UploadButtonText = "Upload";
                            return false;
                        } else {
                            var modalInstance = $modal
                                .open({
                                    templateUrl: 'partials/modals/uploadDataErrorModal.jsp',
                                    controller: 'importDataErrorCtrl',
                                    resolve: {
                                        result: function() {
                                            return response;
                                        },
                                        uploadTypeHeader: function() {
                                            return $scope.uploadTypeHeader;
                                        },

                                    }
                                });
                        }

                    }

                });
        };

    };

    var importDataErrorCtrl = function($scope, $modalInstance, result,
        ngDialog, $timeout, uploadTypeHeader) {
        $scope.result = result;
        $scope.resultShow = $scope.result.length;
        $scope.uploadModalHeaderData = uploadTypeHeader;

        if ($scope.resultShow > 0) {
            $scope.saveInExcel = function() {
                $scope.saveInExcel = [];

                angular.forEach($scope.result, function(item) {
                    $scope.saveInExcel.push({
                        'Row': item.RNo.split(',')[0],
                        'Column': item.RNo.split(',')[1],
                        'IssueStatus': item.IssueStatus
                    });
                });

                var sheetLabel = $scope.RNo + " " + $scope.IssueStatus;
                var opts = [{
                    sheetid: sheetLabel,
                    header: true
                }];
                var fileName = $scope.uploadModalHeaderData;
                var extenstion = '.xlsx';
                var my_filename = String(fileName.concat(extenstion));

                alasql(
                    'SELECT INTO XLSX("' + my_filename + '",?) FROM ?', [opts, [$scope.saveInExcel]]);


            };

            // CLOSE BUTTON FUNCTION
            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            };
        } else {
            ngDialog.open({
                template: $scope.uploadModalHeaderData + 'successfully uploaded',
                plain: true
            });
            /*$timeout(function() {
                $modalInstance.dismiss('cancel');
                ngDialog.close();
            }, 5000);*/

            // CLOSE BUTTON FUNCTION
            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            };
        }

    };


    angular.module('efmfmApp').controller('excelUploadCtrl', excelUploadCtrl);
    angular.module('efmfmApp').controller('importDataErrorCtrl', importDataErrorCtrl);

}());