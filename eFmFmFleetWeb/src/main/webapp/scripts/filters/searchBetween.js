(function(){
   var daterange = function(){
        return function(input, from, to) {
        var result = [];
        var fromDate = new Date(from)*1000;
        var toDate = new Date(to)*1000;
            if(((isNaN(fromDate))&&(isNaN(toDate))) || ((fromDate==0) && (toDate==0))){
                return input;
            }
            else{
                if((!isNaN(fromDate))&&((isNaN(toDate))||(toDate==0))){
                  angular.forEach(input, function(item){
                    itemDate = new Date(item.fromDate)*1000;
                    if(itemDate >= fromDate){
                        result.push(item);
                    }                
                  });
                 }
                else if((isNaN(fromDate))||((fromDate==0)&&(!isNaN(toDate)))){
                    angular.forEach(input, function(item){
                    itemDate = new Date(item.toDate)*1000;
                    if(itemDate <= toDate){
                        result.push(item);
                    }                
                  });
                }
                else if((!isNaN(fromDate))&&(!isNaN(toDate))){
                    angular.forEach(input, function(item){
                    itemDatefrom = new Date(item.fromDate)*1000;
                    itemDateto = new Date(item.toDate)*1000;
                    if((itemDatefrom >= fromDate)&&(itemDateto <= toDate)){
                        result.push(item);
                     }                
                    });
                };
                
                   

                angular.forEach(result, function(item){
                    console.log("result " + item.fromDate);});
              return result;
            }
        }
     };    
    
    angular.module('efmfmApp').filter('daterange', daterange);
}());