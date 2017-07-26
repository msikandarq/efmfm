//(function(){
//    angular.module('efmfmApp').directive('cleanInputField', function() {
//    return {
//     restrict: 'A',
//        require: 'ngModel',
//        link: function (scope, element, attrs, ngModel) {
//            element.bind('blur', function () {
//                console.log(ngModel);
//                scope.$apply(setAnotherValue);
//            });
//
//            function setAnotherValue() {
//                console.log(scope.fromDate);
//                ngModel.$setViewValue('1/1/1');
//                ngModel.$render();
//            }
//        }
//    };
//   });
//}());