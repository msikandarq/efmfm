(function(){
    var releasedHistoryCtrl = function($scope){
        $scope.releasedHistoryArray = [
                                {Date:'02-12-2015', 
                                 Description:'Updated Custom Markers with Numbers.',
                                 FrontEndChanges:'Directives: efmfmSingleLiveTrip.js, efmfmShowAllLiveTrips.js, efmfmShowAllLiveTrip',
                                 BackendChanges:''},
                                {Date:'02-12-2015', 
                                 Description:'Added Trafiic Layer on Maps',
                                 FrontEndChanges:'Directives: efmfmSingleLiveTrip.js, efmfmShowAllLiveTrips.js, efmfmShowAllLiveTrip',
                                 BackendChanges:''}];
        
};    
    angular.module('efmfmApp').controller('releasedHistoryCtrl', releasedHistoryCtrl);
  
}());