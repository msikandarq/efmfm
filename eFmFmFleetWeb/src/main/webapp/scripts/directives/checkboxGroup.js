(function(){
    angular.module('efmfmApp').directive("checkboxGroup", function() {
        return {
            restrict: "A",
            link: function(scope, elem, attrs) {
                // Determine initial checked boxes
                var x = _.findIndex(scope.user.usrroles, { userRoleId: scope.role.roleId });
                if (x !== -1) {
                    elem[0].checked = true;
                }

                // Update array on click
                elem.bind('click', function() {
                    var index = _.findIndex(scope.user.usrroles, { userRoleId: scope.role.roleId });
                    // Add if checked
                    if (elem[0].checked) {
                        if (index === -1) scope.user.usrroles.push({userRoleId:scope.role.roleId, userRole:scope.role.role});
                    }
                    // Remove if unchecked
                    else {
                        if (index !== -1) scope.user.usrroles.splice(index, 1);
                    }
                    // Sort and update DOM display
                    scope.$apply(scope.user.usrroles.sort(function(a, b) {
                        return a - b
                    }));
                });
            }
        }
    });
}());