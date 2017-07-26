(function(){
    var efmfmButton = function(){
        return{
            restrict:'E',
            replace: true,            
            scope :{
                  imgClass: '@',
                  srcUrl: '@',
                  selectedUrl: '@',
                  hoverUrl: '@',
                  altText: '@',
                  mainDiv: '@',
                  targetDiv: '@',
                  },
            template: '<img class = "pointer {{imgClass}} pull-right" ng-src = "{{srcUrl}}.png" alt = "{{altText}}" />',
            link:function(scope, element, attr, fn){
                
                selected = false;
                collapse = false;
                
                element.on('mouseenter', function(event){
                    selected = true;
                    updateImage();
                });
                
                element.on('mouseleave', function(event){
                    selected = false;
                    updateImage();
                });
                
                element.on('click', function(event){
                    
                    switch(scope.altText){
                        case 'Collapse Window':
                            $('.'+scope.targetDiv).slideToggle('slow', function(){
                                                if($('.'+scope.targetDiv).is(':visible')){
                                                    element.attr('src', scope.srcUrl+'.png');
                                                    element.attr('src', selected ? scope.srcUrl+'-white.png' : scope.srcUrl+'.png');
                                                    collapse = false;    
                                                    
                                            }
                                                else if($('.'+scope.targetDiv).is(':hidden')){
                                                    element.attr('src', scope.selectedUrl+'.png');
                                                    element.attr('src', selected ? scope.selectedUrl+'-white.png' : scope.selectedUrl+'.png');
                                                    collapse = true;
                                                }
                            });
                            break; 
                            
                        case 'Reload Window':
                            break;
                            
                        case 'Remove Window':
                            $('#' + scope.mainDiv).hide('slow');                                                      
                            break;
                    }                   
                });
                
                function updateImage(){ 
                    if(!collapse){element.attr('src', selected ? scope.hoverUrl+'-white.png' : scope.srcUrl+'.png');}
                    else
                        element.attr('src', selected ? scope.selectedUrl+'-white.png' : scope.selectedUrl+'.png');                   
                }
            }
        };
    };
 
 angular.module('efmfmApp').directive('efmfmButton', efmfmButton);
    
}());