/*
@date           04/01/2015
@Author         Saima Aziz
@Description    Module for Password Validation
@Required       Form name should be 'changePasswordForm' and password input box should be name 'password

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/17/2016  Saima Aziz      Initial Creation
05/17/2016  Saima Aziz      Final Creation
*/

angular.module('UserValidation', []).directive('validPasswordC', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue, $scope) {
                var noMatch = viewValue != scope.changePasswordForm.password.$viewValue
                ctrl.$setValidity('noMatch', !noMatch)
            })
        }
    }
}).directive('smValidPasswordC', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue, $scope) {
                var noMatch = viewValue != scope.smScreenChangePasswordForm.password.$viewValue
                ctrl.$setValidity('noMatch', !noMatch)
            })
        }
    }
})