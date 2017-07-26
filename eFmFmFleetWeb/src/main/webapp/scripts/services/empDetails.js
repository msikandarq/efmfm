(function(){
	
	var empDetailFactory = function($q, $state, $timeout, $http){	

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
    this.getEmplist = function () {
        return employeeList;
    }
	     

	};
    
    angular.module('efmfmApp').service('EmpDetailsService', empDetailFactory);

}());