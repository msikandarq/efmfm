(function(){
    angular.module('efmfmApp').directive('href', function() {
    return {
     compile: function(element) {
       element.attr('target', '_blank');
     }
    };
   });
}());