(function(){
	
	var alertsFactory = function($q, $state, $timeout, $http){	

	//to create unique employee id
    var uid = 1;
     
    //employeeArray to hold list of all contacts
    var employeeList = [
    {
        id: 0,
        'name': 'Harish Main',
        'address': 'Irving, TX-75063',
        'gender': 'Male',
        'mobile': '4693586005',
        'email': 'hmain@newtglobal.com',
        'dob': '07/23/9999'
    },
    {
        id: 1,
        'name': 'Saima Aziz',
        'address': 'Plano, TX-75000',
        'gender': 'Female',
        'mobile': '123-456-7890',
        'email': 'saziz@newtglobal.com',
        'dob': '99/99/9999'
    }];

   //simply search employee list for given id
    //and returns the employee object if found
    this.getEmpDetails = function (id) {
        for (i in employeeList) {
            if (employeeList[i].id == id) {
                return employeeList[i];
            }
        }
 
    }    

	//simply returns the Employee Detail list
    this.getAlerts = function () {
        console.log("in the getAlerts function");
        var data = {
				   efmFmVendorMaster:{efmFmClientMaster:{clientId:clientId}}
			};
		   $http.post('services/approval/unapproveddriver/',data).
				    success(function(data, status, headers, config) {   
                        console.log("success");
//               alert(JSON.stringify(data));
				      var alertsData = data; 
                
                      var i =1;
                      var j = 10;
				      angular.forEach(alertsData, function(item) {                          
		  		    	  item.fromDate='1/'+i+'/15';
                          item.toDate='1/'+j+'/15';
                          i= i+2;
                          j= j+2;
		  	         });
                    return alertsData;
				    }).
				    error(function(data, status, headers, config) {
				      // log error
				    });
            
    }
	     

	};
    
    angular.module('efmfmApp').service('alertsFactory', alertsFactory);

}());