angular.module('efmfmApp').directive('blink', function($timeout) {
    return {
        restrict: 'E',
        transclude: true,
        scope: { 
        },
        controller: function($scope, $element,$attrs) {
            $scope.speed = $attrs.speed;
            $scope.promise;
            $scope.blinking = true;
            function showElement() {
                $element.css("visibility", "visible"); 
               $scope.promise = $timeout(hideElement,$scope.speed);
            }
            function hideElement() {
                $element.css("visibility", "hidden");
                $scope.promise =  $timeout(showElement,$scope.speed);
                
            }
            $scope.stop = function(){
                $timeout.cancel($scope.promise);
                if($scope.blinking){
                    console.log("stopping the madness...");
                    $element.css("visibility", "visible"); 
                }else{
                    console.log("starting the madness...");
                    showElement();
                }
                $scope.blinking = !$scope.blinking;
                }
            showElement();
        },
        template: '<span ng-transclude></span>',
        replace: true
    };
});