// Only Number

angular.module('efmfmApp')
  .directive('isNumberOnlyValid', isNumberOnlyValid);

function isNumberOnlyValid() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[0-9]|-/.test(
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}

// Only Number

angular.module('efmfmApp')
  .directive('isNumberOnlyValidWithOutZero', isNumberOnlyValidWithOutZero);

function isNumberOnlyValidWithOutZero() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[1-9]|-/.test(
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}


// Only Number with dot(.)

angular.module('efmfmApp')
  .directive('isNumberValid', isNumberValid);

function isNumberValid() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[0-9.]|-/.test(
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}

// Only Number with Comma(,)

angular.module('efmfmApp')
  .directive('isNumberWithCommaValid', isNumberWithCommaValid);

function isNumberWithCommaValid() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[0-9,+]|-/.test(
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}

// Expect Special Char

angular.module('efmfmApp')
  .directive('expectSpecialChar', expectSpecialChar);

function expectSpecialChar() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[0-9,A-Z,a-z,\s.]|-/.test(
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}

angular.module('efmfmApp')
  .directive('isNameOnlyValid', isNameOnlyValid);

function isNameOnlyValid() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[a-zA-Z\s.]|-/.test(  
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}

angular.module('efmfmApp')
    .directive('isAddressOnlyValid', isAddressOnlyValid);

  function isAddressOnlyValid() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
    
        element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[a-zA-Z\s.,()0-9/]|-/.test(  
            String.fromCharCode(event.which))
        }

        })       
    
      }
    }
  }

  angular.module('efmfmApp')
  .directive('isUserNameOnlyValid', isUserNameOnlyValid);

function isUserNameOnlyValid() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
    
      element.on('keypress', function(event) {

        if ( !isIntegerChar() ) 
          event.preventDefault();
        
        function isIntegerChar() {
          return /[0-9A-Za-z.@]|-/.test(  
            String.fromCharCode(event.which))
        }

      })       
    
    }
  }
}