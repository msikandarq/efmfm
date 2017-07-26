angular.module('efmfmApp').directive('onlyNum', function() {
    return function(scope, element, attrs) {

        var keyCode = [8, 16, 9, 37, 39, 6, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 110, 190];
        var keyCodeSpecialChar = ["!","@","#","$","%","^","&","*","(",")"]
        element.bind("keydown", function(event) {

             angular.forEach(keyCodeSpecialChar, function(value, key) {
                  
                  if($.inArray(event.which, keyCode) && event.key == value)
                  {
                    scope.$apply(function() {
                        scope.$eval(attrs.onlyNum);
                        event.preventDefault();
                    });
                    event.preventDefault();    
                  }

            }); 
            

            if ($.inArray(event.which, keyCode) === -1) {
                scope.$apply(function() {
                    scope.$eval(attrs.onlyNum);
                    event.preventDefault();
                });
                event.preventDefault();
            }
        });
    };
});